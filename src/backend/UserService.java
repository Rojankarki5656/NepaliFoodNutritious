package backend;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Models.User;
import javafx.scene.control.Alert;
import utils.AlertUtil;

public class UserService {
	
	//Return the User id and name
	public static User getUserInfo(String email, String password) {
        String query = "SELECT id, name, actors, user_preference, email FROM user WHERE email = ? AND password = ?";

        try (Connection conn = DBConnection.connectDB();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String actor = rs.getString("actors");
                String user_preference = rs.getString("user_preference");
                String userEmail = rs.getString("email");
                

                return new User(id, name, actor,userEmail, user_preference); // Return a User object
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if no user is found
    }
	
	
	//Stores the users name, email and password in the user's database
    public static void register(String name, String email,String preference, String password) {
    	
    	String query = "INSERT INTO user(name,email,user_preference, password, actors) values(?,?,?,?,?)";
    	
    	try (Connection conn = DBConnection.connectDB();
             PreparedStatement stmt = conn.prepareStatement(query)) {
               
               stmt.setString(1, name);
               stmt.setString(2, email);
               stmt.setString(3, preference);
               stmt.setString(4, password);
               stmt.setString(5, "user");
               int rowsInserted = stmt.executeUpdate();
               if (rowsInserted > 0) {
                   AlertUtil.showAlert(Alert.AlertType.INFORMATION, "Success", "Registration successful!");                   
               }
    	} catch (SQLException e) {
            e.printStackTrace();
        }
    	
    }
    
    public static void updateProfile(String update,int id, String type) {
    	String query = "";
    	if(type.equals("password")) {
        	query = "UPDATE user SET password = ? WHERE id = ?";

    	}else
    	if(type.equals("preference")) {
        	query = "UPDATE user SET user_preference = ? WHERE id = ?";

    	}else if(type.equals("name")) {
    		query = "UPDATE user SET name = ? WHERE id = ?";
    	}
    	try(Connection conn = DBConnection.connectDB();
    		PreparedStatement stmt = conn.prepareStatement(query))
    	{
    		stmt.setString(1, update);
            stmt.setInt(2, id);
            int isUpdated = stmt.executeUpdate();
            if(isUpdated > 0) 
            {
                AlertUtil.showAlert(Alert.AlertType.INFORMATION, "Success", "Profile updated successfully!");

            }
    	} catch (SQLException e) {
                e.printStackTrace();
            }
    }

}