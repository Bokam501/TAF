package com.hcl.atf.taf.scheduler.jobs;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.io.filefilter.AgeFileFilter;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bson.types.ObjectId;
import org.json.JSONObject;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.ActivityCollection;
import com.hcl.atf.taf.model.Attachment;
import com.hcl.atf.taf.model.DPAWorkbookCollection;
import com.hcl.atf.taf.model.DataExtractionReportSummary;
import com.hcl.atf.taf.model.DataExtractorNotification;
import com.hcl.atf.taf.model.DataExtractorScheduleMaster;
import com.hcl.atf.taf.model.DefectCollection;
import com.hcl.atf.taf.model.EntityMaster;
import com.hcl.atf.taf.model.ReviewRecordCollection;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.UtilizationCollection;
import com.hcl.atf.taf.mongodb.model.ActivityCollectionMongo;
import com.hcl.atf.taf.mongodb.model.CalculatedDefectCollectionMongo;
import com.hcl.atf.taf.mongodb.model.DPAWorkbookCollectionMongo;
import com.hcl.atf.taf.mongodb.model.DefectCollectionMongo;
import com.hcl.atf.taf.mongodb.model.ReviewRecordCollectionMongo;
import com.hcl.atf.taf.mongodb.model.UtilizationCollectionMongo;
import com.hcl.atf.taf.mongodb.service.DataSourceExtractorMongoService;
import com.hcl.atf.taf.mongodb.service.MongoDBService;
import com.hcl.atf.taf.schedule.data.extractor.ApTestScheduleDataExtractor;
import com.hcl.atf.taf.schedule.data.extractor.CMPMScheduleDataExtractor;
import com.hcl.atf.taf.schedule.data.extractor.CQDumpScheduleDataExtractor;
import com.hcl.atf.taf.schedule.data.extractor.CommonDataScheduleDataExtractor;
import com.hcl.atf.taf.schedule.data.extractor.DPAWorkbookScheduleDataExtractor;
import com.hcl.atf.taf.schedule.data.extractor.DataSourceScheduleMapperExtractor;
import com.hcl.atf.taf.schedule.data.extractor.DefectDumpScheduleDataExtractor;
import com.hcl.atf.taf.schedule.data.extractor.EffortScheduleDataExtractor;
import com.hcl.atf.taf.schedule.data.extractor.JIRADefectDumpScheduleDataExtractor;
import com.hcl.atf.taf.schedule.data.extractor.MagenMatricsScheduleDataExtractor;
import com.hcl.atf.taf.schedule.data.extractor.PMOScheduleDataExtractor;
import com.hcl.atf.taf.schedule.data.extractor.PMSmartScheduleDataExtractor;
import com.hcl.atf.taf.schedule.data.extractor.PairWiseScheduleDataExtractor;
import com.hcl.atf.taf.schedule.data.extractor.QueryLogScheduleDataExtractor;
import com.hcl.atf.taf.schedule.data.extractor.ReviewRecordScheduleDataExtractor;
import com.hcl.atf.taf.schedule.data.extractor.SingleValueMeticScheduleDataExtractor;
import com.hcl.atf.taf.schedule.data.extractor.TaskReportScheduleDataExtractor;
import com.hcl.atf.taf.schedule.data.extractor.TestPlanScheduleDataExtractor;
import com.hcl.atf.taf.schedule.data.extractor.TrainingScheduleDataExtractor;
import com.hcl.atf.taf.schedule.data.extractor.UtilizationIndexScheduleDataExtractor;
import com.hcl.atf.taf.schedule.data.extractor.WATSScheduleDataExtractor;
import com.hcl.atf.taf.service.CommonService;
import com.hcl.atf.taf.service.DataSourceExtractorService;
import com.hcl.atf.taf.service.EmailService;
import com.opencsv.CSVReader;

@Service
public class DataSourceExtractorJob implements Job {
	
private static final Log log = LogFactory.getLog(DataSourceExtractorJob.class);
	
	@Autowired
	private DataSourceScheduleMapperExtractor dataSourceScheduleMapperExtractor;
	
	@Autowired
	private TestPlanScheduleDataExtractor testPlanScheduleDataExtractor;
	
	@Autowired
	private ApTestScheduleDataExtractor apTestScheduleDataExtractor;
	
	@Autowired
	private PairWiseScheduleDataExtractor pairWiseScheduleDataExtractor;

	@Autowired
	private MagenMatricsScheduleDataExtractor magenMatricsScheduleDataExtractor;
	
	@Autowired
	private CQDumpScheduleDataExtractor cqDumpScheduleDataExtractor;
	
	@Autowired
	private PMSmartScheduleDataExtractor pmSmartScheduleDataExtractor;
	
	@Autowired
	private QueryLogScheduleDataExtractor queryLogScheduleDataExtractor;
	
	@Autowired
	private TaskReportScheduleDataExtractor taskReportScheduleDataExtractor;
	
	@Autowired
	private WATSScheduleDataExtractor watsScheduleDataExtractor;
	
	@Autowired
	private ReviewRecordScheduleDataExtractor reviewRecordScheduleDataExtractor;

	@Autowired
	private DPAWorkbookScheduleDataExtractor dpaWorkbookScheduleDataExtractor;
	
	@Autowired
	private UtilizationIndexScheduleDataExtractor utilizationIndexScheduleDataExtractor;
	
	@Autowired
	private TrainingScheduleDataExtractor trainingScheduleDataExtractor;
	
	@Autowired
	private EffortScheduleDataExtractor effortScheduleDataExtractor;
	
	@Autowired
	private SingleValueMeticScheduleDataExtractor singleValueMeticScheduleDataExtractor;
	
	@Autowired
	private PMOScheduleDataExtractor pmoScheduleDataExtractor;
	
	@Autowired
	private CMPMScheduleDataExtractor cmpmScheduleDataExtractor;
	
	@Autowired
	private DefectDumpScheduleDataExtractor defectDumpScheduleDataExtractor;

	@Autowired
	private JIRADefectDumpScheduleDataExtractor jiraDefectDumpScheduleDataExtractor;
	
	@Autowired
	private CommonDataScheduleDataExtractor commonDataScheduleDataExtractor;
	
	@Autowired
	private DataSourceExtractorService dataSourceExtractorService;
	
	@Autowired
	private DataSourceExtractorMongoService dataSourceExtractorMongoService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private MongoDBService mongoDBService;
	
	@Autowired
	private CommonService commonService;
	
	@Value("#{ilcmProps['DATA_EXTRACTOR_BASE_LOCATION']}")
    private String DATA_EXTRACTOR_BASE_LOCATION;
	
	private String mapperFile = ""; 
	private List<String> closedDefects = new ArrayList<String>();
	private List<String> invalidDefects = new ArrayList<String>();
	private List<String> nonQualityDefects = new ArrayList<String>();
	private List<String> leakedDefects = new ArrayList<String>();
	private List<String> collections = new ArrayList<String>();
	private List<String> weightages = new ArrayList<String>();
	private List<String> projectDeliverables = new ArrayList<String>();
	private List<String> valueAddeds = new ArrayList<String>();
	private List<String> skillDevelopments = new ArrayList<String>();
	private List<String> generalMeetings = new ArrayList<String>();
	private List<String> nonWorkingHours = new ArrayList<String>();
	private float testCaseWeightage = 1.0F;
	private float pairwiseWeightage = 1.0F;
	private float testJobWeightage = 1.0F;
	private float testJobOrPairwiseWeightage = 1.0F;
	private String notificationEmailIds = "";
	private Boolean isMailingEnabled = false;
	
	private String extractionReportType = "xlsx";
	
	@SuppressWarnings("unchecked")
	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		DataExtractorNotification dataExtractorNotification = new DataExtractorNotification();
		dataExtractorNotification.setExtarctionDate(new Date());
		Workbook extractionReportWorkbook = null;
		String extractionReportPath = null;
		String currentDate = DateUtility.dateToStringInSecond(new Date());
		currentDate = currentDate.replaceAll(":", "_");
		DataExtractorScheduleMaster dataExtractorScheduleMaster = null;
		try{
			SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
			
			dataExtractorScheduleMaster = dataSourceExtractorService.getDataExtractorByJobName(jobExecutionContext.getJobDetail().getKey().getName(), null);
			extractionReportPath = DATA_EXTRACTOR_BASE_LOCATION+"\\ExtractionReport\\";
			
			HashMap<String, Object> extractorDetails = readExtarctorDetailsProperties();
			mapperFile = (String) extractorDetails.get("mapperFile");
			collections = (List<String>) extractorDetails.get("collections");
			closedDefects = (List<String>) extractorDetails.get("closedDefects");
			invalidDefects = (List<String>) extractorDetails.get("invalidDefects");
			nonQualityDefects = (List<String>) extractorDetails.get("nonQualityDefects");
			leakedDefects = (List<String>) extractorDetails.get("leakedDefects");
			weightages = (List<String>) extractorDetails.get("weightage");
			projectDeliverables = (List<String>) extractorDetails.get("projectDeliverables");
			valueAddeds = (List<String>) extractorDetails.get("valueAddeds");
			skillDevelopments = (List<String>) extractorDetails.get("skillDevelopments");
			generalMeetings = (List<String>) extractorDetails.get("generalMeetings");
			nonWorkingHours = (List<String>) extractorDetails.get("nonWorkingHours");
			isMailingEnabled = (Boolean) extractorDetails.get("isMailingEnabled");
			
			int i = 1;
			for(String weightage : weightages){
				if(i == 1 && NumberUtils.isNumber(weightage)){
					testCaseWeightage = Float.parseFloat(weightage);
				}else if(i == 2 && NumberUtils.isNumber(weightage)){
					pairwiseWeightage = Float.parseFloat(weightage);
				}else if(i == 3 && NumberUtils.isNumber(weightage)){
					testJobWeightage = Float.parseFloat(weightage);
				}else if(i == 4 && NumberUtils.isNumber(weightage)){
					testJobOrPairwiseWeightage = Float.parseFloat(weightage);
				}
				i++;
			}
			notificationEmailIds = (String)extractorDetails.get("notificationEmailIds");
			extractionReportType = (String)extractorDetails.get("extractionReportType");
			if(extractionReportWorkbook == null){
				if(extractionReportType == null || extractionReportType.isEmpty() || "xlsx".equalsIgnoreCase(extractionReportType) || "xlsm".equalsIgnoreCase(extractionReportType)){
					extractionReportWorkbook = new XSSFWorkbook();
				}else if("xls".equalsIgnoreCase(extractionReportType)){
					extractionReportWorkbook = new HSSFWorkbook();
				}else{
					return;
				}
				if(extractionReportType == null || extractionReportType.isEmpty()){
					extractionReportType = "xlsx";
				}
			}
			
			if(dataExtractorScheduleMaster == null){
				dataExtractorNotification.setConfigurationFailed(true);
				dataExtractorNotification.getMessageList().add(new Date()+" Unable to run scheduled job due to insufficient details, please check the extraction configuration");
				extractionNotification(dataExtractorNotification, extractionReportWorkbook, dataExtractorScheduleMaster);
				if(extractionReportWorkbook != null && extractionReportWorkbook.getSheetAt(0) != null){
					writeReportFile(extractionReportWorkbook, extractionReportPath, "InsufficientDetails-"+currentDate+"."+extractionReportType, dataExtractorScheduleMaster);
				}
				return;
			}
			log.info("Executing schedular for Job Name - "+dataExtractorScheduleMaster.getJobName());
			extractionReportPath = dataExtractorScheduleMaster.getFileLocation()+"\\ExtractionReport\\";
			
			if(jobExecutionContext.getNextFireTime() != null && dataExtractorScheduleMaster.getEndDate() != null && (dataExtractorScheduleMaster.getEndDate().equals(new Date()) || dataExtractorScheduleMaster.getEndDate().after(new Date()))){
				dataExtractorScheduleMaster.setNextExecution(jobExecutionContext.getNextFireTime());
			}
			dataExtractorScheduleMaster.setLastExecuted(jobExecutionContext.getFireTime());
			dataSourceExtractorService.updateDataExtractorScheduleMasster(dataExtractorScheduleMaster);
			
			if(!dataSourceExtractorMongoService.checkMongoConnectivity()){
				dataExtractorNotification.setConfigurationFailed(true);
				dataExtractorNotification.getMessageList().add(new Date()+" Unable to run scheduled job due to connection issue with Mongo database, please check the DB is running and configured correctly");
				extractionNotification(dataExtractorNotification, extractionReportWorkbook, dataExtractorScheduleMaster);
				if(extractionReportWorkbook != null && extractionReportWorkbook.getSheetAt(0) != null){
					writeReportFile(extractionReportWorkbook, extractionReportPath, "MongoConnectivityIssue-"+currentDate+"."+extractionReportType, dataExtractorScheduleMaster);
				}
				return;
			}
			
			String errorMessage = (String) extractorDetails.get("errorMessage");
			
			if(errorMessage != null && !errorMessage.trim().isEmpty()){
				dataExtractorNotification.setConfigurationFailed(true);
				dataExtractorNotification.getMessageList().add(new Date()+" "+errorMessage);
				extractionNotification(dataExtractorNotification, extractionReportWorkbook, dataExtractorScheduleMaster);
				if(extractionReportWorkbook != null && extractionReportWorkbook.getSheetAt(0) != null){
					writeReportFile(extractionReportWorkbook, extractionReportPath, "PropertyConfigurationIssue-"+currentDate+"."+extractionReportType, dataExtractorScheduleMaster);
				}
				return;
			}
			
			for(String collection : collections){
				collectionTemplateReader(dataExtractorScheduleMaster, collection, extractionReportWorkbook);
				if(dataExtractorScheduleMaster.getIsStopProcess() && dataExtractorScheduleMaster.getIsExctractorTypeFound()){
					break;
				}
			}
			
			if(!dataExtractorScheduleMaster.getIsExctractorTypeFound()){
				dataExtractorNotification.setConfigurationFailed(true);
				if(dataExtractorScheduleMaster.getProduct() != null){
					dataExtractorNotification.setProductName(dataExtractorScheduleMaster.getProduct().getProductName());
				}
				dataExtractorNotification.setExtractorType(dataExtractorScheduleMaster.getExtractorType().getExtarctorName());
				dataExtractorNotification.getMessageList().add(new Date()+" No mapping available for extractor type - "+dataExtractorScheduleMaster.getExtractorType().getExtarctorName()+" in mapper file "+mapperFile+" to extract");
				extractionNotification(dataExtractorNotification, extractionReportWorkbook, dataExtractorScheduleMaster);
				log.info(" No mapping available for extractor type - "+dataExtractorScheduleMaster.getExtractorType().getExtarctorName()+" in mapper file "+mapperFile+" to extract");
				if(extractionReportWorkbook != null && extractionReportWorkbook.getSheetAt(0) != null){
					writeReportFile(extractionReportWorkbook, extractionReportPath, "MappingConfigurationIssue-"+currentDate+"."+extractionReportType, dataExtractorScheduleMaster);
				}
				return;
			}
			
		}catch(Exception ex){
			dataExtractorNotification.setConfigurationFailed(true);
			dataExtractorNotification.getMessageList().add(new Date()+" Unable to execute scheduled job due to "+ex.getMessage());
			extractionNotification(dataExtractorNotification, extractionReportWorkbook, dataExtractorScheduleMaster);
			log.error("Error while executing schedular", ex);
		}finally{
			try{
				if(extractionReportWorkbook != null && extractionReportWorkbook.getNumberOfSheets() > 0 && extractionReportWorkbook.getSheetAt(0) != null){
					writeReportFile(extractionReportWorkbook, extractionReportPath, "Report-"+currentDate+"."+extractionReportType, dataExtractorScheduleMaster);
				}
			}catch(Exception ex){
				log.error("Exception while generating report - ", ex);
			}
		}
		
	}
	
	private HashMap<String, Object> readExtarctorDetailsProperties(){
		HashMap<String, Object> extractorDetails = new HashMap<String, Object>();
		extractorDetails.put("errorMessage", "");
		InputStream inputStream = null;
		try{
			Properties properties = new Properties();
			inputStream = this.getClass().getResourceAsStream("/TAFServer.properties");
			properties.load(inputStream);
			
			if(properties.containsKey("MAPPER_FILE")){
				extractorDetails.put("mapperFile", properties.get("MAPPER_FILE"));
			}else{
				extractorDetails.put("mapperFile", "");
			}
			if(properties.containsKey("COLLECTIONS")){
				String collections = properties.getProperty("COLLECTIONS");
				List<String> collectionList = Arrays.asList(collections.split(","));
				extractorDetails.put("collections", collectionList);
			}else{
				extractorDetails.put("collections", new ArrayList<String>());
			}
			if(properties.containsKey("CLOSED_DEFECTS")){
				String closedDefetcs = properties.getProperty("CLOSED_DEFECTS").toLowerCase();
				List<String> closedDefectList = Arrays.asList(closedDefetcs.split(","));
				extractorDetails.put("closedDefects", closedDefectList);
			}else{
				extractorDetails.put("closedDefects", new ArrayList<String>());
			}
			if(properties.containsKey("INVALID_DEFECTS")){
				String invalidDefects = properties.getProperty("INVALID_DEFECTS").toLowerCase();
				List<String> invalidDefectList = Arrays.asList(invalidDefects.split(","));
				extractorDetails.put("invalidDefects", invalidDefectList);
			}else{
				extractorDetails.put("invalidDefects", new ArrayList<String>());
			}
			if(properties.containsKey("NON_QUALITY_DEFECTS")){
				String nonQualityDefects = properties.getProperty("NON_QUALITY_DEFECTS").toLowerCase();
				List<String> nonQualityDefectList = Arrays.asList(nonQualityDefects.split(","));
				extractorDetails.put("nonQualityDefects", nonQualityDefectList);
			}else{
				extractorDetails.put("nonQualityDefects", new ArrayList<String>());
			}
			if(properties.containsKey("LEAKED_DEFETCS")){
				String leakedDefects = properties.getProperty("LEAKED_DEFETCS").toLowerCase();
				List<String> leakedDefectList = Arrays.asList(leakedDefects.split(","));
				extractorDetails.put("leakedDefects", leakedDefectList);
			}else{
				extractorDetails.put("leakedDefects", new ArrayList<String>());
			}
			if(properties.containsKey("WEIGHTAGE")){
				String weightages = properties.getProperty("WEIGHTAGE");
				List<String> weightageList = Arrays.asList(weightages.split(","));
				extractorDetails.put("weightage", weightageList);
			}else{
				extractorDetails.put("weightage", new ArrayList<String>());
			}
			if(properties.containsKey("NOTIFICATION_EMAIL_IDS")){
				extractorDetails.put("notificationEmailIds", properties.getProperty("NOTIFICATION_EMAIL_IDS"));
			}else{
				extractorDetails.put("notificationEmailIds", "");
			}
			if(properties.containsKey("PROJECT_DELIVERABLE")){
				String projectDeliverables = properties.getProperty("PROJECT_DELIVERABLE").toLowerCase();
				List<String> projectDeliverableList = Arrays.asList(projectDeliverables.split(","));
				extractorDetails.put("projectDeliverables", projectDeliverableList);
			}else{
				extractorDetails.put("projectDeliverables", new ArrayList<String>());
			}
			if(properties.containsKey("VALUE_ADDED")){
				String valueAddeds = properties.getProperty("VALUE_ADDED").toLowerCase();
				List<String> valueAddedList = Arrays.asList(valueAddeds.split(","));
				extractorDetails.put("valueAddeds", valueAddedList);
			}else{
				extractorDetails.put("valueAddeds", new ArrayList<String>());
			}
			if(properties.containsKey("SKILL_DEVELOPMENT")){
				String skillDevelopments = properties.getProperty("SKILL_DEVELOPMENT").toLowerCase();
				List<String> skillDevelopmentList = Arrays.asList(skillDevelopments.split(","));
				extractorDetails.put("skillDevelopments", skillDevelopmentList);
			}else{
				extractorDetails.put("skillDevelopments", new ArrayList<String>());
			}
			if(properties.containsKey("GENERAL_MEETINGS")){
				String generalMeetings = properties.getProperty("GENERAL_MEETINGS").toLowerCase();
				List<String> generalMeetingList = Arrays.asList(generalMeetings.split(","));
				extractorDetails.put("generalMeetings", generalMeetingList);
			}else{
				extractorDetails.put("generalMeetings", new ArrayList<String>());
			}
			if(properties.containsKey("NON_WORKING_HOURS")){
				String nonWorkingHours = properties.getProperty("NON_WORKING_HOURS").toLowerCase();
				List<String> nonWorkingHourList = Arrays.asList(nonWorkingHours.split(","));
				extractorDetails.put("nonWorkingHours", nonWorkingHourList);
			}else{
				extractorDetails.put("nonWorkingHours", new ArrayList<String>());
			}
			if(properties.containsKey("IS_EXTRACTION_MAILING_ENABLED")){
				extractorDetails.put("isMailingEnabled", Boolean.parseBoolean(properties.getProperty("IS_EXTRACTION_MAILING_ENABLED")));
			}else{
				extractorDetails.put("isMailingEnabled", false);
			}
			if(properties.containsKey("EXTRACTION_REPORT_TYPE")){
				extractorDetails.put("extractionReportType", properties.get("EXTRACTION_REPORT_TYPE"));
			}else{
				extractorDetails.put("extractionReportType", "");
			}
			inputStream.close();
			
		}catch(Exception ex){
			log.error("Error while reading data extractor ptoperty file ", ex);
			extractorDetails.put("errorMessage", new Date()+" Error while reading data extractor ptoperty file due to "+ex.getMessage()+", Please check all properties configured properly");
		}finally{
			if(inputStream != null){
				try {
					inputStream.close();
				} catch (Exception ex) {
					log.error("ERROR  ",ex);
				}
			}
		}
		
		return extractorDetails;
	}
	
	@SuppressWarnings("unchecked")
	public void collectionTemplateReader(DataExtractorScheduleMaster dataExtractorScheduleMaster, String collectionType, Workbook extractionReportWorkbook){
		DataExtractorNotification dataExtractorNotification = new DataExtractorNotification();
		dataExtractorNotification.setExtarctionDate(new Date());
		dataExtractorScheduleMaster.setIsStopProcess(true);
		dataExtractorScheduleMaster.setIsExctractorTypeFound(true);
		try{
			log.info("collectionTemplateReader() of DataExtractor");
			if(mapperFile == null || collectionType == null || mapperFile.trim().isEmpty() || collectionType.trim().isEmpty()){
				dataExtractorNotification.setFileFailed(true);
				dataExtractorNotification.getMessageList().add(new Date()+" No iLCM mapper file / sheet name found to perform extarction");
				log.info("No iLCM mapper file / sheet name found to perform extarction");
				extractionNotification(dataExtractorNotification, extractionReportWorkbook, dataExtractorScheduleMaster);
				return;
			}
			
			String customer = dataExtractorScheduleMaster.getCustomer().getCustomerName();
			Integer customerId = dataExtractorScheduleMaster.getCustomer().getCustomerId();
			String extractorType = dataExtractorScheduleMaster.getExtractorType().getExtarctorName();
			String fileLocation = dataExtractorScheduleMaster.getFileLocation();
			String testCentersName = dataExtractorScheduleMaster.getEngagement().getTestFactoryLab().getTestFactoryLabName();
			Integer testCentersId = dataExtractorScheduleMaster.getEngagement().getTestFactoryLab().getTestFactoryLabId();
			String testFactoryName = dataExtractorScheduleMaster.getEngagement().getTestFactoryName();
			Integer testFactoryId = dataExtractorScheduleMaster.getEngagement().getTestFactoryId();
			
			String product = "";
			Integer productId = 0;
			if(dataExtractorScheduleMaster.getProduct() != null && dataExtractorScheduleMaster.getProduct().getProductId() != null){
				product = dataExtractorScheduleMaster.getProduct().getProductName();
				productId = dataExtractorScheduleMaster.getProduct().getProductId();
			}
			
			LinkedHashMap<String, String> collectionDataTemplate = dataSourceScheduleMapperExtractor.readExcelDataMapperTemplate(mapperFile, collectionType, testFactoryName, product, extractorType, dataExtractorNotification);
			
			dataExtractorNotification.setProductName(product);
			dataExtractorNotification.setExtractorType(extractorType);
			dataExtractorNotification.setFilePath(fileLocation);

			String competency = "";
			Integer competencyId = 0;
			if(dataExtractorScheduleMaster.getCompetency() != null && dataExtractorScheduleMaster.getCompetency().getName() != null && 1 != dataExtractorScheduleMaster.getCompetency().getDimensionId()){
				competency = dataExtractorScheduleMaster.getCompetency().getName();
				competencyId = dataExtractorScheduleMaster.getCompetency().getDimensionId();
			}
			
			if(dataExtractorNotification != null && ((dataExtractorNotification.getMessageList() != null && dataExtractorNotification.getMessageList().size() > 0) || dataExtractorNotification.isFileFailed())){
				dataExtractorNotification.setFilePath(mapperFile);
				extractionNotification(dataExtractorNotification, extractionReportWorkbook, dataExtractorScheduleMaster);
				return;
			}
			
			if(collectionDataTemplate.containsKey("isExtractorTypeFound") && "false".equalsIgnoreCase(collectionDataTemplate.get("isExtractorTypeFound"))){
				dataExtractorScheduleMaster.setIsExctractorTypeFound(false);
				return;
			}
			
			if(!collectionDataTemplate.containsKey("File type") || !extractorType.equalsIgnoreCase(collectionDataTemplate.get("File type")) || !collectionDataTemplate.containsKey("Engagement") || !testFactoryName.equalsIgnoreCase(collectionDataTemplate.get("Engagement"))){
				dataExtractorNotification.setConfigurationFailed(true);
				dataExtractorNotification.setProductName(product);
				dataExtractorNotification.setExtractorType(extractorType);
				dataExtractorNotification.getMessageList().add(new Date()+" No mapping available between extractor type - "+extractorType+" and product - "+product+" in mapper file to extract");
				extractionNotification(dataExtractorNotification, extractionReportWorkbook, dataExtractorScheduleMaster);
				log.info("No mapping available between extractor type - "+extractorType+", Engagement - "+ testFactoryName +" and product - "+product+" in mapper file to extract");
				return;
			}else if(((!collectionDataTemplate.containsKey("Product") || collectionDataTemplate.get("Product") == null || collectionDataTemplate.get("Product").trim().isEmpty()) && !product.trim().isEmpty()) || (collectionDataTemplate.containsKey("Product") && collectionDataTemplate.get("Product") != null && !collectionDataTemplate.get("Product").trim().equalsIgnoreCase(product))){
				dataExtractorNotification.setConfigurationFailed(true);
				dataExtractorNotification.setProductName(product);
				dataExtractorNotification.setExtractorType(extractorType);
				dataExtractorNotification.getMessageList().add(new Date()+" No mapping available between extractor type - "+extractorType+", Engagement - "+ testFactoryName +" and product - "+product+" in mapper file to extract");
				extractionNotification(dataExtractorNotification, extractionReportWorkbook, dataExtractorScheduleMaster);
				log.info("No mapping available between extractor type - "+extractorType+", Engagement - "+ testFactoryName +" and product - "+product+" in mapper file to extract");
				return;
			}else if((!collectionDataTemplate.containsKey("Product") || collectionDataTemplate.get("Product") == null || collectionDataTemplate.get("Product").trim().isEmpty()) && product.trim().isEmpty()){
				collectionDataTemplate.put("Product", product);
			}
			
			collectionDataTemplate.put("scheduledCustomer", customer);
			collectionDataTemplate.put("scheduledCustomerId", customerId.toString());
			collectionDataTemplate.put("scheduledProduct", product);
			collectionDataTemplate.put("scheduledProductId", productId.toString());
			collectionDataTemplate.put("scheduledCompetency", competency);
			collectionDataTemplate.put("scheduledCompetencyId", competencyId.toString());
			collectionDataTemplate.put("scheduledTestCentersName", testCentersName);
			collectionDataTemplate.put("scheduledTestCentersId", testCentersId.toString());
			collectionDataTemplate.put("scheduledTestFactoryName", testFactoryName);
			collectionDataTemplate.put("scheduledTestFactoryId", testFactoryId.toString());
			
			if(collectionDataTemplate.containsKey("Product") && product.equalsIgnoreCase(collectionDataTemplate.get("Product"))){
				
				if(collectionDataTemplate.containsKey("File type")){
					if(!extractorType.equalsIgnoreCase(collectionDataTemplate.get("File type"))){
						return;
					}
					List<File> filesToRead = getFilesToRead(fileLocation);
					if(filesToRead == null || filesToRead.size() == 0){
						dataExtractorNotification.setFileFailed(true);
						dataExtractorNotification.setProductName(product);
						dataExtractorNotification.setExtractorType(extractorType);
						dataExtractorNotification.getMessageList().add(new Date()+" No file / updated file found to extarct in location - "+fileLocation);
						dataExtractorNotification.setFilePath(fileLocation);
						extractionNotification(dataExtractorNotification, extractionReportWorkbook, dataExtractorScheduleMaster);
						log.info("No file / updated file found to extarct in location - "+fileLocation);
						return;
					}
					
					List<?> dataCollections = readDataFromSource(collectionDataTemplate, filesToRead, collectionDataTemplate.get("File type"), extractionReportWorkbook, dataExtractorScheduleMaster);
					if("activity_collection".equalsIgnoreCase(collectionType) && dataCollections != null && dataCollections.size() > 0){
						List<ActivityCollection> activityCollections = (List<ActivityCollection>) dataCollections;
						if(activityCollections != null){
							if("Task".equalsIgnoreCase(extractorType) && extractorType.equalsIgnoreCase(collectionDataTemplate.get("File type"))){
								log.info("Number of task data read - "+activityCollections.size());
								for(ActivityCollection activityCollection : activityCollections){
									try{
										ActivityCollection activityCollectionTask = dataSourceExtractorService.getTaskActivityCollectionByName(activityCollection);
										if(activityCollectionTask != null){
											activityCollection.set_id(activityCollectionTask.get_id());
											activityCollection.setCreatedDate(activityCollectionTask.getCreatedDate());
											
											activityCollection.setActivitySizeActual(activityCollectionTask.getActivitySizeActual());
											activityCollection.setActivityType(activityCollectionTask.getActivityType());
											activityCollection.setWeightageType(activityCollectionTask.getActivityType());
											activityCollection.setWeightageUnit(activityCollectionTask.getWeightageUnit());
											activityCollection.setWorkUnitActual(activityCollectionTask.getWorkUnitActual());
											
											activityCollection.setActivityExecutionEffort(activityCollectionTask.getActivityExecutionEffort());
											activityCollection.setActivityReviewEffort1(activityCollectionTask.getActivityReviewEffort1());
											activityCollection.setActivityReviewEffort2(activityCollectionTask.getActivityReviewEffort2());
											activityCollection.setActivityReviewEffort3(activityCollectionTask.getActivityReviewEffort3());
											activityCollection.setActivityReviewEffort4(activityCollectionTask.getActivityReviewEffort4());
											activityCollection.setActivityReviewEffort5(activityCollectionTask.getActivityReviewEffort5());
											
										}else{
											activityCollection.setCreatedDate(new Date());
										}
										activityCollection.setUpdateDate(new Date());
										
										dataSourceExtractorService.saveOrUpdateActivityCollection(activityCollection);
										ActivityCollectionMongo activityCollectionMongo = new ActivityCollectionMongo(activityCollection);
										dataSourceExtractorMongoService.save(activityCollectionMongo, (collectionType).toLowerCase());
									}catch(Exception ex){
										log.error("Error while processing data extraction - ", ex);
										dataExtractorNotification.setDataProcessingFailed(true);
										dataExtractorNotification.getMessageList().add(new Date()+" Error while processing data extraction due to "+ex.getMessage());
									}
								}
							}else if("WATS".equalsIgnoreCase(extractorType) && extractorType.equalsIgnoreCase(collectionDataTemplate.get("File type"))){
								for(ActivityCollection activityCollection : activityCollections){
									try{
										ActivityCollection activityCollectionTask = dataSourceExtractorService.getTaskActivityCollectionByName(activityCollection);
										if(activityCollectionTask != null){
											activityCollectionTask.setActivitySizeActual(activityCollection.getActivitySizeActual());
											activityCollectionTask.setActivityType(activityCollection.getActivityType());
											activityCollectionTask.setWeightageType(activityCollection.getActivityType());
											activityCollectionTask.setWeightageUnit(activityCollection.getWeightageUnit());
											activityCollectionTask.setWorkUnitActual(activityCollection.getWorkUnitActual());
										}else{
											activityCollection.setCreatedDate(new Date());
											activityCollectionTask = activityCollection;
										}
										activityCollectionTask.setUpdateDate(new Date());
										dataSourceExtractorService.saveOrUpdateActivityCollection(activityCollectionTask);
										ActivityCollectionMongo activityCollectionMongo = new ActivityCollectionMongo(activityCollectionTask);
										dataSourceExtractorMongoService.save(activityCollectionMongo, (collectionType).toLowerCase());
									}catch(Exception ex){
										log.error("Error while processing data extraction - ", ex);
										dataExtractorNotification.setDataProcessingFailed(true);
										dataExtractorNotification.getMessageList().add(new Date()+" Error while processing data extraction due to "+ex.getMessage());
									}
								}
							}else if("Effort".equalsIgnoreCase(extractorType) && extractorType.equalsIgnoreCase(collectionDataTemplate.get("File type"))){
								for(ActivityCollection activityCollection : activityCollections){
									try{
										ActivityCollection activityCollectionTask = dataSourceExtractorService.getTaskActivityCollectionByName(activityCollection);
										if(activityCollectionTask != null){
											activityCollectionTask.setActivityExecutionEffort(activityCollection.getActivityExecutionEffort());
											activityCollectionTask.setActivityReviewEffort1(activityCollection.getActivityReviewEffort1());
											activityCollectionTask.setActivityReviewEffort2(activityCollection.getActivityReviewEffort2());
											activityCollectionTask.setActivityReviewEffort3(activityCollection.getActivityReviewEffort3());
											activityCollectionTask.setActivityReviewEffort4(activityCollection.getActivityReviewEffort4());
											activityCollectionTask.setActivityReviewEffort5(activityCollection.getActivityReviewEffort5());
										}else{
											activityCollection.setCreatedDate(new Date());
											activityCollectionTask = activityCollection;
										}
										activityCollectionTask.setUpdateDate(new Date());
										dataSourceExtractorService.saveOrUpdateActivityCollection(activityCollectionTask);
										ActivityCollectionMongo activityCollectionMongo = new ActivityCollectionMongo(activityCollectionTask);
										dataSourceExtractorMongoService.save(activityCollectionMongo, (collectionType).toLowerCase());
									}catch(Exception ex){
										log.error("Error while processing data extraction - ", ex);
										dataExtractorNotification.setDataProcessingFailed(true);
										dataExtractorNotification.getMessageList().add(new Date()+" Error while processing data extraction due to "+ex.getMessage());
									}
								}
							}else if("Test Plan".equalsIgnoreCase(extractorType) && extractorType.equalsIgnoreCase(collectionDataTemplate.get("File type"))){
								for(ActivityCollection activityCollectionWeek : activityCollections){
									try{
										ActivityCollection activityCollection = dataSourceExtractorService.getActivityCollectionOfWeekForProgramAndType(activityCollectionWeek.getTestFactoryName(), activityCollectionWeek.getProductName(), activityCollectionWeek.getWeekDate(), activityCollectionWeek.getType(), null);
										if(activityCollection == null){
											activityCollectionWeek.setCreatedDate(new Date());
											activityCollectionWeek.setUpdateDate(new Date());
										}else{
											activityCollectionWeek.set_id(activityCollection.get_id());
											activityCollectionWeek.setCreatedDate(activityCollection.getCreatedDate());
											activityCollectionWeek.setUpdateDate(new Date());
										}
										if("Test case".equalsIgnoreCase(activityCollectionWeek.getType())){
											activityCollectionWeek.setWeightageUnit(testCaseWeightage);
											activityCollectionWeek.setWorkUnitActual(activityCollectionWeek.getActivitySizeActual() * testCaseWeightage);
											activityCollectionWeek.setWorkUnitPlanned(activityCollectionWeek.getActivitySizePlanned() * testCaseWeightage);
										}else if("Test job / Pairwise".equalsIgnoreCase(activityCollectionWeek.getType())){
											activityCollectionWeek.setWeightageUnit(testJobOrPairwiseWeightage);
											activityCollectionWeek.setWorkUnitActual(activityCollectionWeek.getActivitySizeActual() * testJobOrPairwiseWeightage);
											activityCollectionWeek.setWorkUnitPlanned(activityCollectionWeek.getActivitySizePlanned() * testJobOrPairwiseWeightage);
										}else if("Pairwise".equalsIgnoreCase(activityCollectionWeek.getType())){
											activityCollectionWeek.setWeightageUnit(pairwiseWeightage);
											activityCollectionWeek.setWorkUnitActual(activityCollectionWeek.getActivitySizeActual() * pairwiseWeightage);
											activityCollectionWeek.setWorkUnitPlanned(activityCollectionWeek.getActivitySizePlanned() * pairwiseWeightage);
										}else if("Test job".equalsIgnoreCase(activityCollectionWeek.getType())){
											activityCollectionWeek.setWeightageUnit(testJobWeightage);
											activityCollectionWeek.setWorkUnitActual(activityCollectionWeek.getActivitySizeActual() * testJobWeightage);
											activityCollectionWeek.setWorkUnitPlanned(activityCollectionWeek.getActivitySizePlanned() * testJobWeightage);
										}
										dataSourceExtractorService.saveOrUpdateActivityCollection(activityCollectionWeek);
										ActivityCollectionMongo activityCollectionMongo = new ActivityCollectionMongo(activityCollectionWeek);
										dataSourceExtractorMongoService.save(activityCollectionMongo, (collectionType).toLowerCase());
									}catch(Exception ex){
										log.error("Error while processing data extraction - ", ex);
										dataExtractorNotification.setDataProcessingFailed(true);
										dataExtractorNotification.getMessageList().add(new Date()+" Error while processing data extraction due to "+ex.getMessage());
									}
								}
							}else if("Test Case".equalsIgnoreCase(extractorType) && extractorType.equalsIgnoreCase(collectionDataTemplate.get("File type"))){
								for(ActivityCollection activityCollectionWeek : activityCollections){
									try{
										ActivityCollection activityCollection = dataSourceExtractorService.getActivityCollectionOfWeekForProgramAndType(activityCollectionWeek.getTestFactoryName(), activityCollectionWeek.getProductName(), activityCollectionWeek.getActualActivityStartDate(), "Test case", null);
										if(activityCollection != null){
											Float cumulativeActual = dataSourceExtractorService.getCumulativeActualActivityCollection(activityCollection.getTestFactoryName(), activityCollection.getProductName(), activityCollection.getWeekDate(), "Test case", null);
											activityCollection.setUpdateDate(new Date());
											activityCollection.setActualActivityStartDate(activityCollectionWeek.getActualActivityStartDate());
											activityCollection.setActualActivityEndDate(activityCollectionWeek.getActualActivityEndDate());
											
											if(cumulativeActual != null){
												activityCollection.setActivitySizeActual(activityCollectionWeek.getActivitySizeActual() - cumulativeActual);
												activityCollection.setCumulativeActivityActual(cumulativeActual + activityCollectionWeek.getActivitySizeActual());
											}else{
												activityCollection.setActivitySizeActual(activityCollectionWeek.getActivitySizeActual());
												activityCollection.setCumulativeActivityActual(activityCollectionWeek.getActivitySizeActual());
											}
											activityCollection.setWeightageUnit(testCaseWeightage);
											activityCollection.setWorkUnitActual(activityCollection.getActivitySizeActual() * testCaseWeightage);
											activityCollection.setWorkUnitPlanned(activityCollection.getActivitySizePlanned() * testCaseWeightage);
											
											dataSourceExtractorService.saveOrUpdateActivityCollection(activityCollection);
											ActivityCollectionMongo activityCollectionMongo = new ActivityCollectionMongo(activityCollection);
											dataSourceExtractorMongoService.save(activityCollectionMongo, (collectionType).toLowerCase());
										}
									}catch(Exception ex){
										log.error("Error while processing data extraction - ", ex);
										dataExtractorNotification.setDataProcessingFailed(true);
										dataExtractorNotification.getMessageList().add(new Date()+" Error while processing data extraction due to "+ex.getMessage());
									}
								}
							}else if("Pairwise".equalsIgnoreCase(extractorType) && extractorType.equalsIgnoreCase(collectionDataTemplate.get("File type"))){
								for(ActivityCollection activityCollectionWeek : activityCollections){
									try{
										ActivityCollection activityCollection = dataSourceExtractorService.getActivityCollectionOfWeekForProgramAndType(activityCollectionWeek.getTestFactoryName(), activityCollectionWeek.getProductName(), activityCollectionWeek.getActualActivityStartDate(), "Pairwise", "Test job / Pairwise");
										if(activityCollection != null){
											Float cumulativeActual = dataSourceExtractorService.getCumulativeActualActivityCollection(activityCollection.getTestFactoryName(), activityCollection.getProductName(), activityCollection.getWeekDate(), "Pairwise", "Test job / Pairwise");
											activityCollection.setUpdateDate(new Date());
											activityCollection.setActualActivityStartDate(activityCollectionWeek.getActualActivityStartDate());
											activityCollection.setActivitySizeActual(activityCollectionWeek.getActivitySizeActual());
											if(cumulativeActual != null){
												activityCollection.setCumulativeActivityActual(cumulativeActual + activityCollectionWeek.getActivitySizeActual());											
											}else{
												activityCollection.setCumulativeActivityActual(activityCollectionWeek.getActivitySizeActual());
											}
											activityCollection.setWeightageUnit(pairwiseWeightage);
											activityCollection.setWorkUnitActual(activityCollection.getActivitySizeActual() * pairwiseWeightage);
											activityCollection.setWorkUnitPlanned(activityCollection.getActivitySizePlanned() * pairwiseWeightage);
											
											dataSourceExtractorService.saveOrUpdateActivityCollection(activityCollection);
											ActivityCollectionMongo activityCollectionMongo = new ActivityCollectionMongo(activityCollection);
											dataSourceExtractorMongoService.save(activityCollectionMongo, (collectionType).toLowerCase());
										}
									}catch(Exception ex){
										log.error("Error while processing data extraction - ", ex);
										dataExtractorNotification.setDataProcessingFailed(true);
										dataExtractorNotification.getMessageList().add(new Date()+" Error while processing data extraction due to "+ex.getMessage());
									}
								}
							}else if("Test Job".equalsIgnoreCase(extractorType) && extractorType.equalsIgnoreCase(collectionDataTemplate.get("File type"))){
								for(ActivityCollection activityCollectionWeek : activityCollections){
									try{
										ActivityCollection activityCollection = dataSourceExtractorService.getActivityCollectionOfWeekForProgramAndType(activityCollectionWeek.getTestFactoryName(), activityCollectionWeek.getProductName(), activityCollectionWeek.getActualActivityStartDate(), "Test job", "Test job / Pairwise");
										if(activityCollection != null){
											Float cumulativeActual = dataSourceExtractorService.getCumulativeActualActivityCollection(activityCollection.getTestFactoryName(), activityCollection.getProductName(), activityCollection.getWeekDate(), "Test job", "Test job / Pairwise");
											activityCollection.setUpdateDate(new Date());
											activityCollection.setActualActivityStartDate(activityCollectionWeek.getActualActivityStartDate());
											activityCollection.setActivitySizeActual(activityCollectionWeek.getActivitySizeActual());
											if(cumulativeActual != null){
												activityCollection.setCumulativeActivityActual(cumulativeActual + activityCollectionWeek.getActivitySizeActual());											
											}else{
												activityCollection.setCumulativeActivityActual(activityCollectionWeek.getActivitySizeActual());
											}
											activityCollection.setWeightageUnit(testJobWeightage);
											activityCollection.setWorkUnitActual(activityCollection.getActivitySizeActual() * testJobWeightage);
											activityCollection.setWorkUnitPlanned(activityCollection.getActivitySizePlanned() * testJobWeightage);
											
											dataSourceExtractorService.saveOrUpdateActivityCollection(activityCollection);
											ActivityCollectionMongo activityCollectionMongo = new ActivityCollectionMongo(activityCollection);
											dataSourceExtractorMongoService.save(activityCollectionMongo, (collectionType).toLowerCase());
										}
									}catch(Exception ex){
										log.error("Error while processing data extraction - ", ex);
										dataExtractorNotification.setDataProcessingFailed(true);
										dataExtractorNotification.getMessageList().add(new Date()+" Error while processing data extraction due to "+ex.getMessage());
									}
								}
							}else{
								dataExtractorNotification.setFileFailed(true);
								dataExtractorNotification.setProductName(product);
								dataExtractorNotification.setExtractorType(collectionDataTemplate.get("File type"));
								dataExtractorNotification.getMessageList().add(new Date()+" Mentioned file type for Engagement - "+ testFactoryName +" and product - "+product+" in mapper file's "+collectionType+" sheet("+collectionDataTemplate.get("File type")+") is not matching with extraction type("+extractorType+") selected in iLCM.");
								extractionNotification(dataExtractorNotification, extractionReportWorkbook, dataExtractorScheduleMaster);
								log.info("Mentioned file type for Engagement - "+ testFactoryName +" and product - "+product+" in mapper file's "+collectionType+" sheet("+collectionDataTemplate.get("File type")+") is not matching with extraction type("+extractorType+") selected in iLCM.");
								return;
							}
						}
					
					}else if("defect_collection".equalsIgnoreCase(collectionType) && dataCollections != null && dataCollections.size() > 0){

						List<DefectCollection> defectCollections = (List<DefectCollection>) dataCollections;
						if(defectCollections != null){
							if("Query Log".equalsIgnoreCase(extractorType) && extractorType.equalsIgnoreCase(collectionDataTemplate.get("File type"))){
								
								for(DefectCollection defectCollection : defectCollections){
									try{
										DefectCollection availableDefectCollection = dataSourceExtractorService.getQueryLogByNameNoRaisedDate(defectCollection);
										if(availableDefectCollection != null){
											defectCollection.set_id(availableDefectCollection.get_id());
											defectCollection.setCreatedDate(availableDefectCollection.getCreatedDate());
										}else{
											defectCollection.setCreatedDate(new Date());
										}
										defectCollection.setUpdatedDate(new Date());
										
										dataSourceExtractorService.saveOrUpdateDefectCollection(defectCollection);
										DefectCollectionMongo activityCollectionMongo = new DefectCollectionMongo(defectCollection);
										dataSourceExtractorMongoService.save(activityCollectionMongo, (collectionType).toLowerCase());
									}catch(Exception ex){
										log.error("Error while processing data extraction - ", ex);
										dataExtractorNotification.setDataProcessingFailed(true);
										dataExtractorNotification.getMessageList().add(new Date()+" Error while processing data extraction due to "+ex.getMessage());
									}
								}
							}else if("CQ Dump".equalsIgnoreCase(extractorType) && extractorType.equalsIgnoreCase(collectionDataTemplate.get("File type"))){

								for(DefectCollection defectCollection : defectCollections){
									try{
										DefectCollection defectCollectionAvailability = dataSourceExtractorService.getDefectCollectionByProgramNameDateAndID(defectCollection.getTestFactoryName(), defectCollection.getProductName(), defectCollection.getRaisedDate(), defectCollection.getDefectId(), defectCollection.getVersionName());
										if(defectCollectionAvailability == null){
											defectCollection.setCreatedDate(new Date());
											defectCollection.setUpdatedDate(new Date());
											
											dataSourceExtractorService.saveOrUpdateDefectCollection(defectCollection);

											DefectCollectionMongo defectCollectionMongo = new DefectCollectionMongo(defectCollection);
											dataSourceExtractorMongoService.save(defectCollectionMongo, (collectionType).toLowerCase());
											
											Calendar weekDate = Calendar.getInstance();
											weekDate.setTime(DateUtility.getWeekStart(defectCollection.getRaisedDate()));
											weekDate.set(Calendar.HOUR_OF_DAY, 0);
											weekDate.set(Calendar.MINUTE, 0);
											weekDate.set(Calendar.SECOND, 0);
											weekDate.set(Calendar.MILLISECOND, 0);
											weekDate.add(Calendar.MILLISECOND, weekDate.getTimeZone().getRawOffset());
											CalculatedDefectCollectionMongo calculatedDefectCollectionMongo = dataSourceExtractorMongoService.getCalculatedDefectCollection(defectCollection.getTestFactoryName(), defectCollection.getProductName(), weekDate.getTime(), ("calculated_defect_collection").toLowerCase());
											if(calculatedDefectCollectionMongo == null){
												calculatedDefectCollectionMongo = new CalculatedDefectCollectionMongo();
												calculatedDefectCollectionMongo.setWeekDate(weekDate.getTime());
												calculatedDefectCollectionMongo.setCumulativeTotal(1.0F);
												
												calculatedDefectCollectionMongo.setProductName(defectCollection.getProductName());
												calculatedDefectCollectionMongo.setProductId(defectCollection.getProductId());
												calculatedDefectCollectionMongo.setCompetency(defectCollection.getCompetency());
												calculatedDefectCollectionMongo.setCompetencyId(defectCollection.getCompetencyId());
												calculatedDefectCollectionMongo.setProject(defectCollection.getProject());
												calculatedDefectCollectionMongo.setProjectId(defectCollection.getProjectId());
												calculatedDefectCollectionMongo.setCustomerName(defectCollection.getCustomerName());
												calculatedDefectCollectionMongo.setCustomerId(defectCollection.getCustomerId());
												calculatedDefectCollectionMongo.setTestFactoryName(defectCollection.getTestFactoryName());
												calculatedDefectCollectionMongo.setTestFactoryId(defectCollection.getTestFactoryId());
												calculatedDefectCollectionMongo.setTestCentersName(defectCollection.getTestCentersName());
												calculatedDefectCollectionMongo.setTestCentersId(defectCollection.getTestCentersId());
												
												if(defectCollection.getDriverState() != null && closedDefects.contains(defectCollection.getDriverState().trim().toLowerCase())){
													calculatedDefectCollectionMongo.setCumulativeClosed(1.0F);
												}
												
												if(defectCollection.getInjection() != null && invalidDefects.contains(defectCollection.getInjection().trim().toLowerCase())){
													calculatedDefectCollectionMongo.setCumulativeInvalid(1.0F);
												}
												
												if(defectCollection.getCategory() != null && nonQualityDefects.contains(defectCollection.getCategory().trim().toLowerCase())){
													calculatedDefectCollectionMongo.setCumulativeNonQulityDefects(1.0F);
												}
												
												if(defectCollection.getDetection() != null && leakedDefects.contains(defectCollection.getDetection().trim().toLowerCase())){
													calculatedDefectCollectionMongo.setCumulativeLeakedDefects(1.0F);
												}
											}else{
												Calendar dbWeekDate = Calendar.getInstance();
												dbWeekDate.setTime((Date)calculatedDefectCollectionMongo.getWeekDate());
												CalculatedDefectCollectionMongo calculatedDefectCollectionMongoNew = null;
												if(dbWeekDate.getTimeInMillis() != (weekDate.getTimeInMillis() - weekDate.getTimeZone().getRawOffset())){
													calculatedDefectCollectionMongoNew = new CalculatedDefectCollectionMongo();
													calculatedDefectCollectionMongoNew.setProductName(defectCollection.getProductName());
												}else{
													calculatedDefectCollectionMongoNew = calculatedDefectCollectionMongo;
												}
												calculatedDefectCollectionMongoNew.setWeekDate(weekDate.getTime());
												calculatedDefectCollectionMongoNew.setCumulativeTotal(calculatedDefectCollectionMongo.getCumulativeTotal() + 1);
												
												if(defectCollection.getDriverState() != null && closedDefects.contains(defectCollection.getDriverState().trim().toLowerCase())){
													calculatedDefectCollectionMongoNew.setCumulativeClosed(calculatedDefectCollectionMongo.getCumulativeClosed() + 1);
												}

												if(defectCollection.getInjection() != null && invalidDefects.contains(defectCollection.getInjection().trim().toLowerCase())){
													calculatedDefectCollectionMongoNew.setCumulativeInvalid(calculatedDefectCollectionMongo.getCumulativeInvalid() + 1);
												}

												if(defectCollection.getCategory() != null && nonQualityDefects.contains(defectCollection.getCategory().trim().toLowerCase())){
													calculatedDefectCollectionMongoNew.setCumulativeNonQulityDefects(calculatedDefectCollectionMongo.getCumulativeNonQulityDefects()+1);
												}

												if(defectCollection.getDetection() != null && leakedDefects.contains(defectCollection.getDetection().trim().toLowerCase())){
													calculatedDefectCollectionMongoNew.setCumulativeLeakedDefects(calculatedDefectCollectionMongo.getCumulativeLeakedDefects() + 1);
												}
												calculatedDefectCollectionMongo = calculatedDefectCollectionMongoNew;
											}
											dataSourceExtractorMongoService.save(calculatedDefectCollectionMongo, ("calculated_defect_collection").toLowerCase());
										}
									}catch(Exception ex){
										log.error("Error while processing data extraction - ", ex);
										dataExtractorNotification.setDataProcessingFailed(true);
										dataExtractorNotification.getMessageList().add(new Date()+" Error while processing data extraction due to "+ex.getMessage());
									}
								}
							}else{
								dataExtractorNotification.setFileFailed(true);
								dataExtractorNotification.setProductName(product);
								dataExtractorNotification.setExtractorType(collectionDataTemplate.get("File type"));
								dataExtractorNotification.getMessageList().add(new Date()+" Mentioned file type for Engagement - "+ testFactoryName +" and product - "+product+" in mapper file's "+collectionType+" sheet is not matching with extraction type selected in iLCM.");
								extractionNotification(dataExtractorNotification, extractionReportWorkbook, dataExtractorScheduleMaster);
								log.info("Mentioned file type for Engagement - "+ testFactoryName +" and product - "+product+" in mapper file's "+collectionType+" sheet is not matching with extraction type selected in iLCM.");
								return;
							}
						}
						
					}else if("review_record_collection".equalsIgnoreCase(collectionType) && dataCollections != null && dataCollections.size() > 0){
						List<ReviewRecordCollection> reviewRecordCollections = (List<ReviewRecordCollection>) dataCollections;
						if(reviewRecordCollections != null){
							if("Review Record".equalsIgnoreCase(extractorType) && extractorType.equalsIgnoreCase(collectionDataTemplate.get("File type"))){
								
								for(ReviewRecordCollection reviewRecordCollection : reviewRecordCollections){
									try{
										reviewRecordCollection.setCreatedDate(new Date());
										reviewRecordCollection.setUpdateDate(new Date());
										
										dataSourceExtractorService.saveOrUpdateReviewRecordCollection(reviewRecordCollection);
										ReviewRecordCollectionMongo reviewRecordCollectionMongo = new ReviewRecordCollectionMongo(reviewRecordCollection);
										dataSourceExtractorMongoService.save(reviewRecordCollectionMongo, (collectionType).toLowerCase());
									}catch(Exception ex){
										log.error("Error while processing data extraction - ", ex);
										dataExtractorNotification.setDataProcessingFailed(true);
										dataExtractorNotification.getMessageList().add(new Date()+" Error while processing data extraction due to "+ex.getMessage());
									}
								}
							}else{
								dataExtractorNotification.setFileFailed(true);
								dataExtractorNotification.setProductName(product);
								dataExtractorNotification.setExtractorType(collectionDataTemplate.get("File type"));
								dataExtractorNotification.getMessageList().add(new Date()+" Mentioned file type for Engagement - "+ testFactoryName +" and product - "+product+" in mapper file's "+collectionType+" sheet is not matching with extraction type selected in iLCM.");
								extractionNotification(dataExtractorNotification, extractionReportWorkbook, dataExtractorScheduleMaster);
								log.info("Mentioned file type for Engagement - "+ testFactoryName +" and product - "+product+" in mapper file's "+collectionType+" sheet is not matching with extraction type selected in iLCM.");
								return;
							}							
						}
					}else if("dpa_workbook_collection".equalsIgnoreCase(collectionType) && dataCollections != null && dataCollections.size() > 0){
						List<DPAWorkbookCollection> dpaWorkbookCollections = (List<DPAWorkbookCollection>) dataCollections;
						if(dpaWorkbookCollections != null){
							if("DPA".equalsIgnoreCase(extractorType) && extractorType.equalsIgnoreCase(collectionDataTemplate.get("File type"))){
								
								for(DPAWorkbookCollection dpaWorkbookCollection : dpaWorkbookCollections){
									try{
										DPAWorkbookCollection availableDPDpaWorkbookCollection = dataSourceExtractorService.getDPAWorkbookCollection(dpaWorkbookCollection.getTestFactoryName(), dpaWorkbookCollection.getProductName(), dpaWorkbookCollection.getDpaId());
										if(availableDPDpaWorkbookCollection != null){
											dpaWorkbookCollection.set_id(availableDPDpaWorkbookCollection.get_id());
											dpaWorkbookCollection.setCreatedDate(availableDPDpaWorkbookCollection.getCreatedDate());
										}else{
											dpaWorkbookCollection.setCreatedDate(new Date());
										}
										dpaWorkbookCollection.setUpdateDate(new Date());
										
										dataSourceExtractorService.saveOrUpdateDPAWorkbookCollection(dpaWorkbookCollection);
										DPAWorkbookCollectionMongo dpaWorkbookCollectionMongo = new DPAWorkbookCollectionMongo(dpaWorkbookCollection);
										dataSourceExtractorMongoService.save(dpaWorkbookCollectionMongo, (collectionType).toLowerCase());
									}catch(Exception ex){
										log.error("Error while processing data extraction - ", ex);
										dataExtractorNotification.setDataProcessingFailed(true);
										dataExtractorNotification.getMessageList().add(new Date()+" Error while processing data extraction due to "+ex.getMessage());
									}
								}
							}else{
								dataExtractorNotification.setFileFailed(true);
								dataExtractorNotification.setProductName(product);
								dataExtractorNotification.setExtractorType(collectionDataTemplate.get("File type"));
								dataExtractorNotification.getMessageList().add(new Date()+" Mentioned file type for Engagement - "+ testFactoryName +" and product - "+product+" in mapper file's "+collectionType+" sheet is not matching with extraction type selected in iLCM.");
								extractionNotification(dataExtractorNotification, extractionReportWorkbook, dataExtractorScheduleMaster);
								log.info("Mentioned file type for Engagement - "+ testFactoryName +" and product - "+product+" in mapper file's "+collectionType+" sheet is not matching with extraction type selected in iLCM.");
								return;
							}							
						}
					}else if("utilization_collection".equalsIgnoreCase(collectionType) && dataCollections != null && dataCollections.size() > 0){
						
						List<UtilizationCollection> utilizationCollections = (List<UtilizationCollection>) dataCollections;
						if(utilizationCollections != null){
							if("Utilization".equalsIgnoreCase(collectionDataTemplate.get("File type"))){
								for(UtilizationCollection utilizationCollection : utilizationCollections){
									try{
										utilizationCollection.setCreatedDate(new Date());
										utilizationCollection.setUpdatedDate(new Date());
										
										if(utilizationCollection.getActivityName() != null && projectDeliverables.contains(utilizationCollection.getActivityName().trim().toLowerCase())){
											utilizationCollection.setBucketType("Project Deliverable");
										}else if(utilizationCollection.getActivityName() != null && valueAddeds.contains(utilizationCollection.getActivityName().trim().toLowerCase())){
											utilizationCollection.setBucketType("Value Added");
										}else if(utilizationCollection.getActivityName() != null && skillDevelopments.contains(utilizationCollection.getActivityName().trim().toLowerCase())){
											utilizationCollection.setBucketType("Skill Development");
										}else if(utilizationCollection.getActivityName() != null && generalMeetings.contains(utilizationCollection.getActivityName().trim().toLowerCase())){
											utilizationCollection.setBucketType("General Meetings");
										}else if(utilizationCollection.getActivityName() != null && nonWorkingHours.contains(utilizationCollection.getActivityName().trim().toLowerCase())){
											utilizationCollection.setBucketType("Non-Working Hours");
										}else{
											if(!utilizationCollection.getActivityType().equalsIgnoreCase("Glb")){
												utilizationCollection.setBucketType("Project Deliverable");
											}else{
												utilizationCollection.setBucketType("Others");
											}
										}
										
										dataSourceExtractorService.saveOrUpdateUtilizationCollection(utilizationCollection);
										UtilizationCollectionMongo utilizationCollectionMongo = new UtilizationCollectionMongo(utilizationCollection);
										dataSourceExtractorMongoService.save(utilizationCollectionMongo, (collectionType).toLowerCase());
									}catch(Exception ex){
										log.error("Error while processing data extraction - ", ex);
										dataExtractorNotification.setDataProcessingFailed(true);
										dataExtractorNotification.getMessageList().add(new Date()+" Error while processing data extraction due to "+ex.getMessage());
									}
								}
							}else{
								dataExtractorNotification.setFileFailed(true);
								dataExtractorNotification.setProductName(product);
								dataExtractorNotification.setExtractorType(collectionDataTemplate.get("File type"));
								dataExtractorNotification.getMessageList().add(new Date()+" Mentioned file type for Engagement - "+ testFactoryName +" and product - "+product+" in mapper file's "+collectionType+" sheet is not matching with extraction type selected in iLCM.");
								extractionNotification(dataExtractorNotification, extractionReportWorkbook, dataExtractorScheduleMaster);
								log.info("Mentioned file type for Engagement - "+ testFactoryName +" and product - "+product+" in mapper file's "+collectionType+" sheet is not matching with extraction type selected in iLCM.");
								return;
							}
						}
					}else if("resource_training_collection".equalsIgnoreCase(collectionType) && dataCollections != null && dataCollections.size() > 0){
						
						List<HashMap<String, Object>> traningCollections = (List<HashMap<String, Object>>) dataCollections;
						if(traningCollections != null){
							if("Resource Training".equalsIgnoreCase(collectionDataTemplate.get("File type"))){
								for(HashMap<String, Object> resourceTraining : traningCollections){
									try{
										resourceTraining.put("createdDate", new Date());
										resourceTraining.put("updatedDate", new Date());
										
										dataSourceExtractorMongoService.save(resourceTraining, (collectionType).toLowerCase());
									}catch(Exception ex){
										log.error("Error while processing data extraction - ", ex);
										dataExtractorNotification.setDataProcessingFailed(true);
										dataExtractorNotification.getMessageList().add(new Date()+" Error while processing data extraction due to "+ex.getMessage());
									}
								}
							}else{
								dataExtractorNotification.setFileFailed(true);
								dataExtractorNotification.setProductName(product);
								dataExtractorNotification.setExtractorType(collectionDataTemplate.get("File type"));
								dataExtractorNotification.getMessageList().add(new Date()+" Mentioned file type for Engagement - "+ testFactoryName +" and product - "+product+" in mapper file's "+collectionType+" sheet is not matching with extraction type selected in iLCM.");
								extractionNotification(dataExtractorNotification, extractionReportWorkbook, dataExtractorScheduleMaster);
								log.info("Mentioned file type for Engagement - "+ testFactoryName +" and product - "+product+" in mapper file's "+collectionType+" sheet is not matching with extraction type selected in iLCM.");
								return;
							}
						}
					}else if("training_type_collection".equalsIgnoreCase(collectionType) && dataCollections != null && dataCollections.size() > 0){
						
						List<HashMap<String, Object>> trainingCollections = (List<HashMap<String, Object>>) dataCollections;
						if(trainingCollections != null){
							if("Training Type".equalsIgnoreCase(collectionDataTemplate.get("File type"))){
								for(HashMap<String, Object> trainingType : trainingCollections){
									try{
										JSONObject availableTrainingWithName = dataSourceExtractorMongoService.getTrainingTypeByNameAndCompetency((String)trainingType.get("testFactoryName"), (String)trainingType.get("Type"), (String)trainingType.get("Competency"), collectionType);
										if(availableTrainingWithName == null){
											trainingType.put("createdDate", new Date());
										}else{
											trainingType.put("_id", availableTrainingWithName.getJSONObject("_id").getString("$oid"));
											Date createdDate = convertMongoDBStringToDate(availableTrainingWithName.getString("createdDate"));
											trainingType.put("createdDate", createdDate);
										}
										trainingType.put("updatedDate", new Date());
										dataSourceExtractorMongoService.save(trainingType, (collectionType).toLowerCase());
									}catch(Exception ex){
										log.error("Error while processing data extraction - ", ex);
										dataExtractorNotification.setDataProcessingFailed(true);
										dataExtractorNotification.getMessageList().add(new Date()+" Error while processing data extraction due to "+ex.getMessage());
									}
								}
							}else{
								dataExtractorNotification.setFileFailed(true);
								dataExtractorNotification.setProductName(product);
								dataExtractorNotification.setExtractorType(collectionDataTemplate.get("File type"));
								dataExtractorNotification.getMessageList().add(new Date()+" Mentioned file type for Engagement - "+ testFactoryName +" and product - "+product+" in mapper file's "+collectionType+" sheet is not matching with extraction type selected in iLCM.");
								extractionNotification(dataExtractorNotification, extractionReportWorkbook, dataExtractorScheduleMaster);
								log.info("Mentioned file type for Engagement - "+ testFactoryName +" and product - "+product+" in mapper file's "+collectionType+" sheet is not matching with extraction type selected in iLCM.");
								return;
							}
						}
					}else if("single_value_metric_collection".equalsIgnoreCase(collectionType) && dataCollections != null && dataCollections.size() > 0){
						
						List<HashMap<String, Object>> metricValueCollections = (List<HashMap<String, Object>>) dataCollections;
						if(metricValueCollections != null){
							if("Single Value Metric".equalsIgnoreCase(collectionDataTemplate.get("File type"))){
								for(HashMap<String, Object> metricValue : metricValueCollections){
									try{
										JSONObject availableSingleValueMetric = dataSourceExtractorMongoService.getSingleMetricValueDetails(metricValue, collectionType);
										if(availableSingleValueMetric != null){
											metricValue.put("_id", availableSingleValueMetric.getJSONObject("_id").getString("$oid"));
											metricValue.put("createdDate", availableSingleValueMetric.getString("createdDate"));
										}
										dataSourceExtractorMongoService.save(metricValue, (collectionType).toLowerCase());
									}catch(Exception ex){
										log.error("Error while processing data extraction - ", ex);
										dataExtractorNotification.setDataProcessingFailed(true);
										dataExtractorNotification.getMessageList().add(new Date()+" Error while processing data extraction due to "+ex.getMessage());
									}
								}
							}else{
								dataExtractorNotification.setFileFailed(true);
								dataExtractorNotification.setProductName(product);
								dataExtractorNotification.setExtractorType(collectionDataTemplate.get("File type"));
								dataExtractorNotification.getMessageList().add(new Date()+" Mentioned file type for Engagement - "+ testFactoryName +" and product - "+product+" in mapper file's "+collectionType+" sheet is not matching with extraction type selected in iLCM.");
								extractionNotification(dataExtractorNotification, extractionReportWorkbook, dataExtractorScheduleMaster);
								log.info("Mentioned file type for Engagement - "+ testFactoryName +" and product - "+product+" in mapper file's "+collectionType+" sheet is not matching with extraction type selected in iLCM.");
								return;
							}
						}
					}else if("pmo_collection".equalsIgnoreCase(collectionType) && dataCollections != null && dataCollections.size() > 0){
						
						List<HashMap<String, Object>> pmoCollections = (List<HashMap<String, Object>>) dataCollections;
						if(pmoCollections != null){
							if("PMO".equalsIgnoreCase(collectionDataTemplate.get("File type"))){
								for(HashMap<String, Object> pmoValue : pmoCollections){
									try{
										dataSourceExtractorMongoService.save(pmoValue, (collectionType).toLowerCase());
										Float pmtId = (Float) pmoValue.get("PMT");
										
										List<JSONObject> defectPmoList = 	mongoDBService.getMongoDefectsPmoByPmtId(pmtId);
										
										
										for(JSONObject object : defectPmoList){
											
											Date defectDate = convertMongoDBStringToDate(object.getString("Defect Creation Date/Time (PT)"));
											Date defectPeriod = convertMongoDBStringToDate(object.getString("Defect Period"));
											Date modifiedDate = convertMongoDBStringToDate(object.getString("Modified Date - not used"));

											Date defectFormattedDate = getFormattedDate(defectDate);
											object.put("Defect Creation Date/Time (PT)", setDateForMongoDB(defectFormattedDate)); 
											
											Date defectPeriodFormattedDate = getFormattedDate(defectPeriod);
											object.put("Defect Period", setDateForMongoDB(defectPeriodFormattedDate)); 
											

											Date modifiedFormattedDate = getFormattedDate(modifiedDate);
											object.put("Modified Date - not used", setDateForMongoDB(modifiedFormattedDate)); 
											
											for(Map.Entry<String, Object> entry : pmoValue.entrySet()){
												if(!entry.getKey().equalsIgnoreCase("_id")){
													object.put(entry.getKey(), entry.getValue());
												}
											}
											
											 String _id = object.getString("_id");
											 if(_id != null && _id.length() == 24){
												 object.put("_id", new ObjectId(_id));
											}

											 
											dataSourceExtractorMongoService.save(object, "defectpmo");
										}
										
										
									}catch(Exception ex){
										log.error("Error while processing data extraction - ", ex);
										dataExtractorNotification.setDataProcessingFailed(true);
										dataExtractorNotification.getMessageList().add(new Date()+" Error while processing data extraction due to "+ex.getMessage());
									}
								}
							}else{
								dataExtractorNotification.setFileFailed(true);
								dataExtractorNotification.setProductName(product);
								dataExtractorNotification.setExtractorType(collectionDataTemplate.get("File type"));
								dataExtractorNotification.getMessageList().add(new Date()+" Mentioned file type for Engagement - "+ testFactoryName +" and product - "+product+" in mapper file's "+collectionType+" sheet is not matching with extraction type selected in iLCM.");
								extractionNotification(dataExtractorNotification, extractionReportWorkbook, dataExtractorScheduleMaster);
								log.info("Mentioned file type for Engagement - "+ testFactoryName +" and product - "+product+" in mapper file's "+collectionType+" sheet is not matching with extraction type selected in iLCM.");
								return;
							}
						}
					}else if("cmpm_collection".equalsIgnoreCase(collectionType) && dataCollections != null && dataCollections.size() > 0){
						
						List<HashMap<String, Object>> cmpmCollections = (List<HashMap<String, Object>>) dataCollections;
						
						if(cmpmCollections != null){
							if("CMPM".equalsIgnoreCase(collectionDataTemplate.get("File type"))){
								HashMap<String, Object> cmpmMap = new HashMap<String, Object>();
								for(HashMap<String, Object> cmpmValue : cmpmCollections){
									try{
										dataSourceExtractorMongoService.save(cmpmValue, (collectionType).toLowerCase());
										
										String key = cmpmValue.get("Date")+"~"+cmpmValue.get("PMT ID")+"~"+cmpmValue.get("PMT Title");
										Float efforts = (Float) cmpmValue.get("Total Hours");
										
										if(cmpmMap.containsKey(key)){
											efforts = efforts +(Float) cmpmMap.get(key);
										}
										cmpmMap.put(key, efforts);
										
									}catch(Exception ex){
										log.error("Error while processing data extraction - ", ex);
										dataExtractorNotification.setDataProcessingFailed(true);
										dataExtractorNotification.getMessageList().add(new Date()+" Error while processing data extraction due to "+ex.getMessage());
									}
								}
								 List<HashMap<String, Object>> slaCollection = dataSourceExtractorMongoService.getCumulativeSlaMetricsCollectionFromCmpmAndDefectExtracter(cmpmMap, "Total Construction Effort");
								 for(HashMap<String, Object> sla :slaCollection ){
									 dataSourceExtractorMongoService.save(sla, "sla_metrics_collection");
								 }
								
							}else{
								dataExtractorNotification.setFileFailed(true);
								dataExtractorNotification.setProductName(product);
								dataExtractorNotification.setExtractorType(collectionDataTemplate.get("File type"));
								dataExtractorNotification.getMessageList().add(new Date()+" Mentioned file type for Engagement - "+ testFactoryName +" and product - "+product+" in mapper file's "+collectionType+" sheet is not matching with extraction type selected in iLCM.");
								extractionNotification(dataExtractorNotification, extractionReportWorkbook, dataExtractorScheduleMaster);
								log.info("Mentioned file type for Engagement - "+ testFactoryName +" and product - "+product+" in mapper file's "+collectionType+" sheet is not matching with extraction type selected in iLCM.");
								return;
							}
						}
					}else if("defect_dump_collection".equalsIgnoreCase(collectionType) && dataCollections != null && dataCollections.size() > 0){
						
						List<HashMap<String, Object>> defectDumpCollections = (List<HashMap<String, Object>>) dataCollections;
						
						HashMap<Float, JSONObject> pmoObjectMap = new HashMap<Float, JSONObject>();
						
						
						if(defectDumpCollections != null){
							if("DEFECT DUMP".equalsIgnoreCase(collectionDataTemplate.get("File type"))){
							HashMap<String, Object> defectDump =new HashMap<String, Object>();
							for(HashMap<String, Object> defectValue : defectDumpCollections){
									JSONObject pmoObject = new JSONObject();
									HashMap<String, Object> mapValues = new HashMap<String, Object>();
									try{
										dataSourceExtractorMongoService.save(defectValue, (collectionType).toLowerCase());
									
										String key = defectValue.get("Defect Period")+"~"+defectValue.get("PMT")+"~"+"";
										if(defectDump.containsKey(key)){
											defectDump.put(key, (Float.parseFloat(defectDump.get(key).toString()))+1F);
										}else{
											defectDump.put(key, 1);
										}
										
										Float pmtId = (Float) defectValue.get("PMT");
										
										if (pmoObjectMap.containsKey(pmtId)){
											pmoObject =  pmoObjectMap.get(pmtId);
										}else{
											pmoObject = 	mongoDBService.getMongoPmoByPmtId(pmtId);
											pmoObjectMap.put(pmtId, pmoObject);
										}
										
									if(pmoObject != null){

												Date m9Date = DateUtility.toConvertDate(pmoObject.getString("M9 Date"));
												String m9Value = pmoObject.getString("M9 Date");
												
												Calendar calendar = Calendar.getInstance();
											    calendar.setTime(m9Date);
											    String dateRange[] = m9Value.split("/");
											    Integer  year = 0;
											    
											    if(dateRange[2].length() == 4){
											    	year = Integer.parseInt(dateRange[2]);
											    }else{
											    	year = 2000+Integer.parseInt(dateRange[2]);
											    }
											    int month = calendar.get(Calendar.MONTH);
											    calendar.set(year, month, 1);
											    
											   
											    
											    Iterator<String> keysItr = pmoObject.keys();
												
												while(keysItr.hasNext()){
													String pmoKey = keysItr.next();
											        Object value = pmoObject.get(pmoKey);
											        mapValues.put(pmoKey, value);
												}
												
												for(Map.Entry<String, Object> entry : mapValues.entrySet()){
													if(!entry.getKey().equalsIgnoreCase("_id")){
														defectValue.put(entry.getKey(), entry.getValue());
													}
												}
											
												 defectValue.put("M9 Period", setDateForMongoDB(calendar.getTime())); 
												 
												 Date updatedDate = convertMongoDBStringToDate(defectValue.get("updatedDate").toString());
													Date createdDate = convertMongoDBStringToDate(defectValue.get("createdDate").toString());

													 defectValue.put("updatedDate", setDateForMongoDB(updatedDate)); 
													 defectValue.put("createdDate", setDateForMongoDB(createdDate)); 
													 defectValue.put("_id", defectValue.get("Defect ID").toString());
												 
												 dataSourceExtractorMongoService.saveDefect(defectValue, "defectpmo");
											
									}else{
										dataSourceExtractorMongoService.saveDefect(defectValue, "defectpmo");
									}
										
								}catch(Exception ex){
										log.error("Error while processing data extraction - ", ex);
										dataExtractorNotification.setDataProcessingFailed(true);
										dataExtractorNotification.getMessageList().add(new Date()+" Error while processing data extraction due to "+ex.getMessage());
								}
							}
							
							List<HashMap<String, Object>> slaCollection = dataSourceExtractorMongoService.getCumulativeSlaMetricsCollectionFromCmpmAndDefectExtracter(defectDump, "Total Production Defects");
							for(HashMap<String, Object> sla :slaCollection ){
								dataSourceExtractorMongoService.save(sla, "sla_metrics_collection");
							 }
							
						}else{
								dataExtractorNotification.setFileFailed(true);
								dataExtractorNotification.setProductName(product);
								dataExtractorNotification.setExtractorType(collectionDataTemplate.get("File type"));
								dataExtractorNotification.getMessageList().add(new Date()+" Mentioned file type for Engagement - "+ testFactoryName +" and product - "+product+" in mapper file's "+collectionType+" sheet is not matching with extraction type selected in iLCM.");
								extractionNotification(dataExtractorNotification, extractionReportWorkbook, dataExtractorScheduleMaster);
								log.info("Mentioned file type for Engagement - "+ testFactoryName +" and product - "+product+" in mapper file's "+collectionType+" sheet is not matching with extraction type selected in iLCM.");
								return;
							}
						}
					}else if("jira_defect_dump_collection".equalsIgnoreCase(collectionType) && dataCollections != null && dataCollections.size() > 0){
						
						List<HashMap<String, Object>> jiraDefectCollections = (List<HashMap<String, Object>>) dataCollections;
						
						if(jiraDefectCollections != null){
							if("JIRA DEFECT DUMP".equalsIgnoreCase(collectionDataTemplate.get("File type"))){
								HashMap<String, Object> jiraDefectMap = new HashMap<String, Object>();
								for(HashMap<String, Object> jiraDefectValue : jiraDefectCollections){
									try{
										dataSourceExtractorMongoService.save(jiraDefectValue, (collectionType).toLowerCase());
																				
									}catch(Exception ex){
										log.error("Error while processing data extraction - ", ex);
										dataExtractorNotification.setDataProcessingFailed(true);
										dataExtractorNotification.getMessageList().add(new Date()+" Error while processing data extraction due to "+ex.getMessage());
									}
								}
							}else{
								dataExtractorNotification.setFileFailed(true);
								dataExtractorNotification.setProductName(product);
								dataExtractorNotification.setExtractorType(collectionDataTemplate.get("File type"));
								dataExtractorNotification.getMessageList().add(new Date()+" Mentioned file type for Engagement - "+ testFactoryName +" and product - "+product+" in mapper file's "+collectionType+" sheet is not matching with extraction type selected in iLCM.");
								extractionNotification(dataExtractorNotification, extractionReportWorkbook, dataExtractorScheduleMaster);
								log.info("Mentioned file type for Engagement - "+ testFactoryName +" and product - "+product+" in mapper file's "+collectionType+" sheet is not matching with extraction type selected in iLCM.");
								return;
							}
						}
					}else{
						List<HashMap<String, Object>> collections = (List<HashMap<String, Object>>) dataCollections;
						if(collections != null){
							for(HashMap<String, Object> collectionValue : collections){
								try{
									dataSourceExtractorMongoService.save(collectionValue, (collectionType).toLowerCase());
								}catch(Exception ex){
									log.error("Error while processing data extraction - ", ex);
									dataExtractorNotification.setDataProcessingFailed(true);
									dataExtractorNotification.getMessageList().add(new Date()+" Error while processing data extraction due to "+ex.getMessage());
								}
							}
						}
					}
					
					if(fileLocation != null && !fileLocation.isEmpty()){
						File readFile = filesToRead.get(filesToRead.size() - 1);
						Date lastUpdatedDate = new Date(readFile.lastModified());
						dataSourceExtractorService.saveOrUpdateFileUpdatedStatus(collectionDataTemplate.get("Engagement"), collectionDataTemplate.get("Product"), fileLocation, lastUpdatedDate);
					}
					if(!dataExtractorNotification.isDataProcessingFailed()){
						dataExtractorNotification.setExtractionSuccess(true);
						dataExtractorNotification.getMessageList().add(new Date()+" Data Extraction completed for the files in location - "+fileLocation);
					}
					log.info("Extraction completed successfully for the files in location - "+fileLocation);
					extractionNotification(dataExtractorNotification, extractionReportWorkbook, dataExtractorScheduleMaster);
					return;
					
				}else{
					dataExtractorNotification.setFileFailed(true);
					dataExtractorNotification.setProductName(product);
					dataExtractorNotification.setExtractorType(collectionDataTemplate.get("File type"));
					dataExtractorNotification.getMessageList().add(new Date()+" Mapper file doesn't contain file type column / value for the column for Engagement - "+ testFactoryName +" and product - "+product+", please check the mapper file.");
					extractionNotification(dataExtractorNotification, extractionReportWorkbook, dataExtractorScheduleMaster);
					log.info("Mapper file doesn't contain file type column, please check the mapper file");
					return;
				}
			}else{
				log.info("No mapping available for Engagement - "+ testFactoryName +" and product - "+product+" in mapper file to extract");
				return;
			}
			
		}catch(Exception ex){
			log.error("Error in collectionTemplateReader() of DataSourceExtractorJob", ex); 
			dataExtractorNotification.getMessageList().add(new Date()+" Error in collectionTemplateReader() of DataSourceExtractorJob due to "+ex.getMessage());
		}
	}
	
	private Date getFormattedDate(Date dateValue) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dateValue);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		return cal.getTime();
	}

	@SuppressWarnings("resource")
	private List<Object> readDataFromSource(LinkedHashMap<String, String> dataTemplate, List<File> filesToRead, String fileType, Workbook extractionReportWorkbook, DataExtractorScheduleMaster dataExtractorScheduleMaster) {
		List<Object> dataCollections = new ArrayList<Object>();
		List<?> extractedDataCollections = null;
		FileInputStream fileInputStream = null;
		try{
			log.info("Inside readDataFromSource()");
			if(filesToRead == null){
				return dataCollections;
			}
			
			for(File file : filesToRead){
				if(file.isDirectory()){
					continue;
				}
				dataTemplate.put("fileToRead", file.getAbsolutePath());
				DataExtractorNotification dataExtractorNotification = new DataExtractorNotification();
				
				dataExtractorNotification.setFilePath(file.getAbsolutePath());
				dataExtractorNotification.setProductName(dataTemplate.get("scheduledProduct"));
				dataExtractorNotification.setExtractorType(dataTemplate.get("File type"));
				dataExtractorNotification.setCreatedOrModifiedDate(new Date(file.lastModified()));
				dataExtractorNotification.setExtarctionDate(new Date());
				
				try{
					if(file != null && file.getName().trim().toLowerCase().endsWith("csv")){
						CSVReader csvReader = new CSVReader(new FileReader(file.getAbsoluteFile()));
						List<String[]> csvContentList = csvReader.readAll();
						
						Integer headerRowNumber = Integer.parseInt(dataTemplate.get("Header Row Number")) - 1; 
						List<String> headerCellValueList = null;
						List<String[]> contentRows = new LinkedList<String[]>();
						String utilizationPeriod = "";
						int counter = 0;
						boolean isPeriodRead = false;
						for (String[] content : csvContentList) {
							if (headerRowNumber == counter) {
								headerCellValueList = Arrays.asList(content);
							} else if(counter > headerRowNumber){
								contentRows.add(content);
							}else if("Utilization".equalsIgnoreCase(fileType) && counter < headerRowNumber && !isPeriodRead){
								if("Resourcewise Report".equalsIgnoreCase(content[0])){
									utilizationPeriod = content[1];
									isPeriodRead = true;
								}
							}
							counter++;
						}
						if (headerCellValueList != null && headerCellValueList.size() > 0 && contentRows != null && contentRows.size() > 0) {
							if("Utilization".equalsIgnoreCase(fileType)){
								extractedDataCollections = utilizationIndexScheduleDataExtractor.readUtilizationIndexCSV( dataTemplate, headerCellValueList, contentRows, utilizationPeriod, dataExtractorNotification);
							}else if("Effort".equalsIgnoreCase(fileType)){
								extractedDataCollections = effortScheduleDataExtractor.readPMSmartEffortsCSV(dataTemplate, headerCellValueList, contentRows, dataExtractorNotification);
							}
						}else{
							dataExtractorNotification.setFileFailed(true);
							dataExtractorNotification.getMessageList().add(new Date()+" No header value found please check the Header row number mapped in iLCM mapper file");
						}
						
					}else{
						fileInputStream = new FileInputStream(file);
						Workbook workBook = null;
						if(file != null && file.getName().trim().toLowerCase().endsWith("xlsx")){
							workBook = new XSSFWorkbook(fileInputStream);
							try{
								XSSFFormulaEvaluator.evaluateAllFormulaCells((XSSFWorkbook) workBook);					
							}catch(Exception ex){
								log.error("XSSFFormulaEvaluator ERROR", ex);
							}
						}else if(file != null && file.getName().trim().toLowerCase().endsWith("xlsm")){
							workBook = new XSSFWorkbook(fileInputStream);
							try{
								XSSFFormulaEvaluator.evaluateAllFormulaCells((XSSFWorkbook) workBook);					
							}catch(Exception ex){
								log.error("XSSFFormulaEvaluator ERROR", ex);
							}
						}else if(file != null && file.getName().trim().toLowerCase().endsWith("xls")){
							workBook = new HSSFWorkbook(fileInputStream);
							try{
								HSSFFormulaEvaluator.evaluateAllFormulaCells((HSSFWorkbook) workBook);					
							}catch(Exception ex){
								log.error("HSSFFormulaEvaluator ERROR", ex);
							}
						}
						
						Sheet sheet = null; 
						if(dataTemplate.containsKey("Sheet Name")){
							sheet = workBook.getSheet(dataTemplate.get("Sheet Name"));
						}
						
						if(sheet == null){
							dataExtractorNotification.setFileFailed(true);
							if(dataTemplate.containsKey("Sheet Name")){
								dataExtractorNotification.getMessageList().add(new Date()+" Unable to find sheet with name : "+dataTemplate.get("Sheet Name")+" in file - "+file.getAbsolutePath());
							}else{
								dataExtractorNotification.getMessageList().add(new Date()+" Sheet name not found for file - "+file.getAbsolutePath());
							}
						}else{
							Cell headerCell = null;
							DataFormatter formatter = new DataFormatter();
							ArrayList<String> headerCellValueList = new ArrayList<String>();
							
							int headerColumnNumber = Integer.parseInt(dataTemplate.get("Header Column Number")) - 1;
							int totalColumnsCount = 0;
							String headerValue = "";			
							String[] headerRowNumbers = dataTemplate.get("Header Row Number").split(",");
							for(String headerRowNumberIterator : headerRowNumbers){
								Integer headerRowNumber = (Integer.parseInt(headerRowNumberIterator) - 1);
								Row headerRow = sheet.getRow(headerRowNumber);
								int currentRowColumnCount = sheet.getRow(headerRowNumber).getLastCellNum();
								
								for(int headerCellCounter = headerColumnNumber; headerCellCounter < currentRowColumnCount; headerCellCounter++){
									headerCell = headerRow.getCell(headerCellCounter);
									headerValue = formatter.formatCellValue(headerCell).trim().replaceAll("[\n\r]", "");
									if(headerValue != null && headerValue.endsWith("*")){
										headerValue = headerValue.substring(0, headerValue.length() - 1);
									}
									headerCellValueList.add(headerValue);
								}
								if(currentRowColumnCount > totalColumnsCount){
									totalColumnsCount = currentRowColumnCount;
								}
							}
							
							
							if (headerCellValueList != null && headerCellValueList.size() > 0) {
								if("Task".equalsIgnoreCase(fileType)){
									extractedDataCollections = taskReportScheduleDataExtractor.readTaskReportExcel(dataTemplate, headerCellValueList, sheet, totalColumnsCount, dataExtractorNotification);
								}else if("PM Smart Dump".equalsIgnoreCase(fileType)){
									extractedDataCollections = pmSmartScheduleDataExtractor.readPMSamrtExcel(dataTemplate, headerCellValueList, sheet, totalColumnsCount, dataExtractorNotification);
								}else if("WATS".equalsIgnoreCase(fileType)){
									extractedDataCollections = watsScheduleDataExtractor.readWATSExcel(dataTemplate, headerCellValueList, sheet, totalColumnsCount, dataExtractorNotification);
								}else if("Effort".equalsIgnoreCase(fileType)){
									extractedDataCollections = effortScheduleDataExtractor.readPMSmartEffortsExcel(dataTemplate, headerCellValueList, sheet, totalColumnsCount, dataExtractorNotification);
								}else if("Test Plan".equalsIgnoreCase(fileType)){
									extractedDataCollections = testPlanScheduleDataExtractor.readTestPlanExcel(dataTemplate, headerCellValueList, sheet, totalColumnsCount, dataExtractorNotification);
								}else if("Ap Test Dump".equalsIgnoreCase(fileType)){
									extractedDataCollections = apTestScheduleDataExtractor.readTestCaseExcel(dataTemplate, headerCellValueList, sheet, totalColumnsCount, dataExtractorNotification);
								}else if("Pairwise".equalsIgnoreCase(fileType)){
									extractedDataCollections = pairWiseScheduleDataExtractor.readPairWiseExcel(dataTemplate, headerCellValueList, sheet, totalColumnsCount, dataExtractorNotification);
								}else if("Magen".equalsIgnoreCase(fileType)){
									extractedDataCollections = magenMatricsScheduleDataExtractor.readMagenMatrixExcel(dataTemplate, headerCellValueList, sheet, totalColumnsCount, dataExtractorNotification);
								}else if("Query Log".equalsIgnoreCase(fileType)){
									extractedDataCollections = queryLogScheduleDataExtractor.readQueryLogExcel(dataTemplate, headerCellValueList, sheet, totalColumnsCount, dataExtractorNotification);
								}else if("CQ Dump".equalsIgnoreCase(fileType)){
									extractedDataCollections = cqDumpScheduleDataExtractor.readCQDumpExcel(dataTemplate, headerCellValueList, sheet, totalColumnsCount, dataExtractorNotification);
								}else if("Review Record".equalsIgnoreCase(fileType)){
									extractedDataCollections = reviewRecordScheduleDataExtractor.readReviewRecordExcel(dataTemplate, headerCellValueList, sheet, totalColumnsCount, dataExtractorNotification);
								}else if("DPA".equalsIgnoreCase(fileType)){
									extractedDataCollections = dpaWorkbookScheduleDataExtractor.readDPAWorkbookExcel(dataTemplate, headerCellValueList, sheet, totalColumnsCount, dataExtractorNotification);
								}else if("Resource Training".equalsIgnoreCase(fileType)){
									extractedDataCollections = trainingScheduleDataExtractor.readResourceTrainingExcel(dataTemplate, headerCellValueList, sheet, totalColumnsCount, dataExtractorNotification);
								}else if("Training Type".equalsIgnoreCase(fileType)){
									extractedDataCollections = trainingScheduleDataExtractor.readTrainingTypeExcel(dataTemplate, headerCellValueList, sheet, totalColumnsCount, dataExtractorNotification);
								}else if("Single Value Metric".equalsIgnoreCase(fileType)){
									extractedDataCollections = singleValueMeticScheduleDataExtractor.readSingleValueMeticExcel(dataTemplate, headerCellValueList, sheet, totalColumnsCount, dataExtractorNotification);
								}else if("PMO".equalsIgnoreCase(fileType)){
									extractedDataCollections = pmoScheduleDataExtractor.readPMOMeticExcel(dataTemplate, headerCellValueList, sheet, totalColumnsCount, dataExtractorNotification);
								}else if("CMPM".equalsIgnoreCase(fileType)){
									extractedDataCollections = cmpmScheduleDataExtractor.readCMPMMeticExcel(dataTemplate, headerCellValueList, sheet, totalColumnsCount, dataExtractorNotification);
								}else if("DEFECT DUMP".equalsIgnoreCase(fileType)){
									extractedDataCollections = defectDumpScheduleDataExtractor.readDefectDumpMeticExcel(dataTemplate, headerCellValueList, sheet, totalColumnsCount, dataExtractorNotification);
								}else if("JIRA DEFECT DUMP".equalsIgnoreCase(fileType)){
									extractedDataCollections = jiraDefectDumpScheduleDataExtractor.readJiraDefectDumpMeticExcel(dataTemplate, headerCellValueList, sheet, totalColumnsCount, dataExtractorNotification);
								}else{
									extractedDataCollections = commonDataScheduleDataExtractor.readCommonDataExcel(dataTemplate, headerCellValueList, sheet, totalColumnsCount, dataExtractorNotification);
								}
							}else{
								dataExtractorNotification.setFileFailed(true);
								dataExtractorNotification.getMessageList().add(new Date()+" No header value found please check the Header row number mapped in iLCM mapper file");
							}
						}
						
						fileInputStream.close();
						fileInputStream = null;
					}
					
				}catch(Exception ex){
					dataExtractorNotification.setFileFailed(true);
					dataExtractorNotification.getMessageList().add(new Date()+" Unable to read file - "+dataTemplate.get("fileToRead")+" due to "+ex.getMessage());
					log.error("EXTRACTION ERROR", ex);
				}
				if(extractedDataCollections != null){
					dataCollections.addAll(extractedDataCollections);
				}
				extractionNotification(dataExtractorNotification, extractionReportWorkbook, dataExtractorScheduleMaster);
			}
			
		}catch(Exception ex){
			log.error("EXTRACTION ERROR", ex);
		}finally{
			if(fileInputStream != null){
				try {
					fileInputStream.close();
				} catch (Exception ex) {
					
					log.error("Stream closing error - ", ex);
				}
			}
		}
		return dataCollections;
	}
	
	private Date getLastUpdateDateOfFileLocation(String fileLocation){
		
		Date lastUpdatedDate = null;
		try{
			log.info("Finding last update date of location"+fileLocation);
			
			if(fileLocation != null && !fileLocation.isEmpty()){
				lastUpdatedDate = dataSourceExtractorService.getLastUpdateDateOfFileLocation(fileLocation);
			}
			
		}catch(Exception ex){
			log.error("Error ", ex);
		}
		return lastUpdatedDate;
	}
	
	private List<File> getFilesToRead(String fileLocation){
		File[] filesList = null;
		List<File> filesToRead = new ArrayList<File>();
		try{
			Date lastUpdatedDateOfFileLocation = getLastUpdateDateOfFileLocation(fileLocation);
			File fileDirectory = new File(fileLocation);
			if(lastUpdatedDateOfFileLocation != null){
				if(fileDirectory.isDirectory()){
					lastUpdatedDateOfFileLocation = new Date(lastUpdatedDateOfFileLocation.getTime() + 999);
					FileFilter fileFilter = new AgeFileFilter(lastUpdatedDateOfFileLocation, false);
					filesList = fileDirectory.listFiles(fileFilter);					
				}
			}else{
				if(fileDirectory.isDirectory()){
					filesList = fileDirectory.listFiles();					
				}
			}
			
			if(filesList != null){
				Arrays.sort(filesList, LastModifiedFileComparator.LASTMODIFIED_COMPARATOR);
				for(File fileFromList : filesList){
					if(fileFromList.isFile()){
						filesToRead.add(fileFromList);
					}
				}
			}
		}catch(Exception ex){
			log.error("ERROR  ",ex);
		}
		return filesToRead;
	}
	
	private void extractionNotification(DataExtractorNotification dataExtractorNotification, Workbook extractionReportWorkbook, DataExtractorScheduleMaster dataExtractorScheduleMaster){
		try{
			extractionReport(dataExtractorNotification, extractionReportWorkbook, dataExtractorScheduleMaster);
			if(!isMailingEnabled){
				return;
			}
			if(dataExtractorNotification != null && dataExtractorNotification.getMessageList() != null && !dataExtractorNotification.getMessageList().isEmpty() && dataExtractorNotification.isConfigurationFailed()){
				String contentMessage = "Hi,\n\tData extraction failed due to some configuration problem\n\n";
				String subject = "Data Extraction failure | iLCM";
				for(String errorMessage : dataExtractorNotification.getMessageList()){
					contentMessage += "\t\t"+errorMessage+"\n";
				}
				contentMessage += "\n\tPlease check log files for more details.";
				contentMessage += "\n\nRegards,\niLCM Team";
				emailService.sendEmail(subject, contentMessage, notificationEmailIds, null);
			}else if(dataExtractorNotification != null && dataExtractorNotification.getMessageList() != null && !dataExtractorNotification.getMessageList().isEmpty() && dataExtractorNotification.isExtractionSuccess()){
				String contentMessage = "Hi,\n\tData extraction completed successfully\n\n";
				String subject = "Data Extraction completed | iLCM";
				for(String errorMessage : dataExtractorNotification.getMessageList()){
					contentMessage += "\t\t"+errorMessage+"\n";
				}
				contentMessage += "\n\tPlease check your dashboard for updates.";
				contentMessage += "\n\nRegards,\niLCM Team";
				emailService.sendEmail(subject, contentMessage, notificationEmailIds, null);
			}else if(dataExtractorNotification != null && dataExtractorNotification.getMessageList() != null && !dataExtractorNotification.getMessageList().isEmpty() && dataExtractorNotification.isFileFailed()){
				String contentMessage = "Hi,\n\tData extraction failed while reading a file\n\n";
				String subject = "Data Extraction failure | iLCM";
				for(String errorMessage : dataExtractorNotification.getMessageList()){
					contentMessage += "\t\t"+errorMessage+"\n";
				}
				contentMessage += "\n\tPlease check log files for more details.";
				contentMessage += "\n\nRegards,\niLCM Team";
				emailService.sendEmail(subject, contentMessage, notificationEmailIds, null);
			}else if(dataExtractorNotification != null && dataExtractorNotification.getMessageList() != null && !dataExtractorNotification.getMessageList().isEmpty()){
				String contentMessage = "Hi,\n\tData extraction failed for the following rows/columns of file - "+dataExtractorNotification.getFilePath()+"\n\n";
				String subject = "Data Extraction failure | iLCM";
				for(String errorMessage : dataExtractorNotification.getMessageList()){
					contentMessage += "\t\t"+errorMessage+"\n";
				}
				contentMessage += "\n\tPlease check log files for more details.";
				contentMessage += "\n\nRegards,\niLCM Team";
				emailService.sendEmail(subject, contentMessage, notificationEmailIds, null);
			}
		}catch(Exception ex){
			log.error("Error on processing extration error handler", ex);
		}
	}
	
	private Date convertMongoDBStringToDate(String mongoDBDateString){
		Date convertedDate = null;
		if(mongoDBDateString != null && mongoDBDateString.contains("$date")){
			try {
				SimpleDateFormat dateFormatForMongoDB = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
				convertedDate = dateFormatForMongoDB.parse(mongoDBDateString.split(":\"")[1].split("\"}")[0]);
				convertedDate = setDateForMongoDB(convertedDate);
			} catch (Exception e) {
				log.error("ERROR  ",e);
			}
		}
		return convertedDate;
	}
	
	public Date setDateForMongoDB(Date dateToMongoDB){
		
		Calendar dateToMongoDBCalendar = Calendar.getInstance();
		dateToMongoDBCalendar.setTime(dateToMongoDB);
		dateToMongoDBCalendar.add(Calendar.MILLISECOND, dateToMongoDBCalendar.getTimeZone().getRawOffset());
		
		return dateToMongoDBCalendar.getTime();
	}
	
	private void extractionReport(DataExtractorNotification dataExtractorNotification, Workbook extractionReportWorkbook, DataExtractorScheduleMaster dataExtractorScheduleMaster){
		try{
			if(!dataExtractorNotification.isConfigurationFailed() && !dataExtractorNotification.isFileFailed() && !dataExtractorNotification.isExtractionSuccess()){
				generateExtractionReportSummary(extractionReportWorkbook, dataExtractorNotification, dataExtractorScheduleMaster);
			}
			if(!dataExtractorNotification.isExtractionSuccess() || dataExtractorNotification.isDataProcessingFailed()){
				generateExtractionReportFailedDetails(extractionReportWorkbook, dataExtractorNotification);
			}
		}catch(Exception ex){
			log.error("Exception occured in extractionReport - ", ex);
		}
	}
	
	private void generateExtractionReportSummary(Workbook extractionReportWorkbook, DataExtractorNotification dataExtractorNotification, DataExtractorScheduleMaster dataExtractorScheduleMaster){
		try{
			Sheet summarySheet = extractionReportWorkbook.getSheet("Extraction Report Summary"); 
			if(summarySheet == null){
				summarySheet = extractionReportWorkbook.createSheet("Extraction Report Summary");
				Row headerRow = summarySheet.createRow(0);
				String[] headerRowContents = {"Product Name", "Extractor Type", "File Path / Name", "Created Date", "Extraction Date", "Extracted By", "Total Records", "No. of Successful Records", "No. of Failed Records", "Extraction Status"};
				for(int i = 0; i < headerRowContents.length; i++){
					Cell headerCell = headerRow.createCell(i);
					summarySheet.autoSizeColumn(headerCell.getColumnIndex());
					CellStyle headerCellStyle = getHeaderCellStyle(extractionReportWorkbook);
					headerCell.setCellStyle(headerCellStyle);
					headerCell.setCellValue(headerRowContents[i]);
				}
			}			
			
			Row contentRow = summarySheet.createRow(summarySheet.getLastRowNum() + 1);
			String cellValue = null;
			for(int i = 0; i <= summarySheet.getRow(0).getLastCellNum(); i++){
				Cell contentCell = contentRow.createCell(i);
				summarySheet.autoSizeColumn(contentCell.getColumnIndex());
				if(i == 0){
					contentCell.setCellValue(dataExtractorNotification.getProductName());
				}else if(i == 1){
					contentCell.setCellValue(dataExtractorNotification.getExtractorType());
				}else if(i == 2){
					contentCell.setCellValue(dataExtractorNotification.getFilePath());
				}else if(i == 3){
					CellStyle cellStyle = extractionReportWorkbook.createCellStyle();
					CreationHelper createHelper = extractionReportWorkbook.getCreationHelper();
					cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy HH:mm:ss"));
					if(dataExtractorNotification.getCreatedOrModifiedDate() != null){
						contentCell.setCellValue(dataExtractorNotification.getCreatedOrModifiedDate());
						contentCell.setCellStyle(cellStyle);
					}
				}else if(i == 4){
					CellStyle cellStyle = extractionReportWorkbook.createCellStyle();
					CreationHelper createHelper = extractionReportWorkbook.getCreationHelper();
					cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy HH:mm:ss"));
					if(dataExtractorNotification.getExtarctionDate() != null){
						contentCell.setCellValue(dataExtractorNotification.getExtarctionDate());
						contentCell.setCellStyle(cellStyle);
					}
				}else if(i == 5){
					contentCell.setCellValue("Schedular");
				}else if(i == 6){
					contentCell.setCellValue(dataExtractorNotification.getTotalNumberOfRecords());
				}else if(i == 7){
					contentCell.setCellValue(dataExtractorNotification.getNumberOfValidRecords());
				}else if(i == 8){
					contentCell.setCellValue(dataExtractorNotification.getNumberOfInvalidRecords());
				}else if(i == 9){
					if(dataExtractorNotification.getTotalNumberOfRecords() == dataExtractorNotification.getNumberOfValidRecords()){
						cellValue = "Completed Successfully";
						contentCell.setCellValue(cellValue);
					}else if(dataExtractorNotification.getTotalNumberOfRecords() > dataExtractorNotification.getNumberOfValidRecords() && dataExtractorNotification.getNumberOfValidRecords() > 0){
						cellValue = "Partially Completed";
						contentCell.setCellValue(cellValue);
					}else if(dataExtractorNotification.getNumberOfValidRecords() == 0){
						cellValue = "Failed";
						contentCell.setCellValue(cellValue);
					}
				}
				
			}
			if(dataExtractorNotification != null && dataExtractorScheduleMaster != null){
				createDataExtractionReportSummary(dataExtractorNotification, dataExtractorScheduleMaster, cellValue);
			}
			
		}catch(Exception ex){
			log.error("Exception occured in getExtractionReportSummarySheet - ", ex);
		}
	}

	private void generateExtractionReportFailedDetails(Workbook extractionReportWorkbook, DataExtractorNotification dataExtractorNotification){
		try{
			Sheet failedDetailsSheet = extractionReportWorkbook.getSheet("Failure Details"); 
			if(failedDetailsSheet == null){
				failedDetailsSheet = extractionReportWorkbook.createSheet("Failure Details");
				Row headerRow = failedDetailsSheet.createRow(0);
				String[] headerRowContents = {"Product Name", "Extractor Type", "File Path / Name", "Created Date", "Extraction Date", "Extracted By", "Failure Type", "Failure Description"};
				for(int i = 0; i < headerRowContents.length; i++){
					Cell headerCell = headerRow.createCell(i);
					failedDetailsSheet.autoSizeColumn(headerCell.getColumnIndex());
					CellStyle headerCellStyle = getHeaderCellStyle(extractionReportWorkbook);
					headerCell.setCellStyle(headerCellStyle);
					headerCell.setCellValue(headerRowContents[i]);
				}
			}
			
			for(String message : dataExtractorNotification.getMessageList()){
				Row contentRow = failedDetailsSheet.createRow(failedDetailsSheet.getLastRowNum() + 1);
				for(int i = 0; i <= failedDetailsSheet.getRow(0).getLastCellNum(); i++){
					Cell contentCell = contentRow.createCell(i);
					failedDetailsSheet.autoSizeColumn(contentCell.getColumnIndex());
					if(i == 0){
						contentCell.setCellValue(dataExtractorNotification.getProductName());
					}else if(i == 1){
						contentCell.setCellValue(dataExtractorNotification.getExtractorType());
					}else if(i == 2){
						contentCell.setCellValue(dataExtractorNotification.getFilePath());
					}else if(i == 3){
						CellStyle cellStyle = extractionReportWorkbook.createCellStyle();
						CreationHelper createHelper = extractionReportWorkbook.getCreationHelper();
						cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy HH:mm:ss"));
						if(dataExtractorNotification.getCreatedOrModifiedDate() != null){
							contentCell.setCellValue(dataExtractorNotification.getCreatedOrModifiedDate());
							contentCell.setCellStyle(cellStyle);
						}
					}else if(i == 4){
						CellStyle cellStyle = extractionReportWorkbook.createCellStyle();
						CreationHelper createHelper = extractionReportWorkbook.getCreationHelper();
						cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy HH:mm:ss"));
						if(dataExtractorNotification.getExtarctionDate() != null){
							contentCell.setCellValue(dataExtractorNotification.getExtarctionDate());
							contentCell.setCellStyle(cellStyle);
						}
					}else if(i == 5){
						contentCell.setCellValue("Schedular");
					}else if(i == 6){
						if(dataExtractorNotification.isConfigurationFailed()){
							contentCell.setCellValue("Configuration Failure");
						}else if(dataExtractorNotification.isFileFailed()){
							contentCell.setCellValue("File Failure");
						}else if(!dataExtractorNotification.isExtractionSuccess()){
							contentCell.setCellValue("Extraction Failure");
						}
					}else if(i == 7){
						contentCell.setCellValue(message);
					}
				}
			}
			
		}catch(Exception ex){
			log.error("ERROR  ",ex);
		}
	}
	
	@SuppressWarnings("static-access")
	private CellStyle getHeaderCellStyle(Workbook extractionReportWorkbook) {
		CellStyle headerCellStyle = extractionReportWorkbook.createCellStyle();
		try{
			headerCellStyle.setAlignment(headerCellStyle.ALIGN_CENTER);
			Font headerFont = extractionReportWorkbook.createFont();
			headerFont.setBoldweight(headerFont.BOLDWEIGHT_BOLD);
			headerFont.setItalic(true);
			headerCellStyle.setFont(headerFont);
		}catch(Exception ex){
			log.error("Exception occured in getHeaderCellStyle - ", ex);
		}
		return headerCellStyle;
	}
	
	private void writeReportFile(Workbook extractionReportWorkbook, String reportLocation, String reportName, DataExtractorScheduleMaster dataExtractorScheduleMaster){
		try{
			File filePath = new File(reportLocation);
			if(filePath == null || !filePath.exists() || !filePath.isDirectory()){
				filePath.mkdirs();
			}
			filePath = new File(reportLocation+reportName);
			FileOutputStream fos = new FileOutputStream(filePath);
			extractionReportWorkbook.write(fos);
			if(dataExtractorScheduleMaster != null){
				createAttachment(dataExtractorScheduleMaster, reportName, reportLocation);
			}
		}catch(Exception ex){
			log.error("Exception occured in writeReportFile - ", ex);
		}
	}
	
	private void createAttachment(DataExtractorScheduleMaster dataExtractorScheduleMaster, String reportName, String reportLocation){
		Attachment attachment = new Attachment();
		attachment.setProduct(dataExtractorScheduleMaster.getProduct());
		attachment.setEntityId(dataExtractorScheduleMaster.getId());
		EntityMaster entityMaster = new EntityMaster();
		entityMaster.setEntitymasterid(IDPAConstants.ENTITY_DATA_EXTRACTOR_ID);
		attachment.setEntityMaster(entityMaster);
		attachment.setAttachmentType("ExtractionReport");
		attachment.setDescription("DataExtractor");
		String ext = reportName.substring(reportName.lastIndexOf(".")).toLowerCase();
		attachment.setAttributeFileExtension(ext);
		String fileName = reportName.substring(0, reportName.lastIndexOf("."));
		attachment.setAttachmentName(fileName);
		attachment.setAttributeFileName(fileName);
		attachment.setAttributeFileURI(reportLocation+reportName);
		UserList userList = new UserList();
		userList.setUserId(1);
		attachment.setCreatedBy(userList);
		attachment.setModifiedBy(userList);
		attachment.setUploadedDate(new Date());
		attachment.setModifiedDate(new Date());
		attachment.setLastModifiedDate(new Date());
		attachment.setStatus(1);
		File file = new File(reportLocation+reportName);
		if(file != null && file.exists() && file.isFile()){
			Long size = file.length();
			String fileSize = "0 MB";
			if(size > 0){
				fileSize = String.format("%.2f", ((size.floatValue() / 1024) / 1024))+" MB";
			}
			attachment.setAttributeFileSize(fileSize);
		}
		List<Attachment> existingAttachments = commonService.getAttachmentOfEntityOrInstance(IDPAConstants.ENTITY_DATA_EXTRACTOR_ID, dataExtractorScheduleMaster.getId(), fileName, userList.getUserId(), null, null);
		if(existingAttachments != null && existingAttachments.size() > 0){
			return;
		}
		commonService.addAttachment(attachment);
	}
	
	private void createDataExtractionReportSummary(DataExtractorNotification dataExtractorNotification, DataExtractorScheduleMaster dataExtractorScheduleMaster, String status){
		DataExtractionReportSummary dataExtractionReportSummary = new DataExtractionReportSummary();
		dataExtractionReportSummary.setFileName(dataExtractorNotification.getFilePath());
		dataExtractionReportSummary.setProductId(dataExtractorScheduleMaster.getProduct());
		dataExtractionReportSummary.setExtractorTypeId(dataExtractorScheduleMaster.getExtractorType());
		dataExtractionReportSummary.setExtractorScheduleId(dataExtractorScheduleMaster);
		dataExtractionReportSummary.setCreatedDate(dataExtractorScheduleMaster.getCreatedDate());
		dataExtractionReportSummary.setExtractionDate(dataExtractorNotification.getExtarctionDate());
		dataExtractionReportSummary.setExtractedBy(dataExtractorScheduleMaster.getExtractorName());
		dataExtractionReportSummary.setTotalRecords(dataExtractorNotification.getTotalNumberOfRecords());
		dataExtractionReportSummary.setSuccessfulRecords(dataExtractorNotification.getNumberOfValidRecords()); 
		dataExtractionReportSummary.setFailedRecords(dataExtractorNotification.getNumberOfInvalidRecords());
		dataExtractionReportSummary.setExtractionStatus(status);
		dataSourceExtractorService.addDataExtractionReportSummary(dataExtractionReportSummary);
	}
}
