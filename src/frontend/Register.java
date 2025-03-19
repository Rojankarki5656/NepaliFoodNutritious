package frontend;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import backend.UserService;

public class Register {
    private final Scene scene;

    public Register(UIManager uiManager) {
        Label titleLabel = new Label("Register");
        TextField nameField = new TextField();
        nameField.setPromptText("Full Name");
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm Password");
        Button registerButton = new Button("Register");
        Button backButton = new Button("Back to Login");
        
        registerButton.setOnAction(e -> {
        	String name = nameField.getText();
        	String email = emailField.getText();
        	String password =passwordField.getText();
        	String confirmPassword = confirmPasswordField.getText();
        	System.out.println(password);
        	System.out.println(confirmPassword);
        	if (password.equals(confirmPassword)) {
        		UserService.register(name, email, password);
        		uiManager.showLoginScene();
        		
        	}else {
        		System.out.println("Possword dont match");
        	}
        	
        });

        backButton.setOnAction(e -> uiManager.showLoginScene());

        VBox layout = new VBox(10, titleLabel, nameField, emailField, passwordField, confirmPasswordField, registerButton, backButton);
        layout.setStyle("-fx-padding: 20px; -fx-alignment: center;");

        scene = new Scene(layout, 300, 300);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

    }

    public Scene getScene() {
        return scene;
    }
}
