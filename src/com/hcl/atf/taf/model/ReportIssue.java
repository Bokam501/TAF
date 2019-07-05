package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "report_issue")
public class ReportIssue {
	
	private Integer reportIssueId;
	private String reportIssueName;
	private String reportIssueType;
	private String reportIssueComment;
	private String reportLoginId;
	private Date reportIssueDate;
	private String reportIssueStatus;
	
	
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "reportIssueId", unique = true, nullable = false)
	public Integer getReportIssueId() {
		return reportIssueId;
	}
	public void setReportIssueId(Integer reportIssueId) {
		this.reportIssueId = reportIssueId;
	}
	@Column(name = "reportIssueName")
	public String getReportIssueName() {
		return reportIssueName;
	}
	public void setReportIssueName(String reportIssueName) {
		this.reportIssueName = reportIssueName;
	}
	@Column(name = "reportIssueType")
	public String getReportIssueType() {
		return reportIssueType;
	}
	public void setReportIssueType(String reportIssueType) {
		this.reportIssueType = reportIssueType;
	}
	@Column(name = "reportIssueComment")
	public String getReportIssueComment() {
		return reportIssueComment;
	}
	
	public void setReportIssueComment(String reportIssueComment) {
		this.reportIssueComment = reportIssueComment;
	}
	
	@Column(name="reportLoginId")
	public String getReportLoginId() {
		return reportLoginId;
	}
	public void setReportLoginId(String reportLoginId) {
		this.reportLoginId = reportLoginId;
	}
	@Column(name="reportIssueDate")
	public Date getReportIssueDate() {
		return reportIssueDate;
	}
	public void setReportIssueDate(Date reportIssueDate) {
		this.reportIssueDate = reportIssueDate;
	}
	
	@Column(name="reportIssueStatus")
	public String getReportIssueStatus() {
		return reportIssueStatus;
	}
	public void setReportIssueStatus(String reportIssueStatus) {
		this.reportIssueStatus = reportIssueStatus;
	}
	
	
	
	
	
	
}
