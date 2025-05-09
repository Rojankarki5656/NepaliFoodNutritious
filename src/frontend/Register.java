package frontend;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import middleware.ValidationLogic;
import backend.UserService;

public class Register {
    private final Scene scene;

    public Register(UIManager uiManager) {
        Label titleLabel = new Label("Register");
        TextField nameField = new TextField();
        nameField.setPromptText("Full Name");
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        ComboBox<String> dietCombo = new ComboBox<>();
        dietCombo.getItems().addAll("veg", "non-veg");
        dietCombo.setPromptText("Select Preference");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm Password");
        Button registerButton = new Button("Register");
        Button backButton = new Button("Back to Login");
        Label messageLabel = new Label();

        
        registerButton.setOnAction(e -> {
        	String name = nameField.getText();
        	String email = emailField.getText();
        	String preference = dietCombo.getValue();
        	String password =passwordField.getText();
        	String confirmPassword = confirmPasswordField.getText();
        	ValidationLogic logic = new ValidationLogic(uiManager);
        	String result = logic.registerValidation(name, email,preference, password, confirmPassword);
        	messageLabel.setText(result);
        	
        });

        backButton.setOnAction(e -> uiManager.showLoginScene());

        VBox layout = new VBox(10, titleLabel, nameField, emailField,dietCombo, passwordField, confirmPasswordField, registerButton, backButton, messageLabel);
        layout.setStyle("-fx-padding: 20px; -fx-alignment: center;");

        scene = new Scene(layout, 1550, 800);
        scene.getStylesheets().add(getClass().getResource("../resources/style.css").toExternalForm());

    }

    public Scene getScene() {
        return scene;
    }
}
