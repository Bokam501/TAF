package com.hcl.atf.taf.dao;

import java.util.List;

import com.hcl.atf.taf.model.Customer;

public interface CustomerDAO  {	
	List<Customer> list(int status, Integer jtStartIndex, Integer jtPageSize);
	boolean isCustomerExistingByName(String customerName);
	Customer getCustomerByName(String customerName);
	Customer getByCustomerId(int customerId);
	
	void add(Customer customer);
	void update(Customer customer);
	Customer updateCustomerUserOneToMany(int customerId, int userId,
			String maporunmap);
	List<Customer> getCustomerByUserProducts(Integer userId, Integer userRoleId, Integer activeStatus);
	
}
