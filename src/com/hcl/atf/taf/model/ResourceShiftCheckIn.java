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
@Table(name = "resource_shift_checkin")
public class ResourceShiftCheckIn {
	private Integer resourceShiftCheckInId;
	private Date checkIn;
	private Date checkOut;
	private Date createdDate;
	private String startTimeRemarks;
	private String endTimeRemarks;
	private Integer isApproved;
	private String approvalRemarks;
	private Date approvedDate;
	private UserList userList;
	private UserList approverUser;
	private ActualShift actualShift;
	private ProductMaster productMaster; 
	
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "resourceShiftCheckInId", unique = true, nullable = false)
	public Integer getResourceShiftCheckInId() {
		return resourceShiftCheckInId;
	}
	public void setResourceShiftCheckInId(Integer resourceShiftCheckInId) {
		this.resourceShiftCheckInId = resourceShiftCheckInId;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "checkIn")
	public Date getCheckIn() {
		return checkIn;
	}
	public void setCheckIn(Date checkIn) {
		this.checkIn = checkIn;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "checkOut")
	public Date getCheckOut() {
		return checkOut;
	}
	public void setCheckOut(Date checkOut) {
		this.checkOut = checkOut;
	}
	@Temporal(TemporalType.DATE)
	@Column(name = "createdDate")
	public Date getCreatedDate() {
		return createdDate;
	}
	
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	public UserList getUserList() {
		return userList;
	}
	public void setUserList(UserList userList) {
		this.userList = userList;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "actualShiftId")
	public ActualShift getActualShift() {
		return actualShift;
	}
	public void setActualShift(ActualShift actualShift) {
		this.actualShift = actualShift;
	}
	@Column(name = "startTimeRemarks")
	public String getStartTimeRemarks() {
		return startTimeRemarks;
	}
	public void setStartTimeRemarks(String startTimeRemarks) {
		this.startTimeRemarks = startTimeRemarks;
	}
	@Column(name = "endTimeRemarks")
	public String getEndTimeRemarks() {
		return endTimeRemarks;
	}
	public void setEndTimeRemarks(String endTimeRemarks) {
		this.endTimeRemarks = endTimeRemarks;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "productId")
	public ProductMaster getProductMaster() {
		return productMaster;
	}
	public void setProductMaster(ProductMaster productMaster) {
		this.productMaster = productMaster;
	}
	@Column(name = "isApproved")
	public Integer getIsApproved() {
		return isApproved;
	}
	public void setIsApproved(Integer isApproved) {
		this.isApproved = isApproved;
	}
	@Column(name = "approvalRemarks")
	public String getApprovalRemarks() {
		return approvalRemarks;
	}
	public void setApprovalRemarks(String approvalRemarks) {
		this.approvalRemarks = approvalRemarks;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "approvedDate")
	public Date getApprovedDate() {
		return approvedDate;
	}
	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "approverId")
	public UserList getApproverUser() {
		return approverUser;
	}
	public void setApproverUser(UserList approverUser) {
		this.approverUser = approverUser;
	}
	@Override
	public boolean equals(Object o) {
		ResourceShiftCheckIn resShiftCheckIn = (ResourceShiftCheckIn) o;
		if (this.resourceShiftCheckInId == resShiftCheckIn.getResourceShiftCheckInId()) {
			return true;
		}else{
			return false;
		}
	}
	
	@Override
	public int hashCode(){
	    return (int) resourceShiftCheckInId;
	  }


}
