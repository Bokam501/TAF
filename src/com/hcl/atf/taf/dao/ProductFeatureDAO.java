package com.hcl.atf.taf.dao;

import java.util.Date;
import java.util.List;

import com.hcl.atf.taf.model.ProductFeature;
import com.hcl.atf.taf.model.ProductFeatureHasTestCase;
import com.hcl.atf.taf.model.ProductFeatureProductBuildMapping;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.TestCasePriority;

public interface ProductFeatureDAO  {	 
	void add (ProductFeature productFeature);
	void update (ProductFeature productFeature);
	void delete (ProductFeature productFeature);
	public TestCaseList updateProductFeatureTestCases(int testCaseId, int productFeatureId);
	List<ProductFeature> list();	
	List<ProductFeature> getFeatureListByProductId(Integer productId, Integer featureStatus, Integer jtStartIndex, Integer jtPageSize);
	List<ProductFeature> getChildFeatureListByParentfeatureId(Integer parentFeatureId);
	List<ProductFeature> getFeatureListExcludingChildByparentFeatureId(Integer productId, Integer parentFeatureId);
	
	ProductFeature getByProductFeatureName(String productFeatureName);
	ProductFeature getByProductFeatureId(int productFeatureId);
	boolean isProductFeatureExistingByName(ProductFeature productFeature);
	boolean isProductFeatureExistingByName(String productFeatureName);
	boolean isProductFeatureExistingByName(String productFeatureName, Integer productId, Integer productFeatureId);
	List<ProductFeature> getFeaturesMappedToTestCase(Integer testCaseId);
	TestCaseList updateProductFeatureTestCasesOneToMany(int testCaseId,	int productFeatureId, String maporunmap);
	Integer getFeatureListSize(Integer productId);
	Integer getUnMappedTestCaseListCountOfFeatureByProductFeatureId(int productId,int productFeatureId);
	List<Object[]> getUnMappedTestCaseListByProductFeatureId(int productId, int productFeatureId, Integer jtStartIndex, Integer jtPageSize);
	Integer geRootProductFeatureId(String rootProductFeatureDescription);
	ProductFeature getByProductFeatureCode(int productFeatureId);
	List<ProductFeature> getFeatureListByProductId(Integer productId, Integer featureStatus, Integer jtStartIndex, Integer jtPageSize, boolean initialize);
	List<ProductFeature> getFeatureListByEnagementId(Integer engagementId, Integer featureStatus, Integer jtStartIndex, Integer jtPageSize, boolean initialize);
	boolean isProductFeatureExistingByFeatureCode(String productFeatureCode, ProductMaster product);
	ProductFeature getByProductFeatureCode(String productFeatureCode);
	ProductFeature getByProductFeatureCode(String productFeatureCode, Integer productId);
	ProductFeature getByProductFeatureCode(String productFeatureCode, ProductMaster product);
	ProductFeature getByProductFeatureName(String productFeatureName, ProductMaster product);
	ProductFeature bulkUpdateOfProductFeature(ProductFeature productFeature,Integer count, Integer maxLimit);
	List<ProductFeature> getRootFeatureListByProductId(Integer productId, Integer jtStartIndex, Integer jtPageSize, Integer rootFeatureId, boolean isInitializationReq);
	List<Object[]> getMappedTestCaseListByProductFeatureId(int productId,
			int productFeatureId, Integer jtStartIndex, Integer jtPageSize);
	Integer getUnMappedTestCaseListCountOfFeatureByProductId(int productId);
	List<ProductFeature> list(Integer startIndex, Integer pageSize, Date startDate,Date endDate);
	Integer countProductFeatures(Date startDate, Date endDate);
	Integer getProductFeatureCount(Integer productId, Integer jtStartIndex,	Integer jtPageSize);
	
	public ProductFeature addProductFeature(ProductFeature productFeature);
	ProductFeature getProductFeatureParentById(int productFeatureParentId);
	List<ProductFeature> listChildNodesInHierarchyinLayers(ProductFeature feature);
	void updateFeatureParent(ProductFeature feature, ProductFeature oldParentFeature, ProductFeature newParentFeature);
	void updateHierarchyIndexForNew(String tableName, int parentRightIndex);
	void updateHierarchyIndexForDelete(String tableName, int leftIndex, int rightIndex);
	void updateHierarchyIndexForDelete(Integer leftIndex, Integer rightIndex);
	void updateHierarchyIndexForNew(Integer parentRightIndex);
	ProductFeature getRootFeature();
	
	List<ProductFeatureProductBuildMapping> getProductFeatureAndProductBuildMappingList(Integer productId);
	
	void mappingProductFeatureToProductBuild(ProductFeatureProductBuildMapping productFeatureProductBuildMapping);
	void unMappingProductFeatureToProductBuild(ProductFeatureProductBuildMapping productFeatureProductBuildMapping);
	List<ProductFeature> getFeatureListByProductIdAndVersionIdAndBuild(Integer productId, Integer versionId, Integer buildId,
			Integer featureStatus, Integer jtStartIndex, Integer jtPageSize);
	List<ProductFeatureProductBuildMapping> getFeatureListByProductIdAndVersionIdAndBuild(Integer productId, Integer versionId, Integer buildId);
	List<ProductFeatureProductBuildMapping> getProductFeatureAndProductBuildMappingId(Integer mappingId);
	List<ProductFeatureProductBuildMapping> getProductFeatureAndProductBuildMappingList();
	
	List<Integer> getFeatureTotestCaseMappingByFeatureId(Integer productFeatureId);
	
	List<ProductFeatureHasTestCase> getProductFeatureHasTestCaseList();
	List<TestCasePriority> listFeatureExecutionPriority();
	
	List<Object[]> getMappedTestScriptListByTestcaseId(int productId, int testcaseId, Integer jtStartIndex,Integer jtPageSize);
	List<ProductFeature> getProductFeatureHasTestCaseId(Integer testCaseId);
	Integer getMappedFeatureCountOfTestCasesByProductId(int productId);
	Integer getMappedFeatureCountByTestcaseId(int testCaseId);
	Integer getMappedTestcasecountByFeatureId(int featureId);
	String updateProductFeatureTestCase(int testCaseId, int productFeatureId, String maporunmap);
	boolean isFeatureExistingByFeatureNameAndFeatureCode(String featureName,String productFeatureCode, ProductMaster product);
}
