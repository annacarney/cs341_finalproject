import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

/* CS 341 - Final Project
 * Team 1 - Shyue Shi Leong, Ze Jia Lim, Steven Welter, and Anna Carney\
 * This class sets up the graphical user interface for the staff user.
 */
public class staffGUI {

	private person u;
	private helper h;

	// constructor
	public staffGUI(person p) {
		u = p;
		h = new helper();
		start();
	}

	// sets up page on initial sign in
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
		m.getContentPane().setBackground(new Color(245,245,220));
		m.setTitle("Staff Page | YMCA ");
		ImageIcon icon = new ImageIcon("ymcalogo.JPG");
		m.setIconImage(icon.getImage());
		m.repaint();
		
		JLabel title = new JLabel("Welcome to the YMCA Staff Member Page, " + u.getFirstName() + "!");
		title.setBounds(0, 0, 900, 50);
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setFont(new Font("SansSerif", Font.BOLD, 35));
		title.setForeground(new Color(128,128,0));
		m.add(title, 0);

	}

}
