package com.hcl.atf.taf.mongodb.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.hcl.atf.taf.model.UserList;


@Document(collection = "userlist")
public class UserListMongo {
	
	@Id
	private String id;
	
	private Integer userId;
	private Integer userTypeMasterId;
	private String userTypeMasterName;
	
	private String commonActiveStatus;
	private String userPassword;
	private String userDisplayName;
	
	private Integer userRoleId;
	private String userRoleName;
	
	private String status;
	private String firstName;
	private String lastName;
	private String middleName;
	private String userCode;
	private String loginId;
	private String emailId;
	private String contactNumber;
	
	
	private Integer authenticationTypeId;
	private String authenticationTypeName;
	
	private Integer authenticationModeId;
	private String authenticationModeName;
	
	private Integer resourcePoolId;
	private String resourcePoolName;
	
	private Integer vendorId;
	private String vendorName;
	
	private String userStatus;
	private String imageURI;
	
	
	private String languages;
	
	

	public UserListMongo(){
		
	}
	

	
public UserListMongo(UserList user) {
	this.id=user.getUserId()+"";
	this.userId=user.getUserId();
	
	if (user.getStatus() == 1){
		this.status = "Active";
	}else{
		this.status = "InActive";
	}
		
	
	this.userPassword=user.getUserPassword();
	this.userDisplayName=user.getUserDisplayName();
	this.firstName=user.getFirstName();
	this.lastName=user.getLastName();
	this.middleName=user.getMiddleName();
	this.userCode=user.getUserCode();
	this.loginId=user.getLoginId();
	this.emailId=user.getEmailId();
	this.contactNumber=user.getContactNumber();
	
	if(user.getStatus() == 1){
		this.userStatus="Active";
	}else{
		this.userStatus="InActive";
	}
	
	
	
	this.imageURI=user.getImageURI();
	this.languages=user.getLanguages().getName();
	
	if(user.getUserTypeMasterNew()!=null){
		this.userTypeMasterId=user.getUserTypeMasterNew().getUserTypeId();
		this.userTypeMasterName=user.getUserTypeMasterNew().getUserTypeName();
	}
	
	if(user.getCommonActiveStatusMaster()!=null){
		this.commonActiveStatus=user.getCommonActiveStatusMaster().getStatus();
	}
	
	
	if(user.getUserRoleMaster()!=null){
		this.userRoleId=user.getUserRoleMaster().getUserRoleId();
		this.userRoleName=user.getUserRoleMaster().getRoleName();
	}
	
	if(user.getAuthenticationType()!=null){
		this.authenticationTypeId=user.getAuthenticationType().getAuthenticationTypeId();
		this.authenticationTypeName=user.getAuthenticationType().getAuthenticationTypeName();
	}

	if(user.getAuthenticationMode()!=null){
		this.authenticationModeId=user.getAuthenticationMode().getAuthenticationModeId();
		this.authenticationModeName=user.getAuthenticationMode().getAuthenticationModeName();
	}
	
	if(user.getResourcePool()!=null){
		this.resourcePoolId=user.getResourcePool().getResourcePoolId();
		this.resourcePoolName=user.getResourcePool().getResourcePoolName();
	}

	if(user.getVendor()!=null){
		this.vendorId=user.getVendor().getVendorId();
		this.vendorName=user.getVendor().getRegisteredCompanyName();
	}
	
	
}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}



	public Integer getUserId() {
		return userId;
	}



	public void setUserId(Integer userId) {
		this.userId = userId;
	}



	public Integer getUserTypeMasterId() {
		return userTypeMasterId;
	}



	public void setUserTypeMasterId(Integer userTypeMasterId) {
		this.userTypeMasterId = userTypeMasterId;
	}



	public String getUserTypeMasterName() {
		return userTypeMasterName;
	}



	public void setUserTypeMasterName(String userTypeMasterName) {
		this.userTypeMasterName = userTypeMasterName;
	}



	public String getCommonActiveStatus() {
		return commonActiveStatus;
	}



	public void setCommonActiveStatus(String commonActiveStatus) {
		this.commonActiveStatus = commonActiveStatus;
	}



	public String getUserPassword() {
		return userPassword;
	}



	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}



	public String getUserDisplayName() {
		return userDisplayName;
	}



	public void setUserDisplayName(String userDisplayName) {
		this.userDisplayName = userDisplayName;
	}



	public Integer getUserRoleId() {
		return userRoleId;
	}



	public void setUserRoleId(Integer userRoleId) {
		this.userRoleId = userRoleId;
	}



	public String getUserRoleName() {
		return userRoleName;
	}



	public void setUserRoleName(String userRoleName) {
		this.userRoleName = userRoleName;
	}



	public String getStatus() {
		return status;
	}



	public void setStatus(String status) {
		this.status = status;
	}



	public String getFirstName() {
		return firstName;
	}



	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}



	public String getLastName() {
		return lastName;
	}



	public void setLastName(String lastName) {
		this.lastName = lastName;
	}



	public String getMiddleName() {
		return middleName;
	}



	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}



	public String getUserCode() {
		return userCode;
	}



	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}



	public String getLoginId() {
		return loginId;
	}



	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}



	public String getEmailId() {
		return emailId;
	}



	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}



	public String getContactNumber() {
		return contactNumber;
	}



	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}



	public Integer getAuthenticationTypeId() {
		return authenticationTypeId;
	}



	public void setAuthenticationTypeId(Integer authenticationTypeId) {
		this.authenticationTypeId = authenticationTypeId;
	}



	public String getAuthenticationTypeName() {
		return authenticationTypeName;
	}



	public void setAuthenticationTypeName(String authenticationTypeName) {
		this.authenticationTypeName = authenticationTypeName;
	}



	public Integer getAuthenticationModeId() {
		return authenticationModeId;
	}



	public void setAuthenticationModeId(Integer authenticationModeId) {
		this.authenticationModeId = authenticationModeId;
	}



	public String getAuthenticationModeName() {
		return authenticationModeName;
	}



	public void setAuthenticationModeName(String authenticationModeName) {
		this.authenticationModeName = authenticationModeName;
	}



	public Integer getResourcePoolId() {
		return resourcePoolId;
	}



	public void setResourcePoolId(Integer resourcePoolId) {
		this.resourcePoolId = resourcePoolId;
	}



	public String getResourcePoolName() {
		return resourcePoolName;
	}



	public void setResourcePoolName(String resourcePoolName) {
		this.resourcePoolName = resourcePoolName;
	}



	public Integer getVendorId() {
		return vendorId;
	}



	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}



	public String getVendorName() {
		return vendorName;
	}



	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}



	public String getUserStatus() {
		return userStatus;
	}



	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}



	public String getImageURI() {
		return imageURI;
	}



	public void setImageURI(String imageURI) {
		this.imageURI = imageURI;
	}



	public String getLanguages() {
		return languages;
	}



	public void setLanguages(String languages) {
		this.languages = languages;
	}
	
}
