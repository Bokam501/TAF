package com.hcl.atf.taf.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.TestManagementSystemDAO;
import com.hcl.atf.taf.model.TestManagementSystem;
import com.hcl.atf.taf.model.TestManagementSystemMapping;
import com.hcl.atf.taf.model.dto.WorkPackageTCEPSummaryDTO;

@Repository
public class TestManagementSystemDAOImpl implements TestManagementSystemDAO {
	
	private static final Log log = LogFactory.getLog(TestManagementSystemDAOImpl.class);

	@Autowired(required=true)
    private SessionFactory sessionFactory;
	
	@Override
	@Transactional
	public void add(TestManagementSystem testManagementSystem) {
		log.info("Adding TestManagementSystem entity");
		try{
			sessionFactory.getCurrentSession().save(testManagementSystem);	
		} catch(RuntimeException re){
			log.error("Error in adding testManagementSystem : ", re);
		}
		log.info("Added TestManagementSystem entity successfully");
	}

	@Override
	@Transactional
	public void update(TestManagementSystem testManagementSystem) {
		log.info("Updating TestManagementSystem entity");
		try{
			sessionFactory.getCurrentSession().saveOrUpdate(testManagementSystem);	
		} catch(RuntimeException re){
			log.error("Error in updating testManagementSystem : ", re);
		}
		log.info("Updated TestManagementSystem entity successfully");
	}

	@Override
	@Transactional
	public void delete(TestManagementSystem testManagementSystem) {
		log.info("Deleting TestManagementSystem entity");
		try{
			sessionFactory.getCurrentSession().delete(testManagementSystem);	
		} catch(Exception e){
			log.error("Error in deleting testManagementSystem : ", e);
		}
		log.info("Deleted TestManagementSystem entity successfully");	
	}
	
	@Override
	@Transactional
	public int getTotalRecordsCount(int productId) {
		log.debug("getting testManagement System total records");
		int count =0;
		try {
			count=((Number) sessionFactory.getCurrentSession().createSQLQuery("select count(*) from test_management_system where productId=:productId").setParameter("productId", productId).uniqueResult()).intValue();
			log.debug("total records fetch successful");
		} catch (RuntimeException re) {
			log.error("total records fetch failed", re);			
		}
		return count;
	}

	@Override
	@Transactional
	public List<TestManagementSystem> listTestManagementSystem(int productId){
		log.info("List all the TestManagementSystem entities for a product");
		List<TestManagementSystem> testManagementSystems = null;
		try{
			testManagementSystems = sessionFactory.getCurrentSession().createQuery("from TestManagementSystem where productId=:productId").setParameter("productId",productId).list();
			if(testManagementSystems!= null && !testManagementSystems.isEmpty())
			for(TestManagementSystem testManagementSystem :testManagementSystems ){
				Hibernate.initialize(testManagementSystem.getProductMaster());
			}			
		} catch(Exception e){
			log.error("Error listing TestManagementSystem : ", e);
		}		
		return testManagementSystems;
	}

	@Override
	@Transactional
	public TestManagementSystem getByTestManagementSystemId(int testManagementSystemId) {
		
		log.info("Get the TestManagementSystem entities by ID");
		List<TestManagementSystem> testManagementSystems = null;
		TestManagementSystem testManagementSystem = null;
		try{
			testManagementSystems = sessionFactory.getCurrentSession().createQuery("from TestManagementSystem where testManagementSystemId=:testManagementSystemId").setParameter("testManagementSystemId",testManagementSystemId).list();
			testManagementSystem = (testManagementSystems!= null && !testManagementSystems.isEmpty())?testManagementSystems.get(0):null;
			if(testManagementSystem != null){
				Hibernate.initialize(testManagementSystem.getTestManagementSystemMappings());
				Hibernate.initialize(testManagementSystem.getProductMaster());
				Hibernate.initialize(testManagementSystem.getProductMaster().getProductName());
				Hibernate.initialize(testManagementSystem.getProductMaster().getTestManagementSystems());
				Set<TestManagementSystem> tmslist=testManagementSystem.getProductMaster().getTestManagementSystems();
				for (TestManagementSystem tms : tmslist) {
					Hibernate.initialize(tms.getTestManagementSystemMappings());
					Hibernate.initialize(tms.getProductMaster());
					Hibernate.initialize(tms.getProductMaster().getProjectName());
				}
			}			
		} catch(Exception e){
			log.error("Error in getting testManagementSystem by ID ",e);
		}		
		return testManagementSystem;
	}

	@Override
	@Transactional
	public TestManagementSystem getByProductId(int productId) {
		
		log.info("Get the TestManagementSystem entities by Product ID");
		List<TestManagementSystem> testManagementSystems = null;
		TestManagementSystem testManagementSystem = null;
		try{
			testManagementSystems = sessionFactory.getCurrentSession().createQuery("from TestManagementSystem where productId =: productId").setParameter("productId",productId).list();
			testManagementSystem = (testManagementSystems!= null && !testManagementSystems.isEmpty())?testManagementSystems.get(0):null;
			if(testManagementSystem != null){
				Hibernate.initialize(testManagementSystem.getProductMaster());				
			}			
		} catch(Exception e){
			log.error("Error in getting testManagementSystem by Product ID : " , e);
		}		
		return testManagementSystem;
	}
	
	@Override
	@Transactional
	public int getTestManagementSystemId(int productId, String testSystemName, String testSystemVersion) {
		
		log.info("Get the TestManagementSystem entities by ID");
		
		int testManagementSystemId = -1;
		List<TestManagementSystem> testManagementSystems = null;
		TestManagementSystem testManagementSystem = null;
		try{
			testManagementSystems = sessionFactory.getCurrentSession().createQuery("from TestManagementSystem where productId=:productId and testSystemName=:testSystemName and testSystemVersion=:testSystemVersion")
					.setParameter("productId", productId)
					.setParameter("testSystemName",testSystemName)
					.setParameter("testSystemVersion",testSystemVersion).list();
			
			testManagementSystem = (testManagementSystems!= null && !testManagementSystems.isEmpty())?testManagementSystems.get(0):null;
			if(testManagementSystem != null){
				Hibernate.initialize(testManagementSystem.getProductMaster());
			}			
			testManagementSystemId = testManagementSystem.getTestManagementSystemId();
		} catch(Exception e){
			log.error("Error in getting testManagementSystem by ID : ", e);
		}		
		return testManagementSystemId;	
	}

	@Override
	@Transactional
	public void addTestManagementSystemMapping(TestManagementSystemMapping testManagementSystemMapping) {
		log.debug("adding Test Management System Mapping instance");
		try {
			sessionFactory.getCurrentSession().save(testManagementSystemMapping);
			log.debug("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}
	}

	@Override
	@Transactional
	public void updateTestManagementSystemMapping(TestManagementSystemMapping testManagementSystemMapping) {
		log.info("Updating Test ManagementSystemMapping entity");
		try{
			sessionFactory.getCurrentSession().saveOrUpdate(testManagementSystemMapping);	
		} catch(Exception e){
			log.error("Error in updating Test ManagementSystem Mapping : ", e);
		}
		log.info("Updated Test ManagementSystemMapping entity successfully");
	}

	@Override
	@Transactional
	public void deleteTestManagementSystemMapping(TestManagementSystemMapping testManagementSystemMapping) {
		log.info("Deleting DefectManagementSystem mapping entity");
		try{
			log.info("inside the dao impl of deleteDefectManagementSystemMapping :"+testManagementSystemMapping.getTestManagementSystemMappingId());
			sessionFactory.getCurrentSession().delete(testManagementSystemMapping);	
			log.info("Deleted Test ManagementSystem entity successfully");
		} catch(Exception e){
			log.error("Error in deleting Test ManagementSystem : ", e);
		}
	}
	
	@Override
	@Transactional
	public List<TestManagementSystemMapping> listTestManagementSystemMapping(
			int testManagementSystemId) {
		log.info("List all the TestManagementSystem Mapping entities");
		List<TestManagementSystemMapping> testManagementSystemsMappings = new ArrayList<TestManagementSystemMapping>();
		try{
			testManagementSystemsMappings = sessionFactory.getCurrentSession().createQuery("from TestManagementSystemMapping where testManagementSystemId=:testManagementSystemId")
					.setParameter("testManagementSystemId",testManagementSystemId).list();
			if(testManagementSystemsMappings!= null && !testManagementSystemsMappings.isEmpty())
				for(TestManagementSystemMapping testManagementSystemMapping:testManagementSystemsMappings ){
					Hibernate.initialize(testManagementSystemMapping.getTestManagementSystem());
					Hibernate.initialize(testManagementSystemMapping.getProductMaster());
				}			
		} catch(Exception e){
			log.error("Error in listing defectManagementSystem Mappings : ", e);
		}		
		return testManagementSystemsMappings;
	}

	@Override
	@Transactional
	public TestManagementSystemMapping getByTestManagementSystemMappingId(int testManagementSystemMappingId) {
		log.info("Get the Test ManagementSystem Mapping entities by Id");
		List<TestManagementSystemMapping> testManagementSystemMappings = null;
		TestManagementSystemMapping testManagementSystemMapping = null;
		try{
			log.info("inside the test mgmt mapping:");
			testManagementSystemMappings = sessionFactory.getCurrentSession().createQuery("from TestManagementSystemMapping where testManagementSystemMappingId=:testManagementSystemMappingId").setParameter("testManagementSystemMappingId",testManagementSystemMappingId).list();
			log.info("inside the test mgmt mapping:"+testManagementSystemMappings.size());
			testManagementSystemMapping = (testManagementSystemMappings!= null && !testManagementSystemMappings.isEmpty())?testManagementSystemMappings.get(0):null;
			if(testManagementSystemMappings != null){
				Hibernate.initialize(testManagementSystemMapping.getTestManagementSystem());
				Hibernate.initialize(testManagementSystemMapping.getProductMaster());
			}			
		} catch(Exception e){
			log.error("Error in getting test Management System Mapping by ID : ", e);
		}		
		return testManagementSystemMapping;
	}

	@Override
	@Transactional
	public String getTestSystemMappingValue(int testManagementSystemId,
			int productId, String mappingType) {
			
		return null;
	}

	@Override
	@Transactional
	public String getTestSystemMappingProductName(int testManagementSystemId, int productId) {
	
		log.info("Get the TestManagementSystem Product Name");		
		String testSystemProductName = "";
		String mappingType = "product";
		List<TestManagementSystemMapping> TestManagementSystemMappings = null;
		TestManagementSystemMapping testManagementSystemMapping = null;
		try{
			TestManagementSystemMappings = sessionFactory.getCurrentSession().createQuery("from TestManagementSystemMapping  where testManagementSystemId=:testManagementSystemId and productId=:productId and mappingType=:mappingType")
					.setParameter("testManagementSystemId", testManagementSystemId)
					.setParameter("productId", productId)
					.setParameter("mappingType", mappingType).list();
			
			testManagementSystemMapping = (TestManagementSystemMappings!= null && !TestManagementSystemMappings.isEmpty())?TestManagementSystemMappings.get(0):null;
			if(testManagementSystemMapping != null){
				Hibernate.initialize(testManagementSystemMapping.getProductMaster());
				Hibernate.initialize(testManagementSystemMapping.getTestManagementSystem());
			}			
			testSystemProductName = testManagementSystemMapping.getMappingValue();
		} catch(Exception e){
			log.error("Error in getting TestManagementSystem Product Name : ", e);
		}		
		return testSystemProductName;
	}

	@Override
	@Transactional
	public String getTestSystemMappingProductVersion(int testManagementSystemId, int productId) {
		
		log.info("Get the TestManagementSystem Product Version");		
		String testSystemProductVersion = "";
		String mappingType = "productVersion";
		List<TestManagementSystemMapping> TestManagementSystemMappings = null;
		TestManagementSystemMapping testManagementSystemMapping = null;
		try{
			TestManagementSystemMappings = sessionFactory.getCurrentSession().createQuery("from TestManagementSystemMapping where testManagementSystemId=:testManagementSystemId and productId=:productId and mappingType=:mappingType")					
					.setParameter("testManagementSystemId", testManagementSystemId)
					.setParameter("productId", productId)
					.setParameter("mappingType", mappingType).list();
			
			testManagementSystemMapping = (TestManagementSystemMappings!= null && !TestManagementSystemMappings.isEmpty())?TestManagementSystemMappings.get(0):null;
			if(testManagementSystemMapping != null){
				Hibernate.initialize(testManagementSystemMapping.getProductMaster());
				Hibernate.initialize(testManagementSystemMapping.getTestManagementSystem());
			}			
			testSystemProductVersion = testManagementSystemMapping.getMappingValue();
		} catch(Exception e){
			log.error("Error in getting TestManagementSystem Product version : ", e);
		}		
		return testSystemProductVersion;
	}

	@Override
	public TestManagementSystemMapping getByTestManagementSystemMappingID(
			int testManagementSystemMappingId) {
		return null;
	}

	@Override
	@Transactional
	public TestManagementSystem getTMSByProduct(int tmsId, int productId) {
		log.info("Get the TestManagementSystem entities by ID");
		List<TestManagementSystem> testManagementSystems = null;
		TestManagementSystem testManagementSystem = null;
		try{
			testManagementSystems = sessionFactory.getCurrentSession().createQuery("from TestManagementSystem tms where tms.testManagementSystemId=:testManagementSystemId and tms.productMaster.productId=:productId").setParameter("testManagementSystemId",tmsId).setParameter("productId",productId).list();
			testManagementSystem = (testManagementSystems!= null && !testManagementSystems.isEmpty())?testManagementSystems.get(0):null;
			if(testManagementSystem != null){
				Hibernate.initialize(testManagementSystem.getProductMaster());
				Hibernate.initialize(testManagementSystem.getProductMaster().getTestManagementSystems());
				Set<TestManagementSystem> tmslist=testManagementSystem.getProductMaster().getTestManagementSystems();
				for (TestManagementSystem tms : tmslist) {
					Hibernate.initialize(tms.getTestManagementSystemMappings());
					Hibernate.initialize(tms.getToolIntagration());
					
				}
			}			
		} catch(Exception e){
			log.error("Error in getting testManagementSystem by ID ", e);
		}		
		return testManagementSystem;

	}

	@Override
	@Transactional
	public TestManagementSystem getPrimaryTMSByProductId(int productId) {
		log.info("Get the TestManagementSystem entities by ID");
		List<TestManagementSystem> testManagementSystems = null;
		TestManagementSystem testManagementSystem = null;
		try{
			testManagementSystems = sessionFactory.getCurrentSession().createQuery("from TestManagementSystem tms where tms.isPrimary=:isPrimary and tms.productMaster.productId=:productId").setParameter("isPrimary",1).setParameter("productId",productId).list();
			testManagementSystem = (testManagementSystems!= null && !testManagementSystems.isEmpty())?testManagementSystems.get(0):null;
			if(testManagementSystem != null){
				Hibernate.initialize(testManagementSystem.getProductMaster());
				Hibernate.initialize(testManagementSystem.getProductMaster().getTestManagementSystems());
				Set<TestManagementSystem> tmslist=testManagementSystem.getProductMaster().getTestManagementSystems();
				for (TestManagementSystem tms : tmslist) {
					Hibernate.initialize(tms.getTestManagementSystemMappings());
					
				}
			}			
		} catch(Exception e){
			log.error("Error in getting testManagementSystem by ID : ", e);
		}		
		return testManagementSystem;

	}	
	
	@Override
	@Transactional
	public boolean checkIfTMSExists(String testManagementSystemName, int productId){
		boolean tmsExists = false;
		List<TestManagementSystem> tmsName = null;
		try{
			tmsName =  sessionFactory.getCurrentSession().createQuery("from TestManagementSystem tms where tms.testSystemName=:testManagementSystemName and tms.productMaster.productId=:productId").setParameter("testManagementSystemName",testManagementSystemName).setParameter("productId",productId).list();
			if(tmsName.size() > 0){
				tmsExists = true;
			}
		} catch(Exception e){			
			log.error("Error in retrieving TMS verification by name and productID : ", e);
		}
		return tmsExists;
	}

	@Override
	@Transactional
	public List<WorkPackageTCEPSummaryDTO> exportSystemNameCodeListDAO(Integer testCaseExecutionResultId) {
		log.info("Get the Export Management System Name and code by testRunJobId and TestCase Id");

		WorkPackageTCEPSummaryDTO exportReportDto = null;
		List<WorkPackageTCEPSummaryDTO> exportReportDtoList = new ArrayList<WorkPackageTCEPSummaryDTO>();
		String sql="";
		try{
			
			sql = 	"SELECT tms.testSystemName AS TestSystemName,tered.resultCode AS ResultCode,tered.exportedDate AS ExportedDate FROM workpackage_testcase_execution_plan AS wtep "
					+"INNER JOIN testcase_execution_result AS ter ON ter.testCaseExecutionResultId = wtep.wptcepId "
					+"LEFT JOIN test_execution_results_export_data AS tered ON tered.testExecutionsResultId = wtep.testRunJobId "
					+"INNER JOIN test_management_system AS tms ON tms.testManagementSystemId = tered.testManagementSystemId "
					+"WHERE wtep.wptcepId = "+testCaseExecutionResultId+"";	
			
			exportReportDtoList = ((SQLQuery) sessionFactory.getCurrentSession().createSQLQuery(sql))
					.addScalar("TestSystemName",StandardBasicTypes.STRING)
					.addScalar("ResultCode",StandardBasicTypes.STRING)
					.addScalar("ExportedDate",StandardBasicTypes.STRING)
					.setResultTransformer(Transformers.aliasToBean(WorkPackageTCEPSummaryDTO.class)).list();
		}			
		catch(HibernateException e){
			log.error("Error in getting Export Management System Name and code by testRunJobId and TestCase Id : ", e);
		}	
		return exportReportDtoList;
	}
	
}
