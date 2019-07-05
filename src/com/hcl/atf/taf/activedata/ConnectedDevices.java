package com.hcl.atf.taf.activedata;


public class ConnectedDevices{
	
	private Integer deviceListId;

	private String deviceId;	
	
	
	public ConnectedDevices(Integer deviceListId,String  deviceId) {
		this.deviceId=deviceId;
		this.deviceListId=deviceListId;
	}	
	
	
	
	public Integer getDeviceListId() {
		return deviceListId;
	}

	
	public String getDeviceId() {
		return deviceId;
	}

	
	
	
	@Override
	public boolean equals(Object o) {
		
		return getDeviceListId().equals(((ConnectedDevices)o).getDeviceListId());
	}
	
}
