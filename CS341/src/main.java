import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/* CS 341 - Final Project
 * Team 1 - Shyue Shi Leong, Ze Jia Lim, Steven Welter, and Anna Carney\
 * Main class for the YMCA registration program.
 */

public class main {

	public static void main(String[] args) {
    	
    	//test db connection
    	testdb();
    	
    	//generate welcome screen
    	gui g = new gui();
    	
    	
    }

    public static void testdb() {
    	database db = new database();
		
		try {
			db.connect();
		} catch (SQLException e) {
			System.out.println("Database connection failed...");
			e.printStackTrace();
		}
	
		//System.out.println("Connected!\n");
		
		try {
			
			ArrayList<person> people = new ArrayList<>();
			ArrayList<program> programs = new ArrayList<>();
			
			//Create a new person object.
			person newPerson = new person("Rick", "Sanchez", "1-800-rickandmorty", "rick", "morty", false, false);
			db.insertEmployee(newPerson);
			
			//Create a new program object.
			program newProgram = new program(1001, "Happy Feet", "This is a class for people who like to dance.", 15, "2020-10-22 08:00:00:000", "2020-10-22 09:00:00:000", 12.23, 34.23);
			db.insertProgram(newProgram);
			
			ResultSet results = db.runQuery("SELECT firstName, lastName, phoneNumber, userName, password, isAdmin, isStaff FROM Person");

			while(results.next()) {
				String firstName = results.getString("firstName");
				String lastName = results.getString("lastName");
				String phoneNumber = results.getString("phoneNumber");
				String userName = results.getString("userName");
				String password = results.getString("password");
				Boolean isAdmin = results.getBoolean("isAdmin");
				Boolean isStaff = results.getBoolean("isStaff");
				
				person e = new person(firstName, lastName, phoneNumber, userName, password, isAdmin, isStaff);
				people.add(e);
				
				
				System.out.println(e);
			}
			
			ResultSet pResults = db.runQuery("SELECT classID, className, classDesc, classSize, startTime, endTime, memFee, nonMemFee FROM Program");

			while(pResults.next()) {
				int classID = pResults.getInt("classID");
				String className = pResults.getString("className");
				String classDesc = pResults.getString("classDesc");
				int classSize = pResults.getInt("classSize");
				String startTime = pResults.getString("startTime");
				String endTime = pResults.getString("endTime");
				Double memFee = pResults.getDouble("memFee");
				Double nonMemFee = pResults.getDouble("nonMemFee");
				
				program p = new program(classID, className, classDesc, classSize, startTime, endTime, memFee, nonMemFee);
				programs.add(p);
				
				System.out.println(p);

			}
			
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		try {
			db.disconnect();
		} catch (SQLException e) {
			System.out.println("Disconnect failed...");
			e.printStackTrace();
		}
		//System.out.println("Disconnected from db.");
    }

}
