package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "risk_mitigation_master")
public class RiskMitigationMaster implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	@Column(name = "riskMitigationId")
	private Integer riskMitigationId;
	@Column(name = "rmCode")
	private String rmCode;
	@Column(name = "mitigationMeasure")
	private String mitigationMeasure;
	@Column(name = "projectRecord")
	private String projectRecord;
	@Column(name = "isAvailable")
	private Integer isAvailable;
	@Column(name = "mitigationDate")
	private Date mitigationDate;
	@Column(name = "testReport")
	private String testReport;
	@Column(name = "productId")
	private ProductMaster productMaster;
	@Column(name = "createdDate")
	private Date createdDate;
	@Column(name = "modifiedDate")
	private Date modifiedDate;
	@Column(name = "status")
	private Integer status;

	private Set<Risk> riskList = new HashSet<Risk>(0);

	public RiskMitigationMaster() {
	}

	@Id
	@Column(name = "riskMitigationId")
	@GeneratedValue(strategy = IDENTITY)
	public Integer getRiskMitigationId() {
		return riskMitigationId;
	}

	public void setRiskMitigationId(Integer riskMitigationId) {
		this.riskMitigationId = riskMitigationId;
	}

	public String getRmCode() {
		return rmCode;
	}

	public void setRmCode(String rmCode) {
		this.rmCode = rmCode;
	}

	public String getMitigationMeasure() {
		return mitigationMeasure;
	}

	public void setMitigationMeasure(String mitigationMeasure) {
		this.mitigationMeasure = mitigationMeasure;
	}

	public String getProjectRecord() {
		return projectRecord;
	}

	public void setProjectRecord(String projectRecord) {
		this.projectRecord = projectRecord;
	}

	public Integer getIsAvailable() {
		return isAvailable;
	}

	public void setIsAvailable(Integer isAvailable) {
		this.isAvailable = isAvailable;
	}

	public Date getMitigationDate() {
		return mitigationDate;
	}

	public void setMitigationDate(Date mitigationDate) {
		this.mitigationDate = mitigationDate;
	}

	public String getTestReport() {
		return testReport;
	}

	public void setTestReport(String testReport) {
		this.testReport = testReport;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "productId", nullable = false)
	public ProductMaster getProductMaster() {
		return productMaster;
	}

	public void setProductMaster(ProductMaster productMaster) {
		this.productMaster = productMaster;
	}	

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createdDate")
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modifiedDate")
	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "mitigationList",cascade=CascadeType.ALL)
	public Set<Risk> getRiskList() {
		return riskList;
	}

	public void setRiskList(Set<Risk> riskList) {
		this.riskList = riskList;
	}

}
