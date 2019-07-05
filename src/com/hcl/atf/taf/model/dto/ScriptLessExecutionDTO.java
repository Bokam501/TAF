package com.hcl.atf.taf.model.dto;

public class ScriptLessExecutionDTO {
	
	private static Integer workPackageId;
	private static String  workPackageName;
	private static String jobIDs;
	private static Integer jobsCount;
	private static Integer testCasesExecutionCount;
	
	public ScriptLessExecutionDTO(){
		
	}
	
	@SuppressWarnings("static-access")
	public ScriptLessExecutionDTO(Integer workPackageId,String workPackageName,String jobIDs, Integer jobsCount){
		this.workPackageId= workPackageId;
		this.workPackageName = workPackageName;
		this. jobIDs= jobIDs;
		this.jobsCount = jobsCount;
		
	}
	
	public ScriptLessExecutionDTO(String jobIDs, Integer testCasesExecutionCount){
		this.jobIDs= jobIDs;
		this.testCasesExecutionCount = testCasesExecutionCount;
	}
	
	public static Integer getWorkPackageId() {
		return workPackageId;
	}
	public static Integer getTestCasesExecutionCount() {
		return testCasesExecutionCount;
	}

	public static void setTestCasesExecutionCount(Integer testCasesExecutionCount) {
		ScriptLessExecutionDTO.testCasesExecutionCount = testCasesExecutionCount;
	}

	public static void setWorkPackageId(Integer workPackageId) {
		ScriptLessExecutionDTO.workPackageId = workPackageId;
	}
	public static String getWorkPackageName() {
		return workPackageName;
	}
	public static void setWorkPackageName(String workPackageName) {
		ScriptLessExecutionDTO.workPackageName = workPackageName;
	}
	public static String getJobIDs() {
		return jobIDs;
	}
	public static void setJobIDs(String jobIDs) {
		ScriptLessExecutionDTO.jobIDs = jobIDs;
	}
	public static Integer getJobsCount() {
		return jobsCount;
	}
	public static void setJobsCount(Integer jobsCount) {
		ScriptLessExecutionDTO.jobsCount = jobsCount;
	}
}