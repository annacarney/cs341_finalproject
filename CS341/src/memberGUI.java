import java.awt.Color;
import java.awt.*;
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

		JFrame m = new JFrame();
		m.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    m.setSize(screenSize.width, screenSize.height);
		
		m.setLayout(null);
		m.setVisible(true);
		m.getContentPane().setBackground(new Color(212,233,196));
		m.setTitle("Welcome Returning Member! | YMCA ");
		ImageIcon icon = new ImageIcon("ymcalogo.JPG");
		m.setIconImage(icon.getImage());
		m.repaint();
		
		displayPrograms(m);

	}

	//lets a member view/register for available programs 
	private void displayPrograms(JFrame m) {
		
		
	}

}
