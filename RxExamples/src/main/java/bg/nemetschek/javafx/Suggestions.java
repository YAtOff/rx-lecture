package bg.nemetschek.javafx;

import com.sun.deploy.util.StringUtils;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import rx.Observable;
import rx.observables.JavaFxObservable;
import rx.schedulers.JavaFxScheduler;
import rx.schedulers.Schedulers;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class Suggestions extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        VBox root = new VBox();

        //Declare a ListView with all U.S. states
        ListView<String> listView = new ListView<>();
        List<String> states = Arrays.asList(getResponse("https://goo.gl/S0xuOi").split("\\r?\\n"));

        TextField inputBox = new TextField();

        Function<String, Observable<List<String>>> getStates = (String search) -> Observable.just(
            states.stream().filter(st -> st.toUpperCase().startsWith(search.toUpperCase())).collect(Collectors.toList())
        )
            .delay(500, TimeUnit.MILLISECONDS, Schedulers.io());

        Observable<String> typedWords = JavaFxObservable.valuesOf(inputBox.textProperty())
            .publish().refCount();

        //immediately jump to state being typed
        typedWords.debounce(1000, TimeUnit.MILLISECONDS).startWith("")
            .switchMap(s ->
                typedWords
                    .filter(w -> w.length() > 2)
                    .switchMap(input ->
                        getStates.apply(input)
                        .observeOn(JavaFxScheduler.getInstance())
                    )
            ).observeOn(JavaFxScheduler.getInstance())
            .subscribe(matching -> listView.getItems().setAll(matching));

        root.getChildren().addAll(inputBox, listView);

        stage.setScene(new Scene(root));

        stage.show();
    }

    private static String getResponse(String path) {
        try {
            return new Scanner(new URL(path).openStream(), "UTF-8").useDelimiter("\\A").next();
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
