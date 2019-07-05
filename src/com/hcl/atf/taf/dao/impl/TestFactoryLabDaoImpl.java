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

import com.hcl.atf.taf.dao.TestFactoryLabDao;
import com.hcl.atf.taf.model.TestFactoryLab;
import com.hcl.atf.taf.model.TestfactoryResourcePool;
import com.hcl.atf.taf.model.WorkPackageDemandProjection;

@Repository
public class TestFactoryLabDaoImpl  implements TestFactoryLabDao {
	

	private static final Log log = LogFactory.getLog(ProductLocaleDaoImpl.class);
	
	@Autowired(required=true)
    private SessionFactory sessionFactory;


	@Override
	@Transactional
	public List<TestFactoryLab> getTestFactoryLabsList() {
	
		List<TestFactoryLab> tfLabsList=null;
		try {
			
			tfLabsList=sessionFactory.getCurrentSession().createQuery("from TestFactoryLab where status=1 and testFactoryLabId !=-10").list();
			
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
			//throw re;
		}
		return tfLabsList;
	}


	@Override
	@Transactional
	public List<TestFactoryLab> getTestFactoryLabsList(Integer testFactoryLabId) {
	
		List<TestFactoryLab> tfLabsList=null;
		try {
			
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestFactoryLab.class, "tflap");
			c.add(Restrictions.eq("tflap.testFactoryLabId", testFactoryLabId));
			tfLabsList = c.list();
			
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return tfLabsList;
	}
	@Override
	@Transactional
	public TestFactoryLab getTestFactoryLabBytestFactoryLabId(int testFactoryLabId) {	

		log.info("getting TestFactoryLab instance by id");
		TestFactoryLab testFactoryLab=null;
		try {
			
			List list =  sessionFactory.getCurrentSession().createQuery("from TestFactoryLab tfl where tfl.testFactoryLabId=:labId")
					.setParameter("labId", testFactoryLabId)
					.list();
			testFactoryLab=(list!=null && list.size()!=0)?(TestFactoryLab)list.get(0):null;
			
			log.debug("getTestFactoryLabBytestFactoryLabId successful");
		} catch (RuntimeException re) {
			log.error("getTestFactoryLabBytestFactoryLabId failed", re);
		}
		return testFactoryLab;
	}


	@Override
	@Transactional
	public List<TestFactoryLab> getTestFactoryLabByResourcePoolId(int resourcePoolId) {
		List<TestFactoryLab> listOfTestFactoryLab = new ArrayList<TestFactoryLab>();
		List<TestfactoryResourcePool> listOfTestfactoryResourcePool = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestfactoryResourcePool.class, "tfrp");
			c.add(Restrictions.eq("tfrp.resourcePoolId", resourcePoolId));
			listOfTestfactoryResourcePool = c.list();
			log.info("Result Set Size of Resource Pool list: " + listOfTestfactoryResourcePool.size());
			
			for (TestfactoryResourcePool testfactoryResourcePool : listOfTestfactoryResourcePool) {
				Hibernate.initialize(testfactoryResourcePool.getTestFactoryLab());
				listOfTestFactoryLab.add(testfactoryResourcePool.getTestFactoryLab());
			}
		} catch (Exception e) {
			log.error("Getting Products with user is associated with via Work Package", e);
		}
		return listOfTestFactoryLab;
	}
	
	
	@Override
	@Transactional
	public List<WorkPackageDemandProjection> listDemandDetailsOfTestFactoryLab(Integer testFactoryLabId,
			Integer shiftTypeId, Date date) {
				log.info("listing listWorkpackageDemandProdjectionByWorkpackageId");
				List<WorkPackageDemandProjection> listWorkPackageDemandProjection = new ArrayList<WorkPackageDemandProjection>();
				try {
					Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackageDemandProjection.class, "wpdp");
					c.createAlias("wpdp.workPackage", "workPackage");
					c.createAlias("workPackage.productBuild", "productbuild");
					c.createAlias("productbuild.productVersion", "productVersion");
					c.createAlias("productVersion.productMaster", "product");
					c.createAlias("product.testFactory", "tf");
					c.createAlias("tf.testFactoryLab", "tfl");
					c.createAlias("wpdp.workShiftMaster", "shift");
					c.createAlias("shift.shiftType", "shiftType");
					
					if (shiftTypeId != null) {
						c.add(Restrictions.eq("shiftType.shiftTypeId", shiftTypeId.intValue()));
					}
					if (testFactoryLabId != null) {
						c.add(Restrictions.eq("tfl.testFactoryLabId", testFactoryLabId));
					}
					c.add(Restrictions.eq("workDate", date));
					
					listWorkPackageDemandProjection = c.list();
					if(listWorkPackageDemandProjection!=null && !listWorkPackageDemandProjection.isEmpty()){
						for(WorkPackageDemandProjection workPackageDemandProjection:listWorkPackageDemandProjection){
							Hibernate.initialize(workPackageDemandProjection.getWorkPackage());
							Hibernate.initialize(workPackageDemandProjection.getWorkPackage().getProductBuild().getProductVersion().getProductMaster());
							Hibernate.initialize(workPackageDemandProjection.getWorkShiftMaster());
							Hibernate.initialize(workPackageDemandProjection.getUserRole());
							Hibernate.initialize(workPackageDemandProjection.getSkill());
							Hibernate.initialize(workPackageDemandProjection.getDemandRaisedByUser());
						}
					}
					log.debug("list successful"+listWorkPackageDemandProjection.size());
				} catch (RuntimeException re) {
					log.error("list failed", re);
				}catch(Exception e){
					log.error("ERROR  ",e);
				}	
				return listWorkPackageDemandProjection;
	}
	

}
