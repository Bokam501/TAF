package com.hcl.atf.taf.model.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.ReportIssue;

public class JsonReporIssue {
	
	@JsonProperty
	private Integer reportIssueid;
	@JsonProperty
	private String reportIssuename;
	@JsonProperty
	private String reportIssuetype;
	@JsonProperty
	private String reportIssuecomment;
	@JsonProperty
	private Integer attachmentCount;

	@JsonProperty
	private String reportIssueLoginId;
	@JsonProperty
	private String reportIssueDate;
	@JsonProperty
	private String reportIssueStatus;
	
	
	public JsonReporIssue() {
		
	}
	public JsonReporIssue(ReportIssue reportIssue){
		
		this.reportIssueid = reportIssue.getReportIssueId();
		this.reportIssuename = reportIssue.getReportIssueName();
		this.reportIssuetype = reportIssue.getReportIssueType();
		this.reportIssuecomment = reportIssue.getReportIssueComment();
		this.reportIssueLoginId = reportIssue.getReportLoginId();
		if(reportIssue.getReportIssueDate() != null) {
			this.reportIssueDate = DateUtility.dateformatWithOutTime(reportIssue.getReportIssueDate());
		}
		this.reportIssueStatus = reportIssue.getReportIssueStatus();
		
		
	}
	@JsonIgnore
	public ReportIssue getResponseIssue() {
		
		ReportIssue reportIssue = new ReportIssue();
		reportIssue.setReportIssueId(reportIssueid);
		reportIssue.setReportIssueName(reportIssuename);
		reportIssue.setReportIssueType(reportIssuetype);
		reportIssue.setReportIssueComment(reportIssuecomment);
		reportIssue.setReportLoginId(reportIssueLoginId);
		reportIssue.setReportIssueDate(DateUtility.dateformatWithOutTime(reportIssueDate));
		reportIssue.setReportIssueStatus(reportIssueStatus);
		return reportIssue;
	}
	
	public Integer getReportIssueid() {
		return reportIssueid;
	}
	public void setReportIssueid(Integer reportIssueid) {
		this.reportIssueid = reportIssueid;
	}
	public String getReportIssuename() {
		return reportIssuename;
	}
	public void setReportIssuename(String reportIssuename) {
		this.reportIssuename = reportIssuename;
	}
	public String getReportIssuetype() {
		return reportIssuetype;
	}
	public void setReportIssuetype(String reportIssuetype) {
		this.reportIssuetype = reportIssuetype;
	}
	public String getReportIssuecomment() {
		return reportIssuecomment;
	}
	public void setReportIssuecomment(String reportIssuecomment) {
		this.reportIssuecomment = reportIssuecomment;
	}
	public Integer getAttachmentCount() {
		return attachmentCount;
	}
	public void setAttachmentCount(Integer attachmentCount) {
		this.attachmentCount = attachmentCount;
	}
	
	public String getReportIssueDate() {
		return reportIssueDate;
	}
	public void setReportIssueDate(String reportIssueDate) {
		this.reportIssueDate = reportIssueDate;
	}
	public String getReportIssueStatus() {
		return reportIssueStatus;
	}
	public void setReportIssueStatus(String reportIssueStatus) {
		this.reportIssueStatus = reportIssueStatus;
	}
	public String getReportIssueLoginId() {
		return reportIssueLoginId;
	}
	public void setReportIssueLoginId(String reportIssueLoginId) {
		this.reportIssueLoginId = reportIssueLoginId;
	}
	
	
	
}
