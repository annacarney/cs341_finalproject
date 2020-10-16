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
	
	//adds a new program to the database
	//returns 1 on success, 0 on fail
	public int addProgram(String classID, String className, String classDesc, String classSize, String startTime, String endTime, String memFee, String nonMemFee) {
		//classID, className, classDesc, classSize, startTime, endTime, memFee, nonMemFee
		
		//add error handling **************************
		int id;
		int size;
		double mfee;
		double nmfee;
		
		try { 
            id = Integer.parseInt(classID); 
            size = Integer.parseInt(classSize);
            mfee = Double.parseDouble(memFee);
            nmfee = Double.parseDouble(nonMemFee);
        } 
        catch (NumberFormatException e) { 
        	System.out.println("Number exception - not added");
        	return 1;
        } 
		
		program newProgram = new program(id, className, classDesc, size, startTime, endTime, mfee, nmfee);
		//program newProgram = new program(1121, "Karate", "Learn the art of karate", 25, "2020-10-25 08:30:00:000", "2020-10-25 09:30:00:000", 13.00, 23.23);
		try {
			//db.connect();
			db.insertProgram(newProgram);			//is this working? :/
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
		
		//need to write
		ArrayList<person> p = person.find(db, "Person.userName LIKE ('" + username + "')" + "AND Person.password LIKE ('" + password + "')" );
		person user = null;
		for(int i = 0; i < p.size(); i++) {
			user = p.get(i);
		}
		
		
		return user;
	}
	
	//returns the program names for all programs whose start time is time
	public String[] getProgramsFromTime(String time) {
		DateTimeFormatter sqlForm = DateTimeFormatter.ofPattern("yyyy-MM-dd[ ][]HH");		//want it in this form to query

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM y hh:mm a"); 			//currently in this form
		LocalDateTime sqlTime = LocalDateTime.parse(time, formatter);
		String t = sqlTime.format(sqlForm);
		System.out.println(t);
		
		ArrayList<program> p = program.find(db, "Program.startTime LIKE ('" + t + "%')");
		
		String[] ret = new String[p.size()];
		program prog = null;
		String s = "";
		for(int i = 0; i < p.size(); i++) {
			prog = p.get(i);
			s = prog.getClassName();
			System.out.println(s);
			ret[i] = s;
			s = "";
		}
		return ret;
	}
		
	// returns all available program times in a formatted way 
	public String[] getProgramTimesFormatted() {
		DateTimeFormatter dt = DateTimeFormatter.ofPattern("d MMM y hh:mm a");
		ArrayList<program> programs = program.findAll(db);
		
		String[] ret = new String[programs.size()];
		program p = null;
		String s = "";
		for(int i = 0; i < programs.size(); i++) {
			p = programs.get(i);
			s = p.getStartTimeAsDateTime().format(dt);
			//s = "Start: " + p.getStartTimeAsDateTime().format(dt) + " End: " + p.getEndTimeAsDateTime().format(dt);
			ret[i] = s;
			s = "";
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
		for(int i = 0; i < programs.size(); i++) {
			p = programs.get(i);
			s = p.getClassName();
			//s = p.getClassName() + ", Start: " + p.getStartTime() + ", End: " + p.getEndTime() + ", M= $" + p.getMemFee() + ", NM= $" + p.getNonMemFee() + ", " + p.getClassDesc();
			ret[i] = s;
			s = "";
		}
		return ret;
	}
	
	//enters a new non-member person into the database and enrolls them in the class in which they registered for
	// returns 1 on successful enrolling non-member, 0 if fails.
	public int enrollNM(String fname, String lname, String phone, String className){
		//fix me!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		
		//need to parse name!
		
		//person newPerson = new person("New", "Sanchez", "1-800-rickandmorty", "NM", "NM", false, false);
//		try {
//			db.insertEmployee(newPerson);
//		} catch (SQLException e) {
//			 TODO Auto-generated catch block
//			e.printStackTrace();
//		}
			
		return 1;
	}
	
	//registers a non member for a program they select
	public void registerNM(ListModel m, JFrame f) {
		String className = "";
		JLabel title = null;
		
		if(m == null || m.getSize()==0) {	//no selection
			title = new JLabel("No program selected. Please try again.");
			title.setBounds(170, 380, 600, 65);
			title.setHorizontalAlignment(SwingConstants.CENTER);
			title.setFont(new Font("SansSerif", Font.BOLD, 18));
			title.setForeground(new Color(0,76,153));
			f.add(title, 0);
			f.repaint();
			return;
		}
		
		className = (String) m.getElementAt(0);
		
		//JLabel title = new JLabel("Register for " + className + "?");
		title = new JLabel("Want to Register?");
		title.setBounds(293, 500, 600, 65);
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setFont(new Font("SansSerif", Font.BOLD, 25));
		title.setForeground(new Color(0,76,153));
		f.add(title, 0);
		f.repaint();
		
		//print class details
		String[] classinfo = null;
		try {
			classinfo = program_details(className);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		JList<String> l = new JList(classinfo);
		l.setFixedCellHeight(15);
	    l.setFixedCellWidth(100);
	    l.setVisibleRowCount(5);
	    l.setBounds(0,550,400,400);
	    l.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		l.setVisible(true);
	    f.add(l,0);
	    f.repaint();
		
		//get non member credentials - name and phone number 
	    JLabel fn = new JLabel("First Name: ");
		fn.setBounds(440, 500, 100, 150); //(x,y, width, height)
		fn.setHorizontalAlignment(SwingConstants.CENTER);
		fn.setFont(new Font("SansSerif", Font.BOLD, 15));
		fn.setForeground(Color.black);
		f.add(fn, 0);
		f.repaint();
	    
	    JLabel un = new JLabel("Last Name: ");
		un.setBounds(440, 550, 100, 150); //(x,y, width, height)
		un.setHorizontalAlignment(SwingConstants.CENTER);
		un.setFont(new Font("SansSerif", Font.BOLD, 15));
		un.setForeground(Color.black);
		f.add(un, 0);
		f.repaint();
		
		JLabel pw = new JLabel("Phone Number: ");
		pw.setBounds(404,600, 200, 150); //(x,y, width, height)
		pw.setHorizontalAlignment(SwingConstants.CENTER);
		pw.setFont(new Font("SansSerif", Font.BOLD, 15));
		pw.setForeground(Color.black);
		f.add(pw, 0);
		f.repaint();
		
		JTextField name = new JTextField(50);
		name.setBounds(600,560,150,40);
		f.add(name);
		f.repaint();
		
		JTextField lname = new JTextField(50);
		lname.setBounds(600,605,150,40);
		f.add(lname);
		f.repaint();
		
		JTextField phone = new JTextField(30);
		phone.setBounds(600,649,150,40);
		f.add(phone);
		f.repaint();
		
		//register user for class. add to database
		JButton reg = new JButton("Register!");
		reg.setBounds(550,700,150,30);
		reg.setBackground(new Color(127,0,255));
		reg.setForeground(Color.white);
		f.add(reg);
		f.repaint();
		
		// when button is clicked
		reg.addActionListener((new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				System.out.println(" " + name.getText() + phone.getText() + "Registering for " + m.getElementAt(0));
				int s = enrollNM(name.getText(), lname.getText(), phone.getText(), (String)m.getElementAt(0));
				if(s == 0) {	//failed to enroll user
					
				}
				}
			} ));
	}
	
	//returns the details for a program based on className
	private String[] program_details(String className) throws SQLException {
		ArrayList<program> programs = program.find(db, "Program.className IN ('" + className + "')");
		program p = programs.get(0);
		DateTimeFormatter dt = DateTimeFormatter.ofPattern("d MMM y hh:mm a");
		String[] ret = new String[8];
	
		ret[0] = "Program Details: ";
		ret[1] = p.getClassName();
		ret[2] = p.getClassDesc();
		ret[3] = "Class Size: " + p.getClassSize();
		ret[4] = "Start Time: " + p.getStartTimeAsDateTime().format(dt);
		ret[5] = "End Time: " + p.getEndTimeAsDateTime().format(dt);
		ret[6] = "Member Fee: $" + p.getMemFee();
		ret[7] = "Non Member Fee (your price): $" + p.getNonMemFee();
		
		return ret;
	}

}