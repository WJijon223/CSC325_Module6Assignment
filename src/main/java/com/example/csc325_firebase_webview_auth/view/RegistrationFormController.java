package com.example.csc325_firebase_webview_auth.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import com.example.csc325_firebase_webview_auth.model.User;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;

public class RegistrationFormController {

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Button registerButton;

    @FXML
    private Hyperlink loginLink;

    @FXML
    private void handleRegister() {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Validate form fields
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            System.out.println("All fields must be filled out.");
            return;
        }

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            System.out.println("Passwords do not match.");
            return;
        }

        // Create User model
        User user = new User(firstName, lastName, email, password);

        // Create user in Firebase Auth
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(user.getEmail())
                .setPassword(user.getPassword()) // Note: In production, handle password securely
                .setDisplayName(user.getFirstName() + " " + user.getLastName())
                .setEmailVerified(false);

        try {
            UserRecord userRecord = App.fauth.createUser(request);
            System.out.println("Successfully created new user in Auth: " + userRecord.getUid());

            // Insert user data into Firestore
            DocumentReference docRef = App.fstore.collection("Users").document(UUID.randomUUID().toString());
            Map<String, Object> data = new HashMap<>();
            data.put("First Name", user.getFirstName());
            data.put("Last Name", user.getLastName());
            data.put("Email", user.getEmail());
            data.put("Password", user.getPassword()); // Note: Storing password in Firestore is insecure; consider removing in production

            ApiFuture<WriteResult> result = docRef.set(data);
            result.get(); // Wait for the operation to complete
            System.out.println("User data inserted into Firestore successfully.");

        } catch (FirebaseAuthException e) {
            System.out.println("Registration failed: " + e.getMessage());
            // Firebase Auth will throw an exception if the email is already in use
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Error inserting data into Firestore: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLoginLink() {
        try {
            App.setRoot("/files/login-form.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
