package com.hcl.atf.taf.model.json;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.RiskMitigationMaster;

public class JsonRiskMitigation implements java.io.Serializable {
	private static final Log log = LogFactory.getLog(JsonRiskMitigation.class);

	@JsonProperty
	private Integer riskMitigationId;
	@JsonProperty
	private String rmCode;
	@JsonProperty
	private String mitigationMeasure;
	@JsonProperty
	private String projectRecord;
	@JsonProperty
	private Integer isAvailable;
	@JsonProperty
	private String mitigationDate;
	@JsonProperty
	private String testReport;
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

	public JsonRiskMitigation() {
	}

	public JsonRiskMitigation(RiskMitigationMaster riskMitigationMaster) {
		this.riskMitigationId = riskMitigationMaster.getRiskMitigationId();
		this.rmCode = riskMitigationMaster.getRmCode();
		this.mitigationMeasure =  riskMitigationMaster.getMitigationMeasure();
		this.projectRecord = riskMitigationMaster.getProjectRecord();

		this.isAvailable = riskMitigationMaster.getIsAvailable();
		if (riskMitigationMaster.getMitigationDate() != null){
			this.mitigationDate = DateUtility.dateformatWithOutTime(riskMitigationMaster.getMitigationDate());
		}

		this.testReport = riskMitigationMaster.getTestReport();

		if (riskMitigationMaster.getProductMaster() != null){
			this.productId = riskMitigationMaster.getProductMaster().getProductId();
		}
		if (riskMitigationMaster.getCreatedDate() != null){
			this.createdDate = DateUtility.dateformatWithOutTime(riskMitigationMaster.getCreatedDate());
		}
		if (riskMitigationMaster.getModifiedDate() != null){
			this.modifiedDate = DateUtility.dateformatWithOutTime(riskMitigationMaster.getModifiedDate());
		}
		this.status = riskMitigationMaster.getStatus();

	}

	@JsonIgnore
	public RiskMitigationMaster getRiskMitigationList() {
		RiskMitigationMaster riskMitigationMaster = new RiskMitigationMaster();

		riskMitigationMaster.setRiskMitigationId(this.riskMitigationId);
		riskMitigationMaster.setRmCode(this.rmCode);
		riskMitigationMaster.setMitigationMeasure(this.mitigationMeasure);
		riskMitigationMaster.setProjectRecord(this.projectRecord);
		riskMitigationMaster.setIsAvailable(this.isAvailable);

		if (this.mitigationDate != null){
			riskMitigationMaster.setMitigationDate(DateUtility.dateformatWithOutTime(this.mitigationDate));
		}else{
			riskMitigationMaster.setMitigationDate(new Date());
		}

		riskMitigationMaster.setTestReport(this.testReport);

		ProductMaster productMaster = new ProductMaster();
		productMaster.setProductId(this.productId);
		riskMitigationMaster.setProductMaster(productMaster);

		if (this.createdDate != null){
			riskMitigationMaster.setCreatedDate(DateUtility.dateformatWithOutTime(this.createdDate));
		}else{
			riskMitigationMaster.setCreatedDate(new Date());
		}
		riskMitigationMaster.setModifiedDate(DateUtility.getCurrentTime());
		riskMitigationMaster.setStatus(this.status);

		return riskMitigationMaster;
	}

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

	public String getMitigationDate() {
		return mitigationDate;
	}

	public void setMitigationDate(String mitigationDate) {
		this.mitigationDate = mitigationDate;
	}

	public String getTestReport() {
		return testReport;
	}

	public void setTestReport(String testReport) {
		this.testReport = testReport;
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
