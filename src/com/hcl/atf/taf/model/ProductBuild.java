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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "product_build")
public class ProductBuild implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	private Integer productBuildId;
	
	@Column(name = "buildNo")
	private String buildNo;
	@Column(name = "buildName")
	private String buildname;
	@Column(name = "buildDescription")
	private String buildDescription;
	@Column(name = "buildDate")
	private Date buildDate;
	private Integer status;
	private ProductBuild clonedBuild;
	private DefectIdentificationStageMaster buildType;
	private Date createdDate;
	private Date modifiedDate;
	private ProductMaster productMaster;
	
	
	private ProductVersionListMaster productVersion;
	private Set<WorkPackage> workPackage=new HashSet<WorkPackage>();
	public ProductBuild() {
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "productBuildId", unique = true, nullable = false)
	public Integer getProductBuildId() {
		return this.productBuildId;
	}

	public void setProductBuildId(Integer productBuildId) {
		this.productBuildId = productBuildId;
	}
	
	public String getBuildname() {
		return buildname;
	}

	public void setBuildname(final String paramBuildname) {
		buildname = paramBuildname;
	}

	public String getBuildNo() {
		return buildNo;
	}

	public void setBuildNo(String buildNo) {
		this.buildNo = buildNo;
	}

	public String getBuildDescription() {
		return buildDescription;
	}

	public void setBuildDescription(String buildDescription) {
		this.buildDescription = buildDescription;
	}

	public Date getBuildDate() {
		return buildDate;
	}

	public void setBuildDate(Date paramBuilddate) {
		this.buildDate = paramBuilddate;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "productVersionId", nullable = true) 
	public ProductVersionListMaster getProductVersion() {
		return productVersion;
	}

	public void setProductVersion(ProductVersionListMaster productVersion) {
		this.productVersion = productVersion;
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
	
	@Column(name = "status")
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	@Override
	public boolean equals(Object o) {
		ProductBuild productBuild = (ProductBuild) o;
		if (this.productBuildId == productBuild.getProductBuildId()) {
			return true;
		}else{
			return false;
		}
	}
	
	@Override
	public int hashCode(){
	    return (int) productBuildId;
	  }
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "productBuild",cascade=CascadeType.ALL)
	public Set<WorkPackage> getWorkPackage() {
		return workPackage;
	}

	public void setWorkPackage(Set<WorkPackage> workPackage) {
		this.workPackage = workPackage;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "buildTypeId")
	public DefectIdentificationStageMaster getBuildType() {
		return buildType;
	}

	public void setBuildType(DefectIdentificationStageMaster buildType) {
		this.buildType = buildType;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "clonedBuildId")
	public ProductBuild getClonedBuild() {
		return clonedBuild;
	}

	public void setClonedBuild(ProductBuild clonedBuild) {
		this.clonedBuild = clonedBuild;
	}

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "productId")
	public ProductMaster getProductMaster() {
		return productMaster;
	}

	public void setProductMaster(ProductMaster productMaster) {
		this.productMaster = productMaster;
	}
	
}
