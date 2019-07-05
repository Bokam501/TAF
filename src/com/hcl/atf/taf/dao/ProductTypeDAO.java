package com.hcl.atf.taf.dao;

import java.util.List;




import com.hcl.atf.taf.model.ProductType;

public interface ProductTypeDAO  {	 
	List<ProductType> getProductList();

	ProductType getProductTypeById(Integer productTypeId);
}
