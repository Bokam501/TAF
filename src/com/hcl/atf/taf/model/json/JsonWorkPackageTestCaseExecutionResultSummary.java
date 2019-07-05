package com.hcl.atf.taf.model.json;


import org.json.simple.JSONObject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.model.dto.WorkPackageTCEPResultSummaryDTO;


public class JsonWorkPackageTestCaseExecutionResultSummary implements java.io.Serializable{
	@JsonProperty
	private Integer wptcepId;
	@JsonProperty
	private Integer workPackageId;
	@JsonProperty
	private String workPackageName;	
	@JsonProperty
	private Integer productVersionId;
	@JsonProperty
	private String productVersionName;
	@JsonProperty
	private Integer productBuildId;
	@JsonProperty
	private String productBuildName;
	@JsonProperty
	private String result;	
	@JsonProperty	
	private Integer isExecuted;	
	@JsonProperty
	private Integer totalExecutedTesCases;
	@JsonProperty
	private Integer notExecuted;
	@JsonProperty
	private Integer totalPass;
	@JsonProperty
	private Integer totalFail;
	@JsonProperty
	private Integer totalNoRun;
	@JsonProperty
	private Integer totalBlock;
	

	public JsonWorkPackageTestCaseExecutionResultSummary() {
	}	

	public JsonWorkPackageTestCaseExecutionResultSummary(WorkPackageTCEPResultSummaryDTO workPackageTCEPResultSummaryDTO) {
		
		this.wptcepId = workPackageTCEPResultSummaryDTO.getWptcepId();
		this.workPackageId = workPackageTCEPResultSummaryDTO.getWorkPackageId();
		this.workPackageName = 	workPackageTCEPResultSummaryDTO.getWorkPackageName();
		this.productVersionId = workPackageTCEPResultSummaryDTO.getProductVersionId();
		this.productVersionName = workPackageTCEPResultSummaryDTO.getProductVersionName();
		this.productBuildId = workPackageTCEPResultSummaryDTO.getProductBuildId();
		this.productBuildName = workPackageTCEPResultSummaryDTO.getProductBuildName();
		
		this.isExecuted = workPackageTCEPResultSummaryDTO.getIsExecuted().intValue();	
		
		int passed=0;
		int failed=0;
		int notrun=0;
		int blocked=0;
		this.result = workPackageTCEPResultSummaryDTO.getResult();
		if(result.equals("1") || result.equals("PASS") || result.equals(IDPAConstants.EXECUTION_RESULT_PASSED)){
			++passed;
		}else if(result.equals("2") || result.equals("FAIL") || result.equals(IDPAConstants.EXECUTION_RESULT_FAILED)){
			++failed;
		}else if(result.equals("3")  || result.equals(IDPAConstants.EXECUTION_RESULT_NORUN)){
			++notrun;
		}else if(result.equals("4") || result.equals(IDPAConstants.EXECUTION_RESULT_BLOCKED)){
			++blocked;
		}		
		this.totalPass = passed;
		this.totalFail = failed;
		this.totalNoRun = notrun;
		this.totalBlock = blocked;
		this.totalExecutedTesCases = totalPass+totalFail+totalNoRun+totalBlock;
	}	
	@JsonIgnore
	public JSONObject getCleanJson() {
		
		JSONObject responseJson = new JSONObject();
		try {
			responseJson.put("productVersionName", productVersionName);
			responseJson.put("productBuildName", productBuildName);
			responseJson.put("result", result);
			responseJson.put("totalPass",totalPass );
			responseJson.put("totalFail", totalFail);
			responseJson.put("totalNoRun", totalNoRun);
			responseJson.put("totalBlock", totalBlock);			
		}catch(Exception e) {
			
		}
		return responseJson;
	}


	public Integer getWptcepId() {
		return wptcepId;
	}

	public void setWptcepId(Integer wptcepId) {
		this.wptcepId = wptcepId;
	}

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

	public Integer getProductVersionId() {
		return productVersionId;
	}

	public void setProductVersionId(Integer productVersionId) {
		this.productVersionId = productVersionId;
	}

	public String getProductVersionName() {
		return productVersionName;
	}

	public void setProductVersionName(String productVersionName) {
		this.productVersionName = productVersionName;
	}

	public Integer getProductBuildId() {
		return productBuildId;
	}

	public void setProductBuildId(Integer productBuildId) {
		this.productBuildId = productBuildId;
	}

	public String getProductBuildName() {
		return productBuildName;
	}

	public void setProductBuildName(String productBuildName) {
		this.productBuildName = productBuildName;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public Integer getIsExecuted() {
		return isExecuted;
	}

	public void setIsExecuted(Integer isExecuted) {
		this.isExecuted = isExecuted;
	}

	public Integer getTotalExecutedTesCases() {
		return totalExecutedTesCases;
	}

	public void setTotalExecutedTesCases(Integer totalExecutedTesCases) {
		this.totalExecutedTesCases = totalExecutedTesCases;
	}

	public Integer getNotExecuted() {
		return notExecuted;
	}

	public void setNotExecuted(Integer notExecuted) {
		this.notExecuted = notExecuted;
	}

	public Integer getTotalPass() {
		return totalPass;
	}

	public void setTotalPass(Integer totalPass) {
		this.totalPass = totalPass;
	}

	public Integer getTotalFail() {
		return totalFail;
	}

	public void setTotalFail(Integer totalFail) {
		this.totalFail = totalFail;
	}

	public Integer getTotalNoRun() {
		return totalNoRun;
	}

	public void setTotalNoRun(Integer totalNoRun) {
		this.totalNoRun = totalNoRun;
	}

	public Integer getTotalBlock() {
		return totalBlock;
	}

	public void setTotalBlock(Integer totalBlock) {
		this.totalBlock = totalBlock;
	}
	
}
