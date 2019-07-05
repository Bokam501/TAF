package com.hcl.atf.taf.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.Transient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.dao.DashBoardDAO;
import com.hcl.atf.taf.model.ActivityCollection;
import com.hcl.atf.taf.model.DashBoardTabs;
import com.hcl.atf.taf.model.DashBoardTabsRoleBasedURL;
import com.hcl.atf.taf.model.DashboardVisualizationUrls;
import com.hcl.atf.taf.model.DefectCollection;
import com.hcl.atf.taf.model.ProductUserRole;
import com.hcl.atf.taf.model.ReviewRecordCollection;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.json.JsonMetricsMaster;
import com.hcl.atf.taf.model.json.JsonMetricsMasterGroup;
import com.hcl.atf.taf.model.json.jtable.JTableResponse;
import com.hcl.atf.taf.mongodb.model.ActivityCollectionMongo;
import com.hcl.atf.taf.mongodb.model.ActivityMongo;
import com.hcl.atf.taf.mongodb.model.DefectCollectionMongo;
import com.hcl.atf.taf.mongodb.model.TestCaseDefectsMasterMongo;
import com.hcl.atf.taf.mongodb.model.TestCaseExecutionResultMongo;
import com.hcl.atf.taf.mongodb.model.UtilizationCollectionMongo;
import com.hcl.atf.taf.mongodb.service.MongoDBService;
import com.hcl.atf.taf.service.DashBoardService;

@Service
public class DashBoardServiceImpl implements DashBoardService {

	@Transient
	private SimpleDateFormat dateFormatForMongoDB = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
	
	private static final Log log = LogFactory
			.getLog(DashBoardServiceImpl.class);

	@Autowired
	private DashBoardDAO dashBoardDAO;
	
	@Autowired
	private MongoDBService mongoDBService;	
	
	
	@Value("#{ilcmProps['COMPETANCY_PROJECT_BCWS']}")
    private Integer COMPETANCY_PROJECT_BCWS;
	
	@Value("#{ilcmProps['COMPETANCY_PROJECT_STARTDATE']}")
    private String projectStartDate;
	
	@Value("#{ilcmProps['UTILIZATION_GLB_ACTIVITYNAME']}")
    private String utilizationGlb;
	
	@Value("#{ilcmProps['PRODUCT_QUALITY_WEIGHTAGE']}")
    private Integer productQualityWeightage;
	
	@Value("#{ilcmProps['RISK_REMOVAL_WEIGHTAGE']}")
    private Integer riskRemovalIndexWeightage;
	
	@Value("#{ilcmProps['SV_WEIGHTAGE']}")
    private Integer svWeightage;
	
	/*sla metrics poor and target range get from ilcm property*/
	
	@Value("#{ilcmProps['SLA_SCHEDULE_VARIANCE_POOR_RANGE']}")
    private float SLA_SCHEDULE_VARIANCE_POOR_RANGE;
	@Value("#{ilcmProps['SLA_SCHEDULE_VARIANCE_TARGET_RANGE']}")
    private float SLA_SCHEDULE_VARIANCE_TARGET_RANGE;
	@Value("#{ilcmProps['SLA_SCHEDULE_VARIANCE_TARGET_RANGE_UI']}")
	private String SLA_SCHEDULE_VARIANCE_TARGET_RANGE_UI;
	
	@Value("#{ilcmProps['SLA_PRODUCT_QUALITY_POOR_RANGE']}")
    private float SLA_PRODUCT_QUALITY_POOR_RANGE;
	@Value("#{ilcmProps['SLA_PRODUCT_QUALITY_TARGET_RANGE']}")
    private float SLA_PRODUCT_QUALITY_TARGET_RANGE;
	@Value("#{ilcmProps['SLA_PRODUCT_QUALITY_TARGET_RANGE_UI']}")
	private String SLA_PRODUCT_QUALITY_TARGET_RANGE_UI;
	
	@Value("#{ilcmProps['SLA_RISK_REMOVAL_INDEX_POOR_RANGE']}")
    private float SLA_RISK_REMOVAL_INDEX_POOR_RANGE;
	@Value("#{ilcmProps['SLA_RISK_REMOVAL_INDEX__TARGET_RANGE']}")
    private float SLA_RISK_REMOVAL_INDEX__TARGET_RANGE;
	@Value("#{ilcmProps['SLA_RISK_REMOVAL_INDEX__TARGET_RANGE_UI']}")
	private String SLA_RISK_REMOVAL_INDEX__TARGET_RANGE_UI;
	
	@Value("#{ilcmProps['SLA_HEALTH_INDEX__POOR_RANGE']}")
    private float SLA_HEALTH_INDEX__POOR_RANGE;
	@Value("#{ilcmProps['SLA_HEALTH_INDEX__TARGET_RANGE']}")
    private float SLA_HEALTH_INDEX__TARGET_RANGE;
	
	@Value("#{ilcmProps['SLA_PRODUCTIVITY_POOR_RANGE']}")
    private float SLA_PRODUCTIVITY_POOR_RANGE;
	@Value("#{ilcmProps['SLA_PRODUCTIVITY_TARGET_RANGE']}")
    private float SLA_PRODUCTIVITY_TARGET_RANGE;
	@Value("#{ilcmProps['SLA_PRODUCTIVITY_TARGET_RANGE_UI']}")
	private String SLA_PRODUCTIVITY_TARGET_RANGE_UI;
	
	@Value("#{ilcmProps['SLA_UTILIZATION_INDEX_POOR_RANGE']}")
    private float SLA_UTILIZATION_INDEX_POOR_RANGE;
	@Value("#{ilcmProps['SLA_UTILIZATION_INDEX_TARGET_RANGE']}")
    private float SLA_UTILIZATION_INDEX_TARGET_RANGE;
	@Value("#{ilcmProps['SLA_UTILIZATION_INDEX_TARGET_RANGE_UI']}")
	private String SLA_UTILIZATION_INDEX_TARGET_RANGE_UI;
	
	@Value("#{ilcmProps['SLA_PCI_SCORE_POOR_RANGE']}")
    private float SLA_PCI_SCORE_POOR_RANGE;
	@Value("#{ilcmProps['SLA_PCI_SCORE_TARGET_RANGE']}")
    private float SLA_PCI_SCORE_TARGET_RANGE;
	@Value("#{ilcmProps['SLA_PCI_SCORE_TARGET_RANGE_UI']}")
	private String SLA_PCI_SCORE_TARGET_RANGE_UI;
	
	@Value("#{ilcmProps['SLA_CSAT_SCORE_POOR_RANGE']}")
    private float SLA_CSAT_SCORE_POOR_RANGE;
	@Value("#{ilcmProps['SLA_CSAT_SCORE_TARGET_RANGE']}")
    private float SLA_CSAT_SCORE_TARGET_RANGE;
	@Value("#{ilcmProps['SLA_CSAT_SCORE_TARGET_RANGE_UI']}")
	private String SLA_CSAT_SCORE_TARGET_RANGE_UI;
	
	@Value("#{ilcmProps['SLA_ARID_POOR_RANGE']}")
    private float SLA_ARID_POOR_RANGE;
	@Value("#{ilcmProps['SLA_ARID_TARGET_RANGE']}")
    private float SLA_ARID_TARGET_RANGE;
	@Value("#{ilcmProps['SLA_ARID_TARGET_RANGE_UI']}")
	private String SLA_ARID_TARGET_RANGE_UI;
	
	private String[] months = {"JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC"};
	
	@Override
	@Transactional
	public List<ActivityCollection> getActivityCollectionCountByDateFilter(Date startDate,Date currentDate) {
		
		return dashBoardDAO.getActivityCollectionCountByDateFilter(startDate,currentDate);
	}

	@Override
	@Transactional
	public float getActivityCollectionListForScheduleVariance(Date startDate,Date currentDate) {
			
		Float schVar = 0.0F;
		List<ActivityCollection> activityCollections = dashBoardDAO.getActivityCollectionListForScheduleVariance(startDate,currentDate);
		try{
			Date plannedStartDate = null;
			Date plannedEndDate = null;
			Date actualEndDate = null;
			int counter = 0;
			for (ActivityCollection activityCollection : activityCollections) {
				if(counter == 0){
					actualEndDate = activityCollection.getActualActivityEndDate();
				}else if(counter == 1){
					plannedEndDate = activityCollection.getPlannedActivityEndDate();
				}else if(counter == 2){
					plannedStartDate = activityCollection.getPlannedActivityStartDate();
				}
				counter++;
			}

			if(plannedStartDate != null && plannedEndDate != null && actualEndDate != null){
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				plannedStartDate = dateFormat.parse(dateFormat.format(plannedStartDate));
				plannedEndDate = dateFormat.parse(dateFormat.format(plannedEndDate));
				actualEndDate = dateFormat.parse(dateFormat.format(actualEndDate));
				
				long actEndPlannedEnd = actualEndDate.getTime() - plannedEndDate.getTime();
				int actEndPlannedEndDays = (int) TimeUnit.DAYS.convert(actEndPlannedEnd, TimeUnit.MILLISECONDS);

				long plannedEndPlannedStart = plannedEndDate.getTime() - plannedStartDate.getTime();
				int plannedEndPlannedStartDays = (int) TimeUnit.DAYS.convert(plannedEndPlannedStart, TimeUnit.MILLISECONDS);

				log.info("actEndPlannedEndDays****"+actEndPlannedEndDays);
				log.info("plannedEndPlannedStartDays****"+plannedEndPlannedStartDays);
				schVar = (((float)actEndPlannedEndDays / (float)plannedEndPlannedStartDays));

			}
		}catch(Exception ex){
			log.error("ERROR  ",ex);
		}
		
		return schVar;
	}
	

	@Override
	@Transactional
	public List<DefectCollection> getDefectCollectionList() {
		return dashBoardDAO.getDefectCollectionList();
	}
	
	@Override
	@Transactional
	public List<ReviewRecordCollection> getReivewRecordCollectionList() {
		return dashBoardDAO.getReivewRecordCollectionList();
	}
	
	@Override
	@Transactional
	public void addDashboardTabsToUI(DashBoardTabs dashBoardTabs) {
		 dashBoardDAO.addDashboardTabsToUI(dashBoardTabs);
		
	}


	@Override
	@Transactional
	public List<DashBoardTabs> listDashboardTabs(Integer status,Integer jtStartIndex, Integer jtPageSize) {
		return dashBoardDAO.listDashboardTabs(status,jtStartIndex,jtPageSize);
	}
	

	@Override
	@Transactional
	public void update(DashBoardTabs dashBoardTabs) {
		dashBoardDAO.update(dashBoardTabs);
		
	}


	@Override
	@Transactional
	public void addDashboardTabsRoleUrl(DashBoardTabsRoleBasedURL dashBoardTabsRoleBasedURL) {
		 dashBoardDAO.addDashboardTabsRoleUrl(dashBoardTabsRoleBasedURL);
		
	}


	@Override
	@Transactional
	public List<DashBoardTabsRoleBasedURL> listDashboardTabsRoleBasedURL(Integer roleId,Integer tabId) {
		return dashBoardDAO.listDashboardTabsRoleBasedURL(roleId,tabId);
	}


	@Override
	@Transactional
	public void update(DashBoardTabsRoleBasedURL dashBoardTabsRoleBasedFromUI) {
		dashBoardDAO.update(dashBoardTabsRoleBasedFromUI);
		
	}

	@Override
	@Transactional
	public String getIndicatorValue(float actualValue, float poorRange, float highRange,String type) {
		String indicator="Green";
		if("Schedule Variance".equalsIgnoreCase(type)||"ARID SCORE".equalsIgnoreCase(type)){
			if(actualValue>=poorRange){
				indicator="Red";
			}else if(actualValue<poorRange && actualValue>highRange){
				indicator="Yellow";
			}
		}else{
			if(actualValue<=poorRange){
				indicator="Red";
			}else if(actualValue>poorRange && actualValue<highRange){
				indicator="Yellow";
			}
		}
		
		
						
		return indicator;
	}

	@Override
	@Transactional
	public void deleteroleBasedDataById(DashBoardTabsRoleBasedURL dashboardroleuser) {
		dashBoardDAO.deleteroleBasedDataById(dashboardroleuser);
		
	}

	@Override
	@Transactional
	public DashBoardTabsRoleBasedURL getDashBoardTabsRoleBasedURLById(Integer roleBasedId) {
		return dashBoardDAO.getDashBoardTabsRoleBasedURLById(roleBasedId);
	}
	
	
	@Override
	@Transactional
	public JTableResponse dashboardMetricsSummaryCalculation(Date startDate,Date endDate,String testFactoryName,String productName,UserList user){

		JTableResponse jTableResponse = null;
		try {	
			log.info("dashboard.metrics.summary.calculation ");
			
			List<HashMap<String, String>> scheduleVariances = new ArrayList<HashMap<String, String>>();
			List<HashMap<String, String>> defectValitities = new ArrayList<HashMap<String, String>>();
			List<HashMap<String, String>> defectQualities = new ArrayList<HashMap<String, String>>();
			List<HashMap<String, String>> testCaseJobsExecutions = new ArrayList<HashMap<String, String>>();
			List<HashMap<String, String>> costPerUnits= new ArrayList<HashMap<String, String>>();
			List<HashMap<String, String>> defectFindRates= new ArrayList<HashMap<String, String>>();
			List<HashMap<String, String>> internalDREs= new ArrayList<HashMap<String, String>>();
			List<HashMap<String, String>> aridMapScores= new ArrayList<HashMap<String, String>>();
			
			
			
			Integer countOfActivityPlanned=0;
			Integer countOfActivityActual=0;
			Integer invalidDefectCount=0;
			Integer notFirstTimeDefects=0;
			Integer defectsLeakedCount=0;
			float totalDefectsCount=0;
			Integer totalWorkingDays=0;
			Integer resourceCount=0;
			
			float defectFindRate=0;
			float percentageForScheduleVar = 0;
			float percentageForDefectValidity = 0;
			float percentageForDefectQuality=0;
			Integer testCaseJobsValue=0;
			Integer costPerUnitValue=0;
			Integer countOfTestacaseJobActual=0;
			float internalDRE=0;
			Integer activityTypeCount = 0;
			Date currentDate=endDate;
			ArrayList<String> actvityList = new ArrayList<String>();
			
			log.info("competancyProjectBCWS******** "+COMPETANCY_PROJECT_BCWS);
			boolean isMongoDBAavailable = mongoDBService.checkAvailabiltyOfMongoDB();
			if(!isMongoDBAavailable){
				jTableResponse = new JTableResponse("ERROR", "Check Your MongoDB Availablity");
				log.error("MongoDB Connection Failed");
				return jTableResponse;
			}
			startDate = setDateForMongoDB(startDate);
			
			Calendar currentDateCalendar = Calendar.getInstance();
			currentDateCalendar.setTime(endDate);
			currentDateCalendar.set(Calendar.HOUR_OF_DAY, 23);
			currentDateCalendar.set(Calendar.MINUTE, 59);
			currentDateCalendar.set(Calendar.SECOND, 59);
			currentDateCalendar.set(Calendar.MILLISECOND, 999);
			currentDate = setDateForMongoDB(currentDateCalendar.getTime());
			
			Set<ProductUserRole> userProducts = null;
			if(user.getUserRoleMaster().getUserRoleId() != IDPAConstants.ROLE_ID_ADMIN && "ALL".equalsIgnoreCase(testFactoryName) && "ALL".equalsIgnoreCase(productName)){
				userProducts = user.getProductUserRoleSet();
			}else if(user.getUserRoleMaster().getUserRoleId() != IDPAConstants.ROLE_ID_ADMIN && !"ALL".equalsIgnoreCase(testFactoryName) && "ALL".equalsIgnoreCase(productName)){	
				Set<ProductUserRole> userProductsTestfactoryWise = user.getProductUserRoleSet();
				if(userProductsTestfactoryWise != null){
					userProducts = new HashSet<ProductUserRole>();
					for(ProductUserRole productUserRole : userProductsTestfactoryWise){
						if(testFactoryName.equalsIgnoreCase(productUserRole.getProduct().getTestFactory().getTestFactoryName())){
							userProducts.add(productUserRole);
						}
					}
				}
			}
		
			/* 1. Sch.Var - consolidated */
		List<ActivityMongo> listActivity = mongoDBService.getActivityMongoByDateFilter(startDate,currentDate,"activity",testFactoryName,productName,userProducts);
		
			for (ActivityMongo activity : listActivity) {
				if(activity.getPlannedUnit() != null){
					countOfActivityPlanned=(int) (countOfActivityPlanned+activity.getPlannedUnit());
				}
				if(activity.getActualUnit() != null){
					countOfActivityActual=(int) (countOfActivityActual+activity.getActualUnit());
				}
			}
			activityTypeCount = actvityList.size();
			
			log.info("countOfActivityPlanned ===="+countOfActivityPlanned);
			log.info("countOfActivityActual ===="+countOfActivityActual);
			if(countOfActivityPlanned == 0){
				percentageForScheduleVar = 0;
			}else{
				percentageForScheduleVar=(((float) COMPETANCY_PROJECT_BCWS/(float)countOfActivityPlanned)*(float)countOfActivityActual)-(float)COMPETANCY_PROJECT_BCWS;
			}
			
			/* 2.  Defect Validity   */
			List<DefectCollectionMongo> listDefects=mongoDBService.getDefectMongoCollectionList(null,"defect_collection",testFactoryName,productName,userProducts);
			
			totalDefectsCount=listDefects.size();
		
			for (DefectCollectionMongo defect : listDefects) {
				if((defect.getInjection().equalsIgnoreCase(IDPAConstants.INVALID_TESTCASE))||(defect.getInjection().equalsIgnoreCase(IDPAConstants.TESTER_ERROR))||(defect.getInjection().equalsIgnoreCase(IDPAConstants.TEST_ENVIRONMENT_ERROR))){
					invalidDefectCount++;
				}
				/* for Defect Quality   */
				if((defect.getCategory().equalsIgnoreCase(IDPAConstants.DEFECT_CATEGORY_MANDATORY_INFORMATION))||(defect.getCategory().equalsIgnoreCase(IDPAConstants.DEFECT_CATEGORY_DIFFERENT_BUILD_OR_PRODUCT))||
						(defect.getCategory().equalsIgnoreCase(IDPAConstants.DEFECT_CATEGORY_ADDITIONAL_INFORMATION))){
							notFirstTimeDefects++;
				}
				
				/* for Internal DRE*/
				if((defect.getDetection().trim().equalsIgnoreCase(IDPAConstants.DEFECT_DETECTION_GMC_GM))||(defect.getDetection().trim().equalsIgnoreCase(IDPAConstants.DEFECT_DETECTION_Pre_GMC_GM))||(defect.getDetection().trim().equalsIgnoreCase(IDPAConstants.DEFECT_DETECTION_Pre_GMC_GMC))){
					defectsLeakedCount++;
				}
				
			}
			if(totalDefectsCount == 0){
				percentageForDefectValidity = 100;
			}else{
				percentageForDefectValidity=	((float)(totalDefectsCount-invalidDefectCount)/totalDefectsCount*100);
			}
			/* 3. Defect Quality   */
			if(totalDefectsCount == 0){
				percentageForDefectQuality = 100;
			}else{
				percentageForDefectQuality=	((float)(totalDefectsCount-notFirstTimeDefects)/totalDefectsCount*100);
			}
			/* 4. Test Case Jobs Execution */
			
			Calendar startDateCalendar = Calendar.getInstance();
			startDateCalendar.setTime(DateUtility.dateFromISTFormatString(projectStartDate));
			
			Calendar todayDateCalendar = Calendar.getInstance();
			while(startDateCalendar.before(todayDateCalendar)){
				if(startDateCalendar.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && startDateCalendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY){
					totalWorkingDays = totalWorkingDays+1;
				}
				startDateCalendar.add(Calendar.DAY_OF_MONTH, 1);
			}
			
			resourceCount=mongoDBService.getProjectTeamMembersCount(startDate,currentDate,testFactoryName,productName,userProducts);
			int resWorkingCount = (resourceCount*totalWorkingDays);
			
			
			for(ActivityMongo activityData:listActivity){
				if(activityData!=null && activityData.getActivityMasterName()!=null ){
					if(activityData.getActivityMasterName().equalsIgnoreCase("Test case")||activityData.getActivityMasterName().equalsIgnoreCase("Magen")||activityData.getActivityMasterName().equalsIgnoreCase("Pairwise")||
							activityData.getActivityMasterName().equalsIgnoreCase("Magen / Pairwise")){
						if(activityData.getActualUnit() != null){
							countOfTestacaseJobActual=(int) (countOfTestacaseJobActual+activityData.getActualUnit());
						}
					}
				}
				
			}
			
			if(resWorkingCount == 0){
				testCaseJobsValue = 100;
			}else{
				testCaseJobsValue= countOfTestacaseJobActual / resWorkingCount;
			}
			
			if(resWorkingCount == 0){
				costPerUnitValue = 100;
			}else{
				costPerUnitValue= countOfActivityActual / resWorkingCount;
			}
			log.info("costPerUnitValue  "+costPerUnitValue);
			
			if(resWorkingCount == 0){
				defectFindRate = 100;
			}else{
				defectFindRate=totalDefectsCount / resWorkingCount;
			}
		
			if(totalDefectsCount!=0){
				internalDRE=(1-(defectsLeakedCount/totalDefectsCount))*100;
			}else{
				internalDRE=100;
			}
			
			List<JsonMetricsMaster> jsonMetricsMasters = new ArrayList<JsonMetricsMaster>();
			
			JsonMetricsMaster jsonMetricsMaster = new JsonMetricsMaster();
			jsonMetricsMaster.setMetricsName("Schedule Variance");
			jsonMetricsMaster.setMetricsType(IDPAConstants.SCHEDULE_VARIANCE);
			jsonMetricsMaster.setTarget(0);
			jsonMetricsMaster.setIndicator(getIndicatorValue(percentageForScheduleVar,4, 0,"Schedule Variance"));
			jsonMetricsMaster.setActualValue(String.format("%.2f", percentageForScheduleVar)   +"%");
			jsonMetricsMaster.setTargetAvailable(true);
			jsonMetricsMaster.setTargetRange(">4%~1-3%~<=0%");
			jsonMetricsMaster.setTrendMetrics(scheduleVariances);
			jsonMetricsMasters.add(jsonMetricsMaster);
			
			
			jsonMetricsMaster = new JsonMetricsMaster();
			jsonMetricsMaster.setMetricsName("Defect Validity");
			jsonMetricsMaster.setMetricsType(IDPAConstants.DEFECT_VALIDITY);
			jsonMetricsMaster.setTarget(93);
			jsonMetricsMaster.setIndicator(getIndicatorValue(percentageForDefectValidity,80, 93,"Defect Validity"));
			jsonMetricsMaster.setActualValue(String.format("%.2f", percentageForDefectValidity)   +"%");
			jsonMetricsMaster.setTargetAvailable(true);
			jsonMetricsMaster.setTargetRange("<80%~80-93%~>93%");
			jsonMetricsMaster.setTrendMetrics(defectValitities);
			jsonMetricsMasters.add(jsonMetricsMaster);
			
			
			jsonMetricsMaster = new JsonMetricsMaster();
			jsonMetricsMaster.setMetricsName("Defect Quality");
			jsonMetricsMaster.setMetricsType(IDPAConstants.DEFECT_QUALITY);
			jsonMetricsMaster.setTarget(96);
			jsonMetricsMaster.setIndicator(getIndicatorValue(percentageForDefectQuality,80, 93,"Defect Quality"));
			jsonMetricsMaster.setActualValue(String.format("%.2f", percentageForDefectQuality)   +"%");
			jsonMetricsMaster.setTargetAvailable(true);
			jsonMetricsMaster.setTargetRange("<80%~80-93%~>93%");
			jsonMetricsMaster.setTrendMetrics(defectQualities);
			jsonMetricsMasters.add(jsonMetricsMaster);
			
			
			
			jsonMetricsMaster = new JsonMetricsMaster();
			jsonMetricsMaster.setMetricsName("Test Case Jobs Execution");
			jsonMetricsMaster.setMetricsType(IDPAConstants.TESTCASE_JOB_EXECUTION);
			jsonMetricsMaster.setTarget(4);
			jsonMetricsMaster.setIndicator(getIndicatorValue(testCaseJobsValue.floatValue(),0, 5,"Test Case Jobs Execution"));
			jsonMetricsMaster.setActualValue(String.format("%.2f", testCaseJobsValue.floatValue())   +"%");
			jsonMetricsMaster.setTargetAvailable(true);
			jsonMetricsMaster.setTargetRange("<0%~1-2%~>5%");
			jsonMetricsMaster.setTrendMetrics(testCaseJobsExecutions);
			jsonMetricsMasters.add(jsonMetricsMaster);
			
			jsonMetricsMaster = new JsonMetricsMaster();
			jsonMetricsMaster.setMetricsName("Cost Per Unit");
			jsonMetricsMaster.setMetricsType(IDPAConstants.COST_PER_UNIT);
			jsonMetricsMaster.setTarget(4);
			jsonMetricsMaster.setIndicator(getIndicatorValue(testCaseJobsValue.floatValue(),0, 5,"Test Case Jobs Execution"));
			jsonMetricsMaster.setActualValue(String.format("%.2f", testCaseJobsValue.floatValue())   +"%");
			jsonMetricsMaster.setTargetAvailable(true);
			jsonMetricsMaster.setTargetRange("<0%~1-2%~>5%");
			jsonMetricsMaster.setTrendMetrics(costPerUnits);
			jsonMetricsMasters.add(jsonMetricsMaster);
			
			jsonMetricsMaster = new JsonMetricsMaster();
			jsonMetricsMaster.setMetricsName("Defect Find Rate");
			jsonMetricsMaster.setMetricsType(IDPAConstants.DEFECT_FIND_RATE);
			jsonMetricsMaster.setTarget(5);
			jsonMetricsMaster.setIndicator(getIndicatorValue(defectFindRate,0, 5,"Defect Find Rate"));
			jsonMetricsMaster.setActualValue(String.format("%.2f", defectFindRate)   +"%");
			jsonMetricsMaster.setTargetAvailable(true);
			jsonMetricsMaster.setTargetRange("<0%~1-2%~>5%");
			jsonMetricsMaster.setTrendMetrics(defectFindRates);
			jsonMetricsMasters.add(jsonMetricsMaster);
			
			jsonMetricsMaster = new JsonMetricsMaster();
			jsonMetricsMaster.setMetricsName("Internal DRE");
			jsonMetricsMaster.setMetricsType(IDPAConstants.INTERNAL_DRE);
			jsonMetricsMaster.setTarget(0);
			jsonMetricsMaster.setIndicator(getIndicatorValue(internalDRE,80, 93,"Internal DRE"));
			jsonMetricsMaster.setActualValue(String.format("%.2f", internalDRE)   +"%");
			jsonMetricsMaster.setTargetAvailable(true);
			jsonMetricsMaster.setTargetRange("<80%~80-93%~>93%");
			jsonMetricsMaster.setTrendMetrics(internalDREs);
			jsonMetricsMasters.add(jsonMetricsMaster);
			
			ArrayList<JsonMetricsMasterGroup> jsonMetricsMasterGroups = new ArrayList<JsonMetricsMasterGroup>();
			JsonMetricsMasterGroup jsonMetricsMasterGroup = new JsonMetricsMasterGroup("", jsonMetricsMasters, "", "","");
			jsonMetricsMasterGroups.add(jsonMetricsMasterGroup);
			
			jTableResponse = new JTableResponse("OK", jsonMetricsMasterGroups,jsonMetricsMasterGroups.size());
        } catch (Exception e) {
        	jTableResponse = new JTableResponse("Error");
            log.error("JSON ERROR", e);	            
        }
		return jTableResponse;
    
	}
	
	private Date getStartDate(Date startDate, Date endDate, int monthDifference){
		Calendar startDateCalendar = Calendar.getInstance();
		startDateCalendar.setTime(startDate);
		if(monthDifference != 0){
			startDateCalendar.setTime(endDate);
			startDateCalendar.set(Calendar.DAY_OF_MONTH, 1);
			startDateCalendar.add(Calendar.MONTH, -(monthDifference));
		}
		return setDateForMongoDB(startDateCalendar.getTime());
	}
	
	private Date getEndDate(Date endDate, int monthDifference){
		Calendar endDateCalendar = Calendar.getInstance();
		endDateCalendar.setTime(endDate);
		endDateCalendar.set(Calendar.HOUR_OF_DAY, 23);
		endDateCalendar.set(Calendar.MINUTE, 59);
		endDateCalendar.set(Calendar.SECOND, 59);
		endDateCalendar.set(Calendar.MILLISECOND, 999);
		if(monthDifference != 0){
			endDateCalendar.add(Calendar.MONTH, -(monthDifference));
			endDateCalendar.set(Calendar.DAY_OF_MONTH, endDateCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		}
		return setDateForMongoDB(endDateCalendar.getTime());
	}
	
	@Override
	@Transactional
	public JTableResponse slaDashboardMetricsSummaryCalculation(Date startDate,Date endDate,String testFactoryName,String productName, UserList user){
		JTableResponse jTableResponse = null;
		try {	
			log.info("sla.dashboard.metrics.summary.calculation function");
			
			Integer totalDefectsCount=0;
			Integer totalReviewRecordsCount=0;
			Integer totalDPAsCount=0;
			float numberOfCommitments=0;
			float effortTaken=0;
			Integer closedDefectsCount=0;
			Integer closedReviewRecords=0;
			Integer closedDPAs = 0;
			
			float riskRemovalIndex = 0;
			float productQuality=0;
			float productivity=0;
			float healthIndex=0;
			float productQualityHealth=0;
			float riskIndexHealth=0;
			
			Date currentDate=endDate;
			
			
			
			float utilizationIndex=0;
			float sumOfEffortSpend=0;
			Integer totalWorkingDays=0;
			float  totalefforts=0;
			
			double pciScore=0;
			float  pciScoreAvg=0;
			float  csat=0;
			float  arid=0;
			
			List<Date> startDates = new ArrayList<Date>();
			List<Date> endDates = new ArrayList<Date>();
			
			List<HashMap<String, String>> scheduleVariances = new ArrayList<HashMap<String, String>>();
			List<HashMap<String, String>> productivities = new ArrayList<HashMap<String, String>>();
			List<HashMap<String, String>> utilizationIndexes = new ArrayList<HashMap<String, String>>();
			List<HashMap<String, String>> productQualities = new ArrayList<HashMap<String, String>>();
			List<HashMap<String, String>> riskIndexs= new ArrayList<HashMap<String, String>>();
			List<HashMap<String, String>> pciScores= new ArrayList<HashMap<String, String>>();
			List<HashMap<String, String>> csatScores= new ArrayList<HashMap<String, String>>();
			List<HashMap<String, String>> aridMapScores= new ArrayList<HashMap<String, String>>();
			
			
			for(int i = 0; i <= 3; i++){
				startDates.add(getStartDate(startDate, endDate, i));
				endDates.add(getEndDate(endDate, i));
			}
			
			
			boolean isMongoDBAavailable = mongoDBService.checkAvailabiltyOfMongoDB();
			
			if(!isMongoDBAavailable){
				jTableResponse = new JTableResponse("ERROR", "Check Your MongoDB Availablity");
				log.info("MongoDB Connection Failed");
				return jTableResponse;
			}
			
			Set<ProductUserRole> userProducts = null;
			if(user.getUserRoleMaster().getUserRoleId() != IDPAConstants.ROLE_ID_ADMIN && "ALL".equalsIgnoreCase(testFactoryName) && "ALL".equalsIgnoreCase(productName)){
				userProducts = user.getProductUserRoleSet();
			}else if(user.getUserRoleMaster().getUserRoleId() != IDPAConstants.ROLE_ID_ADMIN && !"ALL".equalsIgnoreCase(testFactoryName) && "ALL".equalsIgnoreCase(productName)){	
				Set<ProductUserRole> userProductsTestfactoryWise = user.getProductUserRoleSet();
				if(userProductsTestfactoryWise != null){
					userProducts = new HashSet<ProductUserRole>();
					for(ProductUserRole productUserRole : userProductsTestfactoryWise){
						if(testFactoryName.equalsIgnoreCase(productUserRole.getProduct().getTestFactory().getTestFactoryName())){
							userProducts.add(productUserRole);
						}
					}
				}
			}
			
			float scheduleVarianceHealth = 0;
			float schVar = 0;
			float trendSchVar = 0;
			Calendar monthCalendar = Calendar.getInstance();
			for(int i = 0; i <= 3; i++){
				trendSchVar = 0;
				if(i == 0){
					schVar = scheduleVarianceHealth = mongoDBService.getActivityMongoCollectionListForScheduleVarianceAvg(startDates.get(i), endDates.get(i), testFactoryName, productName, userProducts); 
				}else{
					trendSchVar = mongoDBService.getActivityMongoCollectionListForScheduleVarianceAvg(startDates.get(i), endDates.get(i), testFactoryName, productName, userProducts);
					HashMap<String, String> trendScheduleVariances = new HashMap<String, String>();
					monthCalendar.setTime(startDates.get(i));
					trendScheduleVariances.put("month", months[monthCalendar.get(Calendar.MONTH)]);
					trendScheduleVariances.put("indicator", getIndicatorValue(trendSchVar,SLA_SCHEDULE_VARIANCE_POOR_RANGE, SLA_SCHEDULE_VARIANCE_TARGET_RANGE,"Schedule Variance"));
					trendScheduleVariances.put("actualValue", String.format("%.2f", trendSchVar)   +"");
					scheduleVariances.add(trendScheduleVariances);
				}
			}
			/* 2.  Product Quality Index  */
			
			/* 3.  Productivity   */
			float trendProductivity=0;
			float actualSize=0;
			for(int i = 0; i <= 3; i++){
				trendProductivity = 0;
				numberOfCommitments=0;
				effortTaken=0;
				
				List<ActivityCollectionMongo> listActivityMongoRecords = mongoDBService.getActivityMongoCollectionCountByDateFilter(startDates.get(i), endDates.get(i),"activity_collection",testFactoryName, productName,userProducts);
				for (ActivityCollectionMongo activity : listActivityMongoRecords) {

					if(activity.getActivitySizeActual() == null || activity.getActivitySizeActual() == 0){
						actualSize=1;
					}else{
						actualSize=activity.getActivitySizeActual();
					}
					numberOfCommitments=numberOfCommitments+(actualSize*activity.getWeightageUnit());
					effortTaken=effortTaken+activity.getActualActivityEffort();
				}
				if(effortTaken==0){
					trendProductivity = 0;
				}else{
					trendProductivity = (numberOfCommitments/effortTaken)*9;
				}
				if(i == 0){
					productivity = trendProductivity;
				}else{
					HashMap<String, String> trendProductivities = new HashMap<String, String>();
					monthCalendar.setTime(startDates.get(i));
					trendProductivities.put("month", months[monthCalendar.get(Calendar.MONTH)]);
					trendProductivities.put("indicator", getIndicatorValue(trendProductivity,SLA_PRODUCTIVITY_POOR_RANGE, SLA_PRODUCTIVITY_TARGET_RANGE,"Productivity"));
					trendProductivities.put("actualValue", String.format("%.2f", trendProductivity)   +"");
					productivities.add(trendProductivities);
				}
			}
			
			/* 4.  Risk Index  */
			float trendRiskIndex=0;
			Date riskStartDate = null;
			Date riskEndDate = null;
			Integer timeZoneOffSet = Calendar.getInstance().getTimeZone().getRawOffset();
			for(int i = 0; i <= 3; i++){
				trendRiskIndex=0;
				closedDefectsCount=0;
				totalDefectsCount=0;
				
				riskStartDate = startDates.get(i);
				riskEndDate = endDates.get(i);
				
				List<DefectCollectionMongo> listDefectMongoRecords= mongoDBService.getDefectMongoCollectionList(riskEndDate,"defect_collection",testFactoryName, productName,userProducts);
				if(riskStartDate != null){
					riskStartDate = new Date(riskStartDate.getTime() - timeZoneOffSet);
				}
				if(riskEndDate != null){
					riskEndDate = new Date(riskEndDate.getTime() - timeZoneOffSet);
				}
				
				Date raisedDate = null;
				Date closedDate = null;
				for (DefectCollectionMongo defect : listDefectMongoRecords) {
					
					raisedDate = null;
					if(defect.getRaisedDate() != null){
						raisedDate = (Date) defect.getRaisedDate();
					}
					
					closedDate = null;
					if(defect.getClosedDate() != null){
						closedDate = (Date) defect.getClosedDate();
					}
					if((defect.getStatus().equalsIgnoreCase("Close")||defect.getStatus().equalsIgnoreCase("Closed")) && closedDate != null && (closedDate.after(riskStartDate) || closedDate.equals(riskStartDate)) && (closedDate.before(riskEndDate) || closedDate.equals(riskEndDate))){
						closedDefectsCount++;
						totalDefectsCount++;
					}else if((defect.getStatus().equalsIgnoreCase("Close")||defect.getStatus().equalsIgnoreCase("Closed")) && closedDate == null && raisedDate != null && (raisedDate.after(riskStartDate) || raisedDate.equals(riskStartDate)) && (raisedDate.before(riskEndDate) || raisedDate.equals(riskEndDate))){
						closedDefectsCount++;
						totalDefectsCount++;
					}
					
					
					if("Open".equalsIgnoreCase(defect.getStatus()) || "In-progress".equalsIgnoreCase(defect.getStatus()) || "Inprogress".equalsIgnoreCase(defect.getStatus())){
						totalDefectsCount++;
					}else if((defect.getStatus().equalsIgnoreCase("Close")||defect.getStatus().equalsIgnoreCase("Closed")) && closedDate != null && closedDate.after(riskEndDate)){
						totalDefectsCount++;
					}
					
				}
				if(totalDefectsCount == 0){
					riskIndexHealth=1;
					trendRiskIndex=riskIndexHealth*100;
				}else{
					riskIndexHealth=((float)(totalDefectsCount - closedDefectsCount)/totalDefectsCount);
					trendRiskIndex=riskIndexHealth*100;
				}
				
				if(i == 0){
					riskRemovalIndex = trendRiskIndex;
				}else{
					HashMap<String, String> trendRiskIndexes = new HashMap<String, String>();
					monthCalendar.setTime(riskStartDate);
					trendRiskIndexes.put("month", months[monthCalendar.get(Calendar.MONTH)]);
					trendRiskIndexes.put("indicator", getIndicatorValue(trendRiskIndex,SLA_RISK_REMOVAL_INDEX_POOR_RANGE, SLA_RISK_REMOVAL_INDEX__TARGET_RANGE,"Risk Removal Index"));
					trendRiskIndexes.put("actualValue", String.format("%.2f", trendRiskIndex)   +"");
					riskIndexs.add(trendRiskIndexes);
				}
			}
			
			/* 5.  Utilization Index  */
		float trendUtilizationIndex=0;
		for(int i = 0; i <= 3; i++){
			totalefforts=0;
			totalWorkingDays=0;
			trendUtilizationIndex=0;
			sumOfEffortSpend=0;
			Calendar startDateCalendar = Calendar.getInstance();
			startDateCalendar.setTime(DateUtility.getDateFromDateTime(startDates.get(i)));
			
			Calendar todayDateCalendar = Calendar.getInstance();
			todayDateCalendar.setTime(DateUtility.getDateFromDateTime(endDates.get(i)));
			
			while(startDateCalendar.before(todayDateCalendar)){
				if(startDateCalendar.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && startDateCalendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY){
					totalWorkingDays = totalWorkingDays+1;
				}
				startDateCalendar.add(Calendar.DAY_OF_MONTH, 1);
			}
			
			totalefforts=totalWorkingDays*9;
			
			List<UtilizationCollectionMongo> listUtilizationMongoRecords= mongoDBService.getUtilizationMongoCollectionList(startDates.get(i),endDates.get(i), testFactoryName);
			utilizationGlb=utilizationGlb.toLowerCase();
			
			List<String> utilizationGlbList = Arrays.asList(utilizationGlb.split(","));
			float nonProjectEffort=0;
			
			for(UtilizationCollectionMongo utilization :listUtilizationMongoRecords){
				if(!utilization.getActivityType().equalsIgnoreCase("Glb")){
					sumOfEffortSpend=sumOfEffortSpend+utilization.getActivityEffort();
				}
				else if((utilization.getActivityType().equalsIgnoreCase("Glb"))&& (!utilizationGlbList.contains(utilization.getActivityName().toLowerCase())) )
				{
					sumOfEffortSpend=sumOfEffortSpend+utilization.getActivityEffort();
				}else{
					nonProjectEffort=nonProjectEffort+utilization.getActivityEffort();
				}
			}
			
			totalefforts=nonProjectEffort+sumOfEffortSpend;
			if(totalefforts!=0){
				trendUtilizationIndex=(sumOfEffortSpend/totalefforts)*100;
			}if(i == 0){
				utilizationIndex = trendUtilizationIndex;
			}else{
				HashMap<String, String> trendUtilization = new HashMap<String, String>();
				monthCalendar.setTime(startDates.get(i));
				trendUtilization.put("month", months[monthCalendar.get(Calendar.MONTH)]);
				trendUtilization.put("indicator", getIndicatorValue(trendUtilizationIndex,SLA_UTILIZATION_INDEX_POOR_RANGE, SLA_UTILIZATION_INDEX_TARGET_RANGE,"Utilization Index"));
				trendUtilization.put("actualValue", String.format("%.2f", trendUtilizationIndex)   +"");
				utilizationIndexes.add(trendUtilization);
			}
		
			
		}
		
			/* 6. pciScore */
			String pciDateAndMonth="";
			double trendpciScore = 0;
			for(int i = 0; i <= 3; i++){
				trendpciScore = 0;
				List<JSONObject>pciScoreList= mongoDBService.getSingleValueListPCIScroreByDate(endDates.get(i), testFactoryName);
				for(JSONObject pcival:pciScoreList){
					if(pcival!=null && pcival.has("PCI Score")){
						trendpciScore=pcival.getDouble("PCI Score");
						pciDateAndMonth=pcival.getString("Date").substring(10,17);
					}
				}
				if(i == 0){
					pciScore=trendpciScore;
				}else{
					HashMap<String, String> trendPciScoreMap = new HashMap<String, String>();
					monthCalendar.setTime(startDates.get(i));
					trendPciScoreMap.put("month", months[monthCalendar.get(Calendar.MONTH)]);
					trendPciScoreMap.put("indicator", getIndicatorValue((float) trendpciScore,SLA_PCI_SCORE_POOR_RANGE, SLA_PCI_SCORE_TARGET_RANGE,"PCI SCORE"));
					trendPciScoreMap.put("actualValue", String.format("%.2f", trendpciScore)   +"");
					pciScores.add(trendPciScoreMap);
				}
			}
			
			/* csat */
			//String csatAridDateAndMonth = "";
			float  trendcsat=0;
			Boolean isAssigened = false;
			for(int i = 0; i <= 3; i++){
				trendcsat=0;
				List<JSONObject> csatList = mongoDBService.getSingleValueMetricsList(endDates.get(i), testFactoryName, "CSAT");
				isAssigened = false;
				for(JSONObject csatData : csatList){
					trendcsat=0;
					if(csatData != null && csatData.has("CSAT")){
						trendcsat = (float) csatData.getDouble("CSAT");
					}
					
					if(i == 0){
						if(csat == 0){
							csat = trendcsat;
							break;
						}
					}else{
						if(trendcsat != 0 && !isAssigened){
							HashMap<String, String> trendCsatMap = new HashMap<String, String>();
							monthCalendar.setTime(startDates.get(i));
							trendCsatMap.put("month", months[monthCalendar.get(Calendar.MONTH)]);
							trendCsatMap.put("indicator", getIndicatorValue(trendcsat,SLA_CSAT_SCORE_POOR_RANGE, SLA_CSAT_SCORE_TARGET_RANGE,"CSAT SCORE"));
							trendCsatMap.put("actualValue", String.format("%.2f", trendcsat)   +"");
							csatScores.add(trendCsatMap);
							isAssigened = true;
							break;
						}
						
					}
				}
				
				if(i > 0 && !isAssigened){
					HashMap<String, String> trendCsatMap = new HashMap<String, String>();
					monthCalendar.setTime(startDates.get(i));
					trendCsatMap.put("month", months[monthCalendar.get(Calendar.MONTH)]);
					trendCsatMap.put("indicator", getIndicatorValue(trendcsat,SLA_CSAT_SCORE_POOR_RANGE, SLA_CSAT_SCORE_TARGET_RANGE,"CSAT SCORE"));
					trendCsatMap.put("actualValue", String.format("%.2f", trendcsat)   +"");
					csatScores.add(trendCsatMap);
				}
			}
			
			float trendarid = 0;
			for(int i = 0; i <= 3; i++){
				trendarid = 0;
				isAssigened = false;
				List<JSONObject> aridList = mongoDBService.getSingleValueMetricsARIDList(endDates.get(i), testFactoryName);
				for(JSONObject aridData : aridList){
					trendarid = -11111f;
					if(aridData != null && aridData.has("ARID")){
						trendarid = (float) aridData.getDouble("ARID");
					}
					isAssigened = true;
					if(i == 0){
						arid = trendarid;
					}else{
						HashMap<String, String> trendAridMap = new HashMap<String, String>();
						monthCalendar.setTime(startDates.get(i));
						trendAridMap.put("month", months[monthCalendar.get(Calendar.MONTH)]);
						if(trendarid != -11111){
							trendAridMap.put("actualValue", String.format("%.2f", trendarid)+"");
						}else{
							trendAridMap.put("actualValue", "NA");
							trendarid = 1.23f;
						}
						trendAridMap.put("indicator", getIndicatorValue(trendarid,SLA_ARID_POOR_RANGE, SLA_ARID_TARGET_RANGE,"ARID SCORE"));
						aridMapScores.add(trendAridMap);
					}
				}
				
				if(!isAssigened && i == 0){
					arid = -11111f;
				}else if(!isAssigened && i > 0){
					HashMap<String, String> trendAridMap = new HashMap<String, String>();
					monthCalendar.setTime(startDates.get(i));
					trendAridMap.put("month", months[monthCalendar.get(Calendar.MONTH)]);
					trendAridMap.put("actualValue", "NA");
					trendAridMap.put("indicator", getIndicatorValue(trendarid,SLA_ARID_POOR_RANGE, SLA_ARID_TARGET_RANGE,"ARID SCORE"));
					aridMapScores.add(trendAridMap);
				}
			}
			
			/* DPA Index  */
			
			float trendProductQualityByDPA = 0;
			
			for(int i = 0; i <= 3; i++){
				trendProductQualityByDPA = 0;
				productQualityHealth = 1;
				closedDPAs=0;
				totalDPAsCount=0;
				Map<String, Object> dpaWorkbookCounts = mongoDBService.getDPAWorkbookMongoCollectionCounts(startDates.get(i), endDates.get(i), testFactoryName, productName, userProducts);
				
				dpaWorkbookCounts.get("totalDefectsClosedOnPeriod");
				dpaWorkbookCounts.get("totalDefectsTillPeriodEndDate");
				dpaWorkbookCounts.get("defectsClosedBeforePeriodStartDate");
				
				Integer totalDefectsOfPeriod = (Integer)dpaWorkbookCounts.get("totalDefectsTillPeriodEndDate") - (Integer)dpaWorkbookCounts.get("defectsClosedBeforePeriodStartDate");
				if(totalDefectsOfPeriod != 0){
					productQualityHealth = ((Integer)dpaWorkbookCounts.get("totalDefectsClosedOnPeriod")).floatValue() / totalDefectsOfPeriod.floatValue();
					trendProductQualityByDPA = productQualityHealth * 100;
				}
				if(i == 0){
					productQuality = trendProductQualityByDPA;
				}else{
					HashMap<String, String> trendProductQualitesByDPA = new HashMap<String, String>();
					monthCalendar.setTime(startDates.get(i));
					trendProductQualitesByDPA.put("month", months[monthCalendar.get(Calendar.MONTH)]);
					trendProductQualitesByDPA.put("indicator", getIndicatorValue(trendProductQualityByDPA,SLA_PRODUCT_QUALITY_POOR_RANGE, SLA_PRODUCT_QUALITY_TARGET_RANGE,"Product Quality"));
					trendProductQualitesByDPA.put("actualValue", String.format("%.2f", trendProductQualityByDPA)   +"");
					productQualities.add(trendProductQualitesByDPA);
				}
			}
			
			
			healthIndex =(((productQuality)*productQualityWeightage)+ (riskRemovalIndex*riskRemovalIndexWeightage)+ ((100-scheduleVarianceHealth)*svWeightage))/(( productQualityWeightage+ riskRemovalIndexWeightage+ svWeightage));
			/* For month order in UI like JAN FEB MAR*/
			Collections.reverse(scheduleVariances);
			Collections.reverse(productivities);
			Collections.reverse(utilizationIndexes);
			Collections.reverse(productQualities);
			Collections.reverse(riskIndexs);
			Collections.reverse(pciScores);
			Collections.reverse(csatScores);
			Collections.reverse(aridMapScores);
			
			List<JsonMetricsMaster> jsonMetricsMasters = new ArrayList<JsonMetricsMaster>();
			List<JsonMetricsMasterGroup> jsonMetricsMasterGroups = new ArrayList<JsonMetricsMasterGroup>();
			
			JsonMetricsMaster jsonMetricsMaster = new JsonMetricsMaster();
			
			
			jsonMetricsMaster = new JsonMetricsMaster();
			jsonMetricsMaster.setMetricsName("Schedule Variance");
			jsonMetricsMaster.setMetricsType(IDPAConstants.SCHEDULE_VARIANCE);
			jsonMetricsMaster.setTarget(0);
			jsonMetricsMaster.setIndicator(getIndicatorValue(schVar,SLA_SCHEDULE_VARIANCE_POOR_RANGE, SLA_SCHEDULE_VARIANCE_TARGET_RANGE,"Schedule Variance"));
			jsonMetricsMaster.setActualValue(String.format("%.2f", schVar)   +"");
			jsonMetricsMaster.setTargetAvailable(true);
			jsonMetricsMaster.setTargetRange(SLA_SCHEDULE_VARIANCE_TARGET_RANGE_UI);
			jsonMetricsMaster.setTrendMetrics(scheduleVariances);
			jsonMetricsMasters.add(jsonMetricsMaster);
			
			
			
			
			jsonMetricsMaster = new JsonMetricsMaster();
			jsonMetricsMaster.setMetricsName("Product Quality");
			jsonMetricsMaster.setMetricsType(IDPAConstants.PRODUCT_QUALITY);
			jsonMetricsMaster.setTarget(93);
			jsonMetricsMaster.setIndicator(getIndicatorValue(productQuality,SLA_PRODUCT_QUALITY_POOR_RANGE, SLA_PRODUCT_QUALITY_TARGET_RANGE,"Product Quality"));
			jsonMetricsMaster.setActualValue( String.format("%.2f", productQuality)   +"%");
			jsonMetricsMaster.setTargetAvailable(true);
			jsonMetricsMaster.setTargetRange(SLA_PRODUCT_QUALITY_TARGET_RANGE_UI);
			jsonMetricsMaster.setTrendMetrics(productQualities);
			jsonMetricsMasters.add(jsonMetricsMaster);
			
			jsonMetricsMaster = new JsonMetricsMaster();
			jsonMetricsMaster.setMetricsName("Risk Removal Index");
			jsonMetricsMaster.setMetricsType(IDPAConstants.RISK_REMOVAL_INDEX);
			jsonMetricsMaster.setTarget(93);
			jsonMetricsMaster.setIndicator(getIndicatorValue(riskRemovalIndex,SLA_RISK_REMOVAL_INDEX_POOR_RANGE, SLA_RISK_REMOVAL_INDEX__TARGET_RANGE,"Risk Removal Index"));
			jsonMetricsMaster.setActualValue(String.format("%.2f", riskRemovalIndex)  +"%");
			jsonMetricsMaster.setTargetAvailable(true);
			jsonMetricsMaster.setTargetRange(SLA_RISK_REMOVAL_INDEX__TARGET_RANGE_UI);
			jsonMetricsMaster.setTrendMetrics(riskIndexs);
			
			jsonMetricsMasters.add(jsonMetricsMaster);
			
			
			
			JsonMetricsMasterGroup jsonMetricsMasterGroup = new JsonMetricsMasterGroup("Health Index", jsonMetricsMasters, String.format("%.2f", healthIndex) +"%", getIndicatorValue(healthIndex,SLA_HEALTH_INDEX__POOR_RANGE, SLA_HEALTH_INDEX__TARGET_RANGE,"Health Index"),"HealthIndex");
			jsonMetricsMasterGroups.add(jsonMetricsMasterGroup);
			
			jsonMetricsMasters = new ArrayList<JsonMetricsMaster>();
			
			
			
			jsonMetricsMaster = new JsonMetricsMaster();
			jsonMetricsMaster.setMetricsName("Productivity (Day)");
			jsonMetricsMaster.setMetricsType(IDPAConstants.PRODUCTIVITY);
			jsonMetricsMaster.setTarget(93);
			jsonMetricsMaster.setIndicator(getIndicatorValue(productivity,SLA_PRODUCTIVITY_POOR_RANGE, SLA_PRODUCTIVITY_TARGET_RANGE,"Productivity"));
			jsonMetricsMaster.setActualValue(String.format("%.2f", productivity)+"");
			jsonMetricsMaster.setTargetAvailable(false);
			jsonMetricsMaster.setTargetRange(SLA_PRODUCTIVITY_TARGET_RANGE_UI);
			jsonMetricsMaster.setTrendMetrics(productivities);
			jsonMetricsMasters.add(jsonMetricsMaster);
			
			jsonMetricsMaster = new JsonMetricsMaster();
			jsonMetricsMaster.setMetricsName("Utilization Index");
			jsonMetricsMaster.setMetricsType(IDPAConstants.UTILIZATION_INDEX);
			jsonMetricsMaster.setTarget(0);
			jsonMetricsMaster.setIndicator(getIndicatorValue(utilizationIndex,SLA_UTILIZATION_INDEX_POOR_RANGE, SLA_UTILIZATION_INDEX_TARGET_RANGE,"Utilization Index"));
			jsonMetricsMaster.setActualValue(String.format("%.2f", utilizationIndex)+"");
			jsonMetricsMaster.setTargetAvailable(true);
			jsonMetricsMaster.setTargetRange(SLA_UTILIZATION_INDEX_TARGET_RANGE_UI);
			jsonMetricsMaster.setTrendMetrics(utilizationIndexes);
			jsonMetricsMasters.add(jsonMetricsMaster);
			
			
			
			jsonMetricsMasterGroup = new JsonMetricsMasterGroup("Resource Index", jsonMetricsMasters, null, null,"ResourceIndex");
			jsonMetricsMasterGroups.add(jsonMetricsMasterGroup);
			
			
			jsonMetricsMasters = new ArrayList<JsonMetricsMaster>();
						
			jsonMetricsMaster = new JsonMetricsMaster();
			jsonMetricsMaster.setMetricsName("PCI SCORE"+"  ["+pciDateAndMonth+"]");
			jsonMetricsMaster.setMetricsType(IDPAConstants.PCISCORE);
			jsonMetricsMaster.setTarget(0);
			jsonMetricsMaster.setIndicator(getIndicatorValue((float) pciScore,SLA_PCI_SCORE_POOR_RANGE, SLA_PCI_SCORE_TARGET_RANGE,"PCI SCORE"));
			jsonMetricsMaster.setActualValue(String.format("%.2f", pciScore)+"");
			jsonMetricsMaster.setTargetAvailable(true);
			jsonMetricsMaster.setTargetRange(SLA_PCI_SCORE_TARGET_RANGE_UI);
			jsonMetricsMaster.setTrendMetrics(pciScores);
			jsonMetricsMasters.add(jsonMetricsMaster);
			
			jsonMetricsMaster = new JsonMetricsMaster();
			jsonMetricsMaster.setMetricsName("CSAT SCORE [Last Three Values]");
			jsonMetricsMaster.setMetricsType(IDPAConstants.CSAT);
			jsonMetricsMaster.setTarget(0);
			jsonMetricsMaster.setIndicator(getIndicatorValue(csat,SLA_CSAT_SCORE_POOR_RANGE, SLA_CSAT_SCORE_TARGET_RANGE,"CSAT SCORE"));
			jsonMetricsMaster.setActualValue(String.format("%.2f", csat)+"");
			jsonMetricsMaster.setTargetAvailable(true);
			jsonMetricsMaster.setTargetRange(SLA_CSAT_SCORE_TARGET_RANGE_UI);
			jsonMetricsMaster.setTrendMetrics(csatScores);
			jsonMetricsMasters.add(jsonMetricsMaster);
			
			jsonMetricsMaster = new JsonMetricsMaster();
			jsonMetricsMaster.setMetricsName("ARID SCORE [Last Three Values]");
			jsonMetricsMaster.setMetricsType(IDPAConstants.ARID);
			jsonMetricsMaster.setTarget(0);
			if(arid != -11111){
				jsonMetricsMaster.setActualValue(String.format("%.2f", arid)+"");
			}else{
				jsonMetricsMaster.setActualValue("NA");
				arid = 1.23f;				
			}
			jsonMetricsMaster.setIndicator(getIndicatorValue(arid,SLA_ARID_POOR_RANGE, SLA_ARID_TARGET_RANGE,"ARID SCORE"));
			jsonMetricsMaster.setTargetAvailable(true);
			jsonMetricsMaster.setTargetRange(SLA_ARID_TARGET_RANGE_UI);
			jsonMetricsMaster.setTrendMetrics(aridMapScores);
			jsonMetricsMasters.add(jsonMetricsMaster);
			
			jsonMetricsMasterGroup = new JsonMetricsMasterGroup("Project Score", jsonMetricsMasters, null, null,"ProjectScore");
			jsonMetricsMasterGroups.add(jsonMetricsMasterGroup);
			
			jTableResponse = new JTableResponse("OK", jsonMetricsMasterGroups,jsonMetricsMasterGroups.size());
        } catch (Exception e) {
        	jTableResponse = new JTableResponse("Error");
            log.error("JSON ERROR", e);	            
        }
		return jTableResponse;
    }

	@Override
	@Transactional
	public JTableResponse standardDashboardCalculation(Date startDate,Date endDate,String testFactoryName,String productName, UserList user) {
		JTableResponse jTableResponse = null;
		try{
			
			log.info("standard.dashboard.summary.calculation, productName:"+productName);
				
			Integer totalWpTestcaseExePlanCount=0;
			Integer testCaseCompletedCount=0;
			float percetangeForProgramExeVariance=0;
			
			Integer totalDefectsCount=0;
			Integer closedDefectsCount=0;
			float percetangeForProgramQuality=0;
				
			
			Integer defectDuplicateCount=0;
			Integer notReproduceableCount=0;
			Integer referBackCount=0;
			Integer totalBugForQualityIndexCount=0;
			float percentageFordefectQualityIndex=0;
			
			Integer testCasePassedCount=0;
			float percentageForProductConfidence=0;
			
			Date currentDate=endDate;
			ArrayList<String> actvityList = new ArrayList<String>();
			
			
			
			
			List<HashMap<String, String>> progressVariance = new ArrayList<HashMap<String, String>>();
			List<HashMap<String, String>> programQuality = new ArrayList<HashMap<String, String>>();
			List<HashMap<String, String>> defectQuality = new ArrayList<HashMap<String, String>>();
			List<HashMap<String, String>> productQualities = new ArrayList<HashMap<String, String>>();
			List<HashMap<String, String>> productConfidence= new ArrayList<HashMap<String, String>>();
			
			log.info(" startDate - "+startDate);
			boolean isMongoDBAavailable = mongoDBService.checkAvailabiltyOfMongoDB();
			if(!isMongoDBAavailable){
				jTableResponse = new JTableResponse("ERROR", "Check Your MongoDB Availablity");
				log.error("MongoDB Connection Failed" );
				return jTableResponse;
			}
			
			currentDate= DateUtility.getCurrentDate();
		
			Set<ProductUserRole> userProducts = null;
			if(user.getUserRoleMaster().getUserRoleId() != IDPAConstants.ROLE_ID_ADMIN && "ALL".equalsIgnoreCase(testFactoryName) && "ALL".equalsIgnoreCase(productName)){
				userProducts = user.getProductUserRoleSet();
			}else if(user.getUserRoleMaster().getUserRoleId() != IDPAConstants.ROLE_ID_ADMIN && !"ALL".equalsIgnoreCase(testFactoryName) && "ALL".equalsIgnoreCase(productName)){	
				Set<ProductUserRole> userProductsTestfactoryWise = user.getProductUserRoleSet();
				if(userProductsTestfactoryWise != null){
					userProducts = new HashSet<ProductUserRole>();
					for(ProductUserRole productUserRole : userProductsTestfactoryWise){
						if(testFactoryName.equalsIgnoreCase(productUserRole.getProduct().getTestFactory().getTestFactoryName())){
							userProducts.add(productUserRole);
						}
					}
				}
			}
		
			/* 1. Program Execution Progress Variance      - consolidated */
		List<TestCaseExecutionResultMongo> listTestCaseExecutionResult = mongoDBService.getTestCaseExecutionMongoCountByDateFilter(startDate,currentDate,testFactoryName,productName,userProducts);
		totalWpTestcaseExePlanCount=listTestCaseExecutionResult.size();
			
			for(TestCaseExecutionResultMongo testCaseExecution:listTestCaseExecutionResult){
					if(testCaseExecution.getExecutionStatus().equalsIgnoreCase(IDPAConstants.STATUS_COMPLETED)){
						testCaseCompletedCount=testCaseCompletedCount+1;
					}
					/* For Product Confidence */
					if(testCaseExecution.getResult()!=null){
						if(testCaseExecution.getResult().equalsIgnoreCase(IDPAConstants.EXECUTION_RESULT_PASSED)){
							testCasePassedCount=testCasePassedCount+1;
						
						}
					}
				
				}

			if(totalWpTestcaseExePlanCount!=0){
				percetangeForProgramExeVariance=((float)(totalWpTestcaseExePlanCount-testCaseCompletedCount)/totalWpTestcaseExePlanCount*100);
			}else{
				percetangeForProgramExeVariance=0;
			}
			
			
			/* 2.  Program Quality Progress     */
			List<TestCaseDefectsMasterMongo> listDefects=mongoDBService.getTestCaseDefectsMasterMongoList(testFactoryName, productName,userProducts);
			
			totalDefectsCount=listDefects.size();
		
			for (TestCaseDefectsMasterMongo defect : listDefects) {
				
				if(defect.getBugFilingStatusId()==IDPAConstants.TESTCASE_DEFECTS_STATUS_DUPLICATE){
					defectDuplicateCount=defectDuplicateCount+1;
				}
				if(defect.getBugFilingStatusId()==IDPAConstants.TESTCASE_DEFECTS_STATUS_NOT_REPRODUCIBLE){
					notReproduceableCount=notReproduceableCount+1;
				}
				if(defect.getBugFilingStatusId()==IDPAConstants.TESTCASE_DEFECTS_STATUS_REFERBACK){
					referBackCount=referBackCount+1;
				}
				
				if(	"Closed".equalsIgnoreCase(defect.getApprovalStatus())){
					closedDefectsCount=closedDefectsCount+1;
				}
				
			}
			if(totalDefectsCount!=0){
				percetangeForProgramQuality=((float)closedDefectsCount/totalDefectsCount)*100;
			}else{
				percetangeForProgramQuality=100;
			}
			
			
			/* 3. Defect Quality   */
			totalBugForQualityIndexCount=defectDuplicateCount+notReproduceableCount+referBackCount;
		
			
			if(totalDefectsCount!=0){
				percentageFordefectQualityIndex=(float)totalBugForQualityIndexCount/totalDefectsCount;
			}else{
				percentageFordefectQualityIndex=100;
			}
			
			
			/* 4. Product Confidence   */
			if(totalWpTestcaseExePlanCount!=0){
				percentageForProductConfidence=((float)testCasePassedCount/totalWpTestcaseExePlanCount)*100;
			}else{
				percentageForProductConfidence=100;
			}
			
			
			List<JsonMetricsMaster> jsonMetricsMasters = new ArrayList<JsonMetricsMaster>();
			
			JsonMetricsMaster jsonMetricsMaster = new JsonMetricsMaster();
			jsonMetricsMaster.setMetricsName("Schedule Variance");
			jsonMetricsMaster.setTarget(0);
			jsonMetricsMaster.setIndicator(getIndicatorValue(percetangeForProgramExeVariance,4, 0,"Schedule Variance"));
			jsonMetricsMaster.setActualValue(String.format("%.2f", percetangeForProgramExeVariance)   +"%");
			jsonMetricsMaster.setTargetAvailable(true);
			jsonMetricsMaster.setTargetRange(">4%~1-3%~<=0%");
			jsonMetricsMaster.setTrendMetrics(progressVariance);
			jsonMetricsMasters.add(jsonMetricsMaster);
			
			
			jsonMetricsMaster = new JsonMetricsMaster();
			jsonMetricsMaster.setMetricsName("Program Quality Progress");
			jsonMetricsMaster.setTarget(93);
			jsonMetricsMaster.setIndicator(getIndicatorValue(percetangeForProgramQuality,80, 93,"Program Quality Progress"));
			jsonMetricsMaster.setActualValue(String.format("%.2f", percetangeForProgramQuality)   +"%");
			jsonMetricsMaster.setTargetAvailable(true);
			jsonMetricsMaster.setTargetRange("<80%~80-93%~>93%");
			jsonMetricsMaster.setTrendMetrics(programQuality);
			jsonMetricsMasters.add(jsonMetricsMaster);
			
			
			jsonMetricsMaster = new JsonMetricsMaster();
			jsonMetricsMaster.setMetricsName("Defect Quality Index");
			jsonMetricsMaster.setTarget(96);
			jsonMetricsMaster.setIndicator(getIndicatorValue(percentageFordefectQualityIndex,80, 93,"Defect Quality"));
			jsonMetricsMaster.setActualValue(String.format("%.2f", percentageFordefectQualityIndex)   +"%");
			jsonMetricsMaster.setTargetAvailable(true);
			jsonMetricsMaster.setTargetRange("<80%~80-93%~>93%");
			jsonMetricsMaster.setTrendMetrics(defectQuality);
			jsonMetricsMasters.add(jsonMetricsMaster);
			
			jsonMetricsMaster = new JsonMetricsMaster();
			jsonMetricsMaster.setMetricsName("Product Confidence");
			jsonMetricsMaster.setTarget(96);
			jsonMetricsMaster.setIndicator(getIndicatorValue(percentageForProductConfidence,80, 93,"Product Confidence"));
			jsonMetricsMaster.setActualValue(String.format("%.2f", percentageForProductConfidence)   +"%");
			jsonMetricsMaster.setTargetAvailable(true);
			jsonMetricsMaster.setTargetRange("<80%~80-93%~>93%");
			jsonMetricsMaster.setTrendMetrics(productConfidence);
			jsonMetricsMasters.add(jsonMetricsMaster);
			
			
			ArrayList<JsonMetricsMasterGroup> jsonMetricsMasterGroups = new ArrayList<JsonMetricsMasterGroup>();
			JsonMetricsMasterGroup jsonMetricsMasterGroup = new JsonMetricsMasterGroup("", jsonMetricsMasters, "", "","");
			jsonMetricsMasterGroups.add(jsonMetricsMasterGroup);
			
			jTableResponse = new JTableResponse("OK", jsonMetricsMasterGroups,jsonMetricsMasterGroups.size());
			
		}catch(Exception e){
			log.error(e);
		}
		
		return jTableResponse;
		
	}

	@Override
	@Transactional
	public List<DashBoardTabs> getDasboardTabsByEngagementId(Integer engagementId) {
		return dashBoardDAO.getDasboardTabsByEngagementId(engagementId);
	}
	
	
	private Date setDateForMongoDB(Date dateToMongoDB){
		if(dateToMongoDB != null){
			Calendar dateToMongoDBCalendar = Calendar.getInstance();
			dateToMongoDBCalendar.setTime(dateToMongoDB);
			dateToMongoDBCalendar.add(Calendar.MILLISECOND, dateToMongoDBCalendar.getTimeZone().getRawOffset());
			dateToMongoDB = dateToMongoDBCalendar.getTime();
		}
		return dateToMongoDB;
	}

	@Override
	@Transactional
	public void addvisualizationUrls(DashboardVisualizationUrls visualizationUrls) {
		dashBoardDAO.addvisualizationUrls(visualizationUrls);
	}

	@Override
	@Transactional
	public List<DashboardVisualizationUrls> listDashboardVisualization(Integer status, Integer jtStartIndex, Integer jtPageSize) {
		return dashBoardDAO.listDashboardVisualization(status,jtStartIndex,jtPageSize);
	}

	@Override
	@Transactional
	public void updateVisualization(DashboardVisualizationUrls dashboardVisualizationFromUI) {
		 dashBoardDAO.updateVisualization(dashboardVisualizationFromUI);
		
	}
	
	
}
