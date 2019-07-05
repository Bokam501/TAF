package com.hcl.atf.taf.model.json;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.dto.ProductSummaryForActivityProcessDTO;


public class JsonProductSummaryForActivityProcessDTO {
	private static final Log log = LogFactory.getLog(JsonProductSummaryForActivityProcessDTO.class);
	
	@JsonProperty
	private String productName;
	@JsonProperty
	private String status;
	@JsonProperty
	private Integer activeWorkPackageCount;
	@JsonProperty
	private Integer totalWorkPackageCount;
	@JsonProperty
	private Integer totalActivityCount;
	@JsonProperty
	private Integer openActivityCount;
	@JsonProperty
	private Integer onHoldActivityCount;
	@JsonProperty
	private Integer completedActivityCount;
	@JsonProperty
	private Integer totalUsersCount;
	@JsonProperty
	private Integer totalMangersCount;
	@JsonProperty
	private Integer totalLeadsCount;
	@JsonProperty
	private Integer totalEngineersCount;
	
	@JsonProperty
	private String productQuality;
	@JsonProperty
	private String productivity;
	@JsonProperty
	private String requiredRunRate;
	@JsonProperty
	private String scheduleVariance;
	
	
	  

	public JsonProductSummaryForActivityProcessDTO(){	
	}
	
	
	public JsonProductSummaryForActivityProcessDTO(ProductSummaryForActivityProcessDTO productSummaryActivityProcess){	
		
		this.productName = productSummaryActivityProcess.getProductName();
		this.status = "In-Active";
		if(productSummaryActivityProcess.getStatus()==1){
			this.status = "Active";
		}
		this.activeWorkPackageCount = productSummaryActivityProcess.getActiveWorkPackageCount();
		this.totalWorkPackageCount=productSummaryActivityProcess.getTotalWorkPackageCount();
		this.totalActivityCount=productSummaryActivityProcess.getTotalActivityCount();
		this.openActivityCount=productSummaryActivityProcess.getOpenActivityCount();
		this.onHoldActivityCount=productSummaryActivityProcess.getOnHoldActivityCount();
		this.completedActivityCount=productSummaryActivityProcess.getCompletedActivityCount();
		this.totalUsersCount=productSummaryActivityProcess.getTotalUsersCount();
		this.totalMangersCount=productSummaryActivityProcess.getTotalMangersCount();
		this.totalLeadsCount=productSummaryActivityProcess.getTotalLeadsCount();
		this.totalEngineersCount=productSummaryActivityProcess.getTotalEngineersCount();
		this.productQuality=productSummaryActivityProcess.getProductQuality();
		this.productivity=productSummaryActivityProcess.getProductivity();
		this.requiredRunRate=productSummaryActivityProcess.getRequiredRunRate();
		this.scheduleVariance=productSummaryActivityProcess.getScheduleVariance();
		
	}


	


	public String getScheduleVariance() {
		return scheduleVariance;
	}


	public void setScheduleVariance(String scheduleVariance) {
		this.scheduleVariance = scheduleVariance;
	}


	public String getRequiredRunRate() {
		return requiredRunRate;
	}


	public void setRequiredRunRate(String requiredRunRate) {
		this.requiredRunRate = requiredRunRate;
	}


	public String getProductivity() {
		return productivity;
	}


	public void setProductivity(String productivity) {
		this.productivity = productivity;
	}


	public String getProductQuality() {
		return productQuality;
	}


	public void setProductQuality(String productQuality) {
		this.productQuality = productQuality;
	}


	public String getProductName() {
		return productName;
	}


	public void setProductName(String productName) {
		this.productName = productName;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public Integer getActiveWorkPackageCount() {
		return activeWorkPackageCount;
	}


	public void setActiveWorkPackageCount(Integer activeWorkPackageCount) {
		this.activeWorkPackageCount = activeWorkPackageCount;
	}


	public Integer getTotalWorkPackageCount() {
		return totalWorkPackageCount;
	}


	public void setTotalWorkPackageCount(Integer totalWorkPackageCount) {
		this.totalWorkPackageCount = totalWorkPackageCount;
	}


	public Integer getTotalActivityCount() {
		return totalActivityCount;
	}


	public void setTotalActivityCount(Integer totalActivityCount) {
		this.totalActivityCount = totalActivityCount;
	}


	public Integer getOpenActivityCount() {
		return openActivityCount;
	}


	public void setOpenActivityCount(Integer openActivityCount) {
		this.openActivityCount = openActivityCount;
	}


	public Integer getOnHoldActivityCount() {
		return onHoldActivityCount;
	}


	public void setOnHoldActivityCount(Integer onHoldActivityCount) {
		this.onHoldActivityCount = onHoldActivityCount;
	}


	public Integer getCompletedActivityCount() {
		return completedActivityCount;
	}


	public void setCompletedActivityCount(Integer completedActivityCount) {
		this.completedActivityCount = completedActivityCount;
	}


	public Integer getTotalUsersCount() {
		return totalUsersCount;
	}


	public void setTotalUsersCount(Integer totalUsersCount) {
		this.totalUsersCount = totalUsersCount;
	}


	public Integer getTotalMangersCount() {
		return totalMangersCount;
	}


	public void setTotalMangersCount(Integer totalMangersCount) {
		this.totalMangersCount = totalMangersCount;
	}


	public Integer getTotalLeadsCount() {
		return totalLeadsCount;
	}


	public void setTotalLeadsCount(Integer totalLeadsCount) {
		this.totalLeadsCount = totalLeadsCount;
	}


	public Integer getTotalEngineersCount() {
		return totalEngineersCount;
	}


	public void setTotalEngineersCount(Integer totalEngineersCount) {
		this.totalEngineersCount = totalEngineersCount;
	}
	
	
	
	
}
