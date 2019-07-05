/**
 * 
 */
package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/**
 * @author silambarasur
 *
 */
@Entity
@Table(name="testcase_entity_group")
public class TestCaseEntityGroup implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer testCaseEntityGroupId;
	private String testCaseEntityGroupName;
	private String description;
	private TestCaseEntityGroup parentEntityGroupId;
	private TestcaseTypeMaster type;
	private Date createdDate;
	private Date modifiedDate;
	private UserList createdBy;
	private UserList modifiedBy;
	private ProductMaster product;
	
	
	@Column(name = "leftIndex")
	private Integer leftIndex;
	@Column(name = "rightIndex")
	private Integer rightIndex;
	
	private Set<TestCaseList> testCaseList = new HashSet<TestCaseList>(0);
		
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "testCaseEntityGroupId", unique = true, nullable = false)
	public Integer getTestCaseEntityGroupId() {
		return testCaseEntityGroupId;
	}
	
	@Column(name="testCaseEntityGroupName")
	public String getTestCaseEntityGroupName() {
		return testCaseEntityGroupName;
	}
	
	@Column(name="description")
	public String getDescription() {
		return description;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parentEntityGroupId" )
	@NotFound(action=NotFoundAction.IGNORE)
	public TestCaseEntityGroup getParentEntityGroupId() {
		return parentEntityGroupId;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "type")
	public TestcaseTypeMaster getType() {
		return type;
	}
	
	@Column(name="createdDate")
	public Date getCreatedDate() {
		return createdDate;
	}
	
	@Column(name="modifiedDate")
	public Date getModifiedDate() {
		return modifiedDate;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "createdby")
	public UserList getCreatedBy() {
		return createdBy;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "modifiedby")
	public UserList getModifiedBy() {
		return modifiedBy;
	}
	
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "testcase_EntityGroup_has_test_case_list", joinColumns = { @JoinColumn(name = "testCaseEntityGroupId", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "testCaseId", nullable = false, updatable = false) })
	public Set<TestCaseList> getTestCaseList() {
		return testCaseList;
	}
	
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "productId")
	public ProductMaster getProduct() {
		return product;
	}

	
	public Integer getLeftIndex() {
		return leftIndex;
	}

	public Integer getRightIndex() {
		return rightIndex;
	}

	public void setLeftIndex(Integer leftIndex) {
		this.leftIndex = leftIndex;
	}

	public void setRightIndex(Integer rightIndex) {
		this.rightIndex = rightIndex;
	}

	public void setTestCaseEntityGroupId(Integer testCaseEntityGroupId) {
		this.testCaseEntityGroupId = testCaseEntityGroupId;
	}
	public void setTestCaseEntityGroupName(String testCaseEntityGroupName) {
		this.testCaseEntityGroupName = testCaseEntityGroupName;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setParentEntityGroupId(TestCaseEntityGroup parentEntityGroupId) {
		this.parentEntityGroupId = parentEntityGroupId;
	}
	public void setType(TestcaseTypeMaster type) {
		this.type = type;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public void setCreatedBy(UserList createdBy) {
		this.createdBy = createdBy;
	}
	public void setModifiedBy(UserList modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	
	public void setProduct(ProductMaster product) {
		this.product = product;
	}

	public void setTestCaseList(Set<TestCaseList> testCaseList) {
		this.testCaseList = testCaseList;
	}	
	
	

}
