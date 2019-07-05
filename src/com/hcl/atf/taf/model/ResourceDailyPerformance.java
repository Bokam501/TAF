package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.lang.Integer;
import java.lang.String;
import java.util.Date;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: ResourceDailyPerformance
 *
 */
@Entity
@Table(name="resource_daily_performance")
public class ResourceDailyPerformance implements Serializable {

	
	private Integer resourcePerformanceId;
	private UserList user;
	private Date workDate;
	private ActualShift actualShift;
	private WorkPackage workPackage;
	private PerformanceLevel performanceLevel;
	private String raterComments;
	private UserList ratedByUser;
	private Date ratedOn;
	private Integer isRatingApproved;
	private UserList approvedByUser;
	private Date approvedOn;
	private String approverComments;
	private static final long serialVersionUID = 1L;

	public ResourceDailyPerformance() {
		super();
	}
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "resourcePerformanceId", unique = true, nullable = false)
	public Integer getResourcePerformanceId() {
		return this.resourcePerformanceId;
	}

	public void setResourcePerformanceId(Integer resourcePerformanceId) {
		this.resourcePerformanceId = resourcePerformanceId;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	public UserList getUser() {
		return this.user;
	}

	public void setUser(UserList user) {
		this.user = user;
	}
	
	@Column(name = "workdate", length = 45)
	public Date getWorkDate() {
		return this.workDate;
	}

	public void setWorkDate(Date workDate) {
		this.workDate = workDate;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "actualShiftId")
	public ActualShift getActualShift() {
		return this.actualShift;
	}

	public void setActualShift(ActualShift actualShift) {
		this.actualShift = actualShift;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "workPackageId")
	public WorkPackage getWorkPackage() {
		return this.workPackage;
	}

	public void setWorkPackage(WorkPackage workPackage) {
		this.workPackage = workPackage;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "performanceLevelId")
	public PerformanceLevel getPerformanceLevel() {
		return this.performanceLevel;
	}

	public void setPerformanceLevel(PerformanceLevel performanceLevel) {
		this.performanceLevel = performanceLevel;
	}
	
	@Column(name = "raterComments", length = 225)
	public String getRaterComments() {
		return this.raterComments;
	}

	public void setRaterComments(String raterComments) {
		this.raterComments = raterComments;
	}
	
	@Column(name = "ratedOn", length = 45)
	public Date getRatedOn() {
		return this.ratedOn;
	}

	public void setRatedOn(Date ratedOn) {
		this.ratedOn = ratedOn;
	}
	
	
	@Column(name = "approvedOn", length = 45)
	public Date getApprovedOn() {
		return this.approvedOn;
	}

	public void setApprovedOn(Date approvedOn) {
		this.approvedOn = approvedOn;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ratedByUserId")
	public UserList getRatedByUser() {
		return ratedByUser;
	}
	
	public void setRatedByUser(UserList ratedByUser) {
		this.ratedByUser = ratedByUser;
	}

	@Column(name = "isRatingApproved")
	public Integer getIsRatingApproved() {
		return isRatingApproved;
	}
	public void setIsRatingApproved(Integer isRatingApproved) {
		this.isRatingApproved = isRatingApproved;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "approvedByUserId")
	public UserList getApprovedByUser() {
		return approvedByUser;
	}
	
	public void setApprovedByUser(UserList approvedByUser) {
		this.approvedByUser = approvedByUser;
	}
	
	@Column(name = "approverComments", length = 225)
	public String getApproverComments() {
		return this.approverComments;
	}

	public void setApproverComments(String approverComments) {
		this.approverComments = approverComments;
	}
   
}
