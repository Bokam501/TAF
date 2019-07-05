package com.hcl.atf.taf.dao;

import java.util.List;

import com.hcl.atf.taf.model.DecouplingCategory;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.WorkPackageTestCaseExecutionPlan;

public interface DecouplingCategoryDAO  {	 

	List<DecouplingCategory> getDecouplingCategoryListByProductId(int productId);
	DecouplingCategory getDecouplingCategoryById(int decouplingCategoryId);
	DecouplingCategory getDecouplingCategoryByName(String decouplingCategoryName);	
	
	void update (DecouplingCategory decouplingCategory);
	public TestCaseList updateDecouplingCategoriesTestCases(int testCaseId, int decouplingCategoryId);
	
	List<DecouplingCategory> list();	
	boolean isProductDecouplingCategoryExistingByName(DecouplingCategory decouplingCategory);	
	List<DecouplingCategory> getDCByWorkpackage(List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlanList,int workPackageId);
	void add(DecouplingCategory decouplingCategory);
	void delete(DecouplingCategory decouplingCategory);
	TestCaseList updateDecouplingCategoriesTestCasesOneToMany(Integer testCaseId, int decouplingCategoryId, String maporunmap);
	List<DecouplingCategory> getDecouplingCategoriesMappedToTestCase(Integer testCaseId);
	int getRootDecouplingCategoryId();
	List<DecouplingCategory> getChildCategoriesListByParentCategoryId(Integer parentCategoryId);
	boolean isProductDecouplingCategoryExistingByName(DecouplingCategory decouplingCategory, Integer productId);
	Integer geRootDecouplingCategoryId(String rootDecouplingCategoryDescription);
	DecouplingCategory getRootDecouplingCategory();
	List<DecouplingCategory> listChildNodesInHierarchyinLayers(DecouplingCategory decouplingCategory);
}
