package com.hcl.atf.taf.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.dao.ActivityDAO;
import com.hcl.atf.taf.dao.ActivityWorkPackageDAO;
import com.hcl.atf.taf.dao.ClarificationTrackerDAO;
import com.hcl.atf.taf.model.ClarificationScope;
import com.hcl.atf.taf.model.ClarificationTracker;
import com.hcl.atf.taf.model.ClarificationTypeMaster;
import com.hcl.atf.taf.model.EntityStatus;
import com.hcl.atf.taf.model.TransactionClarification;
import com.hcl.atf.taf.model.dto.ClarificationTrackerDTO;
@Repository
public class ClarificationTrackerDAOImpl implements ClarificationTrackerDAO{
private static final Log log = LogFactory.getLog(ClarificationTrackerDAOImpl.class);
@Value("#{ilcmProps['database.url']}")
private String DBUrl;
	
	@Autowired(required=true)
	private SessionFactory sessionFactory;
	@Autowired
	private ActivityWorkPackageDAO activityWorkPackageDAO;
	@Autowired
	private ActivityDAO activityDAO;
	
	@Override
	@Transactional
	public ClarificationTracker getlistClarificationTrackersById(int clarificationTrackerId, Integer initializelevel) {
		log.debug("Inside getlistClarificationTrackersById");
		ClarificationTracker clarificationTracker = null;
		try {
			List list = sessionFactory.getCurrentSession().createQuery("from ClarificationTracker c where clarificationTrackerId=:clarificationTrackerId").setParameter("clarificationTrackerId", clarificationTrackerId).list();
			clarificationTracker = (list != null && list.size() != 0) ? (ClarificationTracker)list.get(0) : null;
			log.debug("getlistClarificationTrackersById successful");
		} catch (RuntimeException re) {
			log.error("getlistClarificationTrackersById failed", re);
		}
		return clarificationTracker;
	}

	
	@Override
	@Transactional
	public Integer addClarificationTracker(
			ClarificationTracker clarificationTracker) {
		log.info("adding ClarificationTracker instance");
		try {	
			sessionFactory.getCurrentSession().save(clarificationTracker);
			log.info("add ClarificationTracker successful");
		} catch (RuntimeException re) {
			log.error("add ClarificationTracker failed", re);
			
		}
		return clarificationTracker.getClarificationTrackerId();
	}

	@Override
	@Transactional
	public void updateClarificationTracker(
			ClarificationTracker clarificationTracker) {
		log.debug("updating ClarificationTracker instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(clarificationTracker);
			log.debug("update ClarificationTracker successful");
		} catch (RuntimeException re) {
			log.error("update ClarificationTracker failed", re);
			
		}
		
	}

	@Override
	@Transactional
	public void deleteClarificationTracker(
			ClarificationTracker clarificationTracker) {
		log.debug("Delete ClarificationTracker instance");
		try {
			sessionFactory.getCurrentSession().delete(clarificationTracker);
			log.debug("ClarificationTracker deletion successful");
		} catch (RuntimeException re) {
			log.error("ClarificationTracker deletion failed", re);
		}	
		
	}
	@Override
	@Transactional
	public List<ClarificationTracker> listClarificationTrackersByActivityId(Integer entityTypeId, Integer entityInstanceId, Integer initializationLevel) {
		log.debug("listing all ClarificationTracker instance");
		List<ClarificationTracker> clarificationTrackerList = null;
		try {
			
			clarificationTrackerList = sessionFactory.getCurrentSession().createQuery("from ClarificationTracker ct where ct.entityInstanceId=:entityInstanceId and ct.entityType.entitymasterid =:entitymasterid ")
					.setParameter("entityInstanceId", entityInstanceId)
					.setParameter("entitymasterid", entityTypeId)
					.list();
			log.debug("list all ClarificationTracker successful");
		} catch (RuntimeException re) {
			log.error("list all ClarificationTracker failed", re);
		}
		return clarificationTrackerList;
	}


	@Override
	@Transactional
	public List<ClarificationTracker> listClarificationsByProductId(Integer entityTypeId, Integer entityInstanceId, Integer jtStartIndex, Integer jtPageSize, Integer initializationLevel) {
		log.debug("listing Product Level ClarificationTrackers");
		List<ClarificationTracker> clarificationTrackerProdList = new ArrayList<ClarificationTracker>();
		List<ClarificationTracker> clarificationTrackerAWPList = new ArrayList<ClarificationTracker>();
		List<ClarificationTracker> clarificationTrackerACTList = new ArrayList<ClarificationTracker>();
		List<ClarificationTracker> clarificationTrackerList = new ArrayList<ClarificationTracker>();
		try {
		List<Integer> awpIdList = new ArrayList<Integer>();
		List<Integer> activityIdList = new ArrayList<Integer>();

		Criteria c = sessionFactory.getCurrentSession().createCriteria(ClarificationTracker.class, "clarif");		
		c.createAlias("clarif.entityType", "entityType");
		c.add(Restrictions.eq("entityType.entitymasterid", entityTypeId));		
		c.add(Restrictions.eq("clarif.entityInstanceId", entityInstanceId));	
		if(jtStartIndex!=-1 && jtPageSize!=-1){
			clarificationTrackerProdList = c.setFirstResult(jtStartIndex).setMaxResults(jtPageSize).list();
			clarificationTrackerList.addAll(clarificationTrackerProdList);
		}else{
			clarificationTrackerProdList = c.list();	
			clarificationTrackerList.addAll(clarificationTrackerProdList);
		}
		
		awpIdList = sessionFactory.getCurrentSession().createSQLQuery("select awp.activityWorkPackageId from activity_work_package awp "
				+ "inner join product_build pb on awp.productbuild =  pb.productBuildId "
				+ "inner join product_version_list_master pvlm on pb.productVersionId = pvlm.productVersionListId "
				+ "inner join product_master prod on pvlm.productId = prod.productId "
				+ "and prod.productId=:prodId")
				.setParameter("prodId", entityInstanceId)
				.list();
		
		if(awpIdList != null && !awpIdList.isEmpty()){
			c = null;//Reset the Criteria Object
			c = sessionFactory.getCurrentSession().createCriteria(ClarificationTracker.class, "clarif");		
			c.createAlias("clarif.entityType", "entityType");
			c.add(Restrictions.eq("entityType.entitymasterid", IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE_ID));
			c.add(Restrictions.in("clarif.entityInstanceId",  Arrays.asList(awpIdList.toArray())));
			if(jtStartIndex!=-1 && jtPageSize!=-1){
				clarificationTrackerAWPList = c.setFirstResult(jtStartIndex).setMaxResults(jtPageSize).list();
				clarificationTrackerList.addAll(clarificationTrackerAWPList);
			}else{
				clarificationTrackerAWPList = c.list();	
				clarificationTrackerList.addAll(clarificationTrackerAWPList);
			}
			
			activityIdList = sessionFactory.getCurrentSession().createSQLQuery("select act.activityId from activity act where activityWorkPackageId in :awpIds")
					.setParameterList("awpIds", awpIdList)
					.list();
			if(activityIdList != null && !activityIdList.isEmpty()){

				c = null;//Reset the Criteria Object
				c = sessionFactory.getCurrentSession().createCriteria(ClarificationTracker.class, "clarif");
				
				c.createAlias("clarif.entityType", "entityType");
				c.add(Restrictions.eq("entityType.entitymasterid", IDPAConstants.ACTIVITY_ENTITY_MASTER_ID));		
				c.add(Restrictions.in("clarif.entityInstanceId",  Arrays.asList(activityIdList.toArray())));
				if(jtStartIndex!=-1 && jtPageSize!=-1){
					clarificationTrackerACTList = c.setFirstResult(jtStartIndex).setMaxResults(jtPageSize).list();
					clarificationTrackerList.addAll(clarificationTrackerACTList);
				}else{
					clarificationTrackerACTList = c.list();	
					clarificationTrackerList.addAll(clarificationTrackerACTList);
				}
			}
			
		}		
		log.info("ClarificationTracker list :"+clarificationTrackerList.size());			
		return clarificationTrackerList;			
		} catch (RuntimeException re) {
			log.error("list AWP Level ClarificationTrackers failed", re);
		}
		return clarificationTrackerList;
	}

	@Override
	@Transactional
	public List<ClarificationTracker> listClarificationsByActivityWorkPackageId(Integer entityTypeId, Integer entityInstanceId, Integer jtStartIndex, Integer jtPageSize, Integer initializationLevel) {
		log.debug("listing AWP Level ClarificationTrackers");
		List<ClarificationTracker> clarificationTrackerAWPList = new ArrayList<ClarificationTracker>();
		List<ClarificationTracker> clarificationTrackerACTList = new ArrayList<ClarificationTracker>();
		List<ClarificationTracker> clarificationTrackerList = new ArrayList<ClarificationTracker>();
		ClarificationTrackerDTO clarificationTrackerDTO=new ClarificationTrackerDTO();
		try {
		List<Integer> activityIdList = new ArrayList<Integer>();		
		
		//AWP Level Clarification List
		Criteria c = sessionFactory.getCurrentSession().createCriteria(ClarificationTracker.class, "clarif");		
		c.createAlias("clarif.entityType", "entityType");
		c.add(Restrictions.eq("entityType.entitymasterid", entityTypeId));		
		c.add(Restrictions.eq("clarif.entityInstanceId", entityInstanceId));		
		if(jtStartIndex!=-1 && jtPageSize!=-1){
			clarificationTrackerAWPList = c.setFirstResult(jtStartIndex).setMaxResults(jtPageSize).list();
			clarificationTrackerList.addAll(clarificationTrackerAWPList);
		}else{
			clarificationTrackerAWPList = c.list();	
			clarificationTrackerList.addAll(clarificationTrackerAWPList);
		}
		
		activityIdList = sessionFactory.getCurrentSession().createSQLQuery("select act.activityId from activity act where activityWorkPackageId=:actWPId")
				.setParameter("actWPId", entityInstanceId)	
				.list();
		if(activityIdList != null && !activityIdList.isEmpty()){
			c = null; //Reset the Criteria Object
			c = sessionFactory.getCurrentSession().createCriteria(ClarificationTracker.class, "clarif");		
			c.createAlias("clarif.entityType", "entityType");
			c.add(Restrictions.eq("entityType.entitymasterid", IDPAConstants.ACTIVITY_ENTITY_MASTER_ID));		
			c.add(Restrictions.in("clarif.entityInstanceId",  Arrays.asList(activityIdList.toArray())));
			if(jtStartIndex!=-1 && jtPageSize!=-1){
				clarificationTrackerACTList = c.setFirstResult(jtStartIndex).setMaxResults(jtPageSize).list();
				clarificationTrackerList.addAll(clarificationTrackerACTList);
			}else{
				clarificationTrackerACTList = c.list();	
				clarificationTrackerList.addAll(clarificationTrackerACTList);
			}
		}
				
		log.info("ClarificationTracker list :"+clarificationTrackerList.size());			
		return clarificationTrackerList;	
		} catch (RuntimeException re) {
			log.error("list AWP Level ClarificationTrackers failed", re);
		}
		return clarificationTrackerList;
	}
    @Override
	@Transactional
	public List<ClarificationTracker> listJsonClarificationTrackers() {
		log.info("listing all larificationTracker instance");
		List<ClarificationTracker> clarificationTrackerList = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ClarificationTracker.class, "clarificationTracker");		
			
			clarificationTrackerList = c.list();		
			log.debug("list all ActivityWorkPackage successful");
		} catch (RuntimeException re) {
			log.error("list all ActivityWorkPackage failed", re);
		}
		return clarificationTrackerList;
	}


	@Override
	@Transactional
	public List<ClarificationTracker> listClarificationTrackersByProductId(Integer productId) {
		List<ClarificationTracker> clarificationTrackerlist = new ArrayList<ClarificationTracker>();
			
			try{
				Criteria c = sessionFactory.getCurrentSession().createCriteria(ClarificationTracker.class, "clarificationTracker");		
				c.createAlias("clarificationTracker.product", "product");
				c.add(Restrictions.eq("product.productId", productId));
				
				clarificationTrackerlist = c.list();		
				log.debug("list all ActivityClarificationTracker list successful");
			}catch(Exception e){
				log.error("ERROR listing ClarificationTrackers By ProductId ",e);
			}
			return clarificationTrackerlist;
			
	}


	@Override
	@Transactional
	public List<ClarificationTypeMaster> listClarificationType() {
		List<ClarificationTypeMaster> clarificationTypeMasterList = new ArrayList<ClarificationTypeMaster>();
		try{
	
		Criteria c = sessionFactory.getCurrentSession().createCriteria(ClarificationTypeMaster.class, "clarificationTypeMaster");
		clarificationTypeMasterList =	c.list();
		log.debug("list all ClarificationTracker list successful");
		
		}catch(Exception e){
			log.error("ERROR listing ClarificationTypeMaster",e);
		}
		return clarificationTypeMasterList;
	}


	@Override
	@Transactional
//	@SuppressWarnings("unchecked")
	public List<EntityStatus> listClarificationStatus(Integer entityTypeId) {
		List<EntityStatus> workFlowStatus = new ArrayList<EntityStatus>();
		try{
			Criteria c = sessionFactory.getCurrentSession().createCriteria(EntityStatus.class, "wokrFlowStatus");
			c.add(Restrictions.eq("wokrFlowStatus.entityMaster.entitymasterid", entityTypeId));
			c.add(Restrictions.eq("activeStatus", 1));			
			c.add(Restrictions.isNull("parentStatusId"));
			workFlowStatus = c.list();	
			log.debug("list all Clarification Status list successful");
			
		}catch(Exception e){
			log.error("ERROR listing ClarificationStatus ",e);			
		}		
		return workFlowStatus;
	}


	@Override
	@Transactional
	public List<EntityStatus> listClarificationResolutionStatus(Integer parentStatusId) {
		List<EntityStatus> workFlowStatus = new ArrayList<EntityStatus>();
		try{
			
			Criteria c = sessionFactory.getCurrentSession().createCriteria(EntityStatus.class,"workFlowStatus");
			c.add(Restrictions.eq("workFlowStatus.parentStatusId",parentStatusId));
			workFlowStatus =	c.list();			
			log.debug("list all Clarification Resolutions status  successful");
			
		}catch(Exception e){			
			log.error("ERROR listing Clarification Resolutions status ",e);
		}		
		return workFlowStatus;
	}


	@Override
	@Transactional
	public Integer countAllClarification(Date startDate, Date endDate) {

		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ClarificationTracker.class,"clarification");
			if (startDate != null) {
				c.add(Restrictions.ge("clarification.createdDate", startDate));
			}
			if (endDate != null) {
				c.add(Restrictions.le("clarification.createdDate", endDate));
			}
			c.setProjection(Projections.rowCount());
			Integer count = Integer.parseInt(c.uniqueResult().toString());
			return count;
		} catch (Exception e) {
			log.error("Unable to get count of all clarification", e);
			return -1;
		}
	}


	@Override
	@Transactional
	public List<ClarificationTracker> listAllClarificationByLastSyncDate(int startIndex, int pageSize, Date startDate, Date endDate) {
		log.debug("listing all Clarification instance");
		List<ClarificationTracker> clarificationList = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ClarificationTracker.class, "clarification");
			if (startDate != null) {
				c.add(Restrictions.ge("clarification.createDate", startDate));
			}
			if (endDate != null) {
				c.add(Restrictions.le("clarification.createDate", endDate));
			}
			c.addOrder(Order.asc("clarificationTrackerId"));
            c.setFirstResult(startIndex);
            c.setMaxResults(pageSize);
            clarificationList = c.list();	
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
			// throw re;
		}
		return clarificationList;
	}


	@Override
	@Transactional
	public List<ClarificationScope> getClarificationScope() {
		List<ClarificationScope> listClarificationScope = new ArrayList<ClarificationScope>();
		try{
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ClarificationScope.class,"clarificationScope");			
			listClarificationScope = c.list();		
			log.debug("list all Clarification Scope   successful");
		}catch(Exception e){
			log.error("ERROR listing Clarification Scope ",e);
		}
		return listClarificationScope;	
	}

	@Override
	@Transactional
	public List<ClarificationTracker> listClarificationsByEngagementAndProductIds(int testFactoryId, int productId){
		log.debug("Inside listClarificationsByEngagementAndProductIds");
		ClarificationTracker clarificationTracker = null;
		List<ClarificationTracker> clarificationTrackerList = null;
		try {
			String query = "";
			if(testFactoryId != 0){
				query = "from ClarificationTracker ct where ct.testFactory.testFactoryId="+testFactoryId;
			}
			if(productId != 0){
				query = "from ClarificationTracker ct where ct.product.productId="+productId;
			}
			clarificationTrackerList = sessionFactory.getCurrentSession().createQuery(query).list();
			log.debug("getlistClarificationTrackersById successful");
		} catch (RuntimeException re) {
			log.error("getlistClarificationTrackersById failed", re);
		}
		return clarificationTrackerList;
	}
	@Override
	@Transactional
	public void addTransactionClarification(TransactionClarification transactionClarification) {
		log.info("add TransactionClarification instance");
		try{
			sessionFactory.getCurrentSession().save(transactionClarification);
		}catch(Exception e){
			log.error(e);			
		}		
	}


	@Override
	@Transactional
	public List<TransactionClarification> listTransactionClarification(Integer clarificationTrackerId) {
		List<TransactionClarification> transactionClarificationList=null;
		try{
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TransactionClarification.class, "transactionClarification");
			if(clarificationTrackerId != null){
				c.add(Restrictions.eq("transactionClarification.clarificationTrackerId.clarificationTrackerId", clarificationTrackerId));
			}
			transactionClarificationList = c.list();
		
		log.debug("list all successful TransactionClarification");
		
		}
		catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return transactionClarificationList;
		
	}
	@Override
	@Transactional
	public void updateTransactionClarification(TransactionClarification transactionClarificationUI) {
		log.info("Update TransactionClarification instance");
		try{
			sessionFactory.getCurrentSession().update(transactionClarificationUI);
		}catch(Exception e){
			log.error(e);			
		}		
	}


	@Override
	@Transactional
	public String deleteClarificationTracker(Integer clarificationTrackerId){	
		String message = null;		
		try{						
					
			Query query =	sessionFactory.getCurrentSession().createQuery("DELETE FROM ClarificationTracker WHERE clarificationTrackerId=:clarificationTrackerId");
			query.setInteger("clarificationTrackerId", clarificationTrackerId);
			query.executeUpdate();
			message = "OK";			
			log.info("deletion of clarificationTracker successful");
				
			}catch(RuntimeException re){
				log.error("deletion of clarificationTracker failed", re);
			}
			return message;
		
	}
	
	@Override
	@Transactional
	public Integer getcountOfClarificationByEntityTypeAndInstanceIds(Integer entityTypeId, Integer entityInstanceId) {
		log.info("Inside getcountOfClarificationByEntityTypeAndInstanceIds");
		Integer clarificationCount = 0;
		try{
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ClarificationTracker.class, "clarificationTracker");
			
			if(entityTypeId != 0 && entityInstanceId != 0){
			c.add(Restrictions.eq("clarificationTracker.entityType.entitymasterid", entityTypeId));						
			c.add(Restrictions.eq("clarificationTracker.entityInstanceId", entityInstanceId));
			c.setProjection(Projections.rowCount());
			String count = ""+c.uniqueResult();		
			clarificationCount = Integer.parseInt(count);
			}
		}catch(RuntimeException re){
			log.error("getcountOfClarificationByEntityTypeAndInstanceIds failed", re);
		}
			return clarificationCount;
	}
	
	@Override
	@Transactional
	public Integer getClarificationTrackerCountFromProductHierarchy(Integer productId, Boolean isHierarchy, 
			Integer jtStartIndex, Integer jtPageSize){
		int clartCount = 0;
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
						sql = "select count(*) from clarification_tracker as clart "
								+ "where (clart.entityTypeId=18 and clart.entityInstanceId=:prodId) "
								+ "or (clart.entityTypeId=34 and clart.entityInstanceId in (:awpIds)) "
								+ "or (clart.entityTypeId=28 and clart.entityInstanceId in (:actIds)) ";

						if(jtStartIndex != -1 && jtPageSize != -1 ){
							sql = sql + "limit ";								
							sql = sql+jtStartIndex+" ,"+jtPageSize+"";	
						}
						clartCount = ((Number)sessionFactory.getCurrentSession().createSQLQuery(sql)
								.setParameter("prodId", productId)
								.setParameterList("awpIds", awpIdList)
								.setParameterList("actIds", actIdList)
								.uniqueResult()).intValue();		
					}else{
						sql = "select count(*) from clarification_tracker as clart "
								+ "where (clart.entityTypeId=18 and clart.entityInstanceId=:prodId) "
								+ "or (clart.entityTypeId=34 and clart.entityInstanceId in (:awpIds)) ";								

						if(jtStartIndex != -1 && jtPageSize != -1 ){
							sql = sql + "limit ";								
							sql = sql+jtStartIndex+" ,"+jtPageSize+"";	
						}
						clartCount = ((Number)sessionFactory.getCurrentSession().createSQLQuery(sql)
								.setParameter("prodId", productId)
								.setParameterList("awpIds", awpIdList)
								.uniqueResult()).intValue();	
					}
				}
							
			}else{
				sql ="select count(*) from clarification_tracker as clart "
						+ "where clart.entityTypeId=18 and clart.entityInstanceId=:prodId "; 

				if(jtStartIndex != -1 && jtPageSize != -1 ){
					sql = sql + "limit ";								
					sql = sql+jtStartIndex+" ,"+jtPageSize+"";	
				}
				clartCount = ((Number)sessionFactory.getCurrentSession().createSQLQuery(sql)
						.setParameter("prodId", productId).uniqueResult()).intValue();
			}
		} catch (Exception e) {
			log.error("getClarificationTrackerCountFromProductHierarchy failed" ,e);
		}
		return clartCount;
	}
	
}
