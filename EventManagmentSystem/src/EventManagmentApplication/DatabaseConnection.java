package EventManagmentApplication;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection 
{
	private static final String URL = "jdbc:mysql://localhost:3306/Event_Managment";
	private static final String USER = "root";
	private static final String PASSWORD = "root";
	
	public static Connection getConnection() throws SQLException 
	{
		return DriverManager.getConnection(URL, USER, PASSWORD);
	}
	
	public static void testConnection() 
	{
		try (Connection conn = getConnection()) 
		{
			if (conn != null) 
			{
				System.out.println("Database Connected Successfully");
			} 
			else 
			{
				System.out.println("Failed to connect to the database");
			}
		} 
		catch (SQLException e) 
		{
			System.out.println("Database Connection Failed");
			e.printStackTrace();
		}
	}

	public static void main(String[] args) 
	{
		testConnection(); // Call this method to check connection
	}
}
