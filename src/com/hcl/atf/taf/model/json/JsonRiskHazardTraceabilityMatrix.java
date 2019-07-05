package com.hcl.atf.taf.model.json;

import java.text.SimpleDateFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.dto.RiskHazardTraceabilityMatrixDTO;

public class JsonRiskHazardTraceabilityMatrix implements java.io.Serializable {
	
	private static final Log log = LogFactory.getLog(JsonRiskHazardTraceabilityMatrix.class);
	@JsonProperty
	private Integer riskId;
	@JsonProperty
	private  String riskName;
	@JsonProperty
	private Integer featureId;
	@JsonProperty
	private Integer riskMitigationId;
	@JsonProperty
	private Integer testCaseId;
	@JsonProperty
	private String SeverityName1;
	@JsonProperty
	private String LikeliHoodName1;
	@JsonProperty
	private String RiskPriority1;
	@JsonProperty
	private String SeverityName2;
	@JsonProperty
	private String LikeliHoodName2;
	@JsonProperty
	private String RiskPriority2;
	@JsonProperty
	private String RiskMitigationCode;
	@JsonProperty
	private String result;
	@JsonProperty
	private Integer iterationNumber;
	@JsonProperty
	private String lifecyclephase;
	@JsonProperty
	private String date;
	@JsonProperty
	private Integer testcaseexeid;
	@JsonProperty
	private String testcasename;
	@JsonProperty
	private Integer bugid;
	@JsonProperty
	private String bugCreationTime;
	@JsonProperty
	private String severityName;
	@JsonProperty
	private Integer scriptId;
	@JsonProperty
	private String reOpenDate;
	@JsonProperty
	private String stageName;
	@JsonProperty
	private String status;
	@JsonProperty
	private String resolution;
	@JsonProperty
	private String scriptdate;
	
public JsonRiskHazardTraceabilityMatrix() {
	}
	
	public JsonRiskHazardTraceabilityMatrix(RiskHazardTraceabilityMatrixDTO riskHazardTraceMatrixDto) {
		this.riskId = riskHazardTraceMatrixDto.getRiskId();
		this.riskName = riskHazardTraceMatrixDto.getRiskName();
		this.featureId = riskHazardTraceMatrixDto.getFeatureId();
		this.testCaseId = riskHazardTraceMatrixDto.getTestCaseId();
		this.SeverityName1 = riskHazardTraceMatrixDto.getSeverityName1();
		this.LikeliHoodName1 = riskHazardTraceMatrixDto.getLikeliHoodName1();
		this.RiskPriority1 = riskHazardTraceMatrixDto.getRiskPriority1();
		this.SeverityName2 = riskHazardTraceMatrixDto.getSeverityName2();
		this.LikeliHoodName2 = riskHazardTraceMatrixDto.getLikeliHoodName2();
		this.RiskPriority2 = riskHazardTraceMatrixDto.getRiskPriority2();
		this.RiskMitigationCode = riskHazardTraceMatrixDto.getRiskMitigationCode();
		this.result = riskHazardTraceMatrixDto.getResult();
		this.iterationNumber = riskHazardTraceMatrixDto.getIterationNumber();
		this.lifecyclephase = riskHazardTraceMatrixDto.getLifecyclephase();
		if(riskHazardTraceMatrixDto.getDate() !=null){
			String dt= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(riskHazardTraceMatrixDto.getDate());
			this.date =dt;
		}
		this.testcaseexeid = riskHazardTraceMatrixDto.getTestcaseexeid();
		this.testcasename = riskHazardTraceMatrixDto.getTestcasename();
	}
	
	@JsonIgnore
	public RiskHazardTraceabilityMatrixDTO getRiskHazardTraceabilityMatrixList() {
		RiskHazardTraceabilityMatrixDTO riskHazardTraceMatrix = new RiskHazardTraceabilityMatrixDTO();
		
		riskHazardTraceMatrix.setTestCaseId(testCaseId);
		riskHazardTraceMatrix.setResult(result);
		riskHazardTraceMatrix.setTestcaseexeid(testcaseexeid);

		return riskHazardTraceMatrix;
		
	}
	
	//For Defect fix fail report
	public JsonRiskHazardTraceabilityMatrix(RiskHazardTraceabilityMatrixDTO fixFaildto,Integer filter) {
		
		this.testCaseId = fixFaildto.getTestCaseId();
		this.bugid = fixFaildto.getBugId();		
		if(fixFaildto.getBugCreationTime() !=null){
			String date= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(fixFaildto.getBugCreationTime());
			this.bugCreationTime = date;
		}		
		this.result = fixFaildto.getResult();
		this.iterationNumber = fixFaildto.getIterationNumber();
		this.lifecyclephase = fixFaildto.getLifecyclephase();
		if(fixFaildto.getDate() !=null){
			String dt= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(fixFaildto.getDate());
			this.date =dt;
		}
		this.scriptId = fixFaildto.getScriptId();
		this.reOpenDate = fixFaildto.getReOpenDate();
		this.scriptdate = fixFaildto.getScriptdate();
		this.testcaseexeid = fixFaildto.getTestcaseexeid();
	}
	
	//For test fix fail report
		public JsonRiskHazardTraceabilityMatrix(RiskHazardTraceabilityMatrixDTO fixFaildto,String filter) {
			
			this.testCaseId = fixFaildto.getTestCaseId();
			this.featureId = fixFaildto.getFeatureId();
			this.stageName = fixFaildto.getStageName();
			this.bugid = fixFaildto.getBugId();						
			this.result = fixFaildto.getResult();
			this.iterationNumber = fixFaildto.getIterationNumber();
			this.lifecyclephase = fixFaildto.getLifecyclephase();
			if(fixFaildto.getDate() !=null){
				String dt= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(fixFaildto.getDate());
				this.date =dt;
			}
			this.scriptId = fixFaildto.getScriptId();
			this.reOpenDate = fixFaildto.getReOpenDate();
			this.status = fixFaildto.getStatus();
			this.resolution = fixFaildto.getResolution();
			if(fixFaildto.getBugCreationTime() !=null){
				String date= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(fixFaildto.getBugCreationTime());
				this.bugCreationTime = date;
			}	
			this.testcaseexeid = fixFaildto.getTestcaseexeid();
		}

	public Integer getRiskId() {
		return riskId;
	}

	public void setRiskId(Integer riskId) {
		this.riskId = riskId;
	}

	public String getRiskName() {
		return riskName;
	}

	public void setRiskName(String riskName) {
		this.riskName = riskName;
	}

	public Integer getFeatureId() {
		return featureId;
	}

	public void setFeatureId(Integer featureId) {
		this.featureId = featureId;
	}

	public Integer getRiskMitigationId() {
		return riskMitigationId;
	}

	public void setRiskMitigationId(Integer riskMitigationId) {
		this.riskMitigationId = riskMitigationId;
	}

	public Integer getTestCaseId() {
		return testCaseId;
	}

	public void setTestCaseId(Integer testCaseId) {
		this.testCaseId = testCaseId;
	}

	public String getSeverityName1() {
		return SeverityName1;
	}

	public void setSeverityName1(String severityName1) {
		SeverityName1 = severityName1;
	}

	public String getLikeliHoodName1() {
		return LikeliHoodName1;
	}

	public void setLikeliHoodName1(String likeliHoodName1) {
		LikeliHoodName1 = likeliHoodName1;
	}

	public String getRiskPriority1() {
		return RiskPriority1;
	}

	public void setRiskPriority1(String riskPriority1) {
		RiskPriority1 = riskPriority1;
	}

	public String getSeverityName2() {
		return SeverityName2;
	}

	public void setSeverityName2(String severityName2) {
		SeverityName2 = severityName2;
	}

	public String getLikeliHoodName2() {
		return LikeliHoodName2;
	}

	public void setLikeliHoodName2(String likeliHoodName2) {
		LikeliHoodName2 = likeliHoodName2;
	}

	public String getRiskPriority2() {
		return RiskPriority2;
	}

	public void setRiskPriority2(String riskPriority2) {
		RiskPriority2 = riskPriority2;
	}

	public String getRiskMitigationCode() {
		return RiskMitigationCode;
	}

	public void setRiskMitigationCode(String riskMitigationCode) {
		RiskMitigationCode = riskMitigationCode;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public Integer getIterationNumber() {
		return iterationNumber;
	}

	public void setIterationNumber(Integer iterationNumber) {
		this.iterationNumber = iterationNumber;
	}

	public String getLifecyclephase() {
		return lifecyclephase;
	}

	public void setLifecyclephase(String lifecyclephase) {
		this.lifecyclephase = lifecyclephase;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Integer getTestcaseexeid() {
		return testcaseexeid;
	}

	public void setTestcaseexeid(Integer testcaseexeid) {
		this.testcaseexeid = testcaseexeid;
	}

	public String getTestcasename() {
		return testcasename;
	}

	public void setTestcasename(String testcasename) {
		this.testcasename = testcasename;
	}

	public Integer getBugid() {
		return bugid;
	}

	public void setBugid(Integer bugid) {
		this.bugid = bugid;
	}

	public String getBugCreationTime() {
		return bugCreationTime;
	}

	public void setBugCreationTime(String bugCreationTime) {
		this.bugCreationTime = bugCreationTime;
	}

	public String getSeverityName() {
		return severityName;
	}

	public void setSeverityName(String severityName) {
		this.severityName = severityName;
	}

	public Integer getScriptId() {
		return scriptId;
	}

	public void setScriptId(Integer scriptId) {
		this.scriptId = scriptId;
	}

	public String getReOpenDate() {
		return reOpenDate;
	}

	public void setReOpenDate(String reOpenDate) {
		this.reOpenDate = reOpenDate;
	}

	public String getStageName() {
		return stageName;
	}

	public void setStageName(String stageName) {
		this.stageName = stageName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getResolution() {
		return resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

}
