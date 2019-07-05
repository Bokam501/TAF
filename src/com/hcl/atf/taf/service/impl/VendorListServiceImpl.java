package com.hcl.atf.taf.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.VendorListDao;
import com.hcl.atf.taf.model.VendorMaster;
import com.hcl.atf.taf.service.VendorListService;

@Service
public class VendorListServiceImpl implements VendorListService {
	
	@Autowired
	private VendorListDao vendorListDao;

	@Override
	@Transactional
	public List<VendorMaster> getVendorMasterList() {
		return vendorListDao.getVendorMasterList();
	}

	@Override
	@Transactional
	public void addVendorMaster(VendorMaster vendor) {
		vendorListDao.addVendorMaster(vendor);
	}

	@Override
	@Transactional
	public VendorMaster getVendorById(Integer vendorId) {
		return vendorListDao.getVendorById(vendorId);
	}

	@Override
	@Transactional
	public void updateVendorMaster(VendorMaster vendorFromUI) {
		vendorListDao.updateVendorMaster(vendorFromUI);
	}

	@Override
	@Transactional
	public List<VendorMaster> getVendorListByResourcePoolId(Integer resourcePoolId) {
		return vendorListDao.getVendorListByResourcePoolId(resourcePoolId);
	}

	@Override
	public List<VendorMaster> getVendorMasterList(Integer jtStartIndex, Integer jtPageSize) {
		return vendorListDao.getVendorMasterList(jtStartIndex, jtPageSize);
	}

	@Override
	@Transactional
	public boolean isVendorExistingByregisteredCompanyName(
			String registeredCompanyName) {
		return vendorListDao.isVendorExistingByregisteredCompanyName(registeredCompanyName);
	}

	@Override
	@Transactional
	public VendorMaster getVendorByregisteredCompanyName(
			String registeredCompanyName) {
		return vendorListDao.getVendorByregisteredCompanyName(registeredCompanyName);
	}

	@Override
	@Transactional
	public boolean isVendorExistingByregisteredCompanyNameForUpdate(
			String registeredCompanyName, int vendorId) {
		return vendorListDao.isVendorExistingByregisteredCompanyNameForUpdate(registeredCompanyName, vendorId);
	}

	
}
