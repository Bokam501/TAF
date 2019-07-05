package com.hcl.atf.taf.dao;

import java.util.List;

import com.hcl.atf.taf.model.ActivitySecondaryStatusMaster;
import com.hcl.atf.taf.model.ActivityStatus;
import com.hcl.atf.taf.model.StatusCategory;


public interface StatusDAO {

	int getNumberOfAssociatedStatus(Integer statusId, Integer status, String statusType);
	
	List<ActivityStatus> getPrimaryStatusByStatus(Integer statusId, Integer status, Integer jtStartIndex, Integer jtPageSize);
	int getTotalRecordsForPrimaryStatusPagination(Integer statusId, Integer status, Class<ActivityStatus> className);
	ActivityStatus getPrimaryStatusAvailability(Integer statusId, String activityStatusName, Integer activityStatusId);
	void addPrimaryStatus(ActivityStatus activityStatus);
	void updatePrimaryStatus(ActivityStatus activityStatus);
	
	List<ActivitySecondaryStatusMaster> getSecondaryStatusByStatus(Integer statusId, Integer status, Integer jtStartIndex, Integer jtPageSize);
	int getTotalRecordsForSecondaryStatusPagination(Integer statusId, Integer status, Class<ActivitySecondaryStatusMaster> className);
	ActivitySecondaryStatusMaster getSecondaryStatusAvailability(Integer statusId, String activitySecondaryStatusName, Integer activitySecondaryStatusId);
	void addSecondaryStatus(ActivitySecondaryStatusMaster activitySecondaryStatusMaster);
	void updateSecondaryStatus(ActivitySecondaryStatusMaster activitySecondaryStatusMaster);
	
	List<Object[]> getSecondaryStatusToAddWithPrimaryStatus(Integer primaryStatusId, Integer statusId, Integer jtStartIndex, Integer jtPageSize);
	Integer getSecondaryStatusToAddWithPrimaryStatusCount(Integer primaryStatusId, Integer statusId);
	List<Object[]> getSecondaryStatusMappedWithPrimaryStatus(Integer primaryStatusId, Integer statusId);
	boolean isSecondaryStatusAlreadyMappedToPrimaryStatus(Integer primaryStatusId, Integer secondaryStatusId);
	void mapOrUnmapSecondaryStatusForPrimaryStatus(Integer primaryStatusId, Integer secondaryStatusId, String mapOrUnmap);

	List<StatusCategory> listAllStatusCategories();
	StatusCategory getStatusCategoryByName(String statusCategoryName);

	StatusCategory getStatusCategoryBystatusCategoryId(Integer statusCategoryId);
	
}
