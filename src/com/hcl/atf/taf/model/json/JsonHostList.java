package com.hcl.atf.taf.model.json;



import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.CommonActiveStatusMaster;
import com.hcl.atf.taf.model.HostList;
import com.hcl.atf.taf.model.HostPlatformMaster;
import com.hcl.atf.taf.model.HostTypeMaster;
public class JsonHostList implements java.io.Serializable {

	@JsonProperty
	private Integer hostId;
	
	//ref to CommmonActiveStatusMaster.status
	@JsonProperty
	private String hostStatus;
	
	//ref to HostTypeMaster.hostType
	@JsonProperty	
	private String hostType;
	
	//ref to HotPlatformMaster.hotPlatform
	@JsonProperty	
	private String hostPlatform;
	
	@JsonProperty
	private String hostName;
	
	@JsonProperty
	private String hostIpAddress;
	@JsonProperty
	private Integer isSelected;

	public JsonHostList() {
	}

	public JsonHostList(HostList hostList) {
		this.hostId=hostList.getHostId();
		if(hostList.getCommonActiveStatusMaster()!=null)
		this.hostStatus=hostList.getCommonActiveStatusMaster().getStatus();
		if(hostList.getHostTypeMaster()!=null)
		this.hostType=hostList.getHostTypeMaster().getHostType();
		if(hostList.getHostPlatformMaster()!=null)
		this.hostPlatform=hostList.getHostPlatformMaster().getHostPlatform();
		this.hostName = hostList.getHostName();
		this.hostIpAddress = hostList.getHostIpAddress();
	}

	public Integer getHostId() {
		return hostId;
	}

	public void setHostId(Integer hostId) {
		this.hostId = hostId;
	}

	public String getHostStatus() {
		return hostStatus;
	}

	public void setHostStatus(String hostStatus) {
		this.hostStatus = hostStatus;
	}

	public String getHostType() {
		return hostType;
	}

	public void setHostType(String hostType) {
		this.hostType = hostType;
	}

	public String getHostPlatform() {
		return hostPlatform;
	}

	public void setHostPlatform(String hostPlatform) {
		this.hostPlatform = hostPlatform;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getHostIpAddress() {
		return hostIpAddress;
	}

	public void setHostIpAddress(String hostIpAddress) {
		this.hostIpAddress = hostIpAddress;
	}
	
	@JsonIgnore
	public HostList getHostList(){
		HostList hostList = new HostList();
		hostList.setHostId(hostId);
		hostList.setHostName(hostName);
		hostList.setHostIpAddress(hostIpAddress);
		
		HostPlatformMaster hostPlatformMaster = new HostPlatformMaster();
		hostPlatformMaster.setHostPlatform(hostPlatform);
		hostList.setHostPlatformMaster(hostPlatformMaster);
		
		HostTypeMaster hostTypeMaster = new HostTypeMaster();
		hostTypeMaster.setHostType(hostType);
		hostList.setHostTypeMaster(hostTypeMaster);
		
		CommonActiveStatusMaster hostStatusMaster = new CommonActiveStatusMaster();
		hostStatusMaster.setStatus(hostStatus);
		hostList.setCommonActiveStatusMaster(hostStatusMaster);
		
		return hostList;
	}

	public Integer getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(Integer isSelected) {
		this.isSelected = isSelected;
	}

}
