package backend;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Models.FoodItems;

public class AdminService {
    
    // Insert a new food item
    public static void insertFood(FoodItems food) {
        String sql = "INSERT INTO foods (name, calories, protein, fat) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.connectDB();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, food.getName());
            stmt.setFloat(2, food.getCalories());
            stmt.setFloat(3, food.getProtein());
            stmt.setFloat(4, food.getFats());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Fetch all food items
    public static List<FoodItems> getAllFoods() {
        List<FoodItems> foods = new ArrayList<>();
        String sql = "SELECT * FROM foods";
        try (Connection conn = DBConnection.connectDB();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                foods.add(new FoodItems(
    		        	rs.getString("name"),
    		            rs.getFloat("calories"),
    		            rs.getFloat("protein"),
    		            rs.getFloat("fat"),
    		            rs.getFloat("carbohydrates")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return foods;
    }

    // Update a food item
    public static void updateFood(FoodItems food, String oldName) {
        String sql = "UPDATE foods SET name=?, calories=?, protein=?, fat=? WHERE name=?";
        try (Connection conn = DBConnection.connectDB();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, food.getName());
            stmt.setFloat(2, food.getCalories());
            stmt.setFloat(3, food.getProtein());
            stmt.setFloat(4, food.getFats());
            stmt.setString(5, oldName);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete a food item
    public static void deleteFood(String name) {
        String sql = "DELETE FROM foods WHERE name=?";
        try (Connection conn = DBConnection.connectDB();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
