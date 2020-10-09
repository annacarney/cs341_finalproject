/* CS 341 - Final Project
 * Team 1 - Shyue Shi Leong, Ze Jia Lim, Steven Welter, and Anna Carney\
 * This class sets up the graphical user interface.
 */
import java.awt.Color;
import java.awt.Component;
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
import javax.swing.*;	//using java swing for gui assistance
import javax.swing.UIManager.*;

public class gui {
	
	//constructor for gui class
	public gui() {
		start();
	}

	//creates the start/welcome window for the YMCA program
	public void start() {
		
		//set LookAndFeel
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {		//Windows, Nimbus, 
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (Exception e) {
		    // If Nimbus is not available, you can set the GUI to another look and feel.
		}
		
		JFrame f = new JFrame();
		// Terminate the program when user clicks the red close button.
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent){
				System.exit(0);
				}
			});
		
		Color purple = new Color(202,185,241);
		Color pale_grey = new Color(237,237,237);
		
		ImageIcon banner = new ImageIcon("banner.jpg");
		JLabel b = new JLabel(banner);
		b.setBounds(0, 50, 900, 160); //(x,y, width, height)
		f.add(b);
		
		//Text area
		JLabel title = new JLabel("Welcome to the YMCA!");
		title.setBounds(0, 0, 780, 65);
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setFont(new Font("Courier", Font.BOLD, 35));
		title.setForeground(purple);
		f.add(title, 0);
		
		//Text area
		JLabel t2 = new JLabel("New? Click below to create an account!");
		t2.setBounds(150, 180, 500, 120); //(x,y, width, height)
		t2.setHorizontalAlignment(SwingConstants.CENTER);
		t2.setFont(new Font("Courier", Font.BOLD, 20));
		t2.setForeground(purple);
		f.add(t2, 0);
		
		//button to create an account 
		JButton createAcct = new JButton("Create an Account");
		createAcct.setBounds(340,260,150,30);
		createAcct.setBackground(purple);
		createAcct.setForeground(Color.DARK_GRAY);
		createAcct.addActionListener (new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				System.out.println("Creaete an Account button clicked");
				// AddAcctGUI g = new AddAcctGUI();
				}
			});
		f.add(createAcct);
		
		//Text area
		JLabel t3 = new JLabel("Existing members and staff members sign in here!");
		t3.setBounds(99, 295, 600, 150); //(x,y, width, height)
		t3.setHorizontalAlignment(SwingConstants.CENTER);
		t3.setFont(new Font("Courier", Font.BOLD, 20));
		t3.setForeground(purple);
		f.add(t3, 0);
		
		JLabel un = new JLabel("Username: ");
		un.setBounds(301, 330, 100, 150); //(x,y, width, height)
		un.setHorizontalAlignment(SwingConstants.CENTER);
		un.setFont(new Font("Courier", Font.BOLD, 15));
		un.setForeground(purple);
		f.add(un, 0);
		
		JLabel pw = new JLabel("Password: ");
		pw.setBounds(301, 360, 100, 150); //(x,y, width, height)
		pw.setHorizontalAlignment(SwingConstants.CENTER);
		pw.setFont(new Font("Courier", Font.BOLD, 15));
		pw.setForeground(purple);
		f.add(pw, 0);
		
		//text entry for username and pass (member and staff member login only)
		JTextField username = new JTextField();
		username.setBounds(400,400,150,20);
		f.add(username);
		JTextField password = new JTextField();
		password.setBounds(400,430,150,20);
		f.add(password);

		//submit button to enter username and password 
		JButton signIn = new JButton("Sign In");
		signIn.setBounds(340,460,150,30);
		signIn.setBackground(purple);
		signIn.setForeground(Color.DARK_GRAY);
		signIn.addActionListener (new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				System.out.println("Sign In button clicked");
				// SignIn s = new SignInGUI();
				}
			});
		f.add(signIn);
		
		//Text area
		JLabel t4 = new JLabel("Non-members, click below to view programs!");
		t4.setBounds(99, 509, 600, 150); //(x,y, width, height)
		t4.setHorizontalAlignment(SwingConstants.CENTER);
		t4.setFont(new Font("Courier", Font.BOLD, 20));
		t4.setForeground(purple);
		f.add(t4, 0);
		
		//for non-member "login"
		//directs the non-member to a page that lists the current available programs
		JButton viewProg = new JButton("Available Programs");
		viewProg.setBounds(340,600,150,30);
		viewProg.setBackground(purple);
		viewProg.setForeground(Color.DARK_GRAY);
		viewProg.addActionListener (new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				System.out.println("Available Programs button clicked");
				// viewProg s = new viewProg();
				}
			});
		f.add(viewProg);
		
		//styling for the window
		f.setSize(800,800);  	//800 width, 800 height
		f.setLayout(null);
		f.setVisible(true);
		f.getContentPane().setBackground(pale_grey);
		f.setTitle("YMCA | Sign In ");
		ImageIcon icon = new ImageIcon("ymcalogo.png");
		f.setIconImage(icon.getImage());
		
		f.repaint();
	}
	    


}
