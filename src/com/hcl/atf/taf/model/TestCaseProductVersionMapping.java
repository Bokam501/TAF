/**
 * 
 */
package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;

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
@Table(name="test_case_list_has_product_version_list")
public class TestCaseProductVersionMapping implements Serializable{
	
	
	private Integer id;
	private Integer testCaseId;
	private Integer versionId;
	private ProductMaster product;
	private Date createdDate;
	private Date modifiedDate;
	private UserList createdBy;
	private UserList modifiedBy;
	private Integer isMapped; 
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	public Integer getId() {
		return id;
	}
		
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "productId")
	public ProductMaster getProduct() {
		return product;
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
	@JoinColumn(name = "createdBy")
	public UserList getCreatedBy() {
		return createdBy;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "modifiedBy")
	public UserList getModifiedBy() {
		return modifiedBy;
	}
	
	@Column(name="isMapped")
	public Integer getIsMapped() {
		return isMapped;
	}
	
	@Column(name="testCaseId")
	public Integer getTestCaseId() {
		return testCaseId;
	}

	@Column(name="productVersionListId")
	public Integer getVersionId() {
		return versionId;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public void setProduct(ProductMaster product) {
		this.product = product;
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


	public void setIsMapped(Integer isMapped) {
		this.isMapped = isMapped;
	}

	public void setTestCaseId(Integer testCaseId) {
		this.testCaseId = testCaseId;
	}

	
	public void setVersionId(Integer versionId) {
		this.versionId = versionId;
	}
	

}
