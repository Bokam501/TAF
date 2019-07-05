package com.hcl.atf.taf.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionException;
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
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.dao.ProductMasterDAO;
import com.hcl.atf.taf.model.DashBoardTabs;
import com.hcl.atf.taf.model.DashBoardTabsRoleBasedURL;
import com.hcl.atf.taf.model.DashboardVisualizationUrls;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.ProductUserRole;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.UserRoleMaster;
import com.hcl.atf.taf.model.json.JsonDashBoard;
import com.hcl.atf.taf.model.json.JsonDashBoardTabs;
import com.hcl.atf.taf.model.json.JsonDashBoardTabsRoleBasedURL;
import com.hcl.atf.taf.model.json.JsonDashBoardVisualizationURL;
import com.hcl.atf.taf.model.json.jtable.JTableResponse;
import com.hcl.atf.taf.model.json.jtable.JTableResponseOptions;
import com.hcl.atf.taf.model.json.jtable.JTableSingleResponse;
import com.hcl.atf.taf.mongodb.service.MongoDBService;
import com.hcl.atf.taf.scheduler.jobs.SLATrendJob;
import com.hcl.atf.taf.service.CustomerService;
import com.hcl.atf.taf.service.DashBoardService;
import com.hcl.atf.taf.service.EventsService;
import com.hcl.atf.taf.service.ProductListService;
import com.hcl.atf.taf.service.UserListService;
import com.hcl.atf.taf.service.WorkPackageService;
@Controller
public class DashBoardController {
	private static final Log log = LogFactory.getLog(DashBoardController.class);
	
	@Autowired
	private CustomerService customerService;
	@Autowired
	private ProductListService productListService;
	@Autowired
	private UserListService userListService;
	
	@Autowired
	private SLATrendJob slaTrendJob;
	
	@Autowired
	private WorkPackageService workPackageService;
	@Autowired
	private DashBoardService dashBoardService;
	@Autowired
	private ProductMasterDAO productMasterDAO;
	@Autowired
	private MongoDBService mongoDBService;
	@Autowired
	private EventsService eventsService;
	
	@Value("#{ilcmProps['COMPETANCY_PROJECT_BCWS']}")
    private Integer COMPETANCY_PROJECT_BCWS;
	
	@Value("#{ilcmProps['COMPETANCY_PROJECT_L0_COUNT']}")
    private Integer competancyProjectL0Count;
	@Value("#{ilcmProps['COMPETANCY_PROJECT_L1_COUNT']}")
    private Integer competancyProjectL1Count;
	@Value("#{ilcmProps['COMPETANCY_PROJECT_L2_COUNT']}")
    private Integer competancyProjectL2Count;
	
	@Value("#{ilcmProps['COMPETANCY_PROJECT_STARTDATE']}")
    private String projectStartDate;
	
	@Value("#{ilcmProps['SV_WEIGHTAGE']}")
    private Integer svWeightage;
	
	@Value("#{ilcmProps['PRODUCT_QUALITY_WEIGHTAGE']}")
    private Integer productQualityWeightage;
	
	@Value("#{ilcmProps['RISK_REMOVAL_WEIGHTAGE']}")
    private Integer riskRemovalIndexWeightage;
	
	@Value("#{ilcmProps['UTILIZATION_GLB_ACTIVITYNAME']}")
    private String utilizationGlb;
	
	@Value("#{ilcmProps['KIBANA_STATUS_REQUIRED']}")
    private String kibanaStatusRequired;
	
	@RequestMapping(value="dashboard.metrics.sla.calculation",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse dashboardCalculationSla() {
		JTableSingleResponse jTSingleResponse;
		try {	
			log.info("dashboard.metrics.sla.calculation");
			
			List<Integer> productIdList = new ArrayList<Integer>();
			List<String> statusList = new ArrayList<String>();
			statusList.add("Closed");
			
			Float scheduleVariance = 0.0F;
			Float productQualityIndex = 0.0F;
			Float riskIndex=0.9f;
			Float utlizationIndex=80F;
			Float healthIndex=99F;
			Float scheduleVarianceTarget = 0F;
			Float productQualityIndexTarget = 96F;
			Float riskIndexTarget = 1F ;
			Float utlizationIndexTarget = 85F ;
			Float healthIndexTarget = 98F;
			
			scheduleVariance =	mongoDBService.getScheduleVarienceData(productIdList);
			productQualityIndex = mongoDBService.getProductQualityIndexData(productIdList, statusList);
			
			
			
			jTSingleResponse = new JTableSingleResponse("OK",new JsonDashBoard(scheduleVariance, productQualityIndex, riskIndex, utlizationIndex, healthIndex, scheduleVarianceTarget, productQualityIndexTarget, riskIndexTarget, utlizationIndexTarget, healthIndexTarget));	
        } catch (Exception e) {
        	jTSingleResponse = new JTableSingleResponse("ERROR fetching DashboardCalculationSla");
            log.error("JSON ERROR fetching DashboardCalculationSla", e);	            
        }
        return jTSingleResponse;
    }
	
	
	
	@RequestMapping(value="dashboard.metrics.summary.calculation",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse dashboardSummaryCalculation(@RequestParam  String startDate,@RequestParam String endDate,@RequestParam  String testFactoryName,@RequestParam  String productName,HttpServletRequest req) {
		JTableResponse jTableResponse = null;
		
		try {
			
			Date dateRangeStart=null;
		    Date dateRangeEnd=null;
			
			if(startDate!=null &&!startDate.isEmpty() && endDate!=null && !endDate.isEmpty() ){
				dateRangeStart=DateUtility.dateformatWithOutTime(startDate);
				dateRangeEnd=DateUtility.dateformatWithOutTime(endDate);
			}
			UserList user = new UserList();
			user = (UserList)req.getSession().getAttribute("USER");
			slaTrendJob.execute(null);
			jTableResponse= dashBoardService.dashboardMetricsSummaryCalculation(dateRangeStart,dateRangeEnd,testFactoryName,productName,user);
			
		}catch (JobExecutionException e) {
			log.error("JSON Error fetching DashboardSummaryCalculation",e);
		}
		return jTableResponse;
	}
	
	
	@RequestMapping(value="sla.dashboard.metrics.summary.calculation",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse slaDashboardSummaryCalculation(@RequestParam  String startDate,@RequestParam String endDate,@RequestParam  String testFactoryName,@RequestParam  String productName,HttpServletRequest req) {
		JTableResponse jTableResponse = null;
		
		try {
			Date dateRangeStart=null;
		    Date dateRangeEnd=null;
			
			if(startDate!=null &&!startDate.isEmpty() && endDate!=null && !endDate.isEmpty() ){
				dateRangeStart=DateUtility.dateformatWithOutTime(startDate);
				dateRangeEnd=DateUtility.dateformatWithOutTime(endDate);
			}
			
			UserList user = new UserList();
			user = (UserList)req.getSession().getAttribute("USER");
			jTableResponse= dashBoardService.slaDashboardMetricsSummaryCalculation(dateRangeStart,dateRangeEnd, testFactoryName, productName, user);
			
		} catch (Exception e) {
			log.error(e);
		}
		return jTableResponse;
	}
	
	
	@RequestMapping(value="standard.dashboard.summary.calculation",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse standardDashboardSummaryCalculation(@RequestParam  String startDate,@RequestParam String endDate,@RequestParam  String testFactoryName,@RequestParam  String productName,HttpServletRequest req) {
		JTableResponse jTableResponse = null;
		try {

			UserList user = new UserList();
			user = (UserList)req.getSession().getAttribute("USER");
			
			Date dateRangeStart=null;
		    Date dateRangeEnd=null;
			
			if(startDate!=null &&!startDate.isEmpty() && endDate!=null && !endDate.isEmpty() ){
				dateRangeStart=DateUtility.dateformatWithOutTime(startDate);
				dateRangeEnd=DateUtility.dateformatWithOutTime(endDate);
			}
			
			jTableResponse= dashBoardService.standardDashboardCalculation(dateRangeStart,dateRangeEnd, testFactoryName, productName, user);
			
		} catch (Exception e) {
        	jTableResponse = new JTableResponse("Error Fetching standardDashboardSummary Calculation");
            log.error("JSON ERROR Fetching StandardDashboardSummary Calculation", e);	            
        }
	        
		return jTableResponse;
    }
	
	@RequestMapping(value="administration.dashboardTab.add",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse addDashboardTabs(HttpServletRequest req, @ModelAttribute JsonDashBoardTabs jsonDashBoardTabs, BindingResult result) {
		log.info("Inside dashboard tabs Add ");
		JTableSingleResponse jTableSingleResponse;
		if(result.hasErrors()){
			jTableSingleResponse  = new JTableSingleResponse("ERROR","Invalid form!"); 
			return jTableSingleResponse;
		}		
		try {   	
				DashBoardTabs dashBoardTabs= jsonDashBoardTabs.getDashBoardTabs();
				List<DashBoardTabs> dashBoardTabList= null;
				log.info("Engaement "+jsonDashBoardTabs.getEngagementId());
				
				dashBoardTabList=dashBoardService.getDasboardTabsByEngagementId(jsonDashBoardTabs.getEngagementId());
				for(DashBoardTabs tabs:dashBoardTabList){
					if(tabs!=null){
						if(jsonDashBoardTabs.getTabName().equalsIgnoreCase(tabs.getTabName())){
							jTableSingleResponse = new JTableSingleResponse("ERROR","Already same tab is available for '"+tabs.getEngagement().getTestFactoryName()+"' !");
							return jTableSingleResponse;							
						}
					}
				}				
				dashBoardTabs.setStatus(1);
				dashBoardService.addDashboardTabsToUI(dashBoardTabs);
				if(dashBoardTabs != null && dashBoardTabs.getTabId() != null){
					UserList user=(UserList)req.getSession().getAttribute("USER");
					eventsService.addNewEntityEvent(IDPAConstants.ENTITY_DASHBOARD_URLS, dashBoardTabs.getTabId(), dashBoardTabs.getTabName(), user);
				}				
				jTableSingleResponse = new JTableSingleResponse("OK",new JsonDashBoardTabs(dashBoardTabs));		
	        } catch (Exception e) {
	            jTableSingleResponse = new JTableSingleResponse("ERROR","Error addDashboardTabs record!");
	            log.error("JSON ERROR adding DashboardTabs", e);
	        }
        return jTableSingleResponse;
    }
	
	@RequestMapping(value="administration.dashboardTab.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listDashboardTabs(@RequestParam Integer status,@RequestParam int jtStartIndex, @RequestParam int jtPageSize) {
		log.debug("inside administration.dashboardTab.list");
		JTableResponse jTableResponse;
		 try{			 
			List<DashBoardTabs> dashBoardTabList=dashBoardService.listDashboardTabs(status,jtStartIndex,jtPageSize);
			
			List<JsonDashBoardTabs> jsonDashBoardTabs=new ArrayList<JsonDashBoardTabs>();
			for(DashBoardTabs tab: dashBoardTabList){				
				jsonDashBoardTabs.add(new JsonDashBoardTabs(tab));				
			}
            jTableResponse = new JTableResponse("OK", jsonDashBoardTabs,jsonDashBoardTabs.size());     
            dashBoardTabList = null;	
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Unable to show TabList!");
	            log.error("JSON ERROR", e);
	        }
        return jTableResponse;
    }
	
	
	
	@RequestMapping(value="administration.dashboardTab.update",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableSingleResponse updateDashboardTab(HttpServletRequest req, @ModelAttribute JsonDashBoardTabs jsonDashBoardTabs,BindingResult result) {
		JTableSingleResponse jTableSingleResponse = null;
		String remarks = "";
			
		DashBoardTabs dashBoardTabsFromUI = null;
		try {
			dashBoardTabsFromUI= jsonDashBoardTabs.getDashBoardTabs();
			List<DashBoardTabs> dashBoardTabList= null;
			dashBoardTabList=dashBoardService.getDasboardTabsByEngagementId(jsonDashBoardTabs.getEngagementId());
			
			for(DashBoardTabs tabs:dashBoardTabList){
				if(tabs!=null){
					if(jsonDashBoardTabs.getTabName().equalsIgnoreCase(tabs.getTabName()) && jsonDashBoardTabs.getTabId()!=tabs.getTabId()){
						jTableSingleResponse = new JTableSingleResponse("ERROR","Already same tab is available for '"+tabs.getEngagement().getTestFactoryName()+"' !");
						return jTableSingleResponse;
					}
				}
			}
			dashBoardService.update(dashBoardTabsFromUI);
			if(dashBoardTabsFromUI != null && dashBoardTabsFromUI.getTabId() != null){
				UserList user=(UserList)req.getSession().getAttribute("USER");
				remarks = "TestFactory :"+dashBoardTabsFromUI.getEngagement().getTestFactoryName()+", DashBoardTab :"+dashBoardTabsFromUI.getTabName();
				eventsService.addEntityChangedEvent(IDPAConstants.ENTITY_DASHBOARD_URLS, dashBoardTabsFromUI.getTabId(), dashBoardTabsFromUI.getTabName(),
						jsonDashBoardTabs.getModifiedField(), jsonDashBoardTabs.getModifiedFieldTitle(),
						jsonDashBoardTabs.getOldFieldValue(), jsonDashBoardTabs.getModifiedFieldValue(), user, remarks);
			}
				jTableSingleResponse = new JTableSingleResponse("OK",new JsonDashBoardTabs(dashBoardTabsFromUI));		
		    } catch (Exception e) {
		    	jTableSingleResponse = new JTableSingleResponse("ERROR","Unable to update the tab!");
	            log.error("JSON ERROR", e);
	        }
        return jTableSingleResponse;
    }
	
	@RequestMapping(value="administration.dashboardTabRoleBased.add",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse addDashboardTabsRole(HttpServletRequest req, @ModelAttribute JsonDashBoardTabsRoleBasedURL jsonDashBoardTabsRoleBasedURL, BindingResult result) {
		log.info("Dashboard tabs RoleBased Add ");
		JTableSingleResponse jTableSingleResponse;
		/*if(result.hasErrors()){
			jTableSingleResponse  = new JTableSingleResponse("ERROR","Invalid form!"); 
			return jTableSingleResponse;
		}*/		
		try {   	
				DashBoardTabsRoleBasedURL dashBoardTabsRoleBasedURL= jsonDashBoardTabsRoleBasedURL.getDashBoardTabsRoleBasedURL();	
				//UserRoleMaster userRole=userListService.getRoleByLabel(jsonDashBoardTabsRoleBasedURL.getProductSpecificUserRoleName());
				UserRoleMaster userRole=userListService.getRoleById(jsonDashBoardTabsRoleBasedURL.getProductSpecificUserRoleId());
				dashBoardTabsRoleBasedURL.setProductSpecificUserRole(userRole);
				dashBoardService.addDashboardTabsRoleUrl(dashBoardTabsRoleBasedURL);
				if(dashBoardTabsRoleBasedURL != null && dashBoardTabsRoleBasedURL.getRoleBasedId() != null){
					UserList user=(UserList)req.getSession().getAttribute("USER");
					//String rolebasedDashboardchange = dashBoardTabsRoleBasedURL.getDashBoardTabs() !=null ? dashBoardTabsRoleBasedURL.getDashBoardTabs().getTabName():"" +"~"+ dashBoardTabsRoleBasedURL.getProductSpecificUserRole() != null ?dashBoardTabsRoleBasedURL.getProductSpecificUserRole().getRoleName():"";
					String rolebasedDashboardchange = "Create user role based tab";
					eventsService.addNewEntityEvent(IDPAConstants.ENTITY_DASHBOARD_TABS_ROLEBASED, dashBoardTabsRoleBasedURL.getRoleBasedId(), rolebasedDashboardchange, user);
				}	
					jTableSingleResponse = new JTableSingleResponse("OK",new JsonDashBoardTabsRoleBasedURL(dashBoardTabsRoleBasedURL));		
	        } catch (Exception e) {
	            jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding role record!");
	            log.error("JSON ERROR adding DashboardTabsRole", e);
	        }
        return jTableSingleResponse;
    }
	
	/*@RequestMapping(value="administration.dashboardTabRoleBased.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listDashboardTabsRoleBasedURL(HttpServletRequest req,@RequestParam Integer tabId) {
		log.debug("inside administration.dashboardTab RoleBased.list");
		JTableResponse jTableResponse;
		 try{
			 String modifiedUrl="";
			 String productName="";
			 String productDetails="";
			 String testFactoryName="";
			 String testFactoryDetails = "";
			 List<Integer> testFactories = new ArrayList<Integer>();
			 UserList user = new UserList();
			user = (UserList)req.getSession().getAttribute("USER");
			List<ProductMaster>productList=new ArrayList<ProductMaster>();
			if(user.getUserRoleMaster().getUserRoleId() == IDPAConstants.ROLE_ID_ADMIN){
				productName = "*";
				testFactoryName = "*";
				productList=productListService.listProduct();
				for(ProductMaster product : productList){
					
					if(product.getTestFactory()!= null){
						if(!testFactories.contains(product.getTestFactory().getTestFactoryId())){
							if(testFactoryDetails==null||testFactoryDetails.trim().isEmpty()){
								testFactoryDetails = product.getTestFactory().getTestFactoryId()+"~~~"+product.getTestFactory().getTestFactoryName();
							}else{
								testFactoryDetails += ","+product.getTestFactory().getTestFactoryId()+"~~~"+product.getTestFactory().getTestFactoryName();
							}
							testFactories.add(product.getTestFactory().getTestFactoryId());
						}
					}
					
					
				}
			}else{
				
				Set<ProductUserRole> userProducts = user.getProductUserRoleSet();
				if(userProducts != null && userProducts.size() > 0){
					for(ProductUserRole productUserRole: userProducts){
						
						if(productUserRole != null && productUserRole.getStatus() >0) {
							if(productName == null || productName.trim().isEmpty()){
								productName = "productName: "+productUserRole.getProduct().getProductName();
							}else{
								productName += " OR "+productUserRole.getProduct().getProductName();
							}
							
							if(!testFactories.contains(productUserRole.getProduct().getTestFactory().getTestFactoryId())){
								if(testFactoryDetails==null||testFactoryDetails.trim().isEmpty()){
									testFactoryName = "testFactoryName: "+productUserRole.getProduct().getTestFactory().getTestFactoryName();
									testFactoryDetails = productUserRole.getProduct().getTestFactory().getTestFactoryId()+"~~~"+productUserRole.getProduct().getTestFactory().getTestFactoryName();
								}else{
									testFactoryName += " OR "+productUserRole.getProduct().getTestFactory().getTestFactoryName();
									testFactoryDetails += ","+productUserRole.getProduct().getTestFactory().getTestFactoryId()+"~~~"+productUserRole.getProduct().getTestFactory().getTestFactoryName();
								}
								testFactories.add(productUserRole.getProduct().getTestFactory().getTestFactoryId());
							}
						}	
					}
				}else{
					productName = "";
				}
				
			}
			if(productName != null && !productName.trim().isEmpty()){
				productName = productName.replaceAll(" ", "%20");
			}
			if(testFactoryName != null && !testFactoryName.trim().isEmpty()){
				testFactoryName = testFactoryName.replace(" ", "%20");
			}
			
			List<DashBoardTabsRoleBasedURL> dashBoardTabsRoleBasedURLList=dashBoardService.listDashboardTabsRoleBasedURL(user.getUserRoleMaster().getUserRoleId(),tabId);
			
			
			List<JsonDashBoardTabsRoleBasedURL> jsonDashBoardTabsRoleBasedURL=new ArrayList<JsonDashBoardTabsRoleBasedURL>();
			
			for(DashBoardTabsRoleBasedURL roleList: dashBoardTabsRoleBasedURLList){
				modifiedUrl=roleList.getUrl().getUrl();
				if("YES".equalsIgnoreCase(kibanaStatusRequired)){
					modifiedUrl=	modifiedUrl.replace("query:'*", "query:'status:%22Active%22%20AND%20parentStatus:%22Active%22%20AND%20("+testFactoryName+")%20AND%20("+productName+")");
				}else{
					modifiedUrl=	modifiedUrl.replace("query:'*", "query:'("+testFactoryName+")%20AND%20("+productName+")");
				}
				roleList.getUrl().setUrl(modifiedUrl);
				JsonDashBoardTabsRoleBasedURL	jsonDashBoardTabsRoleBased=new JsonDashBoardTabsRoleBasedURL(roleList);
				jsonDashBoardTabsRoleBased.setTestFactoryDetails(testFactoryDetails);
				jsonDashBoardTabsRoleBased.setProductDetails(productDetails);
				jsonDashBoardTabsRoleBasedURL.add(jsonDashBoardTabsRoleBased);
				
			}
            jTableResponse = new JTableResponse("OK", jsonDashBoardTabsRoleBasedURL,jsonDashBoardTabsRoleBasedURL.size());     
            dashBoardTabsRoleBasedURLList = null;
		 
			
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Unable to show TabList Role list!");
	            log.error("JSON ERROR", e);
	        }
	        
        return jTableResponse;
    }
	*/
	
	@RequestMapping(value="administration.dashboardTabRoleBased.update",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableSingleResponse updateDashboardTabRoleBased(HttpServletRequest req, @ModelAttribute JsonDashBoardTabsRoleBasedURL jsonDashBoardTabsRoleBasedURL, BindingResult result) {
		JTableSingleResponse jTableSingleResponse = null;
		String remarks = "";
		DashBoardTabsRoleBasedURL dashBoardTabsRoleBasedFromUI = null;
		try {
			dashBoardTabsRoleBasedFromUI= jsonDashBoardTabsRoleBasedURL.getDashBoardTabsRoleBasedURL();
					
			dashBoardService.update(dashBoardTabsRoleBasedFromUI);	
			if(dashBoardTabsRoleBasedFromUI != null && dashBoardTabsRoleBasedFromUI.getRoleBasedId() != null){
				UserList user=(UserList)req.getSession().getAttribute("USER");
				String rolebasedDashboardchange = dashBoardTabsRoleBasedFromUI.getDashBoardTabs().getTabName() +"~"+ dashBoardTabsRoleBasedFromUI.getProductSpecificUserRole().getRoleName();
				eventsService.addEntityChangedEvent(IDPAConstants.ENTITY_DASHBOARD_TABS_ROLEBASED, dashBoardTabsRoleBasedFromUI.getRoleBasedId(), rolebasedDashboardchange,
						jsonDashBoardTabsRoleBasedURL.getModifiedField(), jsonDashBoardTabsRoleBasedURL.getModifiedFieldTitle(),
						jsonDashBoardTabsRoleBasedURL.getOldFieldValue(), jsonDashBoardTabsRoleBasedURL.getModifiedFieldValue(), user, remarks);
			}
			jTableSingleResponse = new JTableSingleResponse("OK" ,new JsonDashBoardTabsRoleBasedURL(dashBoardTabsRoleBasedFromUI));  
		    } catch (Exception e) {
		    	jTableSingleResponse = new JTableSingleResponse("ERROR","Unable to update the roletab!");
	            log.error("JSON ERROR", e);
	        }
        return jTableSingleResponse;
    }
	
	
	@RequestMapping(value="administration.dashboardTabRoleBased.user.delete",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse deleteDashboardTabRoleBased(@RequestParam int roleBasedId) {
		JTableResponse jTableResponse;
		try {
			
			DashBoardTabsRoleBasedURL dashboardroleuser=dashBoardService.getDashBoardTabsRoleBasedURLById(roleBasedId);
			dashBoardService.deleteroleBasedDataById(dashboardroleuser);		
	            log.info("DashBoardTabsRoleBasedURL Deleted");
	            jTableResponse = new JTableResponse("OK");
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error deleting record!");
	            log.error("JSON ERROR", e);
	        }
	        
        return jTableResponse;
    }
	
	
	@RequestMapping(value="administration.bulk.push.from.sqlTo.MongoDB",method=RequestMethod.POST, produces="application/json")
	public @ResponseBody JTableResponse addBulkDatatoMongoDB(@RequestParam  String collectionType,@RequestParam  String startDate,@RequestParam String endDate){
		JTableResponse jTableResponse;
		try{
			Date dateRangeStart=null;
			Date dateRangeEnd=null;
			if(startDate!=null &&!startDate.isEmpty() && endDate!=null && !endDate.isEmpty() ){
				dateRangeStart=DateUtility.dateformatWithOutTime(startDate);
				dateRangeEnd=DateUtility.dateformatWithOutTime(endDate);
			}
			
			String[] collection=collectionType.split(",");
			for(String collectionNumber:collection){
				mongoDBService.pushToMongoDB(Integer.parseInt(collectionNumber),dateRangeStart,dateRangeEnd);
			}
			
			jTableResponse = new JTableResponse("OK");
		}catch(Exception e){
			jTableResponse = new JTableResponse("ERROR","Error pushing record!");
            log.error("JSON ERROR", e);
			
		}
		return jTableResponse;
	}
	
	@RequestMapping(value="administration.visualizationUrl.add",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse addDashboardVisualizationUrl(HttpServletRequest req,@ModelAttribute JsonDashBoardVisualizationURL jsonDashBoardVisualizationURL, BindingResult result) {
		log.info("Inside dashboard tabs Add ");
		JTableSingleResponse jTableSingleResponse=null;
		if(result.hasErrors()){
			jTableSingleResponse  = new JTableSingleResponse("ERROR","Invalid form!"); 
			return jTableSingleResponse;
		}		
		try {   	
			DashboardVisualizationUrls visualizationUrls= jsonDashBoardVisualizationURL.getDashboardVisualizationUrls();
			UserList user = new UserList();
			user = (UserList)req.getSession().getAttribute("USER");
			visualizationUrls.setCreatedBy(user);
			visualizationUrls.setCreatedDate(new Date());
			visualizationUrls.setStatus(1);
				dashBoardService.addvisualizationUrls(visualizationUrls);
				if(visualizationUrls != null && visualizationUrls.getVisualizationId() != null){
					eventsService.addNewEntityEvent(IDPAConstants.ENTITY_DASHBOARD_VISUALIZATION_URLS, visualizationUrls.getVisualizationId(), visualizationUrls.getUrlName(), user);
				}
				jTableSingleResponse = new JTableSingleResponse("OK",new JsonDashBoardVisualizationURL(visualizationUrls));		
	        } catch (Exception e) {
	            jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding DashboardVisualizationUrl record!");
	            log.error("JSON ERROR adding DashboardVisualizationUrl record", e);
	        }
        return jTableSingleResponse;
    }
	
	
	@RequestMapping(value="administration.visualizationUrl.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listvisualizationUrl(@RequestParam Integer status, @RequestParam Integer jtStartIndex, @RequestParam Integer jtPageSize) {
		log.debug("inside administration.visualizationUrl.list");
		JTableResponse jTableResponse;
		 try{
			 
			List<DashboardVisualizationUrls> dashboardVisualizationList=dashBoardService.listDashboardVisualization(status, jtStartIndex, jtPageSize);
			
			List<JsonDashBoardVisualizationURL> jsonDashBoardVisualizationURL=new ArrayList<JsonDashBoardVisualizationURL>();
			for(DashboardVisualizationUrls url: dashboardVisualizationList){
				
				jsonDashBoardVisualizationURL.add(new JsonDashBoardVisualizationURL(url));
				
			}
            jTableResponse = new JTableResponse("OK", jsonDashBoardVisualizationURL,jsonDashBoardVisualizationURL.size());     
            dashboardVisualizationList = null;
		 
			
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Unable to show visualizationUrl List!");
	            log.error("JSON ERROR", e);
	        }
	        
        return jTableResponse;
    }
	
	
	@RequestMapping(value="administration.visualizationUrl.update",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse updateDashboardvisualizationUrl(HttpServletRequest req, @ModelAttribute JsonDashBoardVisualizationURL jsonDashBoardVisualizationURL,BindingResult result) {
		JTableResponse jTableResponse = null;
		String remarks = "";
		if(result.hasErrors()){
			jTableResponse = new JTableResponse("ERROR","Invalid Form!"); 
		}	
		DashboardVisualizationUrls dashboardVisualizationFromUI = null;
		try {
			dashboardVisualizationFromUI= jsonDashBoardVisualizationURL.getDashboardVisualizationUrls();
			
			dashBoardService.updateVisualization(dashboardVisualizationFromUI);			
			if(dashboardVisualizationFromUI != null && dashboardVisualizationFromUI.getVisualizationId() != null){
				UserList user=(UserList)req.getSession().getAttribute("USER");
				remarks = "DashBoardVisualizationURL :"+dashboardVisualizationFromUI.getUrlName();
				eventsService.addEntityChangedEvent(IDPAConstants.ENTITY_DASHBOARD_VISUALIZATION_URLS, dashboardVisualizationFromUI.getVisualizationId(), dashboardVisualizationFromUI.getUrlName(),
						jsonDashBoardVisualizationURL.getModifiedField(), jsonDashBoardVisualizationURL.getModifiedFieldTitle(),
						jsonDashBoardVisualizationURL.getOldFieldValue(), jsonDashBoardVisualizationURL.getModifiedFieldValue(), user, remarks);
			}
			 jTableResponse = new JTableResponse("OK");  
		    } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Unable to update the visualizationUrl!");
	            log.error("JSON ERROR", e);
	        }	        
	        
        return jTableResponse;

    }
	
	
	@RequestMapping(value="administration.visualizationUrl.list.option",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listVisualizationUrl() {			
		JTableResponseOptions jTableResponseOptions;			 
		try {
			Integer status=1;
			log.info("");
			List<DashboardVisualizationUrls> dashboardVisualizationList=dashBoardService.listDashboardVisualization(status, null, null);
			
			List<JsonDashBoardVisualizationURL> jsonDashBoardVisualizationURL=new ArrayList<JsonDashBoardVisualizationURL>();
			for(DashboardVisualizationUrls url: dashboardVisualizationList){
				jsonDashBoardVisualizationURL.add(new JsonDashBoardVisualizationURL(url));
			}
			jTableResponseOptions = new JTableResponseOptions("OK", jsonDashBoardVisualizationURL,true);     
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }		        
        return jTableResponseOptions;
    }
	
	@RequestMapping(value="administration.dashboardTabRoleBased.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listDashboardTabsRoleBasedURL(HttpServletRequest req,@RequestParam Integer tabId) {
		log.debug("inside administration.dashboardTab RoleBased.list");
		JTableResponse jTableResponse;
		 try{
			 String modifiedUrl="";
			 String productName="";
			 String productDetails="";
			 String testFactoryName="";
			 String testFactoryDetails = "";
			 List<Integer> testFactories = new ArrayList<Integer>();
			 UserList user = new UserList();
			user = (UserList)req.getSession().getAttribute("USER");
			List<ProductMaster>productList=new ArrayList<ProductMaster>();
			if(user.getUserRoleMaster().getUserRoleId() == IDPAConstants.ROLE_ID_ADMIN){
				productName = "*";
				testFactoryName = "*";
				productList=productListService.listProduct();
				for(ProductMaster product : productList){
					
					if(product.getTestFactory()!= null){
						if(!testFactories.contains(product.getTestFactory().getTestFactoryId())){
							if(testFactoryDetails==null||testFactoryDetails.trim().isEmpty()){
								testFactoryDetails = product.getTestFactory().getTestFactoryId()+"~~~"+product.getTestFactory().getTestFactoryName();
							}else{
								testFactoryDetails += ","+product.getTestFactory().getTestFactoryId()+"~~~"+product.getTestFactory().getTestFactoryName();
							}
							testFactories.add(product.getTestFactory().getTestFactoryId());
						}
					}
					
					
				}
			}else{
				productName = "%22No Projects for user%22";
				testFactoryName = "%22No Test factory for user%22";
				Set<ProductUserRole> userProducts = user.getProductUserRoleSet();
				if(userProducts != null && userProducts.size() > 0){
					productName = "";
					testFactoryName = "";
					for(ProductUserRole productUserRole: userProducts){
						
						if(productUserRole != null && productUserRole.getStatus() >0) {
							if(productName == null || productName.trim().isEmpty()){
								productName = "productName: "+productUserRole.getProduct().getProductName();
							}else{
								productName += " OR "+productUserRole.getProduct().getProductName();
							}
							
							if(!testFactories.contains(productUserRole.getProduct().getTestFactory().getTestFactoryId())){
								if(testFactoryDetails==null||testFactoryDetails.trim().isEmpty()){
									testFactoryName = "testFactoryName: "+productUserRole.getProduct().getTestFactory().getTestFactoryName();
									testFactoryDetails = productUserRole.getProduct().getTestFactory().getTestFactoryId()+"~~~"+productUserRole.getProduct().getTestFactory().getTestFactoryName();
								}else{
									testFactoryName += " OR "+productUserRole.getProduct().getTestFactory().getTestFactoryName();
									testFactoryDetails += ","+productUserRole.getProduct().getTestFactory().getTestFactoryId()+"~~~"+productUserRole.getProduct().getTestFactory().getTestFactoryName();
								}
								testFactories.add(productUserRole.getProduct().getTestFactory().getTestFactoryId());
							}
						}	
					}
				}else{
					productName = "";
				}
				
			}
			if(productName != null && !productName.trim().isEmpty()){
				productName = productName.replaceAll(" ", "%20");
			}
			if(testFactoryName != null && !testFactoryName.trim().isEmpty()){
				testFactoryName = testFactoryName.replace(" ", "%20");
			}
			
			List<DashBoardTabsRoleBasedURL> dashBoardTabsRoleBasedURLList=dashBoardService.listDashboardTabsRoleBasedURL(user.getUserRoleMaster().getUserRoleId(),tabId);
			
			
			List<JsonDashBoardTabsRoleBasedURL> jsonDashBoardTabsRoleBasedURL=new ArrayList<JsonDashBoardTabsRoleBasedURL>();
			
			for(DashBoardTabsRoleBasedURL roleList: dashBoardTabsRoleBasedURLList){
				modifiedUrl=roleList.getUrl().getUrl();
				
				if(user.getUserRoleMaster().getUserRoleId() != IDPAConstants.ROLE_ID_ADMIN){
					if("YES".equalsIgnoreCase(kibanaStatusRequired)){
						if(modifiedUrl.contains("query:(match_all:())")){
							modifiedUrl = modifiedUrl.replace("query:(match_all:())", "query:(query_string:(query:'status:%22Active%22%20AND%20parentStatus:%22Active%22%20AND%20("+testFactoryName+")%20AND%20("+productName+")'))");
						}else if(modifiedUrl.contains("query:(query_string:(analyze_wildcard:!t,query:'*'))")){
							modifiedUrl = modifiedUrl.replace("query:(query_string:(analyze_wildcard:!t,query:'*'))", "query:(query_string:(query:'status:%22Active%22%20AND%20parentStatus:%22Active%22%20AND%20("+testFactoryName+")%20AND%20("+productName+")'))");
						}else if(modifiedUrl.contains("query:(query_string:(analyze_wildcard:!t,query:'")){
							String query = modifiedUrl.split("query:(query_string:(analyze_wildcard:!t,query:'")[1].split("'")[0];
							query += "%20AND%20status:%22Active%22%20AND%20parentStatus:%22Active%22%20AND%20("+testFactoryName+")%20AND%20("+productName+")";
							modifiedUrl = modifiedUrl.split("query:(query_string:(analyze_wildcard:!t,query:'")[0]+"query:(query_string:(analyze_wildcard:!t,query:'"+query+"'"+modifiedUrl.split("query:(query_string:(analyze_wildcard:!t,query:'")[1].split("'")[1];
						}
					}else{
						if(modifiedUrl.contains("query:(match_all:())")){
							modifiedUrl = modifiedUrl.replace("query:(match_all:())", "query:(query_string:(query:'("+testFactoryName+")%20AND%20("+productName+")'))");
						}else if(modifiedUrl.contains("query:(query_string:(analyze_wildcard:!t,query:'*'))")){
							modifiedUrl = modifiedUrl.replace("query:(query_string:(analyze_wildcard:!t,query:'*'))", "query:(query_string:(query:'("+testFactoryName+")%20AND%20("+productName+")'))");
						}else if(modifiedUrl.contains("query:(query_string:(analyze_wildcard:!t,query:'")){
							String query = modifiedUrl.split("query:(query_string:(analyze_wildcard:!t,query:'")[1].split("'")[0];
							query += "%20AND%20("+testFactoryName+")%20AND%20("+productName+")";
							modifiedUrl = modifiedUrl.split("query:(query_string:(analyze_wildcard:!t,query:'")[0]+"query:(query_string:(analyze_wildcard:!t,query:'"+query+"'"+modifiedUrl.split("query:(query_string:(analyze_wildcard:!t,query:'")[1].split("'")[1];
						}
					}
					
				}
				
				/*if("YES".equalsIgnoreCase(kibanaStatusRequired)){
					modifiedUrl=	modifiedUrl.replace("query:'*", "query:'status:%22Active%22%20AND%20parentStatus:%22Active%22%20AND%20("+testFactoryName+")%20AND%20("+productName+")");
				}else{
					modifiedUrl=	modifiedUrl.replace("query:'*", "query:'("+testFactoryName+")%20AND%20("+productName+")");
				}*/
				roleList.getUrl().setUrl(modifiedUrl);
				JsonDashBoardTabsRoleBasedURL	jsonDashBoardTabsRoleBased=new JsonDashBoardTabsRoleBasedURL(roleList);
				jsonDashBoardTabsRoleBased.setTestFactoryDetails(testFactoryDetails);
				jsonDashBoardTabsRoleBased.setProductDetails(productDetails);
				jsonDashBoardTabsRoleBasedURL.add(jsonDashBoardTabsRoleBased);
				
			}
            jTableResponse = new JTableResponse("OK", jsonDashBoardTabsRoleBasedURL,jsonDashBoardTabsRoleBasedURL.size());     
            dashBoardTabsRoleBasedURLList = null;
		 
			
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Unable to show TabList Role list!");
	            log.error("JSON ERROR", e);
	        }
	        
        return jTableResponse;
    }
}
