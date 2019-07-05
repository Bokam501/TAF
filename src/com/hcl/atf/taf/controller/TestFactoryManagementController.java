package com.hcl.atf.taf.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.constants.TAFConstants;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.controller.utilities.ValidationUtility;
import com.hcl.atf.taf.model.ActualShift;
import com.hcl.atf.taf.model.EngagementTypeMaster;
import com.hcl.atf.taf.model.TestFactory;
import com.hcl.atf.taf.model.TestFactoryCoreResource;
import com.hcl.atf.taf.model.TestFactoryLab;
import com.hcl.atf.taf.model.TestFactoryManager;
import com.hcl.atf.taf.model.TestfactoryResourcePool;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.UserRoleMaster;
import com.hcl.atf.taf.model.WorkShiftMaster;
import com.hcl.atf.taf.model.json.JsonActualShift;
import com.hcl.atf.taf.model.json.JsonResourcePool;
import com.hcl.atf.taf.model.json.JsonTestFactory;
import com.hcl.atf.taf.model.json.JsonTestFactoryCoreResource;
import com.hcl.atf.taf.model.json.JsonTestFactoryLab;
import com.hcl.atf.taf.model.json.JsonTestFactoryManager;
import com.hcl.atf.taf.model.json.JsonUserList;
import com.hcl.atf.taf.model.json.JsonUserRoleMaster;
import com.hcl.atf.taf.model.json.JsonWorkShiftMaster;
import com.hcl.atf.taf.model.json.jtable.JTableResponse;
import com.hcl.atf.taf.model.json.jtable.JTableResponseOptions;
import com.hcl.atf.taf.model.json.jtable.JTableSingleResponse;
import com.hcl.atf.taf.mongodb.constants.MongodbConstants;
import com.hcl.atf.taf.mongodb.dao.TestFactoryMongoDAO;
import com.hcl.atf.taf.mongodb.service.MongoDBService;
import com.hcl.atf.taf.service.EventsService;
import com.hcl.atf.taf.service.ProductListService;
import com.hcl.atf.taf.service.ResourceManagementService;
import com.hcl.atf.taf.service.TestFactoryManagementService;
import com.hcl.atf.taf.service.UserListService;
import com.hcl.atf.taf.service.WorkPackageService;



@Controller
public class TestFactoryManagementController {
	private static final Log log = LogFactory.getLog(TestFactoryManagementController.class);
	
	@Autowired
	private TestFactoryManagementService testFactoryManagementService;
	@Autowired
	private ResourceManagementService  resourceManagementService;
	@Autowired
	private ProductListService  productListService;
	@Autowired
	private WorkPackageService workPackageService;
	@Autowired
	private UserListService userListService;
	@Autowired
	private TestFactoryMongoDAO testFactoryMongoDAO;
	@Autowired
	private MongoDBService mongoDBService;
	@Autowired
	private EventsService eventsService;
	
	@Value("#{ilcmProps['MONGODB_AVAILABLE']}")
    private String MONGODB_AVAILABLE;
	
	String lineSeperator = FileSystems.getDefault().getSeparator();
	
	@RequestMapping(value="administration.testFactoryLabs.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listTestFactoryLabs() {			
		JTableResponse jTableResponse;			 
		try {
			
			List<TestFactoryLab> tfLabsList=testFactoryManagementService.getTestFactoryLabsList();
			List<JsonTestFactoryLab> jsontfLabsList=new ArrayList<JsonTestFactoryLab>();
			for(TestFactoryLab tfLabs: tfLabsList){
				jsontfLabsList.add(new JsonTestFactoryLab(tfLabs));	
				}				
			jTableResponse = new JTableResponse("OK", jsontfLabsList,jsontfLabsList.size());     
			tfLabsList = null;
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Unable to show Test Factory Labs!");
	            log.error("JSON ERROR", e);
	        }		        
        return jTableResponse;
    }
	@RequestMapping(value="administration.testFactory.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listTestFactory(HttpServletRequest req, @RequestParam int testFactoryLabId, @RequestParam int type) {			
		JTableResponse jTableResponse;			 
		try {
			UserList user=(UserList)req.getSession().getAttribute("USER");
			List<TestFactory> tfList=testFactoryManagementService.getTestFactoryList(testFactoryLabId,TAFConstants.ENTITY_STATUS_ACTIVE,type);
			List<JsonTestFactory> jsontfList=new ArrayList<JsonTestFactory>();
			for(TestFactory tfactory: tfList){
				jsontfList.add(new JsonTestFactory(tfactory));	
				}				
			jTableResponse = new JTableResponse("OK", jsontfList,jsontfList.size());     
			tfList = null;
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Unable to show Test Factories!");
	            log.error("JSON ERROR", e);
	        }		        
        return jTableResponse;
    }
	
	@RequestMapping(value="administration.testFactory.listby.userIdandlabId",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listTestFactoryByUserIdAndLabId(HttpServletRequest req, @RequestParam int testFactoryLabId, @RequestParam int userId, @RequestParam int testFactoryId, @RequestParam int status, @RequestParam int userRoleId) {			
		JTableResponse jTableResponse;		
		log.info("administration.testFactory.listby.userIdandlabId");
		try {
			List<TestFactory> tfList = new ArrayList<TestFactory>();
			if(userRoleId == IDPAConstants.ROLE_ID_ADMIN){
				tfList = testFactoryManagementService.listByTestFactoryIdAndLabId(testFactoryId, testFactoryLabId);
			}
			else if((userRoleId == IDPAConstants.ROLE_ID_TEST_FACTORY_MANAGER) || (userRoleId == IDPAConstants.ROLE_ID_TEST_MANAGER) || (userRoleId == IDPAConstants.ROLE_ID_TEST_LEAD)){
				tfList=testFactoryManagementService.getTestFactoryListByLabAndUser(testFactoryLabId,TAFConstants.ENTITY_STATUS_ACTIVE, userId, testFactoryId,status);	
			}
					
			List<JsonTestFactory> jsontfList=new ArrayList<JsonTestFactory>();
			for(TestFactory tfactory: tfList){
				jsontfList.add(new JsonTestFactory(tfactory));	
				}				
			jTableResponse = new JTableResponse("OK", jsontfList,jsontfList.size());     
			tfList = null;
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Unable to show Test Factories!");
	            log.error("JSON ERROR", e);
	        }		        
        return jTableResponse;
    }
	
	@RequestMapping(value="administration.testFactory.add",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse addTestFactory(HttpServletRequest request, @ModelAttribute JsonTestFactory jsonTestFactory, BindingResult result) {
		log.info("Inside administration.testFactory.add ");
		JTableSingleResponse jTableSingleResponse;
		String duplicateName="";
		String testFactoryNameFromDB="";
		if(result.hasErrors()){
			jTableSingleResponse= new JTableSingleResponse("ERROR","Invalid form!"); 
		}		
		try {
			
			log.info("jsonTestFactory.getEngagementTypeId()==="+jsonTestFactory.getEngagementTypeId());
			log.info("jsonTestFactory.getTestFactoryLabId()="+jsonTestFactory.getTestFactoryLabId());
			
			TestFactory testFactoryFromUI = jsonTestFactory.getTestFactory();
			
			if(jsonTestFactory.getTestFactoryLabId() != null){
				TestFactoryLab testFactoryLabFromUI = testFactoryManagementService.getTestFactoryLabById(jsonTestFactory.getTestFactoryLabId());
				testFactoryFromUI.setTestFactoryLab(testFactoryLabFromUI);
			}
			if(jsonTestFactory.getEngagementTypeId() != null){
				EngagementTypeMaster engagementTypeMasterFromUI = testFactoryManagementService.getEngagementTypeById(jsonTestFactory.getEngagementTypeId());
				testFactoryFromUI.setEngagementTypeMaster(engagementTypeMasterFromUI);
			}
			
			int testFactoryLabIdUI=jsonTestFactory.getTestFactory().getTestFactoryLab().getTestFactoryLabId();
			List<TestFactory> tfList=testFactoryManagementService.getTestFactoryList(testFactoryLabIdUI,TAFConstants.ENTITY_STATUS_ACTIVE,jsonTestFactory.getEngagementTypeId());
			
			if(jsonTestFactory.getTestFactoryLabId() != null){
				String testFactoryNameUI = jsonTestFactory.getTestFactory().getTestFactoryName();
				for(TestFactory tfName: tfList){
				 testFactoryNameFromDB=tfName.getTestFactoryName();
				log.info("testFactoryNameFromDB====="+testFactoryNameFromDB);
				if(testFactoryNameFromDB.equals(testFactoryNameUI))
				{
					duplicateName=testFactoryNameFromDB;
				}
				
			}
		}
		
			if(duplicateName!=""){
				 jTableSingleResponse = new JTableSingleResponse("ERROR","This Testfactory name already  Exists!");
				 return jTableSingleResponse;
			}
			else{
				testFactoryManagementService.addTestFactory(testFactoryFromUI);		
				//added for mongoDB insertion
				if(testFactoryFromUI!=null){
					if(testFactoryFromUI.getTestFactoryId()!=null){
						mongoDBService.addTestFactoryToMongo(testFactoryFromUI.getTestFactoryId());
						UserList user=(UserList)request.getSession().getAttribute("USER");
						String testFactorEngage = IDPAConstants.ENTITY_TEST_FACTORY;
						if(testFactoryFromUI.getEngagementTypeMaster().getEngagementTypeId() == 2 ){
							testFactorEngage = IDPAConstants.ENTITY_TEST_FACTORY_ENGAGEMENT;
						}
						eventsService.addNewEntityEvent(testFactorEngage, testFactoryFromUI.getTestFactoryId(), testFactoryFromUI.getTestFactoryName(), user);
					}
				}				
	            jTableSingleResponse = new JTableSingleResponse("OK",new JsonTestFactory(testFactoryFromUI));
			}
			
	    } catch (Exception e) {
	            jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding TestFactory!");
	            log.error("JSON ERROR adding TestFactory", e);
	        }
	        
        return jTableSingleResponse;
    }
	
	@RequestMapping(value="administration.testFactory.addby.userIdandlabId",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse addTestFactoryByUserIdAndLabId(HttpServletRequest req, @ModelAttribute JsonTestFactory jsonTestFactory, BindingResult result) {
		log.info("Inside administration.testFactory.add ");
		JTableSingleResponse jTableSingleResponse;
		UserList user=(UserList)req.getSession().getAttribute("USER");		
		if(result.hasErrors()){
			jTableSingleResponse= new JTableSingleResponse("ERROR","Invalid form!"); 
		}		
		try {
			TestFactory testFactoryFromUI = jsonTestFactory.getTestFactory();
			
			if(jsonTestFactory.getTestFactoryLabId() != null){
				TestFactoryLab testFactoryLabFromUI = testFactoryManagementService.getTestFactoryLabById(jsonTestFactory.getTestFactoryLabId());
				testFactoryFromUI.setTestFactoryLab(testFactoryLabFromUI);
			}
			if(jsonTestFactory.getEngagementTypeId() != null){
				EngagementTypeMaster engagementTypeMasterFromUI = testFactoryManagementService.getEngagementTypeById(jsonTestFactory.getEngagementTypeId());
				testFactoryFromUI.setEngagementTypeMaster(engagementTypeMasterFromUI);
			}
			
			String errorMessage = ValidationUtility.validateForNewTestFactoryAddition(testFactoryFromUI, testFactoryManagementService, "add");			
			if (errorMessage != null) {				
				jTableSingleResponse = new JTableSingleResponse("ERROR",errorMessage);
				return jTableSingleResponse;
			}
				int testFactoryId = testFactoryManagementService.addTestFactory(testFactoryFromUI);
				
				//added for mongoDB insertion
				if(testFactoryFromUI!=null){
					if(testFactoryFromUI.getTestFactoryId()!=null){
						mongoDBService.addTestFactoryToMongo(testFactoryFromUI.getTestFactoryId());
					}
				}
				
				TestFactoryManager tfManager =new TestFactoryManager();
				tfManager.setUserList(user);
				tfManager.setTestFactory(testFactoryManagementService.getTestFactoryById(testFactoryId));					
				tfManager.setCreatedDate(DateUtility.getCurrentTime());				
				
				testFactoryManagementService.addTestFactoryManager(tfManager);
	            
				 jTableSingleResponse = new JTableSingleResponse("OK",new JsonTestFactory(testFactoryFromUI));
	        } catch (Exception e) {
	            jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding TestFactory and Mapping to TestFactoryManager!");
	            log.error("JSON ERROR adding TestFactory and Mapping to TestFactoryManager", e);
	        }
        return jTableSingleResponse;
    }
	
	@RequestMapping(value="administration.testFactory.update",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse updateTestFactory(HttpServletRequest req, @ModelAttribute JsonTestFactory jsonTestFactory, BindingResult result) {
		log.info("administration.testFactory.update");
		JTableResponse jTableResponse;
		String remarks = "";
		if(result.hasErrors()){
			jTableResponse  = new JTableResponse("ERROR","Invalid form!"); 
		}		
		try {
			TestFactory testFactoryFromUI = jsonTestFactory.getTestFactory();			
			testFactoryManagementService.updateTestFactory(testFactoryFromUI);
			
			/*Mongodb paraent status update*/
			if(jsonTestFactory.getModifiedField().equalsIgnoreCase(MongodbConstants.MODIFIED_FIELD_STATUS)){
				mongoDBService.updateParentStatusInChildColletions(IDPAConstants.TESTFACTORY_ENTITY_MASTER_ID,jsonTestFactory.getTestFactoryId(),jsonTestFactory.getStatus());
			}
			
			//Update TestFactory mongoDB insertion
			if(testFactoryFromUI!=null){
				if(testFactoryFromUI.getTestFactoryId()!=null){
					mongoDBService.addTestFactoryToMongo(testFactoryFromUI.getTestFactoryId());
					UserList user=(UserList)req.getSession().getAttribute("USER");
					String testFactorEngage = IDPAConstants.ENTITY_TEST_FACTORY;
					if(testFactoryFromUI.getEngagementTypeMaster().getEngagementTypeId() == 2 ){
						testFactorEngage = IDPAConstants.ENTITY_TEST_FACTORY_ENGAGEMENT;
					}
					remarks = "TestFactory :"+testFactoryFromUI.getTestFactoryName();
					eventsService.addEntityChangedEvent(testFactorEngage, testFactoryFromUI.getTestFactoryId(), testFactoryFromUI.getTestFactoryName(),
							jsonTestFactory.getModifiedField(), jsonTestFactory.getModifiedFieldTitle(),
							jsonTestFactory.getOldFieldValue(), jsonTestFactory.getModifiedFieldValue(), user, remarks);
				}
			}
			
			List<JsonTestFactory> jsonTestFactoryList =new ArrayList<JsonTestFactory>();
			jsonTestFactoryList.add(new JsonTestFactory(testFactoryFromUI));
			jTableResponse = new JTableResponse("OK",jsonTestFactoryList ,1);
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error updating TestFactory record!");
	            log.error("JSON ERROR", e);
	        }	        
        return jTableResponse;
    }
	
	@RequestMapping(value="administration.testFactory.update.inline",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse updateTestFactoryInline(@RequestParam Integer testFactoryId, @RequestParam String modifiedField, @RequestParam String modifiedFieldValue) {			
		JTableResponse jTableResponse = null;
		try {
			
			log.info(" modified Field : "+modifiedField);
			log.info(" modifiedField Value: "+modifiedFieldValue);
			if(modifiedField.equalsIgnoreCase("testFactoryName")){
				TestFactory testFactoryFromDB = testFactoryManagementService.getTestFactoryById(testFactoryId);
				testFactoryFromDB.setTestFactoryName(modifiedFieldValue);
				String errorMessage = ValidationUtility.validateForNewTestFactoryAddition(testFactoryFromDB, testFactoryManagementService, "update");			
				if (errorMessage != null) {				
					jTableResponse = new JTableResponse("ERROR",errorMessage);
					return jTableResponse;
				}	
			}
			
			TestFactory testFactory = testFactoryManagementService.updateTestFactoryInline(testFactoryId, modifiedField, modifiedFieldValue);
			
			if (testFactory == null) {
				jTableResponse = new JTableResponse("Error", "testFactory cannot be updated. Please contact Admin");//, new JsonResourceAvailability(resourceAvailability), 1);
			} else {
				jTableResponse = new JTableResponse("OK", "testFactory updated successfully");//, new JsonResourceAvailability(resourceAvailability), 1);
			}
	        } catch (Exception e) {
	        	jTableResponse = new JTableResponse("ERROR","Error adding record!");
	            log.error("JSON ERROR", e);
	        }
        return jTableResponse;
    }
	
	@RequestMapping(value="administration.testFactory.coreResources.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listCoreResources(@RequestParam int testFactoryId,@RequestParam Integer jtStartIndex, @RequestParam Integer jtPageSize) {			
		JTableResponse jTableResponse;			 
		try {
			List<TestFactoryCoreResource> tfCoreResList=testFactoryManagementService.getCoreResourcesList(testFactoryId,jtStartIndex,jtPageSize);
			List<TestFactoryCoreResource> tfCoreResListforPagination=testFactoryManagementService.getCoreResourcesList(testFactoryId,null,null);
			List<JsonTestFactoryCoreResource> jsontfCoreResList=new ArrayList<JsonTestFactoryCoreResource>();
			for(TestFactoryCoreResource tfCoreRes: tfCoreResList){
				jsontfCoreResList.add(new JsonTestFactoryCoreResource(tfCoreRes));	
				}				
			jTableResponse = new JTableResponse("OK", jsontfCoreResList,tfCoreResListforPagination.size());     
			tfCoreResList = null;
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Unable to show Test Factory CoreResourcess!");
	            log.error("JSON ERROR showing Test Factory CoreResourcess", e);
	        }		        
        return jTableResponse;
    }
	@RequestMapping(value="testFactoryManagementControl.testfactory.resource.pool.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listResourcePool(@RequestParam int testFactoryId) {			
		JTableResponseOptions jTableResponseOptions;			 
		try {				
			List<TestfactoryResourcePool> resourcePoolfromDB=resourceManagementService.testFactoryResourcePoolListbyTFactoryId(testFactoryId);
			if(resourcePoolfromDB==null || resourcePoolfromDB.size()==0){
				jTableResponseOptions = new JTableResponseOptions("ERROR","No Resource Pool(s) mapped to the TestFactory!");
				 return jTableResponseOptions;
			}
			List<JsonResourcePool> jsonResourcePool = new ArrayList<JsonResourcePool>();			
			for(TestfactoryResourcePool tresourcepool: resourcePoolfromDB){
				jsonResourcePool.add(new JsonResourcePool(tresourcepool));
			}				
			jTableResponseOptions = new JTableResponseOptions("OK", jsonResourcePool,true);     
			resourcePoolfromDB = null;
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Unable to show TestFactory ResourcePools!");
	            log.error("JSON ERROR", e);
	        }		        
        return jTableResponseOptions;
    }
	@RequestMapping(value="testFactoryManagementControl.administration.user.roleList",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listAllUser(int userId) {
		log.debug("inside administration.user.roleList");
		JTableResponseOptions jTableResponseOptions = null;
		final boolean flag = true;
			try {
				List<UserRoleMaster> userRoleMaster = productListService.getRolesBasedResource();
				
				List<JsonUserRoleMaster> jsonProductUserRole = new ArrayList<JsonUserRoleMaster>();
				UserRoleMaster role = userListService.listUserRoleByUserId(userId);
				if(userRoleMaster != null && userRoleMaster.size() > 0){
					
					for(UserRoleMaster userRole: userRoleMaster){
						if(role != null && userRole != null){							
							if(userRole.getUserRoleId()==role.getUserRoleId()){
								jsonProductUserRole.add(0,new JsonUserRoleMaster(userRole));
							}else{
								if(userRole.getUserRoleId() == 3 || userRole.getUserRoleId()==4 || userRole.getUserRoleId()==5){
									jsonProductUserRole.add(new JsonUserRoleMaster(userRole));
								}
							}
						}					
						//log.info("Role----->"+role);
					}
					
				 }
					jTableResponseOptions = new JTableResponseOptions("OK", jsonProductUserRole,true);     
		        } catch (Exception e) {
		        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
		            log.error("JSON ERROR", e);
		        }
		        
	        return jTableResponseOptions;
    }
	@RequestMapping(value="testFactoryManagementControl.administration.user.userList",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listUsersForTestFactoryManagers(@RequestParam int testFactoryId) {
		log.debug("inside testFactoryManagementControl.administration.user.userList");
		log.info("testFactoryId---"+testFactoryId);
		JTableResponseOptions jTableResponseOptions = null;
		int jtStartIndex=1;
		int jtPageSize=10;
		int defaultResourcePoolId = -10;
			try {
				TestFactory testFactoryOrEngagementObj = null;
				List<UserList> userList = null;
				List<JsonUserList> jsonUserList = new ArrayList<JsonUserList>();
				Integer engagementType = 0;
					if(testFactoryId != 0){
						testFactoryOrEngagementObj = testFactoryManagementService.getTestFactoryById(testFactoryId);
					}
					
					if(testFactoryOrEngagementObj != null){
						engagementType = testFactoryOrEngagementObj.getEngagementTypeMaster().getEngagementTypeId();
						if(engagementType == 1){ // TestFactory
							List<TestfactoryResourcePool> rpoolList = testFactoryManagementService.getResourcePoolListbyTestFactoryId(testFactoryId);
							if(rpoolList != null){
								TestfactoryResourcePool testfactoryResourcePool = new TestfactoryResourcePool();
								testfactoryResourcePool.setResourcePoolId(-10);
								rpoolList.add(testfactoryResourcePool);
							}
							
							userList=userListService.listTestFactoryManagers(testFactoryId, rpoolList);
							if(userList!=null){
								if(userList.size()!=0){
									for(UserList user: userList){
										jsonUserList.add(new JsonUserList(user));
									}
									jTableResponseOptions = new JTableResponseOptions("OK", jsonUserList,true);   
								}else if(userList.size()==0){
									log.info("error");
									jTableResponseOptions = new JTableResponseOptions("ERROR","No Resource Pool(s) mapped to the test factory!");
									 return jTableResponseOptions;
								}
							}else{
								jTableResponseOptions = new JTableResponseOptions("ERROR","No Engagement Manager is available in the mapped Resource Pool(s)!");
								 return jTableResponseOptions;
							}
						}else if(engagementType == 2){ // TestEngagement
							List<TestfactoryResourcePool> rpoolList = new ArrayList<TestfactoryResourcePool>();
							TestfactoryResourcePool tfResPool = new TestfactoryResourcePool();
							tfResPool.setResourcePoolId(defaultResourcePoolId);
							rpoolList.add(tfResPool);
							userList=userListService.listTestFactoryManagers(testFactoryId, rpoolList);	
							if(userList != null && userList.size() > 0){
								for(UserList user: userList){
									jsonUserList.add(new JsonUserList(user));
								}
								jTableResponseOptions = new JTableResponseOptions("OK", jsonUserList,true);  
							}else{
								jTableResponseOptions = new JTableResponseOptions("ERROR","No Engagement Manager is available in Product Team!");
								return jTableResponseOptions;
							}
						}
					}
		        } catch (Exception e) {
		        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
		            log.error("JSON ERROR", e);
		        }
	        return jTableResponseOptions;
    }

	@RequestMapping(value="administration.testFactory.coreResources.add",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse addCoreRes(@ModelAttribute JsonTestFactoryCoreResource jsonCoreResouce, BindingResult result) {
		JTableSingleResponse jTableSingleResponse;
		JTableResponse jTableResponse;
		if(result.hasErrors()){
			jTableSingleResponse  = new JTableSingleResponse("ERROR","Invalid form!"); 
		}
		
		try {   log.info("Stauts:-->"+jsonCoreResouce.getStatus());
		
		
				TestFactoryCoreResource coreResouce= jsonCoreResouce.getTestFactoryCoreResource();
				
				
				log.info("Status-->"+coreResouce.getStatus());
				log.info("TestFactoryId:-->"+jsonCoreResouce.getTestFactoryId());
				
				log.info("userId:"+jsonCoreResouce.getUserId());
				log.info("Role Id:"+jsonCoreResouce.getUserRoleId());
				
				TestFactory testFactory=testFactoryManagementService.getTestFactoryById(jsonCoreResouce.getTestFactoryId());
				UserList user=userListService.getUserListById(jsonCoreResouce.getUserId());
				Boolean errorMessage=	testFactoryManagementService.isUserExisted(jsonCoreResouce.getTestFactoryId(),jsonCoreResouce.getUserId());
			if (errorMessage) {
				String msg="User "+user.getLoginId() +" already mapped to the TestFactory "+ testFactory.getTestFactoryName();
				jTableSingleResponse = new JTableSingleResponse("ERROR",msg);
				return jTableSingleResponse;
			}
				testFactoryManagementService.addCoreResource(coreResouce);
					jTableSingleResponse = new JTableSingleResponse("OK",new JsonTestFactoryCoreResource(coreResouce));		
	        } catch (Exception e) {
	            jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding record!");
	            log.error("JSON ERROR", e);
	        }
	        
        return jTableSingleResponse;
    }
	@RequestMapping(value="administration.testFactory.coreResources.update",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse updateCoreRes(@ModelAttribute JsonTestFactoryCoreResource jsonCoreResouce, BindingResult result) {
		JTableResponse jTableResponse;
		if(result.hasErrors()){
			jTableResponse = new JTableResponse("ERROR","Invalid Form!"); 
		}			
		try {		log.info("update Calling");	
		log.info("create date:"+jsonCoreResouce.getCreatedDate());
		log.info("fromdate:"+jsonCoreResouce.getFromDate());
		log.info("todate:"+jsonCoreResouce.getToDate());
		TestFactoryCoreResource coreResourceFromUI = jsonCoreResouce.getTestFactoryCoreResource();
				
		             TestFactoryCoreResource coreResourceFromDB = testFactoryManagementService.getCoreResourceById(jsonCoreResouce.getTestFactoryCoreResourceId());	
				log.info("environmentFromDB Cretaed date:"+coreResourceFromDB.getCreatedDate());
				
				if((coreResourceFromUI.getCreatedDate()!=null) ){					
					coreResourceFromUI.setCreatedDate(coreResourceFromDB.getCreatedDate());
				}
			
				testFactoryManagementService.updateCoreResource(coreResourceFromUI);	
				 jTableResponse = new JTableResponse("OK");  
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error updating CoreRes!");
	            log.error("JSON ERROR updating CoreRes", e);
	        }
	         
        return jTableResponse;
    }
	@RequestMapping(value="administration.testFactory.list.option",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listTestFactory(@RequestParam int testFactoryLabId, @RequestParam int userId, @RequestParam int testFactoryId, @RequestParam int userRoleId, @RequestParam int status) {			
		log.debug("Listing TestFactory");
		JTableResponseOptions jTableResponseOptions;
		try {
			List<TestFactory> tfList = new ArrayList<TestFactory>();
				tfList = testFactoryManagementService.getTestFactoryList();
			List<JsonTestFactory> jsontfList=new ArrayList<JsonTestFactory>();
			for(TestFactory tfactory: tfList){
				if(tfactory.getTestFactoryId()==testFactoryId){
					jsontfList.add(0, new JsonTestFactory(tfactory));
				}else{
					jsontfList.add(new JsonTestFactory(tfactory));
				}
					
				}				
			jTableResponseOptions = new JTableResponseOptions("OK", jsontfList,true);     
			tfList = null;
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching TestFactory!");
	            log.error("JSON ERROR fetching TestFactory", e);
	        }		        
        return jTableResponseOptions;
    }
	
	@RequestMapping(value="testFactory.managers.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listTestFactoryManagers(@RequestParam int testFactoryId) {			
		JTableResponse jTableResponse;			 
		try {
			List<TestFactoryManager> tfManagerList=testFactoryManagementService.listTestFactoryManager(testFactoryId,TAFConstants.ENTITY_STATUS_ACTIVE);
			List<JsonTestFactoryManager> jsontfList=new ArrayList<JsonTestFactoryManager>();
			for(TestFactoryManager tfactoryManager: tfManagerList){
				jsontfList.add(new JsonTestFactoryManager(tfactoryManager));	
				}				
			jTableResponse = new JTableResponse("OK", jsontfList,jsontfList.size());     
			tfManagerList = null;
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Unable to show Test factory managers!");
	            log.error("JSON ERROR showing Test factory managers", e);
	        }		        
        return jTableResponse;
    }
	@RequestMapping(value="testFactory.managers.add",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse addTestFactoryManager(@ModelAttribute JsonTestFactoryManager jsonTestFactoryManager, BindingResult result) {
		JTableSingleResponse jTableSingleResponse;
		JTableResponse jTableResponse;
		if(result.hasErrors()){
			jTableSingleResponse  = new JTableSingleResponse("ERROR","Invalid form!"); 
		}
		
		try {   
				TestFactoryManager tfManager	=jsonTestFactoryManager.getTestFactoryManager();
				int  userId=tfManager.getUserList().getUserId();
			int testFactoryId=tfManager.getTestFactory().getTestFactoryId();
			TestFactoryManager testFactoryManagerDB=testFactoryManagementService.getTestFactoryManagerByTestCatoryIdUserId(userId,testFactoryId);
			if(testFactoryManagerDB!=null){
				jTableSingleResponse = new JTableSingleResponse("ERROR","Already Existed!");
				return jTableSingleResponse;
			}else{
				testFactoryManagementService.addTestFactoryManager(tfManager);
				tfManager = testFactoryManagementService.getTestFactoryManagerByTestCatoryIdUserId(userId, testFactoryId);
					jTableSingleResponse = new JTableSingleResponse("OK",new JsonTestFactoryManager(tfManager));	
			}
	        } catch (Exception e) {
	            jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding record!");
	            log.error("JSON ERROR", e);
	        }
	        
        return jTableSingleResponse;
    }
	@RequestMapping(value="testFactory.managers.delete",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse deleteTestFactoryManager(@ModelAttribute JsonTestFactoryManager jsonTestFactoryManager,@RequestParam int testFactoryManagerId) {
		log.info("deleting evironmentId:"+testFactoryManagerId);
		JTableResponse jTableResponse;
		try {
		
		TestFactoryManager manager	=jsonTestFactoryManager.getTestFactoryManager();
			
			manager.setTestFactoryManagerId(testFactoryManagerId);
			
			testFactoryManagementService.deleteTestFactoryManager(manager);
	           
	            jTableResponse = new JTableResponse("OK");
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error deleting TestFactoryManager!");
	            log.error("JSON ERROR", e);
	        }
	        
        return jTableResponse;
    }
	@RequestMapping(value="testFactory.testFactoryWorkShifts.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listTestFactoryShits(@RequestParam int testFactoryId,@RequestParam int testFactoryLabId) {			
		JTableResponse jTableResponse;			 
		try {
			log.info("calling--->");
			log.info("TestFactory Id"+testFactoryId);
			int jtStartIndex=1;
			int jtPageSize=10;
			List<WorkShiftMaster> testFactoryWorkShiftsList=testFactoryManagementService.getTestFactoryWorkShiftsList(testFactoryId,testFactoryLabId);
			List<JsonWorkShiftMaster> jsonWorkShiftMaster=new ArrayList<JsonWorkShiftMaster>();
			for(WorkShiftMaster testFactoryWorkShifts: testFactoryWorkShiftsList){
				jsonWorkShiftMaster.add(new JsonWorkShiftMaster(testFactoryWorkShifts));	
				}				
			jTableResponse = new JTableResponse("OK", jsonWorkShiftMaster,jsonWorkShiftMaster.size());     
			testFactoryWorkShiftsList = null;
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Unable to show Test Factory Work Shifts!");
	            log.error("JSON ERROR", e);
	        }		        
        return jTableResponse;
    }
	
	
	@RequestMapping(value="testFactory.testFactoryWorkShifts.add",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse addTestFactoryShits(HttpServletRequest request, @ModelAttribute JsonWorkShiftMaster jsonWorkShiftMaster, BindingResult result, @RequestParam Integer testFactordId) {			
		JTableSingleResponse jTableSingleResponse;
		Boolean b_exists = false;		
		
		if(result.hasErrors()){
			jTableSingleResponse  = new JTableSingleResponse("ERROR","Invalid form!"); 
		}
		try {   log.info("shifts====>>>"+jsonWorkShiftMaster.getShiftTypeId());
			UserList userList = (UserList)request.getSession().getAttribute("USER");
				WorkShiftMaster workShiftMaster	=jsonWorkShiftMaster.getWorkShiftMaster();
				workShiftMaster.getTestFactory().setTestFactoryId(testFactordId);
				
				String startTime=jsonWorkShiftMaster.getStartTime();
				String endTime=jsonWorkShiftMaster.getEndTime();
				
				if(startTime.compareTo(endTime)>0)
				{
					jTableSingleResponse = new JTableSingleResponse("ERROR"," Warning :Start Time Should be Greater than End Time");
			        return jTableSingleResponse;
				}
					testFactoryManagementService.addTestFactoryShits(workShiftMaster);
					UserList userObj = userListService.getUserListById(userList.getUserId());
					eventsService.addNewEntityEvent(IDPAConstants.ENTITY_WORKSHIFT, workShiftMaster.getShiftId(), workShiftMaster.getShiftName(), userObj);
					jTableSingleResponse = new JTableSingleResponse("OK",new JsonWorkShiftMaster(workShiftMaster));
	        } catch (Exception e) {
	            jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding record!");
	            log.error("JSON ERROR", e);
	        }
        return jTableSingleResponse;
    }
	
	
	@RequestMapping(value="testFactory.testFactoryWorkShifts.update.inline",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse updateTestFactoryShits(@RequestParam Integer shiftId, @RequestParam String modifiedField, @RequestParam String modifiedFieldValue) {			
		JTableResponse jTableResponse = null;
		try {
			
			log.info(" modified Field : "+modifiedField);
			log.info(" modifiedField Value: "+modifiedFieldValue);
			
			WorkShiftMaster workShiftMaster = testFactoryManagementService.updateTestFactoryShitsInline(shiftId, modifiedField,  modifiedFieldValue);
			if (workShiftMaster == null) {
				jTableResponse = new JTableResponse("Error", "WorkShiftMaster cannot be updated. Please contact Admin");//, new JsonResourceAvailability(resourceAvailability), 1);
			} else {
				jTableResponse = new JTableResponse("OK", "WorkShiftMaster updated successfully");//, new JsonResourceAvailability(resourceAvailability), 1);
			}
	        } catch (Exception e) {
	        	jTableResponse = new JTableResponse("ERROR","Error adding record!");
	            log.error("JSON ERROR", e);
	        }
        return jTableResponse;
    }
	
	
	@RequestMapping(value="shift.entries.manage.list",method=RequestMethod.POST ,produces="application/text")
	public @ResponseBody JTableResponse shiftManageList(@RequestParam Integer testFactoryId, @RequestParam Integer shiftId, @RequestParam Date workDate) {
		log.info("inside resource.timesheet.manage");
		log.info("workPackageId>"+testFactoryId);
		log.info("shiftId>"+shiftId);
		
		JTableResponse jTableResponse =null;
		try {
			
			List<ActualShift> actualShift = testFactoryManagementService.shiftManageList(testFactoryId,shiftId,workDate);
			List<JsonActualShift> jsonActualShift=new ArrayList<JsonActualShift>();
			if(shiftId!=-1){
			for(ActualShift actualShifts: actualShift){
				jsonActualShift.add(new JsonActualShift(actualShifts));	
				}	
			}else{
			List<WorkShiftMaster> wrkShiftMasterList=	testFactoryManagementService.listWorkShiftsByTestFactoryId(testFactoryId);
			
			for(ActualShift actualShifts: actualShift){
				if(wrkShiftMasterList.contains(actualShifts.getShift())){
					jsonActualShift.add(new JsonActualShift(actualShifts));	
				}
				}
			
			}
			jTableResponse = new JTableResponse("OK", jsonActualShift,jsonActualShift.size());     
			actualShift = null;
		      } catch (Exception e) {
		        jTableResponse = new JTableResponse("ERROR","Error fetching records!");
		        log.error("JSON ERROR", e);
		        }	        
        return jTableResponse;
    }
	
	@RequestMapping(value="shift.entries.manage.add",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse shiftManageAdd(@RequestParam Integer testFactoryId, @RequestParam Integer shiftId, @RequestParam Date workDate,@RequestParam String shiftTime, @RequestParam String shiftRemarks,@RequestParam String shiftRemarksValue,@RequestParam Integer userId) {
		JTableResponse jTableResponse = null;
		boolean errFlag=false;
		String errMsg="";
		try {
			 List<ActualShift> actualListByDate=testFactoryManagementService.listActualShift(-1, workDate);
			 if(actualListByDate.size()!=0){
				 for(ActualShift actualShiftObj:actualListByDate){
				 if(actualShiftObj.getShift().getShiftId()==shiftId){
					 jTableResponse = new JTableResponse("ERROR","Shift Already Existed");
						return jTableResponse;
				 }
			 }
			 }
			    List<ActualShift> actualList=testFactoryManagementService.listActualShift(shiftId,null);
			log.info(" Added Value 1 : "+shiftTime+ "  Added Value 2 --> "+shiftRemarks);
			if(actualList.size()!=0){
				ActualShift actualShift=actualList.get(actualList.size()-1);
				if(actualShift!=null){
					if(actualShift.getEndTime()==null){
						errFlag=true;
						errMsg="There is a pending check out for you on : "+DateUtility.sdfDateformatWithOutTime(actualShift.getWorkdate())+" - "+actualShift.getShift().getShiftName();
						
					}
				}
			}
			
			if(errFlag){
				jTableResponse = new JTableResponse("ERROR",errMsg);
				return jTableResponse;
			}
			testFactoryManagementService.shiftManageAdd(shiftId,workDate,shiftTime,shiftRemarks,shiftRemarksValue,userId);
			
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error Adding data !");
	        log.error("JSON ERROR", e);	            
	    }
        return jTableResponse;
		
	}
	
	@RequestMapping(value="shift.entries.manage.update",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse shiftManageUpdate(HttpServletRequest request,@RequestParam Integer testFactoryId, @RequestParam Integer shiftId, @RequestParam Date workDate, @RequestParam String shiftTime, @RequestParam String shiftRemarks,@RequestParam String shiftRemarksValue,Integer userId ) {
		JTableResponse jTableResponse = null;
		boolean errFlag=false;
		String errMsg="There is no pending check out";
		ActualShift actualShift=null;
		Date endTime=DateUtility.toDateInSec(shiftTime);
		try {
			InputStream fis = new FileInputStream (request.getServletContext().getRealPath(File.separator)+File.separator+"data"+File.separator+"actualShift.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			String temp[]=null;
			String textLine=null;
			int actualShiftMaxhour=0;
			while ((textLine = br.readLine()) != null) {
				 temp=textLine.split("=");
				 if(temp[0].equals("actualShiftMaxAllowableHours")){
					 actualShiftMaxhour=Integer.parseInt(temp[1]);
					 break;
				 }
				
			}
			br.close();
			List<ActualShift> actualList=testFactoryManagementService.listActualShift(shiftId,null);
			if(actualList.size()!=0){
				 actualShift=actualList.get(actualList.size()-1);
				if(actualShift!=null){
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(actualShift.getStartTime());
					calendar.add(Calendar.HOUR_OF_DAY,actualShiftMaxhour);
				    Date maxCheckOutTime = calendar.getTime();
					if(actualShift.getStartTime().compareTo(endTime)<0 ){
						if(maxCheckOutTime.compareTo(endTime)>0){
						if(actualShift.getEndTime()==null){
							if(actualShift.getShift().getShiftId()!=shiftId){
								errFlag=true;
								errMsg="There is a pending check out for you on : "+DateUtility.sdfDateformatWithOutTime(actualShift.getWorkdate())+"to"+actualShift.getShift().getShiftName();
							}
								
						}else{  
							errFlag=true;
						}
					}else{  
						errFlag=true; errMsg="Check Out time should be lesser than next 24 hours";
					}
						}else{  
							errFlag=true; errMsg="Check Out time should be greater than Check In Time ";
						}
				}
			}else{
				 errFlag=true;
			}
			if(errFlag){
				jTableResponse = new JTableResponse("ERROR",errMsg);
				return jTableResponse;
			}
			testFactoryManagementService.shiftManageUpdate(actualShift, shiftTime, shiftRemarks, shiftRemarksValue,userId);
			
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error Adding data !");
	        log.error("JSON ERROR", e);	            
	    }
        return jTableResponse;
		
	}
	@RequestMapping(value="actual.shift.startTimeAndEndTime.ByShiftId",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody  String  getActualStartTimeAndEndTime(@RequestParam Integer testFactoryId, @RequestParam Integer shiftId, @RequestParam Date workDate) {
		String startTimeAndEndTime="";
		 String currenDateWithFormat="";
	List<ActualShift> actualShiftList = testFactoryManagementService.shiftManageList(testFactoryId,shiftId,workDate);
	Date currentDate=new Date();
	WorkShiftMaster wrkShift=testFactoryManagementService.getWorkShiftsByshiftId(shiftId);
	String workShiftStartTime[]=DateUtility.dateToStringInSecond(wrkShift.getStartTime()).split(" ");
	String startDateString=DateUtility.sdfDateformatWithOutTime(workDate)+" "+workShiftStartTime[1];
	int startHour=Integer.parseInt(workShiftStartTime[1].split(":")[0]);
	String workShiftEndTime[]=DateUtility.dateToStringInSecond(wrkShift.getEndTime()).split(" ");
	int endHour=Integer.parseInt(workShiftEndTime[1].split(":")[0]);
	String endDateString="";
	if(startHour>10 && (endHour>=00 && endHour<=10)){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(workDate);
		 calendar.add(Calendar.DAY_OF_MONTH,1);
		  calendar.set(Calendar.HOUR_OF_DAY,endHour);
	   workDate = calendar.getTime();
		    
		endDateString=DateUtility.sdfDateformatWithOutTime(workDate)+" "+workShiftEndTime[1];
	}else{
		endDateString=DateUtility.sdfDateformatWithOutTime(workDate)+" "+workShiftEndTime[1];
	}
	if(actualShiftList.size()!=0){
	ActualShift  actualShift = actualShiftList.get(actualShiftList.size()-1);
	if(actualShift!=null){
	if(actualShift.getEndTime()!=null){
	       startTimeAndEndTime=DateUtility.dateToStringInSecond(actualShift.getStartTime())+"#"+DateUtility.dateToStringInSecond(actualShift.getEndTime());
		}else if(actualShift.getStartTime()!=null){
			startTimeAndEndTime=DateUtility.dateToStringInSecond(actualShift.getStartTime())+"#"+endDateString;
		}
	}
	}else{
		
		startTimeAndEndTime=startDateString+"#"+"";
	
	}
	if(wrkShift.getShiftType().getShiftTypeId()==IDPAConstants.GRAVE_YARD_SHIFT_TYPE_ID){
		
		startTimeAndEndTime=startTimeAndEndTime+"#"+wrkShift.getShiftId();
		 return startTimeAndEndTime;
	}else{
	        return startTimeAndEndTime;
	}
	    }
	
	
	/* ResourcePool Show Tab*/
	@RequestMapping(value="testfactory.resourcepool.tab.show.mode",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse showResourcePoolHideTab(@RequestParam Integer testFactoryId) {			
		JTableResponse jTableResponse = null;			 
		try {
			TestFactory testFactoryRPShowHideTab=testFactoryManagementService.getResourcePoolShowHideTab(testFactoryId);
			
			if (testFactoryRPShowHideTab != null){
				jTableResponse = new JTableResponse("OK","The Tab can show ");
			}else{
				jTableResponse = new JTableResponse("INFORMATION","The Tab cannot show");
			}
			
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }		        
        return jTableResponse;
    }
	
	@RequestMapping(value="engagement.awp.summary",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableSingleResponse showSummaryForEngagement(@RequestParam Integer testFactId) {			
		JTableSingleResponse jTableSingleResponse = null;			 
		try {
			TestFactory testFactory = testFactoryManagementService.getTestFactoryById(testFactId);
			
			if (testFactory != null){
				JsonTestFactory jsonTestFactory = new JsonTestFactory(testFactory);
				
				jTableSingleResponse = new JTableSingleResponse("OK",jsonTestFactory);
			}else{
				jTableSingleResponse = new JTableSingleResponse("INFORMATION","The Tab cannot show");
			}
			
	        } catch (Exception e) {
	        	jTableSingleResponse = new JTableSingleResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }		        
        return jTableSingleResponse;
    }
}

