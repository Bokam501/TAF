package com.hcl.atf.taf.integration.defectManagementSystem;

import com.hcl.atf.taf.integration.defectManagementSystem.jira.JiraIntegrator;


public class TAFDefectManagementSystemIntegrationFactory {

	public static final String JIRA = "JIRA";
	public static final String BUGZILLA = "BUGZILLA";
	
	public static TAFDefectManagementSystemIntegrator getDefectManagementSystemIntegrator(String defectSystemName) {
	
		if (JIRA.equals(defectSystemName))
			return new JiraIntegrator();
		else 
			return null;
		}
}
