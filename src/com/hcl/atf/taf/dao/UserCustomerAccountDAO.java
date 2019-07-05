package com.hcl.atf.taf.dao;

import java.util.List;

import com.hcl.atf.taf.model.UserCustomerAccount;

public interface UserCustomerAccountDAO {

	List<UserCustomerAccount> list(Integer userId);
	void add(UserCustomerAccount userCustomerAccount);
	UserCustomerAccount listUserCustomerAccountByUserCustomerAccountId(Integer userCustomerAccountId);
	void updateUserCustomerAccountInline(UserCustomerAccount userCustomerAccount);
	UserCustomerAccount getUserCustomerAccountByUserIdCustomerId(Integer userId,Integer customerId);
}
