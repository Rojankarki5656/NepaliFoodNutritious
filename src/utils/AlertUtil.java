package utils;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

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

}
