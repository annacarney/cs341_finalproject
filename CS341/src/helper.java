
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

	/*
	 * constructor for the helper class
	 */
	public helper() {
		db = new database();

		try {
			db.connect();
		} catch (SQLException e) {
			System.out.println("Database connection failed...");
			e.printStackTrace();
		}
	}

	/*
	 * closes connection to the database
	 */
	public void closeDBConnection() {
		try {
			db.disconnect();
			System.out.print("Successfully disconnected from db");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * adds a new program to the database
	 *  returns 1 on success, 0 on fail
	 */
	public int addProgram(String classID, String className, String classDesc, String classSize, String startTime,
			String endTime, String memFee, String nonMemFee, String startDate, String endDate, String days,
			String loc) {
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

		//final parameter(isActive) = true, because we're adding a new program so it will automatically be active [SRW 12/6/2020]
		program newProgram = new program(id, className, classDesc, size, startTime, endTime, mfee, nmfee, startDate,
				endDate, days, loc, true);
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

	/*
	 * "signs in" a user from the database
	 * verifies the username/password is valid for staffmember/member
	 */
	public person signInUser(String username, String password) {

		// implemented password hashing
		String hashed = db.hashing(password);
		ArrayList<person> p = person.find(db,
				"Person.userName LIKE ('" + username + "')" + "AND Person.password LIKE ('" + hashed + "')");
		person user = null;
		for (int i = 0; i < p.size(); i++) {
			user = p.get(i);
		}
		if (user != null && !user.getIsActive()) {
			return null;
		}

		return user;
	}

	/*
	 * returns the program names for all programs whose start time is time
	 */
	public String[] getProgramsFromTime(String time) {

		ArrayList<program> p = program.find(db, "Program.startTime LIKE ('" + time + "%')");

		String[] ret = new String[p.size()];
		program prog = null;
		String s = "";
		for (int i = 0; i < p.size(); i++) {
			prog = p.get(i);
			s = prog.getClassName();
			if (!prog.getIsActive()) {
				s = s + " (Inactive)";
			}
			System.out.println(s);
			ret[i] = s;
			s = "";
		}
		return ret;
	}

	/*
	 * searches all programs, returns all that match the searched word
	 */
	public ArrayList<program> searchPrograms(String search) {

		ArrayList<program> p = program.find(db, "Program.className LIKE ('" + search + "%')");

		return p;
	}

	/*
	 * searches all programs with a classId equalling id
	 */
	public program searchProgramID(int id) {
		ArrayList<program> p = program.find(db, "Program.classID LIKE ('" + id + "%')");
		if (p.isEmpty()) {
			return null;
		} else {
			return p.get(0);
		}
	}

	/*
	 * searches a person
	 */
	public ArrayList<person> searchPeople(String search) {
		ArrayList<person> p = person.find(db, "Person.firstName LIKE ('" + search + "%')");

		if (p == null || p.isEmpty()) {
			p = person.find(db, "Person.userName LIKE ('" + search + "%')");
		}
		
		ArrayList<nonMember> nm = nonMember.find(db, "NonMember.firstName LIKE ('" + search + "%')");
		if(nm != null && !nm.isEmpty()) {
			for(int i = 0; i < nm.size(); i++) {
				nonMember cur = nm.get(i);
				person nonMempers = new person(cur.getFirstName(), cur.getLastName(), cur.getPhoneNumber(), cur.getPhoneNumber(), "", false, false, true);
				p.add(nonMempers);
			}
		} else {
			nm = nonMember.find(db, "NonMember.lastName LIKE ('" + search + "%')");
			for(int i = 0; i < nm.size(); i++) {
				nonMember cur = nm.get(i);
				person nonMempers = new person(cur.getFirstName(), cur.getLastName(), cur.getPhoneNumber(), cur.getPhoneNumber(), "", false, false, true);
				p.add(nonMempers);
			}
		} 
		
		return p;
	}

	/*
	 * returns all available program times in a formatted way
	 */
	public String[] getProgramTimes() {

		ArrayList<String> programs = program.findDistinctTimes(db);

		String[] ret = new String[programs.size()];
		String p = null;
		// String s = "";
		for (int i = 0; i < programs.size(); i++) {
			p = programs.get(i);
			ret[i] = p;
		}
		return ret;
	}

	/*
	 * returns a class name from a given user input string
	 */
	public String getNameFromUserInput(String progString) {

		String[] progs = progString.split("-");

		String sName = progs[1];
		System.out.print("Name: " + sName);

		return sName;
	}

	/*
	 * returns the class id from the gui display of the program -- needed to query
	 *  to show program details
	 */
	public int getIDFromUserInput(String programString) {
		int classId = 0;

		String[] progs = programString.split("-");

		String sId = progs[0];
		classId = Integer.parseInt(sId);

		return classId;
	}

	/*
	returns the available programs (WITHOUT CLASSID) to a list of strings
 for printing out for the user to see
	 * 
	 */
	public String[] getProgramsList() throws SQLException {
		ArrayList<program> programs = program.findAll(db);

		String[] ret = new String[programs.size()];
		program p = null;
		String s = "";
		for (int i = 0; i < programs.size(); i++) {
			p = programs.get(i);

			s = p.getClassID() + "-";
			s = s + p.getClassName();
			if (!p.getIsActive()) {
				s = s + " (Inactive)";
			}

			ret[i] = s;
			s = "";
		}
		return ret;
	}

	/*
	 shyue shi's validation method 
	 does not register a user for a class if there are any time conflicts or full capacity
	 * 
	 */
	public boolean validation(String m, person p, int classID) {
		ArrayList<program> tempProg = program.find(db, "Program.classID LIKE ('" + classID + "')");
		program progToEnroll = tempProg.get(0);

		String[] progStart = progToEnroll.getStartTime().split(":");
		String[] progEnd = progToEnroll.getEndTime().split(":");
		String[] progStartDate = progToEnroll.getStartDate().split("-");
		String[] progEndDate = progToEnroll.getEndDate().split("-");
		String[] progDay = progToEnroll.getDays().split(" ");

		ResultSet enrollResults;

		// check capacity
		try {
			if (isFull(db, classID) == true) {
				System.out.println("Program capacity full.");
				return false;
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		try {
			enrollResults = db.runQuery("SELECT * FROM Enrolled WHERE userName LIKE ('" + p.getUserName() + "')");
			while (enrollResults.next()) {
				int classEnrollID = enrollResults.getInt("classID");
				if (progToEnroll.getClassID() == classEnrollID) {
					System.out.println("Person already enrolled in this program!");
					return false;
				}
				ArrayList<program> tempProg2 = program.find(db, "Program.classID LIKE ('" + classEnrollID + "')");
				program progCheck = tempProg2.get(0);

				boolean check = false;

				// check if there are any day that could have conflict
				for (int i = 0; i < progDay.length; i++) {
					if (progCheck.getDays().contains(progDay[i])) {
						check = true;
					}
				}
				System.out.println("---------------------------------------------------------" + check);
				if (check == true) {
					String[] progCheckStart = progCheck.getStartTime().split(":");
					String[] progCheckEnd = progCheck.getEndTime().split(":");
					// check the time
					if (Integer.parseInt(progStart[0]) < Integer.parseInt(progCheckStart[0])) {
						if (Integer.parseInt(progEnd[0]) <= Integer.parseInt(progCheckStart[0])) {
							if (Integer.parseInt(progEnd[0]) == Integer.parseInt(progCheckStart[0])) {
								if (Integer.parseInt(progEnd[1]) > Integer.parseInt(progCheckStart[1])) {
									System.out.println("There is a clash because checking the minutes!");
									return false;
								}
							}
						} else {
							System.out.println("There is a clash when checking the hour!");
							return false;
						}
					}
					// check time
					else if (Integer.parseInt(progStart[0]) > Integer.parseInt(progCheckStart[0])) {
						if (Integer.parseInt(progCheckEnd[0]) <= Integer.parseInt(progStart[0])) {
							if (Integer.parseInt(progCheckEnd[0]) == Integer.parseInt(progStart[0])) {
								if (Integer.parseInt(progCheckEnd[1]) > Integer.parseInt(progStart[1])) {
									System.out.println("There is a clash because checking the minutes!");
									return false;
								}
							}
						} else {
							System.out.println("There is a clash when checking the hour!");
							return false;
						}
					}
					// if same hour but different minutes
					else if (Integer.parseInt(progStart[0]) == Integer.parseInt(progCheckStart[0])
							&& Integer.parseInt(progStart[1]) != Integer.parseInt(progCheckStart[1])) {
						if (Integer.parseInt(progStart[1]) < Integer.parseInt(progCheckStart[1])) {
							if (Integer.parseInt(progEnd[0]) > Integer.parseInt(progCheckStart[0])) {
								System.out.println("Same hour but overlapping hour");
								return false;
							}
							if (Integer.parseInt(progEnd[0]) == Integer.parseInt(progCheckStart[0])) {
								if (Integer.parseInt(progEnd[1]) > Integer.parseInt(progCheckStart[1])) {
									System.out.println("Same hour but overlapping minutes");
									return false;
								}
							}
						}
					}
					// if the time is exactly the same
					else if (progToEnroll.getStartTime().equalsIgnoreCase(progCheck.getStartTime())) {
						System.out.println("------------------------ENTERED ELSE IF---------------------------------");
						if (progToEnroll.getStartDate().equalsIgnoreCase(progCheck.getStartDate())) {
							System.out
									.println("There is a clash because both program start at the same time and date!");
							return false;
						}
						String[] progCheckStartDate = progCheck.getStartDate().split("-"); // progCheck.getStartTime().split("-");
						String[] progCheckEndDate = progCheck.getEndDate().split("-"); // progCheck.getEndTime().split("-");
						// check year
						if (Integer.parseInt(progStartDate[2]) < Integer.parseInt(progCheckStartDate[2])) {
							if (Integer.parseInt(progEndDate[2]) > Integer.parseInt(progCheckStartDate[2])) {
								System.out.println("End date overlap with other start year!");
								return false;
							} else if (Integer.parseInt(progEndDate[2]) == Integer.parseInt(progCheckStartDate[2])) {
								if (Integer.parseInt(progEndDate[0]) > Integer.parseInt(progCheckStartDate[0])) {
									System.out.println("Same year but end date overlap with other start month!");
									return false;
								} else if (Integer.parseInt(progEndDate[0]) == Integer
										.parseInt(progCheckStartDate[0])) {
									if (Integer.parseInt(progEndDate[1]) > Integer.parseInt(progCheckStartDate[1])) {
										System.out.println(
												"Same year , same month but end date overlap with other start day!");
										return false;
									}
								}
							}
						}
						// check year
						else if (Integer.parseInt(progStartDate[2]) > Integer.parseInt(progCheckStartDate[2])) {
							if (Integer.parseInt(progCheckEndDate[2]) > Integer.parseInt(progStartDate[2])) {
								System.out.println("End date overlap with other start year!");
								return false;
							} else if (Integer.parseInt(progCheckEndDate[2]) == Integer.parseInt(progStartDate[2])) {
								if (Integer.parseInt(progCheckEndDate[0]) > Integer.parseInt(progStartDate[0])) {
									System.out.println("Same year but end date overlap with other start month!");
									return false;
								} else if (Integer.parseInt(progCheckEndDate[0]) == Integer
										.parseInt(progStartDate[0])) {
									if (Integer.parseInt(progCheckEndDate[1]) > Integer.parseInt(progStartDate[1])) {
										System.out.println(
												"Same year , same month but end date overlap with other start day!");
										return false;
									}
								}
							}
						}
						// if the year is the same
						else if (Integer.parseInt(progStartDate[2]) == Integer.parseInt(progCheckStartDate[2])) {
							if (Integer.parseInt(progStartDate[0]) < Integer.parseInt(progCheckStartDate[0])) {
								if (Integer.parseInt(progEndDate[0]) > Integer.parseInt(progCheckStartDate[0])) {
									System.out.println("same year but end month overlap with other start month!");
									return false;
								} else if (Integer.parseInt(progEndDate[0]) == Integer
										.parseInt(progCheckStartDate[0])) {
									if (Integer.parseInt(progEndDate[1]) > Integer.parseInt(progCheckStartDate[1])) {
										System.out.println(
												"same year, same month but end date overlap with other start date!");
										return false;
									}
								}
							} else if (Integer.parseInt(progStartDate[0]) > Integer.parseInt(progCheckStartDate[0])) {
								if (Integer.parseInt(progCheckEndDate[0]) > Integer.parseInt(progStartDate[0])) {
									System.out.println("same year but end month overlap with other start month!");
									return false;
								} else if (Integer.parseInt(progCheckEndDate[0]) == Integer
										.parseInt(progStartDate[0])) {
									if (Integer.parseInt(progCheckEndDate[1]) > Integer.parseInt(progStartDate[1])) {
										System.out.println(
												"same year, same month but end date overlap with other start date!");
										return false;
									}
								}
							} else if (Integer.parseInt(progStartDate[0]) == Integer.parseInt(progCheckStartDate[0])) {
								if (Integer.parseInt(progStartDate[1]) < Integer.parseInt(progCheckStartDate[1])) {
									if (Integer.parseInt(progEndDate[1]) > Integer.parseInt(progCheckStartDate[1])) {
										System.out.println("same year, same month but day overlap other start date!");
										return false;
									}
								} else if (Integer.parseInt(progStartDate[1]) > Integer
										.parseInt(progCheckStartDate[1])) {
									if (Integer.parseInt(progCheckEndDate[1]) > Integer.parseInt(progStartDate[1])) {
										System.out.println("same year, same month but day overlap other start date!");
										return false;
									}
								} else {
									System.out.println("same start date and time!");
									return false;
								}
							}
						}
					}
				}
			}
		} catch (SQLException e) {
			System.out.println("DB Query failed in method validation()");
			return false;
		}
		return true;
	}

	/*
	 checks to see if a non member is already added to the databse, returns true
	 if yes, false otherwise
	 * 
	 */
	private boolean checkNMexist(nonMember nm) {
		ResultSet rs;
		try {
			rs = db.nonMemLookup(nm.getPhoneNumber());

			if (!rs.next()) {
				return false;
			}

		} catch (SQLException e) {
			System.out.print("Failed in checkNMexist");
		}

		return true;
	}

	/*
	 enters a new non-member person into the database and enrolls them in the
	 class in which they registered for
	returns 1 on successful enrolling non-member, 0 if fails.
	 * 
	 */
	public int enrollNM(String fname, String lname, String phone, String className, int classID) {

		nonMember newPerson = new nonMember(fname, lname, phone);

		// check first if nonmember are already in the db
		boolean checkNM = checkNMexist(newPerson);

		if (checkNM == false) { // non-member is not already entered in the database

			try {
				db.insertNonMember(newPerson);
			} catch (SQLException e) {
				System.out.print(e);
			}

		}

		/*
		validate that it is safe for user to enter class
		 * 
		 */
		person nm_pers = new person(fname, lname, phone, phone, null, false, false, true);
		boolean b = validation(className, nm_pers, classID);

		if (b == false) {
			// not okay to add user to class

			// add pop up gui
			JFrame popup = new JFrame();
			popup.setSize(400, 250);
			popup.setLayout(null);
			popup.setVisible(true);
			popup.getContentPane().setBackground(Color.white);
			popup.setTitle("Uh-Oh");
			ImageIcon icon = new ImageIcon("ymcalogo.JPG");
			popup.setIconImage(icon.getImage());

			JLabel title = new JLabel("Error : Unable to enroll in class.");
			title.setBounds(0, 0, 350, 90);
			title.setHorizontalAlignment(SwingConstants.CENTER);
			title.setFont(new Font("SansSerif", Font.BOLD, 15));
			title.setForeground(Color.red);
			popup.add(title, 0);
			popup.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

			System.out.print("Error: Cannot add non-member to this class due to conflicts.");
		} else {
			// okay to add user to class
			// enroll non member in the selected class
			enrolled en = new enrolled(phone, classID);
			try {
				db.insertEnrolled(en);

				JFrame popup = new JFrame();
				popup.setSize(350, 250);
				popup.setLayout(null);
				popup.setVisible(true);
				popup.getContentPane().setBackground(Color.white);
				popup.setTitle("Success!");
				ImageIcon icon = new ImageIcon("ymcalogo.JPG");
				popup.setIconImage(icon.getImage());

				ImageIcon banner = new ImageIcon("smallsmiley.PNG");
				JLabel bl = new JLabel(banner);
				bl.setBounds(120, 20, 300, 200); // (x,y, width, height)
				popup.add(bl);

				JLabel title = new JLabel("Succesfully enrolled!");
				title.setBounds(0, 0, 300, 90);
				title.setHorizontalAlignment(SwingConstants.CENTER);
				title.setFont(new Font("SansSerif", Font.BOLD, 15));
				title.setForeground(Color.black);
				popup.add(title, 0);
				popup.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

			} catch (SQLException e1) {
				System.out.print("db failed to enroll user in program");
				System.out.print(e1);
			}
		}

		return 1;
	}

	/*
	 registers a member for a program they select
	 * 
	 */
	public void registerM(String className, int classId, JFrame f, person p) {

		if (className == null || className.equals("")) { // no selection
			return;
		}

		// when button is clicked

		System.out.println(
				"ClassId" + classId + " " + p.getFirstName() + " " + p.getLastName() + "Registering for " + className);

		// validate here
		boolean b = validation(className, p, classId);
		System.out.println("Validation bool is : " + b);

		// add to database if true, error if false

		if (b == false) {
			// add pop up gui
			JFrame popup = new JFrame();
			popup.setSize(400, 250);
			popup.setLayout(null);
			popup.setVisible(true);
			popup.getContentPane().setBackground(Color.white);
			popup.setTitle("Uh-Oh");
			ImageIcon icon = new ImageIcon("ymcalogo.JPG");
			popup.setIconImage(icon.getImage());

			JLabel title = new JLabel("Error : Unable to enroll in class.");
			title.setBounds(0, 0, 350, 90);
			title.setHorizontalAlignment(SwingConstants.CENTER);
			title.setFont(new Font("SansSerif", Font.BOLD, 15));
			title.setForeground(Color.red);
			popup.add(title, 0);
			popup.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

			System.out.println("Error in enrolling member in program");
		} else {

			enrolled en = new enrolled(p.getUserName(), classId);
			try {
				db.insertEnrolled(en);

				JFrame popup = new JFrame();
				popup.setSize(350, 250);
				popup.setLayout(null);
				popup.setVisible(true);
				popup.getContentPane().setBackground(Color.white);
				popup.setTitle("Success!");
				ImageIcon icon = new ImageIcon("ymcalogo.JPG");
				popup.setIconImage(icon.getImage());

				ImageIcon banner = new ImageIcon("smallsmiley.PNG");
				JLabel bl = new JLabel(banner);
				bl.setBounds(120, 20, 300, 200); // (x,y, width, height)
				popup.add(bl);

				JLabel title = new JLabel("Succesfully enrolled!");
				title.setBounds(0, 0, 200, 90);
				title.setHorizontalAlignment(SwingConstants.CENTER);
				title.setFont(new Font("SansSerif", Font.BOLD, 15));
				title.setForeground(Color.black);
				popup.add(title, 0);
				popup.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

			} catch (SQLException e1) {
				System.out.print("db failed to enroll user in program");
			}
		}
	}

	/*
	returns the details for a program based on className
	 * 
	 */
	public String[] program_details(int classId, Boolean isMember) throws SQLException {
		ArrayList<program> programs = program.find(db, "Program.classId IN ('" + classId + "')");
		program p = programs.get(0);
		String[] ret = new String[11];

		ret[0] = "Program Details: ";
		ret[1] = p.getClassName();
		ret[2] = p.getClassDesc();
		ret[3] = "Class Size: " + p.getClassSize();

		if (isMember) {
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
	
	/*
	returns string array of only members and non members
	 * 
	 */
	public String[] getAllMemNonMem() {
		String[] ret = null;
		
		int val = 0;
		ArrayList<person> members = person.find(db, "Person.isStaff IN ('" + val + "') AND Person.isAdmin IN ('"+ val + "')");
		ArrayList<nonMember> nonmembers = nonMember.find(db, null);
		ret = new String[members.size() + nonmembers.size()];

		// iterate through members
		person p = null;
		String s = "";
		int index = 0;
		for (int i = 0; i < members.size(); i++) {
			p = members.get(i);

			s = p.getUserName() + ": ";
			s = s + p.getFirstName() + " ";
			s = s + p.getLastName();
			//if user is inactive, append (Inactive).
			if (p.getIsActive() == false) {
				s = s + " (Inactive)";
			}
			ret[i] = s;
			s = "";
			index++;
		}

		// iterate through non members
		nonMember nm = null;
		s = "";
		for (int j = 0; j < nonmembers.size(); j++) {
			nm = nonmembers.get(j);

			s = nm.getPhoneNumber() + ": ";
			s = s + nm.getFirstName() + " ";
			s = s + nm.getLastName();

			ret[index] = s;
			s = "";
			index++;
		}

		return ret;
		
	}

	/*
	 returns a string array of all users usernames/phonenumbers for both members
	 and non members
	 * 
	 */
	public String[] getAllUsers() {
		String[] ret = null;

		ArrayList<person> members = person.find(db, null);
		ArrayList<nonMember> nonmembers = nonMember.find(db, null);
		ret = new String[members.size() + nonmembers.size()];

		// iterate through members
		person p = null;
		String s = "";
		int index = 0;
		for (int i = 0; i < members.size(); i++) {
			p = members.get(i);

			s = p.getUserName() + ": ";
			s = s + p.getFirstName() + " ";
			s = s + p.getLastName();
			//if user is inactive, append (Inactive).
			if (p.getIsActive() == false) {
				s = s + " (Inactive)";
			}
			ret[i] = s;
			s = "";
			index++;
		}

		// iterate through non members
		nonMember nm = null;
		s = "";
		for (int j = 0; j < nonmembers.size(); j++) {
			nm = nonmembers.get(j);

			s = nm.getPhoneNumber() + ": ";
			s = s + nm.getFirstName() + " ";
			s = s + nm.getLastName();

			ret[index] = s;
			s = "";
			index++;
		}

		return ret;
	}

	/*
	 returns all classes enrolled in by a user
	 * 
	 */
	public String[] getClassesFromUser(String username) {
		String[] ret = null;
		int index = 0;
		String where = "Enrolled.username = ('" + username + "')";
		ArrayList<enrolled> enrolledres = enrolled.find(db, where);

		System.out.println("username " + username);

		enrolled e = null;
		String s = "";
		ret = new String[enrolledres.size()];

		for (int i = 0; i < enrolledres.size(); i++) {
			e = enrolledres.get(i);

			System.out.println("Got here -- " + e.getClassid());

			// add to string
			String temp = "Program.classID = ('" + e.getClassid() + "')";
			ArrayList<program> progdets = program.find(db, temp);

			if (!progdets.isEmpty()) {
				program found = progdets.get(0);

				s = found.getClassName() + " " + found.getStartDate() + "-" + found.getEndDate() + " "
						+ found.getStartTime() + "-" + found.getEndTime();
				ret[index] = s;

				System.out.println(s);

				index++;
				s = "";
			}

		}

		return ret;
	}

	/*
	 returns a string array of usernames that are enrolled in the class
	 * 
	 */
	public ArrayList<String> enrolled_details(int classId) throws SQLException {
		ArrayList<String> ret = new ArrayList<>();

		ResultSet enresults = db.runQuery("SELECT userName FROM Enrolled WHERE classID = ('" + classId + "')");

		while (enresults.next()) {
			String userName = enresults.getString("userName");
			System.out.println(userName);
			ret.add(userName);
		}

		return ret;
	}

	/*
	 returns the firstname, lastname, and username/phonenumber, in a string from a
	 given username/phonenumber
	 works for both members and nonmembers
	 * 
	 */
	public String lookup_tostring(String username, boolean isNM) throws SQLException {
		String ret = "";

		if (username.equals("") || username == null) { // check if empty
			return "";
		}

		if (isNM == true) { // nonmember
			ResultSet rs = db.nonMemLookup(username);

			if (rs.next()) {
				ret = "Non Member - Name: " + rs.getString("firstName");
				ret = ret + " " + rs.getString("lastName");
				ret = ret + " Phone Number: " + rs.getString("phoneNumber");
				System.out.print("non mem: " + ret);
			}

		} else { // member
			ResultSet rs = db.personLookup(username);

			if (rs.next()) {
				ret = "Member - Name: " + rs.getString("firstName");
				ret = ret + " " + rs.getString("lastName");
				ret = ret + " Username: " + rs.getString("userName");
				System.out.print("mem: " + ret);
			}
		}

		return ret;
	}

	/*
	checks if a class is full or not
	 * 
	 */
	private boolean isFull(database db, int classid) throws SQLException {
		ArrayList<program> programs = program.find(db, "classID = " + classid);
		program p = programs.get(0);
		int size = p.getClassSize();

		ResultSet res = db.runQuery("SELECT COUNT(*) AS c FROM Enrolled WHERE classID = " + classid);
		int count = res.getInt("c");

		if (count >= size) {
			return true;
		}

		return false;
	}

	/*
	 creates a new user and adds to the database
	 * 
	 */
	public void createUser(String fname, String lname, String phone, String un, String pw, boolean isStaff,
			boolean isAdmin) {
		// TODO Auto-generated method stub

		//final parameter (isActive) = true, because we're adding a new program so it will automatically be active [SRW 12/6/2020]
		person newPerson = new person(fname, lname, phone, un, pw, isStaff, isAdmin, true);
		try {
			db.insertEmployee(newPerson);
			System.out.print("Person added!");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	/*
	soft deletes a program from the database
	 * 
	 */
	public void deleteProg(program p) throws SQLException {
		int i = db.updateProg(p, "isActive", false);
		if (i > 1 || i < 1 ) {
			System.out.println("Something went wrong with deleteProg()");
		}
		System.out.println("Delete Successful");
	}
	
	/*
	soft deletes a person from the database
	 * 
	 */
	public void deletePers(person p) throws SQLException {
		//remove person if enrolled
		int numRemoved = db.removeEnrolled(p);
		System.out.println("Removed " + numRemoved + " instances from enrolled");
		
		int i = db.updatePers(p, "isActive", false);
		if (i > 1 || i < 1 ) {
			System.out.println("Something went wrong with deletePers()");
		}
		System.out.println("Delete Successful");
	}

}
