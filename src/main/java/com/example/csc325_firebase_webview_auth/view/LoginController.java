package com.example.csc325_firebase_webview_auth.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

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
        String email = emailField.getText().trim();
        String password = passwordField.getText();

        // Validate form fields
        if (email.isEmpty() || password.isEmpty()) {
            System.out.println("Email and password must be filled out.");
            return;
        }

        // Query Firestore for user with matching email
        ApiFuture<QuerySnapshot> future = App.fstore.collection("Users").whereEqualTo("Email", email).get();

        try {
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            if (documents.size() > 0) {
                // Assuming email is unique, take the first document
                QueryDocumentSnapshot userDoc = documents.get(0);
                String storedPassword = userDoc.getString("Password");
                if (storedPassword != null && storedPassword.equals(password)) {
                    System.out.println("Login successful.");
                    // Navigate to the main application view, e.g., AccessFBView
                    try {
                        App.setRoot("/files/AccessFBView.fxml");
                    } catch (IOException e) {
                        System.out.println("Error navigating to main view: " + e.getMessage());
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("Invalid password.");
                }
            } else {
                System.out.println("User not found.");
            }
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Error querying Firestore: " + e.getMessage());
            e.printStackTrace();
        }
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
