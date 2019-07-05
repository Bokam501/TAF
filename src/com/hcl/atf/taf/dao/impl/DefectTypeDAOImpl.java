package com.hcl.atf.taf.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.DefectTypeDAO;
import com.hcl.atf.taf.model.DefectApprovalStatusMaster;
import com.hcl.atf.taf.model.DefectIdentificationStageMaster;
import com.hcl.atf.taf.model.DefectTypeMaster;
import com.hcl.atf.taf.model.TestExecutionResultBugList;
import com.hcl.atf.taf.model.dto.DefectWeeklyReportDTO;

@Repository
public class DefectTypeDAOImpl implements DefectTypeDAO {
	private static final Log log = LogFactory
			.getLog(DefectTypeDAOImpl.class);
	@Autowired(required = true)
	private SessionFactory sessionFactory;

	@Override
	@Transactional
	public List<DefectTypeMaster> listDefectTypes() {
		List<DefectTypeMaster> defectTypeMasterList = null;
		try{
			Criteria c = sessionFactory.getCurrentSession().createCriteria(DefectTypeMaster.class, "defType");			
			defectTypeMasterList = c.list();			
			log.debug("list successful");
		}catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return defectTypeMasterList;	
	}


	@Override
	@Transactional
	public DefectTypeMaster getDefectTypeById(int defectTypeId) {
		List<DefectTypeMaster> defectTypeMasterList = null;
		DefectTypeMaster defectType = null;
		try{
			Criteria c = sessionFactory.getCurrentSession().createCriteria(DefectTypeMaster.class, "defType");		
			c.add(Restrictions.eq("defType.defectTypeId", defectTypeId));
			defectTypeMasterList = c.list();			
			defectType = (defectTypeMasterList!=null && defectTypeMasterList.size()!=0)?(DefectTypeMaster) defectTypeMasterList.get(0):null;
			log.debug("list successful");
		}catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return defectType;
		}


	@Override
	@Transactional
	public List<DefectIdentificationStageMaster> listDefectIdentificationStages() {
		List<DefectIdentificationStageMaster> defectIdentificationMasterList = null;
		try{
			Criteria c = sessionFactory.getCurrentSession().createCriteria(DefectIdentificationStageMaster.class, "defIdenStage");			
			defectIdentificationMasterList = c.list();			
			log.debug("list successful");
		}catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return defectIdentificationMasterList;	
	}


	@Override
	@Transactional
	public DefectIdentificationStageMaster getDefectIdentificationStageMasterById(
			int stageId) {
		List<DefectIdentificationStageMaster> defectIdentificationStagesList = null;
		DefectIdentificationStageMaster defectIdenStage = null;
		try{
			Criteria c = sessionFactory.getCurrentSession().createCriteria(DefectIdentificationStageMaster.class, "defIdenStage");		
			c.add(Restrictions.eq("defIdenStage.stageId", stageId));
			defectIdentificationStagesList = c.list();			
			defectIdenStage = (defectIdentificationStagesList!=null && defectIdentificationStagesList.size()!=0)?(DefectIdentificationStageMaster) defectIdentificationStagesList.get(0):null;
			log.debug("list successful");
		}catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return defectIdenStage;
	}


	@Override
	@Transactional
	public List<DefectApprovalStatusMaster> listDefectApprovalStatus() {
		List<DefectApprovalStatusMaster> defectApprovalStatusMasterList = null;
		try{
			Criteria c = sessionFactory.getCurrentSession().createCriteria(DefectApprovalStatusMaster.class, "defectApprovalStaus");			
			defectApprovalStatusMasterList = c.list();			
			log.debug("list successful");
		}catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return defectApprovalStatusMasterList;	
	}


	@Override
	@Transactional
	public DefectApprovalStatusMaster getDefectApprovalStatusById(
			int defectApprovalStatusId) {
		List<DefectApprovalStatusMaster> defectApprovalStatusList = null;
		DefectApprovalStatusMaster defectApproval = null;
		try{
			Criteria c = sessionFactory.getCurrentSession().createCriteria(DefectApprovalStatusMaster.class, "defectApprovalStaus");		
			c.add(Restrictions.eq("defectApprovalStaus.approvalStatusId", defectApprovalStatusId));
			defectApprovalStatusList = c.list();			
			defectApproval = (defectApprovalStatusList!=null && defectApprovalStatusList.size()!=0)?(DefectApprovalStatusMaster) defectApprovalStatusList.get(0):null;
			log.debug("list successful");
		}catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return defectApproval;
	}


	@Override
	@Transactional
	public void reviewAndApproveDefect(TestExecutionResultBugList defectFromDB) {
		log.debug("Approve Defects");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(defectFromDB);
			log.debug("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}
	}


	@Override
	@Transactional
	public List<DefectWeeklyReportDTO> listDefects(Date startDate,
			Date endDate,int productId,int productVersionId,int productBuildId,int workPackageId) {
		log.info("listing listDefects by "+startDate+" and "+endDate);
		List<DefectWeeklyReportDTO> defectWeeklyReportListDTO = new ArrayList<DefectWeeklyReportDTO>();
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestExecutionResultBugList.class,"defect");
			c.createAlias("defect.defectApprovalStatus", "approvalStatus");
			c.createAlias("defect.defectType", "defectType");
			c.createAlias("defect.defectFoundStage", "defectFoundStage");
			c.createAlias("defect.testCaseExecutionResult", "testExecResult");
			c.createAlias("testExecResult.workPackageTestCaseExecutionPlan", "wpTCExecPlan");
			c.createAlias("wpTCExecPlan.workPackage", "wp");
			c.createAlias("wp.productBuild", "pbuild");
			c.createAlias("pbuild.productVersion", "pVersion");
			c.createAlias("pVersion.productMaster", "product");
			c.createAlias("defect.bugFilingStatus", "bugFilingStatus");
			
			if(workPackageId != 0){
				c.add(Restrictions.eq("wp.workPackageId", workPackageId));
			}else if(productBuildId != 0){
				c.add(Restrictions.eq("pbuild.productBuildId", productBuildId));
			}else if(productVersionId != 0){
				c.add(Restrictions.eq("pVersion.productVersionListId", productVersionId));
			}else if(productId != 0){
				c.add(Restrictions.eq("product.productId", productId));
			}
			c.add(Restrictions.eq("defect.analysedFlag", 1));
			c.add(Restrictions.ne("bugFilingStatus.stageName", "Fixed").ignoreCase());
			c.add(Restrictions.between("defect.bugCreationTime", startDate,  endDate));
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Property.forName("defect.bugCreationTime").as("bugCreatedTime"));
			projectionList.add(Property.forName("approvalStatus.approvalStatusName").as("approvalStatName"));
			projectionList.add(Property.forName("defectType.defectTypeId").as("defTypeId"));
			projectionList.add(Projections.count("defect.testExecutionResultBugId").as("bugsCount"));
			projectionList.add(Projections.groupProperty("defectFoundStage.stageId"));
			projectionList.add(Projections.groupProperty("approvalStatus.approvalStatusId"));
			projectionList.add(Projections.groupProperty("defect.bugCreationTime"));
			projectionList.add(Projections.groupProperty("defectType.defectTypeId").as("defectTypeCount"));
			
			c.setProjection(projectionList);

			List<Object[]> list = c.list();
			DefectWeeklyReportDTO defectReportDTO = null;
			for (Object[] row : list) {
				defectReportDTO = new DefectWeeklyReportDTO();
				defectReportDTO.setBugCreatedDate((Date)row[0]);
				defectReportDTO.setDefectsApprovalStatusName((String)row[1]);
				defectReportDTO.setDefectTyepId((Integer)(row[2]));
				defectReportDTO.setBugsCount(((Long)row[3]).intValue());
				defectReportDTO.setFoundInStageId((Integer)(row[4]));
				defectReportDTO.setDefectsApprovalStatusId((Integer)(row[5]));
				log.info("Bug Created Date : "+(Date)row[0]+" Defect Approval Status: "+(String)row[1]+" Defect Type: "+(Integer)(row[2])+"  Bugs Count:  "+((Long)row[3]).intValue()+"  Found in State: "+(Integer)(row[4])+"   Defect Approval Status: "+ (Integer)(row[5]));
				defectWeeklyReportListDTO.add(defectReportDTO);
			}
			
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return defectWeeklyReportListDTO;
	}
	
}
