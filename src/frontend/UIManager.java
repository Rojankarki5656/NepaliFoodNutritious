package frontend;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class UIManager {
    private final Stage primaryStage;
    private final Login login;
    private final Register register;
    private Dashboard dashboard;

    public UIManager(Stage stage) {
        this.primaryStage = stage;
        this.login = new Login(this);
        this.register = new Register(this);
    }

    public void showLoginScene() {
        primaryStage.setScene(login.getScene());
    }

    public void showRegisterScene() {
        primaryStage.setScene(register.getScene());
    }

    public void showDashboard(User user) {
        if (user == null) {
            System.out.println("User not found!");
            return;
        }
        
        this.dashboard = new Dashboard(this, user);
        primaryStage.setScene(dashboard.getScene());
    }

    public Scene getLoginScene() {
        return login.getScene();
    }
}
