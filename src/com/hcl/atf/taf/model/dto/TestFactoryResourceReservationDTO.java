package com.hcl.atf.taf.model.dto;

import java.util.Date;

public class TestFactoryResourceReservationDTO {
	
	private Float blockedCount;
	private Date workDate;
	
	public Float getBlockedCount() {
		return blockedCount;
	}

	public void setBlockedCount(Float blockedCount) {
		this.blockedCount = blockedCount;
	}
	
	public Date getWorkDate() {
		return workDate;
	}
	public void setWorkDate(Date workDate) {
		this.workDate = workDate;
	}
}