package com.hcl.atf.taf.mongodb.dao;

import java.util.List;

import com.hcl.atf.taf.mongodb.model.ProductUserRoleMongo;

public interface  ProductUserRoleMongoDAO {
		
	void save(ProductUserRoleMongo productUserRoleMongo);

	void save(List<ProductUserRoleMongo> productUserRoleMongoList);

	
	


}
