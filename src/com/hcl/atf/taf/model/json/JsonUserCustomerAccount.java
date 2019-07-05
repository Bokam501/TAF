package com.hcl.atf.taf.model.json;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.Customer;
import com.hcl.atf.taf.model.UserCustomerAccount;
import com.hcl.atf.taf.model.UserList;

public class JsonUserCustomerAccount implements java.io.Serializable {
	
	private static final Log log = LogFactory
			.getLog(JsonUserCustomerAccount.class);
	
	@JsonProperty
	private Integer userCustomerAccountId;
	@JsonProperty
	private String userCustomerCode;
	@JsonProperty
	private String userCustomerEmailId;
	@JsonProperty
	private String validFromDate;
	@JsonProperty
	private String validTillDate;
	@JsonProperty
	private Integer  userId;
	@JsonProperty
	private Integer customerId;
	@JsonProperty
	private String customerName;
	@JsonProperty
	private String userCustomerName;
	
	
	public JsonUserCustomerAccount(){
		
	}
public JsonUserCustomerAccount(UserCustomerAccount userCustomerAccount){
	this.userCustomerAccountId=userCustomerAccount.getUserCustomerAccountId();
	this.userCustomerName = userCustomerAccount.getUserCustomerName();
	this.userCustomerCode=userCustomerAccount.getUserCustomerCode();
	this.userCustomerEmailId=userCustomerAccount.getUserCustomerEmailId();
	if(userCustomerAccount.getUserList() != null) {
		this.userId=userCustomerAccount.getUserList().getUserId();
	}
	if(userCustomerAccount.getCustomer() != null) {
		this.customerId=userCustomerAccount.getCustomer().getCustomerId();
		this.customerName=	userCustomerAccount.getCustomer().getCustomerName();
	}
	if(userCustomerAccount.getValidFromDate()!=null){
		
		this.validFromDate=DateUtility.dateformatWithOutTime(userCustomerAccount.getValidFromDate());
	}
if(userCustomerAccount.getValidTillDate()!=null){
		
		this.validTillDate=DateUtility.dateformatWithOutTime(userCustomerAccount.getValidTillDate());
	}
	}
@JsonIgnore
public UserCustomerAccount getUserCustomerAccount(){
	UserCustomerAccount userCustomerAccount = new UserCustomerAccount();
	userCustomerAccount.setUserCustomerAccountId(userCustomerAccountId);
	userCustomerAccount.setUserCustomerName(userCustomerName);
	userCustomerAccount.setUserCustomerCode(userCustomerCode);
	userCustomerAccount.setUserCustomerEmailId(userCustomerEmailId);
	if(userId != null){
		UserList userList=new UserList();
		userList.setUserId(userId);
		userCustomerAccount.setUserList(userList);
		
	}
	if(customerId != null){
	Customer customer=new Customer();
	customer.setCustomerId(customerId);
		userCustomerAccount.setCustomer(customer);
		
	}
	if(this.validFromDate == null || this.validFromDate.trim().isEmpty()) {
		
		userCustomerAccount.setValidFromDate(DateUtility.getCurrentTime());
	} else {
	
		userCustomerAccount.setValidFromDate(DateUtility.dateformatWithOutTime(this.validFromDate));
	}
	if(this.validTillDate == null || this.validTillDate.trim().isEmpty()) {
		
		userCustomerAccount.setValidTillDate(DateUtility.getCurrentTime());
	} else {
	
		userCustomerAccount.setValidTillDate(DateUtility.dateformatWithOutTime(this.validTillDate));
	}
	
	return userCustomerAccount;
}
public Integer getUserCustomerAccountId() {
	return userCustomerAccountId;
}
public void setUserCustomerAccountId(Integer userCustomerAccountId) {
	this.userCustomerAccountId = userCustomerAccountId;
}
public String getUserCustomerCode() {
	return userCustomerCode;
}
public void setUserCustomerCode(String userCustomerCode) {
	this.userCustomerCode = userCustomerCode;
}
public String getUserCustomerEmailId() {
	return userCustomerEmailId;
}
public void setUserCustomerEmailId(String userCustomerEmailId) {
	this.userCustomerEmailId = userCustomerEmailId;
}
public String getValidFromDate() {
	return validFromDate;
}
public void setValidFromDate(String validFromDate) {
	this.validFromDate = validFromDate;
}
public String getValidTillDate() {
	return validTillDate;
}
public void setValidTillDate(String validTillDate) {
	this.validTillDate = validTillDate;
}
public Integer getUserId() {
	return userId;
}
public void setUserId(Integer userId) {
	this.userId = userId;
}
public Integer getCustomerId() {
	return customerId;
}
public void setCustomerId(Integer customerId) {
	this.customerId = customerId;
}
public String getUserCustomerName() {
	return userCustomerName;
}
public void setUserCustomerName(String userCustomerName) {
	this.userCustomerName = userCustomerName;
}
	

}
