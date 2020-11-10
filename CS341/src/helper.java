
/* CS 341 - Final Project
 * Team 1 - Shyue Shi Leong, Ze Jia Lim, Steven Welter, and Anna Carney
 * This class helps with database queries and overall functionalities implemented. 
 */
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

public class helper {

	private database db;

	// constructor for the helper class
	public helper() {
		db = new database();

		try {
			db.connect();
		} catch (SQLException e) {
			System.out.println("Database connection failed...");
			e.printStackTrace();
		}
	}

	// adds a new program to the database
	// returns 1 on success, 0 on fail
	public int addProgram(String classID, String className, String classDesc, String classSize, String startTime,
			String endTime, String memFee, String nonMemFee, String startDate, String endDate, String days, String loc) {
		// classID, className, classDesc, classSize, startTime, endTime, memFee,
		// nonMemFee

		// add error handling **************************
		int id;
		int size;
		double mfee;
		double nmfee;

		try {
			id = Integer.parseInt(classID);
			size = Integer.parseInt(classSize);
			mfee = Double.parseDouble(memFee);
			nmfee = Double.parseDouble(nonMemFee);
		} catch (NumberFormatException e) {
			System.out.println("Number exception - not added");
			return 1;
		}

		program newProgram = new program(id, className, classDesc, size, startTime, endTime, mfee, nmfee, startDate, endDate, days, loc);		
		// program newProgram = new program(1121, "Karate", "Learn the art of karate",
		// 25, "2020-10-25 08:30:00:000", "2020-10-25 09:30:00:000", 13.00, 23.23);
		try {
			// db.connect();
			db.insertProgram(newProgram); 
			System.out.print("new program added to db");
		} catch (SQLException e) {
			System.out.print("db failed to add program");
			return 0;
		}

		return 1;
	}

	// "signs in" a user from the database
	// verifies the username/password is valid for staffmember/member
	public person signInUser(String username, String password) {

		// need to write
		ArrayList<person> p = person.find(db,
				"Person.userName LIKE ('" + username + "')" + "AND Person.password LIKE ('" + password + "')");
		person user = null;
		for (int i = 0; i < p.size(); i++) {
			user = p.get(i);
		}

		return user;
	}

	// returns the program names for all programs whose start time is time
	public String[] getProgramsFromTime(String time) {
		//DateTimeFormatter sqlForm = DateTimeFormatter.ofPattern("yyyy-MM-dd[ ][]HH"); // want it in this form to query

		//DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM y hh:mm a"); // currently in this form
		//LocalDateTime sqlTime = LocalDateTime.parse(time, formatter);
		//String t = sqlTime.format(sqlForm);
		//System.out.println(t);

		ArrayList<program> p = program.find(db, "Program.startTime LIKE ('" + time + "%')");

		String[] ret = new String[p.size()];
		program prog = null;
		String s = "";
		for (int i = 0; i < p.size(); i++) {
			prog = p.get(i);
			s = prog.getClassName();
			System.out.println(s);
			ret[i] = s;
			s = "";
		}
		return ret;
	}

	// returns all available program times in a formatted way
	public String[] getProgramTimes() {

		ArrayList<String> programs = program.findDistinctTimes(db);			

		String[] ret = new String[programs.size()];
		String p = null;
		//String s = "";
		for (int i = 0; i < programs.size(); i++) {
			p = programs.get(i);
			ret[i] = p;
		}
		return ret;
	}
	
	
	public String getNameFromUserInput(String progString) {
		
		String[] progs = progString.split("-");	
		
		String sName = progs[1];
		System.out.print("Name: " + sName);
		
		return sName;
	}
	
	//returns the class id from the gui display of the program -- needed to query to show program details
	public int getIDFromUserInput(String programString) {
		int classId = 0;
		
		String[] progs = programString.split("-");	
		
		String sId = progs[0];
		classId = Integer.parseInt(sId);
		
		return classId;
	}

	// returns the available programs (WITHOUT CLASSID) to a list of strings
	// for printing out for the user to see
	public String[] getProgramsList() throws SQLException {
		ArrayList<program> programs = program.findAll(db);

		String[] ret = new String[programs.size()];
		program p = null;
		String s = "";
		for (int i = 0; i < programs.size(); i++) {
			p = programs.get(i);
			
			s = p.getClassID() + "-";
			s = s + p.getClassName();
					
			ret[i] = s;
			s = "";
		}
		return ret;
	}
	
	//shyue shi's validation method
	public boolean validation (String m, person p, int classID) {
		ArrayList<program> tempProg = program.find(db, "Program.classID LIKE ('"+classID+"')");
		program progToEnroll = tempProg.get(0);
		
		String[] progStart = progToEnroll.getStartTime().split(":");	
		String[] progEnd = progToEnroll.getEndTime().split(":");	
		String[] progStartDate = progToEnroll.getStartDate().split("-");
		String[] progEndDate = progToEnroll.getEndDate().split("-");
		String[] progDay = progToEnroll.getDays().split(", ");
		
		ResultSet enrollResults;
		
		//check capacity ? ****************************************
		
		try {
			enrollResults = db.runQuery("SELECT * FROM Enrolled WHERE userName LIKE ('" + p.getUserName() + "')");
			//return true if there is nothing in the enroll results set
			while(enrollResults.next()) {
				int classEnrollID = enrollResults.getInt("classID");
				if(progToEnroll.getClassID() == classEnrollID) {
					System.out.println("Person already enrolled in this program!");
					return false;
				}
				ArrayList<program> tempProg2 = program.find(db, "Program.classID LIKE ('"+classEnrollID+"')");
				program progCheck = tempProg2.get(0);
				
				boolean check=false;
				
				//check if there are any day that could have conflict
				for(int i=0;i<progDay.length;i++) {
					if(progCheck.getDays().contains(progDay[i])) {
						check = true;
					}
				}
				
				if(check=true) {
					String[] progCheckStart = progCheck.getStartTime().split(":");
					String[] progCheckEnd = progCheck.getEndTime().split(":");
					// check the time
					if(Integer.parseInt(progStart[0])< Integer.parseInt(progCheckStart[0])) {
						if(Integer.parseInt(progEnd[0])<=Integer.parseInt(progCheckStart[0])) {
							if(Integer.parseInt(progEnd[0])==Integer.parseInt(progCheckStart[0])) {
								if(Integer.parseInt(progEnd[1])>Integer.parseInt(progCheckStart[1])) {
									System.out.println("There is a clash because checking the minutes!");
									return false;
								}
							}
						}
						else {
							System.out.println("There is a clash when checking the hour!");
							return false;
						}
					}
					//check time
					else if(Integer.parseInt(progStart[0])> Integer.parseInt(progCheckStart[0])){
						if(Integer.parseInt(progCheckEnd[0])<=Integer.parseInt(progStart[0])) {
							if(Integer.parseInt(progCheckEnd[0])==Integer.parseInt(progStart[0])) {
								if(Integer.parseInt(progCheckEnd[1])>Integer.parseInt(progStart[1])) {
									System.out.println("There is a clash because checking the minutes!");
									return false;
								}
							}
						}
						else {
							System.out.println("There is a clash when checking the hour!");
							return false;
						}
					}
					// if same hour but different minutes
					else if(Integer.parseInt(progStart[0]) == Integer.parseInt(progCheckStart[0]) && Integer.parseInt(progStart[1]) != Integer.parseInt(progCheckStart[1])) {
						if(Integer.parseInt(progStart[1])< Integer.parseInt(progCheckStart[1])){
							if(Integer.parseInt(progEnd[0]) > Integer.parseInt(progCheckStart[0])) {
								System.out.println("Same hour but overlapping hour");
								return false;
							}
							if(Integer.parseInt(progEnd[0]) == Integer.parseInt(progCheckStart[0])) {
								if(Integer.parseInt(progEnd[1]) > Integer.parseInt(progCheckStart[1])) {
									System.out.println("Same hour but overlapping minutes");
									return false;
								}
							}
						}
					}
					//if the time is exactly the same
					else if(progToEnroll.getStartTime().equalsIgnoreCase(progCheck.getStartTime())){
						if(progToEnroll.getStartDate().equalsIgnoreCase(progCheck.getStartDate())) {
							System.out.println("There is a clash because both program start at the same time and date!");
							return false;
						}
						String[] progCheckStartDate = progCheck.getStartDate().split("-");		//progCheck.getStartTime().split("-");
						String[] progCheckEndDate = progCheck.getEndDate().split("-");			//progCheck.getEndTime().split("-");
						// check year
						if(Integer.parseInt(progStartDate[0]) < Integer.parseInt(progCheckStartDate[0])) {
							if(Integer.parseInt(progEndDate[0])>Integer.parseInt(progCheckStartDate[0])) {
								System.out.println("End date overlap with other start year!");
								return false;
							}
							else if(Integer.parseInt(progEndDate[0])==Integer.parseInt(progCheckStartDate[0])) {
								if(Integer.parseInt(progEndDate[1])>Integer.parseInt(progCheckStartDate[1])) {
									System.out.println("Same year but end date overlap with other start month!");
									return false;
								}
								else if(Integer.parseInt(progEndDate[1])==Integer.parseInt(progCheckStartDate[1])){
									if(Integer.parseInt(progEndDate[2])>Integer.parseInt(progCheckStartDate[2])) {
										System.out.println("Same year , same month but end date overlap with other start day!");
										return false;
									}
								}
							}
						}
						// check year
						else if(Integer.parseInt(progStartDate[0]) > Integer.parseInt(progCheckStartDate[0])) {
							if(Integer.parseInt(progCheckEndDate[0])>Integer.parseInt(progStartDate[0])) {
								System.out.println("End date overlap with other start year!");
								return false;
							}
							else if(Integer.parseInt(progCheckEndDate[0])==Integer.parseInt(progStartDate[0])) {
								if(Integer.parseInt(progCheckEndDate[1])>Integer.parseInt(progStartDate[1])) {
									System.out.println("Same year but end date overlap with other start month!");
									return false;
								}
								else if(Integer.parseInt(progCheckEndDate[1])==Integer.parseInt(progStartDate[1])){
									if(Integer.parseInt(progCheckEndDate[2])>Integer.parseInt(progStartDate[2])) {
										System.out.println("Same year , same month but end date overlap with other start day!");
										return false;
									}
								}
							}
						}
						// if the year is the same
						else {
							if(Integer.parseInt(progStartDate[1]) < Integer.parseInt(progCheckStartDate[1])) {
								if(Integer.parseInt(progEndDate[1])> Integer.parseInt(progCheckStartDate[1])) {
									System.out.println("same year but end month overlap with other start month!");
									return false;
								}
								else if(Integer.parseInt(progEndDate[1])== Integer.parseInt(progCheckStartDate[1])) {
									if(Integer.parseInt(progEndDate[2])> Integer.parseInt(progCheckStartDate[2])) {
										System.out.println("same year, same month but end date overlap with other start date!");
										return false;
									}
								}
							}
							else if(Integer.parseInt(progStartDate[1]) > Integer.parseInt(progCheckStartDate[1])) {
								if(Integer.parseInt(progCheckEndDate[1])> Integer.parseInt(progStartDate[1])) {
									System.out.println("same year but end month overlap with other start month!");
									return false;
								}
								else if(Integer.parseInt(progCheckEndDate[1])== Integer.parseInt(progStartDate[1])) {
									if(Integer.parseInt(progCheckEndDate[2])> Integer.parseInt(progStartDate[2])) {
										System.out.println("same year, same month but end date overlap with other start date!");
										return false;
									}
								}
							}
							else if(Integer.parseInt(progStartDate[1]) == Integer.parseInt(progCheckStartDate[1])){
								if(Integer.parseInt(progStartDate[2]) < Integer.parseInt(progCheckStartDate[2])) {
									if(Integer.parseInt(progEndDate[2]) > Integer.parseInt(progCheckStartDate[2])) {
										System.out.println("same year, same month but day overlap other start date!");
										return false;
									}
								}
								else if(Integer.parseInt(progStartDate[2]) > Integer.parseInt(progCheckStartDate[2])) {
									if(Integer.parseInt(progCheckEndDate[2]) > Integer.parseInt(progStartDate[2])) {
										System.out.println("same year, same month but day overlap other start date!");
										return false;
									}
								}
								else {
									System.out.println("same start date and time!");
									return false;
								}
							}
						}
					}
				}
				else{
					return true;
				}
				
			}
		}
		catch(SQLException e) {
			System.out.println("DB Query failed in method validation()");
			return false;
		}
		
		return true;
	}

	// enters a new non-member person into the database and enrolls them in the
	// class in which they registered for
	// returns 1 on successful enrolling non-member, 0 if fails.
	public int enrollNM(String fname, String lname, String phone, String className, int classID) {
		// fix me!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

		nonMember newPerson = new nonMember(fname, lname, phone);

		//check first if they are already in the db ************************************ NEED TO DO THIS !!!
		
		try {
			db.insertNonMember(newPerson);
		} catch (SQLException e) {
			System.out.print("Failed to add non mem to db");
		}
		
		//validate that it is safe for user to enter class
		person nm_pers = new person(fname, lname, phone, phone, null, false, false);
		boolean b = validation(className, nm_pers, classID);
		
		if(b == false) {
			//not okay to add user to class
			
			//add pop up gui
			JFrame popup = new JFrame();
			popup.setSize(400,250);
			popup.setLayout(null);
			popup.setVisible(true);
			popup.getContentPane().setBackground(Color.white);
			popup.setTitle("Uh-Oh");
			ImageIcon icon = new ImageIcon("ymcalogo.JPG");
			popup.setIconImage(icon.getImage());
			
			JLabel title = new JLabel("Unable to enroll in class due to time conflict.");
			title.setBounds(0, 0, 350, 90);
			title.setHorizontalAlignment(SwingConstants.CENTER);
			title.setFont(new Font("SansSerif", Font.BOLD, 15));
			title.setForeground(Color.black);
			popup.add(title, 0);
			popup.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);	
			
			System.out.print("Error: Cannot add non-member to this class due to conflicts.");
		} else {
			//okay to add user to class
			
		//enroll non member in the selected class
		enrolled en = new enrolled(phone, classID);
		try {
			db.insertEnrolled(en);
		} catch (SQLException e1) {
			System.out.print("db failed to enroll user in program");
		}
		}

		return 1;
	}

	// registers a member for a program they select
	public void registerM(String className, int classId, JFrame f, person p) { 
		
		if (className == null || className.equals("")) { // no selection
			return;
		}
		
		// when button is clicked
		
				System.out.println("ClassId" + classId + " " + p.getFirstName() + " " + p.getLastName() + "Registering for " + className);
				
				//implement me***********************************************************************************************
				
				//validate here
				boolean b = validation(className, p, classId);
				System.out.println("Validation bool is : " + b);
				
				//add to database if true, error if false
				
				if(b == false) {
					//add pop up gui
					JFrame popup = new JFrame();
					popup.setSize(400,250);
					popup.setLayout(null);
					popup.setVisible(true);
					popup.getContentPane().setBackground(Color.white);
					popup.setTitle("Uh-Oh");
					ImageIcon icon = new ImageIcon("ymcalogo.JPG");
					popup.setIconImage(icon.getImage());
					
					JLabel title = new JLabel("Unable to enroll in class due to time conflict.");
					title.setBounds(0, 0, 350, 90);
					title.setHorizontalAlignment(SwingConstants.CENTER);
					title.setFont(new Font("SansSerif", Font.BOLD, 15));
					title.setForeground(Color.black);
					popup.add(title, 0);
					popup.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);	
					
					System.out.println("Error in enrolling member in program");
				} else {
			
				enrolled en = new enrolled(p.getUserName(), classId);
				try {
					db.insertEnrolled(en);
				} catch (SQLException e1) {
					System.out.print("db failed to enroll user in program");
				}		
				}					
	}

	// returns the details for a program based on className
	public String[] program_details(int classId, Boolean isMember ) throws SQLException {
		ArrayList<program> programs = program.find(db, "Program.classId IN ('" + classId + "')");
		program p = programs.get(0);
		String[] ret = new String[11];

		ret[0] = "Program Details: ";
		ret[1] = p.getClassName();
		ret[2] = p.getClassDesc();
		ret[3] = "Class Size: " + p.getClassSize();
		
		if(isMember) {
			ret[4] = "Member Fee (your price): $" + p.getMemFee();
			ret[5] = "Non Member Fee: $" + p.getNonMemFee();
		} else {
			ret[4] = "Member Fee: $" + p.getMemFee();
			ret[5] = "Non Member Fee (your price): $" + p.getNonMemFee();
		}
		
		ret[6] = "Dates: " + p.getStartDate() + " through " + p.getEndDate();
		ret[7] = "Time: " + p.getStartTime() + " - " + p.getEndTime();
		ret[8] = "Days: " + p.getDays();
		ret[9] = "Location: " + p.getLocation();
		
		ret[10] = "" + p.getClassID();

		return ret;
	}

}
