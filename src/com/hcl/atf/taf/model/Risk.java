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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "risk_list")
public class Risk implements java.io.Serializable{

	private static final long serialVersionUID = 1L;

	@Column(name = "riskId")
	private Integer riskId;
	@Column(name = "riskName")
	private String riskName;
	@Column(name = "description")
	private String description;
	@Column(name = "riskLabel")
	private String riskLabel;
	@Column(name = "createdDate")
	private Date createdDate;
	@Column(name = "modifiedDate")
	private Date modifiedDate;
	@Column(name = "userId")
	private UserList userList;
	@Column(name = "status")
	private Integer status;
	@Column(name = "productId")
	private ProductMaster productMaster;

	private Set<TestCaseList> testCaseList = new HashSet<TestCaseList>(0);
	private Set<ProductFeature> featureList = new HashSet<ProductFeature>(0);
	private Set<RiskMitigationMaster> mitigationList = new HashSet<RiskMitigationMaster>(0);
	private Set<RiskAssessment> assessmentList = new HashSet<RiskAssessment>(0);

	public Risk() {
	}

	@Id
	@Column(name = "riskId")
	@GeneratedValue(strategy = IDENTITY)
	public Integer getRiskId() {
		return riskId;
	}

	public void setRiskId(Integer riskId) {
		this.riskId = riskId;
	}

	public String getRiskName() {
		return riskName;
	}

	public void setRiskName(String riskName) {
		this.riskName = riskName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRiskLabel() {
		return riskLabel;
	}

	public void setRiskLabel(String riskLabel) {
		this.riskLabel = riskLabel;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId", nullable = false)
	public UserList getUserList() {
		return userList;
	}

	public void setUserList(UserList userList) {
		this.userList = userList;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "productId", nullable = false)
	public ProductMaster getProductMaster() {
		return productMaster;
	}

	public void setProductMaster(ProductMaster productMaster) {
		this.productMaster = productMaster;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "product_risk_has_test_case_list", joinColumns = { @JoinColumn(name = "productRiskId", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "testCaseId", nullable = false, updatable = false) })
	public Set<TestCaseList> getTestCaseList() {
		return testCaseList;
	}

	public void setTestCaseList(Set<TestCaseList> testCaseList) {
		this.testCaseList = testCaseList;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "product_risk_has_feature_list", joinColumns = { @JoinColumn(name = "productRiskId", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "featureId", nullable = false, updatable = false) })
	public Set<ProductFeature> getFeatureList() {
		return featureList;
	}

	public void setFeatureList(Set<ProductFeature> featureList) {
		this.featureList = featureList;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "product_risk_has_mitigation_list", joinColumns = { @JoinColumn(name = "productRiskId", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "riskMitigationId", nullable = false, updatable = false) })
	public Set<RiskMitigationMaster> getMitigationList() {
		return mitigationList;
	}

	public void setMitigationList(Set<RiskMitigationMaster> mitigationList) {
		this.mitigationList = mitigationList;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "risk",cascade=CascadeType.ALL)
	public Set<RiskAssessment> getAssessmentList() {
		return assessmentList;
	}

	public void setAssessmentList(Set<RiskAssessment> assessmentList) {
		this.assessmentList = assessmentList;
	}

}
