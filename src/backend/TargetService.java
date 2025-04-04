package backend;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import Models.User_Target;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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
	        String query = "SELECT target_id, target, status, calories_taken, remaining, date, end_date from user_target WHERE user_id =? AND status=?";
	        PreparedStatement pstmt = conn.prepareStatement(query);
	        pstmt.setInt(1, id);
	        pstmt.setString(2, "Active");
		    ResultSet rs = pstmt.executeQuery();
		    if (rs.next()){
	                User_Target target = new User_Target();
	                target.setTargetId(rs.getInt("target_id"));
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
	public static void saveConsumedCaloriesToDatabase(double consumed, int target_id) {
		System.out.println("Consumed cal :" + consumed);
		try (Connection conn = DBConnection.connectDB()) {
	        String query = "UPDATE user_target SET calories_taken = calories_taken + ? WHERE target_id = ?";
	        PreparedStatement pstmt = conn.prepareStatement(query);
	        pstmt.setDouble(1, consumed);
	        pstmt.setDouble(2, target_id);
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
		System.out.println("consumed cal of remaing: "+ totalcal);
	    try (Connection conn = DBConnection.connectDB()) {
	        User_Target target = TargetService.checkTarget(id);
	        double userTarget = target.getTargetCalories();
	        System.out.println("USer target:"+ userTarget);
	        System.out.println("Minus:" + (userTarget - totalcal));
	    	if(target.getTargetRemaining() - totalcal < 0) {
	    		System.out.println("Do nothing!");
	    	}
	    	else{
		        String query = "UPDATE user_target SET remaining = ?  WHERE status = ? AND user_id = ? ";
		        PreparedStatement pstmt = conn.prepareStatement(query);
	
		        pstmt.setDouble(1, target.getTargetRemaining() - totalcal);
		        pstmt.setString(2, "Active");
		        pstmt.setInt(3, id);
		        pstmt.executeUpdate();
	    	}
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
    
	public static int getSumOfCaloriesTaken(int id) {
		//SELECT SUM(calories) FROM food_consumption WHERE user_id = ?;
	    int totalCalories = 0;
		 try (Connection conn = DBConnection.connectDB();
		         PreparedStatement stmt = conn.prepareStatement("SELECT SUM(calories) FROM food_log WHERE user_id = ?")) {
		    	
		    	stmt.setInt(1, id);
		        ResultSet rs = stmt.executeQuery();
		        if(rs.next()) {
		            totalCalories = rs.getInt("SUM(calories)");
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
    public ObservableList<User_Target> loadUserTargetHistory(int id) {
        ObservableList<User_Target> historyList = FXCollections.observableArrayList();
        try (Connection conn = DBConnection.connectDB();
             PreparedStatement stmt = conn.prepareStatement("SELECT user_id, target, calories_taken, remaining, status, date, end_date FROM user_target WHERE user_id = ? ORDER BY date DESC")) {

            stmt.setInt(1, id); // Change this to dynamically get the current user's ID
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                User_Target userTarget = new User_Target();
                userTarget.setTargetCalories(rs.getDouble("target"));
                userTarget.setCaloriesTaken(rs.getDouble("calories_taken"));
                userTarget.setTargetRemaining(rs.getDouble("remaining"));
                userTarget.setStatus(rs.getString("status"));
                userTarget.setStartDate(rs.getDate("date").toLocalDate());
                userTarget.setEndDate(rs.getDate("end_date") != null ? rs.getDate("end_date").toLocalDate() : null);

                historyList.add(userTarget);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return historyList;
    }

}
