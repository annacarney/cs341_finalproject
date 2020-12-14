/*
 * nonMember.java
 * non member object class
 * Created: 10/10/2020
 */
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class nonMember {
	
		private String firstName;
		private String lastName;
		private String phoneNumber;
		
		/*
		Constructor
		 * 
		 */
		public nonMember(String firstName, String lastName, String phoneNumber) {
			super();
			this.firstName = firstName;
			this.lastName = lastName;
			this.phoneNumber = phoneNumber;
		}
		
		/*
		query helper
		 * 
		 */
				public static ArrayList<nonMember> find(database db, String where) {
					ResultSet pResults;
					try {
						pResults = db.runQuery("SELECT firstName, lastName, phoneNumber FROM NonMember " + (where != null ? "WHERE " + where : ""));
						ArrayList<nonMember> nonmems = new ArrayList<>();
						
						while(pResults.next()) {
							String firstName = pResults.getString("firstName");
							String lastName = pResults.getString("lastName");
							String phoneNumber = pResults.getString("phoneNumber");
							
							nonMember nm = new nonMember(firstName, lastName, phoneNumber);
							nonmems.add(nm);
						}
						return nonmems;
					} catch (SQLException e) {
						System.out.println("DB Query failed");
						return new ArrayList<>();
					}
				}
		
		@Override
		public String toString() {
			return "nonMember [firstName=" + firstName + ", lastName=" + lastName + ", phoneNumber=" + phoneNumber
					+ "]";
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

}

