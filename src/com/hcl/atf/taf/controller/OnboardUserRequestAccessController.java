/**
 * 
 */
package com.hcl.atf.taf.controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
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
import com.hcl.atf.taf.controller.utilities.ActiveDirectoryUtility;
import com.hcl.atf.taf.dao.ProductTeamResourcesDao;
import com.hcl.atf.taf.model.ActivityWorkPackage;
import com.hcl.atf.taf.model.AuthenticationType;
import com.hcl.atf.taf.model.CommonActiveStatusMaster;
import com.hcl.atf.taf.model.EntityMaster;
import com.hcl.atf.taf.model.EntityUserGroup;
import com.hcl.atf.taf.model.Languages;
import com.hcl.atf.taf.model.OnboardUserRequestAccess;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.ProductTeamResources;
import com.hcl.atf.taf.model.ProductUserRole;
import com.hcl.atf.taf.model.TestfactoryResourcePool;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.UserRoleMaster;
import com.hcl.atf.taf.model.UserTypeMasterNew;
import com.hcl.atf.taf.model.VendorMaster;
import com.hcl.atf.taf.model.json.JsonOnboardUserRequestAccess;
import com.hcl.atf.taf.model.json.jtable.JTableResponse;
import com.hcl.atf.taf.model.json.jtable.JTableSingleResponse;
import com.hcl.atf.taf.service.ActivityWorkPackageService;
import com.hcl.atf.taf.service.EventsService;
import com.hcl.atf.taf.service.OnboardUserRequestAccessService;
import com.hcl.atf.taf.service.ProductListService;
import com.hcl.atf.taf.service.UserListService;

/**
 * @author silambarasur
 *
 */
@Controller
public class OnboardUserRequestAccessController {

	private static final Log log = LogFactory.getLog(OnboardUserRequestAccessController.class);

	@Autowired
	private OnboardUserRequestAccessService onboardUserRequestAccessService;
	
	@Autowired
	private UserListService userListService;
	
	@Autowired
	private ProductTeamResourcesDao productTeamResourcesDao;
	
	@Autowired
	private ProductListService productListService;
	
	@Autowired
	private ActivityWorkPackageService activityWorkPackageService;
	
	@Value("#{ilcmProps['fetch.active.directory.user.information']}")
	private String activitDirectoryLocation;
	
	@Autowired
	private EventsService eventsService;

	@RequestMapping(value = "onboard.user.request.access.list", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	JTableResponse getOnboardUserList(HttpServletRequest req, @RequestParam int status, @RequestParam int jtStartIndex,
			@RequestParam int jtPageSize) {
		JTableResponse jTableResponse = null;
		List<JsonOnboardUserRequestAccess> jsonOnboardUserRequestAccess = new ArrayList<JsonOnboardUserRequestAccess>();
		List<OnboardUserRequestAccess> onboardUserRequestAccesseList = null;
		try {
			onboardUserRequestAccesseList = onboardUserRequestAccessService.listOnboardUserRequestAccess(status,jtStartIndex,jtPageSize);
			if (onboardUserRequestAccesseList != null && onboardUserRequestAccesseList.size() > 0) {
				for (OnboardUserRequestAccess onboardUserRequestAccess : onboardUserRequestAccesseList) {
					jsonOnboardUserRequestAccess.add(new JsonOnboardUserRequestAccess(onboardUserRequestAccess));
				}
			}
			jTableResponse = new JTableResponse("OK", jsonOnboardUserRequestAccess,
					jsonOnboardUserRequestAccess.size());
			log.info("process fetching onboard user request access records");
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR",
					"Error fetching records!");
			log.error("JSON ERROR", e);
		}
		return jTableResponse;
	}
	
	@RequestMapping(value="onboard.user.request.access.approval", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody JTableResponse confirmOnboardUserApporval(HttpServletRequest request,@RequestParam Integer productId,@RequestParam String userName) {
		JTableResponse jTableResponse = null;
		List<JsonOnboardUserRequestAccess> jsonOnboardUserRequestAccessList= new ArrayList<>();
		try {
			ProductMaster product=null;
			ActivityWorkPackage activityWorkPackage=null;
			UserRoleMaster userRoleMaster=null;
			UserList dbUser=userListService.getUserByLoginId(userName);
			
			OnboardUserRequestAccess onboardUserRequestAccess=onboardUserRequestAccessService.getByOnboardUserRequestAccessByProductIdandUserName(productId, userName);
			UserList userList=new UserList();
			if(onboardUserRequestAccess != null) {
				product=onboardUserRequestAccess.getProduct();
				userRoleMaster=onboardUserRequestAccess.getUserRole();
				activityWorkPackage=onboardUserRequestAccess.getActivityWorkpackage();
				
				UserTypeMasterNew userTypeMasterNew = new UserTypeMasterNew();
				userTypeMasterNew.setUserTypeId(1);
				AuthenticationType userAuthenticationType=new AuthenticationType();
				userAuthenticationType.setAuthenticationTypeId(2);
			
				String defaultPassword="Idpa@123";
				TestfactoryResourcePool resourcePool= new TestfactoryResourcePool();
				resourcePool.setResourcePoolId(-10);
				VendorMaster vendor = new  VendorMaster();
				vendor.setVendorId(-10);
				String phoneno=ActiveDirectoryUtility.fetchActiveDirectoryUserInformation(activitDirectoryLocation,userName, " mobile");
				if(phoneno =="" || phoneno.isEmpty() ) {
					phoneno="9999999999";
				} else {
					userList.setContactNumber(phoneno);
				}
				userList.setEmailId(onboardUserRequestAccess.getEmailId());
				userList.setFirstName(userName);
				userList.setUserPassword(encrypt(defaultPassword));
				userList.setLoginId(userName);
				userList.setStatus(1);
				userList.setUserDisplayName(userName);
				userList.setUserRoleMaster(userRoleMaster);
				userList.setUserTypeMasterNew(userTypeMasterNew);
				userList.setAuthenticationType(userAuthenticationType);
				userList.setResourcePool(resourcePool);
				userList.setVendor(vendor);
				CommonActiveStatusMaster commonActiveStatusMaster= new CommonActiveStatusMaster();
				commonActiveStatusMaster.setStatus("ACTIVE");
				userList.setCommonActiveStatusMaster(commonActiveStatusMaster);
				Languages languages = new Languages();
				languages.setId(1);
				userList.setLanguages(languages);
				userListService.add(userList);
			} else {
				if(dbUser !=null) {
					userList = dbUser;
				}
			}
			Date plannedStartDate=null;
			Date plannedEndDate=null;
			if(productTeamResourcesDao.isExistsTeamResourceByUserIdandProductIdandUserId(product.getProductId(), userList.getUserId())) {
				jTableResponse = new JTableResponse("ERROR",
						"Onboard Already Exist!");
				return jTableResponse;
			}
			
			List<ProductTeamResources> productTeamUserList=productTeamResourcesDao.getProductTeamResourcesList(product.getProductId(), 0, 10000);
			
			if(productTeamUserList != null && productTeamUserList.size() >0) {
				for(ProductTeamResources productTeamUser:productTeamUserList) {
					plannedStartDate=productTeamUser.getFromDate();
					plannedEndDate=productTeamUser.getToDate();
					break;
				}
			}
			
			
			if(product != null && product.getProductId() != null) {
				ProductTeamResources productTeamUser = new ProductTeamResources();
				productTeamUser.setProductMaster(product);
				productTeamUser.setUserList(userList);
				productTeamUser.setFromDate(plannedStartDate == null?new Date():plannedStartDate);
				productTeamUser.setToDate(plannedEndDate == null ? new Date():plannedEndDate);
				productTeamUser.setProductSpecificUserRole(userRoleMaster);
				productTeamUser.setPercentageofallocation(100F);
				productTeamResourcesDao.addProductTeamResource(productTeamUser);
				ProductUserRole productUserPermission = new ProductUserRole();
				productUserPermission.setProduct(product);
				productUserPermission.setUser(userList);
				productUserPermission.setRole(userRoleMaster);
				productListService.addProductUserRole(productUserPermission);
			}
			
				EntityMaster entityMaster = new EntityMaster();
				entityMaster.setEntitymasterid(IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE_ID);
				if(activityWorkPackage != null && activityWorkPackage.getActivityWorkPackageId() != -1 ) {
						EntityUserGroup entityUserGroup = new EntityUserGroup();
						entityUserGroup.setEntityInstanceId(activityWorkPackage.getActivityWorkPackageId());
						entityUserGroup.setEntityTypeId(entityMaster);
						entityUserGroup.setUser(userList);
						entityUserGroup.setMappedBy(userList);
						entityUserGroup.setMappedDate(new Date());
						entityUserGroup.setProduct(product);
						userListService.mapOrUnmapEntityUserGroup(entityUserGroup, "map");				
				} else {
					
					List<ActivityWorkPackage>activityWorkPackages = activityWorkPackageService.getActivityWorkPackageListByProductId(product.getProductId());
					for(ActivityWorkPackage activityWP : activityWorkPackages){
						EntityUserGroup userGroup = new EntityUserGroup();
						userGroup.setEntityInstanceId(activityWP.getActivityWorkPackageId());
						userGroup.setMappedBy(userList);
						userGroup.setUser(userList);
						userGroup.setMappedDate(new Date());
						userGroup.setEntityTypeId(entityMaster);
						userGroup.setProduct(product);
						userListService.mapOrUnmapEntityUserGroup(userGroup,"map");
					}
				}
				UserList user = (UserList) request.getSession().getAttribute("USER");
				onboardUserRequestAccess.setApprovedBy(user);
				onboardUserRequestAccess.setStatus(1);
				onboardUserRequestAccessService.updateOnboardUserRequestAccess(onboardUserRequestAccess); 
				List<OnboardUserRequestAccess> onboardUserRequestAccessList= onboardUserRequestAccessService.listOnboardUserRequestAccess(-2,0,1000);
				if (onboardUserRequestAccessList != null && onboardUserRequestAccessList.size() > 0) {
					for (OnboardUserRequestAccess onboardUserRequest : onboardUserRequestAccessList) {
						jsonOnboardUserRequestAccessList.add(new JsonOnboardUserRequestAccess(onboardUserRequest));
					}
				}
				jTableResponse = new JTableResponse("OK",jsonOnboardUserRequestAccessList);
		}catch(Exception e) {
			jTableResponse = new JTableResponse("ERROR",
					"Error while creating onboarding user!");
			return jTableResponse;
		}
		
		return jTableResponse;
	}
	
	public String encrypt(String passwordToHash)
	{
	    String generatedPassword = null;
	    try {
	        MessageDigest md = MessageDigest.getInstance("MD5");
	        md.update(passwordToHash.getBytes());
	        byte[] bytes = md.digest();
	        StringBuilder sb = new StringBuilder();
	        for(int i=0; i< bytes.length ;i++)
	        {
	            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
	        }
	        generatedPassword = sb.toString();
	    }
	    catch (NoSuchAlgorithmException e)
	    {
	        log.error("Error", e);
	    }
	    
	    return generatedPassword;
	}
		
	@RequestMapping(value="onboard.user.request.access.approval.update",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableSingleResponse updateOnBoarduser(HttpServletRequest request, @ModelAttribute JsonOnboardUserRequestAccess jsonOnboardUserRequestAccess, BindingResult result) {
		log.debug("onboard.user.request.access.approval.update");
		JTableSingleResponse jTableSingleResponse;
	String onBoardUserName =	jsonOnboardUserRequestAccess.getOnboardUserName();
		
		
		if(result.hasErrors()){
			jTableSingleResponse = new JTableSingleResponse("ERROR","Invalid Form!"); 
		}			
		try {
			
			OnboardUserRequestAccess onboardUserRequestAccessFromUI = jsonOnboardUserRequestAccess.getOnboardUserRequestAccessList();
			
			onboardUserRequestAccessService.updateOnboardUserRequestAccess(onboardUserRequestAccessFromUI);
			
			jTableSingleResponse = new JTableSingleResponse("OK",new JsonOnboardUserRequestAccess(onboardUserRequestAccessFromUI));
	        } catch (Exception e) {
	        	jTableSingleResponse = new JTableSingleResponse("ERROR","Error updating Onboard User Request Access Approval!");
	            log.error("JSON ERROR updating Onboard User Request Access Approval", e);
	        }
        return jTableSingleResponse;
    }
	
	

	@RequestMapping(value="administration.onboard.user.reject",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse rejectUser(@RequestParam String onboardUserName, @RequestParam Integer status, @RequestParam String modifiedField,
			@RequestParam String modifiedFieldTitle, @RequestParam String oldFieldValue, @RequestParam String newFieldValue) {
		log.info("Inside administration.onboard.user.reject");
		JTableSingleResponse jTableSingleResponse;
		OnboardUserRequestAccess onBoardRequestAccess = null;
		String remarks = "";
		try {
			onBoardRequestAccess = onboardUserRequestAccessService.getByOnboardUserRequestAccessUserName(onboardUserName);			
			onBoardRequestAccess.setStatus(-3);
			onboardUserRequestAccessService.updateOnboardUserRequestAccess(onBoardRequestAccess);
			remarks = "OnBoardUser :"+onBoardRequestAccess.getOnboardUserName();			
			jTableSingleResponse = new JTableSingleResponse("OK", new JsonOnboardUserRequestAccess(onBoardRequestAccess),"Success");
		} catch (Exception e) {
			jTableSingleResponse = new JTableSingleResponse("ERROR", "Error in administration.onboard.user.reject");
			log.error("JSON ERROR", e);
		}
		return jTableSingleResponse;
	}
	
}
