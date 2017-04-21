
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
import java.util.function.Function;

public class MultiSpeedCounter extends Application {
    @Override
    public void start(Stage stage) throws Exception {

        VBox vBox = new VBox();
        Button startButton = new Button("Start");
        Button halfButton = new Button("Half");
        Button stopButton = new Button("Stop");
        Button resetButton = new Button("Reset");

        long initial = 0;
        Function<Long, Long> incCounter = acc -> acc + 1;
        Function<Long, Long> resetCounter = acc -> initial;

        Observable<ActionEvent> start = JavaFxObservable.actionEventsOf(startButton);
        Observable<ActionEvent> half = JavaFxObservable.actionEventsOf(halfButton);
        Observable<ActionEvent> stop = JavaFxObservable.actionEventsOf(stopButton);
        Observable<ActionEvent> reset = JavaFxObservable.actionEventsOf(resetButton);

        Function<Long, Observable<Long>> interval = time -> Observable.interval(time, TimeUnit.SECONDS)
                .takeUntil(stop);
        Function<Long, Observable<Function<Long, Long>>> incOrReset = (time) -> Observable.merge(
                interval.apply(time).map(event -> incCounter),
                reset.map(event -> resetCounter)
        );

        Observable.merge(
                start.map(event -> 2L),
                half.map(event -> 1L)
        )
            .switchMap(time -> incOrReset.apply(time))
                .scan(initial, (acc, action) -> action.apply(acc))
                .subscribe(System.out::println);

        vBox.getChildren().add(startButton);
        vBox.getChildren().add(halfButton);
        vBox.getChildren().add(stopButton);
        vBox.getChildren().add(resetButton);
        stage.setScene(new Scene(vBox));
        stage.show();
    }
}

