package com.hcl.atf.taf.model.json;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.DashboardVisualizationUrls;
import com.hcl.atf.taf.model.UserList;


public class JsonDashBoardVisualizationURL implements java.io.Serializable {
	private static final Log log = LogFactory
			.getLog(JsonDashBoardVisualizationURL.class);
	
	@JsonProperty
	private Integer visualizationId;
	@JsonProperty
	private String urlName;
	@JsonProperty
	private String url;
	@JsonProperty
	private Integer status;
	@JsonProperty
	private String description;
	@JsonProperty
	private Integer createdBy;
	@JsonProperty
	private Date createdDate;
	@JsonProperty
	private Date modifiedDate;
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


	public JsonDashBoardVisualizationURL() {
	}

	public JsonDashBoardVisualizationURL(DashboardVisualizationUrls dashboardVisualizationUrls) {
		
		this.visualizationId=dashboardVisualizationUrls.getVisualizationId();
		this.urlName=dashboardVisualizationUrls.getUrlName();
		this.url=dashboardVisualizationUrls.getUrl();
		this.status=dashboardVisualizationUrls.getStatus();
		this.description=dashboardVisualizationUrls.getDescription();
		if(dashboardVisualizationUrls.getCreatedBy()!=null){
			this.createdBy=dashboardVisualizationUrls.getCreatedBy().getUserId();
		}
		this.createdDate=dashboardVisualizationUrls.getCreatedDate();
		this.modifiedDate=dashboardVisualizationUrls.getModifiedDate();
		
	}
	
	@JsonIgnore
	public DashboardVisualizationUrls getDashboardVisualizationUrls(){
		DashboardVisualizationUrls dashboardVisualizationUrls = new DashboardVisualizationUrls();
		if(visualizationId != null){
			dashboardVisualizationUrls.setVisualizationId(visualizationId);
		}
		dashboardVisualizationUrls.setUrlName(urlName);
		dashboardVisualizationUrls.setUrl(url);
		
		 if(this.status != null ){			
			 dashboardVisualizationUrls.setStatus(status);			
			}else{
				dashboardVisualizationUrls.setStatus(0);	
			}
		
		dashboardVisualizationUrls.setDescription(description);
		
		if(createdBy!=null){
			UserList user=new UserList();
			user.setUserId(createdBy);
			dashboardVisualizationUrls.setCreatedBy(user);
			
		}
		
		dashboardVisualizationUrls.setCreatedDate(createdDate);
		dashboardVisualizationUrls.setModifiedDate(modifiedDate);
	
		return dashboardVisualizationUrls;
	}

	

	public Integer getVisualizationId() {
		return visualizationId;
	}

	public void setVisualizationId(Integer visualizationId) {
		this.visualizationId = visualizationId;
	}

	public String getUrlName() {
		return urlName;
	}

	public void setUrlName(String urlName) {
		this.urlName = urlName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
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
