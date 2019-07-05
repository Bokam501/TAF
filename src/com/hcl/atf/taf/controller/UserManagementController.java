package com.hcl.atf.taf.controller;

import java.io.File;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.controller.utilities.CommonUtility;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.integration.data.excel.ExcelUserDataIntegrator;
import com.hcl.atf.taf.model.AuthenticationMode;
import com.hcl.atf.taf.model.AuthenticationType;
import com.hcl.atf.taf.model.Customer;
import com.hcl.atf.taf.model.EntityMaster;
import com.hcl.atf.taf.model.EntityUserGroup;
import com.hcl.atf.taf.model.Languages;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.TestfactoryResourcePool;
import com.hcl.atf.taf.model.UserGroup;
import com.hcl.atf.taf.model.UserGroupMapping;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.UserResourcePoolMapping;
import com.hcl.atf.taf.model.UserRoleMaster;
import com.hcl.atf.taf.model.UserRoles;
import com.hcl.atf.taf.model.UserTypeMasterNew;
import com.hcl.atf.taf.model.VendorMaster;
import com.hcl.atf.taf.model.dto.ImportFileValidationDTO;
import com.hcl.atf.taf.model.json.JsonEntityUserGroup;
import com.hcl.atf.taf.model.json.JsonUserGroup;
import com.hcl.atf.taf.model.json.JsonUserList;
import com.hcl.atf.taf.model.json.JsonUserResourcePoolMapping;
import com.hcl.atf.taf.model.json.JsonUserRoles;
import com.hcl.atf.taf.model.json.jtable.JTableResponse;
import com.hcl.atf.taf.model.json.jtable.JTableSingleResponse;
import com.hcl.atf.taf.mongodb.service.MongoDBService;
import com.hcl.atf.taf.service.ActivityWorkPackageService;
import com.hcl.atf.taf.service.CustomerService;
import com.hcl.atf.taf.service.DataTreeService;
import com.hcl.atf.taf.service.EmailService;
import com.hcl.atf.taf.service.EventsService;
import com.hcl.atf.taf.service.ProductListService;
import com.hcl.atf.taf.service.ResourceManagementService;
import com.hcl.atf.taf.service.UserListService;
import com.hcl.atf.taf.service.VendorListService;
import com.hcl.atf.taf.service.WorkPackageService;

@Controller
public class UserManagementController {
	private static final Log log = LogFactory.getLog(UserManagementController.class);
	
	@Autowired
	private UserListService userListService;
	@Autowired
	private ResourceManagementService resourceService;
	@Autowired
	private VendorListService vendorService;
	@Autowired
	private ObjectMapper objectMapper;	
	@Autowired
	private DataTreeService dataTreeService;
	@Autowired
	private EmailService emailService;
	@Autowired
	private ExcelUserDataIntegrator excelUserDataIntegrator;
	@Autowired
	private ProductListService productListService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private MongoDBService mongoDBService;
	@Autowired
	private EventsService eventsService;
	@Autowired
	private WorkPackageService workPackageService; 
	
	@Autowired
	private ActivityWorkPackageService activityWorkPackageService; 
	
	@Value("#{ilcmProps['DEFAULT_PASSWORD']}")
    private String default_password;
	

	@Value("#{ilcmProps['EMAIL_ALERT_NOTIFICATION']}")
    private String emailNotification;
	
	@RequestMapping(value="administration.user.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listUsers(@RequestParam int jtStartIndex, @RequestParam int jtPageSize) {
		log.debug("inside administration.user.list");
		JTableResponse jTableResponse;
		 
		try {
			
			List<UserList> userList=userListService.list(jtStartIndex,jtPageSize);
			List<JsonUserList> jsonUserList=new ArrayList<JsonUserList>();
			for(UserList ul: userList){
				jsonUserList.add(new JsonUserList(ul));
				
			}
	            jTableResponse = new JTableResponse("OK", jsonUserList,userListService.getTotalRecords());
	           
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }
	        
        return jTableResponse;
    }
	

	@RequestMapping(value="administration.user.add",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse addUser(@ModelAttribute JsonUserList jsonUserList, BindingResult result,HttpServletRequest request,@RequestParam Integer resourcesType) {
		JTableSingleResponse jTableSingleResponse;
		
		if(result.hasErrors()){
			jTableSingleResponse = new JTableSingleResponse("ERROR","Invalid form!"); 			
		}
		try {
				String password="";
				UserList userList=jsonUserList.getUserList();
				
				if(userList.getFirstName() == null || userList.getFirstName().equals("")){
					return jTableSingleResponse = new JTableSingleResponse("INFORMATION","First Name should not be empty");
				}
				
				if(userList.getLastName() == null || userList.getLastName().equals("")){
					return jTableSingleResponse = new JTableSingleResponse("INFORMATION","Last Name should not be empty");
				}
				
				if(userList.getLoginId() == null || userList.getLoginId().equals("")){
				   return jTableSingleResponse = new JTableSingleResponse("INFORMATION","LoginId should not be empty");
				}
				
				if(userList.getUserCode() == null || userList.getUserCode().equals("")){
					return jTableSingleResponse = new JTableSingleResponse("INFORMATION","UserCode should not be empty");
				}
				
				if(userList.getEmailId() == null || userList.getEmailId().equals("")){
					return jTableSingleResponse = new JTableSingleResponse("INFORMATION","EmailId should not be empty");
				}
				
				UserResourcePoolMapping userResourcePoolMapping=null;
				if(userList.getAuthenticationType().getAuthenticationTypeId() ==  IDPAConstants.USER_ATUHENTICATION_TYPE_LOCAL){
					password=userList.getUserPassword();
				}
				userList.setUserPassword(encrypt(userList.getUserPassword()));
				if(resourcesType == 1 || resourcesType == 2){
					jsonUserList.setResourcePoolId(-10);
					jsonUserList.setVendorId(-10);
				}
				
				if(jsonUserList.getResourcePoolId() != null) {
					TestfactoryResourcePool resourcePoolFromDB = resourceService.getTestfactoryResourcePoolListbyId(jsonUserList.getResourcePoolId());
					userList.setResourcePool(resourcePoolFromDB);
				}else {
					jsonUserList.setResourcePoolId(-10);
				}
				
				VendorMaster vendor = vendorService.getVendorById(jsonUserList.getVendorId());
				userList.setVendor(vendor);
				
				UserTypeMasterNew userTypeNew = userListService.getUserTypeNewById(jsonUserList.getUserTypeId());
				userList.setUserTypeMasterNew(userTypeNew);
				
				Languages languages = userListService.getLanguageForId(jsonUserList.getLanguageId());
				userList.setLanguages(languages);
				
				AuthenticationType authenticationType = userListService.getAuthenticationTypeById(jsonUserList.getAuthenticationTypeId());
				userList.setAuthenticationType(authenticationType);
				
				if(jsonUserList.getAuthenticationModeId() != null){
					if(jsonUserList.getAuthenticationTypeId()!=2){
						userList.setAuthenticationMode(null);
					}else{
					AuthenticationMode authenticationMode = userListService.getAuthenticationModeById(jsonUserList.getAuthenticationModeId());
					userList.setAuthenticationMode(authenticationMode);
					}
				}
				
				UserRoleMaster userRole=userListService.getRoleByLabel(jsonUserList.getUserRoleLabel());
			userList.setUserRoleMaster(userRole);
			
			userList.setStatus(1);
			userList.setEmailId(userList.getEmailId());
				
				if (!(isUserExists(userList))){
					if(!(isCustomerUserExitsByLoginId(userList))){
						log.info("name is not existed");
						Customer customer = null;
						if(resourcesType ==2){
							int customerId = 0;
							if(jsonUserList.getCustomerId() != null){
								customerId = jsonUserList.getCustomerId();
								customer = customerService.getCustomerId(customerId);
								log.info("customer **********: "+customer.getCustomerName());
								if(userList.getCustomer() != null){
									log.info(" userList.getCustomer() Not NULL");
									Set<Customer> customerSet = userList.getCustomer();
									if(!customerSet.contains(customer)){
										log.info("Does not contain customer: "+customer.getCustomerName());
										customerSet.add(customer);
										userList.setCustomer(customerSet);
									}else{
										log.info("Customer : "+customer.getCustomerName()+" already mapped to user...");
									}
								}else{
									log.info(" userList.getCustomer() Not NULL");
								}				
							}
						}
						int userId = userListService.add(userList);
						
						if(userList != null){
							userResourcePoolMapping = new UserResourcePoolMapping();
							userResourcePoolMapping.setUserId(userList);
							userResourcePoolMapping.setFromDate(new Date());
							userResourcePoolMapping.setToDate(DateUtility.formatedDateWithMaxYear(new Date()));
							userResourcePoolMapping.setResourcepoolId(userList.getResourcePool());
							userResourcePoolMapping.setUserRoleId(userRole);
							userResourcePoolMapping.setMappedDate(new Date());
							
							boolean value = userListService.getUserResourcePoolMappingByFilter(userResourcePoolMapping.getUserId().getUserId(),userResourcePoolMapping.getResourcepoolId().getResourcePoolId(),userResourcePoolMapping.getFromDate(),userResourcePoolMapping.getToDate(),null);
							if(!value){
								userListService.addResourcepoolmapping(userResourcePoolMapping);
							}
						}
						
						
						
						
						if(userList!=null && userList.getUserId()!=null){
							mongoDBService.addUserListToMongoDB(userList.getUserId());							
							eventsService.addNewEntityEvent(IDPAConstants.ENTITY_USER, userId, userList.getLoginId(), userList);
						}
						
						if(jsonUserList.getCustomerId() != null){
							customerService.updateCustomerUserOneToMany(jsonUserList.getCustomerId(),userId, "map");
						}
						if(emailNotification.equalsIgnoreCase("YES")){
							emailService.sendUserCreation(request,userList, password);
						}
						jTableSingleResponse = new JTableSingleResponse("OK",new JsonUserList(userList));
					}else{
						String messageDisplay = "Customer User Already Exists with the name: '"+userList.getLoginId()+"'";
						jTableSingleResponse = new JTableSingleResponse("ERROR",messageDisplay);
					}
				}
				else
				{
					if(isActiveUserExitsByLoginId(userList)){
						String messageDisplay = "LoginId '"+userList.getLoginId()+"' is Already Exists!";
						jTableSingleResponse = new JTableSingleResponse("ERROR",messageDisplay);
					}else{
						if(resourcesType == 2){
							int customerId = 0;
							if(jsonUserList.getCustomerId() != null){
								customerId = jsonUserList.getCustomerId();
								Customer customer = customerService.getCustomerId(customerId);
								log.info("customer **********: "+customer.getCustomerName());
								if(userList.getCustomer() != null){
									Set<Customer> customerSet = userList.getCustomer();
									if(!customerSet.contains(customer)){
										customerSet.add(customer);
										userList.setCustomer(customerSet);
									}
								}					
							}
						}
						userListService.add(userList);
						if(userList!=null){
						
						userResourcePoolMapping = new UserResourcePoolMapping();
						userResourcePoolMapping.setUserId(userList);
						userResourcePoolMapping.setFromDate(new Date());
						userResourcePoolMapping.setToDate(DateUtility.formatedDateWithMaxYear(new Date()));
						userResourcePoolMapping.setResourcepoolId(userList.getResourcePool());
						userResourcePoolMapping.setMappedDate(new Date());
						
						boolean value = userListService.getUserResourcePoolMappingByFilter(userResourcePoolMapping.getUserId().getUserId(),userResourcePoolMapping.getResourcepoolId().getResourcePoolId(),userResourcePoolMapping.getFromDate(),userResourcePoolMapping.getToDate(),null);
						if(!value){
							userListService.addResourcepoolmapping(userResourcePoolMapping);
						}
					}
						if(userList!=null && userList.getUserId()!=null){
							mongoDBService.addUserListToMongoDB(userList.getUserId());							
							eventsService.addNewEntityEvent(IDPAConstants.ENTITY_USER, userList.getUserId(), userList.getLoginId(), userList);
						}
						if(emailNotification.equalsIgnoreCase("YES")){
							emailService.sendUserCreation(request,userList, password);
						}
						jTableSingleResponse = new JTableSingleResponse("OK",new JsonUserList(userList));
					}
				}
	        } catch (Exception e) {
	        	jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding record!");
	            log.error("JSON ERROR", e);
	        }
        	return jTableSingleResponse;
    }
	@RequestMapping(value="administration.user.delete",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse deleteUser(@ModelAttribute JsonUserList jsonUserList,@RequestParam int userId) {
		JTableResponse jTableResponse;
		String remarks = "";
		try {
	           UserList userList=userListService.getUserListById(userId);
	           userList.setStatus(-1);
	            userListService.update(userList);
	            
	            if(userList!=null && userList.getUserId()!=null){
					mongoDBService.addUserListToMongoDB(userList.getUserId());
					remarks = "User :"+userList.getLoginId();
					eventsService.addEntityChangedEvent(IDPAConstants.ENTITY_USER, userList.getUserId(), userList.getLoginId(),
							jsonUserList.getModifiedField(), jsonUserList.getModifiedFieldTitle(),
							jsonUserList.getOldFieldValue(), jsonUserList.getModifiedFieldValue(), userList, remarks);
				}
	            log.info("User Deleted");
	            jTableResponse = new JTableResponse("OK");
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error deleting record!");
	            log.error("JSON ERROR", e);
	        }
	        
        return jTableResponse;
    }
	@RequestMapping(value="administration.user.update",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse updateUser(@ModelAttribute JsonUserList jsonUserList, BindingResult result, HttpServletRequest request) {
		JTableResponse jTableResponse;
		UserList userListFromUI = null;
		UserList userListFromDB = null;
		String remarks = "";
		List<JsonUserList> jsonUserListUpdated=new ArrayList<JsonUserList>();
		if(result.hasErrors()){
			jTableResponse = new JTableResponse("ERROR","Invalid Form!"); 
		}
		try {
					userListFromUI = jsonUserList.getUserList();
					String password="";
					if(userListFromUI.getAuthenticationType() != null){
						if(userListFromUI.getAuthenticationType().getAuthenticationTypeId() ==  IDPAConstants.USER_ATUHENTICATION_TYPE_LOCAL){
							password=userListFromUI.getUserPassword();
						}
					}
					
					UserRoleMaster userRole=userListService.getRoleByLabel(jsonUserList.getUserRoleLabel());
					UserTypeMasterNew userTypeNew=userListService.getUserTypeNewById(jsonUserList.getUserTypeId());
					
					if(jsonUserList.getResourcePoolId() != null && !jsonUserList.getResourcePoolId().equals("0")){						
						userListFromUI.setResourcePool(resourceService.getTestfactoryResourcePoolListbyId(jsonUserList.getResourcePoolId()));
					}	
					if(jsonUserList.getVendorId() != null && !jsonUserList.getVendorId().equals("0")){						
						userListFromUI.setVendor(vendorService.getVendorById(jsonUserList.getVendorId()));
					}	
					
					if(jsonUserList.getUserTypeId() != null && !jsonUserList.getUserTypeId().equals("0")){						
						userListFromUI.setUserTypeMasterNew(userListService.getUserTypeNewById(jsonUserList.getUserTypeId()));
					}
					
					userListFromUI.setUserRoleMaster(userRole);
					userListFromUI.setUserTypeMasterNew(userTypeNew);
							
					if(jsonUserList.getLanguageId() != null && !jsonUserList.getLanguageId().equals("0")){
						userListFromUI.setLanguages(userListService.getLanguageForId(jsonUserList.getLanguageId()));
					}					
					
					userListFromDB = userListService.getUserListById(userListFromUI.getUserId());
					
					if(jsonUserList.getLoginId() == null ){						
						userListFromUI.setLoginId(userListFromDB.getLoginId());
					}else{
						if(userListFromUI.getStatus() == userListFromDB.getStatus()){
							// This option is enabled for modifying login Id. As the users requested for login Id change
							if(userListFromUI.getLoginId().trim().equals(userListFromDB.getLoginId())){
							}else{
								userListFromUI.setLoginId(jsonUserList.getLoginId());
							}
						}
					}					
					if(userListFromUI.getLoginId().trim().equals(userListFromDB.getLoginId())){
						List<UserList> userList=updateUserName(userListFromUI);
						UserList userObj = (UserList)request.getSession().getAttribute("USER");
						remarks = "User :"+userListFromUI.getLoginId();
						eventsService.addEntityChangedEvent(IDPAConstants.ENTITY_USER, userListFromUI.getUserId(), userListFromUI.getLoginId(),
								jsonUserList.getModifiedField(), jsonUserList.getModifiedFieldTitle(),
								jsonUserList.getOldFieldValue(), jsonUserList.getModifiedFieldValue(), userObj, remarks);
						for(UserList ul:userList){
							jsonUserListUpdated.add(new JsonUserList(ul));
						}
						if(emailNotification.equalsIgnoreCase("YES")){
							emailService.sendUserCreation(request,userListFromUI, password);
						}
						jTableResponse = new JTableResponse("OK",jsonUserListUpdated ,1);
					}
					else {
						if (!(isUserExists(userListFromUI))){
							if(!(isCustomerUserExitsByLoginId(userListFromUI))){
								List<UserList> userList=updateUserName(userListFromUI);
								
								for(UserList ul:userList){
									jsonUserListUpdated.add(new JsonUserList(ul));
								}
								jTableResponse = new JTableResponse("OK",jsonUserListUpdated ,1);
							}else{
								jTableResponse = new JTableResponse("INFORMATION","Customer User Already Exists with the same name!");
							}
						}
						else
						{
							if(isActiveUserExitsByLoginId(userListFromUI)){
								jTableResponse = new JTableResponse("INFORMATION","User Name Already Exists!");
							}else{
								List<UserList> userListCheck=updateUserName(userListFromUI);
								UserList userObj = (UserList)request.getSession().getAttribute("USER");
								remarks = "User :"+userListFromUI.getLoginId();
								eventsService.addEntityChangedEvent(IDPAConstants.ENTITY_USER, userListFromUI.getUserId(), userListFromUI.getLoginId(),
										jsonUserList.getModifiedField(), jsonUserList.getModifiedFieldTitle(),
										jsonUserList.getOldFieldValue(), jsonUserList.getModifiedFieldValue(), userObj, remarks);
								for (UserList userList : userListCheck) {
									jsonUserListUpdated.add(new JsonUserList(userList));
								}
								jTableResponse = new JTableResponse("OK",jsonUserListUpdated ,1);
							}
							
						}
					}
		    } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error updating record!");
	            log.error("JSON ERROR", e);
	        }
	    return jTableResponse;
    }
	
	
	/*
	 * 'UserNames should be unique in Edit Screen.'
	 */
	public List<UserList> updateUserName(UserList userListFromUI){
		userListService.update(userListFromUI);
		if(userListFromUI!=null && userListFromUI.getUserId()!=null){
			mongoDBService.addUserListToMongoDB(userListFromUI.getUserId());
		}
		List<UserList> userList =new ArrayList<UserList>();
		userList.add(userListFromUI);
		return userList;
	}
	
	
	/*
	 * 'User should be unique.'
	 */
	public boolean isUserExists(UserList userList){
		boolean bResult=false;	
		try{
			bResult= userListService.isUserExistingByName(userList);
		}catch(Exception exception){
			log.error("JSON ERROR", exception);
		}
		return bResult;
	}
	
	public boolean isActiveUserExitsByLoginId(UserList userList){
		boolean bResult=false;	
		try{
			bResult= userListService.isActiveUserExitsByLoginId(userList.getLoginId().trim());
		}catch(Exception exception){
			log.error("JSON ERROR", exception);
		}
		return bResult;
	}
	
	public boolean isCustomerUserExitsByLoginId(UserList userList){
		boolean bResult=false;	
		try{
			bResult= userListService.isCustomerUserExitsByLoginId(userList.getLoginId().trim());
		}catch(Exception exception){
			log.error("JSON ERROR", exception);
		}
		return bResult;
	}
	

	
	////This funtion serves only for testing purpose - comment it when in production	
		@RequestMapping(value="administration.user.list",method=RequestMethod.GET ,produces="application/json")
	    public @ResponseBody JTableResponse listusers() {
			JTableResponse jTableResponse;
		try {
			
			List<UserList> userList=userListService.list();
			List<JsonUserList> jsonUserList=new ArrayList<JsonUserList>();
			for(UserList ul: userList){
				jsonUserList.add(new JsonUserList(ul));
				
			}
	            jTableResponse = new JTableResponse("OK", jsonUserList,userListService.getTotalRecords());
	           
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }
	        
        return jTableResponse;
	    }
		
		@RequestMapping(value="administration.user.type.list",method=RequestMethod.GET ,produces="application/json")
	    public @ResponseBody JTableResponse listUsersOfType(@RequestParam String userType) {
		JTableResponse jTableResponse;
		 
		try {
			
			List<UserList> userList=userListService.list();
			List<JsonUserList> jsonUserList=new ArrayList<JsonUserList>();
			for(UserList ul: userList){
				jsonUserList.add(new JsonUserList(ul));
				
			}
	            jTableResponse = new JTableResponse("OK", jsonUserList,userListService.getTotalRecords());
	           
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
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
	
	
	@RequestMapping(value="administration.user.list.by.usertype",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listUsersByUserType(@RequestParam int userTypeId) {
		log.debug("administration.user.list.by.usertype: "+userTypeId);
		JTableResponse jTableResponse;
		try {
			List<UserList> userList=userListService.listUserByType(userTypeId);
			List<JsonUserList> jsonUserList=new ArrayList<JsonUserList>();
			for(UserList ul: userList){
				jsonUserList.add(new JsonUserList(ul));				
			}
	            jTableResponse = new JTableResponse("OK", jsonUserList,userListService.getTotalRecords());
	           
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }	        
        return jTableResponse;
    }
	
	@RequestMapping(value="administration.user.list.by.options.status",method=RequestMethod.POST ,produces="application/json")   
	 public @ResponseBody JTableResponse listUsersByOptionsandStatus(@RequestParam int userTypeId, @RequestParam int resourcePoolId,  @RequestParam int testFactoryLabId, @RequestParam int statusID,@RequestParam Integer jtStartIndex,@RequestParam Integer jtPageSize) {	
	log.info("Listing Users By Options and Status");
		JTableResponse jTableResponse;
	    try {			
				List<UserList> userList=userListService.listUserByTypeResourcePoolTestfactoryLabByStatus(userTypeId, resourcePoolId, testFactoryLabId, statusID, jtStartIndex, jtPageSize);
				List<UserList> userListForPagination=userListService.listUserByTypeResourcePoolTestfactoryLabByStatus(userTypeId, resourcePoolId, testFactoryLabId, statusID, null, null);
	
				List<JsonUserList> jsonUserList=new ArrayList<JsonUserList>();
				for(UserList ul: userList){
					jsonUserList.add(new JsonUserList(ul));				
				}
		            jTableResponse = new JTableResponse("OK", jsonUserList,userListForPagination.size());
		           
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error fetching Users!");
		            log.error("JSON ERROR fetching Users", e);
		        }	        
	    return jTableResponse;
   }
	
	
	@RequestMapping(value="administration.user.list.by.resourcepool.mapped.options.status",method=RequestMethod.POST ,produces="application/json")   
	 public @ResponseBody JTableResponse listUsersByResourcePoolMappedOptionsandStatus(@RequestParam int userTypeId, @RequestParam int resourcePoolId,  @RequestParam int testFactoryLabId, @RequestParam int statusID,@RequestParam Integer jtStartIndex,@RequestParam Integer jtPageSize) {	
	log.info("administration.user.list.by.resourcepool.mapped.options.status");
		JTableResponse jTableResponse;
	    try {			
	    	
	    		    	
	    	List<UserList> userListPrimary=userListService.listUserByTypeResourcePoolTestfactoryLabByStatus(userTypeId, resourcePoolId, testFactoryLabId, statusID, jtStartIndex, jtPageSize);
	    	List<Integer>userIds = new ArrayList<Integer>();
	    	
	    	
				List<UserList> userList=userListService.listUserByTypeResourcePoolMapped(userTypeId, resourcePoolId, testFactoryLabId, statusID, jtStartIndex, jtPageSize);
				for(UserList ur:userList){
		    		userIds.add(ur.getUserId());
		    	}
				
				
				List<UserList> userListForPagination=userListService.listUserByTypeResourcePoolMapped(userTypeId, resourcePoolId, testFactoryLabId, statusID, null, null);
	
				List<JsonUserList> jsonUserList=new ArrayList<JsonUserList>();
				
				for(UserList user:userListPrimary){
					if(!userIds.contains(user.getUserId())){
						userList.add(user);
					}
					
				}
				
				for(UserList ul: userList){
					jsonUserList.add(new JsonUserList(ul));				
				}
		            jTableResponse = new JTableResponse("OK", jsonUserList,userListForPagination.size());
		           
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error fetching Users!");
		            log.error("JSON ERROR fetching Users", e);
		        }	        
	    return jTableResponse;
  }
	
	
	
	public String encrypt(String passwordToHash)
    {
        String generatedPassword = null;
        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Add password bytes to digest
            md.update(passwordToHash.getBytes());
            //Get the hash's bytes
            byte[] bytes = md.digest();
            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            log.error("Error", e);
        }
        
        return generatedPassword;
    }
	
	@RequestMapping(value="profile.based.user.password.update",method=RequestMethod.POST,produces="application/json") 
	 public @ResponseBody JTableResponse updateUserPassword(@RequestParam Integer userId, @RequestParam String userPassword,HttpServletRequest request) {
	log.info("profile.based.user.password.update:");
		
		JTableResponse jTableResponse;
		try {
			
			UserList userList=userListService.getUserListById(userId);
				if (userList.getUserPassword().equals(encrypt(userPassword))){
					jTableResponse = new JTableResponse("ERROR","New password should not same as existing password!");
				}
				else {
					userList.setUserPassword(encrypt(userPassword));
					userListService.update(userList);
					 if(userList!=null && userList.getUserId()!=null){
							mongoDBService.addUserListToMongoDB(userList.getUserId());
						}
					if(emailNotification.equalsIgnoreCase("YES")){
						emailService.sendUserPasswordUpdateMail(request,userList,userPassword);
					}
					jTableResponse = new JTableResponse("OK","Your Password has been Updated"); 
				}
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error Updating Records!");
	            log.error("JSON ERROR", e);
	        }	        
       return jTableResponse;
   }
	
	@RequestMapping(value="profile.based.user.list",method=RequestMethod.POST ,produces="application/json")   
	 public @ResponseBody JTableResponse listUsersByUserId( @RequestParam Integer userId) {	
	log.debug("profile.based.user.list: "+userId);
	List<JsonUserList> jsonUserList=new ArrayList<JsonUserList>();
	
		JTableResponse jTableResponse;
		try {			
			UserList userList=null;
			if(userId!=null){
				userList=userListService.getUserListById(userId);
					jsonUserList.add(new JsonUserList(userList));
			}
           jTableResponse = new JTableResponse("OK", jsonUserList,jsonUserList.size());
	           
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }	        
     return jTableResponse;
	}
	
	@RequestMapping(value="profile.based.user.update.inline",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse profileUpdateInline(HttpServletRequest request, @RequestParam String modifiedValue, @RequestParam String modifiedField) {
		JTableResponse jTableResponse = null;
		try {
			
			log.info(" modified Field : "+modifiedField);
			log.info(" modifiedField Value: "+modifiedValue);
			UserList user = (UserList)request.getSession().getAttribute("USER");

			UserList userList= userListService.profileUpdateInline(modifiedValue,modifiedField,user.getUserId());
			if (userList == null) {
				jTableResponse = new JTableResponse("Error", "User Data cannot be modified");
			} else {
				jTableResponse = new JTableResponse("OK", "User data modified successfully");
			}
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error Updating user data !");
	        log.error("JSON ERROR", e);	            
	    }
        return jTableResponse;
		
	}
	
	@RequestMapping(value="profile.based.user.update.inline.photo",method=RequestMethod.POST ,produces="text/plain")
	public @ResponseBody JTableResponse profileUpdateInlinePhoto(HttpServletRequest request,@RequestParam String modifiedField) {
		JTableResponse jTableResponse = null;
		try {
			log.info(" modified Field : "+modifiedField);
			UserList user = (UserList)request.getSession().getAttribute("USER");
			
			String catalinaHome = System.getProperty("catalina.home");
			String serverFolderPath = catalinaHome + "\\webapps\\Profile\\";

			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			CommonsMultipartFile multipartFile = null;
			String fileName = "";
			Iterator<String> iterator = multipartRequest.getFileNames();
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				multipartFile = (CommonsMultipartFile) multipartRequest.getFile(key);
				fileName = multipartFile.getOriginalFilename();
				long size=multipartFile.getSize();
				//String Path = multipartFile.getName();
				fileName=user.getLoginId()+fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
				String extn[] = {".jpg",/*".gif",".png"*/}; 
				//if(size > 20971520){ // modified to reduct low size
				if(size > 122880){ // modified to reduct low size
					jTableResponse = new JTableResponse("ERROR","Image size should not exceed 120Kb!");
					return jTableResponse;
				}else if (!Arrays.asList(extn).contains(fileName.substring(fileName.lastIndexOf(".")).toLowerCase())) {
					jTableResponse = new JTableResponse("ERROR","Please upload an Image file!");
					return jTableResponse;
				}
				InputStream content = multipartFile.getInputStream();
				File filePath = new File(serverFolderPath);
				File image = new File(serverFolderPath + "\\"+ fileName);
				if (!filePath.isDirectory()) {
					FileUtils.forceMkdir(filePath);
				}
				CommonUtility.copyInputStreamToFile(content, image);
				userListService.profileUpdateInline(fileName,modifiedField,user.getUserId());
				jTableResponse = new JTableResponse("OK", "User data modified successfully");
			}
				
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error Updating user data !");
	        log.error("JSON ERROR", e);	            
	    }
        return jTableResponse;
	}
	
	@RequestMapping(value="userList.import", method=RequestMethod.POST ,produces="text/plain" )
	public @ResponseBody JTableResponse userListimport(HttpServletRequest request,@RequestParam String userType) {
		log.debug("userList.import");
		JTableResponse jTableResponse;
		try {
	//		
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			CommonsMultipartFile multipartFile = null;
			String fileName = "";
			InputStream is=null;
			Iterator<String> iterator = multipartRequest.getFileNames();
			//File fileForProcess = null;
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				multipartFile = (CommonsMultipartFile) multipartRequest.getFile(key);
				fileName = multipartFile.getOriginalFilename();
				is=multipartFile.getInputStream();
			}
			
			String isImportComplete = excelUserDataIntegrator.importUserLists(request, fileName, is, userType);
			fileName = isImportComplete.split(";")[1];
			isImportComplete = isImportComplete.split(";")[0];
			
			if(isImportComplete != null){
				
				jTableResponse = new JTableResponse("Ok","Import UserList Completed and" +isImportComplete, fileName);
			} else{
				
				jTableResponse = new JTableResponse("Ok","Import completed", fileName);
			}
			//jTableResponse=listTestCases(productId);
		} catch (Exception e) {
			jTableResponse = new JTableResponse("Error in Import");
			log.error("JSON ERROR importing userList : ", e);
		}
		return jTableResponse;
	}
	
	@RequestMapping(value="userList.validation.for.import", method=RequestMethod.POST ,produces="text/plain" )
	public @ResponseBody JTableResponse userListValidationForImport(HttpServletRequest request, @RequestParam String userType) {
		log.info("userList.validation.for.import");
		JTableResponse jTableResponse;
		try {
			
	//		
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			CommonsMultipartFile multipartFile = null;
			String fileName1 = "";
			Iterator<String> iterator = multipartRequest.getFileNames();
		
			String filePathStr = "";
			String newFileName="";
			String uploadType="UserList";
			String exportFileName = "";
			boolean checkFileWrittenStatus=false;
			
			ImportFileValidationDTO impValidDTO = new ImportFileValidationDTO();
			
			
			
			
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				multipartFile = (CommonsMultipartFile) multipartRequest.getFile(key);
				fileName1= multipartFile.getOriginalFilename();
				
				//InputStream content = item.getInputStream();
				InputStream content = multipartFile.getInputStream();
				filePathStr = request.getSession().getServletContext().getRealPath("/");
				log.info("filePathStr1--"+filePathStr);
				filePathStr = filePathStr + "validate";
				File filePath = new File(filePathStr);
				if (!filePath.exists()) {
					filePath.mkdirs();
				}
				long currtimeMillis = Calendar.getInstance().getTimeInMillis();
				newFileName=currtimeMillis+"_"+fileName1;
				exportFileName = filePathStr+File.separator+newFileName;
				// Upload file with prepended time stamp
				File file = new File(filePathStr+File.separator+newFileName);
				checkFileWrittenStatus=excelUserDataIntegrator.copyInputStreamToFile(content,file);
				log.info("_Copied file to temp loc");
				
				
				if(checkFileWrittenStatus)
				impValidDTO = excelUserDataIntegrator.readExcelFile(filePathStr,filePathStr+File.separator+newFileName, uploadType, userType);
				
			}		
			if(impValidDTO != null){
				
				String m1 = "UserList Validated for Import with";
				String m2 = impValidDTO.getCompletnessPercentage()+"% Completeness";
				String m3 = ", and "+ impValidDTO.getInvalidCount() +" Invalid "+", "+impValidDTO.getValidCount()+" Valid Record Count";
				String message = m1+m2+m3;
				
				jTableResponse = new JTableResponse("Ok",message, exportFileName);
			}else{
				jTableResponse = new JTableResponse("Ok","Validation completed");
	
			}		
			//jTableResponse=listTestCases(productId);
		} catch (Exception e) {
			jTableResponse = new JTableResponse("Error in Validating UserList to Import");
			log.error("JSON ERROR in userListValidation For Import", e);
		}
		return jTableResponse;
	}
	
	@RequestMapping(value="administration.user.list.loginId.exist",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse loginIdExist(@RequestParam String loginId) {
		JTableSingleResponse jTableSingleResponse;
		try {
			UserList userList= userListService.getUserByLoginId(loginId);
				if (userList != null){
					jTableSingleResponse = new JTableSingleResponse("INFORMATION","LoginId Already Exists!");
				}else{
					jTableSingleResponse = new JTableSingleResponse("OK","LoginId is Available!");
				}
	        } catch (Exception e) {
	        	jTableSingleResponse = new JTableSingleResponse("ERROR","Error Fetching record!");
	            log.error("JSON ERROR", e);
	        }
        return jTableSingleResponse;
    }
	
	@RequestMapping(value="administration.user.update.password",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse updatePassword(HttpServletRequest request, @RequestParam Integer userId) {
		JTableSingleResponse jTableSingleResponse;
		try {
			UserList userList= userListService.getUserListById(userId);
				if (userList != null){
					userList.setUserPassword(encrypt(default_password));
					userListService.update(userList);
					 if(userList!=null && userList.getUserId()!=null){
							mongoDBService.addUserListToMongoDB(userList.getUserId());
					 }
					if(emailNotification.equalsIgnoreCase("YES")){
						emailService.sendUserPasswordUpdateMail(request,userList,default_password);
					}	
					
					jTableSingleResponse = new JTableSingleResponse("OK","Password has been Updated!");
				}else{
					jTableSingleResponse = new JTableSingleResponse("ERROR","User is not Exists!");
				}
	        } catch (Exception e) {
	        	jTableSingleResponse = new JTableSingleResponse("ERROR","Error Updating Records!");
	            log.error("JSON ERROR", e);
	        }
        return jTableSingleResponse;
    }
	
	@RequestMapping(value="user.role.add.bulk",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse addRoleToUser(@ModelAttribute JsonUserRoles jsonUserRoles, BindingResult result,HttpServletRequest request,@RequestParam Integer userid) {
		JTableSingleResponse jTableSingleResponse;
		log.info("inside the user.role.add.bulk");
		boolean falg=false;
		try {	
				UserList user = (UserList)request.getSession().getAttribute("USER");
				
				if(user.getUserRoleMaster().getRoleLabel().equalsIgnoreCase(jsonUserRoles.getRoleName())){
					  jTableSingleResponse = new JTableSingleResponse("ERROR","You have selected the default role.Please select a different role.");
					  return jTableSingleResponse;
				}
				jsonUserRoles.setUserId(userid);
				jsonUserRoles.setCreatedBy(user.getLoginId());
				List<UserRoles> userRolesList = productListService.listUserRoles(-1, -1,userid);
				
				if(userRolesList!=null && !userRolesList.isEmpty()){
					for(UserRoles ur:userRolesList){
						if(ur.getRole().getRoleLabel().equalsIgnoreCase(jsonUserRoles.getRoleName())){
							falg=productListService.validateUserRole(ur,jsonUserRoles);
							if(falg){
								jTableSingleResponse = new JTableSingleResponse("ERROR","Already the Selected Role is add.Please edit the record to extend or reduce the assigned period.");
								return jTableSingleResponse;
							}
						}
					}
				}
				
				if(jsonUserRoles.getFromDate().compareTo(jsonUserRoles.getToDate())>0){
					jTableSingleResponse = new JTableSingleResponse("ERROR","From Date should not be greated than To Date");
					return jTableSingleResponse;
				}
				
				UserRoles userroles =productListService.mapUserWithRoles(jsonUserRoles);				
				jTableSingleResponse = new JTableSingleResponse("OK",new JsonUserRoles(userroles));
	        } catch (Exception e) {
	            jTableSingleResponse = new JTableSingleResponse("ERROR","Error Mapping Role to User!");
	            log.error("JSON ERROR adding Role To User", e);	 
	        }	        
        return jTableSingleResponse;
    }	
	
	@RequestMapping(value="user.role.delete.bulk",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse deleteRoleFromUser(@RequestParam Integer userId,@RequestParam Integer userRoleId) {
		JTableResponse jTableResponse;
		log.info("inside the user.role.delete.bulk>>>"+userId+">>>>>>>>>>>>"+userRoleId);
		try {			
			UserRoles userRoles = productListService.getUserRolewithuserRoleIdUserId(userId, userRoleId);
			userRoles.setStatus(0);		
			//Changes on 'Soft delete : Delete should only make the status of the UserRoles inactive and NOT delete from database'	
			productListService.updateUserRoles(userRoles);	
			
			log.debug("Unmapped User from Role at UserRoles, successfully");				
			jTableResponse = new JTableResponse("OK");
        } catch (Exception e) {
            jTableResponse = new JTableResponse("ERROR","Unable to Unmap User from Role at UserRoles!");
            log.error("JSON ERROR Unmapping User from Role at UserRoles", e);
        }
        return jTableResponse;
    }
	
	@RequestMapping(value="administration.userroles.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listUserRoles(@RequestParam int jtStartIndex, @RequestParam int jtPageSize,@RequestParam Integer userId) {
		log.debug("inside administration.userroles.list");
		JTableResponse jTableResponse;	 
		try {
			List<UserRoles> userRolesList = productListService.listUserRoles(jtStartIndex, jtPageSize,userId);
			List<JsonUserRoles> jsonUserRolesList = new ArrayList<JsonUserRoles>();
			for (UserRoles userRoles : userRolesList) {
				jsonUserRolesList.add(new JsonUserRoles(userRoles));
			}			
	            jTableResponse = new JTableResponse("OK", jsonUserRolesList,jsonUserRolesList.size());
	           
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error Listing UserRoles!");
	            log.error("JSON ERROR", e);
	        }	        
        return jTableResponse;
    }
	
	
	@RequestMapping(value="user.role.update.bulk",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse updateProduct(@ModelAttribute JsonUserRoles jsonUserRoles, BindingResult result) {
		JTableResponse jTableResponse = null;
		UserRoles userRolesUI = null;
		//UserRoles userRolesDB = null;
		List<UserRoles> userRoleList= new ArrayList<UserRoles>();
		if(result.hasErrors()){
			jTableResponse = new JTableResponse("ERROR","Invalid Form!"); 
		}
		try {
			
			userRolesUI = jsonUserRoles.getUserRoles();
			productListService.updateUserRoles(userRolesUI);
			userRoleList.add(userRolesUI);
			
			jTableResponse = new JTableResponse("OK",userRoleList,1);
	
		    } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Unable to update the Product!");
	            log.error("JSON ERROR", e);
	        }
	        
	        
        return jTableResponse;
    }
	
	/* My Availability Show Tab*/
	@RequestMapping(value="myavailability.show.mode",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse showMyAvailabilityHideTab(@RequestParam Integer userId) {			
		JTableResponse jTableResponse = null;			 
		try {
			UserList userListShowHideTab=userListService.getUserListById(userId);
			
			if (userListShowHideTab != null && userListShowHideTab.getResourcePool().getResourcePoolId() != -10){
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
	
	
	
	
	@RequestMapping(value="administration.resourcepoolmapping.list",method=RequestMethod.POST ,produces="application/json")   
	 public @ResponseBody JTableResponse listUserResourcePoolMapping(@RequestParam int userId) {	
	log.info("Listing Users By Options and Status");
		JTableResponse jTableResponse;
	    try {			
	    	 int resourcePoolId =0;
	    	 Integer jtStartIndex = 0;
	    	 Integer jtPageSize = 0;
	    	List<UserResourcePoolMapping> resourcepoolMappingList=userListService.listUserResourcePoolMapping(userId, resourcePoolId, jtStartIndex, jtPageSize);
	    	List<JsonUserResourcePoolMapping> jsonUserResourcePoolMapping=new ArrayList<JsonUserResourcePoolMapping>();
	    	
				for(UserResourcePoolMapping rm: resourcepoolMappingList){
					jsonUserResourcePoolMapping.add(new JsonUserResourcePoolMapping(rm));				
				}
		            jTableResponse = new JTableResponse("OK", jsonUserResourcePoolMapping,resourcepoolMappingList.size());
		           
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error fetching Users!");
		            log.error("JSON ERROR fetching Users", e);
		        }	        
	    return jTableResponse;
  }
	
	
	@RequestMapping(value="administration.resourcepoolmapping.add",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse addResourcepoolmapping(HttpServletRequest request, @ModelAttribute JsonUserResourcePoolMapping jsonUserResourcePoolMapping, BindingResult result) {
		JTableSingleResponse jTableSingleResponse;
		log.info(" Mapping Resourcepool ");
		if(result.hasErrors()){				
			jTableSingleResponse = new JTableSingleResponse("ERROR","Invalid form!"); 
		}			
		try {
				UserResourcePoolMapping userResourcePoolMapping = jsonUserResourcePoolMapping.getUserResourcePoolMapping();
				
				
				Date updatetodate=DateUtility.dateformatWithOutTime(jsonUserResourcePoolMapping.getToDate());
				Date updatefromdate=DateUtility.dateformatWithOutTime( jsonUserResourcePoolMapping.getFromDate());
				if(updatefromdate.compareTo(updatetodate)>0){
					jTableSingleResponse = new JTableSingleResponse("ERROR","Warning: From date should be less than or equal to To date");
					return jTableSingleResponse;
				}
				
				UserList user =new UserList();
				user.setUserId(jsonUserResourcePoolMapping.getUserId());
				userResourcePoolMapping.setMappedBy(user);
				
				Integer userId = jsonUserResourcePoolMapping.getUserId();
				Integer rpId = jsonUserResourcePoolMapping.getResourcepoolId();
				Date fromDate = DateUtility.dateformatWithOutTime( jsonUserResourcePoolMapping.getFromDate());
				Date toDate = DateUtility.dateformatWithOutTime( jsonUserResourcePoolMapping.getToDate());
				
				boolean value = userListService.getUserResourcePoolMappingByFilter(userId,rpId,fromDate,toDate,null);
				if(!value){
					userListService.addResourcepoolmapping(userResourcePoolMapping);
				}else{
					return jTableSingleResponse = new JTableSingleResponse("ERROR ","User Aleady mapped for this period");
				}
				
				
				
	            jTableSingleResponse = new JTableSingleResponse("OK",new JsonUserResourcePoolMapping(userResourcePoolMapping));
	        } catch (Exception e) {
	            jTableSingleResponse = new JTableSingleResponse("ERROR","Error Mapping Resourcepool!");
	            log.error("JSON ERROR adding Resourcepool", e);
	        }		        
        return jTableSingleResponse;			
    }
	
	
	@RequestMapping(value="administration.resourcepoolmapping.update",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse updateResourcepoolmapping(HttpServletRequest req, @ModelAttribute JsonUserResourcePoolMapping jsonUserResourcePoolMapping,BindingResult result) {
		JTableResponse jTableResponse = null;
		List<JsonUserResourcePoolMapping> jsonUserResourcePoolMappingList = new ArrayList<JsonUserResourcePoolMapping>();
		if(result.hasErrors()){
			jTableResponse = new JTableResponse("ERROR","Invalid Form!"); 
		}	
		UserResourcePoolMapping userResourcePoolMappingFromUI = null;
		UserResourcePoolMapping userResourcePoolMappingFromDB = null;
		try {
			userResourcePoolMappingFromUI= jsonUserResourcePoolMapping.getUserResourcePoolMapping();
			userResourcePoolMappingFromDB = userListService.getUserResourcePoolMappingById(jsonUserResourcePoolMapping.getResourcePoolMappingId());
			
			
			Integer userId = jsonUserResourcePoolMapping.getUserId();
			Integer rpId = jsonUserResourcePoolMapping.getResourcepoolId();
			Date fromDate = DateUtility.dateformatWithOutTime( jsonUserResourcePoolMapping.getFromDate());
			Date toDate = DateUtility.dateformatWithOutTime( jsonUserResourcePoolMapping.getToDate());
			
			
			Date updatetodate=DateUtility.dateformatWithOutTime(jsonUserResourcePoolMapping.getToDate());
			Date updatefromdate=DateUtility.dateformatWithOutTime( jsonUserResourcePoolMapping.getFromDate());
			if(updatefromdate.compareTo(updatetodate)>0){
				jTableResponse = new JTableResponse("ERROR","Warning: From date should be less than or equal to To date");
				return jTableResponse;
			}
			
			
			
			boolean value = userListService.getUserResourcePoolMappingByFilter(userId,rpId,fromDate,toDate,userResourcePoolMappingFromDB);
			if(!value){
				userListService.updateResourceMapping(userResourcePoolMappingFromUI);
				jsonUserResourcePoolMappingList.add(new JsonUserResourcePoolMapping(userResourcePoolMappingFromUI));
				jTableResponse = new JTableResponse ("OK", jsonUserResourcePoolMappingList);
			}else{
				return jTableResponse = new JTableResponse("INFORMATION ","User Aleady mapped for this period");
			}
			  
		    } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Unable to update the ResourcePoolMapping!");
	            log.error("JSON ERROR", e);
	        }
        return jTableResponse;
    }
	
	@RequestMapping(value="administration.resourcepoolmapping.delete",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse deleteResourcepoolmapping(@RequestParam int resourcePoolMappingId) {
		JTableResponse jTableResponse;
		try {
			
			UserResourcePoolMapping userResourcePoolMapping=userListService.getUserResourcePoolMappingById(resourcePoolMappingId);
			userListService.deleteResourcepoolMapping(userResourcePoolMapping);
			
			
			
	            log.info("resourcepoolmapping Deleted");
	            jTableResponse = new JTableResponse("OK");
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error deleting record!");
	            log.error("JSON ERROR", e);
	        }
	        
        return jTableResponse;
    }

	@RequestMapping(value="user.group.list",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse getUserGroupList(@RequestParam Integer testFactoryId, @RequestParam Integer productId, @RequestParam Boolean isConsolidated, @RequestParam int jtStartIndex, @RequestParam int jtPageSize) {
		JTableResponse jTableResponse;
		try {
			List<UserGroup> userGroups = userListService.getUserGroups(testFactoryId, productId, isConsolidated);
			List<JsonUserGroup> jsonUserGroups = new ArrayList<JsonUserGroup>();
			if(userGroups != null && userGroups.size() > 0){
				for(UserGroup userGroup : userGroups){
					jsonUserGroups.add(new JsonUserGroup(userGroup));
				}
			}
			jTableResponse = new JTableResponse("OK", jsonUserGroups);
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error listing record!");
			log.error("JSON ERROR", e);
		}
		return jTableResponse;
	}
	
	@RequestMapping(value = "user.group.add", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody JTableSingleResponse addUserGroup(@ModelAttribute JsonUserGroup jsonUserGroup,BindingResult result, HttpServletRequest request) {
		JTableSingleResponse jTableSingleResponse;
		try {
			log.debug("In user.group.add");
			UserGroup userGroup = jsonUserGroup.getUserGroup();
			boolean isAlreadyExsist = userListService.isUserGroupAvailable(jsonUserGroup.getUserGroupName(), null, jsonUserGroup.getTestFactoryId(), jsonUserGroup.getProductId());
			if(isAlreadyExsist){
				jTableSingleResponse = new JTableSingleResponse("ERROR", jsonUserGroup.getUserGroupName()+" - Activity type already available for this product / testfactory level");
			}else{
				userListService.addUserGroup(userGroup);
				jTableSingleResponse = new JTableSingleResponse("OK",new JsonUserGroup(userGroup));
			}
		} catch (Exception e) {
			jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding record!");
			log.error("JSON ERROR", e);
		}
		return jTableSingleResponse;
	}
	
	@RequestMapping(value = "user.group.update", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody JTableSingleResponse updateUserGroup(@ModelAttribute JsonUserGroup jsonUserGroup,BindingResult result, HttpServletRequest request) {
		JTableSingleResponse jTableSingleResponse;
		try {
			log.debug("In user.group.update");
			UserGroup userGroup = jsonUserGroup.getUserGroup();
			boolean isAlreadyExsist = userListService.isUserGroupAvailable(jsonUserGroup.getUserGroupName(), jsonUserGroup.getUserGroupId(), jsonUserGroup.getTestFactoryId(), jsonUserGroup.getProductId());
			if(isAlreadyExsist){
				jTableSingleResponse = new JTableSingleResponse("ERROR", jsonUserGroup.getUserGroupName()+" - Activity type already available for this product / testfactory level");
			}else{
				userListService.addUserGroup(userGroup);
				jTableSingleResponse = new JTableSingleResponse("OK",new JsonUserGroup(userGroup));
			}
		} catch (Exception e) {
			jTableSingleResponse = new JTableSingleResponse("ERROR","Error updating record!");
			log.error("JSON ERROR", e);
		}
		return jTableSingleResponse;
	}
	
	@RequestMapping(value = "users.available.to.map.with.group", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody JTableResponse getUsersAvailbleToMapWithGroup(@RequestParam Integer productId, @RequestParam Integer userGroupId) {
		JTableResponse jTableResponse;
		try {
			List<Object[]> usersAvailableToMap = userListService.getUsersAvailbleToMapWithGroup(productId, userGroupId);
			ArrayList<HashMap<String, Object>> usersAvailableToMapOptions = new ArrayList<HashMap<String, Object>>();
			if(usersAvailableToMap != null && usersAvailableToMap.size() > 0){
				for (Object[] row : usersAvailableToMap) {
					HashMap<String, Object> userOption =new HashMap<String, Object>();
					userOption.put(IDPAConstants.ITEM_ID, (Integer)row[0]);
					userOption.put(IDPAConstants.ITEM_NAME, (String)row[1]);					
					usersAvailableToMapOptions.add(userOption);					
				}
			}
			jTableResponse = new JTableResponse("OK", usersAvailableToMapOptions, usersAvailableToMapOptions.size());
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error while fetching records!");
			log.error("JSON ERROR", e);
		}
		return jTableResponse;
	}
	
	@RequestMapping(value = "users.available.to.map.with.group.count", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody JTableSingleResponse getUsersAvailbleToMapWithGroupCount(@RequestParam Integer productId, @RequestParam Integer userGroupId) {
		JTableSingleResponse jTableSingleResponse;
		Integer unMappedUserCount = 0;		
		HashMap<String, Integer> unMappedUsersCountMap =new HashMap<String, Integer>();
		try {
			unMappedUserCount = userListService.getUsersAvailbleToMapWithGroupCount(productId, userGroupId);
			unMappedUsersCountMap.put("unMappedTCCount", unMappedUserCount);						
			jTableSingleResponse = new JTableSingleResponse("OK", unMappedUsersCountMap);
		} catch (Exception e) {
			jTableSingleResponse = new JTableSingleResponse("ERROR","Unable to fetch unmapped dimensions count to product!");
			log.error("Error occured in getUsersAvailbleToMapWithGroupCount - ", e);	 
		}
	
		return jTableSingleResponse;
	}
	
	@RequestMapping(value = "users.already.mapped.with.group", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody JTableResponse getUsersMappedWithGroup(@RequestParam Integer productId, @RequestParam Integer userGroupId) {
		JTableResponse jTableResponse;
		try {
			List<Object[]> usersMapped = userListService.getUsersMappedWithGroup(productId, userGroupId);
			ArrayList<HashMap<String, Object>> usersMappedOptions = new ArrayList<HashMap<String, Object>>();
			if(usersMapped != null && usersMapped.size() > 0){
				for (Object[] row : usersMapped) {
					HashMap<String, Object> userOption =new HashMap<String, Object>();
					userOption.put(IDPAConstants.ITEM_ID, (Integer)row[0]);
					userOption.put(IDPAConstants.ITEM_NAME, (String)row[1]);					
					usersMappedOptions.add(userOption);					
				}
			}
			jTableResponse = new JTableResponse("OK", usersMappedOptions, usersMappedOptions.size());
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error while fetching records!");
			log.error("JSON ERROR", e);
		}
		return jTableResponse;
	}
	
	@RequestMapping(value="user.map.or.unmap.with.group",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse addDimensionForProduct(@RequestParam Integer productId, @RequestParam Integer userGroupId, @RequestParam Integer userId, @RequestParam String maporunmap, HttpServletRequest request) {  
		log.debug("  ------------user.map.or.unmap.with.group-----------");
		JTableSingleResponse jTableSingleResponse;
		try {
			UserGroupMapping userGroupMapping = new UserGroupMapping();
			ProductMaster productMaster = new ProductMaster();
			productMaster.setProductId(productId);
			userGroupMapping.setProduct(productMaster);
			
			UserGroup userGroup = new UserGroup();
			userGroup.setUserGroupId(userGroupId);
			userGroupMapping.setUserGroup(userGroup);
			
			UserList user = new UserList();
			user.setUserId(userId);
			userGroupMapping.setUser(user);
			
			UserList userList = (UserList)request.getSession().getAttribute("USER");
			userGroupMapping.setCreatedBy(userList);
			userGroupMapping.setCreatedDate(new Date());
			userGroupMapping.setModifiedBy(userList);
			userGroupMapping.setModifiedDate(new Date());
						
			userListService.mapOrUnmapUserWithUserGroup(userGroupMapping, maporunmap);			
	        jTableSingleResponse = new JTableSingleResponse("OK");
	    } catch (Exception e) {
	        jTableSingleResponse = new JTableSingleResponse("ERROR","Error while adding user to usergroup!");
	        log.error("JSON ERROR", e);
	    }	        
        return jTableSingleResponse;		
    }
	
	@RequestMapping(value="entity.user.group.map.or.unmap",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse addCustomer(@RequestParam Integer productId,@RequestParam Integer entityTypeId,@RequestParam Integer entityInstanceId, @RequestParam Integer userId, @RequestParam String maporunmap, HttpServletRequest request) {
		log.info("Inside entity.user.group.Map Or Unmap ");
		JTableSingleResponse jTableSingleResponse;
		try {  	
		
				EntityUserGroup entityUserGroup = new EntityUserGroup();
			
				EntityMaster entityMaster = new EntityMaster();
				entityMaster.setEntitymasterid(entityTypeId);
				entityUserGroup.setEntityTypeId(entityMaster);
				
				entityUserGroup.setEntityInstanceId(entityInstanceId);
				
				UserList user =new UserList(); 
				user.setUserId(userId);
				entityUserGroup.setUser(user);
				
				UserList mappedByUser =(UserList)request.getSession().getAttribute("USER");
				entityUserGroup.setMappedBy(mappedByUser);
				entityUserGroup.setMappedDate(new Date());
				ProductMaster product  = productListService.getProductById(productId);
				entityUserGroup.setProduct(product);
				if(maporunmap == null || maporunmap.isEmpty() || maporunmap.equalsIgnoreCase("map")){
					userListService.mapOrUnmapEntityUserGroup(entityUserGroup,maporunmap);
				}else{
					EntityUserGroup entityUserGroupFromDb = userListService.getEntityUserGroupByEntityAndEntityInstanceId(entityTypeId, entityInstanceId,userId);
					userListService.mapOrUnmapEntityUserGroup(entityUserGroupFromDb,maporunmap);
				}
		
					jTableSingleResponse = new JTableSingleResponse("OK",new JsonEntityUserGroup(entityUserGroup));		
	        } catch (Exception e) {
	            jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding Entity record!");
	            log.error("JSON ERROR Mapping User ", e);
	        }
	        
        return jTableSingleResponse;
    }
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="entity.user.group.mapped.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listEntityUserGroup(@RequestParam Integer entityTypeId,@RequestParam Integer entityInstanceId,HttpServletRequest request) {
		log.debug("inside entity.user.group.mapped.list");
		JTableResponse jTableResponse;
		try{
			List<EntityUserGroup>entityUserGroups = userListService.getEntityUserGroupByEntityId(entityTypeId,entityInstanceId);
			JSONArray mappedJsonArray = new JSONArray();
			
			if(entityUserGroups != null && entityUserGroups.size() > 0 ){
				for (EntityUserGroup userGroup : entityUserGroups) {
					JSONObject jsObj =new JSONObject();
					jsObj.put(IDPAConstants.ITEM_ID, userGroup.getUser().getUserId());
					jsObj.put(IDPAConstants.ITEM_NAME, userGroup.getUser().getLoginId());					
					mappedJsonArray.add(jsObj);					
				}
				
			}
			
            jTableResponse = new JTableResponse("OK", mappedJsonArray,mappedJsonArray.size());     
            entityUserGroups = null;
			
	        }catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Unable to show Mapped User list!");
	            log.error("JSON ERROR", e);
	        }
	        
        return jTableResponse;
    }
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "entity.user.group.un.mapped.list", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody JTableResponse getEntityUserGroupMappedList(@RequestParam Integer productId,@RequestParam Integer entityTypeId,@RequestParam Integer entityInstanceId) {
		JTableResponse jTableResponse;
		try {
			
			List<Object[]>entityUserGroups = userListService.getEntityUserGroupReadyForMapping(productId,entityTypeId,entityInstanceId);
			JSONArray mappedJsonArray = new JSONArray();
			for (Object[] userGroup : entityUserGroups) {
				JSONObject jsObj =new JSONObject();
				jsObj.put(IDPAConstants.ITEM_ID, userGroup[0]);
				jsObj.put(IDPAConstants.ITEM_NAME, userGroup[1]);					
				mappedJsonArray.add(jsObj);					
			}
			
			jTableResponse = new JTableResponse("OK", mappedJsonArray,mappedJsonArray.size());
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Unable to fetch unmapped  List!");
			log.error("Error occured in getUsersAvailbleToMapWithGroupList - ", e);	 
		}
	
		return jTableResponse;
	}
	
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "entity.user.group.un.mapped.count", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody JTableSingleResponse getEntityUserGroupMappedCount(@RequestParam Integer productId,@RequestParam Integer entityTypeId,@RequestParam Integer entityInstanceId) {
		JTableSingleResponse jTableSingleResponse;
		try {
			
			List<Object[]>entityUserGroups = userListService.getEntityUserGroupReadyForMapping(productId,entityTypeId,entityInstanceId);
			HashMap<String, Integer> unMappedUsersCountMap =new HashMap<String, Integer>();
			unMappedUsersCountMap.put("unMappedTCCount", entityUserGroups.size());	
			
			jTableSingleResponse = new JTableSingleResponse("OK", unMappedUsersCountMap);
		} catch (Exception e) {
			jTableSingleResponse = new JTableSingleResponse("ERROR","Unable to fetch unmapped Count!");
			log.error("Error occured in getUsersAvailbleToMapWithGroupCount - ", e);	 
		}
	
		return jTableSingleResponse;
	}
	
	@RequestMapping(value = "user.group.status.reason.audit", method = RequestMethod.POST, produces = "application/json")
	public void auditTrackForUserModification(Integer userId,String message,String fieldName,String oldField, String newField) {
		log.info("Inside auditTrackForUserModification method");
		try{
		UserList userListFromUI=userListService.getUserListById(userId);		
			if(fieldName!="" && fieldName.equalsIgnoreCase("Status")) {
				userListFromUI.setStatus(0);
				userListService.update(userListFromUI);
				eventsService.addEntityChangedEvent(IDPAConstants.ENTITY_USER, userListFromUI.getUserId(), userListFromUI.getLoginId(),
						fieldName, fieldName, oldField, newField, userListFromUI, message);
			}else if(fieldName!="" && fieldName.equalsIgnoreCase("Delete")) {
				userListFromUI.setStatus(-1);
				userListService.update(userListFromUI);
				eventsService.addEntityRemovedEvent(IDPAConstants.ENTITY_USER, userListFromUI.getUserId(), userListFromUI.getLoginId(),
						fieldName, fieldName, oldField, newField, userListFromUI, message);			
			}		
		}catch(Exception e){
			log.error("Error occured in auditTrackForUserModification - ", e);
		}
	}
	
	@RequestMapping(value="administration.user.approve",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse approveUser(@RequestParam Integer userId, @RequestParam Integer status, @RequestParam String modifiedField,
    											@RequestParam String modifiedFieldTitle, @RequestParam String oldFieldValue, @RequestParam String newFieldValue) {
		log.info("Inside administration.user.approve");
		JTableSingleResponse jTableSingleResponse;
		UserList userList = null;
		String remarks = "";
		try {
			userList = userListService.getUserListById(userId);
			userList.setStatus(1);
			userListService.update(userList);
			remarks = "User :"+userList.getLoginId();
			eventsService.addEntityChangedEvent(IDPAConstants.ENTITY_USER, userList.getUserId(), 
					userList.getLoginId(), modifiedField, modifiedFieldTitle, oldFieldValue, newFieldValue, userList, remarks);
			jTableSingleResponse = new JTableSingleResponse("OK", new JsonUserList(userList),"Success");
		} catch (Exception e) {
			jTableSingleResponse = new JTableSingleResponse("ERROR", "Error in administration.user.approve");
			log.error("JSON ERROR", e);
		}
		return jTableSingleResponse;
	}
	
	@RequestMapping(value="administration.user.reject",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse rejectUser(@RequestParam Integer userId, @RequestParam Integer status, @RequestParam String modifiedField,
			@RequestParam String modifiedFieldTitle, @RequestParam String oldFieldValue, @RequestParam String newFieldValue) {
		log.info("Inside administration.user.reject");
		JTableSingleResponse jTableSingleResponse;
		UserList userList = null;
		String remarks = "";
		try {
			userList = userListService.getUserListById(userId);
			userList.setStatus(-1);
			userListService.update(userList);
			remarks = "User :"+userList.getLoginId();
			eventsService.addEntityRemovedEvent(IDPAConstants.ENTITY_USER, userList.getUserId(), 
					userList.getLoginId(), modifiedField, modifiedFieldTitle, oldFieldValue, newFieldValue, userList, remarks);
			jTableSingleResponse = new JTableSingleResponse("OK", new JsonUserList(userList),"Success");
		} catch (Exception e) {
			jTableSingleResponse = new JTableSingleResponse("ERROR", "Error in administration.user.reject");
			log.error("JSON ERROR", e);
		}
		return jTableSingleResponse;
	}
	
}
