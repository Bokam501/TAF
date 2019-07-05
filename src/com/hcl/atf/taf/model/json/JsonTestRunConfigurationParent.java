package com.hcl.atf.taf.model.json;



import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.TestRunConfigurationParent;
import com.hcl.atf.taf.model.UserList;


public class JsonTestRunConfigurationParent implements java.io.Serializable {

	@JsonProperty
	private Integer testRunConfigurationParentId;
	
	@JsonProperty
	private Integer productId;
	@JsonProperty
	private String productName;
	@JsonProperty
	private Integer userId;
	@JsonProperty
	private String userName;
	@JsonProperty
	private String testRunConfigurationName;
	@JsonProperty
	private String testRunConfigurationDescription;


	public JsonTestRunConfigurationParent() {
	}

	public JsonTestRunConfigurationParent(TestRunConfigurationParent testRunConfigurationParent) {
		
		this.testRunConfigurationParentId=testRunConfigurationParent.getTestRunConfigurationParentId();
		if(testRunConfigurationParent.getProductMaster()!=null){
			this.productId=testRunConfigurationParent.getProductMaster().getProductId();
			this.productName=testRunConfigurationParent.getProductMaster().getProductName();
		}
		if(testRunConfigurationParent.getUserList()!=null){
			this.userId=testRunConfigurationParent.getUserList().getUserId();
			this.userName = testRunConfigurationParent.getUserList().getFirstName();
		}
		this.testRunConfigurationName=testRunConfigurationParent.getTestRunconfigurationName();
		this.testRunConfigurationDescription=testRunConfigurationParent.getTestRunConfigurationDescription();
	
	}


	public Integer getTestRunConfigurationParentId() {
		return testRunConfigurationParentId;
	}

	public void setTestRunConfigurationParentId(Integer testRunConfigurationParentId) {
		this.testRunConfigurationParentId = testRunConfigurationParentId;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
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

	public String getTestRunConfigurationName() {
		return testRunConfigurationName;
	}

	public void setTestRunConfigurationName(String testRunConfigurationName) {
		this.testRunConfigurationName = testRunConfigurationName;
	}

	public String getTestRunConfigurationDescription() {
		return testRunConfigurationDescription;
	}

	public void setTestRunConfigurationDescription(
			String testRunConfigurationDescription) {
		this.testRunConfigurationDescription = testRunConfigurationDescription;
	}

	@JsonIgnore
	public TestRunConfigurationParent getTestRunConfigurationParent(){
		TestRunConfigurationParent testRunConfigurationParent = new TestRunConfigurationParent();
		testRunConfigurationParent.setTestRunConfigurationParentId(testRunConfigurationParentId);
		
		
		ProductMaster productMaster = new ProductMaster();
		if(productId!=null){
		productMaster.setProductId(productId);
		}
		testRunConfigurationParent.setProductMaster(productMaster);
		
		
		UserList userList = new UserList();
		if(userId!=null){
		userList.setUserId(userId);
		}
		testRunConfigurationParent.setUserList(userList);
		
		testRunConfigurationParent.setTestRunconfigurationName(testRunConfigurationName);
		testRunConfigurationParent.setTestRunConfigurationDescription(testRunConfigurationDescription);
				
		return testRunConfigurationParent;
	}


}
