package com.hcl.atf.taf.dao;

import java.util.Date;
import java.util.List;

import com.hcl.atf.taf.model.ProductVersionListMaster;

public interface ProductVersionListMasterDAO  {	 

	void add (ProductVersionListMaster productVersionListMaster);
	void update (ProductVersionListMaster productVersionListMaster);
	void delete (ProductVersionListMaster productVersionListMaster);
	List<ProductVersionListMaster> list();
	List<ProductVersionListMaster> list(int productId);
	List<ProductVersionListMaster> list(int startIndex, int pageSize);
	List<ProductVersionListMaster> list(int productId,int startIndex, int pageSize);
	ProductVersionListMaster getByProductListId(int productVersionListId);
	int getTotalRecords();
	List<ProductVersionListMaster> list(int productId, String[] parameters);
	
	boolean isProductVersionExistingByName(ProductVersionListMaster productVersionListMaster);
	boolean isProductVersionExistingByNameForUpdate(ProductVersionListMaster productVersionListMaster, int productVersionListId);
	List<ProductVersionListMaster> listProductVersions(int productId, int status);
	void reactivate(int productVersionId);
	ProductVersionListMaster productVersionListByName(String productVersionName);
	List<ProductVersionListMaster> getUserAssociatedProductVariants(int userRoleId, int userId);
	int getProductTypeByVersionId(int productVersionListId, int productId, int workPackageId);
	Integer countAllProductVersions(Date startDate,Date endDate);
	List<ProductVersionListMaster> listAllProductVersionByLastSyncDate(int startIndex,int pageSize, Date startDate,Date endDate);
	List<Integer> listVersionId(int productId);
	ProductVersionListMaster getLatestProductVersionListMaster(Integer productId);
	int getProductVersionIdBybuildId(int productBuildId);
	ProductVersionListMaster getProductVersionListByProductIdAndVersionName(Integer productId,String productVersionName);
}
