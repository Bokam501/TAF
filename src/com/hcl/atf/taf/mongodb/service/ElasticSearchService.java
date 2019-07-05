package com.hcl.atf.taf.mongodb.service;

import com.hcl.atf.taf.model.ProductMaster;


public interface ElasticSearchService {
	
	String elasticSearchTestcases(ProductMaster productMaster,String type);
	void initializeClient(String hostName,String clusterName);
	void closeClient();

}