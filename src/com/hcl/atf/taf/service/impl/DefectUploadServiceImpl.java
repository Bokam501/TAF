package com.hcl.atf.taf.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetFeed;
import com.google.gdata.util.ServiceException;
import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.dao.DefectUploadDAO;
import com.hcl.atf.taf.dao.UserListDAO;
import com.hcl.atf.taf.dao.WorkPackageDAO;
import com.hcl.atf.taf.model.DefectTypeMaster;
import com.hcl.atf.taf.model.ExecutionPriority;
import com.hcl.atf.taf.model.TestExecutionResultBugList;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.WorkFlow;
import com.hcl.atf.taf.model.WorkPackage;
import com.hcl.atf.taf.model.WorkPackageTestCaseExecutionPlan;
import com.hcl.atf.taf.model.dto.TestCaseDTO;
import com.hcl.atf.taf.model.json.JsonWorkPackageTestCaseExecutionPlanForTester;
import com.hcl.atf.taf.service.DefectUploadService;
import com.hcl.atf.taf.service.ProductListService;
import com.hcl.atf.taf.service.WorkPackageService;

@Service
public class DefectUploadServiceImpl implements DefectUploadService {

	private static final Log log = LogFactory.getLog(DefectUploadServiceImpl.class);
	
	@Autowired
	private WorkPackageService workPackageService;
	@Autowired
	private ProductListService productListService; 
	@Autowired
	private WorkPackageDAO workPackageDAO;
	@Autowired
	private DefectUploadDAO defectUploadDAO;
	@Autowired
	private UserListDAO userListDAO;

	
	@Value("#{ilcmProps['SPREADHSEET_SERVICE']}")
    private String spreadsheet_Service;
	
	@Value("#{ilcmProps['CLIENT_ID']}")
    private String client_Id;
	
	@Value("#{ilcmProps['CLIENT_SECRET']}")
    private String client_Secret;
	
	@Value("#{ilcmProps['REDIRECT_URI']}")
    private String redirect_Uri;
	
	@Value("#{ilcmProps['SPREADSHEET_DEFECTS_NAME']}")
    private String spreadsheet_Defects_Name;
	
	@Autowired
	@Qualifier("ilcmProps")
	private Properties defectProperty;
	
	@Value("#{ilcmProps['DEFECT_BUGNIZER_DATA_IMPORT_SIZE']}")
    private String defectBugnizerSize;
	
	@Override
	@Transactional
	public String exportToGetAuthKey() {
		String authorizationUrl = null;
        String[] SCOPESArray = {"https://spreadsheets.google.com/feeds"};
        final List SCOPES = Arrays.asList(SCOPESArray);
        authorizationUrl = new GoogleAuthorizationCodeRequestUrl(client_Id, redirect_Uri, SCOPES).build();
    
	return authorizationUrl;
	}
	
	@Override
	@Transactional
	public Integer exportToGoogleDriveDefects(String code, Integer productId, Integer productVersionId, Integer productBuildId, Integer workPackageId, Integer approveStatus
			, Date startDate, Date endDate) {
		
        final SpreadsheetService service = new SpreadsheetService(spreadsheet_Service);
        
        HttpTransport httpTransport = new NetHttpTransport();
        JacksonFactory jsonFactory = new JacksonFactory();
        GoogleCredential credential;
        String liveString = DateUtility.dateformatWithDateMonth(new Date())+"-Live";
        String webString = DateUtility.dateformatWithDateMonth(new Date())+"-Web";
        String[] fileNameArray = {liveString,webString};
        CellFeed cellFeed = null;
        URL listFeedUrlLive = null;
        URL listFeedUrlWeb = null;
        URL listFeedUrl = null;
        WorksheetEntry newworksheetLive = null;
        
        try {          
           // Step 2: Exchange!
            GoogleTokenResponse response = new GoogleAuthorizationCodeTokenRequest(httpTransport, jsonFactory, client_Id, client_Secret, code, redirect_Uri).execute();
            // Let's build our GoogleCredential now.
            credential = new GoogleCredential.Builder().setClientSecrets(client_Id, client_Secret).setTransport(httpTransport).setJsonFactory(jsonFactory).build()
                    .setAccessToken(response.getAccessToken())
                    .setRefreshToken(response.getRefreshToken());
            service.setOAuth2Credentials(credential);
            
	        // Define the URL to request.  This should never change.
	        URL SPREADSHEET_FEED_URL = new URL("https://spreadsheets.google.com/feeds/spreadsheets/private/full");

	        // Make a request to the API and get all spreadsheets.
	        SpreadsheetFeed feed = service.getFeed(SPREADSHEET_FEED_URL, SpreadsheetFeed.class);
	        List<SpreadsheetEntry> spreadsheets = feed.getEntries();
	        
	        SpreadsheetEntry spreadsheet = spreadsheets.get(getSpreadSheetIndx (spreadsheets, spreadsheet_Defects_Name));
	        log.info(" Spreadsheet Name : "+spreadsheet.getTitle().getPlainText());
	        URL worksheetFeedUrl = spreadsheet.getWorksheetFeedUrl();
	        WorksheetEntry worksheet = new WorksheetEntry();
	        
	        WorksheetFeed worksheetFeed = service.getFeed(spreadsheet.getWorksheetFeedUrl(), WorksheetFeed.class);
	        List<WorksheetEntry> worksheets = worksheetFeed.getEntries();
	        
	        for(int counter=0; counter<fileNameArray.length; counter++){
	 	       for (WorksheetEntry worksheetEntry : worksheets) {
	        		if(worksheetEntry.getTitle().getPlainText().equals(fileNameArray[counter])){
	        			worksheetEntry.delete();
	 	        	}
	 	       }
	 	        
	        		//Create New Work sheet
	        		worksheet.setTitle(new PlainTextConstruct(fileNameArray[counter]));
			        worksheet.setColCount(50);
			        worksheet.setRowCount(100);
			        newworksheetLive = service.insert(worksheetFeedUrl, worksheet);
		        
			        URL cellFeedUrlLive = newworksheetLive.getCellFeedUrl();
			        cellFeed = new CellFeed();
			        cellFeed = service.getFeed(cellFeedUrlLive, CellFeed.class);
			        	        
			        CellEntry cellEntry = new CellEntry(1, 1, "Sl. No.");
			        cellFeed.insert(cellEntry);
			        cellEntry = new CellEntry(1, 2, "NAME of Tester");
			        cellFeed.insert(cellEntry);
			        cellEntry = new CellEntry(1, 3, "Verified on Device Name Tester Name");
			        cellFeed.insert(cellEntry);
			        cellEntry = new CellEntry(1, 4, "Bug Verification Time");
			        cellFeed.insert(cellEntry);
			        cellEntry = new CellEntry(1, 5, "Bug File Time");
			        cellFeed.insert(cellEntry);
			        cellEntry = new CellEntry(1, 6, "Buganizer ID");
			        cellFeed.insert(cellEntry);
			        cellEntry = new CellEntry(1, 7, "Test Plan");
			        cellFeed.insert(cellEntry);
			        cellEntry = new CellEntry(1, 8, "Area");
			        cellFeed.insert(cellEntry);
			        cellEntry = new CellEntry(1, 9, "Bug Title This is just a one liner about the issue");
			        cellFeed.insert(cellEntry);
			        cellEntry = new CellEntry(1, 10, "Priority");
			        cellFeed.insert(cellEntry);
			        cellEntry = new CellEntry(1, 11, "Sync Up update");
			        cellFeed.insert(cellEntry);
			        cellEntry = new CellEntry(1, 12, "Severity");
			        cellFeed.insert(cellEntry);
			        cellEntry = new CellEntry(1, 13, "Description strictly provide all information mentioned in template click here for template");
			        cellFeed.insert(cellEntry);
			        cellEntry = new CellEntry(1, 14, "Is is repro on Live");
			        cellFeed.insert(cellEntry);
			        cellEntry = new CellEntry(1, 15, "Has it failed in Yesterdays WebRelease");
			        cellFeed.insert(cellEntry);
			        cellEntry = new CellEntry(1, 16, "Is there a bug in Buganizer already for this If yes provide the buganizer ID");
			        cellFeed.insert(cellEntry);
			        cellEntry = new CellEntry(1, 17, "Onsite Comments");
			        cellFeed.insert(cellEntry);
			        
			        listFeedUrl = newworksheetLive.getListFeedUrl();
	 	       	
	 	       	if (fileNameArray[counter].contains(liveString)){
		 	      listFeedUrlLive =  listFeedUrl;
		 	    }else{
		 	      listFeedUrlWeb =  listFeedUrl;
		 	    }
	 	       
	        }
	        
	        List<TestExecutionResultBugList> defectList=null;
	        int indexLive = 1;
			int indexWeb = 1;
			Integer counterforUpload = 0;
			Integer totalDefectsCount = 0;
	        defectList	= workPackageService.listDefectsByTestcaseExecutionPlanIdByApprovedStatus(productId, productVersionId,productBuildId,workPackageId,approveStatus,startDate, endDate,null, null, null);
	        if (defectList != null && defectList.size() > 0){
	        totalDefectsCount = defectList.size();
				for(TestExecutionResultBugList defect: defectList){
						ListEntry row = new ListEntry();
						
					if(!defect.getUploadFlag().equals(1)){
						row.getCustomElements().setValueLocal("NAMEofTester",defect.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getTester().getLoginId());
						if(defect.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration() != null){
							row.getCustomElements().setValueLocal("VerifiedonDeviceNameTesterName",
									defect.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration().getRunconfigName()+" / "+defect.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getTester().getLoginId());
						}
						if(defect.getApprovedOn() != null){
							row.getCustomElements().setValueLocal("BugVerificationTime",defect.getApprovedOn().toString());
						}
						if(defect.getBugCreationTime() != null){
							row.getCustomElements().setValueLocal("BugFileTime",defect.getBugCreationTime().toString());
						}
						
						row.getCustomElements().setValueLocal("BuganizerID","");
						if(defect.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getFeature() != null){
							row.getCustomElements().setValueLocal("TestPlan",defect.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getFeature().getProductFeatureName());
							if(defect.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getFeature().getParentFeature() != null){
								row.getCustomElements().setValueLocal("Area",defect.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getFeature().getParentFeature().getProductFeatureName());
							}
						}
						if(defect.getBugTitle() != null){
							row.getCustomElements().setValueLocal("BugTitleThisisjustaonelinerabouttheissue",defect.getBugTitle());
						}
						if (defect.getApproversPriority() != null){
							row.getCustomElements().setValueLocal("Priority",defect.getApproversPriority().getExecutionPriorityName());
						}
						if (defect.getApprovalRemarks() != null){
							row.getCustomElements().setValueLocal("SyncUpupdate",defect.getApprovalRemarks());
						}
						if(defect.getDefectSeverity() != null){
							row.getCustomElements().setValueLocal("Severity",defect.getDefectSeverity().getSeverityName());
						}
						if(defect.getBugDescription() != null){
							row.getCustomElements().setValueLocal("Descriptionstrictlyprovideallinformationmentionedintemplateclickherefortemplate",defect.getBugDescription());
						}
						if (defect.getIsReproducableOnLive() != null){
							row.getCustomElements().setValueLocal("IsisreproonLive","");
						}
						if(defect.getWasOnPrevDayWebRelease() != null){
							row.getCustomElements().setValueLocal("HasitfailedinYesterdaysWebRelease","");
						}
						if (defect.getIsThereABugAlready() != null){
							row.getCustomElements().setValueLocal("IsthereabuginBuganizeralreadyforthisIfyesprovidethebuganizerID","");
						}
						if (defect.getOnsiteComments() != null){
							row.getCustomElements().setValueLocal("OnsiteComments",defect.getOnsiteComments());
						}
						
						if (defect.getDefectFoundStage().getStageId().equals(2)){
							row.getCustomElements().setValueLocal("Sl.No.",String.valueOf(indexLive++));
							row = service.insert(listFeedUrlLive, row);
						}else{
							row.getCustomElements().setValueLocal("Sl.No.",String.valueOf(indexWeb++));
							row = service.insert(listFeedUrlWeb, row);
						}
						defect.setUploadFlag(1);	
						defectUploadDAO.updateDefectUPloadFlag(defect);
					}else{
						counterforUpload++;
					}
				}
					if (totalDefectsCount.equals(counterforUpload)){
						return 2;
					}
	        } else {
		    	return 3;
		    }

		log.info(" Export to Google Drive is Completed ");	
		 } catch(IOException  e){
			 log.error(" IO Exception -->>> "+e);
			 return -1;
		 } catch(ServiceException  se){
			 log.error(" Service Exception -->>> "+se);
			 return -1;
		 }catch(Exception ex){
			 log.error(" Exception -->>> "+ex);
			 return -1;
		 }
    
	return 1;
	}
	
	
	@Override
	@Transactional
	public boolean exportToGoogleDriveTestResults(Integer workPackageId, String code) {
		
        final SpreadsheetService service = new SpreadsheetService(spreadsheet_Service);
        
        
        HttpTransport httpTransport = new NetHttpTransport();
        JacksonFactory jsonFactory = new JacksonFactory();
        GoogleCredential credential;
        
        CellFeed cellFeed = null;
        WorksheetEntry newworksheetLive = null;
        try {
        	
        	String wpName = defectProperty.getProperty(getworkPackageName(workPackageId).toUpperCase()+"_WORKPACKAGE_NAME");
	        log.info("Work Package Name is ---->  "+wpName);
	        
	        String workString = DateUtility.dateformatWithDateMonth(new Date())+"-"+defectProperty.getProperty(wpName+"_WORKSHEET_NAME");
	       
           // Step 2: Exchange!
            GoogleTokenResponse response = new GoogleAuthorizationCodeTokenRequest(httpTransport, jsonFactory, client_Id, client_Secret, code, redirect_Uri).execute();
            // Let's build our GoogleCredential now.
            credential = new GoogleCredential.Builder().setClientSecrets(client_Id, client_Secret).setTransport(httpTransport).setJsonFactory(jsonFactory).build()
                    .setAccessToken(response.getAccessToken())
                    .setRefreshToken(response.getRefreshToken());
            service.setOAuth2Credentials(credential);
            
	        // Define the URL to request.  This should never change.
	        URL SPREADSHEET_FEED_URL = new URL("https://spreadsheets.google.com/feeds/spreadsheets/private/full");

	        // Make a request to the API and get all spreadsheets.
	        SpreadsheetFeed feed = service.getFeed(SPREADSHEET_FEED_URL, SpreadsheetFeed.class);
	        List<SpreadsheetEntry> spreadsheets = feed.getEntries();
	        
	        SpreadsheetEntry spreadsheet = spreadsheets.get(getSpreadSheetIndx (spreadsheets, defectProperty.getProperty(wpName+"_TARGET_TEMPLATE_FILE")));
	        log.info(" Spreadsheet Name : "+spreadsheet.getTitle().getPlainText());
	        URL worksheetFeedUrl = spreadsheet.getWorksheetFeedUrl();
	        WorksheetEntry worksheet = new WorksheetEntry();
	        
	        WorksheetFeed worksheetFeed = service.getFeed(spreadsheet.getWorksheetFeedUrl(), WorksheetFeed.class);
	        List<WorksheetEntry> worksheets = worksheetFeed.getEntries();
	        
 	       	for (WorksheetEntry worksheetEntry : worksheets) {
 	        		if(worksheetEntry.getTitle().getPlainText().equals(workString)){
 	        			worksheetEntry.delete();
	 	        	}
 	        }
 	       	worksheet.setTitle(new PlainTextConstruct(workString));
	        worksheet.setColCount(100);
	        worksheet.setRowCount(300);
	        newworksheetLive = service.insert(worksheetFeedUrl, worksheet);
	        
	        URL cellFeedUrlLive = newworksheetLive.getCellFeedUrl();
	        cellFeed = new CellFeed();
	        cellFeed = service.getFeed(cellFeedUrlLive, CellFeed.class);
	        
 	       	FileInputStream file = new FileInputStream(new File(defectProperty.getProperty(wpName+"_SOURCE_TEMPLATE_FILE")));
	        //Get the workbook instance for XLS file 
	        XSSFWorkbook workbook = new XSSFWorkbook(file);
	        XSSFSheet sheet = workbook.getSheetAt(0);
	        //Iterate through each rows from first sheet
	        Iterator<Row> rowIterator = sheet.iterator();
	        CellEntry cellEntry = null;
	        
	        List<WorkPackageTestCaseExecutionPlan> wptcplanListfor=workPackageService.listWorkPackageTestCasesExecutionPlanBywpId(workPackageId,"ProductFeature");
	        Map<String,Map<String,String>> mapfromiLCM = getExecutionDetails(wptcplanListfor);
        	ArrayList<String> envCombNamesfromTemplate = new ArrayList<String>();
        	int useCaseIdIndex = 0;
            int sfNotesIndex = 0;
	        while(rowIterator.hasNext()) {
	        	String testcaseCodefromTemplate = "";
	            Row row = rowIterator.next();
	            //For each row, iterate through each columns
	            Iterator<Cell> cellIterator = row.cellIterator();
	            int cellCountEnvComb=0;
	            Map<String,String> envMapfromiLCM = new HashMap<String,String>(); 
	            while(cellIterator.hasNext()) {
	            	Cell cell = cellIterator.next();
	            	cell.setCellType(Cell.CELL_TYPE_STRING);
	            	if(row.getRowNum() == 0){
	            		if(isRequiredColumnNameAvailable(cell, defectProperty.getProperty(wpName+"_USE_CASE_CODE_IDENTIFIER"))){
		            		useCaseIdIndex = getCellIndexByColumnName(cell, defectProperty.getProperty(wpName+"_USE_CASE_CODE_IDENTIFIER"));
		            	}
	            		if(isRequiredColumnNameAvailable(cell, defectProperty.getProperty(wpName+"_ENVIRONMENT_COMBINATION_LOCATOR"))){
	            			sfNotesIndex = getCellIndexByColumnName(cell, defectProperty.getProperty(wpName+"_ENVIRONMENT_COMBINATION_LOCATOR"));
		            	}
            		}
	                if (row.getRowNum() > 0 && cell.getStringCellValue().length() > 0 && cell.getColumnIndex() == useCaseIdIndex) { // To filter column headings
	                	testcaseCodefromTemplate = cell.getStringCellValue().toString().trim();
	                }
	                if (row.getRowNum() == 0 && cell.getStringCellValue().length() > 0 && cell.getColumnIndex() >= sfNotesIndex+1 && sfNotesIndex != 0){
	                	envCombNamesfromTemplate.add(cell.getStringCellValue().toString());
	                }
	               
	                if (row.getRowNum() > 0 && cell.getColumnIndex() >= sfNotesIndex+1 && sfNotesIndex != 0){
	                	cellEntry = new CellEntry(row.getRowNum()+1, cell.getColumnIndex()+1, cell.getStringCellValue());
	                	
	                		 if(mapfromiLCM.keySet().toString().contains(testcaseCodefromTemplate)){
	                			 envMapfromiLCM = (HashMap<String,String>) mapfromiLCM.get(testcaseCodefromTemplate);
		                		 if(envMapfromiLCM != null && envMapfromiLCM.size()>0){
		                			 Set<String> envSetKeysFromiLCM = envMapfromiLCM.keySet();
		                			 String envComFromTemplate=envCombNamesfromTemplate.get(cellCountEnvComb);
		                				for (String envKeyFromiLCM : envSetKeysFromiLCM) {
		                						//log.info("Environment Key from iLCM --> "+envKeyFromiLCM);
		 										if(getTiltSymbol(envComFromTemplate).trim().equalsIgnoreCase(getTiltSymbol(envKeyFromiLCM).trim())){
		 											String status = "";
		 											status = (String) envMapfromiLCM.get(envKeyFromiLCM);
		 											log.info("Status --> "+status);
		 											if (!status.equals("")){
		 												log.info("true");
		 					                			cellEntry = new CellEntry(row.getRowNum()+1, cell.getColumnIndex()+1, status);
		 			                				 }
		 											break;
		 										}else{
		 											cellEntry = new CellEntry(row.getRowNum()+1, cell.getColumnIndex()+1, cell.getStringCellValue());
		 										}
		 								}
		                		 }
		                	}else{
			                	cellEntry = new CellEntry(row.getRowNum()+1, cell.getColumnIndex()+1, cell.getStringCellValue());
			                }
	                		 cellFeed.insert(cellEntry);
	                		 cellCountEnvComb++;
	                		
	                }else{
	                	cellEntry = new CellEntry(row.getRowNum()+1, cell.getColumnIndex()+1, cell.getStringCellValue());
	                	cellFeed.insert(cellEntry);
	                }
	            }
	            
	        }
	        file.close();
	        log.info(" Export to Google Drive is Completed ");
	 } catch(IOException  e){
		 log.error(" IO Exception -->>> "+e);
		 return false;
	 } catch(ServiceException  se){
		 log.error(" Service Exception -->>> "+se);
		 return false;
	 }catch(Exception ex){
		 log.error(" Exception -->>> "+ex);
		 return false;
	 }
    
	return true;
	}
	
	
	 public Map<String,Map<String,String>> getExecutionDetails(List<WorkPackageTestCaseExecutionPlan> wptcplanListfor){
		 Map<String, Map<String,String>> map = new HashMap<String, Map<String,String>>();
		 if(wptcplanListfor != null && wptcplanListfor.size()>0){
			 Map<String,String> envMap = null; 
			 for (WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan : wptcplanListfor) {
				 String envCombName = "";
				 String envCombExecutionStatus = "";
				 String testCaseCode = null;
				 testCaseCode = workPackageTestCaseExecutionPlan.getTestCase().getTestCaseCode().trim();
				 if(testCaseCode != null){
					 envCombName = workPackageTestCaseExecutionPlan.getRunConfiguration().getRunconfiguration().getRunconfigName().trim();
					 if(workPackageTestCaseExecutionPlan.getTestCaseExecutionResult() != null){
						 String tcres = workPackageTestCaseExecutionPlan.getTestCaseExecutionResult().getResult();
						 envCombExecutionStatus = "Not Checked";
						 	if(tcres.equalsIgnoreCase("PASS") || tcres.equals("1") || tcres.equalsIgnoreCase(IDPAConstants.EXECUTION_RESULT_PASSED)){
								envCombExecutionStatus="Pass";
							}else if(tcres.equalsIgnoreCase("FAIL") || tcres.equals("2") || tcres.equalsIgnoreCase(IDPAConstants.EXECUTION_RESULT_FAILED)){
								envCombExecutionStatus="Fail";
							}else if(tcres.equals("3") || tcres.equalsIgnoreCase(IDPAConstants.EXECUTION_RESULT_NORUN)){
								envCombExecutionStatus="No Run";
							}else if(tcres.equals("4") || tcres.equalsIgnoreCase(IDPAConstants.EXECUTION_RESULT_BLOCKED)){
								envCombExecutionStatus="Blocked";
							}
					 }
					 log.debug("Test Case Id: "+testCaseCode + "    envCombName: "+envCombName+ "    envCombExecutionStatus:  "+envCombExecutionStatus);
					 if(map.containsKey(testCaseCode)){
						 envMap = map.get(testCaseCode);
						 if(envMap.containsKey(envCombName)){
							 envCombExecutionStatus= envMap.get(envCombName);
							 envMap.put(envCombName, envCombExecutionStatus);
						 }else{
							 envMap.put(envCombName, envCombExecutionStatus);
						 }
					 }else{
						 envMap = new HashMap<String, String>();
						 envMap.put(envCombName, envCombExecutionStatus);
					 }
					 map.put(testCaseCode, envMap);
				 }else{
					 continue;
				 }
			 }
			 log.debug("Map size : "+map.size());
			 log.debug("Map : "+map);
		 }
		 return map;
		 
	 }
	 
	public int getSpreadSheetIndx(List<SpreadsheetEntry> spreadsheets,String DefectSpreadsheet) {

		if (spreadsheets.size() == 0) {
			log.info("Spread sheet list is empty ");
			return -1;
		 }
		int spIndx = 0;
		for (SpreadsheetEntry spreadsheet : spreadsheets) {
			log.debug(" Spreadsheet Name : "+spreadsheet.getTitle().getPlainText());
		if(spreadsheet.getTitle().getPlainText().equals(DefectSpreadsheet))
				return spIndx;
		spIndx++;
		}
		
		log.info("Could not find spread sheet " + DefectSpreadsheet);
		return -1;
	}
	
	public int getWorkSheetIndx(List<WorksheetEntry> worksheets,String worksheettoCheck) {

		if (worksheets.size() == 0) {
			log.info("Work sheet list is empty ");
			return -1;
		 }
			int wkIndx = 0;
			for (WorksheetEntry wksheet : worksheets) {
			log.debug(" Worksheet Name : "+wksheet.getTitle().getPlainText());
			if(wksheet.getTitle().getPlainText().equals(worksheettoCheck))
					return wkIndx;
			wkIndx++;
			}
		
		log.info("Could not find work sheet " + worksheettoCheck);
		return -1;
	}
	
	public String getTiltSymbol(String input){
		 input =  input.replaceAll("~", "");
		 input =  input.replaceAll(" ", "");
		 input =  input.replaceAll("/", "");
		return  input;
	}
	
	public boolean isRequiredColumnNameAvailable(Cell cell, String colNameToVerify){
		 boolean isSame = false;
		 if(cell.getStringCellValue().equalsIgnoreCase(colNameToVerify)){
			 isSame = true;
		 }
		 return isSame;
	 }
	 
	 
	 public int getCellIndexByColumnName(Cell cell, String colNameToVerify){
		 int index = 0;
		 if(cell.getStringCellValue().equalsIgnoreCase(colNameToVerify)){
			 index = cell.getColumnIndex();
		 }
		 return index;
	 }
	 
	 public String getworkPackageName(Integer workPackageId){
		 String workPackageName="";
		 WorkPackage wpName = workPackageService.getWorkPackageById(workPackageId);
		 if( wpName.getName() != null){
			 workPackageName = wpName.getName();
			 log.info(" TH WP NAME from getworkPackageName>> "+workPackageName);
		 }
		 return workPackageName;
	 }

	@Override
	@Transactional
	public int addDefectsBulk(List<TestExecutionResultBugList> listOfDefectData,String action) {
		return defectUploadDAO.addBulk(listOfDefectData, Integer.parseInt(defectBugnizerSize), action);
	}

	@Override
	@Transactional
	public List<String> getExistingIssueId(String issueId) {
		return defectUploadDAO.getExistingIssueId(issueId);
	}

	@Override
	@Transactional
	public Integer getDefectDataId(String issueId) {
		return defectUploadDAO.getDefectDataId(issueId);
	}


	@Override
	@Transactional
	public DefectTypeMaster getDefectType(String defectTypeName) {
		return defectUploadDAO.listDefectType(defectTypeName);
	}

	@Override
	@Transactional
	public ExecutionPriority getExecutionPriority(String priorityName) {
		return defectUploadDAO.listExecutionPriority(priorityName);
	}

	@Override
	@Transactional
	public WorkFlow getWorkFlow(String status) {
		return defectUploadDAO.listWorkflow(status);
	}

	@Override
	@Transactional
	public UserList getUserList(String userName) {
		return defectUploadDAO.listUserList(userName);
	}

	@Override
	@Transactional
	public List<TestExecutionResultBugList> listDefectsByTestcaseExecutionPlanIdAnalyse(Integer productId, Integer productVersionId, Integer productBuildId, Integer workPackageId,
			Integer approveStatus, Date startDate, Date endDate,Integer issueStatus, Integer analyseStatus, Integer jtStartIndex, Integer jtPageSize) {
		List<TestExecutionResultBugList> testExecutionResultBugList = new ArrayList<TestExecutionResultBugList>();
		List<Object> bugIdsList = defectUploadDAO.listDefectsByTestcaseExecutionPlanIdAnalyse(productId, productVersionId, productBuildId, workPackageId, approveStatus, startDate, endDate, issueStatus, analyseStatus, jtStartIndex, jtPageSize);
		if(bugIdsList != null && bugIdsList.size()>0){
			for (Object bugId : bugIdsList) {
				TestExecutionResultBugList defect = defectUploadDAO.getByBugWithCompleteInitialization((Integer) bugId);
				if(defect != null){
					testExecutionResultBugList.add(defect);
				}
			}
		}
		return testExecutionResultBugList;
	}
	
	@Override
	@Transactional
	public List<TestExecutionResultBugList> listDefectsAnalyseFromData(String stageName,String featureName, Integer action, Integer jtStartIndex, Integer jtPageSize) {
		return defectUploadDAO.listDefectsAnalyseFromData(stageName, featureName, action, jtStartIndex, jtPageSize);
	}

	@Override
	@Transactional
	public boolean updateDefectAnalyseUpdate(Integer bugId, Integer action) {
		TestExecutionResultBugList defect = defectUploadDAO.getByBugWithCompleteInitialization(bugId);
			defect.setAnalysedFlag(1);
			if(action != 0 ){
				defect.getBugFilingStatus().setWorkFlowId(18);
			}else{
				defect.getBugFilingStatus().setWorkFlowId(1);
			}
			defectUploadDAO.update(defect);
		return true;
	}

	@Override
	@Transactional
	public TestExecutionResultBugList listDefectsBySpecificTestcaseExecutionBugId(Integer bugid) {
		return defectUploadDAO.getByBugWithCompleteInitialization(bugid);
	}

	@Override
	@Transactional
	public void updateDefectbyIssueId(TestExecutionResultBugList testExecutionResultBugListUI) {
		defectUploadDAO.update(testExecutionResultBugListUI);
	}

	@Override
	@Transactional
	public HashMap<String, JsonWorkPackageTestCaseExecutionPlanForTester> getworkPackageEnvironmentSummary(Integer workPackageId) {

		List<TestCaseDTO> listTestCaseDTO=null;
		listTestCaseDTO = defectUploadDAO.listWorkPackageEnvironmentSummary(workPackageId);
		log.debug("  listTestCaseDTO=-=-  "+listTestCaseDTO.size());
		 HashMap<String, JsonWorkPackageTestCaseExecutionPlanForTester>  mapOfJsonWPTester = new HashMap<String,JsonWorkPackageTestCaseExecutionPlanForTester>();
		
		if(listTestCaseDTO!=null && listTestCaseDTO.size()>0){
			JsonWorkPackageTestCaseExecutionPlanForTester jsonProgramExecutionMetricsMaster = null;
			for (TestCaseDTO testCaseDTO : listTestCaseDTO) {
				Integer totalExecutedTCCount=0, notExecuted  = 0;
				
				if(mapOfJsonWPTester.containsKey(testCaseDTO.getTesterId()+"~"+testCaseDTO.getEnvCombName())){
					jsonProgramExecutionMetricsMaster = mapOfJsonWPTester.get(testCaseDTO.getTesterId()+"~"+testCaseDTO.getEnvCombName());
					if(testCaseDTO.getExecutionPriorityId()==IDPAConstants.TESTCASE_EXECUTION_STATUS_COMPLETED)
					{
						if(jsonProgramExecutionMetricsMaster.getTotalExecutedTesCases() != null){
							totalExecutedTCCount = jsonProgramExecutionMetricsMaster.getTotalExecutedTesCases();
							jsonProgramExecutionMetricsMaster.setTotalExecutedTesCases(totalExecutedTCCount+1);
						}else{
							jsonProgramExecutionMetricsMaster.setTotalExecutedTesCases(1);
						}
					}
					if(testCaseDTO.getExecutionPriorityId()==IDPAConstants.TESTCASE_EXECUTION_STATUS_ASSIGNED)
					{
						if(jsonProgramExecutionMetricsMaster.getNotExecuted() != null){
							notExecuted = jsonProgramExecutionMetricsMaster.getNotExecuted();
							jsonProgramExecutionMetricsMaster.setNotExecuted(notExecuted+1);
						}else{
							jsonProgramExecutionMetricsMaster.setNotExecuted(1);
						}
					}
						
				}else{
					jsonProgramExecutionMetricsMaster = new JsonWorkPackageTestCaseExecutionPlanForTester();
					jsonProgramExecutionMetricsMaster.setTesterName(testCaseDTO.getTesterName());
					jsonProgramExecutionMetricsMaster.setEnvironmentCombinationName(testCaseDTO.getEnvCombName());
					if(testCaseDTO.getExecutionPriorityId()==IDPAConstants.TESTCASE_EXECUTION_STATUS_COMPLETED)
					{
						jsonProgramExecutionMetricsMaster.setTotalExecutedTesCases(1);
					}else{
						jsonProgramExecutionMetricsMaster.setTotalExecutedTesCases(0);
					}
					if(testCaseDTO.getExecutionPriorityId()==IDPAConstants.TESTCASE_EXECUTION_STATUS_ASSIGNED)
					{
						jsonProgramExecutionMetricsMaster.setNotExecuted(1);
					}else{
						jsonProgramExecutionMetricsMaster.setNotExecuted(0);
					}
				}
				mapOfJsonWPTester.put(testCaseDTO.getTesterId()+"~"+testCaseDTO.getEnvCombName(),jsonProgramExecutionMetricsMaster);
			}
		}
	
		log.debug("mapOfJsonWPTester : "+mapOfJsonWPTester.size());
		return mapOfJsonWPTester;
	}
	
	@Override
	@Transactional
	public List<String> listRunConfigurationNameBywpId(Integer workPackageId) {
		return defectUploadDAO.listRunConfigurationNameBywpId(workPackageId);
	}
	
	@Override
	@Transactional
	public List<TestCaseDTO> listWorkPackageTestCasesExecutionPlanBywpId(Integer workPackageId) {
		return  defectUploadDAO.listWorkPackageTestCasesExecutionPlanBywpId(workPackageId);
	}

	@Override
	@Transactional
	public List<TestCaseDTO> listWorkPackageTimeSheetBywpId(Integer workPackageId) {
		return  defectUploadDAO.listWorkPackageTimeSheetBywpId(workPackageId);
	}


}

