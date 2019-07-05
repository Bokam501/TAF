package com.hcl.atf.taf.service;

import java.util.List;

import com.hcl.atf.taf.model.DecouplingCategory;
import com.hcl.atf.taf.model.TestCaseList;

public interface DecouplingCategoryService {

	List<DecouplingCategory> getDecouplingCategoryListByProductId(int productId);
	List<DecouplingCategory> listDecouplingCategories();
	DecouplingCategory getDecouplingCategoryById(int decouplingCategoryId);
	DecouplingCategory getDecouplingCategoryByName(String decouplingCategoryName);
	
	DecouplingCategory updateProductDecouplingCategory (DecouplingCategory decouplingCategory);
	TestCaseList updateDecouplingCategoriesTestCasesOneToMany(int testCaseId, int decouplingCategoryId, String maporunmap);
	List<DecouplingCategory> getDecouplingCategoriesMappedToTestCase(Integer testCaseId);
	TestCaseList updateDecouplingCategoriesTestCases(int testCaseId, int decouplingCategoryId);
	boolean isDecouplingCategoryExistingByName(DecouplingCategory decouplingCategory);
	void addProductDecouplingCategory (DecouplingCategory decouplingCategory);
	List<DecouplingCategory> getDCByWorkpackage(int workPackageId);
	boolean isProductDecouplingCategoryExistingByName(DecouplingCategory decouplingCategory, Integer productId);
	DecouplingCategory getRootDecouplingCategory();
	List<DecouplingCategory> listChildNodesInHierarchyinLayers(DecouplingCategory decouplingCategory);
}
