package com.hcl.atf.taf.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.DecouplingCategoryDAO;
import com.hcl.atf.taf.dao.ProductMasterDAO;
import com.hcl.atf.taf.dao.UserListDAO;
import com.hcl.atf.taf.dao.UserTypeMasterNewDAO;
import com.hcl.atf.taf.dao.WorkPackageDAO;
import com.hcl.atf.taf.model.DecouplingCategory;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.UserTypeMasterNew;
import com.hcl.atf.taf.service.DecouplingCategoryService;

@Service
public class DecouplingCategoryServiceImpl implements DecouplingCategoryService {

	@Autowired
	private DecouplingCategoryDAO decouplingCategoryDAO;
	@Autowired
    private ProductMasterDAO productMasterDAO;
	@Autowired
    private UserListDAO userListDAO;
	@Autowired
    private UserTypeMasterNewDAO userTypeMasterNewDAO;
	@Autowired
	private WorkPackageDAO workPackageDAO;
	
	@Override
	@Transactional
	public List<DecouplingCategory> getDecouplingCategoryListByProductId(int productId) {
		return decouplingCategoryDAO.getDecouplingCategoryListByProductId(productId);
	}
	
	@Override
	@Transactional
	public DecouplingCategory getDecouplingCategoryById(int decouplingCategoryId) {
		return decouplingCategoryDAO.getDecouplingCategoryById(decouplingCategoryId);
	}
	
	@Override
	@Transactional
	public DecouplingCategory getDecouplingCategoryByName(
			String decouplingCategoryName) {

		return decouplingCategoryDAO.getDecouplingCategoryByName(decouplingCategoryName);
	}	
	
	
	@Override
	@Transactional
	public TestCaseList updateDecouplingCategoriesTestCases(int testCaseId, int decouplingCategoryId) {
		return decouplingCategoryDAO.updateDecouplingCategoriesTestCases(testCaseId, decouplingCategoryId);
	}	

	@Override
	@Transactional
	public TestCaseList updateDecouplingCategoriesTestCasesOneToMany(
			int testCaseId, int decouplingCategoryId, String maporunmap) {		
		return decouplingCategoryDAO.updateDecouplingCategoriesTestCasesOneToMany(testCaseId, decouplingCategoryId, maporunmap);
	}	
	
	@Override
	@Transactional
	public boolean isDecouplingCategoryExistingByName(DecouplingCategory decouplingCategory) {
		return decouplingCategoryDAO.isProductDecouplingCategoryExistingByName(decouplingCategory);
	}
	
	@Override
	@Transactional
	public boolean isProductDecouplingCategoryExistingByName(DecouplingCategory decouplingCategory, Integer productId){
		return decouplingCategoryDAO.isProductDecouplingCategoryExistingByName(decouplingCategory, productId);
	}
	
	@Override
	@Transactional
	public void addProductDecouplingCategory(DecouplingCategory decouplingCategory) {		
		
		ProductMaster product = new ProductMaster();
		product.setProductId(decouplingCategory.getProduct().getProductId());
		decouplingCategory.setProduct(product);

		UserTypeMasterNew userTypeMasterNew = userTypeMasterNewDAO.getByuserTypeId(decouplingCategory.getUserTypeMasterNew().getUserTypeId());			
		decouplingCategory.setUserTypeMasterNew(userTypeMasterNew);
	
		if(decouplingCategory.getParentCategory() != null && decouplingCategory.getParentCategory().getDecouplingCategoryId() != null){
			DecouplingCategory parentCategory = decouplingCategoryDAO.getDecouplingCategoryById(decouplingCategory.getParentCategory().getDecouplingCategoryId());
			decouplingCategory.setParentCategory(parentCategory);
			
			//Create display name from parent 
			StringBuffer displayName = new StringBuffer();
			displayName.append(decouplingCategory.getDecouplingCategoryName());
			boolean hasParent = true;
			while (hasParent) {
				if (parentCategory.getParentCategory() != null)
					displayName.insert(0, parentCategory.getDecouplingCategoryName() + " | ");
				parentCategory = parentCategory.getParentCategory();
				if (parentCategory == null)
					hasParent = false;
			}
			decouplingCategory.setDisplayName(displayName.toString());
		}else{
			decouplingCategory.setParentCategory(null);
			decouplingCategory.setDisplayName(decouplingCategory.getDecouplingCategoryName());			
		}
		
		decouplingCategoryDAO.add(decouplingCategory);
	}
	
	@Override
	@Transactional
	public DecouplingCategory updateProductDecouplingCategory(DecouplingCategory decouplingCategory) {
		
		ProductMaster product = new ProductMaster();
		product.setProductId(decouplingCategory.getProduct().getProductId());
		decouplingCategory.setProduct(product);

		UserTypeMasterNew userTypeMasterNew = userTypeMasterNewDAO.getByuserTypeId(decouplingCategory.getUserTypeMasterNew().getUserTypeId());			
		decouplingCategory.setUserTypeMasterNew(userTypeMasterNew);
	
		if(decouplingCategory.getParentCategory() !=null && decouplingCategory.getParentCategory().getDecouplingCategoryId() !=null && !decouplingCategory.getParentCategory().getDecouplingCategoryId().equals("0")){
			DecouplingCategory parentCategory = decouplingCategoryDAO.getDecouplingCategoryById(decouplingCategory.getParentCategory().getDecouplingCategoryId());
			decouplingCategory.setParentCategory(parentCategory);
			
			//Create display name from parent 
			StringBuffer displayName = new StringBuffer();
			displayName.append(decouplingCategory.getDecouplingCategoryName());
			boolean hasParent = true;
			while (hasParent) {
				if (parentCategory.getParentCategory() != null)
				displayName.insert(0, parentCategory.getDecouplingCategoryName() + " | ");
				parentCategory = parentCategory.getParentCategory();
				if (parentCategory == null)
					hasParent = false;
			}
			decouplingCategory.setDisplayName(displayName.toString());
		
		}else{
			decouplingCategory.setDisplayName(decouplingCategory.getDecouplingCategoryName());
		}

		decouplingCategoryDAO.update(decouplingCategory);	
		return decouplingCategory;
	}

	@Override
	@Transactional
	public List<DecouplingCategory> listDecouplingCategories() {		
		return decouplingCategoryDAO.list();
	}

	@Override
	@Transactional
	public List<DecouplingCategory> getDCByWorkpackage(int workPackageId) {
		return decouplingCategoryDAO.getDCByWorkpackage(workPackageDAO.getWorkPackageTestCaseExecutionPlanByWorkpackgeId(workPackageId),workPackageId);
	}

	@Override
	@Transactional
	public List<DecouplingCategory> getDecouplingCategoriesMappedToTestCase(
			Integer testCaseId) {
		return decouplingCategoryDAO.getDecouplingCategoriesMappedToTestCase(testCaseId);
	}

	@Override
	@Transactional
	public DecouplingCategory getRootDecouplingCategory() {
		return decouplingCategoryDAO.getRootDecouplingCategory();
	}

	@Override
	public List<DecouplingCategory> listChildNodesInHierarchyinLayers(
			DecouplingCategory decouplingCategory) {
		return decouplingCategoryDAO.listChildNodesInHierarchyinLayers(decouplingCategory);
	}
	
	
}
