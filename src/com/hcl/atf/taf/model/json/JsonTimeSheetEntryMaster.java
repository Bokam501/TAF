package com.hcl.atf.taf.model.json;



import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.ResourceShiftCheckIn;
import com.hcl.atf.taf.model.TimeSheetEntryMaster;
import com.hcl.atf.taf.model.WorkPackage;


public class JsonTimeSheetEntryMaster implements java.io.Serializable{
	
	private static final Log log = LogFactory.getLog(JsonTimeSheetEntryMaster.class);
	
	@JsonProperty	
	private Integer timeSheetEntryId;
	@JsonProperty
	private String timeEntryDate;
	@JsonProperty
	private Integer hours;
	@JsonProperty
	private Integer mins;
	@JsonProperty
	private Integer shiftId;
	@JsonProperty	
	private String shiftName;
	@JsonProperty
	private Integer workPackageId;
	@JsonProperty	
	private String workPackageName;
	@JsonProperty
	private Integer productId;
	@JsonProperty	
	private String productName;
	@JsonProperty
	private String comments;
	@JsonProperty
	private Integer userId;
	@JsonProperty	
	private String userName;
	@JsonProperty
	private Integer roleId;
	@JsonProperty	
	private String roleName;
	@JsonProperty
	private Integer isApproved;
	@JsonProperty
	private String approvalComments;
	@JsonProperty
	private String approvedDate;
	@JsonProperty
	private Integer approverId;
	@JsonProperty	
	private String approverName;
	@JsonProperty
	private Integer status;
	@JsonProperty
	private String createdDate;
	@JsonProperty
	private String modifiedDate;
	@JsonProperty
	private Integer activityTypeId;
	@JsonProperty	
	private String activityTypeName;
	@JsonProperty
	private Integer resourceShiftCheckInId;
	
	@JsonProperty
	private Integer shiftTypeId;
	@JsonProperty
	private String shiftTypeName;
	public JsonTimeSheetEntryMaster(){
		
	}
	
	public JsonTimeSheetEntryMaster(TimeSheetEntryMaster timeSheetEntryMaster){
		this.timeSheetEntryId = timeSheetEntryMaster.getTimeSheetEntryId();
		this.hours = timeSheetEntryMaster.getHours();
		this.mins = timeSheetEntryMaster.getMins();
		this.comments = timeSheetEntryMaster.getComments();
		this.status = timeSheetEntryMaster.getStatus();
		this.isApproved = timeSheetEntryMaster.getIsApproved();
		this.approvalComments = timeSheetEntryMaster.getApprovalComments();

		if(timeSheetEntryMaster.getDate() != null){
			this.timeEntryDate = DateUtility.sdfDateformatWithOutTime(timeSheetEntryMaster.getDate());
		}
		 	
		if(timeSheetEntryMaster.getProduct() !=null){
			this.productId = timeSheetEntryMaster.getProduct().getProductId();
			log.info("-------------------timeSheetEntryMaster.getProduct().getProductId()----"+productId);
			this.productName = timeSheetEntryMaster.getProduct().getProductName();
			
		}
		
		if(timeSheetEntryMaster.getUser() !=  null){
			this.userId = timeSheetEntryMaster.getUser().getUserId();
			this.userName = timeSheetEntryMaster.getUser().getUserDisplayName();
		}
		
		if(timeSheetEntryMaster.getRole() !=  null){
			this.roleId = timeSheetEntryMaster.getRole().getUserRoleId();
			this.roleName = timeSheetEntryMaster.getRole().getRoleName();
		}
		
		WorkPackage workpackage = timeSheetEntryMaster.getWorkPackage();
		if(workpackage != null){
			this.workPackageId = timeSheetEntryMaster.getWorkPackage().getWorkPackageId();
			this.workPackageName = timeSheetEntryMaster.getWorkPackage().getName();
		}else{
			this.workPackageId = 0;
			this.workPackageName = null;
		}
		
		if(timeSheetEntryMaster.getApprover() != null){
			this.approverId = timeSheetEntryMaster.getApprover().getUserId();
			this.approverName = timeSheetEntryMaster.getApprover().getUserDisplayName();
		}
		
		if(timeSheetEntryMaster.getShift() != null){
			this.shiftId = timeSheetEntryMaster.getShift().getShiftId();
			this.shiftName = timeSheetEntryMaster.getShift().getShiftName();
		}
		
		if(timeSheetEntryMaster.getApprovedDate() != null){
			this.approvedDate = DateUtility.sdfDateformatWithOutTime(timeSheetEntryMaster.getApprovedDate());
		}
		
		if(timeSheetEntryMaster.getCreatedDate()!=null){
			this.createdDate=DateUtility.sdfDateformatWithOutTime(timeSheetEntryMaster.getCreatedDate());
		}
		if(timeSheetEntryMaster.getModifiedDate()!=null){
			this.modifiedDate=DateUtility.sdfDateformatWithOutTime(timeSheetEntryMaster.getModifiedDate());
		}
		if(timeSheetEntryMaster.getActivityType() != null){
			this.activityTypeId = timeSheetEntryMaster.getActivityType().getActivityTypeId();
			this.activityTypeName = timeSheetEntryMaster.getActivityType().getActivityName();
		}
		this.approvalComments = timeSheetEntryMaster.getApprovalComments();
		
		if(timeSheetEntryMaster.getResourceShiftCheckIn() != null){
			this.resourceShiftCheckInId = timeSheetEntryMaster.getResourceShiftCheckIn().getResourceShiftCheckInId();
			if(timeSheetEntryMaster.getResourceShiftCheckIn().getProductMaster() != null){
				if(this.productId == null){
					this.productId = timeSheetEntryMaster.getResourceShiftCheckIn().getProductMaster().getProductId();					
					log.info("-------------------Resource Check in Product id "+timeSheetEntryMaster.getResourceShiftCheckIn().getProductMaster().getProductId());				
				}	
				if(this.shiftId == null){
					this.shiftId = timeSheetEntryMaster.getResourceShiftCheckIn().getActualShift().getShift().getShiftId();
				}
			}			
		}else{
			this.resourceShiftCheckInId = 0;
			
		}
	
		log.info("resourceShiftCheckInId--"+resourceShiftCheckInId);
		
	}
	
	
	@JsonIgnore
	public TimeSheetEntryMaster getTimeSheetEntryMaster() {
		
		String convertedTimeEntryDate="";
		try {
			String temp[]=	timeEntryDate.split("-");
			int yearLength=temp[2].length();
			if(yearLength==4){
		for(int i=temp.length-1;i>=0;i--){
			
			if(i!=0){
				convertedTimeEntryDate=convertedTimeEntryDate+temp[i]+"-";
			}else{
				convertedTimeEntryDate=convertedTimeEntryDate+temp[i];
			}
		}
			}
			else{
				convertedTimeEntryDate=timeEntryDate;
			}
	//	
		}catch(Exception e){
			
		}
		TimeSheetEntryMaster timeSheetEntryMaster = new TimeSheetEntryMaster();
		timeSheetEntryMaster.setHours(this.hours);
		timeSheetEntryMaster.setMins(this.mins);
		timeSheetEntryMaster.setComments(this.comments);
		timeSheetEntryMaster.setStatus(this.status);
		timeSheetEntryMaster.setIsApproved(this.isApproved);
		timeSheetEntryMaster.setApprovalComments(this.approvalComments);
		if (timeSheetEntryId != null) {
			timeSheetEntryMaster.setTimeSheetEntryId(timeSheetEntryId);
		}
		
		log.info("Befor modification Date : "+this.timeEntryDate);
		
		if(convertedTimeEntryDate != null && !(convertedTimeEntryDate.trim().isEmpty())) {
			timeSheetEntryMaster.setDate(DateUtility.dateFormatWithOutSeconds(convertedTimeEntryDate));
		}else{
			timeSheetEntryMaster.setDate(DateUtility.getCurrentTime());
		}
		
		
		if(this.createdDate == null || this.createdDate.trim().isEmpty()) {
			timeSheetEntryMaster.setCreatedDate(DateUtility.getCurrentTime());
		}else {
		
			timeSheetEntryMaster.setCreatedDate(DateUtility.dateFormatWithOutSeconds(this.createdDate));
		}
			timeSheetEntryMaster.setModifiedDate(DateUtility.getCurrentTime());
		

		if(this.approvedDate == null || this.approvedDate.trim().isEmpty()) {
			timeSheetEntryMaster.setApprovedDate(null);
			
		}
		else {
			timeSheetEntryMaster.setApprovedDate(DateUtility.dateFormatWithOutSecondsddMMyyyy(this.approvedDate));
		}
		
		timeSheetEntryMaster.setStatus(status);
			
		
		if(timeSheetEntryMaster.getUser()!=null){
			this.userId = timeSheetEntryMaster.getUser().getUserId();
			this.userName =timeSheetEntryMaster.getUser().getUserDisplayName();
		}
		
		if(timeSheetEntryMaster.getRole()!=null){
			this.roleId = timeSheetEntryMaster.getRole().getUserRoleId();
			this.roleName =timeSheetEntryMaster.getRole().getRoleName();
		}
		
		if(timeSheetEntryMaster.getProduct()!=null){
			this.productId = timeSheetEntryMaster.getProduct().getProductId();
			log.info("---------------Getter--timeSheetEntryMaster.getProduct().getProductId()----"+productId);
			this.productName =timeSheetEntryMaster.getProduct().getProductName();
		}
		
		if(timeSheetEntryMaster.getApprover()!=null){
			this.approverId = timeSheetEntryMaster.getApprover().getUserId();
			this.approverName =timeSheetEntryMaster.getApprover().getUserDisplayName();
		}
		
		WorkPackage workpackageGetter=new WorkPackage();		
		workpackageGetter.setWorkPackageId(workPackageId);
		if(timeSheetEntryMaster.getShift() != null){
			this.shiftId = timeSheetEntryMaster.getShift().getShiftId();
			this.shiftName = timeSheetEntryMaster.getShift().getShiftName();
		}
		if(timeSheetEntryMaster.getActivityType() != null){
			this.activityTypeId = timeSheetEntryMaster.getActivityType().getActivityTypeId();
			this.activityTypeName = timeSheetEntryMaster.getActivityType().getActivityName();
		}
		if(resourceShiftCheckInId != null){			
			ResourceShiftCheckIn resourceShiftCheckIn = new ResourceShiftCheckIn();
			resourceShiftCheckIn.setResourceShiftCheckInId(this.resourceShiftCheckInId);
			timeSheetEntryMaster.setResourceShiftCheckIn(resourceShiftCheckIn);
			if(timeSheetEntryMaster.getResourceShiftCheckIn().getProductMaster() != null){
				log.info("---------------Get ---Resource Check in Product id "+timeSheetEntryMaster.getResourceShiftCheckIn().getProductMaster().getProductId());	
			}
		}
		
		return timeSheetEntryMaster;
	}
	

	public Integer getTimeSheetEntryId() {
		return timeSheetEntryId;
	}

	public void setTimeSheetEntryId(Integer timeSheetEntryId) {
		this.timeSheetEntryId = timeSheetEntryId;
	}

	public String getTimeEntryDate() {
		return timeEntryDate;
	}

	public void setTimeEntryDate(String timeEntryDate) {
		this.timeEntryDate = timeEntryDate;
	}

	public Integer getHours() {
		return hours;
	}

	public void setHours(Integer hours) {
		this.hours = hours;
	}

	public Integer getMins() {
		return mins;
	}

	public void setMins(Integer mins) {
		this.mins = mins;
	}

	public Integer getShiftId() {
		return shiftId;
	}

	public void setShiftId(Integer shiftId) {
		this.shiftId = shiftId;
	}

	public Integer getWorkPackageId() {
		return workPackageId;
	}

	public void setWorkPackageId(Integer workPackageId) {
		this.workPackageId = workPackageId;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String getApprovalComments() {
		return approvalComments;
	}

	public void setApprovalComments(String approvalComments) {
		this.approvalComments = approvalComments;
	}

	public String getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(String approvedDate) {
		this.approvedDate = approvedDate;
	}

	public Integer getApproverId() {
		return approverId;
	}

	public void setApproverId(Integer approverId) {
		this.approverId = approverId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getShiftName() {
		return shiftName;
	}

	public void setShiftName(String shiftName) {
		this.shiftName = shiftName;
	}

	public String getWorkPackageName() {
		return workPackageName;
	}

	public void setWorkPackageName(String workPackageName) {
		this.workPackageName = workPackageName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getApproverName() {
		return approverName;
	}

	public void setApproverName(String approverName) {
		this.approverName = approverName;
	}

	public Integer getIsApproved() {
		return isApproved;
	}

	public void setIsApproved(Integer isApproved) {
		this.isApproved = isApproved;
	}

	public Integer getActivityTypeId() {
		return activityTypeId;
	}

	public void setActivityTypeId(Integer activityTypeId) {
		this.activityTypeId = activityTypeId;
	}

	public String getActivityTypeName() {
		return activityTypeName;
	}

	public void setActivityTypeName(String activityTypeName) {
		this.activityTypeName = activityTypeName;
	}

	public Integer getResourceShiftCheckInId() {
		return resourceShiftCheckInId;
	}

	public void setResourceShiftCheckInId(Integer resourceShiftCheckInId) {
		this.resourceShiftCheckInId = resourceShiftCheckInId;
	}	

	public Integer getShiftTypeId() {
		return shiftTypeId;
	}

	public void setShiftTypeId(Integer shiftTypeId) {
		this.shiftTypeId = shiftTypeId;
	}

	public String getShiftTypeName() {
		return shiftTypeName;
	}

	public void setShiftTypeName(String shiftTypeName) {
		this.shiftTypeName = shiftTypeName;
	}

}
