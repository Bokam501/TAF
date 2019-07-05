package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name = "test_factory_actual_shift")
public class ActualShift implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer actualShiftId;
	private Date workdate;
	private WorkShiftMaster shift;
	private Date startTime;
	private Date endTime;
	private String startTimeRemarks;
	private String endTimeRemarks;
	private UserList startByUserList;
	private UserList endByUserList;
	
	private Set<ResourceShiftCheckIn> resourceShiftCheckInSet;
	public ActualShift() {
	}
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "actualShiftId", unique = true, nullable = false)
	public Integer getActualShiftId() {
		return actualShiftId;
	}

	public void setActualShiftId(Integer actualShiftId) {
		this.actualShiftId = actualShiftId;
	}
	@Column(name = "workdate", length = 45)
	public Date getWorkdate() {
		return workdate;
	}

	public void setWorkdate(Date workdate) {
		this.workdate = workdate;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "shiftId") 
	public WorkShiftMaster getShift() {
		return shift;
	}

	public void setShift(WorkShiftMaster shift) {
		this.shift = shift;
	}
	
	@Column(name = "startTime", length = 45)
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	@Column(name = "endTime", length = 45)
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
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
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "actualShift", cascade=CascadeType.ALL)
	public Set<ResourceShiftCheckIn> getResourceShiftCheckInSet() {
		return resourceShiftCheckInSet;
	}

	public void setResourceShiftCheckInSet(
			Set<ResourceShiftCheckIn> resourceShiftCheckInSet) {
		this.resourceShiftCheckInSet = resourceShiftCheckInSet;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "startByUserId")
	public UserList getStartByUserList() {
		return startByUserList;
	}

	public void setStartByUserList(UserList startByUserList) {
		this.startByUserList = startByUserList;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "endByUserId")
	public UserList getEndByUserList() {
		return endByUserList;
	}

	public void setEndByUserList(UserList endByUserList) {
		this.endByUserList = endByUserList;
	}
	

	
}

