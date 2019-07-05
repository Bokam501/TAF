package com.hcl.atf.taf.dao;

import java.util.List;

import com.hcl.atf.taf.model.Environment;

public interface ProductTestEnvironmentDAO {

	List<Environment> list();
	public List<Environment> getEnvironmentListByProductId(Integer productId);
	public Environment getByEnvironmentId(int environmentId);
	public void add(Environment environment);
	public void update(Environment environment);
	void delete(Environment environment);
	boolean isProductEnvironmentExistingByName(Environment environment);	
	public List<Environment> getEnvironmentListByProductIdAndStatus(Integer productId,Integer status);
}
