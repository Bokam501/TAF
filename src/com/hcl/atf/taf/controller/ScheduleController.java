package com.hcl.atf.taf.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.json.JsonSchedule;
import com.hcl.atf.taf.model.json.jtable.JTableSingleResponse;
import com.hcl.atf.taf.service.SchedulerService;

@Controller
public class ScheduleController {


	private static final Log log = LogFactory.getLog(ScheduleController.class);
	
	@Autowired
	private SchedulerService schedulerService;

	@RequestMapping(value="entity.schedule.addupdate",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableSingleResponse addUpdateSchedule(HttpServletRequest request, @RequestParam int entityId, @RequestParam String entityType, @RequestParam String cronExpression, @RequestParam String startDate, @RequestParam String endDate) {
		
		log.debug("entity.schedule.addupdate");

		JTableSingleResponse jTableSingleResponse = null;
		log.info("Schedule Add/Update : Entyity Type : " + entityType + " : Entity ID : " + entityId + " : Dates : " + startDate + " - " + endDate);
		if (entityId < 0 || entityType == null) {
			jTableSingleResponse  = new JTableSingleResponse("ERROR","The Schedule has insufficient details for adding/updating"); 
	        return jTableSingleResponse;
		} 
		
		try {

			JsonSchedule jsonSchedule = new JsonSchedule();

			jsonSchedule.setOwnerEntityId(entityId);
			jsonSchedule.setOwnerEntityType(entityType);
			jsonSchedule.setCronExpression(cronExpression);
			jsonSchedule.setStartDate(startDate);
			jsonSchedule.setEndDate(endDate);
		
			jsonSchedule = schedulerService.addUpdateSchedule(jsonSchedule);
			if (jsonSchedule != null) {
				jTableSingleResponse = new JTableSingleResponse("OK",jsonSchedule);				
			} else {
				jTableSingleResponse = new JTableSingleResponse("ERROR", "Unable to add/update schedule");
			}
		} catch (Exception e) {

			log.error("Unable to add/update schedule", e);
			jTableSingleResponse = new JTableSingleResponse("ERROR", "Unable to add/update schedule");
		}
        return jTableSingleResponse;
	}
	
	@RequestMapping(value="entity.schedule.delete",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableSingleResponse deleteSchedule(HttpServletRequest request, @RequestParam int entityId, @RequestParam String entityType) {//, @RequestParam String cronExpression, @RequestParam String startDate, @RequestParam String endDate) {
		
		log.debug("entity.schedule.delete");

		JTableSingleResponse jTableSingleResponse = null;
		log.info("Schedule delete : Entyity Type : " + entityType + " : Entity ID : " + entityId);
		if (entityId < 0 || entityType == null) {
			jTableSingleResponse  = new JTableSingleResponse("ERROR","The Schedule has insufficient details for Deleting"); 
	        return jTableSingleResponse;
		} 
		
		try {

			JsonSchedule jsonSchedule = new JsonSchedule();

			jsonSchedule.setOwnerEntityId(entityId);
			jsonSchedule.setOwnerEntityType(entityType);
			jsonSchedule.setCronExpression("");
			jsonSchedule.setEndDate(DateUtility.dateformatWithSlashWithOutTime(new Date()));
		
			jsonSchedule = schedulerService.deleteSchedule(jsonSchedule);
			if (jsonSchedule != null) {
				jTableSingleResponse = new JTableSingleResponse("OK",jsonSchedule);				
			} else {
				jTableSingleResponse = new JTableSingleResponse("ERROR", "Unable to delete schedule");
			}
		} catch (Exception e) {

			log.error("Unable to delete schedule", e);
			jTableSingleResponse = new JTableSingleResponse("ERROR", "Unable to delete schedule");
		}
        return jTableSingleResponse;
	}

	
	@RequestMapping(value="entity.schedule.view",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableSingleResponse viewSchedule(HttpServletRequest request, @RequestParam int entityId, @RequestParam String entityType) {
		
		log.debug("entity.schedule.view");
		JTableSingleResponse jTableSingleResponse = null;
		
		log.info("View Schedule for : Entyity Type : " + entityType + " : Entity ID : " + entityId);
		if (entityId <= 0 || entityType ==  null) {
			jTableSingleResponse  = new JTableSingleResponse("ERROR","Entity id or entity type is missing"); 
	        return jTableSingleResponse;
		} 
		
		try {
			JsonSchedule jsonSchedule = schedulerService.viewEntitySchedule(entityId, entityType);
			if (jsonSchedule != null) {
				jTableSingleResponse = new JTableSingleResponse("OK",jsonSchedule);				
			} else {
				jTableSingleResponse = new JTableSingleResponse("ERROR", "Unable to display schedule");
			}
		} catch (Exception e) {
			log.error("Unable to get Schedule Details", e);
			jTableSingleResponse = new JTableSingleResponse("ERROR", "Unable to display schedule");
		}
		return jTableSingleResponse;
	}
	
		
	@RequestMapping(value="entity.schedule.view.firetimes",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableSingleResponse viewScheduleFireTimes(HttpServletRequest request, @RequestParam int entityId, @RequestParam String entityType) {
		
		JTableSingleResponse jTableSingleResponse = null;
		
		log.debug("entity.schedule.addupdate");
		if (entityId <= 0 || entityType ==  null) {
			jTableSingleResponse  = new JTableSingleResponse("ERROR","Entity id or entity type is missing"); 
	        return jTableSingleResponse;
		} 
		
		try {
			JsonSchedule jsonSchedule = schedulerService.viewUpcomingFireTimes(entityId, entityType, 20);
			if (jsonSchedule != null) {
				jTableSingleResponse = new JTableSingleResponse("OK",jsonSchedule);				
			} else {
				jTableSingleResponse = new JTableSingleResponse("ERROR", "Unable to display schedule firetimes");
			}
		} catch (Exception e) {
			log.error("Unable to get Schedule firetimes", e);
			jTableSingleResponse = new JTableSingleResponse("ERROR", "Unable to display schedule firetimes");
		}
		return jTableSingleResponse;
	}
	
	@RequestMapping(value="entity.schedule.run.now",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableSingleResponse runScheduleNow(HttpServletRequest request, @RequestParam Integer entityId, @RequestParam String entityType) {
		
		log.debug("entity.schedule.run.now");

		JTableSingleResponse jTableSingleResponse = null;
		log.info("Run schedule job for entity id - " + entityId);
		if (entityId == null || entityId < 0) {
			jTableSingleResponse  = new JTableSingleResponse("ERROR","The Schedule has insufficient details to trigger now"); 
	        return jTableSingleResponse;
		} 
		
		try {
			JsonSchedule jsonSchedule = schedulerService.runJobNow(entityId, entityType);
			
			if (jsonSchedule != null) {
				jTableSingleResponse = new JTableSingleResponse("OK",jsonSchedule);				
			} else {
				jTableSingleResponse = new JTableSingleResponse("ERROR", "Unable to trigger now");
			}
		} catch (Exception e) {

			log.error("Unable to trigger now", e);
			jTableSingleResponse = new JTableSingleResponse("ERROR", "Unable to trigger now");
		}
        return jTableSingleResponse;
	}
}
