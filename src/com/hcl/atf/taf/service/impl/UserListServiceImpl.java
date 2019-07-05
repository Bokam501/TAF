package com.hcl.atf.taf.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.LanguagesDAO;
import com.hcl.atf.taf.dao.ProductTeamResourcesDao;
import com.hcl.atf.taf.dao.ProductUserRoleDAO;
import com.hcl.atf.taf.dao.UserListDAO;
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
import com.hcl.atf.taf.service.UserListService;

@Service
public class UserListServiceImpl implements UserListService {
	
	@Autowired
    private UserListDAO userListDAO;
	
	@Autowired
    private LanguagesDAO languagesDAO;
	
	@Autowired
    private ProductUserRoleDAO productUserRoleDAO;
	
	@Autowired
	private ProductTeamResourcesDao productTeamResourceDAO;
	
	
	@Override
	@Transactional
	public int add(UserList userList) {
		return userListDAO.add(userList);
	}
	    
	
	
	@Override
	@Transactional
	public List<UserList> list() {	 
		return userListDAO.list();
	}

	@Override
	@Transactional
	public void delete(int userId) {
		userListDAO.delete(userListDAO.getByUserId(userId));			
	}

	@Override
	@Transactional
	public void update(UserList userList) {
		userListDAO.update(userList);	
		
	}




	@Override
	@Transactional
	public List<UserList> list(int startIndex, int pageSize) {
		
		return userListDAO.list(startIndex,pageSize);
	}

	@Override
	@Transactional
	public int getTotalRecords() {
		
		return userListDAO.getTotalRecords();
	}

	@Override
	@Transactional
	public UserList getUserListByUserName(String userName) {
		
		return userListDAO.getByUserName(userName);
	}

	@Override
	@Transactional
	public boolean isUserExistingByName(UserList userList) {
		return userListDAO.isUserExitsByName(userList);
	}

	@Override
	@Transactional
	public UserList getUserListById(Integer userId) {
		return userListDAO.isUserListById(userId);
	}

	@Override
	@Transactional
	public UserRoleMaster getRoleById(int roleId) {
		return userListDAO.getRoleById(roleId);
	}
	
	@Override
	@Transactional
	public UserRoleMaster listUserRoleByUserId(int userId) {
		return userListDAO.listUserRoleByUserId(userId);
	}

	@Override
	@Transactional
	public List<UserRoleMaster> listRole() {
		return userListDAO.listRole();
		
	}
	
	@Override
	@Transactional
	public List<UserList> listUserByType(int userTypeId) {
		return userListDAO.listUserByType(userTypeId);
	}

	@Override
	@Transactional
	public List<UserList> listUserByTypeResourcePoolTestfactoryLabByStatus(int userTypeId, int resourcePoolId, int testFactoryLabId, int status, Integer jtStartIndex, Integer jtPageSize) {		
		return userListDAO.listUserByTypeResourcePoolTestfactoryLabByStatus(userTypeId, resourcePoolId, testFactoryLabId, status, jtStartIndex, jtPageSize);
	}
	
	@Override
	@Transactional
	public List<UserList> listUserByTypeResourcePoolMapped(int userTypeId, int resourcePoolId, int testFactoryLabId, int status, Integer jtStartIndex, Integer jtPageSize) {		
		return userListDAO.listUserByTypeResourcePoolMapped(userTypeId, resourcePoolId, testFactoryLabId, status, jtStartIndex, jtPageSize);
	}
	
	@Override
	@Transactional
	public List<UserList> listUserByRoleResourcePool(int roleId, int resourcePoolId) {
		return userListDAO.listUserByRoleResourcePool(roleId, resourcePoolId);
	}
	
	
	@Override
	@Transactional
	public UserRoleMaster getRoleByLabel(String userRoleLabel) {
		return  userListDAO.getRoleByLabel(userRoleLabel);
	}

	@Override
	@Transactional
	public UserTypeMasterNew getUserTypeNewByLabel(String userTypeLabel) {
		return userListDAO.getUserTypeNewByLabel(userTypeLabel);
	}

	@Override
	@Transactional
	public UserTypeMasterNew getUserTypeNewById(int userTypeId) {
		return userListDAO.getUserTypeNewById(userTypeId);
	}

	@Override
	public ProductUserRole getProductUserRoleById(int productUserRoleId) {
		return productUserRoleDAO.getByproductUserRoleId(productUserRoleId);
	}

	@Override
	public void updateProductUserRole(ProductUserRole productUserRole) {
		productUserRoleDAO.update(productUserRole);		
	}



	@Override
	@Transactional
	public UserList getUserByLoginId(String loginId) {
		return userListDAO.getUserByLoginId(loginId);
	}



	@Override
	public List<UserList> listTestFactoryManagers(int testFactoryId, List<TestfactoryResourcePool> resourcePoolList) {
		return userListDAO.listTestFactoryManagerUserByResourcePool(resourcePoolList, testFactoryId);
	}



	@Override
	public List<UserList> getUserListByResourcePoolId(int resourcePoolId) {
		return userListDAO.getUserListByResourcePoolId(resourcePoolId);
	}
	
	@Override
	public List<UserList> getUserListBasedRoleId(int resourcePoolId) {
		return userListDAO.getUserListBasedRoleId(resourcePoolId);
	}

	@Override
	@Transactional
	public UserList profileUpdateInline(String modifiedValue, String modifiedField,
			Integer userId) {
				UserList userList = userListDAO.getByUserId(userId);
				
				
				
				if (modifiedField.equalsIgnoreCase("firstName")) {
					userList.setFirstName(modifiedValue);
				} else if (modifiedField.equalsIgnoreCase("middleName")) {
					userList.setMiddleName(modifiedValue);
				} else if (modifiedField.equalsIgnoreCase("lastName")) {
					userList.setLastName(modifiedValue);
				} else if (modifiedField.equalsIgnoreCase("userDisplayName")) {
					userList.setUserDisplayName(modifiedValue);
				} else if (modifiedField.equalsIgnoreCase("emailId")) {
					userList.setEmailId(modifiedValue);
				} else if (modifiedField.equalsIgnoreCase("contactNo")) {
					userList.setContactNumber(modifiedValue);
				}else if(modifiedField.equalsIgnoreCase("imageURI")){
					userList.setImageURI(modifiedValue);
				}
				
				userListDAO.update(userList);
				return userList;
	}

	@Override
	@Transactional
	public List<Languages> listLanguages(int status) {
		return languagesDAO.listLanguages(status);
	}

	@Override
	@Transactional
	public Languages getLanguageForId(int languageId) {
		return languagesDAO.getLanguageForId(languageId);
	}
	
	@Override
	@Transactional
	public Languages getLanguageByName(String languageName) {
		return languagesDAO.getLanguageByName(languageName);
	}
	
	@Override
	@Transactional
	public void updateUsers(List<UserList> modifiedUserLists) {
		UserResourcePoolMapping userResourcePoolMapping=null;
		for(UserList user : modifiedUserLists){
			
			userListDAO.update(user);
			userResourcePoolMapping = new UserResourcePoolMapping();
			userResourcePoolMapping.setUserId(user);
			userResourcePoolMapping.setFromDate(user.getResourceFromDate());
			userResourcePoolMapping.setToDate(user.getResourceToDate());
			userResourcePoolMapping.setResourcepoolId(user.getResourcePool());
			userResourcePoolMapping.setUserRoleId(user.getUserRoleMaster());
			userResourcePoolMapping.setMappedDate(new Date());
			
			boolean value = userListDAO.getUserResourcePoolMappingByFilter(userResourcePoolMapping.getUserId().getUserId(),userResourcePoolMapping.getResourcepoolId().getResourcePoolId(),userResourcePoolMapping.getFromDate(),userResourcePoolMapping.getToDate(),null);
			if(!value){
				userListDAO.addResourcepoolmapping(userResourcePoolMapping);
			}
			
		}
	}

	@Override
	@Transactional
	public boolean isActiveUserExitsByLoginId(String userLoginId) {
		return userListDAO.isActiveUserExitsByLoginId(userLoginId);
	}



	@Override
	public List<UserRoleMaster> getRolesByUser(Integer userid) {
		return userListDAO.getRolesByUser(userid);
	}



	@Override
	public List<UserList> getCustomerUserList(int resourcePoolId,Integer customerId) {
		return userListDAO.getCustomerUserList(resourcePoolId,customerId);
	}
	@Override
	@Transactional
	public List<AuthenticationType> listAuthenticationTypes() {
		return userListDAO.listAuthenticationTypes();
	}



	@Override
	@Transactional
	public AuthenticationType getAuthenticationTypeById(int authenticationTypeId) {
		return userListDAO.getAuthenticationTypeById(authenticationTypeId);
	}



	@Override
	@Transactional
	public List<AuthenticationMode> listAuthenticationModes() {
		return userListDAO.listAuthenticationModes();
	}


	@Override
	@Transactional
	public AuthenticationMode getAuthenticationModeById(int authenticationModeId) {
		return userListDAO.getAuthenticationModeById(authenticationModeId);
	}



	@Override
	@Transactional
	public boolean isCustomerUserExitsByLoginId(String userLoginId) {
			return userListDAO.isCustomerUserExitsByLoginId(userLoginId);
	}

	@Override
	@Transactional
	public List<UserList> getActivityUserListBasedRoleId(int resourcePoolId) {
		return userListDAO.getActivityUserListBasedRoleId(resourcePoolId);
	}

	

	@Override
	@Transactional
	public List<UserList> getActivityUserListBasedRoleIdAndProductId(int resourcePoolId,int productId) {
		return userListDAO.getActivityUserListBasedRoleIdAndProductId(resourcePoolId,productId);
	}



	@Override
	@Transactional
	public UserList getProductTeamResourcesByUserName(int productId,
			String userName,Date plannedExecutionStartDate, Date plannedExecutionEndDate) {
		return productTeamResourceDAO.getProductTeamResourcesByUserName(productId,userName,plannedExecutionStartDate,plannedExecutionEndDate);
	}



	@Override
	@Transactional
	public AuthenticationType getAuthenticationTypeByName(String authenticationTypeName) {
		return userListDAO.getAuthenticationTypeByName(authenticationTypeName);
	}



	@Override
	@Transactional
	public List<String> getUserLoginIds() {
		return userListDAO.getUserLoginIds();
	}



	@Override
	@Transactional
	public Integer countAllUserList(Date startDate, Date endDate) {
		return userListDAO.countAllUserList(startDate,endDate);
	}



	@Override
	@Transactional
	public List<UserList> listAllUsers(int startIndex , int pageSize, Date startDate,Date endDate) {
		return userListDAO.listAllUsers(startIndex,pageSize,startDate,endDate);
		
	}
	
	@Override
	@Transactional
	public List<UserList> getAllUserListBasedRoleId(int resourcePoolId) {
		return userListDAO.getAllUserListBasedRoleId(resourcePoolId);
	}



	@Override
	@Transactional
	public List<UserResourcePoolMapping> listUserResourcePoolMapping(int userId, int resourcePoolId, Integer jtStartIndex,Integer jtPageSize) {
		
		return userListDAO.listUserResourcePoolMapping(userId,resourcePoolId,jtStartIndex,jtPageSize);
	}



	@Override
	@Transactional
	public void addResourcepoolmapping(UserResourcePoolMapping userResourcePoolMapping) {
		userListDAO.addResourcepoolmapping(userResourcePoolMapping);
		
	}



	@Override
	@Transactional
	public void updateResourceMapping(UserResourcePoolMapping userResourcePoolMappingFromUI) {
		userListDAO.updateResourceMapping(userResourcePoolMappingFromUI);
		
	}



	@Override
	@Transactional
	public UserResourcePoolMapping getUserResourcePoolMappingById(int resourcepoolmappingId) {
		return userListDAO.getUserResourcePoolMappingById(resourcepoolmappingId);
	}



	@Override
	@Transactional
	public void deleteResourcepoolMapping(UserResourcePoolMapping deleteResourcepoolMapping) {
		userListDAO.deleteResourcepoolMapping(deleteResourcepoolMapping);
		
	}



	@Override
	@Transactional
	public boolean getUserResourcePoolMappingByFilter(Integer userId,Integer rpId, Date fromDate, Date toDate,UserResourcePoolMapping umrp) {
		
		return userListDAO.getUserResourcePoolMappingByFilter(userId,rpId,fromDate,toDate,umrp);
	}



	@Override
	@Transactional
	public List<UserResourcePoolMapping> getUserResourcePoolMappingByFilter(Integer userId, Integer rpId,Date fromDate, Date toDate) {
		return userListDAO.getUserResourcePoolMappingByFilter(userId,rpId,fromDate,toDate);
	}



	@Override
	@Transactional
	public List<UserGroup> getUserGroups(Integer testFactoryId, Integer productId, Boolean isConsolidated) {
		return userListDAO.getUserGroups(testFactoryId, productId, isConsolidated);
	}
	
	@Override
	@Transactional
	public Boolean isUserGroupAvailable(String userGroupName, Integer referenceUserGroupId, Integer testFactoryId,	Integer productId) {
		return userListDAO.isUserGroupAvailable(userGroupName, referenceUserGroupId, testFactoryId, productId);
	}



	@Override
	@Transactional
	public void addUserGroup(UserGroup userGroup) {
		userListDAO.addUserGroup(userGroup);
	}
	
	@Override
	@Transactional
	public void updateUserGroup(UserGroup userGroup) {
		userListDAO.updateUserGroup(userGroup);
	}



	@Override
	@Transactional
	public List<Object[]> getUsersAvailbleToMapWithGroup(Integer productId, Integer userGroupId) {
		return userListDAO.getUsersAvailbleToMapWithGroup(productId, userGroupId);
	}
	
	@Override
	@Transactional
	public List<Object[]> getUsersMappedWithGroup(Integer productId, Integer userGroupId) {
		return userListDAO.getUsersMappedWithGroup(productId, userGroupId);
	}



	@Override
	@Transactional
	public void mapOrUnmapUserWithUserGroup(UserGroupMapping userGroupMapping, String maporunmap) {
		userListDAO.mapOrUnmapUserWithUserGroup(userGroupMapping, maporunmap);
	}



	@Override
	@Transactional
	public Integer getUsersAvailbleToMapWithGroupCount(Integer productId, Integer userGroupId) {
		return userListDAO.getUsersAvailbleToMapWithGroupCount(productId, userGroupId);
	}



	@Override
	@Transactional
	public List<UserList> getListOfEngManagerByResourcePoolId(Integer resourcePoolId) {
		return userListDAO.getListOfEngManagerByResourcePoolId(resourcePoolId);
	}



	@Override
	@Transactional
	public List<UserResourcePoolMapping> getUserResourcePoolMappingByUserIdAndResourcePoolId(Integer userId,Integer rpId,Date fromDate,Date toDate) {
		return userListDAO.getUserResourcePoolMappingByUserIdAndResourcePoolId(userId,rpId,fromDate,toDate);
	}






	@Override
	@Transactional
	public void mapOrUnmapEntityUserGroup(EntityUserGroup entityUserGroup,String mapOrUnmap) {
		userListDAO.mapOrUnmapEntityUserGroup(entityUserGroup,mapOrUnmap);
		
	}



	@Override
	@Transactional
	public EntityUserGroup getEntityUserGroupByEntityAndEntityInstanceId(Integer entityId, Integer instanceId, Integer userId) {
		return userListDAO.getEntityUserGroupByEntityAndEntityInstanceId(entityId,instanceId,userId);
	}



	@Override
	@Transactional
	public List<EntityUserGroup> getEntityUserGroupByEntityId(Integer entityId,Integer instanceId) {
		return userListDAO.getEntityUserGroupByEntityId(entityId,instanceId);
	}



	@Override
	@Transactional
	public List<Object[]> getEntityUserGroupReadyForMapping(Integer productId,Integer entityTypeId, Integer entityInstanceId) {
		return userListDAO.getEntityUserGroupReadyForMapping(productId,entityTypeId,entityInstanceId);
	}



	@Override
	@Transactional
	public void deleteEntityAndUserMappingByProductIdandUserId(Integer productId, Integer userId) {
		userListDAO.deleteEntityAndUserMappingByProductIdandUserId(productId,userId);
		
	}
}
