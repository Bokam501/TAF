package com.hcl.atf.taf.dao.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.TestExecutionResultBugDAO;
import com.hcl.atf.taf.model.DefectExportData;
import com.hcl.atf.taf.model.TestExecutionResult;
import com.hcl.atf.taf.model.TestExecutionResultBugList;
import com.hcl.atf.taf.model.TestStepExecutionResult;
import com.hcl.jira.rest.JiraConnector;

@Repository
public class TestExecutionResultBugDAOImpl implements TestExecutionResultBugDAO {
	private static final Log log = LogFactory
			.getLog(TestExecutionResultBugDAOImpl.class);
	
	@Autowired(required = true)
	private SessionFactory sessionFactory;

	@Override
	@Transactional
	public void delete(TestExecutionResultBugList bug) {
		log.debug("deleting bug instance");
		try {
			sessionFactory.getCurrentSession().delete(bug);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
		}

	}

	@Override
	@Transactional
	public void add(TestExecutionResultBugList bug) {
		log.debug("adding bug instance");
		try {
			if (bug.getBugDescription().length() >= 5000) {
				bug.setBugDescription(bug.getBugDescription()
						.substring(0, 4998));
			}

			if (bug.getRemarks().length() >= 5000) {
				bug.setRemarks(bug.getRemarks().substring(0, 4998));
			}
			sessionFactory.getCurrentSession().save(bug);
			log.debug("add successful");
		} catch (RuntimeException re) {
			log.error("Unable to create bug for failed test step", re);
		}

	}

	@Override
	@Transactional
	public void update(TestExecutionResultBugList bug) {
		log.debug("updating bug instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(bug);
			log.debug("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
		}

	}

	@Override
	@Transactional
	public List<TestExecutionResultBugList> listAll() {
		log.debug("listing all bug instance");
		List<TestExecutionResultBugList> bugs = null;
		JiraConnector jiraInstance = null;
		String bugStatus = "";
		try {
			bugs = sessionFactory.getCurrentSession()
					.createQuery("from TestExecutionResultBugList").list();
			if (bugs != null && !bugs.isEmpty()) {
				for (TestExecutionResultBugList bug : bugs) {
					if (bug.getBugManagementSystemBugId() != null
							&& bug.getBugManagementSystemBugId().trim() != "") {
						try {
							InputStream fis = new FileInputStream("/properties/jira.properties");
							jiraInstance = new JiraConnector(fis);
							bugStatus = jiraInstance
									.getIssue(bug.getBugManagementSystemBugId())
									.getStatus().getName();
							bug.setRemarks(bug.getRemarks()
									+ "Jira Bug Status:bugStatus");
						} catch (FileNotFoundException e) {
							log.error("ERROR  ",e);
						}
					}
				}
			}
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return bugs;
	}

	@Override
	@Transactional
	public List<TestExecutionResultBugList> listAllPaginate(int startIndex,
			int pageSize) {
		log.debug("listing bug instance");
		List<TestExecutionResultBugList> bugs = null;
		try {
			bugs = sessionFactory.getCurrentSession()
					.createQuery("from TestExecutionResultBugList")
					.setFirstResult(startIndex).setMaxResults(pageSize).list(); 
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return bugs;
	}

	@Override
	@Transactional
	public List<TestExecutionResultBugList> listAllPaginate(int startIndex,int pageSize, Date startDate,Date endDate) {
		log.debug("listing bug instance");
		List<TestExecutionResultBugList> bugs = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestExecutionResultBugList.class, "bugList");
		if (startDate != null) {
			c.add(Restrictions.ge("bugList.bugCreationTime", startDate));
		}
		if (endDate != null) {
			c.add(Restrictions.le("bugList.bugCreationTime", endDate));
		}
		c.addOrder(Order.asc("testExecutionResultBugId"));
        c.setFirstResult(startIndex);
        c.setMaxResults(pageSize);
        bugs = c.list();	
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return bugs;
	}

	@Override
	@Transactional
	public TestExecutionResultBugList getByBugId(int bugId) {
		log.debug("getting bug instance by id");
		TestExecutionResultBugList bug = null;
		try {
			List list = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from TestExecutionResultBugList t where testExecutionResultBugId=:bugId")
					.setParameter("bugId", bugId).list();
			bug = (list != null && list.size() != 0) ? (TestExecutionResultBugList) list
					.get(0) : null;
			if (!(bug == null)) {
				Hibernate.initialize(bug.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getWorkPackage());
				Hibernate.initialize(bug.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getDefectManagementSystems());
				Hibernate.initialize(bug.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration());
				Hibernate.initialize(bug.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration());
				Hibernate.initialize(bug.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration().getGenericDevice());
				
				if(bug.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration().getGenericDevice()!=null){
					Hibernate.initialize(bug.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration().getGenericDevice().getPlatformType());
				}
				

				Hibernate.initialize(bug.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getTestCase());
				Hibernate.initialize(bug.getTestCaseExecutionResult().getTestStepExecutionResultSet());
				Hibernate.initialize(bug.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getTestCase().getTestCasePriority());
				Hibernate.initialize(bug.getTestCaseExecutionResult().getTestStepExecutionResultSet());
				Set<TestStepExecutionResult> testStepExecutionResults =bug.getTestCaseExecutionResult().getTestStepExecutionResultSet();
				for (TestStepExecutionResult testStepExecutionResult : testStepExecutionResults) {
					Hibernate.initialize(testStepExecutionResult.getTestSteps());
				}
				Hibernate.initialize(bug.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getTestRunJob());
				if (bug.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration().getGenericDevice() != null){
					if (bug.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration().getGenericDevice().getDeviceModelMaster() != null){
					Hibernate.initialize(bug.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration().getGenericDevice().getDeviceModelMaster());
					Hibernate.initialize(bug.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration().getGenericDevice().getDeviceModelMaster().getDeviceMakeMaster());
					}
				}

			}
			log.debug("getByBugId successful");
		} catch (RuntimeException re) {
			log.error("getByBugId failed", re);
		}
		return bug;

	}

	@Override
	@Transactional
	public int getTotalRecords() {
		log.debug("getting bug total records");
		int count = 0;
		try {
			count = ((Number) sessionFactory
					.getCurrentSession()
					.createSQLQuery(
							"select count(*) from test_execution_result_bug_list")
					.uniqueResult()).intValue();

			log.debug("total records fetch successful");
		} catch (RuntimeException re) {
			log.error("total records fetch failed", re);
		}
		return count;

	}

	@Override
	@Transactional
	public List<TestExecutionResultBugList> listByTestRun(int runNo,
			int testRunConfigurationChildId) {
		log.debug("listing bug from a test run");
		List<TestExecutionResultBugList> bugs = null;
		try {
			String sql="from TestExecutionResultBugList e where e.testCaseExecutionResult.workPackageTestCaseExecutionPlan.workPackage.workPackageId=:runNo ";
			if(testRunConfigurationChildId >0) {
				bugs = sessionFactory
					.getCurrentSession()
					.createQuery(
							sql+"AND e.testExecutionResult.testRunList.testRunConfigurationChild.testRunConfigurationChildId=:testRunConfigurationChildId")
					.setParameter("runNo", runNo)
					.setParameter("testRunConfigurationChildId",
							testRunConfigurationChildId).list();
			} else {
				bugs = sessionFactory
				.getCurrentSession()
				.createQuery(sql).setParameter("runNo", runNo).list();
			}
			
			if (!(bugs == null || bugs.isEmpty())) {
				for (TestExecutionResultBugList b : bugs) {
				}
			}
			log.debug("list specific successful");
		} catch (RuntimeException re) {
			log.error("list specific failed", re);
		}
		return bugs;
	}

	@Override
	@Transactional
	public List<TestExecutionResultBugList> listTestExecutionResultBugsPaginate(
			int runNo, int testRunConfigurationChildId, int startIndex,
			int pageSize) {
		log.debug("listing specific TestExecutionResult instance");
		List<TestExecutionResultBugList> bugs = null;
		try {
			bugs = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from TestExecutionResultBugList e where e.testExecutionResult.testRunList.runNo=:runNo AND e.testExecutionResult.testRunList.testRunConfigurationChild.testRunConfigurationChildId=:testRunConfigurationChildId")
					.setParameter("runNo", runNo)
					.setParameter("testRunConfigurationChildId",
							testRunConfigurationChildId)
					.setMaxResults(pageSize).setFirstResult(startIndex).list();
			log.debug("list specific successful");
		} catch (RuntimeException re) {
			log.error("list specific failed", re);
			// throw re;
		}
		return bugs;
	}

	@Override
	@Transactional
	public int getTotalBugsForTestRun(int runNo, int testRunConfigurationChildId) {
		log.debug("getting Bug records for TestRun");
		int count = 0;
		try {
			count = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from TestExecutionResultBugList e where e.testExecutionResult.testRunList.runNo=:runNo AND e.testExecutionResult.testRunList.testRunConfigurationChild.testRunConfigurationChildId=:testRunConfigurationChildId")
					.setParameter("runNo", runNo)
					.setParameter("testRunConfigurationChildId",
							testRunConfigurationChildId).list().size();

			log.debug("total records fetch successful");
		} catch (RuntimeException re) {
			log.error("total records fetch failed", re);
			// throw re;
		}
		return count;
	}

	@Override
	@Transactional
	public int getTotalRecords(int runNo, int testRunConfigurationChildId,
			int deviceListId) {
		log.debug("getting bugs records for testRunListId AND testSuiteId");
		int count = 0;
		try {
			count = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from TestExecutionResultBugList e where e.testExecutionResult.testRunList.runNo=:runNo AND e.testExecutionResult.testRunList.testRunConfigurationChild.testRunConfigurationChildId=:testRunConfigurationChildId AND e.testExecutionResult.testRunList.deviceList.deviceListId=:deviceListId")
					.setParameter("runNo", runNo)
					.setParameter("testRunConfigurationChildId",
							testRunConfigurationChildId)
					.setParameter("deviceListId", deviceListId).list().size();

			log.debug("total records fetch successful");
		} catch (RuntimeException re) {
			log.error("total records fetch failed", re);
			// throw re;
		}
		return count;
	}

	@Override
	@Transactional
	public List<TestExecutionResultBugList> list(int runNo,
			int testRunConfigurationChildId, int deviceListId) {
		log.debug("listing specific bug");
		List<TestExecutionResultBugList> bugs = null;
		try {
			bugs = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from TestExecutionResultBugList e where e.testExecutionResult.testRunList.runNo=:runNo AND e.testExecutionResult.testRunList.testRunConfigurationChild.testRunConfigurationChildId=:testRunConfigurationChildId AND e.testExecutionResult.testRunList.deviceList.deviceListId=:deviceListId")
					.setParameter("runNo", runNo)
					.setParameter("testRunConfigurationChildId",
							testRunConfigurationChildId)
					.setParameter("deviceListId", deviceListId).list();
			if (!(bugs == null || bugs.isEmpty())) {
				for (TestExecutionResultBugList b : bugs) {
				}
			}
			log.debug("list specific successful");
		} catch (RuntimeException re) {
			log.error("list specific failed", re);
		}
		return bugs;
	}

	@Override
	@Transactional
	public List<TestExecutionResultBugList> listTestExecutionResultBugsPaginate(
			int runNo, int testRunConfigurationChildId, int deviceListId,
			int startIndex, int pageSize) {
		log.debug("listing specific bugs instance");
		List<TestExecutionResultBugList> bugs = null;
		try {
			bugs = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from TestExecutionResultBugList e where e.testExecutionResult.testRunList.runNo=:runNo AND e.testExecutionResult.testRunList.testRunConfigurationChild.testRunConfigurationChildId=:testRunConfigurationChildId AND e.testExecutionResult.testRunList.deviceList.deviceListId=:deviceListId")
					.setParameter("runNo", runNo)
					.setParameter("testRunConfigurationChildId",
							testRunConfigurationChildId)
					.setParameter("deviceListId", deviceListId)
					.setFirstResult(startIndex).setMaxResults(pageSize).list();
			log.debug("list specific successful");
		} catch (RuntimeException re) {
			log.error("list specific failed", re);
			// throw re;
		}
		return bugs;
	}

	@Override
	@Transactional
	public List<TestExecutionResultBugList> listFilteredTestExecutionResult(
			int startIndex, int pageSize, int productId, String platformName,
			int runNo, Date timeFrom, Date timeTo) {
		log.debug("listing filtered bugs instance");
		List<TestExecutionResultBugList> bugs = null;
		try {
			StringBuffer qry = new StringBuffer(
					"from TestExecutionResultBugList");
			boolean isANDrequired = false;
			if (productId != -1) {
				qry.append(" e where");
				qry.append(" e.testExecutionResult.testRunList.testRunConfigurationChild.testRunConfigurationParent.productMaster.productId=:productId");
				isANDrequired = true;
			}
			if (platformName != null) {
				if (isANDrequired)
					qry.append(" AND ");
				else
					qry.append(" e where");
				qry.append(" e.testExecutionResult.testRunList.testRunConfigurationChild.productVersionListMaster.devicePlatformVersionListMaster.devicePlatformMaster.devicePlatformName=:platformName");
				isANDrequired = true;
			}
			if (runNo != -1) {
				if (isANDrequired)
					qry.append(" AND ");
				else
					qry.append(" e where");
				qry.append(" e.testExecutionResult.testRunList.runNo=:runNo");
				isANDrequired = true;
			}
			if (timeFrom != null) {
				if (isANDrequired)
					qry.append(" AND ");
				else
					qry.append(" e where");
				qry.append(" e.testExecutionResult.testRunList.testRunTriggeredTime>=:timeFrom");
				isANDrequired = true;
			}
			if (timeTo != null) {
				if (isANDrequired)
					qry.append(" AND ");
				else
					qry.append(" e where");
				qry.append(" e.testExecutionResult.testRunList.testRunTriggeredTime<=:timeTo");
				isANDrequired = true;
			}

			Query query = sessionFactory.getCurrentSession().createQuery(
					qry.toString());

			if (productId != -1) {
				query.setParameter("productId", productId);
			}
			if (platformName != null) {
				query.setParameter("platformName", platformName);
			}
			if (runNo != -1) {
				query.setParameter("runNo", runNo);
			}
			if (timeFrom != null) {
				query.setDate("timeFrom", timeFrom);
			}
			if (timeTo != null) {
				query.setDate("timeTo", timeTo);
			}

			bugs = query.setFirstResult(startIndex).setMaxResults(pageSize)
					.list();

			log.debug("list filtered successful");
		} catch (RuntimeException re) {
			log.error("list filtered failed", re);
			// throw re;
		}
		return bugs;
	}

	@Override
	@Transactional
	public int getTotalRecordsFiltered(int productId, String platformName,
			int runNo, Date timeFrom, Date timeTo) {
		log.debug("getting bugs Filtered total records");
		int count = 0;
		try {
			StringBuffer qry = new StringBuffer(
					"select count(*) from TestExecutionResultBugList");
			boolean isANDrequired = false;
			if (productId != -1) {
				qry.append(" e where");
				qry.append(" e.testExecutionResult.testRunList.testRunConfigurationChild.testRunConfigurationParent.productMaster.productId=:productId");
				isANDrequired = true;
			}
			if (platformName != null) {
				if (isANDrequired)
					qry.append(" AND ");
				else
					qry.append(" e where");
				qry.append(" e.testExecutionResult.testRunList.testRunConfigurationChild.productVersionListMaster.devicePlatformVersionListMaster.devicePlatformMaster.devicePlatformName=:platformName");
				isANDrequired = true;
			}
			if (runNo != -1) {
				if (isANDrequired)
					qry.append(" AND ");
				else
					qry.append(" e where");
				qry.append(" e.testExecutionResult.testRunList.runNo=:runNo");
				isANDrequired = true;
			}
			if (timeFrom != null) {
				if (isANDrequired)
					qry.append(" AND ");
				else
					qry.append(" e where");
				qry.append(" e.testExecutionResult.testRunList.testRunTriggeredTime>=:timeFrom");
				isANDrequired = true;
			}
			if (timeTo != null) {
				if (isANDrequired)
					qry.append(" AND ");
				else
					qry.append(" e where");
				qry.append(" e.testExecutionResult.testRunList.testRunTriggeredTime<=:timeTo");
				isANDrequired = true;
			}

			Query query = sessionFactory.getCurrentSession().createQuery(
					qry.toString());

			if (productId != -1) {
				query.setParameter("productId", productId);
			}
			if (platformName != null) {
				query.setParameter("platformName", platformName);
			}
			if (runNo != -1) {
				query.setParameter("runNo", runNo);
			}
			if (timeFrom != null) {
				query.setDate("timeFrom", timeFrom);
			}
			if (timeTo != null) {
				query.setDate("timeTo", timeTo);
			}
			count = ((Number) query.uniqueResult()).intValue();

			log.debug("total Filtered records fetch successful");
		} catch (RuntimeException re) {
			log.error("total Filtered records fetch failed", re);
			// throw re;
		}
		return count;

	}

	// Changes for updating defect traceability information in defect export
	// data table
	@Override
	@Transactional
	public void addDefectExportData(DefectExportData defectsExportData) {
		log.debug("Adding  defectsExport Data Results information from the defect management system");

		try {
			sessionFactory.getCurrentSession().save(defectsExportData);
		} catch (Exception e) {
			log.error("Error in adding the defects export data ", e);
		}

	}

	@Override
	@Transactional
	public List<TestExecutionResult> listExceutionResults(int runListId,
			int testCaseListId) {
		log.debug("listing specific bugs instance");
		List<TestExecutionResult> testExecutionResults = null;
		try {
			testExecutionResults = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from TestExecutionResult e where e.testRunList.testRunListId=:runListId AND e.testCaseList.testCaseId=:testCaseListId")
					.setParameter("runListId", runListId)
					.setParameter("testCaseListId", testCaseListId).list();
			log.debug("list specific successful");
		} catch (RuntimeException re) {
			log.error("list specific failed", re);
			// throw re;
		}
		return testExecutionResults;
	}
	
	@Override
	@Transactional
	public TestExecutionResultBugList getByBugWithCompleteInitialization(int bugId) {
		log.debug("getting bug instance by id");
		TestExecutionResultBugList defect = null;
		try {
			List list = sessionFactory
					.getCurrentSession()
					.createQuery("from TestExecutionResultBugList t where testExecutionResultBugId=:bugId")
					.setParameter("bugId", bugId)
					.list();
			defect = (list != null && list.size() != 0) ? (TestExecutionResultBugList) list.get(0) : null;
			if (!(defect == null)) {
				Hibernate.initialize(defect.getTestCaseExecutionResult());
				Hibernate.initialize(defect.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getTestCase());
				Hibernate.initialize(defect.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getTester());
				Hibernate.initialize(defect.getDefectType());
				Hibernate.initialize(defect.getDefectFoundStage());
				if(defect.getAssignee() != null){
					Hibernate.initialize(defect.getAssignee());
				}
				if(defect.getApprovedBy() != null){
					Hibernate.initialize(defect.getApprovedBy());
				}
				
				if(defect.getApproversDefectSeverity() != null){
					Hibernate.initialize(defect.getApproversDefectSeverity());
				}
				
				if(defect.getDefectApprovalStatus() != null){
					Hibernate.initialize(defect.getDefectApprovalStatus());
				}
				
				if(defect.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan() != null){
					if(defect.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getWorkPackage() != null){
						Hibernate.initialize(defect.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getWorkPackage());
						if(defect.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getWorkPackage().getProductBuild() != null){
							Hibernate.initialize(defect.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getWorkPackage().getProductBuild());
							if(defect.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getWorkPackage().getProductBuild().getProductVersion() != null){
								Hibernate.initialize(defect.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getWorkPackage().getProductBuild().getProductVersion());
								if(defect.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getWorkPackage().getProductBuild().getProductVersion().getProductMaster() != null){
									Hibernate.initialize(defect.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getWorkPackage().getProductBuild().getProductVersion().getProductMaster());
									if(defect.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getDefectManagementSystems() != null){
										Hibernate.initialize(defect.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getDefectManagementSystems());
									}
								}
							}
						}
					}	
				}
				
				Hibernate.initialize(defect.getBugFilingStatus());
				Hibernate.initialize(defect.getDefectSeverity());
				
				if(defect.getTestersPriority() != null){
					Hibernate.initialize(defect.getTestersPriority());
				}
				
				if(defect.getApproversPriority() != null){
					Hibernate.initialize(defect.getApproversPriority());
				}
				
				if(defect.getIsReproducableOnLive()!= null){
					Hibernate.initialize(defect.getIsReproducableOnLive());
				}
				
				if(defect.getIsThereABugAlready()!= null){
					Hibernate.initialize(defect.getIsThereABugAlready());
				}
			}
			log.debug("getByBugId successful");
		} catch (RuntimeException re) {
			log.error("getByBugId failed", re);
		}
		return defect;

	}

	@Override
	public Integer getDefectsCount(Integer productId) {
		Integer defectCount=0;
		try {
			String sql="SELECT COUNT(*),productId FROM test_execution_result_bug_list defect, "+
						" teststep_execution_result testStep, "+
						" test_case_list testcase "+ 
						" WHERE testStep.testCaseId= testcase.testcaseId "+ 
						" AND defect.testStepExecutionResultId=testStep.testStepExecutionResultId "+
						" AND testCase.productId="+productId +"group by testCase.productId";
			List<Object[]> defectList = sessionFactory.getCurrentSession().createSQLQuery(sql).list();
			if(defectList != null && defectList.size() > 0){
				for(Object[] defect:defectList){
					defectCount=Integer.parseInt(defect[0].toString());
				}
			}
		}catch(RuntimeException re) {
			log.error("Error in getDefectsCount",re);
		}
		return defectCount;
	}
}
