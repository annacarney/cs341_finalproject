import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/*
 * program.java
 * Program object class
 * Created: 10/10/2020
 */
public class program {
	private int classID;
	private String className;
	private String classDesc;
	private int classSize;			
	private String startTime;					//Format: HH:MM
	private String endTime;					//Format: HH:MM
	private Double memFee;
	private Double nonMemFee;
	
	private String startDate;				//	Format: YYYY-MM-DD
	private String endDate;				//	Format: YYYY-MM-DD
	private String days;				// Mon, Tues, Wed, Thurs, Fri, Sat, Sun
	private String location;
	private Boolean isActive;
	
	/*
	Constructor
	 * 
	 */
	public program(int classID, String className, String classDesc, int classSize, String startTime, String endTime, Double memFee, 
			Double nonMemFee, String startDate, String endDate, String days, String location, Boolean isActive) {
		super();
		this.classID = classID;
		this.className = className;
		this.classDesc = classDesc;
		this.classSize = classSize;
		//this.startTime = LocalDateTime.parse(startTime, SQL_FORMAT);
		//this.endTime = LocalDateTime.parse(endTime, SQL_FORMAT);		
		this.memFee = memFee;
		this.nonMemFee = nonMemFee;
		
		//startTime_inDB = startTime;
		//endTime_inDB = endTime; 
		
		this.startTime = startTime;
		this.endTime = endTime;
		
		this.startDate = startDate;
		this.endDate = endDate;
		this.days = days;
		this.location = location;
		this.isActive = isActive;
	}
	
	/*
	query helper
	 * 
	 */
	public static ArrayList<program> find(database db, String where) {
		ResultSet pResults;
		try {
			pResults = db.runQuery("SELECT classID, className, classDesc, classSize, startTime, endTime, memFee, nonMemFee, startDate, endDate, days, location, isActive FROM Program " + (where != null ? "WHERE " + where : ""));
			ArrayList<program> programs = new ArrayList<>();
			
			while(pResults.next()) {
				int classID = pResults.getInt("classID");
				String className = pResults.getString("className");
				String classDesc = pResults.getString("classDesc");
				int classSize = pResults.getInt("classSize");
				String startTime = pResults.getString("startTime");
				String endTime = pResults.getString("endTime");
				Double memFee = pResults.getDouble("memFee");
				Double nonMemFee = pResults.getDouble("nonMemFee");
				
				String startDate = pResults.getString("startDate");
				String endDate = pResults.getString("endDate");
				String days = pResults.getString("days");
				String location = pResults.getString("location");
				Boolean isActive = pResults.getBoolean("isActive");
				
				program p = new program(classID, className, classDesc, classSize, startTime, endTime, memFee, nonMemFee, startDate, endDate, days, location, isActive);
				programs.add(p);
			}
			
			return programs;
		} catch (SQLException e) {
			System.out.println("DB Query failed in method getProgramsList()");
			return new ArrayList<>();
		}
	}
	
	/*
	another query helper to find unique program times
	 * 
	 */
	public static ArrayList<String> findDistinctTimes(database db) {
		ResultSet pResults;
		try {
			pResults = db.runQuery("SELECT DISTINCT startTime FROM Program ");
			ArrayList<String> programs = new ArrayList<>();
			
			while(pResults.next()) {
				String startTime = pResults.getString("startTime");
				Boolean isActive = pResults.getBoolean("isActive");
				//if the program isn't active, skip. [SRW 12/6/2020]
				if (!isActive) {
					continue;
				}
				programs.add(startTime);
			}
			
			return programs;
		} catch (SQLException e) {
			System.out.println("DB Query failed");
			return new ArrayList<>();
		}	
	}
	
	public static ArrayList<program> findAll(database db) {
		return find(db, null);
	}

	@Override
	public String toString() {
		return "Program [classID=" + classID + ", className=" + className + ", classDesc=" + classDesc + ", classSize="
				+ classSize + ", startTime=" + startTime + ", endTime=" + endTime + ", memFee=" + memFee
				+ ", nonMemFee=" + nonMemFee + "startTime= " + startTime + "endTime= " + endTime + "startDate" + startDate + "endDate= " 
				+ endDate + "days= " + days + " " + "location= " + location + " " + "isActive= " + isActive + "]";
	}

	public int getClassID() {
		return classID;
	}

	public void setClassID(int classID) {
		this.classID = classID;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getClassDesc() {
		return classDesc;
	}

	public void setClassDesc(String classDesc) {
		this.classDesc = classDesc;
	}

	public int getClassSize() {
		return classSize;
	}

	public void setClassSize(int classSize) {
		this.classSize = classSize;
	}

	public String getStartTime() {
		return startTime;
		//return startTime.format(SQL_FORMAT);
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
		//return endTime.format(SQL_FORMAT);
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
		//this.endTime = LocalDateTime.parse(endTime, SQL_FORMAT);
	}

	public Double getMemFee() {
		return memFee;
	}

	public void setMemFee(Double memFee) {
		this.memFee = memFee;
	}

	public Double getNonMemFee() {
		return nonMemFee;
	}

	public void setNonMemFee(Double nonMemFee) {
		this.nonMemFee = nonMemFee;
	}
	
	public String getStartDate() {
		return startDate;
	}
	
	public void setStartDate(String s) {
		this.startDate = s;
	}
	
	public String getEndDate() {
		return endDate;
	}
	
	public void setEndDate(String e) {
		this.endDate = e;
	}
	
	public String getDays() {
		return days;
	}
	
	public void setDays(String d) {
		this.days = d;
	}
	
	public String getLocation() {
		return location;
	}
	
	public void setLocation(String l) {
		this.location = l;
	}
	
	public Boolean getIsActive() {
		return isActive;
	}
	
	public void setIsActive(Boolean i) {
		this.isActive = i;
	}

}
