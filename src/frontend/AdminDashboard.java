package frontend;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import Models.FoodItems;
import Models.User;
import backend.AdminService;
import javafx.scene.control.cell.PropertyValueFactory;

public class AdminDashboard{
	
	private Scene scene;
    private final UIManager uiManager;
    private TableView<FoodItems> foodTable;
    private TextField nameField, caloriesField, proteinField, carbsField;
    private ObservableList<FoodItems> foodList = FXCollections.observableArrayList();

    public AdminDashboard(UIManager uiManager, User user) {
        this.uiManager = uiManager;
        Label titleLabel = new Label("Admin Dashboard - Nepali Nutritious Food");
        titleLabel.getStyleClass().add("title");

        // TableView
        foodTable = new TableView<>();
        TableColumn<FoodItems, String> nameCol = new TableColumn<>("Food Name");
        TableColumn<FoodItems, Integer> caloriesCol = new TableColumn<>("Calories");
        TableColumn<FoodItems, Float> proteinCol = new TableColumn<>("Protein (g)");
        TableColumn<FoodItems, Float> carbsCol = new TableColumn<>("fat (g)");

        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        caloriesCol.setCellValueFactory(new PropertyValueFactory<>("calories"));
        proteinCol.setCellValueFactory(new PropertyValueFactory<>("protein"));
        carbsCol.setCellValueFactory(new PropertyValueFactory<>("fats"));

        foodTable.getColumns().addAll(nameCol, caloriesCol, proteinCol, carbsCol);
        refreshTable(); // Load data from MySQL

        // Input Fields
        nameField = new TextField();
        nameField.setPromptText("Food Name");
        caloriesField = new TextField();
        caloriesField.setPromptText("Calories");
        proteinField = new TextField();
        proteinField.setPromptText("Protein (g)");
        carbsField = new TextField();
        carbsField.setPromptText("Carbs (g)");

        HBox inputFields = new HBox(10, nameField, caloriesField, proteinField, carbsField);
        inputFields.setPadding(new Insets(10));

        // Buttons
        Button addButton = new Button("Add");
        Button updateButton = new Button("Update");
        Button deleteButton = new Button("Delete");
        Button clearButton = new Button("Clear");

        addButton.setOnAction(e -> addFood());
        updateButton.setOnAction(e -> updateFood());
        deleteButton.setOnAction(e -> deleteFood());
        clearButton.setOnAction(e -> clearFields());

        HBox buttons = new HBox(10, addButton, updateButton, deleteButton, clearButton);
        buttons.setPadding(new Insets(10));

        VBox layout = new VBox(10, titleLabel, foodTable, inputFields, buttons);
        layout.setPadding(new Insets(20));

        this.scene = new Scene(layout, 800, 500);
        this.scene.getStylesheets().add(getClass().getResource("../resources/style.css").toExternalForm());
    }
    public Scene getScene() {
        return scene;
    }

    // Refresh Table
    private void refreshTable() {
        foodList.clear();
        foodList.addAll(AdminService.getAllFoods());
        foodTable.setItems(foodList);
    }

    // Add Food
    private void addFood() {
        FoodItems food = new FoodItems(nameField.getText(), Float.parseFloat(caloriesField.getText()), Float.parseFloat(proteinField.getText()), Float.parseFloat(carbsField.getText()), null);
        AdminService.insertFood(food);
        refreshTable();
        clearFields();
    }

    // Update Food
    private void updateFood() {
        FoodItems selectedFood = foodTable.getSelectionModel().getSelectedItem();
        if (selectedFood != null) {
            AdminService.updateFood(new FoodItems(nameField.getText(), Float.parseFloat(caloriesField.getText()), Float.parseFloat(proteinField.getText()), Float.parseFloat(carbsField.getText()), null), selectedFood.getName());
            refreshTable();
            clearFields();
        }
    }

    // Delete Food
    private void deleteFood() {
        FoodItems selectedFood = foodTable.getSelectionModel().getSelectedItem();
        if (selectedFood != null) {
            AdminService.deleteFood(selectedFood.getName());
            refreshTable();
        }
    }

    // Clear Fields
    private void clearFields() {
        nameField.clear();
        caloriesField.clear();
        proteinField.clear();
        carbsField.clear();
    }
}
