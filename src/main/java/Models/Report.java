package Models;

public class Report {
	
	private int report_id;
	private String reported_user_id;
	private String reporting_user_id;
	private String report_text;
	
	public Report(int report_id, String reported_user_id, String reporting_user_id, String report_text) {
		this.report_id = report_id;
		this.reported_user_id = reported_user_id;
		this.reporting_user_id = reporting_user_id;
		this.report_text = report_text;
	}
	public int getReport_id() {
		return report_id;
	}
	public void setReport_id(int report_id) {
		this.report_id = report_id;
	}
	public String getReported_user_id() {
		return reported_user_id;
	}
	public void setReported_user_id(String reported_user_id) {
		this.reported_user_id = reported_user_id;
	}
	public String getReporting_user_id() {
		return reporting_user_id;
	}
	public void setReporting_user_id(String reporting_user_id) {
		this.reporting_user_id = reporting_user_id;
	}
	public String getReport_text() {
		return report_text;
	}
	public void setReport_text(String report_text) {
		this.report_text = report_text;
	}
	
	
}
