/*
 * database.java
 * Database IO Class
 * Created: 10/10/2020
 */

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
	
	/*
	 * connects the database
	 */
	public void connect() throws SQLException {
		connection = DriverManager.getConnection(url);
	}
	
	/*
	 * disconnects the database
	 */
	public void disconnect() throws SQLException {
		connection.close();
	}
	
	/*
	 * runs a query
	 */
	public ResultSet runQuery(String query) throws SQLException {
		PreparedStatement stmt = connection.prepareStatement(query);
		ResultSet results = stmt.executeQuery();
		return results;
	}
	
	/*
	 * for looking up a person
	 */
	public ResultSet personLookup(String username) throws SQLException {
		
		String query = "SELECT FirstName, LastName, Username FROM Person WHERE userName = ?";
		PreparedStatement stmt = connection.prepareStatement(query);
		stmt.setString(1, username);
		ResultSet results = stmt.executeQuery();
		return results;
		
	}
	
	/*
	 * for looking up a non member
	 */
	public ResultSet nonMemLookup(String phoneNumber) throws SQLException {
		
		String query = "SELECT FirstName, LastName, PhoneNumber FROM NonMember WHERE phoneNumber = ?";
		PreparedStatement stmt = connection.prepareStatement(query);
		stmt.setString(1, phoneNumber);
		ResultSet results = stmt.executeQuery();
		return results;
		
	}
	
	/*
	 * for looking up a program
	 */
	public ResultSet programLookup(int classid) throws SQLException {
		
		String query = "SELECT classID FROM Program WHERE classID = ?";
		PreparedStatement stmt = connection.prepareStatement(query);
		stmt.setInt(1, classid);
		ResultSet results = stmt.executeQuery();
		return results;
		
	}
	
	/*
	 * inserts an employee
	 */
	public void insertEmployee(person p) throws SQLException {
		String sql = "INSERT INTO Person (firstName, lastName, phoneNumber, userName, password, isStaff, isAdmin) VALUES (?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement stmt = connection.prepareStatement(sql);
		stmt.setString(1, p.getFirstName());
		stmt.setString(2, p.getLastName());
		stmt.setString(3, p.getPhoneNumber());
		stmt.setString(4, p.getUserName());
		// implemented password hashing
		String hashed = hashing(p.getPassword());
		stmt.setString(5, hashed);
		stmt.setBoolean(6, p.getIsStaff());
		stmt.setBoolean(7, p.getIsAdmin());
		
		stmt.execute();
	}
	
	/*
	 * members will use username for p, nonMembers will use phone number.
	 */
	public void insertEnrolled(enrolled en) throws SQLException {
		String sql = "INSERT INTO Enrolled (userName, classID) VALUES (?, ?)";
		PreparedStatement stmt = connection.prepareStatement(sql);
		stmt.setString(1, en.getUsername());
		stmt.setInt(2, en.getClassid());
		
		stmt.execute();
		
	}
	
	/*
	 * removes a person from a class
	 */
	public int removeEnrolled(person p) throws SQLException {
		String sql = "DELETE FROM Enrolled WHERE userName = ?";
		PreparedStatement stmt = connection.prepareStatement(sql);
		stmt.setString(1, p.getUserName());
		
		int numRowsAffected = stmt.executeUpdate();
		return numRowsAffected;
		
	}
	
	/*
	 * inserts a non member
	 */
	public void insertNonMember(nonMember nm) throws SQLException {
		String sql = "INSERT INTO NonMember (firstName, lastName, phoneNumber) VALUES (?, ?, ?)";
		PreparedStatement stmt = connection.prepareStatement(sql);
		stmt.setString(1, nm.getFirstName());
		stmt.setString(2, nm.getLastName());
		stmt.setString(3, nm.getPhoneNumber());
		
		stmt.execute();
	}
	
	/*
	 * inserts a program into the database
	 * parameter: program p 
	 */
	public void insertProgram(program p) throws SQLException {
		String sql = "INSERT INTO Program (classID, className, classDesc, classSize, startTime, endTime, memFee, nonMemFee, startDate, endDate, days, location) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement stmt = connection.prepareStatement(sql);
		stmt.setInt(1, p.getClassID());
		stmt.setString(2, p.getClassName());
		stmt.setString(3, p.getClassDesc());
		stmt.setInt(4, p.getClassSize());
		stmt.setString(5, p.getStartTime());
		stmt.setString(6, p.getEndTime());
		stmt.setDouble(7, p.getMemFee());
		stmt.setDouble(8, p.getNonMemFee());
		
		stmt.setString(9, p.getStartDate());
		stmt.setString(10, p.getEndDate());
		stmt.setString(11, p.getDays());
		stmt.setString(12, p.getLocation());
		
		stmt.execute();
	}
	
	/*
	 * password hashing implementation
	 * parameter: string 
	 */
	public String hashing(String passwordToHash) {
        String generatedPassword = null;
		try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Add password bytes to digest
            md.update(passwordToHash.getBytes());
            //Get the hash's bytes 
            byte[] bytes = md.digest();
            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            generatedPassword = sb.toString();
        } 
        catch (NoSuchAlgorithmException e) 
        {
            e.printStackTrace();
        }
        return generatedPassword;
	}
	
	/*
	 * updates a program
	 */
	public int updateProg (program p, String col, Boolean b) throws SQLException {
		String sql = "UPDATE Program SET " + col + " = " + b + " WHERE ClassID = (?)";
		//System.out.println(sql);
		PreparedStatement stmt = connection.prepareStatement(sql);
		stmt.setInt(1, p.getClassID());
		int numRowsAffected = stmt.executeUpdate();
		return numRowsAffected;
	}
	
	/*
	 * updates a person
	 */
	public int updatePers (person p, String col, Boolean b) throws SQLException {
		String sql = "UPDATE Person SET " + col + " = " + b + " WHERE Username = (?)";
		//System.out.println(sql);
		PreparedStatement stmt = connection.prepareStatement(sql);
		stmt.setString(1, p.getUserName());
		int numRowsAffected = stmt.executeUpdate();
		return numRowsAffected;
	}
}