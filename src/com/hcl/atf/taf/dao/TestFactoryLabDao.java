package com.hcl.atf.taf.dao;

import java.util.Date;
import java.util.List;

import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.TestFactoryLab;
import com.hcl.atf.taf.model.WorkPackageDemandProjection;

public interface TestFactoryLabDao {

	List<TestFactoryLab> getTestFactoryLabsList();
	TestFactoryLab getTestFactoryLabBytestFactoryLabId(int testFactoryLabId);
	List<TestFactoryLab> getTestFactoryLabByResourcePoolId(int resourcePoolId);
	List<WorkPackageDemandProjection> listDemandDetailsOfTestFactoryLab(Integer testFactoryLabId, Integer shiftTypeId, Date date);
	List<TestFactoryLab> getTestFactoryLabsList(Integer testFactoryLabId);
}
