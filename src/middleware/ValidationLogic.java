package middleware;

import frontend.UIManager;
import javafx.stage.Stage;
import Models.User;
import backend.UserService;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationLogic {

    private final UIManager uiManager;

    // Constructor to get UIManager instance
    public ValidationLogic(UIManager uiManager) {
        this.uiManager = uiManager;
    }

    public String loginValidation(String email, String password) {
        // Validate empty fields
        if (email.isEmpty() || password.isEmpty()) {
            return "ERROR: Email or password cannot be empty!";
        }
        if (!isValidEmail(email)) {
            return "Invalid email format. Please enter a valid email.";
        }

        // Validate Password
        String passwordValidationMessage = validatePassword(password);
        if (!passwordValidationMessage.equals("Password is valid.")) {
            return passwordValidationMessage; // Show password validation error
             // Stop further processing if password is invalid
        }
        // Get user from UserService
        User loggedInUser = UserService.getUserInfo(email, password);
        if (loggedInUser != null) {
        	uiManager.showDashboard(loggedInUser);
            return "Login Successful!";
        } else {
            return "Invalid Email or Password!";
        } 
    }
    
    public boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        email = email.trim(); // Trim spaces before validation

        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        
        return matcher.matches();
    }


    
    public String validatePassword(String password) {
        // Check if password length is less than 8 characters
        if (password.length() < 8) {
            return "Password must be at least 8 characters long.";
        }

        // Check if password contains at least one capital letter
        if (!password.matches(".*[A-Z].*")) {
            return "Password must contain at least one capital letter.";
        }

        // Check if password contains at least one symbol
        if (!password.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
            return "Password must contain at least one symbol.";
        }

        return "Password is valid."; // Password meets all criteria
    }
    
    public String registerValidation(String name, String email, String password, String confirmPassword) {
    	
        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            return "ERROR: Email or password or confirm password cannot be empty!";
        }
        if (!isValidEmail(email)) {
            return "Invalid email format. Please enter a valid email.";
        }

        // Validate Password
        String passwordValidationMessage = validatePassword(password);
        if (!passwordValidationMessage.equals("Password is valid.")) {
            return passwordValidationMessage; // Show password validation error
             // Stop further processing if password is invalid
        }
    	if(password.equals(confirmPassword)) {
            UserService.register(name,email, password);
            uiManager.showLoginScene();
            return "Register Successful!";
       }else {
    		return"Password and confirm password do not match";
    	}
		
    	
    	
    }
    
}
