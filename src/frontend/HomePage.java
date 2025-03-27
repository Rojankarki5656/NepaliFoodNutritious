package frontend;

import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import middleware.HomepageLogic;
import Models.Target;
import Models.User;
import backend.TargetService;

public class HomePage {

    private Label targetDisplayLabel, consumedDisplayLabel, remainingDisplayLabel, messageLabel;
    private TextField targetField, consumedField;
    private Button setTargetButton, updateButton, resetTargetButton, showHistoryButton;
    private PieChart pieChart;
    private VBox root;
    private Scene scene;
    private final UIManager uiManager;
    private final User user;

    public HomePage(UIManager uiManager, User user) {
        this.uiManager = uiManager;
        this.user = user;
        root = new VBox(15);
        root.setPadding(new Insets(20));

        boolean isTrue = HomepageLogic.switchInHomePage(user.getId());
        if (isTrue) {
            loadConsumptionUI();
        } else {
            initializeUI();
        }
        scene = new Scene(root, 400, 600);
    }

    public void initializeUI() {
        Label targetLabel = new Label("Set your target calorie intake:");
        targetField = new TextField();
        setTargetButton = new Button("Set Target");
        messageLabel = new Label();

        setTargetButton.setOnAction(e -> {
            try {
                int newTarget = Integer.parseInt(targetField.getText());
                if (newTarget <= 0) {
                    messageLabel.setText("Error: Must be greater than 0!");
                    messageLabel.setTextFill(Color.RED);
                    return;
                }
                HomepageLogic logic = new HomepageLogic();
                String result = logic.setTargetCalories(newTarget, user.getId());

                if (result.contains("Yay!")) {
                    loadConsumptionUI();
                } else {
                    messageLabel.setText(result);
                    messageLabel.setTextFill(Color.GREEN);
                }
            } catch (NumberFormatException ex) {
                messageLabel.setText("Invalid input! Please enter a number.");
                messageLabel.setTextFill(Color.RED);
            }
        });


        root.getChildren().addAll(targetLabel, targetField, setTargetButton, messageLabel);
    }

    private void loadConsumptionUI() {
        root.getChildren().clear();

        // Fetch target calories
        Target targetCal = TargetService.checkTarget(user.getId());
        int targetCalories = targetCal.getTarget();
        Float remainingCalories = targetCal.getRemaining();
        Float consumedCal = targetCal.getTotalCalories();

        targetDisplayLabel = new Label("Target: " + targetCalories + " kcal");
        consumedDisplayLabel = new Label("Consumed: " + consumedCal + " kcal");
        remainingDisplayLabel = new Label("Remaining: " + remainingCalories + " kcal");
        messageLabel = new Label();

        consumedField = new TextField();
        consumedField.setPromptText("Enter consumed calories");

        updateButton = new Button("Add Food");
        resetTargetButton = new Button("Reset Target");
        showHistoryButton = new Button("Show History");


        pieChart = new PieChart();
        updatePieChart(targetCalories);
        
        showHistoryButton.setOnAction(e -> {
        	Target result = TargetService.showHistory(user.getId());

        	if (result != null) { // Ensure result is not null before displaying
        	    Stage secondaryStage = new Stage();
        	    secondaryStage.setTitle("History");

        	    StackPane secondaryLayout = new StackPane();
        	    
        	    // Convert Target object to a string format
        	    String displayText = String.format(
        	        "Target: %d kcal\nConsumed: %.2f kcal\nRemaining: %.2f kcal",
        	        result.getTarget(), result.getTotalCalories(), result.getRemaining()
        	    );

        	    Label secondaryLabel = new Label(displayText);
        	    secondaryLayout.getChildren().add(secondaryLabel);

        	    Scene secondaryScene = new Scene(secondaryLayout, 250, 150);
        	    secondaryStage.setScene(secondaryScene);
        	    secondaryStage.show();
        	} else {
        	    System.out.println("No history found for this user.");
        	}

        });

        updateButton.setOnAction(e -> {
            try {
                int consumedCalories = Integer.parseInt(consumedField.getText());
                if (consumedCalories <= 0) {
                    messageLabel.setText("Error: Value must be positive.");
                    messageLabel.setTextFill(Color.RED);
                    return;
                }
                HomepageLogic logic = new HomepageLogic();
                String result = logic.addFood(consumedCalories, user.getId());

                updateLabels(targetCalories);
                updatePieChart(targetCalories);

                messageLabel.setText(result);
                messageLabel.setTextFill(Color.GREEN);
                consumedField.clear();
            } catch (NumberFormatException ex) {
                messageLabel.setText("Invalid input! Enter a number.");
                messageLabel.setTextFill(Color.RED);
            }
        });
        resetTargetButton.setOnAction(e -> {
            HomepageLogic logic = new HomepageLogic();
            logic.resetTargetCalories(user.getId());
            root.getChildren().clear();
            initializeUI();
            messageLabel.setText("Target reset. Set a new one.");
        });

        root.getChildren().addAll(
            targetDisplayLabel, consumedDisplayLabel, remainingDisplayLabel,
            consumedField, updateButton, resetTargetButton,showHistoryButton,
            pieChart, messageLabel
        );
    }

    public VBox getRoot() {
        return root;
    }

    public Scene getScene() {
        return scene;
    }

    private void updateLabels(int targetCalories) {
        Target targetCal = TargetService.checkTarget(user.getId());
        Float remainingCalories = targetCal.getRemaining();
        Float consumedCal = targetCal.getTotalCalories();
        targetDisplayLabel.setText("Target: " + targetCalories + " kcal");
        consumedDisplayLabel.setText("Consumed: " + consumedCal + " kcal");
        remainingDisplayLabel.setText("Remaining: " + remainingCalories + " kcal");
    }

    private void updatePieChart(int targetCalories) {
        Target targetCal = TargetService.checkTarget(user.getId());
        Float remainingCalories = targetCal.getRemaining();
        Float consumedCal = targetCal.getTotalCalories();
        pieChart.getData().clear();

        PieChart.Data consumedData = new PieChart.Data("Consumed", consumedCal);
        PieChart.Data remainingData = new PieChart.Data("Remaining", remainingCalories);

        pieChart.getData().addAll(consumedData, remainingData);

        // Fix: Ensure styles are applied safely
        consumedData.getNode().setStyle("-fx-pie-color: green;");
        remainingData.getNode().setStyle("-fx-pie-color: white;");
    }
}
