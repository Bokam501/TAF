package com.hcl.atf.taf.service;

import java.util.Date;
import java.util.List;

import com.hcl.atf.taf.model.AuthenticationMode;
import com.hcl.atf.taf.model.AuthenticationType;
import com.hcl.atf.taf.model.EntityUserGroup;
import com.hcl.atf.taf.model.Languages;
import com.hcl.atf.taf.model.ProductUserRole;
import com.hcl.atf.taf.model.TestfactoryResourcePool;
import com.hcl.atf.taf.model.UserGroup;
import com.hcl.atf.taf.model.UserGroupMapping;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.UserResourcePoolMapping;
import com.hcl.atf.taf.model.UserRoleMaster;
import com.hcl.atf.taf.model.UserTypeMasterNew;

public interface UserListService {
	int add (UserList userList);
	void update (UserList userList);
	void delete (int userId);
	List<UserList> list(); 
	List<UserList> list(int startIndex, int pageSize);  
	int getTotalRecords();
	UserList getUserListByUserName(String userName);
	boolean isUserExistingByName(UserList userList);
	UserList getUserListById(Integer userId);
	UserRoleMaster getRoleById(int roleId);
	UserRoleMaster listUserRoleByUserId(int userId);
	
	List<UserRoleMaster> listRole();
	List<UserList> listUserByType(int userTypeId);
	
	List<UserList> listUserByTypeResourcePoolTestfactoryLabByStatus(int userTypeId, int resourcePoolId, int testFactoryLabId, int status, Integer jtStartIndex, Integer jtPageSize);
	List<UserList> listUserByRoleResourcePool(int roleId, int resourcePoolId);
	UserRoleMaster getRoleByLabel(String userRoleLabel);
	UserTypeMasterNew getUserTypeNewByLabel(String userTypeLabel);
	UserTypeMasterNew getUserTypeNewById(int userTypeId);
	ProductUserRole getProductUserRoleById(int productUserRoleId);
	void updateProductUserRole(ProductUserRole productUserRole);
	UserList getUserByLoginId(String loginId);
	List<UserList> listTestFactoryManagers(int testFactoryId, List<TestfactoryResourcePool> resourcePoolList);
	List<UserList> getUserListByResourcePoolId(int resourcePoolId);
	List<UserList> getUserListBasedRoleId(int resourcePoolId);
	UserList profileUpdateInline(String modifiedValue,String modifiedField, Integer userId);
	
	List<Languages> listLanguages(int status);
	Languages getLanguageForId(int languageId);
	
	void updateUsers(List<UserList> modifiedUserLists);
	boolean isActiveUserExitsByLoginId(String userLoginId);
	
	List<UserRoleMaster> getRolesByUser(Integer userid);
	List<UserList> getCustomerUserList(int resourcePoolId, Integer customerId);
	
	List<AuthenticationType> listAuthenticationTypes();
	AuthenticationType getAuthenticationTypeById(int authenticationTypeId);
	
	List<AuthenticationMode> listAuthenticationModes();
	AuthenticationMode getAuthenticationModeById(int authenticationModeId);
	boolean isCustomerUserExitsByLoginId(String userLoginId);
	List<UserList> getActivityUserListBasedRoleId(int i);	
	List<UserList> getActivityUserListBasedRoleIdAndProductId(int resourcePoolId, int productId);
	UserList getProductTeamResourcesByUserName(
			int productId, String userName,Date plannedExecutionStartDate, Date plannedExecutionEndDate);
	
	Languages getLanguageByName(String languageName);
	AuthenticationType getAuthenticationTypeByName(String stringCellValue);
	List<String> getUserLoginIds();
	Integer countAllUserList(Date startDate, Date endDate);
	List<UserList> listAllUsers(int startIndex, int pageSize, Date startDate,Date endDate);
	
	List<UserList> getAllUserListBasedRoleId(int i);
	List<UserResourcePoolMapping> listUserResourcePoolMapping(int userId,int resourcePoolId, Integer jtStartIndex, Integer jtPageSize);
	void addResourcepoolmapping(UserResourcePoolMapping userResourcePoolMapping);
	void updateResourceMapping(UserResourcePoolMapping userResourcePoolMappingFromUI);
	UserResourcePoolMapping getUserResourcePoolMappingById(int resourcepoolmappingId);
	void deleteResourcepoolMapping(UserResourcePoolMapping userResourcePoolMapping);
	boolean getUserResourcePoolMappingByFilter(Integer userId, Integer rpId,Date fromDate, Date toDate, UserResourcePoolMapping userResourcePoolMappingFromDB);
	
	List<UserResourcePoolMapping> getUserResourcePoolMappingByFilter(Integer userId, Integer rpId,Date fromDate, Date toDate);
	List<UserList> listUserByTypeResourcePoolMapped(int userTypeId,int resourcePoolId, int testFactoryLabId, int statusID,Integer jtStartIndex, Integer jtPageSize);
	List<UserGroup> getUserGroups(Integer testFactoryId, Integer productId, Boolean isConsolidated);
	Boolean isUserGroupAvailable(String userGroupName, Integer referenceUserGroupId, Integer testFactoryId, Integer productId);
	void addUserGroup(UserGroup userGroup);
	void updateUserGroup(UserGroup userGroup);
	List<Object[]> getUsersAvailbleToMapWithGroup(Integer productId, Integer userGroupId);
	List<Object[]> getUsersMappedWithGroup(Integer productId, Integer userGroupId);
	void mapOrUnmapUserWithUserGroup(UserGroupMapping userGroupMapping, String maporunmap);
	Integer getUsersAvailbleToMapWithGroupCount(Integer productId, Integer userGroupId);
	List<UserList> getListOfEngManagerByResourcePoolId(Integer resourcePoolId);
	List<UserResourcePoolMapping> getUserResourcePoolMappingByUserIdAndResourcePoolId(Integer userId,Integer rpId,Date fromDate,Date toDate);
	void mapOrUnmapEntityUserGroup(EntityUserGroup entityUserGroup,String mapOrUnmap);
	EntityUserGroup getEntityUserGroupByEntityAndEntityInstanceId(Integer entityId, Integer instanceId, Integer userId);
	List<EntityUserGroup> getEntityUserGroupByEntityId(Integer entityId, Integer instanceId);
	List<Object[]> getEntityUserGroupReadyForMapping(Integer productId,Integer entityTypeId, Integer entityInstanceId);
	void deleteEntityAndUserMappingByProductIdandUserId(Integer productId,Integer userId);
}

