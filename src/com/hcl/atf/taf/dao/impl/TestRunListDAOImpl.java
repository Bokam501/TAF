package com.hcl.atf.taf.dao.impl;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.constants.TestExecutionStatus;
import com.hcl.atf.taf.dao.TestRunListDAO;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.TestRunList;

@Repository
public class TestRunListDAOImpl implements TestRunListDAO {
	private static final Log log = LogFactory.getLog(TestRunListDAOImpl.class);
	
	@Autowired(required=true)
    private SessionFactory sessionFactory;
	
	@Override
	@Transactional
	public void delete(TestRunList testRunList) {
		log.debug("deleting TestRunList instance");
		try {
			sessionFactory.getCurrentSession().delete(testRunList);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
		}		
		
	}

	@Override
	@Transactional
	public void add(TestRunList testRunList) {
		TestRunList res=null;
		log.debug("adding TestRunList instance");
		try {
			 sessionFactory.getCurrentSession().save(testRunList);
			log.debug("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}
		
	}

	@Override
	@Transactional
	public void update(TestRunList testRunList) {
		log.debug("updating TestRunList instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(testRunList);
			log.debug("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
		}
	}
    
	@Override
	@Transactional
	public boolean hasTestRunCompleted(TestRunList testRunList) {
		log.debug("Checking if TestRun has completed");
		List<TestRunList> testRuns = null;
		try {
			int testRunConfigurationChildId = testRunList.getTestRunConfigurationChild().getTestRunConfigurationChildId().intValue();
			testRuns=sessionFactory.getCurrentSession().createQuery("from TestRunList e where e.testRunConfigurationChild.testRunConfigurationChildId=:testRunConfigurationChildId and e.runNo=:runNo")
					.setParameter("testRunConfigurationChildId", testRunConfigurationChildId)
					.setParameter("runNo", testRunList.getRunNo()).list();
			int totalJobs = testRuns.size();
			int completedJobs = 0;
			for (TestRunList trl : testRuns) {
				if (trl.getTestResultStatusMaster().getTestResultStatus().equals("PASSED") || trl.getTestResultStatusMaster().getTestResultStatus().equals("FAILED")) {
					completedJobs++;
				}
			}
			log.debug("TestRunLists count : " +  completedJobs + " / " + totalJobs);
			return (completedJobs == totalJobs);
		} catch (RuntimeException re) {
			log.error("Not able to verify of TestRun is complete", re);
		}		
		return false;
	}
	
	
	@Override
	@Transactional
	public List<TestRunList> listAll() {
		log.debug("listing all TestRunList instance");
		List<TestRunList> testRunList=null;
		try {
			testRunList=sessionFactory.getCurrentSession().createQuery("from TestRunList").list();
			if (!(testRunList == null || testRunList.isEmpty())){
				for(TestRunList trl : testRunList){
					Hibernate.initialize(trl.getTestResultStatusMaster());
					Hibernate.initialize(trl.getDeviceList().getDevicePlatformVersionListMaster().getDeviceLists());
					Hibernate.initialize(trl.getDeviceList().getDevicePlatformVersionListMaster().getDevicePlatformMaster());
					Hibernate.initialize(trl.getTestRunConfigurationChild().getProductVersionListMaster().getProductMaster());
					Hibernate.initialize(trl.getTestRunConfigurationChild().getTestEnviromentMaster().getDevicePlatformMaster());
				}
			}
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return testRunList;
	}
	
	@Override
	@Transactional
    public List<TestRunList> listAll(int startIndex, int pageSize) {
		
		log.debug("listing TestRunList instance");
		List<TestRunList> testRunList=null;
		try {
			testRunList=sessionFactory.getCurrentSession().createQuery("from TestRunList TRL order by TRL.testRunListId desc")/* where t.testRunListId=:testRunId").setParameter("testRunId", 260)
*/	                .setFirstResult(startIndex)
	                .setMaxResults(pageSize)
	                .list();
			if (!(testRunList == null || testRunList.isEmpty())){
				for(TestRunList trl : testRunList){
					Hibernate.initialize(trl.getTestResultStatusMaster());
					Hibernate.initialize(trl.getDeviceList().getDevicePlatformVersionListMaster().getDeviceLists());
					Hibernate.initialize(trl.getDeviceList().getDevicePlatformVersionListMaster().getDevicePlatformMaster());

					Hibernate.initialize(trl.getTestRunConfigurationChild().getProductVersionListMaster().getProductMaster());
					Hibernate.initialize(trl.getTestRunConfigurationChild().getTestEnviromentMaster().getDevicePlatformMaster());
					Hibernate.initialize(trl.getTestRunConfigurationChild().getProductVersionListMaster().getProductMaster());
					Hibernate.initialize(trl.getTestEnvironmentDevices());
				}
			}
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return testRunList;       
    }
	
	@Override
	@Transactional
	public TestRunList getByTestRunListId(int testRunListId){
		log.debug("getting TestRunList instance by id");
		TestRunList testRunList=null;
		try {
			testRunList=(TestRunList) sessionFactory.getCurrentSession().get(TestRunList.class,testRunListId);
			if (testRunList != null) {
				Hibernate.initialize(testRunList.getTestResultStatusMaster());
				Hibernate.initialize(testRunList.getDeviceList().getDevicePlatformVersionListMaster().getDeviceLists());
				Hibernate.initialize(testRunList.getDeviceList().getDevicePlatformVersionListMaster().getDevicePlatformMaster());
				Hibernate.initialize(testRunList.getTestRunConfigurationChild().getProductVersionListMaster().getProductMaster());
				Hibernate.initialize(testRunList.getTestRunConfigurationChild().getTestEnviromentMaster().getDevicePlatformMaster());

				
				Hibernate.initialize(testRunList.getTestRunConfigurationChild().getTestSuiteList().getTestCaseLists());

				Hibernate.initialize(testRunList.getTestRunConfigurationChild().getTestSuiteList().getProductMaster());
				Hibernate.initialize(testRunList.getTestExecutionResults());
				Hibernate.initialize(testRunList.getTestRunEvidenceStatus());

			}
			log.debug("getByTesRunId successful");
		} catch (RuntimeException re) {
			log.error("getByTestRunId failed", re);
		}
		return testRunList;
        
	}

	@Override
	@Transactional
	public int getTotalRecords() {
		log.debug("getting TestRunList total records");
		int count =0;
		try {
			count=((Number) sessionFactory.getCurrentSession().createSQLQuery("select count(*) from test_run_list").uniqueResult()).intValue();
			
			log.debug("total records fetch successful");
		} catch (RuntimeException re) {
			log.error("total records fetch failed", re);			
		}
		return count;
	
	}

	@Override
	@Transactional
	public List<TestRunList> list(int testRunConfigurationChildId) {
		log.debug("listing specific ProductVersionListMaster instance");
		List<TestRunList> testRunList=null;
		try {
			testRunList=sessionFactory.getCurrentSession().createQuery("from TestRun where testRunConfigurationChildId=:testRunConfigurationChildId")
														.setParameter("testRunConfigurationChildId", testRunConfigurationChildId).list();
			if (!(testRunList == null || testRunList.isEmpty())){
				for(TestRunList trl : testRunList){
					Hibernate.initialize(trl.getTestResultStatusMaster());
					Hibernate.initialize(trl.getDeviceList().getDevicePlatformVersionListMaster().getDeviceLists());
					Hibernate.initialize(trl.getDeviceList().getDevicePlatformVersionListMaster().getDevicePlatformMaster());
					Hibernate.initialize(trl.getTestRunConfigurationChild().getProductVersionListMaster());
					Hibernate.initialize(trl.getTestRunConfigurationChild().getTestEnviromentMaster().getDevicePlatformMaster());
				}
			}
			log.debug("list specific successful");
		} catch (RuntimeException re) {
			log.error("list specific failed", re);
			//throw re;
		}
		return testRunList;
	}
	
	@Override
	@Transactional
	public Integer getAverageTestRunExecutionTime(TestRunList testRunList) {
		
		log.debug("Getting average testrun execution time");
		try {
			
			//Getting the average execution time from the past using a direct SQL query
			String sqlQuery = "SELECT AVG(TIMESTAMPDIFF(SECOND, testRunStartTime, testRunEndTime)) as averageTestRunExecutionTime "
					+ " FROM test_run_list"
					+ " where testRunConfigurationChildId = " + testRunList.getTestRunConfigurationChild().getTestRunConfigurationChildId()
					+ " and deviceListId = " + testRunList.getDeviceList().getDeviceListId() 
					+ " group by testRunConfigurationChildId, deviceListId";
			
			BigDecimal averageTestRunExecutionTime = (BigDecimal) sessionFactory.getCurrentSession().createSQLQuery(sqlQuery)
														.uniqueResult();
			log.debug("Getting average testrun execution time successful");
			if (averageTestRunExecutionTime != null)
				return averageTestRunExecutionTime.intValue();
			else
				return null;
		} catch (RuntimeException re) {
			log.error("Getting average testrun execution time failed", re);
			return null;
		}
	}

	@Override
	@Transactional
	public List<TestRunList> list(int testRunConfigurationChildId, int startIndex, int pageSize) {
		log.debug("listing specific ProductVersionListMaster instance");
		List<TestRunList> testRunList=null;
		try {
			testRunList=sessionFactory.getCurrentSession().createQuery("from TestRun where testRunConfigurationChildId=:testRunConfigurationChildId")
														.setParameter("testRunConfigurationChildId", testRunConfigurationChildId).setFirstResult(startIndex)
														.setMaxResults(pageSize).list();
			if (!(testRunList == null || testRunList.isEmpty())){
				for(TestRunList trl : testRunList){
					Hibernate.initialize(trl.getTestResultStatusMaster());
					Hibernate.initialize(trl.getDeviceList().getDevicePlatformVersionListMaster().getDeviceLists());
					Hibernate.initialize(trl.getDeviceList().getDevicePlatformVersionListMaster().getDevicePlatformMaster());
					Hibernate.initialize(trl.getTestRunConfigurationChild().getProductVersionListMaster());
					Hibernate.initialize(trl.getTestRunConfigurationChild().getTestEnviromentMaster().getDevicePlatformMaster());
				}
			}
			log.debug("list specific successful");
		} catch (RuntimeException re) {
			log.error("list specific failed", re);
			//throw re;
		}
		return testRunList;
	}

	
	@Override
	@Transactional
	public List<TestRunList> listByHostId(int hostId,String testStatus) {
		log.debug("listByHostId");
		List<TestRunList> testRunList=null;
		try {
			testRunList=sessionFactory.getCurrentSession().createQuery("from TestRunList where deviceList.hostList.hostId=:hostId and testResultStatusMaster.testResultStatus=:testStatus")
														.setParameter("hostId", hostId)
														.setParameter("testStatus", testStatus)
														.list();
			if (!(testRunList == null || testRunList.isEmpty())){
				for(TestRunList trl : testRunList){
					Hibernate.initialize(trl.getTestResultStatusMaster());
					Hibernate.initialize(trl.getTestEnvironmentDevices());
					Hibernate.initialize(trl.getDeviceList().getDevicePlatformVersionListMaster().getDeviceLists());
					Hibernate.initialize(trl.getDeviceList().getDevicePlatformVersionListMaster().getDevicePlatformMaster());
					Hibernate.initialize(trl.getTestRunConfigurationChild().getProductVersionListMaster().getProductMaster());
					Hibernate.initialize(trl.getTestRunConfigurationChild().getTestEnviromentMaster().getDevicePlatformMaster());
					Hibernate.initialize(trl.getTestRunConfigurationChild().getProductVersionListMaster().getProductMaster());

				}
			}
			log.debug("listByHostId successful");
		} catch (RuntimeException re) {
			log.error("listByHostId failed", re);
			////throw re;
		}
		return testRunList;
	}
	@Override
	@Transactional
	public int getTotalRecords(int testRunConfigurationChildId) {
		log.debug("getting TestRunList records for TestRunConfigurationChildId");
		int count =0;
		try {
			count=((Number) sessionFactory.getCurrentSession().createSQLQuery("select count(*) from test_run_list where testRunConfigurationChildId=:testRunConfigurationChildId").setParameter("testRunConfigurationChildId", testRunConfigurationChildId).uniqueResult()).intValue();
			
			log.debug("total records fetch successful");
		} catch (RuntimeException re) {
			log.error("total records fetch failed", re);			
			//throw re;
		}
		return count;
	}

	@Override
	@Transactional
	public void failTestRun(int hostId) {
		log.debug("failTestRun");		
		try {
			sessionFactory.getCurrentSession().createQuery("update TestRunList set testResultStatusMaster.testResultStatus=:failed where deviceList.hostList.hostId=:hostId and (testResultStatusMaster.testResultStatus=:executing or testResultStatusMaster.testResultStatus=:queued)")
					.setParameter("hostId", hostId)
					.setParameter("queued", TestExecutionStatus.EXECUTING.toString())
					.setParameter("executing", TestExecutionStatus.QUEUED.toString())
					.setParameter("failed", TestExecutionStatus.FAILED.toString()).executeUpdate();
			
			
			log.debug("failTestRun successful");
		} catch (RuntimeException re) {
			log.error("failTestRun failed", re);
			////throw re;
		}
		
		
	}

	@Override
	@Transactional
	public List<TestRunList> listExecutedJobList(int hours,int startIndex, int pageSize){
		log.debug("listing specific ProductVersionListMaster instance");
		List<TestRunList> testRunList=null;
		try {
			String lhours=hours+":00:00";
			testRunList=sessionFactory.getCurrentSession().createQuery("from TestRunList  where testRunStatus in ('PASSED','FAILED') and testRunTriggeredTime>=SUBTIME(CURRENT_TIMESTAMP(), :lhours) order by testRunTriggeredTime desc")
					.setParameter("lhours", lhours)
					 .setFirstResult(startIndex)
	                .setMaxResults(pageSize)
	                .list();
			if (!(testRunList == null || testRunList.isEmpty())){
				for(TestRunList trl : testRunList){
					Hibernate.initialize(trl.getTestResultStatusMaster());
					Hibernate.initialize(trl.getTestEnvironmentDevices());
					Hibernate.initialize(trl.getDeviceList().getDevicePlatformVersionListMaster().getDeviceLists());
					Hibernate.initialize(trl.getDeviceList().getDevicePlatformVersionListMaster().getDevicePlatformMaster());
					Hibernate.initialize(trl.getTestRunConfigurationChild().getProductVersionListMaster());
					Hibernate.initialize(trl.getTestRunConfigurationChild().getProductVersionListMaster().getProductMaster());
					Hibernate.initialize(trl.getTestRunConfigurationChild().getTestEnviromentMaster().getDevicePlatformMaster());
				}
			}
			
			log.debug("list specific successful");
		} catch (RuntimeException re) {
			log.error("list specific failed", re);
			throw re;
		}
		return testRunList;
	}
	
	@Override
	@Transactional
	public int getTotalTestRunListInLast24Hours(int hours) {
		log.debug("getting TestRunList total records Executed in last 24 Hours:");
		int count =0;
		try {
			String lhours=hours+":00:00";
			count=((Number) sessionFactory.getCurrentSession().createSQLQuery("select count(*) from test_run_list where testRunStatus in ('PASSED','FAILED') and testRunTriggeredTime>=SUBTIME(CURRENT_TIMESTAMP(), :lhours)")
					.setParameter("lhours", lhours)
					.uniqueResult()).intValue();
		    }
		 catch (RuntimeException re) {
				log.error("total records fetch failed", re);			
	        }
		return count;
	}

	
	@Override
	@Transactional
	public List<TestRunList> listExecutingJobList(){
		log.debug("listing specific lExecutingJobList instance");
		
		List<TestRunList> testRunList=null;
		try {
			testRunList=sessionFactory.getCurrentSession().createQuery("from TestRunList where testRunStatus in ('EXECUTING','QUEUED') ").list();
			if (!(testRunList == null || testRunList.isEmpty())){
				for(TestRunList trl : testRunList){
					Hibernate.initialize(trl.getTestResultStatusMaster());
					Hibernate.initialize(trl.getDeviceList().getDevicePlatformVersionListMaster().getDeviceLists());
					Hibernate.initialize(trl.getDeviceList().getDevicePlatformVersionListMaster().getDevicePlatformMaster());
					Hibernate.initialize(trl.getTestRunConfigurationChild().getProductVersionListMaster());
					Hibernate.initialize(trl.getTestRunConfigurationChild().getProductVersionListMaster().getProductMaster());
					Hibernate.initialize(trl.getTestRunConfigurationChild().getTestEnviromentMaster().getDevicePlatformMaster());
					Hibernate.initialize(trl.getTestRunConfigurationChild().getTestSuiteList());
					if(trl.getTestRunConfigurationChild().getTestSuiteList() != null ){
						Hibernate.initialize(trl.getTestRunConfigurationChild().getTestSuiteList().getTestCaseLists());
					}
					if(trl.getTestRunConfigurationChild().getTestSuiteList().getTestCaseLists() != null 
							&& !trl.getTestRunConfigurationChild().getTestSuiteList().getTestCaseLists().isEmpty()){
						for(TestCaseList testCaseList : trl.getTestRunConfigurationChild().getTestSuiteList().getTestCaseLists()){
							Hibernate.initialize(testCaseList.getTestCaseStepsLists());
						}	
					}
					
				}
			}
			log.debug("list specific successful");
		} catch (RuntimeException re) {
			log.error("list specific failed", re);
			throw re;
		}
		return testRunList;
	}
	
	@Override
	@Transactional
	public void clearTestRuns() {
		log.debug("clearTestRuns");		
		try {
			sessionFactory.getCurrentSession().createQuery("update TestRunList set testResultStatusMaster.testResultStatus=:failed where testResultStatusMaster.testResultStatus=:executing or testResultStatusMaster.testResultStatus=:queued")					
					.setParameter("queued", TestExecutionStatus.EXECUTING.toString())
					.setParameter("executing", TestExecutionStatus.QUEUED.toString())
					.setParameter("failed", TestExecutionStatus.FAILED.toString()).executeUpdate();
			
			
			log.debug("clearTestRuns successful");
		} catch (RuntimeException re) {
			log.error("clearTestRuns failed", re);
		}
	
	}
}