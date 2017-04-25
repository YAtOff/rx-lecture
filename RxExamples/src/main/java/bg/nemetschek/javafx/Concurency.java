package bg.nemetschek.javafx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import rx.Observable;
import rx.observables.JavaFxObservable;
import rx.schedulers.JavaFxScheduler;
import rx.schedulers.Schedulers;


public final class Concurency extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        VBox vBox = new VBox();
        Button processStarter = new Button("Start process");
        processStarter.setMinSize(200, 100);
        Button button = new Button("Click");
        button.setMinSize(200, 100);

        processStarter.setOnAction(event -> {
            processStarter.setText("Working ...");
            Observable.fromCallable(() -> {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return 42;
            })
                    .subscribeOn(Schedulers.computation())
                    .observeOn(JavaFxScheduler.getInstance())
                    .subscribe(ae -> processStarter.setText("Ready!"));
        });

        JavaFxObservable.actionEventsOf(button)
                .map(ae -> Math.random())
                .subscribe(i -> button.setText(i.toString()));

        vBox.getChildren().addAll(processStarter, button);

        stage.setScene(new Scene(vBox));
        stage.show();
    }
}
