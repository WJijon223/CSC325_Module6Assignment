package com.example.csc325_firebase_webview_auth.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Hyperlink registerLink;

    @FXML
    private void handleLogin() {
        // Handle login logic here
        System.out.println("Login button clicked");
    }

    @FXML
    private void handleRegisterLink() {
        try {
            App.setRoot("/files/registration-form.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
