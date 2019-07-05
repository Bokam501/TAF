package com.hcl.atf.taf.model.json;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.DefectApprovalStatusMaster;
import com.hcl.atf.taf.model.TestExecutionResultBugList;
import com.hcl.atf.taf.model.dto.DefectWeeklyReportDTO;


public class JsonDefectsWeeklyReport implements java.io.Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@JsonProperty
	private int defectId;
	@JsonProperty	
	private String defectName;
	@JsonProperty
	private Integer defectsApprovalStatusId;
	@JsonProperty	
	private String defectsApprovalStatusName;
	@JsonProperty
	private Integer defectsTypeId;
	@JsonProperty	
	private String defectsTypeName;
	@JsonProperty	
	private String weekStartDate;
	@JsonProperty	
	private Integer weekNo;
	@JsonProperty	
	private String day1ValueforToolTip;
	@JsonProperty	
	private String day2ValueforToolTip;
	@JsonProperty	
	private String day3ValueforToolTip;
	@JsonProperty	
	private String day4ValueforToolTip;
	@JsonProperty	
	private String day5ValueforToolTip;
	@JsonProperty	
	private String day6ValueforToolTip;
	@JsonProperty	
	private String day7ValueforToolTip;
	@JsonProperty	
	private String  day1;
	@JsonProperty	
	private String day2;
	@JsonProperty	
	private String day3;
	@JsonProperty	
	private String day4;
	@JsonProperty	
	private String day5;
	@JsonProperty	
	private String day6;
	@JsonProperty	
	private String day7;
	
	@JsonProperty	
	private Integer day1LiveDefectsCount;
	@JsonProperty	
	private Integer day2LiveDefectsCount;
	@JsonProperty	
	private Integer day3LiveDefectsCount;
	@JsonProperty	
	private Integer day4LiveDefectsCount;
	@JsonProperty	
	private Integer day5LiveDefectsCount;
	@JsonProperty	
	private Integer day6LiveDefectsCount;
	@JsonProperty	
	private Integer day7LiveDefectsCount;
	@JsonProperty	
	private Integer day8LiveDefectsCount;
	
	@JsonProperty	
	private Integer day1WebDefectsCount;
	@JsonProperty	
	private Integer day2WebDefectsCount;
	@JsonProperty	
	private Integer day3WebDefectsCount;
	@JsonProperty	
	private Integer day4WebDefectsCount;
	@JsonProperty	
	private Integer day5WebDefectsCount;
	@JsonProperty	
	private Integer day6WebDefectsCount;
	@JsonProperty	
	private Integer day7WebDefectsCount;
	@JsonProperty	
	private Integer day8WebDefectsCount;
	@JsonProperty	
	private Integer totalLiveReportCount;
	@JsonProperty	
	private Integer totalWebReportCount;
	@JsonProperty
	private Integer weekDateNumber;
	@JsonProperty
	private String weekDateName;
	
	public JsonDefectsWeeklyReport() {
		this.day1LiveDefectsCount = new Integer(0);
		this.day2LiveDefectsCount = new Integer(0);
		this.day3LiveDefectsCount = new Integer(0);
		this.day4LiveDefectsCount = new Integer(0);
		this.day5LiveDefectsCount = new Integer(0);
		this.day6LiveDefectsCount = new Integer(0);
		this.day7LiveDefectsCount = new Integer(0);
		this.day8LiveDefectsCount = new Integer(0);
		this.totalLiveReportCount = new Integer(0);
		
		this.day1WebDefectsCount = new Integer(0);
		this.day2WebDefectsCount = new Integer(0);
		this.day3WebDefectsCount = new Integer(0);
		this.day4WebDefectsCount = new Integer(0);
		this.day5WebDefectsCount = new Integer(0);
		this.day6WebDefectsCount = new Integer(0);
		this.day7WebDefectsCount = new Integer(0);
		this.day8WebDefectsCount = new Integer(0);
		this.totalWebReportCount = new Integer(0);
		
		this.day1 = new String("-");
		this.day2 = new String("-");
		this.day3= new String("-");
		this.day4 = new String("-");
		this.day5= new String("-");
		this.day6 = new String("-");
		this.day7 = new String("-");
		
		this.day1ValueforToolTip=new String();
		this.day2ValueforToolTip=new String();
		this.day3ValueforToolTip=new String();
		this.day4ValueforToolTip=new String();
		this.day5ValueforToolTip=new String();
		this.day6ValueforToolTip=new String();
		this.day7ValueforToolTip=new String();
	}	

	public JsonDefectsWeeklyReport(TestExecutionResultBugList testExecutionResultBug) {
		this.defectId=testExecutionResultBug.getTestExecutionResultBugId();
		
		this.defectsApprovalStatusId = testExecutionResultBug.getDefectApprovalStatus().getApprovalStatusId();
		this.defectsApprovalStatusName = testExecutionResultBug.getDefectApprovalStatus().getApprovalStatusName();
	
		this.day1LiveDefectsCount = new Integer(0);
		this.day2LiveDefectsCount = new Integer(0);
		this.day3LiveDefectsCount = new Integer(0);
		this.day4LiveDefectsCount = new Integer(0);
		this.day5LiveDefectsCount = new Integer(0);
		this.day6LiveDefectsCount = new Integer(0);
		this.day7LiveDefectsCount = new Integer(0);
		this.day8LiveDefectsCount = new Integer(0);
		this.totalLiveReportCount = new Integer(0);
		
		this.day1WebDefectsCount = new Integer(0);
		this.day2WebDefectsCount = new Integer(0);
		this.day3WebDefectsCount = new Integer(0);
		this.day4WebDefectsCount = new Integer(0);
		this.day5WebDefectsCount = new Integer(0);
		this.day6WebDefectsCount = new Integer(0);
		this.day7WebDefectsCount = new Integer(0);
		this.day8WebDefectsCount = new Integer(0);
		this.totalWebReportCount = new Integer(0);
		
		
		this.day1 = new String();
		this.day2 = new String();
		this.day3= new String();
		this.day4 = new String();
		this.day5= new String();
		this.day6 = new String();
		this.day7 = new String();
		
		this.day1ValueforToolTip=new String();
		this.day2ValueforToolTip=new String();
		this.day3ValueforToolTip=new String();
		this.day4ValueforToolTip=new String();
		this.day5ValueforToolTip=new String();
		this.day6ValueforToolTip=new String();
		this.day7ValueforToolTip=new String();
	}
	
	public String getDay1ValueforToolTip() {
		return day1ValueforToolTip;
	}

	public void setDay1ValueforToolTip(String day1ValueforToolTip) {
		this.day1ValueforToolTip = day1ValueforToolTip;
	}

	public String getDay2ValueforToolTip() {
		return day2ValueforToolTip;
	}

	public void setDay2ValueforToolTip(String day2ValueforToolTip) {
		this.day2ValueforToolTip = day2ValueforToolTip;
	}

	public String getDay3ValueforToolTip() {
		return day3ValueforToolTip;
	}

	public void setDay3ValueforToolTip(String day3ValueforToolTip) {
		this.day3ValueforToolTip = day3ValueforToolTip;
	}

	public String getDay4ValueforToolTip() {
		return day4ValueforToolTip;
	}

	public void setDay4ValueforToolTip(String day4ValueforToolTip) {
		this.day4ValueforToolTip = day4ValueforToolTip;
	}

	public String getDay5ValueforToolTip() {
		return day5ValueforToolTip;
	}

	public void setDay5ValueforToolTip(String day5ValueforToolTip) {
		this.day5ValueforToolTip = day5ValueforToolTip;
	}

	public String getDay6ValueforToolTip() {
		return day6ValueforToolTip;
	}

	public void setDay6ValueforToolTip(String day6ValueforToolTip) {
		this.day6ValueforToolTip = day6ValueforToolTip;
	}

	public String getDay7ValueforToolTip() {
		return day7ValueforToolTip;
	}

	public void setDay7ValueforToolTip(String day7ValueforToolTip) {
		this.day7ValueforToolTip = day7ValueforToolTip;
	}


	public String getDay1() {
		return day1;
	}

	public void setDay1(String day1) {
		this.day1 = day1;
	}

	public String getDay2() {
		return day2;
	}

	public void setDay2(String day2) {
		this.day2 = day2;
	}

	public String getDay3() {
		return day3;
	}

	public void setDay3(String day3) {
		this.day3 = day3;
	}

	public String getDay4() {
		return day4;
	}

	public void setDay4(String day4) {
		this.day4 = day4;
	}

	public String getDay5() {
		return day5;
	}

	public void setDay5(String day5) {
		this.day5 = day5;
	}

	public String getDay6() {
		return day6;
	}

	public void setDay6(String day6) {
		this.day6 = day6;
	}

	public String getDay7() {
		return day7;
	}

	public void setDay7(String day7) {
		this.day7 = day7;
	}


	@JsonIgnore
	public TestExecutionResultBugList getTestExecutionResultBugList() {
	
		TestExecutionResultBugList testExecutionResultBug = new TestExecutionResultBugList();
		testExecutionResultBug.setTestExecutionResultBugId(this.defectId);
		
		DefectApprovalStatusMaster defectApprovalStatus = new DefectApprovalStatusMaster();
		defectApprovalStatus.setApprovalStatusId(this.defectsApprovalStatusId);
		defectApprovalStatus.setApprovalStatusName(this.defectsApprovalStatusName);
		testExecutionResultBug.setDefectApprovalStatus(defectApprovalStatus);
		
		return testExecutionResultBug;
	}
	
	
	public JsonDefectsWeeklyReport(DefectWeeklyReportDTO defectWeeklyReportDTO) {
		this.totalLiveReportCount = 0;
		this.totalWebReportCount = 0;
		DefectApprovalStatusMaster defectApproval = defectWeeklyReportDTO.getDefectApprovalStatus();
		if(defectApproval != null){
			this.defectsApprovalStatusId = defectApproval.getApprovalStatusId();
			this.defectsApprovalStatusName = defectApproval.getApprovalStatusName();
		}else{
			this.defectsApprovalStatusId = 0;
			this.defectsApprovalStatusName = "";
		}
		this.weekNo=defectWeeklyReportDTO.getWeekNo();
		Integer dayWiseArr[][]=null;
		if(defectWeeklyReportDTO.getDateWiseDefectDetailsArry() != null){
			dayWiseArr = (Integer[][])defectWeeklyReportDTO.getDateWiseDefectDetailsArry();
		}
		
		if(dayWiseArr != null){
			this.totalLiveReportCount = 0;
			this.totalWebReportCount = 0;
			if(dayWiseArr[0][0] != null){
				this.day1LiveDefectsCount = dayWiseArr[0][0];
			}
			if(dayWiseArr[0][1] != null){
				this.day1WebDefectsCount = dayWiseArr[0][1];
			}
			
			if(dayWiseArr[1][0] != null){
				this.day2LiveDefectsCount = dayWiseArr[1][0];
			}
			if(dayWiseArr[1][1] != null){
				this.day2WebDefectsCount = dayWiseArr[1][1];
			}
			
			if(dayWiseArr[2][0] != null){
				this.day3LiveDefectsCount = dayWiseArr[2][0];
			}
			if(dayWiseArr[2][1] != null){
				this.day3WebDefectsCount = dayWiseArr[2][1];
			}
			
			if(dayWiseArr[3][0] != null){
				this.day4LiveDefectsCount = dayWiseArr[3][0];
			}
			if(dayWiseArr[3][1] != null){
				this.day4WebDefectsCount = dayWiseArr[3][1];
			}
			
			if(dayWiseArr[4][0] != null){
				this.day5LiveDefectsCount = dayWiseArr[4][0];
			}
			if(dayWiseArr[4][1] != null){
				this.day5WebDefectsCount = dayWiseArr[4][1];
			}
			
			if(dayWiseArr[5][0] != null){
				this.day6LiveDefectsCount = dayWiseArr[5][0];
			}
			if(dayWiseArr[5][1] != null){
				this.day6WebDefectsCount = dayWiseArr[5][1];
			}
			
			if(dayWiseArr[6][0] != null){
				this.day7LiveDefectsCount = dayWiseArr[6][0];
			}
			if(dayWiseArr[6][1] != null){
				this.day7WebDefectsCount = dayWiseArr[6][1];
			}
			this.totalLiveReportCount = dayWiseArr[0][0]+dayWiseArr[1][0]+dayWiseArr[2][0]+dayWiseArr[3][0]+dayWiseArr[4][0]+dayWiseArr[5][0]+dayWiseArr[6][0];	
			this.totalWebReportCount = dayWiseArr[0][1]+dayWiseArr[1][1]+dayWiseArr[2][1]+dayWiseArr[3][1]+dayWiseArr[4][1]+dayWiseArr[5][1]+dayWiseArr[6][1];	
		    }
	}
	
	public Integer getDay1LiveDefectsCount() {
		return day1LiveDefectsCount;
	}

	public void setDay1LiveDefectsCount(Integer day1LiveDefectsCount) {
		this.day1LiveDefectsCount = day1LiveDefectsCount;
	}

	public Integer getDay2LiveDefectsCount() {
		return day2LiveDefectsCount;
	}

	public void setDay2LiveDefectsCount(Integer day2LiveDefectsCount) {
		this.day2LiveDefectsCount = day2LiveDefectsCount;
	}

	public Integer getDay3LiveDefectsCount() {
		return day3LiveDefectsCount;
	}

	public void setDay3LiveDefectsCount(Integer day3LiveDefectsCount) {
		this.day3LiveDefectsCount = day3LiveDefectsCount;
	}

	public Integer getDay4LiveDefectsCount() {
		return day4LiveDefectsCount;
	}

	public void setDay4LiveDefectsCount(Integer day4LiveDefectsCount) {
		this.day4LiveDefectsCount = day4LiveDefectsCount;
	}

	public Integer getDay5LiveDefectsCount() {
		return day5LiveDefectsCount;
	}

	public void setDay5LiveDefectsCount(Integer day5LiveDefectsCount) {
		this.day5LiveDefectsCount = day5LiveDefectsCount;
	}

	public Integer getDay6LiveDefectsCount() {
		return day6LiveDefectsCount;
	}

	public void setDay6LiveDefectsCount(Integer day6LiveDefectsCount) {
		this.day6LiveDefectsCount = day6LiveDefectsCount;
	}

	public Integer getDay7LiveDefectsCount() {
		return day7LiveDefectsCount;
	}

	public void setDay7LiveDefectsCount(Integer day7LiveDefectsCount) {
		this.day7LiveDefectsCount = day7LiveDefectsCount;
	}

	public Integer getDay8LiveDefectsCount() {
		return day8LiveDefectsCount;
	}

	public void setDay8LiveDefectsCount(Integer day8LiveDefectsCount) {
		this.day8LiveDefectsCount = day8LiveDefectsCount;
	}

	public Integer getDay1WebDefectsCount() {
		return day1WebDefectsCount;
	}

	public void setDay1WebDefectsCount(Integer day1WebDefectsCount) {
		this.day1WebDefectsCount = day1WebDefectsCount;
	}

	public Integer getDay2WebDefectsCount() {
		return day2WebDefectsCount;
	}

	public void setDay2WebDefectsCount(Integer day2WebDefectsCount) {
		this.day2WebDefectsCount = day2WebDefectsCount;
	}

	public Integer getDay3WebDefectsCount() {
		return day3WebDefectsCount;
	}

	public void setDay3WebDefectsCount(Integer day3WebDefectsCount) {
		this.day3WebDefectsCount = day3WebDefectsCount;
	}

	public Integer getDay4WebDefectsCount() {
		return day4WebDefectsCount;
	}

	public void setDay4WebDefectsCount(Integer day4WebDefectsCount) {
		this.day4WebDefectsCount = day4WebDefectsCount;
	}

	public Integer getDay5WebDefectsCount() {
		return day5WebDefectsCount;
	}

	public void setDay5WebDefectsCount(Integer day5WebDefectsCount) {
		this.day5WebDefectsCount = day5WebDefectsCount;
	}

	public Integer getDay6WebDefectsCount() {
		return day6WebDefectsCount;
	}

	public void setDay6WebDefectsCount(Integer day6WebDefectsCount) {
		this.day6WebDefectsCount = day6WebDefectsCount;
	}

	public Integer getDay7WebDefectsCount() {
		return day7WebDefectsCount;
	}

	public void setDay7WebDefectsCount(Integer day7WebDefectsCount) {
		this.day7WebDefectsCount = day7WebDefectsCount;
	}

	public Integer getDay8WebDefectsCount() {
		return day8WebDefectsCount;
	}

	public void setDay8WebDefectsCount(Integer day8WebDefectsCount) {
		this.day8WebDefectsCount = day8WebDefectsCount;
	}

	public Integer getWeekNo() {
		return weekNo;
	}

	public void setWeekNo(Integer weekNo) {
		this.weekNo = weekNo;
	}

	public String getWeekStartDate() {
		return weekStartDate;
	}

	public void setWeekStartDate(String weekStartDate) {
		this.weekStartDate = weekStartDate;
	}

	public int getDefectId() {
		return defectId;
	}

	public void setDefectId(int defectId) {
		this.defectId = defectId;
	}

	public String getDefectName() {
		return defectName;
	}

	public void setDefectName(String defectName) {
		this.defectName = defectName;
	}

	public Integer getDefectsApprovalStatusId() {
		return defectsApprovalStatusId;
	}

	public void setDefectsApprovalStatusId(Integer defectsApprovalStatusId) {
		this.defectsApprovalStatusId = defectsApprovalStatusId;
	}

	public String getDefectsApprovalStatusName() {
		return defectsApprovalStatusName;
	}

	public void setDefectsApprovalStatusName(String defectsApprovalStatusName) {
		this.defectsApprovalStatusName = defectsApprovalStatusName;
	}

	public Integer getDefectsTypeId() {
		return defectsTypeId;
	}

	public void setDefectsTypeId(Integer defectsTypeId) {
		this.defectsTypeId = defectsTypeId;
	}

	public String getDefectsTypeName() {
		return defectsTypeName;
	}

	public void setDefectsTypeName(String defectsTypeName) {
		this.defectsTypeName = defectsTypeName;
	}

	public Integer getTotalLiveReportCount() {
		return totalLiveReportCount;
	}

	public void setTotalLiveReportCount(Integer totalLiveReportCount) {
		this.totalLiveReportCount = totalLiveReportCount;
	}

	public Integer getTotalWebReportCount() {
		return totalWebReportCount;
	}

	public void setTotalWebReportCount(Integer totalWebReportCount) {
		this.totalWebReportCount = totalWebReportCount;
	}

	public String getWeekDateName() {
		return weekDateName;
	}

	public void setWeekDateName(String weekDateName) {
		this.weekDateName = weekDateName;
	}

	public Integer getWeekDateNumber() {
		return weekDateNumber;
	}

	public void setWeekDateNumber(Integer weekDateNumber) {
		this.weekDateNumber = weekDateNumber;
	}

}
