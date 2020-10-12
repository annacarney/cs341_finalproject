
/* CS 341 - Final Project
 * Team 1 - Shyue Shi Leong, Ze Jia Lim, Steven Welter, and Anna Carney\
 * This class helps with database queries and overall functionalities implemented. 
 */
import java.awt.Color;
import java.awt.Font;
import java.sql.ResultSet;
import java.sql.SQLException;
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

	// returns the available programs (WITHOUT CLASSID) to a list of strings
	// for printing out for the user to see
	public String[] getProgramsList() throws SQLException {
		
		ResultSet pResults;
		try {
			pResults = db.runQuery("SELECT classID, className, classDesc, classSize, startTime, endTime, memFee, nonMemFee FROM Program");
		} catch (SQLException e) {
			System.out.println("DB Query failed in method getProgramsList()");
			return null;
		}
		
		ArrayList<program> programs = new ArrayList<>();
	
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
		}
		
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
	
	//registers a non member for a program they select
	public void registerNM(ListModel m, JFrame f) {
		String className = "";
		
		if(m == null || m.getSize()==0) {	//no selection
			JLabel title = new JLabel("No program selected. Please try again.");
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
		JLabel title = new JLabel("Want to Register?");
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
		// TO DO *************
	    JLabel un = new JLabel("Name: ");
		un.setBounds(440, 550, 100, 150); //(x,y, width, height)
		un.setHorizontalAlignment(SwingConstants.CENTER);
		un.setFont(new Font("SansSerif", Font.BOLD, 15));
		un.setForeground(Color.black);
		f.add(un, 0);
		f.repaint();
		
		JLabel pw = new JLabel("Phone Number: ");
		pw.setBounds(420,600, 200, 150); //(x,y, width, height)
		pw.setHorizontalAlignment(SwingConstants.CENTER);
		pw.setFont(new Font("SansSerif", Font.BOLD, 15));
		pw.setForeground(Color.black);
		f.add(pw, 0);
		f.repaint();
		
		//text entry for username and pass (member and staff member login only)
		JTextField name = new JTextField(50);
		name.setBounds(600,580,150,50);
		f.add(name);
		f.repaint();
		
		JTextField phone = new JTextField(30);
		phone.setBounds(600,639,150,50);
		f.add(phone);
		f.repaint();
		
		//register user for class. add to database
	    // TO DO ***************************************
		
		JButton reg = new JButton("Register!");
		reg.setBounds(550,700,150,30);
		reg.setBackground(new Color(127,0,255));
		reg.setForeground(Color.white);
		f.add(reg);
		f.repaint();
	}
	
	
	//NOT USED! -- [creates a pop up window to register a user for a program]
	public void registerNM(ListModel m) {
		String className = "";
		
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		f.setSize(800,300);  	
		f.setLayout(null);
		f.setVisible(true);
		f.getContentPane().setBackground(new Color(204,203,255));
		f.setTitle("YMCA | Register for a Program ");
		ImageIcon icon = new ImageIcon("ymcalogo.JPG");
		f.setIconImage(icon.getImage());
		
		if(m == null || m.getSize()==0) {	//no selection
			JLabel title = new JLabel("ERROR! - No class selected. Please close this window.");
			title.setBounds(0, 0, 600, 65);
			title.setHorizontalAlignment(SwingConstants.CENTER);
			title.setFont(new Font("SansSerif", Font.BOLD, 18));
			title.setForeground(new Color(0,76,153));
			f.add(title, 0);
			
			return;
		}
		
		className = (String) m.getElementAt(0);
		
		JLabel title = new JLabel("Register for " + className + "?");
		title.setBounds(0, 0, 600, 65);
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setFont(new Font("SansSerif", Font.BOLD, 25));
		title.setForeground(new Color(0,76,153));
		f.add(title, 0);
		
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
	    l.setBounds(0,50,400,400);
	    l.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		l.setVisible(true);
	    f.add(l,0);
		
		//get non member credentials - name and phone number 
		// TO DO *************
	    JLabel un = new JLabel("Name: ");
		un.setBounds(440, 50, 100, 150); //(x,y, width, height)
		un.setHorizontalAlignment(SwingConstants.CENTER);
		un.setFont(new Font("SansSerif", Font.BOLD, 15));
		un.setForeground(Color.black);
		f.add(un, 0);
		
		JLabel pw = new JLabel("Phone Number: ");
		pw.setBounds(420, 100, 200, 150); //(x,y, width, height)
		pw.setHorizontalAlignment(SwingConstants.CENTER);
		pw.setFont(new Font("SansSerif", Font.BOLD, 15));
		pw.setForeground(Color.black);
		f.add(pw, 0);
		
		//text entry for username and pass (member and staff member login only)
		JTextField name = new JTextField(50);
		name.setBounds(600,90,150,50);
		f.add(name);

		JTextField phone = new JTextField(30);
		phone.setBounds(600,150,150,50);
		f.add(phone);
	    
		//register user for class. add to database
	    // TO DO ***************************************
		
		JButton reg = new JButton("Register!");
		reg.setBounds(550,200,150,30);
		reg.setBackground(new Color(127,0,255));
		reg.setForeground(Color.white);
		f.add(reg);
		
	}
	
	//returns the details for a program based on className
	private String[] program_details(String className) throws SQLException {
		String[] ret = new String[8];
		
		ResultSet pResults;
		try {
			pResults = db.runQuery("SELECT className, classDesc, classSize, startTime, endTime, memFee, nonMemFee FROM Program WHERE Program.className IN ('" + className + "')");
		} catch (SQLException e) {
			System.out.println("DB Query failed in method program_details()");
			return null;
		}
	
		while(pResults.next()) {
			ret[0] = "Program Details: ";
			String name = pResults.getString("className");
			ret[1] = name;
			String classDesc = pResults.getString("classDesc");
			ret[2] = classDesc;
			String classSize = "" + pResults.getInt("classSize");
			ret[3] = "Class Size: " + classSize;
			String startTime = pResults.getString("startTime");
			ret[4] = "Start Time: " + startTime;
			String endTime = pResults.getString("endTime");
			ret[5] = "End Time: " + endTime;
			String memFee = "" + pResults.getDouble("memFee");
			ret[6] = "Member Fee: $" + memFee;
			String nonMemFee = "" + pResults.getDouble("nonMemFee");
			ret[7] = "Non Member Fee (your price): $" + nonMemFee;
		}
		
		return ret;
	}

}
