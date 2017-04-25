package bg.nemetschek.javafx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import rx.Observable;
import rx.observables.JavaFxObservable;

public final class ConfirmDialog extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        Button runButton = new Button("Run Process");

        JavaFxObservable.actionEventsOf(runButton)
            .flatMap(ae ->
                JavaFxObservable.fromDialog(new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to run the process?"))
                    .filter(response -> response.equals(ButtonType.OK))
            ).flatMap(response -> Observable.range(1,10).toList())
            .subscribe(i -> System.out.println("Processed integer list: " + i));

        VBox root = new VBox();
        root.getChildren().add(runButton);

        stage.setScene(new Scene(root));

        stage.show();
    }
}
