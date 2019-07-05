/**
 * 
 */
package com.hcl.atf.taf.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.dao.NotificationPolicyDAO;
import com.hcl.atf.taf.dao.ProductMasterDAO;
import com.hcl.atf.taf.dao.UserListDAO;
import com.hcl.atf.taf.model.Activity;
import com.hcl.atf.taf.model.ActivityTask;
import com.hcl.atf.taf.model.ActivityWorkPackage;
import com.hcl.atf.taf.model.ChangeRequest;
import com.hcl.atf.taf.model.ClarificationTracker;
import com.hcl.atf.taf.model.EntityMaster;
import com.hcl.atf.taf.model.NotificationMaster;
import com.hcl.atf.taf.model.NotificationPolicy;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.TestFactory;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.service.EmailService;
import com.hcl.atf.taf.service.NotificationService;

/**
 * @author silambarasur
 * 
 */
@Service
public class NotificationServiceImpl implements NotificationService {

	private static final Log log = LogFactory
			.getLog(NotificationServiceImpl.class);

	@Autowired
	private NotificationPolicyDAO notificationPolicyDAO;

	@Autowired
	private UserListDAO userListDAO;

	@Autowired
	ProductMasterDAO productMasterDAO;

	@Autowired
	private EmailService emailService;
	
	@Value("#{ilcmProps['EMAIL_ALERT_NOTIFICATION']}")
    private String emailNotification;

	private static SimpleDateFormat dateFormate = new SimpleDateFormat(
			"dd/MM/yyyy");

	@Override
	@Transactional
	public List<NotificationPolicy> getAllNotificationPolicyList() {
		try {
			return notificationPolicyDAO.getAllNotificationPolicyList();
		} catch (Exception e) {
			log.error("Error getAllNotificationPolicyList", e);
		}
		return null;
	}

	@Override
	@Transactional
	public List<NotificationPolicy> getNotificationPolicyByProductId(
			Integer productId) {
		try {
			return notificationPolicyDAO
					.getNotificationPolicyByProductId(productId);
		} catch (Exception e) {
			log.error("Error getNotificationPolicyByProductId", e);
		}
		return null;
	}

	@Override
	@Transactional
	public NotificationPolicy getNotificationPolicyByNotificationId(
			Integer notificationId) {
		try {
			return notificationPolicyDAO
					.getNotificationPolicyByNotificationId(notificationId);
		} catch (Exception e) {
			log.error("Error getNotificationPolicyByNotificationId", e);
		}
		return null;

	}

	@Override
	@Transactional
	public void addNotificationPolicy(NotificationPolicy notificationPolicy) {
		try {
			notificationPolicyDAO.addNotificationPolicy(notificationPolicy);
		} catch (Exception e) {

		}
	}

	@Override
	@Transactional
	public void updateNotificationPolicy(NotificationPolicy notificationPolicy) {
		try {
			notificationPolicyDAO.updateNotificationPolicy(notificationPolicy);
		} catch (Exception e) {
			log.error("Error notificaiton service getAllNotificationList ");
		}
	}

	@Override
	@Transactional
	public List<NotificationMaster> getAllNotificationList(Integer activeState) {
		try {
			return notificationPolicyDAO.getAllNotificationList(activeState);
		} catch (Exception e) {
			log.error("Error notificaiton service getAllNotificationList ");
		}
		return null;
	}

	@Override
	@Transactional
	public void processNotification(HttpServletRequest request,Integer productId,String notificationName, Object entity,String Subject) {

		if(!emailNotification.equalsIgnoreCase("YES"))
			return;
		if (notificationName == null)
			return;
		List<NotificationPolicy> notificationList = notificationPolicyDAO.getNotificationByName(notificationName,productId);
		if (notificationList == null)
			return;
		for (NotificationPolicy notification : notificationList) {
			if (notification.getEmail() ==1) {
				processEmailNotification(request, notification, entity,Subject);
			}
			if (notification.getSms() == 1) {
			}
			if (notification.getWhatsapp() == 1) {
			}
			if (notification.getFacebook() == 1) {
			}
			if (notification.getTwitter() == 1) {
			}
		}

	}

	private void processEmailNotification(HttpServletRequest request,
			NotificationPolicy notification, Object entity,String subject) {

		Activity activity = null;
		ActivityWorkPackage activityWorkpackage=null;
		ActivityTask task=null;
		ClarificationTracker clarificationTracker=null;
		ChangeRequest changeRequest=null;
		UserList user = (UserList) request.getSession().getAttribute("USER");
		Integer entityTypeId=notification.getNotification().getEntity().getEntitymasterid();
		if (entity instanceof ActivityWorkPackage) {
			List<String> toRecipients = processRecipientsMailIds(notification.getPrimaryRecipients(),entity);
			List<String> ccRecipients = processRecipientsMailIds(notification.getSecondaryRecipients(), entity);
			activityWorkpackage = (ActivityWorkPackage) entity;
			emailService.sendEntityBasedEmailNotification(toRecipients,ccRecipients, user.getEmailId(),prepareActivityWorkPackageMailTemplateModel(activityWorkpackage),entityTypeId,subject);
		} else if (entity instanceof Activity) {
			List<String> toRecipients = processRecipientsMailIds(notification.getPrimaryRecipients(),entity);
			List<String> ccRecipients = processRecipientsMailIds(notification.getSecondaryRecipients(), entity);
			activity = (Activity) entity;
			 Map<String, Object> model=prepareActivityMailTemplateModel(activity);
			 model.put("message", "");
			emailService.sendEntityBasedEmailNotification(toRecipients,ccRecipients, user.getEmailId(),model,entityTypeId,subject);
		} else if (entity instanceof ActivityTask) {
			List<String> toRecipients = processRecipientsMailIds(notification.getPrimaryRecipients(),entity);
			List<String> ccRecipients = processRecipientsMailIds(notification.getSecondaryRecipients(), entity);
			task = (ActivityTask) entity;
			emailService.sendEntityBasedEmailNotification(toRecipients,ccRecipients, user.getEmailId(),prepareActivityTaskMailTemplateModel(task),entityTypeId,subject);
		}else if (entity instanceof ClarificationTracker) {
			List<String> toRecipients = processRecipientsMailIds(notification.getPrimaryRecipients(),entity);
			List<String> ccRecipients = processRecipientsMailIds(notification.getSecondaryRecipients(), entity);
			clarificationTracker = (ClarificationTracker) entity;
			emailService.sendEntityBasedEmailNotification(toRecipients,ccRecipients, user.getEmailId(),prepareClarificationTrackerMailTemplateModel(clarificationTracker),entityTypeId,subject);
		}else if (entity instanceof ChangeRequest) {
			List<String> toRecipients = processRecipientsMailIds(notification.getPrimaryRecipients(),entity);
			List<String> ccRecipients = processRecipientsMailIds(notification.getSecondaryRecipients(), entity);
			changeRequest = (ChangeRequest) entity;
			emailService.sendEntityBasedEmailNotification(toRecipients,ccRecipients, user.getEmailId(),prepareChangeRequestMailTemplateModel(changeRequest),entityTypeId,subject);
		}

	}



	private List<String> processRecipientsMailIds(String recipients, Object entity) {

		List<String> mailIds = new ArrayList<String>();
		String[] recipientsList = recipients.split(",");

		for (int i = 0; i < recipientsList.length; i++) {

			String recipient = recipientsList[i];
			if (recipient.equalsIgnoreCase("Owner")) {
				List<UserList> users = getRoleUserForEntity(entity,recipient);
				if (users != null && users.size()>0) {
					for (UserList user : users) {
						if(user != null)
						mailIds.add(user.getEmailId());
					}
				}
			} else if (recipient.equalsIgnoreCase("Assignee")) {
				List<UserList> users = getRoleUserForEntity(entity,recipient);
				if (users != null && users.size()>0) {
					for (UserList user : users) {
						if(user != null)
						mailIds.add(user.getEmailId());
					}
				}
			} else if (recipient.equalsIgnoreCase("Reviewer")) {
				List<UserList> users = getRoleUserForEntity(entity,recipient);
				if (users != null && users.size()>0) {
					for (UserList user : users) {
						if(user != null)
						mailIds.add(user.getEmailId());
					}
				}
			} else if (recipient.equalsIgnoreCase("TestManager")) {
				List<UserList> users = getRoleUserForEntity(entity,recipient);
				if (users != null && users.size()>0) {
					for (UserList user : users) {
						if(user != null)
						mailIds.add(user.getEmailId());
					}
				}
			} else if (recipient.equalsIgnoreCase("TestLead")) {
				List<UserList> users = getRoleUserForEntity(entity,recipient);
				if (users != null && users.size()>0) {
					for (UserList user : users) {
						if(user != null)
						mailIds.add(user.getEmailId());
					}
				}
			} else if (recipient.equalsIgnoreCase("Engagement Manager")) {
				List<UserList> users = getRoleUserForEntity(entity,recipient);
				if (users != null && users.size()>0) {
					for (UserList user : users) {
						if(user != null)
						mailIds.add(user.getEmailId());
					}
				}
			} else {
				UserList user = userListDAO.getByUserName(recipient);
				if(user != null)
				mailIds.add(user.getEmailId());
			}
		}

		return mailIds;
	}

	public List<UserList> getRoleUserForEntity(Object entity, String role) {

		UserList user = null;
		List<UserList> users = new ArrayList<UserList>();

		if (entity instanceof Activity) {
			Activity activity = (Activity) entity;
			if (role.equalsIgnoreCase("Owner")) {
				user=userListDAO.isUserListById(activity.getActivityWorkPackage().getOwner().getUserId());
				users.add(user);
			}
			if (role.equalsIgnoreCase("Assignee")) {
				user=userListDAO.isUserListById(activity.getAssignee().getUserId());
				users.add(user);
			} else if (role.equalsIgnoreCase("Reviewer")) {
				user=userListDAO.isUserListById(activity.getReviewer().getUserId());
				users.add(user);
			} else if (role.equalsIgnoreCase("TestManager")) {
				ProductMaster product = activity.getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster();
				users = userListDAO.getUserListBasedRoleAndProductId(product.getProductId(), role);
			} else if (role.equalsIgnoreCase("TestLead")) {
				ProductMaster product = activity.getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster();
				users = userListDAO.getUserListBasedRoleAndProductId(
						product.getProductId(), role);
			} else if (role.equalsIgnoreCase("Engagement Manager")) {
				TestFactory engagement = activity.getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getTestFactory();
				List<ProductMaster> products = productMasterDAO
						.getProductsByTestFactoryId(engagement.getTestFactoryId());
				if (products != null) {
					for (ProductMaster product : products) {
						users.addAll(userListDAO.getUserListBasedRoleAndProductId(product.getProductId(), role));
					}
				}
			}
		}
		
		
		if (entity instanceof ActivityWorkPackage) {
			ActivityWorkPackage activityWorkPacakge = (ActivityWorkPackage) entity;
			if (role.equalsIgnoreCase("Owner") || role.equalsIgnoreCase("Assignee") || role.equalsIgnoreCase("Reviewer")) {
				user=userListDAO.isUserListById( activityWorkPacakge.getOwner().getUserId());
				users.add(user);
			}  else if (role.equalsIgnoreCase("TestManager")) {
				ProductMaster product = activityWorkPacakge.getProductBuild().getProductVersion().getProductMaster();
				users = userListDAO.getUserListBasedRoleAndProductId(product.getProductId(), role);
			} else if (role.equalsIgnoreCase("TestLead")) {
				ProductMaster product = activityWorkPacakge.getProductBuild().getProductVersion().getProductMaster();
				users = userListDAO.getUserListBasedRoleAndProductId(
						product.getProductId(), role);
			} else if (role.equalsIgnoreCase("Engagement Manager")) {
				TestFactory engagement = activityWorkPacakge.getProductBuild().getProductVersion().getProductMaster().getTestFactory();
				List<ProductMaster> products = productMasterDAO
						.getProductsByTestFactoryId(engagement.getTestFactoryId());
				if (products != null && products.size() >0) {
					for (ProductMaster product : products) {
						users.addAll(userListDAO.getUserListBasedRoleAndProductId(product.getProductId(), role));
					}
				}
			}
		}
		
		if (entity instanceof ActivityTask) {
			ActivityTask task = (ActivityTask) entity;
			if (role.equalsIgnoreCase("Owner")) {
				user=userListDAO.isUserListById( task.getActivity().getActivityWorkPackage().getOwner().getUserId());
				users.add(user);
			}
			if (role.equalsIgnoreCase("Assignee")) {
				user=userListDAO.isUserListById(task.getActivity().getAssignee().getUserId());
				users.add(user);
			} else if (role.equalsIgnoreCase("Reviewer")) {
				user=userListDAO.isUserListById(task.getActivity().getReviewer().getUserId());
				users.add(user);
			} else if (role.equalsIgnoreCase("TestManager")) {
				
				ProductMaster product = task.getActivity().getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster();
				users = userListDAO.getUserListBasedRoleAndProductId(product.getProductId(), role);
			} else if (role.equalsIgnoreCase("TestLead")) {
				ProductMaster product = task.getActivity().getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster();
				users = userListDAO.getUserListBasedRoleAndProductId(
						product.getProductId(), role);
			} else if (role.equalsIgnoreCase("Engagement Manager")) {
				TestFactory engagement = task.getActivity().getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getTestFactory();
				List<ProductMaster> products = productMasterDAO
						.getProductsByTestFactoryId(engagement.getTestFactoryId());
				if (products != null) {
					for (ProductMaster product : products) {
						users.addAll(userListDAO.getUserListBasedRoleAndProductId(product.getProductId(), role));
					}
				}
			}
		}
		
		
		if (entity instanceof ClarificationTracker) {
			ClarificationTracker clarification = (ClarificationTracker) entity;
			if (role.equalsIgnoreCase("Owner") || role.equalsIgnoreCase("Reviewer")) {
				user=userListDAO.isUserListById(clarification.getOwner().getUserId());
				users.add(user);
			}
			if (role.equalsIgnoreCase("Assignee")) {
				user=userListDAO.isUserListById( clarification.getRaisedBy().getUserId());
				users.add(user);
			}  else if (role.equalsIgnoreCase("TestManager")) {
				ProductMaster product = clarification.getProduct();
				users = userListDAO.getUserListBasedRoleAndProductId(product.getProductId(), role);
			} else if (role.equalsIgnoreCase("TestLead")) {
				ProductMaster product = clarification.getProduct();
				users = userListDAO.getUserListBasedRoleAndProductId(
						product.getProductId(), role);
			} else if (role.equalsIgnoreCase("Engagement Manager")) {
				TestFactory engagement = clarification.getProduct().getTestFactory();
				List<ProductMaster> products = productMasterDAO
						.getProductsByTestFactoryId(engagement.getTestFactoryId());
				if (products != null) {
					for (ProductMaster product : products) {
						users.addAll(userListDAO.getUserListBasedRoleAndProductId(product.getProductId(), role));
					}
				}
			}
		}
		

		if (entity instanceof ChangeRequest) {
			ChangeRequest changeRequest = (ChangeRequest) entity;
			if (role.equalsIgnoreCase("Owner") || role.equalsIgnoreCase("Reviewer") || role.equalsIgnoreCase("Assignee")) {
				user=userListDAO.isUserListById( changeRequest.getOwner().getUserId());
				users.add(user);
			} else if (role.equalsIgnoreCase("TestManager")) {
				ProductMaster product = changeRequest.getProduct();
				users = userListDAO.getUserListBasedRoleAndProductId(product.getProductId(), role);
			} else if (role.equalsIgnoreCase("TestLead")) {
				ProductMaster product = changeRequest.getProduct();
				users = userListDAO.getUserListBasedRoleAndProductId(
						product.getProductId(), role);
			} else if (role.equalsIgnoreCase("Engagement Manager")) {
				TestFactory engagement = changeRequest.getProduct().getTestFactory();
				List<ProductMaster> products = productMasterDAO
						.getProductsByTestFactoryId(engagement.getTestFactoryId());
				if (products != null) {
					for (ProductMaster product : products) {
						users.addAll(userListDAO.getUserListBasedRoleAndProductId(product.getProductId(), role));
					}
				}
			}
		}
		
		return users;
	}

	@Override
	@Transactional
	public void prepareNotificationMaster(UserList user) {
		try {
		List<NotificationMaster> notificationList = new ArrayList<NotificationMaster>();
		NotificationMaster notification = new NotificationMaster();
		EntityMaster entity = new EntityMaster();
		notification
				.setNotificationName(IDPAConstants.NOTIFICATION_ACTIVITY_CREATION);
		notification.setNotificationGroup("Activity");
		entity.setEntitymasterid(IDPAConstants.ENTITY_ACTIVITY_TYPE);
		notification.setEntity(entity);
		notification.setActiveState(1);
		notification.setCreatedBy(user);
		notification.setCreatedDate(new Date());
		notification.setModifiedBy(user);
		notification.setModifiedDate(new Date());
		notificationList.add(notification);
		
		
		notification = new NotificationMaster();
		entity = new EntityMaster();
		notification.setNotificationName(IDPAConstants.NOTIFICATION_ACTIVITY_UPDATION);
		notification.setNotificationGroup("Activity");
		entity.setEntitymasterid(IDPAConstants.ENTITY_ACTIVITY_TYPE);
		notification.setEntity(entity);
		notification.setActiveState(1);
		notification.setCreatedBy(user);
		notification.setCreatedDate(new Date());
		notification.setModifiedBy(user);
		notification.setModifiedDate(new Date());
		notificationList.add(notification);
		
		notification = new NotificationMaster();
		entity = new EntityMaster();
		notification.setNotificationName(IDPAConstants.NOTIFICATION_ACTIVITY_DELETION);
		notification.setNotificationGroup("Activity");
		entity.setEntitymasterid(IDPAConstants.ENTITY_ACTIVITY_TYPE);
		notification.setEntity(entity);
		notification.setActiveState(1);
		notification.setCreatedBy(user);
		notification.setCreatedDate(new Date());
		notification.setModifiedBy(user);
		notification.setModifiedDate(new Date());
		notificationList.add(notification);
		
		notification = new NotificationMaster();
		entity = new EntityMaster();
		notification.setNotificationName(IDPAConstants.NOTIFICATION_ACTIVITY_WORKPACKAGE_CREATION);
		notification.setNotificationGroup("Activity Work Package");
		entity.setEntitymasterid(IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE_ID);
		notification.setEntity(entity);
		notification.setActiveState(1);
		notification.setCreatedBy(user);
		notification.setCreatedDate(new Date());
		notification.setModifiedBy(user);
		notification.setModifiedDate(new Date());
		notificationList.add(notification);
		
		
		notification = new NotificationMaster();
		entity = new EntityMaster();
		notification.setNotificationName(IDPAConstants.NOTIFICATION_ACTIVITY_WORKPACKAGE_UPDATION);
		notification.setNotificationGroup("Activity Work Package");
		entity.setEntitymasterid(IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE_ID);
		notification.setEntity(entity);
		notification.setActiveState(1);
		notification.setCreatedBy(user);
		notification.setCreatedDate(new Date());
		notification.setModifiedBy(user);
		notification.setModifiedDate(new Date());
		notificationList.add(notification);
		
		
		notification = new NotificationMaster();
		entity = new EntityMaster();
		notification.setNotificationName(IDPAConstants.NOTIFICATION_ACTIVITY_WORKPACKAGE_DELETION);
		notification.setNotificationGroup("Activity Work Package");
		entity.setEntitymasterid(IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE_ID);
		notification.setEntity(entity);
		notification.setActiveState(1);
		notification.setCreatedBy(user);
		notification.setCreatedDate(new Date());
		notification.setModifiedBy(user);
		notification.setModifiedDate(new Date());
		notificationList.add(notification);
		
		
		notification = new NotificationMaster();		
		entity = new EntityMaster();
		notification.setNotificationName(IDPAConstants.NOTIFICATION_ACTIVITY_TASK_CREATION);
		notification.setNotificationGroup("Activity Task");
		entity.setEntitymasterid(IDPAConstants.ENTITY_TASK_TYPE);
		notification.setEntity(entity);
		notification.setActiveState(1);
		notification.setCreatedBy(user);
		notification.setCreatedDate(new Date());
		notification.setModifiedBy(user);
		notification.setModifiedDate(new Date());
		notificationList.add(notification);
		
		notification = new NotificationMaster();
		entity = new EntityMaster();
		notification.setNotificationName(IDPAConstants.NOTIFICATION_ACTIVITY_TASK_UPDATION);
		notification.setNotificationGroup("Activity Task");
		entity.setEntitymasterid(IDPAConstants.ENTITY_TASK_TYPE);
		notification.setEntity(entity);
		notification.setActiveState(1);
		notification.setCreatedBy(user);
		notification.setCreatedDate(new Date());
		notification.setModifiedBy(user);
		notification.setModifiedDate(new Date());
		notificationList.add(notification);
		
		notification = new NotificationMaster();
		entity = new EntityMaster();
		notification.setNotificationName(IDPAConstants.NOTIFICATION_ACTIVITY_TASK_DELETION);
		notification.setNotificationGroup("Activity Task");
		entity.setEntitymasterid(IDPAConstants.ENTITY_TASK_TYPE);
		notification.setEntity(entity);
		notification.setActiveState(1);
		notification.setCreatedBy(user);
		notification.setCreatedDate(new Date());
		notification.setModifiedBy(user);
		notification.setModifiedDate(new Date());
		notificationList.add(notification);
		
		notification = new NotificationMaster();
		entity = new EntityMaster();
		notification.setNotificationName(IDPAConstants.NOTIFICATION_CHANGE_REQUEST_CREATION);
		notification.setNotificationGroup("Change Request");
		entity.setEntitymasterid(IDPAConstants.CHANGE_REQUEST_ENTITY_MASTER_ID);
		notification.setEntity(entity);
		notification.setActiveState(1);
		notification.setCreatedBy(user);
		notification.setCreatedDate(new Date());
		notification.setModifiedBy(user);
		notification.setModifiedDate(new Date());
		notificationList.add(notification);
		
		notification = new NotificationMaster();
		entity = new EntityMaster();
		notification.setNotificationName(IDPAConstants.NOTIFICATION_CHANGE_REQUEST_UPDATION);
		notification.setNotificationGroup("Change Request");
		entity.setEntitymasterid(IDPAConstants.CHANGE_REQUEST_ENTITY_MASTER_ID);
		notification.setEntity(entity);
		notification.setActiveState(1);
		notification.setCreatedBy(user);
		notification.setCreatedDate(new Date());
		notification.setModifiedBy(user);
		notification.setModifiedDate(new Date());
		notificationList.add(notification);
		
		notification = new NotificationMaster();
		entity = new EntityMaster();
		notification.setNotificationName(IDPAConstants.NOTIFICATION_CLARIFICATION_TRACKER_CREATION);
		notification.setNotificationGroup("Clarification Tracker");
		entity.setEntitymasterid(IDPAConstants.CLARIFICATION_ENTITY_MASTER_ID);
		notification.setEntity(entity);
		notification.setActiveState(1);
		notification.setCreatedBy(user);
		notification.setCreatedDate(new Date());
		notification.setModifiedBy(user);
		notification.setModifiedDate(new Date());
		notificationList.add(notification);
		
		notification = new NotificationMaster();
		entity = new EntityMaster();
		notification.setNotificationName(IDPAConstants.NOTIFICATION_CLARIFICATION_TRACKER_UPDATION);
		notification.setNotificationGroup("Clarification Tracker");
		entity.setEntitymasterid(IDPAConstants.CLARIFICATION_ENTITY_MASTER_ID);
		notification.setEntity(entity);
		notification.setActiveState(1);
		notification.setCreatedBy(user);
		notification.setCreatedDate(new Date());
		notification.setModifiedBy(user);
		notification.setModifiedDate(new Date());
		notificationList.add(notification);
		
		notificationPolicyDAO.bulkAddNotification(notificationList);
		}catch(Exception e) {
			log.error("Error in preparing notificaiton master");
		}
	}

	@Override
	@Transactional
	public void prepareNotificationPolicy(Integer productId) {
		List<NotificationPolicy> notificationPolicyList = new ArrayList<NotificationPolicy>();
		Integer activeState = 1;
		try {
			List<NotificationMaster> notificationList = notificationPolicyDAO.getAllNotificationList(activeState);
			NotificationPolicy notificationPolicy = null;
			ProductMaster product = new ProductMaster();
			product.setProductId(productId);
			for (NotificationMaster notification : notificationList) {
				notificationPolicy = new NotificationPolicy();
				notificationPolicy.setNotification(notification);
				notificationPolicy.setEmail(1);
				notificationPolicy.setProduct(product);
				notificationPolicy.setPrimaryRecipients(IDPAConstants.NOTIFICATION_RECIPIENT_ASSIGNEE);
				notificationPolicy.setSecondaryRecipients(IDPAConstants.NOTIFICATION_RECIPIENT_TESTMANAGER);
				notificationPolicyList.add(notificationPolicy);
			}
			notificationPolicyDAO.bulkAddNotificationPolicy(notificationPolicyList);
		}catch(Exception e) {
			log.error("Error in preparing notificaiton policy");
		}
		
	}
	
	@Override
	public Map<String, Object> prepareActivityMailTemplateModel(Activity activity) {
		Map<String, Object> model = new HashMap<String, Object>();
		try {
			String activityWorkPackageName = activity.getActivityWorkPackage() != null ? activity
					.getActivityWorkPackage().getActivityWorkPackageName() : "";
			String productBuildName = activity.getActivityWorkPackage() != null ? activity
					.getActivityWorkPackage().getProductBuild().getBuildname()
					: "";
			String productVersionName = activity.getActivityWorkPackage() != null ? activity
					.getActivityWorkPackage().getProductBuild()
					.getProductVersion().getProductVersionName()
					: "";
			String productName = activity.getActivityWorkPackage()
					.getProductBuild().getProductVersion().getProductMaster()
					.getProductName();
			model.put("activityName", activity.getActivityName());
			model.put("product", productName);
			model.put("buildName", productBuildName);
			model.put("version", productVersionName);
			model.put("activityWorkpkgName", activityWorkPackageName);
			model.put("plannedStartDate",activity.getPlannedStartDate() == null ? "" : dateFormate
							.format(activity.getPlannedStartDate()));
			model.put("plannedEndDate",activity.getPlannedEndDate() == null ? "" : dateFormate
							.format(activity.getPlannedEndDate()));
			model.put("productMangerMailIds", "");
			model.put("SRSSection", activity.getProductFeature() != null? activity.getProductFeature().getProductFeatureName():"");
			model.put("ActivityTrackerNumber",activity.getActivityTrackerNumber());
			model.put("category", activity.getCategory().getName());
			model.put("assignee", activity.getAssignee() != null? activity.getAssignee().getLoginId():"");
			model.put("reviewer", activity.getReviewer() != null ?activity.getReviewer().getLoginId():"");
			model.put("priority", activity.getPriority() !=null ?activity.getPriority().getExecutionPriorityName():"");
			model.put("status category", activity.getStatusCategory() != null ?activity.getStatusCategory().getStatusCategoryName():"");
			model.put("status", activity.getWorkflowStatus() !=null ? activity.getWorkflowStatus().getWorkflowStatusDisplayName():"");
			model.put("isActive", activity.getIsActive());
			model.put("actualStartDate",activity.getActualStartDate() == null ? "" : dateFormate.format(activity.getActualStartDate()));
			model.put("actualEndDate", activity.getActualEndDate() == null ? "": dateFormate.format(activity.getActualEndDate()));
			model.put("fromMailId", "");
			model.put("summary", "Activity status changed to "+activity.getWorkflowStatus() != null?activity.getWorkflowStatus().getWorkflowStatusDisplayName():"");
			model.put("activityID", activity.getActivityId());
			model.put("activityCreationDate", activity.getCreatedDate());
			
			
		} catch (Exception e) {
			log.error("Error in Activity mail template prepration",e);
		}
		return model;
	}
	
	private Map<String, Object> prepareActivityWorkPackageMailTemplateModel(ActivityWorkPackage activityWorkPackage) {
		Map<String, Object> model = new HashMap<String, Object>();
		try {
				String productBuildName = activityWorkPackage.getProductBuild().getBuildname();
				String productVersionName =activityWorkPackage.getProductBuild().getProductVersion().getProductVersionName();
				String productName = activityWorkPackage.getProductBuild().getProductVersion().getProductMaster()
						.getProductName();
				Integer productId =activityWorkPackage.getProductBuild().getProductVersion().getProductMaster().getProductId();
				List<UserList> users = userListDAO.getUserListBasedRoleAndProductId(productId, "TestManager");
				List<String> managerMailIds= new ArrayList<String>();
				if (users != null && users.size()>0) {
					for (UserList user : users) {
						managerMailIds.add(user.getEmailId());
					}
				}
			
			 	model.put("product", productName);
	            model.put("buildName", productBuildName);
	            model.put("version", productVersionName);
	            model.put("activityWorkpkgName", activityWorkPackage.getActivityWorkPackageName());
	            model.put("plannedStartDate", activityWorkPackage.getPlannedStartDate() == null ?"":dateFormate.format(activityWorkPackage.getPlannedStartDate()));
	            model.put("plannedEndDate", activityWorkPackage.getPlannedEndDate() == null ?"":dateFormate.format(activityWorkPackage.getPlannedEndDate()));
	            model.put("productMangerMailIds", managerMailIds);
	            
	            model.put("owner", activityWorkPackage.getOwner().getUserDisplayName());
	            model.put("priority", activityWorkPackage.getPriority().getExecutionPriorityName());
	            //model.put("status", workPkage.getStatus().getActivityStatusName());
	            model.put("status category", activityWorkPackage.getStatusCategory().getStatusCategoryName());
	            model.put("isActive", activityWorkPackage.getIsActive());
	            model.put("actualStartDate", activityWorkPackage.getActualStartDate() == null ? "":dateFormate.format(activityWorkPackage.getActualStartDate()));
	            model.put("actualEndDate", activityWorkPackage.getActualEndDate() == null ?"":dateFormate.format(activityWorkPackage.getActualEndDate()));
	          //  model.put("fromMailId", fromMailId);
	            model.put("summary", "Activity Workpackage status category changed to "+activityWorkPackage.getStatusCategory().getStatusCategoryName());
		} catch (Exception e) {
			log.error("Error in Activity Workpackage mail template prepration");
		}
		return model;
	}
	
	
	private Map<String, Object> prepareActivityTaskMailTemplateModel(ActivityTask task) {
		Map<String, Object> model = new HashMap<String, Object>();
		try {
			String activityWorkPackageName = task.getActivity().getActivityWorkPackage() != null ? task.getActivity()
					.getActivityWorkPackage().getActivityWorkPackageName() : "";
			String productBuildName = task.getActivity().getActivityWorkPackage() != null ? task.getActivity()
					.getActivityWorkPackage().getProductBuild().getBuildname()
					: "";
			String productVersionName = task.getActivity().getActivityWorkPackage() != null ? task.getActivity()
					.getActivityWorkPackage().getProductBuild()
					.getProductVersion().getProductVersionName()
					: "";
			String productName = task.getActivity().getActivityWorkPackage()
					.getProductBuild().getProductVersion().getProductMaster()
					.getProductName();
			String activityName=task.getActivity().getActivityName();
			
			Integer productId = task.getActivity().getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductId();
			List<UserList> users = userListDAO.getUserListBasedRoleAndProductId(productId, "TestManager");
			List<String> managerMailIds= new ArrayList<String>();
			if (users != null && users.size()>0) {
				for (UserList user : users) {
					managerMailIds.add(user.getEmailId());
				}
			}
            model.put("status", task.getStatus().getWorkflowStatusDisplayName());
            model.put("assignee", "");
            model.put("reviewer", "");
            model.put("taskName", task.getActivityTaskName());
            model.put("taskTypeName", task.getActivityTaskType().getActivityTaskTypeName() == null ?"":task.getActivityTaskType().getActivityTaskTypeName());
            model.put("category", task.getCategory().getName() == null ?"":task.getCategory().getName());
            model.put("priority", task.getPriority().getExecutionPriorityName() == null ?"":task.getPriority().getExecutionPriorityName());
            model.put("result", task.getResult().getActivityResultName());
            model.put("actualStartDate", task.getActualStartDate() == null ? "":dateFormate.format(task.getActualStartDate()));
            model.put("actualEndDate", task.getActualEndDate() == null ?"":dateFormate.format(task.getActualEndDate()));
            model.put("summary", "Activity task status changed to "+task.getStatus().getWorkflowStatusDisplayName());
            
            model.put("activityName", activityName);
            model.put("product", productName);
            model.put("buildName", productBuildName);
            model.put("version", productVersionName);
            model.put("activityWorkpkgName", activityWorkPackageName);
            model.put("plannedStartDate", task.getPlannedStartDate() == null ?"":dateFormate.format(task.getPlannedStartDate()));
            model.put("plannedEndDate", task.getPlannedEndDate() == null ?"":dateFormate.format(task.getPlannedEndDate()));
            model.put("baselineStartDate", task.getBaselineStartDate() == null ?"":dateFormate.format(task.getBaselineStartDate()));
            model.put("baselineEndDate", task.getBaselineEndDate() == null ?"":dateFormate.format(task.getBaselineEndDate()));
            model.put("productMangerMailIds",managerMailIds );
		} catch (Exception e) {
			log.error("Error in Activity Task mail template prepration");
		}
		
		return model;
	}
	
	
	private Map<String, Object> prepareClarificationTrackerMailTemplateModel(ClarificationTracker clarificationTracker) {
		Map<String, Object> model = new HashMap<String, Object>();
		try {
			model.put("clarificationTitle",clarificationTracker.getClarificationTitle());
			model.put("expactedEndDate",clarificationTracker.getPlannedEndDate());
			model.put("raisedBy",clarificationTracker.getRaisedBy().getUserDisplayName());
			model.put("clarificationScope",clarificationTracker.getClarificationScope().getClarificationScopeName());
			model.put("clarificationType",clarificationTracker.getClarificationType().getClarificationType());
            model.put("engagementName", clarificationTracker.getTestFactory().getTestFactoryName());
            model.put("product", clarificationTracker.getProduct().getProductName());
            model.put("clarificationTrackerNumber", clarificationTracker.getClarificationTrackerId());
            model.put("priority", clarificationTracker.getPriority().getExecutionPriority());
            model.put("raisedDate", clarificationTracker.getRaisedDate());
            model.put("status", clarificationTracker.getEntityStatus().getEntityStatusName());
            model.put("owner", clarificationTracker.getOwner());
		} catch (Exception e) {
			log.error("Error in Clarification mail template prepration");
		}
		
		return model;
	}
	
	private Map<String, Object> prepareChangeRequestMailTemplateModel(ChangeRequest changeRequest) {
		Map<String, Object> model = new HashMap<String, Object>();
		try {
			model.put("changeRequstName",changeRequest.getChangeRequestName());
            model.put("engagementName", changeRequest.getProduct().getTestFactory().getTestFactoryName());
            model.put("product", changeRequest.getProduct().getProductName());
            model.put("changeRequstId", changeRequest.getChangeRequestId());
            model.put("priority", changeRequest.getPriority().getExecutionPriority());
            model.put("raisedDate", changeRequest.getRaisedDate());
            model.put("status", changeRequest.getStatusCategory().getStatusCategoryName());
            model.put("owner", changeRequest.getOwner());
		} catch (Exception e) {
			log.error("Error in Change Requst mail template prepration");
		}
		
		return model;
	}
}
