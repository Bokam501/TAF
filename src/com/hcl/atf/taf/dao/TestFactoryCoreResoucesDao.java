package com.hcl.atf.taf.dao;

import java.util.List;

import com.hcl.atf.taf.model.TestFactory;
import com.hcl.atf.taf.model.TestFactoryCoreResource;

public interface TestFactoryCoreResoucesDao {

public	List<TestFactoryCoreResource> getCoreResourcesList(int testFactoryId,
		Integer jtStartIndex, Integer jtPageSize);

public void addCoreResource(TestFactoryCoreResource coreResouce);


public TestFactoryCoreResource getCoreResourceById(
		Integer testFactoryCoreResourceId);

public void updateCoreRes(TestFactoryCoreResource coreResourceFromUI);



public Boolean isUserExisted(Integer testFactoryId, Integer userId);

public List<TestFactoryCoreResource> list();


}
