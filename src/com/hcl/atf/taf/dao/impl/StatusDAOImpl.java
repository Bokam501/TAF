package com.hcl.atf.taf.dao.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.StatusDAO;
import com.hcl.atf.taf.model.ActivitySecondaryStatusMaster;
import com.hcl.atf.taf.model.ActivityStatus;
import com.hcl.atf.taf.model.StatusCategory;

@Service
public class StatusDAOImpl implements StatusDAO {
	private static final Log log = LogFactory.getLog(StatusDAOImpl.class);
	
	@Autowired(required=true)
    private SessionFactory sessionFactory;

	@Override
	@Transactional
	public int getNumberOfAssociatedStatus(Integer statusId, Integer status, String statusType) {
		log.info("inside getNumberOfAssociatedStatus ");
		int totalAssociationCount = 0;
		try {
			if("primaryStatus".equalsIgnoreCase(statusType)){
				String sql="SELECT distinct(ace.activityStatusId) FROM activitystatus_has_secondarystatus ace";
				List<Object> statusAssociations = sessionFactory.getCurrentSession().createSQLQuery(sql).list();
				if(statusAssociations != null && statusAssociations.size() > 0){
					Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ActivityStatus.class, "primaryStatus")
					.add(Restrictions.in("primaryStatus.activityStatusId", statusAssociations))
					.createAlias("primaryStatus.dimension", "dimension")
					.add(Restrictions.eq("dimension.id", statusId));
					if(status != 2){
						criteria.add(Restrictions.eq("primaryStatus.activeStatus", status));
					}
					criteria.setProjection(Projections.rowCount());
					long totalRecordCountLong = (long) criteria.uniqueResult(); 
					totalAssociationCount = (int) totalRecordCountLong;
					
				}
			}else{
				String sql="SELECT distinct(ace.activitySecondaryStatusId) FROM activitystatus_has_secondarystatus ace";
				List<Object> statusAssociations = sessionFactory.getCurrentSession().createSQLQuery(sql).list();
				if(statusAssociations != null && statusAssociations.size() > 0){
					Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ActivitySecondaryStatusMaster.class, "secondaryStatus")
					.add(Restrictions.in("secondaryStatus.activitySecondaryStatusId", statusAssociations))
					.createAlias("secondaryStatus.dimensionId", "dimension")
					.add(Restrictions.eq("dimension.id", statusId));
					if(status != 2){
						criteria.add(Restrictions.eq("secondaryStatus.activeStatus", status));
					}
					criteria.setProjection(Projections.rowCount());
					long totalRecordCountLong = (long) criteria.uniqueResult(); 
					totalAssociationCount = (int) totalRecordCountLong;
				}
			}
			
			log.info("listing successful");
		} catch (RuntimeException re) {
			log.error("listing failed", re);		
		}
		return totalAssociationCount;
	}
	
	@Override
	@Transactional
	public List<ActivityStatus> getPrimaryStatusByStatus(Integer statusId, Integer status, Integer jtStartIndex, Integer jtPageSize) {
		log.info("listing all Primary status instance");
		List<ActivityStatus> primaryStatuses = null;
		try {
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ActivityStatus.class, "primaryStatus");
			criteria.createAlias("primaryStatus.dimension", "dimension");
			criteria.add(Restrictions.eq("dimension.id", statusId));	
			if(status != 2){
				criteria.add(Restrictions.eq("primaryStatus.activeStatus", status));
			}
			if(jtStartIndex != null && jtPageSize != null)
				criteria.setFirstResult(jtStartIndex).setMaxResults(jtPageSize);
			
			primaryStatuses = criteria.list();
			
			for (ActivityStatus primaryStatus : primaryStatuses) {
				Hibernate.initialize(primaryStatus.getDimension());
			}
			log.info("listing successful");
		} catch (RuntimeException re) {
			log.error("listing failed", re);		
		}
		return primaryStatuses;
	}

	@Override
	@Transactional
	public int getTotalRecordsForPrimaryStatusPagination(Integer statusId, Integer status, Class<ActivityStatus> className) {
		log.info("getting records count for pagination");
		int totalRecordCount = 0;
		try {
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(className.getName(), "primaryStatus");
			criteria.createAlias("primaryStatus.dimension", "dimension");
			criteria.add(Restrictions.eq("dimension.id", statusId));
			if(status != 2){
				criteria.add(Restrictions.eq("primaryStatus.activeStatus", status));
			}
			criteria.setProjection(Projections.rowCount());
			long totalRecordCountLong = (long) criteria.uniqueResult(); 
			totalRecordCount = (int) totalRecordCountLong;
			
			log.info("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);		
		}
		return totalRecordCount;
	}

	@Override
	@Transactional
	public ActivityStatus getPrimaryStatusAvailability(Integer statusId, String activityStatusName, Integer activityStatusId) {
		log.info("Check availability of Primary status instance");
		ActivityStatus primaryStatus = null;
		try {
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ActivityStatus.class, "primaryStatus");
			criteria.createAlias("primaryStatus.dimension", "dimension");
			criteria.add(Restrictions.eq("primaryStatus.activityStatusName", activityStatusName));
			criteria.add(Restrictions.eq("dimension.id", statusId));	
			
			if(activityStatusId != null && activityStatusId != 0){
				criteria.add(Restrictions.ne("primaryStatus.activityStatusId", activityStatusId));
			}
			
			List<ActivityStatus> primaryStatuses = criteria.list();
			
			primaryStatus = (primaryStatuses != null && primaryStatuses.size() > 0) ? primaryStatuses.get(0) : null;
			
			log.info("listing successful");
		} catch (RuntimeException re) {
			log.error("listing failed", re);		
		}
		return primaryStatus;
	}
	
	@Override
	@Transactional
	public void addPrimaryStatus(ActivityStatus activityStatus) {
		log.info("adding primary status instance");
		try {
			sessionFactory.getCurrentSession().save(activityStatus);
			
			ActivitySecondaryStatusMaster activitySecondaryStatusMaster = new ActivitySecondaryStatusMaster();
			activitySecondaryStatusMaster.setActivitySecondaryStatusName(activityStatus.getActivityStatusName());
			activitySecondaryStatusMaster.setDescription(activityStatus.getActivityStatusDescription());
			activitySecondaryStatusMaster.setDimensionId(activityStatus.getDimension());
			activitySecondaryStatusMaster.setActiveStatus(1);
			
			sessionFactory.getCurrentSession().save(activitySecondaryStatusMaster);
			
			sessionFactory.getCurrentSession().createSQLQuery("INSERT INTO  activitystatus_has_secondarystatus (activityStatusId, activitySecondaryStatusId) values("+activityStatus.getActivityStatusId()+", "+activitySecondaryStatusMaster.getActivitySecondaryStatusId()+")").executeUpdate();
			
			log.info("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}
	}

	@Override
	@Transactional
	public void updatePrimaryStatus(ActivityStatus activityStatus) {
		log.info("updating primary status instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(activityStatus);
			log.info("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
		}
	}

	@Override
	@Transactional
	public List<ActivitySecondaryStatusMaster> getSecondaryStatusByStatus(Integer statusId, Integer status, Integer jtStartIndex, Integer jtPageSize) {
		log.info("listing all Secondary status instance");
		List<ActivitySecondaryStatusMaster> secondaryStatuses = null;
		try {
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ActivitySecondaryStatusMaster.class, "secondaryStatus");
			criteria.createAlias("secondaryStatus.dimensionId", "dimension");
			criteria.add(Restrictions.eq("dimension.id", statusId));	
			if(status != 2){
				criteria.add(Restrictions.eq("secondaryStatus.activeStatus", status));
			}
			if(jtStartIndex != null && jtPageSize != null)
				criteria.setFirstResult(jtStartIndex).setMaxResults(jtPageSize);
			
			secondaryStatuses = criteria.list();
			
			for (ActivitySecondaryStatusMaster secondaryStatus : secondaryStatuses) {
				Hibernate.initialize(secondaryStatus.getDimensionId());
			}
			log.info("listing successful");
		} catch (RuntimeException re) {
			log.error("listing failed", re);		
		}
		return secondaryStatuses;
	}

	@Override
	@Transactional
	public int getTotalRecordsForSecondaryStatusPagination(Integer statusId, Integer status, Class<ActivitySecondaryStatusMaster> className) {
		log.info("getting records count for pagination");
		int totalRecordCount = 0;
		try {
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(className.getName(), "secondaryStatus");
			criteria.createAlias("secondaryStatus.dimensionId", "dimension");
			criteria.add(Restrictions.eq("dimension.id", statusId));
			if(status != 2){
				criteria.add(Restrictions.eq("secondaryStatus.activeStatus", status));
			}
			criteria.setProjection(Projections.rowCount());
			long totalRecordCountLong = (long) criteria.uniqueResult(); 
			totalRecordCount = (int) totalRecordCountLong;
			
			log.info("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);		
		}
		return totalRecordCount;
	}

	@Override
	@Transactional
	public ActivitySecondaryStatusMaster getSecondaryStatusAvailability(Integer statusId, String activitySecondaryStatusName, Integer activitySecondaryStatusId) {
		log.info("Check availability of Secondary status instance");
		ActivitySecondaryStatusMaster secondaryStatus = null;
		try {
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ActivitySecondaryStatusMaster.class, "secondaryStatus");
			criteria.createAlias("secondaryStatus.dimensionId", "dimension");
			criteria.add(Restrictions.eq("secondaryStatus.activitySecondaryStatusName", activitySecondaryStatusName));
			criteria.add(Restrictions.eq("dimension.id", statusId));	
			
			if(activitySecondaryStatusId != null && activitySecondaryStatusId != 0){
				criteria.add(Restrictions.ne("secondaryStatus.activitySecondaryStatusId", activitySecondaryStatusId));
			}
			
			List<ActivitySecondaryStatusMaster> secondaryStatusMasters = criteria.list();
			
			secondaryStatus = (secondaryStatusMasters != null && secondaryStatusMasters.size() > 0) ? secondaryStatusMasters.get(0) : null;
			
			log.info("listing successful");
		} catch (RuntimeException re) {
			log.error("listing failed", re);		
		}
		return secondaryStatus;
	}
	
	@Override
	@Transactional
	public void addSecondaryStatus(ActivitySecondaryStatusMaster activitySecondaryStatusMaster) {
		log.info("updating secondary status instance");
		try {
			sessionFactory.getCurrentSession().save(activitySecondaryStatusMaster);
			log.info("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
		}
	}

	@Override
	@Transactional
	public void updateSecondaryStatus(ActivitySecondaryStatusMaster activitySecondaryStatusMaster) {
		log.info("updating secondary status instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(activitySecondaryStatusMaster);
			log.info("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
		}
	}

	@Override
	@Transactional
	public List<Object[]> getSecondaryStatusToAddWithPrimaryStatus(Integer primaryStatusId, Integer statusId, Integer jtStartIndex, Integer jtPageSize) {
		log.info("listing all secondary status that not mapped with primar status id - "+primaryStatusId);
		List<Object[]> secondaryStatusNotMappedWithPrimary = null;
		try {
			String sql="SELECT distinct(ace.activitySecondaryStatusId) FROM activitystatus_has_secondarystatus ace WHERE ace.activityStatusId=:statusId";
			List<Object> secondaryStatusMappedWithPrimary = sessionFactory.getCurrentSession().createSQLQuery(sql).setParameter("statusId", primaryStatusId).list();
			
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ActivitySecondaryStatusMaster.class, "secondaryStatus");
			if(secondaryStatusMappedWithPrimary != null && secondaryStatusMappedWithPrimary.size() > 0){
				criteria.add(Restrictions.not(Restrictions.in("secondaryStatus.activitySecondaryStatusId", secondaryStatusMappedWithPrimary)));
			}
			criteria.createAlias("secondaryStatus.dimensionId", "dimension")
			.add(Restrictions.eq("dimension.dimensionId", statusId))
			.setProjection(Projections.projectionList()
				.add(Projections.property("activitySecondaryStatusId"))
	    		.add(Projections.property("activitySecondaryStatusName")
	    	));
			if(jtStartIndex != null && jtPageSize != null){
				criteria.setFirstResult(jtStartIndex).setMaxResults(jtPageSize);
			}
			criteria.add(Restrictions.eq("secondaryStatus.activeStatus", 1));
			secondaryStatusNotMappedWithPrimary = criteria.list();
			log.info("listing successful");
		} catch (RuntimeException re) {
			log.error("listing failed", re);		
		}
		return secondaryStatusNotMappedWithPrimary;
	}

	@Override
	@Transactional
	public Integer getSecondaryStatusToAddWithPrimaryStatusCount(Integer primaryStatusId, Integer statusId) {
		log.info("Count of secondary status that not mapped with primar status id - "+primaryStatusId);
		int totalRecordCount = 0;
		try {
			String sql="SELECT distinct(ace.activitySecondaryStatusId) FROM activitystatus_has_secondarystatus ace WHERE ace.activityStatusId=:statusId";
			List<Object> secondaryStatusMappedWithPrimary = sessionFactory.getCurrentSession().createSQLQuery(sql).setParameter("statusId", primaryStatusId).list();
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ActivitySecondaryStatusMaster.class, "secondaryStatus");
			if(secondaryStatusMappedWithPrimary != null && secondaryStatusMappedWithPrimary.size() > 0){
				criteria.add(Restrictions.not(Restrictions.in("secondaryStatus.activitySecondaryStatusId", secondaryStatusMappedWithPrimary)));
			}
			criteria.createAlias("secondaryStatus.dimensionId", "dimension")
			.add(Restrictions.eq("dimension.dimensionId", statusId))
			.add(Restrictions.eq("secondaryStatus.activeStatus", 1))
			.setProjection(Projections.rowCount());
			
			long totalRecordCountLong = (long) criteria.uniqueResult(); 
			totalRecordCount = (int) totalRecordCountLong;
		} catch (RuntimeException re) {
			log.error("listing failed", re);		
		}
		return totalRecordCount;
	}

	@Override
	@Transactional
	public List<Object[]> getSecondaryStatusMappedWithPrimaryStatus(Integer primaryStatusId, Integer statusId) {
		log.info("listing all secondary status that mapped with primar status id - "+primaryStatusId);
		List<Object[]> secondaryStatusNotMappedWithPrimary = null;
		try {
			String sql="SELECT distinct(ace.activitySecondaryStatusId) FROM activitystatus_has_secondarystatus ace WHERE ace.activityStatusId=:statusId";
			List<Object> secondaryStatusMappedWithPrimary = sessionFactory.getCurrentSession().createSQLQuery(sql).setParameter("statusId", primaryStatusId).list();
			if(secondaryStatusMappedWithPrimary != null && secondaryStatusMappedWithPrimary.size() > 0){
				Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ActivitySecondaryStatusMaster.class, "secondaryStatus")
				.add(Restrictions.in("secondaryStatus.activitySecondaryStatusId", secondaryStatusMappedWithPrimary))
				.createAlias("secondaryStatus.dimensionId", "dimension")
				.add(Restrictions.eq("dimension.dimensionId", statusId))
				.setProjection(Projections.projectionList()
					.add(Projections.property("activitySecondaryStatusId"))
		    		.add(Projections.property("activitySecondaryStatusName")
		    	));
				criteria.add(Restrictions.eq("secondaryStatus.activeStatus", 1));
				secondaryStatusNotMappedWithPrimary = criteria.list();
			}
			
			log.info("listing successful");
		} catch (RuntimeException re) {
			log.error("listing failed", re);		
		}
		return secondaryStatusNotMappedWithPrimary;
	}

	@Override
	@Transactional
	public boolean isSecondaryStatusAlreadyMappedToPrimaryStatus(Integer primaryStatusId, Integer secondaryStatusId) {
		log.info("check whether secondary status is mapped with primar status id - "+primaryStatusId);
		boolean isAlreadyMapped = false;
		try {
			String sql="SELECT distinct(ace.activitySecondaryStatusId) FROM activitystatus_has_secondarystatus ace WHERE ace.activityStatusId=:primaryStatusId and ace.activitySecondaryStatusId=:secondaryStatusId";
			List<Object> secondaryStatusMappedWithPrimary = sessionFactory.getCurrentSession().createSQLQuery(sql).setParameter("primaryStatusId", primaryStatusId).setParameter("secondaryStatusId", secondaryStatusId).list();
			if(secondaryStatusMappedWithPrimary != null && secondaryStatusMappedWithPrimary.size() > 0){
				isAlreadyMapped = true;
			}
		}catch (Exception re) {
			log.error("Availabilty check failed ", re);		
		}
		
		return isAlreadyMapped;
	}

	@Override
	@Transactional
	public void mapOrUnmapSecondaryStatusForPrimaryStatus(Integer primaryStatusId, Integer secondaryStatusId, String mapOrUnmap) {

		log.info(mapOrUnmap+" secondary status for primary status");
		try {
			log.info("update is executed");
			if(mapOrUnmap == null || mapOrUnmap.isEmpty() || mapOrUnmap.equalsIgnoreCase("map")){
				sessionFactory.getCurrentSession().createSQLQuery("INSERT INTO  activitystatus_has_secondarystatus (activityStatusId, activitySecondaryStatusId) values("+primaryStatusId+", "+secondaryStatusId+")").executeUpdate();
			}else{
				sessionFactory.getCurrentSession().createSQLQuery("DELETE FROM activitystatus_has_secondarystatus WHERE activityStatusId = "+primaryStatusId+" and activitySecondaryStatusId = "+secondaryStatusId).executeUpdate();
			}
			log.info(mapOrUnmap+" successful");
		} catch (RuntimeException re) {
			log.error(mapOrUnmap+" failed", re);
		}
	}

	@Override
	@Transactional
	public List<StatusCategory> listAllStatusCategories(){
		List<StatusCategory> statusCategoriesList = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(StatusCategory.class, "statusCategory");
			statusCategoriesList = c.list();
			log.debug("list all Status categories successful");
		} catch (Exception ex) {
			log.error("list all Status categories failed", ex);
		}
		return statusCategoriesList;
	}

	@Override
	@Transactional
	public StatusCategory getStatusCategoryByName(String statusCategoryName) {
		StatusCategory statusCategory = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(StatusCategory.class, "statusCategory");
			c.add(Restrictions.eq("statusCategory.statusCategoryName", statusCategoryName));
			List<StatusCategory> statusCategoriesList = c.list();
			if(statusCategoriesList != null && statusCategoriesList.size() > 0){
				statusCategory = statusCategoriesList.get(0);
			}
			log.debug("Get Status categories by name successful");
		} catch (Exception ex) {
			log.error("Get Status categories by name failed", ex);
		}
		return statusCategory;
	}
	
	@Override
	@Transactional
	public StatusCategory getStatusCategoryBystatusCategoryId(Integer statusCategoryId) {
		StatusCategory statusCategory = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(StatusCategory.class, "statusCategory");
			c.add(Restrictions.eq("statusCategory.statusCategoryId", statusCategoryId));
			List<StatusCategory> statusCategoriesList = c.list();
			if(statusCategoriesList != null && statusCategoriesList.size() > 0){
				statusCategory = statusCategoriesList.get(0);
			}
			log.debug("Get Status categories by statusCategoryId successful");
		} catch (Exception ex) {
			log.error("Get Status categories by statusCategoryId failed", ex);
		}
		return statusCategory;
	}

}
