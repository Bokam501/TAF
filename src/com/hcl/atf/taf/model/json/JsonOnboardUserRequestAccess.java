/**
 * 
 */
package com.hcl.atf.taf.model.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.ActivityWorkPackage;
import com.hcl.atf.taf.model.OnboardUserRequestAccess;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.TestFactory;
import com.hcl.atf.taf.model.UserRoleMaster;

/**
 * @author silambarasur
 * 
 */
public class JsonOnboardUserRequestAccess {

	@JsonProperty
	private Integer onboardUserId;
	@JsonProperty
	private String onboardUserName;
	@JsonProperty
	private String emailId;
	@JsonProperty
	private String userRoleName;
	@JsonProperty
	private Integer userRoleId;
	@JsonProperty
	private Integer testFactoryId;
	@JsonProperty
	private String testFactoryName;	
	@JsonProperty
	private Integer productId;
	@JsonProperty
	private String productName;
	@JsonProperty
	private Integer activityWorkpackageId;
	@JsonProperty
	private String activityWorkpackageName;
	@JsonProperty
	private Integer status;

	public Integer getOnboardUserId() {
		return onboardUserId;
	}

	public String getOnboardUserName() {
		return onboardUserName;
	}

	public String getEmailId() {
		return emailId;
	}

	public String getUserRoleName() {
		return userRoleName;
	}

	public Integer getUserRoleId() {
		return userRoleId;
	}

	public Integer getProductId() {
		return productId;
	}

	public String getProductName() {
		return productName;
	}

	public Integer getActivityWorkpackageId() {
		return activityWorkpackageId;
	}

	public String getActivityWorkpackageName() {
		return activityWorkpackageName;
	}

	public Integer getStatus() {
		return status;
	}

	public void setOnboardUserId(Integer onboardUserId) {
		this.onboardUserId = onboardUserId;
	}

	public void setOnboardUserName(String onboardUserName) {
		this.onboardUserName = onboardUserName;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public void setUserRoleName(String userRoleName) {
		this.userRoleName = userRoleName;
	}

	public void setUserRoleId(Integer userRoleId) {
		this.userRoleId = userRoleId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public void setActivityWorkpackageId(Integer activityWorkpackageId) {
		this.activityWorkpackageId = activityWorkpackageId;
	}

	public void setActivityWorkpackageName(String activityWorkpackageName) {
		this.activityWorkpackageName = activityWorkpackageName;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getTestFactoryId() {
		return testFactoryId;
	}
	public void setTestFactoryId(Integer testFactoryId) {
		this.testFactoryId = testFactoryId;
	}
	public String getTestFactoryName() {
		return testFactoryName;
	}

	public void setTestFactoryName(String testFactoryName) {
		this.testFactoryName = testFactoryName;
	}

	
	public JsonOnboardUserRequestAccess(){
				
	}
	public JsonOnboardUserRequestAccess(
			OnboardUserRequestAccess onboardUserRequestAccess) {
		onboardUserId = onboardUserRequestAccess.getOnboardUserId();
		onboardUserName = onboardUserRequestAccess.getOnboardUserName();
		
		if (onboardUserRequestAccess.getTestFactory() != null) {
			testFactoryId = onboardUserRequestAccess.getTestFactory().getTestFactoryId();
			testFactoryName = onboardUserRequestAccess.getTestFactory().getTestFactoryName();
					
		}
		if (onboardUserRequestAccess.getProduct() != null) {
			productId = onboardUserRequestAccess.getProduct().getProductId();
			productName = onboardUserRequestAccess.getProduct()
					.getProductName();
		}
		if (onboardUserRequestAccess.getActivityWorkpackage() != null) {
			activityWorkpackageId = onboardUserRequestAccess
					.getActivityWorkpackage().getActivityWorkPackageId();
			activityWorkpackageName = onboardUserRequestAccess
					.getActivityWorkpackage().getActivityWorkPackageName();
		}
		if (onboardUserRequestAccess.getUserRole() != null) {
			userRoleId = onboardUserRequestAccess.getUserRole().getUserRoleId();
			userRoleName = onboardUserRequestAccess.getUserRole().getRoleName();
		}
		emailId = onboardUserRequestAccess.getEmailId();
		status = onboardUserRequestAccess.getStatus();
	}

	@JsonIgnore
	public OnboardUserRequestAccess getOnboardUserRequestAccessList() {
		OnboardUserRequestAccess onboardUserRequestAccess = new OnboardUserRequestAccess();
		onboardUserRequestAccess.setOnboardUserId(onboardUserId);
		onboardUserRequestAccess.setOnboardUserName(onboardUserName);
		onboardUserRequestAccess.setEmailId(emailId);
		onboardUserRequestAccess.setStatus(status);
		if (userRoleId != null) {
			UserRoleMaster userRole = new UserRoleMaster();
			userRole.setUserRoleId(userRoleId);
			userRole.setRoleName(userRoleName);
			onboardUserRequestAccess.setUserRole(userRole);
		}
		
		if(testFactoryId != null){
			TestFactory testFactory = new TestFactory();
			testFactory.setTestFactoryId(testFactoryId);
			testFactory.setTestFactoryName(testFactoryName);
			onboardUserRequestAccess.setTestFactory(testFactory);
			
		}
		
		if (productId != null) {
			ProductMaster product = new ProductMaster();
			product.setProductId(productId);
			product.setProductName(productName);
			onboardUserRequestAccess.setProduct(product);
		}

		if (activityWorkpackageId != null) {
			ActivityWorkPackage activityWorkpackage = new ActivityWorkPackage();
			activityWorkpackage.setActivityWorkPackageId(activityWorkpackageId);
			activityWorkpackage.setActivityWorkPackageName(activityWorkpackageName);
			onboardUserRequestAccess.setActivityWorkpackage(activityWorkpackage);
		}
		return onboardUserRequestAccess;
	}

}
