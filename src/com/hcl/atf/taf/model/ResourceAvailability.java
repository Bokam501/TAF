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
@Table(name = "resource_availability")
public class ResourceAvailability  implements java.io.Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Integer resourceAvailabilityId;
	private Date workDate;
	private WorkShiftMaster workShiftMaster;
	private UserList resource;
	private Integer isAvailable;
	private Integer bookForShift;
	private Integer shiftAttendance;
	private Date attendanceCheckInTime;
	private Date attendanceCheckOutTime;
	private ShiftTypeMaster shiftTypeMaster;

	@Column(name="shiftBillingModeIsFull")
	private Integer shiftBillingModeIsFull;


	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "resourceAvailabilityId", unique = true, nullable = false)
	public Integer getResourceAvailabilityId() {
		return resourceAvailabilityId;
	}

	public void setResourceAvailabilityId(Integer resourceAvailabilityId) {
		this.resourceAvailabilityId = resourceAvailabilityId;
	}
	
	@Column(name="isAvailable")
	public Integer getIsAvailable() {
		return isAvailable;
	}

	public void setIsAvailable(Integer isAvailable) {
		this.isAvailable = isAvailable;
	}
	
	@Temporal(TemporalType.DATE)
	@Column(name = "workDate")
	public Date getWorkDate() {
		return workDate;
	}

	public void setWorkDate(Date workDate) {
		this.workDate = workDate;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "shiftId") 
	public WorkShiftMaster getWorkShiftMaster() {
		return workShiftMaster;
	}

	public void setWorkShiftMaster(WorkShiftMaster workShiftMaster) {
		this.workShiftMaster = workShiftMaster;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId") 
	public UserList getResource() {
		return resource;
	}

	public void setResource(UserList resource) {
		this.resource = resource;
	}

	@Column(name="bookForShift")
	public Integer getBookForShift() {
		return bookForShift;
	}

	public void setBookForShift(Integer bookForShift) {
		this.bookForShift = bookForShift;
	}

	@Column(name="shiftAttendance")
	public Integer getShiftAttendance() {
		return shiftAttendance;
	}

	public void setShiftAttendance(Integer shiftAttendance) {
		this.shiftAttendance = shiftAttendance;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "attendanceCheckInTime")
	public Date getAttendanceCheckInTime() {
		return attendanceCheckInTime;
	}

	public void setAttendanceCheckInTime(Date attendanceCheckInTime) {
		this.attendanceCheckInTime = attendanceCheckInTime;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "attendanceCheckOutTime")
	public Date getAttendanceCheckOutTime() {
		return attendanceCheckOutTime;
	}

	public void setAttendanceCheckOutTime(Date attendanceCheckOutTime) {
		this.attendanceCheckOutTime = attendanceCheckOutTime;
	}

	public Integer getShiftBillingModeIsFull() {
		return shiftBillingModeIsFull;
	}

	public void setShiftBillingModeIsFull(Integer shiftBillingModeIsFull) {
		this.shiftBillingModeIsFull = shiftBillingModeIsFull;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "shiftTypeId") 
	public ShiftTypeMaster getShiftTypeMaster() {
		return shiftTypeMaster;
	}

	public void setShiftTypeMaster(ShiftTypeMaster shiftTypeMaster) {
		this.shiftTypeMaster = shiftTypeMaster;
	}
	
	@Override
	public boolean equals(Object o) {
		ResourceAvailability resourceAvailability = (ResourceAvailability) o;
		if (this.resourceAvailabilityId == resourceAvailability.getResourceAvailabilityId()) {
			return true;
		}else{
			return false;
		}
	}
	
	
	@Override
	public int hashCode(){
	    return (int) resourceAvailabilityId;
	  }
	
	
}
