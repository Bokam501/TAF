/**
 * 
 */
package com.hcl.ilcm.workflow.service.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.ilcm.workflow.dao.WorkflowMasterDAO;
import com.hcl.ilcm.workflow.dao.WorkflowStatusDAO;
import com.hcl.ilcm.workflow.model.WorkflowMaster;
import com.hcl.ilcm.workflow.model.WorkflowStatus;
import com.hcl.ilcm.workflow.model.WorkflowStatusMapping;
import com.hcl.ilcm.workflow.service.WorkflowStatusService;

/**
 * @author silambarasur
 *
 */
@Service
public class WorkflowStatusServiceImpl implements WorkflowStatusService{
	private static final Log log = LogFactory.getLog(WorkflowStatusServiceImpl.class);
	@Autowired
	private WorkflowStatusDAO workflowStatusDAO; 
	
	@Autowired
	private WorkflowMasterDAO workflowMasterDAO;
	
	@Override
	@Transactional
	public List<WorkflowStatus> getWorkFlowStatusList(Integer productId,Integer entityTypeId,Integer entityId,List<String> workFlowStatusList){
		return workflowStatusDAO.getWorkFlowStatusList(productId, entityTypeId, entityId, workFlowStatusList);
	}
	@Override
	@Transactional
	public WorkflowStatus getWorkflowStatusById(Integer worflowStatusId){
		return workflowStatusDAO.getWorkflowStatusById(worflowStatusId);
		
	}
	
	@Override
	@Transactional
	public WorkflowStatus getStatusNameByEntityTypeId(String resultPrimaryStatus,Integer entityTypeId){
		return workflowStatusDAO.getStatusNameByEntityTypeId(resultPrimaryStatus, entityTypeId);
	}
	
	@Override
	@Transactional
	public List<WorkflowStatus> getSecondaryStatusByParentId(Integer productId,Integer entityTypeId,Integer wfStatusParentId){
		return workflowStatusDAO.getSecondaryStatusByParentId(productId, entityTypeId, wfStatusParentId);
	}
	

	@Override
	@Transactional
	public List<WorkflowStatus> getWorkFlowStatusList(Integer workflowId, List<String> workFlowStatusNamesFromBPMN) {
		return workflowStatusDAO.getWorkFlowStatusList(workflowId, workFlowStatusNamesFromBPMN);
	}
	
	@Override
	@Transactional
	public boolean isExistWorkflowStausByWorkflowIdAndWorkflowStatusName(Integer workflowId,String workflowStatusName) {
		return workflowStatusDAO.isExistWorkflowStausByWorkflowIdAndWorkflowStatusName(workflowId, workflowStatusName);
	}
	
	@Override
	@Transactional
	public boolean isExistWorkflowStausType(Integer workflowId,String workflowStatusType){
		return workflowStatusDAO.isExistWorkflowStausType(workflowId, workflowStatusType);
	}


	@Override
	@Transactional
	public WorkflowStatus getInitialStatusForInstanceByWorkflowId(Integer productId, Integer entityTypeId, Integer entityId, Integer workflowId) {
		WorkflowStatus workflowStatus = null;
		try{
			if(workflowId != null && workflowId > 0){
				workflowStatus = workflowStatusDAO.getInitialStatusForInstance(productId, entityTypeId, entityId, workflowId);
			}else{
				workflowStatus = getWorkflowStatusById(-1);
			}
		}catch(Exception ex){
			log.error("ERROR  ",ex);
		}
		return workflowStatus;
	}
	
	@Override
	@Transactional
	public WorkflowStatus getInitialStatusForInstanceByEntityType(Integer productId, Integer entityTypeId, Integer entityId) {
		WorkflowStatus workflowStatus = null;
		try{
			Integer activeState = 1;
			Integer entityInstanceId = null;
			WorkflowMaster workflowMaster = workflowMasterDAO.getWorkflowForEntityTypeOrInstance(productId, entityTypeId,  entityId, entityInstanceId, activeState);
			if(workflowMaster != null) {
				workflowStatus = workflowStatusDAO.getInitialStatusForInstance(productId, entityTypeId, entityId, workflowMaster.getWorkflowId());
			}else{
				workflowStatus = getWorkflowStatusById(-1);
			}
		}catch(Exception ex){
			log.error("ERROR  ",ex);
		}
		return workflowStatus;
	}
	
	@Override
	@Transactional
	public Boolean isWorkflowContainsMandatoryStatusTypeStatus(Integer workflowId) {
		return workflowStatusDAO.isWorkflowContainsMandatoryStatusTypeStatus(workflowId);
	}
	
	@Override
	@Transactional
	public Boolean isStatusAvailableForStatusType(Integer workflowId, String updatedStatusType, Integer exceptStatusId) {
		return workflowStatusDAO.isStatusAvailableForStatusType(workflowId, updatedStatusType, exceptStatusId);
	}
	
	@Override
	@Transactional
	public List<Object[]> getWorkflowStatusAvailableForMapping(Integer workflowId, Integer sourceStatusId, Integer jtStartIndex, Integer jtPageSize) {
		return workflowStatusDAO.getWorkflowStatusAvailableForMapping(workflowId, sourceStatusId, jtStartIndex, jtPageSize);
	}

	@Override
	@Transactional
	public Integer getWorkflowStatusAvailableForMappingCount(Integer workflowId, Integer sourceStatusId) {
		return workflowStatusDAO.getWorkflowStatusAvailableForMappingCount(workflowId, sourceStatusId);
	}
	
	@Override
	@Transactional
	public List<Object[]> getWorkflowStatusAlreadyMapped(Integer workflowId, Integer sourceStatusId, Integer jtStartIndex, Integer jtPageSize) {
		return workflowStatusDAO.getWorkflowStatusAlreadyMapped(workflowId, sourceStatusId, jtStartIndex, jtPageSize);
	}

	@Override
	@Transactional
	public boolean isWorkflowStatusAlreadyMapped(Integer workflowId, Integer sourceStatusId, Integer targetStatusId) {
		return workflowStatusDAO.isWorkflowStatusAlreadyMapped(workflowId, sourceStatusId, targetStatusId);
	}

	@Override
	@Transactional
	public void workflowStatusMappingOrUnmapping(WorkflowStatusMapping workflowStatusMapping, String maporunmap) {
		workflowStatusDAO.workflowStatusMappingOrUnmapping(workflowStatusMapping, maporunmap);
	}
	
	@Override
	@Transactional
	public List<WorkflowStatus> getPossibleWorkflowStatusForAction(Integer currentStatusId) {
		return workflowStatusDAO.getPossibleWorkflowStatusForAction(currentStatusId);
	}
	
	@Override
	@Transactional
	public WorkflowStatus getWorkflowStatusByName(String worflowStatusName) {
		try {
			return workflowStatusDAO.getWorkflowStatusByName(worflowStatusName);
		}catch(Exception e) {
			log.error("Error in getWorkflowStatusByName",e);
		}
		return null;
	}
	
	
	@Override
	@Transactional
	public WorkflowStatus getWorkflowStatusForInstanceByEntityType(Integer productId, Integer entityTypeId, Integer entityId,String workflowStatusName) {
		WorkflowStatus workflowStatus = null;
		try{
			Integer activeState = 1;
			Integer entityInstanceId = null;
			WorkflowMaster workflowMaster = workflowMasterDAO.getWorkflowForEntityTypeOrInstance(productId, entityTypeId,  entityId, entityInstanceId, activeState);
			if(workflowMaster != null) {
				workflowStatus = workflowStatusDAO.getWorkflowStausByWorkflowIdAndWorkflowStatusName(workflowMaster.getWorkflowId(), workflowStatusName);
			}else{
				workflowStatus = getWorkflowStatusById(-1);
			}
		}catch(Exception ex){
			log.error("ERROR  in getWorkflowStatusForInstanceByEntityType",ex);
		}
		return workflowStatus;
	}
	
}
