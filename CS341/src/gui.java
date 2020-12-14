/* CS 341 - Final Project
 * Authors: Shyue Shi Leong, Ze Jia Lim, Steven Welter, and Anna Carney 
 * This class sets up the graphical user interface's main page.
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.URI;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Vector;

import javax.swing.*;	
import javax.swing.UIManager.*;

public class gui {
	
	private helper h;
	
	/*
	 * constructor for gui class
	 */
	public gui() {
		start();
		h = new helper();
	}
	
	/*
	 * signs a user in with login credentials (username, password)
	 * returns 1 on succesful login, 0 otherwise
	 */
	public int signIn(String username, String password) {
		
		System.out.println(username);
		System.out.println(password);
		
		//sign in the user
		person p = h.signInUser(username, password);
		
		if(p == null) {
			//wrong sign in credentials
			System.out.print("Sign in failed");
			return 0; 
		}
		
		//create the sign in page based on if user is 
		if(p.getIsStaff() == true) {
			//user is a staff member
			System.out.print("Sign in worked = is a staff member");
			staffGUI sg = new staffGUI(p);
			
		} else if(p.getIsAdmin() == true ){
			//user is an admin
			System.out.print("Sign in worked = is an admin");
			staffGUI sg = new staffGUI(p);
			
		} else {
			//user is a member
			System.out.print("Sign in worked = is a member");
			memberGUI mg = new memberGUI(p);
		}
		
		return 1;
	}
	
	/*
	 * creates an account for a user (staff-member/member/non-member)
	 */
	public void createAccount() {
		
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		//styling for the window
		f.setSize(800,800);  	
		f.setLayout(null);
		f.setVisible(true);
		f.getContentPane().setBackground(new Color(204,229,255));
		f.setTitle("YMCA | Create an Account ");
		ImageIcon icon = new ImageIcon("ymcalogo.JPG");
		f.setIconImage(icon.getImage());
		
		JLabel title = new JLabel("Create an Account");
		title.setBounds(0, 0, 780, 65);
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setFont(new Font("SansSerif", Font.BOLD, 35));
		title.setForeground(new Color(0,76,153));
		f.add(title, 0);	
		
		ImageIcon banner = new ImageIcon("colors.png");
		JLabel b = new JLabel(banner);
		b.setBounds(0, 700, 800, 100); //(x,y, width, height)
		f.add(b);
		
		JLabel usNametext = new JLabel("Username:");
		usNametext.setBounds(75, 165, 100, 50);
		usNametext.setHorizontalAlignment(SwingConstants.CENTER);
		usNametext.setFont(new Font("SansSerif", Font.PLAIN, 15));
		usNametext.setForeground(new Color(128,128,0));
		f.add(usNametext, 0);
		
		JTextField usName = new JTextField(30);
		usName.setBounds(80,200,100,27);
		f.add(usName);
		
		JLabel pwNametext = new JLabel("Password:");
		pwNametext.setBounds(175, 165, 100, 50);
		pwNametext.setHorizontalAlignment(SwingConstants.CENTER);
		pwNametext.setFont(new Font("SansSerif", Font.PLAIN, 15));
		pwNametext.setForeground(new Color(128,128,0));
		f.add(pwNametext, 0);
		
		JTextField pwName = new JTextField(30);
		pwName.setBounds(180,200,100,27);
		f.add(pwName);
		
		JLabel fNametext = new JLabel("First Name:");
		fNametext.setBounds(275, 165, 100, 50);
		fNametext.setHorizontalAlignment(SwingConstants.CENTER);
		fNametext.setFont(new Font("SansSerif", Font.PLAIN, 15));
		fNametext.setForeground(new Color(128,128,0));
		f.add(fNametext, 0);
		
		JTextField fName = new JTextField(30);
		fName.setBounds(280,200,100,27);
		f.add(fName);
		
		JLabel lNametext = new JLabel("Last Name:");
		lNametext.setBounds(375, 165, 100, 50);
		lNametext.setHorizontalAlignment(SwingConstants.CENTER);
		lNametext.setFont(new Font("SansSerif", Font.PLAIN, 15));
		lNametext.setForeground(new Color(128,128,0));
		f.add(lNametext, 0);
		
		JTextField lName = new JTextField(30);
		lName.setBounds(380,200,100,27);
		f.add(lName);
		
		JLabel phonetext = new JLabel("Phone Number:");
		phonetext.setBounds(460, 165, 150, 50);
		phonetext.setHorizontalAlignment(SwingConstants.CENTER);
		phonetext.setFont(new Font("SansSerif", Font.PLAIN, 15));
		phonetext.setForeground(new Color(128,128,0));
		f.add(phonetext, 0);
		
		JTextField phone = new JTextField(30);
		phone.setBounds(480,200,100,27);
		f.add(phone);
		
		JLabel usertypetext = new JLabel("Staff Member or Member? (enter SM or M)");
		usertypetext.setBounds(175, 265, 300, 50);
		usertypetext.setHorizontalAlignment(SwingConstants.CENTER);
		usertypetext.setFont(new Font("SansSerif", Font.PLAIN, 15));
		usertypetext.setForeground(new Color(128,128,0));
		f.add(usertypetext, 0);
		
		JTextField usertype = new JTextField(30);
		usertype.setBounds(180,300,300,27);
		f.add(usertype);
		
		JButton addB = new JButton("Create Account!");
		addB.setBounds(250, 400, 200, 30);
		addB.setBackground(new Color(51,102,0));
		addB.setForeground(Color.white);
		f.add(addB);
		f.repaint();
		
		addB.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) {
            	boolean isStaff = false; 
            	if(usertype.getText().equals("SM")) {
            		isStaff = true;
            	}
            	h.createUser(fName.getText(), lName.getText(), phone.getText(), usName.getText(), pwName.getText(), isStaff, false);

            }
        });
		
		
	}
	
	/*
	 * creates the non-member window
	 */
	public void viewPrograms() {
		
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		//styling for the window
		f.setSize(800,800);  	
		f.setLayout(null);
		f.setVisible(true);
		f.getContentPane().setBackground(new Color(221,229,193)); 
		f.setTitle("YMCA | View Available Programs ");
		ImageIcon icon = new ImageIcon("ymcalogo.JPG");
		f.setIconImage(icon.getImage());
		
		ImageIcon banner = new ImageIcon("colors.png");
		JLabel b = new JLabel(banner);
		b.setBounds(0, 700, 800, 100); //(x,y, width, height)
		f.add(b);
		
		JLabel title = new JLabel("Available Programs");
		title.setBounds(0, 0, 780, 65);
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setFont(new Font("SansSerif", Font.BOLD, 35));
		title.setForeground(new Color(51,102,0));
		f.add(title, 0);
		
		String[] programs = null;
		try {
			programs = h.getProgramsList();
		} catch (SQLException e1) {
			System.out.print("Db query failed.");
		}
		
		DefaultListModel lister = new DefaultListModel();
		int avail_progsSize = programs.length;
		for(int i = 0; i < programs.length; i++) {
			//Check if program is active, if not, skip for this list.
			if (programs[i].contains("(Inactive)")) {
				continue;
			}
			lister.addElement(programs[i]);
		}
		JList<String> avail_progs = new JList<>(lister);
		
		//lists the available programs
		JList<String> jListSelect = new JList<>();
		avail_progs.setFixedCellHeight(15);
		avail_progs.setFixedCellWidth(100);
		avail_progs.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		avail_progs.setVisibleRowCount(5);
		avail_progs.setBounds(100,200,220,200);
		avail_progs.setVisible(true);
		
        JScrollPane scrollableTextArea = new JScrollPane(avail_progs);  
        scrollableTextArea.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);  
        scrollableTextArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);  
        scrollableTextArea.setMinimumSize(new Dimension(100,50));
        scrollableTextArea.setBounds(100,200,220,200);
        f.add(scrollableTextArea);
		f.repaint();
		
		JButton exitB = new JButton("Exit");
		exitB.setBounds(680, 10, 100, 30);
		exitB.setBackground(new Color(255,178,102));
		exitB.setForeground(Color.white);
		f.add(exitB);
		f.repaint();
		
		exitB.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) {
            	//close db connection in helper
            	//h.closeDBConnection();
 	
            	f.dispose();
            }
        });
		
		//help button
		JButton helpB = new JButton("We are here to help!");
		helpB.setBounds(300, 90, 200, 30);
		helpB.setBackground(Color.gray);
		helpB.setForeground(Color.white);
		f.add(helpB);
		f.repaint();
		
		helpB.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) {
            	try {
            		
            		File htmlFile = new File("generalFAQ.html");
            		Desktop.getDesktop().browse(htmlFile.toURI());
            		
            	  //  Desktop.getDesktop().browse(new URL("https://github.com/annacarney/cs341_finalproject/blob/master/CS341/generalFAQ.pdf").toURI());
            	} catch (Exception ee) {}
            }
        });
		 
		 //button to register for selected programs
		 JButton registerButton = new JButton("Show Program Details");
		 registerButton.setBounds(110,410,200,40);
		 registerButton.setForeground(Color.white);
		 registerButton.setBackground(new Color(51,102,0));
		 
		 JLabel title1 = new JLabel();

		 registerButton.addActionListener(new ActionListener() {
			 @Override
	            public void actionPerformed(ActionEvent e) {
				 
				 String progString = avail_progs.getSelectedValue();
				 int classId = h.getIDFromUserInput(progString);
				 //String className = 
						 
				 String[] classinfo = null;
					try {
						classinfo = h.program_details(classId, true);
						//classId = classinfo[10];			//keeps track of the classID * 
					} catch (SQLException eee) {
						// TODO Auto-generated catch block
						eee.printStackTrace();
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
			 	}
		 });
		 
		 f.add(registerButton);
		 f.repaint();
		 

		title = new JLabel("Want to Register?");
		title.setBounds(460, 410, 200, 40);
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setFont(new Font("SansSerif", Font.PLAIN, 25));
		title.setForeground(new Color(51,102,0));
		f.add(title, 0);
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
				
				String progString = avail_progs.getSelectedValue();
				 int classId = h.getIDFromUserInput(progString);
				 String className = h.getNameFromUserInput(progString);
				 
				String[] classinfo = null;
					try {
						classinfo = h.program_details(classId, true);
					} catch (SQLException eee) {
						// TODO Auto-generated catch block
						eee.printStackTrace();
						return;
					}
				
				System.out.println("Class ID " + classId + " " + name.getText() + phone.getText() + "Registering for " + className);
				
				//register NM for class
		
				if(phone.getText().equals("")) {
					phone.setText("Enter phone number.");
				} else if(name.getText().equals("")) {
					name.setText("Enter Name.");
				} else {
					int s = h.enrollNM(name.getText(), lname.getText(), phone.getText(), className, classId);
				}
				
			}
		}));
		 
	}

	/*
	 * creates the start/welcome window for the YMCA program
	 */
	public void start() {
		
		//set LookAndFeel
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
		
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		Color purple = new Color(87,65,47);	//brown
		Color background_color = new Color(249, 251, 220); //yellow
		Color button_color = new Color(255,178,102);
		
		ImageIcon banner = new ImageIcon("banner.jpg");
		JLabel b = new JLabel(banner);
		b.setBounds(0, 50, 900, 160); //(x,y, width, height)
		f.add(b);
		
		//Text area
		JLabel title = new JLabel("Welcome to the YMCA");
		title.setBounds(0, 0, 780, 65);
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setFont(new Font("SansSerif", Font.BOLD, 35));
		title.setForeground(purple);
		f.add(title, 0);
		
		//Text area
		JLabel t2 = new JLabel("New? Click below to create an account!");
		t2.setBounds(150, 180, 500, 120); //(x,y, width, height)
		t2.setHorizontalAlignment(SwingConstants.CENTER);
		t2.setFont(new Font("SansSerif", Font.PLAIN, 20));
		t2.setForeground(purple);
		f.add(t2, 0);
		
		//button to create an account 
		JButton createAcct = new JButton("Create an Account");
		createAcct.setBounds(340,260,150,30);
		createAcct.setBackground(button_color);
		createAcct.setForeground(Color.DARK_GRAY);
		
		//waits for the user to click the create account button
		createAcct.addActionListener (new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				createAccount();
				}
			});
		
		f.add(createAcct);
		
		//Text area Space
		JLabel t3 = new JLabel("Existing members and staff members sign in here!");
		t3.setBounds(99, 295, 600, 150); //(x,y, width, height)
		t3.setHorizontalAlignment(SwingConstants.CENTER);
		t3.setFont(new Font("SansSerif", Font.PLAIN, 20));
		t3.setForeground(purple);
		f.add(t3, 0);
		
		JLabel un = new JLabel("Username: ");
		un.setBounds(301, 330, 100, 150); //(x,y, width, height)
		un.setHorizontalAlignment(SwingConstants.CENTER);
		un.setFont(new Font("SansSerif", Font.PLAIN, 15));
		un.setForeground(purple);
		f.add(un, 0);
		
		JLabel pw = new JLabel("Password: ");
		pw.setBounds(301, 360, 100, 150); //(x,y, width, height)
		pw.setHorizontalAlignment(SwingConstants.CENTER);
		pw.setFont(new Font("SansSerif", Font.PLAIN, 15));
		pw.setForeground(purple);
		f.add(pw, 0);
		
		//text entry for username and pass (member and staff member login only)
		JTextField username = new JTextField(30);
		username.setBounds(400,396,200,27);
		f.add(username);

		JPasswordField password = new JPasswordField(30);
		password.setBounds(400,426,200,27);
		f.add(password);

		//submit button to enter username and password 
		JButton signIn = new JButton("Sign In");
		signIn.setBounds(340,460,150,30);
		signIn.setBackground(button_color);
		signIn.setForeground(Color.DARK_GRAY);
		
		// when button is clicked
		signIn.addActionListener((new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				
				int ret = signIn(username.getText(), password.getText());		//getPassword() return char[] **
				if(ret == 0) {	//login failed - credentials not recognized
					username.setText("Invalid username/password" );
					password.setText("");
				}else {
					//close window
					
					//f.dispatchEvent(new WindowEvent(f, WindowEvent.WINDOW_CLOSING));		//going to leave this open for now -- would be nice to have a back button or something
				}
				
				}
			} ));
		
		f.add(signIn);
		
		//Text area
		JLabel t4 = new JLabel("Non-members, click below to view programs!");
		t4.setBounds(99, 509, 600, 150); //(x,y, width, height)
		t4.setHorizontalAlignment(SwingConstants.CENTER);
		t4.setFont(new Font("SansSerif", Font.PLAIN, 20));
		t4.setForeground(purple);
		f.add(t4, 0);
		
		//for non-member "login"
		//directs the non-member to a page that lists the current available programs
		JButton viewProg = new JButton("Available Programs");
		viewProg.setBounds(340,600,150,30);
		viewProg.setBackground(button_color);
		viewProg.setForeground(Color.DARK_GRAY);
		
		//waits for click
		viewProg.addActionListener (new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				viewPrograms();
				
				}
			});
		
		f.add(viewProg);
		
		//help button
		JButton helpB = new JButton("We are here to help!");
		helpB.setBounds(315, 710, 200, 30);
		helpB.setBackground(Color.gray);
		helpB.setForeground(Color.white);
		f.add(helpB);
		f.repaint();
		
		helpB.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) {
            	try {
            		File htmlFile = new File("generalFAQ.html");
            		Desktop.getDesktop().browse(htmlFile.toURI());
            	} catch (Exception ee) {}
            }
        });
		
		//styling for the window
		f.setSize(800,800);  	//800 width, 800 height
		f.setLayout(null);
		f.setVisible(true);
		f.getContentPane().setBackground(Color.white);		//background_color);
		f.setTitle("YMCA | Welcome ");
		ImageIcon icon = new ImageIcon("ymcalogo.JPG");
		f.setIconImage(icon.getImage());
		
		f.repaint();
	}
	    


}
