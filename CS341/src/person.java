import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/*
 * person.java
 * Person object class
 * Created: 10/10/2020
 */
public class person {
	
	private String firstName;
	private String lastName;
	private String phoneNumber;
	private String userName;
	private String password;
	private Boolean isStaff;
	private Boolean isAdmin;
	private Boolean isActive;
	
	public person(String firstName, String lastName, String phoneNumber, String userName, String password, Boolean isStaff, Boolean isAdmin, Boolean isActive) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.userName = userName;
		this.password = password;
		this.isStaff = isStaff;
		this.isAdmin = isAdmin;
		this.isActive = isActive;
	}
	
	/*
	query helper
	 * 
	 */
		public static ArrayList<person> find(database db, String where) {
			ResultSet pResults;
			try {
				pResults = db.runQuery("SELECT firstName, lastName, phoneNumber, userName, password, isStaff, isAdmin, isActive FROM Person " + (where != null ? "WHERE " + where : ""));
				ArrayList<person> persons = new ArrayList<>();
				
				while(pResults.next()) {
					String firstName = pResults.getString("firstName");
					String lastName = pResults.getString("lastName");
					String phoneNumber = pResults.getString("phoneNumber");
					String userName = pResults.getString("userName");
					String password = pResults.getString("password");
					Boolean isStaff = pResults.getBoolean("isStaff");
					Boolean isAdmin = pResults.getBoolean("isAdmin");
					Boolean isActive = pResults.getBoolean("isActive");
					
					person p = new person(firstName, lastName, phoneNumber, userName, password, isStaff, isAdmin, isActive);
					persons.add(p);
				}
				return persons;
			} catch (SQLException e) {
				System.out.println("DB Query failed");
				return new ArrayList<>();
			}
		}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getIsStaff() {
		return isStaff;
	}

	public void setIsStaff(Boolean isStaff) {
		this.isStaff = isStaff;
	}

	public Boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	
	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	
	@Override
	public String toString() {
		return "Person [firstName=" + firstName + " " + "lastName=" + lastName + " " + "Phone Number=" + phoneNumber 
				+ " " + "Username=" + userName + " " + "Password=" + password + " " + "isAdmin=" + isAdmin 
				+ " " + "isStaff=" + isStaff + " " + "isActive=" + isActive + "]";
	}

}
