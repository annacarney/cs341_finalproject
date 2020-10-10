import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class database {

	/*
	 * load SQL driver (JDBC)
	 * - add to build path
	 * 
	 * set up our database (script)
	 * 
	 * connect to the database (Java)
	 * 
	 * modifications to the database (Java)
	 * - insert/update/delete data
	 * 
	 * querying data (Java)
	 * 
	 * disconnect from the database (Java)
	 * 
	 */
	
	private String url = "jdbc:sqlite:YMCA_db.db";
	
	private Connection connection;
	
	public database() {
		
	}
	
	public void connect() throws SQLException {
		connection = DriverManager.getConnection(url);
	}
	
	public void disconnect() throws SQLException {
		connection.close();
	}
	
	public ResultSet runQuery(String query) throws SQLException {
		PreparedStatement stmt = connection.prepareStatement(query);
		ResultSet results = stmt.executeQuery();
		return results;
	}
	
	public ResultSet personLookup(String username) throws SQLException {
		
		String query = "SELECT FirstName, LastName, Username FROM Person WHERE SSN = ?";
		PreparedStatement stmt = connection.prepareStatement(query);
		stmt.setString(1, username);
		ResultSet results = stmt.executeQuery();
		return results;
		
	}
	
	public void insertEmployee(person p) throws SQLException {
		String sql = "INSERT INTO Person (firstName, lastName, phoneNumber, userName, password, isStaff, isAdmin) VALUES (?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement stmt = connection.prepareStatement(sql);
		stmt.setString(1, p.getFirstName());
		stmt.setString(2, p.getLastName());
		stmt.setString(3, p.getPhoneNumber());
		stmt.setString(4, p.getUserName());
		stmt.setString(5, p.getPassword());
		stmt.setBoolean(6, p.getIsStaff());
		stmt.setBoolean(7, p.getIsAdmin());
		//int numRowsAffected = stmt.executeUpdate();
	}
	
}