package bg.nemetschek.theory.binding;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public final class ChangePropagation extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        VBox root = new VBox();
        TextField input = new TextField();
        input.setMinSize(200, 100);
        input.setFont(new Font("Arial", 25));
        Label result = new Label("");
        result.setMinSize(200, 100);
        result.setFont(new Font("Arial", 25));

        input.textProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.matches("\\d+")) {
                    result.setText(String.valueOf(Integer.valueOf(newValue) * 2));
                } else {
                    result.setText("");
                }
        });

        root.getChildren().addAll(input, result);
        stage.setScene(new Scene(root));
        stage.show();
    }
}
