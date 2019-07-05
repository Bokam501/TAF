package com.hcl.atf.taf.model;


import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;


@Entity
@Table(name = "decoupling_category")
public class DecouplingCategory implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	private Integer decouplingCategoryId;
	@Column(name = "decouplingCategoryName")
	private String decouplingCategoryName;
	@Column(name = "displayName")
	private String displayName;
	@Column(name = "description")
	private String description;
	@Column(name = "status")
	private Integer status;
	@Column(name = "createdDate")
	private Date createdDate;
	@Column(name = "modifiedDate")
	private Date modifiedDate;
	@Column(name = "leftIndex")
	private Integer leftIndex;
	@Column(name = "rightIndex")
	private Integer rightIndex;

	private ProductMaster product;
	private DecouplingCategory parentCategory;
	private UserTypeMasterNew userTypeMasterNew;
	private Set<DecouplingCategory> childCategories = new HashSet<DecouplingCategory>(0);
	
	public DecouplingCategory() {
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "decouplingCategoryId", unique = true, nullable = false)
	public Integer getDecouplingCategoryId() {
		return decouplingCategoryId;
	}

	public void setDecouplingCategoryId(Integer decouplingCategoryId) {
		this.decouplingCategoryId = decouplingCategoryId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parentCategoryId" )
	@NotFound(action=NotFoundAction.IGNORE)
	public DecouplingCategory getParentCategory() {
		return parentCategory;
	}	

	public void setParentCategory(DecouplingCategory parentCategory) {
		this.parentCategory = parentCategory;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parentCategory", cascade=CascadeType.ALL)
	public Set<DecouplingCategory> getChildCategories() {
		return this.childCategories;
	}

	public void setChildCategories(Set<DecouplingCategory> childCategories) {
		this.childCategories = childCategories;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createdDate")
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modifiedDate")
	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getDecouplingCategoryName() {
		return decouplingCategoryName;
	}

	public void setDecouplingCategoryName(
			final String paramDecouplingCategoryName) {
		decouplingCategoryName = paramDecouplingCategoryName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String paramDescription) {
		description = paramDescription;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "productId")
	public ProductMaster getProduct() {
		return product;
	}

	public void setProduct(final ProductMaster paramProduct) {
		product = paramProduct;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userTypeId")
	public UserTypeMasterNew getUserTypeMasterNew() {
		return userTypeMasterNew;
	}

	public void setUserTypeMasterNew(UserTypeMasterNew userTypeMasterNew) {
		this.userTypeMasterNew = userTypeMasterNew;
	}

	private Set<TestCaseList> testCaseList = new HashSet<TestCaseList>(0);
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "decouplingcategory_has_test_case_list", joinColumns = { @JoinColumn(name = "decouplingCategoryId") }, inverseJoinColumns = { @JoinColumn(name = "testCaseId") })
	public Set<TestCaseList> getTestCaseList() {
		return this.testCaseList;
	}

	public void setTestCaseList(Set<TestCaseList> testCaseList) {
		this.testCaseList = testCaseList;
	}	

	@Override
	public boolean equals(Object o) {		
		DecouplingCategory dc = (DecouplingCategory) o;
		if (this.decouplingCategoryId == dc.getDecouplingCategoryId())
			return true;
		else
			return false;
	}	
	
	@Override
	public int hashCode(){
	    return (int) decouplingCategoryId;
	  }

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public Integer getLeftIndex() {
		return leftIndex;
	}

	public void setLeftIndex(Integer leftIndex) {
		this.leftIndex = leftIndex;
	}

	public Integer getRightIndex() {
		return rightIndex;
	}

	public void setRightIndex(Integer rightIndex) {
		this.rightIndex = rightIndex;
	}
}
