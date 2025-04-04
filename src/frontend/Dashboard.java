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
    private NutritiousSection ns; // Reference to HomePage
    private HomePage homepage;
    private ProfileSection profilesection;
    private DailyKcalBMR bmr;

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
        Button calculateBMR = new Button("Calculate BMR");
        Button userGuide = new Button("User Guide");
        Button profileSection = new Button("Profile");
        Button logoutButton = new Button("Logout");

        // Create HomePage instance
        ns = new NutritiousSection(uiManager,user);
        homepage = new HomePage(uiManager, user);
        profilesection = new ProfileSection(uiManager, user);
        bmr = new DailyKcalBMR(uiManager, user);

        // Content Label (Changes Based on Selection)
        contentLabel = new Label("Welcome, " + user.getName() + "!");
        contentLabel.setStyle("-fx-font-size: 18px; -fx-padding: 20px;");

        // Initially show the default label
        StackPane contentArea = new StackPane(contentLabel);
        contentArea.setPadding(new Insets(20));
        layout.setCenter(contentArea); // Default center content

        // Set Button Actions
        homeButton.setOnAction(e -> layout.setCenter(homepage.getRoot())); // Set HomePage content
        foodButton.setOnAction(e -> layout.setCenter(ns.getRoot()));
        logoutButton.setOnAction(e -> uiManager.showLoginScene()); // Return to Login Page
        userGuide.setOnAction(e -> layout.setCenter(userGuide()));
        profileSection.setOnAction(e -> layout.setCenter(profilesection.getRoot()));
        calculateBMR.setOnAction(e -> {
        	layout.setCenter(bmr.getRoot());
        });

        // Style Buttons
        for (Button button : new Button[]{homeButton, foodButton,userGuide,profileSection, logoutButton, calculateBMR}) {
            button.setStyle("-fx-background-color: #34495E; -fx-text-fill: white; -fx-font-size: 14px;");
            button.setMaxWidth(Double.MAX_VALUE);
        }

        sidebar.getChildren().addAll(menuTitle, homeButton, foodButton,calculateBMR,profileSection, userGuide, logoutButton);
        layout.setLeft(sidebar);

        scene = new Scene(layout, 1000, 600);  // Adjusted window size
        scene.getStylesheets().add(getClass().getResource("../resources/style.css").toExternalForm());

    }
    public Scene getScene() {
        return scene;
    }
    
    public VBox userGuide() {
        VBox sidebar = new VBox(10);
        Label userGuideLabel = new Label("User Guide");
        
        sidebar.getChildren().add(userGuideLabel);
        sidebar.setPadding(new Insets(20));        
        return sidebar;  // ✅ Returning VBox instead of Scene
    }

}
