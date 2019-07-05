package com.hcl.atf.taf.service;

import java.util.List;

import com.hcl.atf.taf.model.Customer;
import com.hcl.atf.taf.model.UserCustomerAccount;

public interface CustomerService {
	
	List<Customer> listCustomer(int status, Integer jtStartIndex, Integer jtPageSize);
	boolean isCustomerExistingByName(String customerName);
	Customer getCustomerByName(String customerName);
	Customer getCustomerId(int customerId);
	
	void addCustomer(Customer customer);
	void update (Customer Customer);
	List<UserCustomerAccount> listUserCustomerAccount(Integer userId);
	void addUserCustomerAccount(UserCustomerAccount userCustomerAccount);
	UserCustomerAccount updateUserCustomerAccountInline(Integer userCustomerAccountId, String modifiedField,
			String modifiedFieldValue);
	void updateUserCustomerAccount(UserCustomerAccount userCustomerAccount);
	Customer updateCustomerUserOneToMany(int customerId, int userId,String maporunmap);
	List<Customer> getCustomerByUserProducts(Integer userId, Integer userRoleId, Integer activeStatus);
	
}
