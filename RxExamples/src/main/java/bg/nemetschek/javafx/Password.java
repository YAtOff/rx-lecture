package bg.nemetschek.javafx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import rx.Observable;
import rx.observables.JavaFxObservable;
import rx.schedulers.JavaFxScheduler;
import rx.schedulers.Schedulers;
import sun.rmi.server.InactiveGroupException;

public final class Password extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        VBox vBox = new VBox();

        TextField passwordInput = new TextField();
        passwordInput.setFont(new Font("Arial", 25));
        passwordInput.setMinSize(200, 100);
        Label errorLabel = new Label();
        errorLabel.setFont(new Font("Arial", 25));
        errorLabel.setMinSize(200, 100);
        Button checkButton = new Button("Check");
        checkButton.setMinSize(200, 100);

        JavaFxObservable.actionEventsOf(checkButton)
                .flatMap(attempt -> {
                    String password = passwordInput.getText();
                    return (password.equals("123456"))
                            ? Observable.just("user")
                            : Observable.error(new Exception("Wring password!"));
                })
                .doOnError(e -> errorLabel.setText("Wrong!"))
                .retry(3)
                .doOnError(e -> passwordInput.setDisable(true))
                .subscribe(i -> errorLabel.setText("Success"));

        vBox.getChildren().addAll(passwordInput, checkButton, errorLabel);

        stage.setScene(new Scene(vBox));
        stage.show();
    }
}
