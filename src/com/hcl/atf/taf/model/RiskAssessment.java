package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "risk_assessment")
public class RiskAssessment implements java.io.Serializable{

	private static final long serialVersionUID = 1L;

	@Column(name = "riskAssessmentId")
	private Integer riskAssessmentId;
	@Column(name = "mitigationType")
	private Integer mitigationType;
	@Column(name = "riskRatingId")
	private RiskRating riskRating;
	@Column(name = "userId")
	private UserList userList;
	@Column(name = "lifeCyclePhaseId")
	private LifeCyclePhase lifeCyclePhase;
	@Column(name = "riskSeverityId")
	private RiskSeverityMaster riskSeverity;
	@Column(name = "riskLikeHoodId")
	private RiskLikeHoodMaster riskLikeHood;
	@Column(name = "riskId")
	private Risk risk;
	@Column(name = "assessmentDate")
	private Date assessmentDate;
	@Column(name = "modifiedDate")
	private Date modifiedDate;
	@Column(name = "status")
	private Integer status;

	public RiskAssessment() {
	}

	@Id
	@Column(name = "riskAssessmentId")
	@GeneratedValue(strategy = IDENTITY)
	public Integer getRiskAssessmentId() {
		return riskAssessmentId;
	}

	public void setRiskAssessmentId(Integer riskAssessmentId) {
		this.riskAssessmentId = riskAssessmentId;
	}

	public Integer getMitigationType() {
		return mitigationType;
	}

	public void setMitigationType(Integer mitigationType) {
		this.mitigationType = mitigationType;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "riskRatingId", nullable = false)
	public RiskRating getRiskRating() {
		return riskRating;
	}

	public void setRiskRating(RiskRating riskRating) {
		this.riskRating = riskRating;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId", nullable = false)
	public UserList getUserList() {
		return userList;
	}

	public void setUserList(UserList userList) {
		this.userList = userList;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lifeCyclePhaseId", nullable = false)
	public LifeCyclePhase getLifeCyclePhase() {
		return lifeCyclePhase;
	}

	public void setLifeCyclePhase(LifeCyclePhase lifeCyclePhase) {
		this.lifeCyclePhase = lifeCyclePhase;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "riskSeverityId", nullable = false)
	public RiskSeverityMaster getRiskSeverity() {
		return riskSeverity;
	}

	public void setRiskSeverity(RiskSeverityMaster riskSeverity) {
		this.riskSeverity = riskSeverity;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "riskLikeHoodId", nullable = false)
	public RiskLikeHoodMaster getRiskLikeHood() {
		return riskLikeHood;
	}

	public void setRiskLikeHood(RiskLikeHoodMaster riskLikeHood) {
		this.riskLikeHood = riskLikeHood;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "riskId", nullable = false)
	public Risk getRisk() {
		return risk;
	}

	public void setRisk(Risk risk) {
		this.risk = risk;
	}	

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "assessmentDate")
	public Date getAssessmentDate() {
		return assessmentDate;
	}

	public void setAssessmentDate(Date assessmentDate) {
		this.assessmentDate = assessmentDate;
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

}
