package frontend;
import Models.BMR;
import Models.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import middleware.BMRCalculator;

public class DailyKcalBMR {
    private Scene scene;
    private TextField sexField, weightField, heightField, ageField;
    private TextField activityFactorField;

    public DailyKcalBMR(UIManager uiManager, User user) {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER); // center align all components.

        Label titleLabel = new Label("Calculate BMR");
        titleLabel.setStyle("-fx-font-size: 50px; -fx-font-weight: bold;");
       

        Label sexLabel = new Label("Enter your sex (male or female):");
        sexField = new TextField();
        sexLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;"); 
        sexField.setMaxWidth(300);

        Label weightLabel = new Label("Enter your weight in KG:");
        weightField = new TextField();
        weightLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;"); 
        weightField.setMaxWidth(300);

        Label heightLabel = new Label("Enter your height in CM:");
        heightField = new TextField();
        heightLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;"); 
        heightField.setMaxWidth(300);

        Label ageLabel = new Label("Enter your age:");
        ageField = new TextField();
        ageLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;"); 
        ageField.setMaxWidth(300);

        Label activityLabel = new Label("Enter your activity factor (1-4):");
        activityFactorField = new TextField();
        activityLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;"); 
        activityFactorField.setMaxWidth(300);

        Button calculateButton = new Button("Calculate");
        Button backButton = new Button("Back");
        
        backButton.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;"); 
        calculateButton.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;"); 
        
        backButton.setOnAction(e ->{
        	uiManager.showDashboard(user);
        });
        calculateButton.setOnAction(e -> {
        	String sex = sexField.getText();
        	double weight = Double.parseDouble(weightField.getText());
        	double height = Double.parseDouble(heightField.getText());
        	double age = Double.parseDouble(ageField.getText());
        	int activityFactor = Integer.parseInt(activityFactorField.getText());

        	// Calculate BMR
        	BMR mybmr = BMRCalculator.calculateKcal(sex, weight, height, activityFactor, activityFactor);
        	String error = mybmr.getError();

        	// Create layout container
        	VBox resultLayout = new VBox(10);
        	resultLayout.setAlignment(Pos.CENTER);

        	// If there's an error, only show the error message
        	if (!error.isEmpty()) {
        	    Label errorLabel = new Label("Error: " + error);
        	    errorLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: red;");
        	    resultLayout.getChildren().add(errorLabel);
        	} else {
        	    // No errors, show all calculated values
        	    double bmr = mybmr.getBmr();
        	    double totalKcal = mybmr.getTotalKcal();
        	    double fatKcal = mybmr.getFatKcal();
        	    double carbsKcal = mybmr.getCarbsKcal();
        	    double proteinKcal = mybmr.getProteinKcal();

        	    Label bmrLabel = new Label("Your BMR is: " + bmr + " Kcal");
        	    bmrLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        	    Label totalKcalLabel = new Label("Your total daily Kcal intake is: " + totalKcal + " Kcal");
        	    totalKcalLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        	    Label proteinLabel = new Label("Protein: " + proteinKcal);
        	    proteinLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        	    Label fatLabel = new Label("Fat: " + fatKcal);
        	    fatLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        	    Label carbsLabel = new Label("Carbs: " + carbsKcal);
        	    carbsLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        	    resultLayout.getChildren().addAll(bmrLabel, totalKcalLabel, proteinLabel, fatLabel, carbsLabel);
        	}

        	// Clear previous results before adding new ones
        	VBox mainLayout = (VBox) scene.getRoot();
        	mainLayout.getChildren().removeIf(node -> node instanceof VBox); // Remove old results
        	mainLayout.getChildren().add(resultLayout);

        	// Clear input fields after calculation
        	sexField.clear();
        	weightField.clear();
        	heightField.clear();
        	ageField.clear();
        	activityFactorField.clear();
            
            
        });
        // crete an Hbox to hold the buttons and lign them
        HBox buttonBox = new HBox(10, calculateButton, backButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10));
        layout.getChildren().addAll(titleLabel, sexLabel, sexField, weightLabel, weightField, heightLabel, heightField, ageLabel, ageField, activityLabel, activityFactorField, buttonBox);
        scene = new Scene(layout, 800, 600);
    }

    public Scene getScene() {
        return scene;
    }
}