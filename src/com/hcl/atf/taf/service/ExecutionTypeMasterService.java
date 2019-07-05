package com.hcl.atf.taf.service;

import java.util.List;

import com.hcl.atf.taf.model.ExecutionTypeMaster;

public interface ExecutionTypeMasterService {
	
	List<ExecutionTypeMaster> listExecutionType();
	ExecutionTypeMaster getExecutionTypeByExecutionTypeId(int executionTypeId);
	List<ExecutionTypeMaster> listbyEntityMasterId(int entitymasterid);
}
