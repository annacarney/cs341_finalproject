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
	
	public person(String firstName, String lastName, String phoneNumber, String userName, String password, Boolean isStaff, Boolean isAdmin) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.userName = userName;
		this.password = password;
		this.isStaff = isStaff;
		this.isAdmin = isAdmin;
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
	
	@Override
	public String toString() {
		return "Person [firstName=" + firstName + " " + "lastName=" + lastName + " " + "Phone Number=" + phoneNumber + " " + "Username=" + userName + " " + "Password=" + password + " " + "isAdmin=" + isAdmin + " " + "isStaff=" + isStaff + "]";
	}

}
