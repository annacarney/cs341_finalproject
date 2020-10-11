/*
 * database.java
 * Database IO Class
 * Created: 10/10/2020
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class database {

	
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
		
		String query = "SELECT FirstName, LastName, Username FROM Person WHERE userName = ?";
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
	}
	
	public void insertProgram(program p) throws SQLException {
		String sql = "INSERT INTO Program (classID, className, classDesc, classSize, startTime, endTime, memFee, nonMemFee) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement stmt = connection.prepareStatement(sql);
		stmt.setInt(1, p.getClassID());
		stmt.setString(2, p.getClassName());
		stmt.setString(3, p.getClassDesc());
		stmt.setInt(4, p.getClassSize());
		stmt.setString(5, p.getStartTime());
		stmt.setString(6, p.getEndTime());
		stmt.setDouble(7, p.getMemFee());
		stmt.setDouble(8, p.getNonMemFee());
	}
	
	// returns the available programs 
	public program[] getPrograms() {
		program[] progs = null;
		
		
		return progs;
	}
	
}