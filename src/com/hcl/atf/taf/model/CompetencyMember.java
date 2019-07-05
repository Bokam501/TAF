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

@Entity
@Table(name = "competency_member")
public class CompetencyMember implements Serializable {

	private static final long serialVersionUID = 8662869424402951037L;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "competencyMemberId", unique = true, nullable = false)
	private Integer competencyMemberId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "competencyId")
	private DimensionMaster dimensionId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	private UserList userId;
	
	@Column(name = "startDate")
	private Date startDate;
	
	@Column(name = "endDate")
	private Date endDate;
	
	@Column(name = "status")
	private Integer status;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mappedBy")
	private UserList mappedBy;
	
	@Column(name = "mappedDate")
	private Date mappedDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "modifiedBy")
	private UserList modifiedBy;
	
	@Column(name = "modifiedDate")
	private Date modifiedDate;
	
	public Integer getCompetencyMemberId() {
		return competencyMemberId;
	}
	public void setCompetencyMemberId(Integer competencyMemberId) {
		this.competencyMemberId = competencyMemberId;
	}
	public DimensionMaster getDimensionId() {
		return dimensionId;
	}
	public void setDimensionId(DimensionMaster dimensionId) {
		this.dimensionId = dimensionId;
	}
	public UserList getUserId() {
		return userId;
	}
	public void setUserId(UserList userId) {
		this.userId = userId;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public UserList getMappedBy() {
		return mappedBy;
	}
	public void setMappedBy(UserList mappedBy) {
		this.mappedBy = mappedBy;
	}
	public Date getMappedDate() {
		return mappedDate;
	}
	public void setMappedDate(Date mappedDate) {
		this.mappedDate = mappedDate;
	}
	public UserList getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(UserList modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	
}
