package com.hcl.atf.taf.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.dao.ActivityDAO;
import com.hcl.atf.taf.dao.ActivityWorkPackageDAO;
import com.hcl.atf.taf.dao.ChangeRequestDAO;
import com.hcl.atf.taf.model.ChangeRequest;
import com.hcl.atf.taf.model.ChangeRequestType;
import com.hcl.atf.taf.model.EntityRelationship;
import com.hcl.atf.taf.model.ProductMaster;

@Repository
public class ChangeRequestDAOImpl implements ChangeRequestDAO{
private static final Log log = LogFactory.getLog(ChangeRequestDAOImpl.class);
	
	@Autowired(required=true)
	private SessionFactory sessionFactory;
	@Autowired
	private ActivityWorkPackageDAO activityWorkPackageDAO;
	@Autowired
	private ActivityDAO activityDAO;
	
	
	@Override
	@Transactional
	public List<ChangeRequest> listChangeRequests() {
		log.debug("listing all ChangeRequest instance");
		List<ChangeRequest> changeRequestList = null;
		try {
			changeRequestList = sessionFactory.getCurrentSession().createQuery("from ChangeRequest").list();
			log.debug("list all ChangeRequest successful");
		} catch (RuntimeException re) {
			log.error("list all ChangeRequest failed", re);
		}
		return changeRequestList;
	}

	@Override
	@Transactional
	public ChangeRequest getChangeRequestById(int changeRequestId) {
			log.debug("getting changeRequestId instance by id");
			ChangeRequest changeRequest=null;
			try {			
				List list =  sessionFactory.getCurrentSession().createQuery("from ChangeRequest cr where changeRequestId=:changeRequestId").setParameter("changeRequestId", changeRequestId)
						.list();
				changeRequest=(list!=null && list.size()!=0)?(ChangeRequest)list.get(0):null;
				if(changeRequest != null){
					Hibernate.initialize(changeRequest);
					Hibernate.initialize(changeRequest.getPriority());
					Hibernate.initialize(changeRequest.getStatusCategory());
					Hibernate.initialize(changeRequest.getOwner());
	
		
				
			}
				log.debug("changeRequestId successful");
			} catch (RuntimeException re) {
				log.error("changeRequestId failed", re);
			}
			return changeRequest;        
		
	}
	

	@Override
	@Transactional
	public Integer addChangeRequest(ChangeRequest changeRequest) {
		log.info("adding changeRequest instance");
		try {	
			sessionFactory.getCurrentSession().save(changeRequest);
			log.info("add ChangeRequest successful");
		} catch (RuntimeException re) {
			log.error("add ChangeRequest failed", re);
		}
		return changeRequest.getChangeRequestId();				
	}

	@Override
	@Transactional
	public void updateChangeRequest(ChangeRequest changeRequest) {
		log.debug("updating WorkRequest instance");
		Integer changerequestId=0;
		try {
			
			sessionFactory.getCurrentSession().saveOrUpdate(changeRequest);
			log.debug("update ChangeRequest successful");
		} catch (RuntimeException re) {
			log.error("update ChangeRequest failed", re);
		}
	}

	@Override
	@Transactional
	public void deleteChangeRequest(ChangeRequest changeRequest) {
		
		log.debug("Delete ChangeRequest instance");
		try {
			sessionFactory.getCurrentSession().delete(changeRequest);
			log.debug("ChangeRequest deletion successful");
		} catch (RuntimeException re) {
			log.error("ChangeRequest deletion failed", re);
		}		
	}

	@Override
	@Transactional
	public List<ChangeRequest> listChangeRequestsByActivityId(Integer activityId, Integer initializationLevel) {
		log.debug("listing all ChangeRequest instance");
		List<ChangeRequest> changeRequestList = null;
		try {
			changeRequestList = sessionFactory.getCurrentSession().createQuery("from ChangeRequest cr where cr.activity.activityId=:activityId")
					.setParameter("activityId", activityId)
					.list();
			log.debug("list all ChangeRequest successful");
		} catch (RuntimeException re) {
			log.error("list all ChangeRequest failed", re);
		}
		return changeRequestList;
	}

	@Override
	public int addChangeRequestBulk(List<ChangeRequest> changeRequestList, int maxBatchsize) {

		log.info("Adding Change Request in bulk");
		int count = 0;
		try {
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			if (maxBatchsize <= 0)
				maxBatchsize = 50;
			for (ChangeRequest changeRequest : changeRequestList ) {
				changeRequest.setIsActive(1);
				session.save(changeRequest);
				log.info("Change Request data saved successfully");
				if (count++ % maxBatchsize == 0 ) {
					session.flush();
					session.clear();
			    }
			}
			tx.commit();
			session.close();
			log.info("Bulk Add Successful");
		} catch (RuntimeException re) {
			log.error("Bulk Add failed", re);
		}
		return count;
	}

	@Override
	@Transactional
	public List<String> getExistingChangeRequestNames(ProductMaster product) {
		List<String> changeRequestList = null;
		String hql = "SELECT cr.changeRequestName from ChangeRequest cr WHERE cr.product.productId=:productId";
		changeRequestList=sessionFactory.getCurrentSession().createQuery(hql).
				setParameter("productId",product.getProductId().intValue()).
				list();
		return changeRequestList;
	}

	@Override
	@Transactional
	public List<ChangeRequest> listChangeRequestsByProductId(Integer productId,Integer status,
			Integer initializationLevel,Integer jtStartIndex, Integer jtPageSize) {
		log.debug("listing all Change Request by product id instance");
		List<ChangeRequest> changeRequestList = null;
		Criteria c = sessionFactory.getCurrentSession().createCriteria(ChangeRequest.class, "cr");
		c.createAlias("cr.product", "product");
		if(status != null && status != 2){
			c.add(Restrictions.eq("cr.isActive", status));
		}
		if(productId !=null && productId != 0){
			c.add(Restrictions.eq("product.productId", productId));
		}
		if(jtStartIndex != null && jtPageSize != null)
			c.setFirstResult(jtStartIndex).setMaxResults(jtPageSize);
		changeRequestList = c.list();
		return changeRequestList;
	}
	
	@Override
	@Transactional
	public List<ChangeRequest> listChangeRequestByEntityTypeAndInstanceIds(Integer entityType1, Integer entityInstance1) {
		log.debug(" listChangeRequestByEntityTypeAndInstanceIds");
		List<ChangeRequest> changeRequestList=new ArrayList<ChangeRequest>();		
		try {			 
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ChangeRequest.class,"changeRequest");	
			c.add(Restrictions.eq("changeRequest.entityType.entitymasterid", entityType1));
			c.add(Restrictions.eq("changeRequest.entityInstanceId", entityInstance1));
			changeRequestList = c.list();
			for (ChangeRequest changeRequest : changeRequestList) {
				Hibernate.initialize(changeRequest.getPriority());
				Hibernate.initialize(changeRequest.getStatusCategory());
				Hibernate.initialize(changeRequest.getOwner());
				Hibernate.initialize(changeRequest.getProduct());
				Hibernate.initialize(changeRequest.getProduct().getProductId());
				Hibernate.initialize(changeRequest.getProduct().getProductName());
				
				
			}
			log.debug("listChangeRequestByEntityTypeAndInstanceIds successful");			
			} catch (RuntimeException re) {
				log.error("list specific failed", re);
			}
		return changeRequestList;
	}
	

	@Override
	@Transactional
	public List<ChangeRequest> listChangeRequestByActivityWorkPackageId(Integer entityType1, Integer entityInstance1, Integer jtStartIndex, Integer jtPageSize, Integer initializationLevel) {
		log.debug("listing AWP Level ClarificationTrackers");
		List<ChangeRequest> changeRequestList = new ArrayList<ChangeRequest>();
		List<ChangeRequest> changeRequestAWPList = new ArrayList<ChangeRequest>();
		List<ChangeRequest> changeRequestActivityList = new ArrayList<ChangeRequest>();
		
		try {
		
		Criteria c = sessionFactory.getCurrentSession().createCriteria(ChangeRequest.class, "cr");		
		c.createAlias("cr.entityType", "entityType");
		c.add(Restrictions.eq("entityType.entitymasterid", entityType1));
		c.add(Restrictions.eq("cr.entityInstanceId", entityInstance1));
		if(jtStartIndex!=-1 && jtPageSize!=-1){
			changeRequestAWPList = c.setFirstResult(jtStartIndex).setMaxResults(jtPageSize).list();
			changeRequestList.addAll(changeRequestAWPList);
		}else{
			changeRequestAWPList = c.list();	
			changeRequestList.addAll(changeRequestAWPList);
		}

		List<Integer> activityIdList = new ArrayList<Integer>();		
		activityIdList = sessionFactory.getCurrentSession().createSQLQuery("select act.activityId from activity act where activityWorkPackageId=:actWPId")
							.setParameter("actWPId", entityInstance1)	
							.list();
		if(activityIdList != null && !activityIdList.isEmpty()){
			c = null;//Reset Criteria Object
			c = sessionFactory.getCurrentSession().createCriteria(ChangeRequest.class, "cr");
			
			c.createAlias("cr.entityType", "entityType");
			c.add(Restrictions.eq("entityType.entitymasterid", IDPAConstants.ACTIVITY_ENTITY_MASTER_ID));
			c.add(Restrictions.in("cr.entityInstanceId",  Arrays.asList(activityIdList.toArray())));
			if(jtStartIndex!=-1 && jtPageSize!=-1){
				changeRequestActivityList = c.setFirstResult(jtStartIndex).setMaxResults(jtPageSize).list();
				changeRequestList.addAll(changeRequestActivityList);
			}else{
				changeRequestActivityList = c.list();	
				changeRequestList.addAll(changeRequestActivityList);
			}
		}		
		if(initializationLevel != -1){
			for (ChangeRequest changeRequest : changeRequestList) {
				Hibernate.initialize(changeRequest.getPriority());
				Hibernate.initialize(changeRequest.getStatusCategory());
				Hibernate.initialize(changeRequest.getOwner());
				Hibernate.initialize(changeRequest.getProduct());
				Hibernate.initialize(changeRequest.getProduct().getProductId());
				Hibernate.initialize(changeRequest.getProduct().getProductName());
			}
		}
		log.info("ChangeRequest list :"+changeRequestList.size());			
		return changeRequestList;					
		} catch (RuntimeException re) {
			log.error("list AWP Level ChangeRequest failed", re);
		}
		return changeRequestList;
	}
	


	@Override
	@Transactional
	public List<ChangeRequest> listChangeRequestByProductId(Integer entityType1, Integer entityInstance1, Integer jtStartIndex, Integer jtPageSize, Integer initializationLevel) {
		log.debug("listing Product Level ClarificationTrackers");
		List<ChangeRequest> changeRequestProdList = new ArrayList<ChangeRequest>();
		List<ChangeRequest> changeRequestAWPList = new ArrayList<ChangeRequest>();
		List<ChangeRequest> changeRequestActivityList = new ArrayList<ChangeRequest>();
		List<ChangeRequest> changeRequestList = new ArrayList<ChangeRequest>();
		try 
		{
		List<Integer> awpIdList = new ArrayList<Integer>();
		List<Integer> activityIdList = new ArrayList<Integer>();
		
		Criteria c = sessionFactory.getCurrentSession().createCriteria(ChangeRequest.class, "cr");		
		c.createAlias("cr.entityType", "entityType");
		c.add(Restrictions.eq("entityType.entitymasterid", entityType1));
		c.add(Restrictions.eq("cr.entityInstanceId", entityInstance1));
		
			changeRequestProdList = c.list();	
			changeRequestList.addAll(changeRequestProdList);
				awpIdList = sessionFactory.getCurrentSession().createSQLQuery("select awp.activityWorkPackageId from activity_work_package awp "
				+ "inner join product_build pb on awp.productbuild =  pb.productBuildId "
				+ "inner join product_version_list_master pvlm on pb.productVersionId = pvlm.productVersionListId "
				+ "inner join product_master prod on pvlm.productId = prod.productId "
				+ "and prod.productId=:prodId")
				.setParameter("prodId", entityInstance1)
				.list();
		if(awpIdList != null && !awpIdList.isEmpty()){

			c = null;//Reset the Criteria Object		
			c = sessionFactory.getCurrentSession().createCriteria(ChangeRequest.class, "cr");		
			c.createAlias("cr.entityType", "entityType");
			c.add(Restrictions.eq("entityType.entitymasterid", IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE_ID));
			c.add(Restrictions.in("cr.entityInstanceId",  Arrays.asList(awpIdList.toArray())));
				changeRequestAWPList = c.list();	
				changeRequestList.addAll(changeRequestAWPList);
			activityIdList = sessionFactory.getCurrentSession().createSQLQuery("select act.activityId from activity act where activityWorkPackageId in :awpIds")
					.setParameterList("awpIds", awpIdList)
					.list();
			if(activityIdList != null && !activityIdList.isEmpty()){
				c = null;//Reset Criteria Object
				c = sessionFactory.getCurrentSession().createCriteria(ChangeRequest.class, "cr");		
				c.createAlias("cr.entityType", "entityType");
				c.add(Restrictions.eq("entityType.entitymasterid", IDPAConstants.ACTIVITY_ENTITY_MASTER_ID));		
				c.add(Restrictions.in("cr.entityInstanceId",  Arrays.asList(activityIdList.toArray())));
					changeRequestActivityList = c.list();	
					changeRequestList.addAll(changeRequestActivityList);
			}	
		}	
		if(initializationLevel != -1){
			for (ChangeRequest changeRequest : changeRequestList) {
				Hibernate.initialize(changeRequest.getPriority());
				Hibernate.initialize(changeRequest.getStatusCategory());
				Hibernate.initialize(changeRequest.getOwner());
				Hibernate.initialize(changeRequest.getProduct());
				Hibernate.initialize(changeRequest.getProduct().getProductId());
				Hibernate.initialize(changeRequest.getProduct().getProductName());
			}
		}
		log.info("ChangeRequest list :"+changeRequestList.size());			
		return changeRequestList;					
		} catch (RuntimeException re) {
			log.error("list AWP Level ChangeRequest failed", re);
		}
		return changeRequestList;
	}
	
	@Override
    @Transactional
    public void updateChangeRequesttoActivity(ChangeRequest changeRequest) {
           log.debug("updating  ChangeRequest instance");
           try {
                  
        	   ChangeRequest existingChangeRequestList = null;  
                  List list = sessionFactory.getCurrentSession().createQuery("from ChangeRequest cr where cr.changeRequestId=:changeRequestId").setParameter("changeRequestId",changeRequest.getChangeRequestId())
                               .list();
                  existingChangeRequestList = (list!=null && list.size()!=0)?(ChangeRequest) list.get(0):null;
  						
  						if(changeRequest.getOwner()!=null){
                	  		existingChangeRequestList.setOwner(changeRequest.getOwner());
               	  	 	}
                	  	if(changeRequest.getPriority()!=null){
                	  		existingChangeRequestList.setPriority(changeRequest.getPriority());
                	  	}
                	  	if(changeRequest.getProduct()!=null){
                	  		existingChangeRequestList.setProduct(changeRequest.getProduct());
                	  	}
                		if(changeRequest.getStatusCategory()!=null){
                	  		existingChangeRequestList.setStatusCategory(changeRequest.getStatusCategory());
                	  	}  
                		if(changeRequest.getChangeRequestName() != null){
                			existingChangeRequestList.setChangeRequestName(changeRequest.getChangeRequestName());
                		}
                		if(changeRequest.getDescription() != null){
                			existingChangeRequestList.setDescription(changeRequest.getDescription());
                		}
                		if(changeRequest.getRaisedDate() != null){
                			existingChangeRequestList.setRaisedDate(changeRequest.getRaisedDate());
                		}
                		if(changeRequest.getPlanExpectedValue() != null){
                			existingChangeRequestList.setPlanExpectedValue(changeRequest.getPlanExpectedValue());
                		}
                         sessionFactory.getCurrentSession().saveOrUpdate(existingChangeRequestList);
                         log.debug("update successful");
                 
           } catch (RuntimeException re) {
                  log.error("update failed", re);
                  //throw re;
           }
	}
	
	@Override
    @Transactional
	public ChangeRequest getChangeRequestByName(String changeRequestName) {
		ChangeRequest changeRequest = null;
		try{
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ChangeRequest.class, "changeRequest");
			List list = c.add(Restrictions.eq("changeRequest.changeRequestName", changeRequestName)).list();
			changeRequest = (list != null && list.size() != 0)?(ChangeRequest)list.get(0):null;
		}catch(RuntimeException re){
			log.error("getting change Request by name failed", re);
		}
		return changeRequest;
	}
	
	@Override
    @Transactional
	public List<ChangeRequest> listChangeRequestsByIds(List<Integer> changeRequestIds){
		List<ChangeRequest> changeRequestList = null;
		try{
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ChangeRequest.class, "changeRequest");
			changeRequestList = c.add(Restrictions.in("changeRequest.changeRequestId", changeRequestIds.toArray())).list();
			
		}catch(RuntimeException re){
			log.error("getting Change Request by Ids failed", re);
		}
		return changeRequestList;
	}
	@Override
	@Transactional
	public List<ChangeRequestType> getChangeRequestType() {
		
		List<ChangeRequestType> changeRequestTypeList = new ArrayList<ChangeRequestType>();
		try{			
			String hql = "from ChangeRequestType";
			changeRequestTypeList = sessionFactory.getCurrentSession().createQuery(hql).list();
		}catch(Exception e){			
			log.error("ERROR  ",e);
		}		
		return changeRequestTypeList;
	}
	
	@Override
	@Transactional
	public List<Object[]> getRcnByProductId(int productId, int jtStartIndex, int jtPageSize) {			
		List<Object[]> unMappedRcnListObj = null;
	try {
        String sql="select distinct changeRequestId, changeRequestName from change_request cr where cr.productId=:productId AND cr.isActive=1";
		unMappedRcnListObj=sessionFactory.getCurrentSession().createSQLQuery(sql).
			setParameter("productId", productId).list();
    } catch (RuntimeException re) {
		log.error("Get EnvCombinationList Unmapped to ActivityId", re);	
	}
	return unMappedRcnListObj;
	}
	
	@Override
	@Transactional
	public Integer countAllChangeRequest(Date startDate, Date endDate) {

		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ChangeRequest.class,"changeRequest");
			if (startDate != null) {
				c.add(Restrictions.ge("changeRequest.raisedDate", startDate));
			}
			if (endDate != null) {
				c.add(Restrictions.le("changeRequest.modifiedDate", endDate));
			}
			c.setProjection(Projections.rowCount());
			Integer count = Integer.parseInt(c.uniqueResult().toString());
			return count;
		} catch (Exception e) {
			log.error("Unable to get count of all changeRequest", e);
			return -1;
		}
	}
	
	@Override
	@Transactional
	public List<ChangeRequest> listAllChangeRequestByLastSyncDate(int startIndex, int pageSize, Date startDate, Date endDate) {
		log.debug("listing all ChangeRequest instance");
		List<ChangeRequest> changeRequestList = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ChangeRequest.class, "changeRequest");
			if (startDate != null) {
				c.add(Restrictions.ge("changeRequest.raisedDate", startDate));
			}
			if (endDate != null) {
				c.add(Restrictions.le("changeRequest.modifiedDate", endDate));
			}
			c.addOrder(Order.asc("changeRequestId"));
            c.setFirstResult(startIndex);
            c.setMaxResults(pageSize);
            changeRequestList = c.list();	
			log.debug("list all changeRequest successful");
		} catch (RuntimeException re) {
			log.error("list all changeRequest failed", re);
			// throw re;
		}
		return changeRequestList;
	}
	
	@Override
	@Transactional
	public Integer getcountOfChangeRequestsByEntityTypeAndInstanceIds(Integer entityType1, Integer entityInstance1) {
		log.info("Inside getcountOfChangeRequestsByEntityTypeAndInstanceIds");
		Integer changeRequestCount = 0;
		try{
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ChangeRequest.class, "changeRequest");
			
			if(entityType1 != 0 && entityInstance1 != 0){
			c.add(Restrictions.eq("changeRequest.entityType.entitymasterid", entityType1));						
			c.add(Restrictions.eq("changeRequest.entityInstanceId", entityInstance1));
			c.setProjection(Projections.rowCount());
			String count = ""+c.uniqueResult();			
			changeRequestCount = Integer.parseInt(count);
			}
		}catch(RuntimeException re){
			log.error("getcountOfChangeRequestsByEntityTypeAndInstanceIds failed", re);
		}
			return changeRequestCount;
	}
	

	@Override
	@Transactional
	public Integer getChangeRequestCountFromProductHierarchy(Integer productId, Boolean isHierarchy, 
			Integer jtStartIndex, Integer jtPageSize){
		int changeReqCount = 0;
		List<Integer> awpIdList = null;
		List<Integer> actIdList = null;
		String sql = null;
		Boolean paramIsList = false;
		try {
			if(isHierarchy){				
				awpIdList = activityWorkPackageDAO.getActivityWorkPackagesofProductIDS(productId, null, paramIsList);
				paramIsList = true;
				actIdList = activityDAO.getActivitiesofAWPIDS(null, awpIdList, paramIsList);
				if(awpIdList != null && !awpIdList.isEmpty()){
					if(actIdList != null && !actIdList.isEmpty()){
						sql = "select count(*) from change_request as cr "
								+ "where (cr.entityTypeId=18 and cr.entityInstanceId=:prodId) "
								+ "or (cr.entityTypeId=34 and cr.entityInstanceId in (:awpIds)) "
								+ "or (cr.entityTypeId=28 and cr.entityInstanceId in (:actIds)) ";

						if(jtStartIndex != -1 && jtPageSize != -1 ){
							sql = sql + "limit ";								
							sql = sql+jtStartIndex+" ,"+jtPageSize+"";	
						}
						changeReqCount = ((Number)sessionFactory.getCurrentSession().createSQLQuery(sql)
								.setParameter("prodId", productId)
								.setParameterList("awpIds", awpIdList)
								.setParameterList("actIds", actIdList)
								.uniqueResult()).intValue();
					}else{
						sql = "select count(*) from change_request as cr "
								+ "where (cr.entityTypeId=18 and cr.entityInstanceId=:prodId) "
								+ "or (cr.entityTypeId=34 and cr.entityInstanceId in (:awpIds)) ";
						if(jtStartIndex != -1 && jtPageSize != -1 ){
							sql = sql + "limit ";								
							sql = sql+jtStartIndex+" ,"+jtPageSize+"";	
						}
						changeReqCount = ((Number)sessionFactory.getCurrentSession().createSQLQuery(sql)
								.setParameter("prodId", productId)
								.setParameterList("awpIds", awpIdList)
								.uniqueResult()).intValue();
					}
				}				
			}else{
				sql ="select count(*) from change_request as cr "
						+ "where cr.entityTypeId=18 and cr.entityInstanceId=:prodId ";
				if(jtStartIndex != -1 && jtPageSize != -1 ){
					sql = sql + "limit ";								
					sql = sql+jtStartIndex+" ,"+jtPageSize+"";	
				}
				changeReqCount = ((Number)sessionFactory.getCurrentSession().createSQLQuery(sql)
						.setParameter("prodId", productId).uniqueResult()).intValue();
			}
		} catch (Exception e) {
			log.error("getChangeRequestCountFromProductHierarchy failed", e);
		}
		return changeReqCount;
	}

	@Override
	@Transactional
	public List<ChangeRequest> listCRByEntityTypeAndInstanceIds(
			Integer entityType1, Integer entityType2, Integer entityInstance1) {
		List<Object[]> objArrList = null;
		List<ChangeRequest> listOfMappedCRs = new ArrayList<ChangeRequest>();
		try {
			DetachedCriteria dc = DetachedCriteria.forClass(EntityRelationship.class);
			dc.add(Restrictions.eq("entityTypeId1", entityType1)).add(Restrictions.eq("entityTypeId2", entityType2)).
			add(Restrictions.eq("entityInstanceId1", entityInstance1)).setProjection(Projections.property("entityInstanceId2"));
			
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ChangeRequest.class, "cr");
			listOfMappedCRs = criteria.add(Subqueries.propertyIn("changeRequestId", dc)).list();
		} catch (Exception e) {
			log.error("listCRByEntityTypeAndInstanceIds failed", e);
		}
		return listOfMappedCRs;
	}
}