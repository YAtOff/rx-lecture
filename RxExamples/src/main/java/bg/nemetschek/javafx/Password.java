package bg.nemetschek.javafx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import jdk.nashorn.internal.runtime.ECMAException;
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
        Button notRobotButton = new Button("I'm not a robot");
        notRobotButton.setMinSize(200, 100);
        notRobotButton.setDisable(true);

        JavaFxObservable.actionEventsOf(checkButton)
                .flatMap(attempt -> checkPassword(passwordInput.getText()))
                .doOnError(e -> errorLabel.setText("Wrong!"))
                .retry(3)
                .doOnError(e -> {
                    passwordInput.setDisable(true);
                    notRobotButton.setDisable(false);
                })
                .onErrorResumeNext(t ->
                        JavaFxObservable.actionEventsOf(notRobotButton)
                            .doOnNext(ae -> {
                                passwordInput.setDisable(false);
                                notRobotButton.setDisable(true);
                            })
                            .flatMap(ae -> Observable.error(new Exception("Wring password!"))))
                .retry()
                .subscribe(i -> errorLabel.setText("Success"));

        vBox.getChildren().addAll(passwordInput, checkButton, notRobotButton, errorLabel);

        stage.setScene(new Scene(vBox));
        stage.show();
    }

    private Observable<Object> checkPassword(String password) {
        return (password.equals("123456"))
                ? Observable.just(null)
                : Observable.error(new Exception("Wring password!"));
    }
}
