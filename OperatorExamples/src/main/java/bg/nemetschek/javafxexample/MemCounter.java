package bg.nemetschek.javafxexample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import rx.Observable;
import rx.observables.JavaFxObservable;

import java.util.concurrent.TimeUnit;

public class MemCounter extends Application {
    @Override
    public void start(Stage stage) throws Exception {

        VBox vBox = new VBox();
        Button startButton = new Button("Start");
        Button stopButton = new Button("Stop");

        Observable<ActionEvent> start =  JavaFxObservable.actionEventsOf(startButton);
        Observable<ActionEvent> stop =  JavaFxObservable.actionEventsOf(stopButton);
        Observable<Long> interval = Observable.interval(1, TimeUnit.SECONDS)
                .takeUntil(stop);
        start.switchMap(event -> interval)
                .startWith(0L)
                .scan((acc, val) -> acc + 1)
                .subscribe(System.out::println);

        vBox.getChildren().add(startButton);
        vBox.getChildren().add(stopButton);
        stage.setScene(new Scene(vBox));
        stage.show();
    }
}

