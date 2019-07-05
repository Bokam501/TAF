package com.hcl.atf.taf.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.ExecutionTypeMasterDAO;
import com.hcl.atf.taf.model.ExecutionTypeMaster;
import com.hcl.atf.taf.service.ExecutionTypeMasterService;

@Service
public class ExecutionTypeMasterServiceImpl implements ExecutionTypeMasterService {
	
	@Autowired
	private ExecutionTypeMasterDAO executionTypeMasterDAO;
	
	@Override
	@Transactional
	public List<ExecutionTypeMaster> listExecutionType() {
		return executionTypeMasterDAO.list();		
	}

	@Override
	@Transactional
	public List<ExecutionTypeMaster> listbyEntityMasterId(int entitymasterid) {
		return executionTypeMasterDAO.listbyEntityMasterId(entitymasterid);
	}

	@Override
	@Transactional
	public ExecutionTypeMaster getExecutionTypeByExecutionTypeId(
			int executionTypeId) {
		return executionTypeMasterDAO.getExecutionTypeByExecutionTypeId(executionTypeId);
	}	
		
}
