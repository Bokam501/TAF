package com.hcl.atf.taf.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.dao.CustomerDAO;
import com.hcl.atf.taf.dao.UserCustomerAccountDAO;
import com.hcl.atf.taf.model.Customer;
import com.hcl.atf.taf.model.UserCustomerAccount;
import com.hcl.atf.taf.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {
	private static final Log log = LogFactory.getLog(CustomerServiceImpl.class);
	
	@Autowired
	private CustomerDAO customerDAO;
	@Autowired
	private UserCustomerAccountDAO userCustomerAccountDAO;

	@Override
	@Transactional
	public List<Customer> listCustomer(int status, Integer jtStartIndex, Integer jtPageSize) {
		return customerDAO.list(status, jtStartIndex, jtPageSize);
	}

	@Override
	@Transactional
	public boolean isCustomerExistingByName(String customerName) {
		return customerDAO.isCustomerExistingByName(customerName);
	}

	@Override
	@Transactional
	public Customer getCustomerByName(String customerName) {
		return customerDAO.getCustomerByName(customerName);
	}

	@Override
	@Transactional
	public void addCustomer(Customer customer) {
		customerDAO.add(customer);
		
	}

	@Override
	@Transactional
	public Customer getCustomerId(int customerId) {		
		return customerDAO.getByCustomerId(customerId);
	
	}
	
	@Override
	@Transactional
	public void update(Customer Customer) {
		customerDAO.update(Customer);
		
	}

	@Override
	public List<UserCustomerAccount> listUserCustomerAccount(Integer userId) {
		return userCustomerAccountDAO.list(userId);
	}

	@Override
	public void addUserCustomerAccount(UserCustomerAccount userCustomerAccount) {
		userCustomerAccountDAO.add(userCustomerAccount);
	}

	@Override
	public UserCustomerAccount updateUserCustomerAccountInline(Integer userCustomerAccountId,
			String modifiedField, String modifiedFieldValue) {
		UserCustomerAccount userCustomerAccount = userCustomerAccountDAO.listUserCustomerAccountByUserCustomerAccountId(userCustomerAccountId);
		if (modifiedField.equalsIgnoreCase("userCustomerCode")) {
			
			userCustomerAccount.setUserCustomerCode(modifiedFieldValue);
		}else if (modifiedField.equalsIgnoreCase("userCustomerEmailId")) {
			
			userCustomerAccount.setUserCustomerEmailId(modifiedFieldValue);
		}else if (modifiedField.equalsIgnoreCase("validFromDate")) {
			String date=modifiedField;
			log.info("date"+date);
			Date modifiedFromDate = DateUtility.dateFormatWithOutSeconds(modifiedFieldValue);
			userCustomerAccount.setValidFromDate(modifiedFromDate);
		}else if (modifiedField.equalsIgnoreCase("validTillDate")) {
			Date modifiedToDate =  DateUtility.dateFormatWithOutSeconds(modifiedFieldValue);
			userCustomerAccount.setValidTillDate(modifiedToDate);
		}
		userCustomerAccountDAO.updateUserCustomerAccountInline(userCustomerAccount);
		return userCustomerAccount;
	}

	@Override
	@Transactional
	public void updateUserCustomerAccount(UserCustomerAccount userCustomerAccount) {
		userCustomerAccountDAO.updateUserCustomerAccountInline(userCustomerAccount);
	}

	@Override
	@Transactional
	public Customer updateCustomerUserOneToMany(int customerId, int userId,
			String maporunmap) {
		return customerDAO.updateCustomerUserOneToMany(customerId, userId, maporunmap);
	}

	@Override
	@Transactional
	public List<Customer> getCustomerByUserProducts(Integer userId,	Integer userRoleId, Integer activeStatus) {
		return customerDAO.getCustomerByUserProducts(userId, userRoleId, activeStatus);
	}

	

}
