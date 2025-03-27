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

import Models.FoodItems;
import Models.Target;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class FoodService {
	//For adding the user's daily food into the database
	public static String addFood(int id, String foodName, Integer quantity, Float calories) {
	    if (foodName.isEmpty() || quantity <= 0) {
	        return "Food cannot be empty and quantity cannot be less or equals to zero";
	    }
	    
	    try (Connection conn = DBConnection.connectDB()) {
	        // Check if the food item exists
	        String checkQuery = "SELECT fl.quantity FROM food_log fl \r\n"
	        		+ "JOIN foods f ON fl.food_id = f.id \r\n"
	        		+ "JOIN user u ON fl.user_id  = u.id\r\n"
	        		+ "WHERE f.name = ? AND u.id = ?";
	        try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
	            checkStmt.setString(1, foodName);
	            checkStmt.setInt(2, id);
	            ResultSet rs = checkStmt.executeQuery();

	            if (rs.next()) {
	                // If exists, increase quantity and calories
	                String updateQuery = "UPDATE food_log fl \r\n"
	                		+ "JOIN foods f ON fl.food_id = f.id \r\n"
	                		+ "JOIN user u ON fl.user_id = u.id \r\n"
	                		+ "SET fl.quantity = fl.quantity + ?, fl.calories = fl.calories + ? \r\n"
	                		+ "WHERE f.name = ? AND u.id = ?";
	                try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
	                    updateStmt.setInt(1, quantity);
	                    updateStmt.setFloat(2, calories * quantity); // Fixing the calculation
	                    updateStmt.setString(3, foodName);
	                    updateStmt.setInt(4, id);
	                    updateStmt.executeUpdate();
	                }
	                return "Updated Successfully";
	            } else {
	                // If not exists, insert new row
	                String insertQuery = "INSERT INTO food_log (user_id, food_id , quantity, calories) \r\n"
	                		+ "VALUES (?, (SELECT id FROM foods WHERE name = ?) , ?, ?)";
	                try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
	                    insertStmt.setInt(1, id);
	                    insertStmt.setString(2, foodName);
	                    insertStmt.setInt(3, quantity);
	                    insertStmt.setFloat(4, calories * quantity); // Fixing the calculation
	                    insertStmt.executeUpdate();
	                }
	                return "Inserted Successfully";
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return "Database error: " + e.getMessage();
	    }
	}

    
    public static void updateTotalCalories(int calories,int id) {
    	try (Connection conn = DBConnection.connectDB()) {
    	String updateQuery = "UPDATE  total_calories SET entire_calories = entire_calories + ? WHERE user_id = ?";
        PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
        updateStmt.setFloat(1, calories);
        updateStmt.setInt(2, id);

        updateStmt.executeUpdate();
    	}catch(SQLException e) {
    		e.printStackTrace();
    	}
    }
		public static Map<String, Integer> updateCharts(int id) { //For graph and pie charts
		    Map<String, Integer> foodCounts = new HashMap<>();
		    
		    try (Connection conn = DBConnection.connectDB();
		         PreparedStatement stmt = conn.prepareStatement("SELECT f.name, fl.quantity \r\n"
		         		+ "FROM food_log fl \r\n"
		         		+ "JOIN foods f ON fl.food_id = f.id \r\n"
		         		+ "JOIN user u ON fl.user_id = u.id \r\n"
		         		+ "WHERE u.id = ?")) {
		    	
		    	stmt.setInt(1, id);
		        ResultSet rs = stmt.executeQuery();
		        while (rs.next()) {
		            String food = rs.getString("name");
		            int count = rs.getInt("quantity");
		            foodCounts.put(food, count);
		        }
		        
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		    
		    return foodCounts;  // Returns food data to use in graph and pie chart
		}

		public static FoodItems getFoodCalories(String name) { // For retrieving s the food's information
		    String query = "SELECT calories, protein, fat, carbohydrates FROM foods WHERE name = ?";

		    try (Connection conn = DBConnection.connectDB();
		         PreparedStatement stmt = conn.prepareStatement(query)) {

		        stmt.setString(1, name);
		        ResultSet rs = stmt.executeQuery();
		        Float calories = null;
		        Float proteins = null;
		        Float fats = null;
		        Float carbohydrates = null;

		        if (rs.next()) {
		            calories = rs.getFloat("calories");
		            proteins = rs.getFloat("protein");
		            fats = rs.getFloat("fat");
		            carbohydrates = rs.getFloat("carbohydrates");
		        }
		        
		        rs.close();
		        return new FoodItems(calories, proteins, carbohydrates, fats);

		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		    return null;
		}
  

		public static Float foodCalories(String foodName) {
			String query = "SELECT calories FROM foods where name = ?";
		
		try (Connection conn = DBConnection.connectDB();
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
		    String query = "SELECT name FROM foods WHERE name LIKE ? LIMIT 5"; // Limit results
		
		    try (Connection conn = DBConnection.connectDB();
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

