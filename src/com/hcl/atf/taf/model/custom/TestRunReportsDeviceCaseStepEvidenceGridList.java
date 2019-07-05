package com.hcl.atf.taf.model.custom;

import java.util.HashMap;

/*
 * Corresponds to one row of screenshots of the Evidence Report. 
 */
public class TestRunReportsDeviceCaseStepEvidenceGridList {

	private Integer testRunNo;
	private Integer testRunConfigurationChildId;
	private Integer testCaseId;
	private Integer testStepId;
	private String testCaseName;
	private String testStepName;
	private String testStepDescription;
	private String screenShotLabel;
	//private List<String> screenShotBaseFolderPathList;
	//private List<String> screenShotPathList; //A row of Screenshot paths for this Test Step, for all the devices/testrunlists in the TestRunConfigurationChild
	
	private HashMap<Integer,String> screenShotPathMap; //A row of Screenshot paths for this Test Step, for all the devices/testrunlists in the TestRunConfigurationChild
													   // Key is testRunListId and Value is screenShotPath (filename)
	
	public TestRunReportsDeviceCaseStepEvidenceGridList() {
		
	}

	public TestRunReportsDeviceCaseStepEvidenceGridList(Integer testRunNo, 
							Integer testRunConfigurationChildId,
							Integer testCaseId,
							Integer testStepId,
							String testCaseName,
							String testStepName,
							String testStepDescription,
							String screenShotLabel,
							//List<String> screenShotBaseFolderPathList,
							//List<String> screenShotPathList,
							HashMap<Integer,String> screenShotPathMap
							) {
		this.testRunNo=testRunNo;
		this.testRunConfigurationChildId=testRunConfigurationChildId;
		this.testCaseId=testCaseId;
		this.testStepId=testStepId;
		this.testCaseName=testCaseName;
		this.testStepName=testStepName;
		this.testStepDescription=testStepDescription;
		this.screenShotLabel = screenShotLabel;
		//this.screenShotBaseFolderPathList = screenShotBaseFolderPathList;
		//this.screenShotPathList = screenShotPathList;
		this.screenShotPathMap = screenShotPathMap;
	}
	
	public Integer getTestRunNo() {
		return testRunNo;
	}

	public void setTestRunNo(Integer testRunNo) {
		this.testRunNo = testRunNo;
	}

	public Integer getTestRunConfigurationChildId() {
		return testRunConfigurationChildId;
	}

	public void setTestRunConfigurationChildId(Integer testRunConfigurationChildId) {
		this.testRunConfigurationChildId = testRunConfigurationChildId;
	}

	public Integer getTestCaseId() {
		return testCaseId;
	}

	public void setTestCaseId(Integer testCaseId) {
		this.testCaseId = testCaseId;
	}

	public Integer getTestStepId() {
		return testStepId;
	}

	public void setTestStepId(Integer testStepId) {
		this.testStepId = testStepId;
	}
	
	public String getTestCaseName() {
		return testCaseName;
	}

	public void setTestCaseName(String testCaseName) {
		this.testCaseName = testCaseName;
	}
	
	public String getTestStepName() {
		return testStepName;
	}

	public void setTestStepName(String testStepName) {
		this.testStepName = testStepName;
	}
	
	public String getTestStepDescription() {
		return testStepDescription;
	}

	public void setTestStepDescription(String testStepDescription) {
		this.testStepDescription = testStepDescription;
	}
	
	public String getScreenShotLabel() {
		return screenShotLabel;
	}
	public void setScreenShotLabel(String screenShotLabel) {
		this.screenShotLabel = screenShotLabel;
	}

	/*
	public List<String> getScreenShotBaseFolderPathList() {
		return screenShotBaseFolderPathList;
	}

	public void setScreenShotBaseFolderPathList(List<String> screenShotBaseFolderPathList) {
		this.screenShotBaseFolderPathList = screenShotBaseFolderPathList;
	}

	public List<String> getScreenShotPathList() {
		return screenShotPathList;
	}

	public void setScreenShotPathList(List<String> screenShotPathList) {
		this.screenShotPathList = screenShotPathList;
	}
	*/
	
	public HashMap<Integer,String> getScreenShotPathMap() {
		return screenShotPathMap;
	}

	public void setScreenShotPathMap(HashMap<Integer,String> screenShotPathMap) {
		this.screenShotPathMap = screenShotPathMap;
	}
}
