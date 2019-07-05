package com.hcl.atf.taf.service;

import java.util.List;

import com.hcl.atf.taf.model.DefectManagementSystem;
import com.hcl.atf.taf.model.DefectManagementSystemMapping;
import com.hcl.atf.taf.model.SCMSystem;
import com.hcl.atf.taf.model.dto.DefectReportDTO;

public interface DefectManagementService {
	
	
	//Methods for retrieve / update defect management system details
	void addDefectManagementSystem (DefectManagementSystem defectManagementSystem);
	void updateDefectManagementSystem (DefectManagementSystem defectManagementSystem);
	void deleteDefectManagementSystem (int defectManagementSystemId);
	List<DefectManagementSystem> listDefectManagementSystem(int productId);
	int getTotalRecordsOfDefectManagementSystem(int productId);
	DefectManagementSystem getByDefectManagementSystemId(int defectManagementSystemId);
	int getDefectManagementSystemId(int productId, String defectManagementSystemName, String defectManagementSystemVersion);
	boolean isDefectManagementSystemExistingByTitle(DefectManagementSystem defectManagementSystem);
	
	//Methods for retrieve / update defect management system mapping details	
	void addDefectManagementSystemMapping (DefectManagementSystemMapping defectManagementSystemMapping);
	void updateDefectManagementSystemMapping (DefectManagementSystemMapping defectManagementSystemMapping);
	void deleteDefectManagementSystemMapping (int defectManagementSystemMappingId);
	List<DefectManagementSystemMapping> listDefectManagementSystemMapping(int defectManagementSystemId);	
	String getDefectSystemMappingValue(int defectManagementSystemId, int productId, String mappingType);
	String getDefectSystemMappingProductName(int defectManagementSystemId, int productId);
	String getDefectSystemMappingProductVersion(int defectManagementSystemId, int productId);
	String getEntityName(String mappingType, Integer mappedEntityIdInTAF);
	Integer getEntityIdByEntityName(String mappingType,String mappedEntityNameInTAF, Integer productId);
	DefectManagementSystemMapping getByDefectManagementSystemMappingId(int defectManagementSystemId);
	void deleteDefectManagementSystem(DefectManagementSystem defectManagementSystem);
	void deleteDefectManagementSystemMapping(DefectManagementSystemMapping defectManagementSystemMapping);
	DefectManagementSystem getDMSByProductId(int productId,int dms);
	DefectManagementSystem getPrimaryDMSByProductId(int productId);
	List<DefectReportDTO> listDefectNameAndCode(int testExecutionResultsBugId);
	void addSCMManagementSystem(SCMSystem scmSystem);
	void updateSCMManagementSystem(SCMSystem scmSystem);
	List<SCMSystem> listSCMManagementSystem(int productId);
	String deleteSCMManagementSystem(int id);
	int getTotalRecordsOfSCMManagementSystems(int productId);
	SCMSystem getSCMSystemByNameAndProductId(Integer productId,String scmSystemName);
	List<SCMSystem> listSCMManagementSystemByStatus(int productId,Integer status);
}
