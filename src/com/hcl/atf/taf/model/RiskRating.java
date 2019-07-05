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
@Table(name = "risk_rating")
public class RiskRating implements java.io.Serializable{

	private static final long serialVersionUID = 1L;

	@Column(name = "riskRatingId")
	private Integer riskRatingId;
	@Column(name = "ratingName")
	private String ratingName;
	@Column(name = "ratingDescription")
	private String ratingDescription;
	@Column(name = "colour")
	private String colour;
	@Column(name = "createdDate")
	private Date createdDate;
	@Column(name = "modifiedDate")
	private Date modifiedDate;
	@Column(name = "status")
	private Integer status;
	@Column(name = "productId")
	private ProductMaster productMaster;
	private Set<RiskAssessment> riskAssessment;

	public RiskRating() {
	}

	@Id
	@Column(name = "riskRatingId")
	@GeneratedValue(strategy = IDENTITY)
	public Integer getRiskRatingId() {
		return riskRatingId;
	}

	public void setRiskRatingId(Integer riskRatingId) {
		this.riskRatingId = riskRatingId;
	}

	public String getRatingName() {
		return ratingName;
	}

	public void setRatingName(String ratingName) {
		this.ratingName = ratingName;
	}

	public String getRatingDescription() {
		return ratingDescription;
	}

	public void setRatingDescription(String ratingDescription) {
		this.ratingDescription = ratingDescription;
	}

	public String getColour() {
		return colour;
	}

	public void setColour(String colour) {
		this.colour = colour;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "productId", nullable = false)
	public ProductMaster getProductMaster() {
		return productMaster;
	}

	public void setProductMaster(ProductMaster productMaster) {
		this.productMaster = productMaster;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "riskRating",cascade=CascadeType.ALL)
	public Set<RiskAssessment> getRiskAssessment() {
		return riskAssessment;
	}

	public void setRiskAssessment(Set<RiskAssessment> riskAssessment) {
		this.riskAssessment = riskAssessment;
	}

}
