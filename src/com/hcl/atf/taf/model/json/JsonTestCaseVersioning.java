package com.hcl.atf.taf.model.json;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.WorkPackage;
import com.hcl.atf.taf.model.WorkPackageTestCaseExecutionPlan;
import com.hcl.atf.taf.model.WorkShiftMaster;
import com.hcl.atf.taf.model.WorkpackageRunConfiguration;

public class JsonTestCaseVersioning implements java.io.Serializable {
	private static final Log log = LogFactory
			.getLog(JsonTestCaseVersioning.class);
	
	@JsonProperty	
	private int id;	
	@JsonProperty	
	private int testcaseId;	
	@JsonProperty	
	private String testcaseName;	
	@JsonProperty	
	private String isSelected;
	@JsonProperty	
	private String allVersions;
	@JsonProperty	
	private String clearVersions;
	@JsonProperty
	private int productId;
	@JsonProperty	
	private String productName;
	@JsonProperty
	private int productVersionId;
	@JsonProperty	
	private String productVersionName;
	@JsonProperty	
	private String environmentCombinationName;
	@JsonProperty	
	private Integer environmentCombinationId;
	@JsonProperty	
	private String ver1;
	@JsonProperty	
	private String ver2;
	@JsonProperty	
	private String ver3;
	@JsonProperty	
	private String ver4;
	@JsonProperty	
	private String ver5;
	@JsonProperty	
	private String ver6;
	@JsonProperty	
	private String ver7;
	@JsonProperty	
	private String ver8;
	@JsonProperty	
	private String ver9;
	@JsonProperty	
	private String ver10;
	@JsonProperty	
	private String ver11;
	@JsonProperty	
	private String ver12;
	@JsonProperty	
	private String ver13;
	@JsonProperty	
	private String ver14;
	@JsonProperty	
	private String ver15;
	@JsonProperty	
	private String ver16;
	@JsonProperty	
	private String ver17;
	@JsonProperty	
	private String ver18;
	@JsonProperty	
	private String ver19;
	@JsonProperty	
	private String ver20;
	
	public JsonTestCaseVersioning() {
	}	

	public JsonTestCaseVersioning(WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan) {
//		
		try{
		this.id=workPackageTestCaseExecutionPlan.getId();
		
		this.testcaseId=workPackageTestCaseExecutionPlan.getTestCase().getTestCaseId();
		this.testcaseName=workPackageTestCaseExecutionPlan.getTestCase().getTestCaseName();
		this.allVersions="1";
		this.clearVersions="0";
		if(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion() != null){
			this.productVersionId = workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion().getProductVersionListId();
			this.productVersionName = workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion().getProductVersionName();
		}
		
		if(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion().getProductMaster() != null){
			this.productId = workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductId();
			this.productName = workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductName();
		}
		this.ver1 = "0";
		this.ver2 = "0";
		this.ver3 = "0";
		this.ver4 = "0";
		this.ver5 = "0";
		this.ver6 = "0";
		this.ver7 = "0";
		this.ver8 = "0";
		this.ver9 = "0";
		this.ver10 = "0";
		this.ver11 = "0";
		this.ver12 = "0";
		this.ver13 = "0";
		this.ver14 = "0";
		this.ver15 = "0";
		this.ver16 = "0";
		this.ver17 = "0";
		this.ver18 = "0";
		this.ver19 = "0";
		this.ver20 = "0";
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	

	@JsonIgnore
	public WorkPackageTestCaseExecutionPlan getWorkPackageTestCaseExecutionPlan() {
		
	
		WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan = new WorkPackageTestCaseExecutionPlan();
		workPackageTestCaseExecutionPlan.setId(this.getId());
		
		TestCaseList testCase = new TestCaseList();
		testCase.setTestCaseId(this.testcaseId);
		workPackageTestCaseExecutionPlan.setTestCase(testCase);
		
		
		WorkPackage workPackage = new WorkPackage();
		
		workPackageTestCaseExecutionPlan.setWorkPackage(workPackage);
	
		WorkpackageRunConfiguration wprc= new WorkpackageRunConfiguration();		
		
		workPackageTestCaseExecutionPlan.setModifiedDate(DateUtility.getCurrentTime());
		WorkShiftMaster plannedWorkShiftMaster=new WorkShiftMaster();
		return workPackageTestCaseExecutionPlan;
	}
	
	
	public static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    }
	    // only got here if we didn't return false
	    return true;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
	
	public String getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(String isSelected) {
		this.isSelected = isSelected;
	}

	public String getAllVersions() {
		return allVersions;
	}

	public void setAllVersions(String allVersions) {
		this.allVersions = allVersions;
	}

	public String getClearVersions() {
		return clearVersions;
	}

	public void setClearVersions(String clearVersions) {
		this.clearVersions = clearVersions;
	}

	
	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getProductVersionId() {
		return productVersionId;
	}

	public void setProductVersionId(int productVersionId) {
		this.productVersionId = productVersionId;
	}

	public String getProductVersionName() {
		return productVersionName;
	}

	public void setProductVersionName(String productVersionName) {
		this.productVersionName = productVersionName;
	}


	}
