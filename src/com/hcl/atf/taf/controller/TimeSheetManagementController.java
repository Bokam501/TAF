package com.hcl.atf.taf.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.ActualShift;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.ResourceShiftCheckIn;
import com.hcl.atf.taf.model.TimeSheetActivityType;
import com.hcl.atf.taf.model.TimeSheetEntryMaster;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.UserRoleMaster;
import com.hcl.atf.taf.model.WorkPackage;
import com.hcl.atf.taf.model.WorkShiftMaster;
import com.hcl.atf.taf.model.dto.ShiftHoursValidationDTO;
import com.hcl.atf.taf.model.dto.TimeSheetDaySummaryDTO;
import com.hcl.atf.taf.model.json.JsonResourceShiftCheckin;
import com.hcl.atf.taf.model.json.JsonTimeSheetDaySummary;
import com.hcl.atf.taf.model.json.JsonTimeSheetEntryMaster;
import com.hcl.atf.taf.model.json.jtable.JTableResponse;
import com.hcl.atf.taf.model.json.jtable.JTableResponseOptions;
import com.hcl.atf.taf.model.json.jtable.JTableSingleResponse;
import com.hcl.atf.taf.service.ProductListService;
import com.hcl.atf.taf.service.TestFactoryManagementService;
import com.hcl.atf.taf.service.TimeSheetManagementService;
import com.hcl.atf.taf.service.UserListService;
import com.hcl.atf.taf.service.WorkPackageService;
@Controller
public class TimeSheetManagementController {

	private static final Log log = LogFactory.getLog(TimeSheetManagementController.class);
	
	
	@Autowired
	private TimeSheetManagementService timeSheetManagementService;
	
	@Autowired
	private WorkPackageService workPackageService;
	
	@Autowired
	private UserListService userListService;
	
	@Autowired
	private ProductListService productListService;
	
	@Autowired
	private TestFactoryManagementService testFactoryManagementService;
	
	@RequestMapping(value="timesheet.entries.list",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse listTimeSheetEntries(HttpServletRequest req, @RequestParam int userId) {
			JTableResponse jTableResponse = null;
			try {
					if(userId<=0){
						userId=1;
					}
					List<TimeSheetEntryMaster> timeSheetEntryMaster = timeSheetManagementService.listTimeSheetEntries(userId);
					List<JsonTimeSheetEntryMaster> jsonTimeSheetEntryMaster = new ArrayList<JsonTimeSheetEntryMaster>();					
					for(TimeSheetEntryMaster timeSheetEntry: timeSheetEntryMaster){						
						jsonTimeSheetEntryMaster.add(new JsonTimeSheetEntryMaster(timeSheetEntry));
						
					}
					jTableResponse = new JTableResponse("OK", jsonTimeSheetEntryMaster, timeSheetManagementService.getTotalRecordsOfTimeSheetEntryByUserId(userId));
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
		            log.error("JSON ERROR", e);	            
		        }
	        return jTableResponse;
	    }
		public Date getConvertedTimeEntryDate(String jsondate, Date currentDate){			
			String convertedTimeEntryDate="";
				String temp[]=	jsondate.split("-");
				int yearLength=temp[2].length();
				if(yearLength==4){
			for(int i=temp.length-1;i>=0;i--){
				
				if(i!=0){
					convertedTimeEntryDate=convertedTimeEntryDate+temp[i]+"-";
				}else{
					convertedTimeEntryDate=convertedTimeEntryDate+temp[i];
				}
			}
				}
				else{
					convertedTimeEntryDate=jsondate;
				}
			if(convertedTimeEntryDate != null || !(convertedTimeEntryDate.trim().isEmpty())) {
				currentDate= DateUtility.dateFormatWithOutSeconds(convertedTimeEntryDate);
			}else{
				currentDate= DateUtility.getCurrentTime();
			}	
			return currentDate;
		}
		
		@RequestMapping(value="common.list.reservedcheckin.list",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponseOptions listreservedCheckinId(@RequestParam int userId, @RequestParam String checkInDate) {
			log.info("inside common.list.usertypes.list--"+checkInDate);
			JTableResponseOptions jTableResponseOptions = null;		 
		try {	
			List<JsonResourceShiftCheckin> jsonResourceShiftCheckin =  new ArrayList<JsonResourceShiftCheckin>();
			if(!checkInDate.equalsIgnoreCase("0") && !checkInDate.equalsIgnoreCase("")){
				List<ResourceShiftCheckIn> resourceShiftCheckIn=timeSheetManagementService.getResourceShiftCheckInByCheckinDate(userId, checkInDate);
				
				
					if(resourceShiftCheckIn != null){
						for(ResourceShiftCheckIn resShiftChk: resourceShiftCheckIn){
							jsonResourceShiftCheckin.add(new JsonResourceShiftCheckin(resShiftChk));
						}	
						log.info("jsonResourceShiftCheckin - For date : "+checkInDate+"---"+jsonResourceShiftCheckin.size());
						jTableResponseOptions = new JTableResponseOptions("OK", jsonResourceShiftCheckin,true);   
					} 	
			}else{
				//do nothing
				jsonResourceShiftCheckin.add(null);
				jTableResponseOptions = new JTableResponseOptions("OK", jsonResourceShiftCheckin,true);
			}
			     
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }	
		
	    return jTableResponseOptions;
	    }
		
		@RequestMapping(value="timesheet.entry.add",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableSingleResponse addTimeSheetEntry(HttpServletRequest req, @ModelAttribute JsonTimeSheetEntryMaster jsonTimeSheetEntryMaster, BindingResult result) {
			JTableSingleResponse jTableSingleResponse;
			if(result.hasErrors()){
				jTableSingleResponse  = new JTableSingleResponse("ERROR","Invalid form!"); 
			}
			
			 String errmsg = null;
			 int productId=0;
			 List<ProductMaster> productList = null;
			 ResourceShiftCheckIn resCheckIn = null;
			 WorkShiftMaster shift = null;
			 int rescheckId =0;
			try {   
					
					TimeSheetEntryMaster timeSheetEntryMaster= jsonTimeSheetEntryMaster.getTimeSheetEntryMaster();
					TimeSheetEntryMaster upDatedTimeSheetEntryMaster = null;						
					if(jsonTimeSheetEntryMaster.getResourceShiftCheckInId() != null){
						rescheckId = jsonTimeSheetEntryMaster.getResourceShiftCheckInId();
						productList=timeSheetManagementService.getProductByResourceShiftCheckInId(rescheckId);
						if(productList != null && productList.size() !=0){
							timeSheetEntryMaster.setProduct(productList.get(0));
						}	
						resCheckIn = timeSheetManagementService.getByresourceShiftCheckInId(rescheckId);
						resCheckIn.getActualShift().getShift();
						
					}else if(jsonTimeSheetEntryMaster.getProductId() != null){
						ProductMaster product = productListService.getProductDetailsById(jsonTimeSheetEntryMaster.getProductId());
						timeSheetEntryMaster.setProduct(product);		
					}
					
					Date resourceShiftCheckInCreatedDate = null;
					if(jsonTimeSheetEntryMaster != null){
						resourceShiftCheckInCreatedDate = DateUtility.dateFormatWithOutSeconds(jsonTimeSheetEntryMaster.getCreatedDate());
						log.info("CREATED DATE:::::::::::::: " +resourceShiftCheckInCreatedDate);
						if(resourceShiftCheckInCreatedDate != null){
							timeSheetEntryMaster.setDate(resourceShiftCheckInCreatedDate);
						}
					}
					
					if(jsonTimeSheetEntryMaster.getRoleId() != null){
						UserRoleMaster userRoleMaster=new UserRoleMaster();
						userRoleMaster.setUserRoleId(jsonTimeSheetEntryMaster.getRoleId());
						timeSheetEntryMaster.setRole(userRoleMaster);		
					}
					
					
					if(jsonTimeSheetEntryMaster.getShiftName() != null){
						shift = workPackageService.getWorkShiftByName(jsonTimeSheetEntryMaster.getShiftName());	
					}else if(jsonTimeSheetEntryMaster.getResourceShiftCheckInId() != null){
						rescheckId = jsonTimeSheetEntryMaster.getResourceShiftCheckInId();
						resCheckIn = timeSheetManagementService.getByresourceShiftCheckInId(rescheckId);
						shift = resCheckIn.getActualShift().getShift();
					}
					
					log.info("timeSheetEntryMaster Role Id:-->"+timeSheetEntryMaster.getRole().getUserRoleId());
					
					WorkPackage workPackage  = workPackageService.getWorkPackageById(jsonTimeSheetEntryMaster.getWorkPackageId());
					timeSheetEntryMaster.setWorkPackage(workPackage);		
					
					log.info("Activity Type Id: -->"+jsonTimeSheetEntryMaster.getActivityTypeId());
					TimeSheetActivityType activityType = timeSheetManagementService.getTimeSheetActivityTypeById(jsonTimeSheetEntryMaster.getActivityTypeId());
					timeSheetEntryMaster.setActivityType(activityType);
					
					if(jsonTimeSheetEntryMaster.getResourceShiftCheckInId() != null){
						ResourceShiftCheckIn resourceShiftCheckIn = null; //write code from Service
						timeSheetEntryMaster.setResourceShiftCheckIn(resourceShiftCheckIn);
					}
					if(jsonTimeSheetEntryMaster.getResourceShiftCheckInId()!=null && jsonTimeSheetEntryMaster.getResourceShiftCheckInId()!=null && !jsonTimeSheetEntryMaster.getResourceShiftCheckInId().equals("0")){				
						timeSheetEntryMaster.setResourceShiftCheckIn(timeSheetManagementService.getByresourceShiftCheckInId(jsonTimeSheetEntryMaster.getResourceShiftCheckInId()));
					}
					
								
					
					UserList user = (UserList) req.getSession().getAttribute("USER");
					Date currentDate = new Date(System.currentTimeMillis());
					Date actualShiftWorkDate = new Date(System.currentTimeMillis());
					String jsondate = jsonTimeSheetEntryMaster.getCreatedDate();
					actualShiftWorkDate = DateUtility.dateFormatWithOutSeconds(jsondate);					
				    log.info("json date ----"+jsondate);			    
					currentDate = getConvertedTimeEntryDate(jsondate, currentDate);					
				    
					Integer shiftId = shift.getShiftId();
					String addorupdate ="add";
					ShiftHoursValidationDTO validShifthours = null;
					ResourceShiftCheckIn resourceShiftCheckInObj = timeSheetManagementService.getByresourceShiftCheckInId(jsonTimeSheetEntryMaster.getResourceShiftCheckInId());
					if(resourceShiftCheckInObj != null && resourceShiftCheckInObj.getCheckOut() != null){
						validShifthours = isShiftCheckInHoursExceeded(resourceShiftCheckInObj,jsondate, user, timeSheetEntryMaster,"add");
					}else{
						validShifthours = isValidHoursForTimeSheet(jsondate, shiftId, user, currentDate, timeSheetEntryMaster, shift, addorupdate);
					}
					if(validShifthours != null){
						if(validShifthours.getHoursWithinLimit() != null && validShifthours.getHoursWithinLimit().equalsIgnoreCase("Yes")){
							upDatedTimeSheetEntryMaster = timeSheetManagementService.addTimeSheetEntry(timeSheetEntryMaster, user, shift); 
							 jTableSingleResponse = new JTableSingleResponse("OK",new JsonTimeSheetEntryMaster(upDatedTimeSheetEntryMaster));
						}else if(validShifthours.getExceededResourceShiftCheckInHours()  != null && validShifthours.getExceededResourceShiftCheckInHours().equalsIgnoreCase("Yes")){
							jTableSingleResponse = new JTableSingleResponse("ERROR",validShifthours.getErrorMsg());	
						}else if(validShifthours.getExceededShiftHours()  != null && validShifthours.getExceededShiftHours().equalsIgnoreCase("Yes")){
							jTableSingleResponse = new JTableSingleResponse("ERROR",validShifthours.getErrorMsg());	
						}else if(validShifthours.getExceededTotalHours()  != null && validShifthours.getExceededTotalHours().equalsIgnoreCase("Yes")){
							jTableSingleResponse = new JTableSingleResponse("ERROR",validShifthours.getErrorMsg());	
						}else{
							 jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding record!");	
						}						
					}else{
						jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding record!");
					}				
		        } catch (Exception e) {
		        	 errmsg="";
		            jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding record!");
		            log.error("JSON ERROR", e);
		        }		        
	        return jTableSingleResponse;
	    }
		
		public ShiftHoursValidationDTO isShiftCheckInHoursExceeded(ResourceShiftCheckIn resourceShiftCheckInObj, String jsondate, UserList user, TimeSheetEntryMaster timeSheetEntryMaster, String addOrUpdate){
			ShiftHoursValidationDTO validationDTO = new ShiftHoursValidationDTO();
			String errmsg = null;
			String exceededResourceShiftCheckInHours = "";
			String hoursWithinLimit = "";
			Integer timeSheetEntryMasterId = 0;
			if(resourceShiftCheckInObj != null){
				List<TimeSheetEntryMaster> listOfTimeSheetEntries = timeSheetManagementService.getTimeSheetEntriesOfResourceShiftCheckIn(resourceShiftCheckInObj.getResourceShiftCheckInId());
				if(resourceShiftCheckInObj.getCheckOut() != null){
					long checkInCheckOutDurationInMinutes = DateUtility.DateDifferenceInMinutes(resourceShiftCheckInObj.getCheckIn(), resourceShiftCheckInObj.getCheckOut());
					timeSheetEntryMasterId = timeSheetEntryMaster.getTimeSheetEntryId();
					if(listOfTimeSheetEntries != null && listOfTimeSheetEntries.size()>0){
						Integer totalTimeSheetDuration = 0;
						for (TimeSheetEntryMaster timeSheetEntryMaster2 : listOfTimeSheetEntries) {
							if(timeSheetEntryMaster2.getTimeSheetEntryId() != timeSheetEntryMasterId){
								Integer timeSheetDurationInMins = DateUtility.convertTimeInMinutes(timeSheetEntryMaster2.getHours(), timeSheetEntryMaster2.getMins());
								totalTimeSheetDuration = totalTimeSheetDuration+timeSheetDurationInMins;
							}
						}
						Integer timeSheetDurationInMins = DateUtility.convertTimeInMinutes(0, totalTimeSheetDuration);
						Integer currentTimeEntry = 0;
						Integer totalTimeDuration = 0;
						currentTimeEntry = DateUtility.convertTimeInMinutes(timeSheetEntryMaster.getHours(), timeSheetEntryMaster.getMins());
						totalTimeDuration = timeSheetDurationInMins+currentTimeEntry;
						
						if(totalTimeDuration >  checkInCheckOutDurationInMinutes){
							 exceededResourceShiftCheckInHours = "Yes";
							 validationDTO.setExceededShiftHours(exceededResourceShiftCheckInHours);
							 errmsg= "You are exceeding your shift check-in duration";
							 validationDTO.setErrorMsg(errmsg);
						}else{
							 hoursWithinLimit = "Yes";
							 validationDTO.setHoursWithinLimit(hoursWithinLimit);
						}
					}else{
						if(timeSheetEntryMaster != null){
							Integer timeSheetDurationInMins = DateUtility.convertTimeInMinutes(timeSheetEntryMaster.getHours(), timeSheetEntryMaster.getMins());
							if(timeSheetDurationInMins >  checkInCheckOutDurationInMinutes){
								 exceededResourceShiftCheckInHours = "Yes";
								 validationDTO.setExceededShiftHours(exceededResourceShiftCheckInHours);
								 errmsg= "You are exceeding your shift check-in duration";
								 validationDTO.setErrorMsg(errmsg);
							}else{
								 hoursWithinLimit = "Yes";
								 validationDTO.setHoursWithinLimit(hoursWithinLimit);
							}
						}
					}
				}
			}
			return validationDTO;
		}
		
		public ShiftHoursValidationDTO isValidHoursForTimeSheet(String jsondate, Integer shiftId, UserList user, Date currentDate, TimeSheetEntryMaster timeSheetEntryMaster, WorkShiftMaster shift, String addorupdate){
			Date actualShiftWorkDate = new Date(System.currentTimeMillis());			
			actualShiftWorkDate =DateUtility.dateFormatWithOutSeconds(jsondate);
			
			List<TimeSheetDaySummaryDTO> actualShiftBasedWorkingHoursDTO = new ArrayList<TimeSheetDaySummaryDTO>();
			List<TimeSheetDaySummaryDTO> workShiftBasedWorkingHoursDTO = new ArrayList<TimeSheetDaySummaryDTO>();
			List<TimeSheetDaySummaryDTO> shiftBasedWorkingHoursDTO = new ArrayList<TimeSheetDaySummaryDTO>();
			TimeSheetEntryMaster upDatedTimeSheetEntryMaster = null;	
			//Get Mandatory Shift based Working Hours from TestFactoryActualShift					 
			 actualShiftBasedWorkingHoursDTO = timeSheetManagementService.mandatoryHoursofActualShift(shiftId, currentDate);
			 if(actualShiftBasedWorkingHoursDTO != null && actualShiftBasedWorkingHoursDTO.size() != 0){
				 shiftBasedWorkingHoursDTO = actualShiftBasedWorkingHoursDTO;
				 log.info("Cal from Actual Shift Hours");
			 }else{
					//Get Mandatory Shift based Working Hours from WorkShiftMaster
				 workShiftBasedWorkingHoursDTO = timeSheetManagementService.mandatoryHoursofShifts(shiftId);
				 shiftBasedWorkingHoursDTO = workShiftBasedWorkingHoursDTO;
				 log.info("Cal from Work Shift Hours");
			 }
			
			 //Calculate  Mandatory Shift based Working Hours into minutes.
			 int shiftBasedWorkingHours = 0;
			 if(shiftBasedWorkingHoursDTO != null && shiftBasedWorkingHoursDTO.size() >0){
				 shiftBasedWorkingHours = calTotalTime(shiftBasedWorkingHoursDTO, null); //Does not include Current User's Input Hours
			 }
			 
			 List<TimeSheetDaySummaryDTO>  User_shiftBasedWorkedHoursDTO = null;
			//Get USER's ShiftBased Total Worked Hours	by Date //Hence pass Shift ID param
			 
			 if(addorupdate.equalsIgnoreCase("add")){
				   User_shiftBasedWorkedHoursDTO = timeSheetManagementService.getTotalWorkedHoursbyDatenShift(user, currentDate, 0, shiftId); 
			 }else if(addorupdate.equalsIgnoreCase("update")){
				  
				 User_shiftBasedWorkedHoursDTO = timeSheetManagementService.getTotalWorkedHoursbyDatenShift(user, currentDate, timeSheetEntryMaster.getTimeSheetEntryId(), shiftId);
				 
			 }
			 
			 int uSER_shiftBased_finalTotalTime = 0;
			 if(User_shiftBasedWorkedHoursDTO != null && User_shiftBasedWorkedHoursDTO.size() >0){
				 
				 uSER_shiftBased_finalTotalTime = calTotalTime(User_shiftBasedWorkedHoursDTO, timeSheetEntryMaster); //Includes Current User's Input Hours
			 }else{//Seems no time entry by the user for the given shift w.r.t Date
				 
				 uSER_shiftBased_finalTotalTime = calTotalTime(null, timeSheetEntryMaster); //Includes only Current User's Input Hours, as there is no data for given Shift for User 
			 }
			
			 log.info("Acutaly shiftBasedWorkingHours : "+shiftBasedWorkingHours);
			 log.info("User Shiftbased Worked Hours including Current Input: "+uSER_shiftBased_finalTotalTime);					
			 String exceededShiftHours = null;
			 String exceededTotalHours = null;
			 String hoursWithinLimit = null;
			 ShiftHoursValidationDTO validationDTO = new ShiftHoursValidationDTO();
			 String errmsg = null;
			 if(uSER_shiftBased_finalTotalTime <= shiftBasedWorkingHours){						
					//USER'S Total hours worked by Date
				 List<TimeSheetDaySummaryDTO>  tsdsDTOList = null;
				 if(addorupdate.equalsIgnoreCase("add")){
					tsdsDTOList = timeSheetManagementService.getTotalWorkedHoursbyDatenShift(user, currentDate, 0, 0);//Excludes Shift and Current Input of Hours 
				 }else if(addorupdate.equalsIgnoreCase("update")){
				    tsdsDTOList = timeSheetManagementService.getTotalWorkedHoursbyDatenShift(user,  currentDate, timeSheetEntryMaster.getTimeSheetEntryId(), 0);//Include timesheetId to evaluate user's existing Timesheetdata for the day 
				 }
					 
				
					 int finalTotalTime = 0;
					 if(tsdsDTOList != null && tsdsDTOList.size() >0){
						 finalTotalTime = calTotalTime(tsdsDTOList, timeSheetEntryMaster); //Including current Users Input Hours
					 }else{
						 finalTotalTime = calTotalTime(null, timeSheetEntryMaster);//Includes only current User Input Hours, as there is no Shift data for User
					 }
					 log.info("User Total Working Hours(should be <1440 : "+finalTotalTime);
					 if(finalTotalTime<=1440){//< 24 hrs
						 hoursWithinLimit = "Yes";
						 validationDTO.setHoursWithinLimit(hoursWithinLimit);
					 }else{						 
						 exceededTotalHours ="Yes";
						 validationDTO.setExceededTotalHours(exceededTotalHours);
						 errmsg= "You are exceeding regular 24 hours";
						 validationDTO.setErrorMsg(errmsg);
					 }
				 }else{
					 exceededShiftHours="Yes";
					 validationDTO.setExceededShiftHours(exceededShiftHours);
					 errmsg= "You are exceeding Mandatory working hours of "+ shift.getShiftName()+"Shift";
					 validationDTO.setErrorMsg(errmsg);
				 }			 
			return validationDTO;
		}
		
		public Integer calTotalTime( List<TimeSheetDaySummaryDTO>  tsdsDTOList, TimeSheetEntryMaster timeSheetEntryMaster){
			
			 int existing_totalHours =0;
			 int existing_totalMins = 0;
			 int existing_totalTime = 0;
			 if(tsdsDTOList != null && tsdsDTOList.size() >0){
				 for (TimeSheetDaySummaryDTO tsdsDTO : tsdsDTOList) {
					 existing_totalHours = existing_totalHours+tsdsDTO.getTotalHours();
					 existing_totalMins = existing_totalMins + tsdsDTO.getTotalMins();
				}
				 existing_totalTime = (existing_totalHours *60)+ existing_totalMins; 
			 }
			 
			 //Adding User's Current Input Hours
			 int users_totalHours =0;
			 int users_totalMins = 0;
			 int totalTime = 0;
			 int finalTotalTime = 0;
			 if(timeSheetEntryMaster != null){ //Hours from Current User Input
				 users_totalHours = timeSheetEntryMaster.getHours();
				 users_totalMins = timeSheetEntryMaster.getMins();
				 totalTime = (users_totalHours*60)+users_totalMins; 
			 }			 
			 
			 finalTotalTime = existing_totalTime+ totalTime;
			 
			 return finalTotalTime;
		}
		
		
		@RequestMapping(value="resource.timesheet.summary.view.list",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse listWorkpackageDemandProjection(HttpServletRequest req, @RequestParam int resourceId, @RequestParam int weekNo) {
			JTableResponse jTableResponse = null;
			try {
					if(resourceId<=0){
						resourceId=1;
					}
					if (weekNo < 0) {
						weekNo = DateUtility.getWeekOfYear();
					}
					req.getSession().setAttribute("ssnHdnResourceAvailabilityWeekNo", weekNo);
					req.getSession().setAttribute("ssnHdnSelectedResourceId", resourceId);
					
					List<JsonTimeSheetDaySummary> jsonTimeSheetDaySummary = timeSheetManagementService.listTimeSheetSummaryForWeek(resourceId, weekNo);
					jTableResponse = new JTableResponse("OK", jsonTimeSheetDaySummary, jsonTimeSheetDaySummary.size());
					jsonTimeSheetDaySummary = null;
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
		            log.error("JSON ERROR", e);	            
		        }
	        return jTableResponse;
	    }
		
		@RequestMapping(value="workpackage.timesheet.entries.approve.list",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody JTableResponse approveTimeSheetEntries(HttpServletRequest req, @RequestParam int workPackageId) {
				log.info("inside workpackage.timesheet.entries.approve.list ----> workPackageId: "+workPackageId);
				JTableResponse jTableResponse = null;
				try {
						List<TimeSheetEntryMaster> timeSheetEntryMaster = timeSheetManagementService.getTimeSheetEntriesOfWorkPackageForApproval(workPackageId);
						List<JsonTimeSheetEntryMaster> jsonTimeSheetEntryMaster = new ArrayList<JsonTimeSheetEntryMaster>();
						for(TimeSheetEntryMaster timeSheetEntry: timeSheetEntryMaster){
							jsonTimeSheetEntryMaster.add(new JsonTimeSheetEntryMaster(timeSheetEntry));
						}
						jTableResponse = new JTableResponse("OK", jsonTimeSheetEntryMaster, timeSheetManagementService.getTotalRecordsOfTimeSheetEntryByUserId(workPackageId));
			        } catch (Exception e) {
			            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
			            log.error("JSON ERROR", e);	            
			        }
		        return jTableResponse;
		    }
		
		@RequestMapping(value="workpackage.timesheet.entries.status.list",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody JTableResponse statusBasedTimeSheetEntries(HttpServletRequest req, @RequestParam int workPackageId, @RequestParam int statusID) {
				log.debug("inside workpackage.timesheet.entries.status.list ----> workPackageId: "+workPackageId+", statusID---"+statusID);
				JTableResponse jTableResponse = null;
				try {					
						List<TimeSheetEntryMaster> timeSheetEntryMaster = timeSheetManagementService.getTimeSheetEntriesOfWorkPackageBasedonStatus(workPackageId, statusID);
						List<JsonTimeSheetEntryMaster> jsonTimeSheetEntryMaster = new ArrayList<JsonTimeSheetEntryMaster>();
						for(TimeSheetEntryMaster timeSheetEntry: timeSheetEntryMaster){
							jsonTimeSheetEntryMaster.add(new JsonTimeSheetEntryMaster(timeSheetEntry));
						}
						jTableResponse = new JTableResponse("OK", jsonTimeSheetEntryMaster, timeSheetManagementService.getTotalRecordsOfTimeSheetEntryByUserId(workPackageId));
			        } catch (Exception e) {
			            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
			            log.error("JSON ERROR", e);	            
			        }
		        return jTableResponse;
		    }
		
		
		@RequestMapping(value="workpackage.timesheet.entries.approve.update",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody JTableResponse updateAndApproveTimeSheetEntry(HttpServletRequest req,@ModelAttribute JsonTimeSheetEntryMaster jsonTimeSheetEntryMaster, BindingResult result) {
				JTableResponse jTableResponse;
				if(result.hasErrors()){
					jTableResponse = new JTableResponse("ERROR","Invalid Form!"); 
				}			
				try {			
						int isApproved = jsonTimeSheetEntryMaster.getIsApproved();
						TimeSheetEntryMaster timeSheetEntry = timeSheetManagementService.getTimeSheetEntryById(jsonTimeSheetEntryMaster.getTimeSheetEntryId());
						timeSheetEntry.setCreatedDate(DateUtility.dateformatWithOutTime(jsonTimeSheetEntryMaster.getCreatedDate()));
						timeSheetEntry.setApprovalComments(jsonTimeSheetEntryMaster.getApprovalComments());
						ProductMaster product = productListService.getProductDetailsById(jsonTimeSheetEntryMaster.getProductId());
						timeSheetEntry.setProduct(product);
						WorkPackage workPackage  = workPackageService.getWorkPackageByWorkPackageName(jsonTimeSheetEntryMaster.getWorkPackageName().trim());
						timeSheetEntry.setWorkPackage(workPackage);
						WorkShiftMaster shift = workPackageService.getWorkShiftByName(jsonTimeSheetEntryMaster.getShiftName());
						timeSheetEntry.setShift(shift);
						TimeSheetActivityType activityType = timeSheetManagementService.getTimeSheetActivityTypeById(jsonTimeSheetEntryMaster.getActivityTypeId());
						timeSheetEntry.setActivityType(activityType);
						UserList timeEntryLoggedForuser = userListService.getUserListById(jsonTimeSheetEntryMaster.getUserId());
						timeSheetEntry.setUser(timeEntryLoggedForuser);
						timeSheetEntry.setHours(jsonTimeSheetEntryMaster.getHours());
						UserRoleMaster userRole = userListService.getRoleById(jsonTimeSheetEntryMaster.getRoleId());
						if(userRole != null ){
							timeSheetEntry.setRole(userRole);
						}
						timeSheetEntry.setMins(jsonTimeSheetEntryMaster.getMins());
						UserList approver = (UserList) req.getSession().getAttribute("USER");
						timeSheetManagementService.updateAndApproveTimeSheetEntry(timeSheetEntry,approver,shift,isApproved);
						List<JsonTimeSheetEntryMaster> tmpList = new ArrayList<JsonTimeSheetEntryMaster>();
						tmpList.add(jsonTimeSheetEntryMaster);
			            jTableResponse = new JTableResponse("OK",tmpList ,1);     			
			        } catch (Exception e) {
			            jTableResponse = new JTableResponse("ERROR","Error updating or approving time sheet entry");
			            log.error("JSON ERROR updating or approving time sheet entry", e);
			        }       
		        return jTableResponse;
		    }
		@RequestMapping(value="timesheet.entry.update",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody JTableResponse updateTimeSheetEntry(HttpServletRequest req,@ModelAttribute JsonTimeSheetEntryMaster jsonTimeSheetEntryMaster, BindingResult result) {
				JTableResponse jTableResponse;
				if(result.hasErrors()){
					jTableResponse = new JTableResponse("ERROR","Invalid Form!"); 
				}		
				if(jsonTimeSheetEntryMaster != null){
					if(jsonTimeSheetEntryMaster.getIsApproved() == 1){
						jTableResponse = new JTableResponse("ERROR","You cannot update Time sheet entries, that are approved."); 
						return jTableResponse;
					}
					if(jsonTimeSheetEntryMaster.getHours()==0 && jsonTimeSheetEntryMaster.getMins() == 0){
						jTableResponse = new JTableResponse("ERROR","Time sheet entry cannot be 00:00. Please provide valid values."); 
						return jTableResponse;
					}
				}
				try {
						TimeSheetEntryMaster timeSheetEntryMasterFromDB = timeSheetManagementService.getTimeSheetEntryById(jsonTimeSheetEntryMaster.getTimeSheetEntryId());
						TimeSheetEntryMaster timeSheetEntryMasterFromUI=new TimeSheetEntryMaster();
						timeSheetEntryMasterFromUI.setCreatedDate(timeSheetEntryMasterFromDB.getCreatedDate());
						timeSheetEntryMasterFromUI.setHours(jsonTimeSheetEntryMaster.getHours());
						timeSheetEntryMasterFromUI.setMins(jsonTimeSheetEntryMaster.getMins());
						timeSheetEntryMasterFromUI.setApprovalComments(jsonTimeSheetEntryMaster.getApprovalComments());
						timeSheetEntryMasterFromUI.setStatus(jsonTimeSheetEntryMaster.getStatus());
						timeSheetEntryMasterFromUI.setApprovedDate(DateUtility.dateFormatWithOutSecondsddMMyyyy(jsonTimeSheetEntryMaster.getApprovedDate()));
						timeSheetEntryMasterFromUI.setTimeSheetEntryId(jsonTimeSheetEntryMaster.getTimeSheetEntryId());
						timeSheetEntryMasterFromUI.setIsApproved(jsonTimeSheetEntryMaster.getIsApproved());
						timeSheetEntryMasterFromUI.setComments(jsonTimeSheetEntryMaster.getComments());
						timeSheetEntryMasterFromUI.setModifiedDate(new Date(System.currentTimeMillis()));
						timeSheetEntryMasterFromUI.setDate(timeSheetEntryMasterFromDB.getDate());
						log.info("timeSheetEntryMasterFromUI.getWorkPackage().getWorkPackageId()--"+jsonTimeSheetEntryMaster.getWorkPackageId());
						if(jsonTimeSheetEntryMaster.getProductId()!=null && jsonTimeSheetEntryMaster.getProductId()!=null && !jsonTimeSheetEntryMaster.getProductId().equals("0")){				
							timeSheetEntryMasterFromUI.setProduct(productListService.getProductDetailsById(jsonTimeSheetEntryMaster.getProductId()));
						}
						if(jsonTimeSheetEntryMaster.getRoleId() != null){
							UserRoleMaster userRoleMaster=new UserRoleMaster();
							userRoleMaster.setUserRoleId(jsonTimeSheetEntryMaster.getRoleId());
							timeSheetEntryMasterFromUI.setRole(userRoleMaster);		
						}
						if(jsonTimeSheetEntryMaster.getWorkPackageId()!=null && jsonTimeSheetEntryMaster.getWorkPackageId()!=null && !jsonTimeSheetEntryMaster.getWorkPackageId().equals("0")){				
							timeSheetEntryMasterFromUI.setWorkPackage(workPackageService.getWorkPackageById(jsonTimeSheetEntryMaster.getWorkPackageId()));
						}
						if(jsonTimeSheetEntryMaster.getActivityTypeId()!=null && jsonTimeSheetEntryMaster.getActivityTypeId()!=null && !jsonTimeSheetEntryMaster.getActivityTypeId().equals("0")){				
							timeSheetEntryMasterFromUI.setActivityType(timeSheetManagementService.getTimeSheetActivityTypeById(jsonTimeSheetEntryMaster.getActivityTypeId()));
						}
						if(jsonTimeSheetEntryMaster.getResourceShiftCheckInId()!=null && jsonTimeSheetEntryMaster.getResourceShiftCheckInId()!=null && !jsonTimeSheetEntryMaster.getResourceShiftCheckInId().equals("0")){				
							timeSheetEntryMasterFromUI.setResourceShiftCheckIn(timeSheetManagementService.getByresourceShiftCheckInId(jsonTimeSheetEntryMaster.getResourceShiftCheckInId()));
						}
						WorkShiftMaster shift = workPackageService.getWorkShiftByName(jsonTimeSheetEntryMaster.getShiftName());
						UserList user = (UserList) req.getSession().getAttribute("USER");
						
						Date currentDate = new Date(System.currentTimeMillis());
						Date actualShiftWorkDate = new Date(System.currentTimeMillis());
						String jsondate = DateUtility.sdfDateformatWithOutTime(timeSheetEntryMasterFromDB.getDate());
						actualShiftWorkDate =DateUtility.dateFormatWithOutSeconds(jsondate);
						currentDate = getConvertedTimeEntryDate(jsondate, currentDate);
						
						Integer shiftId = shift.getShiftId();
						String addorupdate ="update";
						ShiftHoursValidationDTO validShifthours = null;
						ResourceShiftCheckIn resourceShiftCheckInObj = timeSheetManagementService.getByresourceShiftCheckInId(jsonTimeSheetEntryMaster.getResourceShiftCheckInId());
						if(resourceShiftCheckInObj != null && resourceShiftCheckInObj.getCheckOut() != null){
							validShifthours = isShiftCheckInHoursExceeded(resourceShiftCheckInObj,jsondate, user, timeSheetEntryMasterFromUI,"update");
						}else{
							validShifthours = isValidHoursForTimeSheet(jsondate, shiftId, user, currentDate, timeSheetEntryMasterFromUI, shift, addorupdate);
						}
						 if(validShifthours != null){
								if(validShifthours.getHoursWithinLimit() != null && validShifthours.getHoursWithinLimit().equalsIgnoreCase("Yes")){
									 timeSheetManagementService.updateTimeSheetEntry(timeSheetEntryMasterFromUI, user, shift);		
										List<JsonTimeSheetEntryMaster> tmpList = new ArrayList<JsonTimeSheetEntryMaster>();
										tmpList.add(jsonTimeSheetEntryMaster);
							            jTableResponse = new JTableResponse("OK",tmpList ,1);  
								}else if(validShifthours.getExceededShiftHours()  != null && validShifthours.getExceededShiftHours().equalsIgnoreCase("Yes")){
									 jTableResponse = new JTableResponse("ERROR",validShifthours.getErrorMsg());
								}else if(validShifthours.getExceededTotalHours()  != null && validShifthours.getExceededTotalHours().equalsIgnoreCase("Yes")){
									jTableResponse = new JTableResponse("ERROR",validShifthours.getErrorMsg());
								}else{
									 jTableResponse = new JTableResponse("ERROR","Error adding record!");
								}						
							}else{
								 jTableResponse = new JTableResponse("ERROR","Error adding record!");
							}	
						 
			        } catch (Exception e) {
			            jTableResponse = new JTableResponse("ERROR","Error updating or approving time sheet entry");
			            log.error("JSON ERROR updating or approving time sheet entry : ", e);
			        }       
		        return jTableResponse;
		    }
		
		@RequestMapping(value="timeSheet.resourceShiftCheckIn.add",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableSingleResponse addResourceShiftCkeckIn(@RequestParam int productId,@RequestParam int userId,@RequestParam Date workDate,@RequestParam int actualshiftId,@RequestParam String shiftTime,@RequestParam String shiftRemarks,@RequestParam String shiftRemarksValue) {  
			log.info(" inside timeSheet.resourceShiftCheckIn.add  : ");
			JTableSingleResponse jTableSingleResponse=null;		
			String errMsg="";
			Date checkIn=DateUtility.toDateInSec(shiftTime);
			boolean	errFlag=false;
			try {
				ResourceShiftCheckIn resShiftObjByWorkDate=null;
				List<ResourceShiftCheckIn> resShiftCheckinByWorkDateList=new ArrayList<ResourceShiftCheckIn>();
				ActualShift actualShift=timeSheetManagementService.listActualShiftbyActualShiftId(actualshiftId);
				List<ResourceShiftCheckIn> resourceShiftCheckinList = timeSheetManagementService.listResourceShiftCkeckInByUserId(userId);
				resShiftCheckinByWorkDateList=(List<ResourceShiftCheckIn>) timeSheetManagementService.getResourceShiftCheckInByDate(workDate,userId);
				if(resShiftCheckinByWorkDateList!=null && resShiftCheckinByWorkDateList.size()!=0){
				resShiftObjByWorkDate=resShiftCheckinByWorkDateList.get(resShiftCheckinByWorkDateList.size()-1);
				}
				if(resourceShiftCheckinList.size()!=0){
				ResourceShiftCheckIn resourceShiftCheckIn=resourceShiftCheckinList.get(resourceShiftCheckinList.size()-1);
				if(resourceShiftCheckIn!=null){
					if(resourceShiftCheckIn.getCheckOut()==null){
						errFlag=true;
						errMsg="There is a pending check out for you on : "+resourceShiftCheckIn.getCreatedDate()+" - "+resourceShiftCheckIn.getActualShift().getShift().getShiftName()+"-"+resourceShiftCheckIn.getProductMaster().getProductName();
						
					}else if(resourceShiftCheckIn.getCreatedDate().compareTo(workDate)==0){
						if(resourceShiftCheckIn.getCheckOut().compareTo(checkIn)>0){
							errFlag=true;
							errMsg="Your current check-in is lesser than your previous check-out.";
						}
						
					}else if(resShiftObjByWorkDate!=null){
						if(resShiftObjByWorkDate.getCheckOut().compareTo(checkIn)>0){
							errFlag=true;
							errMsg="Your current check-in is lesser than your previous check-out.";
						}
						
					}
				}
			}
				if(errFlag){
					jTableSingleResponse = new JTableSingleResponse("ERROR",errMsg);
					return jTableSingleResponse;
				}
					 timeSheetManagementService.addResourceShiftCkeckIn(productId,userId,workDate,actualShift,shiftTime,shiftRemarks,shiftRemarksValue);	
			
		        } catch (Exception e) {
		            jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding !");
		            log.error("JSON ERROR", e);
		        }		        
	        return jTableSingleResponse;			
	    }
		
		@RequestMapping(value="timeSheet.resourceShiftCheckIn.update",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody JTableResponse updateResourceShiftCkeckIn(@RequestParam int productId,@RequestParam int userId,@RequestParam Date workDate,@RequestParam int actualshiftId,@RequestParam String shiftTime,@RequestParam String shiftRemarks,@RequestParam String shiftRemarksValue) {
			JTableResponse jTableResponse;
			Date checkOut=DateUtility.toDateInSec(shiftTime);
			log.info("inside timeSheet.resourceShiftCheckIn.update ");	
			boolean	errFlag=false;
			String errMsg="There is no pending check out";
			ResourceShiftCheckIn resourceShiftCheckIn=null;
			try {
			
			List<ResourceShiftCheckIn> resourceShiftCheckinList = timeSheetManagementService.listResourceShiftCkeckInByUserId(userId);
			if(resourceShiftCheckinList.size()!=0){
				 resourceShiftCheckIn=resourceShiftCheckinList.get(resourceShiftCheckinList.size()-1);
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(resourceShiftCheckIn.getCheckIn());
					calendar.add(Calendar.HOUR_OF_DAY,24);
					    Date maxCheckOutTime = calendar.getTime();
					    log.info("checkOutTime====>"+maxCheckOutTime);
				if(resourceShiftCheckIn!=null){if(resourceShiftCheckIn.getCheckOut()==null){
						if(resourceShiftCheckIn.getActualShift().getActualShiftId()==actualshiftId && resourceShiftCheckIn.getProductMaster().getProductId()==productId){
							if(resourceShiftCheckIn.getCheckIn().compareTo(checkOut)>0 || maxCheckOutTime.compareTo(checkOut)<0 ){
								errFlag=true; errMsg="Check Out time should be greater than Check In Time and should be lesser than next 24 hours";
							}
						}else{
							errFlag=true;
							errMsg="There is a pending check out for you on : "+resourceShiftCheckIn.getCreatedDate()+" - "+resourceShiftCheckIn.getActualShift().getShift().getShiftName()+"-"+resourceShiftCheckIn.getProductMaster().getProductName();
						}
							
					}else{  
						errFlag=true;
					}
				}
			}else{
				 errFlag=true;
			}
			if(errFlag){
				jTableResponse = new JTableResponse("ERROR",errMsg);
				return jTableResponse;
			}
			if (shiftRemarks.equalsIgnoreCase("endRemarks")) {
				resourceShiftCheckIn.setCheckOut(checkOut);
				 resourceShiftCheckIn.setEndTimeRemarks(shiftRemarksValue);
			}
			timeSheetManagementService.updateResourceShiftCkeckIn(resourceShiftCheckIn);
			jTableResponse = new JTableResponse("OK");
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error updating record!");
		            log.error("JSON ERROR updating ResourceShiftCkeckIn : ", e);
		        }
		         
	        return jTableResponse;
	    }
		@RequestMapping(value="timeSheet.resourceShiftCheckIn.list",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse listResourceShiftCkeckIn(@RequestParam int productId,@RequestParam Date date,@RequestParam int userId, @RequestParam int shiftTypeId) {
			log.debug("inside workpackage.resourceShiftCheckIn.list");		
			String workDuration = null;
			String timeSheetDuration = null;
			JTableResponse jTableResponse = null;
			List<JsonResourceShiftCheckin> jsonresourceShiftCheckinList = new ArrayList<JsonResourceShiftCheckin>();
			List<TimeSheetEntryMaster> listOfTimeSheetEntriesOfShiftChInId = null;
				try {
					log.info("userId"+userId);
						List<ResourceShiftCheckIn> resourceShiftCheckinList = timeSheetManagementService.listResourceShiftCkeckInByproductId(productId,date,userId,null,-1,shiftTypeId); 
						if(resourceShiftCheckinList.size()>0){
							for(ResourceShiftCheckIn resourceShiftCheckin:resourceShiftCheckinList){
								JsonResourceShiftCheckin jsonCheckIn = new JsonResourceShiftCheckin(resourceShiftCheckin);
								jsonCheckIn.setCreatedDate(DateUtility.sdfDateformatWithOutTime(resourceShiftCheckin.getCreatedDate()));
								listOfTimeSheetEntriesOfShiftChInId = timeSheetManagementService.getTimeSheetEntriesOfResourceShiftCheckIn(resourceShiftCheckin.getResourceShiftCheckInId());
									if(resourceShiftCheckin.getActualShift()!=null){
										if(resourceShiftCheckin.getCheckOut() != null){
											long checkInCheckOutDurationInMinutes = DateUtility.DateDifferenceInMinutes(resourceShiftCheckin.getCheckIn(), resourceShiftCheckin.getCheckOut());
											workDuration = DateUtility.convertTimeInHoursMinutes(0, new Integer((int)checkInCheckOutDurationInMinutes));
											log.info("totalTimeConvertedInHoursMinutes for Shift Duration=" + workDuration);
										}else{
											workDuration = "00:00";
										}
										jsonCheckIn.setWorkDuration(workDuration);
										jsonresourceShiftCheckinList.add(jsonCheckIn);
									}
									if(listOfTimeSheetEntriesOfShiftChInId != null && listOfTimeSheetEntriesOfShiftChInId.size()>0){
										long totalTimesheetDuration = 0;
										for (TimeSheetEntryMaster tsEntry : listOfTimeSheetEntriesOfShiftChInId) {
											long timeSheetDurationInHrsMins = DateUtility.convertTimeInMinutes(tsEntry.getHours(),tsEntry.getMins());
											totalTimesheetDuration = totalTimesheetDuration+timeSheetDurationInHrsMins;
										}
										if(totalTimesheetDuration == 0){
											timeSheetDuration = "00:00";
										}else{
											timeSheetDuration = DateUtility.convertTimeInHoursMinutes(0, new Integer((int)totalTimesheetDuration));
										}
										jsonCheckIn.setTimeSheetHours(timeSheetDuration);
									}
								}
							}
					if(jsonresourceShiftCheckinList!=null || jsonresourceShiftCheckinList.size()>0){
						jTableResponse = new JTableResponse("OK", jsonresourceShiftCheckinList, jsonresourceShiftCheckinList.size());
					}
					jsonresourceShiftCheckinList=null;
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
		            log.error("JSON ERROR", e);	            
		        }
	        return jTableResponse;
	    }
		@RequestMapping(value="resource.shift.timeSheetEntries.approve.update",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody JTableResponse updateAndApproveResourceShiftTimeSheetEntry(HttpServletRequest req,@ModelAttribute JsonResourceShiftCheckin jsonResourceShiftCheckin, BindingResult result) {
				JTableResponse jTableResponse=null;
				if(result.hasErrors()){
					jTableResponse = new JTableResponse("ERROR","Invalid Form!"); 
				}			
				try {		String strApproverId=	req.getParameter("approverId");
				            Integer approverId=Integer.parseInt(strApproverId);
						int isApproved = jsonResourceShiftCheckin.getIsApproved();
						timeSheetManagementService.updateAndApproveResourceShiftTimeSheet(jsonResourceShiftCheckin,approverId);
			            jTableResponse = new JTableResponse("OK");    			
			        } catch (Exception e) {
			            jTableResponse = new JTableResponse("ERROR","Error updating or approving time sheet entry");
			            log.error("JSON ERROR updating or approving time sheet entry :  ", e);
			        }       
		        return jTableResponse;
		    }
		@RequestMapping(value="resource.shift.timeSheetEntries.approve.list",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse listApproveResourceShiftTimeSheetEntry(@RequestParam int productId,@RequestParam Date date,@RequestParam int userId,@RequestParam int shiftId,@RequestParam int isApproved) {
			log.debug("resource.shift.timeSheetEntries.approve.list");		
			int jtStartIndex=0;
			int jtPageSize=10;
			JTableResponse jTableResponse = null;
			ActualShift actualShift=null;
			List<JsonResourceShiftCheckin> jsonresourceShiftCheckinList = new ArrayList<JsonResourceShiftCheckin>();
				try {
					log.info("shiftId"+shiftId);
					log.info("productId"+productId);
					log.info("userId"+userId);
					UserList user=userListService.getUserListById(userId);
					if(shiftId!=-1){
					 actualShift=timeSheetManagementService.listActualShiftbyshiftId(shiftId,date);
					 if(actualShift==null){
						 jTableResponse = new JTableResponse("OK");
						 return jTableResponse;
					 }
					}
					if(productId!=-1){
					List<ResourceShiftCheckIn> resourceShiftCheckinList = timeSheetManagementService.listResourceShiftCkeckInByproductId(productId,date,-1,actualShift,isApproved,0);	
					if(user.getUserRoleMaster().getUserRoleId()==IDPAConstants.ROLE_ID_TEST_MANAGER){
						for(ResourceShiftCheckIn resourceShiftCheckin:resourceShiftCheckinList){
							if(resourceShiftCheckin.getUserList().getUserRoleMaster().getUserRoleId()==IDPAConstants.ROLE_ID_TEST_LEAD || resourceShiftCheckin.getUserList().getUserRoleMaster().getUserRoleId()==IDPAConstants.ROLE_ID_TESTER){
							jsonresourceShiftCheckinList.add(new JsonResourceShiftCheckin(resourceShiftCheckin));
							}
							}
					}else{
						for(ResourceShiftCheckIn resourceShiftCheckin:resourceShiftCheckinList){
							if(resourceShiftCheckin.getUserList().getUserRoleMaster().getUserRoleId()==IDPAConstants.ROLE_ID_TESTER){
							jsonresourceShiftCheckinList.add(new JsonResourceShiftCheckin(resourceShiftCheckin));
							}
							}
					}
					
					jTableResponse = new JTableResponse("OK", jsonresourceShiftCheckinList, jsonresourceShiftCheckinList.size());
					resourceShiftCheckinList = null;
					}else{
						jTableResponse = new JTableResponse("OK", jsonresourceShiftCheckinList, 0);
					}
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
		            log.error("JSON ERROR", e);	            
		        }
	        return jTableResponse;
	    }
		@RequestMapping(value="timeSheet.resourceShiftCheckIn.updateByResShiftId",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody JTableResponse updateResourceShiftCkeckInByResourceShiftId(HttpServletRequest req,@ModelAttribute JsonResourceShiftCheckin  jsonResourceShiftCheckin, @RequestParam Integer resourceShiftCheckInId,@RequestParam String modifiedField, @RequestParam String modifiedFieldValue) {
			JTableResponse jTableResponse;
			log.info("inside timeSheet.resourceShiftCheckIn.update "+modifiedFieldValue);	
			boolean	errFlag=false;
			String errMsg="There is no pending check out";
			ResourceShiftCheckIn resourceShiftCheckIn=null;
			ResourceShiftCheckIn resourceShiftCheckInByDate=null;
			
			try {
				resourceShiftCheckIn=	timeSheetManagementService.getByresourceShiftCheckInId(resourceShiftCheckInId);
				Date UIfieldDate=DateUtility.toDateInSec(modifiedFieldValue);
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(resourceShiftCheckIn.getCheckIn());
				calendar.add(Calendar.HOUR_OF_DAY,24);
				    Date maxCheckOutTime = calendar.getTime();
				
				List<ResourceShiftCheckIn> resShiftCheckinByWorkDateList=(List<ResourceShiftCheckIn>) timeSheetManagementService.getResourceShiftCheckInByDate(resourceShiftCheckIn.getCreatedDate(),resourceShiftCheckIn.getUserList().getUserId());
				if(resShiftCheckinByWorkDateList!=null && resShiftCheckinByWorkDateList.size()>=2 ){
				resourceShiftCheckInByDate=resShiftCheckinByWorkDateList.get(resShiftCheckinByWorkDateList.size()-2);
				}
				if(resourceShiftCheckIn!=null){
				if (modifiedField.equalsIgnoreCase("checkOutTime")) {
				 if(resourceShiftCheckIn.getCheckIn().compareTo(UIfieldDate)>0 || maxCheckOutTime.compareTo(UIfieldDate)<0 ){
						errFlag=true; 
						errMsg="Check Out time should be greater than Check In Time and should be lesser than next 24 hours";
						jTableResponse = new JTableResponse("ERROR",errMsg);
						return jTableResponse;
					}
					if(resShiftCheckinByWorkDateList.size()>=2){
					for(ResourceShiftCheckIn resObj:resShiftCheckinByWorkDateList){
						if(resObj.getResourceShiftCheckInId()>resourceShiftCheckIn.getResourceShiftCheckInId()) {
							if(resObj .getCheckIn().compareTo(UIfieldDate)<0  ){
								errFlag=true; errMsg="Check Out time  should be lesser than next check in time";
								break;
							}
							
						}
					}
				}
				resourceShiftCheckIn.setCheckOut(UIfieldDate);
				} else if(modifiedField.equalsIgnoreCase("checkInTime")){
					int pos=0;
					int count=0;
					 if(resShiftCheckinByWorkDateList.size()>=2){
					for(ResourceShiftCheckIn resObj:resShiftCheckinByWorkDateList){
						++count;
								if(resObj.getResourceShiftCheckInId().equals(resourceShiftCheckIn.getResourceShiftCheckInId())) {
									 pos=count;
									break;
								}
							}
						 
						 if(pos==1){
							 if(resourceShiftCheckIn.getCheckOut()!=null && resourceShiftCheckIn.getCheckOut().compareTo(UIfieldDate)<0){
									errFlag=true; /*errMsg="Check In time should be greater than previous Check Out Time";*/
									errMsg="Your current check-in is lesser than your previous check-out.";
								}
						 }else{
						 resourceShiftCheckInByDate=resShiftCheckinByWorkDateList.get(pos-2);
						 if(resourceShiftCheckInByDate.getCheckOut()!=null && resourceShiftCheckInByDate.getCheckOut().compareTo(UIfieldDate)>0){
							errFlag=true; /*errMsg="Check In time should be greater than previous Check Out Time";*/
							errMsg="Your current check-in is lesser than your previous check-out.";
						}
						 
					}
					 }
					 else if(resourceShiftCheckIn.getCheckOut() != null){
						if(resourceShiftCheckIn.getCheckOut().compareTo(UIfieldDate)<0){
							errFlag=true; errMsg="Check In time should be lesser than Check Out time";
						}
					}
					 if(UIfieldDate.compareTo(resourceShiftCheckIn.getActualShift().getStartTime())<0){
							UIfieldDate=resourceShiftCheckIn.getActualShift().getStartTime();
						}
					resourceShiftCheckIn.setCheckIn(UIfieldDate);
				}
				}
				if(errFlag){
					jTableResponse = new JTableResponse("ERROR",errMsg);
					return jTableResponse;
				}
				
				timeSheetManagementService.updateResourceShiftCkeckIn(resourceShiftCheckIn);
			jTableResponse = new JTableResponse("OK");  
			}
				catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error updating record!");
		            log.error("JSON ERROR updating ResourceShiftCkeckIn By ResourceShiftId", e);
		        }
	        return jTableResponse;
	    }
		
	@RequestMapping(value="actual.shift.startTimeAndEndTime.ByActualShiftId",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody  String  getActualStartTimeAndEndTime(@RequestParam int actualshiftId) {
		log.info("welcome"+actualshiftId);
		String startTimeAndEndTime="";
		 String currenDateWithFormat="";
		Date currentDate=new Date();
		currenDateWithFormat=DateUtility.dateToStringInSecond(currentDate);
				ActualShift actualShift=timeSheetManagementService.listActualShiftbyActualShiftId(actualshiftId);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				if(actualShift.getEndTime()!=null){
			       startTimeAndEndTime=sdf.format(actualShift.getStartTime())+"#"+sdf.format(actualShift.getEndTime());
				}else{
					startTimeAndEndTime=sdf.format(actualShift.getStartTime())+"#"+currenDateWithFormat;
				}
			           log.info("times"+startTimeAndEndTime);   
		        return startTimeAndEndTime;
		    }
	
	@RequestMapping(value="timeSheet.resourceShiftCheckIn.delete",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse deleteResorceShiftCheckIn(@RequestParam int resourceShiftCheckInId) {			
		JTableResponse jTableResponse;
		try {
			ResourceShiftCheckIn resourceShiftCheckIn= timeSheetManagementService.getByresourceShiftCheckInId(resourceShiftCheckInId);
			 List<JsonTimeSheetEntryMaster>	jsonTimeSheetEntryMaster = timeSheetManagementService.getTimeSheetEntriesOfResourceShiftCheckInId(resourceShiftCheckInId);
			if(resourceShiftCheckIn.getIsApproved()==1){
				jTableResponse = new JTableResponse("ERROR","This shift check-in already approved. So, you can't delete this entry");
				return jTableResponse;
			}else if(jsonTimeSheetEntryMaster.size()!=0){
				jTableResponse = new JTableResponse("ERROR","timesheet for this  shift  checkin exists. So, you can't delete this entry");
				return jTableResponse;
			}
			
				timeSheetManagementService.deleteResorceShiftCheckIn(resourceShiftCheckIn);
	            jTableResponse = new JTableResponse("OK","yes");
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error deleting shift check in!");
	            log.error("JSON ERROR deleting shift check in :", e);
	        }
        return jTableResponse;
    }
	@RequestMapping(value="timeSheet.resourceShiftCheckIn.updateResShiftRemarks",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse updateResShiftCheckInRemarks(HttpServletRequest req,@ModelAttribute JsonResourceShiftCheckin jsonResourceShiftCheckin, BindingResult result) {
			JTableResponse jTableResponse=null;
			if(result.hasErrors()){
				jTableResponse = new JTableResponse("ERROR","Invalid Form!"); 
			}			
			try {	
				ResourceShiftCheckIn resourceShiftCheckIn=null;
				
				resourceShiftCheckIn=	timeSheetManagementService.getByresourceShiftCheckInId(jsonResourceShiftCheckin.getResourceShiftCheckInId());
				if(jsonResourceShiftCheckin.getStartTimeRemarks()!=""){
			String checkInRemarks=	jsonResourceShiftCheckin.getStartTimeRemarks();
			resourceShiftCheckIn.setStartTimeRemarks(checkInRemarks);
				}
				if(jsonResourceShiftCheckin.getEndTimeRemarks()!=""){
			String checkOutRemarks=	jsonResourceShiftCheckin.getEndTimeRemarks();
			resourceShiftCheckIn.setEndTimeRemarks(checkOutRemarks);
				}
				
				timeSheetManagementService.updateResourceShiftCkeckIn(resourceShiftCheckIn);
		            jTableResponse = new JTableResponse("OK");    			
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error updating or approving time sheet entry");
		            log.error("JSON ERROR updating or approving time sheet entry : ", e);
		        }       
	        return jTableResponse;
	    }
	
	@RequestMapping(value="resource.shift.checkin.timesheet.entries.list",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse listResourceShiftCheckInTimeSheetEntries( @RequestParam int resourceShiftCheckInId) {
			JTableResponse jTableResponse = null;
			try {
					if(resourceShiftCheckInId<=0){
						resourceShiftCheckInId=1;
					}
					List<JsonTimeSheetEntryMaster> jsonTimeSheetEntryMaster = null;
					jsonTimeSheetEntryMaster = timeSheetManagementService.getTimeSheetEntriesOfResourceShiftCheckInId(resourceShiftCheckInId);
					jTableResponse = new JTableResponse("OK", jsonTimeSheetEntryMaster, jsonTimeSheetEntryMaster.size());
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
		            log.error("JSON ERROR", e);	            
		        }
	        return jTableResponse;
	    }
		
				
}
