package com.hcl.atf.taf.dao;

import java.util.Date;
import java.util.List;

import com.hcl.atf.taf.model.DefectManagementSystem;
import com.hcl.atf.taf.model.DefectManagementSystemMapping;
import com.hcl.atf.taf.model.SCMSystem;
import com.hcl.atf.taf.model.dto.DefectReportDTO;

public interface DefectManagementSystemDAO  {
	
	void add (DefectManagementSystem defectManagementSystem);
	void update (DefectManagementSystem defectManagementSystem);
	void delete (DefectManagementSystem defectManagementSystem);
	DefectManagementSystem getByDefectManagementSystemId(int defectManagementSystemId);
	List<DefectManagementSystem> listDefectManagementSystem(int productId);
	int getDefectManagementSystemId(int productId, String defectManagementSystemName, String defectManagementSystemVersion);
	
	//Methods for retrieve / update defect management system mapping details	
	void addDefectManagementSystemMapping (DefectManagementSystemMapping defectManagementSystemMapping);
	void updateDefectManagementSystemMapping (DefectManagementSystemMapping defectManagementSystemMapping);
	void deleteDefectManagementSystemMapping (DefectManagementSystemMapping defectManagementSystemMapping);
	List<DefectManagementSystemMapping> listDefectManagementSystemMapping(int productId);	
	String getDefectSystemMappingValue(int defectManagementSystemId, int productId, String mappingType);
	String getDefectSystemMappingProductName(int defectManagementSystemId, int productId);
	String getDefectSystemMappingProductVersion(int defectManagementSystemId, int productId);
	int getTotalRecordsOfDefectManagementSystem(int productId);
	boolean isDefectManagementSystemExistingByTitle(DefectManagementSystem defectManagementSystem);
	DefectManagementSystemMapping getByDefectManagementSystemMappingId(int defectManagementSystemMappingId);
	DefectManagementSystem getDMSByProductId(int productId,int dms);
	DefectManagementSystem getPrimaryDMSByProductId(int productId);
	Integer countAllBugs(Date startDate, Date endDate);
	List<DefectReportDTO> defectSystemNameAndCodeDao(int testExecutionResultsBugId);
	void addSCMSystem(SCMSystem scmSystem);
	void updateSCMSystem(SCMSystem scmSystem);
	List<SCMSystem> listSCMManagementSystem(int productId);
	String deleteSCMSystem(int id);
	SCMSystem getBySCMSystemId(int id);
	int getTotalRecordsOfSCMManagementSystems(int productId);
	SCMSystem getSCMSystemByNameAndProductId(Integer productId,String scmSystemName);
	List<SCMSystem> listSCMManagementSystemByStatus(int productId,Integer status);

}
