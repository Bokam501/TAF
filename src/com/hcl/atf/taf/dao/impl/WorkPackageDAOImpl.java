package com.hcl.atf.taf.dao.impl;

import java.awt.image.BufferedImage;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.set.ListOrderedSet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.hcl.atf.taf.constants.ClientReponseMessage;
import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.constants.TAFConstants;
import com.hcl.atf.taf.controller.TestRunPlanExecutionStatusVO;
import com.hcl.atf.taf.controller.ToolIntegrationController;
import com.hcl.atf.taf.controller.utilities.CommonUtility;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.dao.EnvironmentDAO;
import com.hcl.atf.taf.dao.ExecutionTypeMasterDAO;
import com.hcl.atf.taf.dao.HostHeartbeatDAO;
import com.hcl.atf.taf.dao.ProductBuildDAO;
import com.hcl.atf.taf.dao.ProductFeatureDAO;
import com.hcl.atf.taf.dao.ProductLocaleDao;
import com.hcl.atf.taf.dao.ProductMasterDAO;
import com.hcl.atf.taf.dao.TestCaseListDAO;
import com.hcl.atf.taf.dao.TestCaseStepsListDAO;
import com.hcl.atf.taf.dao.TestFactoryProductCoreResourcesDao;
import com.hcl.atf.taf.dao.TestManagementSystemDAO;
import com.hcl.atf.taf.dao.TestSuiteListDAO;
import com.hcl.atf.taf.dao.UserListDAO;
import com.hcl.atf.taf.dao.WorkPackageDAO;
import com.hcl.atf.taf.model.ActualShift;
import com.hcl.atf.taf.model.Attachment;
import com.hcl.atf.taf.model.DecouplingCategory;
import com.hcl.atf.taf.model.EntityMaster;
import com.hcl.atf.taf.model.Environment;
import com.hcl.atf.taf.model.EnvironmentCombination;
import com.hcl.atf.taf.model.Evidence;
import com.hcl.atf.taf.model.EvidenceType;
import com.hcl.atf.taf.model.ExecutionPriority;
import com.hcl.atf.taf.model.ExecutionTypeMaster;
import com.hcl.atf.taf.model.GenericDevices;
import com.hcl.atf.taf.model.HostHeartbeat;
import com.hcl.atf.taf.model.HostList;
import com.hcl.atf.taf.model.LifeCyclePhase;
import com.hcl.atf.taf.model.MobileType;
import com.hcl.atf.taf.model.PerformanceLevel;
import com.hcl.atf.taf.model.ProductBuild;
import com.hcl.atf.taf.model.ProductFeature;
import com.hcl.atf.taf.model.ProductLocale;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.ProductType;
import com.hcl.atf.taf.model.ProductUserRole;
import com.hcl.atf.taf.model.ProductVersionListMaster;
import com.hcl.atf.taf.model.RunConfiguration;
import com.hcl.atf.taf.model.ServerType;
import com.hcl.atf.taf.model.Skill;
import com.hcl.atf.taf.model.TestCaseConfiguration;
import com.hcl.atf.taf.model.TestCaseExecutionResult;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.TestCaseStepsList;
import com.hcl.atf.taf.model.TestCycle;
import com.hcl.atf.taf.model.TestExecutionResultBugList;
import com.hcl.atf.taf.model.TestFactoryResourceReservation;
import com.hcl.atf.taf.model.TestManagementSystem;
import com.hcl.atf.taf.model.TestRunJob;
import com.hcl.atf.taf.model.TestRunJobTestSuiteHasTestCase;
import com.hcl.atf.taf.model.TestRunPlan;
import com.hcl.atf.taf.model.TestRunPlanGroup;
import com.hcl.atf.taf.model.TestRunPlangroupHasTestRunPlan;
import com.hcl.atf.taf.model.TestStepExecutionResult;
import com.hcl.atf.taf.model.TestSuiteList;
import com.hcl.atf.taf.model.TestToolMaster;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.UserRoleMaster;
import com.hcl.atf.taf.model.UserTypeMasterNew;
import com.hcl.atf.taf.model.WorkFlow;
import com.hcl.atf.taf.model.WorkFlowEvent;
import com.hcl.atf.taf.model.WorkPackage;
import com.hcl.atf.taf.model.WorkPackageDemandProjection;
import com.hcl.atf.taf.model.WorkPackageFeature;
import com.hcl.atf.taf.model.WorkPackageFeatureExecutionPlan;
import com.hcl.atf.taf.model.WorkPackageTestCase;
import com.hcl.atf.taf.model.WorkPackageTestCaseExecutionPlan;
import com.hcl.atf.taf.model.WorkPackageTestSuite;
import com.hcl.atf.taf.model.WorkPackageTestSuiteExecutionPlan;
import com.hcl.atf.taf.model.WorkShiftMaster;
import com.hcl.atf.taf.model.WorkpackageRunConfiguration;
import com.hcl.atf.taf.model.dto.MetricsMasterDTO;
import com.hcl.atf.taf.model.dto.MetricsMasterDefectsDTO;
import com.hcl.atf.taf.model.dto.MetricsMasterTestCaseResultDTO;
import com.hcl.atf.taf.model.dto.ScriptLessExecutionDTO;
import com.hcl.atf.taf.model.dto.TestCaseDTO;
import com.hcl.atf.taf.model.dto.VerificationResult;
import com.hcl.atf.taf.model.dto.WorkPackageBuildTestCaseSummaryDTO;
import com.hcl.atf.taf.model.dto.WorkPackageDayWisePlanDTO;
import com.hcl.atf.taf.model.dto.WorkPackageDemandProjectionDTO;
import com.hcl.atf.taf.model.dto.WorkPackageExecutionPlanUserDetails;
import com.hcl.atf.taf.model.dto.WorkPackageStatusSummaryDTO;
import com.hcl.atf.taf.model.dto.WorkPackageTCEPSummaryDTO;
import com.hcl.atf.taf.model.dto.WorkPackageTestCaseExecutionPlanStatusDTO;
import com.hcl.atf.taf.model.dto.WorkPackageTestCaseSummaryDTO;
import com.hcl.atf.taf.model.json.JsonTestCycle;
import com.hcl.atf.taf.model.json.JsonUserList;
import com.hcl.atf.taf.model.json.JsonWorkPackageFeature;
import com.hcl.atf.taf.model.json.JsonWorkPackageFeatureExecutionPlan;
import com.hcl.atf.taf.model.json.JsonWorkPackageTestCase;
import com.hcl.atf.taf.model.json.JsonWorkPackageTestCaseExecutionPlan;
import com.hcl.atf.taf.model.json.JsonWorkPackageTestCaseExecutionPlanForTester;
import com.hcl.atf.taf.model.json.JsonWorkPackageTestSuiteExecutionPlan;
import com.hcl.atf.taf.mongodb.service.MongoDBService;
import com.hcl.atf.taf.service.EmailService;
import com.hcl.atf.taf.service.TestExecutionService;
import com.hcl.atf.taf.service.WorkPackageService;

@Repository
public class WorkPackageDAOImpl implements WorkPackageDAO {
	private static final Log log = LogFactory.getLog(WorkPackageDAOImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private ProductLocaleDao productLocaleDao;
	
	@Autowired
	private TestCaseListDAO testCaseListDAO;
	
	@Autowired
	private TestCaseStepsListDAO testCaseStepsListDAO;
	@Autowired
	private EnvironmentDAO environmentDAO;
	@Autowired
	private ProductBuildDAO productBuildDAO;
	@Autowired
	private ExecutionTypeMasterDAO executionTypeMasterDAO;
	@Autowired
	private UserListDAO userListDAO;
	@Autowired
	private ProductMasterDAO productMasterDAO;
	@Autowired
	private TestSuiteListDAO testSuiteListDAO;
	@Autowired
	private HostHeartbeatDAO hostHeartbeatDAO;
	@Autowired
	private ProductFeatureDAO productFeatureDAO;
	@Autowired
	private WorkPackageService workPackageService;
	@Autowired
	private TestFactoryProductCoreResourcesDao testFactoryProductCoreResourcesDao;
	private ToolIntegrationController toolIntegrationController;
	@Autowired
	private	TestManagementSystemDAO testManagementSystemDAO;
	@Autowired
	private	MongoDBService mongoDBService;
	@Autowired
	private	TestExecutionService testExecutionService;
	
	@Autowired
	private EmailService emailService;
	
	@Value("#{ilcmProps['hibernate.dialect']}")
	private String databaseDialect;
	
	@Override
	@Transactional
	public List<WorkPackageTestCase> listWorkPackageTestCases(
			int workPackageId) {
		log.debug("listing listWorkPackageTestCases instance");
		List<WorkPackageTestCase> workPackageTestCases = null;
		try {
			workPackageTestCases = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from WorkPackageTestCase wptc where wptc.workPackage.workPackageId = "
									+ workPackageId
									+ " order by wptc.testCase.testCaseId asc")
					.list();
			if (!(workPackageTestCases == null || workPackageTestCases
					.isEmpty())) {
				for (WorkPackageTestCase dl : workPackageTestCases) {
					Hibernate.initialize(dl.getTestCase().getProductMaster());
					Hibernate.initialize(dl.getWorkPackage().getProductBuild().getProductVersion().getProductMaster());
					Hibernate.initialize(dl.getTestCase().getDecouplingCategory());

				}
			}
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return workPackageTestCases;
	}

	@Override
	@Transactional
	public List<WorkPackageTestCase> listWorkPackageTestCases(
			int workPackageId, int jtStartIndex, int jtPageSize) {
		log.debug("listing listWorkPackageTestCases instance");
		List<WorkPackageTestCase> workPackageTestCases = null;
		try {
			if(jtStartIndex!=-1 && jtPageSize!=-1){
				workPackageTestCases = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from WorkPackageTestCase wptc where wptc.workPackage.workPackageId = "
									+ workPackageId
									+ " order by wptc.testCase.testCaseId asc")
					.setFirstResult(jtStartIndex).setMaxResults(jtPageSize).list();
			}else{
				workPackageTestCases = sessionFactory
						.getCurrentSession()
						.createQuery(
								"from WorkPackageTestCase wptc where wptc.workPackage.workPackageId = "
										+ workPackageId
										+ " order by wptc.testCase.testCaseId asc").list();
					
			}
			if (!(workPackageTestCases == null || workPackageTestCases
					.isEmpty())) {
				for (WorkPackageTestCase dl : workPackageTestCases) {
					Hibernate.initialize(dl.getTestCase().getProductMaster());
					Hibernate.initialize(dl.getWorkPackage().getProductBuild().getProductVersion().getProductMaster());
					Hibernate.initialize(dl.getTestCase().getDecouplingCategory());
				}
			}
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return workPackageTestCases;
	}
	
	@Override
	@Transactional
	public List<JsonWorkPackageTestCase> listJsonWorkPackageTestCases(int workPackageId, int jtStartIndex, int jtPageSize) {
		log.debug("listing listJsonWorkPackageTestCases instance");
		List<WorkPackageTestCase> workPackageTestCases = null;
		List<JsonWorkPackageTestCase> jsonWorkPackageTestCasesList = new ArrayList<JsonWorkPackageTestCase>();
		try {
			if(jtStartIndex!=-1 && jtPageSize!=-1){
				workPackageTestCases = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from WorkPackageTestCase wptc where wptc.workPackage.workPackageId = "
									+ workPackageId
									+ " order by wptc.testCase.testCaseId asc")
					.setFirstResult(jtStartIndex).setMaxResults(jtPageSize).list();
			}else{
				workPackageTestCases = sessionFactory
						.getCurrentSession()
						.createQuery(
								"from WorkPackageTestCase wptc where wptc.workPackage.workPackageId = "
										+ workPackageId
										+ " order by wptc.testCase.testCaseId asc").list();
					
			}
			if (!(workPackageTestCases == null || workPackageTestCases
					.isEmpty())) {
				for (WorkPackageTestCase dl : workPackageTestCases) {
					Hibernate.initialize(dl.getTestCase().getProductMaster());
					Hibernate.initialize(dl.getWorkPackage().getProductBuild().getProductVersion().getProductMaster());
					Hibernate.initialize(dl.getTestCase().getDecouplingCategory());
					jsonWorkPackageTestCasesList.add(new JsonWorkPackageTestCase(dl));
				}
			}
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return jsonWorkPackageTestCasesList;
	}
	
	@Override
	@Transactional
	public Integer getWorkPackageTestCasesCount(int workPackageId, int jtStartIndex, int jtPageSize) {
		log.debug("listing getWorkPackageTestCasesCount instance");
		int wpTCCount = 0;
		
		String sql="select count(*) from work_package_testcases wpt where wpt.workPackageId=:wpid group by wpt.testcaseId order by wpt.testcaseId asc";
		try {
			if(jtStartIndex!=-1 && jtPageSize!=-1){
				wpTCCount=((Number) sessionFactory.getCurrentSession().createSQLQuery(sql).setParameter("wpid", workPackageId)
						.setFirstResult(jtStartIndex).setMaxResults(jtPageSize)
						.uniqueResult()).intValue();				
			}else{
				wpTCCount=((Number) sessionFactory.getCurrentSession().createSQLQuery(sql).setParameter("wpid", workPackageId)
						.uniqueResult()).intValue();					
			}
		
		} catch (RuntimeException re) {
			log.error("list getWorkPackageTestCasesCount", re);
		}
		return wpTCCount;
	}
	
	@Override
	@Transactional
	public Integer getWorkPackageTestCaseOfTCID(int testcaseId) {
		log.debug("listing getWorkPackageTestCaseOfTCID instance");
		Integer wptcId = 0;
		
		String sql="select wpt.id from work_package_testcases wpt where wpt.testcaseId=:tcid order by wpt.testcaseId asc";
		try {			
				wptcId=(Integer) ((Number) sessionFactory.getCurrentSession().createSQLQuery(sql).setParameter("tcid", testcaseId)
						.uniqueResult());		
		} catch (RuntimeException re) {
			log.error("list getWorkPackageTestCasesCount", re);
		}
		return wptcId;
	}
	@Override
	@Transactional
	public WorkPackage getWorkPackageById(int workPackageId) {
		WorkPackage workPackage = null;
		boolean isCombinedJob = false;
		Set<TestRunJob> testRunJobList = new HashSet<TestRunJob>();
		try {
			List list = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from WorkPackage w where w.workPackageId=:workPackageId")
					.setParameter("workPackageId", workPackageId).list();
			workPackage = (list != null && list.size() != 0) ? (WorkPackage) list
					.get(0) : null;
				
			if (workPackage != null) {				
				
				
				if(workPackage.getProductBuild()!=null){
					Hibernate.initialize(workPackage.getProductBuild());
					if(workPackage.getProductBuild().getProductVersion() != null){
						Hibernate.initialize(workPackage.getProductBuild().getProductVersion());
						if(workPackage.getProductBuild().getProductVersion().getProductMaster() != null){
							Hibernate.initialize(workPackage.getProductBuild().getProductVersion().getProductMaster());
							Hibernate.initialize(workPackage.getProductBuild().getProductVersion().getProductMaster().getProductName());
							Hibernate.initialize(workPackage.getProductBuild().getProductVersion().getProductMaster().getProductId());
							
							if(workPackage.getProductBuild()
									.getProductVersion().getProductMaster().getProductMode() != null){
								Hibernate.initialize(workPackage.getProductBuild()
										.getProductVersion().getProductMaster().getProductMode());
							}
							if(workPackage.getProductBuild()
									.getProductVersion().getProductMaster().getProductType() != null){
								Hibernate.initialize(workPackage.getProductBuild()
										.getProductVersion().getProductMaster().getProductType());
							}
							if(workPackage.getProductBuild()
									.getProductVersion().getProductMaster().getGenericeDevices() != null){
								Hibernate.initialize(workPackage.getProductBuild()
										.getProductVersion().getProductMaster().getGenericeDevices());								
							}
							if(workPackage.getTestRunPlan() != null){
								if(workPackage.getTestRunPlan().getProductVersionListMaster().getProductMaster().getCustomer()!=null){
									Hibernate.initialize(workPackage.getTestRunPlan().getProductVersionListMaster().getProductMaster().getCustomer());
								}	
							}
						}
					}
				}
				Hibernate.initialize(workPackage.getUserList());
				Hibernate.initialize(workPackage.getLifeCyclePhase());
				Set<GenericDevices> genericDevices =workPackage.getProductBuild()
						.getProductVersion().getProductMaster().getGenericeDevices();
				for(GenericDevices gd:genericDevices){
					Hibernate.initialize(gd.getDeviceLab());
					Hibernate.initialize(gd.getDeviceModelMaster());
					Hibernate.initialize(gd.getHostList());
					Hibernate.initialize(gd.getPlatformType());
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
				}
				if(workPackage.getProductBuild().getProductVersion().getProductMaster() != null && workPackage.getProductBuild().getProductVersion().getProductMaster().getHostLists() != null)
					Hibernate.initialize(workPackage.getProductBuild().getProductVersion().getProductMaster().getHostLists());
				if(workPackage.getProductBuild().getProductVersion().getProductMaster() != null && workPackage.getProductBuild().getProductVersion().getProductMaster().getTestFactory() != null)
					Hibernate.initialize(workPackage.getProductBuild().getProductVersion().getProductMaster().getTestFactory());
				
				if(workPackage.getProductBuild().getProductVersion().getProductMaster().getProductVersionListMasters() != null){
					Hibernate.initialize(workPackage.getProductBuild().getProductVersion().getProductMaster().getProductVersionListMasters());
				}
				try{
					if(workPackage.getProductBuild().getProductVersion().getProductMaster() != null && workPackage.getProductBuild().getProductVersion().getProductMaster().getTestManagementSystems() != null)
						Hibernate.initialize(workPackage.getProductBuild().getProductVersion().getProductMaster().getTestManagementSystems());
				} catch(Exception e){
					log.info("Error in initializing Test Management Systems : "+e.getMessage());
				}
				Hibernate.initialize(workPackage.getEnvironmentList());
				Hibernate.initialize(workPackage.getLocaleList());
				Hibernate.initialize(workPackage.getWorkPackageTestCases());
				Hibernate.initialize(workPackage.getWorkPackageTestSuites());
				Hibernate.initialize(workPackage.getWorkPackageTestCaseExecutionPlan());
				Hibernate.initialize(workPackage.getEnvironmentCombinationList());
				Hibernate.initialize(workPackage.getProductFeature());				
				Hibernate.initialize(workPackage.getProductBuild().getBuildType());
				
				Set<WorkPackageFeature> wpfs=workPackage.getWorkPackageFeature();
				Hibernate.initialize(workPackage.getWorkPackageFeature());
				for(WorkPackageFeature workPackageFeature:workPackage.getWorkPackageFeature()){
					Hibernate.initialize(workPackageFeature.getFeature());
					if(workPackageFeature.getFeature()!=null){
						Hibernate.initialize(workPackageFeature.getFeature().getTestCaseList());
						Hibernate.initialize(workPackageFeature.getFeature().getExecutionPriority());
					}
				}
				Set<WorkpackageRunConfiguration> wpRunConfig=workPackage.getWorkPackageRunConfigSet();
				if(wpRunConfig.size()!=0){
					Hibernate.initialize(workPackage.getWorkPackageRunConfigSet());
					for(WorkpackageRunConfiguration wprc:wpRunConfig){						
						if(wprc.getRunconfiguration()!=null){
							Hibernate.initialize(wprc.getRunconfiguration());
							Hibernate.initialize(wprc.getRunconfiguration().getEnvironmentcombination());
							Hibernate.initialize(wprc.getRunconfiguration().getGenericDevice());
							if(wprc.getRunconfiguration().getGenericDevice()!=null){
								if(wprc.getRunconfiguration().getGenericDevice().getHostList()!=null){
									Hibernate.initialize(wprc.getRunconfiguration().getGenericDevice().getHostList());
									Hibernate.initialize(wprc.getRunconfiguration().getGenericDevice().getHostList().getCommonActiveStatusMaster());
								}
							}
							if(wprc.getRunconfiguration().getHostList()!=null){
								Hibernate.initialize(wprc.getRunconfiguration().getHostList());
								Hibernate.initialize(wprc.getRunconfiguration().getHostList().getCommonActiveStatusMaster());
							}
							if(wprc.getRunconfiguration().getTestRunPlan()!=null){
								Hibernate.initialize(wprc.getRunconfiguration().getTestRunPlan());
								if(wprc.getRunconfiguration().getTestRunPlan().getProductVersionListMaster()!=null){
									Hibernate.initialize(wprc.getRunconfiguration().getTestRunPlan().getProductVersionListMaster());
									if(wprc.getRunconfiguration().getTestRunPlan().getProductVersionListMaster().getProductMaster()!=null){
										Hibernate.initialize(wprc.getRunconfiguration().getTestRunPlan().getProductVersionListMaster().getProductMaster());
										if(wprc.getRunconfiguration().getTestRunPlan().getProductVersionListMaster().getProductMaster().getProductType()!=null){
											Hibernate.initialize(wprc.getRunconfiguration().getTestRunPlan().getProductVersionListMaster().getProductMaster().getProductType());
										}
									}
								}
								
								Hibernate.initialize(wprc.getRunconfiguration().getTestRunPlan().getAttachments());
								if( wprc.getRunconfiguration().getTestRunPlan().getAttachments() != null &&  wprc.getRunconfiguration().getTestRunPlan().getAttachments().size()>0){
									Set<Attachment> attachmentSet= wprc.getRunconfiguration().getTestRunPlan().getAttachments();
									Hibernate.initialize(attachmentSet);
									for(Attachment attach: attachmentSet){
										Hibernate.initialize(attach);
									}
								}
							}
							
						}
					}
				}
				Hibernate.initialize(workPackage.getRunConfigurationList());
				Set<RunConfiguration> runConfigurations = workPackage.getRunConfigurationList();
				for(RunConfiguration rc:runConfigurations){
					Hibernate.initialize(rc.getEnvironmentcombination());
					Hibernate.initialize(rc.getGenericDevice());
					Hibernate.initialize(rc.getHostList());
					if(rc.getWorkPackageRunConfigSet().size()!=0){
						Hibernate.initialize(rc.getWorkPackageRunConfigSet());
						Set<WorkpackageRunConfiguration>  wprunConfigSet=rc.getWorkPackageRunConfigSet();
						for(WorkpackageRunConfiguration wprunConfig:wprunConfigSet){
							 Set<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlanSet =wprunConfig.getWorkPackageTestCaseExecutionPlan();
							  if(workPackageTestCaseExecutionPlanSet.size()!=0){
								  
							for(WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan:workPackageTestCaseExecutionPlanSet){
								Hibernate.initialize(workPackageTestCaseExecutionPlan.getTestCaseExecutionResult());
								TestCaseExecutionResult trcRes=workPackageTestCaseExecutionPlan.getTestCaseExecutionResult();
								if(trcRes!=null){
								Set<TestStepExecutionResult> tcstepexecRes=trcRes.getTestStepExecutionResultSet();
								if(tcstepexecRes.size()!=0){
								for(TestStepExecutionResult testStepExecRes:tcstepexecRes){
									Hibernate.initialize(testStepExecRes.getTestCaseExecutionResult());
								}
								}
							}
								Set<Environment> environments=workPackageTestCaseExecutionPlan.getEnvironmentList();
								if(environments.size()!=0){
									Hibernate.initialize(environments);
									for(Environment environment:environments){
										Hibernate.initialize(environment.getEnvironmentCategory());
										Hibernate.initialize(environment.getEnvironmentCategory().getEnvironmentGroup());
									}
								}
								
								
							}
							
						}
							
						}
					}
				}
				Set<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlans =workPackage.getWorkPackageTestCaseExecutionPlan();
				for(WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan:workPackageTestCaseExecutionPlans){
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getRunConfiguration());
					if(workPackageTestCaseExecutionPlan.getRunConfiguration()!=null){
						if(workPackageTestCaseExecutionPlan.getRunConfiguration().getWorkpackage()!=null){
						Hibernate.initialize(workPackageTestCaseExecutionPlan.getRunConfiguration().getWorkpackage());
						}
						Hibernate.initialize(workPackageTestCaseExecutionPlan.getRunConfiguration().getRunconfiguration());
						Hibernate.initialize(workPackageTestCaseExecutionPlan.getRunConfiguration().getRunconfiguration().getEnvironmentcombination());
						Hibernate.initialize(workPackageTestCaseExecutionPlan.getRunConfiguration().getRunconfiguration().getGenericDevice());						
					}
					if(workPackageTestCaseExecutionPlan.getWorkPackage() != null){
						Hibernate.initialize(workPackageTestCaseExecutionPlan.getWorkPackage());
						if(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild() != null){
							Hibernate.initialize(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild());
							if(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion() != null){
								Hibernate.initialize(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion());
							}
						}
					}
					Set<Environment> environments=workPackageTestCaseExecutionPlan.getEnvironmentList();
					Hibernate.initialize(environments);
					for(Environment environment:environments){
						Hibernate.initialize(environment.getEnvironmentCategory());
						Hibernate.initialize(environment.getEnvironmentCategory().getEnvironmentGroup());
					}
				}
				Set<WorkPackageTestCase> workPackageTestCases =workPackage.getWorkPackageTestCases();
				for(WorkPackageTestCase workPackageTestCase:workPackageTestCases){
					Hibernate.initialize(workPackageTestCase.getTestCase());
					Hibernate.initialize(workPackageTestCase.getTestCase().getTestSuiteLists());
					Hibernate.initialize(workPackageTestCase.getTestCase().getTestCasePriority());
					Hibernate.initialize(workPackageTestCase.getTestCase().getExecutionTypeMaster());
				}
				
				Set<WorkPackageTestSuite> workPackageTestSuites =workPackage.getWorkPackageTestSuites();
				for(WorkPackageTestSuite workPackageTestSuite:workPackageTestSuites){
					Hibernate.initialize(workPackageTestSuite.getTestSuite());
					if(workPackageTestSuite.getTestSuite()!=null)
					Hibernate.initialize(workPackageTestSuite.getTestSuite().getExecutionPriority());
					Hibernate.initialize(workPackageTestSuite.getTestSuite().getTestCaseLists());
				}
				for(WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan:workPackageTestCaseExecutionPlans){
				Set<TestCaseConfiguration> testCaseConfigurationSet =workPackageTestCaseExecutionPlan.getTestCaseConfigurationSet();
				for(TestCaseConfiguration testCaseConfiguration:testCaseConfigurationSet){
					Hibernate.initialize(testCaseConfiguration.getWorkpackage_run_list());
					Hibernate.initialize(testCaseConfiguration.getEnvironmentCombination());
				}
			}
				
				for(WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan:workPackageTestCaseExecutionPlans){
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getTestCaseExecutionResult());
					TestCaseExecutionResult trcRes=workPackageTestCaseExecutionPlan.getTestCaseExecutionResult();
					if(trcRes!=null){
					Set<TestStepExecutionResult> tcstepexecRes=trcRes.getTestStepExecutionResultSet();
					if(tcstepexecRes.size()!=0){
					for(TestStepExecutionResult testStepExecRes:tcstepexecRes){
						Hibernate.initialize(testStepExecRes.getTestCaseExecutionResult());
					}
					}
				}
					
				}
				Hibernate.initialize(workPackage.getWorkFlowEvent());
				Hibernate.initialize(workPackage.getWorkFlowEvent().getWorkFlow());
				Hibernate.initialize(workPackage.getTestcaseList());
				Hibernate.initialize(workPackage.getTestSuiteList());
				Hibernate.initialize(workPackage.getTestRunPlan());
				if(workPackage.getTestRunPlan() != null){
					Hibernate.initialize(workPackage.getTestRunPlan().getAttachments());
					if( workPackage.getTestRunPlan().getAttachments() != null &&  workPackage.getTestRunPlan().getAttachments().size()>0){
						Set<Attachment> attachmentSet= workPackage.getTestRunPlan().getAttachments();
						Hibernate.initialize(attachmentSet);
						for(Attachment attach: attachmentSet){
							Hibernate.initialize(attach);
						}
					}
				}
				
				Set<TestManagementSystem> testManagementSystems=
				workPackage.getProductBuild()
						.getProductVersion().getProductMaster().getTestManagementSystems();
				for (TestManagementSystem testManagementSystem : testManagementSystems) {
					Hibernate.initialize(testManagementSystem.getTestManagementSystemMappings());
				}
				
				Hibernate.initialize(workPackage.getEnvironmentCombinationList());
				if(workPackage.getResultsReportingMode() !=null && workPackage.getResultsReportingMode().equalsIgnoreCase("Combined job")){
					Hibernate.initialize(workPackage.getCombinedResultsReportingJob());
					if(workPackage.getCombinedResultsReportingJob() != null){
						testRunJobList.add(workPackage.getCombinedResultsReportingJob());
						workPackage.setTestRunJobSet(testRunJobList);
					}
				}else {
					Hibernate.initialize(workPackage.getTestRunJobSet());
					testRunJobList = workPackage.getTestRunJobSet();
				}
				for(TestRunJob trj : testRunJobList){
					Hibernate.initialize(trj);
					Hibernate.initialize(trj.getTestSuiteSet());
					for(TestSuiteList tsl : trj.getTestSuiteSet()){
						Hibernate.initialize(tsl);
						Hibernate.initialize(tsl.getTestCaseLists());
						for(TestCaseList tcl : tsl.getTestCaseLists()){
							Hibernate.initialize(tcl);
							Hibernate.initialize(tcl.getTestCaseStepsLists());
							for(TestCaseStepsList tcsl : tcl.getTestCaseStepsLists()){
								Hibernate.initialize(tcsl);
							}
						}
					}
				}
				Hibernate.initialize(workPackage.getTestCycle());
			}
		} catch (Exception re) {
			log.error("Exception occurres failed", re);
			
		}
		return workPackage;

	}
	
	@Override
	@Transactional
	public WorkPackage getWorkPackageByIdWithMinimalnitialization(int workPackageId) {
		WorkPackage workPackage = null;
		try {
			List list = sessionFactory
					.getCurrentSession()
					.createQuery("from WorkPackage w where w.workPackageId=:workPackageId")
					.setParameter("workPackageId", workPackageId).list();
			workPackage = (list != null && list.size() != 0) ? (WorkPackage) list.get(0) : null;
			if (workPackage != null) {
				if(workPackage.getProductBuild() != null){
					Hibernate.initialize(workPackage.getProductBuild());
					if(workPackage.getProductBuild().getProductVersion() != null){
						Hibernate.initialize(workPackage.getProductBuild().getProductVersion());
						Hibernate.initialize(workPackage.getProductBuild().getProductVersion().getProductMaster());
						if(workPackage.getProductBuild().getProductVersion().getProductMaster().getProductType()!=null){
							Hibernate.initialize(workPackage.getProductBuild().getProductVersion().getProductMaster().getProductType());
						}
					}					
				}				
			}
		} catch (RuntimeException re) {
			log.error("get workpackage by Id failed", re);
			re.printStackTrace();
		}catch (Exception e) {
			log.error("Exception occurred while getting the work package object for Id: "+workPackageId, e);
			e.printStackTrace();
		}
		return workPackage;
	}


	@Override
	public int addNewWorkPackageTestCases(List<WorkPackageTestCase> workPackageTestCases) {

		log.debug("Initializing workpackge with new test cases");
		int count = 0;
		try {

			if (workPackageTestCases == null || workPackageTestCases.isEmpty()) {
				return 0;
			}
			for (WorkPackageTestCase workPackageTestCase : workPackageTestCases) {
				sessionFactory.getCurrentSession().save(workPackageTestCase);
				count++;
			}
			log.debug("added workpackage test cases successfully");
		} catch (Exception e) {

			log.error("Unable to initialize workpackage with new testcases", e);
			return count;
		}

		return count;
	}

	@Override
	@Transactional
	public int totalRecordsCountForWorkPackageTestCases(int workPackageId) {

		log.debug("getting totalRecordsCountForWorkPackageTestCases");
		int count = 0;
		try {
			count = ((Number) sessionFactory
					.getCurrentSession()
					.createSQLQuery(
							"select count(*) from work_package_testcases wptc where workPackageId="
									+ workPackageId).uniqueResult()).intValue();

			log.debug("total records fetch successful");
		} catch (RuntimeException re) {
			log.error("total records fetch failed", re);
		}
		return count;
	}

	@Override
	@Transactional
	public WorkPackageTestCase updateWorkPackageTestCase(
			WorkPackageTestCase workPackageTestCase) {
		log.debug("Initializing workpackge with new test cases"+workPackageTestCase.getWorkPackage().getWorkPackageId());
		int count = 0;
		try {

			if (workPackageTestCase == null) {
				return null;
			}

			sessionFactory.getCurrentSession()
					.saveOrUpdate(workPackageTestCase);

			log.debug("Updated workpackage test cases successfully");
		} catch (Exception e) {

			log.error("Unable to initialize workpackage with new testcases", e);
			return null;
		}

		return workPackageTestCase;
	}

	@Override
	@Transactional
	public List<WorkPackage> list() {
		log.debug("listing all WorkPackages instance");
		List<WorkPackage> workPackageList = null;
		try {
			workPackageList = sessionFactory.getCurrentSession()
					.createQuery("from WorkPackage").list();
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return workPackageList;
	}

	@Override
	@Transactional
	public List<WorkPackage> list(int startIndex, int pageSize, Date startDate,Date endDate ) {
		log.debug("listing all WorkPackages instance");
		List<WorkPackage> workPackageList = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackage.class, "wpk");
			if (startDate != null) {
				c.add(Restrictions.ge("wpk.createDate", startDate));
			}
			if (endDate != null) {
				c.add(Restrictions.le("wpk.createDate", endDate));
			}
			c.addOrder(Order.asc("workPackageId"));
            c.setFirstResult(startIndex);
            c.setMaxResults(pageSize);
            workPackageList = c.list();	
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return workPackageList;
	}

	
	@Override
	@Transactional
	public int getTotalRecords() {
		log.debug("getting WorkPackages total records");
		int count = 0;
		try {
			count = ((Number) sessionFactory
					.getCurrentSession()
					.createSQLQuery(
							"select count(*) from workpackage where status=1")
					.uniqueResult()).intValue();
			log.debug("total records fetch successful");
		} catch (RuntimeException re) {
			log.error("total records fetch failed", re);
		}
		return count;

	}

	@Override
	@Transactional
	public List<Environment> getEnvironmentListByProductId(int productId) {
		log.debug("listing all getEnvironmentListByProductId instance");
		List<Environment> environmentList = null;
		try {
			environmentList = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from Environment e where e.productMaster.productId=:productId")
					.setParameter("productId", productId).list();
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return environmentList;
	}
	
	@Override
	@Transactional
	public int addWorkPackageTestcaseExecutionPlan(
			List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlanList) {
		log.debug("Adding workpackage testcases to execution plan");
		int count = 0;
		WorkPackage workPackage=null;
		try {
			if (workPackageTestCaseExecutionPlanList == null || workPackageTestCaseExecutionPlanList.isEmpty()) {
				return 0;
			}
			for (WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan : workPackageTestCaseExecutionPlanList) {
				sessionFactory.getCurrentSession().save(workPackageTestCaseExecutionPlan);
				ArrayList<TestStepExecutionResult> testStepExecutionResultList=new ArrayList<TestStepExecutionResult>();
				TestStepExecutionResult testStepExecutionResult=null;
				int i = workPackageTestCaseExecutionPlan.getTestCase().getTestCaseId().intValue();
				List<TestCaseStepsList> listTestCaseSteps=testCaseStepsListDAO.list(i);
				for(TestCaseStepsList testcasestep:listTestCaseSteps){
					testStepExecutionResult = new TestStepExecutionResult();
					testStepExecutionResult.setComments("");
					testStepExecutionResult.setIsApproved(0);
					testStepExecutionResult.setIsReviewed(0);
					testStepExecutionResult.setObservedOutput("");
					testStepExecutionResult.setResult("");
					testStepExecutionResult.setTestCaseExecutionResult(workPackageTestCaseExecutionPlan.getTestCaseExecutionResult());
					testStepExecutionResult.setTestcase(workPackageTestCaseExecutionPlan.getTestCase());
					testStepExecutionResult.setTestSteps(testcasestep);
					testStepExecutionResultList.add(testStepExecutionResult);
				}
				
				if(testStepExecutionResultList!=null && !testStepExecutionResultList.isEmpty()){
					saveTestStepExecutionResult(testStepExecutionResultList);
				}
				
				TestCaseConfiguration testCaseConfiguration =new TestCaseConfiguration();
				testCaseConfiguration.setDevice_combination_id(null);
				testCaseConfiguration.setWorkpackageRunConfiguration(workPackageTestCaseExecutionPlan.getRunConfiguration());
				testCaseConfiguration.setWorkpackage_run_list(workPackageTestCaseExecutionPlan);
				addTestCaseConfiguration(testCaseConfiguration);
			}
			log.debug("addWorkPackageTestcaseExecutionPlan successfully"+count);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Unable to Add workpackage testcases to execution plan", e);
			return count;
		}

		return count;
	}

	@Override
	@Transactional
	public int addWorkPackageTestcaseExecutionPlan(
			WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan) {
		log.debug("Adding workpackage testcases to execution plan");
		int count = 0;
		WorkPackage workPackage=null;
		try {
			if (workPackageTestCaseExecutionPlan!=null){
				sessionFactory.getCurrentSession().save(workPackageTestCaseExecutionPlan);
				ArrayList<TestStepExecutionResult> testStepExecutionResultList=new ArrayList<TestStepExecutionResult>();
				TestStepExecutionResult testStepExecutionResult=null;
				int i = workPackageTestCaseExecutionPlan.getTestCase().getTestCaseId().intValue();
				List<TestCaseStepsList> listTestCaseSteps=testCaseStepsListDAO.list(i);
				for(TestCaseStepsList testcasestep:listTestCaseSteps){
					testStepExecutionResult = new TestStepExecutionResult();
					testStepExecutionResult.setComments("");
					testStepExecutionResult.setIsApproved(0);
					testStepExecutionResult.setIsReviewed(0);
					testStepExecutionResult.setObservedOutput("");
					testStepExecutionResult.setResult("");
					testStepExecutionResult.setTestCaseExecutionResult(workPackageTestCaseExecutionPlan.getTestCaseExecutionResult());
					testStepExecutionResult.setTestcase(workPackageTestCaseExecutionPlan.getTestCase());
					testStepExecutionResult.setTestSteps(testcasestep);
					testStepExecutionResultList.add(testStepExecutionResult);
				}
				
				if(testStepExecutionResultList!=null && !testStepExecutionResultList.isEmpty()){
					saveTestStepExecutionResult(testStepExecutionResultList);
				}
				
				TestCaseConfiguration testCaseConfiguration =new TestCaseConfiguration();
				testCaseConfiguration.setDevice_combination_id(null);
				testCaseConfiguration.setWorkpackageRunConfiguration(workPackageTestCaseExecutionPlan.getRunConfiguration());
				testCaseConfiguration.setWorkpackage_run_list(workPackageTestCaseExecutionPlan);
				addTestCaseConfiguration(testCaseConfiguration);
			}
			log.debug("addWorkPackageTestcaseExecutionPlan successfully"+count);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Unable to Add workpackage testcases to execution plan", e);
			return count;
		}

		return count;
	}
	
	@Override
	@Transactional
	public Set<Environment> getEnvironmentSet(WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan) {
		log.debug("listing all getEnvironmentListByProductId instance");
		Set<Environment> environmentList = null;
		try {
			environmentList=workPackageTestCaseExecutionPlan.getEnvironmentList();
			for(Environment environment:environmentList){
				Hibernate.initialize(environment.getEnvironmentCategory());
				Hibernate.initialize(environment.getEnvironmentCategory().getEnvironmentGroup());
			}
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return environmentList;
	}
	
	@Override
	@Transactional
	public int addWorkPackageTestcaseExecutionPlan(
			List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlanList,List<Environment> environments) {
		log.debug("Adding workpackage testcases to execution plan");
		int count = 0;
		WorkPackage workPackage=null;
		try {
			if (workPackageTestCaseExecutionPlanList == null || workPackageTestCaseExecutionPlanList.isEmpty()) {
				return 0;
			}
			for (WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan : workPackageTestCaseExecutionPlanList) {
				sessionFactory.getCurrentSession().save(workPackageTestCaseExecutionPlan);
				
				TestStepExecutionResult testStepExecutionResult=null;
				int i = workPackageTestCaseExecutionPlan.getTestCase().getTestCaseId().intValue();
				List<TestCaseStepsList> listTestCaseSteps=testCaseStepsListDAO.list(i);
				for(TestCaseStepsList testcasestep:listTestCaseSteps){
					testStepExecutionResult = new TestStepExecutionResult();
					testStepExecutionResult.setComments("");
					testStepExecutionResult.setIsApproved(0);
					testStepExecutionResult.setIsReviewed(0);
					testStepExecutionResult.setObservedOutput("");
					testStepExecutionResult.setResult("");
					testStepExecutionResult.setTestCaseExecutionResult(workPackageTestCaseExecutionPlan.getTestCaseExecutionResult());
					testStepExecutionResult.setTestcase(workPackageTestCaseExecutionPlan.getTestCase());
					testStepExecutionResult.setTestSteps(testcasestep);
					sessionFactory.getCurrentSession().save(testStepExecutionResult);
				}
				if(workPackageTestCaseExecutionPlan.getWorkPackage() != null){
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getWorkPackage());
					if(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild() != null){
						Hibernate.initialize(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild());
						if(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion() != null){
							Hibernate.initialize(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion());
						}
					}
				}
				Hibernate.initialize(workPackageTestCaseExecutionPlan.getWorkPackage().getWorkFlowEvent());
				Hibernate.initialize(workPackageTestCaseExecutionPlan.getWorkPackage().getWorkFlowEvent().getWorkFlow());
				Hibernate.initialize(workPackageTestCaseExecutionPlan.getWorkPackage().getEnvironmentList());
				Hibernate.initialize(workPackageTestCaseExecutionPlan.getRunConfiguration());
				Hibernate.initialize(workPackageTestCaseExecutionPlan.getRunConfiguration().getRunconfiguration());
				Hibernate.initialize(workPackageTestCaseExecutionPlan.getRunConfiguration().getWorkpackage());
				Hibernate.initialize(workPackageTestCaseExecutionPlan.getTestSuiteList());

				Set<Environment> environmentSet=getEnvironmentSet(workPackageTestCaseExecutionPlan);
				if(environmentSet!=null && !environmentSet.isEmpty()){
					for(Environment environment:environments){
						for(Environment environemntmapping:environmentSet){
							if(environment.getEnvironmentCategory().getEnvironmentGroup().getEnvironmentGroupId()==environemntmapping.getEnvironmentCategory().getEnvironmentGroup().getEnvironmentGroupId()){
								mapWorkPackageTestCaseExecutionPlanEnv(workPackageTestCaseExecutionPlan,environemntmapping,"Add");
							}else{
								mapWorkPackageTestCaseExecutionPlanEnv(workPackageTestCaseExecutionPlan,environment,"Add");
							}
						}
					}
				}else{
					for(Environment environment:environments){
						mapWorkPackageTestCaseExecutionPlanEnv(workPackageTestCaseExecutionPlan,environment,"Add");
					}
					
				}
				
				count++;
				if(count==workPackageTestCaseExecutionPlanList.size())
					workPackage=workPackageTestCaseExecutionPlan.getWorkPackage();
			}
			if(count!=0){
				Set<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlans = new HashSet<WorkPackageTestCaseExecutionPlan>(workPackageTestCaseExecutionPlanList);
				workPackage.setWorkPackageTestCaseExecutionPlan(workPackageTestCaseExecutionPlans);
				updateWorkPackage(workPackage);
			}
			log.debug("addWorkPackageTestcaseExecutionPlan successfully"+count);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Unable to Add workpackage testcases to execution plan", e);
			return count;
		}

		return count;
	}

	@Override
	@Transactional
	public List<WorkPackageTestCaseExecutionPlan> getWorkPackageTestcaseExecutionPlanByWorkPackageId(
			int workPackageId, int testcaseId,int testSuiteId,int featureId,String sourceType) {
		List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlans = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackageTestCaseExecutionPlan.class, "wpfep");
			
			if (workPackageId !=-1) {
				c.createAlias("wpfep.workPackage", "workPackage");
				c.add(Restrictions.eq("workPackage.workPackageId", workPackageId));
			} 
			if (featureId !=-1) {
				c.createAlias("wpfep.feature", "feature");
				c.add(Restrictions.eq("feature.productFeatureId", featureId));
			} 
			if (testcaseId !=-1) {
				c.createAlias("wpfep.testCase", "testCase");
				c.add(Restrictions.eq("testCase.testCaseId", testcaseId));
			} 
			if (testSuiteId !=-1) {
				c.createAlias("wpfep.testSuiteList", "testSuiteList");
				c.add(Restrictions.eq("testSuiteList.testSuiteId", testSuiteId));
			} 
			c.add(Restrictions.eq("wpfep.sourceType", sourceType));
			
			workPackageTestCaseExecutionPlans=c.list();
			
			
			if (!(workPackageTestCaseExecutionPlans == null || workPackageTestCaseExecutionPlans.isEmpty())) {
				for(WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan : workPackageTestCaseExecutionPlans) {
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getTestCase());
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getWorkPackage());
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getTestSuiteList());
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getFeature());
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getEnvironmentList());
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getTestCaseConfigurationSet());
					if(workPackageTestCaseExecutionPlan.getTestCaseExecutionResult()!=null && workPackageTestCaseExecutionPlan.getTestCaseExecutionResult().getTestStepExecutionResultSet()!=null)
						Hibernate.initialize(workPackageTestCaseExecutionPlan.getTestCaseExecutionResult().getTestStepExecutionResultSet());

					Hibernate.initialize(workPackageTestCaseExecutionPlan.getTester());
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getTestLead());
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getTestCaseExecutionResult());
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getActualWorkShiftMaster());
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getPlannedWorkShiftMaster());
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getExecutionPriority());
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getRunConfiguration());
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getTestSuiteList());
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getTestCase().getTestcaseTypeMaster());
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getTestCase().getTestCasePriority());
					if(workPackageTestCaseExecutionPlan.getWorkPackage() != null){
						Hibernate.initialize(workPackageTestCaseExecutionPlan.getWorkPackage());
						if(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild() != null){
							Hibernate.initialize(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild());
							if(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion() != null){
								Hibernate.initialize(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion());
							}
						}
					}
					if(workPackageTestCaseExecutionPlan.getRunConfiguration()!=null){
						if(workPackageTestCaseExecutionPlan.getRunConfiguration().getRunconfiguration()!=null){
							Hibernate.initialize(workPackageTestCaseExecutionPlan.getRunConfiguration().getRunconfiguration());
						}
						
						Hibernate.initialize(workPackageTestCaseExecutionPlan.getRunConfiguration().getWorkpackage());
					}
				}
			}
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
			re.printStackTrace();
		}
		return workPackageTestCaseExecutionPlans;
	}

	@Override
	@Transactional
	public void deleteWorkPackageTestcaseExecutionPlan(
			List<WorkPackageTestCaseExecutionPlan> WorkPackageTestCaseExecutionPlanList) {
		log.debug("deleting WorkPackageTestCaseExecutionPlan instance");
		try {
			for (WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan : WorkPackageTestCaseExecutionPlanList) {
				Set<Environment> mappedEnvironments=workPackageTestCaseExecutionPlan.getEnvironmentList();
				if(mappedEnvironments.size()!=0){
					mapWorkPackageTestCaseExecutionPlanEnv(workPackageTestCaseExecutionPlan, null,"Remove");
				}
				Set<TestCaseConfiguration> testCaseConfigSet=workPackageTestCaseExecutionPlan.getTestCaseConfigurationSet();
				log.debug("testCaseConfigSet size==>"+testCaseConfigSet.size());
				if(testCaseConfigSet.size()!=0){
				for(TestCaseConfiguration tconfig:testCaseConfigSet){
					int	count = (sessionFactory
							.getCurrentSession()
							.createSQLQuery(
									"delete from testcase_configuration where wptcepId="
											+ workPackageTestCaseExecutionPlan.getId()).executeUpdate());
				}
				}
				TestCaseExecutionResult trcRes=workPackageTestCaseExecutionPlan.getTestCaseExecutionResult();
				if(trcRes!=null && trcRes.getTestStepExecutionResultSet()!=null){
					Set<TestStepExecutionResult> tcstepexecRes=trcRes.getTestStepExecutionResultSet();
					if(tcstepexecRes.size()!=0){
						for(TestStepExecutionResult teststep:tcstepexecRes){
							int	delete = (sessionFactory
									.getCurrentSession()
									.createSQLQuery(
											"delete from teststep_execution_result where tescaseexecutionresultid="
													+ trcRes.getTestCaseExecutionResultId()).executeUpdate());
						}
						
					}
				}
				int	delete = (sessionFactory
						.getCurrentSession()
						.createSQLQuery(
								"delete from testcase_execution_result where testCaseExecutionResultId="
										+ workPackageTestCaseExecutionPlan.getId()).executeUpdate());
				int	count = (sessionFactory
						.getCurrentSession()
						.createSQLQuery(
								"delete from workpackage_testcase_execution_plan where wptcepId="
										+ workPackageTestCaseExecutionPlan.getId()).executeUpdate());
			}	
				
		} catch (Exception re) {
			re.printStackTrace();
			log.error("delete failed", re);
		}

	}
	
	@Override
	@Transactional
	public WorkPackageTestCase getWorkPackageTestCaseById(int workPackageTestCaseId) {
		log.debug("getting WorkPackageTestCase instance by id");
		WorkPackageTestCase workPackageTestCase = null;
		try {

			List list = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from WorkPackageTestCase w where id=:workPackageId")
					.setParameter("workPackageId", workPackageTestCaseId).list();
			workPackageTestCase = (list != null && list.size() != 0) ? (WorkPackageTestCase) list
					.get(0) : null;
			if (workPackageTestCase != null) {
				Hibernate.initialize(workPackageTestCase.getWorkPackage());
				Hibernate.initialize(workPackageTestCase.getTestCase());
				Hibernate.initialize(workPackageTestCase.getTestCase().getDecouplingCategory());
			}
			log.debug("getByProductId successful");
		} catch (RuntimeException re) {
			log.error("getByProductId failed", re);
		}
		return workPackageTestCase;

	}
	
	@Override
	@Transactional
	public void addWorkPackage(WorkPackage workPackage) {
		log.debug("adding WorkPackage instance");
		try {	
			workPackage.setStatus(1);
			sessionFactory.getCurrentSession().save(workPackage);
			log.debug("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}		
	}
	
	@Override
	@Transactional
	public void updateWorkPackage(WorkPackage workPackage) {
		log.debug("updating WorkPackage instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(workPackage);			
		} catch (RuntimeException re) {
			log.error("update failed", re);
		}
	}
	
	@Override
	@Transactional
	public void deleteWorkPackage(WorkPackage workPackage) {
		log.debug("reactivate ProductBuild instance");
		try {
			sessionFactory.getCurrentSession().delete(workPackage);
			log.debug("deletion successful");
		} catch (RuntimeException re) {
			log.error("deletion failed", re);
		}		
	}
	
	@Override
	@Transactional
	public List<WorkPackage> listWorkPackages(int productBuildId) {
		log.debug("listing all WorkPackage instance");
		List<WorkPackage> workPackageList = null;
		try {
			workPackageList = sessionFactory.getCurrentSession().createQuery("from WorkPackage wp where (wp.productBuild.productBuildId=:productBuildId) and isActive=1")
					.setParameter("productBuildId", productBuildId).list();			
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		for (WorkPackage workPackage : workPackageList) {
		Hibernate.initialize(workPackage.getWorkFlowEvent().getWorkFlow());	
		}
		return workPackageList;
	}
	
	@Override
	@Transactional
	public WorkPackage getWorkPackageByProductBuildId(int workPackageId) {
		log.debug("getting WorkPackage instance by id");
		WorkPackage workPackage=null;
		try {
			List list = sessionFactory.getCurrentSession().createQuery("from WorkPackage wp where workPackageId=:workPackageId")
					.setParameter("workPackageId", workPackageId).list();
		
			workPackage = (list!=null && list.size()!=0)?(WorkPackage)list.get(0):null;
			if (workPackage != null) {
				if(workPackage.getRunConfigurationList()!=null){
					Hibernate.initialize(workPackage.getRunConfigurationList());
					Set<RunConfiguration> runConfigSet=workPackage.getRunConfigurationList();
					for(RunConfiguration runConfig:runConfigSet){
						Hibernate.initialize(runConfig);
					}
				}
				if(workPackage.getProductBuild()!=null){
					Hibernate.initialize(workPackage.getProductBuild());
					if(workPackage.getProductBuild().getProductVersion()!=null){
						Hibernate.initialize(workPackage.getProductBuild().getProductVersion());
						if(workPackage.getProductBuild().getProductVersion().getProductMaster()!=null){
							Hibernate.initialize(workPackage.getProductBuild().getProductVersion().getProductMaster());
							if(workPackage.getProductBuild().getProductVersion().getProductMaster().getProductType()!=null){
								Hibernate.initialize(workPackage.getProductBuild().getProductVersion().getProductMaster().getProductType());
							}
						}
					}
					
				}
				if(workPackage.getTestRunJobSet()!=null){
					Hibernate.initialize(workPackage.getTestRunJobSet());
					for(TestRunJob testrunJob:workPackage.getTestRunJobSet()){
						if(testrunJob!=null){
							Hibernate.initialize(testrunJob);
							if(testrunJob.getRunConfiguration()!=null){
								Hibernate.initialize(testrunJob.getRunConfiguration());
								
								
							}
						}
					}
				}
				if(workPackage.getTestSuiteList().size()!=0){
					Hibernate.initialize(workPackage.getTestSuiteList());
					Set<TestSuiteList> tsSet=workPackage.getTestSuiteList();
					for(TestSuiteList ts:tsSet){
						Set<WorkPackageTestCaseExecutionPlan> wptcPlanSet=ts.getWptcePlanSet();
						if(wptcPlanSet.size()!=0){
							Hibernate.initialize(wptcPlanSet);
							for(WorkPackageTestCaseExecutionPlan wptcPlan:wptcPlanSet){
								Hibernate.initialize(wptcPlan.getTestCase());
								Hibernate.initialize(wptcPlan.getTestCaseExecutionResult());
								Hibernate.initialize(wptcPlan.getRunConfiguration());
								Hibernate.initialize(wptcPlan.getRunConfiguration().getRunconfiguration());
							}
						}
						
					
						
					}
				}
				if(workPackage.getTestcaseList().size()!=0){
					Hibernate.initialize(workPackage.getTestcaseList());
					Set<TestCaseList> tclist=  workPackage.getTestcaseList();
					for(TestCaseList tc:tclist){
						Hibernate.initialize(tc);
						Set<WorkPackageTestCaseExecutionPlan> wptcPlanSet=tc.getWorkPackageTestCaseExecutionPlan();
						if(wptcPlanSet.size()!=0){
							Hibernate.initialize(wptcPlanSet);
							for(WorkPackageTestCaseExecutionPlan wptcPlan:wptcPlanSet){
								Hibernate.initialize(wptcPlan.getTestCase());
								Hibernate.initialize(wptcPlan.getTestCaseExecutionResult());
								Hibernate.initialize(wptcPlan.getRunConfiguration());
								Hibernate.initialize(wptcPlan.getRunConfiguration().getRunconfiguration());
							}
						}
						
					
						
					}
				}
				
				if(workPackage.getWorkPackageTestCaseExecutionPlan().size()!=0){
					Hibernate.initialize(workPackage.getWorkPackageTestCaseExecutionPlan());
					Set<WorkPackageTestCaseExecutionPlan> wptcplanSet=workPackage.getWorkPackageTestCaseExecutionPlan();
					for(WorkPackageTestCaseExecutionPlan wptcplan:wptcplanSet){
						Hibernate.initialize(wptcplan);
						Hibernate.initialize(wptcplan.getTestCaseExecutionResult());
						if(wptcplan.getWorkPackage() != null){
							Hibernate.initialize(wptcplan.getWorkPackage());
							if(wptcplan.getWorkPackage().getProductBuild() != null){
								Hibernate.initialize(wptcplan.getWorkPackage().getProductBuild());
								if(wptcplan.getWorkPackage().getProductBuild().getProductVersion() != null){
									Hibernate.initialize(wptcplan.getWorkPackage().getProductBuild().getProductVersion());
								}
							}
						}
					}
				}
				
				if(workPackage.getWorkPackageRunConfigSet().size()!=0){
					Hibernate.initialize(workPackage.getWorkPackageRunConfigSet());
					for(WorkpackageRunConfiguration wprunconfig:workPackage.getWorkPackageRunConfigSet()){
						
						if(wprunconfig.getRunconfiguration()!=null){
							if(wprunconfig.getRunconfiguration().getEnvironmentcombination()!=null){
								Hibernate.initialize(wprunconfig.getRunconfiguration().getEnvironmentcombination());
							}
							Hibernate.initialize(wprunconfig.getRunconfiguration());
							if(wprunconfig.getRunconfiguration().getGenericDevice()!=null){
								Hibernate.initialize(wprunconfig.getRunconfiguration().getGenericDevice());
							}if(wprunconfig.getRunconfiguration().getHostList()!=null){
								Hibernate.initialize(wprunconfig.getRunconfiguration().getHostList());
							}
						}
						if(wprunconfig.getWorkPackageTestCaseExecutionPlan().size()!=0){
							Hibernate.initialize(wprunconfig.getWorkPackageTestCaseExecutionPlan());
							for(WorkPackageTestCaseExecutionPlan wptcexePlan:wprunconfig.getWorkPackageTestCaseExecutionPlan()){
								Hibernate.initialize(wptcexePlan);
								Hibernate.initialize(wptcexePlan.getTestCaseExecutionResult());
								if(wptcexePlan.getWorkPackage() != null){
									Hibernate.initialize(wptcexePlan.getWorkPackage());
									if(wptcexePlan.getWorkPackage().getProductBuild() != null){
										Hibernate.initialize(wptcexePlan.getWorkPackage().getProductBuild());
										if(wptcexePlan.getWorkPackage().getProductBuild().getProductVersion() != null){
											Hibernate.initialize(wptcexePlan.getWorkPackage().getProductBuild().getProductVersion());
										}
									}
								}
								if(wptcexePlan.getTestSuiteList()!=null){
									Hibernate.initialize(wptcexePlan.getTestSuiteList());
									if(wptcexePlan.getTestSuiteList().getTestCaseLists().size()!=0){
										Hibernate.initialize(wptcexePlan.getTestSuiteList().getTestCaseLists());
									}
								}
							}
						}
					}
					
				}
			}
			log.debug("getByProductVersionListId successful");
		} catch (RuntimeException re) {
			log.error("getByProductVersionListId failed", re);
		}
		return workPackage;        
	}
	
	@Override
	@Transactional
	public List<WorkPackage> listWorkPackages(int productBuildId, int status) {
		log.debug("listing specific Workpackage instance");
		if(status == TAFConstants.ENTITY_STATUS_ALL){
			return listWorkPackages(productBuildId);
		}
		List<WorkPackage> workPackage=null;
		try {
			workPackage=sessionFactory.getCurrentSession().createQuery("from WorkPackage wp where wp.productBuild.productBuildId=:BuildId")
														.setParameter("BuildId", productBuildId)
														.list();
			if (!(workPackage == null || workPackage.isEmpty())){
				for (WorkPackage wpackage : workPackage) {
					Hibernate.initialize(wpackage.getProductBuild());					
				
				}
			}
			log.debug("list specific successful");
		} catch (RuntimeException re) {
			log.error("list specific failed", re);
		
		}
		return workPackage;
	}
	
	@Override
	@Transactional
	public List<WorkPackageTestCaseExecutionPlan> listWorkPackageTestCaseExecutionPlan(
			int workPackageId, int jtStartIndex, int jtPageSize,UserList user,String plannedExecutionDate) {
		List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlan = null;
		try {
			
			
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackageTestCaseExecutionPlan.class, "wptcep");
			c.createAlias("wptcep.workPackage", "workPackage");
			c.createAlias("wptcep.testCase", "testCase");
			c.createAlias("wptcep.tester", "tester");
			c.createAlias("wptcep.executionPriority", "executionPriority");
			
			c.add(Restrictions.eq("workPackage.workPackageId", workPackageId));
			c.add(Restrictions.eq("tester.userId", user.getUserId()));
			
			if(plannedExecutionDate!=null && !plannedExecutionDate.equals("")){
				c.add(Restrictions.eq("wptcep.plannedExecutionDate", DateUtility.dateformatWithOutTime(plannedExecutionDate)));
			} 
			
			
			c.addOrder(Order.asc("executionPriority.executionPriorityId"));
			c.addOrder(Order.asc("testCase.testCaseId"));
			c.setFirstResult(jtStartIndex);
			c.setMaxResults(jtPageSize);
			workPackageTestCaseExecutionPlan=c.list();
			if (!(workPackageTestCaseExecutionPlan == null || workPackageTestCaseExecutionPlan
					.isEmpty())) {
				for (WorkPackageTestCaseExecutionPlan dl : workPackageTestCaseExecutionPlan) {
					Hibernate.initialize(dl.getTestCase());
					Hibernate.initialize(dl.getWorkPackage());
					if(dl.getWorkPackage() != null){
						Hibernate.initialize(dl.getWorkPackage());
						if(dl.getWorkPackage().getProductBuild() != null){
							Hibernate.initialize(dl.getWorkPackage().getProductBuild());
							if(dl.getWorkPackage().getProductBuild().getProductVersion() != null){
								Hibernate.initialize(dl.getWorkPackage().getProductBuild().getProductVersion());
							}
						}
					}
					Hibernate.initialize(dl.getEnvironmentList());
					Hibernate.initialize(dl.getTester());
					Hibernate.initialize(dl.getTestLead());
					Hibernate.initialize(dl.getTestCaseExecutionResult());
					Hibernate.initialize(dl.getActualWorkShiftMaster());
					Hibernate.initialize(dl.getPlannedWorkShiftMaster());
					Hibernate.initialize(dl.getExecutionPriority());
					Hibernate.initialize(dl.getRunConfiguration());
					Hibernate.initialize(dl.getTestSuiteList());
					Hibernate.initialize(dl.getTestCase().getTestcaseTypeMaster());
					Hibernate.initialize(dl.getTestCase().getTestCasePriority());
				
					Hibernate.initialize(dl.getRunConfiguration().getRunconfiguration());
					Hibernate.initialize(dl.getRunConfiguration().getWorkpackage());
				}
			}
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return workPackageTestCaseExecutionPlan;
	}

	@Override
	@Transactional
	public List<WorkPackageTestCaseExecutionPlan> listWorkPackageTestCaseExecutionPlan(Map<String, String> searchString,
			int workPackageId, int jtStartIndex, int jtPageSize,String testLeadId,String testerId,String envId,int localeId,String plannedExecutionDate,String dcId,String executionPriority,int status ) {
		List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlan = null;
		
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackageTestCaseExecutionPlan.class, "wptcep");
			c.createAlias("wptcep.workPackage", "workPackage");
			c.createAlias("wptcep.testCase", "testCase");
			
			if (workPackageId !=0) {
				c.add(Restrictions.eq("workPackage.workPackageId", workPackageId));
			} 
			
			List<Integer> testLeadList=CommonUtility.getListFromString(testLeadId);
			if(testLeadList!=null && !testLeadList.isEmpty()){
				c.createAlias("wptcep.testLead", "testLead");
				c.add(Restrictions.in("testLead.userId", testLeadList));
			}
			
			List<Integer> testerList=CommonUtility.getListFromString(testerId);
			if(testerList!=null && !testerList.isEmpty()){
				c.createAlias("wptcep.tester", "tester");
				c.add(Restrictions.in("tester.userId", testerList));
			}
			
			List<Integer> environmentList=CommonUtility.getListFromString(envId);
			if(environmentList!=null && !environmentList.isEmpty()){
				c.createAlias("wptcep.environmentCombination", "environment");
				c.add(Restrictions.in("environment.environment_combination_id", environmentList));
			}
			
			List<Integer> dcList=CommonUtility.getListFromString(dcId);
			if(dcList!=null && !dcList.isEmpty()){
				c.createAlias("testCase.decouplingCategory", "dc");
				c.add(Restrictions.in("dc.decouplingCategoryId", dcList));
			}
			
			List<Integer> epList=CommonUtility.getListFromString(executionPriority);
			if(epList!=null && !epList.isEmpty()){
				c.createAlias("wptcep.executionPriority", "ep");
				c.add(Restrictions.in("ep.executionPriorityId", epList));
			}
			
			if(plannedExecutionDate!=null && !plannedExecutionDate.equals("") && !plannedExecutionDate.equals("undefined")){
				
				c.add(Restrictions.eq("wptcep.plannedExecutionDate", DateUtility.dateformatWithOutTime(plannedExecutionDate)));
			} 
			
			if(searchString.get("searchTestCaseId") != null && searchString.get("searchTestCaseId") !=""){
				c.add(Restrictions.eq("testCase.testCaseId", Integer.parseInt(searchString.get("searchTestCaseId"))));
			}
			if(searchString.get("searchTestCaseName") != null && searchString.get("searchTestCaseName") !=""){
				c.add(Restrictions.ilike("testCase.testCaseName", searchString.get("searchTestCaseName"),MatchMode.ANYWHERE));
			}
			if(searchString.get("searchTestCaseCode") != null && searchString.get("searchTestCaseCode") !=""){
				c.add(Restrictions.ilike("testCase.testCaseCode", searchString.get("searchTestCaseCode"),MatchMode.ANYWHERE));
			}
			if(searchString.get("searchTestSuiteName") != null && searchString.get("searchTestSuiteName") !=""){
				c.createAlias("wptcep.testSuiteList", "testSuiteList");
				c.add(Restrictions.ilike("testSuiteList.testSuiteId", searchString.get("searchTestSuiteName"),MatchMode.ANYWHERE));
			}
			if(searchString.get("searchDescription") != null && searchString.get("searchDescription") !=""){
				c.add(Restrictions.ilike("testCase.testCaseDescription", searchString.get("searchDescription"),MatchMode.ANYWHERE));
			}
			if(searchString.get("searchDCName") != null && searchString.get("searchDCName") !=""){
				c.createAlias("testCase.decouplingCategory", "dc");
				c.add(Restrictions.ilike("dc.decouplingCategoryName", searchString.get("searchDCName"),MatchMode.ANYWHERE));
			}
			
			if(searchString.get("searchFeatureName") != null && searchString.get("searchFeatureName") !=""){
				c.createAlias("testCase.productFeature", "pf");
				c.add(Restrictions.ilike("pf.productFeatureName", searchString.get("searchFeatureName"),MatchMode.ANYWHERE));
			}
			if(searchString.get("searchsourceType") != null && searchString.get("searchsourceType") !=""){
				c.add(Restrictions.ilike("wptcep.sourceType", searchString.get("searchsourceType"),MatchMode.ANYWHERE));
			}
			if(searchString.get("searchECName") != null || searchString.get("searchDeviceName") != null)
			{
				c.createAlias("wptcep.runConfiguration", "wrc");
				c.createAlias("wrc.runconfiguration", "rc");
			}
			if(searchString.get("searchECName") != null && searchString.get("searchECName") !=""){
				c.createAlias("rc.environmentcombination", "ec");
				c.add(Restrictions.ilike("ec.environmentCombinationName", searchString.get("searchECName"),MatchMode.ANYWHERE));
			}
			if(searchString.get("searchDeviceName") != null && searchString.get("searchDeviceName") !=""){
				c.createAlias("rc.genericDevice", "gd");
				c.add(Restrictions.ilike("gd.name", searchString.get("searchDeviceName"),MatchMode.ANYWHERE));
			}
			if(searchString.get("searchTestLeadName") != null && searchString.get("searchTestLeadName") !=""){
				c.createAlias("wptcep.testLead", "tl");
				c.add(Restrictions.ilike("tl.loginId", searchString.get("searchTestLeadName"),MatchMode.ANYWHERE));
			}
			
			if(searchString.get("searchTesterName") != null && searchString.get("searchTesterName") !=""){
				c.createAlias("wptcep.tester", "t");
				c.add(Restrictions.ilike("t.loginId", searchString.get("searchTesterName"),MatchMode.ANYWHERE));
			}
			if(searchString.get("searchPED") !=null && !searchString.get("searchPED").equals("") && !searchString.get("searchPED").equals("undefined")){
				c.add(Restrictions.eq("wptcep.plannedExecutionDate", DateUtility.dateformatWithOutTime(searchString.get("searchPED"))));
			} 
			if(searchString.get("searchPlannedShift") != null && searchString.get("searchPlannedShift") !=""){
				c.createAlias("wptcep.plannedWorkShiftMaster", "pws");
				c.add(Restrictions.ilike("pws.shiftName", searchString.get("searchPlannedShift"),MatchMode.ANYWHERE));
			}
			if(searchString.get("searchExecutionStatus") != null && searchString.get("searchExecutionStatus") !="" && !searchString.get("searchExecutionStatus").equals("0")){
				c.add(Restrictions.eq("wptcep.executionStatus", Integer.parseInt(searchString.get("searchExecutionStatus"))));
			}
			
			if(status!=2)
				c.add(Restrictions.eq("status", status));
			
			workPackageTestCaseExecutionPlan = c.setFirstResult(jtStartIndex).setMaxResults(jtPageSize).list();
			if (!(workPackageTestCaseExecutionPlan == null || workPackageTestCaseExecutionPlan
					.isEmpty())) {
				for (WorkPackageTestCaseExecutionPlan dl : workPackageTestCaseExecutionPlan) {
					Hibernate.initialize(dl.getTestCase());
					Hibernate.initialize(dl.getHostList());
					Hibernate.initialize(dl.getTestCase().getDecouplingCategory());
					Hibernate.initialize(dl.getWorkPackage());
					if(dl.getEnvironmentList()!=null || dl.getEnvironmentList().size()!=0){
						Hibernate.initialize(dl.getEnvironmentList());
					}
					Hibernate.initialize(dl.getTestCase().getTestcaseTypeMaster());
					Hibernate.initialize(dl.getTestCase().getTestCasePriority());
					Hibernate.initialize(dl.getTestCase().getProductFeature());
					Hibernate.initialize(dl.getTester());
					Hibernate.initialize(dl.getTestLead());
					Hibernate.initialize(dl.getTestCaseExecutionResult());
					Hibernate.initialize(dl.getActualWorkShiftMaster());
					Hibernate.initialize(dl.getPlannedWorkShiftMaster());
					Hibernate.initialize(dl.getExecutionPriority());
					Hibernate.initialize(dl.getFeature());
					Hibernate.initialize(dl.getWorkPackage().getProductBuild().getProductVersion());
					Hibernate.initialize(dl.getWorkPackage().getProductBuild().getProductVersion().getProductMaster());
					Hibernate.initialize(dl.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductMode());
					if(dl.getRunConfiguration()!=null){
						if(dl.getRunConfiguration().getRunconfiguration()!=null){
							Hibernate.initialize(dl.getRunConfiguration().getRunconfiguration());
							if(dl.getRunConfiguration().getRunconfiguration().getEnvironmentcombination()!=null)
								Hibernate.initialize(dl.getRunConfiguration().getRunconfiguration().getEnvironmentcombination());
							if(dl.getRunConfiguration().getRunconfiguration().getGenericDevice()!=null)
								Hibernate.initialize(dl.getRunConfiguration().getRunconfiguration().getGenericDevice());
						}
						Hibernate.initialize(dl.getRunConfiguration().getWorkpackage());
						Hibernate.initialize(dl.getRunConfiguration());
					}
					Hibernate.initialize(dl.getTestSuiteList());
				}
			}
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return workPackageTestCaseExecutionPlan;
	}
	
	@Override
	@Transactional
	public List<JsonWorkPackageTestCaseExecutionPlan> listJsonWorkPackageTestCaseExecutionPlan(Map<String, String> searchString,
			int workPackageId, int jtStartIndex, int jtPageSize,String testLeadId,String testerId,String envId,int localeId,String plannedExecutionDate,String dcId,String executionPriority,int status ) {
		List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlan = null;
		List<JsonWorkPackageTestCaseExecutionPlan> jsonWorkPackageTestCaseExecutionPlan=new ArrayList<JsonWorkPackageTestCaseExecutionPlan>(); 
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackageTestCaseExecutionPlan.class, "wptcep");
			c.createAlias("wptcep.workPackage", "workPackage");
			c.createAlias("wptcep.testCase", "testCase");
			
			if (workPackageId !=0) {
				c.add(Restrictions.eq("workPackage.workPackageId", workPackageId));
			} 
			
			List<Integer> testLeadList=CommonUtility.getListFromString(testLeadId);
			if(testLeadList!=null && !testLeadList.isEmpty()){
				c.createAlias("wptcep.testLead", "testLead");
				c.add(Restrictions.in("testLead.userId", testLeadList));
			}
			
			List<Integer> testerList=CommonUtility.getListFromString(testerId);
			if(testerList!=null && !testerList.isEmpty()){
				c.createAlias("wptcep.tester", "tester");
				c.add(Restrictions.in("tester.userId", testerList));
			}
			
			List<Integer> environmentList=CommonUtility.getListFromString(envId);
			if(environmentList!=null && !environmentList.isEmpty()){
				c.createAlias("wptcep.environmentCombination", "environment");
				c.add(Restrictions.in("environment.environment_combination_id", environmentList));
			}
			
			List<Integer> dcList=CommonUtility.getListFromString(dcId);
			if(dcList!=null && !dcList.isEmpty()){
				c.createAlias("testCase.decouplingCategory", "dc");
				c.add(Restrictions.in("dc.decouplingCategoryId", dcList));
			}
			
			List<Integer> epList=CommonUtility.getListFromString(executionPriority);
			if(epList!=null && !epList.isEmpty()){
				c.createAlias("wptcep.executionPriority", "ep");
				c.add(Restrictions.in("ep.executionPriorityId", epList));
			}
			
			if(plannedExecutionDate!=null && !plannedExecutionDate.equals("") && !plannedExecutionDate.equals("undefined")){
				
				c.add(Restrictions.eq("wptcep.plannedExecutionDate", DateUtility.dateformatWithOutTime(plannedExecutionDate)));
			} 
			
			if(searchString.get("searchTestCaseId") != null && searchString.get("searchTestCaseId") !=""){
				c.add(Restrictions.eq("testCase.testCaseId", Integer.parseInt(searchString.get("searchTestCaseId"))));
			}
			if(searchString.get("searchTestCaseName") != null && searchString.get("searchTestCaseName") !=""){
				c.add(Restrictions.ilike("testCase.testCaseName", searchString.get("searchTestCaseName"),MatchMode.ANYWHERE));
			}
			if(searchString.get("searchTestCaseCode") != null && searchString.get("searchTestCaseCode") !=""){
				c.add(Restrictions.ilike("testCase.testCaseCode", searchString.get("searchTestCaseCode"),MatchMode.ANYWHERE));
			}
			if(searchString.get("searchTestSuiteName") != null && searchString.get("searchTestSuiteName") !=""){
				c.createAlias("wptcep.testSuiteList", "testSuiteList");
				c.add(Restrictions.ilike("testSuiteList.testSuiteId", searchString.get("searchTestSuiteName"),MatchMode.ANYWHERE));
			}
			if(searchString.get("searchDescription") != null && searchString.get("searchDescription") !=""){
				c.add(Restrictions.ilike("testCase.testCaseDescription", searchString.get("searchDescription"),MatchMode.ANYWHERE));
			}
			if(searchString.get("searchDCName") != null && searchString.get("searchDCName") !=""){
				c.createAlias("testCase.decouplingCategory", "dc");
				c.add(Restrictions.ilike("dc.decouplingCategoryName", searchString.get("searchDCName"),MatchMode.ANYWHERE));
			}
			
			if(searchString.get("searchFeatureName") != null && searchString.get("searchFeatureName") !=""){
				c.createAlias("testCase.productFeature", "pf");
				c.add(Restrictions.ilike("pf.productFeatureName", searchString.get("searchFeatureName"),MatchMode.ANYWHERE));
			}
			if(searchString.get("searchsourceType") != null && searchString.get("searchsourceType") !=""){
				c.add(Restrictions.ilike("wptcep.sourceType", searchString.get("searchsourceType"),MatchMode.ANYWHERE));
			}
			if(searchString.get("searchECName") != null || searchString.get("searchDeviceName") != null)
			{
				c.createAlias("wptcep.runConfiguration", "wrc");
				c.createAlias("wrc.runconfiguration", "rc");
			}
			if(searchString.get("searchECName") != null && searchString.get("searchECName") !=""){
				c.createAlias("rc.environmentcombination", "ec");
				c.add(Restrictions.ilike("ec.environmentCombinationName", searchString.get("searchECName"),MatchMode.ANYWHERE));
			}
			if(searchString.get("searchDeviceName") != null && searchString.get("searchDeviceName") !=""){
				c.createAlias("rc.genericDevice", "gd");
				c.add(Restrictions.ilike("gd.name", searchString.get("searchDeviceName"),MatchMode.ANYWHERE));
			}
			if(searchString.get("searchTestLeadName") != null && searchString.get("searchTestLeadName") !=""){
				c.createAlias("wptcep.testLead", "tl");
				c.add(Restrictions.ilike("tl.loginId", searchString.get("searchTestLeadName"),MatchMode.ANYWHERE));
			}
			
			if(searchString.get("searchTesterName") != null && searchString.get("searchTesterName") !=""){
				c.createAlias("wptcep.tester", "t");
				c.add(Restrictions.ilike("t.loginId", searchString.get("searchTesterName"),MatchMode.ANYWHERE));
			}
			if(searchString.get("searchPED") !=null && !searchString.get("searchPED").equals("") && !searchString.get("searchPED").equals("undefined")){
				c.add(Restrictions.eq("wptcep.plannedExecutionDate", DateUtility.dateformatWithOutTime(searchString.get("searchPED"))));
			} 
			if(searchString.get("searchPlannedShift") != null && searchString.get("searchPlannedShift") !=""){
				c.createAlias("wptcep.plannedWorkShiftMaster", "pws");
				c.add(Restrictions.ilike("pws.shiftName", searchString.get("searchPlannedShift"),MatchMode.ANYWHERE));
			}
			if(searchString.get("searchExecutionStatus") != null && searchString.get("searchExecutionStatus") !="" && !searchString.get("searchExecutionStatus").equals("0")){
				c.add(Restrictions.eq("wptcep.executionStatus", Integer.parseInt(searchString.get("searchExecutionStatus"))));
			}
			
			if(status!=2)
				c.add(Restrictions.eq("status", status));
			
			workPackageTestCaseExecutionPlan = c.setFirstResult(jtStartIndex).setMaxResults(jtPageSize).list();
			if (!(workPackageTestCaseExecutionPlan == null || workPackageTestCaseExecutionPlan
					.isEmpty())) {
				for (WorkPackageTestCaseExecutionPlan dl : workPackageTestCaseExecutionPlan) {
					Hibernate.initialize(dl.getTestCase());
					Hibernate.initialize(dl.getHostList());
					Hibernate.initialize(dl.getTestCase().getDecouplingCategory());
					Hibernate.initialize(dl.getWorkPackage());
					if(dl.getEnvironmentList()!=null || dl.getEnvironmentList().size()!=0){
						Hibernate.initialize(dl.getEnvironmentList());
					}
					Hibernate.initialize(dl.getTestCase().getTestcaseTypeMaster());
					Hibernate.initialize(dl.getTestCase().getTestCasePriority());
					Hibernate.initialize(dl.getTestCase().getProductFeature());
					Hibernate.initialize(dl.getTester());
					Hibernate.initialize(dl.getTestLead());
					Hibernate.initialize(dl.getTestCaseExecutionResult());
					Hibernate.initialize(dl.getActualWorkShiftMaster());
					Hibernate.initialize(dl.getPlannedWorkShiftMaster());
					Hibernate.initialize(dl.getExecutionPriority());
					Hibernate.initialize(dl.getFeature());
					Hibernate.initialize(dl.getWorkPackage().getProductBuild().getProductVersion());
					Hibernate.initialize(dl.getWorkPackage().getProductBuild().getProductVersion().getProductMaster());
					Hibernate.initialize(dl.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductMode());
					if(dl.getRunConfiguration()!=null){
						if(dl.getRunConfiguration().getRunconfiguration()!=null){
							Hibernate.initialize(dl.getRunConfiguration().getRunconfiguration());
							if(dl.getRunConfiguration().getRunconfiguration().getEnvironmentcombination()!=null)
								Hibernate.initialize(dl.getRunConfiguration().getRunconfiguration().getEnvironmentcombination());
							if(dl.getRunConfiguration().getRunconfiguration().getGenericDevice()!=null)
								Hibernate.initialize(dl.getRunConfiguration().getRunconfiguration().getGenericDevice());
						}
						Hibernate.initialize(dl.getRunConfiguration().getWorkpackage());
						Hibernate.initialize(dl.getRunConfiguration());
					}
					Hibernate.initialize(dl.getTestSuiteList());
					jsonWorkPackageTestCaseExecutionPlan.add(new JsonWorkPackageTestCaseExecutionPlan(dl));
				}
			}
		} catch (RuntimeException re) {
			log.error("list failed", re);
			// throw re;
		}
		return jsonWorkPackageTestCaseExecutionPlan;
	}
	
	@Override
	@Transactional
	public List<Environment> getWorkPackageTestCasesExecutionPlanEnvironments(int workPackageId, int testCaseId) {
		
		log.debug("listing getWorkPackageTestCasesExecutionPlanEnvironments");
		List<Environment> environments = null;
		try {
			environments = sessionFactory.getCurrentSession().createQuery("select distinct e"
			+ " from Environment as e, WorkPackageTestCaseExecutionPlan as wptcep"
			+ " where e.environmentId = wptcep.environment.environmentId"
			+ " and wptcep.workPackage.workPackageId = :workPackageId and wptcep.testCase.testCaseId = :testCaseId"
			+ " order by e.environmentId asc")
			.setParameter("workPackageId", workPackageId)
			.setParameter("testCaseId", testCaseId)
			.list();
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return environments;
	}
	
	
	@Override
	@Transactional
	public int totalRecordsCountForWorkPackageTestCaseExecutionPlan(
			int workPackageId,UserList user) {
		log.debug("getting totalRecordsCountForWorkPackageTestCasesExecutionPlan");
		int count = 0;
		List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlan=null;
		try {
		
			workPackageTestCaseExecutionPlan = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from WorkPackageTestCaseExecutionPlan wptcep where wptcep.workPackage.workPackageId = "
									+ workPackageId + "and wptcep.testLead.userId = "+user.getUserId()
									+ " order by wptcep.testCase.testCaseId asc").list();
			if(workPackageTestCaseExecutionPlan!=null && !workPackageTestCaseExecutionPlan.isEmpty()){
				count=workPackageTestCaseExecutionPlan.size();
			}
			log.debug("total records fetch successful");
		} catch (RuntimeException re) {
			log.error("total records fetch failed", re);
			// throw re;
		}
		return count;
	}

	@Override
	@Transactional
	public List<UserList> userListByRole(String role) {
		List<UserList> testerList = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(UserList.class, "user");
			c.createAlias("user.userRoleMaster", "userRole");
			if(role.equals("All")){
				c.createAlias("user.resourcePool", "pool");
			}else if(role.equals("ResourceandTestManager")){
				c.add(Restrictions.in("userRole.userRoleId",  Arrays.asList(3,7)));
			}else{
			c.add(Restrictions.eq("userRole.userRoleId", Integer.valueOf(role)));
			}

		    testerList = c.list();
			for (UserList tester : testerList) {
				Hibernate.initialize(tester.getResourcePool());
				Hibernate.initialize(tester.getUserRoleMaster());
				Hibernate.initialize(tester.getUserTypeMasterNew());
				Hibernate.initialize(tester.getCommonActiveStatusMaster());
				Hibernate.initialize(tester.getVendor());	
				Hibernate.initialize(tester.getUserSkills());
				Hibernate.initialize(tester.getAuthenticationType());
				Hibernate.initialize(tester.getCustomer());
				}
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return testerList;
	}

	@Override
	@Transactional
	public List<ProductUserRole> userListByProductRole(Integer productId,String userRoleId) {
		List<ProductUserRole> productUserRole = null;
		try {
			if(userRoleId!=null  && userRoleId.equalsIgnoreCase("All")){
				productUserRole = sessionFactory
						.getCurrentSession()
						.createQuery(
								"from ProductUserRole t where t.product.productId=:productId ")
						.setParameter("productId", productId).list();
			}else{
				productUserRole = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from ProductUserRole t where t.product.productId=:productId and t.role.userRoleId=:userRoleId")
					.setParameter("productId", productId).setParameter("userRoleId", Integer.parseInt(userRoleId)).list();
			}
			if(productUserRole!=null && !productUserRole.isEmpty()){
				for (ProductUserRole dl : productUserRole) {
					Hibernate.initialize(dl.getUser());
				}
			}
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return productUserRole;
	}
	
	@Override
	@Transactional
	public int getTotalRecordsOfUserByRole(String role) {
		log.debug("getting getTotalRecordsOfUserByRole");
		int count = 0;
		try {
			count = ((Number) sessionFactory
					.getCurrentSession()
					.createSQLQuery(
							"select count(*) from user_list where  userType='"
									+ role+"'").uniqueResult()).intValue();

			log.debug("total records fetch successful");
		} catch (RuntimeException re) {
			log.error("total records fetch failed", re);
		}
		return count;
	}

	@Override
	@Transactional
	public void updateWorkPackageTestCaseExecutionPlan(
			WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlanFromUI) {
		log.debug("updating workPackageTestCaseExecutionPlanFromUI instance");
		try {
			workPackageTestCaseExecutionPlanFromUI.setModifiedDate(DateUtility.getCurrentTime());
			sessionFactory.getCurrentSession().saveOrUpdate(workPackageTestCaseExecutionPlanFromUI);
			if(workPackageTestCaseExecutionPlanFromUI.getTestCaseExecutionResult()!=null){				
				sessionFactory.getCurrentSession().saveOrUpdate(workPackageTestCaseExecutionPlanFromUI.getTestCaseExecutionResult());
			}
			
			//Persist MongoDB records for save or updating Test Case Execution Result records for Dashboard display
			if(workPackageTestCaseExecutionPlanFromUI != null && workPackageTestCaseExecutionPlanFromUI.getTestCaseExecutionResult() != null && workPackageTestCaseExecutionPlanFromUI.getTestCaseExecutionResult().getTestCaseExecutionResultId() != null){
				mongoDBService.addTestCaseExecutionResult(workPackageTestCaseExecutionPlanFromUI.getTestCaseExecutionResult().getTestCaseExecutionResultId());				
			}
			
			log.debug("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
		}
	}

	@Override
	@Transactional
	public Environment getEnvironmentById(int environmentId) {
		log.debug("getting Environment instance by id");
		Environment environment = null;
		try {

			List list = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from Environment w where environmentId=:environmentId")
					.setParameter("environmentId", environmentId).list();
			environment = (list != null && list.size() != 0) ? (Environment) list
					.get(0) : null;
			if (environment != null) {
				Hibernate.initialize(environment.getProductMaster());
				Hibernate.initialize(environment.getEnvironmentCategory());
				Hibernate.initialize(environment.getEnvironmentCategory().getEnvironmentGroup());
			}
			log.debug("getByProductId successful");
		} catch (RuntimeException re) {
			log.error("getByProductId failed", re);
		}
		return environment;
	}

	@Override
	@Transactional
	public List<WorkPackageTestCaseExecutionPlan> listWorkPackageTestCasesExecutionPlanByWorkPackageId(
			int workPackageId) {
		List<WorkPackageTestCaseExecutionPlan> listWorkPackageTestCaseExecutionPlan = null;
		try {
			listWorkPackageTestCaseExecutionPlan =sessionFactory
					.getCurrentSession()
					.createQuery(
							"from WorkPackageTestCaseExecutionPlan wptcep where wptcep.workPackage.workPackageId = "
									+ workPackageId
									+ "order by wptcep.testCase.testCaseId  asc").list();
			if (!(listWorkPackageTestCaseExecutionPlan == null || listWorkPackageTestCaseExecutionPlan.isEmpty())) {

				for (WorkPackageTestCaseExecutionPlan dl : listWorkPackageTestCaseExecutionPlan) {
					Hibernate.initialize(dl.getTestCase());
					Hibernate.initialize(dl.getWorkPackage());
					Hibernate.initialize(dl.getTester());
					Hibernate.initialize(dl.getTestLead());
					Hibernate.initialize(dl.getExecutionPriority());
					Hibernate.initialize(dl.getEnvironmentList());
					Hibernate.initialize(dl.getActualWorkShiftMaster());
					Hibernate.initialize(dl.getPlannedWorkShiftMaster());
					Hibernate.initialize(dl.getTestCase().getDecouplingCategory());
					Hibernate.initialize(dl.getExecutionPriority());
					Hibernate.initialize(dl.getTestCaseExecutionResult());
					Hibernate.initialize(dl.getTestSuiteList());
					Hibernate.initialize(dl.getRunConfiguration());
					Hibernate.initialize(dl.getRunConfiguration().getRunconfiguration());
					Hibernate.initialize(dl.getRunConfiguration().getWorkpackage());
					Hibernate.initialize(dl.getTestCase().getTestcaseTypeMaster());
					Hibernate.initialize(dl.getTestCase().getTestCasePriority());
					if(dl.getWorkPackage() != null){
						Hibernate.initialize(dl.getWorkPackage());
						if(dl.getWorkPackage().getProductBuild() != null){
							Hibernate.initialize(dl.getWorkPackage().getProductBuild());
							if(dl.getWorkPackage().getProductBuild().getProductVersion() != null){
								Hibernate.initialize(dl.getWorkPackage().getProductBuild().getProductVersion());
							}
						}
					}
				
				}
			}
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return listWorkPackageTestCaseExecutionPlan;
	}

	@Override
	@Transactional
	public List<WorkPackageTestCaseExecutionPlan> listWorkPackageTestCaseExecutionPlanByTester(
			int testerId, int workPackageId, int jtStartIndex, int jtPageSize) {
		List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlanList = null;
		try {
			workPackageTestCaseExecutionPlanList = sessionFactory
					.getCurrentSession()
					.createQuery("from WorkPackageTestCaseExecutionPlan wptcep "
							+ "where "
							+ "wptcep.tester.userId = "	+ testerId
							+ " and "
							+ "wptcep.workPackage.workPackageId = "	+ workPackageId
							+ " order by wptcep.testCase.testCaseId asc")
					.setFirstResult(jtStartIndex).setMaxResults(jtPageSize).list();
			if (!(workPackageTestCaseExecutionPlanList == null || workPackageTestCaseExecutionPlanList.isEmpty())) {
				for (WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan : workPackageTestCaseExecutionPlanList) {
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getTestCase());
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getWorkPackage());
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getTester());
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getTestLead());
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getExecutionPriority());
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getTestSuiteList());

					Hibernate.initialize(workPackageTestCaseExecutionPlan.getRunConfiguration());
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getRunConfiguration().getRunconfiguration());
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getRunConfiguration().getWorkpackage());
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getTestCase().getTestcaseTypeMaster());
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getTestCase().getTestCasePriority());
					if(workPackageTestCaseExecutionPlan.getWorkPackage() != null){
						Hibernate.initialize(workPackageTestCaseExecutionPlan.getWorkPackage());
						if(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild() != null){
							Hibernate.initialize(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild());
							if(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion() != null){
								Hibernate.initialize(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion());
							}
						}
					}
				
				}
			}
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return workPackageTestCaseExecutionPlanList;
	}
	
	@Override
	@Transactional
	public int totalRecordsCountForWorkPackageTestCaseExecutionPlanByTesterId(
			int testerId, int workPackageId) {
		log.debug("getting totalRecordsCountForWorkPackageTestCasesExecutionPlan");
		int count = 0;
		try {
			count = ((Number) sessionFactory
					.getCurrentSession()
					.createSQLQuery(
							"select count(*) from workpackage_testcase_execution_plan wptcep where workPackageId="
									+ workPackageId).uniqueResult()).intValue();

			log.debug("total records fetch successful");
		} catch (RuntimeException re) {
			log.error("total records fetch failed", re);
		}
		return count;
	}

	
	@Override
	@Transactional
	public List<WorkPackageDemandProjectionDTO> listWorkPackageDemandProjection(int workPackageId, Integer workShiftId, Date startDate,Date endDate) {
		log.debug("listing listWorkPackageDemandProjection - ByTester instance   workShiftId: "+workShiftId);
		List<WorkPackageDemandProjectionDTO> weeklyWorkPackageDemandProjectionDTO = new ArrayList<WorkPackageDemandProjectionDTO>();
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackageDemandProjection.class,"workPackageDemand");
			c.createAlias("workPackageDemand.workPackage", "wp");
			c.createAlias("wp.productBuild", "pbuild");
			c.createAlias("pbuild.productVersion", "pVersion");
			c.createAlias("pVersion.productMaster", "product");
			c.createAlias("workPackageDemand.workShiftMaster", "workShift");
			c.add(Restrictions.between("workDate", startDate,  endDate));
			c.add(Restrictions.eq("wp.workPackageId", workPackageId));
			if (workShiftId != null) {
				c.add(Restrictions.eq("workShift.shiftId", workShiftId.intValue()));
			}
			ProjectionList projectionList = Projections.projectionList();
			projectionList = Projections.projectionList();
			projectionList.add(Property.forName("wp.workPackageId").as("workPackageId"));
			projectionList.add(Property.forName("wp.name").as("workPackageName"));
			projectionList.add(Property.forName("workDate").as("workDate"));
			projectionList.add(Property.forName("workShift.shiftId").as("shiftId"));
			projectionList.add(Property.forName("workShift.shiftName").as("shiftName"));
			projectionList.add(Property.forName("product.productId").as("productId"));
			projectionList.add(Property.forName("product.productName").as("productName"));
			projectionList.add(Projections.sum("resourceCount").as("totalResourceCount"));
			projectionList.add(Projections.groupProperty("workDate"));
			projectionList.add(Projections.groupProperty("workShift.shiftId"));
			c.setProjection(projectionList);

			List<Object[]> list = c.list();
			WorkPackageDemandProjectionDTO wpDemandProjectionDTO = null;
				for (Object[] row : list) {
					wpDemandProjectionDTO = new WorkPackageDemandProjectionDTO();
					wpDemandProjectionDTO.setWorkPackageId((Integer)row[0]);
					wpDemandProjectionDTO.setWorkPackageName((String)row[1]);
					wpDemandProjectionDTO.setWorkDate((Date)row[2]);
					wpDemandProjectionDTO.setShiftId((Integer)(row[3]));
					wpDemandProjectionDTO.setShiftName((String)row[4]);
					wpDemandProjectionDTO.setProductId((Integer)(row[5]));
					wpDemandProjectionDTO.setProductName((String)row[6]);
					wpDemandProjectionDTO.setResourceCount(((Double)row[7]).floatValue());
					weeklyWorkPackageDemandProjectionDTO.add(wpDemandProjectionDTO);
					
					log.debug("WorkPackage Demand Projection for Work Package Id : " + (Integer)row[0] + " WP Name:"+(String)row[1]+"  Date : " + (Date)row[2]+ " shift Id: " + (Integer)(row[3])+ " shift Name:"+(String)row[4]+ " Resource count: " + ((Double)row[7]).floatValue());
				}
			} catch (RuntimeException re) {
				log.error("list failed", re);
			}
			return weeklyWorkPackageDemandProjectionDTO;
	}
	
	
	@Override
	@Transactional
	public int totalRecordsCountForWorkPackageDemandProjection(
			int workPackageId) {
		log.debug("getting totalRecordsCountForWorkPackageDemandProjection");
		int count = 0;
		try {
			count = ((Number) sessionFactory
					.getCurrentSession()
					.createSQLQuery(
							"select count(*) from workpackage_demand_projection wptcep where workPackageId="
									+ workPackageId).uniqueResult()).intValue();

			log.debug("total records fetch successful");
		} catch (RuntimeException re) {
			log.error("total records fetch failed", re);
		}
		return count;
	}

	@Override
	@Transactional
	public List<WorkPackageDemandProjection> updateWorkPackageDemandProjection(List<WorkPackageDemandProjection> workPackageDemandProjections) {
		List<WorkPackageDemandProjection> updatedWorkPackageDemandProjections = new ArrayList<WorkPackageDemandProjection>();
		try {
			if (workPackageDemandProjections == null || workPackageDemandProjections.isEmpty()) {
				return null;
			}

			for (WorkPackageDemandProjection workPackageDemandProjection : workPackageDemandProjections) {

				WorkPackageDemandProjection workPackageDemandProjectionFromDB = null;
				Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackageDemandProjection.class);
				c.add(Restrictions.eq("workPackage.workPackageId", workPackageDemandProjection.getWorkPackage().getWorkPackageId()));
				c.add(Restrictions.eq("workDate", workPackageDemandProjection.getWorkDate()));
				c.add(Restrictions.eq("workShiftMaster.shiftId", workPackageDemandProjection.getWorkShiftMaster().getShiftId()));
				
				Date demandDate = workPackageDemandProjection.getWorkDate();
				List list = sessionFactory.getCurrentSession().createQuery("from WorkPackageDemandProjection demand where "
						+ " year(demand.workDate) = :year"
						+ " and month(demand.workDate) = :month"
						+ " and day(demand.workDate) = :day"
						+ " and demand.workPackage.workPackageId = :workPackageId"
						+ " and demand.workShiftMaster.shiftId = :shiftId")
						.setParameter("year", DateUtility.getYearOfDate(demandDate))
						.setParameter("month", DateUtility.getMonthOfDate(demandDate))
						.setParameter("day", DateUtility.getDateOfDate(demandDate))
						.setParameter("workPackageId", workPackageDemandProjection.getWorkPackage().getWorkPackageId().intValue())
						.setParameter("shiftId", workPackageDemandProjection.getWorkShiftMaster().getShiftId().intValue())
						.list();
				
				
				Float resourceCount = 0f;
				if ((workPackageDemandProjection.getResourceCount() == null) || (workPackageDemandProjection.getResourceCount().intValue() == 0)) {
					resourceCount = 0f;
				} else {
					resourceCount = workPackageDemandProjection.getResourceCount();
				}
				
				log.debug("Search Date : " +  DateUtility.getDateOfDate(demandDate) + "-" + DateUtility.getMonthOfDate(demandDate) + "-"+ DateUtility.getYearOfDate(demandDate));
				log.debug("Resource Count : " + resourceCount);
				log.debug("List count : " + list.size() + " : List is empty : " + list.isEmpty());
				if (list.isEmpty()) {
					workPackageDemandProjectionFromDB = null;
				} else {
					workPackageDemandProjectionFromDB = (WorkPackageDemandProjection)list.get(0);
				}
				if (workPackageDemandProjectionFromDB == null) {
					if (resourceCount <= 0) {
					} else {
						sessionFactory.getCurrentSession().save(workPackageDemandProjection);
						updatedWorkPackageDemandProjections.add(workPackageDemandProjection);
					}
				} else {
					workPackageDemandProjectionFromDB.setResourceCount(resourceCount);
					sessionFactory.getCurrentSession().update(workPackageDemandProjectionFromDB);
					updatedWorkPackageDemandProjections.add(workPackageDemandProjectionFromDB);
				}
			}
			log.debug("Updated workpackage demand projection successfully");
		} catch (Exception e) {
			log.error("update failed", e);
			return null;
		}
		return updatedWorkPackageDemandProjections;
	}
	
	@Override
	public TestCaseExecutionResult getTestCaseExecutionResultByID(
			int testCaseExecutionResultId) {
		log.debug("getting TCER instance by id");
		TestCaseExecutionResult testCaseExecutionResult = null;
		try {

			List list = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from TestCaseExecutionResult ter where testCaseExecutionResultId=:testCaseExecutionResultId")
					.setParameter("testCaseExecutionResultId", testCaseExecutionResultId).list();
			testCaseExecutionResult = (list != null && list.size() != 0) ? (TestCaseExecutionResult) list
					.get(0) : null;
					if(testCaseExecutionResult!=null){
						Hibernate.initialize(testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan());
						Hibernate.initialize(testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getWorkPackage());
						if(testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getWorkPackage() != null){
							if(testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getWorkPackage().getTestRunPlan() != null){
								Hibernate.initialize(testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getWorkPackage().getTestRunPlan());
								if(testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getWorkPackage().getTestRunPlan() != null){
									Hibernate.initialize(testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getWorkPackage().getTestRunPlan().getAttachments());
									if( testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getWorkPackage().getTestRunPlan().getAttachments() != null &&  testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getWorkPackage().getTestRunPlan().getAttachments().size()>0){
										Set<Attachment> attachmentSet= testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getWorkPackage().getTestRunPlan().getAttachments();
										Hibernate.initialize(attachmentSet);
										for(Attachment attach: attachmentSet){
											Hibernate.initialize(attach);
										}
									}
								}
							}
							if(testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getWorkPackage().getProductBuild() != null){
								Hibernate.initialize(testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getWorkPackage().getProductBuild());
								if(testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getWorkPackage().getProductBuild().getProductVersion() != null){
									Hibernate.initialize(testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getWorkPackage().getProductBuild().getProductVersion());
								}
							}
						}
						Hibernate.initialize(testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getWorkPackage().getProductBuild().getProductVersion().getProductMaster());
						if(testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getWorkPackage().getWorkFlowEvent() != null){
							Hibernate.initialize(testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getWorkPackage().getWorkFlowEvent());
							if(testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getWorkPackage().getWorkFlowEvent().getWorkFlow() != null){
								Hibernate.initialize(testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getWorkPackage().getWorkFlowEvent().getWorkFlow());
								if(testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getWorkPackage().getWorkFlowEvent().getWorkFlow().getEntityMaster() !=null){
									Hibernate.initialize(testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getWorkPackage().getWorkFlowEvent().getWorkFlow().getEntityMaster());
								}
							}
						}
						
						Hibernate.initialize(testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getEnvironmentList());
						Hibernate.initialize(testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getTestSuiteList());
                       if(testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getRunConfiguration()!=null){
	                   Hibernate.initialize(testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getRunConfiguration());
	                    if(testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration()!=null){
		              Hibernate.initialize(testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration());
	                 }
                }
                       if(testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getTester() != null){
                    	   Hibernate.initialize(testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getTester().getLoginId());   
                       }                       
                       if(testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration().getTestRunPlan()!=null){
                    	   Hibernate.initialize(testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration().getTestRunPlan());
                    	   if(testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration().getTestRunPlan() != null){
								Hibernate.initialize(testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration().getTestRunPlan().getAttachments());
								if( testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration().getTestRunPlan().getAttachments() != null &&  testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration().getTestRunPlan().getAttachments().size()>0){
									Set<Attachment> attachmentSet= testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration().getTestRunPlan().getAttachments();
									Hibernate.initialize(attachmentSet);
									for(Attachment attach: attachmentSet){
										Hibernate.initialize(attach);
									}
								}
							}
                       Hibernate.initialize(testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration().getTestRunPlan().getAutoPostBugs());
                       }
                    		   
						if(testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getTestCase()!=null){
							Hibernate.initialize(testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getTestCase());
						}
					}
			
			log.debug("getByProductId successful");
		} catch (RuntimeException re) {
			log.error("getByProductId failed", re);
		}
		return testCaseExecutionResult;
	}
	
	@Override
	@Transactional
	public List<TestCaseExecutionResult> listAllTestCaseExecutionResult(int startIndex, int pageSize, Date startDate, Date endDate) {

		log.debug("getting all TCER");
		List<TestCaseExecutionResult> testCaseExecutionResults = null;
		try {

			
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestCaseExecutionResult.class, "tcer");
			c.createAlias("workPackageTestCaseExecutionPlan", "wptcep");
			if (startDate != null) {
				c.add(Restrictions.ge("wptcep.createdDate", startDate));
			}
			if (endDate != null) {
				c.add(Restrictions.le("wptcep.createdDate", endDate));
			}
			c.addOrder(Order.asc("testCaseExecutionResultId"));
            c.setFirstResult(startIndex);
            c.setMaxResults(pageSize);

            testCaseExecutionResults=c.list();
			
            if (testCaseExecutionResults != null) {
            	for (TestCaseExecutionResult testCaseExecutionResult : testCaseExecutionResults) {
					if(testCaseExecutionResult!=null) {
						Hibernate.initialize(testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan());
						Hibernate.initialize(testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getWorkPackage());
						if(testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getWorkPackage() != null) {
							if(testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getWorkPackage().getProductBuild() != null) {
								Hibernate.initialize(testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getWorkPackage().getProductBuild());
								if(testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getWorkPackage().getProductBuild().getProductVersion() != null) {
									Hibernate.initialize(testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getWorkPackage().getProductBuild().getProductVersion());
								}
							}
						}
						Hibernate.initialize(testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getWorkPackage().getProductBuild().getProductVersion().getProductMaster());
						Hibernate.initialize(testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getWorkPackage().getWorkFlowEvent());
						Hibernate.initialize(testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getWorkPackage().getWorkFlowEvent().getWorkFlow());
						if(testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getWorkPackage().getWorkFlowEvent().getWorkFlow()!=null){
							
							Hibernate.initialize(testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getWorkPackage().getWorkFlowEvent().getWorkFlow().getEntityMaster());
						}
						Hibernate.initialize(testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getEnvironmentList());
						Hibernate.initialize(testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getTestSuiteList());
						if(testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getRunConfiguration()!=null){
							Hibernate.initialize(testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getRunConfiguration());
							if(testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration()!=null){
								Hibernate.initialize(testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration());
								if(testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration().getTestRunPlan()!=null){
			                    	   Hibernate.initialize(testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration().getTestRunPlan());
			                    	  
			                    	   Hibernate.initialize(testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration().getTestRunPlan().getAutoPostBugs());
									}
							}
						}
						if(testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getTester()!=null){
							Hibernate.initialize(testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getTester().getLoginId());
						}
						
						
						
						if(testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration().getTestRunPlan()!=null){
                    	   Hibernate.initialize(testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration().getTestRunPlan());
                    	   Hibernate.initialize(testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration().getTestRunPlan().getAutoPostBugs());
						}
                    		   
						if(testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getTestCase()!=null){
							Hibernate.initialize(testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getTestCase());
							Hibernate.initialize(testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getTestCase().getProductFeature());
						}
					}
            	}
            }
			log.debug("Geting TestCaseExecutionResults successful");
		} catch (RuntimeException re) {
			log.error("Geting TestCaseExecutionResults failed", re);
		}
		return testCaseExecutionResults;
	}
	
	
	
	@Override
	@Transactional
	public List<TestRunJob> listAllTestRunJob(int startIndex, int pageSize, Date startDate,Date endDate) {

		log.debug("getting all testRunJobs");
		List<TestRunJob> testRunJobs = null;
		try {

			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestRunJob.class, "trj");
			if (startDate != null) {
				c.add(Restrictions.ge("trj.testRunStartTime", startDate));
			}
			if (endDate != null) {
				c.add(Restrictions.le("trj.testRunStartTime", endDate));
			}
			
			c.addOrder(Order.asc("testRunJobId"));
            c.setFirstResult(startIndex);
            c.setMaxResults(pageSize);

            testRunJobs=c.list();
			
            if (testRunJobs != null) {
            	for (TestRunJob testRunJob : testRunJobs) {
    				Hibernate.initialize(testRunJob.getWorkPackage());
    				Hibernate.initialize(testRunJob.getTestSuite());
    				if(testRunJob.getTestSuite()!=null)
    					Hibernate.initialize(testRunJob.getTestSuite().getTestCaseLists());
    				Hibernate.initialize(testRunJob.getTestCaseListSet());
    				Hibernate.initialize(testRunJob.getTestSuiteSet());
    				Hibernate.initialize(testRunJob.getFeatureSet());
    				Hibernate.initialize(testRunJob.getRunConfiguration());

    			}
            }
			log.info("List all TestRunJob successful");
		} catch (RuntimeException re) {
			log.error("List all TestRunJob failed", re);
		}
		return testRunJobs;
	}
	
	@Override
	@Transactional
	public Integer countAllTestCaseExecutionResult(Date startDate, Date endDate) {

		try {
			Criteria crit = sessionFactory.getCurrentSession().createCriteria(TestCaseExecutionResult.class,"workPackageTestCaseExecutionPlan");
			crit.createAlias("workPackageTestCaseExecutionPlan", "wptcep");
			if (startDate != null) {
				crit.add(Restrictions.ge("wptcep.modifiedDate", startDate));
			}
			if (endDate != null) {
				crit.add(Restrictions.le("wptcep.modifiedDate", endDate));
			}
			crit.setProjection(Projections.rowCount());
			Integer count = Integer.parseInt(crit.uniqueResult().toString());
			return count;
		} catch (Exception e) {
			log.error("Unable to get count of all TCER", e);
			return -1;
		}
	}
	
	
	@Override
	@Transactional
	public Integer countAllTestRunJob(Date startDate,Date endDate) {

		try {
			Criteria crit = sessionFactory.getCurrentSession().createCriteria(TestRunJob.class ,"testRunJob");
			if (startDate != null) {
				crit.add(Restrictions.gt("testRunJob.testRunStartTime", startDate));
			}
			if (endDate != null) {
				crit.add(Restrictions.le("testRunJob.testRunStartTime", endDate));
			}
			
			crit.setProjection(Projections.rowCount());
			Integer count = Integer.parseInt(crit.uniqueResult().toString());
			return count;
		} catch (Exception e) {
			log.error("Unable to get count of all testRunJob", e);
			return -1;
		}
	}
	
	

	@Override
	@Transactional
	public List<WorkShiftMaster> listAllWorkShift() {
				List<WorkShiftMaster> workShiftMaster = null;
				try {
					workShiftMaster = sessionFactory
								.getCurrentSession()
								.createQuery(
										"from WorkShiftMaster t  ")
								.list();
					if(!workShiftMaster.isEmpty()){
						for(WorkShiftMaster ws:workShiftMaster){
							Hibernate.initialize(ws.getTestFactory());
							Hibernate.initialize(ws.getShiftType());
						}
					}
					log.debug("list all successful");
				} catch (RuntimeException re) {
					log.error("list all failed", re);
				}
				return workShiftMaster;
	}

	@Override
	@Transactional
	public WorkShiftMaster getWorkShiftById(int shiftId) {
		log.debug(" getWorkShiftById instance by id");
		WorkShiftMaster workShiftMaster = null;
		try {

			List list = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from WorkShiftMaster wsmr where shiftId=:shiftId")
					.setParameter("shiftId", shiftId).list();
			workShiftMaster = (list != null && list.size() != 0) ? (WorkShiftMaster) list
					.get(0) : null;
			
			log.debug("getWorkShiftById successful");
		} catch (RuntimeException re) {
			log.error("getWorkShiftById failed", re);
		}
		return workShiftMaster;
	}

	@Override
	@Transactional
	public WorkShiftMaster getWorkShiftByName(String shiftName) {
		log.debug(" getWorkShiftByName instance by id");
		WorkShiftMaster workShiftMaster = null;
		try {

			List list = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from WorkShiftMaster wsmr where shiftName=:shiftName")
					.setParameter("shiftName", shiftName).list();
			workShiftMaster = (list != null && list.size() != 0) ? (WorkShiftMaster) list
					.get(0) : null;
			
			log.debug("getWorkShiftByName successful");
		} catch (RuntimeException re) {
			log.error("getWorkShiftByName failed", re);
		}
		return workShiftMaster;
	}

	@Override
	@Transactional
	public Integer getProductIdByWorkpackage(int workPackageId) {
		log.debug(" getProductIdByWorkpackage instance by id");
		Integer productId = null;
		WorkPackage workpackgae=null;
		try {
			workpackgae = getWorkPackageById(workPackageId);
			productId = workpackgae.getProductBuild().getProductVersion().getProductMaster().getProductId();
			log.debug("getProductIdByWorkpackage successful");
		} catch (RuntimeException re) {
			log.error("getProductIdByWorkpackage failed", re);
		}
		return productId;
	}

	@Override
	@Transactional
	public List<WorkPackageTestCaseExecutionPlan> listWorkPackageTestCasesExecutionPlanTestLead(
			int workPackageId, int jtStartIndex, int jtPageSize,UserList user) {
		log.debug("listing listWorkPackageTestCasesExecutionPlanTestLead instance");
		List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlan = null;
		try {
			workPackageTestCaseExecutionPlan = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from WorkPackageTestCaseExecutionPlan wptcep where wptcep.workPackage.workPackageId = "
									+ workPackageId + "and wptcep.testLead.userId = "+user.getUserId()
									+ " order by wptcep.testCase.testCaseId asc")
					.setFirstResult(jtStartIndex).setMaxResults(jtPageSize).list();
			if (!(workPackageTestCaseExecutionPlan == null || workPackageTestCaseExecutionPlan
					.isEmpty())) {
				for (WorkPackageTestCaseExecutionPlan dl : workPackageTestCaseExecutionPlan) {
					Hibernate.initialize(dl.getTestCase());
					Hibernate.initialize(dl.getWorkPackage());
					if(dl.getWorkPackage() != null){
						Hibernate.initialize(dl.getWorkPackage());
						if(dl.getWorkPackage().getProductBuild() != null){
							Hibernate.initialize(dl.getWorkPackage().getProductBuild());
							if(dl.getWorkPackage().getProductBuild().getProductVersion() != null){
								Hibernate.initialize(dl.getWorkPackage().getProductBuild().getProductVersion());
								if(dl.getWorkPackage().getProductBuild().getProductVersion().getProductMaster() != null){
									Hibernate.initialize(dl.getWorkPackage().getProductBuild().getProductVersion().getProductMaster());
								}
							}
						}
					}
					Hibernate.initialize(dl.getEnvironmentList());
					if(dl.getHostList()!=null){
					Hibernate.initialize(dl.getHostList().getHostName());
					}
					Hibernate.initialize(dl.getTester());
					Hibernate.initialize(dl.getTestLead());
					Hibernate.initialize(dl.getTestCaseExecutionResult());
					Hibernate.initialize(dl.getActualWorkShiftMaster());
					Hibernate.initialize(dl.getPlannedWorkShiftMaster());
					Hibernate.initialize(dl.getTestCase().getTestCasePriority());
					Hibernate.initialize(dl.getTestSuiteList());
					Hibernate.initialize(dl.getTestCase().getTestcaseTypeMaster());
					Hibernate.initialize(dl.getTestCase().getTestCasePriority());
				
					Hibernate.initialize(dl.getRunConfiguration());
					if(dl.getRunConfiguration()!=null){
						if(dl.getRunConfiguration().getRunconfiguration()!=null){
							Hibernate.initialize(dl.getRunConfiguration().getRunconfiguration());
							if(dl.getRunConfiguration().getRunconfiguration().getEnvironmentcombination()!=null)
								Hibernate.initialize(dl.getRunConfiguration().getRunconfiguration().getEnvironmentcombination());
							if(dl.getRunConfiguration().getRunconfiguration().getGenericDevice()!=null)
								Hibernate.initialize(dl.getRunConfiguration().getRunconfiguration().getGenericDevice());
							
						}
						Hibernate.initialize(dl.getRunConfiguration().getWorkpackage());
						
					}

				}
			}
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return workPackageTestCaseExecutionPlan;
	}

	@Override
	public List<WorkPackageTestCaseExecutionPlan> listWorkPackageTestCasesExecutionPlanByEnv(
			int workPackageId, int jtStartIndex, int jtPageSize, int envId) {
				log.debug("listing listWorkPackageTestCasesExecutionPlanByEnv instance");
				List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlan = null;
				try {
					workPackageTestCaseExecutionPlan = sessionFactory
							.getCurrentSession()
							.createQuery(
									"from WorkPackageTestCaseExecutionPlan wptcep where wptcep.workPackage.workPackageId = "
											+ workPackageId + "and wptcep.environment.environmentId = "+envId
											+ " order by wptcep.testCase.testCaseId asc")
							.setFirstResult(jtStartIndex).setMaxResults(jtPageSize).list();
					if (!(workPackageTestCaseExecutionPlan == null || workPackageTestCaseExecutionPlan
							.isEmpty())) {
						for (WorkPackageTestCaseExecutionPlan dl : workPackageTestCaseExecutionPlan) {
							Hibernate.initialize(dl.getTestCase());
							Hibernate.initialize(dl.getWorkPackage());
							if(dl.getWorkPackage() != null){
								Hibernate.initialize(dl.getWorkPackage());
								if(dl.getWorkPackage().getProductBuild() != null){
									Hibernate.initialize(dl.getWorkPackage().getProductBuild());
									if(dl.getWorkPackage().getProductBuild().getProductVersion() != null){
										Hibernate.initialize(dl.getWorkPackage().getProductBuild().getProductVersion());
									}
								}
							}
							Hibernate.initialize(dl.getTester());
							Hibernate.initialize(dl.getTestLead());
							Hibernate.initialize(dl.getTestCaseExecutionResult());
							Hibernate.initialize(dl.getActualWorkShiftMaster());
							Hibernate.initialize(dl.getPlannedWorkShiftMaster());
							Hibernate.initialize(dl.getTestSuiteList());
							Hibernate.initialize(dl.getTestCase().getTestcaseTypeMaster());
							Hibernate.initialize(dl.getTestCase().getTestCasePriority());
						
							Hibernate.initialize(dl.getRunConfiguration());
							Hibernate.initialize(dl.getRunConfiguration().getRunconfiguration());
							Hibernate.initialize(dl.getRunConfiguration().getWorkpackage());
							
							
						}
					}
					log.debug("list successful");
				} catch (RuntimeException re) {
					log.error("list failed", re);
				}
				return workPackageTestCaseExecutionPlan;
	}

	@Override
	public List<WorkPackageTestCaseExecutionPlan> listWorkPackageTestCasesExecutionPlanTestLeadByEnv(
			int workPackageId, int jtStartIndex, int jtPageSize, UserList user,
			int envId) {
				log.debug("listing listWorkPackageTestCasesExecutionPlanTestLeadByEnv instance");
				List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlan = null;
				try {
					workPackageTestCaseExecutionPlan = sessionFactory
							.getCurrentSession()
							.createQuery(
									"from WorkPackageTestCaseExecutionPlan wptcep where wptcep.workPackage.workPackageId = "
											+ workPackageId + "and wptcep.testLead.userId = "+user.getUserId()
											+ "and wptcep.environment.environmentId = "+envId
											+ " order by wptcep.testCase.testCaseId asc")
							.setFirstResult(jtStartIndex).setMaxResults(jtPageSize).list();
					if (!(workPackageTestCaseExecutionPlan == null || workPackageTestCaseExecutionPlan
							.isEmpty())) {
						for (WorkPackageTestCaseExecutionPlan dl : workPackageTestCaseExecutionPlan) {
							Hibernate.initialize(dl.getTestCase());
							Hibernate.initialize(dl.getWorkPackage());
							if(dl.getWorkPackage() != null){
								Hibernate.initialize(dl.getWorkPackage());
								if(dl.getWorkPackage().getProductBuild() != null){
									Hibernate.initialize(dl.getWorkPackage().getProductBuild());
									if(dl.getWorkPackage().getProductBuild().getProductVersion() != null){
										Hibernate.initialize(dl.getWorkPackage().getProductBuild().getProductVersion());
									}
								}
							}
							Hibernate.initialize(dl.getTester());
							Hibernate.initialize(dl.getTestLead());
							Hibernate.initialize(dl.getTestCaseExecutionResult());
							Hibernate.initialize(dl.getActualWorkShiftMaster());
							Hibernate.initialize(dl.getPlannedWorkShiftMaster());
							Hibernate.initialize(dl.getTestSuiteList());
							Hibernate.initialize(dl.getTestCase().getTestcaseTypeMaster());
							Hibernate.initialize(dl.getTestCase().getTestCasePriority());
						
							Hibernate.initialize(dl.getRunConfiguration());
							Hibernate.initialize(dl.getRunConfiguration().getRunconfiguration());
							Hibernate.initialize(dl.getRunConfiguration().getWorkpackage());
						}
					}
					log.debug("list successful");
				} catch (RuntimeException re) {
					log.error("list failed", re);
				}
				return workPackageTestCaseExecutionPlan;
	}
	
	@Override
	@Transactional
	public List<WorkPackageDayWisePlanDTO> listWorkPackageDayWisePlan(int workPackageId, Integer workShiftId, Date startDate, Date endDate) {
		log.debug("listing listWorkPackageDayWisePlan");
		List<WorkPackageDayWisePlanDTO> dailyShiftPlans = new ArrayList<WorkPackageDayWisePlanDTO>();
		log.debug("Getting Plans for : " + workPackageId + " : " + startDate + " : " + endDate);
		try {

			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackageTestCaseExecutionPlan.class, "wptcep");
			c.createAlias("wptcep.plannedWorkShiftMaster", "plannedShift");
			c.add(Restrictions.eq("workPackage.workPackageId", workPackageId));
			c.add(Restrictions.between("plannedExecutionDate", startDate,  endDate));
			if (workShiftId != null) {
				c.add(Restrictions.eq("plannedShift.shiftId", workShiftId.intValue()));
			}
			c.setProjection(Projections.projectionList()
										.add(Projections.countDistinct("id"))
										.add(Projections.groupProperty("plannedShift.shiftId"))
										.add(Projections.groupProperty("plannedShift.shiftName"))
										.add(Projections.groupProperty("plannedExecutionDate"))
							)
			.addOrder(Order.asc("plannedExecutionDate"));
			
			List<Object[]> list = c.list();
			log.debug("Daily shift plans count : " + list.size());
			WorkPackageDayWisePlanDTO workPackageDayWisePlanDTO = null;
			for (Object[] row : list) {
				workPackageDayWisePlanDTO = new WorkPackageDayWisePlanDTO();
				workPackageDayWisePlanDTO.setWorkPackageId(workPackageId);
				workPackageDayWisePlanDTO.setPlannedTestCasesCount(Integer.parseInt(row[0].toString()));
				workPackageDayWisePlanDTO.setShiftId((Integer)row[1]);
				workPackageDayWisePlanDTO.setShiftName((String)row[2]);
				workPackageDayWisePlanDTO.setPlanDate((Date)row[3]);
				dailyShiftPlans.add(workPackageDayWisePlanDTO);
			
			}
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}catch(Exception e){
			e.printStackTrace();
		}
		return dailyShiftPlans;
	}
	

	
	@Override
	@Transactional
	public Set<RunConfiguration> getEnvironmentMappedToWorkpackage(int workpackageId) {
		log.debug("inside getEnvironmentMappedToWorkpackage");
		WorkPackage workPackage =null;
		Set<RunConfiguration> environments = null;
		try {

		List list = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from WorkPackage w where workPackageId=:workPackageId")
					.setParameter("workPackageId", workpackageId).list();
		workPackage = (list != null && list.size() != 0) ? (WorkPackage) list
					.get(0) : null;
					if(workPackage!=null){
		Hibernate.initialize(workPackage.getRunConfigurationList());
	
					}
		environments =workPackage.getRunConfigurationList();
		for(RunConfiguration env:environments){
			Hibernate.initialize(env.getEnvironmentcombination());
			Hibernate.initialize(env.getGenericDevice());
			Hibernate.initialize(env.getHostList());

		}
		
			log.debug("getEnvironmentMappedToWorkpackage successful");
		} catch (RuntimeException re) {
			log.error("getEnvironmentMappedToWorkpackage failed", re);
		}
		return environments;
	}
	
	@Override
	@Transactional
	public List<WorkPackage> getWorkPackagesFromWorkPackageTestcaseExecutionPlanByUserId(int userId) {
		log.debug("listWorkPackagesByUserId method");
		List<WorkPackage> workPackages = null;
		try {
			workPackages = sessionFactory.getCurrentSession()
					.createQuery("select distinct wp from WorkPackage as wp, WorkPackageTestCaseExecutionPlan as wptcep" +
									" where (wptcep.tester.userId=:testerId or wptcep.testLead.userId = :testLeadId)" +
									" and wptcep.workPackage.workPackageId = wp.workPackageId")
									.setParameter("testerId", userId)
									.setParameter("testLeadId", userId)
									.list();
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return workPackages;
	}

	@Override
	@Transactional
	public WorkPackage getWorkPackageByWorkPackageName(String workPackageName) {
		log.debug("getting WorkPackage instance by id"+workPackageName);
		WorkPackage workPackage = null;
		try {
			List list = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from WorkPackage w where name=:workPackageName")
					.setParameter("workPackageName", workPackageName.trim()).list();
			workPackage = (list != null && list.size() != 0) ? (WorkPackage) list
					.get(0) : null;
			if (workPackage != null) {
				Hibernate.initialize(workPackage.getProductBuild()
						.getProductVersion().getProductMaster());
			}
			log.debug("getByProductId successful");
		} catch (RuntimeException re) {
			log.error("getByProductId failed", re);
		}
		return workPackage;

	}
	

	@Override
	@Transactional
	public WorkPackage mapWorkpackageEnv(int workpackageId, int environmentId,
			String action) {
		log.debug("mapWorkpackageEnv");

		WorkPackage workPackage = null;
		Environment environment = null;

		try {
			workPackage = getWorkPackageById(workpackageId);
			environment = getEnvironmentById(environmentId);

			if (workPackage != null && environment != null) {
				boolean needToUpdateOrAdd = false;

				Set<Environment> environmentSet = workPackage
						.getEnvironmentList();
				if (action.equalsIgnoreCase("Add")) {

					if (environmentSet.size() == 0) {
						needToUpdateOrAdd = true;
					} else {
						Environment environmentForProcessing = environmentSet
								.iterator().next();
						if (environmentForProcessing != null) {
							int alreadyAvailableEnvironmentId = environmentForProcessing
									.getEnvironmentId().intValue();

							if (alreadyAvailableEnvironmentId != environmentId) {
								log.debug("alreadyAvailableEnvironmentId---------->"
												+ alreadyAvailableEnvironmentId);
								log.debug("environmentId---------->"
										+ environmentId);
								Environment environmentAvailable = getEnvironmentById(alreadyAvailableEnvironmentId);
								for (WorkPackage wp : environmentAvailable
										.getWorkPackageList()) {
									log.debug("wp.getWorkPackageId().intValue()"
											+ wp.getWorkPackageId().intValue());

									if (wp.getWorkPackageId().intValue() == workpackageId) {
										log.debug("workpackage found in environment");
										sessionFactory.getCurrentSession()
												.saveOrUpdate(
														environmentAvailable);
										log.debug("environmentAvailable.getWorkPackageList().size()="
												+ environmentAvailable
														.getWorkPackageList()
														.size());
										break;
									}
								}

								needToUpdateOrAdd = true;
							}
						}
					}

					if (needToUpdateOrAdd) {
						workPackage.getEnvironmentList().add(environment);
						environment.getWorkPackageList().add(workPackage);
						sessionFactory.getCurrentSession().saveOrUpdate(
								workPackage);
						sessionFactory.getCurrentSession().saveOrUpdate(
								environment);
					}
				}
				else if(action.equalsIgnoreCase("Remove")){

					log.debug("Remove environment from Workpackage");

					try {
						workPackage = (WorkPackage) sessionFactory.getCurrentSession().get(WorkPackage.class, workpackageId);
						if (workPackage == null) {
							log.debug("workpackage with specified id not found : " + workpackageId);
							return null;
						}
						
						environment = (Environment) sessionFactory.getCurrentSession().get(Environment.class, environmentId);
						int environmentGroupId=environment.getEnvironmentCategory().getEnvironmentGroup().getEnvironmentGroupId();
						Set<Environment> environmentMapped=workPackage.getEnvironmentList();
						int count=0;
						for(Environment env:environmentMapped){
							if(env.getEnvironmentCategory().getEnvironmentGroup().getEnvironmentGroupId()==environmentGroupId){
								count++;
							}
						}
						
						if(count==1){
							return null;
						}
							
						if (environment == null) {
							log.debug("environment could not found in the database : " + environmentId);
							return null;
						}
						Set<Environment> environmentList = workPackage.getEnvironmentList();
						environmentList.remove(environment);
						
						workPackage.setEnvironmentList(environmentList);
						
						sessionFactory.getCurrentSession().save(workPackage);
						
						log.debug("Removed environment from workpackage successfully");
					} catch (RuntimeException re) {
						log.error("Failed to remove environment from workpackage", re);
						
					}
				
				}
			}
		} catch (RuntimeException re) {
			log.error("list specific failed", re);
		}
		return workPackage;
	}

	@Override
	@Transactional
	public Set<ProductLocale> getLocaleMappedToWorkpackage(int workpackageId) {
		log.debug("inside getLocaleMappedToWorkpackage");
				WorkPackage workPackage =null;
				Set<ProductLocale> locale = null;
				try {

				List list = sessionFactory
							.getCurrentSession()
							.createQuery(
									"from WorkPackage w where workPackageId=:workPackageId")
							.setParameter("workPackageId", workpackageId).list();
				workPackage = (list != null && list.size() != 0) ? (WorkPackage) list
							.get(0) : null;
				Hibernate.initialize(workPackage.getLocaleList());
				locale =workPackage.getLocaleList();
					
					log.debug("getLocaleMappedToWorkpackage successful");
				} catch (RuntimeException re) {
					log.error("getLocaleMappedToWorkpackage failed", re);
				}
				return locale;
	}

	@Override
	@Transactional
	public WorkPackage mapWorkpackageLocale(int workpackageId, int localeId,
			String action) {
		log.debug("mapWorkpackageLocale");

		WorkPackage workPackage = null;
		ProductLocale locale = null;

		try {
			workPackage = getWorkPackageById(workpackageId);
			locale = productLocaleDao.getProductLocaleById(localeId);

			if (workPackage != null && locale != null) {
				boolean needToUpdateOrAdd = false;

				Set<ProductLocale> localeSet = workPackage
						.getLocaleList();
				if (action.equalsIgnoreCase("Add")) {

					if (localeSet.size() == 0) {
						needToUpdateOrAdd = true;
					} else {
						ProductLocale localeForProcessing = localeSet
								.iterator().next();
						if (localeForProcessing != null) {
							int alreadyAvailableLocaleId = localeForProcessing
									.getProductLocaleId().intValue();

							if (alreadyAvailableLocaleId != localeId) {
								log.debug("alreadyAvailableLocaleId---------->"
												+ alreadyAvailableLocaleId);
								log.debug("localeId---------->"
										+ localeId);
								ProductLocale localeAvailable = productLocaleDao.getProductLocaleById(alreadyAvailableLocaleId);
								for (WorkPackage wp : localeAvailable
										.getWorkPackageList()) {
									log.debug("wp.getWorkPackageId().intValue()"
											+ wp.getWorkPackageId().intValue());

									if (wp.getWorkPackageId().intValue() == workpackageId) {
										log.debug("workpackage found in decouple");
										sessionFactory.getCurrentSession()
												.saveOrUpdate(
														localeAvailable);
										log.debug("localeAvailable.getWorkPackageList().size()="
												+ localeAvailable
														.getWorkPackageList()
														.size());
										break;
									}
								}

								needToUpdateOrAdd = true;
							}
						}
					}

					if (needToUpdateOrAdd) {
						workPackage.getLocaleList().add(locale);
						locale.getWorkPackageList().add(workPackage);
						sessionFactory.getCurrentSession().saveOrUpdate(
								workPackage);
						sessionFactory.getCurrentSession().saveOrUpdate(
								locale);
					}
				}
				else if(action.equalsIgnoreCase("Remove")){

					log.debug("Remove locale from Workpackage");

					try {
						log.debug("localeId input: "  +localeId);
						workPackage = (WorkPackage) sessionFactory.getCurrentSession().get(WorkPackage.class, workpackageId);
						if (workPackage == null) {
							log.debug("workpackage with specified id not found : " + workpackageId);
							return null;
						}
						locale = (ProductLocale) sessionFactory.getCurrentSession().get(ProductLocale.class, localeId);
						if (locale == null) {
							log.debug("locale could not found in the database : " + localeId);
							log.debug("locale could not found in the database : " + localeId);
							return null;
						}
						Set<ProductLocale> localeList = workPackage.getLocaleList();
						log.debug("locale set size before removing :"+ localeList.size());
						localeList.remove(locale);
						log.debug("locale set size  after removing ::"+ localeList.size());
						
						workPackage.setLocaleList(localeList);
						
						sessionFactory.getCurrentSession().save(workPackage);
						
						log.debug("Removed locale from workpackage successfully");
					} catch (RuntimeException re) {
						log.error("Failed to remove locale from workpackage", re);
						
					}
				
				}
			}
		} catch (RuntimeException re) {
			log.error("list specific failed", re);
		}
		return workPackage;
	}

	@Override
	public List<ProductLocale> getWorkPackageTestCasesExecutionPlanLocales(
			int workPackageId, int testCaseId) {
				log.debug("listing getWorkPackageTestCasesExecutionPlanLocales");
				List<ProductLocale> locales = null;
				try {

					locales = sessionFactory.getCurrentSession().createQuery("select distinct l"
					+ " from ProductLocale as l, WorkPackageTestCaseExecutionPlan as wptcep"
					+ " where l.productLocaleId = wptcep.productLocale.productLocaleId"
					+ " and wptcep.workPackage.workPackageId = :workPackageId and wptcep.testCase.testCaseId = :testCaseId"
					+ " order by l.productLocaleId asc")
					.setParameter("workPackageId", workPackageId)
					.setParameter("testCaseId", testCaseId)
					.list();
				
					log.debug("Selected productLocale in DAO : " + locales.size() + " : Workpackage ID : " + workPackageId + " : TestCase ID : " + testCaseId);
				} catch (RuntimeException re) {
					log.error("list failed", re);
				}
				return locales;
	}

	@Override
	@Transactional
	public List<WorkPackageStatusSummaryDTO> workPackageStatusSummary(int workPackageId,ProductUserRole productUserRole) {
		log.debug("listing workPackageStatusSummary: ");
		List<WorkPackageStatusSummaryDTO> listOfWorkPackageStatusSummaryDTO = new ArrayList<WorkPackageStatusSummaryDTO>();
		boolean isTester=false;
		if(productUserRole!=null && productUserRole.getRole().getUserRoleId()== Integer.parseInt(TAFConstants.ROLE_TESTER))
			isTester=true;
		try {
			
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackage.class, "wp");
			c.createAlias("wp.runConfigurationList", "runconflist");
			c.createAlias("wp.workPackageTestCases", "wptclist");
			c.createAlias("wp.workPackageTestCaseExecutionPlan", "wptceplist");
			c.add(Restrictions.eq("wp.workPackageId", workPackageId));
			if(isTester)
				c.add(Restrictions.eq("wptceplist.tester.userId", productUserRole.getUser().getUserId()));
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Property.forName("wp.workPackageId").as("workPackageId"));
			projectionList.add(Property.forName("wp.name").as("workPackageName"));
			projectionList.add(Projections.countDistinct("runconflist.runconfigId"));
			projectionList.add(Projections.countDistinct("wptclist.id"));
			projectionList.add(Projections.countDistinct("wptceplist.id"));
			projectionList.add(Projections.groupProperty("wp.workPackageId"));
			c.setProjection(projectionList);
			
			List<Object[]> list = c.list();
			WorkPackageStatusSummaryDTO workPackageStatusSummaryDTO = null;
			for (Object[] row : list) {
				workPackageStatusSummaryDTO = new WorkPackageStatusSummaryDTO();
				workPackageStatusSummaryDTO.setWorkPackageId((Integer)row[0]);
				workPackageStatusSummaryDTO.setWorkPackageName((String)row[1]);
				workPackageStatusSummaryDTO.setSelectedEnvironmentsCount(((Long)row[2]).intValue());
				workPackageStatusSummaryDTO.setSelectedTestCasesCount(((Long)row[3]).intValue());
				workPackageStatusSummaryDTO.setTotalTestCaseForExecutionCount(((Long)row[4]).intValue());
				listOfWorkPackageStatusSummaryDTO.add(workPackageStatusSummaryDTO);
			}

			//Get the executed Testcases for the workpackages
			c = sessionFactory.getCurrentSession().createCriteria(WorkPackage.class, "wp");
			c.createAlias("wp.workPackageTestCaseExecutionPlan", "wptceplist");
			c.add(Restrictions.eq("wp.workPackageId", workPackageId));
			c.add(Restrictions.eq("wptceplist.isExecuted", 1));
			if(isTester)
				c.add(Restrictions.eq("wptceplist.tester.userId", productUserRole.getUser().getUserId()));
			projectionList = Projections.projectionList();
			projectionList.add(Property.forName("wp.workPackageId").as("workPackageId"));
			projectionList.add(Projections.countDistinct("wptceplist.id"));
			projectionList.add(Projections.groupProperty("wp.workPackageId"));
			c.setProjection(projectionList);
			list = c.list();
			Integer wpId = 0;
			for (Object[] row : list) {
				wpId = ((Integer)row[0]);
				for(WorkPackageStatusSummaryDTO summaryDTO : listOfWorkPackageStatusSummaryDTO) {
					if (summaryDTO.getWorkPackageId().equals( wpId)) {
						summaryDTO.setCompletedTestCaseCount(((Long)row[1]).intValue());
						summaryDTO.setNotCompletedTestCaseCount(summaryDTO.getTotalTestCaseForExecutionCount() - summaryDTO.getCompletedTestCaseCount());
						log.debug("Completed Test case count : "  +((Long)row[1]).intValue());
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
	public int updateWorkPackageTestcaseExecutionPlan(
			List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlanList) {
			log.debug("update workpackage testcases to execution plan");
			int count = 0;
			try {
				if (workPackageTestCaseExecutionPlanList == null || workPackageTestCaseExecutionPlanList.isEmpty()) {
					return 0;
				}
				for (WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan : workPackageTestCaseExecutionPlanList) {
					sessionFactory.getCurrentSession().update(workPackageTestCaseExecutionPlan);
					count++;
				}
				log.debug("update WorkPackageTestcaseExecutionPlan successfully");
			} catch (Exception e) {

				log.error("Unable to update workpackage testcases to execution plan", e);
				return count;
			}

			return count;
	}

	@Override
	@Transactional
	public boolean checkExists(TestCaseList testCase, WorkPackage workPackage,
			Environment environment) {

		String hql = "select count(*) from workpackage_testcase_execution_plan wptcep where wptcep.testcaseId =:testcaseId and " +
				"wptcep.workPackageId =:workPackageId ";
		List list=null;
		if(environment ==null){
			hql=hql+" and wptcep.productLocaleId =:productLocaleId";
			 list = sessionFactory.getCurrentSession().createSQLQuery(hql).
						setParameter("testcaseId", testCase.getTestCaseId()).
						setParameter("workPackageId", workPackage.getWorkPackageId()).
						list();
		}else
		if(environment!=null && environment.getEnvironmentId()!=null ){
			hql=hql+" and wptcep.environmentId =:environmentId";
			list = sessionFactory.getCurrentSession().createSQLQuery(hql).
							setParameter("testcaseId", testCase.getTestCaseId()).
							setParameter("workPackageId", workPackage.getWorkPackageId()).
							setParameter("environmentId", environment.getEnvironmentId())
							.list();
		}
		if (list != null  && !list.isEmpty() && (list.get(0).toString()).equals("0" )){ 
		    return false;
		}
		else{ 
			return true;
		}
	}

	@Override
	@Transactional
	public void deleteWorkPackageTestcaseExecutionPlan(TestCaseList testCase,
			WorkPackage workPackage, Environment environment,
			ProductLocale localeSel) {
		List<WorkPackageTestCaseExecutionPlan> list = null;

		WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan = (list != null && list.size() != 0) ? (WorkPackageTestCaseExecutionPlan)(list.get(0)): null;
		if(workPackageTestCaseExecutionPlan != null){
			TestCaseExecutionResult testCaseExecutionResult =workPackageTestCaseExecutionPlan.getTestCaseExecutionResult();
			sessionFactory.getCurrentSession().delete(testCaseExecutionResult);
			
			sessionFactory.getCurrentSession().delete(workPackageTestCaseExecutionPlan);
		}
	}
	
	@Override
	@Transactional
	public WorkPackageTestCaseExecutionPlan getWorkpackageTestcaseExecutionPlanById(int workPackageTestCaseExecutionPlanId) {
		log.debug("listing getWorkpackageTestcaseExecutionPlanById instance");
		List<WorkPackageTestCaseExecutionPlan> list = null;
		WorkPackageTestCaseExecutionPlan dl =null;
		try {
			list = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from WorkPackageTestCaseExecutionPlan where id = "
									+ workPackageTestCaseExecutionPlanId).list();
			dl = (list != null && list.size() != 0) ? (WorkPackageTestCaseExecutionPlan)(list.get(0)): null;				
			if(dl!=null){
					Hibernate.initialize(dl.getTestCase().getProductMaster());
					Hibernate.initialize(dl.getWorkPackage().getProductBuild().getProductVersion().getProductMaster());
					Hibernate.initialize(dl.getTestCase());
					Hibernate.initialize(dl.getTestCase().getProductFeature());

					Hibernate.initialize(dl.getTestCaseExecutionResult());
					Hibernate.initialize(dl.getWorkPackage());
					if(dl.getWorkPackage() != null){
						Hibernate.initialize(dl.getWorkPackage());
						if(dl.getWorkPackage().getProductBuild() != null){
							Hibernate.initialize(dl.getWorkPackage().getProductBuild());
							if(dl.getWorkPackage().getProductBuild().getProductVersion() != null){
								Hibernate.initialize(dl.getWorkPackage().getProductBuild().getProductVersion());
							}
						}
					}
					Hibernate.initialize(dl.getWorkPackage().getWorkFlowEvent());
					Hibernate.initialize(dl.getWorkPackage().getWorkFlowEvent().getWorkFlow());
					Hibernate.initialize(dl.getEnvironmentList());
					Hibernate.initialize(dl.getTester());
					Hibernate.initialize(dl.getTestLead());
					Hibernate.initialize(dl.getActualWorkShiftMaster());
					Hibernate.initialize(dl.getPlannedWorkShiftMaster());
					Hibernate.initialize(dl.getExecutionPriority());
					Hibernate.initialize(dl.getHostList());
					Hibernate.initialize(dl.getTestCase().getDecouplingCategory());
					Hibernate.initialize(dl.getTestCase().getTestcaseTypeMaster());
					Hibernate.initialize(dl.getTestCase().getTestCasePriority());
				
					Hibernate.initialize(dl.getTestSuiteList());
					if(dl.getRunConfiguration()!=null){
						if(dl.getRunConfiguration().getRunconfiguration()!=null){ 
							Hibernate.initialize(dl.getRunConfiguration().getRunconfiguration());
							if(dl.getRunConfiguration().getRunconfiguration()!=null){
								if(dl.getRunConfiguration().getRunconfiguration().getEnvironmentcombination()!=null)
									Hibernate.initialize(dl.getRunConfiguration().getRunconfiguration().getEnvironmentcombination());
								if(dl.getRunConfiguration().getRunconfiguration().getGenericDevice()!=null)
									Hibernate.initialize(dl.getRunConfiguration().getRunconfiguration().getGenericDevice());
							}
						}
						Hibernate.initialize(dl.getRunConfiguration());
						Hibernate.initialize(dl.getRunConfiguration().getWorkpackage());
						Hibernate.initialize(dl.getFeature());
					}
					
					Hibernate.initialize(dl.getTestCase().getProductMaster().getProductMode());
					if(dl.getTestRunJob() != null){
						Hibernate.initialize(dl.getTestRunJob());
					}
			}
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return dl;
	}
	@Override
	@Transactional
	public WorkPackageTestCaseExecutionPlan getWorkpackageTestcaseExecutionPlanByIdWithMinimalIntizialition(int workPackageTestCaseExecutionPlanId) {
		log.debug("listing getWorkpackageTestcaseExecutionPlanById instance");
		List<WorkPackageTestCaseExecutionPlan> list = null;
		WorkPackageTestCaseExecutionPlan dl =null;
		try {
			list = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from WorkPackageTestCaseExecutionPlan where id = "
									+ workPackageTestCaseExecutionPlanId).list();
			dl = (list != null && list.size() != 0) ? (WorkPackageTestCaseExecutionPlan)(list.get(0)): null;				
			if(dl!=null){
					
					Hibernate.initialize(dl.getTestCaseExecutionResult());
					
			}
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return dl;
	}
	
	@Override
	public List<JsonWorkPackageTestCaseExecutionPlan> getNotExecutedTestCasesOfWorkPackageId(
			int workPackageId) {
		return null;
	}

	@Override
	@Transactional
	public List<WorkPackageTestCaseExecutionPlan> listCompletedOrNotCompletedTestCasesOfworkPackage(
			int workPackageId, int jtStartIndex, int jtPageSize, int isExecuted,ProductUserRole productUserRole) {
		log.debug("listing listWorkPackageTestCasesExecutionPlan instance::: isExecuted: "+isExecuted);
		List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlan = null;
		boolean isTester=false;
		if(productUserRole!=null && productUserRole.getRole().getUserRoleId()== Integer.parseInt(TAFConstants.ROLE_TESTER))
			isTester=true;
		String query="from WorkPackageTestCaseExecutionPlan wptcep where wptcep.workPackage.workPackageId = "
				+ workPackageId + "and wptcep.isExecuted="+isExecuted
				+ " order by wptcep.testCase.testCaseId asc";
		if(isTester){
			query="from WorkPackageTestCaseExecutionPlan wptcep where wptcep.workPackage.workPackageId = "
					+ workPackageId + "and wptcep.isExecuted="+isExecuted + "and wptcep.tester.userId="+productUserRole.getUser().getUserId()
					+ " order by wptcep.testCase.testCaseId asc";
		}
		try {
			workPackageTestCaseExecutionPlan = sessionFactory
					.getCurrentSession()
					.createQuery(query)
					.setFirstResult(jtStartIndex).setMaxResults(jtPageSize).list();
			if (!(workPackageTestCaseExecutionPlan == null || workPackageTestCaseExecutionPlan
					.isEmpty())) {
				for (WorkPackageTestCaseExecutionPlan dl : workPackageTestCaseExecutionPlan) {
					Hibernate.initialize(dl.getTestCase());
					Hibernate.initialize(dl.getWorkPackage());
					if(dl.getWorkPackage() != null){
						Hibernate.initialize(dl.getWorkPackage());
						if(dl.getWorkPackage().getProductBuild() != null){
							Hibernate.initialize(dl.getWorkPackage().getProductBuild());
							if(dl.getWorkPackage().getProductBuild().getProductVersion() != null){
								Hibernate.initialize(dl.getWorkPackage().getProductBuild().getProductVersion());
							}
						}
					}
					Hibernate.initialize(dl.getTester());
					Hibernate.initialize(dl.getTestLead());
					Hibernate.initialize(dl.getTestCaseExecutionResult());
					Hibernate.initialize(dl.getActualWorkShiftMaster());
					Hibernate.initialize(dl.getPlannedWorkShiftMaster());
					Hibernate.initialize(dl.getTestSuiteList());
					Hibernate.initialize(dl.getTestCase().getTestcaseTypeMaster());
					Hibernate.initialize(dl.getTestCase().getTestCasePriority());
					
					Hibernate.initialize(dl.getRunConfiguration());
					Hibernate.initialize(dl.getRunConfiguration().getRunconfiguration());
					Hibernate.initialize(dl.getRunConfiguration().getWorkpackage());
					
					
					Hibernate.initialize(dl.getTestCase().getDecouplingCategory());
					Hibernate.initialize(dl.getEnvironmentList());
					Hibernate.initialize(dl.getTestCase().getProductFeature());
					Hibernate.initialize(dl.getTestCase().getProductMaster().getProductName());
					Hibernate.initialize(dl.getTestCase().getProductMaster().getProductMode().getModeName());
					
				}
			}
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return workPackageTestCaseExecutionPlan;
	}

	@Override
	@Transactional
	public List<WorkPackageTestCaseExecutionPlan> getWorkPackageTestcaseExecutionPlanByWorkPackageId(
			int workPackageId, int testcaseId,int environmentId,WorkPackageTestCase workPackageTestCase) {
		boolean setIsSelected=false;
		TestCaseList testCaseList =null;
		List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlans = null;
		try {
			workPackageTestCaseExecutionPlans = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from WorkPackageTestCaseExecutionPlan wptcep where wptcep.workPackage.workPackageId=:workPackageId and wptcep.testCase.testCaseId=:testcaseId")
					.setParameter("workPackageId", workPackageId).setParameter("testcaseId", testcaseId).list();
			if (!(workPackageTestCaseExecutionPlans == null || workPackageTestCaseExecutionPlans.isEmpty())) {
				
				for(WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan : workPackageTestCaseExecutionPlans) {
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getEnvironmentList());
					if(workPackageTestCaseExecutionPlan.getWorkPackage() != null){
						Hibernate.initialize(workPackageTestCaseExecutionPlan.getWorkPackage());
						if(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild() != null){
							Hibernate.initialize(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild());
							if(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion() != null){
								Hibernate.initialize(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion());
							}
						}
					}
				
				}
			}
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return workPackageTestCaseExecutionPlans;
	}
	
	@Override
	@Transactional
	public List<WorkPackageTestCaseExecutionPlan> getWorkPackageTestcaseExecutionPlanByWorkPackageIdByLocale(
			int workPackageId, int testcaseId,int localeId,WorkPackageTestCase workPackageTestCase) {
		boolean setIsSelected=false;
		TestCaseList testCaseList =null;
		List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlans = null;
		try {
			workPackageTestCaseExecutionPlans = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from WorkPackageTestCaseExecutionPlan wptcep where wptcep.workPackage.workPackageId=:workPackageId and wptcep.testCase.testCaseId=:testcaseId and wptcep.productLocale.productLocaleId=:productLocaleId")
					.setParameter("workPackageId", workPackageId).setParameter("testcaseId", testcaseId).setParameter("productLocaleId", localeId).list();
			if (!(workPackageTestCaseExecutionPlans == null || workPackageTestCaseExecutionPlans.isEmpty())) {
				
				for(WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan : workPackageTestCaseExecutionPlans) {
					if(workPackageTestCaseExecutionPlan.getWorkPackage() != null){
						Hibernate.initialize(workPackageTestCaseExecutionPlan.getWorkPackage());
						if(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild() != null){
							Hibernate.initialize(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild());
							if(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion() != null){
								Hibernate.initialize(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion());
							}
						}
					}
					if(setIsSelected){
						testCaseList = workPackageTestCase.getTestCase();
						workPackageTestCase.setTestCase(testCaseList);
						workPackageTestCase.setIsSelected(0);
						updateWorkPackageTestCase(workPackageTestCase);
					}
				}
			}
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return workPackageTestCaseExecutionPlans;
	}

	@Override
	@Transactional
	public WorkPackageTestCaseExecutionPlanStatusDTO listWorkpackageTestCaseExecutionPlanningStatus(
			int workPackageId) {
		WorkPackageTestCaseExecutionPlanStatusDTO workPackageTestCaseExecutionPlanStatus = new WorkPackageTestCaseExecutionPlanStatusDTO();
		int activeTestcaseCount=0;
		int inActiveTestcaseCount=0;
		int totalTestcaseCount=0;
		int testLeadCount=0;
		int testerCount=0;
		int plannedexecutionDateCount=0;
		try {
			List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlans = getWorkPackageTestCaseExecutionPlanByWorkpackgeId(workPackageId);
			if(workPackageTestCaseExecutionPlans!=null && !workPackageTestCaseExecutionPlans.isEmpty()){
				for(WorkPackageTestCaseExecutionPlan dl:workPackageTestCaseExecutionPlans){
					if(dl.getPlannedExecutionDate()!=null && dl.getStatus() !=null && dl.getStatus().equals(1))
						plannedexecutionDateCount++;
					if(dl.getTester()!=null && dl.getStatus() !=null && dl.getStatus().equals(1))
						testerCount++;
					if(dl.getTestLead()!=null && dl.getStatus() !=null &&  dl.getStatus().equals(1))
						testLeadCount++;
					if(dl.getStatus() !=null && dl.getStatus().equals(1))
						activeTestcaseCount++;
					if(dl.getStatus() !=null && dl.getStatus().equals(0))
						inActiveTestcaseCount++;
					if(dl.getStatus() !=null && (dl.getStatus().equals(0) || dl.getStatus().equals(1)))
						totalTestcaseCount++;
				}
			}
			
			List<WorkPackageFeatureExecutionPlan> workPackageFeatureExecutionPlans= getWorkPackageFeatureExecutionPlanByWorkpackgeId(workPackageId);

			if(workPackageFeatureExecutionPlans!=null && !workPackageFeatureExecutionPlans.isEmpty())
				workPackageTestCaseExecutionPlanStatus.setTotalFeatureCount(workPackageFeatureExecutionPlans.size());
			else
				workPackageTestCaseExecutionPlanStatus.setTotalFeatureCount(0);
			
			List<WorkPackageTestSuiteExecutionPlan> workPackageTestSuiteExecutionPlans= getWorkPackageTestSuiteExecutionPlanByWorkpackgeId(workPackageId);

			if(workPackageTestSuiteExecutionPlans!=null && !workPackageTestSuiteExecutionPlans.isEmpty())
				workPackageTestCaseExecutionPlanStatus.setTotalTestSuiteCount(workPackageTestSuiteExecutionPlans.size());
			else
				workPackageTestCaseExecutionPlanStatus.setTotalTestSuiteCount(0);

			workPackageTestCaseExecutionPlanStatus.setAssignedTesterCount(testerCount);
			workPackageTestCaseExecutionPlanStatus.setAssignedTestLeadCount(testLeadCount);
			workPackageTestCaseExecutionPlanStatus.setActiveTestCaseCount(activeTestcaseCount);
			workPackageTestCaseExecutionPlanStatus.setInActiveTestCaseCount(inActiveTestcaseCount);
			workPackageTestCaseExecutionPlanStatus.setTotalTestCaseCount(totalTestcaseCount);
			workPackageTestCaseExecutionPlanStatus.setPlannedExecutionDateCount(plannedexecutionDateCount);
			workPackageTestCaseExecutionPlanStatus.setWorkPackageId(workPackageId);
			workPackageTestCaseExecutionPlanStatus.setWorkPackageName(getWorkPackageById(workPackageId).getName());
			
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return workPackageTestCaseExecutionPlanStatus;
	}
	
	@Override
	@Transactional
	public List<WorkPackageTestCaseExecutionPlan> getWorkPackageTestCaseExecutionPlanByWorkpackgeId(int workPackageId) {
		log.debug("listing getWorkpackageTestcaseExecutionPlanById instance");
		List<WorkPackageTestCaseExecutionPlan> list = null;
		try {
			list = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from WorkPackageTestCaseExecutionPlan where workPackage.workPackageId = "
									+ workPackageId).list();
			for(WorkPackageTestCaseExecutionPlan dl:list){			
			
					Hibernate.initialize(dl.getTestCase().getProductMaster());
					if(dl.getWorkPackage() != null){
						Hibernate.initialize(dl.getWorkPackage());
						if(dl.getWorkPackage().getProductBuild() != null){
							Hibernate.initialize(dl.getWorkPackage().getProductBuild());
							if(dl.getWorkPackage().getProductBuild().getProductVersion() != null){
								Hibernate.initialize(dl.getWorkPackage().getProductBuild().getProductVersion());
							}
						}
					}
					Hibernate.initialize(dl.getWorkPackage().getProductBuild().getProductVersion().getProductMaster());
					Hibernate.initialize(dl.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductMode());
					Hibernate.initialize(dl.getTestCase());
					Hibernate.initialize(dl.getTester());
					Hibernate.initialize(dl.getTestLead());
					Hibernate.initialize(dl.getTestCase().getDecouplingCategory());
					Hibernate.initialize(dl.getTestSuiteList());
					Hibernate.initialize(dl.getTestCase().getTestcaseTypeMaster());
					Hibernate.initialize(dl.getTestCase().getTestCasePriority());
				
					Hibernate.initialize(dl.getRunConfiguration());
					Hibernate.initialize(dl.getRunConfiguration().getRunconfiguration());
					Hibernate.initialize(dl.getRunConfiguration().getWorkpackage());
			}

		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return list;
	}

	@Override
	@Transactional
	public List<WorkPackageTestCaseExecutionPlan> listWorkPackageTestCasesExecutionPlan(
			int workPackageId, int jtStartIndex, int jtPageSize,
			ProductUserRole productUserRole) {
		boolean isTester=false;
		if(productUserRole!=null && productUserRole.getRole().getUserRoleId()== Integer.parseInt(TAFConstants.ROLE_TESTER))
			isTester=true;
		String query="from WorkPackageTestCaseExecutionPlan wptcep where wptcep.workPackage.workPackageId = "
				+ workPackageId  + " order by wptcep.testCase.testCaseId asc";
		if(isTester){
			query="from WorkPackageTestCaseExecutionPlan wptcep where wptcep.workPackage.workPackageId = "
				+ workPackageId  + "and wptcep.tester.userId="+productUserRole.getUser().getUserId()
				+ " order by wptcep.testCase.testCaseId asc";
		}
				List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlan = null;
				try {
					workPackageTestCaseExecutionPlan = sessionFactory
							.getCurrentSession()
							.createQuery(query)
							.setFirstResult(jtStartIndex).setMaxResults(jtPageSize).list();
					if (!(workPackageTestCaseExecutionPlan == null || workPackageTestCaseExecutionPlan
							.isEmpty())) {
						for (WorkPackageTestCaseExecutionPlan dl : workPackageTestCaseExecutionPlan) {
							Hibernate.initialize(dl.getTestCase());
							Hibernate.initialize(dl.getWorkPackage());
							if(dl.getWorkPackage() != null){
								Hibernate.initialize(dl.getWorkPackage());
								if(dl.getWorkPackage().getProductBuild() != null){
									Hibernate.initialize(dl.getWorkPackage().getProductBuild());
									if(dl.getWorkPackage().getProductBuild().getProductVersion() != null){
										Hibernate.initialize(dl.getWorkPackage().getProductBuild().getProductVersion());
									}
								}
							}
							Hibernate.initialize(dl.getEnvironmentList());
							Hibernate.initialize(dl.getTester());
							Hibernate.initialize(dl.getTestLead());
							Hibernate.initialize(dl.getTestCaseExecutionResult());
							Hibernate.initialize(dl.getActualWorkShiftMaster());
							Hibernate.initialize(dl.getPlannedWorkShiftMaster());
							Hibernate.initialize(dl.getTestCase().getDecouplingCategory());
							Hibernate.initialize(dl.getTestCase().getTestCasePriority());
							Hibernate.initialize(dl.getTestCase().getTestcaseTypeMaster());
							Hibernate.initialize(dl.getTestCase().getTestCasePriority());
							Hibernate.initialize(dl.getTestCase().getProductFeature());
							if(dl.getTestCase().getProductMaster()!=null){
								Hibernate.initialize(dl.getTestCase().getProductMaster().getProductName());
								Hibernate.initialize(dl.getTestCase().getProductMaster().getProductMode().getModeName());
							}
							Hibernate.initialize(dl.getTestSuiteList());
							Hibernate.initialize(dl.getTestSuiteList());

							Hibernate.initialize(dl.getRunConfiguration());
							Hibernate.initialize(dl.getRunConfiguration().getRunconfiguration());
							Hibernate.initialize(dl.getRunConfiguration().getWorkpackage());
						}
					}
				} catch (RuntimeException re) {
					log.error("list failed", re);
				}
				return workPackageTestCaseExecutionPlan;
	}

	@Override
	@Transactional
	public List<WorkPackageDemandProjectionDTO> listWorkpackageDemandProdjectionByWorkpackageId(Date startDate, Date endDate, Integer testFactoryLabId, Integer testFactoryId, Integer productId, Integer workPackageId, Integer shiftId,String viewType) {
		log.debug("listing listWorkpackageDemandProdjectionByWorkpackageId");
		List<WorkPackageDemandProjectionDTO> listWorkPackageDemandProjectionDTO = new ArrayList<WorkPackageDemandProjectionDTO>();
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackageDemandProjection.class, "wpdp");
			c.createAlias("wpdp.workPackage", "workPackage");
			c.createAlias("workPackage.productBuild", "productbuild");
			c.createAlias("productbuild.productVersion", "productVersion");
			c.createAlias("productVersion.productMaster", "product");
			c.createAlias("product.testFactory", "tf");
			c.createAlias("tf.testFactoryLab", "tfl");
			
			ProjectionList projectionList = Projections.projectionList();
			projectionList = Projections.projectionList();
			
			if (shiftId != null && shiftId!=-1) {
				c.createAlias("wpdp.workShiftMaster", "shift");
				c.add(Restrictions.eq("shift.shiftId", shiftId.intValue()));
			}
			
			if (workPackageId != null) {
				c.add(Restrictions.eq("workPackage.workPackageId", workPackageId));
			} 
			if (productId != null) {
				
				c.add(Restrictions.eq("product.productId", productId));
			} 
			if (testFactoryId != null) {
								
				c.add(Restrictions.eq("tf.testFactoryId", testFactoryId));
			} 
			if (testFactoryLabId != null) {
				c.add(Restrictions.eq("tfl.testFactoryLabId", testFactoryLabId));
			}
			if(viewType != null){
				if(viewType.equalsIgnoreCase(IDPAConstants.MONTHLY_VIEW_TYPE)){
					c.add(Restrictions.ge("wpdp.workDate", startDate));
					c.add(Restrictions.le("wpdp.workDate", endDate));
				}
			}
			
			projectionList.add(Property.forName("workDate"));
			projectionList.add(Projections.sum("wpdp.resourceCount").as("resourceCount"));
			projectionList.add(Projections.groupProperty("wpdp.workDate"));
			
			c.setProjection(projectionList);
			
			List<Object[]> list = c.list();
			
			WorkPackageDemandProjectionDTO workPackageDemandProjectionDTO = null;
			for (Object[] row : list) {
				workPackageDemandProjectionDTO = new WorkPackageDemandProjectionDTO();
				
				workPackageDemandProjectionDTO.setWorkDate(((Date)row[0]));
				workPackageDemandProjectionDTO.setResourceCount(((Float)row[1]));
				listWorkPackageDemandProjectionDTO.add(workPackageDemandProjectionDTO);
			}
			
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}catch(Exception e){
			e.printStackTrace();
		}
		return listWorkPackageDemandProjectionDTO;
	}

	@Override
	@Transactional
	public List<WorkPackage> getWorkPackagesByProductId(int productId) {
		List<WorkPackage> listOfWorkPackages = new  ArrayList<WorkPackage>();
		
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackage.class, "wp");
			c.createAlias("wp.productBuild", "prodBuild");
			c.createAlias("prodBuild.productVersion", "prodVersion");
			c.createAlias("prodVersion.productMaster", "product");
			c.add(Restrictions.eq("product.productId", productId));
			c.add(Restrictions.eq("wp.isActive", 1));
			listOfWorkPackages = c.list();
			if (!listOfWorkPackages.isEmpty()) {
				for (WorkPackage workPackage : listOfWorkPackages) {
					Hibernate.initialize(workPackage.getProductBuild().getProductVersion().getProductMaster());
					Hibernate.initialize(workPackage.getWorkFlowEvent());
					if(workPackage.getWorkFlowEvent()!=null){
					Hibernate.initialize(workPackage.getWorkFlowEvent().getWorkFlow());
					}
				}
			}
			log.debug("list successful");
		} catch (Exception e) {
			log.error("list failed", e);
		}
		return listOfWorkPackages;
	}
	
	
	@Override
	@Transactional
	public int getWorkPackagesCountByProductId(int productId) {
		log.debug("getting ProductsWorkPackagesCount");
		int count = 0;
		try {
			count=((Number) sessionFactory.getCurrentSession().createSQLQuery("select count(workPackageId) from workpackage wp "
					+ "inner join product_build pb on pb.productBuildId =  wp.productBuildId "
					+ "inner join product_version_list_master ver on ver.productVersionListId = pb.productVersionId "
					+ "inner join product_master prod on prod.productId = ver.productId where prod.productId=:prodId")
					.setParameter("prodId", productId)
					.uniqueResult()).intValue();

			log.debug("total ProductsWorkPackagesCount fetch successful");
		} catch (RuntimeException re) {
			log.error("total ProductsWorkPackagesCount fetch failed", re);
		}
		return count;
	}
	
	@Override
	@Transactional
	public int getWorkPackagesCountByBuildId(int productBuildId) {
		log.debug("getting BuildsWorkPackagesCount");
		int count = 0;
		try {
			count=((Number) sessionFactory.getCurrentSession().createSQLQuery("select count(workPackageId) from workpackage wp "
					+ "inner join product_build pb on pb.productBuildId =  wp.productBuildId where pb.productBuildId=:buildId")
					.setParameter("buildId", productBuildId)
					.uniqueResult()).intValue();

			log.debug("total BuildsWorkPackagesCount fetch successful");
		} catch (RuntimeException re) {
			log.error("total BuildsWorkPackagesCount fetch failed", re);
		}
		return count;
	}

	@Override
	@Transactional
	public List<WorkPackage> getUserAssociatedWorkPackages(int userRoleId, int userId) {
		List<WorkPackage> listOfWorkPackages = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackage.class, "wp").setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			c.createAlias("wp.workPackageTestCaseExecutionPlan", "wptcep");
			c.add(Restrictions.eq("wp.status", "1"));
			if(userRoleId == IDPAConstants.ROLE_ID_TEST_LEAD){
				c.createAlias("wptcep.testLead", "testLead");
				c.add(Restrictions.eq("testLead.userId", userId));
			}else if(userRoleId == IDPAConstants.ROLE_ID_TESTER){
				c.createAlias("wptcep.tester", "tester");
				c.add(Restrictions.eq("tester.userId", userId));
			}
			listOfWorkPackages = c.list();
		} catch (Exception e) {
			log.error("Getting Work Packages with which the user is associated", e);
		}
		return listOfWorkPackages;
	}

	@Override
	@Transactional
	public List<TestFactoryResourceReservation> getUserByBlockedStatus(int workPackageId, int roleId,String plannedExecutionDate,int shiftId) {
		List<TestFactoryResourceReservation> testFactoryResourceReservationList = new ArrayList<TestFactoryResourceReservation>();
		Criteria c = sessionFactory.getCurrentSession().createCriteria(TestFactoryResourceReservation.class, "tfrr");
		c.createAlias("tfrr.workPackage", "wp");
		c.createAlias("tfrr.shift", "shift");
		c.createAlias("tfrr.blockedUser", "user");
		c.createAlias("user.userRoleMaster", "role");
		c.add(Restrictions.eq("wp.workPackageId", workPackageId));
		if(plannedExecutionDate!=null && !plannedExecutionDate.equals("") && plannedExecutionDate.contains("/")){
			c.add(Restrictions.eq("tfrr.reservationDate", DateUtility.dateformatWithOutTime(plannedExecutionDate)));
		}else{
			if(plannedExecutionDate!=null && !plannedExecutionDate.equals(""))
				c.add(Restrictions.eq("tfrr.reservationDate", DateUtility.dateFormatWithOutSeconds(plannedExecutionDate)));
		}
		
			
		if(shiftId!=0)
			c.add(Restrictions.eq("shift.shiftId", shiftId));
		if(roleId!=0 && roleId!=10){
			if(roleId==4){
				c.add(Restrictions.in("role.userRoleId",  Arrays.asList(4,3)));
			}else if(roleId==5){
				c.add(Restrictions.in("role.userRoleId",  Arrays.asList(5,4)));
			}
		}
		testFactoryResourceReservationList = c.list();
		if(testFactoryResourceReservationList!=null && !testFactoryResourceReservationList.isEmpty()){
			for(TestFactoryResourceReservation testFactoryResourceReservation:testFactoryResourceReservationList){
				Hibernate.initialize(testFactoryResourceReservation.getBlockedUser());
				Hibernate.initialize(testFactoryResourceReservation.getBlockedUser().getUserRoleMaster());
			}
		}
		return testFactoryResourceReservationList;
	}

	@Override
	@Transactional
	public List<JsonUserList> getUserByBlockedStatusPerform(int workPackageId, int roleId,String plannedExecutionDate,int shiftId, int requiredRoleId, int productId) {
		List<TestFactoryResourceReservation> testFactoryResourceReservationList = new ArrayList<TestFactoryResourceReservation>();
		Criteria c = sessionFactory.getCurrentSession().createCriteria(TestFactoryResourceReservation.class, "tfrr");
		c.createAlias("tfrr.workPackage", "wp");
		c.createAlias("tfrr.shift", "shift");
		c.createAlias("tfrr.blockedUser", "user");
		c.createAlias("user.userRoleMaster", "role");		
		c.add(Restrictions.eq("wp.workPackageId", workPackageId));
		if(plannedExecutionDate!=null && !plannedExecutionDate.equals("") && plannedExecutionDate.contains("/")){
			c.add(Restrictions.eq("tfrr.reservationDate", DateUtility.dateformatWithOutTime(plannedExecutionDate)));
		}else{
			if(plannedExecutionDate!=null && !plannedExecutionDate.equals(""))
				c.add(Restrictions.eq("tfrr.reservationDate", DateUtility.dateFormatWithOutSeconds(plannedExecutionDate)));
		}			
		if(shiftId!=0)
			c.add(Restrictions.eq("shift.shiftId", shiftId));		
		
		testFactoryResourceReservationList = c.list();
		List<JsonUserList> jsonUserList = new ArrayList<JsonUserList>();
		JsonUserList jsonUser = null;
	
		if(testFactoryResourceReservationList!=null && !testFactoryResourceReservationList.isEmpty()){
			for(TestFactoryResourceReservation testFactoryResourceReservation:testFactoryResourceReservationList){
				Hibernate.initialize(testFactoryResourceReservation.getBlockedUser());
				Hibernate.initialize(testFactoryResourceReservation.getBlockedUser().getUserRoleMaster());
				int productSpecificRoleId = 0;
				if(testFactoryResourceReservation.getBlockedUser().getProductUserRoleSet() != null && !testFactoryResourceReservation.getBlockedUser().getProductUserRoleSet().isEmpty()){
					Set<ProductUserRole> prodUR = testFactoryResourceReservation.getBlockedUser().getProductUserRoleSet();
					Hibernate.initialize(testFactoryResourceReservation.getBlockedUser().getProductUserRoleSet());	
					 productSpecificRoleId = testFactoryProductCoreResourcesDao.getProductRoleOfProductCoreResource(testFactoryResourceReservation.getBlockedUser().getUserId(), productId,plannedExecutionDate);
				}
				
				if(productSpecificRoleId != 0){
					// Not a product core resource
					if(requiredRoleId == 4 && (productSpecificRoleId == 3 || productSpecificRoleId== 4)){
						// Test Manager or Test Lead
						jsonUser = new JsonUserList(testFactoryResourceReservation.getBlockedUser());
						jsonUserList.add(jsonUser);
					}else if(requiredRoleId == 5 && (productSpecificRoleId == 4 || productSpecificRoleId == 5)){
						// Test lead or Tester						
						jsonUser = new JsonUserList(testFactoryResourceReservation.getBlockedUser());
						jsonUserList.add(jsonUser);
					}else{
						continue;
					}
				}else{
					if(requiredRoleId == 4 && (testFactoryResourceReservation.getBlockedUser().getUserRoleMaster().getUserRoleId() == 3 || testFactoryResourceReservation.getBlockedUser().getUserRoleMaster().getUserRoleId()== 4)){
						// Test Manager or Test Lead
						jsonUser = new JsonUserList(testFactoryResourceReservation.getBlockedUser());
						jsonUserList.add(jsonUser);
					}else if(requiredRoleId == 5 && (testFactoryResourceReservation.getBlockedUser().getUserRoleMaster().getUserRoleId() == 4 || testFactoryResourceReservation.getBlockedUser().getUserRoleMaster().getUserRoleId() == 5)){
						// Test lead or Tester
						jsonUser = new JsonUserList(testFactoryResourceReservation.getBlockedUser());
						jsonUserList.add(jsonUser);
					}else{
						continue;
					}
				}	
				productSpecificRoleId = 0;
			}
		}
		return jsonUserList;
	}
	@Override
	@Transactional
	public List<WorkPackageTestCaseExecutionPlan> listWorkPackageTestCasesExecutionPlanTesterByEnv(
			int workPackageId, int jtStartIndex, int jtPageSize, UserList user,
			int envId) {
		log.debug("listing listWorkPackageTestCasesExecutionPlanTesterByEnv instance");
		List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlan = null;
		try {
			workPackageTestCaseExecutionPlan = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from WorkPackageTestCaseExecutionPlan wptcep where wptcep.workPackage.workPackageId = "
									+ workPackageId + "and wptcep.tester.userId = "+user.getUserId()
									+ "and wptcep.environment.environmentId = "+envId
									+ " order by wptcep.testCase.testCaseId asc").
					setFirstResult(jtStartIndex).setMaxResults(jtPageSize).list();
			if (!(workPackageTestCaseExecutionPlan == null || workPackageTestCaseExecutionPlan
					.isEmpty())) {
				for (WorkPackageTestCaseExecutionPlan dl : workPackageTestCaseExecutionPlan) {
					Hibernate.initialize(dl.getTestCase());
					Hibernate.initialize(dl.getWorkPackage());
					if(dl.getWorkPackage() != null){
						Hibernate.initialize(dl.getWorkPackage());
						if(dl.getWorkPackage().getProductBuild() != null){
							Hibernate.initialize(dl.getWorkPackage().getProductBuild());
							if(dl.getWorkPackage().getProductBuild().getProductVersion() != null){
								Hibernate.initialize(dl.getWorkPackage().getProductBuild().getProductVersion());
							}
						}
					}
					Hibernate.initialize(dl.getTester());
					Hibernate.initialize(dl.getTestLead());
					Hibernate.initialize(dl.getTestCaseExecutionResult());
					Hibernate.initialize(dl.getActualWorkShiftMaster());
					Hibernate.initialize(dl.getPlannedWorkShiftMaster());
					Hibernate.initialize(dl.getTestSuiteList());
					Hibernate.initialize(dl.getTestCase().getTestcaseTypeMaster());
					Hibernate.initialize(dl.getTestCase().getTestCasePriority());
				
					Hibernate.initialize(dl.getRunConfiguration());
					Hibernate.initialize(dl.getRunConfiguration().getRunconfiguration());
					Hibernate.initialize(dl.getRunConfiguration().getWorkpackage());
				}
			}
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return workPackageTestCaseExecutionPlan;
	}

	@Override
	@Transactional
	public WorkPackageTestCaseExecutionPlan listWorkPackageTestCasesExecutionPlan(
			int wptcepId, int testcaseId) {
		List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlanList = null;
		WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan=null;
		try {
			workPackageTestCaseExecutionPlanList = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from WorkPackageTestCaseExecutionPlan wptcep where wptcep.id = "
									+ wptcepId 
									+ "and wptcep.testCase.testCaseId = "+testcaseId).list();
			
			workPackageTestCaseExecutionPlan = (workPackageTestCaseExecutionPlanList != null && workPackageTestCaseExecutionPlanList.size() != 0) ? (WorkPackageTestCaseExecutionPlan) workPackageTestCaseExecutionPlanList
					.get(0) : null;
			if(workPackageTestCaseExecutionPlan!=null){
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getTestCase());
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getWorkPackage());
					if(workPackageTestCaseExecutionPlan.getWorkPackage() != null){
						Hibernate.initialize(workPackageTestCaseExecutionPlan.getWorkPackage());
						if(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild() != null){
							Hibernate.initialize(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild());
							if(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion() != null){
								Hibernate.initialize(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion());
								if(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion().getProductMaster()!=null){
									Hibernate.initialize(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion().getProductMaster());
								}
							}
						}
					}
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getEnvironmentList());
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getTester());
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getTestLead());
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getTestCaseExecutionResult());
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getActualWorkShiftMaster());
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getPlannedWorkShiftMaster());
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getTestCase().getTestCasePriority());
					if(workPackageTestCaseExecutionPlan.getTestCase().getTestCaseType() != null){
						Hibernate.initialize(workPackageTestCaseExecutionPlan.getTestCase().getTestCaseType());	
					}
					if(workPackageTestCaseExecutionPlan.getHostList() != null){
						Hibernate.initialize(workPackageTestCaseExecutionPlan.getHostList().getHostName());
					}

					Hibernate.initialize(workPackageTestCaseExecutionPlan.getTestSuiteList());
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getTestCase().getTestcaseTypeMaster());
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getTestCase().getTestCasePriority());
				
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getRunConfiguration());
					if(workPackageTestCaseExecutionPlan.getRunConfiguration()!=null){
						Hibernate.initialize(workPackageTestCaseExecutionPlan.getRunConfiguration().getRunconfiguration());
						Hibernate.initialize(workPackageTestCaseExecutionPlan.getRunConfiguration().getRunconfiguration().getGenericDevice());
						Hibernate.initialize(workPackageTestCaseExecutionPlan.getRunConfiguration().getWorkpackage());
					}
					
					if(workPackageTestCaseExecutionPlan.getWorkPackage() != null){
						Hibernate.initialize(workPackageTestCaseExecutionPlan.getWorkPackage());
						if(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild() != null){
							Hibernate.initialize(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild());
							if(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion() != null){
								Hibernate.initialize(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion());
								if(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion().getProductMaster()!=null){
									Hibernate.initialize(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion().getProductMaster());
								}
							}
						}
					}
			}
			
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return workPackageTestCaseExecutionPlan;	
	}

	@Override
	@Transactional
	public WorkPackageTestCaseExecutionPlan listWorkPackageTestCasesExecutionPlanOfTCId(int testcaseId) {
		List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlanList = null;
		WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan=null;
		try {
			workPackageTestCaseExecutionPlanList = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from WorkPackageTestCaseExecutionPlan wptcep where "									
									+ "wptcep.testCase.testCaseId = "+testcaseId).list();
			
			workPackageTestCaseExecutionPlan = (workPackageTestCaseExecutionPlanList != null && workPackageTestCaseExecutionPlanList.size() != 0) ? (WorkPackageTestCaseExecutionPlan) workPackageTestCaseExecutionPlanList
					.get(0) : null;
			if(workPackageTestCaseExecutionPlan!=null){
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getTestCase());
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getWorkPackage());
					if(workPackageTestCaseExecutionPlan.getWorkPackage() != null){
						Hibernate.initialize(workPackageTestCaseExecutionPlan.getWorkPackage());
						if(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild() != null){
							Hibernate.initialize(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild());
							if(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion() != null){
								Hibernate.initialize(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion());
								if(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion().getProductMaster()!=null){
									Hibernate.initialize(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion().getProductMaster());
								}
							}
						}
					}
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getEnvironmentList());
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getTester());
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getTestLead());
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getTestCaseExecutionResult());
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getActualWorkShiftMaster());
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getPlannedWorkShiftMaster());
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getTestCase().getTestCasePriority());
					if(workPackageTestCaseExecutionPlan.getTestCase().getTestCaseType() != null){
						Hibernate.initialize(workPackageTestCaseExecutionPlan.getTestCase().getTestCaseType());	
					}
					if(workPackageTestCaseExecutionPlan.getHostList() != null){
						Hibernate.initialize(workPackageTestCaseExecutionPlan.getHostList().getHostName());
					}

					Hibernate.initialize(workPackageTestCaseExecutionPlan.getTestSuiteList());
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getTestCase().getTestcaseTypeMaster());
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getTestCase().getTestCasePriority());
				
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getRunConfiguration());
					if(workPackageTestCaseExecutionPlan.getRunConfiguration()!=null){
						Hibernate.initialize(workPackageTestCaseExecutionPlan.getRunConfiguration().getRunconfiguration());
						Hibernate.initialize(workPackageTestCaseExecutionPlan.getRunConfiguration().getRunconfiguration().getGenericDevice());
						Hibernate.initialize(workPackageTestCaseExecutionPlan.getRunConfiguration().getWorkpackage());
					}
					
					if(workPackageTestCaseExecutionPlan.getWorkPackage() != null){
						Hibernate.initialize(workPackageTestCaseExecutionPlan.getWorkPackage());
						if(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild() != null){
							Hibernate.initialize(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild());
							if(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion() != null){
								Hibernate.initialize(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion());
								if(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion().getProductMaster()!=null){
									Hibernate.initialize(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion().getProductMaster());
								}
							}
						}
					}
			}
			
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return workPackageTestCaseExecutionPlan;	
	}
	
	@Override
	@Transactional
	public WorkPackageTestCaseExecutionPlan listWorkPackageTestCasesExecutionPlanOfTCExeId(int wptcepId) {
		List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlanList = null;
		WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan=null;
		try {
			workPackageTestCaseExecutionPlanList = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from WorkPackageTestCaseExecutionPlan wptcep where "									
									+ "wptcep.id = "+wptcepId).list();
			
			workPackageTestCaseExecutionPlan = (workPackageTestCaseExecutionPlanList != null && workPackageTestCaseExecutionPlanList.size() != 0) ? (WorkPackageTestCaseExecutionPlan) workPackageTestCaseExecutionPlanList
					.get(0) : null;
			if(workPackageTestCaseExecutionPlan!=null){
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getTestCase());
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getWorkPackage());
					if(workPackageTestCaseExecutionPlan.getTestRunJob() != null){
						Hibernate.initialize(workPackageTestCaseExecutionPlan.getTestRunJob());	
					}
					
					if(workPackageTestCaseExecutionPlan.getWorkPackage() != null){
						Hibernate.initialize(workPackageTestCaseExecutionPlan.getWorkPackage());
						if(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild() != null){
							Hibernate.initialize(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild());
							if(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion() != null){
								Hibernate.initialize(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion());
								if(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion().getProductMaster()!=null){
									Hibernate.initialize(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion().getProductMaster());
									if(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductType()!=null){
										Hibernate.initialize(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductType());
									}
								}
							}
						}
					}
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getEnvironmentList());
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getTester());
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getTestLead());
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getTestCaseExecutionResult());
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getActualWorkShiftMaster());
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getPlannedWorkShiftMaster());
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getTestCase().getTestCasePriority());
					if(workPackageTestCaseExecutionPlan.getTestCase().getTestCaseType() != null){
						Hibernate.initialize(workPackageTestCaseExecutionPlan.getTestCase().getTestCaseType());	
					}
					if(workPackageTestCaseExecutionPlan.getHostList() != null){
						Hibernate.initialize(workPackageTestCaseExecutionPlan.getHostList().getHostName());
					}

					Hibernate.initialize(workPackageTestCaseExecutionPlan.getTestSuiteList());
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getTestCase().getTestcaseTypeMaster());
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getTestCase().getTestCasePriority());
				
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getRunConfiguration());
					if(workPackageTestCaseExecutionPlan.getRunConfiguration()!=null){
						Hibernate.initialize(workPackageTestCaseExecutionPlan.getRunConfiguration().getRunconfiguration());
						if(workPackageTestCaseExecutionPlan.getRunConfiguration().getRunconfiguration().getGenericDevice() != null){

							Hibernate.initialize(workPackageTestCaseExecutionPlan.getRunConfiguration().getRunconfiguration().getGenericDevice());
							Hibernate.initialize(workPackageTestCaseExecutionPlan.getRunConfiguration().getRunconfiguration().getGenericDevice().getPlatformType());
							if(workPackageTestCaseExecutionPlan.getRunConfiguration().getRunconfiguration().getGenericDevice().getHostList() != null){
								Hibernate.initialize(workPackageTestCaseExecutionPlan.getRunConfiguration().getRunconfiguration().getGenericDevice().getHostList());
							}
							if(workPackageTestCaseExecutionPlan.getRunConfiguration().getRunconfiguration().getGenericDevice().getDeviceModelMaster() != null){
								Hibernate.initialize(workPackageTestCaseExecutionPlan.getRunConfiguration().getRunconfiguration().getGenericDevice().getDeviceModelMaster());	
							}
						}						
						Hibernate.initialize(workPackageTestCaseExecutionPlan.getRunConfiguration().getWorkpackage());
					}
					
					if(workPackageTestCaseExecutionPlan.getWorkPackage() != null){
						Hibernate.initialize(workPackageTestCaseExecutionPlan.getWorkPackage());
						if(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild() != null){
							Hibernate.initialize(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild());
							if(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion() != null){
								Hibernate.initialize(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion());
								if(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion().getProductMaster()!=null){
									Hibernate.initialize(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion().getProductMaster());
								}
							}
						}
					}
					if(workPackageTestCaseExecutionPlan.getTestCase().getExecutionTypeMaster() != null){
						Hibernate.initialize(workPackageTestCaseExecutionPlan.getTestCase().getExecutionTypeMaster());
					}
					if(workPackageTestCaseExecutionPlan.getFeature() != null){
						Hibernate.initialize(workPackageTestCaseExecutionPlan.getFeature());
					}
			}
			
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
			// throw re;
		}
		return workPackageTestCaseExecutionPlan;	
	}
	@Override
	@Transactional
	public List<TestExecutionResultBugList> listDefectsByTestcaseExecutionPlanId(
			int tcerId,int jtStartIndex,int jtPageSize) {
		List<TestExecutionResultBugList> testExecutionResultBugList = new ArrayList<TestExecutionResultBugList>();
		Criteria c = sessionFactory.getCurrentSession().createCriteria(TestExecutionResultBugList.class, "terbl");
		c.createAlias("terbl.testCaseExecutionResult", "tcer");
		
		c.add(Restrictions.eq("tcer.testCaseExecutionResultId", tcerId));
		c.addOrder(Order.desc("tcer.testCaseExecutionResultId"));
		testExecutionResultBugList = c.list();
		if(testExecutionResultBugList!=null && !testExecutionResultBugList.isEmpty()){
			for(TestExecutionResultBugList defect:testExecutionResultBugList){
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
		return testExecutionResultBugList;
	}

	@Override
	@Transactional
	public void addTestCaseDefect(
			TestExecutionResultBugList testExecutionResultBugList) {
		log.debug("adding TestExecutionResultBugList instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(testExecutionResultBugList);
			log.debug("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}
		
	}

	@Override
	@Transactional
	public void updateTestCaseDefect(
			TestExecutionResultBugList testExecutionResultBugList) {
		log.debug("adding TestExecutionResultBugList instance");
		try {
			sessionFactory.getCurrentSession().update(testExecutionResultBugList);
			log.debug("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}
		
	}

	@Override
	@Transactional
	public List<WorkFlow> getworkFlowListByEntity(int entityType) {
		List<WorkFlow> workFlowList = new ArrayList<WorkFlow>();
		Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkFlow.class, "wf");

		c.createAlias("wf.entityMaster", "e");

		c.add(Restrictions.eq("e.entitymasterid", entityType));
		
		workFlowList = c.list();
		if(workFlowList!=null && !workFlowList.isEmpty()){
			for(WorkFlow workFlow:workFlowList){
			}
		}
		return workFlowList;
	}

	@Override
	@Transactional
	public List<TestCaseExecutionResult> listResultsByTestcaseExecutionPlanId(
			int tcerId) {
		List<TestCaseExecutionResult> testCaseExecutionResultList = new ArrayList<TestCaseExecutionResult>();
		Criteria c = sessionFactory.getCurrentSession().createCriteria(TestCaseExecutionResult.class, "tcer");
		
		c.add(Restrictions.eq("tcer.testCaseExecutionResultId", tcerId));
		
		testCaseExecutionResultList = c.list();
		if(testCaseExecutionResultList!=null && !testCaseExecutionResultList.isEmpty()){
			for(TestCaseExecutionResult testCaseExecutionResult:testCaseExecutionResultList){
				Hibernate.initialize(testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan());
			}
		}
		return testCaseExecutionResultList;
	}
	
	@Override
	@Transactional
	public WorkPackageTestCaseSummaryDTO listWorkPackageTestCaseExecutionSummary(Integer workPackageId) {
		
		WorkPackageTestCaseSummaryDTO workPackageTestCaseSummaryDTO = new WorkPackageTestCaseSummaryDTO();
		try{
		Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackage.class, "wp");
		
		c.add(Restrictions.eq("wp.workPackageId", workPackageId));
		
		ProjectionList projectionList = Projections.projectionList();			
		
		Object [] row=null;
		List<WorkPackage> wplist = c.list();
		WorkPackage workPackage = (wplist != null && wplist.size() != 0) ? (WorkPackage) wplist.get(0) : null;
		
		if(workPackage!=null){
			Hibernate.initialize(workPackage.getProductBuild());
			Hibernate.initialize(workPackage.getProductBuild().getProductVersion().getProductMaster().getProductName());
			Hibernate.initialize(workPackage.getEnvironmentList());
			Hibernate.initialize(workPackage.getWorkFlowEvent());
			Hibernate.initialize(workPackage.getWorkFlowEvent().getWorkFlow());
			Hibernate.initialize(workPackage.getEnvironmentCombinationList());
			Hibernate.initialize(workPackage.getTestSuiteList());
			Hibernate.initialize(workPackage.getProductFeature());
			Hibernate.initialize(workPackage.getLifeCyclePhase());
			workPackageTestCaseSummaryDTO.setWorkPackageId(workPackage.getWorkPackageId());
			workPackageTestCaseSummaryDTO.setWorkPackageName(workPackage.getName());
			workPackageTestCaseSummaryDTO.setWorkPackageType(workPackage.getWorkPackageType().getName());
			workPackageTestCaseSummaryDTO.setpBuildId(workPackage.getProductBuild().getProductBuildId());
			workPackageTestCaseSummaryDTO.setpBuildName(workPackage.getProductBuild().getBuildname());
			workPackageTestCaseSummaryDTO.setProductId(workPackage.getProductBuild().getProductVersion().getProductMaster().getProductId());
			workPackageTestCaseSummaryDTO.setProductName(workPackage.getProductBuild().getProductVersion().getProductMaster().getProductName());
			workPackageTestCaseSummaryDTO.setProductType(String.valueOf(workPackage.getProductBuild().getProductVersion().getProductMaster().getProductType().getProductTypeId().intValue()));		
			workPackageTestCaseSummaryDTO.setDescription(workPackage.getDescription());
			workPackageTestCaseSummaryDTO.setPlannedStartDate(workPackage.getPlannedStartDate());
			workPackageTestCaseSummaryDTO.setPlannedEndDate(workPackage.getPlannedEndDate());
			workPackageTestCaseSummaryDTO.setActualStartDate(workPackage.getActualStartDate());
			workPackageTestCaseSummaryDTO.setActualEndDate(workPackage.getActualEndDate());
			if(workPackage.getWorkFlowEvent() != null){
				if(workPackage.getWorkFlowEvent().getWorkFlow() != null){
					workPackageTestCaseSummaryDTO.setWorkpackageStatus(workPackage.getWorkFlowEvent().getWorkFlow().getWorkFlowId());
				}				
			}			
			Set<Environment> environmentList= workPackage.getEnvironmentList();
			List<WorkpackageRunConfiguration> workpackageRunConfigurations =getWorkpackageRunConfigurationList(workPackage.getWorkPackageId(), null, null);
			Set<RunConfiguration> configurations = new HashSet<RunConfiguration>();
			for(WorkpackageRunConfiguration wprc:workpackageRunConfigurations){
				configurations.add(wprc.getRunconfiguration());
			}
			workPackageTestCaseSummaryDTO.setRunConfigurations(configurations);
			
			workPackageTestCaseSummaryDTO.setEnvironmentList(environmentList);
			
			workPackageTestCaseSummaryDTO.setEnvironmentCombination(workPackage.getEnvironmentCombinationList());
			if(workPackage.getProductFeature()!=null)
				workPackageTestCaseSummaryDTO.setSelectedFeaturesCount(workPackage.getProductFeature().size());
			else
				workPackageTestCaseSummaryDTO.setSelectedFeaturesCount(0);
			if(workPackage.getTestSuiteList()!=null)
				workPackageTestCaseSummaryDTO.setSelectedTestSuitesCount(workPackage.getTestSuiteList().size());
			else
				workPackageTestCaseSummaryDTO.setSelectedTestSuitesCount(0);
			workPackageTestCaseSummaryDTO.setIterationNo(workPackage.getIterationNumber());
			if (workPackage.getLifeCyclePhase() != null){
				workPackageTestCaseSummaryDTO.setLifecyclePhaseId(workPackage.getLifeCyclePhase().getLifeCyclePhaseId());
			}			
			c.createAlias("wp.productBuild", "wpprodbuild");
			c.createAlias("wpprodbuild.productVersion", "prodbuildversion");
			c.createAlias("prodbuildversion.productMaster", "versionproduct");
			
			c.createAlias("wp.workPackageTestCases", "wptclist");
			c.createAlias("wp.workPackageTestCaseExecutionPlan", "wptceplist");
			
			projectionList.add(Property.forName("wp.workPackageId").as("workPackageId"));
			projectionList.add(Property.forName("wp.name").as("workPackageName"));
			projectionList.add(Property.forName("wpprodbuild.buildname").as("buildName"));
			projectionList.add(Property.forName("versionproduct.productName").as("productName"));
			projectionList.add(Property.forName("wp.plannedStartDate").as("plannedStartDate"));
			projectionList.add(Property.forName("wp.plannedEndDate").as("plannedEndDate"));
			projectionList.add(Property.forName("wp.actualStartDate").as("actualStartDate"));
			projectionList.add(Property.forName("wp.actualEndDate").as("actualEndDate"));	
			projectionList.add(Property.forName("wp.environmentList").as("environmentList"));
			projectionList.add(Property.forName("wp.status").as("status"));
			
			projectionList.add(Projections.groupProperty("workPackageId"));			
			projectionList.add(Projections.groupProperty("wpprodbuild.buildname"));
			projectionList.add(Projections.groupProperty("versionproduct.productName"));
			projectionList.add(Projections.groupProperty("wp.plannedStartDate"));
			projectionList.add(Projections.groupProperty("wp.plannedEndDate"));
			projectionList.add(Projections.groupProperty("wp.actualStartDate"));
			projectionList.add(Projections.groupProperty("wp.actualEndDate"));
			projectionList.add(Projections.groupProperty("wp.environmentList"));
			projectionList.add(Projections.groupProperty("wp.status"));
			projectionList.add(Projections.countDistinct("wptclist.id"));	
			
			c.add(Restrictions.eq("wptclist.isSelected", 1));
			c.setProjection(projectionList);
			
			List<Object[]> list = c.list();
			
			row= (list != null && list.size() != 0) ? (Object[]) list.get(0) : null;
			
			if(row!=null){
				workPackageTestCaseSummaryDTO.setSelectedTestCasesCount((Integer) row[10]);
			}
		}
			
			Criteria c1 = sessionFactory.getCurrentSession().createCriteria(WorkPackageTestCaseExecutionPlan.class, "wptcep");
			c1.createAlias("wptcep.workPackage", "workPackage");
			c1.createAlias("wptcep.testCaseExecutionResult", "tcer");
			c1.createAlias("workPackage.workFlowEvent", "workFlowEvent");
			c1.createAlias("workFlowEvent.workFlow", "workFlow");	
			c1.createAlias("workFlow.entityMaster", "entityMaster");
			
			
			c1.add(Restrictions.eq("workPackage.workPackageId", workPackageId));
			
			
			List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlans = c1.list();
			workPackageTestCaseSummaryDTO.setTotalTestCaseCount(workPackageTestCaseExecutionPlans.size());
	
					
			c1.add(Restrictions.eq("entityMaster.entitymasterid",2));

			c1.add(Restrictions.ge("workFlow.stageValue",30));
			c1.add(Restrictions.eq("wptcep.executionStatus", 2));
	
			ProjectionList projectionList1 = Projections.projectionList();
			projectionList1.add(Property.forName("workPackage.workPackageId").as("workPackageId"));
			projectionList1.add(Property.forName("workPackage.name").as("workPackageName"));	
			projectionList1.add(Projections.count("wptcep.id"));
			projectionList1.add(Property.forName("workFlow.workFlowId").as("workFlowId"));
			projectionList1.add(Projections.groupProperty("workPackage.workPackageId"));
			projectionList1.add(Projections.groupProperty("workFlow.workFlowId"));
			c1.setProjection(projectionList1);
			List<Object[]> list1 = c1.list();
			Object[] row1= (list1 != null && list1.size() != 0) ? (Object[]) list1.get(0) : null;
			if(row1!=null){
				workPackageTestCaseSummaryDTO.setTotalTestCaseForExecutionCount(((Long)row1[2]).intValue());
				workPackageTestCaseSummaryDTO.setStatus((Integer)row1[3]);
			}
			
			/*--WP-TC-WorkFlow Complete StageName list Status---*/
			Criteria cc = sessionFactory.getCurrentSession().createCriteria(WorkFlow.class,"wf");
			cc.add(Restrictions.eq("wf.entityMaster.entitymasterid",2));			
			projectionList = Projections.projectionList();
			projectionList.add(Property.forName("wf.workFlowId").as("workFlowId"));
			projectionList.add(Property.forName("wf.stageName").as("stageName"));		//0			
			cc.setProjection(projectionList);	
			List<Object[]> list2 = cc.list();
			 StringBuffer sb = new StringBuffer();
			 HashMap<Integer, String> workFlowValue=new HashMap();
			for (Object[] value : list2) {
				workFlowValue.put((Integer)value[0], (String)value[1]);
			}
			workPackageTestCaseSummaryDTO.setWorkFlowstageNameList(workFlowValue);
			
			
			c1 = sessionFactory.getCurrentSession().createCriteria(WorkPackageTestCaseExecutionPlan.class, "wptcep");
			c1.createAlias("wptcep.workPackage", "workPackage");
			c1.createAlias("wptcep.testCaseExecutionResult", "tcer");
			c1.createAlias("tcer.testExecutionResultBugListSet", "bugList");
			c1.add(Restrictions.eq("workPackage.workPackageId", workPackageId));
			
			projectionList1 = Projections.projectionList();
			projectionList1.add(Projections.count("bugList.testExecutionResultBugId"));
			projectionList1.add(Property.forName("workPackage.workPackageId").as("workPackageId"));
			projectionList1.add(Projections.groupProperty("workPackage.workPackageId"));
			c1.setProjection(projectionList1);
			List<Object[]> list3 = c1.list();
			Object[] row3= (list3 != null && list3.size() != 0) ? (Object[]) list3.get(0) : null;
			if(row3!=null && row3[0]!=null)
				workPackageTestCaseSummaryDTO.setTotalDefectsCount(((Long)row3[0]).intValue());
			
			c1 = sessionFactory.getCurrentSession().createCriteria(WorkPackageTestCaseExecutionPlan.class, "wptcep");
			c1.createAlias("wptcep.workPackage", "workPackage");
			c1.createAlias("wptcep.testCaseExecutionResult", "tcer");
			c1.add(Restrictions.eq("workPackage.workPackageId", workPackageId));
			c1.add(Restrictions.eq("tcer.isApproved", 1));
			
			projectionList1 = Projections.projectionList();
			projectionList1.add(Projections.count("tcer.isApproved"));
			projectionList1.add(Projections.count("tcer.id"));
			projectionList1.add(Property.forName("workPackage.workPackageId").as("workPackageId"));
			projectionList1.add(Projections.groupProperty("workPackage.workPackageId"));
			c1.setProjection(projectionList1);
			List<Object[]> list4 = c1.list();
			if(!list4.isEmpty() ){
				Object[] row4= (list4 != null && list4.size() != 0) ? (Object[]) list4.get(0) : null;
				workPackageTestCaseSummaryDTO.setApprovedDefectsCount(((Integer)row4[0]));
			}
			
			/*Fetching WorkPackage Created by*/
			c1= null;
			c1 = sessionFactory.getCurrentSession().createCriteria(WorkPackage.class, "wpkg");
			c1.createAlias("wpkg.userList", "user");
			c1.add(Restrictions.eq("wpkg.workPackageId", workPackageId));
			
			ProjectionList projectionList2 = Projections.projectionList();
			projectionList2.add(Property.forName("user.loginId").as("loginId"));
			c1.setProjection(projectionList2);
			
			List<Object> list5 = c1.list();
			String singlerow5 = (list5 != null && list5.size() != 0) ? (String) list5.get(0) : null;
			if(singlerow5!=null )				
				workPackageTestCaseSummaryDTO.setWpcreatedUser((String) singlerow5);			
		
			/*Fetching Jobs total and completed count*/
			HashMap<String, Integer> testrunjobs = getTotalAndCompletedTestRunJobsOfWorkPackageById(workPackageId);
			if(testrunjobs != null & testrunjobs.size() >0){
				workPackageTestCaseSummaryDTO.setJobsCount(testrunjobs.get("trjobscount"));
				workPackageTestCaseSummaryDTO.setTestRunJobsCompleted(testrunjobs.get("trjobscompleted"));				
			}				
			
			/*--WP-Life Cycle Phase List---*/
			Criteria c3 = sessionFactory.getCurrentSession().createCriteria(LifeCyclePhase.class,"lf");
				
			projectionList = Projections.projectionList();
			projectionList.add(Property.forName("lf.lifeCyclePhaseId").as("lifeCyclePhaseId"));
			projectionList.add(Property.forName("lf.name").as("lifeCycleName"));		//0			
			c3.setProjection(projectionList);	
			List<Object[]> listLife = c3.list();
			 HashMap<Integer, String> lifeCycleValue=new HashMap();
			for (Object[] value : listLife) {
				lifeCycleValue.put((Integer)value[0], (String)value[1]);
			}
			workPackageTestCaseSummaryDTO.setLifecyclePhaseList(lifeCycleValue);
			
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return workPackageTestCaseSummaryDTO;
	}
	
	@Override
	@Transactional
	public HashMap<String, Integer> getTotalAndCompletedTestRunJobsOfWorkPackageById(int workPackageId) {
		log.debug("getTestRunJobsOfWorkPackageById by workPackageId");
		HashMap<String, Integer> testRunJobsCountSet = new HashMap<String, Integer>();
		String sql = "";
		List<Object []> trjobsCount = null;
		try{		
				 sql="select count(*) as testrunjobs, (select count(*) as jobscompleted "
				 		+ "from test_run_job trj  where trj.testRunStatus=5 and trj.workpackageId="+workPackageId+" ) as jobscompleted"
				 		+ " from test_run_job trj inner join workpackage wp on trj.workpackageId="+workPackageId+" where wp.workpackageId="+workPackageId+"";			
				 trjobsCount = sessionFactory.getCurrentSession().createSQLQuery(sql).list();
				 Integer jobscount= 0;
				 Integer jobscompletedcount =0;
				if(trjobsCount != null && trjobsCount.size() >0){
					Object[] xx = trjobsCount.get(0);
					if(xx[0] != null)						
						jobscount =((BigInteger) xx[0]).intValue();
						testRunJobsCountSet.put("trjobscount", jobscount);
					if(xx[1] != null)
						jobscompletedcount = ((BigInteger) xx[1]).intValue();
						testRunJobsCountSet.put("trjobscompleted", jobscompletedcount);
				}
				
			log.debug("list all successful");			
		}catch(RuntimeException re){
			log.error("list all failed", re);
			//throw re;
		}
		return testRunJobsCountSet;
	}
	
	
	@Override
	@Transactional
	public void updateTestCaseResults(
			TestCaseExecutionResult testCaseExecutionResult) {
		log.debug("adding updateTestCaseResults instance");
		try {
			sessionFactory.getCurrentSession().update(testCaseExecutionResult);
			log.debug("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
		}
	}

	@Override
	@Transactional
	public Date getFirstExecutedTCStartTimeofWP(int workPackageId) {
		log.info("Get FirstExecutedTCStartTimeofWP workPackageId");
		Date dt = null;
		try {
			String sql="select ter.startTime from testcase_execution_result ter "
					+ "inner join workpackage_testcase_execution_plan wtcep on ter.testCaseExecutionResultId = wtcep.wptcepId "
					+ "inner join workpackage wp on wtcep.workPackageId = wp.workPackageId "
					+ "where wp.workPackageId=:wpId and ter.startTime is not null order by ter.startTime asc limit 1";			
		
			dt=(Date) sessionFactory.getCurrentSession().createSQLQuery(sql).
					setParameter("wpId", workPackageId).
					uniqueResult();
			log.debug("FirstExecutedTCStartTimeofWP  :"+dt);
		
		} catch (RuntimeException re) {
			log.error("Get FirstExecutedTCStartTimeofWP workPackageId", re);	
		}
		return dt;		
	}
	
	@Override
	@Transactional
	public Date getLastExecutedTCEndTimeofWP(int workPackageId) {
		log.info("Get LastExecutedTCEndTimeofWP workPackageId");
		Date dt = null;
		try {
			String sql="select ter.endTime from testcase_execution_result ter "
					+ "inner join workpackage_testcase_execution_plan wtcep on ter.testCaseExecutionResultId = wtcep.wptcepId "
					+ "inner join workpackage wp on wtcep.workPackageId = wp.workPackageId "
					+ "where wp.workPackageId=:wpId and ter.endTime is not null order by ter.endTime desc limit 1";
			
			dt=(Date) sessionFactory.getCurrentSession().createSQLQuery(sql).
					setParameter("wpId", workPackageId).
					uniqueResult();
			log.debug("LastExecutedTCEndTimeofWP :"+dt);
		
		} catch (RuntimeException re) {
			log.error("Get LastExecutedTCEndTimeofWP workPackageId", re);	
		}
		return dt;		
	}
	
	@Override
	@Transactional
	public void setUpdateWPActualStartDate(int workPackageId, Date startTime) {
		log.info("Get setUpdateWPActualStartDate workPackageId");
		try {			
			String modifiedDate= "";
			String sql = "";			
			modifiedDate= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startTime);
			if(modifiedDate != null){
				sql = "UPDATE workpackage wp SET wp.actualStartDate='"+ modifiedDate+"' where wp.workPackageId="+workPackageId;	
			}else{
				sql = "UPDATE workpackage wp SET wp.actualStartDate='"+ startTime+"' where wp.workPackageId="+workPackageId;
			}
			
			sessionFactory.getCurrentSession().createSQLQuery(sql).executeUpdate();
			
			log.debug("setUpdateWPActualStartDate :"+startTime);
		
		} catch (RuntimeException re) {
			log.error("Get setUpdateWPActualStartDate workPackageId", re);	
		}
	}	
	
	@Override
	@Transactional
	public void setUpdateWPActualEndtDate(int workPackageId, Date endTime) {
		log.info("Get setUpdateWPActualEndtDate workPackageId endTime "+endTime);
		try {
			String modifiedDate= "";
			String sql = "";
			modifiedDate= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(endTime);
			if(modifiedDate != null){
				 sql = "UPDATE workpackage wp SET wp.actualEndDate='"+ modifiedDate+"' where wp.workPackageId="+workPackageId;	
			}else{
				 sql = "UPDATE workpackage wp SET wp.actualEndDate='"+ endTime+"' where wp.workPackageId="+workPackageId;
			}
			
			sessionFactory.getCurrentSession().createSQLQuery(sql).executeUpdate();
			
			log.debug("setUpdateWPActualEndtDate :"+endTime);
		
		} catch (RuntimeException re) {
			log.error("Get setUpdateWPActualEndtDate workPackageId", re);	
		}
	}	
	
	@Override
	@Transactional
	public ExecutionPriority getExecutionPriorityByName(String priorityValue) {
		List<ExecutionPriority> ExecutionPriorityList = new ArrayList<ExecutionPriority>();
		ExecutionPriority executionPriority=null;
		Criteria c = sessionFactory.getCurrentSession().createCriteria(ExecutionPriority.class, "ep");
		
		c.add(Restrictions.eq("ep.executionPriority", priorityValue));
		
		ExecutionPriorityList = c.list();
		
		executionPriority = (ExecutionPriorityList != null && ExecutionPriorityList.size() != 0) ? (ExecutionPriority) ExecutionPriorityList
				.get(0) : null;
		return executionPriority;
	}

	@Override
	@Transactional
	public List<ExecutionPriority> getExecutionPriority() {
		log.debug("listing ExecutionPriority instance");
		List<ExecutionPriority> executionPriority = null;
		try {
			executionPriority = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from ExecutionPriority")
					.list();
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return executionPriority;
	}

	@Override
	@Transactional
	public ExecutionPriority getExecutionPriorityById(int executionPriorityId) {
		List<ExecutionPriority> ExecutionPriorityList = new ArrayList<ExecutionPriority>();
		ExecutionPriority executionPriority=null;
		Criteria c = sessionFactory.getCurrentSession().createCriteria(ExecutionPriority.class, "ep");
		
		c.add(Restrictions.eq("ep.executionPriorityId", executionPriorityId));
		
		ExecutionPriorityList = c.list();
		
		executionPriority = (ExecutionPriorityList != null && ExecutionPriorityList.size() != 0) ? (ExecutionPriority) ExecutionPriorityList
				.get(0) : null;
		return executionPriority;
	}

	@Override
	@Transactional
	public List<WorkShiftMaster> listActualShiftByPlannedDateAndTestFactory(
			Integer testFactoryId, String actualExecutionDate) {
		List<WorkShiftMaster> actualShiftList = new ArrayList<WorkShiftMaster>();
		List<Object> list = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ActualShift.class, "as");
			c.createAlias("as.shift", "shift");
			c.createAlias("shift.testFactory", "testFactory");
			
			c.add(Restrictions.eq("testFactory.testFactoryId", testFactoryId));
			if(actualExecutionDate!=null){
				c.add(Restrictions.eq("as.workdate", DateUtility.dateformatWithOutTime(actualExecutionDate)));
			}
			ProjectionList projectionList = Projections.projectionList();
			
			projectionList.add(Projections.distinct(Projections.property("shift.shiftId")));
			c.setProjection(projectionList);
			list = c.list();
			WorkShiftMaster workShiftMaster=null;
			if (!list.isEmpty()) {
				for (Object shiftId : list) {
					workShiftMaster = getWorkShiftById((Integer)shiftId);
					Hibernate.initialize(workShiftMaster.getTestFactory());
					Hibernate.initialize(workShiftMaster.getShiftType());
					actualShiftList.add(workShiftMaster);
				}
			}
			
			log.debug("list successful"+actualShiftList.size());
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return actualShiftList;	
	}
	@Override
	@Transactional
	public ProductMaster getProductMasterByWorkpackageId(int workPackageId) {
		log.debug(" getProductIdByWorkpackage instance by id");
		Integer productId = null;
		ProductMaster productMaster=null;
		WorkPackage workpackgae=null;
		try {
			workpackgae = getWorkPackageById(workPackageId);
			productMaster = workpackgae.getProductBuild().getProductVersion().getProductMaster();
			if(productMaster!=null){
				Hibernate.initialize(productMaster.getTestFactory());
				Hibernate.initialize(productMaster.getCustomer());
				Hibernate.initialize(productMaster.getTestSuiteLists());
		Hibernate.initialize(productMaster.getProductVersionListMasters());
		Hibernate.initialize(productMaster.getProductType());
		Hibernate.initialize(productMaster.getProductMode());
				
			}
			log.debug("getProductIdByWorkpackage successful");
		} catch (RuntimeException re) {
			log.error("getProductIdByWorkpackage failed", re);
			// throw re;
		}
		return productMaster;
	}

	@Override
	@Transactional
	public Integer getProductIdByWorkpackageId(int workPackageId) {
		log.debug(" getProductMasterIdByWorkpackageId instance by workPackageId");		
		Integer productId = 0;
		String sql = "";	
		try {
			sql = "select pm.productId from product_master pm inner join product_version_list_master pvlm on pm.productId=pvlm.productId "+
					"inner join product_build pb on pvlm.productVersionListId=pb.productVersionId "+ 
					"inner join workpackage wp on wp.productBuildId=pb.productBuildId and wp.workPackageId=:wpId";
			productId = ((Number)sessionFactory.getCurrentSession().createSQLQuery(sql).setParameter("wpId", workPackageId).uniqueResult()).intValue();
			log.debug("getProductMasterIdByWorkpackageId successful");
		} catch (RuntimeException re) {
			log.error("getProductMasterIdByWorkpackageId failed", re);
		}
		return productId;
	}
	
	@Override
	@Transactional
	public Integer getexecutionTypeIdByWorkpackageId(int workPackageId) {
		log.debug(" getexecutionTypeIdByWorkpackageId instance by workPackageId");		
		Integer productId = 0;
		String sql = "";	
		try {
			sql = "select workPackageType from workpackage wp where wp.workPackageId=:wpId";
			productId = ((Number)sessionFactory.getCurrentSession().createSQLQuery(sql).setParameter("wpId", workPackageId).uniqueResult()).intValue();
			log.debug("getexecutionTypeIdByWorkpackageId successful");
		} catch (RuntimeException re) {
			log.error("getexecutionTypeIdByWorkpackageId failed", re);
		}
		return productId;
	}
	
	@Override
	@Transactional
	public Integer getProductTestCaseCount(int productId, int executionType) {
		log.debug("getProductTestCaseCount by productId");
		Integer productTCCount = 0;		
		String sql = "";
		try{		
			if(executionType!=-1){
				 sql="select count(*) from test_case_list tc where tc.productId=:prodId and tc.executionTypeId=:exTypeId";			
					productTCCount=((Number) sessionFactory.getCurrentSession().createSQLQuery(sql)
								.setParameter("prodId", productId)
								.setParameter("exTypeId", executionType)						
								.uniqueResult()).intValue();	
			}else{
				 sql="select count(*) from test_case_list tc where tc.productId=:prodId";			
					productTCCount=((Number) sessionFactory.getCurrentSession().createSQLQuery(sql)
								.setParameter("prodId", productId)					
								.uniqueResult()).intValue();	
			}
			log.debug("list all successful");			
		}catch(RuntimeException re){
			log.error("list all failed", re);
			//throw re;
		}
		return productTCCount;
	}
	
	
	@Override
	@Transactional
	public String getWptcepTestLeadSpocName(Integer workPackageId, Integer shiftId, Date executionDate, Integer testerId) {
		log.debug("listing getWptcepTestLeadSpocName(Integer workPackageId, Integer shiftId, Date executionDate, Integer testerId)");  
		List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlanList = new ArrayList<WorkPackageTestCaseExecutionPlan>();
		log.debug("Getting getWptcep for workPackageId: " + workPackageId+" shiftId: " +  shiftId +" executionDate: " +  executionDate +" testerId: " +  testerId);
		String testLeadSpocName  = null;
		try{		
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackageTestCaseExecutionPlan.class,"wptcep");
			
			c.createAlias("wptcep.workPackage", "wPackage");
			c.createAlias("wptcep.actualWorkShiftMaster", "awsm");
			c.createAlias("wptcep.tester", "testerPerson");
			
			if (executionDate == null || workPackageId == null || shiftId == null || testerId == null){
				return null;
			}
			
			c.add(Restrictions.eq("wptcep.actualExecutionDate", executionDate));
			c.add(Restrictions.eq("wPackage.workPackageId", workPackageId));
			c.add(Restrictions.eq("awsm.shiftId", shiftId));
			c.add(Restrictions.eq("testerPerson.userId", testerId));
			
			workPackageTestCaseExecutionPlanList = c.list();

			if (!workPackageTestCaseExecutionPlanList.isEmpty()) {
				log.debug("workPackageTestCaseExecutionPlanList.size : "+workPackageTestCaseExecutionPlanList.size());
				for (WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan : workPackageTestCaseExecutionPlanList) {
					if (workPackageTestCaseExecutionPlan.getTestLead() != null){
						Hibernate.initialize(workPackageTestCaseExecutionPlan.getTestLead());
						testLeadSpocName = workPackageTestCaseExecutionPlan.getTestLead().getUserDisplayName();
						if (testLeadSpocName != null);
							return testLeadSpocName;
					}
					if(workPackageTestCaseExecutionPlan.getWorkPackage() != null){
						Hibernate.initialize(workPackageTestCaseExecutionPlan.getWorkPackage());
						if(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild() != null){
							Hibernate.initialize(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild());
							if(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion() != null){
								Hibernate.initialize(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion());
							}
						}
					}
				}
			}
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return testLeadSpocName;
	}

	@Override
	@Transactional
	public String getWptcepTestLeadSpocName(Integer workPackageId, Integer shiftId, Date executionDate) {
		log.debug("listing getWptcepTestLeadSpocName(Integer workPackageId, Integer shiftId, Date executionDate)");  // Method used in Timesheet statistics report
		List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlanList = new ArrayList<WorkPackageTestCaseExecutionPlan>();
		String testLeadSpocName  = null;
		try{		
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackageTestCaseExecutionPlan.class,"wptcep");
			
			c.createAlias("wptcep.workPackage", "wPackage");
			c.createAlias("wptcep.actualWorkShiftMaster", "awsm");
			c.createAlias("wptcep.tester", "testerPerson");
			
			if (executionDate == null || workPackageId == null || shiftId == null){
				return null;
			}
			
			c.add(Restrictions.eq("wptcep.actualExecutionDate", executionDate));
			c.add(Restrictions.eq("wPackage.workPackageId", workPackageId));
			c.add(Restrictions.eq("awsm.shiftId", shiftId));
			
			workPackageTestCaseExecutionPlanList = c.list();

			if (!workPackageTestCaseExecutionPlanList.isEmpty()) {
				for (WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan : workPackageTestCaseExecutionPlanList) {
					if (workPackageTestCaseExecutionPlan.getTestLead() != null){
						Hibernate.initialize(workPackageTestCaseExecutionPlan.getTestLead());
						testLeadSpocName = workPackageTestCaseExecutionPlan.getTestLead().getUserDisplayName();
						if (testLeadSpocName != null);
							return testLeadSpocName;
					}
					if(workPackageTestCaseExecutionPlan.getWorkPackage() != null){
						Hibernate.initialize(workPackageTestCaseExecutionPlan.getWorkPackage());
						if(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild() != null){
							Hibernate.initialize(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild());
							if(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion() != null){
								Hibernate.initialize(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion());
							}
						}
					}
				}
			}
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return testLeadSpocName;
	}
	
	@Override
	@Transactional
	public void deleteWorkPackageTestCaseExecutionPlan(
			WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan) {
		log.debug("deleteWorkPackageTestCaseExecutionPlan");
		try {
		TestCaseExecutionResult testExeRes=	workPackageTestCaseExecutionPlan.getTestCaseExecutionResult();
				sessionFactory.getCurrentSession().delete(testExeRes);
				sessionFactory.getCurrentSession().delete(workPackageTestCaseExecutionPlan);
				log.debug("delete successful");
		} catch (Exception re) {
			re.printStackTrace();
			log.error("delete failed", re);
		}
	}

	@Override
	@Transactional
	public WorkPackageTestCaseExecutionPlan mapWorkPackageTestCaseExecutionPlanEnv(WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan, Environment environment,String action) {
		log.debug("mapWorkPackageTestCaseExecutionPlanEnv");
		Integer workpackageTestacseExecutionId=workPackageTestCaseExecutionPlan.getId();
		workPackageTestCaseExecutionPlan=getWorkpackageTestcaseExecutionPlanById(workpackageTestacseExecutionId);
		
		try {
			
			if (workPackageTestCaseExecutionPlan != null ) {
				boolean needToUpdateOrAdd = false;
				
				if (action.equalsIgnoreCase("Add")) {
					Integer environmentId=environment.getEnvironmentId();
					int environmentGroupId=environment.getEnvironmentCategory().getEnvironmentGroup().getEnvironmentGroupId();
					environment = getEnvironmentById(environmentId);
				
					Set<Environment> environmentSet = workPackageTestCaseExecutionPlan
							.getEnvironmentList();
					
					if (environmentSet.size() == 0) {
						log.debug("needto be u[date");
						needToUpdateOrAdd = true;
					} else {
						Environment environmentForProcessing = environmentSet
								.iterator().next();
						if (environmentForProcessing != null) {
							int alreadyAvailableEnvironmentId = environmentForProcessing
									.getEnvironmentId().intValue();
							int alreadyAvailableEnvironmentGroupId = getEnvironmentById(alreadyAvailableEnvironmentId).getEnvironmentCategory().getEnvironmentGroup().getEnvironmentGroupId();
							log.debug("alreadyAvailableEnvironmentId---------->"
									+ alreadyAvailableEnvironmentId + "environmentId---------->"
							+ environmentId);
					
							log.debug("alreadyAvailableEnvironmentGroupId---------->"
							+ alreadyAvailableEnvironmentGroupId + "environmentGroupId---------->"
					+ environmentGroupId);
							if (alreadyAvailableEnvironmentId != environmentId ) {
						log.debug("workPackageTestCaseExecutionPlan.getEnvironmentList().size()="
										+ workPackageTestCaseExecutionPlan.getEnvironmentList()
												.size());

								Environment environmentAvailable = getEnvironmentById(alreadyAvailableEnvironmentId);
								for (WorkPackageTestCaseExecutionPlan wp : environmentAvailable
										.getWorkPackageTestCaseExecutionPlanList()) {


									if (wp.getId().intValue() == workpackageTestacseExecutionId) {
										log.debug("workpackageTestacseExecutionId found ");
										sessionFactory.getCurrentSession()
												.saveOrUpdate(
														environmentAvailable);
										log.debug("environmentAvailable.getWorkPackageTestCaseExecutionPlanList().size()="
												+ environmentAvailable
														.getWorkPackageTestCaseExecutionPlanList()
														.size());
										break;
									}
								}

								needToUpdateOrAdd = true;
							}
						}
					}

					if (needToUpdateOrAdd) {
						log.debug("updating owrkpackage..."+environment.getEnvironmentId());
						log.debug("updating owrkpackage..."+workPackageTestCaseExecutionPlan.getId());
						workPackageTestCaseExecutionPlan.getEnvironmentList().add(environment);
						environment.getWorkPackageTestCaseExecutionPlanList().add(workPackageTestCaseExecutionPlan);
						sessionFactory.getCurrentSession().saveOrUpdate(
								workPackageTestCaseExecutionPlan);
						sessionFactory.getCurrentSession().saveOrUpdate(
								environment);
					}
				}
				else if(action.equalsIgnoreCase("Remove")){
					try {
						
					int	count = (sessionFactory
								.getCurrentSession()
								.createSQLQuery(
										"delete from workpackagetestcaseexecutionplan_has_environment where id="
												+ workPackageTestCaseExecutionPlan.getId()).executeUpdate());
						
					log.debug("Removed environment from workpackage successfully"+count);
					} catch (Exception re) {
						re.printStackTrace();
						log.error("Failed to remove environment from workpackage", re);
						
					}
				
				}
			}
		} catch (RuntimeException re) {
			log.error("list specific failed", re);
		}
		return workPackageTestCaseExecutionPlan;
	}

	@Override
	@Transactional
	public void mapWorkpackageWithTestCase(Integer workPackageId,
			Integer testcaseId,String action) {
				try {
					log.info("workPackageId--->"+workPackageId);
					log.info("testcaseId--->"+testcaseId);
					WorkPackage workPackage =getWorkPackageByIdWithMinimalnitialization(workPackageId);
					TestCaseList testcase= testCaseListDAO.getByTestCaseId(testcaseId);
					if (workPackage != null && testcase != null) {
						boolean needToUpdateOrAdd = false;
						Set<TestCaseList> testcaseSet = workPackage.getTestcaseList();
						log.info("inside action>>"+action);		
						if (action.equalsIgnoreCase("Add")) {
							log.info("inside add");
							if (testcaseSet.size() == 0) {
								needToUpdateOrAdd = true;
							} else {
								TestCaseList testcaseForProcessing = testcaseSet.iterator().next();
								if (testcaseForProcessing != null) {
									int alreadyAvailableTestcaseId = testcaseForProcessing.getTestCaseId().intValue();
									if (alreadyAvailableTestcaseId != testcaseId) {
										log.info("alreadyAvailableTestcaseId---------->"+ alreadyAvailableTestcaseId);
										log.info("testcaseId---------->"+ testcaseId);
										TestCaseList testcaseAvailable = testCaseListDAO.getByTestCaseId(alreadyAvailableTestcaseId); 
										for (WorkPackage wp : testcaseAvailable.getWorkPackageList()) {
											log.info("wp.getWorkPackageId().intValue()"+ wp.getWorkPackageId().intValue());
											if (wp.getWorkPackageId().intValue() == workPackageId) {
												log.info("workPackageId found ");
												sessionFactory.getCurrentSession()
														.saveOrUpdate(
																testcaseAvailable);
												log.info("testcaseAvailable.getWorkPackageList().size()="
														+ testcaseAvailable
																.getWorkPackageList()
																.size());
												break;
											}
										}

										needToUpdateOrAdd = true;
									}
								}
							}

							if (needToUpdateOrAdd) {
								workPackage.getTestcaseList().add(testcase);
								testcase.getWorkPackageList().add(workPackage);
								sessionFactory.getCurrentSession().saveOrUpdate(
										workPackage);
								sessionFactory.getCurrentSession().saveOrUpdate(
										testcase);
							}
						}
						else if(action.equalsIgnoreCase("Remove")){
							log.info("Remove testcase from Workpackage");
							try {
								Set<TestCaseList> testcaseList = workPackage.getTestcaseList();
								testcaseList.remove(testcase);
								workPackage.setTestcaseList(testcaseList);
								sessionFactory.getCurrentSession().save(workPackage);
								log.info("Removed workPackage from workpackage successfully");
							} catch (RuntimeException re) {
								log.error("Failed to remove workPackage from workpackage", re);
								
							}
						
						}
					}
				} catch (RuntimeException re) {
					log.error("list specific failed", re);
				}
	}

	@Override
	@Transactional
	public void deleteMappingWorkPackageTestCaseExecutionPlanEnv(
			Integer wptcepId,
			Integer environmentId) {
		try {
			
			int	count = (sessionFactory
					.getCurrentSession()
					.createSQLQuery(
							"delete from workpackagetestcaseexecutionplan_has_environment where id="
									+ wptcepId ).executeUpdate());
			log.debug("Removed environment from workPackageTestCaseExecutionPlan successfully");
		} catch (Exception re) {
			re.printStackTrace();
			log.error("Failed to remove workPackage from workpackage", re);
			
		}
	}
	
	@Override
	@Transactional
	public void addWorkPackageDemandProjection(
			WorkPackageDemandProjection workPackageDemandProjection) {
		try {	
			sessionFactory.getCurrentSession().saveOrUpdate(workPackageDemandProjection);
			log.debug("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}		
		
	}
	

	
	@Override
	@Transactional
	public List<WorkPackageDemandProjection> listWorkPackageDemandProjectionByDate(int workPackageId, int shiftId, Date date) {
		List<WorkPackageDemandProjection> workPackageDemandProjectionList = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackageDemandProjection.class,"workPackageDemand");
			if(date!=null)
				c.add(Restrictions.eq("workDate", date));
			if(workPackageId!=-1){
			c.add(Restrictions.eq("workPackage.workPackageId", workPackageId));
			}
			if(shiftId!=-1 && shiftId!=0)
				c.add(Restrictions.eq("workShiftMaster.shiftId", shiftId));
			workPackageDemandProjectionList = c.list();
			
			if (!(workPackageDemandProjectionList == null || workPackageDemandProjectionList
					.isEmpty())) {
				for (WorkPackageDemandProjection workPackageDemandProjection : workPackageDemandProjectionList) {
					Hibernate.initialize(workPackageDemandProjection.getWorkPackage());
					Hibernate.initialize(workPackageDemandProjection.getWorkShiftMaster());
					Hibernate.initialize(workPackageDemandProjection.getDemandRaisedByUser());
				}
			}
			log.debug("list successful");
		} catch (Exception re) {
			log.error("list failed", re);
		}
		return workPackageDemandProjectionList;
	}

	@Override
	@Transactional
	public List<UserRoleMaster> getUserRolesForDemandProjection() {
		log.debug("listing getRolesBasedResource instance");
		List<UserRoleMaster> userRoleMaster = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(UserRoleMaster.class, "userRoleMaster");
			c.add(Restrictions.in("userRoleMaster.userRoleId",  Arrays.asList(4, 5,3)));
			userRoleMaster = c.list();
			
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return userRoleMaster;
	}
	

	@Override
	@Transactional
	public WorkPackageDemandProjection getWorkPackageDemandProjectionById(
			int workPackageDemandProjectionId) {
		WorkPackageDemandProjection workPackageDemandProjection = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackageDemandProjection.class, "wpDemandProejction");
			c.add(Restrictions.eq("wpDemandProejction.wpDemandProjectionId",  workPackageDemandProjectionId));
			List<WorkPackageDemandProjection> wpDemandProjectionList = c.list();

			if (wpDemandProjectionList.size() > 0)
				return (WorkPackageDemandProjection)wpDemandProjectionList.get(0);
			else 
				return null;
		} catch (RuntimeException re) {
			log.error("getByProductId failed", re);
		}catch (Exception re) {
			log.error("Exception occurres failed", re);
		}
		return workPackageDemandProjection;
	}

	@Override
	@Transactional
	public void deleteWorkPackageDemandProjection(WorkPackageDemandProjection wpDemandProjection) {
		try {
			sessionFactory.getCurrentSession().delete(wpDemandProjection);
			log.debug("delete WorkPackageDemandProjection successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
		}
		
	}

	@Override
	@Transactional
	public void updateWorkPackageDemandProjection(
			WorkPackageDemandProjection wpDemandProjection) {
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(wpDemandProjection);
		} catch (RuntimeException re) {
			log.error("updating failed", re);
		}
		
	}

	@Override
	@Transactional
	public WorkFlow getWorkFlowByEntityIdStageId(Integer entityId,
			Integer stageId) {
		WorkFlow workFlow = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkFlow.class, "wf");
			c.createAlias("wf.entityMaster", "em");

			c.add(Restrictions.eq("em.entitymasterid",  entityId));
			c.add(Restrictions.eq("wf.stageId",  stageId));
			List<WorkFlow> workFlowList = c.list();
			log.info("workFlowList size==>"+workFlowList.size());
			workFlow = (workFlowList != null && workFlowList.size() != 0) ? (WorkFlow) workFlowList.get(0) : null;

		} catch (RuntimeException re) {
			log.error("getByProductId failed", re);
		}catch (Exception re) {
			log.error("Exception occurres failed", re);
		}
		return workFlow;
	}

	@Override
	public void addWorkFlowEvent(WorkFlowEvent workFlowEvent) {
		try {	
			sessionFactory.getCurrentSession().save(workFlowEvent);
			log.debug("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}		
	}

	@Override
	@Transactional
	public EntityMaster getEntityMasterById(Integer entityId) {
		EntityMaster entityMaster = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(EntityMaster.class, "em");
			c.add(Restrictions.eq("em.entitymasterid",  entityId));
			List<EntityMaster> entityMasterList = c.list();

			if (entityMasterList.size() > 0)
				entityMaster= (EntityMaster)entityMasterList.get(0);
			else 
				return null;
		} catch (RuntimeException re) {
			log.error("getByProductId failed", re);
		}catch (Exception re) {
			log.error("Exception occurres failed", re);
		}
		return entityMaster;
	}


	@Override
	@Transactional
	public EntityMaster getEntityMasterByName(String entityMasterName) {
		EntityMaster entityMaster = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(EntityMaster.class, "em");
			c.add(Restrictions.eq("em.entitymastername",  entityMasterName));
			List<EntityMaster> entityMasterList = c.list();

			if (entityMasterList.size() > 0)
				entityMaster= (EntityMaster)entityMasterList.get(0);
			else 
				return null;
		} catch (RuntimeException re) {
			log.error("getEntityMasterByName failed", re);
		}catch (Exception re) {
			log.error("Exception occurres failed", re);
		}
		return entityMaster;
	}

	@Override
	@Transactional
	public void addEvidence(Evidence evidence) {
		try {	
			sessionFactory.getCurrentSession().save(evidence);
			log.debug("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}		
	}

	@Override
	@Transactional
	public List<Evidence> testcaseListByEvidence(Integer tcerId,String type) {
		List<Evidence> evidenceList=null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(Evidence.class, "ev");
			c.createAlias("ev.entityMaster", "em");
			EntityMaster entitymaster=null;
			if(type.equals("testcase")){
				entitymaster = getEntityMasterById(IDPAConstants.ENTITY_TEST_CASE_EVIDENCE_ID);
				c.add(Restrictions.eq("em.entitymasterid",  entitymaster.getEntitymasterid()));
			}else if(type.equals("teststep")){
				entitymaster = getEntityMasterById(IDPAConstants.ENTITY_TEST_STEP_EVIDENCE_ID);
				c.add(Restrictions.eq("em.entitymasterid",  entitymaster.getEntitymasterid()));
			}
			c.add(Restrictions.eq("ev.entityvalue",  tcerId));
			c.addOrder(Order.asc("ev.entityvalue"));

			evidenceList = c.list();
			if(evidenceList!=null && !evidenceList.isEmpty()){
				for (Evidence evidence : evidenceList) {
					Hibernate.initialize(evidence.getEvidenceType());
				}
			}

		} catch (Exception re) {
			re.printStackTrace();
			log.error("Exception occurres failed", re);
		}
		return evidenceList;
	}

	@Override
	@Transactional
	public List<TestStepExecutionResult> listTestStepPlan(Integer testcaseId,
			Integer tcerId) {
		log.debug("getting testcaseListByEvidence  instance by id: ");
		List<TestStepExecutionResult> testStepExecutionResultList=null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestStepExecutionResult.class, "tser");
			c.createAlias("tser.testcase", "testcase");
			c.createAlias("tser.testCaseExecutionResult", "testCaseExecutionResult");
			
			if(tcerId!=-1 && tcerId !=0)
				c.add(Restrictions.eq("testCaseExecutionResult.testCaseExecutionResultId",  tcerId));
			
			if(testcaseId!=-1)
				c.add(Restrictions.eq("testcase.testCaseId",  testcaseId));
			
			testStepExecutionResultList = c.list();

			for(TestStepExecutionResult testStepExecutionResult:testStepExecutionResultList){
				Hibernate.initialize(testStepExecutionResult.getTestcase());
				Hibernate.initialize(testStepExecutionResult.getTestSteps());
				Hibernate.initialize(testStepExecutionResult.getTestCaseExecutionResult());
				Hibernate.initialize(testStepExecutionResult.getResult());
			}
		} catch (RuntimeException re) {
			log.error("testcaseListByEvidence failed", re);
		}catch (Exception re) {
			log.error("Exception occurres failed", re);
		}
		return testStepExecutionResultList;
	}

	@Override
	@Transactional
	public TestStepExecutionResult getTestStepExecutionResultById(
			Integer testStepId) {
		log.debug("getting testcaseListByEvidence  instance by id: ");
		List<TestStepExecutionResult> testStepExecutionResultList=null;
		TestStepExecutionResult testStepER=null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestStepExecutionResult.class, "tser");

			c.add(Restrictions.eq("tser.teststepexecutionresultid",  testStepId));
			
			testStepExecutionResultList = c.list();

			for(TestStepExecutionResult testStepExecutionResult:testStepExecutionResultList){
				Hibernate.initialize(testStepExecutionResult.getTestcase());
				Hibernate.initialize(testStepExecutionResult.getTestSteps());
				Hibernate.initialize(testStepExecutionResult.getTestCaseExecutionResult());
				Hibernate.initialize(testStepExecutionResult.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan());
				if(testStepExecutionResult.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getWorkPackage() != null){
					Hibernate.initialize(testStepExecutionResult.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getWorkPackage());
					if(testStepExecutionResult.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getWorkPackage().getProductBuild() != null){
						Hibernate.initialize(testStepExecutionResult.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getWorkPackage().getProductBuild());
						if(testStepExecutionResult.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getWorkPackage().getProductBuild().getProductVersion() != null){
							Hibernate.initialize(testStepExecutionResult.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getWorkPackage().getProductBuild().getProductVersion());
						}
					}
				}
				Hibernate.initialize(testStepExecutionResult.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration().getGenericDevice());
				Hibernate.initialize(testStepExecutionResult.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getWorkPackage());
				Hibernate.initialize(testStepExecutionResult.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getWorkPackage().getWorkFlowEvent());
				Hibernate.initialize(testStepExecutionResult.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getWorkPackage().getWorkFlowEvent().getWorkFlow());
			}
			testStepER = (testStepExecutionResultList != null && testStepExecutionResultList.size() != 0) ? (TestStepExecutionResult) testStepExecutionResultList
					.get(0) : null;
		} catch (RuntimeException re) {
			log.error("testcaseListByEvidence failed", re);
		}catch (Exception re) {
			log.error("Exception occurres failed", re);
		}
		return testStepER;
	}

	@Override
	@Transactional
	public void updateTestStepExecutionResult(
			TestStepExecutionResult testStepExecutionResult) {
		log.debug("updating updateTestStepExecutionResult");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(testStepExecutionResult);
			log.debug("updating successful");
		} catch (RuntimeException re) {
			log.error("updating failed", re);
		}
		
	}

	@Override
	@Transactional
	public List<WorkPackageTestCaseSummaryDTO> listWorkPackageTestCaseExecutionSummaryByDate(
			Integer workPackageId) {
		WorkPackageTestCaseSummaryDTO workPackageTestCaseSummaryDTO = null;
		Date workDate =null;
		List<WorkPackageTestCaseSummaryDTO> workPackageTestCaseSummaryDTOList=new ArrayList<WorkPackageTestCaseSummaryDTO>();
		try{
		Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackageTestCaseExecutionPlan.class,"wptcep");
		c.createAlias("wptcep.workPackage", "wpackage");
		c.createAlias("wptcep.actualWorkShiftMaster", "shift");

		c.add(Restrictions.eq("wpackage.workPackageId", workPackageId));
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.distinct(Projections.property("wptcep.actualExecutionDate")));
		projectionList.add(Property.forName("shift.shiftName").as("shiftName"));

		c.setProjection(projectionList);
		
		Object[] row=null;
		List<Object[]> list = c.list();
		for (int i = 0; i < list.size(); i++) {
			workPackageTestCaseSummaryDTO = new WorkPackageTestCaseSummaryDTO();
			if (list.get(i) != null) {
				row= (Object[]) list.get(i);
				workPackageTestCaseSummaryDTO
						.setActualExecutionDate((Date) row[0]);
				workPackageTestCaseSummaryDTO.setShiftName((String)row[1]);
				workPackageTestCaseSummaryDTOList
						.add(workPackageTestCaseSummaryDTO);
			}

		}
		for(WorkPackageTestCaseSummaryDTO wptcs:workPackageTestCaseSummaryDTOList){
			c = sessionFactory.getCurrentSession().createCriteria(WorkPackageTestCaseExecutionPlan.class,"wptcep");
			c.createAlias("wptcep.testCaseExecutionResult", "result2");
			c.createAlias("wptcep.workPackage", "wpackage");

			c.add(Restrictions.eq("wpackage.workPackageId", workPackageId));

			c.add(Restrictions.eq("result2.isApproved", 1));
			c.add(Restrictions.eq("wptcep.actualExecutionDate", wptcs.getActualExecutionDate()));
			projectionList = Projections.projectionList();
			
			projectionList.add(Property.forName("wptcep.id").as("wptcepid"));	//0
			projectionList.add(Projections.countDistinct("wptcep.id").as("approvedTestCases"));//1
			c.setProjection(projectionList);	
			
			
			List<Object[]> list1 = c.list();
			Object[] row1 = (list1 != null && list1.size() != 0) ? (Object[]) list1.get(0) : null;
			Long longvalueApprovedTestCaseCount = (Long)row1[1];
			wptcs.setApprovedTestCaseCount((longvalueApprovedTestCaseCount).intValue());
		}
		
		for(WorkPackageTestCaseSummaryDTO wptcs:workPackageTestCaseSummaryDTOList){
			c = sessionFactory.getCurrentSession().createCriteria(WorkPackageTestCaseExecutionPlan.class,"wptcep");
			c.createAlias("wptcep.workPackage", "wpackage");
			c.createAlias("wptcep.testCaseExecutionResult", "result1");
			c.add(Restrictions.eq("wpackage.workPackageId", workPackageId));
			c.add(Restrictions.eq("result1.isReviewed", 1));
			c.add(Restrictions.eq("wptcep.actualExecutionDate", wptcs.getActualExecutionDate()));

			projectionList = Projections.projectionList();
			projectionList.add(Property.forName("wptcep.id").as("wptcepid"));	//0
			projectionList.add(Projections.countDistinct("wptcep.id").as("rejectedTestCases"));//1
			c.setProjection(projectionList);	
			
			List<Object[]> list1 = c.list();
			Object[] row1 = (list1 != null && list1.size() != 0) ? (Object[]) list1.get(0) : null;
			Long longvalueApprovedTestCaseCount = (Long)row1[1];
			wptcs.setRejectedTestCaseCount((longvalueApprovedTestCaseCount).intValue());
		}
		
		for(WorkPackageTestCaseSummaryDTO wptcs:workPackageTestCaseSummaryDTOList){
			c = sessionFactory.getCurrentSession().createCriteria(WorkPackageTestCaseExecutionPlan.class,"wptcep");
			c.createAlias("wptcep.workPackage", "wpackage");
			c.add(Restrictions.eq("wpackage.workPackageId", workPackageId));
			c.add(Restrictions.eq("wptcep.executionStatus", 2));
			c.add(Restrictions.eq("wptcep.actualExecutionDate", wptcs.getActualExecutionDate()));

			projectionList = Projections.projectionList();
			projectionList.add(Property.forName("wptcep.id").as("wptcepid"));	//0
			projectionList.add(Projections.countDistinct("wptcep.id").as("completedTotalTestcaseTestCases"));//1
			c.setProjection(projectionList);	
			
			List<Object[]> list1 = c.list();
			Object[] row1 = (list1 != null && list1.size() != 0) ? (Object[]) list1.get(0) : null;
			Long longvalueReviewTestCaseCount = (Long)row1[1];
			wptcs.setCompletedTestCaseCount((longvalueReviewTestCaseCount).intValue());
		}
		
		for(WorkPackageTestCaseSummaryDTO wptcs:workPackageTestCaseSummaryDTOList){
			c = sessionFactory.getCurrentSession().createCriteria(TestExecutionResultBugList.class,"terbl");
			c.createAlias("terbl.testCaseExecutionResult", "tcer");
			c.createAlias("tcer.workPackageTestCaseExecutionPlan", "wptcep");
			c.createAlias("wptcep.workPackage", "wp");
			
			c.add(Restrictions.eq("wp.workPackageId", workPackageId));
			c.add(Restrictions.eq("terbl.bugCreationTime", wptcs.getActualExecutionDate()));

			projectionList = Projections.projectionList();
			projectionList.add(Property.forName("terbl.testExecutionResultBugId").as("testExecutionResultBugId"));	//0
			projectionList.add(Projections.countDistinct("terbl.testExecutionResultBugId").as("bugcount"));//1
			c.setProjection(projectionList);	
			
			List<Object[]> list1 = c.list();
			Object[] row1 = (list1 != null && list1.size() != 0) ? (Object[]) list1.get(0) : null;
			Long longvalueDefectCount = (Long)row1[1];
			wptcs.setDefectsCount((longvalueDefectCount).intValue());
		}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return workPackageTestCaseSummaryDTOList;
	}

	
	@Override
	@Transactional
	public WorkPackageTCEPSummaryDTO listWorkPackageTestCaseExecutionSummaryForTiles(Integer workPackageId) {		
		log.info("Retrieving Workpackage Execution Summary to the Workpackage ID : "+workPackageId);
		List<WorkPackageTCEPSummaryDTO> wptcepResultList = new ArrayList<WorkPackageTCEPSummaryDTO>();
		WorkPackageTCEPSummaryDTO wpSummaryDTO=new WorkPackageTCEPSummaryDTO();
		String sql = "";
		try{			
			if(databaseDialect != null && databaseDialect.equalsIgnoreCase("org.hibernate.dialect.MySQL5Dialect")) {
				sql = " SELECT  wp.workpackageId AS workPackageId,wp.name AS workPackageName ,wp.plannedEndDate as plannedEndDate,wp.plannedStartDate as plannedStartDate, "
						+" wflow.stageName AS workFlowStageName,DATEDIFF(wp.plannedEndDate,wp.plannedStartDate) AS wpStartEnddayDiff,";
			} else if(databaseDialect != null && databaseDialect.equalsIgnoreCase("org.hibernate.dialect.PostgreSQLDialect")) {
				sql = " SELECT  wp.workpackageId AS workPackageId,wp.name AS workPackageName ,wp.plannedEndDate as plannedEndDate,wp.plannedStartDate as plannedStartDate, "
						+" wflow.stageName AS workFlowStageName,DATE_PART('day', wp.plannedEndDate - wp.plannedStartDate) AS wpStartEnddayDiff,";
			} else {
				sql = " SELECT  wp.workpackageId AS workPackageId,wp.name AS workPackageName ,wp.plannedEndDate as plannedEndDate,wp.plannedStartDate as plannedStartDate, "
						+" wflow.stageName AS workFlowStageName,DATEDIFF(wp.plannedEndDate,wp.plannedStartDate) AS wpStartEnddayDiff,";
			}
			log.info("Dialect of the database : " + databaseDialect +"and native SQL query formation : "+sql);		
			sql = sql + " (SELECT COUNT(job.workpackageId) FROM test_run_job job WHERE job.workpackageId ="+workPackageId+"  ) AS jobCount," 
						+" (SELECT COUNT(buglist.testExecutionResultBugId) FROM workpackage_testcase_execution_plan AS wptcPlan "
						+" LEFT JOIN test_execution_result_bug_list AS buglist ON wptcPlan.wptcepId=buglist.testCaseExecutionResultId" 
						+" WHERE wptcPlan.workPackageId="+workPackageId+") AS defectsCount," 
						+" ( SELECT COUNT(tcres.result)   FROM workpackage_testcase_execution_plan AS wptcplan "
						+" INNER JOIN  testcase_execution_result  AS tcres ON tcres.testCaseExecutionResultId = wptcplan.wptcepId AND wptcplan.status=1 " 
						+" WHERE wptcplan.workpackageId= "+workPackageId+"  AND tcres.result='PASSED' ) AS passedCount, "
						+" ( SELECT COUNT(tcres.result)   FROM  workpackage_testcase_execution_plan AS wptcplan " 
						+" INNER JOIN testcase_execution_result  AS tcres ON tcres.testCaseExecutionResultId = wptcplan.wptcepId AND wptcplan.status=1 " 
						+" WHERE wptcplan.workpackageId= "+workPackageId+" AND tcres.result='FAILED' ) AS failedCount,"
						+" ( SELECT COUNT(tcres.result)   FROM  workpackage_testcase_execution_plan AS wptcplan " 
						+" INNER JOIN testcase_execution_result  AS tcres 	ON tcres.testCaseExecutionResultId = wptcplan.wptcepId AND wptcplan.status=1 "
						+" WHERE wptcplan.workpackageId ="+workPackageId+" AND tcres.result='BLOCKED' ) AS blockedCount,"
	                    +" (SELECT COUNT(tcres.result)   FROM  workpackage_testcase_execution_plan AS wptcplan INNER JOIN testcase_execution_result  AS tcres " 
						+" ON tcres.testCaseExecutionResultId = wptcplan.wptcepId AND wptcplan.status=1 WHERE wptcplan.workpackageId = "+workPackageId+" AND tcres.result='NORUN' ) AS norunCount,"
						+" ( SELECT COUNT(tcres.result)   FROM  workpackage_testcase_execution_plan AS wptcplan INNER JOIN testcase_execution_result  AS tcres "
						+" ON tcres.testCaseExecutionResultId= wptcplan.wptcepId AND wptcplan.status = 1  WHERE wptcplan.workpackageId = "+workPackageId+" AND  (tcres.result = '' OR tcres.result IS NULL) ) AS notexecutedCount,"
						+"(SELECT COUNT(job.workpackageId) FROM test_run_job job WHERE job.workpackageId =wp.workpackageId and job.testRunStatus=5 ) AS jobsCompleted"
						+" FROM workpackage wp LEFT JOIN workflowevent AS wfevent ON wp.workfloweventid = wfevent.workfloweventId" 
						+" LEFT JOIN workflow AS wflow ON wfevent.workflowId = wflow.workflowId"				
						+" WHERE  wp.workpackageId="+workPackageId;
			wptcepResultList = sessionFactory.getCurrentSession().createSQLQuery(sql)
					.addScalar("workPackageId",StandardBasicTypes.INTEGER ).addScalar("workPackageName", StandardBasicTypes.STRING)
					.addScalar("plannedEndDate",StandardBasicTypes.DATE ).addScalar("plannedStartDate", StandardBasicTypes.DATE)
					.addScalar("workFlowStageName", StandardBasicTypes.STRING).addScalar("wpStartEnddayDiff", StandardBasicTypes.INTEGER)
					.addScalar("jobCount", StandardBasicTypes.INTEGER)
					.addScalar("defectsCount", StandardBasicTypes.INTEGER)
					.addScalar("passedCount", StandardBasicTypes.INTEGER).addScalar("failedCount", StandardBasicTypes.INTEGER)
					.addScalar("blockedCount", StandardBasicTypes.INTEGER).addScalar("norunCount", StandardBasicTypes.INTEGER)
					.addScalar("notexecutedCount", StandardBasicTypes.INTEGER)	
					.addScalar("jobsCompleted", StandardBasicTypes.INTEGER)	
					.setResultTransformer(Transformers.aliasToBean(WorkPackageTCEPSummaryDTO.class)).list();
			
			if(wptcepResultList.size()>0){
				wpSummaryDTO=wptcepResultList.get(0);
			}
			
			return wpSummaryDTO;
		} catch(Exception e){
			log.error("Error in retrieving Workpackage Summary to the Workpackage Id : "+workPackageId);
			return null;
		}
	}
	@Override
	@Transactional
	public JsonWorkPackageTestCaseExecutionPlanForTester listWorkPackageTestCaseExecutionSummaryReport(
			Integer workPackageId) {
		log.debug("Inside listWorkPackageTestCaseExecutionSummaryByDate MySelf");
		JsonWorkPackageTestCaseExecutionPlanForTester jsonWPTCEPT = new JsonWorkPackageTestCaseExecutionPlanForTester();
		int executed=0;
		int lastonehourexecuted=0;
		int bugCount=0;
		int totalPass=0;
		int totalFail=0;
		int totalNoRun=0;
		int totalBlock=0;
		int resultId=0;
		Long defectsCount=(long)0;
		Long wptrbedsize =(long)0 ;
	
		try{			
			WorkPackage wpobj = getWorkPackageById(workPackageId);
			String wpstatus = "";
			wpstatus = wpobj.getWorkFlowEvent().getWorkFlow().getStageName();
	jsonWPTCEPT.setWpStatus(wpstatus);
			Date sdate = new Date();
			Date edate = new Date();
			sdate = wpobj.getPlannedStartDate();
			edate = wpobj.getPlannedEndDate();
			Long datediff =DateUtility.DateDifferenceInMinutes(sdate, edate);
			int weekEnds = DateUtility.findWeekEndsBetWeenDates(sdate, edate);
			String totalTimeConvertedInHoursMinutes = DateUtility.convertTimeInHoursMinutes(0, new Integer((int) (long)datediff));				
			if(totalTimeConvertedInHoursMinutes.indexOf(':') != -1){
				totalTimeConvertedInHoursMinutes=	totalTimeConvertedInHoursMinutes.substring(0, totalTimeConvertedInHoursMinutes.indexOf(':'));
			}				
			Integer wpStartEndDateDiff = Integer.parseInt(totalTimeConvertedInHoursMinutes)/24;		
			wpStartEndDateDiff = wpStartEndDateDiff - weekEnds;//Excluding weekends.
	jsonWPTCEPT.setWpStartEnddayDiff(String.valueOf(wpStartEndDateDiff));
			//-----------------------								
			Date currentDate = new Date();							
			Long nthDayfromStart = (long)0;
			Integer nthDayfomWPStart = 0;
			if(currentDate.compareTo(edate) >0){//WPEndDate over(Before Current Date)
				nthDayfomWPStart = 0;
			}else if(currentDate.compareTo(edate) <0){//WPEndate is in furture(after Current Date)
				if(currentDate.compareTo(sdate) == -1){//WPStart in future
					jsonWPTCEPT.setPlannedBeforeAfterCurrentDate("after");//StartDate is in future(after Current Date)
					nthDayfromStart = 	DateUtility.DateDifferenceInMinutes(currentDate, sdate);
				}else{
					jsonWPTCEPT.setPlannedBeforeAfterCurrentDate("before");//StartDate is in before Current Date
					 nthDayfromStart = 	DateUtility.DateDifferenceInMinutes(sdate, currentDate);
				}		
			String intoHourMinutes = DateUtility.convertTimeInHoursMinutes(0, new Integer((int) (long)nthDayfromStart));		
			if(intoHourMinutes.indexOf(':') != -1){
				intoHourMinutes=	intoHourMinutes.substring(0, intoHourMinutes.indexOf(':'));
			}
			
			nthDayfomWPStart = Integer.parseInt(intoHourMinutes)/24;	
			}
	jsonWPTCEPT.setWpnthDayfromStrart("-"+String.valueOf(nthDayfomWPStart));
			
		Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackageTestCaseExecutionPlan.class,"wptcep");
		c.createAlias("wptcep.workPackage", "wpackage");		
		c.createAlias("wpackage.productBuild", "build");
		c.createAlias("build.productVersion", "version");
		c.createAlias("version.productMaster", "prod");
		c.createAlias("prod.testCaseLists", "tc");		
		c.createAlias("wptcep.testCase", "wptceptcases");
		c.createAlias("wptcep.testCaseExecutionResult", "wptcexresult");
		c.createAlias("wpackage.testRunJobSet", "runjob");
		c.add(Restrictions.eq("wpackage.workPackageId", workPackageId));		
		c.addOrder(Order.asc("wptcep.actualExecutionDate"));
		
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Property.forName("wpackage.name").as("wpName"));
		projectionList.add(Property.forName("wpackage.plannedStartDate").as("startDate"));
		projectionList.add(Property.forName("wpackage.plannedEndDate").as("endDate"));
		projectionList.add(Property.forName("wptcep.modifiedDate").as("executedmodifiedDate"));
		projectionList.add(Property.forName("wptcep.isExecuted").as("executed"));
		projectionList.add(Property.forName("wptcexresult.testCaseExecutionResultId").as("tcerId"));				
		projectionList.add(Property.forName("wptcexresult.result").as("result"));
		projectionList.add(Projections.countDistinct("wptcep.isExecuted").as("seltestCaseCount"));//1
		projectionList.add(Projections.countDistinct("runjob.testRunJobId").as("testrunbetcount"));
		projectionList.add(Property.forName("wptcep.actualExecutionDate").as("date"));
		projectionList.add(Projections.groupProperty("wptcep.id"));
		c.setProjection(projectionList);		
		
		List<Object[]> wptcepTCList = c.list();		
		log.debug("WPTCEP list.size()=="+wptcepTCList.size());
		Date firsttcexecutedDate = null;
		if(wptcepTCList.size() != 0){			
			String wpName = "";
			Date executedDate;
			for(Object[] row:wptcepTCList){
				int tceResId=(Integer) row[5];
				List<TestExecutionResultBugList> testExecutionResultBugList=listDefectsByTestcaseExecutionPlanId(tceResId,0,0) ;
				bugCount=bugCount+testExecutionResultBugList.size();
				
			Integer wptcepId = (Integer)row[10];
					wpName=(String)row[0];
					if((Integer)row[4]==1){//"wptcep.isExecuted").as("executed")						
						++executed;						
							executedDate =(Date)row[3];
						if(executedDate != null){
							Calendar cal = Calendar.getInstance();						
							cal.add(Calendar.HOUR, -1);
							Date oneHourBack = cal.getTime();
							int withinrange = DateUtility.dateWithInRange(oneHourBack, new Date(), executedDate);
							if(withinrange == 1){
								++lastonehourexecuted;
							}
						}						
					}
					
					sdate  = (Date)row[1];
					edate  = (Date)row[2];					
					String resultval=(String)row[6];
					
					if(!resultval.equals("")){
						if(resultval.equals("PASSED")){
								++totalPass;
							}else if(resultval.equals("FAILED")){
								++totalFail;
							}else if(resultval.equals("NORUN")){
								++totalNoRun;
							}else if(resultval.equals("BLOCKED")){
								++totalBlock;
							}			
					}
					
					if(row[8] != null){
						wptrbedsize  = (Long)row[8];						
					}
					/*-------------------*/
					if(row[9] != null){
						firsttcexecutedDate = (Date)row[9];
						if(jsonWPTCEPT.getActualExecutionDate() == null){
							jsonWPTCEPT.setActualExecutionDate(firsttcexecutedDate.toString());	
							jsonWPTCEPT.setFirstActualExecutionDate(firsttcexecutedDate.toString());
							log.debug("new first executed date --"+firsttcexecutedDate);
						}							
					//	break;
						jsonWPTCEPT.setLastActualExecutionDate(firsttcexecutedDate.toString());
						log.debug("new last executed date --"+firsttcexecutedDate);
						}					
					/*-------------------*/
			}
			
	jsonWPTCEPT.setExecutedLastHour(String.valueOf(lastonehourexecuted));
	jsonWPTCEPT.setWorkPackageName(wpName);
	jsonWPTCEPT.setTotalExecutedTesCases(executed);
	jsonWPTCEPT.setDefectsCount(bugCount);
	jsonWPTCEPT.setTestBedCount((int)(long)wptrbedsize);
	jsonWPTCEPT.setP2totalPass(totalPass);
	jsonWPTCEPT.setP2totalFail(totalFail);
	jsonWPTCEPT.setP2totalNoRun(totalNoRun);
	jsonWPTCEPT.setP2totalBlock(totalBlock);
	List<Object[]> selTCCount = wptcepTCList;
	if(firsttcexecutedDate != null){
		int greatedorlesser = DateUtility.dateGreaterorLesserThan(firsttcexecutedDate, sdate);
		if(greatedorlesser ==1){
			//good
			jsonWPTCEPT.setExecutionBeforeAfter("after");
		}else if(greatedorlesser == 2){
			jsonWPTCEPT.setExecutionBeforeAfter("before");
		}					
	}	
	log.debug("new  list of selected TCs --"+selTCCount.size());
	if(wptcepTCList.size()!=0){
		jsonWPTCEPT.setTotalWPTestCase(wptcepTCList.size());
	}else{
		jsonWPTCEPT.setTotalWPTestCase(selTCCount.size());		
	}
						
		}else{
			jsonWPTCEPT.setProductTotalTC(0);
			jsonWPTCEPT.setTotalWPTestCase(0);
			jsonWPTCEPT.setTotalExecutedTesCases(0);
			jsonWPTCEPT.setWpPlannedTC(0);
			jsonWPTCEPT.setTestBedCount(0);
			jsonWPTCEPT.setDefectsCount(0);
			jsonWPTCEPT.setP2totalPass(0);
			jsonWPTCEPT.setP2totalFail(0);
		}
		
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return jsonWPTCEPT;
	}
	
	
	@Override
	@Transactional
	public List<WorkPackage> listActiveWorkPackages(Integer productId) {
		List<WorkPackage> wpList = new ArrayList<WorkPackage>();		
		try{			
		Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackage.class,"wp");
		c.createAlias("wp.productBuild", "build");		
		c.createAlias("build.productVersion", "version");
		c.createAlias("version.productMaster", "prod");		
		c.createAlias("wp.workFlowEvent", "flowevent");		
		c.createAlias("flowevent.workFlow", "wflow");	
		c.createAlias("wflow.entityMaster", "entmas");	
		//
		c.add(Restrictions.eq("prod.productId", productId));
		c.add(Restrictions.eq("entmas.entitymasterid", 2));
		c.add(Restrictions.in("wflow.stageId", Arrays.asList(1,2, 3)));		
		wpList = c.list();
	
		}
		catch(Exception e){
			e.printStackTrace();
		}		
		return wpList;
	}
	@Override
	@Transactional
	public WorkFlow getWorkFlowByEntityIdWorkFlowId(Integer entityId,
			Integer workFlowId) {
		log.debug("getting getWorkFlowByEntityIdStageId  instance by id: ");
		WorkFlow workFlow = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkFlow.class, "wf");
			c.createAlias("wf.entityMaster", "em");

			c.add(Restrictions.eq("em.entitymasterid",  entityId));
			c.add(Restrictions.eq("wf.workFlowId",  workFlowId));
			List<WorkFlow> workFlowList = c.list();
			workFlow = (workFlowList != null && workFlowList.size() != 0) ? (WorkFlow) workFlowList.get(0) : null;

		} catch (RuntimeException re) {
			log.error("getByProductId failed", re);
		}catch (Exception re) {
			log.error("Exception occurres failed", re);
		}
		return workFlow;
	}
	
	@Override
	@Transactional
	public List<PerformanceLevel> listPerformanceRating() {
		log.debug("getting listPerformanceRating  List: ");
		List<PerformanceLevel> performanceLevelList=null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(PerformanceLevel.class, "performance");
			performanceLevelList = c.list();
			
		} catch (RuntimeException re) {
			log.error("performanceLevelList failed", re);
		}catch (Exception re) {
			log.error("Exception occurres failed", re);
		}
		return performanceLevelList;
	}
	
	@Override
	@Transactional
	public List<UserList> getAllocatedUserListByRole(Integer workpackageId,
			Integer roleId) {
		log.debug("getting getAllocatedUserListByRole  instance by Role: "+roleId);
		List<UserList> userAllocatedList=new ArrayList<UserList>();
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackageTestCaseExecutionPlan.class, "wptcep");
			if(roleId==5){
				c.createAlias("wptcep.tester", "tester");
				c.setProjection(Projections.distinct(Projections.property("tester")));
			}
			else if(roleId==4){
				c.createAlias("wptcep.testLead", "testLead");
				c.setProjection(Projections.distinct(Projections.property("testLead")));

			}
			c.createAlias("wptcep.workPackage", "workPackage");
			
			c.add(Restrictions.eq("workPackage.workPackageId",  workpackageId));
			
			userAllocatedList=c.list();
			if(userAllocatedList!=null && !userAllocatedList.isEmpty()){
				for (UserList userList : userAllocatedList) {
					Hibernate.initialize(userList.getUserRoleMaster());
					Hibernate.initialize(userList.getResourcePool());
					Hibernate.initialize(userList.getUserRoleMaster());
					Hibernate.initialize(userList.getUserTypeMasterNew());
					Hibernate.initialize(userList.getCommonActiveStatusMaster());
					Hibernate.initialize(userList.getVendor());	
					Hibernate.initialize(userList.getUserSkills());
				}	
			}
		} catch (RuntimeException re) {
			log.error("getByProductId failed", re);
		}catch (Exception re) {
			log.error("Exception occurres failed", re);
		}
		return userAllocatedList;
	}
	
	@Override
	@Transactional
	public List<TestCaseExecutionResult> listApprovedDefectsByTestcaseExecutionPlanId(int tcerId,int jtStartIndex, int jtPageSize) {
		List<TestCaseExecutionResult> testExecutionResultBugList = new ArrayList<TestCaseExecutionResult>();
		Criteria c = sessionFactory.getCurrentSession().createCriteria(TestCaseExecutionResult.class, "tcExecResult");
		c.add(Restrictions.eq("tcExecResult.testCaseExecutionResultId", tcerId));
		c.add(Restrictions.eq("tcExecResult.isApproved", 1));
		testExecutionResultBugList = c.list();
		if(testExecutionResultBugList!=null && !testExecutionResultBugList.isEmpty()){
			log.debug("Approved defects count--->"+testExecutionResultBugList.size());
		}
		return testExecutionResultBugList;
	}
	
	@Override
	@Transactional
	public List<WorkPackage> listWpOfUserforSelectedProduct(Integer productId, Integer productVersionId, Integer userId,int startIndex, int pageSize) {
		log.debug("listing ResourceExperienceSummaryDTO: ");
		if(userId == null){
			return null;
		}
		List<WorkPackageTestCaseExecutionPlan> listOfWorkPackageTestCaseExecutionPlan = null;
		List<WorkPackage> listOfWorkPackages = new ArrayList<WorkPackage>();
		log.debug("Getting Resource ExperienceSummary for Product Id : " + productId);
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackageTestCaseExecutionPlan.class, "wptcep");
			c.createAlias("wptcep.workPackage", "workPackage");
			c.createAlias("wptcep.tester", "testerUser");
			c.createAlias("wptcep.testLead", "testLeadUser");
			c.createAlias("workPackage.productBuild", "productbuild");
			c.createAlias("productbuild.productVersion", "productVersion");
			c.createAlias("productVersion.productMaster", "product");
			if(productId == 0){
				return null;
			}else if(productId == -1){
			}
			else if(productId != 0 && (productVersionId == 0 || productVersionId == -1)){
				c.add(Restrictions.eq("product.productId", productId));
			}else if(productVersionId != 0){
				c.add(Restrictions.eq("productVersion.productVersionListId", productVersionId));
			}
			c.add(Restrictions.eq("wptcep.isExecuted", 1));
			
			if(userId != 0){
				c.add(Restrictions.disjunction().add(
				        Restrictions.or(Restrictions.eq("tester.userId", userId),
				                		Restrictions.eq("testLead.userId", userId))));
			}
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.distinct(Projections.property("workPackage.workPackageId")));
			projectionList.add(Projections.groupProperty("productVersion.productVersionListId"));
			if(userId == 0){
				projectionList.add(Projections.groupProperty("testerUser.userId"));
			}
			c.setFirstResult(startIndex);
			c.setMaxResults(pageSize);
			listOfWorkPackageTestCaseExecutionPlan = c.list();
			if(listOfWorkPackageTestCaseExecutionPlan != null && listOfWorkPackageTestCaseExecutionPlan.size()>0){
				log.debug("Distinct WorkPackage from WorkPackageTestCaseExecutionPlan for userId: "+userId+" is : "+listOfWorkPackageTestCaseExecutionPlan.size());
				for(WorkPackageTestCaseExecutionPlan wpTCplan : listOfWorkPackageTestCaseExecutionPlan){
					Hibernate.initialize(wpTCplan.getWorkPackage());
					Hibernate.initialize(wpTCplan.getWorkPackage().getProductBuild().getProductVersion().getProductMaster());
					listOfWorkPackages.add(wpTCplan.getWorkPackage());
				}
			}
			
		} catch (Exception re) {
			log.error("list failed", re);
		}
		return listOfWorkPackages;
	}
	
	
	@Override
	@Transactional
	public List<WorkPackageTestCaseExecutionPlan> listWorkPackageTestCasesExecutionPlanByWorkPackageId(
			int workPackageId, int testcaseId) {
				List<WorkPackageTestCaseExecutionPlan> listWorkPackageTestCaseExecutionPlan = null;
				try {
					listWorkPackageTestCaseExecutionPlan =sessionFactory
							.getCurrentSession()
							.createQuery(
									" from  WorkPackageTestCaseExecutionPlan wptcep where wptcep.workPackage.workPackageId = "
											+ workPackageId
											+ " and wptcep.testCase.testCaseId = "+testcaseId +"  order by wptcep.testCase.testCaseId  asc ").list();
					if (!(listWorkPackageTestCaseExecutionPlan == null || listWorkPackageTestCaseExecutionPlan.isEmpty())) {

						for (WorkPackageTestCaseExecutionPlan dl : listWorkPackageTestCaseExecutionPlan) {
							Hibernate.initialize(dl.getTestCase());
							Hibernate.initialize(dl.getWorkPackage());
							if(dl.getWorkPackage() != null){
								Hibernate.initialize(dl.getWorkPackage());
								if(dl.getWorkPackage().getProductBuild() != null){
									Hibernate.initialize(dl.getWorkPackage().getProductBuild());
									if(dl.getWorkPackage().getProductBuild().getProductVersion() != null){
										Hibernate.initialize(dl.getWorkPackage().getProductBuild().getProductVersion());
									}
								}
							}
							Hibernate.initialize(dl.getTester());
							Hibernate.initialize(dl.getTestLead());
							Hibernate.initialize(dl.getExecutionPriority());
							if(dl.getEnvironmentList().size()!=0){
								Hibernate.initialize(dl.getEnvironmentList());
							}
							Hibernate.initialize(dl.getActualWorkShiftMaster());
							Hibernate.initialize(dl.getPlannedWorkShiftMaster());
							Hibernate.initialize(dl.getTestCase().getDecouplingCategory());
							Hibernate.initialize(dl.getTestCaseExecutionResult());
							Hibernate.initialize(dl.getTestCase().getTestcaseTypeMaster());
							Hibernate.initialize(dl.getTestCase().getTestCasePriority());
						
							Hibernate.initialize(dl.getTestCase().getTestCasePriority());
							Hibernate.initialize(dl.getTestSuiteList());
							if(dl.getRunConfiguration()!=null){
								Hibernate.initialize(dl.getRunConfiguration());
								Hibernate.initialize(dl.getRunConfiguration().getRunconfiguration());
								if(dl.getRunConfiguration().getRunconfiguration()!=null)
									if(dl.getRunConfiguration().getRunconfiguration().getEnvironmentcombination()!=null){
										Hibernate.initialize(dl.getRunConfiguration().getRunconfiguration().getEnvironmentcombination());
									}
								if(dl.getRunConfiguration().getRunconfiguration().getGenericDevice()!=null){
									Hibernate.initialize(dl.getRunConfiguration().getRunconfiguration().getGenericDevice());
								}
								Hibernate.initialize(dl.getRunConfiguration().getWorkpackage());
							}
							
						}
					}
					log.debug("list successful");
				} catch (RuntimeException re) {
					log.error("list failed", re);
				}
				return listWorkPackageTestCaseExecutionPlan;
	}
	
	@Override
	@Transactional
	public void updateTestCaseExecutionResult(
			TestCaseExecutionResult testCaseExecutionResult) {
		log.debug("adding testCaseExecutionResult instance");
		try {
			sessionFactory.getCurrentSession().update(testCaseExecutionResult);
			log.debug("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}
		
	}

	@Override
	public void addTestCaseConfiguration(
			TestCaseConfiguration testCaseConfiguration) {
		try {
			sessionFactory.getCurrentSession().save(testCaseConfiguration);
			log.debug("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}
	}

	@Override
	public List<WorkPackage> listWorkPackagesByUserIdAndPlannedExecutionDate(
			int userId, String plannedExecutionDate) {
		List<WorkPackage> workPackageList=null;
		workPackageList = sessionFactory.getCurrentSession()
				.createQuery("select distinct wp from WorkPackage as wp, WorkPackageTestCaseExecutionPlan as wptcep" +
								" where (wptcep.tester.userId=:testerId or wptcep.testLead.userId = :testLeadId)" +
								" and wptcep.workPackage.workPackageId = wp.workPackageId")
								.setParameter("testerId", userId)
								.setParameter("testLeadId", userId)
								.list();
		Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackageTestCaseExecutionPlan.class, "wptcep");
		if(userId != 0){
			c.add(Restrictions.disjunction().add(
			        Restrictions.or(Restrictions.eq("tester.userId", userId),
			                		Restrictions.eq("testLead.userId", userId))));
		}
		return null;
	}

	@Override
	@Transactional
	public List<WorkPackage> listWorkPackageByResourceCheckInProductId(
			Integer productId) {

		List<WorkPackage> listOfWorkPackages = new  ArrayList<WorkPackage>();
		
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackage.class, "wp");
			c.createAlias("wp.productBuild", "prodBuild");
			c.createAlias("prodBuild.productVersion", "prodVersion");
			c.createAlias("prodVersion.productMaster", "product");
			c.add(Restrictions.eq("product.productId", productId));
			c.add(Restrictions.eq("wp.isActive", 1));
			listOfWorkPackages = c.list();
			if (!listOfWorkPackages.isEmpty()) {
				log.debug("List of Work packages : "+listOfWorkPackages.size());
				for (WorkPackage workPackage : listOfWorkPackages) {
					Hibernate.initialize(workPackage.getProductBuild().getProductVersion().getProductMaster());
					Hibernate.initialize(workPackage.getWorkFlowEvent());
					if(workPackage.getWorkFlowEvent()!=null){
					Hibernate.initialize(workPackage.getWorkFlowEvent().getWorkFlow());
					}
				}
			}
			log.debug("list successful");
		} catch (Exception e) {
			log.error("list failed", e);
		}
		return listOfWorkPackages;
	}

	@Override
	@Transactional
	public void deleteTestCaseConfigurationByWPTCEID(Integer wptcepId) {
		log.debug("reactivate deleteTestCaseConfigurationByWPTCEID");
		List<TestCaseConfiguration> tcc=null;
		TestCaseConfiguration testCaseConfiguration=null;
		try {
			tcc = sessionFactory
					.getCurrentSession()
					.createQuery("from TestCaseConfiguration tcc where tcc.workpackage_run_list.id = "+ wptcepId).list();
			testCaseConfiguration = (tcc != null && tcc.size() != 0) ? (TestCaseConfiguration) tcc.get(0) : null;
			sessionFactory.getCurrentSession().delete(testCaseConfiguration);

			log.debug("deletion successful");
		} catch (RuntimeException re) {
			log.error("deletion failed", re);
		}		
	}

	@Override
	@Transactional
	public WorkPackage mapWorkpackageEnvCombination(int workpackageId,
			int envCombId, String action) {
		log.debug("mapWorkpackageEnv");

		WorkPackage workPackage = null;
		EnvironmentCombination environment = null;

		try {
			workPackage = getWorkPackageById(workpackageId);
			environment = environmentDAO.getEnvironmentCombinationById(envCombId);

			if (workPackage != null && environment != null) {
				boolean needToUpdateOrAdd = false;

				Set<EnvironmentCombination> environmentSet = workPackage.getEnvironmentCombinationList();
				log.debug("environmentSet>>>>>"+environmentSet.size());	
				if (action.equalsIgnoreCase("Add")) {

					if (environmentSet.size() == 0) {
						needToUpdateOrAdd = true;
					} else {
						EnvironmentCombination environmentForProcessing = environmentSet
								.iterator().next();
						if (environmentForProcessing != null) {
							int alreadyAvailableEnvironmentId = environmentForProcessing
									.getEnvironment_combination_id().intValue();

							if (alreadyAvailableEnvironmentId != envCombId) {
								log.debug("alreadyAvailableEnvironmentId---------->"
												+ alreadyAvailableEnvironmentId);
								log.debug("environmentId---------->"
										+ envCombId);
								log.debug("workPackage.getEnvironmentList().size()="
										+ workPackage.getEnvironmentCombinationList()
												.size());

								EnvironmentCombination environmentAvailable = environmentDAO.getEnvironmentCombinationById(alreadyAvailableEnvironmentId);
								for (WorkPackage wp : environmentAvailable.getWorkpackageSet()) {
									log.debug("wp.getWorkPackageId().intValue()"
											+ wp.getWorkPackageId().intValue());

									if (wp.getWorkPackageId().intValue() == workpackageId) {
										log.debug("workpackage found in environment");
										sessionFactory.getCurrentSession()
												.saveOrUpdate(
														environmentAvailable);
										log.debug("environmentAvailable.getWorkPackageList().size()="
												+ environmentAvailable.getWorkpackageSet().size());
										break;
									}
								}

								needToUpdateOrAdd = true;
							}
						}
					}

					if (needToUpdateOrAdd) {
						workPackage.getEnvironmentCombinationList().add(environment);
						environment.getWorkpackageSet().add(workPackage);
						sessionFactory.getCurrentSession().saveOrUpdate(
								workPackage);
						sessionFactory.getCurrentSession().saveOrUpdate(
								environment);
					}
				}
				else if(action.equalsIgnoreCase("Remove")){
					log.debug("Remove Environmnet from Environmnet Combination");

					try {

						Set<EnvironmentCombination> environmentlist = workPackage.getEnvironmentCombinationList();
						environmentlist.remove(environment);
						
						workPackage.setEnvironmentCombinationList(environmentlist);
						
						sessionFactory.getCurrentSession().save(workPackage);
						
						log.debug("Removed Environmnet Combination from Environmnet Combination successfully");
					} catch (RuntimeException re) {
						log.error("Failed to remove workPackage from workpackage", re);
						
					}
				}
			}
		} catch (RuntimeException re) {
			log.error("list specific failed", re);
		}
		return workPackage;
	}

	@Override
	@Transactional
	public List<WorkPackageDemandProjectionDTO> listWorkpackageDemandProdjectionByDate(Date selectedDate, Integer testFactoryLabId, Integer shiftTypeId) {
		List<WorkPackageDemandProjectionDTO> listWorkPackageDemandProjectionDTO = new ArrayList<WorkPackageDemandProjectionDTO>();
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackageDemandProjection.class, "wpdp");
			c.createAlias("wpdp.workPackage", "workPackage");
			c.createAlias("workPackage.productBuild", "productbuild");
			c.createAlias("productbuild.productVersion", "productVersion");
			c.createAlias("productVersion.productMaster", "product");
			c.createAlias("product.testFactory", "tf");
			c.createAlias("tf.testFactoryLab", "tfl");
			c.createAlias("wpdp.workShiftMaster", "ws");
			c.createAlias("ws.shiftType", "st");
			
			ProjectionList projectionList = Projections.projectionList();
				c.add(Restrictions.eq("st.shiftTypeId", shiftTypeId));
				c.add(Restrictions.eq("wpdp.workDate", selectedDate));
			if (testFactoryLabId != null) {
				c.add(Restrictions.eq("tfl.testFactoryLabId", testFactoryLabId));
			}
			projectionList.add(Property.forName("wpdp.workDate"));
			projectionList.add(Projections.sum("wpdp.resourceCount").as("resourceCount"));
			
			c.setProjection(projectionList);
			
			List<Object[]> list = c.list();
			
			WorkPackageDemandProjectionDTO workPackageDemandProjectionDTO = null;
			for (Object[] row : list) {
				workPackageDemandProjectionDTO = new WorkPackageDemandProjectionDTO();
				log.debug("--->"+(Date)row[0]);
				workPackageDemandProjectionDTO.setWorkDate(((Date)row[0]));
				if((Long)row[1] != null){
					log.debug("((Long)row[1]).intValue()--->"+((Long)row[1]).intValue());
					workPackageDemandProjectionDTO.setResourceCount(((Float)row[1]));
				}else{
					workPackageDemandProjectionDTO.setResourceCount(0F);
				}
				listWorkPackageDemandProjectionDTO.add(workPackageDemandProjectionDTO);
			}
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}catch(Exception e){
			e.printStackTrace();
		}
		return listWorkPackageDemandProjectionDTO;
	}

	public List<TestCaseDTO> listWorkPackageTestCasesExecutionPlanByWorkPackageIdGroupByTestcase(
			int workPackageId,String envId,String executionPriority,String result,int jtStartIndex,int jtPageSize,int sortBy,int testcaseId) {
		List<TestCaseDTO> testCaseDTOList = new ArrayList<TestCaseDTO>(0);
		
		try {
			List<Object[]> lisObj=new ArrayList<Object[]>(0);
			List<WorkPackageTestCaseExecutionPlan> listWorkPackageTestCaseExecutionPlan=null;
			 WorkPackage wp=null;
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackageTestCaseExecutionPlan.class, "wptcep");
						c.createAlias("wptcep.testCase", "testCase");
						c.createAlias("wptcep.testCaseExecutionResult", "tcer");
			if (workPackageId !=0 || sortBy==-1) {
				c.createAlias("wptcep.workPackage", "workPackage");
				c.add(Restrictions.eq("workPackage.workPackageId", workPackageId));
			} 
			c.addOrder(Order.asc("testCase.testCaseId"));
			if(sortBy==4){
				c.createAlias("wptcep.executionPriority", "executionPriority");
				c.addOrder(Order.asc("executionPriority.executionPriorityId"));
			}
			else if(sortBy==2){
				
				c.addOrder(Order.asc("tcer.result"));
			}else if(sortBy==1){
				c.createAlias("wptcep.runConfiguration", "wprunConfig");
				c.addOrder(Order.asc("wprunConfig.workpackageRunConfigurationId"));
			}else if(sortBy==0){
				if(testcaseId!=0){
					c.add(Restrictions.eq("testCase.testCaseId",testcaseId));
				}else{
				c.addOrder(Order.asc("testCase.testCaseId"));
				}
			}
			
			listWorkPackageTestCaseExecutionPlan=c.list();
			
		
			ProjectionList projectionList = Projections.projectionList();
			projectionList = Projections.projectionList();
			
			projectionList.add(Property.forName("testCase.testCaseId").as("testCaseId"));
			if(sortBy==2){
			projectionList.add(Property.forName("testCase.testCaseName").as("testCaseName"));
			projectionList.add(Property.forName("testCase.testCaseDescription").as("testCaseDescription"));
			projectionList.add(Property.forName("testCase.testCaseCode").as("testCaseCode"));
			projectionList.add(Property.forName("testCase.testCaseSource").as("testCaseSource"));
			projectionList.add(Property.forName("testCase.testcaseExecutionType").as("testcaseExecutionType"));
			projectionList.add(Property.forName("testCase.testcaseinput").as("testcaseinput"));
			projectionList.add(Property.forName("testCase.testcaseexpectedoutput").as("testcaseexpectedoutput"));
			projectionList.add(Property.forName("testCase.preconditions").as("preconditions"));
			projectionList.add(Property.forName("tcer.observedOutput").as("observedOutput"));
			projectionList.add(Property.forName("tcer.result").max().as("result"));
			}
			
			projectionList.add(Property.forName("executionPriority.executionPriorityId").as("priorityId"));
		
			if( sortBy==2){
				projectionList.add(Projections.sum("tcer.defectsCount"));
				projectionList.add(Projections.count("tcer.id"));
				projectionList.add(Projections.count("testCase.testCaseId"));
			}
			
			else if(sortBy==2 || sortBy==6){
			projectionList.add(Projections.groupProperty("testCase.testCaseId"));
			}
			c.setProjection(projectionList);
			if(jtStartIndex!=0 && jtPageSize!=0){
				 c.setFirstResult(jtStartIndex).setMaxResults(jtPageSize);
			}
			 lisObj=c.list();
			
			 Integer totalWPTestCase=0;
			 Integer totalPass=0;
			 Integer totalFail=0;
			 Integer totalNoRun=0;
			 Integer totalBlock=0;
			 Integer totalExecutedTesCases=0;
			 Integer notExecuted=0;
			
			
		  if(sortBy==6){
				Map <DecouplingCategory,Integer[][]>decoupleMap=new HashMap<DecouplingCategory,Integer[][]>();
			    
			    int executionPrirityId=0;
			   
			    for (Object[] row : lisObj) {
			         Integer prorityIdArr[][]={{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0}};
			    	List<TestCaseExecutionResult> trcList=null;
			    	Set<DecouplingCategory> decouplingSet=null;
			    	TestCaseList tcList=testCaseListDAO.getByTestCaseId((Integer)row[0]);
			    	decouplingSet=tcList.getDecouplingCategory();
			    	if(decouplingSet.size()!=0 ){
			    		for(DecouplingCategory decoupling:decouplingSet){
			    			trcList = listTestCaseExecutionresultBywpRunconfigId(workPackageId,0,(Integer)row[0],0);
			    			if(trcList.size()!=0){
								for(TestCaseExecutionResult tcres:trcList){
								WorkPackageTestCaseExecutionPlan wptceplan=	tcres.getWorkPackageTestCaseExecutionPlan();
								if(wptceplan.getExecutionPriority()!=null){
									 executionPrirityId=wptceplan.getExecutionPriority().getExecutionPriorityId();
									 int PrirityId=executionPrirityId-1;
									if(decoupleMap.containsKey(decoupling)){
										prorityIdArr=decoupleMap.get(decoupling);
										if(PrirityId>=0){
											
											totalPass=prorityIdArr[PrirityId][0];
											 totalFail=prorityIdArr[PrirityId][1];
											 totalNoRun=prorityIdArr[PrirityId][2];
											 totalBlock=prorityIdArr[PrirityId][3];
											
								}	
						      } 
						}
								int PrirityId=executionPrirityId-1;
								if(tcres!=null){
								if(tcres.getResult().equalsIgnoreCase(IDPAConstants.EXECUTION_RESULT_PASSED) || tcres.getResult().equals("1")){
									prorityIdArr[PrirityId][0]=	++totalPass;
								}else if(tcres.getResult().equalsIgnoreCase(IDPAConstants.EXECUTION_RESULT_FAILED) || tcres.getResult().equals("2") ){
									prorityIdArr[PrirityId][1]=++totalFail;
								}else if(tcres.getResult().equals(IDPAConstants.EXECUTION_RESULT_NORUN)){
									prorityIdArr[PrirityId][2]=++totalNoRun;
								}else if(tcres.getResult().equals(IDPAConstants.EXECUTION_RESULT_BLOCKED) ){
									prorityIdArr[PrirityId][3]=++totalBlock;
								}
								}
								}
									
								}
			    			decoupleMap.put(decoupling, prorityIdArr);
			    			 totalPass=0;
							 totalFail=0;
							 totalNoRun=0;
							 totalBlock=0;
							 prorityIdArr=null;
								}
			    			
			    		}
			    	}
			    	
			    	for (Map.Entry<DecouplingCategory,Integer[][]> entry : decoupleMap.entrySet()) {
			    		TestCaseDTO	testCaseDTO = new TestCaseDTO();
			    		DecouplingCategory decouple=	entry.getKey();
			    		testCaseDTO.setDecuplingCategory(decouple);
			    		testCaseDTO.setDeCouplepriotiesArry(entry.getValue());
			    		testCaseDTOList.add(testCaseDTO);
			    		testCaseDTO=null;
			    	}
									    	
			    	lisObj=null;
					return testCaseDTOList;
			    }	
		else{
			Integer envCount=0;
			if(listWorkPackageTestCaseExecutionPlan.size()!=0){
				WorkPackageTestCaseExecutionPlan wptcplan=listWorkPackageTestCaseExecutionPlan.get(0);
				WorkPackage wp1=wptcplan.getWorkPackage();
				
				Set<TestRunJob > testRunjobset=wp1.getTestRunJobSet();
				 envCount=testRunjobset.size();
			}
			
				 if(lisObj.size()!=0){
					
				for (Object[] row : lisObj) {
					 TestCaseDTO	testCaseDTO = new TestCaseDTO();
					testCaseDTO.setTestCaseId((Integer)row[0]);
					testCaseDTO.setTestCaseName((String)row[1]);
					testCaseDTO.setTestCaseDescription((String)row[2]);
					testCaseDTO.setTestCaseCode((String)(row[3]));
					testCaseDTO.setTestCaseSource((String)row[4]);
					testCaseDTO.setTestcaseExecutionType((Integer)row[5]);
					testCaseDTO.setTestcaseinput((String)(row[6]));
					testCaseDTO.setTestcaseexpectedoutput((String)(row[7]));
					testCaseDTO.setPreconditions((String)row[8]);
					testCaseDTO.setObservedOutput((String)row[9]);
					testCaseDTO.setResult((String)row[10]);
					testCaseDTO.setDefectsCount(((Long)row[12]).longValue());
					testCaseDTO.setTestCaseCountOfRunconfig(((Long)row[13]).longValue());
					testCaseDTO.setEnvironmentCount((long)envCount);
					testCaseDTOList.add(testCaseDTO);
					testCaseDTO=null;
				}
			 }
				lisObj=null;
				
		}
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return testCaseDTOList;
	}
@Override
@Transactional
public void saveTestStepExecutionResult(
		List<TestStepExecutionResult> testStepExecutionResultList) {
	log.debug("saveTestStepExecutionResult ");
	int count = 0;
	try {

		if (testStepExecutionResultList == null || testStepExecutionResultList.isEmpty()) {
			
		}
		for (TestStepExecutionResult tser : testStepExecutionResultList) {
			sessionFactory.getCurrentSession().save(tser);
			count++;
		}
		log.debug("teststep Added " +count);
	} catch (Exception e) {

		log.error("Unable to create test step execution result", e);
	}


}

@Override
@Transactional
public void saveTestStepExecutionResult(
		TestStepExecutionResult testStepExecutionResultList) {
	log.debug("saveTestStepExecutionResult ");
	int count = 0;
	try {

			sessionFactory.getCurrentSession().save(testStepExecutionResultList);
		
		log.debug("teststep Added " +count);
	} catch (Exception e) {

		log.error("Unable to create test step execution result", e);
	}


}
@Override
@Transactional
public List<TestCaseConfiguration> listTestCaseConfigurations(int workpackageId) {
	List<TestCaseConfiguration> listOfTestCaseConfiguration = new  ArrayList<TestCaseConfiguration>();
	
	try {
		Criteria c = sessionFactory.getCurrentSession().createCriteria(TestCaseConfiguration.class, "tcc");
		c.createAlias("tcc.workpackage_run_list", "wptcep");
		c.createAlias("wptcep.workPackage", "wp");
		c.add(Restrictions.eq("wp.workPackageId", workpackageId));
		listOfTestCaseConfiguration = c.list();
		if (!listOfTestCaseConfiguration.isEmpty()) {
			log.debug("List of listOfTestCaseConfiguration : "+listOfTestCaseConfiguration.size());
			for (TestCaseConfiguration tcc : listOfTestCaseConfiguration) {
				Hibernate.initialize(tcc.getEnvironmentCombination());
				Hibernate.initialize(tcc.getWorkpackage_run_list());
			}
		}
		log.debug("list successful");
	} catch (Exception e) {
		log.error("list failed", e);
	}
	return listOfTestCaseConfiguration;
}

@Override
@Transactional
public void deleteTestStepResult(Integer testCaseExecutionResultId) {
	log.debug("reactivate deleteTestStepResult");
	List<TestStepExecutionResult> tser=null;
	TestStepExecutionResult testStepExecutionResult=null;
	try {
		tser = sessionFactory
				.getCurrentSession()
				.createQuery("from TestStepExecutionResult tser where tser.testCaseExecutionResult.testCaseExecutionResultId = "+ testCaseExecutionResultId).list();
		if(tser!=null){
		for(TestStepExecutionResult tStepExecutionResult:tser){
			sessionFactory.getCurrentSession().delete(tStepExecutionResult);
		}
	}
		log.debug("deletion successful");
	} catch (RuntimeException re) {
		log.error("deletion failed", re);
	}		

}

	@Override
	@Transactional
	public List<WorkPackageTestSuite> listWorkPackageTestSuite(Integer workPackageId,int startIndex, int pageSize) {
			log.debug("listing listWorkPackageTestSuite instance");
			List<WorkPackageTestSuite> workPackageTestSuites = null;
			try {
				if(startIndex!=0 && pageSize!=0){
					workPackageTestSuites = sessionFactory
						.getCurrentSession()
						.createQuery(
								"from WorkPackageTestSuite wpts where wpts.workPackage.workPackageId = "
										+ workPackageId
										+ " order by wpts.testSuite.testSuiteId asc")
						.setFirstResult(startIndex).setMaxResults(pageSize).list();
				}else{
						
					workPackageTestSuites = sessionFactory
							.getCurrentSession()
							.createQuery(
									"from WorkPackageTestSuite wpts where wpts.workPackage.workPackageId = "
											+ workPackageId
											+ " order by wpts.testSuite.testSuiteId asc")
							.list();

				}
				if (!(workPackageTestSuites == null || workPackageTestSuites
						.isEmpty())) {
					for (WorkPackageTestSuite dl : workPackageTestSuites) {
						Hibernate.initialize(dl.getTestSuite().getProductMaster());
						Hibernate.initialize(dl.getWorkPackage().getProductBuild().getProductVersion().getProductMaster());
						Hibernate.initialize(dl.getTestSuite());
					}
				}
				log.debug("list successful"+workPackageTestSuites.size());
			} catch (RuntimeException re) {
				log.error("list failed", re);
			}
			return workPackageTestSuites;

	}

	@Override
	@Transactional
	public int addNewWorkPackageTestSuite(
			List<WorkPackageTestSuite> workPackageTestSuites) {
		log.debug("Initializing workpackge with new test suite");
		int count = 0;
		try {

			if (workPackageTestSuites == null || workPackageTestSuites.isEmpty()) {
				return 0;
			}
			for (WorkPackageTestSuite workPackageTestSuite : workPackageTestSuites) {
				sessionFactory.getCurrentSession().save(workPackageTestSuite);
				count++;
			}
			log.debug("Testsuite initialized for workpackage " + workPackageTestSuites.size());
			log.debug("added workpackage test suite successfully");
		} catch (Exception e) {

			log.error("Unable to initialize workpackage with new testsuite", e);
			return count;
		}

		return count;
	}

	@Override
	@Transactional
	public int seedWorkPackageWithNewTestSuites(
			List<TestSuiteList> newTestSuites, Integer workPackageId) {
		int workPackagesTestSuiteCount = 0;
		WorkPackage workPackage = getWorkPackageById(workPackageId);
			List<WorkPackageTestSuite> workPackageTestSuites = new ArrayList<WorkPackageTestSuite>();
			WorkPackageTestSuite workPackageTestSuite = null;
			for (TestSuiteList testSuite : newTestSuites) {
				workPackageTestSuite = new WorkPackageTestSuite();
				workPackageTestSuite.setTestSuite(testSuite);
				workPackageTestSuite.setWorkPackage(workPackage);
				workPackageTestSuite.setCreatedDate(new Date(System.currentTimeMillis()));
				workPackageTestSuite.setEditedDate(new Date(System.currentTimeMillis()));
				workPackageTestSuite.setIsSelected(0);
				workPackageTestSuite.setStatus("ACTIVE");
				workPackageTestSuites.add(workPackageTestSuite);
			}
			workPackagesTestSuiteCount = addNewWorkPackageTestSuite(workPackageTestSuites);
			return workPackagesTestSuiteCount;	
		}


	@Override
	@Transactional
	public WorkPackageTestSuite getWorkPackageTestSuiteById(Integer testSuiteId) {
		log.debug("getting WorkPackageTestSuite instance by id");
		WorkPackageTestSuite workPackageTestSuite = null;
		try {

			List list=sessionFactory.getCurrentSession().createQuery("from WorkPackageTestSuite wp where wp.workpackageTestSuiteId=:testSuiteId")
					.setParameter("testSuiteId", testSuiteId)
					.list();

			
			workPackageTestSuite = (list != null && list.size() != 0) ? (WorkPackageTestSuite) list
					.get(0) : null;
			if (workPackageTestSuite != null) {
				Hibernate.initialize(workPackageTestSuite.getWorkPackage());
				Hibernate.initialize(workPackageTestSuite.getTestSuite());
				Hibernate.initialize(workPackageTestSuite.getTestSuite().getTestCaseLists());
			}
			log.debug("getByProductId successful");
		} catch (RuntimeException re) {
			log.error("getByProductId failed", re);
		}
		return workPackageTestSuite;

	}


	@Override
	@Transactional
	public WorkPackageTestSuite updateWorkPackageTestSuite(
			WorkPackageTestSuite workPackageTestSuiteFromDB) {
		log.debug("workPackageTestSuiteFromDB"+workPackageTestSuiteFromDB.getWorkPackage().getWorkPackageId());
		int count = 0;
		try {

			if (workPackageTestSuiteFromDB == null) {
				return null;
			}

			sessionFactory.getCurrentSession()
					.saveOrUpdate(workPackageTestSuiteFromDB);

			log.debug("Updated workpackage test suite successfully");
		} catch (Exception e) {

			log.error("Unable to workPackageTestSuiteFromDB ", e);
			return null;
		}

		return workPackageTestSuiteFromDB;
		
	}

	@Override
	@Transactional
	public List<WorkPackageTestCaseExecutionPlan> getWorkPackageTestcaseExecutionPlanByWorkPackageId(
			Integer workPackageId, Set<TestCaseList> testCaseLists) {
		List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlans = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackageTestCaseExecutionPlan.class, "wptcep");
			c.createAlias("wptcep.workPackage", "workPackage");
			c.createAlias("wptcep.testCase", "testCase");
			
			c.add(Restrictions.eq("workPackage.workPackageId", workPackageId));
			c.add(Restrictions.in("testCase.testCaseId",  Arrays.asList(3,7)));
			
			
			workPackageTestCaseExecutionPlans=c.list();
			if (!(workPackageTestCaseExecutionPlans == null || workPackageTestCaseExecutionPlans.isEmpty())) {
				
				for(WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan : workPackageTestCaseExecutionPlans) {
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getTestCase());
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getWorkPackage());
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getTester());
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getTestLead());
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getTestCaseExecutionResult());
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getActualWorkShiftMaster());
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getPlannedWorkShiftMaster());
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getExecutionPriority());
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getRunConfiguration());
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getTestSuiteList());
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getTestCase().getTestcaseTypeMaster());
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getTestCase().getTestCasePriority());
				

					Hibernate.initialize(workPackageTestCaseExecutionPlan.getRunConfiguration());
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getRunConfiguration().getRunconfiguration());
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getRunConfiguration().getWorkpackage());
				}
			}
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
			// throw re;
		}
		return workPackageTestCaseExecutionPlans;

	}

	@Override
	@Transactional
	public WorkPackage deleteRunConfigurationWorkpackage(Integer workpackageId,
			Integer runconfigId, String type) {
		WorkpackageRunConfiguration workpackageRunConfiguration= getWorkpackageRunConfiguration(workpackageId, runconfigId, type);
		try {
			if(workpackageRunConfiguration!=null && workpackageRunConfiguration.getWorkpackageRunConfigurationId()!=null){
				String hql="delete from workpackage_has_runconfiguration  where workpackageRunConfigurationId="+workpackageRunConfiguration.getWorkpackageRunConfigurationId();
				int  	query = (sessionFactory.getCurrentSession().createSQLQuery(hql)).executeUpdate();
				log.debug("deletion successful");
			}
		} catch (RuntimeException re) {
			log.error("deletion failed", re);
		}	
		return getWorkPackageById(workpackageId);
	}

	@Override
	@Transactional
	public WorkpackageRunConfiguration addRunConfigurationWorkpackage(Integer workpackageId,
			Integer runconfigId, String type) {
		WorkPackage workPackage =getWorkPackageByIdWithMinimalnitialization(workpackageId);
		WorkpackageRunConfiguration workpackageRunConfiguration =new WorkpackageRunConfiguration();
		workpackageRunConfiguration.setWorkpackage(workPackage);
		workpackageRunConfiguration.setRunconfiguration(getRunConfigurationById(runconfigId));
		workpackageRunConfiguration.setType(type);
		try {	
			sessionFactory.getCurrentSession().save(workpackageRunConfiguration);
			log.debug("add successful");
		} catch (Exception re) {
			re.printStackTrace();
			log.error("add failed", re);
		}		
		return workpackageRunConfiguration;
	}

	@Override
	@Transactional
	public WorkpackageRunConfiguration getWorkpackageRunConfiguration(
            Integer workpackageId, Integer runconfigId, String type) {
     List<WorkpackageRunConfiguration> workpackageRunConfigurations = null;

     WorkpackageRunConfiguration workpackageRunConfiguration = null;
     try {
            Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkpackageRunConfiguration.class, "wprc");
            c.createAlias("wprc.workpackage", "workPackage");
            c.createAlias("wprc.runconfiguration", "runconfiguration");
           
            c.add(Restrictions.eq("workPackage.workPackageId",workpackageId));
            if(runconfigId!=-1){
            c.add(Restrictions.eq("runconfiguration.runconfigId", runconfigId ));
            }
            c.add(Restrictions.eq("wprc.type", type ));
            c.add(Restrictions.eq("runconfiguration.runConfigStatus", 1));
           
            workpackageRunConfigurations=c.list();                
            workpackageRunConfiguration = (workpackageRunConfigurations != null && workpackageRunConfigurations.size() != 0) ? (WorkpackageRunConfiguration) workpackageRunConfigurations
                         .get(0) : null;

            if(workpackageRunConfiguration!=null){
                         Hibernate.initialize(workpackageRunConfiguration.getRunconfiguration());
                         Hibernate.initialize(workpackageRunConfiguration.getWorkpackage());
            }
            log.debug("list all successful");
     } catch (RuntimeException re) {
            log.error("list all failed", re);
     }
     return workpackageRunConfiguration;
	}
	
	@Override
	@Transactional
	public List<WorkpackageRunConfiguration> getWorkpackageRunConfigurationList(
			Integer workpackageId, Integer runconfigId, String type) {
		List<WorkpackageRunConfiguration> workpackageRunConfigurations = null;

		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkpackageRunConfiguration.class, "wprc");
			c.createAlias("wprc.workpackage", "workPackage");
			c.createAlias("wprc.runconfiguration", "runconfiguration");
			
			c.add(Restrictions.eq("workPackage.workPackageId",workpackageId));
			if(runconfigId!=null)
				c.add(Restrictions.eq("runconfiguration.runconfigId", runconfigId ));
				c.add(Restrictions.eq("runconfiguration.runConfigStatus", 1 ));
			if(type!=null)
				c.add(Restrictions.eq("wprc.type", type ));
			
			workpackageRunConfigurations=c.list();			

			if(workpackageRunConfigurations!=null && !workpackageRunConfigurations.isEmpty()){
				
				for(WorkpackageRunConfiguration workpackageRunConfiguration:workpackageRunConfigurations){
					Hibernate.initialize(workpackageRunConfiguration.getRunconfiguration());
					Hibernate.initialize(workpackageRunConfiguration.getWorkpackage());
					Set<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlans=workpackageRunConfiguration.getWorkPackageTestCaseExecutionPlan();
					for(WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan:workPackageTestCaseExecutionPlans){
						Hibernate.initialize(workPackageTestCaseExecutionPlan.getRunConfiguration());
						if(workPackageTestCaseExecutionPlan.getRunConfiguration()!=null){
							if(workPackageTestCaseExecutionPlan.getRunConfiguration().getWorkpackage()!=null){
							Hibernate.initialize(workPackageTestCaseExecutionPlan.getRunConfiguration().getWorkpackage());
							}
							Hibernate.initialize(workPackageTestCaseExecutionPlan.getRunConfiguration().getRunconfiguration());
							Hibernate.initialize(workPackageTestCaseExecutionPlan.getRunConfiguration().getRunconfiguration().getEnvironmentcombination());
						}
						Set<Environment> environments=workPackageTestCaseExecutionPlan.getEnvironmentList();
						Hibernate.initialize(environments);
						for(Environment environment:environments){
							Hibernate.initialize(environment.getEnvironmentCategory());
							Hibernate.initialize(environment.getEnvironmentCategory().getEnvironmentGroup());
						}
					}
					for(WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan:workPackageTestCaseExecutionPlans){
						Set<TestCaseConfiguration> testCaseConfigurationSet =workPackageTestCaseExecutionPlan.getTestCaseConfigurationSet();
						for(TestCaseConfiguration testCaseConfiguration:testCaseConfigurationSet){
							Hibernate.initialize(testCaseConfiguration.getWorkpackage_run_list());
							Hibernate.initialize(testCaseConfiguration.getEnvironmentCombination());
						}
					}
						
						for(WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan:workPackageTestCaseExecutionPlans){
							Hibernate.initialize(workPackageTestCaseExecutionPlan.getTestCaseExecutionResult());
							TestCaseExecutionResult trcRes=workPackageTestCaseExecutionPlan.getTestCaseExecutionResult();
							if(trcRes!=null){
							Set<TestStepExecutionResult> tcstepexecRes=trcRes.getTestStepExecutionResultSet();
							if(tcstepexecRes.size()!=0){
							for(TestStepExecutionResult testStepExecRes:tcstepexecRes){
								Hibernate.initialize(testStepExecRes.getTestCaseExecutionResult());
							}
							}
							}
							
						}
					Hibernate.initialize(workpackageRunConfiguration.getWorkPackageTestCaseExecutionPlan());
					if(workpackageRunConfiguration.getRunconfiguration().getGenericDevice()!=null){
						Hibernate.initialize(workpackageRunConfiguration.getRunconfiguration().getGenericDevice());
					}
					
					Hibernate.initialize(workpackageRunConfiguration.getRunconfiguration().getEnvironmentcombination());
					
				}
			}
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return workpackageRunConfigurations;
	}

	@Override
	@Transactional
	public RunConfiguration getRunConfigurationById(Integer runConfigId) {
		List<RunConfiguration> runConfigurations = null;

		RunConfiguration runConfiguration = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(RunConfiguration.class, "rc");
			
			c.add(Restrictions.eq("rc.runconfigId",runConfigId));
			
			runConfigurations=c.list();			
			runConfiguration = (runConfigurations != null && runConfigurations.size() != 0) ? (RunConfiguration) runConfigurations
					.get(0) : null;

			if(runConfiguration!=null){
					Hibernate.initialize(runConfiguration.getEnvironmentcombination());
					Hibernate.initialize(runConfiguration.getGenericDevice());
					Hibernate.initialize(runConfiguration.getProduct());
			}
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return runConfiguration;	
	}

	@Override
	@Transactional
	public WorkpackageRunConfiguration getWorkpackageRunConfigurationByWPTCEP(
			Integer id) {
		List<WorkpackageRunConfiguration> workpackageRunConfigurations = null;

		WorkpackageRunConfiguration workpackageRunConfiguration = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkpackageRunConfiguration.class, "wprc");
			
			c.add(Restrictions.eq("wprc.workpackageRunConfigurationId",id));
			
			workpackageRunConfigurations=c.list();			
			workpackageRunConfiguration = (workpackageRunConfigurations != null && workpackageRunConfigurations.size() != 0) ? (WorkpackageRunConfiguration) workpackageRunConfigurations
					.get(0) : null;

			if(workpackageRunConfiguration!=null){
					Hibernate.initialize(workpackageRunConfiguration.getRunconfiguration());
					if(workpackageRunConfiguration.getRunconfiguration()!=null){
						Hibernate.initialize(workpackageRunConfiguration.getRunconfiguration().getEnvironmentcombination());
						Hibernate.initialize(workpackageRunConfiguration.getRunconfiguration().getGenericDevice());
					}
					Hibernate.initialize(workpackageRunConfiguration.getWorkpackage());
			}
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return workpackageRunConfiguration;
	}

	@Override
	@Transactional
	public void deleteRunConfigurationWorkpackageById(WorkpackageRunConfiguration id) {
		try{
			if(id!=null)
				sessionFactory.getCurrentSession().delete(id);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	@Transactional
	public WorkPackage addWorkpackageToTestRunPlan(TestRunPlan testRunPlan,
			Map<String, String> mapData,UserList user,HttpServletRequest req,TestRunPlanGroup testRunPlanGroup,WorkPackage workPackage) {
		WorkFlowEvent workFlowEvent =null;
		ProductBuild productBuild =null;

		if(workPackage==null){
			workPackage=new WorkPackage();
			workPackage.setName(mapData.get("workpackageName"));
			workPackage.setDescription(mapData.get("description"));
			
			workPackage.setCreateDate(DateUtility.getCurrentTime());
			workPackage.setModifiedDate(DateUtility.getCurrentTime());
			workPackage.setStatus(1);
			workPackage.setIsActive(1);
			if(testRunPlan.getExecutionType().getExecutionTypeId()==3)
			{
				ExecutionTypeMaster executionTypeMaster=executionTypeMasterDAO.getExecutionTypeByExecutionTypeId(7);
				workPackage.setWorkPackageType(executionTypeMaster);
				workPackage.setActualStartDate(DateUtility.getCurrentTime());
				log.info("Setting WP actual start date");
			}else{
				ExecutionTypeMaster executionTypeMaster=executionTypeMasterDAO.getExecutionTypeByExecutionTypeId(8);
				workPackage.setWorkPackageType(executionTypeMaster);
				workPackage.setActualStartDate(DateUtility.getCurrentTime());
			}
			
			if(Integer.parseInt(mapData.get("productBuildId"))!=-1){
				productBuild=	productBuildDAO.getByProductBuildId(Integer.parseInt(mapData.get("productBuildId")), 0);
				workPackage.setProductBuild(productBuild);
			} else {				
				productBuild=productBuildDAO.getLatestProductBuild(testRunPlan.getProductVersionListMaster().getProductMaster().getProductId());
				workPackage.setProductBuild(productBuild);
			}
			
			if(testRunPlan.getProductBuild() != null){
				workPackage.setProductBuild(testRunPlan.getProductBuild());
			}
			
			workPackage.setTestRunPlan(testRunPlan);
			if(testRunPlanGroup!=null && testRunPlanGroup.getTestRunPlanGroupId()!=null){
				workPackage.setTestRunPlanGroup(testRunPlanGroup);
				workPackage.setSourceType(IDPAConstants.WORKPACKAGE_SOURCE_TESTRUNPLANGROUP);
			}else{
				workPackage.setSourceType("TestRunPlan");
			}
			WorkFlow workFlow = getWorkFlowByEntityIdStageId(IDPAConstants.WORKPACKAGE_ENTITY_ID,IDPAConstants.WORKFLOW_STAGE_ID_NEW);
			workFlowEvent = new WorkFlowEvent();
			workFlowEvent.setEventDate(DateUtility.getCurrentTime());
			workFlowEvent.setRemarks("New Workapckage Added :"+workPackage.getName());
			workFlowEvent.setUser(user);
			workFlowEvent.setWorkFlow(workFlow);
			
			addWorkFlowEvent(workFlowEvent);
			workPackage.setWorkFlowEvent(workFlowEvent);
			workPackage.setCreateDate(DateUtility.getCurrentTime());
			workPackage.setModifiedDate(DateUtility.getCurrentTime());
			
			if(testRunPlan.getExecutionType().getExecutionTypeId()==4){
				if(mapData.get("plannedStartDate")!=null && mapData.get("plannedStartDate")!=""){
					workPackage.setPlannedStartDate(DateUtility.dateformatWithOutTime(mapData.get("plannedStartDate")));
				}else{
					workPackage.setPlannedStartDate(DateUtility.getCurrentTime());
				}
				if(mapData.get("plannedEndDate")!=null && mapData.get("plannedEndDate")!=""){
					workPackage.setPlannedEndDate(DateUtility.dateformatWithOutTime(mapData.get("plannedEndDate")));
				}else{
					workPackage.setPlannedEndDate(DateUtility.getCurrentTime());
				}
			}else 	if(testRunPlan.getExecutionType().getExecutionTypeId()==3){
				workPackage.setPlannedStartDate(DateUtility.getCurrentTime());
				workPackage.setPlannedEndDate(DateUtility.getCurrentTime());
			}
			workPackage.setUserList(user);
			workPackage.setIterationNumber(-1);
			LifeCyclePhase lifeCyclePhase = new LifeCyclePhase();
			lifeCyclePhase.setLifeCyclePhaseId(4);
			workPackage.setLifeCyclePhase(lifeCyclePhase);
			//Set Execution Mode to the workpackage based on Test Plan
			workPackage.setTestExecutionMode(testRunPlan.getAutomationMode());
			addWorkPackage(workPackage);
			workFlowEvent.setEntityTypeRefId(workPackage.getWorkPackageId());
			updateWorkFlowEvent(workFlowEvent);
			workPackage.setWorkFlowEvent(workFlowEvent);

			TestCycle tc = new TestCycle();
			tc.setTestRunPlanGroup(testRunPlanGroup);
			tc.setTestCycleStatus(workFlow.getStageName());
			tc.setResult("In Progress");// Result cannot be New So, setting it to In Progress
			tc.setStatus(1);
			tc.setStartTime(DateUtility.getCurrentTime());
			UserList userList= userListDAO.getUserByLoginId(IDPAConstants.USER_FOR_TESTRUN_PLAN);
			tc.setUserList(userList);
			workPackageService.addTestCycle(tc);
			workPackage.setTestCycle(tc);
			updateWorkPackage(workPackage);
			if(workPackage!=null && workPackage.getWorkPackageId()!=null){
				mongoDBService.addWorkPackage(workPackage.getWorkPackageId());
			}

		}else{
			WorkPackage newWorkpackage=new WorkPackage();
			
			newWorkpackage.setName(workPackage.getName());
			newWorkpackage.setDescription(workPackage.getDescription());
			
			newWorkpackage.setCreateDate(DateUtility.getCurrentTime());
			newWorkpackage.setModifiedDate(DateUtility.getCurrentTime());
			newWorkpackage.setStatus(1);
			newWorkpackage.setIsActive(1);
			if(testRunPlanGroup.getExecutionTypeMaster().getExecutionTypeId()==3)
			{
				ExecutionTypeMaster executionTypeMaster=executionTypeMasterDAO.getExecutionTypeByExecutionTypeId(7);
				newWorkpackage.setWorkPackageType(executionTypeMaster);
			}else{
				ExecutionTypeMaster executionTypeMaster=executionTypeMasterDAO.getExecutionTypeByExecutionTypeId(8);
				newWorkpackage.setWorkPackageType(executionTypeMaster);
			}
			
			productBuild=	workPackage.getProductBuild();
			newWorkpackage.setProductBuild(productBuild);
			newWorkpackage.setTestRunPlan(testRunPlan);
			newWorkpackage.setTestRunPlanGroup(testRunPlanGroup);
			newWorkpackage.setSourceType(IDPAConstants.WORKPACKAGE_SOURCE_TESTRUNPLANGROUP);
			WorkFlow workFlow = getWorkFlowByEntityIdStageId(IDPAConstants.WORKPACKAGE_ENTITY_ID,IDPAConstants.WORKFLOW_STAGE_ID_NEW);
			/*TestCycle tc = new TestCycle();
			tc.setTestRunPlanGroup(testRunPlanGroup);
			tc.setTestCycleStatus(workFlow.getStageName());
			tc.setResult("New");
			tc.setStatus(1);
			tc.setStartTime(DateUtility.getCurrentTime());
			tc.setUserList(user);
			workPackageService.addTestCycle(tc);
			newWorkpackage.setTestCycle(tc);*/
			newWorkpackage.setTestCycle(workPackage.getTestCycle());
			workFlowEvent = new WorkFlowEvent();
			workFlowEvent.setEventDate(DateUtility.getCurrentTime());
			workFlowEvent.setRemarks("New Workapckage Added :"+workPackage.getName());
			workFlowEvent.setUser(workPackage.getWorkFlowEvent().getUser());
			workFlowEvent.setWorkFlow(workFlow);
			
			addWorkFlowEvent(workFlowEvent);
			newWorkpackage.setWorkFlowEvent(workFlowEvent);
			newWorkpackage.setPlannedEndDate(workPackage.getPlannedEndDate());
			newWorkpackage.setPlannedStartDate(workPackage.getPlannedStartDate());
			newWorkpackage.setUserList(workPackage.getUserList());
			newWorkpackage.setIterationNumber(-1);
			//Set Execution Mode to the workpackage based on Test Plan
			newWorkpackage.setTestExecutionMode(testRunPlan.getAutomationMode());
			LifeCyclePhase lifeCyclePhase = new LifeCyclePhase();
			lifeCyclePhase.setLifeCyclePhaseId(4);
			newWorkpackage.setLifeCyclePhase(lifeCyclePhase);
			addWorkPackage(newWorkpackage);
			if(newWorkpackage!=null && newWorkpackage.getWorkPackageId()!=null){
				mongoDBService.addWorkPackage(newWorkpackage.getWorkPackageId());
			}

			workPackage=newWorkpackage;
		}
		if(testRunPlan.getAutomationMode() != null && testRunPlan.getAutomationMode().equalsIgnoreCase(IDPAConstants.AUTOMATION_MODE_ATTENDED))
			workpackageExxecutionPlan(workPackage,testRunPlan,req);
		return workPackage;
	}
	
	@Override
	@Transactional
	public WorkPackage addWorkpackageToTestRunPlan(TestRunPlan testRunPlan,
			Map<String, String> mapData,UserList user,HttpServletRequest req,TestRunPlanGroup testRunPlanGroup,WorkPackage workPackage,String deviceNames) {
		WorkFlowEvent workFlowEvent =null;
		ProductBuild productBuild =null;

		if(workPackage==null){
			workPackage=new WorkPackage();
			workPackage.setName(mapData.get("workpackageName"));
			workPackage.setDescription(mapData.get("description"));
			
			workPackage.setCreateDate(DateUtility.getCurrentTime());
			workPackage.setModifiedDate(DateUtility.getCurrentTime());
			workPackage.setStatus(1);
			workPackage.setIsActive(1);
			if(testRunPlan.getExecutionType().getExecutionTypeId()==3)
			{
				ExecutionTypeMaster executionTypeMaster=executionTypeMasterDAO.getExecutionTypeByExecutionTypeId(7);
				workPackage.setWorkPackageType(executionTypeMaster);
			}else{
				ExecutionTypeMaster executionTypeMaster=executionTypeMasterDAO.getExecutionTypeByExecutionTypeId(8);
				workPackage.setWorkPackageType(executionTypeMaster);
			}
			
			if(Integer.parseInt(mapData.get("productBuildId"))!=-1){
				productBuild=	productBuildDAO.getByProductBuildId(Integer.parseInt(mapData.get("productBuildId")), 0);
				workPackage.setProductBuild(productBuild);
			}else{
				productBuild=productBuildDAO.getLatestProductBuild(testRunPlan.getProductVersionListMaster().getProductMaster().getProductId());
				workPackage.setProductBuild(productBuild);
			}
			
			if(testRunPlan.getProductBuild() != null){
				workPackage.setProductBuild(testRunPlan.getProductBuild());
			}
			workPackage.setTestRunPlan(testRunPlan);
			if(testRunPlanGroup!=null && testRunPlanGroup.getTestRunPlanGroupId()!=null){
				workPackage.setTestRunPlanGroup(testRunPlanGroup);
				workPackage.setSourceType("TestRunPlanGroup");
			}else{
				workPackage.setSourceType("TestRunPlan");
			}
			workFlowEvent = new WorkFlowEvent();
			workFlowEvent.setEventDate(DateUtility.getCurrentTime());			
			workFlowEvent.setUser(user);
			workFlowEvent.setWorkFlow(getWorkFlowByEntityIdStageId(IDPAConstants.WORKPACKAGE_ENTITY_ID,IDPAConstants.WORKFLOW_STAGE_ID_NEW));
			
			addWorkFlowEvent(workFlowEvent);
			workPackage.setWorkFlowEvent(workFlowEvent);
			workPackage.setCreateDate(DateUtility.getCurrentTime());
			workPackage.setModifiedDate(DateUtility.getCurrentTime());
			workPackage.setActualStartDate(DateUtility.getCurrentTime());
			if(testRunPlan.getExecutionType().getExecutionTypeId()==4){
				if(mapData.get("plannedStartDate")!=null && mapData.get("plannedStartDate")!=""){
					workPackage.setPlannedStartDate(DateUtility.dateformatWithOutTime(mapData.get("plannedStartDate")));
				}else{
					workPackage.setPlannedStartDate(DateUtility.getCurrentTime());
				}
				if(mapData.get("plannedEndDate")!=null && mapData.get("plannedEndDate")!=""){
					workPackage.setPlannedEndDate(DateUtility.dateformatWithOutTime(mapData.get("plannedEndDate")));
				}else{
					workPackage.setPlannedEndDate(DateUtility.getCurrentTime());
				}
			}else 	if(testRunPlan.getExecutionType().getExecutionTypeId()==3){
				workPackage.setPlannedStartDate(DateUtility.getCurrentTime());
				workPackage.setPlannedEndDate(DateUtility.getCurrentTime());
			}
			workPackage.setUserList(user);
			workPackage.setIterationNumber(-1);
			workPackage.setTestExecutionMode(testRunPlan.getAutomationMode());
			LifeCyclePhase lifeCyclePhase = new LifeCyclePhase();
			lifeCyclePhase.setLifeCyclePhaseId(4);
			workPackage.setLifeCyclePhase(lifeCyclePhase);
			
			addWorkPackage(workPackage);
			workFlowEvent.setRemarks("Created a new Workpackage "+ workPackage.getName() + " with ID " + workPackage.getWorkPackageId() + ".");
			workFlowEvent.setEntityTypeRefId(workPackage.getWorkPackageId());
			updateWorkFlowEvent(workFlowEvent);
			workPackage.setWorkFlowEvent(workFlowEvent);
			updateWorkPackage(workPackage);			
		}else{
			WorkPackage newWorkpackage=new WorkPackage();
			
			newWorkpackage.setName(workPackage.getName());
			newWorkpackage.setDescription(workPackage.getDescription());
			
			newWorkpackage.setCreateDate(DateUtility.getCurrentTime());
			newWorkpackage.setModifiedDate(DateUtility.getCurrentTime());
			newWorkpackage.setStatus(1);
			newWorkpackage.setIsActive(1);
			if(testRunPlanGroup.getExecutionTypeMaster().getExecutionTypeId()==3)
			{
				ExecutionTypeMaster executionTypeMaster=executionTypeMasterDAO.getExecutionTypeByExecutionTypeId(7);
				newWorkpackage.setWorkPackageType(executionTypeMaster);
			}else{
				ExecutionTypeMaster executionTypeMaster=executionTypeMasterDAO.getExecutionTypeByExecutionTypeId(8);
				newWorkpackage.setWorkPackageType(executionTypeMaster);
			}
			
			productBuild=	workPackage.getProductBuild();
			newWorkpackage.setProductBuild(productBuild);
			
			newWorkpackage.setTestRunPlan(testRunPlan);
			newWorkpackage.setTestRunPlanGroup(testRunPlanGroup);
			newWorkpackage.setSourceType("TestRunPlanGroup");
			
			workFlowEvent = new WorkFlowEvent();
			workFlowEvent.setEventDate(DateUtility.getCurrentTime());
			workFlowEvent.setRemarks("New Workpackage Added :"+workPackage.getName());
			workFlowEvent.setUser(workPackage.getWorkFlowEvent().getUser());
			workFlowEvent.setWorkFlow(getWorkFlowByEntityIdStageId(IDPAConstants.WORKPACKAGE_ENTITY_ID,IDPAConstants.WORKFLOW_STAGE_ID_NEW));
			
			addWorkFlowEvent(workFlowEvent);
			newWorkpackage.setWorkFlowEvent(workFlowEvent);
			newWorkpackage.setPlannedEndDate(workPackage.getPlannedEndDate());
			newWorkpackage.setPlannedStartDate(workPackage.getPlannedStartDate());
			newWorkpackage.setUserList(user);
			newWorkpackage.setIterationNumber(-1);
			LifeCyclePhase lifeCyclePhase = new LifeCyclePhase();
			lifeCyclePhase.setLifeCyclePhaseId(4);
			newWorkpackage.setLifeCyclePhase(lifeCyclePhase);
			newWorkpackage.setTestExecutionMode(testRunPlan.getAutomationMode());
			addWorkPackage(newWorkpackage);
			workPackage=newWorkpackage;
		}
		if(testRunPlan.getAutomationMode() != null && testRunPlan.getAutomationMode().equalsIgnoreCase(IDPAConstants.AUTOMATION_MODE_ATTENDED))
			workpackageExxecutionPlanByDevices(workPackage,testRunPlan,req,deviceNames);
		return workPackage;
	}
	
		//mamtha
	public void workpackageExxecutionPlanByDevices(WorkPackage workPackage,TestRunPlan testRunPlan,HttpServletRequest req,String deviceNames){
		Integer workPackageId=workPackage.getWorkPackageId();
		
		//workpackage TestSuite plan starts
			WorkFlowEvent workFlowEvent=null;
			String jobIds="";
	 		int workPackagesTestSuiteCount =0;
			Set<TestSuiteList> testSuiteLists= testRunPlan.getTestSuiteLists();
			List<WorkPackageTestSuite> workPackageTestSuites = new ArrayList<WorkPackageTestSuite>();
			WorkPackageTestSuite workPackageTestSuite = null;
			for (TestSuiteList testSuite : testSuiteLists) {
				workPackageTestSuite = new WorkPackageTestSuite();
				workPackageTestSuite.setTestSuite(testSuite);
				workPackageTestSuite.setWorkPackage(workPackage);
				workPackageTestSuite.setCreatedDate(new Date(System.currentTimeMillis()));
				workPackageTestSuite.setEditedDate(new Date(System.currentTimeMillis()));
				workPackageTestSuite.setIsSelected(1);
				workPackageTestSuite.setStatus("ACTIVE");
				if(workPackageTestSuite.getWorkPackage().getWorkFlowEvent() != null && workPackageTestSuite.getWorkPackage().getWorkFlowEvent().getWorkFlow() != null &&
						workPackageTestSuite.getWorkPackage().getWorkFlowEvent().getWorkFlow().getStageId() != null &&
						workPackageTestSuite.getWorkPackage().getWorkFlowEvent().getWorkFlow().getStageValue() != null &&
						workPackageTestSuite.getWorkPackage().getWorkFlowEvent().getWorkFlow().getStageId()!=IDPAConstants.WORKFLOW_STAGE_ID_PLANNING && workPackageTestSuite.getWorkPackage().getWorkFlowEvent().getWorkFlow().getStageValue()<20){
					WorkFlow workFlow=getWorkFlowByEntityIdStageId(IDPAConstants.WORKPACKAGE_ENTITY_ID, IDPAConstants.WORKFLOW_STAGE_ID_PLANNING);
					workFlowEvent = new WorkFlowEvent();
					workFlowEvent.setEventDate(DateUtility.getCurrentTime());
					workFlowEvent.setRemarks("Planning Workapckage TestSuite:"+workPackage.getName());
					UserList userAdmin= userListDAO.getUserByLoginId(IDPAConstants.ADMIN_LOGIN_ID);
					workFlowEvent.setUser(userAdmin);
					workFlowEvent.setWorkFlow(workFlow);
					workPackage.setWorkFlowEvent(workFlowEvent);
					addWorkFlowEvent(workFlowEvent);
					updateWorkPackage(workPackage);
				}
				workPackageTestSuites.add(workPackageTestSuite);
				mapWorkpackageWithTestSuite(workPackageTestSuite.getWorkPackage().getWorkPackageId(),testSuite.getTestSuiteId(),"Add");
			}
			
			
			workPackagesTestSuiteCount = addNewWorkPackageTestSuite(workPackageTestSuites);
			Set<WorkPackageTestSuite> workPackageTS=new HashSet<WorkPackageTestSuite>();
			workPackageTS.addAll(workPackageTestSuites);
			workPackage.setWorkPackageTestSuites(workPackageTS);
			updateWorkPackage(workPackage);
			//workpackage TestSuite plan end
			
			//workpackage feature plan starts
			int workPackagesFeatureCount =0;
			Set<ProductFeature> featureList= testRunPlan.getFeatureList();
			List<WorkPackageFeature> workPackageFeatures = new ArrayList<WorkPackageFeature>();
			WorkPackageFeature workPackageFeature = null;
			for (ProductFeature feature : featureList) {
				workPackageFeature = new WorkPackageFeature();
				workPackageFeature.setFeature(feature);
				workPackageFeature.setWorkPackage(workPackage);
				workPackageFeature.setCreatedDate(new Date(System.currentTimeMillis()));
				workPackageFeature.setEditedDate(new Date(System.currentTimeMillis()));
				workPackageFeature.setIsSelected(1);
				workPackageFeature.setStatus("ACTIVE");
				if(workPackageTestSuite.getWorkPackage().getWorkFlowEvent() != null && workPackageTestSuite.getWorkPackage().getWorkFlowEvent().getWorkFlow() != null &&
						workPackageTestSuite.getWorkPackage().getWorkFlowEvent().getWorkFlow().getStageId() != null &&
						workPackageTestSuite.getWorkPackage().getWorkFlowEvent().getWorkFlow().getStageValue() != null && workPackageFeature.getWorkPackage().getWorkFlowEvent().getWorkFlow().getStageId()!=IDPAConstants.WORKFLOW_STAGE_ID_PLANNING&& workPackageFeature.getWorkPackage().getWorkFlowEvent().getWorkFlow().getStageValue()<20){
					WorkFlow workFlow=getWorkFlowByEntityIdStageId(IDPAConstants.WORKPACKAGE_ENTITY_ID, IDPAConstants.WORKFLOW_STAGE_ID_PLANNING);
					workFlowEvent = new WorkFlowEvent();
					workFlowEvent.setEventDate(DateUtility.getCurrentTime());
					workFlowEvent.setRemarks("Planning Workapckage Feature :"+workPackage.getName());
					UserList userAdmin= userListDAO.getUserByLoginId(IDPAConstants.ADMIN_LOGIN_ID);
					workFlowEvent.setUser(userAdmin);
					workFlowEvent.setWorkFlow(workFlow);
					workPackage.setWorkFlowEvent(workFlowEvent);
					addWorkFlowEvent(workFlowEvent);
					updateWorkPackage(workPackage);
				}
				workPackageFeatures.add(workPackageFeature);
				mapWorkpackageWithFeature(workPackageFeature.getWorkPackage().getWorkPackageId(),feature.getProductFeatureId(),"Add");
			}
			
			
			log.debug("New testcases to be added for the workpackage : "+ workPackage.getWorkPackageId() + " : count : " + workPackageTestSuites.size());
			workPackagesFeatureCount = addNewWorkPackageFeature(workPackageFeatures);
			Set<WorkPackageFeature> workPackageFeatureSet=new HashSet<WorkPackageFeature>();
			workPackageFeatureSet.addAll(workPackageFeatures);
			workPackage.setWorkPackageFeature(workPackageFeatureSet);
			updateWorkPackage(workPackage);
			//workpackage feature plan ends
			
			//workpackage testcase plan starts
			int workPackagesTestCaseCount =0;
			Set<TestCaseList> testCaseList= testRunPlan.getTestCaseList();
			List<WorkPackageTestCase> workPackageTestCases = new ArrayList<WorkPackageTestCase>();
			WorkPackageTestCase workPackageTestCase = null;
			for (TestCaseList testCase : testCaseList) {
				workPackageTestCase = new WorkPackageTestCase();
				workPackageTestCase.setTestCase(testCase);
				workPackageTestCase.setWorkPackage(workPackage);
				workPackageTestCase.setCreatedDate(new Date(System.currentTimeMillis()));
				workPackageTestCase.setEditedDate(new Date(System.currentTimeMillis()));
				workPackageTestCase.setIsSelected(1);
				workPackageTestCase.setStatus("ACTIVE");
				if(workPackageTestSuite.getWorkPackage().getWorkFlowEvent() != null && workPackageTestSuite.getWorkPackage().getWorkFlowEvent().getWorkFlow() != null &&
						workPackageTestSuite.getWorkPackage().getWorkFlowEvent().getWorkFlow().getStageId() != null &&
						workPackageTestSuite.getWorkPackage().getWorkFlowEvent().getWorkFlow().getStageValue() != null && workPackageTestCase.getWorkPackage().getWorkFlowEvent().getWorkFlow().getStageId()!=IDPAConstants.WORKFLOW_STAGE_ID_PLANNING&& workPackageTestCase.getWorkPackage().getWorkFlowEvent().getWorkFlow().getStageValue()<20){
					WorkFlow workFlow=getWorkFlowByEntityIdStageId(IDPAConstants.WORKPACKAGE_ENTITY_ID, IDPAConstants.WORKFLOW_STAGE_ID_PLANNING);
					workFlowEvent = new WorkFlowEvent();
					workFlowEvent.setEventDate(DateUtility.getCurrentTime());
					workFlowEvent.setRemarks("Planning Workapckage testcase :"+workPackage.getName());
					UserList userAdmin= userListDAO.getUserByLoginId(IDPAConstants.ADMIN_LOGIN_ID);
					workFlowEvent.setUser(userAdmin);
					workFlowEvent.setWorkFlow(workFlow);
					workPackage.setWorkFlowEvent(workFlowEvent);
					addWorkFlowEvent(workFlowEvent);
					updateWorkPackage(workPackage);
				}
				workPackageTestCases.add(workPackageTestCase);
				mapWorkpackageWithTestCase(workPackageTestCase.getWorkPackage().getWorkPackageId(),testCase.getTestCaseId(),"Add");
			}
			
			
			log.debug("New testcases to be added for the workpackage : "+ workPackage.getWorkPackageId() + " : count : " + workPackageTestSuites.size());
			workPackagesTestCaseCount = addNewWorkPackageTestCases(workPackageTestCases);
			Set<WorkPackageTestCase> workPackageTestCaseSet=new HashSet<WorkPackageTestCase>();
			workPackageTestCaseSet.addAll(workPackageTestCases);
			workPackage.setWorkPackageTestCases(workPackageTestCaseSet);
			updateWorkPackage(workPackage);
			//workpackage testcase plan ends
			
			WorkpackageRunConfiguration wpRunConfiguration =null;
			
			Set<RunConfiguration> runConfigurations=new HashSet<RunConfiguration>();
			Set<RunConfiguration> runConfigurationsTotal=testRunPlan.getRunConfigurationList();
			Integer type= testRunPlan.getProductVersionListMaster().getProductMaster().getProductType().getProductTypeId();
			ArrayList dl= new ArrayList();

			if(deviceNames!=null && deviceNames.length()>0){
				String [] deviceName= deviceNames.split(",");
				for (String device : deviceName) {
					dl.add(device);
				}
				for (RunConfiguration runConfiguration : runConfigurationsTotal) {
					if(runConfiguration.getProductType() != null && runConfiguration.getProductType().getProductTypeId() != null)
						type = runConfiguration.getProductType().getProductTypeId();
					
					String runConfigType = "N/A";
					
					if(type==1 || type==5 ){
						runConfigType = "Device / Mobile";
						for(int j=0;j< dl.size();j++){							
							if(dl.get(j).toString().equals(runConfiguration.getRunconfigId().toString())){
								runConfigurations.add(runConfiguration);
							}
						}
					}else if(type==2 || type==3 || type==4){
						runConfigType = "Web / Embedded / Desktop";
						for(int j=0;j< dl.size();j++){
							if(dl.get(j).toString().equals(runConfiguration.getRunconfigId().toString()) || dl.get(j).toString().contains(runConfiguration.getHostList().getHostName())){
								runConfigurations.add(runConfiguration);
							}
						}
					}
					
					if(type == 6){
						runConfigType = "Composite";
						//If type is composite then copy both device and host.
						for(int j=0;j< dl.size();j++){
							if(dl.get(j).toString().equals(runConfiguration.getRunconfigId().toString()) || dl.get(j).toString().contains(runConfiguration.getHostList().getHostName())
									|| dl.get(j).toString().contains(runConfiguration.getGenericDevice().getName())){
								runConfigurations.add(runConfiguration);
							}
						}
					}
					log.info (" Test Configuration Type ID : "+type + " ; Name : " +runConfigType);
				}
			}else{
				runConfigurations.addAll(runConfigurationsTotal);
			}
			
			for (RunConfiguration runConfiguration : runConfigurations) {
				addRunConfigurationWorkpackage(workPackage.getWorkPackageId(),runConfiguration.getRunconfigId(),"testsuite");
				addRunConfigurationWorkpackage(workPackage.getWorkPackageId(),runConfiguration.getRunconfigId(),"feature");
				addRunConfigurationWorkpackage(workPackage.getWorkPackageId(),runConfiguration.getRunconfigId(),"testcase");
			}			
			
		if(testRunPlan.getExecutionType().getExecutionTypeId()==4 ){ //This is for manual workpackage execution
			
			//creating feature execution plan starts
			 WorkPackageFeatureExecutionPlan workPackageFeatureExecutionPlan =null;
             List<WorkPackageFeatureExecutionPlan> workPackageFeatureExecutionPlanListForUpdate= new ArrayList<WorkPackageFeatureExecutionPlan>();
            
			for (RunConfiguration runConfiguration : runConfigurations) {
				List<WorkPackageFeature> workPackageFeatureList=listWorkPackageFeature(workPackage.getWorkPackageId());
				Set<WorkPackageFeature> wpf = new HashSet<WorkPackageFeature>(workPackageFeatureList);
				wpRunConfiguration=getWorkpackageRunConfiguration(workPackage.getWorkPackageId(), runConfiguration.getRunconfigId(), "feature");

	        	ProductFeature feature=null;
	        	WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan =null;
	            List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlanListForUpdate= new ArrayList<WorkPackageTestCaseExecutionPlan>();
	            TestRunJob testRunJob = null;
	            for(WorkPackageFeature wpFeature:wpf){
                	if(wpFeature.getIsSelected()==1){
                		feature=productFeatureDAO.getByProductFeatureId(wpFeature.getFeature().getProductFeatureId());
                		
                		 testRunJob=getTestRunJobByWP(workPackage, runConfiguration);
                		if(testRunJob!=null){
                			mapTestRunJobFeature(testRunJob.getTestRunJobId(), feature.getProductFeatureId(), "Add");
             			}else{
             				addTestRunJob(runConfiguration, null, workPackage, null);
             				testRunJob=getTestRunJobByWP(workPackage, runConfiguration);
             				mapTestRunJobFeature(testRunJob.getTestRunJobId(), feature.getProductFeatureId(), "Add");
             				if(testRunJob!=null){
								if(testRunJob.getTestRunJobId()!=null)
								mongoDBService.addTestRunJobToMongoDB(testRunJob.getTestRunJobId());
							}
             			}
    					jobIds = jobIds + testRunJob.getTestRunJobId() +",";
                		if(feature!=null){
	            			workPackageFeatureExecutionPlan=new WorkPackageFeatureExecutionPlan();
	     					workPackageFeatureExecutionPlan.setCreatedDate(DateUtility.getCurrentTime());
								workPackageFeatureExecutionPlan.setFeature(feature);
								workPackageFeatureExecutionPlan.setModifiedDate(DateUtility.getCurrentTime());
								workPackageFeatureExecutionPlan.setPlannedExecutionDate(workPackage.getPlannedStartDate());
								workPackageFeatureExecutionPlan.setRunConfiguration(wpRunConfiguration);
								workPackageFeatureExecutionPlan.setWorkPackage(workPackage);
								workPackageFeatureExecutionPlan.setStatus(1);
								workPackageFeatureExecutionPlan.setTestRunJob(testRunJob);
								ExecutionPriority executionPriority=null;
								if(wpFeature.getFeature().getExecutionPriority()!=null)
									executionPriority= getExecutionPriorityByName(CommonUtility.getExecutionPriority(wpFeature.getFeature().getExecutionPriority().getPriorityName()));
								else
									executionPriority= getExecutionPriorityByName(IDPAConstants.EXECUTION_PRIORITY_MEDIUM);
								workPackageFeatureExecutionPlan.setExecutionPriority(executionPriority);
								addWorkpackageFeatureExecutionPlan(workPackageFeatureExecutionPlan);


								Set<TestCaseList> featureTC=feature.getTestCaseList();
	                			for(TestCaseList tclist :featureTC){
	                				if (tclist != null){
	                					tclist = testCaseListDAO.getByTestCaseId(tclist.getTestCaseId());
	                					workPackageTestCaseExecutionPlan=workPackageService.addWorkpPackageTestCaseExecutionPlans(tclist, null, workPackage, wpRunConfiguration,feature,"Feature",testRunJob);
		                    			workPackageTestCaseExecutionPlanListForUpdate.add(workPackageTestCaseExecutionPlan);
	                				}
	                			}
	                			addWorkPackageTestcaseExecutionPlan(workPackageTestCaseExecutionPlanListForUpdate);
	                		}     
                		}
                	}
			}
			
			//creating testsuite execution plan ends
			 WorkPackageTestSuiteExecutionPlan workPackageTestSuiteExecutionPlan =null;
             List<WorkPackageTestSuiteExecutionPlan> workPackageTestSuiteExecutionPlanListForUpdate= new ArrayList<WorkPackageTestSuiteExecutionPlan>();
            
			for (RunConfiguration runConfiguration : runConfigurations) {

			wpRunConfiguration=getWorkpackageRunConfiguration(workPackage.getWorkPackageId(), runConfiguration.getRunconfigId(), "testsuite");
			List<WorkPackageTestSuite> workPackageTestSuiteList=listWorkPackageTestSuite(workPackage.getWorkPackageId());
			Set<WorkPackageTestSuite> wpts = new HashSet<WorkPackageTestSuite>(workPackageTestSuiteList);
        	
			TestSuiteList testSuite=null;
        	WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan =null;
            List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlanListForUpdate= new ArrayList<WorkPackageTestCaseExecutionPlan>();
            TestRunJob testRunJob =null;
        	 for(WorkPackageTestSuite wpTestSuite:wpts){
                	if(wpTestSuite.getIsSelected()==1){
                		testSuite=wpTestSuite.getTestSuite();
                		 testRunJob=getTestRunJobByWP(workPackage, runConfiguration);
                		if(testRunJob!=null){
                			mapTestRunJobTestSuite(testRunJob.getTestRunJobId(),testSuite.getTestSuiteId(), "Add");
             			}else{
             				addTestRunJob(runConfiguration, null, workPackage, null);
             				testRunJob=getTestRunJobByWP(workPackage, runConfiguration);
             				mapTestRunJobTestSuite(testRunJob.getTestRunJobId(),testSuite.getTestSuiteId(), "Add");
             				if(testRunJob!=null){
								if(testRunJob.getTestRunJobId()!=null)
								mongoDBService.addTestRunJobToMongoDB(testRunJob.getTestRunJobId());
							}
             			}
            			workPackageTestSuiteExecutionPlan=new WorkPackageTestSuiteExecutionPlan();
            			workPackageTestSuiteExecutionPlan.setCreatedDate(DateUtility.getCurrentTime());
            			workPackageTestSuiteExecutionPlan.setTestsuite(testSuite);
            			workPackageTestSuiteExecutionPlan.setModifiedDate(DateUtility.getCurrentTime());
            			workPackageTestSuiteExecutionPlan.setPlannedExecutionDate(workPackage.getPlannedStartDate());
							workPackageTestSuiteExecutionPlan.setRunConfiguration(wpRunConfiguration);
							workPackageTestSuiteExecutionPlan.setWorkPackage(workPackage);
							workPackageTestSuiteExecutionPlan.setStatus(1);
							workPackageTestSuiteExecutionPlan.setTestRunJob(testRunJob);
							ExecutionPriority executionPriority=null;
							if(wpTestSuite.getTestSuite().getExecutionPriority()!=null)
								executionPriority= getExecutionPriorityByName(CommonUtility.getExecutionPriority(wpTestSuite.getTestSuite().getExecutionPriority().getPriorityName()));
							else
								executionPriority= getExecutionPriorityByName(IDPAConstants.EXECUTION_PRIORITY_MEDIUM);
							workPackageTestSuiteExecutionPlan.setExecutionPriority(executionPriority);
							addWorkpackageTestSuiteExecutionPlan(workPackageTestSuiteExecutionPlan);
							
							Set<TestCaseList> testSuiteTC=testSuite.getTestCaseLists();
                			for(TestCaseList tcList :testSuiteTC){
                				if (tcList != null){
                					tcList = testCaseListDAO.getByTestCaseId(tcList.getTestCaseId());
                					workPackageTestCaseExecutionPlan=workPackageService.addWorkpPackageTestCaseExecutionPlans(tcList, testSuite, workPackage, wpRunConfiguration,null,"TestSuite",testRunJob);
	                    			workPackageTestCaseExecutionPlanListForUpdate.add(workPackageTestCaseExecutionPlan);
                				}
                			}
                			addWorkPackageTestcaseExecutionPlan(workPackageTestCaseExecutionPlanListForUpdate);
                		}     
                   }
        	 jobIds = jobIds + testRunJob.getTestRunJobId() +",";
			}
			//creating testsuite execution plan ends
			List<WorkPackageTestCase> workPackageTestCaseList=listWorkPackageTestCases(workPackage.getWorkPackageId());
			Set<WorkPackageTestCase> workPackageTestCasesSet = new HashSet<WorkPackageTestCase>(workPackageTestCaseList);
			
			 for(RunConfiguration rc:runConfigurations){
             	wpRunConfiguration=getWorkpackageRunConfiguration(workPackage.getWorkPackageId(), rc.getRunconfigId(), "testcase");

				TestCaseList testcase=null;
             	WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan =null;
                List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlanListForUpdate= new ArrayList<WorkPackageTestCaseExecutionPlan>();
                TestRunJob testRunJob =null;
					for(WorkPackageTestCase workPackageTC:workPackageTestCasesSet){
						if(workPackageTC.getIsSelected()==1){
							testcase=testCaseListDAO.getByTestCaseId(workPackageTC.getTestCase().getTestCaseId());
							 testRunJob=getTestRunJobByWP(workPackage, rc);
	                 		if(testRunJob!=null){
	                 			mapTestRunJobTestCase(testRunJob.getTestRunJobId(), testcase.getTestCaseId(), "Add");
	              			}else{
	              				addTestRunJob(rc, null, workPackage, null);
	              				testRunJob=getTestRunJobByWP(workPackage, rc);
	              				mapTestRunJobTestCase(testRunJob.getTestRunJobId(), testcase.getTestCaseId(), "Add");
	              				if(testRunJob!=null){
									if(testRunJob.getTestRunJobId()!=null)
									mongoDBService.addTestRunJobToMongoDB(testRunJob.getTestRunJobId());
								}
	              			}
	    					
							workPackageTestCaseExecutionPlan = new WorkPackageTestCaseExecutionPlan();
							workPackageTestCaseExecutionPlan.setTestCase(testcase);
							workPackageTestCaseExecutionPlan.setWorkPackage(workPackage);
							workPackageTestCaseExecutionPlan.setRunConfiguration(wpRunConfiguration);
							workPackageTestCaseExecutionPlan.setExecutionStatus(3);
							workPackageTestCaseExecutionPlan.setIsExecuted(0);
							workPackageTestCaseExecutionPlan.setSourceType("TestCase");
							workPackageTestCaseExecutionPlan.setStatus(1);
							workPackageTestCaseExecutionPlan.setTestRunJob(testRunJob);
							ExecutionPriority executionPriority=null;
							if(testcase.getTestCasePriority()!=null)
								executionPriority= getExecutionPriorityByName(CommonUtility.getExecutionPriority(testcase.getTestCasePriority().getPriorityName()));
							else
								executionPriority= getExecutionPriorityByName(IDPAConstants.EXECUTION_PRIORITY_HIGH);
							
							workPackageTestCaseExecutionPlan.setExecutionPriority(executionPriority);
							
							TestCaseExecutionResult testCaseExecutionResult = new TestCaseExecutionResult();
							testCaseExecutionResult.setResult("");
							testCaseExecutionResult.setComments("");
							testCaseExecutionResult.setDefectsCount(0);
							testCaseExecutionResult.setDefectIds("");
							testCaseExecutionResult.setIsApproved(0);
							testCaseExecutionResult.setIsReviewed(0);
							testCaseExecutionResult.setObservedOutput("");
							
							testCaseExecutionResult.setWorkPackageTestCaseExecutionPlan(workPackageTestCaseExecutionPlan);
							workPackageTestCaseExecutionPlan.setTestCaseExecutionResult(testCaseExecutionResult);
							workPackageTestCaseExecutionPlanListForUpdate.add(workPackageTestCaseExecutionPlan);
						}
						
					}
					jobIds = jobIds + testRunJob.getTestRunJobId() +",";
					addWorkPackageTestcaseExecutionPlan(workPackageTestCaseExecutionPlanListForUpdate);
				}

			//creating testcase execution plan ends
			
				
			}
		int jobsCount  = 0;
		if(jobIds != null && !jobIds.isEmpty()){
			String[] arr = jobIds.split(",");
			jobsCount = arr.length;
		}
		ScriptLessExecutionDTO scriptExeDTO = new ScriptLessExecutionDTO(workPackage.getWorkPackageId(),workPackage.getName(),jobIds, jobsCount);
		if(testRunPlan.getExecutionType().getExecutionTypeId()==3){
			toolIntegrationController = new ToolIntegrationController();
			TestManagementSystem testManagementSystem=testManagementSystemDAO.getPrimaryTMSByProductId(workPackage.getProductBuild().getProductVersion().getProductMaster().getProductId());
			if(req!=null){
				toolIntegrationController.checkOutScriptsRest(workPackage.getProductBuild().getProductVersion().getProductMaster(), testRunPlan, req,workPackage,testManagementSystem);
			}
			executeTestRunPlan(testRunPlan,workPackage,runConfigurations,null);
		}
		
	}
	@Override
	@Transactional
	public void workpackageExxecutionPlan(WorkPackage workPackage,TestRunPlan testRunPlan,HttpServletRequest req){
		Integer workPackageId=workPackage.getWorkPackageId();
		
		//workpackage TestSuite plan starts
			WorkFlowEvent workFlowEvent=null;
	 		int workPackagesTestSuiteCount =0;
	 		String jobIds="";
			Set<TestSuiteList> testSuiteLists= testRunPlan.getTestSuiteLists();
			List<WorkPackageTestSuite> workPackageTestSuites = new ArrayList<WorkPackageTestSuite>();
			WorkPackageTestSuite workPackageTestSuite = null;
			for (TestSuiteList testSuite : testSuiteLists) {
				workPackageTestSuite = new WorkPackageTestSuite();
				workPackageTestSuite.setTestSuite(testSuite);
				workPackageTestSuite.setWorkPackage(workPackage);
				workPackageTestSuite.setCreatedDate(new Date(System.currentTimeMillis()));
				workPackageTestSuite.setEditedDate(new Date(System.currentTimeMillis()));
				workPackageTestSuite.setIsSelected(1);
				workPackageTestSuite.setStatus("ACTIVE");
				if(workPackageTestSuite.getWorkPackage().getWorkFlowEvent().getWorkFlow().getStageId()!=IDPAConstants.WORKFLOW_STAGE_ID_PLANNING&& workPackageTestSuite.getWorkPackage().getWorkFlowEvent().getWorkFlow().getStageValue()<20){
					WorkFlow workFlow=getWorkFlowByEntityIdStageId(IDPAConstants.WORKPACKAGE_ENTITY_ID, IDPAConstants.WORKFLOW_STAGE_ID_PLANNING);
					workFlowEvent = new WorkFlowEvent();
					workFlowEvent.setEventDate(DateUtility.getCurrentTime());
					workFlowEvent.setRemarks("Planning Workapckage TestSuite:"+workPackage.getName());
					UserList userAdmin= userListDAO.getUserByLoginId(IDPAConstants.ADMIN_LOGIN_ID);
					workFlowEvent.setUser(userAdmin);
					workFlowEvent.setWorkFlow(workFlow);
					workPackage.setWorkFlowEvent(workFlowEvent);
					addWorkFlowEvent(workFlowEvent);
					updateWorkPackage(workPackage);
				}
				workPackageTestSuites.add(workPackageTestSuite);
				mapWorkpackageWithTestSuite(workPackageTestSuite.getWorkPackage().getWorkPackageId(),testSuite.getTestSuiteId(),"Add");
			}
			
			
			workPackagesTestSuiteCount = addNewWorkPackageTestSuite(workPackageTestSuites);
			Set<WorkPackageTestSuite> workPackageTS=new HashSet<WorkPackageTestSuite>();
			workPackageTS.addAll(workPackageTestSuites);
			workPackage.setWorkPackageTestSuites(workPackageTS);
			updateWorkPackage(workPackage);
			//workpackage TestSuite plan end
			
			//workpackage feature plan starts
			int workPackagesFeatureCount =0;
			Set<ProductFeature> featureList= testRunPlan.getFeatureList();
			List<WorkPackageFeature> workPackageFeatures = new ArrayList<WorkPackageFeature>();
			WorkPackageFeature workPackageFeature = null;
			for (ProductFeature feature : featureList) {
				workPackageFeature = new WorkPackageFeature();
				workPackageFeature.setFeature(feature);
				workPackageFeature.setWorkPackage(workPackage);
				workPackageFeature.setCreatedDate(new Date(System.currentTimeMillis()));
				workPackageFeature.setEditedDate(new Date(System.currentTimeMillis()));
				workPackageFeature.setIsSelected(1);
				workPackageFeature.setStatus("ACTIVE");
				if(workPackageFeature.getWorkPackage().getWorkFlowEvent().getWorkFlow().getStageId()!=IDPAConstants.WORKFLOW_STAGE_ID_PLANNING&& workPackageFeature.getWorkPackage().getWorkFlowEvent().getWorkFlow().getStageValue()<20){
					WorkFlow workFlow=getWorkFlowByEntityIdStageId(IDPAConstants.WORKPACKAGE_ENTITY_ID, IDPAConstants.WORKFLOW_STAGE_ID_PLANNING);
					workFlowEvent = new WorkFlowEvent();
					workFlowEvent.setEventDate(DateUtility.getCurrentTime());
					workFlowEvent.setRemarks("Planning Workapckage Feature :"+workPackage.getName());
					UserList userAdmin= userListDAO.getUserByLoginId(IDPAConstants.ADMIN_LOGIN_ID);
					workFlowEvent.setUser(userAdmin);
					workFlowEvent.setWorkFlow(workFlow);
					workPackage.setWorkFlowEvent(workFlowEvent);
					addWorkFlowEvent(workFlowEvent);
					updateWorkPackage(workPackage);
				}
				workPackageFeatures.add(workPackageFeature);
				mapWorkpackageWithFeature(workPackageFeature.getWorkPackage().getWorkPackageId(),feature.getProductFeatureId(),"Add");
			}
			
			
			log.debug("New testcases to be added for the workpackage : "+ workPackage.getWorkPackageId() + " : count : " + workPackageTestSuites.size());
			workPackagesFeatureCount = addNewWorkPackageFeature(workPackageFeatures);
			Set<WorkPackageFeature> workPackageFeatureSet=new HashSet<WorkPackageFeature>();
			workPackageFeatureSet.addAll(workPackageFeatures);
			workPackage.setWorkPackageFeature(workPackageFeatureSet);
			updateWorkPackage(workPackage);
			//workpackage feature plan ends
			
			//workpackage testcase plan starts
			int workPackagesTestCaseCount =0;
			Set<TestCaseList> testCaseList= testRunPlan.getTestCaseList();
			List<WorkPackageTestCase> workPackageTestCases = new ArrayList<WorkPackageTestCase>();
			WorkPackageTestCase workPackageTestCase = null;
			for (TestCaseList testCase : testCaseList) {
				workPackageTestCase = new WorkPackageTestCase();
				workPackageTestCase.setTestCase(testCase);
				workPackageTestCase.setWorkPackage(workPackage);
				workPackageTestCase.setCreatedDate(new Date(System.currentTimeMillis()));
				workPackageTestCase.setEditedDate(new Date(System.currentTimeMillis()));
				workPackageTestCase.setIsSelected(1);
				workPackageTestCase.setStatus("ACTIVE");
				if(workPackageTestCase.getWorkPackage().getWorkFlowEvent().getWorkFlow().getStageId()!=IDPAConstants.WORKFLOW_STAGE_ID_PLANNING&& workPackageTestCase.getWorkPackage().getWorkFlowEvent().getWorkFlow().getStageValue()<20){
					WorkFlow workFlow=getWorkFlowByEntityIdStageId(IDPAConstants.WORKPACKAGE_ENTITY_ID, IDPAConstants.WORKFLOW_STAGE_ID_PLANNING);
					workFlowEvent = new WorkFlowEvent();
					workFlowEvent.setEventDate(DateUtility.getCurrentTime());
					workFlowEvent.setRemarks("Planning Workapckage testcase :"+workPackage.getName());
					UserList userAdmin= userListDAO.getUserByLoginId(IDPAConstants.ADMIN_LOGIN_ID);
					workFlowEvent.setUser(userAdmin);
					workFlowEvent.setWorkFlow(workFlow);
					workPackage.setWorkFlowEvent(workFlowEvent);
					addWorkFlowEvent(workFlowEvent);
					updateWorkPackage(workPackage);
				}
				workPackageTestCases.add(workPackageTestCase);
				mapWorkpackageWithTestCase(workPackageTestCase.getWorkPackage().getWorkPackageId(),testCase.getTestCaseId(),"Add");
			}
			
			
			log.debug("New testcases to be added for the workpackage : "+ workPackage.getWorkPackageId() + " : count : " + workPackageTestSuites.size());
			workPackagesTestCaseCount = addNewWorkPackageTestCases(workPackageTestCases);
			Set<WorkPackageTestCase> workPackageTestCaseSet=new HashSet<WorkPackageTestCase>();
			workPackageTestCaseSet.addAll(workPackageTestCases);
			workPackage.setWorkPackageTestCases(workPackageTestCaseSet);
			updateWorkPackage(workPackage);
			//workpackage testcase plan ends		
			
			WorkpackageRunConfiguration wpRunConfiguration =null;
			Set<RunConfiguration> runConfigurations=testRunPlan.getRunConfigurationList();
			for (RunConfiguration runConfiguration : runConfigurations) {
				addRunConfigurationWorkpackage(workPackage.getWorkPackageId(),runConfiguration.getRunconfigId(),"testsuite");
				addRunConfigurationWorkpackage(workPackage.getWorkPackageId(),runConfiguration.getRunconfigId(),"feature");
				addRunConfigurationWorkpackage(workPackage.getWorkPackageId(),runConfiguration.getRunconfigId(),"testcase");
			}			
			
		if(testRunPlan.getExecutionType().getExecutionTypeId()== IDPAConstants.EXECUTION_TYPE_MANUAL_CODE){
			
			//creating feature execution plan starts
			 WorkPackageFeatureExecutionPlan workPackageFeatureExecutionPlan =null;
             List<WorkPackageFeatureExecutionPlan> workPackageFeatureExecutionPlanListForUpdate= new ArrayList<WorkPackageFeatureExecutionPlan>();
            
			for (RunConfiguration runConfiguration : runConfigurations) {
				List<WorkPackageFeature> workPackageFeatureList=listWorkPackageFeature(workPackage.getWorkPackageId());
				Set<WorkPackageFeature> wpf = new HashSet<WorkPackageFeature>(workPackageFeatureList);
				wpRunConfiguration=getWorkpackageRunConfiguration(workPackage.getWorkPackageId(), runConfiguration.getRunconfigId(), "feature");

	        	ProductFeature feature=null;
	        	WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan =null;
	            List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlanListForUpdate= new ArrayList<WorkPackageTestCaseExecutionPlan>();
	            
	            for(WorkPackageFeature wpFeature:wpf){
                	if(wpFeature.getIsSelected()==1){
                		feature=productFeatureDAO.getByProductFeatureId(wpFeature.getFeature().getProductFeatureId());
                		
                		TestRunJob testRunJob=getTestRunJobByWP(workPackage, runConfiguration);
                		if(testRunJob!=null){
                			mapTestRunJobFeature(testRunJob.getTestRunJobId(), feature.getProductFeatureId(), "Add");
             			}else{
             				addTestRunJob(runConfiguration, null, workPackage, null);
             				testRunJob=getTestRunJobByWP(workPackage, runConfiguration);
             				mapTestRunJobFeature(testRunJob.getTestRunJobId(), feature.getProductFeatureId(), "Add");
             				if(testRunJob!=null){
								if(testRunJob.getTestRunJobId()!=null)
								mongoDBService.addTestRunJobToMongoDB(testRunJob.getTestRunJobId());
							}
             			}
                		jobIds = jobIds +testRunJob.getTestRunJobId() +",";
                		if(feature!=null){
	            			workPackageFeatureExecutionPlan=new WorkPackageFeatureExecutionPlan();
	     					workPackageFeatureExecutionPlan.setCreatedDate(DateUtility.getCurrentTime());
							workPackageFeatureExecutionPlan.setFeature(feature);
							workPackageFeatureExecutionPlan.setModifiedDate(DateUtility.getCurrentTime());
							workPackageFeatureExecutionPlan.setPlannedExecutionDate(workPackage.getPlannedStartDate());
							workPackageFeatureExecutionPlan.setRunConfiguration(wpRunConfiguration);
							workPackageFeatureExecutionPlan.setWorkPackage(workPackage);
							workPackageFeatureExecutionPlan.setStatus(1);
							workPackageFeatureExecutionPlan.setTestRunJob(testRunJob);
							ExecutionPriority executionPriority=null;
							if(wpFeature.getFeature().getExecutionPriority()!=null)
								executionPriority= getExecutionPriorityByName(CommonUtility.getExecutionPriority(wpFeature.getFeature().getExecutionPriority().getPriorityName()));
							else
								executionPriority= getExecutionPriorityByName(IDPAConstants.EXECUTION_PRIORITY_MEDIUM);
							workPackageFeatureExecutionPlan.setExecutionPriority(executionPriority);
							addWorkpackageFeatureExecutionPlan(workPackageFeatureExecutionPlan);							
							Set<TestCaseList> featureTC=feature.getTestCaseList();
                			for(TestCaseList tclist :featureTC){
                				if (tclist != null){
                					tclist = testCaseListDAO.getByTestCaseId(tclist.getTestCaseId());
                					workPackageTestCaseExecutionPlan=workPackageService.addWorkpPackageTestCaseExecutionPlans(tclist, null, workPackage, wpRunConfiguration,feature,"Feature",testRunJob);
	                    			workPackageTestCaseExecutionPlanListForUpdate.add(workPackageTestCaseExecutionPlan);
                				}
                			}
                			addWorkPackageTestcaseExecutionPlan(workPackageTestCaseExecutionPlanListForUpdate);
                		}     
                	}
                }
			}
			//creating feature execution plan ends
			
			//creating testsuite execution plan ends
			WorkPackageTestSuiteExecutionPlan workPackageTestSuiteExecutionPlan =null;
            List<WorkPackageTestSuiteExecutionPlan> workPackageTestSuiteExecutionPlanListForUpdate= new ArrayList<WorkPackageTestSuiteExecutionPlan>();            
			for (RunConfiguration runConfiguration : runConfigurations) {
				wpRunConfiguration=getWorkpackageRunConfiguration(workPackage.getWorkPackageId(), runConfiguration.getRunconfigId(), "testsuite");
				List<WorkPackageTestSuite> workPackageTestSuiteList=listWorkPackageTestSuite(workPackage.getWorkPackageId());
				Set<WorkPackageTestSuite> wpts = new HashSet<WorkPackageTestSuite>(workPackageTestSuiteList);
	        	
				TestSuiteList testSuite=null;
	        	WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan =null;
	            List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlanListForUpdate= new ArrayList<WorkPackageTestCaseExecutionPlan>();
            
	            for(WorkPackageTestSuite wpTestSuite:wpts){
                	if(wpTestSuite.getIsSelected()==1){
                		testSuite=wpTestSuite.getTestSuite();
                		TestRunJob testRunJob=getTestRunJobByWP(workPackage, runConfiguration);
                		if(testRunJob!=null){
                			mapTestRunJobTestSuite(testRunJob.getTestRunJobId(),testSuite.getTestSuiteId(), "Add");
             			}else{
             				addTestRunJob(runConfiguration, null, workPackage, null);
             				testRunJob=getTestRunJobByWP(workPackage, runConfiguration);
             				mapTestRunJobTestSuite(testRunJob.getTestRunJobId(),testSuite.getTestSuiteId(), "Add");
             				if(testRunJob!=null){
								if(testRunJob.getTestRunJobId()!=null)
								mongoDBService.addTestRunJobToMongoDB(testRunJob.getTestRunJobId());
							}
             			}
                		jobIds = jobIds +testRunJob.getTestRunJobId() +",";
            			workPackageTestSuiteExecutionPlan=new WorkPackageTestSuiteExecutionPlan();
            			workPackageTestSuiteExecutionPlan.setCreatedDate(DateUtility.getCurrentTime());
            			workPackageTestSuiteExecutionPlan.setTestsuite(testSuite);
            			workPackageTestSuiteExecutionPlan.setModifiedDate(DateUtility.getCurrentTime());
            			workPackageTestSuiteExecutionPlan.setPlannedExecutionDate(workPackage.getPlannedStartDate());
							workPackageTestSuiteExecutionPlan.setRunConfiguration(wpRunConfiguration);
							workPackageTestSuiteExecutionPlan.setWorkPackage(workPackage);
							workPackageTestSuiteExecutionPlan.setStatus(1);
							workPackageTestSuiteExecutionPlan.setTestRunJob(testRunJob);
							ExecutionPriority executionPriority=null;
							if(wpTestSuite.getTestSuite().getExecutionPriority()!=null)
								executionPriority= getExecutionPriorityByName(CommonUtility.getExecutionPriority(wpTestSuite.getTestSuite().getExecutionPriority().getPriorityName()));
							else
								executionPriority= getExecutionPriorityByName(IDPAConstants.EXECUTION_PRIORITY_MEDIUM);
							workPackageTestSuiteExecutionPlan.setExecutionPriority(executionPriority);
							addWorkpackageTestSuiteExecutionPlan(workPackageTestSuiteExecutionPlan);
							
							Set<TestCaseList> testSuiteTC=testSuite.getTestCaseLists();
                			for(TestCaseList tcList :testSuiteTC){
                				if (tcList != null){
                					tcList = testCaseListDAO.getByTestCaseId(tcList.getTestCaseId());
                					workPackageTestCaseExecutionPlan=workPackageService.addWorkpPackageTestCaseExecutionPlans(tcList, testSuite, workPackage, wpRunConfiguration,null,"TestSuite",testRunJob);
	                    			workPackageTestCaseExecutionPlanListForUpdate.add(workPackageTestCaseExecutionPlan);
                				}
                			}
                			addWorkPackageTestcaseExecutionPlan(workPackageTestCaseExecutionPlanListForUpdate);
                	}     
	            }
			}
			
			//creating testcase execution plan ends
			
			List<WorkPackageTestCase> workPackageTestCaseList=listWorkPackageTestCases(workPackage.getWorkPackageId());
			Set<WorkPackageTestCase> workPackageTestCasesSet = new HashSet<WorkPackageTestCase>(workPackageTestCaseList);
			
			 for(RunConfiguration rc:runConfigurations){
             	wpRunConfiguration=getWorkpackageRunConfiguration(workPackage.getWorkPackageId(), rc.getRunconfigId(), "testcase");

				TestCaseList testcase=null;
             	WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan =null;
                List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlanListForUpdate= new ArrayList<WorkPackageTestCaseExecutionPlan>();
					
					for(WorkPackageTestCase workPackageTC:workPackageTestCasesSet){
						if(workPackageTC.getIsSelected()==1){
							testcase=testCaseListDAO.getByTestCaseId(workPackageTC.getTestCase().getTestCaseId());
							TestRunJob testRunJob=getTestRunJobByWP(workPackage, rc);
	                 		if(testRunJob!=null){
	                 			mapTestRunJobTestCase(testRunJob.getTestRunJobId(), testcase.getTestCaseId(), "Add");
	              			}else{
	              				addTestRunJob(rc, null, workPackage, null);
	              				testRunJob=getTestRunJobByWP(workPackage, rc);
	              				mapTestRunJobTestCase(testRunJob.getTestRunJobId(), testcase.getTestCaseId(), "Add");
	              				if(testRunJob!=null){
									if(testRunJob.getTestRunJobId()!=null)
									mongoDBService.addTestRunJobToMongoDB(testRunJob.getTestRunJobId());
								}
	              			}
	                 		jobIds = jobIds +testRunJob.getTestRunJobId() +",";
							workPackageTestCaseExecutionPlan = new WorkPackageTestCaseExecutionPlan();
							workPackageTestCaseExecutionPlan.setTestCase(testcase);
							workPackageTestCaseExecutionPlan.setWorkPackage(workPackage);
							workPackageTestCaseExecutionPlan.setRunConfiguration(wpRunConfiguration);
							
							workPackageTestCaseExecutionPlan.setExecutionStatus(3);
							workPackageTestCaseExecutionPlan.setIsExecuted(0);
							workPackageTestCaseExecutionPlan.setSourceType("TestCase");
							workPackageTestCaseExecutionPlan.setStatus(1);
							workPackageTestCaseExecutionPlan.setTestRunJob(testRunJob);
							ExecutionPriority executionPriority=null;
							if(testcase.getTestCasePriority()!=null)
								executionPriority= getExecutionPriorityByName(CommonUtility.getExecutionPriority(testcase.getTestCasePriority().getPriorityName()));
							else
								executionPriority= getExecutionPriorityByName(IDPAConstants.EXECUTION_PRIORITY_HIGH);
							
							workPackageTestCaseExecutionPlan.setExecutionPriority(executionPriority);
							
							TestCaseExecutionResult testCaseExecutionResult = new TestCaseExecutionResult();
							testCaseExecutionResult.setResult("");
							testCaseExecutionResult.setComments("");
							testCaseExecutionResult.setDefectsCount(0);
							testCaseExecutionResult.setDefectIds("");
							testCaseExecutionResult.setIsApproved(0);
							testCaseExecutionResult.setIsReviewed(0);
							testCaseExecutionResult.setObservedOutput("");
							
							testCaseExecutionResult.setWorkPackageTestCaseExecutionPlan(workPackageTestCaseExecutionPlan);
							workPackageTestCaseExecutionPlan.setTestCaseExecutionResult(testCaseExecutionResult);
							workPackageTestCaseExecutionPlanListForUpdate.add(workPackageTestCaseExecutionPlan);
						}
						
					}
					addWorkPackageTestcaseExecutionPlan(workPackageTestCaseExecutionPlanListForUpdate);
				}

			//creating manual testcase execution plan ends			
		}
		int jobsCount  = 0;
		if(jobIds != null && !jobIds.isEmpty()){
			String[] arr = jobIds.split(",");
			jobsCount = arr.length;
		}
		ScriptLessExecutionDTO scriptLesExeDTO= new ScriptLessExecutionDTO(workPackage.getWorkPackageId(),workPackage.getName(),jobIds, jobsCount);
		if(testRunPlan.getExecutionType().getExecutionTypeId() == IDPAConstants.EXECUTION_TYPE_AUTO_CODE) {//This is for automated test workpackage execution
			toolIntegrationController = new ToolIntegrationController();
			TestManagementSystem testManagementSystem=testManagementSystemDAO.getPrimaryTMSByProductId(workPackage.getProductBuild().getProductVersion().getProductMaster().getProductId());
			if(req!=null){
				toolIntegrationController.checkOutScriptsRest(workPackage.getProductBuild().getProductVersion().getProductMaster(), testRunPlan, req,workPackage,testManagementSystem);
			}
			executeTestRunPlan(testRunPlan,workPackage,runConfigurations,null);
		}
		
	}

	//This is for automated test Workpackage execution
	public TestRunPlanExecutionStatusVO executeTestRunPlan(TestRunPlan testRunPlan,WorkPackage workPackage,Set<RunConfiguration> runConfigurations,String testcaseNames) {		
		log.debug("Execute TestRunPlan Started");
		TestRunJob testRunJob=null;
		String jobIds ="";
		TestRunPlanExecutionStatusVO statusVO = new TestRunPlanExecutionStatusVO();		
		List<GenericDevices> targetDevices = new ArrayList<GenericDevices> ();
		List<EnvironmentCombination> environmentCombinations = new ArrayList<EnvironmentCombination> ();

		List<GenericDevices> testRunPlanDevices = new ArrayList<GenericDevices> ();
		List<HostList> testRunPlanHosts = new ArrayList<HostList> ();
		List<HostList> targetHosts = new ArrayList<HostList> ();
		Set<TestSuiteList> testRunPlanTestSuites = new HashSet<TestSuiteList> ();
		Set<TestSuiteList> runConfigTestSuites = new HashSet<TestSuiteList>();	
		Set<TestSuiteList> executionTestSuites = new HashSet<TestSuiteList>();
		String testToolName = "";
		try {
			//Get productType
			Integer productType= testRunPlan.getProductVersionListMaster().getProductMaster().getProductType().getProductTypeId();	
			testRunPlanTestSuites=testRunPlan.getTestSuiteLists();			
			GenericDevices genericDevice=null;
			HostList hostList=null;
			EnvironmentCombination environmentCombination=null;
			for (RunConfiguration runConfiguration : runConfigurations) {
				//Test Configuration check whether it is active then only create job.
				VerificationResult isActive = testConfigurationReadinessCheck(runConfiguration, testRunPlan, null);
				log.info("Is the Test Configuration Active : " +isActive.getIsReady());
				if(isActive.getIsReady().equalsIgnoreCase("NO")){
					log.info("Current Test Configuration is not Active , so skipping Test Job Creation.");
					continue;
				}
				productType = runConfiguration.getProductType().getProductTypeId();
				runConfigTestSuites = runConfiguration.getTestSuiteLists();
				if(runConfiguration.getGenericDevice()!=null){
					genericDevice = runConfiguration.getGenericDevice();
				}else{
					log.info("There are no standalone devices specified for the test run plan");
				}
														
				if(runConfiguration.getHostList()!=null) {
					hostList=runConfiguration.getHostList();
				} else {
					log.info("There are no standalone server specified for the test run plan");
				}
				
				if(runConfiguration.getEnvironmentcombination()!=null) {
					environmentCombination=runConfiguration.getEnvironmentcombination();
				} else {
					log.info("There are no environment combination specified for the test run plan");
				}
				
				if (targetHosts.contains(hostList)) {
					log.info("The Server was already processed in this test run plan : " + hostList.getHostIpAddress());						
				} else  {								
					targetHosts.add(hostList);								
				}
				
				if(productType != null && (productType == IDPAConstants.PRODUCT_TYPE_DEVICE || productType == IDPAConstants.PRODUCT_TYPE_MOBILE)){
					if(genericDevice != null){								
						targetDevices.add(genericDevice);
					}
				} else{
					genericDevice = null;
				}
				
				
				executionTestSuites = runConfigTestSuites;
			
				if(executionTestSuites == null || executionTestSuites.isEmpty()){
					log.info("There is no Test Suite mapped to the test plan : "+testRunPlan.getTestRunPlanId() +" / test configuration : "+runConfiguration.getRunconfigId());
					statusVO.hasExecutedSuccessfully = false;
					statusVO.executionMessage = "There is no Test Suite mapped to the test plan : "+testRunPlan.getTestRunPlanId() +" / test configuration : "+runConfiguration.getRunconfigId();
					statusVO.testRunJob = null;
					return statusVO;
				}
				
				if(productType != null && (productType == IDPAConstants.PRODUCT_TYPE_DEVICE || productType == IDPAConstants.PRODUCT_TYPE_MOBILE)){
					if(genericDevice !=null && environmentCombination!=null){
						log.info("Standalone devices specified for the test run configuration " );					
						
						TestSuiteList testSuiteList = executionTestSuites.stream().findFirst().get();
						testRunJob = createJobForDevice(testRunPlan, genericDevice,environmentCombination, workPackage,testSuiteList,runConfiguration,testcaseNames);
						
						if(testRunPlan.getTestScriptsLevel().equalsIgnoreCase(IDPAConstants.MULTIPLE_RUNCONFIG_LEVEL_SCRIPT_PACK))
							testRunJob = addTestConfigurationTestSuiteScriptsPackToJob(testRunJob, testRunPlan, runConfiguration, testSuiteList);
						else
							testRunJob = addTestSuiteScriptsPackToJob(testRunJob, testRunPlan, testSuiteList);
						if(testRunJob!=null){								
							if(testRunJob.getTestRunJobId()!=null)										
								mongoDBService.addTestRunJobToMongoDB(testRunJob.getTestRunJobId());
						}						
					}
					
					if(targetDevices == null || targetDevices.isEmpty()){	
						log.info("No devices specified for the Test Plan / Test Configuration");
						statusVO.hasExecutedSuccessfully = false;
						statusVO.executionMessage = "Device(s) have not been specified or active";
						return statusVO;
					} else {
						log.info("Total No of devices specified for the Test Run : " + targetDevices.size());
					}
				} else if(productType != null && (productType == IDPAConstants.PRODUCT_TYPE_WEB || productType == IDPAConstants.PRODUCT_TYPE_DESKTOP || productType == IDPAConstants.PRODUCT_TYPE_EMBEDDED)){
					if(hostList!=null && environmentCombination!=null){
						log.info("Host specified for the test configuration" );						
						// Starting implementation for TRP Level Multi TS Execution 
						if(testRunPlan.getMultipleTestSuites().equalsIgnoreCase(IDPAConstants.MULTIPLE_TESTSUITES_FOR_EXECUTION)){							
							TestToolMaster toolMaster = testRunPlan.getTestToolMaster();
							testToolName = testRunPlan.getTestToolMaster().getTestToolName();
								testRunJob = createJobForMultiTS(testRunPlan, hostList,genericDevice,environmentCombination, workPackage,executionTestSuites,runConfiguration,testcaseNames);								
							testRunJob = addTestScriptsPackToJob(testRunJob, testRunPlan);
							if(testRunJob != null){						 
								if(testRunJob.getTestRunJobId()!=null)										
									mongoDBService.addTestRunJobToMongoDB(testRunJob.getTestRunJobId());
							}							
						} else {
							TestSuiteList testSuiteList = executionTestSuites.stream().findFirst().get();
							testRunJob = createJobForHost(testRunPlan, hostList,environmentCombination, workPackage,testSuiteList,runConfiguration,testcaseNames);
							
							if(testRunPlan.getTestScriptsLevel().equalsIgnoreCase(IDPAConstants.MULTIPLE_RUNCONFIG_LEVEL_SCRIPT_PACK))
								testRunJob = addTestConfigurationTestSuiteScriptsPackToJob(testRunJob, testRunPlan, runConfiguration, testSuiteList);
							else
								testRunJob = addTestSuiteScriptsPackToJob(testRunJob, testRunPlan, testSuiteList);
							if(testRunJob!=null){								
								if(testRunJob.getTestRunJobId()!=null)										
									mongoDBService.addTestRunJobToMongoDB(testRunJob.getTestRunJobId());
							}							
						}						
					}
					
					if(targetHosts == null || targetHosts.isEmpty()){	
						log.info("No host specified for the Test Plan / Test Configuration");
						statusVO.hasExecutedSuccessfully = false;
						statusVO.executionMessage = "Host has not been specified or active";
						return statusVO;
					} else {
						log.info("Total No of Host specified for the Test Run : " + targetHosts.size());
					}	
				} else if(productType != null && productType == IDPAConstants.PRODUCT_TYPE_COMPOSITE){
					testRunJob = createJobForMultiTS(testRunPlan, hostList,genericDevice,environmentCombination, workPackage,executionTestSuites,runConfiguration,testcaseNames);
					if(testRunJob != null){	
						testRunJob = addTestScriptsPackToJob(testRunJob, testRunPlan);	
						if(testRunJob.getTestRunJobId()!=null)										
							mongoDBService.addTestRunJobToMongoDB(testRunJob.getTestRunJobId());
					}	
				}
				
				if(testRunJob != null && testRunJob.getTestRunJobId() != null){
					jobIds = jobIds +testRunJob.getTestRunJobId() +",";
					if(testRunJob.getTestRunJobId()!=null)										
						mongoDBService.addTestRunJobToMongoDB(testRunJob.getTestRunJobId());
				}
				
				//Set the Combined Results Job to the Workpackage
				if(testRunPlan.getCombinedResultsRunConfiguration() != null && testRunPlan.getCombinedResultsRunConfiguration().getRunconfigId() != null) {
					if (runConfiguration.getRunconfigId() == testRunPlan.getCombinedResultsRunConfiguration().getRunconfigId()) {
						workPackage.setCombinedResultsReportingJob(testRunJob);
					}
				}
			}
			//Add TestRunPlan job mode to workpackage
			if(testRunPlan.getResultsReportingMode() != null)
				workPackage.setResultsReportingMode(testRunPlan.getResultsReportingMode());
			log.info("Updating Workpackage after set combined results reporting job ");
			updateWorkPackage(workPackage);
			log.info("Workpackage updated");
			
			if(jobIds != null && !jobIds.isEmpty()){
				log.info("Execution Job Counts ==> "+jobIds.substring(0, jobIds.lastIndexOf(",")).split(",").length);
				log.info("Execution Job Numbers ==> "+jobIds.substring(0, jobIds.lastIndexOf(",")));
			} else {
				log.info("Execution Job Counts ==> 0");	
			}
			
			int jobsCount  = 0;
			if(jobIds != null && !jobIds.isEmpty()){
				String[] arr = jobIds.split(",");
				jobsCount = arr.length;
			}
			
			ScriptLessExecutionDTO scriptLesExeDTO= new ScriptLessExecutionDTO(workPackage.getWorkPackageId(),workPackage.getName(),jobIds, jobsCount);					
			statusVO.hasExecutedSuccessfully = true;
			statusVO.executionMessage = "Jobs created for active host";
			statusVO.testRunJob = testRunJob;				
		} catch (Exception e) {        	
            log.error("Problem executing Test Run ", e);            
			statusVO.hasExecutedSuccessfully = false;
			statusVO.executionMessage = "Problems in executing. Some jobs may not have been created";
			statusVO.testRunJob = testRunJob;
        }
		return statusVO;
	}

	private TestRunJob addTestScriptsPackToJob(TestRunJob testRunJob, TestRunPlan testRunPlan) {	
		if (testRunPlan.getMultipleTestSuites().equalsIgnoreCase("1")) {
			if (testRunPlan.getTestScriptsLevel().equalsIgnoreCase(IDPAConstants.MULTIPLE_TEST_RUN_PLAN_LEVEL_SCRIPT_PACK)) {
				if(testRunPlan.getTestSuiteScriptFileLocation() != null)
					testRunJob.setTestScriptFileLocation(testRunPlan.getTestSuiteScriptFileLocation());
				if(testRunPlan.getScriptTypeMaster() != null)
					testRunJob.setScriptTypeMaster(testRunPlan.getScriptTypeMaster());
				if(testRunPlan.getTestToolMaster() != null)
					testRunJob.setTestToolMaster(testRunPlan.getTestToolMaster());
			} else if (testRunPlan.getTestScriptsLevel().equalsIgnoreCase(IDPAConstants.MULTIPLE_RUNCONFIG_LEVEL_SCRIPT_PACK)) {			
				if(testRunJob.getRunConfiguration() != null){
					if(testRunJob.getRunConfiguration().getScriptTypeMaster() != null)
						testRunJob.setScriptTypeMaster(testRunJob.getRunConfiguration().getScriptTypeMaster());
					if(testRunJob.getRunConfiguration().getTestTool() != null)
						testRunJob.setTestToolMaster(testRunJob.getRunConfiguration().getTestTool());
				}				
			} else {
				if(testRunPlan.getTestToolMaster() != null)
					testRunJob.setTestToolMaster(testRunPlan.getTestToolMaster());
			}
		} else {
			for(TestSuiteList tsl : testRunPlan.getTestSuiteLists()){			
				if(tsl.getTestSuiteScriptFileLocation() != null)
					testRunJob.setTestScriptFileLocation(tsl.getTestSuiteScriptFileLocation());
				if(tsl.getScriptTypeMaster() != null)
					testRunJob.setScriptTypeMaster(tsl.getScriptTypeMaster());
				if(testRunPlan.getTestToolMaster() != null)
					testRunJob.setTestToolMaster(testRunPlan.getTestToolMaster());		
			}			
		}
		return testRunJob;
	}
	
	private TestRunJob addTestSuiteScriptsPackToJob(TestRunJob testRunJob, TestRunPlan testRunPlan, TestSuiteList tsl) {
		if(testRunPlan.getTestScriptsLevel() != null && testRunPlan.getTestScriptsLevel().equalsIgnoreCase(IDPAConstants.MULTIPLE_TEST_SUITE_LEVEL_SCRIPT_PACKS)){
			if(tsl.getTestSuiteScriptFileLocation() != null)
				testRunJob.setTestScriptFileLocation(tsl.getTestSuiteScriptFileLocation());
			if(tsl.getScriptTypeMaster() != null)
				testRunJob.setScriptTypeMaster(tsl.getScriptTypeMaster());
		} else {
			if(testRunPlan.getTestSuiteScriptFileLocation() != null)
				testRunJob.setTestScriptFileLocation(testRunPlan.getTestSuiteScriptFileLocation());
			if(testRunPlan.getScriptTypeMaster() != null)
				testRunJob.setScriptTypeMaster(testRunPlan.getScriptTypeMaster());
		}
		
		if(testRunPlan.getTestToolMaster() != null)
			testRunJob.setTestToolMaster(testRunPlan.getTestToolMaster());
		return testRunJob;
	}
	
	private TestRunJob addTestConfigurationTestSuiteScriptsPackToJob(TestRunJob testRunJob, TestRunPlan testRunPlan, RunConfiguration rc, TestSuiteList tsl) {
		
		if(rc.getTestScriptFileLocation() != null) {
			testRunJob.setScriptPathLocation(rc.getTestScriptFileLocation());
			testRunJob.setTestScriptFileLocation(rc.getTestScriptFileLocation());			
		}
		
		if(rc.getScriptTypeMaster() != null)
			testRunJob.setScriptTypeMaster(rc.getScriptTypeMaster());	
		if(rc.getTestTool() != null)
			testRunJob.setTestToolMaster(rc.getTestTool());
		return testRunJob;
	}

	public TestRunJob createJobForDevice(TestRunPlan testRunPlan, GenericDevices device,EnvironmentCombination environmentCombination,WorkPackage workPackage,TestSuiteList testSuiteList,RunConfiguration runConfiguration,String testcaseNames){

		try {
			
			//Get the device status
			Integer deviceStatus=device.getAvailableStatus();
			
			//Create a new TestRunJob entry for the device
			TestRunJob testRunJob = new TestRunJob();
			testRunJob.setWorkPackage(workPackage);
			testRunJob.setGenericDevices(device);
			testRunJob.setHostList(device.getHostList());
			testRunJob.setTestSuite(testSuiteList);
			testRunJob.setTestRunStatus(0);
			testRunJob.setTestRunPlan(testRunPlan);
			testRunJob.setEnvironmentCombination(environmentCombination);
			testRunJob.setTestRunTriggeredTime(DateUtility.getCurrentTime());
			testRunJob.setRunConfiguration(runConfiguration);
			if (device.getHostList().getCommonActiveStatusMaster().getStatus().equals("INACTIVE")) {
				testRunJob.setTestRunFailureMessage("Device Host Offline!");
				testRunJob.setTestRunStatus(IDPAConstants.JOB_ABORTED);
				
				addTestRunJob(testRunJob);				
 				mapTestRunJobTestSuite(testRunJob.getTestRunJobId(),testSuiteList.getTestSuiteId(), "Add");
 			    List<TestCaseList> testCaseLists=productMasterDAO.getTestSuiteTestCaseMapped(testRunPlan.getTestRunPlanId(), testSuiteList.getTestSuiteId());
 			    mapTestRunJobTestSuiteTestCase(testRunJob.getTestRunJobId(),testSuiteList.getTestSuiteId(),testCaseLists, "Add");
 		        log.info("Created Job No : " + testRunJob.getTestRunJobId() + " for device : " + device.getUDID() + ". Not posting to terminal as device host is not active");            
				
			//Irrespective of the device status, send the job to the Terminal for execution
			} else {						
				//queue the run and inform the client/terminal
				testRunJob.setTestRunStatus(IDPAConstants.JOB_QUEUED);
				
				addTestRunJob(testRunJob);	
 				mapTestRunJobTestSuite(testRunJob.getTestRunJobId(),testSuiteList.getTestSuiteId(), "Add");
 				
 				 if(workPackage != null ){//If Job is Queued, setting WP as Executing.
 					workPackageService.updateWPStatus(testRunJob,workPackage.getWorkPackageId(), IDPAConstants.WORKFLOW_STAGE_ID_EXECUTION, 0,false);
 				} 
 				 
 				if(testRunJob!=null){
					if(testRunJob.getTestRunJobId()!=null)
					mongoDBService.addTestRunJobToMongoDB(testRunJob.getTestRunJobId());
				}
 				List<TestCaseList> testCaseListTotal = null;
 				
 				testCaseListTotal=productMasterDAO.getRunConfigTestSuiteTestCaseMapped(runConfiguration.getRunconfigId(), testSuiteList.getTestSuiteId());
 				Set<TestCaseList> testCases=new LinkedHashSet<TestCaseList>();
 				Integer productId= testRunPlan.getProductVersionListMaster().getProductMaster().getProductId();
 				TestCaseList testCaseList=null;
 				if(testcaseNames!=null && testcaseNames.length()>0){
 					String [] testcaseName= testcaseNames.split(",");
					log.debug("Selective Testcases count  : " + testcaseName.length);
 					for (String tc : testcaseName) {
 						testCaseList=testCaseListDAO.getTestCaseByName(tc, productId);
 						testCases.add(testCaseList);
 						if (testCaseList == null) {
 							log.debug("Job : " + testRunJob.getTestRunJobId() + " Selective Testcase : " + tc + " : Not found");
 						} else {
 							log.debug("Job : " + testRunJob.getTestRunJobId() + "Selective Testcase Added : " + testCaseList.getTestCaseName());
 						}
 					}
 				} else {
 					testCases.addAll(testCaseListTotal);
					log.info("Job : " + testRunJob.getTestRunJobId() + " : No Selective Testcases specified. All added");
 				}
 				if (testSuiteList.getScriptTypeMaster().getScriptType().equals("GHERKIN") && (testSuiteList.getTestSuiteScriptFileLocation() == null || testSuiteList.getTestSuiteScriptFileLocation().trim().isEmpty())) {
 					Set<TestCaseList> totalTestCasesList= null;
 					if (testCases.size() == 0) {
 						totalTestCasesList = testSuiteList.getTestCaseLists();
 					} else {
 						totalTestCasesList = testCases;
 					}
 					testExecutionService.prepareBDDTestScriptsForJobExecution(testSuiteList, testRunJob.getTestRunJobId(), totalTestCasesList, testRunPlan);
					log.info("Job : " + testRunJob.getTestRunJobId() + " Prepared BDD stories pack for execution");
 				}else if ((testSuiteList.getScriptTypeMaster().getScriptType().equalsIgnoreCase("EDAT_PYTHON") || testSuiteList.getScriptTypeMaster().getScriptType().equalsIgnoreCase("EDAT_POWERSHELL")) && (testSuiteList.getTestSuiteScriptFileLocation() == null || testSuiteList.getTestSuiteScriptFileLocation().trim().isEmpty())) {
	 					Set<TestCaseList> totalTestCasesList= null;
	 					if (testCases.size() == 0) {
	 						totalTestCasesList = testSuiteList.getTestCaseLists();
	 					} else {
	 						totalTestCasesList = testCases;
	 					}
	 					testExecutionService.prepareBDDTestScriptsForJobExecution(testSuiteList, testRunJob.getTestRunJobId(), totalTestCasesList,testRunPlan);
						log.info("Job : " + testRunJob.getTestRunJobId() + " Prepared BDD stories pack for execution");
	 				}
 				ArrayList<TestCaseList> testCaseLists=new ArrayList<TestCaseList>();
 				testCaseLists.addAll(testCases);
 				
 			    mapTestRunJobTestSuiteTestCase(testRunJob.getTestRunJobId(),testSuiteList.getTestSuiteId(),testCaseLists, "Add");
 			    
				HostHeartbeat host = hostHeartbeatDAO.getByHostId(device.getHostList().getHostId());
				host.setHasResponse(true);
				host.setResponseToSend((short)ClientReponseMessage.PULSE_ACK_JOBS_AVAILABLE);
				hostHeartbeatDAO.update(host);
	            log.info("Created Job : " + testRunJob.getTestRunJobId() + " for device : " + device.getUDID() 
	            												   + " and queued for terminal : " + host.getHostId());            
			}
			return testRunJob;
		} catch (Exception e) {
            log.error("Test Run Plan : " + testRunPlan.getTestRunPlanName() + " : Problem in creating Job for device : " + device.getUDID());            
			return null;
		}
	
	}


	public TestRunJob createJobForHost(TestRunPlan testRunPlan, HostList host,EnvironmentCombination environmentCombination,WorkPackage workPackage,TestSuiteList testSuiteList,RunConfiguration runConfiguration,String testcaseNames){
		try {
			//Get the device status
			String hostStatus=host.getCommonActiveStatusMaster().getStatus();
			log.info("Host Status is : " + hostStatus);		
			//Create a new TestRunJob entry for the device
			TestRunJob testRunJob = new TestRunJob();
			testRunJob.setWorkPackage(workPackage);
			testRunJob.setHostList(host);
			testRunJob.setEnvironmentCombination(environmentCombination);		
			testRunJob.setTestSuite(testSuiteList);
						
			//testRunJob.setTestSuiteSet(testSuites); Multiple testSuite Changes
			testRunJob.setTestRunStatus(0);
			testRunJob.setTestRunPlan(testRunPlan);
			testRunJob.setRunConfiguration(runConfiguration);
			testRunJob.setTestRunTriggeredTime(DateUtility.getCurrentTime());
			//Added support for Multiple Test SuiteList support
			if(testRunPlan.getTestSuiteScriptFileLocation() != null && testRunPlan.getTestSuiteScriptFileLocation() != "")
				testRunJob.setScriptPathLocation(testRunPlan.getTestSuiteScriptFileLocation());
			
			if(hostStatus.equalsIgnoreCase("INACTIVE")){										
				log.info("Ignoring job - Host is not active or busy : " + host.getHostIpAddress());            
				//Device is not active. No need to publish the job. Set the reason for failure alone						
				testRunJob.setTestRunFailureMessage("Job could not be executed because the Host is offline!");				
				testRunJob.setTestRunStatus(IDPAConstants.JOB_ABORTED);
				addTestRunJob(testRunJob);
				mapTestRunJobTestSuite(testRunJob.getTestRunJobId(),testSuiteList.getTestSuiteId(), "Add");
				List<TestCaseList> testCaseLists=productMasterDAO.getTestSuiteTestCaseMapped(testRunPlan.getTestRunPlanId(), testSuiteList.getTestSuiteId());
				mapTestRunJobTestSuiteTestCase(testRunJob.getTestRunJobId(),testSuiteList.getTestSuiteId(),testCaseLists, "Add");
			} else if(hostStatus.equalsIgnoreCase("ACTIVE")) {						
				//queue the run and inform the client/terminal
				testRunJob.setTestRunStatus(4); //Not updating WP, as current job's status is completed, and could update WP, based on overall Jobs.
				
				addTestRunJob(testRunJob);		
				
				mapTestRunJobTestSuite(testRunJob.getTestRunJobId(),testSuiteList.getTestSuiteId(), "Add");
				
				
 				if(testRunJob!=null){
					if(testRunJob.getTestRunJobId()!=null)
					mongoDBService.addTestRunJobToMongoDB(testRunJob.getTestRunJobId());
				}
 				//For multiple Test Suite Support, change here to iterate TestSuite, 
 				List<TestCaseList> testCaseListTotal = new ArrayList<TestCaseList>();
 				
 				testCaseListTotal=productMasterDAO.getRunConfigTestSuiteTestCaseMapped(runConfiguration.getRunconfigId(), testSuiteList.getTestSuiteId());
 	 				//Set<TestCaseList> testCases=new LinkedHashSet<TestCaseList>();
 					//Set<TestCaseList> testCases=new LinkedHashSet<TestCaseList>();
 					ListOrderedSet testCases=new ListOrderedSet();
 	 				Integer productId= testRunPlan.getProductVersionListMaster().getProductMaster().getProductId();
 	 				TestCaseList testCaseList=null;
 	 				if(testcaseNames!=null && testcaseNames.length()>0){
 	 					String [] testcaseName= testcaseNames.split(",");
 	 					for (String tc : testcaseName) {
 	 						testCaseList=testCaseListDAO.getTestCaseByName(tc, productId);
 	 						testCases.add(testCaseList);
 	 					}
 	 				}else{
 	 					for(TestCaseList tcl : testCaseListTotal){
 	 						testCases.add(tcl);
 	 					}
 	 					//testCases.addAll(testCaseListTotal);
 						log.info("Job : " + testRunJob.getTestRunJobId() + " : No Selective Testcases specified. All added");
 	 				}
 	 				
 	 				ListOrderedSet totalTestCasesList=new ListOrderedSet();
 	 				if (testSuiteList.getScriptTypeMaster().getScriptType().equals("GHERKIN") && (testSuiteList.getTestSuiteScriptFileLocation() == null || testSuiteList.getTestSuiteScriptFileLocation().trim().isEmpty())) {
 	 					//Set<TestCaseList> totalTestCasesList= new LinkedHashSet<TestCaseList>();
 	 					if (testCases.size() == 0) {
 	 						for(TestCaseList tcl : testSuiteList.getTestCaseLists()){
 	 							totalTestCasesList.add(tcl);
 	 						}
 	 					} else { 	 						
 	 	 					totalTestCasesList = testCases;					
 	 					}
 	 					testExecutionService.prepareBDDTestScriptsForJobExecution(testSuiteList, testRunJob.getTestRunJobId(), totalTestCasesList,testRunPlan);
 						log.info("Job : " + testRunJob.getTestRunJobId() + " Prepared BDD stories pack for execution");
 	 				}else if ((testSuiteList.getScriptTypeMaster().getScriptType().equalsIgnoreCase("EDAT_PYTHON") || testSuiteList.getScriptTypeMaster().getScriptType().equalsIgnoreCase("EDAT_POWERSHELL")) && (testSuiteList.getTestSuiteScriptFileLocation() == null || testSuiteList.getTestSuiteScriptFileLocation().trim().isEmpty())) {
 	 					//Set<TestCaseList> totalTestCasesList= new LinkedHashSet<TestCaseList>();
 	 					if (testCases.size() == 0) {
 	 						for(TestCaseList tcl : testSuiteList.getTestCaseLists()){
 	 							totalTestCasesList.add(tcl);
 	 						}
 	 					} else { 	 						
 	 	 					totalTestCasesList = testCases;					
 	 					}
 	 					testExecutionService.prepareBDDTestScriptsForJobExecution(testSuiteList, testRunJob.getTestRunJobId(), totalTestCasesList,testRunPlan);
 						log.info("Job : " + testRunJob.getTestRunJobId() + " Prepared BDD stories pack for execution");
 	 				}
 	 				ArrayList<TestCaseList> testCaseLists=new ArrayList<TestCaseList>();
 	 				testCaseLists.addAll(testCases);
 	 				
 	 			    mapTestRunJobTestSuiteTestCase(testRunJob.getTestRunJobId(),testSuiteList.getTestSuiteId(),testCaseLists, "Add");
 						
 			    
				HostHeartbeat hostHeart = hostHeartbeatDAO.getByHostId(host.getHostId());
				hostHeart.setHasResponse(true);
				hostHeart.setResponseToSend((short)ClientReponseMessage.PULSE_ACK_JOBS_AVAILABLE);
				hostHeartbeatDAO.update(hostHeart);
	            //log.info("Created Job for host : " + host.getHostName()+ " : " + host.getHostIpAddress());            
	            log.info("Created Job No : " + testRunJob.getTestRunJobId() + " for Host : " + host.getHostIpAddress() 
	            												   + " and queued to terminal : " + host.getHostId());            
			}
			return testRunJob;
		} catch (Exception e) {
            log.error("Test Run Plan : " + testRunPlan.getTestRunPlanName() + " : Problem in creating Job for Host : " + host.getHostIpAddress());            
			return null;
		}
	}
	
	@Override
	@Transactional
	public void addTestRunJob(TestRunJob testRunJob){
		log.debug("adding Test Run Job instance");
		try {	
			sessionFactory.getCurrentSession().saveOrUpdate(testRunJob);
			log.debug("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}	
	}

	@Override
	@Transactional
	public RunConfiguration getWorkpackageRCByEnvDev(Integer workpackageId,
			Integer environmentCombinationId, Integer deviceId) {
		List<WorkpackageRunConfiguration> wprunConfigurations = null;
		WorkpackageRunConfiguration wprConfiguration=null;
		RunConfiguration runConfiguration = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkpackageRunConfiguration.class, "wprc");
			c.createAlias("wprc.runconfiguration", "rc");
			c.createAlias("wprc.workpackage", "wp");
			c.createAlias("rc.genericDevice", "gd");
			c.createAlias("rc.environmentcombination", "ec");
			c.add(Restrictions.eq("ec.environment_combination_id",environmentCombinationId));
			c.add(Restrictions.eq("gd.genericsDevicesId",deviceId));
			c.add(Restrictions.eq("wp.workPackageId",workpackageId));
			wprunConfigurations=c.list();			
			wprConfiguration = (wprunConfigurations != null && wprunConfigurations.size() != 0) ? (WorkpackageRunConfiguration) wprunConfigurations
					.get(0) : null;

			if(wprConfiguration!=null){
					Hibernate.initialize(wprConfiguration.getRunconfiguration());
					runConfiguration=wprConfiguration.getRunconfiguration();
					Hibernate.initialize(runConfiguration.getEnvironmentcombination());
					Hibernate.initialize(runConfiguration.getGenericDevice());
					Hibernate.initialize(runConfiguration.getProduct());
			}
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return runConfiguration;	
	}

	@Override
	@Transactional
	public List<WorkPackage> listWorkPackagesByProductBuild(int productBuildId) {
		log.debug("listing all WorkPackage instance");
		List<WorkPackage> workPackageList = null;
		try {
			workPackageList = sessionFactory.getCurrentSession().createQuery("from WorkPackage wp where (wp.productBuild.productBuildId=:productBuildId) order by wp.createDate desc")
					.setParameter("productBuildId", productBuildId).list();			
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		for (WorkPackage workPackage : workPackageList) {
		Hibernate.initialize(workPackage.getWorkFlowEvent().getWorkFlow());	
		}
		return workPackageList;
	}
	
	@Override
	@Transactional
	public List<WorkPackage> listWorkPackageBytestrunplanId(Integer testRunPlanId) {
		List<WorkPackage> workPackageList = null;
		try {
			workPackageList = sessionFactory.getCurrentSession().createQuery("from WorkPackage wp where (wp.testRunPlan.testRunPlanId=:testRunPlanId)")
					.setParameter("testRunPlanId", testRunPlanId).list();			
			log.debug("list all successful");
		
			for(WorkPackage workPackage : workPackageList) {
				Hibernate.initialize(workPackage.getTestRunPlan());	
				Hibernate.initialize(workPackage.getTestRunPlan().getExecutionType());	
				Hibernate.initialize(workPackage.getProductBuild());
				Hibernate.initialize(workPackage.getProductBuild().getProductVersion());
				Hibernate.initialize(workPackage.getProductBuild().getProductVersion().getProductMaster());
				Hibernate.initialize(workPackage.getTestRunPlan().getRunConfigurationList());
				Hibernate.initialize(workPackage.getTestRunJobSet());
			
			}
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return workPackageList;
	}	
	
	@Override
	@Transactional
	public int getTotalRecordWPTCEP(Integer workPackageId,int status) {
		List<WorkPackageTestCaseExecutionPlan> wpExecutionPlans = null;
		int size=0;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackageTestCaseExecutionPlan.class, "wptcep");
			
		
			c.createAlias("wptcep.workPackage", "wp");
			c.add(Restrictions.eq("wp.workPackageId",workPackageId));
			if(status!=2)
			c.add(Restrictions.eq("wptcep.status",status));

			
			wpExecutionPlans=c.list();			

			if(wpExecutionPlans!=null && !wpExecutionPlans.isEmpty()){
				size=wpExecutionPlans.size();
			}
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return size;	
	}
	
	@Override
	@Transactional
	public int getTotalRecordWPTCEPCount(Integer workPackageId,int status) {
		int WPTCEPCount=0;
		String sql = "";
		try {
			if(status!=2){						
				 sql="select count(*) from workpackage_testcase_execution_plan wtep where wtep.workPackageId=:wpId and wtep.status=:stat";	
				 WPTCEPCount=((Number) sessionFactory.getCurrentSession().createSQLQuery(sql)
								.setParameter("wpId", workPackageId)
								.setParameter("stat", status)						
								.uniqueResult()).intValue();	
			}else{						
				 sql="select count(*) from workpackage_testcase_execution_plan wtep where wtep.workPackageId=:wpId";	
				 WPTCEPCount=((Number) sessionFactory.getCurrentSession().createSQLQuery(sql)
								.setParameter("wpId", workPackageId)					
								.uniqueResult()).intValue();
				 }		
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return WPTCEPCount;	
	}

	@Override
	@Transactional
	public Set<EnvironmentCombination> getEnvironmentCombinationMappedToWorkpackage(int workpackageId) {
		List<WorkpackageRunConfiguration> wprunConfigurations = null;
		Set<EnvironmentCombination> ec=new HashSet<EnvironmentCombination>();
		WorkpackageRunConfiguration wprConfiguration=null;
		RunConfiguration runConfiguration = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkpackageRunConfiguration.class, "wprc");
			
		
			c.createAlias("wprc.runconfiguration", "rc");
			c.createAlias("wprc.workpackage", "wp");
			c.add(Restrictions.eq("wp.workPackageId",workpackageId));
			
			
			wprunConfigurations=c.list();			
			
			if(wprunConfigurations!=null &&! wprunConfigurations.isEmpty()){
				for(WorkpackageRunConfiguration wprc:wprunConfigurations){
					Hibernate.initialize(wprc.getRunconfiguration());
					runConfiguration=wprc.getRunconfiguration();
					Hibernate.initialize(runConfiguration.getEnvironmentcombination());
					ec.add(runConfiguration.getEnvironmentcombination());
					Hibernate.initialize(runConfiguration.getGenericDevice());
					Hibernate.initialize(runConfiguration.getProduct());
				}
			}
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return ec;	
	}

	@Override
	@Transactional
	public Set<GenericDevices> getDevicesMappedToWorkpackage(int workpackageId) {
		List<WorkpackageRunConfiguration> wprunConfigurations = null;
		Set<GenericDevices> gd=new HashSet<GenericDevices>();
		WorkpackageRunConfiguration wprConfiguration=null;
		RunConfiguration runConfiguration = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkpackageRunConfiguration.class, "wprc");
			
		
			c.createAlias("wprc.runconfiguration", "rc");
			c.createAlias("wprc.workpackage", "wp");
			c.add(Restrictions.eq("wp.workPackageId",workpackageId));
			
			
			wprunConfigurations=c.list();			
			
			if(wprunConfigurations!=null &&! wprunConfigurations.isEmpty()){
				for(WorkpackageRunConfiguration wprc:wprunConfigurations){
					Hibernate.initialize(wprc.getRunconfiguration());
					runConfiguration=wprc.getRunconfiguration();
					Hibernate.initialize(runConfiguration.getEnvironmentcombination());
					Hibernate.initialize(runConfiguration.getGenericDevice());
					if(runConfiguration.getGenericDevice()!=null)
						gd.add(runConfiguration.getGenericDevice());
					
					Hibernate.initialize(runConfiguration.getProduct());
				}
			}
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return gd;	

	}

	@Override
	@Transactional
	public void mapWorkpackageWithTestSuite(Integer workPackageId,
			Integer testSuiteId, String action) {

				try {
					log.debug("workPackageId--->"+workPackageId);
					log.debug("testcaseId--->"+testSuiteId);
					WorkPackage workPackage =getWorkPackageByIdWithMinimalnitialization(workPackageId);
					TestSuiteList testSuite= testSuiteListDAO.getByTestSuiteId(testSuiteId);
					if (workPackage != null && testSuite != null) {
						boolean needToUpdateOrAdd = false;

						Set<TestSuiteList> testSuiteSet = workPackage.getTestSuiteList();
						if (action.equalsIgnoreCase("Add")) {
							if (testSuiteSet.size() == 0) {
								needToUpdateOrAdd = true;
							} else {
								TestSuiteList testSuiteForProcessing = testSuiteSet
										.iterator().next();
								if (testSuiteForProcessing != null) {
									int alreadyAvailableTestSuiteId = testSuiteForProcessing
											.getTestSuiteId().intValue();

									if (alreadyAvailableTestSuiteId != testSuiteId) {
										
										TestSuiteList testSuiteAvailable = testSuiteListDAO.getByTestSuiteId(alreadyAvailableTestSuiteId); 
										for (WorkPackage wp : testSuiteAvailable
												.getWorkPackageList()) {
											if (wp.getWorkPackageId().intValue() == workPackageId) {
												sessionFactory.getCurrentSession()
														.saveOrUpdate(
																testSuiteAvailable);
												break;
											}
										}

										needToUpdateOrAdd = true;
									}
								}
							}

							if (needToUpdateOrAdd) {
								workPackage.getTestSuiteList().add(testSuite);
								testSuite.getWorkPackageList().add(workPackage);
								sessionFactory.getCurrentSession().saveOrUpdate(
										workPackage);
								sessionFactory.getCurrentSession().saveOrUpdate(
										testSuite);
							}
						}
						else if(action.equalsIgnoreCase("Remove")){

							log.debug("Remove testsuite from Workpackage");

							try {

								Set<TestSuiteList> testSuiteList = workPackage.getTestSuiteList();
								testSuiteList.remove(testSuite);
								
								workPackage.setTestSuiteList(testSuiteList);
								
								sessionFactory.getCurrentSession().save(workPackage);
								
								log.debug("Removed workPackage from workpackage successfully");
							} catch (RuntimeException re) {
								log.error("Failed to remove workPackage from workpackage", re);
								
							}
						
						}
					}
				} catch (RuntimeException re) {
					log.error("list specific failed", re);
				}
	
	}

	@Override
	@Transactional
	public WorkPackageTestCaseExecutionPlan workPackageTestCasesExecutionPlanByJobId(
			TestRunJob reportingTestRunJob, TestRunJob executionTestRunJob, TestCaseList testCaseList) {
		List<WorkPackageTestCaseExecutionPlan> listWorkPackageTestCaseExecutionPlan = null;
		WorkPackageTestCaseExecutionPlan dl=null;
		try {
			listWorkPackageTestCaseExecutionPlan =sessionFactory
					.getCurrentSession()
					.createQuery(
							" from  WorkPackageTestCaseExecutionPlan wptcep where wptcep.workPackage.workPackageId = " + reportingTestRunJob.getWorkPackage().getWorkPackageId()
							+ " and wptcep.testCase.testCaseId = "+testCaseList.getTestCaseId()
							/* + " and wptcep.runConfiguration.runconfiguration.runconfigId = " + testRunJob.getRunConfiguration().getRunconfigId()+
							+ " and wptcep.hostList.hostId = " + testRunJob.getHostList().getHostId() */
							+ " and wptcep.testRunJob.testRunJobId = " + reportingTestRunJob.getTestRunJobId() 
							+ " and wptcep.testRunJob.environmentCombination.environment_combination_id = " + executionTestRunJob.getEnvironmentCombination().getEnvironment_combination_id()
							+ " and wptcep.testSuiteList.testSuiteId = " + executionTestRunJob.getTestSuite().getTestSuiteId()
							).list();			
			dl = (listWorkPackageTestCaseExecutionPlan != null && listWorkPackageTestCaseExecutionPlan.size() != 0) ? (WorkPackageTestCaseExecutionPlan) listWorkPackageTestCaseExecutionPlan
					.get(0) : null;

			if (dl!=null) {

					Hibernate.initialize(dl.getTestCase());
					Hibernate.initialize(dl.getWorkPackage());
					Hibernate.initialize(dl.getTester());
					Hibernate.initialize(dl.getTestLead());
					Hibernate.initialize(dl.getExecutionPriority());
					if(dl.getEnvironmentList().size()!=0){
						Hibernate.initialize(dl.getEnvironmentList());
					}
					Hibernate.initialize(dl.getActualWorkShiftMaster());
					Hibernate.initialize(dl.getPlannedWorkShiftMaster());
					Hibernate.initialize(dl.getTestCase().getDecouplingCategory());
					Hibernate.initialize(dl.getTestCaseExecutionResult());
					Hibernate.initialize(dl.getTestCase().getTestcaseTypeMaster());
					Hibernate.initialize(dl.getTestCase().getTestCasePriority());
					Hibernate.initialize(dl.getHostList());

					Hibernate.initialize(dl.getTestCase().getTestCasePriority());
					Hibernate.initialize(dl.getTestSuiteList());
					if(dl.getRunConfiguration()!=null){
						Hibernate.initialize(dl.getRunConfiguration());
						Hibernate.initialize(dl.getRunConfiguration().getRunconfiguration());
						if(dl.getRunConfiguration().getRunconfiguration()!=null)
							if(dl.getRunConfiguration().getRunconfiguration().getEnvironmentcombination()!=null){
								Hibernate.initialize(dl.getRunConfiguration().getRunconfiguration().getEnvironmentcombination());
							}
						if(dl.getRunConfiguration().getRunconfiguration().getGenericDevice()!=null){
							Hibernate.initialize(dl.getRunConfiguration().getRunconfiguration().getGenericDevice());
						}
						Hibernate.initialize(dl.getRunConfiguration().getWorkpackage());
					}
			}	
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return dl;
	}

	
	@Override
	@Transactional
	public WorkPackageTestCaseExecutionPlan workPackageTestCasesExecutionPlanByJobId(
			TestRunJob testRunJob,TestCaseList testCaseList) {
		return workPackageTestCasesExecutionPlanByJobId(testRunJob, testRunJob, testCaseList);
	}

	@Override
	@Transactional
	public WorkPackageTestSuite workPackageTestCasesByJobId(
			TestRunJob testRunJob) {
		log.debug("getting WorkPackageTestCase instance by id");
		WorkPackageTestSuite workPackageTestSuite = null;
		List<WorkPackageTestSuite> wptsList=null;
		try {			
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackageTestSuite.class, "wts");		
			c.createAlias("wts.workPackage", "wp");
			c.createAlias("wts.testSuite", "ts");
			c.add(Restrictions.eq("wp.workPackageId",testRunJob.getWorkPackage().getWorkPackageId()));
			c.add(Restrictions.eq("ts.testSuiteId",testRunJob.getTestSuite().getTestSuiteId()));
			wptsList=c.list();
			workPackageTestSuite = (wptsList != null && wptsList.size() != 0) ? (WorkPackageTestSuite) wptsList
					.get(0) : null;
			if (workPackageTestSuite != null) {
				Hibernate.initialize(workPackageTestSuite.getWorkPackage());
				Hibernate.initialize(workPackageTestSuite.getTestSuite());
				Hibernate.initialize(workPackageTestSuite.getTestSuite().getTestCaseLists());
				Hibernate.initialize(workPackageTestSuite.getTestSuite().getExecutionPriority());
			}
			log.debug("getByProductId successful");
		} catch (RuntimeException re) {
			log.error("getByProductId failed", re);
		}
		return workPackageTestSuite;
}
	@Override
	@Transactional
	public List<TestCaseExecutionResult> listTestCaseExecutionresultBywpRunconfigId(int workpackageId,Integer wpRunConfigId,Integer testCaseId,Integer priorityId) {
		List<TestCaseExecutionResult> trcList=null;
		Criteria c1 = sessionFactory.getCurrentSession().createCriteria(TestCaseExecutionResult.class, "tcExecutionRes");
		c1.createAlias("tcExecutionRes.workPackageTestCaseExecutionPlan", "wptcep");
		c1.createAlias("wptcep.workPackage", "workPackage");
		
		
		
		if (workpackageId !=0) {
			
			c1.add(Restrictions.eq("workPackage.workPackageId", workpackageId));
		} 
		if(wpRunConfigId!=0){
			
			c1.createAlias("wptcep.runConfiguration", "wprunConfig");
			c1.add(Restrictions.eq("wprunConfig.workpackageRunConfigurationId", wpRunConfigId));
		}
		if(testCaseId!=0){
			c1.createAlias("wptcep.testCase", "testCase");
			c1.add(Restrictions.eq("testCase.testCaseId", testCaseId));
		}
		if(priorityId!=0){
			c1.createAlias("wptcep.executionPriority", "executionPriority");
			c1.add(Restrictions.eq("executionPriority.executionPriorityId", priorityId));
		}
		 trcList=c1.list();
		return trcList;
	}

	@Override
	public List<TestCaseExecutionResult> listTestCaseExecutionresultBywpRunconfigId(
			int workpackageId, Integer wpRunConfigId, Integer testCaseId) {
		return null;
	}

	@Override
	@Transactional
	public void addTestRunJob(RunConfiguration runConfiguration,TestSuiteList testSuiteList,WorkPackage workPackage,TestRunPlan testRunPlan) {
		log.debug("adding addTestRunJob instance");
		try {	
			TestRunJob testRunJob= new TestRunJob();
			testRunJob.setEnvironmentCombination(runConfiguration.getEnvironmentcombination());
			testRunJob.setGenericDevices(runConfiguration.getGenericDevice());
			testRunJob.setHostList(runConfiguration.getHostList());
			testRunJob.setRunConfiguration(runConfiguration);
			testRunJob.setTestRunStartTime(DateUtility.getCurrentTime());
			testRunJob.setTestRunStatus(4);
			testRunJob.setTestSuite(testSuiteList);
			testRunJob.setWorkPackage(workPackage);
			if(testRunPlan!=null)
				testRunJob.setTestRunPlan(testRunPlan);
			sessionFactory.getCurrentSession().save(testRunJob);
			log.debug("add successful");
		} catch (RuntimeException re) {
			re.printStackTrace();
			log.error("add failed", re);
		}		
	}

	@Override
	@Transactional
	public TestRunJob getTestRunJobByWP(WorkPackage workPackage,
			RunConfiguration runConfiguration) {
		log.debug("getting getTestRunJobByWP instance by id");
		TestRunJob testRunJob = null;
		List<TestRunJob> testRunJobList=null;
		try {

			
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestRunJob.class, "trj");
			
			
			c.createAlias("trj.workPackage", "wp");
			c.createAlias("trj.runConfiguration", "rc");
			c.add(Restrictions.eq("wp.workPackageId",workPackage.getWorkPackageId()));
			c.add(Restrictions.eq("rc.runconfigId",runConfiguration.getRunconfigId()));
			c.add(Restrictions.eq("rc.runConfigStatus", 1));
			testRunJobList=c.list();
			testRunJob = (testRunJobList != null && testRunJobList.size() != 0) ? (TestRunJob) testRunJobList
					.get(0) : null;
			if (testRunJob != null) {
				Hibernate.initialize(testRunJob.getWorkPackage());
				Hibernate.initialize(testRunJob.getTestSuite());
				if(testRunJob.getTestSuite()!=null)
					Hibernate.initialize(testRunJob.getTestSuite().getTestCaseLists());
				Hibernate.initialize(testRunJob.getTestCaseListSet());
				Hibernate.initialize(testRunJob.getTestSuiteSet());
				Hibernate.initialize(testRunJob.getFeatureSet());

				Hibernate.initialize(testRunJob.getRunConfiguration());

			}
			log.debug("TestRunJob successful");
		} catch (Exception re) {
			re.printStackTrace();
			log.error("TestRunJob failed", re);
		}
		return testRunJob;
	}

	@Override
	@Transactional
	public void mapTestRunJobTestCase(Integer testRunJobId, Integer testcaseId,
			String type) {

		TestRunJob testRunJob = null;
		TestCaseList testCase = null;

		try {
			testRunJob = environmentDAO.getTestRunJobById(testRunJobId);
			testCase = testCaseListDAO.getByTestCaseId(testcaseId);

			if (testRunJob != null && testCase != null) {
				boolean needToUpdateOrAdd = false;

				Set<TestCaseList> testCaseSet = testRunJob
						.getTestCaseListSet();
				if (type.equalsIgnoreCase("Add")) {

					if (testCaseSet.size() == 0) {
						needToUpdateOrAdd = true;
					} else {
						TestCaseList testCaseForProcessing = testCaseSet
								.iterator().next();
						if (testCaseForProcessing != null) {
							int alreadyAvailableTestCaseId = testCaseForProcessing
									.getTestCaseId().intValue();

							if (alreadyAvailableTestCaseId != testcaseId) {

								TestCaseList testCaseAvailable = testCaseListDAO.getByTestCaseId(alreadyAvailableTestCaseId);
								for (TestRunJob trj : testCaseAvailable.getTestRunJobSet()) {

									if (trj.getTestRunJobId().intValue() == testRunJobId) {
										sessionFactory.getCurrentSession()
												.saveOrUpdate(
														testCaseAvailable);
										break;
									}
								}

								needToUpdateOrAdd = true;
							}
						}
					}

					if (needToUpdateOrAdd) {
						testRunJob.getTestCaseListSet().add(testCase);
						testCase.getTestRunJobSet().add(testRunJob);
						sessionFactory.getCurrentSession().saveOrUpdate(
								testRunJob);
						sessionFactory.getCurrentSession().saveOrUpdate(
								testCase);
					}
				}
				else if(type.equalsIgnoreCase("Remove")){

					try {
						testRunJob = (TestRunJob) sessionFactory.getCurrentSession().get(TestRunJob.class, testRunJobId);
						if (testRunJob == null) {
							log.debug("testrunjob with specified id not found : " + testRunJobId);
						}
						testCase = (TestCaseList) sessionFactory.getCurrentSession().get(TestCaseList.class, testcaseId);
						if (testCase == null) {
							log.debug("testcaese could not found in the database : " + testcaseId);
						
						}
						Set<TestCaseList> testcaseList = testRunJob.getTestCaseListSet();
					
						testcaseList.remove(testCase);
						
						
						testRunJob.setTestCaseListSet(testcaseList);
						
						sessionFactory.getCurrentSession().save(testRunJob);
						
						log.debug("Removed locale from workpackage successfully");
					} catch (RuntimeException re) {
						log.error("Failed to remove locale from workpackage", re);
						
					}
				
				}
			}
		} catch (RuntimeException re) {
			log.error("list specific failed", re);
			// throw re;
		}
	}

	@Override
	@Transactional
	public void mapTestRunJobTestSuite(Integer testRunJobId,
			Integer testsuiteId, String type) {

				TestRunJob testRunJob = null;
				TestSuiteList testSuite = null;

				try {
					testRunJob = environmentDAO.getTestRunJobById(testRunJobId);
					testSuite = testSuiteListDAO.getByTestSuiteId(testsuiteId);

					if (testRunJob != null && testSuite != null) {
						boolean needToUpdateOrAdd = false;

						Set<TestSuiteList> testSuiteSet = testRunJob
								.getTestSuiteSet();
						if (type.equalsIgnoreCase("Add")) {

							if (testSuiteSet.size() == 0) {
								needToUpdateOrAdd = true;
							} else {
								TestSuiteList testSuiteForProcessing = testSuiteSet
										.iterator().next();
								if (testSuiteForProcessing != null) {
									int alreadyAvailableTestSuiteId = testSuiteForProcessing
											.getTestSuiteId().intValue();

									if (alreadyAvailableTestSuiteId != testsuiteId) {

										TestSuiteList testSuiteAvailable = testSuiteListDAO.getByTestSuiteId(alreadyAvailableTestSuiteId);
										for (TestRunJob trj : testSuiteAvailable.getTestRunJobSet()) {
											log.debug("trj.getTestRunJobId().intValue()"
													+ trj.getTestRunJobId().intValue());

											if (trj.getTestRunJobId().intValue() == testRunJobId) {
												log.debug("testrunjo found in testcase");
												sessionFactory.getCurrentSession()
														.saveOrUpdate(
																testSuiteAvailable);
												log.debug("testCaseAvailable.getWorkPackageList().size()="
														+ testSuiteAvailable.getTestRunJobSet()
																.size());
												break;
											}
										}

										needToUpdateOrAdd = true;
									}
								}
							}

							if (needToUpdateOrAdd) {
								testRunJob.getTestSuiteSet().add(testSuite);
								testSuite.getTestRunJobSet().add(testRunJob);
								sessionFactory.getCurrentSession().saveOrUpdate(
										testRunJob);
								sessionFactory.getCurrentSession().saveOrUpdate(
										testSuite);
							}
						}
						else if(type.equalsIgnoreCase("Remove")){

							try {
								log.debug("testsuiteId input: "  +testsuiteId);
								testRunJob = (TestRunJob) sessionFactory.getCurrentSession().get(TestRunJob.class, testRunJobId);
								if (testRunJob == null) {
									log.debug("testrunjob with specified id not found : " + testRunJobId);
								}
								testSuite = (TestSuiteList) sessionFactory.getCurrentSession().get(TestSuiteList.class, testsuiteId);
								if (testSuite == null) {
									log.debug("testcaese could not found in the database : " + testsuiteId);
								}
								Set<TestSuiteList> testsuiteList = testRunJob.getTestSuiteSet();
								testsuiteList.remove(testSuite);
								
								testRunJob.setTestSuiteSet(testsuiteList);
								
								sessionFactory.getCurrentSession().save(testRunJob);
								
								log.debug("Removed locale from workpackage successfully");
							} catch (RuntimeException re) {
								log.error("Failed to remove locale from workpackage", re);
								
							}
						
						}
					}
				} catch (RuntimeException re) {
					log.error("list specific failed", re);
				}
	}

	@Override
	@Transactional
	public int addNewWorkPackageFeature(
			List<WorkPackageFeature> workPackageFeatures) {
		log.debug("Initializing workpackge with new test cases");
		int count = 0;
		try {

			if (workPackageFeatures == null || workPackageFeatures.isEmpty()) {
				return 0;
			}
			for (WorkPackageFeature workPackageFeature : workPackageFeatures) {
				sessionFactory.getCurrentSession().save(workPackageFeature);
				count++;
			}
			log.debug("added workpackage test cases successfully");
		} catch (Exception e) {

			log.error("Unable to initialize workpackage with new testcases", e);
			return count;
		}

		return count;
	}

	@Override
	@Transactional
	public List<WorkPackageFeature> listWorkPackageFeature(
			Integer workPackageId, Integer jtStartIndex, Integer jtPageSize) {
		log.debug("listing listWorkPackageFeature instance");
		List<WorkPackageFeature> workPackageFeature = null;
		try {
			if(jtStartIndex!=-1 && jtPageSize!=-1){
				workPackageFeature = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from WorkPackageFeature wpf where wpf.workPackage.workPackageId = "
									+ workPackageId
									+ " order by wpf.feature.productFeatureId asc")
					.setFirstResult(jtStartIndex).setMaxResults(jtPageSize).list();
			}else{
				workPackageFeature = sessionFactory
						.getCurrentSession()
						.createQuery(
								"from WorkPackageFeature wpf where wpf.workPackage.workPackageId = "
										+ workPackageId
										+ " order by wpf.feature.productFeatureId asc").list();
					
			}
			if (!(workPackageFeature == null || workPackageFeature
					.isEmpty())) {
				for (WorkPackageFeature dl : workPackageFeature) {
					Hibernate.initialize(dl.getFeature());
					Hibernate.initialize(dl.getWorkPackage().getProductBuild().getProductVersion().getProductMaster());
					Hibernate.initialize(dl.getFeature().getTestCaseList());
				}
			}
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return workPackageFeature;
	}
	
	@Override
	@Transactional
	public List<JsonWorkPackageFeature> listJsonWorkPackageFeature(Integer workPackageId, Integer jtStartIndex, Integer jtPageSize) {
		log.debug("listing listJsonWorkPackageFeature by workPackageId");
		List<JsonWorkPackageFeature> jsonWorkPackageFeatures=new ArrayList<JsonWorkPackageFeature>();
		
		List<WorkPackageFeature> workPackageFeature = null;
		try {
			if(jtStartIndex!=-1 && jtPageSize!=-1){
				workPackageFeature = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from WorkPackageFeature wpf where wpf.workPackage.workPackageId = "
									+ workPackageId
									+ " order by wpf.feature.productFeatureId asc")
					.setFirstResult(jtStartIndex).setMaxResults(jtPageSize).list();
			}else{
				workPackageFeature = sessionFactory
						.getCurrentSession()
						.createQuery(
								"from WorkPackageFeature wpf where wpf.workPackage.workPackageId = "
										+ workPackageId
										+ " order by wpf.feature.productFeatureId asc").list();
					
			}
			if (!(workPackageFeature == null || workPackageFeature
					.isEmpty())) {
				for (WorkPackageFeature dl : workPackageFeature) {					
					Hibernate.initialize(dl.getFeature());
					Hibernate.initialize(dl.getWorkPackage().getProductBuild().getProductVersion().getProductMaster());
					Hibernate.initialize(dl.getFeature().getTestCaseList());
					jsonWorkPackageFeatures.add(new JsonWorkPackageFeature(dl));
				}
			}
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return jsonWorkPackageFeatures;
	}
	@Override
	@Transactional
	public Integer getWorkPackageFeatureCount(Integer workPackageId, Integer jtStartIndex, Integer jtPageSize) {
		log.debug("getWorkPackageFeatureCount by workPackageId");
		
		int wpFeatureCount =0;		
			String sql="select count(*) from workpackage_feature wpf where wpf.workpackageId=:wpid group by wpf.featureId order by wpf.featureId asc";
		try {
			if(jtStartIndex!=-1 && jtPageSize!=-1){
				wpFeatureCount=((Number) sessionFactory.getCurrentSession().createSQLQuery(sql).setParameter("wpid", workPackageId)
						.setFirstResult(jtStartIndex).setMaxResults(jtPageSize)
						.uniqueResult()).intValue();				
			}else{
				Object wpFeatCountObj = sessionFactory.getCurrentSession().createSQLQuery(sql).setParameter("wpid", workPackageId)
						.uniqueResult() ;
				if(wpFeatCountObj != null){
					wpFeatureCount=((Number)wpFeatCountObj).intValue();
				}
								
			}
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return wpFeatureCount;
	}
	@Override
	@Transactional
	public int seedWorkPackageWithNewFeature(
			List<ProductFeature> newFeatures, Integer workPackageId) {

		int workPackagesFeatureCount = 0;
		WorkPackage workPackage = getWorkPackageById(workPackageId);
			List<WorkPackageFeature> workPackageFeatures = new ArrayList<WorkPackageFeature>();
			WorkPackageFeature workPackageFeature = null;
			for (ProductFeature feature : newFeatures) {
				workPackageFeature = new WorkPackageFeature();
				workPackageFeature.setFeature(feature);
				workPackageFeature.setWorkPackage(workPackage);
				workPackageFeature.setCreatedDate(new Date(System.currentTimeMillis()));
				workPackageFeature.setEditedDate(new Date(System.currentTimeMillis()));
				workPackageFeature.setIsSelected(0);
				workPackageFeature.setStatus("ACTIVE");
				workPackageFeatures.add(workPackageFeature);
			}
			workPackagesFeatureCount = addNewWorkPackageFeature(workPackageFeatures);
			return workPackagesFeatureCount;	
		
	}

	@Override
	@Transactional
	public WorkPackageFeature getWorkPackageFeatureById(
			Integer workpackageFeatureId) {
		log.debug("getting WorkPackageFeature instance by id");
		WorkPackageFeature workPackageFeature = null;
		try {

			List list = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from WorkPackageFeature w where id=:workPackageId")
					.setParameter("workPackageId", workpackageFeatureId).list();
			workPackageFeature = (list != null && list.size() != 0) ? (WorkPackageFeature) list
					.get(0) : null;
			if (workPackageFeature != null) {
				Hibernate.initialize(workPackageFeature.getWorkPackage());
				Hibernate.initialize(workPackageFeature.getFeature());
				Hibernate.initialize(workPackageFeature.getFeature().getTestCaseList());
			}
			log.debug("getByProductId successful");
		} catch (RuntimeException re) {
			log.error("getByProductId failed", re);
		}
		return workPackageFeature;

	}

	@Override
	@Transactional
	public WorkPackageFeature updateWorkPackageFeature(
			WorkPackageFeature workPackageFeatureFromDB) {
				int count = 0;
				try {

					if (workPackageFeatureFromDB == null) {
						return null;
					}

					sessionFactory.getCurrentSession()
							.saveOrUpdate(workPackageFeatureFromDB);

					log.debug("Updated workpackage test suite successfully");
				} catch (Exception e) {

					log.error("Unable to workPackageTestSuiteFromDB ", e);
					return null;
				}

				return workPackageFeatureFromDB;
	}
	
	
	@Override
	@Transactional
	public void mapWorkpackageWithFeature(Integer workPackageId,
			Integer featureId, String action) {
				try {
					
					WorkPackage workPackage =getWorkPackageByIdWithMinimalnitialization(workPackageId);
					ProductFeature feature= productFeatureDAO.getByProductFeatureId(featureId);
					if (workPackage != null && feature != null) {
						boolean needToUpdateOrAdd = false;

						Set<ProductFeature> featureSet = workPackage.getProductFeature();
						if (action.equalsIgnoreCase("Add")) {
							if (featureSet.size() == 0) {
								needToUpdateOrAdd = true;
							} else {
								ProductFeature featureForProcessing = featureSet
										.iterator().next();
								if (featureForProcessing != null) {
									int alreadyAvailableFeatureId = featureForProcessing
											.getProductFeatureId().intValue();

									if (alreadyAvailableFeatureId != featureId) {

										ProductFeature featureAvailable = productFeatureDAO.getByProductFeatureId(featureId); 
										for (WorkPackage wp : featureAvailable
												.getWorkPackageList()) {
											log.debug("wp.getWorkPackageId().intValue()"
													+ wp.getWorkPackageId().intValue());

											if (wp.getWorkPackageId().intValue() == workPackageId) {
												log.debug("workPackageId found ");
												sessionFactory.getCurrentSession()
														.saveOrUpdate(
																featureAvailable);
												log.debug("testSuiteAvailable.getWorkPackageList().size()="
														+ featureAvailable
																.getWorkPackageList()
																.size());
												break;
											}
										}

										needToUpdateOrAdd = true;
									}
								}
							}

							if (needToUpdateOrAdd) {
								workPackage.getProductFeature().add(feature);
								feature.getWorkPackageList().add(workPackage);
								sessionFactory.getCurrentSession().saveOrUpdate(
										workPackage);
								sessionFactory.getCurrentSession().saveOrUpdate(
										feature);
							}
						}
						else if(action.equalsIgnoreCase("Remove")){

							log.debug("Remove testsuite from Workpackage");

							try {

								Set<ProductFeature> featureList = workPackage.getProductFeature();
								featureList.remove(feature);
								
								workPackage.setProductFeature(featureList);
								
								sessionFactory.getCurrentSession().save(workPackage);
								
								log.debug("Removed workPackage from workpackage successfully");
							} catch (RuntimeException re) {
								log.error("Failed to remove workPackage from workpackage", re);
								
							}
						
						}
					}
				} catch (RuntimeException re) {
					log.error("list specific failed", re);
				}
	
	}

	@Override
	@Transactional
	public void addWorkpackageFeatureExecutionPlan(
			WorkPackageFeatureExecutionPlan workPackageFeatureExecutionPlan) {
		log.debug("adding WorkPackageFeatureExecutionPlan instance");
		try {	
			sessionFactory.getCurrentSession().save(workPackageFeatureExecutionPlan);
			log.debug("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}		
	}

	@Override
	@Transactional
	public void mapTestRunJobFeature(Integer testRunJobId, Integer featureId,
			String type) {

				TestRunJob testRunJob = null;
				ProductFeature feature = null;

				try {
					testRunJob = environmentDAO.getTestRunJobById(testRunJobId);
					feature = productFeatureDAO.getByProductFeatureId(featureId);

					if (testRunJob != null && feature != null) {
						boolean needToUpdateOrAdd = false;

						Set<ProductFeature> featureSet = testRunJob
								.getFeatureSet();
						if (type.equalsIgnoreCase("Add")) {

							if (featureSet.size() == 0) {
								needToUpdateOrAdd = true;
							} else {
								ProductFeature featureForProcessing = featureSet
										.iterator().next();
								if (featureForProcessing != null) {
									int alreadyAvailableFeatureId = featureForProcessing
											.getProductFeatureId().intValue();

									if (alreadyAvailableFeatureId != featureId) {
										

										ProductFeature featureAvailable = productFeatureDAO.getByProductFeatureId(alreadyAvailableFeatureId);
										for (TestRunJob trj : featureAvailable.getTestRunJobSet()) {
											
											if (trj.getTestRunJobId().intValue() == testRunJobId) {
												sessionFactory.getCurrentSession()
														.saveOrUpdate(
																featureAvailable);
												break;
											}
										}

										needToUpdateOrAdd = true;
									}
								}
							}

							if (needToUpdateOrAdd) {
								testRunJob.getFeatureSet().add(feature);
								feature.getTestRunJobSet().add(testRunJob);
								sessionFactory.getCurrentSession().saveOrUpdate(
										testRunJob);
								sessionFactory.getCurrentSession().saveOrUpdate(
										feature);
							}
						}
						else if(type.equalsIgnoreCase("Remove")){

							try {
								testRunJob = (TestRunJob) sessionFactory.getCurrentSession().get(TestRunJob.class, testRunJobId);
								if (testRunJob == null) {
									log.debug("testrunjob with specified id not found : " + testRunJobId);
								}
								feature = (ProductFeature) sessionFactory.getCurrentSession().get(ProductFeature.class, featureId);
								if (feature == null) {
									log.debug("testcaese could not found in the database : " + featureId);
								}
								Set<ProductFeature> featureList = testRunJob.getFeatureSet();
								log.debug("testcase set size before removing :"+ featureList.size());
								featureList.remove(feature);
								log.debug("testcase set size  after removing ::"+ featureList.size());
								
								testRunJob.setFeatureSet(featureList);
								
								sessionFactory.getCurrentSession().save(testRunJob);
								
								log.debug("Removed locale from workpackage successfully");
							} catch (RuntimeException re) {
								log.error("Failed to remove locale from workpackage", re);
								
							}
						
						}
					}
				} catch (RuntimeException re) {
					log.error("list specific failed", re);
				}
	}

	@Override
	@Transactional
	public List<WorkPackageFeatureExecutionPlan> listWorkPackageFeatureExecutionPlan(Map<String, String> searchString,
			Integer workPackageId, Integer jtStartIndex, Integer jtPageSize,int status) {
			List<WorkPackageFeatureExecutionPlan> workPackageFeatureExecutionPlan = null;
			
			try {
				Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackageFeatureExecutionPlan.class, "wpfep");
				c.createAlias("wpfep.workPackage", "workPackage");
				
				if (workPackageId !=0) {
					c.add(Restrictions.eq("workPackage.workPackageId", workPackageId));
				} 
				if(searchString.get("searchFeatureName") != null && searchString.get("searchFeatureName") !=""){
					c.createAlias("wpfep.feature", "feature");
					c.add(Restrictions.ilike("feature.productFeatureName", searchString.get("searchFeatureName"),MatchMode.ANYWHERE));
				}
				if(searchString.get("searchFeatureDescription") != null && searchString.get("searchFeatureDescription") !=""){
					log.debug("searchFeatureDescription==>"+searchString.values());
					c.createAlias("wpfep.feature", "feature");
					c.add(Restrictions.ilike("feature.productFeatureDescription", searchString.get("searchFeatureDescription"),MatchMode.ANYWHERE));
				}
				
				if(searchString.get("searchECName") != null || searchString.get("searchDeviceName") != null)
				{
					c.createAlias("wpfep.runConfiguration", "wrc");
					c.createAlias("wrc.runconfiguration", "rc");
				}
				if(searchString.get("searchECName") != null && searchString.get("searchECName") !=""){
					log.debug("environmentcombination==>"+searchString.values());
					c.createAlias("rc.environmentcombination", "ec");
					c.add(Restrictions.ilike("ec.environmentCombinationName", searchString.get("searchECName"),MatchMode.ANYWHERE));
				}
				
				if(searchString.get("searchDeviceName") != null && searchString.get("searchDeviceName") !=""){
					log.debug("DeviceName==>"+searchString.values());
					c.createAlias("rc.genericDevice", "gd");
					c.add(Restrictions.ilike("gd.name", searchString.get("searchDeviceName"),MatchMode.ANYWHERE));
				}
				
				if(searchString.get("searchPED") !=null && !searchString.get("searchPED").equals("") && !searchString.get("searchPED").equals("undefined")){
					c.add(Restrictions.eq("wpfep.plannedExecutionDate", DateUtility.dateformatWithOutTime(searchString.get("searchPED"))));
				} 
				if(searchString.get("searchPlannedShift") != null && searchString.get("searchPlannedShift") !=""){
					log.debug("searchPlannedShift==>"+searchString.values());
					c.createAlias("wpfep.plannedWorkShiftMaster", "pws");
					c.add(Restrictions.ilike("pws.shiftName", searchString.get("searchPlannedShift"),MatchMode.ANYWHERE));
				}
				
				if(searchString.get("searchTestLeadName") != null && searchString.get("searchTestLeadName") !=""){
					c.createAlias("wpfep.testLead", "tl");
					c.add(Restrictions.ilike("tl.loginId", searchString.get("searchTestLeadName"),MatchMode.ANYWHERE));
				}
				
				if(searchString.get("searchTesterName") != null && searchString.get("searchTesterName") !=""){
					c.createAlias("wpfep.tester", "t");
					c.add(Restrictions.ilike("t.loginId", searchString.get("searchTesterName"),MatchMode.ANYWHERE));
				}
				if(status!=2)
					c.add(Restrictions.eq("status", status));
				workPackageFeatureExecutionPlan = c.setFirstResult(jtStartIndex).setMaxResults(jtPageSize).list();

				
				if (!(workPackageFeatureExecutionPlan == null || workPackageFeatureExecutionPlan
						.isEmpty())) {
					for (WorkPackageFeatureExecutionPlan dl : workPackageFeatureExecutionPlan) {
						Hibernate.initialize(dl.getFeature());
						Hibernate.initialize(dl.getHostList());
						Hibernate.initialize(dl.getWorkPackage());
						Hibernate.initialize(dl.getExecutionPriority());
							Hibernate.initialize(dl.getTester());
						Hibernate.initialize(dl.getTestLead());
						Hibernate.initialize(dl.getActualWorkShiftMaster());
						Hibernate.initialize(dl.getPlannedWorkShiftMaster());
						Hibernate.initialize(dl.getWorkPackage().getProductBuild().getProductVersion());
						Hibernate.initialize(dl.getWorkPackage().getProductBuild().getProductVersion().getProductMaster());
						Hibernate.initialize(dl.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductMode());
						if(dl.getRunConfiguration()!=null){
							if(dl.getRunConfiguration().getRunconfiguration()!=null){
								Hibernate.initialize(dl.getRunConfiguration().getRunconfiguration());
								if(dl.getRunConfiguration().getRunconfiguration().getEnvironmentcombination()!=null)
									Hibernate.initialize(dl.getRunConfiguration().getRunconfiguration().getEnvironmentcombination());
								if(dl.getRunConfiguration().getRunconfiguration().getGenericDevice()!=null)
									Hibernate.initialize(dl.getRunConfiguration().getRunconfiguration().getGenericDevice());
							}
							Hibernate.initialize(dl.getRunConfiguration().getWorkpackage());
							Hibernate.initialize(dl.getRunConfiguration());
						}
					}
				}
			} catch (RuntimeException re) {
				log.error("list failed", re);
			}
			return workPackageFeatureExecutionPlan;

	}

	@Override
	@Transactional
	public List<JsonWorkPackageFeatureExecutionPlan> listJsonWorkPackageFeatureExecutionPlan(Map<String, String> searchString,
			Integer workPackageId, Integer jtStartIndex, Integer jtPageSize,int status) {
		log.info("listing listJsonWorkPackageFeatureExecutionPlan instance");
			List<WorkPackageFeatureExecutionPlan> workPackageFeatureExecutionPlan = null;
			List<JsonWorkPackageFeatureExecutionPlan> jsonWorkPackageFeatureExecutionPlanList=new ArrayList<JsonWorkPackageFeatureExecutionPlan>();
			try {
				Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackageFeatureExecutionPlan.class, "wpfep");
				c.createAlias("wpfep.workPackage", "workPackage");
				
				if (workPackageId !=0) {
					c.add(Restrictions.eq("workPackage.workPackageId", workPackageId));
				} 
				if(searchString.get("searchFeatureName") != null && searchString.get("searchFeatureName") !=""){
					c.createAlias("wpfep.feature", "feature");
					c.add(Restrictions.ilike("feature.productFeatureName", searchString.get("searchFeatureName"),MatchMode.ANYWHERE));
				}
				if(searchString.get("searchFeatureDescription") != null && searchString.get("searchFeatureDescription") !=""){
					log.debug("searchFeatureDescription==>"+searchString.values());
					c.createAlias("wpfep.feature", "feature");
					c.add(Restrictions.ilike("feature.productFeatureDescription", searchString.get("searchFeatureDescription"),MatchMode.ANYWHERE));
				}
				
				if(searchString.get("searchECName") != null || searchString.get("searchDeviceName") != null)
				{
					c.createAlias("wpfep.runConfiguration", "wrc");
					c.createAlias("wrc.runconfiguration", "rc");
				}
				if(searchString.get("searchECName") != null && searchString.get("searchECName") !=""){
					log.debug("environmentcombination==>"+searchString.values());
					c.createAlias("rc.environmentcombination", "ec");
					c.add(Restrictions.ilike("ec.environmentCombinationName", searchString.get("searchECName"),MatchMode.ANYWHERE));
				}
				
				if(searchString.get("searchDeviceName") != null && searchString.get("searchDeviceName") !=""){
					log.debug("DeviceName==>"+searchString.values());
					c.createAlias("rc.genericDevice", "gd");
					c.add(Restrictions.ilike("gd.name", searchString.get("searchDeviceName"),MatchMode.ANYWHERE));
				}
				
				if(searchString.get("searchPED") !=null && !searchString.get("searchPED").equals("") && !searchString.get("searchPED").equals("undefined")){
					c.add(Restrictions.eq("wpfep.plannedExecutionDate", DateUtility.dateformatWithOutTime(searchString.get("searchPED"))));
				} 
				if(searchString.get("searchPlannedShift") != null && searchString.get("searchPlannedShift") !=""){
					log.debug("searchPlannedShift==>"+searchString.values());
					c.createAlias("wpfep.plannedWorkShiftMaster", "pws");
					c.add(Restrictions.ilike("pws.shiftName", searchString.get("searchPlannedShift"),MatchMode.ANYWHERE));
				}
				
				if(searchString.get("searchTestLeadName") != null && searchString.get("searchTestLeadName") !=""){
					c.createAlias("wpfep.testLead", "tl");
					c.add(Restrictions.ilike("tl.loginId", searchString.get("searchTestLeadName"),MatchMode.ANYWHERE));
				}
				
				if(searchString.get("searchTesterName") != null && searchString.get("searchTesterName") !=""){
					c.createAlias("wpfep.tester", "t");
					c.add(Restrictions.ilike("t.loginId", searchString.get("searchTesterName"),MatchMode.ANYWHERE));
				}
				if(status!=2)
					c.add(Restrictions.eq("status", status));
				workPackageFeatureExecutionPlan = c.setFirstResult(jtStartIndex).setMaxResults(jtPageSize).list();
				
				if (!(workPackageFeatureExecutionPlan == null || workPackageFeatureExecutionPlan
						.isEmpty())) {
					for (WorkPackageFeatureExecutionPlan dl : workPackageFeatureExecutionPlan) {
						Hibernate.initialize(dl.getFeature());
						Hibernate.initialize(dl.getHostList());
						Hibernate.initialize(dl.getWorkPackage());
						Hibernate.initialize(dl.getExecutionPriority());
							Hibernate.initialize(dl.getTester());
						Hibernate.initialize(dl.getTestLead());
						Hibernate.initialize(dl.getActualWorkShiftMaster());
						Hibernate.initialize(dl.getPlannedWorkShiftMaster());
						Hibernate.initialize(dl.getWorkPackage().getProductBuild().getProductVersion());
						Hibernate.initialize(dl.getWorkPackage().getProductBuild().getProductVersion().getProductMaster());
						Hibernate.initialize(dl.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductMode());
						if(dl.getRunConfiguration()!=null){
							if(dl.getRunConfiguration().getRunconfiguration()!=null){
								Hibernate.initialize(dl.getRunConfiguration().getRunconfiguration());
								if(dl.getRunConfiguration().getRunconfiguration().getEnvironmentcombination()!=null)
									Hibernate.initialize(dl.getRunConfiguration().getRunconfiguration().getEnvironmentcombination());
								if(dl.getRunConfiguration().getRunconfiguration().getGenericDevice()!=null)
									Hibernate.initialize(dl.getRunConfiguration().getRunconfiguration().getGenericDevice());
							}
							Hibernate.initialize(dl.getRunConfiguration().getWorkpackage());
							Hibernate.initialize(dl.getRunConfiguration());
						}
						jsonWorkPackageFeatureExecutionPlanList.add(new JsonWorkPackageFeatureExecutionPlan(dl));
					}
				}
			} catch (RuntimeException re) {
				log.error("list failed", re);
			}
			return jsonWorkPackageFeatureExecutionPlanList;
	}
	@Override
	@Transactional
	public void updateWorkPackageFeatureExecutionPlan(
			WorkPackageFeatureExecutionPlan workPackageFeatureExecutionPlanFromUI) {
		log.debug("updating workPackageFeatureExecutionPlanFromUI instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(workPackageFeatureExecutionPlanFromUI);
			
			log.debug("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
		}
	}

	@Override
	@Transactional
	public WorkPackageFeatureExecutionPlan getWorkpackageFeatureExecutionPlanById(
			Integer rowId) {
		log.debug("listing getWorkpackageTestcaseExecutionPlanById instance");
		List<WorkPackageFeatureExecutionPlan> list = null;
		WorkPackageFeatureExecutionPlan dl =null;
		try {
			list = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from WorkPackageFeatureExecutionPlan where id = "
									+ rowId).list();
			dl = (list != null && list.size() != 0) ? (WorkPackageFeatureExecutionPlan)(list.get(0)): null;				
			if(dl!=null){
					Hibernate.initialize(dl.getWorkPackage().getProductBuild().getProductVersion().getProductMaster());
					Hibernate.initialize(dl.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductMode());
					Hibernate.initialize(dl.getWorkPackage());
					Hibernate.initialize(dl.getWorkPackage().getWorkFlowEvent());
					Hibernate.initialize(dl.getWorkPackage().getWorkFlowEvent().getWorkFlow());
					Hibernate.initialize(dl.getTester());
					Hibernate.initialize(dl.getFeature());
					Hibernate.initialize(dl.getTestLead());
					Hibernate.initialize(dl.getActualWorkShiftMaster());
					Hibernate.initialize(dl.getPlannedWorkShiftMaster());
					Hibernate.initialize(dl.getHostList());
					if(dl.getRunConfiguration()!=null){
						if(dl.getRunConfiguration().getRunconfiguration()!=null){ 
							Hibernate.initialize(dl.getRunConfiguration().getRunconfiguration());
							if(dl.getRunConfiguration().getRunconfiguration()!=null){
								if(dl.getRunConfiguration().getRunconfiguration().getEnvironmentcombination()!=null)
									Hibernate.initialize(dl.getRunConfiguration().getRunconfiguration().getEnvironmentcombination());
								if(dl.getRunConfiguration().getRunconfiguration().getGenericDevice()!=null)
									Hibernate.initialize(dl.getRunConfiguration().getRunconfiguration().getGenericDevice());
							}
						}
						Hibernate.initialize(dl.getRunConfiguration());
						Hibernate.initialize(dl.getRunConfiguration().getWorkpackage());
					}
					
			}
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return dl;
	
	}

	@Override
	@Transactional
	public WorkPackageFeatureExecutionPlan getWorkpackageFeatureExecutionPlan(Integer workPackageId,
			Integer productFeatureId,Integer runConfigurationId) {
		List<WorkPackageFeatureExecutionPlan> list = null;
		WorkPackageFeatureExecutionPlan dl =null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackageFeatureExecutionPlan.class, "wpfep");
			
			if (workPackageId !=0) {
				c.createAlias("wpfep.workPackage", "workPackage");
				c.add(Restrictions.eq("workPackage.workPackageId", workPackageId));
			} 
			if (productFeatureId !=0) {
				c.createAlias("wpfep.feature", "feature");
				c.add(Restrictions.eq("feature.productFeatureId", productFeatureId));
			} 
			if (runConfigurationId !=0) {
				c.createAlias("wpfep.runConfiguration", "wprc");
				c.createAlias("wprc.runconfiguration", "rc");

				c.add(Restrictions.eq("rc.runconfigId", runConfigurationId));
			} 
			list = c.list();
			dl = (list != null && list.size() != 0) ? (WorkPackageFeatureExecutionPlan)(list.get(0)): null;				
			if(dl!=null){
					Hibernate.initialize(dl.getWorkPackage().getProductBuild().getProductVersion().getProductMaster());
					Hibernate.initialize(dl.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductMode());
					Hibernate.initialize(dl.getWorkPackage());
					Hibernate.initialize(dl.getWorkPackage().getWorkFlowEvent());
					Hibernate.initialize(dl.getWorkPackage().getWorkFlowEvent().getWorkFlow());
					Hibernate.initialize(dl.getTester());
					Hibernate.initialize(dl.getTestLead());
					Hibernate.initialize(dl.getActualWorkShiftMaster());
					Hibernate.initialize(dl.getPlannedWorkShiftMaster());
					Hibernate.initialize(dl.getHostList());
					if(dl.getRunConfiguration()!=null){
						if(dl.getRunConfiguration().getRunconfiguration()!=null){ 
							Hibernate.initialize(dl.getRunConfiguration().getRunconfiguration());
							if(dl.getRunConfiguration().getRunconfiguration()!=null){
								if(dl.getRunConfiguration().getRunconfiguration().getEnvironmentcombination()!=null)
									Hibernate.initialize(dl.getRunConfiguration().getRunconfiguration().getEnvironmentcombination());
								if(dl.getRunConfiguration().getRunconfiguration().getGenericDevice()!=null)
									Hibernate.initialize(dl.getRunConfiguration().getRunconfiguration().getGenericDevice());
							}
						}
						Hibernate.initialize(dl.getRunConfiguration());
						Hibernate.initialize(dl.getRunConfiguration().getWorkpackage());
					}
					
			}
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return dl;
	}

	@Override
	@Transactional
	public void deleteWorkpackageFeatureExecutionPlan(
			WorkPackageFeatureExecutionPlan wpfep) {
		try {
			sessionFactory.getCurrentSession().delete(wpfep);
			log.debug("deletion successful");
		} catch (RuntimeException re) {
			log.error("deletion failed", re);
		}		
	}

	@Override
	@Transactional
	public void addWorkpackageTestSuiteExecutionPlan(
			WorkPackageTestSuiteExecutionPlan workPackageTestSuiteExecutionPlan) {
		log.debug("adding WorkPackageFeatureExecutionPlan instance");
		try {	
			sessionFactory.getCurrentSession().save(workPackageTestSuiteExecutionPlan);
			log.debug("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}		
	}

	@Override
	@Transactional
	public WorkPackageTestSuiteExecutionPlan getWorkpackageTestSuiteExecutionPlan(
			Integer workPackageId, Integer testSuiteId,Integer runConfigurationId) {
		List<WorkPackageTestSuiteExecutionPlan> list = null;
		WorkPackageTestSuiteExecutionPlan dl =null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackageTestSuiteExecutionPlan.class, "wptsep");
			
			if (workPackageId !=-1) {
				c.createAlias("wptsep.workPackage", "workPackage");
				c.add(Restrictions.eq("workPackage.workPackageId", workPackageId));
			} 
			if (testSuiteId !=-1) {
				c.createAlias("wptsep.testsuite", "testsuite");
				c.add(Restrictions.eq("testsuite.testSuiteId", testSuiteId));
			} 
			if (runConfigurationId !=0) {
				c.createAlias("wptsep.runConfiguration", "wprc");
				c.createAlias("wprc.runconfiguration", "rc");

				c.add(Restrictions.eq("rc.runconfigId", runConfigurationId));
			} 
			list = c.list();
			dl = (list != null && list.size() != 0) ? (WorkPackageTestSuiteExecutionPlan)(list.get(0)): null;				
			if(dl!=null){
					Hibernate.initialize(dl.getWorkPackage().getProductBuild().getProductVersion().getProductMaster());
					Hibernate.initialize(dl.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductMode());
					Hibernate.initialize(dl.getWorkPackage());
					Hibernate.initialize(dl.getWorkPackage().getWorkFlowEvent());
					Hibernate.initialize(dl.getWorkPackage().getWorkFlowEvent().getWorkFlow());
					Hibernate.initialize(dl.getTester());
					Hibernate.initialize(dl.getTestLead());
					Hibernate.initialize(dl.getActualWorkShiftMaster());
					Hibernate.initialize(dl.getPlannedWorkShiftMaster());
					Hibernate.initialize(dl.getHostList());
					if(dl.getRunConfiguration()!=null){
						if(dl.getRunConfiguration().getRunconfiguration()!=null){ 
							Hibernate.initialize(dl.getRunConfiguration().getRunconfiguration());
							if(dl.getRunConfiguration().getRunconfiguration()!=null){
								if(dl.getRunConfiguration().getRunconfiguration().getEnvironmentcombination()!=null)
									Hibernate.initialize(dl.getRunConfiguration().getRunconfiguration().getEnvironmentcombination());
								if(dl.getRunConfiguration().getRunconfiguration().getGenericDevice()!=null)
									Hibernate.initialize(dl.getRunConfiguration().getRunconfiguration().getGenericDevice());
							}
						}
						Hibernate.initialize(dl.getRunConfiguration());
						Hibernate.initialize(dl.getRunConfiguration().getWorkpackage());
					}
					
			}
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return dl;
	}

	@Override
	@Transactional
	public void deleteWorkpackageTestSuiteExecutionPlan(
			WorkPackageTestSuiteExecutionPlan wptsp) {
		try {
			sessionFactory.getCurrentSession().delete(wptsp);
			log.debug("deletion successful");
		} catch (RuntimeException re) {
			log.error("deletion failed", re);
		}		
		
	}

	@Override
	public int getTotalRecordWPFEP(Integer workpackageId,int status) {
		List<WorkPackageFeatureExecutionPlan> wpExecutionPlans = null;
		int size=0;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackageFeatureExecutionPlan.class, "wptcep");
			
		
			c.createAlias("wptcep.workPackage", "wp");
			c.add(Restrictions.eq("wp.workPackageId",workpackageId));
			if(status!=2)
				c.add(Restrictions.eq("wptcep.status",status));
			
			wpExecutionPlans=c.list();			

			if(wpExecutionPlans!=null && !wpExecutionPlans.isEmpty()){
				size=wpExecutionPlans.size();
			}
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return size;	
	}
	
	@Override
	@Transactional
	public int getTotalRecordWPFEPCount(Integer workpackageId,int status) {		
		Integer WPFEPCount = 0;		
		String sql = "";
		try {
			if(status!=2){
				 sql="select count(*) from workpackage_feature_execution_plan wfep where wfep.workPackageId=:wpId and wfep.status=:wfepStatus";			
				 WPFEPCount=((Number) sessionFactory.getCurrentSession().createSQLQuery(sql)
								.setParameter("wpId", workpackageId)
								.setParameter("wfepStatus", status)						
								.uniqueResult()).intValue();	
			}else{
				 sql="select count(*) from workpackage_feature_execution_plan wfep where wfep.workPackageId=:wpId";			
				 WPFEPCount=((Number) sessionFactory.getCurrentSession().createSQLQuery(sql)
								.setParameter("wpId", workpackageId)					
								.uniqueResult()).intValue();
				 }
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return WPFEPCount;	
	}

	@Override
	@Transactional
	public int getTestFactoryIdOfWorkPackage(Integer workpackageId) {		
		Integer testFactoryId = 0;		
		String sql = "";
		try {
				 sql="select tf.testFactoryId from test_factory tf inner join product_master pm on tf.testFactoryId=pm.testFactoryId"+
						 " inner join product_version_list_master pvlm on pm.productId=pvlm.productId"+
						 " inner join product_build pb on pvlm.productVersionListId=pb.productVersionId"+ 
						 " inner join workpackage wp on wp.productBuildId=pb.productBuildId and wp.workPackageId=:wpId";			
				 testFactoryId=((Number) sessionFactory.getCurrentSession().createSQLQuery(sql)
								.setParameter("wpId", workpackageId)				
								.uniqueResult()).intValue();	
			
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return testFactoryId;	
	}
	@Override
	public int getTotalRecordWPTSEP(Integer workpackageId,int status) {
		List<WorkPackageTestSuiteExecutionPlan> wpExecutionPlans = null;
		int size=0;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackageTestSuiteExecutionPlan.class, "wptcep");
			
		
			c.createAlias("wptcep.workPackage", "wp");
			c.add(Restrictions.eq("wp.workPackageId",workpackageId));
			if(status!=2)
				c.add(Restrictions.eq("wptcep.status",status));
			
			wpExecutionPlans=c.list();			

			if(wpExecutionPlans!=null && !wpExecutionPlans.isEmpty()){
				size=wpExecutionPlans.size();
			}
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return size;	
	}

	@Override
	@Transactional
	public int getTotalRecordWPTSEPCount(Integer workpackageId,int status) {
		log.info("getTotalRecordWPTSEPCount");
		int wpTSEPCount = 0;
		String sql= "";			
		try {
			if(status!=2){
				sql="select count(*) from workpackage_testsuite_execution_plan wtsep where wtsep.workPackageId=:wpId and wtsep.status=:stat";
				wpTSEPCount = ((Number)sessionFactory.getCurrentSession().createSQLQuery(sql)
						.setParameter("wpId", workpackageId)
						.setParameter("stat", status)
						.uniqueResult()).intValue();
			}else{
				sql="select count(*) from workpackage_testsuite_execution_plan wtsep where wtsep.workPackageId=:wpId";
				wpTSEPCount = ((Number)sessionFactory.getCurrentSession().createSQLQuery(sql)
						.setParameter("wpId", workpackageId)
						.uniqueResult()).intValue();
			}			
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return wpTSEPCount;	
	}
	
	@Override
	@Transactional
	public List<WorkPackageTestSuiteExecutionPlan> listWorkPackageTestSuiteExecutionPlan(Map<String, String> searchString,Integer workPackageId, Integer jtStartIndex, Integer jtPageSize,int status) {
			List<WorkPackageTestSuiteExecutionPlan> workPackageTestSuiteExecutionPlan = null;
		
			try {
				Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackageTestSuiteExecutionPlan.class, "wpfep");
				c.createAlias("wpfep.workPackage", "workPackage");
				
				
				if (workPackageId !=0) {
					c.add(Restrictions.eq("workPackage.workPackageId", workPackageId));
				} 
				
				if(searchString.get("searchTestSuiteName") != null && searchString.get("searchTestSuiteName") !=""){
					c.createAlias("wpfep.testsuite", "testsuite");
					c.add(Restrictions.ilike("testsuite.testSuiteName", searchString.get("searchTestSuiteName"),MatchMode.ANYWHERE));
				}
				if(searchString.get("searchTestSuiteDescription") != null && searchString.get("searchTestSuiteDescription") !=""){
					c.createAlias("wpfep.testsuite", "testsuite");
					c.add(Restrictions.ilike("testsuite.description", searchString.get("searchTestSuiteDescription"),MatchMode.ANYWHERE));
				}
				if(searchString.get("searchECName") != null || searchString.get("searchDeviceName") != null)
				{
					c.createAlias("wpfep.runConfiguration", "wrc");
					c.createAlias("wrc.runconfiguration", "rc");
				}
				if(searchString.get("searchECName") != null && searchString.get("searchECName") !=""){
					c.createAlias("rc.environmentcombination", "ec");
					c.add(Restrictions.ilike("ec.environmentCombinationName", searchString.get("searchECName"),MatchMode.ANYWHERE));
				}
				if(searchString.get("searchDeviceName") != null && searchString.get("searchDeviceName") !=""){
					c.createAlias("rc.genericDevice", "gd");
					c.add(Restrictions.ilike("gd.name", searchString.get("searchDeviceName"),MatchMode.ANYWHERE));
				}
				if(searchString.get("searchPED") !=null && !searchString.get("searchPED").equals("") && !searchString.get("searchPED").equals("undefined")){
					c.add(Restrictions.eq("wpfep.plannedExecutionDate", DateUtility.dateformatWithOutTime(searchString.get("searchPED"))));
				}
				if(searchString.get("searchPlannedShift") != null && searchString.get("searchPlannedShift") !=""){
					c.createAlias("wpfep.plannedWorkShiftMaster", "pws");
					c.add(Restrictions.ilike("pws.shiftName", searchString.get("searchPlannedShift"),MatchMode.ANYWHERE));
				}
				if(searchString.get("searchTestLeadName") != null && searchString.get("searchTestLeadName") !=""){
					c.createAlias("wpfep.testLead", "tl");
					c.add(Restrictions.ilike("tl.loginId", searchString.get("searchTestLeadName"),MatchMode.ANYWHERE));
				}
				if(searchString.get("searchTesterName") != null && searchString.get("searchTesterName") !=""){
					c.createAlias("wpfep.tester", "t");
					c.add(Restrictions.ilike("t.loginId", searchString.get("searchTesterName"),MatchMode.ANYWHERE));
				}
				
				if (status !=2) {
					c.add(Restrictions.eq("status", status));
				} 
				workPackageTestSuiteExecutionPlan = c.setFirstResult(jtStartIndex).setMaxResults(jtPageSize).list();

				
				if (!(workPackageTestSuiteExecutionPlan == null || workPackageTestSuiteExecutionPlan.isEmpty())) {
					for (WorkPackageTestSuiteExecutionPlan dl : workPackageTestSuiteExecutionPlan) {
						Hibernate.initialize(dl.getTestsuite());
						Hibernate.initialize(dl.getHostList());
						Hibernate.initialize(dl.getWorkPackage());
						Hibernate.initialize(dl.getTester());
						Hibernate.initialize(dl.getTestLead());
						Hibernate.initialize(dl.getActualWorkShiftMaster());
						Hibernate.initialize(dl.getPlannedWorkShiftMaster());
						Hibernate.initialize(dl.getWorkPackage().getProductBuild().getProductVersion());
						Hibernate.initialize(dl.getWorkPackage().getProductBuild().getProductVersion().getProductMaster());
						Hibernate.initialize(dl.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductMode());
						if(dl.getRunConfiguration()!=null){
							if(dl.getRunConfiguration().getRunconfiguration()!=null){
								Hibernate.initialize(dl.getRunConfiguration().getRunconfiguration());
								if(dl.getRunConfiguration().getRunconfiguration().getEnvironmentcombination()!=null)
									Hibernate.initialize(dl.getRunConfiguration().getRunconfiguration().getEnvironmentcombination());
								if(dl.getRunConfiguration().getRunconfiguration().getGenericDevice()!=null)
									Hibernate.initialize(dl.getRunConfiguration().getRunconfiguration().getGenericDevice());
							}
							Hibernate.initialize(dl.getRunConfiguration().getWorkpackage());
							Hibernate.initialize(dl.getRunConfiguration());
						}
					}
				}
			} catch (RuntimeException re) {
				log.error("list failed", re);
			}
			return workPackageTestSuiteExecutionPlan;

	}

	@Override
	@Transactional
	public List<JsonWorkPackageTestSuiteExecutionPlan> listJsonWorkPackageTestSuiteExecutionPlan(Map<String, String> searchString,Integer workPackageId, Integer jtStartIndex, Integer jtPageSize,int status) {
		log.info("listing listJsonWorkPackageTestSuiteExecutionPlan instance");
			List<WorkPackageTestSuiteExecutionPlan> workPackageTestSuiteExecutionPlan = null;
			List<JsonWorkPackageTestSuiteExecutionPlan> jsonWorkPackageTestSuiteExecutionPlan=new ArrayList<JsonWorkPackageTestSuiteExecutionPlan>();
			try {
				Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackageTestSuiteExecutionPlan.class, "wpfep");
				c.createAlias("wpfep.workPackage", "workPackage");				
				
				if (workPackageId !=0) {
					c.add(Restrictions.eq("workPackage.workPackageId", workPackageId));
				} 
				
				if(searchString.get("searchTestSuiteName") != null && searchString.get("searchTestSuiteName") !=""){
					c.createAlias("wpfep.testsuite", "testsuite");
					c.add(Restrictions.ilike("testsuite.testSuiteName", searchString.get("searchTestSuiteName"),MatchMode.ANYWHERE));
				}
				if(searchString.get("searchTestSuiteDescription") != null && searchString.get("searchTestSuiteDescription") !=""){
					c.createAlias("wpfep.testsuite", "testsuite");
					c.add(Restrictions.ilike("testsuite.description", searchString.get("searchTestSuiteDescription"),MatchMode.ANYWHERE));
				}
				if(searchString.get("searchECName") != null || searchString.get("searchDeviceName") != null)
				{
					c.createAlias("wpfep.runConfiguration", "wrc");
					c.createAlias("wrc.runconfiguration", "rc");
				}
				if(searchString.get("searchECName") != null && searchString.get("searchECName") !=""){
					c.createAlias("rc.environmentcombination", "ec");
					c.add(Restrictions.ilike("ec.environmentCombinationName", searchString.get("searchECName"),MatchMode.ANYWHERE));
				}
				if(searchString.get("searchDeviceName") != null && searchString.get("searchDeviceName") !=""){
					c.createAlias("rc.genericDevice", "gd");
					c.add(Restrictions.ilike("gd.name", searchString.get("searchDeviceName"),MatchMode.ANYWHERE));
				}
				if(searchString.get("searchPED") !=null && !searchString.get("searchPED").equals("") && !searchString.get("searchPED").equals("undefined")){
					c.add(Restrictions.eq("wpfep.plannedExecutionDate", DateUtility.dateformatWithOutTime(searchString.get("searchPED"))));
				}
				if(searchString.get("searchPlannedShift") != null && searchString.get("searchPlannedShift") !=""){
					c.createAlias("wpfep.plannedWorkShiftMaster", "pws");
					c.add(Restrictions.ilike("pws.shiftName", searchString.get("searchPlannedShift"),MatchMode.ANYWHERE));
				}
				if(searchString.get("searchTestLeadName") != null && searchString.get("searchTestLeadName") !=""){
					c.createAlias("wpfep.testLead", "tl");
					c.add(Restrictions.ilike("tl.loginId", searchString.get("searchTestLeadName"),MatchMode.ANYWHERE));
				}
				if(searchString.get("searchTesterName") != null && searchString.get("searchTesterName") !=""){
					c.createAlias("wpfep.tester", "t");
					c.add(Restrictions.ilike("t.loginId", searchString.get("searchTesterName"),MatchMode.ANYWHERE));
				}
				
				if (status !=2) {
					c.add(Restrictions.eq("status", status));
				} 
				workPackageTestSuiteExecutionPlan = c.setFirstResult(jtStartIndex).setMaxResults(jtPageSize).list();
				
				if (!(workPackageTestSuiteExecutionPlan == null || workPackageTestSuiteExecutionPlan.isEmpty())) {
					for (WorkPackageTestSuiteExecutionPlan dl : workPackageTestSuiteExecutionPlan) {
						Hibernate.initialize(dl.getTestsuite());
						Hibernate.initialize(dl.getHostList());
						Hibernate.initialize(dl.getWorkPackage());
						Hibernate.initialize(dl.getTester());
						Hibernate.initialize(dl.getTestLead());
						Hibernate.initialize(dl.getActualWorkShiftMaster());
						Hibernate.initialize(dl.getPlannedWorkShiftMaster());
						Hibernate.initialize(dl.getWorkPackage().getProductBuild().getProductVersion());
						Hibernate.initialize(dl.getWorkPackage().getProductBuild().getProductVersion().getProductMaster());
						Hibernate.initialize(dl.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductMode());
						if(dl.getRunConfiguration()!=null){
							if(dl.getRunConfiguration().getRunconfiguration()!=null){
								Hibernate.initialize(dl.getRunConfiguration().getRunconfiguration());
								if(dl.getRunConfiguration().getRunconfiguration().getEnvironmentcombination()!=null)
									Hibernate.initialize(dl.getRunConfiguration().getRunconfiguration().getEnvironmentcombination());
								if(dl.getRunConfiguration().getRunconfiguration().getGenericDevice()!=null)
									Hibernate.initialize(dl.getRunConfiguration().getRunconfiguration().getGenericDevice());
							}
							Hibernate.initialize(dl.getRunConfiguration().getWorkpackage());
							Hibernate.initialize(dl.getRunConfiguration());
						}
						jsonWorkPackageTestSuiteExecutionPlan.add(new JsonWorkPackageTestSuiteExecutionPlan(dl));
					}
				}
			} catch (RuntimeException re) {
				log.error("list failed", re);
			}
			return jsonWorkPackageTestSuiteExecutionPlan;
	}

	@Override
	@Transactional
	public void updateWorkPackageTestSuiteExecutionPlan(
			WorkPackageTestSuiteExecutionPlan workPackageTestSuiteExecutionPlanFromUI) {
		log.debug("updating WorkPackageTestSuiteExecutionPlan instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(workPackageTestSuiteExecutionPlanFromUI);
			
			log.debug("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
		}
	
	}

	@Override
	@Transactional
	public WorkPackageTestSuiteExecutionPlan getWorkpackageTestSuiteExecutionPlanById(
			Integer rowId) {
		log.debug("listing getWorkpackageTestcaseExecutionPlanById instance");
		List<WorkPackageTestSuiteExecutionPlan> list = null;
		WorkPackageTestSuiteExecutionPlan dl =null;
		try {
			list = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from WorkPackageTestSuiteExecutionPlan where id = "
									+ rowId).list();
			dl = (list != null && list.size() != 0) ? (WorkPackageTestSuiteExecutionPlan)(list.get(0)): null;				
			if(dl!=null){
					Hibernate.initialize(dl.getWorkPackage().getProductBuild().getProductVersion().getProductMaster());
					Hibernate.initialize(dl.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductMode());
					Hibernate.initialize(dl.getWorkPackage());
					Hibernate.initialize(dl.getWorkPackage().getWorkFlowEvent());
					Hibernate.initialize(dl.getWorkPackage().getWorkFlowEvent().getWorkFlow());
					Hibernate.initialize(dl.getTester());
					Hibernate.initialize(dl.getTestsuite());
					Hibernate.initialize(dl.getTestLead());
					Hibernate.initialize(dl.getActualWorkShiftMaster());
					Hibernate.initialize(dl.getPlannedWorkShiftMaster());
					Hibernate.initialize(dl.getHostList());
					if(dl.getRunConfiguration()!=null){
						if(dl.getRunConfiguration().getRunconfiguration()!=null){ 
							Hibernate.initialize(dl.getRunConfiguration().getRunconfiguration());
							if(dl.getRunConfiguration().getRunconfiguration()!=null){
								if(dl.getRunConfiguration().getRunconfiguration().getEnvironmentcombination()!=null)
									Hibernate.initialize(dl.getRunConfiguration().getRunconfiguration().getEnvironmentcombination());
								if(dl.getRunConfiguration().getRunconfiguration().getGenericDevice()!=null)
									Hibernate.initialize(dl.getRunConfiguration().getRunconfiguration().getGenericDevice());
							}
						}
						Hibernate.initialize(dl.getRunConfiguration());
						Hibernate.initialize(dl.getRunConfiguration().getWorkpackage());
					}
					
			}
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return dl;
	
	}

	@Override
	@Transactional
	public WorkPackageTestCaseExecutionPlanStatusDTO listWorkpackageFeatureExecutionPlanningStatus(
			Integer workPackageId) {
		WorkPackageTestCaseExecutionPlanStatusDTO workPackageTestCaseExecutionPlanStatus = new WorkPackageTestCaseExecutionPlanStatusDTO();
		int activeTestcaseCount=0;
		int inActiveTestcaseCount=0;
		int totalTestcaseCount=0;
		int testLeadCount=0;
		int testerCount=0;
		int plannedexecutionDateCount=0;
		try {
			List<WorkPackageFeatureExecutionPlan> workPackageFeatureExecutionPlans= getWorkPackageFeatureExecutionPlanByWorkpackgeId(workPackageId);
			List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlans = getWorkPackageTestcaseExecutionPlanByWorkPackageId(workPackageId, -1, -1, -1, "Feature");
			if(workPackageTestCaseExecutionPlans!=null && !workPackageTestCaseExecutionPlans.isEmpty()){
				for(WorkPackageTestCaseExecutionPlan dl:workPackageTestCaseExecutionPlans){
					if(dl.getPlannedExecutionDate()!=null && dl.getStatus() !=null && dl.getStatus().equals(1))
						plannedexecutionDateCount++;
					if(dl.getTester()!=null && dl.getStatus() !=null && dl.getStatus().equals(1))
						testerCount++;
					if(dl.getTestLead()!=null && dl.getStatus() !=null &&  dl.getStatus().equals(1))
						testLeadCount++;
					if(dl.getStatus() !=null && dl.getStatus().equals(1))
						activeTestcaseCount++;
					if(dl.getStatus() !=null && dl.getStatus().equals(0))
						inActiveTestcaseCount++;
					if(dl.getStatus() !=null && (dl.getStatus().equals(0) || dl.getStatus().equals(1)))
						totalTestcaseCount++;
				}
			}
			if(workPackageFeatureExecutionPlans!=null && !workPackageFeatureExecutionPlans.isEmpty())
				workPackageTestCaseExecutionPlanStatus.setTotalFeatureCount(workPackageFeatureExecutionPlans.size());
			else
				workPackageTestCaseExecutionPlanStatus.setTotalFeatureCount(0);
			workPackageTestCaseExecutionPlanStatus.setAssignedTesterCount(testerCount);
			workPackageTestCaseExecutionPlanStatus.setAssignedTestLeadCount(testLeadCount);
			workPackageTestCaseExecutionPlanStatus.setActiveTestCaseCount(activeTestcaseCount);
			workPackageTestCaseExecutionPlanStatus.setInActiveTestCaseCount(inActiveTestcaseCount);
			workPackageTestCaseExecutionPlanStatus.setTotalTestCaseCount(totalTestcaseCount);
			workPackageTestCaseExecutionPlanStatus.setPlannedExecutionDateCount(plannedexecutionDateCount);
			workPackageTestCaseExecutionPlanStatus.setWorkPackageId(workPackageId);
			workPackageTestCaseExecutionPlanStatus.setWorkPackageName(getWorkPackageById(workPackageId).getName());
			
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return workPackageTestCaseExecutionPlanStatus;
	}

	@Override
	@Transactional
	public List<WorkPackageFeatureExecutionPlan> getWorkPackageFeatureExecutionPlanByWorkpackgeId(
			Integer workPackageId) {
		log.debug("listing getWorkpackageTestcaseExecutionPlanById instance");
		List<WorkPackageFeatureExecutionPlan> list = null;
		try {
			list = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from WorkPackageFeatureExecutionPlan where workPackage.workPackageId = "
									+ workPackageId).list();
			for(WorkPackageFeatureExecutionPlan dl:list){			
			
					Hibernate.initialize(dl.getFeature());
					if(dl.getWorkPackage() != null){
						Hibernate.initialize(dl.getWorkPackage());
						if(dl.getWorkPackage().getProductBuild() != null){
							Hibernate.initialize(dl.getWorkPackage().getProductBuild());
							if(dl.getWorkPackage().getProductBuild().getProductVersion() != null){
								Hibernate.initialize(dl.getWorkPackage().getProductBuild().getProductVersion());
							}
						}
					}
					Hibernate.initialize(dl.getWorkPackage().getProductBuild().getProductVersion().getProductMaster());
					Hibernate.initialize(dl.getTester());
					Hibernate.initialize(dl.getTestLead());
				
					Hibernate.initialize(dl.getRunConfiguration());
					Hibernate.initialize(dl.getRunConfiguration().getRunconfiguration());
					Hibernate.initialize(dl.getRunConfiguration().getWorkpackage());
			}

		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return list;
	}

	@Override
	@Transactional
	public WorkPackageTestCaseExecutionPlanStatusDTO listWorkpackageTestSuiteExecutionPlanningStatus(
			Integer workPackageId) {
		WorkPackageTestCaseExecutionPlanStatusDTO workPackageTestCaseExecutionPlanStatus = new WorkPackageTestCaseExecutionPlanStatusDTO();
		int activeTestcaseCount=0;
		int inActiveTestcaseCount=0;
		int totalTestcaseCount=0;
		int testLeadCount=0;
		int testerCount=0;
		int plannedexecutionDateCount=0;
		try {
			List<WorkPackageTestSuiteExecutionPlan> workPackageTestSuiteExecutionPlans= getWorkPackageTestSuiteExecutionPlanByWorkpackgeId(workPackageId);
			List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlans = getWorkPackageTestcaseExecutionPlanByWorkPackageId(workPackageId, -1, -1, -1, "TestSuite");
			if(workPackageTestCaseExecutionPlans!=null && !workPackageTestCaseExecutionPlans.isEmpty()){
				for(WorkPackageTestCaseExecutionPlan dl:workPackageTestCaseExecutionPlans){
					if(dl.getPlannedExecutionDate()!=null && dl.getStatus() !=null && dl.getStatus().equals(1))
						plannedexecutionDateCount++;
					if(dl.getTester()!=null && dl.getStatus() !=null && dl.getStatus().equals(1))
						testerCount++;
					if(dl.getTestLead()!=null && dl.getStatus() !=null &&  dl.getStatus().equals(1))
						testLeadCount++;
					if(dl.getStatus() !=null && dl.getStatus().equals(1))
						activeTestcaseCount++;
					if(dl.getStatus() !=null && dl.getStatus().equals(0))
						inActiveTestcaseCount++;
					if(dl.getStatus() !=null && (dl.getStatus().equals(0) || dl.getStatus().equals(1)))
						totalTestcaseCount++;
				}
			}
			if(workPackageTestSuiteExecutionPlans!=null && !workPackageTestSuiteExecutionPlans.isEmpty())
				workPackageTestCaseExecutionPlanStatus.setTotalTestSuiteCount(workPackageTestSuiteExecutionPlans.size());
			else
				workPackageTestCaseExecutionPlanStatus.setTotalTestSuiteCount(0);
			workPackageTestCaseExecutionPlanStatus.setAssignedTesterCount(testerCount);
			workPackageTestCaseExecutionPlanStatus.setAssignedTestLeadCount(testLeadCount);
			workPackageTestCaseExecutionPlanStatus.setActiveTestCaseCount(activeTestcaseCount);
			workPackageTestCaseExecutionPlanStatus.setInActiveTestCaseCount(inActiveTestcaseCount);
			workPackageTestCaseExecutionPlanStatus.setTotalTestCaseCount(totalTestcaseCount);
			workPackageTestCaseExecutionPlanStatus.setPlannedExecutionDateCount(plannedexecutionDateCount);
			workPackageTestCaseExecutionPlanStatus.setWorkPackageId(workPackageId);
			workPackageTestCaseExecutionPlanStatus.setWorkPackageName(getWorkPackageById(workPackageId).getName());
			
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return workPackageTestCaseExecutionPlanStatus;
	}

	@Override
	@Transactional
	public List<WorkPackageTestSuiteExecutionPlan> getWorkPackageTestSuiteExecutionPlanByWorkpackgeId(Integer workPackageId) {
		log.debug("listing WorkPackageTestSuiteExecutionPlan instance");
		List<WorkPackageTestSuiteExecutionPlan> list = null;
		try {
			list = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from WorkPackageTestSuiteExecutionPlan where workPackage.workPackageId = "
									+ workPackageId).list();
			for(WorkPackageTestSuiteExecutionPlan dl:list){			
			
					Hibernate.initialize(dl.getTestsuite());
					if(dl.getWorkPackage() != null){
						Hibernate.initialize(dl.getWorkPackage());
						if(dl.getWorkPackage().getProductBuild() != null){
							Hibernate.initialize(dl.getWorkPackage().getProductBuild());
							if(dl.getWorkPackage().getProductBuild().getProductVersion() != null){
								Hibernate.initialize(dl.getWorkPackage().getProductBuild().getProductVersion());
							}
						}
					}
					Hibernate.initialize(dl.getWorkPackage().getProductBuild().getProductVersion().getProductMaster());
					Hibernate.initialize(dl.getTester());
					Hibernate.initialize(dl.getTestLead());
				
					Hibernate.initialize(dl.getRunConfiguration());
					Hibernate.initialize(dl.getRunConfiguration().getRunconfiguration());
					Hibernate.initialize(dl.getRunConfiguration().getWorkpackage());
			}

		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return list;
	}

	@Override
	@Transactional
	public List<WorkPackageTestSuite> listWorkPackageTestSuite(
			Integer workPackageId) {
		log.debug("listing WorkPackageTestSuite instance");
		List<WorkPackageTestSuite> workPackageTestSuites = null;
		try {
			workPackageTestSuites = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from WorkPackageTestSuite wptc where wptc.workPackage.workPackageId = "
									+ workPackageId
									+ " order by wptc.testSuite.testSuiteId asc")
					.list();
			if (!(workPackageTestSuites == null || workPackageTestSuites
					.isEmpty())) {
				for (WorkPackageTestSuite dl : workPackageTestSuites) {
					Hibernate.initialize(dl.getTestSuite().getProductMaster());
					Hibernate.initialize(dl.getWorkPackage().getProductBuild().getProductVersion().getProductMaster());
					Hibernate.initialize(dl.getTestSuite().getTestCaseLists());
				}
			}
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return workPackageTestSuites;
	}

	@Override
	@Transactional
	public List<WorkPackageFeature> listWorkPackageFeature(Integer workPackageId) {
		log.debug("listing WorkPackageFeature instance");
		List<WorkPackageFeature> workPackageFeatures = null;
		try {
			workPackageFeatures = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from WorkPackageFeature wptc where wptc.workPackage.workPackageId = "
									+ workPackageId
									+ " order by wptc.feature.productFeatureId asc")
					.list();
			if (!(workPackageFeatures == null || workPackageFeatures
					.isEmpty())) {
				for (WorkPackageFeature dl : workPackageFeatures) {
					Hibernate.initialize(dl.getFeature().getProductMaster());
					Hibernate.initialize(dl.getWorkPackage().getProductBuild().getProductVersion().getProductMaster());
					Hibernate.initialize(dl.getFeature());
				}
			}
			log.debug("list successful"+workPackageFeatures.size());
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return workPackageFeatures;
	}
	@Override
	@Transactional
	public List<WorkPackageTestCaseExecutionPlan> listWorkPackageTestCasesExecutionPlanBywpId(int workpackageId,String filter) {
		
		List<WorkPackageTestCaseExecutionPlan> wpTcPlanList = null;
		
		try {
			
			if(filter.equals("TestSuiteTestCase")){
				Query query = sessionFactory.getCurrentSession().createSQLQuery(
						"select * from workpackage_testcase_execution_plan wptcPlan where wptcPlan.workPackageId = :workPackageId AND  wptcPlan.featureId IS NULL ORDER BY wptcPlan.testsuiteId ASC,wptcPlan.testcaseId ASC ")
						.addEntity(WorkPackageTestCaseExecutionPlan.class)
						.setParameter("workPackageId",workpackageId);
				wpTcPlanList = query.list();
			}else{
				
				Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackageTestCaseExecutionPlan.class, "wpfep");
					c.createAlias("wpfep.workPackage", "workPackage");
					c.createAlias("wpfep.feature", "feature");
					
							if (workpackageId !=-1) {
								c.add(Restrictions.eq("workPackage.workPackageId", workpackageId));
							} 
							if(filter.equals("ProductFeature")){
								c.createAlias("wpfep.testCase", "testCase");
								c.createAlias("wpfep.runConfiguration", "wprunConfiguration");
								c.createAlias("wprunConfiguration.runconfiguration", "runconfiguration");
								//c.add(Restrictions.eq("wpfep.sourceType", "Feature"));
								c.addOrder(Order.asc("feature.productFeatureId"));
								c.addOrder(Order.asc("testCase.testCaseId"));
								c.addOrder(Order.asc("runconfiguration.runconfigId"));
							}
							else if(filter.equals("ProductFeaturePrioritieswise")){
								c.createAlias("wpfep.executionPriority", "execuitionPriority");
								//c.add(Restrictions.eq("wpfep.sourceType", "Feature"));
								c.addOrder(Order.asc("feature.productFeatureId"));
								c.addOrder(Order.asc("execuitionPriority.executionPriorityId"));
								}
							
							wpTcPlanList=c.list();
							
				
			}
			
					
			
			if(wpTcPlanList!=null &&! wpTcPlanList.isEmpty()){
				for(WorkPackageTestCaseExecutionPlan wptcep:wpTcPlanList){
					if(wptcep.getTestCase()!=null){
						Hibernate.initialize(wptcep.getTestCase());
						if(wptcep.getTestCase().getTestCasePriority()!=null){
							Hibernate.initialize(wptcep.getTestCase().getTestCasePriority());
						}
						if(wptcep.getTestCase().getExecutionTypeMaster()!=null){
							Hibernate.initialize(wptcep.getTestCase().getExecutionTypeMaster());
						}
					}
					
					if(wptcep.getFeature()!=null){
						Hibernate.initialize(wptcep.getFeature());
					}
					if(wptcep.getTestSuiteList()!=null){
						Hibernate.initialize(wptcep.getTestSuiteList());
					}
					if(wptcep.getRunConfiguration()!=null){
						Hibernate.initialize(wptcep.getRunConfiguration());
						Hibernate.initialize(wptcep.getRunConfiguration().getRunconfiguration());
					}
					if(wptcep.getExecutionPriority()!=null){
						Hibernate.initialize(wptcep.getExecutionPriority());
					}
					if(wptcep.getTestCaseExecutionResult()!=null){
						Hibernate.initialize(wptcep.getTestCaseExecutionResult());
					}
					if(wptcep.getExecutionPriority()!=null){
						Hibernate.initialize(wptcep.getExecutionPriority());
					}
					if(wptcep.getWorkPackage()!=null){
						Hibernate.initialize(wptcep.getWorkPackage());
						if(wptcep.getWorkPackage().getTestRunJobSet().size()!=0){
							Hibernate.initialize(wptcep.getWorkPackage().getTestRunJobSet());
						}
					}
					
					
				}
			}
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return wpTcPlanList;	
	}
	
	@Override
	@Transactional
	public WorkPackageFeature getWorkpackageFeaturePlanById(Integer tsWpId) {
		log.debug("getting WorkPackageFeature instance by id");
		WorkPackageFeature workPackageFeature = null;
		try {

			List list = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from WorkPackageFeature w where workpackageFeatureId=:workpackageFeatureId")
					.setParameter("workpackageFeatureId", tsWpId).list();
			workPackageFeature = (list != null && list.size() != 0) ? (WorkPackageFeature) list
					.get(0) : null;
			if (workPackageFeature != null) {
				Hibernate.initialize(workPackageFeature.getWorkPackage());
				Hibernate.initialize(workPackageFeature.getFeature());
				Hibernate.initialize(workPackageFeature.getFeature().getTestCaseList());
			}
			log.debug("getByProductId successful");
		} catch (RuntimeException re) {
			log.error("getByProductId failed", re);
		}
		return workPackageFeature;

	}

	@Override
	@Transactional
	public WorkPackageTestCase getWorkpackageTestCaseByPlanId(Integer tcWpId) {
		log.debug("getting WorkPackageTestCase instance by id");
		WorkPackageTestCase workPackageTestCase = null;
		try {

			List list = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from WorkPackageTestCase w where id=:tcWpId")
					.setParameter("tcWpId", tcWpId).list();
			workPackageTestCase = (list != null && list.size() != 0) ? (WorkPackageTestCase) list
					.get(0) : null;
			if (workPackageTestCase != null) {
				Hibernate.initialize(workPackageTestCase.getWorkPackage());
				Hibernate.initialize(workPackageTestCase.getTestCase());
				Hibernate.initialize(workPackageTestCase.getTestCase().getDecouplingCategory().size());
			}
			log.debug("getByProductId successful");
		} catch (RuntimeException re) {
			log.error("getByProductId failed", re);
		}
		return workPackageTestCase;

	}

	@Override
	@Transactional
	public WorkPackageTestSuite getWorkpackageTestSuiteByPlanId(Integer tsWpId) {
		log.debug("getting WorkPackageTestCase instance by id");
		WorkPackageTestSuite workPackageTestSuite = null;
		try {

			List list = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from WorkPackageTestSuite w where workpackageTestSuiteId=:tsWpId")
					.setParameter("tsWpId", tsWpId).list();
			workPackageTestSuite = (list != null && list.size() != 0) ? (WorkPackageTestSuite) list
					.get(0) : null;
			if (workPackageTestSuite != null) {
				Hibernate.initialize(workPackageTestSuite.getWorkPackage());
				Hibernate.initialize(workPackageTestSuite.getTestSuite());
			}
			log.debug("getByProductId successful");
		} catch (RuntimeException re) {
			log.error("getByProductId failed", re);
		}
		return workPackageTestSuite;

	}

	@Override
	@Transactional
	public List<WorkPackageExecutionPlanUserDetails> listWorkpackageExecutionPlanUserDetails(
			Integer workPackageId,String plannedExecutionDate,String role) {
		List<WorkPackageExecutionPlanUserDetails> workPackageExecutionPlanUserDetailList = new ArrayList<WorkPackageExecutionPlanUserDetails>();
		try{
		Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackageTestCaseExecutionPlan.class, "wptcep");
		if (workPackageId !=0 ) {
			c.createAlias("wptcep.workPackage", "workPackage");

			c.add(Restrictions.eq("workPackage.workPackageId", workPackageId));
		} 
		c.add(Restrictions.eq("wptcep.status", 1));

		ProjectionList projectionList = Projections.projectionList();
		projectionList = Projections.projectionList();
		if(role.equalsIgnoreCase("Tester")){
			c.createAlias("wptcep.tester", "tester");
			projectionList.add(Property.forName("wptcep.plannedExecutionDate").as("plannedExecutionDate"));
			projectionList.add(Property.forName("tester.loginId").as("loginId"));
			projectionList.add(Projections.count("tester.loginId"));
			projectionList.add(Projections.groupProperty("wptcep.plannedExecutionDate"));
			projectionList.add(Projections.groupProperty("tester.loginId"));
			
		}else if(role.equalsIgnoreCase("TestLead")){
			c.createAlias("wptcep.testLead", "testLead");
			projectionList.add(Property.forName("wptcep.plannedExecutionDate").as("plannedExecutionDate"));
			projectionList.add(Property.forName("testLead.loginId").as("loginId"));
			projectionList.add(Projections.count("testLead.loginId"));
			projectionList.add(Projections.groupProperty("wptcep.plannedExecutionDate"));
			projectionList.add(Projections.groupProperty("testLead.loginId"));
			
		}
		
		c.setProjection(projectionList);
		c.addOrder(Order.asc("wptcep.plannedExecutionDate"));

		List<Object[]> list = c.list();

		WorkPackageExecutionPlanUserDetails workPackageExecutionPlanUserDetails = null;
	
		for (Object[] row : list) {
				workPackageExecutionPlanUserDetails = new WorkPackageExecutionPlanUserDetails();
				workPackageExecutionPlanUserDetails.setPlannedExecutionDate((Date)row[0]);
				workPackageExecutionPlanUserDetails.setLoginId((String)row[1]);
				workPackageExecutionPlanUserDetails.setTotalAllocatedCount(((Long)row[2]).intValue());
				workPackageExecutionPlanUserDetailList.add(workPackageExecutionPlanUserDetails);
				
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return workPackageExecutionPlanUserDetailList;
	}

	@Override
	@Transactional
	public List<WorkPackageTestCaseExecutionPlan> listWorkPackageTestCasesExecutionPlan(
			Integer workPackageId, UserList user, String plannedExecutionDate,String filter,WorkpackageRunConfiguration wpRunConfigObj) {
				List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlan = null;
				try {
					
					
					Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackageTestCaseExecutionPlan.class, "wptcep");
					c.createAlias("wptcep.testCase", "testCase");
					c.createAlias("wptcep.tester", "tester");
					c.createAlias("wptcep.executionPriority", "executionPriority");
					if(workPackageId!=0){
						c.createAlias("wptcep.workPackage", "workPackage");
						c.add(Restrictions.eq("workPackage.workPackageId", workPackageId));
					}
					c.add(Restrictions.eq("tester.userId", user.getUserId()));
					
					if(plannedExecutionDate!=null && !plannedExecutionDate.equals("") && !plannedExecutionDate.equals("undefined") && !plannedExecutionDate.equals("Planned Execution Date")){
						c.add(Restrictions.eq("wptcep.plannedExecutionDate", DateUtility.dateformatWithOutTime(plannedExecutionDate)));
					} 
					
					if(filter!=null){
					if(filter.equals("1")){
					
						c.createAlias("wptcep.feature", "feature");
					c.add(Restrictions.eq("wptcep.sourceType", "Feature"));
					c.addOrder(Order.asc("feature.productFeatureId"));
					}
					if(filter.equals("2")){
						c.createAlias("wptcep.testSuiteList", "testsuite");
						c.add(Restrictions.eq("wptcep.sourceType", "TestSuite"));
						c.addOrder(Order.asc("testsuite.testSuiteId"));
						}
				if(filter.equals("0")){
					c.createAlias("wptcep.runConfiguration", "wpRunConfig");
				c.createAlias("wpRunConfig.runconfiguration", "runconfig");
				
				if(wpRunConfigObj!=null){
					
				
					c.add(Restrictions.eq("wpRunConfig.workpackageRunConfigurationId", wpRunConfigObj.getWorkpackageRunConfigurationId()));
					if(wpRunConfigObj.getType().equalsIgnoreCase("feature")){
						
						c.createAlias("wptcep.feature", "feature");
						c.addOrder(Order.asc("feature.productFeatureId"));
					}else if(wpRunConfigObj.getType().equalsIgnoreCase("testsuite")){
						c.createAlias("wptcep.testSuiteList", "testsuite");
						c.addOrder(Order.asc("testsuite.testSuiteId"));
					}
				}
						
						}
					}
			
				    c.addOrder(Order.asc("executionPriority.executionPriorityId"));
					c.addOrder(Order.asc("testCase.testCaseId"));
					workPackageTestCaseExecutionPlan=c.list();
					if (!(workPackageTestCaseExecutionPlan == null || workPackageTestCaseExecutionPlan
							.isEmpty())) {
						for (WorkPackageTestCaseExecutionPlan dl : workPackageTestCaseExecutionPlan) {
							Hibernate.initialize(dl.getTestCase());
							Hibernate.initialize(dl.getTestCase().getTestcaseTypeMaster());
							Hibernate.initialize(dl.getTestCase().getTestCasePriority());
							Hibernate.initialize(dl.getWorkPackage());
							if(dl.getWorkPackage() != null){
								Hibernate.initialize(dl.getWorkPackage());
								if(dl.getWorkPackage().getProductBuild() != null){
									Hibernate.initialize(dl.getWorkPackage().getProductBuild());
									if(dl.getWorkPackage().getProductBuild().getProductVersion() != null){
										Hibernate.initialize(dl.getWorkPackage().getProductBuild().getProductVersion());
										if(dl.getWorkPackage().getProductBuild().getProductVersion().getProductMaster()!=null){
											Hibernate.initialize(dl.getWorkPackage().getProductBuild().getProductVersion().getProductMaster());
										}
									}
								}
							}
							Hibernate.initialize(dl.getEnvironmentList());
							Hibernate.initialize(dl.getTester());
							Hibernate.initialize(dl.getTestLead());
							Hibernate.initialize(dl.getTestCaseExecutionResult());
							Hibernate.initialize(dl.getActualWorkShiftMaster());
							Hibernate.initialize(dl.getPlannedWorkShiftMaster());
							Hibernate.initialize(dl.getExecutionPriority());
							Hibernate.initialize(dl.getTestCase().getTestCasePriority());
							Hibernate.initialize(dl.getWorkPackage().getProductBuild().getProductVersion());
							Hibernate.initialize(dl.getWorkPackage().getProductBuild().getProductVersion().getProductMaster());
							Hibernate.initialize(dl.getTestSuiteList());
							if(dl.getRunConfiguration()!=null){
							Hibernate.initialize(dl.getRunConfiguration());
							if(dl.getRunConfiguration().getRunconfiguration()!=null){
								Hibernate.initialize(dl.getRunConfiguration().getRunconfiguration());
								if(dl.getRunConfiguration().getRunconfiguration().getEnvironmentcombination()!=null){
									Hibernate.initialize(dl.getRunConfiguration().getRunconfiguration().getEnvironmentcombination());
								}
								if(dl.getRunConfiguration().getRunconfiguration().getGenericDevice()!=null){
									Hibernate.initialize(dl.getRunConfiguration().getRunconfiguration().getGenericDevice());
								}
							}
							Hibernate.initialize(dl.getRunConfiguration().getWorkpackage());
						}
							if(dl.getFeature()!=null){
								Hibernate.initialize(dl.getFeature());
							}
						}
					}
					log.debug("list successful");
				} catch (RuntimeException re) {
					log.error("list failed", re);
				}
				return workPackageTestCaseExecutionPlan;
	}

	@Override
	@Transactional
	public List<WorkPackageTestCaseExecutionPlan> getWorkPackageTestcaseExecutionPlanByWorkPackageId(
			int workPackageId, int testcaseId, int testSuiteId, int featureId,
			String type, int runConfigId) {
		List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlans = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackageTestCaseExecutionPlan.class, "wpfep");
			
			if (workPackageId !=-1) {
				c.createAlias("wpfep.workPackage", "workPackage");
				c.add(Restrictions.eq("workPackage.workPackageId", workPackageId));
			} 
			if (featureId !=-1) {
				c.createAlias("wpfep.feature", "feature");
				c.add(Restrictions.eq("feature.productFeatureId", featureId));
			} 
			if (testcaseId !=-1) {
				c.createAlias("wpfep.testCase", "testCase");
				c.add(Restrictions.eq("testCase.testCaseId", testcaseId));
			} 
			if (testSuiteId !=-1) {
				c.createAlias("wpfep.testSuiteList", "testSuiteList");
				c.add(Restrictions.eq("testSuiteList.testSuiteId", testSuiteId));
			} 
			if (runConfigId !=-1) {
				c.createAlias("wpfep.runConfiguration", "wprc");
				c.createAlias("wprc.runconfiguration", "rc");
				c.add(Restrictions.eq("rc.runconfigId", runConfigId));
			} 
			c.add(Restrictions.eq("wpfep.sourceType", type));
			
			workPackageTestCaseExecutionPlans=c.list();
			
			
			if (!(workPackageTestCaseExecutionPlans == null || workPackageTestCaseExecutionPlans.isEmpty())) {
				for(WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan : workPackageTestCaseExecutionPlans) {
					//Commented the following statements when remove the WorkPackageTestcaseExecutionId column from Test Case Execution Result table. By: Logeswari, On :  11-Feb-2015
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getTestCase());
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getWorkPackage());
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getTestSuiteList());
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getFeature());
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getEnvironmentList());
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getTestCaseConfigurationSet());
					if(workPackageTestCaseExecutionPlan.getTestCaseExecutionResult()!=null && workPackageTestCaseExecutionPlan.getTestCaseExecutionResult().getTestStepExecutionResultSet()!=null)
						Hibernate.initialize(workPackageTestCaseExecutionPlan.getTestCaseExecutionResult().getTestStepExecutionResultSet());

					Hibernate.initialize(workPackageTestCaseExecutionPlan.getTester());
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getTestLead());
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getTestCaseExecutionResult());
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getActualWorkShiftMaster());
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getPlannedWorkShiftMaster());
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getExecutionPriority());
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getRunConfiguration());
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getTestSuiteList());
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getTestCase().getTestcaseTypeMaster());
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getTestCase().getTestCasePriority());
					if(workPackageTestCaseExecutionPlan.getWorkPackage() != null){
						Hibernate.initialize(workPackageTestCaseExecutionPlan.getWorkPackage());
						if(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild() != null){
							Hibernate.initialize(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild());
							if(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion() != null){
								Hibernate.initialize(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion());
							}
						}
					}
					if(workPackageTestCaseExecutionPlan.getRunConfiguration()!=null){
						if(workPackageTestCaseExecutionPlan.getRunConfiguration().getRunconfiguration()!=null){
							Hibernate.initialize(workPackageTestCaseExecutionPlan.getRunConfiguration().getRunconfiguration());
						}
						
						Hibernate.initialize(workPackageTestCaseExecutionPlan.getRunConfiguration().getWorkpackage());
					}
				}
			}
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
			re.printStackTrace();
		}
		return workPackageTestCaseExecutionPlans;
	
	}

	@Override
	@Transactional
	public boolean isWorkPackageExecutionAvailable(WorkPackage workPackage) {
		List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlans = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackageTestCaseExecutionPlan.class, "wpfep");
			c.createAlias("wpfep.workPackage", "workPackage");
			c.add(Restrictions.eq("workPackage.workPackageId", workPackage.getWorkPackageId()));
			c.add(Restrictions.in("wpfep.executionStatus",  Arrays.asList(1,3)));

			workPackageTestCaseExecutionPlans=c.list();
			if(workPackageTestCaseExecutionPlans!=null && !workPackageTestCaseExecutionPlans.isEmpty()){
				return true;
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}

	@Override
	@Transactional
	public WorkPackageTestSuiteExecutionPlan workPackageTestSuiteExecutionPlanByJobId(
			TestRunJob testRunJob, TestSuiteList testSuiteList) {
		List<WorkPackageTestSuiteExecutionPlan> listWorkPackageTestSuiteExecutionPlan = null;
		WorkPackageTestSuiteExecutionPlan dl=null;
		try {
			listWorkPackageTestSuiteExecutionPlan =sessionFactory
					.getCurrentSession()
					.createQuery(
							" from  WorkPackageTestSuiteExecutionPlan wptcep where wptcep.workPackage.workPackageId = "
									+testRunJob.getWorkPackage().getWorkPackageId()
									+ " and wptcep.testsuite.testSuiteId = "+testSuiteList.getTestSuiteId()+ " and wptcep.runConfiguration.runconfiguration.runconfigId = "
									+testRunJob.getRunConfiguration().getRunconfigId()+ " and wptcep.hostList.hostId = "
											+testRunJob.getHostList().getHostId() ).list();
			
			
			dl = (listWorkPackageTestSuiteExecutionPlan != null && listWorkPackageTestSuiteExecutionPlan.size() != 0) ? (WorkPackageTestSuiteExecutionPlan) listWorkPackageTestSuiteExecutionPlan
					.get(0) : null;

			if (dl!=null) {

					Hibernate.initialize(dl.getTestsuite());
					Hibernate.initialize(dl.getWorkPackage());
					Hibernate.initialize(dl.getTester());
					Hibernate.initialize(dl.getTestLead());
					Hibernate.initialize(dl.getExecutionPriority());
					
					Hibernate.initialize(dl.getActualWorkShiftMaster());
					Hibernate.initialize(dl.getPlannedWorkShiftMaster());
					Hibernate.initialize(dl.getHostList());

					if(dl.getRunConfiguration()!=null){
						Hibernate.initialize(dl.getRunConfiguration());
						Hibernate.initialize(dl.getRunConfiguration().getRunconfiguration());
						if(dl.getRunConfiguration().getRunconfiguration()!=null)
							if(dl.getRunConfiguration().getRunconfiguration().getEnvironmentcombination()!=null){
								Hibernate.initialize(dl.getRunConfiguration().getRunconfiguration().getEnvironmentcombination());
							}
						if(dl.getRunConfiguration().getRunconfiguration().getGenericDevice()!=null){
							Hibernate.initialize(dl.getRunConfiguration().getRunconfiguration().getGenericDevice());
						}
						Hibernate.initialize(dl.getRunConfiguration().getWorkpackage());
					}
			}	
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return dl;
	
	}	
	
	@Override
	@Transactional
	public List<WorkpackageRunConfiguration> getTestRunJobByWPTCEP(Integer workPackageId, Integer userId,
			String plannedExecutionDate) {
		List<WorkpackageRunConfiguration> wpackagerunconfigList=null;
	
	
		Date planExeDate=DateUtility.dateformatWithOutTime(plannedExecutionDate);
	String query=" select distinct wprunConfig from WorkPackageTestCaseExecutionPlan AS wptcep join wptcep.runConfiguration AS wprunConfig   where  (wptcep.workPackage.workPackageId=:workPackageId) and (wptcep.tester.userId=:testerUserId or wptcep.testLead.userId=:testLeadUserId) and (wptcep.plannedExecutionDate=:planExeDate) and wprunConfig.runconfiguration.runConfigStatus=1 group by wptcep.runConfiguration.workpackageRunConfigurationId order by wprunConfig.runconfiguration.runconfigId asc,FIELD( wprunConfig.type, 'feature', 'testsuite', 'testcase')";
	Query	hqlquery = sessionFactory.getCurrentSession().createQuery(query);
	hqlquery.setParameter("workPackageId", workPackageId);
	hqlquery.setParameter("testerUserId", userId);
	hqlquery.setParameter("testLeadUserId", userId);
	hqlquery.setParameter("planExeDate", planExeDate);
	
		 wpackagerunconfigList=hqlquery.list();
		 if(wpackagerunconfigList.size()!=0){
		 for(WorkpackageRunConfiguration wpRunConfig:wpackagerunconfigList){
			 
										Hibernate.initialize(wpRunConfig);
										RunConfiguration runConfig=wpRunConfig.getRunconfiguration();
										if(runConfig!=null){
											Hibernate.initialize(runConfig);
											Set<TestRunJob> testRunJobSet= runConfig.getTestRunJobSet();
											if(testRunJobSet.size()!=0){
												Hibernate.initialize(testRunJobSet);
												for(TestRunJob testRunJob:testRunJobSet){
													if(testRunJob!=null){
														RunConfiguration runConfigObj=testRunJob.getRunConfiguration();
														if(runConfigObj!=null){
															Hibernate.initialize(runConfigObj);
														}
													}
												}
											}
										}
		
	}
	}
		 return wpackagerunconfigList;
		
	}

	@Override
	@Transactional
	public List<WorkPackageTestCaseExecutionPlan> getWorkPackageTestCaseExecutionPlanByTestRunJob(
			TestRunJob testRunJob,int testSuiteId) {
		List<WorkPackageTestCaseExecutionPlan> listWorkPackageTestCaseExecutionPlan = null;
		String sourceType="TestSuite";
		try {
			/*listWorkPackageTestCaseExecutionPlan =sessionFactory
					.getCurrentSession()
					.createQuery(
							" from  WorkPackageTestCaseExecutionPlan wptcep where wptcep.workPackage.workPackageId = "
									+testRunJob.getWorkPackage().getWorkPackageId()
									+" and wptcep.runConfiguration.runconfiguration.runconfigId = "
									+testRunJob.getRunConfiguration().getRunconfigId()+ " and wptcep.testSuiteList.testSuiteId="+testSuiteId +"and wptcep.sourceType ="+sourceType).list();*/
			
			
			//Criteria query
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackageTestCaseExecutionPlan.class, "wpfep");
						c.createAlias("wpfep.workPackage", "workPackage");
						c.createAlias("wpfep.runConfiguration", "runconfig");
						c.createAlias("runconfig.runconfiguration", "rc");
						c.createAlias("wpfep.testSuiteList", "tsl");
						c.add(Restrictions.eq("workPackage.workPackageId",testRunJob.getWorkPackage().getWorkPackageId()));
						c.add(Restrictions.eq("rc.runconfigId",testRunJob.getRunConfiguration().getRunconfigId()));
						c.add(Restrictions.eq("tsl.testSuiteId",testSuiteId));
						c.add(Restrictions.eq("wpfep.sourceType", sourceType));

			listWorkPackageTestCaseExecutionPlan = c.list();
			for (WorkPackageTestCaseExecutionPlan dl : listWorkPackageTestCaseExecutionPlan) {
	
			if (dl!=null) {
					Hibernate.initialize(dl.getTestCase());
					Hibernate.initialize(dl.getWorkPackage());
					Hibernate.initialize(dl.getTester());
					Hibernate.initialize(dl.getTestLead());
					Hibernate.initialize(dl.getExecutionPriority());
					if(dl.getEnvironmentList().size()!=0){
						Hibernate.initialize(dl.getEnvironmentList());
					}
					Hibernate.initialize(dl.getActualWorkShiftMaster());
					Hibernate.initialize(dl.getPlannedWorkShiftMaster());
					Hibernate.initialize(dl.getTestCase().getDecouplingCategory());
					Hibernate.initialize(dl.getTestCaseExecutionResult());
					Hibernate.initialize(dl.getTestCaseExecutionResult().getTestStepExecutionResultSet());
					Set<TestStepExecutionResult> testStepExecutionResults=dl.getTestCaseExecutionResult().getTestStepExecutionResultSet();
					for (TestStepExecutionResult testStepExecutionResult : testStepExecutionResults) {
						Hibernate.initialize(testStepExecutionResult.getTestSteps());

					}
							
					Hibernate.initialize(dl.getTestCaseExecutionResult().getTestExecutionResultBugListSet());

					Hibernate.initialize(dl.getTestCase().getTestcaseTypeMaster());
					Hibernate.initialize(dl.getTestCase().getTestCasePriority());
					Hibernate.initialize(dl.getHostList());
					Hibernate.initialize(dl.getTestCase().getTestCasePriority());
					Hibernate.initialize(dl.getTestSuiteList());
					if(dl.getRunConfiguration()!=null){
						Hibernate.initialize(dl.getRunConfiguration());
						Hibernate.initialize(dl.getRunConfiguration().getRunconfiguration());
						if(dl.getRunConfiguration().getRunconfiguration()!=null)
							if(dl.getRunConfiguration().getRunconfiguration().getEnvironmentcombination()!=null){
								Hibernate.initialize(dl.getRunConfiguration().getRunconfiguration().getEnvironmentcombination());
							}
						if(dl.getRunConfiguration().getRunconfiguration().getGenericDevice()!=null){
							Hibernate.initialize(dl.getRunConfiguration().getRunconfiguration().getGenericDevice());
						}
						Hibernate.initialize(dl.getRunConfiguration().getWorkpackage());
					}
			}
			}	
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return listWorkPackageTestCaseExecutionPlan;
	
	}

	@Override
	@Transactional
	public List<TestExecutionResultBugList> listDefectsByWorkpackageId(
			int workpackageId) {
		List<TestExecutionResultBugList> testExecutionResultBugLists = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestExecutionResultBugList.class, "tebl");
			c.createAlias("tebl.testCaseExecutionResult", "tcer");
			c.createAlias("tcer.workPackageTestCaseExecutionPlan", "wptep");
			c.createAlias("wptep.workPackage", "wp");

			c.add(Restrictions.eq("wp.workPackageId", workpackageId));

			testExecutionResultBugLists=c.list();
			if(testExecutionResultBugLists!=null && !testExecutionResultBugLists.isEmpty()){
				for (TestExecutionResultBugList testExecutionResultBugList : testExecutionResultBugLists) {
					Hibernate.initialize(testExecutionResultBugList.getTestCaseExecutionResult());
					Hibernate.initialize(testExecutionResultBugList.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan());
					Hibernate.initialize(testExecutionResultBugList.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getWorkPackage());
					Hibernate.initialize(testExecutionResultBugList.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getWorkPackage().getProductBuild().getProductVersion().getProductMaster());
					Hibernate.initialize(testExecutionResultBugList.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getWorkPackage());
					Hibernate.initialize(testExecutionResultBugList.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getDefectManagementSystems());
					Hibernate.initialize(testExecutionResultBugList.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration());
					Hibernate.initialize(testExecutionResultBugList.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getTestCase());
					Hibernate.initialize(testExecutionResultBugList.getTestCaseExecutionResult().getTestStepExecutionResultSet());
					Hibernate.initialize(testExecutionResultBugList.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getTestCase().getTestCasePriority());
					Hibernate.initialize(testExecutionResultBugList.getTestCaseExecutionResult().getTestStepExecutionResultSet());
					Set<TestStepExecutionResult> testStepExecutionResults =testExecutionResultBugList.getTestCaseExecutionResult().getTestStepExecutionResultSet();
					for (TestStepExecutionResult testStepExecutionResult : testStepExecutionResults) {
						Hibernate.initialize(testStepExecutionResult.getTestSteps());
					}
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return testExecutionResultBugLists;
	}

	@Override
	@Transactional
	public int addWorkpackageToTestRunPlanGroup(
			TestRunPlanGroup testRunPlanGroup, Map<String, String> mapData,
			UserList user, HttpServletRequest req) {
		TestRunPlan testRunPlan=null;
		WorkPackage workPackage=null;
		List<TestRunPlangroupHasTestRunPlan> testRunPlans= productMasterDAO.listTestRunPlanGroupMap(testRunPlanGroup.getTestRunPlanGroupId());
		int count=0;
		if(testRunPlanGroup.getExecutionTypeMaster().getExecutionTypeId()==3){
			if(testRunPlans!=null && !testRunPlans.isEmpty()){
				//TestRunPlangroupHasTestRunPlan testRunPlangroupHasTestRunPlan=testRunPlans.get(0);
				testRunPlan=productMasterDAO.getFirstTestRunPlanByTestPlanGroupId(testRunPlanGroup.getTestRunPlanGroupId());
				workPackage=addWorkpackageToTestRunPlan(testRunPlan, mapData, user, req,testRunPlanGroup,null);
			}
		}
		return count;
	}

	@Override
	@Transactional
	public TestRunPlan getNextTestRunPlan(TestRunPlanGroup testRunPlanGroup,
			TestRunPlan testRunPlan) {
		List<TestRunPlangroupHasTestRunPlan> trpgmList = null;
		TestRunPlangroupHasTestRunPlan trpgm = null;
		TestRunPlan testRunPlanNext=null;
		int executionOrder=0;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestRunPlangroupHasTestRunPlan.class, "trpgm");
			c.createAlias("trpgm.testRunPlanGroup", "trpg");
			c.createAlias("trpgm.testrunplan", "trp");

			c.add(Restrictions.eq("trpg.testRunPlanGroupId", testRunPlanGroup.getTestRunPlanGroupId()));
			c.add(Restrictions.eq("trp.testRunPlanId", testRunPlan.getTestRunPlanId()));

			trpgmList=c.list();
			trpgm = (trpgmList != null && trpgmList.size() != 0) ? (TestRunPlangroupHasTestRunPlan) trpgmList.get(0) : null;
			if(trpgm!=null){
				executionOrder=trpgm.getExecutionOrder();
				c = sessionFactory.getCurrentSession().createCriteria(TestRunPlangroupHasTestRunPlan.class, "trpgm");
				c.createAlias("trpgm.testRunPlanGroup", "trpg");
				c.createAlias("trpgm.testrunplan", "trp");

				c.add(Restrictions.eq("trpg.testRunPlanGroupId", testRunPlanGroup.getTestRunPlanGroupId()));
				//c.add(Restrictions.eq("trp.testRunPlanId", testRunPlan.getTestRunPlanId()));
				c.add(Restrictions.eq("trpgm.executionOrder", executionOrder+1));
				trpgmList=c.list();
				trpgm = (trpgmList != null && trpgmList.size() != 0) ? (TestRunPlangroupHasTestRunPlan) trpgmList.get(0) : null;
				if(trpgm!=null){
					//testRunPlanNext=trpgm.getTestrunplan();
					testRunPlanNext = productMasterDAO.getTestRunPlanBytestRunPlanId(trpgm.getTestrunplan().getTestRunPlanId());
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return testRunPlanNext;

	}

	@Override
	@Transactional
	public List<Object> listDefectsByTestcaseExecutionPlanIdByApprovedStatus(Integer productId, Integer productVersionId, 
			Integer productBuildId,Integer workPackageId, Integer status, Date startDate, Date endDate,Integer raisedByUser, Integer jtStartIndex,Integer jtPageSize) {
		List<Object> testExecutionResultBugList  = new ArrayList<Object>();
		
		Criteria c = sessionFactory.getCurrentSession().createCriteria(TestExecutionResultBugList.class, "tebl");
		c.createAlias("tebl.testCaseExecutionResult", "tcer");
		c.createAlias("tcer.workPackageTestCaseExecutionPlan", "wptep");
		c.createAlias("wptep.tester", "user");
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
		
		if(status != 2){
			c.add(Restrictions.eq("tebl.isApproved", status));
		}
		if (raisedByUser != -1){
			c.add(Restrictions.eq("user.userId", raisedByUser));
		}
		
		c.add(Restrictions.between("tebl.bugCreationTime", startDate,  endDate));
		
		
		c.setProjection( Projections.projectionList()
		        .add( Projections.distinct(Projections.property("tebl.testExecutionResultBugId")))
			    );
		
		if(jtStartIndex != null && jtPageSize != null)
			c.setFirstResult(jtStartIndex).setMaxResults(jtPageSize);
		c.addOrder(Order.asc("tebl.testExecutionResultBugId"));
		testExecutionResultBugList =c.list();
			
		return testExecutionResultBugList;
	}
	@Override
	@Transactional
	public List<Object[]> listWorkPackageTestCaseExeSummaryByFilters(int workpackageId,int filterId,String wpSummary){
		List<Object[]> listFromDb=new ArrayList<Object[]>();
		String sql="";
		Query query=null;
		if(databaseDialect != null && databaseDialect.equalsIgnoreCase("org.hibernate.dialect.MySQL5Dialect")) {
			if(filterId==0){  //workpackage summary
				sql="SELECT COUNT(tcres.result) AS totalResult,tcres.result AS resultId FROM workpackage_testcase_execution_plan AS wptcPlan INNER JOIN testcase_execution_result AS tcres ON wptcPlan.wptcepId=tcres.testCaseExecutionResultId  WHERE workPackageId=:workPackageId and wptcPlan.status=1 GROUP BY (tcres.result)";
				 query = sessionFactory.getCurrentSession().createSQLQuery(sql).setParameter("workPackageId",workpackageId);
				listFromDb = query.list();
				int length=listFromDb.size();
				if(wpSummary!=null){
					if(wpSummary.equals("wpSummary")){
						sql="SELECT wp.name,wf.stageId,"
								+"(  SELECT COUNT(wphastc.workPackageId) FROM  workpackage_has_testcase wphastc WHERE wphastc.workPackageId ="+workpackageId+"  ) AS testCaseCount,  "    
								+"(  SELECT COUNT(wphasTestSuite.testSuiteId) FROM workpackage_has_testsuite  wphasTestSuite WHERE wphasTestSuite.workpackageId="+workpackageId+" ) AS testSuiteCount,"
								+"(  SELECT COUNT(wphasFeature.featureId) FROM   workpackage_has_feature  wphasFeature WHERE wphasFeature.workpackageId="+workpackageId+" ) AS featueCount,"
								+"(  SELECT COUNT(job.workpackageId) FROM test_run_job job WHERE job.workpackageId ="+workpackageId+"   ) AS jobCount,"
								+"(  SELECT COUNT(buglist.testExecutionResultBugId) AS bugCount FROM workpackage_testcase_execution_plan AS wptcPlan" 
								 +"  LEFT JOIN test_execution_result_bug_list AS buglist ON wptcPlan.wptcepId=buglist.testCaseExecutionResultId WHERE wptcPlan.workPackageId="+workpackageId+" ) AS defectsCount"
								+"  FROM    workpackage AS wp INNER JOIN workflowevent AS wrkflwevent ON wp.workfloweventid=wrkflwevent.workfloweventId "
								+ "INNER JOIN workflow wf ON wrkflwevent.workflowId=wf.workflowId AND entitymasterid=2 WHERE wp.workPackageId="+workpackageId ;
						 query = sessionFactory.getCurrentSession().createSQLQuery(sql);
						listFromDb = ((SQLQuery) query).addScalar("name",StandardBasicTypes.STRING).addScalar("stageId",StandardBasicTypes.INTEGER).addScalar("testCaseCount",StandardBasicTypes.INTEGER).addScalar("testSuiteCount",StandardBasicTypes.INTEGER).addScalar("featueCount",StandardBasicTypes.INTEGER).addScalar("jobCount",StandardBasicTypes.LONG).addScalar("defectsCount",StandardBasicTypes.LONG).list();
						return listFromDb;	
					}
				}
				
	
				
			}else if(filterId==1){
				sql="SELECT testrunJob.testRunJobId,runConfig.runconfigName AS runConfigName,tcres.result AS result,COUNT(tcres.result) AS totalResult ,COUNT(wptcepId) AS totalExecTCS,testrunJob.testRunFailureMessage as comments,testrunJob.testRunStatus as  jobStatus FROM test_run_job AS testrunJob LEFT JOIN workpackage_testcase_execution_plan AS wptcPlan  ON testrunJob.testRunJobId =wptcPlan.testRunJobId AND wptcPlan.status=1 INNER JOIN runconfiguration AS runConfig ON testrunJob.runConfigurationId=runConfig.runconfigId   LEFT JOIN testcase_execution_result AS tcres ON wptcPlan.wptcepId=tcres.testCaseExecutionResultId   WHERE testrunJob.workpackageId=:workPackageId GROUP BY wptcPlan.testRunJobId, tcres.result";
			}else if(filterId==4){
				sql="SELECT wptcPlan.executionPriorityId AS priorityId,tcres.result AS result,COUNT(tcres.result) As totalResTC FROM workpackage_testcase_execution_plan AS wptcPlan INNER JOIN testcase_execution_result AS tcres ON wptcPlan.wptcepId=tcres.testCaseExecutionResultId  WHERE workPackageId=:workPackageId and wptcPlan.status=1 GROUP BY wptcPlan.executionPriorityId,tcres.result";
			}else if(filterId==5){
				sql="SELECT feature.productFeatureId AS featureId,feature.productFeatureName AS featureName,wptcPlan.executionPriorityId AS executionPriority,tcres.result AS resultId,COUNT(tcres.result) AS totalResult  FROM workpackage_testcase_execution_plan AS wptcPlan INNER JOIN testcase_execution_result AS tcres ON wptcPlan.wptcepId=tcres.testCaseExecutionResultId INNER JOIN product_feature AS feature ON wptcPlan.featureId =feature.productFeatureId   WHERE wptcPlan.workPackageId=:workPackageId AND wptcPlan.sourceType='feature' and wptcPlan.status=1  GROUP BY feature.productFeatureId,"
						+ "wptcPlan.featureId,wptcPlan.executionPriorityId,tcres.result";
			}
					
			else{
				sql="SELECT feature.productFeatureId AS featureId,feature.productFeatureName AS featureName,runcfg.runconfigId AS runConfigId,tcres.result AS resultId,COUNT(tcres.result) AS totalResult  FROM workpackage_testcase_execution_plan AS wptcPlan INNER JOIN testcase_execution_result AS tcres ON wptcPlan.wptcepId=tcres.testCaseExecutionResultId INNER JOIN product_feature AS feature ON wptcPlan.featureId =feature.productFeatureId  INNER JOIN workpackage_has_runconfiguration wpruncfg ON  wptcPlan.testRunConfigurationId=wpruncfg.workpackageRunConfigurationId INNER JOIN runconfiguration AS runcfg ON  wpruncfg.runconfigurationId=runcfg.runconfigId WHERE wptcPlan.workPackageId=:workPackageId AND wptcPlan.sourceType='feature' and wptcPlan.status=1 GROUP BY wptcPlan.featureId,wpruncfg.runconfigurationId,tcres.result ORDER BY wptcPlan.featureId,wpruncfg.runconfigurationId,tcres.result";
				
			}
		}else if(databaseDialect != null && databaseDialect.equalsIgnoreCase("org.hibernate.dialect.PostgreSQLDialect")) {
			if(filterId==0){  //workpackage summary
				sql="SELECT COUNT(tcres.result) AS totalResult,tcres.result AS resultId FROM workpackage_testcase_execution_plan AS wptcPlan INNER JOIN testcase_execution_result AS tcres ON wptcPlan.wptcepId=tcres.testCaseExecutionResultId  WHERE workPackageId=:workPackageId and wptcPlan.status=1 GROUP BY (tcres.result)";
				 query = sessionFactory.getCurrentSession().createSQLQuery(sql).setParameter("workPackageId",workpackageId);
				listFromDb = query.list();
				int length=listFromDb.size();
				if(wpSummary!=null){
					if(wpSummary.equals("wpSummary")){
						sql="SELECT wp.name,wf.stageId,"
								+"(  SELECT COUNT(wphastc.workPackageId) FROM  workpackage_has_testcase wphastc WHERE wphastc.workPackageId ="+workpackageId+"  ) AS testCaseCount,  "    
								+"(  SELECT COUNT(wphasTestSuite.testSuiteId) FROM workpackage_has_testsuite  wphasTestSuite WHERE wphasTestSuite.workpackageId="+workpackageId+" ) AS testSuiteCount,"
								+"(  SELECT COUNT(wphasFeature.featureId) FROM   workpackage_has_feature  wphasFeature WHERE wphasFeature.workpackageId="+workpackageId+" ) AS featueCount,"
								+"(  SELECT COUNT(job.workpackageId) FROM test_run_job job WHERE job.workpackageId ="+workpackageId+"   ) AS jobCount,"
								+"(  SELECT COUNT(buglist.testExecutionResultBugId) AS bugCount FROM workpackage_testcase_execution_plan AS wptcPlan" 
								 +"  LEFT JOIN test_execution_result_bug_list AS buglist ON wptcPlan.wptcepId=buglist.testCaseExecutionResultId WHERE wptcPlan.workPackageId="+workpackageId+" ) AS defectsCount"
								+"  FROM    workpackage AS wp INNER JOIN workflowevent AS wrkflwevent ON wp.workfloweventid=wrkflwevent.workfloweventId "
								+ "INNER JOIN workflow wf ON wrkflwevent.workflowId=wf.workflowId AND entitymasterid=2 WHERE wp.workPackageId="+workpackageId ;
						 query = sessionFactory.getCurrentSession().createSQLQuery(sql);
						listFromDb = ((SQLQuery) query).addScalar("name",StandardBasicTypes.STRING).addScalar("stageId",StandardBasicTypes.INTEGER).addScalar("testCaseCount",StandardBasicTypes.INTEGER).addScalar("testSuiteCount",StandardBasicTypes.INTEGER).addScalar("featueCount",StandardBasicTypes.INTEGER).addScalar("jobCount",StandardBasicTypes.LONG).addScalar("defectsCount",StandardBasicTypes.LONG).list();
						return listFromDb;	
					}
				}
				
	
				
			}else if(filterId==1){
				sql = "SELECT testrunJob.testRunJobId,runConfig.runconfigName AS runConfigName,tcres.result AS result,COUNT(tcres.result) AS totalResult ,COUNT(wptcepId) AS totalExecTCS,testrunJob.testRunFailureMessage as comments,testrunJob.testRunStatus as  jobStatus FROM test_run_job AS testrunJob LEFT JOIN workpackage_testcase_execution_plan AS wptcPlan  ON testrunJob.testRunJobId =wptcPlan.testRunJobId AND wptcPlan.status=1 INNER JOIN runconfiguration AS runConfig ON testrunJob.runConfigurationId=runConfig.runconfigId   LEFT JOIN testcase_execution_result AS tcres ON wptcPlan.wptcepId=tcres.testCaseExecutionResultId   WHERE testrunJob.workpackageId=:workPackageId GROUP BY wptcPlan.testRunJobId, tcres.result,testrunJob.testRunJobId,runConfigName";
			}else if(filterId==4){
				sql="SELECT wptcPlan.executionPriorityId AS priorityId,tcres.result AS result,COUNT(tcres.result) As totalResTC FROM workpackage_testcase_execution_plan AS wptcPlan INNER JOIN testcase_execution_result AS tcres ON wptcPlan.wptcepId=tcres.testCaseExecutionResultId  WHERE workPackageId=:workPackageId and wptcPlan.status=1 GROUP BY wptcPlan.executionPriorityId,tcres.result";
			}else if(filterId==5){
				sql="SELECT feature.productFeatureId AS featureId,feature.productFeatureName AS featureName,wptcPlan.executionPriorityId AS executionPriority,tcres.result AS resultId,COUNT(tcres.result) AS totalResult  FROM workpackage_testcase_execution_plan AS wptcPlan INNER JOIN testcase_execution_result AS tcres ON wptcPlan.wptcepId=tcres.testCaseExecutionResultId INNER JOIN product_feature AS feature ON wptcPlan.featureId =feature.productFeatureId   WHERE wptcPlan.workPackageId=:workPackageId AND wptcPlan.status=1  GROUP BY feature.productFeatureId,wptcPlan.featureId,wptcPlan.executionPriorityId,tcres.result";
				
			}
					
			else{
				sql="SELECT feature.productFeatureId AS featureId,feature.productFeatureName AS featureName,runcfg.runconfigId AS runConfigId,tcres.result AS resultId,COUNT(tcres.result) AS totalResult  FROM workpackage_testcase_execution_plan AS wptcPlan INNER JOIN testcase_execution_result AS tcres ON wptcPlan.wptcepId=tcres.testCaseExecutionResultId INNER JOIN product_feature AS feature ON wptcPlan.featureId =feature.productFeatureId  INNER JOIN workpackage_has_runconfiguration wpruncfg ON  wptcPlan.testRunConfigurationId=wpruncfg.workpackageRunConfigurationId INNER JOIN runconfiguration AS runcfg ON  wpruncfg.runconfigurationId=runcfg.runconfigId WHERE wptcPlan.workPackageId=:workPackageId AND wptcPlan.status=1 GROUP BY feature.productFeatureId,wptcPlan.featureId,wpruncfg.runconfigurationId,tcres.result,runcfg.runconfigId ORDER BY wptcPlan.featureId,wpruncfg.runconfigurationId,tcres.result";
				
			}
		}
			
		 query = sessionFactory.getCurrentSession().createSQLQuery(sql).setParameter("workPackageId",workpackageId);
		listFromDb = query.list();
		return listFromDb;	 
		
	}

	@Override
	@Transactional
	public Set<RunConfiguration> listTestBedByFeature(int featureId,
			int workpackageId,int status) {
		Set<RunConfiguration> runConfigurations = new HashSet<RunConfiguration>();
		List<WorkPackageFeatureExecutionPlan> workPackageFeatureExecutionPlans = null;

		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackageFeatureExecutionPlan.class, "wfep");
			c.createAlias("wfep.feature", "f");
			c.createAlias("wfep.workPackage", "wp");

			c.add(Restrictions.eq("wp.workPackageId", workpackageId));
			c.add(Restrictions.eq("f.productFeatureId", featureId));
			c.add(Restrictions.eq("wfep.status", status));

			workPackageFeatureExecutionPlans=c.list();
			if(workPackageFeatureExecutionPlans!=null && !workPackageFeatureExecutionPlans.isEmpty()){
				for (WorkPackageFeatureExecutionPlan wpfep : workPackageFeatureExecutionPlans) {
					Hibernate.initialize(wpfep.getRunConfiguration().getRunconfiguration());
					Hibernate.initialize(wpfep.getRunConfiguration().getRunconfiguration().getEnvironmentcombination());
					Hibernate.initialize(wpfep.getRunConfiguration().getRunconfiguration().getGenericDevice());
					Hibernate.initialize(wpfep.getRunConfiguration().getRunconfiguration().getHostList());
					Hibernate.initialize(wpfep.getRunConfiguration().getRunconfiguration().getTestRunPlan());
					Hibernate.initialize(wpfep.getRunConfiguration().getRunconfiguration().getProductVersion());
					
					runConfigurations.add(wpfep.getRunConfiguration().getRunconfiguration());
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return runConfigurations;
	}

	@Override
	@Transactional
	public List<WorkPackageTestCaseExecutionPlan> listWorkPackageTestcaseExecutionPlan(
			int featureId, int workpackageId, int runConfigId, String type,int testSuiteId,int testcaseId) {
		List<WorkPackageTestCaseExecutionPlan> workPackageTestcaseExecutionPlans = null;

		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackageTestCaseExecutionPlan.class, "wtcep");
			c.createAlias("wtcep.workPackage", "wp");
			c.add(Restrictions.eq("wp.workPackageId", workpackageId));
			if(featureId!=-1){
				c.createAlias("wtcep.feature", "f");
				c.add(Restrictions.eq("f.productFeatureId", featureId));
			}
			if(testSuiteId!=-1){
				c.createAlias("wtcep.testSuiteList", "ts");
				c.add(Restrictions.eq("ts.testSuiteId", testSuiteId));
			}
			if(testcaseId!=-1){
				c.createAlias("wtcep.testCase", "tc");
				c.add(Restrictions.eq("tc.testCaseId", testcaseId));
			}
			if(runConfigId!=-1){
				c.createAlias("wtcep.runConfiguration", "wprc");
				c.createAlias("wprc.runconfiguration", "rc");
				c.add(Restrictions.eq("rc.runconfigId", runConfigId));
			}
			c.add(Restrictions.eq("wtcep.sourceType", type));

			workPackageTestcaseExecutionPlans=c.list();
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return workPackageTestcaseExecutionPlans;
	}

	@Override
	@Transactional
	public Set<RunConfiguration> listTestBedByTestSuite(int testsuiteId,
			int workpackageId, int status) {

		Set<RunConfiguration> runConfigurations = new HashSet<RunConfiguration>();
		List<WorkPackageTestSuiteExecutionPlan> workPackageTestSuiteExecutionPlans = null;

		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackageTestSuiteExecutionPlan.class, "wtsep");
			c.createAlias("wtsep.testsuite", "ts");
			c.createAlias("wtsep.workPackage", "wp");

			c.add(Restrictions.eq("wp.workPackageId", workpackageId));
			c.add(Restrictions.eq("ts.testSuiteId", testsuiteId));
			c.add(Restrictions.eq("wtsep.status", status));

			workPackageTestSuiteExecutionPlans=c.list();
			if(workPackageTestSuiteExecutionPlans!=null && !workPackageTestSuiteExecutionPlans.isEmpty()){
				for (WorkPackageTestSuiteExecutionPlan wptsep : workPackageTestSuiteExecutionPlans) {
					Hibernate.initialize(wptsep.getRunConfiguration().getRunconfiguration());
					Hibernate.initialize(wptsep.getRunConfiguration().getRunconfiguration().getEnvironmentcombination());
					Hibernate.initialize(wptsep.getRunConfiguration().getRunconfiguration().getGenericDevice());
					Hibernate.initialize(wptsep.getRunConfiguration().getRunconfiguration().getHostList());

					runConfigurations.add(wptsep.getRunConfiguration().getRunconfiguration());
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return runConfigurations;
	}

	@Override
	@Transactional
	public WorkPackageTestCaseExecutionPlan getWorkPackageTestcaseExecutionPlan(
			int featureId, int workpackageId, String runConfigName, String type,
			int testSuiteId, int testcaseId) {
				List<WorkPackageTestCaseExecutionPlan> workPackageTestcaseExecutionPlans = null;
				WorkPackageTestCaseExecutionPlan workPackageTestcaseExecutionPlan = null;

				try {
					Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackageTestCaseExecutionPlan.class, "wtcep");
					c.createAlias("wtcep.workPackage", "wp");
					c.add(Restrictions.eq("wp.workPackageId", workpackageId));
					if(featureId!=-1){
						c.createAlias("wtcep.feature", "f");
						c.add(Restrictions.eq("f.productFeatureId", featureId));
					}
					if(testSuiteId!=-1){
						c.createAlias("wtcep.testSuiteList", "ts");
						c.add(Restrictions.eq("ts.testSuiteId", testSuiteId));
					}
					if(testcaseId!=-1){
						c.createAlias("wtcep.testCase", "tc");
						c.add(Restrictions.eq("tc.testCaseId", testcaseId));
					}
					if(runConfigName!=null){
						c.createAlias("wtcep.runConfiguration", "wprc");
						c.createAlias("wprc.runconfiguration", "rc");
						c.add(Restrictions.eq("rc.runconfigName", runConfigName));
					}
					c.add(Restrictions.eq("wtcep.sourceType", type));

					workPackageTestcaseExecutionPlans=c.list();
					workPackageTestcaseExecutionPlan = (workPackageTestcaseExecutionPlans != null && workPackageTestcaseExecutionPlans.size() != 0) ? (WorkPackageTestCaseExecutionPlan) workPackageTestcaseExecutionPlans.get(0) : null;

				}
				catch(Exception e){
					e.printStackTrace();
				}
				return workPackageTestcaseExecutionPlan;
	}
	
	@Override
	@Transactional
	public WorkPackageFeatureExecutionPlan getWorkpackageFeatureExecutionPlan(Integer workPackageId,
			Integer productFeatureId,String runConfigurationName) {
		List<WorkPackageFeatureExecutionPlan> list = null;
		WorkPackageFeatureExecutionPlan dl =null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackageFeatureExecutionPlan.class, "wpfep");
			
			if (workPackageId !=0) {
				c.createAlias("wpfep.workPackage", "workPackage");
				c.add(Restrictions.eq("workPackage.workPackageId", workPackageId));
			} 
			if (productFeatureId !=0) {
				c.createAlias("wpfep.feature", "feature");
				c.add(Restrictions.eq("feature.productFeatureId", productFeatureId));
			} 
			if (runConfigurationName !=null) {
				c.createAlias("wpfep.runConfiguration", "wprc");
				c.createAlias("wprc.runconfiguration", "rc");

				c.add(Restrictions.eq("rc.runconfigName", runConfigurationName));
			} 
			list = c.list();
			dl = (list != null && list.size() != 0) ? (WorkPackageFeatureExecutionPlan)(list.get(0)): null;				
			if(dl!=null){
					Hibernate.initialize(dl.getWorkPackage().getProductBuild().getProductVersion().getProductMaster());
					Hibernate.initialize(dl.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductMode());
					Hibernate.initialize(dl.getWorkPackage());
					Hibernate.initialize(dl.getWorkPackage().getWorkFlowEvent());
					Hibernate.initialize(dl.getWorkPackage().getWorkFlowEvent().getWorkFlow());
					Hibernate.initialize(dl.getTester());
					Hibernate.initialize(dl.getTestLead());
					Hibernate.initialize(dl.getActualWorkShiftMaster());
					Hibernate.initialize(dl.getPlannedWorkShiftMaster());
					Hibernate.initialize(dl.getHostList());
					if(dl.getRunConfiguration()!=null){
						if(dl.getRunConfiguration().getRunconfiguration()!=null){ 
							Hibernate.initialize(dl.getRunConfiguration().getRunconfiguration());
							if(dl.getRunConfiguration().getRunconfiguration()!=null){
								if(dl.getRunConfiguration().getRunconfiguration().getEnvironmentcombination()!=null)
									Hibernate.initialize(dl.getRunConfiguration().getRunconfiguration().getEnvironmentcombination());
								if(dl.getRunConfiguration().getRunconfiguration().getGenericDevice()!=null)
									Hibernate.initialize(dl.getRunConfiguration().getRunconfiguration().getGenericDevice());
							}
						}
						Hibernate.initialize(dl.getRunConfiguration());
						Hibernate.initialize(dl.getRunConfiguration().getWorkpackage());
					}
					
			}
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return dl;
	}

	@Override
	@Transactional
	public WorkPackageTestSuiteExecutionPlan getWorkpackageTestSuiteExecutionPlan(
			Integer workPackageId, Integer testSuiteId,String runConfigurationName) {
		List<WorkPackageTestSuiteExecutionPlan> list = null;
		WorkPackageTestSuiteExecutionPlan dl =null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackageTestSuiteExecutionPlan.class, "wptsep");
			
			if (workPackageId !=-1) {
				c.createAlias("wptsep.workPackage", "workPackage");
				c.add(Restrictions.eq("workPackage.workPackageId", workPackageId));
			} 
			if (testSuiteId !=-1) {
				c.createAlias("wptsep.testsuite", "testsuite");
				c.add(Restrictions.eq("testsuite.testSuiteId", testSuiteId));
			} 
			if (runConfigurationName !=null) {
				c.createAlias("wptsep.runConfiguration", "wprc");
				c.createAlias("wprc.runconfiguration", "rc");

				c.add(Restrictions.eq("rc.runconfigName", runConfigurationName));
			} 
			list = c.list();
			dl = (list != null && list.size() != 0) ? (WorkPackageTestSuiteExecutionPlan)(list.get(0)): null;				
			if(dl!=null){
					Hibernate.initialize(dl.getWorkPackage().getProductBuild().getProductVersion().getProductMaster());
					Hibernate.initialize(dl.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductMode());
					Hibernate.initialize(dl.getWorkPackage());
					Hibernate.initialize(dl.getWorkPackage().getWorkFlowEvent());
					Hibernate.initialize(dl.getWorkPackage().getWorkFlowEvent().getWorkFlow());
					Hibernate.initialize(dl.getTester());
					Hibernate.initialize(dl.getTestLead());
					Hibernate.initialize(dl.getActualWorkShiftMaster());
					Hibernate.initialize(dl.getPlannedWorkShiftMaster());
					Hibernate.initialize(dl.getHostList());
					if(dl.getRunConfiguration()!=null){
						if(dl.getRunConfiguration().getRunconfiguration()!=null){ 
							Hibernate.initialize(dl.getRunConfiguration().getRunconfiguration());
							if(dl.getRunConfiguration().getRunconfiguration()!=null){
								if(dl.getRunConfiguration().getRunconfiguration().getEnvironmentcombination()!=null)
									Hibernate.initialize(dl.getRunConfiguration().getRunconfiguration().getEnvironmentcombination());
								if(dl.getRunConfiguration().getRunconfiguration().getGenericDevice()!=null)
									Hibernate.initialize(dl.getRunConfiguration().getRunconfiguration().getGenericDevice());
							}
						}
						Hibernate.initialize(dl.getRunConfiguration());
						Hibernate.initialize(dl.getRunConfiguration().getWorkpackage());
					}
					
			}
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return dl;
	}
	
	
	@Override
	@Transactional
	public List<WorkPackageTCEPSummaryDTO> getWPTCExecutionSummaryByProdIdBuildId(Integer testFactoryId,Integer productId, Integer productBuildId, Integer jtStartIndex, Integer jtPageSize, String filter) {		
			List<WorkPackageTCEPSummaryDTO> wptcepResultList = new ArrayList<WorkPackageTCEPSummaryDTO>();
		try {
			String sql="select pm.productName as productName, wp.workpackageId as workPackageId,wp.name as workPackageName ,"
					+ "wp.actualStartDate as actualStartDate, wp.actualEndDate as actualEndDate, wflow.stageName as workFlowStageName,"
					+ "(select etm.name from execution_type_master as etm  where etm.executionTypeId =wp.workPackageType ) as exeType,"
					+ "(SELECT COUNT(wphastc.workPackageId) FROM  workpackage_has_testcase wphastc "
					+ "WHERE wphastc.workPackageId =wp.workpackageId ) AS testCaseCount, "
					+ "(SELECT COUNT(wphasTestSuite.testSuiteId) FROM workpackage_has_testsuite  wphasTestSuite "
					+ "WHERE wphasTestSuite.workpackageId=wp.workpackageId ) AS testSuiteCount, "
					+ "(SELECT COUNT(wphasFeature.featureId) FROM   workpackage_has_feature  wphasFeature "
					+ "WHERE wphasFeature.workpackageId=wp.workpackageId ) AS featueCount, "
					+ "(SELECT COUNT(job.workpackageId) FROM test_run_job job WHERE job.workpackageId =wp.workpackageId  ) AS jobCount, "

					+ "(SELECT COUNT(job.workpackageId) FROM test_run_job job WHERE job.workpackageId =wp.workpackageId and job.testRunStatus=3 ) AS jobsExecuting, "

					+ "(SELECT COUNT(job.workpackageId) FROM test_run_job job WHERE job.workpackageId =wp.workpackageId and job.testRunStatus=4 ) AS jobsQueued, "

					+ "(SELECT COUNT(job.workpackageId) FROM test_run_job job WHERE job.workpackageId =wp.workpackageId and job.testRunStatus=5 ) AS jobsCompleted, "

					+ "(SELECT COUNT(job.workpackageId) FROM test_run_job job WHERE job.workpackageId =wp.workpackageId and job.testRunStatus=7 ) AS jobsAborted, "
					
					+ "(SELECT COUNT(buglist.testExecutionResultBugId) FROM workpackage_testcase_execution_plan AS wptcPlan "
					+ "LEFT JOIN test_execution_result_bug_list AS buglist ON wptcPlan.wptcepId=buglist.testCaseExecutionResultId "
					+ "WHERE wptcPlan.workPackageId=wp.workpackageId) AS defectsCount, "
					+ "( SELECT COUNT(tcres.result)   FROM workpackage_testcase_execution_plan as wptcplan "
					+ "inner join  testcase_execution_result  AS tcres "
					+ "on tcres.testCaseExecutionResultId = wptcplan.wptcepId and wptcplan.status=1 "
					+ "where wptcplan.workpackageId= wp.workpackageId  and tcres.result='PASSED' ) AS passedCount,"
					+ " ( SELECT COUNT(tcres.result)   FROM  workpackage_testcase_execution_plan as wptcplan "
					+ "inner join testcase_execution_result  AS tcres "
					+ "on tcres.testCaseExecutionResultId = wptcplan.wptcepId and wptcplan.status=1 "
					+ " WHERE wptcplan.workpackageId= wp.workpackageId AND tcres.result='FAILED' ) AS failedCount,"
					+ " ( SELECT COUNT(tcres.result)   FROM  workpackage_testcase_execution_plan as wptcplan "
					+ "inner join testcase_execution_result  AS tcres "
					+ "on tcres.testCaseExecutionResultId = wptcplan.wptcepId and wptcplan.status=1"
					+ " WHERE wptcplan.workpackageId = wp.workpackageId AND tcres.result='BLOCKED' ) AS blockedCount,"
					+ " ( SELECT COUNT(tcres.result)   FROM  workpackage_testcase_execution_plan as wptcplan "
					+ "inner join testcase_execution_result  AS tcres "
					+ "on tcres.testCaseExecutionResultId = wptcplan.wptcepId and wptcplan.status=1 "
					+ "WHERE wptcplan.workpackageId = wp.workpackageId AND tcres.result='NORUN' ) AS norunCount,"
					+ "  ( SELECT COUNT(tcres.result)   FROM  workpackage_testcase_execution_plan as wptcplan "
					+ "inner join testcase_execution_result  AS tcres "
					+ "on tcres.testCaseExecutionResultId= wptcplan.wptcepId and wptcplan.status = 1"
					+ " WHERE wptcplan.workpackageId = wp.workpackageId AND tcres.result IS NULL ) AS notexecutedCount, "
					+ "(SELECT COUNT(tser.teststepexecutionresultid) FROM teststep_execution_result as tser "
					+ "inner join  testcase_execution_result  AS tcres "
					+ "on tcres.testCaseExecutionResultId = tser.tescaseexecutionresultid "
					+ "inner join workpackage_testcase_execution_plan as wptcplan "
					+ "on wptcplan.wptcepId = tcres.testCaseExecutionResultId "
					+ "where wptcplan.workpackageId= wp.workpackageId )  AS teststepcount "
					
					+ "FROM workpackage wp left JOIN workflowevent AS wfevent on wp.workfloweventid = wfevent.workfloweventId "
					+ "left join workflow as wflow on wfevent.workflowId = wflow.workflowId "
					+ "INNER JOIN  product_build pb ON pb.productBuildId=wp.productBuildId ";
					
			if( productId != -1 && productBuildId == -1){//Product Level
				sql = sql + "inner join product_version_list_master  pvlm on pb.productVersionId=pvlm.productVersionListId "
						+ "inner join product_master pm on pvlm.productId=pm.productId "
						+ "where pm.productId="+ productId +"  and pb.status=1 ";
			}else if(productId == -1 && productBuildId != -1){//Build Level
				sql = sql  + "inner join product_version_list_master  pvlm on pb.productVersionId=pvlm.productVersionListId "
						+ "inner join product_master pm on pvlm.productId=pm.productId "
						+ "WHERE  pb.productBuildId="+productBuildId+"  and pb.status=1 ";	
			}else if(testFactoryId !=null && productId == -1){//Build Level
				if(testFactoryId == -1){
					sql = sql + "inner join product_version_list_master  pvlm on pb.productVersionId=pvlm.productVersionListId "
							+ "inner join product_master pm on pvlm.productId=pm.productId "
							+ "where pb.status=1 ";	
				} else {
					sql = sql + "inner join product_version_list_master  pvlm on pb.productVersionId=pvlm.productVersionListId "
						+ "inner join product_master pm on pvlm.productId=pm.productId "
						+ "INNER JOIN test_factory tf ON (pm.testFactoryId=tf.testFactoryId) "
						+ "where tf.testFactoryId="+testFactoryId+"  and pb.status=1 ";
				}				
			}
			
			if(filter != null && filter.equalsIgnoreCase("DAY"))
				sql = sql + "AND wp.createdate BETWEEN (CURRENT_DATE - INTERVAL '1 DAY') AND (CURRENT_DATE + INTERVAL '1 DAY') GROUP BY wp.workpackageId,pm.productname,wflow.stagename order by wp.workPackageId desc LIMIT 1000 ";
			else if(filter != null && filter.equalsIgnoreCase("WEEK"))
				sql = sql + "AND wp.createdate BETWEEN (CURRENT_DATE - INTERVAL '1 WEEK') AND (CURRENT_DATE + INTERVAL '1 WEEK') GROUP BY wp.workpackageId,pm.productname,wflow.stagename order by wp.workPackageId desc LIMIT 1000 ";
			else if(filter != null && filter.equalsIgnoreCase("MONTH"))
				sql = sql + "AND wp.createdate BETWEEN (CURRENT_DATE - INTERVAL '1 MONTH') AND (CURRENT_DATE + INTERVAL '1 MONTH') GROUP BY wp.workpackageId,pm.productname,wflow.stagename order by wp.workPackageId desc LIMIT 1000 ";
			else
				sql = sql + "GROUP BY wp.workpackageId,pm.productname,wflow.stagename order by wp.workPackageId desc ";
			
			if(jtStartIndex != -1 && jtPageSize != -1 ){
				sql = sql + "offset ";								
				sql = sql+jtStartIndex+" limit "+jtPageSize+"";	
			}
			
			log.info("SQL Query for Workpackages : "+sql);
			
			wptcepResultList=sessionFactory.getCurrentSession().createSQLQuery(sql)
					.addScalar("productName",StandardBasicTypes.STRING )
					.addScalar("workPackageId",StandardBasicTypes.INTEGER ).addScalar("workPackageName", StandardBasicTypes.STRING)
					.addScalar("actualStartDate",StandardBasicTypes.DATE ).addScalar("actualEndDate", StandardBasicTypes.DATE)
					.addScalar("workFlowStageName", StandardBasicTypes.STRING).addScalar("exeType", StandardBasicTypes.STRING)					
					.addScalar("testCaseCount", StandardBasicTypes.INTEGER)
					.addScalar("testSuiteCount", StandardBasicTypes.INTEGER).addScalar("featueCount", StandardBasicTypes.INTEGER)					
					.addScalar("jobCount", StandardBasicTypes.INTEGER)
					.addScalar("jobsExecuting", StandardBasicTypes.INTEGER).addScalar("jobsQueued", StandardBasicTypes.INTEGER)
					.addScalar("jobsCompleted", StandardBasicTypes.INTEGER).addScalar("jobsAborted", StandardBasicTypes.INTEGER)					
					.addScalar("defectsCount", StandardBasicTypes.INTEGER)
					.addScalar("passedCount", StandardBasicTypes.INTEGER).addScalar("failedCount", StandardBasicTypes.INTEGER)
					.addScalar("blockedCount", StandardBasicTypes.INTEGER).addScalar("norunCount", StandardBasicTypes.INTEGER)
					.addScalar("notexecutedCount", StandardBasicTypes.INTEGER).addScalar("teststepcount", StandardBasicTypes.INTEGER)					
					.setResultTransformer(Transformers.aliasToBean(WorkPackageTCEPSummaryDTO.class)).list();
				
		} catch (RuntimeException re) {
		log.error("getWPTCExecutionSummary by BuildId failed", re);
			re.printStackTrace();
		}
		return wptcepResultList;		
	}	
	
	
	
	@Override
	@Transactional
	public List<WorkPackageTCEPSummaryDTO> getWPTCExecutionSummaryByProdIdAndBuildIdAndWorkpackageId(Integer testFactoryId,Integer productId, Integer productBuildId,Integer workpackageId, Integer jtStartIndex, Integer jtPageSize, String filter) {		
			List<WorkPackageTCEPSummaryDTO> wptcepResultList = new ArrayList<WorkPackageTCEPSummaryDTO>();
		try {
			String sql="select pm.productName as productName, wp.workpackageId as workPackageId,wp.name as workPackageName ,"
					+ "wp.actualStartDate as actualStartDate, wp.actualEndDate as actualEndDate, wflow.stageName as workFlowStageName,"
					+ "(select etm.name from execution_type_master as etm  where etm.executionTypeId =wp.workPackageType ) as exeType,"
					+ "(SELECT COUNT(wphastc.workPackageId) FROM  workpackage_has_testcase wphastc "
					+ "WHERE wphastc.workPackageId =wp.workpackageId ) AS testCaseCount, "
					+ "(SELECT COUNT(wphasTestSuite.testSuiteId) FROM workpackage_has_testsuite  wphasTestSuite "
					+ "WHERE wphasTestSuite.workpackageId=wp.workpackageId ) AS testSuiteCount, "
					+ "(SELECT COUNT(wphasFeature.featureId) FROM   workpackage_has_feature  wphasFeature "
					+ "WHERE wphasFeature.workpackageId=wp.workpackageId ) AS featueCount, "
					+ "(SELECT COUNT(job.workpackageId) FROM test_run_job job WHERE job.workpackageId =wp.workpackageId  ) AS jobCount, "

					+ "(SELECT COUNT(job.workpackageId) FROM test_run_job job WHERE job.workpackageId =wp.workpackageId and job.testRunStatus=3 ) AS jobsExecuting, "

					+ "(SELECT COUNT(job.workpackageId) FROM test_run_job job WHERE job.workpackageId =wp.workpackageId and job.testRunStatus=4 ) AS jobsQueued, "

					+ "(SELECT COUNT(job.workpackageId) FROM test_run_job job WHERE job.workpackageId =wp.workpackageId and job.testRunStatus=5 ) AS jobsCompleted, "

					+ "(SELECT COUNT(job.workpackageId) FROM test_run_job job WHERE job.workpackageId =wp.workpackageId and job.testRunStatus=7 ) AS jobsAborted, "
					
					+ "(SELECT COUNT(buglist.testExecutionResultBugId) FROM workpackage_testcase_execution_plan AS wptcPlan "
					+ "LEFT JOIN test_execution_result_bug_list AS buglist ON wptcPlan.wptcepId=buglist.testCaseExecutionResultId "
					+ "WHERE wptcPlan.workPackageId=wp.workpackageId) AS defectsCount, "
					+ "( SELECT COUNT(tcres.result)   FROM workpackage_testcase_execution_plan as wptcplan "
					+ "inner join  testcase_execution_result  AS tcres "
					+ "on tcres.testCaseExecutionResultId = wptcplan.wptcepId and wptcplan.status=1 "
					+ "where wptcplan.workpackageId= wp.workpackageId  and tcres.result='PASSED' ) AS passedCount,"
					+ " ( SELECT COUNT(tcres.result)   FROM  workpackage_testcase_execution_plan as wptcplan "
					+ "inner join testcase_execution_result  AS tcres "
					+ "on tcres.testCaseExecutionResultId = wptcplan.wptcepId and wptcplan.status=1 "
					+ " WHERE wptcplan.workpackageId= wp.workpackageId AND tcres.result='FAILED' ) AS failedCount,"
					+ " ( SELECT COUNT(tcres.result)   FROM  workpackage_testcase_execution_plan as wptcplan "
					+ "inner join testcase_execution_result  AS tcres "
					+ "on tcres.testCaseExecutionResultId = wptcplan.wptcepId and wptcplan.status=1"
					+ " WHERE wptcplan.workpackageId = wp.workpackageId AND tcres.result='BLOCKED' ) AS blockedCount,"
					+ " ( SELECT COUNT(tcres.result)   FROM  workpackage_testcase_execution_plan as wptcplan "
					+ "inner join testcase_execution_result  AS tcres "
					+ "on tcres.testCaseExecutionResultId = wptcplan.wptcepId and wptcplan.status=1 "
					+ "WHERE wptcplan.workpackageId = wp.workpackageId AND tcres.result='NORUN' ) AS norunCount,"
					+ "  ( SELECT COUNT(tcres.result)   FROM  workpackage_testcase_execution_plan as wptcplan "
					+ "inner join testcase_execution_result  AS tcres "
					+ "on tcres.testCaseExecutionResultId= wptcplan.wptcepId and wptcplan.status = 1"
					+ " WHERE wptcplan.workpackageId = wp.workpackageId AND tcres.result IS NULL ) AS notexecutedCount, "
					+ "(SELECT COUNT(tser.teststepexecutionresultid) FROM teststep_execution_result as tser "
					+ "inner join  testcase_execution_result  AS tcres "
					+ "on tcres.testCaseExecutionResultId = tser.tescaseexecutionresultid "
					+ "inner join workpackage_testcase_execution_plan as wptcplan "
					+ "on wptcplan.wptcepId = tcres.testCaseExecutionResultId "
					+ "where wptcplan.workpackageId= wp.workpackageId )  AS teststepcount "
					
					+ "FROM workpackage wp left JOIN workflowevent AS wfevent on wp.workfloweventid = wfevent.workfloweventId "
					+ "left join workflow as wflow on wfevent.workflowId = wflow.workflowId "
					+ "INNER JOIN  product_build pb ON pb.productBuildId=wp.productBuildId ";
			
					
			if( productId != -1 && productBuildId != -1){//Product Level
				sql = sql + "inner join product_version_list_master  pvlm on pb.productVersionId=pvlm.productVersionListId "
						+ "inner join product_master pm on pvlm.productId=pm.productId "
						+ "where pm.productId="+ productId +" and pb.productBuildId="+productBuildId+"  and pb.status=1 ";
			}else if( productId != -1 && productBuildId == -1){//Product Level
				sql = sql + "inner join product_version_list_master  pvlm on pb.productVersionId=pvlm.productVersionListId "
						+ "inner join product_master pm on pvlm.productId=pm.productId "
						+ "where pm.productId="+ productId +"  and pb.status=1 ";
			}else if(productId == -1 && productBuildId != -1){//Build Level
				sql = sql  + "inner join product_version_list_master  pvlm on pb.productVersionId=pvlm.productVersionListId "
						+ "inner join product_master pm on pvlm.productId=pm.productId "
						+ "WHERE  pb.productBuildId="+productBuildId+"  and pb.status=1 ";	
			}else if(testFactoryId !=null && productId == -1){//Build Level
				if(testFactoryId == -1){
					sql = sql + "inner join product_version_list_master  pvlm on pb.productVersionId=pvlm.productVersionListId "
							+ "inner join product_master pm on pvlm.productId=pm.productId "
							+ "where pb.status=1 ";	
				} else {
					sql = sql + "inner join product_version_list_master  pvlm on pb.productVersionId=pvlm.productVersionListId "
						+ "inner join product_master pm on pvlm.productId=pm.productId "
						+ "INNER JOIN test_factory tf ON (pm.testFactoryId=tf.testFactoryId) "
						+ "where tf.testFactoryId="+testFactoryId+"  and pb.status=1 ";
				}				
			}
			if(workpackageId != -1) {
				sql = sql + " and wp.workpackageId="+workpackageId+" ";
			}
			
			if(filter != null && filter.equalsIgnoreCase("DAY")){
				sql = sql + "AND wp.createdate BETWEEN (CURRENT_DATE - INTERVAL '1 DAY') AND (CURRENT_DATE + INTERVAL '1 DAY') GROUP BY wp.workpackageId,pm.productname,wflow.stagename order by wp.workPackageId desc ";
			}else if(filter != null && filter.equalsIgnoreCase("WEEK")){
				sql = sql + "AND wp.createdate BETWEEN (CURRENT_DATE - INTERVAL '1 WEEK') AND (CURRENT_DATE + INTERVAL '1 WEEK') GROUP BY wp.workpackageId,pm.productname,wflow.stagename order by wp.workPackageId desc ";
			}else if(filter != null && filter.equalsIgnoreCase("MONTH")){
				sql = sql + "AND wp.createdate BETWEEN (CURRENT_DATE - INTERVAL '1 MONTH') AND (CURRENT_DATE + INTERVAL '1 MONTH') GROUP BY wp.workpackageId,pm.productname,wflow.stagename order by wp.workPackageId desc ";
			}else {
				if(productId !=-1){
					
					sql = sql + "GROUP BY wp.workpackageId,pm.productname,wflow.stagename order by wp.workPackageId desc ";
				} else {
					sql = sql + "GROUP BY wp.workpackageId,wflow.stagename order by wp.workPackageId desc ";
				}
			}
			if(jtStartIndex != -1 && jtPageSize != -1 ){
				sql = sql + "offset ";								
				sql = sql+jtStartIndex+" limit "+jtPageSize+"";	
			}
			
//			log.info("SQL Query for Workpackages : "+sql);
			
			wptcepResultList=sessionFactory.getCurrentSession().createSQLQuery(sql)
					.addScalar("productName",StandardBasicTypes.STRING )
					.addScalar("workPackageId",StandardBasicTypes.INTEGER ).addScalar("workPackageName", StandardBasicTypes.STRING)
					.addScalar("actualStartDate",StandardBasicTypes.DATE ).addScalar("actualEndDate", StandardBasicTypes.DATE)
					.addScalar("workFlowStageName", StandardBasicTypes.STRING).addScalar("exeType", StandardBasicTypes.STRING)					
					.addScalar("testCaseCount", StandardBasicTypes.INTEGER)
					.addScalar("testSuiteCount", StandardBasicTypes.INTEGER).addScalar("featueCount", StandardBasicTypes.INTEGER)					
					.addScalar("jobCount", StandardBasicTypes.INTEGER)
					.addScalar("jobsExecuting", StandardBasicTypes.INTEGER).addScalar("jobsQueued", StandardBasicTypes.INTEGER)
					.addScalar("jobsCompleted", StandardBasicTypes.INTEGER).addScalar("jobsAborted", StandardBasicTypes.INTEGER)					
					.addScalar("defectsCount", StandardBasicTypes.INTEGER)
					.addScalar("passedCount", StandardBasicTypes.INTEGER).addScalar("failedCount", StandardBasicTypes.INTEGER)
					.addScalar("blockedCount", StandardBasicTypes.INTEGER).addScalar("norunCount", StandardBasicTypes.INTEGER)
					.addScalar("notexecutedCount", StandardBasicTypes.INTEGER).addScalar("teststepcount", StandardBasicTypes.INTEGER)					
					.setResultTransformer(Transformers.aliasToBean(WorkPackageTCEPSummaryDTO.class)).list();
				
		} catch (RuntimeException re) {
		log.error("getWPTCExecutionSummary by BuildId failed", re);
			re.printStackTrace();
		}
		return wptcepResultList;		
	}	
	

	@Override
	public List<TestRunJob> getTestRunJobByBuildID(Integer productBuildId,Integer workPackageType, Integer jtStartIndex, Integer jtPageSize) {

		log.debug("getting getTestRunJobByWP instance by id");
		
		List<TestRunJob> testRunJobList=null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestRunJob.class, "trj");
			c.createAlias("trj.workPackage", "wp");
			c.createAlias("wp.productBuild", "pb");
			c.createAlias("wp.workPackageType", "wptype");
			
			c.add(Restrictions.eq("pb.productBuildId",productBuildId));
			c.add(Restrictions.eq("wptype.executionTypeId",workPackageType));
			if(jtStartIndex != null && jtPageSize != null)
				c.setFirstResult(jtStartIndex).setMaxResults(jtPageSize);
			c.addOrder(Order.desc("trj.testRunJobId"));
			
			testRunJobList=c.list();
			if(testRunJobList!=null && testRunJobList.size()!=0){
			for(TestRunJob trj:testRunJobList){
				Hibernate.initialize(trj.getEnvironmentCombination());
				
				if(trj.getGenericDevices()!=null){
					GenericDevices gd = trj.getGenericDevices();
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
					Hibernate.initialize(trj.getGenericDevices().getUDID());
					if(trj.getGenericDevices().getPlatformType()!=null){						
						Hibernate.initialize(trj.getGenericDevices().getPlatformType().getName());
					}
				}
				
				
				
				
				if(trj.getHostList()!=null){
				Hibernate.initialize(trj.getHostList().getHostName());
					
				}
			}
		}
			log.debug("TestRunJob successful");
		} catch (Exception re) {
			re.printStackTrace();
			log.error("TestRunJob failed", re);
		}
		return testRunJobList;
	}

	@Override
	@Transactional
	public TestRunJob getTestRunJobById(int testRunJobId) {

		log.debug("getting getTestRunJobByWP instance by id");
		
		List<TestRunJob> testRunJobLists=null;
		TestRunJob testRunJob=null;

		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestRunJob.class, "trj");
			c.add(Restrictions.eq("trj.testRunJobId",testRunJobId));
			c.addOrder(Order.desc("trj.testRunJobId"));
			testRunJobLists=c.list();
			testRunJob = (testRunJobLists != null && testRunJobLists.size() != 0) ? (TestRunJob)(testRunJobLists.get(0)): null;				
			
			log.debug("TestRunJob successful");
		} catch (Exception re) {
			re.printStackTrace();
			log.error("TestRunJob failed", re);
		}
		TestRunPlan testRunPlan=null;
		if(testRunJob!=null){
			if(testRunJob.getWorkPackage()!=null){
				WorkPackage wp=testRunJob.getWorkPackage();
				Hibernate.initialize(wp);
				Hibernate.initialize(wp.getUserList());
				if(wp.getTestRunPlan()!=null){
					 testRunPlan=wp.getTestRunPlan();
					Hibernate.initialize(testRunPlan);
					Hibernate.initialize(testRunPlan.getAttachments());
					if( testRunPlan.getAttachments() != null &&  testRunPlan.getAttachments().size()>0){
						Set<Attachment> attachmentSet= testRunPlan.getAttachments();
						Hibernate.initialize(attachmentSet);
						for(Attachment attach: attachmentSet){
							Hibernate.initialize(attach);
						}
					}
					if(testRunPlan.getProductVersionListMaster()!=null){
						Hibernate.initialize(testRunPlan.getProductVersionListMaster());
						if(testRunPlan.getProductVersionListMaster().getProductMaster()!=null){
							Hibernate.initialize(testRunPlan.getProductVersionListMaster().getProductMaster());
							
								if(testRunPlan.getProductVersionListMaster().getProductMaster().getProductType()!=null){
									Hibernate.initialize(testRunPlan.getProductVersionListMaster().getProductMaster().getProductType());
								}
								
								if(testRunPlan.getProductVersionListMaster().getProductMaster().getCustomer()!=null){
									Hibernate.initialize(testRunPlan.getProductVersionListMaster().getProductMaster().getCustomer());
								}
							   	
						}
					}
				}
			}
			
			if(testRunJob.getGenericDevices()!=null){
				Hibernate.initialize(testRunJob.getGenericDevices());
			}//Changes for attaching testReports to TestSets - Bug 777
			if(testRunJob.getTestSuite()!= null){
				Hibernate.initialize(testRunJob.getTestSuite());
			}
			Hibernate.initialize(testRunJob.getTestSuiteSet());
			for(TestSuiteList tsl : testRunJob.getTestSuiteSet()){
				Hibernate.initialize(tsl);
				Hibernate.initialize(tsl.getTestCaseLists());
				for(TestCaseList tcl : tsl.getTestCaseLists()){
					Hibernate.initialize(tcl);
				}
			}
			if(testRunJob.getRunConfiguration() != null){
				Hibernate.initialize(testRunJob.getRunConfiguration());
				if(testRunJob.getRunConfiguration().getTestSuiteLists() != null ){
					Hibernate.initialize(testRunJob.getRunConfiguration().getTestSuiteLists());
					for(TestSuiteList tsl : testRunJob.getRunConfiguration().getTestSuiteLists()){
						if(tsl.getTestCaseLists()!=null){
							Hibernate.initialize(tsl.getTestCaseLists());
						}
					}
				}	
			}
			
			if(testRunJob.getTestRunStatus() != null) {
				Hibernate.initialize(testRunJob.getTestRunStatus());
			}
		}
		return testRunJob;
	}

	@Override
	@Transactional
	public WorkPackage addWorkpackageToClonedProductBuild(ProductBuild clonedProductBuild, UserList user, HttpServletRequest req,WorkPackage workPackageToBeCloned ) {
		WorkPackage newClonedWorkPackage = null;
		WorkFlowEvent workFlowEvent = null;
		if(workPackageToBeCloned != null){
			 newClonedWorkPackage = new WorkPackage();
				newClonedWorkPackage.setName(workPackageToBeCloned.getName());
				newClonedWorkPackage.setDescription(workPackageToBeCloned.getDescription());
				
				newClonedWorkPackage.setCreateDate(DateUtility.getCurrentTime());
				newClonedWorkPackage.setModifiedDate(DateUtility.getCurrentTime());
				newClonedWorkPackage.setStatus(1);
				newClonedWorkPackage.setIsActive(1);
				newClonedWorkPackage.setProductBuild(clonedProductBuild);
				
				newClonedWorkPackage.setTestRunPlan(workPackageToBeCloned.getTestRunPlan());
				newClonedWorkPackage.setTestRunPlanGroup(workPackageToBeCloned.getTestRunPlanGroup());
				
				newClonedWorkPackage.setSourceType("BuildCloned");
				newClonedWorkPackage.setWorkpackageCloneId(workPackageToBeCloned.getWorkPackageId());
				newClonedWorkPackage.setWorkPackageType(workPackageToBeCloned.getWorkPackageType());
				
				workFlowEvent = new WorkFlowEvent();
				workFlowEvent.setEventDate(DateUtility.getCurrentTime());
				workFlowEvent.setRemarks("New Workapckage Added :"+workPackageToBeCloned.getName());
				workFlowEvent.setUser(workPackageToBeCloned.getWorkFlowEvent().getUser());
				workFlowEvent.setWorkFlow(getWorkFlowByEntityIdStageId(IDPAConstants.WORKPACKAGE_ENTITY_ID,IDPAConstants.WORKFLOW_STAGE_ID_NEW));
				
				addWorkFlowEvent(workFlowEvent);
				newClonedWorkPackage.setWorkFlowEvent(workFlowEvent);
				newClonedWorkPackage.setPlannedEndDate(clonedProductBuild.getBuildDate());
				newClonedWorkPackage.setPlannedStartDate(clonedProductBuild.getBuildDate());
				newClonedWorkPackage.setIterationNumber(-1);
				LifeCyclePhase lifeCyclePhase = new LifeCyclePhase();
				lifeCyclePhase.setLifeCyclePhaseId(4);
				newClonedWorkPackage.setLifeCyclePhase(lifeCyclePhase);
				newClonedWorkPackage.setUserList(user);
				addWorkPackage(newClonedWorkPackage);
		}	
		return newClonedWorkPackage;
	}

	@Override
	@Transactional
	public EvidenceType getEvidenceTypeById(Integer evidenceTypeId) {
		log.debug("getting getEvidenceTypeById instance by id");
		
		List<EvidenceType> evidenceTypes=null;
		EvidenceType evidenceType=null;

		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(EvidenceType.class, "et");
			c.add(Restrictions.eq("et.evidenceTypeId",evidenceTypeId));
		
			evidenceTypes=c.list();
			evidenceType = (evidenceTypes != null && evidenceTypes.size() != 0) ? (EvidenceType)(evidenceTypes.get(0)): null;				
			
			log.debug("EvidenceType successful");
		} catch (Exception re) {
			re.printStackTrace();
			log.error("EvidenceType failed", re);
		}
		if(evidenceType!=null){
			
		}
		return evidenceType;
	}

	@Override
	@Transactional
	public List<EvidenceType> getEvidenceTypes() {
		List<EvidenceType> evidenceTypes=null;

		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(EvidenceType.class, "et");
		
			evidenceTypes=c.list();
			
			log.debug("EvidenceType successful");
		} catch (Exception re) {
			re.printStackTrace();
			log.error("EvidenceType failed", re);
		}
		
		return evidenceTypes;
	}

	@Override
	@Transactional
	public List<WorkPackage> getActiveWorkpackagesByProductBuildId(
			Integer productBuildId) {
		List<WorkPackage> workpackages=null;

		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackage.class, "wp");
			c.createAlias("wp.productBuild", "pb");
			c.createAlias("wp.workFlowEvent", "wfe");
			c.createAlias("wfe.workFlow", "wf");
			c.createAlias("wf.entityMaster", "em");

			c.add(Restrictions.eq("pb.productBuildId",productBuildId));
			c.add(Restrictions.eq("em.entitymasterid",2));
			
			c.add(Restrictions.in("wf.stageId",Arrays.asList(1,2,3,4,5)));
			
			workpackages=c.list();
			
			log.debug("EvidenceType successful");
		} catch (Exception re) {
			re.printStackTrace();
			log.error("EvidenceType failed", re);
		}
		
		return workpackages;
	}
	
	@Override
	@Transactional
	public int totalWorkPackageDemandProjectionCountByWpId(
			int workPackageId, int shiftId, Date date) {
		log.debug("getting totalWorkPackageDemandProjectionCountByWpId");
		int count = 0;
		try {
			count=((Number) sessionFactory.getCurrentSession().createSQLQuery("select count(*) from workpackage_demand_projection wptcep where workPackageId=:workPackageId and shiftId=:shiftId and workDate=:date")
					.setParameter("workPackageId", workPackageId)
					.setParameter("shiftId",shiftId)
					.setParameter("date",date).uniqueResult()).intValue();

			log.debug("total records fetch successful");
		} catch (RuntimeException re) {
			log.error("total records fetch failed", re);
		}
		return count;
	}

	@Override
	@Transactional
	public WorkPackage getWorkPackageByIdWithLoad(int workPackageId) {
		WorkPackage workPackage = null;
		try {
			workPackage = (WorkPackage)sessionFactory
					.getCurrentSession().load(WorkPackage.class,workPackageId); //proxy
		} catch (RuntimeException re) {
			log.error("get workpackage by Id failed", re);
			re.printStackTrace();
		}catch (Exception e) {
			log.error("Exception occurred while getting the work package object for Id: "+workPackageId, e);
			e.printStackTrace();
		}
		return workPackage;

	}

	@Override
	@Transactional
	public void cloneBuildPlan(int workPackageIdExisting,WorkPackage clonedWorkpackage,UserList user) {
		int clonedWorkpackageId=clonedWorkpackage.getWorkPackageId();
		
		Session session =sessionFactory.getCurrentSession();
		
		
		//feature plan starts
		session.createSQLQuery("CREATE TEMPORARY TABLE temptable ENGINE=MEMORY SELECT * FROM workpackage_feature WHERE workpackageId="+workPackageIdExisting).executeUpdate();
		session.createSQLQuery("UPDATE temptable SET workpackageFeatureId=0,workpackageId="+clonedWorkpackageId).executeUpdate();
		session.createSQLQuery("INSERT INTO  workpackage_feature SELECT * FROM temptable").executeUpdate();
		session.createSQLQuery("DROP TABLE temptable").executeUpdate();

		session.createSQLQuery("CREATE TEMPORARY TABLE temptable ENGINE=MEMORY SELECT * FROM workpackage_has_feature WHERE workpackageId="+workPackageIdExisting).executeUpdate();
		session.createSQLQuery("UPDATE temptable SET workpackageId="+clonedWorkpackageId).executeUpdate();
		session.createSQLQuery("INSERT INTO  workpackage_has_feature SELECT * FROM temptable").executeUpdate();
		session.createSQLQuery("DROP TABLE temptable").executeUpdate();

		//feature plan ends
		
		//testsuite plan starts
		session.createSQLQuery("CREATE TEMPORARY TABLE temptable ENGINE=MEMORY SELECT * FROM workpackage_test_suite WHERE workpackageId="+workPackageIdExisting).executeUpdate();
		session.createSQLQuery("UPDATE temptable SET workpackageTestSuiteId=0,workpackageId="+clonedWorkpackageId).executeUpdate();
		session.createSQLQuery("INSERT INTO  workpackage_test_suite SELECT * FROM temptable").executeUpdate();
		session.createSQLQuery("DROP TABLE temptable").executeUpdate();

		session.createSQLQuery("CREATE TEMPORARY TABLE temptable ENGINE=MEMORY SELECT * FROM workpackage_has_testsuite WHERE workpackageId="+workPackageIdExisting).executeUpdate();
		session.createSQLQuery("UPDATE temptable SET workpackageId="+clonedWorkpackageId).executeUpdate();
		session.createSQLQuery("INSERT INTO  workpackage_has_testsuite SELECT * FROM temptable").executeUpdate();
		session.createSQLQuery("DROP TABLE temptable").executeUpdate();

		//testsuite plan ends
		
		//testcase plan starts
		session.createSQLQuery("CREATE TEMPORARY TABLE temptable ENGINE=MEMORY SELECT * FROM work_package_testcases WHERE workpackageId="+workPackageIdExisting).executeUpdate();
		session.createSQLQuery("UPDATE temptable SET id=0,workpackageId="+clonedWorkpackageId).executeUpdate();
		session.createSQLQuery("INSERT INTO  work_package_testcases SELECT * FROM temptable").executeUpdate();
		session.createSQLQuery("DROP TABLE temptable").executeUpdate();

		session.createSQLQuery("CREATE TEMPORARY TABLE temptable ENGINE=MEMORY SELECT * FROM workpackage_has_testcase WHERE workpackageId="+workPackageIdExisting).executeUpdate();
		session.createSQLQuery("UPDATE temptable SET workpackageId="+clonedWorkpackageId).executeUpdate();
		session.createSQLQuery("INSERT INTO  workpackage_has_testcase SELECT * FROM temptable").executeUpdate();
		session.createSQLQuery("DROP TABLE temptable").executeUpdate();

		//testcase plan ends

		//plan event starts
		WorkFlow workFlow=getWorkFlowByEntityIdStageId(IDPAConstants.WORKPACKAGE_ENTITY_ID, IDPAConstants.WORKFLOW_STAGE_ID_PLANNING);
		WorkFlowEvent workFlowEvent = new WorkFlowEvent();
		
		workFlowEvent.setEventDate(DateUtility.getCurrentTime());
		workFlowEvent.setRemarks("Planning Workapckage:"+clonedWorkpackage.getName());
		workFlowEvent.setUser(user);
		workFlowEvent.setWorkFlow(workFlow);
		addWorkFlowEvent(workFlowEvent);

		clonedWorkpackage.setWorkFlowEvent(workFlowEvent);
		updateWorkPackage(clonedWorkpackage);
		//plan event ends
		
	}
	@Override
	@Transactional
	public void cloneBuildExecution(int workPackageIdExisting,WorkPackage clonedWorkpackage,UserList user) {
	
		
		int clonedWorkpackageId=clonedWorkpackage.getWorkPackageId();

		//runconfiguration starts
		log.info("workPackageIdExisting:"+workPackageIdExisting);
		log.info("clonedWorkpackageId:"+clonedWorkpackageId);
		
		
		Session session =sessionFactory.getCurrentSession();

		//runconfiguration starts

		session.createSQLQuery("CREATE TEMPORARY TABLE TEMPTABLE ENGINE=MEMORY SELECT * FROM runconfiguration WHERE workpackageId="+workPackageIdExisting).executeUpdate();
		SQLQuery query=session.createSQLQuery("UPDATE TEMPTABLE SET runconfigId=0,workpackageId="+clonedWorkpackageId);
		query.executeUpdate();
		
		session.createSQLQuery("INSERT INTO  runconfiguration SELECT * FROM TEMPTABLE").executeUpdate();
		session.createSQLQuery("DROP TABLE TEMPTABLE").executeUpdate();
		
	
		List<RunConfiguration> runConfigurations= getRunConfigurationByWPId(clonedWorkpackageId);

		//runconfiguration ends
		
		WorkpackageRunConfiguration wpRunConfiguration=null;
		log.info("runConfigurations:"+runConfigurations.size());
		for (RunConfiguration runConfiguration : runConfigurations) {
			wpRunConfiguration=  addRunConfigurationWorkpackage(clonedWorkpackageId,runConfiguration.getRunconfigId(),"feature");

			TestRunJob testRunJob=getTestRunJobByWP(clonedWorkpackage, runConfiguration);
			TestRunJob testRunJobExisting=getTestRunJobByWPAndRunConfigName(workPackageIdExisting, runConfiguration.getRunconfigName());
			log.info(+workPackageIdExisting + " for this work package and for "+ runConfiguration.getRunconfigName() +"This run configuration, test run job is "+testRunJobExisting);
			log.info("TestRunJobExisting    == "+testRunJobExisting);
			if(testRunJob==null){
				addTestRunJob(runConfiguration, null, clonedWorkpackage, null);
 				testRunJob=getTestRunJobByWP(clonedWorkpackage, runConfiguration);
 				if(testRunJob!=null){
					if(testRunJob.getTestRunJobId()!=null)
					mongoDBService.addTestRunJobToMongoDB(testRunJob.getTestRunJobId());
				}
			}
			session.createSQLQuery("CREATE TEMPORARY TABLE temptable ENGINE=MEMORY SELECT * FROM testrunjob_has_feature WHERE testRunJobId="+testRunJobExisting.getTestRunJobId()).executeUpdate();
			(session.createSQLQuery("UPDATE temptable SET testRunJobId="+testRunJob.getTestRunJobId())).executeUpdate();
			session.createSQLQuery("INSERT INTO  testrunjob_has_feature SELECT * FROM temptable").executeUpdate();
			session.createSQLQuery("DROP TABLE temptable").executeUpdate();

			session.createSQLQuery("CREATE TEMPORARY TABLE temptable ENGINE=MEMORY SELECT * FROM workpackage_feature_execution_plan WHERE workPackageId="+workPackageIdExisting +" and testRunJobId="+testRunJobExisting.getTestRunJobId() ).executeUpdate();
			log.info("Date"+DateUtility.dateToStringInSecond(clonedWorkpackage.getPlannedStartDate())+"plann exxxxx===?"+DateUtility.sdfDateformatWithOutTime(clonedWorkpackage.getPlannedStartDate()));
			session.createSQLQuery("UPDATE temptable SET wppfepId=0, testRunJobId="+testRunJob.getTestRunJobId()+", workPackageId="+clonedWorkpackageId + ", testRunConfigurationId="+wpRunConfiguration.getWorkpackageRunConfigurationId()).executeUpdate();

			session.createSQLQuery("INSERT INTO  workpackage_feature_execution_plan SELECT * FROM temptable").executeUpdate();
			session.createSQLQuery("DROP TABLE temptable").executeUpdate();
			
			session.createSQLQuery("CREATE TEMPORARY TABLE temptable ENGINE=MEMORY SELECT * FROM testrunjob_has_testsuite WHERE testRunJobId="+testRunJobExisting.getTestRunJobId()).executeUpdate();
			session.createSQLQuery("UPDATE temptable SET testRunJobId="+testRunJob.getTestRunJobId()).executeUpdate();
			session.createSQLQuery("INSERT INTO  testrunjob_has_testsuite SELECT * FROM temptable").executeUpdate();
			session.createSQLQuery("DROP TABLE temptable").executeUpdate();

			session.createSQLQuery("CREATE TEMPORARY TABLE temptable ENGINE=MEMORY SELECT * FROM workpackage_testsuite_execution_plan WHERE workPackageId="+workPackageIdExisting +" and testRunJobId="+testRunJobExisting.getTestRunJobId() ).executeUpdate();
			session.createSQLQuery("UPDATE temptable SET wptspId=0, testRunJobId="+testRunJob.getTestRunJobId()+", workPackageId="+clonedWorkpackageId + ", testRunConfigurationId="+wpRunConfiguration.getWorkpackageRunConfigurationId()).executeUpdate();
			session.createSQLQuery("INSERT INTO  workpackage_testsuite_execution_plan SELECT * FROM temptable").executeUpdate();
			session.createSQLQuery("DROP TABLE temptable").executeUpdate();
			
			session.createSQLQuery("CREATE TEMPORARY TABLE temptable ENGINE=MEMORY SELECT * FROM workpackage_testcase_execution_plan WHERE workPackageId="+workPackageIdExisting +" and testRunJobId="+testRunJobExisting.getTestRunJobId() ).executeUpdate();
			session.createSQLQuery("UPDATE temptable SET wptcepId=0, testRunJobId="+testRunJob.getTestRunJobId()+", workPackageId="+clonedWorkpackageId + ", testRunConfigurationId="+wpRunConfiguration.getWorkpackageRunConfigurationId()+",executionStatus=3").executeUpdate();
			session.createSQLQuery("INSERT INTO  workpackage_testcase_execution_plan SELECT * FROM temptable").executeUpdate();
			session.createSQLQuery("DROP TABLE temptable").executeUpdate();

			session.createSQLQuery("CREATE TEMPORARY TABLE temptable ENGINE=MEMORY SELECT wptcepId,result,defectsCount,defectIds,startTime,comments," +
					"isReviewed,isApproved,observedOutput,executionTime,failureReason,endTime FROM testcase_execution_result r RIGHT OUTER JOIN " +
					"workpackage_testcase_execution_plan w ON w.wptcepId=r.testCaseExecutionResultId WHERE w.workPackageId="+clonedWorkpackageId +" AND  w.testRunJobId="+testRunJobExisting.getTestRunJobId()).executeUpdate();
			session.createSQLQuery(" INSERT INTO  testcase_execution_result SELECT * FROM temptable").executeUpdate();
			session.createSQLQuery("DROP TABLE temptable").executeUpdate();
			
			List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlanList= 
					session.createQuery("from WorkPackageTestCaseExecutionPlan wptcep WHERE wptcep.workPackage.workPackageId="+clonedWorkpackageId +" and wptcep.testRunJob.testRunJobId="+testRunJob.getTestRunJobId()).list();
			log.info("workPackageTestCaseExecutionPlanList size:"+workPackageTestCaseExecutionPlanList.size());
			for (WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan : workPackageTestCaseExecutionPlanList) {
				ArrayList<TestStepExecutionResult> testStepExecutionResultList=new ArrayList<TestStepExecutionResult>();
				TestStepExecutionResult testStepExecutionResult=null;
				int i = workPackageTestCaseExecutionPlan.getTestCase().getTestCaseId().intValue();
				List<TestCaseStepsList> listTestCaseSteps=testCaseStepsListDAO.list(i);
				for(TestCaseStepsList testcasestep:listTestCaseSteps){
					testStepExecutionResult = new TestStepExecutionResult();
					testStepExecutionResult.setComments("");
					testStepExecutionResult.setIsApproved(0);
					testStepExecutionResult.setIsReviewed(0);
					testStepExecutionResult.setObservedOutput("");
					testStepExecutionResult.setResult("");
					testStepExecutionResult.setTestCaseExecutionResult(workPackageTestCaseExecutionPlan.getTestCaseExecutionResult());
					testStepExecutionResult.setTestcase(workPackageTestCaseExecutionPlan.getTestCase());
					testStepExecutionResult.setTestSteps(testcasestep);
					sessionFactory.getCurrentSession().save(testStepExecutionResult);
				}
				
				TestCaseConfiguration testCaseConfiguration =new TestCaseConfiguration();
				testCaseConfiguration.setDevice_combination_id(null);
				testCaseConfiguration.setWorkpackageRunConfiguration(workPackageTestCaseExecutionPlan.getRunConfiguration());
				testCaseConfiguration.setWorkpackage_run_list(workPackageTestCaseExecutionPlan);
				addTestCaseConfiguration(testCaseConfiguration);
			}
		}
	}

	@Override
	@Transactional
	public TestRunJob getTestRunJobByWPAndRunConfigName(
			int workPackageIdExisting, String runconfigName) {

		log.debug("getting getTestRunJobByWP instance by id");
		TestRunJob testRunJob = null;
		List<TestRunJob> testRunJobList=null;
		try {

			
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestRunJob.class, "trj");
			
			
			c.createAlias("trj.workPackage", "wp");
			c.createAlias("trj.runConfiguration", "rc");
			c.add(Restrictions.eq("wp.workPackageId",workPackageIdExisting));
			c.add(Restrictions.eq("rc.runconfigName",runconfigName));
			testRunJobList=c.list();
			testRunJob = (testRunJobList != null && testRunJobList.size() != 0) ? (TestRunJob) testRunJobList
					.get(0) : null;
			if (testRunJob != null) {
				Hibernate.initialize(testRunJob.getWorkPackage());
				Hibernate.initialize(testRunJob.getTestSuite());
				if(testRunJob.getTestSuite()!=null)
					Hibernate.initialize(testRunJob.getTestSuite().getTestCaseLists());
				Hibernate.initialize(testRunJob.getTestCaseListSet());
				Hibernate.initialize(testRunJob.getTestSuiteSet());
				Hibernate.initialize(testRunJob.getFeatureSet());

				Hibernate.initialize(testRunJob.getRunConfiguration());

			}
			log.debug("TestRunJob successful");
		} catch (Exception re) {
			re.printStackTrace();
			log.error("TestRunJob failed", re);
			// throw re;
		}
		return testRunJob;
	
	}
	
	
	@Override
	@Transactional
	public List<RunConfiguration> getRunConfigurationByWPId(Integer workpackageId) {
		List<RunConfiguration> runConfigurations = null;

		RunConfiguration runConfiguration = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(RunConfiguration.class, "rc");
			c.createAlias("rc.workPackage", "wp");
			c.add(Restrictions.eq("wp.workPackageId",workpackageId));
			
			runConfigurations=c.list();			
			
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return runConfigurations;	
	}

	@Override
	@Transactional
	public void mapTestRunJobTestSuiteTestCase(int testRunJobId,
			int testSuiteId, List<TestCaseList> testCaseLists, String type) {

		if (testCaseLists == null)
			return;
		
		if(type.equals("Add")){
			for (TestCaseList testCaseList : testCaseLists) {
				TestRunJobTestSuiteHasTestCase testRunJobTestSuiteHasTestCase = new TestRunJobTestSuiteHasTestCase();
				testRunJobTestSuiteHasTestCase.setTestRunJob(environmentDAO.getTestRunJobById(testRunJobId));
				TestSuiteList testSuiteList =testSuiteListDAO.getByTestSuiteId(testSuiteId);
				testRunJobTestSuiteHasTestCase.setTestSuiteList(testSuiteList);
				testRunJobTestSuiteHasTestCase.setTestCaseList(testCaseList);
				sessionFactory.getCurrentSession().save(testRunJobTestSuiteHasTestCase);
			}
		}		
	}

	
	@Override
	@Transactional
	public List<TestCaseList> getSelectedTestCasesFromTestRunJob(
			int testRunListId) {
		List<TestCaseList> testCaseLists = new ArrayList<TestCaseList>();
		List<TestRunJobTestSuiteHasTestCase> testRunJobTestSuiteHasTestCaseList = new ArrayList<TestRunJobTestSuiteHasTestCase>();

		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestRunJobTestSuiteHasTestCase.class, "trjtstc");
			c.createAlias("trjtstc.testRunJob", "trj");
			c.createAlias("trjtstc.testCaseList", "tcl");
			c.add(Restrictions.eq("trj.testRunJobId",testRunListId));
			c.addOrder(Order.asc("tcl.testCaseExecutionOrder"));
			c.addOrder(Order.asc("tcl.testCaseId"));
			testRunJobTestSuiteHasTestCaseList=c.list();			
			for (TestRunJobTestSuiteHasTestCase testRunJobTestSuiteHasTestCase : testRunJobTestSuiteHasTestCaseList) {
				Hibernate.initialize(testRunJobTestSuiteHasTestCase.getTestCaseList());
				testCaseLists.add(testRunJobTestSuiteHasTestCase.getTestCaseList());
				log.info("Job Test Case Id : "+testRunJobTestSuiteHasTestCase.getTestCaseList().getTestCaseId() +" Test Case Name : "+testRunJobTestSuiteHasTestCase.getTestCaseList().getTestCaseName());
			}
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return testCaseLists;	
	}

	@Override
	@Transactional
	public TestExecutionResultBugList createBug(TestExecutionResultBugList bug) {

		try {
			addTestCaseDefect(bug);
			bug=checkIfBugIsDuplicate(bug);
			addTestCaseDefect(bug);
			return bug;
		} catch (Exception e) {
			log.error("Problem creating bug ", e);
			return bug;
		}
	}
	
	private TestExecutionResultBugList checkIfBugIsDuplicate(TestExecutionResultBugList bug) {
			
		try {
			//Get the bugs created against the testcasestep earlier
			List<TestExecutionResultBugList> similarBugs = getBugsSameAsThisBug(bug, sessionFactory);
			//If no bugs are there, no action needed
			if (similarBugs == null || similarBugs.isEmpty()) {
				return bug;
			}
			boolean isDuplicateBugInTAF = false;
			boolean bugAlreadyReportedInDefectSystem = false;
			String bugCode = null;
			//Find if the bugs are similar and take action
			for (TestExecutionResultBugList similarBug : similarBugs) {
				
				//Check if the bug is actually a duplicate
				if (similarBug.getBugTitle().equalsIgnoreCase(bug.getBugTitle())) {
					if (similarBug.getBugDescription()!=null && bug.getBugDescription()!=null && similarBug.getBugDescription().equalsIgnoreCase(bug.getBugDescription())) {
						if (similarBug.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan() == null ||
								similarBug.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getRunConfiguration() == null ||
									similarBug.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration() == null ||
										similarBug.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration().getGenericDevice() == null ||
												similarBug.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration().getGenericDevice().getUDID() == null) {
							continue;
						}
						if (similarBug.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration().getGenericDevice().getUDID()
								.equals(bug.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration().getGenericDevice().getUDID())) {
							if (bug.getRemarks() == null)
								bug.setRemarks("");
							if (!isDuplicateBugInTAF){
								bug.setRemarks(bug.getRemarks() + "\n This is a duplicate of an existing bug (In TAF), with ID : " + bug.getTestExecutionResultBugId());
								isDuplicateBugInTAF = true;
							}
							if (!(similarBug.getBugManagementSystemBugId() == null || similarBug.getBugManagementSystemBugId().isEmpty())) {
								bugAlreadyReportedInDefectSystem = true;
								bugCode = similarBug.getBugManagementSystemBugId();
							}
						}
					}
				}
			}
			if (bugAlreadyReportedInDefectSystem) {
				bug.setRemarks(bug.getRemarks() + "\n It has already been filed in the defect management system (" + bugCode + ")");
			}
			return bug;
		} catch (Exception e) {
			log.error("Problem locating duplicate bugs for Bug : " + bug.getTestExecutionResultBugId(), e);
			return bug;
		}
	}
	
	public List<TestExecutionResultBugList> getBugsSameAsThisBug(TestExecutionResultBugList bug, SessionFactory sessionFactory) {
		
		List<TestExecutionResultBugList> bugs=null;
		try {
			bugs=sessionFactory.getCurrentSession().createQuery("from TestExecutionResultBugList e where e.testStepExecutionResult.testSteps.testStepId=:testStepId")
					.setParameter("testStepId", bug.getTestStepExecutionResult().getTestSteps().getTestStepId())
					.list();
			if (bugs!=null && !bugs.isEmpty()) {
				for (TestExecutionResultBugList b : bugs) {
					
					if(b.getTestCaseExecutionResult()!=null && b.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan()!=null && b.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getRunConfiguration()!= null && b.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration()!=null && b.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration().getGenericDevice()!=null){
						Hibernate.initialize(b.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration().getGenericDevice().getUDID());
					}
				}
				log.debug("Total bugs similar to the this bug are : " + bugs.size());
			}
		} catch (Exception e) {
			
			log.error("Problem in finding similar bugs", e);			
		}
		return bugs;
	}

	@Override
	@Transactional
	public List<TestExecutionResultBugList> getTestRunJobDefects(int testRunJobId) {
		List<TestExecutionResultBugList> testExecutionResultBugList = new ArrayList<TestExecutionResultBugList>();

		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestExecutionResultBugList.class, "tebl");
			c.createAlias("tebl.testCaseExecutionResult", "tcer");
			c.createAlias("tcer.workPackageTestCaseExecutionPlan", "wptcep");
			c.createAlias("wptcep.testRunJob", "trj");

			c.add(Restrictions.eq("trj.testRunJobId",testRunJobId));
			
			testExecutionResultBugList=c.list();			
			if(testExecutionResultBugList!=null &&!testExecutionResultBugList.isEmpty()){
				for (TestExecutionResultBugList terb : testExecutionResultBugList) {
					Hibernate.initialize(terb.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getWorkPackage().getProductBuild().getProductVersion().getProductMaster());
					Hibernate.initialize(terb.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration());
					Hibernate.initialize(terb.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration().getGenericDevice());
					Hibernate.initialize(terb.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration().getGenericDevice().getPlatformType());
					Hibernate.initialize(terb.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getTestCase());
					Hibernate.initialize(terb.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getTestCase().getTestCasePriority());

					Hibernate.initialize(terb.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getTestCaseExecutionResult().getTestStepExecutionResultSet());
					Set<TestStepExecutionResult> testStepExecutionResults = terb.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getTestCaseExecutionResult().getTestStepExecutionResultSet();
					for (TestStepExecutionResult testStepExecutionResult : testStepExecutionResults) {
						Hibernate.initialize(testStepExecutionResult.getTestSteps());
					}

				}
			}
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
			// throw re;
		}
		return testExecutionResultBugList;	
	}

	
	@Override
	@Transactional
	public List<MetricsMasterDTO> getWorkpackageTestcaseExecutionPlanByDateFilter(int status,Date startDate,
			Date endDate) {
		List<MetricsMasterDTO> listOfWrokpackageTestcaseplan = new ArrayList<MetricsMasterDTO>();
	try{
		Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackageTestCaseExecutionPlan.class, "wptcep");
		c.createAlias("wptcep.workPackage", "wp");
		c.createAlias("wp.productBuild", "pb");
		c.createAlias("pb.productVersion", "pv");
		c.createAlias("pv.productMaster", "product");
		c.add(Restrictions.between("plannedExecutionDate", startDate,  endDate));
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Property.forName("product.productId").as("productId"));
		projectionList.add(Property.forName("product.productName").as("productName"));
		projectionList.add(Property.forName("wp.workPackageId").as("workPackageId"));
		projectionList.add(Property.forName("wp.name").as("workPackageName"));
		projectionList.add(Property.forName("wptcep.executionStatus").as("executionStatusId"));
		projectionList.add(Projections.count("wptcep.id").as("testCaseCount"));
		projectionList.add(Projections.groupProperty("wp.workPackageId"));
		projectionList.add(Projections.groupProperty("wptcep.executionStatus"));
		c.setProjection(projectionList);
		
		List<Object[]> list = c.list();
		log.info("Result Set Size : " + list.size());
		MetricsMasterDTO metricsMasterDTO = null;
		for (Object[] row : list) {
			metricsMasterDTO = new MetricsMasterDTO();
			metricsMasterDTO.setProductId(((Integer)row[0]).intValue());
			metricsMasterDTO.setProductName(((String)row[1]).toString());
			metricsMasterDTO.setWorkPackageId(((Integer)row[2]).intValue());
			metricsMasterDTO.setWorkPackageName(((String)row[3]).toString());
			Integer exeStatus=((Integer)row[4]).intValue();
			metricsMasterDTO.setExecutionStatus(exeStatus);
			if(exeStatus==IDPAConstants.TESTCASE_EXECUTION_STATUS_COMPLETED)
			{
				metricsMasterDTO.setTestCaseCompletedCount(((Long)row[5]).intValue());
			}
			if(exeStatus==IDPAConstants.TESTCASE_EXECUTION_STATUS_ASSIGNED)
			{
				metricsMasterDTO.setTestCaseAllocatedCount(((Long)row[5]).intValue());
			}
			if(exeStatus==IDPAConstants.TESTCASE_EXECUTION_STATUS_NOT_STARTED)
			{
				metricsMasterDTO.setTestCaseNotStartedCount(((Long)row[5]).intValue());
			}
			listOfWrokpackageTestcaseplan.add(metricsMasterDTO);
			log.debug("Status Summary for Product Id : "  +((Integer)row[0]).intValue() +"  Name: "+((String)row[1]).toString() +"  Work package Id : "  +((Integer)row[2]).intValue() +"  Name: "+((String)row[3]).toString() +"  Status : "+((Integer)row[4]).intValue()+"   TotalTestCaseForAllocateCount : " + ((Long)row[5]).intValue());
		}
		} catch (Exception e) {
			log.error("update failed", e);
			return null;
		}
		
		log.debug("list all sucessfull: "+listOfWrokpackageTestcaseplan.size());
	
	
	return listOfWrokpackageTestcaseplan;
		
	}


	@Override
	@Transactional
	public List<MetricsMasterDefectsDTO> getTotalBugListByStatus(
			Integer status) {
		List<MetricsMasterDefectsDTO> listTestExecutionResultBugList = new ArrayList<MetricsMasterDefectsDTO>();
		try{

			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestExecutionResultBugList.class, "terbl");
			c.createAlias("terbl.testCaseExecutionResult", "tcer");
			c.createAlias("tcer.workPackageTestCaseExecutionPlan", "wptcep");
			c.createAlias("wptcep.workPackage", "wp");
			c.createAlias("wp.productBuild", "productBuild");
			c.createAlias("productBuild.productVersion", "productVersion");
			c.createAlias("productVersion.productMaster", "product");
			c.createAlias("terbl.bugFilingStatus", "wkflow");
			
			
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Property.forName("product.productId").as("productId"));
			projectionList.add(Property.forName("product.productName").as("productName"));
			projectionList.add(Property.forName("wp.workPackageId").as("workPackageId"));
			projectionList.add(Property.forName("wp.name").as("workPackageName"));
			projectionList.add(Property.forName("wkflow.workFlowId").as("bugFilingStatus"));
			projectionList.add(Projections.count("terbl.testExecutionResultBugId").as("bugCount"));
			projectionList.add(Projections.groupProperty("product.productId"));
			projectionList.add(Projections.groupProperty("wkflow.workFlowId"));
			c.setProjection(projectionList);
			
			List<Object[]> list = c.list();
			log.debug("Result Set Size : " + list.size());
			MetricsMasterDefectsDTO metricsMasterDefectsDTO = null;
			for (Object[] row : list) {
				metricsMasterDefectsDTO = new MetricsMasterDefectsDTO();
				metricsMasterDefectsDTO.setProductId(((Integer)row[0]).intValue());
				metricsMasterDefectsDTO.setProductName(((String)row[1]).toString());
				metricsMasterDefectsDTO.setWorkPackageId(((Integer)row[2]).intValue());
				metricsMasterDefectsDTO.setWorkPackageName(((String)row[3]).toString());
				
				Integer bugFilingStatus=((Integer)row[4]).intValue();
				metricsMasterDefectsDTO.setBugFilingStatus(bugFilingStatus);
				
				if(bugFilingStatus==IDPAConstants.TESTCASE_DEFECTS_STATUS_NEW)
				{
					metricsMasterDefectsDTO.setDefectsBugfilingStatusNewCount(((Long)row[5]).intValue());
				}
				if(bugFilingStatus==IDPAConstants.TESTCASE_DEFECTS_STATUS_REFERBACK)
				{
					metricsMasterDefectsDTO.setDefectsBugfilingStatusreferbackCount(((Long)row[5]).intValue());
				}
				if(bugFilingStatus==IDPAConstants.TESTCASE_DEFECTS_STATUS_REVIEWED)
				{
					metricsMasterDefectsDTO.setDefectsBugfilingStatusreviewedCount(((Long)row[5]).intValue());
				}
				if(bugFilingStatus==IDPAConstants.TESTCASE_DEFECTS_STATUS_APPROVED)
				{
					metricsMasterDefectsDTO.setDefectsBugfilingStatusapprovedCount(((Long)row[5]).intValue());
				}
				if(bugFilingStatus==IDPAConstants.TESTCASE_DEFECTS_STATUS_CLOSED)
				{
					metricsMasterDefectsDTO.setDefectsBugfilingStatusClosedCount(((Long)row[5]).intValue());
				}
				if(bugFilingStatus==IDPAConstants.TESTCASE_DEFECTS_STATUS_DUPLICATE)
				{
					metricsMasterDefectsDTO.setDefectsBugfilingStatusDuplicateCount(((Long)row[5]).intValue());
				}
				if(bugFilingStatus==IDPAConstants.TESTCASE_DEFECTS_STATUS_FIXED)
				{
					metricsMasterDefectsDTO.setDefectsBugfilingStatusFixedCount(((Long)row[5]).intValue());
				}
				if(bugFilingStatus==IDPAConstants.TESTCASE_DEFECTS_STATUS_VIRIFIED)
				{
					metricsMasterDefectsDTO.setDefectsBugfilingStatusVerifiedCount(((Long)row[5]).intValue());
				}
				if(bugFilingStatus==IDPAConstants.TESTCASE_DEFECTS_STATUS_INTENDED_BEHAVIOUR)
				{
					metricsMasterDefectsDTO.setDefectsBugfilingStatusIntendedBehaviourCount(((Long)row[5]).intValue());
				}
				if(bugFilingStatus==IDPAConstants.TESTCASE_DEFECTS_STATUS_NOT_REPRODUCIBLE)
				{
					metricsMasterDefectsDTO.setDefectsBugfilingStatusNotReproducibleCount(((Long)row[5]).intValue());
				}
				
				listTestExecutionResultBugList.add(metricsMasterDefectsDTO);
				log.info("Status Summary for Product Id : "  +((Integer)row[0]).intValue() +"  Name: "+((String)row[1]).toString() +"  Work package Id : "  +((Integer)row[2]).intValue() +"  Name: "+((String)row[3]).toString() +"  Status : "+((Integer)row[4]).intValue()+"   TotalDefects : " + ((Long)row[5]).intValue());
			}
			
		
		
		log.debug("list all sucessfull listTestExecutionResultBugList size"+listTestExecutionResultBugList.size());
		}catch (RuntimeException re) {
			log.error("list all failed", re);
			
		}
		
		return listTestExecutionResultBugList;
	}


	@Override
	@Transactional
	public List<MetricsMasterTestCaseResultDTO> getTestcaseExecutionResultList() {
		List<MetricsMasterTestCaseResultDTO> listTestCaseResult = new ArrayList<MetricsMasterTestCaseResultDTO>();
		try{
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestCaseExecutionResult.class, "tcer");
			c.createAlias("tcer.workPackageTestCaseExecutionPlan", "wptcep");
			c.createAlias("wptcep.workPackage", "wp");
			c.createAlias("wp.productBuild", "productBuild");
			c.createAlias("productBuild.productVersion", "productVersion");
			c.createAlias("productVersion.productMaster", "product");
			
			
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Property.forName("product.productId").as("productId"));
			projectionList.add(Property.forName("product.productName").as("productName"));
			projectionList.add(Property.forName("wp.workPackageId").as("workPackageId"));
			projectionList.add(Property.forName("wp.name").as("workPackageName"));
			projectionList.add(Property.forName("tcer.result").as("result"));
			projectionList.add(Projections.count("tcer.testCaseExecutionResultId").as("resultCount"));
			projectionList.add(Projections.groupProperty("product.productId"));
			projectionList.add(Projections.groupProperty("tcer.result"));
			c.setProjection(projectionList);
			
			List<Object[]> list = c.list();
			log.info("Result Set Size : " + list.size());
			MetricsMasterTestCaseResultDTO metricsMasterTestCaseResultDTO = null;
			for (Object[] row : list) {
				metricsMasterTestCaseResultDTO = new MetricsMasterTestCaseResultDTO();
				metricsMasterTestCaseResultDTO.setProductId(((Integer)row[0]).intValue());
				metricsMasterTestCaseResultDTO.setProductName(((String)row[1]).toString());
				metricsMasterTestCaseResultDTO.setWorkPackageId(((Integer)row[2]).intValue());
				metricsMasterTestCaseResultDTO.setWorkPackageName(((String)row[3]).toString());
				
				String result=((String)row[4]);
				if((result!="")&&(result!=null)){
				metricsMasterTestCaseResultDTO.setResult(result);
				
				if(result.equals(IDPAConstants.TESTCASE_PASSED)&&(result!=null))
				{
					metricsMasterTestCaseResultDTO.setTestCasePassedCount(((Long)row[5]).intValue());
				}
				if(result.equals(IDPAConstants.TESTCASE_FAILED)&&(result!=null))
				{
					metricsMasterTestCaseResultDTO.setTestCaseFailedCount(((Long)row[5]).intValue());
				}
				if(result.equals(IDPAConstants.TESTCASE_BLOCKED)&&(result!=null))
				{
					metricsMasterTestCaseResultDTO.setTestCaseBlockedCount(((Long)row[5]).intValue());
				}
				if(result.equals(IDPAConstants.TESTCASE_NORUN)&&(result!=null))
				{
					metricsMasterTestCaseResultDTO.setTestCaseNorunCount(((Long)row[5]).intValue());
				}
				}
				listTestCaseResult.add(metricsMasterTestCaseResultDTO);
			}
		log.info("list all sucessfull listTestExecutionResultBugList size"+listTestCaseResult.size());
		}catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return listTestCaseResult;
	}
	@Override
	@Transactional
	public List<MetricsMasterDTO>  getWorkpackageTestcaseExecutionPlanForResourceByDateFilter(
			int i, Date startDate, Date currentDate) {
	List<MetricsMasterDTO> listOfWrokpackageTestcaseplan = new ArrayList<MetricsMasterDTO>();
	try{
		Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackageTestCaseExecutionPlan.class, "wptcep");
		c.createAlias("wptcep.workPackage", "wp");
		c.createAlias("wp.productBuild", "pb");
		c.createAlias("pb.productVersion", "pv");
		c.createAlias("pv.productMaster", "product");
		c.createAlias("wptcep.tester", "user");
	
		c.add(Restrictions.between("plannedExecutionDate", startDate,  currentDate));
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Property.forName("product.productId").as("productId"));
		projectionList.add(Property.forName("product.productName").as("productName"));
	
		projectionList.add(Property.forName("user.userId").as("userId"));
		projectionList.add(Property.forName("user.loginId").as("loginName"));
		projectionList.add(Property.forName("wptcep.executionStatus").as("executionStatusId"));
		projectionList.add(Projections.count("wptcep.id").as("testCaseCount"));
		//projectionList.add(Projections.groupProperty("product.productId"));
		projectionList.add(Projections.groupProperty("wptcep.tester.userId"));
		projectionList.add(Projections.groupProperty("wptcep.executionStatus"));
		c.setProjection(projectionList);
		
		List<Object[]> list = c.list();
		log.debug("Result Set Size : " + list.size());
		MetricsMasterDTO metricsMasterDTO = null;
		for (Object[] row : list) {
			metricsMasterDTO = new MetricsMasterDTO();
			metricsMasterDTO.setProductId(((Integer)row[0]).intValue());
			metricsMasterDTO.setProductName(((String)row[1]).toString());
			metricsMasterDTO.setUserId(((Integer)row[2]).intValue());
			metricsMasterDTO.setLoginId(((String)row[3]).toString());
			Integer exeStatus=((Integer)row[4]).intValue();
			metricsMasterDTO.setExecutionStatus(exeStatus);
			int testCaseCompletedCount = 0;
			int testCaseAllocatedCount = 0;
			int testCaseNotStartedCount = 0;
			int totalTCAllocatedCount = 0;
			if(exeStatus==IDPAConstants.TESTCASE_EXECUTION_STATUS_COMPLETED)
			{
				testCaseCompletedCount = ((Long)row[5]).intValue();
				metricsMasterDTO.setTestCaseCompletedCount(testCaseCompletedCount);
			}
			if(exeStatus==IDPAConstants.TESTCASE_EXECUTION_STATUS_ASSIGNED)
			{
				testCaseAllocatedCount = ((Long)row[5]).intValue(); 
				metricsMasterDTO.setTestCaseAllocatedCount(testCaseAllocatedCount);
			}
			if(exeStatus==IDPAConstants.TESTCASE_EXECUTION_STATUS_NOT_STARTED)
			{
				testCaseNotStartedCount = ((Long)row[5]).intValue(); 
				metricsMasterDTO.setTestCaseNotStartedCount(testCaseNotStartedCount);
			}
			totalTCAllocatedCount = testCaseCompletedCount+testCaseAllocatedCount+testCaseNotStartedCount;
			metricsMasterDTO.setTotalTCAllocatedCount(totalTCAllocatedCount);
			listOfWrokpackageTestcaseplan.add(metricsMasterDTO);
			log.info("Status Summary for Product Id : "  +((Integer)row[0]).intValue() +"  Name: "+((String)row[1]).toString() +"  User Id : "  +((Integer)row[2]).intValue() +" LoginId (Name): "+((String)row[3]).toString() +"  Status : "+((Integer)row[4]).intValue()+"   TotalTestCaseForAllocateCount : " + ((Long)row[5]).intValue());
		}
		} catch (Exception e) {
			log.error("update failed", e);
			return null;
		}
		
		log.info("list all sucessfull: "+listOfWrokpackageTestcaseplan.size());
	
	
	return listOfWrokpackageTestcaseplan;
		
	}
	
	@Override
	@Transactional
	public List<WorkpackageRunConfiguration> getWorkpackageRunConfigurationOfWPwithrcStatus(Integer workpackageId, Integer runConfigStatus) {
     List<WorkpackageRunConfiguration> wpRunConfigurationList = null;    
     Set<WorkpackageRunConfiguration> wpRunConfigurationSet = null;
  
     try {    	 	
            Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkpackageRunConfiguration.class, "wprc");
            c.createAlias("wprc.workpackage", "workPackage");
            c.createAlias("wprc.runconfiguration", "runconfiguration");
            c.add(Restrictions.eq("workPackage.workPackageId",workpackageId));
            c.add(Restrictions.eq("runconfiguration.runConfigStatus", runConfigStatus));
           
            wpRunConfigurationList=c.list();      
            if(wpRunConfigurationList != null && wpRunConfigurationList.size() >0){
            	 for (WorkpackageRunConfiguration wpRunConfigObj : wpRunConfigurationList) {
 					Hibernate.initialize(wpRunConfigObj.getRunconfiguration());
 					if(wpRunConfigObj.getRunconfiguration()!=null){
 						if(wpRunConfigObj.getRunconfiguration().getEnvironmentcombination() != null){
 							 EnvironmentCombination env = wpRunConfigObj.getRunconfiguration().getEnvironmentcombination();
 							 Hibernate.initialize(env);
 							 if(env.getProductMaster() != null){
 								ProductMaster prd = env.getProductMaster();
 								Hibernate.initialize(prd);
 								if(prd.getProductType() != null){
 									ProductType prdType = prd.getProductType();
 									Hibernate.initialize(prdType);
 								}
 							 }
 						}
 						Hibernate.initialize(wpRunConfigObj.getRunconfiguration().getEnvironmentcombination());
 						Hibernate.initialize(wpRunConfigObj.getRunconfiguration().getGenericDevice());
 						if(wpRunConfigObj.getRunconfiguration().getGenericDevice()!=null){
 							if(wpRunConfigObj.getRunconfiguration().getGenericDevice().getHostList()!=null){
 								Hibernate.initialize(wpRunConfigObj.getRunconfiguration().getGenericDevice().getHostList());
 								Hibernate.initialize(wpRunConfigObj.getRunconfiguration().getGenericDevice().getHostList().getCommonActiveStatusMaster());
 							}
 						}
 						if(wpRunConfigObj.getRunconfiguration().getHostList()!=null){
 							Hibernate.initialize(wpRunConfigObj.getRunconfiguration().getHostList());
 							Hibernate.initialize(wpRunConfigObj.getRunconfiguration().getHostList().getCommonActiveStatusMaster());
 						}
 						if(wpRunConfigObj.getRunconfiguration().getTestRunPlan()!=null){
 							Hibernate.initialize(wpRunConfigObj.getRunconfiguration().getTestRunPlan());
 							if(wpRunConfigObj.getRunconfiguration().getTestRunPlan().getProductVersionListMaster()!=null){
 								Hibernate.initialize(wpRunConfigObj.getRunconfiguration().getTestRunPlan().getProductVersionListMaster());
 								if(wpRunConfigObj.getRunconfiguration().getTestRunPlan().getProductVersionListMaster().getProductMaster()!=null){
 									Hibernate.initialize(wpRunConfigObj.getRunconfiguration().getTestRunPlan().getProductVersionListMaster().getProductMaster());
 									if(wpRunConfigObj.getRunconfiguration().getTestRunPlan().getProductVersionListMaster().getProductMaster().getProductType()!=null){
 										Hibernate.initialize(wpRunConfigObj.getRunconfiguration().getTestRunPlan().getProductVersionListMaster().getProductMaster().getProductType());
 									}
 								}
 							}
 						}
 						
 					}
 				}
            }

            log.debug("list all successful");
     } catch (RuntimeException re) {
            log.error("list all failed", re);
     }
     return wpRunConfigurationList;
	}	


	@Override
	@Transactional
	public void createWorkpackageExecutionPlanForExistingWorkPackage(WorkPackage workPackage, TestRunPlan testRunPlan, HttpServletRequest req, String deviceNames,String testcaseNames) {
		
		//Reload the workpackage from Session or DB
		workPackage = getWorkPackageById(workPackage.getWorkPackageId());
		workpackageExxecutionPlan(workPackage, testRunPlan, req, deviceNames, testcaseNames);
	}
	
	@Override
	@Transactional
	public void workpackageExxecutionPlan(WorkPackage workPackage,
			TestRunPlan testRunPlan, HttpServletRequest req, String deviceNames,String testcaseNames) {
		
		Integer workPackageId = workPackage.getWorkPackageId();
		String jobIds = "";
		//workpackage TestSuite plan starts
			WorkFlowEvent workFlowEvent = null;
	 		int workPackagesTestSuiteCount = 0;
			Set<TestSuiteList> testSuiteLists = testRunPlan.getTestSuiteLists();
			List<WorkPackageTestSuite> workPackageTestSuites = new ArrayList<WorkPackageTestSuite>();
			WorkPackageTestSuite workPackageTestSuite = null;
			for (TestSuiteList testSuite : testSuiteLists) {
				workPackageTestSuite = new WorkPackageTestSuite();
				workPackageTestSuite.setTestSuite(testSuite);
				workPackageTestSuite.setWorkPackage(workPackage);
				workPackageTestSuite.setCreatedDate(new Date(System.currentTimeMillis()));
				workPackageTestSuite.setEditedDate(new Date(System.currentTimeMillis()));
				workPackageTestSuite.setIsSelected(1);
				workPackageTestSuite.setStatus("ACTIVE");
				if(workPackageTestSuite.getWorkPackage().getWorkFlowEvent().getWorkFlow().getStageId() != IDPAConstants.WORKFLOW_STAGE_ID_PLANNING && workPackageTestSuite.getWorkPackage().getWorkFlowEvent().getWorkFlow().getStageValue() < 20){
					WorkFlow workFlow = getWorkFlowByEntityIdStageId(IDPAConstants.WORKPACKAGE_ENTITY_ID, IDPAConstants.WORKFLOW_STAGE_ID_PLANNING);
					workFlowEvent = new WorkFlowEvent();
					workFlowEvent.setEventDate(DateUtility.getCurrentTime());
					workFlowEvent.setRemarks("Planning Workpackage TestSuite:"+workPackage.getName());
					UserList userAdmin = userListDAO.getUserByLoginId(IDPAConstants.ADMIN_LOGIN_ID);
					workFlowEvent.setUser(userAdmin);
					workFlowEvent.setWorkFlow(workFlow);
					workPackage.setWorkFlowEvent(workFlowEvent);
					addWorkFlowEvent(workFlowEvent);
					updateWorkPackage(workPackage);
				}
				workPackageTestSuites.add(workPackageTestSuite);
				mapWorkpackageWithTestSuite(workPackageTestSuite.getWorkPackage().getWorkPackageId(),testSuite.getTestSuiteId(),"Add");
			}
			
			
			//log.info("New testcases to be added for the workpackage : "+ workPackage.getWorkPackageId() + " : count : " + workPackageTestSuites.size());
			workPackagesTestSuiteCount = addNewWorkPackageTestSuite(workPackageTestSuites);
			Set<WorkPackageTestSuite> workPackageTS=new HashSet<WorkPackageTestSuite>();
			workPackageTS.addAll(workPackageTestSuites);
			workPackage.setWorkPackageTestSuites(workPackageTS);
			updateWorkPackage(workPackage);
			//workpackage TestSuite plan end
			
			//workpackage feature plan starts
			int workPackagesFeatureCount =0;
			Set<ProductFeature> featureList= testRunPlan.getFeatureList();
			List<WorkPackageFeature> workPackageFeatures = new ArrayList<WorkPackageFeature>();
			WorkPackageFeature workPackageFeature = null;
			for (ProductFeature feature : featureList) {
				workPackageFeature = new WorkPackageFeature();
				workPackageFeature.setFeature(feature);
				workPackageFeature.setWorkPackage(workPackage);
				workPackageFeature.setCreatedDate(new Date(System.currentTimeMillis()));
				workPackageFeature.setEditedDate(new Date(System.currentTimeMillis()));
				workPackageFeature.setIsSelected(1);
				workPackageFeature.setStatus("ACTIVE");
				if(workPackageFeature.getWorkPackage().getWorkFlowEvent().getWorkFlow().getStageId()!=IDPAConstants.WORKFLOW_STAGE_ID_PLANNING&& workPackageFeature.getWorkPackage().getWorkFlowEvent().getWorkFlow().getStageValue()<20){
					WorkFlow workFlow=getWorkFlowByEntityIdStageId(IDPAConstants.WORKPACKAGE_ENTITY_ID, IDPAConstants.WORKFLOW_STAGE_ID_PLANNING);
					workFlowEvent = new WorkFlowEvent();
					workFlowEvent.setEventDate(DateUtility.getCurrentTime());
					workFlowEvent.setRemarks("Planning Workapckage Feature :"+workPackage.getName());
					UserList userAdmin= userListDAO.getUserByLoginId(IDPAConstants.ADMIN_LOGIN_ID);
					workFlowEvent.setUser(userAdmin);
					workFlowEvent.setWorkFlow(workFlow);
					workPackage.setWorkFlowEvent(workFlowEvent);
					addWorkFlowEvent(workFlowEvent);
					updateWorkPackage(workPackage);
				}
				workPackageFeatures.add(workPackageFeature);
				mapWorkpackageWithFeature(workPackageFeature.getWorkPackage().getWorkPackageId(),feature.getProductFeatureId(),"Add");
			}
			
			
			log.debug("New testcases to be added for the workpackage : "+ workPackage.getWorkPackageId() + " : count : " + workPackageTestSuites.size());
			workPackagesFeatureCount = addNewWorkPackageFeature(workPackageFeatures);
			Set<WorkPackageFeature> workPackageFeatureSet=new HashSet<WorkPackageFeature>();
			workPackageFeatureSet.addAll(workPackageFeatures);
			workPackage.setWorkPackageFeature(workPackageFeatureSet);
			updateWorkPackage(workPackage);
			//workpackage feature plan ends
			
			//workpackage testcase plan starts
			int workPackagesTestCaseCount =0;
			Set<TestCaseList> testCaseList= testRunPlan.getTestCaseList();
			List<WorkPackageTestCase> workPackageTestCases = new ArrayList<WorkPackageTestCase>();
			WorkPackageTestCase workPackageTestCase = null;
			for (TestCaseList testCase : testCaseList) {
				workPackageTestCase = new WorkPackageTestCase();
				workPackageTestCase.setTestCase(testCase);
				workPackageTestCase.setWorkPackage(workPackage);
				workPackageTestCase.setCreatedDate(new Date(System.currentTimeMillis()));
				workPackageTestCase.setEditedDate(new Date(System.currentTimeMillis()));
				workPackageTestCase.setIsSelected(1);
				workPackageTestCase.setStatus("ACTIVE");
				if(workPackageTestCase.getWorkPackage().getWorkFlowEvent().getWorkFlow().getStageId()!=IDPAConstants.WORKFLOW_STAGE_ID_PLANNING&& workPackageTestCase.getWorkPackage().getWorkFlowEvent().getWorkFlow().getStageValue()<20){
					WorkFlow workFlow=getWorkFlowByEntityIdStageId(IDPAConstants.WORKPACKAGE_ENTITY_ID, IDPAConstants.WORKFLOW_STAGE_ID_PLANNING);
					workFlowEvent = new WorkFlowEvent();
					workFlowEvent.setEventDate(DateUtility.getCurrentTime());
					workFlowEvent.setRemarks("Planning Workapckage testcase :"+workPackage.getName());
					UserList userAdmin= userListDAO.getUserByLoginId(IDPAConstants.ADMIN_LOGIN_ID);
					workFlowEvent.setUser(userAdmin);
					workFlowEvent.setWorkFlow(workFlow);
					workPackage.setWorkFlowEvent(workFlowEvent);
					addWorkFlowEvent(workFlowEvent);
					updateWorkPackage(workPackage);
				}
				workPackageTestCases.add(workPackageTestCase);
				mapWorkpackageWithTestCase(workPackageTestCase.getWorkPackage().getWorkPackageId(),testCase.getTestCaseId(),"Add");
			}
			
			
			log.debug("New testcases to be added for the workpackage : "+ workPackage.getWorkPackageId() + " : count : " + workPackageTestSuites.size());
			workPackagesTestCaseCount = addNewWorkPackageTestCases(workPackageTestCases);
			Set<WorkPackageTestCase> workPackageTestCaseSet=new HashSet<WorkPackageTestCase>();
			workPackageTestCaseSet.addAll(workPackageTestCases);
			workPackage.setWorkPackageTestCases(workPackageTestCaseSet);
			updateWorkPackage(workPackage);
			//workpackage testcase plan ends
			
			WorkpackageRunConfiguration wpRunConfiguration =null;
			Set<RunConfiguration> runConfigurations=new HashSet<RunConfiguration>();
			Set<RunConfiguration> runConfigurationsTotal=testRunPlan.getRunConfigurationList();
			Integer type= testRunPlan.getProductVersionListMaster().getProductMaster().getProductType().getProductTypeId();
			ArrayList dl= new ArrayList();

			if(deviceNames!=null && deviceNames.length()>0){
				String [] deviceName= deviceNames.split(",");
				for (String device : deviceName) {
					dl.add(device);
				}
				for (RunConfiguration runConfiguration : runConfigurationsTotal) {
					
					if(runConfiguration.getProductType() != null && runConfiguration.getProductType().getProductTypeId() != null)
						type = runConfiguration.getProductType().getProductTypeId();
					
					String runConfigType = "N/A";
					
					if(type==1 || type==5){
						runConfigType = "Device / Mobile";
						for(int j=0;j< dl.size();j++){							
							if(dl.get(j).toString().equals(runConfiguration.getRunconfigId().toString()) || (runConfiguration.getHostList() != null && runConfiguration.getHostList().getHostName() != null && dl.get(j).toString().equals(runConfiguration.getHostList().getHostName()))){
								runConfigurations.add(runConfiguration);
							}
						}
					}else if(type==2 || type==3 || type == 4){
						runConfigType = "Web / Embedded / Desktop";
						for(int j=0;j< dl.size();j++){							
							if(dl.get(j).toString().equals(runConfiguration.getRunconfigId().toString()) || dl.get(j).toString().equals(runConfiguration.getHostList().getHostName())){
								runConfigurations.add(runConfiguration);
							} 
						}						
					} else if(type == 6){
						runConfigType = "Composite";
						//If type is composite then copy both device and host.
						for(int j=0;j< dl.size();j++){
							if(dl.get(j).toString().equals(runConfiguration.getRunconfigId().toString()) || dl.get(j).toString().contains(runConfiguration.getHostList().getHostName())
									|| dl.get(j).toString().contains(runConfiguration.getGenericDevice().getName())){
								runConfigurations.add(runConfiguration);
							}
						}
					}
					log.info (" Test Configuration Type ID : "+type + " ; Name : " +runConfigType);
				}
			}else{
				runConfigurations.addAll(runConfigurationsTotal);
			}
			
			for (RunConfiguration runConfiguration : runConfigurations) {
				addRunConfigurationWorkpackage(workPackage.getWorkPackageId(),runConfiguration.getRunconfigId(),"testsuite");
				addRunConfigurationWorkpackage(workPackage.getWorkPackageId(),runConfiguration.getRunconfigId(),"feature");
				addRunConfigurationWorkpackage(workPackage.getWorkPackageId(),runConfiguration.getRunconfigId(),"testcase");
			}			
			
		if(testRunPlan.getExecutionType().getExecutionTypeId()==4){
			
			//creating feature execution plan starts
			 WorkPackageFeatureExecutionPlan workPackageFeatureExecutionPlan =null;
             List<WorkPackageFeatureExecutionPlan> workPackageFeatureExecutionPlanListForUpdate= new ArrayList<WorkPackageFeatureExecutionPlan>();
            
			for (RunConfiguration runConfiguration : runConfigurations) {
				List<WorkPackageFeature> workPackageFeatureList=listWorkPackageFeature(workPackage.getWorkPackageId());
				Set<WorkPackageFeature> wpf = new HashSet<WorkPackageFeature>(workPackageFeatureList);
				wpRunConfiguration=getWorkpackageRunConfiguration(workPackage.getWorkPackageId(), runConfiguration.getRunconfigId(), "feature");

	        	ProductFeature feature=null;
	        	WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan =null;
	            List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlanListForUpdate= new ArrayList<WorkPackageTestCaseExecutionPlan>();
	            
	            for(WorkPackageFeature wpFeature:wpf){
                	if(wpFeature.getIsSelected()==1){
                		feature=productFeatureDAO.getByProductFeatureId(wpFeature.getFeature().getProductFeatureId());
                		
                		TestRunJob testRunJob=getTestRunJobByWP(workPackage, runConfiguration);
                		if(testRunJob!=null){
                			mapTestRunJobFeature(testRunJob.getTestRunJobId(), feature.getProductFeatureId(), "Add");
             			}else{
             				addTestRunJob(runConfiguration, null, workPackage, null);
             				testRunJob=getTestRunJobByWP(workPackage, runConfiguration);
             				mapTestRunJobFeature(testRunJob.getTestRunJobId(), feature.getProductFeatureId(), "Add");
             				if(testRunJob!=null){
								if(testRunJob.getTestRunJobId()!=null)
								mongoDBService.addTestRunJobToMongoDB(testRunJob.getTestRunJobId());
							}
             			}
                		jobIds = jobIds +testRunJob.getTestRunJobId() +",";
                		if(feature!=null){
	            			workPackageFeatureExecutionPlan=new WorkPackageFeatureExecutionPlan();
	     					workPackageFeatureExecutionPlan.setCreatedDate(DateUtility.getCurrentTime());
								workPackageFeatureExecutionPlan.setFeature(feature);
								workPackageFeatureExecutionPlan.setModifiedDate(DateUtility.getCurrentTime());
								workPackageFeatureExecutionPlan.setPlannedExecutionDate(workPackage.getPlannedStartDate());
								workPackageFeatureExecutionPlan.setRunConfiguration(wpRunConfiguration);
								workPackageFeatureExecutionPlan.setWorkPackage(workPackage);
								workPackageFeatureExecutionPlan.setStatus(1);
								workPackageFeatureExecutionPlan.setTestRunJob(testRunJob);
								ExecutionPriority executionPriority=null;
								if(wpFeature.getFeature().getExecutionPriority()!=null)
									executionPriority= getExecutionPriorityByName(CommonUtility.getExecutionPriority(wpFeature.getFeature().getExecutionPriority().getPriorityName()));
								else
									executionPriority= getExecutionPriorityByName(IDPAConstants.EXECUTION_PRIORITY_MEDIUM);
								workPackageFeatureExecutionPlan.setExecutionPriority(executionPriority);
								addWorkpackageFeatureExecutionPlan(workPackageFeatureExecutionPlan);


								Set<TestCaseList> featureTC=feature.getTestCaseList();
	                			for(TestCaseList tclist :featureTC){
	                				if (tclist != null){
	                					tclist = testCaseListDAO.getByTestCaseId(tclist.getTestCaseId());
	                					workPackageTestCaseExecutionPlan=workPackageService.addWorkpPackageTestCaseExecutionPlans(tclist, null, workPackage, wpRunConfiguration,feature,"Feature",testRunJob);
		                    			workPackageTestCaseExecutionPlanListForUpdate.add(workPackageTestCaseExecutionPlan);
	                				}
	                			}
	                			addWorkPackageTestcaseExecutionPlan(workPackageTestCaseExecutionPlanListForUpdate);
	                		}     
                		}
                	}
			}
			//creating feature execution plan ends
			
			//creating testsuite execution plan ends
			 WorkPackageTestSuiteExecutionPlan workPackageTestSuiteExecutionPlan =null;
             List<WorkPackageTestSuiteExecutionPlan> workPackageTestSuiteExecutionPlanListForUpdate= new ArrayList<WorkPackageTestSuiteExecutionPlan>();
            
			for (RunConfiguration runConfiguration : runConfigurations) {

			wpRunConfiguration=getWorkpackageRunConfiguration(workPackage.getWorkPackageId(), runConfiguration.getRunconfigId(), "testsuite");
			List<WorkPackageTestSuite> workPackageTestSuiteList=listWorkPackageTestSuite(workPackage.getWorkPackageId());
			Set<WorkPackageTestSuite> wpts = new HashSet<WorkPackageTestSuite>(workPackageTestSuiteList);
        	
			TestSuiteList testSuite=null;
        	WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan =null;
            List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlanListForUpdate= new ArrayList<WorkPackageTestCaseExecutionPlan>();
            
        	 for(WorkPackageTestSuite wpTestSuite:wpts){
                	if(wpTestSuite.getIsSelected()==1){
                		testSuite=wpTestSuite.getTestSuite();
                		TestRunJob testRunJob=getTestRunJobByWP(workPackage, runConfiguration);
                		if(testRunJob!=null){
                			mapTestRunJobTestSuite(testRunJob.getTestRunJobId(),testSuite.getTestSuiteId(), "Add");
             			}else{
             				addTestRunJob(runConfiguration, null, workPackage, null);
             				testRunJob=getTestRunJobByWP(workPackage, runConfiguration);
             				mapTestRunJobTestSuite(testRunJob.getTestRunJobId(),testSuite.getTestSuiteId(), "Add");
             			}
                		jobIds = jobIds +testRunJob.getTestRunJobId() +",";
            			workPackageTestSuiteExecutionPlan=new WorkPackageTestSuiteExecutionPlan();
            			workPackageTestSuiteExecutionPlan.setCreatedDate(DateUtility.getCurrentTime());
            			workPackageTestSuiteExecutionPlan.setTestsuite(testSuite);
            			workPackageTestSuiteExecutionPlan.setModifiedDate(DateUtility.getCurrentTime());
            			workPackageTestSuiteExecutionPlan.setPlannedExecutionDate(workPackage.getPlannedStartDate());
							workPackageTestSuiteExecutionPlan.setRunConfiguration(wpRunConfiguration);
							workPackageTestSuiteExecutionPlan.setWorkPackage(workPackage);
							workPackageTestSuiteExecutionPlan.setStatus(1);
							workPackageTestSuiteExecutionPlan.setTestRunJob(testRunJob);
							ExecutionPriority executionPriority=null;
							if(wpTestSuite.getTestSuite().getExecutionPriority()!=null)
								executionPriority= getExecutionPriorityByName(CommonUtility.getExecutionPriority(wpTestSuite.getTestSuite().getExecutionPriority().getPriorityName()));
							else
								executionPriority= getExecutionPriorityByName(IDPAConstants.EXECUTION_PRIORITY_MEDIUM);
							workPackageTestSuiteExecutionPlan.setExecutionPriority(executionPriority);
							addWorkpackageTestSuiteExecutionPlan(workPackageTestSuiteExecutionPlan);
							
							Set<TestCaseList> testSuiteTC=testSuite.getTestCaseLists();
                			for(TestCaseList tcList :testSuiteTC){
                				if (tcList != null){
                					tcList = testCaseListDAO.getByTestCaseId(tcList.getTestCaseId());
                					workPackageTestCaseExecutionPlan=workPackageService.addWorkpPackageTestCaseExecutionPlans(tcList, testSuite, workPackage, wpRunConfiguration,null,"TestSuite",testRunJob);
	                    			workPackageTestCaseExecutionPlanListForUpdate.add(workPackageTestCaseExecutionPlan);
                				}
                			}
                			addWorkPackageTestcaseExecutionPlan(workPackageTestCaseExecutionPlanListForUpdate);
                		} 
                	
                   }
			}
			//creating testsuite execution plan ends
			
			//creating testcase execution plan ends
			
			List<WorkPackageTestCase> workPackageTestCaseList=listWorkPackageTestCases(workPackage.getWorkPackageId());
			Set<WorkPackageTestCase> workPackageTestCasesSet = new HashSet<WorkPackageTestCase>(workPackageTestCaseList);
			
			 for(RunConfiguration rc:runConfigurations){
             	wpRunConfiguration=getWorkpackageRunConfiguration(workPackage.getWorkPackageId(), rc.getRunconfigId(), "testcase");

				TestCaseList testcase=null;
             	WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan =null;
                List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlanListForUpdate= new ArrayList<WorkPackageTestCaseExecutionPlan>();
					
					for(WorkPackageTestCase workPackageTC:workPackageTestCasesSet){
						if(workPackageTC.getIsSelected()==1){
							testcase=testCaseListDAO.getByTestCaseId(workPackageTC.getTestCase().getTestCaseId());
							TestRunJob testRunJob=getTestRunJobByWP(workPackage, rc);
	                 		if(testRunJob!=null){
	                 			mapTestRunJobTestCase(testRunJob.getTestRunJobId(), testcase.getTestCaseId(), "Add");
	              			}else{
	              				addTestRunJob(rc, null, workPackage, null);
	              				testRunJob=getTestRunJobByWP(workPackage, rc);
	              				mapTestRunJobTestCase(testRunJob.getTestRunJobId(), testcase.getTestCaseId(), "Add");
	              				if(testRunJob!=null){
									if(testRunJob.getTestRunJobId()!=null)
									mongoDBService.addTestRunJobToMongoDB(testRunJob.getTestRunJobId());
								}
	              			}
	                 		jobIds = jobIds +testRunJob.getTestRunJobId() +",";
							workPackageTestCaseExecutionPlan = new WorkPackageTestCaseExecutionPlan();
							workPackageTestCaseExecutionPlan.setTestCase(testcase);
							workPackageTestCaseExecutionPlan.setWorkPackage(workPackage);
						//workPackageTestCaseExecutionPlan.setEnvironment(null);
							workPackageTestCaseExecutionPlan.setRunConfiguration(wpRunConfiguration);
							
							//workPackageTestCaseExecutionPlan.setProductLocale(null);
							workPackageTestCaseExecutionPlan.setExecutionStatus(3);
							workPackageTestCaseExecutionPlan.setIsExecuted(0);
							workPackageTestCaseExecutionPlan.setSourceType("TestCase");
							workPackageTestCaseExecutionPlan.setStatus(1);
							workPackageTestCaseExecutionPlan.setTestRunJob(testRunJob);
							ExecutionPriority executionPriority=null;
							if(testcase.getTestCasePriority()!=null)
								executionPriority= getExecutionPriorityByName(CommonUtility.getExecutionPriority(testcase.getTestCasePriority().getPriorityName()));
							else
								executionPriority= getExecutionPriorityByName(IDPAConstants.EXECUTION_PRIORITY_HIGH);
							
							workPackageTestCaseExecutionPlan.setExecutionPriority(executionPriority);
							
							TestCaseExecutionResult testCaseExecutionResult = new TestCaseExecutionResult();
							testCaseExecutionResult.setResult("");
							testCaseExecutionResult.setComments("");
							testCaseExecutionResult.setDefectsCount(0);
							testCaseExecutionResult.setDefectIds("");
							testCaseExecutionResult.setIsApproved(0);
							testCaseExecutionResult.setIsReviewed(0);
							testCaseExecutionResult.setObservedOutput("");
							
							testCaseExecutionResult.setWorkPackageTestCaseExecutionPlan(workPackageTestCaseExecutionPlan);
							workPackageTestCaseExecutionPlan.setTestCaseExecutionResult(testCaseExecutionResult);
							workPackageTestCaseExecutionPlanListForUpdate.add(workPackageTestCaseExecutionPlan);
						}
						
					}
					addWorkPackageTestcaseExecutionPlan(workPackageTestCaseExecutionPlanListForUpdate);
				}

			//creating testcase execution plan ends
			
				
			}
		int jobsCount  = 0;
		if(jobIds != null && !jobIds.isEmpty()){
			String[] arr = jobIds.split(",");
			jobsCount = arr.length;
		}
		ScriptLessExecutionDTO scriptLesExeDTO= new ScriptLessExecutionDTO(workPackage.getWorkPackageId(),workPackage.getName(),jobIds, jobsCount);
		if(testRunPlan.getExecutionType().getExecutionTypeId()==3){
			//log.info("execution type is auto");
			toolIntegrationController = new ToolIntegrationController();
			TestManagementSystem testManagementSystem=testManagementSystemDAO.getPrimaryTMSByProductId(workPackage.getProductBuild().getProductVersion().getProductMaster().getProductId());
			if(req!=null){
				toolIntegrationController.checkOutScriptsRest(workPackage.getProductBuild().getProductVersion().getProductMaster(), testRunPlan, req,workPackage,testManagementSystem);
			}
			executeTestRunPlan(testRunPlan,workPackage,runConfigurations,testcaseNames);
		}
		
		
	}	
	@Override
	@Transactional
	public int getWPTypeByTestRunPlanExecutionType(int workPackageId){
		log.debug("GetWPType by TRP ExecutionType by WP ID");
		int wpType = 0;
		String sql ="";
		String sql1= "";
		List<Object[]> unMappedEnvCombiListObj = null;
			try {			
				sql = "select trp.executionTypeId from workpackage wp inner join test_run_plan trp "+
					"on wp.testRunPlanId = trp.testRunPlanId " +
					"where wp.workPackageId=:wpId group by wp.workPackageId,trp.executiontypeid";
				Object wpTypeObject = sessionFactory.getCurrentSession().createSQLQuery(sql)
				.setParameter("wpId", workPackageId).uniqueResult();
				
				if(wpTypeObject != null){
					wpType = (int) wpTypeObject;
					log.info("Obtained Execution Type for TRP Type for WorkPackage--"+wpType);
				}
			} catch (HibernateException e) {
				log.error(e);
			}
		return wpType;
	}
	@Override
	@Transactional
	public List getTestRunResultFromListAndDetailViews(int testRunNo,String deviceId){
		List testRunReportListFromView=null;
		try{
			//String sql="SELECT rdevlist.testRunNo,rdevlist.testRunListId,rdevlist.productName,rdevlist.productVersionName,rdevlist.deviceId,rdevlist.deviceModel,rdevlist.devicePlatformName,rdevlist.devicePlatformVersion, rdevlist.testRunconfigurationName,rdevlist.testSuiteId,rdevlist.testSuiteName,rdevlist.testRunStartTime, rdevlist.testRunEndTime,rdevlist.testRunStatus,rdevlist.testRunTriggeredTime,rdevlist.testRunFailureMessage,rdevlist.testCaseName,rdevlist.testStep,rdevlist.testStepInput,rdevlist.testStepExpectedOutput,rdevlist.testStepObservedOutput,rdevlist.testResultStatus,SUM(IF(rdevdet.testResultStatus='PASSED',1,0)), COUNT(rdevdet.testResultStatus),IF (SUM(IF(rdevdet.testResultStatus='PASSED',1,0))=COUNT(rdevdet.testResultStatus),'PASSED', 'FAILED'), rdevlist.testRunConfigurationChildId FROM test_run_reports_device_list_view AS rdevlist,test_run_reports_device_details_view AS rdevdet WHERE rdevlist.testRunNo=rdevdet.testRunNo AND rdevlist.testRunListId=rdevdet.testRunListId AND  rdevlist.testRunNo=:testRunNo AND rdevlist.deviceId=:deviceId";
			String sql="SELECT rdevlist.testRunNo,rdevlist.testRunListId,rdevlist.productName,"
					+"rdevlist.productVersionName,rdevlist.deviceId,rdevlist.deviceModel,"
					+"rdevlist.devicePlatformName,rdevlist.devicePlatformVersion, rdevlist.testRunconfigurationName,rdevlist.testSuiteId,"
					+"rdevlist.testSuiteName,rdevlist.testRunStartTime, rdevlist.testRunEndTime,rdevlist.testRunStatus,"
					+"rdevlist.testRunTriggeredTime,rdevlist.testRunFailureMessage,rdevlist.testCaseName,rdevlist.testStep,"
					+"rdevlist.testStepInput,rdevlist.testStepExpectedOutput,rdevlist.testStepObservedOutput,rdevlist.testResultStatus,"
					+"SUM(CASE when rdevdet.testResultStatus='PASSED'THEN 1 ELSE 0 END), COUNT(rdevdet.testResultStatus),"
					+"CASE WHEN (SUM(CASE WHEN rdevdet.testResultStatus='PASSED' THEN 1 ELSE 0 END) = COUNT(rdevdet.testResultStatus)) THEN 'PASSED' ELSE 'FAILED' END," 
					+"rdevlist.testRunConfigurationChildId FROM test_run_reports_device_list_view AS rdevlist,test_run_reports_device_details_view AS rdevdet "
					+"WHERE rdevlist.testRunNo=rdevdet.testRunNo AND rdevlist.testRunListId=rdevdet.testRunListId AND  "
					+"rdevlist.testRunNo=:testRunNo AND rdevlist.deviceId=:deviceId GROUP BY rdevlist.testRunNo,rdevlist.testRunListId,rdevlist.productName,rdevlist.productVersionName,rdevlist.deviceId,rdevlist.deviceModel,"
					+"rdevlist.devicePlatformName,rdevlist.devicePlatformVersion, rdevlist.testRunconfigurationName,rdevlist.testSuiteId,"
					+"rdevlist.testSuiteName,rdevlist.testRunStartTime, rdevlist.testRunEndTime,rdevlist.testRunStatus,"
					+"rdevlist.testRunTriggeredTime,rdevlist.testRunFailureMessage,rdevlist.testCaseName,rdevlist.testStep,"
					+"rdevlist.testStepInput,rdevlist.testStepExpectedOutput,rdevlist.testStepObservedOutput,rdevlist.testResultStatus,rdevlist.testRunConfigurationChildId";
			testRunReportListFromView = sessionFactory.getCurrentSession().createSQLQuery(sql)
					.setParameter("testRunNo", testRunNo)
					.setParameter("deviceId",deviceId)
					.list();
			
			final Iterator it = testRunReportListFromView.iterator(); 
			while(it.hasNext()) 
			{ 
				Object row[] = (Object[]) it.next();
				
				
				
				
			}
			
			return testRunReportListFromView;
		}catch(Exception e){
			log.error("Error while getting test run report values from views", e);
			return null;
		}
		
	}
	@Override
	@Transactional
	public List getTestRunResultFromViews(int testRunNo){
		List testRunReportListFromView=null;
		try{
			String sql="SELECT v.testCaseId, t.testCaseDescription ,v.testCaseName,v.testStepName,v.testStepDescription,v.testStepExpectedOutput,v.testStepObservedOutput,v.executionRemarks,v.testStepResult,v.screenShotPath, v.productName, v.productVersionName, v.deviceId, v.devicePlatformName, v. devicePlatformVersion, v.testRunConfigurationName FROM testrunreports_teststeps_devicedetails_view AS v, test_case_list AS t WHERE v.testCaseId=t.testCaseId AND testRunNo=:testRunNo ORDER BY testStepId asc";
			testRunReportListFromView = sessionFactory.getCurrentSession().createSQLQuery(sql)
					.setParameter("testRunNo", testRunNo)					
					.list();
			return testRunReportListFromView;
		}catch(Exception e){
			log.error("Error while getting test run report values from views", e);
			return null;
		}
		
	}
	@Override
	@Transactional
	public List<TestStepExecutionResult> listTestStepResultByCaseExecId(Integer tcerId) {		
		log.debug("getting testStepExecutionResult by testCaseexecutionResultId: ");
		List<TestStepExecutionResult> testStepExecutionResultList=null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestStepExecutionResult.class, "tser");
			c.createAlias("tser.testcase", "testcase");
			c.createAlias("tser.testCaseExecutionResult", "testCaseExecutionResult");
			
			
			if(tcerId!=-1 && tcerId !=0)
				c.add(Restrictions.eq("testCaseExecutionResult.testCaseExecutionResultId",  tcerId));
			c.addOrder(Order.asc("teststepexecutionresultid"));
			
			testStepExecutionResultList = c.list();

			for(TestStepExecutionResult testStepExecutionResult:testStepExecutionResultList){
				Hibernate.initialize(testStepExecutionResult.getTestcase());
				Hibernate.initialize(testStepExecutionResult.getTestSteps());
				Hibernate.initialize(testStepExecutionResult.getTestCaseExecutionResult());
				Hibernate.initialize(testStepExecutionResult.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration());
				
			}
		} catch (RuntimeException re) {
			log.error("testcaseListByEvidence failed", re);
		}catch (Exception re) {
			log.error("Exception occurres failed", re);
		}
		return testStepExecutionResultList;
	}

	@Override
	@Transactional
	public Evidence getEvidenceById(int evidenceId) {
		Evidence evidence=null;
		try{
		List list = sessionFactory
				.getCurrentSession()
				.createQuery("from Evidence e where e.evidenceid=:evidenceid")
				.setParameter("evidenceid", evidenceId).list();
		evidence = (list != null && list.size() != 0) ? (Evidence) list
				.get(0) : null;
		}catch (Exception re) {
			log.error("Exception occurres failed", re);
		}
				return evidence;
	}
	
	@Override
	@Transactional
	public void deleteEvidence(Evidence evidence) {
		try {
			sessionFactory.getCurrentSession().delete(evidence);
			log.debug("deletion successful");
		} catch (RuntimeException re) {
			log.error("deletion failed", re);
			//throw re;
		}		
	}
	
	@Override
	@Transactional
	public List<Object[]> getTescaseExecutionHistory(int testCaseId, Integer workPackageId, String dataLevel, Integer jtStartIndex, Integer jtPageSize) {
		log.debug("getTescaseExecutionHistory with testCaseId");
		List<Object[]> tcExecutionHistoryList = null;
		try {
			String sql="SELECT ecomb.environmentCombinationName, wptcep.wptcepId, wptcep.testcaseId, tc.testCaseName, ter.endTime, "
					+ "pm.productName, pvlm.productVersionName  , pb.buildName, wp.workPackageId, wp.name, etm.name as executionType, ter.result, wptcep.testRunJobId "
					+ "FROM workpackage_testcase_execution_plan wptcep "
					+ "INNER JOIN testcase_execution_result ter ON wptcep.wptcepId =ter.testCaseExecutionResultId "
					+ "INNER JOIN test_case_list tc ON wptcep.testcaseId = tc.testCaseId " 
					+ "INNER JOIN workpackage wp ON wptcep.workPackageId=wp.workPackageId "
					+ "INNER JOIN product_build pb ON wp.productBuildId = pb.productBuildId "
					+ "INNER JOIN product_version_list_master pvlm ON pb.productVersionId = pvlm.productVersionListId "
					+ "INNER JOIN product_master pm ON pvlm.productId = pm.productId "
					+ "INNER JOIN test_run_job trj ON wptcep.testRunJobId = trj.testRunJobId "
					+ "INNER JOIN environment_combination ecomb ON  trj.environmentCombinationId = ecomb.envionmentCombinationId "
					+ "INNER JOIN execution_type_master etm ON wp.workPackageType= etm.executionTypeId ";
					
		if(dataLevel.equals(IDPAConstants.EXECUTION_HISTORY_PRODUCT_LEVEL)){
			sql = sql +  "AND wptcep.testcaseId=:tcId order by wptcep.testcaseId, wptcep.wptcepId desc";
			if(jtStartIndex != -1 && jtPageSize != -1){
				sql = sql+" OFFSET "+jtStartIndex+" limit "+jtPageSize+"";
			}
			tcExecutionHistoryList=sessionFactory.getCurrentSession().createSQLQuery(sql).
					setParameter("tcId", testCaseId).
					list();
		}else if(dataLevel.equalsIgnoreCase(IDPAConstants.EXECUTION_HISTORY_WORKPACKAGE_LEVEL)){
			
			if(testCaseId == 0){
				sql = sql+ "AND wp.workPackageId =:wpid order by wptcep.testcaseId, wptcep.wptcepId desc";
			}else{
				sql = sql+ "AND wp.workPackageId =:wpid AND wptcep.testcaseId=:tcId order by wptcep.testcaseId, wptcep.wptcepId desc";
			}
		
			if(jtStartIndex != -1 && jtPageSize != -1){
				sql = sql+" OFFSET "+jtStartIndex+" limit "+jtPageSize+"";
			}
			if(testCaseId == 0){
				tcExecutionHistoryList=sessionFactory.getCurrentSession().createSQLQuery(sql).
						setParameter("wpid", workPackageId).					
						list();
			}else{
				tcExecutionHistoryList=sessionFactory.getCurrentSession().createSQLQuery(sql).
						setParameter("tcId", testCaseId).
						setParameter("wpid", workPackageId).					
						list();
			}
			
		}			
			log.debug("getTescaseExecutionHistory list :"+tcExecutionHistoryList.size());
		
		} catch (RuntimeException re) {
			log.error("getTescaseExecutionHistory with testCaseId", re);	
		}
		return tcExecutionHistoryList;
	}
	
	@Override
	@Transactional
	public Integer getWPTCEPCountOfATestCaseId(int testCaseId, int jtStartIndex, int jtPageSize) {
		log.debug("listing getWPTCEPCountOfATestCaseId instance");
		int wpTCCount = 0;
		
		String sql="select count(*) from workpackage_testcase_execution_plan wptcep where wptcep.testcaseId=:tcid GROUP BY wptcep.testcaseid order by wptcep.testcaseId asc";
		try {
			if(jtStartIndex!=-1 && jtPageSize!=-1){
				wpTCCount=((Number) sessionFactory.getCurrentSession().createSQLQuery(sql).setParameter("tcid", testCaseId)
						.setFirstResult(jtStartIndex).setMaxResults(jtPageSize)
						.uniqueResult()).intValue();				
			}else{
				wpTCCount=((Number) sessionFactory.getCurrentSession().createSQLQuery(sql).setParameter("tcid", testCaseId)
						.uniqueResult()).intValue();					
			}
		
		} catch (RuntimeException re) {
			log.error("list getWPTCEPCountOfATestCaseId", re);
			// throw re;
		}
		return wpTCCount;
	}
	
	@Override
	@Transactional
	public List<WorkPackageBuildTestCaseSummaryDTO> listWorkPackageTestCaseExecutionBuildSummary(Integer workPackageId,Integer productBuildId) {
		WorkPackageBuildTestCaseSummaryDTO workPackageBuildTCSummaryDTO = null;
		List<WorkPackageBuildTestCaseSummaryDTO> workPackageBuildTCSummaryList=new ArrayList<WorkPackageBuildTestCaseSummaryDTO>();		
		String sql="";
		try {
			sql="SELECT  wp.workpackageId as workPackageId ,wp.name as workPackageName,job.testRunJobId as testRunJobId,"
					+ "job.testRunStatus as testRunStatus,job.testRunFailureMessage as testRunFailureMessage,job.testRunEvidenceStatus as testRunEvidenceStatus,"
				+"( SELECT  COUNT(tcres.testCaseExecutionResultId)   FROM workpackage_testcase_execution_plan AS wptcplan INNER JOIN  testcase_execution_result  AS tcres ON tcres.testCaseExecutionResultId = wptcplan.wptcepId AND wptcplan.status=1 "
				+"WHERE wptcplan.testRunJobId =job.testRunJobId AND tcres.result='PASSED' ) AS passedCount,"
				+"( SELECT  COUNT(tcres.testCaseExecutionResultId)   FROM workpackage_testcase_execution_plan AS wptcplan INNER JOIN  testcase_execution_result  AS tcres ON tcres.testCaseExecutionResultId = wptcplan.wptcepId AND wptcplan.status=1 "
				+"WHERE job.testRunJobId=wptcplan.testRunJobId AND tcres.result='FAILED' ) AS failedCount,"
				+"( SELECT  COUNT(tcres.testCaseExecutionResultId)   FROM workpackage_testcase_execution_plan AS wptcplan INNER JOIN  testcase_execution_result  AS tcres ON tcres.testCaseExecutionResultId = wptcplan.wptcepId AND wptcplan.status=1 "
				+"WHERE job.testRunJobId=wptcplan.testRunJobId AND tcres.result='NORUN' ) AS norunCount,"
				+"( SELECT  COUNT(tcres.testCaseExecutionResultId)   FROM workpackage_testcase_execution_plan AS wptcplan INNER JOIN  testcase_execution_result  AS tcres ON tcres.testCaseExecutionResultId = wptcplan.wptcepId AND wptcplan.status=1 "
				+"WHERE job.testRunJobId=wptcplan.testRunJobId AND tcres.result IS NULL ) AS notexecutedCount, "
				+"(SELECT COUNT(tser.teststepexecutionresultid)  FROM teststep_execution_result as tser "
				+ "inner join  testcase_execution_result  AS tcres on tcres.testCaseExecutionResultId = tser.tescaseexecutionresultid "
				+ "inner join workpackage_testcase_execution_plan as wptcplan on wptcplan.wptcepId = tcres.testCaseExecutionResultId "
				+ "where job.testRunJobId=wptcplan.testRunJobId) AS teststepcount, "
				+ "(SELECT COUNT(buglist.testExecutionResultBugId)  FROM workpackage_testcase_execution_plan AS wptcPlan "
				+ "LEFT JOIN test_execution_result_bug_list AS buglist ON wptcPlan.wptcepId=buglist.testCaseExecutionResultId "
				+ "WHERE job.testRunJobId=wptcplan.testRunJobId ) AS defectsCount "				
				+"FROM    test_run_job  AS job "
				+"INNER JOIN  workpackage wp ON job.workpackageId=wp.workpackageId " 
				+"WHERE wp.workpackageId ="+workPackageId+"  ORDER BY job.testRunJobId ASC";
				
			workPackageBuildTCSummaryList = ((SQLQuery) sessionFactory.getCurrentSession().createSQLQuery(sql)).addScalar("workPackageId",StandardBasicTypes.INTEGER).addScalar("workPackageName",StandardBasicTypes.STRING)
					.addScalar("testRunJobId",StandardBasicTypes.INTEGER).addScalar("testRunStatus",StandardBasicTypes.INTEGER)
					.addScalar("testRunFailureMessage",StandardBasicTypes.STRING).addScalar("testRunEvidenceStatus",StandardBasicTypes.STRING)
					.addScalar("passedCount",StandardBasicTypes.INTEGER).addScalar("failedCount",StandardBasicTypes.INTEGER)
					.addScalar("norunCount",StandardBasicTypes.INTEGER).addScalar("notexecutedCount",StandardBasicTypes.INTEGER)
					.addScalar("teststepcount",StandardBasicTypes.INTEGER).addScalar("defectsCount",StandardBasicTypes.INTEGER)					
					.setResultTransformer(Transformers.aliasToBean(WorkPackageBuildTestCaseSummaryDTO.class)).list();
		} catch (HibernateException e) {
			e.printStackTrace();
		}		
		return workPackageBuildTCSummaryList;
	}

	@Override
	public List<TestCaseList> listTestCaseOfTRJob(int testRunJobId) {
		log.debug("listing all TestCaseList instance");
		List<TestCaseList> testCaseList= new ArrayList<TestCaseList>();
		List<Object[]> objectList=null;
		try {
			
			String hql = "from TestCaseList tc "
					+ "inner join tc.workPackageTestCaseExecutionPlan as wptcep  "	
					+ "inner join wptcep.testRunJob as trj"					
					+ " where trj.testRunJobId =:trjId ";
			
			objectList=sessionFactory.getCurrentSession().createQuery(hql)
					.setParameter("trjId", testRunJobId)
					.list();			
			if(objectList != null && !objectList.isEmpty()){
				for (Object[] modelobjects : objectList) {
					if(modelobjects != null && modelobjects[0] != null){
						testCaseList.add((TestCaseList)modelobjects[0]);
					}					
				}
			}
			if(testCaseList!= null && !testCaseList.isEmpty()){
				for(TestCaseList testCase : testCaseList){
					Hibernate.initialize(testCase.getProductMaster());
					if(testCase.getProductFeature() != null){
						Hibernate.initialize(testCase.getProductFeature());
					}
					if(testCase.getProductVersionList() != null){
						Set<ProductVersionListMaster> pvmset = testCase.getProductVersionList();
						for (ProductVersionListMaster productVersionListMaster : pvmset) {
							Hibernate.initialize(productVersionListMaster);
							Hibernate.initialize(productVersionListMaster.getTestCaseLists());	
						}											
					}
					Hibernate.initialize(testCase.getTestCaseStepsLists());
					Hibernate.initialize(testCase.getTestSuiteLists());
					Hibernate.initialize(testCase.getDecouplingCategory());
					Hibernate.initialize(testCase.getExecutionTypeMaster());
					Hibernate.initialize(testCase.getTestcaseTypeMaster());
				}
			}
			
			log.debug("list TCOfTRJ all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return testCaseList;
	}

	@Override
	public TestRunJob listTestCaseExecutionDetailsOfTRJob(int testRunJobId) {
		log.debug("listing all listTestCaseExecutionDetailsOfTRJob instance");
		List<TestRunJob> trjObjList= new ArrayList<TestRunJob>();
		TestRunJob trjObj= null;
		
		List<Object[]> objectList=null;
		try {			
			String hql = "";						
			hql = "from TestRunJob trj where trj.testRunJobId=:trjId";			
			trjObjList=sessionFactory.getCurrentSession().createQuery(hql)
					.setParameter("trjId", testRunJobId)
					.list();	
			
			trjObj = (trjObjList != null && trjObjList.size() != 0) ? (TestRunJob)trjObjList.get(0): null ;
			if(trjObj != null){
				if(trjObj.getTestCaseListSet() != null && trjObj.getTestCaseListSet().size() >0){
					Hibernate.initialize(trjObj.getTestCaseListSet());
					Set<TestCaseList> tcSet = trjObj.getTestCaseListSet();
					for (TestCaseList tcobj : tcSet) {
						Hibernate.initialize(tcobj);
					}
					Set<WorkPackageTestCaseExecutionPlan> wptcepSet = trjObj.getWorkPackageTestCaseExecutionPlans();
					for (WorkPackageTestCaseExecutionPlan wptcep : wptcepSet) {
						if(wptcep.getTestCaseExecutionResult() != null){							
							TestCaseExecutionResult tcer = wptcep.getTestCaseExecutionResult();
							Hibernate.initialize(tcer);
							if(tcer.getTestStepExecutionResultSet() != null){
								Set<TestStepExecutionResult> tstepSet = tcer.getTestStepExecutionResultSet();
								Hibernate.initialize(tstepSet);
								for (TestStepExecutionResult tstepexeobj : tstepSet) {
									if(tstepexeobj.getTestSteps() != null){
										TestCaseStepsList tstep = tstepexeobj.getTestSteps();
										Hibernate.initialize(tstep);										
									}
								}
								
								
							}
						}
						if(wptcep.getWorkPackage() != null){
							Hibernate.initialize(wptcep.getWorkPackage());
							if(wptcep.getWorkPackage().getWorkPackageType() != null){
								Hibernate.initialize(wptcep.getWorkPackage().getWorkPackageType());
							}
							if(wptcep.getWorkPackage().getUserList() != null){
								Hibernate.initialize(wptcep.getWorkPackage().getUserList());
							}
						}
						Hibernate.initialize(wptcep.getTester());
						if(wptcep.getTestCase() != null){
							TestCaseList tcofWptcep = wptcep.getTestCase();
							Hibernate.initialize(tcofWptcep);
						}
						
					}
					Hibernate.initialize(trjObj.getRunConfiguration());
				}
			}
			log.debug("list listTestCaseExecutionDetailsOfTRJob all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return trjObj;
	}
	
	@Override
	@Transactional
	public WorkPackageTestCaseExecutionPlan listTestStepExecutionDetailsOfTCExecutionId(int wptcepId) {
		log.debug("listing all listTestStepExecutionDetailsOfTCExecutionId instance");
		List<WorkPackageTestCaseExecutionPlan> wptcepObjList= new ArrayList<WorkPackageTestCaseExecutionPlan>();
		WorkPackageTestCaseExecutionPlan wptcep= null;		
		try {			
			String hql = "";						
			hql = "from WorkPackageTestCaseExecutionPlan wptcep where wptcep.id=:wptcepId";			
			wptcepObjList=sessionFactory.getCurrentSession().createQuery(hql)
					.setParameter("wptcepId", wptcepId)
					.list();	
			
			wptcep = (wptcepObjList != null && wptcepObjList.size() != 0) ? (WorkPackageTestCaseExecutionPlan)wptcepObjList.get(0): null ;
			if(wptcep != null){
						if(wptcep.getTestCaseExecutionResult() != null){							
							TestCaseExecutionResult tcer = wptcep.getTestCaseExecutionResult();
							Hibernate.initialize(tcer);
							if(tcer.getTestStepExecutionResultSet() != null){
								Set<TestStepExecutionResult> tstepSet = tcer.getTestStepExecutionResultSet();
								Hibernate.initialize(tstepSet);
								for (TestStepExecutionResult tstepexeobj : tstepSet) {
									if(tstepexeobj.getTestSteps() != null){
										TestCaseStepsList tstep = tstepexeobj.getTestSteps();
										Hibernate.initialize(tstep);										
									}
								}	
							}
						}						
						Hibernate.initialize(wptcep.getTester());
						if(wptcep.getTestCase() != null){
							TestCaseList tcofWptcep = wptcep.getTestCase();
							Hibernate.initialize(tcofWptcep);
						}
			}		
			log.debug("list listTestStepExecutionDetailsOfTCExecutionId all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return wptcep;
	}
	
	@Override
	@Transactional
	public String getDeviceNameByTestRunJob(int testRunJobId) {
		log.debug("getting getDeviceNameByTestRunJob instance by id");
		String devName = "";
		List<Object> objlist = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestRunJob.class, "trj");
			c.createAlias("trj.genericDevices", "gd");			
			c.add(Restrictions.eq("trj.testRunJobId",testRunJobId));
			ProjectionList proj = Projections.projectionList();
			proj.add(Property.forName("gd.name"));
			c.setProjection(proj);
			
			objlist = c.list();
			if(objlist != null && objlist.size() >0){
				devName	= c.uniqueResult().toString();	
			}
			log.debug("getting getDeviceNameByTestRunJob successful");
		} catch (Exception re) {
			re.printStackTrace();
			log.error("getting getDeviceNameByTestRunJob failed", re);
		}
		return devName;
	}
	
	
	@Override
	@Transactional
	public List<WorkPackageTestCaseExecutionPlan> getNotExecutedTestCaseListByWpId(Integer wpid){
		List<WorkPackageTestCaseExecutionPlan> wpepList = null;
		Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackageTestCaseExecutionPlan.class, "wptcep");
		c.createAlias("wptcep.workPackage", "workPackage");
		c.add(Restrictions.eq("workPackage.workPackageId", wpid));
		c.add(Restrictions.eq("wptcep.isExecuted", 0));
		wpepList = c.list();
		
		return wpepList;
	}

	@Override
	@Transactional
	public Integer addWorkFlowevent(WorkFlowEvent workFlowEvent) {
		log.info("inside addWorkFlowevent");
		Integer wrkflwevntId=null;
		try {	
			sessionFactory.getCurrentSession().saveOrUpdate(workFlowEvent);
			wrkflwevntId=workFlowEvent.getWorkfloweventId();
			log.debug("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}		
		return wrkflwevntId;
	}

	@Override
	@Transactional
	public List<WorkPackageTestCaseExecutionPlan> getNotExecutedTestCaseListByJobId(Integer jobid) {
		List<WorkPackageTestCaseExecutionPlan> wpepListjob = null;
		Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackageTestCaseExecutionPlan.class, "wptcep");
		c.createAlias("wptcep.testRunJob", "testRunJob");
		c.add(Restrictions.eq("testRunJob.testRunJobId", jobid));
		c.add(Restrictions.eq("wptcep.isExecuted", 0));
		wpepListjob = c.list();		
		return wpepListjob;
	}

	@Override
	@Transactional
	public Integer getWPTCEPCountOfWPId(Integer testCaseId,
			Integer workPackageId) {
		log.debug("listing getWPTCEPCountOfATestCaseId instance");
		int wpTCCount = 0;
		String sql = "";
		if(testCaseId != null && testCaseId != 0){
			sql="select count(*) from workpackage_testcase_execution_plan wptcep where wptcep.testcaseId=:tcid and  wptcep.workPackageId=:workPackageId";
		}else{
			sql="select count(*) from workpackage_testcase_execution_plan wptcep where wptcep.workPackageId=:workPackageId";
		}
		
		try {
			if(testCaseId != null && testCaseId != 0){
				wpTCCount=((Number) sessionFactory.getCurrentSession()
						.createSQLQuery(sql).setParameter("tcid", testCaseId)
						.setParameter("workPackageId", workPackageId)						
						.uniqueResult()).intValue();	
			}else{
				wpTCCount=((Number) sessionFactory.getCurrentSession().createSQLQuery(sql)
						.setParameter("workPackageId", workPackageId)						
						.uniqueResult()).intValue();	
			}
		} catch (RuntimeException re) {
			log.error("list getWPTCEPCountOfATestCaseId", re);
		}
		return wpTCCount;
	}

	@Override
	@Transactional
	public List<WorkPackage> getWorkpackageSetForAHost(Integer hostId) {
		List<WorkPackage> workPackageSet = null;
		try{
			String sqlQuery =  "SELECT * FROM workpackage WHERE testRunPlanId IN (" +
					   "SELECT DISTINCT testRunPlanId FROM test_run_job WHERE hostId=:hostId)" +
					   "AND workfloweventid IN (SELECT DISTINCT workfloweventId FROM workflowevent WHERE workflowId IN (6,7,8))";		
			workPackageSet = sessionFactory.getCurrentSession().createSQLQuery(sqlQuery).addEntity(WorkPackage.class).setParameter("hostId", hostId).list();					
			if(workPackageSet.size()>0){				
				for (WorkPackage workPackage : workPackageSet) {
					Hibernate.initialize(workPackage.getTestRunJobSet());	
					Hibernate.initialize(workPackage.getUserList());					
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}		
		return workPackageSet;
	}
	
	@Override
	@Transactional
	public  List<TestRunJob> incompleteTestRunJobSetForAWorkpackage(Integer workpackageId){
		List<TestRunJob> incompleteTestRunJobSet = null;
		try{
			String sqlQuery =  "SELECT * FROM test_run_job WHERE workpackageId=:workpackageId AND testRunStatus NOT IN (5,7)";		
			incompleteTestRunJobSet = sessionFactory.getCurrentSession().createSQLQuery(sqlQuery).addEntity(TestRunJob.class).setParameter("workpackageId", workpackageId).list();			
		}catch(Exception e){
			e.printStackTrace();
		}				
		return incompleteTestRunJobSet;		
	}
	
	@Override
	@Transactional
	public  List<TestRunJob> completedAndAbortedTestRunJobSetForAWorkpackage(Integer workpackageId){
		List<TestRunJob> incompleteTestRunJobSet = null;
		try{
			String sqlQuery =  "SELECT * FROM test_run_job WHERE workpackageId=:workpackageId AND testRunStatus IN (5,7)";		
			incompleteTestRunJobSet = sessionFactory.getCurrentSession().createSQLQuery(sqlQuery).addEntity(TestRunJob.class).setParameter("workpackageId", workpackageId).list();			
		}catch(Exception e){
			e.printStackTrace();
		}				
		return incompleteTestRunJobSet;		
	}

	@Override
	@Transactional
	public List<WorkPackageDemandProjection> listWorkPackageDemandProjectionByWeekAndYear(int workPackageId, int shiftId, Set<Integer> recursiveWeeks, Integer workYear,Integer skillId,Integer roleId,Integer userTypeId) {

		List<WorkPackageDemandProjection> workPackageDemandProjectionList = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackageDemandProjection.class,"workPackageDemand");
			if(recursiveWeeks != null && recursiveWeeks.size() > 0){
				c.add(Restrictions.in("workPackageDemand.workWeek", recursiveWeeks));
			}
			
			if(workPackageId!=-1){
			c.createAlias("workPackage", "workPackage");	
			c.add(Restrictions.eq("workPackage.workPackageId", workPackageId));
		
			
			}
			
			if(workYear != null){
				c.add(Restrictions.eq("workPackageDemand.workYear", workYear));
			}
			if(shiftId!=-1 && shiftId!=0){
				c.createAlias("workPackageDemand.workShiftMaster", "shift");	
				c.add(Restrictions.eq("shift.shiftId", shiftId));
			}
			c.createAlias("workPackageDemand.userRole", "userRole");
			c.createAlias("workPackageDemand.skill", "skill");
			c.createAlias("workPackageDemand.userTypeMasterNew", "userType");
			ProjectionList projectionList = Projections.projectionList();
			projectionList = Projections.projectionList();
			
			projectionList.add(Property.forName("workPackage.workPackageId").as("workPackageId"));
			projectionList.add(Property.forName("workPackage.name").as("workPackageName"));
			projectionList.add(Property.forName("shift.shiftId").as("shiftId"));
			projectionList.add(Property.forName("shift.shiftName").as("shiftName"));
			projectionList.add(Property.forName("userRole.userRoleId").as("userRoleId"));
			projectionList.add(Property.forName("userRole.roleLabel").as("roleLabel"));
			projectionList.add(Property.forName("skill.skillId").as("skillId"));
			projectionList.add(Property.forName("skill.skillName").as("skillName"));
			projectionList.add(Property.forName("workPackageDemand.workWeek").as("workWeek"));
			projectionList.add(Property.forName("workPackageDemand.workYear").as("workYear"));
			projectionList.add(Projections.avg("workPackageDemand.resourceCount").as("totalResourceCount"));
			projectionList.add(Property.forName("workPackageDemand.wpDemandProjectionId").as("wpDemandProjectionId"));
			projectionList.add(Property.forName("workPackageDemand.groupDemandId").as("groupDemandId"));
			
			projectionList.add(Property.forName("userType.userTypeId").as("userTypeId"));
			projectionList.add(Property.forName("userType.userTypeLabel").as("userTypeLabel"));
			
			
			projectionList.add(Projections.groupProperty("workPackage.workPackageId"));
			projectionList.add(Projections.groupProperty("shift.shiftId"));
			projectionList.add(Projections.groupProperty("userRole.userRoleId"));
			projectionList.add(Projections.groupProperty("skill.skillId"));
			projectionList.add(Projections.groupProperty("workPackageDemand.workWeek"));
			projectionList.add(Projections.groupProperty("workPackageDemand.groupDemandId"));
			
			c.setProjection(projectionList);
			c.addOrder(Order.asc("workPackageDemand.workWeek"));
			
			List<Object[]> workpackageDemandList = c.list();
			
			if (!(workpackageDemandList == null || workpackageDemandList.isEmpty())) {
				workPackageDemandProjectionList = new ArrayList<WorkPackageDemandProjection>();
				for(Object[] obj:workpackageDemandList){
					WorkPackageDemandProjection workPackageDemandProjection = new WorkPackageDemandProjection();
					WorkPackage workPackage = new WorkPackage();
					workPackage.setWorkPackageId((Integer)obj[0]);
					workPackage.setName((String)obj[1]);
					workPackageDemandProjection.setWorkPackage(workPackage);
					
					WorkShiftMaster workShiftMaster = new WorkShiftMaster();
					workShiftMaster.setShiftId((Integer)obj[2]);
					workShiftMaster.setShiftName((String)obj[3]);
					workPackageDemandProjection.setWorkShiftMaster(workShiftMaster);
					
					UserRoleMaster userRoleMaster = new UserRoleMaster();
					userRoleMaster.setUserRoleId((Integer)obj[4]);
					userRoleMaster.setRoleLabel((String)obj[5]);
					userRoleMaster.setRoleName((String)obj[5]);
					workPackageDemandProjection.setUserRole(userRoleMaster);
					
					Skill skill = new Skill();
					skill.setSkillId((Integer)obj[6]);
					skill.setSkillName((String)obj[7]);
					workPackageDemandProjection.setSkill(skill);
					
					workPackageDemandProjection.setWorkWeek((Integer)obj[8]);
					workPackageDemandProjection.setWorkYear((Integer)obj[9]);
					workPackageDemandProjection.setResourceCount(((Double)obj[10]).floatValue());
					workPackageDemandProjection.setWpDemandProjectionId((Integer)obj[11]);
					workPackageDemandProjection.setGroupDemandId((Long)obj[12]);
					
					UserTypeMasterNew usertype = new UserTypeMasterNew();
					usertype.setUserTypeId((Integer)obj[13]);
					usertype.setUserTypeLabel((String)obj[14]);
					workPackageDemandProjection.setUserTypeMasterNew(usertype);
					
					workPackageDemandProjectionList.add(workPackageDemandProjection);
				}
				
				
			}
			log.debug("list successful");
		} catch (Exception re) {
			log.error("list failed", re);
		}
		return workPackageDemandProjectionList;
	}

	@Override
	@Transactional
	public List<TestFactoryResourceReservation> listWorkPackageResourceReservationByWorkpackageWeekAndYear(Integer workPackageId, Integer shiftId, Integer reservationWeek,Integer reservationYear,Integer resourceId) {

		List<TestFactoryResourceReservation> testFactoryResourceReservationList = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestFactoryResourceReservation.class,"resourceReservation");
			if(reservationWeek!=null)
				c.add(Restrictions.eq("resourceReservation.reservationWeek", reservationWeek));
			
			if(reservationYear!=null)
				c.add(Restrictions.eq("resourceReservation.reservationYear", reservationYear));
			
			if(workPackageId!=-1){
			c.createAlias("workPackage", "workPackage");	
			c.add(Restrictions.eq("workPackage.workPackageId", workPackageId));
		
			
			}
			if(shiftId!=-1 && shiftId!=0){
				c.createAlias("resourceReservation.shift", "shift");	
				c.add(Restrictions.eq("shift.shiftId", shiftId));
			}
			
			ProjectionList projectionList = Projections.projectionList();
			projectionList = Projections.projectionList();
			
			projectionList.add(Property.forName("workPackage.workPackageId").as("workPackageId"));
			projectionList.add(Property.forName("workPackage.name").as("workPackageName"));
			projectionList.add(Property.forName("shift.shiftId").as("shiftId"));
			projectionList.add(Property.forName("shift.shiftName").as("shiftName"));
			projectionList.add(Property.forName("resourceReservation.reservationWeek").as("reservationWeek"));
			projectionList.add(Property.forName("resourceReservation.reservationYear").as("reservationYear"));
			projectionList.add(Projections.avg("resourceReservation.reservationPercentage").as("reservationPercentage"));
			projectionList.add(Property.forName("resourceReservation.resourceReservationId").as("resourceReservationId"));
			projectionList.add(Property.forName("resourceReservation.groupReservationId").as("groupReservationId"));
			
			
			projectionList.add(Projections.groupProperty("workPackage.workPackageId"));
			projectionList.add(Projections.groupProperty("shift.shiftId"));
			projectionList.add(Projections.groupProperty("workPackageDemand.workWeek"));
			projectionList.add(Projections.groupProperty("workPackageDemand.groupDemandId"));
			
			c.setProjection(projectionList);
			
			List<Object[]> testFactoryResourceReservationObjList = c.list();
			
			if (!(testFactoryResourceReservationObjList == null || testFactoryResourceReservationObjList.isEmpty())) {
				testFactoryResourceReservationList = new ArrayList<TestFactoryResourceReservation>();
				for(Object[] obj:testFactoryResourceReservationObjList){
					
					TestFactoryResourceReservation testFactoryResourceReservation = new TestFactoryResourceReservation();
					
					WorkPackage workPackage = new WorkPackage();
					workPackage.setWorkPackageId((Integer)obj[0]);
					workPackage.setName((String)obj[1]);
					testFactoryResourceReservation.setWorkPackage(workPackage);
					
					WorkShiftMaster workShiftMaster = new WorkShiftMaster();
					workShiftMaster.setShiftId((Integer)obj[2]);
					workShiftMaster.setShiftName((String)obj[3]);
					testFactoryResourceReservation.setShift(workShiftMaster);
					
					
					
					testFactoryResourceReservation.setReservationWeek((Integer)obj[4]);
					testFactoryResourceReservation.setReservationYear(((Integer)obj[5]));
					testFactoryResourceReservation.setReservationPercentage(((Integer)obj[6]));
					
					testFactoryResourceReservation.setResourceReservationId((Integer)obj[7]);
					testFactoryResourceReservation.setGroupReservationId((Long)obj[8]);
					
					testFactoryResourceReservationList.add(testFactoryResourceReservation);
				}
				
				
			}
			log.debug("list successful");
		} catch (Exception re) {
			log.error("list failed", re);
		}
		return testFactoryResourceReservationList;
	}	
	
	
	
	
	@Override
	@Transactional
	public List<WorkPackageDemandProjection> getWorkPackageDemandProjectionByGroupDemandId(Long groupDemandId) {
		List<WorkPackageDemandProjection> list = new ArrayList<WorkPackageDemandProjection>();
		try{
			Criteria c =sessionFactory.getCurrentSession().createCriteria(WorkPackageDemandProjection.class,"wpdp");
			c.add((Restrictions.eq("wpdp.groupDemandId", groupDemandId)));
			list= c.list();
			
		}catch(Exception ex){
			log.error("Unable To Retrive ", ex);
		}
		return list;
	}

	@Override
	@Transactional
	public void addTestfactoryResourceReservationWeekly(TestFactoryResourceReservation testFactoryResourceReservation) {
		try{
			sessionFactory.getCurrentSession().save(testFactoryResourceReservation);
		}catch(Exception ex){
			log.error("Unable to add TestFactoryResourceReservation ", ex);
		}
	}

	@Override
	@Transactional
	public Integer countAllResourceDemands(Date startDate, Date endDate) {
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackageDemandProjection.class,"wdp");
			if (endDate != null) {
				c.add(Restrictions.le("wdp.workDate", endDate));
			}
			
			c.setProjection(Projections.rowCount());
			Integer count = Integer.parseInt(c.uniqueResult().toString());
			return count;
			
		} catch (Exception e) {
			log.error("Unable to get count of all WorkPackageDemandProjection", e);
			return -1;
		}
	}

	@Override
	@Transactional
	public List<WorkPackageDemandProjection> listAllResourceDemands(int startIndex, int pageSize, Date startDate, Date endDate) {
		log.debug("listing all EffortTracker");
		List<WorkPackageDemandProjection> workPackageDemandProjectionList=null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackageDemandProjection.class, "wpd");
			if (endDate != null) {
				c.add(Restrictions.le("wpd.workDate", endDate));
			}
			c.addOrder(Order.asc("wpDemandProjectionId"));
	        c.setFirstResult(startIndex);
	        c.setMaxResults(pageSize);
	        workPackageDemandProjectionList = c.list();		

			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return workPackageDemandProjectionList;
}

	@Override
	@Transactional
	public TestFactoryResourceReservation getTestFactoryResourceReservationById(Integer reservationId) {
		TestFactoryResourceReservation testFactoryResourceReservation = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestFactoryResourceReservation.class, "tfrr");
			c.add(Restrictions.eq("tfrr.resourceReservationId",  reservationId));
			List<TestFactoryResourceReservation> testFactoryResourceReservationList = c.list();

			if (testFactoryResourceReservationList.size() > 0)
				return (TestFactoryResourceReservation)testFactoryResourceReservationList.get(0);
			else 
				return null;
		} catch (RuntimeException re) {
			log.error("getBy TestFactoryResourceReservationByIdId failed", re);
		}catch (Exception re) {
			log.error("Exception occurres failed", re);
		}
		return testFactoryResourceReservation;
	}

	@Override
	@Transactional
	public Integer countAllReservedResources(Date startDate, Date endDate) {
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestFactoryResourceReservation.class,"tfrr");
			if (endDate != null) {
				c.add(Restrictions.le("tfrr.reservationActionDate", endDate));
			}
			
			c.setProjection(Projections.rowCount());
			Integer count = Integer.parseInt(c.uniqueResult().toString());
			return count;
			
		} catch (Exception e) {
			log.error("Unable to get count of all ReservedResources", e);
			return -1;
		}
	}

	@Override
	@Transactional
	public List<TestFactoryResourceReservation> listAllReservedResources(int startIndex, int pageSize, Date startDate, Date endDate) {
		log.debug("listing all ReservedResources");
		List<TestFactoryResourceReservation> testFactoryResourceReservationList=null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestFactoryResourceReservation.class, "tfrr");
			if (endDate != null) {
				c.add(Restrictions.le("tfrr.reservationActionDate", endDate));
			}
			c.addOrder(Order.asc("resourceReservationId"));
	        c.setFirstResult(startIndex);
	        c.setMaxResults(pageSize);
	        testFactoryResourceReservationList = c.list();		

			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return testFactoryResourceReservationList;
	}
	
	@Override
	@Transactional
	public List<String> generateTestRunListHtmlReport(Integer testRunNo, Integer testRunConfigurationChildId, String strPrintMode, ProductType productType, BufferedImage logo, String loginUserName) {
		List<String> htmlReportData = new ArrayList<String>();
		String jsonSourceFile = null;
		String jsonSourceSubFile1 = null;
		String jsonSourceSubFile2 = null;
		String jsonSourceSubFile3 = null;
		String jsonSourceSubFile4 = null;
		Integer testRunListId = null;
		log.info("Getting data from db for HTML Report");

		// Query for Report Summary
		String reportSummaryFile = null;
		if(databaseDialect != null && databaseDialect.equalsIgnoreCase("org.hibernate.dialect.MySQL5Dialect")) {
			reportSummaryFile = "SELECT rdevlist.testRunNo as testRunNo,rdevlist.testRunListId as testRunListId,rdevlist.testEnvironmentName as testEnvironmentName,rdevlist.productName as productName,rdevlist.productVersionName as productVersionName,rdevlist.deviceId as deviceId,rdevlist.deviceModel as deviceModel,rdevlist.deviceMake as deviceMake,rdevlist.devicePlatformVersion as devicePlatformVersion,rdevlist.devicePlatformName as devicePlatformName,rdevlist.testToolName as testToolName,rdevlist.hostName as hostName,rdevlist.hostIpAddress as hostIpAddress,"
					+ "rdevlist.testRunconfigurationName as testRunconfigurationName,rdevlist.testSuiteName as testSuiteId,rdevlist.testRunStatus as testRunStatus,rdevlist.testRunStartTime as testRunStartTime,rdevlist.testRunEndTime as testRunEndTime,rdevlist.testRunTriggeredTime as testRunTriggeredTime,rdevlist.testRunFailureMessage as testRunFailureMessage,"
					+ "rdevlist.testCaseName as testCaseName,rdevlist.testStep as testStep,rdevlist.testStepInput as testStepInput,rdevlist.testStepExpectedOutput as testStepExpectedOutput,rdevlist.testStepObservedOutput as testStepObservedOutput,rdevlist.testResultStatus as testResultStatus,rdevdet.passedTestecase AS passedTestecase,rdevdet.testCaseResult AS testCaseResult,"
					+ "rdevdet.totalTestCase AS totalTestCase, rdevlist.testRunConfigurationChildId AS testRunConfigurationChildId, "
					+ "rdevtestdata.totalTest as totalTest, rdevtestdata.passedTest as passedTest,rdevlist.totalTime AS totalTime FROM test_run_reports_device_list_view as rdevlist,(select testRunNo testRunNo,"
					+ " SUM(if(testResultStatus='PASSED',1,0))  AS passedTestecase,COUNT(testResultStatus) AS totalTestCase,if (SUM(if(testResultStatus='PASSED',1,0))=COUNT(testResultStatus),'PASSED', 'FAILED') AS testCaseResult from test_run_reports_device_details_view where  testRunNo="
					+ testRunNo
					+ " and testRunConfigurationChildId="
					+ testRunConfigurationChildId
					+ ") as rdevdet, "
					+ "(SELECT COUNT(testCaseResult) AS totalTest , SUM(IF(testCaseResult='PASSED',1,0)) AS passedTest FROM testrunreports_testcases_devicedetails_view  WHERE testRunNo="
					+ testRunNo
					+ " and testRunConfigurationChildId="
					+ testRunConfigurationChildId
					+ ")  AS rdevtestdata where rdevlist.testRunNo=rdevdet.testRunNo and  rdevlist.testRunNo="
					+ testRunNo + "";
		} else if(databaseDialect != null && databaseDialect.equalsIgnoreCase("org.hibernate.dialect.PostgreSQLDialect")) {
			reportSummaryFile = "SELECT distinct rdevlist.testRunNo as testRunNo,rdevlist.testRunListId as testRunListId,rdevlist.testEnvironmentName as testEnvironmentName,rdevlist.productName as productName,rdevlist.productVersionName as productVersionName,rdevlist.deviceId as deviceId,rdevlist.deviceModel as deviceModel,rdevlist.deviceMake as deviceMake,rdevlist.devicePlatformVersion as devicePlatformVersion,rdevlist.devicePlatformName as devicePlatformName,rdevlist.testToolName as testToolName,rdevlist.hostName as hostName,rdevlist.hostIpAddress as hostIpAddress,"
					+"rdevlist.testRunconfigurationName as testRunconfigurationName,rdevlist.testSuiteName as testSuiteId,rdevlist.testRunStatus as testRunStatus,"
					+"rdevlist.testRunStartTime as testRunStartTime,rdevlist.testRunEndTime as testRunEndTime,rdevlist.testRunTriggeredTime as testRunTriggeredTime,"
					+"rdevlist.testRunFailureMessage as testRunFailureMessage,rdevlist.testCaseName as testCaseName,rdevdet.passedTestecase AS passedTestecase,rdevdet.testCaseResult AS testCaseResult,rdevdet.totalTestCase AS totalTestCase, rdevlist.testRunConfigurationChildId AS testRunConfigurationChildId, rdevtestdata.totalTest as totalTest, rdevtestdata.passedTest as passedTest,TO_CHAR((rdevlist.totalTime || 'second')\\:\\:interval, 'HH24:MI:SS') AS totalTime FROM test_run_reports_device_list_view as rdevlist,(select testRunNo, sum (case when testResultStatus = 'PASSED' THEN 1 ELSE 0 END) as passedTestecase,COUNT(testResultStatus) AS totalTestCase,CASE WHEN (SUM(CASE WHEN testResultStatus='PASSED' THEN 1 ELSE 0 END)=COUNT(testResultStatus)) THEN 'PASSED' ELSE 'FAILED' END AS testCaseResult from test_run_reports_device_details_view where  testRunNo= "+testRunNo+" and testRunConfigurationChildId="+testRunConfigurationChildId+" group by testRunNo) as rdevdet, (SELECT COUNT(testCaseResult) AS totalTest , sum (case when testCaseResult = 'PASSED' THEN 1 ELSE 0 END) AS passedTest FROM testrunreports_testcases_devicedetails_view  WHERE testRunNo="+testRunNo+" and testRunConfigurationChildId="+testRunConfigurationChildId+")  AS rdevtestdata where rdevlist.testRunNo=rdevdet.testRunNo and  rdevlist.testRunNo="+testRunNo + "";
		}
		log.info("Dialect SQL Query : "+reportSummaryFile);
		SQLQuery strSourceFileQuery = sessionFactory.getCurrentSession()
				.createSQLQuery(reportSummaryFile);
		try{
			strSourceFileQuery
			.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
			List<Map<String, Object>> aliasToValueMapList = strSourceFileQuery
					.list();
			JSONArray jsonReportSummaryFileArray = new JSONArray();
			for (Map<String, Object> map : aliasToValueMapList) {
				JSONObject jsonReportSummaryObject = new JSONObject();
				for (Map.Entry<String, Object> entry : map.entrySet()) {
					String key = entry.getKey();
					Object value = entry.getValue();
					if(value == null || value.toString().isEmpty()){
						jsonReportSummaryObject.put(key,"N/A");
					} else {
						jsonReportSummaryObject.put(key, value);
					}
				}
				jsonReportSummaryFileArray.put(jsonReportSummaryObject);
			}
			jsonSourceFile = jsonReportSummaryFileArray.toString();
			Map<Integer, Integer> jobIdsList = new HashMap<Integer, Integer>();
			for (int i = 0; i < jsonReportSummaryFileArray.length(); i++) {
				JSONObject jsonObject = jsonReportSummaryFileArray.getJSONObject(i);
				testRunListId = jsonObject.getInt("testrunlistid");
				jobIdsList.putIfAbsent(testRunListId, i);
			}
			log.info("job id's are.." + jobIdsList);
			JSONArray jsonSubFileArray1 = new JSONArray();
			JSONArray jsonSubFileArray2 = new JSONArray();
			JSONArray jsonSubFileArray3 = new JSONArray();
			JSONArray jsonSubFileArray4 = new JSONArray();
			// Query for Job Report
			String jobReportFile = "SELECT DISTINCT testrunreports_devicelist_view.testRunListId AS Job_Id,"
					+ "testrunreports_devicelist_view.runconfigurationid AS runconfigid,"
					+ "testrunreports_devicelist_view.testEnvironmentName AS Job_Name,"
					+ "testrunreports_devicelist_view.reportmode as reportmode,"
					+ "testrunreports_devicelist_view.testRunNo AS testrunno,"
					+ "testrunreports_devicelist_view.deviceId AS Device_Id,"
					+ "testrunreports_devicelist_view.deviceMake AS deviceMake,"
					+ "testrunreports_devicelist_view.deviceModel AS deviceModel,"
					+ "testrunreports_devicelist_view.hostName AS hostname,"
					+ "testrunreports_devicelist_view.devicePlatformName AS Device_Platform,"
					+ "testrunreports_devicelist_view.testRunStatus AS Result,"
					+ "testrunreports_devicelist_view.testRunStartTime AS start_time,"
					+ "testrunreports_devicelist_view.testRunEndTime AS end_time,"
					+ "testrunreports_devicelist_view.hostIpAddress AS hostIpAddress,"
					+ "testrunreports_devicelist_view.testRunFailureMessage AS comments,"
					+ "(select (CASE WHEN SUM(CASE WHEN rdevdet.testCaseResult='FAILED' THEN 1 ELSE 0 END) > 0 THEN 'FAILED' ELSE 'PASSED' END ) FROM testrunreports_testcases_devicedetails_view AS rdevdet WHERE testRunNo="
					+ testRunNo +" AND testRunConfigurationChildId="+testRunConfigurationChildId+") AS testResultStatus"
					+ " FROM test_run_reports_device_list_view testrunreports_devicelist_view WHERE testrunreports_devicelist_view.testRunNo="
					+ testRunNo
					+ "  AND testrunreports_devicelist_view.testRunConfigurationChildId="
					+ testRunConfigurationChildId + "";

			log.info("1 : " +jobReportFile);
			SQLQuery strSourceSubRepFileQuery = sessionFactory.getCurrentSession()
					.createSQLQuery(jobReportFile);
			strSourceSubRepFileQuery
			.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
			List<Map<String, Object>> aliasToValueMapListSubFile1 = strSourceSubRepFileQuery
					.list();
			for (Map<String, Object> mapSubFile1 : aliasToValueMapListSubFile1) {
				JSONObject jsonObject = new JSONObject();
				for (Map.Entry<String, Object> entry : mapSubFile1.entrySet()) {
					String key = entry.getKey();
					Object value = entry.getValue();
					if(value == null || value.toString().isEmpty()){
						jsonObject.put(key,"N/A");
					} else {
						jsonObject.put(key, value);
					}
				}
				jsonSubFileArray1.put(jsonObject);
			}
			jsonSourceSubFile1 = jsonSubFileArray1.toString();
			for (Integer jobId : jobIdsList.keySet()) {
				String jobSummaryFile = "SELECT DISTINCT rdevlist.testRunNo AS testRunNo,rdevlist.testRunListId AS testRunListId,"
						+ " rdevlist.productName AS productName,"
						+ " rdevlist.productVersionName AS productVersionName,"
						+ " rdevlist.deviceId AS deviceId,rdevlist.devicePlatformName AS devicePlatformName,"
						+ " rdevlist.devicePlatformVersion AS devicePlatformVersion,rdevlist.deviceModel AS deviceModel,"
						+ " rdevlist.testRunconfigurationName AS testRunconfigurationName,rdevlist.testSuiteId AS testSuiteId,"
						+ " rdevlist.testSuiteName AS testSuiteName,rdevlist.testRunStatus AS testRunStatus,rdevlist.testRunStartTime AS testRunStartTime,"
						+ " rdevlist.testRunEndTime AS testRunEndTime,rdevlist.testRunFailureMessage AS testRunFailureMessage,"
						+ " rdevlist.testCaseName AS testCaseName,rdevlist.testResultStatus AS testResultStatus,"
						+ " SUM(CASE WHEN rdevdet.testResultStatus='PASSED' THEN 1 ELSE 0 END )  AS passedTestecase," 
						+ " COUNT(rdevdet.testResultStatus) AS totalTestCase,CASE WHEN SUM(CASE WHEN rdevdet.testResultStatus='PASSED' THEN 1 ELSE 0 END )=COUNT(rdevdet.testResultStatus) THEN 'PASSED' ELSE 'FAILED' END AS testCaseResult,"
						+ " rdevlist.testRunConfigurationChildId AS testRunConfigurationChildId,"
						+ " rdevtestdata.passedTest AS passedTest,rdevtestdata.totalTest AS totalTest," 
						+ " TO_CHAR((rdevlist.totalTime || 'second')\\:\\:interval, 'HH24:MI:SS') AS totalTime," 
						+ " rdevlist.testToolName AS testToolName FROM test_run_reports_device_list_view AS rdevlist,"
						+ " test_run_reports_device_details_view AS rdevdet,(SELECT COUNT(testCaseResult) AS totalTest ,"
						+ " SUM(CASE WHEN testCaseResult='PASSED' THEN 1 ELSE 0 END) AS passedTest FROM testrunreports_testcases_devicedetails_view WHERE testRunNo="
						+ testRunNo
						+"AND testRunConfigurationChildId="
						+ testRunConfigurationChildId
						+ " AND testRunListId="+jobId+")" 
						+ " AS rdevtestdata WHERE rdevlist.testRunNo=rdevdet.testRunNo AND rdevlist.testRunListId=rdevdet.testRunListId AND"
						+ "  rdevlist.testRunNo="
						+ testRunNo
						+ " AND rdevlist.testRunConfigurationChildId="
						+ testRunConfigurationChildId
						+ " AND rdevlist.testRunListId="
						+ jobId
						+ "GROUP BY rdevlist.testRunNo ,"
						+ " rdevlist.testRunListId ,rdevlist.productName,rdevlist.productVersionName,rdevlist.deviceId ,rdevlist.devicePlatformName ,rdevlist.devicePlatformVersion,rdevlist.deviceModel,"
						+ " rdevlist.testRunconfigurationName ,rdevlist.testSuiteId ,rdevlist.testSuiteName ,"
						+ " rdevlist.testRunStatus,rdevlist.testRunStartTime ,rdevlist.testRunEndTime,"
						+ " rdevlist.testRunFailureMessage,rdevlist.testCaseName,rdevlist.testStep,"
						+ " rdevlist.testStepInput,rdevlist.testStepExpectedOutput ,rdevlist.testStepObservedOutput ,"
						+ " rdevlist.testResultStatus,rdevlist.testRunConfigurationChildId, rdevtestdata.passedTest, "
						+ " rdevtestdata.totalTest, rdevlist.totalTime ,rdevlist.testToolName"+";";


				log.info("2 : " +jobSummaryFile);
				SQLQuery strSourceSubRepFileQuery2 = sessionFactory
						.getCurrentSession().createSQLQuery(jobSummaryFile);
				strSourceSubRepFileQuery2
				.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
				List<Map<String, Object>> aliasToValueMapListSubFile2 = strSourceSubRepFileQuery2
						.list();
				for (Map<String, Object> mapSubFile2 : aliasToValueMapListSubFile2) {
					JSONObject jsonObject = new JSONObject();
					for (Map.Entry<String, Object> entry : mapSubFile2.entrySet()) {
						String key = entry.getKey();
						Object value = entry.getValue();
						if(value == null || value.toString().isEmpty()){
							jsonObject.put(key,"N/A");
						} else {
							jsonObject.put(key, value);
						}
					}
					jsonSubFileArray2.put(jsonObject);
				}
				jsonSourceSubFile2 = jsonSubFileArray2.toString();
				// Query for Test Case Summary(mutliple test cases)
				String testCaseSummaryFile = "SELECT DISTINCT testrunreports_testcases_devicedetails_view.testcaseexecutionid AS testcaseexecutionid,"
						+ " testrunreports_testcases_devicedetails_view.testCaseId AS Test_Case_Id,"
						+ " testrunreports_testcases_devicedetails_view.testRunListId AS Job_Id,"
						+" testrunreports_testcases_devicedetails_view.actualtestjobid AS actualtestjobid,"
						+ " testrunreports_testcases_devicedetails_view.testCaseCode AS Test_Case_Code,"
						+ " testrunreports_testcases_devicedetails_view.testCaseName AS Test_Case,"
						+ " testrunreports_testcases_devicedetails_view.testCaseDescription AS Description,"
						+ " testrunreports_testcases_devicedetails_view.testCaseResult AS Result,"
						+ " testrunreports_testcases_devicedetails_view.testCaseStartTime AS StartTime,"
						+ " testrunreports_testcases_devicedetails_view.testCaseEndTime AS EndTime,"
						+ " TO_CHAR((testrunreports_testcases_devicedetails_view.totalTime || 'second')\\:\\:interval, 'HH24:MI:SS') AS totalTime,"
						+ " testrunreports_testcases_devicedetails_view.passedTeststep AS passedTestStep,"
						+ " testrunreports_testcases_devicedetails_view.totalTeststep AS totalTestStep,"
						+ " string_agg(distinct testrunreports_testcases_devicedetails_view.productFeatureName, ',') AS productFeatureName,"
						+ " string_agg(distinct testrunreports_testcases_devicedetails_view.riskName, ',') AS riskName,"
						+ " testrunreports_testcases_devicedetails_view.testCaseCode AS testCaseCode,"
						+ " (SELECT TO_CHAR((SUM(totalTime) || 'second')\\:\\:interval, 'HH24:MI:SS') FROM  testrunreports_testcases_devicedetails_view WHERE testRunListId="
						+ jobId
						+ "  AND"
						+ " testRunConfigurationChildId="
						+ testRunConfigurationChildId
						+ ") AS totalTCExecTime"
						+ " FROM testrunreports_testcases_devicedetails_view testrunreports_testcases_devicedetails_view"
						+ " WHERE testRunListId=" + jobId + "  AND"
						+ " testRunConfigurationChildId="
						+ testRunConfigurationChildId  
						+" GROUP BY testrunreports_testcases_devicedetails_view.actualtestjobid,testrunreports_testcases_devicedetails_view.testCaseId , testrunreports_testcases_devicedetails_view.testRunListId , testrunreports_testcases_devicedetails_view.testCaseCode, testrunreports_testcases_devicedetails_view.testCaseName, testrunreports_testcases_devicedetails_view.testCaseDescription, testrunreports_testcases_devicedetails_view.testCaseResult , testrunreports_testcases_devicedetails_view.testCaseStartTime, testrunreports_testcases_devicedetails_view.testCaseEndTime ,"
						+" testrunreports_testcases_devicedetails_view.totalTime, testrunreports_testcases_devicedetails_view.passedTeststep, testrunreports_testcases_devicedetails_view.totalTeststep, testrunreports_testcases_devicedetails_view.testCaseCode, testrunreports_testcases_devicedetails_view.testcaseexecutionid";
				log.info("3 : "+testCaseSummaryFile);
				SQLQuery strSourceSubRepFileQuery3 = sessionFactory
						.getCurrentSession().createSQLQuery(testCaseSummaryFile);
				strSourceSubRepFileQuery3
				.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
				List<Map<String, Object>> aliasToValueMapListSubFile3 = strSourceSubRepFileQuery3
						.list();

				log.info("source sub file3 results...");

				for (Map<String, Object> mapSubFile3 : aliasToValueMapListSubFile3) {
					JSONObject jsonObject = new JSONObject();
					for (Map.Entry<String, Object> entry : mapSubFile3.entrySet()) {
						String key = entry.getKey();
						Object value = entry.getValue();

						if(value == null || value.toString().isEmpty()){
							jsonObject.put(key,"N/A");
						} else {
							jsonObject.put(key, value);
						}
					}
					jsonSubFileArray3.put(jsonObject);
				}
				jsonSourceSubFile3 = jsonSubFileArray3.toString();

				// Query for Test Step ID's associated with Test Case(mutliple test Step ID's)
				/*String testStepIdFile = "SELECT DISTINCT testStepDeviceData.teststepexecutionid AS teststepexecutionid,testStepDeviceData.screenShotPath AS Screenshot,testStepDeviceData.testRunListId AS Job_Id,testStepDeviceData.testStepId AS TestStepId,testStepDeviceData.testStepName AS TestStep ,testStepDeviceData.testStepCode AS TestStepCode, testStepDeviceData.testStepDescription AS TestStepDescription,testStepDeviceData.testStepExpectedOutput AS ExpectedOutput,testStepDeviceData.testStepObservedOutput AS ObservedOutput,testStepDeviceData.testStepResult AS ResultStatus,testStepDeviceData.failureReason  AS failureReason,testStepDeviceData.executionRemarks  AS executionRemarks,testStepDeviceData.testStepStartTime  AS testStepStartTime, testStepDeviceData.testStepEndTime  AS testStepEndTime,testCaseDeviceData.testCaseId AS testCaseId,testCaseDeviceData.testCaseName AS testCaseName,testCaseDeviceData.testCaseCode AS testCaseCode,testCaseDeviceData.testCaseDescription AS testCaseDescription,testCaseDeviceData.testCaseResult AS testCaseResult, testCaseDeviceData.testCaseStartTime AS testCaseStartTime,testCaseDeviceData.testCaseEndTime AS testCaseEndTime,testCaseDeviceData.testcaseexpectedoutput AS testCaseExpectedOutput,testCaseDeviceData.observedOutput AS testCaseObservedOutput, testCaseDeviceData.passedTeststep AS passedTeststep,testCaseDeviceData.totalTeststep AS totalTeststep, testCaseDeviceData.testRunFailureMessage AS testCaseFailureReason,"
										+"TO_CHAR((testCaseDeviceData.totalTime || 'second')\\:\\:interval, 'HH24:MI:SS') AS totalTestCasesTime,TO_CHAR((testStepDeviceData.totalTime || 'second')\\:\\:interval, 'HH24:MI:SS') AS totalTestStepsTime,string_agg(distinct testCase_ProductFeature_group.feature_names , ',') AS feature_names ,string_agg(distinct testCase_ProductFeature_group.feature_ids\\:\\:text , ',')  AS feature_ids,string_agg(distinct testCase_risk_group.risk_ids\\:\\:text , ', ') AS risk_ids,string_agg(distinct testCase_risk_group.risk_names, ', ' ) AS risk_names FROM testrunreports_testcases_devicedetails_view testCaseDeviceData LEFT JOIN(SELECT testCaseId,  CONCAT(m.productFeatureId) feature_ids,concat(productFeatureName) feature_names FROM product_feature_has_test_case_list m LEFT JOIN product_feature d ON (m.productFeatureId = d.productFeatureId) GROUP BY testCaseId, m.productFeatureId, productFeatureName) testCase_ProductFeature_group ON testCaseDeviceData.testCaseId = testCase_ProductFeature_group.testCaseId LEFT JOIN (SELECT testCaseId, CONCAT(n.productRiskId) risk_ids, concat(riskName) risk_names FROM product_risk_has_test_case_list n LEFT JOIN risk_list o ON (n.productRiskId = o.riskId) GROUP BY testCaseId,n.productRiskId, o.riskName) testCase_risk_group ON testCase_ProductFeature_group.testCaseId = testCase_risk_group.testCaseId, testrunreports_teststeps_devicedetails_view testStepDeviceData WHERE testStepDeviceData.testRunConfigurationChildId = testCaseDeviceData.testRunConfigurationChildId AND   testStepDeviceData.testRunNo = testCaseDeviceData.testRunNo AND testStepDeviceData.testCaseId = testCaseDeviceData.testCaseId AND testStepDeviceData.testRunListId = testCaseDeviceData.testRunListId AND testStepDeviceData.testRunNo = "
										+ testRunNo
										+ " AND testStepDeviceData.testRunConfigurationChildId="
										+ testRunConfigurationChildId
										+ " AND testStepDeviceData.testRunListId="
										+ jobId
										+ " GROUP BY testStepDeviceData.screenShotPath,testStepDeviceData.testRunListId ,testStepDeviceData.testStepId ,testStepDeviceData.testStepName,testStepDeviceData.testStepCode, testStepDeviceData.testStepDescription ,testStepDeviceData.testStepExpectedOutput ,testStepDeviceData.testStepObservedOutput ,testStepDeviceData.testStepResult,testStepDeviceData.failureReason ,testStepDeviceData.executionRemarks,testStepDeviceData.testStepStartTime, testStepDeviceData.testStepEndTime ,testCaseDeviceData.testCaseId,testCaseDeviceData.testCaseName,testCaseDeviceData.testCaseCode,testCaseDeviceData.testCaseDescription,testCaseDeviceData.testCaseResult, testCaseDeviceData.testCaseStartTime ,testCaseDeviceData.testCaseEndTime ,testCaseDeviceData.testcaseexpectedoutput,testCaseDeviceData.observedOutput, testCaseDeviceData.passedTeststep,testCaseDeviceData.totalTeststep,  testCaseDeviceData.testRunFailureMessage, testCaseDeviceData.testRunFailureMessage,testCaseDeviceData.totalTime,testStepDeviceData.totalTime,testStepDeviceData.teststepexecutionid";
				 */
				String testStepIdFile = "SELECT DISTINCT testStepDeviceData.teststepexecutionid AS teststepexecutionid,"
						+"testStepDeviceData.teststepinput AS teststepinput,"
						+"testStepDeviceData.screenShotPath AS Screenshot,testStepDeviceData.testRunListId AS combined_Job_Id,"
						+"testStepDeviceData.testjobid AS Job_Id,testStepDeviceData.testStepId AS TestStepId,"
						+"testStepDeviceData.testStepName AS TestStep ,"
						+"testStepDeviceData.testStepCode AS TestStepCode,"
						+"testStepDeviceData.testStepDescription AS TestStepDescription,"
						+"testStepDeviceData.testStepExpectedOutput AS ExpectedOutput,"
						+"testStepDeviceData.testStepObservedOutput AS ObservedOutput,"
						+"testStepDeviceData.testStepResult AS ResultStatus,"
						+"testStepDeviceData.testenvironmentname AS testenvironmentname,"
						+"testStepDeviceData.failureReason  AS failureReason,"
						+"testStepDeviceData.executionRemarks  AS executionRemarks,"
						+"testStepDeviceData.testStepStartTime  AS testStepStartTime,"
					    +"testStepDeviceData.testStepEndTime  AS testStepEndTime,"
						+"testCaseDeviceData.testCaseId AS testCaseId,testCaseDeviceData.testcasename AS testcasename,TO_CHAR((testStepDeviceData.totalTime || 'second')\\:\\:interval, 'HH24:MI:SS') AS totalTestStepsTime,"
						+"string_agg(distinct testCase_ProductFeature_group.feature_names , ',') AS feature_names ,"
						+"string_agg(distinct testCase_ProductFeature_group.feature_ids\\:\\:text , ',')  AS feature_ids,"
						+"string_agg(distinct testCase_risk_group.risk_ids\\:\\:text , ', ') AS risk_ids,"
						+"string_agg(distinct testCase_risk_group.risk_names, ', ' ) AS risk_names FROM "
						+ "testrunreports_testcases_devicedetails_view testCaseDeviceData LEFT JOIN(SELECT testCaseId,  CONCAT(m.productFeatureId) feature_ids,concat(productFeatureName) feature_names FROM product_feature_has_test_case_list m LEFT JOIN product_feature d ON (m.productFeatureId = d.productFeatureId) GROUP BY testCaseId, m.productFeatureId, productFeatureName) testCase_ProductFeature_group "
						+ "ON testCaseDeviceData.testCaseId = testCase_ProductFeature_group.testCaseId LEFT JOIN (SELECT testCaseId, CONCAT(n.productRiskId) risk_ids, concat(riskName) risk_names FROM product_risk_has_test_case_list n LEFT JOIN risk_list o ON (n.productRiskId = o.riskId) "
						+ "GROUP BY testCaseId,n.productRiskId, o.riskName) testCase_risk_group ON testCase_ProductFeature_group.testCaseId = testCase_risk_group.testCaseId,"
						+ " testrunreports_teststeps_devicedetails_view testStepDeviceData WHERE testStepDeviceData.testRunConfigurationChildId = testCaseDeviceData.testRunConfigurationChildId AND   testStepDeviceData.testRunNo = testCaseDeviceData.testRunNo AND testStepDeviceData.testCaseId = testCaseDeviceData.testCaseId AND testStepDeviceData.testRunListId = testCaseDeviceData.testRunListId AND"
						+ " testStepDeviceData.testRunNo ="+ testRunNo + " AND testStepDeviceData.testRunConfigurationChildId="+testRunConfigurationChildId+" AND testStepDeviceData.testRunListId="+jobId+""
						+ " GROUP BY testStepDeviceData.screenShotPath,testStepDeviceData.testenvironmentname,testStepDeviceData.testRunListId ,testStepDeviceData.testStepId ,testStepDeviceData.testStepName,testStepDeviceData.testStepCode, testStepDeviceData.testStepDescription ,testStepDeviceData.testStepExpectedOutput ,"
						+ "testStepDeviceData.testStepObservedOutput ,testStepDeviceData.testStepResult,testStepDeviceData.failureReason ,testStepDeviceData.executionRemarks,"
						+ "testStepDeviceData.testStepStartTime, testStepDeviceData.testStepEndTime ,testStepDeviceData.totalTime,testStepDeviceData.teststepexecutionid,testStepDeviceData.testjobid,testCaseDeviceData.testCaseId,testCaseDeviceData.testcasename,testStepDeviceData.teststepinput;";

						log.info("4 : " +testStepIdFile);
				SQLQuery strSourceSubRepFileQuery4 = sessionFactory
						.getCurrentSession().createSQLQuery(testStepIdFile);
				strSourceSubRepFileQuery4
				.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
				List<Map<String, Object>> aliasToValueMapListSubFile4 = strSourceSubRepFileQuery4
						.list();

				log.info("source sub file4 results...");

				for (Map<String, Object> mapSubFile4 : aliasToValueMapListSubFile4) {
					JSONObject jsonObject = new JSONObject();
					for (Map.Entry<String, Object> entry : mapSubFile4.entrySet()) {
						String key = entry.getKey();
						Object value = entry.getValue();
						if(value == null || value.toString().isEmpty()){
							jsonObject.put(key,"N/A");
						} else {
							jsonObject.put(key, value);
						}
					}
					jsonSubFileArray4.put(jsonObject);
				}
				jsonSourceSubFile4 = jsonSubFileArray4.toString();
			}
			htmlReportData.add(jsonSourceFile);
			htmlReportData.add(jsonSourceSubFile1);
			htmlReportData.add(jsonSourceSubFile2);
			htmlReportData.add(jsonSourceSubFile3);
			htmlReportData.add(jsonSourceSubFile4);
		}catch(Exception e){
			log.error("Error : "+e.getMessage());
			htmlReportData=new ArrayList<String>();
		}
		return htmlReportData;
	}

	@Override
	@Transactional
	public WorkPackage getLatestWorkPackageByproductId(Integer productId,String wpName) {
		List<WorkPackage> wps = new ArrayList<WorkPackage>();
		WorkPackage workPackage = null;
		try{
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackage.class, "wpk");
			c.createAlias("wpk.productBuild", "build");
			c.createAlias("build.productVersion", "version");
			c.createAlias("version.productMaster", "product");
			
			c.add(Restrictions.eq("product.productId", productId));
			c.add(Restrictions.eq("wpk.isActive", 1));
			c.add(Restrictions.eq("wpk.name", wpName));
			wps = c.list();
			if (wps.size() > 0)
				workPackage =  (WorkPackage)wps.get(0);
		}catch(Exception ex){
			log.error("Unable to get WP", ex);
		}
		return workPackage;
		
	}
		
	@Override
    @Transactional
	public WorkPackage getWorkPackage(WorkPackage wp){
		return (WorkPackage) sessionFactory.getCurrentSession().merge(wp);
	}
	
	@Override
	@Transactional
	public List<WorkPackageTCEPSummaryDTO> getWPTCExecutionSummaryByTestRunPlanId(Integer testRunPlanId , Integer jtStartIndex, Integer jtPageSize) {		
		List<WorkPackageTCEPSummaryDTO> wptcepResultList = new LinkedList<WorkPackageTCEPSummaryDTO>();
		try {
			String sql="select wp.workpackageId as workPackageId,wp.name as workPackageName ,"
					+ "wp.actualStartDate as actualStartDate, wp.actualEndDate as actualEndDate, wflow.stageName as workFlowStageName,"
					+ "(select etm.name from execution_type_master as etm  where etm.executionTypeId =wp.workPackageType ) as exeType,"
					+ "(SELECT COUNT(wphastc.workPackageId) FROM  workpackage_has_testcase wphastc "
					+ "WHERE wphastc.workPackageId =wp.workpackageId ) AS testCaseCount, "
					+ "(SELECT COUNT(wphasTestSuite.testSuiteId) FROM workpackage_has_testsuite  wphasTestSuite "
					+ "WHERE wphasTestSuite.workpackageId=wp.workpackageId ) AS testSuiteCount, "
					+ "(SELECT COUNT(wphasFeature.featureId) FROM   workpackage_has_feature  wphasFeature "
					+ "WHERE wphasFeature.workpackageId=wp.workpackageId ) AS featueCount, "
					+ "(SELECT COUNT(job.workpackageId) FROM test_run_job job WHERE job.workpackageId =wp.workpackageId  ) AS jobCount, "

					+ "(SELECT COUNT(job.workpackageId) FROM test_run_job job WHERE job.workpackageId =wp.workpackageId and job.testRunStatus=3 ) AS jobsExecuting, "

					+ "(SELECT COUNT(job.workpackageId) FROM test_run_job job WHERE job.workpackageId =wp.workpackageId and job.testRunStatus=4 ) AS jobsQueued, "

					+ "(SELECT COUNT(job.workpackageId) FROM test_run_job job WHERE job.workpackageId =wp.workpackageId and job.testRunStatus=5 ) AS jobsCompleted, "

					+ "(SELECT COUNT(job.workpackageId) FROM test_run_job job WHERE job.workpackageId =wp.workpackageId and job.testRunStatus=7 ) AS jobsAborted, "
					
					+ "(SELECT COUNT(buglist.testExecutionResultBugId) FROM workpackage_testcase_execution_plan AS wptcPlan "
					+ "LEFT JOIN test_execution_result_bug_list AS buglist ON wptcPlan.wptcepId=buglist.testCaseExecutionResultId "
					+ "WHERE wptcPlan.workPackageId=wp.workpackageId) AS defectsCount, "
					+ "( SELECT COUNT(tcres.result)   FROM workpackage_testcase_execution_plan as wptcplan "
					+ "inner join  testcase_execution_result  AS tcres "
					+ "on tcres.testCaseExecutionResultId = wptcplan.wptcepId and wptcplan.status=1 "
					+ "where wptcplan.workpackageId= wp.workpackageId  and tcres.result='PASSED' ) AS passedCount,"
					+ " ( SELECT COUNT(tcres.result)   FROM  workpackage_testcase_execution_plan as wptcplan "
					+ "inner join testcase_execution_result  AS tcres "
					+ "on tcres.testCaseExecutionResultId = wptcplan.wptcepId and wptcplan.status=1 "
					+ " WHERE wptcplan.workpackageId= wp.workpackageId AND tcres.result='FAILED' ) AS failedCount,"
					+ " ( SELECT COUNT(tcres.result)   FROM  workpackage_testcase_execution_plan as wptcplan "
					+ "inner join testcase_execution_result  AS tcres "
					+ "on tcres.testCaseExecutionResultId = wptcplan.wptcepId and wptcplan.status=1"
					+ " WHERE wptcplan.workpackageId = wp.workpackageId AND tcres.result='BLOCKED' ) AS blockedCount,"
					+ " ( SELECT COUNT(tcres.result)   FROM  workpackage_testcase_execution_plan as wptcplan "
					+ "inner join testcase_execution_result  AS tcres "
					+ "on tcres.testCaseExecutionResultId = wptcplan.wptcepId and wptcplan.status=1 "
					+ "WHERE wptcplan.workpackageId = wp.workpackageId AND tcres.result='NORUN' ) AS norunCount,"
					+ "  ( SELECT COUNT(tcres.result)   FROM  workpackage_testcase_execution_plan as wptcplan "
					+ "inner join testcase_execution_result  AS tcres "
					+ "on tcres.testCaseExecutionResultId= wptcplan.wptcepId and wptcplan.status = 1"
					+ " WHERE wptcplan.workpackageId = wp.workpackageId AND tcres.result IS NULL ) AS notexecutedCount, "
					+ "(SELECT COUNT(tser.teststepexecutionresultid) FROM teststep_execution_result as tser "
					+ "inner join  testcase_execution_result  AS tcres "
					+ "on tcres.testCaseExecutionResultId = tser.tescaseexecutionresultid "
					+ "inner join workpackage_testcase_execution_plan as wptcplan "
					+ "on wptcplan.wptcepId = tcres.testCaseExecutionResultId "
					+ "where wptcplan.workpackageId= wp.workpackageId )  AS teststepcount "
					
					+ "FROM workpackage wp left JOIN workflowevent AS wfevent on wp.workfloweventid = wfevent.workfloweventId "
					+ "left join workflow as wflow on wfevent.workflowId = wflow.workflowId "
					+ "INNER JOIN  product_build pb ON pb.productBuildId=wp.productBuildId "
					+ "where wp.testRunPlanId = "+ testRunPlanId
					+ " order by  wp.workpackageId desc";
					
				
			if(jtStartIndex != -1 && jtPageSize != -1 ){
				sql = sql + "limit ";								
				sql = sql+jtStartIndex+" ,"+jtPageSize+"";	
			}
					
			wptcepResultList=sessionFactory.getCurrentSession().createSQLQuery(sql)
					.addScalar("workPackageId",StandardBasicTypes.INTEGER ).addScalar("workPackageName", StandardBasicTypes.STRING)
					.addScalar("actualStartDate",StandardBasicTypes.DATE ).addScalar("actualEndDate", StandardBasicTypes.DATE)
					.addScalar("workFlowStageName", StandardBasicTypes.STRING).addScalar("exeType", StandardBasicTypes.STRING)					
					.addScalar("testCaseCount", StandardBasicTypes.INTEGER)
					.addScalar("testSuiteCount", StandardBasicTypes.INTEGER).addScalar("featueCount", StandardBasicTypes.INTEGER)					
					.addScalar("jobCount", StandardBasicTypes.INTEGER)
					.addScalar("jobsExecuting", StandardBasicTypes.INTEGER).addScalar("jobsQueued", StandardBasicTypes.INTEGER)
					.addScalar("jobsCompleted", StandardBasicTypes.INTEGER).addScalar("jobsAborted", StandardBasicTypes.INTEGER)					
					.addScalar("defectsCount", StandardBasicTypes.INTEGER)
					.addScalar("passedCount", StandardBasicTypes.INTEGER).addScalar("failedCount", StandardBasicTypes.INTEGER)
					.addScalar("blockedCount", StandardBasicTypes.INTEGER).addScalar("norunCount", StandardBasicTypes.INTEGER)
					.addScalar("notexecutedCount", StandardBasicTypes.INTEGER).addScalar("teststepcount", StandardBasicTypes.INTEGER)					
					.setResultTransformer(Transformers.aliasToBean(WorkPackageTCEPSummaryDTO.class)).list();
		} catch (RuntimeException re) {
		log.error("getWPTCExecutionSummary by BuildId failed", re);
			re.printStackTrace();
		}
		return wptcepResultList;	
	}
	
	public TestRunJob createJobForMultiTS(TestRunPlan testRunPlan, HostList host, GenericDevices genericDevice, EnvironmentCombination environmentCombination,WorkPackage workPackage,Set<TestSuiteList> testSuiteList,RunConfiguration runConfiguration,String testcaseNames){
		try {
			//Get the device status
			String hostStatus=host.getCommonActiveStatusMaster().getStatus();
			log.info("Host Status is : " + hostStatus);		
			//Create a new TestRunJob entry for the device
			TestRunJob testRunJob = new TestRunJob();
			testRunJob.setWorkPackage(workPackage);		
			testRunJob.setHostList(host);
			testRunJob.setGenericDevices(genericDevice);
			testRunJob.setEnvironmentCombination(environmentCombination);
			testRunJob.setTestRunStatus(0);
			testRunJob.setTestRunPlan(testRunPlan);
			testRunJob.setRunConfiguration(runConfiguration);
			testRunJob.setTestRunTriggeredTime(DateUtility.getCurrentTime());			
			
			//Added support for Multiple Test SuiteList support
			if(testRunPlan.getTestScriptsLevel().equalsIgnoreCase(IDPAConstants.MULTIPLE_TEST_RUN_PLAN_LEVEL_SCRIPT_PACK)){
				if(testRunPlan.getTestSuiteScriptFileLocation() != null && testRunPlan.getTestSuiteScriptFileLocation() != "")
					testRunJob.setScriptPathLocation(testRunPlan.getTestSuiteScriptFileLocation());
			} else if(testRunPlan.getTestScriptsLevel().equalsIgnoreCase(IDPAConstants.MULTIPLE_RUNCONFIG_LEVEL_SCRIPT_PACK)){
				if(runConfiguration != null && runConfiguration.getTestScriptFileLocation() != null && runConfiguration.getTestScriptFileLocation() != "")
					testRunJob.setScriptPathLocation(runConfiguration.getTestScriptFileLocation());
			}
			if(hostStatus.equalsIgnoreCase("INACTIVE")){										
				log.info("Ignoring job - Host is not active or busy : " + host.getHostIpAddress());            
				//Device is not active. No need to publish the job. Set the reason for failure alone						
				testRunJob.setTestRunFailureMessage("Job could not be executed because the Host is offline!");				
				testRunJob.setTestRunStatus(IDPAConstants.JOB_ABORTED);
				addTestRunJob(testRunJob);
				
				for(TestSuiteList tsl : testSuiteList){
					mapTestRunJobTestSuite(testRunJob.getTestRunJobId(),tsl.getTestSuiteId(), "Add");
					List<TestCaseList> testCaseLists=productMasterDAO.getTestSuiteTestCaseMapped(testRunPlan.getTestRunPlanId(), tsl.getTestSuiteId());
					mapTestRunJobTestSuiteTestCase(testRunJob.getTestRunJobId(),tsl.getTestSuiteId(),testCaseLists, "Add");
				}
			} else if(hostStatus.equalsIgnoreCase("ACTIVE")) {						
				//queue the run and inform the client/terminal
				testRunJob.setTestRunStatus(4); //Not updating WP, as current job's status is completed, and could update WP, based on overall Jobs.
				
				addTestRunJob(testRunJob);	
				for(TestSuiteList tsl : testSuiteList){
					mapTestRunJobTestSuite(testRunJob.getTestRunJobId(),tsl.getTestSuiteId(), "Add");
				}
				
 				if(testRunJob!=null){
					if(testRunJob.getTestRunJobId()!=null)
					mongoDBService.addTestRunJobToMongoDB(testRunJob.getTestRunJobId());
				}
 				//For multiple Test Suite Support, change here to iterate TestSuite, 
 				for(TestSuiteList tsl : testSuiteList){
 					List<TestCaseList> testCaseListTotal = new LinkedList<TestCaseList>(); 					
 					//Changed logic for getting mapped testcases from test configuration instead of testplan
 					testCaseListTotal=productMasterDAO.getRunConfigTestSuiteTestCaseMapped(runConfiguration.getRunconfigId(), tsl.getTestSuiteId());
 	 				Set<TestCaseList> testCases=new HashSet<TestCaseList>();
 	 				Integer productId= testRunPlan.getProductVersionListMaster().getProductMaster().getProductId();
 	 				TestCaseList testCaseList=null;
 	 				if(testcaseNames!=null && testcaseNames.length()>0){
 	 					String [] testcaseName= testcaseNames.split(",");
 	 					for (String tc : testcaseName) {
 	 						testCaseList=testCaseListDAO.getTestCaseByName(tc, productId);
 	 						testCases.add(testCaseList);
 	 					}
 	 				}else{
 	 					testCases.addAll(testCaseListTotal);
 						log.info("Job : " + testRunJob.getTestRunJobId() + " : No Selective Testcases specified. All added");
 	 				}
 	 				if (tsl.getScriptTypeMaster().getScriptType().equals("GHERKIN") && (tsl.getTestSuiteScriptFileLocation() == null || tsl.getTestSuiteScriptFileLocation().trim().isEmpty())) {
 	 					Set<TestCaseList> totalTestCasesList= null;
 	 					if (testCases.size() == 0) {
 	 						totalTestCasesList = tsl.getTestCaseLists();
 	 					} else {
 	 						totalTestCasesList = testCases;
 	 					}
 	 					testExecutionService.prepareBDDTestScriptsForJobExecution(tsl, testRunJob.getTestRunJobId(), totalTestCasesList,testRunPlan);
 						log.info("Job : " + testRunJob.getTestRunJobId() + " Prepared BDD stories pack for execution");
 	 				}
 	 				ArrayList<TestCaseList> testCaseLists=new ArrayList<TestCaseList>();
 	 				testCaseLists.addAll(testCases);
 	 				
 	 			    mapTestRunJobTestSuiteTestCase(testRunJob.getTestRunJobId(),tsl.getTestSuiteId(),testCaseLists, "Add");
 				}		
 			    
				HostHeartbeat hostHeart = hostHeartbeatDAO.getByHostId(host.getHostId());
				hostHeart.setHasResponse(true);
				hostHeart.setResponseToSend((short)ClientReponseMessage.PULSE_ACK_JOBS_AVAILABLE);
				hostHeartbeatDAO.update(hostHeart);
	            log.info("Created Job No : " + testRunJob.getTestRunJobId() + " for Host : " + host.getHostIpAddress() 
	            												   + " and queued to terminal : " + host.getHostId());            
			}
			return testRunJob;
		} catch (Exception e) {
            log.error("Test Run Plan : " + testRunPlan.getTestRunPlanName() + " : Problem in creating Job for Host : " + host.getHostIpAddress());            
			return null;
		}
	}
	
	//Create Composite Job irrespective of Test Engine
	public TestRunJob createCompositeJob(TestRunPlan testRunPlan, RunConfiguration runConfig, HostList host, GenericDevices device, EnvironmentCombination environmentCombination,WorkPackage workPackage,TestSuiteList testSuiteList,RunConfiguration runConfiguration,String testcaseNames){
		try {
			//Get the host status
			String hostStatus = host.getCommonActiveStatusMaster().getStatus();
			log.info("Host Status : " + hostStatus);		
			//Create a new TestRunJob entry for the device
			TestRunJob testRunJob = new TestRunJob();			
			if (!(testSuiteList == null) && (testSuiteList.getScriptTypeMaster() == null) && (testSuiteList.getScriptTypeMaster().getScriptType() == null) && (testSuiteList.getScriptTypeMaster().getScriptType().equalsIgnoreCase("GHERKIN"))) {
				testRunJob.setWorkPackage(workPackage);
				testRunJob.setHostList(host);
				testRunJob.setEnvironmentCombination(environmentCombination);		
				testRunJob.setTestSuite(testSuiteList);
				if(device != null){
					testRunJob.setGenericDevices(device);
				}
				testRunJob.setTestRunStatus(0);
				testRunJob.setTestRunPlan(testRunPlan);
				testRunJob.setRunConfiguration(runConfiguration);
				testRunJob.setTestRunTriggeredTime(DateUtility.getCurrentTime());			
			
				if(hostStatus.equalsIgnoreCase("ACTIVE")) {						
					//queue the run and inform the client/terminal
					//Not updating WP, as current job's status is completed, and could update WP, based on overall Jobs.
					testRunJob.setTestRunStatus(4);				
					addTestRunJob(testRunJob);
					mapTestRunJobTestSuite(testRunJob.getTestRunJobId(),testSuiteList.getTestSuiteId(), "Add");			
					
	 				if(testRunJob!=null){
						if(testRunJob.getTestRunJobId()!=null)
						mongoDBService.addTestRunJobToMongoDB(testRunJob.getTestRunJobId());
					}
	 				
	 				List<TestCaseList> testCaseListTotal=productMasterDAO.getRunConfigTestSuiteTestCaseMapped(runConfig.getRunconfigId(), testSuiteList.getTestSuiteId());
	 				Set<TestCaseList> testCases = new HashSet<TestCaseList>();
	 				Integer productId= testRunPlan.getProductVersionListMaster().getProductMaster().getProductId();
	 				TestCaseList testCaseList=null;
	 				if(testcaseNames!=null && testcaseNames.length()>0){
	 					String [] testcaseName= testcaseNames.split(",");
	 					for (String tc : testcaseName) {
	 						testCaseList=testCaseListDAO.getTestCaseByName(tc, productId);
	 						testCases.add(testCaseList);
	 					}
	 				}else{
	 					testCases.addAll(testCaseListTotal);
						log.info("Job : " + testRunJob.getTestRunJobId() + " : No Selective Testcases specified. All added");
	 				}		
	 				
	 				ArrayList<TestCaseList> testCaseLists = new ArrayList<TestCaseList>();
	 				testCaseLists.addAll(testCases);				
	 			    mapTestRunJobTestSuiteTestCase(testRunJob.getTestRunJobId(),testSuiteList.getTestSuiteId(),testCaseLists, "Add");			    
					HostHeartbeat hostHeart = hostHeartbeatDAO.getByHostId(host.getHostId());
					hostHeart.setHasResponse(true);
					hostHeart.setResponseToSend((short)ClientReponseMessage.PULSE_ACK_JOBS_AVAILABLE);
					hostHeartbeatDAO.update(hostHeart);           
		            log.info("Created Job No : " + testRunJob.getTestRunJobId() + " for Host : " + host.getHostIpAddress() + " and queued to terminal : " + host.getHostId());            
				}
			} else {
				testRunJob = null;
			}
			return testRunJob;
		} catch (Exception e) {
            log.error("Run Configuration : " + runConfig.getRunconfigName() + " : Problem in creating Job for Host : " + host.getHostIpAddress());            
			return null;
		}
	}
	
	@Override
	@Transactional
	public List<TestCaseList> getSelectedTestCasesFromTestRunJobTestSuite(int testRunListId, Integer testSuiteId) {
		// TODO Auto-generated method stub
		List<TestCaseList> testCaseLists = new ArrayList<TestCaseList>();
		List<TestRunJobTestSuiteHasTestCase> testRunJobTestSuiteHasTestCaseList = null;

		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestRunJobTestSuiteHasTestCase.class, "trjtstc");
			c.createAlias("trjtstc.testRunJob", "trj");
			c.createAlias("trjtstc.testCaseList", "tcl");
			c.createAlias("trjtstc.testSuiteList", "tsl");
			c.add(Restrictions.eq("trj.testRunJobId",testRunListId));
			c.add(Restrictions.eq("tsl.testSuiteId",testSuiteId));
			
			testRunJobTestSuiteHasTestCaseList=c.list();			
			for (TestRunJobTestSuiteHasTestCase testRunJobTestSuiteHasTestCase : testRunJobTestSuiteHasTestCaseList) {
				Hibernate.initialize(testRunJobTestSuiteHasTestCase.getTestCaseList());
				testCaseLists.add(testRunJobTestSuiteHasTestCase.getTestCaseList());
			}
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
			// throw re;
		}
		return testCaseLists;	
	}
	
	@Override
	@Transactional
	public ListMultimap<Integer, Object> getFeatureNamesByTestCaseIds(Set<Integer> listOfTestCaseIds) {
		ListMultimap<Integer, Object> productFeatureNamesWithTestCaseList = ArrayListMultimap.create();		
		try{
			if(!listOfTestCaseIds.isEmpty()) {
				List<Object[]> list = sessionFactory.getCurrentSession().createSQLQuery("SELECT pf.productFeatureName , pfTc.testCaseId FROM product_feature pf, product_feature_has_test_case_list pfTc WHERE pf.productFeatureId = pfTc.productFeatureId AND pfTc.testCaseId in (:testCaseIds)").setParameterList("testCaseIds", listOfTestCaseIds).list();
			    if(list != null && list.size() > 0){
			    	for(Object[] obj:list) {
			    		String productFeatureName=(String)obj[0];
			    		Integer testCaseId=(Integer)obj[1];
			    		productFeatureNamesWithTestCaseList.put(testCaseId, productFeatureName);
			    	}
			    }
				log.debug("getFeatureNamesByTestCaseIds successful");
			} else {
				log.debug("getFeatureNamesByTestCaseIds is not possible as there is no valid test case id{s} passed");
			}			
		}catch(Exception e){
			e.printStackTrace();
			log.error("Obtaining Feature NamesBy TestCaseIds failed");
		}
		return productFeatureNamesWithTestCaseList;
	}

	@Override
	@Transactional
	public WorkPackage executeTestRunPlanWorkPackageUnattendedMode(TestRunPlan testRunPlan, Map<String, String> mapData,UserList user, HttpServletRequest req,TestRunPlanGroup testRunPlanGroup, WorkPackage workPackage,String deviceNames) {
		workpackageExxecutionPlanByDevices(workPackage,testRunPlan,req,deviceNames);
		return workPackage;
	}	
	
	@Override
	@Transactional
	public WorkFlowEvent updateWorkFlowEvent(WorkFlowEvent workFlowEvent) {
		try {	
			sessionFactory.getCurrentSession().update(workFlowEvent);
			log.debug("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
		}		
		return workFlowEvent;
	}

	@Override
	@Transactional
	public List<WorkFlowEvent> workFlowEventlist(int typeId) {
		List<WorkFlowEvent> wfeList = new ArrayList<WorkFlowEvent>();
		List<WorkFlowEvent> wfeLists = new ArrayList<WorkFlowEvent>();

		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkFlowEvent.class, "wfe");			
			c.add(Restrictions.eq("wfe.entityTypeRefId",typeId));	
			c.addOrder(Order.asc("wfe.workfloweventId"));
			wfeList = c.list();			
			for (WorkFlowEvent wfe : wfeList) {
				Hibernate.initialize(wfe.getRemarks());
				Hibernate.initialize(wfe.getWorkFlow());
				wfeLists.add(wfe);
			}
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return wfeLists;	
	}

	@Override
	@Transactional
	public String getExecutionTimeForEachWPByTPId(Integer testRunPlanId) {
		log.debug("listing getExecutionTimeForEachWPByTPId instance");
		List<String> avgExecTimeList = null;
		String avgExecTime = null;
		try {
			avgExecTimeList = sessionFactory
					.getCurrentSession()
					.createSQLQuery(
							"select TO_CHAR((AVG(testRunEndTime - testRunStartTime) || 'second')\\:\\:interval, 'HH24:MI:SS') FROM test_run_job WHERE testRunPlanId = "
									+ testRunPlanId).list();
			
			if(avgExecTimeList.size() > 0){
				avgExecTime = avgExecTimeList.get(0);
			}
				
		}catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return avgExecTime;
	}
	
	public Integer getWPTCExecutionSummaryCount(Integer testFactoryId,Integer productId, Integer productBuildId, Integer jtStartIndex, Integer jtPageSize) {		
		List<Object> wptcepResultList = new LinkedList<Object>();
		try {
			String sql="select pm.productName as productName, wp.workpackageId as workPackageId,wp.name as workPackageName ,"
				+ "wp.actualStartDate as actualStartDate, wp.actualEndDate as actualEndDate, wflow.stageName as workFlowStageName,"
				+ "(select etm.name from execution_type_master as etm  where etm.executionTypeId =wp.workPackageType ) as exeType,"
				+ "(SELECT COUNT(wphastc.workPackageId) FROM  workpackage_has_testcase wphastc "
				+ "WHERE wphastc.workPackageId =wp.workpackageId ) AS testCaseCount, "
				+ "(SELECT COUNT(wphasTestSuite.testSuiteId) FROM workpackage_has_testsuite  wphasTestSuite "
				+ "WHERE wphasTestSuite.workpackageId=wp.workpackageId ) AS testSuiteCount, "
				+ "(SELECT COUNT(wphasFeature.featureId) FROM   workpackage_has_feature  wphasFeature "
				+ "WHERE wphasFeature.workpackageId=wp.workpackageId ) AS featueCount, "
				+ "(SELECT COUNT(job.workpackageId) FROM test_run_job job WHERE job.workpackageId =wp.workpackageId  ) AS jobCount, "
				+ "(SELECT COUNT(job.workpackageId) FROM test_run_job job WHERE job.workpackageId =wp.workpackageId and job.testRunStatus=3 ) AS jobsExecuting, "
				+ "(SELECT COUNT(job.workpackageId) FROM test_run_job job WHERE job.workpackageId =wp.workpackageId and job.testRunStatus=4 ) AS jobsQueued, "
				+ "(SELECT COUNT(job.workpackageId) FROM test_run_job job WHERE job.workpackageId =wp.workpackageId and job.testRunStatus=5 ) AS jobsCompleted, "
				+ "(SELECT COUNT(job.workpackageId) FROM test_run_job job WHERE job.workpackageId =wp.workpackageId and job.testRunStatus=7 ) AS jobsAborted, "				
				+ "(SELECT COUNT(buglist.testExecutionResultBugId) FROM workpackage_testcase_execution_plan AS wptcPlan "
				+ "LEFT JOIN test_execution_result_bug_list AS buglist ON wptcPlan.wptcepId=buglist.testCaseExecutionResultId "
				+ "WHERE wptcPlan.workPackageId=wp.workpackageId) AS defectsCount, "
				+ "( SELECT COUNT(tcres.result)   FROM workpackage_testcase_execution_plan as wptcplan "
				+ "inner join  testcase_execution_result  AS tcres "
				+ "on tcres.testCaseExecutionResultId = wptcplan.wptcepId and wptcplan.status=1 "
				+ "where wptcplan.workpackageId= wp.workpackageId  and tcres.result='PASSED' ) AS passedCount,"
				+ " ( SELECT COUNT(tcres.result)   FROM  workpackage_testcase_execution_plan as wptcplan "
				+ "inner join testcase_execution_result  AS tcres "
				+ "on tcres.testCaseExecutionResultId = wptcplan.wptcepId and wptcplan.status=1 "
				+ " WHERE wptcplan.workpackageId= wp.workpackageId AND tcres.result='FAILED' ) AS failedCount,"
				+ " ( SELECT COUNT(tcres.result)   FROM  workpackage_testcase_execution_plan as wptcplan "
				+ "inner join testcase_execution_result  AS tcres "
				+ "on tcres.testCaseExecutionResultId = wptcplan.wptcepId and wptcplan.status=1"
				+ " WHERE wptcplan.workpackageId = wp.workpackageId AND tcres.result='BLOCKED' ) AS blockedCount,"
				+ " ( SELECT COUNT(tcres.result)   FROM  workpackage_testcase_execution_plan as wptcplan "
				+ "inner join testcase_execution_result  AS tcres "
				+ "on tcres.testCaseExecutionResultId = wptcplan.wptcepId and wptcplan.status=1 "
				+ "WHERE wptcplan.workpackageId = wp.workpackageId AND tcres.result='NORUN' ) AS norunCount,"
				+ "  ( SELECT COUNT(tcres.result)   FROM  workpackage_testcase_execution_plan as wptcplan "
				+ "inner join testcase_execution_result  AS tcres "
				+ "on tcres.testCaseExecutionResultId= wptcplan.wptcepId and wptcplan.status = 1"
				+ " WHERE wptcplan.workpackageId = wp.workpackageId AND tcres.result IS NULL ) AS notexecutedCount, "
				+ "(SELECT COUNT(tser.teststepexecutionresultid) FROM teststep_execution_result as tser "
				+ "inner join  testcase_execution_result  AS tcres "
				+ "on tcres.testCaseExecutionResultId = tser.tescaseexecutionresultid "
				+ "inner join workpackage_testcase_execution_plan as wptcplan "
				+ "on wptcplan.wptcepId = tcres.testCaseExecutionResultId "
				+ "where wptcplan.workpackageId= wp.workpackageId )  AS teststepcount "				
				+ "FROM workpackage wp left JOIN workflowevent AS wfevent on wp.workfloweventid = wfevent.workfloweventId "
				+ "left join workflow as wflow on wfevent.workflowId = wflow.workflowId "
				+ "INNER JOIN  product_build pb ON pb.productBuildId=wp.productBuildId ";
				
			if( productId != -1 && productBuildId == -1){//Product Level
				sql = sql + "inner join product_version_list_master  pvlm on pb.productVersionId=pvlm.productVersionListId "
						+ "inner join product_master pm on pvlm.productId=pm.productId "
						+ "where pm.productId="+ productId +"  and pb.status=1 GROUP BY wp.workpackageId,pm.productname,wflow.stagename order by wp.workPackageId desc ";
			}else if(productId == -1 && productBuildId != -1){//Build Level
				sql = sql  + "inner join product_version_list_master  pvlm on pb.productVersionId=pvlm.productVersionListId "
						+ "inner join product_master pm on pvlm.productId=pm.productId "
						+ "WHERE  pb.productBuildId="+productBuildId+"  and pb.status=1 GROUP BY wp.workpackageId,pm.productname,wflow.stagename order by wp.workPackageId desc ";	
			}else if(testFactoryId !=null && productId == -1){//Build Level
				sql = sql + "inner join product_version_list_master  pvlm on pb.productVersionId=pvlm.productVersionListId "
						+ "inner join product_master pm on pvlm.productId=pm.productId "
						+ "INNER JOIN test_factory tf ON (pm.testFactoryId=tf.testFactoryId)"
						+ "where tf.testFactoryId="+testFactoryId+"  and pb.status=1 GROUP BY wp.workpackageId,pm.productname,wflow.stagename order by wp.workPackageId desc ";
				
			}	
			wptcepResultList = sessionFactory.getCurrentSession().createSQLQuery(sql).list();
		} catch (RuntimeException re) {
			log.error("getWPTCExecutionSummary by BuildId failed", re);
			re.printStackTrace();
			return wptcepResultList.size();
		}
		return wptcepResultList.size();		
	}
	
	@Override
	@Transactional
	public Set<String> workFlowEvents(int typeId) {
		List<WorkFlowEvent> wfeList = new ArrayList<WorkFlowEvent>();
		Set<String> wfeLists = new LinkedHashSet<String>();

		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkFlowEvent.class, "wfe");			
			c.add(Restrictions.eq("wfe.entityTypeRefId",typeId));			
			wfeList = c.list();			
			for (WorkFlowEvent wfe : wfeList) {
				Hibernate.initialize(wfe.getRemarks());
				Hibernate.initialize(wfe.getWorkFlow());
				wfeLists.add(wfe.getRemarks());
			}
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return wfeLists;	
	}
	
	public VerificationResult testConfigurationReadinessCheck(RunConfiguration testConfiguration, TestRunPlan testPlan, StringBuffer sb) {
		
		VerificationResult testConfigurationResult = new VerificationResult();
		testConfigurationResult.setIsReady("Yes");
		StringBuffer messageSB = new StringBuffer();
		try {			
			//Verify that the target device / host is active
			if(testConfiguration.getProductType() != null && (testConfiguration.getProductType().getTypeName().equalsIgnoreCase("MOBILE") || testConfiguration.getProductType().getTypeName().equalsIgnoreCase("DEVICE"))) {
				if(testConfiguration.getGenericDevice() != null && testConfiguration.getGenericDevice().getAvailableStatus() != null && testConfiguration.getGenericDevice().getAvailableStatus() == 0){
					testConfigurationResult.setIsReady("No");
					messageSB.append("Device : "+ testConfiguration.getGenericDevice().getName() +" is INACTIVE" + System.lineSeparator());
					testConfigurationResult.addMessage(messageSB.toString());
					return testConfigurationResult;
				} else {
					messageSB.append("Device : "+ testConfiguration.getGenericDevice().getName() +" is ACTIVE" + System.lineSeparator());
				}
			} else if(testConfiguration.getProductType() != null && ( testConfiguration.getProductType().getTypeName().equalsIgnoreCase("WEB") || testConfiguration.getProductType().getTypeName().equalsIgnoreCase("DEKSTOP") || testConfiguration.getProductType().getTypeName().equalsIgnoreCase("EMBEDDED"))) {
				if(testConfiguration.getHostList() != null && testConfiguration.getHostList().getCommonActiveStatusMaster() != null 
						&& testConfiguration.getHostList().getCommonActiveStatusMaster().getStatus().equalsIgnoreCase("INACTIVE")){
					testConfigurationResult.setIsReady("No");
					messageSB.append("Target Host: "+testConfiguration.getHostList().getHostName()+" is INACTIVE" + System.lineSeparator());
					//testConfigurationResult.addMessage(messageSB.toString()); 
					return testConfigurationResult;
				} else if(testConfiguration.getHostList() == null || testConfiguration.getHostList().getCommonActiveStatusMaster() == null ||
						testConfiguration.getHostList().getCommonActiveStatusMaster().getStatus() == null){
					testConfigurationResult.setIsReady("No");
					messageSB.append("Target Host: "+testConfiguration.getHostList().getHostName()+" is INACTIVE" + System.lineSeparator());					
				} else if(testConfiguration.getHostList() != null && testConfiguration.getHostList().getCommonActiveStatusMaster() != null 
						&& testConfiguration.getHostList().getCommonActiveStatusMaster().getStatus().equalsIgnoreCase("ACTIVE")){
					messageSB.append("Target Host: "+testConfiguration.getHostList().getHostName()+" is ACTIVE" + System.lineSeparator());
				}
			}
			testConfigurationResult.addMessage(messageSB.toString());
		} catch(Exception e) {
			log.error("Error in checking test configuration readiness check ", e);
		}
		return testConfigurationResult;
	}

	@Override
	@Transactional
	public List<String> getAllJobIdsByWorkpackageId(Integer testRunNo) {
		return null;
		
		/*try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackage.class, "wfe");			
			c.add(Restrictions.eq("wfe.entityTypeRefId",typeId));			
			wfeList = c.list();			
			for (WorkFlowEvent wfe : wfeList) {
				Hibernate.initialize(wfe.getRemarks());
				Hibernate.initialize(wfe.getWorkFlow());
				wfeLists.add(wfe.getRemarks());
			}
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}*/
	}
	
	@Override
	@Transactional
	public Integer getProductBuildsTestedCount(Integer productBuildId) {
		log.info("Product Build test count retrieval");
		List<WorkPackage> wfeList = new ArrayList<WorkPackage>();
		Integer count = 0;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackage.class, "wfe");
			c.createAlias("wfe.productBuild", "pb");
			c.add(Restrictions.eq("pb.productBuildId",productBuildId));			
			wfeList = c.list();
			
			if(wfeList != null && !wfeList.isEmpty())
				count = wfeList.size();
			log.debug("list all successful");
		} catch(Exception e) {
			log.error("list all failed", e);
			return 0;
		}
		return count;
	}

	@Override
	@Transactional
	public Integer getExecutionTCCountForJob(Integer testRunJobId) {
		Integer count = 0;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestRunJobTestSuiteHasTestCase.class , "trjtstc");
			c.createAlias("trjtstc.testRunJob", "testJob");
			c.add(Restrictions.eq("testJob.testRunJobId",testRunJobId));			
			List tcCount = c.list();
			
			if(tcCount != null && !tcCount.isEmpty())
				count = tcCount.size();
			log.debug("list all successful");
		} catch(Exception e){
			log.error("list all failed", e);
			return 0;
		}
		return count;
	}

	@Override
	public List<WorkPackageTestCaseExecutionPlan> listWorkPackageTestCaseExecutionPlanByJobAndSuiteId(
			Integer testRunJobId, Integer testSuiteId) {
		List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlansList = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackageTestCaseExecutionPlan.class , "wptcep");
			c.createAlias("wptcep.actualTestRunJob", "actualjob");
			c.add(Restrictions.eq("actualjob.testRunJobId",testRunJobId));		
			c.createAlias("wptcep.testSuiteList", "testsuitelist");
			c.add(Restrictions.eq("testsuitelist.testSuiteId",testSuiteId));	
			workPackageTestCaseExecutionPlansList = c.list();

			if(workPackageTestCaseExecutionPlansList != null && workPackageTestCaseExecutionPlansList.size() > 0){
				return workPackageTestCaseExecutionPlansList;
			}
		} catch(Exception e){
			log.error("list all failed", e);
			return null;
		}
		return workPackageTestCaseExecutionPlansList;
	}

	@Override
	public List<WorkPackageTestCaseExecutionPlan> listWorkPackageTestCaseExecutionPlanByWorkPackageId(
			Integer testRunJobId) {
		List<WorkPackageTestCaseExecutionPlan>workPackageTestCaseExecutionPlansList = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackageTestCaseExecutionPlan.class , "wptcep");
			c.createAlias("wptcep.actualTestRunJob", "actualjob");
			c.add(Restrictions.eq("actualjob.testRunJobId",testRunJobId));		
			workPackageTestCaseExecutionPlansList = c.list();

			if(workPackageTestCaseExecutionPlansList != null && workPackageTestCaseExecutionPlansList.size() > 0){
				return workPackageTestCaseExecutionPlansList;
			}
		} catch(Exception e){
			log.error("list all failed", e);
			return null;
		}
		return workPackageTestCaseExecutionPlansList;
		
	}

	//For selective testcases
	@Override
	@Transactional
	public void workpackageSelectiveExecutionPlan(WorkPackage workPackage,TestRunPlan testRunPlan, Map<RunConfiguration, String> testConfigs, HttpServletRequest req, String deviceNames) {
		
		Integer workPackageId = workPackage.getWorkPackageId();
		String jobIds = "";
		//workpackage TestSuite plan starts
		WorkFlowEvent workFlowEvent = null;
 		int workPackagesTestSuiteCount = 0;
		Set<TestSuiteList> testSuiteLists = new HashSet<TestSuiteList>();
		List<WorkPackageTestSuite> workPackageTestSuites = new ArrayList<WorkPackageTestSuite>();
		
		for(Map.Entry<RunConfiguration, String> rcc : testConfigs.entrySet()){
			testSuiteLists.addAll(rcc.getKey().getTestSuiteLists());
		}
				
		WorkPackageTestSuite workPackageTestSuite = null;
		for (TestSuiteList testSuite : testSuiteLists) {
			workPackageTestSuite = new WorkPackageTestSuite();
			workPackageTestSuite.setTestSuite(testSuite);
			workPackageTestSuite.setWorkPackage(workPackage);
			workPackageTestSuite.setCreatedDate(new Date(System.currentTimeMillis()));
			workPackageTestSuite.setEditedDate(new Date(System.currentTimeMillis()));
			workPackageTestSuite.setIsSelected(1);
			workPackageTestSuite.setStatus("ACTIVE");
			if(workPackageTestSuite.getWorkPackage().getWorkFlowEvent().getWorkFlow().getStageId() != IDPAConstants.WORKFLOW_STAGE_ID_PLANNING && workPackageTestSuite.getWorkPackage().getWorkFlowEvent().getWorkFlow().getStageValue() < 20){
				WorkFlow workFlow = getWorkFlowByEntityIdStageId(IDPAConstants.WORKPACKAGE_ENTITY_ID, IDPAConstants.WORKFLOW_STAGE_ID_PLANNING);
				workFlowEvent = new WorkFlowEvent();
				workFlowEvent.setEventDate(DateUtility.getCurrentTime());
				workFlowEvent.setRemarks("Planning Workpackage TestSuite:"+workPackage.getName());
				UserList userAdmin = userListDAO.getUserByLoginId(IDPAConstants.ADMIN_LOGIN_ID);
				workFlowEvent.setUser(userAdmin);
				workFlowEvent.setWorkFlow(workFlow);
				workPackage.setWorkFlowEvent(workFlowEvent);
				addWorkFlowEvent(workFlowEvent);
				updateWorkPackage(workPackage);
			}
			workPackageTestSuites.add(workPackageTestSuite);
			mapWorkpackageWithTestSuite(workPackageTestSuite.getWorkPackage().getWorkPackageId(),testSuite.getTestSuiteId(),"Add");
		}
			
			
		//log.info("New testcases to be added for the workpackage : "+ workPackage.getWorkPackageId() + " : count : " + workPackageTestSuites.size());
		workPackagesTestSuiteCount = addNewWorkPackageTestSuite(workPackageTestSuites);
		Set<WorkPackageTestSuite> workPackageTS=new HashSet<WorkPackageTestSuite>();
		workPackageTS.addAll(workPackageTestSuites);
		workPackage.setWorkPackageTestSuites(workPackageTS);
		updateWorkPackage(workPackage);
		//workpackage TestSuite plan end
		
		//workpackage feature plan starts
		int workPackagesFeatureCount =0;
		Set<ProductFeature> featureList= testRunPlan.getFeatureList();
		List<WorkPackageFeature> workPackageFeatures = new ArrayList<WorkPackageFeature>();
		WorkPackageFeature workPackageFeature = null;
		for (ProductFeature feature : featureList) {
			workPackageFeature = new WorkPackageFeature();
			workPackageFeature.setFeature(feature);
			workPackageFeature.setWorkPackage(workPackage);
			workPackageFeature.setCreatedDate(new Date(System.currentTimeMillis()));
			workPackageFeature.setEditedDate(new Date(System.currentTimeMillis()));
			workPackageFeature.setIsSelected(1);
			workPackageFeature.setStatus("ACTIVE");
			if(workPackageFeature.getWorkPackage().getWorkFlowEvent().getWorkFlow().getStageId()!=IDPAConstants.WORKFLOW_STAGE_ID_PLANNING&& workPackageFeature.getWorkPackage().getWorkFlowEvent().getWorkFlow().getStageValue()<20){
				WorkFlow workFlow=getWorkFlowByEntityIdStageId(IDPAConstants.WORKPACKAGE_ENTITY_ID, IDPAConstants.WORKFLOW_STAGE_ID_PLANNING);
				workFlowEvent = new WorkFlowEvent();
				workFlowEvent.setEventDate(DateUtility.getCurrentTime());
				workFlowEvent.setRemarks("Planning Workapckage Feature :"+workPackage.getName());
				UserList userAdmin= userListDAO.getUserByLoginId(IDPAConstants.ADMIN_LOGIN_ID);
				workFlowEvent.setUser(userAdmin);
				workFlowEvent.setWorkFlow(workFlow);
				workPackage.setWorkFlowEvent(workFlowEvent);
				addWorkFlowEvent(workFlowEvent);
				updateWorkPackage(workPackage);
			}
			workPackageFeatures.add(workPackageFeature);
			mapWorkpackageWithFeature(workPackageFeature.getWorkPackage().getWorkPackageId(),feature.getProductFeatureId(),"Add");
		}
		
		
		log.info("New testcases to be added for the workpackage : "+ workPackage.getWorkPackageId() + " : count : " + workPackageTestSuites.size());
		workPackagesFeatureCount = addNewWorkPackageFeature(workPackageFeatures);
		Set<WorkPackageFeature> workPackageFeatureSet=new HashSet<WorkPackageFeature>();
		workPackageFeatureSet.addAll(workPackageFeatures);
		workPackage.setWorkPackageFeature(workPackageFeatureSet);
		updateWorkPackage(workPackage);
		//workpackage feature plan ends
		
		//workpackage testcase plan starts
		int workPackagesTestCaseCount =0;
		List<TestCaseList> testCaseList = new ArrayList<TestCaseList>();
		
		for(Map.Entry<RunConfiguration, String> rc: testConfigs.entrySet()) {
			if(rc.getValue() != null && !rc.getValue().isEmpty()) {
				for(String tcName : rc.getValue().split(",")){
					testCaseList.add(testCaseListDAO.getTestCaseByName(tcName, testRunPlan.getProductVersionListMaster().getProductMaster().getProductId()));
				}
			} else {
				for(TestSuiteList tsl : testSuiteLists){
					testCaseList.addAll(productMasterDAO.getRunConfigTestSuiteTestCaseMapped(rc.getKey().getRunconfigId(), tsl.getTestSuiteId()));
				}
			}
		}
		
		List<WorkPackageTestCase> workPackageTestCases = new ArrayList<WorkPackageTestCase>();
		WorkPackageTestCase workPackageTestCase = null;
		for (TestCaseList testCase : testCaseList) {
			workPackageTestCase = new WorkPackageTestCase();
			workPackageTestCase.setTestCase(testCase);
			workPackageTestCase.setWorkPackage(workPackage);
			workPackageTestCase.setCreatedDate(new Date(System.currentTimeMillis()));
			workPackageTestCase.setEditedDate(new Date(System.currentTimeMillis()));
			workPackageTestCase.setIsSelected(1);
			workPackageTestCase.setStatus("ACTIVE");
			if(workPackageTestCase.getWorkPackage().getWorkFlowEvent().getWorkFlow().getStageId()!=IDPAConstants.WORKFLOW_STAGE_ID_PLANNING&& workPackageTestCase.getWorkPackage().getWorkFlowEvent().getWorkFlow().getStageValue()<20){
				WorkFlow workFlow=getWorkFlowByEntityIdStageId(IDPAConstants.WORKPACKAGE_ENTITY_ID, IDPAConstants.WORKFLOW_STAGE_ID_PLANNING);
				workFlowEvent = new WorkFlowEvent();
				workFlowEvent.setEventDate(DateUtility.getCurrentTime());
				workFlowEvent.setRemarks("Planning Workpackage testcase :"+workPackage.getName());
				UserList userAdmin= userListDAO.getUserByLoginId(IDPAConstants.ADMIN_LOGIN_ID);
				workFlowEvent.setUser(userAdmin);
				workFlowEvent.setWorkFlow(workFlow);
				workPackage.setWorkFlowEvent(workFlowEvent);
				addWorkFlowEvent(workFlowEvent);
				updateWorkPackage(workPackage);
			}
			workPackageTestCases.add(workPackageTestCase);
			//mapWorkpackageWithTestCase(workPackageTestCase.getWorkPackage().getWorkPackageId(),testCase.getTestCaseId(),"Add");
			mapWorkpackageWithTestCase(workPackage,testCase,"Add");
		}
		
		
		log.info("New testcases to be added for the workpackage : "+ workPackage.getWorkPackageId() + " : count : " + workPackageTestSuites.size());
		workPackagesTestCaseCount = addNewWorkPackageTestCases(workPackageTestCases);
		Set<WorkPackageTestCase> workPackageTestCaseSet=new HashSet<WorkPackageTestCase>();
		workPackageTestCaseSet.addAll(workPackageTestCases);
		workPackage.setWorkPackageTestCases(workPackageTestCaseSet);
		updateWorkPackage(workPackage);
		//workpackage testcase plan ends
		
		WorkpackageRunConfiguration wpRunConfiguration =null;
		
		Set<RunConfiguration> runConfigurations = new HashSet<RunConfiguration>();
		Set<RunConfiguration> runConfigurationsTotal = testConfigs.keySet();
		
		Integer type= testRunPlan.getProductVersionListMaster().getProductMaster().getProductType().getProductTypeId();
		
		ArrayList dl= new ArrayList();
		if(deviceNames!=null && deviceNames.length()>0){
			String [] deviceName= deviceNames.split(",");
			for (String device : deviceName) {
				dl.add(device);
			}
			for (RunConfiguration runConfiguration : runConfigurationsTotal) {
				
				if(runConfiguration.getProductType() != null && runConfiguration.getProductType().getProductTypeId() != null)
					type = runConfiguration.getProductType().getProductTypeId();
				
				String runConfigType = "N/A";
				
				if(type==1 || type==5){
					runConfigType = "Device / Mobile";
					for(int j=0;j< dl.size();j++){							
						if(dl.get(j).toString().equals(runConfiguration.getRunconfigId().toString()) || (runConfiguration.getHostList() != null && runConfiguration.getHostList().getHostName() != null && dl.get(j).toString().equals(runConfiguration.getHostList().getHostName()))){
							runConfigurations.add(runConfiguration);
						}
					}
				}else if(type==2 || type==3 || type == 4){
					runConfigType = "Web / Embedded / Desktop";
					for(int j=0;j< dl.size();j++){							
						if(dl.get(j).toString().equals(runConfiguration.getRunconfigId().toString()) || dl.get(j).toString().equals(runConfiguration.getHostList().getHostName())){
							runConfigurations.add(runConfiguration);
						} 
					}						
				} else if(type == 6){
					runConfigType = "Composite";
					//If type is composite then copy both device and host.
					for(int j=0;j< dl.size();j++){
						if(dl.get(j).toString().equals(runConfiguration.getRunconfigId().toString()) || dl.get(j).toString().contains(runConfiguration.getHostList().getHostName())
								|| dl.get(j).toString().contains(runConfiguration.getGenericDevice().getName())){
							runConfigurations.add(runConfiguration);
						}
					}
				}
				log.info (" Test Configuration Type ID : "+type + " ; Name : " +runConfigType);
			}
		}else{
			runConfigurations.addAll(runConfigurationsTotal);
		}
		
		for (RunConfiguration runConfiguration : runConfigurations) {
			addRunConfigurationWorkpackage(workPackage.getWorkPackageId(),runConfiguration.getRunconfigId(),"testsuite");
			addRunConfigurationWorkpackage(workPackage.getWorkPackageId(),runConfiguration.getRunconfigId(),"feature");
			addRunConfigurationWorkpackage(workPackage.getWorkPackageId(),runConfiguration.getRunconfigId(),"testcase");
		}
		//Start creating Job for the runconfigurations
		executeSelectiveTestConfigTestCases(testRunPlan,workPackage,testConfigs);			
	}
		
	private void mapWorkpackageWithTestCase(WorkPackage workPackage,TestCaseList testCase, String action) {
		log.info("mapWorkpackageWithTestCase : enter");
		try{
			workPackage.getTestcaseList().add(testCase);
			testCase.getWorkPackageList().add(workPackage);
			sessionFactory.getCurrentSession().saveOrUpdate(workPackage);
			sessionFactory.getCurrentSession().saveOrUpdate(testCase);
		}catch(Exception e){
			log.info("mapWorkpackageWithTestCase : error"+e);
		}
	}

	//This is for automated test Workpackage execution
	public TestRunPlanExecutionStatusVO executeSelectiveTestConfigTestCases(TestRunPlan testRunPlan,WorkPackage workPackage,Map<RunConfiguration, String> runConfigurationTestCases) {		
		log.debug("Execute SelectiveTestConfigTestCases Started");
		TestRunJob testRunJob=null;
		String jobIds ="";
		TestRunPlanExecutionStatusVO statusVO = new TestRunPlanExecutionStatusVO();		
		List<GenericDevices> targetDevices = new ArrayList<GenericDevices> ();
		List<EnvironmentCombination> environmentCombinations = new ArrayList<EnvironmentCombination> ();

		List<GenericDevices> testRunPlanDevices = new ArrayList<GenericDevices> ();
		List<HostList> testRunPlanHosts = new ArrayList<HostList> ();
		List<HostList> targetHosts = new ArrayList<HostList> ();
		Set<TestSuiteList> testRunPlanTestSuites = new HashSet<TestSuiteList> ();
		Set<TestSuiteList> runConfigTestSuites = new HashSet<TestSuiteList>();	
		Set<TestSuiteList> executionTestSuites = new HashSet<TestSuiteList>();
		String testToolName = "";
		String testcaseNames = null;
		try {
			//Get productType
			Integer productType= testRunPlan.getProductVersionListMaster().getProductMaster().getProductType().getProductTypeId();	
			testRunPlanTestSuites=testRunPlan.getTestSuiteLists();			
			GenericDevices genericDevice=null;
			HostList hostList=null;
			EnvironmentCombination environmentCombination=null;
			for(Map.Entry<RunConfiguration, String> rc : runConfigurationTestCases.entrySet()) {
				RunConfiguration runConfiguration = rc.getKey();
				testcaseNames = rc.getValue();				
				//Test Configuration check whether it is active then only create job.
				VerificationResult isActive = testConfigurationReadinessCheck(runConfiguration, testRunPlan, null);
				log.info("Is the Test Configuration Active : " +isActive.getIsReady());
				if(isActive.getIsReady().equalsIgnoreCase("NO")){
					log.info("Current Test Configuration is not Active , so skipping Test Job Creation.");
					continue;
				}
				productType = runConfiguration.getProductType().getProductTypeId();
				runConfigTestSuites = runConfiguration.getTestSuiteLists();
				
				if(runConfiguration.getGenericDevice()!=null){
					genericDevice = runConfiguration.getGenericDevice();
				}else{
					log.info("There are no standalone devices specified for the test run plan");
				}
														
				if(runConfiguration.getHostList()!=null) {
					hostList=runConfiguration.getHostList();
				} else {
					log.info("There are no standalone server specified for the test run plan");
				}
				
				if(runConfiguration.getEnvironmentcombination()!=null) {
					environmentCombination=runConfiguration.getEnvironmentcombination();
				} else {
					log.info("There are no environment combination specified for the test run plan");
				}
				
				if (targetHosts.contains(hostList)) {
					log.info("The Server was already processed in this test run plan : " + hostList.getHostIpAddress());						
				} else  {								
					targetHosts.add(hostList);								
				}
				
				if(productType != null && (productType == IDPAConstants.PRODUCT_TYPE_DEVICE || productType == IDPAConstants.PRODUCT_TYPE_MOBILE)){
					if(genericDevice != null){								
						targetDevices.add(genericDevice);
					}
				} else{
					genericDevice = null;
				}
								
				executionTestSuites = runConfigTestSuites;
			
				if(executionTestSuites == null || executionTestSuites.isEmpty()){
					log.info("There is no Test Suite mapped to the test plan : "+testRunPlan.getTestRunPlanId() +" / test configuration : "+runConfiguration.getRunconfigId());
					statusVO.hasExecutedSuccessfully = false;
					statusVO.executionMessage = "There is no Test Suite mapped to the test plan : "+testRunPlan.getTestRunPlanId() +" / test configuration : "+runConfiguration.getRunconfigId();
					statusVO.testRunJob = null;
					return statusVO;
				}
				
				if(productType != null && (productType == IDPAConstants.PRODUCT_TYPE_DEVICE || productType == IDPAConstants.PRODUCT_TYPE_MOBILE)){
					if(genericDevice !=null && environmentCombination!=null){
						log.info("Standalone devices specified for the test run configuration " );					
						
						TestSuiteList testSuiteList = executionTestSuites.stream().findFirst().get();
						testRunJob = createJobForDevice(testRunPlan, genericDevice,environmentCombination, workPackage,testSuiteList,runConfiguration,testcaseNames);
						
						if(testRunPlan.getTestScriptsLevel().equalsIgnoreCase(IDPAConstants.MULTIPLE_RUNCONFIG_LEVEL_SCRIPT_PACK))
							testRunJob = addTestConfigurationTestSuiteScriptsPackToJob(testRunJob, testRunPlan, runConfiguration, testSuiteList);
						else
							testRunJob = addTestSuiteScriptsPackToJob(testRunJob, testRunPlan, testSuiteList);												
					}
					
					if(targetDevices == null || targetDevices.isEmpty()){	
						log.info("No devices specified for the Test Plan / Test Configuration");
						statusVO.hasExecutedSuccessfully = false;
						statusVO.executionMessage = "Device(s) have not been specified or active";
						return statusVO;
					} else {
						log.info("Total No of devices specified for the Test Run : " + targetDevices.size());
					}
				} else if(productType != null && (productType == IDPAConstants.PRODUCT_TYPE_WEB || productType == IDPAConstants.PRODUCT_TYPE_DESKTOP || productType == IDPAConstants.PRODUCT_TYPE_EMBEDDED)){
					if(hostList!=null && environmentCombination!=null){
						log.info("Host specified for the test configuration" );						
						// Starting implementation for TRP Level Multi TS Execution 
						if(testRunPlan.getMultipleTestSuites().equalsIgnoreCase(IDPAConstants.MULTIPLE_TESTSUITES_FOR_EXECUTION)){							
							TestToolMaster toolMaster = testRunPlan.getTestToolMaster();
							testToolName = testRunPlan.getTestToolMaster().getTestToolName();
								testRunJob = createJobForMultiTS(testRunPlan, hostList,genericDevice,environmentCombination, workPackage,executionTestSuites,runConfiguration,testcaseNames);								
							testRunJob = addTestScriptsPackToJob(testRunJob, testRunPlan);													
						} else {
							TestSuiteList testSuiteList = executionTestSuites.stream().findFirst().get();
							testRunJob = createJobForHost(testRunPlan, hostList,environmentCombination, workPackage,testSuiteList,runConfiguration,testcaseNames);
							
							if(testRunPlan.getTestScriptsLevel().equalsIgnoreCase(IDPAConstants.MULTIPLE_RUNCONFIG_LEVEL_SCRIPT_PACK))
								testRunJob = addTestConfigurationTestSuiteScriptsPackToJob(testRunJob, testRunPlan, runConfiguration, testSuiteList);
							else
								testRunJob = addTestSuiteScriptsPackToJob(testRunJob, testRunPlan, testSuiteList);													
						}						
					}
					
					if(targetHosts == null || targetHosts.isEmpty()){	
						log.info("No host specified for the Test Plan / Test Configuration");
						statusVO.hasExecutedSuccessfully = false;
						statusVO.executionMessage = "Host has not been specified or active";
						return statusVO;
					} else {
						log.info("Total No of Host specified for the Test Run : " + targetHosts.size());
					}	
				} else if(productType != null && productType == IDPAConstants.PRODUCT_TYPE_COMPOSITE){
					testRunJob = createJobForMultiTS(testRunPlan, hostList,genericDevice,environmentCombination, workPackage,executionTestSuites,runConfiguration,testcaseNames);
					if(testRunJob != null){	
						testRunJob = addTestScriptsPackToJob(testRunJob, testRunPlan);	
					}	
				}
				
				if(testRunJob != null && testRunJob.getTestRunJobId() != null){
					jobIds = jobIds +testRunJob.getTestRunJobId() +",";
				}
				
				//Set the Combined Results Job to the Workpackage
				if(testRunPlan.getCombinedResultsRunConfiguration() != null && testRunPlan.getCombinedResultsRunConfiguration().getRunconfigId() != null) {
					if (runConfiguration.getRunconfigId() == testRunPlan.getCombinedResultsRunConfiguration().getRunconfigId()) {
						workPackage.setCombinedResultsReportingJob(testRunJob);
					}
				}		
			}			
			
			//Add TestRunPlan job mode to workpackage
			if(testRunPlan.getResultsReportingMode() != null)
				workPackage.setResultsReportingMode(testRunPlan.getResultsReportingMode());
			log.info("Updating Workpackage after set combined results reporting job ");
			updateWorkPackage(workPackage);
			log.info("Workpackage updated");
			
			if(jobIds != null && !jobIds.isEmpty()){
				log.info("Execution Job Counts ==> "+jobIds.substring(0, jobIds.lastIndexOf(",")).split(",").length);
				log.info("Execution Job Numbers ==> "+jobIds.substring(0, jobIds.lastIndexOf(",")));
			} else {
				log.info("Execution Job Counts ==> 0");	
			}
			
			int jobsCount  = 0;
			if(jobIds != null && !jobIds.isEmpty()){
				String[] arr = jobIds.split(",");
				jobsCount = arr.length;
			}
			
			ScriptLessExecutionDTO scriptLesExeDTO= new ScriptLessExecutionDTO(workPackage.getWorkPackageId(),workPackage.getName(),jobIds, jobsCount);
			
			statusVO.hasExecutedSuccessfully = true;
			statusVO.executionMessage = "Jobs created for active host";
			statusVO.testRunJob = testRunJob;				
		} catch (Exception e) {        	
            log.error("Problem executing Test Run ", e);            
			statusVO.hasExecutedSuccessfully = false;
			statusVO.executionMessage = "Problems in executing. Some jobs may not have been created";
			statusVO.testRunJob = testRunJob;
        }
		return statusVO;
	}
	
	@Override
	@Transactional
	public List<WorkPackage> listAllWorkPackages(int limit, int jtPageSize) {
		log.debug("listing all WorkPackage instance");
		List<WorkPackage> workPackageList = null;
		try {
			workPackageList = sessionFactory.getCurrentSession().createSQLQuery("from WorkPackage where isActive=1")
					.setFirstResult(limit).setMaxResults(jtPageSize).list();			
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		for (Object obj : workPackageList) {
			WorkPackage workPackage = (WorkPackage)obj;
			if (workPackage != null) {
				if(workPackage.getRunConfigurationList()!=null){
					Hibernate.initialize(workPackage.getRunConfigurationList());
					Set<RunConfiguration> runConfigSet=workPackage.getRunConfigurationList();
					for(RunConfiguration runConfig:runConfigSet){
						Hibernate.initialize(runConfig);
					}
				}
				if(workPackage.getProductBuild()!=null){
					Hibernate.initialize(workPackage.getProductBuild());
					if(workPackage.getProductBuild().getProductVersion()!=null){
						Hibernate.initialize(workPackage.getProductBuild().getProductVersion());
						if(workPackage.getProductBuild().getProductVersion().getProductMaster()!=null){
							Hibernate.initialize(workPackage.getProductBuild().getProductVersion().getProductMaster());
							if(workPackage.getProductBuild().getProductVersion().getProductMaster().getProductType()!=null){
								Hibernate.initialize(workPackage.getProductBuild().getProductVersion().getProductMaster().getProductType());
							}
						}
					}
					
				}
				if(workPackage.getTestRunJobSet()!=null){
					Hibernate.initialize(workPackage.getTestRunJobSet());
					for(TestRunJob testrunJob:workPackage.getTestRunJobSet()){
						if(testrunJob!=null){
							Hibernate.initialize(testrunJob);
							if(testrunJob.getRunConfiguration()!=null){
								Hibernate.initialize(testrunJob.getRunConfiguration());
								
								
							}
						}
					}
				}
				if(workPackage.getTestSuiteList().size()!=0){
					Hibernate.initialize(workPackage.getTestSuiteList());
					Set<TestSuiteList> tsSet=workPackage.getTestSuiteList();
					for(TestSuiteList ts:tsSet){
						Set<WorkPackageTestCaseExecutionPlan> wptcPlanSet=ts.getWptcePlanSet();
						if(wptcPlanSet.size()!=0){
							Hibernate.initialize(wptcPlanSet);
							for(WorkPackageTestCaseExecutionPlan wptcPlan:wptcPlanSet){
								Hibernate.initialize(wptcPlan.getTestCase());
								Hibernate.initialize(wptcPlan.getTestCaseExecutionResult());
								Hibernate.initialize(wptcPlan.getRunConfiguration());
								Hibernate.initialize(wptcPlan.getRunConfiguration().getRunconfiguration());
							}
						}
						
					
						
					}
				}
				if(workPackage.getTestcaseList().size()!=0){
					Hibernate.initialize(workPackage.getTestcaseList());
					Set<TestCaseList> tclist=  workPackage.getTestcaseList();
					for(TestCaseList tc:tclist){
						Hibernate.initialize(tc);
						Set<WorkPackageTestCaseExecutionPlan> wptcPlanSet=tc.getWorkPackageTestCaseExecutionPlan();
						if(wptcPlanSet.size()!=0){
							Hibernate.initialize(wptcPlanSet);
							for(WorkPackageTestCaseExecutionPlan wptcPlan:wptcPlanSet){
								Hibernate.initialize(wptcPlan.getTestCase());
								Hibernate.initialize(wptcPlan.getTestCaseExecutionResult());
								Hibernate.initialize(wptcPlan.getRunConfiguration());
								Hibernate.initialize(wptcPlan.getRunConfiguration().getRunconfiguration());
							}
						}
						
					
						
					}
				}
				
				if(workPackage.getWorkPackageTestCaseExecutionPlan().size()!=0){
					Hibernate.initialize(workPackage.getWorkPackageTestCaseExecutionPlan());
					Set<WorkPackageTestCaseExecutionPlan> wptcplanSet=workPackage.getWorkPackageTestCaseExecutionPlan();
					for(WorkPackageTestCaseExecutionPlan wptcplan:wptcplanSet){
						Hibernate.initialize(wptcplan);
						Hibernate.initialize(wptcplan.getTestCaseExecutionResult());
						if(wptcplan.getWorkPackage() != null){
							Hibernate.initialize(wptcplan.getWorkPackage());
							if(wptcplan.getWorkPackage().getProductBuild() != null){
								Hibernate.initialize(wptcplan.getWorkPackage().getProductBuild());
								if(wptcplan.getWorkPackage().getProductBuild().getProductVersion() != null){
									Hibernate.initialize(wptcplan.getWorkPackage().getProductBuild().getProductVersion());
								}
							}
						}
					}
				}
				
				if(workPackage.getWorkPackageRunConfigSet().size()!=0){
					Hibernate.initialize(workPackage.getWorkPackageRunConfigSet());
					for(WorkpackageRunConfiguration wprunconfig:workPackage.getWorkPackageRunConfigSet()){
						
						if(wprunconfig.getRunconfiguration()!=null){
							if(wprunconfig.getRunconfiguration().getEnvironmentcombination()!=null){
								Hibernate.initialize(wprunconfig.getRunconfiguration().getEnvironmentcombination());
							}
							Hibernate.initialize(wprunconfig.getRunconfiguration());
							if(wprunconfig.getRunconfiguration().getGenericDevice()!=null){
								Hibernate.initialize(wprunconfig.getRunconfiguration().getGenericDevice());
							}if(wprunconfig.getRunconfiguration().getHostList()!=null){
								Hibernate.initialize(wprunconfig.getRunconfiguration().getHostList());
							}
						}
						if(wprunconfig.getWorkPackageTestCaseExecutionPlan().size()!=0){
							Hibernate.initialize(wprunconfig.getWorkPackageTestCaseExecutionPlan());
							for(WorkPackageTestCaseExecutionPlan wptcexePlan:wprunconfig.getWorkPackageTestCaseExecutionPlan()){
								Hibernate.initialize(wptcexePlan);
								Hibernate.initialize(wptcexePlan.getTestCaseExecutionResult());
								if(wptcexePlan.getWorkPackage() != null){
									Hibernate.initialize(wptcexePlan.getWorkPackage());
									if(wptcexePlan.getWorkPackage().getProductBuild() != null){
										Hibernate.initialize(wptcexePlan.getWorkPackage().getProductBuild());
										if(wptcexePlan.getWorkPackage().getProductBuild().getProductVersion() != null){
											Hibernate.initialize(wptcexePlan.getWorkPackage().getProductBuild().getProductVersion());
										}
									}
								}
								if(wptcexePlan.getTestSuiteList()!=null){
									Hibernate.initialize(wptcexePlan.getTestSuiteList());
									if(wptcexePlan.getTestSuiteList().getTestCaseLists().size()!=0){
										Hibernate.initialize(wptcexePlan.getTestSuiteList().getTestCaseLists());
									}
								}
							}
						}
					}
				}
			}			
		}
		return workPackageList;
	}
	
	
	@Override
	@Transactional
	public List<WorkPackage> listAllWorkPackagesBasedOnDateFilters(String fromDate, String toDate) {
		log.debug("listing all WorkPackage instance based on fromDate and toDate");
		List<WorkPackage> workPackageList = null;
		String sql = null;		
		try {
			if(fromDate != null && toDate != null && !(fromDate.isEmpty() && toDate.isEmpty()))
				sql = "select * from workpackage where date(createdate) between to_date('"+fromDate+"', 'YYYY/MM/DD') and to_date('"+toDate+"', 'YYYY/MM/DD')";
			workPackageList = sessionFactory.getCurrentSession().createSQLQuery(sql).addEntity(WorkPackage.class).list();			
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		for (WorkPackage workPackage : workPackageList) {
			if (workPackage != null) {
				if(workPackage.getRunConfigurationList()!=null){
					Hibernate.initialize(workPackage.getRunConfigurationList());
					Set<RunConfiguration> runConfigSet=workPackage.getRunConfigurationList();
					for(RunConfiguration runConfig:runConfigSet){
						Hibernate.initialize(runConfig);
					}
				}
				if(workPackage.getProductBuild()!=null){
					Hibernate.initialize(workPackage.getProductBuild());
					if(workPackage.getProductBuild().getProductVersion()!=null){
						Hibernate.initialize(workPackage.getProductBuild().getProductVersion());
						if(workPackage.getProductBuild().getProductVersion().getProductMaster()!=null){
							Hibernate.initialize(workPackage.getProductBuild().getProductVersion().getProductMaster());
							if(workPackage.getProductBuild().getProductVersion().getProductMaster().getProductType()!=null){
								Hibernate.initialize(workPackage.getProductBuild().getProductVersion().getProductMaster().getProductType());
							}
						}
					}
					
				}
				if(workPackage.getTestRunJobSet()!=null){
					Hibernate.initialize(workPackage.getTestRunJobSet());
					for(TestRunJob testrunJob:workPackage.getTestRunJobSet()){
						if(testrunJob!=null){
							Hibernate.initialize(testrunJob);
							if(testrunJob.getRunConfiguration()!=null){
								Hibernate.initialize(testrunJob.getRunConfiguration());
								
								
							}
						}
					}
				}
				if(workPackage.getTestSuiteList().size()!=0){
					Hibernate.initialize(workPackage.getTestSuiteList());
					Set<TestSuiteList> tsSet=workPackage.getTestSuiteList();
					for(TestSuiteList ts:tsSet){
						Set<WorkPackageTestCaseExecutionPlan> wptcPlanSet=ts.getWptcePlanSet();
						if(wptcPlanSet.size()!=0){
							Hibernate.initialize(wptcPlanSet);
							for(WorkPackageTestCaseExecutionPlan wptcPlan:wptcPlanSet){
								Hibernate.initialize(wptcPlan.getTestCase());
								Hibernate.initialize(wptcPlan.getTestCaseExecutionResult());
								Hibernate.initialize(wptcPlan.getRunConfiguration());
								Hibernate.initialize(wptcPlan.getRunConfiguration().getRunconfiguration());
							}
						}
						
					
						
					}
				}
				if(workPackage.getTestcaseList().size()!=0){
					Hibernate.initialize(workPackage.getTestcaseList());
					Set<TestCaseList> tclist=  workPackage.getTestcaseList();
					for(TestCaseList tc:tclist){
						Hibernate.initialize(tc);
						Set<WorkPackageTestCaseExecutionPlan> wptcPlanSet=tc.getWorkPackageTestCaseExecutionPlan();
						if(wptcPlanSet.size()!=0){
							Hibernate.initialize(wptcPlanSet);
							for(WorkPackageTestCaseExecutionPlan wptcPlan:wptcPlanSet){
								Hibernate.initialize(wptcPlan.getTestCase());
								Hibernate.initialize(wptcPlan.getTestCaseExecutionResult());
								Hibernate.initialize(wptcPlan.getRunConfiguration());
								Hibernate.initialize(wptcPlan.getRunConfiguration().getRunconfiguration());
							}
						}
						
					
						
					}
				}
				
				if(workPackage.getWorkPackageTestCaseExecutionPlan().size()!=0){
					Hibernate.initialize(workPackage.getWorkPackageTestCaseExecutionPlan());
					Set<WorkPackageTestCaseExecutionPlan> wptcplanSet=workPackage.getWorkPackageTestCaseExecutionPlan();
					for(WorkPackageTestCaseExecutionPlan wptcplan:wptcplanSet){
						Hibernate.initialize(wptcplan);
						Hibernate.initialize(wptcplan.getTestCaseExecutionResult());
						if(wptcplan.getWorkPackage() != null){
							Hibernate.initialize(wptcplan.getWorkPackage());
							if(wptcplan.getWorkPackage().getProductBuild() != null){
								Hibernate.initialize(wptcplan.getWorkPackage().getProductBuild());
								if(wptcplan.getWorkPackage().getProductBuild().getProductVersion() != null){
									Hibernate.initialize(wptcplan.getWorkPackage().getProductBuild().getProductVersion());
								}
							}
						}
					}
				}
				
				if(workPackage.getWorkPackageRunConfigSet().size()!=0){
					Hibernate.initialize(workPackage.getWorkPackageRunConfigSet());
					for(WorkpackageRunConfiguration wprunconfig:workPackage.getWorkPackageRunConfigSet()){
						
						if(wprunconfig.getRunconfiguration()!=null){
							if(wprunconfig.getRunconfiguration().getEnvironmentcombination()!=null){
								Hibernate.initialize(wprunconfig.getRunconfiguration().getEnvironmentcombination());
							}
							Hibernate.initialize(wprunconfig.getRunconfiguration());
							if(wprunconfig.getRunconfiguration().getGenericDevice()!=null){
								Hibernate.initialize(wprunconfig.getRunconfiguration().getGenericDevice());
							}if(wprunconfig.getRunconfiguration().getHostList()!=null){
								Hibernate.initialize(wprunconfig.getRunconfiguration().getHostList());
							}
						}
						if(wprunconfig.getWorkPackageTestCaseExecutionPlan().size()!=0){
							Hibernate.initialize(wprunconfig.getWorkPackageTestCaseExecutionPlan());
							for(WorkPackageTestCaseExecutionPlan wptcexePlan:wprunconfig.getWorkPackageTestCaseExecutionPlan()){
								Hibernate.initialize(wptcexePlan);
								Hibernate.initialize(wptcexePlan.getTestCaseExecutionResult());
								if(wptcexePlan.getWorkPackage() != null){
									Hibernate.initialize(wptcexePlan.getWorkPackage());
									if(wptcexePlan.getWorkPackage().getProductBuild() != null){
										Hibernate.initialize(wptcexePlan.getWorkPackage().getProductBuild());
										if(wptcexePlan.getWorkPackage().getProductBuild().getProductVersion() != null){
											Hibernate.initialize(wptcexePlan.getWorkPackage().getProductBuild().getProductVersion());
										}
									}
								}
								if(wptcexePlan.getTestSuiteList()!=null){
									Hibernate.initialize(wptcexePlan.getTestSuiteList());
									if(wptcexePlan.getTestSuiteList().getTestCaseLists().size()!=0){
										Hibernate.initialize(wptcexePlan.getTestSuiteList().getTestCaseLists());
									}
								}
							}
						}
					}
				}
			}			
		}
		return workPackageList;
	}
	
	@Override
	@Transactional
	public List<WorkPackage> listAllWorkPackagesBasedOnProductBuildIdAndDateFilters(Integer productBuildId, String fromDate, String toDate) {
		log.debug("listing all WorkPackage instance based on fromDate and toDate");
		List<WorkPackage> workPackageList = null;
		String sql = null;		
		try {
			if(fromDate != null && toDate != null && !(fromDate.isEmpty() && toDate.isEmpty()))
				sql = "select * from workpackage where productbuildid="+productBuildId+" and date(createdate) between to_date('"+fromDate+"', 'YYYY/MM/DD') and to_date('"+toDate+"', 'YYYY/MM/DD')";
			workPackageList = sessionFactory.getCurrentSession().createSQLQuery(sql).addEntity(WorkPackage.class).list();	
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		for (WorkPackage workPackage : workPackageList) {
			if (workPackage != null) {
				if(workPackage.getRunConfigurationList()!=null){
					Hibernate.initialize(workPackage.getRunConfigurationList());
					Set<RunConfiguration> runConfigSet=workPackage.getRunConfigurationList();
					for(RunConfiguration runConfig:runConfigSet){
						Hibernate.initialize(runConfig);
					}
				}
				if(workPackage.getProductBuild()!=null){
					Hibernate.initialize(workPackage.getProductBuild());
					if(workPackage.getProductBuild().getProductVersion()!=null){
						Hibernate.initialize(workPackage.getProductBuild().getProductVersion());
						if(workPackage.getProductBuild().getProductVersion().getProductMaster()!=null){
							Hibernate.initialize(workPackage.getProductBuild().getProductVersion().getProductMaster());
							if(workPackage.getProductBuild().getProductVersion().getProductMaster().getProductType()!=null){
								Hibernate.initialize(workPackage.getProductBuild().getProductVersion().getProductMaster().getProductType());
							}
						}
					}
					
				}
				if(workPackage.getTestRunJobSet()!=null){
					Hibernate.initialize(workPackage.getTestRunJobSet());
					for(TestRunJob testrunJob:workPackage.getTestRunJobSet()){
						if(testrunJob!=null){
							Hibernate.initialize(testrunJob);
							if(testrunJob.getRunConfiguration()!=null){
								Hibernate.initialize(testrunJob.getRunConfiguration());
								
								
							}
						}
					}
				}
				if(workPackage.getTestSuiteList().size()!=0){
					Hibernate.initialize(workPackage.getTestSuiteList());
					Set<TestSuiteList> tsSet=workPackage.getTestSuiteList();
					for(TestSuiteList ts:tsSet){
						Set<WorkPackageTestCaseExecutionPlan> wptcPlanSet=ts.getWptcePlanSet();
						if(wptcPlanSet.size()!=0){
							Hibernate.initialize(wptcPlanSet);
							for(WorkPackageTestCaseExecutionPlan wptcPlan:wptcPlanSet){
								Hibernate.initialize(wptcPlan.getTestCase());
								Hibernate.initialize(wptcPlan.getTestCaseExecutionResult());
								Hibernate.initialize(wptcPlan.getRunConfiguration());
								Hibernate.initialize(wptcPlan.getRunConfiguration().getRunconfiguration());
							}
						}
						
					
						
					}
				}
				if(workPackage.getTestcaseList().size()!=0){
					Hibernate.initialize(workPackage.getTestcaseList());
					Set<TestCaseList> tclist=  workPackage.getTestcaseList();
					for(TestCaseList tc:tclist){
						Hibernate.initialize(tc);
						Set<WorkPackageTestCaseExecutionPlan> wptcPlanSet=tc.getWorkPackageTestCaseExecutionPlan();
						if(wptcPlanSet.size()!=0){
							Hibernate.initialize(wptcPlanSet);
							for(WorkPackageTestCaseExecutionPlan wptcPlan:wptcPlanSet){
								Hibernate.initialize(wptcPlan.getTestCase());
								Hibernate.initialize(wptcPlan.getTestCaseExecutionResult());
								Hibernate.initialize(wptcPlan.getRunConfiguration());
								Hibernate.initialize(wptcPlan.getRunConfiguration().getRunconfiguration());
							}
						}
						
					
						
					}
				}
				
				if(workPackage.getWorkPackageTestCaseExecutionPlan().size()!=0){
					Hibernate.initialize(workPackage.getWorkPackageTestCaseExecutionPlan());
					Set<WorkPackageTestCaseExecutionPlan> wptcplanSet=workPackage.getWorkPackageTestCaseExecutionPlan();
					for(WorkPackageTestCaseExecutionPlan wptcplan:wptcplanSet){
						Hibernate.initialize(wptcplan);
						Hibernate.initialize(wptcplan.getTestCaseExecutionResult());
						if(wptcplan.getWorkPackage() != null){
							Hibernate.initialize(wptcplan.getWorkPackage());
							if(wptcplan.getWorkPackage().getProductBuild() != null){
								Hibernate.initialize(wptcplan.getWorkPackage().getProductBuild());
								if(wptcplan.getWorkPackage().getProductBuild().getProductVersion() != null){
									Hibernate.initialize(wptcplan.getWorkPackage().getProductBuild().getProductVersion());
								}
							}
						}
					}
				}
				
				if(workPackage.getWorkPackageRunConfigSet().size()!=0){
					Hibernate.initialize(workPackage.getWorkPackageRunConfigSet());
					for(WorkpackageRunConfiguration wprunconfig:workPackage.getWorkPackageRunConfigSet()){
						
						if(wprunconfig.getRunconfiguration()!=null){
							if(wprunconfig.getRunconfiguration().getEnvironmentcombination()!=null){
								Hibernate.initialize(wprunconfig.getRunconfiguration().getEnvironmentcombination());
							}
							Hibernate.initialize(wprunconfig.getRunconfiguration());
							if(wprunconfig.getRunconfiguration().getGenericDevice()!=null){
								Hibernate.initialize(wprunconfig.getRunconfiguration().getGenericDevice());
							}if(wprunconfig.getRunconfiguration().getHostList()!=null){
								Hibernate.initialize(wprunconfig.getRunconfiguration().getHostList());
							}
						}
						if(wprunconfig.getWorkPackageTestCaseExecutionPlan().size()!=0){
							Hibernate.initialize(wprunconfig.getWorkPackageTestCaseExecutionPlan());
							for(WorkPackageTestCaseExecutionPlan wptcexePlan:wprunconfig.getWorkPackageTestCaseExecutionPlan()){
								Hibernate.initialize(wptcexePlan);
								Hibernate.initialize(wptcexePlan.getTestCaseExecutionResult());
								if(wptcexePlan.getWorkPackage() != null){
									Hibernate.initialize(wptcexePlan.getWorkPackage());
									if(wptcexePlan.getWorkPackage().getProductBuild() != null){
										Hibernate.initialize(wptcexePlan.getWorkPackage().getProductBuild());
										if(wptcexePlan.getWorkPackage().getProductBuild().getProductVersion() != null){
											Hibernate.initialize(wptcexePlan.getWorkPackage().getProductBuild().getProductVersion());
										}
									}
								}
								if(wptcexePlan.getTestSuiteList()!=null){
									Hibernate.initialize(wptcexePlan.getTestSuiteList());
									if(wptcexePlan.getTestSuiteList().getTestCaseLists().size()!=0){
										Hibernate.initialize(wptcexePlan.getTestSuiteList().getTestCaseLists());
									}
								}
							}
						}
					}
				}
			}			
		}
		return workPackageList;
	}

	@Override
	@Transactional
	public WorkPackage getworkpackageByTestPlanId(Integer testPlanId, Integer productBuildId) {

		WorkPackage workPackage = null;
		boolean isCombinedJob = false;
		Set<TestRunJob> testRunJobList = new HashSet<TestRunJob>();
		try {
			List list = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from WorkPackage where testRunPlanId=:testrunplanid and productBuildId=:productBuildId order by workpackageid desc")
					.setParameter("testrunplanid", testPlanId)
					.setParameter("productBuildId", productBuildId)
					.list();
			workPackage = (list != null && list.size() != 0) ? (WorkPackage) list
					.get(0) : null;
				
			if (workPackage != null) {				
				
				
				if(workPackage.getProductBuild()!=null){
					Hibernate.initialize(workPackage.getProductBuild());
					if(workPackage.getProductBuild().getProductVersion() != null){
						Hibernate.initialize(workPackage.getProductBuild().getProductVersion());
						if(workPackage.getProductBuild().getProductVersion().getProductMaster() != null){
							Hibernate.initialize(workPackage.getProductBuild().getProductVersion().getProductMaster());
							Hibernate.initialize(workPackage.getProductBuild().getProductVersion().getProductMaster().getProductName());
							Hibernate.initialize(workPackage.getProductBuild().getProductVersion().getProductMaster().getProductId());
							
							if(workPackage.getProductBuild()
									.getProductVersion().getProductMaster().getProductMode() != null){
								Hibernate.initialize(workPackage.getProductBuild()
										.getProductVersion().getProductMaster().getProductMode());
							}
							if(workPackage.getProductBuild()
									.getProductVersion().getProductMaster().getProductType() != null){
								Hibernate.initialize(workPackage.getProductBuild()
										.getProductVersion().getProductMaster().getProductType());
							}
							if(workPackage.getProductBuild()
									.getProductVersion().getProductMaster().getGenericeDevices() != null){
								Hibernate.initialize(workPackage.getProductBuild()
										.getProductVersion().getProductMaster().getGenericeDevices());								
							}
							if(workPackage.getTestRunPlan() != null){
								if(workPackage.getTestRunPlan().getProductVersionListMaster().getProductMaster().getCustomer()!=null){
									Hibernate.initialize(workPackage.getTestRunPlan().getProductVersionListMaster().getProductMaster().getCustomer());
								}	
							}
						}
					}
				}
				Hibernate.initialize(workPackage.getUserList());
				Hibernate.initialize(workPackage.getLifeCyclePhase());
				Set<GenericDevices> genericDevices =workPackage.getProductBuild()
						.getProductVersion().getProductMaster().getGenericeDevices();
				for(GenericDevices gd:genericDevices){
					Hibernate.initialize(gd.getDeviceLab());
					Hibernate.initialize(gd.getDeviceModelMaster());
					Hibernate.initialize(gd.getHostList());
					Hibernate.initialize(gd.getPlatformType());
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
				}
				if(workPackage.getProductBuild().getProductVersion().getProductMaster() != null && workPackage.getProductBuild().getProductVersion().getProductMaster().getHostLists() != null)
					Hibernate.initialize(workPackage.getProductBuild().getProductVersion().getProductMaster().getHostLists());
				if(workPackage.getProductBuild().getProductVersion().getProductMaster() != null && workPackage.getProductBuild().getProductVersion().getProductMaster().getTestFactory() != null)
					Hibernate.initialize(workPackage.getProductBuild().getProductVersion().getProductMaster().getTestFactory());
				
				if(workPackage.getProductBuild().getProductVersion().getProductMaster().getProductVersionListMasters() != null){
					Hibernate.initialize(workPackage.getProductBuild().getProductVersion().getProductMaster().getProductVersionListMasters());
				}
				try{
					if(workPackage.getProductBuild().getProductVersion().getProductMaster() != null && workPackage.getProductBuild().getProductVersion().getProductMaster().getTestManagementSystems() != null)
						Hibernate.initialize(workPackage.getProductBuild().getProductVersion().getProductMaster().getTestManagementSystems());
				} catch(Exception e){
					log.info("Error in initializing Test Management Systems : "+e.getMessage());
				}
				Hibernate.initialize(workPackage.getEnvironmentList());
				Hibernate.initialize(workPackage.getLocaleList());
				Hibernate.initialize(workPackage.getWorkPackageTestCases());
				Hibernate.initialize(workPackage.getWorkPackageTestSuites());
				Hibernate.initialize(workPackage.getWorkPackageTestCaseExecutionPlan());
				Hibernate.initialize(workPackage.getEnvironmentCombinationList());
				Hibernate.initialize(workPackage.getProductFeature());				
				Hibernate.initialize(workPackage.getProductBuild().getBuildType());
				
				Set<WorkPackageFeature> wpfs=workPackage.getWorkPackageFeature();
				Hibernate.initialize(workPackage.getWorkPackageFeature());
				for(WorkPackageFeature workPackageFeature:workPackage.getWorkPackageFeature()){
					Hibernate.initialize(workPackageFeature.getFeature());
					if(workPackageFeature.getFeature()!=null){
						Hibernate.initialize(workPackageFeature.getFeature().getTestCaseList());
						Hibernate.initialize(workPackageFeature.getFeature().getExecutionPriority());
					}
				}
				Set<WorkpackageRunConfiguration> wpRunConfig=workPackage.getWorkPackageRunConfigSet();
				if(wpRunConfig.size()!=0){
					Hibernate.initialize(workPackage.getWorkPackageRunConfigSet());
					for(WorkpackageRunConfiguration wprc:wpRunConfig){						
						if(wprc.getRunconfiguration()!=null){
							Hibernate.initialize(wprc.getRunconfiguration());
							Hibernate.initialize(wprc.getRunconfiguration().getEnvironmentcombination());
							Hibernate.initialize(wprc.getRunconfiguration().getGenericDevice());
							if(wprc.getRunconfiguration().getGenericDevice()!=null){
								if(wprc.getRunconfiguration().getGenericDevice().getHostList()!=null){
									Hibernate.initialize(wprc.getRunconfiguration().getGenericDevice().getHostList());
									Hibernate.initialize(wprc.getRunconfiguration().getGenericDevice().getHostList().getCommonActiveStatusMaster());
								}
							}
							if(wprc.getRunconfiguration().getHostList()!=null){
								Hibernate.initialize(wprc.getRunconfiguration().getHostList());
								Hibernate.initialize(wprc.getRunconfiguration().getHostList().getCommonActiveStatusMaster());
							}
							if(wprc.getRunconfiguration().getTestRunPlan()!=null){
								Hibernate.initialize(wprc.getRunconfiguration().getTestRunPlan());
								if(wprc.getRunconfiguration().getTestRunPlan().getProductVersionListMaster()!=null){
									Hibernate.initialize(wprc.getRunconfiguration().getTestRunPlan().getProductVersionListMaster());
									if(wprc.getRunconfiguration().getTestRunPlan().getProductVersionListMaster().getProductMaster()!=null){
										Hibernate.initialize(wprc.getRunconfiguration().getTestRunPlan().getProductVersionListMaster().getProductMaster());
										if(wprc.getRunconfiguration().getTestRunPlan().getProductVersionListMaster().getProductMaster().getProductType()!=null){
											Hibernate.initialize(wprc.getRunconfiguration().getTestRunPlan().getProductVersionListMaster().getProductMaster().getProductType());
										}
									}
								}
								
								Hibernate.initialize(wprc.getRunconfiguration().getTestRunPlan().getAttachments());
								if( wprc.getRunconfiguration().getTestRunPlan().getAttachments() != null &&  wprc.getRunconfiguration().getTestRunPlan().getAttachments().size()>0){
									Set<Attachment> attachmentSet= wprc.getRunconfiguration().getTestRunPlan().getAttachments();
									Hibernate.initialize(attachmentSet);
									for(Attachment attach: attachmentSet){
										Hibernate.initialize(attach);
									}
								}
							}
							
						}
					}
				}
				Hibernate.initialize(workPackage.getRunConfigurationList());
				Set<RunConfiguration> runConfigurations = workPackage.getRunConfigurationList();
				for(RunConfiguration rc:runConfigurations){
					Hibernate.initialize(rc.getEnvironmentcombination());
					Hibernate.initialize(rc.getGenericDevice());
					Hibernate.initialize(rc.getHostList());
					if(rc.getWorkPackageRunConfigSet().size()!=0){
						Hibernate.initialize(rc.getWorkPackageRunConfigSet());
						Set<WorkpackageRunConfiguration>  wprunConfigSet=rc.getWorkPackageRunConfigSet();
						for(WorkpackageRunConfiguration wprunConfig:wprunConfigSet){
							 Set<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlanSet =wprunConfig.getWorkPackageTestCaseExecutionPlan();
							  if(workPackageTestCaseExecutionPlanSet.size()!=0){
								  
							for(WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan:workPackageTestCaseExecutionPlanSet){
								Hibernate.initialize(workPackageTestCaseExecutionPlan.getTestCaseExecutionResult());
								TestCaseExecutionResult trcRes=workPackageTestCaseExecutionPlan.getTestCaseExecutionResult();
								if(trcRes!=null){
								Set<TestStepExecutionResult> tcstepexecRes=trcRes.getTestStepExecutionResultSet();
								if(tcstepexecRes.size()!=0){
								for(TestStepExecutionResult testStepExecRes:tcstepexecRes){
									Hibernate.initialize(testStepExecRes.getTestCaseExecutionResult());
								}
								}
							}
								Set<Environment> environments=workPackageTestCaseExecutionPlan.getEnvironmentList();
								if(environments.size()!=0){
									Hibernate.initialize(environments);
									for(Environment environment:environments){
										Hibernate.initialize(environment.getEnvironmentCategory());
										Hibernate.initialize(environment.getEnvironmentCategory().getEnvironmentGroup());
									}
								}
								
								
							}
							
						}
							
						}
					}
				}
				Set<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlans =workPackage.getWorkPackageTestCaseExecutionPlan();
				for(WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan:workPackageTestCaseExecutionPlans){
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getRunConfiguration());
					if(workPackageTestCaseExecutionPlan.getRunConfiguration()!=null){
						if(workPackageTestCaseExecutionPlan.getRunConfiguration().getWorkpackage()!=null){
						Hibernate.initialize(workPackageTestCaseExecutionPlan.getRunConfiguration().getWorkpackage());
						}
						Hibernate.initialize(workPackageTestCaseExecutionPlan.getRunConfiguration().getRunconfiguration());
						Hibernate.initialize(workPackageTestCaseExecutionPlan.getRunConfiguration().getRunconfiguration().getEnvironmentcombination());
						Hibernate.initialize(workPackageTestCaseExecutionPlan.getRunConfiguration().getRunconfiguration().getGenericDevice());						
					}
					if(workPackageTestCaseExecutionPlan.getWorkPackage() != null){
						Hibernate.initialize(workPackageTestCaseExecutionPlan.getWorkPackage());
						if(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild() != null){
							Hibernate.initialize(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild());
							if(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion() != null){
								Hibernate.initialize(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion());
							}
						}
					}
					Set<Environment> environments=workPackageTestCaseExecutionPlan.getEnvironmentList();
					Hibernate.initialize(environments);
					for(Environment environment:environments){
						Hibernate.initialize(environment.getEnvironmentCategory());
						Hibernate.initialize(environment.getEnvironmentCategory().getEnvironmentGroup());
					}
				}
				Set<WorkPackageTestCase> workPackageTestCases =workPackage.getWorkPackageTestCases();
				for(WorkPackageTestCase workPackageTestCase:workPackageTestCases){
					Hibernate.initialize(workPackageTestCase.getTestCase());
					Hibernate.initialize(workPackageTestCase.getTestCase().getTestSuiteLists());
					Hibernate.initialize(workPackageTestCase.getTestCase().getTestCasePriority());
					Hibernate.initialize(workPackageTestCase.getTestCase().getExecutionTypeMaster());
				}
				
				Set<WorkPackageTestSuite> workPackageTestSuites =workPackage.getWorkPackageTestSuites();
				for(WorkPackageTestSuite workPackageTestSuite:workPackageTestSuites){
					Hibernate.initialize(workPackageTestSuite.getTestSuite());
					if(workPackageTestSuite.getTestSuite()!=null)
					Hibernate.initialize(workPackageTestSuite.getTestSuite().getExecutionPriority());
					Hibernate.initialize(workPackageTestSuite.getTestSuite().getTestCaseLists());
				}
				for(WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan:workPackageTestCaseExecutionPlans){
				Set<TestCaseConfiguration> testCaseConfigurationSet =workPackageTestCaseExecutionPlan.getTestCaseConfigurationSet();
				for(TestCaseConfiguration testCaseConfiguration:testCaseConfigurationSet){
					Hibernate.initialize(testCaseConfiguration.getWorkpackage_run_list());
					Hibernate.initialize(testCaseConfiguration.getEnvironmentCombination());
				}
			}
				
				for(WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan:workPackageTestCaseExecutionPlans){
					Hibernate.initialize(workPackageTestCaseExecutionPlan.getTestCaseExecutionResult());
					TestCaseExecutionResult trcRes=workPackageTestCaseExecutionPlan.getTestCaseExecutionResult();
					if(trcRes!=null){
					Set<TestStepExecutionResult> tcstepexecRes=trcRes.getTestStepExecutionResultSet();
					if(tcstepexecRes.size()!=0){
					for(TestStepExecutionResult testStepExecRes:tcstepexecRes){
						Hibernate.initialize(testStepExecRes.getTestCaseExecutionResult());
					}
					}
				}
					
				}
				Hibernate.initialize(workPackage.getWorkFlowEvent());
				Hibernate.initialize(workPackage.getWorkFlowEvent().getWorkFlow());
				Hibernate.initialize(workPackage.getTestcaseList());
				Hibernate.initialize(workPackage.getTestSuiteList());
				Hibernate.initialize(workPackage.getTestRunPlan());
				if(workPackage.getTestRunPlan() != null){
					Hibernate.initialize(workPackage.getTestRunPlan().getAttachments());
					if( workPackage.getTestRunPlan().getAttachments() != null &&  workPackage.getTestRunPlan().getAttachments().size()>0){
						Set<Attachment> attachmentSet= workPackage.getTestRunPlan().getAttachments();
						Hibernate.initialize(attachmentSet);
						for(Attachment attach: attachmentSet){
							Hibernate.initialize(attach);
						}
					}
				}
				
				Set<TestManagementSystem> testManagementSystems=
				workPackage.getProductBuild()
						.getProductVersion().getProductMaster().getTestManagementSystems();
				for (TestManagementSystem testManagementSystem : testManagementSystems) {
					Hibernate.initialize(testManagementSystem.getTestManagementSystemMappings());
				}
				
				Hibernate.initialize(workPackage.getEnvironmentCombinationList());
				if(workPackage.getResultsReportingMode() !=null && workPackage.getResultsReportingMode().equalsIgnoreCase("Combined job")){
					Hibernate.initialize(workPackage.getCombinedResultsReportingJob());
					if(workPackage.getCombinedResultsReportingJob() != null){
						testRunJobList.add(workPackage.getCombinedResultsReportingJob());
						workPackage.setTestRunJobSet(testRunJobList);
					}
				}else {
					Hibernate.initialize(workPackage.getTestRunJobSet());
					testRunJobList = workPackage.getTestRunJobSet();
				}
				for(TestRunJob trj : testRunJobList){
					Hibernate.initialize(trj);
					Hibernate.initialize(trj.getTestSuiteSet());
					for(TestSuiteList tsl : trj.getTestSuiteSet()){
						Hibernate.initialize(tsl);
						Hibernate.initialize(tsl.getTestCaseLists());
						for(TestCaseList tcl : tsl.getTestCaseLists()){
							Hibernate.initialize(tcl);
							Hibernate.initialize(tcl.getTestCaseStepsLists());
							for(TestCaseStepsList tcsl : tcl.getTestCaseStepsLists()){
								Hibernate.initialize(tcsl);
							}
						}
					}
				}
			}
		} catch (Exception re) {
			log.error("Exception occurres failed", re);
			
		}
		return workPackage;

	
	}

	@Override
	@Transactional
	public WorkPackage getWorkPackageByProductBuild(Integer productBuildId) {
		log.debug("getting WorkPackage instance by Product Build id");
		WorkPackage workPackage=null;
		try {
			List list = sessionFactory.getCurrentSession().createQuery("from WorkPackage wp where productbuildid=:productbuildid order by workpackageid desc")
					.setParameter("productbuildid", productBuildId).list();
		
			workPackage = (list!=null && list.size()!=0)?(WorkPackage)list.get(0):null;
			if (workPackage != null) {
				if(workPackage.getRunConfigurationList()!=null){
					Hibernate.initialize(workPackage.getRunConfigurationList());
					Set<RunConfiguration> runConfigSet=workPackage.getRunConfigurationList();
					for(RunConfiguration runConfig:runConfigSet){
						Hibernate.initialize(runConfig);
					}
				}
				if(workPackage.getProductBuild()!=null){
					Hibernate.initialize(workPackage.getProductBuild());
					if(workPackage.getProductBuild().getProductVersion()!=null){
						Hibernate.initialize(workPackage.getProductBuild().getProductVersion());
						if(workPackage.getProductBuild().getProductVersion().getProductMaster()!=null){
							Hibernate.initialize(workPackage.getProductBuild().getProductVersion().getProductMaster());
							if(workPackage.getProductBuild().getProductVersion().getProductMaster().getProductType()!=null){
								Hibernate.initialize(workPackage.getProductBuild().getProductVersion().getProductMaster().getProductType());
							}
						}
					}
					
				}
				if(workPackage.getTestRunJobSet()!=null){
					Hibernate.initialize(workPackage.getTestRunJobSet());
					for(TestRunJob testrunJob:workPackage.getTestRunJobSet()){
						if(testrunJob!=null){
							Hibernate.initialize(testrunJob);
							if(testrunJob.getRunConfiguration()!=null){
								Hibernate.initialize(testrunJob.getRunConfiguration());
								
								
							}
						}
					}
				}
				if(workPackage.getTestSuiteList().size()!=0){
					Hibernate.initialize(workPackage.getTestSuiteList());
					Set<TestSuiteList> tsSet=workPackage.getTestSuiteList();
					for(TestSuiteList ts:tsSet){
						Set<WorkPackageTestCaseExecutionPlan> wptcPlanSet=ts.getWptcePlanSet();
						if(wptcPlanSet.size()!=0){
							Hibernate.initialize(wptcPlanSet);
							for(WorkPackageTestCaseExecutionPlan wptcPlan:wptcPlanSet){
								Hibernate.initialize(wptcPlan.getTestCase());
								Hibernate.initialize(wptcPlan.getTestCaseExecutionResult());
								Hibernate.initialize(wptcPlan.getRunConfiguration());
								Hibernate.initialize(wptcPlan.getRunConfiguration().getRunconfiguration());
							}
						}
						
					
						
					}
				}
				if(workPackage.getTestcaseList().size()!=0){
					Hibernate.initialize(workPackage.getTestcaseList());
					Set<TestCaseList> tclist=  workPackage.getTestcaseList();
					for(TestCaseList tc:tclist){
						Hibernate.initialize(tc);
						Set<WorkPackageTestCaseExecutionPlan> wptcPlanSet=tc.getWorkPackageTestCaseExecutionPlan();
						if(wptcPlanSet.size()!=0){
							Hibernate.initialize(wptcPlanSet);
							for(WorkPackageTestCaseExecutionPlan wptcPlan:wptcPlanSet){
								Hibernate.initialize(wptcPlan.getTestCase());
								Hibernate.initialize(wptcPlan.getTestCaseExecutionResult());
								Hibernate.initialize(wptcPlan.getRunConfiguration());
								Hibernate.initialize(wptcPlan.getRunConfiguration().getRunconfiguration());
							}
						}
						
					
						
					}
				}
				
				if(workPackage.getWorkPackageTestCaseExecutionPlan().size()!=0){
					Hibernate.initialize(workPackage.getWorkPackageTestCaseExecutionPlan());
					Set<WorkPackageTestCaseExecutionPlan> wptcplanSet=workPackage.getWorkPackageTestCaseExecutionPlan();
					for(WorkPackageTestCaseExecutionPlan wptcplan:wptcplanSet){
						Hibernate.initialize(wptcplan);
						Hibernate.initialize(wptcplan.getTestCaseExecutionResult());
						if(wptcplan.getWorkPackage() != null){
							Hibernate.initialize(wptcplan.getWorkPackage());
							if(wptcplan.getWorkPackage().getProductBuild() != null){
								Hibernate.initialize(wptcplan.getWorkPackage().getProductBuild());
								if(wptcplan.getWorkPackage().getProductBuild().getProductVersion() != null){
									Hibernate.initialize(wptcplan.getWorkPackage().getProductBuild().getProductVersion());
								}
							}
						}
					}
				}
				
				if(workPackage.getWorkPackageRunConfigSet().size()!=0){
					Hibernate.initialize(workPackage.getWorkPackageRunConfigSet());
					for(WorkpackageRunConfiguration wprunconfig:workPackage.getWorkPackageRunConfigSet()){
						
						if(wprunconfig.getRunconfiguration()!=null){
							if(wprunconfig.getRunconfiguration().getEnvironmentcombination()!=null){
								Hibernate.initialize(wprunconfig.getRunconfiguration().getEnvironmentcombination());
							}
							Hibernate.initialize(wprunconfig.getRunconfiguration());
							if(wprunconfig.getRunconfiguration().getGenericDevice()!=null){
								Hibernate.initialize(wprunconfig.getRunconfiguration().getGenericDevice());
							}if(wprunconfig.getRunconfiguration().getHostList()!=null){
								Hibernate.initialize(wprunconfig.getRunconfiguration().getHostList());
							}
						}
						if(wprunconfig.getWorkPackageTestCaseExecutionPlan().size()!=0){
							Hibernate.initialize(wprunconfig.getWorkPackageTestCaseExecutionPlan());
							for(WorkPackageTestCaseExecutionPlan wptcexePlan:wprunconfig.getWorkPackageTestCaseExecutionPlan()){
								Hibernate.initialize(wptcexePlan);
								Hibernate.initialize(wptcexePlan.getTestCaseExecutionResult());
								if(wptcexePlan.getWorkPackage() != null){
									Hibernate.initialize(wptcexePlan.getWorkPackage());
									if(wptcexePlan.getWorkPackage().getProductBuild() != null){
										Hibernate.initialize(wptcexePlan.getWorkPackage().getProductBuild());
										if(wptcexePlan.getWorkPackage().getProductBuild().getProductVersion() != null){
											Hibernate.initialize(wptcexePlan.getWorkPackage().getProductBuild().getProductVersion());
										}
									}
								}
								if(wptcexePlan.getTestSuiteList()!=null){
									Hibernate.initialize(wptcexePlan.getTestSuiteList());
									if(wptcexePlan.getTestSuiteList().getTestCaseLists().size()!=0){
										Hibernate.initialize(wptcexePlan.getTestSuiteList().getTestCaseLists());
									}
								}
							}
						}
					}
					
				}
			}
			log.debug("getByProductVersionListId successful");
		} catch (RuntimeException re) {
			log.error("getByProductVersionListId failed", re);
		}
		return workPackage;        
	}

	@Override
	@Transactional
	public void addTestCycle(TestCycle testCycle) {
		log.info("adding TestCycle instance");
		try {	
			sessionFactory.getCurrentSession().save(testCycle);
			log.info("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}	
	}

	@Override
	@Transactional
	public List<TestCycle> getTestCycleList(Integer testFactoryId,Integer productId, Integer productVersionId, Integer testPlanGroupId, int jtStartIndex, int jtPageSize) {
		log.info("getting TestCycle List");
		List<TestCycle> testCycleList = new ArrayList<TestCycle>();
		try {	
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestCycle.class, "tc");
			if (testPlanGroupId !=-1) {
				c.createAlias("tc.testRunPlanGroup", "tpg");
				c.add(Restrictions.eq("tpg.testRunPlanGroupId", testPlanGroupId));
			} 
			if (productVersionId !=-1) {
				c.createAlias("tc.testRunPlanGroup", "tpg");
				c.createAlias("tpg.productVersionListMaster", "productVersion");
				c.add(Restrictions.eq("productVersion.productVersionListId", productVersionId));
			} 
			if (productId !=-1) {
				c.createAlias("tc.testRunPlanGroup", "tpg");
				c.createAlias("tpg.product", "prod");
				c.add(Restrictions.eq("prod.productId", productId));
			} 
			if (testFactoryId !=-1) {
				
			} 
			
			c.addOrder(Order.desc("tc.testCycleId"));
			c.setFirstResult(jtStartIndex);
			c.setMaxResults(jtPageSize);
			testCycleList=c.list();
			log.info("Listing successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}
		return testCycleList;
	}

	@Override
	public List<WorkPackageTCEPSummaryDTO> getWPTCExecutionSummaryByTestCycleId(Integer testCycleId, int jtStartIndex, int jtPageSize) {
		List<WorkPackageTCEPSummaryDTO> wptcepResultList = new ArrayList<WorkPackageTCEPSummaryDTO>();
	try {
		String sql="select pm.productName as productName, wp.workpackageId as workPackageId,wp.name as workPackageName ,"
				+ "wp.actualStartDate as actualStartDate, wp.actualEndDate as actualEndDate, wflow.stageName as workFlowStageName,"
				+ "(select etm.name from execution_type_master as etm  where etm.executionTypeId =wp.workPackageType ) as exeType,"
				+ "(SELECT COUNT(wphastc.workPackageId) FROM  workpackage_has_testcase wphastc "
				+ "WHERE wphastc.workPackageId =wp.workpackageId ) AS testCaseCount, "
				+ "(SELECT COUNT(wphasTestSuite.testSuiteId) FROM workpackage_has_testsuite  wphasTestSuite "
				+ "WHERE wphasTestSuite.workpackageId=wp.workpackageId ) AS testSuiteCount, "
				+ "(SELECT COUNT(wphasFeature.featureId) FROM   workpackage_has_feature  wphasFeature "
				+ "WHERE wphasFeature.workpackageId=wp.workpackageId ) AS featueCount, "
				+ "(SELECT COUNT(job.workpackageId) FROM test_run_job job WHERE job.workpackageId =wp.workpackageId  ) AS jobCount, "

				+ "(SELECT COUNT(job.workpackageId) FROM test_run_job job WHERE job.workpackageId =wp.workpackageId and job.testRunStatus=3 ) AS jobsExecuting, "

				+ "(SELECT COUNT(job.workpackageId) FROM test_run_job job WHERE job.workpackageId =wp.workpackageId and job.testRunStatus=4 ) AS jobsQueued, "

				+ "(SELECT COUNT(job.workpackageId) FROM test_run_job job WHERE job.workpackageId =wp.workpackageId and job.testRunStatus=5 ) AS jobsCompleted, "

				+ "(SELECT COUNT(job.workpackageId) FROM test_run_job job WHERE job.workpackageId =wp.workpackageId and job.testRunStatus=7 ) AS jobsAborted, "
				
				+ "(SELECT COUNT(buglist.testExecutionResultBugId) FROM workpackage_testcase_execution_plan AS wptcPlan "
				+ "LEFT JOIN test_execution_result_bug_list AS buglist ON wptcPlan.wptcepId=buglist.testCaseExecutionResultId "
				+ "WHERE wptcPlan.workPackageId=wp.workpackageId) AS defectsCount, "
				+ "( SELECT COUNT(tcres.result)   FROM workpackage_testcase_execution_plan as wptcplan "
				+ "inner join  testcase_execution_result  AS tcres "
				+ "on tcres.testCaseExecutionResultId = wptcplan.wptcepId and wptcplan.status=1 "
				+ "where wptcplan.workpackageId= wp.workpackageId  and tcres.result='PASSED' ) AS passedCount,"
				+ " ( SELECT COUNT(tcres.result)   FROM  workpackage_testcase_execution_plan as wptcplan "
				+ "inner join testcase_execution_result  AS tcres "
				+ "on tcres.testCaseExecutionResultId = wptcplan.wptcepId and wptcplan.status=1 "
				+ " WHERE wptcplan.workpackageId= wp.workpackageId AND tcres.result='FAILED' ) AS failedCount,"
				+ " ( SELECT COUNT(tcres.result)   FROM  workpackage_testcase_execution_plan as wptcplan "
				+ "inner join testcase_execution_result  AS tcres "
				+ "on tcres.testCaseExecutionResultId = wptcplan.wptcepId and wptcplan.status=1"
				+ " WHERE wptcplan.workpackageId = wp.workpackageId AND tcres.result='BLOCKED' ) AS blockedCount,"
				+ " ( SELECT COUNT(tcres.result)   FROM  workpackage_testcase_execution_plan as wptcplan "
				+ "inner join testcase_execution_result  AS tcres "
				+ "on tcres.testCaseExecutionResultId = wptcplan.wptcepId and wptcplan.status=1 "
				+ "WHERE wptcplan.workpackageId = wp.workpackageId AND tcres.result='NORUN' ) AS norunCount,"
				+ "  ( SELECT COUNT(tcres.result)   FROM  workpackage_testcase_execution_plan as wptcplan "
				+ "inner join testcase_execution_result  AS tcres "
				+ "on tcres.testCaseExecutionResultId= wptcplan.wptcepId and wptcplan.status = 1"
				+ " WHERE wptcplan.workpackageId = wp.workpackageId AND tcres.result IS NULL ) AS notexecutedCount, "
				+ "(SELECT COUNT(tser.teststepexecutionresultid) FROM teststep_execution_result as tser "
				+ "inner join  testcase_execution_result  AS tcres "
				+ "on tcres.testCaseExecutionResultId = tser.tescaseexecutionresultid "
				+ "inner join workpackage_testcase_execution_plan as wptcplan "
				+ "on wptcplan.wptcepId = tcres.testCaseExecutionResultId "
				+ "where wptcplan.workpackageId= wp.workpackageId )  AS teststepcount "
				
				+ "FROM workpackage wp left JOIN workflowevent AS wfevent on wp.workfloweventid = wfevent.workfloweventId "
				+ "left join workflow as wflow on wfevent.workflowId = wflow.workflowId "
				+ "INNER JOIN  product_build pb ON pb.productBuildId=wp.productBuildId ";
				
		if( testCycleId != -1){//Test Cycle Level
			sql = sql + "inner join product_version_list_master  pvlm on pb.productVersionId=pvlm.productVersionListId "
					+ "inner join product_master pm on pvlm.productId=pm.productId "
					+ "where wp.test_cycle_id="+ testCycleId;
		}
		if(jtStartIndex != -1 && jtPageSize != -1 ){
			sql = sql + "offset ";								
			sql = sql+jtStartIndex+" limit "+jtPageSize+"";	
		}
		
		log.info("SQL Query for Workpackages : "+sql);
		
		wptcepResultList=sessionFactory.getCurrentSession().createSQLQuery(sql)
				.addScalar("productName",StandardBasicTypes.STRING )
				.addScalar("workPackageId",StandardBasicTypes.INTEGER ).addScalar("workPackageName", StandardBasicTypes.STRING)
				.addScalar("actualStartDate",StandardBasicTypes.DATE ).addScalar("actualEndDate", StandardBasicTypes.DATE)
				.addScalar("workFlowStageName", StandardBasicTypes.STRING).addScalar("exeType", StandardBasicTypes.STRING)					
				.addScalar("testCaseCount", StandardBasicTypes.INTEGER)
				.addScalar("testSuiteCount", StandardBasicTypes.INTEGER).addScalar("featueCount", StandardBasicTypes.INTEGER)					
				.addScalar("jobCount", StandardBasicTypes.INTEGER)
				.addScalar("jobsExecuting", StandardBasicTypes.INTEGER).addScalar("jobsQueued", StandardBasicTypes.INTEGER)
				.addScalar("jobsCompleted", StandardBasicTypes.INTEGER).addScalar("jobsAborted", StandardBasicTypes.INTEGER)					
				.addScalar("defectsCount", StandardBasicTypes.INTEGER)
				.addScalar("passedCount", StandardBasicTypes.INTEGER).addScalar("failedCount", StandardBasicTypes.INTEGER)
				.addScalar("blockedCount", StandardBasicTypes.INTEGER).addScalar("norunCount", StandardBasicTypes.INTEGER)
				.addScalar("notexecutedCount", StandardBasicTypes.INTEGER).addScalar("teststepcount", StandardBasicTypes.INTEGER)					
				.setResultTransformer(Transformers.aliasToBean(WorkPackageTCEPSummaryDTO.class)).list();
			
	} catch (RuntimeException re) {
	log.error("getWPTCExecutionSummary by BuildId failed", re);
		re.printStackTrace();
	}
	return wptcepResultList;		
	}

	@Override
	@Transactional
	public Integer getWPTotalCount(Integer testFactoryId, Integer productId,Integer productBuildId) {
		log.info("In getWPTotalCount");
		Integer totalWPCount = 0;
		try {
			String sql = "select count(*) from workpackage wp "		
				+ "left JOIN workflowevent AS wfevent on wp.workfloweventid = wfevent.workfloweventId "
				+ "left join workflow as wflow on wfevent.workflowId = wflow.workflowId "
				+ "INNER JOIN  product_build pb ON pb.productBuildId=wp.productBuildId ";
				
			if( productId != -1 && productBuildId == -1){//Product Level
				sql = sql + "inner join product_version_list_master  pvlm on pb.productVersionId=pvlm.productVersionListId "
						+ "inner join product_master pm on pvlm.productId=pm.productId "
						+ "where pm.productId="+ productId +"  and pb.status=1";
			}else if(productId == -1 && productBuildId != -1){//Build Level
				sql = sql  + "inner join product_version_list_master  pvlm on pb.productVersionId=pvlm.productVersionListId "
						+ "inner join product_master pm on pvlm.productId=pm.productId "
						+ "WHERE  pb.productBuildId="+productBuildId+"  and pb.status=1";	
			}else if(testFactoryId !=null && productId == -1){//Build Level
				sql = sql + "inner join product_version_list_master  pvlm on pb.productVersionId=pvlm.productVersionListId "
						+ "inner join product_master pm on pvlm.productId=pm.productId "
						+ "INNER JOIN test_factory tf ON (pm.testFactoryId=tf.testFactoryId)"
						+ "where tf.testFactoryId="+testFactoryId+"  and pb.status=1";
				
			}	
			totalWPCount = ((Number)sessionFactory.getCurrentSession().createSQLQuery(sql).uniqueResult()).intValue();
			log.info("totalWPCount : "+ totalWPCount);
		} catch (RuntimeException re) {
			log.error("getWPTCExecutionSummary by BuildId failed", re);
			re.printStackTrace();
		}
		return totalWPCount;	
		
	}
}