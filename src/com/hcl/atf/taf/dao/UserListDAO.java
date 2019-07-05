package com.hcl.atf.taf.dao;

import java.util.Date;
import java.util.List;

import com.hcl.atf.taf.model.AuthenticationMode;
import com.hcl.atf.taf.model.AuthenticationType;
import com.hcl.atf.taf.model.EntityUserGroup;
import com.hcl.atf.taf.model.Skill;
import com.hcl.atf.taf.model.TestfactoryResourcePool;
import com.hcl.atf.taf.model.UserCustomerAccount;
import com.hcl.atf.taf.model.UserGroup;
import com.hcl.atf.taf.model.UserGroupMapping;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.UserResourcePoolMapping;
import com.hcl.atf.taf.model.UserRoleMaster;
import com.hcl.atf.taf.model.UserTypeMasterNew;
import com.hcl.atf.taf.model.dto.ResourcePoolSummaryDTO;

public interface UserListDAO  {	 
	int add (UserList userList);
	void update (UserList userList);
	void delete (UserList userList);
	List<UserList> list();
	UserList getByUserId(int userId);
	UserList getByUserName(String userName);
	List<UserList> list(int startIndex, int pageSize);  
	int getTotalRecords();
	boolean isUserExitsByName(UserList userList);
	UserList isUserListById(Integer userId);
	UserRoleMaster getRoleById(int roleId);
	List<UserRoleMaster> listRole();
	List<UserList> listUserByType(int userTypeId);
	List<UserList> listUserByTypeResourcePoolTestfactoryLabByStatus(int userTypeId, int resourcePoolId, int testFactoryLabId, int status, Integer jtStartIndex, Integer jtPageSize);	
	List<UserList> listUserByRoleResourcePool(int roleId, int resourcePoolId);	
	List<UserList> listTestFactoryManagerUserByResourcePool(List<TestfactoryResourcePool> resourcePoolList, int testFactoryId);	
	
	List<ResourcePoolSummaryDTO> listUserByRoleResourcePoolSummary(int roleId, int resourcePoolId);
	UserTypeMasterNew getUserTypeNewByLabel(String userTypeLabel);
	UserTypeMasterNew getUserTypeNewById(int userTypeId);	
	
	UserRoleMaster getRoleByLabel(String userRoleLabel);
	UserList getUserByLoginId(String loginId);
	List<UserList> getUserListByResourcePoolId(int resourcePoolId);
	List<UserList> getUserListByRoleFromResourcePoolId(int resourcePoolId);
	List<UserList> getUserListBasedRoleId(int resourcePoolId);
	List<UserList> getUserListByResourcePoolIdByRole(int resourcePoolId) ;
	public boolean isActiveUserExitsByLoginId(String userLoginId);
	UserRoleMaster listUserRoleByUserId(int userId);
	
	List<UserRoleMaster> getRolesByUser(Integer userid);
	List<UserList> getCustomerUserList(int resourcePoolId, Integer customerId);
	List<AuthenticationType> listAuthenticationTypes();
	AuthenticationType getAuthenticationTypeById(int authenticationTypeId);
	List<AuthenticationMode> listAuthenticationModes();
	AuthenticationMode getAuthenticationModeById(int authenticationModeId);
	UserCustomerAccount getCustomerUserByName(String customerUserName);
	boolean isCustomerUserExitsByLoginId(String userLoginId);
	List<UserList> getActivityUserListBasedRoleId(int resourcePoolId);
	
	public List<String> getProductManagerEmailList(Integer productId);		
	List<UserList> getActivityUserListBasedRoleIdAndProductId(int resourcePoolId,
			int productId);
	AuthenticationType getAuthenticationTypeByName(String authenticationTypeName);
	List<String> getUserLoginIds();
	Integer countAllUserList(Date startDate, Date endDate);
	List<UserList> listAllUsers(Integer startIndex, Integer pageSize, Date startDate, Date endDate);
	
	List<UserList> getAllUserListBasedRoleId(int resourcePoolId);
	List<UserList> getUserListByRoleFromResourcePoolIdByRoleAndSkill(Integer resourcePoolId, Integer roleId, Integer skillId, Integer userTypeId, Date weekDate, Skill skill);
	List<UserList> getUserListByLabIdOrResourcePoolId(Integer labId, Integer resourcePoolId);
	List<UserResourcePoolMapping> listUserResourcePoolMapping(int userId,int resourcePoolId, Integer jtStartIndex, Integer jtPageSize);
	void addResourcepoolmapping(UserResourcePoolMapping userResourcePoolMapping);
	void updateResourceMapping(UserResourcePoolMapping userResourcePoolMappingFromUI);
	UserResourcePoolMapping getUserResourcePoolMappingById(int UserResourcePoolMapping);
	void deleteResourcepoolMapping(UserResourcePoolMapping deleteResourcepoolMapping);
	boolean getUserResourcePoolMappingByFilter(Integer userId, Integer rpId,Date fromDate, Date toDate, UserResourcePoolMapping umrp);
	List<UserList> getUserListMappedToResourcePool(Integer loopResourcePoolId, Date startDate, Date endDate);
	List<UserResourcePoolMapping> getUserResourcePoolMappingByFilter(Integer userId, Integer rpId,Date fromDate, Date toDate);
	List<UserList> getUserListBasedRoleAndProductId(int productId,String roleName);
	List<UserList> listUserByTypeResourcePoolMapped(int userTypeId,int resourcePoolId, int testFactoryLabId, int status,Integer jtStartIndex, Integer jtPageSize);
	List<UserGroup> getUserGroups(Integer testFactoryId, Integer productId, Boolean isConsolidated);
	Boolean isUserGroupAvailable(String userGroupName, Integer referenceUserGroupId, Integer testFactoryId,	Integer productId);
	void addUserGroup(UserGroup userGroup);
	void updateUserGroup(UserGroup userGroup);
	Integer getUsersAvailbleToMapWithGroupCount(Integer productId, Integer userGroupId);
	List<Object[]> getUsersAvailbleToMapWithGroup(Integer productId, Integer userGroupId);
	List<Object[]> getUsersMappedWithGroup(Integer productId, Integer userGroupId);
	void mapOrUnmapUserWithUserGroup(UserGroupMapping userGroupMapping, String maporunmap);
	List<UserList> getListOfEngManagerByResourcePoolId(Integer resourcePoolId);
	List<UserResourcePoolMapping> getUserResourcePoolMappingByUserIdAndResourcePoolId(Integer userId,Integer rpId,Date fromDate,Date toDate);
	void mapOrUnmapEntityUserGroup(EntityUserGroup entityUserGroup,String mapOrUnmap);
	EntityUserGroup getEntityUserGroupByEntityAndEntityInstanceId(Integer entityId, Integer instanceId, Integer userId);
	List<EntityUserGroup> getEntityUserGroupByEntityId(Integer entityId, Integer instanceId);
	List<Object[]> getEntityUserGroupReadyForMapping(Integer productId,Integer entityTypeId, Integer entityInstanceId);
	EntityUserGroup getEntityUserGroupByEntityTypeIdAndUserId(Integer entityTypeId, Integer activityWorkPackageId,Integer userId);
	void deleteEntityAndUserMappingByProductIdandUserId(Integer productId,Integer userId);
	

}
