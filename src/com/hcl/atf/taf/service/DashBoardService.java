package com.hcl.atf.taf.service;

import java.util.Date;
import java.util.List;

import com.hcl.atf.taf.model.ActivityCollection;
import com.hcl.atf.taf.model.DashBoardTabs;
import com.hcl.atf.taf.model.DashBoardTabsRoleBasedURL;
import com.hcl.atf.taf.model.DashboardVisualizationUrls;
import com.hcl.atf.taf.model.DefectCollection;
import com.hcl.atf.taf.model.ReviewRecordCollection;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.json.jtable.JTableResponse;

public interface DashBoardService {

	List<ActivityCollection> getActivityCollectionCountByDateFilter(Date startDate, Date currentDate);

	List<DefectCollection> getDefectCollectionList();
	List<ReviewRecordCollection> getReivewRecordCollectionList();
	

	void addDashboardTabsToUI(DashBoardTabs dashBoardTabs);

	List<DashBoardTabs> listDashboardTabs(Integer status,Integer jtStartIndex, Integer jtPageSize);

	void update(DashBoardTabs dashBoardTabsFromUI);

	void addDashboardTabsRoleUrl(DashBoardTabsRoleBasedURL dashBoardTabsRoleBasedURL);

	List<DashBoardTabsRoleBasedURL> listDashboardTabsRoleBasedURL(Integer roleId, Integer tabId);

	void update(DashBoardTabsRoleBasedURL dashBoardTabsRoleBasedFromUI);

	float getActivityCollectionListForScheduleVariance(Date startDate, Date currentDate);
	
	String getIndicatorValue(float actualValue, float poorRange,float highRange,String type);

	void deleteroleBasedDataById(DashBoardTabsRoleBasedURL dashboardroleuser);
	
	DashBoardTabsRoleBasedURL getDashBoardTabsRoleBasedURLById(Integer roleBasedId);
	
	JTableResponse dashboardMetricsSummaryCalculation(Date startDate,Date endDate, String testFactoryName, String productName,UserList user);

	JTableResponse slaDashboardMetricsSummaryCalculation(Date startDate,Date endDate,String testFactoryName,String productName, UserList user);

	JTableResponse standardDashboardCalculation(Date startDate,Date endDate,String testFactoryName, String productName, UserList user);

	List<DashBoardTabs> getDasboardTabsByEngagementId(Integer engagementId);

	void addvisualizationUrls(DashboardVisualizationUrls visualizationUrls);

	List<DashboardVisualizationUrls> listDashboardVisualization(Integer status, Integer jtStartIndex, Integer jtPageSize);

	void updateVisualization(DashboardVisualizationUrls dashboardVisualizationFromUI);
	
	
}
