package com.hcl.atf.taf.service.impl;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.dao.ActivityDAO;
import com.hcl.atf.taf.dao.ActivityTaskDAO;
import com.hcl.atf.taf.dao.ChangeRequestDAO;
import com.hcl.atf.taf.dao.ClarificationTrackerDAO;
import com.hcl.atf.taf.dao.CommonDAO;
import com.hcl.atf.taf.dao.DashboardSLADAO;
import com.hcl.atf.taf.dao.EntityRelationshipCommonDAO;
import com.hcl.atf.taf.dao.EnvironmentDAO;
import com.hcl.atf.taf.dao.ExecutionTypeMasterDAO;
import com.hcl.atf.taf.dao.ProductMasterDAO;
import com.hcl.atf.taf.dao.ProductTeamResourcesDao;
import com.hcl.atf.taf.dao.UserListDAO;
import com.hcl.atf.taf.model.Activity;
import com.hcl.atf.taf.model.ActivityTask;
import com.hcl.atf.taf.model.ActivityWorkPackage;
import com.hcl.atf.taf.model.ChangeRequest;
import com.hcl.atf.taf.model.ClarificationTracker;
import com.hcl.atf.taf.model.Comments;
import com.hcl.atf.taf.model.CustomFieldConfigMaster;
import com.hcl.atf.taf.model.CustomFieldValues;
import com.hcl.atf.taf.model.EntityRelationshipCommon;
import com.hcl.atf.taf.model.EnvironmentCombination;
import com.hcl.atf.taf.model.ExecutionTypeMaster;
import com.hcl.atf.taf.model.ProductBuild;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.dto.ActivitiesDTO;
import com.hcl.atf.taf.model.dto.ProductSummaryForActivityProcessDTO;
import com.hcl.atf.taf.model.json.JsonActivity;
import com.hcl.atf.taf.service.ActivityService;
import com.hcl.atf.taf.service.ActivityTaskService;
import com.hcl.atf.taf.service.CommonService;
import com.hcl.atf.taf.service.CustomFieldService;
import com.hcl.atf.taf.service.EmailService;
import com.hcl.atf.taf.service.EntityRelationshipCommonService;
import com.hcl.atf.taf.service.NotificationService;
import com.hcl.atf.taf.service.ProductListService;
import com.hcl.ilcm.workflow.dao.WorkflowEventDAO;
import com.hcl.ilcm.workflow.dao.WorkflowStatusPolicyDAO;
import com.hcl.ilcm.workflow.model.WorkflowStatusActor;
import com.hcl.ilcm.workflow.service.WorkflowStatusPolicyService;
@Service
public class ActivityServiceImpl implements ActivityService {

	
	private static final Log log = LogFactory
			.getLog(ActivityServiceImpl.class);

	private static final int EnvironmentsCombination = 0;

	@Autowired
	ActivityDAO activityDAO;
	
	@Autowired
	ClarificationTrackerDAO clarificationTrackerDAO;

	
	@Autowired
	private ExecutionTypeMasterDAO executionTypeMasterDAO;
	
	@Autowired
	private ProductListService productListService; 
	
	@Autowired
	private ProductTeamResourcesDao productTeamResourcesDao;
	
	@Autowired
	private DashboardSLADAO dashboardSLADAO;
	
	@Autowired
	ActivityTaskService activityTaskService;
	@Autowired
	ActivityTaskDAO activityTaskDAO;
	@Autowired
	EnvironmentDAO environmentDAO;
	
	@Autowired
	WorkflowStatusPolicyService workflowStatusPolicyService;
	
	@Autowired
	WorkflowStatusPolicyDAO workflowStatusPolicyDAO;
	
	@Autowired
	CommonService commonService;
	
	@Value("#{ilcmProps['TEST_STEP_BATCH_PROCESSING_COUNT']}")
    private String activitiesMaxBatchCount;
	@Autowired
	ChangeRequestDAO changeRequestDAO;
	
	@Autowired
	ProductMasterDAO productMasterDAO;
	
	@Autowired
	private UserListDAO userListDAO;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private NotificationService notificationService;
	
	@Autowired
	private WorkflowEventDAO workflowEventDAO;
	
	@Value("#{ilcmProps['EMAIL_ALERT_NOTIFICATION']}")
    private String emailNotification;
	
	@Value("#{ilcmProps['WEEKLY_REPORT_FROM_MAIL_ADDRESS']}")
    private String fromMail;
	
	@Value("#{ilcmProps['WEEKLY_REPORT_EMAIL_TO_ADDRESS']}")
	private String toMail;
	
	@Value("#{ilcmProps['WEEKLY_REPORT_EMAIL_CC_ADDRESS']}")
    private String ccMail;
	
	@Value("#{ilcmProps['WEEKLY_REPORT_STATUS_CATEGORIES']}")
    private String statusCagories;
	
	
	@Autowired
	private  CommonDAO commonDAO;
	@Autowired
	private EntityRelationshipCommonService entityRelationshipCommonService;
	@Autowired
	private EntityRelationshipCommonDAO entityRelationshipCommonDAO;

	
	
	@Override
	@Transactional
	public List<Activity> activityLists() {
		List<Activity> activitiesList = null;
		List<JsonActivity> jsonactivity = new ArrayList<JsonActivity>();
		try {
			activitiesList = activityDAO.activitylists();
			for(Activity wr: activitiesList){
				jsonactivity.add(new JsonActivity(wr));	
			}
		} catch (Exception e) {
			log.error("ERROR  ",e);
		}
		return activitiesList;
	}

	@Override
	public void addActivity(Activity addActivity) {
		activityDAO.addActivity(addActivity);
		
	}

	@Override
	public void updateActivity(Activity updateActivity) {
		activityDAO.updateActivity(updateActivity);
		
	}

	@Override
	@Transactional
	public String deleteActivity(int activityId, String referencedTableName, String referencedColumnName) {
		return activityDAO.deleteActivity(activityId, referencedTableName, referencedColumnName);
	}



	@Override
	@Transactional
	public List<JsonActivity> listActivitiesByActivityWorkPackageId(Integer activityWorkPackageId,Integer initializationLevel,Integer isActive) {
		
		List<Activity> activitieslist = null;
		List<JsonActivity> jsonActivitieslist = new ArrayList<JsonActivity>();
		try {
			activitieslist = activityDAO.listActivitiesByActivityWorkPackageId(activityWorkPackageId,initializationLevel,isActive);
			if(activitieslist != null && activitieslist.size()>0){
				for(Activity wr: activitieslist){
					jsonActivitieslist.add(new JsonActivity(wr));	
				}
			}
		} catch (Exception e) {
			log.error("ERROR  ",e);
		}
		return jsonActivitieslist;
	}

	@Override
	@Transactional
	public Activity getActivityById(Integer activityId,
			Integer initializationLevel) {
		return activityDAO.getActivityById(activityId,initializationLevel);
	}

	@Override
	@Transactional
	public Activity getByActivityId(int activityId) {
				return activityDAO.getByActivityId(activityId);
	}
	
	@Override
	public int getUnMappedEnvironmentCombiListCountOfActivityByActivityId(
			int productId, int activityId, int initialisationLevel) {

				return activityDAO.getUnMappedEnvironmentCombiListCountOfActivityByActivityId(productId, activityId,initialisationLevel);
	}

	@Override
	public List<Object[]> getUnMappedEnvironmentCombiListByActivityId(
			int productId, int activityId, int jtStartIndex, int jtPageSize,
			int initialisationLevel) {
		return activityDAO.getUnMappedEnvironmentCombiListByActivityId(productId, activityId, jtStartIndex, jtPageSize);
	}

	@Override
	public EnvironmentCombination updateEnvironmentCombiToActivityOneToMany(
			Integer enviCombiId, Integer activityId, String maporunmap) {
		return activityDAO.updateEnvironmentCombiToActivityOneToMany(enviCombiId, activityId, maporunmap);
	}

	@Override
	public List<Object[]> getMappedEnvCombiByActivityId(int productId,
			int activityId, int jtStartIndex, int jtPageSize, int initialisationLevel) {
		return activityDAO.getECListByProductFeatureId(productId,  activityId,jtStartIndex, jtPageSize,initialisationLevel);
	}



	@Override
	public int getUnMappedRcnByActivityId(int activityId, int i, int productId) {
		return activityDAO.getUnMappedRcnByActivityId(activityId,i, productId);
	}


	@Override
	public List<Object[]> getMappedRcnByActivityId(int intactivityId, int i,
			int j, int k) {
		return activityDAO.getMappedRcnByActivityId(intactivityId, i,j,k);
	}

	@Override
	public ChangeRequest updateChangeRequestToActivityOneToMany(Integer changeRequestId,
			Integer activityId, String maporunmap) {
		return activityDAO.updateChangeRequestToActivityOneToMany(changeRequestId, activityId, maporunmap);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<JsonActivity> listActivities(Map<String, String> searchString,Integer engagementId,Integer productId,
			Integer productVersionId, Integer productBuildId,
			Integer activityWorkPackageId, Integer initializationLevel,
			Integer isActive,HttpServletRequest req,String statusType,Integer startIndex,Integer pageSize ) {
		List<Activity> activitieslist = null;
		List<JsonActivity> jsonActivitiesReturnList = new ArrayList<JsonActivity>();
		List<JsonActivity> jsonActivitieslist = new ArrayList<JsonActivity>();
		try {
			UserList user = (UserList) req.getSession().getAttribute("USER");
			activitieslist = activityDAO.listActivities(searchString,engagementId,productId,productVersionId, productBuildId, activityWorkPackageId,initializationLevel,isActive,user,statusType,startIndex,pageSize);
			if(activitieslist != null && activitieslist.size()>0){
				Date modifiedAfterDate = commonService.getModifiedAfterDate((Date)req.getSession().getAttribute("userLoginTime"), (Date)req.getSession().getAttribute("userLogoutTime"));
				for(Activity wr: activitieslist){
					JsonActivity jsonActivity = new JsonActivity(wr);
					jsonActivitieslist.add(jsonActivity);	
				}
				workflowStatusPolicyService.setInstanceIndicators(engagementId,productId,activityWorkPackageId, IDPAConstants.ENTITY_ACTIVITY_TYPE, jsonActivitieslist, IDPAConstants.ENTITY_ACTIVITY_ID,user, modifiedAfterDate, null, null,!IDPAConstants.ENABLE_PENDING_WITH_COLUMN);
			}
			
			
			Collections.sort(jsonActivitieslist, new Comparator<JsonActivity>() {
				@Override
				public int compare(JsonActivity activity, JsonActivity activity2) {
					if(activity.getRemainingHours() != null && activity2.getRemainingHours() != null){
						return activity.getRemainingHours().compareTo(activity2.getRemainingHours());
					}else {
						return 0;
					}
				}
			});
			
			jsonActivitiesReturnList = (List<JsonActivity>) workflowStatusPolicyService.getPaginationListFromFullList(jsonActivitieslist, startIndex, pageSize);
		} catch (Exception e) {
			log.error("ERROR  ",e);
		}
		return jsonActivitiesReturnList;
	}

	@Override
	public int addActivitiesBulk(List<Activity> activitiesList) {
		return activityDAO.addBulk(activitiesList, Integer.parseInt(activitiesMaxBatchCount));
	}

	@Override
	@Transactional
	public List<String> getExistingActiviesName(ActivityWorkPackage activityWorkPackage,
			ProductBuild productBuild) {
		return activityDAO.getExistingActiviesName(activityWorkPackage,productBuild);
	}

	@Override
	@Transactional
	public ExecutionTypeMaster getExecutionTypeByExecutionTypeName(
			String executionTypeName) {
		// TODO Auto-generated method stub
		return executionTypeMasterDAO.getExecutionTypeByExecutionTypeName(executionTypeName);
	}

	@Override
	public List<Object[]> getUnMappedRcnByActivityId(int productId,
			int activityId, int jtStartIndex, int jtPageSize, int i) {
		return activityDAO.getUnMappedRcnByActivityId(productId,activityId, jtStartIndex, jtPageSize);
	}
	
	
	@Override
	public Integer getActivitiesCount(Map<String,String> searchString,Integer engagementId, Integer productId, Integer productVersionId, Integer productBuildId, Integer activityWorkPackageId, Integer initializationLevel, Integer isActive){
		return activityDAO.getActivitiesCount(searchString,engagementId,productId, productVersionId, productBuildId, activityWorkPackageId, initializationLevel, isActive);
	}
	
	@Override
	@Transactional
	public float getScheduleVarianceforProduct(Integer productId) {
		Float schVar = 0.0F;
		try{
			List<Activity> activityCollections = dashboardSLADAO.getScheduleVarianceforProduct(productId);
			
			log.info("Schedule Variance list size "+activityCollections.size());
			try{
				Date plannedStartDate = null;
				Date plannedEndDate = null;
				Date actualEndDate = null;
				int counter = 0;
				for (Activity activityCollection : activityCollections) {
					if(counter == 0){
						actualEndDate = activityCollection.getActualEndDate();
					}else if(counter == 1){
						plannedEndDate = activityCollection.getPlannedEndDate();
					}else if(counter == 2){
						plannedStartDate = activityCollection.getPlannedStartDate();
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
					log.info("schVar "+schVar);
				}
			}catch(Exception ex){
				log.error("ERROR  ",ex);
			}
			
			return schVar;
		}catch (Exception e) {
			log.error("Error while fetching activity", e);
			return schVar;
		}
	}
	

	
	

	@SuppressWarnings("null")
	@Override
	@Transactional
	public ProductSummaryForActivityProcessDTO getProductActivitySummaryDetails(Integer productId) {
		
		List<ClarificationTracker>listOfClarification=new ArrayList<ClarificationTracker>();
		try {
			
			ProductSummaryForActivityProcessDTO productSummaryActivityProcess=new ProductSummaryForActivityProcessDTO();
			
			Integer totalWpkCont=0;
			Integer activeWorkPackage=0;
			Integer totalActivity = 0;
			Integer openActivity = 0;
			Integer onHoldActivity = 0;
			Integer completedActivity = 0;
			Integer abortedActivity = 0;
			Integer totalUsers = 0;
			Integer managers = 0;
			Integer testLeads = 0;
			Integer engg = 0;
			String productName="";
			Integer status=0;
			float totalBugs=0;
			float closedBugs=0;
			float productQuality=0;
			float productivity=0;
			float requiredRunRate=0;
			float plannedActivitySize=0;
			float plannedActivitySizeForRemainig=0;
			float totalWorkingDays=0;
			Integer remainingWorkingDays=0;
			float totalefforts=0;
			float remainingEfforts=0;
			float schVariance=0;
			
			ProductMaster product=productListService.getProductDetailsById(productId);
			productName=product.getProductName();
			status=product.getStatus();
			
			List<Object[]> activityWpCountDetails=activityDAO.getActivityWorkPackageCountByProductId(productId);
			if(activityWpCountDetails!=null){
				for(Object[] actwpkCount:activityWpCountDetails){
					if(actwpkCount!=null && actwpkCount.length>=2){
						Integer activityStatus=(int) actwpkCount[0];
						if(activityStatus==1){
							activeWorkPackage=((BigInteger) actwpkCount[1]).intValue();
						}
						totalWpkCont+=((BigInteger) actwpkCount[1]).intValue();
					}
				}
			}
			
			List<Object[]> activityCountDetails=activityDAO.getActivityCountByProductId(productId);
			
			log.info("activityCountDetails "+activityCountDetails.size());
			if(activityCountDetails!=null){
				for(Object[] actityCount:activityCountDetails){
					if(actityCount!=null && actityCount.length>=2){
						String activityStatus = (String) actityCount[1];
						if(activityStatus != null && activityStatus.toLowerCase().contains("hold")){
							onHoldActivity += ((BigInteger) actityCount[0]).intValue();
						}else if(activityStatus != null && (activityStatus.toLowerCase().contains("complete") || activityStatus.toLowerCase().contains("Deliver"))){
							completedActivity += ((BigInteger) actityCount[0]).intValue();
						}else if(activityStatus != null && (activityStatus.toLowerCase().contains("Abort"))){
							abortedActivity += ((BigInteger) actityCount[0]).intValue();
						}else{
							openActivity += ((BigInteger) actityCount[0]).intValue();
						}
						totalActivity+=((BigInteger) actityCount[0]).intValue();
					}
				}
			}
			
			Map<Integer, Integer> prtCountByRole = productTeamResourcesDao.getProductTeamResourcesCountByRole(productId);
			if(prtCountByRole != null && prtCountByRole.size() >0){
				for(Map.Entry<Integer, Integer> entry : prtCountByRole.entrySet()){
					Integer roleId = entry.getKey();
					Integer roleCount = entry.getValue();
					totalUsers += roleCount;
					if(roleId == IDPAConstants.ROLE_ID_TEST_MANAGER){
						managers = roleCount;
					}else if(roleId == IDPAConstants.ROLE_ID_TEST_LEAD){
						testLeads = roleCount;
					}else if(roleId == IDPAConstants.ROLE_ID_TESTER){
						engg = roleCount;
					}
				}
			}
			
			
			listOfClarification=clarificationTrackerDAO.listClarificationTrackersByProductId(productId);
			totalBugs=listOfClarification.size();
			log.info("totalBugs  "+totalBugs);
			if(listOfClarification.size()!=0){
				for(ClarificationTracker tracker:listOfClarification){
					if(tracker.getEntityStatus() !=null && tracker.getEntityStatus().getEntityStatusName()!=null && tracker.getEntityStatus().getEntityStatusName().equalsIgnoreCase("completed")){
						closedBugs=closedBugs+1;
					}
				}
			}
			
			if(totalBugs!=0){
				productQuality= (closedBugs/totalBugs);
			}else{
				productQuality=100;
			}
			
			List<Activity> listOfActivity=new ArrayList<Activity>();
			listOfActivity=activityDAO.getActivityListByProductId(productId);
		Date currentDate=new Date();
			for(Activity activity:listOfActivity){
				if(activity.getStatusCategory()!=null &&activity.getStatusCategory().getStatusCategoryName()!=null &&  activity.getStatusCategory().getStatusCategoryName().equalsIgnoreCase("Completed")){
					if(activity.getPlannedActivitySize()!=null){
						plannedActivitySize=plannedActivitySize+activity.getPlannedActivitySize();
					}else{
						plannedActivitySize=plannedActivitySize+1;
					}
					
					Calendar startDateCalendar = Calendar.getInstance();
					startDateCalendar.setTime(DateUtility.getDateFromDateTime(activity.getPlannedStartDate()));
					if(activity.getPlannedEndDate().before(currentDate)){
						currentDate=activity.getPlannedEndDate();
					}
					Calendar todayDateCalendar = Calendar.getInstance();
					todayDateCalendar.setTime(DateUtility.getDateFromDateTime(currentDate));
					
					while(startDateCalendar.before(todayDateCalendar)){
						if(startDateCalendar.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && startDateCalendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY){
							totalWorkingDays = totalWorkingDays+1;
						}
						startDateCalendar.add(Calendar.DAY_OF_MONTH, 1);
					}
				}else{
					if(activity.getPlannedActivitySize()!=null){
						plannedActivitySizeForRemainig=plannedActivitySizeForRemainig+activity.getPlannedActivitySize();
					}else{
						plannedActivitySizeForRemainig=plannedActivitySizeForRemainig+1;
					}
					
					Calendar startDateCalendar = Calendar.getInstance();
					startDateCalendar.setTime(DateUtility.getDateFromDateTime(currentDate));
					
					Calendar todayDateCalendar = Calendar.getInstance();
					if(activity.getPlannedEndDate()!=null){
						todayDateCalendar.setTime(DateUtility.getDateFromDateTime(activity.getPlannedEndDate()));
					}else{
						todayDateCalendar.setTime(DateUtility.getDateFromDateTime(currentDate));
					}
					
					while(startDateCalendar.before(todayDateCalendar)){
						if(startDateCalendar.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && startDateCalendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY){
							remainingWorkingDays = remainingWorkingDays+1;
						}
						startDateCalendar.add(Calendar.DAY_OF_MONTH, 1);
					}
				
				}
				
			}
			totalefforts=totalWorkingDays/9;
			if(totalefforts!=0){
				productivity=  ((plannedActivitySize/totalefforts));
				
			}else{
				productivity=0;
			}
			
			remainingEfforts=remainingWorkingDays;
			if(remainingEfforts!=0){
				requiredRunRate=((plannedActivitySizeForRemainig/remainingEfforts));
			}else{
				requiredRunRate=0;
			}
			
			
			schVariance=getScheduleVarianceforProduct(productId);
			
			
			productSummaryActivityProcess.setProductName(productName);
			productSummaryActivityProcess.setStatus(status);
			productSummaryActivityProcess.setActiveWorkPackageCount(activeWorkPackage);
			productSummaryActivityProcess.setTotalWorkPackageCount(totalWpkCont);
			productSummaryActivityProcess.setTotalActivityCount(totalActivity);
			productSummaryActivityProcess.setOpenActivityCount(openActivity);
			productSummaryActivityProcess.setOnHoldActivityCount(onHoldActivity);
			productSummaryActivityProcess.setCompletedActivityCount(completedActivity);
			productSummaryActivityProcess.setTotalUsersCount(totalUsers);
			productSummaryActivityProcess.setTotalMangersCount(managers);
			productSummaryActivityProcess.setTotalLeadsCount(testLeads);
			productSummaryActivityProcess.setTotalEngineersCount(engg);
			productSummaryActivityProcess.setProductQuality(String.format("%.2f", productQuality));
			productSummaryActivityProcess.setProductivity(String.format("%.2f", productivity));
			productSummaryActivityProcess.setRequiredRunRate(String.format("%.2f", requiredRunRate));
			productSummaryActivityProcess.setScheduleVariance(String.format("%.2f", schVariance));
			
			
			
			
			return productSummaryActivityProcess;
			
		} catch (Exception e) {
			log.error("Error while fetching activity", e);
			return null;
		}

	}

	@Override
	@Transactional
	public Integer countAllActivity(Date startDate, Date endDate) {
		return activityDAO.countAllActivity(startDate,endDate);
	}
	
	@Override
	@Transactional
	public boolean getActivityByWPIdandActivityName(Integer activityWorkPackageId,String activityName){
			return activityDAO.getActivityByWPIdandActivityName(activityWorkPackageId, activityName);
	}

	@Override
	@Transactional
	public Integer countAllActivityWorkpackage(Date startDate, Date endDate) {
		return activityDAO.countAllActivityWorkpackage(startDate, endDate);
	}

	@Override
	public List<JsonActivity> listTotalActivitiesByUserId(int userId,Integer userRoleId,int jtStartIndex,int jtPageSize) {
	

		
		List<Activity> totalActivitieslist = null;
		List<JsonActivity> jsonActivitieslist = new ArrayList<JsonActivity>();
		try {
			if(userRoleId!=null && userRoleId==IDPAConstants.ROLE_ID_PQA_REVIEWER){
				totalActivitieslist=activityDAO.listActiveActivities();
			
			}else{
				totalActivitieslist = activityDAO.listActivitiesByActivityWorkPackageIdAndLoginId(0, 1, 1,userId, false);
			}
			
			if(totalActivitieslist != null && totalActivitieslist.size()>0){
				for(Activity wr: totalActivitieslist){
					jsonActivitieslist.add(new JsonActivity(wr));	
				}
			}
		} catch (Exception e) {
			log.error("ERROR  ",e);
		}
		return jsonActivitieslist;
	}
	
	@Override
	@Transactional
	public ActivitiesDTO getMyActivitySummary(Integer activityId,Integer userId){
		
		ActivitiesDTO activitiesDTO = new ActivitiesDTO();
		ArrayList<String> ecList= new ArrayList<String>();
		ArrayList<String> ctList= new ArrayList<String>();
		ArrayList<String> crList= new ArrayList<String>();
 		int taskOpenStatusValue = 0;
		int taskOnHoldStatusRecords = 0;
		int taskCompletedStatusRecords = 0;
		int taskInprogressStatusRecords = 0;
		int loggedInUserTaskCount = 0;		
		int totalTaskCount = 0;
		StringBuffer ecString=new StringBuffer(""); 
		StringBuffer ctString=new StringBuffer(""); 
		StringBuffer crString=new StringBuffer(""); 
	
	   
		Activity activity =activityDAO.getActivityById(activityId,1);
		ActivityWorkPackage activityWorkPackage = activity.getActivityWorkPackage();
		ActivityTask activityTask = activityTaskDAO.getActivityTaskById(activityId, 1);
		totalTaskCount= activityTaskDAO.getActivitytaskCountByActivityId(activityId);
		taskOpenStatusValue=activityTaskDAO.getActivitytaskCountByStatus(activityId,userId,IDPAConstants.STATUS_OPEN_CODE);
		taskOnHoldStatusRecords=activityTaskDAO.getActivitytaskCountByStatus(activityId,userId,IDPAConstants.STATUS_HOLD_CODE);
		taskCompletedStatusRecords=activityTaskDAO.getActivitytaskCountByStatus(activityId,userId,IDPAConstants.STATUS_COMPLETED_CODE);
		taskInprogressStatusRecords=activityTaskDAO.getActivitytaskCountByStatus(activityId,userId,IDPAConstants.STATUS_INPROG_CODE);
		loggedInUserTaskCount=activityTaskDAO.getActivitytaskCountByActivityIdAndUserId(activityId,userId);
		
		List<EnvironmentCombination> ec = environmentDAO.getEnvironmentsCombinationsMappedToActivity(activityId,0);
		List<ClarificationTracker> ct = clarificationTrackerDAO.listClarificationTrackersByActivityId(IDPAConstants.ACTIVITY_ENTITY_MASTER_ID, activityId,1);
		List<ChangeRequest> cr = changeRequestDAO.listChangeRequestByEntityTypeAndInstanceIds(IDPAConstants.ACTIVITY_ENTITY_MASTER_ID, activityId);
		Integer changeRequestCount = changeRequestDAO.getcountOfChangeRequestsByEntityTypeAndInstanceIds(IDPAConstants.ACTIVITY_ENTITY_MASTER_ID, activityId);
		Integer clarificationCount = clarificationTrackerDAO.getcountOfClarificationByEntityTypeAndInstanceIds(IDPAConstants.ACTIVITY_ENTITY_MASTER_ID, activityId);
		for(EnvironmentCombination ecArrayList : ec){
			ecList.add(ecArrayList.getEnvironmentCombinationName());			
		}
		ecString.append(ecList);
		
		for(ClarificationTracker ctArrayList : ct){
			ctList.add(ctArrayList.getClarificationTitle());			
		}
		ctString.append(ctList);
		
		for(ChangeRequest crArrayList : cr){
			crList.add(crArrayList.getChangeRequestName());			
		}
		crString.append(crList);

		activitiesDTO.setActivity(activity);
		activitiesDTO.setProductMaster(activityWorkPackage.getProductBuild().getProductVersion().getProductMaster());
		activitiesDTO.setLoggedInUserTaskCount(loggedInUserTaskCount);
		activitiesDTO.setTaskInProgressStatusRecords(taskInprogressStatusRecords);
		activitiesDTO.setTaskOpenStatusValue(taskOpenStatusValue);
		activitiesDTO.setTaskOnHoldStatusRecords(taskOnHoldStatusRecords);
		activitiesDTO.setTaskCompletedStatusRecords(taskCompletedStatusRecords);
		activitiesDTO.setTotalTaskCount(totalTaskCount);	
		activitiesDTO.setActivityName(activity.getActivityName());
		activitiesDTO.setActivityWorkPackage(activityWorkPackage);
		
		if(activity.getProductFeature() !=null){
			activitiesDTO.setProductFeatureId(activity.getProductFeature().getProductFeatureId());
		}else{
			activitiesDTO.setProductFeatureId(0);
		}
		
		activitiesDTO.setProductFeatureName(activity.getProductFeature() !=null ? activity.getProductFeature().getProductFeatureName():"-");
		activitiesDTO.setActivityMasterName(activity.getActivityMaster() !=null ?activity.getActivityMaster().getActivityMasterName():"-");
		if(activity.getActivityMaster() != null){
			activitiesDTO.setActivityMasterId(activity.getActivityMaster().getActivityMasterId());
		}else{
			activitiesDTO.setActivityMasterId(0);
		}
		if(activity.getActivityMaster() != null){
			activitiesDTO.setWeightage(activity.getActivityMaster().getWeightage() !=null ?activity.getActivityMaster().getWeightage():0);
		}
		if(activity.getAssignee()!=null){
			activitiesDTO.setAssigneeId(activity.getAssignee().getUserId());
			activitiesDTO.setAssigneeName(activity.getAssignee().getLoginId());			
		}else{			
			activitiesDTO.setAssigneeName("-");
		}
	
		if(activity.getReviewer()!=null){
			activitiesDTO.setReviewerName(activity.getReviewer().getLoginId());
			activitiesDTO.setReviewerId(activity.getReviewer().getUserId());
		}else{
			activitiesDTO.setReviewerName("-");			
		}
		
		if(activity.getRemark() != null){
		activitiesDTO.setRemark(activity.getRemark());
		}else{
			activitiesDTO.setRemark("-");
		}
		activitiesDTO.setCategoryName(activity.getCategory() !=null ?activity.getCategory().getName(): "-");
		if(activity.getCategory() != null){
			activitiesDTO.setCategoryId(activity.getCategory().getExecutionTypeId());
		}else{
			activitiesDTO.setCategoryId(0);
		}
		
		if(ecString!= null){
		activitiesDTO.setEnvironmentCombinations(ecString.toString());
		}else{
			activitiesDTO.setEnvironmentCombinations(null);
		}
		if(ctString!= null){
			activitiesDTO.setClarificationNumber(ctString.toString());
			}else{
				activitiesDTO.setClarificationNumber(null);
			}
		if(crString!= null){
			activitiesDTO.setChangeRequest(crString.toString());
			}else{
				activitiesDTO.setChangeRequest(null);
			}
		if(activity.getStatusCategory()!= null){
		activitiesDTO.setStatusCategoryId(activity.getStatusCategory().getStatusCategoryId());
		activitiesDTO.setStatusCategoryName(activity.getStatusCategory().getStatusCategoryName());	
		}
		activitiesDTO.setIsActive(activity.getIsActive());		
		activitiesDTO.setBaselineActivitySize(activity.getBaselineActivitySize());
		activitiesDTO.setPlannedActivitySize(activity.getPlannedActivitySize());
		activitiesDTO.setActualActivitySize(activity.getActualActivitySize());
		
		if(activity.getActivityTrackerNumber() != null){
		activitiesDTO.setActivityTrackerNumber(activity.getActivityTrackerNumber());
		}else{
			activitiesDTO.setActivityTrackerNumber("0");
		}
		activitiesDTO.setPriorityName(activity.getPriority() !=null ? activity.getPriority().getExecutionPriorityName():"");
		if(activity.getPriority() != null){
			activitiesDTO.setPriorityId(activity.getPriority().getExecutionPriorityId());
		}else{
			activitiesDTO.setPriorityId(1);
		}
		activitiesDTO.setStatusCategoryName(activity.getStatusCategory() !=null ?activity.getStatusCategory().getStatusCategoryName():"");
		if(activity.getCreatedBy()!=null){		
			activitiesDTO.setCreatedByName(activity.getCreatedBy().getLoginId());
			activitiesDTO.setCreatedById(activity.getCreatedBy().getUserId());
		}else{			
			activitiesDTO.setCreatedByName("-");			
		}
		if(activity.getModifiedBy()!=null){	
			activitiesDTO.setModifiedByName(activity.getModifiedBy().getLoginId());
			activitiesDTO.setModifiedById(activity.getModifiedBy().getUserId());
		
		}else{
			activitiesDTO.setModifiedByName("-");			
		}	
				
		if(activity.getCreatedDate() != null) {
			activitiesDTO.setCreatedDate(DateUtility.dateToStringWithSeconds1(activity.getCreatedDate()));
		}
		if(activity.getModifiedDate() != null) {
			activitiesDTO.setModifiedDate(DateUtility.dateformatWithOutTime(activity.getModifiedDate()));
		}
		
		activitiesDTO.setBaselineStartDate(DateUtility.dateformatWithOutTime(activity.getBaselineStartDate() !=null ?activity.getBaselineStartDate():new Date()));
		activitiesDTO.setBaselineEndDate(DateUtility.dateformatWithOutTime(activity.getBaselineEndDate() != null ? activity.getBaselineEndDate(): new Date()));
		activitiesDTO.setPlannedStartDate(DateUtility.dateformatWithOutTime(activity.getPlannedStartDate() !=null ?activity.getPlannedStartDate():new Date()));
		activitiesDTO.setPlannedEndDate(DateUtility.dateformatWithOutTime(activity.getPlannedEndDate() != null ? activity.getPlannedEndDate(): new Date()));
		activitiesDTO.setActualStartDate(DateUtility.dateformatWithOutTime(activity.getActualStartDate() !=null ?activity.getActualStartDate():new Date()));
		activitiesDTO.setActualEndDate(DateUtility.dateformatWithOutTime(activity.getActualEndDate() != null ? activity.getActualEndDate(): new Date()));
		activitiesDTO.setProductName(activity.getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductName());
		activitiesDTO.setEngagementId(activity.getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getTestFactory().getTestFactoryId());
		activitiesDTO.setEngagementName(activity.getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getTestFactory().getTestFactoryName());
		activitiesDTO.setChangeRequestCount(changeRequestCount);
		
		activitiesDTO.setClarificationCount(clarificationCount);
		if(activity.getComplexity() != null){
			activitiesDTO.setComplexity(activity.getComplexity());
		}else{
			activitiesDTO.setComplexity("-");
		}			
		activitiesDTO.setPercentageCompletion(activity.getPercentageCompletion());		
		
		if(activity.getTotalEffort() != null){
			activitiesDTO.setTotalEffort(activity.getTotalEffort());
		}else{
			activitiesDTO.setTotalEffort(0);
		}		
		
		activitiesDTO.setPlannedEffort(activity.getPlannedEffort());
		activitiesDTO.setBaselineEffort(activity.getBaselineEffort());
		
		if(activity.getWorkflowStatus() != null) {
			activitiesDTO.setStatusId(activity.getWorkflowStatus().getWorkflowStatusId());
			activitiesDTO.setStatusName(activity.getWorkflowStatus().getWorkflowStatusName());
			activitiesDTO.setStatusDisplayName(activity.getWorkflowStatus().getWorkflowStatusDisplayName());
			activitiesDTO.setWorkflowStatus(activity.getWorkflowStatus());
						
			if(activity.getWorkflowStatus().getWorkflowStatusCategory() != null){
				activitiesDTO.setWorkflowStatusCategoryName("["+activity.getWorkflowStatus().getWorkflowStatusCategory().getWorkflowStatusCategoryName()+"]");
			}else{
				activitiesDTO.setWorkflowStatusCategoryName("");
			}
			
			if(activity.getWorkflowStatus().getWorkflow() != null) {
				activitiesDTO.setWorkflowId(activity.getWorkflowStatus().getWorkflow().getWorkflowId());
			} else {
				activitiesDTO.setWorkflowId(0);
			}
		}		
		
		if(activity.getLifeCycleStage() != null){
			activitiesDTO.setLifeCycleStageName(activity.getLifeCycleStage().getWorkflowStatusDisplayName());
			activitiesDTO.setLifeCycleStageId(activity.getLifeCycleStage().getWorkflowStatusId());
		}else{
			activitiesDTO.setLifeCycleStageName("-");
			activitiesDTO.setLifeCycleStageId(0);
		}
		activitiesDTO.setBaselineUnit(activity.getBaselineUnit());
		activitiesDTO.setPlannedUnit(activity.getPlannedUnit());
		activitiesDTO.setActualUnit(activity.getActualUnit());
		return activitiesDTO;
	}
	@Override
	@Transactional
	public ActivityWorkPackage getActivityWorkPackageByActivityId(Integer activityId,Integer initializationLevel) {
		return activityDAO.getActivityWorkPackageByActivityId(activityId, initializationLevel);
	}
	

	@Override
	@Transactional
	public Activity getActivityByWorkpackageIdAndActivityName(Integer activityWorkPackageId,String activityName){
		return activityDAO.getActivityByWorkpackageIdAndActivityName(activityWorkPackageId, activityName);
	}

	@Override
	@Transactional
	public int getActivityWorkpackageStatusCategoryIdByActivityStatusCategory(Integer activityWorkPackageId) {
		return activityDAO.getActivityWorkpackageStatusCategoryIdByActivityStatusCategory(activityWorkPackageId);
	}
	
	@Override
	@Transactional
	public Activity getActivityByName(String activityName) {
		return activityDAO.getActivityByName(activityName);
	}
	@Override
	@Transactional
	public List<Activity> listAllActivities(Map<String, String> searchString,Integer productId,
			Integer productVersionId, Integer productBuildId,
			Integer activityWorkPackageId, Integer initializationLevel,
			Integer isActive) {
		List<Activity> activitieslist = null;
		List<JsonActivity> jsonActivitieslist = new ArrayList<JsonActivity>();
		try {
			activitieslist = activityDAO.listAllActivities(searchString,productId,productVersionId, productBuildId, activityWorkPackageId,initializationLevel,isActive);
			
		} catch (Exception e) {
			log.error("ERROR  ",e);
		}
		return activitieslist;
	}
	@Override
	@Transactional
	public List<Activity> listAllActivitiesByActivityWorkPackageId(Integer activityWorkPackageId,Integer initializationLevel,Integer isActive) {
		
		List<Activity> activitieslist = null;
		try {
			activitieslist = activityDAO.listActivitiesByActivityWorkPackageId(activityWorkPackageId,initializationLevel,isActive);
			
		} catch (Exception e) {
			log.error("ERROR  ",e);
		}
		return activitieslist;
	}

	@Override
	@Transactional
	public List<Activity> getActivityByActivtyWorkpackageId(Integer awpId) {
		return activityDAO.getActivityByActivtyWorkpackageId(awpId);
	}
	
	@Override
	@Transactional
	public int getProductIdOfActivity(Integer activityId) {	
		return activityDAO.getProductIdOfActivity(activityId);
	}
	

	@Override
	@Transactional
	public List<Integer> getActivitiesofAWPIDS(int activityWorkPackageID, List<Integer> activityWorkPackageIdList, boolean paramIsList){
		return activityDAO.getActivitiesofAWPIDS(activityWorkPackageID, activityWorkPackageIdList, paramIsList);
	}

	@Override
	@Transactional
	public List<Activity> getActivityByParents(Integer parentInstanceId, String parentName) {
		return activityDAO.getActivityByParents(parentInstanceId, parentName);
	}

	@Override
	@Transactional
	public List<Activity> getActivitiesForAutoAllocation(Integer parentEntityId, Integer parentEntityInstanceId) {
		return activityDAO.getActivitiesForAutoAllocation(parentEntityId, parentEntityInstanceId);
	}

	@Override
	@Transactional
	public List<Activity> getActivitiesForAutoAllocationByReferenceId(Integer referenceId) {
		return activityDAO.getActivitiesForAutoAllocationByReferenceId(referenceId);
	}

	@Override
	@Transactional
	public int getActivityPresentCountBasedOnCategoryName(int activityId, String categoryName){
		return activityDAO.getActivityPresentCountBasedOnCategoryName(activityId, categoryName);
	}

	@Override
	public void processActivityPokeNotification(Integer activityId,UserList userList,String message,String messageType,List<String> ccMailList,List<String> toMailList) {
		
		if(!emailNotification.equalsIgnoreCase("YES"))
			return;

		try {
			Activity activity=activityDAO.getActivityById(activityId, 0);
			Integer productId=0;
			List<String> secondaryEmailIds =new ArrayList<String>();
			List<String> primaryEmailIds=new ArrayList<String>();
			List<String> pendingWith=new ArrayList<String>();
			String subject="";
			String [] toMailIds =null;
			String [] ccMailIds =null;
			if(activity != null) {
				subject = "Information requested for: ["+activity.getActivityId()+"]"+" "+activity.getActivityName();
				if(userList != null && userList.getUserId() ==1 ) {
					secondaryEmailIds.add(userList.getEmailId());
				}
				if(ccMailList != null && ccMailList.size()>0){
					for(String ccMail:ccMailList){
						secondaryEmailIds.add(ccMail);
					}
					
				}
				
				if(toMailList != null && toMailList.size()>0){					
					if(toMailList.contains(IDPAConstants.ASSIGNEE)){
					primaryEmailIds.add(activity.getAssignee().getEmailId());
					toMailList.removeAll(Collections.singleton(IDPAConstants.ASSIGNEE));
					}
					if(toMailList.contains(IDPAConstants.REVIEWER)){
					primaryEmailIds.add(activity.getReviewer().getEmailId());
					toMailList.removeAll(Collections.singleton(IDPAConstants.REVIEWER));
					}
					for(String toMail:toMailList){
						primaryEmailIds.add(toMail);
					}
					
				}
				
				productId=activity.getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductId();
				List<WorkflowStatusActor> workflowStatusActorList=workflowStatusPolicyService.getWorkflowStatusActor(productId, activity.getWorkflowStatus().getWorkflowStatusId(), IDPAConstants.ENTITY_ACTIVITY_TYPE, activity.getActivityMaster().getActivityMasterId(), activity.getActivityId(), IDPAConstants.WORKFLOW_STATUS_POLICY_BOTH_ACTION_SCOPE);
				if(workflowStatusActorList != null && workflowStatusActorList.size() >0) {
					for(WorkflowStatusActor statusActor :workflowStatusActorList) {
						if(statusActor.getActorMappingType().equalsIgnoreCase("User")) {
							pendingWith.add(statusActor.getUser().getEmailId());
						} else if(statusActor.getActorMappingType().equalsIgnoreCase("Role")) {
							String role =statusActor.getRole().getRoleName();
							  if (role.equalsIgnoreCase("TestManager")) {
									List<UserList> users = userListDAO.getUserListBasedRoleAndProductId(productId, role);
									if(users!= null && users.size() >0) {
										for(UserList user:users) {
											primaryEmailIds.add(user.getEmailId());
										}
										
									}
								} else if (role.equalsIgnoreCase("TestLead")) {
									List<UserList> users = userListDAO.getUserListBasedRoleAndProductId(productId, role);
									if(users!= null && users.size() >0) {
										for(UserList user:users) {
											primaryEmailIds.add(user.getEmailId());
										}
									}
								}
							
						}
					}
				}
			}
			
			primaryEmailIds.addAll(pendingWith);
			
			if(primaryEmailIds != null && primaryEmailIds.size() >0) {
				toMailIds=primaryEmailIds.toArray(new String[primaryEmailIds.size()]);
			}
			if(secondaryEmailIds != null && secondaryEmailIds.size() >0) {
				ccMailIds = secondaryEmailIds.toArray(new String[secondaryEmailIds.size()]);
			}
			Map<String, Object> model=notificationService.prepareActivityMailTemplateModel(activity);
			model.put("message", message);
			emailService.sendEntityBasedEmailNotification(primaryEmailIds,secondaryEmailIds, userList.getEmailId(),model,IDPAConstants.ENTITY_ACTIVITY_TYPE,subject);
			
		}catch(Exception e) {
			log.error("Error in prepare poke notification ",e);
		}
	}

	@Override
	@Transactional
	public List<Activity> getActivitiesForSetOfActivityIds(List<Integer> activityIds) {
		return activityDAO.getActivitiesForSetOfActivityIds(activityIds);
	}

	@Override
	@Transactional
	public List<Activity> getActivitiesBasedOnTestFactoryIdProductIdandAwpId(Integer testFactoryId, Integer productId, Integer awpId) {
		return activityDAO.getActivitiesBasedOnTestFactoryIdProductIdandAwpId(testFactoryId,productId,awpId);
	}

	@Override
	@Transactional
	public List<JsonActivity> listMyActivities(HttpServletRequest request,List<Activity>activityList,Integer testFactoryId,Integer productId,Integer startIndex,Integer pageSize) {
		

		List<JsonActivity> jsonActivitiesReturnList = new ArrayList<JsonActivity>();
		List<JsonActivity> jsonActivitieslist = new ArrayList<JsonActivity>();
		try {
			UserList user = (UserList) request.getSession().getAttribute("USER");
			if(activityList != null && activityList.size()>0){
				Date modifiedAfterDate = commonService.getModifiedAfterDate((Date)request.getSession().getAttribute("userLoginTime"), (Date)request.getSession().getAttribute("userLogoutTime"));
				for(Activity activity: activityList){
					JsonActivity jsonActivity = new JsonActivity(activity);
					jsonActivitieslist.add(jsonActivity);	
				}
				workflowStatusPolicyService.setInstanceIndicators(testFactoryId,productId,9999999, IDPAConstants.ENTITY_ACTIVITY_TYPE, jsonActivitieslist, IDPAConstants.ENTITY_ACTIVITY_ID,user, modifiedAfterDate, null, null,IDPAConstants.ENABLE_PENDING_WITH_COLUMN);
			}
			
			
			Collections.sort(jsonActivitieslist, new Comparator<JsonActivity>() {
				@Override
				public int compare(JsonActivity activity, JsonActivity activity2) {
					if(activity.getRemainingHours() != null && activity2.getRemainingHours() != null){
						return activity.getRemainingHours().compareTo(activity2.getRemainingHours());
					}else {
						return 0;
					}
				}
			});
			
			jsonActivitiesReturnList = (List<JsonActivity>) workflowStatusPolicyService.getPaginationListFromFullList(jsonActivitieslist, startIndex, pageSize);
		} catch (Exception e) {
			log.error("ERROR  ",e);
		}
		return jsonActivitiesReturnList;
	
		
	}

	@Override
	@Transactional
	public List<Activity> listMyActivitiesByLoginIdAndProductId(Integer userId,Integer testFactoryId,Integer productId,Integer activityWorkPackageId) {
		return activityDAO.listMyActivitiesByLoginIdAndProductId(userId,testFactoryId,productId,activityWorkPackageId);
	}
	
	@Override
	@Transactional
	public List<Activity> listActivitiesByPassingThrough(Integer productId,Integer userId,Integer activityWorkPackageId) {
		try {
			return activityDAO.listActivitiesByPassingThrough(productId, userId,activityWorkPackageId);
		}catch(Exception e) {
			log.error("Error in Service listActivitiesByPassingThrough",e);
		}
		
		return null;
	}
	
	
	@Override
	@Transactional
	public void getPendingStatusActivities(Integer productId, Integer entityTypeId,String botName){
		try {
			List<Object[]> beginStageList=workflowStatusPolicyDAO.getBeginStatusActivities(productId, entityTypeId);
			
			HashMap<String, String> statusActionQueryDetails = new HashMap<String, String>();
			if(IDPAConstants.ENTITY_ACTIVITY_TYPE == entityTypeId){
				statusActionQueryDetails.put("joinColumn", " LEFT JOIN activity act ON wfe.entityInstanceId = act.activityId");
			}
			
			List<Object[]> currentStatusActionList = workflowEventDAO.getCurrentStatusActionViewTestFactoryLevel(null,productId, entityTypeId, "", null, statusActionQueryDetails);
			HashMap<Integer, HashMap<String, String>> individualUserBeginActivities = new HashMap<Integer, HashMap<String, String>>();
			String mailBody="";
			String subject="<"+botName+"> Bot:Act on Activities Pending with you";
			String toMailId="";
			String ccMailId="";
			String primaryEmailIds="";
			String secondaryEmailIds="";
			String [] toMailIds =null;
			String [] ccMailIds =null;
			
			Date completeBy = null;
			String completeByFormat = "--";
			String statusType = "";
			
			if(beginStageList != null && beginStageList.size() >0 ) {
				for(Object[] row:beginStageList){
					Integer activityId=(Integer)row[0];
					String activityName=(String)row[1];
					String status=(String)row[2];
					Integer userId=(Integer)row[3];
					String pendingWith=(String)row[4];
					toMailId=(String)row[5];
					Integer reviewerId=(Integer)row[6];
					String productName=(String)row[7];
					String workpackageName=(String)row[8];
						if(reviewerId != null && reviewerId >0) {
							UserList user=userListDAO.getByUserId(reviewerId);
							if(null != user) {
								ccMailId=user.getEmailId();
						}
					}
						
						
						if(currentStatusActionList != null && currentStatusActionList.size() > 0) {
							for(Object[] statusAction : currentStatusActionList){
								Integer pendingEntityInstanceId = (Integer)statusAction[0];
								if(pendingEntityInstanceId != null && activityId.equals(pendingEntityInstanceId)){
									statusType = (String) statusAction[5];
									if(statusType != null && !"End".equalsIgnoreCase(statusType)){
										completeBy=(Date)statusAction[3];
									}
								}
							}
										
							
						
							
							if(completeBy != null) {
								completeByFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,DateFormat.SHORT).format(completeBy.getTime());
							}else{
								completeByFormat = "--";
							}
						}
						
						
						
					HashMap<String ,String> beginUserGroup=null;
					if(!individualUserBeginActivities.containsKey(userId)){
						mailBody="<table border='1' cellspacing='0' cellpadding='0'><tr><th>ActivityId</th><th>Activity Name</th><th style='width: 20px;'>Product</th><th style='width: 30px;'>WorkPackage</th><th style='width: 50px;'>Status</th><th style='width: 120px;'>Pending With</th><th style='width: 140px;'>Planned Completion Date</th></tr>";
						beginUserGroup=new HashMap<String,String>();
						beginUserGroup.put("mailContent", mailBody);
						beginUserGroup.put("userName", pendingWith);
						beginUserGroup.put("primaryEmailIds", toMailId);
						beginUserGroup.put("secondaryEmailIds", ccMailId);
						individualUserBeginActivities.put(userId, beginUserGroup);
					} 
					beginUserGroup=individualUserBeginActivities.get(userId);
					mailBody=beginUserGroup.get("mailContent");
					mailBody+="<tr><td>"+activityId+"</td><td>"+activityName+"</td><td>"+productName+"</td><td>"+workpackageName+"</td><td>"+status+"</td><td>"+pendingWith+"</td><td>"+completeByFormat+"</td></tr>";
					beginUserGroup.put("mailContent", mailBody);
					primaryEmailIds =beginUserGroup.get("primaryEmailIds");
					if(!primaryEmailIds.contains(toMailId)){
						primaryEmailIds+=","+toMailId;
						
					}
					
					secondaryEmailIds=beginUserGroup.get("secondaryEmailIds");
					if(!secondaryEmailIds.contains(ccMailId)){
						secondaryEmailIds+=","+ccMailId;
						
					}
					individualUserBeginActivities.put(userId, beginUserGroup);
				}
				
				if(individualUserBeginActivities !=null && !individualUserBeginActivities.isEmpty()) {
					String mailBodys="";
					for(Map.Entry<Integer,HashMap<String,String>> indivialUserGroup:individualUserBeginActivities.entrySet()){
						
						Map<String,String> userGrouping=indivialUserGroup.getValue();
						String primaryMails=userGrouping.get("primaryEmailIds");
						String secondaryMails=userGrouping.get("secondaryEmailIds");
						String userName=userGrouping.get("userName");
						mailBodys="Dear "+userName+",<br/> Please take action on below pending Activities,<br/><br/>";
						mailBodys+=userGrouping.get("mailContent");
						mailBodys+="</table><div align='center'><hr size='2' width='100%' noshade='' align='center'/></div><p>  Regards,</p><p>  Admin.</p><p>  Do-Not-Reply@Admin: Activity Notification</p>";
						toMailIds=primaryMails.split(",");
						ccMailIds=secondaryMails.split(",");					
						if(emailNotification.equalsIgnoreCase("YES")){
							emailService.sendBotEmailNotifications(toMailIds, ccMailIds, subject, mailBodys, null);
						}
					}
					
				}
				
				
			}
			
		}catch(Exception e) {
			log.error("Error in Service getBeginStatusActivities",e);
		}
	}

	@Override
	@Transactional
	public List<Activity> listActivitiesByProductIdAndWorkpackageId(Integer enagementId, Integer productId,Integer activityWorkPackageId, Integer initializationLevel,
			Integer isActive, Integer startIndex, Integer pageSize) {
		try {
			return activityDAO.listActivitiesByProductIdAndWorkpackageId(enagementId, productId, activityWorkPackageId, initializationLevel, isActive, startIndex, pageSize);
		}catch(Exception e){
			log.error("Error service listActivitiesByProductIdAndWorkpackageId",e);
		}
		return null;
	}
	
	@Override
	@Transactional
	public List<Activity> getActivityByActivtyWorkpackageIdNotCompletedStage(Integer awpId) {
		
		try {
			return activityDAO.getActivityByActivtyWorkpackageIdNotCompletedStage(awpId);
		}catch(Exception e) {
			log.error("Error in getActivityByActivtyWorkpackageIdNotCompletedStage",e);
		}
		return null;
	}
	
	
	@Override
	@Transactional
	public void getWeeklyReportsForSLUActivities(Integer productId, Integer entityTypeId){
		try {
			List<Object[]> beginStageList=workflowStatusPolicyDAO.getBeginStageActivitiesForWeeklyReport(productId, entityTypeId);
			List<Object[]> intermediateStageList=workflowStatusPolicyDAO.getIntermediateStageActivitiesForWeeklyReport(productId, entityTypeId);
			List<Object[]> endStageList=workflowStatusPolicyDAO.getEndStageActivitiesForWeeklyReport(productId, entityTypeId);
			List<Object[]> workpackageBasedCountList=workflowStatusPolicyDAO.getWorkpackageLevelActivitiesCountForWeekly(productId, entityTypeId);
			List<Object[]> attentionActivities=workflowStatusPolicyDAO.getAttentionActivitiesForWeeklyReport(productId, entityTypeId);
			
			
			String mailContent="<html> <style> h4{margin-bottom: 2px;font-family: Calibri;font-size: 16px;} body{font-family: Calibri;font-size: 13px;} </style>  <h4>Highlights:</h4>";
			
			if(endStageList != null && endStageList.size() >0 ) {
				mailContent+="<ul style='list-style-type: none;'><h4>Deployments completed this week:</h4><li><ul>";
				for(Object[] row:endStageList){
					String sluName=(String)row[0];
					String activityName=(String)row[1];
					String stateName=(String)row[2];
					Integer activityId=(Integer)row[4];
					String latestComments="";
					Comments comment=commonDAO.getLatestCommentOfByEntityTypeAndEntityInstanceId(IDPAConstants.ACTIVITY_ENTITY_MASTER_ID, activityId);
					if(comment != null) {
						latestComments=comment.getComments();
					}
					
					mailContent+="<li>"+sluName+"--"+activityName+"--"+stateName+"--"+latestComments+" </li>";
				
				}
				mailContent+="</ul></li></ul>";
			} 
			
			if(intermediateStageList != null && intermediateStageList.size() >0 ) {
				mailContent+="<ul style='list-style-type: none;'><h4>Deployments started this week</h4><li><ul>";
				for(Object[] row:intermediateStageList){
					String sluName=(String)row[0];
					String activityName=(String)row[1];
					String stateName=(String)row[2];
					Integer activityId=(Integer)row[4];				
					String latestComments="";
					Comments comment=commonDAO.getLatestCommentOfByEntityTypeAndEntityInstanceId(IDPAConstants.ACTIVITY_ENTITY_MASTER_ID, activityId);
					if(comment != null) {
						latestComments=comment.getComments();
					}
					
					if(latestComments =="" || latestComments.isEmpty()) {
						latestComments="No Comments";
					}
					mailContent+="<li>"+sluName+"--"+activityName+"--"+stateName+"--"+latestComments+" </li>";
					
				}
				
				mailContent+="</ul></li></ul>";
			}
			
			
			if(beginStageList != null && beginStageList.size() >0 ) {
				mailContent+="<ul style='list-style-type: none;'><h4>Opportunities identified this week</h4><li><ul>";
				for(Object[] row:beginStageList){
					String sluName=(String)row[0];
					String activityName=(String)row[1];
					String stateName=(String)row[2];
					Integer activityId=(Integer)row[4];				
					String latestComments="";
					Comments comment=commonDAO.getLatestCommentOfByEntityTypeAndEntityInstanceId(IDPAConstants.ACTIVITY_ENTITY_MASTER_ID, activityId);
					if(comment != null) {
						latestComments=comment.getComments();
					}
					
					if(latestComments =="" || latestComments.isEmpty()) {
						latestComments="No Comments";
					}
					mailContent+="<li>"+sluName+"--"+activityName+"--"+stateName+"--"+latestComments+" </li>";
				
				}
				mailContent+="</ul></li></ul>";
			}
			
			
			if(workpackageBasedCountList != null && workpackageBasedCountList.size() >0 ) {
				Set<String> slus = new HashSet<String>();
				List<String> categories = new ArrayList<String>();
				if(statusCagories !="" && !statusCagories.isEmpty()) {
					String statusCtgy[]=statusCagories.split(",");
					for(String ctgy:statusCtgy) {
						categories.add(ctgy);
					}
				}
				
				HashMap<String, HashMap<String, Integer>> categoryWiseSLUDetails = new HashMap<String, HashMap<String, Integer>>();
				Integer sluTotal=0;
				for(Object[] row:workpackageBasedCountList){
					String sluName=(String)row[0];
					String statusCategory=(String)row[1];
					Integer count = ((BigInteger)row[2]).intValue();
					
					slus.add(sluName);
					if(categoryWiseSLUDetails.containsKey(statusCategory)){
						sluTotal=0;
						categoryWiseSLUDetails.get(statusCategory).put(sluName, count);
						sluTotal= sluTotal+count;
						categoryWiseSLUDetails.get(statusCategory).put(sluName+"sluTotal",sluTotal );
					}else{
						HashMap<String, Integer> sluDetails = new HashMap<String, Integer>();
						sluDetails.put(sluName, count);
						sluDetails.put(sluName+"sluTotal", count);
						categoryWiseSLUDetails.put(statusCategory, sluDetails);
					}
				}
				mailContent+="<h4>Deployment Summary:</h4><table border='1' cellspacing='0' cellpadding='0'><tr><th style='width: 100px;background-color: #ff9933;'></th>";
				for(String slu : slus){
					mailContent += "<th style='width: 80px; background-color: #ff9933;'>"+slu.toUpperCase()+"</th>";
				}
				mailContent += "<th style='width: 80px;background-color: #ff9933;'>Total</th></tr>";
				Integer tdTotal=0;
				for(String category : categories){
					tdTotal=0;
					mailContent += "<tr><th style='background-color: #ff9933;'>"+category+"</th>";
					if(categoryWiseSLUDetails.containsKey(category)){
						HashMap<String, Integer> sluDetails = categoryWiseSLUDetails.get(category);
					
						for(String slu :slus){
							if(sluDetails.containsKey(slu)){
								mailContent += "<td style='width: 80px;text-align: center;background-color: #ffd9b3;'>"+sluDetails.get(slu)+"</td>";
								tdTotal=tdTotal+sluDetails.get(slu);
							}else{
								mailContent += "<td style='width: 80px;text-align: center;background-color: #ffd9b3;'>0</td>";
							}
							
						}
						
					}else{
						for(String slu :slus){
							mailContent += "<td style='width: 80px;text-align: center;background-color: #ffd9b3;'>0</td>";
						}
					}
					mailContent += "<th style='width: 80px;background-color: #ffd9b3;'>"+tdTotal+"</th></tr>";
					
					
					
				}
					mailContent+="<tr><td style='background-color: #ff9933;'></td>";
					Integer sumSLUTotal=0;
					for(String slu :slus){
						Integer trTotal=0;
						for(String category : categories){
							HashMap<String, Integer> sluTotalDetails = categoryWiseSLUDetails.get(category);
							if(sluTotalDetails != null && sluTotalDetails.containsKey(slu+"sluTotal")){
								trTotal=trTotal+sluTotalDetails.get(slu+"sluTotal");
							}
							
						}
						sumSLUTotal=sumSLUTotal+trTotal;
						mailContent += "<th style='width: 80px;background-color: #ffd9b3;'>"+trTotal+"</th>";
					}
				mailContent += "<th style='width: 80px;background-color: #ffd9b3;'>"+sumSLUTotal+"</th></tr></table>";
			}
			
			Boolean headerFlag=true;
			String mailBody="<table border='1' cellspacing='0' cellpadding='0'><tr><th style='background-color: #80bfff;'>SLU</th><th style='width: 150px;background-color: #80bfff;'>Deployment </th><th style='width: 150px;background-color: #80bfff;'>Assignee</th><th style='width: 150px;background-color: #80bfff;'>Planned End Date</th><th style='width: 150px;background-color: #80bfff;'>Current Status</th></tr>";
			if(attentionActivities != null && attentionActivities.size() >0 ) {
				mailContent+="<ul style='-webkit-padding-start: 0px;padding-left: 0px'>";
				for(Object[] row:attentionActivities){
					String sluName=(String)row[0];
					String activityName=(String)row[1];
					String stateName=(String)row[2];
					Date plannedEndDate=(Date)row[3];
					String userLoginId=(String)row[4];
					String formattedPlannedEndDate=DateUtility.dateformatWithOutTime(plannedEndDate);
					if(headerFlag) {
						mailContent+="<h4>Activities Need Attention:</h4>"+mailBody;
						headerFlag=false;
					}
					mailContent+="<tr><th style='padding-left: 5px;text-align: center;background-color: #80bfff;'>"+sluName+"</th><td style='width: 200px;padding-left: 5px;text-align: center;background-color: #cce6ff;'>"+activityName+"</td><td style='width: 200px;padding-left: 5px;text-align: center;background-color: #cce6ff;'>"+userLoginId+"</td><td style='width: 50px;padding-left: 5px;text-align: center;background-color: #cce6ff;'>"+formattedPlannedEndDate+"</td><td style='width: 50px;padding-left: 5px;text-align: center;background-color: #cce6ff;'>"+stateName+"</td></tr>";
				}
				mailContent+="</table><br/>";
				headerFlag=true;
				mailContent+="</ul>";
			}
			mailContent+="<div align='center'><hr size='2' width='100%' noshade='' align='center'/></div><p>  Report created by ERS iLCM Bot on behalf of Ambica</p> </html>";
			
			log.info("Weekly Report Mail Content:"+mailContent);
			String primaryEmailIds[]=toMail.split(",");
			String secondaryEmailIds[]=ccMail.split(",");
			String subject="Deployment Weekly Report";
			if(emailNotification.equalsIgnoreCase("Yes")) {
				emailService.sendReportIssue(fromMail, primaryEmailIds, secondaryEmailIds, subject, mailContent);
			}
			
		}catch(Exception e) {
			log.error("Error in getWeeklyReportsForSLUActivities",e);
		}
	}

	@Override
	@Transactional
	public String customFieldValidationProcessing(String customFileds, Integer customFieldMappedTo, UserList user, List<CustomFieldValues> customFieldValuesList, CustomFieldService customFieldService) {
		String customFieldErrorMessage = "";
		try{
			JSONParser parser = new JSONParser();
			JSONArray jsonArray = (JSONArray) parser.parse(customFileds);
			for(int i=0;i<jsonArray.size();i++) {
				JSONObject jsonObject=(JSONObject)jsonArray.get(i);
				Integer activityMasterId =Integer.parseInt(jsonObject.get("activityMasterId").toString());
				
				if(activityMasterId.equals(customFieldMappedTo)) {
					CustomFieldConfigMaster customFieldConfigMaster = customFieldService.getCustomFieldById(Integer.parseInt(jsonObject.get("customFieldId").toString()), 1);
					String fieldValue = "";
					if(jsonObject.containsKey("fieldValue") && jsonObject.get("fieldValue") != null){
						fieldValue = jsonObject.get("fieldValue").toString();
					}
					if((customFieldConfigMaster != null && "Yes".equalsIgnoreCase(customFieldConfigMaster.getIsMandatory())) && (fieldValue  == null || fieldValue.trim().isEmpty())){
						customFieldErrorMessage += customFieldConfigMaster.getFieldName()+" is Mandatory<br>";
					}
					
					if(fieldValue  != null && !fieldValue.trim().isEmpty()){
						String errorMessage = customFieldService.validateCustomFieldValueForDataType(customFieldConfigMaster.getDataType(), jsonObject.get("fieldValue").toString());
						if(errorMessage != null && !errorMessage.trim().isEmpty()){
							customFieldErrorMessage += errorMessage + " for field "+customFieldConfigMaster.getFieldName()+"<br>";
						}
						errorMessage = customFieldService.validateCustomFieldValueForDataRange(customFieldConfigMaster.getDataType(), jsonObject.get("fieldValue").toString(), customFieldConfigMaster.getUpperLimit(), customFieldConfigMaster.getLowerLimit());
						if(errorMessage != null && !errorMessage.trim().isEmpty()){
							customFieldErrorMessage += errorMessage + " for field "+customFieldConfigMaster.getFieldName()+"<br>";
						}
						
						if(customFieldErrorMessage == null || customFieldErrorMessage.trim().isEmpty()){
							CustomFieldValues customFieldValues = new CustomFieldValues();
							customFieldValues.setCustomFieldId(customFieldConfigMaster);
							customFieldValues.setFieldValue(jsonObject.get("fieldValue").toString());
							customFieldValues.setFrequencyOrder(null);
							customFieldValues.setFrequencyMonth(null);
							customFieldValues.setFrequencyYear(null);
							
							customFieldValues.setCreatedBy(user);
							customFieldValues.setCreatedOn(new Date());
							customFieldValues.setModifiedBy(user);
							customFieldValues.setModifiedOn(new Date());
							
							customFieldValuesList.add(customFieldValues);
						}
					}
				}
			}
		}catch(Exception ex){
			log.error("Error in customFieldProcessing",ex);
		}
		return customFieldErrorMessage;
	}
	
	@Override
	@Transactional
	public void getMonthlyReportsForSLUActivities(Integer productId,
			Integer entityTypeId) {
		List<Object[]> beginStageMonthlyList=workflowStatusPolicyDAO.getBeginStageActivitiesForMonthlyReport(productId, entityTypeId);
		List<Object[]> intermediateStageMonthlyList=workflowStatusPolicyDAO.getIntermediateStageActivitiesForMonthlyReport(productId, entityTypeId);
		List<Object[]> endStageMonthlyList=workflowStatusPolicyDAO.getEndStageActivitiesForMonthlyReport(productId, entityTypeId);
		List<Object[]> customFieldValList=null;
		Boolean headerFlag=true;
		String mailBody = "";
		String fieldName="";
		String fieldVal="";
		
		String mailContent="<html> <style> h4{margin-bottom: 2px;font-family: Calibri;font-size: 16px;} body{font-family: Calibri;font-size: 13px;} </style>  <h4 style='color:#ff3534;'>SLU Deployments:</h4>";
		
		if(endStageMonthlyList != null && endStageMonthlyList.size() >0 ) {
			mailBody = "<table border='1' cellspacing='0' cellpadding='0'><tr><th style='width: 100px;background-color: #80bfff;'>SLU</th><th style='width: 150px;background-color: #80bfff;'>Account/Project</th><th style='width: 150px;background-color: #80bfff;'>C-code</th><th style='width: 150px;background-color: #80bfff;'>Project Name</th></tr>";
			mailContent+="<ul style='-webkit-padding-start: 0px;padding-left: 0px'>";
			for(Object[] row:endStageMonthlyList){
				String sluName=(String)row[0];
				String activityName=(String)row[1];
				Integer activityId=(Integer)row[2];
				Integer activityTypeId=(Integer)row[3];
				String latestComments="";
				fieldName="";
				fieldVal="";
				
				Comments comment=commonDAO.getLatestCommentOfByEntityTypeAndEntityInstanceId(IDPAConstants.ACTIVITY_ENTITY_MASTER_ID, activityId);
				customFieldValList=workflowStatusPolicyDAO.getCustomFieldValuesByentityTypeAndInstanceIds(entityTypeId,activityTypeId, activityId);
				for(Object[] customValues:customFieldValList){
					fieldName=(String)customValues[0];
					fieldVal=(String)customValues[1];
				}
				if(comment != null) {
					latestComments=comment.getComments();
				}
				if(headerFlag) {
					mailContent+="<table style='margin-top: 5px; margin-bottom: 10px;' border='1' cellspacing='0' cellpadding='0'><tr><th style='padding-right: 337px; padding-left: 10px; background-color: #80bfff;'>Deployments completed and Signed Off </th></tr></table>"+mailBody;
					headerFlag=false;
				}
				mailContent+="<tr><th style='padding-left: 5px;text-align: center;background-color: #80bfff;'>"+sluName+"</th><td style='width: 200px;padding-left: 5px;text-align: center;background-color: #cce6ff;'>"+fieldName+"</td><td style='width: 50px;padding-left: 5px;text-align: center;background-color: #cce6ff;'>"+fieldVal+"</td><td style='width: 50px;padding-left: 5px;text-align: center;background-color: #cce6ff;'>"+activityName+"</td></tr>";
			}
			mailContent+="</ul></table>";
			headerFlag=true;
		}
				
		if(beginStageMonthlyList != null && beginStageMonthlyList.size() >0 ) {
			mailBody="<table border='1' cellspacing='0' cellpadding='0'><tr><th style='width: 100px;background-color: #80bfff;'>SLU</th><th style='width: 150px;background-color: #80bfff;'>Account</th><th style='width: 150px;background-color: #80bfff;'>C-code</th><th style='width: 150px;background-color: #80bfff;'>Project Name</th></tr>";
			mailContent+="<ul style='-webkit-padding-start: 0px;padding-left: 0px'>";
			for(Object[] row:beginStageMonthlyList){
				String sluName=(String)row[0];
				String activityName=(String)row[1];
				Integer activityId=(Integer)row[2];
				Integer activityTypeId=(Integer)row[3];
				customFieldValList=workflowStatusPolicyDAO.getCustomFieldValuesByentityTypeAndInstanceIds(entityTypeId,activityTypeId, activityId);
				fieldName="";
				fieldVal="";
				for(Object[] customValues:customFieldValList){
					fieldName=(String)customValues[0];
					fieldVal=(String)customValues[1];
				}
				
				if(headerFlag) {
					mailContent+="<table style='margin-top: 25px; margin-bottom: 10px;' border='1' cellspacing='0' cellpadding='0'><tr><th style='padding-right: 529px; padding-left: 10px; background-color: #80bfff;'>New Deployments Kicked Off </th></tr></table>"+mailBody;
					headerFlag=false;
				}
				mailContent+="<tr><th style='padding-left: 5px;text-align: center;background-color: #80bfff;'>"+sluName+"</th><td style='width: 200px;padding-left: 5px;text-align: center;background-color: #cce6ff;'>"+fieldName+"</td><td style='width: 50px;padding-left: 5px;text-align: center;background-color: #cce6ff;'>"+fieldVal+"</td><td style='width: 50px;padding-left: 5px;text-align: center;background-color: #cce6ff;'>"+activityName+"</td></tr>";
			}			
			mailContent+="</ul></table>";
			headerFlag=true;
		}
		
		if(intermediateStageMonthlyList != null && intermediateStageMonthlyList.size() >0 ) {
			mailBody="<table border='1' cellspacing='0' cellpadding='0'><tr><th style='width: 100px;background-color: #80bfff;'>SLU</th><th style='width: 150px;background-color: #80bfff;'>Account</th><th style='width: 150px;background-color: #80bfff;'>C-code</th><th style='width: 150px;background-color: #80bfff;'>Project Name</th><th style='width: 150px;background-color: #80bfff;'>Comments</th></tr>";
			mailContent+="<ul style='-webkit-padding-start: 0px;padding-left: 0px'>";
			for(Object[] row:intermediateStageMonthlyList){
				String sluName=(String)row[0];
				String activityName=(String)row[1];
				Integer activityId=(Integer)row[2];
				Integer activityTypeId=(Integer)row[3];
				String latestComments="";
				fieldName="";
				fieldVal="";
				Comments comment=commonDAO.getLatestCommentOfByEntityTypeAndEntityInstanceId(IDPAConstants.ACTIVITY_ENTITY_MASTER_ID, activityId);
				customFieldValList=workflowStatusPolicyDAO.getCustomFieldValuesByentityTypeAndInstanceIds(entityTypeId,activityTypeId, activityId);
				for(Object[] customValues:customFieldValList){
					fieldName=(String)customValues[0];
					fieldVal=(String)customValues[1];
				}
				if(comment != null) {
					latestComments=comment.getComments();
				}
				
				if(latestComments =="" || latestComments.isEmpty()) {
					latestComments="No Comments";
				}
				if(headerFlag) {
					mailContent+="<table style='margin-top: 25px; margin-bottom: 10px;' border='1' cellspacing='0' cellpadding='0'><tr><th style='padding-right: 483px; padding-left: 10px; background-color: #80bfff;'>Deployments Upgrades/PoCs In Progress </th></tr></table>"+mailBody;
					headerFlag=false;
				}
				mailContent+="<tr><th style='padding-left: 5px;text-align: center;background-color: #80bfff;'>"+sluName+"</th><td style='width: 200px;padding-left: 5px;text-align: center;background-color: #cce6ff;'>"+fieldName+"</td><td style='width: 50px;padding-left: 5px;text-align: center;background-color: #cce6ff;'>"+fieldVal+"</td><td style='width: 50px;padding-left: 5px;text-align: center;background-color: #cce6ff;'>"+activityName+"</td><td style='width: 50px;padding-left: 5px;text-align: center;background-color: #cce6ff;'>"+latestComments+"</td></tr>";
			}
			mailContent+="</ul></table>";
			headerFlag=true;
		}		
		log.info("Activity Notification for Priya----------"+mailContent);
	}
	
	@Override
	@Transactional
	public void updateActivityPredecessors(Activity activity, String predecessorsString ) {
		
		if (activity == null)
			return;
		
		//Tokenize the string into activity Ids
		String[] predecessorIdsArray = predecessorsString.trim().split(",");
		if (predecessorsString == "" || predecessorsString.isEmpty()) {
			//TODO : Delete all existing predecessors
			deleteAllPredecessorForActivity(activity);
			return;
		}
		Set<Integer> predecessorIdsList = new HashSet<Integer>();
		
		//Get the current list of predecessors for the activity
		Set<Integer> existingPredecessorIdsList = getActivityPredecessorIdsList(activity); 
	
		//Add new activities to the predecessor list
		Integer id = null;
		for(String predecessorId : predecessorIdsArray) {
			id = new Integer(predecessorId);
			predecessorIdsList.add(id);
			if ((existingPredecessorIdsList != null && existingPredecessorIdsList.size() >0) && existingPredecessorIdsList.contains(id)) {
				//This id is already a predecessor of the activity
				//Do nothing
			} else {
				addNewPredecessorToActivity(activity, id);
			}
		}
		
		//Check for deleted predecessors
		for(Integer existingPredecessorId : existingPredecessorIdsList) {
			if(predecessorIdsList.contains(existingPredecessorId)) {
				//Do nothing
			} else {
				//This id has been removed from the predecessors list in the UI
				//Remove the relationship
				deletePredecessorForActivity(activity, existingPredecessorId);
			}
		}
	}
	
	public Set<Integer> getActivityPredecessorIdsList(Activity activity) {
		
		Set<Integer> predecessorIds = new HashSet<Integer>();
		List<EntityRelationshipCommon> relationships = entityRelationshipCommonService.getEntityRelationships(33, activity.getActivityId(), IDPAConstants.ENTITY_RELATIONSHIP_TYPE_PREDECESSOR, IDPAConstants.ENTITY_RELATIONSHIP_SUBTYPE_FINISH_START);
		if(relationships != null && relationships.size() >0) {
			for(EntityRelationshipCommon relationship :relationships) {
				predecessorIds.add(relationship.getTargetEntityInstanceId());
			}
		}
		return predecessorIds;
	}

	public void addNewPredecessorToActivity(Activity activity, Integer predecessorId) {
		entityRelationshipCommonService.addEntityRelationship(33, activity.getActivityId(), IDPAConstants.ENTITY_RELATIONSHIP_TYPE_PREDECESSOR, IDPAConstants.ENTITY_RELATIONSHIP_SUBTYPE_FINISH_START, 33, predecessorId);
	}

	public void deletePredecessorForActivity(Activity activity, Integer predecessorId) {
		entityRelationshipCommonService.deleteEntityRelationship(33, activity.getActivityId(), IDPAConstants.ENTITY_RELATIONSHIP_TYPE_PREDECESSOR, IDPAConstants.ENTITY_RELATIONSHIP_SUBTYPE_FINISH_START, 33, predecessorId);
	}

	public void deleteAllPredecessorForActivity(Activity activity) {
		entityRelationshipCommonService.deleteEntityRelationships(33, activity.getActivityId(), IDPAConstants.ENTITY_RELATIONSHIP_TYPE_PREDECESSOR, IDPAConstants.ENTITY_RELATIONSHIP_SUBTYPE_FINISH_START);
	}
	
	@Override
	public List<Object[]> getUnMappedRcnByActivityWPId(int productId, int activityWPId, int activityId, int jtStartIndex, int jtPageSize) {
			return activityDAO.getUnMappedRcnByActivityWPId(productId, activityWPId, activityId, jtStartIndex, jtPageSize);
	}
	
	@Override
	public int getUnMappedRcnCountByActivityWPId(int activityId, int productId,int activityWPId) {		
		return activityDAO.getUnMappedRcnCountByActivityWPId(activityId, productId, activityWPId);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<JsonActivity> listActivitiesBySerachCriteria(Integer engagementId,Integer productId, Integer productVersionId, Integer productBuildId,UserList user,List<Integer> activityWorkPackageIds,List<String> workflowStatusList,List<String> workflowStatusTypeList, List<String> activityTypeList,List<String> assigneeList,List<String> reviewerList,
			Date plannedStartFromDate,Date plannedStartToDate,String plannedStartDateSearch,
			Date plannedEndFromDate,Date plannedEndToDate,String plannedEndDateSerach,
			Date actualStartFromDate,Date actualStartToDate,String actualStartDateSearch,
			Date actualEndFromDate,Date actualEndToDate, String actualEndDateSearch) {
		List<Activity> activitieslist = null;
		List<JsonActivity> jsonActivitiesReturnList = new ArrayList<JsonActivity>();
		List<JsonActivity> jsonActivitieslist = new ArrayList<JsonActivity>();
		try {
			activitieslist = activityDAO.listActivitiesBySerachCriteria(engagementId, productId, productVersionId, productBuildId, user, activityWorkPackageIds, workflowStatusList, workflowStatusTypeList, activityTypeList, assigneeList, reviewerList,plannedStartFromDate,plannedStartToDate,plannedEndFromDate,plannedEndToDate,actualStartFromDate,actualStartToDate,actualEndFromDate,actualEndToDate);
			if(activitieslist != null && activitieslist.size()>0){
				for(Activity actvity: activitieslist){
					JsonActivity jsonActivity = new JsonActivity(actvity);
					if(plannedStartDateSearch != null && !plannedStartDateSearch.isEmpty()) {
						if(plannedStartDateSearch.equals("=") && actvity.getPlannedStartDate().compareTo(plannedStartFromDate) == 0) {
							jsonActivitieslist.add(jsonActivity);	
						} else if( plannedStartDateSearch.equals(">=") && actvity.getPlannedStartDate().compareTo(plannedStartFromDate)>=0) {
							jsonActivitieslist.add(jsonActivity);
						}else if( plannedStartDateSearch.equals("<=") && actvity.getPlannedStartDate().compareTo(plannedStartFromDate)<=0) {
							jsonActivitieslist.add(jsonActivity);
						}else if( plannedStartDateSearch.equals(">") && actvity.getPlannedStartDate().compareTo(plannedStartFromDate)>0) {
							jsonActivitieslist.add(jsonActivity);
						}else if( plannedStartDateSearch.equals("<") && actvity.getPlannedStartDate().compareTo(plannedStartFromDate)<0) {
							jsonActivitieslist.add(jsonActivity);
						}else if( plannedStartDateSearch.equals("!=") && actvity.getPlannedStartDate().compareTo(plannedStartFromDate)!=0) {
							jsonActivitieslist.add(jsonActivity);
						} else if(plannedStartDateSearch.equals("between")) {
							jsonActivitieslist.add(jsonActivity);
						}
					}else if(plannedEndDateSerach != null && !plannedEndDateSerach.isEmpty()) {
						if(plannedEndDateSerach.equals("=") && actvity.getPlannedEndDate().compareTo(plannedEndFromDate) == 0) {
							jsonActivitieslist.add(jsonActivity);	
						} else if( plannedEndDateSerach.equals(">=") && actvity.getPlannedEndDate().compareTo(plannedEndFromDate)>=0) {
							jsonActivitieslist.add(jsonActivity);
						}else if( plannedEndDateSerach.equals("<=") && actvity.getPlannedEndDate().compareTo(plannedEndFromDate)<=0) {
							jsonActivitieslist.add(jsonActivity);
						}else if( plannedEndDateSerach.equals(">") && actvity.getPlannedEndDate().compareTo(plannedEndFromDate)>0) {
							jsonActivitieslist.add(jsonActivity);
						}else if( plannedEndDateSerach.equals("<") && actvity.getPlannedEndDate().compareTo(plannedEndFromDate)<0) {
							jsonActivitieslist.add(jsonActivity);
						}else if( plannedEndDateSerach.equals("!=") && actvity.getPlannedEndDate().compareTo(plannedEndFromDate)!=0) {
							jsonActivitieslist.add(jsonActivity);
						} else if(plannedEndDateSerach.equals("between")) {
							jsonActivitieslist.add(jsonActivity);
						}
					} else if(actualStartDateSearch != null && !actualStartDateSearch.isEmpty()) {
						if(actualStartDateSearch.equals("=") && (actvity.getActualStartDate() != null && actvity.getActualStartDate().compareTo(actualStartFromDate) == 0)) {
							jsonActivitieslist.add(jsonActivity);	
						} else if( actualStartDateSearch.equals(">=") && (actvity.getActualStartDate() != null && actvity.getActualStartDate().compareTo(actualStartFromDate)>=0)) {
							jsonActivitieslist.add(jsonActivity);
						}else if( actualStartDateSearch.equals("<=") && (actvity.getActualStartDate() != null && actvity.getActualStartDate().compareTo(actualStartFromDate)<=0)) {
							jsonActivitieslist.add(jsonActivity);
						}else if( actualStartDateSearch.equals(">") && (actvity.getActualStartDate() != null && actvity.getActualStartDate().compareTo(actualStartFromDate)>0)) {
							jsonActivitieslist.add(jsonActivity);
						}else if( actualStartDateSearch.equals("<") && (actvity.getActualStartDate() != null && actvity.getActualStartDate().compareTo(actualStartFromDate)<0)) {
							jsonActivitieslist.add(jsonActivity);
						}else if( actualStartDateSearch.equals("!=") && (actvity.getActualStartDate() != null && actvity.getActualStartDate().compareTo(actualStartFromDate)!=0)) {
							jsonActivitieslist.add(jsonActivity);
						} else if(actualStartDateSearch.equals("between")) {
							jsonActivitieslist.add(jsonActivity);
						}
					} else if(actualEndDateSearch != null && !actualEndDateSearch.isEmpty()) {
						if(actualEndDateSearch.equals("=") && (actvity.getActualEndDate() != null && actvity.getActualEndDate().compareTo(actualEndFromDate) == 0)) {
							jsonActivitieslist.add(jsonActivity);	
						} else if( actualEndDateSearch.equals(">=")&&(actvity.getActualEndDate() != null && actvity.getActualEndDate().compareTo(actualEndFromDate)>=0)) {
							jsonActivitieslist.add(jsonActivity);
						}else if( actualEndDateSearch.equals("<=")&&(actvity.getActualEndDate() != null &&  actvity.getActualEndDate().compareTo(actualEndFromDate)<=0)) {
							jsonActivitieslist.add(jsonActivity);
						}else if( actualEndDateSearch.equals(">") && (actvity.getActualEndDate() != null && actvity.getActualEndDate().compareTo(actualEndFromDate)>0)) {
							jsonActivitieslist.add(jsonActivity);
						}else if( actualEndDateSearch.equals("<") && (actvity.getActualEndDate() != null && actvity.getActualEndDate().compareTo(actualEndFromDate)<0)) {
							jsonActivitieslist.add(jsonActivity);
						}else if( actualEndDateSearch.equals("!=") && (actvity.getActualEndDate() != null && actvity.getActualEndDate().compareTo(actualEndFromDate)!=0)) {
							jsonActivitieslist.add(jsonActivity);
						} else if(actualEndDateSearch.equals("between")) {
							jsonActivitieslist.add(jsonActivity);
						}
					} else {
						jsonActivitieslist.add(jsonActivity);
					}
					
					
				}
				Integer dummyWorkpackageId=999999999;
				workflowStatusPolicyService.setInstanceIndicators(engagementId,productId,dummyWorkpackageId, IDPAConstants.ENTITY_ACTIVITY_TYPE, jsonActivitieslist, IDPAConstants.ENTITY_ACTIVITY_ID,user, null, null, null,!IDPAConstants.ENABLE_PENDING_WITH_COLUMN);
			}
			
			
			Collections.sort(jsonActivitieslist, new Comparator<JsonActivity>() {
				@Override
				public int compare(JsonActivity activity, JsonActivity activity2) {
					if(activity.getRemainingHours() != null && activity2.getRemainingHours() != null){
						return activity.getRemainingHours().compareTo(activity2.getRemainingHours());
					}else {
						return 0;
					}
				}
			});
			
			jsonActivitiesReturnList = (List<JsonActivity>) workflowStatusPolicyService.getPaginationListFromFullList(jsonActivitieslist, 0, 1000);
		} catch (Exception e) {
			log.error("ERROR  ",e);
		}
		return jsonActivitiesReturnList;
	}
}
