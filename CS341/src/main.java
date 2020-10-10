import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/* CS 341 - Final Project
 * Team 1 - Shyue Shi Leong, Ze Jia Lim, Steven Welter, and Anna Carney\
 * Main class for the YMCA registration program.
 */

public class main {

    private static final Boolean TRUE = true;
	private static final Boolean FALSE = false;

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
	
		System.out.println("Connected!\n");
		
		try {
			
			ArrayList<person> people = new ArrayList<>();
			
			person newPerson = new person("Rick", "Sanchez", "1-800-rickandmorty", "rick", "morty", FALSE, FALSE);
			db.insertEmployee(newPerson);
			
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
			
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		try {
			db.disconnect();
		} catch (SQLException e) {
			System.out.println("Disconnect failed...");
			e.printStackTrace();
		}
    }

}
