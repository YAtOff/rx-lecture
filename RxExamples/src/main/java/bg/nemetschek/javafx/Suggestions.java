package bg.nemetschek.javafx;

import com.sun.deploy.util.StringUtils;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
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
        listView.setMinSize(200, 500);
        List<String> states = Arrays.asList(getResponse("https://goo.gl/S0xuOi").split("\\r?\\n"));

        TextField inputBox = new TextField();
        inputBox.setMinSize(200, 50);
        inputBox.setFont(new Font("Arial", 20));

        Function<String, Observable<List<String>>> getStates = (String search) -> Observable.just(
            states.stream().filter(st -> st.toUpperCase().startsWith(search.toUpperCase())).collect(Collectors.toList())
        )
            .delay(500, TimeUnit.MILLISECONDS, Schedulers.io());

        Observable<String> typedWords = JavaFxObservable.valuesOf(inputBox.textProperty())
            .publish().refCount();

        typedWords
            .doOnNext(word -> System.out.println("w..."))
            .debounce(1000, TimeUnit.MILLISECONDS).startWith("")
            .doOnNext(word -> System.out.println(".d.."))
            .filter(word -> word.length() > 2)
            .doOnNext(word -> System.out.println("..f."))
            .switchMap(word ->
                getStates.apply(word)
                    .doOnNext(w -> System.out.println("...r"))
                    .observeOn(JavaFxScheduler.getInstance())
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
