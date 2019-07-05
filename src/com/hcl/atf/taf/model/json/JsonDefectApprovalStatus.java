package com.hcl.atf.taf.model.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.DefectApprovalStatusMaster;

public class JsonDefectApprovalStatus implements java.io.Serializable {
	
	@JsonProperty
	private Integer approvalStatusId;
	
	@JsonProperty	
	private String approvalStatusName;
	
	@JsonProperty	
	private String approvalStatusDescription;

	
	public JsonDefectApprovalStatus(){
		
	}
	
	public JsonDefectApprovalStatus(DefectApprovalStatusMaster defectApprovalStatus) {
		approvalStatusId = defectApprovalStatus.getApprovalStatusId();
		approvalStatusName = defectApprovalStatus.getApprovalStatusName();
		approvalStatusDescription = defectApprovalStatus.getApprovalStatusDescription();
	}
	
	@JsonIgnore
	public DefectApprovalStatusMaster getDefectApprovalStatus() {
		DefectApprovalStatusMaster defectApprovalStatus = new DefectApprovalStatusMaster();
		if (approvalStatusId != null) {
			defectApprovalStatus.setApprovalStatusId(approvalStatusId);
		}
		if (approvalStatusName != null) {
			defectApprovalStatus.setApprovalStatusName(approvalStatusName);
		}
		if (approvalStatusDescription != null) {
			defectApprovalStatus.setApprovalStatusDescription(approvalStatusDescription);
		}
		return defectApprovalStatus;
	}

	public Integer getApprovalStatusId() {
		return approvalStatusId;
	}

	public void setApprovalStatusId(Integer approvalStatusId) {
		this.approvalStatusId = approvalStatusId;
	}

	public String getApprovalStatusName() {
		return approvalStatusName;
	}

	public void setApprovalStatusName(String approvalStatusName) {
		this.approvalStatusName = approvalStatusName;
	}

	public String getApprovalStatusDescription() {
		return approvalStatusDescription;
	}

	public void setApprovalStatusDescription(String approvalStatusDescription) {
		this.approvalStatusDescription = approvalStatusDescription;
	}


}
