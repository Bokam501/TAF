package com.hcl.atf.taf.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.DimensionDAO;
import com.hcl.atf.taf.model.DimensionMaster;
import com.hcl.atf.taf.model.DimensionProduct;
import com.hcl.atf.taf.model.DimensionType;
import com.hcl.atf.taf.service.DimensionService;

@Service
public class DimensionServiceImpl implements DimensionService {

	@Autowired
	private DimensionDAO dimensionDAO;
	
	@Override
	@Transactional
	public void addDimension(DimensionMaster dimensionMaster) {
		dimensionDAO.addDimension(dimensionMaster);
	}
	
	@Override
	@Transactional
	public List<DimensionMaster> getDimensionList(Integer status, Integer jtStartIndex, Integer jtPageSize, boolean allowRootDimension, Integer dimensionTypeId) {
		return dimensionDAO.getDimensionList(status, jtStartIndex, jtPageSize, allowRootDimension, dimensionTypeId);
	}
	
	@Override
	@Transactional
	public Integer getTotalRecordsForDimensionPagination(Integer status, Class<DimensionMaster> className, boolean allowRootDimension, Integer dimensionTypeId) {
		return dimensionDAO.getTotalRecordsForDimensionPagination(status, className, allowRootDimension, dimensionTypeId);
	}
	
	@Override
	@Transactional
	public void updateDimension(DimensionMaster dimensionMaster) {
		dimensionDAO.updateDimension(dimensionMaster);	
	}

	@Override
	@Transactional
	public DimensionMaster getDimensionById(Integer dimensionId) {
		return dimensionDAO.getDimensionById(dimensionId);
	}
	
	@Override
	@Transactional
	public DimensionMaster getDimensionByNameAndType(String dimensionName, Integer referenceId, Integer dimensionTypeId) {
		return dimensionDAO.getDimensionByNameAndType(dimensionName, referenceId, dimensionTypeId);
	}

	@Override
	@Transactional
	public List<Integer> getDimensionIdByProductId(Integer productId, Integer dimensionTypeId){
		return dimensionDAO.getDimensionIdByProductId(productId, dimensionTypeId);
	}

	@Override
	@Transactional
	public List<DimensionType> getDimensionTypeList() {
		return dimensionDAO.getDimensionTypeList();
	}

	@Override
	@Transactional
	public List<DimensionProduct> getDimensionProducts(Integer dimensionId, Integer status, Integer jtStartIndex, Integer jtPageSize) {
		return dimensionDAO.getDimensionProducts(dimensionId, status, jtStartIndex, jtPageSize);
	}

	@Override
	@Transactional
	public int getTotalRecordsForDimensionProductPagination(Integer dimensionId, Integer status, Class<DimensionProduct> className) {
		return dimensionDAO.getTotalRecordsForDimensionProductPagination(dimensionId, status, className);
	}

	@Override
	@Transactional
	public void updateDimensionProduct(DimensionProduct dimensionProduct) {
		dimensionDAO.updateDimensionProduct(dimensionProduct);		
	}

	@Override
	@Transactional
	public List<Object[]> getDimensionsToAddWithProduct(Integer productId, boolean allowRootDimension, Integer dimensionTypeId, Integer status, Integer jtStartIndex, Integer jtPageSize) {
		return dimensionDAO.getDimensionsToAddWithProduct(productId, allowRootDimension, dimensionTypeId, status, jtStartIndex, jtPageSize);
	}

	@Override
	@Transactional
	public Integer getDimensionsToAddWithProductCount(Integer productId, boolean allowRootDimension, Integer dimensionTypeId, Integer status) {
		return dimensionDAO.getDimensionsToAddWithProductCount(productId, allowRootDimension, dimensionTypeId, status);
	}

	@Override
	@Transactional
	public List<Object[]> getDimensionsOfProduct(Integer productId, Integer dimensionTypeId, Integer status, Integer jtStartIndex, Integer jtPageSize) {
		return dimensionDAO.getDimensionsOfProduct(productId, dimensionTypeId, status, jtStartIndex, jtPageSize);
	}

	@Override
	@Transactional
	public int getTotalRecordsForDimensionOfProductPagination(Integer productId, Integer dimensionTypeId, Integer status, Class<DimensionProduct> className) {
		return dimensionDAO.getTotalRecordsForDimensionOfProductPagination(productId, dimensionTypeId, status, className);
	}

	@Override
	@Transactional
	public DimensionProduct getDimensionProductMappedByProductId(Integer productId, Integer dimensionId, Integer dimensionProductId) {
		return dimensionDAO.getDimensionProductMappedByProductId(productId, dimensionId, dimensionProductId);
	}

	@Override
	@Transactional
	public void addDimensionForProduct(DimensionProduct dimensionProduct, String mapOrUnmap) {
		dimensionDAO.addDimensionForProduct(dimensionProduct, mapOrUnmap);
	}
	
}
