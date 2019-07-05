package com.hcl.atf.taf.service;

import java.util.Date;
import java.util.List;

import com.hcl.atf.taf.model.DefectManagementSystem;
import com.hcl.atf.taf.model.DeviceList;
import com.hcl.atf.taf.model.Event;
import com.hcl.atf.taf.model.HostList;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.TestManagementSystem;
import com.hcl.atf.taf.model.TestRunConfigurationChild;
import com.hcl.atf.taf.model.TestRunList;
import com.hcl.atf.taf.model.TestSuiteList;
import com.hcl.atf.taf.model.UserList;

public interface EventsService {

	Integer raiseEvent(String eventSourceComponent, String eventName,
			String eventDescription, Double eventData, Double additonalEventData,
			Double additonalEventData2, String eventCategory, String eventType,
			String sourceEntityType, Integer sourceEntityId,
			String sourceEntityName, String targetEntityType,
			Integer targetEntityId, String targetEntityName,
			String additionalTargetEntityType,
			Integer additionalTargetEntityId,
			String additionalTargetEntityName, Integer eventStatus,
			Date startTime, Date endTime);

	Integer raiseDeviceConnectedEvent(DeviceList device,
			String eventDescription);

	Integer raiseDeviceDisconnectedEvent(DeviceList device,
			String eventDescription );

	Integer raiseNewDeviceRegisteredEvent(DeviceList device,
			String eventDescription);

	Integer raiseDeviceDeregisteredEvent(DeviceList device,
			String eventDescription);

	Integer raiseDeviceTerminalChangedEvent(DeviceList device,
			String eventDescription);

	Integer raiseDeviceUsedEvent(DeviceList device, TestRunList testRunList, String eventDescription);

	Integer raiseTerminalConnectedEvent(HostList host, String eventDescription);

	Integer raiseTerminalDisconnectedEvent(HostList host,
			String eventDescription);

	Integer raiseNewTerminalRegisteredEvent(HostList host,
			String eventDescription);

	Integer raiseTerminalDeregisteredEvent(HostList host,
			String eventDescription);

	Integer raiseTestRunJobExecutedEvent(TestRunList trl,
			String eventDescription);

	Integer raiseTestRunAbortedEvent(TestRunConfigurationChild trcc,
			String eventDescription);

	Integer raiseTestRunExecutedEvent(TestRunConfigurationChild trcc,
			String eventDescription, Double eventData, Double additonalEventData,
			Double additonalEventData2, Date startTime, Date endTime);

	Integer raiseTestResultsExportedEvent(TestRunList trl, TestManagementSystem testManagementSystem, Integer testResultsCount, String eventDescription);

	Integer raiseTestSuiteImportedEvent(TestSuiteList tsl, TestManagementSystem testManagementSystem, Integer testCasesCount, String eventDescription);

	Integer raiseTestCasesImportedEvent(ProductMaster product, TestManagementSystem testManagementSystem, Integer testCasesCount, String eventDescription);

	Integer raiseTestDefectsExportedEvent(TestRunList trl, DefectManagementSystem defectManagementSystem, Integer defectsCount, Integer exportedDefectsCount, String eventDescription);

	
	List<Event> getEventsByFilter(String eventSourceComponent, String eventName,
			String eventCategory, String eventType, String sourceEntityType, Integer sourceEntityId, 
			String sourceEntityName, Integer eventStatus,
			Date startTime, Date endTime);
	Event getEventById(int eventId);
    List<Event> list(int startIndex, int pageSize);
	List<Event> list();
	Integer update(Event event);
	int getTotalRecordCount();

	int addNewEntityEvent(String entityTypeName, Integer entityInstanceId, String entityInstanceName, UserList user);
	int addEntityChangedEvent(String entityTypeName, Integer entityInstanceId, String entityInstanceName, String fieldName, String modifiedFieldTitle, String fieldValue, String newFieldValue, UserList user, String remarks);
	List<Event> listEventsBySourceEntityType(String sourceEntityType, int sourceEntityId, int jtStartIndex, int jtPageSize);
	int addEntityRemovedEvent(String entityTypeName, Integer entityInstanceId, String entityInstanceName, 
			String fieldName, String modifiedFieldTitle, String fieldValue, String newFieldValue, UserList user, String remarks);
	int addEntityMappingAddedEvent(Integer sourceEntityTypeId, Integer sourceEntityInstanceId, String sourceEntityInstanceName, Integer targetEntityTypeId, Integer targetEntityInstanceId, String targetEntityInstanceName, UserList user);
	int addEntityMappingRemovedEvent(Integer sourceEntityTypeId, Integer sourceEntityInstanceId, String sourceEntityInstanceName, Integer targetEntityTypeId, Integer targetEntityInstanceId, String targetEntityInstanceName, UserList user);
	Integer getEventTotalCount(String sourceEntityType, Integer sourceEntityId, String eventSourceComponent, int jtStartIndex, int jtPageSize);

	List<Event> getEventsOfIds(String enityType, List<Integer> instanceIds);
	List<Event> listEventsByUserId(int userId, String currentDate, int jtStartIndex, int jtPageSize);

	List<Event> listBotCommandEventsBySourceEntityType(int botEventId, int jtStartIndex, int jtPageSize);

	int getBotCommandEventTotalCount(Integer botEventId,String sourceComponentIlcm, int i, int j);
	
	
	
}
