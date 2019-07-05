package com.hcl.atf.taf.service;

import java.util.List;

import com.hcl.atf.taf.model.UserTypeMasterNew;

public interface UserTypeMasterNewService {
	UserTypeMasterNew getByuserTypeId(int userTypeId);
	List<UserTypeMasterNew> list();
	UserTypeMasterNew getUserTypeMasterNewByuserTypeLabel(String userTypeLabel);
	
	List<UserTypeMasterNew> listProductUser(Integer typeFilter);
}

