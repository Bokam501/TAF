package com.hcl.atf.taf.dao;

import java.util.Date;
import java.util.List;

import com.hcl.atf.taf.model.DefectApprovalStatusMaster;
import com.hcl.atf.taf.model.DefectIdentificationStageMaster;
import com.hcl.atf.taf.model.DefectTypeMaster;
import com.hcl.atf.taf.model.TestExecutionResultBugList;
import com.hcl.atf.taf.model.dto.DefectWeeklyReportDTO;

public interface DefectTypeDAO  {	 
	
	List<DefectTypeMaster> listDefectTypes();
	DefectTypeMaster getDefectTypeById(int defectTypeId);
	
	List<DefectIdentificationStageMaster> listDefectIdentificationStages();
	DefectIdentificationStageMaster getDefectIdentificationStageMasterById(int defectTypeId);
	
	List<DefectApprovalStatusMaster> listDefectApprovalStatus();
	DefectApprovalStatusMaster getDefectApprovalStatusById(int defectApprovalStatusId);
	void reviewAndApproveDefect(TestExecutionResultBugList defectFromDB);
	List<DefectWeeklyReportDTO> listDefects(Date startDate, Date endDate,int productId,int productVersionId,int productBuildId,int workPackageId);
	
}
