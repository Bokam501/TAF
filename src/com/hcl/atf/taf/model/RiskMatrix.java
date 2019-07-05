package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "risk_matrix")
public class RiskMatrix implements java.io.Serializable{

	private static final long serialVersionUID = 1L;

	@Column(name = "riskMatrixId")
	private Integer riskMatrixId;
	@Column(name = "riskSeverityId")
	private RiskSeverityMaster riskSeverity;
	@Column(name = "riskLikeHoodId")
	private RiskLikeHoodMaster riskLikeHood;
	@Column(name = "riskRatingId")
	private RiskRating riskRating;

	public RiskMatrix() {
	}

	@Id
	@Column(name = "riskMatrixId")
	@GeneratedValue(strategy = IDENTITY)
	public Integer getRiskMatrixId() {
		return riskMatrixId;
	}

	public void setRiskMatrixId(Integer riskMatrixId) {
		this.riskMatrixId = riskMatrixId;
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
	@JoinColumn(name = "riskRatingId", nullable = false)
	public RiskRating getRiskRating() {
		return riskRating;
	}

	public void setRiskRating(RiskRating riskRating) {
		this.riskRating = riskRating;
	}

}
