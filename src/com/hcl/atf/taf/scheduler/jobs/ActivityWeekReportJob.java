/**
 * 
 */
package com.hcl.atf.taf.scheduler.jobs;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.dao.ProductMasterDAO;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.service.ActivityService;

/**
 * @author silambarasur
 *
 */
@Service
public class ActivityWeekReportJob implements Job{

	private static final Log log = LogFactory
			.getLog(ActivityWeekReportJob.class);

	
	@Autowired
	private ActivityService activityService;
	@Autowired
	private ProductMasterDAO productMasterDAO;
	
	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
		try{
			String productName = jobExecutionContext.getJobDetail().getJobDataMap().getString("weeklyReport_productName");
			ProductMaster product=productMasterDAO.getProductByName(productName);
			if(product != null) {
				activityService.getWeeklyReportsForSLUActivities(product.getProductId(), IDPAConstants.ENTITY_ACTIVITY_TYPE);
			}
		}catch(Exception e) {
			log.error("Error in Activity Weekly Report Job Execution",e);
		}
		
	}

}
