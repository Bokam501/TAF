package com.hcl.atf.taf.dao.impl;

import java.util.ArrayList;
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

import com.hcl.atf.taf.dao.WorkShiftMasterDAO;
import com.hcl.atf.taf.model.ActualShift;
import com.hcl.atf.taf.model.ShiftTypeMaster;
import com.hcl.atf.taf.model.WorkShiftMaster;
import com.hcl.atf.taf.model.json.JsonWorkShiftMaster;


@Repository
public class WorkShiftMasterDAOImpl implements WorkShiftMasterDAO {
	private static final Log log = LogFactory.getLog(WorkShiftMasterDAOImpl.class);

	@Autowired(required=true)
    private SessionFactory sessionFactory;

	
	@Override
	@Transactional
	public List<WorkShiftMaster> listWorkShiftsByTestFactoryId(int testFactoryId) {
		log.debug("inside listWorkShiftsByTestFactoryId in WorkShiftMasterDAOImpl");
		List<WorkShiftMaster> workShiftsList = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkShiftMaster.class, "ws");
			if(testFactoryId != -1){
				c.add(Restrictions.eq("ws.testFactory.testFactoryId", testFactoryId));
			}
			workShiftsList = c.list();
			if (!workShiftsList.isEmpty()) {
				for (WorkShiftMaster workShift : workShiftsList) {
					Hibernate.initialize(workShift.getShiftType());
					Hibernate.initialize(workShift.getTestFactory());
					Hibernate.initialize(workShift.getShiftType().getShiftName());
				}
			}
			
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return workShiftsList;
	}
	
	@Override
	@Transactional
	public List<JsonWorkShiftMaster> listJsonWorkShiftsByTestFactoryId(int testFactoryId) {
		log.debug("inside listJsonWorkShiftsByTestFactoryId by testFactoryId");
		List<WorkShiftMaster> workShiftsList = null;
		List<JsonWorkShiftMaster> jsonWorkShiftMaster= new ArrayList<JsonWorkShiftMaster>();
		try {
			String hql = "";
				if(testFactoryId != -1){
					hql="from WorkShiftMaster wsm where wsm.testFactory.testFactoryId=:tfId";			
					 workShiftsList	 =sessionFactory.getCurrentSession().createQuery(hql).setParameter("tfId", testFactoryId).list();
				}else{
					hql="from WorkShiftMaster";			
					 workShiftsList	 =sessionFactory.getCurrentSession().createQuery(hql).list();
				}
			if (!workShiftsList.isEmpty()) {
				for (WorkShiftMaster workShift : workShiftsList) {
					Hibernate.initialize(workShift.getShiftType());
					Hibernate.initialize(workShift.getTestFactory());
					Hibernate.initialize(workShift.getShiftType().getShiftName());
					jsonWorkShiftMaster.add(new JsonWorkShiftMaster(workShift));
				}
			}
			
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return jsonWorkShiftMaster;
	}
	
	@Override
	@Transactional
	public List<ActualShift> listActualShift(Integer workShiftId, Date workDate) {
		log.info("inside listshiftManage in WorkShiftMasterDAOImpl");
		List<ActualShift> actualShift = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ActualShift.class, "actual");
			if(workShiftId!=-1){
			c.add(Restrictions.eq("actual.shift.shiftId", workShiftId));
			}
			if(workDate!=null){
			c.add(Restrictions.eq("actual.workdate", workDate));
			}
			log.debug("list successful");
			actualShift =  c.list();
			log.debug("list size: "+actualShift.size());
			if (!actualShift.isEmpty()) {
				log.info("Actual Shift : "+actualShift.size());
				for (ActualShift actualShifts : actualShift) {
					Hibernate.initialize(actualShifts.getShift());
					Hibernate.initialize(actualShifts.getShift().getTestFactory());
				}
			}
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return actualShift;
	}
	
	@Override
	@Transactional
	public ActualShift listshiftManage(Integer workShiftId, Date workDate) {
		log.info("inside listshiftManage in WorkShiftMasterDAOImpl");
		ActualShift actualShift = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ActualShift.class, "actual");
			c.add(Restrictions.eq("actual.shift.shiftId", workShiftId));
			c.add(Restrictions.eq("actual.workdate", workDate));
			log.debug("list successful");
			List list = c.list();
			actualShift = (list != null && list.size() != 0) ? (ActualShift) list.get(0) : null;
			
			if (actualShift != null) 				
				Hibernate.initialize(actualShift.getShift());
				
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return actualShift;
	}

	
	@Override
	@Transactional
	public ActualShift listActualShiftbyActualShiftId(Integer actualShiftId) {
		log.debug("getting ActualShift instance by actualShiftid");
		ActualShift actualShift=null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ActualShift.class, "actual");
			c.add(Restrictions.eq("actual.actualShiftId", actualShiftId));
			List<ActualShift> list = c.list();
			actualShift=(list!=null && list.size()!=0)?(ActualShift)list.get(0):null;
			if (actualShift != null) {
				Hibernate.initialize(actualShift.getShift());
			}
		} catch (RuntimeException re) {
			log.error(re);
		}
		
		return actualShift;
	}
	
	@Override
	@Transactional
	public void addShiftManage(ActualShift actualShifts) {
		log.debug("Adding ActualShift instance");
		try {
			log.info("Add is executed");
			sessionFactory.getCurrentSession().save(actualShifts);
			log.info("Add successful");
		} catch (RuntimeException re) {
			log.error("Add failed", re);
		}
	}
	
	@Override
	@Transactional
	public void updateShiftManage(ActualShift actualShifts) {
		log.debug("updating ActualShift instance");
		try {
			log.info("update is executed");
			sessionFactory.getCurrentSession().update(actualShifts);
			log.info("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
		}
	}

	@Override
	@Transactional
	public void addTestFactoryShits(WorkShiftMaster workShiftMaster) {
		log.debug("adding WorkShiftMaster instance");
		try {
			
			sessionFactory.getCurrentSession().save(workShiftMaster);
			log.debug("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}
		
	}

	@Override
	@Transactional
	public WorkShiftMaster listWorkShiftsByshiftId(Integer shiftId) {
		WorkShiftMaster workShiftMaster = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkShiftMaster.class, "workshift");
			c.add(Restrictions.eq("workshift.shiftId", shiftId));
			List<WorkShiftMaster> list = c.list();
			workShiftMaster=(list!=null && list.size()!=0)?(WorkShiftMaster)list.get(0):null;
			
			if (workShiftMaster != null) {
				Hibernate.initialize(workShiftMaster.getTestFactory());
				Hibernate.initialize(workShiftMaster.getShiftType());
			}
		} catch (Exception e) {
			log.error(e);
		}
		return workShiftMaster;
	}

	@Override
	@Transactional
	public void updateTestFactoryShitsInline(WorkShiftMaster workShiftMaster) {
		log.debug("updating WorkshiftMaster instance");
		try {
			log.info("update is executed");
			sessionFactory.getCurrentSession().update(workShiftMaster);
			log.info("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
		}
		
	}

	@Override
	@Transactional
	public ShiftTypeMaster getShiftTypeByShiftId(int shiftId) {
		log.debug("getting ActualShift instance by actualShiftid");
		ShiftTypeMaster shiftType = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkShiftMaster.class, "ws");
			c.add(Restrictions.eq("ws.shiftId", shiftId));
			List<WorkShiftMaster> list = c.list();
			if(list!=null && list.size()!=0){						
				WorkShiftMaster wsm = (WorkShiftMaster)list.get(0);
				if (wsm != null) {
					Hibernate.initialize(wsm.getShiftType());
					shiftType = wsm.getShiftType();
				}
			}
		} catch (Exception re) {
			log.error(re);
		}
		return shiftType;
	}

	@Override
	@Transactional
	public ActualShift listActualShiftbyshiftId(Integer shiftId, Date workDate) {
		log.debug("getting ActualShift instance by shiftId and Date");
		ActualShift actualShift=null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ActualShift.class, "actual");
			c.add(Restrictions.eq("actual.shift.shiftId", shiftId));
			c.add(Restrictions.eq("actual.workdate", workDate));
			List<ActualShift> list = c.list();
			actualShift=(list!=null && list.size()!=0)?(ActualShift)list.get(0):null;
			if (actualShift != null) {
				Hibernate.initialize(actualShift.getShift());
				Hibernate.initialize(actualShift.getShift().getShiftType());
			}
		} catch (RuntimeException re) {
			log.error(re);
		}
		return actualShift;
	}
	
	@Override
	@Transactional
	public WorkShiftMaster getWorkShiftByshiftTypeId(int shiftTypeId) {
		log.debug("getting ActualShift instance by actualShiftid");
		ShiftTypeMaster shiftType = null;
		WorkShiftMaster wsm = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkShiftMaster.class, "ws");
			c.createAlias("ws.shiftType", "shiftTypemaster");
		
			c.add(Restrictions.eq("shiftTypemaster.shiftTypeId", shiftTypeId));
			List<WorkShiftMaster> list = c.list();
			if(list!=null && list.size()!=0){						
				wsm = (WorkShiftMaster)list.get(0);
				if (wsm != null) {
					Hibernate.initialize(wsm.getShiftType());
					shiftType = wsm.getShiftType();
				}
			}
		} catch (Exception re) {
			log.error(re);
		}
		return wsm;
	}

	@Override
	@Transactional
	public List<ActualShift> getActualShiftByStartByUserId(Integer userId) {
		List<ActualShift> actualShiftList = null;
		try {Criteria c = sessionFactory.getCurrentSession().createCriteria(ActualShift.class, "actualShift");
			c.createAlias("actualShift.startByUserList", "user");
			c.add(Restrictions.eq("user.userId", userId));
			actualShiftList=c.list();
			log.info("actualShiftList size-->"+actualShiftList.size());
			if (!(actualShiftList == null || actualShiftList.isEmpty())) {
				for (ActualShift actualShift : actualShiftList) {
					Hibernate.initialize(actualShift.getShift());
					Hibernate.initialize(actualShift.getStartByUserList());
					Hibernate.initialize(actualShift.getEndByUserList());
				}
			}
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return actualShiftList;
	}
}
