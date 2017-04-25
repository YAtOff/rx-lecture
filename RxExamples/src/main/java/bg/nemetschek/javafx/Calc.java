package bg.nemetschek.javafx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import rx.Observable;
import rx.observables.JavaFxObservable;

public final class Calc extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        VBox root = new VBox();

        Label label = new Label("Input Number");
        label.setFont(new Font("Arial", 25));
        label.setMinSize(200, 100);
        TextField input = new TextField();
        input.setFont(new Font("Arial", 25));
        input.setMinSize(200, 100);
        Label totalLabel = new Label();
        totalLabel.setFont(new Font("Arial", 25));
        totalLabel.setMinSize(200, 100);

        Button button = new Button("Add to Total");

        Observable<Integer> proactive = JavaFxObservable.actionEventsOf(button)
                .map(ae -> input.getText())
                .filter(s -> s.matches("[0-9]+"))
                .map(Integer::valueOf)
                .scan(0,(x,y) -> x + y);

        Observable<Integer> errorHandling = JavaFxObservable.actionEventsOf(button)
            .map(ae -> Integer.valueOf(input.getText()))
            .scan(0,(x,y) -> x + y)
            .doOnError( e -> new Alert(Alert.AlertType.ERROR, e.getMessage()).show())
            .retry();

        errorHandling
        //proactive
            .subscribe(i -> {
                totalLabel.setText(i.toString());
                input.clear();
            });

        root.getChildren().setAll(label,input, totalLabel, button);
        stage.setScene(new Scene(root));
        stage.show();
    }
}
