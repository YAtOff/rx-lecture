package bg.nemetschek.javafx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
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
        TextField input2 = new TextField();
        TextField operator = new TextField();
        Label result = new Label();

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
