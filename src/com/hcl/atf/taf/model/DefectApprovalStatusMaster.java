package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "defect_approval_status_master")
public class DefectApprovalStatusMaster implements java.io.Serializable{
	private Integer approvalStatusId;
	private String approvalStatusName;
	private String approvalStatusDescription;
	
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "approvalStatusId", unique = true, nullable = false)
	public Integer getApprovalStatusId() {
		return approvalStatusId;
	}
	public void setApprovalStatusId(Integer approvalStatusId) {
		this.approvalStatusId = approvalStatusId;
	}
	
	@Column(name = "approvalStatusName")
	public String getApprovalStatusName() {
		return approvalStatusName;
	}
	public void setApprovalStatusName(String approvalStatusName) {
		this.approvalStatusName = approvalStatusName;
	}
	
	@Column(name = "approvalStatusDescription")
	public String getApprovalStatusDescription() {
		return approvalStatusDescription;
	}
	public void setApprovalStatusDescription(String approvalStatusDescription) {
		this.approvalStatusDescription = approvalStatusDescription;
	}
	
}
