package com.hcl.atf.taf.dao.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
//import org.elasticsearch.search.aggregations.bucket.histogram.Histogram.Order;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.ActivityTaskDAO;
import com.hcl.atf.taf.model.Activity;
import com.hcl.atf.taf.model.ActivityStatus;
import com.hcl.atf.taf.model.ActivityTask;
import com.hcl.atf.taf.model.ActivityTaskType;
import com.hcl.atf.taf.model.ExecutionPriority;
import com.hcl.atf.taf.mongodb.service.MongoDBService;
import com.hcl.ilcm.workflow.service.WorkflowStatusPolicyService;
@Repository
public class ActivityTaskDAOImpl implements ActivityTaskDAO {
private static final Log log = LogFactory.getLog(ActivityTaskDAOImpl.class);
	
	@Autowired(required=true)
	private SessionFactory sessionFactory;
	
	@Autowired
	private WorkflowStatusPolicyService workflowStatusPolicyService;
	
	@Autowired
	private MongoDBService mongoDBService;
	
	private static String PQA="%PQA%";
	private static String PEER="%Peer%";
	private static String DELIVERED="%Delivered%";
	private static String SELF="%Self%";
	private static String REWORK="%Rework%";
	private static String OPEN="%Open%";
	private static String NOTSTARTED="%Not Started%";
	private static String YETTOSTART="%Yet to Start%";
	private static String INPROGRESS="%InProgress%";
	private static String READY ="%Ready%";
	private static String REVIEW="%Review%"; 
	private static String HOLD="%HOLD%"; 
	private static String COMPLETED="%completed%";
	private static String COMPLETE="%COMPLETE%";
	
	@Value("#{ilcmProps['database.url']}")
    private String DBUrl;
	
	@Value("#{ilcmProps['ACTIVITY_TASK_WORKPACKAGE_LIST_REVERSE_ORDER']}")
    private String activityTaskWorkpackageListOrder;
	
	@Override
	@Transactional
	public List<ActivityTask> activityTasklists() {
		log.debug("ActivityTasklists Instance");
		List<ActivityTask> activityTaskList = null;
		try {
			activityTaskList = sessionFactory.getCurrentSession().createQuery("from ActivityTask").list();
			log.debug("list all ActivityTasks successful");
		} catch (RuntimeException re) {
			log.error("list all ActivityTasks failed", re);
			// throw re;
		}
		return activityTaskList;
	}

	
	@Override
	@Transactional
	public void addActivityTask(ActivityTask addTask) {
		log.info("adding addActivityTask instance");
		try {	
			sessionFactory.getCurrentSession().save(addTask);
			log.info("add addActivityTask successful");
		} catch (RuntimeException re) {
			log.error("add addActivityTask failed", re);
			//throw re;
		}	
		
	}


	@Override
	@Transactional
	public void updateActivityTask(ActivityTask updateTask) {
		log.debug("updating updateActivityTask instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(updateTask);
			log.debug("update updateActivityTask successful");
		} catch (RuntimeException re) {
			log.error("update updateActivityTask failed", re);
			//throw re;
		}
		
	}

	@Override
	@Transactional
	public String deleteActivityTask(int activityTaskId,String referencedTableName, String referencedColumnName) {
		boolean isMappingAvailable=false;
		
		String dbName = DBUrl.split("\\?")[0];
		dbName = dbName.substring(dbName.lastIndexOf("/")+1);
		String message = "Delete all references which are mapped with this ActivityTask and try again";
		try{
			String sql = "SELECT Table_name, Column_name FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE WHERE CONSTRAINT_SCHEMA = '"+dbName+"' AND REFERENCED_TABLE_NAME = '"+referencedTableName+"' AND REFERENCED_COLUMN_NAME = '"+referencedColumnName+"'";
			Session session = sessionFactory.getCurrentSession();
			List list = session.createSQLQuery(sql).setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();
			for(Object o : list) {
				Map row = (Map)o;
				String queryForChild = "select count(*) from "+row.get("Table_name")+" where "+row.get("Column_name")+"="+activityTaskId;
				int count = ((BigInteger)session.createSQLQuery(queryForChild).uniqueResult()).intValue();
				if(count > 0){
					isMappingAvailable = true;
				}
			}
			if(!isMappingAvailable) {
			String hql = "delete from ActivityTask where activityTaskId=:activityTaskId";
			sessionFactory.getCurrentSession().createQuery(hql).setInteger("activityTaskId", activityTaskId).executeUpdate();
			message="OK";
			log.info("deletion of ActivityTask successful");
			}
		}catch(RuntimeException re){
			log.error("deletion of ActivityTask failed", re);
		}
		return message;
	}

	

@Override
	@Transactional
	public List<ActivityTask> listActivityTasksByActivityId(Integer activityId,Integer initializationLevel,  Integer isActive) {
		log.debug("listing all Activity instance: activityId: "+activityId);
		List<ActivityTask> activityTaskList = null;
		try {
			    log.debug("list all Activity successful");
				Criteria c = sessionFactory.getCurrentSession().createCriteria(ActivityTask.class, "activityTask");		
				c.add(Restrictions.eq("activityTask.activity.activityId", activityId));
				if (isActive != 2){
					c.add(Restrictions.eq("activityTask.isActive", isActive));
				}
				activityTaskList = c.list();
				if (activityTaskList != null && activityTaskList.size() > 0) {
					for (ActivityTask activityTask : activityTaskList) {
						Hibernate.initialize(activityTask.getStatus());
						Hibernate.initialize(activityTask.getSecondaryStatus());
					}
				}
		} catch (RuntimeException re) {
			log.error("list all Activity failed", re);
		}
		return activityTaskList;
	}

@Override
@Transactional
public List<ActivityTask> getActivityTaskByActivityId(Integer activityId) {
	List<ActivityTask> listOfActivityTasks = new ArrayList<ActivityTask>();
	try {
		Criteria c = sessionFactory.getCurrentSession().createCriteria(
				ActivityTask.class, "activityTask");
		c.add(Restrictions.eq("activityTask.activity.activityId", activityId));
		listOfActivityTasks = c.list();
		if (listOfActivityTasks != null && listOfActivityTasks.size() > 0) {
			for (ActivityTask activityTask : listOfActivityTasks) {
				Hibernate.initialize(activityTask.getStatus());
				Hibernate.initialize(activityTask.getEnvironmentCombination());
			}
		}
	} catch (RuntimeException re) {
		log.error("list failed", re);
	}
	return listOfActivityTasks;
}
	
	@Override
	@Transactional
	public ActivityTask getActivityTaskById(Integer activityTaskId,
			Integer initializationLevel) {
		ActivityTask activityTask=null;
		try {
			List list =  sessionFactory.getCurrentSession().createQuery("from ActivityTask where activityTaskId=:activityTaskId")
					.setParameter("activityTaskId", activityTaskId)
					.list();
			activityTask=(list!=null && list.size()!=0)?(ActivityTask)list.get(0):null;
			if (activityTask != null) {
				if(initializationLevel == 1){
					Hibernate.initialize(activityTask.getActivity());
					if(activityTask.getActivity() != null){
						Hibernate.initialize(activityTask.getActivity().getActivityWorkPackage());
						if(activityTask.getActivity().getActivityWorkPackage() != null){
							Hibernate.initialize(activityTask.getActivity().getActivityWorkPackage().getCompetency());
						}
					}
				}
			}
		}catch (RuntimeException re) {
			log.error("getActivityById failed", re);
		}
		return activityTask;
	}

	@Override
	@Transactional
	public List<ActivityTask> listActivityTaskforSelfReview(
			int activityId, Integer userId,Integer userRoleId, Integer jtStartIndex,
			Integer jtPageSize, Integer initializationLevel, Map<String, String> searchString)  {
		log.info("listing all Activity instance: activityId: "+activityId);
		List<ActivityTask> activityTaskList = null;
		try {
			log.info("listing all Activity listActivityTaskforSelfReview");
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ActivityTask.class, "activityTask");	
		    c.createAlias("activityTask.activity", "activity");
		    String assigneeId = searchString.get("searchassigneeId");
		    String priorityId= searchString.get("searchpriority");
		    String statusId= searchString.get("searchstatusId");
		   
		    if(activityId != 0){
				c.add(Restrictions.eq("activity.activityId", activityId));
			}
			
			if( searchString != null && searchString.size()>0&& !searchString.isEmpty() && (statusId!=null && assigneeId!=null && priorityId!=null)){							
					
						if(!statusId.equals("-1") && !assigneeId.equals("-1")&&!priorityId.equals("-1"))
							c.add(Restrictions.and(Restrictions.eq("assignee.userId", Integer.parseInt(assigneeId)), Restrictions.eq("priority.executionPriorityId",Integer.parseInt(priorityId)), Restrictions.eq("status.activityStatusId",Integer.parseInt(statusId))));
						if(!statusId.equals("-1") && assigneeId.equals("-1")&&priorityId.equals("-1"))
							c.add(Restrictions.eq("status.activityStatusId",Integer.parseInt(statusId)));	
						if(statusId.equals("-1") && !assigneeId.equals("-1")&& priorityId.equals("-1"))
							c.add(Restrictions.eq("assignee.userId",Integer.parseInt(assigneeId)));	
						if(statusId.equals("-1") && assigneeId.equals("-1")&&  !priorityId.equals("-1"))
							c.add(Restrictions.eq("priority.executionPriorityId",Integer.parseInt(priorityId)));	
						if(!statusId.equals("-1") && !assigneeId.equals("-1") && priorityId.equals("-1"))
							c.add(Restrictions.and(Restrictions.eq("assignee.userId", Integer.parseInt(assigneeId)),Restrictions.eq("status.activityStatusId", Integer.parseInt(statusId))));
						if(statusId.equals("-1") && !assigneeId.equals("-1")&&!priorityId.equals("-1"))
							c.add(Restrictions.and(Restrictions.eq("assignee.userId", Integer.parseInt(assigneeId)),Restrictions.eq("priority.executionPriorityId", Integer.parseInt(priorityId))));
						if(!statusId.equals("-1") && assigneeId.equals("-1")&&!priorityId.equals("-1"))
							c.add(Restrictions.and(Restrictions.eq("status.activityStatusId", Integer.parseInt(statusId)),Restrictions.eq("priority.executionPriorityId", Integer.parseInt(priorityId))));
				}
				if(activityTaskWorkpackageListOrder.equalsIgnoreCase("YES")){
					c.addOrder(Order.desc("activityTaskId"));
				}
			activityTaskList = c.list();
		    log.debug("list all Activity successful");
		} catch (RuntimeException re) {
			log.error("list all Activity failed", re);
		}
		return activityTaskList;
	}


	@Override
	@Transactional
	public List<ActivityTask> listActivityTasks(Integer productId,Integer productVersionId, Integer productBuildId, Integer activityWorkPackageId, Integer activityId,
			Integer initializationLevel, Integer isActive, Integer jtStartIndex, Integer jtPageSize, Map<String, String> searchString) {
		List<ActivityTask> activityTaskList = null;
		try {
			    log.debug("list all Activity Tasks successful");
				Criteria c = sessionFactory.getCurrentSession().createCriteria(ActivityTask.class, "activityTask");		
				c.createAlias("activityTask.activity", "activity");
				c.createAlias("activity.activityWorkPackage", "activityWp");
				c.createAlias("activityWp.productBuild", "pbuild");
				c.createAlias("pbuild.productVersion", "pVersion");
				c.createAlias("pVersion.productMaster", "product");
				
				if(activityId != 0){
					c.add(Restrictions.eq("activity.activityId", activityId));
				}
				
				if(activityWorkPackageId != 0){
					c.add(Restrictions.eq("activityWp.activityWorkPackageId", activityWorkPackageId));
				}
				
				if(productBuildId != 0){
					c.add(Restrictions.eq("pbuild.productBuildId", productBuildId));
				}
				
				if(productVersionId != 0){
					c.add(Restrictions.eq("pVersion.productVersionListId", productVersionId));
				}
				
				if(productId != 0){
					c.add(Restrictions.eq("product.productId", productId));
				}
				if (isActive != 2){
					c.add(Restrictions.eq("activityTask.isActive", isActive));
				}		
				String assigneeId = searchString.get("searchassigneeId");
			    String priorityId= searchString.get("searchpriority");
			    String statusId= searchString.get("searchstatusId");
			  		
				if( searchString != null && searchString.size()>0&& !searchString.isEmpty() && (statusId!=null && assigneeId!=null && priorityId!=null)){							
						
							if(!statusId.equals("-1") && !assigneeId.equals("-1")&&!priorityId.equals("-1"))
								c.add(Restrictions.and(Restrictions.eq("assignee.userId", Integer.parseInt(assigneeId)), Restrictions.eq("priority.executionPriorityId",Integer.parseInt(priorityId)), Restrictions.eq("status.activityStatusId",Integer.parseInt(statusId))));
							if(!statusId.equals("-1") && assigneeId.equals("-1")&&priorityId.equals("-1"))
								c.add(Restrictions.eq("status.activityStatusId",Integer.parseInt(statusId)));	
							if(statusId.equals("-1") && !assigneeId.equals("-1")&& priorityId.equals("-1"))
								c.add(Restrictions.eq("assignee.userId",Integer.parseInt(assigneeId)));	
							if(statusId.equals("-1") && assigneeId.equals("-1")&&  !priorityId.equals("-1"))
								c.add(Restrictions.eq("priority.executionPriorityId",Integer.parseInt(priorityId)));	
							if(!statusId.equals("-1") && !assigneeId.equals("-1") && priorityId.equals("-1"))
								c.add(Restrictions.and(Restrictions.eq("assignee.userId", Integer.parseInt(assigneeId)),Restrictions.eq("status.activityStatusId", Integer.parseInt(statusId))));
							if(statusId.equals("-1") && !assigneeId.equals("-1")&&!priorityId.equals("-1"))
								c.add(Restrictions.and(Restrictions.eq("assignee.userId", Integer.parseInt(assigneeId)),Restrictions.eq("priority.executionPriorityId", Integer.parseInt(priorityId))));
							if(!statusId.equals("-1") && assigneeId.equals("-1")&&!priorityId.equals("-1"))
								c.add(Restrictions.and(Restrictions.eq("status.activityStatusId", Integer.parseInt(statusId)),Restrictions.eq("priority.executionPriorityId", Integer.parseInt(priorityId))));
					}
				if(activityTaskWorkpackageListOrder.equalsIgnoreCase("YSE")){
					c.addOrder(Order.desc("activityTaskId"));
				}
				
				activityTaskList = c.list();						
		} catch (RuntimeException re) {
			log.error("list all Activity failed", re);
		}
		return activityTaskList;
	}


	@Override
	@Transactional
	public Integer listActivityTasksCount(Integer productId,Integer productVersionId, Integer productBuildId, Integer activityWorkPackageId, Integer activityId, Integer initializationLevel, Integer isActive, Map<String, String> searchString) {
		Integer activityTaskCount = 0;
		try {
			    log.debug("list all Activity Tasks successful");
				Criteria c = sessionFactory.getCurrentSession().createCriteria(ActivityTask.class, "activityTask");		
				c.createAlias("activityTask.activity", "activity");
				c.createAlias("activity.activityWorkPackage", "activityWp");
				c.createAlias("activityWp.productBuild", "pbuild");
				c.createAlias("pbuild.productVersion", "pVersion");
				c.createAlias("pVersion.productMaster", "product");
				
				if(activityId != 0){
					c.add(Restrictions.eq("activity.activityId", activityId));
				}
				
				if(activityWorkPackageId != 0){
					c.add(Restrictions.eq("activityWp.activityWorkPackageId", activityWorkPackageId));
				}
				
				if(productBuildId != 0){
					c.add(Restrictions.eq("pbuild.productBuildId", productBuildId));
				}
				
				if(productVersionId != 0){
					c.add(Restrictions.eq("pVersion.productVersionListId", productVersionId));
				}
				
				if(productId != 0){
					c.add(Restrictions.eq("product.productId", productId));
				}
				if (isActive != 2){
					c.add(Restrictions.eq("activityTask.isActive", isActive));
				}		
				String assigneeId = searchString.get("searchassigneeId");
			    String priorityId= searchString.get("searchpriority");
			    String statusId= searchString.get("searchstatusId");
			  		
				if( searchString != null && searchString.size()>0&& !searchString.isEmpty() && (statusId!=null && assigneeId!=null && priorityId!=null)){							
						
							if(!statusId.equals("-1") && !assigneeId.equals("-1")&&!priorityId.equals("-1"))
								c.add(Restrictions.and(Restrictions.eq("assignee.userId", Integer.parseInt(assigneeId)), Restrictions.eq("priority.executionPriorityId",Integer.parseInt(priorityId)), Restrictions.eq("status.activityStatusId",Integer.parseInt(statusId))));
							if(!statusId.equals("-1") && assigneeId.equals("-1")&&priorityId.equals("-1"))
								c.add(Restrictions.eq("status.activityStatusId",Integer.parseInt(statusId)));	
							if(statusId.equals("-1") && !assigneeId.equals("-1")&& priorityId.equals("-1"))
								c.add(Restrictions.eq("assignee.userId",Integer.parseInt(assigneeId)));	
							if(statusId.equals("-1") && assigneeId.equals("-1")&&  !priorityId.equals("-1"))
								c.add(Restrictions.eq("priority.executionPriorityId",Integer.parseInt(priorityId)));	
							if(!statusId.equals("-1") && !assigneeId.equals("-1") && priorityId.equals("-1"))
								c.add(Restrictions.and(Restrictions.eq("assignee.userId", Integer.parseInt(assigneeId)),Restrictions.eq("status.activityStatusId", Integer.parseInt(statusId))));
							if(statusId.equals("-1") && !assigneeId.equals("-1")&&!priorityId.equals("-1"))
								c.add(Restrictions.and(Restrictions.eq("assignee.userId", Integer.parseInt(assigneeId)),Restrictions.eq("priority.executionPriorityId", Integer.parseInt(priorityId))));
							if(!statusId.equals("-1") && assigneeId.equals("-1")&&!priorityId.equals("-1"))
								c.add(Restrictions.and(Restrictions.eq("status.activityStatusId", Integer.parseInt(statusId)),Restrictions.eq("priority.executionPriorityId", Integer.parseInt(priorityId))));
					}
				if(activityTaskWorkpackageListOrder.equalsIgnoreCase("YSE")){
					c.addOrder(Order.desc("activityTaskId"));
				}
				c.setProjection(Projections.rowCount());
				activityTaskCount = ((Long) c.uniqueResult()).intValue();						
		} catch (RuntimeException re) {
			log.error("list all Activity failed", re);
		}
		return activityTaskCount;
	}

	@Override
	@Transactional
	public List<ActivityTask> listActivityTaskforPeerReview(int activityId,
			Integer userId, Integer jtStartIndex, Integer jtPageSize,
			Integer initializationLevel, int filter) {
		log.info("listing all Activity instance: activityId: "+activityId);
		List<ActivityTask> activityTaskList = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ActivityTask.class, "activityTask");	
			c.createAlias("activityTask.status", "activityStatus");
			c.createAlias("activityTask.activity", "activity");
			c.createAlias("activityTask.reviewer", "reviewer");
				Query query = null;
				if(filter == -1){
					c.add(Restrictions.or(Restrictions.like("activityStatus.workflowStatusName", PQA).ignoreCase(),Restrictions.like("activityStatus.workflowStatusName", PEER).ignoreCase(),Restrictions.like("activityStatus.workflowStatusName", REVIEW).ignoreCase()));
					c.add(Restrictions.eq("reviewer.userId", userId));
					c.add(Restrictions.eq("activity.activityId",activityId));
					
				}
				else if(filter == 0){
					c.add(Restrictions.like("activityStatus.workflowStatusName", PEER).ignoreCase());
					c.add(Restrictions.or(Restrictions.like("activityStatus.workflowStatusName", PEER).ignoreCase(),Restrictions.like("activityStatus.workflowStatusName", REVIEW).ignoreCase()));
					c.add(Restrictions.eq("reviewer.userId", userId));
					c.add(Restrictions.eq("activity.activityId",activityId));
				}
				else if(filter == 1){
					c.add(Restrictions.like("activityStatus.workflowStatusName", PQA).ignoreCase());
					c.add(Restrictions.eq("reviewer.userId", userId));
					c.add(Restrictions.eq("activity.activityId",activityId));
				}
				c.addOrder(Order.desc("activityTaskId"));
				activityTaskList = c.list();		
				log.debug("list all Activity successful");
		} catch (RuntimeException re) {
			log.error("list all Activity failed", re);
		}
		return activityTaskList;
	}
	
	@Override
	@Transactional
	public List<ActivityTask> listActivityTaskforPQAReview(int activityId,
			Integer userId, Integer jtStartIndex, Integer jtPageSize,
			Integer initializationLevel, int filter) {
		log.info("listing all Activity instance: activityId: "+activityId);
		List<ActivityTask> activityTaskList = null;
		try {
				Criteria c = sessionFactory.getCurrentSession().createCriteria(ActivityTask.class, "activityTask");	
				c.createAlias("activityTask.status", "status");
				c.createAlias("activityTask.activity", "activity");
				Query query = null;
				if(filter == -1){
					c.add(Restrictions.or(Restrictions.like("status.workflowStatusName", PQA).ignoreCase(),Restrictions.like("status.workflowStatusName", DELIVERED).ignoreCase()));
					c.add(Restrictions.eq("activity.activityId",activityId));
				}
				else if(filter == 0){
					c.add(Restrictions.like("status.workflowStatusName", PQA).ignoreCase());
					c.add(Restrictions.eq("activity.activityId",activityId));
				}
				else if(filter == 1){
					c.add(Restrictions.like("status.workflowStatusName", DELIVERED).ignoreCase());
					c.add(Restrictions.eq("activity.activityId",activityId));
				}
				c.addOrder(Order.desc("activityTaskId"));
				activityTaskList = c.list();	
				log.debug("list all Activity successful");
		} catch (RuntimeException re) {
			log.error("list all Activity failed", re);
		}
		return activityTaskList;
	}


	@Override
	@Transactional
	public Integer countAllactivityTaskService(Date startDate, Date endDate) {
		try {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(ActivityTask.class,"activityTask");
		if (startDate != null) {
			crit.add(Restrictions.ge("activityTask.createdDate", startDate));
		}
		if (endDate != null) {
			crit.add(Restrictions.le("activityTask.createdDate", endDate));
		}
		crit.setProjection(Projections.rowCount());
		Integer count = Integer.parseInt(crit.uniqueResult().toString());
		return count;
	} catch (Exception e) {
		log.error("Unable to get count of all activityTask", e);
		return -1;
	}}


	@Override
	@Transactional
	public List<ActivityTask> listAllactivityTask(int startIndex,int pageSize, Date startDate, Date endDate) {
		log.debug("listing all ActivityTask");
		List<ActivityTask> activityTaskList=null;
	try {
		
		Criteria c = sessionFactory.getCurrentSession().createCriteria(ActivityTask.class, "activityTask");
		if (startDate != null) {
			c.add(Restrictions.ge("activityTask.createdDate", startDate));
		}
		if (endDate != null) {
			c.add(Restrictions.le("activityTask.createdDate", endDate));
		}
    	c.addOrder(Order.desc("activityTaskId"));
        c.setFirstResult(startIndex);
        c.setMaxResults(pageSize);

        activityTaskList = c.list();		
		
        if (activityTaskList != null && activityTaskList.size() > 0) {
			for (ActivityTask activityTask : activityTaskList) {
				Hibernate.initialize(activityTask.getStatus());
				Hibernate.initialize(activityTask.getSecondaryStatus());
			}
		}
		log.debug("list all successful");
	} catch (RuntimeException re) {
		log.error("list all failed", re);
		//throw re;
	}
	return activityTaskList;
}

	@Override
	@Transactional
	public boolean getActivityTaskById(Integer activityId,String activityTaskName){
		List<ActivityTask> listOfActivityTasks = new ArrayList<ActivityTask>();
		boolean flag=false;
		try{
		Criteria c = sessionFactory.getCurrentSession().createCriteria(ActivityTask.class, "activityTask");
		c.add(Restrictions.eq("activityTask.activity.activityId", activityId));
		c.add(Restrictions.eq("activityTask.activityTaskName", activityTaskName));
		listOfActivityTasks = c.list();
		if(listOfActivityTasks == null || listOfActivityTasks.isEmpty())
			flag=false;
		else
			flag=true;
		}catch(RuntimeException re){
			log.error("list failed", re);
		}
		return flag;
	}


	@Override
	@Transactional
	public ActivityTaskType getActivityTaskTypeById(Integer activityTaskTypeId) {
		ActivityTaskType activityTaskType = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ActivityTaskType.class, "activityTaskType");
			List<ActivityTaskType> activityTaskTypeList = c.list();
			
			if(activityTaskTypeList != null && activityTaskTypeList.size() > 0){
				activityTaskType = activityTaskTypeList.get(0);
			}
			
			log.debug("List getActivityTaskTypeById() successful");
		} catch (RuntimeException re) {
			log.error("List getActivityTaskTypeById() failed", re);
		}
		return activityTaskType;
	}


	@Override
	@Transactional
	public void updateActivityBasedOnTasks(Integer activityId) {
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ActivityTask.class, "activityTask")
			.createAlias("activityTask.activity", "activity")
		    .add(Restrictions.eq("activity.activityId", activityId))
			.setProjection(Projections.projectionList()
		    	.add(Projections.sum("plannedTaskSize"))
		    	.add(Projections.sum("actualTaskSize"))
		    	.add(Projections.sum("plannedUnit"))
		    	.add(Projections.sum("actualUnit"))
		    	.add(Projections.sum("totalEffort"))
		    );
			List<Object[]> activityTaskDetails = c.list();
			
			if(activityTaskDetails != null){
				c = sessionFactory.getCurrentSession().createCriteria(Activity.class, "activity")
				.add(Restrictions.eq("activity.activityId", activityId));
				
				List<Activity> activityList = c.list();
				
				if(activityList != null && activityList.size() > 0){
					Activity activity = activityList.get(0);
					for (Object[] activityTaskDetail : activityTaskDetails) {
						if(activityTaskDetail[0] != null){
							activity.setPlannedActivitySize(((Long)activityTaskDetail[0]).intValue());
						}
						if(activityTaskDetail[1] != null){
							activity.setActualActivitySize(((Long)activityTaskDetail[1]).intValue());
						}
						if(activityTaskDetail[2] != null){
							activity.setPlannedUnit(((Double)activityTaskDetail[2]).floatValue());
						}
						if(activityTaskDetail[3] != null){
							activity.setActualUnit(((Double)activityTaskDetail[3]).floatValue());
						}
						sessionFactory.getCurrentSession().saveOrUpdate(activity);
					}
				}
			}
			
			log.debug("List updateActivityBasedOnTasks() successful");
		} catch (RuntimeException re) {
			log.error("List updateActivityBasedOnTasks() failed", re);
		}
	}

	@Override
	@Transactional
	public Integer getActivitytaskCountByActivityId(Integer activityId) {
	List<ActivityTask> activityTaskList;	
	Integer count = 0;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ActivityTask.class, "activityTask")
					.createAlias("activityTask.activity", "activity")
				    .add(Restrictions.eq("activity.activityId", activityId));
			activityTaskList= c.list();
			if(activityTaskList!=null && activityTaskList.size()>0)
			count=activityTaskList.size();
	} catch (Exception e) {
		log.error("Unable to get count of all activityTask", e);
		return -1;
	}
		return count;
	
	}
	@Override
	@Transactional
	public Integer getActivitytaskCountByActivityIdAndUserId(Integer activityId,Integer userId) {
	List<ActivityTask> activityTaskList;	
	Integer count = 0;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ActivityTask.class, "activityTask");
					c.createAlias("activityTask.activity", "activity");
			 		c.createAlias("activityTask.assignee", "assignee");
			 		c.add(Restrictions.eq("assignee.userId", userId));
			 		c.add(Restrictions.eq("activity.activityId", activityId));
			
			
			activityTaskList= c.list();
			if(activityTaskList!=null && activityTaskList.size()>0)
			count=activityTaskList.size();
	} catch (Exception e) {
		log.error("Unable to get count of all activityTask", e);
		return -1;
	}
		return count;
	
	}
	@Override
	@Transactional
	public Integer getActivitytaskCountByStatus(Integer activityId,Integer userId,Integer statusFilterCode) {
		Integer count=0;
		List<ActivityTask> activityTaskList=null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ActivityTask.class, "activityTask");				
			c.createAlias("activityTask.activity", "activity");
			c.createAlias("activityTask.assignee", "assignee");
			c.createAlias("activityTask.reviewer", "reviewer");
			c.createAlias("activityTask.status", "status");
			
			if(statusFilterCode==1)
			{  c.add(Restrictions.eq("activity.activityId", activityId));
			   c.add(Restrictions.or(Restrictions.eq("assignee.userId", userId),Restrictions.eq("reviewer.userId", userId)));
			   c.add(Restrictions.or(Restrictions.like("status.workflowStatusName", OPEN).ignoreCase(),Restrictions.like("status.workflowStatusName", NOTSTARTED).ignoreCase(),Restrictions.like("status.workflowStatusName", YETTOSTART).ignoreCase()));
			}
		else if(statusFilterCode==2){
			c.add(Restrictions.eq("activity.activityId", activityId));
			c.add(Restrictions.or(Restrictions.eq("assignee.userId", userId),Restrictions.eq("reviewer.userId", userId)));
			c.add(Restrictions.like("status.workflowStatusName", HOLD).ignoreCase());
		}
		else if(statusFilterCode==3){
			c.add(Restrictions.eq("activity.activityId", activityId));
			c.add(Restrictions.or(Restrictions.eq("assignee.userId", userId),Restrictions.eq("reviewer.userId", userId)));
			c.add(Restrictions.or(Restrictions.like("status.workflowStatusName", COMPLETED).ignoreCase(),Restrictions.like("status.workflowStatusName", COMPLETE).ignoreCase()));
		}
		else if(statusFilterCode==4){
			c.add(Restrictions.eq("activity.activityId", activityId));
			c.add(Restrictions.or(Restrictions.eq("assignee.userId", userId),Restrictions.eq("reviewer.userId", userId)));
			c.add(Restrictions.or(Restrictions.like("status.workflowStatusName", INPROGRESS).ignoreCase(),Restrictions.like("status.workflowStatusName", REWORK).ignoreCase()));
		}
		
		
		activityTaskList = c.list();	
		if(activityTaskList!=null && activityTaskList.size()>0)
	    count=activityTaskList.size();
		
	} catch (Exception e) {
		log.error("Unable to get count of all activityTask", e);
		return -1;
	}
		return count;
	}
	

	@Override
	public int addBulk(List<ActivityTask> activityTaskList,int batchSize,int productId) {
		// TODO Auto-generated method stub

		log.info("Adding Test case steps in bulk");
		int count = 0;
		try {
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			ExecutionPriority executionPriority=new ExecutionPriority();
			executionPriority.setExecutionPriorityId(1);
			if (batchSize <= 0)
				batchSize = 50;
			for (ActivityTask task : activityTaskList ) {
				task.setIsActive(1);
				task.setPriority(executionPriority);
				task.setCreatedDate(new Date());
				task.setModifiedDate(new Date());
				session.save(task);

				log.info("Activity task data saved successfully");
				if (count++ % batchSize == 0 ) {
					session.flush();
					session.clear();
			    }
			}
			tx.commit();
			session.close();
			log.info("Bulk Add Successful");
		} catch (RuntimeException re) {
			log.error("Bulk Add failed", re);
			//throw re;
		}
		return count;
	}

	@Override
	@Transactional
	public List<ActivityTaskType> listActivityTaskTypes(Integer testFactoryId, Integer productId, Integer status, Integer jtStartIndex, Integer jtPageSize, Boolean isConsildated) {
		List<ActivityTaskType> activityTaskTypeList = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ActivityTaskType.class, "activityTaskType");
			if(testFactoryId != null && testFactoryId > 0 && productId != null && productId > 0){
				if(isConsildated){
					c.add(Restrictions.disjunction().add(
							Restrictions.or(
									Restrictions.conjunction()
										.add(Restrictions.eq("activityTaskType.testFactory.testFactoryId", testFactoryId)).add(Restrictions.isNull("activityTaskType.product.productId")), 
									Restrictions.eq("activityTaskType.product.productId", productId)
					)));					
				}else{
					c.add(Restrictions.eq("activityTaskType.testFactory.testFactoryId", testFactoryId));
					c.add(Restrictions.eq("activityTaskType.product.productId", productId));
				}
			}else if(testFactoryId != null && testFactoryId > 0){
				c.add(Restrictions.eq("activityTaskType.testFactory.testFactoryId", testFactoryId));
				c.add(Restrictions.isNull("activityTaskType.product.productId"));
			}else if(productId != null && productId > 0){
				c.add(Restrictions.eq("activityTaskType.product.productId", productId));
			}else{
				c.add(Restrictions.isNull("activityTaskType.testFactory.testFactoryId"));
				c.add(Restrictions.isNull("activityTaskType.product.productId"));
			}
			
			if(jtStartIndex != null && jtPageSize != null){
				c.setFirstResult(jtStartIndex);
				c.setMaxResults(jtPageSize);
			}
			activityTaskTypeList = c.list();
			
			log.debug("List listActivityTaskTypeOfProduct() successful for product id - "+productId);
		} catch (RuntimeException re) {
			log.error("List listActivityTaskTypeOfProduct() failed", re);
		}
		return activityTaskTypeList;
	}

	@Override
	@Transactional
	public Integer getTotalRecordsForTaskTypesPagination(Integer testFactoryId, Integer productId, Integer status, Boolean isConsildated) {
		Integer recordsCount = 0;
		try{
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ActivityTaskType.class, "activityTaskType");
			if(testFactoryId != null && testFactoryId > 0 && productId != null && productId > 0){
				if(isConsildated){
					c.add(Restrictions.disjunction().add(
							Restrictions.or(
									Restrictions.conjunction()
										.add(Restrictions.eq("activityTaskType.testFactory.testFactoryId", testFactoryId)).add(Restrictions.isNull("activityTaskType.product.productId")), 
									Restrictions.eq("activityTaskType.product.productId", productId)
					)));					
				}else{
					c.add(Restrictions.eq("activityTaskType.testFactory.testFactoryId", testFactoryId));
					c.add(Restrictions.eq("activityTaskType.product.productId", productId));
				}
			}else if(testFactoryId != null && testFactoryId > 0){
				c.add(Restrictions.eq("activityTaskType.testFactory.testFactoryId", testFactoryId));
				c.add(Restrictions.isNull("activityTaskType.product.productId"));
			}else if(productId != null && productId > 0){
				c.add(Restrictions.eq("activityTaskType.product.productId", productId));
			}else{
				c.add(Restrictions.isNull("activityTaskType.testFactory.testFactoryId"));
				c.add(Restrictions.isNull("activityTaskType.product.productId"));
			}
			recordsCount = c.list().size();
		}catch(Exception ex){
			log.error("Error in getTotalRecordsForTaskTypeOfProductPagination ", ex);
		}
		return recordsCount;
	}
	
	@Override
	@Transactional
	public Boolean checkTaskTypeExistForProduct(Integer testFactoryId, Integer productId, String taskTypeName, Integer activityTaskTypeId, Boolean isConsolidated) {
		Boolean isAlreadyExists = false;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ActivityTaskType.class, "activityTaskType");
			if(testFactoryId != null && testFactoryId > 0 && productId != null && productId > 0){
				if(isConsolidated){
					c.add(Restrictions.disjunction().add(
							Restrictions.or(
									Restrictions.conjunction()
										.add(Restrictions.eq("activityTaskType.testFactory.testFactoryId", testFactoryId)).add(Restrictions.isNull("activityTaskType.product.productId")), 
									Restrictions.eq("activityTaskType.product.productId", productId)
					)));					
				}else{
					c.add(Restrictions.eq("activityTaskType.testFactory.testFactoryId", testFactoryId));
					c.add(Restrictions.eq("activityTaskType.product.productId", productId));
				}
			}else if(testFactoryId != null && testFactoryId > 0){
				c.add(Restrictions.eq("activityTaskType.testFactory.testFactoryId", testFactoryId));
				c.add(Restrictions.isNull("activityTaskType.product.productId"));
			}else if(productId != null && productId > 0){
				c.add(Restrictions.eq("activityTaskType.product.productId", productId));
			}else{
				c.add(Restrictions.isNull("activityTaskType.testFactory.testFactoryId"));
				c.add(Restrictions.isNull("activityTaskType.product.productId"));
			}
			c.add(Restrictions.eq("activityTaskType.activityTaskTypeName", taskTypeName));
			if(activityTaskTypeId != null && activityTaskTypeId != 0){
				c.add(Restrictions.ne("activityTaskType.activityTaskTypeId", activityTaskTypeId));
			}
			List<ActivityTaskType> activityTaskTypeList = c.list();
			
			if(activityTaskTypeList != null && activityTaskTypeList.size() > 0){
				isAlreadyExists = true;
			}
			log.debug("checkTaskTypeExistForProduct() successful for product id - "+productId+" - "+isAlreadyExists);
		} catch (RuntimeException re) {
			log.error("checkTaskTypeExistForProduct() failed", re);
		}
		return isAlreadyExists;
	}
	
	@Override
	@Transactional
	public void addActivityTaskType(ActivityTaskType activityTaskType) {
		try {
			sessionFactory.getCurrentSession().save(activityTaskType);
		} catch (RuntimeException re) {
			log.error("Add addActivityTaskType() failed", re);
		}
	}
	
	@Override
	@Transactional
	public void updateActivityTaskType(ActivityTaskType activityTaskType) {
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(activityTaskType);
		} catch (RuntimeException re) {
			log.error("Add updateActivityTaskType() failed", re);
		}
	}

	@Override
	@Transactional
	public int getActivityStatusCategoryIdByTaskStatus(Integer activityId) {
		List<Integer> statusCategoryIdList = new ArrayList<Integer>();
		int statusCategoryId=0;
		ActivityStatus activityStatus = new ActivityStatus();
		Query q = sessionFactory.getCurrentSession().createSQLQuery("SELECT DISTINCT(statusCategoryId) FROM activity_status_master WHERE activityStatusId IN(SELECT statusId FROM activity_task WHERE activityId =" +activityId+") AND statusCategoryId IS NOT NULL ORDER BY statusCategoryId");
		statusCategoryIdList = q.list();
		if(statusCategoryIdList != null && statusCategoryIdList.size()>0){
			statusCategoryId = (int)statusCategoryIdList.get(0);
		}
		return statusCategoryId;
	}	
}
