package com.hcl.atf.taf.model.json;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.ActualShift;
import com.hcl.atf.taf.model.ResourceShiftCheckIn;
import com.hcl.atf.taf.model.UserList;

public class JsonResourceShiftCheckin {
	@JsonProperty
	private Integer resourceShiftCheckInId;
	@JsonProperty
	private String checkIn;
	@JsonProperty
	private String checkOut;
	@JsonProperty
	private String createdDate;
	@JsonProperty
	private Integer userId;
	@JsonProperty
	private Integer actaulShiftId;
	@JsonProperty
	private String userName;
	@JsonProperty
	private String actualShiftName;
	@JsonProperty
	private String startTimeRemarks;
	@JsonProperty
	private String endTimeRemarks;
	@JsonProperty
	private int testFactoryId;
	@JsonProperty
	private int productId;
	@JsonProperty
	private String productName;
	@JsonProperty
	private Integer isApproved;
	@JsonProperty
	private String approvalRemarks;
	@JsonProperty
	private String approvedDate;
	@JsonProperty
	private String approverName;
	@JsonProperty
	private String resourceShiftCheckInDisplayName;
	@JsonProperty
	private Integer approverId;
	@JsonProperty
	private String startTime;
	@JsonProperty
	private String endTime;
	@JsonProperty
	private Integer shiftId;
	@JsonProperty
	private String testFactoryName;
	@JsonProperty
	private Integer shiftTypeId;
	@JsonProperty
	private String shiftTypeName;
	@JsonProperty
	private String workDuration;
	@JsonProperty
	private String timeSheetHours;
	
	public JsonResourceShiftCheckin() {
	}
	public JsonResourceShiftCheckin(ResourceShiftCheckIn resourceShiftCheckin) {
		this.resourceShiftCheckInId = resourceShiftCheckin.getResourceShiftCheckInId();
		this.startTimeRemarks=resourceShiftCheckin.getStartTimeRemarks();
		this.endTimeRemarks=resourceShiftCheckin.getEndTimeRemarks();
		if(resourceShiftCheckin.getActualShift().getStartTime() !=null)
		{
			this.startTime =  DateUtility.dateToStringInSecond(resourceShiftCheckin.getActualShift().getStartTime());
		}
		if(resourceShiftCheckin.getActualShift().getEndTime() !=null)
		{
			this.endTime =  DateUtility.dateToStringInSecond(resourceShiftCheckin.getActualShift().getEndTime());
		}
		
		if(resourceShiftCheckin.getActualShift()!=null){
			if(resourceShiftCheckin.getActualShift().getShift()!=null)
				this.actualShiftName=resourceShiftCheckin.getActualShift().getShift().getShiftName();
			this.shiftId=resourceShiftCheckin.getActualShift().getShift().getShiftId();
			this.shiftTypeId=resourceShiftCheckin.getActualShift().getShift().getShiftType().getShiftTypeId();
			this.shiftTypeName=resourceShiftCheckin.getActualShift().getShift().getShiftType().getShiftName();
			this.testFactoryName=resourceShiftCheckin.getActualShift().getShift().getTestFactory().getTestFactoryName();
		}
		if(resourceShiftCheckin.getIsApproved()==null || resourceShiftCheckin.getIsApproved()==0){
		this.isApproved=0;
		}else{
			this.isApproved=resourceShiftCheckin.getIsApproved();
		}
		this.approvalRemarks=resourceShiftCheckin.getApprovalRemarks();
		if(resourceShiftCheckin.getCheckIn() == null){
			this.checkIn="";
		}else{
			this.checkIn = DateUtility.dateToStringInSecond(resourceShiftCheckin.getCheckIn());
			this.checkIn=this.checkIn.replace(" ", ",");
		}
		if(resourceShiftCheckin.getCheckOut() == null){
			this.checkOut="";
		}else{
			this.checkOut = DateUtility.dateToStringInSecond(resourceShiftCheckin.getCheckOut());
			this.checkOut=this.checkOut.replace(" ", ",");
		}
		if(resourceShiftCheckin.getActualShift() != null){
			this.actaulShiftId = resourceShiftCheckin.getActualShift().getActualShiftId();
		}
		if(resourceShiftCheckin.getUserList() != null){
			this.userId = resourceShiftCheckin.getUserList().getUserId();
			this.userName=resourceShiftCheckin.getUserList().getLoginId();
		}
		if(resourceShiftCheckin.getApproverUser() != null){
			this.approverName=resourceShiftCheckin.getApproverUser().getLoginId();
		}
		if(resourceShiftCheckin.getProductMaster() != null){
			this.productId = resourceShiftCheckin.getProductMaster().getProductId();
			this.productName=resourceShiftCheckin.getProductMaster().getProductName();
		}	
		if(resourceShiftCheckin.getApprovedDate()!=null){
			this.approvedDate = DateUtility.sdfDateformatWithOutTime(resourceShiftCheckin.getApprovedDate());
		}
		
		this.workDuration = "00:00";
		this.timeSheetHours = "00:00";
		String wShiftName =""; 
		String sTypeName = "";
		String prod_Name = "";
		String timefromDate = "";
		Date checkdatetime = resourceShiftCheckin.getCheckIn();
			if(resourceShiftCheckin.getActualShift() != null){
				if(resourceShiftCheckin.getActualShift().getShift() != null){
					wShiftName= resourceShiftCheckin.getActualShift().getShift().getShiftName();
					if(resourceShiftCheckin.getActualShift().getShift().getShiftType() != null){
						sTypeName =	resourceShiftCheckin.getActualShift().getShift().getShiftType().getShiftName();
						if( resourceShiftCheckin.getProductMaster()!=null){
						prod_Name = resourceShiftCheckin.getProductMaster().getProductName();
						this.resourceShiftCheckInDisplayName = sTypeName+"|"+ wShiftName+"|"+prod_Name+"|"+timefromDate;
						}
						//
						timefromDate = ""+resourceShiftCheckin.getCheckIn().getHours()+":"+resourceShiftCheckin.getCheckIn().getMinutes();	
					}					
					this.resourceShiftCheckInDisplayName = sTypeName+"|"+ wShiftName+"|"+prod_Name+"|"+timefromDate;	
				}
				
			}	
			else{
				this.resourceShiftCheckInDisplayName = "";
			}
		
	}
	
	@JsonIgnore
	public ResourceShiftCheckIn  getResourceShiftCheckin(){
		
		ResourceShiftCheckIn resourceShiftCheckin=new ResourceShiftCheckIn();
		
		resourceShiftCheckin.setResourceShiftCheckInId(this.resourceShiftCheckInId);
		resourceShiftCheckin.setApprovalRemarks(this.approvalRemarks);
	if((this.isApproved==null)||(this.isApproved==0)){
		resourceShiftCheckin.setIsApproved(0);
		}else{
			resourceShiftCheckin.setIsApproved(this.isApproved);
		}
		resourceShiftCheckin.setApprovedDate(DateUtility.getDateFromDateTime(new Date(System.currentTimeMillis())));
		if(this.createdDate == null || this.createdDate.trim().isEmpty()) {
			
			resourceShiftCheckin.setCreatedDate(DateUtility.getDateFromDateTime(new Date(System.currentTimeMillis())));
		} else {
		
			resourceShiftCheckin.setCreatedDate(DateUtility.dateFormatWithOutSeconds(this.createdDate));
		}
		if(!(this.checkOut == null || this.checkOut.trim().isEmpty())) {
			resourceShiftCheckin.setCheckOut(DateUtility.toDateInSec(this.checkOut));
		}
		if(!(this.checkIn == null || this.checkIn.trim().isEmpty())) {
			resourceShiftCheckin.setCheckIn(DateUtility.toDateInSec(this.checkIn));
		}
		if(this.userId!=null){
			UserList user=new UserList();
			user.setUserId(this.userId);
			resourceShiftCheckin.setUserList(user);
		}
		if(this.actaulShiftId!=null){
	    ActualShift actualShift=new ActualShift();
	    actualShift.setActualShiftId(actaulShiftId);
	    
	    resourceShiftCheckin.setActualShift(actualShift);
	  
	    
		}

	return resourceShiftCheckin;
	}
public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getActualShiftName() {
		return actualShiftName;
	}
	public void setActualShiftName(String actualShiftName) {
		this.actualShiftName = actualShiftName;
	}
	public Integer getResourceShiftCheckInId() {
		return resourceShiftCheckInId;
	}
	public void setResourceShiftCheckInId(Integer resourceShiftCheckInId) {
		this.resourceShiftCheckInId = resourceShiftCheckInId;
	}
	public String getCheckIn() {
		return checkIn;
	}
	public void setCheckIn(String checkIn) {
		this.checkIn = checkIn;
	}
	public String getCheckOut() {
		return checkOut;
	}
	public void setCheckOut(String checkOut) {
		this.checkOut = checkOut;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getActaulShiftId() {
		return actaulShiftId;
	}
	public void setActaulShiftId(Integer actaulShiftId) {
		this.actaulShiftId = actaulShiftId;
	}
	public String getStartTimeRemarks() {
		return startTimeRemarks;
	}
	public void setStartTimeRemarks(String startTimeRemarks) {
		this.startTimeRemarks = startTimeRemarks;
	}
	public String getEndTimeRemarks() {
		return endTimeRemarks;
	}
	public void setEndTimeRemarks(String endTimeRemarks) {
		this.endTimeRemarks = endTimeRemarks;
	}
	public int getTestFactoryId() {
		return testFactoryId;
	}
	public void setTestFactoryId(int testFactoryId) {
		this.testFactoryId = testFactoryId;
	}
	public String getResourceShiftCheckInDisplayName() {
		return resourceShiftCheckInDisplayName;
	}
	public void setResourceShiftCheckInDisplayName(
			String resourceShiftCheckInDisplayName) {
		this.resourceShiftCheckInDisplayName = resourceShiftCheckInDisplayName;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public Integer getIsApproved() {
		return isApproved;
	}
	public void setIsApproved(Integer isApproved) {
		this.isApproved = isApproved;
	}
	public String getApprovalRemarks() {
		return approvalRemarks;
	}
	public void setApprovalRemarks(String approvalRemarks) {
		this.approvalRemarks = approvalRemarks;
	}
	public String getApprovedDate() {
		return approvedDate;
	}
	public void setApprovedDate(String approvedDate) {
		this.approvedDate = approvedDate;
	}
	public String getApproverName() {
		return approverName;
	}
	public void setApproverName(String approverName) {
		this.approverName = approverName;
	}
	public Integer getApproverId() {
		return approverId;
	}
	public void setApproverId(Integer approverId) {
		this.approverId = approverId;
	}
	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getTestFactoryName() {
		return testFactoryName;
	}
	public void setTestFactoryName(String testFactoryName) {
		this.testFactoryName = testFactoryName;
	}
	public String getWorkDuration() {
		return workDuration;
	}
	public void setWorkDuration(String workDuration) {
		this.workDuration = workDuration;
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
	public String getTimeSheetHours() {
		return timeSheetHours;
	}
	public void setTimeSheetHours(String timeSheetHours) {
		this.timeSheetHours = timeSheetHours;
	}
	
}
