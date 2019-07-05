package com.hcl.atf.taf.scheduler.jobs;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.UserRoleMaster;
import com.hcl.atf.taf.model.json.JsonMetricsMaster;
import com.hcl.atf.taf.model.json.JsonMetricsMasterGroup;
import com.hcl.atf.taf.model.json.jtable.JTableResponse;
import com.hcl.atf.taf.mongodb.model.TrendCollectionMongo;
import com.hcl.atf.taf.mongodb.service.MongoDBService;
import com.hcl.atf.taf.service.DashBoardService;
import com.hcl.atf.taf.service.DimensionService;
import com.hcl.atf.taf.service.ProductListService;

@Service
public class SLATrendJob implements Job {
	
private static final Log log = LogFactory.getLog(SLATrendJob.class);
	@Autowired
	DashBoardService dashBoardService;
	@Autowired 
	ProductListService productListService;
	@Autowired
	private MongoDBService mongoDBService;	
	@Autowired
	private DimensionService dimensionService;	
			
	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		TrendCollectionMongo trendValue= new TrendCollectionMongo();
		try{
			
			JTableResponse summaryMetrics=null;
			SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
			UserList user=new UserList();
			UserRoleMaster role=new UserRoleMaster();
			role.setUserRoleId(IDPAConstants.ROLE_ID_ADMIN);
			
			
			Date startDate=null;
			Date endDate=null;
			user.setUserRoleMaster(role);
			
			List<ProductMaster> productList=productListService.listProduct();
			
			Integer summaryValue = jobExecutionContext.getJobDetail().getJobDataMap().getInt("summaryValue");
			String metricsURL = jobExecutionContext.getJobDetail().getJobDataMap().getString("metricsURL");
			startDate =mongoDBService.getStartDate(summaryValue);
			
			Calendar currentDateCalendar = Calendar.getInstance();
			currentDateCalendar.set(Calendar.HOUR_OF_DAY, 23);
			currentDateCalendar.set(Calendar.MINUTE, 59);
			currentDateCalendar.set(Calendar.SECOND, 59);
			currentDateCalendar.set(Calendar.MILLISECOND, 999);
			endDate = setDateForMongoDB(currentDateCalendar.getTime());
			log.info("endDate="+endDate); 
			
			String productName="";
			String testFactoryName = "";
			for(ProductMaster product : productList){
				productName=product.getProductName();
				testFactoryName = product.getTestFactory().getTestFactoryName();
				if("sla.dashboard.metrics.summary.calculation".equalsIgnoreCase(metricsURL)){
					summaryMetrics = dashBoardService.slaDashboardMetricsSummaryCalculation(startDate,endDate, testFactoryName, productName, user);
				}else if("dashboard.metrics.summary.calculation".equalsIgnoreCase(metricsURL)){
					summaryMetrics = dashBoardService.dashboardMetricsSummaryCalculation(startDate,endDate, testFactoryName, productName, user);
				}
				if(summaryMetrics!=null){
					trendValue=metricsSummaryProcessForTrend(summaryMetrics,product,summaryValue);
					mongoDBService.addTrendCollectionMongo(trendValue);
				}
			}
		}catch(Exception e){
			log.error(e);
		}
		
	}
	
	private TrendCollectionMongo metricsSummaryProcessForTrend(JTableResponse summaryMetrics,ProductMaster product,Integer summaryValue){
		TrendCollectionMongo trendValue= new TrendCollectionMongo();
		Date startDate = mongoDBService.getStartDate(summaryValue);
		Date endDate=new Date();
		try{
			String value="";
			for(Object jsonMetricsMasterGroupObject : summaryMetrics.getRecords()){
				JsonMetricsMasterGroup jsonMetricsMasterGroup = (JsonMetricsMasterGroup) jsonMetricsMasterGroupObject;
				if(jsonMetricsMasterGroup.getActualValue()!=null && !jsonMetricsMasterGroup.getActualValue().isEmpty()){
					value = jsonMetricsMasterGroup.getActualValue().replaceAll("%", "").replaceAll("/Day", "");
					setMetricsValueToCollectionEntity(trendValue, jsonMetricsMasterGroup.getGroupType(), value,product);
				}
				for(Object jsonMetricsMasterObject : jsonMetricsMasterGroup.getGroupValues()){
					JsonMetricsMaster jsonMetricsMaster = (JsonMetricsMaster) jsonMetricsMasterObject;
					if(jsonMetricsMaster.getActualValue()!=null && !jsonMetricsMaster.getActualValue().isEmpty()){
						 value = jsonMetricsMaster.getActualValue().replaceAll("%", "").replaceAll("/Day", "");
						 setMetricsValueToCollectionEntity(trendValue, jsonMetricsMaster.getMetricsType(), value,product);
					}
				}
			}
			trendValue.setStartDate(setDateForMongoDB(startDate));
			trendValue.setEndDate(setDateForMongoDB(endDate));
			trendValue.setCurrentDate(setDateForMongoDB(endDate));
			
		}catch(Exception e){
			log.error(e);
		}
		return trendValue;
	}
	
	
	private void  setMetricsValueToCollectionEntity(TrendCollectionMongo trendValue,String metric,String value,ProductMaster product){
		
		
		if(metric.equalsIgnoreCase(IDPAConstants.SCHEDULE_VARIANCE)){
			trendValue.setScheduleVariance(Float.parseFloat(value));
		}
		if(metric.equalsIgnoreCase(IDPAConstants.DEFECT_VALIDITY)){
			trendValue.setDefectValidity(Float.parseFloat(value));
		}
		if(metric.equalsIgnoreCase(IDPAConstants.DEFECT_QUALITY)){
			trendValue.setDefectQuality(Float.parseFloat(value));
		}
		if(metric.equalsIgnoreCase(IDPAConstants.TESTCASE_JOB_EXECUTION)){
			trendValue.setTestCaseJobExecution(Float.parseFloat(value));
		}
		if(metric.equalsIgnoreCase(IDPAConstants.COST_PER_UNIT)){
			trendValue.setCostPerUnit(Float.parseFloat(value));
		}
		if(metric.equalsIgnoreCase(IDPAConstants.DEFECT_FIND_RATE)){
			trendValue.setDefectFindRate(Float.parseFloat(value));
		}
		if(metric.equalsIgnoreCase(IDPAConstants.INTERNAL_DRE)){
			trendValue.setInternalDre(Float.parseFloat(value));
		}
		if(metric.equalsIgnoreCase(IDPAConstants.PRODUCT_QUALITY)){
			trendValue.setProductQuality(Float.parseFloat(value));
		}
		if(metric.equalsIgnoreCase(IDPAConstants.PRODUCTIVITY)){
			trendValue.setProductivity(Float.parseFloat(value));
		}
		
		if(metric.equalsIgnoreCase(IDPAConstants.UTILIZATION_INDEX)){
			trendValue.setUtilizationIndex(Float.parseFloat(value));
		}
		if(metric.equalsIgnoreCase(IDPAConstants.PCISCORE)){
			trendValue.setPciScore(Float.parseFloat(value));
		}
		if(metric.equalsIgnoreCase(IDPAConstants.CSAT)){
			trendValue.setCsat(Float.parseFloat(value));
		}
		if(metric.equalsIgnoreCase(IDPAConstants.ARID)){
			if(value != null && !"NA".equalsIgnoreCase(value)){
				trendValue.setArid(Float.parseFloat(value));
			}
		}
		if(metric.equalsIgnoreCase(IDPAConstants.HEALTH_INDEX)){
			trendValue.setHealthIndex(Float.parseFloat(value));
		}
		if(metric.equalsIgnoreCase(IDPAConstants.PRODUCT_CONFIDENCE)){
			trendValue.setProductConfidence(Float.parseFloat(value));
		}
		
		List<Object[]> dimensionMasters = dimensionService.getDimensionsOfProduct(product.getProductId(), 1, 1, null, null);
		String compentencies="";
		for (Object[] row : dimensionMasters) {
			if(compentencies!=null && !compentencies.isEmpty()){
				compentencies=	compentencies+","+(String)row[1];
			}else{
				compentencies=	(String)row[1];
			}
			
		}	
		log.info("compentencies Names "+compentencies);
		
		if(compentencies!=null && !compentencies.isEmpty()){
			trendValue.setCompetencyName(compentencies);
		}
		if(product!=null){
			trendValue.setProductId(product.getProductId());
			trendValue.setProductName(product.getProductName());
			
			if(product.getCustomer()!=null){
				trendValue.setCustomerId(product.getCustomer().getCustomerId());
				trendValue.setCustomerName(product.getCustomer().getCustomerName());
			}
			
			if(product.getTestFactory()!=null){
				trendValue.setTestFactoryId(product.getTestFactory().getTestFactoryId());
				trendValue.setTestFactoryName(product.getTestFactory().getTestFactoryName());
				
				if(product.getTestFactory().getTestFactoryLab()!=null){
					trendValue.setTestCentersId(product.getTestFactory().getTestFactoryLab().getTestFactoryLabId());
					trendValue.setTestCentersName(product.getTestFactory().getTestFactoryLab().getTestFactoryLabName());
				}
			}
		}
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
	
}
