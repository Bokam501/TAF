package com.hcl.atf.taf.dao;

import java.util.Date;
import java.util.List;

import com.hcl.atf.taf.model.ProductUserRole;
import com.hcl.atf.taf.model.UserList;

public interface ProductUserRoleDAO  {	
	void add (ProductUserRole productUserRole);
	void update (ProductUserRole productUserRole);
	ProductUserRole getByproductUserRoleId(int productUserRoleId);
	ProductUserRole getProductUserRoleByUserId(int userId);	
	ProductUserRole getProductUserRoleByUserIdAndProductId(int userId, int productId);
	List<UserList> getProductResources(int productId);
	Integer countAllProductUserRole(Date startDate, Date endDate);
	List<ProductUserRole> listAllPaginate(int i, int pageSize,Date startDate, Date endDate);
}
