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
@Table(name="productFeature_productBuild_mapping")
public class ProductFeatureProductBuildMapping implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private Integer featureId;
	private Integer buildId;
	private ProductMaster product;
	private Date createdDate;
	private Date modifiedDate;
	private UserList createdBy;
	private UserList modifiedBy;
	private Integer isMapped; 
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}
	

	@Column(name="featureId")
	public Integer getFeatureId() {
		return featureId;
	}

	@Column(name="buildId")
	public Integer getBuildId() {
		return buildId;
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

	public void setFeatureId(Integer featureId) {
		this.featureId = featureId;
	}

	public void setBuildId(Integer buildId) {
		this.buildId = buildId;
	}
	
	
	

}
