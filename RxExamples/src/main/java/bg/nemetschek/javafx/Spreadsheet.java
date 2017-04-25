package bg.nemetschek.javafx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import rx.Observable;
import rx.observables.JavaFxObservable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public final class Spreadsheet extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        VBox root = new VBox();

        TextField input1 = new TextField();
        input1.setFont(new Font("Arial", 25));
        input1.setMinSize(200, 100);
        TextField input2 = new TextField();
        input2.setFont(new Font("Arial", 25));
        input2.setMinSize(200, 100);
        TextField operator = new TextField();
        operator.setFont(new Font("Arial", 25));
        operator.setMinSize(200, 100);
        Label result = new Label();
        result.setFont(new Font("Arial", 25));
        result.setMinSize(200, 100);

        Map<String, BiFunction<Integer, Integer, Integer>> operations = new HashMap<>();
        operations.put("+", (x, y) -> x + y);
        operations.put("-", (x, y) -> x - y);
        operations.put("*", (x, y) -> x * y);

        Observable.combineLatest(
                JavaFxObservable.valuesOf(input1.textProperty()).filter(s -> s.matches("\\d+")).map(Integer::valueOf),
                JavaFxObservable.valuesOf(input2.textProperty()).filter(s -> s.matches("\\d+")).map(Integer::valueOf),
                JavaFxObservable.valuesOf(operator.textProperty()).filter(s -> s.matches("[-+*]")),
                (x, y, op) -> operations.get(op).apply(x, y).toString()
        )
                .subscribe(result::setText);

        root.getChildren().setAll(input1, input2, operator, result);
        stage.setScene(new Scene(root));
        stage.show();
    }
}
