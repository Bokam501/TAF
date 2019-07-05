package com.hcl.atf.taf.mongodb.dao;

import java.util.List;

import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.ProductVersionListMaster;
import com.hcl.atf.taf.mongodb.model.ProductVersionMasterMongo;

public interface ProductVersionMasterMongoDAO {
	void save(ProductVersionListMaster productVersionListMaster, ProductMaster product);
	
	void save(ProductVersionMasterMongo productVersionMasterMongo);
	void save(List<ProductVersionMasterMongo> productVersionMasterMongo);

}
