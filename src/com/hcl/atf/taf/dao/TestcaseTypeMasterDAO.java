package com.hcl.atf.taf.dao;

import java.util.List;

import com.hcl.atf.taf.model.TestcaseTypeMaster;

public interface TestcaseTypeMasterDAO  {	
	List<TestcaseTypeMaster> list();	
	TestcaseTypeMaster getTestcaseTypeMasterBytestcaseTypeId(int testcaseTypeId);
	TestcaseTypeMaster getTestcaseTypeMasterByName(String name);
	
}
