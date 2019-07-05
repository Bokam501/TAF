package com.hcl.atf.taf.dao.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.TrccExecutionPlansCommonDAO;
import com.hcl.atf.taf.constants.TAFConstants;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.TrccExecutionPlan;
import com.hcl.atf.taf.model.TrccExecutionPlanDetails;

@Repository
public class TrccExecutionPlansCommonDAOImpl implements TrccExecutionPlansCommonDAO {
	private static final Log log = LogFactory.getLog(TrccExecutionPlansCommonDAOImpl.class);
	
	@Autowired(required=true)
    private SessionFactory sessionFactory;
	
	@Override
	@Transactional
	public int getTotalRecordsOfTrccExecutionPlan(int testRunConfigurationChildId) {
		log.debug("getting TrccExecutionPlan total records");
		int count =0;
		try {
			count=((Number) sessionFactory.getCurrentSession().createSQLQuery(""
					+ "select count(*) from trcc_execution_plan "
					+ "where "
					+ "testRunConfigurationChildId=:testRunConfigurationChildId")
					.setParameter("testRunConfigurationChildId", testRunConfigurationChildId).uniqueResult()).intValue();
			
			log.debug("total records fetch successful");
		} catch (RuntimeException re) {
			log.error("total records fetch failed", re);			
		}
		return count;
	}

	@Override
	@Transactional
	public TrccExecutionPlan getTrccExecutionPlanById(int trccExecutionPlanId) {
		log.debug("getting TrccExecutionplan instance by id");
		TrccExecutionPlan trccExecutionPlan=null;
		try {
			List list =  sessionFactory.getCurrentSession().createQuery(
					"from TrccExecutionPlan tep "
					+ "where "
					+ "trccExecutionPlanId=:trccExecutionPlanId")
					.setParameter("trccExecutionPlanId", trccExecutionPlanId)
					.list();
			trccExecutionPlan=(list!=null && list.size()!=0)?(TrccExecutionPlan)list.get(0):null;
			if (trccExecutionPlan != null) {
				Hibernate.initialize(trccExecutionPlan.getPlanName());
				Hibernate.initialize(trccExecutionPlan.getDescription());
				Hibernate.initialize(trccExecutionPlan.getCreatedDate());
				Hibernate.initialize(trccExecutionPlan.getStatus());
				Hibernate.initialize(trccExecutionPlan.getStatusChangeDate());
				Hibernate.initialize(trccExecutionPlan.getLastModifiedDate());
				Hibernate.initialize(trccExecutionPlan.getTestRunConfigurationChild());
				Hibernate.initialize(trccExecutionPlan.getIsDefaultPlan());
			}
			log.debug("getByTrccExecutionPlanId successful");
		} catch (RuntimeException re) {
			log.error("getByTrccExecutionPlanId failed", re);
		}
		return trccExecutionPlan;
	}

	@Override
	@Transactional
	public TrccExecutionPlan getTrccExecutionPlanByName(String planName) {
		log.debug("listing specific TrccExecutionPlan  instance");
		TrccExecutionPlan trccExecutionPlan=null;
		try {
			List list=sessionFactory.getCurrentSession().createQuery(
					"from TrccExecutionPlan "
					+ "where "
					+ "planName=:planName")
					.setParameter("planName", planName).list();
			
			//log.info();
			trccExecutionPlan=(list!=null && list.size()!=0)?(TrccExecutionPlan)list.get(0):null;
			log.debug("list specific successful");
		} catch (RuntimeException re) {
			log.error("list specific failed", re);
		}
		return trccExecutionPlan;
	}

	@Override
	@Transactional
	public List<TrccExecutionPlan> listTrccExecutionPlan(int testRunConfigurationChildId) {
		log.debug("listing TrccExecutionPlan instance");
		List<TrccExecutionPlan> trccExecutionPlans=null;
		try {
			trccExecutionPlans=sessionFactory.getCurrentSession().createQuery(""
					+ "from TrccExecutionPlan "
					+ "where "
					+ "testRunConfigurationChildId=:testRunConfigurationChildId and "
					+ "status=1")
					.setParameter("testRunConfigurationChildId",testRunConfigurationChildId)	               
	                .list();
			log.debug("list successful");
			if (!(trccExecutionPlans == null || trccExecutionPlans.isEmpty())) {
				for (TrccExecutionPlan trccExecutionPlan : trccExecutionPlans) {
					Hibernate.initialize(trccExecutionPlan.getPlanName());
					Hibernate.initialize(trccExecutionPlan.getDescription());
					Hibernate.initialize(trccExecutionPlan.getCreatedDate());
					Hibernate.initialize(trccExecutionPlan.getStatus());
					Hibernate.initialize(trccExecutionPlan.getStatusChangeDate());
					Hibernate.initialize(trccExecutionPlan.getLastModifiedDate());
					Hibernate.initialize(trccExecutionPlan.getTestRunConfigurationChild());
					Hibernate.initialize(trccExecutionPlan.getIsDefaultPlan());
				}
			}
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return trccExecutionPlans;
	}

	@Override
	@Transactional
	public void addTrccExecutionPlan(TrccExecutionPlan trccExecutionPlan) {
		log.debug("adding TrccExecutionPlan instance");
		try {
			trccExecutionPlan.setStatus(1);
			trccExecutionPlan.setStatusChangeDate(new Date(System.currentTimeMillis()));
			trccExecutionPlan.setCreatedDate(new Date(System.currentTimeMillis()));
			trccExecutionPlan.setLastModifiedDate(new Date(System.currentTimeMillis()));
			sessionFactory.getCurrentSession().save(trccExecutionPlan);
			log.debug("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}
	}

	@Override
	@Transactional
	public void updateTrccExecutionPlan(TrccExecutionPlan trccExecutionPlan) {
		   log.debug("updating TrccExecutionPlan instance");
           try {
                  
        	   TrccExecutionPlan existingTrccExecutionPlan = null;  
                  List list = sessionFactory.getCurrentSession().createQuery(""
                  		+ "from TrccExecutionPlan t "
                  		+ "where "
                  		+ "trccExecutionPlanId=:trccExecutionPlanId")
                  		.setParameter("trccExecutionPlanId",trccExecutionPlan.getTrccExecutionPlanId())
                        .list();
                  
                  existingTrccExecutionPlan=(list!=null && list.size()!=0)?(TrccExecutionPlan) list.get(0):null;
                  if (existingTrccExecutionPlan != null) {
                	  existingTrccExecutionPlan.setDescription(trccExecutionPlan.getDescription());
                	  existingTrccExecutionPlan.setPlanName(trccExecutionPlan.getPlanName());
                	  existingTrccExecutionPlan.setLastModifiedDate(new Date(System.currentTimeMillis()));
                	  existingTrccExecutionPlan.setIsDefaultPlan(trccExecutionPlan.getIsDefaultPlan());
                	  sessionFactory.getCurrentSession().saveOrUpdate(existingTrccExecutionPlan);
                      log.debug("update successful");
                  } else {
                        log.error("Update of TrccExecutionPlan failed");
                  }
           } catch (RuntimeException re) {
                  log.error("update failed", re);
           }
	}
	

	@Override
	@Transactional
	public void deleteTrccExecutionPlan(TrccExecutionPlan trccExecutionPlan) {
		log.debug("deleting TrccExecutionPlanFrom TestRunConfigurationChild instance");
		try {
			trccExecutionPlan.setStatus(TAFConstants.ENTITY_STATUS_INACTIVE);
			trccExecutionPlan.setStatusChangeDate(new Date(System.currentTimeMillis()));
			trccExecutionPlan.setLastModifiedDate(new Date(System.currentTimeMillis()));
			updateTrccExecutionPlan(trccExecutionPlan);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
		}	
	}

	@Override
	@Transactional
	public List<TestCaseList> getSelectedTestCasesFromPlanDetails(int trccExecutionPlanId, Integer deviceListId) {
		log.info("inside getSelectedTestCasesFromPlanDetails method");
		log.info("trccExecutionPlanId="+trccExecutionPlanId);
		log.info("deviceListId="+deviceListId);
		
		List<TestCaseList> testCaseList=null;
		try {
			
			testCaseList=sessionFactory.getCurrentSession().createQuery(""
					+ "select tc from TestCaseList tc, TrccExecutionPlanDetails planDetails "
					+ "where "
					+ "planDetails.trccExecutionPlan.trccExecutionPlanId=:trccExecutionPlanId and "
					+ "planDetails.deviceList.deviceListId = :deviceListId "
					+ "and planDetails.testCaseList.testCaseId = tc.testCaseId and "
					+ "planDetails.status=1")
					.setParameter("trccExecutionPlanId",trccExecutionPlanId)
					.setParameter("deviceListId",deviceListId)
					.list();
			
			log.debug("list successful");
			if (testCaseList == null || testCaseList.isEmpty())
				return null;
			
			for (TestCaseList testCase : testCaseList) {
				if (testCase != null) {
					log.info("testCase getTestCasePriority="+testCase.getTestCasePriority());
					log.info("testCase getTestCaseId="+testCase.getTestCaseId());
				}	
			}
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return testCaseList;
	}
	

	@Override
	@Transactional
	public List<TrccExecutionPlanDetails> listTrccExecutionPlanDetails(int trccExecutionPlanId, int deviceListId) {
		List<TrccExecutionPlanDetails> trccExecutionPlanDetails=null;
		try {
			trccExecutionPlanDetails=sessionFactory.getCurrentSession().createQuery(
					"from TrccExecutionPlanDetails tep "
					+ "where "
					+ "tep.trccExecutionPlan.trccExecutionPlanId=:trccExecutionPlanId and "
					+ "tep.deviceList.deviceListId=:deviceListId")
	                .setParameter("trccExecutionPlanId", trccExecutionPlanId)
	                .setParameter("deviceListId", deviceListId)
	                .list();
		
			log.debug("list successful");
			
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return trccExecutionPlanDetails;
	}

	@Override
	@Transactional
	public void addTrccExecutionPlanDetails(List<TrccExecutionPlanDetails> trccExecutionPlanDetails) {
		log.debug("adding list of TrccExecutionPlanDetail");
		for(TrccExecutionPlanDetails trccExecutionPlanDetail : trccExecutionPlanDetails){
			trccExecutionPlanDetail.setStatus(1);
			trccExecutionPlanDetail.setStatusChangeDate(new Date(System.currentTimeMillis()));
			sessionFactory.getCurrentSession().save(trccExecutionPlanDetail);
		}
		
	}
	
	@Override
	@Transactional
	public void updateTrccExecutionPlanDetail(TrccExecutionPlanDetails trccExecutionPlanDetails) {
		log.debug("updating list of TrccExecutionPlanDetail");
			
			if(trccExecutionPlanDetails.getDeviceList()!=null){
				trccExecutionPlanDetails.setDeviceList(trccExecutionPlanDetails.getDeviceList());
			}
			if(trccExecutionPlanDetails.getTrccExecutionPlan()!=null){
				trccExecutionPlanDetails.setTrccExecutionPlan(trccExecutionPlanDetails.getTrccExecutionPlan());
			}
			trccExecutionPlanDetails.setStatus(1);
			trccExecutionPlanDetails.setStatusChangeDate(new Date(System.currentTimeMillis()));
			sessionFactory.getCurrentSession().save(trccExecutionPlanDetails);
		}
	
	@Override
	@Transactional
	public void deleteExistingTrccExecutionPlanDetails(List<TrccExecutionPlanDetails> trccExecutionPlanDetails) {
		log.info("Deleting Existed TrccExecutionPlanDetails");
		try{
			log.info("==========daoImpl delete method=====:"+trccExecutionPlanDetails.size());

			for(TrccExecutionPlanDetails trccExecutionPlanDetail : trccExecutionPlanDetails){
				log.info("id=" + trccExecutionPlanDetail.getTrccExecutionPlanDetailsId());
				
				trccExecutionPlanDetail.setStatus(TAFConstants.ENTITY_STATUS_INACTIVE);
				trccExecutionPlanDetail.setStatusChangeDate(new Date(System.currentTimeMillis()));
				sessionFactory.getCurrentSession().saveOrUpdate(trccExecutionPlanDetail);
			}
			log.info("Deleted TrccExecutionPlanDetails entity successfully");	
		} catch(Exception e){
			log.error("Error in deleting TrccExecutionPlanDetails ", e);
		}
		
	}
	
	@Override
	@Transactional
	public void deleteExistingTrccExecutionPlanDetails(int trccExecutionPlanId, int deviceListId) {
		log.info("Deleting Existing TrccExecutionPlanDetails");
		try{
			sessionFactory.getCurrentSession().createQuery(""
					+ "delete from TrccExecutionPlanDetails "
					+ "where "
					+ "trccExecutionPlan.trccExecutionPlanId=:trccExecutionPlanId and "
					+ "deviceList.deviceListId=:deviceListId")
				.setParameter("trccExecutionPlanId", trccExecutionPlanId)
				.setParameter("deviceListId", deviceListId)
				.executeUpdate();
			log.info("Deleted TrccExecutionPlanDetails entity successfully");	
		} catch(Exception e){
			log.error("Error in deleting TrccExecutionPlanDetails"+ e);
		}
		
	}
	
	@Override
	@Transactional
	public int getTotalRecordsOfTrccExecutionPlanDetailsOnADevice(int trccExecutionPlanId, int deviceListId) {
		log.debug("getting TrccExecutionPlanDetailsOnADevice total records");
		int count=0;
		try {
			count=((Number) sessionFactory.getCurrentSession().createSQLQuery(
					"select count(*) from trcc_execution_plan_details "
					+ "where "
					+ "trccExecutionPlanId=:trccExecutionPlanId and "
					+ "deviceListId=:deviceListId")
					.setParameter("trccExecutionPlanId", trccExecutionPlanId)
					.setParameter("deviceListId", deviceListId)
					.uniqueResult()).intValue();
			
			log.debug("total records fetch successful");
		} catch (RuntimeException re) {
			log.error("total records fetch failed", re);			
		}
		return count;
	}	
}