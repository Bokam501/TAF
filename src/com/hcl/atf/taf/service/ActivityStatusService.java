package com.hcl.atf.taf.service;

import java.util.List;

import com.hcl.atf.taf.model.ActivityStatus;


public interface ActivityStatusService {
	ActivityStatus getStatusById(Integer statusId);
	ActivityStatus getStatusByName(String statusName);
	List<ActivityStatus> listStatusByWorkItemId(Integer workItemId);
	List<ActivityStatus> listActivityStatus();
	List<ActivityStatus> listActivityStatusByDimensionId(Integer dimensionId);
	ActivityStatus getStatusNameByDimensionId(String statusName,Integer dimensionId);
	
}
