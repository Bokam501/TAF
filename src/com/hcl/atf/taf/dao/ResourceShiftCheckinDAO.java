package com.hcl.atf.taf.dao;

import java.util.Date;
import java.util.List;

import com.hcl.atf.taf.model.ActualShift;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.ResourceShiftCheckIn;
import com.hcl.atf.taf.model.json.JsonResourceShiftCheckin;


public interface ResourceShiftCheckinDAO {

	List<ResourceShiftCheckIn> listResourceShiftCkeckInByUserId(int userId);
	void addResourceShiftCkeckIn(ResourceShiftCheckIn resourceShiftCheckin);

	void updateResourceShiftCkeckIn(ResourceShiftCheckIn resourceShiftCheckin);
	List<ResourceShiftCheckIn> listResourceShiftCkeckIn(int testFactoryId,Date workDate, int userId,ActualShift actualShift,int isApproved, int shiftTypeId);
	ResourceShiftCheckIn getByresourceShiftCheckInId(int resourceShiftCheckInId);
	List<ProductMaster> getProductByResourceShiftCheckInId(int resourceShiftCheckInId);
	void deleteResorceShiftCheckIn(ResourceShiftCheckIn resourceShiftCheckIn);
	List<ResourceShiftCheckIn> getResourceShiftCheckInByDate(Date createdDate, int userId);
	List<ResourceShiftCheckIn> getResourceShiftCheckInByDateAndShift(Date createdDate,int actualShiftId);
	JsonResourceShiftCheckin getResourceShiftCheckInsForAWeekDay(Date workDate, int userId, int shiftTypeId);
	List<ResourceShiftCheckIn> getResourceShiftCheckInsForADay(Date workDate, int userId, int shiftTypeId);
}
