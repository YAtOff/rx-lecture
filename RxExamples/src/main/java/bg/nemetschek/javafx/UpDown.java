package bg.nemetschek.javafx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import rx.Observable;
import rx.observables.JavaFxObservable;

public final class UpDown extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        VBox root = new VBox();

        Label label = new Label("0");
        label.setMinSize(200, 100);
        label.setFont(new Font("Arial", 30));

        Button buttonUp = new Button("UP");
        buttonUp.setMinSize(200, 100);
        Button buttonDown = new Button("DOWN");
        buttonDown.setMinSize(200, 100);

        Observable.merge(
            JavaFxObservable.actionEventsOf(buttonUp).map(ae -> 1),
            JavaFxObservable.actionEventsOf(buttonDown).map(ae -> -1)
        )
            .scan(0, (x, y) -> x + y)
            .subscribe(i -> label.setText(i.toString()));

        root.getChildren().addAll(label,buttonUp,buttonDown);
        stage.setScene(new Scene(root));
        stage.show();
    }
}
