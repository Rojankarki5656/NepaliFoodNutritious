package frontend;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import middleware.CaloriesCalculator;
import middleware.NutritiousSectionLogic;
import utils.AlertUtil;
import javafx.geometry.Insets;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import Models.FoodItems;
import Models.User_Target;
import Models.User;
import backend.FoodService;
import backend.TargetService;

public class NutritiousSection {
    private PieChart pieChart;
    private BarChart<String, Number> barChart;
    private TextField foodInput, quantityInput;
    private final Scene scene;
    private final UIManager uiManager;
    private VBox root;
    private Label calorieResultLabel,totalCalories;

    public NutritiousSection(UIManager uiManager, User user) {
        this.uiManager = uiManager;
        root = new VBox(15);
        root.setPadding(new Insets(20));
        
        //Label of HomePage
        Label titleLabel = new Label("Nutrition Analysis");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        //FoodInout Section
        foodInput = new TextField();
        foodInput.setPromptText("Enter food item");
        ContextMenu suggestionsMenu = new ContextMenu();
        foodInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                List<String> foodSuggestions = FoodService.searchFood(newValue, user.getUserPreference());
                FoodService.showSuggestions(suggestionsMenu, foodInput, foodSuggestions);
            } else {
                suggestionsMenu.hide();
            }
        });

        //Quantity Input Section
        quantityInput = new TextField();
        quantityInput.setPromptText("Enter quantity");

        //Buttons
        Button addButton = new Button("Add Food");
        Button searchButton = new Button("Search Food");
        Button calculateButton = new Button("Calculate Calories");


        //The result of Calculated Calories of food is shown Here
        calorieResultLabel = new Label();
        calorieResultLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        
        //Filter Button - Updates the charts
        Button filterButton = new Button("Filter Data");
        
        //Total Calories
        totalCalories = new Label();
        totalCalories.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        //Pie charts and Bar chart
        pieChart = new PieChart();
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        barChart = new BarChart<>(xAxis, yAxis);
        
        //Logics of the Buttons
        addButton.setOnAction(e -> {
        	try {
	            int id = user.getId();
	        	int quantity = Integer.parseInt(quantityInput.getText());
	        	String foodName = foodInput.getText();
	        	Float calories = FoodService.foodCalories(foodName);
	        	if(calories != null) {
		        	boolean isTrue = NutritiousSectionLogic.checkLogic(id);
		        	if(isTrue) {
		        		new AlertUtil();
						boolean isFalse = AlertUtil.showConfirmation();
		                if (!isFalse) {
			                	User_Target cal = TargetService.checkTarget(id);
			                    String result1 = NutritiousSectionLogic.addLogic(id, foodName, quantity, calories);
			                    TargetService.saveConsumedCaloriesToDatabase(quantity*calories, cal.getTargetId());
			                    TargetService.updateRemaining(id, quantity*calories);
			                    calorieResultLabel.setText(result1);
			                    return;
		
			                }else{
			                    String result1 = NutritiousSectionLogic.addLogic(id, foodName, quantity, calories);
			                    calorieResultLabel.setText(result1);
			                }
		
			        	}else {
			        		String result1 = NutritiousSectionLogic.addLogic(id, foodName, quantity, calories);
			        		calorieResultLabel.setText(result1);
			        		}
	        		}else {
			        	calorieResultLabel.setText("Food not found!");
	        		}
        		} catch (Exception err) {
         		calorieResultLabel.setText(err.getMessage());
        		}
            foodInput.clear();
            quantityInput.clear();
        });
        searchButton.setOnAction(e -> {
        	String foodName = foodInput.getText();
        	FoodItems fooditems = FoodService.getFoodCalories(foodName);
            Stage secondaryStage = new Stage();
            secondaryStage.setTitle("Food information");
            
            StackPane secondaryLayout = new StackPane();

            Label resultLabel = new Label(
            		"Calories: "+ fooditems.getCalories() +
            		"\nProteins: " + fooditems.getProtein() +
            		"\nFats: " + fooditems.getFats() +
            		"\nCarbohydrates: " + fooditems.getCarbohydrates()
            		);
            resultLabel.setPrefHeight(150);
            resultLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
            secondaryLayout.getChildren().add(resultLabel);
             
            Scene secondaryScene = new Scene(secondaryLayout, 400, 250);
            secondaryStage.setScene(secondaryScene);
            secondaryStage.show();

            
        });

        calculateButton.setOnAction(e -> {
            String foodName = foodInput.getText();
            String quantity = quantityInput.getText();
    		if (!foodName.isEmpty() && !quantity.isEmpty()) {
    			String result = CaloriesCalculator.CalculateCalories(foodName, quantity);
    			calorieResultLabel.setText(result);
    		}else {
    			calorieResultLabel.setText("Please input foodname and quantity");
    		}
        });

        filterButton.setOnAction(e -> {
            updateMyboy(user.getId());
        });
        
        int totalCaloriesResult = TargetService.getSumOfCaloriesTaken(user.getId());
        totalCalories.setText("Total Calories: "+ totalCaloriesResult);
        updateMyboy(user.getId());
        //adding all the widgets in HBox container
        HBox inputSection = new HBox(10, foodInput, quantityInput, addButton, searchButton, calculateButton);
        HBox filterSection = new HBox(10, filterButton);
        root.getChildren().addAll(titleLabel, inputSection, calorieResultLabel, filterSection,totalCalories,pieChart, barChart);

        // Scene Setup
        scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("../resources/style.css").toExternalForm());
    }

    public VBox getRoot() {
        return root;
    }
    
    private void updateMyboy(int id) {
    	pieChart.getData().clear();
        barChart.getData().clear();
        int totalCaloriesResult = TargetService.getSumOfCaloriesTaken(id);
        totalCalories.setText("Total Calories: "+totalCaloriesResult);

        Map<String, Integer> foodData = FoodService.updateCharts(id);
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        for (Map.Entry<String, Integer> entry : foodData.entrySet()) {
            pieChart.getData().add(new PieChart.Data(entry.getKey(), entry.getValue()));
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        barChart.getData().add(series);
    }
}
