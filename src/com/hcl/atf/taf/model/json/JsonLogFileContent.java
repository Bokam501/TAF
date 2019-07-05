package com.hcl.atf.taf.model.json;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.ActivityGroup;


public class JsonLogFileContent {

	private static final Log log = LogFactory.getLog(JsonLogFileContent.class);
	
	@JsonProperty
	private Integer jobId;
	@JsonProperty
	private Integer workpackageId;
	@JsonProperty
	private Integer startingLine;
	@JsonProperty
	private Integer lastLine;
	@JsonProperty
	private String logFileContent;
	@JsonProperty
	private String jobStatus;


	public JsonLogFileContent(){	
	}


	public Integer getStartingLine() {
		return startingLine;
	}


	public void setStartingLine(Integer startingLine) {
		this.startingLine = startingLine;
	}


	public Integer getLastLine() {
		return lastLine;
	}


	public void setLastLine(Integer lastLine) {
		this.lastLine = lastLine;
	}


	public String getLogFileContent() {
		return logFileContent;
	}


	public void setLogFileContent(String logFileContent) {
		this.logFileContent = logFileContent;
	}


	public Integer getJobId() {
		return jobId;
	}


	public void setJobId(Integer jobId) {
		this.jobId = jobId;
	}


	public Integer getWorkpackageId() {
		return workpackageId;
	}


	public void setWorkpackageId(Integer workpackageId) {
		this.workpackageId = workpackageId;
	}


	public String getJobStatus() {
		return jobStatus;
	}


	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}
	
	
	
}
