package com.hcl.atf.taf.controller;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jettison.json.JSONObject;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.constants.TAFConstants;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.controller.utilities.ValidationUtility;
import com.hcl.atf.taf.dao.WorkShiftMasterDAO;
import com.hcl.atf.taf.integration.data.UserDataIntegrator;
import com.hcl.atf.taf.integration.data.excel.ExcelTestDataIntegrator;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.ResourceAvailability;
import com.hcl.atf.taf.model.ShiftTypeMaster;
import com.hcl.atf.taf.model.Skill;
import com.hcl.atf.taf.model.TestFactory;
import com.hcl.atf.taf.model.TestFactoryLab;
import com.hcl.atf.taf.model.TestFactoryResourceReservation;
import com.hcl.atf.taf.model.TestfactoryResourcePool;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.UserResourcePoolMapping;
import com.hcl.atf.taf.model.UserRoleMaster;
import com.hcl.atf.taf.model.UserSkills;
import com.hcl.atf.taf.model.UserTypeMasterNew;
import com.hcl.atf.taf.model.WorkPackage;
import com.hcl.atf.taf.model.WorkPackageDemandProjection;
import com.hcl.atf.taf.model.WorkShiftMaster;
import com.hcl.atf.taf.model.dto.ResourceAttendanceSummaryDTO;
import com.hcl.atf.taf.model.dto.ResourceAvailabilityDTO;
import com.hcl.atf.taf.model.dto.TestFactoryResourceReservationDTO;
import com.hcl.atf.taf.model.dto.UserWeekUtilisedTimeDTO;
import com.hcl.atf.taf.model.dto.WorkPackageDemandProjectionDTO;
import com.hcl.atf.taf.model.dto.WorkPackageDemandProjectionStatisticsDTO;
import com.hcl.atf.taf.model.json.JsonReservedResourcesForBooking;
import com.hcl.atf.taf.model.json.JsonResourceAttendance;
import com.hcl.atf.taf.model.json.JsonResourceAvailability;
import com.hcl.atf.taf.model.json.JsonResourceAvailabilityDetails;
import com.hcl.atf.taf.model.json.JsonResourceAvailabilityPlan;
import com.hcl.atf.taf.model.json.JsonResourceDemandAndAvailabilityView;
import com.hcl.atf.taf.model.json.JsonResourcePool;
import com.hcl.atf.taf.model.json.JsonResourcePoolSummary;
import com.hcl.atf.taf.model.json.JsonResourceShiftCheckinDetailsForWeek;
import com.hcl.atf.taf.model.json.JsonSkill;
import com.hcl.atf.taf.model.json.JsonTestFactory;
import com.hcl.atf.taf.model.json.JsonTestFactoryResourceReservation;
import com.hcl.atf.taf.model.json.JsonUserList;
import com.hcl.atf.taf.model.json.JsonUserResourcePoolMapping;
import com.hcl.atf.taf.model.json.JsonUserSkills;
import com.hcl.atf.taf.model.json.JsonWorkPackageDemandProjection;
import com.hcl.atf.taf.model.json.JsonWorkPackageDemandProjectionList;
import com.hcl.atf.taf.model.json.JsonWorkPackageResourceReservation;
import com.hcl.atf.taf.model.json.JsonWorkPackageResultsStatistics;
import com.hcl.atf.taf.model.json.JsonWorkPackageWeeklyDemandProjection;
import com.hcl.atf.taf.model.json.JsonWorkPackageWeeklyResourceReservation;
import com.hcl.atf.taf.model.json.calHeatMap.JCalHeatMapEntity;
import com.hcl.atf.taf.model.json.calHeatMap.JCalHeatMapResponse;
import com.hcl.atf.taf.model.json.jtable.JTableResponse;
import com.hcl.atf.taf.model.json.jtable.JTableResponseOptions;
import com.hcl.atf.taf.model.json.jtable.JTableSingleResponse;
import com.hcl.atf.taf.mongodb.service.MongoDBService;
import com.hcl.atf.taf.service.DataTreeService;
import com.hcl.atf.taf.service.EmailService;
import com.hcl.atf.taf.service.EventsService;
import com.hcl.atf.taf.service.HierarchicalEntitiesService;
import com.hcl.atf.taf.service.ProductListService;
import com.hcl.atf.taf.service.ResourceManagementService;
import com.hcl.atf.taf.service.SkillService;
import com.hcl.atf.taf.service.TestFactoryManagementService;
import com.hcl.atf.taf.service.UserListService;
import com.hcl.atf.taf.service.UserTypeMasterNewService;
import com.hcl.atf.taf.service.WorkPackageService;

@Controller
public class ResourceManagementController {
	
	private static final Log log = LogFactory.getLog(ResourceManagementController.class);
	
	@Autowired
	private ProductListService productListService;
	
	@Autowired
	private ResourceManagementService resourceManagementService;
	
	@Autowired
	private DataTreeService dataTreeService;

	@Autowired
	private TestFactoryManagementService testFactoryService;
	
	@Autowired
	private UserListService userListService;
	@Autowired
	private UserTypeMasterNewService userTypeMasterNewService;
	
	@Autowired
	private WorkPackageService workpackageService;
	
	@Autowired
	private SkillService skillService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private WorkPackageService  workPackageService;
	
	@Autowired
	private TestFactoryManagementService  testFactoryManagementService;
	
	@Autowired
	private WorkShiftMasterDAO  workShiftMasterDAO;
	
	
	@Autowired
	private MongoDBService  mongoDBService;
	
	@Autowired(required=true)
    private SessionFactory sessionFactory;
	
	@Autowired
	private ExcelTestDataIntegrator	excelTestDataIntegrator;
	
	@Autowired
	private UserDataIntegrator userDataIntegrator;
	
	@Autowired
	private EventsService eventService;
	
	@Autowired
	private HierarchicalEntitiesService hierarchicalEntitiesService;
	
	private String maxValue=null;
	
	public String getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(String maxValue) {
		this.maxValue = maxValue;
	}

	@RequestMapping(value="resource.availability.view.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listWorkpackageDemandProjection(HttpServletRequest req, @RequestParam int resourceId, @RequestParam int weekNo) {
		log.info("inside resource.availability.view.list");
		JTableResponse jTableResponse = null;
		log.info("Week No from Request : " + weekNo);

		try {
				if(resourceId<=0){
					resourceId=1;
				}
				if (weekNo < 0) {
					weekNo = DateUtility.getWeekOfYear();
				}
				req.getSession().setAttribute("ssnHdnResourceAvailabilityWeekNo", weekNo);
				req.getSession().setAttribute("ssnHdnSelectedResourceId", resourceId);
				
				log.info("Setting weekNo in session : " + weekNo);
				List<JsonResourceAvailability> jsonResourceAvailabilityList = resourceManagementService.listWorkPackageDemandProjection(resourceId, weekNo);
				log.info("JsonResourceAvailability size="+jsonResourceAvailabilityList.size());
				jTableResponse = new JTableResponse("OK", jsonResourceAvailabilityList, jsonResourceAvailabilityList.size());
				jsonResourceAvailabilityList = null;
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);	            
	        }
        return jTableResponse;
    }
	
	
	
	@RequestMapping(value="resource.availability.view.update",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse updateWorkPackageDemandProjection(@ModelAttribute JsonResourceAvailability jsonResourceAvailability, BindingResult result) {
		log.info("inside resource.availability.view.update");
		JTableResponse jTableResponse = null;
		if(result.hasErrors()){
			jTableResponse = new JTableResponse("ERROR","Invalid Form!"); 
		}
		try {
			
			JsonResourceAvailability updatedJsonResourceAvailability = resourceManagementService.updateWorkPackageDemandProjection(jsonResourceAvailability);

			List<JsonResourceAvailability> jsonResourceAvailabilityList = new ArrayList<JsonResourceAvailability>();
			jsonResourceAvailabilityList.add(updatedJsonResourceAvailability);
			
			log.info("returning data");
			jTableResponse = new JTableResponse("OK",jsonResourceAvailabilityList,1);
	    } catch (Exception e) {
	        jTableResponse = new JTableResponse("ERROR","Unable to update Resource availability !");
	        log.error("JSON ERROR", e);
	    }
	        
        return jTableResponse;
    }
	
	@RequestMapping(value="testfactory.resourcepool.summary.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listResourcePoolSummary(@RequestParam int userTypeId, @RequestParam int resourcePoolId, @RequestParam int testFactoryLabId) {			
		JTableResponse jTableResponse;			 
		try {
			 List<JsonResourcePoolSummary> jsonResourcePoolSummaryFromDB =new ArrayList<JsonResourcePoolSummary>();
			 if(resourcePoolId != 0){			
				 jsonResourcePoolSummaryFromDB =resourceManagementService.getResourcePoolwithRoleJson(resourcePoolId);			
			}else if(testFactoryLabId !=0){
				List<TestfactoryResourcePool> resourcePoolList= null;
				resourcePoolList= resourceManagementService. getTestfactoryResourcePoolListbyLabId(testFactoryLabId);
				List<Integer> resourcePoolIdList = new ArrayList<Integer>();
				if (!(resourcePoolList == null || resourcePoolList.isEmpty())) {
					for (TestfactoryResourcePool rp : resourcePoolList) {
						Hibernate.initialize(rp.getTestFactoryLab());
						resourcePoolIdList.add(rp.getResourcePoolId());
					}
				}			
				jsonResourcePoolSummaryFromDB =resourceManagementService.getResourcePoolwithRoleJson(resourcePoolIdList);		
			}
			
			jTableResponse = new JTableResponse("OK", jsonResourcePoolSummaryFromDB,jsonResourcePoolSummaryFromDB.size()); 
			jsonResourcePoolSummaryFromDB=null;
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Unable to show ResourcePoolSummary IN controller.!");
	            log.error("JSON ERROR", e);
	        }		        
        return jTableResponse;
    }
	
	@RequestMapping(value="testfactory.resource.pool.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listResourcePool(@RequestParam int testFactoryLabId) {			
		JTableResponse jTableResponse;			 
		try {				
			List<TestfactoryResourcePool> resourcePoolfromDB = resourceManagementService.getTestfactoryResourcePoolListbyLabId(testFactoryLabId);
			List<JsonResourcePool> jsonResourcePool = new ArrayList<JsonResourcePool>();			
			for(TestfactoryResourcePool tresourcepool: resourcePoolfromDB){
				jsonResourcePool.add(new JsonResourcePool(tresourcepool));
			}				
			jTableResponse = new JTableResponse("OK", jsonResourcePool,jsonResourcePool.size());     
			resourcePoolfromDB = null;
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }		        
        return jTableResponse;
    }	
	
	@RequestMapping(value="testfactory.resource.pool.create",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse addResourcePool(HttpServletRequest request, @ModelAttribute JsonResourcePool jsonResourcePool, BindingResult result) {
		JTableSingleResponse jTableSingleResponse;			
		if(result.hasErrors()){
			jTableSingleResponse  = new JTableSingleResponse("ERROR","Invalid form!"); 
		}
		
		try {
				log.info("Test Facroty Lab Id: "+jsonResourcePool.getTestFactoryLabId());
				TestfactoryResourcePool resourcePool = jsonResourcePool.getResourcePool();
				UserList userList = (UserList)request.getSession().getAttribute("USER");
				UserList userObj = userListService.getUserListById(userList.getUserId());
				
				TestFactoryLab testFactoryLab = testFactoryService.getTestFactoryLabById(jsonResourcePool.getTestFactoryLabId());
				resourcePool.setTestFactoryLab(testFactoryLab);					
			log.info("testFactoryLab----->"+testFactoryLab);							
				resourceManagementService.addResourcePool(resourcePool);				
				eventService.addNewEntityEvent(IDPAConstants.ENTITY_RESOURCE_POOL, resourcePool.getResourcePoolId(), resourcePool.getResourcePoolName(), userObj);
					jTableSingleResponse = new JTableSingleResponse("OK",new JsonResourcePool(resourcePool));		
	        } catch (Exception e) {
	            jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding record!");
	            log.error("JSON ERROR", e);
	        }		        
        return jTableSingleResponse;
    }
	@RequestMapping(value="testfactory.resource.pool.update",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse updateResourcePool(HttpServletRequest request, @ModelAttribute JsonResourcePool jsonResourcePool, BindingResult result) {
		JTableResponse jTableResponse;	
		String remarks = "";
		if(result.hasErrors()){
			jTableResponse = new JTableResponse("ERROR","Invalid Form!"); 
		}			
		try {	
				UserList userList = (UserList)request.getSession().getAttribute("USER");
				UserList userObj = userListService.getUserListById(userList.getUserId());
				
				TestfactoryResourcePool resourcePoolFromUI = jsonResourcePool.getResourcePool();
				TestfactoryResourcePool resourcePoolFromDB = resourceManagementService.getTestfactoryResourcePoolListbyId(jsonResourcePool.getResourcePoolId());
				
				resourcePoolFromUI.setTestFactoryList(resourcePoolFromDB.getTestFactoryList());
				if(jsonResourcePool.getTestFactoryLabId() != null && !jsonResourcePool.getTestFactoryLabId().equals("0")){
					resourcePoolFromUI.setTestFactoryLab(testFactoryService.getTestFactoryLabById(jsonResourcePool.getTestFactoryLabId()));
				}
											
				resourceManagementService.updateResourcePool(resourcePoolFromUI);
				remarks = "TestFactoryResourcePool :"+resourcePoolFromUI.getResourcePoolName();
				eventService.addEntityChangedEvent(IDPAConstants.ENTITY_RESOURCE_POOL, resourcePoolFromUI.getResourcePoolId(), resourcePoolFromUI.getResourcePoolName(),
						jsonResourcePool.getModifiedField(), jsonResourcePool.getModifiedFieldTitle(),
						jsonResourcePool.getOldFieldValue(), jsonResourcePool.getModifiedFieldValue(), userObj, remarks);
				List<JsonResourcePool> tmpList = new ArrayList();
				tmpList.add(jsonResourcePool);
				jTableResponse = new JTableResponse("OK",tmpList ,1);			
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error updating ResourcePool!");
	            log.error("JSON ERROR updating ResourcePool", e);
	        }
        return jTableResponse;
    }

	@RequestMapping(value="workPackage.resource.demand.list",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse listWorkPackageDemandProjectionForResourcePlanning(@RequestParam int productId, @RequestParam  Date resourceDemandForDate) {
		log.info("inside workPackage.resource.demand.list"); 
		JTableResponse jTableResponse = null;
		try {
				log.info(" productId: "+productId);
				List<JsonWorkPackageResourceReservation> jsonWorkPackageResourceReservationList = resourceManagementService.listWorkPackageDemandProjectionForResourcePlanning(productId, resourceDemandForDate);
				if (jsonWorkPackageResourceReservationList != null){
					Collections.sort(jsonWorkPackageResourceReservationList, JsonWorkPackageResourceReservation.jsonWorkPackageResourceComparator);
				}
				log.info("JsonWorkPackageResourceReservation size="+jsonWorkPackageResourceReservationList.size());
				jTableResponse = new JTableResponse("OK", jsonWorkPackageResourceReservationList, jsonWorkPackageResourceReservationList.size());
				jsonWorkPackageResourceReservationList = null;
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);	            
	        }
        return jTableResponse;
    }
	
	@RequestMapping(value="resource.availability.plan.list",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse listResourcesForAvailabilityPlan(HttpServletRequest req, @RequestParam int resourcePoolId, @RequestParam  Date workDate,@RequestParam int shiftId,@RequestParam Integer jtStartIndex, @RequestParam Integer jtPageSize) {
		log.info("inside resource.availability.plan.list");
		log.info("resourcePoolId="+  resourcePoolId + " resourceDemandForDate = " + workDate);
		JTableResponse jTableResponse = null;
		List<JsonResourceAvailabilityPlan> jsonResourceAvailabilityPlanList=null;
		Map<String, String> searchStrings = new HashMap<String, String>();
		try {
			int shiftTypeId = shiftId;
            String searchUserName = req.getParameter("searchUserName");
            String searchResourcePoolName=req.getParameter("searchResourcePoolName");
            String searchUserTypeName=req.getParameter("searchUserTypeName");
            String searchSkillName=req.getParameter("searchSkillName");
            String searchRoleName=req.getParameter("searchRoleName");
            String searchRegisteredCompanyName=req.getParameter("searchRegisteredCompanyName");
            String searchTimeSheetHours=req.getParameter("searchTimeSheetHours");
            String searchBookedHrs=req.getParameter("searchBookedHrs");
            String searchShiftTypeAvailability=req.getParameter("searchShiftTypeAvailability");
            String searchShiftTypeBooking=req.getParameter("searchShiftTypeBooking");
          
            
            searchStrings.put("searchUserName", searchUserName);
            searchStrings.put("searchResourcePoolName", searchResourcePoolName);
            searchStrings.put("searchUserTypeName", searchUserTypeName);
            searchStrings.put("searchSkillName", searchSkillName);
            searchStrings.put("searchRoleName", searchRoleName);
            searchStrings.put("searchRegisteredCompanyName", searchRegisteredCompanyName);
            searchStrings.put("searchTimeSheetHours", searchTimeSheetHours);
            searchStrings.put("searchBookedHrs", searchBookedHrs);
            searchStrings.put("searchShiftTypeAvailability", searchShiftTypeAvailability);
            searchStrings.put("searchShiftTypeBooking", searchShiftTypeBooking);
            
            
            
				jsonResourceAvailabilityPlanList = resourceManagementService.listResourcesForAvailabilityPlanByShifId(searchStrings, searchResourcePoolName,searchUserName,resourcePoolId, workDate, shiftTypeId, jtStartIndex, jtPageSize);
				Long jsonjsonResourceAvailabilityPlanforPaging = resourceManagementService.listResourcesForAVailabilityCount(resourcePoolId);
				
				req.getSession().setAttribute("ssnHdnResourceManagerResourceAvailabilityDate", workDate);
				log.info("jsonResourceAvailabilityPlanList size="+jsonResourceAvailabilityPlanList.size());
				log.info("Selected Date is "+workDate);
				jTableResponse = new JTableResponse("OK", jsonResourceAvailabilityPlanList,jsonjsonResourceAvailabilityPlanforPaging.intValue());
				jsonResourceAvailabilityPlanList = null;
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);	            
	        }
        return jTableResponse;
    }
	
	@RequestMapping(value="resource.booking.plan.list",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse listResourcesForBookingPlan(HttpServletRequest req, @RequestParam Integer resourcePoolId, @RequestParam  Date workDate,@RequestParam Integer shiftId) {
		log.info("inside resource.booking.plan.list");
		log.info("resourcePoolId="+  resourcePoolId + " resourceDemandForDate = " + workDate);
		JTableResponse jTableResponse = null;
		List<JsonResourceAvailabilityPlan> jsonResourceAvailabilityPlanList=null;
		List<JsonResourceAvailabilityPlan> jsonResourceAvailabilityPlanListforPagination=null;
		Map<String, String> searchStrings = new HashMap<String, String>();
		try {
			int shiftTypeId = shiftId;
            String searchUserName = req.getParameter("searchUserName");
            String searchResourcePoolName=req.getParameter("searchResourcePoolName");
            String searchUserTypeName=req.getParameter("searchUserTypeName");
            String searchSkillName=req.getParameter("searchSkillName");
            String searchRoleName=req.getParameter("searchRoleName");
            String searchRegisteredCompanyName=req.getParameter("searchRegisteredCompanyName");
            String searchTimeSheetHours=req.getParameter("searchTimeSheetHours");
            String searchBookedHrs=req.getParameter("searchBookedHrs");
            String searchShiftTypeBooking=req.getParameter("searchShiftTypeBooking");
          
            
            searchStrings.put("searchUserName", searchUserName);
            searchStrings.put("searchResourcePoolName", searchResourcePoolName);
            searchStrings.put("searchUserTypeName", searchUserTypeName);
            searchStrings.put("searchSkillName", searchSkillName);
            searchStrings.put("searchRoleName", searchRoleName);
            searchStrings.put("searchRegisteredCompanyName", searchRegisteredCompanyName);
            searchStrings.put("searchTimeSheetHours", searchTimeSheetHours);
            searchStrings.put("searchBookedHrs", searchBookedHrs);
            searchStrings.put("searchShiftTypeBooking", searchShiftTypeBooking);
            
            
            
				jsonResourceAvailabilityPlanList = resourceManagementService.listResourcesForBookingPlanByShifId(searchStrings, searchResourcePoolName,searchUserName,resourcePoolId, workDate, shiftTypeId);
				
				req.getSession().setAttribute("ssnHdnResourceManagerResourceAvailabilityDate", workDate);
				log.info("jsonResourceAvailabilityPlanList size="+jsonResourceAvailabilityPlanList.size());
				log.info("Selected Date is "+workDate);
				jTableResponse = new JTableResponse("OK", jsonResourceAvailabilityPlanList,jsonResourceAvailabilityPlanList.size());
				jsonResourceAvailabilityPlanList = null;
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);	            
	        }
        return jTableResponse;
    }
	
	
	public String[] convertStringToStringArray(String delimiter,String value){
		String[] result=null;
		
		if(value!=null && value.trim().length()>0){
			result=value.split(delimiter);
		}
		return result;
	}
	
	@RequestMapping(value="resource.availability.plan.update.inline",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse updateResourceAvailabilityInline(HttpServletRequest req, @RequestParam Integer userId, @RequestParam String modifiedField, @RequestParam String modifiedFieldValue) {
		log.info("inside resource.availability.plan.update.inline");
		log.info("Resource id ="+  userId + " modifiedColumn = " + modifiedField + " modifiedValue  " + modifiedFieldValue );
		JTableResponse jTableResponse = null;
		try {
			
			Date date = (Date) req.getSession().getAttribute("ssnHdnResourceManagerResourceAvailabilityDate");
			log.info(" Date from Session : "+date);
			log.info(" modified Field : "+modifiedField);
			log.info(" modifiedField Value: "+modifiedFieldValue);
			ResourceAvailability resourceAvailability =null;
			if (resourceAvailability == null) {
				jTableResponse = new JTableResponse("Error", "Resource cannot be booked due to non-availability");//, new JsonResourceAvailability(resourceAvailability), 1);
			} else {
				jTableResponse = new JTableResponse("OK", "Resource availability / booking updated successfully");//, new JsonResourceAvailability(resourceAvailability), 1);
			}
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error Updating Availability / Booking the Resource!");
	        log.error("JSON ERROR", e);	            
	    }
        return jTableResponse;
    }
	

	@RequestMapping(value="resource.list.calHeatMap",method=RequestMethod.GET ,produces="application/text")
	public @ResponseBody String reosurceManagementListForCalHeatMap(ModelMap model,@RequestParam Integer testFactoryLabId, @RequestParam Integer testFactoryId,@RequestParam Integer productId,@RequestParam Integer workPackageId,@RequestParam Integer shiftId,@RequestParam String dataType,HttpServletRequest req) {
		log.info("inside resource.list.calHeatMap");
		
		
		
		
		
		
		JCalHeatMapResponse response=null;
		HashMap<String, String> data= new HashMap<String ,String>();
		JSONObject jsonObj= new JSONObject();
		List<JCalHeatMapEntity> listJCalHeatMapEntity= new ArrayList<JCalHeatMapEntity>();
		JCalHeatMapEntity jCalHeatMapEntity = new JCalHeatMapEntity();
		Date startDate =null;
		Date endDate =null;
		
		
		try {
			Date workDate=null;
			Float output=0f;
				List<WorkPackageDemandProjectionDTO>  listWorkPackageDemandProjectionDTO =workpackageService.listWorkpackageDemandProdjectionByWorkpackageId(startDate, endDate,  testFactoryLabId,  testFactoryId,  productId,  workPackageId,  shiftId,null);
				
				if(dataType!=null && dataType.equalsIgnoreCase("All")){
				}else if(dataType.equalsIgnoreCase("Demand")){
					for(WorkPackageDemandProjectionDTO workPackageDemandProjection:listWorkPackageDemandProjectionDTO){
						workDate=workPackageDemandProjection.getWorkDate();
						if(workPackageDemandProjection.getResourceCount()==null){
							output=0f;
						}else{
							output=workPackageDemandProjection.getResourceCount();
						}
						if(workDate!=null)
							jsonObj.put(Long.toString(DateUtility.getTimestampFromDate(workDate)), output);
					}
				}else if(dataType.equalsIgnoreCase("Availablity")){
					for(WorkPackageDemandProjectionDTO workPackageDemandProjection:listWorkPackageDemandProjectionDTO){
						workDate=workPackageDemandProjection.getWorkDate();
						ResourceAvailabilityDTO resourceAvailabilities = resourceManagementService.listResourceAvailablityByDate(startDate, endDate,  testFactoryLabId,  testFactoryId,  productId,  workPackageId,  shiftId,workDate);
						if(resourceAvailabilities.getResourceAvailabilityCount()==null){
							output=0f;
						}else{
							output=resourceAvailabilities.getResourceAvailabilityCount();
						}
						jsonObj.put(Long.toString(DateUtility.getTimestampFromDate(workDate)), output);
					}
				}else if(dataType.equalsIgnoreCase("Blocked") || dataType.equalsIgnoreCase("Reserved")){
					for(WorkPackageDemandProjectionDTO workPackageDemandProjection:listWorkPackageDemandProjectionDTO){
						workDate=workPackageDemandProjection.getWorkDate();
						TestFactoryResourceReservationDTO testFactoryResourceReservationList=resourceManagementService.listTestFactoryResourceReservationByWorkpackageIdDate(startDate, endDate,  testFactoryLabId,  testFactoryId,  productId,  workPackageId,  shiftId,workDate);
						if(testFactoryResourceReservationList.getBlockedCount()==null){
							output=0f;
						}else{
							output=testFactoryResourceReservationList.getBlockedCount();
						}
						jsonObj.put(Long.toString(DateUtility.getTimestampFromDate(workDate)), output);
					}
				}else if(dataType.equalsIgnoreCase("Shortage")){
					for(WorkPackageDemandProjectionDTO workPackageDemandProjection:listWorkPackageDemandProjectionDTO){
						workDate=workPackageDemandProjection.getWorkDate();
						ResourceAvailabilityDTO resourceAvailabilities = resourceManagementService.listResourceAvailablityByDate(startDate, endDate,  testFactoryLabId,  testFactoryId,  productId,  workPackageId,  shiftId,workDate);
						if(resourceAvailabilities.getResourceAvailabilityCount()==null){
							output=workPackageDemandProjection.getResourceCount()-0;
						}else if(workPackageDemandProjection.getResourceCount()==null){
							output=workPackageDemandProjection.getResourceCount();
						}else{
							output=(workPackageDemandProjection.getResourceCount() -resourceAvailabilities.getResourceAvailabilityCount() );
						}
						jsonObj.put(Long.toString(DateUtility.getTimestampFromDate(workDate)), output);
					}
					
				}
				//Added for legend
				if(jsonObj.length()!=0){
					String [] jsonValue= jsonObj.toString().split(",");
					String[] jsonValueAfterSplit=null;
					ArrayList<Integer> listOfValues=new ArrayList<Integer>();
					for(int j=0;j<jsonValue.length;j++){
						jsonValueAfterSplit=jsonValue[j].split(":");
						if(jsonValueAfterSplit!=null){
							if(jsonValueAfterSplit[1]!=null && jsonValueAfterSplit[1].lastIndexOf("}")==-1)
								listOfValues.add(new Integer(jsonValueAfterSplit[1]));
							else
								listOfValues.add(new Integer(jsonValueAfterSplit[1].substring(0, jsonValueAfterSplit[1].length()-1)));
						}
					}
					
					 Collections.sort(listOfValues); // Sort the arraylist
					 listOfValues.get(listOfValues.size() - 1);
					req.getSession().setAttribute("ssnHdnLegendMaxValue", Collections.max(listOfValues));
					req.getSession().setAttribute("ssnHdnLegendMinValue", Collections.min(listOfValues));
					
				}else{
					req.getSession().setAttribute("ssnHdnLegendMaxValue",10 );
					req.getSession().setAttribute("ssnHdnLegendMinValue", 0);
				}
	        } catch (Exception e) {
	        	
	            log.error("JSON ERROR", e);	            
	        }
        return jsonObj.toString();
    }
	@RequestMapping(value="testFactory.ResourceReservation.list",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse listloadBlockedResourceCount(@RequestParam int workPackageId,@RequestParam int shiftId,@RequestParam String reservationDate){
		log.info("testFactory.ResourceReservation.list*********");
		JTableResponse jTableResponse = null;
		Date tfreservationDate=null;
		
		JsonTestFactoryResourceReservation jsontestFactResReservation=null;
		log.info("workPackageId: " + workPackageId);
		log.info("shiftId: " + shiftId);
		log.info("reservationDate: " + reservationDate);
		
		if(reservationDate != null ) {
			
			tfreservationDate=DateUtility.dateformatWithOutTime(reservationDate);
			
		}
		log.info("tfreservationDate"+tfreservationDate);
		
		try {
		List<TestFactoryResourceReservation>   testFactResReservationList=	resourceManagementService.getTestFactoryResourceReservation(workPackageId,shiftId,tfreservationDate);
	
		log.info("testFactResReservationList"+testFactResReservationList.size());
		for(TestFactoryResourceReservation testFactResReservation: testFactResReservationList){
			log.info("ShiftId:-->"+testFactResReservation.getShift().getShiftName()+"workpackage anme:-->"+testFactResReservation.getWorkPackage().getName());
		}
		List<JsonTestFactoryResourceReservation> jsontestFactResReservationList=new ArrayList<JsonTestFactoryResourceReservation>();
		
			for(TestFactoryResourceReservation testFactResReservation: testFactResReservationList){
				jsontestFactResReservation = new JsonTestFactoryResourceReservation(testFactResReservation);
				jsontestFactResReservationList.add(jsontestFactResReservation);
			}
		
		log.info("workPackage.status.summary.notcompleted.testcases.list : "+jsontestFactResReservationList.size());
		jTableResponse = new JTableResponse("OK", jsontestFactResReservationList, jsontestFactResReservationList.size());
		testFactResReservationList = null;
		
} catch (Exception e) {
    jTableResponse = new JTableResponse("ERROR","Error fetching records!");
    log.error("JSON ERROR", e);
} 
	
        return jTableResponse;
	}
	
	@RequestMapping(value="testFactory.ResourceReservation.details.list",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse listloadBlockedResourceDetails(@RequestParam int workPackageId,@RequestParam int shiftId,@RequestParam String reservationDate){
		log.info("************ testFactory.ResourceReservation.details.list *********");
		JTableResponse jTableResponse = null;		
		try {
			List<JsonWorkPackageDemandProjection> jsonWorkPackageDemandProjectionList=new ArrayList<JsonWorkPackageDemandProjection>();
			List<WorkPackageDemandProjectionStatisticsDTO>  listWorkPackageDemandProjectionStatisticsDTO =resourceManagementService.listWorkpackageDemandProjectionByRole(workPackageId, shiftId, DateUtility.dateFormatWithOutSeconds(reservationDate));
			
			if (listWorkPackageDemandProjectionStatisticsDTO == null || listWorkPackageDemandProjectionStatisticsDTO.isEmpty()) {
				
				jTableResponse = new JTableResponse("OK", jsonWorkPackageDemandProjectionList, 0);
			} else {
				JsonWorkPackageDemandProjection jwpdpl ;
				for(WorkPackageDemandProjectionStatisticsDTO wpDemandProjectiItem:listWorkPackageDemandProjectionStatisticsDTO){
					jwpdpl = new JsonWorkPackageDemandProjection();
					jwpdpl.setWpDemandProjectionId(wpDemandProjectiItem.getWpDemandProjectionId());					
					jwpdpl.setResourceCount(wpDemandProjectiItem.getResourceCount());
					jwpdpl.setShiftId(wpDemandProjectiItem.getShiftId());
					jwpdpl.setShiftName(wpDemandProjectiItem.getShiftName());
					jwpdpl.setWorkDate(wpDemandProjectiItem.getWorkDate().toString());
					jwpdpl.setWorkPackageId(wpDemandProjectiItem.getWorkPackageId());
					jwpdpl.setWorkPackageName(wpDemandProjectiItem.getWorkPackageName());
					jwpdpl.setSkillId(wpDemandProjectiItem.getSkillId());
					jwpdpl.setSkillName(wpDemandProjectiItem.getSkillName());
					jwpdpl.setUserRoleId(wpDemandProjectiItem.getUserRoleId());
					jwpdpl.setUserRoleName(wpDemandProjectiItem.getRoleName());
					jsonWorkPackageDemandProjectionList.add(jwpdpl);
					//jsonWorkPackageDemandProjectionList.add(new JsonWorkPackageDemandProjectionList(workPackageDemandProjection));
				}
				jTableResponse = new JTableResponse("OK", jsonWorkPackageDemandProjectionList,jsonWorkPackageDemandProjectionList.size() );
				listWorkPackageDemandProjectionStatisticsDTO = null;
			}
			log.info("---------- Obtained value ---------");
		} catch (Exception e) {
    jTableResponse = new JTableResponse("ERROR","Error fetching records!");
    log.error("JSON ERROR", e);
		} 
	
        return jTableResponse;
	}
	
	
	@RequestMapping(value="workPackage.resource.demand.listbywp",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse listWorkPackageDemandProjectionForResourcePlanningByWorkpackage(@RequestParam int workPackageId, @RequestParam  Date resourceDemandForDate) {
		log.info("inside workPackage.resource.demand.list");
		JTableResponse jTableResponse = null;
		try {
				log.info(" workPackageId: "+workPackageId);
				List<JsonWorkPackageResourceReservation> jsonWorkPackageResourceReservationList = resourceManagementService.listWorkPackageDemandProjectionForResourcePlanningByWorkpackage(workPackageId, resourceDemandForDate);
				if (jsonWorkPackageResourceReservationList != null){
					Collections.sort(jsonWorkPackageResourceReservationList, JsonWorkPackageResourceReservation.jsonWorkPackageResourceComparator);
				}
				log.info("JsonWorkPackageResourceReservation size="+jsonWorkPackageResourceReservationList.size());
				jTableResponse = new JTableResponse("OK", jsonWorkPackageResourceReservationList, jsonWorkPackageResourceReservationList.size());
				jsonWorkPackageResourceReservationList = null;
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);	            
	        }
        return jTableResponse;
    }
	
	@RequestMapping(value="resource.attendance.list",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse listResourceAttendance(HttpServletRequest req, @RequestParam int resourcePoolId, @RequestParam  Date workDate, @RequestParam Integer shiftId) {
		log.info("inside resource.attendance.list");
		log.info("resourcePoolId="+  resourcePoolId + " resourceDemandForDate = " + workDate);
		UserList user = (UserList)req.getSession().getAttribute("USER");
		Locale locale=(Locale)req.getSession().getAttribute("locale");
		String localeId=(String)req.getSession().getAttribute("localeId");
		Date workDateinDate=null;
		JTableResponse jTableResponse = null;
		
		try {			
				List<JsonResourceAttendance> jsonResourceAttendanceList = resourceManagementService.listResourcesAttendance(resourcePoolId, workDate, shiftId);
				req.getSession().setAttribute("ssnHdnResourceManagerResourceAttendanceDate", workDate);
				log.info("jsonResourceAttendanceList size="+jsonResourceAttendanceList.size());
				log.info("Selected Date is "+workDate);
				jTableResponse = new JTableResponse("OK", jsonResourceAttendanceList, jsonResourceAttendanceList.size());
				jsonResourceAttendanceList = null;
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);	            
	        }
        return jTableResponse;
    }

	@RequestMapping(value="resource.attendance.list.user",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse listResourceAttendancebyUser(HttpServletRequest req, @RequestParam  Date workdateFrom, @RequestParam  Date workdateTo, @RequestParam Integer shiftId) {
		log.info("inside resource.attendance.list.user");
		UserList user = (UserList)req.getSession().getAttribute("USER");
		JTableResponse jTableResponse = null;
		
		try {
			int userId = user.getUserId();
				UserList userforResourcePool = userListService.getUserListById(userId);
				List<JsonResourceAttendance> jsonResourceAttendanceList = resourceManagementService.listResourcesAttendance(userforResourcePool.getResourcePool().getResourcePoolId(), workdateFrom,workdateTo, shiftId, userId);
				req.getSession().setAttribute("ssnHdnResourceManagerResourceAttendanceDate",  workdateFrom);
				log.info("jsonResourceAttendanceList size="+jsonResourceAttendanceList.size());
				log.info("Selected Date is "+workdateFrom);
				jTableResponse = new JTableResponse("OK", jsonResourceAttendanceList, jsonResourceAttendanceList.size());
				jsonResourceAttendanceList = null;
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);	            
	        }
        return jTableResponse;
    }
	
	@RequestMapping(value="resource.attendance.update.inline",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse updateResourceAttendanceInline(HttpServletRequest req, @RequestParam Integer resourceAvailabilityId, @RequestParam String modifiedField, @RequestParam String modifiedFieldValue) {

		JTableResponse jTableResponse = null;
		try {
			
			log.info(" modified Field : "+modifiedField);
			log.info(" modifiedField Value: "+modifiedFieldValue);
			
			ResourceAvailability resourceAvailability = resourceManagementService.updateResourceAttendanceInline(resourceAvailabilityId, modifiedField,  modifiedFieldValue,req);
			if (resourceAvailability == null) {
				jTableResponse = new JTableResponse("Error", "Resource attendance cannot be marked. Please contact Admin");//, new JsonResourceAvailability(resourceAvailability), 1);
			} else{
			jTableResponse = new JTableResponse("OK");
			}
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error Updating attendance for Resource!");
	        log.error("JSON ERROR", e);	            
	    }
        return jTableResponse;
    }
	
	@RequestMapping(value="resource.pool.management.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listResourcePoolManagement(@RequestParam int testFactoryLabId, @RequestParam int resourcePoolId) {			
		JTableResponse jTableResponse;			 
		try {				
			List<TestfactoryResourcePool> resourcePoolfromDB = resourceManagementService.getResourcePoolListById(testFactoryLabId, resourcePoolId);
			List<JsonResourcePool> jsonResourcePool = new ArrayList<JsonResourcePool>();			
			for(TestfactoryResourcePool tresourcepool: resourcePoolfromDB){
				jsonResourcePool.add(new JsonResourcePool(tresourcepool));
			}				
			jTableResponse = new JTableResponse("OK", jsonResourcePool,jsonResourcePool.size());     
			resourcePoolfromDB = null;
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }		        
        return jTableResponse;
    }
	@RequestMapping(value="testFacory.management.mapTestFactoryWithRespool.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listTestFactoryByResPool(@RequestParam int resourcePoolId) {			
		JTableResponse jTableResponse;			 
		try {
			
			TestfactoryResourcePool tfResPoolList=resourceManagementService.getTestfactoryResourcePoolListbyId(resourcePoolId);
			Set<TestFactory> tfList=tfResPoolList.getTestFactoryList();
			log.info("TestFactorySet Size"+tfList.size());
			
			List<JsonTestFactory> jsontfList=new ArrayList<JsonTestFactory>();
			for(TestFactory tfactory: tfList){
				log.info("tfactoryId"+tfactory.getTestFactoryId());
				log.info("tfactoryId"+tfactory.getTestfactoryResourcePoolList());
				jsontfList.add(new JsonTestFactory(tfactory));	
				}			
			jTableResponse = new JTableResponse("OK", jsontfList,jsontfList.size());     
			tfList = null;
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }		        
        return jTableResponse;
    }
	
	
	
	@RequestMapping(value="testFacory.management.mapTestFactoryWithRespool.add",method=RequestMethod.POST ,produces="application/json")
	@Transactional
    public @ResponseBody JTableSingleResponse addTestfactoryToRespool(@ModelAttribute JsonTestFactory jsonTestFactory, BindingResult result) {
		JTableSingleResponse jTableSingleResponse;
		
		if(result.hasErrors()){
			jTableSingleResponse  = new JTableSingleResponse("ERROR","Invalid form!"); 
		}
		TestfactoryResourcePool testfactoryResourcePool=null;
		try {      
			log.info("Jsontestfactorydata---->"+jsonTestFactory.getTestFactory());
			log.info("Jsontestfactorydata---->"+jsonTestFactory.getTestFactory().getTestfactoryResourcePoolList().size());
		        log.info("TestfactoryId---->"+jsonTestFactory.getTestFactoryId());
		        log.info("TestfactoryResourcePoolId"+jsonTestFactory.getResourcePoolId());
		        log.info("TestfactoryLabId"+jsonTestFactory.getTestFactoryLabId());
		        testfactoryResourcePool = 	resourceManagementService.mapRespoolTestfactory(jsonTestFactory.getTestFactoryId(),jsonTestFactory.getResourcePoolId(),"Exist");
		        if(testfactoryResourcePool==null){
		        	jTableSingleResponse = new JTableSingleResponse("ERROR","Test Factory Already Exists!");
		        	return jTableSingleResponse;
		        }else{
		        	testfactoryResourcePool=null;
		 testfactoryResourcePool = 	resourceManagementService.mapRespoolTestfactory(jsonTestFactory.getTestFactoryId(),jsonTestFactory.getResourcePoolId(),TAFConstants.ADD);
		
		if(testfactoryResourcePool==null ){
			
			jTableSingleResponse = new JTableSingleResponse("INFORMATION","Test Factory Already Exists!");
		}
		        }
		
					jTableSingleResponse = new JTableSingleResponse("OK",new JsonResourcePool(testfactoryResourcePool));		
	        } catch (Exception e) {
	            jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding record!");
	            log.error("JSON ERROR", e);
	        }
	        
        return jTableSingleResponse;
    }
	
	
	@RequestMapping(value="testFactory.resourcepool.add",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse addTestfactoryToResourcepool(@ModelAttribute JsonResourcePool jsonResourcePool, BindingResult result,@RequestParam Integer testFactoryId) {
		
		JTableSingleResponse jTableSingleResponse;
		
		TestfactoryResourcePool testfactoryResourcePool=null;
		try {      
			
		        log.info("TestfactoryId---->"+testFactoryId);
		        testfactoryResourcePool = 	resourceManagementService.mapRespoolTestfactory(testFactoryId,jsonResourcePool.getResourcePoolId(),"Exist");
		        if(testfactoryResourcePool==null){
		        	jTableSingleResponse = new JTableSingleResponse("ERROR","ResourcePool already Mapped!");
		        	return jTableSingleResponse;
		        }else{
		        	testfactoryResourcePool=null;
		        	testfactoryResourcePool = 	resourceManagementService.mapRespoolTestfactory(testFactoryId,jsonResourcePool.getResourcePoolId(),TAFConstants.ADD);
		
		if(testfactoryResourcePool==null ){
			
			jTableSingleResponse = new JTableSingleResponse("INFORMATION","ResourcePool already Mapped!");
		}
		        }
		
					jTableSingleResponse = new JTableSingleResponse("OK",new JsonResourcePool(testfactoryResourcePool));		
	        } catch (Exception e) {
	            jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding record!");
	            log.error("JSON ERROR", e);
	        }
	        
        return jTableSingleResponse;
    }
	
	
	
	
	@RequestMapping(value="resourcepool.list.for.testfactory",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse listResourcepoolByTestfactory(HttpServletRequest request, @RequestParam Integer testFactoryId) {
		JTableResponse jTableResponse;
		
		List<TestfactoryResourcePool> testfactoryResourcePoolList=new ArrayList<TestfactoryResourcePool>();
		TestfactoryResourcePool testfactoryResourcePool = new TestfactoryResourcePool();
		List<Integer> resourcePoolIds=new ArrayList<Integer>();
		try {
		        resourcePoolIds = 	resourceManagementService.getTestfactoryResourcePoolListbyTestFactoryId(testFactoryId);
		        for(Integer id:resourcePoolIds){
		        	testfactoryResourcePool = resourceManagementService.getTestfactoryResourcePoolListbyId(id);
		        	testfactoryResourcePoolList.add(testfactoryResourcePool);
		        }
		        
		        List<JsonResourcePool> jsonresourcepoolList=new ArrayList<JsonResourcePool>();
		        for(TestfactoryResourcePool resourcepool:testfactoryResourcePoolList){
		        	jsonresourcepoolList.add(new JsonResourcePool(resourcepool));
		        }
		
		jTableResponse = new JTableResponse("OK",jsonresourcepoolList,jsonresourcepoolList.size());		
	        } catch (Exception e) {
	        	jTableResponse = new JTableResponse("ERROR","Error listing Resourcepool By Testfactory!");
	            log.error("JSON ERROR listing Resourcepool By Testfactory", e);
	        }
	        
        return jTableResponse;
    }
	
	
	
	
	
	
	@RequestMapping(value="testFacory.management.mapTestFactoryWithRespool.remove",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse removeTestFactoryFromResPool(@ModelAttribute JsonTestFactory jsonTestFactory,BindingResult result) {
	
		JTableResponse jTableResponse;
		if(result.hasErrors()){
			jTableResponse = new JTableResponse("ERROR","Invalid Form!"); 
		}			
	
		try {      
			
			log.info("Jsontestfactorydata---->");
		        log.info("TestfactoryId---->"+jsonTestFactory.getTestFactoryId());
		        
		        log.info("JsonTestfactoryResourcePoolId"+jsonTestFactory.getResourcePoolId());
		        log.info("TestfactoryLabId"+jsonTestFactory.getTestFactoryLabId());
	
		TestfactoryResourcePool testfactoryResourcePool = 	resourceManagementService.mapRespoolTestfactory(jsonTestFactory.getTestFactoryId(),jsonTestFactory.getResourcePoolId(),TAFConstants.REMOVE);
		log.info("testfactoryResourcePool size-->"+testfactoryResourcePool);
		jTableResponse = new JTableResponse("OK");  
        
		
        } catch (Exception e) {
            jTableResponse = new JTableResponse("ERROR","Error removing TestFactory From ResPool!");
            log.error("JSON ERROR removing TestFactory From ResPool", e);
        }
        return jTableResponse;
    }
	
	@RequestMapping(value="testFacory.resourcepool.edit",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse removeTestFactoryFromResPoolBytestFactoryId(@ModelAttribute JsonResourcePool jsonResourcePool, BindingResult result,@RequestParam Integer testFactoryId) {
	
		JTableResponse jTableResponse;
		if(result.hasErrors()){
			jTableResponse = new JTableResponse("ERROR","Invalid Form!"); 
		}			
	
		try {      
		log.info("testfactoryId "+testFactoryId);
		log.info("resourcepoolId  "+jsonResourcePool.getResourcePoolId());
		TestfactoryResourcePool testfactoryResourcePool = 	resourceManagementService.mapRespoolTestfactory(testFactoryId,jsonResourcePool.getResourcePoolId(),TAFConstants.REMOVE);
		List<UserList> listOfEngagementManagerByResourcePoolId = userListService.getListOfEngManagerByResourcePoolId(jsonResourcePool.getResourcePoolId());
		List<Integer>userIds = new ArrayList<Integer>();
		
		
		for(UserList user : listOfEngagementManagerByResourcePoolId){
			userIds.add(user.getUserId());
		}
		
		testFactoryManagementService.deleteTestFactoryMappedUsersByTestFactoryIdAndUserId(testFactoryId,userIds);
		
		
		log.info("testfactoryResourcePool size-->"+testfactoryResourcePool);
		jTableResponse = new JTableResponse("OK");  
        } catch (Exception e) {
            jTableResponse = new JTableResponse("ERROR","Error removing  TestFactory From ResPool!");
            log.error("JSON ERROR removing  TestFactory From ResPoolBytestFactoryId", e);
        }
        return jTableResponse;
    }
	
	
	@RequestMapping(value="administration.skill.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listSkills() {		
		//log.info("Inside Listing Skills");
		JTableResponse jTableResponse;			 
		try {		
			List<Skill> skillFromDB = skillService.listSkill(1);
			List<JsonSkill> jsonSkill = new ArrayList<JsonSkill>();
			
			for(Skill sk : skillFromDB){
				jsonSkill.add(new JsonSkill(sk));
			}
			jTableResponse = new JTableResponse("OK", jsonSkill, jsonSkill.size());
			skillFromDB=null;			
			
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }		        
        return jTableResponse;
    }	
	
	@RequestMapping(value="administration.skill.add",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse addSkill(@ModelAttribute JsonSkill jsonSkill, BindingResult result) {  
		log.debug("  ------------administration.skill.add-----------");
		JTableSingleResponse jTableSingleResponse;
		if(result.hasErrors()){
			jTableSingleResponse =jTableSingleResponse = new JTableSingleResponse("ERROR","Invalid form!"); 
		}
		try {
			Skill skillFromUI = jsonSkill.getSkill();
			
			if(jsonSkill.getParentSkillId()!= null && !jsonSkill.getParentSkillId().equals("0")){
				skillFromUI.setParentSkill(skillService.getBySkillId(jsonSkill.getParentSkillId()));
			}
		
			String errorMessage = ValidationUtility.validateForNewSkillAddition(skillFromUI.getSkillName().trim(), skillService);			
			if (errorMessage != null) {
				
				jTableSingleResponse = new JTableSingleResponse("ERROR",errorMessage);
				return jTableSingleResponse;
			}
			skillService.addSkill(skillFromUI);			
	        jTableSingleResponse = new JTableSingleResponse("OK",new JsonSkill(skillFromUI));
	    } catch (Exception e) {
	        jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding new Skill!");
	        log.error("JSON ERROR", e);
	    }	        
        return jTableSingleResponse;		
    }
	
	@RequestMapping(value="administration.skill.add.logic",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse addSkillLogic(@ModelAttribute JsonSkill jsonSkill, BindingResult result) {  
		log.info("  ------------administration.skill.add.logic-----------");
		JTableSingleResponse jTableSingleResponse;
		if(result.hasErrors()){
			jTableSingleResponse =jTableSingleResponse = new JTableSingleResponse("ERROR","Invalid form!"); 
		}
		try {	        
			Skill skillFromUI = jsonSkill.getSkill();		
			
			if(jsonSkill.getParentSkillId()!= null && jsonSkill.getParentSkillId() !=0){
				skillFromUI.setParentSkill(skillService.getBySkillId(jsonSkill.getParentSkillId()));
			}
		
			String errorMessage = ValidationUtility.validateForNewSkillAddition(skillFromUI.getSkillName().trim(), skillService);			
			if (errorMessage != null) {
				
				jTableSingleResponse = new JTableSingleResponse("ERROR",errorMessage);
				return jTableSingleResponse;
			}
			skillService.addSkill(skillFromUI);			
	        jTableSingleResponse = new JTableSingleResponse("OK",new JsonSkill(skillFromUI));
	    } catch (Exception e) {
	        jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding new Skill!");
	        log.error("JSON ERROR", e);
	    }	        
        return jTableSingleResponse;		
    }
	
	@RequestMapping(value="administration.skill.update",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse updateSkill(@ModelAttribute JsonSkill jsonSkill, BindingResult result) {
		JTableResponse jTableResponse = null;
		if(result.hasErrors()){
			jTableResponse = new JTableResponse("ERROR","Invalid Form!"); 
		}			
		try {			
			Skill skillFromUI = jsonSkill.getSkill();
			if(skillFromUI.getParentSkill()!=null && skillFromUI.getParentSkill().getSkillId()!=null && !skillFromUI.getParentSkill().getSkillId().equals("0")){
				skillFromUI.setParentSkill(skillService.getBySkillId(skillFromUI.getParentSkill().getSkillId()));				
			}
			else if(skillFromUI.getParentSkill()!=null && skillFromUI.getParentSkill().getSkillName()!=null && !skillFromUI.getParentSkill().getSkillName().equals("")){
				skillFromUI.setParentSkill(skillService.getSkillByName(skillFromUI.getParentSkill().getSkillName()));
			}			
						
			if (jsonSkill.getModifiedField().equalsIgnoreCase("parentSkillId")) {
				if (jsonSkill.getOldFieldID() == null || jsonSkill.getOldFieldID().equals("-1")) {
					Skill rootSkill = skillService.getRootSkill();
					jsonSkill.setOldFieldID(rootSkill.getSkillId()+"");
					jsonSkill.setOldFieldValue(rootSkill.getSkillName());
				}
				if (jsonSkill.getModifiedFieldID() == null || jsonSkill.getModifiedFieldID().equals("-1")) {
					Skill rootSkill = skillService.getRootSkill();
					jsonSkill.setModifiedFieldID(rootSkill.getSkillId()+"");
					jsonSkill.setModifiedFieldValue(rootSkill.getSkillName());
				}
				updateSkill(skillFromUI, new Integer(jsonSkill.getOldFieldID()), new Integer(jsonSkill.getModifiedFieldID()));
			}else{
				skillService.update(skillFromUI);
			}
			List<JsonSkill> jsonSkillList = new ArrayList<JsonSkill>();
			jsonSkillList.add(new JsonSkill(skillFromUI));
			
			jTableResponse = new JTableResponse("OK",jsonSkillList,1);
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error updating Skills!");
	            log.error("JSON ERROR updating Skills", e);
	        }		
		return jTableResponse;      
    }
	
	@RequestMapping(value="administration.userskills.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listUserSkills(HttpServletRequest request, @RequestParam String toApprove, @RequestParam int isApproved, @RequestParam int selfIsPrimary, @RequestParam int managerIsPrimary) {		
		log.debug("Inside Listing User Skills");
		JTableResponse jTableResponse;			 
		try {			
			UserList sessionUser = (UserList)request.getSession().getAttribute("USER");
			Integer userRoleID = sessionUser.getUserRoleMaster().getUserRoleId();
			Integer userId = sessionUser.getUserId();
			List<UserSkills> userSkillsFromDB = new ArrayList<UserSkills>();
			if(toApprove.equalsIgnoreCase("yes")){				
				userSkillsFromDB = skillService.listUserSkillsToBeApproved(2, userId, isApproved, selfIsPrimary, managerIsPrimary );
			}else{
				userSkillsFromDB = skillService.listUserSkillsByUserId(2, userId, isApproved, selfIsPrimary, managerIsPrimary);
			}			
			List<JsonUserSkills> jsonUserSkills = new ArrayList<JsonUserSkills>();
			
			for(UserSkills usk : userSkillsFromDB){
				jsonUserSkills.add(new JsonUserSkills(usk));
			}
			
			jTableResponse = new JTableResponse("OK", jsonUserSkills, jsonUserSkills.size());
			userSkillsFromDB = null;
			
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching UserSkills!");
	            log.error("JSON ERROR fetching UserSkills", e);
	        }		        
        return jTableResponse;
    }
	
	@RequestMapping(value="administration.userskills.list.byuserid",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listUserSkillsByUserId(HttpServletRequest request, @RequestParam int selecteduserId,  @RequestParam String toApprove, @RequestParam int isApproved, @RequestParam int selfIsPrimary, @RequestParam int managerIsPrimary) {		
		log.debug("Inside Listing User Skills");
		JTableResponse jTableResponse;			 
		try {			
			UserList sessionUser = (UserList)request.getSession().getAttribute("USER");
			Integer userRoleID = sessionUser.getUserRoleMaster().getUserRoleId();
			Integer userId = sessionUser.getUserId();
			List<UserSkills> userSkillsFromDB = new ArrayList<UserSkills>();
			if(toApprove.equalsIgnoreCase("yes")){				
				userSkillsFromDB = skillService.listUserSkillsToBeApproved(2, selecteduserId, isApproved, selfIsPrimary, managerIsPrimary );
			}else{
				userSkillsFromDB = skillService.listUserSkillsByUserId(2, selecteduserId, isApproved, selfIsPrimary, managerIsPrimary);
			}			
			List<JsonUserSkills> jsonUserSkills = new ArrayList<JsonUserSkills>();
			
			for(UserSkills usk : userSkillsFromDB){
				jsonUserSkills.add(new JsonUserSkills(usk));
			}
			
			jTableResponse = new JTableResponse("OK", jsonUserSkills, jsonUserSkills.size());
			userSkillsFromDB = null;
			
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching UserSkills");
	            log.error("JSON ERROR fetching UserSkills By UserId", e);
	        }		        
        return jTableResponse;
    }
	
	@RequestMapping(value="administration.userskills.add",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse addUserSkills(HttpServletRequest request, @ModelAttribute JsonUserSkills jsonUserSkills, BindingResult result, @RequestParam String toApprove ) {		
		log.info("Inside Adding User Skills");
		JTableSingleResponse jTableSingleResponse;
		if(result.hasErrors()){
			jTableSingleResponse =jTableSingleResponse = new JTableSingleResponse("ERROR","Invalid form!"); 
		}					 
		try {	
			UserList sessionUser = (UserList)request.getSession().getAttribute("USER");
			Integer userRoleID = sessionUser.getUserRoleMaster().getUserRoleId();
			Integer userId = sessionUser.getUserId();
			UserList userObj = null;
			UserSkills userSkillsFromUI =  jsonUserSkills.getUserSkills();
			
			String UserSkills_SkillName = null;
			int CurrentUser_userId = 0;
			if(userId != null && userId != 0){
				userObj = userListService.getUserListById(userId);
				userSkillsFromUI.setUser(userObj);//User
				CurrentUser_userId=userId;
			}			
			if(jsonUserSkills.getApprovingManagerId()!= null && !jsonUserSkills.getApprovingManagerId().equals("0")){
				userSkillsFromUI.setApprovingManager(userListService.getUserListById(jsonUserSkills.getApprovingManagerId()));//Approving Manager
			}
			
			if(jsonUserSkills.getSkillId()!= null && !jsonUserSkills.getSkillId().equals("0")){
				userSkillsFromUI.setSkill(skillService.getBySkillId(jsonUserSkills.getSkillId()));//Self Skill
				UserSkills_SkillName = userSkillsFromUI.getSkill().getSkillName();
			}
			
			if(jsonUserSkills.getSelfSkillLevelId()!= null && !jsonUserSkills.getSelfSkillLevelId().equals("0")){
				userSkillsFromUI.setSelfSkillLevel(skillService.getByskillLevelId(jsonUserSkills.getSelfSkillLevelId()));//Self SkillLevel				
			}
			
			if(jsonUserSkills.getManagerSkillLevelId()!= null && !jsonUserSkills.getManagerSkillLevelId().equals("0")){
				userSkillsFromUI.setManagerSkillLevel(skillService.getByskillLevelId(jsonUserSkills.getManagerSkillLevelId()));//Manager SkillLevel
			}
			
			String errorMessage = null;
			if(UserSkills_SkillName != null){
				errorMessage = ValidationUtility.validateForNewUserSkillAddition(UserSkills_SkillName, skillService, CurrentUser_userId);
			}
			if (errorMessage != null) {
				
				jTableSingleResponse = new JTableSingleResponse("ERROR",errorMessage);
				return jTableSingleResponse;
			}
			
			skillService.addUserSkill(userSkillsFromUI);
			if(userObj != null){
			eventService.addNewEntityEvent(IDPAConstants.ENTITY_USER_SKILLS, userSkillsFromUI.getUserSkillId(), userSkillsFromUI.getSkill().getSkillName(), userObj);
			}
			jTableSingleResponse = new JTableSingleResponse("OK",new JsonUserSkills(userSkillsFromUI));	
			
	        } catch (Exception e) {
	        	jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding new Skill to User");	  	        
	            log.error("JSON ERROR adding new Skill to User", e);
	        }		        
        return jTableSingleResponse;
    }
	
	@RequestMapping(value="administration.userskills.update",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse updateUserSkills(HttpServletRequest request, @ModelAttribute JsonUserSkills jsonUserSkills, BindingResult result) {
		log.info("administration.userskills.update");
		JTableResponse jTableResponse = null;
		String remarks = "";
		if(result.hasErrors()){
			jTableResponse = new JTableResponse("ERROR","Invalid Form!"); 
		}			
		try {
			UserSkills userSkillsFromUI = jsonUserSkills.getUserSkills();			
			
			UserList sessionUser = (UserList)request.getSession().getAttribute("USER");
			Integer userRoleID = sessionUser.getUserRoleMaster().getUserRoleId();
			Integer userId = sessionUser.getUserId();
			UserList userObj = null;
			
			String UserSkills_SkillName = null;
			int CurrentUser_userId = 0;
			if(userId != null && userId != 0){
				userObj = userListService.getUserListById(userId);
				userSkillsFromUI.setUser(userObj);//User
				CurrentUser_userId=userId;
			}			
			if(jsonUserSkills.getApprovingManagerId()!= null && !jsonUserSkills.getApprovingManagerId().equals("0")){
				userSkillsFromUI.setApprovingManager(userListService.getUserListById(jsonUserSkills.getApprovingManagerId()));//Approving Manager
			}
			
			if(jsonUserSkills.getSkillId()!= null && !jsonUserSkills.getSkillId().equals("0")){
				userSkillsFromUI.setSkill(skillService.getBySkillId(jsonUserSkills.getSkillId()));//Self Skill
				UserSkills_SkillName = userSkillsFromUI.getSkill().getSkillName();
			}
			
			if(jsonUserSkills.getSelfSkillLevelId()!= null && !jsonUserSkills.getSelfSkillLevelId().equals("0")){
				userSkillsFromUI.setSelfSkillLevel(skillService.getByskillLevelId(jsonUserSkills.getSelfSkillLevelId()));//Self SkillLevel				
			}
			
			if(jsonUserSkills.getManagerSkillLevelId()!= null && !jsonUserSkills.getManagerSkillLevelId().equals("0")){
				userSkillsFromUI.setManagerSkillLevel(skillService.getByskillLevelId(jsonUserSkills.getManagerSkillLevelId()));//Manager SkillLevel
			}
			
			
			skillService.updateUserSkill(userSkillsFromUI);
			if(userObj != null){
			remarks = "UserSkill :"+userSkillsFromUI.getSkill().getSkillName();
			eventService.addEntityChangedEvent(IDPAConstants.ENTITY_USER_SKILLS, userSkillsFromUI.getUserSkillId(), userSkillsFromUI.getSkill().getSkillName(),
					jsonUserSkills.getModifiedField(), jsonUserSkills.getModifiedFieldTitle(),
					jsonUserSkills.getOldFieldValue(), jsonUserSkills.getModifiedFieldValue(), userObj, remarks);
			}
			List<JsonUserSkills> userSkillsList = new ArrayList<JsonUserSkills>();
			userSkillsList.add(new JsonUserSkills(userSkillsFromUI));
			
			jTableResponse = new JTableResponse("OK",userSkillsList,1);
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error updating Product Decoupling Category!");
	            log.error("JSON ERROR updating Decoupling Category", e);
	        }		
		return jTableResponse;      
    
    }
	
	@RequestMapping(value="administration.userskills.update.byuserid",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse updateUserSkillsByUserId(HttpServletRequest request, @ModelAttribute JsonUserSkills jsonUserSkills, BindingResult result) {
		log.info("administration.userskills.update");
		JTableResponse jTableResponse = null;
		if(result.hasErrors()){
			jTableResponse = new JTableResponse("ERROR","Invalid Form!"); 
		}			
		try {
			UserSkills userSkillsFromUI = jsonUserSkills.getUserSkills();			
			
			UserList sessionUser = (UserList)request.getSession().getAttribute("USER");
			Integer userRoleID = sessionUser.getUserRoleMaster().getUserRoleId();
			Integer userId = sessionUser.getUserId();
			
			String UserSkills_SkillName = null;
			int CurrentUser_userId = 0;
			if(userId != null && userId != 0){
				userSkillsFromUI.setUser(userListService.getUserListById(userId));//User
				CurrentUser_userId=userId;
			}			
			if(jsonUserSkills.getApprovingManagerId()!= null && !jsonUserSkills.getApprovingManagerId().equals("0")){
				userSkillsFromUI.setApprovingManager(userListService.getUserListById(jsonUserSkills.getApprovingManagerId()));//Approving Manager
			}
			
			if(jsonUserSkills.getSkillId()!= null && !jsonUserSkills.getSkillId().equals("0")){
				userSkillsFromUI.setSkill(skillService.getBySkillId(jsonUserSkills.getSkillId()));//Self Skill
				UserSkills_SkillName = userSkillsFromUI.getSkill().getSkillName();
			}
			
			if(jsonUserSkills.getSelfSkillLevelId()!= null && !jsonUserSkills.getSelfSkillLevelId().equals("0")){
				userSkillsFromUI.setSelfSkillLevel(skillService.getByskillLevelId(jsonUserSkills.getSelfSkillLevelId()));//Self SkillLevel				
			}
			
			if(jsonUserSkills.getManagerSkillLevelId()!= null && !jsonUserSkills.getManagerSkillLevelId().equals("0")){
				userSkillsFromUI.setManagerSkillLevel(skillService.getByskillLevelId(jsonUserSkills.getManagerSkillLevelId()));//Manager SkillLevel
			}
			
			
			skillService.updateUserSkill(userSkillsFromUI);
			List<JsonUserSkills> userSkillsList = new ArrayList<JsonUserSkills>();
			userSkillsList.add(new JsonUserSkills(userSkillsFromUI));
			
			jTableResponse = new JTableResponse("OK",userSkillsList,1);
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error updating UserSkills!");
	            log.error("JSON ERROR updating UserSkills By UserId", e);
	        }		
		return jTableResponse;      
    
    }
	
	@RequestMapping(value="administration.userskills.update.inline",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse updateUserSkillsInline(HttpServletRequest request, @RequestParam Integer userSkillId, @RequestParam String modifiedField, @RequestParam String modifiedFieldValue) {
		log.info("administration.userskills.update.inline---	"+userSkillId);
		JTableResponse jTableResponse = null;					
		try {
			UserList sessionUser = (UserList)request.getSession().getAttribute("USER");
			Integer userRoleID = sessionUser.getUserRoleMaster().getUserRoleId();
			Integer userId = sessionUser.getUserId();
			
			UserSkills userSkillsFromUI = skillService.updateUserSkillInline(userId, userSkillId, modifiedField, modifiedFieldValue);		
						
			List<JsonUserSkills> userSkillsList = new ArrayList<JsonUserSkills>();
			userSkillsList.add(new JsonUserSkills(userSkillsFromUI));
			
			jTableResponse = new JTableResponse("OK",userSkillsList,1);
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error updating UserSkills!");
	            log.error("JSON ERROR updating UserSkillsInline", e);
	        }		
		return jTableResponse;      
    
    }
	
	@RequestMapping(value="administration.userskills.approve.update.inline",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse updateUserSkillsApprovalInline(HttpServletRequest request, @RequestParam Integer userSkillId, @RequestParam String modifiedField, @RequestParam String modifiedFieldValue) {
		log.info("administration.userskills.approve.update.inline---	"+userSkillId);
		JTableResponse jTableResponse = null;					
		try {
			UserList sessionUser = (UserList)request.getSession().getAttribute("USER");
			Integer userRoleID = sessionUser.getUserRoleMaster().getUserRoleId();
			Integer approverId = sessionUser.getUserId();
			
			UserSkills userSkillsFromUI = skillService.updateUserSkillApprovalInline(approverId, userSkillId, modifiedField, modifiedFieldValue);		
						
			List<JsonUserSkills> userSkillsList = new ArrayList<JsonUserSkills>();
			userSkillsList.add(new JsonUserSkills(userSkillsFromUI));
			
			jTableResponse = new JTableResponse("OK",userSkillsList,1);
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error updating UserSkills with Approval!");
	            log.error("JSON ERROR updating UserSkills with Approval", e);
	        }		
		return jTableResponse;      
    
    }
	
	@RequestMapping(value="resource.reserved.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody HashMap<String, List<JsonTestFactoryResourceReservation>> listReservedResources(HttpServletRequest request, @RequestParam int productId,@RequestParam int workPackageId, @RequestParam int shiftId,@RequestParam String reservationDate) {
		log.info("inside  testFactResReservationList ********  productId: "+productId);
		log.info("inside  testFactResReservationList ********  workPackageId: "+workPackageId);
		log.info("inside  testFactResReservationList ********  shiftId: "+shiftId);

		Date dtReservationDate = null;
		UserList user = null;
		int userRoleId = 0;
		HashMap<String, List<JsonTestFactoryResourceReservation>> mapOfTestFactoryResourceReservation = null;
			try {
				HashMap<Integer,List<TestFactoryResourceReservation>> mapTFReservation	= new HashMap<Integer,List<TestFactoryResourceReservation>>();			
				
				if(reservationDate != null ) {
					dtReservationDate=DateUtility.dateformatWithOutTime(reservationDate);
				}
				
				if(productId == 0 &&  workPackageId == 0){
					List<ProductMaster> listOfProducts = null;
					if(request != null){
						user = (UserList)request.getSession().getAttribute("USER");
						if(user != null){
							userRoleId = user.getUserRoleMaster().getUserRoleId();
						}
					}
					List<WorkPackage>  listOfWorkPackages = new ArrayList<WorkPackage>();
					if(userRoleId == IDPAConstants.ROLE_ID_TEST_MANAGER){
						listOfProducts = productListService.getProductsByProductUserRoleForUserId(userRoleId, user.getUserId(),1);
					}else if(userRoleId == IDPAConstants.ROLE_ID_ADMIN){
						// List only Test Factory Products
						listOfProducts = productListService.listProductByEngagementType(1);
					}
					
					
					if(listOfProducts != null && listOfProducts.size()>0){
						for (ProductMaster productMaster : listOfProducts) {
							List<WorkPackage>  subWorkPackageList = null;
							subWorkPackageList = workpackageService.getWorkPackagesByProductId(productMaster.getProductId());
							if(listOfWorkPackages.size() == 0){
								listOfWorkPackages = subWorkPackageList;
							}else{
								listOfWorkPackages.addAll(subWorkPackageList);
							}
						}
					}
					if(listOfWorkPackages != null && listOfWorkPackages.size()>0){
						for (WorkPackage workPackage : listOfWorkPackages) {
							List<TestFactoryResourceReservation> testFactResReservationList = null;
							log.info("workPackage: "+workPackage.getName()+ " Id>>>>>  "+workPackage.getWorkPackageId());
							testFactResReservationList = resourceManagementService.getTestFactoryResourceReservation(workPackage.getWorkPackageId(),shiftId,dtReservationDate);
							log.info("testFactResReservationList size: "+ testFactResReservationList.size());
							if(testFactResReservationList != null && testFactResReservationList.size()>0){
								mapTFReservation.put(workPackage.getWorkPackageId(),testFactResReservationList);
							}else{
								mapTFReservation.put(workPackage.getWorkPackageId(),testFactResReservationList);
							}
						}
					}
				}else{
					log.info("PRODUCT OR WORK PACKAGE NOT 0");
				}
				
				WorkShiftMaster workShift = resourceManagementService.listWorkShiftsByshiftId(shiftId);
				List<UserWeekUtilisedTimeDTO> listOfTimeSheetEntries = resourceManagementService.getTimesheetEntriesForWorkPackage(workPackageId, workShift, dtReservationDate);
				List<UserWeekUtilisedTimeDTO> listOfReservedShifts = resourceManagementService.getReservedShiftsOfUser(workPackageId, shiftId, dtReservationDate);
				
				log.info("mapTFReservation: "+mapTFReservation);
				log.info("mapTFReservation size: "+mapTFReservation.size());
				mapOfTestFactoryResourceReservation = new HashMap<String, List<JsonTestFactoryResourceReservation>>();
				if(mapTFReservation != null && mapTFReservation.size()>0){
					Set<Integer> keySet = mapTFReservation.keySet();
						if(keySet != null && keySet.size()>0){
							for (Integer workPackageIdKey : keySet) {
								Float demand=0f;
								if(workPackageIdKey != 0){
									List<TestFactoryResourceReservation> reservedResourceList = mapTFReservation.get(workPackageIdKey);
									if(reservedResourceList == null || (reservedResourceList != null && reservedResourceList.size() == 0)){
										String productName = "";
										String wpName = ""; 
										String shiftName = "";
										demand = 0f;
										WorkPackage wp = workpackageService.getWorkPackageByIdWithMinimalnitialization(workPackageIdKey.intValue());
										shiftName = testFactoryService.getWorkShiftsByshiftId(shiftId).getShiftName();
										if(wp != null){
											wpName = wp.getName();
											productName = wp.getProductBuild().getProductVersion().getProductMaster().getProductName();
											demand = (float) workpackageService.totalWorkPackageDemandProjectionCountByWpId(wp.getWorkPackageId(), shiftId, dtReservationDate);
											String key = productName+" >> "+wpName+ " / " +shiftName+" >> "+dtReservationDate+" * "+demand;
											List<JsonTestFactoryResourceReservation> rList = new ArrayList<JsonTestFactoryResourceReservation>();
											mapOfTestFactoryResourceReservation.put(key,rList);
										}
									}else{
										List<JsonTestFactoryResourceReservation> jsontestFactResReservationList  = null;
										for(TestFactoryResourceReservation testFactResReservation: reservedResourceList){
											JsonTestFactoryResourceReservation jsontestFactResReservation = null;
												if(testFactResReservation!=null){
													log.info(">>>>>>>>>>>>>>>>>>"+testFactResReservation.getResourceReservationId());
													log.info(">>>>>>>>>>>>>>>>>>"+testFactResReservation.getWorkPackage().getWorkPackageId());
													log.info(">>>>>>>>>>>>>>>>>>>>"+reservationDate);
													if(testFactResReservation.getWorkPackage()!=null){
														List<JsonWorkPackageResourceReservation> jsonWorkPackageResourceReservations=resourceManagementService.listWorkPackageDemandProjectionForResourcePlanningByWorkpackage(testFactResReservation.getWorkPackage().getWorkPackageId(), DateUtility.dateformatWithOutTime(reservationDate));
														for(JsonWorkPackageResourceReservation jwprr :jsonWorkPackageResourceReservations){
															if(jwprr.getShiftId().equals(shiftId)){
																demand=jwprr.getResourceDemandCount();
															}
														}
													
													String key = testFactResReservation.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductName()+" >> "+testFactResReservation.getWorkPackage().getName()+ " / " +testFactResReservation.getShift().getShiftName()+" >> "+testFactResReservation.getReservationDate()+" * "+demand;
								
													log.info("key ::: "+key);
													if(mapOfTestFactoryResourceReservation.containsKey(key)){
														log.info(" key is available.....");
														jsontestFactResReservationList = mapOfTestFactoryResourceReservation.get(key);
														
														jsontestFactResReservation = new JsonTestFactoryResourceReservation(testFactResReservation);
														for (UserWeekUtilisedTimeDTO userWeekUtilisedTimeDTO : listOfTimeSheetEntries) {
															if(userWeekUtilisedTimeDTO.getUserId() == jsontestFactResReservation.getBlockedUserId()){
																Integer totalTimeInMinutes = DateUtility.convertTimeInMinutes(userWeekUtilisedTimeDTO.getTimeSheetTotalHours(),userWeekUtilisedTimeDTO.getTimeSheetTotalMins());
																Integer remainingTimeSheetInMinutes = 2400-totalTimeInMinutes;
																userWeekUtilisedTimeDTO.setTimeSheetDuration(DateUtility.convertTimeInHoursMinutes(0,remainingTimeSheetInMinutes));
																jsontestFactResReservation.setTimeSheetHours(userWeekUtilisedTimeDTO.getTimeSheetDuration());
																break;
															}else{
																continue;
															}
														}
														
														for (UserWeekUtilisedTimeDTO userWeekUtilisedTimeDTO : listOfReservedShifts) {
															if(userWeekUtilisedTimeDTO.getUserId() == jsontestFactResReservation.getBlockedUserId()){
																long totalTime = 40-(userWeekUtilisedTimeDTO.getShiftBookingCount()*8);
																userWeekUtilisedTimeDTO.setShiftBookingDuration(String.valueOf(totalTime));
																jsontestFactResReservation.setBookedHrs(totalTime);
																break;
															}else{
																continue;
															}
														}
														jsontestFactResReservationList.add(jsontestFactResReservation);
														mapOfTestFactoryResourceReservation.put(key, jsontestFactResReservationList);
													}else{
														log.info(" key is not available.....");
														jsontestFactResReservationList = new ArrayList<JsonTestFactoryResourceReservation>();
														jsontestFactResReservation = new JsonTestFactoryResourceReservation(testFactResReservation);
														for (UserWeekUtilisedTimeDTO userWeekUtilisedTimeDTO : listOfTimeSheetEntries) {
															if(userWeekUtilisedTimeDTO.getUserId() == jsontestFactResReservation.getBlockedUserId()){
																Integer totalTimeInMinutes = DateUtility.convertTimeInMinutes(userWeekUtilisedTimeDTO.getTimeSheetTotalHours(),userWeekUtilisedTimeDTO.getTimeSheetTotalMins());
																Integer remainingTimeSheetInMinutes = 2400-totalTimeInMinutes;
																userWeekUtilisedTimeDTO.setTimeSheetDuration(DateUtility.convertTimeInHoursMinutes(0,remainingTimeSheetInMinutes));
																jsontestFactResReservation.setTimeSheetHours(userWeekUtilisedTimeDTO.getTimeSheetDuration());
																break;
															}else{
																continue;
															}
														}
														
														for (UserWeekUtilisedTimeDTO userWeekUtilisedTimeDTO : listOfReservedShifts) {
															if(userWeekUtilisedTimeDTO.getUserId() == jsontestFactResReservation.getBlockedUserId()){
																long totalTime = 40-(userWeekUtilisedTimeDTO.getShiftBookingCount()*8);
																userWeekUtilisedTimeDTO.setShiftBookingDuration(String.valueOf(totalTime));
																jsontestFactResReservation.setBookedHrs(totalTime);
																break;
															}else{
																continue;
															}
														}
														jsontestFactResReservationList.add(jsontestFactResReservation);
														mapOfTestFactoryResourceReservation.put(key, jsontestFactResReservationList);
													}
												}
											}
										}
									}
								}
							}
						}
					}
				
				log.info("mapOfTestFactoryResourceReservation data : "+mapOfTestFactoryResourceReservation);
				log.info("mapOfTestFactoryResourceReservation .size() : "+mapOfTestFactoryResourceReservation.size());
			} catch (Exception e) {
			    log.error("JSON ERROR", e);
			}
			return mapOfTestFactoryResourceReservation;
    	}
	
	@RequestMapping(value="resource.reserved.list.weekly",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody HashMap<String, List<JsonTestFactoryResourceReservation>> listReservedResourcesByWeekForWorkPackage(HttpServletRequest request,@RequestParam int workPackageId,@RequestParam int productId ,@RequestParam int weekNo) {
		log.info("inside  resource.reserved.list.weekly ********  workPackageId: "+workPackageId+ " Week No:"+weekNo+"productId"+productId);
		HashMap<String, List<JsonTestFactoryResourceReservation>> mapOfTestFactoryResourceReservation = null;
			try {
				List<TestFactoryResourceReservation>   testFactResReservationList = new ArrayList<TestFactoryResourceReservation>();
				ProductMaster productMaster=null;
				if(workPackageId == -1 ){
					productMaster=productListService.getProductById(productId);
					List <WorkPackage> listOfWorkPackages = workpackageService.getWorkPackagesByProductId(productId);
					for (WorkPackage workPackage : listOfWorkPackages) {
						List<TestFactoryResourceReservation>   subTestFactResReservationList = null;
						subTestFactResReservationList = resourceManagementService.getTestFactoryResourceReservation(workPackage.getWorkPackageId(),weekNo);
						if(testFactResReservationList.size() == 0){
							testFactResReservationList = subTestFactResReservationList;
						}else{
							testFactResReservationList.addAll(subTestFactResReservationList);
						}
					}
				}
				else{
					
					testFactResReservationList = resourceManagementService.getTestFactoryResourceReservation(workPackageId,weekNo);
					productMaster=workpackageService.getProductMasterByWorkpackageId(workPackageId);
				}
				request.getSession().setAttribute("ssnHdnResourceAvailabilityWeekNo", weekNo);
				if(testFactResReservationList != null && testFactResReservationList.size()>0){
					mapOfTestFactoryResourceReservation = new HashMap<String, List<JsonTestFactoryResourceReservation>>();
					List<JsonTestFactoryResourceReservation> jsontestFactResReservationList  = null;
					for(TestFactoryResourceReservation testFactResReservation: testFactResReservationList){
						JsonTestFactoryResourceReservation jsontestFactResReservation = null;
						if(testFactResReservation.getBlockedUser() != null){

							log.info("productMaster.getProductName()"+productMaster.getProductName());
							String key = productMaster.getProductName()+" >> "+testFactResReservation.getWorkPackage().getName()+ " / " +testFactResReservation.getShift().getShiftName()+" >> "+testFactResReservation.getReservationDate();
							log.info("key ::: "+key);
							if(mapOfTestFactoryResourceReservation.containsKey(key)){
								log.info(" key is available.....");
								jsontestFactResReservationList = mapOfTestFactoryResourceReservation.get(key);
								jsontestFactResReservation = new JsonTestFactoryResourceReservation(testFactResReservation);
								log.info("jsontestFactResReservation.getBlockedUserId:: "+jsontestFactResReservation.getBlockedUserId());
								jsontestFactResReservationList.add(jsontestFactResReservation);
								mapOfTestFactoryResourceReservation.put(key, jsontestFactResReservationList);
							}else{
								log.info(" key is not available.....");
								jsontestFactResReservationList = new ArrayList<JsonTestFactoryResourceReservation>();
								jsontestFactResReservation = new JsonTestFactoryResourceReservation(testFactResReservation);
								log.info("jsontestFactResReservation.getBlockedUserId:: ***** "+jsontestFactResReservation.getBlockedUserId());
								jsontestFactResReservationList.add(jsontestFactResReservation);
								mapOfTestFactoryResourceReservation.put(key, jsontestFactResReservationList);
							}
						}
					}
					log.info("mapOfTestFactoryResourceReservation data size() : "+mapOfTestFactoryResourceReservation.size());
					log.info("mapOfTestFactoryResourceReservation .size() : "+mapOfTestFactoryResourceReservation.size());
				}
				
			} catch (Exception e) {
			    log.error("JSON ERROR", e);
			}
			return mapOfTestFactoryResourceReservation;
    	}
	
	@RequestMapping(value="resource.list.calHeatMap.popup",method=RequestMethod.POST ,produces="application/text")
	public @ResponseBody JTableResponse reosurceManagementListForCalHeatMapFilter(ModelMap model,@RequestParam Integer testFactoryLabId, @RequestParam Integer testFactoryId,@RequestParam Integer productId,@RequestParam Integer workPackageId,@RequestParam Integer shiftId,@RequestParam String dataType,HttpServletRequest req ,@RequestParam String workDate,@RequestParam Integer jtStartIndex,@RequestParam Integer jtPageSize) {
		log.info("inside resource.list.calHeatMap");
		
		
		
		
		
		
		JSONObject jsonObj= new JSONObject();
		Date startDate =null;
		Date endDate =null;
		JTableResponse jTableResponse =null;
		
		try {
			Integer output=0;

				List<JsonWorkPackageDemandProjectionList> jsonWorkPackageDemandProjectionList=new ArrayList<JsonWorkPackageDemandProjectionList>();
				List<JsonResourceAvailabilityDetails> jsonResourceAvailabilityDetails=new ArrayList<JsonResourceAvailabilityDetails>();
				List<JsonTestFactoryResourceReservation> jsonTestFactoryResourceReservation=new ArrayList<JsonTestFactoryResourceReservation>();

				if(dataType.equalsIgnoreCase("Demand")){
					List<WorkPackageDemandProjection>  listWorkPackageDemandProjection =resourceManagementService.listWorkpackageDemandProjection(testFactoryLabId,  testFactoryId,  productId,  workPackageId,  shiftId,DateUtility.dateFormatWithOutSeconds(workDate), jtStartIndex, jtPageSize);
					List<WorkPackageDemandProjection>  listWorkPackageDemandProjectionforPagination =resourceManagementService.listWorkpackageDemandProjection(testFactoryLabId,  testFactoryId,  productId,  workPackageId,  shiftId,DateUtility.dateFormatWithOutSeconds(workDate),null,null);
					

					if (listWorkPackageDemandProjection == null || listWorkPackageDemandProjection.isEmpty()) {
						
						jTableResponse = new JTableResponse("OK", jsonWorkPackageDemandProjectionList, 0);
					} else {
						for(WorkPackageDemandProjection workPackageDemandProjection:listWorkPackageDemandProjection){
							jsonWorkPackageDemandProjectionList.add(new JsonWorkPackageDemandProjectionList(workPackageDemandProjection));
						}
						jTableResponse = new JTableResponse("OK", jsonWorkPackageDemandProjectionList,listWorkPackageDemandProjectionforPagination.size() );
						listWorkPackageDemandProjection = null;
					}
					
				}else if(dataType.equalsIgnoreCase("Availablity")){
					List<ResourceAvailability>  listResourceAvailablity =resourceManagementService.listAvaiablitybyDate(testFactoryLabId,  testFactoryId,  productId,  workPackageId,  shiftId,DateUtility.dateFormatWithOutSeconds(workDate), jtStartIndex, jtPageSize);
					List<ResourceAvailability>  listResourceAvailablityforPagination =resourceManagementService.listAvaiablitybyDate(testFactoryLabId,  testFactoryId,  productId,  workPackageId,  shiftId,DateUtility.dateFormatWithOutSeconds(workDate), null, null);
					if (listResourceAvailablity == null || listResourceAvailablity.isEmpty()) {
						
						jTableResponse = new JTableResponse("OK", jsonResourceAvailabilityDetails, 0);
					} else {
						for(ResourceAvailability resourceAvailability:listResourceAvailablity){
							jsonResourceAvailabilityDetails.add(new JsonResourceAvailabilityDetails(resourceAvailability));
						}
						jTableResponse = new JTableResponse("OK", jsonResourceAvailabilityDetails,listResourceAvailablityforPagination.size() );
						listResourceAvailablity = null;
					}
					
				}else if(dataType.equalsIgnoreCase("Blocked")){
					List<TestFactoryResourceReservation> testFactoryResourceReservationList=resourceManagementService.listTestFactoryResourceReservation(testFactoryLabId,  testFactoryId,  productId,  workPackageId,  shiftId,DateUtility.dateFormatWithOutSeconds(workDate), jtStartIndex, jtPageSize);
					List<TestFactoryResourceReservation> testFactoryResourceReservationListforPagination=resourceManagementService.listTestFactoryResourceReservation(testFactoryLabId,  testFactoryId,  productId,  workPackageId,  shiftId,DateUtility.dateFormatWithOutSeconds(workDate), null, null);
					if (testFactoryResourceReservationList == null || testFactoryResourceReservationList.isEmpty()) {
						
						jTableResponse = new JTableResponse("OK", testFactoryResourceReservationList, 0);
					} else {
						for(TestFactoryResourceReservation testFactoryResourceReservation:testFactoryResourceReservationList){
							jsonTestFactoryResourceReservation.add(new JsonTestFactoryResourceReservation(testFactoryResourceReservation));
						}
						jTableResponse = new JTableResponse("OK", jsonTestFactoryResourceReservation,testFactoryResourceReservationListforPagination.size() );
						testFactoryResourceReservationList = null;
					}
					
				}
	        } catch (Exception e) {
	        	
	            log.error("JSON ERROR", e);	            
	        }
        return jTableResponse;
    }
	
	@RequestMapping(value="resource.availability.plan.byShift.update",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse updateResourceAvailabilityInlineByShiftId(@ModelAttribute JsonResourceAvailabilityPlan jsonResourceAvailabilityPlan,BindingResult result,@RequestParam String workDate,@RequestParam Integer shiftListId) {
		log.info("inside resource.availability.plan.byShift.update");
		long startTime = System.currentTimeMillis();
		log.info("startTime: "+startTime);
		JTableResponse jTableResponse = null;
		try {
		
			log.info(jsonResourceAvailabilityPlan.getJsonResourceAvailabilityPlan().getShiftAvailibility());
			log.info(jsonResourceAvailabilityPlan.getJsonResourceAvailabilityPlan().getUserId());
			log.info(jsonResourceAvailabilityPlan.getJsonResourceAvailabilityPlan().getShiftTypeId());
			log.info(jsonResourceAvailabilityPlan.getJsonResourceAvailabilityPlan().getShiftTypeAvailability());
			log.info("workDate>>>>"+workDate);
			UserList userList= userListService.getUserListById(jsonResourceAvailabilityPlan.getUserId());
			Integer shiftTypeAvailability=jsonResourceAvailabilityPlan.getJsonResourceAvailabilityPlan().getShiftTypeAvailability();
			ShiftTypeMaster shiftTypeMaster=resourceManagementService.getShiftTypeMasterById(shiftListId);
			ResourceAvailability resourceAvailability = resourceManagementService.updateResourceAvailabilityInline(userList,shiftTypeMaster,shiftTypeAvailability,workDate);
			if (resourceAvailability == null) {
				jTableResponse = new JTableResponse("Error", "Resource cannot be booked due to non-availability");//, new JsonResourceAvailability(resourceAvailability), 1);
			} else {
				jTableResponse = new JTableResponse("OK", "Resource availability / booking updated successfully");//, new JsonResourceAvailability(resourceAvailability), 1);
			}
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error Updating Availability / Booking the Resource!");
	        log.error("JSON ERROR", e);	            
	    }
		log.info("resource.availability.plan.byShift.update : Controller : " + (System.currentTimeMillis() - startTime));
        return jTableResponse;
    }
	
	@RequestMapping(value="resource.booking.plan.byShift.update",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse updateResourceBookingInlineByShiftId(@ModelAttribute JsonResourceAvailabilityPlan jsonResourceAvailabilityPlan,BindingResult result,@RequestParam String workDate,@RequestParam Integer shiftListId) {
		log.info("inside resource.booking.plan.byShift.update");
		long startTime = System.currentTimeMillis();
		log.info("startTime: "+startTime);
		JTableResponse jTableResponse = null;
		try {
		
			log.info(jsonResourceAvailabilityPlan.getJsonResourceAvailabilityPlan().getShiftAvailibility());
			log.info(jsonResourceAvailabilityPlan.getJsonResourceAvailabilityPlan().getUserId());
			log.info(jsonResourceAvailabilityPlan.getJsonResourceAvailabilityPlan().getShiftTypeId());
			log.info(jsonResourceAvailabilityPlan.getJsonResourceAvailabilityPlan().getShiftTypeBooking());
			log.info("workDate>>>>"+workDate);
			UserList userList= userListService.getUserListById(jsonResourceAvailabilityPlan.getUserId());
			Integer shiftTypeBooking =jsonResourceAvailabilityPlan.getJsonResourceAvailabilityPlan().getShiftTypeBooking();
			ShiftTypeMaster shiftTypeMaster=resourceManagementService.getShiftTypeMasterById(shiftListId);
			ResourceAvailability resourceAvailability = resourceManagementService.updateResourceBookingInline(userList,shiftTypeMaster,shiftTypeBooking,workDate);
			if (resourceAvailability == null) {
				jTableResponse = new JTableResponse("Error", "Resource cannot be booked due to non-availability");//, new JsonResourceAvailability(resourceAvailability), 1);
			} else {
				jTableResponse = new JTableResponse("OK", "Resource availability / booking updated successfully");//, new JsonResourceAvailability(resourceAvailability), 1);
			}
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error Updating Availability / Booking the Resource!");
	        log.error("JSON ERROR", e);	            
	    }
		log.info("resource.availability.plan.byShift.update : Controller : " + (System.currentTimeMillis() - startTime));
        return jTableResponse;
    }
	
	
	@RequestMapping(value="testfactory.resource.management.pool.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listTestFactoryResourcePool(@RequestParam Integer testFactoryId) {			
		JTableResponse jTableResponse;		 
		try {				
			
			List<TestfactoryResourcePool> testFactoryResourcePoolDB=resourceManagementService.testFactoryResourcePoolListbyTFactoryId(testFactoryId);
			List<JsonResourcePool> jsonResourcePool=new ArrayList<JsonResourcePool>();
			log.info("Size----------------"+testFactoryResourcePoolDB.size());
			for(TestfactoryResourcePool testfactoryResourcePool: testFactoryResourcePoolDB){
				jsonResourcePool.add(new JsonResourcePool(testfactoryResourcePool));		
				log.info("RP Id ---> "+testfactoryResourcePool.getResourcePoolName());
			}
	            jTableResponse = new JTableResponse("OK", jsonResourcePool,jsonResourcePool.size());
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }	     
		
		
        return jTableResponse;
    }
	
	@RequestMapping(value="complete.resource.reserved.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody HashMap<String, List<JsonTestFactoryResourceReservation>> listAllReservedResources(@RequestParam int testFactoryId, @RequestParam int shiftId,@RequestParam String reservationDate) {
		log.info("inside  testFactResReservationList ********  testFactoryId: "+testFactoryId);
		
		Date dtReservationDate = null;
	    HashMap<String, List<JsonTestFactoryResourceReservation>> mapOfTestFactoryResourceReservation = null;
			try {
				List<ProductMaster> productMasterList = null;
				List<TestFactoryResourceReservation>   testFactResReservationList = new ArrayList<TestFactoryResourceReservation>();
				
				if(reservationDate != null ) {
					dtReservationDate=DateUtility.dateformatWithOutTime(reservationDate);
				}
				if(testFactoryId != 0){
					productMasterList=productListService.listProductsByTestFactoryId(testFactoryId);
					for(ProductMaster productMaster:productMasterList){
						log.info("productMaster.getProductId()"+productMaster.getProductId());
					List <WorkPackage> listOfWorkPackages = workpackageService.getWorkPackagesByProductId(productMaster.getProductId());
				     log.info("listOfWorkPackages size:::    "+listOfWorkPackages.size());
				     if(listOfWorkPackages.size()!=0){
					for (WorkPackage workPackage : listOfWorkPackages) {
						
						List<TestFactoryResourceReservation>   subTestFactResReservationList = null;
						subTestFactResReservationList = resourceManagementService.getTestFactoryResourceReservation(workPackage.getWorkPackageId(),shiftId,dtReservationDate);
						if(testFactResReservationList.size() == 0){
							testFactResReservationList = subTestFactResReservationList;
						}else{
							testFactResReservationList.addAll(subTestFactResReservationList);
						}
					}
				}
			}
		}else if(testFactoryId == 0 ){
				}
				for(TestFactoryResourceReservation testFactResReservation: testFactResReservationList){
				}
				
				mapOfTestFactoryResourceReservation = new HashMap<String, List<JsonTestFactoryResourceReservation>>();
				List<JsonTestFactoryResourceReservation> jsontestFactResReservationList  = null;
				for(TestFactoryResourceReservation testFactResReservation: testFactResReservationList){
					JsonTestFactoryResourceReservation jsontestFactResReservation = null;
				log.info("product Name based on workpackage:-->"+testFactResReservation.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductName());
					String key = testFactResReservation.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductName()+" >> "+testFactResReservation.getWorkPackage().getName()+ " / " +testFactResReservation.getShift().getShiftName()+" >> "+testFactResReservation.getReservationDate();
                    
					log.info("key ::: "+key);
					if(mapOfTestFactoryResourceReservation.containsKey(key)){
						log.info(" key is available.....");
						jsontestFactResReservationList = mapOfTestFactoryResourceReservation.get(key);
						jsontestFactResReservation = new JsonTestFactoryResourceReservation(testFactResReservation);
						jsontestFactResReservationList.add(jsontestFactResReservation);
						mapOfTestFactoryResourceReservation.put(key, jsontestFactResReservationList);
					}else{
						log.info(" key is not available.....");
						jsontestFactResReservationList = new ArrayList<JsonTestFactoryResourceReservation>();
						jsontestFactResReservation = new JsonTestFactoryResourceReservation(testFactResReservation);
						jsontestFactResReservationList.add(jsontestFactResReservation);
						mapOfTestFactoryResourceReservation.put(key, jsontestFactResReservationList);
					}
				}
				log.info("mapOfTestFactoryResourceReservation data : "+mapOfTestFactoryResourceReservation);
				log.info("mapOfTestFactoryResourceReservation .size() : "+mapOfTestFactoryResourceReservation.size());
			} catch (Exception e) {
			    log.error("JSON ERROR", e);
			}
			return mapOfTestFactoryResourceReservation;
    	}
	
	@RequestMapping(value="resource.attendance.plan.summary",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse listWorkpackageTestCaseExecutionPlanningStatus(@RequestParam Integer resourcePoolId, @RequestParam  Date workDate, @RequestParam Integer shiftId) {
		log.info("resource.attendance.plan.summary -> "+resourcePoolId);
		JTableResponse jTableResponse = null;
		try {
			List<ResourceAttendanceSummaryDTO> resourceAttendanceSummaryDTO  = resourceManagementService.listResourceAttendanceSummary(resourcePoolId, workDate, shiftId) ;
			List<JsonResourceAttendance> jsonResourceAttendanceList = new ArrayList<JsonResourceAttendance>();
				if(resourceAttendanceSummaryDTO!=null){
					for(ResourceAttendanceSummaryDTO resourceAttendanceSummaryDTOList: resourceAttendanceSummaryDTO){
						jsonResourceAttendanceList.add(new JsonResourceAttendance(resourceAttendanceSummaryDTOList));		
						log.info(" RP Name ---> "+resourceAttendanceSummaryDTOList.getResourcePoolName());
					}
				}
				log.info("resource.attendance.plan.summary : "+jsonResourceAttendanceList.size());
				jTableResponse = new JTableResponse("OK", jsonResourceAttendanceList, jsonResourceAttendanceList.size());
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);	            
	        }
        return jTableResponse;
	}
	
	public List<String> getLoginIdOfUsers(String [] selectedResourcesFromUI){
		List<String> blockedUserList = null;
		if(selectedResourcesFromUI != null && selectedResourcesFromUI.length > 0 ){
			blockedUserList = new ArrayList<String>();
			for (String reservedUser : selectedResourcesFromUI) {
				String userLoginId = null;
				if(reservedUser.contains("[")){
					int startIndexOfSquareBrace = reservedUser.indexOf("[");
					userLoginId = reservedUser.substring(0, startIndexOfSquareBrace);
					log.info("userLoginId: "+userLoginId); 
					blockedUserList.add(userLoginId);
				}
			}
		}
		return blockedUserList;
	}
	
	@RequestMapping(value="workPackage.block.resources",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse getReservedResourcesDetailsForWorkPackage(@RequestParam Integer workPackageId,@RequestParam Integer shiftId,@RequestParam String resourceDemandForDate,@RequestParam String filter) throws Exception {
		Date date = null;
		String strResourceDemandForDate = resourceDemandForDate;
		JTableResponse jTableResponse = null;
		
		String errorMessage = "";
		boolean isError = false;
		
		List<JsonUserList> jsonAvailableResources = new ArrayList<JsonUserList>();
		List<JsonUserList> jsonBlockedResourceList = new ArrayList<JsonUserList>(0);
		List<JsonUserList> jsonCompleteList = new ArrayList<JsonUserList>();
		
		log.debug("inside workPackage.block.resources"+resourceDemandForDate);
		try{
				date = DateUtility.dateformatWithOutTime(strResourceDemandForDate);
				if(filter.equalsIgnoreCase("All")){
					jsonAvailableResources = resourceManagementService.getAllUnReservedResourcesForReservation(workPackageId,shiftId,date,filter);
				}else{
					jsonAvailableResources = resourceManagementService.getResourcesForReservation(workPackageId,shiftId,date,filter);
				}
				
				if(jsonAvailableResources != null && jsonAvailableResources.size()>0){
						Collections.sort(jsonAvailableResources, JsonUserList.jsonUserListComparator);
					log.info("availableResources size: "+jsonAvailableResources.size());
				}
				
				jsonBlockedResourceList = resourceManagementService.getBlockedResourcesOfWorkPackage(workPackageId,shiftId,date);
				if(jsonBlockedResourceList != null && jsonBlockedResourceList.size()>0){
					Collections.sort(jsonBlockedResourceList, JsonUserList.jsonUserListComparator);
				}
				
				if(workPackageId == -1){
					if(jsonAvailableResources != null && jsonAvailableResources.size()>0){
						jsonCompleteList.addAll(jsonAvailableResources);
					}
				}else{
					if(jsonBlockedResourceList != null && jsonBlockedResourceList.size()>0){
						jsonCompleteList.addAll(jsonBlockedResourceList);
					}
					if(jsonAvailableResources != null && jsonAvailableResources.size()>0){
						jsonCompleteList.addAll(jsonAvailableResources);
					}
				}
				
				jTableResponse = new JTableResponse("OK", jsonCompleteList,jsonCompleteList.size());
        } catch (Exception e) {
            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
            log.error("JSON ERROR", e);
        }	     
        return jTableResponse;
	}
	
	
	@RequestMapping(value="workpackage.block.resources.for.day.shift")
	public @ResponseBody JTableResponse blockResourcesForWorkpackage(HttpServletRequest req,@ModelAttribute JsonUserList jsonuser, BindingResult result, @RequestParam Integer workpackageId, @RequestParam  String blockResourceForDate,@RequestParam Integer shiftId, @RequestParam Integer demand) {
		Integer reservedorUnreservedUserId = 0;
		JTableResponse jTableResponse = null;
		try {
			String errorMessage = "";
			boolean isError = false;
			reservedorUnreservedUserId = jsonuser.getUserId();
			Integer isReserved = jsonuser.getReserve();
			log.debug("inside workpackage.block.resources.for.day.shift.inline:::  reservedorUnreservedUserId::: "+jsonuser.getUserId()+"   isReserved: "+isReserved);
			Date dtResourceBlockedForDate=null;
			if(blockResourceForDate!=null){
				dtResourceBlockedForDate=DateUtility.dateformatWithOutTime(blockResourceForDate);
			}
			 
			UserList userSession = (UserList)req.getSession().getAttribute("USER");
			Integer loggedInUserId = userSession.getUserId();
			
			
			List<UserList> blockedResourceListFromDB = resourceManagementService.getExistingBlockedResourcesOfWorkPackage(workpackageId, shiftId, dtResourceBlockedForDate);
			if(blockedResourceListFromDB != null && blockedResourceListFromDB.size()>0){
				int existingReserverResourceCount = blockedResourceListFromDB.size();
				if(existingReserverResourceCount == demand && isReserved == 1){
					log.info("demand and available users are same");
					errorMessage = "Resource Demand is already Fullfilled.";
					isError = true;
				}else if(existingReserverResourceCount > demand && isReserved == 1){
					log.info("demand and available users are more than demand");
					errorMessage = "Resource Demand is already Fullfilled.";
					isError = true;
				}
			}

			if (isError) {
				jTableResponse = new JTableResponse("ERROR",errorMessage);
				return jTableResponse;
			}else{
				if(isReserved == 1){
					log.info("Add reservations");
					TestFactoryResourceReservation reservedResource = null;
					reservedResource = resourceManagementService.saveReservedResource(reservedorUnreservedUserId, dtResourceBlockedForDate, workpackageId+"", shiftId+"", loggedInUserId+"");
					if(reservedResource != null){
						log.info("Add reservations completed successfully");
						emailService.sendResourceReservationOrCancellationNotificationMail(req, reservedResource,isReserved);
						jTableResponse = new JTableResponse("OK", "Resource Reserved successfully");
					}else if(reservedResource == null) {
						jTableResponse = new JTableResponse("ERROR", "Reservation Failed");
						return jTableResponse;
					} 
				}else if(isReserved == 0){
					log.info("Remove reservations");
					TestFactoryResourceReservation unBlockResource = resourceManagementService.removeReservationForResource(reservedorUnreservedUserId, dtResourceBlockedForDate,workpackageId+"", shiftId+"", loggedInUserId+"");
					if (unBlockResource == null) {
						jTableResponse = new JTableResponse("Error", "Remove Reservation of Resource Failed. Please contact Admin");
					} else {
						emailService.sendResourceReservationOrCancellationNotificationMail(req, unBlockResource,isReserved);
						jTableResponse = new JTableResponse("OK", "Resource Reservation Removed successfully");
					}
				}
				log.info("Resource reservation process completed");
			}
        } catch (Exception e) {
        	jTableResponse = new JTableResponse("ERROR","Error Resource Reservation!");
            log.error("JSON ERROR", e);
        }		  
		 return jTableResponse;
    }
	
	@RequestMapping(value="resource.availability.reservedStatus.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listForBookingAndReservedStatus(HttpServletRequest req, @RequestParam int resourceId, @RequestParam int weekNo) {
		log.info("resource.availability.reservedStatus.list");
		JTableResponse jTableResponse = null;
		log.info("Week No from Request : " + weekNo);

		try {
				if(resourceId<=0){
					resourceId=1;
				}
				if (weekNo < 0) {
					weekNo = DateUtility.getWeekOfYear();
				}
				req.getSession().setAttribute("ssnHdnResourceAvailabilityWeekNo", weekNo);
				req.getSession().setAttribute("ssnHdnSelectedResourceId", resourceId);
				
				log.info("Setting weekNo in session : " + weekNo);
				List<JsonResourceAvailability> jsonResourceAvailabilityList = resourceManagementService.listForBookingAndReservedStatus(resourceId, weekNo);
				log.info("JsonResourceAvailability size="+jsonResourceAvailabilityList.size());
				jTableResponse = new JTableResponse("OK", jsonResourceAvailabilityList, jsonResourceAvailabilityList.size());
				jsonResourceAvailabilityList = null;
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);	            
	        }
        return jTableResponse;
    }
	
	
	@RequestMapping(value="resource.availability.plan.summary",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse listResourceAvailabiltyPlanSummary(@RequestParam Integer resourcePoolId, @RequestParam  Date workDate, @RequestParam Integer shiftId) {
		log.info("resource.availability.plan.summary -> "+resourcePoolId);
		JTableResponse jTableResponse = null;
		try {
			List<ResourceAttendanceSummaryDTO> resourceAttendanceSummaryDTO  = resourceManagementService.listResourceAvailabiltySummary(resourcePoolId, workDate, shiftId) ;
			List<JsonResourceAttendance> jsonResourceAttendanceList = new ArrayList<JsonResourceAttendance>();
				if(resourceAttendanceSummaryDTO!=null){
					for(ResourceAttendanceSummaryDTO resourceAttendanceSummaryDTOList: resourceAttendanceSummaryDTO){
						jsonResourceAttendanceList.add(new JsonResourceAttendance(resourceAttendanceSummaryDTOList));		
					}
				}
				log.info("resource.availability.plan.summary : "+jsonResourceAttendanceList.size());
				jTableResponse = new JTableResponse("OK", jsonResourceAttendanceList, jsonResourceAttendanceList.size());
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);	            
	        }
        return jTableResponse;
	}
	
	
	@RequestMapping(value="resource.reliable.list",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse listResourceReliable(HttpServletRequest req, @RequestParam Integer resourcePoolId, @RequestParam  Date startDate, @RequestParam  Date endDate, @RequestParam  Integer userId) {
		log.info("inside resource.reliable.list");
		log.info("resourcePoolId="+  resourcePoolId + " resourceStartDate = " + startDate);
		log.info("resourcePoolId="+  resourcePoolId + " resourceEndDate = " + endDate);
		JTableResponse jTableResponse = null;
			try{
				List<JsonResourceAttendance> jsonResourceReliabilityList  = null;
				jsonResourceReliabilityList = resourceManagementService.listResourcesReliable(resourcePoolId, startDate, endDate, userId);
				log.info(" resource.reliable.list : "+jsonResourceReliabilityList.size());
				jTableResponse = new JTableResponse("OK", jsonResourceReliabilityList, jsonResourceReliabilityList.size());
				
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);	            
	        }
        return jTableResponse;
    }
	
	@RequestMapping(value="resourceAvailability.list",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse viewAvailableResourcesDetails(@RequestParam int workPackageId,@RequestParam int shiftId,@RequestParam String availabilityForDate){
		log.info("resourceAvailability.list*********");
		JTableResponse jTableResponse = null;
		Date getAvailabilityForDate=null;
		int shiftTypeId = 0;
		log.info("workPackageId: " + workPackageId);
		log.info("shiftId: " + shiftId);
		log.info("reservationDate: " + availabilityForDate);
		if(availabilityForDate != null ) {
			getAvailabilityForDate=DateUtility.dateformatWithOutTime(availabilityForDate);
		}
		log.info("Availability for Date: "+getAvailabilityForDate);
		if(shiftId != 0){
			ShiftTypeMaster shiftType = testFactoryService.getShiftTypeByShiftId(shiftId);
			if(shiftType != null){
				shiftTypeId = shiftType.getShiftTypeId();
			}
		}
		try {
			List<JsonResourceAvailabilityDetails>   resAvailabilityList =	resourceManagementService.getResourceAvailability(workPackageId,shiftId,shiftTypeId,getAvailabilityForDate);
			log.info("resourceAvailability.list : "+resAvailabilityList.size());
			jTableResponse = new JTableResponse("OK", resAvailabilityList, resAvailabilityList.size());
		} catch (Exception e) {
		    jTableResponse = new JTableResponse("ERROR","Error fetching records!");
		    log.error("JSON ERROR", e);
		} 
        return jTableResponse;
	}
	
	@RequestMapping(value="resource.total.booking.list.by.date",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse listResourceReliableTotalBookings(HttpServletRequest req, @RequestParam Integer userId, @RequestParam  Date startDate, @RequestParam  Date endDate) {
		log.info("inside resource.total.booking.list.by.date");
		JTableResponse jTableResponse = null;
			try{
				List<JsonTestFactoryResourceReservation> jsonTestFactoryResourceReservation  = null;
				jsonTestFactoryResourceReservation = resourceManagementService.listResourcesReliableTotalBookingSummary(userId, startDate, endDate);
				log.info(" resource.total.booking.list.by.date : "+jsonTestFactoryResourceReservation.size());
				jTableResponse = new JTableResponse("OK", jsonTestFactoryResourceReservation, jsonTestFactoryResourceReservation.size());
				
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);	            
	        }
        return jTableResponse;
    }
	
	@RequestMapping(value="workpackage.block.resources.for.workpackage")
	public @ResponseBody JTableResponse reserveResourceForWorkpackage(HttpServletRequest req,@RequestParam Integer workpackageId, @RequestParam  String blockResourceForDate,@RequestParam Integer shiftId, @RequestParam Integer demand, @RequestParam Integer userId, @RequestParam String reserveOrUnreserve,@RequestParam String currentAvailability) {
		log.info("workpackage.block.resources.for.workpackage currentAvailability: "+currentAvailability);
		Integer reservedorUnreservedUserId = 0;
		JTableResponse jTableResponse = null;
		try {
			String errorMessage = "";
			boolean isError = false;
			reservedorUnreservedUserId = userId;
			Integer isReserved = 0;
			if(reserveOrUnreserve.equalsIgnoreCase("Reserve")){
				isReserved = new Integer(1);
			}
			if(reserveOrUnreserve.equalsIgnoreCase("Release")){
				isReserved = new Integer(0);
			}
			log.info("inside workpackage.block.resources.for.day.shift.inline:::  reservedorUnreservedUserId::: "+userId+"  reserveOrUnreserve::: "+ reserveOrUnreserve+ "   ***** isReserved: "+isReserved);
			Date dtResourceBlockedForDate=null;
			if(blockResourceForDate!=null){
				dtResourceBlockedForDate=DateUtility.dateformatWithOutTime(blockResourceForDate);
			}
			 
			log.info("resourceBlockedForDate-->"+blockResourceForDate + "demand : "+demand);
			UserList userSession = (UserList)req.getSession().getAttribute("USER");
			Integer loggedInUserId = userSession.getUserId();
			
			
			List<UserList> blockedResourceListFromDB = resourceManagementService.getExistingBlockedResourcesOfWorkPackage(workpackageId, shiftId, dtResourceBlockedForDate);
			if(blockedResourceListFromDB != null && blockedResourceListFromDB.size()>0){
				int existingReserverResourceCount = blockedResourceListFromDB.size();
				if(existingReserverResourceCount == demand && isReserved == 1){
					log.info("demand and available users are same");
					errorMessage = "Resource Demand is already Fullfilled.";
					isError = true;
				}else if(existingReserverResourceCount > demand && isReserved == 1){
					log.info("demand and available users are more than demand");
					errorMessage = "Resource Demand is already Fullfilled.";
					isError = true;
				}
			}

			if (isError) {
				jTableResponse = new JTableResponse("ERROR",errorMessage);
				return jTableResponse;
			}else{
				if(isReserved == 1){
					log.info("Add reservations");
					TestFactoryResourceReservation testFacrReservedResource = null;
					WorkShiftMaster workShift = testFactoryService.getWorkShiftsByshiftId(shiftId);
					if(currentAvailability.equalsIgnoreCase("All")){
						if(workShift != null){
							boolean isAvailabilityExistForUser = resourceManagementService.isAvailabilityExistForUser(reservedorUnreservedUserId, dtResourceBlockedForDate,workShift.getShiftType().getShiftTypeId());
							if(!isAvailabilityExistForUser){
								resourceManagementService.addEntryForUserInResourceAvailability(reservedorUnreservedUserId, dtResourceBlockedForDate,workShift.getShiftType());
							}
						}
						
					}else if(currentAvailability.equalsIgnoreCase("BookedOrAvailable")){
						resourceManagementService.confirmBookingForAvailableResource(reservedorUnreservedUserId, dtResourceBlockedForDate, workShift.getShiftType().getShiftTypeId());
					}
					testFacrReservedResource = resourceManagementService.saveReservedResource(reservedorUnreservedUserId, dtResourceBlockedForDate, workpackageId+"", shiftId+"", loggedInUserId+"");
					if(testFacrReservedResource != null){
						log.info("Add reservations completed successfully");
						emailService.sendResourceReservationOrCancellationNotificationMail(req, testFacrReservedResource,isReserved);
						jTableResponse = new JTableResponse("OK", "Resource Reserved successfully");
					}else if(testFacrReservedResource == null) {
						jTableResponse = new JTableResponse("ERROR", "Reservation Failed");
						return jTableResponse;
					} 
					
				}else if(isReserved == 0){
					log.info("Remove reservations");
					TestFactoryResourceReservation unBlockResource = resourceManagementService.removeReservationForResource(reservedorUnreservedUserId, dtResourceBlockedForDate,workpackageId+"", shiftId+"", loggedInUserId+"");
					if (unBlockResource == null) {
						jTableResponse = new JTableResponse("ERROR", "Remove Reservation of Resource Failed. Please contact Admin");
						return jTableResponse;
					} else {
						emailService.sendResourceReservationOrCancellationNotificationMail(req, unBlockResource,isReserved);
						jTableResponse = new JTableResponse("OK", "Resource Reservation Removed successfully");
					}
					log.info("Resource reservation process completed");
				}
				
			}
        } catch (Exception e) {
        	jTableResponse = new JTableResponse("ERROR","Error Resource Reservation!");
            log.error("JSON ERROR", e);
        }		  
		 return jTableResponse;
    }
	
	
	@RequestMapping(value="workpackage.save.resources.for.workpackage.for.weekly",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse reserveResourceForWorkpackageForWeekly(HttpServletRequest req,@RequestParam Integer workpackageId, @RequestParam  Integer workWeek, @RequestParam  Integer workYear,@RequestParam Integer shiftId,@RequestParam Integer skillId,@RequestParam Integer userRoleId, @RequestParam Long groupDemandId,@RequestParam Float demand, @RequestParam Integer userId, 
						@RequestParam String reserveOrUnreserve,@RequestParam String currentAvailability, @RequestParam String startDate,@RequestParam String endDate,@RequestParam Integer reservationPercentage,@RequestParam Integer userTypeId,@RequestParam String overOccupancy,@RequestParam String reservationExceed,@RequestParam String percentAndDemandCount) {
		log.info("workpackage.save.resources.for.workpackage.for.weekly: "+currentAvailability);
		Integer reservedorUnreservedUserId = 0;
		JTableResponse jTableResponse = null;
		int totalReservePercentByUserId = 0;
		int totalReservePercentByWpId = 0;
		int exceededReservePercent = 0;
		Date demandStartDate=null;
		Date demandEndDate=null;
		try {
			log.info("reservationPercentage  ---  "+reservationPercentage);
			String errorMessage = "";
			boolean isError = false;
			reservedorUnreservedUserId = userId;
			Integer isReserved = 0;
			
			demandStartDate=DateUtility.dateformatWithOutTime(startDate);
			
			demandEndDate=DateUtility.dateformatWithOutTime(endDate);
			
			Calendar cal = Calendar.getInstance();
			cal.clear();
			cal.setMinimalDaysInFirstWeek(7);
			cal.setFirstDayOfWeek(Calendar.MONDAY);
			cal.setTime(demandEndDate);
			Integer demandStartWeek = cal.get(Calendar.WEEK_OF_YEAR);
					
			cal = Calendar.getInstance();
			cal.clear();
			cal.setMinimalDaysInFirstWeek(7);
			cal.setFirstDayOfWeek(Calendar.MONDAY);
			cal.setTime(demandEndDate);
			Integer demandEndWeek = cal.get(Calendar.WEEK_OF_YEAR);
			
			String recursiveWeeks=demandStartWeek+"-"+demandEndWeek;

			log.info(" userTypeId == > "+userTypeId);
			
			String percentAndDemandCountArr[] = percentAndDemandCount.split("/");
			Double percentVal = Double.parseDouble(percentAndDemandCountArr[0]);
			Double demandVal = Double.parseDouble(percentAndDemandCountArr[1]);
			totalReservePercentByWpId = resourceManagementService.getTotalReservationPercentageByRoleAndSkill(userId, workpackageId, workWeek, workYear, skillId, userRoleId, userTypeId);
			totalReservePercentByUserId = resourceManagementService.getTotalReservationPercentageByUserId(userId, workWeek, workYear);
			if(reservationExceed.equals("NO")){
				if(reservationPercentage > totalReservePercentByWpId){
					if(demandVal<=percentVal){
						return jTableResponse = new JTableResponse ("ERROR","Reservation may exceeds Demand:"+percentVal+":"+demandVal);
					}
				}
			}
			
			if(overOccupancy.equals("NO")){
				if(workWeek != null && workYear != null && userId != null && workpackageId != null){											
						if(totalReservePercentByWpId != totalReservePercentByUserId){
							exceededReservePercent = Math.abs(totalReservePercentByWpId-totalReservePercentByUserId);
							if(reservationPercentage+exceededReservePercent > 100) {
								return jTableResponse = new JTableResponse ("ERROR","occupancy exceeded:"+totalReservePercentByUserId);
							}
						}
						else if(Math.abs(totalReservePercentByWpId-reservationPercentage) > 100){
							return jTableResponse = new JTableResponse("ERROR","occupancy exceeded:"+totalReservePercentByWpId);
						}
					
				}
			}
			
			if(reservationPercentage == 0){
				isReserved = new Integer(0);
			}else{
				isReserved = new Integer(1);
			}
			
			
			Set<Integer> recursiveWeekSet = new HashSet<Integer>();
			
			recursiveWeekSet = resourceManagementService.getRecursiveWeeks(recursiveWeeks, recursiveWeekSet);
			if(recursiveWeekSet.size() == 0){
				recursiveWeekSet.add(workWeek);
			}
			
			
			List<String> errorMessages = new ArrayList<String>();
			for(Integer recursiveWeek : recursiveWeekSet){
				log.info("resourceBlockedFor week -->"+recursiveWeek + "demand : "+demand);
				UserList userSession = (UserList)req.getSession().getAttribute("USER");
				Integer loggedInUserId = userSession.getUserId();
				
				isError = false;

				if (isError) {
					errorMessages.add(errorMessage);
				}else{
					if(isReserved == 1){
						log.info("Add reservations");
						TestFactoryResourceReservation testFacrReservedResource = null;
						WorkShiftMaster workShift = testFactoryService.getWorkShiftsByshiftId(shiftId);
						
					List<TestFactoryResourceReservation>listOfResource=	resourceManagementService.getReservedResourcesWeeklyByUserIdForUpdateOrAdd(workpackageId,recursiveWeek,workYear,shiftId,skillId,reservedorUnreservedUserId,userRoleId,userTypeId);
						
					if(listOfResource != null && listOfResource.size() >0){
						resourceManagementService.updateReservedResourcePercentage(workpackageId,recursiveWeek,workYear,shiftId,skillId,reservationPercentage,reservedorUnreservedUserId,userRoleId,userTypeId);
						
						for(TestFactoryResourceReservation reservation:listOfResource){
							if(reservation != null){
								mongoDBService.addReseveredResourceToMongoDB(reservation.getResourceReservationId());
							}
						}
						
					}else{
						testFacrReservedResource = resourceManagementService.saveReservedResourceForWeekly(reservedorUnreservedUserId, recursiveWeek, workYear,workpackageId, shiftId,skillId,userRoleId, loggedInUserId,groupDemandId,reservationPercentage,userTypeId);
						if(testFacrReservedResource != null){
							log.info("Add reservations completed successfully");
							jTableResponse = new JTableResponse("OK", "Resource Reserved successfully");
						}else if(testFacrReservedResource == null) {
							UserList usr = userListService.getUserListById(reservedorUnreservedUserId);
							errorMessages.add(""+usr.getLoginId()+" not available for week "+recursiveWeek);
						} 
					}
						
					}else if(isReserved == 0){
						log.info("Remove reservations");
						List<TestFactoryResourceReservation> unBlockResources = resourceManagementService.removeReservationForResourceForWeekly(reservedorUnreservedUserId, recursiveWeek,workYear,workpackageId, shiftId, loggedInUserId,groupDemandId,userTypeId);
						
						if (unBlockResources == null) {
							errorMessages.add("Unable to remove reservation of user id - "+reservedorUnreservedUserId+" for week "+recursiveWeek);
						}else{
							jTableResponse = new JTableResponse("OK", "Remove Reservation of Resource Success.");
						}
						log.info("Resource reservation process completed");
					}
					
				}
			}
			
			if(errorMessages != null && errorMessages.size() > 0){
				String returnErrorMessage = "";
				for(String message : errorMessages){
					returnErrorMessage += message+" | ";
				}
				returnErrorMessage = returnErrorMessage.trim().substring(0, returnErrorMessage.length() - 1);
				jTableResponse = new JTableResponse("ERROR", returnErrorMessage);
			}else if(isReserved == 1){
				jTableResponse = new JTableResponse("OK", "Resource Reserved successfully");
			}else if(isReserved == 0){
				jTableResponse = new JTableResponse("OK", "Remove Reservation of Resource Success.");
			}
			
        } catch (Exception e) {
        	jTableResponse = new JTableResponse("ERROR","Error Resource Reservation!");
            log.error("JSON ERROR", e);
        }		  
		 return jTableResponse;
    }
	
	
	
	@RequestMapping(value="resource.show.up.list.by.date",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse listResourceReliableShowUp(HttpServletRequest req, @RequestParam Integer userId, @RequestParam  Date startDate, @RequestParam  Date endDate) {
		log.info("inside resource.show.up.list.by.date");
		JTableResponse jTableResponse = null;
			try{
				List<JsonTestFactoryResourceReservation> jsonTestFactoryResourceReservation  = null;
				jsonTestFactoryResourceReservation = resourceManagementService.listResourcesReliableShowUpSummary(userId, startDate, endDate);
				log.info(" resource.show.up.list.by.date : "+jsonTestFactoryResourceReservation.size());
				jTableResponse = new JTableResponse("OK", jsonTestFactoryResourceReservation, jsonTestFactoryResourceReservation.size());
				
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);	            
	        }
        return jTableResponse;
    }
	
	@RequestMapping(value="resource.on.time.list.by.date",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse listResourceReliableOnTime(HttpServletRequest req, @RequestParam Integer userId, @RequestParam  Date startDate, @RequestParam  Date endDate) {
		log.info("inside resource.on.time.list.by.date");
		JTableResponse jTableResponse = null;
			try{
				List<JsonTestFactoryResourceReservation> jsonTestFactoryResourceReservation  = null;
				jsonTestFactoryResourceReservation = resourceManagementService.listResourcesReliableOnTimeSummary(userId, startDate, endDate);
				log.info(" resource.on.time.list.by.date : "+jsonTestFactoryResourceReservation.size());
				jTableResponse = new JTableResponse("OK", jsonTestFactoryResourceReservation, jsonTestFactoryResourceReservation.size());
				
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);	            
	        }
        return jTableResponse;
    }

	@RequestMapping(value="resource.dailyperformance.update",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse updateResourceDailyPerformance(HttpServletRequest req, 
			@ModelAttribute JsonWorkPackageResultsStatistics jsonWorkPackageResultsStatistics, BindingResult result){
		log.info("inside resource.dailyperformance.update");
		JTableResponse jTableResponse = null;

		if(result.hasErrors()){
			jTableResponse = new JTableResponse("ERROR","Invalid Form!"); 
		}
		try {
			UserList rater = (UserList) req.getSession().getAttribute("USER");
			
			JsonWorkPackageResultsStatistics jsonWorkPackageResultsStatisticsOutput = 
					resourceManagementService.updateResourceDailyPerformance(jsonWorkPackageResultsStatistics, rater);

			List<JsonWorkPackageResultsStatistics> jsonWorkPackageResultsStatisticsList = new ArrayList<JsonWorkPackageResultsStatistics>();
			jsonWorkPackageResultsStatisticsList.add(jsonWorkPackageResultsStatisticsOutput);
			
			log.info("returning data");
			
			jTableResponse = new JTableResponse("OK",jsonWorkPackageResultsStatisticsList,jsonWorkPackageResultsStatisticsList.size());
	    } catch (Exception e) {
	        jTableResponse = new JTableResponse("ERROR","Unable to update Resource Performance !");
	        log.error("JSON ERROR", e);
	    }
	        
        return jTableResponse;
    }
	
	
	@RequestMapping(value="reserved.resources.count",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse listReservedResourcesCountByDate(@RequestParam Integer resourcePoolId, @RequestParam  Date startDate, @RequestParam  Date endDate) {
		log.info("inside reserved.resources.count");
		log.info("resourcePoolId="+  resourcePoolId + " resourceStartDate = " + startDate);
		log.info("resourcePoolId="+  resourcePoolId + " resourceEndDate = " + endDate);
		JTableResponse jTableResponse = null;
			try{
				List<JsonReservedResourcesForBooking> jsonResourceList  = null;
				jsonResourceList = resourceManagementService.getNotAvailAndOrNotBookedResourceCount(resourcePoolId, startDate, endDate);
				log.info(" reserved.resources.count : "+jsonResourceList.size());
				jTableResponse = new JTableResponse("OK", jsonResourceList, jsonResourceList.size());
				
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);	            
	        }
        return jTableResponse;
    }
	
	@RequestMapping(value="list.reserved.resources.for.booking",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse listReservedResourcesForBooking(HttpServletRequest req, @RequestParam int resourcePoolId, @RequestParam  String workDate,@RequestParam int shiftTypeId, int availabilityStatus,int jtStartIndex, int jtPageSize) {
		log.info("inside list.reserved.resources.for.booking");
		log.info("resourcePoolId="+  resourcePoolId + " resourceDemandForDate = " + workDate);
		JTableResponse jTableResponse = null;
		List<JsonResourceAvailabilityPlan> jsonResourceAvailabilityPlanList=null;
		try {
				jsonResourceAvailabilityPlanList = resourceManagementService.listReservedResourcesForBooking(resourcePoolId, DateUtility.dateFormatWithOutSeconds(workDate), shiftTypeId,availabilityStatus,jtStartIndex,jtPageSize);
				List<JsonResourceAvailabilityPlan> jsonResourceAvailabilityPlanListPaging = resourceManagementService.listReservedResourcesForBooking(resourcePoolId, DateUtility.dateFormatWithOutSeconds(workDate), shiftTypeId,availabilityStatus,null,null);
				req.getSession().setAttribute("ssnHdnResourceManagerResourceAvailabilityDate", workDate);
				log.info("jsonResourceAvailabilityPlanList size="+jsonResourceAvailabilityPlanList.size());
				log.info("Selected Date is "+workDate);
				jTableResponse = new JTableResponse("OK", jsonResourceAvailabilityPlanList, jsonResourceAvailabilityPlanListPaging.size());
				jsonResourceAvailabilityPlanList = null;
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);	            
	        }
        return jTableResponse;
    }

	@RequestMapping(value="resource.dailyperformance.approve.update",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse resourceDailyPerformanceApproveUpdate(HttpServletRequest req, 
			@ModelAttribute JsonWorkPackageResultsStatistics jsonWorkPackageResultsStatistics, BindingResult result){
		log.info("inside resource.dailyperformance.approve.update");
		JTableResponse jTableResponse = null;

		if(result.hasErrors()){
			jTableResponse = new JTableResponse("ERROR","Invalid Form!"); 
		}
		try {
		
			UserList approver = (UserList) req.getSession().getAttribute("USER");
			
			JsonWorkPackageResultsStatistics jsonWorkPackageResultsStatisticsOutput = 
					resourceManagementService.resourceDailyPerformanceApproveUpdate(jsonWorkPackageResultsStatistics, approver);

			List<JsonWorkPackageResultsStatistics> jsonWorkPackageResultsStatisticsList = new ArrayList<JsonWorkPackageResultsStatistics>();
			jsonWorkPackageResultsStatisticsList.add(jsonWorkPackageResultsStatisticsOutput);
			
			log.info("returning data");
			
			jTableResponse = new JTableResponse("OK",jsonWorkPackageResultsStatisticsList,jsonWorkPackageResultsStatisticsList.size());
	    } catch (Exception e) {
	        jTableResponse = new JTableResponse("ERROR","Unable to update Resource Performance !");
	        log.error("JSON ERROR", e);
	    }
	        
        return jTableResponse;
    }
	
	
	@RequestMapping(value="make.bookings.for.reserved.resources.bulk",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableSingleResponse updateBookingForReservedResourcesBulk(@RequestParam String resourceListsFromUI, @RequestParam String workDate,@RequestParam Integer shiftTypeId, @RequestParam Integer availabilityStatus) {
		
		JTableSingleResponse jTableSingleResponse = null;
		log.info("resourceListsFromUI::: "+resourceListsFromUI);
		try {
			String[] resourceLists = resourceListsFromUI.split(",");
			for (String resource : resourceLists) {
				log.info("resource: "+resource);
			}
			resourceManagementService.updateAvailabilityForReservedResources(resourceLists,workDate,shiftTypeId,availabilityStatus,1);
			jTableSingleResponse = new JTableSingleResponse("OK");
	    } 
		catch (Exception e) {
            jTableSingleResponse = new JTableSingleResponse("ERROR","Unable to plan the testcase executionselection!");
            log.error("JSON ERROR", e);	            
        }
	        
	        
        return jTableSingleResponse;
    }
	
	@RequestMapping(value="workpackage.block.resources.for.day.shift.bulk")
	public @ResponseBody JTableResponse blockResourcesForWorkpackageBulk(HttpServletRequest req,@RequestParam String  loginId,@RequestParam Integer isReserved,@RequestParam String workPackageName, @RequestParam  String blockResourceForDate,@RequestParam Integer shiftId, @RequestParam Integer demand) {
		Integer reservedorUnreservedUserId = 0;
		Integer workpackageId=0;
		JTableResponse jTableResponse = null;
		try {
			String errorMessage = "";
			boolean isError = false;
			log.info("inside workpackage.block.resources.for.day.shift.inline:::  reservedorUnreservedUserId::: "+loginId+"   isReserved: "+isReserved+"--workPackageName=="+workPackageName+" demand::: "+demand);
			UserList userList=userListService.getUserListByUserName(loginId);
			 reservedorUnreservedUserId=userList.getUserId();
			WorkPackage  workPackage=workpackageService.getWorkPackageByWorkPackageName(workPackageName);
			
			if(workPackage!=null){
			workpackageId=workPackage.getWorkPackageId();
			}
			Date dtResourceBlockedForDate=null;
			if(blockResourceForDate!=null){
				dtResourceBlockedForDate=DateUtility.dateformatWithOutTime(blockResourceForDate);
			}
			 
			UserList userSession = (UserList)req.getSession().getAttribute("USER");
			Integer loggedInUserId = userSession.getUserId();
			
			List<UserList> blockedResourceListFromDB = resourceManagementService.getExistingBlockedResourcesOfWorkPackage(workpackageId, shiftId, dtResourceBlockedForDate);
			if(blockedResourceListFromDB != null && blockedResourceListFromDB.size()>0){
				int existingReserverResourceCount = blockedResourceListFromDB.size();
				if(existingReserverResourceCount == demand && isReserved == 1){
					log.info("demand and available users are same");
					errorMessage = "Resource Demand is already Fullfilled.";
					isError = true;
				}else if(existingReserverResourceCount > demand && isReserved == 1){
					log.info("demand and available users are more than demand");
					errorMessage = "Resource Demand is already Fullfilled.";
					isError = true;
				}
			}

			if (isError) {
				jTableResponse = new JTableResponse("ERROR",errorMessage);
				return jTableResponse;
			}else{
				if(isReserved == 1){
					log.info("Add reservations");
					TestFactoryResourceReservation resourceReserved = null;
					resourceReserved = resourceManagementService.saveReservedResource(reservedorUnreservedUserId, dtResourceBlockedForDate, workpackageId+"", shiftId+"", loggedInUserId+"");
					if(resourceReserved != null){
						log.info("Add reservations completed successfully");
						emailService.sendResourceReservationOrCancellationNotificationMail(req, resourceReserved, isReserved);
						jTableResponse = new JTableResponse("OK", "Resource Reserved successfully");
					}else if(resourceReserved == null) {
						jTableResponse = new JTableResponse("ERROR", "Reservation Failed");
						return jTableResponse;
					} 
				}else if(isReserved == 0){
					log.info("Remove reservations");
					TestFactoryResourceReservation unBlockResource = resourceManagementService.removeReservationForResource(reservedorUnreservedUserId, dtResourceBlockedForDate,workpackageId+"", shiftId+"", loggedInUserId+"");
					if (unBlockResource == null) {
						jTableResponse = new JTableResponse("Error", "Remove Reservation of Resource Failed. Please contact Admin");
					} else {
						emailService.sendResourceReservationOrCancellationNotificationMail(req, unBlockResource, isReserved);
						jTableResponse = new JTableResponse("OK", "Resource Reservation Removed successfully");
					}
				}
				log.info("Resource reservation process completed");
			}
        } catch (Exception e) {
        	jTableResponse = new JTableResponse("ERROR","Error Resource Reservation!");
            log.error("JSON ERROR", e);
        }		  
		 return jTableResponse;
    }
	
	@RequestMapping(value="dashboard.resource.demand",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse getResourceDemandForDateByShiftType(@RequestParam Integer type, @RequestParam String strSelectedDate,@RequestParam Integer testFactoryLabId,@RequestParam Integer resourcePoolId,@RequestParam Integer shiftTypeId) {
		
		JTableResponse jTableResponse = null;
		String result = "";
		try {
			Date selectedDate = DateUtility.strInddMMMyyyyFormatToDate(strSelectedDate);
			
			if(type == 0){
				// Get Demand Details
				Float resourceDemandCount = workpackageService.listWorkpackageDemandProdjectionByDate(selectedDate, testFactoryLabId, shiftTypeId);
				result = resourceDemandCount.toString();
			}
			if(type == 1){
				Float totalDemand = workpackageService.listWorkpackageDemandProdjectionByDate(selectedDate, testFactoryLabId, shiftTypeId);
				Float reservedResourceCount = resourceManagementService.getBlockedResourcesCount(selectedDate,testFactoryLabId, resourcePoolId,shiftTypeId);
				Float percentageReservation = 0f;
				Integer roundOff = 0;
				if(totalDemand != 0 && reservedResourceCount != 0){
					percentageReservation = (reservedResourceCount*100)/totalDemand;
					roundOff = Math.round(percentageReservation);
					log.info("reservedResourceCount : "+reservedResourceCount+"  totalDemand: "+totalDemand);
					log.info("percentageReservation: "+roundOff);
				}
				result = reservedResourceCount+" ["+roundOff+"%] ";
			}
			if(type == 2){
				// Get Attendance Details
				Float reservedResourceCount = resourceManagementService.getBlockedResourcesCount(selectedDate, testFactoryLabId, resourcePoolId,shiftTypeId);
				Float attendedResourceCount = resourceManagementService.getBlockedResourcesAttendanceCount(selectedDate,testFactoryLabId, resourcePoolId,shiftTypeId);
				Float percentageAttended = 0f;
				Integer roundOff = 0;
				if(attendedResourceCount != 0 && reservedResourceCount != 0 && attendedResourceCount < reservedResourceCount){
					percentageAttended = (attendedResourceCount*100)/reservedResourceCount;
					roundOff = Math.round(percentageAttended);
					log.info("reservedResourceCount : "+reservedResourceCount+"  attendedResourceCount: "+attendedResourceCount);
					log.info("percentageReservation: "+roundOff);
				}
				result = attendedResourceCount+" ["+roundOff+"%] ";
			}
			jTableResponse = new JTableResponse("OK", result);
	    } 
		catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","");
            log.error("JSON ERROR", e);	            
        }
        return jTableResponse;
    }
	
	@RequestMapping(value="demand.booking.attendance.details.listing",method=RequestMethod.POST ,produces="application/text")
	public @ResponseBody JTableResponse getDemandBookingandAttendanceDetails(ModelMap model,@RequestParam String dataTypeFilter, @RequestParam Integer testFactoryLabId, @RequestParam Integer resourcePoolId,@RequestParam Integer shiftTypeId,@RequestParam String workDate) {
		Date selectedDate = DateUtility.strInddMMMyyyyFormatToDate(workDate);
		JTableResponse jTableResponse =null;
			try {

				List<JsonWorkPackageDemandProjectionList> jsonWorkPackageDemandProjectionList = null;
				List<JsonResourceAvailabilityDetails> jsonResourceAvailabilityDetails=new ArrayList<JsonResourceAvailabilityDetails>();
				List<JsonTestFactoryResourceReservation> jsonTestFactoryResourceReservation=new ArrayList<JsonTestFactoryResourceReservation>();

				if(dataTypeFilter.equalsIgnoreCase("Demand")){
					jsonWorkPackageDemandProjectionList = testFactoryService.listDemandDetailsOfTestFactoryLab(testFactoryLabId, shiftTypeId,selectedDate);
					log.info("workPackageDemandProjectionList--->"+jsonWorkPackageDemandProjectionList.size());
					if (jsonWorkPackageDemandProjectionList == null || jsonWorkPackageDemandProjectionList.isEmpty()) {
						jTableResponse = new JTableResponse("OK", jsonWorkPackageDemandProjectionList, 0);
					} else {
						jTableResponse = new JTableResponse("OK", jsonWorkPackageDemandProjectionList,jsonWorkPackageDemandProjectionList.size() );
					}
				}else if(dataTypeFilter.equalsIgnoreCase("Reservation")){
					List<TestFactoryResourceReservation> testFactoryResourceReservationList= resourceManagementService.listTestFactoryResourceReservationByDate(testFactoryLabId, resourcePoolId, shiftTypeId, selectedDate);
					if (testFactoryResourceReservationList == null || testFactoryResourceReservationList.isEmpty()) {
						jTableResponse = new JTableResponse("OK", testFactoryResourceReservationList, 0);
					} else {
						for(TestFactoryResourceReservation testFactoryResourceReservation:testFactoryResourceReservationList){
							jsonTestFactoryResourceReservation.add(new JsonTestFactoryResourceReservation(testFactoryResourceReservation));
						}
						jTableResponse = new JTableResponse("OK", jsonTestFactoryResourceReservation,jsonTestFactoryResourceReservation.size() );
						testFactoryResourceReservationList = null;
					}
				}else if(dataTypeFilter.equalsIgnoreCase("Attendance")){
					List<ResourceAvailability>  listResourceAvailablity = resourceManagementService.listAttendanceByDate(testFactoryLabId, resourcePoolId, shiftTypeId, selectedDate);
					List<ResourceAvailability>  listResourceAvailablityForSelectedTestFacLab = new ArrayList<ResourceAvailability>();
					if (listResourceAvailablity == null || listResourceAvailablity.isEmpty()) {
						jTableResponse = new JTableResponse("OK", jsonResourceAvailabilityDetails, 0);
					} else {
						for(ResourceAvailability resourceAvailability:listResourceAvailablity){
							jsonResourceAvailabilityDetails.add(new JsonResourceAvailabilityDetails(resourceAvailability));
						}
						jTableResponse = new JTableResponse("OK", jsonResourceAvailabilityDetails,jsonResourceAvailabilityDetails.size() );
						listResourceAvailablity = null;
					}
				}
	        } catch (Exception e) {
	            log.error("JSON ERROR", e);	            
	        }
        return jTableResponse;
	}
	
	@RequestMapping(value="resource.work.time.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listResourceWorkTime(HttpServletRequest req, @RequestParam int resourceId, @RequestParam int weekNo) {
		log.info("inside resource.work.time.list");
		JTableResponse jTableResponse = null;
		log.info("Week No from Request : " + weekNo);
		try {
				if(resourceId<=0){
					resourceId=1;
				}
				if (weekNo < 0) {
					weekNo = DateUtility.getWeekOfYear();
				}
				req.getSession().setAttribute("ssnHdnResourceAvailabilityWeekNo", weekNo);
				req.getSession().setAttribute("ssnHdnSelectedResourceId", resourceId);
				
				log.info("Setting weekNo in session : " + weekNo);
				List<JsonResourceAvailability> jsonResourceAvailabilityList = resourceManagementService.listWorkPackageDemandProjection(resourceId, weekNo);
				log.info("JsonResourceAvailability size="+jsonResourceAvailabilityList.size());
				jTableResponse = new JTableResponse("OK", jsonResourceAvailabilityList, jsonResourceAvailabilityList.size());
				jsonResourceAvailabilityList = null;
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);	            
	        }
        return jTableResponse;
    }
	
	@RequestMapping(value="user.attendance.details",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody HashMap<Integer,Integer> listAttendanceByUser(HttpServletRequest req, @RequestParam  int weekNo, @RequestParam  String workdate) {
		log.info("inside user.attendance.details");
		UserList user = (UserList)req.getSession().getAttribute("USER");
		HashMap<Integer,Integer> hashMap = null;
		try {
				int userId = user.getUserId();
				hashMap = resourceManagementService.getUserAttendanceForMonth(userId,workdate);
	        } catch (Exception e) {
	        	hashMap = null;
	            log.error("JSON ERROR", e);	            
	        }
        return hashMap;
    }

	
	
	@RequestMapping(value="user.time.management.summary",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse listUserTimeManagementSummary(HttpServletRequest req, @RequestParam int resourceId, @RequestParam int weekNo, @RequestParam  Date workdate) {
		log.info("inside user.time.management.summary");
		JTableResponse jTableResponse = null;
		try {
				if(resourceId<=0){
					resourceId=1;
				}
				if (weekNo < 0) {
					weekNo = DateUtility.getWeekOfYear();
				}
				int weekNoOfSelectedDate = DateUtility.getWeekNumberOfDate(workdate);
				if(weekNoOfSelectedDate == weekNo){
					// If Week No from session and Week Number of selected date is same then it means current date or any dates of current week.
				}else{
					// If the user select any previous date, then week number will vary.
					weekNo = weekNoOfSelectedDate;
				}
				req.getSession().setAttribute("ssnHdnResourceAvailabilityWeekNo", weekNo);
				req.getSession().setAttribute("ssnHdnSelectedResourceId", resourceId);
				
				log.info("Setting weekNo in session : " + weekNo);
				List<JsonResourceShiftCheckinDetailsForWeek> jsonResourceAvailabilityForWeek = resourceManagementService.listUserTimeManagementSummary(resourceId, weekNo);
				log.info("jsonResourceAvailabilityForWeek size="+jsonResourceAvailabilityForWeek.size());
				jTableResponse = new JTableResponse("OK", jsonResourceAvailabilityForWeek, jsonResourceAvailabilityForWeek.size());
				jsonResourceAvailabilityForWeek = null;
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);	            
	        }
        return jTableResponse;
    }
	
	@RequestMapping(value="list.user.by.specific.role",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponseOptions listUserbySpecificRole(int testFactoryId,int productId ) {
		log.debug("inside list.user.by.specific.role");
		JTableResponseOptions jTableResponseOptions = null;
		try {
			ProductMaster product = productListService.getProductDetailsById(productId);
			Integer modeid = product.getProductMode().getModeId();

//			log.info("product=====>"+productId);
//			log.info("modeid=====>"+modeid);
			if(modeid == 2){
				List<UserList> userList = userListService.list();
				if(userList != null){
//					log.info("userList.size : "+userList.size());
					List<JsonUserList> jsonUserList = new ArrayList<JsonUserList>();
					for(UserList user : userList){
						Integer repoolid = user.getResourcePool().getResourcePoolId();
						Integer status = user.getStatus();
						Integer userroleId = user.getUserRoleMaster().getUserRoleId();
//						if(repoolid == -10 && status == 1 && userroleId != 2 ){
						if(repoolid == -10 && status == 1){
//							log.info("user "+user.getFirstName());
							jsonUserList.add(new JsonUserList(user));
						}	
					} 
					if(jsonUserList.size()==0||jsonUserList==null){
						log.info("error");
						jTableResponseOptions = new JTableResponseOptions("ERROR","No Users are available in Product Team!");
						return jTableResponseOptions;
					}else{
						jTableResponseOptions = new JTableResponseOptions("OK", jsonUserList,true); 
						return jTableResponseOptions;
					}
				}
			}else{
				List<JsonUserList> jsonUserList = resourceManagementService.listUserbySpecificRoleByTestFactoryId(testFactoryId,productId);
				if(jsonUserList!=null){
					if(jsonUserList.size()!=0){
						log.info("login");
						jTableResponseOptions = new JTableResponseOptions("OK", jsonUserList,true);   
					}else if(jsonUserList.size()==0){
						log.info("error");
						jTableResponseOptions = new JTableResponseOptions("ERROR","No Resource Pool(s) mapped to the test factory!");
						return jTableResponseOptions;
					}
				}else{
					jTableResponseOptions = new JTableResponseOptions("ERROR","No Resource(s) available in the mapped Resource Pool(s)!");
					return jTableResponseOptions;
				}
			}
		} catch (Exception e) {
			jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
			log.error("JSON ERROR", e);
		}

		return jTableResponseOptions;
	}
	
	@RequestMapping(value="cancel.bookings.for.unbooked.reserved.resources.bulk",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableSingleResponse cancelReservationsForUnbookedResourcesBulk(@RequestParam String resourceListsFromUI, @RequestParam String workDate,@RequestParam Integer shiftTypeId, @RequestParam Integer availabilityStatus) {
		
		JTableSingleResponse jTableSingleResponse = null;
		log.info("resourceListsFromUI::: "+resourceListsFromUI);
		try {
			String[] resourceLists = resourceListsFromUI.split(",");
			for (String resource : resourceLists) {
				log.info("resource: "+resource);
			}
			resourceManagementService.updateAvailabilityForReservedResources(resourceLists,workDate,shiftTypeId,availabilityStatus,0);
			jTableSingleResponse = new JTableSingleResponse("OK");
	    } 
		catch (Exception e) {
            jTableSingleResponse = new JTableSingleResponse("ERROR","Unable to plan the testcase executionselection!");
            log.error("JSON ERROR", e);	            
        }
	        
	        
        return jTableSingleResponse;
    }
	
	@RequestMapping(value="availability.marking.or.removing.bulk",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableSingleResponse availabilityMarkingOrRemovingBulk(@RequestParam String resourceListsFromUI, @RequestParam String workDate,@RequestParam Integer shiftTypeId, @RequestParam Integer availabilityMarkingType) {
		
		JTableSingleResponse jTableSingleResponse = null;
		try {
			String[] resourceLists = resourceListsFromUI.split(",");
			for (String resource : resourceLists) {
			}
			resourceManagementService.updateAvailabilityForResourcesBulk(resourceLists,workDate,shiftTypeId,availabilityMarkingType);
			jTableSingleResponse = new JTableSingleResponse("OK");
	    } 
		catch (Exception e) {
            jTableSingleResponse = new JTableSingleResponse("ERROR","Unable to Update the Availability of Resource(s)!");
            log.error("JSON ERROR", e);	            
        }
        return jTableSingleResponse;
    }
	
	@RequestMapping(value="mark.or.remove.booking.bulk",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableSingleResponse markOrRemoveBookingBulk(@RequestParam String resourceListsFromUI, @RequestParam String workDate,@RequestParam Integer shiftTypeId, @RequestParam Integer availabilityMarkingType) {
		
		JTableSingleResponse jTableSingleResponse = null;
		String message = "";
		log.info("resourceListsFromUI::: "+resourceListsFromUI);
		try {
			String[] resourceLists = resourceListsFromUI.split(",");
			for (String resource : resourceLists) {
				log.info("resource: "+resource);
			}
			message = resourceManagementService.updateBookingForResourcesBulk(resourceLists,workDate,shiftTypeId,availabilityMarkingType);
			jTableSingleResponse = new JTableSingleResponse("OK",message);
	    } 
		catch (Exception e) {
            jTableSingleResponse = new JTableSingleResponse("ERROR","Unable to Update the Booking deatils of Resource(s)!");
            log.error("JSON ERROR", e);	            
        }
        return jTableSingleResponse;
    }
	
	@RequestMapping(value="resource.calendar.monthly.view.list",method=RequestMethod.GET ,produces="application/json")
	public @ResponseBody JTableResponse calendarMonthlyView(ModelMap model,@RequestParam Integer monthNumber,@RequestParam Integer testFactoryLabId, @RequestParam Integer testFactoryId,@RequestParam Integer productId,@RequestParam Integer workPackageId,@RequestParam Integer shiftId,@RequestParam String dataType,HttpServletRequest req) {
		log.info("inside resource.calendar.monthly.view.list");
		JTableResponse jTableResponse = null;
		List<JsonResourceDemandAndAvailabilityView> jsonResourceDemandAndAvailabilityList = new ArrayList<JsonResourceDemandAndAvailabilityView>();
		try {
				if (monthNumber < 0) {
					monthNumber = DateUtility.getMonthOfDate(new Date());
				}
				Date startDate = DateUtility.dateFormatWithOutSeconds("2016-01-01");
				Date endDate = DateUtility.dateFormatWithOutSeconds("2016-01-31");
				List<WorkPackageDemandProjectionDTO>  listWorkPackageDemandProjectionDTO = workpackageService.listWorkpackageDemandProdjectionByWorkpackageId(startDate, endDate,  testFactoryLabId,  testFactoryId,  productId,  workPackageId,  shiftId, IDPAConstants.MONTHLY_VIEW_TYPE);
				for(WorkPackageDemandProjectionDTO wpDTO : listWorkPackageDemandProjectionDTO){
					JsonResourceDemandAndAvailabilityView jsonResourceDemandAndAvailabilityView = new JsonResourceDemandAndAvailabilityView();
					jsonResourceDemandAndAvailabilityView.setStart(DateUtility.sdfDateformatWithOutTime(wpDTO.getWorkDate()));
					jsonResourceDemandAndAvailabilityView.setTitle("Demand: "+wpDTO.getResourceCount());
					jsonResourceDemandAndAvailabilityList.add(jsonResourceDemandAndAvailabilityView);
				}
				List<ResourceAvailabilityDTO> resourceAvailabilityList = resourceManagementService.getResourceAvailablityList(startDate, endDate,  testFactoryLabId,  testFactoryId,  productId,  workPackageId,  shiftId, IDPAConstants.MONTHLY_VIEW_TYPE);
				for(ResourceAvailabilityDTO resourceAvailabilityDTO : resourceAvailabilityList){
					JsonResourceDemandAndAvailabilityView jsonResourceDemandAndAvailabilityView = new JsonResourceDemandAndAvailabilityView();
					jsonResourceDemandAndAvailabilityView.setStart(DateUtility.sdfDateformatWithOutTime(resourceAvailabilityDTO.getWorkDate()));
					jsonResourceDemandAndAvailabilityView.setTitle("Available: "+resourceAvailabilityDTO.getResourceAvailabilityCount());
					jsonResourceDemandAndAvailabilityList.add(jsonResourceDemandAndAvailabilityView);
				}
				List<TestFactoryResourceReservationDTO> testFactoryResourceReservationList=resourceManagementService.getTestFactoryResourceReservationList(startDate, endDate,  testFactoryLabId,  testFactoryId,  productId,  workPackageId,  shiftId, IDPAConstants.MONTHLY_VIEW_TYPE);
				for(TestFactoryResourceReservationDTO reservationDTO : testFactoryResourceReservationList){
					JsonResourceDemandAndAvailabilityView jsonResourceDemandAndAvailabilityView = new JsonResourceDemandAndAvailabilityView();
					jsonResourceDemandAndAvailabilityView.setStart(DateUtility.sdfDateformatWithOutTime(reservationDTO.getWorkDate()));
					jsonResourceDemandAndAvailabilityView.setTitle("Reserved: "+reservationDTO.getBlockedCount());
					jsonResourceDemandAndAvailabilityList.add(jsonResourceDemandAndAvailabilityView);
				}
				
				for(WorkPackageDemandProjectionDTO wpDTO : listWorkPackageDemandProjectionDTO){
					for(ResourceAvailabilityDTO resourceAvailabilityDTO : resourceAvailabilityList){
						if(wpDTO.getWorkDate().equals(resourceAvailabilityDTO.getWorkDate())){
							JsonResourceDemandAndAvailabilityView jsonResourceDemandAndAvailabilityView = new JsonResourceDemandAndAvailabilityView();
							jsonResourceDemandAndAvailabilityView.setStart(DateUtility.sdfDateformatWithOutTime(wpDTO.getWorkDate()));
							Float shortage = 0f;
							shortage = wpDTO.getResourceCount()-resourceAvailabilityDTO.getResourceAvailabilityCount();
							if(shortage<0){
								shortage = 0f;
							}
							jsonResourceDemandAndAvailabilityView.setTitle("Shortage: "+shortage);
							jsonResourceDemandAndAvailabilityList.add(jsonResourceDemandAndAvailabilityView);
							
						}
					}
				}
				
				log.info("jsonResourceDemandAndAvailabilityList size="+jsonResourceDemandAndAvailabilityList.size());
				jTableResponse = new JTableResponse("OK", jsonResourceDemandAndAvailabilityList, jsonResourceDemandAndAvailabilityList.size());
				jsonResourceDemandAndAvailabilityList = null;
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);	            
	        }
        return jTableResponse;
    }
	
	
	@RequestMapping(value="workpackage.demand.projection.list.for.all.weeks",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listWorkpackageDemandProjectionForAllWeeks(HttpServletRequest req, @RequestParam int workPackageId, @RequestParam int weekNumber, @RequestParam int weekYear) {
		log.info("workpackage.demand.projection.list.for.all.weeks");
		JTableResponse jTableResponse = null;
		int weekNo = weekNumber;
		log.info("Week No from Request : " + weekNo);

		try {
				if(workPackageId<=0){
					workPackageId=1;
				}
				if (weekNo < 0) {
					weekNo = DateUtility.getWeekOfYear();
				}
				req.getSession().setAttribute("currentWorkPackageResourceDemandWeekNo", weekNo);
				WorkPackage workPackage = workpackageService.getWorkPackageById(workPackageId);
				
				Integer testFactoryLabId = workPackage.getProductBuild().getProductVersion().getProductMaster().getTestFactory().getTestFactoryLab().getTestFactoryLabId();
				Integer 	testFactoryId = workPackage.getProductBuild().getProductVersion().getProductMaster().getTestFactory().getTestFactoryId();	
				Integer productId = workPackage.getProductBuild().getProductVersion().getProductMaster().getProductId();
				Integer shiftId =3;
				
				List<JsonWorkPackageWeeklyDemandProjection> jsonWorkPackageWeeklyDemandProjection = resourceManagementService.listWorkpackageWeeklyDemandProjection(testFactoryLabId, testFactoryId, productId,workPackageId, shiftId,  weekNo, weekYear);
				
				if (jsonWorkPackageWeeklyDemandProjection == null || jsonWorkPackageWeeklyDemandProjection.isEmpty()) {
					
					log.info("No Demand projections found. Or workshifts have not been defined for the TestFactory : "+workPackageId  );
					jTableResponse = new JTableResponse("ERROR", "No Demand projections found. Or workshifts have not been defined for the TestFactory");
				} else {
					
					jTableResponse = new JTableResponse("OK", jsonWorkPackageWeeklyDemandProjection, jsonWorkPackageWeeklyDemandProjection.size());
				}
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error getting Resource Demand for Workpackage!");
	            log.error("JSON ERROR", e);	            
	        }
        return jTableResponse;
    }
	
	
	@RequestMapping(value="workpackage.resource.reservation.projection.list.for.all.weeks",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listWorkpackageResourceReservationProjectionForAllWeeks(HttpServletRequest req, @RequestParam int workPackageId, @RequestParam int reservationWeek, @RequestParam int reservationYear) {
		log.info("workpackage.demand.projection.list.for.all.weeks");
		JTableResponse jTableResponse = null;
		try {
				if(workPackageId<=0){
					workPackageId=1;
				}
				if (reservationWeek < 0) {
					reservationWeek = DateUtility.getWeekOfYear();
				}
				req.getSession().setAttribute("currentWorkPackageResourceDemandWeekNo", reservationWeek);
				WorkPackage workPackage = workpackageService.getWorkPackageById(workPackageId);
				
				Integer shiftId =3;
				
				List<JsonWorkPackageWeeklyResourceReservation> jsonWorkPackageWeeklyResourceReservation = resourceManagementService.listWorkpackageWeeklyResourceReservationProjection(workPackageId,reservationWeek,reservationYear,1);
				
				if (jsonWorkPackageWeeklyResourceReservation == null || jsonWorkPackageWeeklyResourceReservation.isEmpty()) {
					
					log.info("No Demand projections found. Or workshifts have not been defined for the TestFactory : "+workPackageId  );
					jTableResponse = new JTableResponse("ERROR", "No Demand projections found. Or workshifts have not been defined for the TestFactory");
				} else {
					
					jTableResponse = new JTableResponse("OK", jsonWorkPackageWeeklyResourceReservation, jsonWorkPackageWeeklyResourceReservation.size());
				}
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error getting Resource Demand for Workpackage!");
	            log.error("JSON ERROR", e);	            
	        }
        return jTableResponse;
    }
	
	
	
	
	@RequestMapping(value="workpackage.demand.projection.weekly.add",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableSingleResponse addSkillSpecificDemandProjectionForWorkPackage(HttpServletRequest request, @ModelAttribute JsonWorkPackageDemandProjection jsonWorkPackageDemandProjection, BindingResult result) {
		JTableSingleResponse jTableSingleResponse;
		UserList user = null;
		UserList userObj = null;
		String remarks="";
		try {  
			if(request != null ){
				user = (UserList)request.getSession().getAttribute("USER");
				userObj = userListService.getUserListById(user.getUserId());
			}
			
			WorkPackage workPackage = workPackageService.getWorkPackageById(jsonWorkPackageDemandProjection.getWorkPackageId());
			WorkShiftMaster shift = testFactoryManagementService.getWorkShiftsByshiftId(jsonWorkPackageDemandProjection.getShiftId());
			Skill skill = skillService.getBySkillId(jsonWorkPackageDemandProjection.getSkillId());
			UserRoleMaster userRole = userListService.getRoleById(jsonWorkPackageDemandProjection.getUserRoleId());
			UserTypeMasterNew userType = userTypeMasterNewService.getByuserTypeId(jsonWorkPackageDemandProjection.getUserTypeId());
			
			Set<Integer> recursiveWeeks = new HashSet<Integer>();
			recursiveWeeks = resourceManagementService.getRecursiveWeeks(jsonWorkPackageDemandProjection.getRecursiveWeeks(), recursiveWeeks);
			if(recursiveWeeks.size() == 0){
				recursiveWeeks.add(jsonWorkPackageDemandProjection.getWorkWeek());
			}
			
			WorkPackageDemandProjection workPackageDemandProjection = null;
			for(Integer recursiveWeek : recursiveWeeks){
				
				List<WorkPackageDemandProjection> workPackageDemandProjections = resourceManagementService.getWorkpackageDemandProjection(workPackage.getWorkPackageId(), shift.getShiftId(), skill.getSkillId(), userRole.getUserRoleId(), recursiveWeek, jsonWorkPackageDemandProjection.getWorkYear(),jsonWorkPackageDemandProjection.getUserTypeId());
				
				if(workPackageDemandProjections != null && workPackageDemandProjections.size() > 0){
					for(WorkPackageDemandProjection workPackageDemandProjectionLoop : workPackageDemandProjections){
						workPackageDemandProjectionLoop.setResourceCount(workPackageDemandProjectionLoop.getResourceCount() + jsonWorkPackageDemandProjection.getSkillandRoleBasedresourceCount());
						workPackageService.updateWorkPackageDemandProjection(workPackageDemandProjectionLoop);	
						remarks = "WP:"+workPackageDemandProjectionLoop.getWorkPackage().getName()+"  WK:"+workPackageDemandProjectionLoop.getWorkWeek()+"  Shift:"+workPackageDemandProjectionLoop.getWorkShiftMaster().getShiftName()
								+"  Role: "+workPackageDemandProjectionLoop.getUserRole().getRoleLabel()+" Skill:"+workPackageDemandProjectionLoop.getSkill().getSkillName()+"  DemandedCount: "+workPackageDemandProjectionLoop.getResourceCount();
						eventService.addNewEntityEvent(IDPAConstants.ENTITY_RESOURCE_DEMAND, workPackageDemandProjectionLoop.getWpDemandProjectionId(), remarks, userObj);

						
						if(workPackageDemandProjectionLoop != null && workPackageDemandProjectionLoop.getWpDemandProjectionId() != null){
							mongoDBService.addResourceDemandToMongoDB(workPackageDemandProjectionLoop.getWpDemandProjectionId());
						}
						
					}
					workPackageDemandProjection = workPackageDemandProjections.get(0);
				}else{
					workPackageDemandProjection = jsonWorkPackageDemandProjection.workPackageDemandProjection();
					workPackageDemandProjection.setWorkPackage(workPackage);
					workPackageDemandProjection.setWorkShiftMaster(shift);
					workPackageDemandProjection.setSkill(skill);
					workPackageDemandProjection.setUserRole(userRole);
					workPackageDemandProjection.setWorkDate(DateUtility.dateformatWithOutTime(jsonWorkPackageDemandProjection.getWorkDate()));
					workPackageDemandProjection.setWorkWeek(recursiveWeek);
					workPackageDemandProjection.setWorkYear(jsonWorkPackageDemandProjection.getWorkYear());
					workPackageDemandProjection.setDemandMode("Weekly");
					workPackageDemandProjection.setUserTypeMasterNew(userType);
					workPackageDemandProjection.setResourceCount(jsonWorkPackageDemandProjection.getSkillandRoleBasedresourceCount());
					workPackageDemandProjection.setDemandRaisedOn(new Date());
					workPackageDemandProjection.setDemandRaisedByUser(user);
					workPackageDemandProjection.setGroupDemandId(System.currentTimeMillis());
					Float resourceCount = workPackageDemandProjection.getResourceCount();
					if(resourceCount == null || resourceCount == 0){
						resourceCount = 1f;
						jsonWorkPackageDemandProjection.setResourceCount(resourceCount);
					}
							
					Date startDate = DateUtility.getWeekStartDateFromJanMonday(recursiveWeek,jsonWorkPackageDemandProjection.getWorkYear());
					Calendar startCalendar = Calendar.getInstance();
					startCalendar.setTime(startDate);
					
					Calendar endCalendar = Calendar.getInstance();
					endCalendar.setTime(startDate);
					endCalendar.add(Calendar.DAY_OF_MONTH, 4);
					endCalendar.set(Calendar.HOUR_OF_DAY, 23);
					endCalendar.set(Calendar.MINUTE, 59);
					endCalendar.set(Calendar.SECOND, 59);
					
					Date endDate = endCalendar.getTime();
					
					while(startDate.before(endDate)){
						workPackageDemandProjection.setWorkDate(startDate);
						workPackageDemandProjection.setWpDemandProjectionId(null);
						workPackageService.addWorkPackageDemandProjection(workPackageDemandProjection);	
						if(workPackageDemandProjection != null && workPackageDemandProjection.getWpDemandProjectionId() != null){
							mongoDBService.addResourceDemandToMongoDB(workPackageDemandProjection.getWpDemandProjectionId());
						}
						remarks = "WP:"+workPackageDemandProjection.getWorkPackage().getName()+"  WK:"+workPackageDemandProjection.getWorkWeek()+"  Shift:"+workPackageDemandProjection.getWorkShiftMaster().getShiftName()
						+"  Role: "+workPackageDemandProjection.getUserRole().getRoleLabel()+" Skill:"+workPackageDemandProjection.getSkill().getSkillName()+"  DemandedCount: "+workPackageDemandProjection.getResourceCount();
						eventService.addNewEntityEvent(IDPAConstants.ENTITY_RESOURCE_DEMAND, workPackageDemandProjection.getWpDemandProjectionId(), remarks, userObj);
						startCalendar.add(Calendar.DAY_OF_MONTH, 1);
						startDate = startCalendar.getTime();
					}
				}
				
			}
			if(workPackageDemandProjection != null){
				jTableSingleResponse = new JTableSingleResponse("OK", new JsonWorkPackageDemandProjection(workPackageDemandProjection));
			}else{
				jTableSingleResponse = new JTableSingleResponse("OK", jsonWorkPackageDemandProjection);
			}
        } catch (Exception e) {
            jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding record!");
            log.error("JSON ERROR", e);
        }
        return jTableSingleResponse;
    }
	
	@RequestMapping(value="workpackage.resource.reservation.projection.weekly.add",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableSingleResponse assignSkillSpecificResourceProjectionForWorkPackage(HttpServletRequest request, @RequestParam Integer workPackageId,Integer shiftId,Integer reservationWeek,Integer reservationYear,Integer resourceId,Integer reservedPercentage) {
		JTableSingleResponse jTableSingleResponse;
		UserList user = null;
		try {  
			if(request != null ){
				user = (UserList)request.getSession().getAttribute("USER");
			}
			
			WorkPackage workPackage = workPackageService.getWorkPackageById(workPackageId);
			WorkShiftMaster shift = testFactoryManagementService.getWorkShiftsByshiftId(shiftId);
			UserList resource = userListService.getUserListById(resourceId);
			UserRoleMaster userRole = userListService.getRoleById(resource.getUserRoleMaster().getUserRoleId());
			
			TestFactoryResourceReservation testFactoryResourceReservation = new TestFactoryResourceReservation();
			
			testFactoryResourceReservation.setWorkPackage(workPackage);
			testFactoryResourceReservation.setShift(shift);
			testFactoryResourceReservation.setReservationWeek(reservationWeek);
			testFactoryResourceReservation.setReservationYear(reservationYear);
			testFactoryResourceReservation.setReservationMode("Weekly");
			
			testFactoryResourceReservation.setReservationPercentage(reservedPercentage);
			testFactoryResourceReservation.setReservationDate(new Date());
			testFactoryResourceReservation.setReservationActionUser(user);
			testFactoryResourceReservation.setGroupReservationId(System.currentTimeMillis());
			Integer reservationPercentage = testFactoryResourceReservation.getReservationPercentage();
				if (reservationPercentage != null){
					if(reservationPercentage == 0 || reservationPercentage<0){
						jTableSingleResponse = new JTableSingleResponse("INFORMATION","Please provide a valid value for resource(s) count");
					}else{
						
						Date startDate = DateUtility.getWeekStartDate(reservationWeek,reservationYear);
						Calendar startCalendar = Calendar.getInstance();
						startCalendar.setTime(startDate);
						
						Calendar endCalendar = Calendar.getInstance();
						endCalendar.setTime(startDate);
						endCalendar.add(Calendar.DAY_OF_MONTH, 6);
						endCalendar.set(Calendar.HOUR_OF_DAY, 23);
						endCalendar.set(Calendar.MINUTE, 59);
						endCalendar.set(Calendar.SECOND, 59);
						
						Date endDate = endCalendar.getTime();
						
						while(startDate.before(endDate)){
							testFactoryResourceReservation.setReservationActionDate(startDate);
							testFactoryResourceReservation.setResourceReservationId(null);
							workPackageService.addTestfactoryResourceReservationWeekly(testFactoryResourceReservation);	
							startCalendar.add(Calendar.DAY_OF_MONTH, 1);
							startDate = startCalendar.getTime();
						}
						jTableSingleResponse = new JTableSingleResponse("OK",new JsonTestFactoryResourceReservation(testFactoryResourceReservation));
					}
				}else{
					jTableSingleResponse = new JTableSingleResponse("INFORMATION","Please provide the resource(s) count.");
				}
	        } catch (Exception e) {
	            jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding record!");
	            log.error("JSON ERROR", e);
	        }
        return jTableSingleResponse;
    }

	@RequestMapping(value="workpackage.skill.demand.projection.list.by.week.and.year",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listWorkpackageSkillSpecificDemandProjectionByWeek(@RequestParam int workPackageId, @RequestParam int shiftId, @RequestParam Integer workWeek, @RequestParam  Integer workYear,@RequestParam Integer skillId,@RequestParam Integer roleId,Integer userTypeId, @RequestParam String recursiveWeeks) {
		log.info("workpackage.skill.demand.projection.list.by.week.and.year ");
		
		JTableResponse jTableResponse = null;
		try {
				if(workPackageId<=0){
					workPackageId=1;
				}
				Set<Integer> recursiveWeekSet = new HashSet<Integer>();
				recursiveWeekSet = resourceManagementService.getRecursiveWeeks(recursiveWeeks, recursiveWeekSet);
				if(recursiveWeekSet.size() == 0){
					recursiveWeekSet.add(workWeek);
				}
				
				List<JsonWorkPackageDemandProjection> jsonWorkPackageDemandProjectionList = workPackageService.listWorkPackageDemandProjectionByWorkpackageWeekAndYear(workPackageId, shiftId, recursiveWeekSet,workYear,skillId,roleId,userTypeId);
				
				if (jsonWorkPackageDemandProjectionList != null && jsonWorkPackageDemandProjectionList.size()>0) {
					jTableResponse = new JTableResponse("OK", jsonWorkPackageDemandProjectionList, jsonWorkPackageDemandProjectionList.size());
				} else{
					jTableResponse = new JTableResponse("OK", null, 0);
				}
				jsonWorkPackageDemandProjectionList = null;
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error getting Resource Demand for Workpackage!");
	            log.error("JSON ERROR", e);	            
	        }
        return jTableResponse;
    }
	
	
	@RequestMapping(value="workpackage.skill.resource.reservation.list.by.week.and.year",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listWorkpackageResourceReservationByWeek(@RequestParam int workPackageId, @RequestParam int shiftId, @RequestParam Integer reservationWeek, @RequestParam  Integer reservationYear,@RequestParam Integer skillId,@RequestParam Integer resourceId) {
		log.info("workpackage.skill.resource.reservation.list.by.week.and.year");
		
		JTableResponse jTableResponse = null;
		try {
				if(workPackageId<=0){
					workPackageId=1;
				}
				
				List<JsonTestFactoryResourceReservation> jsonTestFactoryResourceReservationList = workPackageService.listWorkPackageResourceReservationByWorkpackageWeekAndYear(workPackageId, shiftId, reservationWeek,reservationYear,resourceId);
				
				if (jsonTestFactoryResourceReservationList != null && jsonTestFactoryResourceReservationList.size()>0) {
					jTableResponse = new JTableResponse("OK", jsonTestFactoryResourceReservationList, jsonTestFactoryResourceReservationList.size());
				} else{
					jTableResponse = new JTableResponse("OK", null, 0);
				}
				jsonTestFactoryResourceReservationList = null;
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error getting Resource Reservation for Workpackage!");
	            log.error("JSON ERROR", e);	            
	        }
        return jTableResponse;
    }
	
	@RequestMapping(value="workpackage.demand.projection.skill.weekly.data.update",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse updateDemandProjection(HttpServletRequest req, @ModelAttribute JsonWorkPackageDemandProjection jsonWorkPackageDemandProjection,BindingResult result) {
		JTableResponse jTableResponse = null;
		UserList user = null;
		String remarks = "";
		Float resourceCount = 0f;
		WorkPackageDemandProjection workPackageDemandProjection = null;
		if(result.hasErrors()){
			jTableResponse = new JTableResponse("ERROR","Invalid Form!"); 
		}	
		try {
			
			List<JsonWorkPackageDemandProjection> jDemandProjections = new ArrayList<JsonWorkPackageDemandProjection>();
			
			jDemandProjections.add(jsonWorkPackageDemandProjection);
			
			workPackageDemandProjection = jsonWorkPackageDemandProjection.workPackageDemandProjection();
			WorkPackageDemandProjection wpdemand = workpackageService.getWorkPackageDemandProjectionById(jsonWorkPackageDemandProjection.getWpDemandProjectionId());
			
			Set<Integer> recursiveWeekSet = new HashSet<Integer>();
			recursiveWeekSet = resourceManagementService.getRecursiveWeeks(jsonWorkPackageDemandProjection.getRecursiveWeeks(), recursiveWeekSet);
			if(recursiveWeekSet.size() == 0){
				recursiveWeekSet.add(wpdemand.getWorkWeek());
			}
			List<WorkPackageDemandProjection> workPackageDemandProjectionList = new ArrayList<WorkPackageDemandProjection>();
			for(Integer rwk:recursiveWeekSet){
				List<WorkPackageDemandProjection> existingDemandProjectionList = resourceManagementService.getWorkpackageDemandProjection(wpdemand.getWorkPackage().getWorkPackageId(), wpdemand.getWorkShiftMaster().getShiftId(), wpdemand.getSkill().getSkillId(), wpdemand.getUserRole().getUserRoleId(), rwk, wpdemand.getWorkYear(),wpdemand.getUserTypeMasterNew().getUserTypeId());
				if(existingDemandProjectionList != null && existingDemandProjectionList.size() > 0){
					for(WorkPackageDemandProjection existingDemandProjection : existingDemandProjectionList){
						existingDemandProjection.setResourceCount(jsonWorkPackageDemandProjection.getSkillandRoleBasedresourceCount());
						//existingDemandProjection.setWorkWeek(rwk);
					}
				}else{
					existingDemandProjectionList = resourceManagementService.getWorkpackageDemandProjection(wpdemand.getWorkPackage().getWorkPackageId(), wpdemand.getWorkShiftMaster().getShiftId(), wpdemand.getSkill().getSkillId(), wpdemand.getUserRole().getUserRoleId(), wpdemand.getWorkWeek(), wpdemand.getWorkYear(),wpdemand.getUserTypeMasterNew().getUserTypeId());
					for(WorkPackageDemandProjection existingDemandProjection : existingDemandProjectionList){
						existingDemandProjection.setResourceCount(jsonWorkPackageDemandProjection.getSkillandRoleBasedresourceCount());
						existingDemandProjection.setWorkWeek(rwk);
						existingDemandProjection.setWpDemandProjectionId(null);
					}
				}
				if(existingDemandProjectionList != null && existingDemandProjectionList.size() > 0){
					workPackageDemandProjectionList.addAll(existingDemandProjectionList);
				}
			}
			
			if(jsonWorkPackageDemandProjection.getSkillandRoleBasedresourceCount() !=0){
				for(WorkPackageDemandProjection wpdp : workPackageDemandProjectionList){
					workPackageService.updateWorkPackageDemandProjection(wpdp);	
					resourceCount = wpdp.getResourceCount();
					if(wpdp != null){
						mongoDBService.addResourceDemandToMongoDB(wpdp.getWpDemandProjectionId());
					}										
				}
				user = (UserList)req.getSession().getAttribute("USER");
				UserList userObj = userListService.getUserListById(user.getUserId());
				
				remarks = "WP:"+jsonWorkPackageDemandProjection.getWorkPackageName()+"  WK:"+jsonWorkPackageDemandProjection.getWorkWeek()+"  Shift:"+jsonWorkPackageDemandProjection.getShiftName()
						+"  Role: "+jsonWorkPackageDemandProjection.getUserRoleName()+" Skill:"+jsonWorkPackageDemandProjection.getSkillName()+"  DemandedCount: "+resourceCount;
				
				eventService.addEntityChangedEvent(IDPAConstants.ENTITY_RESOURCE_DEMAND, workPackageDemandProjection.getWpDemandProjectionId(), remarks, 
						jsonWorkPackageDemandProjection.getModifiedField(), jsonWorkPackageDemandProjection.getModifiedFieldTitle(), jsonWorkPackageDemandProjection.getOldFieldValue(), jsonWorkPackageDemandProjection.getModifiedFieldValue(), userObj, remarks);
			}else{
				for(WorkPackageDemandProjection wpdp : workPackageDemandProjectionList){
					workPackageService.deleteWorkPackageDemandProjection(wpdp);	
					if(wpdp!=null && wpdp.getWpDemandProjectionId() != null ){
						mongoDBService.deleteResourceDemandFromMongoDb(wpdp.getWpDemandProjectionId());
					}
				}
			}
			
			 jTableResponse = new JTableResponse("OK",jDemandProjections,1);  
//			 jTableResponse = new JTableResponse("OK");  
		    } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Unable to update the Demand!");
	            log.error("JSON ERROR", e);
	        }
        return jTableResponse;
    }
	
	
	@RequestMapping(value="workpackage.demand.projection.skill.weekly.data.delete",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse deleteDemandProjection(HttpServletRequest req, @ModelAttribute JsonWorkPackageDemandProjection jsonWorkPackageDemandProjection,BindingResult result) {
		JTableResponse jTableResponse = null;
		if(result.hasErrors()){
			jTableResponse = new JTableResponse("ERROR","Invalid Form!"); 
		}	
		try {
			log.info("jsonWorkPackageDemandProjection-------------------- "+jsonWorkPackageDemandProjection.getWpDemandProjectionId());
			
			WorkPackageDemandProjection workPackageDemandProjection = workpackageService.getWorkPackageDemandProjectionById(jsonWorkPackageDemandProjection.getWpDemandProjectionId());
			List<WorkPackageDemandProjection> workPackageDemandProjectionList = resourceManagementService.getWorkpackageDemandProjection(workPackageDemandProjection.getWorkPackage().getWorkPackageId(), workPackageDemandProjection.getWorkShiftMaster().getShiftId(), workPackageDemandProjection.getSkill().getSkillId(), workPackageDemandProjection.getUserRole().getUserRoleId(), workPackageDemandProjection.getWorkWeek(), workPackageDemandProjection.getWorkYear(),workPackageDemandProjection.getUserTypeMasterNew().getUserTypeId());
			for(WorkPackageDemandProjection wpdp : workPackageDemandProjectionList){
				workPackageService.deleteWorkPackageDemandProjection(wpdp);	
			}
			
			 jTableResponse = new JTableResponse("OK");  
		    } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Unable to update the Demand!");
	            log.error("JSON ERROR", e);
	        }
        return jTableResponse;
    }
	
	@RequestMapping(value="workPackage.block.resources.weekly",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse getWeeklyReservedResourcesDetailsForWorkPackage(@RequestParam Integer workPackageId,@RequestParam Integer shiftId,@RequestParam Integer workWeek, @RequestParam Integer workYear, @RequestParam Integer userRoleId,@RequestParam Integer skillId,@RequestParam Integer userTypeId,@RequestParam String filter) throws Exception {
		Date date = null;
		JTableResponse jTableResponse = null;
		
		String errorMessage = "";
		boolean isError = false;
		
		List<JsonUserList> jsonAvailableResources = new ArrayList<JsonUserList>();
		List<JsonUserList> jsonBlockedResourceList = new ArrayList<JsonUserList>(0);
		List<JsonUserList> jsonCompleteList = new ArrayList<JsonUserList>();
		
		try{
			
				jsonAvailableResources = resourceManagementService.getAllUnReservedResourcesForReservationWeekly(workPackageId,shiftId,workWeek,workYear,userRoleId,skillId,filter,userTypeId);
				if(jsonAvailableResources != null && jsonAvailableResources.size()>0){
						Collections.sort(jsonAvailableResources, JsonUserList.jsonUserListComparator);
						log.info("availableResources size: "+jsonAvailableResources.size());
				}
				
				jsonBlockedResourceList = resourceManagementService.getReservedResourcesWeeklyByRoleAndSkill(workPackageId, workWeek, workYear, shiftId, userRoleId, skillId,userTypeId);
				if(jsonBlockedResourceList != null && jsonBlockedResourceList.size()>0){
					Collections.sort(jsonBlockedResourceList, JsonUserList.jsonUserListComparator);
				}
				
				if(workPackageId == -1){
					if(jsonAvailableResources != null && jsonAvailableResources.size()>0){
						jsonCompleteList.addAll(jsonAvailableResources);
					}
				}else{
					
					if(jsonAvailableResources != null && jsonAvailableResources.size()>0){
						jsonCompleteList.addAll(jsonAvailableResources);
					}
					if(jsonBlockedResourceList != null && jsonBlockedResourceList.size()>0){
						jsonCompleteList.addAll(jsonBlockedResourceList);
					}
				}
				
				jTableResponse = new JTableResponse("OK", jsonCompleteList,jsonCompleteList.size());
        } catch (Exception e) {
            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
            log.error("JSON ERROR", e);
        }	     
        return jTableResponse;
	}
	
	@RequestMapping(value="workpackage.block.resources.table.edit.for.weekly",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse blockResourcesForWorkpackageEditTable(@ModelAttribute JsonUserList jsonuser, HttpServletRequest req, @RequestParam Integer workpackageId, @RequestParam  Integer workWeek, @RequestParam  Integer workYear,@RequestParam Integer reserveShiftId,@RequestParam Integer reserveSkillId,@RequestParam Integer reserveUserRoleId, @RequestParam Long groupDemandId, @RequestParam Float demand, @RequestParam String recursiveWeeks,Integer userTypeId) {
		
		Integer reservedorUnreservedUserId = 0;
		JTableResponse jTableResponse = null;
		try {
			String errorMessage = "";
			boolean isError = false;
			reservedorUnreservedUserId = jsonuser.getUserId();
			Integer isReserved = jsonuser.getReserve();
			log.debug("inside workpackage.block.resources.for.day.shift.inline:::  reservedorUnreservedUserId::: "+jsonuser.getUserId()+"   isReserved: "+isReserved);
			Date dtResourceBlockedForDate=null;
			UserList userSession = (UserList)req.getSession().getAttribute("USER");
			Integer loggedInUserId = userSession.getUserId();
			
			Set<Integer> recursiveWeekSet = new HashSet<Integer>();
			recursiveWeekSet = resourceManagementService.getRecursiveWeeks(recursiveWeeks, recursiveWeekSet);
			if(recursiveWeekSet.size() == 0){
				recursiveWeekSet.add(workWeek);
			}
			
			List<String> errorMessages = new ArrayList<String>();
			for(Integer recursiveWeek : recursiveWeekSet){
				if(!jsonuser.getModifiedField().equalsIgnoreCase("reserve")){
					resourceManagementService.updateReservedResourcePercentage(workpackageId,recursiveWeek,workYear,reserveShiftId,reserveSkillId,jsonuser.getReservationPercentage(),jsonuser.getUserId(),reserveUserRoleId,userTypeId);
					
				}else{
					if(isReserved == 1){
						
												
						log.info("Add reservations");
						TestFactoryResourceReservation reservedResource = null;
						reservedResource = resourceManagementService.saveReservedResourceForWeekly(reservedorUnreservedUserId, recursiveWeek, workYear, workpackageId, reserveShiftId, reserveSkillId, reserveUserRoleId, loggedInUserId, groupDemandId,0,userTypeId);
						
						if(reservedResource != null){
							log.info("Add reservations completed successfully");
							jTableResponse = new JTableResponse("OK", "Resource Reserved successfully");
						}else if(reservedResource == null) {
							errorMessages.add("Unable to reserve user id - "+reservedorUnreservedUserId+" for week "+recursiveWeek);
						} 
					}else if(isReserved == 0){
						log.info("Remove reservations");
						List<TestFactoryResourceReservation> unBlockResources = resourceManagementService.removeReservationForResourceForWeekly(reservedorUnreservedUserId, recursiveWeek, workYear, workpackageId, reserveShiftId, loggedInUserId,groupDemandId,userTypeId);
						
						if (unBlockResources == null) {
							errorMessages.add("Unable to remove reservation of user id - "+reservedorUnreservedUserId+" for week "+recursiveWeek);
						} 
					}
					log.info("Resource reservation process completed");
				}
			}
			if(errorMessages != null && errorMessages.size() > 0){
				String returnErrorMessage = "";
				for(String message : errorMessages){
					returnErrorMessage += message+" | ";
				}
				returnErrorMessage = returnErrorMessage.trim().substring(0, returnErrorMessage.length() - 1);
				jTableResponse = new JTableResponse("ERROR", returnErrorMessage);
			}else if(isReserved == 1){
				jTableResponse = new JTableResponse("OK", "Resource Reserved successfully");
			}else if(isReserved == 0){
				jTableResponse = new JTableResponse("OK", "Remove Reservation of Resource Success.");
			}
        } catch (Exception e) {
        	jTableResponse = new JTableResponse("ERROR","Error Resource Reservation!");
            log.error("JSON ERROR", e);
        }		  
		 return jTableResponse;
    }
	
	@RequestMapping(value="resource.pool.resource.allocation.weekly",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse resourcePoolResourceAllocationWeekly(@RequestParam Integer labId, @RequestParam Integer resourcePoolId, @RequestParam Integer testFactoryId, @RequestParam Integer productId, @RequestParam Integer workpackageId, @RequestParam Integer allocationYear,@RequestParam Integer userId,@RequestParam Integer utilizationRange, @RequestParam String recursiveWeeks) {
		log.info("resource.pool.resource.allocation.weekly");
		JTableResponse jTableResponse = null;
		Set<Integer> recursiveWeekSet = new HashSet<Integer>();
		Integer workWeek = 0;
		try {
			if(recursiveWeeks !=null && !recursiveWeeks.trim().isEmpty()){
				recursiveWeekSet = resourceManagementService.getRecursiveWeeks(recursiveWeeks, recursiveWeekSet);
				}
				
				List<Integer> recursiveWeekList = new ArrayList<Integer>();	
				if(recursiveWeekSet.size() == 0){
					recursiveWeekSet.add(workWeek);
				}else{
					recursiveWeekList.addAll(recursiveWeekSet);
				}
			List<JsonWorkPackageWeeklyResourceReservation> jsonWorkPackageWeeklyResourceReservation = resourceManagementService.getResourceAllocationWeekly(labId, resourcePoolId, testFactoryId, productId, workpackageId, allocationYear,userId,utilizationRange,recursiveWeekList);
			jTableResponse = new JTableResponse("OK", jsonWorkPackageWeeklyResourceReservation, jsonWorkPackageWeeklyResourceReservation.size());
        } catch (Exception e) {
            jTableResponse = new JTableResponse("ERROR","Error getting Resource pool resource allocation!");
            log.error("JSON ERROR", e);	            
        }
        return jTableResponse;
    }
	
	@RequestMapping(value="resource.pool.resource.allocation.detailed.weekly",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse resourcePoolResourceAllocationDetailedWeekly(@RequestParam Integer labId, @RequestParam Integer resourcePoolId, @RequestParam Integer testFactoryId, @RequestParam Integer productId, @RequestParam Integer workpackageId, @RequestParam Integer userId, @RequestParam String recursiveWeeks, @RequestParam Integer workYear) {
		log.info("resource.pool.resource.allocation.detailed.weekly");
		JTableResponse jTableResponse = null;
		Set<Integer> recursiveWeekSet = new HashSet<Integer>();
		Integer workWeek = 0;
		try {
				if(recursiveWeeks !=null && !recursiveWeeks.trim().isEmpty()){
					recursiveWeekSet = resourceManagementService.getRecursiveWeeks(recursiveWeeks, recursiveWeekSet);
				}
				
				List<Integer> recursiveWeekList = new ArrayList<Integer>();	
				if(recursiveWeekSet.size() == 0){
					recursiveWeekSet.add(workWeek);
				}else{
					recursiveWeekList.addAll(recursiveWeekSet);
				}
			List<JsonWorkPackageWeeklyDemandProjection> jsonWorkPackageWeeklyDemandProjections = resourceManagementService.getResourceAllocationDetailedWeekly(labId, resourcePoolId, testFactoryId, productId, workpackageId, userId, recursiveWeekList, workYear);
			jTableResponse = new JTableResponse("OK", jsonWorkPackageWeeklyDemandProjections, jsonWorkPackageWeeklyDemandProjections.size());
        } catch (Exception e) {
            jTableResponse = new JTableResponse("ERROR","Error getting Resource pool resource detailed allocation!");
            log.error("JSON ERROR", e);	            
        }
        return jTableResponse;
    }
	
	@RequestMapping(value="workpackage.demand.projection.list.for.all.weeks.enagementlevel",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listWorkpackageDemandProjectionForAllWeeksEnagementLevel(HttpServletRequest req, @RequestParam int testFactoryLabId,@RequestParam int testFactoryId, @RequestParam int productId, @RequestParam int workPackageId ,@RequestParam String recursiveWeeks, @RequestParam int weekYear,@RequestParam int selectedTab) {
		log.info("workpackage.demand.projection.list.for.all.weeks");
		JTableResponse jTableResponse = null;
		Set<Integer> recursiveWeekSet = new HashSet<Integer>();
		Integer workWeek = 0;
		try { 
		log.info("selectedTab== "+selectedTab);
				
		if(recursiveWeeks !=null && !recursiveWeeks.trim().isEmpty()){
		recursiveWeekSet = resourceManagementService.getRecursiveWeeks(recursiveWeeks, recursiveWeekSet);
		}
		
		List<Integer> recursiveWeekList = new ArrayList<Integer>();	
		if(recursiveWeekSet.size() == 0){
			recursiveWeekSet.add(workWeek);
		}else{
			recursiveWeekList.addAll(recursiveWeekSet);
		}
								
				List<JsonWorkPackageWeeklyDemandProjection> jsonWorkPackageWeeklyDemandProjection = resourceManagementService.listWorkpackageWeeklyDemandProjectionEnagementLevel(testFactoryLabId,testFactoryId, productId,workPackageId,weekYear,selectedTab,recursiveWeekList);
				
				if (jsonWorkPackageWeeklyDemandProjection == null || jsonWorkPackageWeeklyDemandProjection.isEmpty()) {
					
					log.info("No Demand projections found. Or workshifts have not been defined for the TestFactory : "+workPackageId  );
					jTableResponse = new JTableResponse("ERROR", "No Demand projections found. Or workshifts have not been defined for the TestFactory");
				} else {					
					jTableResponse = new JTableResponse("OK", jsonWorkPackageWeeklyDemandProjection, jsonWorkPackageWeeklyDemandProjection.size());
				}
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error getting Resource Demand for Workpackage!");
	            log.error("JSON ERROR", e);	            
	        }
        return jTableResponse;
    }
	
	
	@RequestMapping(value="user.skills.import", method=RequestMethod.POST ,produces="text/plain" )
	public @ResponseBody JTableResponse skillImport(HttpServletRequest request) {
		log.info("Uploading  User Skills");
		JTableResponse jTableResponse;
		try {
			
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			CommonsMultipartFile multipartFile = null;
			String fileName = "";
			InputStream is=null;
			Iterator<String> iterator = multipartRequest.getFileNames();
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				multipartFile = (CommonsMultipartFile) multipartRequest.getFile(key);
				fileName = multipartFile.getOriginalFilename();
				is=multipartFile.getInputStream();
			}
			
			String isImportComplete = " ";
			isImportComplete = excelTestDataIntegrator.importSkills(request,fileName,is);
			fileName = isImportComplete.split(";")[1];
			isImportComplete = isImportComplete.split(";")[0];
			
			if(isImportComplete != null){
				log.info("Import skill Completed."+" "+isImportComplete);
				jTableResponse = new JTableResponse("Ok","Import skill Completed."+" "+isImportComplete, fileName);
			} else{
				log.info("Import completed");
				jTableResponse = new JTableResponse("Ok","Import completed."+" "+isImportComplete, fileName);
			}
		} catch (Exception e) {
			jTableResponse = new JTableResponse("Error Importing Skill");
			log.error("JSON ERROR Importing Skill", e);
		}
		return jTableResponse;
	}
	
	@RequestMapping(value="resource.demand.import", method=RequestMethod.POST ,produces="text/plain" )
	public @ResponseBody JTableResponse resourceDemandImport(HttpServletRequest request, @RequestParam Integer workPackageId, @RequestParam Integer shiftId,@RequestParam String demandUploadPermission) {
		log.debug("resource.demand.import");
		JTableResponse jTableResponse;
		try {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			CommonsMultipartFile multipartFile = null;
			String fileName = "";
			InputStream is=null;
			Iterator<String> iterator = multipartRequest.getFileNames();
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				multipartFile = (CommonsMultipartFile) multipartRequest.getFile(key);
				fileName = multipartFile.getOriginalFilename();
				is=multipartFile.getInputStream();
			}
			log.info("fileName-"+fileName);
			String isImportComplete = "";
			
			isImportComplete = userDataIntegrator.importResourceDemandWeekly(request,fileName,is, workPackageId, shiftId,demandUploadPermission);			
			fileName = isImportComplete.split(";")[1];
			isImportComplete = isImportComplete.split(";")[0];
			if(isImportComplete != null){
				jTableResponse = new JTableResponse("Ok","Import  resource Demand Completed."+" "+isImportComplete,fileName);
			} else{
				jTableResponse = new JTableResponse("Ok","Import completed."+" "+isImportComplete,fileName);
			}
		} catch (Exception e) {
			jTableResponse = new JTableResponse("Error in Import");
			log.error("JSON ERROR in resourceDemandImport : " , e);
		}
		return jTableResponse;
	}
	
	@RequestMapping(value="mapped.resources.based.on.resourcepool", method=RequestMethod.POST,produces="application/json")
	public @ResponseBody JTableResponse getResourcesMappingDates(HttpServletRequest request, @RequestParam Integer userId,  @RequestParam Integer resourcePoolId, @RequestParam Integer year){
		JTableResponse jTableResponse;
		try{
			
			List <JsonUserResourcePoolMapping>jsonMappings = new ArrayList<JsonUserResourcePoolMapping>();
			
			Calendar startCalendar = Calendar.getInstance();
			startCalendar.set(Calendar.DAY_OF_MONTH, 1);
			startCalendar.set(Calendar.MONTH, Calendar.JANUARY);
			startCalendar.set(Calendar.YEAR, year);
			startCalendar.set(Calendar.HOUR_OF_DAY, 0);
			startCalendar.set(Calendar.MINUTE, 0);
			startCalendar.set(Calendar.SECOND, 0);
			
			
			Calendar endCalendar = Calendar.getInstance();
			endCalendar.set(Calendar.DAY_OF_MONTH, 31);
			endCalendar.set(Calendar.MONTH, Calendar.DECEMBER);
			endCalendar.set(Calendar.YEAR, year); 
			endCalendar.set(Calendar.HOUR_OF_DAY, 23);
			endCalendar.set(Calendar.MINUTE, 59);
			endCalendar.set(Calendar.SECOND, 59);
			
			Date fromDate = startCalendar.getTime(); 
			Date toDate = endCalendar.getTime(); 
			
			
			List<UserResourcePoolMapping>userResourcePoolMappings = userListService.getUserResourcePoolMappingByUserIdAndResourcePoolId(userId, resourcePoolId, fromDate,toDate);
			
			for(UserResourcePoolMapping mapping :userResourcePoolMappings){
					jsonMappings.add(new JsonUserResourcePoolMapping(mapping));
			}
			
			
			for(JsonUserResourcePoolMapping dateFormat : jsonMappings ){
				dateFormat.setFromDate(DateUtility.sdfDateformatWithOutTime(DateUtility.getddmmyyyytoyyyymmddwithSec(dateFormat.getFromDate())));
				dateFormat.setToDate(DateUtility.sdfDateformatWithOutTime(DateUtility.getddmmyyyytoyyyymmddwithSec(dateFormat.getToDate())));
			}
				jTableResponse = new JTableResponse("OK", jsonMappings, jsonMappings.size());
			
			
		}catch(Exception ex){
			jTableResponse = new JTableResponse("ERROR","Error while fetching records!");
			log.error("Unable to get Dates ", ex);
		}
		
		return jTableResponse;
		
	}
	
	public void updateSkill(Skill skill, Integer oldParentSkillId, Integer newParentSkillId) {
		
		Skill oldParentSkill = null;
		if (oldParentSkillId == null || oldParentSkillId < 0 ) {
			//This was a root skill
			oldParentSkill = skillService.getRootSkill();
		} else {
			oldParentSkill = skillService.getBySkillId(oldParentSkillId);
		}
		Skill newParentSkill = null;
		if (newParentSkillId == null || newParentSkillId < 0) {
			//This is becoming a root skill
			newParentSkill = skillService.getRootSkill();
		} else {
			newParentSkill = skillService.getBySkillId(newParentSkillId);
		}
		updateSkillParent(skill, oldParentSkill, newParentSkill);
	}
	
	public void updateSkillParent(Skill skill, Skill oldParentSkill, Skill newParentSkill) {
		
		log.debug("updating Skill parent");
		String ENTITY_TABLE_NAME = "skill";

		try {
			
			log.info("Change Skill parent for " + skill.getSkillName() + " from " + oldParentSkill.getSkillName() + " to " + newParentSkill.getSkillName());
			//Refresh the skill from DB so that all index related fileds are available
			skill = skillService.getBySkillId(skill.getSkillId());
			log.info("Updated Skill Indexes : " + skill.getLeftIndex() + " : " + skill.getRightIndex());
			//Get the child nodes hierarchy in a list. The list will be ordered top to bottom, left to right
			List<Skill> childSkillsHierarchy = skillService.listChildNodesInHierarchyinLayers(skill);
			log.info("Updated Node : " + skill.getSkillName());
			log.info("Child Nodes Count : " + childSkillsHierarchy.size());
			if (childSkillsHierarchy == null || childSkillsHierarchy.isEmpty()) {

				log.info("Updated Skill Indexes Before Delete : " + skill.getLeftIndex() + " : " + skill.getRightIndex());
				log.info("Parent Skill Indexes Before Delete : " + newParentSkill.getLeftIndex() + " : " + newParentSkill.getRightIndex());
				hierarchicalEntitiesService.updateHierarchyIndexForDelete(ENTITY_TABLE_NAME, skill.getLeftIndex(), skill.getRightIndex());
				
				//Refresh the Parent so that its indices are refreshed
				newParentSkill = skillService.getBySkillId(newParentSkill.getSkillId());
				log.info("Parent Skill Indexes After Delete : " + newParentSkill.getLeftIndex() + " : " + newParentSkill.getRightIndex());
				log.info("Updated Skill Indexes After Delete : " + skill.getLeftIndex() + " : " + skill.getRightIndex());
				//Add the updated skill to its new parent hierarchy
				hierarchicalEntitiesService.updateHierarchyIndexForNew(ENTITY_TABLE_NAME, newParentSkill.getRightIndex());
				newParentSkill = skillService.getBySkillId(newParentSkill.getSkillId());

				log.info("Parent Skill Indexes After Add : " + newParentSkill.getLeftIndex() + " : " + newParentSkill.getRightIndex());
				log.info("Updated Skill Indexes After Add : " + skill.getLeftIndex() + " : " + skill.getRightIndex());
				
				//Update the skill's new parent in database
				skill.setParentSkill(newParentSkill);
				skill.setLeftIndex(newParentSkill.getRightIndex() - 2);
				skill.setRightIndex(newParentSkill.getRightIndex() - 1);
				skillService.update(skill);
				skill = skillService.getBySkillId(skill.getSkillId());
				log.info("Updated Skill Indexes After Add : " + skill.getLeftIndex() + " : " + skill.getRightIndex());
				return;
			}
			
			
			for (Skill skill1 : childSkillsHierarchy) {
				log.info("Skill Hierarchy Layered sorted : " + skill1.getLeftIndex() + " : " + skill1.getSkillName() + " : " + skill1.getRightIndex());
			}
			
			//Remove all the child nodes, starting from leaf nodes and then progress upwards
			int childNodesCount = childSkillsHierarchy.size();
			for (int i = childNodesCount-1; i >= 0; i--) {
				
				Skill childSkill = childSkillsHierarchy.get(i);
				childSkill = skillService.getBySkillId(childSkill.getSkillId());
				oldParentSkill = skillService.getBySkillId(oldParentSkill.getSkillId());
				log.info("Old Parent before Removing child Node : " + oldParentSkill.getLeftIndex() + " : " + oldParentSkill.getSkillName() + " : " + oldParentSkill.getRightIndex());
				hierarchicalEntitiesService.updateHierarchyIndexForDelete(ENTITY_TABLE_NAME, childSkill.getLeftIndex(), childSkill.getRightIndex());
				log.info("Removed child Node : " + childSkill.getLeftIndex() + " : " + childSkill.getSkillName() + " : " + childSkill.getRightIndex());
				oldParentSkill = skillService.getBySkillId(oldParentSkill.getSkillId());
				log.info("Old Parent after Removing child Node : " + oldParentSkill.getLeftIndex() + " : " + oldParentSkill.getSkillName() + " : " + oldParentSkill.getRightIndex());
			}
			//Remove the updated node. With this, the hierarchy now has no indexes for the updated skill and its n level child nodes 
			skill = skillService.getBySkillId(skill.getSkillId());
			hierarchicalEntitiesService.updateHierarchyIndexForDelete(ENTITY_TABLE_NAME, skill.getLeftIndex(), skill.getRightIndex());
			log.info("Removed updated Node : " + skill.getLeftIndex() + " : " + skill.getSkillName() + " : " + skill.getRightIndex());
			oldParentSkill = skillService.getBySkillId(oldParentSkill.getSkillId());
			log.info("Old Parent after updated Node : " + oldParentSkill.getLeftIndex() + " : " + oldParentSkill.getSkillName() + " : " + oldParentSkill.getRightIndex());
			log.info("Finished removing Hierarchy from old parent");
			
			//Update the skill's new parent in database

			newParentSkill = skillService.getBySkillId(newParentSkill.getSkillId());
			log.info("New Parent before adding updated Node : " + newParentSkill.getLeftIndex() + " : " + newParentSkill.getSkillName() + " : " + newParentSkill.getRightIndex());
			hierarchicalEntitiesService.updateHierarchyIndexForNew(ENTITY_TABLE_NAME, newParentSkill.getRightIndex());
			newParentSkill = skillService.getBySkillId(newParentSkill.getSkillId());
			skill.setParentSkill(newParentSkill);
			skill.setLeftIndex(newParentSkill.getRightIndex() - 2);
			skill.setRightIndex(newParentSkill.getRightIndex() - 1);
			skillService.update(skill);
			skill = skillService.getBySkillId(skill.getSkillId());
			log.info("Updated Skill Indexes After Add : " + skill.getLeftIndex() + " : " + skill.getRightIndex());
			log.info("Added updated Node : " + skill.getLeftIndex() + " : " + skill.getSkillName() + " : " + skill.getRightIndex());
			newParentSkill = skillService.getBySkillId(newParentSkill.getSkillId());
			log.info("New Parent after adding updated Node : " + newParentSkill.getLeftIndex() + " : " + newParentSkill.getSkillName() + " : " + newParentSkill.getRightIndex());
			oldParentSkill = skillService.getBySkillId(oldParentSkill.getSkillId());
			log.info("Old Parent after adding updated Node : " + oldParentSkill.getLeftIndex() + " : " + oldParentSkill.getSkillName() + " : " + oldParentSkill.getRightIndex());
			
			//Add the child nodes back to the hierarchy. Their parents have not changed
			for (int i = 0; i < childNodesCount; i++) {
				
				Skill childSkill = childSkillsHierarchy.get(i);
				//Reload parent from DB, so that it contains the updated indices
				Skill tempParentSkill = skillService.getBySkillId(childSkill.getParentSkill().getSkillId());
				log.info("Child node parent : " + tempParentSkill.getLeftIndex() + " : " + tempParentSkill.getSkillName() + " : " + tempParentSkill.getRightIndex());
				hierarchicalEntitiesService.updateHierarchyIndexForNew(ENTITY_TABLE_NAME, tempParentSkill.getRightIndex());
				tempParentSkill = skillService.getBySkillId(tempParentSkill.getSkillId());
				childSkill.setLeftIndex(tempParentSkill.getRightIndex() - 2);
				childSkill.setRightIndex(tempParentSkill.getRightIndex() - 1);
				skillService.update(childSkill);
				log.info("Added child Node : " + childSkill.getLeftIndex() + " : " + childSkill.getSkillName() + " : " + childSkill.getRightIndex());
				newParentSkill = skillService.getBySkillId(newParentSkill.getSkillId());
				log.info("New Parent after adding child Node : " + newParentSkill.getLeftIndex() + " : " + newParentSkill.getSkillName() + " : " + newParentSkill.getRightIndex());
				oldParentSkill = skillService.getBySkillId(oldParentSkill.getSkillId());
				log.info("Old Parent after adding child Node : " + oldParentSkill.getLeftIndex() + " : " + oldParentSkill.getSkillName() + " : " + oldParentSkill.getRightIndex());
			}
			log.debug("Parent Skill Updated Successfully");
		} catch (RuntimeException re) {
			log.error("update failed", re);
		}		
	}
	
	@RequestMapping(value="administration.skill.update.parent",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse updateFeatureParentInHierarchy(HttpServletRequest request,@RequestParam Integer skillId, @RequestParam Integer oldParentSkillId, @RequestParam Integer newParentSkillId) {

		log.debug("Updating Skill");
		JTableResponse jTableResponse;
		try {
			Skill skill = skillService.getBySkillId(skillId);
			updateSkill(skill, oldParentSkillId, newParentSkillId);
			jTableResponse = new JTableResponse("OK", "Updated Skill parent");
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error updating Skill parent!");
			log.error("JSON ERROR updating Skill parent", e);
		}
		return jTableResponse;
	}
	
	@RequestMapping(value="workpackage.copy.resources.for.specified.week.range", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody JTableResponse reserveResourcesForSpecifiedWeekRange(HttpServletRequest request, @RequestParam String blockedResourcesData, @RequestParam Integer workPackageId, @RequestParam Integer shiftId,@RequestParam Integer skillId,@RequestParam Integer workYear,
																					@RequestParam Integer userTypeId,@RequestParam Long groupDemandId, @RequestParam String startDate, @RequestParam String endDate) {
		JTableResponse jTableResponse=null;		
		List<HashMap<String, Integer>> usersMapList = new ArrayList<HashMap<String, Integer>>();
		List<String> errorMessages = new ArrayList<String>();
		Date demandStartDate=null;
		Date demandEndDate=null;
		
		try {
			if(blockedResourcesData != null && !blockedResourcesData.trim().isEmpty()){	
				Set<Integer> recursiveWeekSet = new HashSet<Integer>();
				demandStartDate=DateUtility.dateformatWithOutTime(startDate);				
				demandEndDate=DateUtility.dateformatWithOutTime(endDate);
				
				Integer demandStartWeek=DateUtility.getWeekNumberOfDateOnAYear(demandStartDate);
				Integer demandEndWeek=DateUtility.getWeekNumberOfDateOnAYear(demandEndDate);				
				String recursiveWeeks=demandStartWeek+"-"+demandEndWeek;
				
				recursiveWeekSet = resourceManagementService.getRecursiveWeeks(recursiveWeeks, recursiveWeekSet);
				
				String[] blockedResourceDataArr = blockedResourcesData.split("~");
				for(String blockedUsers : blockedResourceDataArr){
					String[] diffIds = blockedUsers.split(";");
					HashMap<String, Integer> usersMap = new HashMap<String, Integer>();
					for(String idPairs : diffIds){
						String[] ids = idPairs.split(":");
						usersMap.put(ids[0], Integer.parseInt(ids[1]));						
					}
					usersMapList.add(usersMap);
				}
				if(!usersMapList.isEmpty() && usersMapList.size() > 0){
					for(Integer recursiveWeek : recursiveWeekSet){
						for(HashMap<String, Integer> blockedUserMap : usersMapList){
							List<TestFactoryResourceReservation> listOfResource =	resourceManagementService.getReservedResourcesWeeklyByUserIdForUpdateOrAdd(workPackageId,recursiveWeek,workYear,shiftId,skillId,blockedUserMap.get("UserId"),blockedUserMap.get("RoleId"),userTypeId);
							TestFactoryResourceReservation testFactReservedResource = null;
							UserList userList = (UserList)request.getSession().getAttribute("USER");
							Integer loggedInUserId = userList.getUserId();						
							if(listOfResource != null && listOfResource.size() >0){
								resourceManagementService.updateReservedResourcePercentage(workPackageId,recursiveWeek,workYear,shiftId,skillId,blockedUserMap.get("ReservePercent"),blockedUserMap.get("UserId"),blockedUserMap.get("RoleId"),userTypeId);
								jTableResponse = new JTableResponse("OK", "Resource(s) Reserved successfully");
								for(TestFactoryResourceReservation reservation:listOfResource){
									if(reservation != null){
										mongoDBService.addReseveredResourceToMongoDB(reservation.getResourceReservationId());
									}
								}
								
							}else{
								testFactReservedResource = resourceManagementService.saveReservedResourceForWeekly(blockedUserMap.get("UserId"), recursiveWeek, workYear,workPackageId, shiftId,skillId,blockedUserMap.get("RoleId"), loggedInUserId,groupDemandId,blockedUserMap.get("ReservePercent"),userTypeId);
								if(testFactReservedResource != null){
									log.info("Add reservations completed successfully");
									jTableResponse = new JTableResponse("OK", "Resource(s) Reserved successfully");
								}else if(testFactReservedResource == null) {
									UserList usr = userListService.getUserListById(blockedUserMap.get("UserId"));
									errorMessages.add(""+usr.getLoginId()+" not available for week "+recursiveWeek);
								} 
							}
						 }
				   }
				}
			}
			if(errorMessages != null && errorMessages.size() > 0){
				String returnErrorMessage = "";
				for(String message : errorMessages){
					returnErrorMessage += message+" | ";
				}
				returnErrorMessage = returnErrorMessage.trim().substring(0, returnErrorMessage.length() - 1);
				jTableResponse = new JTableResponse("ERROR", returnErrorMessage);
			}
		} catch (Exception e) {
			log.error("JSON ERROR while booking resources for specified week range", e);
		}
		return jTableResponse;
	}
	
}
