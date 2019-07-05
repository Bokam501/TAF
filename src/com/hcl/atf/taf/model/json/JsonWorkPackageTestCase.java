package com.hcl.atf.taf.model.json;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.DecouplingCategory;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.WorkPackage;
import com.hcl.atf.taf.model.WorkPackageTestCase;


public class JsonWorkPackageTestCase implements java.io.Serializable{
	
	private static final org.apache.commons.logging.Log log = LogFactory.getLog(JsonWorkPackageTestCase.class);
	
	@JsonProperty	
	private int id;
	@JsonProperty	
	private int testcaseId;
	@JsonProperty	
	private String testcaseName;
	@JsonProperty	
	private String testcaseDescription;
	@JsonProperty	
	private String testcaseCode;
	@JsonProperty	
	private String isSelected;
	@JsonProperty	
	private String allEnvironments;
	@JsonProperty	
	private String clearEnvironments;
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

	@JsonProperty
	private int decouplingCategoryId;
	
	@JsonProperty	
	private String decouplingCategoryName;
	
	public JsonWorkPackageTestCase() {
	}	

	public JsonWorkPackageTestCase(WorkPackageTestCase workPackageTestCase) {
		this.id=workPackageTestCase.getId();
		this.testcaseId=workPackageTestCase.getTestCase().getTestCaseId();
		this.testcaseName=workPackageTestCase.getTestCase().getTestCaseName();
		this.testcaseDescription=workPackageTestCase.getTestCase().getTestCaseDescription();
		this.testcaseCode=workPackageTestCase.getTestCase().getTestCaseCode();
		if (workPackageTestCase.getIsSelected() == 0)
			this.isSelected="0";
		else
			this.isSelected="1";
		
		this.workPackageId=workPackageTestCase.getWorkPackage().getWorkPackageId();
		this.workPackageName=workPackageTestCase.getWorkPackage().getName();
		this.allEnvironments="1";
		this.clearEnvironments="0";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(workPackageTestCase.getCreatedDate()!=null)
			this.createdDate=DateUtility.sdfDateformatWithOutTime(workPackageTestCase.getCreatedDate());
		if(workPackageTestCase.getEditedDate()!=null)
			this.editedDate=DateUtility.sdfDateformatWithOutTime(workPackageTestCase.getEditedDate());
		this.status=workPackageTestCase.getStatus();
		
		this.recommendedByITF = "TBD";
		this.recommendationType = "TBD";
		
		this.env1 = "0";
		this.env2 = "0";
		this.env3 = "0";
		this.env4 = "0";
		this.env5 = "0";
		this.env6 = "0";
		this.env7 = "0";
		this.env8 = "0";
		this.env9 = "0";
		this.env10 = "0";
		
		this.env11 = "0";
		this.env12 = "0";
		this.env13 = "0";
		this.env14 = "0";
		this.env15 = "0";
		this.env16 = "0";
		this.env17 = "0";
		this.env18 = "0";
		this.env19 = "0";
		this.env20 = "0";
		
		this.loc1="0";
		this.loc2="0";
		this.loc3="0";
		this.loc4="0";
		this.loc5="0";
		this.loc6="0";
		this.loc7="0";
		this.loc8="0";
		this.loc9="0";
		this.loc10="0";

		if(workPackageTestCase.getTestCase().getDecouplingCategory()!=null && workPackageTestCase.getTestCase().getDecouplingCategory().size()!=0){
			Set<DecouplingCategory> decouplingCategorySet =workPackageTestCase.getTestCase().getDecouplingCategory();
			if (decouplingCategorySet.size() != 0)
			{
				DecouplingCategory decouplingCategory = decouplingCategorySet.iterator().next();
				if (decouplingCategory != null)
				{
					decouplingCategoryId = decouplingCategory.getDecouplingCategoryId();
					decouplingCategoryName = decouplingCategory.getDecouplingCategoryName();				
				}
			}
		}
	}

	public JsonWorkPackageTestCase(WorkPackageTestCase workPackageTestCase, List<String> environmentSelectionValues,List<String> localeSelectedValues) {
		this.id=workPackageTestCase.getId();
		this.testcaseId=workPackageTestCase.getTestCase().getTestCaseId();
		this.testcaseName=workPackageTestCase.getTestCase().getTestCaseName();
		this.testcaseDescription=workPackageTestCase.getTestCase().getTestCaseDescription();
		this.testcaseCode=workPackageTestCase.getTestCase().getTestCaseCode();
		if(workPackageTestCase.getIsSelected()==1){
			this.isSelected="Yes";
		}else{
			this.isSelected="No";
		}
		this.workPackageId=workPackageTestCase.getWorkPackage().getWorkPackageId();
		this.workPackageName=workPackageTestCase.getWorkPackage().getName();
		this.allEnvironments="Yes";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
		if(workPackageTestCase.getCreatedDate()!=null)
			this.createdDate=sdf.format(workPackageTestCase.getCreatedDate());
		if(workPackageTestCase.getEditedDate()!=null)
			this.editedDate=sdf.format(workPackageTestCase.getEditedDate());
		
		this.status=workPackageTestCase.getStatus();

		this.env1 = "No";
		this.env2 = "No";
		this.env3 = "No";
		this.env4 = "No";
		this.env5 = "No";
		this.env6 = "No";
		this.env7 = "No";
		this.env8 = "No";
		this.env9 = "No";
		this.env10 = "No";
		
		this.env11 = "No";
		this.env12 = "No";
		this.env13 = "No";
		this.env14 = "No";
		this.env15 = "No";
		this.env16 = "No";
		this.env17 = "No";
		this.env18 = "No";
		this.env19 = "No";
		this.env20 = "No";
		
		
		this.loc1="No";
		this.loc2="No";
		this.loc3="No";
		this.loc4="No";
		this.loc5="No";
		this.loc6="No";
		this.loc7="No";
		this.loc8="No";
		this.loc9="No";
		this.loc10="No";

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

	public String getTestcaseDescription() {
		return testcaseDescription;
	}

	public void setTestcaseDescription(String testcaseDescription) {
		this.testcaseDescription = testcaseDescription;
	}

	public String getTestcaseCode() {
		return testcaseCode;
	}

	public void setTestcaseCode(String testcaseCode) {
		this.testcaseCode = testcaseCode;
	}

	public String getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(String isSelected) {
		this.isSelected = isSelected;
	}

	public String getAllEnvironments() {
		return allEnvironments;
	}

	public void setClearEnvironments(String clearEnvironments) {
		this.clearEnvironments = clearEnvironments;
	}

	public String getClearEnvironments() {
		return clearEnvironments;
	}

	public void setAllEnvironments(String allEnvironments) {
		this.allEnvironments = allEnvironments;
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
	public WorkPackageTestCase getWorkPackageTestCase() {
	
		WorkPackageTestCase workPackageTestCase = new WorkPackageTestCase();
		workPackageTestCase.setId(this.getId());
		if (this.isSelected!=null && this.isSelected.equalsIgnoreCase("1") || this.isSelected.equalsIgnoreCase("Yes")) {
			workPackageTestCase.setIsSelected(1);
		} else {
			workPackageTestCase.setIsSelected(0);
		}
		TestCaseList testCase = new TestCaseList();
		testCase.setTestCaseId(this.testcaseId);
		workPackageTestCase.setTestCase(testCase);
		
		WorkPackage workPackage = new WorkPackage();
		workPackage.setWorkPackageId(this.workPackageId);
		workPackageTestCase.setWorkPackage(workPackage);
		
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");		
		
		if(this.createdDate == null || this.createdDate.trim().isEmpty()) {
			workPackageTestCase.setCreatedDate(new Date(System.currentTimeMillis()));
		} else {
			try{
				//Commenting this line as the created date format is yyyy-MM-dd , so created new date format for parsing the date format to date.
				//workPackageTestCase.setCreatedDate(DateUtility.dateformatWithOutTime(this.createdDate));
				workPackageTestCase.setCreatedDate(DateUtility.dateNewFormatWithOutTime(this.createdDate));
			} catch(Exception e) {
				log.error("Error in parsing Created Date ("+this.createdDate.trim()+") to Date format ",e); 
			}				
		}		
		workPackageTestCase.setEditedDate(new Date(System.currentTimeMillis()));
		workPackageTestCase.setStatus(this.status);		
		return workPackageTestCase;
	}
	
	
	@JsonProperty	
	private String env1;
	@JsonProperty	
	private String env2;
	@JsonProperty	
	private String env3;
	@JsonProperty	
	private String env4;
	@JsonProperty	
	private String env5;
	@JsonProperty	
	private String env6;
	@JsonProperty	
	private String env7;
	@JsonProperty	
	private String env8;
	@JsonProperty	
	private String env9;
	@JsonProperty	
	private String env10;
	
	@JsonProperty	
	private String env11;
	@JsonProperty	
	private String env12;
	@JsonProperty	
	private String env13;
	@JsonProperty	
	private String env14;
	@JsonProperty	
	private String env15;
	@JsonProperty	
	private String env16;
	@JsonProperty	
	private String env17;
	@JsonProperty	
	private String env18;
	@JsonProperty	
	private String env19;
	@JsonProperty	
	private String env20;
	public String getEnv1() {
		return env1;
	}

	public void setEnv1(String env1) {
		this.env1 = env1;
	}

	public String getEnv2() {
		return env2;
	}

	public void setEnv2(String env2) {
		this.env2 = env2;
	}

	public String getEnv3() {
		return env3;
	}

	public void setEnv3(String env3) {
		this.env3 = env3;
	}

	public String getEnv4() {
		return env4;
	}

	public void setEnv4(String env4) {
		this.env4 = env4;
	}

	public String getEnv5() {
		return env5;
	}

	public void setEnv5(String env5) {
		this.env5 = env5;
	}

	public String getEnv6() {
		return env6;
	}

	public void setEnv6(String env6) {
		this.env6 = env6;
	}

	public String getEnv7() {
		return env7;
	}

	public void setEnv7(String env7) {
		this.env7 = env7;
	}

	public String getEnv8() {
		return env8;
	}

	public void setEnv8(String env8) {
		this.env8 = env8;
	}

	public String getEnv9() {
		return env9;
	}

	public void setEnv9(String env9) {
		this.env9 = env9;
	}

	public String getEnv10() {
		return env10;
	}

	public void setEnv10(String env10) {
		this.env10 = env10;
	}
	
	
	
	public String getRecommendedByITF() {
		return recommendedByITF;
	}

	public void setRecommendedByITF(String recommendedByITF) {
		this.recommendedByITF = recommendedByITF;
	}

	public String getRecommendationType() {
		return recommendationType;
	}

	public void setRecommendationType(String recommendationType) {
		this.recommendationType = recommendationType;
	}

	public String getEnv11() {
		return env11;
	}

	public void setEnv11(String env11) {
		this.env11 = env11;
	}

	public String getEnv12() {
		return env12;
	}

	public void setEnv12(String env12) {
		this.env12 = env12;
	}

	public String getEnv13() {
		return env13;
	}

	public void setEnv13(String env13) {
		this.env13 = env13;
	}

	public String getEnv14() {
		return env14;
	}

	public void setEnv14(String env14) {
		this.env14 = env14;
	}

	public String getEnv15() {
		return env15;
	}

	public void setEnv15(String env15) {
		this.env15 = env15;
	}

	public String getEnv16() {
		return env16;
	}

	public void setEnv16(String env16) {
		this.env16 = env16;
	}

	public String getEnv17() {
		return env17;
	}

	public void setEnv17(String env17) {
		this.env17 = env17;
	}

	public String getEnv18() {
		return env18;
	}

	public void setEnv18(String env18) {
		this.env18 = env18;
	}

	public String getEnv19() {
		return env19;
	}

	public void setEnv19(String env19) {
		this.env19 = env19;
	}

	public String getEnv20() {
		return env20;
	}

	public void setEnv20(String env20) {
		this.env20 = env20;
	}


	@JsonProperty	
	private String loc1;
	@JsonProperty	
	private String loc2;
	@JsonProperty	
	private String loc3;
	@JsonProperty	
	private String loc4;
	@JsonProperty	
	private String loc5;
	@JsonProperty	
	private String loc6;
	@JsonProperty	
	private String loc7;
	@JsonProperty	
	private String loc8;
	@JsonProperty	
	private String loc9;
	@JsonProperty	
	private String loc10;



	public String getLoc1() {
		return loc1;
	}

	public void setLoc1(String loc1) {
		this.loc1 = loc1;
	}

	public String getLoc2() {
		return loc2;
	}

	public void setLoc2(String loc2) {
		this.loc2 = loc2;
	}

	public String getLoc3() {
		return loc3;
	}

	public void setLoc3(String loc3) {
		this.loc3 = loc3;
	}

	public String getLoc4() {
		return loc4;
	}

	public void setLoc4(String loc4) {
		this.loc4 = loc4;
	}

	public String getLoc5() {
		return loc5;
	}

	public void setLoc5(String loc5) {
		this.loc5 = loc5;
	}

	public String getLoc6() {
		return loc6;
	}

	public void setLoc6(String loc6) {
		this.loc6 = loc6;
	}

	public String getLoc7() {
		return loc7;
	}

	public void setLoc7(String loc7) {
		this.loc7 = loc7;
	}

	public String getLoc8() {
		return loc8;
	}

	public void setLoc8(String loc8) {
		this.loc8 = loc8;
	}

	public String getLoc9() {
		return loc9;
	}

	public void setLoc9(String loc9) {
		this.loc9 = loc9;
	}

	public String getLoc10() {
		return loc10;
	}

	public void setLoc10(String loc10) {
		this.loc10 = loc10;
	}
	
	public int getDecouplingCategoryId() {
		return decouplingCategoryId;
	}

	public void setDecouplingCategoryId(int decouplingCategoryId) {
		this.decouplingCategoryId = decouplingCategoryId;
	}

	public String getDecouplingCategoryName() {
		return decouplingCategoryName;
	}

	public void setDecouplingCategoryName(String decouplingCategoryName) {
		this.decouplingCategoryName = decouplingCategoryName;
	}
}
