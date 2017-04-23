package bg.nemetschek.javafxexample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import rx.Observable;
import rx.observables.JavaFxObservable;

public final class Hot extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        VBox vBox = new VBox();
        Button button = new Button("Press Me");
        Button secondSubButton = new Button("Subscribe Subscriber 2");

        Observable<ActionEvent> clicks =
            JavaFxObservable.actionEventsOf(button);

        //Subscriber 1
        clicks.subscribe(ae ->
            System.out.println("Subscriber 1 Received Click!"));

        //Subscribe Subscriber 2 when secondSubButton is clicked
        secondSubButton.setOnAction(event -> {
                System.out.println("Subscriber 2 subscribing!");
                secondSubButton.disableProperty().set(true);
                //Subscriber 2
                clicks.subscribe(ae ->
                    System.out.println("Subscriber 2 Received Click!")
                );
            });

        vBox.getChildren().addAll(button,secondSubButton);

        stage.setScene(new Scene(vBox));
        stage.show();
    }
}
