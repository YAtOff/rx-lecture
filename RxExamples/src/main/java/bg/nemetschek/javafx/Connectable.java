package bg.nemetschek.javafx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import rx.Observable;
import rx.observables.JavaFxObservable;

import java.time.LocalDateTime;

public final class Connectable extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        VBox vBox = new VBox();
        Button button = new Button("Press Me");
        button.setMinSize(200, 100);
        Button secondSubButton = new Button("Subscribe Subscriber 2");
        secondSubButton.setMinSize(200, 100);

        Observable<LocalDateTime> clicks = JavaFxObservable.actionEventsOf(button)
                .map(ae -> LocalDateTime.now())
                .doOnNext(d -> System.out.println("tick"))
                .publish().refCount();

        //Subscriber 1
        clicks.subscribe(d -> System.out.println("Subscriber 1 Received Click at " + d));

        //Subscribe Subscriber 2 when secondSubButton is clicked
        secondSubButton.setOnAction(event -> {
            System.out.println("Subscriber 2 subscribing!");
            secondSubButton.disableProperty().set(true);
            //Subscriber 2
            clicks.subscribe(d -> System.out.println("Subscriber 2 Received Click at " + d));
        });

        vBox.getChildren().addAll(button,secondSubButton);

        stage.setScene(new Scene(vBox));
        stage.show();
    }
}
