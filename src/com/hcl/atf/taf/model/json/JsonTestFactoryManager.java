package com.hcl.atf.taf.model.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.TestFactory;
import com.hcl.atf.taf.model.TestFactoryManager;
import com.hcl.atf.taf.model.UserList;

public class JsonTestFactoryManager {
	 
	@JsonProperty
	private Integer testFactoryManagerId;
	  @JsonProperty
	private Integer status;
	  @JsonProperty
    private String createdDate;
	  @JsonProperty
	private Integer userId;
	  @JsonProperty
	  private String userName;
	  @JsonProperty
	private Integer testFactoryId;
	  @JsonProperty
	  private String loginId;
	  @JsonProperty
	  private String userDisplayName;
	  
	public JsonTestFactoryManager(){
		
	}  
public JsonTestFactoryManager(TestFactoryManager testFactoryManager){
	testFactoryManagerId = testFactoryManager.getTestFactoryManagerId();
	
	if(testFactoryManager.getStatus() != null){
		this.status = testFactoryManager.getStatus();
	}
	
	if(testFactoryManager.getCreatedDate()!=null){
		this.createdDate = DateUtility.dateformatWithOutTime(testFactoryManager.getCreatedDate());
	}
	if(testFactoryManager.getUserList()!=null){
		this.userId=testFactoryManager.getUserList().getUserId();
		if(testFactoryManager.getUserList().getLoginId() != null)
			this.userName = testFactoryManager.getUserList().getLoginId();
	}
	if(testFactoryManager.getUserList()!=null){
		this.userDisplayName=testFactoryManager.getUserList().getUserDisplayName();
	}
	if(testFactoryManager.getTestFactory()!=null){
		this.testFactoryId=testFactoryManager.getTestFactory().getTestFactoryId();
	}
		
	}  

@JsonIgnore
public TestFactoryManager getTestFactoryManager() {
	TestFactoryManager testFactoryManager = new TestFactoryManager();
	if (testFactoryManagerId != null) {
		testFactoryManager.setTestFactoryManagerId(testFactoryManagerId);
	}
	if(this.testFactoryId!=null){
    	TestFactory testFactory=new TestFactory();
    	testFactory.setTestFactoryId(testFactoryId);
    	testFactoryManager.setTestFactory(testFactory);
   }
	if(this.userId!=null){
    	UserList user=new UserList();
    	user.setUserId(userId);
testFactoryManager.setUserList(user);
   }
	
	if(this.status != null ){

		testFactoryManager.setStatus(status);			
	}else{
		testFactoryManager.setStatus(0);	
	}
	if(this.createdDate == null || this.createdDate.trim().isEmpty()) {
	
		testFactoryManager.setCreatedDate(DateUtility.getCurrentTime());
	} else {
	
		testFactoryManager.setCreatedDate(DateUtility.dateFormatWithOutSeconds(this.createdDate));
	}

	return testFactoryManager;
}
public Integer getTestFactoryManagerId() {
	return testFactoryManagerId;
}
public void setTestFactoryManagerId(Integer testFactoryManagerId) {
	this.testFactoryManagerId = testFactoryManagerId;
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
public Integer getUserId() {
	return userId;
}
public void setUserId(Integer userId) {
	this.userId = userId;
}
public String getUserName() {
	return userName;
}
public void setUserName(String userName) {
	this.userName = userName;
}
public Integer getTestFactoryId() {
	return testFactoryId;
}
public void setTestFactoryId(Integer testFactoryId) {
	this.testFactoryId = testFactoryId;
}
public String getLoginId() {
	return loginId;
}
public void setLoginId(String loginId) {
	this.loginId = loginId;
}
public String getUserDisplayName() {
	return userDisplayName;
}
public void setUserDisplayName(String userDisplayName) {
	this.userDisplayName = userDisplayName;
}

}
