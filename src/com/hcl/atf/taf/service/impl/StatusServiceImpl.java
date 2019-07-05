package com.hcl.atf.taf.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.DimensionDAO;
import com.hcl.atf.taf.dao.StatusDAO;
import com.hcl.atf.taf.model.ActivitySecondaryStatusMaster;
import com.hcl.atf.taf.model.ActivityStatus;
import com.hcl.atf.taf.model.StatusCategory;
import com.hcl.atf.taf.model.dto.StatusSummaryDTO;
import com.hcl.atf.taf.service.StatusService;

@Service
public class StatusServiceImpl implements StatusService {

	@Autowired
	StatusDAO statusDAO;

	@Autowired
	DimensionDAO dimensionDAO;
	
	@Override
	@Transactional
	public StatusSummaryDTO getStatusSummary(Integer statusId) {
		
		StatusSummaryDTO statusSummaryDTO = new StatusSummaryDTO();
		
		int totalPrimaryStatus = 0;
		int totalSecondaryStatus = 0;
		int primaryStatusWithSecondaryStatus = 0;
		int primaryStatusWithOutSecondaryStatus = 0;
		int secondaryStatusMappedWithPrimaryStatus = 0;
		int secondaryStatusUnMappedWithPrimaryStatus = 0;
		
		totalPrimaryStatus = statusDAO.getTotalRecordsForPrimaryStatusPagination(statusId, 2, ActivityStatus.class);
		primaryStatusWithSecondaryStatus = statusDAO.getNumberOfAssociatedStatus(statusId, 2, "primaryStatus");
		primaryStatusWithOutSecondaryStatus = totalPrimaryStatus - primaryStatusWithSecondaryStatus;
		
		totalSecondaryStatus = statusDAO.getTotalRecordsForSecondaryStatusPagination(statusId, 2, ActivitySecondaryStatusMaster.class);
		secondaryStatusMappedWithPrimaryStatus = statusDAO.getNumberOfAssociatedStatus(statusId, 2, "secondaryStatus");
		secondaryStatusUnMappedWithPrimaryStatus = totalSecondaryStatus - secondaryStatusMappedWithPrimaryStatus;
		
		statusSummaryDTO.setDimensionMaster(dimensionDAO.getDimensionById(statusId));		
		statusSummaryDTO.setNumberOfPrimaryStatus(totalPrimaryStatus);
		statusSummaryDTO.setNumberOfSecondaryStatus(totalSecondaryStatus);
		statusSummaryDTO.setNumberOfPrimaryStatusHavingSecondaryStatus(primaryStatusWithSecondaryStatus);
		statusSummaryDTO.setNumberOfPrimaryStatusNotHavingSecondaryStatus(primaryStatusWithOutSecondaryStatus);
		statusSummaryDTO.setNumberOfSecondaryStatusMappedWithPrimaryStatus(secondaryStatusMappedWithPrimaryStatus);
		statusSummaryDTO.setNumberOfSecondaryStatusUnMappedWithPrimaryStatus(secondaryStatusUnMappedWithPrimaryStatus);
		
		return statusSummaryDTO;
	}
	
	@Override
	@Transactional
	public List<ActivityStatus> getPrimaryStatusByStatus(Integer statusId, Integer status, Integer jtStartIndex, Integer jtPageSize) {
		return statusDAO.getPrimaryStatusByStatus(statusId, status, jtStartIndex, jtPageSize);
	}

	@Override
	@Transactional
	public int getTotalRecordsForPrimaryStatusPagination(Integer statusId, Integer status, Class<ActivityStatus> className) {
		return statusDAO.getTotalRecordsForPrimaryStatusPagination(statusId, status, className);
	}

	@Override
	@Transactional
	public ActivityStatus getPrimaryStatusAvailability(Integer statusId, String activityStatusName, Integer activityStatusId) {
		return statusDAO.getPrimaryStatusAvailability(statusId, activityStatusName, activityStatusId);
	}
	
	@Override
	@Transactional
	public void addPrimaryStatus(ActivityStatus activityStatus) {
		statusDAO.addPrimaryStatus(activityStatus);
	}

	@Override
	@Transactional
	public void updatePrimaryStatus(ActivityStatus activityStatus) {
		statusDAO.updatePrimaryStatus(activityStatus);
	}

	@Override
	@Transactional
	public List<ActivitySecondaryStatusMaster> getSecondaryStatusByStatus(Integer statusId, Integer status, Integer jtStartIndex, Integer jtPageSize) {
		return statusDAO.getSecondaryStatusByStatus(statusId, status, jtStartIndex, jtPageSize);
	}

	@Override
	@Transactional
	public int getTotalRecordsForSecondaryStatusPagination(Integer statusId, Integer status, Class<ActivitySecondaryStatusMaster> className) {
		return statusDAO.getTotalRecordsForSecondaryStatusPagination(statusId, status, className);
	}

	@Override
	@Transactional
	public ActivitySecondaryStatusMaster getSecondaryStatusAvailability(Integer statusId, String activitySecondaryStatusName, Integer activitySecondaryStatusId) {
		return statusDAO.getSecondaryStatusAvailability(statusId, activitySecondaryStatusName, activitySecondaryStatusId);
	}

	@Override
	@Transactional
	public void addSecondaryStatus(ActivitySecondaryStatusMaster activitySecondaryStatusMaster) {
		statusDAO.addSecondaryStatus(activitySecondaryStatusMaster);
	}

	@Override
	@Transactional
	public void updateSecondaryStatus(ActivitySecondaryStatusMaster activitySecondaryStatusMaster) {
		statusDAO.updateSecondaryStatus(activitySecondaryStatusMaster);
	}

	@Override
	@Transactional
	public List<Object[]> getSecondaryStatusToAddWithPrimaryStatus(Integer primaryStatusId, Integer statusId, Integer jtStartIndex, Integer jtPageSize) {
		return statusDAO.getSecondaryStatusToAddWithPrimaryStatus(primaryStatusId, statusId, jtStartIndex, jtPageSize);
	}


	@Override
	@Transactional
	public Integer getSecondaryStatusToAddWithPrimaryStatusCount(Integer primaryStatusId, Integer statusId) {
		return statusDAO.getSecondaryStatusToAddWithPrimaryStatusCount(primaryStatusId, statusId);
	}
	
	@Override
	@Transactional
	public List<Object[]> getSecondaryStatusMappedWithPrimaryStatus(Integer primaryStatusId, Integer statusId) {
		return statusDAO.getSecondaryStatusMappedWithPrimaryStatus(primaryStatusId, statusId);
	}

	@Override
	@Transactional
	public boolean isSecondaryStatusAlreadyMappedToPrimaryStatus(Integer primaryStatusId, Integer secondaryStatusId) {
		return statusDAO.isSecondaryStatusAlreadyMappedToPrimaryStatus(primaryStatusId, secondaryStatusId);
	}

	@Override
	@Transactional
	public void mapOrUnmapSecondaryStatusForPrimaryStatus(Integer primaryStatusId, Integer secondaryStatusId, String mapOrUnmap) {
		statusDAO.mapOrUnmapSecondaryStatusForPrimaryStatus(primaryStatusId, secondaryStatusId, mapOrUnmap);
	}

	@Override
	@Transactional
	public List<StatusCategory> listAllStatusCategories() {
		return statusDAO.listAllStatusCategories();
	}

	@Override
	@Transactional
	public StatusCategory getStatusCategoryByName(String statusCategoryName) {
		return statusDAO.getStatusCategoryByName(statusCategoryName);
	}

	@Override
	@Transactional
	public StatusCategory getStatusCategoryBystatusCategoryId(
			Integer statusCategoryId) {
		return statusDAO.getStatusCategoryBystatusCategoryId(statusCategoryId);
	}

}
