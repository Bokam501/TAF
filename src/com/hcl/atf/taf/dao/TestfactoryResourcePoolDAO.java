package com.hcl.atf.taf.dao;

import java.util.Date;
import java.util.List;

import com.hcl.atf.taf.model.TestFactoryLab;
import com.hcl.atf.taf.model.TestfactoryResourcePool;

public interface TestfactoryResourcePoolDAO {

	List<TestfactoryResourcePool> listResourcePool();
	List<TestfactoryResourcePool> listResourcePoolbyID(int resourcePoolId);
	TestfactoryResourcePool getbyresourcePoolId(int resourcePoolId);
	List<TestfactoryResourcePool> listResourcePoolbytestFactoryLabId(int testFactoryLabId);
	TestfactoryResourcePool getResourcePoolbytestFactoryLabId(int testFactoryLabId);
	
	void addResourcePool(TestfactoryResourcePool resourcePool);
	void updateResourcePool(TestfactoryResourcePool resourcePool);
	List<TestfactoryResourcePool> listResourcePoolByResourceManagerId(int userRoleId, int userId);
	List<TestfactoryResourcePool> listResourcePoollistbyId(int testFactoryLabId, int resourcePoolId);
	List<TestfactoryResourcePool> listResourcePoolByTestFactoryLabId(int testFactoryLabId);
	TestFactoryLab getTestFactoryLabIdByUserResourcePool(Integer userId);
	TestfactoryResourcePool getTestFactoryResourcePoolByName(String testFactoryResourcePoolName);
	List<Integer> getTestfactoryResourcePoolListbyTestFactoryId(
			Integer testFactoryId);
	boolean checkResourceAssignedToRPforWeek(Integer userId,Integer resourcePoolId, Date startDate);
}
