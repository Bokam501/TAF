package com.hcl.atf.taf.integration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class TAFTestSystemConnectorsFactory {

	private static final Log log = LogFactory.getLog(TAFTestSystemConnectorsFactory.class);
	
	public static ITestSystemConnector getTestSystemConnector(String testSystemName, String testSystemVersion, String testSystemType,String customConnectorClassName) {
	
		try {
			ITestSystemConnector customConnector = (ITestSystemConnector) Class.forName(customConnectorClassName).newInstance();
			if (customConnector == null) {
				log.info("No custom connectors found for " + " : " + testSystemName + " : " + testSystemVersion + " : " + testSystemType);
				return null;
			}
			if (customConnector.isSupportsSystem(testSystemName, testSystemVersion, testSystemType)) {
				return customConnector;
			} else {
				log.info("No active connector is found for Test/Defect System" + " : " + testSystemName + " : " + testSystemVersion + " : " + testSystemType);
				return null;
			}
		} catch (Exception c) {
			log.error("Unable to load custom connector class " + customConnectorClassName, c);
			return null;
		}
	}
}
