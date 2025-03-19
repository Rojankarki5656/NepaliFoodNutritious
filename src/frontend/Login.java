package frontend;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import backend.UserService;

public class Login {
    private final Scene scene;

    public Login(UIManager uiManager) {
        Label titleLabel = new Label("Login");
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

            User loggedInUser = UserService.getUserInfo(email, password);
            if (loggedInUser != null) {
                messageLabel.setText("Login Successful!");
                uiManager.showDashboard(loggedInUser);
            } else {
            	messageLabel.setText("Invalid Email or Password!");
            }


        });

        registerButton.setOnAction(e -> uiManager.showRegisterScene());

        VBox layout = new VBox(10, titleLabel, emailField, passwordField, loginButton, registerButton, messageLabel);
        layout.setStyle("-fx-padding: 20px; -fx-alignment: center;");

        scene = new Scene(layout, 300, 300);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

    }

    public Scene getScene() {
        return scene;
    }
}
