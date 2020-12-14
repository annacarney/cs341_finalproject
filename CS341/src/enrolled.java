import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/*
 * enrolled.java
 * Class for the enrolled table in the database 
 * Created: 10/10/2020
 */

public class enrolled {
	
	private String username;
	private int classid;
	
	/*
	 * constructor
	 */
	public enrolled(String username, int classid) {
		super();
		this.username = username;
		this.classid = classid;
	}
	
	/*
	 * query helper
	 */
	public static ArrayList<enrolled> find(database db, String where) {
		ResultSet pResults;
		try {
			pResults = db.runQuery("SELECT username, classID FROM Enrolled " + (where != null ? "WHERE " + where : ""));
			ArrayList<enrolled> regs = new ArrayList<>();
			
			while(pResults.next()) {
				String username = pResults.getString("username");
				int classid = pResults.getInt("classID");
				
				enrolled e = new enrolled(username, classid);
				regs.add(e);
			}
			
			return regs;
		} catch (SQLException e) {
			System.out.println("DB Query failed");
			return new ArrayList<>();
		}
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getClassid() {
		return classid;
	}

	public void setClassid(int classid) {
		this.classid = classid;
	}

	@Override
	public String toString() {
		return "enrolled [username=" + username + ", classid=" + classid + "]";
	}	
	
}