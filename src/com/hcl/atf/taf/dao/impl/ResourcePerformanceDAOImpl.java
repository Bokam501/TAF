package com.hcl.atf.taf.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.ResourcePerformanceDAO;
import com.hcl.atf.taf.model.ResourceDailyPerformance;
import com.hcl.atf.taf.model.dto.ResourceExperienceSummaryDTO;

@Repository
public class ResourcePerformanceDAOImpl implements ResourcePerformanceDAO {
	
	private static final Log log = LogFactory.getLog(ResourcePerformanceDAOImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	@Transactional
	public ResourceDailyPerformance getResourceDailyPerformance(Integer userId, Date workDate, Integer actualShiftId, Integer workPackageId) {
		log.info("listing getResourceDailyPerformance");
		ResourceDailyPerformance resourceDailyPerformance = null;
				
		try {
			
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ResourceDailyPerformance.class,"resDPerf");
			
			c.createAlias("resDPerf.user", "testerPerson");			
			c.createAlias("resDPerf.workPackage", "wPackage");
			
			c.add(Restrictions.eq("resDPerf.workDate",workDate));
			c.add(Restrictions.eq("testerPerson.userId",userId));
			if(actualShiftId != null && actualShiftId != 0){
				c.createAlias("resDPerf.actualShift", "actualShft");
				c.add(Restrictions.eq("actualShft.actualShiftId",actualShiftId));
			}			
			c.add(Restrictions.eq("wPackage.workPackageId",workPackageId));
			
			List list = c.list();
			resourceDailyPerformance = (list != null && list.size() != 0) ? (ResourceDailyPerformance) list.get(0) : null;
			
			if (resourceDailyPerformance != null) {
					Hibernate.initialize(resourceDailyPerformance.getActualShift());
					Hibernate.initialize(resourceDailyPerformance.getWorkPackage());
					Hibernate.initialize(resourceDailyPerformance.getUser());
					Hibernate.initialize(resourceDailyPerformance.getRatedByUser());
					Hibernate.initialize(resourceDailyPerformance.getApprovedByUser());
					if(resourceDailyPerformance.getPerformanceLevel() != null && resourceDailyPerformance.getPerformanceLevel().getPerformanceLevelId() != 0){
						Hibernate.initialize(resourceDailyPerformance.getPerformanceLevel());
					}
			}
			
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return resourceDailyPerformance;
	}
	
	@Override
	@Transactional
	public void updateResourceDailyPerformance(ResourceDailyPerformance resourceDailyPerformance) {
		log.debug("Adding/updating ResourceDailyPerformance instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(resourceDailyPerformance);
			log.debug("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
		}
	}	
	
	@Override
	@Transactional
	public List<ResourceExperienceSummaryDTO> getResourceAveragePerformance(Integer userId, Integer workPackageId) {
		log.debug("listing userAveragePerformanceRating");
		List<ResourceExperienceSummaryDTO> listOfResourceExperienceSummaryDTO = new ArrayList<ResourceExperienceSummaryDTO>();
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ResourceDailyPerformance.class,"resDPerf");
			c.createAlias("resDPerf.user", "tester");
			c.createAlias("resDPerf.workPackage", "workPackage");
			c.add(Restrictions.eq("tester.userId",userId));
			c.add(Restrictions.eq("workPackage.workPackageId",workPackageId));
			
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Property.forName("tester.userId"));
			projectionList.add(Property.forName("tester.loginId"));
			projectionList.add(Property.forName("workPackage.workPackageId"));
			projectionList.add(Property.forName("workPackage.name"));
			projectionList.add(Projections.avg("resDPerf.performanceLevel"));
			c.setProjection(projectionList);
			
			List<Object[]> list = c.list();
			ResourceExperienceSummaryDTO resourceExperienceSummaryDTO = null;
			for (Object[] row : list) {
				resourceExperienceSummaryDTO = new ResourceExperienceSummaryDTO();
				resourceExperienceSummaryDTO.setUserId((Integer)row[0]);
				resourceExperienceSummaryDTO.setUserLoginId((String)row[1]);
				resourceExperienceSummaryDTO.setWorkPackageId((Integer)row[2]);
				resourceExperienceSummaryDTO.setWorkPackageName((String)row[3]);
				if(row[4] != null){
					resourceExperienceSummaryDTO.setUserAveragePerformanceRating(((Double)row[4]).intValue());
				}else{
					resourceExperienceSummaryDTO.setUserAveragePerformanceRating(0);
				}
				listOfResourceExperienceSummaryDTO.add(resourceExperienceSummaryDTO);
			}
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return listOfResourceExperienceSummaryDTO;
	}

	@Override
	@Transactional
	public List<ResourceDailyPerformance> getResourceAveragePerformanceDetails(Integer userId, Integer workPackageId) {
		log.info("listing getResourceAveragePerformanceDetails");
		List<ResourceDailyPerformance> listOfResourceDailyPerformance = new ArrayList<ResourceDailyPerformance>();
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ResourceDailyPerformance.class,"resDPerf");
			c.createAlias("resDPerf.user", "tester");
			c.createAlias("resDPerf.workPackage", "workPackage");
			c.add(Restrictions.eq("tester.userId",userId));
			c.add(Restrictions.eq("workPackage.workPackageId",workPackageId));
			
			listOfResourceDailyPerformance = c.list();
			log.info("Result Set Size : " + listOfResourceDailyPerformance.size());
			for (ResourceDailyPerformance resourceDailyPerformance : listOfResourceDailyPerformance) {
				resourceDailyPerformance.getUser();
				resourceDailyPerformance.getActualShift();
				resourceDailyPerformance.getActualShift().getShift();
				resourceDailyPerformance.getWorkPackage();
				resourceDailyPerformance.getRatedByUser();
				resourceDailyPerformance.getApprovedByUser();
			}
			
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return listOfResourceDailyPerformance;
	}


	@Override
	@Transactional
	public ResourceDailyPerformance getResourceDailyPerformanceWithHighRating(Integer userId, Date fromDate, Date toDate) {
		log.info("listing getResourceAveragePerformanceDetails");
		Criteria c = sessionFactory.getCurrentSession().createCriteria(ResourceDailyPerformance.class,"resDPerf");
		c.createAlias("resDPerf.user", "user");
		c.createAlias("resDPerf.performanceLevel", "performanceLevel");
		c.add(Restrictions.eq("user.userId",userId));
		c.add(Restrictions.between("resDPerf.workDate",fromDate,toDate ));
		c.addOrder(Order.desc("performanceLevel.performanceLevelId")).setMaxResults(1);
		ResourceDailyPerformance resourceDailyPerformance =  (ResourceDailyPerformance)c.uniqueResult();
		if (resourceDailyPerformance != null)
			Hibernate.initialize(resourceDailyPerformance.getPerformanceLevel());
		
		return resourceDailyPerformance;
	}
}
