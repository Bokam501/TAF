package com.hcl.atf.taf.mongodb.dao;

import java.util.List;

import com.hcl.atf.taf.model.json.JsonProductMaster;
import com.hcl.atf.taf.mongodb.model.ProductMasterMongo;

public interface  ProductMasterMongoDAO {
		
	void save(JsonProductMaster product);
	void save(ProductMasterMongo productMasterMongo);
	void save(List<ProductMasterMongo> productMasterMongo);
	


}
