package com.hcl.atf.taf.model.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.DashBoardTabs;
import com.hcl.atf.taf.model.DashBoardTabsRoleBasedURL;
import com.hcl.atf.taf.model.DashboardVisualizationUrls;
import com.hcl.atf.taf.model.UserRoleMaster;

public class JsonDashBoardTabsRoleBasedURL {
	@JsonProperty
	private Integer roleBasedId;
	@JsonProperty
	private Integer dashBoardTabsId;
	@JsonProperty
	private String dashBoardTabsName;
	@JsonProperty
	private Integer productSpecificUserRoleId;
	@JsonProperty
	private String productSpecificUserRoleName;
	@JsonProperty
	private Integer urlId;
	@JsonProperty
	private String url;
	@JsonProperty
	private String testFactoryDetails;
	@JsonProperty
	private String productDetails;
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
	
	
	public JsonDashBoardTabsRoleBasedURL(){
		
	}
	
	public JsonDashBoardTabsRoleBasedURL(DashBoardTabsRoleBasedURL dashBoardTabsRoleBasedURL){
		this.roleBasedId = dashBoardTabsRoleBasedURL.getRoleBasedId();
		
		
		
		if(dashBoardTabsRoleBasedURL.getDashBoardTabs()!=null){
			this.dashBoardTabsId=dashBoardTabsRoleBasedURL.getDashBoardTabs().getTabId();
			this.dashBoardTabsName=dashBoardTabsRoleBasedURL.getDashBoardTabs().getTabName();
		}
		
		
		if(dashBoardTabsRoleBasedURL.getProductSpecificUserRole()!=null){
			this.productSpecificUserRoleId=dashBoardTabsRoleBasedURL.getProductSpecificUserRole().getUserRoleId();
			this.productSpecificUserRoleName=dashBoardTabsRoleBasedURL.getProductSpecificUserRole().getRoleLabel();
		}
		
		if(dashBoardTabsRoleBasedURL.getUrl()!=null){
			this.url = dashBoardTabsRoleBasedURL.getUrl().getUrl();
			this.urlId=dashBoardTabsRoleBasedURL.getUrl().getVisualizationId();
		}
		
		this.status = dashBoardTabsRoleBasedURL.getStatus();

		
	}
	
	@JsonIgnore
	public DashBoardTabsRoleBasedURL  getDashBoardTabsRoleBasedURL(){

		
		DashBoardTabsRoleBasedURL dashBoardTabsRoleBasedURL=new DashBoardTabsRoleBasedURL();
		
		
		if (roleBasedId != null) {
			dashBoardTabsRoleBasedURL.setRoleBasedId(roleBasedId);
		}
		
		
		if(this.dashBoardTabsId!=null){
			DashBoardTabs dashBoardTabs = new DashBoardTabs();
			dashBoardTabs.setTabId(dashBoardTabsId);
			dashBoardTabsRoleBasedURL.setDashBoardTabs(dashBoardTabs);
			}
		
	    if(this.productSpecificUserRoleId != null){
				UserRoleMaster userRole = new UserRoleMaster();
				userRole.setUserRoleId(productSpecificUserRoleId);
				dashBoardTabsRoleBasedURL.setProductSpecificUserRole(userRole);
			}
	    
	    DashboardVisualizationUrls visualization=null;
		    if(this.urlId!=null){
		    	visualization=new DashboardVisualizationUrls();
		    	visualization.setVisualizationId(this.urlId);
		    }
	    dashBoardTabsRoleBasedURL.setUrl(visualization);
	    
	    if(this.status != null ){			
	    	dashBoardTabsRoleBasedURL.setStatus(status);			
		}else{
			dashBoardTabsRoleBasedURL.setStatus(0);	
		}
		
		return dashBoardTabsRoleBasedURL;
		
	}

	public Integer getRoleBasedId() {
		return roleBasedId;
	}

	public void setRoleBasedId(Integer roleBasedId) {
		this.roleBasedId = roleBasedId;
	}

	public Integer getDashBoardTabsId() {
		return dashBoardTabsId;
	}

	public void setDashBoardTabsId(Integer dashBoardTabsId) {
		this.dashBoardTabsId = dashBoardTabsId;
	}

	public String getDashBoardTabsName() {
		return dashBoardTabsName;
	}

	public void setDashBoardTabsName(String dashBoardTabsName) {
		this.dashBoardTabsName = dashBoardTabsName;
	}

	public Integer getProductSpecificUserRoleId() {
		return productSpecificUserRoleId;
	}

	public void setProductSpecificUserRoleId(Integer productSpecificUserRoleId) {
		this.productSpecificUserRoleId = productSpecificUserRoleId;
	}

	public String getProductSpecificUserRoleName() {
		return productSpecificUserRoleName;
	}

	public void setProductSpecificUserRoleName(String productSpecificUserRoleName) {
		this.productSpecificUserRoleName = productSpecificUserRoleName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getTestFactoryDetails() {
		return testFactoryDetails;
	}

	public void setTestFactoryDetails(String testFactoryDetails) {
		this.testFactoryDetails = testFactoryDetails;
	}

	public String getProductDetails() {
		return productDetails;
	}

	public void setProductDetails(String productDetails) {
		this.productDetails = productDetails;
	}

	public Integer getUrlId() {
		return urlId;
	}

	public void setUrlId(Integer urlId) {
		this.urlId = urlId;
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
