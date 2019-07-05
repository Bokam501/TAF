package com.hcl.atf.taf.model.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.TestFactoryResourceReservation;

public class JsonReservedResources implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@JsonProperty
	private Integer resourceReservationtId;
	@JsonProperty
	private Integer productId;
	@JsonProperty	
	private String productName;
	@JsonProperty
	private Integer workPackageId;
	@JsonProperty	
	private String workPackageName;
	@JsonProperty
	private Integer reservedResourceId;
	@JsonProperty	
	private String reservedResourceName;
	@JsonProperty
	private Integer reservedByUserId;
	@JsonProperty	
	private String reservedByUserName;
	@JsonProperty
	private Integer shiftId;
	@JsonProperty	
	private String shiftName;
	@JsonProperty	
	private String reservedForDate;
	

	public JsonReservedResources(){
		
	}
	
	
	public JsonReservedResources(TestFactoryResourceReservation resourceReservation){
		
		if (resourceReservation.getBlockedUser() != null){
			this.reservedResourceId = resourceReservation.getBlockedUser().getUserId();
			this.reservedResourceName = resourceReservation.getBlockedUser().getLoginId(); 
		}
		
		if (resourceReservation.getWorkPackage()!= null) {
			this.workPackageId = resourceReservation.getWorkPackage().getWorkPackageId();
			this.workPackageName = resourceReservation.getWorkPackage().getName();
		}
		
		if(resourceReservation.getWorkPackage().getProductBuild().getProductVersion().getProductMaster()!= null){
			this.productId = resourceReservation.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductId();
			this.productName = resourceReservation.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductName();
		}
		
		if(resourceReservation.getShift() != null){
			this.shiftId = resourceReservation.getShift().getShiftId();
			this.shiftName = resourceReservation.getShift().getShiftName();
		}
		
		if (resourceReservation.getReservationActionUser() != null){
			this.reservedByUserId = resourceReservation.getReservationActionUser().getUserId();
			this.reservedByUserName = resourceReservation.getReservationActionUser().getLoginId(); 
		}
		
		if(resourceReservation.getReservationDate() != null){
			this.reservedForDate = DateUtility.dateformatWithOutTime(resourceReservation.getReservationDate());
		}
	}
	
	
	@JsonIgnore
	public TestFactoryResourceReservation getReservedResources() {
		TestFactoryResourceReservation resourceReservation = new TestFactoryResourceReservation();
		
		if (resourceReservation.getBlockedUser() != null){
			this.reservedResourceId = resourceReservation.getBlockedUser().getUserId();
			this.reservedResourceName = resourceReservation.getBlockedUser().getLoginId(); 
		}
		
		if (resourceReservation.getWorkPackage()!= null) {
			this.workPackageId = resourceReservation.getWorkPackage().getWorkPackageId();
			this.workPackageName = resourceReservation.getWorkPackage().getName();
		}
		
		if(resourceReservation.getWorkPackage().getProductBuild().getProductVersion().getProductMaster()!= null){
			this.productId = resourceReservation.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductId();
			this.productName = resourceReservation.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductName();
		}
		
		if(resourceReservation.getShift() != null){
			this.shiftId = resourceReservation.getShift().getShiftId();
			this.shiftName = resourceReservation.getShift().getShiftName();
		}
		
		if (resourceReservation.getReservationActionUser() != null){
			this.reservedByUserId = resourceReservation.getReservationActionUser().getUserId();
			this.reservedByUserName = resourceReservation.getReservationActionUser().getLoginId(); 
		}
		
		if(this.reservedForDate == null || this.reservedForDate.trim().isEmpty()) {
			resourceReservation.setReservationDate(resourceReservation.getReservationDate());
		}
		else {
			resourceReservation.setReservationDate(DateUtility.dateFormatWithOutSecondsddMMyyyy(this.reservedForDate));
		}
		return resourceReservation;
	}
	
	public Integer getResourceReservationtId() {
		return resourceReservationtId;
	}


	public void setResourceReservationtId(Integer resourceReservationtId) {
		this.resourceReservationtId = resourceReservationtId;
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
	public Integer getWorkPackageId() {
		return workPackageId;
	}
	public void setWorkPackageId(Integer workPackageId) {
		this.workPackageId = workPackageId;
	}
	public String getWorkPackageName() {
		return workPackageName;
	}
	public void setWorkPackageName(String workPackageName) {
		this.workPackageName = workPackageName;
	}
	
	public Integer getReservedResourceId() {
		return reservedResourceId;
	}


	public void setReservedResourceId(Integer reservedResourceId) {
		this.reservedResourceId = reservedResourceId;
	}


	public String getReservedResourceName() {
		return reservedResourceName;
	}


	public void setReservedResourceName(String reservedResourceName) {
		this.reservedResourceName = reservedResourceName;
	}


	public Integer getReservedByUserId() {
		return reservedByUserId;
	}


	public void setReservedByUserId(Integer reservedByUserId) {
		this.reservedByUserId = reservedByUserId;
	}


	public String getReservedByUserName() {
		return reservedByUserName;
	}


	public void setReservedByUserName(String reservedByUserName) {
		this.reservedByUserName = reservedByUserName;
	}


	public Integer getShiftId() {
		return shiftId;
	}
	public void setShiftId(Integer shiftId) {
		this.shiftId = shiftId;
	}
	public String getShiftName() {
		return shiftName;
	}
	public void setShiftName(String shiftName) {
		this.shiftName = shiftName;
	}
	public String getReservedForDate() {
		return reservedForDate;
	}
	public void setReservedForDate(String reservedForDate) {
		this.reservedForDate = reservedForDate;
	}
}
