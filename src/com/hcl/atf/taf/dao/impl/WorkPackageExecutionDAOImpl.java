package com.hcl.atf.taf.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.dao.ProductLocaleDao;
import com.hcl.atf.taf.dao.WorkPackageExecutionDAO;
import com.hcl.atf.taf.model.Attachment;
import com.hcl.atf.taf.model.GenericDevices;
import com.hcl.atf.taf.model.MobileType;
import com.hcl.atf.taf.model.ServerType;
import com.hcl.atf.taf.model.TestCaseExecutionResult;
import com.hcl.atf.taf.model.TestRunJob;
import com.hcl.atf.taf.model.TestStepExecutionResult;
import com.hcl.atf.taf.model.TestcaseExecutionEvent;
import com.hcl.atf.taf.model.WorkPackage;
import com.hcl.atf.taf.model.dto.WorkPackageTCEPStatusSummaryDTO;

@Repository
public class WorkPackageExecutionDAOImpl implements WorkPackageExecutionDAO {
	private static final Log log = LogFactory.getLog(WorkPackageExecutionDAOImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private ProductLocaleDao productLocaleDao;
	
	@Override
	@Transactional
	public List<WorkPackageTCEPStatusSummaryDTO> listWorkPackageTCEPStatusSummaryForTester(Integer testerId) {

		log.info("listing workPackageStatusSummary: ");
		List<WorkPackageTCEPStatusSummaryDTO> listOfWorkPackageStatusSummaryDTO = new ArrayList<WorkPackageTCEPStatusSummaryDTO>();
		log.info("Getting Work Package TCEP Summary for Tester Id: " + testerId);
		
		if (testerId == null)
			return null;
		
		try {
			
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackage.class, "wp");
			c.createAlias("wp.workPackageTestCases", "wptclist");
			c.createAlias("wp.workPackageTestCaseExecutionPlan", "wptceplist");
			ProjectionList projectionList = Projections.projectionList();
			
			projectionList.add(Property.forName("wp.workPackageId").as("workPackageId"));
			projectionList.add(Property.forName("wp.name").as("workPackageName"));
			projectionList.add(Projections.groupProperty("wp.workPackageId"));

			c.add(Restrictions.eq("wptceplist.tester.userId", testerId.intValue()));
			projectionList.add(Property.forName("wptceplist.tester.userId").as("testerId"));
			projectionList.add(Property.forName("wptceplist.tester.loginId").as("testerName"));
			projectionList.add(Projections.groupProperty("wptceplist.tester.userId"));

			projectionList.add(Projections.countDistinct("wptceplist.id").as("totalTestCases"));
			c.setProjection(projectionList);
			
			List<Object[]> list = c.list();
			log.info("Result Set Size : " + list.size());
			WorkPackageTCEPStatusSummaryDTO workPackageTCEPStatusSummaryDTO = null;
			for (Object[] row : list) {
				int i = 0;
				workPackageTCEPStatusSummaryDTO = new WorkPackageTCEPStatusSummaryDTO();
				workPackageTCEPStatusSummaryDTO.setWorkPackageId((Integer)row[i++]);
				workPackageTCEPStatusSummaryDTO.setWorkPackageName((String)row[i++]);
				workPackageTCEPStatusSummaryDTO.setTesterId(((Long)row[i++]).intValue());
				workPackageTCEPStatusSummaryDTO.setTesterName(((String)row[i++]));
				workPackageTCEPStatusSummaryDTO.setTotalTestCaseForExecutionCount(((Long)row[i++]).intValue());
				listOfWorkPackageStatusSummaryDTO.add(workPackageTCEPStatusSummaryDTO);
			}


			///
			c = sessionFactory.getCurrentSession().createCriteria(WorkPackage.class, "wp");
			c.createAlias("wp.workPackageTestCases", "wptclist");
			c.createAlias("wp.workPackageTestCaseExecutionPlan", "wptceplist");
			c.add(Restrictions.eq("wptceplist.isExecuted", 1));
			projectionList = Projections.projectionList();
			projectionList.add(Property.forName("wp.workPackageId").as("workPackageId"));
			projectionList.add(Projections.countDistinct("wptceplist.id").as("completedTestCases"));
			projectionList.add(Projections.groupProperty("wp.workPackageId"));
			projectionList.add(Projections.groupProperty("wptceplist.tester.userId"));
			c.setProjection(projectionList);
			
			list = c.list();
			Integer wpId = 0;
			for (Object[] row : list) {
				wpId = ((Integer)row[0]);
				for(WorkPackageTCEPStatusSummaryDTO summaryDTO : listOfWorkPackageStatusSummaryDTO) {
					if (summaryDTO.getWorkPackageId() == wpId) {
						summaryDTO.setCompletedTestCaseCount(((Integer)row[1]));
						break;
					}
				}
			}
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return listOfWorkPackageStatusSummaryDTO;
	}

	@Override
	@Transactional
	public TestRunJob getTestRunJobById(Integer testRunJobId) {
		List<TestRunJob> testRunJobs = null;
		TestRunJob testRunJob=null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestRunJob.class, "trj");
			
			c.add(Restrictions.eq("trj.testRunJobId", testRunJobId));

			testRunJobs = c.list();
			testRunJob = (testRunJobs != null && testRunJobs.size() != 0) ? (TestRunJob) testRunJobs.get(0) : null;
			if (testRunJob != null) {
				GenericDevices gd = testRunJob.getGenericDevices();	
					Hibernate.initialize(testRunJob.getGenericDevices());
					if(testRunJob.getGenericDevices()!=null && testRunJob.getGenericDevices().getPlatformType()!=null)
						Hibernate.initialize(testRunJob.getGenericDevices().getPlatformType());
					if((gd instanceof MobileType) && ((MobileType) gd).getDeviceMakeMaster() != null){
						Hibernate.initialize(((MobileType) gd).getDeviceMakeMaster());
					}
					if((gd instanceof ServerType) ){
						if(((ServerType) gd).getProcessor() != null){
							Hibernate.initialize(((ServerType) gd).getProcessor());	
						}
						if(((ServerType) gd).getSystemType() != null){
							Hibernate.initialize(((ServerType) gd).getSystemType());	
						}						
					}
					Hibernate.initialize(testRunJob.getHostList());
					Hibernate.initialize(testRunJob.getEnvironmentCombination());

					if(testRunJob.getTestRunPlan() != null){
						
						Hibernate.initialize(testRunJob.getTestRunPlan());
						if(testRunJob.getTestRunPlan()!=null && testRunJob.getTestRunPlan().getProductVersionListMaster()!=null && testRunJob.getTestRunPlan().getProductVersionListMaster().getProductMaster()!=null)
							Hibernate.initialize(testRunJob.getTestRunPlan().getProductVersionListMaster().getProductMaster());						
						if(testRunJob.getTestRunPlan().getRunConfigurationList() != null && !testRunJob.getTestRunPlan().getRunConfigurationList().isEmpty()){
							Hibernate.initialize(testRunJob.getTestRunPlan().getRunConfigurationList());
						}
						
						if(testRunJob.getWorkPackage().getTestRunPlan() != null && testRunJob.getWorkPackage().getTestRunPlan().getResultsReportingMode() != null)
							Hibernate.initialize(testRunJob.getWorkPackage().getTestRunPlan().getResultsReportingMode());
					}
					Hibernate.initialize(testRunJob.getTestRunPlan().getAttachments());
					if( testRunJob.getTestRunPlan().getAttachments() != null &&  testRunJob.getTestRunPlan().getAttachments().size()>0){
						Set<Attachment> attachmentSet= testRunJob.getTestRunPlan().getAttachments();
						Hibernate.initialize(attachmentSet);
						for(Attachment attach: attachmentSet){
							Hibernate.initialize(attach);
						}
					}
					Hibernate.initialize(testRunJob.getTestSuite());
					Hibernate.initialize(testRunJob.getWorkPackage());
					Hibernate.initialize(testRunJob.getWorkPackage().getTestRunJobSet());
					Hibernate.initialize(testRunJob.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductType());
					Hibernate.initialize(testRunJob.getWorkPackage().getCombinedResultsReportingJob());
					if(testRunJob.getTestRunStatus() != null) {
						Hibernate.initialize(testRunJob.getTestRunStatus());
					}
			}
		} catch (Exception e) {
			log.error(e);
		}
		return testRunJob;
	}

	@Override
	@Transactional
	public String getTestRunJobResultStatus(int testRunJobId) {
		
		try {

			//Check if any test steps failed
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestStepExecutionResult.class, "tser");
			c.createAlias("tser.testCaseExecutionResult", "tcer");
			c.createAlias("tcer.workPackageTestCaseExecutionPlan", "wptceplist");
			c.createAlias("wptceplist.testRunJob", "trj");

			List<String> failedStatusList = new ArrayList<String>();
			failedStatusList.add(IDPAConstants.EXECUTION_RESULT_FAILED);
			failedStatusList.add(IDPAConstants.EXECUTION_RESULT_BLOCKED);
			
			c.add(Restrictions.in("tser.result", failedStatusList));
			c.add(Restrictions.eq("trj.testRunJobId", testRunJobId));
			
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.countDistinct("tser.teststepexecutionresultid").as("totalFailedTestSteps"));
			c.setProjection(projectionList);
			
			List<Object[]> list = c.list();
			if (list.size() > 0)
				return IDPAConstants.EXECUTION_RESULT_FAILED;
			
			//Check if any test cases failed
			c = sessionFactory.getCurrentSession().createCriteria(TestCaseExecutionResult.class, "tcer");
			c.createAlias("tcer.workPackageTestCaseExecutionPlan", "wptceplist");
			c.createAlias("wptceplist.testRunJob", "trj");

			c.add(Restrictions.in("tcer.result", failedStatusList));
			c.add(Restrictions.eq("trj.testRunJobId", testRunJobId));
			
			projectionList = Projections.projectionList();
			projectionList.add(Projections.countDistinct("tcer.testCaseExecutionResultId").as("totalFailedTestCases"));
			c.setProjection(projectionList);
			
			list = c.list();
			if (list.size() > 0)
				return IDPAConstants.EXECUTION_RESULT_FAILED;
			else 
				return IDPAConstants.EXECUTION_RESULT_PASSED;


		} catch (Exception e) {
			log.error("Unable to get Result status of Test Run Job : " + testRunJobId, e);
			return IDPAConstants.EXECUTION_RESULT_FAILED;
		}
	}

	@Override
	@Transactional
	public String getWorkpackageResultStatus(int workPackageId) {
		try {

			//Check if any test steps failed

			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestStepExecutionResult.class, "tser");
			c.createAlias("tser.testCaseExecutionResult", "tcer");
			c.createAlias("tcer.workPackageTestCaseExecutionPlan", "wptceplist");
			c.createAlias("wptceplist.workPackage", "wp");

			List<String> failedStatusList = new ArrayList<String>();
			failedStatusList.add(IDPAConstants.EXECUTION_RESULT_FAILED);
			failedStatusList.add(IDPAConstants.EXECUTION_RESULT_BLOCKED);
			
			c.add(Restrictions.in("tser.result", failedStatusList));
			c.add(Restrictions.eq("wp.workPackageId", workPackageId));
			
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.countDistinct("tser.teststepexecutionresultid").as("totalFailedTestSteps"));
			c.setProjection(projectionList);
			
			List<Object[]> list = c.list();
			if (list.size() > 0)
				return IDPAConstants.EXECUTION_RESULT_FAILED;
			
			//Check if any test cases failed
			c = sessionFactory.getCurrentSession().createCriteria(TestCaseExecutionResult.class, "tcer");
			c.createAlias("tcer.workPackageTestCaseExecutionPlan", "wptceplist");
			c.createAlias("wptceplist.workPackage", "wp");

			c.add(Restrictions.in("tcer.result", failedStatusList));
			c.add(Restrictions.eq("wp.workPackageId", workPackageId));
			
			projectionList = Projections.projectionList();
			projectionList.add(Projections.countDistinct("tcer.testCaseExecutionResultId").as("totalFailedTestCases"));
			c.setProjection(projectionList);
			
			list = c.list();
			if (list.size() > 0)
				return IDPAConstants.EXECUTION_RESULT_FAILED;
			else 
				return IDPAConstants.EXECUTION_RESULT_PASSED;


		} catch (Exception e) {
			log.error("Unable to get Result status of Workpackage : " + workPackageId, e);
			return IDPAConstants.EXECUTION_RESULT_FAILED;
		}
	}

	@Override
	public int getCompletedJobsCount(int workPackageId) {
		
		log.info("getCompletedJobsCount");
		
		try {
			
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestRunJob.class, "trj");
			c.createAlias("trj.workPackage", "wp");
			ProjectionList projectionList = Projections.projectionList();
			
			List<Integer> jobCompletionStatuses = new ArrayList<Integer>();
			jobCompletionStatuses.add(IDPAConstants.JOB_COMPLETED);
			jobCompletionStatuses.add(IDPAConstants.JOB_ABORTED);
			c.add(Restrictions.in("trj.testRunStatus", jobCompletionStatuses));
			c.add(Restrictions.eq("wp.workPackageId", workPackageId));
			
			projectionList.add(Projections.countDistinct("trj.testRunJobId").as("completedJobsCount"));
			c.setProjection(projectionList);
			
			List<Object[]> list = c.list();
			log.info("Result Set Size : " + list.size());
			Object[] row = list.get(0);
			int completedJobsCount = (Integer) row[0];
			return completedJobsCount;
		} catch (Exception e) {
			log.error("Error while getting completed jobs count for workpackage  : " + workPackageId);
			return 0;
		}
	}

	@Override
	public Integer countAllWorkpackages(Date startDate, Date endDate) {
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackage.class,"wpk");
			if (startDate != null) {
				c.add(Restrictions.ge("wpk.createDate", startDate));
			}
			if (endDate != null) {
				c.add(Restrictions.le("wpk.createDate", endDate));
			}
			
			c.setProjection(Projections.rowCount());
			Integer count = Integer.parseInt(c.uniqueResult().toString());
			return count;
		} catch (Exception e) {
			log.error("Unable to get count of all WorkPackageList", e);
			return -1;
		}
	}

	@Override
	@Transactional
	public void addTestcaseExecutionEvent(TestcaseExecutionEvent testcaseExecutionEvent) {

		log.debug("adding TestcaseExecutionEvent instance");
		try {
			sessionFactory.getCurrentSession().save(testcaseExecutionEvent);
			log.debug("added TestcaseExecutionEvent successfully");
		} catch (RuntimeException re) {
			log.error("adding TestcaseExecutionEvent failed", re);		
		}
		
	}

	@Override
	@Transactional
	public TestcaseExecutionEvent getTestcaseExecutiontEvent(String eventName, String testcaseName, Integer jobId, Integer workPackageId) {

		log.debug("getting TestcaseExecutionEvent instance");
		TestcaseExecutionEvent testcaseExecutionEvent = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestcaseExecutionEvent.class, "event");			
			c.add(Restrictions.eq("event.eventName", eventName));
			
			/*if(testcaseName != null || !testcaseName.equalsIgnoreCase("null") || !testcaseName.isEmpty()){
				c.add(Restrictions.eq("event.testcaseName", testcaseName)); 
			}*/
			/*if(jobId != null){
				c.add(Restrictions.eq("event.jobId", jobId));
			}*/
			if(workPackageId != null){
				c.add(Restrictions.eq("event.workpackageId", workPackageId));
			}
			List list = c.list();
			testcaseExecutionEvent = (list!=null && list.size()!=0) ? (TestcaseExecutionEvent)list.get(0) : null;		
			
			if(testcaseExecutionEvent != null){
				Hibernate.initialize(testcaseExecutionEvent.getEventName());
				Hibernate.initialize(testcaseExecutionEvent.getPayload());
				Hibernate.initialize(testcaseExecutionEvent.getTestcaseName());				
				Hibernate.initialize(testcaseExecutionEvent.getExpiryPolicy());
			}
		} catch (RuntimeException re) {
			log.error("adding TestcaseExecutionEvent failed", re);		
		}
		return testcaseExecutionEvent;
	}

	@Override
	@Transactional
	public void deleteTestcaseExecutiontEvents(String eventName, Integer testcaseId, Integer jobId, Integer workPackageId, Integer expiryPolicy, Long expiryTime) {

		log.debug("deleting TestcaseExecutionEvents");
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestcaseExecutionEvent.class, "event");			
			if(eventName != null){
				c.add(Restrictions.eq("eventName", eventName));
			} else {
				c.add(Restrictions.eq("expiryPolicy", expiryPolicy));
			}
			if(testcaseId != null){
				c.add(Restrictions.eq("testcaseId", testcaseId)); 
			}
			if(jobId != null){
				c.add(Restrictions.eq("jobId", jobId));
			}
			if(workPackageId != null){
				c.add(Restrictions.eq("workPackageId", workPackageId));
			}
			if(expiryTime != null){
				c.add(Restrictions.le("expiryTime", expiryTime));
			}
			List list = c.list();
			if (list != null && list.size() > 0) {
				Iterator itList = list.iterator();
			    while(itList.hasNext()) {
			    	TestcaseExecutionEvent testcaseExecutionEvent = (TestcaseExecutionEvent) itList.next();
			    	sessionFactory.getCurrentSession().delete(testcaseExecutionEvent);
			    }
				log.info("Deleted events" + list.size());
			}
		} catch (RuntimeException re) {
			log.error("delete failed", re);
		}		
		
		
	}
	
	@Override
	@Transactional
	public void deleteTestcaseExecutiontEvent(TestcaseExecutionEvent testcaseExecutionEvent) {
		log.debug("Deleting TestCaseExecution Event "+testcaseExecutionEvent.getTestcaseExecutionEventId());
		try{
			sessionFactory.getCurrentSession().delete(testcaseExecutionEvent);
		} catch(Exception e){
			log.error("delete failed", e);
		}
	}

}
