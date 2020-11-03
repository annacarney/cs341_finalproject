import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class nonMember {
	
		private String firstName;
		private String lastName;
		private String phoneNumber;
		
		//Constructor
		public nonMember(String firstName, String lastName, String phoneNumber) {
			super();
			this.firstName = firstName;
			this.lastName = lastName;
			this.phoneNumber = phoneNumber;
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

