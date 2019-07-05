package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;
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
@Table(name = "risk_likehood_master")
public class RiskLikeHoodMaster implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	@Column(name = "riskLikeHoodId")
	private Integer riskLikeHoodId;
	@Column(name = "likeHoodName")
	private String likeHoodName;
	@Column(name = "likeHoodRating")
	private String likeHoodRating;
	@Column(name = "expectedFrequency")
	private String expectedFrequency;
	@Column(name = "productId")
	private ProductMaster productMaster;
	@Column(name = "createdDate")
	private Date createdDate;
	@Column(name = "modifiedDate")
	private Date modifiedDate;
	@Column(name = "status")
	private Integer status;
	private Set<RiskAssessment> riskAssessment;

	public RiskLikeHoodMaster() {
	}

	@Id
	@Column(name = "riskLikeHoodId")
	@GeneratedValue(strategy = IDENTITY)
	public Integer getRiskLikeHoodId() {
		return riskLikeHoodId;
	}

	public void setRiskLikeHoodId(Integer riskLikeHoodId) {
		this.riskLikeHoodId = riskLikeHoodId;
	}

	public String getLikeHoodName() {
		return likeHoodName;
	}

	public void setLikeHoodName(String likeHoodName) {
		this.likeHoodName = likeHoodName;
	}

	public String getLikeHoodRating() {
		return likeHoodRating;
	}

	public void setLikeHoodRating(String likeHoodRating) {
		this.likeHoodRating = likeHoodRating;
	}

	public String getExpectedFrequency() {
		return expectedFrequency;
	}

	public void setExpectedFrequency(String expectedFrequency) {
		this.expectedFrequency = expectedFrequency;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "productId", nullable = false)
	public ProductMaster getProductMaster() {
		return productMaster;
	}

	public void setProductMaster(ProductMaster productMaster) {
		this.productMaster = productMaster;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "riskLikeHood",cascade=CascadeType.ALL)
	public Set<RiskAssessment> getRiskAssessment() {
		return riskAssessment;
	}

	public void setRiskAssessment(Set<RiskAssessment> riskAssessment) {
		this.riskAssessment = riskAssessment;
	}
	@Override
	public boolean equals(Object o) {
		RiskLikeHoodMaster riskLikeHoodMaster = (RiskLikeHoodMaster) o;
		if (this.riskLikeHoodId == riskLikeHoodMaster.getRiskLikeHoodId()) {
			return true;
		}else{
			return false;
		}
	}
	
	@Override
	public int hashCode(){
	    return (int) riskLikeHoodId;
	  }
}
