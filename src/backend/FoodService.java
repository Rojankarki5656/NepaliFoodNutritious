package backend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;

public class FoodService {
    
	//For adding the user's daily food into the database
    public static void addFood(int id,String foodName, Integer quantity) {

        try (Connection conn = DBConnection.connectUserDB()) {
            // Check if the food item exists
            String checkQuery = "SELECT quantity FROM food_log WHERE food_name = ? AND user_id = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setString(1, foodName);
            checkStmt.setInt(2, id);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                // If exists, increase quantity
                String updateQuery = "UPDATE food_log SET quantity = quantity + ? WHERE food_name = ? AND user_id = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
                updateStmt.setInt(1, quantity);
                updateStmt.setString(2, foodName);
                updateStmt.setInt(3, id);

                updateStmt.executeUpdate();
            } else {
                // If not exists, insert new row
                String insertQuery = "INSERT INTO food_log (user_id, food_name, quantity) VALUES (?, ?, ?)";
                PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
                insertStmt.setInt(1, id);
                insertStmt.setString(2, foodName);
                insertStmt.setInt(3, quantity);
                insertStmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        }
        
    
public static Map<String, Integer> updateCharts(int id) { //For graph and pie charts
    Map<String, Integer> foodCounts = new HashMap<>();
    
    try (Connection conn = DBConnection.connectUserDB();
         PreparedStatement stmt = conn.prepareStatement("SELECT food_name,quantity FROM food_log where user_id = ?")) {
    	
    	stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            String food = rs.getString("food_name");
            int count = rs.getInt("quantity");
            foodCounts.put(food, count);
        }
        
    } catch (SQLException e) {
        e.printStackTrace();
    }
    
    return foodCounts;  // Returns food data to use in graph and pie chart
}

public static Map<Float, List<Float>> getFoodCalories(String name) { //For retriving the foods information
    Map<Float, List<Float>> foodInfo = new HashMap<>();
    String query = "SELECT calories, protein, fat, carbohydrates FROM non_veg where name = ?";

    try (Connection conn = DBConnection.connectFoodDB();
         PreparedStatement stmt = conn.prepareStatement(query);) {
    	
        stmt.setString(1, name);
    	 ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Float calories = rs.getFloat("calories");
            Float proteins = rs.getFloat("protein");
            Float fats = rs.getFloat("fat");
            Float carbohydrates = rs.getFloat("carbohydrates");

            // Store as a list
            List<Float> nutrients = Arrays.asList(proteins, fats, carbohydrates);
            foodInfo.put(calories, nutrients);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return foodInfo; //Returns the food info in a list
}   

public static Float foodCalories(String foodName) {
	String query = "SELECT calories FROM non_veg where name = ?";

try (Connection conn = DBConnection.connectFoodDB();
		PreparedStatement stmt = conn.prepareStatement(query)){
    stmt.setString(1, foodName);
    ResultSet rs = stmt.executeQuery();
    while (rs.next()) {
        Float calories = rs.getFloat("calories");
        return calories;
    }
    
	}catch (SQLException e) {
			e.printStackTrace();
		}
return null;
	}


public static List<String> searchFood(String keyword) {
    List<String> results = new ArrayList<>();
    String query = "SELECT name FROM non_veg WHERE name LIKE ? LIMIT 5"; // Limit results

    try (Connection conn = DBConnection.connectFoodDB();
         PreparedStatement stmt = conn.prepareStatement(query)) {

        stmt.setString(1, "%" + keyword + "%"); // Search for partial matches
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            results.add(rs.getString("name"));
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
    return results;
}

public static void showSuggestions(ContextMenu menu, TextField textField, List<String> suggestions) {
    menu.getItems().clear();

    for (String suggestion : suggestions) {
        MenuItem item = new MenuItem(suggestion);
        item.setOnAction(e -> textField.setText(suggestion)); // Set selected value
        menu.getItems().add(item);
    }

    if (!menu.getItems().isEmpty()) {
        menu.show(textField, textField.getScene().getWindow().getX() + textField.getLayoutX(),
                textField.getScene().getWindow().getY() + textField.getLayoutY() + textField.getHeight());
    } else {
        menu.hide();
    }
}

}
