package com.hcl.atf.taf.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.common.events.EventsMasterRef;
import com.hcl.atf.taf.dao.EventsDAO;
import com.hcl.atf.taf.model.DefectManagementSystem;
import com.hcl.atf.taf.model.DeviceList;
import com.hcl.atf.taf.model.EntityMaster;
import com.hcl.atf.taf.model.Event;
import com.hcl.atf.taf.model.HostList;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.TestManagementSystem;
import com.hcl.atf.taf.model.TestRunConfigurationChild;
import com.hcl.atf.taf.model.TestRunList;
import com.hcl.atf.taf.model.TestSuiteList;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.service.EventsService;
import com.hcl.atf.taf.service.UserListService;
import com.hcl.atf.taf.service.WorkPackageService;

@Service
public class EventsServiceImpl implements EventsService{

	private static final Log log = LogFactory.getLog(EventsServiceImpl.class);
	@Autowired
	private UserListService userListService;
	@Autowired
	private WorkPackageService workPackageService;	
	@Autowired
    private EventsDAO eventDAO;
	
	@Override
	@Transactional
	public Integer raiseEvent(
			String eventSourceComponent, 
			String eventName, String eventDescription, 
			Double eventData, Double additonalEventData, Double additonalEventData2, 
			String eventCategory, String eventType,
			String sourceEntityType, Integer sourceEntityId, String sourceEntityName,
			String targetEntityType, Integer targetEntityId, String targetEntityName,
			String additionalTargetEntityType, Integer additionalTargetEntityId, String additionalTargetEntityName, 
			Integer eventStatus,
			Date startTime, Date endTime
			){
		
		Event event = new Event(
				eventSourceComponent, 
				eventName, eventDescription,
				eventData, additonalEventData, additonalEventData2,
				eventCategory, eventType,
				sourceEntityType, sourceEntityId, sourceEntityName,
				targetEntityType, targetEntityId, targetEntityName,
				additionalTargetEntityType, additionalTargetEntityId, additionalTargetEntityName,
				eventStatus,
				startTime, endTime
				);
			
			return eventDAO.addEvent(event);
	}


	
	@Override
	@Transactional
	public Integer raiseDeviceConnectedEvent(
			DeviceList device,
			String eventDescription
			) {
		
			Event event = new Event(
				EventsMasterRef.EventSource.TAF.getValue(), 
				EventsMasterRef.DeviceEvents.CONNECTED.getValue(), eventDescription,
				new Double(1), null, null,
				EventsMasterRef.EventCategory.TEST_INFRASTRUCTURE_MANAGEMENT.getValue(), EventsMasterRef.EventType.DEVICE.getValue(),
				EventsMasterRef.EntityType.DEVICE.getValue(), device.getDeviceListId(), device.getDeviceId(),
				EventsMasterRef.EntityType.TERMINAL.getValue(), device.getHostList().getHostId(), device.getHostList().getHostName(),
				null, null, null,
				1,
				new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis())
				);
			
			return eventDAO.addEvent(event);
	}
	
	@Override
	@Transactional
	public Integer raiseDeviceDisconnectedEvent(
			DeviceList device,
			String eventDescription 
			) {
		
			Event event = new Event(
				EventsMasterRef.EventSource.TAF.getValue(), 
				EventsMasterRef.DeviceEvents.DISCONNECTED.getValue(), eventDescription,
				new Double(1), null, null,
				EventsMasterRef.EventCategory.TEST_INFRASTRUCTURE_MANAGEMENT.getValue(), EventsMasterRef.EventType.DEVICE.getValue(),
				EventsMasterRef.EntityType.DEVICE.getValue(), device.getDeviceListId(), device.getDeviceId(),
				EventsMasterRef.EntityType.TERMINAL.getValue(), device.getHostList().getHostId(), device.getHostList().getHostName(),
				null, null, null,
				1,
				new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis())
				);
			
			return eventDAO.addEvent(event);
	}
	
	@Override
	@Transactional
	public Integer raiseNewDeviceRegisteredEvent(
			DeviceList device,
			String eventDescription 
			) {
		
		Event event = new Event(
				EventsMasterRef.EventSource.TAF.getValue(), 
				EventsMasterRef.DeviceEvents.NEW_REGISTERED.getValue(), eventDescription,
				new Double(1), null, null,
				EventsMasterRef.EventCategory.TEST_INFRASTRUCTURE_MANAGEMENT.getValue(), EventsMasterRef.EventType.DEVICE.getValue(),
				EventsMasterRef.EntityType.DEVICE.getValue(), device.getDeviceListId(), device.getDeviceId(),
				EventsMasterRef.EntityType.TERMINAL.getValue(), device.getHostList().getHostId(), device.getHostList().getHostName(),
				null, null, null,
				1,
				new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis())
				);
			
			return eventDAO.addEvent(event);
	}

	@Override
	@Transactional
	public Integer raiseDeviceDeregisteredEvent(
			DeviceList device,
			String eventDescription 
			) {
		
		Event event = new Event(
				EventsMasterRef.EventSource.TAF.getValue(), 
				EventsMasterRef.DeviceEvents.DEREGISTERED.getValue(), eventDescription,
				new Double(1), null, null,
				EventsMasterRef.EventCategory.TEST_INFRASTRUCTURE_MANAGEMENT.getValue(), EventsMasterRef.EventType.DEVICE.getValue(),
				EventsMasterRef.EntityType.DEVICE.getValue(), device.getDeviceListId(), device.getDeviceId(),
				EventsMasterRef.EntityType.TERMINAL.getValue(), device.getHostList().getHostId(), device.getHostList().getHostName(),
				null, null, null,
				1,
				new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis())
				);
			
			return eventDAO.addEvent(event);
	}

	@Override
	@Transactional
	public Integer raiseDeviceTerminalChangedEvent(
			DeviceList device,
			String eventDescription 
			) {
		
		Event event = new Event(
				EventsMasterRef.EventSource.TAF.getValue(), 
				EventsMasterRef.DeviceEvents.TERMINAL_CHANGED.getValue(), eventDescription,
				new Double(1), null, null,
				EventsMasterRef.EventCategory.TEST_INFRASTRUCTURE_MANAGEMENT.getValue(), EventsMasterRef.EventType.DEVICE.getValue(),
				EventsMasterRef.EntityType.DEVICE.getValue(), device.getDeviceListId(), device.getDeviceId(),
				EventsMasterRef.EntityType.TERMINAL.getValue(), device.getHostList().getHostId(), device.getHostList().getHostName(),
				null, null, null,
				1,
				new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis())
				);
			
			return eventDAO.addEvent(event);
	}
	
	@Override
	@Transactional
	public Integer raiseDeviceUsedEvent(DeviceList device,
			TestRunList testRunList, String eventDescription) {

		Event event = new Event(
				EventsMasterRef.EventSource.TAF.getValue(), 
				EventsMasterRef.DeviceEvents.USED.getValue(), eventDescription,
				new Double(1), null, null,
				EventsMasterRef.EventCategory.TEST_INFRASTRUCTURE_MANAGEMENT.getValue(), EventsMasterRef.EventType.DEVICE.getValue(),
				EventsMasterRef.EntityType.DEVICE.getValue(), device.getDeviceListId(), device.getDeviceId(),
				null, null, null,
				null, null, null,
				1,
				new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis())
				);
			
			return eventDAO.addEvent(event);
	}

	@Override
	@Transactional
	public Integer raiseTerminalConnectedEvent(
			HostList host,
			String eventDescription 
			) {
		
			Event event = new Event(
				EventsMasterRef.EventSource.TAF.getValue(), 
				EventsMasterRef.TerminalEvents.CONNECTED.getValue(), eventDescription,
				new Double(1), null, null,
				EventsMasterRef.EventCategory.TEST_INFRASTRUCTURE_MANAGEMENT.getValue(), EventsMasterRef.EventType.DEVICE.getValue(),
				EventsMasterRef.EntityType.TERMINAL.getValue(), host.getHostId(), host.getHostName(),
				null, null, null,
				null, null, null,
				1,
				new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis())
				);
			
			return eventDAO.addEvent(event);
	}
	
	@Override
	@Transactional
	public Integer raiseTerminalDisconnectedEvent(
			HostList host,
			String eventDescription 
			) {
		
			Event event = new Event(
				EventsMasterRef.EventSource.TAF.getValue(), 
				EventsMasterRef.TerminalEvents.DISCONNECTED.getValue(), eventDescription,
				new Double(1), null, null,
				EventsMasterRef.EventCategory.TEST_INFRASTRUCTURE_MANAGEMENT.getValue(), EventsMasterRef.EventType.DEVICE.getValue(),
				EventsMasterRef.EntityType.TERMINAL.getValue(), host.getHostId(), host.getHostName(),
				null, null, null,
				null, null, null,
				1,
				new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis())
				);
			
			return eventDAO.addEvent(event);
	}
	
	@Override
	@Transactional
	public Integer raiseNewTerminalRegisteredEvent(
			HostList host,
			String eventDescription 
			) {
		
			Event event = new Event(
				EventsMasterRef.EventSource.TAF.getValue(), 
				EventsMasterRef.TerminalEvents.NEW_REGISTERED.getValue(), eventDescription,
				new Double(1), null, null,
				EventsMasterRef.EventCategory.TEST_INFRASTRUCTURE_MANAGEMENT.getValue(), EventsMasterRef.EventType.DEVICE.getValue(),
				EventsMasterRef.EntityType.TERMINAL.getValue(), host.getHostId(), host.getHostName(),
				null, null, null,
				null, null, null,
				1,
				new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis())
				);
			
			return eventDAO.addEvent(event);
	}

	@Override
	@Transactional
	public Integer raiseTerminalDeregisteredEvent(
			HostList host,
			String eventDescription 
			) {
		
			Event event = new Event(
				EventsMasterRef.EventSource.TAF.getValue(), 
				EventsMasterRef.TerminalEvents.DEREGISTERED.getValue(), eventDescription,
				new Double(1), null, null,
				EventsMasterRef.EventCategory.TEST_INFRASTRUCTURE_MANAGEMENT.getValue(), EventsMasterRef.EventType.DEVICE.getValue(),
				EventsMasterRef.EntityType.TERMINAL.getValue(), host.getHostId(), host.getHostName(),
				null, null, null,
				null, null, null,
				1,
				new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis())
				);
			
			return eventDAO.addEvent(event);
	}

	@Override
	@Transactional
	public Integer raiseTestRunExecutedEvent(
			TestRunConfigurationChild trcc,
			String eventDescription,
			Double eventData, Double additonalEventData, Double additonalEventData2,
			Date startTime, Date endTime
			) {
		
			Event event = new Event(
				EventsMasterRef.EventSource.TAF.getValue(), 
				EventsMasterRef.TestExecutionEvents.TESTRUN_CONFIGURATION_EXECUTED_EVENT.getValue(), eventDescription,
				eventData, additonalEventData, additonalEventData2,
				EventsMasterRef.EventCategory.TEST_EXECUTION.getValue(), EventsMasterRef.EventType.JOB_EXECUTION.getValue(),
				EventsMasterRef.EntityType.TESTRUN.getValue(), trcc.getTestRunConfigurationChildId(), trcc.getTestRunConfigurationName(),
				null, null, null,
				EventsMasterRef.EntityType.PRODUCT.getValue(), trcc.getProductVersionListMaster().getProductMaster().getProductId(), trcc.getProductVersionListMaster().getProductMaster().getProductName(),
				1,
				startTime, endTime
				);
			
			return eventDAO.addEvent(event);
	}

	@Override
	@Transactional
	public Integer raiseTestRunAbortedEvent(
			TestRunConfigurationChild trcc,
			String eventDescription
			) {
		
			Event event = new Event(
				EventsMasterRef.EventSource.TAF.getValue(), 
				EventsMasterRef.TestExecutionEvents.TESTRUN_CONFIGURATION_ABORTED_EVENT.getValue(), eventDescription,
				new Double(1), null, null,
				EventsMasterRef.EventCategory.TEST_EXECUTION.getValue(), EventsMasterRef.EventType.JOB_EXECUTION.getValue(),
				EventsMasterRef.EntityType.TESTRUN.getValue(), trcc.getTestRunConfigurationChildId(), trcc.getTestRunConfigurationName(),
				null, null, null,
				EventsMasterRef.EntityType.PRODUCT.getValue(), trcc.getProductVersionListMaster().getProductMaster().getProductId(), trcc.getProductVersionListMaster().getProductMaster().getProductName(),
				1,
				new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis())
				);
			
			return eventDAO.addEvent(event);
	}


	@Override
	@Transactional
	public Integer raiseTestRunJobExecutedEvent(
			TestRunList trl,
			String eventDescription
			) {
		
			Event event = new Event(
				EventsMasterRef.EventSource.TAF.getValue(), 
				EventsMasterRef.TestExecutionEvents.TESTRUN_JOB_EXECUTED_EVENT.getValue(), eventDescription,
				new Double(1), new Double(trl.getTestExecutionResults().size()), null,
				EventsMasterRef.EventCategory.TEST_EXECUTION.getValue(), EventsMasterRef.EventType.JOB_EXECUTION.getValue(),
				EventsMasterRef.EntityType.TESTRUN_JOB.getValue(), trl.getTestRunListId(), trl.getTestRunListId().toString(),
				EventsMasterRef.EntityType.DEVICE.getValue(), trl.getDeviceList().getDeviceListId(), trl.getDeviceList().getDeviceId(),
				EventsMasterRef.EntityType.PRODUCT.getValue(), trl.getTestRunConfigurationChild().getProductVersionListMaster().getProductMaster().getProductId(), trl.getTestRunConfigurationChild().getProductVersionListMaster().getProductMaster().getProductName(),
				1,
				trl.getTestRunStartTime(), trl.getTestRunEndTime()
				);
			
			return eventDAO.addEvent(event);
	}

	@Override
	@Transactional
	public Integer raiseTestResultsExportedEvent(
			TestRunList trl, TestManagementSystem testManagementSystem, Integer testResultsCount,  
			String eventDescription
			) {
		
			
			
			Event event = new Event(
				EventsMasterRef.EventSource.TAF.getValue(), 
				EventsMasterRef.TestReportingEvents.TESTRUN_TESTRESULTS_EXPORTED_EVENT.getValue(), eventDescription,
				new Double(testResultsCount), new Double(trl.getTestExecutionResults().size()), null,
				EventsMasterRef.EventCategory.TEST_REPORTING.getValue(), EventsMasterRef.EventType.INTEGRATION_TESTMANAGEMENTSYSTEM.getValue(),
				EventsMasterRef.EntityType.TESTRUN_JOB.getValue(), trl.getTestRunListId(), trl.getTestRunConfigurationChild().getTestRunConfigurationName(),
				EventsMasterRef.EntityType.TESTMANAGEMENTSYSTEM.getValue(),testManagementSystem.getTestManagementSystemId(), testManagementSystem.getTestSystemName(),
				EventsMasterRef.EntityType.PRODUCT.getValue(), trl.getTestRunConfigurationChild().getProductVersionListMaster().getProductMaster().getProductId(), trl.getTestRunConfigurationChild().getProductVersionListMaster().getProductMaster().getProductName(),
				1,
				trl.getTestRunStartTime(), trl.getTestRunEndTime()
				);
			
			return eventDAO.addEvent(event);
	}
	
	@Override
	@Transactional
	public Integer raiseTestCasesImportedEvent(
			 ProductMaster product, TestManagementSystem testManagementSystem, Integer testCasesCount,  
			String eventDescription
			) {
		
			
			
			Event event = new Event(
				EventsMasterRef.EventSource.TAF.getValue(), 
				EventsMasterRef.PlanningEvents.TESTMANAGEMENT_TESTCASES_IMPORTED_EVENT.getValue(), eventDescription,
				new Double(testCasesCount), new Double(testCasesCount), null,
				EventsMasterRef.EventCategory.TEST_PLANNING.getValue(), EventsMasterRef.EventType.INTEGRATION_TESTMANAGEMENTSYSTEM.getValue(),
				EventsMasterRef.EntityType.PRODUCT.getValue(),product.getProductId(), product.getProductName(),
				EventsMasterRef.EntityType.TESTMANAGEMENTSYSTEM.getValue(),testManagementSystem.getTestManagementSystemId(), testManagementSystem.getTestSystemName(),
				EventsMasterRef.EntityType.PRODUCT.getValue(), product.getProductId(), product.getProductName(),
				1,
				new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis())
				);
			
			return eventDAO.addEvent(event);
	}
	
	@Override
	@Transactional
	public Integer raiseTestSuiteImportedEvent(
			TestSuiteList tsl, TestManagementSystem testManagementSystem, Integer testCasesCount,  
			String eventDescription
			) {
		
			
			
			Event event = new Event(
				EventsMasterRef.EventSource.TAF.getValue(), 
				EventsMasterRef.PlanningEvents.TESTMANAGEMENT_TESTSUITES_IMPORTED_EVENT.getValue(), eventDescription,
				new Double(testCasesCount), new Double(testCasesCount), null,
				EventsMasterRef.EventCategory.TEST_PLANNING.getValue(), EventsMasterRef.EventType.INTEGRATION_TESTMANAGEMENTSYSTEM.getValue(),
				EventsMasterRef.EntityType.TESTSUITE.getValue(), tsl.getTestSuiteId(), tsl.getTestSuiteName(),
				EventsMasterRef.EntityType.TESTMANAGEMENTSYSTEM.getValue(),testManagementSystem.getTestManagementSystemId(), testManagementSystem.getTestSystemName(),
				EventsMasterRef.EntityType.PRODUCT.getValue(), tsl.getProductMaster().getProductId(), tsl.getProductMaster().getProductName(),
				1,
				new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis())
				);
			
			return eventDAO.addEvent(event);
	}

	
	@Override
	@Transactional
	public Integer raiseTestDefectsExportedEvent(
			TestRunList trl, DefectManagementSystem defectManagementSystem, Integer defectsCount, Integer exportedDefectsCount, 
			String eventDescription
			) {
		
			
			
			Event event = new Event(
				EventsMasterRef.EventSource.TAF.getValue(), 
				EventsMasterRef.TestReportingEvents.TESTRUN_DEFECTS_REPORTED_EVENT.getValue(), eventDescription,
				new Double(defectsCount), new Double(exportedDefectsCount), null,
				EventsMasterRef.EventCategory.TEST_REPORTING.getValue(), EventsMasterRef.EventType.INTEGRATION_TESTMANAGEMENTSYSTEM.getValue(),
				EventsMasterRef.EntityType.TESTRUN_JOB.getValue(), trl.getTestRunListId(), trl.getTestRunConfigurationChild().getTestRunConfigurationName(),
				EventsMasterRef.EntityType.DEFECTMANAGEMENTSYSTEM.getValue(),defectManagementSystem.getDefectManagementSystemId(), defectManagementSystem.getToolIntagration().getName(),
				EventsMasterRef.EntityType.PRODUCT.getValue(), trl.getTestRunConfigurationChild().getProductVersionListMaster().getProductMaster().getProductId(), trl.getTestRunConfigurationChild().getProductVersionListMaster().getProductMaster().getProductName(),
				1,
				trl.getTestRunStartTime(), trl.getTestRunEndTime()
				);
			
			return eventDAO.addEvent(event);
	}



	@Override
	@Transactional
	public List<Event> getEventsByFilter(String eventSourceComponent,
			String eventName, String eventCategory, String eventType,
			String sourceEntityType, Integer sourceEntityId,
			String sourceEntityName, Integer eventStatus, Date startTime,
			Date endTime) {
		return eventDAO.getEventsByFilter(eventSourceComponent, eventName, eventCategory, eventType, sourceEntityType, sourceEntityId, sourceEntityName, eventStatus, startTime, endTime);
	}



	@Override
	@Transactional
	public Event getEventById(int eventId) {
		return eventDAO.getEventById(eventId);
	}



	@Override
	@Transactional
	public List<Event> list(int startIndex, int pageSize) {
		return eventDAO.list(startIndex, pageSize);
	}



	@Override
	@Transactional
	public List<Event> list() {
		return eventDAO.list();
	}



	@Override
	@Transactional
	public Integer update(Event event) {
		return eventDAO.update(event);
	}



	@Override
	@Transactional
	public int getTotalRecordCount() {
		return eventDAO.getTotalRecordCount();
	}
	

	@Override
	@Transactional
	public int addNewEntityEvent(String entityTypeName, Integer entityInstanceId, String entityInstanceName, UserList user) {		
		int userId=user.getUserId();
		int eventId = 0;
		try {
			user = userListService.getUserListById(userId);
			EntityMaster entityMaster = workPackageService.getEntityMasterByName(entityTypeName);	
			if(entityMaster != null){
				eventId = eventDAO.addNewEntityEvent(entityMaster.getEntitymasterid(), entityTypeName, entityInstanceId, entityInstanceName, user);
			}else{
				log.error("Event addition failed. Entity type - "+entityTypeName+" is not available, Please check the entityTypeName passed");
			}
			return eventId;
		} catch (Exception e) {
			log.error("ERROR", e);
		}
		return eventId;
	}


	
	@Override
	@Transactional
	public int addEntityChangedEvent(String entityTypeName, Integer entityInstanceId, String entityInstanceName, 
			String fieldName, String modifiedFieldTitle, String fieldValue, String newFieldValue, UserList user, String remarks) {
		log.info("addEntityChangedEvent Service");		
		int eventId = 0;
		try {
			if(fieldName != null && !fieldName.equalsIgnoreCase("")){
				if(modifiedFieldTitle != null && !modifiedFieldTitle.equalsIgnoreCase("")){
					if(fieldValue != null && newFieldValue != null ){					
						if(!fieldValue.equalsIgnoreCase("") || !newFieldValue.equalsIgnoreCase("")){//If only modified, event is tracked.
							if(!(fieldValue.trim().equals(newFieldValue.trim()))){
								int userId=user.getUserId();
								user = userListService.getUserListById(userId);			
								EntityMaster entityMaster = workPackageService.getEntityMasterByName(entityTypeName);
								if(entityMaster != null){
									eventId =  eventDAO.addEntityChangedEvent( entityMaster.getEntitymasterid(),  entityTypeName,  entityInstanceId,  entityInstanceName,  fieldName,  fieldValue,  newFieldValue,  user, remarks);
								}else{
									log.error("Event addition failed. Entity type - "+entityTypeName+" is not available, Please check the entityTypeName passed for Instance - "+entityInstanceName+" ["+entityInstanceId+"]");
								}
								return eventId;
							}
						}
					}		
				}				
			}
		} catch (Exception e) {
			log.error("ERROR", e);
		}
		return eventId;		
	}

	@Override
	@Transactional
	public List<Event> listEventsBySourceEntityType(String sourceEntityType,
			int sourceEntityId, int jtStartIndex, int jtPageSize) {
		return eventDAO.listEventsBySourceEntityType(sourceEntityType, sourceEntityId, jtStartIndex, jtPageSize);
	}	

	@Override
	@Transactional
	public Integer getEventTotalCount(String sourceEntityType,
			Integer sourceEntityId, String eventSourceComponent, int jtStartIndex, int jtPageSize) {
		return eventDAO.getEventTotalCount(sourceEntityType, sourceEntityId, eventSourceComponent, jtStartIndex, jtPageSize);
	}
	
	@Override
	@Transactional
	public int addEntityRemovedEvent(String entityTypeName, Integer entityInstanceId, String entityInstanceName, 
			String fieldName, String modifiedFieldTitle, String fieldValue, String newFieldValue, UserList user, String remarks) {
		log.info("addEntityRemovedEvent Service");		
		int eventId = 0;
		try {
			if(fieldName != null && !fieldName.equalsIgnoreCase("")){
				if(modifiedFieldTitle != null && !modifiedFieldTitle.equalsIgnoreCase("")){
					if(fieldValue != null && newFieldValue != null ){					
						if(!fieldValue.equalsIgnoreCase("") || !newFieldValue.equalsIgnoreCase("")){//If only modified, event is tracked.
							if(!(fieldValue.trim().equals(newFieldValue.trim()))){
								int userId=user.getUserId();
								user = userListService.getUserListById(userId);			
								EntityMaster entityMaster = workPackageService.getEntityMasterByName(entityTypeName);
								if(entityMaster != null){
									eventId =  eventDAO.addEntityRemovedEvent(entityMaster.getEntitymasterid(),  entityTypeName,  entityInstanceId,  entityInstanceName,  fieldName,  fieldValue,  newFieldValue,  user, remarks);
								}else{
									log.error("Event addition failed. Entity type - "+entityTypeName+" is not available, Please check the entityTypeName passed for Instance - "+entityInstanceName+" ["+entityInstanceId+"]");
								}
								return eventId;
							}
						}
					}		
				}				
			}
		} catch (Exception e) {
			log.error("ERROR", e);
		}
		return eventId;
	}

	@Override
	@Transactional
	public int addEntityMappingAddedEvent(Integer sourceEntityTypeId, Integer sourceEntityInstanceId, String sourceEntityInstanceName, Integer targetEntityTypeId, Integer targetEntityInstanceId, String targetEntityInstanceName, UserList user) {
		return eventDAO.getTotalRecordCount();
	}
	
	@Override
	@Transactional
	public int addEntityMappingRemovedEvent(Integer sourceEntityTypeId, Integer sourceEntityInstanceId, String sourceEntityInstanceName, Integer targetEntityTypeId, Integer targetEntityInstanceId, String targetEntityInstanceName, UserList user) {
		return eventDAO.getTotalRecordCount();
	}





	@Override
	@Transactional
	public List<Event> getEventsOfIds(String enityType, List<Integer> instanceIds) {
		return eventDAO.getEventsOfIds(enityType, instanceIds);
	}

	@Override
	@Transactional
	public List<Event> listEventsByUserId(int userId, String currentDate, int jtStartIndex, int jtPageSize){		
		return eventDAO.listEventsByUserId(userId, currentDate, jtStartIndex, jtPageSize);
	}


	@Override
	@Transactional
	public List<Event> listBotCommandEventsBySourceEntityType(int botEventId, int jtStartIndex, int jtPageSize) {
		return eventDAO.listBotCommandEventsBySourceEntityType(botEventId, jtStartIndex, jtPageSize);
	}



	@Override
	public int getBotCommandEventTotalCount(Integer botEventId,String sourceComponentIlcm, int jtStartIndex, int jtPageSize) {
		try {
			return eventDAO.getBotCommandEventTotalCount(botEventId, sourceComponentIlcm, jtStartIndex, jtPageSize);
		}catch(Exception e) {
			log.error("Error in getBotCommandEventTotalCount",e);
		}
		return 0;
	}	
}