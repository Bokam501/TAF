package com.hcl.atf.taf.dao;

import java.util.List;

import com.hcl.atf.taf.model.TestCategoryMaster;
import com.hcl.atf.taf.model.TestToolMaster;

public interface TestCategoryMasterDAO  {	 

	List<TestCategoryMaster> list();
	TestToolMaster getTestToolByName(String testToolName);
	TestToolMaster getTestToolById(Integer testToolId);
}
