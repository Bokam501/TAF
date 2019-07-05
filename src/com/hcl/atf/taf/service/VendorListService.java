package com.hcl.atf.taf.service;

import java.util.List;

import com.hcl.atf.taf.model.VendorMaster;

public interface VendorListService {

	public List<VendorMaster> getVendorMasterList();

	public void addVendorMaster(VendorMaster vendor);

	public List<VendorMaster> getVendorListByResourcePoolId(Integer resourcePoolId);

	public VendorMaster getVendorById(Integer vendorId);

	public void updateVendorMaster(VendorMaster vendorFromUI);

	public List<VendorMaster> getVendorMasterList(Integer jtStartIndex, Integer jtPageSize);
	boolean isVendorExistingByregisteredCompanyName(String registeredCompanyName);
	boolean isVendorExistingByregisteredCompanyNameForUpdate(String registeredCompanyName, int vendorId);
	VendorMaster getVendorByregisteredCompanyName(String registeredCompanyName);
}
