package com.example.csc325_firebase_webview_auth.view;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.util.Duration;
import java.io.IOException;

public class SplashScreenController {

    @FXML
    public void initialize() {
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(event -> {
            try {
                App.setRoot("/files/login-form.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        pause.play();
    }
}
