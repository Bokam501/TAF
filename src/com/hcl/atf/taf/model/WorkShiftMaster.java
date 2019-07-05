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
@Table(name = "work_shifts_master")
public class WorkShiftMaster  implements java.io.Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Integer shiftId;
	
	private String shiftName;
	
	private String displayLabel;
	
	private String description;
	
	private Date startTime;
	
	private Date endTime;
	
	private Integer status;
	
	private ShiftTypeMaster shiftType;
	
	private TestFactory testFactory;
		
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "shiftId", unique = true, nullable = false)
	public Integer getShiftId() {
		return shiftId;
	}

	public void setShiftId(Integer shiftId) {
		this.shiftId = shiftId;
	}
	
	@Column(name="shiftName")
	public String getShiftName() {
		return shiftName;
	}

	public void setShiftName(String shiftName) {
		this.shiftName = shiftName;
	}
	
	@Column(name="displayLabel")
	public String getDisplayLabel() {
		return displayLabel;
	}

	public void setDisplayLabel(String displayLabel) {
		this.displayLabel = displayLabel;
	}
	
	@Column(name="description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "startTime")
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "endTime")
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	@Column(name="status")
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "shiftTypeId")
	public ShiftTypeMaster getShiftType() {
		return shiftType;
	}

	public void setShiftType(ShiftTypeMaster shiftType) {
		this.shiftType = shiftType;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "testFactoryId")
	public TestFactory getTestFactory() {
		return testFactory;
	}

	public void setTestFactory(TestFactory testFactory) {
		this.testFactory = testFactory;
	}
	@Override
	public boolean equals(Object o) {
		WorkShiftMaster wrkshift = (WorkShiftMaster) o;
		if (this.shiftId == wrkshift.getShiftId()) {
			return true;
		}else{
			return false;
		}
	}
	
	@Override
	public int hashCode(){
	    return (int) shiftId;
	  }

}
