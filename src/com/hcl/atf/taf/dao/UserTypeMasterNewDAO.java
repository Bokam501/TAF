package com.hcl.atf.taf.dao;

import java.util.List;

import com.hcl.atf.taf.model.UserTypeMasterNew;

public interface UserTypeMasterNewDAO  {
	UserTypeMasterNew getByuserTypeId(int userTypeId);
	UserTypeMasterNew isUserTypeMasterNewById(Integer userId);
	List<UserTypeMasterNew> list();
	UserTypeMasterNew getUserTypeMasterNewByuserTypeLabel(String userTypeLabel);
	List<UserTypeMasterNew> listProductUser(Integer typeFilter);

}
