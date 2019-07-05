/**
 * 
 */
package com.hcl.atf.taf.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.dao.ProductMasterDAO;
import com.hcl.atf.taf.model.NotificationMaster;
import com.hcl.atf.taf.model.NotificationPolicy;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.json.JsonNotification;
import com.hcl.atf.taf.model.json.JsonNotificationPolicy;
import com.hcl.atf.taf.model.json.jtable.JTableResponse;
import com.hcl.atf.taf.model.json.jtable.JTableResponseOptions;
import com.hcl.atf.taf.service.NotificationService;
import com.hcl.atf.taf.service.UserListService;
import com.hcl.ilcm.workflow.model.json.JsonCommonOption;

/**
 * @author silambarasur
 * 
 */
@Controller
public class NotificationController {
	
	private static final Log log = LogFactory.getLog(NotificationController.class);

	@Autowired
	private NotificationService notificationService;
	
	@Autowired
	private ProductMasterDAO productMasterDAO;
	
	@Autowired
	UserListService userListService;

	@RequestMapping(value = "notification.policy.management.list", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	JTableResponse getNotificationPolicyList(HttpServletRequest req,
			@RequestParam int isActive,@RequestParam Integer productId, @RequestParam int jtStartIndex,
			@RequestParam int jtPageSize) {
		JTableResponse jTableResponse = null;
		List<JsonNotificationPolicy> jsonNotification = new ArrayList<JsonNotificationPolicy>();
		List<NotificationPolicy> notificationList = null;
		try {
			notificationList = notificationService.getNotificationPolicyByProductId(productId);
			if (notificationList != null && notificationList.size() > 0) {
				for (NotificationPolicy notification : notificationList) {
					jsonNotification.add(new JsonNotificationPolicy(notification));
				}
			}
			jTableResponse = new JTableResponse("OK", jsonNotification,
					jsonNotification.size());
			log.info("process fetching notification policy records completed");
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR",
					"Error fetching records!");
			log.error("JSON ERROR", e);
		}
		return jTableResponse;
	}

	@RequestMapping(value = "notification.policy.management.add", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	JTableResponse addNotificationPolicyManagement(HttpServletRequest request,
			@ModelAttribute JsonNotificationPolicy jsonNotification,
			BindingResult result) {
		JTableResponse jTableResponse = null;
		List<JsonNotificationPolicy> jsonNotificationList=new ArrayList<>();
		try {

			NotificationPolicy notification = jsonNotification.getNotificationPolicy();
			notificationService.addNotificationPolicy(notification);
			jsonNotificationList.add(new JsonNotificationPolicy(notification));
			jTableResponse = new JTableResponse("OK",jsonNotificationList);
			log.info("notification.policy.managment.add Success");
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR",
					"Error adding notification management data record!");
			log.error("JSON ERROR", e);
		}
		return jTableResponse;
	}

	@RequestMapping(value = "notification.policy.management.update", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	JTableResponse updateNotificationPolicyManagement(HttpServletRequest request,
			@ModelAttribute JsonNotificationPolicy jsonNotification,
			BindingResult result) {
		JTableResponse jTableResponse = null;
		List<JsonNotificationPolicy> jsonNotificationList=new ArrayList<>();
		try {

			NotificationPolicy notification = jsonNotification.getNotificationPolicy();
			notificationService.updateNotificationPolicy(notification);
			jsonNotificationList.add(new JsonNotificationPolicy(notification));
			jTableResponse = new JTableResponse("OK",jsonNotificationList);
			log.info("notification.policy.managment.update Success");
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR",
					"Error updating notification management data record!");
			log.error("JSON ERROR", e);
		}
		return jTableResponse;
	}
	
	
	@RequestMapping(value="notification.management.option.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions getNotificationManagementOptionList(@RequestParam Integer activeState) {
		log.debug("inside notification.management.option.list");
		JTableResponseOptions jTableResponseOptions = null;
			try {
				
				List<NotificationMaster> notificationList=notificationService.getAllNotificationList(activeState);
				List<JsonNotification> josnNotificationList=new ArrayList<JsonNotification>();
				if(notificationList != null && notificationList.size() >0) {
					for(NotificationMaster notification:notificationList){
						josnNotificationList.add(new JsonNotification(notification));
					}
				}
				jTableResponseOptions = new JTableResponseOptions("OK", josnNotificationList, true);     
		        } catch (Exception e) {
		        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error getNotificationManagementOptionList records!");
		            log.error("JSON ERROR", e);
		        }
		        
	        return jTableResponseOptions;
    }
	
	@RequestMapping(value="notification.user.list.by.resourcepool.product",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponseOptions listUsersbyResourcePoolIdAndEngement(@RequestParam Integer productId) {

		log.debug("inside notification.user.list.by.resourcepool.product");
		JTableResponseOptions jTableResponseOptions = null;
		List<UserList> userList=new ArrayList<UserList>();
		try {
			List<JsonCommonOption> jsonCommonOptions= new ArrayList<JsonCommonOption>();
			JsonCommonOption commonOption=new JsonCommonOption();
			commonOption.setValue(IDPAConstants.NOTIFICATION_RECIPIENT_ASSIGNEE);
			jsonCommonOptions.add(commonOption);
			commonOption=new JsonCommonOption();
			commonOption.setValue(IDPAConstants.NOTIFICATION_RECIPIENT_REVIEWER);
			jsonCommonOptions.add(commonOption);
			commonOption=new JsonCommonOption();
			commonOption.setValue(IDPAConstants.NOTIFICATION_RECIPIENT_OWNER);
			jsonCommonOptions.add(commonOption);
			
			commonOption=new JsonCommonOption();
			commonOption.setValue(IDPAConstants.NOTIFICATION_RECIPIENT_TESTLEAD);
			jsonCommonOptions.add(commonOption);
			
			commonOption=new JsonCommonOption();
			commonOption.setValue(IDPAConstants.NOTIFICATION_RECIPIENT_TESTMANAGER);
			jsonCommonOptions.add(commonOption);
			
			commonOption=new JsonCommonOption();
			commonOption.setValue(IDPAConstants.NOTIFICATION_RECIPIENT_ENGAGEMENTMANAGER);
			jsonCommonOptions.add(commonOption);
			userList=userListService.getActivityUserListBasedRoleIdAndProductId(-10,productId);
			for(UserList ul: userList){
				commonOption=new JsonCommonOption();
				commonOption.setValue(ul.getUserDisplayName());
				jsonCommonOptions.add(commonOption);
				
			}
			
			
			
			jTableResponseOptions = new JTableResponseOptions("OK", jsonCommonOptions,false);
	           
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }
	        
        return jTableResponseOptions;
    }
}
