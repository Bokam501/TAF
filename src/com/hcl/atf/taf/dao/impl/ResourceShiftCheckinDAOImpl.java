package com.hcl.atf.taf.dao.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.dao.ResourceShiftCheckinDAO;
import com.hcl.atf.taf.model.ActualShift;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.ResourceShiftCheckIn;
import com.hcl.atf.taf.model.json.JsonResourceShiftCheckin;


@Repository
public class ResourceShiftCheckinDAOImpl implements ResourceShiftCheckinDAO {
	private static final Log log = LogFactory.getLog(ResourceShiftCheckinDAOImpl.class);
	@Autowired
	private SessionFactory sessionFactory;
	@Override
	@Transactional
	public void addResourceShiftCkeckIn(ResourceShiftCheckIn resourceShiftCheckin) {
		log.info("adding ResourceShiftCheckin instance");
		try {
			sessionFactory.getCurrentSession().save(resourceShiftCheckin);
			log.debug("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}
	}
	@Override
	@Transactional
	public void updateResourceShiftCkeckIn(ResourceShiftCheckIn resourceShiftCheckin) {
		log.debug("updating ResourceShift CheckIn instance");
		try {			
			sessionFactory.getCurrentSession().update(resourceShiftCheckin);
			
		} catch (RuntimeException re) {
			log.error("update failed", re);
		} 
		
		
	}


	@Override
	@Transactional
	public List<ResourceShiftCheckIn> listResourceShiftCkeckInByUserId(int userId) {
		List<ResourceShiftCheckIn> resourceShiftCheckinList = null;
		try {Criteria c = sessionFactory.getCurrentSession().createCriteria(ResourceShiftCheckIn.class, "rscin");
			c.createAlias("rscin.userList", "userList");
			c.add(Restrictions.eq("userList.userId", userId));
			resourceShiftCheckinList=c.list();
			log.info("resourceShiftCheckinList size-->"+resourceShiftCheckinList.size());
			if (!(resourceShiftCheckinList == null || resourceShiftCheckinList.isEmpty())) {
				for (ResourceShiftCheckIn resourceShiftCheckin : resourceShiftCheckinList) {
					Hibernate.initialize(resourceShiftCheckin.getActualShift());
					Hibernate.initialize(resourceShiftCheckin.getActualShift().getShift());
					Hibernate.initialize(resourceShiftCheckin.getUserList());
					Hibernate.initialize(resourceShiftCheckin.getApproverUser());
					if(resourceShiftCheckin.getProductMaster()!=null){
					Hibernate.initialize(resourceShiftCheckin.getProductMaster());
					}
				}
			}
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return resourceShiftCheckinList;
	}
	@Override
	@Transactional
	public ResourceShiftCheckIn getByresourceShiftCheckInId(int resourceShiftCheckInId) {
		List<ResourceShiftCheckIn> resourceShiftCheckinList = null;
		ResourceShiftCheckIn resourceShiftCheckInobj = new ResourceShiftCheckIn();
		try {Criteria c = sessionFactory.getCurrentSession().createCriteria(ResourceShiftCheckIn.class, "rscin");
		
			c.add(Restrictions.eq("rscin.resourceShiftCheckInId", resourceShiftCheckInId));
			resourceShiftCheckinList=c.list();
			log.info("resourceShiftCheckinList size-->"+resourceShiftCheckinList.size());
			if (!(resourceShiftCheckinList == null || resourceShiftCheckinList.isEmpty())) {
				for (ResourceShiftCheckIn resourceShiftCheckin : resourceShiftCheckinList) {
					Hibernate.initialize(resourceShiftCheckin.getActualShift());
					Hibernate.initialize(resourceShiftCheckin.getActualShift().getShift());
					Hibernate.initialize(resourceShiftCheckin.getUserList());
					Hibernate.initialize(resourceShiftCheckin.getProductMaster());
				}
			}			
			
			resourceShiftCheckInobj = (resourceShiftCheckinList != null && resourceShiftCheckinList.size() != 0) ? (ResourceShiftCheckIn) resourceShiftCheckinList.get(0) : null;
			
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return resourceShiftCheckInobj;
	}
	
	
	@Override
	@Transactional
	public List<ResourceShiftCheckIn> listResourceShiftCkeckIn(int productId, Date workDate, int userId, ActualShift actualShift,int isApproved, int shiftTypeId) {
		List<ResourceShiftCheckIn> resourceShiftCheckinList = null;
		
		log.info("inside listResourceShiftCkeckIn");
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ResourceShiftCheckIn.class, "rscin");
			c.createAlias("rscin.productMaster", "productMaster");
			c.createAlias("rscin.actualShift", "actualShift");
			c.createAlias("actualShift.shift", "shift");
			c.createAlias("shift.shiftType", "sType");
			c.createAlias("rscin.userList", "userList");
			if(productId!=-1){
			c.add(Restrictions.eq("productMaster.productId", productId));
			}
			c.add(Restrictions.eq("rscin.createdDate", workDate));
			if(userId!=-1){
			c.add(Restrictions.eq("userList.userId", userId));
			}if(actualShift!=null){
				
			c.add(Restrictions.eq("actualShift.actualShiftId", actualShift.getActualShiftId()));
			}
			if(isApproved==1){
				c.add(Restrictions.eq("rscin.isApproved", isApproved));
			}else if(isApproved==0){
				c.add(Restrictions.eq("rscin.isApproved", isApproved));
			}else if(isApproved==-1){
				
			}
			if(shiftTypeId != 0){
				c.add(Restrictions.eq("sType.shiftTypeId", shiftTypeId));
			}
			
			resourceShiftCheckinList=c.list();
			
			log.info("resourceShiftCheckinList size-->"+resourceShiftCheckinList.size());
			if (!(resourceShiftCheckinList == null || resourceShiftCheckinList.isEmpty())) {
				for (ResourceShiftCheckIn resourceShiftCheckin : resourceShiftCheckinList) {
					Hibernate.initialize(resourceShiftCheckin.getActualShift());
					Hibernate.initialize(resourceShiftCheckin.getActualShift().getShift());
					Hibernate.initialize(resourceShiftCheckin.getActualShift().getShift().getShiftType());
					Hibernate.initialize(resourceShiftCheckin.getActualShift().getShift().getTestFactory());
					Hibernate.initialize(resourceShiftCheckin.getUserList());
					Hibernate.initialize(resourceShiftCheckin.getApproverUser());
					Hibernate.initialize(resourceShiftCheckin.getProductMaster());
				}
			}
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return resourceShiftCheckinList;
	}
	@Override
	@Transactional
	public List<ProductMaster> getProductByResourceShiftCheckInId(
			int resourceShiftCheckInId) {
		List<ResourceShiftCheckIn> resourceShiftCheckinList = null;
		List<ProductMaster> productList = null;
		ResourceShiftCheckIn resourceShiftCheckInobj = new ResourceShiftCheckIn();
		try {Criteria c = sessionFactory.getCurrentSession().createCriteria(ProductMaster.class, "product");
			c.createAlias("product.resourceShiftCheckInSet", "resourceCheck");
			c.add(Restrictions.eq("resourceCheck.resourceShiftCheckInId", resourceShiftCheckInId));
			productList=c.list();
			resourceShiftCheckInobj = (resourceShiftCheckinList != null && resourceShiftCheckinList.size() != 0) ? (ResourceShiftCheckIn) resourceShiftCheckinList.get(0) : null;
				
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return productList;
	}
	@Override
	@Transactional
	public void deleteResorceShiftCheckIn(ResourceShiftCheckIn resourceShiftCheckIn) {
		log.info("resourceShiftCheckIn id"+resourceShiftCheckIn.getResourceShiftCheckInId());
		try {
			sessionFactory.getCurrentSession().delete(resourceShiftCheckIn);
			log.debug("deletion successful");
		} catch (RuntimeException re) {
			log.error("deletion failed", re);
		}
		
	}
	@Override
	@Transactional
	public List<ResourceShiftCheckIn> getResourceShiftCheckInByDate(Date createdDate, int userId) {
		List<ResourceShiftCheckIn> resourceShiftCheckinList = null;
		ResourceShiftCheckIn resourceShiftCheckInobj = new ResourceShiftCheckIn();
		try {Criteria c = sessionFactory.getCurrentSession().createCriteria(ResourceShiftCheckIn.class, "rscin");
		
			c.add(Restrictions.eq("rscin.createdDate", createdDate));
			if(userId != 0){
				c.add(Restrictions.eq("rscin.userList.userId", userId));
			}
			resourceShiftCheckinList=c.list();
			log.info("resourceShiftCheckinList size-->"+resourceShiftCheckinList.size());
			if (!(resourceShiftCheckinList == null || resourceShiftCheckinList.isEmpty())) {
				for (ResourceShiftCheckIn resourceShiftCheckin : resourceShiftCheckinList) {
					Hibernate.initialize(resourceShiftCheckin.getActualShift());
					Hibernate.initialize(resourceShiftCheckin.getActualShift().getShift());
					Hibernate.initialize(resourceShiftCheckin.getUserList());
					Hibernate.initialize(resourceShiftCheckin.getProductMaster());
				}
			}			
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return resourceShiftCheckinList;
	}
	@Override
	@Transactional
	public List<ResourceShiftCheckIn> getResourceShiftCheckInByDateAndShift(Date createdDate,int actualShiftId) {
		List<ResourceShiftCheckIn> resourceShiftCheckinList = null;
		ResourceShiftCheckIn resourceShiftCheckInobj = new ResourceShiftCheckIn();
		try {Criteria c = sessionFactory.getCurrentSession().createCriteria(ResourceShiftCheckIn.class, "rscin");
		c.createAlias("rscin.actualShift", "actualShift");
			c.add(Restrictions.eq("rscin.createdDate", createdDate));
			if(actualShiftId!=0){
			c.add(Restrictions.eq("actualShift.actualShiftId", actualShiftId));
			}
			resourceShiftCheckinList=c.list();
			log.info("resourceShiftCheckinList size-->"+resourceShiftCheckinList.size());
			if (!(resourceShiftCheckinList == null || resourceShiftCheckinList.isEmpty())) {
				for (ResourceShiftCheckIn resourceShiftCheckin : resourceShiftCheckinList) {
					Hibernate.initialize(resourceShiftCheckin.getActualShift());
					Hibernate.initialize(resourceShiftCheckin.getActualShift().getShift());
					Hibernate.initialize(resourceShiftCheckin.getUserList());
					Hibernate.initialize(resourceShiftCheckin.getProductMaster());
				}
			}			
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return resourceShiftCheckinList;
	}
	
	@Override
	@Transactional
	public JsonResourceShiftCheckin getResourceShiftCheckInsForAWeekDay(Date workDate, int userId, int shiftTypeId) {
		log.info("ResourceShiftCheckIns for userId: "+userId+"        >>>>>>> work Date: "+workDate+ "    >>>>>>> shiftTypeId: "+shiftTypeId);
		JsonResourceShiftCheckin jsonResourceShiftCheckin = null;
		List<ResourceShiftCheckIn> resourceShiftCheckinList = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ResourceShiftCheckIn.class, "rscin");
			c.createAlias("rscin.actualShift", "as");
			c.createAlias("as.shift", "ws");
			c.createAlias("ws.shiftType", "st");
			c.add(Restrictions.eq("rscin.createdDate", workDate));
			if(userId != 0){
				c.add(Restrictions.eq("rscin.userList.userId", userId));
			}
			c.add(Restrictions.eqProperty("rscin.actualShift.actualShiftId", "as.actualShiftId"));
			c.add(Restrictions.eqProperty("as.shift.shiftId", "ws.shiftId"));
			c.add(Restrictions.eqProperty("ws.shiftType.shiftTypeId", "st.shiftTypeId"));
			c.add(Restrictions.eq("st.shiftTypeId", shiftTypeId));
			resourceShiftCheckinList=c.list();
			log.info("resourceShiftCheckinList size-->"+resourceShiftCheckinList.size());
			if (!(resourceShiftCheckinList == null || resourceShiftCheckinList.isEmpty())) {
				for (ResourceShiftCheckIn resourceCheckin : resourceShiftCheckinList) {
					Hibernate.initialize(resourceCheckin.getActualShift());
					Hibernate.initialize(resourceCheckin.getActualShift().getShift());
					Hibernate.initialize(resourceCheckin.getActualShift().getShift().getShiftType());
					Hibernate.initialize(resourceCheckin.getUserList());
					Hibernate.initialize(resourceCheckin.getProductMaster());
				}
				long tempcheckInCheckOutDurationInMinutes = 0;
				Date dummyCreatedDate = null;
				for(ResourceShiftCheckIn resourceShiftCheckIn : resourceShiftCheckinList){
					jsonResourceShiftCheckin = new JsonResourceShiftCheckin(resourceShiftCheckIn);
					jsonResourceShiftCheckin.setCreatedDate(DateUtility.sdfDateformatWithOutTime(resourceShiftCheckIn.getCreatedDate()));
					jsonResourceShiftCheckin.setUserId(resourceShiftCheckIn.getUserList().getUserId());
					jsonResourceShiftCheckin.setUserName(resourceShiftCheckIn.getUserList().getLoginId());
					jsonResourceShiftCheckin.setShiftTypeId(resourceShiftCheckIn.getActualShift().getShift().getShiftType().getShiftTypeId());
					jsonResourceShiftCheckin.setShiftTypeName(resourceShiftCheckIn.getActualShift().getShift().getShiftType().getShiftName());
					dummyCreatedDate = resourceShiftCheckIn.getCreatedDate();
					if(resourceShiftCheckIn.getCheckOut() != null ){
						long checkInCheckOutDurationInMinutes = DateUtility.DateDifferenceInMinutes(resourceShiftCheckIn.getCheckIn(), resourceShiftCheckIn.getCheckOut());
						tempcheckInCheckOutDurationInMinutes = tempcheckInCheckOutDurationInMinutes+checkInCheckOutDurationInMinutes;
					}
				}
				
				log.info("**********Work Hours for : "+dummyCreatedDate+" ***********");
				String workDuration = DateUtility.convertTimeInHoursMinutes(0, new Integer((int)tempcheckInCheckOutDurationInMinutes));
				log.info("workDuration: "+workDuration);
				jsonResourceShiftCheckin.setWorkDuration(workDuration);
				log.info("------------------------------------------------------------");
			}			
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return jsonResourceShiftCheckin;
	}
	
	@Override
	@Transactional
	public List<ResourceShiftCheckIn> getResourceShiftCheckInsForADay(Date workDate, int userId, int shiftTypeId) {
		List<ResourceShiftCheckIn> resourceShiftCheckinList = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ResourceShiftCheckIn.class, "rscin");
			c.createAlias("rscin.actualShift", "as");
			c.createAlias("as.shift", "ws");
			c.createAlias("ws.shiftType", "st");
			c.add(Restrictions.eq("rscin.createdDate", workDate));
			if(userId != 0){
				c.add(Restrictions.eq("rscin.userList.userId", userId));
			}
			c.add(Restrictions.eqProperty("rscin.actualShift.actualShiftId", "as.actualShiftId"));
			c.add(Restrictions.eqProperty("as.shift.shiftId", "ws.shiftId"));
			c.add(Restrictions.eqProperty("ws.shiftType.shiftTypeId", "st.shiftTypeId"));
			c.add(Restrictions.eq("st.shiftTypeId", shiftTypeId));
			resourceShiftCheckinList=c.list();
			if (!(resourceShiftCheckinList == null || resourceShiftCheckinList.isEmpty())) {
				for (ResourceShiftCheckIn resourceCheckin : resourceShiftCheckinList) {
					Hibernate.initialize(resourceCheckin.getActualShift());
					Hibernate.initialize(resourceCheckin.getActualShift().getShift());
					Hibernate.initialize(resourceCheckin.getActualShift().getShift().getShiftType());
					Hibernate.initialize(resourceCheckin.getUserList());
					Hibernate.initialize(resourceCheckin.getProductMaster());
				}
			}			
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return resourceShiftCheckinList;
	}
}
