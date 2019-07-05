package com.hcl.atf.taf.report.jasper;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.sql.DataSource;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.dao.WorkPackageDAO;
import com.hcl.atf.taf.model.ProductType;
import com.hcl.atf.taf.report.Report;
import com.hcl.atf.taf.service.TestRunReportsDeviceListService;


@Service
public class JasperReportImpl implements Report{
	private static final Log log = LogFactory.getLog(JasperReportImpl.class);
	
	@Autowired
	DataSource dataSource;
	
	@Autowired
	WorkPackageDAO workPackageDAO;
	
	@Value("#{ilcmProps['ENABLING_TAB_PROPERTY']}")
    private String riskTabEnabled;

	Connection connection;
	Properties properties = new Properties();
	TestRunReportsDeviceListService testRunReportsDeviceListService;
	Map<String,Object> mapParameter = new HashMap<String,Object>();
	private SessionFactory sessionFactory;
	
@Override
	public JasperPrint generateTestRunListReport(Integer testRunNo,Integer testRunConfigurationChildId,String reportMode,ProductType productTypeMaster,BufferedImage logo, String loginUserName) {
		JasperPrint jasperPrint=null;
		
		if (reportMode.equalsIgnoreCase("HTML")){
			jasperPrint=generateTestRunPDFreport(testRunNo,testRunConfigurationChildId,productTypeMaster,logo, loginUserName);
		}else if (reportMode.equalsIgnoreCase("PDF")){
				jasperPrint=generateTestRunPDFreport(testRunNo,testRunConfigurationChildId,productTypeMaster,logo, loginUserName);
			
			
		}else if (reportMode.equalsIgnoreCase("XLS")){
			jasperPrint=generateTestRunXLSreport(testRunNo,testRunConfigurationChildId);
		}
		return jasperPrint;
	}

@Override
public JasperPrint generateTestRunListRCReport(Integer testRunNo,Integer testRunConfigurationChildId,String reportMode,ProductType productTypeMaster,BufferedImage logo, String loginUserName) {
	JasperPrint jasperPrint=null;
	
	if (reportMode.equalsIgnoreCase("HTML")){
		jasperPrint=generateTestRunPDFRCreport(testRunNo,testRunConfigurationChildId,productTypeMaster,logo, loginUserName);
	}else if (reportMode.equalsIgnoreCase("PDF")){
			jasperPrint=generateTestRunPDFRCreport(testRunNo,testRunConfigurationChildId,productTypeMaster,logo, loginUserName);
		
		
	}else if (reportMode.equalsIgnoreCase("XLS")){
		jasperPrint=generateTestRunXLSreport(testRunNo,testRunConfigurationChildId);
	}
	return jasperPrint;
}

@Override
	public List<JasperPrint> generateTestRunReport(Map<String,Object> pInput) {
		List<JasperPrint> lJasperPrint=null;
		String reportMode=(String) pInput.get("printMode");
		
		if (reportMode.equalsIgnoreCase("HTML")){
		}else if (reportMode.equalsIgnoreCase("PDF")){
		}else if (reportMode.equalsIgnoreCase("XLS")){
			lJasperPrint=generateTestRunXLSReport(pInput);
		}
		return lJasperPrint;
	}
	
@Override
	public List<JasperPrint> generateTestRunDeviceReport(Map<String,Object> pInput) {
		List<JasperPrint> lJasperPrint=null;
		String reportMode=(String) pInput.get("printMode");
		
		if (reportMode.equalsIgnoreCase("HTML")){
		}else if (reportMode.equalsIgnoreCase("PDF")){
		}else if (reportMode.equalsIgnoreCase("XLS")){
			lJasperPrint=generateTestRunDeviceXLSReport(pInput);
		}
		return lJasperPrint;
	}

	public JasperPrint generateTestRunDeviceListReport(Integer testRunNo,Integer testRunListId,String deviceId,Integer testRunConfigurationChildId,String reportMode,BufferedImage logo, String loginUserName, String reportType){
		JasperPrint jasperPrint=null;
		
		
		if (reportMode.equalsIgnoreCase("HTML")){
			jasperPrint=generateTestRunDevicePDFreport(testRunNo,testRunListId,deviceId,testRunConfigurationChildId,logo, loginUserName,reportType);
		}else if (reportMode.equalsIgnoreCase("PDF")){
			jasperPrint=generateTestRunDevicePDFreport(testRunNo,testRunListId,deviceId,testRunConfigurationChildId,logo, loginUserName,reportType);
		}else if (reportMode.equalsIgnoreCase("XLS")){
			jasperPrint=generateTestRunDeviceXLSreport(testRunNo,deviceId,testRunConfigurationChildId);
			
		}
		return jasperPrint;
	}
	private JasperPrint generateTestRunPDFreport(Integer testRunNo,Integer testRunConfigurationChildId,ProductType productTypeMaster,BufferedImage logo, String loginUserName)  {
		
		String strSourceFile=null;
		String strReturnValue=null;
		String strSourceSubRepFile=null;
		String strSourceSubRep1File=null;
		String strSourceSubRep2File=null;
		String strSourceSubRep3File=null; 
		String strSourceSubRepFilePath=null;
		JasperPrint jasperPrint=null;
		JasperReport jasperReport=null;
		JasperReport jasperSubReport=null;
		JasperReport jasperSubReport1=null;
		JasperReport jasperSubReport2=null;
		JasperReport jasperSubReport3=null;
		
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
	    Map<String,Object> mapParameter = new HashMap<String,Object>();
	    //
	    mapParameter.put("ptestRunNo", testRunNo);
	    mapParameter.put("logo", logo);
	    mapParameter.put("loginUser", loginUserName);
	    mapParameter.put("riskTabEnabled", riskTabEnabled);
	    if(testRunConfigurationChildId!=null){
	    	log.info("ptestRunConfigurationChildId"+testRunConfigurationChildId);
	    	 mapParameter.put("ptestRunConfigurationChildId", testRunConfigurationChildId);
	    }
	   
		try{
			String repDir=getReportDir();
			
			if(productTypeMaster.getTypeName().equalsIgnoreCase(IDPAConstants.APP_MOBILE) || productTypeMaster.getTypeName().equalsIgnoreCase(IDPAConstants.APP_DEVICE)){
				strSourceFile=repDir+"TestRunReport.jrxml";
				strSourceSubRepFile=repDir+"TestRunReport_JobSummaryReport_Ver2.jrxml";	
				strSourceSubRep1File=repDir+"TestRunReport_JobSummaryDetail.jrxml";
				strSourceSubRep2File=repDir+"TestRunReport_TestCasesummarysubreport_Ver2.jrxml";	
				strSourceSubRep3File=repDir+"TestRunDeviceReport_TestCaseDetailsSubreport_Ver3.jrxml";
				connection=getConnection();
				// jrxml compiling process
				jasperReport = JasperCompileManager.compileReport(strSourceFile);
				jasperSubReport = JasperCompileManager.compileReport(strSourceSubRepFile);
				jasperSubReport1 = JasperCompileManager.compileReport(strSourceSubRep1File);
				jasperSubReport2 = JasperCompileManager.compileReport(strSourceSubRep2File);
				jasperSubReport3 = JasperCompileManager.compileReport(strSourceSubRep3File);
				mapParameter.put("subreportParameter", jasperSubReport);
				mapParameter.put("subreportParameter1", jasperSubReport1);
				mapParameter.put("subreportParameter2", jasperSubReport2);
				mapParameter.put("subreportParameter3", jasperSubReport3);
				mapParameter.put("subreportdir",repDir);
				jasperPrint = JasperFillManager.fillReport(jasperReport,mapParameter, connection);
				closeConnection();			
				log.info("strSourceFile==>"+strSourceFile);
				log.info(strSourceSubRepFile);
				log.info(strSourceSubRep1File);
				log.info(repDir);
			}
			else{
				strSourceFile=repDir+"TestRunReport.jrxml";
				strSourceSubRepFile=repDir+"TestRunReport_JobSummaryReport_Ver2.jrxml";	
				strSourceSubRep1File=repDir+"TestRunReport_JobSummaryDetail.jrxml";
				strSourceSubRep2File=repDir+"TestRunReport_TestCasesummarysubreport_Ver2.jrxml";	
				strSourceSubRep3File=repDir+"TestRunDeviceReport_TestCaseDetailsSubreport_Ver3.jrxml";
				connection=getConnection();
				// jrxml compiling process
				jasperReport = JasperCompileManager.compileReport(strSourceFile);
				jasperSubReport = JasperCompileManager.compileReport(strSourceSubRepFile);
				jasperSubReport1 = JasperCompileManager.compileReport(strSourceSubRep1File);
				jasperSubReport2 = JasperCompileManager.compileReport(strSourceSubRep2File);
				jasperSubReport3 = JasperCompileManager.compileReport(strSourceSubRep3File);
				mapParameter.put("subreportParameter", jasperSubReport);
				mapParameter.put("subreportParameter1", jasperSubReport1);
				mapParameter.put("subreportParameter2", jasperSubReport2);
				mapParameter.put("subreportParameter3", jasperSubReport3);
				mapParameter.put("subreportdir",repDir);
				jasperPrint = JasperFillManager.fillReport(jasperReport,mapParameter, connection);
				closeConnection();			
				log.info("strSourceFile==>"+strSourceFile);
				log.info(strSourceSubRepFile);
				log.info(strSourceSubRep1File);
				log.info(repDir);
			}
			
		}catch(Exception exception){
			log.error(exception);
			
		}
		return jasperPrint;
	}

	private JasperPrint generateTestRunDevicePDFreport(Integer testRunNo,Integer testRunListId, String deviceId,Integer testRunConfigurationChildId, BufferedImage logo, String loginUserName, String reportType)  {
		
		String strSourceFile=null;
		String strReturnValue=null;
		String strSourceSubRepFile=null;
		String strSourceSubRepFilePath=null;
		String strSourceSubRep1File = null;			
		JasperPrint jasperPrint=null;
		JasperReport jasperReport=null;
		JasperReport jasperSubReport=null;
		JasperReport jasperSubReport1=null;
        Map<String,Object> mapParameter = new HashMap<String,Object>();
        mapParameter.put("loginUser", loginUserName);
		mapParameter.put("ptestRunNo", testRunNo);
		mapParameter.put("ptestRunListId", testRunListId);
		mapParameter.put("pdeviceId", deviceId);//deviceId=UDID 
        mapParameter.put("logo", logo);
        mapParameter.put("ptestRunConfigurationChildId", testRunConfigurationChildId);
        mapParameter.put("riskTabEnabled", riskTabEnabled);
		try{
			if(reportType != null && reportType.equalsIgnoreCase("screenshot"))
			{
				String strCurrLoc=this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
				String repDir=getReportDir();
				strSourceFile=repDir+"TestRunDeviceReport_Ver3.jrxml";			
				strSourceSubRep1File=repDir+"TestRunReport_TestCasesummarysubreport_Ver3.jrxml";
				strSourceSubRepFile=repDir+"TestRunDeviceReport_TestCaseDetailsSubreport_Ver4.jrxml";
				log.info("strSourceFile==>"+strSourceFile);
				log.info(strSourceFile);
				log.info(strSourceSubRepFile);
				connection=getConnection();
				// jrxml compiling process
				jasperReport = JasperCompileManager.compileReport(strSourceFile);
				jasperSubReport1 = JasperCompileManager.compileReport(strSourceSubRep1File);
				jasperSubReport = JasperCompileManager.compileReport(strSourceSubRepFile);

				
				mapParameter.put("subreportParameter", jasperSubReport);
				mapParameter.put("subreportParameter1", jasperSubReport1);
				mapParameter.put("subreportdir", repDir);
				
				// filling report with data from data source
				jasperPrint = JasperFillManager.fillReport(jasperReport,mapParameter, connection);

			}
			else if(reportType != null && reportType.equalsIgnoreCase("Noscreenshot"))
			{
				
				String strCurrLoc=this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
				String repDir=getReportDir();
				strSourceFile=repDir+"TestRunDeviceReport_NoScreenshot_Ver2.jrxml";			
				strSourceSubRep1File=repDir+"TestRunReport_TestCasesummarysubreport_Ver2.jrxml";
				strSourceSubRepFile=repDir+"TestRunDeviceReport_TestCaseDetailsSubreport_NoScreenshot_Ver3.jrxml";
				log.info("strSourceFile==>"+strSourceFile);
				log.info(strSourceFile);
				log.info(strSourceSubRepFile);
				connection=getConnection();
				// jrxml compiling process
				jasperReport = JasperCompileManager.compileReport(strSourceFile);
				jasperSubReport1 = JasperCompileManager.compileReport(strSourceSubRep1File);
				jasperSubReport = JasperCompileManager.compileReport(strSourceSubRepFile);

				
				mapParameter.put("subreportParameter", jasperSubReport);
				mapParameter.put("subreportParameter1", jasperSubReport1);
				mapParameter.put("subreportdir", repDir);
				
				// filling report with data from data source
				jasperPrint = JasperFillManager.fillReport(jasperReport,mapParameter, connection);
							
			}
								
			else
			{	
			String strCurrLoc=this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
			String repDir=getReportDir();
			strSourceFile=repDir+"TestRunDeviceReport_Ver2.jrxml";//Modified for Abbott new Requirements in reports			
			strSourceSubRep1File=repDir+"TestRunReport_TestCasesummarysubreport_Ver2.jrxml";//Modified for Abbott new Requirements in reports
			strSourceSubRepFile=repDir+"TestRunDeviceReport_TestCaseDetailsSubreport_Ver3.jrxml";//Modified for Abbott new Requirements in reports
			log.info("strSourceFile==>"+strSourceFile);
			log.info(strSourceFile);
			log.info(strSourceSubRepFile);
			connection=getConnection();
			// jrxml compiling process
			jasperReport = JasperCompileManager.compileReport(strSourceFile);
			jasperSubReport1 = JasperCompileManager.compileReport(strSourceSubRep1File);
			jasperSubReport = JasperCompileManager.compileReport(strSourceSubRepFile);

			
			mapParameter.put("subreportParameter", jasperSubReport);
			mapParameter.put("subreportParameter1", jasperSubReport1);
			mapParameter.put("subreportdir", repDir);
			
			// filling report with data from data source
			jasperPrint = JasperFillManager.fillReport(jasperReport,mapParameter, connection);
			}
		}catch(Exception exception){
			log.error(exception);
		
		}
		return jasperPrint;
	}
	
private JasperPrint generateTestRunPDFRCreport(Integer testRunNo,Integer testRunConfigurationChildId,ProductType productTypeMaster,BufferedImage logo, String loginUserName)  {
		
		String strSourceFile=null;
		String strReturnValue=null;
		String strSourceSubRepFile=null;
		String strSourceSubRep1File=null;
		String strSourceSubRepFilePath=null;
		JasperPrint jasperPrint=null;
		JasperReport jasperReport=null;
		JasperReport jasperSubReport=null;
		JasperReport jasperSubReport1=null;
				
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
	    Map<String,Object> mapParameter = new HashMap<String,Object>();
	    //
	    mapParameter.put("ptestRunNo", testRunNo);
	    mapParameter.put("logo", logo);
	    mapParameter.put("loginUser", loginUserName);
	    mapParameter.put("riskTabEnabled", riskTabEnabled);
	    if(testRunConfigurationChildId!=null){
	    	log.info("ptestRunConfigurationChildId"+testRunConfigurationChildId);
	    	 mapParameter.put("ptestRunConfigurationChildId", testRunConfigurationChildId);
	    }
	   
		try{
			String repDir=getReportDir();
				strSourceFile=repDir+"TestRunReportRC.jrxml";
				strSourceSubRepFile=repDir+"TestRunReport_TestCasesummarysubreportRC_Ver2.jrxml";	
				strSourceSubRep1File=repDir+"TestRunDeviceReport_TestCaseDetailsSubreportRC_Ver3.jrxml";
				connection=getConnection();
				// jrxml compiling process
				jasperReport = JasperCompileManager.compileReport(strSourceFile);
				jasperSubReport = JasperCompileManager.compileReport(strSourceSubRepFile);
				jasperSubReport1 = JasperCompileManager.compileReport(strSourceSubRep1File);
				mapParameter.put("subreportParameter", jasperSubReport);
				mapParameter.put("subreportParameter1", jasperSubReport1);
				mapParameter.put("subreportdir",repDir);
				jasperPrint = JasperFillManager.fillReport(jasperReport,mapParameter, connection);
				closeConnection();			
				log.info("strSourceFile==>"+strSourceFile);
				log.info(strSourceSubRepFile);
				log.info(strSourceSubRep1File);
				log.info(repDir);
			}
			
			catch(Exception exception){
			log.error(exception);
			
		}
		return jasperPrint;
	}
		
	private JasperPrint generateTestRunXLSreport(Integer testRunNo,Integer testRunConfigurationChildId)  {
		
		String strSourceFile=null;
		String strReturnValue=null;
		String strSourceSubRepFile=null;
		String strSourceSubRep1File=null;
		String strSourceSubRepFilePath=null;
		JasperPrint jasperPrint=null;
		JasperReport jasperReport=null;
		JasperReport jasperSubReport=null;
		JasperReport jasperSubReport1=null;
		
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
	    Map<String,Object> mapParameter = new HashMap<String,Object>();
	    //
	    mapParameter.put("ptestRunNo", testRunNo);
	    mapParameter.put("ptestRunConfigurationChildId", testRunConfigurationChildId);
		try{
			
			String repDir=getReportDir();
			strSourceFile=repDir+"TestRunReport_XLS.jrxml";
			strSourceSubRepFile=repDir+"TestRunReport_TestCasesubreport.jrxml";	
			strSourceSubRep1File=repDir+"TestRunReport_TestCasesummarysubreport.jrxml";
			log.info(strSourceFile);
			log.info(strSourceSubRepFile);
			log.info(strSourceSubRep1File);
			log.info(repDir);
			//
			connection=getConnection();
			// jrxml compiling process
			jasperReport = JasperCompileManager.compileReport(strSourceFile);
				
			jasperSubReport = JasperCompileManager.compileReport(strSourceSubRepFile);
			
			jasperSubReport1 = JasperCompileManager.compileReport(strSourceSubRep1File);
			
			mapParameter.put("subreportParameter", jasperSubReport);
			mapParameter.put("subreportParameter1", jasperSubReport1);
			mapParameter.put("subreportdir",repDir);
			jasperPrint = JasperFillManager.fillReport(jasperReport,mapParameter, connection);
			closeConnection();
		}catch(Exception exception){
			log.error(exception);
		}
		return jasperPrint;
	}
	
	private List<JasperPrint> generateTestRunXLSReport(Map<String,Object> pInput)  {
		
		String strSourceFile=null;
		JasperPrint jasperPrint=null;
		List<JasperPrint> lJasperPrintlist=new ArrayList<JasperPrint>();
	    Map<String,Object> mapParameter = new HashMap<String,Object>();
	
	    Integer testRunNo=(Integer)pInput.get("testRunNo");
	    Integer testRunListId=(Integer)pInput.get("testRunListId");
	    Integer testRunConfigurationChildId=(Integer)pInput.get("testRunConfigurationChildId");
	    String deviceId=(String)pInput.get("deviceId");
	    
	    String repDir=getReportDir();
		
		try{
	    	mapParameter.put("ptestRunNo", testRunNo);
		    mapParameter.put("ptestRunConfigurationChildId", testRunConfigurationChildId);
		    mapParameter.put("ptestRunListId", testRunListId);
	    	mapParameter.put("pdeviceId", deviceId);
		    mapParameter.put("subreportdir",repDir);
		    
		    strSourceFile=repDir+"TestRunReport_Summary_XLS.jrxml";
	    	jasperPrint=getJasperPrint(strSourceFile,mapParameter);
			lJasperPrintlist.add(jasperPrint);
			
			jasperPrint=null;
			strSourceFile=repDir+"TestRunReport_TestCasesubreport_XLS.jrxml";
	    	jasperPrint=getJasperPrint(strSourceFile,mapParameter);
			lJasperPrintlist.add(jasperPrint);
			
			jasperPrint=null;
			strSourceFile=repDir+"TestRunReport_TestCasesummarysubreport_XLS.jrxml";
	    	jasperPrint=getJasperPrint(strSourceFile,mapParameter);
			lJasperPrintlist.add(jasperPrint);
			
		}catch(Exception exception){
			log.error(exception);
		}
		return lJasperPrintlist;
	}
	
	private List<JasperPrint> generateTestRunDeviceXLSReport(Map<String,Object> pInput)  {
		
		String strSourceFile=null;
		JasperPrint jasperPrint=null;
		List<JasperPrint> lJasperPrintlist=new ArrayList<JasperPrint>();
	    Map<String,Object> mapParameter = new HashMap<String,Object>();
	
	    Integer testRunNo=(Integer)pInput.get("testRunNo");
	    Integer testRunListId=(Integer)pInput.get("testRunListId");
	    Integer testRunConfigurationChildId=(Integer)pInput.get("testRunConfigurationChildId");
	    String deviceId=(String)pInput.get("deviceId");
	    
	    String repDir=getReportDir();
		
		try{
			mapParameter.put("ptestRunNo", testRunNo);
		    mapParameter.put("ptestRunConfigurationChildId", testRunConfigurationChildId);
		    mapParameter.put("ptestRunListId", testRunListId);
	    	mapParameter.put("pdeviceId", deviceId);
		    mapParameter.put("subreportdir",repDir);
		   
		    strSourceFile=repDir+"TestRunDeviceReport_Summary_XLS.jrxml";
	    	jasperPrint=getJasperPrint(strSourceFile,mapParameter);
			lJasperPrintlist.add(jasperPrint);
			
			jasperPrint=null;
			strSourceFile=repDir+"TestRunDeviceReport_TestCasesubreport_XLS.jrxml";
	    	jasperPrint=getJasperPrint(strSourceFile,mapParameter);
			lJasperPrintlist.add(jasperPrint);
			
			jasperPrint=null;
			strSourceFile=repDir+"TestRunDeviceReport_TestCasesummarysubreport_XLS.jrxml";
	    	jasperPrint=getJasperPrint(strSourceFile,mapParameter);
			lJasperPrintlist.add(jasperPrint);
			
		}catch(Exception exception){
			log.error(exception);
		}
		return lJasperPrintlist;
	}
	
	private JasperPrint getJasperPrint(String strSourceFile,Map<String,Object> mapParameter){
		JasperPrint jasperPrint=null;
		JasperReport jasperReport=null;
		try{
			connection=getConnection();
			
			jasperReport = JasperCompileManager.compileReport(strSourceFile);
			jasperPrint = JasperFillManager.fillReport(jasperReport,mapParameter, connection);
			
			closeConnection();
		}catch(JRException jrException){
		}catch(Exception exceeption){}
		
		return jasperPrint;
	}
	
	private JasperPrint generateTestRunDeviceXLSreport(Integer testRunNo, String deviceId,Integer testRunConfigurationChildId)  {
		
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
        mapParameter.put("ptestRunConfigurationChildId", testRunConfigurationChildId);
		try{
			
			String strCurrLoc=this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
			String repDir=getReportDir();
			strSourceFile=repDir+"TestRunDeviceReport_XLS.jrxml";
			strSourceSubRepFile=repDir+"TestRunDeviceReport_TestCasesubreport.jrxml";
			
			connection=getConnection();
			// jrxml compiling process
			jasperReport = JasperCompileManager.compileReport(strSourceFile);
			jasperSubReport = JasperCompileManager.compileReport(strSourceSubRepFile);
			
			mapParameter.put("subreportParameter", jasperSubReport);
			mapParameter.put("subreportdir", repDir);
			// filling report with data from data source
			jasperPrint = JasperFillManager.fillReport(jasperReport,mapParameter, connection);
			closeConnection();	
		}catch(Exception exception){
			log.error(exception);
		}
		return jasperPrint;
	}	
	private Connection getConnection(){
		try{
			connection=this.dataSource.getConnection();
	
		}catch(Exception exception){
			log.error("getConnection:"+exception);
		}
			return connection;
	}
	
	
	private void closeConnection(){
		try{
			if (!connection.isClosed()){
				connection.close();
				connection=null;
				}
		}catch(Exception exception){
		}
	}
	private String getReportDir(){
		String strReturnVal=null;
		try{			
			String strCurrLoc=this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
			strCurrLoc = strCurrLoc.replace("%20", " ");
			if (strCurrLoc.indexOf("WEB-INF")>0){
				strCurrLoc=strCurrLoc.substring(1,strCurrLoc.indexOf("WEB-INF"));
			}
			strReturnVal=strCurrLoc+strReturnVal;
			strReturnVal=strReturnVal.substring(0,strReturnVal.lastIndexOf('/')+1);
			if(System.getProperty("os.name").contains("Linux")){
				strReturnVal="/"+strReturnVal+"WEB-INF/resources/Reports/";
			} else {
				strReturnVal=strReturnVal+"WEB-INF/resources/Reports/";
			}
        }catch(Exception exception){
			log.error("getReportDir:"+exception);
		}
		 return strReturnVal;
	}
	
	@Override
	@Transactional
	public List<String> generateTestRunListHtmlReport(Integer testRunNo, Integer testRunConfigurationChildId, String strPrintMode, ProductType productType, BufferedImage logo, String loginUserName) {
		log.info("Requesting to WorkpackageDAO generate test run html");
		List<String> list = new ArrayList<String>();
		list = workPackageDAO.generateTestRunListHtmlReport(testRunNo, testRunConfigurationChildId, strPrintMode, productType, logo,loginUserName);
		log.info("Responding back from WorkpackageDAO");
		return list;
	}	
	@Override
	@Transactional
	public ListMultimap<Integer, Object> getFeatureNamesByTestCaseIdList(Set<Integer> listOfTestCaseIds) {
		log.info("Requesting to WorkpackageDAO for getting feature names by passing test case ids ");
		ListMultimap<Integer, Object> multimap = ArrayListMultimap.create();
		multimap = workPackageDAO.getFeatureNamesByTestCaseIds(listOfTestCaseIds);
		log.info("Responding back from WorkpackageDAO");
		return multimap;
	}

	@Override
	@Transactional
	public List<String> getAllJobIdsByWorkpackageId(Integer testRunNo) {
		List<String> list = new ArrayList<String>();
		list = workPackageDAO.getAllJobIdsByWorkpackageId(testRunNo);
		return list;
	}
}