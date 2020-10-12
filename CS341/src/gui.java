/* CS 341 - Final Project
 * Team 1 - Shyue Shi Leong, Ze Jia Lim, Steven Welter, and Anna Carney\
 * This class sets up the graphical user interface.
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Label;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.*;	
import javax.swing.UIManager.*;

public class gui {
	
	private helper h;
	
	//constructor for gui class
	public gui() {
		start();
		h = new helper();
	}
	
	//signs a user in with login credentials (username, password)
	public void signIn(String username, String password) {
		
		System.out.println(username);
		System.out.println(password);
		
		//implement me
		
	}
	
	
	// creates an account for a user (staff-member/member/non-member)
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
		
	}
	
	public void viewPrograms() {
		
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		//styling for the window
		f.setSize(800,800);  	
		f.setLayout(null);
		f.setVisible(true);
		f.getContentPane().setBackground(new Color(229,255,204));
		f.setTitle("YMCA | View Available Programs ");
		ImageIcon icon = new ImageIcon("ymcalogo.JPG");
		f.setIconImage(icon.getImage());
		
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
			// TODO Auto-generated catch block
			//e1.printStackTrace();
			System.out.print("Db query failed.");
		}
		
		//lists the available programs
		JList<String> jListSelect = new JList<>();
		JList<String> avail_progs = new JList<>(programs);
		avail_progs.setFixedCellHeight(15);
		avail_progs.setFixedCellWidth(100);
		avail_progs.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		avail_progs.setVisibleRowCount(5);
		avail_progs.setBounds(100, 100, 200, 200);
		avail_progs.setVisible(true);
		
        JScrollPane scrollableTextArea = new JScrollPane(avail_progs);  
        scrollableTextArea.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);  
        scrollableTextArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);  
        scrollableTextArea.setMinimumSize(new Dimension(100,50));
        scrollableTextArea.setBounds(100,100,220,200);
        f.add(scrollableTextArea);
        //f.add(avail_progs,0);
		f.repaint();
		
		//lists the programs selected by the user
	    jListSelect.setFixedCellHeight(15);
	    jListSelect.setFixedCellWidth(100);
	    jListSelect.setVisibleRowCount(5);
	    jListSelect.setBounds(500,100,200,200);
	    jListSelect.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		jListSelect.setVisible(true);
	    f.add(jListSelect,0);
		
	    //button for the user to select programs to register for 
		JButton selectButton = new JButton("Select>>>");
		 selectButton.addActionListener(new ActionListener() {
	            
	            @Override
	            public void actionPerformed(ActionEvent e) {
	            	//FIX ME LATER --- ONLY SELECTS ONE ITEM!!!!!!!!!!!!!!!!!
	                jListSelect.setListData(avail_progs.getSelectedValuesList().toArray(new String[0]));
	            }
	        });
		 selectButton.setBounds(370,150,90,40);
		 selectButton.setBackground(new Color(51,102,0));
		 selectButton.setForeground(Color.white);
		 f.add(selectButton);
		 
		 //button to register for selected programs
		 JButton registerButton = new JButton("Show Program Details");
		 registerButton.setBounds(500,310,200,40);
		 registerButton.setForeground(Color.white);
		 registerButton.setBackground(new Color(51,102,0));

		 registerButton.addActionListener(new ActionListener() {
			 @Override
	            public void actionPerformed(ActionEvent e) {
	              h.registerNM(jListSelect.getModel(), f);
			 	}
		 });
		 
		 f.add(registerButton);
		 f.repaint();
		
	}

	//creates the start/welcome window for the YMCA program
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
		JLabel title = new JLabel("Welcome to the YMCA!");
		title.setBounds(0, 0, 780, 65);
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setFont(new Font("SansSerif", Font.BOLD, 35));
		title.setForeground(purple);
		f.add(title, 0);
		
		//Text area
		JLabel t2 = new JLabel("New? Click below to create an account!");
		t2.setBounds(150, 180, 500, 120); //(x,y, width, height)
		t2.setHorizontalAlignment(SwingConstants.CENTER);
		t2.setFont(new Font("SansSerif", Font.BOLD, 20));
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
				System.out.println("Create an Account button clicked");
				// AddAcctGUI g = new AddAcctGUI();
				}
			});
		
		f.add(createAcct);
		
		//Text area Space
		JLabel t3 = new JLabel("Existing members and staff members sign in here!");
		t3.setBounds(99, 295, 600, 150); //(x,y, width, height)
		t3.setHorizontalAlignment(SwingConstants.CENTER);
		t3.setFont(new Font("SansSerif", Font.BOLD, 20));
		t3.setForeground(purple);
		f.add(t3, 0);
		
		JLabel un = new JLabel("Username: ");
		un.setBounds(301, 330, 100, 150); //(x,y, width, height)
		un.setHorizontalAlignment(SwingConstants.CENTER);
		un.setFont(new Font("SansSerif", Font.BOLD, 15));
		un.setForeground(purple);
		f.add(un, 0);
		
		JLabel pw = new JLabel("Password: ");
		pw.setBounds(301, 360, 100, 150); //(x,y, width, height)
		pw.setHorizontalAlignment(SwingConstants.CENTER);
		pw.setFont(new Font("SansSerif", Font.BOLD, 15));
		pw.setForeground(purple);
		f.add(pw, 0);
		
		//text entry for username and pass (member and staff member login only)
		JTextField username = new JTextField(30);
		username.setBounds(400,396,150,27);
		f.add(username);

		JPasswordField password = new JPasswordField(30);
		password.setBounds(400,426,150,27);
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
				System.out.println("Sign In button clicked");
				signIn(username.getText(), password.getText());
				}
			} ));
		
		f.add(signIn);
		
		//Text area
		JLabel t4 = new JLabel("Non-members, click below to view programs!");
		t4.setBounds(99, 509, 600, 150); //(x,y, width, height)
		t4.setHorizontalAlignment(SwingConstants.CENTER);
		t4.setFont(new Font("SansSerif", Font.BOLD, 20));
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
				System.out.println("Available Programs button clicked");
				// viewProg s = new viewProg();
				}
			});
		
		f.add(viewProg);
		
		//styling for the window
		f.setSize(800,800);  	//800 width, 800 height
		f.setLayout(null);
		f.setVisible(true);
		f.getContentPane().setBackground(background_color);
		f.setTitle("YMCA | Welcome ");
		ImageIcon icon = new ImageIcon("ymcalogo.JPG");
		f.setIconImage(icon.getImage());
		
		f.repaint();
	}
	    


}
