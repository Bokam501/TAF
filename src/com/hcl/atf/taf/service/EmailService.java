package com.hcl.atf.taf.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.hcl.atf.taf.model.Activity;
import com.hcl.atf.taf.model.ActivityTask;
import com.hcl.atf.taf.model.ActivityWorkPackage;
import com.hcl.atf.taf.model.TestFactoryResourceReservation;
import com.hcl.atf.taf.model.TestRunJob;
import com.hcl.atf.taf.model.TestRunList;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.WorkPackage;

public interface EmailService {

	void sendTestRunReportMail(TestRunList testRunList, String reportPath);

	void sendTestRunCompletionMail(TestRunList testRunList);

	void sendTestRunCompletionMail(Integer testRunListId);

	void sendTestRunReportMail(Integer testRunListId, String reportPath);
	
	void sendUserPasswordUpdateMail(HttpServletRequest request,UserList userList,String password);
	void sendUserCreation(HttpServletRequest request,UserList userList, String password);
	
	void sendResourceBookingNotificationMail();
	void sendResourceReservationOrCancellationNotificationMail(HttpServletRequest request, TestFactoryResourceReservation resourceReservation,int typeOfActivity);

	void sendTestRunPlanCompletionMail(TestRunJob testRunJob);
	
	void sendActivityTaskStatusChangeMail(HttpServletRequest request,String assignee,String reviewer,ActivityTask jsonActivityTask,String activityName,String productName,String productVersionName,String productBuildName,String awpName,Integer productId);
	
	void sendActivityManagementMail(HttpServletRequest request,String assignee,String reviewer,Activity activity,String productName,String productVersionName,String productBuildName,String awpName,Integer productId);
	
	void sendActivityWorkPkgMail(HttpServletRequest request,ActivityWorkPackage activityWorkPkg,String productName,String productVersionName,String productBuildName,Integer productId);
	
	void sendEmail(String subject, String contentMessage, String emailIds, String attachmentPaths);
	
	void sendWorkPackageReportMail(WorkPackage workpackage);
	
	void sendEmailWhenWorkflowStatusChange(String[] emailIds, String[] ccMailIds, String firstNames, String entityTypeName, String instanceNames, String actionBy, String comments, Date eventTime, String movedFrom, String movedTo, String pendingWith, Integer slaDuration,Integer entityInstanceId);
	
	void sendEntityBasedEmailNotification(List<String> emails, List<String> ccMails,String fromMailId,Map<String,Object> templateModel,Integer entityTypeId,String subject);
	
	void sendBotEmailNotifications(String[] primaryEmailIds, String[] secondaryEmailIds,String subject, String bodyMessage, String[] attachmentLocations);
	
	void sendPokeEmailNotifications(String[] primaryEmailIds, String[] secondaryEmailIds,String subject, String bodyMessage);
	
	void sendReportIssue(String fromMail,String[] primaryEmailIds, String[] secondaryEmailIds,String subject, String bodyMessage);

	String sendOutLookEmail(Map<String, Object> params);

	String sendWorkpackageCompletedReport(WorkPackage workPackage,String toRecipients, String ccRecipients);
}