package frontend;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import Models.User;
import javafx.geometry.Insets;
import javafx.scene.layout.*;

public class Dashboard {
    private final Scene scene;
    private final UIManager uiManager;
    private BorderPane layout;  // Make layout an instance variable
    private Label contentLabel;
    private HomePage homePage; // Reference to HomePage

    public Dashboard(UIManager uiManager, User user) {
        this.uiManager = uiManager;
        layout = new BorderPane();  // Initialize layout

        // Sidebar Menu (Left Panel)
        VBox sidebar = new VBox(10);
        sidebar.setPadding(new Insets(20));
        sidebar.setStyle("-fx-background-color: #2C3E50; -fx-pref-width: 200px;");

        Label menuTitle = new Label("Dashboard");
        menuTitle.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");

        Button homeButton = new Button("Home");
        Button foodButton = new Button("Food Nutrition");
//        Button profileButton = new Button("Profile");
//        Button settingsButton = new Button("Settings");
        Button logoutButton = new Button("Logout");
//        Button BMRButton = new Button("C BMR & Kcal");

        // Create HomePage instance
        homePage = new HomePage(uiManager,user);

        // Content Label (Changes Based on Selection)
        contentLabel = new Label("Welcome, " + user.getName() + "!");
        contentLabel.setStyle("-fx-font-size: 18px; -fx-padding: 20px;");

        // Initially show the default label
        StackPane contentArea = new StackPane(contentLabel);
        contentArea.setPadding(new Insets(20));
        layout.setCenter(contentArea); // Default center content

        // Set Button Actions
        homeButton.setOnAction(e -> showContent("Home")); // Set HomePage content
        foodButton.setOnAction(e -> layout.setCenter(homePage.getRoot()));
//        BMRButton.setOnAction(e -> showContent("CalCulate"));
//        profileButton.setOnAction(e -> showContent("ðŸ‘¤ User Profile"));
//        settingsButton.setOnAction(e -> showContent("âš™ Settings"));
        logoutButton.setOnAction(e -> uiManager.showLoginScene()); // Return to Login Page

        // Style Buttons
        for (Button button : new Button[]{homeButton, foodButton, logoutButton}) {
            button.setStyle("-fx-background-color: #34495E; -fx-text-fill: white; -fx-font-size: 14px;");
            button.setMaxWidth(Double.MAX_VALUE);
        }

        sidebar.getChildren().addAll(menuTitle, homeButton, foodButton, logoutButton);
        layout.setLeft(sidebar);

        scene = new Scene(layout, 1000, 600);  // Adjusted window size
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

    }

    private void showContent(String text) {
        contentLabel.setText(text);
        layout.setCenter(new StackPane(contentLabel)); // Show text in center
    }

    public Scene getScene() {
        return scene;
    }

}
