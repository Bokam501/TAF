package com.hcl.atf.taf.dao;

import java.util.List;

import com.hcl.atf.taf.model.ActivityStatus;


public interface ActivityStatusDAO {

	List<ActivityStatus> listActivityStatuses();
	ActivityStatus getStatusByName(String statusName);
	ActivityStatus getStatusById(Integer statusId);
	List<ActivityStatus> listActivityStatusByDimensionId(Integer dimensionId);
	ActivityStatus getStatusNameByDimensionId(String statusName,Integer dimensionId);
	
}
