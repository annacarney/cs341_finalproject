
public class program {
	
	private int classID;
	private String className;
	private String classDesc;
	private int classSize;
	private String startTime;			//Format: YYYY-MM-DD HH:MM:SS:SSS
	private String endTime;				//Format: YYYY-MM-DD HH:MM:SS:SSS
	private Double memFee;
	private Double nonMemFee;
	
	//Constructor
	public program(int classID, String className, String classDesc, int classSize, String startTime, String endTime, Double memFee, Double nonMemFee) {
		super();
		this.classID = classID;
		this.className = className;
		this.classDesc = classDesc;
		this.classSize = classSize;
		this.startTime = startTime;
		this.endTime = endTime;
		this.memFee = memFee;
		this.nonMemFee = nonMemFee;
	}

	@Override
	public String toString() {
		return "program [classID=" + classID + ", className=" + className + ", classDesc=" + classDesc + ", classSize="
				+ classSize + ", startTime=" + startTime + ", endTime=" + endTime + ", memFee=" + memFee
				+ ", nonMemFee=" + nonMemFee + "]";
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
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
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

}
