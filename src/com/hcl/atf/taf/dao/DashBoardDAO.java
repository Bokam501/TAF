package com.hcl.atf.taf.dao;

import java.util.Date;
import java.util.List;

import com.hcl.atf.taf.model.ActivityCollection;
import com.hcl.atf.taf.model.DashBoardTabs;
import com.hcl.atf.taf.model.DashBoardTabsRoleBasedURL;
import com.hcl.atf.taf.model.DashboardVisualizationUrls;
import com.hcl.atf.taf.model.DefectCollection;
import com.hcl.atf.taf.model.ReviewRecordCollection;


public interface DashBoardDAO  {

	List<ActivityCollection> getActivityCollectionCountByDateFilter(Date startDate,Date currentDate);

	List<DefectCollection> getDefectCollectionList();
	
	List<ReviewRecordCollection> getReivewRecordCollectionList();
	

	void addDashboardTabsToUI(DashBoardTabs dashBoardTabs);

	List<DashBoardTabs> listDashboardTabs(Integer status,Integer jtStartIndex, Integer jtPageSize);

	void update(DashBoardTabs dashBoardTabs);

	void addDashboardTabsRoleUrl(DashBoardTabsRoleBasedURL dashBoardTabsRoleBasedURL);

	List<DashBoardTabsRoleBasedURL> listDashboardTabsRoleBasedURL(Integer roleId, Integer tabId);

	void update(DashBoardTabsRoleBasedURL dashBoardTabsRoleBasedFromUI);

	List<ActivityCollection> getActivityCollectionListForScheduleVariance(Date startDate, Date currentDate);

	void deleteroleBasedDataById(DashBoardTabsRoleBasedURL dashboardroleuser);

	DashBoardTabsRoleBasedURL getDashBoardTabsRoleBasedURLById(Integer roleBasedId);

	List<DashBoardTabs> getDasboardTabsByEngagementId(Integer engagementId);

	void addvisualizationUrls(DashboardVisualizationUrls visualizationUrls);

	List<DashboardVisualizationUrls> listDashboardVisualization(Integer status, Integer jtStartIndex, Integer jtPageSize);

	void updateVisualization(DashboardVisualizationUrls dashboardVisualizationFromUI);
	
	
}
