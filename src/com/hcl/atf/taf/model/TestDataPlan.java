package com.hcl.atf.taf.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="test_data_plan")
public class TestDataPlan implements Serializable{
	private static final long serialVersionUID = 1L;
	private Integer testDataPlanId;
	private String testDataPlanName;
	private String testDataPlanDescription;
	private Date createdOn;
	private UserList userlist;
	private ProductMaster productMaster;
	public TestDataPlan() {

	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="testDataPlanId", unique=true, nullable=false)
	public Integer getTestDataPlanId() {
		return testDataPlanId;
	}
	public void setTestDataPlanId(Integer testDataPlanId) {
		this.testDataPlanId = testDataPlanId;
	}

	@Column(name="testDataPlanName")
	public String getTestDataPlanName() {
		return testDataPlanName;
	}
	public void setTestDataPlanName(String testDataPlanName) {
		this.testDataPlanName = testDataPlanName;
	}

	@Column(name="testDataPlanDescription")
	public String getTestDataPlanDescription() {
		return testDataPlanDescription;
	}


	public void setTestDataPlanDescription(String testDataPlanDescription) {
		this.testDataPlanDescription = testDataPlanDescription;
	}

	@Temporal(value=TemporalType.TIMESTAMP)
	@Column(name="createdOn")
	public Date getCreatedOn() {
		return createdOn;
	}


	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="createdBy")
	public UserList getUserlist() {
		return userlist;
	}


	public void setUserlist(UserList userlist) {
		this.userlist = userlist;
	}

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="productId")
	public ProductMaster getProductMaster() {
		return productMaster;
	}

	public void setProductMaster(ProductMaster productMaster) {
		this.productMaster = productMaster;
	}


}
