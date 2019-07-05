package com.hcl.atf.taf.model.json;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.Risk;
import com.hcl.atf.taf.model.UserList;


public class JsonRisk implements java.io.Serializable {
	private static final Log log = LogFactory.getLog(JsonRisk.class);
	@JsonProperty
	private Integer riskId;
	@JsonProperty
	private String riskName;
	@JsonProperty
	private String description;
	@JsonProperty
	private String riskLabel;
	@JsonProperty
	private String createdDate;
	@JsonProperty
	private String modifiedDate;
	@JsonProperty
	private Integer userId;
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

	public JsonRisk() {
	}

	public JsonRisk(Risk risk) {
		this.riskId = risk.getRiskId();
		this.riskName = risk.getRiskName();
		this.description =  risk.getDescription();
		this.riskLabel = risk.getRiskLabel();
		if (risk.getCreatedDate() != null){
			this.createdDate = DateUtility.dateformatWithOutTime(risk.getCreatedDate());
		}
		if (risk.getModifiedDate() != null){
			this.modifiedDate = DateUtility.dateformatWithOutTime(risk.getModifiedDate());
		}
		if (risk.getUserList() != null){
			this.userId = risk.getUserList().getUserId();
		}
		this.status = risk.getStatus();
		if (risk.getProductMaster() != null){
			this.productId = risk.getProductMaster().getProductId();
		}

	}

	@JsonIgnore
	public Risk getRiskList() {
		Risk risk = new Risk();
		risk.setRiskId(this.riskId);
		risk.setRiskName(this.riskName);
		risk.setDescription(this.description);
		risk.setRiskLabel(this.riskLabel);
		if (this.createdDate != null){
			risk.setCreatedDate(DateUtility.dateformatWithOutTime(this.createdDate));
		}else{
			risk.setCreatedDate(new Date());
		}
		risk.setModifiedDate(DateUtility.getCurrentTime());

		UserList userList = new UserList();
		userList.setUserId(this.userId);
		risk.setUserList(userList);	

		risk.setStatus(this.status);

		ProductMaster productMaster = new ProductMaster();
		productMaster.setProductId(this.productId);
		risk.setProductMaster(productMaster);

		return risk;
	}

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

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
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
