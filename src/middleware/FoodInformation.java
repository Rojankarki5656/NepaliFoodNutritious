package middleware;

import java.util.List;
import java.util.Map;

import backend.FoodService;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class FoodInformation {
    private static TextArea resultArea;

	
	public static void showFoodInfo(String foodName) {
		Map<Float, List<Float>> foodData = FoodService.getFoodCalories(foodName);
        StringBuilder resultText = new StringBuilder();
		
        Stage secondaryStage = new Stage();
        secondaryStage.setTitle("Food information");
        
        StackPane secondaryLayout = new StackPane();

        resultArea = new TextArea();
        resultArea.setEditable(false);
        resultArea.setPrefHeight(150);
        secondaryLayout.getChildren().add(resultArea);
         
        Scene secondaryScene = new Scene(secondaryLayout, 250, 150);
        secondaryStage.setScene(secondaryScene);
        secondaryStage.show();
        for (Map.Entry<Float, List<Float>> entry : foodData.entrySet()) {
            resultText.append("Calories: ").append(entry.getKey())
                      .append("\nProteins: ").append(entry.getValue().get(0))
                      .append("\nFats: ").append(entry.getValue().get(1))
                      .append("\nCarbohydrates: ").append(entry.getValue().get(2))
                      .append("\n-----------------\n");
        }

        resultArea.setText(resultText.toString());
	}

}
