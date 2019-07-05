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
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.DashBoardDAO;
import com.hcl.atf.taf.model.ActivityCollection;
import com.hcl.atf.taf.model.DashBoardTabs;
import com.hcl.atf.taf.model.DashBoardTabsRoleBasedURL;
import com.hcl.atf.taf.model.DashboardVisualizationUrls;
import com.hcl.atf.taf.model.DefectCollection;
import com.hcl.atf.taf.model.ReviewRecordCollection;

@Repository
public class DashBoardDAOImpl implements DashBoardDAO {
	
	private static final Log log = LogFactory.getLog(DashBoardDAOImpl.class);
	@Autowired
	private SessionFactory sessionFactory;
	
	
	
	@Override
	@Transactional
	public List<ActivityCollection> getActivityCollectionCountByDateFilter(Date startDate,Date currentDate) {

		List<ActivityCollection> activityCollection = null;
		try {

			Criteria c = sessionFactory.getCurrentSession().createCriteria(ActivityCollection.class, "ac");
			c.add(Restrictions.between("ac.weekDate", startDate,  currentDate));
			activityCollection=c.list();
			log.info("Data fetched Successfully");
		} catch (Exception e) {

			log.error("Unable to get data from Table", e);
		}

		return activityCollection;
	}



	@Override
	@Transactional
	public List<DefectCollection> getDefectCollectionList() {

		List<DefectCollection> defectCollection = null;
		try {

			Criteria c = sessionFactory.getCurrentSession().createCriteria(DefectCollection.class, "dc");
			defectCollection=c.list();
			log.info("Data fetched Successfully from DefectCollection ");
		} catch (Exception e) {

			log.error("Unable to get data from Table", e);
		}

		return defectCollection;
	}
	
	
	
	
	
	@Override
	@Transactional
	public void addDashboardTabsToUI(DashBoardTabs dashBoardTabs) {
		log.info("adding DashBoardTabs instance");
		try {
			sessionFactory.getCurrentSession().save(dashBoardTabs);
			log.debug("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}}



	
	
	
	@Override
	@Transactional
	public List<DashBoardTabs> listDashboardTabs(Integer status,Integer startIndex, Integer pageSize) {
		List<DashBoardTabs> dashBoardTabs=null;
		try{
			Criteria c = sessionFactory.getCurrentSession().createCriteria(DashBoardTabs.class, "dashboardTabs");
			if(status != 2){
				c.add(Restrictions.eq("dashboardTabs.status", status));	
			}	
			
			dashBoardTabs = c.list();
		
		log.debug("list all successful tabs");
		
		}
		catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return dashBoardTabs;
		
	}
	
	@Override
	@Transactional
	public void update(DashBoardTabs dashBoardTabs) {
		log.debug("updating DashBoardTabs instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(dashBoardTabs);
			log.debug("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
		}
		
	}



	@Override
	@Transactional
	public void addDashboardTabsRoleUrl(DashBoardTabsRoleBasedURL dashBoardTabsRoleBasedURL) {
		log.debug("adding DashBoardTabsRoleBased instance");
		try {
			sessionFactory.getCurrentSession().save(dashBoardTabsRoleBasedURL);
			log.debug("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}}



	@Override
	@Transactional
	public List<DashBoardTabsRoleBasedURL> listDashboardTabsRoleBasedURL(Integer roleId,Integer tabId) {
		List<DashBoardTabsRoleBasedURL> dashBoardTabsRoleBased=null;
		try{
			Criteria c = sessionFactory.getCurrentSession().createCriteria(DashBoardTabsRoleBasedURL.class, "dbtr");
			c.createAlias("dbtr.dashBoardTabs", "tabs");
			c.createAlias("dbtr.productSpecificUserRole", "user");
		
			if(tabId==-1){ /*this is for in Dashboard page we need to display tab by user role login*/
				c.add(Restrictions.eq("user.userRoleId", roleId));
				c.add(Restrictions.eq("dbtr.status", 1));
				c.add(Restrictions.eq("tabs.status", 1));
			}if(tabId!=-1){
				c.add(Restrictions.eq("tabs.tabId", tabId));
				c.add(Restrictions.eq("dbtr.status", 1));
			}
			c.addOrder(Order.asc("tabs.orderNo"));
			dashBoardTabsRoleBased=c.list();
			
		log.info("list all successful tabsRole");
		
		for (DashBoardTabsRoleBasedURL dbUrl : dashBoardTabsRoleBased) {
			Hibernate.initialize(dbUrl.getDashBoardTabs().getTabName());
			Hibernate.initialize(dbUrl.getProductSpecificUserRole().getUserRoleId());
			Hibernate.initialize(dbUrl.getProductSpecificUserRole().getRoleLabel());
			Hibernate.initialize(dbUrl.getProductSpecificUserRole().getRoleName());

		}
		
		}
		catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return dashBoardTabsRoleBased;
		
	}



	@Override
	@Transactional
	public void update(DashBoardTabsRoleBasedURL dashBoardTabsRoleBasedFromUI) {
		log.debug("updating DashBoardTabsRole instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(dashBoardTabsRoleBasedFromUI);
			log.debug("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
		}
		
	}	
	
	
	
	
	@Override
	@Transactional
	public List<ActivityCollection> getActivityCollectionListForScheduleVariance(Date startDate,Date currentDate) {
		List<ActivityCollection> activityCollections = new ArrayList<ActivityCollection>();
		try {

			Criteria c = sessionFactory.getCurrentSession().createCriteria(ActivityCollection.class, "activity");
			c.add(Restrictions.ne("activity.actualActivityEndDate", null));
			c.add(Restrictions.between("activity.weekDate", startDate,  currentDate));
			c.addOrder(Order.desc("activity.actualActivityEndDate"));
			List resultList = c.list();
			
			if(resultList != null && resultList.size() > 0){
				activityCollections.add((ActivityCollection) resultList.get(0));
			}
			
			c = sessionFactory.getCurrentSession().createCriteria(ActivityCollection.class, "activity");
			c.add(Restrictions.ne("activity.plannedActivityEndDate", null));
			c.add(Restrictions.between("activity.weekDate", startDate,  currentDate));
			c.addOrder(Order.desc("activity.plannedActivityEndDate"));
			resultList = c.list();
			
			if(resultList != null && resultList.size() > 0){
				activityCollections.add((ActivityCollection) resultList.get(0));
			}
			
			c = sessionFactory.getCurrentSession().createCriteria(ActivityCollection.class, "activity");
			c.add(Restrictions.ne("activity.plannedActivityStartDate", null));
			c.add(Restrictions.between("activity.weekDate", startDate,  currentDate));
			c.addOrder(Order.asc("activity.plannedActivityStartDate"));
			resultList = c.list();
			
			if(resultList != null && resultList.size() > 0){
				activityCollections.add((ActivityCollection) resultList.get(0));
			}
			
			log.info("Data fetched Successfully from ActivityCollection ");
			
		} catch (Exception e) {

			log.error("Unable to get data from Table", e);
		}

		return activityCollections;
	}
	
	
	
	@Override
	@Transactional
	public List<ReviewRecordCollection> getReivewRecordCollectionList() {

		List<ReviewRecordCollection> reviewRecordCollection = null;
		try {

			Criteria c = sessionFactory.getCurrentSession().createCriteria(ReviewRecordCollection.class, "rerc");
			reviewRecordCollection=c.list();
			
			log.info("Data fetched Successfully from ReviewRecordCollection ");
			
		} catch (Exception e) {

			log.error("Unable to get data from Table", e);
		}

		return reviewRecordCollection;
	}



	@Override
	@Transactional
	public void deleteroleBasedDataById(DashBoardTabsRoleBasedURL dashboardroleuser) {
	sessionFactory.getCurrentSession().delete(dashboardroleuser);
	log.debug("DashBoardTabsRoleBasedURL deletion successful");
	
	}



	@Override
	@Transactional
	public DashBoardTabsRoleBasedURL getDashBoardTabsRoleBasedURLById(Integer roleBasedId) {
		log.debug("getting getDashBoardTabsRoleBasedURL instance by id");
		DashBoardTabsRoleBasedURL dashBoardTabsRoleBasedURL=null;
		try {
			List list =  sessionFactory.getCurrentSession().createQuery("from DashBoardTabsRoleBasedURL dbt where roleBasedId=:roleBasedId").setParameter("roleBasedId", roleBasedId)
					.list();
			dashBoardTabsRoleBasedURL=(list!=null && list.size()!=0)?(DashBoardTabsRoleBasedURL)list.get(0):null;
				
			log.debug("dashBoardTabsRoleBasedURL successful");
		} catch (RuntimeException re) {
			log.error("dashBoardTabsRoleBasedURLId failed", re);
		}
		return dashBoardTabsRoleBasedURL;
        
	}



	@Override
	@Transactional
	public List<DashBoardTabs> getDasboardTabsByEngagementId(Integer engagementId) {
		List<DashBoardTabs>dashBoardTabList=new ArrayList<DashBoardTabs>();
		try{
			
			Criteria c=sessionFactory.getCurrentSession().createCriteria(DashBoardTabs.class, "dashBoardTabs");
			c.createAlias("dashBoardTabs.engagement", "testFactory");
			c.add(Restrictions.eq("testFactory.testFactoryId", engagementId));
			dashBoardTabList=c.list();
		}catch(Exception e){
			log.error(e);
		}
		
		return dashBoardTabList;
	}

	@Override
	@Transactional
	public void addvisualizationUrls(DashboardVisualizationUrls visualizationUrls) {
		log.info("addvisualizationUrls instance");
		try{
			sessionFactory.getCurrentSession().save(visualizationUrls);
		}catch(Exception e){
			log.error(e);
			
		}
		
	}



	@Override
	@Transactional
	public List<DashboardVisualizationUrls> listDashboardVisualization(Integer status, Integer jtStartIndex, Integer jtPageSize) {
		List<DashboardVisualizationUrls> dashboardVisualizationUrls=null;
		try{
			Criteria c = sessionFactory.getCurrentSession().createCriteria(DashboardVisualizationUrls.class, "visualizationUrls");
			if(status != 2){
				c.add(Restrictions.eq("visualizationUrls.status", status));
			}
			
			dashboardVisualizationUrls = c.list();
		
		log.debug("list all successful visualizationUrls");
		
		}
		catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return dashboardVisualizationUrls;
		
	}



	@Override
	@Transactional
	public void updateVisualization(DashboardVisualizationUrls dashboardVisualizationFromUI) {
		log.debug("updating DashboardVisualizationUrls instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(dashboardVisualizationFromUI);
			log.info("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
		}
		
	}
	
	
}
