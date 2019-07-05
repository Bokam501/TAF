package com.hcl.atf.taf.jasper.reports.impl;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;

import com.hcl.atf.taf.jasper.reports.TestResultReports;
import com.hcl.atf.taf.service.TestRunReportsDeviceListService;


public class TestResultReportsimpl implements TestResultReports{
	private static final Log log = LogFactory.getLog(TestResultReportsimpl.class);
	Connection connection;
	Properties properties = new Properties();
	TestRunReportsDeviceListService testRunReportsDeviceListService;
	Map<String,Object> mapParameter = new HashMap<String,Object>();
	private SessionFactory sessionFactory;

	public JasperPrint generateTestRunListReport(Integer testRunNo,String reportMode,DataSource dataSource) throws Exception{
		JasperPrint jasperPrint=null;
		
		if (reportMode.equalsIgnoreCase("HTML")){
		}else if (reportMode.equalsIgnoreCase("PDF")){
			jasperPrint=generateTestRunPDFreport(testRunNo,dataSource);
		}else if (reportMode.equalsIgnoreCase("XLS")){
		}
		return jasperPrint;
	}
	
	public JasperPrint generateTestRunDeviceListReport(Integer testRunNo,String deviceId, String reportMode,DataSource dataSource) throws Exception{
		JasperPrint jasperPrint=null;
		
		
		if (reportMode.equalsIgnoreCase("HTML")){
		}else if (reportMode.equalsIgnoreCase("PDF")){
			jasperPrint=generateTestRunDevicePDFreport(testRunNo,deviceId,dataSource);
		}else if (reportMode.equalsIgnoreCase("XLS")){
		}
		return jasperPrint;
	}

	private JasperPrint generateTestRunPDFreport(Integer testRunNo,DataSource dataSource) throws Exception {
		
		String strSourceFile=null;
		String strReturnValue=null;
		String strSourceSubRepFile=null;
		String strSourceSubRepFilePath=null;
		JasperPrint jasperPrint=null;
		JasperReport jasperReport=null;
		JasperReport jasperSubReport=null;
		
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
	    Map<String,Object> mapParameter = new HashMap<String,Object>();
	    
	    mapParameter.put("ptestRunNo", testRunNo);
		try{
			strSourceFile=getFileDetails("TestRunJasperReport");
			strSourceSubRepFile=getFileDetails("TestRunJasperSubReport");
			
			strSourceFile=strSourceFile.replaceAll("/", "//");
			strSourceSubRepFile=strSourceSubRepFile.replaceAll("/", "//");
			strSourceSubRepFilePath=getCurrDir("TestRunJasperSubReport");
			strSourceSubRepFilePath=strSourceSubRepFilePath.replace("/","\\\\");
		
			connection=getConnection(dataSource);
			// jrxml compiling process
			jasperReport = JasperCompileManager.compileReport(strSourceFile);
				
			jasperSubReport = JasperCompileManager.compileReport(strSourceSubRepFile);
			
			mapParameter.put("subreportParameter", jasperSubReport);
			mapParameter.put("subreportdir",strSourceSubRepFilePath);
			jasperPrint = JasperFillManager.fillReport(jasperReport,mapParameter, connection);
			closeConnection();
		}catch(Exception exception){
			log.error("ERROR  ",exception);
		}
		return jasperPrint;
	}

	private JasperPrint generateTestRunDevicePDFreport(Integer testRunNo, String deviceId,DataSource dataSource) throws Exception {
		
		String strSourceFile=null;
		String strReturnValue=null;
		String strSourceSubRepFile=null;
		String strSourceSubRepFilePath=null;
		JasperPrint jasperPrint=null;
		JasperReport jasperReport=null;
		JasperReport jasperSubReport=null;
        Map<String,Object> mapParameter = new HashMap<String,Object>();
		mapParameter.put("ptestRunNo", testRunNo);
        mapParameter.put("pdeviceId", deviceId);
		try{
			
			strSourceFile=getFileDetails("TestRunDeviceJasperReport");//D:EclipseWorkspace_Project//TestAutomationFramework//TestAutomationFramework//WebContent//WEB-INF//resources//Reports//TestRunDeviceReport.jrxml
			strSourceSubRepFile=getFileDetails("TestRunDeviceJasperSubReport");
			strSourceSubRepFile=getFileDetails("TestRunDeviceJasperSubReport");
			
			strSourceSubRepFilePath=getCurrDir("TestRunDeviceJasperSubReport");
			strSourceSubRepFilePath=strSourceSubRepFilePath.replace("/","\\\\");
			
			strSourceFile=strSourceFile.replaceAll("/", "//");
			strSourceSubRepFile=strSourceSubRepFile.replaceAll("/", "//");
			
			
			
			
			connection=getConnection(dataSource);
			// jrxml compiling process
			jasperReport = JasperCompileManager.compileReport(strSourceFile);
			jasperSubReport = JasperCompileManager.compileReport(strSourceSubRepFile);
			
			mapParameter.put("subreportParameter", jasperSubReport);
			mapParameter.put("subreportdir", strSourceSubRepFilePath);
			// filling report with data from data source
			jasperPrint = JasperFillManager.fillReport(jasperReport,mapParameter, connection);
			closeConnection();	
		}catch(Exception exception){
		}
		return jasperPrint;
	}	
	
	private Connection getConnection(DataSource dataSource){
		try{
			connection=dataSource.getConnection();
	
		}catch(Exception exception){}
			return connection;
	}
	
	private Connection getConnectionProperties(){
		try{
			InputStream inDBConnection = getClass().getResourceAsStream("/com/TAFServer.properties");
			properties.load(inDBConnection);
		
			String strDBDriver=properties.getProperty("database.driver");
			String strURL=properties.getProperty("database.url");
			String strUser=properties.getProperty("database.user");
			String strPassword=properties.getProperty("database.password");
			
			Class.forName(strDBDriver);
			connection = DriverManager.getConnection(strURL+"?user="+strUser+"&password="+strPassword);
		}catch(Exception exception){
		}
		 return connection;
	}
	private void closeConnection(){
		try{
			if (!connection.isClosed()){
				connection.close();}
		}catch(Exception exception){
		}
	}
	private String getFileDetails(String strFileName){
		String strReturnVal=null;
		try{
			InputStream inReportProp = getClass().getResourceAsStream("/com/reportConfig.properties");
			properties.load(inReportProp);
			strReturnVal=properties.getProperty("report."+strFileName);
			
			String strCurrLoc=this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
			if (strCurrLoc.indexOf("WEB-INF")>0){
				strCurrLoc=strCurrLoc.substring(1,strCurrLoc.indexOf("WEB-INF"));
			}
			strReturnVal=strCurrLoc+strReturnVal;
		}catch(Exception exception){
		}
		 return strReturnVal;
	}

	private String getCurrDir(String strFileName){
		String strReturnVal=null;
		try{
			InputStream inReportProp = getClass().getResourceAsStream("/com/reportConfig.properties");
			properties.load(inReportProp);
			strReturnVal=properties.getProperty("report."+strFileName);
			
			String strCurrLoc=this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
			if (strCurrLoc.indexOf("WEB-INF")>0){
				strCurrLoc=strCurrLoc.substring(1,strCurrLoc.indexOf("WEB-INF"));
			}
			strReturnVal=strCurrLoc+strReturnVal;
			strReturnVal=strReturnVal.substring(0,strReturnVal.lastIndexOf('/')+1);
		}catch(Exception exception){
		}
		 return strReturnVal;
	}
	
}
