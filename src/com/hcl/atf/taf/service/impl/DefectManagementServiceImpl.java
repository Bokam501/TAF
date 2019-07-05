package com.hcl.atf.taf.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.constants.TAFConstants;
import com.hcl.atf.taf.dao.DefectManagementSystemDAO;
import com.hcl.atf.taf.dao.ProductMasterDAO;
import com.hcl.atf.taf.dao.ProductVersionListMasterDAO;
import com.hcl.atf.taf.dao.TestRunConfigurationChildDAO;
import com.hcl.atf.taf.dao.TestSuiteListDAO;
import com.hcl.atf.taf.model.DefectManagementSystem;
import com.hcl.atf.taf.model.DefectManagementSystemMapping;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.ProductVersionListMaster;
import com.hcl.atf.taf.model.SCMSystem;
import com.hcl.atf.taf.model.TestRunConfigurationChild;
import com.hcl.atf.taf.model.TestSuiteList;
import com.hcl.atf.taf.model.dto.DefectReportDTO;
import com.hcl.atf.taf.service.DefectManagementService;



@Service
public class DefectManagementServiceImpl  implements DefectManagementService{	 
	
	
	private static final Log log = LogFactory.getLog(DefectManagementServiceImpl.class);
	@Autowired
	private DefectManagementSystemDAO defectManagementSystemDao;
	@Autowired
	private ProductMasterDAO productMasterDAO;
	@Autowired
	private ProductVersionListMasterDAO productVersionListMasterDAO;
	@Autowired
	private TestSuiteListDAO testSuiteListDAO;
	@Autowired
	private TestRunConfigurationChildDAO testRunConfigurationChildDAO;	
	
	
	@Override
	@Transactional
	public void addDefectManagementSystem(DefectManagementSystem defectManagementSystem) {
		defectManagementSystemDao.add(defectManagementSystem);
	}
	
	@Override
	@Transactional
	public void updateDefectManagementSystem(DefectManagementSystem defectManagementSystem) {
		defectManagementSystemDao.update(defectManagementSystem);
		
	}
	
	@Override
	@Transactional
	public void deleteDefectManagementSystem(DefectManagementSystem defectManagementSystem) {
		defectManagementSystemDao.delete(defectManagementSystem);
		
	}
	
	@Override
	@Transactional
	public List<DefectManagementSystem> listDefectManagementSystem(int productId) {
		return defectManagementSystemDao.listDefectManagementSystem(productId);
		
	}
	
	@Override
	@Transactional
	public DefectManagementSystem getByDefectManagementSystemId(int defectManagementSystemId) {
		return defectManagementSystemDao.getByDefectManagementSystemId(defectManagementSystemId);
	}
	
	@Override
	@Transactional
	public int getDefectManagementSystemId(int productId, String defectManagementSystemName, String defectManagementSystemVersion) {
		return defectManagementSystemDao.getDefectManagementSystemId(productId, defectManagementSystemName, defectManagementSystemVersion);
	}
	
	@Override
	@Transactional
	public void addDefectManagementSystemMapping(DefectManagementSystemMapping defectManagementSystemMapping) {
		defectManagementSystemDao.addDefectManagementSystemMapping(defectManagementSystemMapping);
		
	}
	
	@Override
	@Transactional
	public void updateDefectManagementSystemMapping(DefectManagementSystemMapping defectManagementSystemMapping) {
		defectManagementSystemDao.updateDefectManagementSystemMapping(defectManagementSystemMapping);
	}
	
	@Override
	@Transactional
	public void deleteDefectManagementSystemMapping(DefectManagementSystemMapping defectManagementSystemMapping) {
		defectManagementSystemDao.deleteDefectManagementSystemMapping(defectManagementSystemMapping);
		
	}
	
	@Override
	@Transactional
	public List<DefectManagementSystemMapping> listDefectManagementSystemMapping(int productId) {
		return defectManagementSystemDao.listDefectManagementSystemMapping(productId);
	}
	
	@Override
	@Transactional
	public DefectManagementSystemMapping getByDefectManagementSystemMappingId(int defectManagementSystemId) {
		return defectManagementSystemDao.getByDefectManagementSystemMappingId(defectManagementSystemId);
	}
	
	@Override
	@Transactional
	public String getDefectSystemMappingValue(int defectManagementSystemId, int productId, String mappingType) {
		return getDefectSystemMappingValue(defectManagementSystemId, productId, mappingType);
	}
	
	@Override
	@Transactional
	public String getDefectSystemMappingProductName(int defectManagementSystemId, int productId) {
		return defectManagementSystemDao.getDefectSystemMappingProductName(defectManagementSystemId, productId);
	}
	
	@Override
	@Transactional
	public String getDefectSystemMappingProductVersion(int defectManagementSystemId, int productId) {
		return defectManagementSystemDao.getDefectSystemMappingProductVersion(defectManagementSystemId, productId);
	}
	
	@Override
	@Transactional
	public String getEntityName(String mappingType, Integer mappedEntityIdInTAF) {
		
		if (mappingType == null) {
			log.debug("Mapping type is null. Unable to retrieve the mapped entity");
			return null;
		}
		if (mappedEntityIdInTAF == null) {
			log.debug("Mapped entity Id null. Unable to retrieve the mapped entity");
			return null;
		}
		
		String entityName = null;
		//Get the corresponding entity based on entity type
		if (mappingType.equals(TAFConstants.ENTITY_PRODUCT)) {
			
			ProductMaster product = productMasterDAO.getProductDetailsById(mappedEntityIdInTAF);
			if (product != null) {
				entityName = product.getProductName();
			}
		} else if (mappingType.equals(TAFConstants.ENTITY_PRODUCT_VERSION)) {
			
			ProductVersionListMaster productVersion = productVersionListMasterDAO.getByProductListId(mappedEntityIdInTAF);
			if (productVersion != null) {
				entityName = productVersion.getProductVersionName();
			}
		} else if (mappingType.equals(TAFConstants.ENTITY_TEST_SUITE)) { 
			
			TestSuiteList testSuiteList = testSuiteListDAO.getByTestSuiteId(mappedEntityIdInTAF);
			if (testSuiteList != null) {
				entityName = testSuiteList.getTestSuiteName();
			}
		} else if (mappingType.equals(TAFConstants.ENTITY_TEST_RUN_CONFIGURATION_CHILD)) {
			TestRunConfigurationChild testRunConfigurationChild = testRunConfigurationChildDAO.getByTestRunConfigurationChildId(mappedEntityIdInTAF);
			if (testRunConfigurationChild != null) {
				entityName = testRunConfigurationChild.getTestRunConfigurationName();
			}
		}
		log.debug("Entity Name : " + entityName );
		return entityName;
	}
	
	@Override
	@Transactional
	public Integer getEntityIdByEntityName(String mappingType, String mappedEntityNameInTAF, Integer productId) {
		
		if (mappingType == null) {
			log.debug("Mapping type is null. Unable to retrieve the mapped entity");
			return null;
		}
		if (mappedEntityNameInTAF == null) {
			log.debug("Mapped entity name is null. Unable to retrieve the mapped entity");
			return null;
		}
		
		if (productId == null) {
			log.debug("Product Id is null. Unable to retrieve the mapped entity");
			return null;
		}

		Integer entityId = null;
		//Get the corresponding entity based on entity type
		if (mappingType.equals(TAFConstants.ENTITY_PRODUCT)) {
			
			entityId  = productId;
		} else if (mappingType.equals(TAFConstants.ENTITY_PRODUCT_VERSION)) {
			
			ProductMaster product = productMasterDAO.getProductDetailsById(productId);
			if (product != null) {
				
				Set<ProductVersionListMaster> productVersions = product.getProductVersionListMasters();
				for (ProductVersionListMaster productVersion : productVersions) {
					if (mappedEntityNameInTAF.equals(productVersion.getProductVersionName())) {
						entityId = productVersion.getProductVersionListId();
						break;
					}
				}
			}
		}  else if (mappingType.equals(TAFConstants.ENTITY_TEST_SUITE)) {
			
			ProductMaster product = productMasterDAO.getProductDetailsById(productId);
			if (product != null) {
				Set<TestSuiteList> testSuites = product.getTestSuiteLists();
				if (testSuites != null) {
					for (TestSuiteList testSuite : testSuites) {
						if (mappedEntityNameInTAF.equals(testSuite.getTestSuiteName())) {
							entityId = testSuite.getTestSuiteId();
							break;
						}
					}
				}
			}
		}  else {
			entityId = null;
		}
		log.debug("Entity Id : " + entityId );
		return entityId;
	}

	@Override
	@Transactional
	public void deleteDefectManagementSystem(int defectManagementSystemId) {
		defectManagementSystemDao.delete(getByDefectManagementSystemId(defectManagementSystemId));
		
	}
	
	@Override
	@Transactional
	public void deleteDefectManagementSystemMapping(int defectManagementSystemMappingId) {
		defectManagementSystemDao.deleteDefectManagementSystemMapping(getByDefectManagementSystemMappingId(defectManagementSystemMappingId));
		
	}
	
	@Override
	@Transactional
	public int getTotalRecordsOfDefectManagementSystem(int productId) {
		return defectManagementSystemDao.getTotalRecordsOfDefectManagementSystem(productId);
	}

	@Override
	@Transactional
	public boolean isDefectManagementSystemExistingByTitle(
			DefectManagementSystem defectManagementSystem) {
		return defectManagementSystemDao.isDefectManagementSystemExistingByTitle(defectManagementSystem);
	}

	@Override
	@Transactional
	public DefectManagementSystem getDMSByProductId(int productId, int dms) {
		return defectManagementSystemDao.getDMSByProductId(productId, dms);
	}

	@Override
	@Transactional
	public DefectManagementSystem getPrimaryDMSByProductId(int productId) {
		return defectManagementSystemDao.getPrimaryDMSByProductId(productId);
	}

	@Override
	@Transactional
	public List<DefectReportDTO> listDefectNameAndCode(int testExecutionResultsBugId) {
		List<DefectReportDTO>  defectDtoList = defectManagementSystemDao.defectSystemNameAndCodeDao(testExecutionResultsBugId);
		List<DefectReportDTO>  finaldefectDtoList = new ArrayList<DefectReportDTO>();
		for (DefectReportDTO defDtoObj : defectDtoList) {
			if(defDtoObj.getDefectSystemName().equalsIgnoreCase(IDPAConstants.JIRA_TOOL)){
				String fullURI = defDtoObj.getDefectSystemURI()+"/browse/"+defDtoObj.getDefectSystemCode();
				defDtoObj.setDefectSystemURI(fullURI);				
			}else{
				defDtoObj.setDefectSystemURI(null);
			}
			finaldefectDtoList.add(defDtoObj);
		}
		return finaldefectDtoList;		
	}

	@Override
	@Transactional
	public void addSCMManagementSystem(SCMSystem scmSystem) {
		defectManagementSystemDao.addSCMSystem(scmSystem);
		
	}

	@Override
	@Transactional
	public void updateSCMManagementSystem(SCMSystem scmSystem) {
		defectManagementSystemDao.updateSCMSystem(scmSystem);
		
	}

	@Override
	@Transactional
	public List<SCMSystem> listSCMManagementSystem(int productId) {
		return defectManagementSystemDao.listSCMManagementSystem(productId);
	}

	@Override
	@Transactional
	public String deleteSCMManagementSystem(int id) {
		return defectManagementSystemDao.deleteSCMSystem(id);
	}

	@Override
	@Transactional
	public int getTotalRecordsOfSCMManagementSystems(int productId) {
		return defectManagementSystemDao.getTotalRecordsOfSCMManagementSystems(productId);
	}

	@Override
	@Transactional
	public SCMSystem getSCMSystemByNameAndProductId(Integer productId, String scmSystemName) {
		return defectManagementSystemDao.getSCMSystemByNameAndProductId(productId,scmSystemName);
	}

	@Override
	@Transactional
	public List<SCMSystem> listSCMManagementSystemByStatus(int productId,Integer status) {
		return defectManagementSystemDao.listSCMManagementSystemByStatus(productId,status);
	}
	
}
