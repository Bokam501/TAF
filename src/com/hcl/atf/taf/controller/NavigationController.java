package com.hcl.atf.taf.controller;





import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hcl.atf.taf.model.Customer;
import com.hcl.atf.taf.model.ProductLocale;
import com.hcl.atf.taf.model.UserActivityTracker;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.json.jtable.JTableResponseOptions;
import com.hcl.atf.taf.service.CustomerService;
import com.hcl.atf.taf.service.DeviceListService;
import com.hcl.atf.taf.service.HostListService;
import com.hcl.atf.taf.service.LicenseCheckService;
import com.hcl.atf.taf.service.ProductListService;
import com.hcl.atf.taf.service.ResourceManagementService;
import com.hcl.atf.taf.service.TestFactoryManagementService;
import com.hcl.atf.taf.service.TestRunConfigurationService;
import com.hcl.atf.taf.service.TestRunReportsDeviceListService;
import com.hcl.atf.taf.service.TestSuiteConfigurationService;
import com.hcl.atf.taf.service.UserActivityTrackerService;
import com.hcl.atf.taf.service.UserListService;
import com.hcl.atf.taf.service.WorkPackageService;


@Controller
public class NavigationController {
	
	@Autowired
	private UserListService userListService;
	@Autowired
	private DeviceListService deviceListService;
	@Autowired
	private HostListService hostListService;
	@Autowired
	private ProductListService productListService;
	@Autowired
	private TestSuiteConfigurationService testSuiteConfigurationService;
	@Autowired
	private TestRunConfigurationService testRunConfigurationService;
	@Autowired
	private TestRunReportsDeviceListService testRunReportsDeviceListService;
	@Autowired
	private WorkPackageService workPackageService;
	@Autowired
	private TestFactoryManagementService testFactoryManagementService;
	@Autowired
	private ResourceManagementService resourceManagementService;
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private UserActivityTrackerService userActivityTrackerService;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private LicenseCheckService licenseCheckService;
	
	private static final Log log = LogFactory.getLog(NavigationController.class);
	
	private String userNameAndRole;
	
	@RequestMapping(value="FormUpload")
	public ModelAndView upload(HttpServletRequest req,HttpServletResponse res) throws Exception {		
		 
		return new ModelAndView("FormUpload");
	}
	
	@RequestMapping(value="FormEvidenceUpload")
	public ModelAndView Evidenceupload(HttpServletRequest req,HttpServletResponse res) throws Exception {		
		 
		return new ModelAndView("FormEvidenceUpload");
	}
	
	@RequestMapping(value="login")
	public ModelAndView login(HttpServletRequest req,HttpServletResponse res) throws Exception {	
		
		log.info("inside navigation Controller and login method");
		/*String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
		log.info("loginId in login () ofnavigation Controller: "+loginId);
		UserList user = userListService.getUserListByUserName(loginId);*/
		UserList user = (UserList) req.getSession().getAttribute("USER");
		req.getSession().removeAttribute("userLoginTime");
		req.getSession().removeAttribute("userLogoutTime");
		
		String filePath = request.getServletContext().getRealPath(File.separator)+File.separator+"data"+File.separator+"license.properties";
		String response = licenseCheckService.licenseAgrementValidation(filePath); 
		log.info("Response from licenseAgrementValidation : "+response);
		if(!response.contains("success")){
			return new ModelAndView("error_new","message","License expired. Please contact the TAF admin.");
		}else{
			//Do nothing acnd continue to login
			log.info("Active License available.");
		}
		if(user != null) {
			 
			UserActivityTracker userActivityTracker=userActivityTrackerService.getUserActivityTracker(user.getUserId());
             if(userActivityTracker !=null) {
            	 userActivityTracker.setUser(user);
            	 userActivityTracker.setUserLogoutTime(new Date());
            	 userActivityTracker.setReason("User proper Logoff ");
            	 userActivityTrackerService.updateUserActivityTracker(userActivityTracker);
             }
		}
		return new ModelAndView("login_new");
		
	}
	@RequestMapping(value="welcome")
	public ModelAndView welcome(HttpServletRequest req,HttpServletResponse res) throws Exception {	

		String userDisplayName="HCL";
		log.debug("inside navigation Controller and welcome method");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();
			setUserNameAndRole(userName);
			//commented to the User Name and Role in the JSP pages.
			/*userDisplayName=userListService.getUserListByUserName(userName).getUserDisplayName();*/
			userDisplayName = getUserNameAndRole();
		}catch(Exception e){}
		return new ModelAndView("welcome","user",userDisplayName);
		
	}
	@RequestMapping(value="Reactivate")
	public ModelAndView Reactivate(HttpServletRequest req,HttpServletResponse res) throws Exception {	
		String userDisplayName="HCL";
		log.debug("inside navigation Controller and Reactivate method");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();
		}catch(Exception e){}
		
		return new ModelAndView("Reactivate","user",userDisplayName);
	}
	
	@RequestMapping(value="index.jsp")
	public ModelAndView index(HttpServletRequest req,HttpServletResponse res) throws Exception {
		
		return new ModelAndView("login_new");
	}
	@RequestMapping(value="login.error")
	public ModelAndView loginError(HttpServletRequest req,HttpServletResponse res) throws Exception {		
		ModelAndView mv = new ModelAndView("login_new");
		/*String message = req.getParameter("message");
		if(message!=null)*/mv.addObject("message", "INVALID CREDENTIALS!");
		return mv;
	}
	@RequestMapping(value="session.error")
	public ModelAndView loginSession(HttpServletRequest req,HttpServletResponse res) throws Exception {		
		ModelAndView mv = new ModelAndView("login_new");
		/*String message = req.getParameter("message");
		if(message!=null)*/mv.addObject("message", "SESSION EXPIRED!");
		return mv;
	}
	@RequestMapping(value="Reactivate.error")
	public ModelAndView reactivateError(HttpServletRequest req,HttpServletResponse res) throws Exception {		
		ModelAndView mv = new ModelAndView("Reactivate");
		/*String message = req.getParameter("message");
		if(message!=null)*/mv.addObject("message", "INVALID CREDENTIALS!");
		return mv;
	}
	
	@RequestMapping(value="homepage") 
	public ModelAndView home(HttpServletRequest req,HttpServletResponse res) throws Exception {
		String userDisplayName="HCL";
		log.debug("inside navigation Controller and home method");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();
		}catch(Exception e){}
		return new ModelAndView("home_new", "user",userDisplayName);
	}
	
	@RequestMapping(value="administration")
	public String administration(ModelMap model) throws Exception {
		log.debug("inside navigation Controller and adminstration method");
		model.addAttribute("userCount",userListService.getTotalRecords());
		model.addAttribute("deviceCount",deviceListService.getTotalRecords());
		model.addAttribute("hostCount",hostListService.getTotalRecords());
		model.addAttribute("productCount",productListService.getTotalRecordsOfProduct());
		
		String userDisplayName="HCL";
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);
		
		return "administration_new";
	}
	@RequestMapping(value="administration.user")
	public String adminUser(ModelMap model) throws Exception {
		String userDisplayName="HCL";
		log.debug("inside navigation Controller and admin user method");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();
			setUserNameAndRole(userName);
			log.debug("inside navigation Controller and admin user method admin.user :"+userName);
			//userDisplayName=userListService.getUserListByUserName(userName).getUserDisplayName();
			userDisplayName = getUserNameAndRole();
			log.debug("inside navigation Controller and admin user method admin.user :"+userDisplayName);
			
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);
		log.info("inside navigation Controller and admin user method admin.user :"+userDisplayName);
		return "user_new";
	}
	@RequestMapping(value="administration.workPackage.testCase")
	public String adminHost(ModelMap model) throws Exception {
		String userDisplayName="HCL";
		log.debug("inside navigation Controller and adminhost method");
		try{
			String userName=SecurityContextHolder.getContext()
		     .getAuthentication().getName();
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();
			
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);
		return "workPackageTestCaseView";
		//return "host_new";
	}
	@RequestMapping(value="administration.device")
	public String adminDevice(ModelMap model) throws Exception {
		String userDisplayName="HCL";
		log.debug("inside navigation Controller and admindevice method");
		try{
			String userName=SecurityContextHolder.getContext()
		     .getAuthentication().getName();
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();
			
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);
		
		return "device_new";
	}
	@RequestMapping(value="administration.product")
	public String adminProduct(ModelMap model) throws Exception {
		
		String userDisplayName="HCL";
		log.debug("inside navigation Controller and adminproduct method");
		try{
			String userName=SecurityContextHolder.getContext()
		     .getAuthentication().getName();
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();
			
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);
			return "product_new";
	}
	
	@RequestMapping(value="administration.tools.integration")
	public String adminToolIntegration(ModelMap model) throws Exception {
		
		String userDisplayName="HCL";
		log.debug("inside navigation Controller and adminToolIntegration method");
		try{
			String userName=SecurityContextHolder.getContext()
		     .getAuthentication().getName();
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();
			
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);
			return "toolintegration_new";
	}
	
	@RequestMapping(value="test")
	public String test(ModelMap model) throws Exception {
		model.addAttribute("testSuiteCount",testSuiteConfigurationService.getTotalRecordsOfTestSuite());
		model.addAttribute("TestRunConfigurationChildCount",testRunConfigurationService.getTotalRecordsOfTestRunConfigurationChild());
		
		String userDisplayName="HCL";
		log.debug("inside navigation Controller and test method");
		try{
			String userName=SecurityContextHolder.getContext()
		     .getAuthentication().getName();
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();
			
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);
			return "test_new";
	}
	
	@RequestMapping(value="test.suite")
	public String testSuite(ModelMap model) throws Exception {
		String userDisplayName="HCL";
		log.debug("inside navigation Controller and testSuite method");
		try{
			String userName=SecurityContextHolder.getContext()
		     .getAuthentication().getName();
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();
			
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);
			return "testsuite_new";
	}
	@RequestMapping(value="test.run")
	public String testenvironment(ModelMap model) throws Exception {
		String userDisplayName="HCL";
		log.debug("inside navigation Controller and testSuite method");
		try{
			String userName=SecurityContextHolder.getContext()
		     .getAuthentication().getName();
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();
			
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);
			return "testrun_new";
	}
	@RequestMapping(value="runTerminal")
	public String runTerminal(ModelMap model) throws Exception {
		String userDisplayName="HCL";
		log.debug("inside navigation Controller and runTerminal method");
		try{
			String userName=SecurityContextHolder.getContext()
		     .getAuthentication().getName();
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();
			
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);
			return "terminalclient_new";
	}
	@RequestMapping(value="test.suite.testcases")
	public String testSuiteCases(@RequestParam Integer testSuiteId,ModelMap model,HttpServletRequest request,HttpServletResponse response) throws Exception {		
		request.setAttribute("testSuiteId", testSuiteId.toString());
		log.debug("inside navigation Controller and testSuiteCases method");
		String userDisplayName="HCL";
		try{
			String userName=SecurityContextHolder.getContext()
		     .getAuthentication().getName();
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();
			
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);
		return "testSuiteCases_new";
	}
	@RequestMapping(value="report")
	public String report(ModelMap model,ServletRequest req) throws Exception {
		String filter=req.getParameter("filter");
		String userDisplayName="HCL";
		log.debug("inside navigation Controller and report method");
		try{
			String userName=SecurityContextHolder.getContext()
		     .getAuthentication().getName();
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();
			
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);
		if(filter!=null && filter.equalsIgnoreCase("true"))
			return "report_with_filter_new";
		else
			return "report_without_filter_new";
	}
	@RequestMapping(value="events")
	public String event(ModelMap model,ServletRequest req) throws Exception {
		String userDisplayName="HCL";
		String filter=req.getParameter("filter");
		log.debug("inside navigation Controller and event method");
		try{
			String userName=SecurityContextHolder.getContext()
		     .getAuthentication().getName();
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);
		if(filter!=null && filter.equalsIgnoreCase("true"))
			return "monitor_with_filter";
		else
			return "monitor";
		
	}
	@RequestMapping(value="trccExecutionPlanDetail")
	public String trccExecutionPlanDetail(ModelMap model,ServletRequest req) throws Exception {
		String userDisplayName="HCL";
		String testSuiteId = req.getParameter("testSuiteId");
		String testRunConfigurationChildId = req.getParameter("testRunConfigurationChildId");
		log.debug("inside navigation Controller and trccExecutionPlanDetail method");
		try{
			String userName=SecurityContextHolder.getContext()
		     .getAuthentication().getName();
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);
		model.addAttribute(testSuiteId, testSuiteId);
		model.addAttribute(testRunConfigurationChildId, testRunConfigurationChildId);
			return "trccExecutionPlanDetails";
		
	}
	@RequestMapping(value="test.suite.results")
	public String testSuite(ModelMap model,ServletRequest req) throws Exception {
		String filter=req.getParameter("filter");
		String userDisplayName="HCL";
		log.debug("inside navigation Controller and report method");
		try{
			String userName=SecurityContextHolder.getContext()
		     .getAuthentication().getName();
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();
			
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);
		if(filter!=null && filter.equalsIgnoreCase("true"))
			return "testsuite_with_filter_new";
		else
			return "testsuite_new";
	}
		
	@RequestMapping(value="test.status")
	public String testStatus(ModelMap model) throws Exception {
		String userDisplayName="HCL";
		log.debug("inside navigation Controller and testStatus method");
		try{
			String userName=SecurityContextHolder.getContext()
		     .getAuthentication().getName();
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();
			
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);
			return "testrunstatus_new";
	}
	
	//Changes made for test management tools integration -Priya.B
	@RequestMapping(value="test.integration")
	public String testIntegration(ModelMap model) throws Exception {
		String userDisplayName="HCL";
		try{
			String userName=SecurityContextHolder.getContext()
		     .getAuthentication().getName();
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();
			//ExcelTestDataIntegrator excelTestDataIntegrator = null;
			
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);
			return "test_Integration";
	}
	
	@RequestMapping(value="error")
	public ModelAndView errorPOST(HttpServletRequest req,
			HttpServletResponse res) throws Exception {
			
			return new ModelAndView("error_new","message",req.getParameter("message"));
	}
	
	/*
	@RequestMapping(value="error",method=RequestMethod.GET)
	public String errorGET(@RequestParam String message) throws Exception {
			return "error_new";
	}*/
	
	@ExceptionHandler(Exception.class)
	public void handleException(Throwable ex, HttpServletResponse response)
			throws Exception {

		response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
				ex.getMessage());
	}
	
	@RequestMapping(value="administration.workpackage")
	public String adminWorkPackage(ModelMap model) throws Exception {
		String userDisplayName="HCL";
		log.debug("inside navigation Controller and admin WorkPackage method");
		try{
			String userName=SecurityContextHolder.getContext()
		     .getAuthentication().getName();
		//	log.info("inside navigation Controller and admin user method admin.user :"+userName);
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();
			log.info("inside navigation Controller and admin user method admin.user :"+userDisplayName);
			
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);
		log.info("inside navigation Controller and admin user method admin.user :"+userDisplayName);
		return "workpackage";
	}
	
	@RequestMapping(value="administration.product.environment")
	public String adminEnvironment(ModelMap model) throws Exception {
		String userDisplayName="HCL";
		log.debug("inside navigation Controller and admin Environment method");
		try{
			String userName=SecurityContextHolder.getContext()
		     .getAuthentication().getName();			
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();	
		}catch(Exception e){}
		
		model.addAttribute("user",userDisplayName);	
		return "product_test_environment";		
	}	
	
	/* Product Locale */
	@RequestMapping(value="administration.product.locale")
	public String adminLocale(ModelMap model) throws Exception {
		String userDisplayName="HCL";
		log.debug("inside navigation Controller and admin Environment method");
		try{
			String userName=SecurityContextHolder.getContext()
		     .getAuthentication().getName();			
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();	
		}catch(Exception e){}
		
		model.addAttribute("user",userDisplayName);	
		return "product_locale";		
	}	
	
	@RequestMapping(value="administration.product.decouplingcategory")
	public String adminDecouplingCategory(ModelMap model) throws Exception {
		String userDisplayName="HCL";

		log.debug("inside navigation Controller and admin DecouplingCategory method");
		try{
			String userName=SecurityContextHolder.getContext()
		     .getAuthentication().getName();			
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();			
		}catch(Exception e){}
		
		model.addAttribute("user",userDisplayName);	
		return "product_decouplingcategory";		
	}

	@RequestMapping(value="administration.product.feature")
	public String adminFeature(ModelMap model) throws Exception {
		String userDisplayName="HCL";
		log.debug("inside navigation Controller and admin Feature method");
		try{
			String userName=SecurityContextHolder.getContext()
		     .getAuthentication().getName();			
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();	
		}catch(Exception e){}
		
		model.addAttribute("user",userDisplayName);	
		return "product_feature";		
	}
	
	@RequestMapping(value="product.feature.testcase")
	public String featureTestCase(ModelMap model) throws Exception {
		String userDisplayName="HCL";
		log.debug("inside navigation Controller and FeatureTestCase method");
		try{
			String userName=SecurityContextHolder.getContext()
		     .getAuthentication().getName();
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();

		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);
			return "product_feature_testcase";
	}
	@RequestMapping(value="product.testcase")
	public String productTestCase(ModelMap model) throws Exception {
		String userDisplayName="HCL";
		log.debug("inside navigation Controller and productTestCase method");
		try{
			String userName=SecurityContextHolder.getContext()
		     .getAuthentication().getName();
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();
			
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);
			return "product_testcases_new";
	}
	@RequestMapping(value="administration.workpackage.plan")
	public String adminWorkPackageTestCasePlan(ModelMap model) throws Exception {
		String userDisplayName="HCL";
		log.debug("inside navigation Controller and adminhost method");
		try{
			String userName=SecurityContextHolder.getContext()
		     .getAuthentication().getName();
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();
			
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);
		return "workPackageTestCasePlan";
		//return "host_new";
	}

	@RequestMapping(value="workpackage.testcase.plan.testerhome")
	public String adminWorkPackageTestCasePlanForTester(ModelMap model) throws Exception {
		String userDisplayName="HCL";
		log.debug("inside navigation Controller and adminWorkPackageTestCasePlanForTester method");
		try{
			String userName=SecurityContextHolder.getContext()
		     .getAuthentication().getName();
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();
			
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);
		return "workPackageTestCasePlanResult";
		//return "host_new";
	}

	@RequestMapping(value="product.decouplingcategory.testcase")
	public String decouplingCategoryTestCase(ModelMap model) throws Exception {
		String userDisplayName="HCL";
		log.debug("inside navigation Controller and decouplingCategoryTestCase method");
		try{
			String userName=SecurityContextHolder.getContext()
		     .getAuthentication().getName();
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();

		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);
			return "decouplingcategory_testcase_new";
	}
	
	@RequestMapping(value="administration.product.userRole")
	public String productUserRole(ModelMap model) throws Exception {
		
		String userDisplayName="HCL";
		log.debug("inside navigation Controller and product user role method");
		try{
			String userName=SecurityContextHolder.getContext()
		     .getAuthentication().getName();
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();
			
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);
			return "product_user_role";
	}
	
	@RequestMapping(value="workpackage.demand.projection")
	public String workPackageDemandProjection(ModelMap model) throws Exception {
		
		String userDisplayName="HCL";
		log.debug("inside navigation Controller and workPackageDemandProjection method");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();
			setUserNameAndRole(userName);
			//commented to the User Name and Role in the JSP pages.
			/*userDisplayName=userListService.getUserListByUserName(userName).getUserDisplayName();*/
			userDisplayName = getUserNameAndRole();
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);
			return "workPackage_demand_projection";
	}
			
	@RequestMapping(value="workpackage.testcase.plan.reviewTestLeadTestCases")
	public String reviewTestCaseByTestLead(ModelMap model) throws Exception {
		
		String userDisplayName="HCL";
		log.debug("inside navigation Controller and review testcase by test lead method");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();
			setUserNameAndRole(userName);
			//commented to the User Name and Role in the JSP pages.
			/*userDisplayName=userListService.getUserListByUserName(userName).getUserDisplayName();*/
			userDisplayName = getUserNameAndRole();
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);
			return "workPackageTestCaseReview_testLead";
	}
	
	public String getUserNameAndRole() {
		return userNameAndRole;
	}
	
	public  void setUserNameAndRole(String userLoginId){
		this.userNameAndRole = userListService.getUserListByUserName(userLoginId).getUserDisplayName()+" ["+userListService.getUserListByUserName(userLoginId).getUserRoleMaster().getRoleLabel()+"]";
	}
	
	@RequestMapping(value="resource.availability.view")
	public String resourceAvailabilityView(ModelMap model) throws Exception {
		
		String userDisplayName="HCL";
		log.debug("inside navigation Controller and workPackageDemandProjection method");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();
			setUserNameAndRole(userName);
			//commented to the User Name and Role in the JSP pages.
			/*userDisplayName=userListService.getUserListByUserName(userName).getUserDisplayName();*/
			userDisplayName = getUserNameAndRole();
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);
			return "resourceAvailabilityView";
	}
	
	@RequestMapping(value="resource.availability.plan")
	public String resourceAvailabilityPlan(ModelMap model) throws Exception {
		
		String userDisplayName="HCL";
		log.debug("inside navigation Controller and workPackageDemandProjection method");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();
			setUserNameAndRole(userName);
			//commented to the User Name and Role in the JSP pages.
			/*userDisplayName=userListService.getUserListByUserName(userName).getUserDisplayName();*/
			userDisplayName = getUserNameAndRole();
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);
			return "resourceAvailabilityPlan";
	}
	
	@RequestMapping(value="testfactory.resource.pool")
	public String adminResourcePool(ModelMap model) throws Exception {
		
		String userDisplayName="HCL";
		log.debug("inside navigation Controller and adminResourcePool method");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();
			setUserNameAndRole(userName);
			//commented to the User Name and Role in the JSP pages.
			/*userDisplayName=userListService.getUserListByUserName(userName).getUserDisplayName();*/
			userDisplayName = getUserNameAndRole();
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);
			return "testfactory_resource_pool";
	}
	@RequestMapping(value="report.workpackage.testcase.plan.results.eod")
	public String workPackageTestCasePlanResultsEod(ModelMap model) throws Exception {
		String userDisplayName="HCL";
		log.debug("inside navigation Controller and workPackageTestCasePlanResultsEod method");
		try{
			String userName=SecurityContextHolder.getContext()
		     .getAuthentication().getName();
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();
			
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);
		return "workPackageTestCasePlanResultEod";
		//return "host_new";
	}
	
	@RequestMapping(value="report.workpackage.statistics.eod")
	public String workPackageStatisticsEod(ModelMap model) throws Exception {
		String userDisplayName="HCL";
		log.debug("inside navigation Controller and workPackageStatisticsEod method");
		try{
			String userName=SecurityContextHolder.getContext()
		     .getAuthentication().getName();
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();
			
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);
		return "workPackageStatisticsEod";
		//return "host_new";
	}

	@RequestMapping(value="resource.dailyperformance.approve")
	public String resourceDailyPerformanceApprove(ModelMap model) throws Exception {
		String userDisplayName="HCL";
		log.debug("inside navigation Controller and resourceDailyPerformanceApprove method");
		try{
			String userName=SecurityContextHolder.getContext()
		     .getAuthentication().getName();
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();
			
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);
		return "resourceDailyPerformanceApprove";  // This page uses almost same fields as that of  workPackageStatisticsEod.jsp/report.workpackage.statistics.eod
		//return "host_new";
	}
	
	@RequestMapping(value="report.timesheet.statistics")
	public String timeSheetStatistics(ModelMap model) throws Exception {
		String userDisplayName="HCL";
		log.debug("inside navigation Controller and workPackageStatisticsEod method");
		try{
			String userName=SecurityContextHolder.getContext()
		     .getAuthentication().getName();
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();
			
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);
		return "report_TimeSheetStatistics";
		//return "host_new";
	}

	
	
	@RequestMapping(value="timesheet.entries")
	public String myTimeSheet(ModelMap model) throws Exception {
		
		String userDisplayName="HCL";
		log.debug("inside myTimeSheet method");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();
			setUserNameAndRole(userName);
			//commented to the User Name and Role in the JSP pages.
			/*userDisplayName=userListService.getUserListByUserName(userName).getUserDisplayName();*/
			userDisplayName = getUserNameAndRole();
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);
			return "mytimesheet";
	}
	
	
	@RequestMapping(value="timesheet.entries.approve")
	public String approveTimeSheetEntries(ModelMap model) throws Exception {
		
		String userDisplayName="HCL";
		log.debug("inside myTimeSheet method");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();
			setUserNameAndRole(userName);
			//commented to the User Name and Role in the JSP pages.
			/*userDisplayName=userListService.getUserListByUserName(userName).getUserDisplayName();*/
			userDisplayName = getUserNameAndRole();
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);
			return "timesheet_entries_approval";
	}
	
	@RequestMapping(value="administration.workpackage.locale")
	public String mapWorkpackageWithLocale(ModelMap model,ServletRequest req) throws Exception {
		
		String userDisplayName="HCL";
		String productId = req.getParameter("productId");
		String workpackageId = req.getParameter("workpackageId");
		
		
		List<ProductLocale> localeSourceList = new ArrayList<ProductLocale>();
		Set<ProductLocale> localeDestinationList = new HashSet<ProductLocale>(0);
		log.debug("inside navigation Controller and mapWorkpackageWithLocale");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();
			setUserNameAndRole(userName);
			
			localeSourceList=productListService.getProductLocaleListByProductId(Integer.parseInt(productId));
			localeDestinationList=workPackageService.getLocaleMappedToWorkpackage(Integer.parseInt(workpackageId));
			
			for(ProductLocale pl:localeDestinationList){
				if(localeSourceList.contains(pl)){
					localeSourceList.remove(pl);
				}
			}
			
			
			userDisplayName = getUserNameAndRole();
		}catch(Exception e){}
		model.addAttribute("localeSourceList",localeSourceList);
		model.addAttribute("localeDestinationList",localeDestinationList);
		model.addAttribute("productId",productId);
		model.addAttribute("workpackageId",workpackageId);
		model.addAttribute("user",userDisplayName);
			return "workpackageWithLocale";
	}
	
	
	
	@RequestMapping(value="workPackage.status.summary.view")
	public String viewWorkPackageStatusSummary(ModelMap model) throws Exception {
		String userDisplayName="HCL";
		log.debug("inside myTimeSheet method");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();
			setUserNameAndRole(userName);
			//commented to the User Name and Role in the JSP pages.
			/*userDisplayName=userListService.getUserListByUserName(userName).getUserDisplayName();*/
			userDisplayName = getUserNameAndRole();
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);
			return "workpackageStatusSummary";
	}
	
	@RequestMapping(value="testrunplan.view")
	public String viewTestRunPlan(ModelMap model) throws Exception {
		String userDisplayName="HCL";
		log.debug("inside viewTestRunPlan method");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();
			setUserNameAndRole(userName);
			//commented to the User Name and Role in the JSP pages.
			/*userDisplayName=userListService.getUserListByUserName(userName).getUserDisplayName();*/
			userDisplayName = getUserNameAndRole();
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);
			return "testRunPlanView";
	}
	
	@RequestMapping(value="administration.vendor")
	public String viewManagerVendor(ModelMap model) throws Exception {
		String userDisplayName="HCL";
		log.debug("inside myTimeSheet method");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();
			setUserNameAndRole(userName);
			//commented to the User Name and Role in the JSP pages.
			/*userDisplayName=userListService.getUserListByUserName(userName).getUserDisplayName();*/
			userDisplayName = getUserNameAndRole();
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);
			return "vendor";
	}
	@RequestMapping(value="administration.testFactoryLabs")
	public String viewManageTestFactoryLabs(ModelMap model) throws Exception {
		String userDisplayName="HCL";
		log.debug("inside myTimeSheet method");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();
			setUserNameAndRole(userName);
			//commented to the User Name and Role in the JSP pages.
			/*userDisplayName=userListService.getUserListByUserName(userName).getUserDisplayName();*/
			userDisplayName = getUserNameAndRole();
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);
			return "testFactoryLab";
	}
	
	@RequestMapping(value="administration.help")
	public String viewAdministrationHelp(ModelMap model) throws Exception {
		String userDisplayName="HCL";
		log.debug("inside administrationHelp method");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);
			return "administrationHelp";
	}

	@RequestMapping(value="workpackage.resource.planning")
	public String resourcePlanning(ModelMap model) throws Exception {
		String userDisplayName="HCL";
		log.debug("workpackage.resource.planning");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);
			return "resourcePlanning";
	}
	
//	@RequestMapping(value="workPackage.block.resources")
//	public String blockResourceForWorkPackage(ModelMap model,ServletRequest req) throws Exception {
//		Date date = null;
//		String userDisplayName="HCL";
//		String workpackageId = req.getParameter("workPackageId");
//		String strResourceDemandForDate = req.getParameter("resourceDemandForDate");
//		String shiftId = req.getParameter("shiftId");
//		String loggedInUserId = "";
//		
//		List<JsonUserList> jsonAvailableResources = new ArrayList<JsonUserList>();
//		List<JsonUserList> jsonBlockedResourceList = new ArrayList<JsonUserList>(0);
//		
//		log.info("inside navigation Controller and resource reservation for work package");
//		try{
//			String userName=SecurityContextHolder.getContext().getAuthentication().getName();
//			setUserNameAndRole(userName);
//			loggedInUserId = userListService.getUserListByUserName(userName).getUserId().toString();
//			log.info("loggedInUserId: "+loggedInUserId);
//			date = DateUtility.dateformatWithOutTime(strResourceDemandForDate);
//			jsonAvailableResources = resourceManagementService.getAvailableUsersForBlocking(Integer.parseInt(workpackageId),Integer.parseInt(shiftId),date);
//			if(jsonAvailableResources != null && jsonAvailableResources.size()>0){
//				Collections.sort(jsonAvailableResources, JsonUserList.jsonUserListComparator);
//			}
//			log.info("availableResources size: "+jsonAvailableResources.size());
//			for (JsonUserList jsonUserList : jsonAvailableResources) {
//				log.info("UserId: ** "+jsonUserList.getUserId() +" Login Id: "+jsonUserList.getLoginId()+ "  Time sheet Duration: "+jsonUserList.getTimeSheetHours());
//			}
//			jsonBlockedResourceList = resourceManagementService.getBlockedResourcesOfWorkPackage(Integer.parseInt(workpackageId),Integer.parseInt(shiftId),date);
//			if(jsonBlockedResourceList != null && jsonBlockedResourceList.size()>0){
//				Collections.sort(jsonBlockedResourceList, JsonUserList.jsonUserListComparator);
//			}
//			log.info("blockedResourceList size: "+jsonBlockedResourceList.size());
//			userDisplayName = getUserNameAndRole();
//		}catch(Exception e){
//			log.error(e);
//		}
//		
//		model.addAttribute("availableResourceList",jsonAvailableResources);
//		model.addAttribute("blockedResourceList",jsonBlockedResourceList);
//		model.addAttribute("blockResourceForDate",date);
//		model.addAttribute("shiftId",shiftId);
//		model.addAttribute("workpackageId",workpackageId);
//		model.addAttribute("user",userDisplayName);
//		model.addAttribute("loggedInUserId",loggedInUserId);
//		//return "blockResourceForWorkPackage";
//		return "resourceReservationForWorkPackage";
//	}
	
	@RequestMapping(value="workpackage.resource.availability")
	public String resourceAvailablity(ModelMap model) throws Exception {
		String userDisplayName="HCL";
		log.debug("workpackage.resource.availability");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);
			return "resourceAvailability";
	}
	
	@RequestMapping(value="resource.attendance.availability")
	public String resourceAttendance(ModelMap model) throws Exception {
		String userDisplayName="HCL";
		log.debug("resource.attendance.sheet");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);
			return "resourceAttendance";
	}
	
	@RequestMapping(value="resource.attendance.availability.user")
	public String resourceAttendancebyUser(ModelMap model) throws Exception {
		String userDisplayName="HCL";
		log.debug("resource.attendance.sheet");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);
			return "resourceAttendance_user";
	}
	@RequestMapping(value="resource.management.plan")
	public String resourceManagement(ModelMap model) throws Exception {
		String userDisplayName="HCL";
		log.debug("resource.management.plan");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);
			return "resourceManagementPlan";
	}
	
	@RequestMapping(value="resource.management.plannew")
	public String resourceManagementNew(ModelMap model) throws Exception {
		String userDisplayName="HCL";
		log.debug("resource.management.plannew");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);
			return "resourceManagementPlannew";
	}
	
	@RequestMapping(value="resource.pool.management")
	public String resourcePoolManagement(ModelMap model) throws Exception {
		String userDisplayName="HCL";
		log.debug("resource.pool.management");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);
			return "resourcePool";
	}
	
	@RequestMapping(value="workpackage.testcase.execution.view")
	public String workpackageTestCaseExecution(ModelMap model,HttpServletRequest req) throws Exception {
		String userDisplayName="HCL";
		log.debug("workpackage.testcase.execution.view");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();
			req.getSession().setAttribute("tcermode", "edit");
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);
		//model.addAttribute("tcermode","edit");
			return "workpackageTestCaseExecutionView";
	}
	
	@RequestMapping(value="profile.management.plan")
	public String profileManagement(ModelMap model) throws Exception {
		String userDisplayName="HCL";
		log.info("profile.management.plan ---> ");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);
			return "profileManagementPlan";
	}

	@RequestMapping(value="view.reserved.resources")
	public String viewReservedResources(ModelMap model) throws Exception {
		String userDisplayName="HCL";
		log.debug("profile.management.plan");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);
			return "viewReservedResources";
	}
	
	@RequestMapping(value="administration.skill")
	public String adminSkill(ModelMap model) throws Exception {
		String userDisplayName="HCL";

		log.debug("inside navigation Controller and admin Skill method");
		try{
			String userName=SecurityContextHolder.getContext()
		     .getAuthentication().getName();			
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();			
		}catch(Exception e){}
		
		model.addAttribute("user",userDisplayName);	
		return "skill";		
	}
	
	@RequestMapping(value="administration.userskills.approve")
	public String adminApproveUserSkills(ModelMap model) throws Exception {
		String userDisplayName="HCL";

		log.debug("inside navigation Controller and admin userskills approve method");
		try{
			String userName=SecurityContextHolder.getContext()
		     .getAuthentication().getName();			
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();			
		}catch(Exception e){}
		
		model.addAttribute("user",userDisplayName);	
		return "approve_userskills";		
	}
	
	@RequestMapping(value="administration.environmentGroup")
	public String adminEnvironmentGroup(ModelMap model) throws Exception {
		String userDisplayName="HCL";

		log.debug("inside navigation Controller and admin environmentGroup method");
		try{
			String userName=SecurityContextHolder.getContext()
		     .getAuthentication().getName();			
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();			
		}catch(Exception e){}
		
		model.addAttribute("user",userDisplayName);	
		return "environmentGroup";		
	}
	@RequestMapping(value="testFactory.testFactories")
	public String viewTestFcatoryWorkShifts(ModelMap model) throws Exception {
		String userDisplayName="HCL";

		log.debug("inside navigation Controller and testFactoryWorkShifts method");
		try{
			String userName=SecurityContextHolder.getContext()
		     .getAuthentication().getName();			
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();			
		}catch(Exception e){}
		
		model.addAttribute("user",userDisplayName);	
		return "testFactoryManagePlan";		
	}
	
	
	@RequestMapping(value="shift.entries.manage")
	public String resourceTimeSheetManage(ModelMap model) throws Exception {
		String userDisplayName="HCL";

		log.debug("inside navigation Controller and shift.entries.manage");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();			
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();			
		}catch(Exception e){}
		
		model.addAttribute("user",userDisplayName);	
		return "shiftEntryManage";		
	}
	
	@RequestMapping(value="view.allReserved.resources")
	public String viewAllReservedResources(ModelMap model) throws Exception {
		String userDisplayName="HCL";

		log.debug("inside navigation Controller and view.allReserved.resources");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();			
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();			
		}catch(Exception e){}
		
		model.addAttribute("user",userDisplayName);	
		return "viewAllReservedResource";		
	}
	
	@RequestMapping(value="product.management.plan")
	public String productManagePlan(ModelMap model) throws Exception {
		String userDisplayName="HCL";

		log.debug("inside navigation Controller and product.management.plan");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();			
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();			
		}catch(Exception e){}
		
		model.addAttribute("user",userDisplayName);	
		return "productManagePlan";		
	}
	
	@RequestMapping(value="products.bot.plan")
	public String productsBotPlan(ModelMap model) throws Exception {
		String userDisplayName="HCL";

		log.debug("inside navigation Controller and Products.bot.plan");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();			
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();			
		}catch(Exception e){}
		
		model.addAttribute("user",userDisplayName);	
		return "myBots";		
	}
	
	
	
	
	
	@RequestMapping(value="resource.tracking.plan")
	public String resourceTrackingPlan(ModelMap model) throws Exception {
		String userDisplayName="HCL";

		log.debug("inside navigation Controller and resource.tracking.plan");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();			
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();			
		}catch(Exception e){}
		
		model.addAttribute("user",userDisplayName);	
		return "resourceTrackingPlan";		
	}
	@RequestMapping(value="resourceShiftCheckin.view")
	public String resourceShiftChackIn(ModelMap model) throws Exception {
		String userDisplayName="HCL";

		log.debug("inside navigation Controller and resourceShiftCheckin.view");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();			
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();			
		}catch(Exception e){}
		
		model.addAttribute("user",userDisplayName);	
		return "resourceShiftCheckin";		
	}
	
	@RequestMapping(value="resource.availability.report")
	public String resourceAvailabilityReport(ModelMap model) throws Exception {
		String userDisplayName="HCL";

		log.debug("inside navigation Controller and resource.availability.report");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();			
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();			
		}catch(Exception e){}
		
		model.addAttribute("user",userDisplayName);	
		return "resourceAvailabilityReport";		
	}
	
	@RequestMapping(value="resource.reliablity.report")
	public String resourceReliabilityReport(ModelMap model) throws Exception {
		String userDisplayName="HCL";

		log.debug("inside navigation Controller and resource.reliablity.report");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();			
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();			
		}catch(Exception e){}
		
		model.addAttribute("user",userDisplayName);	
		return "resourceReliabilityReport";		
	}
	
	@RequestMapping(value="view.resources.experience")
	public String viewResourcesExperience(ModelMap model) throws Exception {
		String userDisplayName="HCL";

		log.debug("inside navigation Controller and resource.reliablity.report");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();			
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();			
		}catch(Exception e){}
		
		model.addAttribute("user",userDisplayName);	
		return "resourceExperienceStatistics";		
	}
	@RequestMapping(value="myApprovals.view")
	public String viewMyApprovals(ModelMap model) throws Exception {
		String userDisplayName="HCL";

		log.debug("inside navigation Controller and resource.reliablity.report");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();			
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();			
		}catch(Exception e){}
		
		model.addAttribute("user",userDisplayName);	
		return "viewMyApprovals";		
	}
	
	@RequestMapping(value="profile.management.plan.content")
	public String profileManagementContent(ModelMap model,HttpServletRequest req) throws Exception {
		String userDisplayName="HCL";
		String selectedUserId = "";
		log.info("profile.management.plan ---> ");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();
			selectedUserId=req.getParameter("selecteduserId");
			req.getSession().setAttribute("selectedUserId", selectedUserId);
			
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);		
			return "profileManagementPlanContent";
	}
	@RequestMapping(value="customer.management.plan")
	public String viewCustomer(ModelMap model) throws Exception {
		String userDisplayName="HCL";
		log.info("customer.management.plan ---> ");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);
			return "customer";
	}
	
	@RequestMapping(value="adding.tabs.dasboard")
	public String addTabs(ModelMap model) throws Exception {
		String userDisplayName="HCL";
		log.info("adding.tabs.dasboard ---> ");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);
			return "dashboardAddTabs";
	}
	
	
	@RequestMapping(value="bulkPush.from.SqlToMongoDB")
	public String addBulkPushFromSqlToMongoDB(ModelMap model) throws Exception {
		String userDisplayName="HCL";
		log.info("bulkPush.from.SqlToMongoDB ---> ");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);
			return "bulkDataPush";
	}
	
	@RequestMapping(value="workpackage.resource.reservation")
	public String workpackageResourceReservation(ModelMap model) throws Exception {
		String userDisplayName="HCL";
		log.info("workpackage.resource.reservation ---> ");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);
			return "workpackageResourceReservation";
	}
	
	@RequestMapping(value="about.palm")
	public String aboutPalm(ModelMap model) throws Exception {
		String userDisplayName="HCL";
		log.info("about.palm ---> ");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);
			return "aboutPalm";
	}
	
	@RequestMapping(value="resource.management.dashboard")
	public String resourceManagerdashboard(ModelMap model) throws Exception {
		String userDisplayName="HCL";
		log.info("customer.management.plan ---> ");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);
			return "dashboardResourceManager";
	}
	
	@RequestMapping(value="administration.dashboard.status")
	public String dashboardStatus(ModelMap model) throws Exception {
		String userDisplayName="HCL";

		log.debug("inside navigation Controller and administration.dashboard.testing");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();			
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();			
		}catch(Exception e){}
		
		model.addAttribute("user",userDisplayName);	
		return "dashboardStatus";		
	}
	
	@RequestMapping(value="administration.dashboard.metricBoard")
	public String host2(ModelMap model) throws Exception {
		String userDisplayName="HCL";
		log.info("administration.dashboard.metricBoard ---> ");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);
			return "dashboardMetrics";
	}
	
	
	@RequestMapping(value="user.time.management")
	public String userTimeManagement(ModelMap model) throws Exception {
		String userDisplayName="HCL";
		log.info("customer.management.plan ---> ");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);
			return "userTimeManagement";
	}
	
	@RequestMapping(value="product.version.testcase.plan")
	public String productVersionTestcasePlan(ModelMap model) throws Exception {
		String userDisplayName="HCL";

		log.debug("inside navigation Controller and product.version.testcase.plan");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();			
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();			
		}catch(Exception e){}
		
		model.addAttribute("user",userDisplayName);	
		return "productVersionTestCasePlan";		
	}
	
	@RequestMapping(value="administration.host")
	public String host(ModelMap model) throws Exception {
		String userDisplayName="HCL";

		log.debug("inside navigation Controller and administration.host");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();			
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();			
		}catch(Exception e){}
		
		model.addAttribute("user",userDisplayName);	
		return "hostDetails";		
	}
	
	@RequestMapping(value="resources.productivity.quality.reports")
	public String resourcesReports(ModelMap model) throws Exception {
		String userDisplayName="HCL";

		log.debug("inside navigation Controller and administration.dashboard.testing");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();			
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();			
		}catch(Exception e){}
		
		model.addAttribute("user",userDisplayName);	
		return "resouceProductivityReports";
	}
	
	@RequestMapping(value="administration.product.team.users")
	public String productTeamUsers(ModelMap model) throws Exception {
		String userDisplayName="HCL";

		log.debug("inside navigation Controller and product.version.testcase.plan");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();			
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();			
		}catch(Exception e){}
		
		model.addAttribute("user",userDisplayName);	
		return "productTeamUsers";		
	}
	
	
	@RequestMapping(value="kibana.elastic.search")
	public String dashBoardWithKibana(ModelMap model) throws Exception {
		String userDisplayName="HCL";

		log.debug("inside navigation Controller and kibana.elastic.search");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();			
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();			
		}catch(Exception e){}
		
		model.addAttribute("user",userDisplayName);	
		return "kibanaDashBoard";		
	}
	
	
	@RequestMapping(value="administration.indicies")
	public String indicesForDashBoard(ModelMap model) throws Exception {
		String userDisplayName="HCL";

		log.debug("inside navigation Controller and administration.indicies");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();			
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();			
		}catch(Exception e){}
		
		model.addAttribute("user",userDisplayName);	
		return "indicesForDashboard";		
	}
	
	@RequestMapping(value="workpackage.testcase.plan.reviewDefects")
	public String showRDefectsReviewPage(ModelMap model) throws Exception {
		String userDisplayName="HCL";

		log.debug("inside navigation workpackage.testcase.plan.reviewDefects");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();			
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();			
		}catch(Exception e){}
		
		model.addAttribute("user",userDisplayName);	
		return "reviewDefects";		
	}
	
	
	@RequestMapping(value="defects.weekly.report")
	public String defectsWeeklyReport(ModelMap model) throws Exception {
		String userDisplayName="HCL";

		log.debug("defects.weekly.report");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();			
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();			
		}catch(Exception e){}
		
		model.addAttribute("user",userDisplayName);	
		return "defectsWeeklyReport";		
	}
	
	@RequestMapping(value="workpackage.analyse.defects")
	public String showDefectAnalysePage(ModelMap model) throws Exception {
		String userDisplayName="HCL";

		log.debug("inside navigation workpackage.analyse.defects");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();			
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();			
		}catch(Exception e){}
		
		model.addAttribute("user",userDisplayName);	
		return "analyseDefects";		
	}
	
	@RequestMapping(value="resource.calendar.monthly.view")
	public String resourceCalendarView(ModelMap model) throws Exception {
		String userDisplayName="HCL";
		log.debug("resource.calendar.monthly.view");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);
			return "resourceCalendarView";
	}
	
	@RequestMapping(value="process.activityWorkPackage")
	public String processActivityWorkPackage(ModelMap model) throws Exception {
		String userDisplayName="HCL";
		log.debug("process.workrequest");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();			
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();			
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);	
		return "activityWorkPackageManagement";		
	}
	
	@RequestMapping(value="administration.activityType")
	public String adminActivityGroup(ModelMap model) throws Exception {
		String userDisplayName="HCL";

		log.info("inside navigation Controller and admin adminActivityGroup method");
		try{
			String userName=SecurityContextHolder.getContext()
		     .getAuthentication().getName();			
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();			
		}catch(Exception e){}
		
		model.addAttribute("user",userDisplayName);	
		return "activityType";		
	}
	
	@RequestMapping(value="process.activity")
	public String processActivity(ModelMap model) throws Exception {
		String userDisplayName="HCL";
		log.debug("process.activity");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();			
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();	
			Integer userId = userListService.getUserListByUserName(userName).getUserId();
			model.addAttribute("userId", userId);
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);	
		return "activityManagement";		
	}
	@RequestMapping(value="process.activityTask")
	public String processActivityTask(ModelMap model) throws Exception {
		String userDisplayName="HCL";
		log.debug("process.activityTask");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();			
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();	
			Integer userId = userListService.getUserListByUserName(userName).getUserId();
			model.addAttribute("userId", userId);
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);	
		return "activityTaskManagement";		
	}
	
	@RequestMapping(value="process.activityGrouping")
	public String processActivityGrouping(ModelMap model) throws Exception {
		String userDisplayName="HCL";
		log.debug("process.activityGrouping");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();			
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();	
			Integer userId = userListService.getUserListByUserName(userName).getUserId();
			model.addAttribute("userId", userId);
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);	
		return "activityGroupingManagement";		
	}
	
	@RequestMapping(value="show.overall.demand.and.reservation")
	public String showOverallDemandAndReservation(ModelMap model) throws Exception {
		String userDisplayName="HCL";
		log.debug("show.overall.demand.and.reservation");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();			
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();	
			Integer userId = userListService.getUserListByUserName(userName).getUserId();
			model.addAttribute("userId", userId);
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);	
		return "resourceDemandAndReservationWeekly";		
	}
	
	@RequestMapping(value="process.review.activity")
	public String reviewActivity(ModelMap model) throws Exception {
		String userDisplayName="HCL";
		log.debug("process.activityTask");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();			
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();			
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);	
		return "reviewActivity";		
	}
	
	@RequestMapping(value="process.assigned.activity")
	public String viewAssignedActivities(ModelMap model) throws Exception {
		String userDisplayName="HCL";
		log.debug("process.assigned.activity");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();			
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();
			Integer userId = userListService.getUserListByUserName(userName).getUserId();
			model.addAttribute("userId", userId);
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);	
		return "assignedActivity";		
	}
	
	
	@RequestMapping(value="pqa.review")
	public String viewPqaReview(ModelMap model) throws Exception {
		String userDisplayName="HCL";
		log.debug("process.assigned.activity");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();			
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();			
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);	
		return "pqaReview";		
	}
	
	@RequestMapping(value="environment.combination.report")
	public String environmentReport(ModelMap model) throws Exception {
		String userDisplayName="HCL";
		log.debug("environment.combination.report");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();			
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();			
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);	
		return "environmentReports";		
	}
	
	@RequestMapping(value="competency.management.plan")
	public String competencyManagePlan(ModelMap model) throws Exception {
		String userDisplayName="HCL";

		log.debug("inside navigation Controller and competency.management.plan");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();			
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();		
		}catch(Exception e){}
		
		model.addAttribute("user",userDisplayName);	
		return "competencyManagePlan";		
	}
	
	@RequestMapping(value="activities.status.report")
	public String viewActivitiesStatusReport(ModelMap model) throws Exception {
		String userDisplayName="HCL";
		log.debug("activities.status.report");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();			
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();			
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);	
		return "activityStatusReport";		
	}
	
	@RequestMapping(value="generate.report")
	public String generateReport(ModelMap model) throws Exception {
		String userDisplayName="HCL";
		log.debug("generate.report");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();			
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();			
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);	
		return "generateReport";		
	}
	
	@RequestMapping(value="data.source.extractor")
	public String dataSourceExtractor(ModelMap model) throws Exception {
		String userDisplayName="HCL";

		log.debug("inside navigation Controller and data.source.extractor");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();			
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();		
			
			List<Customer> customers = customerService.listCustomer(2, 1, 1);
			if(customers != null && customers.size() > 0){
				model.addAttribute("customerId", customers.get(0).getCustomerId());
			}
		}catch(Exception e){}
		
		model.addAttribute("user",userDisplayName);	
		
		return "dataSourceExtractorManage";		
	}
	
	@RequestMapping(value="dashboard.data.extractor.process")
	public String dataExtractionProcess(ModelMap model) throws Exception {
		String userDisplayName="HCL";

		log.debug("inside navigation Controller and dashboard.data.extractor.process");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();			
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();		
		}catch(Exception e){}
		
		model.addAttribute("user",userDisplayName);	
		return "dataExtractionProcess";		
	}
	
	@RequestMapping(value="dimension.management.plan")
	public String dimensionProcess(ModelMap model) throws Exception {
		String userDisplayName="HCL";

		log.debug("inside navigation Controller and dimension.management.plan");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();			
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();		
		}catch(Exception e){}
		
		model.addAttribute("user",userDisplayName);	
		return "dimensionManagePlan";		
	}
	
	@RequestMapping(value="status.management.plan")
	public String statusManagePlan(ModelMap model) throws Exception {
		String userDisplayName="HCL";

		log.debug("inside navigation Controller and status.management.plan");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();			
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();		
		}catch(Exception e){}
		
		model.addAttribute("user",userDisplayName);	
		return "statusManagePlan";		
	}
	
	@RequestMapping(value="administration.BDDkeywordsphrases")
	public String BDDkeywordsphrases(ModelMap model) throws Exception {
		String userDisplayName="HCL";

		log.debug("inside navigation Controller and administration.BDDkeywordsphrases");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();			
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();		
		}catch(Exception e){}
		
		model.addAttribute("user",userDisplayName);	
		return "bddkeywordsphrases";		
	}
	
	@RequestMapping(value="workflow.my.actions")
	public String getWorkflowMyActionsPage(ModelMap model) throws Exception {
		String userDisplayName="HCL";

		log.debug("inside navigation Controller and workflow.my.actions");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();			
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();		
		}catch(Exception e){}
		
		model.addAttribute("user",userDisplayName);	
		return "workflowMyActions";		
	}
	
	
	@RequestMapping(value="my.activities")
	public String getMyActivities(ModelMap model) throws Exception {
		String userDisplayName="HCL";

		log.debug("inside navigation Controller and my.activities");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();			
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();		
		}catch(Exception e){}
		
		model.addAttribute("user",userDisplayName);	
		return "myActivities";		
	}
	
	@RequestMapping(value="workflows.management.plan")
	public String viewWorkflowMaster(ModelMap model) throws Exception {
		String userDisplayName="HCL";
		log.debug("workflows.management.plan");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);
			return "workflowMaster";
	}
	
	@RequestMapping(value="workflow.product.entity.mapping")
	public String getWorkflowProductEntityMappingPage(ModelMap model) throws Exception {
		String userDisplayName="HCL";

		log.debug("inside navigation Controller and workflow.my.actions");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();			
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();		
		}catch(Exception e){}
		
		model.addAttribute("user",userDisplayName);	
		return "workflowProductEntityMapping";		
	}	
	
	@RequestMapping(value="workflow.summary")
	public String getWorkflowSummaryPage(ModelMap model) throws Exception {
		String userDisplayName="HCL";

		log.debug("inside navigation Controller and workflow.summary");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();			
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();		
		}catch(Exception e){}
		
		model.addAttribute("user",userDisplayName);	
		return "workflowSummary";		
	}
	@RequestMapping(value="engagement.product.clarifications")
	public String showClarifications(ModelMap model) throws Exception {
		String userDisplayName="HCL";

		log.debug("inside navigation Controller and engagement.product.clarifications");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();			
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();		
		}catch(Exception e){}
		
		model.addAttribute("user",userDisplayName);	
		return "clarification";		
	}
	
	@RequestMapping(value="entity.type.management.plan")
	public String getTypeManagementPage(ModelMap model) throws Exception {
		String userDisplayName="HCL";

		log.debug("inside navigation Controller and entity.type.management.plan");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();			
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();		
		}catch(Exception e){}
		
		model.addAttribute("user",userDisplayName);	
		return "typeManagement";		
	}
	
	@RequestMapping(value="notification.product.management")
	public String getNotificationManagementPage(ModelMap model) throws Exception {
		String userDisplayName="HCL";

		log.debug("inside navigation Controller and notification.product.management");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();			
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();		
		}catch(Exception e){}
		
		model.addAttribute("user",userDisplayName);	
		return "notificationManagement";		
	}
	
	@RequestMapping(value="activitytask.effort.report")
	public String viewResourceEffortReport(ModelMap model) throws Exception {
		String userDisplayName="HCL";
		log.debug("activitytask.effort.report");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();			
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();			
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);	
		return "activitytaskeffortreport";		
	}
	
	@RequestMapping(value="resource.effort.report")
	public String viewActivityTaskEffortReport(ModelMap model) throws Exception {
		String userDisplayName="HCL";
		log.debug("resource.effort.report");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();			
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();			
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);	
		return "resourceEffortReport";		
	}
	
	@RequestMapping(value="pivot.report")
	public String generatePivotReport(ModelMap model) throws Exception {
		String userDisplayName="HCL";
		log.debug("pivot.report");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();			
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();			
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);	
		return "pivotreport";		
	}
	@RequestMapping(value="pivot.nreco.report")
	public String generatePivotNRecoReport(ModelMap model) throws Exception {
		String userDisplayName="HCL";
		log.debug("pivot.report");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();			
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();			
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);	
		return "pivotnrecoreport";		
	}
	@RequestMapping(value="pivot.nreco.advance.create.report")
	public String generatePivotNRecoAdvanceCreateReport(ModelMap model) throws Exception {
		String userDisplayName="HCL";
		log.debug("pivot.nreco.advance.create.report");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();			
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();			
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);	
		return "pivotnrecoadvancecreatereport";		
	}@RequestMapping(value="pivot.nreco.advance.report")
	public String generatePivotNRecoAdvanceReport(ModelMap model,HttpServletRequest request,HttpServletResponse response) throws Exception {
		String userDisplayName="HCL";
		log.debug("pivot.nreco.advance.report");
		try{
			//res.addHeader("Access-Control-Allow-Origin", "*");
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();			
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();
			/*response.setHeader("Access-Control-Allow-Origin", "http://localhost:5000");
		    response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
		    response.setHeader("Access-Control-Max-Age", "3600");
		    response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
		    */
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);	
		return "pivotnrecoadvancereport";		
	}
	
	@RequestMapping(value="custom.field.configuration")
	public String getCustomFieldConfiguration(ModelMap model) throws Exception {
		String userDisplayName="HCL";
		log.debug("pivot.report");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();			
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();			
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);	
		return "customFieldConfiguration";		
	}
	
	@RequestMapping(value="db.backup.restore")
	public String mysqlDbBackupAndRestoreProcess(ModelMap model) throws Exception {
		String userDisplayName="HCL";
		log.debug("db.backup.restore");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();			
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();			
		}catch(Exception e){}
		model.addAttribute("user",userDisplayName);	
		return "dbbackuprestore";		
	}
	
    @RequestMapping(value="bot.details.configuration")
    public String getBotDetailsConfiguration(ModelMap model) throws Exception {
           String userDisplayName="HCL";
           try{
                  String userName=SecurityContextHolder.getContext().getAuthentication().getName();                  
                  setUserNameAndRole(userName);
                  userDisplayName = getUserNameAndRole();              
           }catch(Exception e){}
           model.addAttribute("user",userDisplayName);   
           return "botConfiguration";             
    }
    
    
    @RequestMapping(value="automation.bot.repository")
    public String getBotRepository(ModelMap model) throws Exception {
           String userDisplayName="HCL";
           try{
                  String userName=SecurityContextHolder.getContext().getAuthentication().getName();                  
                  setUserNameAndRole(userName);
                  userDisplayName = getUserNameAndRole();              
           }catch(Exception e){}
           model.addAttribute("user",userDisplayName);   
           return "botStore";             
    }
    
    
    @RequestMapping(value="administration.bot.command.master")
    public String getBotCommandMaster(ModelMap model) throws Exception {
           String userDisplayName="HCL";
           try{
                  String userName=SecurityContextHolder.getContext().getAuthentication().getName();                  
                  setUserNameAndRole(userName);
                  userDisplayName = getUserNameAndRole();              
           }catch(Exception e){}
           model.addAttribute("user",userDisplayName);   
           return "botCommandMaster";             
    }
    
    @RequestMapping(value="keyword.libarary")
   	public String keywordApprovalstatus(ModelMap model) throws Exception {
   		String userDisplayName="HCL";

   		log.info("inside navigation Controller ");
   		try{
   			String userName=SecurityContextHolder.getContext().getAuthentication().getName();			
   			setUserNameAndRole(userName);
   			userDisplayName = getUserNameAndRole();			
   		}catch(Exception e){			log.error("Error:"+e);
   }
   		
   		model.addAttribute("user",userDisplayName);	
   		return "keywordWordLibrary";		
   	}

    @RequestMapping(value="audit.history.byUser")
    public String getAuditHistoryByUser(ModelMap model) throws Exception {
           String userDisplayName="HCL";
           try{
                  String userName=SecurityContextHolder.getContext().getAuthentication().getName();                  
                  setUserNameAndRole(userName);
                  userDisplayName = getUserNameAndRole();              
           }catch(Exception e){}
           model.addAttribute("user",userDisplayName);   
           return "myAudits";             
    }
    
    @RequestMapping(value="administration.onboard.user.approval")
	public String userApprove(ModelMap model) throws Exception {
		String userDisplayName="HCL";

		log.debug("inside navigation Controller and administration.user.permission");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();			
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();			
		}catch(Exception e){}
		
		model.addAttribute("user",userDisplayName);	
		return "userApproval";		
	}
    
    
    @RequestMapping(value="administration.report.issue")
   	public String reportfIssue(ModelMap model) throws Exception {
   		String userDisplayName="HCL";

   		log.debug("inside navigation Controller and administration.report.issue");
   		try{
   			String userName=SecurityContextHolder.getContext().getAuthentication().getName();			
   			setUserNameAndRole(userName);
   			userDisplayName = getUserNameAndRole();			
   		}catch(Exception e){}
   		
   		model.addAttribute("user",userDisplayName);	
   		return "reportIssue";		
   	}
    
    @RequestMapping(value={"testengines.languages.modes.map.list"})
    public String getTestEngineLanguagesMode(ModelMap model) throws Exception {
        String userDisplayName = "HCL";
        try {
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            this.setUserNameAndRole(userName);
            userDisplayName = this.getUserNameAndRole();
        }
        catch (Exception e) {
            log.error((Object)("Error:" + e));
        }
        model.addAttribute("user", (Object)userDisplayName);
        return "testEnginesLanguagesModesMap";
    }
    
	@RequestMapping(value="atlas.testplan.component") 
	public ModelAndView atlasTestPlan(HttpServletRequest req,HttpServletResponse res) throws Exception {
		log.debug("inside navigation Controller and atlas home method");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();
			setUserNameAndRole(userName);
		}catch(Exception e){}
		return new ModelAndView("testRunPlanComponent");
	}
	
	@RequestMapping(value="atlas.product.management.plan") 
	public ModelAndView atlasProductManagement(HttpServletRequest req,HttpServletResponse res) throws Exception {
		log.debug("inside navigation Controller and atlas home method");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();
			setUserNameAndRole(userName);
		}catch(Exception e){}
		return new ModelAndView("productManagePlanComponent");
	}

	@RequestMapping(value="atlas.workpackage.summary") 
	public ModelAndView atlasWorkpackageSummary(HttpServletRequest req,HttpServletResponse res) throws Exception {
		log.debug("inside navigation Controller and atlas home method");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();
			setUserNameAndRole(userName);
		}catch(Exception e){}
		return new ModelAndView("workPackageTestCasePlanComponent");
	}
	
	@RequestMapping(value="atlas.admin.environmentGroup")
	public String AtlasEnvironmentGroup(ModelMap model) throws Exception {
		String userDisplayName="HCL";

		log.debug("inside navigation Controller and atlas admin environmentGroup method");
		try{
			String userName=SecurityContextHolder.getContext()
		     .getAuthentication().getName();			
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();			
		}catch(Exception e){}
		
		model.addAttribute("user",userDisplayName);	
		return "environmentGroupComponent";		
	}
	
	@RequestMapping(value="atlas.administration.host")
	public String AtlasHost(ModelMap model) throws Exception {
		String userDisplayName="HCL";

		log.debug("inside navigation Controller and atlas.administration.host");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();			
			setUserNameAndRole(userName);
			userDisplayName = getUserNameAndRole();			
		}catch(Exception e){}
		
		model.addAttribute("user",userDisplayName);	
		return "hostDetailsComponent";		
	}
	
	@RequestMapping(value="atlas.component") 
	public ModelAndView atlasMainComponent(HttpServletRequest req,HttpServletResponse res) throws Exception {
		log.debug("inside navigation Controller and atlas home method");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();
			setUserNameAndRole(userName);
		}catch(Exception e){}
		return new ModelAndView("atlasComponent");
	}
	
	@RequestMapping(value="atlas.test.engagement.component") 
	public ModelAndView atlasTestEnagementComponent(HttpServletRequest req,HttpServletResponse res) throws Exception {
		log.debug("inside navigation Controller and atlas home method");
		try{
			String userName=SecurityContextHolder.getContext().getAuthentication().getName();
			setUserNameAndRole(userName);
		}catch(Exception e){}
		return new ModelAndView("testEngagementComponent");
	}
	
	@RequestMapping(value="atlas.skip.login")
    public String skipLogin(ModelMap model) throws Exception {
           String userDisplayName="HCL";
           try{
                              
           }catch(Exception e){}
           model.addAttribute("user",userDisplayName);   
           return "skipLogin";             
    }
    
    @RequestMapping(value="atlas.skip.hidden.login.page")
    public String skipLoginHiddenPage(ModelMap model) throws Exception {
           String userDisplayName="HCL";
           try{
                              
           }catch(Exception e){}
           model.addAttribute("user",userDisplayName);   
           return "skipHiddenLoginPage";             
    }
    
    @RequestMapping(value="tool.intagration.master")
    public String toolIntagrationMasterPage(ModelMap model) throws Exception {
           String userDisplayName="HCL";
           try{
                              
           }catch(Exception e){}
           model.addAttribute("user",userDisplayName);   
           return "ToolIntagrationMaster";             
    }
    
	@RequestMapping(value="administration.validate.license.expiry",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions checkLicenseExpiry() {
		log.debug("administration.validate.license.expiry");
		JTableResponseOptions jTableResponseOptions = null;
			try {
				String filePath = request.getServletContext().getRealPath(File.separator)+File.separator+"data"+File.separator+"license.properties";
				String response = licenseCheckService.licenseAgrementValidation(filePath);
				log.info("Response from licenseAgrementValidation : "+response);
				if(response.contains("success"))
					jTableResponseOptions = new JTableResponseOptions("OK","Active License available.");
				else
					jTableResponseOptions = new JTableResponseOptions("ERROR","License expired. Please contact the TAF admin.");
		        } catch (Exception e) {
		        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error in checking license!");
		            log.error("JSON ERROR", e);
		        }
		        
	        return jTableResponseOptions;
    } 
}
