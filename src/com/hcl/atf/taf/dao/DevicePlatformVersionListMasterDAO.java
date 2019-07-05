package com.hcl.atf.taf.dao;

import java.util.List;

import com.hcl.atf.taf.model.DevicePlatformVersionListMaster;

public interface DevicePlatformVersionListMasterDAO  {	 

	List<DevicePlatformVersionListMaster> list();
	List<DevicePlatformVersionListMaster> list(String devicePlatform);
	
	DevicePlatformVersionListMaster getDevicePlatformVersionByVersionCode(String platform,String code);
	void add(DevicePlatformVersionListMaster devicePlatformVersionListMaster);
	DevicePlatformVersionListMaster getDevicePlatformVersionById(Integer devicePlatformVersionListId);
}
