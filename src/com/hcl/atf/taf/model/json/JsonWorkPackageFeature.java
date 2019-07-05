package com.hcl.atf.taf.model.json;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.ProductFeature;
import com.hcl.atf.taf.model.WorkPackage;
import com.hcl.atf.taf.model.WorkPackageFeature;


public class JsonWorkPackageFeature implements java.io.Serializable{
	
	@JsonProperty	
	private int id;
	@JsonProperty	
	private int featureId;
	@JsonProperty	
	private String featureName;
	@JsonProperty	
	private String isSelected;
	@JsonProperty
	private int workPackageId;
	@JsonProperty	
	private String workPackageName;
	@JsonProperty	
	private String editedDate;
	@JsonProperty	
	private String createdDate;
	@JsonProperty	
	private String status;
	@JsonProperty	
	private String featureDesc;
	
	@JsonProperty
	private String productFeatureCode;
	
	public JsonWorkPackageFeature() {
	}	

	public JsonWorkPackageFeature(WorkPackageFeature workPackageFeature) {
		this.id=workPackageFeature.getWorkpackageFeatureId();
		this.featureId=workPackageFeature.getFeature().getProductFeatureId();
		this.featureName=workPackageFeature.getFeature().getProductFeatureName();
		this.productFeatureCode=workPackageFeature.getFeature().getProductFeatureCode();
		
		this.featureDesc=workPackageFeature.getFeature().getProductFeatureDescription();
		if (workPackageFeature.getIsSelected() == 0)
			this.isSelected="0";
		else
			this.isSelected="1";
		
		this.workPackageId=workPackageFeature.getWorkPackage().getWorkPackageId();
		this.workPackageName=workPackageFeature.getWorkPackage().getName();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(workPackageFeature.getCreatedDate()!=null)
			this.createdDate=sdf.format(workPackageFeature.getCreatedDate());
		if(workPackageFeature.getEditedDate()!=null)
			this.editedDate=sdf.format(workPackageFeature.getEditedDate());
		this.status=workPackageFeature.getStatus();
		
	}

	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	
	public int getFeatureId() {
		return featureId;
	}

	public void setFeatureId(int featureId) {
		this.featureId = featureId;
	}

	public String getFeatureName() {
		return featureName;
	}

	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}

	public String getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(String isSelected) {
		this.isSelected = isSelected;
	}

	public int getWorkPackageId() {
		return workPackageId;
	}

	public void setWorkPackageId(int workPackageId) {
		this.workPackageId = workPackageId;
	}

	public String getWorkPackageName() {
		return workPackageName;
	}

	public void setWorkPackageName(String workPackageName) {
		this.workPackageName = workPackageName;
	}

	public String getEditedDate() {
		return editedDate;
	}

	public void setEditedDate(String editedDate) {
		this.editedDate = editedDate;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@JsonIgnore
	public WorkPackageFeature getWorkPackageFeature() {
	
		WorkPackageFeature workPackageFeature = new WorkPackageFeature();
		workPackageFeature.setWorkpackageFeatureId(this.id);
		if (this.isSelected!=null && this.isSelected.equalsIgnoreCase("1") || this.isSelected.equalsIgnoreCase("Yes")) {
			workPackageFeature.setIsSelected(1);
		} else {
			workPackageFeature.setIsSelected(0);
		}
		ProductFeature feature= new ProductFeature();
		feature.setProductFeatureId(this.featureId);
		workPackageFeature.setFeature(feature);
		
		WorkPackage workPackage = new WorkPackage();
		workPackage.setWorkPackageId(this.workPackageId);
		workPackageFeature.setWorkPackage(workPackage);
		
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
		if(this.createdDate == null || this.createdDate.trim().isEmpty()) {
			workPackageFeature.setCreatedDate(new Date(System.currentTimeMillis()));
		} else {
			try {
				workPackageFeature.setCreatedDate(dateformat.parse(this.createdDate));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		workPackageFeature.setEditedDate(new Date(System.currentTimeMillis()));
		workPackageFeature.setStatus(this.status);
		
		return workPackageFeature;
	}

	public String getFeatureDesc() {
		return featureDesc;
	}

	public void setFeatureDesc(String featureDesc) {
		this.featureDesc = featureDesc;
	}

	public String getProductFeatureCode() {
		return productFeatureCode;
	}

	public void setProductFeatureCode(String productFeatureCode) {
		this.productFeatureCode = productFeatureCode;
	}
	
	
	
}
