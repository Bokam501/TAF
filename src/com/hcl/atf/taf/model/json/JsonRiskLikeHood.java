package com.hcl.atf.taf.model.json;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.RiskLikeHoodMaster;

public class JsonRiskLikeHood implements java.io.Serializable {
	private static final Log log = LogFactory.getLog(JsonRiskLikeHood.class);
	@JsonProperty
	private Integer riskLikeHoodId;
	@JsonProperty
	private String likeHoodName;
	@JsonProperty
	private String likeHoodRating;
	@JsonProperty
	private String expectedFrequency;
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

	public JsonRiskLikeHood() {
	}

	public JsonRiskLikeHood(RiskLikeHoodMaster riskLikeHoodMaster) {
		this.riskLikeHoodId = riskLikeHoodMaster.getRiskLikeHoodId();
		this.likeHoodName = riskLikeHoodMaster.getLikeHoodName();
		this.likeHoodRating =  riskLikeHoodMaster.getLikeHoodRating();
		this.expectedFrequency = riskLikeHoodMaster.getExpectedFrequency();
		if (riskLikeHoodMaster.getProductMaster() != null){
			this.productId = riskLikeHoodMaster.getProductMaster().getProductId();
		}
		if (riskLikeHoodMaster.getCreatedDate() != null){
			this.createdDate = DateUtility.dateformatWithOutTime(riskLikeHoodMaster.getCreatedDate());
		}
		if (riskLikeHoodMaster.getModifiedDate() != null){
			this.modifiedDate = DateUtility.dateformatWithOutTime(riskLikeHoodMaster.getModifiedDate());
		}
		this.status = riskLikeHoodMaster.getStatus();

	}

	@JsonIgnore
	public RiskLikeHoodMaster getRiskLikeHoodList() {
		RiskLikeHoodMaster riskLikeHoodMaster = new RiskLikeHoodMaster();
		riskLikeHoodMaster.setRiskLikeHoodId(this.riskLikeHoodId);
		riskLikeHoodMaster.setLikeHoodName(this.likeHoodName);
		riskLikeHoodMaster.setLikeHoodRating(this.likeHoodRating);
		riskLikeHoodMaster.setExpectedFrequency(this.expectedFrequency);

		ProductMaster productMaster = new ProductMaster();
		productMaster.setProductId(this.productId);
		riskLikeHoodMaster.setProductMaster(productMaster);

		if (this.createdDate != null){
			riskLikeHoodMaster.setCreatedDate(DateUtility.dateformatWithOutTime(this.createdDate));
		}else{
			riskLikeHoodMaster.setCreatedDate(new Date());
		}
		riskLikeHoodMaster.setModifiedDate(DateUtility.getCurrentTime());
		riskLikeHoodMaster.setStatus(this.status);

		return riskLikeHoodMaster;
	}

	public Integer getRiskLikeHoodId() {
		return riskLikeHoodId;
	}

	public void setRiskLikeHoodId(Integer riskLikeHoodId) {
		this.riskLikeHoodId = riskLikeHoodId;
	}

	public String getLikeHoodName() {
		return likeHoodName;
	}

	public void setLikeHoodName(String likeHoodName) {
		this.likeHoodName = likeHoodName;
	}

	public String getLikeHoodRating() {
		return likeHoodRating;
	}

	public void setLikeHoodRating(String likeHoodRating) {
		this.likeHoodRating = likeHoodRating;
	}

	public String getExpectedFrequency() {
		return expectedFrequency;
	}

	public void setExpectedFrequency(String expectedFrequency) {
		this.expectedFrequency = expectedFrequency;
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
