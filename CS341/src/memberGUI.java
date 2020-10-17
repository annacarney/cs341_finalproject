import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

	// constructor
	public memberGUI(person p) {
		u = p;
		h = new helper();
		start();
	}

	// sets up page on intital sign in
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
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    f.setSize(screenSize.width, screenSize.height);
		
		f.setLayout(null);
		f.setVisible(true);
		f.getContentPane().setBackground(new Color(212,233,196));
		f.setTitle("Welcome Returning Member! | YMCA ");
		ImageIcon icon = new ImageIcon("ymcalogo.JPG");
		f.setIconImage(icon.getImage());
		f.repaint();
		
		displayPrograms(f);

	}

	//lets a member view/register for available programs 
	private void displayPrograms(JFrame f) {
		
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
		
		DefaultListModel lister = new DefaultListModel();
		int avail_progsSize = programs.length;
		for(int i = 0; i < programs.length; i++) {
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
		
		//get all available program times
		String[] progTimes = h.getProgramTimesFormatted();
		
		//need to search for programs at a time ** add here
		JComboBox<String> searchTimes = new JComboBox(progTimes);
		searchTimes.setBounds(100,100,380,30);
		f.add(searchTimes);
		
		JButton searchB = new JButton("Search Programs from Time");
		searchB.setBounds(500, 100, 200, 30);
		searchB.setBackground(new Color(127,0,255));
		searchB.setForeground(Color.white);
		f.add(searchB);
		f.repaint();
		
		//find programs at the selected time
		searchB.addActionListener(new ActionListener() { 
	            @Override
	            public void actionPerformed(ActionEvent e) {
	            	
	            	String selected = (String)searchTimes.getSelectedItem();
	            	String [] p = h.getProgramsFromTime(selected);
	            	
	            	//delete current programs from JList
	            	lister.clear();
	            	
	            	//add selected programs
	            	for(int i = 0; i < p.length; i++) {
	            		lister.add(i, p[i]);
	            	}
	            
	            }
	        });
	
		
		//lists the programs selected by the user
	    jListSelect.setFixedCellHeight(15);
	    jListSelect.setFixedCellWidth(100);
	    jListSelect.setVisibleRowCount(5);
	    jListSelect.setBounds(500,200,200,200);
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
		 selectButton.setBounds(370,300,90,40);
		 selectButton.setBackground(new Color(51,102,0));
		 selectButton.setForeground(Color.white);
		 f.add(selectButton);
		 
		 //button to register for selected programs
		 JButton registerButton = new JButton("Show Program Details");
		 registerButton.setBounds(500,410,200,40);
		 registerButton.setForeground(Color.white);
		 registerButton.setBackground(new Color(51,102,0));
		 
		 JLabel title1 = new JLabel();

		 registerButton.addActionListener(new ActionListener() {
			 @Override
	            public void actionPerformed(ActionEvent e) {
	              h.registerM(jListSelect.getModel(), f, u);
			 	}
		 });
		 
		 f.add(registerButton);
		 f.repaint();	
	}

}
