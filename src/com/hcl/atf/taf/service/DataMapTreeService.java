package com.hcl.atf.taf.service;

import org.json.simple.JSONArray;

import com.hcl.atf.taf.model.UserList;

public interface DataMapTreeService {
	JSONArray getFeatureTestCaseMappingTree(int productId);
	JSONArray getChildFeaturesOfParentFeature(int featureId, String node);
	JSONArray getDecouplingTestCaseMappingTree(int productId);
	JSONArray getChildDecouplingCategoriesOfParentCatregory(int featureId, String node);
	JSONArray getTestSuiteTestCaseMappingTree(int productId);
	JSONArray getChildTestSuiteTestCaseMappingTree(int parentId, String node);
	JSONArray getEnvironmentGroupTree();
	JSONArray getEnvironmentCategoryOfParentNodeTree(int parentId, String node);
	JSONArray getProductPlanTree(UserList user);
	JSONArray loadProductVersions(UserList user,int entityId);
	JSONArray loadProductBuilds(UserList user,int entityId);
	JSONArray loadProductWorkPackages(UserList user,int entityId);
	JSONArray getUserSkillsMappingTree();
	JSONArray getChildSkillsOfParentSkill(int skillId, String node);
}
