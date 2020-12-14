import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;
import java.sql.SQLException;

import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;

/* CS 341 - Final Project
 * Team 1 - Shyue Shi Leong, Ze Jia Lim, Steven Welter, and Anna Carney\
 * This class sets up the graphical user interface for the member user.
 */
public class memberGUI {

	// Functions of a member : view program, register for a program,

	private person u;
	private helper h;

	/*
	 constructor
	 * 
	 */
	public memberGUI(person p) {
		u = p;
		h = new helper();
		start();
	}

	/*
	 sets up page on intital sign in
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

		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		//Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    //f.setSize(screenSize.width, screenSize.height);
		f.setSize(800,800);
		
		f.setLayout(null);
		f.setVisible(true);
		f.getContentPane().setBackground(new Color(221,229,193));
		f.setTitle("Member Page | YMCA ");
		ImageIcon icon = new ImageIcon("ymcalogo.JPG");
		f.setIconImage(icon.getImage());
		f.repaint();
		
		ImageIcon banner = new ImageIcon("colors.png");
		JLabel b = new JLabel(banner);
		b.setBounds(0, 700, 800, 100); //(x,y, width, height)
		f.add(b);
		f.repaint();
		
		JButton exitB = new JButton("Sign Out");
		exitB.setBounds(300, 650, 200, 30);
		exitB.setBackground(Color.gray);
		exitB.setForeground(Color.white);
		f.add(exitB);
		f.repaint();
		
		exitB.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) {
            	//close db connection in helper
            	h.closeDBConnection();
 	
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
            	} catch (Exception ee) {}
            }
        });
		
		displayPrograms(f);

	}

	/*
	lets a member view/register for available programs 
	 * 
	 */
	private void displayPrograms(JFrame f) {
		
		JLabel title = new JLabel("Welcome back, " + u.getFirstName() + "!");
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
			if (programs[i].contains("(Inactive)")) {
				continue;
			}
			lister.addElement(programs[i]);
		}
		JList<String> avail_progs = new JList<>(lister);
		
		//lists the available programs
		JList<String> jListSelect = new JList<>();
		//JList<String> avail_progs = new JList<>(programs);
		avail_progs.setFixedCellHeight(15);
		avail_progs.setFixedCellWidth(100);
		avail_progs.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		avail_progs.setVisibleRowCount(5);
		avail_progs.setBounds(100, 200, 200, 200);
		avail_progs.setVisible(true);
		
        JScrollPane scrollableTextArea = new JScrollPane(avail_progs);  
        scrollableTextArea.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);  
        scrollableTextArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);  
        scrollableTextArea.setMinimumSize(new Dimension(100,50));
        scrollableTextArea.setBounds(100,200,220,200);
        f.add(scrollableTextArea);
		f.repaint();
		 
		 //button to register for selected programs
		 JButton registerButton = new JButton("Show Program Details");
		 registerButton.setBounds(110,410,200,40);
		 registerButton.setForeground(Color.white);
		 registerButton.setBackground(new Color(51,102,0));
		 
		 JLabel title1 = new JLabel();

		 registerButton.addActionListener(new ActionListener() {
			 public void actionPerformed(ActionEvent e) {
				 String progString = avail_progs.getSelectedValue();
				 int classId = h.getIDFromUserInput(progString);
			
				 String[] classinfo = null;
					try {
						classinfo = h.program_details(classId, true);
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
		 
		 JButton reg = new JButton("Register!");
		 reg.setBounds(457, 410, 200, 40);
		 reg.setBackground(new Color(22,65,45));
	     reg.setForeground(Color.white);
	     f.add(reg);
		 f.repaint();
		 
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
					
					h.registerM(avail_progs.getSelectedValue(), classId, f, u);
				}
			}));
	
}
	
	
	
	
	
	
	
	
	
	
}
