package com.hcl.atf.taf.model.json;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.LifeCyclePhase;
import com.hcl.atf.taf.model.Risk;
import com.hcl.atf.taf.model.RiskAssessment;
import com.hcl.atf.taf.model.RiskLikeHoodMaster;
import com.hcl.atf.taf.model.RiskRating;
import com.hcl.atf.taf.model.RiskSeverityMaster;
import com.hcl.atf.taf.model.UserList;

public class JsonRiskAssessment implements java.io.Serializable {
	private static final Log log = LogFactory.getLog(JsonRiskAssessment.class);
	@JsonProperty
	private Integer riskAssessmentId;
	@JsonProperty
	private Integer mitigationType;
	@JsonProperty
	private Integer riskRatingId;
	@JsonProperty
	private Integer userId;
	@JsonProperty
	private Integer lifeCyclePhaseId;
	@JsonProperty
	private Integer riskSeverityId;
	@JsonProperty
	private Integer riskLikeHoodId;
	@JsonProperty
	private Integer riskId;
	@JsonProperty
	private String assessmentDate;
	@JsonProperty
	private String modifiedDate;
	@JsonProperty
	private Integer status;
	@JsonProperty
	private String lifeCyclePhaseName;

	public JsonRiskAssessment() {
	}

	public JsonRiskAssessment(RiskAssessment riskAssessment) {
		this.riskAssessmentId = riskAssessment.getRiskAssessmentId();
		this.mitigationType = riskAssessment.getMitigationType();

		if (riskAssessment.getRiskRating() != null){
			this.riskRatingId = riskAssessment.getRiskRating().getRiskRatingId();
		}
		if (riskAssessment.getUserList() != null){
			this.userId = riskAssessment.getUserList().getUserId();
		}
		if (riskAssessment.getLifeCyclePhase() != null){
			this.lifeCyclePhaseId = riskAssessment.getLifeCyclePhase().getLifeCyclePhaseId();
			this.lifeCyclePhaseName = riskAssessment.getLifeCyclePhase().getName();
		}
		if (riskAssessment.getRiskSeverity() != null){
			this.riskSeverityId = riskAssessment.getRiskSeverity().getRiskSeverityId();
		}
		if (riskAssessment.getRiskLikeHood() != null){
			this.riskLikeHoodId = riskAssessment.getRiskLikeHood().getRiskLikeHoodId();
		}
		if (riskAssessment.getRisk() != null){
			this.riskId = riskAssessment.getRisk().getRiskId();
		}
		if (riskAssessment.getAssessmentDate() != null){
			this.assessmentDate = DateUtility.dateformatWithOutTime(riskAssessment.getAssessmentDate());
		}
		if (riskAssessment.getModifiedDate() != null){
			this.modifiedDate = DateUtility.dateformatWithOutTime(riskAssessment.getModifiedDate());
		}
		this.status = riskAssessment.getStatus();


	}

	@JsonIgnore
	public RiskAssessment getRiskAssessmentList() {
		RiskAssessment riskAssessment = new RiskAssessment();
		riskAssessment.setRiskAssessmentId(this.riskAssessmentId);
		riskAssessment.setMitigationType(this.mitigationType);

		RiskRating riskRating = new RiskRating();
		riskRating.setRiskRatingId(this.riskRatingId);
		riskAssessment.setRiskRating(riskRating);

		UserList userList = new UserList();
		userList.setUserId(this.userId);
		riskAssessment.setUserList(userList);

		LifeCyclePhase lifeCyclePhase = new LifeCyclePhase();
		lifeCyclePhase.setLifeCyclePhaseId(this.lifeCyclePhaseId);
		lifeCyclePhase.setName(this.lifeCyclePhaseName);
		riskAssessment.setLifeCyclePhase(lifeCyclePhase);

		RiskSeverityMaster riskSeverity = new RiskSeverityMaster();
		riskSeverity.setRiskSeverityId(this.riskSeverityId);
		riskAssessment.setRiskSeverity(riskSeverity);

		RiskLikeHoodMaster riskLikeHood = new RiskLikeHoodMaster();
		riskLikeHood.setRiskLikeHoodId(this.riskLikeHoodId);
		riskAssessment.setRiskLikeHood(riskLikeHood);

		Risk risk = new Risk();
		risk.setRiskId(this.riskId);
		riskAssessment.setRisk(risk);

		if (this.assessmentDate != null){
			riskAssessment.setAssessmentDate(DateUtility.dateformatWithOutTime(this.assessmentDate));
		}else{
			riskAssessment.setAssessmentDate(new Date());
		}

		riskAssessment.setModifiedDate(DateUtility.getCurrentTime());
		riskAssessment.setStatus(this.status);


		return riskAssessment;
	}

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

	public Integer getRiskRatingId() {
		return riskRatingId;
	}

	public void setRiskRatingId(Integer riskRatingId) {
		this.riskRatingId = riskRatingId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getLifeCyclePhaseId() {
		return lifeCyclePhaseId;
	}

	public void setLifeCyclePhaseId(Integer lifeCyclePhaseId) {
		this.lifeCyclePhaseId = lifeCyclePhaseId;
	}

	public Integer getRiskSeverityId() {
		return riskSeverityId;
	}

	public void setRiskSeverityId(Integer riskSeverityId) {
		this.riskSeverityId = riskSeverityId;
	}

	public Integer getRiskLikeHoodId() {
		return riskLikeHoodId;
	}

	public void setRiskLikeHoodId(Integer riskLikeHoodId) {
		this.riskLikeHoodId = riskLikeHoodId;
	}

	public Integer getRiskId() {
		return riskId;
	}

	public void setRiskId(Integer riskId) {
		this.riskId = riskId;
	}

	public String getAssessmentDate() {
		return assessmentDate;
	}

	public void setAssessmentDate(String assessmentDate) {
		this.assessmentDate = assessmentDate;
	}

	public String getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getLifeCyclePhaseName() {
		return lifeCyclePhaseName;
	}

	public void setLifeCyclePhaseName(String lifeCyclePhaseName) {
		this.lifeCyclePhaseName = lifeCyclePhaseName;
	}

}
