package middleware;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class BMICalculator {

	public static void calculateBMI() {
		
		Stage bmiStage = new Stage();
        bmiStage.setTitle("Food information");
                
        TextField weightField = new TextField();
        weightField.setText("Enter your weight");
        
        TextField heightField = new TextField();
        heightField.setText("Enter your height");
        
        Button calculateButton = new Button("Filter Data");
        
        Label resultLabel = new Label();

        calculateButton.setOnAction(e ->{
        	try {
                double weight = Double.parseDouble(weightField.getText());
                double height = Double.parseDouble(heightField.getText());

                if (weight <= 0 || height <= 0) {
                    resultLabel.setText("Please enter valid positive values!");
                    return;
                }

                double bmi = weight / (height * height);
                String category;

                if (bmi < 18.5) {
                    category = "Underweight";
                } else if (bmi < 24.9) {
                    category = "Normal weight";
                } else if (bmi < 29.9) {
                    category = "Overweight";
                } else {
                    category = "Obese";
                }

                resultLabel.setText(String.format("Your BMI: %.2f (%s)", bmi, category));
            } catch (NumberFormatException ex) {
                resultLabel.setText("Invalid input! Please enter numbers.");
            }
        	
        });
        VBox bmiLayout = new VBox(10);
        bmiLayout.setPadding(new Insets(20));
        bmiLayout.getChildren().addAll(weightField,heightField,calculateButton, resultLabel);


        Scene secondaryScene = new Scene(bmiLayout, 250, 150);
        bmiStage.setScene(secondaryScene);
        bmiStage.show();
	}


}
