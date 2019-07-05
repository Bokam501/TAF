package com.hcl.atf.taf.controller;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hcl.atf.taf.constants.TAFConstants;
import com.hcl.atf.taf.controller.utilities.CronValidate;
import com.hcl.atf.taf.controller.utilities.ValidationUtility;
import com.hcl.atf.taf.exceptions.ATFException;
import com.hcl.atf.taf.integration.testManagementSystem.TAFTestManagementSystemIntegrator;
import com.hcl.atf.taf.model.DeviceList;
import com.hcl.atf.taf.model.GenericDevices;
import com.hcl.atf.taf.model.ProductFeature;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.RunConfiguration;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.TestEnvironmentDevices;
import com.hcl.atf.taf.model.TestManagementSystem;
import com.hcl.atf.taf.model.TestManagementSystemMapping;
import com.hcl.atf.taf.model.TestRunConfigurationChild;
import com.hcl.atf.taf.model.TestRunConfigurationParent;
import com.hcl.atf.taf.model.TestRunList;
import com.hcl.atf.taf.model.TestRunPlan;
import com.hcl.atf.taf.model.TestRunSelectedDeviceList;
import com.hcl.atf.taf.model.TestSuiteList;
import com.hcl.atf.taf.model.TrccExecutionPlan;
import com.hcl.atf.taf.model.TrccExecutionPlanDetails;
import com.hcl.atf.taf.model.json.JsonDeviceList;
import com.hcl.atf.taf.model.json.JsonGenericDevice;
import com.hcl.atf.taf.model.json.JsonProductFeature;
import com.hcl.atf.taf.model.json.JsonRunConfiguration;
import com.hcl.atf.taf.model.json.JsonTestCaseList;
import com.hcl.atf.taf.model.json.JsonTestEnvironmentDevices;
import com.hcl.atf.taf.model.json.JsonTestRunConfigurationChild;
import com.hcl.atf.taf.model.json.JsonTestRunConfigurationJob;
import com.hcl.atf.taf.model.json.JsonTestRunConfigurationParent;
import com.hcl.atf.taf.model.json.JsonTestRunList;
import com.hcl.atf.taf.model.json.JsonTestRunPlan;
import com.hcl.atf.taf.model.json.JsonTestRunSelectedDeviceList;
import com.hcl.atf.taf.model.json.JsonTestSuiteList;
import com.hcl.atf.taf.model.json.JsonTrccExecutionPlan;
import com.hcl.atf.taf.model.json.JsonTrccExecutionPlanDetails;
import com.hcl.atf.taf.model.json.jtable.JTableResponse;
import com.hcl.atf.taf.model.json.jtable.JTableResponseOptions;
import com.hcl.atf.taf.model.json.jtable.JTableSingleResponse;
import com.hcl.atf.taf.scheduler.TAFScheduleManager;
import com.hcl.atf.taf.scheduler.TestConfigScheduleEntity;
import com.hcl.atf.taf.service.DeviceListService;
import com.hcl.atf.taf.service.EnvironmentService;
import com.hcl.atf.taf.service.PasswordEncryptionService;
import com.hcl.atf.taf.service.ProductListService;
import com.hcl.atf.taf.service.TestEnvironmentDeviceService;
import com.hcl.atf.taf.service.TestExecutionService;
import com.hcl.atf.taf.service.TestRunConfigurationService;
import com.hcl.atf.taf.service.TestSuiteConfigurationService;
import com.hcl.hpqc.connector.ConnectorHPQC;

@Controller
public class TestRunConfigurationController {

	private static final Log log = LogFactory.getLog(TestRunConfigurationController.class);
	
	@Autowired
	private TestRunConfigurationService testRunConfigurationService;
	@Autowired
	private TestExecutionService testExecutionService;
	@Autowired
	private TestEnvironmentDeviceService testEnvironmentDeviceService;
	@Autowired
	private TestSuiteConfigurationService testSuiteConfigurationService;
	@Autowired
	private DeviceListService deviceListService;
	@Autowired
	private ToolIntegrationController toolsController;	
	@Autowired
	private ProductListService 	productListService;
	@Autowired
	private TAFTestManagementSystemIntegrator tafTestManagementSystemIntegrator;
	@Autowired
	private PasswordEncryptionService passwordEncryptionService;
	@Autowired
	private EnvironmentService environmentService;
	
	//Parent table CRUD operation
	
	@RequestMapping(value="test.run.parent.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listParentConfiguration(@RequestParam int jtStartIndex, @RequestParam int jtPageSize) {
		log.debug("inside test.run.parent.list");
		JTableResponse jTableResponse;
			try 
			{
				int userId=1;//TODO has to be integrated with user session object
				List<TestRunConfigurationParent> testRunConfigurationParent=testRunConfigurationService.listAllTestRunConfigurationParent(jtStartIndex, jtPageSize);
				List<JsonTestRunConfigurationParent> jsonTestRunConfigurationParent=new ArrayList<JsonTestRunConfigurationParent>();
				for(TestRunConfigurationParent trcp: testRunConfigurationParent){
					jsonTestRunConfigurationParent.add(new JsonTestRunConfigurationParent(trcp));
				
			}
				jTableResponse = new JTableResponse("OK", jsonTestRunConfigurationParent,testRunConfigurationService.getTotalRecordsOfTestRunConfigurationParent(userId));
	        testRunConfigurationParent = null;   
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching ParentConfiguration!");
	            log.error("JSON ERROR : fetching ParentConfiguration", e);
	        }
        return jTableResponse;
    }

	@RequestMapping(value="test.run.parent.add",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse addParentConfiguration(
    		@ModelAttribute JsonTestRunConfigurationParent jsonTestRunConfigurationParent, BindingResult result) {
		
		JTableSingleResponse jTableSingleResponse;
		if(result.hasErrors()){
			jTableSingleResponse  = new JTableSingleResponse("ERROR","Invalid form!"); 
		}
		
		try {
			int userId=1;
			jsonTestRunConfigurationParent.setUserId(userId);
			
				
				TestRunConfigurationParent testRunConfigurationParent= jsonTestRunConfigurationParent.getTestRunConfigurationParent();
				if (!(isTestRunConfigurationParentExists(testRunConfigurationParent.getTestRunconfigurationName().trim()))){
					testRunConfigurationService.addTestRunConfigurationParent(testRunConfigurationParent);
		            jTableSingleResponse = new JTableSingleResponse("OK",new JsonTestRunConfigurationParent(testRunConfigurationParent));
				}
				else{
					jTableSingleResponse = new JTableSingleResponse("INFORMATION","TestRunConfigurationParent Already Exists!");
				}
	        } catch (Exception e) {
	            jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding record!");
	            log.error("JSON ERROR", e);	            
	        }
	        
        return jTableSingleResponse;
    }
	
	@RequestMapping(value="test.run.parent.update",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse updateParentConfiguration(@ModelAttribute JsonTestRunConfigurationParent jsonTestRunConfigurationParent, BindingResult result) {
		JTableResponse jTableResponse;
		TestRunConfigurationParent testRunConfigurationParentFromUI = null;
		if(result.hasErrors()){
			jTableResponse  = new JTableResponse("ERROR","Invalid form!"); 
		}
		try {

			TestRunConfigurationParent tmp=testRunConfigurationService.getByTestRunConfigurationParentId(jsonTestRunConfigurationParent.getTestRunConfigurationParentId());
			jsonTestRunConfigurationParent.setProductId(tmp.getProductMaster().getProductId());
			jsonTestRunConfigurationParent.setUserId(tmp.getUserList().getUserId());			
			
				testRunConfigurationParentFromUI= jsonTestRunConfigurationParent.getTestRunConfigurationParent();
				if(testRunConfigurationParentFromUI.getTestRunconfigurationName().trim().equals(tmp.getTestRunconfigurationName())){
					List<TestRunConfigurationParent> testRunConfigurationParentList = updateTestRunConfigurationParent(testRunConfigurationParentFromUI);
					jTableResponse = new JTableResponse("OK",testRunConfigurationParentList ,1);
				}
				else{
					if (!(isTestRunConfigurationParentExists(testRunConfigurationParentFromUI.getTestRunconfigurationName().trim()))){
						List<TestRunConfigurationParent> testRunConfigurationParentList = updateTestRunConfigurationParent(testRunConfigurationParentFromUI);
						jTableResponse = new JTableResponse("OK",testRunConfigurationParentList ,1);
					}
					else{
						jTableResponse = new JTableResponse("INFORMATION","TestRunConfigurationParent Already Exists!");
					}
				}
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error updating record!");
	            log.error("JSON ERROR Updating ParentConfiguration : ", e);
	        }
	        
        return jTableResponse;
    }
	@RequestMapping(value="test.run.parent.delete",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse deleteParentConfiguration(@RequestParam int testRunConfigurationParentId) {
		JTableResponse jTableResponse;
        jTableResponse = new JTableResponse("ERROR","Deleting a Test Configuration is disabled currently!");
        return jTableResponse;
   }
	
	
	/*
	 * 'TestRunConfigurationParent should be unique in Edit Screen.'
	 */
	public List<TestRunConfigurationParent> updateTestRunConfigurationParent(TestRunConfigurationParent testRunConfigurationParentFromUI){
		testRunConfigurationService.updateTestRunConfigurationParent(testRunConfigurationParentFromUI);
		List<TestRunConfigurationParent> testRunConfigurationParentList =new ArrayList();
		testRunConfigurationParentList.add(testRunConfigurationParentFromUI);
		return testRunConfigurationParentList;
	}
	
	/*
	 * 'TestRunConfigurationParent should be unique.'
	 */
	public boolean isTestRunConfigurationParentExists(String testRunconfigurationName){
		boolean bResult=false;	
		try{
			bResult= testRunConfigurationService.isTestRunParentExistsByName(testRunconfigurationName);
		}catch(Exception exception){
			log.error("JSON ERROR", exception);
		}
		return bResult;
	}
	
	
	//Child table CRUD operations
	@RequestMapping(value="test.run.child.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listChildConfiguration(@RequestParam int testRunConfigurationParentId) {
		log.debug("inside test.run.child.list");
		JTableResponse jTableResponse;
			try {
			
				List<TestRunConfigurationChild> testRunConfigurationChild=testRunConfigurationService.listTestRunConfigurationChild(
																	testRunConfigurationParentId, TAFConstants.ENTITY_STATUS_ACTIVE);
				List<JsonTestRunConfigurationChild> jsonTestRunConfigurationChild=new ArrayList<JsonTestRunConfigurationChild>();
				for(TestRunConfigurationChild trcc: testRunConfigurationChild){
					jsonTestRunConfigurationChild.add(new JsonTestRunConfigurationChild(trcc));
				}
	            jTableResponse = new JTableResponse("OK", jsonTestRunConfigurationChild,
	            		testRunConfigurationService.getTotalRecordsOfTestRunConfigurationChild(testRunConfigurationParentId));
	            testRunConfigurationChild = null;   
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }
	        
        return jTableResponse;
    }

	@RequestMapping(value="test.run.child.add",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse addChildConfiguration(
    		@ModelAttribute JsonTestRunConfigurationParent jsonTestRunConfigurationParent,
    		@ModelAttribute JsonTestRunConfigurationChild jsonTestRunConfigurationChild,BindingResult result) {
		
		JTableSingleResponse jTableSingleResponse;
		if(result.hasErrors()){
			jTableSingleResponse  = new JTableSingleResponse("ERROR","Invalid form!"); 
		}
		
		boolean isAdhoc=false;
		TestRunConfigurationChild testRunConfigurationChild = null;
		TestRunConfigurationParent testRunConfigurationParent = null;
		try {
				//jsonTestRunConfigurationChild.setTestSuiteId(testSuiteConfigurationService.getByTestSuiteId(testSuitId));
				
				testRunConfigurationChild= jsonTestRunConfigurationChild.getTestRunConfigurationChild();
				testRunConfigurationParent = jsonTestRunConfigurationParent.getTestRunConfigurationParent();
				if(testRunConfigurationChild.getTestRunCronSchedule()==null || testRunConfigurationChild.getTestRunCronSchedule().trim().equals("") ){
					isAdhoc=true;
					String testRunConfigurationName = testRunConfigurationChild.getTestRunConfigurationName()+"_AdHoc";
					String errorMessage = ValidationUtility.validateForNewTestConfigurationChildAddition(testRunConfigurationName, testRunConfigurationService);
					if (errorMessage!= null) {
						jTableSingleResponse = new JTableSingleResponse("ERROR",errorMessage);
						return jTableSingleResponse;
					}
					String configName=((testRunConfigurationChild.getTestRunConfigurationName()==null)?"":testRunConfigurationChild.getTestRunConfigurationName())+"_AdHoc";
					testRunConfigurationChild.setTestRunConfigurationName(configName);
					testRunConfigurationChild.setTestRunScheduledStartTime(null);
					testRunConfigurationChild.setTestRunScheduledEndTime(null);
				}
				if(!isAdhoc){	
					String errorMessage = ValidationUtility.validateForNewTestConfigurationChildAddition(
							testRunConfigurationChild.getTestRunConfigurationName().trim(), testRunConfigurationService);
					String cronErrorMessage = CronValidate.validQuartzCron(testRunConfigurationChild.getTestRunCronSchedule());
					String error = null;
					if (errorMessage != null)
						error = errorMessage + "\n";/*System.lineSeparator();*/
					if (cronErrorMessage != null)
						error = error + cronErrorMessage;
					if (error != null) {
						jTableSingleResponse = new JTableSingleResponse("ERROR",error);
						return jTableSingleResponse;
					}
				}
				testRunConfigurationChild.setStatus(1);
				testRunConfigurationChild.setStatusChangeDate(new Date(System.currentTimeMillis()));			
				testRunConfigurationService.addTestRunConfigurationChild(testRunConfigurationChild);
	            jTableSingleResponse = new JTableSingleResponse("OK",new JsonTestRunConfigurationChild(testRunConfigurationChild));
	            
        } catch (Exception e) {
            jTableSingleResponse = new JTableSingleResponse("ERROR","Unable to create the Test Conguration!");
            log.error("JSON ERROR", e);	 
            return jTableSingleResponse;
        }
	          
	    if(!isAdhoc){
			try {
				
				//Create the schedule Job & Trigger for the configuration : Rajesh
	    		TAFScheduleManager tafScheduleManager = TAFScheduleManager.getTAFScheduleManager();
	    		log.info("inside the !isAdhoc if condition TestConfigchild id:"+testRunConfigurationChild.getTestRunConfigurationChildId());
	   			tafScheduleManager.createTestConfigSchedule(createTestConfigScheduleEntity(testRunConfigurationChild));
	   			
	   		} catch (ATFException e) {
		    			
	            jTableSingleResponse = new JTableSingleResponse("ERROR","Test Configuration Created.Error in CRON Expression.Correct and Resave using EDIT option.");
	            log.error("Quartz Scheduler Error : Unable to create schedule for Test Configuration", e);	 
	            //deleteChildConfiguration(testRunConfigurationChild.getTestRunConfigurationChildId());
	  		} catch (Exception e) {
	  			
	            jTableSingleResponse = new JTableSingleResponse("ERROR","Unable to create schedule for Test Configuration!");
	            log.error("Quartz Scheduler Error : Unable to create schedule for Test Configuration", e);	 
	           // deleteChildConfiguration(testRunConfigurationChild.getTestRunConfigurationChildId());
	  		}
	    }
	        
        return jTableSingleResponse;
    }
	
	/*
	 * Create a ScheduleEntity object from the testconfigchild data object
	 */
	private TestConfigScheduleEntity createTestConfigScheduleEntity(TestRunConfigurationChild testRunConfigurationChild) {
		
		//Fetch all the related objects into the child instance
		testRunConfigurationChild = testRunConfigurationService.getByTestRunConfigurationChildId(testRunConfigurationChild.getTestRunConfigurationChildId());
	
		//Convert the object for creating a new schedule
		TestConfigScheduleEntity entity = new TestConfigScheduleEntity();
		entity.setProductId(testRunConfigurationChild.getProductVersionListMaster().getProductMaster().getProductId().toString());
		entity.setProductName(testRunConfigurationChild.getProductVersionListMaster().getProductMaster().getProductName());
		entity.setProductVersionId(testRunConfigurationChild.getProductVersionListMaster().getProductVersionListId().toString());
		entity.setProductVersionName(testRunConfigurationChild.getProductVersionListMaster().getProductVersionName());
		entity.setTestConfigName(testRunConfigurationChild.getTestRunConfigurationName());
		entity.setTestConfigDescription(testRunConfigurationChild.getDescription());
		entity.setTestConfigId(testRunConfigurationChild.getTestRunConfigurationChildId().toString());
		entity.setScheduleCronExpression(testRunConfigurationChild.getTestRunCronSchedule());
		entity.setJobClassName("com.hcl.atf.taf.scheduler.jobs.DefaultTestConfigCycleJob");
		entity.setStartDate(testRunConfigurationChild.getTestRunScheduledStartTime());
		entity.setEndDate(testRunConfigurationChild.getTestRunScheduledEndTime());
		
		return entity;
	}

	
	/*
	 * Create a ScheduleEntity object from the testconfigchild data object
	 */

	@RequestMapping(value="test.run.child.update",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse updateChildConfiguration(@ModelAttribute JsonTestRunConfigurationChild jsonTestRunConfigurationChild, BindingResult result) {
		JTableResponse jTableResponse;
		if(result.hasErrors()){
			jTableResponse  = new JTableResponse("ERROR","Invalid form!"); 
		}
		
		try {

			//UseId and productId should not allowed to be changed
			//Override the json entity values with the exiting DB entity values 
			TestRunConfigurationChild old=testRunConfigurationService.getByTestRunConfigurationChildId(jsonTestRunConfigurationChild.getTestRunConfigurationChildId());
			jsonTestRunConfigurationChild.setProductVersionListId(old.getProductVersionListMaster().getProductVersionListId());
			jsonTestRunConfigurationChild.setTestRunConfigurationName(old.getTestRunConfigurationName());
			jsonTestRunConfigurationChild.setDescription(old.getDescription());
			TestRunConfigurationChild testRunConfigurationChild= jsonTestRunConfigurationChild.getTestRunConfigurationChild();
			if (old.getTestSuiteList().getTestSuiteId() == (jsonTestRunConfigurationChild.getTestSuiteId())) {
				testRunConfigurationChild.setTestSuiteList(old.getTestSuiteList());
			} else {
				TestSuiteList testSuite = testSuiteConfigurationService.getByTestSuiteId(jsonTestRunConfigurationChild.getTestSuiteId());
				testRunConfigurationChild.setTestSuiteList(testSuite);
			}
			if (testRunConfigurationChild.getStatus() == null)
				testRunConfigurationChild.setStatus(TAFConstants.ENTITY_STATUS_ACTIVE);
			testRunConfigurationService.updateTestRunConfigurationChild(testRunConfigurationChild);
			List<JsonTestRunConfigurationChild> tmpList =new ArrayList();
			tmpList.add(new JsonTestRunConfigurationChild(testRunConfigurationChild));

			if (isScheduleChanged(old, testRunConfigurationChild)) {
				if(testRunConfigurationChild.getTestRunCronSchedule()==null || testRunConfigurationChild.getTestRunCronSchedule().trim().equals("") ){
					try {
						TAFScheduleManager tafScheduleManager = TAFScheduleManager.getTAFScheduleManager();
						tafScheduleManager.deleteTestConfigSchedule(createTestConfigScheduleEntity(testRunConfigurationChild));
					} catch (ATFException e) {
	    			
						log.error("Quartz Scheduler Error. Unable to update Job & Trigger for the Test Configuration", e);
					}
					
				} else {					
					
					String cronErrorMessage = CronValidate.validQuartzCron(testRunConfigurationChild.getTestRunCronSchedule());
					String error = null;
					if (cronErrorMessage != null)
						error = cronErrorMessage;
					if (error != null) {
						jTableResponse = new JTableResponse("ERROR",error);
						return jTableResponse;
					}
					TAFScheduleManager tafScheduleManager = TAFScheduleManager.getTAFScheduleManager();
					try {
						tafScheduleManager.updateTestConfigSchedule(createTestConfigScheduleEntity(testRunConfigurationChild));
					} catch (ATFException e) {
    			
						log.error("Quartz Scheduler Error. Unable to update Job & Trigger for the Test Configuration", e);
						jTableResponse = new JTableResponse("ERROR","Quartz Scheduler Error. Unable to update Job & Trigger for the Test Configuration");
						return jTableResponse;
					}
				}
			}
		
	    	jTableResponse = new JTableResponse("OK",tmpList ,1);
	    } catch (Exception e) {
	        jTableResponse = new JTableResponse("ERROR","Error updating Test Configuration!");
	        log.error("JSON ERROR", e);	   
	    }
        return jTableResponse;
    }
	private boolean isScheduleChanged(TestRunConfigurationChild oldChild, TestRunConfigurationChild newChild) {
		
		boolean hasChanged = false;
		String oldCron = oldChild.getTestRunCronSchedule(); 
		String newCron = newChild.getTestRunCronSchedule();
		if (!(oldCron == null && newCron == null)) {
			if (oldCron == null || newCron == null) {
				hasChanged =  true;
				return hasChanged;
			} else if (!oldCron.trim().equals(newCron.trim())) {
				hasChanged = true;
				return hasChanged;
			}
		}
		
		Date oldStartTime = oldChild.getTestRunScheduledStartTime();
		Date newStartTime = newChild.getTestRunScheduledStartTime();
		if (!(oldStartTime == null && newStartTime == null)) {
			if (oldStartTime == null || newStartTime == null) {
				hasChanged =  true;
				return hasChanged;
			} else if (!oldStartTime.equals(newStartTime)) {
				hasChanged = true;
				return hasChanged;
			}
		}

		Date oldEndTime = oldChild.getTestRunScheduledEndTime();
		Date newEndTime = newChild.getTestRunScheduledEndTime();
		if (!(oldEndTime == null && newEndTime == null)) {
			if (oldEndTime == null || newEndTime == null) {
				hasChanged =  true;
				return hasChanged;
			} else if (!oldEndTime.equals(newEndTime)) {
				hasChanged = true;
				return hasChanged;
			}
		}
		return hasChanged;
	}//Update the schedule Job & Trigger for the configuration : Rajesh

	
	@RequestMapping(value="test.run.child.delete",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse deleteChildConfiguration(@RequestParam int testRunConfigurationChildId) {
		JTableResponse jTableResponse;
		boolean bAccessService=false;

		try {

			//Delete the schedule Job & Trigger for the configuration : Rajesh
			TestRunConfigurationChild old=testRunConfigurationService.getByTestRunConfigurationChildId(testRunConfigurationChildId);
			//Update the schedule Job & Trigger for the configuration : Rajesh
			if (old.getTestRunCronSchedule() == null || old.getTestRunCronSchedule().trim().equals("")) {
				bAccessService=true;
			} else {	
				TAFScheduleManager tafScheduleManager = TAFScheduleManager.getTAFScheduleManager();
				try {
					tafScheduleManager.deleteTestConfigSchedule(createTestConfigScheduleEntity(old));
				} catch (ATFException e) {
					log.error("Quartz Scheduler Error. Unable to delete Job & Trigger for the Test Configuration", e);
				}
				bAccessService=true;
			}
			jTableResponse = new JTableResponse("OK");
			if (bAccessService){
				testRunConfigurationService.deleteTestRunConfigurationChild(testRunConfigurationChildId);
			}
	    } catch (Exception e) {
	        jTableResponse = new JTableResponse("ERROR","Error deleting record!");
	        log.error("JSON ERROR", e);
	    }
        return jTableResponse;
    }
	
	// all device list - testRunSelectedDeviceList and the devices through Test Environment Devices
	@RequestMapping(value="test.run.child.alldevices.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listChildDeviceAndTEDDeviceConfiguration(@RequestParam int testRunConfigurationChildId) {
		log.debug("inside test.run.child.alldevice.list");
		JTableResponseOptions jTableResponseOptions;
		try {
			List<DeviceList> allDeviceList=testExecutionService.getTotalDevicesInTestRunConfigurationChild(testRunConfigurationChildId);
			// Now totalDeviceList contains the list of devices attached normally to a runconfig and list of devices attached via
			// Test Environment devices. This list contains unique devices alone
			List<JsonDeviceList> jsonAllDeviceList=new ArrayList<JsonDeviceList>();

			for(DeviceList singleDevice: allDeviceList){
				jsonAllDeviceList.add(new JsonDeviceList(singleDevice));
			}
			jTableResponseOptions = new JTableResponseOptions("OK", jsonAllDeviceList);
            allDeviceList = null;
        } catch (Exception e) {
        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
            log.error("JSON ERROR fetching ChildDevice And TEDDeviceConfiguration", e);
        }
        return jTableResponseOptions;
    }
	
	
	// device list
	@RequestMapping(value="test.run.child.device.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listChildDeviceConfiguration(@RequestParam int testRunConfigurationChildId) {
		log.debug("inside test.run.child.device.list");
		JTableResponse jTableResponse;
			try {
			
			List<TestRunSelectedDeviceList> testRunSelectedDeviceList=testRunConfigurationService.listTestRunConfigurationChildDevice(testRunConfigurationChildId);
			List<JsonTestRunSelectedDeviceList> jsonTestRunSelectedDeviceList=new ArrayList<JsonTestRunSelectedDeviceList>();
			for(TestRunSelectedDeviceList tsdl: testRunSelectedDeviceList){
				jsonTestRunSelectedDeviceList.add(new JsonTestRunSelectedDeviceList(tsdl));
				
			}
	 
	            jTableResponse = new JTableResponse("OK", jsonTestRunSelectedDeviceList,jsonTestRunSelectedDeviceList.size());
	            testRunSelectedDeviceList = null;
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR fetching ChildDeviceConfiguration : ", e);
	        }
	        
        return jTableResponse;
    }
	
	@RequestMapping(value="test.run.child.device.add",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse addChildConfigurationDevices(
    		@ModelAttribute JsonTestRunSelectedDeviceList jsonTestRunSelectedDeviceList, BindingResult result) {

		JTableSingleResponse jTableSingleResponse;
		if(result.hasErrors()){
			jTableSingleResponse = new JTableSingleResponse("ERROR","Invalid form!"); 
		}
		
		try {
			TestRunSelectedDeviceList testRunSelectedDeviceList= jsonTestRunSelectedDeviceList.getTestRunSelectedDeviceList();
			int testRunConfigurationChildId=jsonTestRunSelectedDeviceList.getTestRunConfigurationChildId();
			if(isDevicesExists(testRunSelectedDeviceList,testRunConfigurationChildId)==false)
			{
				testRunSelectedDeviceList= jsonTestRunSelectedDeviceList.getTestRunSelectedDeviceList();
				testRunConfigurationService.addTestRunConfigurationChildDevice(testRunSelectedDeviceList);
				
				jsonTestRunSelectedDeviceList.setSelectedDeviceListId(testRunSelectedDeviceList.getSelectedDeviceListId());
				jTableSingleResponse = new JTableSingleResponse("OK",jsonTestRunSelectedDeviceList);
			}
			else
			{
				jTableSingleResponse = new JTableSingleResponse("INFORMATION","Device Already Exists!");
			}
	        } catch (Exception e) {
	            jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding ChildConfigurationDevices record!");
	            log.error("JSON ERROR adding ChildConfigurationDevices :", e);	 
	        }
	        
        return jTableSingleResponse;
    }
	public boolean isDevicesExists(TestRunSelectedDeviceList testRunSelectedDeviceList,int testRunConfigurationChildId){
		boolean bResult=false;
		List<TestRunSelectedDeviceList> tmpList = new ArrayList();
		try{
			tmpList=(ArrayList) testRunConfigurationService.listTestConfigurationChildDevices(testRunConfigurationChildId);
			for(TestRunSelectedDeviceList tedhd: tmpList){
				if (testRunSelectedDeviceList.getDeviceList().getDeviceListId().toString().equals(tedhd.getDeviceList().getDeviceListId().toString())){
					bResult=true;
				}
			}
		}catch(Exception exception){
			log.error("JSON ERROR", exception);
		}
		return bResult;
	}
	
	
	@RequestMapping(value="test.run.child.device.delete",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse deleteChildConfigurationDevice(@RequestParam int selectedDeviceListId) {
		JTableResponse jTableResponse;
		try {
	            testRunConfigurationService.deleteTestRunConfigurationChildDevice(selectedDeviceListId);
	            jTableResponse = new JTableResponse("OK");
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error deleting record!");
	            log.error("JSON ERROR deleting ChildConfigurationDevice : ", e);
	        }
        return jTableResponse;
    }
	
	/**
	 * Listing the TestEnviornments from the testRunConfigurationChild
	 */
	
	@RequestMapping(value="test.run.child.testenviornment.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listTestRunTestEviornments(@RequestParam int testRunConfigurationChildId) {
		log.debug("inside test.run.child.Enviornment.list");
		JTableResponse jTableResponse;
		List<TestEnvironmentDevices> testEnviornmentDevices = null;
			try {
				testEnviornmentDevices = testRunConfigurationService.listAllTestEnviornments(testRunConfigurationChildId);
				List<JsonTestEnvironmentDevices> jsonTestEnvironmentDevices = new ArrayList<JsonTestEnvironmentDevices>(); 
				for(TestEnvironmentDevices Ted :testEnviornmentDevices)
				{
					jsonTestEnvironmentDevices.add(new JsonTestEnvironmentDevices(Ted));
				}
					jTableResponse = new JTableResponse("OK", jsonTestEnvironmentDevices,jsonTestEnvironmentDevices.size());
	            
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR listing TestRunTestEviornments :", e);
	        }
	        
        return jTableResponse;
    }
	
	/**
	 * Adding the TestEnviornments to testRunConfigurationChild
	 */
	
	@RequestMapping(value="test.run.child.testenviornment.add",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse addChildTestEnviornments(
    		@ModelAttribute JsonTestRunConfigurationChild jsonTestRunConfigurationChild,
    		@ModelAttribute JsonTestEnvironmentDevices jsonTestEnvironmentDevices,BindingResult result) {
		
		JTableSingleResponse jTableSingleResponse=null;
		TestEnvironmentDevices testEnviromentDevices = null;
		Set<DeviceList> environmentDeviceList=null;
		if(result.hasErrors()){
			jTableSingleResponse = new JTableSingleResponse("ERROR","Invalid form!"); 
		}
		
		try {
			TestEnvironmentDevices testEnvironmentDevices = jsonTestEnvironmentDevices.getTestEnvironmentDevices();
			int testEnviornmentId=Integer.parseInt(jsonTestEnvironmentDevices.getName());
			if(isTestEnviornmentsExists(jsonTestRunConfigurationChild,testEnviornmentId)==false)
			{
				testRunConfigurationService.addTestRunConfigurationChildTestEnviornment(jsonTestRunConfigurationChild.getTestRunConfigurationChildId(),testEnviornmentId);
				jTableSingleResponse = new JTableSingleResponse("OK",new JsonTestEnvironmentDevices(testEnvironmentDevices));
			}
			else
			{
				jTableSingleResponse = new JTableSingleResponse("INFORMATION","TestEnvironment Already Exists!");
			}
		}
			catch (Exception e) {
	            jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding record!");
	            log.error("JSON ERROR adding ChildTestEnviornments : ", e);	 
	        }        
        return jTableSingleResponse;
    }
	
	/**
	 * Deleting the TestEnviornments from testRunConfigurationChild
	 */
	
	@RequestMapping(value="test.run.child.testenviornment.delete",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse deleteChildTestEnviornments(@RequestParam int testRunConfigurationChildId,@RequestParam int testEnvironmentDevicesId) {
		JTableResponse jTableResponse;
		log.debug("in test.run.child.testenviornment.delete:");
		try {
				testRunConfigurationService.deleteTestEnviornmentDevicesFromTestRunConfigChild(testRunConfigurationChildId,testEnvironmentDevicesId);
	            jTableResponse = new JTableResponse("OK");
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error deleting record!");
	            log.error("JSON ERROR deleting ChildTestEnviornments", e);
	        }
	        
        return jTableResponse;
    }
	
	/**
	 * Test Enviornments should be unique.
	 */
	
	public boolean isTestEnviornmentsExists(JsonTestRunConfigurationChild jsonTestRunConfigurationChild,int testEnviornmentId){
		boolean bResult=false;
		List<TestEnvironmentDevices> testEnviornmentDeviceList=null;
		String testEnvId=Integer.toString(testEnviornmentId);
		try{
			
			testEnviornmentDeviceList=(ArrayList) testRunConfigurationService.listAllTestEnviornments(jsonTestRunConfigurationChild.getTestRunConfigurationChildId());
			for(TestEnvironmentDevices tedhd: testEnviornmentDeviceList){
				if (testEnvId.equals(tedhd.getTestEnvironmentDevicesId().toString())){
					bResult=true;
				}
			}
		}catch(Exception exception){
			log.error("JSON ERROR", exception);
		}
		return bResult;
	}
	
	@RequestMapping(value="test.status.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listRunList(@RequestParam int jtStartIndex, @RequestParam int jtPageSize) {
		log.debug("inside test.run.status.list");
		JTableResponse jTableResponse;
		try {
			
			List<TestRunList> testRunList = testExecutionService.listAllTestRunList(jtStartIndex, jtPageSize);
			List<JsonTestRunList> jsonTestRunLists=new ArrayList<JsonTestRunList>();
			for(TestRunList trl: testRunList){
				JsonTestRunList jsonTestRunList = new JsonTestRunList(trl);
				jsonTestRunList.setAverageTestRunExecutionTime(testExecutionService.getAverageTestRunExecutionTime(trl));
				jsonTestRunLists.add(jsonTestRunList);
			}
			testRunList.clear();testRunList=null;
			
	        jTableResponse = new JTableResponse("OK", jsonTestRunLists,testExecutionService.getTotalRecordsOfTestRunList());
	        testRunList = null;
	    } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }
	        
        return jTableResponse;
    }


    public @ResponseBody JTableResponse listUpcomingTestRuns(@RequestParam int jtStartIndex, @RequestParam int jtPageSize) {
		log.debug("inside test.run.status.list");
		JTableResponse jTableResponse;
		try {
			
			TAFScheduleManager tafScheduleManager = TAFScheduleManager.getTAFScheduleManager();
			List<JsonTestRunConfigurationJob> jsonTestConfigurationJobList = new ArrayList<JsonTestRunConfigurationJob>();

			//TODO : Get the list of products authorized to the user
			Map<String, Map<String, List<Date>>> upComingTestRuns = tafScheduleManager.getUpcomingFireTimes(null, null, null);
			Collection<Map<String, List<Date>>> c = upComingTestRuns.values();

			for ( Map<String, List<Date>> configurationRunsMap : c) {
				
				Set<String> testConfigurationNames = configurationRunsMap.keySet();
				for (String testConfigurationName : testConfigurationNames) {
					
					//TODO : Get the test configuration object using the test configuration name
					TestRunConfigurationChild testRunConfigurationChild = testRunConfigurationService.getByTestRunConfigurationChildName(testConfigurationName);
					//Iterate through the scheduled times and create the UpComing jobs for the times
					List<Date> scheduleTimes = configurationRunsMap.get(testConfigurationName);
					for (Date timeOfExecution : scheduleTimes) {
						
						JsonTestRunConfigurationJob jsonTestConfigurationJob = new JsonTestRunConfigurationJob(testRunConfigurationChild, timeOfExecution);
						jsonTestConfigurationJobList.add(jsonTestConfigurationJob);
					}
				}
			}
	        jTableResponse = new JTableResponse("OK", jsonTestConfigurationJobList, jsonTestConfigurationJobList.size());
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching Upcoming Test Runs!");
	            log.error("JSon : Error fetching Upcoming Test Runs", e);
	        }
        return jTableResponse;
    }

    
    @ExceptionHandler(Exception.class)
	public void handleException(Throwable ex, HttpServletResponse response)
			throws Exception {

		response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
				ex.getMessage());
		ex.printStackTrace();
	}
    
	//Trcc Execution Plan Methods - Start
	
	@RequestMapping(value="trcc.execution.plan.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listTrccExecutionPlan(@RequestParam int testRunConfigurationChildId) {
		log.debug("inside trcc.execution.plan.list");
		JTableResponse jTableResponse;
			try {
				List<TrccExecutionPlan> trccExecutionPlans=testExecutionService.listTrccExecutionPlan(testRunConfigurationChildId);
				List<JsonTrccExecutionPlan> jsonTrccExecutionPlan=new ArrayList<JsonTrccExecutionPlan>();
				for(TrccExecutionPlan trccExecutionPlan: trccExecutionPlans){
					jsonTrccExecutionPlan.add(new JsonTrccExecutionPlan(trccExecutionPlan));
				}
	            jTableResponse = new JTableResponse("OK", jsonTrccExecutionPlan,testExecutionService.getTotalRecordsOfTrccExecutionPlan(testRunConfigurationChildId));
	            trccExecutionPlans = null;   
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }
	        
        return jTableResponse;
    }
	
	/**
	 * Adding the TrccExecutionPlan to testRunConfigurationChild
	 */
	
	@RequestMapping(value="trcc.execution.plan.add",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse addTrccExecutionPlan(@ModelAttribute JsonTestRunConfigurationChild jsonTestRunConfigurationChild,
    														@ModelAttribute JsonTrccExecutionPlan jsonTrccExecutionPlan,BindingResult result) {
		JTableSingleResponse jTableSingleResponse=null;
		String Status="";
		if(result.hasErrors()){
			jTableSingleResponse = new JTableSingleResponse("ERROR","Invalid form!"); 
		}
		try {
			TrccExecutionPlan trccExecutionPlanFromUI = jsonTrccExecutionPlan.getTrccExecutionPlan();
			TrccExecutionPlan trccExecutionPlanFromDB = testExecutionService.getTrccExecutionPlanByName(trccExecutionPlanFromUI.getPlanName().trim());
			if(trccExecutionPlanFromDB==null){
				log.info("No TrccExecutionPlan found with the given Name:");
			}
			else{
				int status= trccExecutionPlanFromDB.getStatus();
				if(status==0)
					Status = "InActive";
				else
					Status = "Active";
			}
			String trccExecutionPlanName=jsonTrccExecutionPlan.getPlanName();
			if(!(isTrccExecutionPlanExists(jsonTestRunConfigurationChild,trccExecutionPlanName.trim())))
			{
				testExecutionService.addTrccExecutionPlan(trccExecutionPlanFromUI);
				jTableSingleResponse = new JTableSingleResponse("OK",new JsonTrccExecutionPlan(trccExecutionPlanFromUI));
			}
			else
			{
				jTableSingleResponse = new JTableSingleResponse("INFORMATION",Status+"-Test ExecutionPlan Already Exists!");
			}
		}
			catch (Exception e) {
	            jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding record!");
	            log.error("JSON ERROR adding TrccExecutionPlan : ", e);	 
	        }        
        return jTableSingleResponse;
    }
	
	/**
	 * 'TrccExecutionPlan Update Screen.'
	 */
	public List<TrccExecutionPlan> updateTrccExecutionPlan(TrccExecutionPlan trccExecutionPlanFromUI){
		
		testExecutionService.updateTrccExecutionPlan(trccExecutionPlanFromUI);
		List<TrccExecutionPlan> trccExecutionPlans =new ArrayList<TrccExecutionPlan>();
		trccExecutionPlans.add(trccExecutionPlanFromUI);
		return trccExecutionPlans;
	}
	
	@RequestMapping(value="trcc.execution.plan.update",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse updateTrccExecutionPlan(@ModelAttribute JsonTrccExecutionPlan jsonTrccExecutionPlan, BindingResult result) {
		JTableResponse jTableResponse = null;
		TrccExecutionPlan trccExecutionPlanFromUI = null;
		TrccExecutionPlan trccExecutionPlanFromDB = null;
		String Status = null;
		if(result.hasErrors()){
			jTableResponse = new JTableResponse("ERROR","Invalid Form!"); 
		}
		try {
			trccExecutionPlanFromUI = jsonTrccExecutionPlan.getTrccExecutionPlan();
			trccExecutionPlanFromDB = testExecutionService.getTrccExecutionPlanById(trccExecutionPlanFromUI.getTrccExecutionPlanId());
		
			if(trccExecutionPlanFromDB==null){
				log.info("No TrccExecutionPlan found with the given Name:");
			}
			else{
				int status= trccExecutionPlanFromDB.getStatus();
				if(status==0)
					Status = "InActive";
				else
					Status = "Active";
			if(trccExecutionPlanFromUI.getPlanName().trim().equals(trccExecutionPlanFromDB.getPlanName())){
				List<TrccExecutionPlan> trccExecutionPlanList = updateTrccExecutionPlan(trccExecutionPlanFromUI);
				jTableResponse = new JTableResponse("OK",trccExecutionPlanList,1);
			}
			else {
				if((!isTrccExecutionPlanExists(trccExecutionPlanFromUI)))
				{
					List<TrccExecutionPlan> trccExecutionPlanList = updateTrccExecutionPlan(trccExecutionPlanFromUI);
					jTableResponse = new JTableResponse("OK",trccExecutionPlanList,1);
				}
				else
				{
					jTableResponse = new JTableResponse("INFORMATION",Status+"-TestExecutionPlan Already Exists!");
				}
			}
		}
		}catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error updating record!");
	            log.error("JSON ERROR", e);
	        }
        return jTableResponse;
    }
	
	@RequestMapping(value="trcc.execution.plan.delete",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse deleteTrccExecutionPlan(@RequestParam int trccExecutionPlanId) {
		JTableResponse jTableResponse;
		log.debug("in trcc.execution.plan.delete:");
		try {
				testExecutionService.deleteTrccExecutionPlan(trccExecutionPlanId);
	            jTableResponse = new JTableResponse("OK");
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error deleting record!");
	            log.error("JSON ERROR deleting TrccExecutionPlan : ", e);
	        }
	        
        return jTableResponse;
    }
	
	/**
	 * Trcc Execution Plan should be unique.
	 */
	
	public boolean isTrccExecutionPlanExists(JsonTestRunConfigurationChild jsonTestRunConfigurationChild,String trccExecutionPlanName){
		boolean bResult=false;
		List<TrccExecutionPlan> trccExecutionPlanList=null;
		try{
			
			trccExecutionPlanList=(ArrayList) testExecutionService.listTrccExecutionPlan(jsonTestRunConfigurationChild.getTestRunConfigurationChildId());
			for(TrccExecutionPlan trccExecutionPlan: trccExecutionPlanList){
				if (trccExecutionPlanName.trim().equals(trccExecutionPlan.getPlanName().trim())){
					bResult=true;
				}
			}
		}catch(Exception exception){
			log.error("JSON ERROR", exception);
		}
		return bResult;
	}
	
	public boolean isTrccExecutionPlanExists(TrccExecutionPlan trccExecutionPlan){
		boolean bResult=false;
		TrccExecutionPlan trccExecutionPlanFromDB=null;
		try{
			trccExecutionPlanFromDB=testExecutionService.getTrccExecutionPlanByName(trccExecutionPlan.getPlanName());
		}catch(Exception exception){
			log.error("JSON ERROR", exception);
		}
		return bResult;
	}
	
    //Trcc Execution Plan Details Methods - Start
	
	@RequestMapping(value="trcc.execution.plan.details.loadExistingTestCases",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions loadExistingTestCasesInPlanDetails(@RequestParam int trccExecutionPlanId, @RequestParam int deviceListId) {
		log.debug("trcc.execution.plan.details.loadExistingTestCases");
		JTableResponseOptions jTableResponseOptions;
			try {
				List<TrccExecutionPlanDetails> trccExecutionPlanDetails=testExecutionService.listTrccExecutionPlanDetails(trccExecutionPlanId, deviceListId);
				List<JsonTrccExecutionPlanDetails> jsonTrccExecutionPlanDetails=new ArrayList<JsonTrccExecutionPlanDetails>();
				for(TrccExecutionPlanDetails trccExecutionPlanDetail: trccExecutionPlanDetails){
					jsonTrccExecutionPlanDetails.add(new JsonTrccExecutionPlanDetails(trccExecutionPlanDetail));
				}
				jTableResponseOptions = new JTableResponseOptions("OK", jsonTrccExecutionPlanDetails);
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }
	        
        return jTableResponseOptions;
    }

	@RequestMapping(value="trcc.execution.plan.details.add",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse addTrccExecutionPlanDetails(
    		@RequestParam Integer trccExecutionPlanId, @RequestParam int deviceListId, @RequestParam String testCaseListsFromUI) {
		log.debug("inside the trcc.execution.plan.details.add:");
		JTableSingleResponse jTableSingleResponse = null;

		try {
	    	String[] testCaseLists = testCaseListsFromUI.split(",");
	       	testExecutionService.addTrccExecutionPlanDetail(trccExecutionPlanId, deviceListId, testCaseLists);		
 	    	jTableSingleResponse = new JTableSingleResponse("OK");
		}
    	catch (Exception e) {
            jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding record!");
            log.error("JSON ERROR", e);	            
        }
	       
        return jTableSingleResponse;
    }
	
	@RequestMapping(value="trcc.execution.plan.details.update",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse updateTrccExecutionPlanDetails(@ModelAttribute JsonTrccExecutionPlanDetails jsonTrccExecutionPlanDetail,@RequestParam Integer trccExecutionPlanId, @RequestParam int deviceListId) {
		log.debug("inside the trcc.execution.plan.details.update:");
		JTableResponse jTableResponse = null;

		try {
			TrccExecutionPlanDetails trccExecutionPlanDetail = jsonTrccExecutionPlanDetail.getTrccExecutionPlanDetails();
			if(trccExecutionPlanId==0){
				jTableResponse = new JTableResponse("INFORMATION","PLEASE SELECT TRCCEXECUTION PLAN!");
			}
			else if(deviceListId==0){
				jTableResponse = new JTableResponse("INFORMATION","PLEASE SELECT DEVICE PLAN!");
			}
			else{
				testExecutionService.updateTrccExecutionPlanDetail(trccExecutionPlanDetail,trccExecutionPlanId,deviceListId);
	 	    	jTableResponse = new JTableResponse("OK");
			}
		}
    	catch (Exception e) {
            jTableResponse = new JTableResponse("ERROR","Error adding record!");
            log.error("JSON ERROR", e);	            
        }
	       
        return jTableResponse;
    }
	
	//Selective Test Cases - Execution Methods - Start
	
	@RequestMapping(value="trcc.execution.plan.ondevice.saveandexecute",method=RequestMethod.POST, produces="application/json")
    public @ResponseBody JTableSingleResponse saveAndExecuteTestRunPlanOnADevice(
    		@RequestParam int testRunConfigurationChildId, @RequestParam int trccExecutionPlanId, @RequestParam  int deviceListId, 
    		@RequestParam String testCaseListsFromUI, HttpServletRequest request,HttpServletResponse response) {
		
		log.info("Inside trcc.execution.plan.ondevice.saveandexecute");
		JTableSingleResponse jTableSingleResponse = null;

		try {
	    	String[] testCaseLists = testCaseListsFromUI.split(",");
	    	log.info("testCaseList[0]="+testCaseLists[0]);
       		testExecutionService.addTrccExecutionPlanDetail(trccExecutionPlanId, deviceListId, testCaseLists);		
       		jTableSingleResponse = new JTableSingleResponse("OK");
		}
    	catch (Exception e) {
            jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding record!");
            log.error("JSON ERROR", e);	     
            return jTableSingleResponse;
        }
		
		ProductMaster product = null;
		try {				
				
				TestRunConfigurationChild testRunChildConfig = testRunConfigurationService.getByTestRunConfigurationChildId(testRunConfigurationChildId);
				
				//Scripts Checkout from Test Management System
				
				product = testRunChildConfig.getProductVersionListMaster().getProductMaster();
				TestRunExecutionStatusVO statusVO = testExecutionService.executeTestRunPlanOnADevice(testRunConfigurationChildId, trccExecutionPlanId, deviceListId);
				
				if (statusVO.hasExecutedSuccessfully) {
					jTableSingleResponse = new JTableSingleResponse("OK",new JsonTestRunList(statusVO.testRunList));				
				} else {
					jTableSingleResponse = new JTableSingleResponse("ERROR",statusVO.executionMessage);				
				}
	        } catch (Exception e) {
	        	jTableSingleResponse = new JTableSingleResponse("ERROR", "Some unknown problem in executing Test Run Plan On a Device");
	            log.error("JSON ERROR", e);	            
	        }
        return jTableSingleResponse;
    }
	
	@RequestMapping(value="trcc.execution.plan.ondevice.execute",method=RequestMethod.POST,produces="application/json")
    public @ResponseBody JTableSingleResponse executeTestRunPlanOnADevice(@RequestParam int testRunConfigurationChildId,
    		@RequestParam int trccExecutionPlanId, @RequestParam  int deviceListId, HttpServletRequest request,HttpServletResponse response) {
		JTableSingleResponse jTableSingleResponse;
		ProductMaster product = null;
		try {				
				log.info("Inside trcc.execution.plan.ondevice.execute");
				TestRunConfigurationChild testRunChildConfig = testRunConfigurationService.getByTestRunConfigurationChildId(testRunConfigurationChildId);
				
				//Scripts Checkout from Test Management System
				
				product = testRunChildConfig.getProductVersionListMaster().getProductMaster();
				
				TestRunExecutionStatusVO statusVO = testExecutionService.executeTestRunPlanOnADevice(testRunConfigurationChildId, trccExecutionPlanId, deviceListId);
				
				if (statusVO.hasExecutedSuccessfully) {
					jTableSingleResponse = new JTableSingleResponse("OK",new JsonTestRunList(statusVO.testRunList));				
				} else {
					jTableSingleResponse = new JTableSingleResponse("ERROR",statusVO.executionMessage);				
				}
	        } catch (Exception e) {
	        	jTableSingleResponse = new JTableSingleResponse("ERROR", "Some unknown problem in executing Test Run Plan on a Device");
	            log.error("JSON ERROR", e);	            
	        }
        return jTableSingleResponse;
    }

	@RequestMapping(value="trcc.execution.plan.execute",method=RequestMethod.POST,produces="application/json")
    public @ResponseBody JTableSingleResponse executeTestRunPlan(@RequestParam int testRunConfigurationChildId,
    		@RequestParam int trccExecutionPlanId, HttpServletRequest request,HttpServletResponse response) {
		JTableSingleResponse jTableSingleResponse;
		ProductMaster product = null;
		try {				
				log.info("Inside trcc.execution.plan.execute");
				TestRunConfigurationChild testRunChildConfig = testRunConfigurationService.getByTestRunConfigurationChildId(testRunConfigurationChildId);
				
				//Scripts Checkout from Test Management System
				
				product = testRunChildConfig.getProductVersionListMaster().getProductMaster();
				TestRunExecutionStatusVO statusVO = testExecutionService.executeTestRunPlan(testRunConfigurationChildId, trccExecutionPlanId);
				
				if (statusVO.hasExecutedSuccessfully) {
					jTableSingleResponse = new JTableSingleResponse("OK",new JsonTestRunList(statusVO.testRunList));				
				} else {
					jTableSingleResponse = new JTableSingleResponse("ERROR",statusVO.executionMessage);				
				}
	        } catch (Exception e) {
	        	jTableSingleResponse = new JTableSingleResponse("ERROR", "Some unknown problem in executing Test Run Plan");
	            log.error("JSON ERROR", e);	            
	        }
        return jTableSingleResponse;
    }

	@RequestMapping(value="trcc.execution.plans.execute",method=RequestMethod.POST,produces="application/json")
    public @ResponseBody JTableSingleResponse executeTestRunPlans(@RequestParam int testRunConfigurationChildId,
    		HttpServletRequest request,HttpServletResponse response) {
		JTableSingleResponse jTableSingleResponse;
		ProductMaster product = null;
		try {				
				log.info("Inside trcc.execution.plans.execute");
				TestRunConfigurationChild testRunChildConfig = testRunConfigurationService.getByTestRunConfigurationChildId(testRunConfigurationChildId);
				
				//Scripts Checkout from Test Management System
				
				product = testRunChildConfig.getProductVersionListMaster().getProductMaster();
				List<TestRunExecutionStatusVO> statusVO = testExecutionService.executeTestRunPlans(testRunConfigurationChildId);
				
				if (statusVO == null || statusVO.isEmpty()) {
					jTableSingleResponse = new JTableSingleResponse("ERROR","Not able to execute Test Run Plans. Check the system log");				
				} else {
				
					jTableSingleResponse = new JTableSingleResponse("OK",new JsonTestRunList(statusVO.get(0).testRunList));				
				} 
	        } catch (Exception e) {
	        	jTableSingleResponse = new JTableSingleResponse("ERROR", "Some unknown problem in executing Test Run Plans");
	            log.error("JSON ERROR", e);	            
	        }
        return jTableSingleResponse;
    }

	
	// Moved from TerminalClientController to TestRunConfigurationController
	@RequestMapping(value="test.run.parent.execute",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse executeTestRuns(@RequestParam int testRunConfigurationParentId,  HttpServletRequest request) {

		JTableSingleResponse jTSingleResponse;
		try {				
				//Checkout scripts from external source
				toolsController.checkOutScriptsForParentConfig(testRunConfigurationParentId, request);
				
				List<TestRunExecutionStatusVO> statusVO = testExecutionService.executeTestRuns(testRunConfigurationParentId, request);
				
				if (statusVO == null || statusVO.isEmpty()) {
					jTSingleResponse = new JTableSingleResponse("ERROR","Not able to execute Test Runs. Check the system log");				
				} else {
				
					jTSingleResponse = new JTableSingleResponse("OK",new JsonTestRunList(statusVO.get(0).testRunList));				
				} 
	        } catch (Exception e) {
	        	jTSingleResponse = new JTableSingleResponse("ERROR", "Some unknown problem in executing Test Runs");
	            log.error("JSON ERROR", e);	            
	        }
        return jTSingleResponse;
    }
	
	// Moved from TerminalClientController to TestRunConfigurationController
	@RequestMapping(value="test.run.child.execute",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse executeTestRun(@RequestParam int testRunConfigurationChildId, HttpServletRequest request) {
		JTableSingleResponse jTSingleResponse;
		ProductMaster product = null;
		ConnectorHPQC hpqcConnector = null;
		log.info("test.run.child.execute");
		try {				
				TestRunConfigurationChild testRunChildConfig = testRunConfigurationService.getByTestRunConfigurationChildId(testRunConfigurationChildId);
				product = testRunChildConfig.getProductVersionListMaster().getProductMaster();
				Set<TestRunList> testRunLists = testRunChildConfig.getTestRunLists();
				TestRunExecutionStatusVO statusVO = testExecutionService.executeTestRun(testRunConfigurationChildId);
				log.info("inside the test.run.child.execute testRunList Size:"+testRunLists.size());
				hpqcConnector =  getHPQCConnector(product.getProductId(), request);
				if (hpqcConnector == null) {
					jTSingleResponse = new JTableSingleResponse("Error", "Unable to establish connection to HPQC. Check the mappings");
					return jTSingleResponse;
				}
				log.info("inside the test.run.child.execute testRunList Size:"+testRunLists.size());
				for(TestRunList testRunList : testRunLists){
					if(testRunChildConfig.getAutoPostResults()!=0 && hpqcConnector!=null){
						log.info("===========inside the test.run.child.execute =======:");
						String importCompleteStatus =  tafTestManagementSystemIntegrator.ExportTestExecutionResultsOTA(testRunList.getTestRunListId(), hpqcConnector);
						hpqcConnector.disconnect();
						if(importCompleteStatus != null){
							log.info("Export TestResults completed.");
							jTSingleResponse = new JTableSingleResponse("Ok","Export TestResults completed.");
						} else{
							log.info("Export TestResults completed.");
							jTSingleResponse = new JTableSingleResponse("Ok","Export TestResults completed1");
						}
					}
				}
				if (statusVO.hasExecutedSuccessfully) {
					jTSingleResponse = new JTableSingleResponse("OK",new JsonTestRunList(statusVO.testRunList));				
				} else {
					jTSingleResponse = new JTableSingleResponse("ERROR",statusVO.executionMessage);				
				}
	        } catch (Exception e) {
	        	jTSingleResponse = new JTableSingleResponse("ERROR", "Some unknown problem in executing Test Run");
	            log.error("JSON ERROR", e);	            
	        }
        return jTSingleResponse;
    }

	public ConnectorHPQC getHPQCConnector(int productId, HttpServletRequest request){

		
		ConnectorHPQC hpqcConnector = null;
		TestManagementSystem hpqcTestManagementSystem = null;

		try {
			log.info("Getting HPQC connection for product : " + productId);
			String testCaseSource = "HPQC";
			
			ProductMaster product = productListService.getProductById(productId);
			Set<TestManagementSystem> testManagementSystems = product.getTestManagementSystems();
			if(testManagementSystems == null || testManagementSystems.isEmpty()){
				log.info("No test Management system mapped to product. Hence using default properties file for connection.");
				try{
					
						
					InputStream fis = new FileInputStream (request.getServletContext().getRealPath("/")+"\\"+"properties"+"\\hpqc.properties");
					hpqcConnector =  new ConnectorHPQC(fis);
					
				} catch(Exception e){
					log.error("Unable to get default HPQC connection properties file.Hence aborting connection",e);
					return null;
				}
			} else {
				for (TestManagementSystem testManagementSystem : testManagementSystems) {
					if (testManagementSystem.getTestSystemName().equalsIgnoreCase(TAFConstants.TEST_MANAGEMENT_SYSTEM_HPQC)){
						hpqcTestManagementSystem = testManagementSystem;						
					}
				}
				if(hpqcTestManagementSystem != null){
					Set<TestManagementSystemMapping> testManagementMappings = hpqcTestManagementSystem.getTestManagementSystemMappings();
					if(testManagementMappings != null && !testManagementMappings.isEmpty()){
						
						String userName = hpqcTestManagementSystem.getConnectionUserName();
						String encyrptedPasssword = hpqcTestManagementSystem.getConnectionPassword();
						String passsword = passwordEncryptionService.decrypt(encyrptedPasssword);
						String url =hpqcTestManagementSystem.getConnectionUri();
						String domainName =hpqcTestManagementSystem.getConnectionProperty1();
						String hpqcProductName = null;
						for(TestManagementSystemMapping testManagementSystemMapping : testManagementMappings){
							if(testManagementSystemMapping.getMappingType().equals(TAFConstants.ENTITY_PRODUCT)){
								hpqcProductName = testManagementSystemMapping.getMappingValue();
							}
						}
						if(hpqcProductName == null || url == null || userName == null || passsword == null){
							log.info("Connection Parameters missing. hpqcProductName : url : userName : passsword -> " + hpqcProductName + " : " + url + " : " + userName + " : " +  passsword);
							return null;
						} else {
							if (domainName == null || domainName.isEmpty())
								domainName = "default";
													
							hpqcConnector = new ConnectorHPQC(url, userName, passsword, domainName, hpqcProductName);
							log.info("Obtained HPQC connection from mappings. hpqcProductName : url : userName : passsword -> " + hpqcProductName + " : " + url + " : " + userName + " : " +  passsword);
							//return hpqcConnector;
						}
					}
				} else {
					log.info("No HPQC Management system mapped to product. Hence using default properties file for connection.");
					try{
						InputStream fis = new FileInputStream (request.getServletContext().getRealPath("/")+"\\"+"properties"+"\\hpqc.properties");
						hpqcConnector =  new ConnectorHPQC(fis);
						return hpqcConnector;
					} catch(Exception e){
						log.error("Unable to get default HPQC connection properties file.Hence aborting connection",e);
						return null;
					}
				}
			}
			
		} catch(Exception e){
			log.error("Unable to get HPQC Connection.", e);
			return null;
		}
		return hpqcConnector;

	}
	
	@RequestMapping(value="testrunplan.has.runconfig.list",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse listRunConfigsOfTestRunPlan(@RequestParam String testRunPlanId) {
		log.debug("testrunplan.has.runconfig.list");	
		int intTmptestRunPlanId=Integer.parseInt(testRunPlanId);
		JTableResponse jTableResponse;
			try {
				TestRunPlan testRunPlan = productListService.getTestRunPlanById(intTmptestRunPlanId);
				Set<RunConfiguration> runConfigSet = testRunPlan.getRunConfigurationList();//runConfigStatus implementation
				log.info("Runconfig List --"+runConfigSet.size());			
				
				List<JsonRunConfiguration> jsonRunConfigurationList = new ArrayList<JsonRunConfiguration>();
				for (RunConfiguration runConfiguration : runConfigSet) {
					jsonRunConfigurationList.add(new JsonRunConfiguration(runConfiguration));
				}
				
				jTableResponse = new JTableResponse("OK", jsonRunConfigurationList,jsonRunConfigurationList.size());
		           
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records for RunConfiguration of TestRunPlan!");
	            log.error("JSON ERROR", e);
	        }	        
        return jTableResponse;
    }
	
	@RequestMapping(value="testrunplan.has.testsuite.list",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse listTestSuiteOfTestRunPlan(@RequestParam int testRunPlanId) {
		log.info("testrunplan.has.testsuite.list");	
		//int intTmptestRunPlanId=Integer.parseInt(testRunPlanId);
		JTableResponse jTableResponse;
		List<JsonTestSuiteList> jsonTestSuiteList = new ArrayList<JsonTestSuiteList>();				
			
		try {
				TestRunPlan testRunPlan = productListService.getTestRunPlanBytestRunPlanId(testRunPlanId);
				int executionTypeId=-1;
				if(testRunPlan!=null && testRunPlan.getTestSuiteLists()!=null){
					Set<TestSuiteList> testSuiteListSet = testRunPlan.getTestSuiteLists();				
					for (TestSuiteList testSuite : testSuiteListSet) {	
						executionTypeId=testRunPlan.getExecutionType().getExecutionTypeId();
						if(executionTypeId==3){
							if(testSuite.getExecutionTypeMaster().getExecutionTypeId()==3){
								jsonTestSuiteList.add(new JsonTestSuiteList(testSuite));
							}
						}else{
							jsonTestSuiteList.add(new JsonTestSuiteList(testSuite));
						}
					}
				}
				jTableResponse = new JTableResponse("OK", jsonTestSuiteList,jsonTestSuiteList.size());
		           
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records for TestSuiteList of TestRunPlan!");
	            log.error("JSON ERROR fetching records for TestSuiteList : ", e);
	        }	        
        return jTableResponse;
    }
	
	@RequestMapping(value="testrunplan.has.testcase.list",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse listTestCaseOfTestRunPlan(@RequestParam int testRunPlanId) {
		log.info("testrunplan.has.testcase.list");	
		JTableResponse jTableResponse;
		List<JsonTestCaseList> jsonTestCaseList = new ArrayList<JsonTestCaseList>();				
			
		try {
				TestRunPlan testRunPlan = productListService.getTestRunPlanBytestRunPlanId(testRunPlanId);
				int executionTypeId=-1;
				if(testRunPlan!=null && testRunPlan.getTestCaseList()!=null){
					Set<TestSuiteList> testSuiteListSet = testRunPlan.getTestSuiteLists();				
					for (TestSuiteList testSuite : testSuiteListSet) {	
						Set<TestCaseList> testCaseListSet = testSuite.getTestCaseLists();
						for (TestCaseList testCase : testCaseListSet) {	
							jsonTestCaseList.add(new JsonTestCaseList(testCase));
						}
					}
				}
				log.info("Test case list size :"+jsonTestCaseList.size());
				jTableResponse = new JTableResponse("OK", jsonTestCaseList,jsonTestCaseList.size());
		           
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records for TestCaseList of TestRunPlan!");
	            log.error("JSON ERROR fetching records for TestCaseList of TestRunPlan :", e);
	        }	        
        return jTableResponse;
    }
	
	@RequestMapping(value="testrunplan.has.feature.list",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse listFeatureOfTestRunPlan(@RequestParam int testRunPlanId) {
		log.info("testrunplan.has.feature.list");	
		JTableResponse jTableResponse;
		List<JsonProductFeature> jsonFeatureList = new ArrayList<JsonProductFeature>();				
			
		try {
				TestRunPlan testRunPlan = productListService.getTestRunPlanBytestRunPlanId(testRunPlanId);
				int executionTypeId=-1;
				if(testRunPlan!=null && testRunPlan.getFeatureList()!=null){
					Set<ProductFeature> featureListSet = testRunPlan.getFeatureList();				
					for (ProductFeature feature : featureListSet) {	
						jsonFeatureList.add(new JsonProductFeature(feature));
					}
				}
				jTableResponse = new JTableResponse("OK", jsonFeatureList,jsonFeatureList.size());
		           
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records for Feature of TestRunPlan!");
	            log.error("JSON ERROR fetching records for Feature :", e);
	        }	        
        return jTableResponse;
    }
	
	@RequestMapping(value="testrunplan.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listTestRunPlanByVersionId(@RequestParam int testRunPlanId, HttpServletRequest req) {
		log.debug("testrunplan.list");
		JTableResponse jTableResponse = null;
			try {		
				int id = 9;
				TestRunPlan testRunPlanObj = productListService.getTestRunPlanBytestRunPlanId(testRunPlanId);
				List<JsonTestRunPlan> jsonTestRunPlanlist = new ArrayList<JsonTestRunPlan>();		
				if(testRunPlanObj!=null)
					jsonTestRunPlanlist.add(new JsonTestRunPlan(testRunPlanObj));
				jTableResponse = new JTableResponse("OK", jsonTestRunPlanlist,jsonTestRunPlanlist.size() );				
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records for TestRunPlan list!");
	            log.error("JSON ERROR", e);	            
	        }
        return jTableResponse;
    }
	
	@RequestMapping(value="testrunplan.runconfig.device.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listGeneric_DevicesByTestRunPlanId(@RequestParam int testRunPlanId, HttpServletRequest req) {
		log.debug("testrunplan.runconfig.device.list");
		JTableResponse jTableResponse = null;
		List<GenericDevices>  genericDeviceList = new ArrayList<GenericDevices>();
		List<JsonGenericDevice> jsonGenericDeviceList = new ArrayList<JsonGenericDevice>();
		
		try {
				TestRunPlan testRunPlanObj = productListService.getTestRunPlanBytestRunPlanId(testRunPlanId);
				if(testRunPlanObj!=null && testRunPlanObj.getRunConfigurationList()!=null){
					Set<RunConfiguration> runConfigurationSet = testRunPlanObj.getRunConfigurationList(); //runConfigStatus implementation
					for (RunConfiguration runConfigurationObj : runConfigurationSet) {
						genericDeviceList.add(runConfigurationObj.getGenericDevice());	
						if(runConfigurationObj.getGenericDevice()!=null){
							jsonGenericDeviceList.add(new JsonGenericDevice(runConfigurationObj.getGenericDevice()));
						}
					}
				}
				jTableResponse = new JTableResponse("OK", jsonGenericDeviceList,jsonGenericDeviceList.size() );				
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching GenericDevice records for TestRunPlan !");
	            log.error("JSON ERROR", e);	            
	        }
        return jTableResponse;
    }
	
	@RequestMapping(value="runconfiguration.has.testsuite.list",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse listTestSuiteOfRunConfig(@RequestParam int runconfigId) {
		log.info("runconfiguration.has.testsuite.list");	
		JTableResponse jTableResponse;
		List<JsonTestSuiteList> jsonTestSuiteList = new ArrayList<JsonTestSuiteList>();		
		try {
			RunConfiguration runConfig = environmentService.getRunConfiguration(runconfigId);
			if(runConfig !=null && runConfig.getTestSuiteLists()!=null){
				Set<TestSuiteList> testSuiteListSet = runConfig.getTestSuiteLists();				
				for (TestSuiteList testSuite : testSuiteListSet) {				
					jsonTestSuiteList.add(new JsonTestSuiteList(testSuite));
				}
			}
			jTableResponse = new JTableResponse("OK", jsonTestSuiteList,jsonTestSuiteList.size());		           
        } catch (Exception e) {
            jTableResponse = new JTableResponse("ERROR","Error fetching records for TestSuiteList of Run Configuration!");
            log.error("JSON ERROR fetching records for TestSuiteList : ", e);
        }	        
        return jTableResponse;
    }
}
