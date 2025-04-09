package frontend;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import middleware.ValidationLogic;  // Import LoginValidation for validation checks
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

public class Login {
    private final Scene scene;

    public Login(UIManager uiManager) {
        // Create the landing page label
        Label landingPageLabel = new Label("Welcome to NutriNepal!");
        landingPageLabel.setStyle("-fx-font-size: 50px; -fx-font-weight: bold;");

        // Load the image
        Image image = new Image(getClass().getResourceAsStream("../resources/logo.png"));
        if (image.isError()) {
            System.out.println("Error: Image not found!");
        }

         //Create an ImageView to display the image
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(200);  // Set width
        imageView.setFitHeight(400); // Set height
        imageView.setPreserveRatio(true); // Maintain aspect ratio
        imageView.setTranslateY(100); // Adjust image position

        // Create the sign-in/sign-up components
        Label titleLabel = new Label("Sign In");
        titleLabel.setStyle("-fx-font-size: 50px; -fx-font-weight: bold;");

        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        usernameLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        usernameField.setMaxWidth(300);  // Set maximum width

        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        passwordField.setMaxWidth(300);
        passwordLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Extra field for Sign Up (Hidden initially)
        Label confirmPasswordLabel = new Label("Confirm Password:");
        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordLabel.setVisible(false);
        confirmPasswordField.setVisible(false);
        confirmPasswordField.setMaxWidth(300);
        confirmPasswordLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Error Label for displaying messages
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px;"); // Style for error messages

        // Buttons
        Button actionButton = new Button("Sign In");
        Button switchButton = new Button("Create an Account");

        // Login Action
        actionButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
       
			ValidationLogic logic = new ValidationLogic(uiManager); 
			String result = logic.loginValidation(username, password);
			errorLabel.setText(result);

            // Clear fields after clicking the button
           usernameField.clear();
            passwordField.clear();
       });

       switchButton.setOnAction(e -> uiManager.showRegisterScene());


//        // Create a VBox to hold the landing page components
        VBox landingPage = new VBox(10, landingPageLabel);
       landingPage.setAlignment(Pos.CENTER);

        // Create a VBox to hold the sign-in/sign-up components
        VBox signInSignUpPage = new VBox(10, titleLabel, usernameLabel, usernameField,
                passwordLabel, passwordField, confirmPasswordLabel, confirmPasswordField,
                actionButton, switchButton, errorLabel); // Include errorLabel in the layout
        signInSignUpPage.setAlignment(Pos.CENTER);
        signInSignUpPage.setStyle("-fx-padding: 20px;");

        // Create the root layout and add the landing page initially
        StackPane root = new StackPane(landingPage, imageView);

        // Show the landing page for 2 seconds, then replace with the sign-in/sign-up page
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(e -> {
            root.getChildren().clear();
            root.getChildren().add(signInSignUpPage);
        });
        pause.play();  // Start the pause transition

        // Scene & Stage
        scene = new Scene(root, 800, 600);
    }

    public Scene getScene() {
        return scene;
    }
}