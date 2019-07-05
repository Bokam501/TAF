package com.hcl.atf.taf.dao;

import java.util.Date;
import java.util.List;






import com.hcl.atf.taf.model.ActualShift;
import com.hcl.atf.taf.model.ShiftTypeMaster;
import com.hcl.atf.taf.model.WorkShiftMaster;
import com.hcl.atf.taf.model.json.JsonWorkShiftMaster;


public interface WorkShiftMasterDAO  {

	List<WorkShiftMaster> listWorkShiftsByTestFactoryId(int testFactoryId);

	List<ActualShift> listActualShift(Integer workShiftId,	Date workDate);
	
	ActualShift listshiftManage(Integer workShiftId, Date workDate);
	
	public void addShiftManage(ActualShift actualShifts);

	public void updateShiftManage(ActualShift actualShifts);

	ActualShift listActualShiftbyActualShiftId(Integer actualShiftId);
	
	public void addTestFactoryShits(WorkShiftMaster workShiftMaster);

	WorkShiftMaster listWorkShiftsByshiftId(Integer shiftId);

	public void updateTestFactoryShitsInline(WorkShiftMaster workShiftMaster);
	
	ShiftTypeMaster getShiftTypeByShiftId(int shiftId);
	
	public WorkShiftMaster getWorkShiftByshiftTypeId(int shiftTypeId);

	ActualShift listActualShiftbyshiftId(Integer shiftId, Date workDate);

	List<ActualShift> getActualShiftByStartByUserId(Integer userId);

	List<JsonWorkShiftMaster> listJsonWorkShiftsByTestFactoryId(int testFactoryId);

}
