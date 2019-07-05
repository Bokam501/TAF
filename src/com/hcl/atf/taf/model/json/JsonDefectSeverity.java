package com.hcl.atf.taf.model.json;



import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.DefectSeverity;
public class JsonDefectSeverity implements java.io.Serializable {
	private static final Log log = LogFactory
			.getLog(JsonDefectSeverity.class);
	@JsonProperty
	private Integer severityId;	
	@JsonProperty
	private String severityName;

	public JsonDefectSeverity() {
	}

	public JsonDefectSeverity(DefectSeverity defectSeverity){
		this.severityId = defectSeverity.getSeverityId();
		this.severityName = defectSeverity.getSeverityName();
	}	
	
	@JsonIgnore
	public DefectSeverity getDefectSeverity(){
		DefectSeverity defectSeverity = new DefectSeverity();
		defectSeverity.setSeverityId(severityId);
		defectSeverity.setSeverityName(severityName);		
		return defectSeverity;
	}

	public Integer getSeverityId() {
		return severityId;
	}

	public void setSeverityId(Integer severityId) {
		this.severityId = severityId;
	}

	public String getSeverityName() {
		return severityName;
	}

	public void setSeverityName(String severityName) {
		this.severityName = severityName;
	}
	
}
