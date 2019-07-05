package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name = "time_sheet_entry_master")
public class TimeSheetEntryMaster implements java.io.Serializable {
	
	private Integer timeSheetEntryId;
	private Date date;
	private Integer hours;
	private Integer mins;
	private WorkShiftMaster shift;
	private WorkPackage workPackage;
	private ProductMaster product;
	private String comments;
	private UserList user;
	private UserRoleMaster role;
	private Integer isApproved;
	private String approvalComments;
	private Date approvedDate;
	private UserList approver;
	private Integer status;
	private Date createdDate;
	private Date modifiedDate;
	private TimeSheetActivityType activityType;
	private ResourceShiftCheckIn resourceShiftCheckIn; 
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "timeSheetEntryId", unique = true, nullable = false)
	public Integer getTimeSheetEntryId() {
		return timeSheetEntryId;
	}
	public void setTimeSheetEntryId(Integer timeSheetEntryId) {
		this.timeSheetEntryId = timeSheetEntryId;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date")	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	@Column(name = "hours")
	public Integer getHours() {
		return hours;
	}
	public void setHours(Integer hours) {
		this.hours = hours;
	}
	
	@Column(name = "mins")
	public Integer getMins() {
		return mins;
	}
	public void setMins(Integer mins) {
		this.mins = mins;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "shiftId")
	public WorkShiftMaster getShift() {
		return shift;
	}
	public void setShift(WorkShiftMaster shift) {
		this.shift = shift;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "workPackageId")
	public WorkPackage getWorkPackage() {
		return workPackage;
	}
	public void setWorkPackage(WorkPackage workPackage) {
		this.workPackage = workPackage;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "productId")
	public ProductMaster getProduct() {
		return product;
	}
	public void setProduct(ProductMaster product) {
		this.product = product;
	}
	
	@Column(name = "comments")
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	public UserList getUser() {
		return user;
	}
	public void setUser(UserList user) {
		this.user = user;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "roleId")
	public UserRoleMaster getRole() {
		return role;
	}
	public void setRole(UserRoleMaster role) {
		this.role = role;
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
	
	@Column(name = "isApproved")
	public Integer getIsApproved() {
		return isApproved;
	}
	public void setIsApproved(Integer isApproved) {
		this.isApproved = isApproved;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "approverId")
	public UserList getApprover() {
		return approver;
	}
	public void setApprover(UserList approver) {
		this.approver = approver;
	}
	
	@Column(name = "approvalComments")
	public String getApprovalComments() {
		return approvalComments;
	}
	public void setApprovalComments(String approvalComments) {
		this.approvalComments = approvalComments;
	}
	
	@Column(name = "approvedDate")
	public Date getApprovedDate() {
		return approvedDate;
	}
	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}
	
	@Column(name = "status")
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "activityTypeId")
	public TimeSheetActivityType getActivityType() {
		return activityType;
	}
	public void setActivityType(TimeSheetActivityType activityType) {
		this.activityType = activityType;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "resourceShiftCheckInId")
	public ResourceShiftCheckIn getResourceShiftCheckIn() {
		return resourceShiftCheckIn;
	}
	public void setResourceShiftCheckIn(ResourceShiftCheckIn resourceShiftCheckIn) {
		this.resourceShiftCheckIn = resourceShiftCheckIn;
	}
	
}
