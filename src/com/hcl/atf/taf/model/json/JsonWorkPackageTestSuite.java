package com.hcl.atf.taf.model.json;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.TestSuiteList;
import com.hcl.atf.taf.model.WorkPackage;
import com.hcl.atf.taf.model.WorkPackageTestSuite;


public class JsonWorkPackageTestSuite implements java.io.Serializable{
	
	@JsonProperty	
	private int id;
	@JsonProperty	
	private int testsuiteId;
	@JsonProperty	
	private String testsuiteName;
	@JsonProperty	
	private String isSelected;
	@JsonProperty
	private int workPackageId;
	@JsonProperty	
	private String workPackageName;
	@JsonProperty	
	private String editedDate;
	@JsonProperty	
	private String createdDate;
	@JsonProperty	
	private String status;
	
	@JsonProperty	
	private String recommendedByITF;
	@JsonProperty	
	private String recommendationType;

	
	public JsonWorkPackageTestSuite() {
	}	

	public JsonWorkPackageTestSuite(WorkPackageTestSuite workPackageTestSuite) {
		this.id=workPackageTestSuite.getWorkpackageTestSuiteId();
		this.testsuiteId=workPackageTestSuite.getTestSuite().getTestSuiteId();
		this.testsuiteName=workPackageTestSuite.getTestSuite().getTestSuiteName();
		
		if (workPackageTestSuite.getIsSelected() == 0)
			this.isSelected="0";
		else
			this.isSelected="1";
		
		this.workPackageId=workPackageTestSuite.getWorkPackage().getWorkPackageId();
		this.workPackageName=workPackageTestSuite.getWorkPackage().getName();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(workPackageTestSuite.getCreatedDate()!=null)
			this.createdDate=sdf.format(workPackageTestSuite.getCreatedDate());
		if(workPackageTestSuite.getEditedDate()!=null)
			this.editedDate=sdf.format(workPackageTestSuite.getEditedDate());
		this.status=workPackageTestSuite.getStatus();
		
		this.recommendedByITF = "TBD";
		this.recommendationType = "TBD";
	}

	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTestsuiteId() {
		return testsuiteId;
	}

	public void setTestsuiteId(int testsuiteId) {
		this.testsuiteId = testsuiteId;
	}

	public String getTestsuiteName() {
		return testsuiteName;
	}

	public void setTestsuiteName(String testsuiteName) {
		this.testsuiteName = testsuiteName;
	}

	public String getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(String isSelected) {
		this.isSelected = isSelected;
	}

	public int getWorkPackageId() {
		return workPackageId;
	}

	public void setWorkPackageId(int workPackageId) {
		this.workPackageId = workPackageId;
	}

	public String getWorkPackageName() {
		return workPackageName;
	}

	public void setWorkPackageName(String workPackageName) {
		this.workPackageName = workPackageName;
	}

	public String getEditedDate() {
		return editedDate;
	}

	public void setEditedDate(String editedDate) {
		this.editedDate = editedDate;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@JsonIgnore
	public WorkPackageTestSuite getWorkPackageTestSuite() {
	
		WorkPackageTestSuite workPackageTestSuite = new WorkPackageTestSuite();
		workPackageTestSuite.setWorkpackageTestSuiteId(this.id);
		if (this.isSelected!=null && this.isSelected.equalsIgnoreCase("1") || this.isSelected.equalsIgnoreCase("Yes")) {
			workPackageTestSuite.setIsSelected(1);
		} else {
			workPackageTestSuite.setIsSelected(0);
		}
		TestSuiteList testSuite = new TestSuiteList();
		testSuite.setTestSuiteId(this.testsuiteId);
		workPackageTestSuite.setTestSuite(testSuite);
		
		WorkPackage workPackage = new WorkPackage();
		workPackage.setWorkPackageId(this.workPackageId);
		workPackageTestSuite.setWorkPackage(workPackage);
		
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
		if(this.createdDate == null || this.createdDate.trim().isEmpty()) {
			workPackageTestSuite.setCreatedDate(new Date(System.currentTimeMillis()));
		} else {
			try {
				workPackageTestSuite.setCreatedDate(dateformat.parse(this.createdDate));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		workPackageTestSuite.setEditedDate(new Date(System.currentTimeMillis()));
		workPackageTestSuite.setStatus(this.status);
		
		return workPackageTestSuite;
	}
	
}
