package com.hcl.atf.taf.model.dto;

public class ProductSummaryForActivityProcessDTO {
	
	private String productName;
	private Integer status;
	private Integer activeWorkPackageCount;
	private Integer totalWorkPackageCount;
	private Integer totalActivityCount;
	private Integer openActivityCount;
	private Integer onHoldActivityCount;
	private Integer completedActivityCount;
	private Integer totalUsersCount;
	private Integer totalMangersCount;
	private Integer totalLeadsCount;
	private Integer totalEngineersCount;
	private String productQuality;
	private String productivity;
	private String requiredRunRate;
	private String scheduleVariance;
	
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
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
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
	
	public Integer getTotalActivityCount() {
		return totalActivityCount;
	}
	public void setTotalActivityCount(Integer totalActivityCount) {
		this.totalActivityCount = totalActivityCount;
	}
	
}
