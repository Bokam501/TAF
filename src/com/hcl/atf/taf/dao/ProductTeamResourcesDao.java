package com.hcl.atf.taf.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.ProductTeamResources;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.json.JsonUserList;

public interface ProductTeamResourcesDao {
	public List<ProductTeamResources> getProductTeamResourcesList(int productId, Integer jtStartIndex, Integer jtPageSize);

	public void addProductTeamResource(ProductTeamResources pTeamResource);

	public Boolean isUserAlreadyProductTeamResource(Integer productId, Integer userId,	Date fromDate, Date toDate,ProductTeamResources productTeamResourcesFromDB);

	public ProductTeamResources getProductTeamResourceById(Integer productTeamResourceId);

	public void update(ProductTeamResources productTeamResourcesFromUI);
	
	public List<UserList> getProductTeamResourcesOfProduct(Integer productId, String plannedExecutionDate, int roleId);

	public void delete(ProductTeamResources productTeamResource);
	
	public List<ProductTeamResources> getProductTeamResourcesByRole(int productId, Integer roleId);
	
	public Boolean getTeamResourceByUserIdandProductIdandDate(Integer productId, Integer userId, Date aWpPsDate, Date aWpPeDate);

	List<JsonUserList> getProductTeamResourcesJsonListOfProduct(
			Integer productId, String plannedExecutionDate, int roleId);

	Map<Integer, Integer> getProductTeamResourcesCountByRole(
			int productId);
	
	public UserList getProductTeamResourcesByUserName(int productId, String userName,Date plannedExecutionStartDate, Date plannedExecutionEndDate);

	 Integer countAlladdAllProductTeamResources(Date startDate,Date endDate);

	 List<ProductTeamResources> listAllProductTeam(int startIndex, int pageSize, Date startDate,Date endDate);
	 
	 Boolean isExistsTeamResourceByUserIdandProductIdandUserId(Integer productId, Integer userId);
	 List<ProductMaster> getProductDetailsByTeamResourceUserId(Integer userId);
}
