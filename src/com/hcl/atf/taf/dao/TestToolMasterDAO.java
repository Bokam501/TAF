package com.hcl.atf.taf.dao;

import java.util.List;

import com.hcl.atf.taf.model.TestToolMaster;

public interface TestToolMasterDAO  {	 
	List<TestToolMaster> list();

	TestToolMaster getTestToolMaster(int parseInt);
	
	TestToolMaster getTestToolIdByName(String testEngine);
}
