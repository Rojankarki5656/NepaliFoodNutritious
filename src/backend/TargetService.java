package backend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Models.Target;

public class TargetService {
	public static void saveTargetToDatabase(int target, int id) {
	    try (Connection conn = DBConnection.connectDB()) {
	        String query = "INSERT INTO user_target (target, user_id, remaining, status) VALUES (?,?,?,?) ON DUPLICATE KEY UPDATE target = ?";
	        PreparedStatement pstmt = conn.prepareStatement(query);
	        pstmt.setInt(1, target);
	        pstmt.setInt(2, id);
	        pstmt.setInt(3, target);
	        pstmt.setString(4, "Active");
	        pstmt.setInt(5, target);
	        pstmt.executeUpdate();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	public static Target checkTarget(int id) {
	    try (Connection conn = DBConnection.connectDB()) {
	        String query = "SELECT target, status, calories_taken, remaining from user_target WHERE user_id =?";
	        PreparedStatement pstmt = conn.prepareStatement(query);
	        pstmt.setInt(1, id);
		    ResultSet rs = pstmt.executeQuery();
		    Integer targetCalories = null;
		    String status = null;
		    Float totalCalories = null;
		    Float remaining = null;
		    while (rs.next()){
		        targetCalories = rs.getInt("target");
		        status = rs.getString("status");
		        totalCalories = rs.getFloat("calories_taken");
		        remaining = rs.getFloat("remaining");
		        
		    }
		    rs.close();
		    return new Target(targetCalories, status, totalCalories, remaining);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		return null;
	}
	public static void saveConsumedCaloriesToDatabase(Float consumed, int target) {
		try (Connection conn = DBConnection.connectDB()) {
	        String query = "UPDATE user_target SET calories_taken = calories_taken + ? WHERE target = ?";
	        PreparedStatement pstmt = conn.prepareStatement(query);
	        pstmt.setFloat(1, consumed);
	        pstmt.setInt(2, target);
	        pstmt.executeUpdate();
	    } catch(Exception e) {
	    	e.printStackTrace();

	    }
	}
	public static void resetTargetToDatabase(String status ,  int id) {
	    try (Connection conn = DBConnection.connectDB()) {
	        String query = "UPDATE user_target SET status = ? WHERE status = ? AND user_id = ? ";
	        PreparedStatement pstmt = conn.prepareStatement(query);
	        pstmt.setString(1, status);
	        pstmt.setString(2, "Active");
	        pstmt.setInt(3, id);
	        pstmt.executeUpdate();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	public static void updateRemaining(int id) {
	    try (Connection conn = DBConnection.connectDB()) {
	        String query = "UPDATE user_target ut SET ut.remaining = ut.remaining - ut.calories_taken  WHERE status = ? AND user_id = ? ";
	        PreparedStatement pstmt = conn.prepareStatement(query);
	        pstmt.setString(1, "Active");
	        pstmt.setInt(2, id);
	        pstmt.executeUpdate();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
    
public static int updateCalories(int id) {
	//SELECT SUM(calories) FROM food_consumption WHERE user_id = ?;
    int totalCalories = 0;
	 try (Connection conn = DBConnection.connectDB();
	         PreparedStatement stmt = conn.prepareStatement("SELECT entire_calories FROM total_calories WHERE user_id = ?")) {
	    	
	    	stmt.setInt(1, id);
	        ResultSet rs = stmt.executeQuery();
	        if(rs.next()) {
	            totalCalories = rs.getInt("entire_calories");
	            System.out.println(totalCalories);
	            return totalCalories;
	        }else {
	        	return 0;
	        }
	        
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	return totalCalories;

}
public static Target showHistory(int id) {
    Target targetData = null; // Default to null in case no record is found

    try (Connection conn = DBConnection.connectDB();
         PreparedStatement stmt = conn.prepareStatement(
             "SELECT target, remaining, calories_taken FROM user_target WHERE user_id = ?"
         )) {
         
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) { // Ensure at least one row is found
            int target = rs.getInt("target");
            float remaining = rs.getFloat("remaining");
            float calories_taken = rs.getFloat("calories_taken");

            targetData = new Target(target, "", calories_taken, remaining);
        }
        
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return targetData; // Will return null if no data is found
}

}
