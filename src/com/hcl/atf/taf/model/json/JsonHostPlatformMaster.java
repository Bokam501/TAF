package com.hcl.atf.taf.model.json;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.HostPlatformMaster;


public class JsonHostPlatformMaster implements java.io.Serializable {
	@JsonProperty
	private String hostPlatform;
	
	public JsonHostPlatformMaster() {
	}

	public JsonHostPlatformMaster(HostPlatformMaster hostPlatformMaster) {
		this.hostPlatform = hostPlatformMaster.getHostPlatform();
	}

		public String getHostPlatform() {
		return this.hostPlatform;
	}

	public void setHostPlatform(String hostPlatform) {
		this.hostPlatform = hostPlatform;
	}
	
	@JsonIgnore
	public HostPlatformMaster getHostPlatformMaster(){
		HostPlatformMaster hostPlatformMaster = new HostPlatformMaster();
		hostPlatformMaster.setHostPlatform(hostPlatform);
				
		return hostPlatformMaster;
	}

}
