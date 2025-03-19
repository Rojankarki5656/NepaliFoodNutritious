package frontend;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        UIManager uiManager = new UIManager(primaryStage);
        primaryStage.setTitle("Nutrition App");
        primaryStage.setScene(uiManager.getLoginScene()); // Start with login scene
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
