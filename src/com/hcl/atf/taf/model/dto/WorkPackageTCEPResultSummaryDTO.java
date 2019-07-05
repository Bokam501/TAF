package com.hcl.atf.taf.model.dto;


public class WorkPackageTCEPResultSummaryDTO {	
	
	private Integer wptcepId;
	
	private Integer workPackageId;
	
	private String workPackageName;	
	
	private Integer productVersionId;
	
	private String productVersionName;
		
	private Integer productBuildId;
	
	private String productBuildName;
	
	private String result;	
		
	private Short isExecuted;
	private Integer totalExecutedTesCases;
	private Integer notExecuted;
	private Integer totalPass;
	private Integer totalFail;
	private Integer totalNoRun;
	private Integer totalBlock;
	private Integer totalResultCount;
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

	public Short getIsExecuted() {
		return isExecuted;
	}

	public void setIsExecuted(Short isExecuted) {
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

	public Integer getTotalResultCount() {
		return totalResultCount;
	}

	public void setTotalResultCount(Integer totalResultCount) {
		this.totalResultCount = totalResultCount;
	}	
}
