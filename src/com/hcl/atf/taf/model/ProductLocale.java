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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "product_locale")
public class ProductLocale implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer productLocaleId;
	@Column(name = "localeName")
	private String localeName;
	@Column(name = "description")
	private String description;
	@Column(name = "createdDate")
	private Date createdDate;
	@Column(name = "modifiedDate")
	private Date modifiedDate;
	
	private Integer status;
	
	private ProductMaster productMaster;
	private Set<WorkPackage> workPackageList = new HashSet<WorkPackage>(0);
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "productLocaleId", unique = true, nullable = false)
	public Integer getProductLocaleId() {
		return productLocaleId;
	}

	public void setProductLocaleId(Integer productLocaleId) {
		this.productLocaleId = productLocaleId;
	}

	public String getLocaleName() {
		return localeName;
	}

	public void setLocaleName(String localeName) {
		this.localeName = localeName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	@Temporal(TemporalType.TIMESTAMP)
	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	@Column(name = "status")
	public Integer getStatus() {
		return status;
	}
	
	public void setStatus(Integer status) {
		this.status = status;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "productId") 
	public ProductMaster getProductMaster() {
		return productMaster;
	}

	public void setProductMaster(ProductMaster productMaster) {
		this.productMaster = productMaster;
	}

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "localeList",cascade=CascadeType.ALL)
	public Set<WorkPackage> getWorkPackageList() {
		return workPackageList;
	}

	public void setWorkPackageList(Set<WorkPackage> workPackageList) {
		this.workPackageList = workPackageList;
	}

	@Override
	public boolean equals(Object o) {
		
		ProductLocale e = (ProductLocale) o;
		if (this.productLocaleId == e.productLocaleId)
			return true;
		else
			return false;
	}
}
