package com.hcl.atf.taf.scheduler.jobs;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.dao.ExecutionTypeMasterDAO;
import com.hcl.atf.taf.dao.ProductBuildDAO;
import com.hcl.atf.taf.model.ExecutionTypeMaster;
import com.hcl.atf.taf.model.LifeCyclePhase;
import com.hcl.atf.taf.model.ProductBuild;
import com.hcl.atf.taf.model.TestRunPlan;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.WorkFlowEvent;
import com.hcl.atf.taf.model.WorkPackage;
import com.hcl.atf.taf.service.ProductListService;
import com.hcl.atf.taf.service.TestExecutionService;
import com.hcl.atf.taf.service.TestRunConfigurationService;
import com.hcl.atf.taf.service.UserListService;
import com.hcl.atf.taf.service.WorkPackageService;

public class DefaultTestConfigCycleJob implements Job {

	private static final Log log = LogFactory.getLog(DefaultTestConfigCycleJob.class);

	@Autowired
	private TestExecutionService testExecutionService;
	@Autowired
	private TestRunConfigurationService testRunConfigurationService;

	@Autowired
	private ProductListService productListService;

	@Autowired
	private WorkPackageService workPackageService;

	@Autowired
	private ExecutionTypeMasterDAO executionTypeMasterDAO;
	
	@Autowired
	private ProductBuildDAO productBuildDAO;
	
	@Autowired
	private UserListService userListService;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
				
		log.info("Executing TestConfigCycle : " + context.getJobDetail().getKey().getName() + " at : " + context.getFireTime() + " : Next Fire at : " + context.getNextFireTime());
		TestRunPlan testRunPlan = productListService.getTestRunPlanByName(context.getTrigger().getKey().getName());
		if (testRunPlan == null) {
        	log.error("Quartz Scheduler Error : Unable to find the Test Run Plan for execution" , new Exception());
			return;
		} else {
			log.info("Found Test Run Plan : " + testRunConfigurationService + " : " + testRunPlan.getTestRunPlanName() + " : " + testRunPlan.getTestRunCronSchedule());
		}
				
		WorkPackage newWorkpackage=new WorkPackage();
		String name=testRunPlan.getProductVersionListMaster().getProductMaster().getProductName()+"-"+testRunPlan.getProductVersionListMaster().getProductVersionName()+"-"+new Date();
		
		newWorkpackage.setName(name);
		newWorkpackage.setDescription(name +" created.");
		
		newWorkpackage.setCreateDate(DateUtility.getCurrentTime());
		newWorkpackage.setModifiedDate(DateUtility.getCurrentTime());
		newWorkpackage.setStatus(1);
		newWorkpackage.setIsActive(1);
		if(testRunPlan.getExecutionType().getExecutionTypeId()==3)
		{
			ExecutionTypeMaster executionTypeMaster=executionTypeMasterDAO.getExecutionTypeByExecutionTypeId(7);
			newWorkpackage.setWorkPackageType(executionTypeMaster);
		}else{
			ExecutionTypeMaster executionTypeMaster=executionTypeMasterDAO.getExecutionTypeByExecutionTypeId(8);
			newWorkpackage.setWorkPackageType(executionTypeMaster);
		}
		
		ProductBuild productBuild=productBuildDAO.getLatestProductBuild(testRunPlan.getProductVersionListMaster().getProductMaster().getProductId());
		newWorkpackage.setProductBuild(productBuild);
		
		newWorkpackage.setTestRunPlan(testRunPlan);
		newWorkpackage.setTestRunPlanGroup(null);
		newWorkpackage.setSourceType(IDPAConstants.WORKPACKAGE_SOURCE_TESTRUNPLAN_SCHEDULER);
		
		WorkFlowEvent workFlowEvent = new WorkFlowEvent();
		workFlowEvent.setEventDate(DateUtility.getCurrentTime());
		workFlowEvent.setRemarks("New Workapckage Added :"+newWorkpackage.getName());
		workFlowEvent.setUser(null);
		workFlowEvent.setWorkFlow(workPackageService.getWorkFlowByEntityIdStageId(IDPAConstants.WORKPACKAGE_ENTITY_ID,IDPAConstants.WORKFLOW_STAGE_ID_NEW));
		
		workPackageService.addWorkFlowEvent(workFlowEvent);
		newWorkpackage.setWorkFlowEvent(workFlowEvent);
		newWorkpackage.setPlannedEndDate(DateUtility.getCurrentTime());
		newWorkpackage.setPlannedStartDate(DateUtility.getCurrentTime());
		UserList userList= userListService.getUserByLoginId(IDPAConstants.USER_FOR_TESTRUN_PLAN);
		newWorkpackage.setUserList(userList);
		newWorkpackage.setIterationNumber(-1);
		LifeCyclePhase lifeCyclePhase = new LifeCyclePhase();
		lifeCyclePhase.setLifeCyclePhaseId(4);
		newWorkpackage.setLifeCyclePhase(lifeCyclePhase);
		newWorkpackage.setWorkPackageId(0);
		if(productBuild!=null){
			workPackageService.addWorkPackage(newWorkpackage);
			workPackageService.workpackageExxecutionPlan(newWorkpackage,testRunPlan,null);
		}else{
			log.info("Unable to execute Test Run Plan. This could be because the Product Build specified is not active");
		}
	
		
	}

}
