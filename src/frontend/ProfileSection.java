package frontend;
import Models.User;
import backend.UserService;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import middleware.ValidationLogic;
import utils.AlertUtil;

public class ProfileSection{

    private UIManager uiManager;
    private Scene scene;
    private VBox root;
	private User user;

	public ProfileSection(UIManager uiManager, User user) {
    	
    	this.uiManager = uiManager;
    	this.user = user;
        root = new VBox(10);
        root.setPadding(new Insets(20));
        this.scene = new Scene(root, 600, 500);  	
    	showProfile();
    	
	}
    	
    private void showProfile() {
        // Title
        Label titleLabel = new Label("Profile");
        titleLabel.setFont(new Font("Arial", 24));
        titleLabel.setTextFill(Color.web("#2c3e50"));

        // Name
        Label nameLabel = new Label("Name: "+ user.getName());
        // Email
        Label emailLabel = new Label("Email: " + user.getEmail());
        // Dietary Preference
        Label dietLabel = new Label("Dietary Preference: "+ user.getUserPreference());

        // Update Button
        Button updateBtn = new Button("Edit Profile");
        updateBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold;");
        updateBtn.setOnAction(e -> {
        	updateProfile();
        });
        root.getChildren().addAll(titleLabel,nameLabel,emailLabel,dietLabel,updateBtn);
    }

	public VBox getRoot() {
		return root;
	}
	
	private void updateProfile() {
		root.getChildren().clear();
        Label message = new Label();

        Label titleLabel = new Label("Profile");
        titleLabel.setFont(new Font("Arial", 24));
        titleLabel.setTextFill(Color.web("#2c3e50"));

        // Name
        Label nameLabel = new Label("Name: ");
        TextField nameField = new TextField();
        nameField.setPromptText("Enter your name");
        Button nameUpdateBtn = new Button("Update Name");
        
        nameUpdateBtn.setOnAction(e ->{
        	String name = nameField.getText();
            	String result = new ValidationLogic(uiManager).updateProfileValidation(name, user.getId(), "name");
            	message.setText(result);
        		
        	
        });


        // Password
        Label passwordLabel = new Label("Password: ");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter new password");
        Button passwordUpdateBtn = new Button("Update Password");
        passwordUpdateBtn.setOnAction(e ->{
        	String password = passwordField.getText();

            	String result = new ValidationLogic(uiManager).updateProfileValidation(password, user.getId(), "password");
            	message.setText(result);
        });


        // Dietary Preference
        Label dietLabel = new Label("Dietary Preference: ");
        ComboBox<String> dietCombo = new ComboBox<>();
        dietCombo.getItems().addAll("veg", "non-veg");
        dietCombo.setPromptText("Select Preference");
        Button updatePreferenceBtn = new Button("Update Preference");
        updatePreferenceBtn.setOnAction(e ->{
        	String preference = dietCombo.getValue();
            	String result = new ValidationLogic(uiManager).updateProfileValidation(preference, user.getId(), "preference");
            	message.setText(result);
        });


        // Update Button
        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> {
            root.getChildren().clear();
        	showProfile();
        });

        root.getChildren().addAll(titleLabel,
                nameLabel, nameField,nameUpdateBtn,
                passwordLabel, passwordField, passwordUpdateBtn,
                dietLabel, dietCombo,updatePreferenceBtn,
                backBtn, message);
		
		}

}
