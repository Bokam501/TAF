package com.hcl.atf.taf.controller.utilities;

import java.io.InputStream;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

public class PropertyLoader {
	public static String PASSWORD_UPDATE_SUBJECT = null;
	public static String USER_CREATION_SUBJECT = null;
	public static String USER_BOOKING_NOTIFICATION_SUBJECT = null;
	public static String USER_RESERVAION_NOTIFICATION_SUBJECT = null;
	public static String USER_UNRESERVAION_NOTIFICATION_SUBJECT = null;
	public static String PASSWORD_UPDATE_TEMPLATE_PATH = null;

	public static String SERVER_IP = null;
	public static String PORT = null;
	public static String DB_NAME = null;
	public static String ACTIVITY_TASK_STATUS_CHANGE_TEMPLATE_PATH = null;
	public static String ACTIVITY_MANAGEMENT_TEMPLATE_PATH = null;
	public static String ACTIVITY_WORK_PKG_TEMPLATE_PATH = null;
	public static String ACTIVITY_TASK_STATUS_CHANGE_SUBJECT = null;
	public static String ACTIVITY_MANAGEMENT_SUBJECT = null;
	public static String ACTIVITY_WORK_PKG_SUBJECT = null;

	InputStream propertyFileInputStream = this.getClass().getResourceAsStream("/TAFServer.properties");
	
	public static void loadProperties(HttpServletRequest request) {
		Properties properties = new Properties();
		try {
			properties.load(new PropertyLoader().propertyFileInputStream);
			PASSWORD_UPDATE_SUBJECT= properties.getProperty("mail.passwordupdate.subject");
			USER_CREATION_SUBJECT= properties.getProperty("mail.usercreation.subject");
			USER_BOOKING_NOTIFICATION_SUBJECT= properties.getProperty("mail.user.booking.notification.subject");
			USER_RESERVAION_NOTIFICATION_SUBJECT= properties.getProperty("mail.user.reservation.notification.subject");
			USER_UNRESERVAION_NOTIFICATION_SUBJECT= properties.getProperty("mail.user.unreservation.notification.subject");
			PASSWORD_UPDATE_TEMPLATE_PATH=properties.getProperty("mail.passwordupdate.template");
			ACTIVITY_TASK_STATUS_CHANGE_TEMPLATE_PATH=properties.getProperty("mail.activitytaskStatusChange.template");
			ACTIVITY_TASK_STATUS_CHANGE_SUBJECT=properties.getProperty("mail.actitivityStatus.subject");
			ACTIVITY_MANAGEMENT_SUBJECT=properties.getProperty("mail.actitivityManagement.subject");
			ACTIVITY_MANAGEMENT_TEMPLATE_PATH=properties.getProperty("mail.activityManagement.template");
			ACTIVITY_WORK_PKG_TEMPLATE_PATH=properties.getProperty("mail.activityWorkPkag.template");
			ACTIVITY_WORK_PKG_SUBJECT=properties.getProperty("mail.activityWorkPkag.subject");
			
		}
		catch(Exception e){
			
		}
		
		
	}
}
