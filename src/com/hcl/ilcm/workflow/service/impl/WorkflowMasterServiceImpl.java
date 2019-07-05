package com.hcl.ilcm.workflow.service.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.ilcm.workflow.dao.WorkflowMasterDAO;
import com.hcl.ilcm.workflow.model.WorkflowMaster;
import com.hcl.ilcm.workflow.model.WorkflowMasterEntityMapping;
import com.hcl.ilcm.workflow.service.WorkflowMasterService;

@Service
public class WorkflowMasterServiceImpl implements WorkflowMasterService {
	
	private static final Log log = LogFactory.getLog(WorkflowMasterServiceImpl.class);

	@Autowired
	private WorkflowMasterDAO workflowMasterDAO;
	
	@Override
	@Transactional
	public List<WorkflowMaster> getWorkflowMasterList() {
		return workflowMasterDAO.getWorkflowMasterList();
	}
	
	@Override
	@Transactional
	public List<WorkflowMaster> getWorkflowMasterListByType(String workflowType){
		return workflowMasterDAO.getWorkflowMasterListByType(workflowType);
	}
	
	@Override
	@Transactional
	public void addWorkflowMaster(WorkflowMaster workflowMaster) {
		workflowMasterDAO.addWorkflowMaster(workflowMaster);
	}
	
	@Override
	@Transactional
	public void updateWorkflowMaster(WorkflowMaster workflowMaster){
		workflowMasterDAO.updateWorkflowMaster(workflowMaster);
	}
	
	@Override
	@Transactional
	public WorkflowMaster getWorkflowForEntityTypeOrInstance(Integer productId, Integer entityTypeId, Integer entityId, Integer entityInstanceId, Integer activeState) {
		log.debug("Fetching getWorkflowForEntityTypeOrInstance with parameters productId - "+productId+", entityTypeId - "+entityTypeId+", entityId - "+entityId+", entityInstanceId - "+entityInstanceId+", activeState - "+activeState);
		try{
			return workflowMasterDAO.getWorkflowForEntityTypeOrInstance(productId, entityTypeId, entityId, entityInstanceId, activeState);
		}catch(Exception ex){
			log.error("Error inside getWorkflowForEntityTypeOrInstance - ", ex);
		}
		return null;
	}

	@Override
	@Transactional
	public List<WorkflowMaster> getWorkflowMasterMappedToEntityList(Integer productId, Integer entityTypeId, Integer entityId, Integer activeState) {
		return workflowMasterDAO.getWorkflowMasterMappedToEntityList(productId, entityTypeId, entityId, activeState);
	}

	@Override
	public List<WorkflowMaster> getWorkFlowMasterListByWorkflowName(
			String WorkflowName) {
		
		return workflowMasterDAO.getWorkFlowMasterListByWorkflowName(WorkflowName);
	}

	@Override
	@Transactional
	public List<WorkflowMasterEntityMapping> getEntitiesAndInstanceMappedWithWorkflow(Integer workflowId) {
		return workflowMasterDAO.getEntitiesAndInstanceMappedWithWorkflow(workflowId);
	}

	@Override
	@Transactional
	public Boolean isWorkflowAleadyExist(Integer referenceWorkflowId, String workflowName) {
		return workflowMasterDAO.isWorkflowAleadyExist(referenceWorkflowId, workflowName);
	}

	@Override
	@Transactional
	public List<WorkflowMaster> getWorkflowMasterListByStatus(Integer isActive) {
		try {
		return workflowMasterDAO.getWorkflowMasterListByStatus(isActive);
		}catch(Exception e) {
			log.error("Error in getWorkflowMasterListByStatus",e);
		}
		return null;
	}
}
