package bg.nemetschek.javafxexample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import rx.Observable;
import rx.observables.JavaFxObservable;
import rx.schedulers.JavaFxScheduler;
import rx.schedulers.Schedulers;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public final class Search  extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        VBox root = new VBox();

        //Declare a ListView with all U.S. states
        ListView<String> listView = new ListView<>();
        List<String> states = Arrays.asList(getResponse("https://goo.gl/S0xuOi").split("\\r?\\n"));

        //broadcast typed keys
        Observable<String> typedKeys = JavaFxObservable.eventsOf(listView, KeyEvent.KEY_TYPED)
                .map(KeyEvent::getCharacter)
                .publish().refCount();

        //immediately jump to state being typed
        typedKeys.debounce(1000, TimeUnit.MILLISECONDS).startWith("")
            .switchMap(s ->
                typedKeys.scan((x,y) -> x + y)
                    .delay(1000, TimeUnit.MILLISECONDS, Schedulers.io())
                    .switchMap(input ->
                        Observable.from(states)
                            .filter(st -> st.toUpperCase().startsWith(input.toUpperCase()))
                    )
                    .scan(new ArrayList<String>(), (matching, state) -> {
                        matching.add(state);
                        return matching;
                    })
                ).observeOn(JavaFxScheduler.getInstance())
                .subscribe(matching -> {
                    if (matching.size() > 0) {
                        listView.getItems().setAll(matching);
                    }
                });

        root.getChildren().add(listView);

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
