package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "risk_severity_master")
public class RiskSeverityMaster implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	@Column(name = "riskSeverityId")
	private Integer riskSeverityId;
	@Column(name = "severityName")
	private String severityName;
	@Column(name = "severityRating")
	private String severityRating;
	@Column(name = "expectedEvents")
	private String expectedEvents;
	@Column(name = "productId")
	private ProductMaster productMaster;
	@Column(name = "createdDate")
	private Date createdDate;
	@Column(name = "modifiedDate")
	private Date modifiedDate;
	@Column(name = "status")
	private Integer status;
	private Set<RiskAssessment> riskAssessment;

	public RiskSeverityMaster() {
	}

	@Id
	@Column(name = "riskSeverityId")
	@GeneratedValue(strategy = IDENTITY)
	public Integer getRiskSeverityId() {
		return riskSeverityId;
	}

	public void setRiskSeverityId(Integer riskSeverityId) {
		this.riskSeverityId = riskSeverityId;
	}

	public String getSeverityName() {
		return severityName;
	}

	public void setSeverityName(String severityName) {
		this.severityName = severityName;
	}

	public String getSeverityRating() {
		return severityRating;
	}

	public void setSeverityRating(String severityRating) {
		this.severityRating = severityRating;
	}

	public String getExpectedEvents() {
		return expectedEvents;
	}

	public void setExpectedEvents(String expectedEvents) {
		this.expectedEvents = expectedEvents;
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

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "riskSeverity",cascade=CascadeType.ALL)
	public Set<RiskAssessment> getRiskAssessment() {
		return riskAssessment;
	}

	public void setRiskAssessment(Set<RiskAssessment> riskAssessment) {
		this.riskAssessment = riskAssessment;
	}

}
