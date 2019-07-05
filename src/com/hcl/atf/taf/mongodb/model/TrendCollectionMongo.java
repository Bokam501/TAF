package com.hcl.atf.taf.mongodb.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "trendCollection")
public class TrendCollectionMongo {
	@Id
	private String id;
	
	private String _id;	
	private float scheduleVariance;
	private float defectValidity;
	private float defectQuality;
	private float testCaseJobExecution;
	private float costPerUnit;
	private float defectFindRate;
	private float internalDre;
	
	private float riskRemovalIndex;
	private float productQuality;
	private float productivity;
	private float utilizationIndex;
	private float pciScore;
	private float csat;
	private float arid;
	private float healthIndex;
	private String competencyName;
	
	private Date startDate;
	private Date endDate;
	
	private float productConfidence ;
	
	private Integer productId;
	private String productName;
	private Integer testFactoryId;
	private String testFactoryName;
	private Integer testCentersId;
	private String testCentersName;
	private Integer customerId;
	private String customerName;
	
	private Date currentDate;
	
	
	
	
	
	public TrendCollectionMongo(){
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public float getScheduleVariance() {
		return scheduleVariance;
	}

	public void setScheduleVariance(float scheduleVariance) {
		this.scheduleVariance = scheduleVariance;
	}

	public float getDefectValidity() {
		return defectValidity;
	}

	public void setDefectValidity(float defectValidity) {
		this.defectValidity = defectValidity;
	}

	public float getDefectQuality() {
		return defectQuality;
	}

	public void setDefectQuality(float defectQuality) {
		this.defectQuality = defectQuality;
	}

	public float getTestCaseJobExecution() {
		return testCaseJobExecution;
	}

	public void setTestCaseJobExecution(float testCaseJobExecution) {
		this.testCaseJobExecution = testCaseJobExecution;
	}

	public float getCostPerUnit() {
		return costPerUnit;
	}

	public void setCostPerUnit(float costPerUnit) {
		this.costPerUnit = costPerUnit;
	}

	public float getDefectFindRate() {
		return defectFindRate;
	}

	public void setDefectFindRate(float defectFindRate) {
		this.defectFindRate = defectFindRate;
	}

	public float getInternalDre() {
		return internalDre;
	}

	public void setInternalDre(float internalDre) {
		this.internalDre = internalDre;
	}

	public float getProductQuality() {
		return productQuality;
	}

	public void setProductQuality(float productQuality) {
		this.productQuality = productQuality;
	}

	public float getProductivity() {
		return productivity;
	}

	public void setProductivity(float productivity) {
		this.productivity = productivity;
	}

	public float getUtilizationIndex() {
		return utilizationIndex;
	}

	public void setUtilizationIndex(float utilizationIndex) {
		this.utilizationIndex = utilizationIndex;
	}

	public float getPciScore() {
		return pciScore;
	}

	public void setPciScore(float pciScore) {
		this.pciScore = pciScore;
	}

	public float getCsat() {
		return csat;
	}

	public void setCsat(float csat) {
		this.csat = csat;
	}

	public float getArid() {
		return arid;
	}

	public void setArid(float arid) {
		this.arid = arid;
	}

	public float getHealthIndex() {
		return healthIndex;
	}

	public void setHealthIndex(float healthIndex) {
		this.healthIndex = healthIndex;
	}

	public float getProductConfidence() {
		return productConfidence;
	}

	public void setProductConfidence(float productConfidence) {
		this.productConfidence = productConfidence;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getTestFactoryId() {
		return testFactoryId;
	}

	public void setTestFactoryId(Integer testFactoryId) {
		this.testFactoryId = testFactoryId;
	}

	public String getTestFactoryName() {
		return testFactoryName;
	}

	public void setTestFactoryName(String testFactoryName) {
		this.testFactoryName = testFactoryName;
	}

	public Integer getTestCentersId() {
		return testCentersId;
	}

	public void setTestCentersId(Integer testCentersId) {
		this.testCentersId = testCentersId;
	}

	public String getTestCentersName() {
		return testCentersName;
	}

	public void setTestCentersName(String testCentersName) {
		this.testCentersName = testCentersName;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	

	public float getRiskRemovalIndex() {
		return riskRemovalIndex;
	}

	public void setRiskRemovalIndex(float riskRemovalIndex) {
		this.riskRemovalIndex = riskRemovalIndex;
	}

	public String getCompetencyName() {
		return competencyName;
	}

	public void setCompetencyName(String competencyName) {
		this.competencyName = competencyName;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(Date currentDate) {
		this.currentDate = currentDate;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

}
