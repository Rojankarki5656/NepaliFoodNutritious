package utils;

import java.util.Optional;

import Models.User_Target;
import backend.TargetService;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import middleware.NutritiousSectionLogic;

public class AlertUtil {
	
    public static void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public static boolean createAlert(String text) {
      	 Alert alert = new Alert(Alert.AlertType.INFORMATION);
   	    alert.setTitle("Message");
   	    alert.setHeaderText(null); // No header
   	    alert.setContentText(text);

   	    // Add custom button
   	    ButtonType resetButton = new ButtonType("Reset Your Target");

   	    alert.getButtonTypes().setAll(resetButton);

   	    // Show alert and wait for response
   	    Optional<ButtonType> click = alert.showAndWait();

   	    if (click.isPresent() && click.get() == resetButton) {
   	    		return true;
   	        }
   	    return false;
       }
    
    public static boolean showConfirmation() {
    	Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Message");
        alert.setHeaderText(null); // No header
        alert.setContentText("Do you want to add in your calories taken of your target");

        // Customize button types
        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");

        alert.getButtonTypes().setAll(yesButton, noButton);

        // Show the alert and wait for response
        Optional<ButtonType> result = alert.showAndWait();
        // Run method if "Yes" is clicked
        if (result.isPresent() && result.get() == yesButton) {
            return true;
        } else {
        	return false;
        }
    }

}
