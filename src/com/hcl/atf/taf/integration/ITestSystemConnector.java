package com.hcl.atf.taf.integration;

import com.hcl.atf.taf.constants.IDPAConstants;

/**
 * 
 * @author rajeshbabus
 * This Interface specifies the methods a custom connector should implement
 * to be added to TAF
 */
public interface ITestSystemConnector {

	
	public static final String TEST_MANAGEMENT_SYSTEM = IDPAConstants.ENTITY_TEST_MANAGEMENT_SYSTEM;
	public static final String DEFECT_MANAGEMENT_SYSTEM = IDPAConstants.ENTITY_DEFECT_MANAGEMENT_SYSTEM;
	public static final String TEST_SCM_SYSTEM = IDPAConstants.ENTITY_SCM_MANAGEMENT_SYSTEM;
	
	/**
	 * Returns the name of the test system name
	 * For example, HPALM, TestLink, Zephyr, JIRA 
	 */
	public String getTestSystemName();
	
	/**
	 * Returns the version of the test system
	 * For example, 11, 12.2, 0.5 
	 */
	public String getTestSystemVersion();
	
	/**
	 * Returns the type of test system 
	 * This will be one of the constants specified in this interface
	 */
	public String getTestSystemType();
	
	/**
	 * Returns whether this connector is able to establish a connection to the specified test system
	 */
	public boolean isConnectionAvailable(String testSystemName, String testSystemVersion, String testSystemType);
	
	/**
	 * Returns whether this connector supports the specified system and version
	 */
	public boolean isSupportsSystem(String systemName, String systemVersion, String systemType);

	
	/**
	 * Import features into TAF
	 * The implementation should establish connection to the system and extract the data, convert it into TAF JSON format
	 * 
	 * @param systemConnectionDetailsJson. This provides the necessary details like URL, Username, Password, Project name etc
	 * @param queryJson. This provides data for filtering data. Typically this will have lastSycnTime
	 * @return Response json containing details of success and data
	 */
	public String importFeaturesIntoTAF(String systemConnectionDetailsJson, String queryJson);

	/**
	 * Import TestSuites into TAF
	 * The implementation should establish connection to the system and extract the data, convert it into TAF JSON format
	 * 
	 * @param systemConnectionDetailsJson. This provides the necessary details like URL, Username, Password, Project name etc
	 * @param queryJson. This provides data for filtering data. Typically this will have lastSycnTime
	 * @return Response json containing details of success and data
	 */
	public String importTestSuitesIntoTAF(String systemConnectionDetailsJson, String queryJson);
	
	/**
	 * Import Testcases into TAF
	 * The implementation should establish connection to the system and extract the data, convert it into TAF JSON format
	 * 
	 * @param systemConnectionDetailsJson. This provides the necessary details like URL, Username, Password, Project name etc
	 * @param queryJson. This provides data for filtering data. Typically this will have lastSycnTime
	 * @return Response json containing details of success and data
	 */
	public String importTestcasesIntoTAF(String systemConnectionDetailsJson, String queryJson);
	
	/**
	 * Import Feature to Testcase mapppings
	 * The implementation should establish connection to the system and extract the data, convert it into TAF JSON format
	 * 
	 * @param systemConnectionDetailsJson. This provides the necessary details like URL, Username, Password, Project name etc
	 * @param queryJson. This provides data for filtering data. Typically this will have lastSycnTime
	 * @return Response json containing details of success and data
	 */
	public String importFeatureToTestcaseMappings(String systemConnectionDetailsJson, String queryJson);

	/**
	 * Import Testcase to Testsuite mapppings
	 * The implementation should establish connection to the system and extract the data, convert it into TAF JSON format
	 * 
	 * @param systemConnectionDetailsJson. This provides the necessary details like URL, Username, Password, Project name etc
	 * @param queryJson. This provides data for filtering data. Typically this will have lastSycnTime
	 * @return Response json containing details of success and data
	 */
	public String importTestcaseToTestSuiteMappings(String systemConnectionDetailsJson, String queryJson);
	
	/**
	 * Report defects to Test system
	 * The implementation should establish connection to the system and report the defects to the test system
	 * 
	 * @param systemConnectionDetailsJson. This provides the necessary details like URL, Username, Password, Project name etc
	 * @param queryJson. This provides data for filtering data. Typically this will have lastSycnTime
	 * @return Response json containing details of success and data.. Data should contain the test system ID for the defects reported
	 */
	public String reportDefectsToSystem(String systemConnectionDetailsJson, String defectsJson);
	
	/**
	 * Report test execution results to Test system
	 * The implementation should establish connection to the system and report the defects to the test system
	 * 
	 * @param systemConnectionDetailsJson. This provides the necessary details like URL, Username, Password, Project name etc
	 * @param queryJson. This provides data for filtering data. Typically this will have lastSycnTime
	 * @return Response json containing details of success and data.. Data should contain the test system IDs for the workpackage, job, testcases, test steps reported
	 */
	public String reportTestExecutionResultsToSystem(String systemConnectionDetailsJson, String workpackageExecutionsJson);
}
