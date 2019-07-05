package com.hcl.atf.taf.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
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

import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.dao.TimeSheetManagementDAO;
import com.hcl.atf.taf.model.ActualShift;
import com.hcl.atf.taf.model.ResourceAvailability;
import com.hcl.atf.taf.model.ResourceShiftCheckIn;
import com.hcl.atf.taf.model.TimeSheetActivityType;
import com.hcl.atf.taf.model.TimeSheetEntryMaster;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.WorkShiftMaster;
import com.hcl.atf.taf.model.dto.TimeSheetDaySummaryDTO;
import com.hcl.atf.taf.model.dto.TimeSheetResourceCheckinDTO;
import com.hcl.atf.taf.model.dto.UserWeekUtilisedTimeDTO;

@Repository
public class TimeSheetManagementDAOImpl implements TimeSheetManagementDAO{
	
	private static final Log log = LogFactory.getLog(TimeSheetManagementDAOImpl.class);
	
	@Autowired(required=true)
    private SessionFactory sessionFactory;

	@Override
	@Transactional
	public List<TimeSheetEntryMaster> listTimeSheetEntries(int userId) {
		List<TimeSheetEntryMaster> listOfTimeSheetEntries = null;
		try {
			listOfTimeSheetEntries = sessionFactory.getCurrentSession().createQuery("from TimeSheetEntryMaster where status='1' and userId=:userId").setParameter("userId", userId).list();
			if(listOfTimeSheetEntries!=null && !listOfTimeSheetEntries.isEmpty()){
				for (TimeSheetEntryMaster timeSheetEntry : listOfTimeSheetEntries) {
					Hibernate.initialize(timeSheetEntry.getRole());
					Hibernate.initialize(timeSheetEntry.getProduct());
					Hibernate.initialize(timeSheetEntry.getWorkPackage());
					Hibernate.initialize(timeSheetEntry.getApprover());
					Hibernate.initialize(timeSheetEntry.getShift());
					Hibernate.initialize(timeSheetEntry.getShift().getShiftType());
					Hibernate.initialize(timeSheetEntry.getUser());
					Hibernate.initialize(timeSheetEntry.getActivityType());
					Hibernate.initialize(timeSheetEntry.getResourceShiftCheckIn());
					if(timeSheetEntry.getResourceShiftCheckIn()!=null)
						Hibernate.initialize(timeSheetEntry.getResourceShiftCheckIn().getActualShift());
				}
			}
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return listOfTimeSheetEntries;
	}

	@Override
	@Transactional
	public TimeSheetResourceCheckinDTO listTimeSheetForResourceShiftCheckin(int resourceId, int workShiftId, Date timeSheetDate) {
		List<TimeSheetResourceCheckinDTO> dailyTimeSheetSummary = new ArrayList<TimeSheetResourceCheckinDTO>();
		TimeSheetResourceCheckinDTO timeSheetResourceCheckinDTO = new TimeSheetResourceCheckinDTO();
		try {
		Criteria c = sessionFactory.getCurrentSession().createCriteria(TimeSheetEntryMaster.class,"tsem");
		c.createAlias("tsem.shift", "workShift");
		c.createAlias("workShift.shiftType", "workShiftType");
		c.createAlias("tsem.resourceShiftCheckIn", "resShiftCheckIn");
		c.createAlias("resShiftCheckIn.productMaster", "productMaster");
		c.createAlias("resShiftCheckIn.actualShift", "actualShift");
		c.add(Restrictions.eq("tsem.user.userId", Integer.valueOf(resourceId)));
		c.add(Restrictions.eq("workShift.shiftId", workShiftId));
		c.add(Restrictions.eq("tsem.date", timeSheetDate));
		c.setProjection(Projections.projectionList()
									.add(Property.forName("tsem.user.userId").as("userId"))//0
									.add(Property.forName("tsem.date").as("timeSheetDate"))//1
									.add(Property.forName("tsem.timeSheetEntryId").as("timeSheetEntryId"))//2
									.add(Property.forName("workShift.shiftId").as("shiftId"))//3
									.add(Property.forName("workShift.shiftName").as("shiftName"))//4
									.add(Property.forName("workShiftType.shiftTypeId").as("shiftTypeId"))//5
									.add(Property.forName("workShiftType.shiftName").as("shiftName"))//6
									.add(Property.forName("resShiftCheckIn.checkIn").as("checkIn"))//7
									.add(Property.forName("productMaster.productId").as("productId"))//8
									.add(Property.forName("productMaster.productName").as("productName"))//9
									.add(Property.forName("actualShift.actualShiftId").as("actualShiftId"))//10
									.add(Projections.sum("tsem.hours").as("totalHours"))//11
									.add(Projections.sum("tsem.mins").as("totalMins"))//12
									.add(Projections.groupProperty("tsem.date"))//13
									.add(Projections.groupProperty("workShift.shiftId"))//14
						)
		.addOrder(Order.asc("tsem.date"))
		.addOrder(Order.asc("workShift.shiftId"));
		
		List<Object[]> list = c.list();
		log.info("Timesheet with Resource Check-in data : " + list.size());
		for (Object[] row : list) {
			timeSheetResourceCheckinDTO = new TimeSheetResourceCheckinDTO();
			timeSheetResourceCheckinDTO.setUserId((Integer)row[0]);
			timeSheetResourceCheckinDTO.setTimeSheetEntryDate((Date)row[1]);
			timeSheetResourceCheckinDTO.setTimeSheetEntryId((Integer)row[2]);
			timeSheetResourceCheckinDTO.setWorkShiftId((Integer)(row[3]));
			timeSheetResourceCheckinDTO.setWorkShiftName((String)(row[4]));
			timeSheetResourceCheckinDTO.setShiftTypeId((Integer)(row[5]));
			timeSheetResourceCheckinDTO.setShiftName((String)(row[6]));
			timeSheetResourceCheckinDTO.setResourceCheckInDate((Date)row[7]);
			timeSheetResourceCheckinDTO.setProductId((Integer)(row[8]));
			timeSheetResourceCheckinDTO.setProductName((String)(row[9]));
			timeSheetResourceCheckinDTO.setActualShiftId((Integer)(row[10]));
			timeSheetResourceCheckinDTO.setTotalHours(((Long)row[11]).intValue());
			timeSheetResourceCheckinDTO.setTotalMins(((Long)row[12]).intValue());
		}
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return timeSheetResourceCheckinDTO;
	}
	
	@Override
	@Transactional
	public TimeSheetEntryMaster addTimeSheetEntry(TimeSheetEntryMaster timeSheetEntryMaster) {
		log.debug("adding ProductMaster instance");
		try {
			timeSheetEntryMaster.setIsApproved(0);
			timeSheetEntryMaster.setStatus(1);
			timeSheetEntryMaster.setCreatedDate(new Date(System.currentTimeMillis()));
			sessionFactory.getCurrentSession().save(timeSheetEntryMaster);
			log.debug("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}
		return timeSheetEntryMaster;
		
	}
	
	@Override
	@Transactional
	public void updateTimeSheetEntry(TimeSheetEntryMaster timeSheetEntryMaster) {
		log.info("updating Time sheet Entry instance");
		try {
			timeSheetEntryMaster.setStatus(1);
			timeSheetEntryMaster.setCreatedDate(timeSheetEntryMaster.getCreatedDate());
			timeSheetEntryMaster.setModifiedDate(new Date(System.currentTimeMillis()));
			sessionFactory.getCurrentSession().saveOrUpdate(timeSheetEntryMaster);
			log.info("updating successful");
		} catch (RuntimeException re) {
			log.error("updating failed", re);
		}
		
	}

	@Override
	@Transactional
	public int getTotalRecords(int userId) {
		log.debug("getting ProductMaster total records");
		int count =0;
		try {
			count=((Number) sessionFactory.getCurrentSession().createSQLQuery("select count(*) from time_sheet_entry_master where status=1 and userId=:userId").setParameter("userId", userId).uniqueResult()).intValue();
			log.debug("total records fetch successful");
		} catch (RuntimeException re) {
			log.error("total records fetch failed", re);
		}
		return count;
	
	}

	@Override
	@Transactional
	public List<TimeSheetDaySummaryDTO> listTimeSheetSummaryForWeek(int resourceId, Date startDate, Date endDate) {
		List<TimeSheetDaySummaryDTO> dailyTimeSheetSummary = new ArrayList<TimeSheetDaySummaryDTO>();
		try {
		Criteria c = sessionFactory.getCurrentSession().createCriteria(TimeSheetEntryMaster.class,"tsem");
		c.createAlias("tsem.shift", "workShift");
		c.createAlias("workShift.shiftType", "workShiftType");
		c.add(Restrictions.eq("tsem.user.userId", Integer.valueOf(resourceId)));
		c.add(Restrictions.between("tsem.date", startDate,  endDate));
		c.setProjection(Projections.projectionList()
									.add(Property.forName("tsem.user.userId").as("userId"))
									.add(Property.forName("tsem.date").as("timeSheetDate"))
									.add(Property.forName("workShift.shiftId").as("shiftId"))
									.add(Property.forName("workShiftType.shiftTypeId").as("shiftTypeId"))
									.add(Projections.sum("tsem.hours").as("totalHours"))
									.add(Projections.sum("tsem.mins").as("totalMins"))
									.add(Projections.groupProperty("tsem.date"))
									.add(Projections.groupProperty("workShift.shiftId"))
						)
		.addOrder(Order.asc("tsem.date"))
		.addOrder(Order.asc("workShift.shiftId"));
		
		List<Object[]> list = c.list();
		TimeSheetDaySummaryDTO timeSheetDaySummaryDTO = null;
		for (Object[] row : list) {
			timeSheetDaySummaryDTO = new TimeSheetDaySummaryDTO();
			timeSheetDaySummaryDTO.setUserId((Integer)row[0]);
			timeSheetDaySummaryDTO.setTimeSheetEntryDate((Date)row[1]);
			timeSheetDaySummaryDTO.setShiftId((Integer)(row[2]));
			timeSheetDaySummaryDTO.setShiftTypeId((Integer)(row[3]));
			timeSheetDaySummaryDTO.setTotalHours(((Long)row[4]).intValue());
			timeSheetDaySummaryDTO.setTotalMins(((Long)row[5]).intValue());
			dailyTimeSheetSummary.add(timeSheetDaySummaryDTO);
			
			log.info("Daily Shift Plan for user Id : " + (Integer)row[0] + "Date : " + (Date)row[1]+ " shift Id: " + (Integer)(row[2])+ " shift Type Id: " + (Integer)(row[3])+" Hours:   "+((Long)row[4]).intValue()+"   Mins: "+((Long)row[5]).intValue());
		}
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return dailyTimeSheetSummary;
	}

	@Override
	@Transactional
	public List<TimeSheetEntryMaster> getTimeSheetEntriesOfWorkPackageForApproval(
			int workPackageId) {
		log.info("listing all Time Sheet Entries for workPackageId id: "+workPackageId);
		List<TimeSheetEntryMaster> listOfTimeSheetEntries = null;
		try {
			listOfTimeSheetEntries = sessionFactory.getCurrentSession().createQuery("from TimeSheetEntryMaster tem where tem.status='1' and isApproved=0 and tem.workPackage.workPackageId=:workPackageId").setParameter("workPackageId", workPackageId).list();
			log.info("listOfTimeSheetEntries: "+listOfTimeSheetEntries.size());
			if (!(listOfTimeSheetEntries == null || listOfTimeSheetEntries.isEmpty())){
				for (TimeSheetEntryMaster timeSheetEntry : listOfTimeSheetEntries) {
					Hibernate.initialize(timeSheetEntry.getRole());
					Hibernate.initialize(timeSheetEntry.getProduct());
					Hibernate.initialize(timeSheetEntry.getWorkPackage());
					Hibernate.initialize(timeSheetEntry.getApprover());
					Hibernate.initialize(timeSheetEntry.getShift());
					Hibernate.initialize(timeSheetEntry.getUser());
					Hibernate.initialize(timeSheetEntry.getActivityType());
					Hibernate.initialize(timeSheetEntry.getResourceShiftCheckIn());
				}
			}
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return listOfTimeSheetEntries;
	}

	@Override
	@Transactional
	public List<TimeSheetEntryMaster> getTimeSheetEntriesOfWorkPackageBasedonStatus(
			int workPackageId, int statusID) {
		log.info("listing all Time Sheet Entries for workPackageId id: "+workPackageId+ ", for status --"+statusID);
		List<TimeSheetEntryMaster> listOfTimeSheetEntries = null;
		try {
			if(statusID==0){//Unapproved
				listOfTimeSheetEntries = sessionFactory.getCurrentSession().createQuery("from TimeSheetEntryMaster tem where tem.status='1' and isApproved=0 and tem.workPackage.workPackageId=:workPackageId").setParameter("workPackageId", workPackageId).list();				
			}else if(statusID == 1){//Approved
				listOfTimeSheetEntries = sessionFactory.getCurrentSession().createQuery("from TimeSheetEntryMaster tem where tem.status='1' and isApproved=1 and tem.workPackage.workPackageId=:workPackageId").setParameter("workPackageId", workPackageId).list();				
			}else if(statusID == 2){//All
				listOfTimeSheetEntries = sessionFactory.getCurrentSession().createQuery("from TimeSheetEntryMaster tem where tem.status='1' and tem.workPackage.workPackageId=:workPackageId").setParameter("workPackageId", workPackageId).list();				
			}
			log.info("listOfTimeSheetEntries: "+listOfTimeSheetEntries.size());
			if (!(listOfTimeSheetEntries == null || listOfTimeSheetEntries.isEmpty())){
				for (TimeSheetEntryMaster timeSheetEntry : listOfTimeSheetEntries) {
					Hibernate.initialize(timeSheetEntry.getRole());
					Hibernate.initialize(timeSheetEntry.getProduct());
					Hibernate.initialize(timeSheetEntry.getWorkPackage());
					Hibernate.initialize(timeSheetEntry.getApprover());
					Hibernate.initialize(timeSheetEntry.getShift());
					Hibernate.initialize(timeSheetEntry.getUser());
					Hibernate.initialize(timeSheetEntry.getActivityType());
					Hibernate.initialize(timeSheetEntry.getResourceShiftCheckIn().getProductMaster());
				}
			}
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return listOfTimeSheetEntries;
	}
	
	
	
	@Override
	@Transactional
	public void updateAndApproveTimeSheetEntry(TimeSheetEntryMaster timeSheetEntryMaster) {
		log.debug("Approve Time sheet Entry");
		try {
			timeSheetEntryMaster.setModifiedDate(new Date(System.currentTimeMillis()));
			sessionFactory.getCurrentSession().saveOrUpdate(timeSheetEntryMaster);
			log.debug("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}
	}

	@Override
	@Transactional
	public TimeSheetEntryMaster getTimeSheetEntryById(int timeSheetEntryId) {
		log.info("Get details of time sheet entry :"+timeSheetEntryId);
		TimeSheetEntryMaster timeSheetEntry = null;
		try {
			List list =  sessionFactory.getCurrentSession().createQuery("from TimeSheetEntryMaster tem where tem.timeSheetEntryId=:timeSheetEntryId").setParameter("timeSheetEntryId", timeSheetEntryId).list();
			timeSheetEntry = (list!=null && list.size()!=0)?(TimeSheetEntryMaster)list.get(0):null;
			if (timeSheetEntry != null) {
				Hibernate.initialize(timeSheetEntry.getRole());
				Hibernate.initialize(timeSheetEntry.getProduct());
				Hibernate.initialize(timeSheetEntry.getWorkPackage());
				Hibernate.initialize(timeSheetEntry.getApprover());
				Hibernate.initialize(timeSheetEntry.getShift());
				Hibernate.initialize(timeSheetEntry.getUser());
			}
		}catch (RuntimeException re) {
			log.error("getByProductId failed", re);
		}
		return timeSheetEntry;
		
	}
	
	@Override
	@Transactional
	public List<TimeSheetActivityType> listGenericTimeSheetActivityTypes() {
		log.info("listing listTimeSheetActivityTypes");
		List<TimeSheetActivityType> listOfTimeSheetActivityType = null;
		List<TimeSheetActivityType> listOfGenericTimeSheetActivityType = new ArrayList<TimeSheetActivityType>();
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TimeSheetActivityType.class,"activityType");
			listOfTimeSheetActivityType = c.list();
			if (!(listOfTimeSheetActivityType == null || listOfTimeSheetActivityType.isEmpty())){
				for (TimeSheetActivityType timeSheetActivityType : listOfTimeSheetActivityType) {
					if(timeSheetActivityType.getProduct() == null){
						listOfGenericTimeSheetActivityType.add(timeSheetActivityType);
					}
				}
			}
		} catch (Exception e) {
			log.error("list failed", e);
		}
		return listOfGenericTimeSheetActivityType;
	}

	@Override
	public TimeSheetActivityType getTimeSheetActivityTypeById(int activityTypeId) {
		log.info("listing getTimeSheetActivityTypeById");
		List<TimeSheetActivityType> listOfTimeSheetActivityType = null;
		TimeSheetActivityType timeSheetActivityType = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TimeSheetActivityType.class,"activityType");
			c.add(Restrictions.eq("activityType.activityTypeId", Integer.valueOf(activityTypeId)));
			listOfTimeSheetActivityType = c.list();
			if (!(listOfTimeSheetActivityType == null || listOfTimeSheetActivityType.isEmpty())){
				timeSheetActivityType = (listOfTimeSheetActivityType != null && listOfTimeSheetActivityType.size() != 0)?(TimeSheetActivityType)listOfTimeSheetActivityType.get(0):null;
				
				if(timeSheetActivityType != null ){
					Hibernate.initialize(timeSheetActivityType.getProduct());
				}
			}
		} catch (Exception e) {
			log.error("list failed", e);
		}
		return timeSheetActivityType;
	}
	
	
	@Override
	public List<TimeSheetDaySummaryDTO>  getTotalWorkedHoursbyDatenShift(UserList user, Date currentDate, int timeSheetEntryId, int shiftId){
		TimeSheetEntryMaster timeSheetEntry = null;		
		HashMap<String,Integer> timeMap =  new HashMap<String,Integer>();
		List<TimeSheetDaySummaryDTO> timeSheetDSDTOList = new ArrayList<TimeSheetDaySummaryDTO>();
		try{
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TimeSheetEntryMaster.class,"tsem");
			c.createAlias("tsem.user", "userInfo");
			c.createAlias("tsem.shift", "shift");
			c.add(Restrictions.eq("userInfo.userId",user.getUserId()));
			if(timeSheetEntryId  !=0){ // For update functionality to exclude current Timesheet Entry hours
				c.add(Restrictions.ne("tsem.timeSheetEntryId",timeSheetEntryId));
			}
			if(shiftId != 0){
				c.add(Restrictions.eq("shift.shiftId",shiftId));
			}
			if (currentDate == null){
				currentDate = DateUtility.getCurrentDate();
			}
			c.add(Restrictions.eq("tsem.date", currentDate));
			
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.sum("tsem.hours").as("totalHours"));//2
			projectionList.add(Projections.sum("tsem.mins").as("totalMinutes"));//3
			c.setProjection(projectionList);
			List<Object[]> list = c.list();
			if (list==null || list.size()==0)
				return null;		
			
			TimeSheetDaySummaryDTO timeSheetDSDTO = null;
			for (Object[] row : list) {
			if(row[0] != null){
				timeSheetDSDTO = new TimeSheetDaySummaryDTO();		
			
				Integer longvalueTotalHours = ((Long)row[0]).intValue();
				if(longvalueTotalHours!=null){
					timeSheetDSDTO.setTotalHours(Integer.valueOf(longvalueTotalHours));
				}else{
					timeSheetDSDTO.setTotalHours(0);
				}	
				Integer longvalueTotalMins = ((Long)row[1]).intValue();
				if(longvalueTotalMins!=null){
					timeSheetDSDTO.setTotalMins(Integer.valueOf(longvalueTotalMins));	
				}else{
					timeSheetDSDTO.setTotalMins(0);
				}				
							
				timeSheetDSDTOList.add(timeSheetDSDTO);	
			}else{
			}
							
			}
					
		
		}catch (RuntimeException re) {
			log.error("getTotalWorkedHoursbyDate failed", re);
		}
		return timeSheetDSDTOList;
	}
	
	@Override
	public List<TimeSheetActivityType> listProductSpecificTimeSheetActivityTypes(
			int productId) {
		log.info("listing listProductSpecificTimeSheetActivityTypes");
		List<TimeSheetActivityType> listOfTimeSheetActivityType = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TimeSheetActivityType.class,"activityType");
			c.createAlias("activityType.product", "product");
			c.add(Restrictions.eq("product.productId",new Integer(productId)));
			listOfTimeSheetActivityType = c.list();
			if (!(listOfTimeSheetActivityType == null || listOfTimeSheetActivityType.isEmpty())){
				for (TimeSheetActivityType timeSheetActivityType : listOfTimeSheetActivityType) {
					Hibernate.initialize(timeSheetActivityType.getProduct());
				}
			}
		} catch (Exception e) {
			log.error("list failed", e);
		}
		return listOfTimeSheetActivityType;
	}

	// This gives hours worked on workPackageId on a shiftId on a specified executionDate
	@Override
	@Transactional
	public HashMap<String,Integer> getTimeSheetEntryForWorkPackageStatisticsReportEOD(Integer workPackageId, Integer shiftId, Date executionDate, Integer testerId) {
		log.info("inside getTimeSheetEntryForWorkPackageReportEOD method");
		log.info("Filter Parameters : workPackageId=" + workPackageId + " shiftId=" + shiftId + " executionDate=" + executionDate + " testerId=" + testerId);
		HashMap<String,Integer> timeMap =  new HashMap<String,Integer>();
		try {
			
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TimeSheetEntryMaster.class,"tsem");

			c.createAlias("tsem.workPackage", "wpackage");
			c.createAlias("tsem.shift", "shiftInfo");
			c.createAlias("tsem.user", "userInfo");
			
			c.add(Restrictions.eq("tsem.status", 1));
			c.add(Restrictions.eq("tsem.isApproved", 1));
			c.add(Restrictions.eq("wpackage.workPackageId", workPackageId));
			c.add(Restrictions.eq("shiftInfo.shiftId", shiftId));
			
			if (executionDate == null){
				executionDate = DateUtility.getCurrentDate();
			}	
			c.add(Restrictions.eq("tsem.date", executionDate));
			c.add(Restrictions.eq("userInfo.userId", testerId));
			
			log.info("executionDate="+executionDate);
			
			ProjectionList projectionList = Projections.projectionList();
			
			projectionList.add(Projections.groupProperty("wpackage.workPackageId").as("workPackageId"));
			projectionList.add(Projections.sum("tsem.hours").as("totalHours"));
			projectionList.add(Projections.sum("tsem.mins").as("totalMinutes"));
																			   
			c.setProjection(projectionList);

			List<Object[]> list = c.list();
	
			if (list==null || list.size()==0)
				return null;
			Object[] row = list.get(0);
			log.info(row[1] + ":"+ row[2]);
			if (row[1] == null)
				timeMap.put("totalHours", null);
			else
				timeMap.put("totalHours", Integer.valueOf(((Long)row[1]).intValue()));
			
			if (row[2] == null)
				timeMap.put("totalMinutes", null);
			else
				timeMap.put("totalMinutes", Integer.valueOf(((Long)row[2]).intValue()));

		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return timeMap;
	}

	//This gives hours worked by a testerId on a specified executionDate. This doesn't group data workPackageId wise or shiftId wise
	@Override
	@Transactional
	public HashMap<String,Integer> getTimeSheetEntryForADate(Date executionDate, Integer testerId) {
		log.info("inside getTimeSheetEntryForADate method");
		log.info("Filter Parameters : executionDate=" + executionDate + " testerId=" + testerId);
		HashMap<String,Integer> timeMap =  new HashMap<String,Integer>();
		try {
			
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TimeSheetEntryMaster.class,"tsem");
			c.createAlias("tsem.user", "userInfo");
			
			if (executionDate == null){
				executionDate = DateUtility.getCurrentDate();
			}	
			
			c.add(Restrictions.eq("tsem.status", 1));
			c.add(Restrictions.eq("tsem.isApproved", 1));
			c.add(Restrictions.eq("tsem.date", executionDate));
			c.add(Restrictions.eq("userInfo.userId", testerId));
			
			ProjectionList projectionList = Projections.projectionList();
			
			projectionList.add(Projections.groupProperty("userInfo.userId").as("testerId"));
			projectionList.add(Projections.sum("tsem.hours").as("totalHours"));
			projectionList.add(Projections.sum("tsem.mins").as("totalMinutes"));
																			   
			c.setProjection(projectionList);

			List<Object[]> list = c.list();
	
			if (list==null || list.size()==0)
				return null;
			Object[] row = list.get(0);
			log.info(row[1] + ":"+ row[2]);
			if (row[1] == null)
				timeMap.put("totalHours", null);
			else
				timeMap.put("totalHours", Integer.valueOf(((Long)row[1]).intValue()));
			
			if (row[2] == null)
				timeMap.put("totalMinutes", null);
			else
				timeMap.put("totalMinutes", Integer.valueOf(((Long)row[2]).intValue()));
		} catch (RuntimeException re) {
			log.error("list failed", re);
			// throw re;
		}
		return timeMap;
	}

	//This gives hours worked by a testerId from week start date until a specified date. This doesn't group data workPackageId wise or shiftId wise
	@Override
	@Transactional
	public HashMap<String,Integer> getTimeSheetEntryForADateFromWeekStart(Date executionDate, Integer testerId) {
		log.info("inside getTimeSheetEntryForADateFromWeekStart(Date executionDate, Integer testerId) method");
		log.info("Filter Parameters : executionDate=" + executionDate + " testerId=" + testerId);
		HashMap<String,Integer> timeMap =  new HashMap<String,Integer>();
		try {
			
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TimeSheetEntryMaster.class,"tsem");
			c.createAlias("tsem.user", "userInfo");
			
			if (executionDate == null){
				return null;
			}	
			Date weekStartDate = DateUtility.getWeekStart(executionDate);
			
			log.info("weekStartDate="+weekStartDate);
			
			c.add(Restrictions.eq("tsem.status", 1));
			c.add(Restrictions.eq("tsem.isApproved", 1));
			c.add(Restrictions.between("tsem.date", weekStartDate,  executionDate));
			c.add(Restrictions.eq("userInfo.userId", testerId));
			
			ProjectionList projectionList = Projections.projectionList();
			
			projectionList.add(Projections.groupProperty("userInfo.userId").as("testerId"));
			projectionList.add(Projections.sum("tsem.hours").as("totalHours"));
			projectionList.add(Projections.sum("tsem.mins").as("totalMinutes"));
																			   
			c.setProjection(projectionList);

			List<Object[]> list = c.list();
	
			if (list==null || list.size()==0)
				return null;
			Object[] row = list.get(0);
			log.info(row[1] + ":"+ row[2]);
			if (row[1] == null)
				timeMap.put("totalHours", null);
			else
				timeMap.put("totalHours", Integer.valueOf(((Long)row[1]).intValue()));
			
			if (row[2] == null)
				timeMap.put("totalMinutes", null);
			else
				timeMap.put("totalMinutes", Integer.valueOf(((Long)row[2]).intValue()));

		} catch (RuntimeException re) {
			log.error("list failed", re);
			// throw re;
		}
		return timeMap;
	}
	

	//This gives hours worked on a productId from week start date until a specified date. This doesn't group data workPackageId wise or shiftId wise
	@Override
	@Transactional
	public HashMap<String,Integer> getTimeSheetEntryForADateFromWeekStartOnAProduct(Date executionDate, Integer testerId, Integer productId) {
		log.info("inside getTimeSheetEntryForADateFromWeekStartOnAProduct method");
		log.info("Filter Parameters : executionDate=" + executionDate + " testerId=" + testerId);
		HashMap<String,Integer> timeMap =  new HashMap<String,Integer>();
		try {
			
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TimeSheetEntryMaster.class,"tsem");
			c.createAlias("tsem.user", "userInfo");
			
			c.createAlias("tsem.workPackage", "wpackage");
			c.createAlias("wpackage.productBuild", "productBuild");
			c.createAlias("productBuild.productVersion", "productVersion");
			c.createAlias("productVersion.productMaster", "productMaster");
			
			if (executionDate == null){
				return null;
			}	
			Date weekStartDate = DateUtility.getWeekStart(executionDate);
			
			log.info("weekStartDate="+weekStartDate);
			
			c.add(Restrictions.eq("tsem.status", 1));
			c.add(Restrictions.eq("tsem.isApproved", 1));
			c.add(Restrictions.between("tsem.date", weekStartDate,  executionDate));
			c.add(Restrictions.eq("userInfo.userId", testerId));
			c.add(Restrictions.eq("productMaster.productId", productId));
			
			ProjectionList projectionList = Projections.projectionList();
			
			projectionList.add(Projections.groupProperty("userInfo.userId").as("testerId"));
			projectionList.add(Projections.sum("tsem.hours").as("totalHours"));
			projectionList.add(Projections.sum("tsem.mins").as("totalMinutes"));
																			   
			c.setProjection(projectionList);

			List<Object[]> list = c.list();
	
			if (list==null || list.size()==0)
				return null;
			Object[] row = list.get(0);
			log.info(row[1] + ":"+ row[2]);
			if (row[1] == null)
				timeMap.put("totalHours", null);
			else
				timeMap.put("totalHours", Integer.valueOf(((Long)row[1]).intValue()));
			
			if (row[2] == null)
				timeMap.put("totalMinutes", null);
			else
				timeMap.put("totalMinutes", Integer.valueOf(((Long)row[2]).intValue()));

		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return timeMap;
	}


	@Override
	@Transactional
	public HashMap<String,Integer> getTimeSheetEntryForLunchAndBreaks(Integer workPackageId, Integer shiftId, Date executionDate, 
			Integer testerId, Integer activityTypeId, Integer resourceShiftCheckInId, Integer userRoleId) {
		log.info("inside getTimeSheetEntryForLunchAndBreaks method");
		log.info("Filter Parameters : workPackageId=" + workPackageId + " shiftId=" + shiftId + " executionDate=" + executionDate +
				" testerId=" + testerId + " resourceShiftCheckInId=" + resourceShiftCheckInId+ " userRoleId=" + userRoleId);
		HashMap<String,Integer> timeMap =  new HashMap<String,Integer>();
		try {
			
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TimeSheetEntryMaster.class,"tsem");

			c.createAlias("tsem.workPackage", "wpackage");
			c.createAlias("tsem.shift", "shiftInfo");
			c.createAlias("tsem.user", "userInfo");
			c.createAlias("tsem.activityType", "activityType");
			
			c.createAlias("tsem.resourceShiftCheckIn", "resShftCheckIn");
			c.createAlias("tsem.role", "userRole");
			
			c.add(Restrictions.eq("tsem.status", 1));
			c.add(Restrictions.eq("tsem.isApproved", 1));
			c.add(Restrictions.eq("wpackage.workPackageId", workPackageId));
			c.add(Restrictions.eq("shiftInfo.shiftId", shiftId));
			c.add(Restrictions.eq("activityType.activityTypeId", activityTypeId));
			
			c.add(Restrictions.eq("resShftCheckIn.resourceShiftCheckInId", resourceShiftCheckInId));
			
			c.add(Restrictions.eq("userRole.userRoleId", userRoleId));
			
			if (executionDate == null){
				executionDate = DateUtility.getCurrentDate();
			}	
			c.add(Restrictions.eq("tsem.date", executionDate));
			c.add(Restrictions.eq("userInfo.userId", testerId));
			
			log.info("executionDate="+executionDate);
			
			ProjectionList projectionList = Projections.projectionList();
			
			projectionList.add(Projections.groupProperty("wpackage.workPackageId").as("workPackageId"));
			projectionList.add(Projections.sum("tsem.hours").as("totalHours"));
			projectionList.add(Projections.sum("tsem.mins").as("totalMinutes"));
																			   
			c.setProjection(projectionList);

			List<Object[]> list = c.list();
	
			if (list==null || list.size()==0)
				return null;
			Object[] row = list.get(0);
			log.info(row[1] + ":"+ row[2]);
			if (row[1] == null)
				timeMap.put("totalHours", null);
			else
				timeMap.put("totalHours", Integer.valueOf(((Long)row[1]).intValue()));
			
			if (row[2] == null)
				timeMap.put("totalMinutes", null);
			else
				timeMap.put("totalMinutes", Integer.valueOf(((Long)row[2]).intValue()));

		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return timeMap;
	}
	
	//This gives hours worked on: from week start date until a specified date. This doesn't group data workPackageId wise or shiftId wise. This prepares 
	//data groupwise testerId
	@Override
	@Transactional
	public HashMap<Integer,Integer> getTimeSheetEntryForADateFromWeekStart(Date executionDate) {
		log.info("inside getTimeSheetEntryForADateFromWeekStart method");
		log.info("Filter Parameters : executionDate=" + executionDate);
		HashMap<Integer,Integer> timeMapWeekUserWise =  new HashMap<Integer,Integer>();
		try {
			
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TimeSheetEntryMaster.class,"tsem");
			c.createAlias("tsem.user", "userInfo");
			
			if (executionDate == null){
				return null;
			}	
			Date weekStartDate = DateUtility.getWeekStart(executionDate);
			
			log.info("weekStartDate="+weekStartDate);
			
			c.add(Restrictions.eq("tsem.status", 1));
			c.add(Restrictions.eq("tsem.isApproved", 1));
			c.add(Restrictions.between("tsem.date", weekStartDate,  executionDate));
						
			ProjectionList projectionList = Projections.projectionList();
			
			projectionList.add(Projections.groupProperty("userInfo.userId").as("testerId"));
			projectionList.add(Projections.sum("tsem.hours").as("totalHours"));
			projectionList.add(Projections.sum("tsem.mins").as("totalMinutes"));
																			   
			c.setProjection(projectionList);

			List<Object[]> list = c.list();
	
			if (list==null || list.size()==0)
				return null;

			for (Object[] row : list) {
				Long longHrsWorked = (Long)row[1];
				Long longMinutesWorked = (Long)row[2];
				Integer convertTimeInMinutes = DateUtility.convertTimeInMinutes(Integer.valueOf(longHrsWorked.intValue()),Integer.valueOf(longMinutesWorked.intValue()));
				timeMapWeekUserWise.put((Integer)row[0],convertTimeInMinutes);
			}
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return timeMapWeekUserWise;
	}

	// This gets the records and gets role available in anyone of the records and sends the result
	@Override
	@Transactional
	public String getRoleOfUserOnWorkPackage(Integer workPackageId, Integer shiftId, Date executionDate, Integer testerId) {
		log.info("inside getRoleOfUser method");
		log.info("Filter Parameters : workPackageId=" + workPackageId + " shiftId=" + shiftId + " executionDate=" + executionDate + " testerId=" + testerId);
		List<TimeSheetEntryMaster> timeSheetEntryMasterList = new ArrayList<TimeSheetEntryMaster>();
		String userRoleOnWorkPackage  = null;
		try {
			
			if (executionDate == null || workPackageId == null || shiftId == null || testerId == null){
				return null;
			}
			
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TimeSheetEntryMaster.class,"tsem");

			c.createAlias("tsem.workPackage", "wpackage");
			c.createAlias("tsem.shift", "shiftInfo");
			c.createAlias("tsem.user", "userInfo");
			
			c.add(Restrictions.eq("tsem.status", 1));
			c.add(Restrictions.eq("tsem.date", executionDate));
			c.add(Restrictions.eq("wpackage.workPackageId", workPackageId));
			c.add(Restrictions.eq("shiftInfo.shiftId", shiftId));
			c.add(Restrictions.eq("userInfo.userId", testerId));
			
			List<Object[]> list = c.list();

			timeSheetEntryMasterList = c.list();
			log.info("getRoleOfUser timeSheetEntryMasterList size : " + timeSheetEntryMasterList.size());

			if (!timeSheetEntryMasterList.isEmpty()) {
				for (TimeSheetEntryMaster timeSheetEntryMaster : timeSheetEntryMasterList) {
					if (timeSheetEntryMaster.getRole() != null){
						Hibernate.initialize(timeSheetEntryMaster.getRole());
						userRoleOnWorkPackage = timeSheetEntryMaster.getRole().getRoleName();
						if (userRoleOnWorkPackage != null);
							return userRoleOnWorkPackage;
					}
				}
			}
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return userRoleOnWorkPackage;
	}
	
	@Override
	@Transactional
    public List<TimeSheetDaySummaryDTO> mandatoryHoursofShifts(int shiftId){
		List<WorkShiftMaster> workShiftsList = null;
		List<TimeSheetDaySummaryDTO> timeSheetDSDTOlist = new ArrayList<TimeSheetDaySummaryDTO>();
		Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkShiftMaster.class, "ws");
		c.add(Restrictions.eq("ws.shiftId", shiftId));	
		
		workShiftsList = c.list();
		
		log.info("workShiftsList size "+workShiftsList.size());
		Date startTime = null;
		Date endTime = null;
		if(workShiftsList != null && workShiftsList.size()>0){
			for (WorkShiftMaster workShiftMaster : workShiftsList) {
				startTime = workShiftMaster.getStartTime();
				endTime= workShiftMaster.getEndTime();
			}
		}
		
		log.info("startTime---"+startTime+", endTime--"+endTime);        

	        long diff = endTime.getTime() - startTime.getTime();
	        long diffSeconds = diff / 1000 % 60;
	        long diffMinutes = diff / (60 * 1000) % 60;
	        long diffHours = diff / (60 * 60 * 1000);
	        int diffInDays = (int) ((endTime.getTime() - startTime.getTime()) / (1000 * 60 * 60 * 24));

	        if (diffInDays > 1) {
	           
	        } else if (diffHours > 24) {
	    
	           
	        } else if ((diffHours == 24) && (diffMinutes >= 1)) {
	           
	        }
		
	       TimeSheetDaySummaryDTO timeSheetDSDTO = new TimeSheetDaySummaryDTO();
	       timeSheetDSDTO.setTotalHours((int)diffHours);
	       timeSheetDSDTO.setTotalMins((int)diffMinutes);
	       timeSheetDSDTO.setShiftId(shiftId);
	       timeSheetDSDTOlist.add(timeSheetDSDTO);
		
    	return timeSheetDSDTOlist;
    }
	
	@Override
	@Transactional
    public List<TimeSheetDaySummaryDTO> mandatoryHoursofActualShift(int shiftId, Date currentDate){
		List<ActualShift> workShiftsList = null;
		List<TimeSheetDaySummaryDTO> timeSheetDSDTOlist = new ArrayList<TimeSheetDaySummaryDTO>();
		Criteria c = sessionFactory.getCurrentSession().createCriteria(ActualShift.class, "as");
		c.createAlias("as.shift", "shift");
		
		c.add(Restrictions.eq("shift.shiftId", shiftId));	
		
		if (currentDate == null){
			currentDate = DateUtility.getCurrentDate();
		}
		c.add(Restrictions.eq("as.workdate", currentDate));
		
		workShiftsList = c.list();
		
		log.info("workShiftsList size "+workShiftsList.size());
		Date startTime = null;
		Date endTime = null;
		if(workShiftsList != null && workShiftsList.size()>0){
			for (ActualShift workShiftMaster : workShiftsList) {
				startTime = workShiftMaster.getStartTime();
				endTime= workShiftMaster.getEndTime();
				log.info("Work date "+workShiftMaster.getWorkdate());
			}
		}
		
		log.info("startTime---"+startTime+", endTime--"+endTime);        
		if(startTime != null & endTime != null){
			long diff = endTime.getTime() - startTime.getTime();
	        long diffSeconds = diff / 1000 % 60;
	        long diffMinutes = diff / (60 * 1000) % 60;
	        long diffHours = diff / (60 * 60 * 1000);
	        int diffInDays = (int) ((endTime.getTime() - startTime.getTime()) / (1000 * 60 * 60 * 24));

	        if (diffInDays > 1) {
	        } else if (diffHours > 24) {
	        } else if ((diffHours == 24) && (diffMinutes >= 1)) {
	        }
		
	       TimeSheetDaySummaryDTO timeSheetDSDTO = new TimeSheetDaySummaryDTO();
	       timeSheetDSDTO.setTotalHours((int)diffHours);
	       timeSheetDSDTO.setTotalMins((int)diffMinutes);
	       timeSheetDSDTO.setShiftId(shiftId);
	       timeSheetDSDTOlist.add(timeSheetDSDTO);
		}
	        
		
    	return timeSheetDSDTOlist;
    }
	
	@Override
	@Transactional
	public List<UserWeekUtilisedTimeDTO> getTimesheetEntriesForWorkPackage(int workPackgeId, WorkShiftMaster shift, Date blockResourceForDate) {
			log.info("inside getTimesheetEntriesForWorkPackage");
			List<UserWeekUtilisedTimeDTO> userWeekTimeBookingCompleteList = new ArrayList<UserWeekUtilisedTimeDTO>();
			List<UserWeekUtilisedTimeDTO> userWeekTimeBookingList = new ArrayList<UserWeekUtilisedTimeDTO>();
			List<UserWeekUtilisedTimeDTO> userPreviousShiftTimeBookingList = new ArrayList<UserWeekUtilisedTimeDTO>();
			log.info("Getting time sheet booking for workPackgeId: " + workPackgeId + " : " + shift.getShiftId() + " : " + blockResourceForDate);
			Date startDate = null;
			Date endDate = null;
			try {
				int weekDayNumber = DateUtility.getDayOfWeek(blockResourceForDate);
				String  day = DateUtility.getDayNameForDayNumber(weekDayNumber);
				if(day.equalsIgnoreCase("Monday")){
					// do nothing
				}else if(day.equalsIgnoreCase("Tuesday") || day.equalsIgnoreCase("Wednesday") || day.equalsIgnoreCase("Thursday") || day.equalsIgnoreCase("Friday")){
					startDate = DateUtility.getWeekStart(blockResourceForDate);
					endDate = DateUtility.getPrevDate(blockResourceForDate);
				}else if(day.equalsIgnoreCase("Saturday")){
					startDate = DateUtility.getWeekStart(blockResourceForDate);
					endDate = DateUtility.getAnyPrevDate(blockResourceForDate, -1);
				}else if(day.equalsIgnoreCase("Sunday")){
					startDate = DateUtility.getWeekStart(blockResourceForDate);
					endDate = DateUtility.getAnyPrevDate(blockResourceForDate, -2);
					
				}
			
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TimeSheetEntryMaster.class,"tsem");
			c.createAlias("tsem.shift", "workShift");
			c.add(Restrictions.between("tsem.date", startDate,  endDate));
			c.setProjection(Projections.projectionList()
										.add(Property.forName("tsem.user.userId").as("userId"))
										.add(Property.forName("tsem.date").as("timeSheetDate"))
										.add(Property.forName("workShift.shiftId").as("shiftId"))
										.add(Projections.sum("tsem.hours").as("totalHours"))
										.add(Projections.sum("tsem.mins").as("totalMins"))
										.add(Projections.groupProperty("tsem.user.userId"))
							)
			.addOrder(Order.asc("tsem.date"))
			.addOrder(Order.asc("workShift.shiftId"));
			
			List<Object[]> list = c.list();
			UserWeekUtilisedTimeDTO userWeekUtilisedTimeDTO = null;
			for (Object[] row : list) {
				userWeekUtilisedTimeDTO = new UserWeekUtilisedTimeDTO();
				userWeekUtilisedTimeDTO.setUserId((Integer)row[0]);
				userWeekUtilisedTimeDTO.setStartDate((Date)row[1]);
				userWeekUtilisedTimeDTO.setShiftId((Integer)(row[2]));
				userWeekUtilisedTimeDTO.setTimeSheetTotalHours(((Long)row[3]).intValue());
				userWeekUtilisedTimeDTO.setTimeSheetTotalMins(((Long)row[4]).intValue());
				userWeekTimeBookingList.add(userWeekUtilisedTimeDTO);
			}
			
			if(shift.getShiftType() != null){
				// Do nothing if shift Type is morning
				int shiftTypeId = shift.getShiftType().getShiftTypeId();
				if(shiftTypeId != 1){
					Criteria c1 = sessionFactory.getCurrentSession().createCriteria(TimeSheetEntryMaster.class,"tsem");
					c1.createAlias("tsem.shift", "workShift");
					c1.createAlias("workShift.shiftType", "shiftType");
					c1.add(Restrictions.eq("tsem.date", blockResourceForDate));
					if(shiftTypeId == 2){
						c1.add(Restrictions.in("shiftType.shiftTypeId",  Arrays.asList(1)));
					}else if(shiftTypeId == 3){
						c1.add(Restrictions.in("shiftType.shiftTypeId",  Arrays.asList(1,2)));
					}
					c1.setProjection(Projections.projectionList()
						.add(Property.forName("tsem.user.userId").as("userId"))
						.add(Property.forName("tsem.date").as("timeSheetDate"))
						.add(Property.forName("workShift.shiftId").as("shiftId"))
						.add(Projections.sum("tsem.hours").as("totalHours"))
						.add(Projections.sum("tsem.mins").as("totalMins"))
						.add(Projections.groupProperty("tsem.user.userId"))
					);
					List<Object[]> timeEntriesOfPreviousShifts = c1.list();
					log.info("Daily shift plans count : " + list.size());
					UserWeekUtilisedTimeDTO usertimeEntriesOfPreviousShifts = null;
					for (Object[] row : timeEntriesOfPreviousShifts) {
						usertimeEntriesOfPreviousShifts = new UserWeekUtilisedTimeDTO();
						usertimeEntriesOfPreviousShifts.setUserId((Integer)row[0]);
						usertimeEntriesOfPreviousShifts.setStartDate((Date)row[1]);
						usertimeEntriesOfPreviousShifts.setShiftId((Integer)(row[2]));
						usertimeEntriesOfPreviousShifts.setTimeSheetTotalHours(((Long)row[3]).intValue());
						usertimeEntriesOfPreviousShifts.setTimeSheetTotalMins(((Long)row[4]).intValue());
						userPreviousShiftTimeBookingList.add(usertimeEntriesOfPreviousShifts);
					}
				}
			}
			
			
			if(userPreviousShiftTimeBookingList == null || userPreviousShiftTimeBookingList.size() == 0 && userWeekTimeBookingList != null && userWeekTimeBookingList.size()>0){
				userWeekTimeBookingCompleteList.addAll(userWeekTimeBookingList);
			}else if(userWeekTimeBookingList == null || userWeekTimeBookingList.size() == 0 && userPreviousShiftTimeBookingList != null && userPreviousShiftTimeBookingList.size()>0){
				userWeekTimeBookingCompleteList.addAll(userPreviousShiftTimeBookingList);
			}
			if(userWeekTimeBookingList != null && userWeekTimeBookingList.size()>0 && userPreviousShiftTimeBookingList != null && userPreviousShiftTimeBookingList.size()>0){
				userWeekTimeBookingCompleteList.addAll(userWeekTimeBookingList);
				int index = -1;
				for (UserWeekUtilisedTimeDTO tillPreviousDayTimeEntry : userWeekTimeBookingCompleteList) {
					index = userPreviousShiftTimeBookingList.indexOf(tillPreviousDayTimeEntry);
					if (index >= 0) {
						UserWeekUtilisedTimeDTO tillPreviousShiftTimeEntry = userPreviousShiftTimeBookingList.get(index);
						tillPreviousDayTimeEntry.setTimeSheetTotalHours(tillPreviousDayTimeEntry.getTimeSheetTotalHours()+tillPreviousShiftTimeEntry.getTimeSheetTotalHours());
						tillPreviousDayTimeEntry.setTimeSheetTotalMins(tillPreviousDayTimeEntry.getTimeSheetTotalMins()+tillPreviousShiftTimeEntry.getTimeSheetTotalMins());
					}
				}
			}
			
			} catch (RuntimeException re) {
				log.error("list failed", re);
			}
			return userWeekTimeBookingCompleteList;
		}

	@Override
	@Transactional
	public List<UserWeekUtilisedTimeDTO> getTimesheetEntriesForUser(int workPackgeId, int shiftTypeId, Date blockResourceForDate, int userId) {
			List<UserWeekUtilisedTimeDTO> userWeekTimeBookingCompleteList = new ArrayList<UserWeekUtilisedTimeDTO>();
			List<UserWeekUtilisedTimeDTO> userWeekTimeBookingList = new ArrayList<UserWeekUtilisedTimeDTO>();
			List<UserWeekUtilisedTimeDTO> userPreviousShiftTimeBookingList = new ArrayList<UserWeekUtilisedTimeDTO>();
			Date startDate = null;
			Date endDate = null;
			try {
				int weekDayNumber = DateUtility.getDayOfWeek(blockResourceForDate);
				String  day = DateUtility.getDayNameForDayNumber(weekDayNumber);
				if(day.equalsIgnoreCase("Monday")){
				}else if(day.equalsIgnoreCase("Tuesday") || day.equalsIgnoreCase("Wednesday") || day.equalsIgnoreCase("Thursday") || day.equalsIgnoreCase("Friday")){
					startDate = DateUtility.getWeekStart(blockResourceForDate);
					endDate = DateUtility.getPrevDate(blockResourceForDate);
				}else if(day.equalsIgnoreCase("Saturday")){
					startDate = DateUtility.getWeekStart(blockResourceForDate);
					endDate = DateUtility.getAnyPrevDate(blockResourceForDate, -1);
				}else if(day.equalsIgnoreCase("Sunday")){
					startDate = DateUtility.getWeekStart(blockResourceForDate);
					endDate = DateUtility.getAnyPrevDate(blockResourceForDate, -2);
				}
			
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TimeSheetEntryMaster.class,"tsem");
			c.createAlias("tsem.shift", "workShift");
			c.add(Restrictions.between("tsem.date", startDate,  endDate));
			c.add(Restrictions.eq("tsem.user.userId", userId));
			c.add(Restrictions.eq("workShift.shiftType.shiftTypeId", shiftTypeId));
			c.setProjection(Projections.projectionList()
										.add(Property.forName("tsem.user.userId").as("userId"))
										.add(Property.forName("tsem.date").as("timeSheetDate"))
										.add(Property.forName("workShift.shiftId").as("shiftId"))
										.add(Projections.sum("tsem.hours").as("totalHours"))
										.add(Projections.sum("tsem.mins").as("totalMins"))
							);
			
			List<Object[]> list = c.list();
			UserWeekUtilisedTimeDTO userWeekUtilisedTimeDTO = null;
			for (Object[] row : list) {
				userWeekUtilisedTimeDTO = new UserWeekUtilisedTimeDTO();
				userWeekUtilisedTimeDTO.setUserId((Integer)row[0]);
				userWeekUtilisedTimeDTO.setStartDate((Date)row[1]);
				userWeekUtilisedTimeDTO.setShiftId((Integer)(row[2]));
				if(row[3] != null){//To avoid Null Pointer Exception
					userWeekUtilisedTimeDTO.setTimeSheetTotalHours(((Long)row[3]).intValue());
					if(row[4] != null){
						userWeekUtilisedTimeDTO.setTimeSheetTotalMins(((Long)row[4]).intValue());
						userWeekTimeBookingList.add(userWeekUtilisedTimeDTO);
					}
				}
			}			
			if(shiftTypeId != 0){
				// Do nothing if shift Type is morning				
				if(shiftTypeId != 1){
					Criteria c1 = sessionFactory.getCurrentSession().createCriteria(TimeSheetEntryMaster.class,"tsem");
					c1.createAlias("tsem.shift", "workShift");
					c1.createAlias("workShift.shiftType", "shiftType");
					c1.add(Restrictions.eq("tsem.date", blockResourceForDate));
					if(shiftTypeId == 2){
						c1.add(Restrictions.in("shiftType.shiftTypeId",  Arrays.asList(1)));
					}else if(shiftTypeId == 3){
						c1.add(Restrictions.in("shiftType.shiftTypeId",  Arrays.asList(1,2)));
					}
					c1.setProjection(Projections.projectionList()
						.add(Property.forName("tsem.user.userId").as("userId"))
						.add(Property.forName("tsem.date").as("timeSheetDate"))
						.add(Property.forName("workShift.shiftId").as("shiftId"))
						.add(Projections.sum("tsem.hours").as("totalHours"))
						.add(Projections.sum("tsem.mins").as("totalMins"))
						.add(Projections.groupProperty("tsem.user.userId"))
					);
					List<Object[]> timeEntriesOfPreviousShifts = c1.list();
					UserWeekUtilisedTimeDTO usertimeEntriesOfPreviousShifts = null;
					for (Object[] row : timeEntriesOfPreviousShifts) {
						usertimeEntriesOfPreviousShifts = new UserWeekUtilisedTimeDTO();
						usertimeEntriesOfPreviousShifts.setUserId((Integer)row[0]);
						usertimeEntriesOfPreviousShifts.setStartDate((Date)row[1]);
						usertimeEntriesOfPreviousShifts.setShiftId((Integer)(row[2]));
						if(row[3] != null){//To avoid Null Pointer Exception
							usertimeEntriesOfPreviousShifts.setTimeSheetTotalHours(((Long)row[3]).intValue());
							if(row[4] != null){
								usertimeEntriesOfPreviousShifts.setTimeSheetTotalMins(((Long)row[4]).intValue());
								userPreviousShiftTimeBookingList.add(usertimeEntriesOfPreviousShifts);
							}
						}
					}
				}
			}
			
			
			if(userPreviousShiftTimeBookingList == null || userPreviousShiftTimeBookingList.size() == 0 && userWeekTimeBookingList != null && userWeekTimeBookingList.size()>0){
				userWeekTimeBookingCompleteList.addAll(userWeekTimeBookingList);
			}else if(userWeekTimeBookingList == null || userWeekTimeBookingList.size() == 0 && userPreviousShiftTimeBookingList != null && userPreviousShiftTimeBookingList.size()>0){
				userWeekTimeBookingCompleteList.addAll(userPreviousShiftTimeBookingList);
			}
			if(userWeekTimeBookingList != null && userWeekTimeBookingList.size()>0 && userPreviousShiftTimeBookingList != null && userPreviousShiftTimeBookingList.size()>0){
				userWeekTimeBookingCompleteList.addAll(userWeekTimeBookingList);
				int index = -1;
				for (UserWeekUtilisedTimeDTO tillPreviousDayTimeEntry : userWeekTimeBookingCompleteList) {
					index = userPreviousShiftTimeBookingList.indexOf(tillPreviousDayTimeEntry);
					if (index >= 0) {
						UserWeekUtilisedTimeDTO tillPreviousShiftTimeEntry = userPreviousShiftTimeBookingList.get(index);
						tillPreviousDayTimeEntry.setTimeSheetTotalHours(tillPreviousDayTimeEntry.getTimeSheetTotalHours()+tillPreviousShiftTimeEntry.getTimeSheetTotalHours());
						tillPreviousDayTimeEntry.setTimeSheetTotalMins(tillPreviousDayTimeEntry.getTimeSheetTotalMins()+tillPreviousShiftTimeEntry.getTimeSheetTotalMins());
					}
				}
			}
			
			if(userWeekTimeBookingList != null || userWeekTimeBookingList.size() > 0){
				userWeekTimeBookingCompleteList.addAll(userWeekTimeBookingList);
			}
			
			} catch (RuntimeException re) {
				log.error("list failed", re);
			}
			return userWeekTimeBookingCompleteList;
		}
	
	@Override
	@Transactional
	public List<UserWeekUtilisedTimeDTO> getReservedShiftsOfUser(int workPackgeId, int shiftId, Date blockResourceForDate) {
		log.debug("listing getReservedShiftsOfUser");
		List<UserWeekUtilisedTimeDTO> userWeekTimeBookingList = new ArrayList<UserWeekUtilisedTimeDTO>();
		log.debug("Getting shift booking for Date: " + blockResourceForDate);
		Date startDate = null;
		Date endDate = null;
		try {
			
			int weekDayNumber = DateUtility.getDayOfWeek(blockResourceForDate);
			log.info("weekDayNumber:: "+weekDayNumber);
			String  day = DateUtility.getDayNameForDayNumber(weekDayNumber);
			log.info("day:: "+day);
			if(day.equalsIgnoreCase("Monday")){
				// do nothing
			}else if(day.equalsIgnoreCase("Tuesday") || day.equalsIgnoreCase("Wednesday") || day.equalsIgnoreCase("Thursday") || day.equalsIgnoreCase("Friday")){
				startDate = DateUtility.getWeekStart(blockResourceForDate);
				log.info("startDate:: "+startDate);
				endDate = DateUtility.getPrevDate(blockResourceForDate);
				log.info("endDate:: "+endDate);
			}else if(day.equalsIgnoreCase("Saturday")){
				startDate = DateUtility.getWeekStart(blockResourceForDate);
				log.info("startDate:: "+startDate);
				endDate = DateUtility.getAnyPrevDate(blockResourceForDate, -1);
				log.info("endDate:: "+endDate);
			}else if(day.equalsIgnoreCase("Sunday")){
				startDate = DateUtility.getWeekStart(blockResourceForDate);
				log.info("startDate:: "+startDate);
				endDate = DateUtility.getAnyPrevDate(blockResourceForDate, -2);
				log.info("endDate:: "+endDate);
			}
		
		Criteria c = sessionFactory.getCurrentSession().createCriteria(ResourceAvailability.class,"resourceAvailable");
		c.add(Restrictions.between("resourceAvailable.workDate", startDate,  endDate));
		c.add(Restrictions.eq("resourceAvailable.bookForShift", 1));
		c.setProjection(Projections.projectionList()
									.add(Property.forName("resourceAvailable.resource.userId").as("userId"))
									.add(Projections.count("resourceAvailable.resource.userId"))
									.add(Projections.groupProperty("resourceAvailable.resource.userId"))
						);
		
		List<Object[]> list = c.list();
		UserWeekUtilisedTimeDTO userWeekUtilisedTimeDTO = null;
		for (Object[] row : list) {
			userWeekUtilisedTimeDTO = new UserWeekUtilisedTimeDTO();
			userWeekUtilisedTimeDTO.setUserId((Integer)row[0]);
			userWeekUtilisedTimeDTO.setShiftBookingCount(((Long)(row[1])).intValue());
			userWeekTimeBookingList.add(userWeekUtilisedTimeDTO);
		}
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return userWeekTimeBookingList;
	}
	
	@Override
	@Transactional
	public List<UserWeekUtilisedTimeDTO> getReservedShiftsforUserId(int userId, Date blockResourceForDate) {
		List<UserWeekUtilisedTimeDTO> userWeekTimeBookingList = new ArrayList<UserWeekUtilisedTimeDTO>();
		Date startDate = null;
		Date endDate = null;
		try {
			
			int weekDayNumber = DateUtility.getDayOfWeek(blockResourceForDate);
			String  day = DateUtility.getDayNameForDayNumber(weekDayNumber);
			if(day.equalsIgnoreCase("Monday")){
				// do nothing
			}else if(day.equalsIgnoreCase("Tuesday") || day.equalsIgnoreCase("Wednesday") || day.equalsIgnoreCase("Thursday") || day.equalsIgnoreCase("Friday")){
				startDate = DateUtility.getWeekStart(blockResourceForDate);
				endDate = DateUtility.getPrevDate(blockResourceForDate);
			}else if(day.equalsIgnoreCase("Saturday")){
				startDate = DateUtility.getWeekStart(blockResourceForDate);
				endDate = DateUtility.getAnyPrevDate(blockResourceForDate, -1);
			}else if(day.equalsIgnoreCase("Sunday")){
				startDate = DateUtility.getWeekStart(blockResourceForDate);
				endDate = DateUtility.getAnyPrevDate(blockResourceForDate, -2);
			}
		Criteria c = sessionFactory.getCurrentSession().createCriteria(ResourceAvailability.class,"resourceAvailable");
		c.add(Restrictions.between("resourceAvailable.workDate", startDate,  endDate));
		c.add(Restrictions.eq("resourceAvailable.bookForShift", 1));
		c.add(Restrictions.eq("resourceAvailable.resource.userId", userId));
		//ssdf
		c.setProjection(Projections.projectionList()
									.add(Property.forName("resourceAvailable.resource.userId").as("userId"))
									.add(Projections.count("resourceAvailable.resource.userId"))
									.add(Projections.groupProperty("resourceAvailable.resource.userId"))
						);
		
		List<Object[]> list = c.list();
		UserWeekUtilisedTimeDTO userWeekUtilisedTimeDTO = null;
		for (Object[] row : list) {
			userWeekUtilisedTimeDTO = new UserWeekUtilisedTimeDTO();
			userWeekUtilisedTimeDTO.setUserId((Integer)row[0]);
			if(row[1] != null){
				userWeekUtilisedTimeDTO.setShiftBookingCount(((Long)(row[1])).intValue());
				userWeekTimeBookingList.add(userWeekUtilisedTimeDTO);
			}
			
		}
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return userWeekTimeBookingList;
	}

	@Override
	@Transactional
	public List<ResourceShiftCheckIn> getResourceShiftCheckInByCheckinDate(
			int userId, String checkInDate) {
		String convertCheckInDate="";
		try {
			
			String temp[]=	checkInDate.split("-");
			int yearLength=temp[2].length();
			if(yearLength==4){
		for(int i=temp.length-1;i>=0;i--){
			
			if(i!=0){
				convertCheckInDate=convertCheckInDate+temp[i]+"-";
			}else{
				convertCheckInDate=convertCheckInDate+temp[i];
			}
		}
			}
			else{
				convertCheckInDate=checkInDate;
				
			}
		
		
		}catch(Exception e){
			
		}
		log.info("listing getResourceShiftCheckInByCheckinDate "+checkInDate);
		List<ResourceShiftCheckIn> resourceShiftCheckInList = null;
		try {		
			
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ResourceShiftCheckIn.class, "reschkin");
			c.createAlias("reschkin.userList", "resource");
			c.add(Restrictions.eq("resource.userId",userId));
			if(convertCheckInDate!=null && !convertCheckInDate.equals(""))
				c.add(Restrictions.eq("reschkin.createdDate", DateUtility.dateFormatWithOutSeconds(convertCheckInDate)));
			resourceShiftCheckInList = c.list();
		
			
			if (!resourceShiftCheckInList.isEmpty()) {
				log.info("Resource Check In List : "+resourceShiftCheckInList.size());
				for (ResourceShiftCheckIn resourceShiftCheckIn : resourceShiftCheckInList) {
					Hibernate.initialize(resourceShiftCheckIn.getActualShift());
					if(resourceShiftCheckIn.getActualShift().getShift() != null){
						Hibernate.initialize(resourceShiftCheckIn.getActualShift().getShift());
						Hibernate.initialize(resourceShiftCheckIn.getActualShift().getShift().getShiftType());
						Hibernate.initialize(resourceShiftCheckIn.getActualShift().getShift().getTestFactory());
					}
					Hibernate.initialize(resourceShiftCheckIn.getProductMaster());
					Hibernate.initialize(resourceShiftCheckIn.getUserList());
					Hibernate.initialize(resourceShiftCheckIn.getApproverUser());
				}
			}
			
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return resourceShiftCheckInList;
	}

	@Override
	@Transactional
	public List<TimeSheetEntryMaster> getTimeSheetEntriesOfResourceShiftCheckInId(
			int resourceShiftCheckInId) {

		List<TimeSheetEntryMaster> listOfTimeSheetEntryMaster = new  ArrayList<TimeSheetEntryMaster>();
		
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TimeSheetEntryMaster.class, "tsem");
			c.createAlias("tsem.resourceShiftCheckIn", "resShiftCheckIn");
			c.add(Restrictions.eq("resShiftCheckIn.resourceShiftCheckInId", resourceShiftCheckInId));
			listOfTimeSheetEntryMaster = c.list();
			if (!listOfTimeSheetEntryMaster.isEmpty()) {
				log.info("List of Time Entries for Resource shift check in : "+listOfTimeSheetEntryMaster.size());
				for (TimeSheetEntryMaster timeSheetEntryMaster : listOfTimeSheetEntryMaster) {
					Hibernate.initialize(timeSheetEntryMaster.getProduct());
					Hibernate.initialize(timeSheetEntryMaster.getRole());
					Hibernate.initialize(timeSheetEntryMaster.getWorkPackage());
					Hibernate.initialize(timeSheetEntryMaster.getActivityType());
					Hibernate.initialize(timeSheetEntryMaster.getApprover());
					Hibernate.initialize(timeSheetEntryMaster.getUser());
					Hibernate.initialize(timeSheetEntryMaster.getShift());
					Hibernate.initialize(timeSheetEntryMaster.getResourceShiftCheckIn().getActualShift());
				}
			}
			log.debug("list successful");
		} catch (Exception e) {
			log.error("list failed", e);
		}
		return listOfTimeSheetEntryMaster;
	}
}