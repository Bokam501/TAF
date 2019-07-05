package com.hcl.atf.taf.model.dto;

import java.util.Date;

public class RiskHazardTraceabilityMatrixDTO {
	private Integer riskId;
	private  String riskName;
	private Integer featureId;
	private Integer riskMitigationId;
	private Integer testCaseId;
	private String SeverityName1;
	private String LikeliHoodName1;
	private String RiskPriority1;
	private String SeverityName2;
	private String LikeliHoodName2;
	private String RiskPriority2;
	private String RiskMitigationCode;
	//testcase table 
	private String result;
	private Integer iterationNumber;
	private String lifecyclephase;
	private Date date;
	private Integer testcaseexeid;
	private String testcasename;
	//Fix Fail Details Table
	private Integer bugId;
	private Date bugCreationTime;
	private String severityName;
	private Integer scriptId;
	private String reOpenDate;
	//Test Report
	private String stageName;
	private String status;
	private String resolution;
	private String scriptdate;
	
	
	public Integer getRiskId() {
		return riskId;
	}
	public void setRiskId(Integer riskId) {
		this.riskId = riskId;
	}
	public String getRiskName() {
		return riskName;
	}
	public void setRiskName(String riskName) {
		this.riskName = riskName;
	}
	public Integer getFeatureId() {
		return featureId;
	}
	public void setFeatureId(Integer featureId) {
		this.featureId = featureId;
	}
	public Integer getRiskMitigationId() {
		return riskMitigationId;
	}
	public void setRiskMitigationId(Integer riskMitigationId) {
		this.riskMitigationId = riskMitigationId;
	}
	public Integer getTestCaseId() {
		return testCaseId;
	}
	public void setTestCaseId(Integer testCaseId) {
		this.testCaseId = testCaseId;
	}
	public String getSeverityName2() {
		return SeverityName2;
	}
	public void setSeverityName2(String severityName2) {
		SeverityName2 = severityName2;
	}
	public String getLikeliHoodName2() {
		return LikeliHoodName2;
	}
	public void setLikeliHoodName2(String likeliHoodName2) {
		LikeliHoodName2 = likeliHoodName2;
	}
	public String getRiskPriority2() {
		return RiskPriority2;
	}
	public void setRiskPriority2(String riskPriority2) {
		RiskPriority2 = riskPriority2;
	}
	public String getSeverityName1() {
		return SeverityName1;
	}
	public void setSeverityName1(String severityName1) {
		SeverityName1 = severityName1;
	}
	public String getLikeliHoodName1() {
		return LikeliHoodName1;
	}
	public void setLikeliHoodName1(String likeliHoodName1) {
		LikeliHoodName1 = likeliHoodName1;
	}
	public String getRiskPriority1() {
		return RiskPriority1;
	}
	public void setRiskPriority1(String riskPriority1) {
		RiskPriority1 = riskPriority1;
	}
	public String getRiskMitigationCode() {
		return RiskMitigationCode;
	}
	public void setRiskMitigationCode(String riskMitigationCode) {
		RiskMitigationCode = riskMitigationCode;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public Integer getIterationNumber() {
		return iterationNumber;
	}
	public void setIterationNumber(Integer iterationNumber) {
		this.iterationNumber = iterationNumber;
	}
	public String getLifecyclephase() {
		return lifecyclephase;
	}
	public void setLifecyclephase(String lifecyclephase) {
		this.lifecyclephase = lifecyclephase;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Integer getTestcaseexeid() {
		return testcaseexeid;
	}
	public void setTestcaseexeid(Integer testcaseexeid) {
		this.testcaseexeid = testcaseexeid;
	}
	public String getTestcasename() {
		return testcasename;
	}
	public void setTestcasename(String testcasename) {
		this.testcasename = testcasename;
	}
	public Integer getBugId() {
		return bugId;
	}
	public void setBugId(Integer bugId) {
		this.bugId = bugId;
	}
	public Date getBugCreationTime() {
		return bugCreationTime;
	}
	public void setBugCreationTime(Date bugCreationTime) {
		this.bugCreationTime = bugCreationTime;
	}
	public String getSeverityName() {
		return severityName;
	}
	public void setSeverityName(String severityName) {
		this.severityName = severityName;
	}
	public Integer getScriptId() {
		return scriptId;
	}
	public void setScriptId(Integer scriptId) {
		this.scriptId = scriptId;
	}
	public String getReOpenDate() {
		return reOpenDate;
	}
	public void setReOpenDate(String reOpenDate) {
		this.reOpenDate = reOpenDate;
	}
	public String getStageName() {
		return stageName;
	}
	public void setStageName(String stageName) {
		this.stageName = stageName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getResolution() {
		return resolution;
	}
	public void setResolution(String resolution) {
		this.resolution = resolution;
	}
	public String getScriptdate() {
		return scriptdate;
	}
	public void setScriptdate(String scriptdate) {
		this.scriptdate = scriptdate;
	}
	

}
