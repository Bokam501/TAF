package com.hcl.atf.taf.model.json;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.RiskRating;


public class JsonRiskRating implements java.io.Serializable {
	private static final Log log = LogFactory.getLog(JsonRiskRating.class);
	
	@JsonProperty
	private Integer riskRatingId;
	@JsonProperty
	private String ratingName;
	@JsonProperty
	private String ratingDescription;
	@JsonProperty
	private String colour;
	@JsonProperty
	private String createdDate;
	@JsonProperty
	private String modifiedDate;
	@JsonProperty
	private Integer status;
	@JsonProperty
	private Integer productId;
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

	public JsonRiskRating() {
	}

	public JsonRiskRating(RiskRating riskRating) {
		this.riskRatingId = riskRating.getRiskRatingId();
		this.ratingName = riskRating.getRatingName();
		this.ratingDescription = riskRating.getRatingDescription();
		this.colour = riskRating.getColour();
		if (riskRating.getCreatedDate() != null){
			this.createdDate = DateUtility.dateformatWithOutTime(riskRating.getCreatedDate());
		}
		if (riskRating.getModifiedDate() != null){
			this.modifiedDate = DateUtility.dateformatWithOutTime(riskRating.getModifiedDate());
		}
		this.status = riskRating.getStatus();
		
		if (riskRating.getProductMaster() != null){
			this.productId = riskRating.getProductMaster().getProductId();
		}
		
	}

	@JsonIgnore
	public RiskRating getRiskRatingList() {
		RiskRating riskRating = new RiskRating();
		riskRating.setRiskRatingId(this.riskRatingId);
		riskRating.setRatingName(this.ratingName);
		riskRating.setRatingDescription(this.ratingDescription);
		riskRating.setColour(this.colour);
		
		
		if (this.createdDate != null){
			riskRating.setCreatedDate(DateUtility.dateformatWithOutTime(this.createdDate));
		}else{
			riskRating.setCreatedDate(new Date());
		}
		riskRating.setModifiedDate(DateUtility.getCurrentTime());
		riskRating.setStatus(this.status);
		
		ProductMaster productMaster = new ProductMaster();
		productMaster.setProductId(this.productId);
		riskRating.setProductMaster(productMaster);
		
		return riskRating;
	}

	public Integer getRiskRatingId() {
		return riskRatingId;
	}

	public void setRiskRatingId(Integer riskRatingId) {
		this.riskRatingId = riskRatingId;
	}

	public String getRatingName() {
		return ratingName;
	}

	public void setRatingName(String ratingName) {
		this.ratingName = ratingName;
	}

	public String getRatingDescription() {
		return ratingDescription;
	}

	public void setRatingDescription(String ratingDescription) {
		this.ratingDescription = ratingDescription;
	}

	public String getColour() {
		return colour;
	}

	public void setColour(String colour) {
		this.colour = colour;
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

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
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
