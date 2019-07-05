package com.hcl.atf.taf.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.DefectUploadDAO;
import com.hcl.atf.taf.model.DefectTypeMaster;
import com.hcl.atf.taf.model.ExecutionPriority;
import com.hcl.atf.taf.model.TestExecutionResultBugList;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.WorkFlow;
import com.hcl.atf.taf.model.WorkPackageTestCaseExecutionPlan;
import com.hcl.atf.taf.model.dto.TestCaseDTO;

@Repository
public class DefectUploadDAOImpl implements DefectUploadDAO {
	
	private static final Log log = LogFactory.getLog(DefectUploadDAOImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	@Transactional
	public void updateDefectUPloadFlag(TestExecutionResultBugList defect) {
		log.debug("updating TestExecution Result BugList instance");
		try {
			sessionFactory.getCurrentSession().update(defect);
			log.debug("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
			//throw re;
		}	
		
	}
	

	@Override
	@Transactional
	public int addBulk(List<TestExecutionResultBugList> listTestExecutionResultBug, int batchSize, String action) {
		log.info("Adding Defects in bulk");
		int count = 0;
		try {
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			if (batchSize <= 0)
				batchSize = 50;
			for (TestExecutionResultBugList testBugs : listTestExecutionResultBug ) {
				if (action.equalsIgnoreCase("New")){
					session.save(testBugs);
				}else{
					session.update(testBugs);
				}
				
				log.info("Defects data saved successfully");
				if (count++ % batchSize == 0 ) {
					session.flush();
					session.clear();
			    }
			}
			tx.commit();
			session.close();
			log.info("Bulk Add Successful");
		} catch (RuntimeException re) {
			log.error("Bulk Add failed", re);
		}
		return count;
	}


	@Override
	@Transactional
	public List<String> getExistingIssueId(String issueId) {
		List<String> IssueIdList = new ArrayList<String>();
		String hql = "SELECT TS.bugManagementSystemBugId from TestExecutionResultBugList TS WHERE TS.bugManagementSystemBugId=:defectIssueId";
		IssueIdList = sessionFactory.getCurrentSession().createQuery(hql).
				setParameter("defectIssueId", issueId).
				list();
		return IssueIdList;
	}
	
	@Override
	@Transactional
	public Integer getDefectDataId(String issueId) {
		Integer IssueIdList = null;
		Criteria c = sessionFactory.getCurrentSession().createCriteria(TestExecutionResultBugList.class, "test");
		c.add(Restrictions.eq("test.bugManagementSystemBugId", issueId));
		List<TestExecutionResultBugList> defectsList = c.list();
		TestExecutionResultBugList defect = (defectsList!=null && defectsList.size()!=0)?(TestExecutionResultBugList) defectsList.get(0):null;	
		if(defect != null ){
			IssueIdList =defect.getTestExecutionResultBugId();
		}
		return IssueIdList;
		
	}


	@Override
	@Transactional
	public DefectTypeMaster listDefectType(String defectTypeName) {
		List<DefectTypeMaster> defectTypeMasterList = null;		
		DefectTypeMaster defectTypeMaster = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(DefectTypeMaster.class, "defects");		
			c.add(Restrictions.eq("defects.defectTypeName", defectTypeName).ignoreCase());
			defectTypeMasterList = c.list();
			
			defectTypeMaster = (defectTypeMasterList!=null && defectTypeMasterList.size()!=0)?(DefectTypeMaster)defectTypeMasterList.get(0):null;
		} catch (HibernateException e) {
			log.error("Error listing DefectTypeMaster ", e);
		}		
		return defectTypeMaster;
	}




	@Override
	@Transactional
	public ExecutionPriority listExecutionPriority(String priorityName) {
		List<ExecutionPriority> executionPriorityList = null;		
		ExecutionPriority executionPriority = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ExecutionPriority.class, "exec");		
			c.add(Restrictions.eq("exec.executionPriority", priorityName).ignoreCase());
			executionPriorityList = c.list();
			
			executionPriority = (executionPriorityList!=null && executionPriorityList.size()!=0)?(ExecutionPriority)executionPriorityList.get(0):null;
		} catch (HibernateException e) {
			log.error("Error ExecutionPriority :",e);
		}		
		return executionPriority;
	}

	@Override
	@Transactional
	public WorkFlow listWorkflow(String status) {
		List<WorkFlow> workFlowList = null;		
		WorkFlow workFlow = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkFlow.class, "work");		
			c.add(Restrictions.eq("work.stageName", status).ignoreCase());
			workFlowList = c.list();
			
			workFlow = (workFlowList!=null && workFlowList.size()!=0)?(WorkFlow)workFlowList.get(0):null;
		} catch (HibernateException e) {
			log.error("ERROR listing Workflow ",e);
		}		
		return workFlow;
	}

	@Override
	@Transactional
	public UserList listUserList(String userName) {
		List<UserList> userListforListing = null;		
		UserList userList = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(UserList.class, "user");		
			c.add(Restrictions.eq("user.loginId", userName).ignoreCase());
			userListforListing = c.list();
			
			userList = (userListforListing!=null && userListforListing.size()!=0)?(UserList)userListforListing.get(0):null;
		} catch (HibernateException e) {
			log.error("ERROR listing UserList",e);
		}		
		return userList;
	}


	@Override
	@Transactional
	public List<Object> listDefectsByTestcaseExecutionPlanIdAnalyse(Integer productId, Integer productVersionId,Integer productBuildId, Integer workPackageId,Integer status, Date startDate, Date endDate,
			Integer issueStatus, Integer analyseStatus, Integer jtStartIndex, Integer jtPageSize) {
		
		List<Object> testExecutionResultBugList  = new ArrayList<Object>();
		
		Criteria c = sessionFactory.getCurrentSession().createCriteria(TestExecutionResultBugList.class, "tebl");
		c.createAlias("tebl.testCaseExecutionResult", "tcer");
		c.createAlias("tcer.workPackageTestCaseExecutionPlan", "wptep");
		c.createAlias("wptep.workPackage", "wp");
		c.createAlias("wp.productBuild", "productBuild");
		c.createAlias("productBuild.productVersion", "productVer");
		c.createAlias("productVer.productMaster", "prodMaster");
		
		if (productId != 0){
			c.add(Restrictions.eq("prodMaster.productId", productId));
		}
		if (productVersionId != 0){
			c.add(Restrictions.eq("productVer.productVersionListId", productVersionId));
		}
		if (productBuildId != 0){
			c.add(Restrictions.eq("productBuild.productBuildId", productBuildId));
		}
		if (workPackageId != 0){
			c.add(Restrictions.eq("wp.workPackageId", workPackageId));
		}
		
		if(issueStatus == 1){
			c.add(Restrictions.and(Restrictions.isNotNull("tebl.bugManagementSystemBugId"), Restrictions.ne("tebl.bugManagementSystemBugId", "")));
		}else if (issueStatus == 0){
			c.add(Restrictions.or(Restrictions.isNull("tebl.bugManagementSystemBugId"), Restrictions.eq("tebl.bugManagementSystemBugId", "")));
		}
		
		if(analyseStatus == 1){
			c.add(Restrictions.eq("tebl.analysedFlag", analyseStatus));
		}else if (analyseStatus == 0){
			c.add(Restrictions.or(Restrictions.isNull("tebl.analysedFlag"), Restrictions.eq("tebl.analysedFlag", analyseStatus)));
		}
		
		if(status != -1){
			c.add(Restrictions.eq("tebl.bugFilingStatus.workFlowId", status));
		}
		
		c.add(Restrictions.between("tebl.bugCreationTime", startDate,  endDate));
		
		
		c.setProjection( Projections.projectionList()
		        .add( Projections.distinct(Projections.property("tebl.testExecutionResultBugId")))
			    );
		
		if(jtStartIndex != null && jtPageSize != null)
			c.setFirstResult(jtStartIndex).setMaxResults(jtPageSize);
		
		testExecutionResultBugList =c.list();
			
	return testExecutionResultBugList;
	
	}
	


	@Override
	@Transactional
	public TestExecutionResultBugList getByBugWithCompleteInitialization(Integer bugId) {
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
				if (defect.getTestCaseExecutionResult() != null){
					Hibernate.initialize(defect.getTestCaseExecutionResult());
					if(defect.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan() != null){
						Hibernate.initialize(defect.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getTestCase());
						Hibernate.initialize(defect.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getTester());
					}
				}	
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
				
				if(defect.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getFeature()!=null){
					Hibernate.initialize(defect.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getFeature());
				}
			}
			log.debug("getByBugId successful");
		} catch (RuntimeException re) {
			log.error("getByBugId failed", re);
			// throw re;
		}
		return defect;

	}


	@Override
	@Transactional
	public List<TestExecutionResultBugList> listDefectsAnalyseFromData(String stageName,String featureName, Integer action, Integer jtStartIndex, Integer jtPageSize) {
		log.info("Defect analyse listing");
		
		Criteria c = sessionFactory.getCurrentSession().createCriteria(TestExecutionResultBugList.class, "tebl");
		
		c.add(Restrictions.and(Restrictions.isNotNull("tebl.bugManagementSystemBugId"), Restrictions.ne("tebl.bugManagementSystemBugId", "")));
		if (action != 0)
		{
			c.add(Restrictions.ne("tebl.bugFilingStatus.workFlowId", 19));
		}
		if (stageName != null){
			c.add(Restrictions.and(Restrictions.like("tebl.bugTitle", "%"+stageName+"%").ignoreCase(), Restrictions.like("tebl.bugTitle", "%"+featureName+"%").ignoreCase()));
		}
		
		if(jtStartIndex != null && jtPageSize != null)
			c.setFirstResult(jtStartIndex).setMaxResults(jtPageSize);
		c.addOrder(Order.desc("tebl.testExecutionResultBugId"));
		List<TestExecutionResultBugList> defectsList = c.list();
		
		for (TestExecutionResultBugList defect : defectsList) {
			if (!(defect == null)) {
				if (defect.getTestCaseExecutionResult() != null){
					Hibernate.initialize(defect.getTestCaseExecutionResult());
					if(defect.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan() != null){
						Hibernate.initialize(defect.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getTestCase());
						Hibernate.initialize(defect.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getTester());
					}
				}	
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
		}
		log.info("Total Defects for Analyse -----"+defectsList.size());
		return defectsList;
	}
	
	@Override
	@Transactional
	public void update(TestExecutionResultBugList defects) {
		log.debug("updating TestExecutionResultBugList instance");
		try {
			log.info("update is executed");
			sessionFactory.getCurrentSession().saveOrUpdate(defects);
			log.info("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
		}
		
	}


	@Override
	@Transactional
	public List<TestCaseDTO> listWorkPackageEnvironmentSummary(Integer workpackageId) {
		List<TestCaseDTO> listOfWorkpackageTestcaseplan = new ArrayList<TestCaseDTO>();
		try{
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackageTestCaseExecutionPlan.class, "wptcep");
			c.createAlias("wptcep.workPackage", "wp");
			c.createAlias("wptcep.runConfiguration","run");
			c.createAlias("run.runconfiguration", "runConfig");
			c.createAlias("wptcep.tester", "user");
			c.add(Restrictions.eq("wp.workPackageId", workpackageId));
			c.add(Restrictions.eq("wptcep.status", 1));
			
			ProjectionList projectionList = Projections.projectionList();
			
			projectionList.add(Property.forName("user.loginId").as("userName"));
			projectionList.add(Property.forName("runConfig.runconfigName").as("runconfigName"));
			projectionList.add(Property.forName("wptcep.executionStatus").as("executionStatusId"));
			projectionList.add(Property.forName("user.userId").as("userId"));
			c.setProjection(projectionList);
			
			List<Object[]> list = c.list();
			log.debug("Result Set Size : " + list.size());
			TestCaseDTO testCaseDTO = null;
			for (Object[] row : list) {
				testCaseDTO = new TestCaseDTO();
				testCaseDTO.setTesterName((String)row[0]);
				testCaseDTO.setEnvCombName((String)row[1]);
				Integer exeStatus=((Integer)row[2]).intValue();
				testCaseDTO.setTesterId((Integer)row[3]);
				testCaseDTO.setExecutionPriorityId(exeStatus);
				listOfWorkpackageTestcaseplan.add(testCaseDTO);
				log.debug("Status Summary for UserName : "  +((String)row[0]) +"  Env Combination Name: "+((String)row[1]));
			}
			} catch (Exception e) {
				log.error("List failed", e);
				return null;
			}
			log.debug("list all sucessfull: "+listOfWorkpackageTestcaseplan.size());
			return listOfWorkpackageTestcaseplan;
	}
	
	@Override
	@Transactional
	public List<String> listRunConfigurationNameBywpId(Integer workpackageId) {
		List<String> wptepList =  new ArrayList<String>();
		String sql="";
		Query query=null;
		try {
			sql = "SELECT DISTINCT runconfigu3_.runconfigName FROM workpackage_testcase_execution_plan  wtep INNER JOIN workpackage_has_runconfiguration wprunconfi2_ ON wtep.testRunConfigurationId=wprunconfi2_.WorkpackageRunConfigurationId INNER JOIN runconfiguration runconfigu3_ ON wprunconfi2_.runconfigurationId=runconfigu3_.runconfigId WHERE wtep.workPackageId=:wpId AND wtep.status=1 AND wtep.testerId IS NOT NULL ORDER BY runconfigu3_.runconfigName";
			query = sessionFactory.getCurrentSession().createSQLQuery(sql).setParameter("wpId", workpackageId);
			wptepList = query.list();
			log.debug("Result Set Size : " + wptepList.size());
			} catch (Exception e) {
				log.error("List failed", e);
				return null;
			}
			log.debug("list all sucessfull: "+wptepList.size());
			return wptepList;
	}
	
	@Override
	@Transactional
	public List<TestCaseDTO> listWorkPackageTestCasesExecutionPlanBywpId(Integer workpackageId) {
		List<TestCaseDTO> testCaseDTOList =  new ArrayList<TestCaseDTO>();
		
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackageTestCaseExecutionPlan.class, "wptcep");
			c.createAlias("wptcep.workPackage", "wp");
			c.createAlias("wptcep.runConfiguration","run");
			c.createAlias("run.runconfiguration", "runConfig");
			c.createAlias("wptcep.tester", "user");
			c.createAlias("wptcep.testRunJob", "runJob");
			c.add(Restrictions.eq("wp.workPackageId", workpackageId));
			c.add(Restrictions.eq("wptcep.status", 1));
			c.add(Restrictions.isNotNull("user.userId"));
			
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Property.forName("user.userId").as("userId"));
			projectionList.add(Property.forName("user.loginId").as("userName"));
			projectionList.add(Property.forName("runConfig.runconfigName").as("runconfigName"));
			projectionList.add(Property.forName("runJob.testRunJobId").as("testRunJobId"));
			projectionList.add(Projections.groupProperty("user.loginId"));
			projectionList.add(Projections.groupProperty("runConfig.runconfigName"));
			
			c.setProjection(projectionList);
			
			List<Object[]> list = c.list();
			log.debug("Result Set Size : " + list.size());
			TestCaseDTO testCaseDTO = null;
			for (Object[] row : list) {
				testCaseDTO = new TestCaseDTO();
				testCaseDTO.setTesterId((Integer)row[0]);
				testCaseDTO.setTesterName((String)row[1]);
				testCaseDTO.setEnvCombName((String)row[2]);
				testCaseDTOList.add(testCaseDTO);
				log.debug("Status Summary for UserName : "  +((String)row[1]) +"  Env Combination Name: "+((String)row[2]));
			}
			} catch (Exception e) {
				log.error("List failed", e);
				return null;
			}
			log.debug("list all sucessfull: "+testCaseDTOList.size());
			return testCaseDTOList;
	}


	@Override
	@Transactional
	public List<TestCaseDTO> listWorkPackageTimeSheetBywpId(Integer workPackageId) {
		List<TestCaseDTO> testDTOList =  new ArrayList<TestCaseDTO>();
		
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackageTestCaseExecutionPlan.class, "wptcep");
			c.createAlias("wptcep.workPackage", "wp");
			c.createAlias("wptcep.runConfiguration","run");
			c.createAlias("run.runconfiguration", "runConfig");
			c.createAlias("wptcep.tester", "user");
			c.createAlias("wptcep.testCaseExecutionResult", "test");
			c.add(Restrictions.eq("wp.workPackageId", workPackageId));
			c.add(Restrictions.eq("wptcep.status", 1));
			c.add(Restrictions.isNotNull("user.userId"));
			
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Property.forName("user.userId").as("userId"));
			projectionList.add(Property.forName("user.loginId").as("userName"));
			projectionList.add(Property.forName("runConfig.runconfigName").as("runconfigName"));
			projectionList.add(Projections.min("test.startTime"));
			projectionList.add(Projections.max("test.endTime"));
			projectionList.add(Projections.groupProperty("user.loginId"));
			projectionList.add(Projections.groupProperty("runConfig.runconfigName"));
			
			c.setProjection(projectionList);
			
			List<Object[]> list = c.list();
			log.debug("Result Set Size : " + list.size());
			TestCaseDTO testCaseDTO = null;
			for (Object[] row : list) {
				testCaseDTO = new TestCaseDTO();
				testCaseDTO.setTesterId((Integer)row[0]);
				testCaseDTO.setTesterName((String)row[1]);
				testCaseDTO.setEnvCombName((String)row[2]);
				testCaseDTO.setStartTime((Date)row[3]);
				testCaseDTO.setEndTime((Date)row[4]);
				testDTOList.add(testCaseDTO);
				log.debug("Status Summary for UserName : "  +((String)row[1]) +"  Env Combination Name: "+((String)row[2]));
			}
			} catch (Exception e) {
				log.error("List failed", e);
				return null;
			}
			log.debug("list all sucessfull: "+testDTOList.size());
			return testDTOList;
	}
	

}
