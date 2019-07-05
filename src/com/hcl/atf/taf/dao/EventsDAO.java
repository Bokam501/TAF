package com.hcl.atf.taf.dao;

import java.util.Date;
import java.util.List;

import com.hcl.atf.taf.model.Event;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.vo.EventVO;

public interface EventsDAO {

	Integer addEvent(Event event);
	Integer update(Event event);
	List<Event> list();
	List<Event> list(int startIndex, int pageSize);
	Event getEventById(int eventId);
	List<Event> getEventsByFilter(String eventSourceComponent,
			String eventName, String eventCategory, String eventType,
			String sourceEntityType, Integer sourceEntityId,
			String sourceEntityName, Integer eventStatus, Date startTime,
			Date endTime);
	List<EventVO> getAggregatedEvents(String eventSourceComponent,
			String sourceEntityType, Integer sourceEntityId, Date startTime,
			Date endTime);
	int getTotalRecordCount();
	int addNewEntityEvent(Integer entityTypeId, String entityTypeName, Integer entityInstanceId, String entityInstanceName, UserList user);
	int addEntityChangedEvent(Integer entityTypeId, String entityTypeName, Integer entityInstanceId, String entityInstanceName, String fieldName, String fieldValue, String newFieldValue, UserList user, String remarks);
	int addEntityRemovedEvent(Integer entityTypeId, String entityTypeName, Integer entityInstanceId, String entityInstanceName, String fieldName, String fieldValue,
			String newFieldValue, UserList user, String remarks);
	int addEntityMappingAddedEvent(Integer sourceEntityTypeId, Integer sourceEntityInstanceId, String sourceEntityInstanceName, Integer targetEntityTypeId, Integer targetEntityInstanceId, String targetEntityInstanceName, UserList user);
	int addEntityMappingRemovedEvent(Integer sourceEntityTypeId, Integer sourceEntityInstanceId, String sourceEntityInstanceName, Integer targetEntityTypeId, Integer targetEntityInstanceId, String targetEntityInstanceName, UserList user);
	List<Event> listEventsBySourceEntityType(String sourceEntityType, int sourceEntityId, int jtStartIndex, int jtPageSize);
	Integer getEventTotalCount(String sourceEntityType, Integer sourceEntityId, String eventSourceComponent, int jtStartIndex, int jtPageSize);
	List<Event> getEventsOfIds(String enityType, List<Integer> instanceIds);
	List<Event> listEventsByUserId(int userId, String currentDate, int jtStartIndex, int jtPageSize);
	List<Event> listBotCommandEventsBySourceEntityType(int botEventId, int jtStartIndex, int jtPageSize);
	Integer getBotCommandEventTotalCount(Integer botEventId,String eventSourceComponent, int jtStartIndex, int jtPageSize);

}
