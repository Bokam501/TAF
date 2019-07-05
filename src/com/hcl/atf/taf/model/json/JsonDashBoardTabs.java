package com.hcl.atf.taf.model.json;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.DashBoardTabs;
import com.hcl.atf.taf.model.TestFactory;


public class JsonDashBoardTabs implements java.io.Serializable {
	private static final Log log = LogFactory
			.getLog(JsonDashBoardTabs.class);
	@JsonProperty
	private Integer tabId;
	@JsonProperty
	private String tabName;
	@JsonProperty
	private String tabType;
	@JsonProperty
	private String deployment;
	@JsonProperty
	private Integer orderNo;
	@JsonProperty
	private Integer status;
	@JsonProperty
	private Integer engagementId;
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

	public JsonDashBoardTabs() {
	}

	public JsonDashBoardTabs(DashBoardTabs dashBoardTabs) {
		this.tabId = dashBoardTabs.getTabId();
		this.tabName = dashBoardTabs.getTabName();
		this.tabType= dashBoardTabs.getTabType();
		this.deployment = dashBoardTabs.getDeployment();
		this.status = dashBoardTabs.getStatus();
		if(dashBoardTabs.getOrderNo() != null){
			this.orderNo = dashBoardTabs.getOrderNo();
		}
		if(dashBoardTabs.getEngagement()!=null){
			this.engagementId=dashBoardTabs.getEngagement().getTestFactoryId();
		}
	}
	
	@JsonIgnore
	public DashBoardTabs getDashBoardTabs(){
		DashBoardTabs dashBoardTabs = new DashBoardTabs();
		if(tabId != null){
			dashBoardTabs.setTabId(tabId);
		}
		
		dashBoardTabs.setTabName(tabName);
		dashBoardTabs.setTabType(tabType);
		dashBoardTabs.setDeployment(deployment);
		dashBoardTabs.setOrderNo(orderNo);
		 if(this.status != null ){			
			 dashBoardTabs.setStatus(status);			
			}else{
				dashBoardTabs.setStatus(0);	
			}
		
		if(this.engagementId!=null){
			TestFactory testFactory=new TestFactory();
			testFactory.setTestFactoryId(engagementId);
			dashBoardTabs.setEngagement(testFactory);
		}
		 
		return dashBoardTabs;
	}

	public Integer getTabId() {
		return tabId;
	}

	public void setTabId(Integer tabId) {
		this.tabId = tabId;
	}

	public String getTabName() {
		return tabName;
	}

	public void setTabName(String tabName) {
		this.tabName = tabName;
	}

	public String getTabType() {
		return tabType;
	}

	public void setTabType(String tabType) {
		this.tabType = tabType;
	}

	public String getDeployment() {
		return deployment;
	}

	public void setDeployment(String deployment) {
		this.deployment = deployment;
	}

	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getEngagementId() {
		return engagementId;
	}

	public void setEngagementId(Integer engagementId) {
		this.engagementId = engagementId;
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
