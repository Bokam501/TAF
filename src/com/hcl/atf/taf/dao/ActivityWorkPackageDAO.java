package com.hcl.atf.taf.dao;

import java.util.List;
import java.util.Map;

import com.hcl.atf.taf.model.Activity;
import com.hcl.atf.taf.model.ActivityWorkPackage;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.dto.ActivityWorkPackageSummaryDTO;
import com.hcl.atf.taf.model.dto.ProductAWPSummaryDTO;
import com.hcl.atf.taf.model.json.JsonActivityWorkPackage;
public interface ActivityWorkPackageDAO {
	
	List<ActivityWorkPackage> listActivityWorkPackages();
	List<ActivityWorkPackage> listActivityWorkPackagesByBuildId(Integer productBuildId, int isActive);
	ActivityWorkPackage getActivityWorkPackageById(int workRequestId,Integer initializationLevel);
	void addActivityWorkPackage(ActivityWorkPackage workRequest);
	void updateActivityWorkPackage(ActivityWorkPackage workRequest);
	void deleteActivityWorkPackage(ActivityWorkPackage workRequest);
	void updateActivity(Activity ActivityFromUI);
	ActivityWorkPackage getActivityWorkPackageByName(String activityWpName,	Integer productBuildId);

	List<ActivityWorkPackage> listActivityWorkPackagesByProductId(Integer productId, Integer productVersionId,Integer productBuildId, int isActive, UserList user,Map<String, String> searchStrings);
	String deleteActivityWorkPackage(int activityWorkPackageId, String referencedTableName, String referencedColumnName);
	List<ActivityWorkPackage> listActivityWorkPackagesByTestFactoryId(Integer testFactoryId, Integer productId, Integer isActive, Map<String, String> searchString, Integer jtStartIndex, Integer jtPageSize);
	Integer getActivityWPCountByTestFactoryIdProductId(int testFactoryId, Integer productId);
	Integer listActivityWorkPackagesByProductIdCount(Integer productId, Integer productVersionId, Integer productBuildId, Integer isActive, Map<String, String> searchString);
	List<ActivityWorkPackage> getActivityWorkPackageByBuildId(Integer buildId);
	ActivityWorkPackageSummaryDTO listActivityWorkPackageSummaryDetails(Integer activityWorkPackageId);
	ProductAWPSummaryDTO listProductAWPSummaryDetails(Integer productId);
	List<Integer> getActivityWorkPackagesofProductIDS(int productId, List<Integer> productIdList, boolean paramIsList);
	List<JsonActivityWorkPackage> listJsonActivityWorkPackagesByTestFactoryId(Integer testFactoryId, Integer productId, Integer isActive,
			Map<String, String> searchString, UserList user, Integer jtStartIndex, Integer jtPageSize);
	ActivityWorkPackage getLatestActivityWPByProductId(Integer productId);
	ActivityWorkPackage getLastestActivityWorkPackageByNameInProduct(String workpackageName, Integer productId);
	List<Integer> getWorkpackagesPrevillageForBasedLoginUser(Integer userId);
	List<ActivityWorkPackage> getActivityWorkPackageListByProductId(Integer productId);
	List<ActivityWorkPackage> getActivityWorkPackagesByName(String activityWpName);
	List<Object[]> getActivityWorkpackageByTestFactoryIdProductId(Integer testFactoryId, Integer productId);
}
