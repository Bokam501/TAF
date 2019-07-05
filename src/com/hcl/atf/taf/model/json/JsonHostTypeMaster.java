package com.hcl.atf.taf.model.json;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.HostTypeMaster;

public class JsonHostTypeMaster implements java.io.Serializable {
	@JsonProperty
	private String hostType;
	
	public JsonHostTypeMaster() {
	}

	
	public JsonHostTypeMaster(HostTypeMaster hostTypeMaster) {
		this.hostType = hostTypeMaster.getHostType();
		
	}
	public String getHostType() {
		return this.hostType;
	}

	public void setHostType(String hostType) {
		this.hostType = hostType;
	}

	@JsonIgnore
	public HostTypeMaster getHostTypeMaster(){
		HostTypeMaster hostTypeMaster = new HostTypeMaster();
		hostTypeMaster.setHostType(hostType);
				
		return hostTypeMaster;
	}

}
