package com.hcl.atf.taf.controller;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.hcl.atf.taf.controller.utilities.ValidationUtility;
import com.hcl.atf.taf.model.ActivitySecondaryStatusMaster;
import com.hcl.atf.taf.model.ActivityStatus;
import com.hcl.atf.taf.model.DimensionMaster;
import com.hcl.atf.taf.model.StatusCategory;
import com.hcl.atf.taf.model.dto.StatusSummaryDTO;
import com.hcl.atf.taf.model.json.JsonActivitySecondaryStatusMaster;
import com.hcl.atf.taf.model.json.JsonActivityStatus;
import com.hcl.atf.taf.model.json.JsonStatusCategory;
import com.hcl.atf.taf.model.json.JsonStatusSummary;
import com.hcl.atf.taf.model.json.jtable.JTableResponse;
import com.hcl.atf.taf.model.json.jtable.JTableResponseOptions;
import com.hcl.atf.taf.model.json.jtable.JTableSingleResponse;
import com.hcl.atf.taf.service.StatusService;

@Controller
public class StatusManagementController {

	private static final Log log = LogFactory.getLog(StatusManagementController.class);
	
	@Autowired
	private StatusService statusService;
	
	@RequestMapping(value="status.summary",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse getProductSummary(@RequestParam Integer statusId){	
		log.debug("status.summary");
		JTableResponse jTableResponse = null;
		try {
			StatusSummaryDTO statusSummaryDTO = statusService.getStatusSummary(statusId);
			List<JsonStatusSummary> jsonStatusSummaryList = new ArrayList<JsonStatusSummary>();
			if (statusSummaryDTO == null ) {
				jTableResponse = new JTableResponse("OK", jsonStatusSummaryList, 0);
			} else {
				jsonStatusSummaryList.add(new JsonStatusSummary(statusSummaryDTO));
				jTableResponse = new JTableResponse("OK", jsonStatusSummaryList, jsonStatusSummaryList.size());
				statusSummaryDTO = null;
			}
        } catch (Exception e) {
            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
            log.error("JSON ERROR", e);	            
        }
        return jTableResponse;
    }
	
	@RequestMapping(value="status.primary.by.status",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse getPrimaryStatusList(@RequestParam Integer statusId, @RequestParam Integer activeStatus, @RequestParam Integer jtStartIndex, @RequestParam Integer jtPageSize) {
		log.debug("inside status.primary.by.status");
		JTableResponse jTableResponse;
		 
		try {
			List<ActivityStatus> primaryStatuses = statusService.getPrimaryStatusByStatus(statusId, activeStatus, jtStartIndex, jtPageSize);
			List<JsonActivityStatus> jsonActivityStatuses = new ArrayList<JsonActivityStatus>();
			int totalRecordsAvailable = statusService.getTotalRecordsForPrimaryStatusPagination(statusId, activeStatus, ActivityStatus.class);
			
			for(ActivityStatus primaryStatus : primaryStatuses){
				jsonActivityStatuses.add(new JsonActivityStatus(primaryStatus));
			}
	        jTableResponse = new JTableResponse("OK", jsonActivityStatuses, totalRecordsAvailable);
	           
        } catch (Exception e) {
            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
            log.error("JSON ERROR", e);
        }
	        
        return jTableResponse;
    }
	
	@RequestMapping(value="status.primary.add",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse addPrimaryStatus(@RequestParam Integer statusId, @ModelAttribute JsonActivityStatus jsonActivityStatus, BindingResult result, HttpServletRequest request) {  
		log.debug("  ------------status.primary.add-----------");
		JTableSingleResponse jTableSingleResponse;
		if(result.hasErrors()){
			jTableSingleResponse = new JTableSingleResponse("ERROR","Invalid form!");
			return jTableSingleResponse;
		}
		try {
			String errorMessage = ValidationUtility.validateForPrimaryStatusAdditionOrUpdation(statusId, jsonActivityStatus.getActivityStatusName(), statusService, jsonActivityStatus.getActivityStatusId());			
			if (errorMessage != null) {
				jTableSingleResponse = new JTableSingleResponse("ERROR",errorMessage);
				return jTableSingleResponse;
			}
			ActivityStatus activityStatus = jsonActivityStatus.getActivityStatus();
			DimensionMaster dimensionMaster = new DimensionMaster();
			dimensionMaster.setDimensionId(statusId);
			activityStatus.setDimension(dimensionMaster);
			statusService.addPrimaryStatus(activityStatus);			
	        jTableSingleResponse = new JTableSingleResponse("OK",new JsonActivityStatus(activityStatus));
	    } catch (Exception e) {
	        jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding new primary status!");
	        log.error("JSON ERROR", e);
	    }	        
        return jTableSingleResponse;		
    }
	
	@RequestMapping(value="status.primary.update",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse updatePrimaryStatus(@RequestParam Integer statusId, @ModelAttribute JsonActivityStatus jsonActivityStatus, BindingResult result, HttpServletRequest request) {  
		log.debug("  ------------status.primary.update-----------");
		JTableSingleResponse jTableSingleResponse;
		if(result.hasErrors()){
			jTableSingleResponse = new JTableSingleResponse("ERROR","Invalid form!");
			return jTableSingleResponse;
		}
		try {
			
			String errorMessage = ValidationUtility.validateForPrimaryStatusAdditionOrUpdation(statusId, jsonActivityStatus.getActivityStatusName(), statusService, jsonActivityStatus.getActivityStatusId());			
			if (errorMessage != null) {
				jTableSingleResponse = new JTableSingleResponse("ERROR",errorMessage);
				return jTableSingleResponse;
			}
			ActivityStatus activityStatus = jsonActivityStatus.getActivityStatus();
			DimensionMaster dimensionMaster = new DimensionMaster();
			dimensionMaster.setDimensionId(statusId);
			activityStatus.setDimension(dimensionMaster);
			statusService.updatePrimaryStatus(activityStatus);			
	        jTableSingleResponse = new JTableSingleResponse("OK",new JsonActivityStatus(activityStatus));
	    } catch (Exception e) {
	        jTableSingleResponse = new JTableSingleResponse("ERROR","Error updating primary status!");
	        log.error("JSON ERROR", e);
	    }	        
        return jTableSingleResponse;		
    }
	
	@RequestMapping(value="status.secondary.by.status",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse getSecondaryStatusList(@RequestParam Integer statusId, @RequestParam Integer activeStatus, @RequestParam Integer jtStartIndex, @RequestParam Integer jtPageSize) {
		log.debug("inside status.secondary.by.status");
		JTableResponse jTableResponse;
		 
		try {
			List<ActivitySecondaryStatusMaster> secondaryStatuses = statusService.getSecondaryStatusByStatus(statusId, activeStatus, jtStartIndex, jtPageSize);
			List<JsonActivitySecondaryStatusMaster> jsonActivitySecondaryStatusMasters = new ArrayList<JsonActivitySecondaryStatusMaster>();
			int totalRecordsAvailable = statusService.getTotalRecordsForSecondaryStatusPagination(statusId, activeStatus, ActivitySecondaryStatusMaster.class);
			
			for(ActivitySecondaryStatusMaster secondaryStatusMaster : secondaryStatuses){
				jsonActivitySecondaryStatusMasters.add(new JsonActivitySecondaryStatusMaster(secondaryStatusMaster));
			}
	        jTableResponse = new JTableResponse("OK", jsonActivitySecondaryStatusMasters, totalRecordsAvailable);
	           
        } catch (Exception e) {
            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
            log.error("JSON ERROR", e);
        }
	        
        return jTableResponse;
    }
	
	@RequestMapping(value="status.secondary.add",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse addSecondaryStatus(@RequestParam Integer statusId, @ModelAttribute JsonActivitySecondaryStatusMaster jsonActivitySecondaryStatusMaster, BindingResult result, HttpServletRequest request) {  
		log.debug("  ------------status.secondary.add-----------");
		JTableSingleResponse jTableSingleResponse;
		if(result.hasErrors()){
			jTableSingleResponse = new JTableSingleResponse("ERROR","Invalid form!");
			return jTableSingleResponse;
		}
		try {
			String errorMessage = ValidationUtility.validateForSecondaryStatusAdditionOrUpdation(statusId, jsonActivitySecondaryStatusMaster.getActivitySecondaryStatusName(), statusService, jsonActivitySecondaryStatusMaster.getActivitySecondaryStatusId());			
			if (errorMessage != null) {
				jTableSingleResponse = new JTableSingleResponse("ERROR",errorMessage);
				return jTableSingleResponse;
			}
			ActivitySecondaryStatusMaster activitySecondaryStatusMaster = jsonActivitySecondaryStatusMaster.getActivitySecondaryStatusMaster();
			DimensionMaster dimensionMaster = new DimensionMaster();
			dimensionMaster.setDimensionId(statusId);
			activitySecondaryStatusMaster.setDimensionId(dimensionMaster);
			statusService.addSecondaryStatus(activitySecondaryStatusMaster);			
	        jTableSingleResponse = new JTableSingleResponse("OK",new JsonActivitySecondaryStatusMaster(activitySecondaryStatusMaster));
	    } catch (Exception e) {
	        jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding new secondary status!");
	        log.error("JSON ERROR", e);
	    }	        
        return jTableSingleResponse;		
    }
	
	@RequestMapping(value="status.secondary.update",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse updateSecondaryStatus(@RequestParam Integer statusId, @ModelAttribute JsonActivitySecondaryStatusMaster jsonActivitySecondaryStatusMaster, BindingResult result, HttpServletRequest request) {  
		log.debug("  ------------status.secondary.update-----------");
		JTableSingleResponse jTableSingleResponse;
		if(result.hasErrors()){
			jTableSingleResponse = new JTableSingleResponse("ERROR","Invalid form!");
			return jTableSingleResponse;
		}
		try {
			String errorMessage = ValidationUtility.validateForSecondaryStatusAdditionOrUpdation(statusId, jsonActivitySecondaryStatusMaster.getActivitySecondaryStatusName(), statusService, jsonActivitySecondaryStatusMaster.getActivitySecondaryStatusId());			
			if (errorMessage != null) {
				jTableSingleResponse = new JTableSingleResponse("ERROR",errorMessage);
				return jTableSingleResponse;
			}
			ActivitySecondaryStatusMaster activitySecondaryStatusMaster = jsonActivitySecondaryStatusMaster.getActivitySecondaryStatusMaster();
			DimensionMaster dimensionMaster = new DimensionMaster();
			dimensionMaster.setDimensionId(statusId);
			activitySecondaryStatusMaster.setDimensionId(dimensionMaster);
			statusService.updateSecondaryStatus(activitySecondaryStatusMaster);			
	        jTableSingleResponse = new JTableSingleResponse("OK",new JsonActivitySecondaryStatusMaster(activitySecondaryStatusMaster));
	    } catch (Exception e) {
	        jTableSingleResponse = new JTableSingleResponse("ERROR","Error updating secondary status!");
	        log.error("JSON ERROR", e);
	    }	        
        return jTableSingleResponse;		
    }
	
	@RequestMapping(value="secondary.status.available.for.primary.status",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listSecondaryStatusToAddWithPrimaryStatus(@RequestParam Integer primaryStatusId, @RequestParam Integer statusId, @RequestParam Integer jtStartIndex, @RequestParam Integer jtPageSize) {  
		log.debug("  ------------secondary.status.available.for.primary.status-----------");
		JTableResponse jTableResponse;
		try {
			List<Object[]> activitySecondaryStatusMasters = statusService.getSecondaryStatusToAddWithPrimaryStatus(primaryStatusId, statusId, jtStartIndex, jtPageSize);
			ArrayList<HashMap<String, Object>> availableSecondaryStatus = new ArrayList<HashMap<String, Object>>();
			if(activitySecondaryStatusMasters!=null && activitySecondaryStatusMasters.size()>0){
				for (Object[] row : activitySecondaryStatusMasters) {
					HashMap<String, Object> secondaryStatus =new HashMap<String, Object>();
					secondaryStatus.put(IDPAConstants.ITEM_ID, (Integer)row[0]);
					secondaryStatus.put(IDPAConstants.ITEM_NAME, (String)row[1]);					
					availableSecondaryStatus.add(secondaryStatus);					
				}
			}
			jTableResponse = new JTableResponse("OK", availableSecondaryStatus, availableSecondaryStatus.size());
		} catch (Exception e) {
	       	jTableResponse = new JTableResponse("ERROR","Error listing un mapped secondary status!");
	       	log.error("JSON ERROR", e);
	    }	        
		return jTableResponse;
    }
	
	@RequestMapping(value="secondary.status.available.for.primary.status.count",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse listSecondaryStatusAddedWithPrimaryStatus(@RequestParam Integer primaryStatusId, @RequestParam Integer statusId) {  
		log.debug("  ------------secondary.status.available.for.primary.status.count-----------");
		JTableSingleResponse jTableSingleResponse;
		
		Integer unMappedStatusCount = 0;		
		HashMap<String, Integer> unMappedStatusCountMap =new HashMap<String, Integer>();
		try {
			unMappedStatusCount = statusService.getSecondaryStatusToAddWithPrimaryStatusCount(primaryStatusId, statusId);
			unMappedStatusCountMap.put("unMappedTCCount", unMappedStatusCount);						
			jTableSingleResponse = new JTableSingleResponse("OK",unMappedStatusCountMap);
        } catch (Exception e) {
            jTableSingleResponse = new JTableSingleResponse("ERROR","Unable to fetch unmapped secondary status count to primary status!");
            log.error("ERROR fetching unmapped secondary status", e);	 
        }
	        
        return jTableSingleResponse;
    }
	
	@RequestMapping(value="secondary.status.list.for.primary.status",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse getSecondaryStatusMappedWithPrimaryStatus(@RequestParam Integer primaryStatusId, @RequestParam Integer statusId, @RequestParam Integer jtStartIndex, @RequestParam Integer jtPageSize) {
		log.debug("inside secondary.status.list.for.primary.status");
		JTableResponse jTableResponse;
		 
		try {
			List<Object[]> activitySecondaryStatusMasters = statusService.getSecondaryStatusMappedWithPrimaryStatus(primaryStatusId, statusId);
			ArrayList<HashMap<String, Object>> mappedSecondaryStatus = new ArrayList<HashMap<String, Object>>();
			if(activitySecondaryStatusMasters!=null){
				for (Object[] row : activitySecondaryStatusMasters) {
					HashMap<String, Object> secondaryStatus =new HashMap<String, Object>();
					secondaryStatus.put(IDPAConstants.ITEM_ID, (Integer)row[0]);
					secondaryStatus.put(IDPAConstants.ITEM_NAME, (String)row[1]);					
					mappedSecondaryStatus.add(secondaryStatus);					
				}				
			}
			jTableResponse = new JTableResponse("OK", mappedSecondaryStatus, mappedSecondaryStatus.size());
	           
        } catch (Exception e) {
            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
            log.error("JSON ERROR", e);
        }
	        
        return jTableResponse;
	}
	
	@RequestMapping(value="secondary.status.for.primary.status.mapping",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse mapOrUnmapSecondaryStatusWithPrimaryStatus(@RequestParam Integer primaryStatusId, @RequestParam Integer secondaryStatusId, @RequestParam String maporunmap, HttpServletRequest request) {  
		log.debug("  ------------secondary.status.for.primary.status.mapping-----------");
		JTableSingleResponse jTableSingleResponse;
		
		try {
			if(maporunmap == null || maporunmap.isEmpty() || maporunmap.equalsIgnoreCase("map")){
				String errorMessage = ValidationUtility.validateForSecondaryStatusMappingForPrimaryStatus(primaryStatusId, secondaryStatusId, statusService);			
				if (errorMessage != null) {
					jTableSingleResponse = new JTableSingleResponse("ERROR",errorMessage);
					return jTableSingleResponse;
				}
			}
			
			statusService.mapOrUnmapSecondaryStatusForPrimaryStatus(primaryStatusId, secondaryStatusId, maporunmap);			
	        jTableSingleResponse = new JTableSingleResponse("OK", "");
	    } catch (Exception e) {
	        jTableSingleResponse = new JTableSingleResponse("ERROR","Error while adding secondary status to primary status!");
	        log.error("JSON ERROR", e);
	    }	        
        return jTableSingleResponse;		
    }
	
	@RequestMapping(value="status.category.option.list", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody JTableResponseOptions listAllStatusCategories(){
		JTableResponseOptions jTableResponseOptions = null;
		List<StatusCategory> statusCategories = new ArrayList<StatusCategory>();
		try{
			statusCategories = statusService.listAllStatusCategories();
			List<JsonStatusCategory> jsonStatusCategories = new ArrayList<JsonStatusCategory>();
			if(statusCategories != null && statusCategories.size() > 0){
				for(StatusCategory statusCategory : statusCategories){
					jsonStatusCategories.add(new JsonStatusCategory(statusCategory));
				}
			}
				jTableResponseOptions = new JTableResponseOptions("OK", jsonStatusCategories, true);
				return jTableResponseOptions;
		
		}catch (Exception e) {
        	jTableResponseOptions = new JTableResponseOptions("ERROR","Please map status to the product.");
            log.error("JSON ERROR", e);
        }	
		jTableResponseOptions = new JTableResponseOptions("ERROR","Please map status to the product.");
		log.info("jTableResponseOptions success");
		return jTableResponseOptions;
	}
}
