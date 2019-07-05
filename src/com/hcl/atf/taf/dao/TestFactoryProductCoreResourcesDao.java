package com.hcl.atf.taf.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.hcl.atf.taf.model.ProductTeamResources;
import com.hcl.atf.taf.model.TestFactoryProductCoreResource;

public interface TestFactoryProductCoreResourcesDao {

	public List<TestFactoryProductCoreResource> getProductCoreResourcesList(
			int productId, Integer jtStartIndex, Integer jtPageSize);

	public void add(TestFactoryProductCoreResource coreResouce);

	public TestFactoryProductCoreResource getCoreResourceById(
			Integer testFactoryProductCoreResourceId);

	public void upadte(TestFactoryProductCoreResource coreResourceFromUI);

	public Boolean isUserAlreadyCoreResource(Integer productId, Integer userId,	Date fromDate, Date toDate,TestFactoryProductCoreResource coreResourceFromDB);
	
	public boolean isUserAProductCoreResource(int userId, int productId, String date);
	
	public List<TestFactoryProductCoreResource> getProductCoreResourcesByRole(
			int productId, Integer roleId);
	
	public int getProductRoleOfProductCoreResource(int userId, int productId, String date);

	Map<Integer, Integer> getProductCoreResourcesCountByRole(int productId);

	Integer getTestFactoryResourcePoolHasTestFactorySizebyTestFactoryId(
			Integer testFcatoryId);

}
