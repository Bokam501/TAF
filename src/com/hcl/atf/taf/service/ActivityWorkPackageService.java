package com.hcl.atf.taf.service;

import java.util.List;
import java.util.Map;

import com.hcl.atf.taf.model.ActivityWorkPackage;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.dto.ActivityWorkPackageSummaryDTO;
import com.hcl.atf.taf.model.dto.ProductAWPSummaryDTO;
import com.hcl.atf.taf.model.json.JsonActivityWorkPackage;

public interface ActivityWorkPackageService {
	List<JsonActivityWorkPackage> listActivityWorkPackagessByBuildId(Integer productId,Integer productVersionId,Integer productBuildId, int isActive, Map<String, String> searchStrings,UserList user);
    List<ActivityWorkPackage> listActivityWorkPackages();
	void addActivityWorkPackage(ActivityWorkPackage workRequest);
	void updateActivityWorkPackage(ActivityWorkPackage workRequest);
    ActivityWorkPackage getActivityWorkPackageById(Integer activityWorkPackageId, Integer initializationLevel);
    void updateActivity(String[] activityBulkLists, Integer categoryId,
			Integer assigneeId, String plannedEndDate, Integer priorityId,
			Integer reviewerId);
    ActivityWorkPackage getActivityWorkPackageByName(String activityWpName,Integer productBuildId);
	void updateActivityTaskBulkComments(String resultComments,
			String entityValue, String[] activityTaskBulkLists,
			String raisedByValue, String commentsTypeValue,String resultRaisedDateValue);
	String deleteActivityWorkPackage(int activityWorkPackageId, String referencedTableName, String referencedColumnName);
	List<JsonActivityWorkPackage> listActivityWorkPackagesByTestFactoryId(Integer testFactoryId, Integer productId, Integer isActive, Map<String, String> searchStrings,UserList user, Integer jtStartIndex, Integer jtPageSize);
	Integer getActivityWPCountByTestFactoryIdProductId(int testFactoryId, Integer productId);
	Integer listActivityWorkPackagesByProductIdCount(Integer productId, Integer productVersionId, Integer productBuildId, Integer isActive, Map<String, String> searchString);
	 List<ActivityWorkPackage> getActivityWorkPackageByBuildId(Integer buildId);
	 ActivityWorkPackageSummaryDTO listActivityWorkPackageSummaryDetails(Integer activityWorkPackageId);
	ProductAWPSummaryDTO listProductAWPSummaryDetails(Integer productId);
	List<Integer> getActivityWorkPackagesofProductIDS(int productId, List<Integer> productIdList, boolean paramIsList);
	ActivityWorkPackage getLatestActivityWPByProductId(Integer productId);
	ActivityWorkPackage getLastestActivityWorkPackageByNameInProduct(String workpackageName, Integer productId);
	List<ActivityWorkPackage> getActivityWorkPackageListByProductId(Integer productId);
	List<ActivityWorkPackage> getActivityWorkPackagesByName(String activityWpName);
	List<Object[]> getActivityWorkpackageByTestFactoryIdProductId(Integer testFactoryId, Integer productId);
}

