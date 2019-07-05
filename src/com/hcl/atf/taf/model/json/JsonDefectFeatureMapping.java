package com.hcl.atf.taf.model.json;



import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.DefectFeatureMapping;
import com.hcl.atf.taf.model.ProductFeature;
import com.hcl.atf.taf.model.TestExecutionResultBugList;
import com.hcl.atf.taf.model.UserList;


public class JsonDefectFeatureMapping implements java.io.Serializable {

	@JsonProperty
	private Integer defectFeatureMappingId;	
	@JsonProperty
	private Integer testExecutionResultBugId;
	@JsonProperty
	private Integer productFeatureId;	
	@JsonProperty
	private Integer explicitMapping;	
	@JsonProperty
	private Integer analyticsMappingConfidence;
	@JsonProperty	
	private Integer analyticsMappingConfirmation;
	@JsonProperty
	private Integer userId;	
	@JsonProperty
	private String dateOfConfrimation;	

	public JsonDefectFeatureMapping() {
	}

	public JsonDefectFeatureMapping(DefectFeatureMapping defectFeatureMapping) {
		this.defectFeatureMappingId = defectFeatureMapping.getDefectFeatureMappingId();		
		this.explicitMapping = defectFeatureMapping.getExplicitMapping();	
		this.analyticsMappingConfidence = defectFeatureMapping.getAnalyticsMappingConfidence();
		this.analyticsMappingConfirmation = defectFeatureMapping.getAnalyticsMappingConfirmation();
		
		if(defectFeatureMapping.getTestExecutionResultBugList() != null){
			this.testExecutionResultBugId = defectFeatureMapping.getTestExecutionResultBugList().getTestExecutionResultBugId();	
		}
		if(defectFeatureMapping.getFeature() != null){
			this.productFeatureId = defectFeatureMapping.getFeature().getProductFeatureId();	
		}
		if(defectFeatureMapping.getUser() != null){
			this.userId = defectFeatureMapping.getUser().getUserId();
		}		

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(defectFeatureMapping.getDateOfConfrimation()!=null){				
		this.dateOfConfrimation = sdf.format(defectFeatureMapping.getDateOfConfrimation());
		}
	}

	@JsonIgnore
	public DefectFeatureMapping getDefectFeatureMapping(){
		DefectFeatureMapping defectFeatureMapping = new DefectFeatureMapping();
		defectFeatureMapping.setDefectFeatureMappingId(defectFeatureMappingId);		
		defectFeatureMapping.setExplicitMapping(explicitMapping);
		defectFeatureMapping.setAnalyticsMappingConfidence(analyticsMappingConfidence);
		defectFeatureMapping.setAnalyticsMappingConfirmation(analyticsMappingConfirmation);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			if(!(dateOfConfrimation==null || dateOfConfrimation.trim() == ""))
				defectFeatureMapping.setDateOfConfrimation(sdf.parse(dateOfConfrimation));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		
		if(testExecutionResultBugId != null){
			TestExecutionResultBugList testExecutionResultBugList = new TestExecutionResultBugList();
			testExecutionResultBugList.setTestExecutionResultBugId(testExecutionResultBugId);
			defectFeatureMapping.setTestExecutionResultBugList(testExecutionResultBugList);
		}
		
		if(productFeatureId != null){
			ProductFeature productFeature = new ProductFeature();
			productFeature.setProductFeatureId(productFeatureId);
			defectFeatureMapping.setFeature(productFeature);
		}
		
		if(userId != null){
			UserList userList = new UserList();
			userList.setUserId(userId);
			defectFeatureMapping.setUser(userList);
		}		
		
		return defectFeatureMapping;		
	}

	public Integer getDefectFeatureMappingId() {
		return defectFeatureMappingId;
	}

	public void setDefectFeatureMappingId(Integer defectFeatureMappingId) {
		this.defectFeatureMappingId = defectFeatureMappingId;
	}

	public Integer getTestExecutionResultBugId() {
		return testExecutionResultBugId;
	}

	public void setTestExecutionResultBugId(Integer testExecutionResultBugId) {
		this.testExecutionResultBugId = testExecutionResultBugId;
	}

	public Integer getProductFeatureId() {
		return productFeatureId;
	}

	public void setProductFeatureId(Integer productFeatureId) {
		this.productFeatureId = productFeatureId;
	}

	public Integer getExplicitMapping() {
		return explicitMapping;
	}

	public void setExplicitMapping(Integer explicitMapping) {
		this.explicitMapping = explicitMapping;
	}

	public Integer getAnalyticsMappingConfidence() {
		return analyticsMappingConfidence;
	}

	public void setAnalyticsMappingConfidence(Integer analyticsMappingConfidence) {
		this.analyticsMappingConfidence = analyticsMappingConfidence;
	}

	public Integer getAnalyticsMappingConfirmation() {
		return analyticsMappingConfirmation;
	}

	public void setAnalyticsMappingConfirmation(Integer analyticsMappingConfirmation) {
		this.analyticsMappingConfirmation = analyticsMappingConfirmation;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getDateOfConfrimation() {
		return dateOfConfrimation;
	}

	public void setDateOfConfrimation(String dateOfConfrimation) {
		this.dateOfConfrimation = dateOfConfrimation;
	}
}
