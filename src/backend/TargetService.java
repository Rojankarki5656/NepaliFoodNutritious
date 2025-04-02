package backend;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import Models.User_Target;

public class TargetService {
	public static void saveTargetToDatabase(double target, int id, LocalDate date) {
	    try (Connection conn = DBConnection.connectDB()) {
	        String query = "INSERT INTO user_target (target, user_id, remaining, status, end_date) VALUES (?,?,?,?,?) ON DUPLICATE KEY UPDATE target = ?";
	        PreparedStatement pstmt = conn.prepareStatement(query);
	        pstmt.setDouble(1, target);
	        pstmt.setInt(2, id);
	        pstmt.setDouble(3, target);
	        pstmt.setString(4, "Active");
	        pstmt.setDouble(6, target);
	        pstmt.setDate(5, Date.valueOf(date));
	        pstmt.executeUpdate();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	public static User_Target checkTarget(int id) {
	    try (Connection conn = DBConnection.connectDB()) {
	        String query = "SELECT target, status, calories_taken, remaining, date, end_date from user_target WHERE user_id =? AND status=?";
	        PreparedStatement pstmt = conn.prepareStatement(query);
	        pstmt.setInt(1, id);
	        pstmt.setString(2, "Active");
		    ResultSet rs = pstmt.executeQuery();
		    if (rs.next()){
	                User_Target target = new User_Target();
	                target.setUserId(id);
	                target.setTargetCalories(rs.getDouble("target"));
	                target.setCaloriesTaken(rs.getDouble("calories_taken"));
	                target.setTargetRemaining(rs.getDouble("remaining"));
	                target.setStartDate(rs.getDate("date").toLocalDate());
	                target.setEndDate(rs.getDate("end_date").toLocalDate());
	                target.setStatus(rs.getString("status"));
	                
	                return target;
	                }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		return null;
	}
	public static void saveConsumedCaloriesToDatabase(double consumed, double target) {
		try (Connection conn = DBConnection.connectDB()) {
	        String query = "UPDATE user_target SET calories_taken = calories_taken + ? WHERE target = ?";
	        PreparedStatement pstmt = conn.prepareStatement(query);
	        pstmt.setDouble(1, consumed);
	        pstmt.setDouble(2, target);
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
	public static void updateRemaining(int id, double totalcal) {
	    try (Connection conn = DBConnection.connectDB()) {
	        User_Target target = TargetService.checkTarget(id);
	        double userTarget = target.getTargetCalories();
	    	if(userTarget - totalcal < 0) {
	    		System.out.println("Do nothing!");
	    	}
	    	else{
	        String query = "UPDATE user_target ut SET ut.remaining = ?  WHERE status = ? AND user_id = ? ";
	        PreparedStatement pstmt = conn.prepareStatement(query);

	        pstmt.setDouble(1, userTarget - totalcal);
	        pstmt.setString(2, "Active");
	        pstmt.setInt(3, id);
	        pstmt.executeUpdate();
	    	}
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
public static User_Target showHistory(int id) {
    User_Target targetData = null; // Default to null in case no record is found

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

            targetData = null;
        }
        
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return targetData; // Will return null if no data is found
}

}
