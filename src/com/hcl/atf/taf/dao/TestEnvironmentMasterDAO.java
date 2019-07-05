package com.hcl.atf.taf.dao;

import java.util.List;

import com.hcl.atf.taf.model.TestEnviromentMaster;

public interface TestEnvironmentMasterDAO  {	 
	List<TestEnviromentMaster> list();
	List<TestEnviromentMaster>  list(String devicePlatform);
}
