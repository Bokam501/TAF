package com.hcl.atf.taf.dao.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.dao.ActivityWorkPackageDAO;
import com.hcl.atf.taf.dao.AttachmentDAO;
import com.hcl.atf.taf.dao.ChangeRequestDAO;
import com.hcl.atf.taf.dao.ClarificationTrackerDAO;
import com.hcl.atf.taf.dao.EventsDAO;
import com.hcl.atf.taf.model.Activity;
import com.hcl.atf.taf.model.ActivityWorkPackage;
import com.hcl.atf.taf.model.Customer;
import com.hcl.atf.taf.model.EntityUserGroup;
import com.hcl.atf.taf.model.Event;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.dto.ActivityWorkPackageSummaryDTO;
import com.hcl.atf.taf.model.dto.ProductAWPSummaryDTO;
import com.hcl.atf.taf.model.json.JsonActivityWorkPackage;

@Repository
public class ActivityWorkPackageDAOImpl implements ActivityWorkPackageDAO {
	private static final Log log = LogFactory.getLog(ActivityWorkPackageDAOImpl.class);
	
	@Value("#{ilcmProps['ACTIVITY_TASK_WORKPACKAGE_LIST_REVERSE_ORDER']}")
    private String activityTaskWorkpackageListOrder;
	
	@Autowired(required=true)
	private SessionFactory sessionFactory;
	@Autowired
	private ClarificationTrackerDAO clarificationTrackerDAO;
	@Autowired
	private ChangeRequestDAO changeRequestDAO;
	@Autowired
	private AttachmentDAO attachmentDAO;
	@Autowired
	private EventsDAO eventsDAO;

	@Override
	@Transactional
	public List<ActivityWorkPackage> listActivityWorkPackages() {
		log.debug("listing all WorkRequest instance");
		List<ActivityWorkPackage> workRequestList = null;
		try {
			workRequestList = sessionFactory.getCurrentSession().createQuery("from ActivityWorkPackage").list();
			log.debug("list all WorkRequest successful");
		} catch (RuntimeException re) {
			log.error("list all WorkRequest failed", re);
		}
		return workRequestList;
	}
	
	



	@Override
	@Transactional
	public void addActivityWorkPackage(ActivityWorkPackage activityWorkPackage) {
		log.info("adding WorkRequest instance");
		try {	
			sessionFactory.getCurrentSession().save(activityWorkPackage);
			log.info("add ActivityWorkPackage successful");
		} catch (RuntimeException re) {
			log.error("add ActivityWorkPackage failed", re);
		}		
	}

	
	@Override
	@Transactional
	public void updateActivityWorkPackage(ActivityWorkPackage activityWorkPackage)  {
		log.debug("updating WorkRequest instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(activityWorkPackage);
			log.debug("update ActivityWorkPackage successful");
		} catch (RuntimeException re) {
			log.error("update ActivityWorkPackage failed", re);
		}
	}

	
	@Override
	@Transactional
	public void deleteActivityWorkPackage(ActivityWorkPackage activityWorkPackage) {
		
		log.debug("reactivate ProductBuild instance");
		try {
			sessionFactory.getCurrentSession().delete(activityWorkPackage);
			log.debug("ActivityWorkPackage deletion successful");
		} catch (RuntimeException re) {
			log.error("ActivityWorkPackage deletion failed", re);
		}		
	}



	@Override
	@Transactional
	public List<ActivityWorkPackage> listActivityWorkPackagesByBuildId(Integer productBuildId,int isActive) {
		log.debug("listing all ActivityWorkPackage instance");
		List<ActivityWorkPackage> workRequestList = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ActivityWorkPackage.class, "activityWorkPackage");		
			c.add(Restrictions.eq("activityWorkPackage.productBuild.productBuildId", productBuildId));
			if (isActive != 2){
				c.add(Restrictions.eq("activityWorkPackage.isActive", isActive));
			}
			
			if(activityTaskWorkpackageListOrder.equalsIgnoreCase("YES")){
				c.addOrder(Order.desc("activityWorkPackageId"));
			}
			workRequestList = c.list();		
			log.debug("list all ActivityWorkPackage successful");
		}
		catch (RuntimeException re) {
			log.error("list all ActivityWorkPackage failed", re);
		}
		return workRequestList;
	}
	
	@Override
	@Transactional
	public List<ActivityWorkPackage> listActivityWorkPackagesByProductId(Integer productId,Integer productVersionId,Integer productBuildId,int isActive,UserList user, Map<String, String> searchString) {
		log.debug("listing all ActivityWorkPackage instance");
		List<ActivityWorkPackage> workRequestList = null;
		
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ActivityWorkPackage.class, "activityWorkPackage");		
			
		      c.createAlias("activityWorkPackage.productBuild", "pbuild");
		      c.createAlias("pbuild.productVersion", "pVersion");
		      c.createAlias("pVersion.productMaster", "product");
		      c.createAlias("activityWorkPackage.owner", "assignee");
		      c.createAlias("activityWorkPackage.statusCategory", "status");
		      c.createAlias("activityWorkPackage.priority", "priority");
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
				     c.add(Restrictions.eq("activityWorkPackage.isActive", isActive));
			         }
					
				    if(searchString != null && searchString.size()>0&& !searchString.isEmpty()){
				    	String assigneeId = searchString.get("searchassigneeId");
					    String priorityId= searchString.get("searchpriority");
					    String statusId= searchString.get("searchstatusId");
					    if( statusId!=null && assigneeId!=null && priorityId!=null){							
							
							if(!statusId.equals("-1") && !assigneeId.equals("-1")&&!priorityId.equals("-1"))
								c.add(Restrictions.and(Restrictions.eq("assignee.userId", Integer.parseInt(assigneeId)), Restrictions.eq("priority.executionPriorityId",Integer.parseInt(priorityId)), Restrictions.eq("status.statusCategoryId",Integer.parseInt(statusId))));
							if(!statusId.equals("-1") && assigneeId.equals("-1")&&priorityId.equals("-1"))
								c.add(Restrictions.eq("status.statusCategoryId",Integer.parseInt(statusId)));	
							if(statusId.equals("-1") && !assigneeId.equals("-1")&& priorityId.equals("-1"))
								c.add(Restrictions.eq("assignee.userId",Integer.parseInt(assigneeId)));	
							if(statusId.equals("-1") && assigneeId.equals("-1")&&  !priorityId.equals("-1"))
								c.add(Restrictions.eq("priority.executionPriorityId",Integer.parseInt(priorityId)));	
							if(!statusId.equals("-1") && !assigneeId.equals("-1") && priorityId.equals("-1"))
								c.add(Restrictions.and(Restrictions.eq("assignee.userId", Integer.parseInt(assigneeId)),Restrictions.eq("status.statusCategoryId", Integer.parseInt(statusId))));
							if(statusId.equals("-1") && !assigneeId.equals("-1")&&!priorityId.equals("-1"))
								c.add(Restrictions.and(Restrictions.eq("assignee.userId", Integer.parseInt(assigneeId)),Restrictions.eq("priority.executionPriorityId", Integer.parseInt(priorityId))));
							if(!statusId.equals("-1") && assigneeId.equals("-1")&&!priorityId.equals("-1"))
								c.add(Restrictions.and(Restrictions.eq("status.statusCategoryId", Integer.parseInt(statusId)),Restrictions.eq("priority.executionPriorityId", Integer.parseInt(priorityId))));
					}
				    }
				   
				    if(activityTaskWorkpackageListOrder.equalsIgnoreCase("YES")){
				    	c.addOrder(Order.desc("activityWorkPackageId"));
				    }
				    
				    if(user.getUserRoleMaster().getUserRoleId() != IDPAConstants.ROLE_ID_ADMIN){
				    	 DetachedCriteria detachedCriteria = DetachedCriteria.forClass(EntityUserGroup.class,"entityUserGroup");
					      detachedCriteria.createAlias("entityUserGroup.entityTypeId", "entityType");
					      detachedCriteria.createAlias("entityUserGroup.user", "user");
					      detachedCriteria.add(Restrictions.eq("entityType.entitymasterid",IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE_ID));
					      detachedCriteria.add(Restrictions.eq("user.userId",user.getUserId()));
					      detachedCriteria.setProjection(Property.forName("entityUserGroup.entityInstanceId"));
					      c.add(Subqueries.propertyIn("activityWorkPackageId", detachedCriteria));
				    }
				    
				    
					workRequestList = c.list();	
					
					if(workRequestList != null && !workRequestList.isEmpty()){
						for (ActivityWorkPackage activityWorkPackage : workRequestList) {
							Hibernate.initialize(activityWorkPackage);
							if(activityWorkPackage.getProductBuild() != null){
								Hibernate.initialize(activityWorkPackage.getProductBuild());
								if(activityWorkPackage.getProductBuild().getProductVersion() != null){
									Hibernate.initialize(activityWorkPackage.getProductBuild().getProductVersion());
									if(activityWorkPackage.getProductBuild().getProductVersion().getProductMaster() != null){
										Hibernate.initialize(activityWorkPackage.getProductBuild().getProductVersion().getProductMaster());
										if(activityWorkPackage.getProductBuild().getProductVersion().getProductMaster().getTestFactory() != null){
											Hibernate.initialize(activityWorkPackage.getProductBuild().getProductVersion().getProductMaster().getTestFactory());
										}	
									}	
								}	
							}				
						}
					}			
					log.debug("list all ActivityWorkPackage successful");
		}
		catch (RuntimeException re) {
			log.error("list all ActivityWorkPackage failed", re);
		}
		return workRequestList;
	}

	@Override
	@Transactional
	public Integer listActivityWorkPackagesByProductIdCount(Integer productId,Integer productVersionId,Integer productBuildId,Integer isActive, Map<String, String> searchString) {
		Integer workRequestCount = 0;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ActivityWorkPackage.class, "activityWorkPackage");		
			
		      c.createAlias("activityWorkPackage.productBuild", "pbuild");
		      c.createAlias("pbuild.productVersion", "pVersion");
		      c.createAlias("pVersion.productMaster", "product");
		      c.createAlias("activityWorkPackage.owner", "assignee");
		      c.createAlias("activityWorkPackage.statusCategory", "status");
		      c.createAlias("activityWorkPackage.priority", "priority");
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
				     c.add(Restrictions.eq("activityWorkPackage.isActive", isActive));
			         }
					
				    if(searchString != null && searchString.size()>0&& !searchString.isEmpty()){
				    	String assigneeId = searchString.get("searchassigneeId");
					    String priorityId= searchString.get("searchpriority");
					    String statusId= searchString.get("searchstatusId");
					    if( statusId!=null && assigneeId!=null && priorityId!=null){							
							
							if(!statusId.equals("-1") && !assigneeId.equals("-1")&&!priorityId.equals("-1"))
								c.add(Restrictions.and(Restrictions.eq("assignee.userId", Integer.parseInt(assigneeId)), Restrictions.eq("priority.executionPriorityId",Integer.parseInt(priorityId)), Restrictions.eq("status.statusCategoryId",Integer.parseInt(statusId))));
							if(!statusId.equals("-1") && assigneeId.equals("-1")&&priorityId.equals("-1"))
								c.add(Restrictions.eq("status.statusCategoryId",Integer.parseInt(statusId)));	
							if(statusId.equals("-1") && !assigneeId.equals("-1")&& priorityId.equals("-1"))
								c.add(Restrictions.eq("assignee.userId",Integer.parseInt(assigneeId)));	
							if(statusId.equals("-1") && assigneeId.equals("-1")&&  !priorityId.equals("-1"))
								c.add(Restrictions.eq("priority.executionPriorityId",Integer.parseInt(priorityId)));	
							if(!statusId.equals("-1") && !assigneeId.equals("-1") && priorityId.equals("-1"))
								c.add(Restrictions.and(Restrictions.eq("assignee.userId", Integer.parseInt(assigneeId)),Restrictions.eq("status.statusCategoryId", Integer.parseInt(statusId))));
							if(statusId.equals("-1") && !assigneeId.equals("-1")&&!priorityId.equals("-1"))
								c.add(Restrictions.and(Restrictions.eq("assignee.userId", Integer.parseInt(assigneeId)),Restrictions.eq("priority.executionPriorityId", Integer.parseInt(priorityId))));
							if(!statusId.equals("-1") && assigneeId.equals("-1")&&!priorityId.equals("-1"))
								c.add(Restrictions.and(Restrictions.eq("status.statusCategoryId", Integer.parseInt(statusId)),Restrictions.eq("priority.executionPriorityId", Integer.parseInt(priorityId))));
					}
			    }
			   
			    if(activityTaskWorkpackageListOrder.equalsIgnoreCase("YES")){
			    	c.addOrder(Order.desc("activityWorkPackageId"));
			    }
			    
			c.setProjection(Projections.rowCount());
			workRequestCount = ((Long)c.uniqueResult()).intValue();		
		}
		catch (RuntimeException re) {
			log.error("Error in listActivityWorkPackagesByProductIdCount - ", re);
		}
		return workRequestCount;
	}
	
	@Override
	@Transactional
	public List<ActivityWorkPackage> listActivityWorkPackagesByTestFactoryId(Integer testFactoryId, Integer productId, Integer isActive,
			Map<String, String> searchString, Integer jtStartIndex, Integer jtPageSize) {
		log.debug("listing all listActivityWorkPackagesByTestFactoryId instance");
		List<ActivityWorkPackage> workRequestList = null;		
		try {			
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ActivityWorkPackage.class, "activityWorkPackage");		
			
		      c.createAlias("activityWorkPackage.productBuild", "pbuild");
		      c.createAlias("pbuild.productVersion", "pVersion");
		      c.createAlias("pVersion.productMaster", "product");
		      c.createAlias("product.testFactory", "testFactory");
		      if(testFactoryId != -1){
				c.add(Restrictions.eq("testFactory.testFactoryId", testFactoryId));
		      }
		      if(productId != -1){
					c.add(Restrictions.eq("product.productId", productId));
			      }
		      if (isActive != 2){
		    	  c.add(Restrictions.eq("activityWorkPackage.isActive", isActive));
		      }
		      if(searchString != null && searchString.size()>0&& !searchString.isEmpty()){
			    	String assigneeId = searchString.get("searchassigneeId");
				    String priorityId= searchString.get("searchpriority");
				    String statusId= searchString.get("searchstatusId");
				    if( statusId!=null && assigneeId!=null && priorityId!=null){							
						
						if(!statusId.equals("-1") && !assigneeId.equals("-1")&&!priorityId.equals("-1"))
							c.add(Restrictions.and(Restrictions.eq("assignee.userId", Integer.parseInt(assigneeId)), Restrictions.eq("priority.executionPriorityId",Integer.parseInt(priorityId)), Restrictions.eq("status.statusCategoryId",Integer.parseInt(statusId))));
						if(!statusId.equals("-1") && assigneeId.equals("-1")&&priorityId.equals("-1"))
							c.add(Restrictions.eq("status.statusCategoryId",Integer.parseInt(statusId)));	
						if(statusId.equals("-1") && !assigneeId.equals("-1")&& priorityId.equals("-1"))
							c.add(Restrictions.eq("assignee.userId",Integer.parseInt(assigneeId)));	
						if(statusId.equals("-1") && assigneeId.equals("-1")&&  !priorityId.equals("-1"))
							c.add(Restrictions.eq("priority.executionPriorityId",Integer.parseInt(priorityId)));	
						if(!statusId.equals("-1") && !assigneeId.equals("-1") && priorityId.equals("-1"))
							c.add(Restrictions.and(Restrictions.eq("assignee.userId", Integer.parseInt(assigneeId)),Restrictions.eq("status.statusCategoryId", Integer.parseInt(statusId))));
						if(statusId.equals("-1") && !assigneeId.equals("-1")&&!priorityId.equals("-1"))
							c.add(Restrictions.and(Restrictions.eq("assignee.userId", Integer.parseInt(assigneeId)),Restrictions.eq("priority.executionPriorityId", Integer.parseInt(priorityId))));
						if(!statusId.equals("-1") && assigneeId.equals("-1")&&!priorityId.equals("-1"))
							c.add(Restrictions.and(Restrictions.eq("status.statusCategoryId", Integer.parseInt(statusId)),Restrictions.eq("priority.executionPriorityId", Integer.parseInt(priorityId))));
				    }
		      }
			   
		      if(activityTaskWorkpackageListOrder.equalsIgnoreCase("YES")){
			    	c.addOrder(Order.desc("activityWorkPackageId"));
		      }
		      c.addOrder(Order.desc("activityWorkPackageId"));
		      
		      c.setFirstResult(jtStartIndex);
	          c.setMaxResults(jtPageSize);
			workRequestList = c.list();		
			for (ActivityWorkPackage activeWp : workRequestList) {
				if(activeWp.getProductBuild() != null){
					Hibernate.initialize(activeWp.getProductBuild());	
					if(activeWp.getProductBuild().getProductVersion() != null){
						Hibernate.initialize(activeWp.getProductBuild().getProductVersion());	
						if(activeWp.getProductBuild().getProductVersion().getProductMaster() != null){
							Hibernate.initialize(activeWp.getProductBuild().getProductVersion().getProductMaster());
							if(activeWp.getProductBuild().getProductVersion().getProductMaster().getCustomer() != null){
								Customer customer=activeWp.getProductBuild().getProductVersion().getProductMaster().getCustomer();
								Hibernate.initialize(customer);
							}
						}
					}
				}				
				Hibernate.initialize(activeWp.getOwner());
				Hibernate.initialize(activeWp.getStatusCategory());
				Hibernate.initialize(activeWp.getPriority());
				Hibernate.initialize(activeWp.getCompetency());
				if(activeWp.getActivity().size()>0){
					Hibernate.initialize(activeWp.getActivity());
				}
			}
			log.debug("list all listActivityWorkPackagesByTestFactoryId successful");
		}
		catch (RuntimeException re) {
			log.error("list all listActivityWorkPackagesByTestFactoryId failed", re);
		}
		return workRequestList;
	}
	
	@Override
	@Transactional
	public List<JsonActivityWorkPackage> listJsonActivityWorkPackagesByTestFactoryId(Integer testFactoryId, Integer productId, Integer isActive,
			Map<String, String> searchString, UserList user,Integer jtStartIndex, Integer jtPageSize) {
		log.debug("listing all listJsonActivityWorkPackagesByTestFactoryId instance");
		List<ActivityWorkPackage> workRequestList = null;		
		List<JsonActivityWorkPackage> jsonActivityWPList = new ArrayList<JsonActivityWorkPackage>();
		try {			
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ActivityWorkPackage.class, "activityWorkPackage");		
			
		      c.createAlias("activityWorkPackage.productBuild", "pbuild");
		      c.createAlias("pbuild.productVersion", "pVersion");
		      c.createAlias("pVersion.productMaster", "product");
		      c.createAlias("product.testFactory", "testFactory");
		      if(testFactoryId != -1){
				c.add(Restrictions.eq("testFactory.testFactoryId", testFactoryId));
		      }
		      if(productId != -1){
					c.add(Restrictions.eq("product.productId", productId));
			      }
		      if (isActive != 2){
		    	  c.add(Restrictions.eq("activityWorkPackage.isActive", isActive));
		      }
		      if(searchString != null && searchString.size()>0&& !searchString.isEmpty()){
			    	String assigneeId = searchString.get("searchassigneeId");
				    String priorityId= searchString.get("searchpriority");
				    String statusId= searchString.get("searchstatusId");
				    if( statusId!=null && assigneeId!=null && priorityId!=null){							
						
						if(!statusId.equals("-1") && !assigneeId.equals("-1")&&!priorityId.equals("-1"))
							c.add(Restrictions.and(Restrictions.eq("assignee.userId", Integer.parseInt(assigneeId)), Restrictions.eq("priority.executionPriorityId",Integer.parseInt(priorityId)), Restrictions.eq("status.statusCategoryId",Integer.parseInt(statusId))));
						if(!statusId.equals("-1") && assigneeId.equals("-1")&&priorityId.equals("-1"))
							c.add(Restrictions.eq("status.statusCategoryId",Integer.parseInt(statusId)));	
						if(statusId.equals("-1") && !assigneeId.equals("-1")&& priorityId.equals("-1"))
							c.add(Restrictions.eq("assignee.userId",Integer.parseInt(assigneeId)));	
						if(statusId.equals("-1") && assigneeId.equals("-1")&&  !priorityId.equals("-1"))
							c.add(Restrictions.eq("priority.executionPriorityId",Integer.parseInt(priorityId)));	
						if(!statusId.equals("-1") && !assigneeId.equals("-1") && priorityId.equals("-1"))
							c.add(Restrictions.and(Restrictions.eq("assignee.userId", Integer.parseInt(assigneeId)),Restrictions.eq("status.statusCategoryId", Integer.parseInt(statusId))));
						if(statusId.equals("-1") && !assigneeId.equals("-1")&&!priorityId.equals("-1"))
							c.add(Restrictions.and(Restrictions.eq("assignee.userId", Integer.parseInt(assigneeId)),Restrictions.eq("priority.executionPriorityId", Integer.parseInt(priorityId))));
						if(!statusId.equals("-1") && assigneeId.equals("-1")&&!priorityId.equals("-1"))
							c.add(Restrictions.and(Restrictions.eq("status.statusCategoryId", Integer.parseInt(statusId)),Restrictions.eq("priority.executionPriorityId", Integer.parseInt(priorityId))));
				    }
		      }
			  
		      if(user.getUserRoleMaster().getUserRoleId() != IDPAConstants.ROLE_ID_ADMIN){
		    	  DetachedCriteria detachedCriteria = DetachedCriteria.forClass(EntityUserGroup.class,"entityUserGroup");
			      detachedCriteria.createAlias("entityUserGroup.entityTypeId", "entityType");
			      detachedCriteria.createAlias("entityUserGroup.user", "user");
			      detachedCriteria.add(Restrictions.eq("entityType.entitymasterid",IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE_ID));
			      detachedCriteria.add(Restrictions.eq("user.userId",user.getUserId()));
			      detachedCriteria.setProjection(Property.forName("entityUserGroup.entityInstanceId"));
			      c.add(Subqueries.propertyIn("activityWorkPackageId", detachedCriteria));
		      }
		     
		      
		      if(activityTaskWorkpackageListOrder.equalsIgnoreCase("YES")){
			    	c.addOrder(Order.desc("activityWorkPackageId"));
		      }
		      c.addOrder(Order.desc("activityWorkPackageId"));
		      
		      c.setFirstResult(jtStartIndex);
	          c.setMaxResults(jtPageSize);
			workRequestList = c.list();		
			
			for (ActivityWorkPackage activeWp : workRequestList) {
				if(activeWp.getProductMaster() != null){
					Hibernate.initialize(activeWp.getProductMaster());	
				}
				if(activeWp.getProductBuild() != null){
					Hibernate.initialize(activeWp.getProductBuild());	
					if(activeWp.getProductBuild().getProductVersion() != null){
						Hibernate.initialize(activeWp.getProductBuild().getProductVersion());	
						if(activeWp.getProductBuild().getProductVersion().getProductMaster() != null){
							Hibernate.initialize(activeWp.getProductBuild().getProductVersion().getProductMaster());
							if(activeWp.getProductBuild().getProductVersion().getProductMaster().getCustomer() != null){
								Customer customer=activeWp.getProductBuild().getProductVersion().getProductMaster().getCustomer();
								Hibernate.initialize(customer);
							}
						}
					}
				}				
				Hibernate.initialize(activeWp.getOwner());
				Hibernate.initialize(activeWp.getStatusCategory());
				Hibernate.initialize(activeWp.getPriority());
				Hibernate.initialize(activeWp.getCompetency());
				if(activeWp.getActivity().size()>0){
					Hibernate.initialize(activeWp.getActivity());
				}
				JsonActivityWorkPackage jsonActivityWorkPackage = new JsonActivityWorkPackage(activeWp);
				jsonActivityWPList.add(jsonActivityWorkPackage);
			}
			log.debug("list all listJsonActivityWorkPackages ByTestFactoryId successful");
		}
		catch (RuntimeException re) {
			log.error("list all listJsonActivityWorkPackages ByTestFactoryId failed", re);
		}
		return jsonActivityWPList;
	}
	
	@Override
	@Transactional
	public Integer getActivityWPCountByTestFactoryIdProductId(int testFactoryId, Integer productId) {
		log.debug("listing getActivityWorkPackagesCountByTestFactoryIdProductId instance");
		int activityWPCount = 0;		
		try {
			String sql="SELECT  count(awp.activityWorkPackageId) as activityWPIdCount FROM activity_work_package awp "
					+ "inner join product_build pb on awp.productbuild = pb.productBuildId "
					+ "inner join product_version_list_master pvlm on pb.productVersionId = pvlm.productVersionListId "
					+ "inner join product_master pm on pvlm.productId = pm.productId ";
			
			if(testFactoryId == -1){//Product  Level
				sql = sql + "where pm.productId="+productId+" and awp.isActive=1 order by activityWPIdCount desc";
				
			}else if(productId == -1){//TestFactory Level				
				sql = sql  +  "inner join test_factory tf on pm.testFactoryId = tf.testFactoryId "
						+ "where tf.testFactoryId="+testFactoryId+" and awp.isActive=1 order by activityWPIdCount desc ";	
			}

			activityWPCount = ((Number) sessionFactory.getCurrentSession().createSQLQuery(sql).uniqueResult()).intValue();
		} catch (RuntimeException re) {
			log.error("list getActivityWorkPackagesCountByTestFactoryIdProductId", re);
		}
		return activityWPCount;
	}
	
	@Override
	@Transactional
	public ActivityWorkPackage getActivityWorkPackageById(int activityWorkPackageId, Integer initializationLevel) {
		List<ActivityWorkPackage> workRequestList = null;
		ActivityWorkPackage activeWp=null;
		try {
			workRequestList = sessionFactory.getCurrentSession().createQuery("from ActivityWorkPackage wr where wr.activityWorkPackageId=:activityWorkPackageId")
			.setParameter("activityWorkPackageId",activityWorkPackageId).list();			
			if(workRequestList.size()!=0){
				activeWp=workRequestList.get(0);				
				if(initializationLevel == 1){
					Hibernate.initialize(activeWp.getProductBuild());
					Hibernate.initialize(activeWp.getProductBuild().getProductVersion());
					Hibernate.initialize(activeWp.getProductBuild().getProductVersion().getProductMaster());
					Hibernate.initialize(activeWp.getOwner());
					Hibernate.initialize(activeWp.getStatusCategory());
					Hibernate.initialize(activeWp.getPriority());
					Hibernate.initialize(activeWp.getCompetency());
					
					
				}	
				if(initializationLevel == 0){
					Customer customer=activeWp.getProductBuild().getProductVersion().getProductMaster().getCustomer();
					Hibernate.initialize(customer);
					if(activeWp.getActivity().size()>0){
						Hibernate.initialize(activeWp.getActivity());
					}
				}
				if(initializationLevel == 2){
					Hibernate.initialize(activeWp.getProductBuild().getProductVersion().getProductMaster());
					Hibernate.initialize(activeWp.getOwner());
					Hibernate.initialize(activeWp.getStatusCategory());
					Hibernate.initialize(activeWp.getPriority());
					Hibernate.initialize(activeWp.getCompetency());
					Customer customer=activeWp.getProductBuild().getProductVersion().getProductMaster().getCustomer();
					Hibernate.initialize(customer);
					if(activeWp.getActivity().size()>0){
						Hibernate.initialize(activeWp.getActivity());
					}
				}
			}
			
		} catch (RuntimeException re) {
			log.error("list all ActivityWorkPackage failed", re);
		}
		return activeWp;
	}
	@Override
	@Transactional
	public void updateActivity(
			Activity ActivityFromUI) {
		log.debug("updating workPackageFeatureExecutionPlanFromUI instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(ActivityFromUI);
			
			log.debug("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
			//throw re;
		}
	}
	
	public ActivityWorkPackage getActivityWorkPackageByName(String activityWpName, Integer productBuildId) {
		List<ActivityWorkPackage> activityWorkPackages = null;
		ActivityWorkPackage activityWorkPackage = null;
		String hql = "from ActivityWorkPackage awp where awp.activityWorkPackageName = :activityWorkPackageName and awp.productBuild.productBuildId = :productBuildId";
		try {
			activityWorkPackages = sessionFactory.getCurrentSession().createQuery(hql)
					.setParameter("activityWorkPackageName", activityWpName)
					.setParameter("productBuildId", productBuildId).list();
			activityWorkPackage = ((activityWorkPackages!=null && !activityWorkPackages.isEmpty())?activityWorkPackages.get(0):null);
		} catch (RuntimeException re) {
			log.error("Error in TestCaseList retrieval by Code and Product : ", re);
		}
		return activityWorkPackage;
	}

	@Override
	@Transactional
	public String deleteActivityWorkPackage(int activityWorkPackageId, String referencedTableName, String referencedColumnName) {
		boolean isMappingAvailable=false;
		String message = "Delete all references which are mapped with this ActivityWorkPackage and try again";
		try{
			String sql = "SELECT Table_name, Column_name FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE WHERE REFERENCED_TABLE_NAME = '"+referencedTableName+"' AND REFERENCED_COLUMN_NAME = '"+referencedColumnName+"'";
			Session session = sessionFactory.getCurrentSession();
			List list = session.createSQLQuery(sql).setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();
			for(Object o : list) {
				Map row = (Map)o;
				String queryForChild = "select count(*) from "+row.get("Table_name")+" where "+row.get("Column_name")+"="+activityWorkPackageId;
				int count = ((BigInteger)session.createSQLQuery(queryForChild).uniqueResult()).intValue();
				if(count > 0){
					isMappingAvailable = true;
				}
			}
			if(!isMappingAvailable) {
				String hql = "delete from ActivityWorkPackage where activityWorkPackageId =:activityWorkPackageId";
				session.createQuery(hql).setInteger("activityWorkPackageId", activityWorkPackageId).executeUpdate();
				log.info("deletion of ActivityWorkPackage successful");
				message="OK";
			}
		 }catch (RuntimeException re) {
			 log.error("deletion of ActivityWorkPackage failed", re);
			}
		return message;
		}





	@Override
	@Transactional
	public List<ActivityWorkPackage> getActivityWorkPackageByBuildId(Integer buildId) {
		log.debug("listing all ActivityWorkPackage instance");
		List<ActivityWorkPackage> workRequestList = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ActivityWorkPackage.class, "activityWorkPackage");		
			c.add(Restrictions.eq("activityWorkPackage.productBuild.productBuildId", buildId));
			workRequestList = c.list();		
			log.debug("list all ActivityWorkPackage successful");
		}
		catch (RuntimeException re) {
			log.error("list all ActivityWorkPackage failed", re);
		}
		return workRequestList;
	
	}
	
	@Override
	@Transactional
	public ActivityWorkPackageSummaryDTO listActivityWorkPackageSummaryDetails(Integer activityWorkPackageId)
	{
		
		ActivityWorkPackageSummaryDTO activityWPSummaryDTO;
		List<ActivityWorkPackageSummaryDTO> aWPDTOList = new ArrayList<ActivityWorkPackageSummaryDTO>();
		activityWPSummaryDTO = new ActivityWorkPackageSummaryDTO();
		try {

			String sql="select awp.activityWorkPackageId as activityWorkPackageId, awp.activityWorkPackageName as activityWorkPackageName, "
					+ "awp.description as description, awp.plannedStartDate as plannedStartDate, awp.plannedEndDate as plannedEndDate, "				
					+ "awp.actualStartDate as actualStartDate, awp.actualEndDate as actualEndDate, "
					+ "awp.isActive as isActive, pb.productBuildId as productBuildId, pb.buildName as productBuildName, "
					
					+ "pvlm.productVersionListId as productVersionListId, pvlm.productVersionName as productVersionName, "
					
					
					+ "pm.productId as productId, pm.productName as productName, ulowner.userId as ownerId, ulowner.loginId as ownerName, "
					+ "scat.statusCategoryId as statusCategoryId, scat.statusCategoryName as statusCategoryName, "
					+ " wwfstatus.workflowStatusName as workflowStatusName, "
					+ " wwfstatus.workflowStatusDisplayName as workflowStatusDisplayName, "
					
					
					+"(select count(*) from activity where activityWorkPackageId = "+activityWorkPackageId+") AS activityCount, "
					
					+ " (select count(*) from clarification_tracker as clart where clart.entityTypeId=28 "
					+ "and clart.entityInstanceId in(select act.activityId from activity act where act.activityWorkPackageId = "+activityWorkPackageId+") ) AS clarificationCount, "
					
					+ "(select count(*) from change_request cr where cr.entityTypeId=28 "
					+ "and cr.entityInstanceId in(select activityId from activity where activityWorkPackageId = "+activityWorkPackageId+")) AS changeRequestCount, "
					
					+ "(select count(*) from attachment att where att.entityMasterId=28 "
					+ "and att.entityId in(select activityId from activity where activityWorkPackageId = "+activityWorkPackageId+")) AS attachmentCount, "
					
					+ "(SELECT count( wfs.workflowStatusId) FROM activity act "
					+ "INNER JOIN wf_workflows_status wfs ON act.workflowStatusId = wfs.workflowStatusId "
					+ " where act.activityWorkPackageId = "+activityWorkPackageId+" and wfs.workflowStatusType = 'Begin') AS actBeginCount, "
					
					+ "(SELECT count( wfs.workflowStatusId) FROM activity act "
					+ "INNER JOIN wf_workflows_status wfs ON act.workflowStatusId = wfs.workflowStatusId "
					+ " where act.activityWorkPackageId = "+activityWorkPackageId+" and wfs.workflowStatusType = 'Intermediate') AS actIntermediateCount, "
										
					+ "(SELECT count( wfs.workflowStatusId) FROM activity act "
					+ "INNER JOIN wf_workflows_status wfs ON act.workflowStatusId = wfs.workflowStatusId "
					+ " where act.activityWorkPackageId = "+activityWorkPackageId+" and wfs.workflowStatusType = 'Abort') AS actAbortCount, "
									
					+ "(SELECT count( wfs.workflowStatusId) FROM activity act "
					+ "INNER JOIN wf_workflows_status wfs ON act.workflowStatusId = wfs.workflowStatusId "
					+ " where act.activityWorkPackageId = "+activityWorkPackageId+" and wfs.workflowStatusType = 'End') AS actEndCount "
					
					+ "from activity_work_package awp "
					+ "inner join product_build pb on awp.productbuild = pb.productBuildId "
					+ "inner join product_version_list_master pvlm on pb.productVersionId =  pvlm.productVersionListId "
					+ "inner join product_master pm on pvlm.productId = pm.productId "
					+ "inner join user_list ulowner on awp.owner = ulowner.userId "
					
					+ "inner join user_list ulcreated on awp.createdBy = ulcreated.userId "
					+ "inner join status_category scat on awp.statusCategoryId = scat.statusCategoryId "
					+ "inner join wf_workflows_status wwfstatus on awp.workflowStatusId = wwfstatus.workflowStatusId "
					+ "where awp.activityWorkPackageId="+activityWorkPackageId;

			
			aWPDTOList=sessionFactory.getCurrentSession().createSQLQuery(sql)
					.addScalar("activityWorkPackageId",StandardBasicTypes.INTEGER ).addScalar("activityWorkPackageName", StandardBasicTypes.STRING)
					.addScalar("description", StandardBasicTypes.STRING)
					.addScalar("plannedStartDate",StandardBasicTypes.DATE ).addScalar("plannedEndDate",StandardBasicTypes.DATE )
					
					.addScalar("actualStartDate", StandardBasicTypes.DATE).addScalar("actualEndDate", StandardBasicTypes.DATE)
					.addScalar("isActive", StandardBasicTypes.INTEGER)
					.addScalar("productBuildId", StandardBasicTypes.INTEGER)
					.addScalar("productBuildName", StandardBasicTypes.STRING)
					.addScalar("productVersionListId", StandardBasicTypes.INTEGER)
					.addScalar("productVersionName", StandardBasicTypes.STRING)
					
					.addScalar("productId", StandardBasicTypes.INTEGER)
					.addScalar("productName", StandardBasicTypes.STRING)
					
					.addScalar("ownerId", StandardBasicTypes.INTEGER)
					.addScalar("ownerName", StandardBasicTypes.STRING)
					
					
					.addScalar("statusCategoryId", StandardBasicTypes.INTEGER)
					.addScalar("statusCategoryName", StandardBasicTypes.STRING)
					
					.addScalar("workflowStatusName", StandardBasicTypes.STRING)
					.addScalar("workflowStatusDisplayName", StandardBasicTypes.STRING)
					.addScalar("clarificationCount", StandardBasicTypes.INTEGER)
					.addScalar("changeRequestCount", StandardBasicTypes.INTEGER)
					.addScalar("attachmentCount", StandardBasicTypes.INTEGER)
					.addScalar("actBeginCount", StandardBasicTypes.INTEGER)
					.addScalar("actIntermediateCount", StandardBasicTypes.INTEGER)
					.addScalar("actAbortCount", StandardBasicTypes.INTEGER)
					.addScalar("actEndCount", StandardBasicTypes.INTEGER)
					.addScalar("activityCount",StandardBasicTypes.INTEGER)
									
					.setResultTransformer(Transformers.aliasToBean(ActivityWorkPackageSummaryDTO.class)).list();
			if(aWPDTOList.size()>0){
				activityWPSummaryDTO=aWPDTOList.get(0);
			}
			
		} catch (HibernateException e) {
			log.error("ERROR listing ActivityWorkPackageSummary Details : ",e);
		}	
		return activityWPSummaryDTO;
	}
	

	@Override
	@Transactional
	public ProductAWPSummaryDTO listProductAWPSummaryDetails(Integer productId)
	{
		log.debug("listProductAWPSummaryDetails");
		ProductAWPSummaryDTO productAWPSummaryDTO;
		List<ProductAWPSummaryDTO> productAWPList = new ArrayList<ProductAWPSummaryDTO>();
		
		productAWPSummaryDTO = new ProductAWPSummaryDTO();		
		try {

			String sql="SELECT pm.productId as productId, pm.productName as productName, pm.productDescription as description, "
					+ "tf.testFactoryId as testFactoryId, tf.testFactoryName as testFactoryName, "
					+ "pvlm.productVersionListId as productVersionListId, pvlm.productVersionName as productVersionName, "
					+ "build.productBuildId as productBuildId, build.buildName as buildname, "
					+ "cust.customerId as customerId, cust.customerName as customerName, "
					+ "pm.projectCode as projectCode, pm.projectName as projectName, "
					+ "prodType.productTypeId as productTypeId, prodType.typeName as typeName,  "
					+ "prodmode.modeId as modeId, prodmode.modeName as modeName, "
					+ "pm.createdDate as createdDate "
					+ "FROM product_master pm "
					+ "inner join test_factory tf on pm.testFactoryId = tf.testFactoryId "
					+ "inner join product_version_list_master pvlm on pm.productId = pvlm.productId "
					+ "inner join product_build build on pvlm.productVersionListId = build.productVersionId "
					+ "inner join customer cust on pm.customerId = cust.customerId "
					+ "inner join product_type prodType on pm.productTypeId = prodType.productTypeId "
					+ "inner join product_mode prodmode on pm.modeId = prodmode.modeId "
					+ "WHERE pm.productId ="+productId;
			
			productAWPList=sessionFactory.getCurrentSession().createSQLQuery(sql)
					.addScalar("productId",StandardBasicTypes.INTEGER ).addScalar("productName", StandardBasicTypes.STRING)
					.addScalar("description", StandardBasicTypes.STRING)
					.addScalar("testFactoryId",StandardBasicTypes.INTEGER ).addScalar("testFactoryName",StandardBasicTypes.STRING )
					.addScalar("productVersionListId",StandardBasicTypes.INTEGER ).addScalar("productVersionName",StandardBasicTypes.STRING )
					.addScalar("productBuildId",StandardBasicTypes.INTEGER ).addScalar("buildname",StandardBasicTypes.STRING )
					.addScalar("customerId",StandardBasicTypes.INTEGER ).addScalar("customerName",StandardBasicTypes.STRING )
					.addScalar("projectCode",StandardBasicTypes.STRING ).addScalar("projectName",StandardBasicTypes.STRING )
					.addScalar("productTypeId",StandardBasicTypes.INTEGER ).addScalar("typeName",StandardBasicTypes.STRING )
					.addScalar("modeId",StandardBasicTypes.INTEGER ).addScalar("modeName",StandardBasicTypes.STRING )
					.addScalar("createdDate", StandardBasicTypes.DATE)
					.setResultTransformer(Transformers.aliasToBean(ProductAWPSummaryDTO.class)).list();
			if(productAWPList.size()>0){
				productAWPSummaryDTO=productAWPList.get(0);
				int clarificCountOfProduct = clarificationTrackerDAO.getClarificationTrackerCountFromProductHierarchy(productId, true, -1, -1);
				productAWPSummaryDTO.setClarificationCount(clarificCountOfProduct);
				int changeReqCountOfProduct = changeRequestDAO.getChangeRequestCountFromProductHierarchy(productId, true, -1, -1);
				productAWPSummaryDTO.setChangeRequestCount(changeReqCountOfProduct);
				int attachmentsCountOfProduct = attachmentDAO.getAttachmentsCountFromProductHierarchy(productId, true, -1, -1);
				productAWPSummaryDTO.setAttachmentsCount(attachmentsCountOfProduct);
				String createdBy = "";
				String lastModifiedBy = "";
				Date lastModifiedDate = null;
				List<Event> eventList = eventsDAO.listEventsBySourceEntityType(IDPAConstants.ENTITY_PRODUCT, productId, -1, -1);
				if(eventList != null && !eventList.isEmpty()){
					createdBy = eventList.get(0).getUserName();
					productAWPSummaryDTO.setCreatedBy(createdBy);
					Integer eventTotalSize = eventList.size();
					if(eventTotalSize != null && eventTotalSize>0){
						lastModifiedBy = eventList.get(eventTotalSize-1).getUserName();
						lastModifiedDate = eventList.get(eventTotalSize-1).getEndTime();
						productAWPSummaryDTO.setLastModifiedBy(lastModifiedBy);
						productAWPSummaryDTO.setLastModifiedDate(lastModifiedDate);
					}
					
				}
				
			}
			
			log.info("ProductAWPSummary list :"+productAWPList.size());
		} catch (HibernateException e) {
			log.error("ProductAWPSummary failed", e);
		}	
		return productAWPSummaryDTO;
	}
	

	@Override
	@Transactional
	public List<Integer> getActivityWorkPackagesofProductIDS(int productId, List<Integer> productIdList, boolean paramIsList){
		log.debug("getActivityWorkPackagesofProductIDS");
		List<Integer> activityWPList = null;
		try {
			activityWPList = new ArrayList<Integer>();
			if(paramIsList && productIdList != null && !productIdList.isEmpty()){

				activityWPList = sessionFactory.getCurrentSession()
						.createSQLQuery("select awp.activityWorkPackageId from activity_work_package awp "
								+ "inner join product_build build on awp.productbuild = build.productBuildId "
								+ "inner join product_version_list_master pvlm on build.productVersionId = pvlm.productVersionListId "
								+ "inner join product_master pm on pvlm.productId = pm.productId "
								+ "where pm.productId in :prodIds")
						.setParameterList("prodIds", productIdList)
						.list();
			}else{
				activityWPList = sessionFactory.getCurrentSession()
						.createSQLQuery("select awp.activityWorkPackageId from activity_work_package awp "
						+ "inner join product_build build on awp.productbuild = build.productBuildId "
						+ "inner join product_version_list_master pvlm on build.productVersionId = pvlm.productVersionListId "
						+ "inner join product_master pm on pvlm.productId = pm.productId "
						+ "where pm.productId =:prodIds")
						.setParameter("prodIds", productId)
						.list();
			}
			
		} catch (Exception e) {
			log.error("getActivityWorkPackagesofProductIDS failed", e);
		}
		return activityWPList;
	}
	
	@Override
	@Transactional
	public ActivityWorkPackage getLatestActivityWPByProductId(Integer productId) {
		log.debug("getting getLatestActivityWPByProductId instance");
		ActivityWorkPackage resActivityWorkPackage=null;
		List<ActivityWorkPackage> activityWkpckgList = null;	
		try {
			try {
				activityWkpckgList = sessionFactory.getCurrentSession().createQuery("from ActivityWorkPackage awp where awp.productBuild.productVersion.productMaster.productId = "+productId+" order by awp.activityWorkPackageId desc").list();
				if(activityWkpckgList != null && !activityWkpckgList.isEmpty()){
						resActivityWorkPackage=activityWkpckgList.get(0);
				}else{
					resActivityWorkPackage=null;
				}
				log.debug("list all activity workpackages successful");
			} catch (RuntimeException re) {
				log.error("list all activity workpackages failed", re);
			}
		} catch (RuntimeException re) {
			log.error("list getActivityWorkPackagesCountByTestFactoryIdProductId", re);
		}
		return resActivityWorkPackage;
	}

	@Override
	@Transactional
	public ActivityWorkPackage getLastestActivityWorkPackageByNameInProduct(String workpackageName, Integer productId) {
		ActivityWorkPackage activityWorkPackage = null;
		List<ActivityWorkPackage> activityWorkPackages = null;	
		try {
			activityWorkPackages = sessionFactory.getCurrentSession().createQuery("from ActivityWorkPackage awp where awp.productBuild.productVersion.productMaster.productId = "+productId+" and awp.activityWorkPackageName = '"+workpackageName+"' order by awp.activityWorkPackageId desc").list();
			if(activityWorkPackages != null && !activityWorkPackages.isEmpty()){
				activityWorkPackage = activityWorkPackages.get(0);
			}
		} catch (Exception ex) {
			log.error("Error occured in getLastestActivityWorkPackageByNameInProduct - ", ex);
		}
		return activityWorkPackage;
	}
	
	@Override
	@Transactional
	public List<Integer> getWorkpackagesPrevillageForBasedLoginUser(Integer userId) {
		List<Integer> workpackageIds= new ArrayList<Integer>();
		try {
			String sql="select entityInstanceId from entity_user_group where entityTypeId=34 and userId="+userId;
			
			List<Object[]> workpackageList=sessionFactory.getCurrentSession().createSQLQuery(sql).list();
			for(Object workpackages:workpackageList) {
				Integer workpackageId=(Integer)workpackages;
				workpackageIds.add(workpackageId);
			}
		}catch(RuntimeException re) {
			log.error("Error in getWorkpackagePrevillageForLoginUser",re);
		}
		return workpackageIds;
	}

	@Override
	@Transactional
	public List<ActivityWorkPackage> getActivityWorkPackageListByProductId(Integer productId) {
		List<ActivityWorkPackage>activityWorkPackages = new ArrayList<ActivityWorkPackage>();
		try{
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ActivityWorkPackage.class, "activityWorkPackage");		
			
		      c.createAlias("activityWorkPackage.productBuild", "pbuild");
		      c.createAlias("pbuild.productVersion", "pVersion");
		      c.createAlias("pVersion.productMaster", "product");
					
					if(productId != 0){
						c.add(Restrictions.eq("product.productId", productId));
					}
					activityWorkPackages = c.list();
					
		}catch(Exception ex){
			log.error("Unable to fetch The List",ex);
		}

		return activityWorkPackages;
	}
	
	@Override
	@Transactional
	public List<ActivityWorkPackage> getActivityWorkPackagesByName(String activityWpName) {
		List<ActivityWorkPackage> activityWorkPackages = new ArrayList<ActivityWorkPackage>();
		try{
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ActivityWorkPackage.class, "activityWorkpackage");
			activityWorkPackages = c.add(Restrictions.eq("activityWorkpackage.activityWorkPackageName", activityWpName)).list();
		}catch(Exception ex){
			log.error("Unable to fetch The List",ex);
		}
		return activityWorkPackages;
	}
	
	@Override
	@Transactional
	public List<Object[]> getActivityWorkpackageByTestFactoryIdProductId(Integer testFactoryId, Integer productId) {
		log.debug("listing getActivityWorkPackagesCountByTestFactoryIdProductId instance");
		List<Object[]> activityWPList =null;		
		try {
			String sql="SELECT awp.activityWorkpackageId,awp.activityWorkpackageName FROM activity_work_package awp "
					+ "inner join product_build pb on awp.productbuild = pb.productBuildId "
					+ "inner join product_version_list_master pvlm on pb.productVersionId = pvlm.productVersionListId "
					+ "inner join product_master pm on pvlm.productId = pm.productId "
					+ "inner join test_factory tf on pm.testFactoryId = tf.testFactoryId where pm.productId="+productId+" and tf.testFactoryId="+testFactoryId+" and awp.isActive=1 order by awp.activityWorkPackageId desc";
			activityWPList = sessionFactory.getCurrentSession().createSQLQuery(sql).list();
		} catch (RuntimeException re) {
			log.error("list getActivityWorkpackageByTestFactoryIdProductId", re);
		}
		return activityWPList;
	}
	
}
