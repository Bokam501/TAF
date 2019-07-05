package com.hcl.atf.taf.model.json;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.RiskSeverityMaster;


public class JsonRiskSeverity implements java.io.Serializable {
	private static final Log log = LogFactory.getLog(JsonRiskSeverity.class);
	@JsonProperty
	private Integer riskSeverityId;
	@JsonProperty
	private String severityName;
	@JsonProperty
	private String severityRating;
	@JsonProperty
	private String expectedEvents;
	@JsonProperty
	private Integer productId;
	@JsonProperty
	private String createdDate;
	@JsonProperty
	private String modifiedDate;
	@JsonProperty
	private Integer status;
	@JsonProperty
	private String modifiedField;
	@JsonProperty
	private String 	modifiedFieldTitle;	
	@JsonProperty
	private String oldFieldID;
	@JsonProperty
	private String	oldFieldValue;
	@JsonProperty
	private String	modifiedFieldID;
	@JsonProperty	
	private String modifiedFieldValue;


	public JsonRiskSeverity() {
	}

	public JsonRiskSeverity(RiskSeverityMaster riskSeverityMaster) {
		this.riskSeverityId = riskSeverityMaster.getRiskSeverityId();
		this.severityName = riskSeverityMaster.getSeverityName();
		this.severityRating =  riskSeverityMaster.getSeverityRating();
		this.expectedEvents = riskSeverityMaster.getExpectedEvents();
		if (riskSeverityMaster.getProductMaster() != null){
			this.productId = riskSeverityMaster.getProductMaster().getProductId();
		}
		if (riskSeverityMaster.getCreatedDate() != null){
			this.createdDate = DateUtility.dateformatWithOutTime(riskSeverityMaster.getCreatedDate());
		}
		if (riskSeverityMaster.getModifiedDate() != null){
			this.modifiedDate = DateUtility.dateformatWithOutTime(riskSeverityMaster.getModifiedDate());
		}
		
		this.status = riskSeverityMaster.getStatus();

	}

	@JsonIgnore
	public RiskSeverityMaster getRiskSeverityList() {
		RiskSeverityMaster riskSeverityMaster = new RiskSeverityMaster();
		riskSeverityMaster.setRiskSeverityId(this.riskSeverityId);
		riskSeverityMaster.setSeverityName(this.severityName);
		riskSeverityMaster.setSeverityRating(this.severityRating);
		riskSeverityMaster.setExpectedEvents(this.expectedEvents);

		ProductMaster productMaster = new ProductMaster();
		productMaster.setProductId(this.productId);
		riskSeverityMaster.setProductMaster(productMaster);

		if (this.createdDate != null){
			riskSeverityMaster.setCreatedDate(DateUtility.dateformatWithOutTime(this.createdDate));
		}else{
			riskSeverityMaster.setCreatedDate(new Date());
		}
		riskSeverityMaster.setModifiedDate(DateUtility.getCurrentTime());
		riskSeverityMaster.setStatus(this.status);

		return riskSeverityMaster;
	}

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

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
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

	public String getModifiedField() {
		return modifiedField;
	}

	public void setModifiedField(String modifiedField) {
		this.modifiedField = modifiedField;
	}

	public String getModifiedFieldTitle() {
		return modifiedFieldTitle;
	}

	public void setModifiedFieldTitle(String modifiedFieldTitle) {
		this.modifiedFieldTitle = modifiedFieldTitle;
	}

	public String getOldFieldID() {
		return oldFieldID;
	}

	public void setOldFieldID(String oldFieldID) {
		this.oldFieldID = oldFieldID;
	}

	public String getOldFieldValue() {
		return oldFieldValue;
	}

	public void setOldFieldValue(String oldFieldValue) {
		this.oldFieldValue = oldFieldValue;
	}

	public String getModifiedFieldID() {
		return modifiedFieldID;
	}

	public void setModifiedFieldID(String modifiedFieldID) {
		this.modifiedFieldID = modifiedFieldID;
	}

	public String getModifiedFieldValue() {
		return modifiedFieldValue;
	}

	public void setModifiedFieldValue(String modifiedFieldValue) {
		this.modifiedFieldValue = modifiedFieldValue;
	}


}
