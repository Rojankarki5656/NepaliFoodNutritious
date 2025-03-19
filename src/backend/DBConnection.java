package backend;
import java.sql.*;
public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/users";
    private static final String USER = "root";
    private static final String PASS = "Rojankarki123@";
    private static final String URLFood = "jdbc:mysql://localhost:3306/nutrition_db";


   
    public static Connection connectUserDB() {
    	try {
			Connection conn = DriverManager.getConnection(URL,USER,PASS);
			return conn;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }
    public static Connection connectFoodDB() {
    	try {
			Connection conn = DriverManager.getConnection(URLFood,USER,PASS);
			return conn;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }
}


