package com.hcl.atf.taf.dao;

import java.util.List;

import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.TrccExecutionPlan;
import com.hcl.atf.taf.model.TrccExecutionPlanDetails;

public interface TrccExecutionPlansCommonDAO  {	 

	//Test Execution Plan Methods - Start
	int getTotalRecordsOfTrccExecutionPlan(int testRunConfigurationChildId);
	List<TrccExecutionPlan> listTrccExecutionPlan(int testRunConfigurationChildId);
	TrccExecutionPlan getTrccExecutionPlanById(int trccExecutionPlanId);
	TrccExecutionPlan getTrccExecutionPlanByName(String planName);
	void addTrccExecutionPlan (TrccExecutionPlan trccExecutionPlan);
	void updateTrccExecutionPlan (TrccExecutionPlan trccExecutionPlan);
	void deleteTrccExecutionPlan (TrccExecutionPlan trccExecutionPlan);
	
	//Test Execution Plan Details Methods - Start
	List<TrccExecutionPlanDetails> listTrccExecutionPlanDetails(int trccExecutionPlanId, int deviceListId);
	void addTrccExecutionPlanDetails(List<TrccExecutionPlanDetails> trccExecutionPlanDetails);
	void updateTrccExecutionPlanDetail(TrccExecutionPlanDetails trccExecutionPlanDetails);
	void deleteExistingTrccExecutionPlanDetails(List<TrccExecutionPlanDetails> trccExecutionPlanDetails);
	void deleteExistingTrccExecutionPlanDetails(int trccExecutionPlanId, int deviceListId);
	List<TestCaseList> getSelectedTestCasesFromPlanDetails(int executionPlanId, Integer deviceListId);
	int getTotalRecordsOfTrccExecutionPlanDetailsOnADevice(int trccExecutionPlanId, int deviceListId); 
}
