package com.hcl.atf.taf.dao;

import java.util.List;

import com.hcl.atf.taf.model.VendorMaster;

public interface VendorListDao {

public	List<VendorMaster> getVendorMasterList();

public void addVendorMaster(VendorMaster vendor);

public VendorMaster getVendorById(Integer vendorId);

public void updateVendorMaster(VendorMaster vendorFromUI);

public List<VendorMaster> getVendorListByResourcePoolId(Integer resourcePoolId);

public List<VendorMaster> getVendorMasterList(Integer jtStartIndex, Integer jtPageSize);
	
boolean isVendorExistingByregisteredCompanyName(String registeredCompanyName);
boolean isVendorExistingByregisteredCompanyNameForUpdate(String registeredCompanyName, int vendorId);
VendorMaster getVendorByregisteredCompanyName(String registeredCompanyName);

}
