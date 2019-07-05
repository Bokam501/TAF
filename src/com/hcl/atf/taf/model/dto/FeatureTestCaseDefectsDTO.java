package com.hcl.atf.taf.model.dto;


public class FeatureTestCaseDefectsDTO {
	
	private Integer workPackageId;
	private String workPackageName;
	private int testcaseId;
	private String testcaseName;
	
	private String openDefects;
	private String closedDefects;
	private String totalDefects;
	
	
	public Integer getWorkPackageId() {
		return workPackageId;
	}
	public void setWorkPackageId(Integer workPackageId) {
		this.workPackageId = workPackageId;
	}
	public String getWorkPackageName() {
		return workPackageName;
	}
	public void setWorkPackageName(String workPackageName) {
		this.workPackageName = workPackageName;
	}
	public int getTestcaseId() {
		return testcaseId;
	}
	public void setTestcaseId(int testcaseId) {
		this.testcaseId = testcaseId;
	}
	public String getTestcaseName() {
		return testcaseName;
	}
	public void setTestcaseName(String testcaseName) {
		this.testcaseName = testcaseName;
	}
	
	public String getOpenDefects() {
		return openDefects;
	}
	public void setOpenDefects(String openDefects) {
		this.openDefects = openDefects;
	}
	public String getClosedDefects() {
		return closedDefects;
	}
	public void setClosedDefects(String closedDefects) {
		this.closedDefects = closedDefects;
	}
	public String getTotalDefects() {
		return totalDefects;
	}
	public void setTotalDefects(String totalDefects) {
		this.totalDefects = totalDefects;
	}
	
	
}
