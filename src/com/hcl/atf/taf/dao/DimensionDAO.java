package com.hcl.atf.taf.dao;

import java.util.List;

import com.hcl.atf.taf.model.DimensionMaster;
import com.hcl.atf.taf.model.DimensionProduct;
import com.hcl.atf.taf.model.DimensionType;

public interface DimensionDAO {
	
	void addDimension(DimensionMaster dimensionMaster);
	List<DimensionMaster> getDimensionList(Integer status, Integer jtStartIndex, Integer jtPageSize, boolean allowRootDimension, Integer dimensionTypeId);
	int getTotalRecordsForDimensionPagination(Integer status, Class<DimensionMaster> className, boolean allowRootDimension, Integer dimensionTypeId);
	void updateDimension(DimensionMaster dimensionMaster);
	DimensionMaster getDimensionById(Integer dimensionId);
	DimensionMaster getDimensionByNameAndType(String dimensionName, Integer referenceId, Integer dimensionTypeId);
	
	List<Integer> getDimensionIdByProductId(Integer productId, Integer dimensionTypeId);
	List<DimensionType> getDimensionTypeList();
	List<DimensionMaster> getDimensionListByUserId(Integer status, Integer userId, Integer dimensionTypeId);
	
	List<DimensionProduct> getDimensionProducts(Integer dimensionId, Integer status, Integer jtStartIndex, Integer jtPageSize);
	int getTotalRecordsForDimensionProductPagination(Integer dimensionId, Integer status, Class<DimensionProduct> className);
	void updateDimensionProduct(DimensionProduct dimensionProduct);
	List<Object[]> getDimensionsToAddWithProduct(Integer productId, boolean allowRootDimension, Integer dimensionTypeId, Integer status, Integer jtStartIndex, Integer jtPageSize);
	Integer getDimensionsToAddWithProductCount(Integer productId, boolean allowRootDimension, Integer dimensionTypeId, Integer status);
	List<Object[]> getDimensionsOfProduct(Integer productId, Integer dimensionTypeId, Integer status, Integer jtStartIndex, Integer jtPageSize);
	int getTotalRecordsForDimensionOfProductPagination(Integer productId, Integer dimensionTypeId, Integer status, Class<DimensionProduct> className);
	DimensionProduct getDimensionProductMappedByProductId(Integer productId, Integer dimensionId, Integer dimensionProductId);
	void addDimensionForProduct(DimensionProduct dimensionProduct, String mapOrUnmap);
	
}
