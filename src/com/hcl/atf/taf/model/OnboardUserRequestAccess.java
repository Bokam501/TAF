/**
 * 
 */
package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author silambarasur
 *
 */
@Entity
@Table(name="onboard_user_request_access")
public class OnboardUserRequestAccess implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer onboardUserId;
	private String onboardUserName;
	private String emailId;
	private UserRoleMaster userRole;
	private TestFactory testFactory;
	private ProductMaster product;
	private ActivityWorkPackage activityWorkpackage;
	private Integer status;
	
	private UserList approvedBy;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "onboardUserId", unique = true)
	public Integer getOnboardUserId() {
		return onboardUserId;
	}
	@Column(name="onboardUserName")
	public String getOnboardUserName() {
		return onboardUserName;
	}

	@Column(name="emailId")
	public String getEmailId() {
		return emailId;
	}
	
	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn(name = "userRoleId")
	public UserRoleMaster getUserRole() {
		return userRole;
	}
	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn(name = "testFactoryId")
	public TestFactory getTestFactory() {
		return testFactory;
	}
	
	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn(name = "productId")
	public ProductMaster getProduct() {
		return product;
	}
	
	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn(name = "activityWorkpackageId")
	public ActivityWorkPackage getActivityWorkpackage() {
		return activityWorkpackage;
	}
	@Column(name="status")
	public Integer getStatus() {
		return status;
	}
	
	
	
	
	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn(name = "approvedBy")
	public UserList getApprovedBy() {
		return approvedBy;
	}
	
	public void setApprovedBy(UserList approvedBy) {
		this.approvedBy = approvedBy;
	}
	public void setOnboardUserId(Integer onboardUserId) {
		this.onboardUserId = onboardUserId;
	}
	
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	
	public void setOnboardUserName(String onboardUserName) {
		this.onboardUserName = onboardUserName;
	}
	
	public void setUserRole(UserRoleMaster userRole) {
		this.userRole = userRole;
	}
	public void setProduct(ProductMaster product) {
		this.product = product;
	}
	public void setActivityWorkpackage(ActivityWorkPackage activityWorkpackage) {
		this.activityWorkpackage = activityWorkpackage;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public void setTestFactory(TestFactory testFactory) {
		this.testFactory = testFactory;
	}
}
