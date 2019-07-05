package com.hcl.atf.taf.dao;

import java.util.List;

import com.hcl.atf.taf.model.ExecutionTypeMaster;

public interface ExecutionTypeMasterDAO  {	
	List<ExecutionTypeMaster> list();
	ExecutionTypeMaster getExecutionTypeByExecutionTypeId(int executionTypeId);
	List<ExecutionTypeMaster> listbyEntityMasterId(int entitymasterid);	
	ExecutionTypeMaster getExecutionTypeByExecutionTypeName(String executionTypeName);
}
