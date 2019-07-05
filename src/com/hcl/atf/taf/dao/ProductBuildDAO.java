package com.hcl.atf.taf.dao;

import java.util.Date;
import java.util.List;

import com.hcl.atf.taf.model.ProductBuild;
import com.hcl.atf.taf.model.RunConfiguration;

public interface ProductBuildDAO  {	 

	Integer add (ProductBuild productBuild);
	void update (ProductBuild productBuild);
	void delete (ProductBuild productBuild);
	List<ProductBuild> list();
	List<ProductBuild> list(int productVersionMasterListId);
	List<ProductBuild> list(int startIndex, int pageSize);
	List<ProductBuild> list(int productVersionMasterListId,int startIndex, int pageSize);
	ProductBuild getByProductBuildId(int productBuildId, int listOrObjInitialize);
	ProductBuild getProductBuildIdWithCompleteInitialization(int productBuildId);
	int getTotalRecords();
	List<ProductBuild> list(int productVersionListId, String[] parameters);
	
	boolean isProductBuildExistingByName(ProductBuild productBuild);
	List<ProductBuild> listProductBuilds(int productVersionMasterListId, int status);
	void reactivate(int productBuildId);
	ProductBuild productBuildByName(String productBuildName);
	List<ProductBuild> getUserAssociatedProductBuilds(int userRoleId,int userId);
	RunConfiguration getRunConfigurationById(Integer runconfigId);
	ProductBuild getLatestProductBuild(Integer productId);
	List<ProductBuild> listBuildsByProductId(int productId);
	Integer countAllProductBuilds(Date startDate, Date endDate);
	List<ProductBuild> listAllProductBuildByLastSyncDate(int startIndex,
			int pageSize, Date startDate, Date endDate);
	Integer countAllProductTestCases(Date startDate,Date endDate);
	
	List<ProductBuild> getProductBuildDetailsByVersionIdAndDayORWeek(int productVersionMasterListId,String filter);
	RunConfiguration getRunConfigurationByIdWithoutInitialization(Integer runconfigId);
	ProductBuild getproductBuildByProductIdAndBuildName(Integer productVersionId,String productBuildName);
	
}
