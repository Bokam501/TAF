package com.hcl.atf.taf.service.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.UserTypeMasterNewDAO;
import com.hcl.atf.taf.model.UserTypeMasterNew;
import com.hcl.atf.taf.service.UserTypeMasterNewService;

@Service
public class UserTypeMasterNewServiceImpl implements UserTypeMasterNewService {
	
	@Autowired
    private UserTypeMasterNewDAO userTypeMasterNewDAO;	

	@Override
	@Transactional
	public UserTypeMasterNew getByuserTypeId(int userTypeId) {
		return userTypeMasterNewDAO.isUserTypeMasterNewById(userTypeId);
	}

	@Override
	@Transactional
	public List<UserTypeMasterNew> list() {
		return userTypeMasterNewDAO.list();
	}

	@Override
	@Transactional
	public UserTypeMasterNew getUserTypeMasterNewByuserTypeLabel(
			String userTypeLabel) {		
		return userTypeMasterNewDAO.getUserTypeMasterNewByuserTypeLabel(userTypeLabel);
	}

	@Override
	@Transactional
	public List<UserTypeMasterNew> listProductUser(Integer typeFilter) {
		return userTypeMasterNewDAO.listProductUser(typeFilter);
	}
	
	
}
