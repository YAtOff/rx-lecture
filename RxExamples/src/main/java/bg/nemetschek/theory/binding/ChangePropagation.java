package bg.nemetschek.theory.binding;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public final class ChangePropagation extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        VBox root = new VBox();
        TextField input = new TextField();
        Label result = new Label("");

        input.textProperty().addListener(
                new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                        if (newValue.matches("\\d+")) {
                            result.setText(String.valueOf(Integer.valueOf(newValue) * 2));
                        }
                    }
                });

                root.getChildren().addAll(input, result);
        stage.setScene(new Scene(root));
        stage.show();
    }
}
