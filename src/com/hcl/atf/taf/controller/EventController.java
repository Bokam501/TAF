package com.hcl.atf.taf.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.model.Event;
import com.hcl.atf.taf.model.json.JsonEvent;
import com.hcl.atf.taf.model.json.jtable.JTableResponse;
import com.hcl.atf.taf.service.CommonService;
import com.hcl.atf.taf.service.EventsService;
@Controller
public class EventController {

	private static final Log log = LogFactory.getLog(EventController.class);
	
	@Autowired
	private EventsService eventsService;
	
	@Autowired
	private CommonService commonService;
	
	@RequestMapping(value="event.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listEvents(@RequestParam int jtStartIndex, @RequestParam int jtPageSize) {
		log.debug("inside event.list");
		JTableResponse jTableResponse;
			try {
			List<Event> events=eventsService.list(jtStartIndex, jtPageSize);
			List<JsonEvent> jsonEvents=new ArrayList<JsonEvent>();
			for(Event event:events){
				jsonEvents.add(new JsonEvent(event));	
			}
			log.info("inside the event.list controller events Size:"+jsonEvents.size());
	        jTableResponse = new JTableResponse("OK",jsonEvents,eventsService.getTotalRecordCount());  
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
	

	@RequestMapping(value="administration.event.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listEventsBySourceEntityType(@RequestParam String sourceEntityType, @RequestParam int sourceEntityId, @RequestParam int jtStartIndex, @RequestParam int jtPageSize, HttpServletRequest request) {	
		log.debug("----------------inside administration.event.list");		
		JTableResponse jTableResponse;			 
		try {				
			List<Event> eventList = eventsService.listEventsBySourceEntityType(sourceEntityType, sourceEntityId, jtStartIndex, jtPageSize);
			int totalComponentBasedEvent = eventsService.getEventTotalCount(sourceEntityType, sourceEntityId, IDPAConstants.SOURCE_COMPONENT_ILCM, -1, -1);
			 
			List<JsonEvent> jsonEventList = new ArrayList<JsonEvent>();
			if(eventList != null && !eventList.isEmpty()){
				Date modifiedAfterDate = commonService.getModifiedAfterDate((Date)request.getSession().getAttribute("userLoginTime"), (Date)request.getSession().getAttribute("userLogoutTime"));
				for (Event event : eventList) {
					JsonEvent jsonEvent = new JsonEvent(event);
					jsonEvent.setIsModified(commonService.isModifiedAfterDate(event.getEndTime(), modifiedAfterDate));
					jsonEventList.add(jsonEvent);
				}
			}									
			jTableResponse = new JTableResponse("OK", jsonEventList,totalComponentBasedEvent);			
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Unable to show Events!");
	            log.error("JSON ERROR", e);
	        }		        
        return jTableResponse;
    }
	
	@RequestMapping(value="administration.event.list.by.userId",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listEventsByUserId(@RequestParam int userId, @RequestParam int jtStartIndex, @RequestParam int jtPageSize, HttpServletRequest request) {	
		log.debug("----------------inside administration.event.list");		
		JTableResponse jTableResponse;			 
		try {			
			
			DateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-DD HH:MM:SS");
			String currentDate = simpleDateFormat.format(new Date());
			List<Event> eventList = eventsService.listEventsByUserId(userId, currentDate, jtStartIndex, jtPageSize);
			 
			List<JsonEvent> jsonEventList = new ArrayList<JsonEvent>();
			if(eventList != null && !eventList.isEmpty()){
				Date modifiedAfterDate = commonService.getModifiedAfterDate((Date)request.getSession().getAttribute("userLoginTime"), (Date)request.getSession().getAttribute("userLogoutTime"));
				for (Event event : eventList) {
					JsonEvent jsonEvent = new JsonEvent(event);
					jsonEvent.setIsModified(commonService.isModifiedAfterDate(event.getEndTime(), modifiedAfterDate));
					jsonEventList.add(jsonEvent);
				}
			}									
			jTableResponse = new JTableResponse("OK", jsonEventList,jsonEventList.size());			
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Unable to show Events!");
	            log.error("JSON ERROR", e);
	        }		        
        return jTableResponse;
    }

}
