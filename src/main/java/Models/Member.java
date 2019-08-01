package Models;

//This was and should be called User, but it conflicts with the JDA user so we're gonna go with Member instead (even though theres a JDA member too..)
public class Member {
	
	private String userID;
	private String warnings;
	private String bans;
	private String joinDate;
	private String username;
	private String kicks;
	
	public Member(String userID, String username, String joinDate,  String warnings, String kicks, String bans) {
		this.userID = userID;
		this.warnings = warnings;
		this.bans = bans;
		this.joinDate = joinDate;
		this.username = username;
		this.kicks = kicks;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getWarnings() {
		return warnings;
	}
	public void setWarnings(String warnings) {
		this.warnings = warnings;
	}
	public String getBans() {
		return bans;
	}
	public void setBans(String bans) {
		this.bans = bans;
	}
	public String getJoinDate() {
		return joinDate;
	}
	public void setJoinDate(String joinDate) {
		this.joinDate = joinDate;
	}
	
	public String getKicks() {
		return kicks;
	}
	
	public void setKicks(String kicks) {
		this.kicks = kicks;
	}
}
