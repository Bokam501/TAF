package com.hcl.atf.taf.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "testdata_items")
public class TestDataItems implements Serializable{

	private Integer testDataItemId;	
	private String dataName;
	private String type;
	private String remarks;
	private Date createdDate;
	private Date modifiedDate;
	private String groupName;
	private ProductMaster productMaster;
	private UserList userlist;
	private Integer isShare;
	private Set<TestDataItemValues> testDataItemsValSet;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "testDataItemId", unique = true, nullable = false)
	public Integer getTestDataItemId() {
		return testDataItemId;
	}
	public void setTestDataItemId(Integer testDataItemId) {
		this.testDataItemId = testDataItemId;
	}
	
	@Column(name = "dataName")
	public String getDataName() {
		return dataName;
	}
	public void setDataName(String dataName) {
		this.dataName = dataName;
	}
	
	@Column(name = "typeName")
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	@Column(name = "remarks")
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createdDate")
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modifiedDate")
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="productId")
	public ProductMaster getProductMaster() {
		return productMaster;
	}
	public void setProductMaster(ProductMaster productMaster) {
		this.productMaster = productMaster;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="createdBy")
	public UserList getUserlist() {
		return userlist;
	}
	public void setUserlist(UserList userlist) {
		this.userlist = userlist;
	}
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="testDataItems", cascade={CascadeType.ALL})
	public Set<TestDataItemValues> getTestDataItemsValSet() {
		return testDataItemsValSet;
	}
	public void setTestDataItemsValSet(Set<TestDataItemValues> testDataItemsValSet) {
		this.testDataItemsValSet = testDataItemsValSet;
	}
	
	@Column(name = "groupName")
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	@Column(name = "isShare")
	public Integer getIsShare() {
		return isShare;
	}
	public void setIsShare(Integer isShare) {
		this.isShare = isShare;
	}
	
	
	
}
