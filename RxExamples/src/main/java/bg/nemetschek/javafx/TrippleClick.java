package bg.nemetschek.javafx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import rx.observables.JavaFxObservable;
import rx.schedulers.JavaFxScheduler;

import java.util.concurrent.TimeUnit;

public final class TrippleClick extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        VBox root = new VBox();

        Label label = new Label("");
        label.setMinSize(200, 100);
        label.setFont(new Font("Arial", 30));

        Button button = new Button("Click 3");
        button.setMinSize(200, 100);

        JavaFxObservable.actionEventsOf(button)
                .buffer(500, TimeUnit.MILLISECONDS)
                .map(clicks -> clicks.size())
                .filter(count -> count >= 3)
                .observeOn(JavaFxScheduler.getInstance())
                .subscribe(i -> label.setText("Clicked"));

        root.getChildren().addAll(label, button);
        stage.setScene(new Scene(root));
        stage.show();
    }
}
