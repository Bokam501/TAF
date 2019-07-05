package com.hcl.atf.taf.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.ActualShift;
import com.hcl.atf.taf.model.DeviceList;
import com.hcl.atf.taf.model.TestRunList;
import com.hcl.atf.taf.model.UserActivityTracker;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.UserRoleMaster;
import com.hcl.atf.taf.model.WorkShiftMaster;
import com.hcl.atf.taf.model.custom.TestRunListCustom;
import com.hcl.atf.taf.model.json.JsonDeviceList;
import com.hcl.atf.taf.model.json.JsonTestRunList;
import com.hcl.atf.taf.model.json.jtable.JTableResponse;
import com.hcl.atf.taf.service.DeviceListService;
import com.hcl.atf.taf.service.LicenseCheckService;
import com.hcl.atf.taf.service.TestExecutionService;
import com.hcl.atf.taf.service.TestRunConfigurationService;
import com.hcl.atf.taf.service.UserActivityTrackerService;
import com.hcl.atf.taf.service.UserListService;
import com.hcl.atf.taf.service.WorkPackageService;
@Controller
public class HomePageController {

	private static final Log log = LogFactory.getLog(HomePageController.class);
	
	@Autowired
	private TestRunConfigurationService testRunConfigurationService;
	@Autowired
	private TestExecutionService testExecutionService;
	@Autowired 
	private UserListService userListService;
	@Autowired 
	private DeviceListService deviceListService;
	
	
	@Autowired 
	private WorkPackageService workPackageService;
	
	@Autowired 
	private UserActivityTrackerService userActivityTrackerService;
	@Autowired
	private LicenseCheckService licenseCheckService;
	
	private final String strACIVEStatus="ACTIVE";
	
	@RequestMapping(value="home.test.executed.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listExecutedRunList(@RequestParam int jtStartIndex, @RequestParam int jtPageSize) {
		log.debug("inside home.test.executed.list");
		JTableResponse jTableResponse;
			try {
			List<TestRunList> testRunList = testExecutionService.listExecutedTestRunList(24,jtStartIndex,jtPageSize);
			List<JsonTestRunList> jsonTestRunList=new ArrayList<JsonTestRunList>();
			for(TestRunList trl: testRunList){
				log.info("inside the controller of the inside home.test.executed.list triggered time is:"+trl.getTestRunTriggeredTime());
				jsonTestRunList.add(new JsonTestRunList(trl));
			}
		        jTableResponse = new JTableResponse("OK", jsonTestRunList,testExecutionService.getTotalTestRunListInLast24Hours(24));
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching ExecutedRunList!");
	            log.error("JSON ERROR fetching ExecutedRunList", e);
	        }	        
        return jTableResponse;
    }
	
	@RequestMapping(value="administration.device.active.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listDevices(@RequestParam int jtStartIndex, @RequestParam int jtPageSize) {
		log.debug("inside administration.device.list");
		
		JTableResponse jTableResponse;
		
			try {
			
			List<DeviceList> deviceList=deviceListService.list(strACIVEStatus,jtStartIndex,jtPageSize);
			
			List<JsonDeviceList> jsonDeviceList=new ArrayList<JsonDeviceList>();
			for(DeviceList dl: deviceList){
				jsonDeviceList.add(new JsonDeviceList(dl));
				
			}
			
	            jTableResponse = new JTableResponse("OK", jsonDeviceList,deviceListService.list(strACIVEStatus).size());
	            
	            
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);	            
	        }
	        
        return jTableResponse;
    }
	
	@RequestMapping(value="home")
	public ModelAndView home(HttpServletRequest req,HttpServletResponse res,ModelMap model,@RequestParam Integer filter) throws Exception {
		String userDisplayName="HCL";		
		String userName="";
		String roleName = "";
		int roleId = 0;
		String modelName = "home_new";
		UserList user = null;
		ActualShift actualShift=null;
		WorkShiftMaster workShiftMaster=null;
		try{
			List<TestRunList> testRuns = new ArrayList<TestRunList>();
			List<TestRunListCustom> testRunListCustom = new ArrayList<TestRunListCustom>();
			testRuns = testExecutionService.listExecutingTestRunList();
			testRunListCustom = testExecutionService.getExecutedStepsCountofTestRunlist(testRuns);
			model.addAttribute("testRunListCustomValue",testRunListCustom);
			model.addAttribute("sizeActiveDeviceList",deviceListService.list(strACIVEStatus).size());
			HttpSession session = req.getSession();
			//Add License Code here
			String filePath = req.getServletContext().getRealPath(File.separator)+File.separator+"data"+File.separator+"license.properties";
			String response = licenseCheckService.licenseAgrementValidation(filePath); 
			log.info("Response from licenseAgrementValidation : "+response);
			if(!response.contains("success")){
				return new ModelAndView("error_new","message","License expired. Please contact the TAF admin.");
			}else{
				//Do nothing acnd continue to login
				log.info("Active License available.");
			}
			if(filter==-1){
				String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
				log.info("loginId in home () of Home Page Controller: "+loginId);
				user = userListService.getUserListByUserName(loginId);
				roleName = user.getUserRoleMaster().getRoleName();
				roleId = user.getUserRoleMaster().getUserRoleId();
				userDisplayName = user.getUserDisplayName()+" ["+user.getUserRoleMaster().getRoleLabel()+"]";
			}else{
				user=(UserList)req.getSession().getAttribute("USER");
				roleName=(String)req.getSession().getAttribute("roleName");
				roleId=(Integer)req.getSession().getAttribute("roleId");
				userDisplayName = user.getUserDisplayName()+" ["+roleName+"]";
			}
			userName = user.getUserDisplayName();
			String imageName = user.getImageURI();
			session.setAttribute("USER", user);
			session.setAttribute("currentWorkPackageResourceDemandWeekNo", DateUtility.getWeekOfYear());
			session.setAttribute("ssnHdnUserId", user.getUserId().intValue());
			session.setAttribute("ssnHdnSelectedResourceId", user.getUserId().intValue());
			session.setAttribute("ssnHdnResourceAvailabilityWeekNo", DateUtility.getWeekOfYear());
			session.setAttribute("roleName", roleName);
			session.setAttribute("userDisplayName", userDisplayName);
			log.info("roleName --> "+roleName);
			
			session.setAttribute("imageName", imageName);
			session.setAttribute("roleId", roleId);			
			log.info("Set current week for demand projection in after login in Home : " + DateUtility.getWeekOfYear());
			
		 String userLocale = req.getHeader("Accept-Language");
			 Locale locale = req.getLocale();
			 String localeId=locale.getLanguage()+"_"+locale.getCountry();
			 Locale localeNew= new Locale(locale.getLanguage(),locale.getCountry());
			 
			 session.setAttribute("locale", locale);
			 session.setAttribute("localeId", localeId);
			 if(user != null) {
					UserActivityTracker userActivityTracker=userActivityTrackerService.getUserActivityTracker(user.getUserId());
		             if(userActivityTracker !=null) {
		            	 session.setAttribute("userLoginTime",userActivityTracker.getUserLoginTime());
		            	 session.setAttribute("userLogoutTime",userActivityTracker.getUserLogoutTime());
		             }
				}
			 UserActivityTracker userActivityTracker=new UserActivityTracker();
			 userActivityTracker.setUser(user);
			 userActivityTracker.setUserLoginTime(new Date());
			 userActivityTracker.setReason("User Enter into login");
			 userActivityTrackerService.addUserActivityTracker(userActivityTracker);
		}catch(Exception e){
			log.error("Problem in initializing in Home Page", e);
		}
		return new ModelAndView(modelName, "user", userDisplayName);
	}
	
	@RequestMapping(value="home.changerole",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody String homeChangerole(HttpServletRequest req,HttpServletResponse res,ModelMap model,@RequestParam Integer userId,@RequestParam Integer roleId) throws Exception {
		String userDisplayName="HCL";
		String userName="";
		String roleName = "";
		String modelName = "home_new";
		UserList user = null;
		ActualShift actualShift=null;
		WorkShiftMaster workShiftMaster=null;
		try{
			user = userListService.getUserListById(userId);
			//roleName = user.getUserRoleMaster().getRoleName();
			userName = user.getUserDisplayName();
			String imageName = user.getImageURI();
			UserRoleMaster userRoleMaster = userListService.getRoleById(roleId);
			roleName=userRoleMaster.getRoleName();
			//roleId = user.getUserRoleMaster().getUserRoleId();
			
			userDisplayName = user.getUserDisplayName()+" ["+roleName+"]";
			HttpSession session = req.getSession();
			session.removeValue(roleName);
			session.removeAttribute(roleName);
			session.setAttribute("USER", user);
			session.setAttribute("currentWorkPackageResourceDemandWeekNo", DateUtility.getWeekOfYear());
			session.setAttribute("ssnHdnUserId", user.getUserId().intValue());
			session.setAttribute("ssnHdnSelectedResourceId", user.getUserId().intValue());
			session.setAttribute("ssnHdnResourceAvailabilityWeekNo", DateUtility.getWeekOfYear());
			session.setAttribute("roleName", roleName);
			session.setAttribute("userDisplayName", userDisplayName);
			session.setAttribute("imageName", imageName);
			session.setAttribute("roleId", roleId);
			String userLocale = req.getHeader("Accept-Language");
			Locale locale = req.getLocale();
			String localeId=locale.getLanguage()+"_"+locale.getCountry();
			Locale localeNew= new Locale(locale.getLanguage(),locale.getCountry());
			 
			session.setAttribute("locale", locale);
			session.setAttribute("localeId", localeId);

		}catch(Exception e){
			log.error("Problem in initializing in Home Page", e);
		}
		return userDisplayName;//new ModelAndView(modelName, "user", userDisplayName);
	}
	
	@ExceptionHandler(Exception.class)
	public void handleException(Throwable ex, HttpServletResponse response)
			throws Exception {

		response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
				ex.getMessage());
		ex.printStackTrace();
	}
	
	@RequestMapping(value="customerlogin")
	public ModelAndView customerLogin(HttpServletRequest req,HttpServletResponse res,ModelMap model) throws Exception {
		log.info("*************** customerlogin ************ "+req.getAttribute("j_custUsername").toString());
		String userDisplayName="HCL";		
		String userName="";
		String roleName = "";
		int roleId = 0;
		String modelName = "home_new";
		UserList user = null;
		ActualShift actualShift=null;
		WorkShiftMaster workShiftMaster=null;
		try{
			List<TestRunList> testRuns = new ArrayList<TestRunList>();
			List<TestRunListCustom> testRunListCustom = new ArrayList<TestRunListCustom>();
			testRuns = testExecutionService.listExecutingTestRunList();
			testRunListCustom = testExecutionService.getExecutedStepsCountofTestRunlist(testRuns);
			model.addAttribute("testRunListCustomValue",testRunListCustom);
			model.addAttribute("sizeActiveDeviceList",deviceListService.list(strACIVEStatus).size());
			HttpSession session = req.getSession();
			
			userName = user.getUserDisplayName();
			String imageName = user.getImageURI();
			session.setAttribute("USER", user);
			session.setAttribute("currentWorkPackageResourceDemandWeekNo", DateUtility.getWeekOfYear());
			session.setAttribute("ssnHdnUserId", user.getUserId().intValue());
			session.setAttribute("ssnHdnSelectedResourceId", user.getUserId().intValue());
			session.setAttribute("ssnHdnResourceAvailabilityWeekNo", DateUtility.getWeekOfYear());
			session.setAttribute("roleName", roleName);
			session.setAttribute("userDisplayName", userDisplayName);
			log.info("roleName --> "+roleName);
			
			
			session.setAttribute("imageName", imageName);
			session.setAttribute("roleId", roleId);			
			log.info("Set current week for demand projection in after login in Home : " + DateUtility.getWeekOfYear());
			String userLocale = req.getHeader("Accept-Language");
			 Locale locale = req.getLocale();
			 String localeId=locale.getLanguage()+"_"+locale.getCountry();
			 Locale localeNew= new Locale(locale.getLanguage(),locale.getCountry());
			 
			 session.setAttribute("locale", locale);
			 session.setAttribute("localeId", localeId);
			 
		}catch(Exception e){
			log.error("Problem in initializing in Home Page", e);
		}
		return new ModelAndView(modelName, "user", userDisplayName);
	}
}
