package com.hcl.atf.taf.controller;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXhtmlExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

import org.apache.commons.collections.MultiMap;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.constants.TAFConstants;
import com.hcl.atf.taf.controller.utilities.CommonUtility;
import com.hcl.atf.taf.controller.utilities.EvidenceGridData;
import com.hcl.atf.taf.integration.data.excel.ExcelTestDataIntegrator;//Changes for Bug :717
import com.hcl.atf.taf.model.ProductType;
import com.hcl.atf.taf.model.RunConfiguration;
import com.hcl.atf.taf.model.TestExecutionResult;
import com.hcl.atf.taf.model.TestExecutionResultBugList;
import com.hcl.atf.taf.model.TestRunJob;
import com.hcl.atf.taf.model.WorkPackage;
import com.hcl.atf.taf.model.WorkPackageTestCaseExecutionPlan;
import com.hcl.atf.taf.model.custom.TestRunReportsDeviceCaseList;
import com.hcl.atf.taf.model.custom.TestRunReportsDeviceCaseStepEvidenceGridList;
import com.hcl.atf.taf.model.custom.TestRunReportsDeviceCaseStepList;
import com.hcl.atf.taf.model.custom.TestRunReportsDeviceList;
import com.hcl.atf.taf.model.custom.TestRunReportsList;
import com.hcl.atf.taf.model.json.JsonTestExecutionResultBugList;
import com.hcl.atf.taf.model.json.JsonTestReport;
import com.hcl.atf.taf.model.json.JsonTestRunReportsDeviceCaseList;
import com.hcl.atf.taf.model.json.JsonTestRunReportsDeviceCaseStepList;
import com.hcl.atf.taf.model.json.JsonTestRunReportsDeviceList;
import com.hcl.atf.taf.model.json.JsonTestRunReportsList;
import com.hcl.atf.taf.model.json.jtable.JTableResponse;
import com.hcl.atf.taf.report.Report;
import com.hcl.atf.taf.report.xml.beans.WorkPackageBean;
import com.hcl.atf.taf.service.EmailService;
import com.hcl.atf.taf.service.ProductListService;
import com.hcl.atf.taf.service.TestExecutionBugsService;
import com.hcl.atf.taf.service.TestReportService;
import com.hcl.atf.taf.service.TestRunReportsDeviceCaseListService;
import com.hcl.atf.taf.service.TestRunReportsDeviceCaseStepListService;
import com.hcl.atf.taf.service.TestRunReportsDeviceListService;
import com.hcl.atf.taf.service.TestRunReportsListService;
import com.hcl.atf.taf.service.WorkPackageService;
import com.hcl.atf.taf.util.ZipTool;

@Controller
public class TestReportController {

	private static final Log log = LogFactory.getLog(TestReportController.class);

	@Autowired
	private TestReportService testReportService;
	@Autowired
	private TestRunReportsListService testRunReportsListService;
	@Autowired
	private TestRunReportsDeviceListService testRunReportsDeviceListService;
	@Autowired
	private TestRunReportsDeviceCaseListService testRunReportsDeviceCaseListService;
	@Autowired
	private TestRunReportsDeviceCaseStepListService testRunReportsDeviceCaseStepListService;
	@Autowired
	private Report report;
	@Autowired
	private EmailService emailService;
	@Autowired
	private WorkPackageService workPackageService;

	@Autowired
	private TestExecutionBugsService testExecutionBugsService;
	@Autowired
	private ExcelTestDataIntegrator excelTestDataIntegrator;//Changes for Bug :717
	
	@Autowired
	private ProductListService productService;

	@Value("#{ilcmProps['EVIDENCE_FOLDER']}")
	private String evidence_Folder;

	@Value("#{ilcmProps['html.report.generate.driveLocation']}")
	private String htmlReportGenLocation;
	
	@Value("#{ilcmProps['SHOW_TEST_STEP_EXECUTION_ID_IN_REPORT']}")
	private String showTestStepExecutionId;
	
	@Value("#{ilcmProps['JOB_ROOT_EVIDENCE_FOLDER_BACKUP_REQD']}")
	private String job_root_evidence_folder_backup_reqd;
	
	@RequestMapping(value="report.list",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse listFiltered(@RequestParam int jtStartIndex, @RequestParam int jtPageSize,@RequestParam Integer productId,@RequestParam String platformName,@RequestParam Integer runNo,@RequestParam String timeSince) {
		log.debug("inside report.list");
		JTableResponse jTableResponse;
		try {
			String tmpPlatformName=(platformName.equalsIgnoreCase("ALL"))?null:platformName;
			Date toDate = null;
			Date fromDate =null;
			if(!timeSince.equalsIgnoreCase("ALL")){
				toDate = new Date(System.currentTimeMillis());
				Calendar cal = Calendar.getInstance();  
				cal.setTime(toDate);  
				if(timeSince.endsWith("D"))
					cal.set(Calendar.DATE, (cal.get(Calendar.DATE)-Integer.parseInt( timeSince.substring(0, 2))));
				else if(timeSince.endsWith("W"))
					cal.set(Calendar.DATE, (cal.get(Calendar.DATE)-(7*Integer.parseInt( timeSince.substring(0, 2)))));
				else if(timeSince.endsWith("M"))
					cal.set(Calendar.DATE, (cal.get(Calendar.MONTH)-(Integer.parseInt( timeSince.substring(0, 2)))));  
				else if(timeSince.endsWith("Y"))
					cal.set(Calendar.DATE, (cal.get(Calendar.YEAR)-(Integer.parseInt( timeSince.substring(0, 2)))));  

				fromDate = cal.getTime();
			}

			List<TestExecutionResult> testExecutionResult = testReportService.listFilteredTestExecutionResult(jtStartIndex, jtPageSize, productId,tmpPlatformName , runNo, fromDate, toDate);
			List<JsonTestReport> jsonTestReport=new ArrayList<JsonTestReport>();
			for(TestExecutionResult ter: testExecutionResult){
				jsonTestReport.add(new JsonTestReport(ter));
			}

			jTableResponse = new JTableResponse("OK", jsonTestReport,testReportService.getTotalRecordsFiltered(productId, tmpPlatformName, runNo, fromDate, toDate));
			testExecutionResult = null;   
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error fetching records!");
			log.error("JSON ERROR", e);
			e.printStackTrace();
		}

		return jTableResponse;
	}


	/*public @ResponseBody JTableResponse list(@RequestParam int jtStartIndex, @RequestParam int jtPageSize,@RequestParam Integer productId,@RequestParam String platformName,@RequestParam Integer runNo,@RequestParam String timeSince,@RequestParam Integer productVersionListId,@RequestParam Integer testRunNo,@RequestParam String productName,@RequestParam String productVersionName) {	
		testRunNo=(testRunNo==null?0:testRunNo);
		productName=(productName==null?"":productName);
		productVersionName=(productVersionName==null?"":productVersionName);
		log.debug("inside test.run.parent.list");
		JTableResponse jTableResponse;

			try {
				String tmpProductName=(productName.equalsIgnoreCase("ALL"))?null:productName;
				String tmpProductVersionName=(productVersionName.equalsIgnoreCase("ALL"))?null:productVersionName;

			List<TestRunReportsList> testRunReportsList = testRunReportsListService.listAllTestRunReportsResult(jtStartIndex, jtPageSize,testRunNo,productName,productVersionName);

			List<JsonTestRunReportsList> jsonTestRunReportsList=new ArrayList<JsonTestRunReportsList>();

			for (JsonTestRunReportsList jsonTestRunReportsList2 : jsonTestRunReportsList) {

			}

			for(TestRunReportsList ter: testRunReportsList){

				jsonTestRunReportsList.add(new JsonTestRunReportsList(ter));

			}
			    jTableResponse = new JTableResponse("OK", jsonTestRunReportsList,testRunReportsListService.getTotalRecordsOfTestRunReportsResult());

	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	            e.printStackTrace();
	        }

        return jTableResponse;
    }
	 */

	@RequestMapping(value="report.run.list",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse listRun(@RequestParam int jtStartIndex, @RequestParam int jtPageSize,@RequestParam Integer runNo,@RequestParam String timeSince) {	
		log.debug("inside report.run.list");
		JTableResponse jTableResponse;

		try {

			List<TestRunReportsList> testRunReportsList = testRunReportsListService.listAllTestRunReportsResult(jtStartIndex,jtPageSize);

			List<JsonTestRunReportsList> jsonTestRunReportsList=new ArrayList<JsonTestRunReportsList>();

			for(TestRunReportsList testRunReportsListdet: testRunReportsList){
				jsonTestRunReportsList.add(new JsonTestRunReportsList(testRunReportsListdet));
			}
			jTableResponse = new JTableResponse("OK", jsonTestRunReportsList,testRunReportsListService.getTotalRecordsOfTestRunReportsResult());

		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error fetching records!");
			log.error("JSON ERROR", e);
			e.printStackTrace();
		}
		return jTableResponse;
	}

	/*public @ResponseBody JTableResponse listHomeDetailFiltered(@RequestParam Integer testRunListId,@RequestParam   String productName,@RequestParam String productVersionName,@RequestParam String deviceId,@RequestParam String testRunTriggeredTime) {
		log.debug("inside test.run.parent.list");
		JTableResponse jTableResponse;

			try {

			List<JsonTestRunReportsDeviceList> jsonTestRunReportsDeviceList=new ArrayList<JsonTestRunReportsDeviceList>();

			  jTableResponse = new JTableResponse("OK", jsonTestRunReportsDeviceList,testRunReportsDeviceListService.getTotalRecordsOfTestRunReportsDeviceListResult(testRunListId, productName, productVersionName));

	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	            e.printStackTrace();
	        }

        return jTableResponse;
    }
	 */
	@RequestMapping(value="report.run.device.list",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse listRunDevice(@RequestParam Integer testRunNo,@RequestParam   Integer testRunConfigurationChildId) {
		log.debug("inside report.run.device.list");
		JTableResponse jTableResponse;

		try {
			List<TestRunReportsDeviceList> testRunReportsDeviceList = testRunReportsDeviceListService.listAllTestRunReportsDeviceResult(testRunNo, testRunConfigurationChildId);
			List<JsonTestRunReportsDeviceList> jsonTestRunReportsDeviceList=new ArrayList<JsonTestRunReportsDeviceList>();
			for(TestRunReportsDeviceList testRunReportsDeviceListdet: testRunReportsDeviceList){
				jsonTestRunReportsDeviceList.add(new JsonTestRunReportsDeviceList(testRunReportsDeviceListdet));
			}
			jTableResponse = new JTableResponse("OK", jsonTestRunReportsDeviceList,testRunReportsDeviceListService.getTotalRecordsOfTestRunReportsDeviceListResult(testRunNo, testRunConfigurationChildId));

		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error fetching records!");
			log.error("JSON ERROR", e);
			e.printStackTrace();
		}

		return jTableResponse;
	}


	@RequestMapping(value="report.run.device.case.list",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse listRunDeviceCase(@RequestParam Integer testRunListId,@RequestParam   Integer testRunConfigurationChildId) {
		log.debug("inside report.run.device.case.list");
		JTableResponse jTableResponse;

		try {
			List<TestRunReportsDeviceCaseList> testRunReportsDeviceCaseList = testRunReportsDeviceCaseListService.listAllTestRunReportsDeviceCaseResult(testRunListId, testRunConfigurationChildId);
			List<JsonTestRunReportsDeviceCaseList> jsonTestRunReportsDeviceCaseList=new ArrayList<JsonTestRunReportsDeviceCaseList>();

			for(TestRunReportsDeviceCaseList testRunReportsDeviceCaseListdet: testRunReportsDeviceCaseList){
				jsonTestRunReportsDeviceCaseList.add(new JsonTestRunReportsDeviceCaseList(testRunReportsDeviceCaseListdet));
			}

			jTableResponse = new JTableResponse("OK", jsonTestRunReportsDeviceCaseList,testRunReportsDeviceCaseListService.getTotalRecordsOfTestRunReportsDeviceCaseListResult(testRunListId, testRunConfigurationChildId));

		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error fetching records!");
			log.error("JSON ERROR", e);
			e.printStackTrace();
		}

		return jTableResponse;
	}

	@RequestMapping(value="report.run.device.case.step.list",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse listRunDeviceCase(@RequestParam Integer testRunListId,@RequestParam   Integer testRunConfigurationChildId,@RequestParam Integer testCaseId ) {
		log.debug("inside report.run.device.case.step.list");
		JTableResponse jTableResponse;

		try {
			List<TestRunReportsDeviceCaseStepList> testRunReportsDeviceCaseStepList = testRunReportsDeviceCaseStepListService.listAllTestRunReportsDeviceCaseStepResult(testRunListId, testRunConfigurationChildId,testCaseId);
			List<JsonTestRunReportsDeviceCaseStepList> jsonTestRunReportsDeviceCaseStepList=new ArrayList<JsonTestRunReportsDeviceCaseStepList>();
			for(TestRunReportsDeviceCaseStepList testRunReportsDeviceCaseStepListdet: testRunReportsDeviceCaseStepList){
				jsonTestRunReportsDeviceCaseStepList.add(new JsonTestRunReportsDeviceCaseStepList(testRunReportsDeviceCaseStepListdet));
			}

			jTableResponse = new JTableResponse("OK", jsonTestRunReportsDeviceCaseStepList,testRunReportsDeviceCaseStepListService.getTotalRecordsOfTestRunReportsDeviceCaseStepListResult(testRunListId, testRunConfigurationChildId,testCaseId));

		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error fetching records!");
			log.error("JSON ERROR", e);
			e.printStackTrace();
		}

		return jTableResponse;
	}

	//Method Added for Evidence module
	/*@RequestMapping(value="evidence.grid.list")
    public String listEvidenceGrid(@RequestParam Integer testRunNo,@RequestParam Integer testRunConfigurationChildId, ModelMap model) {
		log.debug("inside evidence.grid.list");
		List<TestRunReportsDeviceCaseStepList> testRunReportsDeviceCaseStepList = null;
		List<TestRunReportsDeviceCaseStepEvidenceGridList> testRunReportsDeviceCaseStepEvidenceGridList = null; 
		List<TestRunReportsDeviceList> testRunReportsDeviceList = null;

		try {

			testRunReportsDeviceCaseStepList = testRunReportsDeviceCaseStepListService.listAllTestRunReportsDeviceCaseStepResult(testRunNo, testRunConfigurationChildId);

			testRunReportsDeviceCaseStepEvidenceGridList = EvidenceGridData.transposeTestStepScreenShotData(testRunReportsDeviceCaseStepList);

			testRunReportsDeviceList = testRunReportsDeviceListService.listAllTestRunReportsDeviceResult(testRunNo, testRunConfigurationChildId);

        } catch (Exception e) {
            log.error("Issue calling service layer/Getting Transpose of EvidenceGrid data", e);

            e.printStackTrace();
        }
		model.addAttribute("testRunReportsList", (testRunReportsDeviceCaseStepList == null) ? null : testRunReportsDeviceCaseStepList.get(0));
		model.addAttribute("testRunReportsDeviceList",testRunReportsDeviceList);
	    model.addAttribute("testRunReportsDeviceCaseStepEvidenceGridList",testRunReportsDeviceCaseStepEvidenceGridList);    
		return "evidenceGrid";
    }*/

	@RequestMapping(value="evidence.grid.list" ,method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JSONObject listEvidenceGrid(@RequestParam Integer testRunNo,@RequestParam Integer testRunConfigurationChildId) {
		List<TestRunReportsDeviceCaseStepList> testRunReportsDeviceCaseStepList = null;
		List<TestRunReportsDeviceCaseStepEvidenceGridList> testRunReportsDeviceCaseStepEvidenceGridList = null; 
		List<TestRunReportsDeviceList> testRunReportsDeviceList = null;
		WorkPackage wp=workPackageService.getWorkPackageById(testRunNo);
		Integer productTypeId=wp.getProductBuild().getProductVersion().getProductMaster().getProductType().getProductTypeId();
		log.info("productTypeId==>"+productTypeId);
		JSONObject finalObj = new JSONObject();
		JSONObject tctitle= new JSONObject();
		JSONObject deviceTitle=new JSONObject();

		JSONObject productHeader= new JSONObject();
		JSONObject productVersionHeader= new JSONObject();
		JSONObject runConfigurationHeader= new JSONObject();
		JSONObject runNoHeader= new JSONObject();
		JSONObject StartTimeHeader= new JSONObject();

		JSONArray titleList = new JSONArray();
		JSONArray columnData = new JSONArray();
		JSONArray headerData = new JSONArray();
		JSONArray finalcolumnDataList = new JSONArray();
		JSONArray finalList = new JSONArray();
		try {
			testRunReportsDeviceCaseStepList = testRunReportsDeviceCaseStepListService.listAllTestRunReportsDeviceCaseStepResult(testRunNo, testRunConfigurationChildId);
			testRunReportsDeviceCaseStepEvidenceGridList = EvidenceGridData.transposeTestStepScreenShotData(testRunReportsDeviceCaseStepList);
			testRunReportsDeviceList = testRunReportsDeviceListService.listAllTestRunReportsDeviceResult(testRunNo, testRunConfigurationChildId);
		} catch (Exception e) {
			log.error("Issue calling service layer/Getting Transpose of EvidenceGrid data", e);
			e.printStackTrace();
		}

		String runConfig="";
		int testRunListIdCount = 0;

		if(testRunReportsDeviceCaseStepList.size()!=0){
			TestRunReportsDeviceCaseStepList testRunReportsDeviceCaseStepListObj=testRunReportsDeviceCaseStepList.get(0);
			//deviceTitle.put("title", runConfig);
			productHeader.put("header", "Product : "+testRunReportsDeviceCaseStepListObj.getProductname()+"  ");
			productVersionHeader.put("header","Product Version : "+testRunReportsDeviceCaseStepListObj.getProductversionname()+"  ");
			runConfigurationHeader.put("header","Configuration Name : "+testRunReportsDeviceCaseStepListObj.getTestrunconfigurationname()+"  ");
			runNoHeader.put("header","Workpackage No : "+testRunReportsDeviceCaseStepListObj.getTestrunno()+"  ");
			StartTimeHeader.put("header","Start Time : "+testRunReportsDeviceCaseStepListObj.getTeststepstarttime()+"  ");
			headerData.add(productHeader);
			headerData.add(productVersionHeader);
			headerData.add(runConfigurationHeader);
			headerData.add(runNoHeader);
			headerData.add(StartTimeHeader);
		}

		for(TestRunReportsDeviceList testRunReportsDeviceListObj :testRunReportsDeviceList){
			runConfig="";
			testRunListIdCount = testRunListIdCount+1;
			String logPath= testRunReportsDeviceListObj.getTestRunListId().toString();
			String logFileName="";
			if(productTypeId==1 || productTypeId==5){ // 1 is Device
				logFileName = logPath + "-" + testRunReportsDeviceListObj.getDeviceId() + ".log";
			}else if(productTypeId==2){ //2 is Web
				logFileName = logPath + "-" + testRunReportsDeviceListObj.getHostId() + ".log";
			}

			String logUrl = "evidence.log?logPath="+logPath+"&logFileName="+logFileName;
			String jobLogFileName=IDPAConstants.JOB_LOG_PATH+"-"+logPath+".txt";
			String jobLogUrl="evidence.log?logPath="+logPath+"&logFileName="+jobLogFileName;
			if(productTypeId==1 || productTypeId==5){
				//runConfig=testRunReportsDeviceListObj.getDeviceModel()+","+testRunReportsDeviceListObj.getTestEnvironmentName()+"  "+testRunReportsDeviceListObj.getDevicePlatformVersion()+","+testRunReportsDeviceListObj.getDeviceId()+"#"+logUrl+"#"+jobLogUrl;
				runConfig=testRunReportsDeviceListObj.getDeviceModel()+","+testRunReportsDeviceListObj.getTestEnvironmentName()+"  "+testRunReportsDeviceListObj.getDevicePlatformVersion()+","+testRunReportsDeviceListObj.getDeviceId()+"#"+jobLogUrl;
			}else if(productTypeId==2){
				//runConfig=testRunReportsDeviceListObj.getHostName()+","+testRunReportsDeviceListObj.getTestEnvironmentName()+"  "+","+testRunReportsDeviceListObj.getHostIpAddress()+"#"+logUrl+"#"+jobLogUrl;
				runConfig=testRunReportsDeviceListObj.getHostName()+","+testRunReportsDeviceListObj.getTestEnvironmentName()+"  "+","+testRunReportsDeviceListObj.getHostIpAddress()+"#"+jobLogUrl;
			}
			//runConfig=logUrl;
			deviceTitle.put("title", runConfig);
			titleList.add(deviceTitle);
			deviceTitle=new JSONObject();
		}

		String screenShotLabel = "";
		String testStepName ="";
		String testCaseName = "";
		String testCaseInfo="";
		Integer testStepId;
		Integer testCaseId;

		for(TestRunReportsDeviceCaseStepEvidenceGridList testRunReportsDeviceCaseStepEvidenceGridListdet:testRunReportsDeviceCaseStepEvidenceGridList){
			screenShotLabel = testRunReportsDeviceCaseStepEvidenceGridListdet.getScreenShotLabel();
			testStepName = testRunReportsDeviceCaseStepEvidenceGridListdet.getTestStepName();
			testCaseName = testRunReportsDeviceCaseStepEvidenceGridListdet.getTestCaseName();
			testStepId = testRunReportsDeviceCaseStepEvidenceGridListdet.getTestStepId();
			testCaseId = testRunReportsDeviceCaseStepEvidenceGridListdet.getTestCaseId();
			testCaseInfo="["+testCaseId+"]"+testCaseName+"~"+"["+testStepId+"]"+testStepName +"~"+screenShotLabel;
			columnData.add(testCaseInfo);

			for(int i=0;i<testRunListIdCount;i++){
				String imageUrl="";
				String deviceModel = testRunReportsDeviceList.get(i).getDeviceModel();
				Integer imageBaseFolderPath = testRunReportsDeviceList.get(i).getTestRunListId();
				String imageFileName = testRunReportsDeviceCaseStepEvidenceGridListdet.getScreenShotPathMap().get(imageBaseFolderPath);
				boolean isTestStepExecutedOnAtestRunListId = testRunReportsDeviceCaseStepEvidenceGridListdet.getScreenShotPathMap().containsKey(imageBaseFolderPath);

				if (!isTestStepExecutedOnAtestRunListId) {	
					imageUrl="Test Step Not Executed";
					//<td>Test Step Not Executed</td>

				}else if ((imageFileName == null) || imageFileName.trim().equals("") || ("null".equals(imageFileName))) {	
					//<td> Not avScreenshotailable</td>
					imageUrl=null;
				} else {
					imageUrl = "evidence.image?imagePath="+imageBaseFolderPath+"&imageFileName="+imageFileName;
					/*String arr[]=imageFileName.split("\\");
						String url=arr[arr.length-1];*/

					// imageUrl=url;
					log.info("imageUrl"+imageUrl);
					//imageUrl=imageFileName;;
					//	<td><img width="<%=imageWidth%>" BORDER="0" height="<%=imageHeight%>" src="<%=imageUrl%>"/></td>
				}
				columnData.add(imageUrl);
			}
			finalcolumnDataList.add(columnData);
			columnData= new JSONArray(); 
		}

		finalObj.put("COLUMNS", titleList);
		finalObj.put("DATA", finalcolumnDataList);
		finalObj.put("HEADER",headerData);
		return finalObj;
	}

	//Method Added for Evidence module
	@RequestMapping(value="evidence.image",method=RequestMethod.GET,produces=MediaType.IMAGE_JPEG_VALUE)
	public @ResponseBody byte[] showImage(@RequestParam String imagePath, String imageFileName,HttpServletRequest request,HttpServletResponse response) {
		log.debug("inside evidence.image");
		byte[] imageBinary  = null;
		try {

			//String imageRealPath=CommonUtility.getCatalinaPath()+ File.separator+ imageFileName;
			String imageRealPath=imageFileName;
			File imageFile = new File(imageRealPath);
			if(imageFile.exists() && imageFile !=null){
				FileInputStream fis = new FileInputStream(imageFile);				
				imageBinary = IOUtils.toByteArray(fis);			    
				String strContentType="image/jpeg";
				ServletOutputStream servletOutputStream = response.getOutputStream();
				response.setContentType(strContentType);
				servletOutputStream.write(imageBinary);
				servletOutputStream.flush();
				servletOutputStream.close();
				fis.close();
			}
		} catch (Exception e) {

			log.error("Issue in getting image", e);
		}
		return imageBinary;
	}

	//Method Added for Evidence module
	@RequestMapping(value="evidence.log",produces="application/txt")
	public @ResponseBody byte[] getLogFile(@RequestParam String logPath, String logFileName,HttpServletRequest request,HttpServletResponse response) {
		log.debug("inside evidence.log");
		byte[] logFileBinary  = null;
		try {
			String logRealPath= evidence_Folder+File.separator+logPath;
			//If Backup folder is set as YES
			logRealPath=CommonUtility.getCatalinaPath() +File.separator+evidence_Folder+File.separator+logPath+File.separator+TAFConstants.EVIDENCE_LOG+File.separator+logFileName;
			
			//logRealPath=CommonUtility.getCatalinaPath() +File.separator+logFileName;
			log.info("logRealPath"+logRealPath);
			FileInputStream fis = new FileInputStream(logRealPath);

			logFileBinary = IOUtils.toByteArray(fis);

			String strContentType="application/octet-stream";
			ServletOutputStream servletOutputStream = response.getOutputStream();
			response.setContentType(strContentType);
			response.setHeader("Content-Disposition", "inline; filename=" + logFileName);

			servletOutputStream.write(logFileBinary);
			servletOutputStream.flush();
			servletOutputStream.close();
			fis.close();
		} catch (Exception e) {
			log.error("Issue in getting log file", e);
			e.printStackTrace();
		}
		return logFileBinary;
	}

	@RequestMapping(value="report.run.device.pdf" ,produces="application/pdf")
	public  @ResponseBody JTableResponse runDeviceReport_PDF(@RequestParam Integer testRunNo,@RequestParam Integer testRunConfigurationChildId,@RequestParam String deviceId,
			@RequestParam Integer testRunJobId,@RequestParam String reportType,@RequestParam String viewType,ModelAndView modelAndView,HttpServletRequest request,HttpServletResponse response) throws Exception {

		JTableResponse jTableResponse;
		TestRunJob testRunJob=null;
		ProductType productType =null;
		String exportLocation =null;
		String serverFolderPath = null;
		if (System.getProperty("os.name").contains("Linux")) {
			serverFolderPath = CommonUtility.getCatalinaPath()
					+ IDPAConstants.JASPERREPORTS_PATH_LINUX;
		} else {
			serverFolderPath = CommonUtility.getCatalinaPath()
					+ IDPAConstants.JASPERREPORTS_PATH;
		}
		String testSuiteName = "";//Changes for Bugzilla bug 784 - Report customizations
		String workpackageName="";
		String imageFileName = "";
		BufferedImage logo = null;
		String loginUserName = "";

		/*UserList user = (UserList)request.getSession().getAttribute("USER");
        String loginUserName = user.getFirstName().concat("").concat(user.getLastName());*/


		String imageServerPath = null;
		String productName = "";
		String testRunPlanName = "";
		String customerName = "";

		String productTypeName = "";
		if(System.getProperty("os.name").contains("Linux")){
			imageServerPath= CommonUtility.getCatalinaPath().concat("/webapps/Logo/");
		} else {
			imageServerPath= CommonUtility.getCatalinaPath().concat("\\webapps\\Logo\\");
		}
		log.info("Job Id:" +testRunJobId);
		if(testRunJobId!=-1){  // Test Run Job Level
			testRunJob=workPackageService.getTestRunJobById(testRunJobId);
			testRunNo=testRunJob.getWorkPackage().getWorkPackageId();
			if(testRunJob.getWorkPackage()!=null){
				loginUserName = testRunJob.getWorkPackage().getUserList().getLoginId();
			}
			if(testRunJob.getWorkPackage().getTestRunPlan()!=null){
				testRunConfigurationChildId=testRunJob.getWorkPackage().getTestRunPlan().getTestRunPlanId();
				if(testRunJob.getTestSuite() != null){
					testSuiteName = testRunJob.getTestSuite().getTestSuiteName();	
				}					
				productType =testRunJob.getWorkPackage().getTestRunPlan().getProductVersionListMaster().getProductMaster().getProductType();
				customerName = testRunJob.getWorkPackage().getTestRunPlan().getProductVersionListMaster().getProductMaster().getCustomer().getCustomerName().trim();
				productName = testRunJob.getWorkPackage().getTestRunPlan().getProductVersionListMaster().getProductMaster().getProductName();
				testRunPlanName = testRunJob.getWorkPackage().getTestRunPlan().getTestRunPlanName();
				log.info("product Type Master Name "+productType.getTypeName());
				//log.info("product Master name based on the testRunConfigChild"+productMaster.getProductName());
				imageFileName = testRunJob.getWorkPackage().getTestRunPlan().getProductVersionListMaster().getProductMaster().getCustomer().getImageURI();
				productTypeName = "Job";
				log.info("ImageFileName==>"+imageFileName);
			}
			if(testRunJob.getGenericDevices()!=null){
				deviceId= testRunJob.getGenericDevices().getUDID();
			}			 
		}else{ // Work Package Level
			WorkPackage wp=workPackageService.getWorkPackageById(testRunNo);
			if(wp!=null){				
				loginUserName = wp.getUserList().getLoginId();
			}
			workpackageName = wp.getName();
			productType=wp.getProductBuild().getProductVersion().getProductMaster().getProductType();
			productName = wp.getProductBuild().getProductVersion().getProductMaster().getProductName();
			customerName = wp.getProductBuild().getProductVersion().getProductMaster().getCustomer().getCustomerName().trim();
			testRunPlanName = wp.getTestRunPlan().getTestRunPlanName();
			if(wp.getTestRunPlan()!=null){
				testRunConfigurationChildId=wp.getTestRunPlan().getTestRunPlanId();
				imageFileName = wp.getTestRunPlan().getProductVersionListMaster().getProductMaster().getCustomer().getImageURI();
				productTypeName = "Workpackage";
				log.info("ImageFileName==>"+imageFileName);
			}			
		}

		if(imageFileName != null){
			imageServerPath = imageServerPath.concat(imageFileName);
			log.info("ImageServerPath==>"+imageServerPath);
		} else {		
			if(System.getProperty("os.name").contains("Linux")){
				imageServerPath= CommonUtility.getCatalinaPath().concat("/webapps"+ File.separator + request.getContextPath() + File.separator +"css/images/noimage.jpg");
			} else {
				imageServerPath= CommonUtility.getCatalinaPath().concat("\\webapps"+ File.separator + request.getContextPath() + File.separator +"css\\images\\noimage.jpg");
			}
		}
		File imageFile = new File(imageServerPath);
		log.info("ImageFile==>"+imageFile);
		if(imageFile.exists() && imageFile != null){
			logo = ImageIO.read(imageFile);	
		} 
		if(testRunNo!=-1 && viewType.equalsIgnoreCase("PDF")){
			serverFolderPath=serverFolderPath+File.separator+"WorkPackages"+File.separator+testRunNo;
			exportLocation = serverFolderPath+File.separator+productName+"-"+testRunPlanName+"-"+testRunNo+".pdf";
			log.info("wp exportLocation==>"+exportLocation);
		}
		else
		{
			serverFolderPath=serverFolderPath+File.separator+"WorkPackages"+File.separator+testRunNo;
			exportLocation = serverFolderPath+File.separator+productName+"-"+testRunPlanName+"-"+testRunNo+".html";
			log.info("wp exportLocation==>"+exportLocation);
		}
		// if(testRunJobId!=-1 || viewType.equalsIgnoreCase("PDF")){
		if(viewType.equalsIgnoreCase("PDF")){
			serverFolderPath=serverFolderPath+File.separator+"Jobs"+File.separator+testRunJobId;
			exportLocation = serverFolderPath+File.separator+productName+"-"+testRunPlanName+"-"+testRunJobId+".pdf";
			log.info("job exportLocation==>"+exportLocation);
		}
		else
		{
			serverFolderPath=serverFolderPath+File.separator+"Jobs"+File.separator+testRunJobId;
			exportLocation = serverFolderPath+File.separator+productName+"-"+testRunPlanName+"-"+testRunJobId+".html";
			log.info("job exportLocation==>"+exportLocation);
		}
		log.info("Report Inputs : " + testRunNo + " : " + testRunConfigurationChildId);

		JasperPrint jasperPrint=null;

		String strContentType=null;
		String strPrintMode=null;

		if(viewType.equalsIgnoreCase("PDF"))
		{
			strPrintMode="PDF";
			strContentType="application/pdf";
		}
		else
		{
			strPrintMode="HTML";
			strContentType="application/html";
		}

		response.reset();
		response.resetBuffer();
		//ServletOutputStream servletOutputStream = response.getOutputStream();

		//response.setContentType(strContentType);
		response.setHeader("Content-Disposition","inline;");

		if (productTypeName.equalsIgnoreCase("JOB")){
			log.info("Going to TestRunDeviceListReport : " + testRunNo + " : " + testRunConfigurationChildId);
			jasperPrint=report.generateTestRunDeviceListReport(testRunNo,testRunJobId,deviceId,testRunConfigurationChildId,strPrintMode,logo, loginUserName,reportType);
		}

		if (productTypeName.equalsIgnoreCase("WORKPACKAGE")){
			if(customerName.equalsIgnoreCase("Rockwell Collins"))
			{
				jasperPrint=report.generateTestRunListRCReport(testRunNo, testRunConfigurationChildId, strPrintMode, productType, logo, loginUserName);
			}
			else
			{
				log.info("Going to TestRunListReport : " + testRunNo + " : " + testRunConfigurationChildId);
				jasperPrint=report.generateTestRunListReport(testRunNo, testRunConfigurationChildId,strPrintMode,productType,logo, loginUserName);
			}
		}

		if(jasperPrint==null ){
			log.info("no data ");
			jTableResponse = new JTableResponse("ERROR","Error fetching records!");
			return jTableResponse;
		}

		File file = new File(serverFolderPath);
		if (!file.isDirectory()) {
			file.mkdirs();
		}
		if(viewType.equalsIgnoreCase("Pdf"))
		{
			FileOutputStream  fos=new FileOutputStream(exportLocation);
			JRPdfExporter jRPdfExporter =new JRPdfExporter();
			jRPdfExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			/*jRPdfExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream );*/
			jRPdfExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, fos );
			jRPdfExporter.exportReport();
		}
		else
		{
			FileOutputStream  fos=new FileOutputStream(exportLocation);
			JRXhtmlExporter exporter = new JRXhtmlExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			/*exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, Boolean.FALSE); */
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, fos);
			exporter.exportReport();
		}

		jTableResponse = new JTableResponse("Ok","Export testCases Completed.",exportLocation);

		return jTableResponse;
	}

	//Functionality for generating HTML reports
	@RequestMapping(value="report.run.device.html" ,produces="application/html")
	public  @ResponseBody JTableResponse runDeviceReport_HTML(@RequestParam Integer testRunNo,@RequestParam Integer testRunConfigurationChildId,@RequestParam String deviceId,
			@RequestParam Integer testRunJobId,@RequestParam String reportType,@RequestParam String viewType,ModelAndView modelAndView,HttpServletRequest request,HttpServletResponse response) throws Exception {
		log.info("Report Inputs : work package no " + testRunNo + " child id : " + testRunConfigurationChildId+" job id :"+testRunJobId);
		JTableResponse jTableResponse = null ;
		TestRunJob testRunJob=null;
		ProductType productType =null;
		String exportLocation =null;
		String serverFolderPath = null;
		if (System.getProperty("os.name").contains("Linux")) {
			serverFolderPath = CommonUtility.getCatalinaPath()
					+ IDPAConstants.JASPERREPORTS_PATH_LINUX;
		} else {
			serverFolderPath = CommonUtility.getCatalinaPath()
					+ IDPAConstants.JASPERREPORTS_PATH;
		}
		String testSuiteName = "";
		String workpackageName="";
		String imageFileName = "";
		BufferedImage logo = null;
		String loginUserName = "";
		String workpackageHtmlFile = "";
		String JobLevelHTMLFIle="";
		String imageServerPath = null;
		String productName = "";
		String testRunPlanName = "";
		String customerName = "";
		String closeImagePath = "";

		String productTypeName = "";
		if(System.getProperty("os.name").contains("Linux")){
			imageServerPath= CommonUtility.getCatalinaPath().concat("/webapps/Logo/");
		} else {
			imageServerPath= CommonUtility.getCatalinaPath().concat("\\webapps\\Logo\\");
		}
		closeImagePath = CommonUtility.getCatalinaPath() +File.separator + "webapps" + File.separator + request.getContextPath() + File.separator + "css" + File.separator + "images" +File.separator + "close_screenshot_image.jpg";
		log.info("Job Id:" +testRunJobId);
		if(testRunJobId!=-1){  // Test Run Job Level
			testRunJob=workPackageService.getTestRunJobById(testRunJobId);
			testRunNo=testRunJob.getWorkPackage().getWorkPackageId();
			if(testRunJob.getWorkPackage()!=null){
				// loginUserName = testRunJob.getWorkPackage().getUserList().getLoginId();
			}
			if(testRunJob.getWorkPackage().getTestRunPlan()!=null){
				testRunConfigurationChildId=testRunJob.getWorkPackage().getTestRunPlan().getTestRunPlanId();
				if(testRunJob.getTestSuite() != null){
					testSuiteName = testRunJob.getTestSuite().getTestSuiteName();	
				}					
				productType =testRunJob.getWorkPackage().getTestRunPlan().getProductVersionListMaster().getProductMaster().getProductType();
				customerName = testRunJob.getWorkPackage().getTestRunPlan().getProductVersionListMaster().getProductMaster().getCustomer().getCustomerName().trim();
				productName = testRunJob.getWorkPackage().getTestRunPlan().getProductVersionListMaster().getProductMaster().getProductName();
				testRunPlanName = testRunJob.getWorkPackage().getTestRunPlan().getTestRunPlanName();
				log.info("product Type Master Name "+productType.getTypeName());
				//log.info("product Master name based on the testRunConfigChild"+productMaster.getProductName());
				imageFileName = testRunJob.getWorkPackage().getTestRunPlan().getProductVersionListMaster().getProductMaster().getCustomer().getImageURI();
				productTypeName = "Job";
				log.info("ImageFileName==>"+imageFileName);
			}
			if(testRunJob.getGenericDevices()!=null){
				deviceId= testRunJob.getGenericDevices().getUDID();
			}			 
		}else{ // Work Package Level
			WorkPackage wp=workPackageService.getWorkPackageById(testRunNo);
			if(wp!=null){				
				//loginUserName = wp.getUserList().getLoginId();
			}
			workpackageName = wp.getName();
			productType=wp.getProductBuild().getProductVersion().getProductMaster().getProductType();
			productName = wp.getProductBuild().getProductVersion().getProductMaster().getProductName();
			customerName = wp.getProductBuild().getProductVersion().getProductMaster().getCustomer().getCustomerName().trim();
			//testRunPlanName = wp.getTestRunPlan().getTestRunPlanName();
			if(wp.getTestRunPlan()!=null){
				testRunConfigurationChildId=wp.getTestRunPlan().getTestRunPlanId();
				imageFileName = wp.getTestRunPlan().getProductVersionListMaster().getProductMaster().getCustomer().getImageURI();
				productTypeName = "Workpackage";
				log.info("ImageFileName==>"+imageFileName);
			}			
		}

		if(imageFileName != null && new File(imageServerPath.concat(imageFileName)).exists()){
			imageServerPath = imageServerPath.concat(imageFileName);
			log.info("ImageServerPath==>"+imageServerPath);
		} else {		
			if(System.getProperty("os.name").contains("Linux")){
				imageServerPath= CommonUtility.getCatalinaPath().concat("/webapps"+ File.separator + request.getContextPath() + File.separator +"css/images/noimage.jpg");
			} else {
				imageServerPath= CommonUtility.getCatalinaPath().concat("\\webapps"+ File.separator + request.getContextPath() + File.separator +"css\\images\\noimage.jpg");
			}
		}
		File imageFile = new File(imageServerPath);
		log.info("ImageFile==>"+imageFile);

		if(imageFile.exists() && imageFile != null){
			//logo = ImageIO.read(imageFile);	
		} else {
			Files.createDirectories(Paths.get(imageFile.getParent()));
			log.info("imageFileName is:"+imageFileName);
		}

		log.info("Report Inputs : " + testRunNo + " : " + testRunConfigurationChildId);
		log.info("logo path is :"+logo);
		JasperPrint jasperPrint=null;

		String strContentType=null;
		String strPrintMode=null;

		if(viewType.equalsIgnoreCase("PDF")) {
			strPrintMode="PDF"; 
			strContentType="application/pdf";
		} else {
			strPrintMode="HTML";
			strContentType="application/html";
		}
		response.reset();
		response.resetBuffer();
		String jsonSourceFile = null;
		String jsonSourceSubFile1 = null;
		String jsonSourceSubFile2 = null;
		String jsonSourceSubFile3 = null;
		String jsonSourceSubFile4= null;
		if (productTypeName.equalsIgnoreCase("JOB")){
			List<String>JobHtmlReportData=new ArrayList<String>();
			log.info("Going to TestRunListHtmlReport : " + testRunNo + " : " + testRunConfigurationChildId+"  :  "+productTypeName);
			JobHtmlReportData=report.generateTestRunListHtmlReport(testRunNo, testRunConfigurationChildId,strPrintMode,productType,logo, loginUserName);
			log.info("after getting the html report data");
			if(!JobHtmlReportData.isEmpty()){
				for(int i=0;i<JobHtmlReportData.size();i++){
					jsonSourceFile=JobHtmlReportData.get(0);
					jsonSourceSubFile1=JobHtmlReportData.get(1);
					jsonSourceSubFile2=JobHtmlReportData.get(2);
					jsonSourceSubFile3=JobHtmlReportData.get(3);
					jsonSourceSubFile4=JobHtmlReportData.get(4);
				}
				org.json.JSONArray jsonJobReportArray=new org.json.JSONArray(jsonSourceSubFile1);
				boolean isJobExists=false;
				List<String> jobIdsList=new ArrayList<String>();
				for(int i=0;i<jsonJobReportArray.length();i++){
					org.json.JSONObject jsonJobReportObject = jsonJobReportArray.getJSONObject(i);
					jobIdsList.add(jsonJobReportObject.getString("job_id"));
				}
				for(int l=0;l<jobIdsList.size();l++){
					if(Integer.parseInt(jobIdsList.get(l))==testRunJobId){
						isJobExists=true;
					}
				}
				log.info("isJobExists :"+isJobExists);
				if(isJobExists && new org.json.JSONArray(JobHtmlReportData.get(1)).length() > 0){
					org.json.JSONArray jsonReportSummaryArray=new org.json.JSONArray(jsonSourceFile);
					//org.json.JSONArray jsonJobReportArray=new org.json.JSONArray(jsonSourceSubFile1);
					org.json.JSONArray jsonJobSummaryArray=new org.json.JSONArray(jsonSourceSubFile2);
					org.json.JSONArray jsonTestCaseSummaryArray=new org.json.JSONArray(jsonSourceSubFile3);
					org.json.JSONArray jsonTestStepArray=new org.json.JSONArray(jsonSourceSubFile4);
					log.info("jsonReportSummaryArray values are :"+jsonReportSummaryArray);
					log.info("jsonJobReportArray values are :"+jsonJobReportArray);
					log.info("jsonJobSummaryArray values are :"+jsonJobSummaryArray);
					log.info("jsonTestCaseSummaryArray values are :"+jsonTestCaseSummaryArray);
					log.info("jsonTestStepArray values are :"+jsonTestStepArray);	

					try{
						List<String>testStepPath=new ArrayList<String>();
						JobLevelHTMLFIle = htmlReportGenLocation+File.separator+"JobReport"+File.separator+productName+"-JR-"+testRunJobId+".html";
						File htmFile = new File(JobLevelHTMLFIle);
						if(!htmFile.exists()){
							new File(htmFile.getParent()).mkdirs();
						}
						byte[] buffer = new byte[1024];
						BufferedWriter bw = new BufferedWriter(new FileWriter(new File(JobLevelHTMLFIle)));	 
						//HashMap<String, Object> map = new HashMap<String, Object>();
						bw.write("<html><head><META http-equiv=Content-Type content='text/html; charset=utf-8'><style>.center {margin: auto;width: 75%;border: 3px solid #473572;padding: 10px;position : fixed;background:white;height:60%;}.parentdiv {margin: auto; width: 80%;height:50%;border: 3px;padding: 10px;position : fixed;top:20%;left:8%;right:auto;}body {background-color: #FFFFFF;}th{text-align: left;color:white;background: #3c6ac6;}a {color:blue;} a:visited {color:blue;}a:hover {color:blue;}a:active {color:blue;}</style><title>Job Report</title><script>function display(e){document.getElementById(\"tag\").style.display='';var img = document.getElementById(\"f\");img.setAttribute(\"src\",e);document.getElementById(\"childdiv\").appendChild(img);return false;}</script></head><body>");
						try {
							String passedOrFailedJob="PASSED";
							String jobresult="", device="", devicePlatForm="";
							org.json.JSONObject jsonReportSummaryObject = jsonReportSummaryArray.getJSONObject(0);	
							bw.write("<table width='100%'><tr style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #FFFFFF; font-size: 24px; height:62px;background:#003ca4; font-weight: bold;'><td><img style=\"width:50px;height:50px;\" src='logo/"+imageFile.getName()+"'/></td> <td align='middle' id='TOP'>Test Run Job Report - "+jsonReportSummaryObject.getString("productname")+" - "+jsonReportSummaryObject.getString("testrunconfigurationname")+"</td></tr><tr style='height:13px'></tr></table>");		
							bw.write("<table bgcolor='white' width='100%' style='table-layout: fixed;'>");
							//report summary
							for(int j=0;j<jsonTestCaseSummaryArray.length();j++){
								if(!jobresult.equalsIgnoreCase("FAILED")){
									org.json.JSONObject testCaseObj =jsonTestCaseSummaryArray.getJSONObject(j);
									passedOrFailedJob = "<td style='font-weight: bold'> : <span style='color:#00C800;font-family: DejaVu Sans, Arial, Helvetica, sans-serif;font-size: 12px;'><b>PASSED</span></b></td>";
									if(testCaseObj.getString("job_id").equals(Integer.toString(testRunJobId))&&testCaseObj.getString("result").equalsIgnoreCase("FAILED")){
										passedOrFailedJob = "<td style='font-weight: bold'> : <span style='color:#c80000;font-family: DejaVu Sans, Arial, Helvetica, sans-serif;font-size: 12px;'><b>FAILED</span></b></td>";
										jobresult="FAILED";	
									}		
								}
							}		
							log.info("jobresult is "+jobresult);
							if((jsonReportSummaryObject.getString("deviceid").equalsIgnoreCase("N/A")&&jsonReportSummaryObject.getString("devicemodel").equalsIgnoreCase("N/A"))&&jsonReportSummaryObject.getString("deviceplatformname").equalsIgnoreCase("N/A")&&jsonReportSummaryObject.getString("deviceplatformversion").equalsIgnoreCase("N/A")){
								device="<td style='font-weight: bold'> : <span style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; font-weight: bold'>N/A</td>";
								devicePlatForm="<td style='font-weight: bold'> : <span style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; font-weight: bold'>N/A</td>";
							}
							else{
								device="<td style='font-weight: bold'> : <span style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; font-weight: bold'>"+jsonReportSummaryObject.getString("devicemodel")+","+jsonReportSummaryObject.getString("deviceid")+"</span></td>";
								devicePlatForm="<td style='font-weight: bold'> : <span style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; font-weight: bold'>"+jsonReportSummaryObject.getString("deviceplatformname")+" "+jsonReportSummaryObject.getString("deviceplatformversion")+"</span></td>";
							}
							bw.write("<tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px;width:150;'>Product Name </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold' > : "+jsonReportSummaryObject.getString("productname")+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px;width:150;'>Device </th>"+device+"</tr><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Product version	</th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jsonReportSummaryObject.getString("productversionname")+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Device Platform </th>"+devicePlatForm+"</tr><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test Run Plan	</th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jsonReportSummaryObject.getString("testrunconfigurationname")+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test Cases Passed </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jsonReportSummaryObject.getInt("passedtest")+"/"+jsonReportSummaryObject.getInt("totaltest")+"</td></tr><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test Suite	</th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jsonReportSummaryObject.getString("testsuiteid")+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test Steps Passed </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jsonReportSummaryObject.getInt("passedtestecase")+"/"+jsonReportSummaryObject.getInt("totaltestcase")+"</td></tr><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test Start Date & Time </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jsonReportSummaryObject.getString("testrunstarttime")+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test End Date & Time</th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jsonReportSummaryObject.getString("testrunendtime")+"</td></tr><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Host IP</th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jsonReportSummaryObject.getString("hostipaddress")+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test Tool </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jsonReportSummaryObject.getString("testtoolname")+"</td></tr><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Host </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jsonReportSummaryObject.getString("hostname")+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test result	</th>"+passedOrFailedJob.replace("\"", "")+"</tr>");
							bw.write("</table>");
						} catch (JSONException e) {
							log.error("Error :"+e);
						}
						for(int i=0;i<jsonJobReportArray.length();i++){			
							org.json.JSONObject jsonJobReportObject = jsonJobReportArray.getJSONObject(i);					
							if(jsonJobReportObject.getString("job_id").equalsIgnoreCase(Integer.toString(testRunJobId))){
								//Test case summary details			
								String totalTCExecutionTime="";
								String testCaseType="Test Case Code";
								boolean testCaseTypeFlag=true;
								for(int j=0;j<jsonTestCaseSummaryArray.length();j++){			
									org.json.JSONObject testCaseObj =jsonTestCaseSummaryArray.getJSONObject(j);
									if(testCaseObj.getString("job_id").equals(jsonJobReportObject.getString("job_id"))){
										totalTCExecutionTime = testCaseObj.getString("totaltcexectime");
									}
									if(testCaseObj.getString("testcasecode").equalsIgnoreCase("N/A")&&testCaseTypeFlag){
										testCaseType="Test Case Id";
										testCaseTypeFlag=false;
									}
								}
								bw.write("<table width='100%'><tr style='height:13px'></tr><tr style='font-family:DejaVu Sans, Arial, Helvetica,sans-serif;color:#ffffff;background:#003ca4;font-weight: bold;height:26px;font-size: 12px;'><td id=TESTCASESUMMARY"+jsonJobReportObject.getString("job_id")+">TEST CASE SUMMARY</td><td><span style='float:left'>Total Execution Time : "+totalTCExecutionTime+"</span><span style='float:right'><a style='text-decoration:none;font-weight:bold;color:#ffffff;font-size:13px;' href=#End>End</a></span></td></tr>");
								bw.write("<table bgcolor='white' width='100%'>");
								bw.write("<tr style='color:#ffffff;background: #3c6ac6;font-family:Arial;font-size:12px'><th>"+testCaseType+"</th><th>Test Case Name</th><th>Description</th><th>result</th><th>Start Date & Time</th><th>End Date & Time</th><th>Execution Time</th></tr>");

								for(int j=0;j<jsonTestCaseSummaryArray.length();j++){			
									org.json.JSONObject testCaseObj =jsonTestCaseSummaryArray.getJSONObject(j);
									if(testCaseObj.getString("job_id").equals(jsonJobReportObject.getString("job_id"))&&jsonJobReportObject.getString("job_id").equalsIgnoreCase(Integer.toString(testRunJobId))){
										String result = testCaseObj.getString("result");
										String testCaseCode=testCaseObj.getString("testcasecode");
										String passedOrFailed = "";
										if(result.equalsIgnoreCase("PASSED")){
											passedOrFailed = "<td style='color:#00C800;font-family: DejaVu Sans, Arial, Helvetica, sans-serif;font-size: 12px;'><b>"+result+"</b></td>";
										} else {
											passedOrFailed = "<td style='color:#c80000;font-family: DejaVu Sans, Arial, Helvetica, sans-serif;font-size: 12px;'><b>"+result+"</b></td>";				
										}
										if(testCaseCode.equalsIgnoreCase("N/A")){
											testCaseCode=testCaseObj.getString("test_case_id");
										}
										bw.write("<tr><td colspan='7'><hr/></td></tr><tr style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;vertical-align:top'><td>"+testCaseCode+"</td><td><a style='text-decoration: none;font-weight:bold' href=#"+testCaseObj.getString("test_case")+jsonJobReportObject.getString("job_id")+">"+testCaseObj.getString("test_case")+"</a></td><td>"+testCaseObj.getString("description")+"</td>"+passedOrFailed.replace("\"", "")+"<td>"+testCaseObj.getString("starttime")+"</td><td>"+testCaseObj.getString("endtime")+"</td><td>"+testCaseObj.getString("totaltime")+"</td></tr>");
									}						
								}							
								bw.write("</table>");

								//Test Case Details
								bw.write("<table width='100%'><tr></tr><tr align='left' style='font-family:DejaVu Sans, Arial, Helvetica,sans-serif;color:#ffffff;background:#003ca4;font-weight: bold;height:26px;font-size: 12px'><td>TEST CASE DETAILS</td></tr></table>");
								for(int k=0;k<jsonTestCaseSummaryArray.length();k++){			
									org.json.JSONObject testCaseObj =jsonTestCaseSummaryArray.getJSONObject(k);				
									String testcasename = testCaseObj.getString("test_case");
									if(testCaseObj.getString("job_id").equals(jsonJobReportObject.getString("job_id"))&&jsonJobReportObject.getString("job_id").equalsIgnoreCase(Integer.toString(testRunJobId))){
										//Test case name with description
										bw.write("<table width='100%'>");
										bw.write("<tr style='font-family:Arial;color:#ffffff;font-size:12px;'><th colspan='4' align='left' id="+testCaseObj.getString("test_case")+jsonJobReportObject.getString("job_id")+">Test Case Name  	:"+testCaseObj.getString("test_case")+"</th><th><span style='float:right'><a style='text-decoration:none;font-weight:bold;font-size:13px;color:ffffff;' href=#TESTCASESUMMARY"+jsonJobReportObject.getString("job_id")+">"+"Test Case Summary | "+"</a><a style='text-decoration:none;font-weight:bold;font-size:13px;color:ffffff;' href=#TOP>Beginning | </a><a style='text-decoration:none;font-weight:bold;font-size:13px;color:ffffff;' href=#End>End</a></span></th></tr></table>");
										String testCaseResultStatus = testCaseObj.getString("result");
										String passedOrFailedTestCase = "";
										if(testCaseResultStatus.equalsIgnoreCase("PASSED")){
											passedOrFailedTestCase = "<td style='font-weight: bold'> : <span style='font-family: DejaVu Sans, Arial, Helvetica, sans-serif;color: #00C800;font-size: 12px;font-weight: bold'>"+testCaseResultStatus+"</span></td>";
										} else {
											passedOrFailedTestCase = "<td style='font-weight: bold'> : <span style='font-family: DejaVu Sans, Arial, Helvetica, sans-serif;color: #c80000;font-size: 12px;font-weight: bold'>"+testCaseResultStatus+"</span></td>";				
										}
										bw.write("<table width='100%' style='table-layout: fixed;'><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px;width:150;'>Test Case description </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;height:17px; font-weight: bold' > : "+testCaseObj.getString("description")+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px;width:150;'>Test Case Code/ID </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;height:17px; font-weight: bold' > : "+testCaseObj.getString("testcasecode")+"/"+testCaseObj.getString("test_case_id")+"</td></tr><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Start Date & Time</th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;font-weight: bold'> : "+testCaseObj.getString("starttime")+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>End Date & Time </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+testCaseObj.getString("endtime")+"</td></tr><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test Case Result </th>"+passedOrFailedTestCase.replace("\"", "")+"<th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test Steps Passed </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+testCaseObj.getInt("passedteststep")+"/"+testCaseObj.getInt("totalteststep")+"</td></tr><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Features Covered </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;height:17px; font-weight: bold' > : "+testCaseObj.getString("productfeaturename")+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Risks Covered </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;height:17px; font-weight: bold' > : "+testCaseObj.getString("riskname")+"</td></tr><tr style='height:13px'></tr></table>");
										//Test Step Details

										String testStepType="Teststepcode";
										boolean testStepTypeFlag=true;
										for(int j=0;j<jsonTestStepArray.length();j++){
											org.json.JSONObject testStepObj =jsonTestStepArray.getJSONObject(j);
											if(testStepObj.getString("teststepcode").equalsIgnoreCase("N/A")&&testStepTypeFlag){
												testStepType = "Teststepid";
												testStepTypeFlag = false;
											}
										}
										bw.write("<table width='100%' style='table-layout: fixed;'><tr style='color:white;background: #3c6ac6;font-family: Arial;font-size: 12px;height:18px'><th width='8%'>"+testStepType+"</th><th width='10%'>teststepdescription</th><th width='15%'>Expected Result</th><th width='15%'>Observed Result</th><th width='7%'>Status</th><th width='10%'>Execution Time</th><th width='15%'>Comments</th><th width='20%'>Screenshot</th></tr>");										
										for(int j=0;j<jsonTestStepArray.length();j++){
											org.json.JSONObject testStepObj =jsonTestStepArray.getJSONObject(j);
											String testStepResultStatus = testStepObj.getString("resultstatus");
											String passedOrFailedTestStep = "";
											String failureTestStepComments="";
											String testStepCode=testStepObj.getString("teststepcode");

											if(testStepResultStatus.equalsIgnoreCase("PASSED")){
												passedOrFailedTestStep = "<td style='font-family: DejaVu Sans, Arial, Helvetica, sans-serif;color: #00C800;font-size: 12px;font-weight: bold;' width='5%'>"+testStepResultStatus+"</td>";
												//passedOrFailedTestStep = "<span style='font-family: DejaVu Sans, Arial, Helvetica, sans-serif;color: #00C800;font-size: 12px;font-weight: bold;'>"+testStepResultStatus+"</span>";
											} else {
												passedOrFailedTestStep = "<td style='font-family: DejaVu Sans, Arial, Helvetica, sans-serif;color: #c80000;font-size: 12px;font-weight: bold;' width='5%'>"+testStepResultStatus+"</td>";
												//passedOrFailedTestStep = "<span style='font-family: DejaVu Sans, Arial, Helvetica, sans-serif;color: #c80000;font-size: 12px;font-weight: bold;'>"+testStepResultStatus+"</span>";
											}
											if(testStepObj.getString("teststepcode").equalsIgnoreCase("N/A")){
												testStepCode=testStepObj.getString("teststepid");
											}

											if(testStepObj.getString("testcasename").equalsIgnoreCase(testcasename) && testStepObj.getString("job_id").equals(jsonJobReportObject.getString("job_id"))&&jsonJobReportObject.getString("job_id").equalsIgnoreCase(Integer.toString(testRunJobId))){	
												if(!testStepObj.getString("failurereason").equalsIgnoreCase("N/A")){
													failureTestStepComments=testStepObj.getString("failurereason");
													log.info("test step comments"+failureTestStepComments);
												}
												String testStep=testStepObj.getString("screenshot");
												if(new File(testStep).exists()){												
													testStepPath.add(testStep);
													testStep = testStep.substring(testStep.lastIndexOf("\\")+1, testStep.length());
													closeImagePath = closeImagePath.substring(closeImagePath.lastIndexOf("\\")+1, closeImagePath.length());
													bw.write("<tr><td colspan='8'><hr/></td></tr><tr style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;vertical-align:top'><td width='5%' style='word-wrap: break-word;'>"+testStepCode+"</td><td width='10%' style='word-wrap: break-word;'>"+testStepObj.getString("teststepdescription")+"</td><td width='15%' style='word-wrap: break-word;'>"+testStepObj.getString("expectedoutput")+"</td><td width='15%' style='word-wrap: break-word;'>"+testStepObj.getString("observedoutput")+"</td>"+passedOrFailedTestStep.replace("\"", "")+"<td align='middle' width='10%' style='word-wrap: break-word;'>"+testStepObj.getString("totalteststepstime")+"</td><td width='20%' style='word-wrap: break-word;'>"+failureTestStepComments+"</td><td width='20%' style='word-wrap: break-word;'><a href='javascript:void(0);'><img height='100' width='200' src='screenshots/"+testStep+"' onclick=\"display('screenshots/"+testStep+"')\";/></a><div id=\"tag\" class=\"parentdiv\" style=\"display:none;\"><div id=\"childdiv\" class=\"center\"><a href='javascript:void(0);'><img align='right' src='screenshots/"+closeImagePath+"' height=\"20\" width=\"20;\" alt='Close' onclick=\"document.getElementById('tag').style.display='none';\"/></a><img id=\"f\" height=\"100%\" width=\"70%\"/></div></div></td></tr>");
													//bw.write("<tr><td colspan='8'><hr/></td></tr><tr style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;vertical-align:top'><td width='5%' style='word-wrap: break-word;'>"+testStepCode+"</td><td width='45%' style='word-wrap: break-word;'><pre style=\"font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;vertical-align:top; white-space: pre-wrap; word-wrap: break-word;\"><b>description:</b> <br/><br/>    "+testStepObj.getString("teststepdescription")+"<br/><br/><b>Expected : </b><br/><br/>    "+testStepObj.getString("expectedoutput")+"<br/><br/><b>Observed : </b><br/><br/>    "+testStepObj.getString("observedoutput")+"<br/><br/><b>Status : </b><br/><br/>    "+passedOrFailedTestStep.replace("\"", "")+"<br/><br/><b>Comments : </b><br/><br/>    "+failureTestStepComments+"<br/></pre></td><td width='55%' style='word-wrap: break-word;'><a href='javascript:void(0);'><img height='400' width='700' src='screenshots/"+testStep+"' onclick=\"display('screenshots/"+testStep+"')\";/></a><div id=\"tag\" class=\"parentdiv\" style=\"display:none;\"><div id=\"childdiv\" class=\"center\"><a href='javascript:void(0);'><img align='right' src='screenshots/"+closeImagePath+"' height=\"20\" width=\"20;\" alt='Close' onclick=\"document.getElementById('tag').style.display='none';\"/></a><img id=\"f\" height=\"100%\" width=\"70%\"/></div></div></td></tr>");
												} else {
													bw.write("<tr><td colspan='8'><hr/></td></tr><tr style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;vertical-align:top'><td width='5%' style='word-wrap: break-word;'>"+testStepCode+"</td><td width='10%' style='word-wrap: break-word;'>"+testStepObj.getString("teststepdescription")+"</td><td width='15%' style='word-wrap: break-word;'>"+testStepObj.getString("expectedoutput")+"</td><td width='15%' style='word-wrap: break-word;'>"+testStepObj.getString("observedoutput")+"</td>"+passedOrFailedTestStep.replace("\"", "")+"<td align='middle' width='10%' style='word-wrap: break-word;'>"+testStepObj.getString("totalteststepstime")+"</td><td width='20%' style='word-wrap: break-word;'>"+failureTestStepComments+"</td><td width='20%' style='word-wrap: break-word;'>No screenshot available for test step</td></tr>");
													//bw.write("<tr><td colspan='8'><hr/></td></tr><tr style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;vertical-align:top'><td width='5%' style='word-wrap: break-word;'>"+testStepCode+"</td><td width='45%' style='word-wrap: break-word;'><pre style=\"font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;vertical-align:top; white-space: pre-wrap; word-wrap: break-word;\"><b>description:</b> <br/><br/>    "+testStepObj.getString("teststepdescription")+"<br/><br/><b>Expected : </b><br/><br/>    "+testStepObj.getString("expectedoutput")+"<br/><br/><b>Observed : </b><br/><br/>    "+testStepObj.getString("observedoutput")+"<br/><br/><b>Status : </b><br/><br/>    "+passedOrFailedTestStep.replace("\"", "")+"<br/><br/><b>Comments : </b><br/><br/>    "+failureTestStepComments+"<br/></pre></td><td width='55%' style='word-wrap: break-word;'><a href='javascript:void(0);'><img height='400' width='700' src='screenshots/"+testStep+"' onclick=\"display('screenshots/"+testStep+"')\";/></a><div id=\"tag\" class=\"parentdiv\" style=\"display:none;\"><div id=\"childdiv\" class=\"center\"><a href='javascript:void(0);'><img align='right' src='screenshots/"+closeImagePath+"' height=\"20\" width=\"20;\" alt='Close' onclick=\"document.getElementById('tag').style.display='none';\"/></a><img id=\"f\" height=\"100%\" width=\"70%\"/></div></div></td></tr>");
												}
											}
										}
										bw.write("</table>");									
									}				
								}
							}
						}
						bw.write("<div id='End'><span style='float:right;font-weight:bold;font-family:DejaVu Sans, Arial, Helvetica,sans-serif;font-size:13px'><a style='text-decoration:none;' href='#' onclick='window.history.go(-1);return false'>Go Back</a> | <a style='text-decoration:none;' href='#TOP'>Beginning</a></span></div>");
						bw.write("</body></html>");
						bw.close();	

						try{
							File screenshotFolder = null;
							File logoFolder  = null;
							FileOutputStream fos = new FileOutputStream(htmlReportGenLocation+File.separator+"JobReport"+File.separator+productName+"-JR-"+testRunJobId+".zip");				    		
							ZipOutputStream zos = new ZipOutputStream(fos);
							List<String> screenshotImages = new ArrayList<String>();
							String closeScreenshotImage  = CommonUtility.getCatalinaPath() +File.separator + "webapps" + File.separator + request.getContextPath() + File.separator + "css" + File.separator + "images" +File.separator + "close_screenshot_image.jpg";
							screenshotImages.add(closeScreenshotImage);
							screenshotImages.addAll(testStepPath);
							List<String> logoImage = new ArrayList<String>();
							logoImage.add(imageServerPath);
							List<String> names = new ArrayList<String>();
							names.add(JobLevelHTMLFIle);
							for(String name : names){				    			
								if(new File(name).exists()){
									zos.putNextEntry(new ZipEntry(new File(name).getName()));
									FileInputStream in = new FileInputStream(name);					    			
									if(in != null){
										int len;
										while ((len = in.read(buffer)) > 0) {
											zos.write(buffer, 0, len);
										}						    		
										zos.closeEntry();
										in.close();
									}			
								}
							}	
							for(String name : screenshotImages){
								log.info(name);
								screenshotFolder  = new File(htmlReportGenLocation+File.separator+"JobReport"+File.separator+productName+"-JR-"+testRunJobId+File.separator+"screenshots");
								if(!screenshotFolder.exists()){
									screenshotFolder.mkdirs();				    				
								}
								File screenshotFile = new File(name);
								if(screenshotFile.exists()){
									FileUtils.copyFile(screenshotFile, new File(screenshotFolder+File.separator+screenshotFile.getName()));
								}
							}	
							for(String name : logoImage){	
								log.info("logo path is :"+name);
								logoFolder  = new File(htmlReportGenLocation+File.separator+"JobReport"+File.separator+productName+"-JR-"+testRunJobId+File.separator+"logo");
								if(!logoFolder.exists()){
									logoFolder.mkdirs();				    				
								}
								File logoFile = new File(name);
								if(logoFile.exists()){
									FileUtils.copyFile(logoFile, new File(logoFolder+File.separator+logoFile.getName()));
								}
							}	
							zipDir(zos,"",screenshotFolder);
							zipDir(zos,"",logoFolder);				    		
							zos.close();				    		
						}catch(IOException ex){
							log.info("no data " + ex);
							if(testRunJobId!=-1){				
								jTableResponse = new JTableResponse("ERROR","No records available for the current Job "+testRunJobId);
							} else if(testRunNo != -1){
								jTableResponse = new JTableResponse("ERROR","No records available for the current Workpackage "+testRunNo);
							}
							return jTableResponse;
						}
						log.info("JobLevelHTMLFIle is"+JobLevelHTMLFIle);
						JobLevelHTMLFIle = htmlReportGenLocation+File.separator+"JobReport"+File.separator+productName+"-JR-"+testRunJobId+".zip";						
					} catch (IOException | JSONException e) {		
						log.info("no data " +e);
						if(testRunJobId!=-1){				
							jTableResponse = new JTableResponse("ERROR","No records available for the current Job "+testRunJobId);
						} else if(testRunNo != -1){
							jTableResponse = new JTableResponse("ERROR","No records available for the current Workpackage "+testRunNo);
						}
						return jTableResponse;
					} 					
				} else {
					log.info("no data ");
					if(testRunJobId!=-1){				
						jTableResponse = new JTableResponse("ERROR","No records available for the current Job "+testRunJobId);
					} else if(testRunNo != -1){
						jTableResponse = new JTableResponse("ERROR","No records available for the current Workpackage "+testRunNo);
					}
				} 

			}else{
				log.info("no data ");
				if(testRunJobId!=-1){				
					jTableResponse = new JTableResponse("ERROR","No records available for the current Job "+testRunJobId);
				} else if(testRunNo != -1){
					jTableResponse = new JTableResponse("ERROR","No records available for the current Workpackage "+testRunNo);
				}
			}

		}
		if (productTypeName.equalsIgnoreCase("WORKPACKAGE")){
			if(customerName.equalsIgnoreCase("Rockwell Collins")) {
				jasperPrint=report.generateTestRunListRCReport(testRunNo, testRunConfigurationChildId, strPrintMode, productType, logo, loginUserName);
			} else {					
				List<String>htmlReportData=new ArrayList<String>();
				log.info("Going to TestRunListHtmlReport : " + testRunNo + " : " + testRunConfigurationChildId+"  :  "+productTypeName);
				htmlReportData=report.generateTestRunListHtmlReport(testRunNo, testRunConfigurationChildId,strPrintMode,productType,logo, loginUserName);
				log.info("after getting the html report data");
				if(!htmlReportData.isEmpty()){
					for(int i=0;i<htmlReportData.size();i++){
						jsonSourceFile=htmlReportData.get(0);
						jsonSourceSubFile1=htmlReportData.get(1);
						jsonSourceSubFile2=htmlReportData.get(2);
						jsonSourceSubFile3=htmlReportData.get(3);
						jsonSourceSubFile4=htmlReportData.get(4);
					}
					if(new org.json.JSONArray(htmlReportData.get(1)).length() > 0){
						org.json.JSONArray jsonReportSummaryArray=new org.json.JSONArray(jsonSourceFile);
						org.json.JSONArray jsonJobReportArray=new org.json.JSONArray(jsonSourceSubFile1);
						org.json.JSONArray jsonJobSummaryArray=new org.json.JSONArray(jsonSourceSubFile2);
						org.json.JSONArray jsonTestCaseSummaryArray=new org.json.JSONArray(jsonSourceSubFile3);
						org.json.JSONArray jsonTestStepArray=new org.json.JSONArray(jsonSourceSubFile4);
						log.info("jsonReportSummaryArray values are :"+jsonReportSummaryArray);
						log.info("jsonJobReportArray values are :"+jsonJobReportArray);
						log.info("jsonJobSummaryArray values are :"+jsonJobSummaryArray);
						log.info("jsonTestCaseSummaryArray values are :"+jsonTestCaseSummaryArray);
						log.info("jsonTestStepArray values are :"+jsonTestStepArray);	
						try{
							log.debug("workpackageName is"+workpackageName);
							List<String>testStepPath=new ArrayList<String>();
							Map<String,String>jobResultWithJobId=new HashMap<String, String>();
							for(int i=0;i<jsonJobSummaryArray.length();i++){
								org.json.JSONObject jobSummaryObject =jsonJobSummaryArray.getJSONObject(i);
								jobResultWithJobId.put(jobSummaryObject.getString("testRunListId"), jobSummaryObject.getString("testCaseResult"));
							}
							log.info("jobResultWithJobId"+jobResultWithJobId);
							workpackageHtmlFile = htmlReportGenLocation+File.separator+"HtmlReport"+File.separator+productName+"-WP-"+testRunNo+".html";
							File htmFile = new File(workpackageHtmlFile);
							if(!htmFile.exists()){
								new File(htmFile.getParent()).mkdirs();
							}
							byte[] buffer = new byte[1024];
							BufferedWriter bw = new BufferedWriter(new FileWriter(new File(workpackageHtmlFile)));	 
							HashMap<String, Object> map = new HashMap<String, Object>();
							bw.write("<html><head><META http-equiv=Content-Type content='text/html; charset=utf-8'><style>.center {margin: auto;width: 75%;border: 3px solid #473572;padding: 10px;position : fixed;background:white;height:60%;}.parentdiv {margin: auto; width: 80%;height:50%;border: 3px;padding: 10px;position : fixed;top:20%;left:8%;right:auto;}body {background-color: #FFFFFF;}th{text-align: left;color:white;background: #3c6ac6;}a {color:blue;} a:visited {color:blue;}a:hover {color:blue;}a:active {color:blue;}</style><title>Workpackage Report</title><script>function display(e){document.getElementById(\"tag\").style.display='';var img = document.getElementById(\"f\");img.setAttribute(\"src\",e);document.getElementById(\"childdiv\").appendChild(img);return false;}</script></head><body>");
							try {		
								org.json.JSONObject jsonObject = jsonReportSummaryArray.getJSONObject(0);	
								bw.write("<table width='100%'><tr style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #FFFFFF; font-size: 24px; height:62px;background:#003ca4; font-weight: bold;'><td><img style=\"width:50px;height:50px;\" src='logo/"+imageFile.getName()+"'/></td> <td align='middle' id='TOP'>Work Package Report - "+jsonObject.getString("productName")+" - "+jsonObject.getString("testRunconfigurationName")+"</td></tr><tr style='height:13px'></tr></table>");		
								bw.write("<table bgcolor='white' width='100%' style='table-layout: fixed;'>");
								//report summary
								String result = jsonObject.getString("testCaseResult");
								String passedOrFailed = "";
								if(result.equalsIgnoreCase("PASSED")){
									passedOrFailed = "<td style='font-weight: bold'> : <span style='font-family: DejaVu Sans, Arial, Helvetica, sans-serif;color: #00C800;font-size: 12px;font-weight: bold'> "+result+"</span></td>";
								} else {
									passedOrFailed = "<td style='font-weight: bold'> : <span style='font-family: DejaVu Sans, Arial, Helvetica, sans-serif;color: #c80000;font-size: 12px;font-weight: bold'> "+result+"</span></td>";
								}
								bw.write("<tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px;width:150;'>Product Name </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold' > : "+jsonObject.getString("productName")+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px;width:150;'>Work Package No </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jsonObject.getString("testRunNo")+"</td></tr><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Product version	</th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jsonObject.getString("productVersionName")+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Triggered Time </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jsonObject.getString("testRunTriggeredTime")+"</td></tr><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test Run Plan	</th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jsonObject.getString("testRunconfigurationName")+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test Cases Passed </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jsonObject.getInt("passedTest")+"/"+jsonObject.getInt("totalTest")+"</td></tr><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test Suite	</th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jsonObject.getString("testSuiteId")+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test Steps Passed </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jsonObject.getInt("passedTestecase")+"/"+jsonObject.getInt("totalTestCase")+"</td></tr><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test Start Date & Time </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jsonObject.getString("testRunStartTime")+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test End Date & Time </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jsonObject.getString("testRunEndTime")+"</td></tr><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Host </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jsonObject.getString("hostName")+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test Result	</th>"+passedOrFailed.replace("\"", "")+"</tr>");

							} catch (JSONException e) {
								log.info("Error while iterating source file json data "+e);
							}
							bw.write("</table>");
							//Job Report Details					
							bw.write("<table width='100%' style='table-layout: fixed;'><tr></tr><tr align='left' style='font-family:DejaVu Sans, Arial, Helvetica,sans-serif;color:#ffffff;background:#003ca4;font-weight: bold;height:25px'><td id='JOBREPORT'>JOB REPORT<span style='float:right'><a style='text-decoration:none;font-weight:bold;color:#ffffff;font-size:13px;' href='#End'>End</a></span></td></tr></table>");		
							bw.write("<table width='100%' style='table-layout: fixed;'>");
							bw.write("<tr style='color:#ffffff;height:18px;font-family: Arial;background: #3c6ac6;font-size: 12px; font-weight: bold'><th>Job ID</th><th>Environment</th><th>Device ID</th><th>Device Platform</th><th>Status</th><th>Result</th><th>Start Date & Time </th><th>End Date & Time</th><th>Comments</th></tr>");
							String job_id="";
							for(int i=0;i<jsonJobReportArray.length();i++){			
								org.json.JSONObject jsonJobReportObject = jsonJobReportArray.getJSONObject(i);
								String jobResult = jsonJobReportObject.getString("testResultStatus");
								String jobStatus=jsonJobReportObject.getString("Result");
								String passedOrFailedJobResult = "",passedOrFailedJobStatus="";
								if(jobStatus.equalsIgnoreCase("5")){
									passedOrFailedJobStatus = "<td>Completed</td>";
								}
								else if(jobStatus.equalsIgnoreCase("7")){
									passedOrFailedJobStatus = "<td>Aborted</td>";
								}
								for(Map.Entry<String, String> entry : jobResultWithJobId.entrySet()){
									if(entry.getKey().equalsIgnoreCase(jsonJobReportObject.getString("job_id"))){
										passedOrFailedJobResult=entry.getValue();
										if(passedOrFailedJobResult.equalsIgnoreCase("PASSED")){
											passedOrFailedJobResult = "<td style='color:#00C800;font-family: DejaVu Sans, Arial, Helvetica, sans-serif;font-size: 12px;'><b>"+passedOrFailedJobResult+"</b></td>";
										} else {
											passedOrFailedJobResult = "<td style='color:#c80000;font-family: DejaVu Sans, Arial, Helvetica, sans-serif;font-size: 12px;'><b>"+passedOrFailedJobResult+"</b></td>";				
										}	 
										bw.write("<tr><td colspan='9'><hr/></td></tr><tr style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;vertical-align:top'><td><a style='text-decoration: none;font-weight:bold' href=#"+jsonJobReportObject.getString("job_id")+">"+jsonJobReportObject.getString("job_id")+"</a></td><td>"+jsonJobReportObject.getString("job_name")+"</td><td>"+jsonJobReportObject.getString("device_id")+"</td><td align='middle'>"+jsonJobReportObject.getString("device_platform")+"</td>"+passedOrFailedJobStatus.replace("\"", "")+passedOrFailedJobResult.replace("\"", "")+"<td>"+jsonJobReportObject.getString("start_time")+"</td><td>"+jsonJobReportObject.getString("end_time")+"</td><td>"+jsonJobReportObject.getString("comments")+"</td></tr>");	
									}
								}
							}						
							bw.write("</table>");
							//job report part is completed 
							for(int i=0;i<jsonJobReportArray.length();i++){			
								org.json.JSONObject jsonJobReportObject = jsonJobReportArray.getJSONObject(i);					
								for(int l=0;l<jsonJobReportArray.length();l++){							
									org.json.JSONObject jobSummaryObject = jsonJobSummaryArray.getJSONObject(l);							
									if(jsonJobReportObject.getString("job_id").equals(jobSummaryObject.getString("testRunListId"))){
										//Job Summary details
										bw.write("<table width='100%' style='table-layout: fixed;'><tr style='height:10px'></tr><tr align='left' style='font-family:DejaVu Sans, Arial, Helvetica,sans-serif;color:#ffffff;background:#003ca4;font-weight: bold;height:27px'><td id=JOBSUMMARY"+jsonJobReportObject.getString("job_id")+">JOB SUMMARY<span style='float:right;'><a style='text-decoration:none;font-weight:bold;font-size:13px;color:#ffffff;' href=#JOBREPORT>Job Report | </a><a style='text-decoration:none;font-weight:bold;font-size:13px;color:#ffffff;' href=#TOP>Beginning | </a><a style='text-decoration:none;font-weight:bold;font-size:13px;color:#ffffff;' href=#End>End</a></span></td></tr>");
										bw.write("<table width='100%' style='table-layout: fixed;'><tr style='font-family: DejaVu Sans, Arial, Helvetica, sans-serif;height:25px;background: #3c6ac6; color: #FFFFFF; font-size: 12px;'><td id="+jsonJobReportObject.getString("job_id")+">Job No : "+jsonJobReportObject.getString("job_id")+"</td><td>Environment : "+jsonJobReportObject.getString("job_name")+"</td><td>Device ID : "+jsonJobReportObject.getString("device_id")+"</td><td>Device Platform : "+jsonJobReportObject.getString("device_platform")+"</td><td>Host Platform : "+jsonJobReportObject.getString("hostIpAddress")+"</td></tr></table>");
										bw.write("<table width='100%' style='table-layout: fixed;'>");
										String jobResultStatus = jobSummaryObject.getString("testCaseResult");
										String passedOrFailedJobStatus = "";
										String device="",devicePlatForm="";
										if(jobResultStatus.equalsIgnoreCase("PASSED")){
											passedOrFailedJobStatus = "<td style='font-weight: bold'> : <span style='font-family: DejaVu Sans, Arial, Helvetica, sans-serif;color: #00C800;font-size: 12px;font-weight: bold'> "+jobResultStatus+"</span></td>";
										} else {
											passedOrFailedJobStatus = "<td style='font-weight: bold'> : <span style='font-family: DejaVu Sans, Arial, Helvetica, sans-serif;color: #c80000;font-size: 12px;font-weight: bold'> "+jobResultStatus+"</span></td>";						
										}
										if((jobSummaryObject.getString("deviceId").equalsIgnoreCase("N/A")&&jobSummaryObject.getString("deviceModel").equalsIgnoreCase("N/A"))&&jobSummaryObject.getString("devicePlatformName").equalsIgnoreCase("N/A")&&jobSummaryObject.getString("devicePlatformVersion").equalsIgnoreCase("N/A")){
											device="<td style='font-weight: bold'> : <span style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; font-weight: bold'>N/A</td>";
											devicePlatForm="<td style='font-weight: bold'> : <span style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; font-weight: bold'>N/A</td>";
										}
										else{
											device="<td style='font-weight: bold'> : <span style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; font-weight: bold'>"+jobSummaryObject.getString("deviceModel")+","+jobSummaryObject.getString("deviceId")+"</span></td>";
											devicePlatForm="<td style='font-weight: bold'> : <span style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; font-weight: bold'>"+jobSummaryObject.getString("devicePlatformName")+" "+jobSummaryObject.getString("devicePlatformVersion")+"</span></td>";
										}
										bw.write("<tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px;width:150;'>Product Name </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;height:17px; font-weight: bold' > : "+jobSummaryObject.getString("productName")+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px;width:150;'>Device </th>"+device+"</tr><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Product version	</th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;font-weight: bold'> : "+jobSummaryObject.getString("productVersionName")+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Device Platform </th>"+devicePlatForm+"</tr><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test Run Plan	</th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jobSummaryObject.getString("testRunconfigurationName")+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test Cases Passed </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jobSummaryObject.getInt("passedTest")+"/"+jobSummaryObject.getInt("totalTest")+"</td></tr><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test Suite	</th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jobSummaryObject.getString("testSuiteName")+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test Steps Passed </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jobSummaryObject.getInt("passedTestecase")+"/"+jobSummaryObject.getInt("totalTestCase")+"</td></tr><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test Start Date & Time </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jobSummaryObject.getString("testRunStartTime")+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test End Date & Time </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jobSummaryObject.getString("testRunEndTime")+"</td></tr><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test Tool </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jobSummaryObject.getString("testToolName")+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test Result	</th>"+passedOrFailedJobStatus.replace("\"", "")+"</tr>");
										bw.write("</table>");
									}
								}
								//Test case summary details			
								String totalTCExecutionTime="";
								String testCaseId="";
								String testCaseType="Test Case Code";
								boolean testCaseTypeFlag=true;
								for(int j=0;j<jsonTestCaseSummaryArray.length();j++){			
									org.json.JSONObject testCaseObj =jsonTestCaseSummaryArray.getJSONObject(j);
									if(testCaseObj.getString("job_id").equals(jsonJobReportObject.getString("job_id"))){
										totalTCExecutionTime = testCaseObj.getString("totalTCExecTime");
									}
									if(testCaseObj.getString("testCaseCode").equalsIgnoreCase("N/A")&&testCaseTypeFlag){
										testCaseType="Test Case Id";
										testCaseTypeFlag=false;
									}
								}
								bw.write("<table width='100%'><tr style='height:13px'></tr><tr style='font-family:DejaVu Sans, Arial, Helvetica,sans-serif;color:#ffffff;background:#003ca4;font-weight: bold;height:26px;font-size: 12px;'><td id=TESTCASESUMMARY"+jsonJobReportObject.getString("job_id")+">TEST CASE SUMMARY</td><td><span style='float:left'>Total Execution Time : "+totalTCExecutionTime+"</span><span style='float:right'><a style='text-decoration:none;font-weight:bold;color:#ffffff;font-size:13px;' href=#JOBSUMMARY"+jsonJobReportObject.getString("job_id")+">"+"Job Summary | "+"</a><a style='text-decoration:none;font-weight:bold;color:#ffffff;font-size:13px;' href=#JOBREPORT>Job Report | </a><a style='text-decoration:none;font-weight:bold;color:#ffffff;font-size:13px;' href=#TOP>Beginning | </a><a style='text-decoration:none;font-weight:bold;color:#ffffff;font-size:13px;' href=#End>End</a></span></td></tr>");
								bw.write("<table bgcolor='white' width='100%'>");		
								bw.write("<tr style='color:#ffffff;background: #3c6ac6;font-family:Arial;font-size:12px'><th>"+testCaseType+"</th><th>Test Case Name</th><th>Description</th><th>Result</th><th>Start Date & Time</th><th>End Date & Time</th><th>Execution Time</th></tr>");
								for(int j=0;j<jsonTestCaseSummaryArray.length();j++){			
									org.json.JSONObject testCaseObj =jsonTestCaseSummaryArray.getJSONObject(j);
									if(testCaseObj.getString("job_id").equals(jsonJobReportObject.getString("job_id"))){
										String result = testCaseObj.getString("Result");
										String passedOrFailed = "";
										if(result.equalsIgnoreCase("PASSED")){
											passedOrFailed = "<td style='color:#00C800;font-family: DejaVu Sans, Arial, Helvetica, sans-serif;font-size: 12px;'><b>"+result+"</b></td>";
										} else {
											passedOrFailed = "<td style='color:#c80000;font-family: DejaVu Sans, Arial, Helvetica, sans-serif;font-size: 12px;'><b>"+result+"</b></td>";				
										}
										if(testCaseObj.getString("testCaseCode").equalsIgnoreCase("N/A")){
											testCaseId=testCaseObj.getString("Test_Case_Id");
										}
										else{
											testCaseId=testCaseObj.getString("testCaseCode");
										}
										bw.write("<tr><td colspan='7'><hr/></td></tr><tr style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;vertical-align:top'><td>"+testCaseId+"</td><td><a style='text-decoration: none;font-weight:bold' href=#"+testCaseObj.getString("Test_Case")+jsonJobReportObject.getString("job_id")+">"+testCaseObj.getString("Test_Case")+"</a></td><td>"+testCaseObj.getString("Description")+"</td>"+passedOrFailed.replace("\"", "")+"<td>"+testCaseObj.getString("StartTime")+"</td><td>"+testCaseObj.getString("EndTime")+"</td><td>"+testCaseObj.getString("totalTime")+"</td></tr>");
									}						
								}							
								bw.write("</table>");							
								//Test Case Details
								bw.write("<table width='100%'><tr></tr><tr align='left' style='font-family:DejaVu Sans, Arial, Helvetica,sans-serif;color:#ffffff;background:#003ca4;font-weight: bold;height:26px;font-size: 12px'><td>TEST CASE DETAILS</td></tr></table>");
								for(int k=0;k<jsonTestCaseSummaryArray.length();k++){			
									org.json.JSONObject testCaseObj =jsonTestCaseSummaryArray.getJSONObject(k);				
									String testCaseName = testCaseObj.getString("Test_Case");
									if(testCaseObj.getString("job_id").equals(jsonJobReportObject.getString("job_id"))){
										//Test case with description
										bw.write("<table width='100%'>");
										bw.write("<tr style='font-family:Arial;color:#ffffff;font-size:12px;'><th colspan='4' align='left' id="+testCaseObj.getString("Test_Case")+jsonJobReportObject.getString("job_id")+">Test Case Name  	:"+testCaseObj.getString("Test_Case")+"</th><th><span style='float:right'><a style='text-decoration:none;font-weight:bold;font-size:13px;color:ffffff;' href=#TESTCASESUMMARY"+jsonJobReportObject.getString("job_id")+">"+"Test Case Summary | "+"</a><a style='text-decoration:none;font-weight:bold;font-size:13px;color:ffffff;' href=#JOBSUMMARY"+jsonJobReportObject.getString("job_id")+">"+"Job Summary | "+"</a><a style='text-decoration:none;font-weight:bold;font-size:13px;color:ffffff;' href=#JOBREPORT>Job Report | </a><a style='text-decoration:none;font-weight:bold;font-size:13px;color:ffffff;' href=#TOP>Beginning | </a><a style='text-decoration:none;font-weight:bold;font-size:13px;color:ffffff;' href=#End>End</a></span></th></tr></table>");
										String testCaseResultStatus = testCaseObj.getString("Result");
										String passedOrFailedTestCase = "";
										if(testCaseResultStatus.equalsIgnoreCase("PASSED")){
											passedOrFailedTestCase = "<td style='font-weight: bold'> : <span style='font-family: DejaVu Sans, Arial, Helvetica, sans-serif;color: #00C800;font-size: 12px;font-weight: bold'>"+testCaseResultStatus+"</span></td>";
										} else {
											passedOrFailedTestCase = "<td style='font-weight: bold'> : <span style='font-family: DejaVu Sans, Arial, Helvetica, sans-serif;color: #c80000;font-size: 12px;font-weight: bold'>"+testCaseResultStatus+"</span></td>";				
										}
										bw.write("<table width='100%' style='table-layout: fixed;'><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px;width:150;'>Test Case Description </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;height:17px; font-weight: bold' > : "+testCaseObj.getString("Description")+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px;width:150;'>Test Case Code/ID </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;height:17px; font-weight: bold' > : "+testCaseObj.getString("testCaseCode")+"/"+testCaseObj.getString("Test_Case_Id")+"</td></tr><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Start Date & Time</th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;font-weight: bold'> : "+testCaseObj.getString("StartTime")+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>End Date & Time </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+testCaseObj.getString("EndTime")+"</td></tr><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test Case Result </th>"+passedOrFailedTestCase.replace("\"", "")+"<th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test Steps Passed </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+testCaseObj.getInt("passedTestStep")+"/"+testCaseObj.getInt("totalTestStep")+"</td></tr><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Features Covered </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;height:17px; font-weight: bold' > : "+testCaseObj.getString("productFeatureName")+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Risks Covered </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;height:17px; font-weight: bold' > : "+testCaseObj.getString("riskName")+"</td></tr><tr style='height:13px'></tr></table>");
										//Test Step Details
										String testStepType="Test Step Code";
										boolean testStepTypeFlag=true;
										for(int j=0;j<jsonTestStepArray.length();j++){
											org.json.JSONObject testStepObj =jsonTestStepArray.getJSONObject(j);
											if(testStepObj.getString("Test Step Code").equalsIgnoreCase("N/A")&&testStepTypeFlag){
												testStepType="Test Step Id";
												testStepTypeFlag=false;
											}
										}
										bw.write("<table width='100%' style='table-layout: fixed;'><tr style='color:white;background: #3c6ac6;font-family: Arial;font-size: 12px;height:18px'><th width='8%'>"+testStepType+"</th><th width='10%'>Test Step Description</th><th width='15%'>Expected Result</th><th width='15%'>Observed Result</th><th width='7%'>Status</th><th width='10%'>Execution Time</th><th width='15%'>Comments</th><th width='20%'>Screenshot</th></tr>");

										for(int j=0;j<jsonTestStepArray.length();j++){
											org.json.JSONObject testStepObj =jsonTestStepArray.getJSONObject(j);
											String testStepResultStatus = testStepObj.getString("Result Status");
											String passedOrFailedTestStep = "",failureTestStepComments="";
											String testStepId = testStepObj.getString("Test Step Code");;
											if(testStepResultStatus.equalsIgnoreCase("PASSED")){
												passedOrFailedTestStep = "<td style='font-family: DejaVu Sans, Arial, Helvetica, sans-serif;color: #00C800;font-size: 12px;font-weight: bold;' width='5%'>"+testStepResultStatus+"</td>";
											} else {
												passedOrFailedTestStep = "<td style='font-family: DejaVu Sans, Arial, Helvetica, sans-serif;color: #c80000;font-size: 12px;font-weight: bold;' width='5%'>"+testStepResultStatus+"</td>";				
											}
											if(testStepId.equalsIgnoreCase("N/A")){
												testStepId = testStepObj.getString("Test Step Id");
											}

											if(testStepObj.getString("testCaseName").equalsIgnoreCase(testCaseName) && testStepObj.getString("job_id").equals(jsonJobReportObject.getString("job_id"))){					
												String testStep=testStepObj.getString("Screenshot");

												if(!testStepObj.getString("failureReason").equalsIgnoreCase("N/A")){
													failureTestStepComments=testStepObj.getString("failureReason");
													log.info("test step comments"+failureTestStepComments);
												}
												if(new File(testStep).exists()){												
													testStepPath.add(testStep);
													testStep = testStep.substring(testStep.lastIndexOf("\\")+1, testStep.length());
													closeImagePath = closeImagePath.substring(closeImagePath.lastIndexOf("\\")+1, closeImagePath.length());
													bw.write("<tr><td colspan='8'><hr/></td></tr><tr style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;vertical-align:top'><td width='5%' style='word-wrap: break-word;'>"+testStepId+"</td><td width='10%' style='word-wrap: break-word;'>"+testStepObj.getString("Test Step Description")+"</td><td width='15%' style='word-wrap: break-word;'>"+testStepObj.getString("Expected Output")+"</td><td width='15%' style='word-wrap: break-word;'>"+testStepObj.getString("Observed Output")+"</td>"+passedOrFailedTestStep.replace("\"", "")+"<td align='middle' width='10%' style='word-wrap: break-word;'>"+testStepObj.getString("totalTestStepsTime")+"</td><td width='20%' style='word-wrap: break-word;'>"+failureTestStepComments+"</td><td width='20%' style='word-wrap: break-word;'><a href='javascript:void(0);'><img height='100' width='200' src='screenshots/"+testStep+"' onclick=\"display('screenshots/"+testStep+"')\";/></a><div id=\"tag\" class=\"parentdiv\" style=\"display:none;\"><div id=\"childdiv\" class=\"center\"><a href='javascript:void(0);'><img align='right' src='screenshots/"+closeImagePath+"' height=\"20\" width=\"20;\" alt='Close' onclick=\"document.getElementById('tag').style.display='none';\"/></a><img id=\"f\" height=\"100%\" width=\"70%\"/></div></div></td></tr>");
												} else {
													bw.write("<tr><td colspan='8'><hr/></td></tr><tr style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;vertical-align:top'><td width='5%' style='word-wrap: break-word;'>"+testStepId+"</td><td width='10%' style='word-wrap: break-word;'>"+testStepObj.getString("Test Step Description")+"</td><td width='15%' style='word-wrap: break-word;'>"+testStepObj.getString("Expected Output")+"</td><td width='15%' style='word-wrap: break-word;'>"+testStepObj.getString("Observed Output")+"</td>"+passedOrFailedTestStep.replace("\"", "")+"<td align='middle' width='10%' style='word-wrap: break-word;'>"+testStepObj.getString("totalTestStepsTime")+"</td><td width='20%' style='word-wrap: break-word;'>"+failureTestStepComments+"</td><td width='20%' style='word-wrap: break-word;'>No screenshot available for the test step</td></tr>");
												}
											}
										}
										bw.write("</table>");									
									}				
								}	
							}
							bw.write("<div id='End'><span style='float:right;font-weight:bold;font-family:DejaVu Sans, Arial, Helvetica,sans-serif;font-size:13px'><a style='text-decoration:none;' href='#' onclick='window.history.go(-1);return false'>Go Back</a> | <a style='text-decoration:none;' href='#TOP'>Beginning</a></span></div>");

							bw.write("</body></html>");
							bw.close();		
							try{
								File screenshotFolder = null;
								File logoFolder  = null;
								FileOutputStream fos = new FileOutputStream(htmlReportGenLocation+File.separator+"HtmlReport"+File.separator+productName+"-WP-"+testRunNo+".zip");				    		
								ZipOutputStream zos = new ZipOutputStream(fos);
								List<String> screenshotImages = new ArrayList<String>();
								String closeScreenshotImage  = CommonUtility.getCatalinaPath() +File.separator + "webapps" + File.separator + request.getContextPath() + File.separator + "css" + File.separator + "images" +File.separator + "close_screenshot_image.jpg";
								screenshotImages.add(closeScreenshotImage);
								screenshotImages.addAll(testStepPath);
								List<String> logoImage = new ArrayList<String>();
								logoImage.add(imageServerPath);
								List<String> names = new ArrayList<String>();
								names.add(workpackageHtmlFile);
								for(String name : names){				    			
									if(new File(name).exists()){
										zos.putNextEntry(new ZipEntry(new File(name).getName()));
										FileInputStream in = new FileInputStream(name);					    			
										if(in != null){
											int len;
											while ((len = in.read(buffer)) > 0) {
												zos.write(buffer, 0, len);
											}						    		
											zos.closeEntry();
											in.close();
										}			
									}					    			
								}	
								for(String name : screenshotImages){
									log.info(name);
									screenshotFolder  = new File(htmlReportGenLocation+File.separator+"HtmlReport"+File.separator+productName+"-WP-"+testRunNo+File.separator+"screenshots");
									if(!screenshotFolder.exists()){
										screenshotFolder.mkdirs();				    				
									}
									File screenshotFile = new File(name);
									if(screenshotFile.exists()){
										FileUtils.copyFile(screenshotFile, new File(screenshotFolder+File.separator+screenshotFile.getName()));
									}
								}	
								for(String name : logoImage){	
									log.info("logo path is :"+name);
									logoFolder  = new File(htmlReportGenLocation+File.separator+"HtmlReport"+File.separator+productName+"-WP-"+testRunNo+File.separator+"logo");
									if(!logoFolder.exists()){
										logoFolder.mkdirs();				    				
									}
									File logoFile = new File(name);
									if(logoFile.exists()){
										FileUtils.copyFile(logoFile, new File(logoFolder+File.separator+logoFile.getName()));
									}
								}	
								zipDir(zos,"",screenshotFolder);
								zipDir(zos,"",logoFolder);				    		
								zos.close();				    		
							}catch(IOException ex){
								log.info("no data ");
								if(testRunJobId!=-1){				
									jTableResponse = new JTableResponse("ERROR","No records available for the current Job "+testRunJobId);
								} else if(testRunNo != -1){
									jTableResponse = new JTableResponse("ERROR","No records available for the current Workpackage "+testRunNo);
								}
								return jTableResponse;
							}
							log.info("workpackageHtmlFile is"+workpackageHtmlFile);
							workpackageHtmlFile = htmlReportGenLocation+File.separator+"HtmlReport"+File.separator+productName+"-WP-"+testRunNo+".zip";						
						}catch (IOException | JSONException e) {		
							e.printStackTrace();
							if(testRunJobId!=-1){				
								jTableResponse = new JTableResponse("ERROR","No records available for the current Job "+testRunJobId);
							} else if(testRunNo != -1){
								jTableResponse = new JTableResponse("ERROR","No records available for the current Workpackage "+testRunNo);
							}
							return jTableResponse;
						} 					
					} else {
						log.info("no data ");
						if(testRunJobId!=-1){				
							jTableResponse = new JTableResponse("ERROR","No records available for the current Job "+testRunJobId);
						} else if(testRunNo != -1){
							jTableResponse = new JTableResponse("ERROR","No records available for the current Workpackage "+testRunNo);
						}
						return jTableResponse;
					}			
				}
			}	
		}
		if(!new File(workpackageHtmlFile).exists()&&!new File(JobLevelHTMLFIle).exists()) {
			log.info("no data ");
			if(testRunJobId!=-1){				
				jTableResponse = new JTableResponse("ERROR","No records available for the current Job "+testRunJobId);
			} else if(testRunNo != -1){
				jTableResponse = new JTableResponse("ERROR","No records available for the current Workpackage "+testRunNo);
			}
		}
		else {				
			if(productTypeName.equalsIgnoreCase("WORKPACKAGE")){
				jTableResponse = new JTableResponse("Ok","Export testCases Completed.",workpackageHtmlFile);
			} else if(productTypeName.equalsIgnoreCase("JOB")){
				jTableResponse = new JTableResponse("Ok","Export testCases Completed.",JobLevelHTMLFIle);
			}	
		}		
		return jTableResponse;
	}

	@RequestMapping(value="report.run.pdf" )
	public String runReport_PDF(@RequestParam Integer testRunNo,@RequestParam Integer testRunConfigurationChildId,ModelAndView modelAndView,HttpServletRequest request,HttpServletResponse response) throws Exception {
		return "forward:report.run.device.pdf";
	}

	@RequestMapping(value="report.run.html" )
	public String runReport_HTML(@RequestParam Integer testRunNo,@RequestParam Integer testRunConfigurationChildId,ModelAndView modelAndView,HttpServletRequest request,HttpServletResponse response) throws Exception {
		return "forward:report.run.device.html";
	}

	@RequestMapping(value="report.run.evidence.html")
	public String reportHtmlEvidence(@RequestParam Integer testRunNo,@RequestParam Integer testRunConfigurationChildId,ModelAndView modelAndView,HttpServletRequest request,HttpServletResponse response) throws Exception {
		return "forward:report.run.device.evidence.html";
	}

	@RequestMapping(value="report.run.excel" )
	public String runReport_EXCEL(@RequestParam Integer testRunNo,@RequestParam Integer testRunListId,@RequestParam Integer testRunConfigurationChildId,@RequestParam String deviceListId,@RequestParam String reportMode,ModelAndView modelAndView,HttpServletRequest request,HttpServletResponse response) throws Exception {
		return "forward:report.run.device.excel";
	}

	@RequestMapping(value="report.run.device.excel", produces = "application/xls")
	public void runDeviceReport_EXCEL(@RequestParam Integer testRunNo,@RequestParam Integer testRunListId,@RequestParam Integer testRunConfigurationChildId,@RequestParam String deviceListId,@RequestParam String reportMode,ModelAndView modelAndView,HttpServletRequest request,HttpServletResponse response) throws Exception {
		log.info("TestReportController::runDeviceReportL");

		String strContentType=null;
		String strPrintMode=null;
		String fileName=request.getServletContext().getRealPath("/")+"report\\";
		checkAndCreateReportDirectory(fileName);


		List<JasperPrint> jPrintList = new ArrayList<JasperPrint>();
		Map<String,Object> pInput=new HashMap<String, Object>();		
		if (reportMode.equals("XLS")){
			strPrintMode="XLS";
			strContentType="application/xls";
		}else if(reportMode.equals("CSV")){
			strPrintMode="CSV";
			strContentType="text/csv";
		}
		response.reset();
		response.resetBuffer();
		ServletOutputStream servletOutputStream = response.getOutputStream();
		response.setContentType(strContentType);
		response.setHeader("Content-Disposition","inline;");

		pInput.put("testRunNo",testRunNo);
		pInput.put("testRunListId",testRunListId);
		pInput.put("testRunConfigurationChildId",testRunConfigurationChildId);
		pInput.put("printMode",strPrintMode);
		pInput.put("deviceId",deviceListId);
		if (deviceListId!=null && !deviceListId.equals("")){
			jPrintList=report.generateTestRunDeviceReport(pInput);			
			fileName=fileName+"TestRunDeviceReport-"+testRunNo+"-"+deviceListId;
		}else {
			jPrintList=report.generateTestRunReport(pInput);			
			fileName=fileName+"TestRunReport-"+testRunNo;
		}

		fileName=fileName+".xls";	

		FileOutputStream fos = new FileOutputStream(fileName);
		if (reportMode.equals("XLS"))
		{	

			JRXlsExporter exporterXls = new JRXlsExporter ();  
			exporterXls.setParameter(JRXlsExporterParameter.JASPER_PRINT_LIST, jPrintList);
			exporterXls.setParameter(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN, false);  
			exporterXls.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, false);  
			exporterXls.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, false);  
			exporterXls.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, true);  
			exporterXls.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, true);  
			exporterXls.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, fileName);  
			exporterXls.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, true);
			exporterXls.setParameter(JRXlsExporterParameter.CREATE_CUSTOM_PALETTE,false);
			exporterXls.setParameter(JRXlsExporterParameter.IGNORE_PAGE_MARGINS, true);  
			exporterXls.setParameter(JRXlsExporterParameter.IS_IGNORE_CELL_BORDER,true);
			exporterXls.setParameter(JRXlsExporterParameter.IS_IGNORE_GRAPHICS,false);
			exporterXls.setParameter(JRXlsExporterParameter.IS_IGNORE_CELL_BACKGROUND,false);
			exporterXls.setParameter(JRXlsExporterParameter.OUTPUT_STREAM,fos);
			exporterXls.exportReport();


			log.info("location:" + fileName);

			HSSFWorkbook workbook = new  HSSFWorkbook();
			workbook.write(fos);		    
			servletOutputStream.flush();  
			servletOutputStream.close();
		}

	}

	//**************Bug 
	@RequestMapping(value="report.bug.list",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse listBug(@RequestParam Integer testRunNo,@RequestParam Integer testRunConfigurationChildId,@RequestParam Integer deviceListId) {	

		JTableResponse jTableResponse;
		try {
			List<TestExecutionResultBugList> testExecutionResultBugList=null;
			if(deviceListId==null || deviceListId==-1){
				testExecutionResultBugList = testExecutionBugsService.listByTestRun(testRunNo, testRunConfigurationChildId);
			}else{
				testExecutionResultBugList = testExecutionBugsService.list(testRunNo, testRunConfigurationChildId,deviceListId);
			}				

			List<JsonTestExecutionResultBugList> jsonTestExecutionResultBugList=new ArrayList<JsonTestExecutionResultBugList>();

			for(TestExecutionResultBugList testExecutionResultBugListdet: testExecutionResultBugList){
				jsonTestExecutionResultBugList.add(new JsonTestExecutionResultBugList(testExecutionResultBugListdet));
			}
			testExecutionResultBugList = null;
			jTableResponse = new JTableResponse("OK", jsonTestExecutionResultBugList,testExecutionBugsService.getTotalBugsCount(testRunNo, testRunConfigurationChildId));

		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error fetching records!");
			log.error("JSON ERROR", e);	            
		}

		return jTableResponse;
	}

	@RequestMapping(value="report.bug.update",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse updateDevice(@ModelAttribute JsonTestExecutionResultBugList jsonTestExecutionResultBugList, BindingResult result) {
		JTableResponse jTableResponse;
		TestExecutionResultBugList testExecutionResultBugList=null;
		if(result.hasErrors()){
			jTableResponse = new JTableResponse("ERROR","Invalid Form!"); 
		}

		try {
			//testExecutionResultBugList =jsonTestExecutionResultBugList.getTestExecutionResultBugList();

			testExecutionResultBugList=testExecutionBugsService.getByBugId(jsonTestExecutionResultBugList.getTestExecutionResultBugId());

			testExecutionResultBugList.setBugTitle(jsonTestExecutionResultBugList.getBugTitle());
			testExecutionResultBugList.setBugDescription(jsonTestExecutionResultBugList.getBugDescription());

			testExecutionBugsService.update(testExecutionResultBugList);
			List<JsonTestExecutionResultBugList> jsonTestExecutionResultBugListVal=new ArrayList<JsonTestExecutionResultBugList>();

			jsonTestExecutionResultBugListVal.add(new JsonTestExecutionResultBugList(testExecutionResultBugList));
			jTableResponse = new JTableResponse("OK", jsonTestExecutionResultBugListVal,1);
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error updating record!");
			log.error("JSON ERROR", e);
		}

		return jTableResponse;
	}

	/*
	//@RequestMapping(value="bugdetails.bug.update",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse updateBugRunlist(@RequestParam Integer testExecutionResultBugId, @RequestParam String bugTitle, @RequestParam String bugDescription) {	
		log.debug("bugdetails.bug.update");

		JTableResponse jTableResponse;
		TestExecutionResultBugList testExecutionResultBugList=null;
			try {
				//int itestExecutionResultBugId=Integer.parseInt(testExecutionResultBugId);
				testExecutionResultBugList=testExecutionBugsService.getByBugId(testExecutionResultBugId);
				testExecutionResultBugList.setBugTitle(bugTitle);
				testExecutionResultBugList.setBugDescription(bugDescription);

				testExecutionBugsService.update(testExecutionResultBugList);

				List<JsonTestExecutionResultBugList> jsonTestExecutionResultBugList=new ArrayList<JsonTestExecutionResultBugList>();

				jsonTestExecutionResultBugList.add(new JsonTestExecutionResultBugList(testExecutionResultBugList));
			    jTableResponse = new JTableResponse("OK", jsonTestExecutionResultBugList,1);

	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);	            
	        }

        return jTableResponse;
    }*/

	@RequestMapping(value="report.bug.delete",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse deleteBugRunlist(@RequestParam Integer testExecutionResultBugId) {
		JTableResponse jTableResponse;
		log.debug("bugdetails.bug.delete");
		TestExecutionResultBugList testExecutionResultBugList=null;

		try {
			testExecutionResultBugList=testExecutionBugsService.getByBugId(testExecutionResultBugId);
			testExecutionBugsService.delete(testExecutionResultBugList);
			jTableResponse = new JTableResponse("OK");

		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error fetching records!");
			log.error("JSON ERROR", e);	            
		}

		return jTableResponse;
	}

	/*@RequestMapping(value="bugdevicedetails.list",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse listBugRundevicelistFiltered(@RequestParam Integer testRunNo, @RequestParam Integer testRunConfigurationChildId,@RequestParam Integer deviceListId) {	
		log.debug("bugdetails.list");
		JTableResponse jTableResponse;

			try {
				List<TestExecutionResultBugList> testExecutionResultBugList = testExecutionBugsService.list(testRunNo, testRunConfigurationChildId,deviceListId);

				List<JsonTestExecutionResultBugList> jsonTestExecutionResultBugList=new ArrayList<JsonTestExecutionResultBugList>();

				for(TestExecutionResultBugList testExecutionResultBugListdet: testExecutionResultBugList){
					jsonTestExecutionResultBugList.add(new JsonTestExecutionResultBugList(testExecutionResultBugListdet));
				}
			    jTableResponse = new JTableResponse("OK", jsonTestExecutionResultBugList,testExecutionBugsService.getTotalBugsCount(testRunNo, testRunConfigurationChildId, deviceListId));

	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);	            
	        }

        return jTableResponse;
    }*/

	//*******BUG END

	@ExceptionHandler(Exception.class)
	public void handleException(Throwable ex, HttpServletResponse response)
			throws Exception {

		response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
				ex.getMessage());
		ex.printStackTrace();
	}

	public static void checkAndCreateReportDirectory(String fileName){
		//create root dir
		File file = new File(fileName);
		if(file.exists()){
			if(file.isFile()){file.delete(); file.mkdirs();}

		}else{
			file.mkdirs();
		}			
	}


	//Changes for Bug :717
	@RequestMapping(value="test.results.export", method=RequestMethod.POST ,produces="application/json")
	public  @ResponseBody JTableResponse testExecResultsExport(@RequestParam Integer testRunJobId) {
		log.debug("test.results.export");
		JTableResponse jTableResponse;
		try {

			String serverFolderPath = CommonUtility.getCatalinaPath() + IDPAConstants.TESTRUNJOBREPORTS_EXCELPATH+File.separator+testRunJobId;

			String exportLocation = serverFolderPath+".zip";

			File file = new File(serverFolderPath);
			if (!file.isDirectory()) {
				file.mkdirs();
			}
			Boolean isExportComplete = excelTestDataIntegrator.exportTestResults(testRunJobId,serverFolderPath);

			if(isExportComplete){
				jTableResponse = new JTableResponse("Ok",exportLocation);
			} else{			
				jTableResponse = new JTableResponse("ERROR","Export test results completed");
			}
		} catch (Exception e) {

			log.error("JSON ERROR", e);
			jTableResponse = new JTableResponse("Error in Export test results");
		}
		return jTableResponse;
	}

	//Changes for Localization Test Results 
	@RequestMapping(value="test.Locale.results.export", method=RequestMethod.POST ,produces="application/json")
	public  @ResponseBody JTableResponse LocalizationResultsExport(@RequestParam Integer testRunJobId) {
		log.debug("test.results.export");
		log.info("export test exec results started");
		JTableResponse jTableResponse;

		try {		

			String serverFolderPath = null;
			if(System.getProperty("os.name").contains("Linux")){
				serverFolderPath= CommonUtility.getCatalinaPath() + IDPAConstants.TESTRUNJOBREPORTS_EXCELPATH_LINUX+"/"+testRunJobId;
			} else {
				serverFolderPath= CommonUtility.getCatalinaPath() + IDPAConstants.TESTRUNJOBREPORTS_EXCELPATH+File.separator+testRunJobId;
			}


			String exportLocation = serverFolderPath+".zip";

			//String exportLocation = request.getServletContext().getRealPath("/")+"report\\"+"export\\"+"Locale\\";
			File file = new File(serverFolderPath);
			if (!file.isDirectory()) {
				file.mkdirs();
			}
			boolean isExportComplete = excelTestDataIntegrator.exportLocaleReports(testRunJobId, serverFolderPath);
			if(isExportComplete){
				jTableResponse = new JTableResponse("Ok",exportLocation);
			} else{			

				jTableResponse = new JTableResponse("Ok","Export test results completed");
			}
		} catch (Exception e) {

			log.error("JSON ERROR", e);
			jTableResponse = new JTableResponse("Error in Export test results");
		}
		return jTableResponse;
	}

	//Changes for Features Test Results 
	@RequestMapping(value="test.features.results.export", method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse FeaturesResultsExport(@RequestParam Integer testRunJobId) {
		log.debug("test.results.export");
		log.info("export test exec results started");
		JTableResponse jTableResponse;

		try {		
			String serverFolderPath = null;
			if(System.getProperty("os.name").contains("Linux")){
				serverFolderPath = CommonUtility.getCatalinaPath() + IDPAConstants.TESTRUNJOBREPORTS_FEATURES_PATH_LINUX+"/"+testRunJobId;
			} else {
				serverFolderPath = CommonUtility.getCatalinaPath() + IDPAConstants.TESTRUNJOBREPORTS_FEATURES_PATH+File.separator+testRunJobId;
			}			
			String exportLocation = serverFolderPath+File.separator+IDPAConstants.FEATURE_NAMING_CONV+testRunJobId+".xlsx";
			File file = new File(serverFolderPath);
			if (!file.isDirectory()) {
				file.mkdirs();
			}
			boolean isExportComplete = excelTestDataIntegrator.exportFeatureReports(testRunJobId, exportLocation);
			if(isExportComplete){
				jTableResponse = new JTableResponse("Ok",exportLocation);
			} else{			
				jTableResponse = new JTableResponse("ERROR","Error in feature results export");
			}	
		} catch (Exception e) {

			log.error("JSON ERROR", e);
			jTableResponse = new JTableResponse("Error in feature results export");
		}
		return jTableResponse;
	}
	@RequestMapping(value="download.run.word.report", produces="application/msword")
	public  @ResponseBody JTableResponse exportTestRunWordReport(@RequestParam Integer testRunNo,@RequestParam Integer testRunConfigurationChildId,@RequestParam String deviceId,
			@RequestParam Integer testRunJobId,ModelAndView modelAndView,HttpServletRequest request,HttpServletResponse response) throws Exception {



		JTableResponse jTableResponse;
		TestRunJob testRunJob=null;
		ProductType productType =null;
		String exportLocation =null;
		if(testRunJobId!=-1){  // Test Run Job Level
			testRunJob=workPackageService.getTestRunJobById(testRunJobId);
			testRunNo=testRunJob.getWorkPackage().getWorkPackageId();

			if(testRunJob.getWorkPackage().getTestRunPlan()!=null){
				testRunConfigurationChildId=testRunJob.getWorkPackage().getTestRunPlan().getTestRunPlanId();
				productType =testRunJob.getWorkPackage().getTestRunPlan().getProductVersionListMaster().getProductMaster().getProductType();
				log.info("product Type Master Name "+productType.getTypeName());
				//log.info("product Master name based on the testRunConfigChild"+productMaster.getProductName());
			}
			if(testRunJob.getGenericDevices()!=null){
				deviceId= testRunJob.getGenericDevices().getUDID();
			}

		}else{ // Work Package Level
			WorkPackage wp=workPackageService.getWorkPackageById(testRunNo);
			productType=wp.getProductBuild().getProductVersion().getProductMaster().getProductType();
			if(wp.getTestRunPlan()!=null){
				testRunConfigurationChildId=wp.getTestRunPlan().getTestRunPlanId();
			}

		}
		response.reset();
		response.resetBuffer();

		response.setHeader("Content-Disposition","inline;");
		try {		

			exportLocation = request.getServletContext().getRealPath("/")+"report\\"+"export\\";
			//String serverFolderPath = CommonUtility.getCatalinaPath() + IDPAConstants.TESTRUNJOBREPORTS_PATH;

			//
			String fileLocation = excelTestDataIntegrator.exportWordTestResults(testRunNo,deviceId,exportLocation);
			if(new File(fileLocation).exists()){


				//jTableResponse = new JTableResponse("Ok","export test exec results word completed");
				jTableResponse = new JTableResponse("Ok","Word - Test results report completed.",fileLocation);
			} else{			

				jTableResponse = new JTableResponse("Error","Error in Export word generation");
			}
		} catch (Exception e) {

			log.error("JSON ERROR", e);
			jTableResponse = new JTableResponse("Error in Export word generation");
		}

		return jTableResponse;
	}
	@RequestMapping(value="report.run.device.evidence.html" ,produces="application/html")
	public  @ResponseBody JTableResponse evidenceReportHtml(@RequestParam Integer testRunNo,@RequestParam Integer testRunConfigurationChildId,@RequestParam String deviceId,
			@RequestParam Integer testRunJobId,@RequestParam String reportType,@RequestParam String viewType,ModelAndView modelAndView,HttpServletRequest request,HttpServletResponse response) throws Exception {
		log.info("Report Inputs : work package no " + testRunNo + " child id : " + testRunConfigurationChildId+" job id :"+testRunJobId);
		JTableResponse jTableResponse = null ;
		TestRunJob testRunJob=null;
		ProductType productType =null;       
		String exportLocation =null;
		String serverFolderPath = null;
		if (System.getProperty("os.name").contains("Linux")) {
			serverFolderPath = CommonUtility.getCatalinaPath()
					+ IDPAConstants.JASPERREPORTS_PATH_LINUX;
		} else {
			serverFolderPath = CommonUtility.getCatalinaPath()
					+ IDPAConstants.JASPERREPORTS_PATH;
		}
		String testSuiteName = "";
		String workpackageName="";
		String imageFileName = "";
		BufferedImage logo = null;
		String loginUserName = "";
		String workpackageHtmlFile = "";
		String JobLevelHTMLFIle="";
		String imageServerPath = null;
		String productName = "";
		String testRunPlanName = "";
		String customerName = "";
		String closeImagePath = "";
		String workPackageReportDirectory = "";
		String jobReportDirectory = "";
		String fileToOpenInBrowser = "";
		String fileToDownload = "";
		String workPackageMode = "";
		Set<TestRunJob> jobsSet = null;
        List<WorkPackageTestCaseExecutionPlan> wptcExecutionList = null;
		String productTypeName = "";
		if(System.getProperty("os.name").contains("Linux")){
			imageServerPath = CommonUtility.getCatalinaPath().concat("/webapps/Logo/");
		} else {
			imageServerPath = CommonUtility.getCatalinaPath().concat("\\webapps\\Logo\\");
		}
		log.info("Job Id:" +testRunJobId);
		if(testRunJobId != -1){  // Test Run Job Level
			testRunJob = workPackageService.getTestRunJobById(testRunJobId);
			testRunNo = testRunJob.getWorkPackage().getWorkPackageId();
			if(testRunJob.getWorkPackage()!= null){
				loginUserName = testRunJob.getWorkPackage().getUserList().getLoginId();
			}
			if(testRunJob.getWorkPackage().getTestRunPlan()!= null){
				testRunConfigurationChildId = testRunJob.getWorkPackage().getTestRunPlan().getTestRunPlanId();
				if(testRunJob.getTestSuite() != null){
					testSuiteName = testRunJob.getTestSuite().getTestSuiteName();	
				}					
				productType = testRunJob.getWorkPackage().getTestRunPlan().getProductVersionListMaster().getProductMaster().getProductType();
				customerName = testRunJob.getWorkPackage().getTestRunPlan().getProductVersionListMaster().getProductMaster().getCustomer().getCustomerName().trim();
				productName = testRunJob.getWorkPackage().getTestRunPlan().getProductVersionListMaster().getProductMaster().getProductName();
				testRunPlanName = testRunJob.getWorkPackage().getTestRunPlan().getTestRunPlanName();
				log.info("product Type Master Name " +productType.getTypeName());
				//log.info("product Master name based on the testRunConfigChild"+productMaster.getProductName());
				imageFileName = testRunJob.getWorkPackage().getTestRunPlan().getProductVersionListMaster().getProductMaster().getCustomer().getImageURI();
				productTypeName = "Job";
				log.info("ImageFileName==>"+imageFileName);
			}
			if(testRunJob.getGenericDevices() != null){
				deviceId = testRunJob.getGenericDevices().getUDID();
			}			 
		}else{ // Work Package Level
			WorkPackage wp = workPackageService.getWorkPackageById(testRunNo);
			if(wp != null){				
				loginUserName = wp.getUserList().getLoginId();
				jobsSet = wp.getTestRunJobSet();
				
			}
			if(wp.getResultsReportingMode() != null && wp.getResultsReportingMode().equalsIgnoreCase("COMBINED JOB"))
				workPackageMode = "COMBINED JOB";

			workpackageName = wp.getName();
			productType = wp.getProductBuild().getProductVersion().getProductMaster().getProductType();
			productName = wp.getProductBuild().getProductVersion().getProductMaster().getProductName();
			customerName = wp.getProductBuild().getProductVersion().getProductMaster().getCustomer().getCustomerName().trim();
			//testRunPlanName = wp.getTestRunPlan().getTestRunPlanName();
			if(wp.getTestRunPlan() != null){
				testRunConfigurationChildId = wp.getTestRunPlan().getTestRunPlanId();
				imageFileName = wp.getTestRunPlan().getProductVersionListMaster().getProductMaster().getCustomer().getImageURI();
				productTypeName = "Workpackage";
				log.info("ImageFileName==>"+imageFileName);
			}			
		}

		if(imageFileName != null && new File(imageServerPath.concat(imageFileName)).exists()){
			imageServerPath = imageServerPath.concat(imageFileName);
			log.info("ImageServerPath==>"+imageServerPath);
		} else {		
			if(System.getProperty("os.name").contains("Linux")){
				imageServerPath= CommonUtility.getCatalinaPath().concat("/webapps"+ File.separator + request.getContextPath() + File.separator +"css/images/noimage.jpg");
			} else {
				imageServerPath= CommonUtility.getCatalinaPath().concat("\\webapps"+ File.separator + request.getContextPath() + File.separator +"css\\images\\noimage.jpg");
			}
		}
		File imageFile = new File(imageServerPath);
		log.info("ImageFile==>"+imageFile);

		if(imageFile.exists() && imageFile != null){
			//logo = ImageIO.read(imageFile);	
		} else {
			Files.createDirectories(Paths.get(imageFile.getParent()));
			log.info("imageFileName is:"+imageFileName);
		}

		log.info("Report Inputs : " + testRunNo + " : " + testRunConfigurationChildId);
		log.info("logo path is :"+logo);
		JasperPrint jasperPrint=null;

		String strContentType=null;
		String strPrintMode=null;

		if(viewType.equalsIgnoreCase("PDF")) {
			strPrintMode="PDF"; 
			strContentType="application/pdf";
		} else {
			strPrintMode="HTML";
			strContentType="application/html";
		}
		response.reset();
		response.resetBuffer();
		String jsonSourceFile = null;
		String jsonSourceSubFile1 = null;
		String jsonSourceSubFile2 = null;
		String jsonSourceSubFile3 = null;
		String jsonSourceSubFile4= null;
		if (productTypeName.equalsIgnoreCase("JOB")){
			List<String>JobHtmlReportData = new ArrayList<String>();
			log.info("Going to TestRunListHtmlReport : " + testRunNo + " : " + testRunConfigurationChildId+"  :  "+productTypeName);
			JobHtmlReportData = report.generateTestRunListHtmlReport(testRunNo, testRunConfigurationChildId,strPrintMode,productType,logo, loginUserName);
			log.info("after getting the html report data");
			if(!JobHtmlReportData.isEmpty()){
				for(int i=0;i<JobHtmlReportData.size();i++){
					jsonSourceFile=JobHtmlReportData.get(0);
					jsonSourceSubFile1=JobHtmlReportData.get(1);
					jsonSourceSubFile2=JobHtmlReportData.get(2);
					jsonSourceSubFile3=JobHtmlReportData.get(3);
					jsonSourceSubFile4=JobHtmlReportData.get(4);
				}
				org.json.JSONArray jsonJobReportArray=new org.json.JSONArray(jsonSourceSubFile1);
				boolean isJobExists=false;
				List<String> jobIdsList=new ArrayList<String>();
				for(int i=0;i<jsonJobReportArray.length();i++){
					org.json.JSONObject jsonJobReportObject = jsonJobReportArray.getJSONObject(i);
					jobIdsList.add(jsonJobReportObject.getString("job_id"));
				}
				for(int l=0;l<jobIdsList.size();l++){
					if(Integer.parseInt(jobIdsList.get(l)) == testRunJobId){
						isJobExists=true;
					}
				}
				log.info("isJobExists :"+isJobExists);
				if(isJobExists && new org.json.JSONArray(JobHtmlReportData.get(1)).length() > 0){
					org.json.JSONArray jsonReportSummaryArray = new org.json.JSONArray(jsonSourceFile);
					//org.json.JSONArray jsonJobReportArray=new org.json.JSONArray(jsonSourceSubFile1);
					org.json.JSONArray jsonJobSummaryArray = new org.json.JSONArray(jsonSourceSubFile2);
					org.json.JSONArray jsonTestCaseSummaryArray = new org.json.JSONArray(jsonSourceSubFile3);
					org.json.JSONArray jsonTestStepArray = new org.json.JSONArray(jsonSourceSubFile4);
					log.info("jsonReportSummaryArray values are :"+jsonReportSummaryArray);
					log.info("jsonJobReportArray values are :"+jsonJobReportArray);
					log.info("jsonJobSummaryArray values are :"+jsonJobSummaryArray);
					log.info("jsonTestCaseSummaryArray values are :"+jsonTestCaseSummaryArray);
					log.info("jsonTestStepArray values are :"+jsonTestStepArray);	

					try{
						List<String>testStepPath = new ArrayList<String>();

						jobReportDirectory = CommonUtility.getCatalinaPath()+ File.separator + "webapps" + File.separator + request.getContextPath() + File.separator+"JobReport"+File.separator;
						JobLevelHTMLFIle = jobReportDirectory+ productName+"-JR-"+testRunJobId+"-Evidence.html";
						File htmFile = new File(JobLevelHTMLFIle);
						if(!htmFile.exists()){
							new File(htmFile.getParent()).mkdirs();
						}
						byte[] buffer = new byte[1024];
						String testenvironmentname = "";
						StringBuilder testEnvironment = new StringBuilder();
						BufferedWriter bw = new BufferedWriter(new FileWriter(new File(JobLevelHTMLFIle)));	 
						//HashMap<String, Object> map = new HashMap<String, Object>();
						bw.write("<html><head><META http-equiv=Content-Type content='text/html; charset=utf-8'><style>.center {margin: auto;width: 75%;border: 3px solid #473572;padding: 10px;position : fixed;background:white;height:60%;}.parentdiv {margin: auto; width: 80%;height:50%;border: 3px;padding: 10px;position : fixed;top:20%;left:8%;right:auto;}body {background-color: #FFFFFF;}th{text-align: left;color:white;background: #3c6ac6;}a {color:blue;} a:visited {color:blue;}a:hover {color:blue;}a:active {color:blue;}</style><title>Job Evidence Report</title><script>function display(e){document.getElementById(\"tag\").style.display='';var img = document.getElementById(\"f\");img.setAttribute(\"src\",e);document.getElementById(\"childdiv\").appendChild(img);return false;}</script></head><body>");
						try {

							for(int i = 0;i < jsonJobReportArray.length() ; i++){
								org.json.JSONObject jsonJobsListObject = jsonJobReportArray.getJSONObject(i);
								if(jsonJobsListObject.getString("job_name") != null && !jsonJobsListObject.getString("job_name").isEmpty()){
									if(testRunJobId.equals(Integer.valueOf(jsonJobsListObject.getString("job_id")))){
										testenvironmentname = jsonJobsListObject.getString("job_name");
										testEnvironment.append(testenvironmentname+";"); 
									}
								}
							}
							String passedOrFailedJob="PASSED";
							String jobResult="", device="", devicePlatForm="";
							org.json.JSONObject jsonReportSummaryObject = jsonReportSummaryArray.getJSONObject(0);	
							bw.write("<table width='100%'><tr style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #FFFFFF; font-size: 24px; height:62px;background:#003ca4; font-weight: bold;'><td><img style=\"width:50px;height:50px;\" src='logo/"+imageFile.getName()+"'/></td> <td align='middle' id='TOP'>"+jsonReportSummaryObject.getString("productname")+" - "+jsonReportSummaryObject.getString("testrunconfigurationname")+"-Job Evidence Report</td></tr><tr style='height:13px'></tr></table>");		
							bw.write("<table bgcolor='white' width='100%' style='table-layout: fixed;'>");
							//report summary
							for(int j=0;j<jsonTestCaseSummaryArray.length();j++){
								if(!jobResult.equalsIgnoreCase("FAILED")){
									org.json.JSONObject testCaseObj =jsonTestCaseSummaryArray.getJSONObject(j);
									passedOrFailedJob = "<td style='font-weight: bold'> : <span style='color:#00C800;font-family: DejaVu Sans, Arial, Helvetica, sans-serif;font-size: 12px;'><b>PASSED</span></b></td>";
									if(testCaseObj.getString("job_id").equals(Integer.toString(testRunJobId))&&testCaseObj.getString("result").equalsIgnoreCase("FAILED")){
										passedOrFailedJob = "<td style='font-weight: bold'> : <span style='color:#c80000;font-family: DejaVu Sans, Arial, Helvetica, sans-serif;font-size: 12px;'><b>FAILED</span></b></td>";
										jobResult="FAILED";	
									}		
								}
							}		
							log.info("jobResult is "+jobResult);
							if((jsonReportSummaryObject.getString("deviceid").equalsIgnoreCase("N/A") && jsonReportSummaryObject.getString("devicemodel").equalsIgnoreCase("N/A"))&&jsonReportSummaryObject.getString("deviceplatformname").equalsIgnoreCase("N/A")&&jsonReportSummaryObject.getString("deviceplatformversion").equalsIgnoreCase("N/A")){
								device="<td style='font-weight: bold'> : <span style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; font-weight: bold'>N/A</td>";
								devicePlatForm="<td style='font-weight: bold'> : <span style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; font-weight: bold'>N/A</td>";
							}
							else{
								device="<td style='font-weight: bold'> : <span style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; font-weight: bold;word-wrap: break-word;'>"+jsonReportSummaryObject.getString("devicemodel")+","+jsonReportSummaryObject.getString("deviceid")+"</span></td>";
								devicePlatForm="<td style='font-weight: bold'> : <span style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; font-weight: bold;word-wrap: break-word;'>"+jsonReportSummaryObject.getString("deviceplatformname")+" "+jsonReportSummaryObject.getString("deviceplatformversion")+"</span></td>";
							}
							//bw.write("<tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px;width:150;'>Product Name </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold' > : "+jsonReportSummaryObject.getString("productname")+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px;width:150;'>Work Package No </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jsonReportSummaryObject.getString("testrunno")+"</td></tr><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Product version	</th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jsonReportSummaryObject.getString("productversionname")+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Triggered Time </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jsonReportSummaryObject.getString("testruntriggeredtime")+"</td></tr><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test Run Plan	</th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jsonReportSummaryObject.getString("testrunconfigurationname")+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test Cases Passed </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jsonReportSummaryObject.getInt("passedtest")+"/"+jsonReportSummaryObject.getInt("totaltest")+"</td></tr><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test Suite	</th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jsonReportSummaryObject.getString("testsuiteid")+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test Steps Passed </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jsonReportSummaryObject.getInt("passedtestecase")+"/"+jsonReportSummaryObject.getInt("totaltestcase")+"</td></tr><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test Start Date & Time </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jsonReportSummaryObject.getString("testrunstarttime")+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test End Date & Time </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jsonReportSummaryObject.getString("testrunendtime")+"</td></tr><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px;width:150;'>Test Environment    </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+testEnvironment+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test result	</th>"+passedOrFailedJob.replace("\"", "")+"</tr><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Host </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jsonReportSummaryObject.getString("hostname")+"</td></tr>");
							bw.write("<tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px;width:150;'>Product Name </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold' > : "+jsonReportSummaryObject.getString("productname")+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px;width:150;'>Work Package No </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jsonReportSummaryObject.getString("testrunno")+"</td></tr><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Product version	</th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jsonReportSummaryObject.getString("productversionname")+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px;width:150;'>Device Make & Model </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jsonReportSummaryObject.getString("devicemake")+" & "+jsonReportSummaryObject.getString("devicemodel")+"</td></tr><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test Run Plan	</th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jsonReportSummaryObject.getString("testrunconfigurationname")+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Triggered Time </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jsonReportSummaryObject.getString("testruntriggeredtime")+"</td></tr><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test Suite	</th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jsonReportSummaryObject.getString("testsuiteid")+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test Cases Passed </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jsonReportSummaryObject.getInt("passedtest")+"/"+jsonReportSummaryObject.getInt("totaltest")+"</td></tr><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test Start Date & Time </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jsonReportSummaryObject.getString("testrunstarttime")+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test Steps Passed </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jsonReportSummaryObject.getInt("passedtestecase")+"/"+jsonReportSummaryObject.getInt("totaltestcase")+"</td></tr><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px;width:150;word-wrap: break-word;'>Test Environment    </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold;word-wrap: break-word;'> : "+testEnvironment+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test End Date & Time </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jsonReportSummaryObject.getString("testrunendtime")+"</td></tr><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Host </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jsonReportSummaryObject.getString("hostname")+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test result	</th>"+passedOrFailedJob.replace("\"", "")+"</tr>");
							bw.write("</table>");
						} catch (JSONException e) {
							log.error("Error :"+e);
						}
						for(int i=0;i<jsonJobReportArray.length();i++){			
							org.json.JSONObject jsonJobReportObject = jsonJobReportArray.getJSONObject(i);					
							if(jsonJobReportObject.getString("job_id").equalsIgnoreCase(Integer.toString(testRunJobId))){
								//Test case summary details			
								String totalTCExecutionTime = "";
								String testCaseType = "Test Case Code";
								boolean testCaseTypeFlag = true;
								Set<Integer> listOfTestCaseIds = new HashSet<Integer>(); 
								for(int j=0;j<jsonTestCaseSummaryArray.length();j++){			
									org.json.JSONObject testCaseObj = jsonTestCaseSummaryArray.getJSONObject(j);
									if(testCaseObj.getString("job_id").equals(jsonJobReportObject.getString("job_id"))){
										totalTCExecutionTime = testCaseObj.getString("totaltcexectime");
									}
									if(testCaseObj.getString("testcasecode").equalsIgnoreCase("N/A")&&testCaseTypeFlag){
										testCaseType="Test Case Id";
										testCaseTypeFlag = false;
									}
									listOfTestCaseIds.add(Integer.valueOf(testCaseObj.getString("test_case_id")));
								}
								ListMultimap<Integer, Object> multimap = ArrayListMultimap.create();
								multimap = report.getFeatureNamesByTestCaseIdList(listOfTestCaseIds);
								bw.write("<table width='100%'><tr style='height:13px'></tr><tr style='font-family:DejaVu Sans, Arial, Helvetica,sans-serif;color:#ffffff;background:#003ca4;font-weight: bold;height:26px;font-size: 12px;'><td id=TESTCASESUMMARY"+jsonJobReportObject.getString("job_id")+">TEST CASE SUMMARY</td><td><span style='float:left'>Total Execution Time : "+totalTCExecutionTime+"</span><span style='float:right'><a style='text-decoration:none;font-weight:bold;color:#ffffff;font-size:13px;' href=#End>End</a></span></td></tr>");
								bw.write("<table bgcolor='white' width='100%'>");
								bw.write("<tr style='color:#ffffff;background: #3c6ac6;font-family:Arial;font-size:12px'><th>"+testCaseType+"</th><th>Test Case Name</th><th>description</th><th>result</th><th>Start Date & Time</th><th>End Date & Time</th><th>Execution Time</th></tr>");

								for(int j=0;j<jsonTestCaseSummaryArray.length();j++){			
									org.json.JSONObject testCaseObj =jsonTestCaseSummaryArray.getJSONObject(j);
									if(testCaseObj.getString("job_id").equals(jsonJobReportObject.getString("job_id"))&&jsonJobReportObject.getString("job_id").equalsIgnoreCase(Integer.toString(testRunJobId))){
										String result = testCaseObj.getString("result");
										String testcasecode=testCaseObj.getString("testcasecode");
										String passedOrFailed = "";
										if(result.equalsIgnoreCase("PASSED")){
											passedOrFailed = "<td style='color:#00C800;font-family: DejaVu Sans, Arial, Helvetica, sans-serif;font-size: 12px;'><b>"+result+"</b></td>";
										} else {
											passedOrFailed = "<td style='color:#c80000;font-family: DejaVu Sans, Arial, Helvetica, sans-serif;font-size: 12px;'><b>"+result+"</b></td>";				
										}
										if(testcasecode.equalsIgnoreCase("N/A")){
											testcasecode=testCaseObj.getString("test_case_id");
										}
										bw.write("<tr><td colspan='7'><hr/></td></tr><tr style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;vertical-align:top'><td>"+testcasecode+"</td><td><a style='text-decoration: none;font-weight:bold' href=#"+testCaseObj.getString("test_case")+jsonJobReportObject.getString("job_id")+">"+testCaseObj.getString("test_case")+"</a></td><td>"+testCaseObj.getString("description")+"</td>"+passedOrFailed.replace("\"", "")+"<td>"+testCaseObj.getString("starttime")+"</td><td>"+testCaseObj.getString("endtime")+"</td><td>"+testCaseObj.getString("totaltime")+"</td></tr>");
									}						
								}							
								bw.write("</table>");							
								//Test Case Details
								bw.write("<table width='100%'><tr></tr><tr align='left' style='font-family:DejaVu Sans, Arial, Helvetica,sans-serif;color:#ffffff;background:#003ca4;font-weight: bold;height:26px;font-size: 12px'><td>TEST CASE DETAILS</td></tr></table>");
								for(int k=0;k<jsonTestCaseSummaryArray.length();k++){			
									org.json.JSONObject testCaseObj =jsonTestCaseSummaryArray.getJSONObject(k);				
									String testcasename = testCaseObj.getString("test_case");
									Integer testCaseIdFromTCSummary = Integer.valueOf(testCaseObj.getString("test_case_id"));
									if(testCaseObj.getString("job_id").equals(jsonJobReportObject.getString("job_id"))&&jsonJobReportObject.getString("job_id").equalsIgnoreCase(Integer.toString(testRunJobId))){
										//Test case name with description
										List<Object> mappedFeaturesToTestCase = multimap.get(testCaseIdFromTCSummary);
										String featurenames = "";
										StringBuffer sbFeatureNames = new StringBuffer();
										if(mappedFeaturesToTestCase.size() > 0){
											for(int ii=0;ii < mappedFeaturesToTestCase.size();ii++){
												if(mappedFeaturesToTestCase.get(ii) != null){
													featurenames = mappedFeaturesToTestCase.get(ii).toString();
													sbFeatureNames.append(featurenames+",");
												}
											}
											if(sbFeatureNames != null && sbFeatureNames.length() > 0){
												featurenames = sbFeatureNames.toString();
												featurenames = featurenames.substring(0,featurenames.length()-1);
											}
										}
										else {
											featurenames="N/A";
										}
										bw.write("<table width='100%'>");
										bw.write("<tr style='font-family:Arial;color:#ffffff;font-size:12px;'><th colspan='4' align='left' id="+testCaseObj.getString("test_case")+jsonJobReportObject.getString("job_id")+">Test Case Name  	:"+testCaseObj.getString("test_case")+"</th><th><span style='float:right'><a style='text-decoration:none;font-weight:bold;font-size:13px;color:ffffff;' href=#TESTCASESUMMARY"+jsonJobReportObject.getString("job_id")+">"+"Test Case Summary | "+"</a><a style='text-decoration:none;font-weight:bold;font-size:13px;color:ffffff;' href=#TOP>Beginning | </a><a style='text-decoration:none;font-weight:bold;font-size:13px;color:ffffff;' href=#End>End</a></span></th></tr></table>");
										String testCaseResultStatus = testCaseObj.getString("result");
										String passedOrFailedTestCase = "";
										if(testCaseResultStatus.equalsIgnoreCase("PASSED")){
											passedOrFailedTestCase = "<td style='font-weight: bold'> : <span style='font-family: DejaVu Sans, Arial, Helvetica, sans-serif;color: #00C800;font-size: 12px;font-weight: bold'>"+testCaseResultStatus+"</span></td>";
										} else {
											passedOrFailedTestCase = "<td style='font-weight: bold'> : <span style='font-family: DejaVu Sans, Arial, Helvetica, sans-serif;color: #c80000;font-size: 12px;font-weight: bold'>"+testCaseResultStatus+"</span></td>";				
										}
										bw.write("<table width='100%' style='table-layout: fixed;'><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px;width:150;'>Test Case description </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;height:17px; font-weight: bold' > : "+testCaseObj.getString("description")+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px;width:150;'>Test Case Code/ID </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;height:17px; font-weight: bold' > : "+testCaseObj.getString("testcasecode")+"/"+testCaseObj.getString("test_case_id")+"</td></tr><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Start Date & Time</th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;font-weight: bold'> : "+testCaseObj.getString("starttime")+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>End Date & Time </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+testCaseObj.getString("endtime")+"</td></tr><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test Case result </th>"+passedOrFailedTestCase.replace("\"", "")+"<th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test Steps Passed </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+testCaseObj.getInt("passedteststep")+"/"+testCaseObj.getInt("totalteststep")+"</td></tr><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Features Covered </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;height:17px; font-weight: bold' > : "+featurenames+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Risks Covered </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;height:17px; font-weight: bold' > : "+testCaseObj.getString("riskname")+"</td></tr><tr style='height:13px'></tr></table>");
										//Test Step Details

										String testStepType="teststepcode";
										boolean testStepTypeFlag=true;
										for(int j=0;j<jsonTestStepArray.length();j++){
											org.json.JSONObject testStepObj =jsonTestStepArray.getJSONObject(j);
											if(testStepObj.getString("teststepcode").equalsIgnoreCase("N/A")&&testStepTypeFlag){
												testStepType="Test Step Id";
												testStepTypeFlag=false;
											}
										}
										//bw.write("<table width='100%' style='table-layout: fixed;'><tr style='color:white;background: #3c6ac6;font-family: Arial;font-size: 12px;height:18px'><th width='8%'>"+testStepType+"</th><th width='10%'>teststepdescription</th><th width='15%'>Expected result</th><th width='15%'>Observed result</th><th width='7%'>Status</th><th width='10%'>Execution Time</th><th width='15%'>Comments</th><th width='20%'>screenshot</th></tr>");
										bw.write("<table width='100%' style='table-layout: fixed;'><tr style='color:white;background: #3c6ac6;font-family: Arial;font-size: 12px;height:18px'><th width='5%' style='padding-left:5px;'>"+testStepType+"</th><th width='40%' style='padding-left:5px;'>Test Step Summary</th><th width='55%' style='padding-left:5px;'>Screenshot</th></tr>");
										for(int j=0;j<jsonTestStepArray.length();j++){
											org.json.JSONObject testStepObj =jsonTestStepArray.getJSONObject(j);
											String testStepResultStatus = testStepObj.getString("resultstatus");
											String passedOrFailedTestStep = "";
											String failureTestStepComments="";
											String testStepCode=testStepObj.getString("teststepcode");
											String testStepId = testStepObj.getString("teststepcode");
											if(testStepId.equalsIgnoreCase("N/A")){
												testStepId = testStepObj.getString("teststepid");
											}
											//Configurable property
											if(showTestStepExecutionId != null && showTestStepExecutionId.equalsIgnoreCase("YES")){
												testStepId = testStepId +" ["+ testStepObj.getString("teststepexecutionid") +"]";
											}
											if(testStepResultStatus.equalsIgnoreCase("PASSED")){
												//passedOrFailedTestStep = "<td style='font-family: DejaVu Sans, Arial, Helvetica, sans-serif;color: #00C800;font-size: 12px;font-weight: bold;' width='5%'>"+testStepResultStatus+"</td>";
												passedOrFailedTestStep = "<span style='font-family: DejaVu Sans, Arial, Helvetica, sans-serif;color: #00C800;font-size: 12px;font-weight: bold;'>"+testStepResultStatus+"</span>";
											} else {
												//passedOrFailedTestStep = "<td style='font-family: DejaVu Sans, Arial, Helvetica, sans-serif;color: #c80000;font-size: 12px;font-weight: bold;' width='5%'>"+testStepResultStatus+"</td>";
												passedOrFailedTestStep = "<span style='font-family: DejaVu Sans, Arial, Helvetica, sans-serif;color: #c80000;font-size: 12px;font-weight: bold;'>"+testStepResultStatus+"</span>";
											}
											if(testStepObj.getString("teststepcode").equalsIgnoreCase("N/A")){
												testStepCode=testStepObj.getString("teststepid");
											}

											if(testStepObj.getString("testcasename").equalsIgnoreCase(testcasename) && testStepObj.getString("job_id").equals(jsonJobReportObject.getString("job_id"))&&jsonJobReportObject.getString("job_id").equalsIgnoreCase(Integer.toString(testRunJobId))){	
												if(!testStepResultStatus.equalsIgnoreCase("PASSED") && !testStepObj.getString("failurereason").equalsIgnoreCase("N/A")){
													failureTestStepComments = testStepObj.getString("failurereason");
													/*if(failureTestStepComments.length() > 50)
														failureTestStepComments = failureTestStepComments.substring(0, 50);*/
													failureTestStepComments = "<b>Comments : </b><br/><br/>    "+failureTestStepComments+"<br/>";

													log.info("test step comments"+failureTestStepComments);
												}
												String testStep = testStepObj.getString("screenshot");
												if(new File(testStep).exists()){												
													testStepPath.add(testStep);
													log.info(testStep);
													testStep= testStep.substring(testStep.lastIndexOf("\\")+1, testStep.length());
													//closeImagePath = CommonUtility.getCatalinaPath().concat("\\webapps\\iLCM\\css\\images\\close_screenshot_image.jpg");
													 closeImagePath = request.getServletContext().getRealPath("\\close_screenshot_image.jpg");
													 log.info("Close Image Path :"+closeImagePath);
													closeImagePath = closeImagePath.substring(closeImagePath.lastIndexOf("\\")+1, closeImagePath.length());
													//bw.write("<tr><td colspan='8'><hr/></td></tr><tr style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;vertical-align:top'><td width='5%' style='word-wrap: break-word;'>"+testStepId+"</td><td width='10%' style='word-wrap: break-word;'>"+testStepObj.getString("teststepdescription")+"</td><td width='15%' style='word-wrap: break-word;'>"+testStepObj.getString("expectedoutput")+"</td><td width='15%' style='word-wrap: break-word;'>"+testStepObj.getString("observedoutput")+"</td>"+passedOrFailedTestStep.replace("\"", "")+"<td align='middle' width='10%' style='word-wrap: break-word;'>"+testStepObj.getString("totalteststepstime")+"</td><td width='20%' style='word-wrap: break-word;'>"+failureTestStepComments+"</td><td width='20%' style='word-wrap: break-word;'><a href='javascript:void(0);'><img height='100' width='200' src='screenshots/"+testStep+"' onclick=\"display('screenshots/"+testStep+"')\";/></a><div id=\"tag\" class=\"parentdiv\" style=\"display:none;\"><div id=\"childdiv\" class=\"center\"><a href='javascript:void(0);'><img align='right' src='screenshots/"+closeImagePath+"' height=\"20\" width=\"20;\" alt='Close' onclick=\"document.getElementById('tag').style.display='none';\"/></a><img id=\"f\" height=\"100%\" width=\"70%\"/></div></div></td></tr>");
													//bw.write("<tr><td colspan='8'><hr/></td></tr><tr style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;vertical-align:top'><td width='5%' style='word-wrap: break-word;'>"+testStepCode+"</td><td width='40%' style='word-wrap: break-word;'><pre style=\"height : auto ; max-height : 400px; overflow : auto; font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;vertical-align:top; white-space: pre-wrap; word-wrap: break-word;\"><b>description:</b> <br/><br/>    "+testStepObj.getString("teststepdescription")+"<br/><br/><b>Expected : </b><br/><br/>    "+testStepObj.getString("expectedoutput")+"<br/><br/><b>Observed : </b><br/><br/>    "+testStepObj.getString("observedoutput")+"<br/><br/><b>Status : </b><br/><br/>    "+passedOrFailedTestStep.replace("\"", "")+"<br/><br/><b>Execution Time:</b> <br/><br/>    "+testStepObj.getString("totalteststepstime")+"<br/><br/>"+failureTestStepComments+"</pre></td><td width='50%' style='word-wrap: break-word;'><a href='javascript:void(0);'><img height='400' width='500' src='screenshots/"+testStep+"' onclick=\"display('screenshots/"+testStep+"')\";/></a><div id=\"tag\" class=\"parentdiv\" style=\"display:none;\"><div id=\"childdiv\" class=\"center\"><a href='javascript:void(0);'><img align='right' src='screenshots/"+closeImagePath+"' height=\"20\" width=\"20;\" alt='Close' onclick=\"document.getElementById('tag').style.display='none';\"/></a><img id=\"f\" height=\"100%\" width=\"70%\"/></div></div></td></tr>");
													bw.write("<tr><td colspan='8'><hr/></td></tr><tr style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;vertical-align:top'><td width='5%' style='word-wrap: break-word;'>"+testStepId+"</td><td width='40%' style='word-wrap: break-word;'><pre style=\"height : auto ; max-height : 400px; overflow : auto; font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;vertical-align:top; white-space: pre-wrap; word-wrap: break-word;\"><b>Test Step Name:</b> <br/><br/>    "+testStepObj.getString("teststep")+"<br/><br/><br/><b>Description:</b> <br/><br/>    "+testStepObj.getString("teststepdescription")+"<br/><br/><br/><b>Executed Environment : </b><br/><br/>    "+testStepObj.getString("testenvironmentname")+"<br/><br/><b>Test Step Input : </b><br/><br/>    "+testStepObj.getString("teststepinput")+"<br/><br/><b>Expected : </b><br/><br/>   "+testStepObj.getString("expectedoutput")+"<br/><br/><b>Observed : </b><br/><br/>    "+testStepObj.getString("observedoutput")+"<br/><br/><b>Status : </b><br/><br/>    "+passedOrFailedTestStep.replace("\"", "")+"<br/><br/><b>Execution Time:</b> <br/><br/>    "+testStepObj.getString("totalteststepstime")+"<br/><br/>"+failureTestStepComments+"<br/></pre></td><td width='50%' style='word-wrap: break-word;'><a href='javascript:void(0);'><img height='400' width='500' src='screenshots/"+testStep+"' onclick=\"display('screenshots/"+testStep+"')\";/></a><div id=\"tag\" class=\"parentdiv\" style=\"display:none;\"><div id=\"childdiv\" class=\"center\"><a href='javascript:void(0);'><img align='right' src='screenshots/"+closeImagePath+"' height=\"20\" width=\"20;\" alt='Close' onclick=\"document.getElementById('tag').style.display='none';\"/></a><img id=\"f\" height=\"100%\" width=\"70%\"/></div></div></td></tr>");
												} else {
													log.info(testStep);
													closeImagePath = CommonUtility.getCatalinaPath().concat("\\webapps"+ File.separator + request.getContextPath() + File.separator +"css\\images\\noscreenshot_available.jpg");
													closeImagePath=closeImagePath.substring(closeImagePath.lastIndexOf("\\")+1, closeImagePath.length());
													//bw.write("<tr><td colspan='8'><hr/></td></tr><tr style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;vertical-align:top'><td width='10%' style='word-wrap: break-word;'>"+testStepCode+"</td><td width='40%' style='word-wrap: break-word;'><pre style=\"height : auto ; max-height : 400px; overflow : auto; font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;vertical-align:top; white-space: pre-wrap; word-wrap: break-word;\"><b>description:</b> <br/><br/>    "+testStepObj.getString("teststepdescription")+"<br/><br/><b>Expected : </b><br/><br/>    "+testStepObj.getString("expectedoutput")+"<br/><br/><b>Observed : </b><br/><br/>    "+testStepObj.getString("observedoutput")+"<br/><br/><b>Status : </b><br/><br/>    "+passedOrFailedTestStep.replace("\"", "")+"<br/><br/><b>Execution Time:</b> <br/><br/>    "+testStepObj.getString("totalteststepstime")+"<br/><br/>"+failureTestStepComments+"<br/></pre></td><td width='50%' style='word-wrap: break-word;'><img height='200' width='300' src='screenshots/"+closeImagePath+"')\";/>No screenshot available for the test step</td></tr>");
													//bw.write("<tr><td colspan='8'><hr/></td></tr><tr style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;vertical-align:top'><td width='10%' style='word-wrap: break-word;'>"+testStepCode+"</td><td width='40%' style='word-wrap: break-word;'><pre style=\"height : auto ; max-height : 400px; overflow : auto; font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;vertical-align:top; white-space: pre-wrap; word-wrap: break-word;\"><b>description:</b> <br/><br/>    "+testStepObj.getString("teststepdescription")+"<br/><br/><b>Expected : </b><br/><br/>    "+testStepObj.getString("expectedoutput")+"<br/><br/><b>Observed : </b><br/><br/>    "+testStepObj.getString("observedoutput")+"<br/><br/><b>Status : </b><br/><br/>    "+passedOrFailedTestStep.replace("\"", "")+"<br/><br/><b>Execution Time:</b> <br/><br/>    "+testStepObj.getString("totalteststepstime")+"<br/><br/>"+failureTestStepComments+"<br/></pre></td><td width='50%' style='word-wrap: break-word;'>No screenshot available for the test step</td></tr>");
													bw.write("<tr><td colspan='8'><hr/></td></tr><tr style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;vertical-align:top'><td width='5%' style='word-wrap: break-word;'>"+testStepId+"</td><td width='40%' style='word-wrap: break-word;'><pre style=\"height : auto ; max-height : 400px; overflow : auto; font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;vertical-align:top; white-space: pre-wrap; word-wrap: break-word;\"><b>Test Step Name:</b> <br/><br/>    "+testStepObj.getString("teststep")+"<br/><br/><br/><b>Description:</b> <br/><br/>    "+testStepObj.getString("teststepdescription")+"<br/><br/><br/><b>Executed Environment : </b><br/><br/>    "+testStepObj.getString("testenvironmentname")+"<br/><br/><b>Test Step Input : </b><br/><br/>    "+testStepObj.getString("teststepinput")+"<br/><br/><b>Expected : </b><br/><br/>    "+testStepObj.getString("expectedoutput")+"<br/><br/><b>Observed : </b><br/><br/>    "+testStepObj.getString("observedoutput")+"<br/><br/><b>Status : </b><br/><br/>    "+passedOrFailedTestStep.replace("\"", "")+"<br/><br/><b>Execution Time:</b> <br/><br/>    "+testStepObj.getString("totalteststepstime")+"<br/><br/>"+failureTestStepComments+"<br/></pre></td><td width='50%' style='word-wrap: break-word;'>No screenshot available for the test step</td></tr>");
												}

											}
										}
										bw.write("</table>");									
									}				
								}
							}
						}
						bw.write("<div id='End'><span style='float:right;font-weight:bold;font-family:DejaVu Sans, Arial, Helvetica,sans-serif;font-size:13px'><a style='text-decoration:none;' href='#' onclick='window.history.go(-1);return false'>Go Back</a> | <a style='text-decoration:none;' href='#TOP'>Beginning</a></span></div>");
						bw.write("<br/><br/>");
						bw.write("<div id='sign'><span style='float:left;font-weight:bold;font-family:DejaVu Sans, Arial, Helvetica,sans-serif;font-size:13px'>Username : "+loginUserName+"</span> <span style='float:right;font-weight:bold;font-family:DejaVu Sans, Arial, Helvetica,sans-serif;font-size:13px'>Signature : &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>");
						bw.write("</body></html>");
						bw.close();


						try{
							File screenshotFolder = null;
							File logoFolder  = null;
							FileOutputStream fos = new FileOutputStream(jobReportDirectory+productName+"-JR-"+testRunJobId+"-Evidence.zip");				    		
							ZipOutputStream zos = new ZipOutputStream(fos);
							List<String> screenshotImages = new ArrayList<String>();
							String closescreenshotPath = CommonUtility.getCatalinaPath().concat("\\webapps"+ File.separator + request.getContextPath() + File.separator + "css\\images\\close_screenshot_image.jpg");
							String noScreenshotAvailableImagePath = CommonUtility.getCatalinaPath().concat("\\webapps" + File.separator + request.getContextPath() + File.separator + "css\\images\\noscreenshot_available.jpg");
							screenshotImages.add(closescreenshotPath);
							screenshotImages.add(noScreenshotAvailableImagePath);
							screenshotImages.addAll(testStepPath);
							List<String> logoImage = new ArrayList<String>();
							logoImage.add(imageServerPath);
							List<String> names = new ArrayList<String>();
							names.add(JobLevelHTMLFIle);
							for(String name : names){				    			
								if(new File(name).exists()){
									zos.putNextEntry(new ZipEntry(new File(name).getName()));
									FileInputStream in = new FileInputStream(name);					    			
									if(in != null){
										int len;
										while ((len = in.read(buffer)) > 0) {
											zos.write(buffer, 0, len);
										}						    		
										zos.closeEntry();
										in.close();
									}			
								}
							}	
							for(String name : screenshotImages){
								screenshotFolder  = new File(jobReportDirectory+productName+"-JR-"+testRunJobId+File.separator+"screenshots");
								if(!screenshotFolder.exists()){
									screenshotFolder.mkdirs();				    				
								}
								if(name != null && name.endsWith(".png")){
									File screenshotFile = new File(name);
									if(screenshotFile.exists()){
										FileUtils.copyFile(screenshotFile, new File(screenshotFolder+File.separator+screenshotFile.getName()));
									}
								}else{
									log.info("Image is not captured with the teststep :"+name);
								}
							}	
							for(String name : logoImage){	
								log.info("logo path is :"+name);
								logoFolder  = new File(jobReportDirectory+productName+"-JR-"+testRunJobId+File.separator+"logo");
								if(!logoFolder.exists()){
									logoFolder.mkdirs();				    				
								}
								File logoFile = new File(name);
								if(logoFile.exists()){
									FileUtils.copyFile(logoFile, new File(logoFolder+File.separator+logoFile.getName()));
								}
							}	
							zipDir(zos,"",screenshotFolder);
							zipDir(zos,"",logoFolder);				    		
							zos.close();				    		
						}catch(IOException ex){
							log.info("Error while iterating data :"+ex);
							if(testRunJobId!=-1){				
								jTableResponse = new JTableResponse("ERROR","No records available for the current Job "+testRunJobId);
							} else if(testRunNo != -1){
								jTableResponse = new JTableResponse("ERROR","No records available for the current Workpackage "+testRunNo);
							}
							return jTableResponse;
						}
						log.info("JobLevelHTMLFIle is"+JobLevelHTMLFIle);
						//JobLevelHTMLFIle = jobReportDirectory+productName+"-JR-"+testRunJobId+"-Evidence.zip";	fgf
						JobLevelHTMLFIle = jobReportDirectory+productName+"-JR-"+testRunJobId+"-Evidence.zip";
						ZipTool.unzip(JobLevelHTMLFIle, jobReportDirectory+productName+"-JR-"+testRunJobId+"-Evidence");
						fileToOpenInBrowser = jobReportDirectory +productName+"-JR-"+testRunJobId+"-Evidence"+File.separator +productName+"-JR-"+testRunJobId+"-Evidence.html"; 
						fileToDownload = jobReportDirectory +productName+"-JR-"+testRunJobId+"-Evidence.zip";						
					} catch (IOException | JSONException e) {		
						log.error("Error while iterating data "+e);
						if(testRunJobId!=-1){				
							jTableResponse = new JTableResponse("ERROR","No records available for the current Job "+testRunJobId);
						} else if(testRunNo != -1){
							jTableResponse = new JTableResponse("ERROR","No records available for the current Workpackage "+testRunNo);
						}
						return jTableResponse;
					} 					
				} else {
					log.info("no data for job level report");
					if(testRunJobId!=-1){				
						jTableResponse = new JTableResponse("ERROR","No records available for the current Job "+testRunJobId);
					} else if(testRunNo != -1){
						jTableResponse = new JTableResponse("ERROR","No records available for the current Workpackage "+testRunNo);
					}
				} 

			}else{
				log.info("no data for job level report");
				if(testRunJobId!=-1){				
					jTableResponse = new JTableResponse("ERROR","No records available for the current Job "+testRunJobId);
				} else if(testRunNo != -1){
					jTableResponse = new JTableResponse("ERROR","No records available for the current Workpackage "+testRunNo);
				}
			}

		}
		if (productTypeName.equalsIgnoreCase("WORKPACKAGE")){
			if(customerName.equalsIgnoreCase("Rockwell Collins")) {
				jasperPrint = report.generateTestRunListRCReport(testRunNo, testRunConfigurationChildId, strPrintMode, productType, logo, loginUserName);
			} else {					
				List<String>htmlReportData = new ArrayList<String>();
				Set<String> hostNameSet = new HashSet<String>();
				String hostName = "";
				String evidencePathFolder = null;
				StringBuilder multipleHostIds = new StringBuilder();
				Map<String,String> hostNameMap = new HashMap<String,String>();
				File testJobEvidenceFolderPath = null;
				log.info("Going to TestRunListHtmlReport : " + testRunNo + " : " + testRunConfigurationChildId+"  :  "+productTypeName);
				htmlReportData = report.generateTestRunListHtmlReport(testRunNo, testRunConfigurationChildId,strPrintMode,productType,logo, loginUserName);
				log.info("after getting the html report data");
				if(!htmlReportData.isEmpty()){
					for(int i=0;i<htmlReportData.size();i++){
						jsonSourceFile = htmlReportData.get(0);
						jsonSourceSubFile1 = htmlReportData.get(1);
						jsonSourceSubFile2 = htmlReportData.get(2);
						jsonSourceSubFile3 = htmlReportData.get(3);
						jsonSourceSubFile4 = htmlReportData.get(4);
					}
					if(new org.json.JSONArray(htmlReportData.get(1)).length() > 0){
						org.json.JSONArray jsonReportSummaryArray = new org.json.JSONArray(jsonSourceFile);
						org.json.JSONArray jsonJobReportArray = new org.json.JSONArray(jsonSourceSubFile1);
						org.json.JSONArray jsonJobSummaryArray = new org.json.JSONArray(jsonSourceSubFile2);
						org.json.JSONArray jsonTestCaseSummaryArray = new org.json.JSONArray(jsonSourceSubFile3);
						org.json.JSONArray jsonTestStepArray = new org.json.JSONArray(jsonSourceSubFile4);
						log.info("jsonReportSummaryArray values are :"+jsonReportSummaryArray);
						log.info("jsonJobReportArray values are :"+jsonJobReportArray);
						log.info("jsonJobSummaryArray values are :"+jsonJobSummaryArray);
						log.info("jsonTestCaseSummaryArray values are :"+jsonTestCaseSummaryArray);
						log.info("jsonTestStepArray values are :"+jsonTestStepArray);	
						try{
							log.debug("workpackageName is" +workpackageName);
							List<String>testStepPath = new ArrayList<String>();
							Map<String,String>jobResultWithJobId = new HashMap<String, String>();
							for(int i=0;i<jsonJobSummaryArray.length();i++){
								org.json.JSONObject jobSummaryObject =jsonJobSummaryArray.getJSONObject(i);
								jobResultWithJobId.put(jobSummaryObject.getString("testrunlistid"), jobSummaryObject.getString("testcaseresult"));
							}
							log.info("jobResultWithJobId"+jobResultWithJobId);
							String contextPath = request.getContextPath().contains("/") ? request.getContextPath().replace("/", "").replace("\\", "") : request.getContextPath().replace("\\", "");
							workPackageReportDirectory = CommonUtility.getCatalinaPath()+ File.separator + "webapps" + File.separator + contextPath + File.separator+"WPReport"+File.separator;
							workpackageHtmlFile = workPackageReportDirectory+ productName+"-WP-"+testRunNo+"-Evidence.html";
							File htmFile = new File(workpackageHtmlFile);
							if(!htmFile.exists()){
								new File(htmFile.getParent()).mkdirs();
							}
							byte[] buffer = new byte[1024];
							//List<String>testEnvironmentsList=new ArrayList<String>();
							StringBuilder testEnvironment =new StringBuilder();
							String deviceMakeandModel = "";
							boolean WPHasDeviceMakeAndModel = false;
							StringBuilder deviceMakeAndModelBuilder = new StringBuilder();
							String testEnvironmentName ="" , testRunNoForHostids = "",deviceMake = "" , deviceModel = "";
							Charset charset = Charset.forName("UTF-8");
							//BufferedWriter bw = new BufferedWriter(new FileWriter(new File(workpackageHtmlFile)));
							Path workpackageHtmlFilePath = Paths.get(workpackageHtmlFile, new String[0]);
							BufferedWriter bw = Files.newBufferedWriter(workpackageHtmlFilePath, charset);
							bw.write("<html><head><META http-equiv=Content-Type content='text/html; charset=utf-8'><style>.center {margin: auto;width: 75%;border: 3px solid #473572;padding: 10px;position : fixed;background:white;height:60%;}.parentdiv {margin: auto; width: 80%;height:50%;border: 3px;padding: 10px;position : fixed;top:20%;left:8%;right:auto;}body {background-color: #FFFFFF;}th{text-align: left;color:white;background: #3c6ac6;}a {color:blue;} a:visited {color:blue;}a:hover {color:blue;}a:active {color:blue;}</style><title>Workpackage Evidence Report</title><script>function display(e){document.getElementById(\"tag\").style.display='';var img = document.getElementById(\"f\");img.setAttribute(\"src\",e);document.getElementById(\"childdiv\").appendChild(img);return false;}</script></head><body>");
							try {
								for(int i=0;i<jsonJobReportArray.length();i++){
									org.json.JSONObject jsonObject = jsonJobReportArray.getJSONObject(i);
									//Obtaining Run Configuration for Product Type - Starts
									RunConfiguration rc = productService.getRunConfigurationById(Integer.valueOf(jsonObject.getString("runconfigid")));	
									String runconfigType = "N/A";
									if(rc != null && rc.getProductType() != null && rc.getProductType().getTypeName() != null)
										runconfigType = rc.getProductType().getTypeName();
									//Obtaining Run Configuration for Product Type - Ends
									if(jsonObject.getString("job_name")!=null&&!jsonObject.getString("job_name").isEmpty()){
										testEnvironmentName = jsonObject.getString("job_name");
										testEnvironment.append(testEnvironmentName+";"); 
									}
									if(runconfigType.equalsIgnoreCase("Device") || runconfigType.equalsIgnoreCase("Mobile")){
										if(!jsonObject.getString("devicemake").equalsIgnoreCase("N/A") && !jsonObject.getString("devicemodel").equalsIgnoreCase("N/A")){
											deviceMake = jsonObject.getString("devicemake");
											deviceModel = jsonObject.getString("devicemodel");
											deviceMakeAndModelBuilder.append(deviceMake+"&"+deviceModel+";"); 
											WPHasDeviceMakeAndModel = true;
										}else{
											deviceMakeAndModelBuilder.append("N/A");
										}										
									}
									
									if(jsonObject.getString("hostname") != null && !jsonObject.getString("hostname").isEmpty()){
										testRunNoForHostids = jsonObject.getString("testrunno");
										if(hostNameSet.add(jsonObject.getString("hostname"))){
											hostName = jsonObject.getString("hostname");
											multipleHostIds.append(hostName+"; "); 
										}
									}
								}			
								testEnvironmentName = "";
								deviceMake = "";
								deviceModel = "";
								testEnvironmentName = testEnvironment.substring(0, (testEnvironment.lastIndexOf(";")));
								log.info("testEnvironment values are :"+testEnvironmentName);
								if(WPHasDeviceMakeAndModel)
									deviceMakeandModel = deviceMakeAndModelBuilder.substring(0, (deviceMakeAndModelBuilder.lastIndexOf(";")));
								else
									deviceMakeandModel = "N/A & N/A";
								hostName = "";
								hostName = multipleHostIds.substring(0, (multipleHostIds.lastIndexOf("; ")));
								hostNameMap.put(testRunNoForHostids, hostName);
								hostNameSet.clear();
								org.json.JSONObject jsonObject = jsonReportSummaryArray.getJSONObject(0);

								bw.write("<table width='100%'><tr style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #FFFFFF; font-size: 24px; height:62px;background:#003ca4; font-weight: bold;'><td><img style=\"width:50px;height:50px;\" src='logo/"+imageFile.getName()+"'/></td> <td align='middle' id='TOP'>"+jsonObject.getString("productname")+" - "+jsonObject.getString("testrunconfigurationname")+" - Evidence Report </td></tr><tr style='height:13px'></tr></table>");		
								bw.write("<table bgcolor='white' width='100%' style='table-layout: fixed;'>");
								//report summary
								String result = jsonObject.getString("testcaseresult");
								String passedOrFailed = "";
								if(result.equalsIgnoreCase("PASSED")){
									passedOrFailed = "<td style='font-weight: bold'> : <span style='font-family: DejaVu Sans, Arial, Helvetica, sans-serif;color: #00C800;font-size: 12px;font-weight: bold'> "+result+"</span></td>";
								} else {
									passedOrFailed = "<td style='font-weight: bold'> : <span style='font-family: DejaVu Sans, Arial, Helvetica, sans-serif;color: #c80000;font-size: 12px;font-weight: bold'> "+result+"</span></td>";
								}
								bw.write("<tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px;width:150;'>Product Name </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold' > : "+jsonObject.getString("productname")+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px;width:150;'>Work Package No </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jsonObject.getString("testrunno")+"</td></tr><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Product version	</th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jsonObject.getString("productversionname")+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px;width:150;'>Device Make & Model </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold;word-wrap: break-word;'> : "+deviceMakeandModel+"</td></tr><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test Run Plan	</th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jsonObject.getString("testrunconfigurationname")+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Triggered Time </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jsonObject.getString("testruntriggeredtime")+"</td></tr><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test Suite	</th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jsonObject.getString("testsuiteid")+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test Cases Passed </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jsonObject.getInt("passedtest")+"/"+jsonObject.getInt("totaltest")+"</td></tr><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test Start Date & Time </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jsonObject.getString("testrunstarttime")+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test Steps Passed </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jsonObject.getInt("passedtestecase")+"/"+jsonObject.getInt("totaltestcase")+"</td></tr><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px;width:150;'>Test Environment    </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold;word-wrap: break-word;'> : "+testEnvironmentName+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test End Date & Time </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jsonObject.getString("testrunendtime")+"</td></tr><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Host </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+hostName+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test Result	</th>"+passedOrFailed.replace("\"", "")+"</tr>");

							} catch (JSONException e) {
								log.info("Error while iterating source file json data "+e);
							}
							bw.write("</table>");
							//Job Report Details					
							bw.write("<table width='100%' style='table-layout: fixed;'><tr></tr><tr align='left' style='font-family:DejaVu Sans, Arial, Helvetica,sans-serif;color:#ffffff;background:#003ca4;font-weight: bold;height:25px'><td id='JOBREPORT'>JOB REPORT<span style='float:right'><a style='text-decoration:none;font-weight:bold;color:#ffffff;font-size:13px;' href='#End'>End</a></span></td></tr></table>");		
							bw.write("<table width='100%' style='table-layout: fixed;'>");
							bw.write("<tr style='color:#ffffff;height:18px;font-family: Arial;background: #3c6ac6;font-size: 12px; font-weight: bold'><th>Job ID</th><th>Product Type</th><th>Environment</th><th>Device ID</th><th>Device Platform</th><th>Status</th><th>Result</th><th>Start Date & Time </th><th>End Date & Time</th><th>Comments</th></tr>");
							String job_id="";
							for(int i=0;i<jsonJobReportArray.length();i++){			
								org.json.JSONObject jsonJobReportObject = jsonJobReportArray.getJSONObject(i);
								String jobResult = jsonJobReportObject.getString("testresultstatus");
								String jobStatus = jsonJobReportObject.getString("result");
								//Obtaining Run Configuration for Product Type - Starts
								RunConfiguration rc = productService.getRunConfigurationById(Integer.valueOf(jsonJobReportObject.getString("runconfigid")));	
								String runconfigType = "N/A";
								if(rc != null && rc.getProductType() != null && rc.getProductType().getTypeName() != null)
									runconfigType = rc.getProductType().getTypeName();
								//Obtaining Run Configuration for Product Type - Ends
								String passedOrFailedJobResult = "",passedOrFailedJobStatus="";
								if(jobStatus.equalsIgnoreCase("5")){
									passedOrFailedJobStatus = "<td>Completed</td>";
								}
								else if(jobStatus.equalsIgnoreCase("7")){
									passedOrFailedJobStatus = "<td>Aborted</td>";
								}
								for(Map.Entry<String, String> entry : jobResultWithJobId.entrySet()){
									if(entry.getKey().equalsIgnoreCase(jsonJobReportObject.getString("job_id"))){
										passedOrFailedJobResult=entry.getValue();
										if(passedOrFailedJobResult.equalsIgnoreCase("PASSED")){
											passedOrFailedJobResult = "<td style='color:#00C800;font-family: DejaVu Sans, Arial, Helvetica, sans-serif;font-size: 12px;'><b>"+passedOrFailedJobResult+"</b></td>";
										} else {
											passedOrFailedJobResult = "<td style='color:#c80000;font-family: DejaVu Sans, Arial, Helvetica, sans-serif;font-size: 12px;'><b>"+passedOrFailedJobResult+"</b></td>";				
										}	 
										bw.write("<tr><td colspan='9'><hr/></td></tr><tr style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;vertical-align:top'><td><a style='text-decoration: none;font-weight:bold' href=#"+jsonJobReportObject.getString("job_id")+">"+jsonJobReportObject.getString("job_id")+"</a></td><td>"+runconfigType+"</td><td>"+jsonJobReportObject.getString("job_name")+"</td><td>"+jsonJobReportObject.getString("device_id")+"</td><td align='middle'>"+jsonJobReportObject.getString("device_platform")+"</td>"+passedOrFailedJobStatus.replace("\"", "")+passedOrFailedJobResult.replace("\"", "")+"<td>"+jsonJobReportObject.getString("start_time")+"</td><td>"+jsonJobReportObject.getString("end_time")+"</td><td>"+jsonJobReportObject.getString("comments")+"</td></tr>");	
									}
								}
							}						
							bw.write("</table>");
							boolean flagForJobSummaryIteration = true;
							File wpFolder  = new File(workPackageReportDirectory + productName+"-WP-"+testRunNo+File.separator+"screenshots");
							if(!wpFolder.exists()){
								wpFolder.mkdirs();
							}
							for(int l=0;l<jsonJobReportArray.length();l++){	
								org.json.JSONObject jsonJobReportObject = jsonJobReportArray.getJSONObject(l);
								//Copy the evidence folder of the job id to the workpackage folder
								//If 
								evidencePathFolder = CommonUtility.getCatalinaPath()+ evidence_Folder;
								
								/*if(jsonJobReportObject.getString("reportmode") != null && !jsonJobReportObject.getString("reportmode").isEmpty() && !jsonJobReportObject.getString("reportmode").equalsIgnoreCase("N/A"))
									workPackageMode = jsonJobReportObject.getString("reportmode");*/
								if(workPackageMode.equalsIgnoreCase("COMBINED JOB")){
									//List<String> jobIds = report.getAllJobIdsByWorkpackageId(testRunNo);
									if(jobsSet != null && jobsSet.size() > 0){
										for(TestRunJob testJob : jobsSet){
											testJobEvidenceFolderPath = new File(evidencePathFolder + File.separator + testJob.getTestRunJobId() + File.separator + "SCREENSHOT");
											if(testJobEvidenceFolderPath.exists())
												FileUtils.copyDirectory(testJobEvidenceFolderPath, wpFolder);
										}
									}
								}
								else{
									testJobEvidenceFolderPath = new File(evidencePathFolder + File.separator + jsonJobReportObject.getString("job_id") + File.separator + "SCREENSHOT");
									if(testJobEvidenceFolderPath.exists())
										FileUtils.copyDirectory(testJobEvidenceFolderPath, wpFolder);
								}
								for(int m = 0; m < jsonJobSummaryArray.length();m++){
									org.json.JSONObject jobSummaryObject = jsonJobSummaryArray.getJSONObject(m);									
									if(jsonJobReportObject.getString("job_id").equals(jobSummaryObject.getString("testrunlistid")) && flagForJobSummaryIteration){
										//Job Summary details
										bw.write("<table width='100%' style='table-layout: fixed;'><tr style='height:10px'></tr><tr align='left' style='font-family:DejaVu Sans, Arial, Helvetica,sans-serif;color:#ffffff;background:#003ca4;font-weight: bold;height:27px'><td id=JOBSUMMARY"+jobSummaryObject.getString("testrunlistid")+">JOB SUMMARY<span style='float:right;'><a style='text-decoration:none;font-weight:bold;font-size:13px;color:#ffffff;' href=#JOBREPORT>Job Report | </a><a style='text-decoration:none;font-weight:bold;font-size:13px;color:#ffffff;' href=#TOP>Beginning | </a><a style='text-decoration:none;font-weight:bold;font-size:13px;color:#ffffff;' href=#End>End</a></span></td></tr>");
										bw.write("<table width='100%' style='table-layout: fixed;'><tr style='font-family: DejaVu Sans, Arial, Helvetica, sans-serif;height:25px;background: #3c6ac6; color: #FFFFFF; font-size: 12px;'><td id="+jobSummaryObject.getString("testrunlistid")+">Job No : "+jobSummaryObject.getString("testrunlistid")+"</td><td>Environment : "+jsonJobReportObject.getString("job_name")+"</td><td>Device ID : "+jsonJobReportObject.getString("device_id")+"</td><td>Device Platform : "+jsonJobReportObject.getString("device_platform")+"</td><td>Host Platform : "+jsonJobReportObject.getString("hostipaddress")+"</td></tr></table>");
										bw.write("<table width='100%' style='table-layout: fixed;'>");
										String jobresultStatus = jobSummaryObject.getString("testcaseresult");
										String passedOrFailedJobStatus = "";
										String device="",devicePlatForm="";
										if(jobresultStatus.equalsIgnoreCase("PASSED")){
											passedOrFailedJobStatus = "<td style='font-weight: bold'> : <span style='font-family: DejaVu Sans, Arial, Helvetica, sans-serif;color: #00C800;font-size: 12px;font-weight: bold'> "+jobresultStatus+"</span></td>";
										} else {
											passedOrFailedJobStatus = "<td style='font-weight: bold'> : <span style='font-family: DejaVu Sans, Arial, Helvetica, sans-serif;color: #c80000;font-size: 12px;font-weight: bold'> "+jobresultStatus+"</span></td>";						
										}
										if((jobSummaryObject.getString("deviceid").equalsIgnoreCase("N/A")&&jobSummaryObject.getString("devicemodel").equalsIgnoreCase("N/A"))&&jobSummaryObject.getString("deviceplatformname").equalsIgnoreCase("N/A")&&jobSummaryObject.getString("deviceplatformversion").equalsIgnoreCase("N/A")){
											device="<td style='font-weight: bold'> : <span style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; font-weight: bold'>N/A</td>";
											devicePlatForm="<td style='font-weight: bold'> : <span style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; font-weight: bold'>N/A</td>";
										}
										else{
											device="<td style='font-weight: bold'> : <span style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; font-weight: bold'>"+jobSummaryObject.getString("devicemodel")+","+jobSummaryObject.getString("deviceid")+"</span></td>";
											devicePlatForm="<td style='font-weight: bold'> : <span style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; font-weight: bold'>"+jobSummaryObject.getString("deviceplatformname")+" "+jobSummaryObject.getString("deviceplatformversion")+"</span></td>";
										}
										bw.write("<tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px;width:150;'>Product Name </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;height:17px; font-weight: bold' > : "+jobSummaryObject.getString("productname")+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px;width:150;'>Device </th>"+device+"</tr><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Product version	</th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;font-weight: bold'> : "+jobSummaryObject.getString("productversionname")+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Device Platform </th>"+devicePlatForm+"</tr><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test Run Plan	</th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jobSummaryObject.getString("testrunconfigurationname")+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test Cases Passed </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jobSummaryObject.getInt("passedtest")+"/"+jobSummaryObject.getInt("totaltest")+"</td></tr><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test Suite	</th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jobSummaryObject.getString("testsuitename")+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test Steps Passed </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jobSummaryObject.getInt("passedtestecase")+"/"+jobSummaryObject.getInt("totaltestcase")+"</td></tr><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test Start Date & Time </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jobSummaryObject.getString("testrunstarttime")+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test End Date & Time </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jobSummaryObject.getString("testrunendtime")+"</td></tr><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test Tool </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jobSummaryObject.getString("testtoolname")+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test result	</th>"+passedOrFailedJobStatus.replace("\"", "")+"</tr>");
										bw.write("</table>");
										flagForJobSummaryIteration = false;
									}
								}
								//Test case summary details		
								flagForJobSummaryIteration = true;
								String totalTCExecutionTime="";
								String testCaseId="";								
								String testCaseType = "Test Case Code";
								Integer testCaseIdFromTCSummary = null;
								Set<Integer> listOfTestCaseIds = new HashSet<Integer>(); 
								boolean testCaseTypeFlag = true;
								String testCaseStatus = "PASSED";

								boolean isCombinedJobTCFailed = false;
								Set<Integer> testCaseListForTSCount = new HashSet<Integer>();

								Map<Integer,String> testcaseidWithStatus = new HashMap<>();
								//Map<Integer,String> testStepCountForTC = new HashMap<>();
								Integer testStepPassedCount = 0,totalTestStepCount = 0;
								Map<Integer, List<Integer>> testStepCountForTCMap = new HashMap<Integer, List<Integer>>();
								Set<Integer> listOfTestCaseIdsForTSCount = new HashSet<Integer>();
								List<Integer> passedAndTotalTS =  null;
								for(int j=0;j<jsonTestCaseSummaryArray.length();j++){			
									org.json.JSONObject testCaseObj = jsonTestCaseSummaryArray.getJSONObject(j);
									testCaseIdFromTCSummary = Integer.valueOf(testCaseObj.getString("test_case_id"));
									String testcasename = testCaseObj.getString("test_case");
									if(testCaseObj != null && testCaseObj.getString("result") != null && 
											testCaseObj.getString("result").trim().equalsIgnoreCase("FAILED") ) {
										isCombinedJobTCFailed = true;
										testCaseStatus = "FAILED";
										testCaseTypeFlag = false;
									}else{
										if(listOfTestCaseIds.add(Integer.valueOf(testCaseObj.getString("test_case_id"))))
											testCaseStatus = "PASSED";
									}
									testcaseidWithStatus.put(Integer.valueOf(testCaseObj.getString("test_case_id")), testCaseStatus);

									if(testCaseObj.getString("job_id").equals(jsonJobReportObject.getString("job_id"))){
										totalTCExecutionTime = testCaseObj.getString("totaltcexectime");
									}
									if(testCaseObj.getString("testcasecode").equalsIgnoreCase("N/A")&&testCaseTypeFlag){
										testCaseType = "Test Case Id";
										testCaseTypeFlag = false;
									}
									passedAndTotalTS = new LinkedList<Integer>();
									if(testCaseObj.getString("job_id").equalsIgnoreCase(jsonJobReportObject.getString("job_id")) && testCaseListForTSCount.add(testCaseIdFromTCSummary)){
										for(int k=0;k < jsonTestStepArray.length(); k++){
											org.json.JSONObject testStepObj = jsonTestStepArray.getJSONObject(k);
											if(workPackageMode.equalsIgnoreCase("COMBINED JOB")){
												job_id = testStepObj.getString("combined_job_id");
											}else{
												job_id = testStepObj.getString("job_id");
											}

											if(testStepObj.getString("testcasename").equalsIgnoreCase(testcasename) && job_id.equalsIgnoreCase(jsonJobReportObject.getString("job_id"))){
											//if(testStepObj.getString("testcasename").equalsIgnoreCase(testcasename)){
												String testStepresultStatus = testStepObj.getString("resultstatus");
												if(testStepresultStatus.equalsIgnoreCase("PASSED")){
													testStepPassedCount ++;
												}
												totalTestStepCount ++;
											}

										}
										passedAndTotalTS.add(testStepPassedCount);
										passedAndTotalTS.add(totalTestStepCount);
										if(listOfTestCaseIdsForTSCount.add(Integer.valueOf(testCaseObj.getString("test_case_id"))))
											testStepCountForTCMap.put(Integer.valueOf(testCaseObj.getString("test_case_id")),passedAndTotalTS);
										testStepPassedCount = 0;
										totalTestStepCount = 0;
										//listOfTestCaseIds.add(Integer.valueOf(testCaseObj.getString("test_case_id")));
									}
								}
								//Multimap<Integer, String> multimap = ArrayListMultimap.create();
								ListMultimap<Integer, Object> multimap = ArrayListMultimap.create();
								multimap = report.getFeatureNamesByTestCaseIdList(listOfTestCaseIds);
								bw.write("<table width='100%'><tr style='height:13px'></tr><tr style='font-family:DejaVu Sans, Arial, Helvetica,sans-serif;color:#ffffff;background:#003ca4;font-weight: bold;height:26px;font-size: 12px;'><td id=TESTCASESUMMARY"+jsonJobReportObject.getString("job_id")+">TEST CASE SUMMARY</td><td><span style='float:left'>Total Execution Time : "+totalTCExecutionTime+"</span><span style='float:right'><a style='text-decoration:none;font-weight:bold;color:#ffffff;font-size:13px;' href=#JOBSUMMARY"+jsonJobReportObject.getString("job_id")+">"+"Job Summary | "+"</a><a style='text-decoration:none;font-weight:bold;color:#ffffff;font-size:13px;' href=#JOBREPORT>Job Report | </a><a style='text-decoration:none;font-weight:bold;color:#ffffff;font-size:13px;' href=#TOP>Beginning | </a><a style='text-decoration:none;font-weight:bold;color:#ffffff;font-size:13px;' href=#End>End</a></span></td></tr>");
								bw.write("<table bgcolor='white' width='100%'>");		
								bw.write("<tr style='color:#ffffff;background: #3c6ac6;font-family:Arial;font-size:12px'><th>"+testCaseType+"</th><th>Test Case Name</th><th>Description</th><th>Result</th><th>Start Date & Time</th><th>End Date & Time</th><th>Execution Time</th></tr>");

								Set<Integer> testCaseIdSet = new HashSet<Integer>();
								if(workPackageMode != null && workPackageMode.equalsIgnoreCase("COMBINED JOB")){
									//wptcExecutionList = workPackageService.getWPTCExecutionPlanByWpid(testRunNo);
									for(int j=0;j<jsonTestCaseSummaryArray.length();j++){			
										org.json.JSONObject testCaseObj = jsonTestCaseSummaryArray.getJSONObject(j);
										testCaseIdFromTCSummary = Integer.valueOf(testCaseObj.getString("test_case_id"));
										//String result = testCaseObj.getString("result");
										String result = testcaseidWithStatus.get(testCaseIdFromTCSummary);
										String passedOrFailed = "";
										if(testCaseIdFromTCSummary != null && testCaseIdSet.add(testCaseIdFromTCSummary)){
											if(result.equalsIgnoreCase("PASSED")){
												passedOrFailed = "<td style='color:#00C800;font-family: DejaVu Sans, Arial, Helvetica, sans-serif;font-size: 12px;'><b>"+result+"</b></td>";
											} else {
												passedOrFailed = "<td style='color:#c80000;font-family: DejaVu Sans, Arial, Helvetica, sans-serif;font-size: 12px;'><b>"+result+"</b></td>";				
											}
											if(testCaseObj.getString("testcasecode").equalsIgnoreCase("N/A")){
												testCaseId = testCaseObj.getString("test_case_id");
											}
											else{
												testCaseId = testCaseObj.getString("testcasecode");
											}
											bw.write("<tr><td colspan='7'><hr/></td></tr><tr style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;vertical-align:top'><td>"+testCaseId+"</td><td><a style='text-decoration: none;font-weight:bold' href=#"+testCaseObj.getString("test_case")+jsonJobReportObject.getString("job_id")+">"+testCaseObj.getString("test_case")+"</a></td><td>"+testCaseObj.getString("description")+"</td>"+passedOrFailed.replace("\"", "")+"<td>"+testCaseObj.getString("starttime")+"</td><td>"+testCaseObj.getString("endtime")+"</td><td>"+testCaseObj.getString("totaltime")+"</td></tr>");
										}

									}	
								}else{
									for(int j=0;j<jsonTestCaseSummaryArray.length();j++){			
										org.json.JSONObject testCaseObj = jsonTestCaseSummaryArray.getJSONObject(j);
										testCaseIdFromTCSummary = Integer.valueOf(testCaseObj.getString("test_case_id"));
										if(testCaseObj.getString("job_id").equals(jsonJobReportObject.getString("job_id"))){
											String result = testCaseObj.getString("result");
											String passedOrFailed = "";
											if(result.equalsIgnoreCase("PASSED")){
												passedOrFailed = "<td style='color:#00C800;font-family: DejaVu Sans, Arial, Helvetica, sans-serif;font-size: 12px;'><b>"+result+"</b></td>";
											} else {
												passedOrFailed = "<td style='color:#c80000;font-family: DejaVu Sans, Arial, Helvetica, sans-serif;font-size: 12px;'><b>"+result+"</b></td>";				
											}
											if(testCaseObj.getString("testcasecode").equalsIgnoreCase("N/A")){
												testCaseId=testCaseObj.getString("test_case_id");
											}
											else{
												testCaseId=testCaseObj.getString("testcasecode");
											}
											bw.write("<tr><td colspan='7'><hr/></td></tr><tr style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;vertical-align:top'><td>"+testCaseId+"</td><td><a style='text-decoration: none;font-weight:bold' href=#"+testCaseObj.getString("test_case")+jsonJobReportObject.getString("job_id")+">"+testCaseObj.getString("test_case")+"</a></td><td>"+testCaseObj.getString("description")+"</td>"+passedOrFailed.replace("\"", "")+"<td>"+testCaseObj.getString("starttime")+"</td><td>"+testCaseObj.getString("endtime")+"</td><td>"+testCaseObj.getString("totaltime")+"</td></tr>");

										}						
									}	
								}
								bw.write("</table>");							
								//Test Case Details
								bw.write("<table width='100%'><tr></tr><tr align='left' style='font-family:DejaVu Sans, Arial, Helvetica,sans-serif;color:#ffffff;background:#003ca4;font-weight: bold;height:26px;font-size: 12px'><td>TEST CASE DETAILS</td></tr></table>");
								Integer actualJobIdForTC = null;
								Set<Integer> testCaseList = new HashSet<Integer>();
								List<Integer> passsedAndTotalTSCount = new LinkedList<Integer>();
								//for(TestRunJob testJob : jobsSet){
								//Integer jobId = testJob.getTestRunJobId();
								for(int k = 0; k < jsonTestCaseSummaryArray.length(); k++){			
									org.json.JSONObject testCaseObj = jsonTestCaseSummaryArray.getJSONObject(k);				
									String testcasename = testCaseObj.getString("test_case");
									testCaseIdFromTCSummary = Integer.valueOf(testCaseObj.getString("test_case_id"));
									if(testCaseObj.getString("test_case_id") != null && !testCaseObj.getString("test_case_id").equalsIgnoreCase("N/A") )
										actualJobIdForTC = Integer.valueOf(testCaseObj.getString("actualtestjobid"));
									//if(actualJobIdForTC.equals(jsonJobReportObject.getString("job_id"))){
									if(testCaseObj.getString("job_id").equalsIgnoreCase(jsonJobReportObject.getString("job_id")) && testCaseList.add(testCaseIdFromTCSummary)){
										//Test case with description
										String featurenames = "";
										StringBuffer sbFeatureNames = new StringBuffer();
										List<Object> mappedFeaturesToTestCase = multimap.get(testCaseIdFromTCSummary);
										if(mappedFeaturesToTestCase.size() > 0){
											for(int ii=0;ii < mappedFeaturesToTestCase.size();ii++){
												if(mappedFeaturesToTestCase.get(ii) != null){
													featurenames = mappedFeaturesToTestCase.get(ii).toString();
													sbFeatureNames.append(featurenames+",");
												}
											}
											if(sbFeatureNames != null && sbFeatureNames.length() > 0){
												featurenames = sbFeatureNames.toString();
												featurenames = featurenames.substring(0,featurenames.length()-1);
											}
										}else{
											featurenames = "N/A";
										}

										bw.write("<table width='100%'>");
										bw.write("<tr style='font-family:Arial;color:#ffffff;font-size:12px;'><th colspan='4' align='left' id="+testCaseObj.getString("test_case")+jsonJobReportObject.getString("job_id")+">Test Case Name  	:"+testCaseObj.getString("test_case")+"</th><th><span style='float:right'><a style='text-decoration:none;font-weight:bold;font-size:13px;color:ffffff;' href=#TESTCASESUMMARY"+jsonJobReportObject.getString("job_id")+">"+"Test Case Summary | "+"</a><a style='text-decoration:none;font-weight:bold;font-size:13px;color:ffffff;' href=#JOBSUMMARY"+jsonJobReportObject.getString("job_id")+">"+"Job Summary | "+"</a><a style='text-decoration:none;font-weight:bold;font-size:13px;color:ffffff;' href=#JOBREPORT>Job Report | </a><a style='text-decoration:none;font-weight:bold;font-size:13px;color:ffffff;' href=#TOP>Beginning | </a><a style='text-decoration:none;font-weight:bold;font-size:13px;color:ffffff;' href=#End>End</a></span></th></tr></table>");
										String testcaseresultStatus = "";

										if(workPackageMode != null &&  workPackageMode.equalsIgnoreCase("COMBINED JOB")){
											testcaseresultStatus = testcaseidWithStatus.get(testCaseIdFromTCSummary);
											passsedAndTotalTSCount = testStepCountForTCMap.get(testCaseIdFromTCSummary);
											if(passsedAndTotalTSCount != null && passsedAndTotalTSCount.size() > 0){
												testStepPassedCount = passsedAndTotalTSCount.get(0);
												totalTestStepCount = passsedAndTotalTSCount.get(1);
											}
										}else{
											testcaseresultStatus = testCaseObj.getString("result");
											testStepPassedCount = testCaseObj.getInt("passedteststep");
											totalTestStepCount	= testCaseObj.getInt("totalteststep");
										}
										String passedOrFailedTestCase = "";
										if(testcaseresultStatus.equalsIgnoreCase("PASSED")){
											passedOrFailedTestCase = "<td style='font-weight: bold'> : <span style='font-family: DejaVu Sans, Arial, Helvetica, sans-serif;color: #00C800;font-size: 12px;font-weight: bold'>"+testcaseresultStatus+"</span></td>";
										} else {
											passedOrFailedTestCase = "<td style='font-weight: bold'> : <span style='font-family: DejaVu Sans, Arial, Helvetica, sans-serif;color: #c80000;font-size: 12px;font-weight: bold'>"+testcaseresultStatus+"</span></td>";				
										}
										//bw.write("<table width='100%' style='table-layout: fixed;'><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px;width:150;'>Test Case description </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;height:17px; font-weight: bold' > : "+testCaseObj.getString("description")+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px;width:150;'>Test Case Code/ID </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;height:17px; font-weight: bold' > : "+testCaseObj.getString("testcasecode")+"/"+testCaseObj.getString("test_case_id")+"</td></tr><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Start Date & Time</th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;font-weight: bold'> : "+testCaseObj.getString("starttime")+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>End Date & Time </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+testCaseObj.getString("endtime")+"</td></tr><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test Case result </th>"+passedOrFailedTestCase.replace("\"", "")+"<th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test Steps Passed </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+testCaseObj.getInt("passedteststep")+"/"+testCaseObj.getInt("totalteststep")+"</td></tr><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Features Covered </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;height:17px; font-weight: bold' > : "+testCaseObj.getString("productFeatureName")+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Risks Covered </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;height:17px; font-weight: bold' > : "+testCaseObj.getString("riskname")+"</td></tr><tr style='height:13px'></tr></table>");
										bw.write("<table width='100%' style='table-layout: fixed;'><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px;width:150;'>Test Case Description </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;height:17px; font-weight: bold' > : "+testCaseObj.getString("description")+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px;width:150;'>Test Case Code/ID </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;height:17px; font-weight: bold' > : "+testCaseObj.getString("testcasecode")+"/"+testCaseObj.getString("test_case_id")+"</td></tr><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Start Date & Time</th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;font-weight: bold'> : "+testCaseObj.getString("starttime")+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>End Date & Time </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+testCaseObj.getString("endtime")+"</td></tr><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test Case result </th>"+passedOrFailedTestCase.replace("\"", "")+"<th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test Steps Passed </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+testStepPassedCount+"/"+totalTestStepCount+"</td></tr><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Features Covered </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;height:17px; font-weight: bold' > : "+featurenames+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Risks Covered </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;height:17px; font-weight: bold' > : "+testCaseObj.getString("riskname")+"</td></tr><tr style='height:13px'></tr></table>");
										//Test Step Details
										String testStepType = "Test Step Code";
										boolean testStepTypeFlag = true;
										for(int j = 0;j < jsonTestStepArray.length(); j++){
											org.json.JSONObject testStepObj = jsonTestStepArray.getJSONObject(j);
											if(testStepObj.getString("teststepcode").equalsIgnoreCase("N/A")&&testStepTypeFlag){
												testStepType = "Teststepid";
												testStepTypeFlag = false;
											}

											if(workPackageMode.equalsIgnoreCase("COMBINED JOB")){
												job_id = testStepObj.getString("combined_job_id");
											}else{
												job_id = testStepObj.getString("job_id");
											}
										}
										//bw.write("<table width='100%' style='table-layout: fixed;'><tr style='color:white;background: #3c6ac6;font-family: Arial;font-size: 12px;height:18px'><th width='8%'>"+testStepType+"</th><th width='10%'>teststepdescription</th><th width='15%'>Expected result</th><th width='15%'>Observed result</th><th width='7%'>Status</th><th width='10%'>Execution Time</th><th width='15%'>Comments</th><th width='20%'>screenshot</th></tr>");
										bw.write("<table width='100%' style='table-layout: fixed;'><tr style='color:white;background: #3c6ac6;font-family: Arial;font-size: 12px;height:18px'><th width='10%' style='padding-left:5px;'>"+testStepType+"</th><th width='40%' style='padding-left:5px;'>Test Step Summary</th><th width='50%' style='padding-left:5px;'>Screenshot</th></tr>");
										for(int j = 0;j<jsonTestStepArray.length();j++){
											org.json.JSONObject testStepObj = jsonTestStepArray.getJSONObject(j);
											String testStepresultStatus = testStepObj.getString("resultstatus");
											String passedOrFailedTestStep = "",failureTestStepComments="";
											String testStepId = testStepObj.getString("teststepcode");
											if(testStepresultStatus.equalsIgnoreCase("PASSED")){
												//passedOrFailedTestStep = "<td style='font-family: DejaVu Sans, Arial, Helvetica, sans-serif;color: #00C800;font-size: 12px;font-weight: bold;' width='5%'>"+testStepresultStatus+"</td>";
												passedOrFailedTestStep = "<span style='font-family: DejaVu Sans, Arial, Helvetica, sans-serif;color: #00C800;font-size: 12px;font-weight: bold;'>"+testStepresultStatus+"</span>";
											} else {
												//passedOrFailedTestStep = "<td style='font-family: DejaVu Sans, Arial, Helvetica, sans-serif;color: #c80000;font-size: 12px;font-weight: bold;' width='5%'>"+testStepresultStatus+"</td>";
												passedOrFailedTestStep = "<span style='font-family: DejaVu Sans, Arial, Helvetica, sans-serif;color: #c80000;font-size: 12px;font-weight: bold;'>"+testStepresultStatus+"</span>";
											}
											if(testStepId.equalsIgnoreCase("N/A")){
												testStepId = testStepObj.getString("teststepid");
											}
											//Configurable property
											if(showTestStepExecutionId != null && showTestStepExecutionId.equalsIgnoreCase("YES")){
												testStepId = testStepId +" ["+ testStepObj.getString("teststepexecutionid") +"]";
											}
											//if(testStepObj.getString("testcasename").equalsIgnoreCase(testcasename) && testStepObj.getString("job_id").equals(String.valueOf(jobId))){
											if(workPackageMode.equalsIgnoreCase("COMBINED JOB")){
												job_id = testStepObj.getString("combined_job_id");
											}else{
												job_id = testStepObj.getString("job_id");
											}
											if(testStepObj.getString("testcasename").equalsIgnoreCase(testcasename) && job_id.equalsIgnoreCase(jsonJobReportObject.getString("job_id"))){
												String testStep = testStepObj.getString("screenshot");

												if(!testStepresultStatus.equalsIgnoreCase("PASSED") && !testStepObj.getString("failurereason").equalsIgnoreCase("N/A")){
													failureTestStepComments=testStepObj.getString("failurereason");
													/*if(failureTestStepComments.length() > 50)
														failureTestStepComments = failureTestStepComments.substring(0, 50);*/
													failureTestStepComments = "<b>Comments : </b><br/><br/>    "+failureTestStepComments+"<br/>";
													log.info("test step comments"+failureTestStepComments);
												}														
								
												// Screenshot validation added for Workpackage 
												if(new File(testStep).exists()){												
													testStepPath.add(testStep);
													log.info(testStep);
													testStep= testStep.substring(testStep.lastIndexOf("\\")+1, testStep.length());
													//closeImagePath = CommonUtility.getCatalinaPath().concat("\\webapps\\iLCM\\css\\images\\close_screenshot_image.jpg");
													 closeImagePath = request.getServletContext().getRealPath("\\close_screenshot_image.jpg");
													 log.info("Close Image Path :"+closeImagePath);
													closeImagePath = closeImagePath.substring(closeImagePath.lastIndexOf("\\")+1, closeImagePath.length());
													//bw.write("<tr><td colspan='8'><hr/></td></tr><tr style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;vertical-align:top'><td width='5%' style='word-wrap: break-word;'>"+testStepId+"</td><td width='10%' style='word-wrap: break-word;'>"+testStepObj.getString("teststepdescription")+"</td><td width='15%' style='word-wrap: break-word;'>"+testStepObj.getString("expectedoutput")+"</td><td width='15%' style='word-wrap: break-word;'>"+testStepObj.getString("observedoutput")+"</td>"+passedOrFailedTestStep.replace("\"", "")+"<td align='middle' width='10%' style='word-wrap: break-word;'>"+testStepObj.getString("totalteststepstime")+"</td><td width='20%' style='word-wrap: break-word;'>"+failureTestStepComments+"</td><td width='20%' style='word-wrap: break-word;'><a href='javascript:void(0);'><img height='100' width='200' src='screenshots/"+testStep+"' onclick=\"display('screenshots/"+testStep+"')\";/></a><div id=\"tag\" class=\"parentdiv\" style=\"display:none;\"><div id=\"childdiv\" class=\"center\"><a href='javascript:void(0);'><img align='right' src='screenshots/"+closeImagePath+"' height=\"20\" width=\"20;\" alt='Close' onclick=\"document.getElementById('tag').style.display='none';\"/></a><img id=\"f\" height=\"100%\" width=\"70%\"/></div></div></td></tr>");
													//bw.write("<tr><td colspan='8'><hr/></td></tr><tr style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;vertical-align:top'><td width='5%' style='word-wrap: break-word;'>"+testStepId+"</td><td width='40%' style='word-wrap: break-word;'><pre style=\"height : auto ; max-height : 400px; overflow : auto; font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;vertical-align:top; white-space: pre-wrap; word-wrap: break-word;\"><b>description:</b> <br/><br/>    "+testStepObj.getString("teststepdescription")+"<br/><br/><br/><b>Executed Environment : </b><br/><br/>    "+testStepObj.getString("testenvironmentname")+"<br/><br/><b>Expected : </b><br/><br/>    "+testStepObj.getString("expectedoutput")+"<br/><br/><b>Observed : </b><br/><br/>    "+testStepObj.getString("observedoutput")+"<br/><br/><b>Status : </b><br/><br/>    "+passedOrFailedTestStep.replace("\"", "")+"<br/><br/><b>Execution Time:</b> <br/><br/>    "+testStepObj.getString("totalteststepstime")+"<br/><br/>"+failureTestStepComments+"<br/></pre></td><td width='50%' style='word-wrap: break-word;'><a href='javascript:void(0);'><img height='400' width='500' src='screenshots/"+testStep+"' onclick=\"display('screenshots/"+testStep+"')\";/></a><div id=\"tag\" class=\"parentdiv\" style=\"display:none;\"><div id=\"childdiv\" class=\"center\"><a href='javascript:void(0);'><img align='right' src='screenshots/"+closeImagePath+"' height=\"20\" width=\"20;\" alt='Close' onclick=\"document.getElementById('tag').style.display='none';\"/></a><img id=\"f\" height=\"100%\" width=\"70%\"/></div></div></td></tr>");
													bw.write("<tr><td colspan='8'><hr/></td></tr><tr style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;vertical-align:top'><td width='5%' style='word-wrap: break-word;'>"+testStepId+"</td><td width='40%' style='word-wrap: break-word;'><pre style=\"height : auto ; max-height : 400px; overflow : auto; font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;vertical-align:top; white-space: pre-wrap; word-wrap: break-word;\"><b>Test Step Name:</b> <br/><br/>    "+testStepObj.getString("teststep")+"<br/><br/><br/><b>Description:</b> <br/><br/>    "+testStepObj.getString("teststepdescription")+"<br/><br/><br/><b>Executed Environment : </b><br/><br/>    "+testStepObj.getString("testenvironmentname")+"<br/><br/><b>Test Step Input : </b><br/><br/>    "+testStepObj.getString("teststepinput")+"<br/><br/><b>Expected : </b><br/><br/>   "+testStepObj.getString("expectedoutput")+"<br/><br/><b>Observed : </b><br/><br/>    "+testStepObj.getString("observedoutput")+"<br/><br/><b>Status : </b><br/><br/>    "+passedOrFailedTestStep.replace("\"", "")+"<br/><br/><b>Execution Time:</b> <br/><br/>    "+testStepObj.getString("totalteststepstime")+"<br/><br/>"+failureTestStepComments+"<br/></pre></td><td width='50%' style='word-wrap: break-word;'><a href='javascript:void(0);'><img height='400' width='500' src='screenshots/"+testStep+"' onclick=\"display('screenshots/"+testStep+"')\";/></a><div id=\"tag\" class=\"parentdiv\" style=\"display:none;\"><div id=\"childdiv\" class=\"center\"><a href='javascript:void(0);'><img align='right' src='screenshots/"+closeImagePath+"' height=\"20\" width=\"20;\" alt='Close' onclick=\"document.getElementById('tag').style.display='none';\"/></a><img id=\"f\" height=\"100%\" width=\"70%\"/></div></div></td></tr>");
												} else {
													log.info(testStep);
													closeImagePath = CommonUtility.getCatalinaPath().concat("\\webapps"+ File.separator + request.getContextPath() + File.separator +"css\\images\\noscreenshot_available.jpg");
													closeImagePath=closeImagePath.substring(closeImagePath.lastIndexOf("\\")+1, closeImagePath.length());
													//bw.write("<tr><td colspan='8'><hr/></td></tr><tr style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;vertical-align:top'><td width='10%' style='word-wrap: break-word;'>"+testStepCode+"</td><td width='40%' style='word-wrap: break-word;'><pre style=\"height : auto ; max-height : 400px; overflow : auto; font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;vertical-align:top; white-space: pre-wrap; word-wrap: break-word;\"><b>description:</b> <br/><br/>    "+testStepObj.getString("teststepdescription")+"<br/><br/><b>Expected : </b><br/><br/>    "+testStepObj.getString("expectedoutput")+"<br/><br/><b>Observed : </b><br/><br/>    "+testStepObj.getString("observedoutput")+"<br/><br/><b>Status : </b><br/><br/>    "+passedOrFailedTestStep.replace("\"", "")+"<br/><br/><b>Execution Time:</b> <br/><br/>    "+testStepObj.getString("totalteststepstime")+"<br/><br/>"+failureTestStepComments+"<br/></pre></td><td width='50%' style='word-wrap: break-word;'><img height='200' width='300' src='screenshots/"+closeImagePath+"')\";/>No screenshot available for the test step</td></tr>");
													//bw.write("<tr><td colspan='8'><hr/></td></tr><tr style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;vertical-align:top'><td width='5%' style='word-wrap: break-word;'>"+testStepId+"</td><td width='40%' style='word-wrap: break-word;'><pre style=\"height : auto ; max-height : 400px; overflow : auto; font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;vertical-align:top; white-space: pre-wrap; word-wrap: break-word;\"><b>description:</b> <br/><br/>    "+testStepObj.getString("teststepdescription")+"<br/><br/><br/><b>Executed Environment : </b><br/><br/>    "+testStepObj.getString("testenvironmentname")+"<br/><br/><b>Expected : </b><br/><br/>    "+testStepObj.getString("expectedoutput")+"<br/><br/><b>Observed : </b><br/><br/>    "+testStepObj.getString("observedoutput")+"<br/><br/><b>Status : </b><br/><br/>    "+passedOrFailedTestStep.replace("\"", "")+"<br/><br/><b>Execution Time:</b> <br/><br/>    "+testStepObj.getString("totalteststepstime")+"<br/><br/>"+failureTestStepComments+"<br/></pre></td><td width='50%' style='word-wrap: break-word;'>No screenshot available for the test step</td></tr>");
													bw.write("<tr><td colspan='8'><hr/></td></tr><tr style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;vertical-align:top'><td width='5%' style='word-wrap: break-word;'>"+testStepId+"</td><td width='40%' style='word-wrap: break-word;'><pre style=\"height : auto ; max-height : 400px; overflow : auto; font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;vertical-align:top; white-space: pre-wrap; word-wrap: break-word;\"><b>Test Step Name:</b> <br/><br/>    "+testStepObj.getString("teststep")+"<br/><br/><br/><b>Description:</b> <br/><br/>    "+testStepObj.getString("teststepdescription")+"<br/><br/><br/><b>Executed Environment : </b><br/><br/>    "+testStepObj.getString("testenvironmentname")+"<br/><br/><b>Test Step Input : </b><br/><br/>    "+testStepObj.getString("teststepinput")+"<br/><br/><b>Expected : </b><br/><br/>    "+testStepObj.getString("expectedoutput")+"<br/><br/><b>Observed : </b><br/><br/>    "+testStepObj.getString("observedoutput")+"<br/><br/><b>Status : </b><br/><br/>    "+passedOrFailedTestStep.replace("\"", "")+"<br/><br/><b>Execution Time:</b> <br/><br/>    "+testStepObj.getString("totalteststepstime")+"<br/><br/>"+failureTestStepComments+"<br/></pre></td><td width='50%' style='word-wrap: break-word;'>No screenshot available for the test step</td></tr>");
												}
											}

										}
										bw.write("</table>");									
									}				
								}	
								//}
							}
							bw.write("<div id='End'><span style='float:right;font-weight:bold;font-family:DejaVu Sans, Arial, Helvetica,sans-serif;font-size:13px'><a style='text-decoration:none;' href='#' onclick='window.history.go(-1);return false'>Go Back</a> | <a style='text-decoration:none;' href='#TOP'>Beginning</a></span></div>");
							bw.write("<br/><br/>");
							bw.write("<div id='sign'><span style='float:left;font-weight:bold;font-family:DejaVu Sans, Arial, Helvetica,sans-serif;font-size:13px'>Username : "+loginUserName+"</span> <span style='float:right;font-weight:bold;font-family:DejaVu Sans, Arial, Helvetica,sans-serif;font-size:13px'>Signature : &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>");
							bw.write("</body></html>");
							bw.close();
							try{
								File screenshotFolder = null;
								File logoFolder  = null;
								FileOutputStream fos = new FileOutputStream(workPackageReportDirectory+productName+"-WP-"+testRunNo+"-Evidence.zip");
								ZipOutputStream zos = new ZipOutputStream(fos);
								List<String> screenshotImages = new ArrayList<String>();

								//String closeScreenshotPath = CommonUtility.getCatalinaPath().concat("\\webapps\\iLCM\\css\\images\\close_screenshot_image.jpg");
								String closeScreenshotPath = request.getServletContext().getRealPath("close_screenshot_image.jpg");
								//closeScreenshotPath = closeImagePath.concat("\\close_screenshot_image.jpg");
								String noScreenshotAvailableImagePath = CommonUtility.getCatalinaPath().concat("\\webapps"+ File.separator + request.getContextPath() + File.separator +"css\\images\\noscreenshot_available.jpg");
								//screenshotImages.add(closeScreenshotPath);
								screenshotImages.add(noScreenshotAvailableImagePath);
								//screenshotImages.addAll(testStepPath);
								List<String> logoImage = new ArrayList<String>();
								logoImage.add(imageServerPath);
								List<String> names = new ArrayList<String>();
								names.add(workpackageHtmlFile);
								for(String name : names){				    			
									if(new File(name).exists()){
										zos.putNextEntry(new ZipEntry(new File(name).getName()));
										FileInputStream in = new FileInputStream(name);					    			
										if(in != null){
											int len;
											while ((len = in.read(buffer)) > 0) {
												zos.write(buffer, 0, len);
											}						    		
											zos.closeEntry();
											in.close();
										}			
									}else{
										log.info(name+" image does not exists");
									}
								}	
								for(String name : screenshotImages){
									log.info(name);
									screenshotFolder  = new File(workPackageReportDirectory+productName+"-WP-"+testRunNo+File.separator+"screenshots");
									if(!screenshotFolder.exists()){
										screenshotFolder.mkdirs();				    				
									}
									if(name != null && name.endsWith(".jpg")){
										File screenshotFile = new File(name);
										if(screenshotFile.exists()){
											FileUtils.copyFile(screenshotFile, new File(screenshotFolder+File.separator+screenshotFile.getName()));
										}
									}
									else{
										log.info("Image is not available :"+name );
									}
								}	
								for(String name : logoImage){	
									log.info("logo path is :"+name);
									logoFolder  = new File(workPackageReportDirectory+productName+"-WP-"+testRunNo+File.separator+"logo");
									if(!logoFolder.exists()){
										logoFolder.mkdirs();				    				
									}
									File logoFile = new File(name);
									if(logoFile.exists()){
										FileUtils.copyFile(logoFile, new File(logoFolder+File.separator+logoFile.getName()));
									}
								}	
								zipDir(zos,"",screenshotFolder);
								zipDir(zos,"",logoFolder);				    		
								zos.close();				    		
							}catch(IOException ex){
								log.error("error while iterating data "+ex);
								if(testRunJobId!=-1){				
									jTableResponse = new JTableResponse("ERROR","No records available for the current Job "+testRunJobId);
								} else if(testRunNo != -1){
									jTableResponse = new JTableResponse("ERROR","No records available for the current Workpackage "+testRunNo);
								}
								return jTableResponse;
							}
							log.info("workpackageHtmlFile is"+workpackageHtmlFile);
							workpackageHtmlFile = workPackageReportDirectory+productName+"-WP-"+testRunNo+"-Evidence.zip";
							ZipTool.unzip(workpackageHtmlFile, workPackageReportDirectory+productName+"-WP-"+testRunNo+"-Evidence");
							fileToOpenInBrowser = workPackageReportDirectory +productName+"-WP-"+testRunNo+"-Evidence"+File.separator +productName+"-WP-"+testRunNo+"-Evidence.html"; 
							fileToDownload = workPackageReportDirectory +productName+"-WP-"+testRunNo+"-Evidence.zip";

						}catch (IOException | JSONException e) {		
							log.error("error while iterating data "+e);
							e.printStackTrace();
							if(testRunJobId!=-1){				
								jTableResponse = new JTableResponse("ERROR","No records available for the current Job "+testRunJobId);
							} else if(testRunNo != -1){
								jTableResponse = new JTableResponse("ERROR","No records available for the current Workpackage "+testRunNo);
							}
							return jTableResponse;
						} 					
					} else {
						log.info("no data ");
						if(testRunJobId!=-1){				
							jTableResponse = new JTableResponse("ERROR","No records available for the current Job "+testRunJobId);
						} else if(testRunNo != -1){
							jTableResponse = new JTableResponse("ERROR","No records available for the current Workpackage "+testRunNo);
						}
						return jTableResponse;
					}			
				}
			}	
		}
		if(!new File(workpackageHtmlFile).exists()&&!new File(JobLevelHTMLFIle).exists()) {
			log.info("no data ");
			if(testRunJobId != -1){				
				jTableResponse = new JTableResponse("ERROR","No records available for the current Job "+testRunJobId);
			} else if(testRunNo != -1){
				jTableResponse = new JTableResponse("ERROR","No records available for the current Workpackage "+testRunNo);
			}
		}
		else {				
			if(productTypeName.equalsIgnoreCase("WORKPACKAGE") && reportType.equalsIgnoreCase("VIEWHTML")){
				fileToOpenInBrowser = "http://" + request.getServerName() + ":" + request.getServerPort() +request.getContextPath() + File.separator + "WPReport" +File.separator +productName+"-WP-"+testRunNo+"-Evidence"+File.separator +productName+"-WP-"+testRunNo+"-Evidence.html";
				jTableResponse = new JTableResponse("Ok","Export testCases Completed.",fileToOpenInBrowser);
			} else if(productTypeName.equalsIgnoreCase("JOB") && reportType.equalsIgnoreCase("VIEWHTML")){
				fileToOpenInBrowser = "http://" + request.getServerName() + ":" + request.getServerPort() +request.getContextPath() + File.separator + "JobReport" +File.separator +productName+"-JR-"+testRunJobId+"-Evidence"+File.separator +productName+"-JR-"+testRunJobId+"-Evidence.html";
				jTableResponse = new JTableResponse("Ok","Export testCases Completed.",fileToOpenInBrowser);
			}	
		}		
		return jTableResponse;
	}

	private static String buildPath(String path, String file)
	{
		if (path == null || path.isEmpty()) {
			return file;
		} else {
			return path + File.separator + file;
		}
	}

	private static void zipDir(ZipOutputStream zos, String path, File dir) throws IOException
	{
		File[] files = dir.listFiles();
		path = buildPath(path, dir.getName());
		log.info("Adding Directory " + path);

		for (File source : files) {
			if (source.isDirectory()) {
				zipDir(zos, path, source);
			} else {
				zipFile(zos, path, source);
			}
		}
		log.info("Leaving Directory " + path);
	}

	private static void zipFile(ZipOutputStream zos, String path, File file) throws IOException
	{
		log.info("Compressing " + file.getName());
		log.info(buildPath(path, file.getName()));

		zos.putNextEntry(new ZipEntry(buildPath(path, file.getName())));

		FileInputStream fis = new FileInputStream(file);

		byte[] buffer = new byte[4092];
		int byteCount = 0;
		while ((byteCount = fis.read(buffer)) != -1)
		{
			zos.write(buffer, 0, byteCount);
		}

		fis.close();
		zos.closeEntry();
	}

	@RequestMapping(value="report.run.xml",method=RequestMethod.POST ,produces="application/xml")
	public @ResponseBody JTableResponse externalXMLReport(@RequestParam int testRunNo,HttpServletRequest request, HttpServletResponse response) throws Exception {

		JTableResponse jTableResponse = null ;

		String downloadFolder = htmlReportGenLocation + File.separator + "EXTERNAL_REPORTS"+File.separator + testRunNo+ File.separator;
		FileFilter filter = new WildcardFileFilter("*.zip");
		File[] files = new File(downloadFolder).listFiles(filter);
		File file = new File(files[0].getAbsolutePath());		  
		// Check if file exists
		if (file.exists()) {
			// set content type 		 
			jTableResponse = new JTableResponse("Ok","Export Completed.",file.getAbsolutePath());

		} else {
			jTableResponse = new JTableResponse("ERROR","Export Completed.",file.getAbsolutePath());
		}
		return jTableResponse;    
	}

	@RequestMapping(value="report.run.device.evidence.multiple.html.download" ,method=RequestMethod.POST , produces="application/html")
	public  @ResponseBody JTableResponse multipleWorkpackageReportHtml(@RequestParam String workPackageIds,@RequestParam String testRunConfigurationChildId,@RequestParam String type, ModelAndView modelAndView, HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.info("Report Inputs : work package no " + workPackageIds + " child id : " + testRunConfigurationChildId);
		JTableResponse jTableResponse = null ;
		TestRunJob testRunJob= null;
		ProductType productType = null;
		String exportLocation = null;
		String serverFolderPath = null;
		String testSuiteName = "";
		String workpackageName="";
		BufferedImage logo = null;
		String loginUserName = "";
		String workpackageHtmlFile = "";
		String JobLevelHTMLFIle = "";
		String imageServerPath = null;
		String productName = "";
		String testRunPlanName = "";
		String customerName = "";
		String closeImagePath = "";
		String productTypeName = "";
		Integer testRunNo = 0;
		String fileToOpenInBrowser = "";
		String fileToDownload = "";
		String generatedFileName = "";
		String abortedWorkPackages = "";
		List<WorkPackage> workpackages = new LinkedList<WorkPackage>();
		if (System.getProperty("os.name").contains("Linux")) {
			serverFolderPath = CommonUtility.getCatalinaPath()+ IDPAConstants.JASPERREPORTS_PATH_LINUX;
		} else if (System.getProperty("os.name").contains("Windows")) {
			serverFolderPath = CommonUtility.getCatalinaPath()+ IDPAConstants.JASPERREPORTS_PATH;
		} else {
			serverFolderPath = CommonUtility.getCatalinaPath()+ IDPAConstants.JASPERREPORTS_PATH;	
		}	
		imageServerPath = CommonUtility.getCatalinaPath() + File.separator + "webapps" + File.separator + "Logo"+ File.separator;
		Set<Integer> workPackageIdsSet = new HashSet<Integer>();
		if(workPackageIds != null && !workPackageIds.isEmpty()){
			String [] workpackageIdsArray = workPackageIds.split(",");
			Arrays.sort(workpackageIdsArray);
			for(String workPackageId : workpackageIdsArray){
				workPackageIdsSet.add(Integer.valueOf(workPackageId));
			}
		}		
		if(workPackageIdsSet != null && workPackageIdsSet.size() > 0){
			for(Integer wpId : workPackageIdsSet) {			
				WorkPackage wp=workPackageService.getWorkPackageById(wpId);
				if(wp != null){
					workpackages.add(wp);
					if(wp.getProductBuild().getProductVersion().getProductMaster() != null && wp.getProductBuild().getProductVersion().getProductMaster().getProductName() != null)
						productName = wp.getProductBuild().getProductVersion().getProductMaster().getProductName();
				}
			}
		}
		generatedFileName = workPackageIdsSet.toString().replace("[", "").replace("]", "");
		workpackageHtmlFile = CommonUtility.getCatalinaPath()+ File.separator + "webapps" + File.separator + request.getContextPath() + File.separator+"CWPReport"+File.separator+ "Consolidated_WorkPackage_Report_"+generatedFileName +".html" ;
		File htmFile = new File(workpackageHtmlFile);
		if(!htmFile.exists()){
			new File(htmFile.getParent()).mkdirs();
		}
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(workpackageHtmlFile)));	 
		bw.write("<html><head><META http-equiv=Content-Type content='text/html; charset=utf-8'><style>.center {margin: auto;width: 75%;border: 3px solid #473572;padding: 10px;position : fixed;background:white;height:60%;}.parentdiv {margin: auto; width: 80%;height:50%;border: 3px;padding: 10px;position : fixed;top:20%;left:8%;right:auto;}body {background-color: #FFFFFF;}th{text-align: left;color:white;background: #3c6ac6;}a {color:blue;} a:visited {color:blue;}a:hover {color:blue;}a:active {color:blue;}</style><link rel='icon' type='image/gif/png' href='logo/taf_logo.jpg' style='width:5px;height:5px;'><title>Consolidated Workpackage Evidence Report</title><script>function display(e){document.getElementById(\"tag\").style.display='';var img = document.getElementById(\"f\");img.setAttribute(\"src\",e);document.getElementById(\"childdiv\").appendChild(img);return false;}</script></head><body>");
		int count = 1;
		List<String>testStepPath = new ArrayList<String>();
		Map<String,String> hostNameMap = new HashMap<String,String>();
		byte[] buffer = new byte[1024];
		List<String> htmlReportDataForWorkPackages = new ArrayList<String>();
		List<String> workPackageDataList = new ArrayList<String>();
		List<String> jobsDataList = new ArrayList<String>();
		Set<String> abortedWorkPackageDataSet = new HashSet<String>();
		StringBuffer sb = new StringBuffer();
		String jsonReportSummary = " ",jsonJobListSummaryForEnvironment = "";
		for(WorkPackage workPackage : workpackages){	
			testRunNo = workPackage.getWorkPackageId();			
			loginUserName = workPackage.getUserList().getLoginId();	
			workpackageName = workPackage.getName();
			productType = workPackage.getProductBuild().getProductVersion().getProductMaster().getProductType();
			productName = workPackage.getProductBuild().getProductVersion().getProductMaster().getProductName();
			customerName = workPackage.getProductBuild().getProductVersion().getProductMaster().getCustomer().getCustomerName().trim();
			htmlReportDataForWorkPackages = report.generateTestRunListHtmlReport(testRunNo, workPackage.getTestRunPlan().getTestRunPlanId(),"HTML",productType,logo, loginUserName);
			if(!htmlReportDataForWorkPackages.isEmpty() && htmlReportDataForWorkPackages.toString() != null  && htmlReportDataForWorkPackages != null ){
				for(int i=0 ;i < htmlReportDataForWorkPackages.size(); i++){
					jsonReportSummary = htmlReportDataForWorkPackages.get(0);
					jsonJobListSummaryForEnvironment = htmlReportDataForWorkPackages.get(1);

				}
				workPackageDataList.add(jsonReportSummary);
				jobsDataList.add(jsonJobListSummaryForEnvironment);
				if(!jsonReportSummary.isEmpty() && jsonReportSummary != null && !jsonReportSummary.toString().isEmpty() && !jsonReportSummary.equalsIgnoreCase("[]") ){

				}else{
					abortedWorkPackageDataSet.add(String.valueOf(testRunNo));
				}
			}
		}
		abortedWorkPackages = abortedWorkPackageDataSet.toString().replace("[", "").replace("]", "");
		for(WorkPackage wp : workpackages){			
			testRunNo = wp.getWorkPackageId();			
			loginUserName = wp.getUserList().getLoginId();	
			workpackageName = wp.getName();
			productType = wp.getProductBuild().getProductVersion().getProductMaster().getProductType();
			productName = wp.getProductBuild().getProductVersion().getProductMaster().getProductName();
			customerName = wp.getProductBuild().getProductVersion().getProductMaster().getCustomer().getCustomerName().trim();
			//testRunPlanName = wp.getTestRunPlan().getTestRunPlanName();
			log.info("Report Inputs : " + wp.getWorkPackageId() + " : " + testRunConfigurationChildId);
			log.info("logo path is :"+logo);
			JasperPrint jasperPrint = null;
			String strContentType = "application/html";
			String strPrintMode = "HTML";
			response.reset();
			response.resetBuffer();
			String jsonSourceFile = null;
			String jsonSourceSubFile1 = null;
			String jsonSourceSubFile2 = null;
			String jsonSourceSubFile3 = null;
			String jsonSourceSubFile4= null;
			List<String>htmlReportData = new ArrayList<String>();
			log.info("Going to TestRunListHtmlReport : " + testRunNo + " : " + testRunConfigurationChildId+"  :  "+productTypeName);
			htmlReportData = report.generateTestRunListHtmlReport(testRunNo, wp.getTestRunPlan().getTestRunPlanId(),strPrintMode,productType,logo, loginUserName);
			log.info("after getting the html report data");
			if(!htmlReportData.isEmpty()){
				for(int i=0 ;i < htmlReportData.size(); i++){
					jsonSourceFile = htmlReportData.get(0);
					jsonSourceSubFile1 = htmlReportData.get(1);
					jsonSourceSubFile2 = htmlReportData.get(2);
					jsonSourceSubFile3 = htmlReportData.get(3);
					jsonSourceSubFile4 = htmlReportData.get(4);
				}
				if(new org.json.JSONArray(htmlReportData.get(1)).length() > 0){
					org.json.JSONArray jsonReportSummaryArray = new org.json.JSONArray(jsonSourceFile);
					org.json.JSONArray jsonJobReportArray = new org.json.JSONArray(jsonSourceSubFile1);
					org.json.JSONArray jsonJobSummaryArray = new org.json.JSONArray(jsonSourceSubFile2);
					org.json.JSONArray jsonTestCaseSummaryArray = new org.json.JSONArray(jsonSourceSubFile3);
					org.json.JSONArray jsonTestStepArray = new org.json.JSONArray(jsonSourceSubFile4);
					log.info("jsonReportSummaryArray values are :"+jsonReportSummaryArray);
					log.info("jsonJobReportArray values are :"+jsonJobReportArray);
					log.info("jsonJobSummaryArray values are :"+jsonJobSummaryArray);
					log.info("jsonTestCaseSummaryArray values are :"+jsonTestCaseSummaryArray);
					log.info("jsonTestStepArray values are :"+jsonTestStepArray);	
					try{
						log.debug("workpackageName is"+ workpackageName);						
						Map <String,String> jobResultWithJobId = new HashMap<String, String>();
						for(int i = 0;i < jsonJobSummaryArray.length(); i++){
							org.json.JSONObject jobSummaryObject = jsonJobSummaryArray.getJSONObject(i);
							jobResultWithJobId.put(jobSummaryObject.getString("testrunlistid"), jobSummaryObject.getString("testcaseresult"));
						}
						log.info("jobResultWithJobId" + jobResultWithJobId);
						StringBuilder testEnvironment = new StringBuilder();
						StringBuilder multipleHostIds = new StringBuilder();
						StringBuilder testEnvironmentForWorkPackageSummary ;
						String testEnvironmentName = "" ,testEnvironmentNameForWorkPackageSummary = "" , hostName = "" ;
						String testRunNoForWorkPackageSummary = "";
						String deviceMakeandModel = "";
						boolean WPHasDeviceMakeAndModel = false;
						StringBuilder deviceMakeAndModelBuilder = new StringBuilder();
						String testRunNoForHostids = "",deviceMake = "" , deviceModel = "";
						Map<String,String> testEnvironmentMap = new HashMap<String,String>();
						try {
							for(int i=0;i<jsonJobReportArray.length();i++){
								org.json.JSONObject jsonObject = jsonJobReportArray.getJSONObject(i);
								if(jsonObject.getString("job_name") != null && !jsonObject.getString("job_name").isEmpty()){
									testEnvironmentName = jsonObject.getString("job_name");
									testEnvironment.append(testEnvironmentName+";"); 
								}
								if(!jsonObject.getString("devicemake").equalsIgnoreCase("N/A") && !jsonObject.getString("devicemodel").equalsIgnoreCase("N/A")){
									deviceMake = jsonObject.getString("devicemake");
									deviceModel = jsonObject.getString("devicemodel");
									deviceMakeAndModelBuilder.append(deviceMake+"&"+deviceModel+";"); 
									WPHasDeviceMakeAndModel = true;
								}
								else{
									deviceMakeAndModelBuilder.append("N/A");
								}

							}			
							testEnvironmentName = "";
							testEnvironmentName = testEnvironment.substring(0, (testEnvironment.lastIndexOf(";")));
							if(WPHasDeviceMakeAndModel)
								deviceMakeandModel = deviceMakeAndModelBuilder.substring(0, (deviceMakeAndModelBuilder.lastIndexOf(";")));
							else
								deviceMakeandModel = "N/A & N/A";
							log.info("testEnvironment values are :"+testEnvironmentName);
							org.json.JSONObject jsonObject = jsonReportSummaryArray.getJSONObject(0);
							if(count > 1){
								bw.write("</hr>");
								bw.write("<div> </div>");
							}							
							if(count < 2){
								bw.write("<table width='100%' style='margin-bottom:20px;'><tr style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #FFFFFF; font-size: 24px; height:62px;background:#003ca4; font-weight: bold;'><td align='middle' id='TOP'>"+"Consolidated Work Package Report </td></tr></table>");
								//workpackage details
								Set<String> workPackageIdSet = new HashSet<String>();
								Set<String> hostNameSet = new HashSet<String>();
								bw.write("<table width='100%' style='table-layout: fixed;'><tr style='color:#ffffff;height:18px;font-family: Arial;background: #3c6ac6;font-size: 12px; font-weight: bold'><th width='15%'>Workpackage No</th><th width='15%'>Product Name</th><th width='15%'>Status</th><th width='15%'>Start Time</th><th width='15%'>End Time</th><th width='15%'>Test Environment</th><th width='15%'>Host</th></tr>");
								for(String job : jobsDataList){
									org.json.JSONArray jsonjobArray = new org.json.JSONArray(job);
									testEnvironmentForWorkPackageSummary = new StringBuilder();

									multipleHostIds = new StringBuilder();
									for(int i = 0 ; i < jsonjobArray.length(); i++){
										org.json.JSONObject jsonJobObject = jsonjobArray.getJSONObject(i);
										if(jsonJobObject.getString("job_name") != null && !jsonJobObject.getString("job_name").isEmpty()){
											testEnvironmentNameForWorkPackageSummary = jsonJobObject.getString("job_name");
											testEnvironmentForWorkPackageSummary.append(testEnvironmentNameForWorkPackageSummary+"; "); 
										}
										testRunNoForWorkPackageSummary = jsonJobObject.getString("testrunno");
										if(jsonJobObject.getString("hostname") != null && !jsonJobObject.getString("hostname").isEmpty()){
											if(hostNameSet.add(jsonJobObject.getString("hostname"))){
												hostName = jsonJobObject.getString("hostname");
												multipleHostIds.append(hostName+"; "); 
											}
										}
									}
									testEnvironmentNameForWorkPackageSummary = "" ;
									hostName = "";
									testEnvironmentNameForWorkPackageSummary  = testEnvironmentForWorkPackageSummary.substring(0, (testEnvironmentForWorkPackageSummary.lastIndexOf("; ")));
									hostName = multipleHostIds.substring(0, (multipleHostIds.lastIndexOf("; ")));
									testEnvironmentMap.put(testRunNoForWorkPackageSummary, testEnvironmentNameForWorkPackageSummary);
									hostNameMap.put(testRunNoForWorkPackageSummary, hostName);
									hostNameSet.clear();
								}
								for(String workPackage : workPackageDataList){
									org.json.JSONArray jsonWorkpackageSummaryArray = new org.json.JSONArray(workPackage);
									for(int i = 0 ; i < jsonWorkpackageSummaryArray.length(); i++){
										org.json.JSONObject jsonWPObject = jsonWorkpackageSummaryArray.getJSONObject(i);
										String result = jsonWPObject.getString("testcaseresult");
										String passedOrFailed = "";
										if(result.equalsIgnoreCase("PASSED")){
											passedOrFailed = "<td style='font-weight: bold'>  <span style='font-family: DejaVu Sans, Arial, Helvetica, sans-serif;color: #00C800;font-size: 12px;font-weight: bold'> "+result+"</span></td>";
										} else {
											passedOrFailed = "<td style='font-weight: bold'>  <span style='font-family: DejaVu Sans, Arial, Helvetica, sans-serif;color: #c80000;font-size: 12px;font-weight: bold'> "+result+"</span></td>";
										}
										if( workPackageIdSet.add(jsonWPObject.getString("testrunno"))){
											bw.write("<tr><td colspan='9'><hr/></td></tr><tr style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 11px;vertical-align:top'><td><a style='text-decoration: none;font-weight:bold' href=#"+jsonWPObject.getString("testrunno")+" - "+jsonWPObject.getString("productname")+">"+jsonWPObject.getString("testrunno")+"</a></td><td>"+jsonWPObject.getString("productname")+"</td>"+passedOrFailed+"<td>"+jsonWPObject.getString("testrunstarttime")+"</td><td>"+jsonWPObject.getString("testrunendtime")+"</td><td style='word-wrap: break-word;'>"+testEnvironmentMap.get(jsonWPObject.getString("testrunno"))+"</td><td>"+hostNameMap.get(jsonWPObject.getString("testrunno"))+"</td></tr>");
										}
									}
								}
							}
							//bw.write("<table width='100%'><tr style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #FFFFFF; font-size: 24px; height:62px;background:#003ca4; font-weight: bold;'><td><img style=\"width:50px;height:50px;\"/></td> <td align='middle' id='TOP'>"+wp.getWorkPackageId() +" - "+jsonObject.getString("productName")+" - "+jsonObject.getString("testRunconfigurationName")+"</td></tr><tr style='height:13px'></tr></table>");
							bw.write("<table width='100%'><tr style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #FFFFFF; font-size: 24px; height:62px;background:#003ca4; font-weight: bold;'><td align='middle' id="+wp.getWorkPackageId() +" - "+jsonObject.getString("productname")+">"+"Work Package - "+wp.getWorkPackageId() +" - "+jsonObject.getString("productname")+" - "+jsonObject.getString("testrunconfigurationname")+"</td></tr><tr style='height:13px'></tr></table>");
							bw.write("<table bgcolor='white' width='100%' style='table-layout: fixed;'>");
							//report summary
							String result = jsonObject.getString("testcaseresult");
							String passedOrFailed = "";
							if(result.equalsIgnoreCase("PASSED")){
								passedOrFailed = "<td style='font-weight: bold'> : <span style='font-family: DejaVu Sans, Arial, Helvetica, sans-serif;color: #00C800;font-size: 12px;font-weight: bold'> "+result+"</span></td>";
							} else {
								passedOrFailed = "<td style='font-weight: bold'> : <span style='font-family: DejaVu Sans, Arial, Helvetica, sans-serif;color: #c80000;font-size: 12px;font-weight: bold'> "+result+"</span></td>";
							}
							bw.write("<tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px;width:150;'>Product Name </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold' > : "+jsonObject.getString("productname")+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px;width:150;'>Work Package No </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jsonObject.getString("testrunno")+"</td></tr><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Product version	</th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jsonObject.getString("productversionname")+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px;width:150;'>Device Make & Model </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+deviceMakeandModel+"</td></tr><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test Run Plan	</th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jsonObject.getString("testrunconfigurationname")+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Triggered Time </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jsonObject.getString("testruntriggeredtime")+"</td></tr><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test Suite	</th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jsonObject.getString("testsuiteid")+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test Cases Passed </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jsonObject.getInt("passedtest")+"/"+jsonObject.getInt("totaltest")+"</td></tr><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test Start Date & Time </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jsonObject.getString("testrunstarttime")+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test Steps Passed </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jsonObject.getInt("passedtestecase")+"/"+jsonObject.getInt("totaltestcase")+"</td></tr><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px;width:150;'>Test Environment    </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+testEnvironmentName+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test End Date & Time </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jsonObject.getString("testrunendtime")+"</td></tr><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Host </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+hostNameMap.get(jsonObject.getString("testrunno"))+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test Result	</th>"+passedOrFailed.replace("\"", "")+"</tr>");

						} catch (JSONException e) {
							log.info("Error while iterating source file json data"+e);
						}
						bw.write("</table>");
						//Job Report Details					
						bw.write("<table width='100%' style='table-layout: fixed;'><tr></tr><tr align='left' style='font-family:DejaVu Sans, Arial, Helvetica,sans-serif;color:#ffffff;background:#003ca4;font-weight: bold;height:25px'><td id='JOBREPORT'>JOBS<span style='float:right'><a style='text-decoration:none;font-weight:bold;color:#ffffff;font-size:13px;' href='#End'>End</a></span></td></tr></table>");		
						bw.write("<table width='100%' style='table-layout: fixed;'>");
						bw.write("<tr style='color:#ffffff;height:18px;font-family: Arial;background: #3c6ac6;font-size: 12px; font-weight: bold'><th>Job ID</th><th>Environment</th><th>Device ID</th><th>Device Platform</th><th>Status</th><th>Result</th><th>Start Date & Time </th><th>End Date & Time</th><th>Comments</th></tr>");
						String job_id="";
						for(int i = 0;i<jsonJobReportArray.length();i++){			
							org.json.JSONObject jsonJobReportObject = jsonJobReportArray.getJSONObject(i);
							String jobResult = jsonJobReportObject.getString("testresultstatus");
							String jobStatus = jsonJobReportObject.getString("result");
							String passedOrFailedJobResult = "",passedOrFailedJobStatus="";
							if(jobStatus.equalsIgnoreCase("5")){
								passedOrFailedJobStatus = "<td>Completed</td>";
							}
							else if(jobStatus.equalsIgnoreCase("7")){
								passedOrFailedJobStatus = "<td>Aborted</td>";
							}
							for(Map.Entry<String, String> entry : jobResultWithJobId.entrySet()){
								if(entry.getKey().equalsIgnoreCase(jsonJobReportObject.getString("job_id"))){
									passedOrFailedJobResult=entry.getValue();
									if(passedOrFailedJobResult.equalsIgnoreCase("PASSED")){
										passedOrFailedJobResult = "<td style='color:#00C800;font-family: DejaVu Sans, Arial, Helvetica, sans-serif;font-size: 12px;'><b>"+passedOrFailedJobResult+"</b></td>";
									} else {
										passedOrFailedJobResult = "<td style='color:#c80000;font-family: DejaVu Sans, Arial, Helvetica, sans-serif;font-size: 12px;'><b>"+passedOrFailedJobResult+"</b></td>";				
									}	 
									bw.write("<tr><td colspan='9'><hr/></td></tr><tr style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;vertical-align:top'><td><a style='text-decoration: none;font-weight:bold' href=#"+jsonJobReportObject.getString("job_id")+">"+jsonJobReportObject.getString("job_id")+"</a></td><td>"+jsonJobReportObject.getString("job_name")+"</td><td>"+jsonJobReportObject.getString("device_id")+"</td><td align='middle'>"+jsonJobReportObject.getString("device_platform")+"</td>"+passedOrFailedJobStatus.replace("\"", "")+passedOrFailedJobResult.replace("\"", "")+"<td>"+jsonJobReportObject.getString("start_time")+"</td><td>"+jsonJobReportObject.getString("end_time")+"</td><td>"+jsonJobReportObject.getString("comments")+"</td></tr>");	
								}
							}
						}						
						bw.write("</table>");
						boolean flagForJobSummaryIteration = true;
						for(int l=0;l<jsonJobReportArray.length();l++){	
							org.json.JSONObject jsonJobReportObject = jsonJobReportArray.getJSONObject(l);
							/*//Copy the evidence folder of the job id to the workpackage folder									
							String evidencePathFolder = evidence_Folder;
							File testJobEvidenceFolderPath = new File(evidencePathFolder + File.separator + jsonJobReportObject.getString("job_id") + "SCREENSHOT");
							File wpFolder  = new File(workPackageReportDirectory + productName+"-WP-"+testRunNo+File.separator+"screenshots");
							if(!wpFolder.exists()){
								wpFolder.mkdirs();
							}
							FileUtils.copyDirectory(testJobEvidenceFolderPath, wpFolder);*/
							for(int m = 0; m < jsonJobSummaryArray.length();m++){
								org.json.JSONObject jobSummaryObject = jsonJobSummaryArray.getJSONObject(m);
								if(jsonJobReportObject.getString("job_id").equals(jobSummaryObject.getString("testrunlistid")) && flagForJobSummaryIteration){
									log.info("job id : "+jobSummaryObject.getString("testrunlistid"));
									//Job Summary details
									bw.write("<table width='100%' style='table-layout: fixed;'><tr style='height:10px'></tr><tr align='left' style='font-family:DejaVu Sans, Arial, Helvetica,sans-serif;color:#ffffff;background:#003ca4;font-weight: bold;height:27px'><td id=JOBSUMMARY"+jobSummaryObject.getString("testrunlistid")+">JOB SUMMARY<span style='float:right;'><a style='text-decoration:none;font-weight:bold;font-size:13px;color:#ffffff;' href=#JOBREPORT>Job Report | </a><a style='text-decoration:none;font-weight:bold;font-size:13px;color:#ffffff;' href=#TOP>Beginning | </a><a style='text-decoration:none;font-weight:bold;font-size:13px;color:#ffffff;' href=#End>End</a></span></td></tr>");
									bw.write("<table width='100%' style='table-layout: fixed;'><tr style='font-family: DejaVu Sans, Arial, Helvetica, sans-serif;height:25px;background: #3c6ac6; color: #FFFFFF; font-size: 12px;'><td id="+jobSummaryObject.getString("testrunlistid")+">Job No : "+jobSummaryObject.getString("testrunlistid")+"</td><td>Environment : "+jsonJobReportObject.getString("job_name")+"</td><td>Device ID : "+jsonJobReportObject.getString("device_id")+"</td><td>Device Platform : "+jsonJobReportObject.getString("device_platform")+"</td><td>Host Platform : "+jsonJobReportObject.getString("hostipaddress")+"</td></tr></table>");
									bw.write("<table width='100%' style='table-layout: fixed;'>");
									String jobresultStatus = jobSummaryObject.getString("testcaseresult");
									String passedOrFailedJobStatus = "";
									String device="",devicePlatForm="";
									if(jobresultStatus.equalsIgnoreCase("PASSED")){
										passedOrFailedJobStatus = "<td style='font-weight: bold'> : <span style='font-family: DejaVu Sans, Arial, Helvetica, sans-serif;color: #00C800;font-size: 12px;font-weight: bold'> "+jobresultStatus+"</span></td>";
									} else {
										passedOrFailedJobStatus = "<td style='font-weight: bold'> : <span style='font-family: DejaVu Sans, Arial, Helvetica, sans-serif;color: #c80000;font-size: 12px;font-weight: bold'> "+jobresultStatus+"</span></td>";						
									}
									if((jobSummaryObject.getString("deviceid").equalsIgnoreCase("N/A")&&jobSummaryObject.getString("devicemodel").equalsIgnoreCase("N/A"))&&jobSummaryObject.getString("deviceplatformname").equalsIgnoreCase("N/A")&&jobSummaryObject.getString("deviceplatformversion").equalsIgnoreCase("N/A")){
										device="<td style='font-weight: bold'> : <span style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; font-weight: bold'>N/A</td>";
										devicePlatForm="<td style='font-weight: bold'> : <span style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; font-weight: bold'>N/A</td>";
									}
									else{
										device="<td style='font-weight: bold'> : <span style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; font-weight: bold'>"+jobSummaryObject.getString("devicemodel")+","+jobSummaryObject.getString("deviceid")+"</span></td>";
										devicePlatForm="<td style='font-weight: bold'> : <span style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; font-weight: bold'>"+jobSummaryObject.getString("deviceplatformname")+" "+jobSummaryObject.getString("deviceplatformversion")+"</span></td>";
									}
									bw.write("<tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px;width:150;'>Product Name </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;height:17px; font-weight: bold' > : "+jobSummaryObject.getString("productname")+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px;width:150;'>Device </th>"+device+"</tr><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Product version	</th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;font-weight: bold'> : "+jobSummaryObject.getString("productversionname")+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Device Platform </th>"+devicePlatForm+"</tr><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test Run Plan	</th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jobSummaryObject.getString("testrunconfigurationname")+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test Cases Passed </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jobSummaryObject.getInt("passedtest")+"/"+jobSummaryObject.getInt("totaltest")+"</td></tr><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test Suite	</th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jobSummaryObject.getString("testsuitename")+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test Steps Passed </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jobSummaryObject.getInt("passedtestecase")+"/"+jobSummaryObject.getInt("totaltestcase")+"</td></tr><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test Start Date & Time </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jobSummaryObject.getString("testrunstarttime")+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test End Date & Time </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jobSummaryObject.getString("testrunendtime")+"</td></tr><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test Tool </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+jobSummaryObject.getString("testtoolname")+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test result	</th>"+passedOrFailedJobStatus.replace("\"", "")+"</tr>");
									bw.write("</table>");
									flagForJobSummaryIteration = false;
								}
							}
							//Test case summary details	
							flagForJobSummaryIteration = true;
							String totalTCExecutionTime="";
							String testCaseId="";								
							String testCaseType="Test Case Code";
							Set<Integer> listOfTestCaseIds = new HashSet<Integer>(); 
							boolean testCaseTypeFlag=true;
							for(int j=0;j<jsonTestCaseSummaryArray.length();j++){			
								org.json.JSONObject testCaseObj =jsonTestCaseSummaryArray.getJSONObject(j);
								if(testCaseObj.getString("job_id").equals(jsonJobReportObject.getString("job_id"))){
									totalTCExecutionTime = testCaseObj.getString("totaltcexectime");
								}
								if(testCaseObj.getString("testcasecode").equalsIgnoreCase("N/A")&&testCaseTypeFlag){
									testCaseType="Test Case Id";
									testCaseTypeFlag=false;
								}
								listOfTestCaseIds.add(Integer.valueOf(testCaseObj.getString("test_case_id")));
							}
							//Multimap<Integer, String> multimap = ArrayListMultimap.create();
							ListMultimap<Integer, Object> multimap = ArrayListMultimap.create();
							multimap = report.getFeatureNamesByTestCaseIdList(listOfTestCaseIds);
							bw.write("<table width='100%'><tr style='height:13px'></tr><tr style='font-family:DejaVu Sans, Arial, Helvetica,sans-serif;color:#ffffff;background:#003ca4;font-weight: bold;height:26px;font-size: 12px;'><td id=TESTCASESUMMARY"+jsonJobReportObject.getString("job_id")+">TEST CASE SUMMARY</td><td><span style='float:left'>Total Execution Time : "+totalTCExecutionTime+"</span><span style='float:right'><a style='text-decoration:none;font-weight:bold;color:#ffffff;font-size:13px;' href=#JOBSUMMARY"+jsonJobReportObject.getString("job_id")+">"+"Job Summary | "+"</a><a style='text-decoration:none;font-weight:bold;color:#ffffff;font-size:13px;' href=#JOBREPORT>Job Report | </a><a style='text-decoration:none;font-weight:bold;color:#ffffff;font-size:13px;' href=#TOP>Beginning | </a><a style='text-decoration:none;font-weight:bold;color:#ffffff;font-size:13px;' href=#End>End</a></span></td></tr>");
							bw.write("<table bgcolor='white' width='100%'>");		
							bw.write("<tr style='color:#ffffff;background: #3c6ac6;font-family:Arial;font-size:12px'><th>"+testCaseType+"</th><th>Test Case Name</th><th>Description</th><th>Result</th><th>Start Date & Time</th><th>End Date & Time</th><th>Execution Time</th></tr>");
							for(int j=0;j<jsonTestCaseSummaryArray.length();j++){			
								org.json.JSONObject testCaseObj =jsonTestCaseSummaryArray.getJSONObject(j);
								if(testCaseObj.getString("job_id").equals(jsonJobReportObject.getString("job_id"))){
									String result = testCaseObj.getString("result");
									String passedOrFailed = "";
									if(result.equalsIgnoreCase("PASSED")){
										passedOrFailed = "<td style='color:#00C800;font-family: DejaVu Sans, Arial, Helvetica, sans-serif;font-size: 12px;'><b>"+result+"</b></td>";
									} else {
										passedOrFailed = "<td style='color:#c80000;font-family: DejaVu Sans, Arial, Helvetica, sans-serif;font-size: 12px;'><b>"+result+"</b></td>";				
									}
									if(testCaseObj.getString("testcasecode").equalsIgnoreCase("N/A")){
										testCaseId=testCaseObj.getString("test_case_id");
									}
									else{
										testCaseId=testCaseObj.getString("testcasecode");
									}
									bw.write("<tr><td colspan='7'><hr/></td></tr><tr style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;vertical-align:top'><td>"+testCaseId+"</td><td><a style='text-decoration: none;font-weight:bold' href=#"+testCaseObj.getString("test_case")+jsonJobReportObject.getString("job_id")+">"+testCaseObj.getString("test_case")+"</a></td><td>"+testCaseObj.getString("description")+"</td>"+passedOrFailed.replace("\"", "")+"<td>"+testCaseObj.getString("starttime")+"</td><td>"+testCaseObj.getString("endtime")+"</td><td>"+testCaseObj.getString("totaltime")+"</td></tr>");
								}						
							}							
							bw.write("</table>");							
							//Test Case Details
							bw.write("<table width='100%'><tr></tr><tr align='left' style='font-family:DejaVu Sans, Arial, Helvetica,sans-serif;color:#ffffff;background:#003ca4;font-weight: bold;height:26px;font-size: 12px'><td>TEST CASE DETAILS</td></tr></table>");
							for(int k = 0; k < jsonTestCaseSummaryArray.length(); k++){			
								org.json.JSONObject testCaseObj = jsonTestCaseSummaryArray.getJSONObject(k);				
								String testcasename = testCaseObj.getString("test_case");
								Integer testCaseIdFromTCSummary = Integer.valueOf(testCaseObj.getString("test_case_id"));
								if(testCaseObj.getString("job_id").equals(jsonJobReportObject.getString("job_id"))){
									//Test case with description
									String featurenames = "";
									StringBuffer sbFeatureNames = new StringBuffer();
									List<Object> mappedFeaturesToTestCase = multimap.get(testCaseIdFromTCSummary);
									if(mappedFeaturesToTestCase.size() > 0){
										for(int ii=0;ii < mappedFeaturesToTestCase.size();ii++){
											if(mappedFeaturesToTestCase.get(ii) != null){
												featurenames = mappedFeaturesToTestCase.get(ii).toString();
												sbFeatureNames.append(featurenames+",");
											}
										}
										if(sbFeatureNames != null && sbFeatureNames.length() > 0){
											featurenames = sbFeatureNames.toString();
											featurenames = featurenames.substring(0,featurenames.length()-1);
										}
									}else{
										featurenames = "N/A";
									}

									bw.write("<table width='100%'>");
									bw.write("<tr style='font-family:Arial;color:#ffffff;font-size:12px;'><th colspan='4' align='left' id="+testCaseObj.getString("test_case")+jsonJobReportObject.getString("job_id")+">Test Case Name  	:"+testCaseObj.getString("test_case")+"</th><th><span style='float:right'><a style='text-decoration:none;font-weight:bold;font-size:13px;color:ffffff;' href=#TESTCASESUMMARY"+jsonJobReportObject.getString("job_id")+">"+"Test Case Summary | "+"</a><a style='text-decoration:none;font-weight:bold;font-size:13px;color:ffffff;' href=#JOBSUMMARY"+jsonJobReportObject.getString("job_id")+">"+"Job Summary | "+"</a><a style='text-decoration:none;font-weight:bold;font-size:13px;color:ffffff;' href=#JOBREPORT>Job Report | </a><a style='text-decoration:none;font-weight:bold;font-size:13px;color:ffffff;' href=#TOP>Beginning | </a><a style='text-decoration:none;font-weight:bold;font-size:13px;color:ffffff;' href=#End>End</a></span></th></tr></table>");
									String testcaseresultStatus = testCaseObj.getString("result");
									String passedOrFailedTestCase = "";
									if(testcaseresultStatus.equalsIgnoreCase("PASSED")){
										passedOrFailedTestCase = "<td style='font-weight: bold'> : <span style='font-family: DejaVu Sans, Arial, Helvetica, sans-serif;color: #00C800;font-size: 12px;font-weight: bold'>"+testcaseresultStatus+"</span></td>";
									} else {
										passedOrFailedTestCase = "<td style='font-weight: bold'> : <span style='font-family: DejaVu Sans, Arial, Helvetica, sans-serif;color: #c80000;font-size: 12px;font-weight: bold'>"+testcaseresultStatus+"</span></td>";				
									}
									//bw.write("<table width='100%' style='table-layout: fixed;'><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px;width:150;'>Test Case description </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;height:17px; font-weight: bold' > : "+testCaseObj.getString("description")+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px;width:150;'>Test Case Code/ID </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;height:17px; font-weight: bold' > : "+testCaseObj.getString("testcasecode")+"/"+testCaseObj.getString("test_case_id")+"</td></tr><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Start Date & Time</th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;font-weight: bold'> : "+testCaseObj.getString("starttime")+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>End Date & Time </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+testCaseObj.getString("endtime")+"</td></tr><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test Case result </th>"+passedOrFailedTestCase.replace("\"", "")+"<th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test Steps Passed </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+testCaseObj.getInt("passedteststep")+"/"+testCaseObj.getInt("totalteststep")+"</td></tr><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Features Covered </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;height:17px; font-weight: bold' > : "+testCaseObj.getString("productFeatureName")+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Risks Covered </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;height:17px; font-weight: bold' > : "+testCaseObj.getString("riskname")+"</td></tr><tr style='height:13px'></tr></table>");
									bw.write("<table width='100%' style='table-layout: fixed;'><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px;width:150;'>Test Case Description </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;height:17px; font-weight: bold' > : "+testCaseObj.getString("description")+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px;width:150;'>Test Case Code/ID </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;height:17px; font-weight: bold' > : "+testCaseObj.getString("testcasecode")+"/"+testCaseObj.getString("test_case_id")+"</td></tr><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Start Date & Time</th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;font-weight: bold'> : "+testCaseObj.getString("starttime")+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>End Date & Time </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+testCaseObj.getString("endtime")+"</td></tr><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test Case result </th>"+passedOrFailedTestCase.replace("\"", "")+"<th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Test Steps Passed </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px; line-height: 1.2578125; font-weight: bold'> : "+testCaseObj.getInt("passedteststep")+"/"+testCaseObj.getInt("totalteststep")+"</td></tr><tr><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Features Covered </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;height:17px; font-weight: bold' > : "+featurenames+"</td><th style='color:white;background: #3c6ac6;font-family:DejaVu Sans,Arial,Helvetica,sans-serif;font-size: 12px;height:25px'>Risks Covered </th><td style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;height:17px; font-weight: bold' > : "+testCaseObj.getString("riskname")+"</td></tr><tr style='height:13px'></tr></table>");
									//Test Step Details
									String testStepType = "Test Step Code";
									boolean testStepTypeFlag = true;
									for(int j=0;j<jsonTestStepArray.length();j++){
										org.json.JSONObject testStepObj = jsonTestStepArray.getJSONObject(j);
										if(testStepObj.getString("teststepcode").equalsIgnoreCase("N/A")&&testStepTypeFlag){
											testStepType = "Teststepid";
											testStepTypeFlag = false;
										}
									}
									//bw.write("<table width='100%' style='table-layout: fixed;'><tr style='color:white;background: #3c6ac6;font-family: Arial;font-size: 12px;height:18px'><th width='8%'>"+testStepType+"</th><th width='10%'>teststepdescription</th><th width='15%'>Expected result</th><th width='15%'>Observed result</th><th width='7%'>Status</th><th width='10%'>Execution Time</th><th width='15%'>Comments</th><th width='20%'>screenshot</th></tr>");
									bw.write("<table width='100%' style='table-layout: fixed;'><tr style='color:white;background: #3c6ac6;font-family: Arial;font-size: 12px;height:18px'><th width='10%' style='padding-left:5px;'>"+testStepType+"</th><th width='40%' style='padding-left:5px;'>Test Step Summary</th><th width='50%' style='padding-left:5px;'>Screenshot</th></tr>");

									for(int j=0;j<jsonTestStepArray.length();j++){
										org.json.JSONObject testStepObj =jsonTestStepArray.getJSONObject(j);
										String testStepresultStatus = testStepObj.getString("resultstatus");
										String passedOrFailedTestStep = "",failureTestStepComments="";
										String testStepId=testStepObj.getString("teststepcode");;
										if(testStepresultStatus.equalsIgnoreCase("PASSED")){
											//passedOrFailedTestStep = "<td style='font-family: DejaVu Sans, Arial, Helvetica, sans-serif;color: #00C800;font-size: 12px;font-weight: bold;' width='5%'>"+testStepresultStatus+"</td>";
											passedOrFailedTestStep = "<span style='font-family: DejaVu Sans, Arial, Helvetica, sans-serif;color: #00C800;font-size: 12px;font-weight: bold;'>"+testStepresultStatus+"</span>";
										} else {
											//passedOrFailedTestStep = "<td style='font-family: DejaVu Sans, Arial, Helvetica, sans-serif;color: #c80000;font-size: 12px;font-weight: bold;' width='5%'>"+testStepresultStatus+"</td>";
											passedOrFailedTestStep = "<span style='font-family: DejaVu Sans, Arial, Helvetica, sans-serif;color: #c80000;font-size: 12px;font-weight: bold;'>"+testStepresultStatus+"</span>";
										}
										if(testStepId.equalsIgnoreCase("N/A")){
											testStepId = testStepObj.getString("teststepid");
										}
										if(showTestStepExecutionId != null && showTestStepExecutionId.equalsIgnoreCase("YES")){
											testStepId = testStepId +" ["+ testStepObj.getString("teststepexecutionid") +"]";
										}
										if(testStepObj.getString("testcasename").equalsIgnoreCase(testcasename) && testStepObj.getString("job_id").equals(jsonJobReportObject.getString("job_id"))){					
											String testStep = testStepObj.getString("screenshot");

											if(!testStepresultStatus.equalsIgnoreCase("PASSED") && !testStepObj.getString("failurereason").equalsIgnoreCase("N/A")){
												failureTestStepComments = testStepObj.getString("failurereason");
												/*if(failureTestStepComments.length() > 50)
													failureTestStepComments = failureTestStepComments.substring(0, 50);*/
												failureTestStepComments = "<b>Comments : </b><br/><br/>    "+failureTestStepComments+"<br/>";
												log.info("test step comments"+failureTestStepComments);
											}
											if(new File(testStep).exists()){												
												testStepPath.add(testStep);
												log.info(testStep);
												testStep= testStep.substring(testStep.lastIndexOf("\\")+1, testStep.length());
												closeImagePath = CommonUtility.getCatalinaPath().concat("\\webapps"+ File.separator + request.getContextPath() + File.separator +"css\\images\\close_screenshot_image.jpg");
												closeImagePath=closeImagePath.substring(closeImagePath.lastIndexOf("\\")+1, closeImagePath.length());
												//bw.write("<tr><td colspan='8'><hr/></td></tr><tr style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;vertical-align:top'><td width='5%' style='word-wrap: break-word;'>"+testStepId+"</td><td width='10%' style='word-wrap: break-word;'>"+testStepObj.getString("teststepdescription")+"</td><td width='15%' style='word-wrap: break-word;'>"+testStepObj.getString("expectedoutput")+"</td><td width='15%' style='word-wrap: break-word;'>"+testStepObj.getString("observedoutput")+"</td>"+passedOrFailedTestStep.replace("\"", "")+"<td align='middle' width='10%' style='word-wrap: break-word;'>"+testStepObj.getString("totalteststepstime")+"</td><td width='20%' style='word-wrap: break-word;'>"+failureTestStepComments+"</td><td width='20%' style='word-wrap: break-word;'><a href='javascript:void(0);'><img height='100' width='200' src='screenshots/"+testStep+"' onclick=\"display('screenshots/"+testStep+"')\";/></a><div id=\"tag\" class=\"parentdiv\" style=\"display:none;\"><div id=\"childdiv\" class=\"center\"><a href='javascript:void(0);'><img align='right' src='screenshots/"+closeImagePath+"' height=\"20\" width=\"20;\" alt='Close' onclick=\"document.getElementById('tag').style.display='none';\"/></a><img id=\"f\" height=\"100%\" width=\"70%\"/></div></div></td></tr>");
												//bw.write("<tr><td colspan='8'><hr/></td></tr><tr style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;vertical-align:top'><td width='5%' style='word-wrap: break-word;'>"+testStepId+"</td><td width='40%' style='word-wrap: break-word;'><pre style=\"height : auto ; max-height : 400px; overflow : auto; font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;vertical-align:top; white-space: pre-wrap; word-wrap: break-word;\"><b>Description:</b> <br/><br/>    "+testStepObj.getString("teststepdescription")+"<br/><br/><br/><b>Expected : </b><br/><br/>    "+testStepObj.getString("expectedoutput")+"<br/><br/><b>Observed : </b><br/><br/>    "+testStepObj.getString("observedoutput")+"<br/><br/><b>Status : </b><br/><br/>    "+passedOrFailedTestStep.replace("\"", "")+"<br/><br/><b>Execution Time:</b> <br/><br/>    "+testStepObj.getString("totalteststepstime")+"<br/><br/>"+failureTestStepComments+"<br/></pre></td><td width='50%' style='word-wrap: break-word;'><a href='javascript:void(0);'><img height='400' width='500' src='screenshots/"+testStep+"' onclick=\"display('screenshots/"+testStep+"')\";/></a><div id=\"tag\" class=\"parentdiv\" style=\"display:none;\"><div id=\"childdiv\" class=\"center\"><a href='javascript:void(0);'><img align='right' src='screenshots/"+closeImagePath+"' height=\"20\" width=\"20;\" alt='Close' onclick=\"document.getElementById('tag').style.display='none';\"/></a><img id=\"f\" height=\"100%\" width=\"70%\"/></div></div></td></tr>");
												bw.write("<tr><td colspan='8'><hr/></td></tr><tr style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;vertical-align:top'><td width='5%' style='word-wrap: break-word;'>"+testStepId+"</td><td width='40%' style='word-wrap: break-word;'><pre style=\"height : auto ; max-height : 400px; overflow : auto; font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;vertical-align:top; white-space: pre-wrap; word-wrap: break-word;\"><b>Test Step Name:</b> <br/><br/>    "+testStepObj.getString("teststep")+"<br/><br/><br/><b>Description:</b> <br/><br/>    "+testStepObj.getString("teststepdescription")+"<br/><br/><br/><b>Executed Environment : </b><br/><br/>    "+testStepObj.getString("testenvironmentname")+"<br/><br/><b>Test Step Input : </b><br/><br/>    "+testStepObj.getString("teststepinput")+"<br/><br/><b>Expected : </b><br/><br/>   "+testStepObj.getString("expectedoutput")+"<br/><br/><b>Observed : </b><br/><br/>    "+testStepObj.getString("observedoutput")+"<br/><br/><b>Status : </b><br/><br/>    "+passedOrFailedTestStep.replace("\"", "")+"<br/><br/><b>Execution Time:</b> <br/><br/>    "+testStepObj.getString("totalteststepstime")+"<br/><br/>"+failureTestStepComments+"<br/></pre></td><td width='50%' style='word-wrap: break-word;'><a href='javascript:void(0);'><img height='400' width='500' src='screenshots/"+testStep+"' onclick=\"display('screenshots/"+testStep+"')\";/></a><div id=\"tag\" class=\"parentdiv\" style=\"display:none;\"><div id=\"childdiv\" class=\"center\"><a href='javascript:void(0);'><img align='right' src='screenshots/"+closeImagePath+"' height=\"20\" width=\"20;\" alt='Close' onclick=\"document.getElementById('tag').style.display='none';\"/></a><img id=\"f\" height=\"100%\" width=\"70%\"/></div></div></td></tr>");
											} else {
												log.info(testStep);
												closeImagePath = CommonUtility.getCatalinaPath().concat("\\webapps"+ File.separator + request.getContextPath() + File.separator +"css\\images\\noscreenshot_available.jpg");
												closeImagePath = closeImagePath.substring(closeImagePath.lastIndexOf("\\")+1, closeImagePath.length());
												//bw.write("<tr><td colspan='8'><hr/></td></tr><tr style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;vertical-align:top'><td width='10%' style='word-wrap: break-word;'>"+testStepId+"</td><td width='40%' style='word-wrap: break-word;'><pre style=\"height : auto ; max-height : 400px; overflow : auto; font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;vertical-align:top; white-space: pre-wrap; word-wrap: break-word;\"><b>Description:</b> <br/><br/>    "+testStepObj.getString("teststepdescription")+"<br/><br/><br/><b>Expected : </b><br/><br/>    "+testStepObj.getString("expectedoutput")+"<br/><br/><b>Observed : </b><br/><br/>    "+testStepObj.getString("observedoutput")+"<br/><br/><b>Status : </b><br/><br/>    "+passedOrFailedTestStep.replace("\"", "")+"<br/><br/><b>Execution Time:</b> <br/><br/>    "+testStepObj.getString("totalteststepstime")+"<br/><br/>"+failureTestStepComments+"<br/></pre></td><td width='50%' style='word-wrap: break-word;'>No screenshot available for the test step</td></tr>");
												bw.write("<tr><td colspan='8'><hr/></td></tr><tr style='font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;vertical-align:top'><td width='5%' style='word-wrap: break-word;'>"+testStepId+"</td><td width='40%' style='word-wrap: break-word;'><pre style=\"height : auto ; max-height : 400px; overflow : auto; font-family:DejaVu Sans, Arial, Helvetica, sans-serif; color: #000000; font-size: 12px;vertical-align:top; white-space: pre-wrap; word-wrap: break-word;\"><b>Test Step Name:</b> <br/><br/>    "+testStepObj.getString("teststep")+"<br/><br/><br/><b>Description:</b> <br/><br/>    "+testStepObj.getString("teststepdescription")+"<br/><br/><br/><b>Executed Environment : </b><br/><br/>    "+testStepObj.getString("testenvironmentname")+"<br/><br/><b>Test Step Input : </b><br/><br/>    "+testStepObj.getString("teststepinput")+"<br/><br/><b>Expected : </b><br/><br/>    "+testStepObj.getString("expectedoutput")+"<br/><br/><b>Observed : </b><br/><br/>    "+testStepObj.getString("observedoutput")+"<br/><br/><b>Status : </b><br/><br/>    "+passedOrFailedTestStep.replace("\"", "")+"<br/><br/><b>Execution Time:</b> <br/><br/>    "+testStepObj.getString("totalteststepstime")+"<br/><br/>"+failureTestStepComments+"<br/></pre></td><td width='50%' style='word-wrap: break-word;'>No screenshot available for the test step</td></tr>");
											}
										}
									}
									bw.write("</table>");									
								}				
							}								
						}
					}catch (IOException | JSONException e) {		
						log.error(e);
						jTableResponse = new JTableResponse("OK",abortedWorkPackages,"NODATA");
						return jTableResponse;
					} 					
				} else {
					log.info("no data ");
					jTableResponse = new JTableResponse("OK",abortedWorkPackages,"NODATA");
					return jTableResponse;
				}			
			}
			count++; 
		}		
		bw.write("<div id='End'><span style='float:right;font-weight:bold;font-family:DejaVu Sans, Arial, Helvetica,sans-serif;font-size:13px'><a style='text-decoration:none;' href='#' onclick='window.history.go(-1);return false'>Go Back</a> | <a style='text-decoration:none;' href='#TOP'>Beginning</a></span></div>");
		bw.write("<br/><br/>");
		bw.write("<div id='sign'><span style='float:left;font-weight:bold;font-family:DejaVu Sans, Arial, Helvetica,sans-serif;font-size:13px'>Username : "+loginUserName+"</span> <span style='float:right;font-weight:bold;font-family:DejaVu Sans, Arial, Helvetica,sans-serif;font-size:13px'>Signature : &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>");
		bw.write("</body></html>");
		bw.close();		
		try{
			File screenshotFolder = null;
			File logoFolder  = null;
			FileOutputStream fos = new FileOutputStream(CommonUtility.getCatalinaPath() + File.separator + "webapps" + File.separator + request.getContextPath() + File.separator+"CWPReport"+File.separator+generatedFileName+".zip");
			ZipOutputStream zos = new ZipOutputStream(fos);
			List<String> screenshotImages = new ArrayList<String>();
			String closeScreenshotPath = CommonUtility.getCatalinaPath().concat("\\webapps"+ File.separator + request.getContextPath()+"\\css\\images\\close_screenshot_image.jpg");
			String noScreenshotAvailableImagePath = CommonUtility.getCatalinaPath().concat("\\webapps"+ File.separator + request.getContextPath()+"\\css\\images\\noscreenshot_available.jpg");
			screenshotImages.add(closeScreenshotPath);
			screenshotImages.add(noScreenshotAvailableImagePath);
			screenshotImages.addAll(testStepPath);
			List<String> logoImage = new ArrayList<String>();
			logoImage.add(imageServerPath);
			List<String> names = new ArrayList<String>();
			names.add(workpackageHtmlFile);

			for(String name : names){				    			
				if(new File(name).exists()){
					zos.putNextEntry(new ZipEntry(new File(name).getName()));
					FileInputStream in = new FileInputStream(name);					    			
					if(in != null){
						int len;
						while ((len = in.read(buffer)) > 0) {
							zos.write(buffer, 0, len);
						}						    		
						zos.closeEntry();
						in.close();
					}			
				}else{

				}
			}	
			for(String name : screenshotImages){
				log.info(name);
				//screenshotFolder  = new File(htmlReportGenLocation+File.separator+"HtmlReport"+File.separator+productName+File.separator+"screenshots");
				screenshotFolder  = new File(CommonUtility.getCatalinaPath() + File.separator + "webapps" + File.separator + request.getContextPath() +File.separator+"CWPReport"+File.separator+generatedFileName+File.separator+"screenshots");
				if(!screenshotFolder.exists()){
					screenshotFolder.mkdirs();				    				
				} 
				if(name != null && name.endsWith(".png")){
					File screenshotFile = new File(name);
					if(screenshotFile.exists()){
						FileUtils.copyFile(screenshotFile, new File(screenshotFolder + File.separator + screenshotFile.getName()));
					}
				}else{
					log.info("Image is not captured for the test step and path :"+name);
				}
			}	
			for(String name : logoImage){	
				log.info("logo path is :"+name);
				//logoFolder  = new File(htmlReportGenLocation+File.separator+"HtmlReport"+File.separator+productName+File.separator+"logo");
				logoFolder  = new File(CommonUtility.getCatalinaPath() + File.separator + "webapps" + File.separator + request.getContextPath() + File.separator+"CWPReport"+File.separator+generatedFileName+File.separator+"logo");
				if(!logoFolder.exists()){
					logoFolder.mkdirs();				    				
				}
				File logoFile = new File(name);
				if(logoFile.exists()){
					FileUtils.copyFile(logoFile, new File(logoFolder + File.separator + logoFile.getName()));
				}
			}	
			zipDir(zos,"",screenshotFolder);
			zipDir(zos,"",logoFolder);				    		
			zos.close();			

			File file = new File(CommonUtility.getCatalinaPath() + File.separator + "webapps" + File.separator + request.getContextPath() + File.separator+"CWPReport"+File.separator+generatedFileName);
			if(file.exists()){
				FileUtils.deleteDirectory(file);
			}

			//Extract the compressed file to view in browser
			ZipTool.unzip(CommonUtility.getCatalinaPath() + File.separator + "webapps" + File.separator + request.getContextPath() + File.separator+"CWPReport"+File.separator+generatedFileName+".zip", CommonUtility.getCatalinaPath() + File.separator + "webapps" + File.separator + request.getContextPath() + File.separator+ "CWPReport" + File.separator + generatedFileName);

			//format the file for directly open in browser for viewing 
			fileToOpenInBrowser = CommonUtility.getCatalinaPath() + File.separator + "webapps" + File.separator + request.getContextPath() + File.separator +"CWPReport"+File.separator+ generatedFileName+ File.separator + "Consolidated_WorkPackage_Report_"+ generatedFileName +".html"; 
			fileToDownload = CommonUtility.getCatalinaPath() + File.separator + "webapps" + File.separator + request.getContextPath() + File.separator +"CWPReport"+File.separator+generatedFileName+".zip";
		}catch(IOException ex){
			log.error("error while iterating..."+ex);
			//if(testRunNo != -1){
			jTableResponse = new JTableResponse("OK",abortedWorkPackages,"NODATA");
			//}
			return jTableResponse;
		}
		if(type.equalsIgnoreCase("viewReport")){			
			if(! new File(fileToOpenInBrowser).exists()) {
				jTableResponse = new JTableResponse("OK",abortedWorkPackages,"NODATA");			
			} else {		
				fileToOpenInBrowser = "http://" + request.getServerName() + ":" + request.getServerPort() +request.getContextPath() + File.separator + "CWPReport" + File.separator + generatedFileName + File.separator +"Consolidated_WorkPackage_Report_"+generatedFileName+".html";
				jTableResponse = new JTableResponse("Ok","Export testCases Completed.",fileToOpenInBrowser);			
			}
		}else if(type.equalsIgnoreCase("downloadReport")) {
			if(! new File(fileToOpenInBrowser).exists()) {
				jTableResponse = new JTableResponse("OK",abortedWorkPackages,"NODATA");			
			} else {		
				//fileToOpenInBrowser = "http://" + request.getServerName() + ":" + request.getServerPort() +request.getContextPath() + File.separator + "HtmlReport" + File.separator + productName + File.separator + productName +"_Consolidated_WorkPackage_Report.html";
				fileToDownload = "http://" + request.getServerName() + ":" + request.getServerPort() +request.getContextPath() + File.separator + "CWPReport" + File.separator + generatedFileName+".zip";
				jTableResponse = new JTableResponse("Ok","Export testCases Completed.",fileToDownload);			
			}
		}
		return jTableResponse;
	}
	

	@RequestMapping(value="report.run.evidence.xml" ,method=RequestMethod.POST , produces="application/html")
	public  @ResponseBody JTableResponse generateWorkPackageXmlReport(@RequestParam String workPackageId, ModelAndView modelAndView, HttpServletRequest request, HttpServletResponse response) throws Exception {
		JTableResponse jTableResponse = null;
		WorkPackageBean workPackageBean = null;
		String fileToOpenInBrowser = null;
		String xmlreportPath = CommonUtility.getCatalinaPath() + File.separator + "webapps" + File.separator + request.getContextPath() + File.separator+"XMLReport";
		if(workPackageId != null){
			 workPackageBean = workPackageService.getWorkPackageBeanById(Integer.parseInt(workPackageId));
		}
		if(workPackageBean != null){
			xmlreportPath = jaxbObjectToXML(workPackageBean,xmlreportPath);
		}
		if(workPackageBean != null && xmlreportPath != null){
			fileToOpenInBrowser = "http://" + request.getServerName() + ":" + request.getServerPort() +request.getContextPath() + File.separator + "XMLReport" + File.separator + workPackageId + File.separator + workPackageId +".xml";
			jTableResponse = new JTableResponse("Ok","Export Xml Completed.",fileToOpenInBrowser);
		}else {
			jTableResponse = new JTableResponse("ERROR","No records available for the current Workpackage "+workPackageId);
		}
		return jTableResponse;
		
	}
	
	private static String jaxbObjectToXML(WorkPackageBean wpBean,String xmlReportFolderPath) {
		String fileLocation = "";
		try {
			JAXBContext context = JAXBContext.newInstance(WorkPackageBean.class);
			Marshaller m = context.createMarshaller();
			fileLocation = xmlReportFolderPath + File.separator + wpBean.getWorkPackageId();
			if(!new File(fileLocation ).exists()){
				new File(fileLocation).mkdirs();
			}
			//for pretty-print XML in JAXB
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			// Write to System.out for debugging
			// m.marshal(emp, System.out);

			// Write to File
			if(wpBean.getName() != null)
				fileLocation = fileLocation + File.separator + wpBean.getWorkPackageId() + ".xml";
				m.marshal(wpBean, new File(fileLocation));
		} catch (JAXBException e) {
			log.error("Error while converting bean to xml file", e);
			return null;
		}
		return fileLocation;
	}

}