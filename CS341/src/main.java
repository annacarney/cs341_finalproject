/* CS 341 - Final Project
 * Team 1 - Shyue Shi Leong, Ze Jia Lim, Steven Welter, and Anna Carney
 * Main class for the YMCA registration program.
 */
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class main {

	/*
	 * main
	 */
	public static void main(String[] args) {
    	
    	//generate welcome screen
    	gui g = new gui();
    	
    	//user logins--
    	//member -> us: jdoe pw: jdoe
    	//member -> us: aini pw: aini
    	//staff -> us: staff pw: staff
    	//admin -> us: admin pw: admin
    	
    }
	
	//testing function - for debugging purposes only. 
    public static void testdb() {
    	database db = new database();
    	
		try {
			db.connect();
		} catch (SQLException e) {
			System.out.println("Database connection failed...");
			e.printStackTrace();
		}
	
		
		try {
			
			ArrayList<person> people = new ArrayList<>();
			ArrayList<program> programs = new ArrayList<>();
			ArrayList<enrolled> enro = new ArrayList<>();
			
			//Create a new person object.
			person newPerson = new person("Anna", "Carney", "111-111-2222", "anna", "anna", false, false, true);
			db.insertEmployee(newPerson);
			
			//Create a new program object.
			//program newProgram = new program(1001, "Happy Feet", "This is a class for people who like to dance.", 15, "2020-10-22 08:00:00:000", "2020-10-22 09:00:00:000", 12.23, 34.23);
			//db.insertProgram(newProgram);
			
			//Create a new nonMember object.
			//nonMember newNon = new nonMember("Morty", "Sanchez", "608-790-3666");
			//db.insertNonMember(newNon);
			
			//Insert nonMember '608-790-3666' into classID 1
			ResultSet nonM, progg;
			String usname = "";
			int ii = 0;
			nonM = db.nonMemLookup("608-790-3666");
			progg = db.programLookup(1);
			while (nonM.next()) {
				usname = nonM.getString("phoneNumber");
			}
			while (progg.next()) {
				ii = progg.getInt("classID");
			}
			
			System.out.println("Adding username = " + usname + " ClassID = " + ii + " to the db");
			enrolled en = new enrolled(usname, ii);
			db.insertEnrolled(en);
			
			//Insert member 'rick' into classID 2
			ResultSet per, pp;
			String uname = "";
			int i = 0;
			per = db.personLookup("rick");
			pp = db.programLookup(2);
			while(per.next()) {
				uname = per.getString("userName");
			}
			while (pp.next()) {
				i = pp.getInt("classID");
			}
			
			System.out.println("Adding username = " + uname + " ClassID = " + i + " to the db");
			enrolled en1 = new enrolled(uname, i);
			db.insertEnrolled(en1);
			
			ResultSet results = db.runQuery("SELECT firstName, lastName, phoneNumber, userName, password, isAdmin, isStaff, isActive FROM Person");

			while(results.next()) {
				String firstName = results.getString("firstName");
				String lastName = results.getString("lastName");
				String phoneNumber = results.getString("phoneNumber");
				String userName = results.getString("userName");
				String password = results.getString("password");
				Boolean isAdmin = results.getBoolean("isAdmin");
				Boolean isStaff = results.getBoolean("isStaff");
				Boolean isActive = results.getBoolean("isActive");

				person e = new person(firstName, lastName, phoneNumber, userName, password, isAdmin, isStaff, isActive);
				people.add(e);
				
				
				System.out.println(e);
			}
			
			ResultSet pResults = db.runQuery("SELECT classID, className, classDesc, classSize, startTime, endTime, memFee, nonMemFee, startDate, endDate, days, location, isActive FROM Program");

			while(pResults.next()) {
				int classID = pResults.getInt("classID");
				String className = pResults.getString("className");
				String classDesc = pResults.getString("classDesc");
				int classSize = pResults.getInt("classSize");
				String startTime = pResults.getString("startTime");
				String endTime = pResults.getString("endTime");
				Double memFee = pResults.getDouble("memFee");
				Double nonMemFee = pResults.getDouble("nonMemFee");
				
				String startDate = pResults.getString("startDate");
				String endDate = pResults.getString("endDate");
				String days = pResults.getString("days");
				String location = pResults.getString("location");
				Boolean isActive = pResults.getBoolean("isActive");
				
				program p = new program(classID, className, classDesc, classSize, startTime, endTime, memFee, nonMemFee, startDate, endDate, days, location, isActive);
				programs.add(p);
				
				System.out.println(p);

			}
			
			ResultSet enresults = db.runQuery("SELECT userName, classID FROM Enrolled");

			while(enresults.next()) {
				String userName = enresults.getString("userName");
				int clID = enresults.getInt("classID");
				
				enrolled enr = new enrolled(userName, clID);
				enro.add(enr);
				
				System.out.println(enr);
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
