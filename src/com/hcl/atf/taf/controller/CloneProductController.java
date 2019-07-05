/**
 * 
 */
package com.hcl.atf.taf.controller;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.controller.utilities.ActiveDirectoryUtility;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.dao.ActivityTypeDAO;
import com.hcl.atf.taf.dao.ProductBuildDAO;
import com.hcl.atf.taf.dao.ProductMasterDAO;
import com.hcl.atf.taf.dao.ProductTeamResourcesDao;
import com.hcl.atf.taf.dao.ProductUserRoleDAO;
import com.hcl.atf.taf.dao.ProductVersionListMasterDAO;
import com.hcl.atf.taf.model.ActivityMaster;
import com.hcl.atf.taf.model.ActivityWorkPackage;
import com.hcl.atf.taf.model.EntityMaster;
import com.hcl.atf.taf.model.EntityUserGroup;
import com.hcl.atf.taf.model.OnboardUserRequestAccess;
import com.hcl.atf.taf.model.ProductBuild;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.ProductTeamResources;
import com.hcl.atf.taf.model.ProductUserRole;
import com.hcl.atf.taf.model.ProductVersionListMaster;
import com.hcl.atf.taf.model.TestFactory;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.UserRoleMaster;
import com.hcl.atf.taf.mongodb.service.MongoDBService;
import com.hcl.atf.taf.service.ActivityWorkPackageService;
import com.hcl.atf.taf.service.OnboardUserRequestAccessService;
import com.hcl.atf.taf.service.ProductListService;
import com.hcl.atf.taf.service.UserListService;
import com.hcl.ilcm.workflow.service.WorkflowStatusPolicyService;

/**
 * @author silambarasur
 *
 */
@Controller
@Path("restAPIService")
public class CloneProductController {
	
	private static final Log log = LogFactory
			.getLog(CloneProductController.class);
	
	@Autowired
	private ProductMasterDAO productMasterDAO;
	
	@Autowired
	private ProductVersionListMasterDAO productVersionListMasterDAO;
	
	@Autowired
	private ProductBuildDAO productBuildDAO;
	
	@Autowired
	private ActivityWorkPackageService activityWorkPackageService;
	
	@Autowired
	private ActivityTypeDAO activityTypeDAO;
	
	@Autowired
	private ProductTeamResourcesDao productTeamResourcesDao;
	
	@Autowired
	private ProductUserRoleDAO productUserRoleDAO;
	
	@Autowired
	private UserListService userListService;
	
	@Autowired
	private WorkflowStatusPolicyService workflowStatusPolicyService;
	
	@Autowired
	private MongoDBService mongoDBService;
	
	@Autowired
	private ProductListService productListService;
	
	@Autowired
	private OnboardUserRequestAccessService onboardUserRequestAccessService;
	
	 private static String startHeaderTag="<h1 style='color: Red;font-size:15px;padding-bottom: 5%;padding-top: 5%;padding-left: 5%;'>";
	 
	 private static String endHeaderTag="</h1>";
	 
	@Value("#{ilcmProps['fetch.active.directory.user.information']}")
	private String activitDirectoryLocation;

	
	@GET
	@Path("/cloneProduct")
	public Response cloneProduct(@QueryParam("srcProductName") String srcProductName,@QueryParam("destProductName") String destProductName,@QueryParam("startDate") String startDate,@QueryParam("endDate") String endDate,
			
			@QueryParam("team") String team,@QueryParam("userpremssion") String userpremssion,@QueryParam("workflow") String workflow,@QueryParam("workpackage") String workpackage) {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
		
		List<ProductVersionListMaster> productVersionList=null;
		List<ProductBuild> productBuildList = null;
	    List<ActivityWorkPackage> activityWorkPackageList =null;
	    Integer productId=0;
	    Integer testFactoryId=0;
	    ProductMaster product=null;
	    Date plannedStartDate=null;
	    Date plannedEndDate=null;
 
	    try{
			
	    	if(startDate != "" && !startDate.isEmpty()){
				try {
				plannedStartDate=DateUtility.dateformatWithOutTime(startDate);
				}catch(Exception e) {
					return Response
							   .status(200)
							   .entity(startHeaderTag+"Please give valid date format example mm/dd/yyyy"+endHeaderTag).build();
				}
			} else {
				return Response
						   .status(200)
						   .entity(startHeaderTag+"Please enter Start Date"+endHeaderTag).build();
			}
			if(endDate != "" && !endDate.isEmpty()){
				try {
					plannedEndDate=DateUtility.dateformatWithOutTime(endDate);
				}catch(Exception e) {
					return Response
							   .status(200)
							   .entity(startHeaderTag+"Please give valid date formate example mm/dd/yyyy"+endHeaderTag).build();
				}
			} else {
				return Response
						   .status(200)
						   .entity(startHeaderTag+"Please enter End Date "+endHeaderTag).build();
			}
			
			UserList user = new UserList();
			user.setUserId(1);//Time being adding admin user for creating workpackage
			if(srcProductName !="" &&!srcProductName.isEmpty()) {
				product=productMasterDAO.getProductByName(srcProductName);
				
				if(product == null) {
					return Response
							   .status(200)
							   .entity("<h1 style='color: Red;font-size:15px;'>Please Enter available source product name ").build();
				}
			} else {
				return Response
						   .status(200)
						   .entity("<h1 style='color: Red;font-size:15px;'>Please enter source product name ").build();
			}
			
			
			
			if(destProductName !="" &&!destProductName.isEmpty()) {
				ProductMaster destinationProduct=productMasterDAO.getProductByNameAndTestfactoryId(destProductName, product.getTestFactory().getTestFactoryId());
				if(destinationProduct != null) {
					return Response
							   .status(200)
							   .entity("<h1 style='color: Red;font-size:15px;'>Destination product already exist ").build();
				}
			} else {
				return Response
						   .status(200)
						   .entity("<h1 style='color: Red;font-size:15px;'>Please enter destination product name ").build();
			}
			
			
			if(product != null && product.getProductId() != null) {
				productId=product.getProductId();
				TestFactory testFactory= product.getTestFactory();
				product.setProductId(null);
				product.setProductName(destProductName);
				product.setProductDescription(destProductName);
				product.setProjectCode(destProductName);
				product.setTestFactory(testFactory);				
				productMasterDAO.add(product);
				
				if(team != "" && !team.isEmpty()){
					List<ProductTeamResources> productTeamUserList=productTeamResourcesDao.getProductTeamResourcesList(productId, 0, 10000);
					
						if(productTeamUserList != null && productTeamUserList.size() >0) {
							for(ProductTeamResources productTeamUser:productTeamUserList) {
								productTeamUser.setProductTeamResourceId(null);
								productTeamUser.setProductMaster(product);
								productTeamUser.setFromDate(plannedStartDate);
								productTeamUser.setToDate(plannedEndDate);
								productTeamResourcesDao.addProductTeamResource(productTeamUser);
							}
						}
				}
				if(userpremssion != null && !userpremssion.isEmpty()){
					List<ProductUserRole> productUserRole=productListService.listProductUserRole(productId,0,10000);
					
					if(productUserRole !=null && productUserRole.size() >0) {
						for(ProductUserRole productUserPermission:productUserRole) {
							productUserPermission.setProductUserRoleId(null);
							productUserPermission.setProduct(product);
							productListService.addProductUserRole(productUserPermission);
						}
					}
				}
				
				
				List<ActivityMaster> activityTypeList=activityTypeDAO.listActivityTypes(testFactoryId, productId, 0, 1000, 1, false);
				
				if (activityTypeList != null && activityTypeList.size() >0) {
					for(ActivityMaster activityType:activityTypeList) {
						activityType.setActivityMasterId(null);
						activityType.setProductMaster(product);
						activityType.setTestFactory(testFactory);
						activityTypeDAO.addActivityMaster(activityType);
					}
				}
				
				
				int actWPId = 0;
				productVersionList=productVersionListMasterDAO.list(productId);
				if(productVersionList != null && productVersionList.size() >0) {
					Integer productVersionId=0;
					for(ProductVersionListMaster productVersion:productVersionList) {
						productVersionId=productVersion.getProductVersionListId();
						productVersion.setProductVersionListId(null);
						productVersion.setProductMaster(product);
						productVersionListMasterDAO.add(productVersion);
						if(productVersionId !=null && productVersionId >0) {
							productBuildList=productBuildDAO.list(productVersionId);
							
							if(productBuildList != null && productBuildList.size() >0) {
								Integer buildId=0;
								for(ProductBuild build:productBuildList) {
									buildId=build.getProductBuildId();
									build.setProductBuildId(null);
									build.setProductVersion(productVersion);
									build.setProductMaster(product);
									productBuildDAO.add(build);
									if(workpackage !=null && !workpackage.isEmpty()) {
									activityWorkPackageList =activityWorkPackageService.getActivityWorkPackageByBuildId(buildId);
									
									if(activityWorkPackageList != null && activityWorkPackageList.size() >0 && (workpackage != "" && !workpackage.isEmpty())) {
										for(ActivityWorkPackage activityWorkPackage:activityWorkPackageList) {
											actWPId = activityWorkPackage.getActivityWorkPackageId();
											activityWorkPackage.setActivityWorkPackageId(null);
											activityWorkPackage.setProductBuild(build);
											activityWorkPackage.setProductMaster(product);
											activityWorkPackage.setPlannedStartDate(plannedStartDate);
											activityWorkPackage.setPlannedEndDate(plannedEndDate);
											activityWorkPackage.setTotalEffort(0);
											activityWorkPackage.setActualStartDate(null);
											activityWorkPackageService.addActivityWorkPackage(activityWorkPackage);
											
											if(activityWorkPackage !=null){
												
												EntityMaster entityMaster = new EntityMaster();
												entityMaster.setEntitymasterid(IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE_ID);
												
												List<EntityUserGroup> userGroupList = userListService.getEntityUserGroupByEntityId(IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE_ID,actWPId);
												
												for(EntityUserGroup userGroup : userGroupList){
													EntityUserGroup entityUserGroup = new EntityUserGroup();
													entityUserGroup.setEntityInstanceId(activityWorkPackage.getActivityWorkPackageId());
													entityUserGroup.setEntityTypeId(entityMaster);
													entityUserGroup.setUser(userGroup.getUser());
													entityUserGroup.setMappedBy(user);
													entityUserGroup.setMappedDate(new Date());
													entityUserGroup.setProduct(product);
													userListService.mapOrUnmapEntityUserGroup(entityUserGroup, "map");
												}
												
											}
											
											if(activityWorkPackage!=null){
												mongoDBService.addActivityWorkPackagetoMongoDB(activityWorkPackage.getActivityWorkPackageId());
											}
											
											if(activityWorkPackage != null && activityWorkPackage.getActivityWorkPackageId() != null){
												workflowStatusPolicyService.instanciateWorkflowStatusAndPoliciesForInstanceByWorkflowId(build.getProductVersion().getProductMaster().getProductId(), IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE_ID, null, activityWorkPackage.getActivityWorkPackageId(), null, null, user, activityWorkPackage.getPlannedStartDate(), activityWorkPackage);
											}
											
										}
									}
									
								}
								
								}
							}
						
						}
					}
				}
				
				
				
			}else {
				return Response
						   .status(200)
						   .entity("<h1 style='color: Red;font-size:15px;'>Please enter Existing product name ").build();			
			}
			
			
		}catch(Exception e){
			log.error("process.activityProduct.clone...",e);
		}
		
		
			return Response
			   .status(200)
			   .entity("<h1 style='color: Green;font-size:15px;'>Destination product Cloned Successfully ").build();			

		}




	@GET
	@Path("/createOnboardUser")
	public Response createOnboardUser(@QueryParam("emailId") String emailId,
			@QueryParam("workpackageName") String workpackageName,
			@QueryParam("productName") String productName) {
	SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	try {
		if(emailId == "" || emailId.isEmpty() ) {
			return Response
					   .status(200)
					   .entity(startHeaderTag+"Please enter EmailId"+endHeaderTag).build();
		}
		if(!validateEmailAddress(emailId)) {
			return Response
					   .status(200)
					   .entity(startHeaderTag+"Please enter valid EmailId"+endHeaderTag).build();
		}
		
		String userName[]=emailId.split("@");
		String user="";
		if(userName !=null && userName.length >0) {
			user=userName[0];
		}
		
		
		if(user == "" || user.isEmpty() ) {
			return Response
					   .status(200)
					   .entity(startHeaderTag+"Please enter valid EmailId "+endHeaderTag).build();
		}
		
		
		String role=ActiveDirectoryUtility.fetchActiveDirectoryUserInformation(activitDirectoryLocation,user, " title");
		
		if(role =="" || role.isEmpty()) {
			role="TECHNICAL MANAGER";
		}
		
		
		String ilcmRole=roleConversion(role); 
		
		UserRoleMaster userRoleMaster=userListService.getRoleByLabel(ilcmRole);
		if(userRoleMaster == null) {
			return Response
					   .status(200)
					   .entity(startHeaderTag+"Please enter available EmailId "+endHeaderTag).build();
			
		}
		
		ProductMaster product=null;
		if(productName !="" &&!productName.isEmpty()) {
			product=productMasterDAO.getProductByName(productName);
			if(product == null) {
				return Response
						.status(200)
						.entity(startHeaderTag+"Please enter available product name </h1>").build();
			}
		} else {
			return Response
					   .status(200)
					   .entity(startHeaderTag+"Please enter Product name "+endHeaderTag).build();
		}
		ActivityWorkPackage activityWorkPackage =null;
		if(workpackageName == "" || workpackageName.isEmpty()) {
			return Response
					.status(200)
					.entity(startHeaderTag+"Please enter workpackage name </h1>").build();
		} else {
			 activityWorkPackage = activityWorkPackageService.getLastestActivityWorkPackageByNameInProduct(workpackageName, product.getProductId());
			if(activityWorkPackage == null && workpackageName.equals("ALL")) {
				activityWorkPackage = new ActivityWorkPackage();
				activityWorkPackage.setActivityWorkPackageId(-1);
				activityWorkPackage.setActivityWorkPackageName("System Workpackage");
				activityWorkPackage.setIsActive(-1);
			} else if(activityWorkPackage == null && !workpackageName.equals("ALL")){
				return Response
						.status(200)
						.entity(startHeaderTag+"Please enter available workpackage name </h1>").build();
			}
			
		}
		
		OnboardUserRequestAccess dbOnboardUserRequestAccess = onboardUserRequestAccessService.getByOnboardUserRequestAccessByProductIdandUserName(product.getProductId(), user);
		if(dbOnboardUserRequestAccess == null) {
			OnboardUserRequestAccess onboardUserRequestAccess= new OnboardUserRequestAccess();
			onboardUserRequestAccess.setEmailId(emailId);
			onboardUserRequestAccess.setOnboardUserName(user);
			onboardUserRequestAccess.setTestFactory(product.getTestFactory());
			onboardUserRequestAccess.setProduct(product);
			onboardUserRequestAccess.setActivityWorkpackage(activityWorkPackage);
			onboardUserRequestAccess.setUserRole(userRoleMaster);
			onboardUserRequestAccess.setStatus(-2);
			onboardUserRequestAccessService.addOnboardUserRequestAccess(onboardUserRequestAccess);
		} else {
			return Response.status(200).entity(startHeaderTag+"User already Onboared "+endHeaderTag).build();
		}
		
	}catch(Exception e) {
		return Response
				.status(200)
				.entity(startHeaderTag+"User Onboard failure </h1>").build();
	}
	return Response
			.status(200)
			.entity("<h1 style='padding-bottom: 5%;padding-top: 5%;padding-left: 5%;color: Green;'>User Onboard Successfully </h1>").build();
}



	private String roleConversion(String role) {
		String ilcmRole="";
		switch(role.trim().toUpperCase()) {
		
		case "SOFTWARE ENGINEER" :
			ilcmRole="Tester ";
			break;
		case "LEAD ENGINEER" :
			ilcmRole="Test Lead";
			break;
		case "SENIOR SOFTWARE ENGINEER" :
			ilcmRole="Test Lead";
			break;
		case "SENIOR TEST ENGINEER" :
			ilcmRole="Test Lead";
			break;	
		case "TEST LEAD" :
			ilcmRole="Test Lead";
			break;
		case "TECHNICAL LEAD" :
			ilcmRole="Test Lead";
			break;
		case "SENIOR TECHNICAL LEAD" :
			ilcmRole="Test Lead";
			break;
		case "PROJECT MANAGER" :
			ilcmRole="Test Manager";
			break;
		case "TECHNICAL MANAGER" :
			ilcmRole="Test Manager";
			break;
		case "SENIOR TECHNICAL MANAGER" :
			ilcmRole="Test Manager";
			break;
		case "ASSOCIATE GENERAL MANAGER" :
			ilcmRole="Test Manager";
			break;
		case "GENERAL MANAGER" :
			ilcmRole="Test Manager";
			break;
		case "DEPUTY GENERAL MANAGER":
			ilcmRole="Test Manager";
			break;
		case "MANAGER" :
			ilcmRole="Test Manager";
		break;		
		
		}
		
		return ilcmRole;
	}

		private Pattern regexPattern;
	    private Matcher regMatcher;

	    public boolean validateEmailAddress(String emailAddress) {

	        regexPattern = Pattern.compile("^[(a-zA-Z-0-9-\\_\\+\\.)]+@[(a-z-A-z)]+\\.[(a-zA-z)]{2,3}$");
	        regMatcher   = regexPattern.matcher(emailAddress);
	        if(regMatcher.matches()) {
	            return true;
	        } else {
	            return false;
	        }
	    }
	    
	    
	    @GET
		@Path("/add.newBuild.to.product")
	    @Produces(MediaType.APPLICATION_JSON)
	    public Response addNewBuildToProduct(@QueryParam("productId") Integer productId, @QueryParam("versionId") Integer versionId,  @QueryParam("buildName") String buildName, @QueryParam("description") String description) {
			SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
			try {
				ProductBuild productBuild = new ProductBuild();
				ProductMaster product= new ProductMaster();
				productBuild.setBuildname(buildName);
				productBuild.setStatus(1);
				productBuild.setBuildDescription(description);
				if(productId != null && productId >0) {
					product.setProductId(productId);
					productBuild.setProductMaster(product);
				}
				if(versionId != null && versionId >0) {
					ProductVersionListMaster version = new ProductVersionListMaster();
					version.setProductVersionListId(versionId);
					productBuild.setProductVersion(version);
				}
				
				productBuild.setCreatedDate( new Date());
				productBuild.setModifiedDate(new Date());
				productBuildDAO.add(productBuild);
				return Response.status(200).entity("ProductBuild Successfully Added:"+productBuild.getProductBuildId().toString()).build();
			} catch(Exception e){
				log.error("Error in addNewBuildToProduct",e);
			}
			return Response.status(200).entity("ProductBuild Successfully Added").build();
			}

} 