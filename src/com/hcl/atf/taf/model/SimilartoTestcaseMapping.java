/**
 * 
 */
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

/**
 * @author silambarasur
 *
 */
@Entity
@Table(name="similar_to_testcase_mapping")
public class SimilartoTestcaseMapping implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private Integer testCaseId;
	private Integer similarToTestCaseId;
	private ProductMaster product;
	private Date createdDate;
	private Date modifiedDate;
	private UserList createdBy;
	private UserList modifiedBy;
	private Integer isMapped; 
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}
	
	@Column(name="testCaseId")
	public Integer getTestCaseId() {
		return testCaseId;
	}
	

	@Column(name="similarToTestCaseId")
	public Integer getSimilarToTestCaseId() {
		return similarToTestCaseId;
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


	public void setSimilarToTestCaseId(Integer similarToTestCaseId) {
		this.similarToTestCaseId = similarToTestCaseId;
	}
	
	


}
