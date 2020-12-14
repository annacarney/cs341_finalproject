/* CS 341 - Final Project
 * Team 1 - Shyue Shi Leong, Ze Jia Lim, Steven Welter, and Anna Carney\
 * This class sets up the graphical user interface for the staff user.
 */
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;

public class staffGUI {

	private person u;
	private helper h;
	private JFrame m; // the window for this page
	private boolean pressedAddB;
	private boolean pressedviewprogB;

	/*
	 constructor
	 * 
	 */
	public staffGUI(person p) {
		u = p;
		pressedAddB = false;
		pressedviewprogB = false;
		h = new helper();
		start();
	}

	/*
	displays all available programs
	 * 
	 */
	private void viewPrograms() {

		JLabel text = new JLabel("Search Programs:");
		text.setBounds(0, 0, 780, 65);
		text.setHorizontalAlignment(SwingConstants.CENTER);
		text.setFont(new Font("SansSerif", Font.BOLD, 35));
		text.setForeground(new Color(128, 128, 0));
		m.add(text, 0);

		String[] programs = null;
		try {
			programs = h.getProgramsList();
		} catch (SQLException e1) {
			System.out.print("Db query failed.");
		}

		DefaultListModel lister = new DefaultListModel();
		int avail_progsSize = programs.length;
		for (int i = 0; i < programs.length; i++) {
			lister.addElement(programs[i]);
		}
		JList<String> avail_progs = new JList<>(lister);

		// lists the available programs
		JList<String> jListSelect = new JList<>();
		avail_progs.setFixedCellHeight(15);
		avail_progs.setFixedCellWidth(100);
		avail_progs.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		avail_progs.setVisibleRowCount(5);
		avail_progs.setBounds(180, 180, 400, 150);
		avail_progs.setVisible(true);

		JScrollPane scrollableTextArea = new JScrollPane(avail_progs);
		scrollableTextArea.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollableTextArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollableTextArea.setMinimumSize(new Dimension(100, 50));
		scrollableTextArea.setBounds(180, 180, 400, 150);
		m.add(scrollableTextArea);
		m.repaint();

		// search text bar
		JTextField searchField = new JTextField();
		searchField.setBounds(120, 80, 300, 45); // x y width height
		m.add(searchField);
		m.repaint();

		JButton searchB = new JButton("Search");
		searchB.setBounds(470, 86, 130, 30);
		searchB.setBackground(new Color(51, 102, 0));
		searchB.setForeground(Color.white);
		m.add(searchB);
		m.repaint();

		searchB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				ArrayList<program> searchResults = h.searchPrograms(searchField.getText());

				if (searchResults.isEmpty()) {
					lister.clear();
					lister.addElement("No results found.");
				} else {
					lister.clear();
					for (int i = 0; i < searchResults.size(); i++) {
						program p = searchResults.get(i);
						// lister.addElement(p);
						String progView = p.getClassID() + "-" + p.getClassName();
						lister.addElement(progView);
					}
				}
			}
		});

		JButton selectB = new JButton("Select");
		selectB.setBounds(330, 350, 130, 30);
		selectB.setBackground(new Color(1, 50, 32));
		selectB.setForeground(Color.white);
		m.add(selectB);
		m.repaint();

		DefaultListModel lister2 = new DefaultListModel();
		JList<String> searchResultslist = new JList<>(lister2);
		searchResultslist.setFixedCellHeight(15);
		searchResultslist.setFixedCellWidth(100);
		searchResultslist.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		searchResultslist.setVisibleRowCount(5);
		searchResultslist.setBounds(135, 400, 500, 120);
		searchResultslist.setVisible(true);
		m.add(searchResultslist);

		selectB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// display all the details for the selected program
				String selected = avail_progs.getSelectedValue();

				if (selected != null && selected != "") {
					int classId = h.getIDFromUserInput(selected);
					program selectedprog = h.searchProgramID(classId);

					if (selectedprog != null) {
						lister2.clear();
						lister2.addElement(selectedprog.getClassName());
						lister2.addElement(selectedprog.getStartDate() + " through " + selectedprog.getEndDate() + " "
								+ selectedprog.getStartTime() + " to " + selectedprog.getEndTime());
						lister2.addElement("Day(s): " + selectedprog.getDays());
						lister2.addElement("Location: " + selectedprog.getLocation());
						lister2.addElement("Size: " + selectedprog.getClassSize());
						lister2.addElement("Members: $" + selectedprog.getMemFee() + " Non-Members: $"
								+ selectedprog.getNonMemFee());
						lister2.addElement(selectedprog.getClassDesc());
					}
				}
			}
		});

		JButton deleteB = new JButton("Delete");
		deleteB.setBounds(280, 550, 100, 30);
		deleteB.setBackground(Color.red);
		deleteB.setForeground(Color.white);
		m.add(deleteB);
		m.repaint();

		deleteB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Steven to add delete functionality here.
				// If program selected, set isActive = FALSE
				String selected = avail_progs.getSelectedValue();
				if (selected != null && selected != "") {
					int classId = h.getIDFromUserInput(selected);
					program selectedprog = h.searchProgramID(classId);
					try {
						h.deleteProg(selectedprog);
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					lister2.addElement(selectedprog.getClassName() + " successfully deleted");
					// viewPrograms();
				}
				
				JFrame popup = new JFrame();
				popup.setSize(350, 250);
				popup.setLayout(null);
				popup.setVisible(true);
				popup.getContentPane().setBackground(Color.white);
				popup.setTitle("Program Deleted!");
				ImageIcon icon = new ImageIcon("ymcalogo.JPG");
				popup.setIconImage(icon.getImage());

				ImageIcon banner = new ImageIcon("smallsmiley.PNG");
				JLabel b = new JLabel(banner);
				b.setBounds(120, 20, 300, 200); // (x,y, width, height)
				popup.add(b);

				JLabel title = new JLabel("Program Deleted!");
				title.setBounds(0, 0, 200, 90);
				title.setHorizontalAlignment(SwingConstants.CENTER);
				title.setFont(new Font("SansSerif", Font.BOLD, 15));
				title.setForeground(Color.black);
				popup.add(title, 0);
				popup.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

			}
		});

		JButton enrolledB = new JButton("Enrolled");
		enrolledB.setBounds(400, 550, 100, 30);
		enrolledB.setBackground(Color.blue);
		enrolledB.setForeground(Color.white);
		m.add(enrolledB);
		m.repaint();

		enrolledB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// find all users enrolled in program

				if (avail_progs.getSelectedValue().equals("")) {
				} else {
					String progString = avail_progs.getSelectedValue();
					int classId = h.getIDFromUserInput(progString);
					String[] enrolled = null;
					ArrayList<String> enrolledUserNames;
					try {
						enrolledUserNames = h.enrolled_details(classId);

						// for each user name, create a person object (if member) or nonmember object
						// have a string array with each object's First Name, Last Name, and
						// username/phonenumber
						enrolled = new String[enrolledUserNames.size() + 1];

						for (int i = 0; i < enrolledUserNames.size(); i++) {

							String cur_un = enrolledUserNames.get(i);

							System.out.print("username" + cur_un);
							if (cur_un.contains("-")) { // is a non-member
								// get nonmember person from db.nonMemLookup, return a string of FirstName,
								// LastName, UserName
								String s = h.lookup_tostring(cur_un, true);
								enrolled[i] = s;
							} else { // is a member
								String s = h.lookup_tostring(cur_un, false);
								enrolled[i] = s;
							}

						}

					} catch (SQLException eee) {
						// TODO Auto-generated catch block
						eee.printStackTrace();
						return;
					}
					
					JFrame popup = new JFrame();
					popup.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					JList<String> l = new JList(enrolled);
					l.setFixedCellHeight(15);
					l.setFixedCellWidth(100);
					l.setVisibleRowCount(5);
					l.setBounds(0, 0, 400, 200);
					l.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
					l.setVisible(true);
					popup.add(l, 0);
					popup.repaint();
					
					popup.setSize(400,200);  	//800 width, 800 height
					popup.setLayout(null);
					popup.setVisible(true);
					popup.getContentPane().setBackground(Color.white);		//background_color);
					popup.setTitle("Enrolled Users ");
					ImageIcon icon = new ImageIcon("ymcalogo.JPG");
					popup.setIconImage(icon.getImage());

				}

			}
		});

		JButton exitB = new JButton("Sign Out");
		exitB.setBounds(400, 690, 100, 30);
		exitB.setBackground(Color.gray);
		exitB.setForeground(Color.white);
		m.add(exitB);
		m.repaint();

		exitB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// close db connection in helper
				h.closeDBConnection();

				m.dispose();
			}
		});
		
		JButton helpB = new JButton("Help");
		helpB.setBounds(280, 690, 100, 30);
		helpB.setBackground(Color.gray);
		helpB.setForeground(Color.white);
		m.add(helpB);
		m.repaint();
		
		helpB.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) {
            	try {
            		File htmlFile = new File("staffFAQ.html");
            		Desktop.getDesktop().browse(htmlFile.toURI());
            	
            	} catch (Exception ee) {}
            }
        });

	}

	/*
	 search feature to find all programs in which a user is enrolled in
	 * 
	 */
	private void searchEnrolled() {

		// get all users -- nonmembers and members
		String[] users = h.getAllUsers();
		
		if(u.getIsAdmin()) {
			users = h.getAllUsers();
		}else {
			users = h.getAllMemNonMem();
		}

		DefaultListModel lister = new DefaultListModel();
		for (int i = 0; i < users.length; i++) {
			lister.addElement(users[i]);
		}
		JList<String> user_search = new JList<>(lister);
		JList<String> jListSelect = new JList<>();
		user_search.setFixedCellHeight(15);
		user_search.setFixedCellWidth(100);
		user_search.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		user_search.setVisibleRowCount(5);
		user_search.setBounds(180, 180, 400, 150);
		user_search.setVisible(true);

		JScrollPane scrollableTextArea = new JScrollPane(user_search);
		scrollableTextArea.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollableTextArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollableTextArea.setMinimumSize(new Dimension(100, 50));
		scrollableTextArea.setBounds(180, 180, 400, 150);
		m.add(scrollableTextArea);
		m.repaint();

		// search text bar
		JTextField searchField = new JTextField();
		searchField.setBounds(120, 80, 300, 45); // x y width height
		m.add(searchField);
		m.repaint();

		JButton searchB = new JButton("Search");
		searchB.setBounds(470, 86, 130, 30);
		searchB.setBackground(new Color(51, 102, 0));
		searchB.setForeground(Color.white);
		m.add(searchB);
		m.repaint();

		searchB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				// search user
				ArrayList<person> searchResults = h.searchPeople(searchField.getText());

				if (searchResults.isEmpty()) {
					lister.clear();
					lister.addElement("No results found.");
				} else {
					lister.clear();
					for (int i = 0; i < searchResults.size(); i++) {
						person p = searchResults.get(i);
						// lister.addElement(p);
						String persView = p.getUserName() + ":" + p.getFirstName() + " " + p.getLastName();
						lister.addElement(persView);
					}
				}
			}
		});

		JButton deleteB = new JButton("Delete");
		deleteB.setBounds(285, 350, 100, 30);
		deleteB.setBackground(Color.red);
		deleteB.setForeground(Color.white);
		m.add(deleteB);
		m.repaint();

		deleteB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Steven to add delete functionality here.
				// If program selected, set isActive = FALSE
				String selected = user_search.getSelectedValue();
				String[] uName = selected.split(":");
				System.out.println(uName[0]);
				if (selected != null && selected != "") {
					// int classId = h.getIDFromUserInput(selected);
					ArrayList<person> selectedpers = h.searchPeople(uName[0]);
					try {
						h.deletePers(selectedpers.get(0));
					} catch (SQLException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					// lister2.addElement(selectedprog.getClassName() + " successfully deleted");
					// viewPrograms();
					
					JFrame popup = new JFrame();
					popup.setSize(350, 250);
					popup.setLayout(null);
					popup.setVisible(true);
					popup.getContentPane().setBackground(Color.white);
					popup.setTitle("User Deleted!");
					ImageIcon icon = new ImageIcon("ymcalogo.JPG");
					popup.setIconImage(icon.getImage());

					ImageIcon banner = new ImageIcon("smallsmiley.PNG");
					JLabel b = new JLabel(banner);
					b.setBounds(120, 20, 300, 200); // (x,y, width, height)
					popup.add(b);

					JLabel title = new JLabel("User Deleted!");
					title.setBounds(0, 0, 200, 90);
					title.setHorizontalAlignment(SwingConstants.CENTER);
					title.setFont(new Font("SansSerif", Font.BOLD, 15));
					title.setForeground(Color.black);
					popup.add(title, 0);
					popup.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					
				}
			}
		});

		JButton enrolledB = new JButton("Enrolled In");
		enrolledB.setBounds(400, 350, 100, 30);
		enrolledB.setBackground(Color.blue);
		enrolledB.setForeground(Color.white);
		m.add(enrolledB);
		m.repaint();

		DefaultListModel lister2 = new DefaultListModel();
		JList<String> searchResultslist = new JList<>(lister2);
		searchResultslist.setFixedCellHeight(15);
		searchResultslist.setFixedCellWidth(100);
		searchResultslist.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		searchResultslist.setVisibleRowCount(5);
		searchResultslist.setBounds(135, 400, 500, 120);
		searchResultslist.setVisible(true);
		m.add(searchResultslist);

		enrolledB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String selected = user_search.getSelectedValue();

				String[] sp = selected.split(":");
				// find classes from the username/phonenumber at sp[0] and display them
				String[] classes = h.getClassesFromUser(sp[0]);
				lister2.clear();

				if (classes.length == 0) {
					lister2.addElement("User not enrolled in any classes.");
				}

				for (int i = 0; i < classes.length; i++) {
					lister2.addElement(classes[i]);
				}
			}
		});

		JButton exitB = new JButton("Sign Out");
		exitB.setBounds(400, 690, 100, 30);
		exitB.setBackground(Color.gray);
		exitB.setForeground(Color.white);
		m.add(exitB);
		m.repaint();

		exitB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// close db connection in helper
				h.closeDBConnection();

				m.dispose();
			}
		});
		
		JButton helpB = new JButton("Help");
		helpB.setBounds(280, 690, 100, 30);
		helpB.setBackground(Color.gray);
		helpB.setForeground(Color.white);
		m.add(helpB);
		m.repaint();
		
		helpB.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) {
            	try {
            		File htmlFile = new File("staffFAQ.html");
            		Desktop.getDesktop().browse(htmlFile.toURI());
            	} catch (Exception ee) {}
            }
        });

	}

	/*
	shows which users are involved in which programs
	 * 
	 */
	private void viewEnrolled() {

		// search
		searchEnrolled();

		JLabel text = new JLabel("Search Users:");
		text.setBounds(0, 0, 780, 65);
		text.setHorizontalAlignment(SwingConstants.CENTER);
		text.setFont(new Font("SansSerif", Font.BOLD, 35));
		text.setForeground(new Color(128, 128, 0));
		m.add(text, 0);

	}

	/*
	 displays the register for a program portion
	 program format is classID, className, classDesc, classSize, startTime,
	 endTime, memFee, nonMemFee
	time is in the format YYYY-MM-DD HH:MM:SS:SSS
	 * 
	 */
	private void registerDisplay() {
		pressedAddB = true;

		JLabel text = new JLabel("Add a New Program:");
		text.setBounds(0, 0, 780, 65);
		text.setHorizontalAlignment(SwingConstants.CENTER);
		text.setFont(new Font("SansSerif", Font.BOLD, 35));
		text.setForeground(new Color(128, 128, 0));
		m.add(text, 0);

		addText("Class ID: ", 15, 165, 100, 50);
		JTextField classID = new JTextField(30);
		classID.setBounds(15, 200, 100, 27);
		m.add(classID);

		addText("Class Name: ", 125, 165, 100, 50);
		JTextField className = new JTextField(30);
		className.setBounds(125, 200, 100, 27);
		m.add(className);

		addText("Class Size: ", 235, 165, 100, 50);
		JTextField classSize = new JTextField(30);
		classSize.setBounds(235, 200, 100, 27);
		m.add(classSize);

		addText("Member Fee: ", 345, 165, 100, 50);
		JTextField memFee = new JTextField(30);
		memFee.setBounds(345, 200, 100, 27);
		m.add(memFee);

		JLabel nmtext = new JLabel("Non-Member Fee:");
		nmtext.setBounds(425, 165, 200, 50);
		nmtext.setHorizontalAlignment(SwingConstants.CENTER);
		nmtext.setFont(new Font("SansSerif", Font.PLAIN, 15));
		nmtext.setForeground(new Color(128, 128, 0));
		m.add(nmtext, 0);
		JTextField nonmemFee = new JTextField(30);
		nonmemFee.setBounds(455, 200, 100, 27);
		m.add(nonmemFee);

		addText("Start Date: ", 15, 275, 100, 50);
		JTextField startDate = new JTextField(30);
		startDate.setBounds(15, 310, 100, 27);
		startDate.setText("MM-DD-YYYY");
		m.add(startDate);

		addText("End Date: ", 125, 275, 100, 50);
		JTextField endDate = new JTextField(30);
		endDate.setBounds(125, 310, 100, 27);
		endDate.setText("MM-DD-YYYY");
		m.add(endDate);

		addText("Start Time: ", 235, 275, 100, 50);
		JTextField startTime = new JTextField(30);
		startTime.setBounds(235, 310, 100, 27);
		startTime.setText("HH:MM");
		m.add(startTime);

		addText("End Time: ", 345, 275, 100, 50);
		JTextField endTime = new JTextField(30);
		endTime.setBounds(345, 310, 100, 27);
		endTime.setText("HH:MM");
		m.add(endTime);

		addText("Days: ", 455, 275, 100, 50);
		JTextField days = new JTextField(30);
		days.setBounds(455, 310, 235, 27);
		days.setText("Ex. Mon Tues Wed Thurs Fri");
		m.add(days);

		addText("Location: ", 585, 165, 100, 50);
		JTextField loc = new JTextField(30);
		loc.setBounds(585, 200, 100, 27);
		m.add(loc);

		addText("Description: ", 15, 375, 100, 50);
		JTextField classDesc = new JTextField(30);
		classDesc.setBounds(15, 410, 500, 120);
		m.add(classDesc);

		JButton regB = new JButton("Add Program");
		regB.setBounds(150, 570, 200, 40);
		regB.setBackground(new Color(51, 102, 0));
		regB.setForeground(Color.white);
		m.add(regB);
		m.repaint();

		// find programs at the selected time
		regB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// classID, className, classDesc, classSize, startTime, endTime, memFee,
				// nonMemFee

				if (classID.getText().equals("") || classID.getText() == null) {
					classID.setText("Enter ID! ");
				} else {
					int result = h.addProgram(classID.getText(), className.getText(), classDesc.getText(),
							classSize.getText(), startTime.getText(), endTime.getText(), memFee.getText(),
							nonmemFee.getText(), startDate.getText(), endDate.getText(), days.getText(), loc.getText());

					try {
						String[] progs = h.getProgramsList();
						for (int i = 0; i < progs.length; i++) {
							System.out.println(progs[i]);
						}
					} catch (SQLException e1) {

					}

					if (result == 0) {
						classDesc.setText("Unable to add program. Please try again.");
					} else {

						JFrame popup = new JFrame();
						popup.setSize(350, 250);
						popup.setLayout(null);
						popup.setVisible(true);
						popup.getContentPane().setBackground(Color.white);
						popup.setTitle("Success!");
						ImageIcon icon = new ImageIcon("ymcalogo.JPG");
						popup.setIconImage(icon.getImage());

						ImageIcon banner = new ImageIcon("smallsmiley.PNG");
						JLabel b = new JLabel(banner);
						b.setBounds(120, 20, 300, 200); // (x,y, width, height)
						popup.add(b);

						JLabel title = new JLabel("Added " + className.getText() + "!");
						title.setBounds(0, 0, 200, 90);
						title.setHorizontalAlignment(SwingConstants.CENTER);
						title.setFont(new Font("SansSerif", Font.BOLD, 15));
						title.setForeground(Color.black);
						popup.add(title, 0);
						popup.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

					}
				}

			}
		});

		JButton exitB = new JButton("Sign Out");
		exitB.setBounds(300, 690, 200, 30);
		exitB.setBackground(Color.gray);
		exitB.setForeground(Color.white);
		m.add(exitB);
		m.repaint();

		exitB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// close db connection in helper
				h.closeDBConnection();

				m.dispose();
			}
		});
	}

	/*
	helper method to add text to the window
	 * 
	 */
	private void addText(String t, int x, int y, int w, int h) {
		JLabel text = new JLabel(t);
		text.setBounds(x, y, w, h);
		text.setHorizontalAlignment(SwingConstants.CENTER);
		text.setFont(new Font("SansSerif", Font.PLAIN, 15));
		text.setForeground(new Color(128, 128, 0));
		m.add(text, 0);
	}

	/*
	sets up page on initial sign in
	 * 
	 */
	private void start() {
		// set LookAndFeel
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			// If Nimbus is not available, you can set the GUI to another look and feel.
		}

		m = new JFrame();
		m.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		// Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		// m.setSize(screenSize.width, screenSize.height);
		m.setSize(800, 800);

		m.setLayout(null);
		m.setVisible(true);
		m.getContentPane().setBackground(new Color(245, 245, 220));
		m.setTitle("Staff Page | YMCA ");
		ImageIcon icon = new ImageIcon("ymcalogo.JPG");
		m.setIconImage(icon.getImage());
		m.repaint();
		
		JButton helpB = new JButton("Help");
		helpB.setBounds(280, 690, 200, 30);
		helpB.setBackground(Color.gray);
		helpB.setForeground(Color.white);
		m.add(helpB);
		m.repaint();
		
		helpB.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) {
            	try {
            		File htmlFile = new File("staffFAQ.html");
            		Desktop.getDesktop().browse(htmlFile.toURI());
            	} catch (Exception ee) {}
            }
        });

		JLabel title = new JLabel("Welcome to the YMCA Staff Member Page, " + u.getFirstName() + "!");
		title.setBounds(0, 200, 600, 50);
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setFont(new Font("SansSerif", Font.BOLD, 25));
		title.setForeground(new Color(128, 128, 0));
		m.add(title, 0);

		ImageIcon banner = new ImageIcon("colors.png");
		JLabel b = new JLabel(banner);
		b.setBounds(0, 250, 800, 300); // (x,y, width, height)
		m.add(b);
		m.repaint();

		JMenuBar mb = new JMenuBar();
		JMenu x = new JMenu("Programs");
		JMenuItem p1 = new JMenuItem("Add new Program");

		p1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				m.getContentPane().removeAll();
				m.repaint();
				pressedviewprogB = false;

				if (pressedAddB == false) {
					registerDisplay();
				}
			}
		});

		JMenuItem p2 = new JMenuItem("Search Programs");
		p2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				m.getContentPane().removeAll();
				m.repaint();
				pressedAddB = false;

				if (pressedviewprogB == false) {
					viewPrograms();
				}
			}
		});

		JMenuItem p3 = new JMenuItem("Search Members/Non-Members");
		p3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				m.getContentPane().removeAll();
				m.repaint();
				pressedAddB = false;
				pressedviewprogB = false;

				viewEnrolled();

			}
		});

		x.add(p1);
		x.add(p2);
		x.add(p3);
		mb.add(x);
		m.setJMenuBar(mb);

	}

}
