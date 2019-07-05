package com.hcl.atf.taf.mongodb.dao;

import java.util.List;

import com.hcl.atf.taf.model.ProductBuild;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.mongodb.model.ISEProductBuildMongo;
import com.hcl.atf.taf.mongodb.model.ProductBuildMongo;

public interface BuildMongoDAO {
	void save(ProductBuild productBuild, String productName, UserList user);
	void save(ProductBuildMongo productBuildMongo);
	void save(List<ProductBuildMongo> productBuildMongo);
	void saveISE(ISEProductBuildMongo iseProductBuildMongo);
}
