package backend;
import java.sql.*;
public class DBConnection {
    private static final String USER = "root";
    private static final String PASS = "Rojankarki123@";
    private static final String URL = "jdbc:mysql://localhost:3306/nutrition_db";


   
    public static Connection connectDB() {
    	try {
			Connection conn = DriverManager.getConnection(URL,USER,PASS);
			return conn;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }
}


