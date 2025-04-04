package frontend;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import middleware.ValidationLogic;  // Import LoginValidation for validation checks

public class Login {
    private final Scene scene;

    public Login(UIManager uiManager) {
        Label titleLabel = new Label("Login");

        // Load image
        Image logo = new Image(getClass().getResource("../resources/logo.png").toExternalForm());
        ImageView logoView = new ImageView(logo);
        logoView.setFitWidth(100); // Set width
        logoView.setPreserveRatio(true); // Maintain aspect ratio

        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Button loginButton = new Button("Login");
        Button registerButton = new Button("Register");
        Label messageLabel = new Label();

        // Set Login Button Action
        loginButton.setOnAction(e -> {
            String email = emailField.getText();
            String password = passwordField.getText();        
            ValidationLogic logic = new ValidationLogic(uiManager); 
            String result = logic.loginValidation(email, password);
            messageLabel.setText(result);
        });

        registerButton.setOnAction(e -> uiManager.showRegisterScene());


        VBox layout = new VBox(10, logoView, titleLabel, emailField, passwordField, loginButton, registerButton, messageLabel);
        layout.setStyle("-fx-padding: 20px; -fx-alignment: center;");

        scene = new Scene(layout, 1550, 800);
        scene.getStylesheets().add(getClass().getResource("../resources/style.css").toExternalForm());
    }

    public Scene getScene() {
        return scene;
    }
}
