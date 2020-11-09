
public class enrolled {
	
	private String username;
	private int classid;
	
	//constructor
	public enrolled(String username, int classid) {
		super();
		this.username = username;
		this.classid = classid;
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