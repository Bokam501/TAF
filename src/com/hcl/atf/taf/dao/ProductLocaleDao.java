package com.hcl.atf.taf.dao;

import java.util.List;

import com.hcl.atf.taf.model.ProductLocale;

public interface ProductLocaleDao {
	List<ProductLocale> list();
	public List<ProductLocale> getProductLocaleListByProductId(Integer productId);
	void addLocale(ProductLocale locale);
	void updateProductLocale(ProductLocale locale);
	ProductLocale getLocaleByName(String localeName);
	ProductLocale getProductLocaleById(int localeId);
	ProductLocale getLocaleByNameByProduct(String localeName,String productId);
	
}
