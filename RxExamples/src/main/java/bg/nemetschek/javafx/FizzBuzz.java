package bg.nemetschek.javafx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import rx.observables.JavaFxObservable;

public final class FizzBuzz  extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        VBox vBox = new VBox();
        Button button = new Button("Press Me");
        Label itemLabel = new Label("0");
        Label doneLabel = new Label("");

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
