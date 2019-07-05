package com.hcl.atf.taf.dao;

import java.util.Date;
import java.util.List;

import com.hcl.atf.taf.model.ResourceDailyPerformance;
import com.hcl.atf.taf.model.dto.ResourceExperienceSummaryDTO;

public interface ResourcePerformanceDAO {

	ResourceDailyPerformance getResourceDailyPerformance(Integer userId, Date workDate, Integer actualShiftId, Integer workPackageId);
	void updateResourceDailyPerformance(ResourceDailyPerformance resourceDailyPerformance);
	List<ResourceExperienceSummaryDTO> getResourceAveragePerformance(Integer userId, Integer workPackageId);
	List<ResourceDailyPerformance> getResourceAveragePerformanceDetails(Integer userId, Integer workPackageId);
	ResourceDailyPerformance getResourceDailyPerformanceWithHighRating(Integer userId, Date fromDate, Date toDate);
	
}
