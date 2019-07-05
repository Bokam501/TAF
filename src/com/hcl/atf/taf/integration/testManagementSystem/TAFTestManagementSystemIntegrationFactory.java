package com.hcl.atf.taf.integration.testManagementSystem;

import com.hcl.atf.taf.integration.testManagementSystem.hpqc.HPQCIntegrator;



public class TAFTestManagementSystemIntegrationFactory {

	public static final String HPQC = "HPQC";
	
	public static TAFTestManagementSystemIntegrator getTestManagementSystemIntegrator(String testSystemName) {
	
		if (HPQC.equalsIgnoreCase(testSystemName))
			return new HPQCIntegrator();
		else 
			return null;
		
	}
}
