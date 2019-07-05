package com.hcl.atf.taf.model.json;

import java.util.Comparator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonWorkPackageResourceReservation implements java.io.Serializable {
	private static final Log log = LogFactory.getLog(JsonWorkPackageResourceReservation.class);
	
	@JsonProperty
	private Integer workPackageResourceReservationId;
	@JsonProperty
	private Integer workPackageId;
	@JsonProperty
	private String workPackageName;
	@JsonProperty
	private Integer productId;
	@JsonProperty
	private String ProductName;
	@JsonProperty
	private Integer shiftId;
	@JsonProperty	
	private String shiftName;
	@JsonProperty
	private Integer shiftTypeId;
	@JsonProperty	
	private String shiftTypeName;
	@JsonProperty
	private Integer demandId;
	@JsonProperty	
	private Float resourceDemandCount;
	@JsonProperty	
	private Float availableCoreResourceCount;
	@JsonProperty	
	private Float blockedResourceCount;
	@JsonProperty	
	private Float gapInResourceCount;
	@JsonProperty
	private String dateOfCount;
	
	public JsonWorkPackageResourceReservation(){
		resourceDemandCount = 0f;
		availableCoreResourceCount = 0f;
		blockedResourceCount = 0f;
		gapInResourceCount = 0f;
	}
	
	public Integer getWorkPackageResourceReservationId() {
		return workPackageResourceReservationId;
	}
	public void setWorkPackageResourceReservationId(
			Integer workPackageResourceReservationId) {
		this.workPackageResourceReservationId = workPackageResourceReservationId;
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
	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return ProductName;
	}

	public void setProductName(String productName) {
		ProductName = productName;
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
	public Integer getShiftTypeId() {
		return shiftTypeId;
	}

	public void setShiftTypeId(Integer shiftTypeId) {
		this.shiftTypeId = shiftTypeId;
	}

	public String getShiftTypeName() {
		return shiftTypeName;
	}

	public void setShiftTypeName(String shiftTypeName) {
		this.shiftTypeName = shiftTypeName;
	}

	public Integer getDemandId() {
		return demandId;
	}
	public void setDemandId(Integer demandId) {
		this.demandId = demandId;
	}
	public Float getResourceDemandCount() {
		return resourceDemandCount;
	}
	public void setResourceDemandCount(Float resourceDemandCount) {
		this.resourceDemandCount = resourceDemandCount;
	}
	public Float getAvailableCoreResourceCount() {
		return availableCoreResourceCount;
	}
	public void setAvailableCoreResourceCount(Float availableCoreResourceCount) {
		this.availableCoreResourceCount = availableCoreResourceCount;
	}
	public Float getBlockedResourceCount() {
		return blockedResourceCount;
	}
	public void setBlockedResourceCount(Float blockedResourceCount) {
		this.blockedResourceCount = blockedResourceCount;
	}
	public Float getGapInResourceCount() {
		return gapInResourceCount;
	}
	public void setGapInResourceCount(Float gapInResourceCount) {
		this.gapInResourceCount = gapInResourceCount;
	}

	public String getDateOfCount() {
		return dateOfCount;
	}

	public void setDateOfCount(String dateOfCount) {
		this.dateOfCount = dateOfCount;
	}
	
	public static Comparator<JsonWorkPackageResourceReservation> jsonWorkPackageResourceComparator = new Comparator<JsonWorkPackageResourceReservation>() {

		public int compare(JsonWorkPackageResourceReservation shift1, JsonWorkPackageResourceReservation shift2) {
			String shift1Id = shift1.getShiftName().toUpperCase();
			String shift2Id = shift2.getShiftName().toUpperCase();

			// ascending order
			return shift1Id.compareTo(shift2Id);

			// descending order
			// return shift2Id.compareTo(shift1Id);
		}
	};
}
