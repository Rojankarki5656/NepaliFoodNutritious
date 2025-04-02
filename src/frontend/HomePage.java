package frontend;

import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import middleware.HomepageLogic;
import middleware.NutritiousSectionLogic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.Optional;

import Models.User_Target;
import Models.User;
import backend.DBConnection;
import backend.TargetService;

public class HomePage {

    private Label targetDisplayLabel, consumedDisplayLabel, remainingDisplayLabel, messageLabel;
    private TextField targetField, consumedField;
    private Button setTargetButton, updateButton, resetTargetButton, showHistoryButton;
    private PieChart pieChart;
    private TableView<User_Target> historyTable = new TableView<>();
    private VBox root;
    private Scene scene;
    private final UIManager uiManager;
    private final User user;

    public HomePage(UIManager uiManager, User user) {
        this.uiManager = uiManager;
        this.user = user;
        root = new VBox(15);
        root.setPadding(new Insets(20));
        scene = new Scene(root, 400, 600);

        
        User_Target targetCal = TargetService.checkTarget(user.getId());
        boolean isTrue = HomepageLogic.switchInHomePage(user.getId());

        if (isTrue) {
            LocalDate currentDate = LocalDate.now();

            if(targetCal.getEndDate().isAfter(currentDate)) {
            	createAlert("You have reached the date");
            }
            
            loadConsumptionUI();
            
        } else {
            initializeUI(); // Normal UI initialization
        }        
    }
    public void initializeUI() {
        Label targetLabel = new Label("Set your target calorie intake:");
        targetField = new TextField();
        DatePicker datePicker = new DatePicker(LocalDate.now());
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
                String result = logic.setTargetCalories(newTarget, user.getId(), datePicker.getValue());

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


        root.getChildren().addAll(targetLabel, targetField,datePicker, setTargetButton, messageLabel);
    }

    private void loadConsumptionUI() {
        root.getChildren().clear();

        // Fetch target calories
        User_Target targetCal = TargetService.checkTarget(user.getId());
        double targetCalories = targetCal.getTargetCalories();
        double remainingCalories = targetCal.getTargetRemaining();
        double consumedCal = targetCal.getCaloriesTaken();

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
// Ensure result is not null before displaying
        	    Stage secondaryStage = new Stage();
        	    secondaryStage.setTitle("History");

        	    StackPane secondaryLayout = new StackPane();
        	    TableColumn<User_Target, Double> colTargetCalories = new TableColumn<>("Target Calories");
                colTargetCalories.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getTargetCalories()));

                TableColumn<User_Target, Double> colCaloriesTaken = new TableColumn<>("Calories Taken");
                colCaloriesTaken.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getCaloriesTaken()));

                TableColumn<User_Target, Double> colTargetRemaining = new TableColumn<>("Remaining Calories");
                colTargetRemaining.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getTargetRemaining()));

                TableColumn<User_Target, String> colStatus = new TableColumn<>("Status");
                colStatus.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getStatus()));

                TableColumn<User_Target, LocalDate> colStartDate = new TableColumn<>("Start Date");
                colStartDate.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getStartDate()));

                TableColumn<User_Target, LocalDate> colEndDate = new TableColumn<>("End Date");
                colEndDate.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getEndDate()));

                // Add columns to TableView
                historyTable.getColumns().addAll(colTargetCalories, colCaloriesTaken, colTargetRemaining, colStatus, colStartDate, colEndDate);
//
//                // Load data into TableView
                loadUserTargetHistory(user.getId());
//
//                // Layout
                VBox layout = new VBox(10, historyTable);
                layout.setAlignment(Pos.CENTER);
                layout.setPrefSize(700, 400);
//
//                Scene scene = new Scene(layout);
//                primaryStage.setScene(scene);
//                primaryStage.show();

        	    secondaryLayout.getChildren().add(layout);

        	    Scene secondaryScene = new Scene(secondaryLayout, 250, 150);
        	    secondaryStage.setScene(secondaryScene);
        	    secondaryStage.show();

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
                String result = logic.addFood(consumedCalories, user.getId(), targetCal.getCaloriesTaken());
                
                if(result.equals("Error: Consumed calories exceed target!")) {
                	 createAlert("You have completed your target");
                }else if(result.equals("You have reached the date")) {
                	createAlert("You have reached the date limit");
                	
                }else {
                updateLabels(targetCalories);
                updatePieChart(targetCalories);

                messageLabel.setText(result);
                messageLabel.setTextFill(Color.GREEN);
                consumedField.clear();
                }
            } catch (NumberFormatException ex) {
                messageLabel.setText("Invalid input! Enter a number.");
                messageLabel.setTextFill(Color.RED);
            }
        });
        resetTargetButton.setOnAction(e -> {
            HomepageLogic logic = new HomepageLogic();
            logic.resetTargetCalories(user.getId(), "Reset");
            root.getChildren().clear();
            initializeUI();
            messageLabel.setText("Target reset. Set a new one.");
        });

        root.getChildren().addAll(
            targetDisplayLabel, consumedDisplayLabel, remainingDisplayLabel,
            consumedField, updateButton, resetTargetButton,showHistoryButton,
            messageLabel,pieChart
        );
    }

    public VBox getRoot() {
        return root;
    }

    public Scene getScene() {
        return scene;
    }

    private void updateLabels(double targetCalories) {
        User_Target targetCal = TargetService.checkTarget(user.getId());
        double remainingCalories = targetCal.getTargetRemaining();
        double consumedCal = targetCal.getCaloriesTaken();
        targetDisplayLabel.setText("Target: " + targetCalories + " kcal");
        consumedDisplayLabel.setText("Consumed: " + consumedCal + " kcal");
        remainingDisplayLabel.setText("Remaining: " + remainingCalories + " kcal");
    }
    
    private void createAlert(String text) {
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
         root.getChildren().clear();
	        initializeUI();
	        }
    }

    private void updatePieChart(double targetCalories) {
        User_Target targetCal = TargetService.checkTarget(user.getId());
        double remainingCalories = targetCal.getTargetRemaining();
        double consumedCal = targetCal.getCaloriesTaken();
        pieChart.getData().clear();

        PieChart.Data consumedData = new PieChart.Data("Consumed", consumedCal);
        PieChart.Data remainingData = new PieChart.Data("Remaining", remainingCalories);

        pieChart.getData().addAll(consumedData, remainingData);

        // Fix: Ensure styles are applied safely
        consumedData.getNode().setStyle("-fx-pie-color: green;");
        remainingData.getNode().setStyle("-fx-pie-color: white;");
    }
    private void loadUserTargetHistory(int id) {
        ObservableList<User_Target> historyList = FXCollections.observableArrayList();
        try (Connection conn = DBConnection.connectDB();
             PreparedStatement stmt = conn.prepareStatement("SELECT user_id, target, calories_taken, remaining, status, date, end_date FROM user_target WHERE user_id = ? ORDER BY date DESC")) {

            stmt.setInt(1, id); // Change this to dynamically get the current user's ID
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                User_Target userTarget = new User_Target();
                userTarget.setUserId(rs.getInt("user_id"));
                userTarget.setTargetCalories(rs.getDouble("target"));
                userTarget.setCaloriesTaken(rs.getDouble("calories_taken"));
                userTarget.setTargetRemaining(rs.getDouble("remaining"));
                userTarget.setStatus(rs.getString("status"));
                userTarget.setStartDate(rs.getDate("date").toLocalDate());
                userTarget.setEndDate(rs.getDate("end_date") != null ? rs.getDate("end_date").toLocalDate() : null);

                historyList.add(userTarget);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        historyTable.setItems(historyList);
    }
}
