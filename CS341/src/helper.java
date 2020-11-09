
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

	// returns the available programs (WITHOUT CLASSID) to a list of strings
	// for printing out for the user to see
	public String[] getProgramsList() throws SQLException {
		ArrayList<program> programs = program.findAll(db);

		String[] ret = new String[programs.size()];
		program p = null;
		String s = "";
		for (int i = 0; i < programs.size(); i++) {
			p = programs.get(i);
			s = p.getClassName();
			// s = p.getClassName() + ", Start: " + p.getStartTime() + ", End: " +
			// p.getEndTime() + ", M= $" + p.getMemFee() + ", NM= $" + p.getNonMemFee() + ",
			// " + p.getClassDesc();
			ret[i] = s;
			s = "";
		}
		return ret;
	}

	// enters a new non-member person into the database and enrolls them in the
	// class in which they registered for
	// returns 1 on successful enrolling non-member, 0 if fails.
	public int enrollNM(String fname, String lname, String phone, String className, int classID) {
		// fix me!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

		// validate here 

		//check first if they are already in the db ************************************ NEED TO DO THIS !!!
		
		nonMember newPerson = new nonMember(fname, lname, phone);
		try {
			db.insertNonMember(newPerson);
		} catch (SQLException e) {
			System.out.print("Failed to add non mem to db");
		}
		
		//add to database
		enrolled en = new enrolled(phone, classID);
		try {
			db.insertEnrolled(en);
		} catch (SQLException e1) {
			System.out.print("db failed to enroll user in program");
		}

		return 1;
	}

	public boolean validation (String m, person p, int classID) {
		ArrayList<program> tempProg = program.find(db, "Program.classID LIKE ('"+classID+"%')");
		program progToEnroll = tempProg.get(0);
		String[] progStart = progToEnroll.getStartTime().split(":");
		String[] progEnd = progToEnroll.getEndTime().split(":");
		String[] progStartDate = progToEnroll.getStartDate().split("-");
		String[] progEndDate = progToEnroll.getEndDate().split("-");
		String[] progDay = progToEnroll.getDays().split(", ");
		ResultSet enrollResults;
		
		//check capacity
		
		try {
			enrollResults = db.runQuery("SELECT * FROM Enrolled WHERE userName = 'username'");
			//return true if there is nothing in the enroll results set
			while(enrollResults.next()) {
				int classEnrollID = enrollResults.getInt("classid");
				if(progToEnroll.getClassID() == classEnrollID) {
					System.out.println("Person already enrolled in this program!");
					return false;
				}
				ArrayList<program> tempProg2 = program.find(db, "Program.classID LIKE ('"+classEnrollID+"%')");
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
						String[] progCheckStartDate = progCheck.getStartTime().split("-");
						String[] progCheckEndDate = progCheck.getEndTime().split("-");
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
		/*ResultSet progToEnroll;
		ResultSet enrollResults;
		ResultSet progCheck;
		
		try {
			progToEnroll = db.programLookup(classID);
			enrollResults = db.runQuery("SELECT * FROM Enrolled WHERE userName = 'username'");
			int progToEnrollID = progToEnroll.getInt("classid");
			String start = progToEnroll.getString("startTime");
			String[] progStart = start.split(":");
			String end = progToEnroll.getString("endTime");
			String[] progEnd = end.split(":");
			String startDate = progToEnroll.getString("startDate");
			String[] progStartDate = startDate.split("-");
			String endDate = progToEnroll.getString("endDate");
			String[] progEndDate = endDate.split("-");
			String day = progToEnroll.getString("days");
			String[] progDays = day.split(", ");
			//check for capacity and return true if enrollResults is empty
			
			while(enrollResults.next()) {
				int classEnrollID = enrollResults.getInt("classid");
				if(progToEnrollID == classEnrollID) {
					System.out.println("Person already enrolled in this program!");
					return false;
				}
				progCheck = db.programLookup(classEnrollID);
				
				
				
				if() {
					
				}
				
			}
		}
		catch(SQLException e) {
			System.out.println("DB Query failed in method getProgramsList()");
			return false;
		}*/
		
		return true;
	}
	
	// registers a member for a program they select
	public void registerM(String m, JFrame f, person p) {
		final String classId; 
		
		if (m == null || m.equals("")) { // no selection
			return;
		}

		// register user for class. add to database
		JButton reg = new JButton("Register!");
		reg.setBounds(457, 410, 200, 40);
		reg.setBackground(new Color(22,65,45));
		reg.setForeground(Color.white);
		f.add(reg);
		f.repaint();
		
		String className = m;
		String[] classinfo = null;
		try {
			classinfo = program_details(className, true);
			classId = classinfo[10];			//keeps track of the classID * 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		JList<String> l = new JList(classinfo);
		l.setFixedCellHeight(15);
		l.setFixedCellWidth(100);
		l.setVisibleRowCount(5);
		l.setBounds(350, 200, 400, 200);
		l.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		l.setVisible(true);
		f.add(l, 0);
		f.repaint();

		// when button is clicked
		reg.addActionListener((new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int classID_int = Integer.parseInt(classId);
				System.out.println("ClassId" + classID_int + " " + p.getFirstName() + " " + p.getLastName() + "Registering for " + m);
				//implement me***********************************************************************************************
				
				//validate here
				
				
				//add to database
				enrolled en = new enrolled(p.getUserName(), classID_int);
				try {
					db.insertEnrolled(en);
				} catch (SQLException e1) {
					System.out.print("db failed to enroll user in program");
				}
				
			}
		}));
		
		
	}

	// registers a non member for a program they select
	public void registerNM(String m, JFrame f) {
		final String classId;
		String className = "";
		JLabel title = null;

		if (m == null || m.equals("")) { // no selection

			return;
		}

		className = m;

		title = new JLabel("Want to Register?");
		title.setBounds(460, 410, 200, 40);
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setFont(new Font("SansSerif", Font.PLAIN, 25));
		title.setForeground(new Color(51,102,0));
		f.add(title, 0);
		f.repaint();

		// print class details
		String[] classinfo = null;
		try {
			classinfo = program_details(className, false);
			classId = classinfo[10];
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		JList<String> l = new JList(classinfo);
		l.setFixedCellHeight(15);
		l.setFixedCellWidth(100);
		l.setVisibleRowCount(5);
		l.setBounds(350, 200, 400, 200);
		l.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		l.setVisible(true);
		f.add(l, 0);
		f.repaint();

		// get non member credentials - name and phone number
		JLabel fn = new JLabel("First Name: ");
		fn.setBounds(400, 390, 100, 150); // (x,y, width, height)
		fn.setHorizontalAlignment(SwingConstants.CENTER);
		fn.setFont(new Font("SansSerif", Font.PLAIN, 15));
		fn.setForeground(Color.black);
		f.add(fn, 0);
		f.repaint();

		JLabel un = new JLabel("Last Name: ");
		un.setBounds(400, 440, 100, 150); // (x,y, width, height)
		un.setHorizontalAlignment(SwingConstants.CENTER);
		un.setFont(new Font("SansSerif", Font.PLAIN, 15));
		un.setForeground(Color.black);
		f.add(un, 0);
		f.repaint();

		JLabel pw = new JLabel("Phone Number: ");
		pw.setBounds(335, 490, 200, 150); // (x,y, width, height)
		pw.setHorizontalAlignment(SwingConstants.CENTER);
		pw.setFont(new Font("SansSerif", Font.PLAIN, 15));
		pw.setForeground(Color.black);
		f.add(pw, 0);
		f.repaint();

		JTextField name = new JTextField(50);
		name.setBounds(560, 450, 150, 40);
		f.add(name);
		f.repaint();

		JTextField lname = new JTextField(50);
		lname.setBounds(560, 498, 150, 40);
		f.add(lname);
		f.repaint();

		JTextField phone = new JTextField(30);
		phone.setBounds(560, 542, 150, 40);
		f.add(phone);
		f.repaint();

		// register user for class. add to database
		JButton reg = new JButton("Register!");
		reg.setBounds(460, 600, 200, 40);
		reg.setBackground(new Color(22,65,45));		//(127, 0, 255));
		reg.setForeground(Color.white);
		f.add(reg);
		f.repaint();

		// when button is clicked
		reg.addActionListener((new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Class ID " + classId + " " + name.getText() + phone.getText() + "Registering for " + m);
				int classID_int = Integer.parseInt(classId);
				
				int s = enrollNM(name.getText(), lname.getText(), phone.getText(), m, classID_int);
				
				if (s == 0) { // failed to enroll user

				}
			}
		}));
	}

	// returns the details for a program based on className
	private String[] program_details(String className, Boolean isMember ) throws SQLException {
		ArrayList<program> programs = program.find(db, "Program.className IN ('" + className + "')");
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
