package com.hcl.atf.taf.service;

import java.util.List;

import org.json.simple.JSONArray;

import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.json.JsonProductBuild;
import com.hcl.atf.taf.model.json.JsonWorkPackageTestCaseExecutionPlanForTester;
public interface DataTreeService {
	String getWorkPackageTree(int userRoleId, int userId);
	JSONArray getWorkPackagePlanTreeInJSON(int userRoleId, int userId, int treeType,String parentType,int parentId);
	String getWorkPackagePlanTree(int userRoleId, int userId, int treeType);
	String getProductTree(int userRoleId, int userId);
	String getProductTreeWithFolder(int userRoleId, int userId);
	String getProductHierarchyTree(int userRoleId, int userId);
	String getUserTypeTree(int userRoleId, int userId);
	String getEodShiftsReportTree(int userRoleId, int userId);
	String getResourcePoolTree(int userRoleId, int userId);
	String getTestFactoryProductTree(int userRoleId, int userId);
	String getTestFactoryLabTestFactoryProductTree(int userRoleId, int userId);
	String getTestFactoryShiftTree(int userRoleId, int userId, int treeType);
	String getEnvironmentCategoryGroupTree(int userRoleId, int userId);
	String getTestFactoryLabTestFactoryProductsCompleteTree(Integer userRoleId,Integer userId);
	String getUserVendorTree(Integer userRoleId, Integer userId);
	String getProductVersionTree(int userRoleId, int userId);
	String getAllocateTestcaseFilter(UserList user, int workpackageId,String param);
	List<ProductMaster> getUserAssociatedProducts(int userRoleId, int userId, int filter);
	String getProductWithTFTree(int userRoleId, int userId);
	String getDeviceLabTree(int userRoleId, int userId);
	String getWorkPackageTestCaseReviewTree(Integer userRoleId, Integer userId);
	String getProductActivityWorkPackageTree(int userRoleId, int userId);
	String getProductActivityTree(int userRoleId, int userId,String filter);
	String getCustomersTreeData(int userRoleId, int userId);
	String getActivityTree(int userRoleId, int userId, String filer);
	String getActivityTreeForPqaReviewer(Integer userRoleId, Integer userId,String roleType);
	
	String getDimensionTreeByType(int userRoleId, int userId, String filer, Integer dimensionTypeId);
	
	JSONArray getProductTestingTeamActivityTreeJSON(int userRoleId, int userId,
			int treeType, String parentType, int parentId, int isActive);
	String getProductTreeForMyWorkflowActions(int userRoleId, int userId);
	JSONArray getActivityWorkPackageTaskPlanTreeInJSON(int userRoleId, int userId, int treeType, String parentType, int parentId);
	JSONArray getPivotReportViewAdvanceTree(String nodeType, Integer parentId);
	String getResourcePoolTreeFromLab(UserList userList);
	JSONArray getProductAndWPListForDemandAndReservationInJson(int userRoleId,int userId, int treeType, String parentType, int parentId);
	String getProductActivityWorkPackageTreeByProductId(Integer productId);
	String getProductHierarchyTreeByProductId(Integer productId);
	String getProductBotTree(int userRoleId, int userId);
	
	String getProductBuildNodeTree(int userRoleId, int userId);
	List<JsonProductBuild> getProductBuildDetails(int userRoleId, int userId,String filter);
	List<JsonWorkPackageTestCaseExecutionPlanForTester> getUserRolebasedCompletedAndAbortedWorkpackges(int userRoleId, int userId); 
}
