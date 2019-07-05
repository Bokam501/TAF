package com.hcl.atf.taf.dao.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.common.events.EventsMasterRef;
import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.dao.EventsDAO;
import com.hcl.atf.taf.model.Event;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.vo.EventVO;

@Repository
public class EventsDAOImpl implements EventsDAO{

	private static final Log log = LogFactory.getLog(EventsDAOImpl.class);

	@Autowired(required=true)
    private SessionFactory sessionFactory;
	
	@Override
	@Transactional
	public Integer addEvent(Event event) {
		log.debug("Adding new Event");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(event);
			log.debug("Add event successful");
			return event.getEventId();
		} catch (RuntimeException re) {
			log.error("Add event failed", re);
			return null;
		}		
	}
	
	@Override
	@Transactional
	public Integer update(Event event) {
		log.debug("Updating Event");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(event);
			log.debug("update successful");
			return event.getEventId();
		} catch (RuntimeException re) {
			log.error("update failed", re);
			return null;
		}
	}
	
	@Override
	@Transactional
	public List<Event> list() {
		log.debug("Listing all events");
		List<Event> events=null;
		try {
			events=sessionFactory.getCurrentSession().createQuery("from Event").list();
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return events;
	}

	@Override
	@Transactional
    public List<Event> list(int startIndex, int pageSize) {
		log.debug("Listing events by page");
		List<Event> events=null;
		try {
			events=sessionFactory.getCurrentSession().createQuery("from Event")
	                .setFirstResult(startIndex)
	                .setMaxResults(pageSize)
	                .list();
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return events;       
	}
	
	@Override
	@Transactional
	public Event getEventById(int eventId){
		log.debug("Getting Event by id");
		Event event=null;
		try {
			List list =  sessionFactory.getCurrentSession().createQuery("from Event t where eventId=:eventId").setParameter("eventId", eventId)
					.list();
			event=(list!=null && list.size()!=0)?(Event)list.get(0):null;
			log.debug("Getting Event by id successful");
		} catch (RuntimeException re) {
			log.error("Getting Event by id failed", re);
		}
		return event;
	}

	@Override
	@Transactional
	public List<Event> getEventsByFilter(String eventSourceComponent, String eventName,
			String eventCategory, String eventType, String sourceEntityType, Integer sourceEntityId, 
			String sourceEntityName, Integer eventStatus,
			Date startTime, Date endTime){

		List<Event> events = null;
		String eventSourceComponentFilter = null;
		String eventNameFilter = null;
		String eventCategoryFilter = null;
		String eventTypeFilter = null;
		String sourceEntityTypeFilter = null;
		Integer sourceEntityIdFilter = null;
		String sourceEntityNameFilter = null;
		Integer eventStatusFilter = null;
		Date startTimeFilter = null;
		Date endTimeFilter = null;
		
		try {

			eventSourceComponentFilter = (eventSourceComponent == null) ? EventsMasterRef.EventSource.TAF.getValue() : eventSourceComponent;
			eventNameFilter = (eventName == null) ? "*" : eventName;
			eventCategoryFilter = (eventCategory == null) ? "*" : eventCategory;
			eventTypeFilter = (eventType == null) ? "*" : eventType;
			sourceEntityTypeFilter = (sourceEntityType == null) ? "*" : sourceEntityType;
			sourceEntityNameFilter = (sourceEntityName == null) ? "*" : sourceEntityName;
			
			if (startTime == null && endTime == null) {
				Calendar cal = Calendar.getInstance();
				endTime = cal.getTime();
				cal.add(Calendar.DATE, -30);
				startTimeFilter = cal.getTime();
			} else if (endTime == null) {
				endTimeFilter = new Date(System.currentTimeMillis());
			} else if (startTime == null) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(endTime);
				cal.add(Calendar.DATE, -30);
				startTimeFilter = cal.getTime();
			}
	
			Criteria c = sessionFactory.getCurrentSession().createCriteria(Event.class)       
	                .add(Restrictions.eq("eventSourceComponent", eventSourceComponentFilter))
	                .add(Restrictions.eq("eventName", eventNameFilter))
	                .add(Restrictions.eq("sourceEntityType", sourceEntityTypeFilter))
	                .add(Restrictions.eq("eventCategory", eventCategoryFilter))
	                .add(Restrictions.eq("eventType", eventTypeFilter))
	                .add(Restrictions.eq("sourceEntityName", sourceEntityNameFilter))
	                .add(Restrictions.between("startTime", startTimeFilter, endTimeFilter))
	                .add(Restrictions.eq("eventStatus", 1))
	                .setProjection(Projections.projectionList()
	                        .add(Projections.property("eventId"))
	                        .add(Projections.property("eventSourceComponent"))
	                        .add(Projections.property("eventName"))
	                        .add(Projections.property("eventDescription"))
	                        .add(Projections.property("eventData"))
	                        .add(Projections.property("additonalEventData"))
	                        .add(Projections.property("additonalEventData2"))
	                        .add(Projections.property("eventType"))
	                        .add(Projections.property("sourceEntityType"))
	                        .add(Projections.property("sourceEntityId"))
	                        .add(Projections.property("sourceEntityName"))
	                        .add(Projections.property("targetEntityType"))
	                        .add(Projections.property("targetEntityId"))
	                        .add(Projections.property("targetEntityName"))
	                        .add(Projections.property("additionalTargetEntityType"))
	                        .add(Projections.property("additionalTargetEntityId"))
	                        .add(Projections.property("additionalTargetEntityName"))
	                        .add(Projections.property("eventStatus"))
	                        .add(Projections.property("startTime"))
	                        .add(Projections.property("endTime"))
	                );
	           c.setResultTransformer(Transformers.aliasToBean(Event.class));
	           events = c.list();
			
		} catch (RuntimeException re) {
			log.error("Error in retrieving events by filter", re);
		}
		return events;
	}
	
	@Override
	@Transactional
	public List<EventVO> getAggregatedEvents(String eventSourceComponent, 
			String sourceEntityType, Integer sourceEntityId, Date startTime, Date endTime) {
		
		List<EventVO> aggregatedEvents = null;
		try {
			if (sourceEntityId == null) {
				log.info("Product Id is not available");
				return null;
			}
			if (startTime == null && endTime == null) {
				Calendar cal = Calendar.getInstance();
				endTime = cal.getTime();
				cal.add(Calendar.DATE, -30);
				startTime = cal.getTime();
			} else if (endTime == null) {
				endTime = new Date(System.currentTimeMillis());
			} else if (startTime == null) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(endTime);
				cal.add(Calendar.DATE, -30);
				startTime = cal.getTime();
			}
	
			Criteria c = sessionFactory.getCurrentSession().createCriteria(Event.class)       
	                .add(Restrictions.eq("eventSourceComponent", eventSourceComponent))
	                .add(Restrictions.eq("sourceEntityType", sourceEntityType))
	                .add(Restrictions.eq("sourceEntityId", sourceEntityId))
	                .add(Restrictions.between("startTime", startTime, endTime))
	                .add(Restrictions.eq("eventStatus", 1))
	                .setProjection(Projections.projectionList()
	                		.add(Projections.property("eventSourceComponent"))
	                        .add(Projections.property("eventName"))
	                		.add(Projections.property("eventCategory"))
	                        .add(Projections.property("eventDescription"))
	                        .add(Projections.property("eventType"))
	                		.add(Projections.groupProperty("eventCategory"))
	                		.add(Projections.groupProperty("eventName"))
	                        .add(Projections.sum("eventData").as("eventData"))
	                        .add(Projections.sum("additonalEventData").as("additonalEventData"))
	                        .add(Projections.sum("additonalEventData2").as("additonalEventData2"))           
	                        .add(Projections.min("startTime").as("startTime"))           
	                        .add(Projections.max("endTime").as("endTime"))           
	                );
	           c.setResultTransformer(Transformers.aliasToBean(EventVO.class));
	           aggregatedEvents = c.list();
		} catch (Exception e) {
			log.error("Problem getting aggregated events", e);
			return null;
		}
		if (aggregatedEvents == null)
			return null;
		return aggregatedEvents;
	}

	@Override
	@Transactional
	public int getTotalRecordCount() {
		log.debug("getting Event total records count");
		int count =0;
		try {
			count=((Number) sessionFactory.getCurrentSession().createSQLQuery("select count(*) from common_events").uniqueResult()).intValue();
			log.debug("total records count fetch successful");
		} catch (RuntimeException re) {
			log.error("total records count fetch failed", re);			
		}
		return count;
	
	}
	
	@Override
	@Transactional
	public int addNewEntityEvent(Integer entityTypeId, String entityTypeName, Integer entityInstanceId, String entityInstanceName, UserList user) {
		int eventId = 0;
		Event event = new Event();
		log.debug("adding Event(Audit History) instance");
		try {
			event.setEventSourceComponent(IDPAConstants.SOURCE_COMPONENT_ILCM);
			event.setEventName(IDPAConstants.EVENT_CREATE);
			event.setEventData(new Double(1));
			event.setEventDescription(entityTypeName+" created");
			event.setSourceEntityType(entityTypeName);
			event.setSourceEntityId(entityInstanceId);
			event.setSourceEntityName(entityInstanceName);
			event.setUserId(user.getUserId());
			event.setUserName(user.getLoginId());
			event.setRoleId(user.getUserRoleMaster().getUserRoleId());
			event.setRoleLabel(user.getUserRoleMaster().getRoleLabel());
			event.setEventStatus(1);
			event.setStartTime(new Date(System.currentTimeMillis()));
			event.setEndTime(new Date(System.currentTimeMillis()));
			sessionFactory.getCurrentSession().save(event);
			log.debug("add successful");
			eventId = event.getEventId();
			return eventId;
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}
		return eventId;
	}

	@Override
	public int addEntityChangedEvent(Integer entityTypeId, String entityTypeName, Integer entityInstanceId, String entityInstanceName, String fieldName, String fieldValue,
			String newFieldValue, UserList user, String remarks) {
		log.debug("updating Event(Audition History) instance");
		int eventId = 0;
		Event event = new Event();
		try {
			String fieldNameLabel = "";
			String oldValue = "";
			String newValue = "";
			String description = "";
			event.setEventSourceComponent(IDPAConstants.SOURCE_COMPONENT_ILCM);
			event.setEventName(IDPAConstants.EVENT_UPDATE);
			event.setEventData(new Double(1));
			event.setSourceEntityType(entityTypeName);
			event.setSourceEntityId(entityInstanceId);
			if(remarks != "" || remarks != null){
				event.setSourceEntityName(remarks);
			}else{
				event.setSourceEntityName(entityInstanceName);			
			}
			
			if(fieldName != null)
				fieldNameLabel = fieldName;
				
			if(fieldValue != null)
				oldValue = fieldValue;
			
			if(newFieldValue != null)
				newValue = newFieldValue;
			
			event.setFieldName(fieldNameLabel);
			event.setFieldValue(oldValue);
			event.setNewFieldValue(newValue);			
			
			if(fieldName != null){
				description = "Field '"+fieldNameLabel+"' changed from '"+oldValue+"' to '"+newValue+"'";
			}else{
				description = entityTypeName +" updated";
			}
			event.setEventDescription(description);
			
			event.setUserId(user.getUserId());
			event.setUserName(user.getLoginId());
			event.setRoleId(user.getUserRoleMaster().getUserRoleId());
			event.setRoleLabel(user.getUserRoleMaster().getRoleLabel());
			event.setEventStatus(1);
			event.setStartTime(new Date(System.currentTimeMillis()));
			event.setEndTime(new Date(System.currentTimeMillis()));
			sessionFactory.getCurrentSession().saveOrUpdate(event);
			log.debug("update successful");
			eventId = event.getEventId();
		} catch (RuntimeException re) {
			log.error("update failed", re);
		}
		return eventId;
	}

	@Override
	@Transactional
    public List<Event> listEventsBySourceEntityType(String sourceEntityType, int sourceEntityId, int jtStartIndex, int jtPageSize) {
		log.debug("Listing events by SourceEntityType");
		List<Event> events=null;
		try {
			String hql = "";
			hql = "from Event event where event.sourceEntityId = "
					+ sourceEntityId
					+ " and event.sourceEntityType = '"
					+ sourceEntityType 
					+ "' order by event.endTime desc";
			if(jtStartIndex!=-1 && jtPageSize!=-1){
				events=sessionFactory.getCurrentSession().createQuery(hql).setFirstResult(jtStartIndex).setMaxResults(jtPageSize).list();
			}else{
				events=sessionFactory.getCurrentSession().createQuery(hql).list();
			}
			
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return events;       
	}

	@Override
	@Transactional
	public Integer getEventTotalCount(String sourceEntityType, Integer sourceEntityId,  String eventSourceComponent, int jtStartIndex, int jtPageSize) {
		log.debug("listing getEventTotalCount instance");
		int eventCount = 0;
		
		String sql="select count(evt.eventSourceComponent) as EvntSrcCmpntCount from common_events evt where evt.sourceEntityId=:entityId and evt.sourceEntityType=:sourceEntType and evt.eventSourceComponent=:comp order by EvntSrcCmpntCount asc";
		try {
			if(jtStartIndex!=-1 && jtPageSize!=-1){
				eventCount=((Number) sessionFactory.getCurrentSession().createSQLQuery(sql)
						.setParameter("sourceEntType", sourceEntityType)
						.setParameter("entityId", sourceEntityId)
						.setParameter("comp", eventSourceComponent)
						.setFirstResult(jtStartIndex).setMaxResults(jtPageSize)
						.uniqueResult()).intValue();				
			}else{
				eventCount=((Number) sessionFactory.getCurrentSession().createSQLQuery(sql)
						.setParameter("sourceEntType", sourceEntityType)
						.setParameter("entityId", sourceEntityId)
						.setParameter("comp", eventSourceComponent)
						.uniqueResult()).intValue();					
			}
		
		} catch (RuntimeException re) {
			log.error("list getEventTotalCount", re);
		}
		return eventCount;
	}
	
	@Override
	public int addEntityRemovedEvent(Integer entityTypeId, String entityTypeName, Integer entityInstanceId, String entityInstanceName, String fieldName, String fieldValue,
			String newFieldValue, UserList user, String remarks) {
		log.debug("updating Event(Audition History) instance");
		int eventId = 0;
		Event event = new Event();
		try {
			String fieldNameLabel = "";
			String oldValue = "";
			String newValue = "";
			String description = "";
			event.setEventSourceComponent(IDPAConstants.SOURCE_COMPONENT_ILCM);
			event.setEventName(IDPAConstants.EVENT_DELETE);
			event.setEventData(new Double(1));
			event.setSourceEntityType(entityTypeName);
			event.setSourceEntityId(entityInstanceId);
			if(remarks != "" || remarks != null){
				event.setSourceEntityName(remarks);
			}else{
				event.setSourceEntityName(entityInstanceName);			
			}
			
			if(fieldName != null)
				fieldNameLabel = fieldName;
				
			if(fieldValue != null)
				oldValue = fieldValue;
			
			if(newFieldValue != null)
				newValue = newFieldValue;
			
			event.setFieldName(fieldNameLabel);
			event.setFieldValue(oldValue);
			event.setNewFieldValue(newValue);			
			
			if(fieldName != null){
				description = "Field '"+fieldNameLabel+"' changed from '"+oldValue+"' to '"+newValue+"'";
			}else{
				description = entityTypeName +" deleted";
			}
			event.setEventDescription(description);
			
			event.setUserId(user.getUserId());
			event.setUserName(user.getLoginId());
			event.setRoleId(user.getUserRoleMaster().getUserRoleId());
			event.setRoleLabel(user.getUserRoleMaster().getRoleLabel());
			event.setEventStatus(1);
			event.setStartTime(new Date(System.currentTimeMillis()));
			event.setEndTime(new Date(System.currentTimeMillis()));
			sessionFactory.getCurrentSession().saveOrUpdate(event);
			log.debug("delete successful");
			eventId = event.getEventId();
		} catch (RuntimeException re) {
			log.error("delete failed", re);
		}
		return eventId;
	}

	@Override
	public int addEntityMappingAddedEvent(Integer sourceEntityTypeId,
			Integer sourceEntityInstanceId, String sourceEntityInstanceName,
			Integer targetEntityTypeId, Integer targetEntityInstanceId,
			String targetEntityInstanceName, UserList user) {
		return 0;
	}

	@Override
	public int addEntityMappingRemovedEvent(Integer sourceEntityTypeId,
			Integer sourceEntityInstanceId, String sourceEntityInstanceName,
			Integer targetEntityTypeId, Integer targetEntityInstanceId,
			String targetEntityInstanceName, UserList user) {
		return 0;
	}

	@Override
	@Transactional
	public List<Event> getEventsOfIds(String entityType, List<Integer> instanceIds) {
		List<Event> events = null;
		try{
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Event.class, "event");
			criteria.add(Restrictions.eq("event.sourceEntityType", entityType));
			criteria.add(Restrictions.in("event.sourceEntityId", instanceIds));
			events = criteria.list();
		}catch(Exception ex){
			log.error("Error occured in getEventsOfIds - ", ex);
		}
		return events;
	}
	
	@Override
	@Transactional
	public List<Event> listEventsByUserId(int userId, String currentDate, int jtStartIndex, int jtPageSize){
		log.debug("Listing events by UserId");
		List<Event> events=null;
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DATE, -7);
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Event.class, "event");
			criteria.add(Restrictions.eq("event.userId", userId));
			criteria.add(Restrictions.ge("event.endTime", calendar.getTime()));
			events = criteria.list();
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return events;
	}

	@Override
	public List<Event> listBotCommandEventsBySourceEntityType(int botEventId, int jtStartIndex,int jtPageSize) {
		log.debug("Listing bot Command events by SourceEntityType");
		List<Event> events=null;
		try {
			String hql = "";
			hql = "from Event event where event.targetEntityId = "
					+ botEventId
					+ " and event.sourceEntityType ='BotCommand' and targetEntityId is not null order by event.endTime desc";
			if(jtStartIndex!=-1 && jtPageSize!=-1){
				events=sessionFactory.getCurrentSession().createQuery(hql).setFirstResult(jtStartIndex).setMaxResults(jtPageSize).list();
			}else{
				events=sessionFactory.getCurrentSession().createQuery(hql).list();
			}
			
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed in bot Command event", re);
		}
		return events;
	}
	
	
	@Override
	@Transactional
	public Integer getBotCommandEventTotalCount( Integer botEventId,  String eventSourceComponent, int jtStartIndex, int jtPageSize) {
		log.debug("listing getBotCommandEventTotalCount instance");
		int eventCount = 0;
		
		String sql="select count(*) from common_events evt where evt.targetEntityId=:botEventId and evt.eventSourceComponent=:comp order by evt.eventSourceComponent asc";
		try {
			if(jtStartIndex!=-1 && jtPageSize!=-1){
				eventCount=((Number) sessionFactory.getCurrentSession().createSQLQuery(sql)
						.setParameter("botEventId", botEventId)
						.setParameter("comp", eventSourceComponent)
						.setFirstResult(jtStartIndex).setMaxResults(jtPageSize)
						.uniqueResult()).intValue();				
			}else{
				eventCount=((Number) sessionFactory.getCurrentSession().createSQLQuery(sql)
						.setParameter("botEventId", botEventId)
						.setParameter("comp", eventSourceComponent)
						.uniqueResult()).intValue();					
			}
		
		} catch (RuntimeException re) {
			log.error("list getBotCommandEventTotalCount", re);
		}
		return eventCount;
	}
}


