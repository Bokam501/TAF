package com.hcl.atf.taf.service;

public interface LicenseCheckService {
	String licenseAgrementValidation(String filePath);
	String getLicenseExpiryData(String filePath);
}
