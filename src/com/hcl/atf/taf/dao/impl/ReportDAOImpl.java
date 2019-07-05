package com.hcl.atf.taf.dao.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.dao.ReportDAO;
import com.hcl.atf.taf.model.Activity;
import com.hcl.atf.taf.model.ActivityTask;
import com.hcl.atf.taf.model.MongoCollection;
import com.hcl.atf.taf.model.PivotRestTemplate;
import com.hcl.atf.taf.model.ReportIssue;
import com.hcl.atf.taf.model.TaskEffortTemplate;
import com.hcl.atf.taf.model.TimeSheetEntryMaster;
import com.hcl.atf.taf.model.WorkPackageTestCaseExecutionPlan;
import com.hcl.atf.taf.model.dto.PivotRestTemplateDTO;
import com.hcl.atf.taf.model.dto.TimeSheetStatisticsDTO;
import com.hcl.atf.taf.model.dto.WorkPackageResultsStatisticsDTO;

@Repository
public class ReportDAOImpl implements ReportDAO {

	private static final Log log = LogFactory.getLog(ReportDAOImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	
	
	@Override
	@Transactional
	public List<WorkPackageTestCaseExecutionPlan> listWorkPackageTestCasesExecutionPlanResultsEod(Integer workPackageId, Integer shiftId, Date executionDate,
			 int jtStartIndex,int jtPageSize) {
		log.info("listing listWorkPackageTestCasesExecutionPlanResultsEod");
		List<WorkPackageTestCaseExecutionPlan> testCaseResults = new ArrayList<WorkPackageTestCaseExecutionPlan>();
		log.info("Getting TestCase Results for : " + workPackageId + " : " +  executionDate + " : " + shiftId);
		
		try {

			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackageTestCaseExecutionPlan.class,"wptcep");
			
			if (executionDate == null)
				executionDate = DateUtility.getCurrentDate();
			
			c.add(Restrictions.eq("wptcep.actualExecutionDate", executionDate));
			
			c.add(Restrictions.eq("wptcep.workPackage.workPackageId", workPackageId));
			c.add(Restrictions.eq("wptcep.plannedWorkShiftMaster.shiftId", shiftId))
			.setFirstResult(jtStartIndex)
			.setMaxResults(jtPageSize);
			
			testCaseResults = c.list();
			for(WorkPackageTestCaseExecutionPlan result : testCaseResults) {
				Hibernate.initialize(result.getTestCase());
				Hibernate.initialize(result.getWorkPackage());
				if(result.getWorkPackage() != null){
					Hibernate.initialize(result.getWorkPackage());
					if(result.getWorkPackage().getProductBuild() != null){
						Hibernate.initialize(result.getWorkPackage().getProductBuild());
						if(result.getWorkPackage().getProductBuild().getProductVersion() != null){
							Hibernate.initialize(result.getWorkPackage().getProductBuild().getProductVersion());
						}
					}
				}
				Hibernate.initialize(result.getEnvironmentList());
				Hibernate.initialize(result.getTester());
				Hibernate.initialize(result.getTestLead());
				Hibernate.initialize(result.getTestCaseExecutionResult());
				Hibernate.initialize(result.getTestCaseExecutionResult().getTestExecutionResultBugListSet());
				Hibernate.initialize(result.getTestCase().getTestCasePriority().getPriorityName());
				Hibernate.initialize(result.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductName());
				if(result.getRunConfiguration()!=null){
					Hibernate.initialize(result.getRunConfiguration());
					Hibernate.initialize(result.getRunConfiguration().getRunconfiguration().getRunconfigName());
				}
			
				
				Hibernate.initialize(result.getTestCase().getTestcaseTypeMaster().getName());
				Hibernate.initialize(result.getActualWorkShiftMaster());
				Hibernate.initialize(result.getPlannedWorkShiftMaster());
				
			}
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return testCaseResults;
	}

	@Override
	@Transactional
	public List<WorkPackageResultsStatisticsDTO> listWorkPackageStatisticsEOD(Integer workPackageId, Integer shiftId, Date executionDate) {
		log.info("listing listWorkPackageStatisticsEOD");
		List<WorkPackageResultsStatisticsDTO> workPackageResultsStatisticsDTOList = new ArrayList<WorkPackageResultsStatisticsDTO>();
		log.info("Getting WorkPackageTestCaseExecutionPlan statistics for : " + workPackageId + " : " +  executionDate + " : " + shiftId);
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackageTestCaseExecutionPlan.class,"wptcep");
			c.createAlias("wptcep.testCaseExecutionResult", "result");
			c.createAlias("wptcep.workPackage", "wpackage");
			c.createAlias("wptcep.tester", "tester");
			c.createAlias("wptcep.testLead", "testLead");			
			c.createAlias("wptcep.plannedWorkShiftMaster", "pwsm");
			
			c.add(Restrictions.eq("wptcep.workPackage.workPackageId", workPackageId));
			c.add(Restrictions.eq("pwsm.shiftId", shiftId));
			
			if (executionDate == null){
				executionDate = DateUtility.getCurrentDate();			
			}		
			
			Calendar cal = Calendar.getInstance();       // get calendar instance
			cal.setTime(executionDate);                           // set cal to date
			cal.set(Calendar.HOUR_OF_DAY, 0);            // set hour to midnight
			cal.set(Calendar.MINUTE, 0);                 // set minute in hour
			cal.set(Calendar.SECOND, 0);                 // set second in minute
			cal.set(Calendar.MILLISECOND, 0);            // set millis in second
			Date startExecutionDate = cal.getTime();
			// timestamp now
			cal = null;
			cal = Calendar.getInstance();       // get calendar instance
			cal.setTime(executionDate);                           // set cal to date
			cal.set(Calendar.HOUR_OF_DAY, 23);            // set hour to midnight
			cal.set(Calendar.MINUTE, 59);                 // set minute in hour
			cal.set(Calendar.SECOND, 59);                 // set second in minute
			cal.set(Calendar.MILLISECOND, 0);            // set millis in second
			Date endExecutionDate = cal.getTime();
			
			c.add(Restrictions.between("wptcep.actualExecutionDate", startExecutionDate, endExecutionDate));
			log.info("executionDate="+executionDate);
			ProjectionList projectionList = Projections.projectionList();
			
			projectionList.add(Property.forName("wpackage.name").as("workPackageName"));
			projectionList.add(Property.forName("wpackage.workPackageId").as("workPackageId"));
			
			projectionList.add(Property.forName("wptcep.actualExecutionDate").as("actualExecutionDate"));

			projectionList.add(Property.forName("tester.userDisplayName").as("testerName"));
			projectionList.add(Property.forName("testLead.userDisplayName").as("testLeadName"));
			projectionList.add(Property.forName("testLead.userId").as("testLeadId"));
			projectionList.add(Property.forName("pwsm.shiftName").as("shiftName"));
			projectionList.add(Projections.groupProperty("pwsm.shiftId").as("shiftId"));
			projectionList.add(Projections.groupProperty("tester.userId").as("testerId"));

			projectionList.add(Projections.sum("result.defectsCount"));
																			   
			projectionList.add(Projections.count("wptcep.id"));
			c.setProjection(projectionList);

			List<Object[]> list = c.list();
			log.info("listWorkPackageStatisticsEOD size : " + list.size());
			WorkPackageResultsStatisticsDTO workPackageResultsStatisticsDTO = null;
			for (Object[] row : list) {
				workPackageResultsStatisticsDTO = new WorkPackageResultsStatisticsDTO();
				
				workPackageResultsStatisticsDTO.setWorkPackageName((String)row[0]);
				workPackageResultsStatisticsDTO.setWorkPackageId((Integer)row[1]);
				workPackageResultsStatisticsDTO.setActualExecutionDate((Date)row[2]);
				workPackageResultsStatisticsDTO.setTesterName((String)row[3]);
				workPackageResultsStatisticsDTO.setTestLeadName((String)row[4]);
				workPackageResultsStatisticsDTO.setTestLeadId((Integer)row[5]);
				
				workPackageResultsStatisticsDTO.setShiftName((String)row[6]);
				
				workPackageResultsStatisticsDTO.setShiftId((Integer)row[7]);
				workPackageResultsStatisticsDTO.setTesterId((Integer)row[8]);
				
				Long longvalueDefectsCount = (Long)row[9];
				workPackageResultsStatisticsDTO.setDefectsCount(Integer.valueOf(longvalueDefectsCount.intValue()));
				
				Long longvalueTestCaseCount = (Long)row[10];
				workPackageResultsStatisticsDTO.setTestCasesCount(Integer.valueOf(longvalueTestCaseCount.intValue()));
				
				workPackageResultsStatisticsDTOList.add(workPackageResultsStatisticsDTO);
			}
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return workPackageResultsStatisticsDTOList;
	}

	// The following NEW method contains logic for the complete Timesheet Statistics/Billing report, to be used in invoicing
	// This uses timeSheetEntryMaster.class as the based table
	// This method is currently used by ReportServiceImpl.java
	@Override
	@Transactional
	public List<TimeSheetStatisticsDTO> listCompleteTimeSheetStatistics(Date executionDateFrom, Date executionDateTo) {				
		log.info("listing listTimeSheetStatistics");
		List<TimeSheetStatisticsDTO> timeSheetStatisticsDTOList = new ArrayList<TimeSheetStatisticsDTO>();
		log.info("Getting TimeSheet statistics for  executionDateFrom: " +  executionDateFrom + " executionDateTo " +  executionDateTo);
		
		try {

			Criteria c = sessionFactory.getCurrentSession().createCriteria(TimeSheetEntryMaster.class,"tsem");
			
			c.createAlias("tsem.user", "tsemUser");
			c.createAlias("tsem.shift", "tsemShift");
			c.createAlias("tsem.workPackage", "wPackage");

			c.createAlias("tsem.resourceShiftCheckIn", "resShftCheckIn");
			c.createAlias("resShftCheckIn.actualShift", "actlShift");
			
			c.createAlias("wPackage.productBuild", "pBuild");
			c.createAlias("pBuild.productVersion", "pVersion");
			c.createAlias("pVersion.productMaster", "pMaster");
			c.createAlias("pMaster.testFactory", "tFactory");

			c.createAlias("tsemUser.vendor", "vendorAgency");
			c.createAlias("tsemShift.shiftType", "shftType");
			
			c.createAlias("pMaster.customer", "customer");
			c.createAlias("tsem.role", "userRole");
			
			if (executionDateFrom == null){
				executionDateFrom = DateUtility.getCurrentDate();
			}
			
			if (executionDateTo == null){
				executionDateTo = executionDateFrom;
			}
			
			log.info("executionDateFrom sent for Query="+executionDateFrom);
			log.info("executionDateTo sent for Query="+executionDateTo);

			c.add(Restrictions.between("tsem.date", executionDateFrom,  executionDateTo));
			
			c.add(Restrictions.eq("tsem.isApproved", 1));

			ProjectionList projectionList = Projections.projectionList();
			
			projectionList.add(Property.forName("tsemShift.shiftId").as("shiftId"));
			projectionList.add(Property.forName("tsemShift.shiftName").as("shiftName"));
			
			projectionList.add(Property.forName("tsemShift.startTime").as("workShiftStartDateTime"));
			projectionList.add(Property.forName("tsemShift.endTime").as("workShiftEndDateTime"));
			projectionList.add(Property.forName("shftType.shiftTypeId").as("shiftTypeId"));
			
			projectionList.add(Property.forName("actlShift.endTime").as("actualShiftEndDateTime"));

			projectionList.add(Property.forName("tsemUser.userDisplayName").as("testerName"));
			projectionList.add(Property.forName("tsemUser.firstName").as("userFirstName"));
			projectionList.add(Property.forName("tsemUser.lastName").as("userlastName"));
			projectionList.add(Property.forName("userRole.roleName").as("userRoleName"));
			projectionList.add(Property.forName("customer.customerId").as("customerId"));
			projectionList.add(Property.forName("vendorAgency.registeredCompanyName").as("vendorName"));
			
			projectionList.add(Property.forName("tFactory.testFactoryId").as("testFactoryId"));
			projectionList.add(Property.forName("tFactory.testFactoryName").as("testFactoryName"));
			
			projectionList.add(Property.forName("pMaster.productId").as("productId"));
			projectionList.add(Property.forName("pMaster.productName").as("productName"));
			projectionList.add(Property.forName("pMaster.projectCode").as("projectCode"));
			projectionList.add(Property.forName("wPackage.name").as("workPackageName"));
			
			projectionList.add(Property.forName("pMaster.shiftAttendanceGraceTime").as("shiftAttendanceGraceTime"));
			projectionList.add(Property.forName("pMaster.shiftLunchAuthorisedTime").as("shiftLunchAuthorisedTime"));
			projectionList.add(Property.forName("pMaster.shiftBreaksAuthorisedTime").as("shiftBreaksAuthorisedTime"));
			projectionList.add(Property.forName("pMaster.weeklyOverTimeLimit").as("weeklyOverTimeLimit"));
			
			projectionList.add(Property.forName("resShftCheckIn.resourceShiftCheckInId").as("productResourceShiftCheckInId"));
			projectionList.add(Property.forName("resShftCheckIn.checkOut").as("productResourceShiftCheckOut"));
			
			projectionList.add(Projections.groupProperty("tsemShift.shiftId").as("shiftCount"));
			projectionList.add(Projections.groupProperty("tsemShift.shiftName").as("shiftNameCount"));
			projectionList.add(Projections.groupProperty("tsemShift.startTime").as("shiftStartTimeCount"));
			projectionList.add(Projections.groupProperty("tsemShift.endTime").as("shiftEndTimeCount"));
			projectionList.add(Projections.groupProperty("shftType.shiftTypeId").as("shiftTypeCount"));
			projectionList.add(Projections.groupProperty("actlShift.endTime").as("actlShiftEndTimeCount"));
			projectionList.add(Projections.groupProperty("tsemUser.userDisplayName").as("userDispNameCount"));
			projectionList.add(Projections.groupProperty("tsemUser.firstName").as("firstNameCount"));
			projectionList.add(Projections.groupProperty("tsemUser.lastName").as("lastNameCount"));
			projectionList.add(Projections.groupProperty("userRole.roleName").as("roleNameCount"));
			projectionList.add(Projections.groupProperty("customer.customerId").as("custIdCount"));
			projectionList.add(Projections.groupProperty("vendorAgency.registeredCompanyName").as("vendorNameCount"));
			projectionList.add(Projections.groupProperty("tFactory.testFactoryId").as("testFactIdCount"));
			projectionList.add(Projections.groupProperty("tFactory.testFactoryName").as("testFactNameCount"));
			projectionList.add(Projections.groupProperty("pMaster.productId").as("prodIdCount"));
			projectionList.add(Projections.groupProperty("pMaster.productName").as("prodNameCount"));
			projectionList.add(Projections.groupProperty("pMaster.projectCode").as("projCdCount"));
			projectionList.add(Projections.groupProperty("wPackage.name").as("wpNameCount"));
			projectionList.add(Projections.groupProperty("pMaster.shiftAttendanceGraceTime").as("shiftGraceTimeCount"));
			projectionList.add(Projections.groupProperty("pMaster.shiftLunchAuthorisedTime").as("shiftLunchTimeCount"));
			projectionList.add(Projections.groupProperty("pMaster.shiftBreaksAuthorisedTime").as("shiftBreakTimeCount"));
			projectionList.add(Projections.groupProperty("pMaster.weeklyOverTimeLimit").as("weeklyOTLimitCount"));
			projectionList.add(Projections.groupProperty("resShftCheckIn.resourceShiftCheckInId").as("resShiftChkinIdCount"));
			projectionList.add(Projections.groupProperty("resShftCheckIn.checkOut").as("resChkoutCount"));
			
			projectionList.add(Projections.groupProperty("tsem.date").as("actualExecutionDate"));
			projectionList.add(Projections.groupProperty("actlShift.startTime").as("actualShiftStartDateTime"));
			projectionList.add(Projections.groupProperty("tsemUser.userId").as("testerId"));
			
			projectionList.add(Projections.groupProperty("resShftCheckIn.checkIn").as("productResourceShiftCheckIn"));
			
			projectionList.add(Projections.groupProperty("wPackage.workPackageId").as("workPackageId"));
			projectionList.add(Projections.groupProperty("userRole.userRoleId").as("userRoleId"));
			
			projectionList.add(Projections.sum("tsem.hours").as("hoursWorked"));
			projectionList.add(Projections.sum("tsem.mins").as("minutesWorked"));
			
			
			c.setProjection(projectionList);
			
			c.addOrder(Order.asc("tsem.date"));
			c.addOrder(Order.asc("actlShift.startTime"));
			c.addOrder(Order.asc("tsemUser.userId"));
			c.addOrder(Order.asc("resShftCheckIn.checkIn"));
			c.addOrder(Order.asc("workPackage.workPackageId"));
		
			List<Object[]> list = c.list();
			log.info("listTimeCompleteSheetStatistics size : " + list.size());
			TimeSheetStatisticsDTO timeSheetStatisticsDTO = null;
			for (Object[] row : list) {
				timeSheetStatisticsDTO = new TimeSheetStatisticsDTO();
				
				timeSheetStatisticsDTO.setShiftId((Integer)row[0]);
				timeSheetStatisticsDTO.setShiftName((String)row[1]);

				timeSheetStatisticsDTO.setWorkShiftStartDateTime((Date)row[2]);
				timeSheetStatisticsDTO.setWorkShiftEndDateTime((Date)row[3]);
				timeSheetStatisticsDTO.setShiftTypeId((Integer)row[4]);
				
				timeSheetStatisticsDTO.setActualShiftEndDateTime((Date)row[5]);
				
				timeSheetStatisticsDTO.setTesterName((String)row[6]);
				timeSheetStatisticsDTO.setUserFirstName((String)row[7]);
				timeSheetStatisticsDTO.setUserLastName((String)row[8]);
				timeSheetStatisticsDTO.setUserRoleName((String)row[9]);
				timeSheetStatisticsDTO.setCustomerId((Integer)row[10]);
				timeSheetStatisticsDTO.setVendorName((String)row[11]);
				
				timeSheetStatisticsDTO.setTestFactoryId((Integer)row[12]);
				timeSheetStatisticsDTO.setTestFactoryName((String)row[13]);
				
				timeSheetStatisticsDTO.setProductId((Integer)row[14]);
				timeSheetStatisticsDTO.setProductName((String)row[15]);
				timeSheetStatisticsDTO.setProjectCode((String)row[16]);
				timeSheetStatisticsDTO.setWorkPackageName((String)row[17]);
				
				timeSheetStatisticsDTO.setShiftAttendanceGraceTime((Integer)row[18]);
				timeSheetStatisticsDTO.setShiftLunchAuthorisedTime((Integer)row[19]);
				timeSheetStatisticsDTO.setShiftBreaksAuthorisedTime((Integer)row[20]);
				timeSheetStatisticsDTO.setWeeklyOverTimeLimit((Integer)row[21]);
				
				timeSheetStatisticsDTO.setProductResourceShiftCheckInId((Integer)row[22]);
				timeSheetStatisticsDTO.setProductResourceShiftCheckOut((Date)row[23]);
				
				timeSheetStatisticsDTO.setActualExecutionDate((Date)row[24]);
				timeSheetStatisticsDTO.setActualShiftStartDateTime((Date)row[25]);
				timeSheetStatisticsDTO.setTesterId((Integer)row[26]);
				
				timeSheetStatisticsDTO.setProductResourceShiftCheckIn((Date)row[27]);
				
				timeSheetStatisticsDTO.setWorkPackageId((Integer)row[28]);
				timeSheetStatisticsDTO.setUserRoleId((Integer)row[29]);
				
				Long longTimeHoursWorked = (Long)row[30];
				timeSheetStatisticsDTO.setHoursWorked(Integer.valueOf(longTimeHoursWorked.intValue()));

				Long longTimeMinutesWorked = (Long)row[31];
				timeSheetStatisticsDTO.setMinutesWorked(Integer.valueOf(longTimeMinutesWorked.intValue()));
				
				timeSheetStatisticsDTOList.add(timeSheetStatisticsDTO);
			}
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return timeSheetStatisticsDTOList;
	}

	@Override
	@Transactional
	public List<Activity> listActivitiesByDate(
			Date dataFromDate, Date dataToDate,Integer productId) {
		List<Activity> listOfActivitys = new ArrayList<Activity>();
		try {

			Criteria c = sessionFactory.getCurrentSession().createCriteria(Activity.class,"activity");
			c.createAlias("activity.activityWorkPackage", "activityWp");
			c.createAlias("activityWp.productBuild", "pbuild");
			c.createAlias("pbuild.productVersion", "pVersion");
			c.createAlias("pVersion.productMaster", "product");
			c.add(Restrictions.between("activity.plannedEndDate", dataFromDate,  dataToDate));
			c.add(Restrictions.eq("product.productId", productId));
			listOfActivitys = c.list();
			if(listOfActivitys != null && listOfActivitys.size()>0){
				for (Activity activity : listOfActivitys) {
					Hibernate.initialize(activity.getStatusCategory());
					Hibernate.initialize(activity.getPriority());
					Hibernate.initialize(activity.getReviewer());
					Hibernate.initialize(activity.getAssignee());
					Hibernate.initialize(activity.getProductFeature());
					Hibernate.initialize(activity.getActivityWorkPackage());
					Hibernate.initialize(activity.getActivityWorkPackage().getOwner());
					Hibernate.initialize(activity.getActivityWorkPackage().getProductBuild());
					Hibernate.initialize(activity.getActivityWorkPackage().getProductBuild().getBuildType());
					Hibernate.initialize(activity.getActivityWorkPackage().getProductBuild().getProductVersion());
					Hibernate.initialize(activity.getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster());
					Hibernate.initialize(activity.getCategory());
					Hibernate.initialize(activity.getActivityMaster());
				}
			}
		}catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return listOfActivitys;
	}

	@Override
	@Transactional
	public List<Activity> getActivitiesByDate(Date dataFromDate, Date dataToDate) {
		List<Activity> listOfActivities = new ArrayList<Activity>();
		try {

			Criteria c = sessionFactory.getCurrentSession().createCriteria(ActivityTask.class,"activityTasks");
			c.add(Restrictions.between("activityTasks.actualEndDate", dataFromDate,  dataToDate));
			listOfActivities = c.list();
			if(listOfActivities != null && listOfActivities.size()>0){
				for (Activity activity : listOfActivities) {
					if(activity.getEnvironmentCombination() != null && activity.getEnvironmentCombination().size()>0){
						Hibernate.initialize(activity.getEnvironmentCombination());
					}
				}
			}
		}catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return listOfActivities;
	}
	@Override
	@Transactional
	public List<Object[]> getActivityTaskEffortReport(Integer productId) {
		log.debug("getActivityTaskEffortReport with productId>>>"+productId);
		List<Object[]> activityTaskEffortList = null;
		try {
			
			String sql="SELECT pm.productName, awp.activityWorkPackageName, aw.activityName, a.activityTaskId,a.activityTaskName, "
					+ " at.activityTaskTypeName, wf.actualEffort, u.loginId, p.executionPriority, a.plannedStartDate, a.plannedEndDate, a.createdDate, a.remark "
					+ " FROM wf_workflow_events wf "
					+ " INNER JOIN activity_task a ON wf.entityInstanceId=a.activityTaskId "
					+ " INNER JOIN activity_task_type AT ON a.activityTaskTypeId=at.activityTaskTypeId "
					+ " INNER JOIN activity aw ON a.activityId=aw.activityId "
					+ " INNER JOIN activity_work_package awp ON aw.activityWorkPackageId=awp.activityWorkPackageId "
					+ " INNER JOIN execution_priority p ON a.priorityId=p.executionPriorityId "
					+ " INNER JOIN product_master pm ON wf.productId = pm.productId "
					+ " INNER JOIN user_list u ON a.assigneeId=u.userId "
					+ " AND wf.productId=:prodId AND wf.entityTypeId IN (29,30) ORDER BY wf.entityInstanceId ASC; ";
		
			activityTaskEffortList=sessionFactory.getCurrentSession().createSQLQuery(sql).
					setParameter("prodId", productId).
					list();
				
			log.debug("getActivityTaskEffortReport list :"+activityTaskEffortList.size());
		
		} catch (RuntimeException re) {
			log.error("getActivityTaskEffortReport with productId", re);	
		}
		return activityTaskEffortList;
	}
	
	
	@Override
	@Transactional
	public List<Object[]> getResourceEffortReport(Integer productId) {
		log.debug("getResourceEffortReport with productId>>>"+productId);
		List<Object[]> activityTaskEffortList = null;
		try {
			
			String sql="SELECT u.loginId,pm.productName, awp.activityWorkPackageName, aw.activityName,a.activityTaskName, "
					+ " at.activityTaskTypeName,wfstatus.workflowStatusDisplayName,wf.actualEffort, p.executionPriority, wf.lastUpdatedDate, wf.plannedStartDate, wf.plannedEndDate,a.remark ,wf.slaDurationPlanned,wf.slaDurationActual,wf.entityInstanceId "
					+ " FROM wf_workflow_events wf "
					+ " INNER JOIN activity_task a ON wf.entityInstanceId=a.activityTaskId "
					+ " INNER JOIN activity_task_type AT ON a.activityTaskTypeId=at.activityTaskTypeId "
					+ " INNER JOIN activity aw ON a.activityId=aw.activityId "
					+ " INNER JOIN activity_work_package awp ON aw.activityWorkPackageId=awp.activityWorkPackageId "
					+ " INNER JOIN execution_priority p ON a.priorityId=p.executionPriorityId "
					+ " INNER JOIN product_master pm ON wf.productId = pm.productId "
					+ " INNER JOIN user_list u ON wf.modifiedBy=u.userId "
					+ " INNER JOIN wf_workflows_status wfstatus ON wfstatus.workflowStatusId=wf.targetStatusId "
					+ " AND wf.productId=:prodId AND wf.entityTypeId IN (29,30) ORDER BY wf.entityInstanceId ASC; ";
		
			activityTaskEffortList=sessionFactory.getCurrentSession().createSQLQuery(sql).
					setParameter("prodId", productId).
					list();
				
			log.debug("getActivityTaskEffortReport list :"+activityTaskEffortList.size());
		
		} catch (RuntimeException re) {
			log.error("getActivityTaskEffortReport with productId", re);	
		}
		return activityTaskEffortList;
	}
	@Override
	@Transactional
	public void addTaskEffortTemplate(TaskEffortTemplate taskEffortTemplate) {
		log.info("adding addTaskEffortTemplate instance");
		try {	
			sessionFactory.getCurrentSession().save(taskEffortTemplate);
			log.info("add addTaskEffortTemplate successful");
		} catch (RuntimeException re) {
			log.error("add addTaskEffortTemplate failed", re);
			//throw re;
		}	
		
	}
	@Override
	@Transactional
	public List<TaskEffortTemplate> getTaskEffortTemplateList() {
		List<TaskEffortTemplate> listOfTaskEffortTemplates = new ArrayList<TaskEffortTemplate>();
		try {

			Criteria c = sessionFactory.getCurrentSession().createCriteria(TaskEffortTemplate.class,"tets");
			
			c.add(Restrictions.eq("tets.isActive", 1));
			c.addOrder(Order.desc("tets.templateId"));
			listOfTaskEffortTemplates = c.list();
			
		}catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return listOfTaskEffortTemplates;
	}
	@Override
	@Transactional
	public List<Object[]> getActivityTaskEffortReportBasedOnFilter(String productIdVal, String statusVal, Date fromDate, Date toDate, String resourceVal) {
		log.debug("getActivityTaskEffortReportBasedOnFilter with productId>>>"+productIdVal+">>>statusVal>>>"+statusVal);
		List<Object[]> activityTaskEffortList = null;
		try {
			
			String sql="SELECT pm.productName,awp.activityWorkPackageName,aw.activityName,a.activityTaskName, u.loginId, ws.workflowStatusName, "
					+ " wf.actualEffort, wf.actualStartDate, wf.actualEndDate, wf.plannedStartDate, wf.plannedEndDate, wf.comments  "
					+ " FROM wf_workflow_events wf "
					+ " INNER JOIN activity_task a ON wf.entityInstanceId=a.activityTaskId "
					+ " INNER JOIN activity aw ON a.activityId=aw.activityId "
					+ " INNER JOIN activity_work_package awp ON aw.activityWorkPackageId=awp.activityWorkPackageId "
					+ " INNER JOIN wf_workflows_status ws ON wf.targetStatusId=ws.workflowStatusId "
					+ " INNER JOIN product_master pm ON wf.productId = pm.productId "
					+ " INNER JOIN user_list u ON wf.modifiedBy=u.userId "
					+ " AND wf.entityTypeId IN (29,30) ";
					if(productIdVal!=null && !productIdVal.equals(""))
						sql= sql+ " AND wf.productId IN ("+productIdVal+") ";
					if(statusVal!=null && !statusVal.equals(""))
						sql= sql+ " AND wf.targetStatusId IN ("+statusVal+") ";
					if(resourceVal!=null && !resourceVal.equals(""))
						sql= sql+ " AND wf.modifiedBy IN ("+resourceVal+") ";
					if(fromDate!=null && toDate!=null)
						sql= sql+ "AND (wf.plannedEndDate BETWEEN '"+fromDate+"' AND '"+toDate+"')";
					sql= sql+ " ORDER BY wf.entityInstanceId ASC; ";
		
			activityTaskEffortList=sessionFactory.getCurrentSession().createSQLQuery(sql).list();
				
			log.debug("getActivityTaskEffortReportBasedOnFilter list :"+activityTaskEffortList.size());
		
		} catch (RuntimeException re) {
			log.error("getActivityTaskEffortReportBasedOnFilter with filter", re);	
		}
		return activityTaskEffortList;
	}
	@Override
	@Transactional
	public TaskEffortTemplate getTaskEffortTemplateById(Integer templateId) {
		List<TaskEffortTemplate> taskEffortTemplateList=null;
		TaskEffortTemplate taskEffortTemplate=null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TaskEffortTemplate.class, "taskEffortTemplate");		
			c.add(Restrictions.eq("taskEffortTemplate.templateId", templateId));			
			taskEffortTemplateList = c.list();
			if(taskEffortTemplateList!= null && taskEffortTemplateList.size() >0) {
				taskEffortTemplate= taskEffortTemplateList.get(0);
				if(taskEffortTemplate!= null) {
					Hibernate.initialize(taskEffortTemplate.getMappedFields());
					Hibernate.initialize(taskEffortTemplate.getProductValue());
					Hibernate.initialize(taskEffortTemplate.getFromDateValue());
					Hibernate.initialize(taskEffortTemplate.getToDateValue());
					Hibernate.initialize(taskEffortTemplate.getStatusValue());
					Hibernate.initialize(taskEffortTemplate.getTemplateName());
					Hibernate.initialize(taskEffortTemplate.getResourceValue());
				}
				return taskEffortTemplate;
			}
		}catch(Exception e) {
			
		}
		
		return null;
	}
	@Override
	@Transactional
	public List<MongoCollection> getMongoCollectionList() {
		List<MongoCollection> listOfMongoCollections = new ArrayList<MongoCollection>();
		try {

			Criteria c = sessionFactory.getCurrentSession().createCriteria(MongoCollection.class,"tets");
			
			c.add(Restrictions.eq("tets.activeStatus", 1));
			c.addOrder(Order.asc("tets.collectionId"));
			listOfMongoCollections = c.list();
			
		}catch (RuntimeException re) {
			log.error(" getMongoCollectionList failed", re);
		}
		return listOfMongoCollections;
	}
	@Override
	@Transactional
	public void addPivotRestTemplate(PivotRestTemplate pivotRestTemplate) {
		log.info("adding pivotRestTemplate instance");
		try {	
			sessionFactory.getCurrentSession().save(pivotRestTemplate);
			log.info("add pivotRestTemplate successful");
		} catch (RuntimeException re) {
			log.error("add pivotRestTemplate failed", re);
			//throw re;
		}	
		
	}
	@Override
	@Transactional
	public List<Object[]> getPivotRestTemplateList(Integer templateId) {
		List<Object[]> listOfPivotRestTemplate = null;
		try {
			
			String sql="SELECT pt.factoryId, pt.productId, pt.collectionId, pt.templateName, pt.cubeName, pt.configJsonValue "
					+ " FROM pivot_rest_template pt where"
					+ " pt.templateId=:templateId ; ";
		
			listOfPivotRestTemplate=sessionFactory.getCurrentSession().createSQLQuery(sql).
					setParameter("templateId", templateId).					
					list();
				
			log.debug("getActivityTaskEffortReport list :"+listOfPivotRestTemplate.size());
		
		} catch (RuntimeException re) {
			log.error("getActivityTaskEffortReport with productId", re);	
		}
		return listOfPivotRestTemplate;
	}
	@Override
	@Transactional
	public List<PivotRestTemplate> getPivotRestTemplateReportByParams(Integer factoryId, Integer productId, Integer collectionId){
		List<PivotRestTemplate> listOfPivotRestTemplate = new ArrayList<PivotRestTemplate>();
		try {

			Criteria c = sessionFactory.getCurrentSession().createCriteria(PivotRestTemplate.class,"tets");
			if(factoryId != null){
				c.add(Restrictions.eq("tets.factoryId", factoryId));
			}
			if(productId != null){
				c.add(Restrictions.eq("tets.productId", productId));
			}
			
			c.add(Restrictions.eq("tets.collectionId", collectionId));
			c.add(Restrictions.eq("tets.activeStatus", 1));
			c.addOrder(Order.desc("tets.templateId"));
			listOfPivotRestTemplate = c.list();
			
		}catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return listOfPivotRestTemplate;	
		
	}
	@Override
	@Transactional
	public List<PivotRestTemplate> getPivotRestTemplateList() {
		List<PivotRestTemplate> listOfPivotRestTemplates = new ArrayList<PivotRestTemplate>();
		try {

			Criteria c = sessionFactory.getCurrentSession().createCriteria(PivotRestTemplate.class,"tets");
			
			c.add(Restrictions.eq("tets.activeStatus", 1));
			c.addOrder(Order.desc("tets.templateId"));
			listOfPivotRestTemplates = c.list();
			
		}catch (RuntimeException re) {
			log.error(" getPivotRestTemplateList failed", re);
		}
		return listOfPivotRestTemplates;
	}
	@Override
	@Transactional
	public List<PivotRestTemplateDTO> getPivotRestTemplateList(Integer collectionId, Integer jtStartIndex, Integer jtPageSize){
		
		List<Object[]> list = null;
		List<PivotRestTemplateDTO> listOfPivotRestTemplate=new ArrayList<PivotRestTemplateDTO>();
		try {
			
			String sql="";
			if(collectionId!=null && collectionId!=0){
				sql="SELECT m.displayName, p.templateId, p.templateName, p.description, u.loginId, p.createdDate FROM pivot_rest_template AS p, mongo_collections AS m, user_list AS u   "
					+ " WHERE m.collectionId=p.collectionId AND u.userId=p.createdBy AND p.activeStatus=1 AND p.collectionId=:collectionId "
					+ " ORDER BY p.templateId DESC";
				sql = sql+" OFFSET "+jtStartIndex+" LIMIT "+jtPageSize+"";
				
				list=sessionFactory.getCurrentSession().createSQLQuery(sql).setParameter("collectionId", collectionId).list();
			}else{
				sql="SELECT m.displayName, p.templateId, p.templateName, p.description, u.loginId, p.createdDate FROM pivot_rest_template AS p, mongo_collections AS m, user_list AS u   "
					+ " WHERE m.collectionId=p.collectionId AND u.userId=p.createdBy AND p.activeStatus=1 "
					+ " ORDER BY p.templateId DESC";
				sql = sql+" OFFSET "+jtStartIndex+" LIMIT "+jtPageSize+"";
			
				list=sessionFactory.getCurrentSession().createSQLQuery(sql).list();
			}
			
			log.info("list object[] size : " + list.size());
			PivotRestTemplateDTO pivotRestTemplateDTO = null;
			for (Object[] row : list) {
				pivotRestTemplateDTO = new PivotRestTemplateDTO();
				pivotRestTemplateDTO.setReportType((String)row[0]);
				pivotRestTemplateDTO.setTemplateId((Integer)row[1]);
				pivotRestTemplateDTO.setTemplateName((String)row[2]);
				pivotRestTemplateDTO.setDescription((String)row[3]);
				pivotRestTemplateDTO.setUserName((String)row[4]);
				pivotRestTemplateDTO.setCreatedDate((Date)row[5]);
				listOfPivotRestTemplate.add(pivotRestTemplateDTO);
			}
			log.info("getlistOfPivotRestTemplate list :"+listOfPivotRestTemplate.size());
			return listOfPivotRestTemplate;
		} catch (RuntimeException re) {
			log.error("getActivityTaskEffortReport with productId", re);	
			return null;
		}
	}
	@Override
	@Transactional
	public String deletePivotRestReportById(int templateId) {
		String message="";
		try{
			
			Session session = sessionFactory.getCurrentSession();
			
				String hql = "delete from PivotRestTemplate where templateId=:templateId";
				session.createQuery(hql).setInteger("templateId", templateId).executeUpdate();
				log.info("deletion of report successful");
				message="OK";
			
		}catch(RuntimeException re){
			log.error("deletion of report failed", re);
			message="ERROR";
		}
		return message;
	}
	@Override
	@Transactional
	public List<Object[]> getProductAndEngagementNameList(String productIds) {
		List<Object[]> productAndEngagementNameList = null;
		try {
			
			String sql="SELECT DISTINCT t.testFactoryId, t.displayName, p.productId, p.productName "
					+ " FROM product_master AS p, test_factory AS t WHERE p.testFactoryId=t.testFactoryId "
					+ " AND p.productId IN ("+productIds+") ORDER BY t.testFactoryId, p.productId ASC ; ";
			log.info("sql>>>>>"+sql);
		
			productAndEngagementNameList=sessionFactory.getCurrentSession().createSQLQuery(sql).list();
				
			log.debug("getProductAndEngagementNameList list :"+productAndEngagementNameList.size());
		
		} catch (RuntimeException re) {
			log.error("getProductAndEngagementNameList with productId", re);	
		}
		return productAndEngagementNameList;
	}

	@Override
	@Transactional
	public List<ReportIssue> getReportIssueList() {
		List<ReportIssue> reportIssueList = null;
		try{
			reportIssueList = sessionFactory.getCurrentSession().createQuery("from ReportIssue").list();
			log.debug("list all ReportIssue successful");
		}catch(RuntimeException re){
			log.error("getReportIssueList ",re);
		}
		return reportIssueList;
	}
	@Override
	@Transactional
	public void updateReportIssue(ReportIssue reportIssue){
		try{
			sessionFactory.getCurrentSession().saveOrUpdate(reportIssue);
		    log.debug("update reportIssue successfull");	
		}catch(RuntimeException re){
			log.error("updateReportIssue ",re);
		}
		
	}
	
	
	@Override
	@Transactional
	public void addReportIssue(ReportIssue reportIssue) {
		log.info("adding addReportIssue instance");
		try {	
			sessionFactory.getCurrentSession().save(reportIssue);
			log.debug("adding reportIssue successfull");
		} catch (RuntimeException re) {
			log.error("add reportIssue failed", re);
		}	
		
	}
}
