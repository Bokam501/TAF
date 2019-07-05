package com.hcl.atf.taf.model.json;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.DecouplingCategory;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.UserTypeMasterNew;

public class JsonDecouplingCategory implements java.io.Serializable {

	private static final Log log = LogFactory
			.getLog(JsonDecouplingCategory.class);

	@JsonProperty
	private Integer decouplingCategoryId;
	@JsonProperty
	private String decouplingCategoryName;
	@JsonProperty
	private String displayName;
	@JsonProperty
	private String description;
	@JsonProperty
	private Integer status;
	@JsonProperty
	private String createdDate;
	@JsonProperty
	private String modifiedDate;
	@JsonProperty
	private Integer productMasterId;
	@JsonProperty
	private Integer parentCategoryId;
	@JsonProperty
	private String parentCategoryName;
	@JsonProperty
	private String userTypeLabel;
	@JsonProperty
	private Integer userTypeId;
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

	public JsonDecouplingCategory() {
	}

	public JsonDecouplingCategory(DecouplingCategory decouplingCategory) {
		this.decouplingCategoryId = decouplingCategory
				.getDecouplingCategoryId();
		this.decouplingCategoryName = decouplingCategory
				.getDecouplingCategoryName();
		this.displayName = decouplingCategory
				.getDisplayName();
		this.description = decouplingCategory.getDescription();

		if(decouplingCategory.getStatus() != null){
			this.status = decouplingCategory.getStatus();
		}			
		if(decouplingCategory.getCreatedDate()!=null){
			this.createdDate = DateUtility.dateformatWithOutTime(decouplingCategory.getCreatedDate());
		}
		if(decouplingCategory.getModifiedDate()!=null){
			this.modifiedDate = DateUtility.dateformatWithOutTime(decouplingCategory.getModifiedDate());
			}
			
		
		if (decouplingCategory.getProduct() != null) {
			productMasterId = decouplingCategory.getProduct().getProductId();
		}

		if (decouplingCategory.getUserTypeMasterNew() != null) {
			this.userTypeId = decouplingCategory.getUserTypeMasterNew()
					.getUserTypeId();
			this.userTypeLabel = decouplingCategory.getUserTypeMasterNew()
					.getUserTypeLabel();
		} else {
			this.userTypeId = 0;
			this.userTypeLabel = null;
		}

		DecouplingCategory parentDecouplingCategory = decouplingCategory.getParentCategory();
		if(parentDecouplingCategory != null){
			this.parentCategoryId=parentDecouplingCategory.getDecouplingCategoryId();
			if(parentDecouplingCategory.getDecouplingCategoryName() != null){
				this.parentCategoryName=parentDecouplingCategory.getDecouplingCategoryName();
			}else{
				this.parentCategoryName=null;
			}
			
		}else{
			this.parentCategoryId=0;
			this.parentCategoryName=null;
		}
		if (decouplingCategory.getProduct() != null) {
			this.productMasterId = decouplingCategory.getProduct()
					.getProductId();
		}
	}

	public Integer getDecouplingCategoryId() {
		return decouplingCategoryId;
	}

	public void setDecouplingCategoryId(Integer decouplingCategoryId) {
		this.decouplingCategoryId = decouplingCategoryId;
	}

	public String getDecouplingCategoryName() {
		return decouplingCategoryName;
	}

	public void setDecouplingCategoryName(String decouplingCategoryName) {
		this.decouplingCategoryName = decouplingCategoryName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	public Integer getParentCategoryId() {
		return parentCategoryId;
	}

	public void setParentCategoryId(Integer parentCategoryId) {
		this.parentCategoryId = parentCategoryId;
	}

	@JsonIgnore
	public DecouplingCategory getDecouplingCategory() {

		DecouplingCategory decouplingCategory = new DecouplingCategory();
		decouplingCategory.setDecouplingCategoryId(decouplingCategoryId);
		decouplingCategory.setDecouplingCategoryName(decouplingCategoryName);
		decouplingCategory.setDisplayName(displayName);
		decouplingCategory.setDescription(description);
	
		if(this.status != null ){			
			decouplingCategory.setStatus(status);			
		}else{
			decouplingCategory.setStatus(0);	
		}		

		if(this.createdDate == null || this.createdDate.trim().isEmpty()) {
			decouplingCategory.setCreatedDate(DateUtility.getCurrentTime());
		}else{			
			decouplingCategory.setCreatedDate(DateUtility.dateformatWithOutTime(this.createdDate));						
		}
	
		decouplingCategory.setModifiedDate(DateUtility.getCurrentTime());
		
		
		if (productMasterId != null) {
			ProductMaster product = new ProductMaster();
			product.setProductId(productMasterId);
			decouplingCategory.setProduct(product);// Product object is passed
													// not ID.
		}	

		DecouplingCategory parentDecouplingCategory = new DecouplingCategory();
		parentDecouplingCategory.setDecouplingCategoryId(parentCategoryId);
		decouplingCategory.setParentCategory(parentDecouplingCategory);
		
		UserTypeMasterNew userTypeMasterNew = new UserTypeMasterNew();
		if (this.userTypeLabel != null && !this.userTypeLabel.equals("")) {
			if (isInteger(this.userTypeLabel)) {
				userTypeMasterNew = new UserTypeMasterNew();
				userTypeMasterNew
						.setUserTypeId(new Integer(this.userTypeLabel));
				decouplingCategory.setUserTypeMasterNew(userTypeMasterNew);
				
			} else {
				userTypeMasterNew = new UserTypeMasterNew();
				userTypeMasterNew.setUserTypeLabel(this.userTypeLabel);
				decouplingCategory.setUserTypeMasterNew(userTypeMasterNew);
				
			}
		}
		log.info("Getter  ---   DC ID :"+decouplingCategoryId+", DC Name : "+decouplingCategoryName+", DC DN : "+displayName+", P ID :"+parentCategoryId+", P Name: "+parentCategoryName);
		return decouplingCategory;
	}

	public static boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	public Integer getProductMasterId() {
		return productMasterId;
	}

	public void setProductMasterId(Integer productMasterId) {
		this.productMasterId = productMasterId;
	}

	public String getParentCategoryName() {
		return parentCategoryName;
	}

	public void setParentCategoryName(String parentCategoryName) {
		this.parentCategoryName = parentCategoryName;
	}

	public String getUserTypeLabel() {
		return userTypeLabel;
	}

	public void setUserTypeLabel(String userTypeLabel) {
		this.userTypeLabel = userTypeLabel;
	}

	public Integer getUserTypeId() {
		return userTypeId;
	}

	public void setUserTypeId(Integer userTypeId) {
		this.userTypeId = userTypeId;
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
