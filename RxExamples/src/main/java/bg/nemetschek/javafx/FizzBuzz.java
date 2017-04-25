package bg.nemetschek.javafx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import rx.observables.JavaFxObservable;

public final class FizzBuzz  extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        VBox vBox = new VBox();
        Button button = new Button("Press Me");
        button.setMinSize(200, 100);
        Label itemLabel = new Label("0");
        itemLabel.setMinSize(200, 100);
        itemLabel.setFont(new Font("Arial", 25));
        Label doneLabel = new Label("");
        doneLabel.setMinSize(200, 100);
        doneLabel.setFont(new Font("Arial", 25));

        JavaFxObservable.actionEventsOf(button)
                .scan(1, (x, y) -> x + 1)
                .map(this::fizzBuzz)
                .takeUntil(val -> val.equals("FizzBuzz"))
                .subscribe(
                    itemLabel::setText,
                    Throwable::printStackTrace,
                    () -> doneLabel.setText("Done!")
                );

        vBox.getChildren().addAll(itemLabel, doneLabel,button);

        stage.setScene(new Scene(vBox));
        stage.show();
    }

    private String fizzBuzz(int x) {
       String fb = (x % 3 == 0 ? "Fizz" : "") + (x % 5 == 0 ? "Buzz" : "");
       return fb.equals("") ? String.valueOf(x) : fb;
    }
}
