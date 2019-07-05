package com.hcl.atf.taf.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.constants.TAFConstants;
import com.hcl.atf.taf.dao.TestRunConfigurationChildDAO;
import com.hcl.atf.taf.model.DeviceList;
import com.hcl.atf.taf.model.ProductVersionListMaster;
import com.hcl.atf.taf.model.TestEnvironmentDevices;
import com.hcl.atf.taf.model.TestExecutionResult;
import com.hcl.atf.taf.model.TestRunConfigurationChild;
import com.hcl.atf.taf.model.TestRunList;

@Repository
public class TestRunConfigurationChildDAOImpl implements TestRunConfigurationChildDAO {
	private static final Log log = LogFactory.getLog(TestRunConfigurationChildDAOImpl.class);
	
	@Autowired(required=true)
    private SessionFactory sessionFactory;

	@Override
	@Transactional
	public void add(TestRunConfigurationChild testRunConfigurationChild) {
		log.debug("adding TestRunConfigurationChild instance");
		try {
			sessionFactory.getCurrentSession().save(testRunConfigurationChild);
			log.debug("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}
		
	}

	@Override
	@Transactional
	public void delete(TestRunConfigurationChild testRunConfigurationChild) {
		log.debug("deleting TestRunConfigurationChild instance");
		try {
			testRunConfigurationChild.setStatus(0);
			testRunConfigurationChild.setStatusChangeDate(new Date(System.currentTimeMillis()));
			update(testRunConfigurationChild);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
		}		
		
	}
	
	@Override
	@Transactional
	public void reactivate(TestRunConfigurationChild testRunConfigurationChild) {
		log.debug("deleting TestRunConfigurationChild instance");
		try {
			testRunConfigurationChild.setStatus(TAFConstants.ENTITY_STATUS_ACTIVE);
			testRunConfigurationChild.setStatusChangeDate(new Date(System.currentTimeMillis()));
			update(testRunConfigurationChild);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
		}		
		
	}
	
	@Override
	@Transactional
	public void delete(Set<TestRunConfigurationChild> testRunConfigurationChilds) {
		log.debug("deleting TestRunConfigurationChild instances");
		try {
			for(TestRunConfigurationChild testRunConfigurationChild: testRunConfigurationChilds){
				
				testRunConfigurationChild.setStatus(0);
				testRunConfigurationChild.setStatusChangeDate(new Date(System.currentTimeMillis()));
				update(testRunConfigurationChild);
			}
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
		}		
		
	}

	@Override
	@Transactional
	public void reactivate(Set<TestRunConfigurationChild> testRunConfigurationChilds) {
		log.debug("deleting TestRunConfigurationChild instances");
		try {
			for(TestRunConfigurationChild testRunConfigurationChild: testRunConfigurationChilds){
				
				testRunConfigurationChild.setStatus(TAFConstants.ENTITY_STATUS_ACTIVE);
				testRunConfigurationChild.setStatusChangeDate(new Date(System.currentTimeMillis()));
				update(testRunConfigurationChild);
			}
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
		}		
		
	}

	@Override
	@Transactional
	public TestRunConfigurationChild getByTestRunConfigurationChildId(
			int testRunConfigurationChildId) {
		log.debug("getting getByTestRunConfigurationChild instance by id");
		TestRunConfigurationChild testRunConfigurationChild=null;
		try {
			List list = sessionFactory.getCurrentSession().createQuery("from TestRunConfigurationChild t where testRunConfigurationChildId=:testRunConfigurationChildId").setParameter("testRunConfigurationChildId",testRunConfigurationChildId).list();
			testRunConfigurationChild=(list!=null && list.size()!=0)?(TestRunConfigurationChild) list.get(0):null;
			if (testRunConfigurationChild != null) {
			
				Hibernate.initialize(testRunConfigurationChild.getTestRunConfigurationParent());
				Hibernate.initialize(testRunConfigurationChild.getProductVersionListMaster().getProductMaster());
				Hibernate.initialize(testRunConfigurationChild.getTestSuiteList().getTestCaseLists());
				Hibernate.initialize(testRunConfigurationChild.getTestEnviromentMaster().getTestToolMaster());
				Hibernate.initialize(testRunConfigurationChild.getTestCategoryMaster());
				Hibernate.initialize(testRunConfigurationChild.getTestRunLists());
			}
			log.debug("getByUserId successful");
		} catch (RuntimeException re) {
			log.error("getByUserId failed", re);
		}
		return testRunConfigurationChild;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public TestRunConfigurationChild getByTestRunConfigurationChildName(String testRunConfigurationChildName) {
		log.debug("getting getByTestRunConfigurationChild instance by Name");
		List<TestRunConfigurationChild> testRunConfigurationChildList=null;

		try {
			testRunConfigurationChildList=sessionFactory.getCurrentSession().createQuery("from TestRunConfigurationChild e where e.testRunConfigurationName =:testRunConfigurationChildName")
	                .setParameter("testRunConfigurationChildName", testRunConfigurationChildName)
	                .list();
			log.debug("getting getByTestRunConfigurationChild instance by Name successful");
		} catch (RuntimeException re) {
			log.error("getting getByTestRunConfigurationChild instance by Name failed", re);
		}

		if (testRunConfigurationChildList == null || testRunConfigurationChildList.isEmpty())
			return null;
		
		return testRunConfigurationChildList.get(0);
	}

	@Override
	@Transactional
	public int getTotalRecords() {
		log.debug("getting TestRunConfigurationChild total records");
		int count =0;
		try {
			count=((Number) sessionFactory.getCurrentSession().createSQLQuery("select count(*) from test_run_configuration_child").uniqueResult()).intValue();
			
			log.debug("total records fetch successful");
		} catch (RuntimeException re) {
			log.error("total records fetch failed", re);			
		}
		return count;
	
	}	
	@Override
	@Transactional
	public List<TestRunConfigurationChild> listAll(int startIndex, int pageSize) {
		log.debug("listing TestRunConfigurationChild instance");
		List<TestRunConfigurationChild> testRunConfigurationChild=null;
		try {
			testRunConfigurationChild=sessionFactory.getCurrentSession().createQuery("from TestRunConfigurationChild where status=1")
	                .setFirstResult(startIndex)
	                .setMaxResults(pageSize)
	                .list();
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return testRunConfigurationChild;     
	}
	@Override
	@Transactional
	public List<TestRunConfigurationChild> list(int testRunConfigurationParentId,
			int startIndex, int pageSize) {
		log.debug("listing TestRunConfigurationChild instance");
		List<TestRunConfigurationChild> testRunConfigurationChild=null;
		try {
			testRunConfigurationChild=sessionFactory.getCurrentSession().createQuery("from TestRunConfigurationChild where testRunConfigurationParentId=:testRunConfigurationParentId").setParameter("testRunConfigurationParentId",testRunConfigurationChild)
	                .setFirstResult(startIndex)
	                .setMaxResults(pageSize)
	                .list();
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return testRunConfigurationChild; 
	}
	
	
	@Override
	@Transactional
	public List<TestRunConfigurationChild> listAll() {
		log.debug("listing TestRunConfigurationChild instance");
		List<TestRunConfigurationChild> testRunConfigurationChild=null;
		try {
			testRunConfigurationChild=sessionFactory.getCurrentSession().createQuery("from TestRunConfigurationChild")
										.list();
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return testRunConfigurationChild;     
	}
		
	@Override
    @Transactional
    public void update(TestRunConfigurationChild testRunConfigurationChild) {
           log.debug("updating TestRunConfigurationChild instance");
           try {
                  
        	   TestRunConfigurationChild existingTestRunConfigurationChild = null;  
                  List list = sessionFactory.getCurrentSession().createQuery("from TestRunConfigurationChild t where testRunConfigurationChildId=:testRunConfigurationChildId").setParameter("testRunConfigurationChildId",testRunConfigurationChild.getTestRunConfigurationChildId())
                               .list();
                  existingTestRunConfigurationChild=(list!=null && list.size()!=0)?(TestRunConfigurationChild) list.get(0):null;
                  if (existingTestRunConfigurationChild != null) {
                	  	 existingTestRunConfigurationChild.setTestEnviromentMaster(testRunConfigurationChild.getTestEnviromentMaster());
                	  	 existingTestRunConfigurationChild.setTestCategoryMaster(testRunConfigurationChild.getTestCategoryMaster());
                	  	 existingTestRunConfigurationChild.setTestRunScheduledStartTime(testRunConfigurationChild.getTestRunScheduledStartTime());
                	  	 existingTestRunConfigurationChild.setTestRunCronSchedule(testRunConfigurationChild.getTestRunCronSchedule());
                	  	 existingTestRunConfigurationChild.setTestRunScheduledEndTime(testRunConfigurationChild.getTestRunScheduledEndTime());
                	  	 existingTestRunConfigurationChild.setTestSuiteList(testRunConfigurationChild.getTestSuiteList());
                	  	 existingTestRunConfigurationChild.setLocale(testRunConfigurationChild.getLocale());
                	  	 existingTestRunConfigurationChild.setNotifyByMail(testRunConfigurationChild.getNotifyByMail());
                         sessionFactory.getCurrentSession().saveOrUpdate(existingTestRunConfigurationChild);
                         log.debug("update successful");
                  } else {
                        log.error("Update of testRunConfigurationChild failed");
                  }
           } catch (RuntimeException re) {
                  log.error("update failed", re);
           }
    }

	@Override
	@Transactional
	public List<TestRunConfigurationChild> list(int testRunConfigurationParentId) {
		log.debug("listing TestRunConfigurationChild instance");
		List<TestRunConfigurationChild> testRunConfigurationChild=null;
		try {
			testRunConfigurationChild=sessionFactory.getCurrentSession().createQuery("from TestRunConfigurationChild where testRunConfigurationParentId=:testRunConfigurationParentId").setParameter("testRunConfigurationParentId",testRunConfigurationParentId)	               
	                .list();
			log.debug("list successful");
			if (!(testRunConfigurationChild == null || testRunConfigurationChild.isEmpty())) {
				for (TestRunConfigurationChild trcc : testRunConfigurationChild) {
					Hibernate.initialize(trcc.getTestRunConfigurationParent());
					Hibernate.initialize(trcc.getTestSuiteList());
					Hibernate.initialize(trcc.getTestEnviromentMaster().getTestToolMaster());
					Hibernate.initialize(trcc.getTestCategoryMaster());
					Hibernate.initialize(trcc.getProductVersionListMaster().getProductMaster());
				}
			}
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return testRunConfigurationChild; 
	}
	
	@Override
	@Transactional
	public List<TestRunConfigurationChild> list(int testRunConfigurationParentId, int status) {
		log.debug("listing TestRunConfigurationChild instance");
		if(status == TAFConstants.ENTITY_STATUS_ALL){
			return list(testRunConfigurationParentId);
		}
		
		List<TestRunConfigurationChild> testRunConfigurationChild=null;
		try {
			
			testRunConfigurationChild=sessionFactory.getCurrentSession().createQuery("from TestRunConfigurationChild where testRunConfigurationParentId=:testRunConfigurationParentId and status=:status")
					.setParameter("testRunConfigurationParentId",testRunConfigurationParentId)
					.setParameter("status",status)
					.list();
			log.debug("list successful");
			if (!(testRunConfigurationChild == null || testRunConfigurationChild.isEmpty())) {
				for (TestRunConfigurationChild trcc : testRunConfigurationChild) {
					Hibernate.initialize(trcc.getTestRunConfigurationParent());
					Hibernate.initialize(trcc.getTestSuiteList());
					Hibernate.initialize(trcc.getTestEnviromentMaster().getTestToolMaster());
					Hibernate.initialize(trcc.getTestCategoryMaster());
					Hibernate.initialize(trcc.getProductVersionListMaster().getProductMaster());
				}
			}
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return testRunConfigurationChild; 
	}


	@Override
	@Transactional
	public int getTotalRecords(int testRunConfigurationParentId) {
		log.debug("getting TestRunConfigurationChild total records");
		int count =0;
		try {
			count=((Number) sessionFactory.getCurrentSession().createSQLQuery("select count(*) from test_run_configuration_child where testRunConfigurationParentId=:testRunConfigurationParentId").setParameter("testRunConfigurationParentId", testRunConfigurationParentId).uniqueResult()).intValue();
			
			log.debug("total records fetch successful");
		} catch (RuntimeException re) {
			log.error("total records fetch failed", re);			
		}
		return count;
	}

	@Override
	@Transactional
	public int getMaxRunNo() {
		log.debug("getting Max Run No");
		int max =0;
		try {
			max=((Number) sessionFactory.getCurrentSession().createSQLQuery("select max(lastRunNo) from test_run_configuration_child").uniqueResult()).intValue();
			
			log.debug("Max Run No fetch successful");
		} catch (RuntimeException re) {
			log.error("Max Run No fetch failed", re);			
		}
		//
		return max;		
	}	
	
	@Override
	@Transactional
	public List<TestRunList> getRunNo(int productId, String pltfm_id){
		log.debug("getting Run No");		
		int max =0;			
		List<ProductVersionListMaster> productVersionListMaster=new ArrayList<ProductVersionListMaster>();
		List<TestExecutionResult> TEResult_prdid=null;
		List<TestExecutionResult> TEResult_runno=null;
		List<TestRunList> TR_list = new ArrayList<TestRunList>();
		List<Integer> TR_runno_list = new ArrayList<Integer>();
		if(productId!=-1 && pltfm_id.equalsIgnoreCase("ALL")){		
			try {				
				StringBuffer TER_qry= new StringBuffer("from TestExecutionResult");
				TER_qry.append(" e where ");
				TER_qry.append(" e.testRunList.testRunConfigurationChild.productVersionListMaster.productMaster.productId= :productId");
				Query hib_TER_qry = sessionFactory.getCurrentSession().createQuery(TER_qry.toString());
						
						hib_TER_qry.setParameter("productId", productId);						
						int pageSize=500;					
						
						TEResult_prdid=hib_TER_qry.setFirstResult(0).setMaxResults(pageSize).list();
						
						for(TestExecutionResult tex:TEResult_prdid){
							ProductVersionListMaster pvlm= tex.getTestRunList().getTestRunConfigurationChild().getProductVersionListMaster();
							productVersionListMaster.add(pvlm);
						}
					for(ProductVersionListMaster prd_ver:productVersionListMaster){						
						//From run list
						StringBuffer TER_runno_qry= new StringBuffer("from TestExecutionResult");
						TER_runno_qry.append(" e where ");
						TER_runno_qry.append(" e.testRunList.testRunConfigurationChild.productVersionListMaster.productMaster.productId= :productId");
						TER_runno_qry.append(" AND ");
						TER_runno_qry.append(" e.testRunList.testRunConfigurationChild.productVersionListMaster.devicePlatformVersionListMaster.devicePlatformMaster.devicePlatformName=:platformName");
						TER_runno_qry.append(" group by e.testRunList.runNo");
						Query hib_TER_runno_qry = sessionFactory.getCurrentSession().createQuery(TER_runno_qry.toString());
						hib_TER_runno_qry.setParameter("productId", productId);
						TEResult_runno=hib_TER_runno_qry.list();		
		
						for(TestExecutionResult terr:TEResult_runno){

							if(TR_runno_list !=null){
								TestRunList terr_tr = terr.getTestRunList();
								Integer ter_runno=terr_tr.getRunNo();
								//ADDING ACCURATE RUN NOS
								if(!TR_runno_list.contains(ter_runno)){
									TR_list.add(terr_tr);
									TR_runno_list.add(ter_runno);									
								}else{
									//Nothing
								}								
							}
						}						
						
						}
					
				log.debug("Max Run No fetch successful");
			} catch (RuntimeException re) {
				log.error("Max Run No fetch failed", re);		
			}			
		}else if(productId!=-1 && !pltfm_id.equalsIgnoreCase("ALL")){
			try {				
					StringBuffer TER_qry= new StringBuffer("from ProductVersionListMaster");
						TER_qry.append(" p where ");
						TER_qry.append(" p.productMaster.productId= :productId and " +								
								" p.devicePlatformVersionListMaster.devicePlatformMaster.devicePlatformName= :pltfm_id");
						Query hib_TER_qry = sessionFactory.getCurrentSession().createQuery(TER_qry.toString());
						
						hib_TER_qry.setParameter("pltfm_id", pltfm_id);
						hib_TER_qry.setParameter("productId", productId);
						
						int pageSize=500;						
					
						productVersionListMaster=hib_TER_qry.setFirstResult(0).setMaxResults(pageSize).list();
						//
					for(ProductVersionListMaster prd_ver:productVersionListMaster){					
						//From run list
						StringBuffer TER_runno_qry= new StringBuffer("from TestExecutionResult");
						TER_runno_qry.append(" e where ");
						TER_runno_qry.append(" e.testRunList.testRunConfigurationChild.productVersionListMaster.productMaster.productId= :productId");
						TER_runno_qry.append(" AND ");
						TER_runno_qry.append(" e.testRunList.testRunConfigurationChild.productVersionListMaster.devicePlatformVersionListMaster.devicePlatformMaster.devicePlatformName=:platformName");
						TER_runno_qry.append(" group by e.testRunList.runNo");
						Query hib_TER_runno_qry = sessionFactory.getCurrentSession().createQuery(TER_runno_qry.toString());
						hib_TER_runno_qry.setParameter("productId", productId);
						TEResult_runno=hib_TER_runno_qry.list();
						for(TestExecutionResult terr:TEResult_runno){
							if(TR_runno_list !=null){
								TestRunList terr_tr = terr.getTestRunList();
								Integer ter_runno=terr_tr.getRunNo();
								
								if(!TR_runno_list.contains(ter_runno)){
									TR_list.add(terr_tr);
									TR_runno_list.add(ter_runno);									
								}else{
									//Nothing
								}								
							}
						}						
					}					
					
				log.debug("Max Run No fetch successful");
			} catch (RuntimeException re) {
				log.error("Max Run No fetch failed", re);			
				//throw re;
			}			
		}else{			
			try {
				
				max=((Number) sessionFactory.getCurrentSession().createSQLQuery("select max(lastRunNo) from test_run_configuration_child").uniqueResult()).intValue();				
				log.debug("Max Run No fetch successful");
			} catch (RuntimeException re) {
				log.error("Max Run No fetch failed", re);			
				//throw re;
			}			
		}
		return TR_list;
	}	
		
	@Override
	@Transactional
	public boolean isTestConfigurationChildExistingByName(String testRunConfigurationName) {

		String hql = "from TestRunConfigurationChild c where c.testRunConfigurationName =:name";
		List instances = sessionFactory.getCurrentSession().createQuery(hql).setParameter("name", testRunConfigurationName).list();
		if (instances != null  && !instances.isEmpty()) 
		    return true;
		else 
			return false;
	}
	
	@Override
	@Transactional
	public List<TestEnvironmentDevices> listTestEnvironmentsOfTestRunConfigurationChild(int testRunConfigurationChildId) {

		log.debug("Inside addTestEnvironmentToTestRunConfigurationChild");
		TestRunConfigurationChild childConfig = null;
		List<TestEnvironmentDevices> environments=null;
		try {
			childConfig = (TestRunConfigurationChild) sessionFactory.getCurrentSession().get(TestRunConfigurationChild.class, testRunConfigurationChildId);
			if (childConfig == null) {
				log.debug("TestRunChildConfiguration with specified id not found : " + testRunConfigurationChildId);
				return null;
			}
			Set<TestEnvironmentDevices> testEnvironments = childConfig.getTestEnvironmentDeviceses();
			for(TestEnvironmentDevices Ted:testEnvironments)
			{
				Hibernate.initialize(Ted);
			}
				environments=new ArrayList<TestEnvironmentDevices>();
				environments.addAll(testEnvironments);
				log.debug("Found TestRunConfigChild by ID successfully");
			} catch (RuntimeException re) {
				log.error("Failed to find TestRunConfigChild by ID", re);
				//throw re;
			}
		return environments;
	}

	@Override
	@Transactional
	public boolean addTestEnvironmentToTestRunConfigurationChild(int testRunConfigurationChildId, int testEnvironmentId) {

		log.debug("Inside addTestEnvironmentToTestRunConfigurationChild");
		TestEnvironmentDevices testEnvironment = null;
		TestRunConfigurationChild childConfig = null;
		List<DeviceList> deviceList=null;
		try {
		
			childConfig = (TestRunConfigurationChild) sessionFactory.getCurrentSession().get(TestRunConfigurationChild.class,testRunConfigurationChildId);
			if (childConfig == null) {
				
				log.debug("TestRunChildConfiguration with specified id not found : " + testRunConfigurationChildId);
				return false;
			}

			testEnvironment = (TestEnvironmentDevices) sessionFactory.getCurrentSession().get(TestEnvironmentDevices.class, testEnvironmentId);
			if (testEnvironment == null) {
				log.debug("Test Environment with specified id not found : " + testEnvironmentId);
				return false;
			}
			
			Set<TestEnvironmentDevices> testEnvironments = childConfig.getTestEnvironmentDeviceses();
			testEnvironments.add(testEnvironment);
			childConfig.setTestEnvironmentDeviceses(testEnvironments);
			sessionFactory.getCurrentSession().save(childConfig);
			log.error("Added TestEnvironment to Child Configuration");
		} catch (RuntimeException re) {
			log.error("Failed to add TestEnvironment to Child Configuration", re);
			return false;
		}
		return true;
	}

	@Override
	@Transactional
	public boolean removeTestEnvironmentFromTestRunConfigurationChild(int testRunConfigurationChildId, int testEnvironmentId) {

		log.debug("Inside removeTestEnvironmentToTestRunConfigurationChild");
		TestEnvironmentDevices testEnvironment = null;
		TestRunConfigurationChild childConfig = null;
		List<DeviceList> deviceList=null;
		try {
		
			childConfig = (TestRunConfigurationChild) sessionFactory.getCurrentSession().get(TestRunConfigurationChild.class, testRunConfigurationChildId);
			if (childConfig == null) {
				log.debug("TestRunChildConfiguration with specified id not found : " + testRunConfigurationChildId);
				return false;
			}

			testEnvironment = (TestEnvironmentDevices) sessionFactory.getCurrentSession().get(TestEnvironmentDevices.class, testEnvironmentId);
			if (testEnvironment == null) {
				log.debug("Test Environment with specified id not found : " + testEnvironmentId);
				return false;
			}
			
			Set<TestEnvironmentDevices> testEnvironments = childConfig.getTestEnvironmentDeviceses();
			testEnvironments.remove(testEnvironment);
			childConfig.setTestEnvironmentDeviceses(testEnvironments);
			sessionFactory.getCurrentSession().save(childConfig);
			log.error("Removed TestEnvironment from Child Configuration");
		} catch (RuntimeException re) {
			log.error("Failed to remove TestEnvironment from Child Configuration", re);
			return false;
		}
		return true;
	}
}
