package backend;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Models.User;

public class UserService {
	
	//Return the User id and name
	public static User getUserInfo(String email, String password) {
        String query = "SELECT id, name FROM user WHERE email = ? AND password = ?";

        try (Connection conn = DBConnection.connectUserDB();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");

                return new User(id, name); // Return a User object
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if no user is found
    }
	
	
	//Stores the users name, email and password in the user's database
    public static void register(String name, String email, String password) {
    	
    	String query = "INSERT INTO user(name,email,password) values(?,?,?)";
    	
    	try (Connection conn = DBConnection.connectUserDB();
             PreparedStatement stmt = conn.prepareStatement(query)) {
               
               stmt.setString(1, name);
               stmt.setString(2, email);
               stmt.setString(3, password);
               int rowsInserted = stmt.executeUpdate();
               if (rowsInserted > 0) {
                   System.out.println("Data inserted successfully!");
                   
               }
    	} catch (SQLException e) {
            e.printStackTrace();
        }
    	
    }

}