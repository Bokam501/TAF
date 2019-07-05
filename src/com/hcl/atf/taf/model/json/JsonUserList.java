package com.hcl.atf.taf.model.json;



import java.util.Comparator;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.AuthenticationMode;
import com.hcl.atf.taf.model.AuthenticationType;
import com.hcl.atf.taf.model.CommonActiveStatusMaster;
import com.hcl.atf.taf.model.Languages;
import com.hcl.atf.taf.model.TestfactoryResourcePool;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.UserRoleMaster;
import com.hcl.atf.taf.model.UserSkills;
import com.hcl.atf.taf.model.UserTypeMasterNew;
import com.hcl.atf.taf.model.VendorMaster;
public class JsonUserList implements java.io.Serializable {
	private static final Log log = LogFactory
			.getLog(JsonUserList.class);
	
	private String imagePath = "/Profile/";
	@JsonProperty
	private String firstName;
	@JsonProperty
	private String lastName;
	@JsonProperty
	private String middleName;
	@JsonProperty
	private String loginId ;
	@JsonProperty
	private Integer userRoleId;
	@JsonProperty
	private Integer userTypeId;
	@JsonProperty
	private Integer userId;	
	@JsonProperty	
	private Integer status;	
	@JsonProperty
	private String userPassword;
	@JsonProperty
	private String userDisplayName;
	@JsonProperty	
	private String userTypeLabel;
	@JsonProperty	
	private String userRoleLabel;
	@JsonProperty	
	private String userRoleLabelHref;
	@JsonProperty
	private  Integer resourcePoolId;	
	@JsonProperty
	private  String resourcePoolName;	
	@JsonProperty
	private  Integer vendorId;	
	@JsonProperty
	private  String vendorName;
	@JsonProperty
	private  String emailId;
	@JsonProperty
	private  String contactNumber;
	@JsonProperty
	private  Long bookedHrs;
	@JsonProperty
	private  String timeSheetHours;
	@JsonProperty
	private  Integer reserve;
	@JsonProperty
	private  String reservationDetails;
	@JsonProperty
	private  Integer reservationPercentage;
	@JsonProperty
	private  Integer totalReservationPercentage;
	@JsonProperty
	private String userCode;
	@JsonProperty
	private StringBuffer skillName;
	@JsonProperty
	private  String imageURI;
	@JsonProperty
	private  String booked;
	@JsonProperty
	private  String available;
	@JsonProperty
	private  Integer languageId;	
	@JsonProperty
	private  String languageName;	
	@JsonProperty
	private  String productCoreResource;
	@JsonProperty
	private  String tfCoreResource;
	@JsonProperty
	private Integer customerId;
	@JsonProperty
	private String customerName;
	@JsonProperty
	private Integer authenticationTypeId;
	@JsonProperty
	private String authenticationTypeName;
	@JsonProperty
	private Integer authenticationModeId;
	@JsonProperty
	private String authenticationModeName;
	@JsonProperty
	private String modifiedField;
	@JsonProperty
	private String 	modifiedFieldTitle;	
	@JsonProperty
	private String oldFieldID;
	@JsonProperty
	private String	oldFieldValue;
	@JsonProperty
	private String	modifiedFieldID;
	@JsonProperty	
	private String modifiedFieldValue;
	
	public Integer getUserRoleId() {
		return userRoleId;
	}

	public void setUserRoleId(Integer userRoleId) {
		this.userRoleId = userRoleId;
	}

	public Integer getUserTypeId() {
		return userTypeId;
	}

	public void setUserTypeId(Integer userTypeId) {
		this.userTypeId = userTypeId;
	}

	public JsonUserList() {
	}

	public JsonUserList(UserList userList) {
		userId=userList.getUserId();
		
		if(userList.getUserRoleMaster()!=null){
			userRoleLabel = userList.getUserRoleMaster().getRoleLabel();   
			userRoleLabelHref=userList.getUserRoleMaster().getRoleLabel();
		}
		if(userList.getStatus() != null){
			this.status = userList.getStatus();
		}
		
		userList.getCommonActiveStatusMaster().getStatus();
		
		userPassword = userList.getUserPassword();
		userDisplayName = userList.getUserDisplayName();
		
		firstName = userList.getFirstName();
		lastName=userList.getLastName();
		middleName = userList.getMiddleName();
		loginId=userList.getLoginId();
				
		UserTypeMasterNew userTypeMasterNew= userList.getUserTypeMasterNew();
		if(userTypeMasterNew != null){
			this.userTypeId=userTypeMasterNew.getUserTypeId();
			this.userTypeLabel=userTypeMasterNew.getUserTypeLabel();
		}else{
			this.userTypeId=0;
			this.userTypeLabel=null;
		}

		if (userList.getUserRoleMaster() != null) {
			this.userRoleId = userList.getUserRoleMaster().getUserRoleId();
			this.userRoleLabel = userList.getUserRoleMaster().getRoleLabel();
					
		} else {
			this.userRoleId = 0;
			this.userRoleLabel = null;
		}
		TestfactoryResourcePool resourcePool = userList.getResourcePool();
		if(resourcePool != null){
			this.resourcePoolId=resourcePool.getResourcePoolId();
			this.resourcePoolName=resourcePool.getResourcePoolName();
		}else{
			this.resourcePoolId=0;
			this.resourcePoolName=null;
		}
		
		VendorMaster vendor = userList.getVendor();
		if(vendor != null){
			this.vendorId=vendor.getVendorId();
			this.vendorName=vendor.getRegisteredCompanyName();
		}else{
			this.vendorId=0;
			this.vendorName=null;
		}
		
		Set<UserSkills> userSkillsSet = userList.getUserSkills();
		StringBuffer skillarray = new StringBuffer();
		if(userSkillsSet != null && userSkillsSet.size()>0){
			skillarray = getSkillsOfUser(userSkillsSet);
		}
		
		if(skillarray.length() != 0){
			this.skillName = skillarray;
		}else{
			this.skillName = null;
		}
		
		this.emailId=userList.getEmailId();
		this.contactNumber=userList.getContactNumber();
		this.imageURI = userList.getImageURI();
		this.bookedHrs = new Long(40);
		this.timeSheetHours = new String("40");
		this.reserve=0;
		this.booked = new String("NB");
		this.available = new String("NA");
		this.productCoreResource = new String("");
		this.tfCoreResource = new String("");
		
		Languages languages = userList.getLanguages();
		if(languages != null){
			this.languageId=languages.getId();
			this.languageName=languages.getName();
		}else{
			this.languageId=0;
			this.languageName=null;
		}
		
			this.userCode = userList.getUserCode();
		
		AuthenticationType authenticationType = userList.getAuthenticationType();
		if(authenticationType != null){
			this.authenticationTypeId = authenticationType.getAuthenticationTypeId();
			this.authenticationTypeName = authenticationType.getAuthenticationTypeName();
		}else{
			this.authenticationTypeId=0;
			this.authenticationTypeName=null;
		}
		
		AuthenticationMode authenticationMode = userList.getAuthenticationMode();
		if(authenticationMode != null){
			this.authenticationModeId = authenticationMode.getAuthenticationModeId();
			this.authenticationModeName = authenticationMode.getAuthenticationModeName();
		}else{
			this.authenticationModeId=0;
			this.authenticationModeName=null;
		}
		
		if(userList.getCustomer() != null)
		{
			if(userList.getCustomer().iterator().hasNext()){
				this.customerId = userList.getCustomer().iterator().next().getCustomerId();				
			}
		}
		
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	public Integer getStatus() {
		return this.status;
	}

	
	public void setStatus(
			Integer status) {
		
			this.status = status;
			
	}


	public String getUserPassword() {
		return this.userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getUserDisplayName() {
		return this.userDisplayName;
	}

	public void setUserDisplayName(String userDisplayName) {
		this.userDisplayName = userDisplayName;
	}
	
	public String getUserTypeLabel() {
		return userTypeLabel;
	}

	public void setUserTypeLabel(String userTypeLabel) {
		this.userTypeLabel = userTypeLabel;
	}
	
	public String getUserRoleLabel() {
		return userRoleLabel;
	}

	public void setUserRoleLabel(String userRoleLabel) {
		this.userRoleLabel = userRoleLabel;
	}
	
	@JsonIgnore
	public UserList getUserList(){
		UserList userList = new UserList();
		userList.setUserId(userId);
		
		
		userList.setUserPassword(userPassword);
		userList.setUserDisplayName(userDisplayName);
		userList.setUserCode(userCode);
		UserTypeMasterNew userTypeMasterNew = new UserTypeMasterNew();
		userTypeMasterNew.setUserTypeId(userTypeId);
		userList.setUserTypeMasterNew(userTypeMasterNew);
			UserRoleMaster userRoleMaster = new UserRoleMaster();
			
			userRoleMaster.setRoleLabel(userRoleLabel);
			userList.setUserRoleMaster(userRoleMaster);
			
			userList.setUserRoleMaster(userRoleMaster);
			
		CommonActiveStatusMaster userStatusMaster = new CommonActiveStatusMaster();
		
		String userStatusTemp = "ACTIVE";	
	
		if(this.status != null ){			
			userList.setStatus(this.status);			
		}else{
			userList.setStatus(0);	
		}
	
		userStatusMaster.setStatus(userStatusTemp);
		userList.setCommonActiveStatusMaster(userStatusMaster);		
		
		userList.setFirstName(firstName);
		userList.setLastName(lastName);
		userList.setMiddleName(middleName);
		userList.setLoginId(loginId);
		if (emailId != null && emailId != ""){
			userList.setEmailId(emailId);
		}
		if (contactNumber != null && contactNumber != ""){
			userList.setContactNumber(contactNumber);
		}
		TestfactoryResourcePool resourcePool = new TestfactoryResourcePool();
		resourcePool.setResourcePoolId(resourcePoolId);
		Languages languages = new Languages();
		languages.setId(languageId);
		userList.setLanguages(languages);
		
		VendorMaster vendor = new VendorMaster();
		vendor.setVendorId(vendorId);
		
		AuthenticationType authenticationType = new AuthenticationType();
		authenticationType.setAuthenticationTypeId(authenticationTypeId);
		userList.setAuthenticationType(authenticationType);
		
		AuthenticationMode authenticationMode = new AuthenticationMode();
		authenticationMode.setAuthenticationModeId(authenticationModeId);
		
		if (imageURI != null && imageURI != ""){
			userList.setImageURI(imageURI);
		}
		
		return userList;
	}
	
	public Integer getResourcePoolId() {
		return resourcePoolId;
	}

	public void setResourcePoolId(Integer resourcePoolId) {
		this.resourcePoolId = resourcePoolId;
	}

	public String getResourcePoolName() {
		return resourcePoolName;
	}


	public Integer getLanguageId() {
		return languageId;
	}

	public void setLanguageId(Integer languageId) {
		this.languageId = languageId;
	}

	public String getLanguageName() {
		return languageName;
	}

	public void setLanguageName(String languageName) {
		this.languageName = languageName;
	}

	public StringBuffer getSkillName() {
		return skillName;
	}

	public void setSkillName(StringBuffer skillName) {
		this.skillName = skillName;
	}

	public void setResourcePoolName(String resourcePoolName) {
		this.resourcePoolName = resourcePoolName;
	}

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public static boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		}
		// only got here if we didn't return false
		return true;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	
	 public static Comparator<JsonUserList> jsonUserListComparator = new Comparator<JsonUserList>() {

	public int compare(JsonUserList user1, JsonUserList user2) {
	   String user1LoginId = user1.getLoginId().toUpperCase();
	   String user2LoginId = user2.getLoginId().toUpperCase();

	   return user1LoginId.compareTo(user2LoginId);

    }};
    
    @Override
    public String toString() {
        return "[ loginId=" + loginId + ", name=" + firstName + "]";
    }

	public Long getBookedHrs() {
		return bookedHrs;
	}

	public void setBookedHrs(Long bookedHrs) {
		this.bookedHrs = bookedHrs;
	}

	public String getTimeSheetHours() {
		return timeSheetHours;
	}

	public void setTimeSheetHours(String timeSheetHours) {
		this.timeSheetHours = timeSheetHours;
	}

	public Integer getReserve() {
		return reserve;
	}

	public void setReserve(Integer reserve) {
		this.reserve = reserve;
	}

	public String getReservationDetails() {
		return reservationDetails;
	}

	public void setReservationDetails(String reservationDetails) {
		this.reservationDetails = reservationDetails;
	}
	
	public StringBuffer getSkillsOfUser(Set<UserSkills> userSkillsSet){
		StringBuffer skillarray = null;
		if(userSkillsSet != null && userSkillsSet.size()>0){
			skillarray = new StringBuffer();
			for (UserSkills userSkillsets : userSkillsSet) {
				int user_id = userSkillsets.getUser().getUserId();
				if(user_id == this.userId){
					int isPrimary = userSkillsets.getSelfIsPrimary();
					if(isPrimary == 1){
						if(skillarray.length() == 0){
							skillarray.append(userSkillsets.getSkill().getDisplayName());
						}else if(skillarray.length() >0 && userSkillsets.getSkill() != null){
							skillarray.append(",");
							skillarray.append(userSkillsets.getSkill().getDisplayName());
						}
					}
				}
			}
		}
		return skillarray;
	}

	public String getImageURI() {
		return imageURI;
	}

	public void setImageURI(String imageURI) {
		this.imageURI = imageURI;
	}

	public String getBooked() {
		return booked;
	}

	public void setBooked(String booked) {
		this.booked = booked;
	}

	public String getAvailable() {
		return available;
	}

	public void setAvailable(String available) {
		this.available = available;
	}

	public String getProductCoreResource() {
		return productCoreResource;
	}

	public void setProductCoreResource(String productCoreResource) {
		this.productCoreResource = productCoreResource;
	}

	public String getTfCoreResource() {
		return tfCoreResource;
	}

	public void setTfCoreResource(String tfCoreResource) {
		this.tfCoreResource = tfCoreResource;
	}

	public String getUserRoleLabelHref() {
		return userRoleLabelHref;
	}

	public void setUserRoleLabelHref(String userRoleLabelHref) {
		this.userRoleLabelHref = userRoleLabelHref;
	}
	
	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Integer getAuthenticationTypeId() {
		return authenticationTypeId;
	}

	public void setAuthenticationTypeId(Integer authenticationTypeId) {
		this.authenticationTypeId = authenticationTypeId;
	}

	public String getAuthenticationTypeName() {
		return authenticationTypeName;
	}

	public void setAuthenticationTypeName(String authenticationTypeName) {
		this.authenticationTypeName = authenticationTypeName;
	}

	public Integer getAuthenticationModeId() {
		return authenticationModeId;
	}

	public void setAuthenticationModeId(Integer authenticationModeId) {
		this.authenticationModeId = authenticationModeId;
	}

	public String getAuthenticationModeName() {
		return authenticationModeName;
	}

	public void setAuthenticationModeName(String authenticationModeName) {
		this.authenticationModeName = authenticationModeName;
	}

	public String getModifiedField() {
		return modifiedField;
	}

	public void setModifiedField(String modifiedField) {
		this.modifiedField = modifiedField;
	}

	public String getModifiedFieldTitle() {
		return modifiedFieldTitle;
	}

	public void setModifiedFieldTitle(String modifiedFieldTitle) {
		this.modifiedFieldTitle = modifiedFieldTitle;
	}

	public String getOldFieldID() {
		return oldFieldID;
	}

	public void setOldFieldID(String oldFieldID) {
		this.oldFieldID = oldFieldID;
	}

	public String getOldFieldValue() {
		return oldFieldValue;
	}

	public void setOldFieldValue(String oldFieldValue) {
		this.oldFieldValue = oldFieldValue;
	}

	public String getModifiedFieldID() {
		return modifiedFieldID;
	}

	public void setModifiedFieldID(String modifiedFieldID) {
		this.modifiedFieldID = modifiedFieldID;
	}

	public String getModifiedFieldValue() {
		return modifiedFieldValue;
	}

	public void setModifiedFieldValue(String modifiedFieldValue) {
		this.modifiedFieldValue = modifiedFieldValue;
	}

	public Integer getReservationPercentage() {
		return reservationPercentage;
	}

	public void setReservationPercentage(Integer reservationPercentage) {
		this.reservationPercentage = reservationPercentage;
	}

	public Integer getTotalReservationPercentage() {
		return totalReservationPercentage;
	}

	public void setTotalReservationPercentage(Integer totalReservationPercentage) {
		this.totalReservationPercentage = totalReservationPercentage;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	
	
	
}
