package com.hcl.atf.taf.model.json;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.TestFactory;
import com.hcl.atf.taf.model.TestFactoryCoreResource;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.UserRoleMaster;

public class JsonTestFactoryCoreResource {
	private static final Log log = LogFactory.getLog(JsonTestFactoryCoreResource.class);

	
	@JsonProperty
	private Integer testFactoryCoreResourceId;
	@JsonProperty
	private String remarks;
	@JsonProperty
	private String fromDate;
	@JsonProperty
	private String toDate;
	@JsonProperty
	private String createdDate;
	@JsonProperty
	private String modifiedDate;
	@JsonProperty
	private Integer status;
	@JsonProperty
	private Integer userRoleId;
	@JsonProperty
	private String roleName;
	@JsonProperty
	private Integer testFactoryId;
	@JsonProperty
	private Integer userId;
	@JsonProperty
	private String loginId;
	@JsonProperty
	private Integer userDefaultRoleId;
	@JsonProperty
	private String userDefaultRoleName;
	
public String getLoginId() {
		return loginId;
	}


	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}


public JsonTestFactoryCoreResource(){
		
	}


public JsonTestFactoryCoreResource(TestFactoryCoreResource tfCoreResource){
	
	this.testFactoryCoreResourceId=tfCoreResource.getTestFactoryCoreResourceId();
	this.remarks=tfCoreResource.getRemarks();
	
	if(tfCoreResource.getStatus() != null){
		this.status = tfCoreResource.getStatus();
	}
	if(tfCoreResource.getCreatedDate()!=null){
		
		this.createdDate = DateUtility.dateformatWithOutTime(tfCoreResource.getCreatedDate());
	}
	if(tfCoreResource.getModifiedDate()!=null){
		this.modifiedDate = DateUtility.dateformatWithOutTime(tfCoreResource.getModifiedDate());
		}
	if(tfCoreResource.getFromDate()!=null){
		this.fromDate=DateUtility.sdfDateformatWithOutTime(tfCoreResource.getFromDate());
	}
	if(tfCoreResource.getToDate()!=null){
		this.toDate=DateUtility.sdfDateformatWithOutTime(tfCoreResource.getToDate());
		}
	
	if(tfCoreResource.getTestFactory()!=null){
		this.testFactoryId=tfCoreResource.getTestFactory().getTestFactoryId();
	}
	if(tfCoreResource.getUserList()!=null){
		this.userId=tfCoreResource.getUserList().getUserId();
		this.loginId=tfCoreResource.getUserList().getLoginId();
	}
	if(tfCoreResource.getUserRoleMaster()!=null){
		this.userRoleId=tfCoreResource.getUserRoleMaster().getUserRoleId();
		
	}
	
	if(tfCoreResource.getUserList().getUserRoleMaster() != null){
		this.userDefaultRoleId = tfCoreResource.getUserList().getUserRoleMaster().getUserRoleId();
		this.userDefaultRoleName = tfCoreResource.getUserList().getUserRoleMaster().getRoleLabel();
	}
	
	
}

@JsonIgnore
public TestFactoryCoreResource  getTestFactoryCoreResource(){
	
	TestFactoryCoreResource tfCoreResource=new TestFactoryCoreResource();
	log.info("roleId-->"+this.userRoleId);
	log.info("roleName:-->"+this.roleName);
	if(this.testFactoryCoreResourceId!=null){
		tfCoreResource.setTestFactoryCoreResourceId(testFactoryCoreResourceId);
	}
	tfCoreResource.setRemarks(remarks);
	
	if(this.status != null ){			
		tfCoreResource.setStatus(status);			
	}else{
		tfCoreResource.setStatus(0);	
	}
	if(this.createdDate == null || this.createdDate.trim().isEmpty()) {
		tfCoreResource.setCreatedDate(DateUtility.getCurrentTime());
	} else {
	
		tfCoreResource.setCreatedDate(DateUtility.dateformatWithOutTime(this.createdDate));
	}
	tfCoreResource.setModifiedDate(DateUtility.getCurrentTime());
	if(this.fromDate == null || this.fromDate.trim().isEmpty()) {
	
		tfCoreResource.setFromDate(DateUtility.getCurrentTime());
	} else {
	
		tfCoreResource.setFromDate(DateUtility.dateformatWithOutTime(this.fromDate));
	}
	if(this.toDate == null || this.toDate.trim().isEmpty()) {
		
		tfCoreResource.setToDate(DateUtility.getCurrentTime());
	} else {
	
		tfCoreResource.setToDate(DateUtility.dateformatWithOutTime(this.toDate));
	}

	if(this.testFactoryId!=null){
    	TestFactory tf=new TestFactory();
    	tf.setTestFactoryId(this.testFactoryId);
    	
    	tfCoreResource.setTestFactory(tf);
	
	}
	
	if(this.userId!=null){
		UserList user=new UserList();
		user.setUserId(this.userId);
		tfCoreResource.setUserList(user);
	}
	
	if(this.userRoleId!=null){
    	UserRoleMaster userRoleMaster=new UserRoleMaster();
    	userRoleMaster.setUserRoleId(this.userRoleId);
    
    	tfCoreResource.setUserRoleMaster(userRoleMaster);
  
	}

return tfCoreResource;
}

	
	public Integer getTestFactoryCoreResourceId() {
		return testFactoryCoreResourceId;
	}
	public void setTestFactoryCoreResourceId(Integer testFactoryCoreResourceId) {
		this.testFactoryCoreResourceId = testFactoryCoreResourceId;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
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
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getUserRoleId() {
		return userRoleId;
	}
	public void setUserRoleId(Integer userRoleId) {
		this.userRoleId = userRoleId;
	}
	public Integer getTestFactoryId() {
		return testFactoryId;
	}
	public void setTestFactoryId(Integer testFactoryId) {
		this.testFactoryId = testFactoryId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}


	
	
	public String getRoleName() {
		return roleName;
	}


	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getUserDefaultRoleName() {
		return userDefaultRoleName;
	}

	public void setUserDefaultRoleName(String userDefaultRoleName) {
		this.userDefaultRoleName = userDefaultRoleName;
	}

}
