package com.hcl.atf.taf.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.constants.TAFConstants;
import com.hcl.atf.taf.dao.DefectManagementSystemDAO;
import com.hcl.atf.taf.model.DefectManagementSystem;
import com.hcl.atf.taf.model.DefectManagementSystemMapping;
import com.hcl.atf.taf.model.Environment;
import com.hcl.atf.taf.model.SCMSystem;
import com.hcl.atf.taf.model.TestExecutionResultBugList;
import com.hcl.atf.taf.model.dto.DefectReportDTO;

@Repository
public class DefectManagementSystemDAOImpl implements DefectManagementSystemDAO {

	private static final Log log = LogFactory.getLog(DefectManagementSystemDAOImpl.class);

	@Autowired(required=true)
	private SessionFactory sessionFactory;

	@Override
	@Transactional
	public void add(DefectManagementSystem defectManagementSystem) {
		log.debug("adding DefectManagementSystem instance");
		try {
			sessionFactory.getCurrentSession().save(defectManagementSystem);
			log.debug("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);			
		}
		log.info("Added DefectManagementSystem entity successfully");
	}

	@Override
	@Transactional
	public void update(DefectManagementSystem defectManagementSystem) {
		log.info("Updating DefectManagementSystem entity");
		try{
			sessionFactory.getCurrentSession().saveOrUpdate(defectManagementSystem);	
		} catch(RuntimeException re){
			log.error("Error in updating defectManagementSystem : ", re);
		}
		log.info("Updated DefectManagementSystem entity successfully");
	}

	@Override
	@Transactional
	public void delete(DefectManagementSystem defectManagementSystem) {
		log.info("Deleting DefectManagementSystem entity");
		try{
			sessionFactory.getCurrentSession().delete(defectManagementSystem);	
			log.info("Deleted DefectManagementSystem entity successfully");	
		} catch(Exception e){
			log.error("Error in deleting defectManagementSystem : ", e);
		}		
	}

	@Override
	@Transactional
	public List<DefectManagementSystem> listDefectManagementSystem(int productId){
		log.info("List all the DefectManagementSystem entities for a product");
		List<DefectManagementSystem> defectManagementSystems = null;
		try{
			defectManagementSystems = sessionFactory.getCurrentSession().createQuery("from DefectManagementSystem where productId=:productId").setParameter("productId",productId).list();
			if(defectManagementSystems!= null && !defectManagementSystems.isEmpty())
				for(DefectManagementSystem defectManagementSystem :defectManagementSystems ){
					Hibernate.initialize(defectManagementSystem.getProductMaster());
				}			
		} catch(Exception e){
			log.error("Error in listing defectManagementSystem : ", e);
		}		
		return defectManagementSystems;
	}

	@Override
	@Transactional
	public int getTotalRecordsOfDefectManagementSystem(int productId) {
		log.debug("getting defectManagement System total records");
		int count =0;
		try {
			count=((Number) sessionFactory.getCurrentSession().createSQLQuery("select count(*) from defect_management_system where productId=:productId").setParameter("productId", productId).uniqueResult()).intValue();
			log.debug("total records fetch successful");
		} catch (RuntimeException re) {
			log.error("total records fetch failed", re);			
			//throw re;
		}
		return count;
	}

	@Override
	@Transactional
	public DefectManagementSystem getByDefectManagementSystemId(int defectManagementSystemId) {

		log.info("Get the DefectManagementSystem entities by ID");
		List<DefectManagementSystem> defectManagementSystems = null;
		DefectManagementSystem defectManagementSystem = null;
		try{
			defectManagementSystems = sessionFactory.getCurrentSession().createQuery("from DefectManagementSystem where defectManagementSystemId=:defectManagementSystemId").setParameter("defectManagementSystemId",defectManagementSystemId).list();
			defectManagementSystem = (defectManagementSystems!= null && !defectManagementSystems.isEmpty())?defectManagementSystems.get(0):null;
			if(defectManagementSystem != null){
				Hibernate.initialize(defectManagementSystem.getProductMaster());
				Hibernate.initialize(defectManagementSystem.getDefectManagementSystemMappings());

			}			
		} catch(Exception e){
			log.error("Error in getting defectManagementSystem by ID", e);
		}		
		return defectManagementSystem;
	}

	@Override
	@Transactional
	public boolean isDefectManagementSystemExistingByTitle(DefectManagementSystem defectManagementSystem) {
		String hql = "from DefectManagementSystem dms where title = :title";
		List list = sessionFactory.getCurrentSession().createQuery(hql).setParameter("title", defectManagementSystem.getTitle()).list();
		if (list != null  && !list.isEmpty()) 
			return true;
		else 
			return false;
	}

	@Override
	@Transactional
	public DefectManagementSystemMapping getByDefectManagementSystemMappingId(int defectManagementSystemMappingId) {

		log.info("Get the DefectManagementSystem Mapping entities by Id");
		List<DefectManagementSystemMapping> defectManagementSystemMappings = null;
		DefectManagementSystemMapping defectManagementSystemMapping = null;
		try{
			log.info("inside the defecrmgmtmapping:");
			defectManagementSystemMappings = sessionFactory.getCurrentSession().createQuery("from DefectManagementSystemMapping where defectManagementSystemMappingId=:defectManagementSystemMappingId").setParameter("defectManagementSystemMappingId",defectManagementSystemMappingId).list();
			log.info("inside the defecrmgmtmapping:"+defectManagementSystemMappings.size());
			defectManagementSystemMapping = (defectManagementSystemMappings!= null && !defectManagementSystemMappings.isEmpty())?defectManagementSystemMappings.get(0):null;
			if(defectManagementSystemMappings != null){
				Hibernate.initialize(defectManagementSystemMapping.getDefectManagementSystem());
				Hibernate.initialize(defectManagementSystemMapping.getProductMaster());
			}			
		} catch(Exception e){
			log.error("Error in getting defectManagementSystem Mapping by ID : ", e);
		}		
		return defectManagementSystemMapping;
	}
	@Override
	@Transactional
	public int getDefectManagementSystemId(int productId, String defectSystemName, String defectSystemVersion) {

		log.info("Get the DefectManagementSystem entities by ID");

		int defectManagementSystemId = -1;
		List<DefectManagementSystem> defectManagementSystems = null;
		DefectManagementSystem defectManagementSystem = null;
		try{
			defectManagementSystems = sessionFactory.getCurrentSession().createQuery("from DefectManagementSystem where productId=:productId").setParameter("productId", productId).list();
			defectManagementSystem = (defectManagementSystems!= null && !defectManagementSystems.isEmpty())?defectManagementSystems.get(0):null;
			if(defectManagementSystem != null){
				Hibernate.initialize(defectManagementSystem.getProductMaster());
			}			
			defectManagementSystemId = defectManagementSystem.getDefectManagementSystemId();
		} catch(Exception e){
			log.error("Error in getting defectManagementSystem by ID");
		}		
		return defectManagementSystemId;	
	}

	@Override
	@Transactional
	public void addDefectManagementSystemMapping(DefectManagementSystemMapping defectManagementSystemMapping) {
		log.debug("adding DefectManagement System Mapping instance");
		try {
			sessionFactory.getCurrentSession().save(defectManagementSystemMapping);
			log.debug("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
			//throw re;
		}
	}

	@Override
	@Transactional
	public void updateDefectManagementSystemMapping(DefectManagementSystemMapping defectManagementSystemMapping) {
		log.info("Updating DefectManagementSystemMapping entity");
		try{
			sessionFactory.getCurrentSession().saveOrUpdate(defectManagementSystemMapping);	
		} catch(Exception e){
			log.error("Error in updating defectManagementSystem Mapping : ", e);
		}
		log.info("Updated DefectManagementSystemMapping entity successfully");

	}

	@Override
	@Transactional
	public void deleteDefectManagementSystemMapping(DefectManagementSystemMapping defectManagementSystemMapping) {
		log.info("Deleting DefectManagementSystem mapping entity");
		try{
			log.info("inside the dao impl of deleteDefectManagementSystemMapping :"+defectManagementSystemMapping.getDefectManagementSystemMappingId());
			sessionFactory.getCurrentSession().delete(defectManagementSystemMapping);	
			log.info("Deleted DefectManagementSystem entity successfully");
		} catch(Exception e){
			log.error("Error in deleting defectManagementSystem : ", e);
		}
	}

	@Override
	@Transactional
	public List<DefectManagementSystemMapping> listDefectManagementSystemMapping(int defectManagementSystemId) {

		log.info("List all the DefectManagementSystem Mapping entities");
		List<DefectManagementSystemMapping> defectManagementSystemsMappings = new ArrayList<DefectManagementSystemMapping>();
		try{
			defectManagementSystemsMappings = sessionFactory.getCurrentSession().createQuery("from DefectManagementSystemMapping where defectManagementSystemId=:defectManagementSystemId").setParameter("defectManagementSystemId",defectManagementSystemId).list();
			if(defectManagementSystemsMappings!= null && !defectManagementSystemsMappings.isEmpty())
				for(DefectManagementSystemMapping defectManagementSystemMapping:defectManagementSystemsMappings ){
					Hibernate.initialize(defectManagementSystemMapping.getDefectManagementSystem());
					Hibernate.initialize(defectManagementSystemMapping.getProductMaster());
				}			
		} catch(Exception e){
			log.error("Error in listing defectManagementSystem Mappings : ", e);
		}		
		return defectManagementSystemsMappings;
	}




	@Override
	@Transactional
	public String getDefectSystemMappingValue(int defectManagementSystemId,int productId, String mappingType) {

		return null;
	}

	@Override
	@Transactional
	public String getDefectSystemMappingProductName(int defectManagementSystemId, int productId) {

		log.info("Get the DefectManagementSystem Product Name");		
		String defectSystemProductName = "";
		String mappingType = TAFConstants.ENTITY_PRODUCT;
		List<DefectManagementSystemMapping> DefectManagementSystemMappings = null;
		DefectManagementSystemMapping defectManagementSystemMapping = null;
		try{
			DefectManagementSystemMappings = sessionFactory.getCurrentSession().createQuery("from DefectManagementSystemMapping  where defectManagementSystemId=:defectManagementSystemId and productId=:productId and mappingType=:mappingType")
					.setParameter("defectManagementSystemId", defectManagementSystemId)
					.setParameter("productId", productId)
					.setParameter("mappingType", mappingType).list();

			defectManagementSystemMapping = (DefectManagementSystemMappings!= null && !DefectManagementSystemMappings.isEmpty())?DefectManagementSystemMappings.get(0):null;
			if(defectManagementSystemMapping != null){
				Hibernate.initialize(defectManagementSystemMapping.getProductMaster());
				Hibernate.initialize(defectManagementSystemMapping.getDefectManagementSystem());
			}			
			defectSystemProductName = defectManagementSystemMapping.getMappingValue();
		} catch(Exception e){
			log.error("Error in getting DefectManagementSystem Product Name : ", e);
		}		
		return defectSystemProductName;
	}

	@Override
	@Transactional
	public String getDefectSystemMappingProductVersion(int defectManagementSystemId, int productId) {

		log.info("Get the DefectManagementSystem Product Version");		
		String defectSystemProductVersion = "";
		String mappingType = TAFConstants.ENTITY_PRODUCT_VERSION;
		List<DefectManagementSystemMapping> DefectManagementSystemMappings = null;
		DefectManagementSystemMapping defectManagementSystemMapping = null;
		try{
			DefectManagementSystemMappings = sessionFactory.getCurrentSession().createQuery("from DefectManagementSystemMapping where defectManagementSystemId=:defectManagementSystemId and productId=:productId and mappingType=:mappingType")					
					.setParameter("defectManagementSystemId", defectManagementSystemId)
					.setParameter("productId", productId)
					.setParameter("mappingType", mappingType).list();

			defectManagementSystemMapping = (DefectManagementSystemMappings!= null && !DefectManagementSystemMappings.isEmpty())?DefectManagementSystemMappings.get(0):null;
			if(defectManagementSystemMapping != null){
				Hibernate.initialize(defectManagementSystemMapping.getProductMaster());
				Hibernate.initialize(defectManagementSystemMapping.getDefectManagementSystem());
			}			
			defectSystemProductVersion = defectManagementSystemMapping.getMappingValue();
		} catch(Exception e){
			log.error("Error in getting DefectManagementSystem Product version", e);
		}		
		return defectSystemProductVersion;
	}

	@Override
	@Transactional
	public DefectManagementSystem getDMSByProductId(int productId, int dms) {
		// TODO Auto-generated method stub
		log.info("Get the DefectManagementSystem entities by ID");

		List<DefectManagementSystem> defectManagementSystems = null;
		DefectManagementSystem defectManagementSystem = null;
		try{
			defectManagementSystems = sessionFactory.getCurrentSession().createQuery("from DefectManagementSystem where productId=:productId and defectManagementSystemId=:dms").setParameter("productId", productId).setParameter("dms", dms).list();
			defectManagementSystem = (defectManagementSystems!= null && !defectManagementSystems.isEmpty())?defectManagementSystems.get(0):null;
			if(defectManagementSystem != null){
				Hibernate.initialize(defectManagementSystem.getProductMaster());
			}			
		} catch(Exception e){
			log.error("Error in getting defectManagementSystem by ID : ", e);
		}		
		return defectManagementSystem;	

	}

	@Override
	@Transactional
	public DefectManagementSystem getPrimaryDMSByProductId(int productId) {
		// TODO Auto-generated method stub
		log.info("Get the DefectManagementSystem entities by ID");

		List<DefectManagementSystem> defectManagementSystems = null;
		DefectManagementSystem defectManagementSystem = null;
		try{
			defectManagementSystems = sessionFactory.getCurrentSession().createQuery("from DefectManagementSystem where productId=:productId and isPrimary=:isPrimary").setParameter("productId", productId).setParameter("isPrimary", 1).list();
			defectManagementSystem = (defectManagementSystems!= null && !defectManagementSystems.isEmpty())?defectManagementSystems.get(0):null;
			if(defectManagementSystem != null){
				Hibernate.initialize(defectManagementSystem.getProductMaster());
			}			
		} catch(Exception e){
			log.error("Error in getting defectManagementSystem by ID : ", e);
		}		
		return defectManagementSystem;	
	}

	@Override
	@Transactional
	public Integer countAllBugs(Date startDate, Date endDate) {
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestExecutionResultBugList.class,"bugList");
			if (startDate != null) {
				c.add(Restrictions.ge("bugList.bugCreationTime", startDate));
			}
			if (endDate != null) {
				c.add(Restrictions.le("bugList.bugCreationTime", endDate));
			}

			c.setProjection(Projections.rowCount());
			Integer count = Integer.parseInt(c.uniqueResult().toString());
			return count;
		} catch (Exception e) {
			log.error("Unable to get count of all BugList", e);
			return -1;
		}
	}

	public List<DefectReportDTO> defectSystemNameAndCodeDao(int testExecutionResultsBugId){	

		log.info("Get the DefectManagementSystem Name and System code by testExecutionResultBugId ID");

		DefectReportDTO defectReportDto = null;
		List<DefectReportDTO> defectReportDtoList = new ArrayList<DefectReportDTO>();
		String sql="";
		try{
			sql =	"SELECT tim.name AS defectSystemName, dms.connectionUri AS defectSystemURI, ded.defectSystemCode AS defectSystemCode,ded.defectExportDate AS defectExportDate "
					+ "FROM defect_management_system AS dms JOIN defect_export_data ded " 
					+ "ON dms.defectManagementSystemId = ded.defectManagementSystemId join tool_intagration_master tim on dms.toolintagrationid=tim.id WHERE ded.testExecutionResultsBugId = "+testExecutionResultsBugId+"";

			defectReportDtoList = ((SQLQuery) sessionFactory.getCurrentSession().createSQLQuery(sql))
					.addScalar("defectSystemName",StandardBasicTypes.STRING)
					.addScalar("defectSystemCode",StandardBasicTypes.STRING)
					.addScalar("defectExportDate",StandardBasicTypes.STRING)
					.addScalar("defectSystemURI",StandardBasicTypes.STRING)
					.setResultTransformer(Transformers.aliasToBean(DefectReportDTO.class)).list();
		}			
		catch(HibernateException e){
			log.error("Error in getting defectManagementSystem Name and System code by testExecutionResultsBugId :", e);
		}	
		return defectReportDtoList;
	}

	@Override
	@Transactional
	public void addSCMSystem(SCMSystem scmSystem) {
		log.debug("adding SCMSystem instance");
		try {
			sessionFactory.getCurrentSession().save(scmSystem);
			log.debug("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);			
		}
		log.info("Added SCMSystem entity successfully");

	}

	@Override
	@Transactional
	public void updateSCMSystem(SCMSystem scmSystem) {
		log.info("Updating SCMSystem entity");
		try{
			sessionFactory.getCurrentSession().saveOrUpdate(scmSystem);	
		} catch(RuntimeException re){
			log.error("Error in updating scmSystem : ", re);
		}
		log.info("Updated SCMSystem entity successfully");

	}

	@Override
	@Transactional
	public List<SCMSystem> listSCMManagementSystem(int productId) {
		log.info("List all the SCMSystem entities for a product");
		List<SCMSystem> scmSystems = null;
		try{
			scmSystems = sessionFactory.getCurrentSession().createQuery("from SCMSystem where productId=:productId").setParameter("productId",productId).list();
			if(scmSystems!= null && !scmSystems.isEmpty())
				for(SCMSystem scmSystem : scmSystems ){
					Hibernate.initialize(scmSystem.getProductMaster());
				}			
		} catch(Exception e){
			log.error("Error in listing SourceControlManagementSystem : ", e);
		}		
		return scmSystems;
	}

	@Override
	@Transactional
	public String deleteSCMSystem(int id) {
		log.info("Deleting SCMSystem entity");
		String message="Delete all references which are mapped with SCMSystem and try again";
		try{			
				String deleteSCM = "Delete from SCMSystem where id=:scmSystemId";
				int result = sessionFactory.getCurrentSession().createQuery(deleteSCM).setInteger("scmSystemId", id).executeUpdate();
				if(result > 0){					
					message="OK";
					log.info("deletion of SCMSystem successful");
				}
		} catch(RuntimeException re){
			log.error("Error in deleting scmSystem : ", re);
		}
		return message;
	}

	@Override
	@Transactional
	public SCMSystem getBySCMSystemId(int id) {

		log.info("Get the SCMSystem entity by ID");
		List<SCMSystem> scmSystems = null;
		SCMSystem scmSystem = null;
		try{
			scmSystems = sessionFactory.getCurrentSession().createQuery("from SCMSystem where id=:id").setParameter("id",id).list();
			scmSystem = (scmSystems!= null && !scmSystems.isEmpty())?scmSystems.get(0):null;
			if(scmSystem != null){
				Hibernate.initialize(scmSystem.getProductMaster());
				//Hibernate.initialize(defectManagementSystem.getDefectManagementSystemMappings());
			}			
		} catch(Exception e){
			log.error("Error in getting SourceControlManagementSystem by ID", e);
		}		
		return scmSystem;
	}

	@Override
	@Transactional
	public int getTotalRecordsOfSCMManagementSystems(int productId) {
		log.debug("getting SourceControlManagement System total records");
		int count =0;
		try {
			count=((Number) sessionFactory.getCurrentSession().createSQLQuery("select count(*) from scm_management_system where productId=:productId").setParameter("productId", productId).uniqueResult()).intValue();
			log.debug("total records fetch successful");
		} catch (RuntimeException re) {
			log.error("total records fetch failed", re);			
			//throw re;
		}
		return count;
	}

	@Override
	@Transactional
	public SCMSystem getSCMSystemByNameAndProductId(Integer productId,String scmSystemName) {		
		SCMSystem scmSystem = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(SCMSystem.class);
			List scmSystemList = c.add(Restrictions.like("title", scmSystemName)).add(Restrictions.eq("productMaster.productId", productId)).list();
			scmSystem = (scmSystemList != null && scmSystemList.size()>0) ? (SCMSystem) scmSystemList.get(0) : null;
			log.debug("getSCMSystemByName success");
		} catch (RuntimeException re) {
			log.error("getSCMSystemByName failed", re);
		}
		return scmSystem;
	}
	@Override
	@Transactional
	public List<SCMSystem> listSCMManagementSystemByStatus(int productId,Integer status){
		log.debug("listing all SCM Systems for a product");		
		List<SCMSystem> scmList = null;

		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(SCMSystem.class, "scm");
			c.createAlias("scm.productMaster", "pm");
			if(productId!=0){
			c.add(Restrictions.eq("pm.productId", productId));
			}
			if(status ==2){
				c.add(Restrictions.in("scm.status", Arrays.asList(0,1)));
			}else {
				c.add(Restrictions.eq("scm.status", status));
			}
			scmList = c.list();
			
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return scmList;
	}

}
