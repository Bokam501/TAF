package com.hcl.atf.taf.service.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.imageio.ImageIO;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.HttpServletRequest;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.hcl.atf.dto.NotificationComponents;
import com.hcl.atf.dto.UserDetails;
import com.hcl.atf.email.EmailMessageBodyTemplate;
import com.hcl.atf.email.EmailSender;
import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.controller.utilities.CommonUtility;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.controller.utilities.PropertyLoader;
import com.hcl.atf.taf.dao.TestRunListDAO;
import com.hcl.atf.taf.dao.UserListDAO;
import com.hcl.atf.taf.dao.WorkPackageDAO;
import com.hcl.atf.taf.model.Activity;
import com.hcl.atf.taf.model.ActivityTask;
import com.hcl.atf.taf.model.ActivityWorkPackage;
import com.hcl.atf.taf.model.ProductType;
import com.hcl.atf.taf.model.TestFactory;
import com.hcl.atf.taf.model.TestFactoryResourceReservation;
import com.hcl.atf.taf.model.TestRunJob;
import com.hcl.atf.taf.model.TestRunList;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.WorkPackage;
import com.hcl.atf.taf.model.WorkpackageRunConfiguration;
import com.hcl.atf.taf.model.json.JsonWorkPackageTestCaseExecutionPlanForTester;
import com.hcl.atf.taf.report.Report;
import com.hcl.atf.taf.service.EmailService;
import com.hcl.atf.taf.service.EventsService;
import com.hcl.atf.taf.service.TestExecutionService;
import com.hcl.atf.taf.service.TestReportService;
import com.hcl.atf.taf.service.WorkPackageService;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.LibraryLoader;


@Service
public class EmailServiceImpl implements EmailService {	 
	private static final Log log = LogFactory.getLog(EmailServiceImpl.class);

	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private TestRunListDAO testRunListDAO;
	@Autowired
	private TestReportService testReportService;
	@Autowired
	private TestExecutionService testExecutionService;
	@Autowired
	private EventsService eventsService;
	@Autowired
	private WorkPackageDAO workPackageDAO;
	@Autowired
    private VelocityEngine velocityEngine;
	
	@Autowired
	private UserListDAO  userListDAO;
	
	@Autowired
	private Report report;
	
	@Autowired
	WorkPackageService workpackageService;
	
	@Autowired
	private HttpServletRequest request;
	
	@Value("#{ilcmProps['EMAIL_FROM_ADDRESS']}")
    private String EMAIL_FROM_ADDRESS;
	
	@Value("#{ilcmProps['EMAIL_CHANNEL']}")
    private String emailChannel;
	
	 public void setVelocityEngine(VelocityEngine velocityEngine) {
	        this.velocityEngine = velocityEngine;
	    }
	 
	@Override
	@Transactional
	public void sendTestRunCompletionMail(TestRunList testRunList) {
		
		//Check if there are any subscribers for the report
        String mailIdString = testRunList.getTestRunConfigurationChild().getNotifyByMail();
        if (mailIdString == null || mailIdString.trim().isEmpty()) {
			log.info("There are no subscribers for Email notification of report.");
        	return;
        }
		log.info("Email report to be sent to : " + mailIdString);

		String reportPath =  null;
		try {
			reportPath = testReportService.saveTestRunReportToFile_HTML(testRunList);
		} catch (Exception e) {
			log.error("Unable to generate a report to file", e);
		}
		
		if (reportPath == null) {
			log.info("Some problem in generating report file : " + reportPath);
			return;
		}
		log.info("Saved report to local file for dispatching by Email : " + reportPath);
		
		sendTestRunReportMail(testRunList, reportPath);
	}
	
	@Override
	@Transactional
	public void sendTestRunCompletionMail(Integer testRunListId) {
		
		TestRunList testRunList = testRunListDAO.getByTestRunListId(testRunListId);
		sendTestRunCompletionMail(testRunList);
	}
	
	@Override
	@Transactional
	public void sendTestRunReportMail(Integer testRunListId, String reportPath) {

		TestRunList testRunList = testRunListDAO.getByTestRunListId(testRunListId);
		sendTestRunReportMail(testRunList, reportPath);
	}

	public void sendTestRunReportMail(TestRunList testRunList, String reportPath) {

		try{
	        String mailIdString = testRunList.getTestRunConfigurationChild().getNotifyByMail();
	        if (mailIdString == null || mailIdString.trim().isEmpty()) {
				log.info("There are no subscribers for Email notification of report.");
	        	return;
	        }

	        MimeMessage message = mailSender.createMimeMessage();
			// use the true flag to indicate you need a multipart message
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			
			//TODO : Get the users to who a mail notification has to be sent. A separate subscribers table should be created
	        String[] mailIds = mailIdString.split(",");
	        
            InternetAddress[] toAddresses = new InternetAddress[mailIds.length];
            for (int i = 0; i < mailIds.length; i++) {        
            	if (mailIds[i].contains("@") && mailIds[i].contains(".com"))
            	toAddresses[i] = new InternetAddress(mailIds[i]);
	        }
            
	        helper.setTo(toAddresses); 
			helper.setSubject("TAF Report : " + testRunList.getTestResultStatusMaster().getTestResultStatus() + " for " + testRunList.getTestRunConfigurationChild().getProductVersionListMaster().getProductVersionName() + " - " + testRunList.getTestRunConfigurationChild().getTestRunConfigurationName());
			
			StringBuffer messageText = new StringBuffer();
			messageText.append("Test Run Report");messageText.append(System.lineSeparator());
			messageText.append("Product : " + testRunList.getTestRunConfigurationChild().getProductVersionListMaster().getProductMaster().getProductName());messageText.append(System.lineSeparator());
			messageText.append("Product Version : " + testRunList.getTestRunConfigurationChild().getProductVersionListMaster().getProductVersionName());messageText.append(System.lineSeparator());
			messageText.append("Test Configuration : " + testRunList.getTestRunConfigurationChild().getTestRunConfigurationName());messageText.append(System.lineSeparator());
			messageText.append("Platform : " + testRunList.getTestRunConfigurationChild().getTestEnviromentMaster().getDevicePlatformMaster().getDevicePlatformName());messageText.append(System.lineSeparator());messageText.append(System.lineSeparator());
			messageText.append("Status : " + testRunList.getTestResultStatusMaster().getTestResultStatus());messageText.append(System.lineSeparator());messageText.append(System.lineSeparator());
			messageText.append("Please refer the attached report for details");messageText.append(System.lineSeparator());messageText.append(System.lineSeparator());
			messageText.append("From");messageText.append(System.lineSeparator());
			messageText.append("TAF Administrator");messageText.append(System.lineSeparator());

			helper.setText(messageText.toString());

			if (reportPath == null || reportPath.trim().isEmpty()) {
				log.info("Could not access the report for sending by Email.");
			} else {
				FileSystemResource file = new FileSystemResource(new File(reportPath));
				helper.addAttachment(reportPath, file);
			}

			mailSender.send(message);
			log.info("Sent Report by mail to subscribers");
		} catch(Exception e) {
			// simply log it and go on...
			log.error("Unable to send message", e);            
		}
	}

	@Override
	@Transactional
	public void sendUserPasswordUpdateMail(HttpServletRequest request,UserList userList,String password) {
		try{
			PropertyLoader.loadProperties(request);
			
			NotificationComponents notificationComponents= new NotificationComponents();			
			String mailIdString = userList.getEmailId();
			if (mailIdString == null || mailIdString.trim().isEmpty()) {
				log.info("There are no subscribers for Email notification of report.");
	        	return;
	        }
			String[] mailIds = mailIdString.split(",");
            notificationComponents.setToEmail(mailIds);
			
            notificationComponents.setSubject(PropertyLoader.PASSWORD_UPDATE_SUBJECT);
            notificationComponents.setMail(true);
            
            UserDetails userDetails= new UserDetails();
            userDetails.setFirstName(userList.getFirstName());
            userDetails.setLoginId(userList.getLoginId());
            userDetails.setUserPassword(password);
            
            EmailSender emailService = new EmailSender();
	        
	        Map model = new HashMap();
            model.put("user", userDetails);
            
            String templatePath=PropertyLoader.PASSWORD_UPDATE_TEMPLATE_PATH;
	        emailService.sendEmail(notificationComponents,mailSender,velocityEngine,templatePath,model);
	        
			log.info("Sent Report by mail to subscribers");
		} catch(Exception e) {
			log.error("Unable to send message", e);
			e.printStackTrace();
		}
	}

	@Override
	@Transactional
	public void sendUserCreation(HttpServletRequest request, UserList userList,
			String password) {
		try{
			PropertyLoader.loadProperties(request);
			UserList user = (UserList)request.getSession().getAttribute("USER");
			String mailIdString = userList.getEmailId();
			if (mailIdString == null || mailIdString.trim().isEmpty()) {
				log.info("There are no subscribers for Email notification of report.");
	        	return;
	        }
			String[] mailIds = mailIdString.split(",");
			System.setProperty("java.net.preferIPv4Stack" , "true");
            UserDetails userDetails= new UserDetails();
            userDetails.setFirstName(userList.getFirstName());
            userDetails.setLoginId(userList.getLoginId());
            userDetails.setUserPassword(password);
            
            SimpleMailMessage message = new SimpleMailMessage();
    		
    		message.setFrom(user.getEmailId());
    		message.setTo(mailIds);
    		message.setSubject(PropertyLoader.USER_CREATION_SUBJECT);
    		message.setText(EmailMessageBodyTemplate.userCreation(userDetails));
    		mailSender.send(message);	
	        
			log.info("Sent Report by mail to subscribers");
		} catch(Exception e) {
			e.printStackTrace();
			log.error("Unable to send message", e);            
		}
	}

	@Override
	public void sendResourceBookingNotificationMail() {
		
	}

	@Override
	public void sendResourceReservationOrCancellationNotificationMail(HttpServletRequest request, TestFactoryResourceReservation resourceReservation, int typeOfActivity) {
		try{
			PropertyLoader.loadProperties(request);
			
			
			NotificationComponents notificationComponents= new NotificationComponents();			
			UserList user = (UserList)request.getSession().getAttribute("USER");
			
			if(resourceReservation != null){
				notificationComponents.setFromEmail(user.getEmailId());
				UserList reservedUser = resourceReservation.getBlockedUser();
				String mailIdString = reservedUser.getEmailId();
				if (mailIdString == null || mailIdString.trim().isEmpty()) {
					log.info("There are no subscribers for Email notification of report.");
		        	return;
		        }
				String[] mailIds = mailIdString.split(",");
	            notificationComponents.setToEmail(mailIds);
	            notificationComponents.setMail(true);
	            if(typeOfActivity ==0){
	            	notificationComponents.setSubject(PropertyLoader.USER_UNRESERVAION_NOTIFICATION_SUBJECT);
	            }else if(typeOfActivity ==1){
	            	notificationComponents.setSubject(PropertyLoader.USER_RESERVAION_NOTIFICATION_SUBJECT);
	            }
	           
	            UserDetails userDetails= new UserDetails();
	            userDetails.setFirstName(reservedUser.getFirstName());
	            userDetails.setLoginId(reservedUser.getLoginId());
	            String resrvedUserName = "";
	            String resrvedByUserName = "";
	            String workPackageName = "";
	            String testFactoryName = "";
	            String shiftName = "";
	            String reservationDate = "";
	            WorkPackage workPacakge = workPackageDAO.getWorkPackageById(resourceReservation.getWorkPackage().getWorkPackageId());
	            workPackageName = workPacakge.getName();
	            TestFactory testFactory = workPacakge.getProductBuild().getProductVersion().getProductMaster().getTestFactory();
	            testFactoryName = testFactory.getTestFactoryName();
	            shiftName = resourceReservation.getShift().getShiftName();
	            resrvedUserName = resourceReservation.getBlockedUser().getLoginId();
	            resrvedByUserName= 	resourceReservation.getReservationActionUser().getLoginId();
	            reservationDate = resourceReservation.getReservationDate().toString();
	            HashMap<String,String> resourceReservationMap = new HashMap<String, String>();
	            resourceReservationMap.put(IDPAConstants.EMAIL_RESRVATION_RESERVED_USER, resrvedUserName);
	            resourceReservationMap.put(IDPAConstants.EMAIL_RESRVATION_RESERVEDBY_USER, resrvedByUserName);
	            resourceReservationMap.put(IDPAConstants.EMAIL_RESRVATION_RESERVED_TESTFACROTY, testFactoryName);
	            resourceReservationMap.put(IDPAConstants.EMAIL_RESRVATION_RESERVED_WORKPACKAGE, workPackageName);
	            resourceReservationMap.put(IDPAConstants.EMAIL_RESRVATION_RESERVED_DATE, reservationDate);
	            resourceReservationMap.put(IDPAConstants.EMAIL_RESRVATION_RESERVED_SHIFT, shiftName);
	            if(typeOfActivity == 1){
	            	/// Reservation
	            	notificationComponents.setMessageBody(resourceReservationNotification(userDetails,resourceReservationMap));
	            }else if(typeOfActivity == 0){
	            	//Unreserve User 
	            	notificationComponents.setMessageBody(resourceReservationCancellationNotification(userDetails,resourceReservationMap));
	            }
      	        EmailSender emailService = new EmailSender();
      	        emailService.sendEmail(notificationComponents,mailSender);
            }
			log.info("Sent Report by mail to subscribers");
		} catch(Exception e) {
			// simply log it and go on...
			log.error("Unable to send message", e);            
		}
	}
	
	
	public static String resourceReservationNotification(UserDetails userList, HashMap<String, String> resourceReservationMap) {
		String resrvedUserName = "";
        String resrvedByUserName = "";
        String workPackageName = "";
        String testFactoryName = "";
        String shiftName = "";
        String reservationDate = "";
        
        for (String key : resourceReservationMap.keySet()) {
            log.info("Key = " + key);
            if(key.equalsIgnoreCase(IDPAConstants.EMAIL_RESRVATION_RESERVED_USER)){
            	resrvedUserName = resourceReservationMap.get(key);
            }
            if(key.equalsIgnoreCase(IDPAConstants.EMAIL_RESRVATION_RESERVEDBY_USER)){
            	resrvedByUserName = resourceReservationMap.get(key);
            }
            if(key.equalsIgnoreCase(IDPAConstants.EMAIL_RESRVATION_RESERVED_WORKPACKAGE)){
            	workPackageName = resourceReservationMap.get(key);
            }
            if(key.equalsIgnoreCase(IDPAConstants.EMAIL_RESRVATION_RESERVED_TESTFACROTY)){
            	testFactoryName = resourceReservationMap.get(key);
            }
            if(key.equalsIgnoreCase(IDPAConstants.EMAIL_RESRVATION_RESERVED_SHIFT)){
            	shiftName = resourceReservationMap.get(key);
            }
            if(key.equalsIgnoreCase(IDPAConstants.EMAIL_RESRVATION_RESERVED_DATE)){
            	reservationDate = resourceReservationMap.get(key);
            }
        }
		StringBuffer messageText = new StringBuffer();
		messageText.append("Hi " + userList.getFirstName() + ",");
		messageText.append(System.lineSeparator());
		messageText.append("Your reservation has been confirmed.");
		messageText.append(System.lineSeparator());
		messageText.append("Date : " + reservationDate);
		messageText.append(System.lineSeparator());
		messageText.append("Shift : " + shiftName);
		messageText.append(System.lineSeparator());
		messageText.append("Test Factory : " + testFactoryName);
		messageText.append(System.lineSeparator());
		messageText.append("Work Package : " + workPackageName);
		messageText.append(System.lineSeparator());
		messageText.append("Reserved By : " + resrvedByUserName);
		messageText.append(System.lineSeparator());
		messageText.append(System.lineSeparator());
		messageText.append("Regards,");
		messageText.append(System.lineSeparator());
		messageText.append("Admin");
		messageText.append(System.lineSeparator());
		messageText.append("This is an auto generated mail. Please dont reply to this mail.");
		messageText.append(System.lineSeparator());
		return messageText.toString();
	}

	public static String resourceReservationCancellationNotification(UserDetails userList,HashMap<String, String> resourceReservationMap) {
		String resrvedUserName = "";
        String resrvedByUserName = "";
        String workPackageName = "";
        String testFactoryName = "";
        String shiftName = "";
        String reservationDate = "";
        
        for (String key : resourceReservationMap.keySet()) {
            log.info("Key = " + key);
            if(key.equalsIgnoreCase(IDPAConstants.EMAIL_RESRVATION_RESERVED_USER)){
            	resrvedUserName = resourceReservationMap.get(key);
            }
            if(key.equalsIgnoreCase(IDPAConstants.EMAIL_RESRVATION_RESERVEDBY_USER)){
            	resrvedByUserName = resourceReservationMap.get(key);
            }
            if(key.equalsIgnoreCase(IDPAConstants.EMAIL_RESRVATION_RESERVED_WORKPACKAGE)){
            	workPackageName = resourceReservationMap.get(key);
            }
            if(key.equalsIgnoreCase(IDPAConstants.EMAIL_RESRVATION_RESERVED_TESTFACROTY)){
            	testFactoryName = resourceReservationMap.get(key);
            }
            if(key.equalsIgnoreCase(IDPAConstants.EMAIL_RESRVATION_RESERVED_SHIFT)){
            	shiftName = resourceReservationMap.get(key);
            }
            if(key.equalsIgnoreCase(IDPAConstants.EMAIL_RESRVATION_RESERVED_DATE)){
            	reservationDate = resourceReservationMap.get(key);
            }
        }
		StringBuffer messageText = new StringBuffer();
		messageText.append("Hi " + userList.getFirstName() + ",");
		messageText.append(System.lineSeparator());
		messageText.append("Your reservation for the following date and shift has been cancelled.");
		messageText.append(System.lineSeparator());
		messageText.append("Date : " + reservationDate);
		messageText.append(System.lineSeparator());
		messageText.append("Shift : " + shiftName);
		messageText.append(System.lineSeparator());
		messageText.append("Test Factory : " + testFactoryName);
		messageText.append(System.lineSeparator());
		messageText.append("Work Package : " + workPackageName);
		messageText.append(System.lineSeparator());
		messageText.append("Reservation Cancelled By : " + resrvedByUserName);
		messageText.append(System.lineSeparator());
		messageText.append(System.lineSeparator());
		messageText.append("Regards,");
		messageText.append(System.lineSeparator());
		messageText.append("Admin");
		messageText.append(System.lineSeparator());
		messageText.append("This is an auto generated mail. Please dont reply to this mail.");
		messageText.append(System.lineSeparator());
		return messageText.toString();
	}

	@Override
	@Transactional
	public void sendTestRunPlanCompletionMail(TestRunJob testRunJob) {		
		String mailSubject="";
		String primaryEmailIds="";
		String jobReportLocation = "";
		try {		
			log.info("Sending Test Run Job Status e-mail");
			
			String mailIdString = testRunJob.getTestRunPlan().getNotifyByMail();
	        if (mailIdString == null || mailIdString.trim().isEmpty()) {
				log.info("There are no subscribers for E-Mail Notification related to Test Run Job");
	        	return;
	        }		
			StringBuffer messageText = new StringBuffer();
			
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			if(mailIdString.contains(",")){
				String[] mailIds = mailIdString.split(",");	
				InternetAddress[] toAddresses = new InternetAddress[mailIds.length];
	            for (int i = 0; i < mailIds.length; i++) {	   
	            	if(mailIds[i].contains("@") && mailIds[i].contains(".com")){
	            		toAddresses[i] = new InternetAddress(mailIds[i]);		
	            		primaryEmailIds=mailIds[i];
	            		log.info("E-Mail Job Status to be sent to : " + mailIds[i]);
	            	}
		        }
	            helper.setTo(toAddresses); 
			} else {
				String mailIds = mailIdString;
				InternetAddress toAddresses = null;				
            	if(mailIds.contains("@") && mailIds.contains(".com")){
            		toAddresses = new InternetAddress(mailIds);		
            		log.info("E-Mail Job Status to be sent to : " + mailIds);
            	}			     
				helper.setTo(toAddresses); 
			}         	            
			if(testRunJob.getTestRunStatus().equals(IDPAConstants.JOB_COMPLETED)){
				helper.setSubject("TAF Job Execution Status for ID : "+ testRunJob.getTestRunJobId()+" is COMPLETED to the Test Run Plan : " +testRunJob.getTestRunPlan().getTestRunPlanId() + " - " + testRunJob.getTestRunPlan().getTestRunPlanName()+" - Reg.");
			}
			
				messageText.append("Hi,");
				messageText.append(System.lineSeparator());
				messageText.append(System.lineSeparator());
				messageText.append("Test Run Job Report");
				messageText.append(System.lineSeparator());
				messageText.append("----------------------------------------------------------------------------------------------------------------------------");
				messageText.append(System.lineSeparator());
				if(testRunJob.getWorkPackage()!=null){
					messageText.append("Source : "+testRunJob.getWorkPackage().getSourceType());
					messageText.append(System.lineSeparator());
				}
				messageText.append("Product : " + testRunJob.getRunConfiguration().getProduct().getProjectName());
				messageText.append(System.lineSeparator());
				messageText.append("Product Version : " + testRunJob.getRunConfiguration().getProductVersion().getProductVersionName());
				messageText.append(System.lineSeparator());
				messageText.append("Product Type : " + testRunJob.getRunConfiguration().getProduct().getProductType().getTypeName());
				messageText.append(System.lineSeparator());
				if(testRunJob.getRunConfiguration().getTestRunPlan() != null){
					messageText.append("Test Run Plan Name : " + testRunJob.getRunConfiguration().getTestRunPlan().getTestRunPlanName());
					messageText.append(System.lineSeparator());
					messageText.append("Test Run Plan ID : " + testRunJob.getRunConfiguration().getTestRunPlan().getTestRunPlanId());
					messageText.append(System.lineSeparator());
				}
				messageText.append("Test Configuration : " + testRunJob.getRunConfiguration().getEnvironmentcombination().getEnvironmentCombinationName());
				messageText.append(System.lineSeparator());	
				if(testRunJob.getTestRunStatus().equals(IDPAConstants.JOB_COMPLETED)){
					messageText.append("Status : COMPLETED");
				}
				messageText.append(System.lineSeparator());
				messageText.append("Remarks : "+testRunJob.getTestRunFailureMessage());
				messageText.append(System.lineSeparator());
				if(testRunJob.getHostList()!=null){
					messageText.append("Host IP : "+testRunJob.getHostList().getHostIpAddress());
					messageText.append(System.lineSeparator());
					messageText.append("Host Name : "+testRunJob.getHostList().getHostName());
					messageText.append(System.lineSeparator());
				}				
				messageText.append("Please refer the attached report for details");			
				messageText.append(System.lineSeparator());
				messageText.append(System.lineSeparator());
				messageText.append("Regards,");
				messageText.append(System.lineSeparator());
				messageText.append("TAF Administrator");
				messageText.append(System.lineSeparator());
				
				//Attachment for test Run Job
				if(testRunJob.getTestRunStatus().equals(IDPAConstants.JOB_COMPLETED)){
					jobReportLocation = testRunJobReportLocation(testRunJob);
					log.info("Test Run Job Report Export Location : "+jobReportLocation);
				}
				
				if (jobReportLocation == null || jobReportLocation.trim().isEmpty()) {
					log.info("Could not send test run job report by E-Mail.");
				} else {
					FileSystemResource file = new FileSystemResource(new File(jobReportLocation));
					helper.addAttachment("Test Run Job : "+testRunJob.getTestRunJobId()+".html", file);
				}
				
			helper.setText(messageText.toString());
			if(emailChannel.equalsIgnoreCase("SMTP")) {
				mailSender.send(message);
			} else if (emailChannel.equalsIgnoreCase("Outlook")){
			
				HashMap<String, Object> mailParams = new HashMap<String, Object>();
				
				/*if(primaryEmailIds != null && primaryEmailIds.length >0 ){
					String primaryMail = "";
					for( String pyMailIds : primaryEmailIds){
						if(primaryMail == null || primaryMail.trim().length() == 0){
							primaryMail = pyMailIds;
						}else{
							primaryMail += ";"+pyMailIds;
						}
					}
				}*/
				mailParams.put("to", primaryEmailIds);
				
				/*if(syMailIds != null){
					mailParams.put("cc", syMailIds.replaceAll(",", ";"));
				}*/
				mailParams.put("subject",mailSubject );
				//messageText = messageText.replaceAll("(\\r|\\n)", "<br>");
				mailParams.put("body", messageText.toString());
				if(jobReportLocation != null){
					String attachment[]={jobReportLocation}; 
					mailParams.put("attachments", attachment);
				}
				//sendOutLookEmail(mailParams);
			} else {
				log.info("No Mail channel enabled");
			}
			log.info("Test Run Job Status mail has been successfully sent to subscribers");
		} catch (Exception e) {
			log.error("Unable to send Test Run Job Status mail to subscribers"+ e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public void sendActivityTaskStatusChangeMail(HttpServletRequest request,String assignee,String reviewer,ActivityTask jsonActivityTask,String activityName,String productName,String productVersionName,String productBuildName,String awpName,Integer productId){
		
		try{
			PropertyLoader.loadProperties(request);
			
			UserList user = (UserList)request.getSession().getAttribute("USER");
			List<String> productManagerEmailList = userListDAO.getProductManagerEmailList(productId);
			String productMangerMailIds="";
			String[] toMailIds = null;
			String [] ccMailId=null;
			String maildIds="";
			if(productManagerEmailList!=null && productManagerEmailList.size() >0){
				
				for(String emailId:productManagerEmailList){
					productMangerMailIds += emailId+",";
				}
				productMangerMailIds=productMangerMailIds.substring(0,productMangerMailIds.length()-1);
			}
			if (assignee == null || reviewer.trim().isEmpty() && assignee == null || reviewer.trim().isEmpty()) {
				log.info("There are no subscribers for Email notification of report.");
	        	return;
	        }else {
	        	if (!validateEmail(reviewer) ){
	        		log.info("Invalid format of Reviewer MailId:"+reviewer);
					return;
	        	}else if (!validateEmail(assignee)){
	        		log.info("Invalid format of Assignee MailId:"+assignee);
					return;
	        	}else {
	        		
	        		
	        		
	        		 if(user.getEmailId().equals(assignee)){
	 	            	maildIds=reviewer+","+user.getEmailId();
	 	            	toMailIds=maildIds.split(",");
	 	            	ccMailId = assignee.split(",");
	 	            	productMangerMailIds=productMangerMailIds+","+assignee;
	         			ccMailId = productMangerMailIds != ""?productMangerMailIds.split(","):ccMailId;
	         			
	         		}else if(user.getEmailId().equals(reviewer)){
	         			maildIds=assignee+","+user.getEmailId();
	         			toMailIds=maildIds.split(",");
	         			productMangerMailIds=productMangerMailIds+","+reviewer;
	         			ccMailId = productMangerMailIds != ""?productMangerMailIds.split(","):ccMailId;
	         			
	         		}else {
	         			maildIds=reviewer+","+assignee;
	         			toMailIds=maildIds.split(",");
	         			productMangerMailIds=productMangerMailIds+","+user.getEmailId();
	         			ccMailId = productMangerMailIds != ""?productMangerMailIds.split(","):ccMailId;
	         		}
	        		 
	    			
	                sendActivtiyStatusChangeEmail(user.getEmailId(),assignee,reviewer,jsonActivityTask,activityName,productName,productVersionName,productBuildName,awpName,toMailIds,ccMailId,productMangerMailIds);
	    	        log.info("Sent Report by mail to Activity task status changed");
				}
	        }
		}  catch(Exception e) {
			// simply log it and go on...
			log.error("Unable to send message", e);            
		} 
		
	}

	
	public static boolean validateEmail(String email){

		   Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
		   Matcher m = p.matcher(email);
		   
		   if(!email.contains(".com")){
			   return false;
		   }

		   boolean matchFound = m.matches();

		   StringTokenizer st = new StringTokenizer(email, ".");
		   String lastToken = null;
		   while (st.hasMoreTokens()) {
		      lastToken = st.nextToken();
		   }

		   if (matchFound && lastToken.length() >= 2
		      && email.length() - 1 != lastToken.length())  {
		      return true;
		   }
		   else return false;
		}

	
	private void sendActivtiyStatusChangeEmail(final String fromMailId,final String assignee,final String reviewer,final ActivityTask jsonActivityTask,final String activityName,final String productName,final String productVersionName,final String productBuildName,final String awpName,final String[] toMailIds,final String[] ccMailId,final String productMangerMailIds) {
		  final String templatePath=PropertyLoader.ACTIVITY_TASK_STATUS_CHANGE_TEMPLATE_PATH;
		  
		  final SimpleDateFormat dateFormate=new SimpleDateFormat("dd/MM/yyyy");
		  
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
	    	  
	         public void prepare(MimeMessage mimeMessage) throws Exception {
	            MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
	            
	            message.setTo(toMailIds);
	            message.setFrom(fromMailId); // could be parameterized...
	            message.setCc(ccMailId);
	            message.setSubject(PropertyLoader.ACTIVITY_TASK_STATUS_CHANGE_SUBJECT+" "+jsonActivityTask.getStatus().getWorkflowStatusDisplayName().toUpperCase());
	           
	            final Map model = new HashMap();
	            
	            model.put("fromMailId", fromMailId);
	            model.put("toMailIds", toMailIds.toString());
	            model.put("status", jsonActivityTask.getStatus().getWorkflowStatusDisplayName());
	            model.put("assignee", assignee);
	            model.put("reviewer", reviewer);
	            model.put("taskName", jsonActivityTask.getActivityTaskName());
	            model.put("taskTypeName", jsonActivityTask.getActivityTaskType().getActivityTaskTypeName() == null ?"":jsonActivityTask.getActivityTaskType().getActivityTaskTypeName());
	            model.put("category", jsonActivityTask.getCategory().getName() == null ?"":jsonActivityTask.getCategory().getName());
	            model.put("priority", jsonActivityTask.getPriority().getExecutionPriorityName() == null ?"":jsonActivityTask.getPriority().getExecutionPriorityName());
	            model.put("result", jsonActivityTask.getResult().getActivityResultName());
	            model.put("actualStartDate", jsonActivityTask.getActualStartDate() == null ? "":dateFormate.format(jsonActivityTask.getActualStartDate()));
	            model.put("actualEndDate", jsonActivityTask.getActualEndDate() == null ?"":dateFormate.format(jsonActivityTask.getActualEndDate()));
	            model.put("summary", "Activity task status changed to "+jsonActivityTask.getStatus().getWorkflowStatusDisplayName());
	            
	            model.put("activityName", activityName);
	            model.put("product", productName);
	            model.put("buildName", productBuildName);
	            model.put("version", productVersionName);
	            model.put("activityWorkpkgName", awpName);
	            model.put("plannedStartDate", jsonActivityTask.getPlannedStartDate() == null ?"":dateFormate.format(jsonActivityTask.getPlannedStartDate()));
	            model.put("plannedEndDate", jsonActivityTask.getPlannedEndDate() == null ?"":dateFormate.format(jsonActivityTask.getPlannedEndDate()));
	            model.put("baselineStartDate", jsonActivityTask.getBaselineStartDate() == null ?"":dateFormate.format(jsonActivityTask.getBaselineStartDate()));
	            model.put("baselineEndDate", jsonActivityTask.getBaselineEndDate() == null ?"":dateFormate.format(jsonActivityTask.getBaselineEndDate()));
	            model.put("productMangerMailIds", productMangerMailIds);
	            
	            
	            @SuppressWarnings("deprecation")
				String text = VelocityEngineUtils.mergeTemplateIntoString(
	               velocityEngine, templatePath, model);
	            message.setText(text, true);
	         }
	      };
	      mailSender.send(preparator);
	   }
	
	
	
	@Override
	@Transactional
	public void sendActivityManagementMail(HttpServletRequest request,String assignee,String reviewer,Activity activity,String productName,String productVersionName,String productBuildName,String awpName,Integer productId){
		
		try{
			PropertyLoader.loadProperties(request);
			
			UserList user = (UserList)request.getSession().getAttribute("USER");
			List<String> productManagerEmailList = userListDAO.getProductManagerEmailList(productId);
			String productMangerMailIds="";
			String[] toMailIds = null;
			String [] ccMailId=null;
			String maildIds="";
			if(productManagerEmailList!=null && productManagerEmailList.size() >0){
				
				for(String emailId:productManagerEmailList){
					productMangerMailIds += emailId+",";
				}
				productMangerMailIds=productMangerMailIds.substring(0,productMangerMailIds.length()-1);
			}
			if ((assignee == null || assignee.trim().isEmpty() ) ||  (reviewer == null || reviewer.trim().isEmpty())) {
				log.info("There are no subscribers for Email notification of report.");
	        	return;
	        }else {
	        	if (!validateEmail(reviewer) ){
	        		log.info("Invalid format of Reviewer MailId:"+reviewer);
					return;
	        	}else if (!validateEmail(assignee)){
	        		log.info("Invalid format of Assignee MailId:"+assignee);
					return;
	        	}else {
	        		
	        		
	        		
	        		 if(user.getEmailId().equals(assignee)){
	 	            	maildIds=reviewer+","+user.getEmailId();
	 	            	toMailIds=maildIds.split(",");
	 	            	ccMailId = assignee.split(",");
	 	            	productMangerMailIds=productMangerMailIds+","+assignee;
	         			ccMailId = productMangerMailIds != ""?productMangerMailIds.split(","):ccMailId;
	         			
	         		}else if(user.getEmailId().equals(reviewer)){
	         			maildIds=assignee+","+user.getEmailId();
	         			toMailIds=maildIds.split(",");
	         			productMangerMailIds=productMangerMailIds+","+reviewer;
	         			ccMailId = productMangerMailIds != ""?productMangerMailIds.split(","):ccMailId;
	         			
	         		}else {
	         			maildIds=reviewer+","+assignee;
	         			toMailIds=maildIds.split(",");
	         			productMangerMailIds=productMangerMailIds+","+user.getEmailId();
	         			ccMailId = productMangerMailIds != ""?productMangerMailIds.split(","):ccMailId;
	         		}
	        		 
	    			
	        		 sendActivityManagementMailCall(user.getEmailId(),assignee,reviewer,activity,productName,productVersionName,productBuildName,awpName,toMailIds,ccMailId,productMangerMailIds);
	    	        log.info("Sent Report by mail to Activity task status changed");
				}
	        }
		} catch(Exception e) {
			// simply log it and go on...
			log.error("Unable to send message", e);            
		}
		
	}
	
	
	private void sendActivityManagementMailCall(final String fromMailId,final String assignee,final String reviewer,final Activity activity,final String productName,final String productVersionName,final String productBuildName,final String awpName,final String[] toMailIds,final String[] ccMailId, final String productMangerMailIds){
		
		 final String templatePath=PropertyLoader.ACTIVITY_MANAGEMENT_TEMPLATE_PATH;
		 
	      final Map activityModel = new HashMap();
		  final SimpleDateFormat dateFormate=new SimpleDateFormat("dd/MM/yyyy");
		  	MimeMessagePreparator preparator = new MimeMessagePreparator() {
	         public void prepare(MimeMessage mimeMessage) throws Exception {
	            MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
	            
	            message.setTo(toMailIds);
	            message.setFrom(fromMailId); // could be parameterized...
	            message.setCc(ccMailId);
	            message.setSubject(PropertyLoader.ACTIVITY_MANAGEMENT_SUBJECT+" "+activity.getStatusCategory().getStatusCategoryName().toUpperCase());
	           
	            final Map model = new HashMap();
	        
	            model.put("activityName", activity.getActivityName());
	            model.put("product", productName);
	            model.put("buildName", productBuildName);
	            model.put("version", productVersionName);
	            model.put("activityWorkpkgName", awpName);
	            model.put("plannedStartDate", activity.getPlannedStartDate() == null ?"":dateFormate.format(activity.getPlannedStartDate()));
	            model.put("plannedEndDate", activity.getPlannedEndDate() == null ?"":dateFormate.format(activity.getPlannedEndDate()));
	            model.put("productMangerMailIds", productMangerMailIds);
	            
	            
	            model.put("SRSSection", activity.getProductFeature().getProductFeatureName());
	            model.put("ActivityTrackerNumber", activity.getActivityTrackerNumber());
	            model.put("category", activity.getCategory().getName());
	            model.put("assignee", assignee);
	            model.put("reviewer", reviewer);
	            model.put("priority", activity.getPriority().getExecutionPriorityName());
	            model.put("status category", activity.getStatusCategory().getStatusCategoryName());
	            model.put("isActive", activity.getIsActive());
	            model.put("actualStartDate", activity.getActualStartDate() == null ? "":dateFormate.format(activity.getActualStartDate()));
	            model.put("actualEndDate", activity.getActualEndDate() == null ?"":dateFormate.format(activity.getActualEndDate()));
	            model.put("fromMailId", fromMailId);
	            model.put("summary", "Activity status category changed to "+activity.getStatusCategory().getStatusCategoryName());
	            
	            @SuppressWarnings("deprecation")
				String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, templatePath, activityModel);
	            message.setText(text, true);
	         }
	      };
	      mailSender.send(preparator);
	}

	@Override
	@Transactional
	public void sendActivityWorkPkgMail(HttpServletRequest request,ActivityWorkPackage activityWorkPkg,String productName,String productVersionName,String productBuildName,Integer productId) {
		
		try{
			PropertyLoader.loadProperties(request);
			
			UserList user = (UserList)request.getSession().getAttribute("USER");
			List<String> productManagerEmailList = userListDAO.getProductManagerEmailList(productId);
			String productMangerMailIds="";
			String[] toMailIds = null;
			String [] ccMailId=null;
			String maildIds="";
			if(productManagerEmailList!=null && productManagerEmailList.size() >0){
				
				for(String emailId:productManagerEmailList){
					productMangerMailIds += emailId+",";
				}
				productMangerMailIds=productMangerMailIds.substring(0,productMangerMailIds.length()-1);
			}
			String owner=activityWorkPkg.getOwner().getEmailId();
			if (owner == null || owner.isEmpty()) {
				log.info("There are no subscribers for Email notification of report.");
	        	return;
	        }else {
	        	if (!validateEmail(owner) ){
	        		log.info("Invalid format of Owner MailId:"+owner);
					return;
	        	}else {
	        		
	        		
	        		
	        		 if(user.getEmailId().equals(owner)){
	 	            	maildIds=user.getEmailId();
	 	            	toMailIds=maildIds.split(",");
	 	            	ccMailId = owner.split(",");
	 	            	productMangerMailIds=productMangerMailIds+","+user.getEmailId();
	         			ccMailId = productMangerMailIds != ""?productMangerMailIds.split(","):ccMailId;
	         			
	         		}else {
	         			toMailIds=owner.split(",");
	         			productMangerMailIds=productMangerMailIds+","+user.getEmailId();
	         			ccMailId = productMangerMailIds != ""?productMangerMailIds.split(","):ccMailId;
	         		}
	        		 
	    			
	        		 sendActivityWorkPkgMail(user.getEmailId(),owner,activityWorkPkg,productName,productVersionName,productBuildName,toMailIds,ccMailId,productMangerMailIds);
	    	        log.info("Sent Report by mail to Activity Workpackage Changed status");
				}
	        }
	}catch(Exception e) {
		// simply log it and go on...
		log.error("Unable to send message", e);            
	}
	}
	
	
	
	private void sendActivityWorkPkgMail(final String fromMailId,final String owner,final ActivityWorkPackage workPkage,final String productName,final String productVersionName,final String productBuildName,final String[] toMailIds,final String[] ccMailId, final String productMangerMailIds) throws MessagingException{
		
		 final String templatePath=PropertyLoader.ACTIVITY_WORK_PKG_TEMPLATE_PATH;
		  
		  final SimpleDateFormat dateFormate=new SimpleDateFormat("dd/MM/yyyy");
		  MimeMessagePreparator preparator = new MimeMessagePreparator() {
	         public void prepare(MimeMessage mimeMessage) throws Exception {
	            MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
	            
	            message.setTo(toMailIds);
	            message.setFrom(fromMailId); // could be parameterized...
	            message.setCc(ccMailId);
	            message.setSubject("[INFO]:"+workPkage.getActivityWorkPackageName()+"Status Change to"+workPkage.getStatusCategory().getStatusCategoryName().toUpperCase());
	           
	            final Map model = new HashMap();
	            model.put("product", productName);
	            model.put("buildName", productBuildName);
	            model.put("version", productVersionName);
	            model.put("activityWorkpkgName", workPkage.getActivityWorkPackageName());
	            model.put("plannedStartDate", workPkage.getPlannedStartDate() == null ?"":dateFormate.format(workPkage.getPlannedStartDate()));
	            model.put("plannedEndDate", workPkage.getPlannedEndDate() == null ?"":dateFormate.format(workPkage.getPlannedEndDate()));
	            model.put("productMangerMailIds", productMangerMailIds);
	            
	            model.put("owner", owner);
	            model.put("priority", workPkage.getPriority().getExecutionPriorityName());
	            model.put("status category", workPkage.getStatusCategory().getStatusCategoryName());
	            model.put("isActive", workPkage.getIsActive());
	            model.put("actualStartDate", workPkage.getActualStartDate() == null ? "":dateFormate.format(workPkage.getActualStartDate()));
	            model.put("actualEndDate", workPkage.getActualEndDate() == null ?"":dateFormate.format(workPkage.getActualEndDate()));
	            model.put("fromMailId", fromMailId);
	            model.put("summary", "Activity Workpackage status category changed to "+workPkage.getStatusCategory().getStatusCategoryName());
	            @SuppressWarnings("deprecation")
				String text = VelocityEngineUtils.mergeTemplateIntoString(
	               velocityEngine, templatePath, model);
	            message.setText(text, true);
	         }
	      };
	      mailSender.send(preparator);
	     
	}

	@Override
	public void sendEmail(String subject, String contentMessage, String emailIds, String attachmentPaths) {

		try{
	        if (emailIds == null || emailIds.trim().isEmpty()) {
				log.info("There are no subscribers for Email notification of report.");
	        	return;
	        }

	        MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			
	        String[] mailIds = emailIds.split(",");
	        
	        InternetAddress fromAddress = new InternetAddress("iLCM@hcl.com");

	        InternetAddress[] toAddresses = new InternetAddress[mailIds.length];
            for (int i = 0; i < mailIds.length; i++) {
            	if (mailIds[i].contains("@") && mailIds[i].contains(".com"))
            	toAddresses[i] = new InternetAddress(mailIds[i]);
	        }
            
            helper.setFrom(fromAddress);
	        helper.setTo(toAddresses); 
			helper.setSubject(subject);
			
			StringBuffer messageText = new StringBuffer();
			messageText.append(contentMessage);

			helper.setText(messageText.toString());

			if (attachmentPaths == null || attachmentPaths.trim().isEmpty()) {
				log.info("Could not access the report for sending by Email.");
			} else {
				FileSystemResource file = new FileSystemResource(new File(attachmentPaths));
				helper.addAttachment(attachmentPaths, file);
			}
			mailSender.send(message);
			log.info("Sent mail to subscribers - "+emailIds);
		} catch(Exception e) {
			log.error("Unable to send message", e);            
		}
	}
	
	private String testRunJobReportLocation(TestRunJob testRunJob){
		String exportLocation =null;
		try{
			int testRunNo = 0;
			int testRunConfigurationChildId = 0;
			ProductType productType =null;
			
			String serverFolderPath = null;
			if (System.getProperty("os.name").contains("Linux")) {
				serverFolderPath = CommonUtility.getCatalinaPath()+ IDPAConstants.JASPERREPORTS_PATH_LINUX;
			} else {
				serverFolderPath = CommonUtility.getCatalinaPath()+ IDPAConstants.JASPERREPORTS_PATH;
			}
			
	        String testSuiteName = "";
	        String workpackageName="";
	        String imageFileName = "";
	        BufferedImage logo = null;
	        String loginUserName = "";        
	        String imageServerPath = null;
	        String productName = "";
	        String testRunPlanName = "";
	        String customerName = "";	        
	        String productTypeName = "";	        
	        String viewType = "PDF";
	        String deviceId = "";
	        
	        if(System.getProperty("os.name").contains("Linux")){
	        	imageServerPath= CommonUtility.getCatalinaPath().concat("/webapps/Logo/");
			} else {
				imageServerPath= CommonUtility.getCatalinaPath().concat("\\webapps\\Logo\\");
			}
			if(testRunJob.getTestRunJobId()!=-1){ 
				  testRunNo=testRunJob.getWorkPackage().getWorkPackageId();
				  if(testRunJob.getWorkPackage()!=null){
					  loginUserName = testRunJob.getWorkPackage().getUserList().getLoginId();
				  }
				 if(testRunJob.getWorkPackage().getTestRunPlan()!=null){
						testRunConfigurationChildId=testRunJob.getWorkPackage().getTestRunPlan().getTestRunPlanId();
						if(testRunJob.getTestSuite() != null){
							testSuiteName = testRunJob.getTestSuite().getTestSuiteName();	
						}					
						productType =testRunJob.getWorkPackage().getTestRunPlan().getProductVersionListMaster().getProductMaster().getProductType();
						customerName = testRunJob.getWorkPackage().getTestRunPlan().getProductVersionListMaster().getProductMaster().getCustomer().getCustomerName().trim();
						productName = testRunJob.getWorkPackage().getTestRunPlan().getProductVersionListMaster().getProductMaster().getProductName();
						testRunPlanName = testRunJob.getWorkPackage().getTestRunPlan().getTestRunPlanName();						
						imageFileName = testRunJob.getWorkPackage().getTestRunPlan().getProductVersionListMaster().getProductMaster().getCustomer().getImageURI();
						productTypeName = "Job";
					}
				 if(testRunJob.getGenericDevices()!=null){
					 deviceId= testRunJob.getGenericDevices().getUDID();
				}		
				 
				 if(imageFileName != null){
						imageServerPath = imageServerPath.concat(imageFileName);
					} else {		
				        if(System.getProperty("os.name").contains("Linux")){
				        	imageServerPath= CommonUtility.getCatalinaPath().concat("/webapps/iLCM/css/images/noimage.jpg");
						} else {
							imageServerPath= CommonUtility.getCatalinaPath().concat("\\webapps\\iLCM\\css\\images\\noimage.jpg");
						}
					}
					File imageFile = new File(imageServerPath);
					if(imageFile.exists() && imageFile != null){
						logo = ImageIO.read(imageFile);	
					}		
					
					if(testRunNo != -1 && viewType.equalsIgnoreCase("PDF")){
						serverFolderPath=serverFolderPath+File.separator+"WorkPackages"+File.separator+testRunNo;
						exportLocation = serverFolderPath+File.separator+productName+"-"+testRunPlanName+"-"+testRunNo+".html";
						log.info("Report Export location : "+exportLocation);
					}
					
					if(viewType.equalsIgnoreCase("PDF")){
						serverFolderPath=serverFolderPath+File.separator+"Jobs"+File.separator+testRunJob.getTestRunJobId();
						exportLocation = serverFolderPath+File.separator+productName+"-"+testRunPlanName+"-"+testRunJob.getTestRunJobId()+".html";
						log.info("Job Report Export : "+exportLocation);
					}
					
					JasperPrint jasperPrint=null;

					String strContentType=null;
					String strPrintMode=null;
					
					if(viewType.equalsIgnoreCase("PDF")){
						strPrintMode="PDF";
						strContentType="application/pdf";
					}
					
					if(viewType.equalsIgnoreCase("HTML")){
						strPrintMode="HTML";
						strContentType="text/html";
					}
					
					
					File file = new File(serverFolderPath);
					if (!file.isDirectory()) {
						file.mkdirs();
					}
					if (productTypeName.equalsIgnoreCase("JOB")){
						//jasperPrint=report.generateTestRunDeviceListReport(testRunNo,testRunJob.getTestRunJobId(),deviceId,testRunConfigurationChildId,strPrintMode,logo, loginUserName,"No");;
						String htmlFileLink=testReportService.evidenceReportHtml(testRunNo, testRunConfigurationChildId, deviceId, -1, "", viewType);
						log.info("Job level HTML Report file location:"+htmlFileLink);
					}
					/*if(viewType.equalsIgnoreCase("Pdf"))
					{
						FileOutputStream  fos=new FileOutputStream(exportLocation);
						JRPdfExporter jRPdfExporter =new JRPdfExporter();
						jRPdfExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
						jRPdfExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, fos );
				 		jRPdfExporter.exportReport();
					}*/
			}
		} catch(Exception e){
			log.error("Unknown error on generating test run job report : "+e.getMessage());
		}
		return exportLocation;
	}

	@Override
	@Transactional
	public void sendWorkPackageReportMail(WorkPackage workpackage) {
		String mailSubject="";
		String primaryEmailIds="";
		String jobReportLocation = "";
		try{
			JsonWorkPackageTestCaseExecutionPlanForTester tester = workpackageService.listWorkPackageTestCaseExecutionSummaryReport(workpackage.getWorkPackageId());
			log.info("Sending Workpackage Status e-mail");
			String mailIdString = workpackage.getTestRunPlan().getNotifyByMail();
	        if (mailIdString == null || mailIdString.trim().isEmpty()) {
				log.info("There are no subscribers for E-Mail Notification related to Workpackage");
	        	return;
	        }		
			StringBuffer messageText = new StringBuffer();
			
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			//primaryEmailIds=mailIdString;
			if(mailIdString.contains(",")){
				String[] mailIds = mailIdString.split(",");	
				InternetAddress[] toAddresses = new InternetAddress[mailIds.length];
	            for (int i = 0; i < mailIds.length; i++) {	   
	            	if(mailIds[i].contains("@") && mailIds[i].contains(".com")){
	            		toAddresses[i] = new InternetAddress(mailIds[i]);		
	            		log.info("E-Mail Workpackage Status to be sent to : " + mailIds[i]);
	            		if(primaryEmailIds == "" || primaryEmailIds.isEmpty()) {
	            			primaryEmailIds =mailIds[i];
	            		} else {
	            			primaryEmailIds += ","+mailIds[i];
	            		}
	            	}
		        }
	            helper.setTo(toAddresses); 
			} else {
				String mailIds = mailIdString;
				primaryEmailIds=mailIdString;
				InternetAddress toAddresses = null;				
	        	if(mailIds.contains("@") && mailIds.contains(".com")){
	        		toAddresses = new InternetAddress(mailIds);		
	        		log.info("E-Mail Workpackage Status to be sent to : " + mailIds);
	        	}			     
				helper.setTo(toAddresses); 
			} 
			
			if(workpackage.getWorkFlowEvent().getWorkFlow().getEntityMaster().getEntitymasterid() == IDPAConstants.WORKPACKAGE_ENTITY_ID && (workpackage.getWorkFlowEvent().getWorkFlow().getStageId() == IDPAConstants.WORKFLOW_STAGE_ID_COMPLETED)){
				//mailSubject="TAF Workpackage Status for ID : "+ workpackage.getWorkPackageId()+" is COMPLETED - Reg.";
				mailSubject="Workpackage "+workpackage.getWorkPackageId()+" : Test Report [Result : <result>]";
				helper.setSubject(mailSubject);								
			} 			
				messageText.append("Hi,");
				messageText.append("<br/>");
				messageText.append("<br/>");
				messageText.append("Workpackage Report");
				messageText.append("<br/>");
				messageText.append("----------------------------------------------------------------------------------------------------------------------------");
				messageText.append("<br/>");
				messageText.append("Source : "+workpackage.getSourceType());
				messageText.append("<br/>");
				messageText.append("Host IP : ");	
				Set<String> hostIPs = new HashSet<String>();	
				String hostIP = "";
				if(null != workpackage.getWorkPackageRunConfigSet()){
					Set<WorkpackageRunConfiguration> runconfigs = workpackage.getWorkPackageRunConfigSet();
					if(runconfigs != null){
						for(WorkpackageRunConfiguration runconfig : runconfigs){
							if(runconfig.getRunconfiguration() != null){
								hostIPs.add(runconfig.getRunconfiguration().getHostList().getHostIpAddress());								
							}
						}
					}					
				}		
				
				if(hostIPs != null && hostIPs.size() >0) {
					for(String hIP : hostIPs){
						hostIP = hostIP.concat(hIP).concat(",");	
					}				
					hostIP = hostIP.substring(0, hostIP.lastIndexOf(","));
				}
				messageText.append(hostIP);
				messageText.append("<br/>");
				messageText.append("Host Name : ");
				Set<String> hostNames = new HashSet<String>();	
				String hostName = "";
				if(null != workpackage.getWorkPackageRunConfigSet()){
					Set<WorkpackageRunConfiguration> runconfigs = workpackage.getWorkPackageRunConfigSet();
					if(runconfigs != null){
						for(WorkpackageRunConfiguration runconfig : runconfigs){
							if(runconfig.getRunconfiguration() != null){
								hostNames.add(runconfig.getRunconfiguration().getHostList().getHostName());
							}
						}
					}					
				}
				if(hostNames != null && hostNames.size() >0) {
					for(String hName : hostNames){
						hostName = hostName.concat(hName).concat(",");	
					}
					hostName = hostName.substring(0, hostName.lastIndexOf(","));
				}
				messageText.append(hostName);		
				
				messageText.append("<br/>");
				messageText.append("Product : " + workpackage.getProductBuild().getProductVersion().getProductMaster().getProductName());
				messageText.append("<br/>");
				messageText.append("Product Version : " + workpackage.getProductBuild().getProductVersion().getProductVersionName());
				messageText.append("<br/>");
				messageText.append("Test Plan Name : " + workpackage.getTestRunPlan().getTestRunPlanName());
				messageText.append("<br/>");
				messageText.append("Test Plan ID : " + workpackage.getTestRunPlan().getTestRunPlanId());
				messageText.append("<br/>");
				boolean flag = false;
				int abortedJobsCount = 0, completedjobsCount =0 ;
				if(null != workpackage.getTestRunJobSet()){
					Set<TestRunJob> trjs = workpackage.getTestRunJobSet();
					for(TestRunJob trj : trjs){
						if(trj.getTestRunStatus().equals(IDPAConstants.JOB_COMPLETED)){
							messageText.append("Test Job : " +trj.getTestRunJobId() +" - Host Name : "+trj.getHostList().getHostName()+" - Status : COMPLETED - Remarks : "+trj.getTestRunFailureMessage());
							completedjobsCount ++;
						} else if(trj.getTestRunStatus().equals(IDPAConstants.JOB_ABORTED)){
							messageText.append("Test Job : " +trj.getTestRunJobId() +" - Host Name : "+trj.getHostList().getHostName()+" - Status : ABORTED - Remarks : "+trj.getTestRunFailureMessage());
							abortedJobsCount++;
							flag = true;
						}
						if(!(abortedJobsCount > 0 || completedjobsCount > 0)){
							messageText.append("Workpackage Execution Remarks : Workpackage could not be executed completely either Job(s) cannot be created or Host(s) / Device(s) went offline / disconnected interruptedly.");
						}
						messageText.append("<br/>");						
					}
				} else {
					messageText.append("Workpackage Execution Remarks : Workpackage could not be executed completely either Job(s) cannot be created or Host(s) / Device(s) went offline / disconnected interruptedly.");
				}
				
				messageText.append("<br/>");
				if(workpackage.getWorkFlowEvent().getWorkFlow().getEntityMaster().getEntitymasterid() == IDPAConstants.WORKPACKAGE_ENTITY_ID && (workpackage.getWorkFlowEvent().getWorkFlow().getStageId() == IDPAConstants.WORKFLOW_STAGE_ID_COMPLETED)){
					messageText.append("Overall WorkPackage Status : COMPLETED");
				}
				messageText.append("<br/>");
				if(completedjobsCount > 1){
					messageText.append("Please refer the attached report for details");
					messageText.append("\n");
				}
				messageText.append("<br/>");
				messageText.append("Regards,");
				messageText.append("<br/>");
				messageText.append("TAF Administrator");
				messageText.append("<br/>");
				
				//Attachment for Workpackage
			
				if(workpackage.getWorkFlowEvent().getWorkFlow().getEntityMaster().getEntitymasterid() == IDPAConstants.WORKPACKAGE_ENTITY_ID && (workpackage.getWorkFlowEvent().getWorkFlow().getStageId()== IDPAConstants.WORKFLOW_STAGE_ID_COMPLETED) && flag == false && (workpackage.getTestRunJobSet().size() > abortedJobsCount)){
					jobReportLocation = workPackageCompletionHTMLReportMail(workpackage);
					log.info("Workpackage Report Export Location : "+jobReportLocation);
				}
				
				if (jobReportLocation == null || jobReportLocation.trim().isEmpty()) {
					log.info("Could not send workpackage report by E-Mail.");
				} else {
					FileSystemResource file = new FileSystemResource(new File(jobReportLocation));
					//helper.addAttachment("Workpackage : "+workpackage.getWorkPackageId()+".pdf", file);
					helper.addAttachment("Workpackage : "+workpackage.getWorkPackageId()+".zip", file);
				}
				Map model = new HashMap<>();
				model.put("mime", messageText);
				
				
			helper.setText(messageText.toString());
			//SMTP Mail Channel
			
			if(emailChannel.equalsIgnoreCase("SMTP")) {
				mailSender.send(message);
			} else if (emailChannel.equalsIgnoreCase("Outlook")){
			
				HashMap<String, Object> mailParams = new HashMap<String, Object>();
				
				/*if(primaryEmailIds != null && primaryEmailIds.length >0 ){
					String primaryMail = "";
					for( String pyMailIds : primaryEmailIds){
						if(primaryMail == null || primaryMail.trim().length() == 0){
							primaryMail = pyMailIds;
						}else{
							primaryMail += ";"+pyMailIds;
						}
					}
				}*/
				if(primaryEmailIds !="") {
					primaryEmailIds = primaryEmailIds.replaceAll(",", ";");
				}
				mailParams.put("to", primaryEmailIds);
				
				/*if(syMailIds != null){
					mailParams.put("cc", syMailIds.replaceAll(",", ";"));
				}*/
				mailParams.put("subject",mailSubject );
				//messageText = messageText.replaceAll("(\\r|\\n)", "<br>");
				mailParams.put("body", messageText.toString());
				if(jobReportLocation != null){
					String attachment[]={jobReportLocation}; 
					mailParams.put("attachments", attachment);
				}
				String result=sendOutLookEmail(mailParams);
				if (result == null || result.startsWith("FAIL")) {
					log.info(result);
				}
			} else {
				log.info("No Mail channel enabled");
			}
			log.info("Workpackage Status mail has been successfully sent to subscribers");
		} catch (Exception e) {
			log.error("Unable to send Workpackage Status mail to subscribers : "+ e.getMessage());
		}		
	}
	
	private String workPackageCompletionMail(WorkPackage workpackage){
		String exportLocation =null;
		try{			
			int testRunNo = 0;
			int testRunConfigurationChildId = 0;
			ProductType productType =null;			
			String serverFolderPath = null;
			
			if (System.getProperty("os.name").contains("Linux")) {
				serverFolderPath = CommonUtility.getCatalinaPath()+ IDPAConstants.JASPERREPORTS_PATH_LINUX;
			} else {
				serverFolderPath = CommonUtility.getCatalinaPath()+ IDPAConstants.JASPERREPORTS_PATH;
			}
			
	        String testSuiteName = "";
	        String workpackageName="";
	        String imageFileName = "";
	        BufferedImage logo = null;
	        String loginUserName = "";        
	        String imageServerPath = null;
	        String productName = "";
	        String testRunPlanName = "";
	        String customerName = "";	        
	        String productTypeName = "";	        
	        String viewType = "PDF";
	        String deviceId = "";
	        
	        if(System.getProperty("os.name").contains("Linux")){
	        	imageServerPath= CommonUtility.getCatalinaPath().concat("/webapps/Logo/");
			} else {
				imageServerPath= CommonUtility.getCatalinaPath().concat("\\webapps\\Logo\\");
			}
	        
	        testRunNo = workpackage.getWorkPackageId();
			WorkPackage wp=workPackageDAO.getWorkPackageById(workpackage.getWorkPackageId());
			if(wp!=null){				
				loginUserName = wp.getUserList().getLoginId();
			}
			workpackageName = wp.getName();
			productType=wp.getProductBuild().getProductVersion().getProductMaster().getProductType();
			productName = wp.getProductBuild().getProductVersion().getProductMaster().getProductName();
			customerName = wp.getProductBuild().getProductVersion().getProductMaster().getCustomer().getCustomerName().trim();
			testRunPlanName = wp.getTestRunPlan().getTestRunPlanName();
			if(wp.getTestRunPlan()!=null){
				testRunConfigurationChildId=wp.getTestRunPlan().getTestRunPlanId();
				imageFileName = wp.getTestRunPlan().getProductVersionListMaster().getProductMaster().getCustomer().getImageURI();
				productTypeName = "Workpackage";
			}
			
			if(imageFileName != null){
				imageServerPath = imageServerPath.concat(imageFileName);
			} else {		
		        if(System.getProperty("os.name").contains("Linux")){
		        	imageServerPath= CommonUtility.getCatalinaPath().concat("/webapps/iLCM/css/images/noimage.jpg");
				} else {
					imageServerPath= CommonUtility.getCatalinaPath().concat("\\webapps\\iLCM\\css\\images\\noimage.jpg");
				}
			}
			File imageFile = new File(imageServerPath);
			if(imageFile.exists() && imageFile != null){
				logo = ImageIO.read(imageFile);	
			}
			
			serverFolderPath=serverFolderPath+File.separator+"WorkPackages"+File.separator+testRunNo;
			exportLocation = serverFolderPath+File.separator+productName+"-"+testRunPlanName+"-"+testRunNo+".pdf";
			log.info("Workpackage Report Export Location : "+exportLocation);
						
			JasperPrint jasperPrint=null;
			String strContentType=null;
			String strPrintMode=null;
			
			strPrintMode="PDF";
			strContentType="application/pdf";
			
			jasperPrint=report.generateTestRunListReport(testRunNo, testRunConfigurationChildId,strPrintMode,productType,logo, loginUserName);
			
			File file = new File(serverFolderPath);
			if (!file.isDirectory()) {
				file.mkdirs();
			}
			
			FileOutputStream  fos=new FileOutputStream(exportLocation);
			JRPdfExporter jRPdfExporter =new JRPdfExporter();
			jRPdfExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			jRPdfExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, fos );
		 	jRPdfExporter.exportReport();		
			
		}catch(Exception e){
			log.error("Unknown error on generating Workpackage report : "+e.getMessage());
		}
		return exportLocation;
	}
	
	private String workPackageCompletionHTMLReportMail(WorkPackage workpackage){
		String exportLocation ="";
		try{			
			int testRunNo = 0;
			int testRunConfigurationChildId = 0;
			ProductType productType =null;			
			String serverFolderPath = null;
			
			if (System.getProperty("os.name").contains("Linux")) {
				serverFolderPath = CommonUtility.getCatalinaPath()+ IDPAConstants.WORKPACKAGE_REPORTS_PATH_LINUX;
			} else {
				serverFolderPath = CommonUtility.getCatalinaPath()+ IDPAConstants.WORKPACKAGE_REPORTS_PATH;
			}
			
	        String testSuiteName = "";
	        String workpackageName="";
	        String imageFileName = "";
	        BufferedImage logo = null;
	        String loginUserName = "";        
	        String imageServerPath = null;
	        String productName = "";
	        String testRunPlanName = "";
	        String customerName = "";	        
	        String productTypeName = "";	        
	        String viewType = "HTML";
	        String deviceId = "";
	        
	        if(System.getProperty("os.name").contains("Linux")){
	        	imageServerPath= CommonUtility.getCatalinaPath().concat("/webapps/Logo/");
			} else {
				imageServerPath= CommonUtility.getCatalinaPath().concat("\\webapps\\Logo\\");
			}
	        
	        testRunNo = workpackage.getWorkPackageId();
			WorkPackage wp=workPackageDAO.getWorkPackageById(workpackage.getWorkPackageId());
			if(wp!=null){				
				loginUserName = wp.getUserList().getLoginId();
			}
			workpackageName = wp.getName();
			productType=wp.getProductBuild().getProductVersion().getProductMaster().getProductType();
			productName = wp.getProductBuild().getProductVersion().getProductMaster().getProductName();
			customerName = wp.getProductBuild().getProductVersion().getProductMaster().getCustomer().getCustomerName().trim();
			testRunPlanName = wp.getTestRunPlan().getTestRunPlanName();
			if(wp.getTestRunPlan()!=null){
				testRunConfigurationChildId=wp.getTestRunPlan().getTestRunPlanId();
				imageFileName = wp.getTestRunPlan().getProductVersionListMaster().getProductMaster().getCustomer().getImageURI();
				productTypeName = "Workpackage";
			}
			
			if(imageFileName != null){
				imageServerPath = imageServerPath.concat(imageFileName);
			} else {		
		        if(System.getProperty("os.name").contains("Linux")){
		        	imageServerPath= CommonUtility.getCatalinaPath().concat("/webapps/iLCM/css/images/noimage.jpg");
				} else {
					imageServerPath= CommonUtility.getCatalinaPath().concat("\\webapps\\iLCM\\css\\images\\noimage.jpg");
				}
			}
			File imageFile = new File(imageServerPath);
			if(imageFile.exists() && imageFile != null){
				logo = ImageIO.read(imageFile);	
			}
			
		//	serverFolderPath=serverFolderPath+File.separator+"WorkPackages"+File.separator+testRunNo;
			//exportLocation = serverFolderPath+File.separator+productName+"-"+testRunPlanName+"-"+testRunNo+".html";
			 String workpackageReportLocation=testReportService.evidenceReportHtml(testRunNo, testRunConfigurationChildId, deviceId, -1, "", viewType);
			exportLocation=CommonUtility.getCatalinaPath()+File.separator+"webapps"+File.separator+workpackageReportLocation;
			//log.info("Workpackage Report html file link : "+htmlFileLink);
		//	exportLocation = serverFolderPath+File.separator+productName+"-WP-"+testRunNo+"-Evidence"+File.separator +productName+"-WP-"+testRunNo+"-Evidence.html";
			
			log.info("Workpackage Report Export Location : "+exportLocation);
						
			/*JasperPrint jasperPrint=null;
			String strContentType=null;
			String strPrintMode=null;
			
			strPrintMode="HTML";
			strContentType="text/html";*/
			
			/*jasperPrint=report.generateTestRunListReport(testRunNo, testRunConfigurationChildId,strPrintMode,productType,logo, loginUserName);
			
			File file = new File(serverFolderPath);
			if (!file.isDirectory()) {
				file.mkdirs();
			}
			
			FileOutputStream  fos=new FileOutputStream(exportLocation);
			JRPdfExporter jRPdfExporter =new JRPdfExporter();
			jRPdfExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			jRPdfExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, fos );
		 	jRPdfExporter.exportReport();	*/	
			
		}catch(Exception e){
			log.error("Unknown error on generating Workpackage report : "+e.getMessage());
		}
		return exportLocation;
	}	
	
	public void sendEmailWhenWorkflowStatusChange(String[] emails, String[] ccMailIds, String firstNames, String entityTypeName, String instanceNames, String actionBy, String comments, Date eventTime, String movedFrom, String movedTo, String pendingWith, Integer slaDuration,Integer entityInstanceId){
		try{
			NotificationComponents notificationComponents = new NotificationComponents();
			notificationComponents.setMail(true);
			notificationComponents.setFromEmail(EMAIL_FROM_ADDRESS);
			notificationComponents.setToEmail(emails);			
	        notificationComponents.setSubject("WORKFLOW STATUS CHANGE UPDATE - Reg.");
	        notificationComponents.setCcEmail(ccMailIds);
	        EmailSender emailService = new EmailSender();						        
		    Map<String, Object> workflowStatusModel = new HashMap<String, Object>();
		    workflowStatusModel.put("firstName", firstNames);
		    workflowStatusModel.put("entityType", entityTypeName);
		    workflowStatusModel.put("entityInstanceId",entityInstanceId);
		    workflowStatusModel.put("instanceName", instanceNames);
		    workflowStatusModel.put("actionBy", actionBy);		    
		    workflowStatusModel.put("eventTime", eventTime);
		    workflowStatusModel.put("comments", comments);
		    workflowStatusModel.put("pendingWith", pendingWith);
		    workflowStatusModel.put("movedFrom", movedFrom);
		    workflowStatusModel.put("movedTo", movedTo);
		    workflowStatusModel.put("slaDuration", slaDuration);
		    
            String templatePath="com/mailTemplate/WorkflowStatusChangeTemplate.vm";
	        emailService.sendEmail(notificationComponents,mailSender,velocityEngine,templatePath,workflowStatusModel);
        
	        log.info("Sent Report by mail to subscribers");
		}catch(Exception e){
			log.error("Unknown error while sending mail to subscribers on WorkflowStatus change : ",e);
		}
	}
	
	@Override
	@Transactional
	public void sendEntityBasedEmailNotification(List<String> emails, List<String> ccMails,String fromMailId,Map<String,Object> templateModel,Integer entityTypeId,String subject){
		String templatePath="";
		try{
			NotificationComponents notificationComponents = new NotificationComponents();
			if(entityTypeId != null && entityTypeId.equals(IDPAConstants.ENTITY_TASK_TYPE)) {
			 templatePath="com/mailTemplate/ActivityTaskStatusChange.vm";
			 notificationComponents.setSubject(subject);
			} else if(entityTypeId != null && entityTypeId.equals(IDPAConstants.ENTITY_ACTIVITY_TYPE)) {
				 templatePath="com/mailTemplate/ActivityManagementTemplate.vm";
				 notificationComponents.setSubject(subject);
			} else if(entityTypeId != null && entityTypeId.equals(IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE_ID)) {
				 templatePath="com/mailTemplate/ActivityWorkPkgTemplate.vm";
				 notificationComponents.setSubject(subject);
			} else if(entityTypeId != null && entityTypeId.equals(IDPAConstants.CHANGE_REQUEST_ENTITY_MASTER_ID)) {
				 templatePath="com/mailTemplate/ChangeRequestTemplate.vm";
				 notificationComponents.setSubject(subject);
			} else if(entityTypeId != null && entityTypeId.equals(IDPAConstants.CLARIFICATION_ENTITY_MASTER_ID)) {
				 templatePath="com/mailTemplate/ClarificationTemplate.vm";
				 notificationComponents.setSubject(subject);
			}
			
			
			
			notificationComponents.setMail(true);
			notificationComponents.setFromEmail(fromMailId);
			if(emails !=null && emails.size() >0){
				String [] toMails = emails.toArray(new String[emails.size()]);
				notificationComponents.setToEmail(toMails);
			}
	        if(ccMails !=null && ccMails.size() >0){
				String [] ccMailIds = ccMails.toArray(new String[ccMails.size()]);
				notificationComponents.setCcEmail(ccMailIds);
			}
	        EmailSender emailService = new EmailSender();						        
	        emailService.sendEmail(notificationComponents,mailSender,velocityEngine,templatePath,templateModel);
		}catch(Exception e){
			log.error("Unknown error while sending mail to subscribers on  ",e);
		}
	}
	
	
	@Override
	@Transactional
	public void sendBotEmailNotifications(String[] primaryEmailIds, String[] secondaryEmailIds,String subject, String bodyMessage, String[] attachmentFileLocations){ 
		try{
			System.setProperty("java.net.preferIPv4Stack" , "true");
    		
            if(primaryEmailIds == null || primaryEmailIds.length == 0){
            	primaryEmailIds = IDPAConstants.defaultMailId;
            }
            
            MimeMessage message = mailSender.createMimeMessage();
            message.setFrom(new InternetAddress(EMAIL_FROM_ADDRESS));
            InternetAddress[] primaryInternetAddress = new InternetAddress[primaryEmailIds.length];
            for(int i = 0; i < primaryEmailIds.length; i++){
            	primaryInternetAddress[i] = new InternetAddress(primaryEmailIds[i]);
            }
            message.setRecipients(Message.RecipientType.TO, primaryInternetAddress);
            message.setSubject(subject);
            
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(bodyMessage, "text/html; charset=utf-8");
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            if(attachmentFileLocations != null && attachmentFileLocations.length > 0){
            	for(String attachmentFileLocation : attachmentFileLocations){
                	if(attachmentFileLocation != null && !attachmentFileLocation.trim().isEmpty()){
                		messageBodyPart = new MimeBodyPart();
                        DataSource source = new FileDataSource(attachmentFileLocation);
                        messageBodyPart.setDataHandler(new DataHandler(source));
                        messageBodyPart.setFileName(source.getName());
                        multipart.addBodyPart(messageBodyPart);
                	}
                }
            }
            
            message.setContent(multipart);
            mailSender.send(message);
            
    	
			log.info("Sent Report by mail to subscribers");
		} catch(Exception e) {
			e.printStackTrace();
			log.error("Unable to send bot notification message", e);            
		}
	}
	
	
	@Override
	@Transactional
	public void sendPokeEmailNotifications(String[] primaryEmailIds, String[] secondaryEmailIds,String subject, String bodyMessage){ 
		try{
			System.setProperty("java.net.preferIPv4Stack" , "true");
            if(primaryEmailIds == null || primaryEmailIds.length == 0){
            	primaryEmailIds = IDPAConstants.defaultMailId;
            }
            
            if(secondaryEmailIds == null || secondaryEmailIds.length == 0){
            	secondaryEmailIds = IDPAConstants.defaultMailId;
            }
            
            MimeMessage message = mailSender.createMimeMessage();
            message.setFrom(new InternetAddress(EMAIL_FROM_ADDRESS));
            InternetAddress[] primaryInternetAddress = new InternetAddress[primaryEmailIds.length];
            InternetAddress[] secondaryInternetAddress = new InternetAddress[secondaryEmailIds.length];
            if(primaryEmailIds !=null && primaryEmailIds.length>0){
	            for(int i = 0; i < primaryEmailIds.length; i++){
	            	primaryInternetAddress[i] = new InternetAddress(primaryEmailIds[i]);
	            }
	            message.setRecipients(Message.RecipientType.TO, primaryInternetAddress);
            }
            if(secondaryEmailIds !=null && secondaryEmailIds.length>0){
            	for(int i = 0; i < secondaryEmailIds.length; i++){
            		secondaryInternetAddress[i] = new InternetAddress(secondaryEmailIds[i]);
                }
            	message.setRecipients(Message.RecipientType.CC, secondaryInternetAddress);
			}
            
            message.setSubject(subject);
            
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(bodyMessage);
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            
            message.setContent(multipart);
            mailSender.send(message);
	        
			log.info("Sent poke notification Report by mail to subscribers");
		} catch(Exception e) {
			e.printStackTrace();
			log.error("Unable to send poke notification message", e);            
		}
	}
	
	
	@Override
	@Transactional
	public void sendReportIssue(String fromMail,String[] primaryEmailIds, String[] secondaryEmailIds,String subject, String bodyMessage){ 
		try{
			System.setProperty("java.net.preferIPv4Stack" , "true");
            if(primaryEmailIds == null || primaryEmailIds.length == 0){
            	primaryEmailIds = IDPAConstants.defaultMailId;
            }
            
            if(secondaryEmailIds == null || secondaryEmailIds.length == 0){
            	secondaryEmailIds = IDPAConstants.defaultMailId;
            }
            
            MimeMessage message = mailSender.createMimeMessage();
            message.setFrom(new InternetAddress(fromMail));
            InternetAddress[] primaryInternetAddress = new InternetAddress[primaryEmailIds.length];
            InternetAddress[] secondaryInternetAddress = new InternetAddress[secondaryEmailIds.length];
            if(primaryEmailIds !=null && primaryEmailIds.length>0){
	            for(int i = 0; i < primaryEmailIds.length; i++){
	            	primaryInternetAddress[i] = new InternetAddress(primaryEmailIds[i]);
	            }
	            message.setRecipients(Message.RecipientType.TO, primaryInternetAddress);
            }
            if(secondaryEmailIds !=null && secondaryEmailIds.length>0){
            	for(int i = 0; i < secondaryEmailIds.length; i++){
            		secondaryInternetAddress[i] = new InternetAddress(secondaryEmailIds[i]);
                }
            	message.setRecipients(Message.RecipientType.CC, secondaryInternetAddress);
			}
            
            message.setSubject(subject);
            
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(bodyMessage, "text/html; charset=utf-8");
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            
            message.setContent(multipart);
            mailSender.send(message);
	        
			log.info("Sent poke notification Report by mail to subscribers");
		} catch(Exception e) {
			e.printStackTrace();
			log.error("Unable to send poke notification message", e);            
		}
	}
	
	@Override
	public String sendOutLookEmail(Map<String, Object> params) {
		try {
               /* String jacobDllVersionToUse;
                String jvmBitVersion=System.getProperty("sun.arch.data.model");
                if (jvmBitVersion.contains("32")) {
                      jacobDllVersionToUse = "jacob-1.18-x86.dll";
                } else {
                      jacobDllVersionToUse = "jacob-1.18-x64.dll";
                }
                
                String jcobDLLFile=CommonUtility.getCatalinaPath().concat("\\webapps"+ File.separator + request.getContextPath()+File.separator +"\\WEB-INF\\lib"+File.separator+ jacobDllVersionToUse);
                log.info("JcobDLL File location:"+jcobDLLFile);
                File file = new File(jcobDLLFile);
                if (!file.exists()) {
                      System.out.println("File not Exists");
                }
                System.setProperty(LibraryLoader.JACOB_DLL_PATH, jcobDLLFile);
                
*/
			
			ActiveXComponent ol = new ActiveXComponent("Outlook.Application");  
	    	Dispatch mail =Dispatch.invoke(ol.getObject(), "CreateItem", Dispatch.Get,new Object[] { "0" }, new int[0]).toDispatch();
	    	
	    	Dispatch.put(mail, "To", params.get("to"));
	    	if(params.containsKey("cc")){
	    		Dispatch.put(mail, "Cc", params.get("cc"));
	    	}
	    	Dispatch.put(mail, "Subject", params.get("subject"));
	        Dispatch.put(mail, "HTMLBody", params.get("body"));
	    	Dispatch.put(mail, "ReadReceiptRequested", "false");
	    	if(params.containsKey("attachments")){
	    		String attachments[] = (String[]) params.get("attachments");
	    		Dispatch attachs = Dispatch.get(mail, "Attachments").toDispatch();
	            for(Object attachment : attachments){
	            	if(attachment != null && !attachment.toString().trim().isEmpty()){
	            		Dispatch.call(attachs, "Add", attachment);
	            	}
	            }
	    	}
	    	
	    	Dispatch.call(mail, "Send");
	    	log.info("Html Mail sent successfully...");
		}catch(Exception re) {
			log.error("Error in sendOutLookEmail",re);
			return "FAIL : Problem while sending mail through Outlook client : " + re.getLocalizedMessage();
		}
		return "SUCCESS : Sent mail through Outlook client : ";
    }

	
	@Override
	@Transactional
	public String sendWorkpackageCompletedReport(WorkPackage workPackage,String toRecipients, String ccRecipients) {
		String mailSubject="";
		String primaryEmailIds="";
		String secondaryEmailIds="";
		String jobReportLocation = "";
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
		try{
			JsonWorkPackageTestCaseExecutionPlanForTester tester = workpackageService.listWorkPackageTestCaseExecutionSummaryReport(workPackage.getWorkPackageId());
			log.info("Sending Workpackage Status e-mail");
			String mailIdString=toRecipients;
	        if (mailIdString == null || mailIdString.trim().isEmpty()) {
				log.info("There are no subscribers for E-Mail Notification related to Workpackage");
	        	return "There are no subscribers for E-Mail Notification related to Workpackage";
	        }		
			StringBuffer messageText = new StringBuffer();
			
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			//primaryEmailIds=mailIdString;
			if(mailIdString.contains(",")){
				String[] mailIds = mailIdString.split(",");	
				InternetAddress[] toAddresses = new InternetAddress[mailIds.length];
	            for (int i = 0; i < mailIds.length; i++) {	   
	            	if(mailIds[i].contains("@") && mailIds[i].contains(".com")){
	            		toAddresses[i] = new InternetAddress(mailIds[i]);		
	            		log.info("E-Mail Workpackage Status to be sent to : " + mailIds[i]);
	            		if(primaryEmailIds == "" || primaryEmailIds.isEmpty()) {
	            			primaryEmailIds =mailIds[i];
	            		} else {
	            			primaryEmailIds += ","+mailIds[i];
	            		}
	            	}
		        }
	            helper.setTo(toAddresses); 
			} else {
				String mailIds = mailIdString;
				primaryEmailIds=mailIdString;
				InternetAddress toAddresses = null;				
	        	if(mailIds.contains("@") && mailIds.contains(".com")){
	        		toAddresses = new InternetAddress(mailIds);		
	        		log.info("E-Mail Workpackage Status to be sent to : " + mailIds);
	        	}			     
				helper.setTo(toAddresses); 
			} 
			
			if(ccRecipients !="" && !ccRecipients.trim().isEmpty()) {
				if(ccRecipients.contains(",")){
					String[] mailIds = ccRecipients.split(",");	
					InternetAddress[] ccAddresses = new InternetAddress[mailIds.length];
		            for (int i = 0; i < mailIds.length; i++) {	   
		            	if(mailIds[i].contains("@") && mailIds[i].contains(".com")){
		            		ccAddresses[i] = new InternetAddress(mailIds[i]);		
		            		log.info("E-Mail Workpackage Status to be sent to : " + mailIds[i]);
		            		if(secondaryEmailIds == "" || secondaryEmailIds.isEmpty()) {
		            			secondaryEmailIds =mailIds[i];
		            		} else {
		            			secondaryEmailIds += ","+mailIds[i];
		            		}
		            	}
			        }
		            helper.setCc(ccAddresses); 
				} else {
					String mailIds = ccRecipients;
					secondaryEmailIds=ccRecipients;
					InternetAddress ccAddresses = null;				
		        	if(mailIds.contains("@") && mailIds.contains(".com")){
		        		ccAddresses = new InternetAddress(mailIds);		
		        		log.info("E-Mail Workpackage Status to be sent to : " + mailIds);
		        	}			     
					helper.setCc(ccAddresses); 
				} 
			}
			boolean flag = false;
			int abortedJobsCount = 0, completedjobsCount =0 ;
			String jobDetails="";
			String hostname="";
			if(null != workPackage.getTestRunJobSet()){
				Set<TestRunJob> trjs = workPackage.getTestRunJobSet();
				for(TestRunJob trj : trjs){
					if(trj.getTestRunStatus().equals(IDPAConstants.JOB_COMPLETED)){
						
						if(jobDetails == "") {
							jobDetails=trj.getTestRunJobId().toString();
						} else {
							jobDetails+=","+trj.getTestRunJobId().toString();
						}
						
						if(hostname == "") {
							hostname=trj.getHostList().getHostName();
						} else if(!hostname.trim().equalsIgnoreCase(trj.getHostList().getHostName().trim())){
							hostname+=","+trj.getHostList().getHostName();
						}
						completedjobsCount ++;
					} else if(trj.getTestRunStatus().equals(IDPAConstants.JOB_ABORTED)){
						if(jobDetails == "") {
							jobDetails=trj.getTestRunJobId().toString();
						} else {
							jobDetails+=","+trj.getTestRunJobId().toString();
						}
						abortedJobsCount++;
						flag = true;
						if(hostname == "") {
							hostname=trj.getHostList().getHostName();
						} else if(!hostname.trim().equalsIgnoreCase(trj.getHostList().getHostName().trim())){
							hostname+=","+trj.getHostList().getHostName();
						}
					}
					if(!(abortedJobsCount > 0 || completedjobsCount > 0)){
						log.info("Workpackage Execution Remarks : Workpackage could not be executed completely either Job(s) cannot be created or Host(s) / Device(s) went offline / disconnected interruptedly.");
					}
					messageText.append("<br/>");						
				}
			} else {
				messageText.append("Workpackage Execution Remarks : Workpackage could not be executed completely either Job(s) cannot be created or Host(s) / Device(s) went offline / disconnected interruptedly.");
			}
			
			if(workPackage.getWorkFlowEvent().getWorkFlow().getEntityMaster().getEntitymasterid() == IDPAConstants.WORKPACKAGE_ENTITY_ID){
				//mailSubject="TAF Workpackage Status for ID : "+ workPackage.getWorkPackageId()+" is COMPLETED - Reg.";
				String result="";
				if(!tester.getResult().contains("---")) {
					result="[Result : "+ tester.getResult() +"]";
				}
				mailSubject="WorkpackageId :"+workPackage.getWorkPackageId()+" : Test Report "+result;
				helper.setSubject(mailSubject);								
			} 			
			
			//Get the workpackage result start
			
			
			//Get the WP result end
				messageText.append("Hi,");
				messageText.append("<br/>");
				messageText.append("<br/>");
				messageText.append("Attached Workpackage detail Report and below Summary report");
				messageText.append("<br/>");
				messageText.append("<br/>");
				messageText.append("<table style='font-size: 11px;font-family:Verdana, sans-serif;margin-top: 5px; margin-bottom: 5px;' border='1' cellspacing='0' cellpadding='0'>");
				messageText.append("<tr>");
				messageText.append("<th style='padding-right: 10px; padding-left: 5px; background-color: #4C9B6E;color: #fff;' colspan='2'>Workpackage Summary</th>");
				messageText.append("</tr>");
				messageText.append("<tr>");
				messageText.append("<th style='padding-right: 10px; padding-left: 2px; background-color: #4C9B6E;color: #fff;'>ID</th><td style='padding-left: 2px;padding-right: 10px;'>"+workPackage.getWorkPackageId()+"</td>");
				messageText.append("</tr>");
				
				messageText.append("<tr>");
				messageText.append("<th style='padding-right: 10px; padding-left: 2px; background-color: #4C9B6E;color: #fff;'>Name</th><td style='padding-left: 2px;padding-right: 10px;'>"+workPackage.getName()+"</td>");
				messageText.append("</tr>");
				
				messageText.append("<tr>");
				messageText.append("<th style='padding-right: 10px; padding-left: 2px; background-color: #4C9B6E;color: #fff;'>Product</th><td style='padding-left: 2px;padding-right: 10px;'>"+workPackage.getProductBuild().getProductVersion().getProductMaster().getProductName()+"</td>");
				messageText.append("</tr>");
				
				messageText.append("<tr>");
				messageText.append("<th style='padding-right: 10px; padding-left: 2px; background-color: #4C9B6E;color: #fff;'>Product Build</th><td style='padding-left: 2px;padding-right: 10px;'>"+workPackage.getProductBuild().getBuildname()+"</td>");
				messageText.append("</tr>");
				
				messageText.append("<tr>");
				messageText.append("<th style='padding-right: 10px; padding-left: 10px; background-color: #4C9B6E;color: #fff;'>Test Plan</th><td style='padding-left: 2px;padding-right: 10px;'>"+workPackage.getTestRunPlan().getTestRunPlanName()+"</td>");
				messageText.append("</tr>");
				Integer jobCount=workPackage.getTestRunJobSet() != null ? workPackage.getTestRunJobSet().size() : 0 ;		
				messageText.append("<tr>");
				messageText.append("<th style='padding-right: 10px; padding-left: 2px; background-color: #4C9B6E;color: #fff;'>Job Ids</th><td style='padding-left: 2px;padding-right: 10px;'>"+jobCount+"("+jobDetails+")</td>");
				messageText.append("</tr>");
				
				messageText.append("<tr>");
				messageText.append("<th style='padding-right: 10px; padding-left: 2px; background-color: #4C9B6E;color: #fff;'>HostName</th><td style='padding-left: 2px;padding-right: 10px;'>"+hostname+"</td>");
				messageText.append("</tr>");
	
				
				messageText.append("<tr>");
				messageText.append("<th style='padding-right: 10px; padding-left: 2px; background-color: #4C9B6E;color: #fff;'>Status</th><td style='padding-left: 2px;padding-right: 10px;'>"+tester.getWpStatus()+"</td>");
				messageText.append("</tr>");
				//change
				Integer wpTotalTestcasesCount = 0;
				if(workPackage.getTestRunJobSet() != null){
					for(TestRunJob trj : workPackage.getTestRunJobSet()){
						wpTotalTestcasesCount += workPackageDAO.getExecutionTCCountForJob(trj.getTestRunJobId());
					}
				}
				Integer executedTC = 0;
				executedTC = tester.getP2totalPass() + tester.getP2totalFail();
				messageText.append("<tr>");
				if(!tester.getResult().contains("---")) {
					messageText.append("<th style='padding-right: 10px; padding-left: 2px; background-color: #4C9B6E;color: #fff;'>Result</th><td style='padding-left: 2px;padding-right: 10px;'>"+tester.getResult()+"["+executedTC+"/"+wpTotalTestcasesCount +"]</td>");
				} else {
					messageText.append("<th style='padding-right: 10px; padding-left: 2px; background-color: #4C9B6E;color: #fff;'>Result</th><td style='padding-left: 2px;padding-right: 10px;'>["+executedTC+"/"+wpTotalTestcasesCount +"]</td>");
				}
				messageText.append("</tr>");
				
				messageText.append("<tr>");
				messageText.append("<th style='padding-right: 10px; padding-left: 2px; background-color: #4C9B6E;color: #fff;'>Start Time</th><td style='padding-left: 2px;padding-right: 10px;'>"+sdf.format(workPackage.getCreateDate())+"</td>");
				messageText.append("</tr>");
				
				messageText.append("<tr>");
				messageText.append("<th style='padding-right: 10px; padding-left: 2px; background-color: #4C9B6E;color: #fff;'>End Time</th><td style='padding-left: 2px;padding-right: 10px;'>"+sdf.format(workPackage.getActualEndDate())+"</td>");
				messageText.append("</tr>");
				
				messageText.append("</table>");
			
				messageText.append("<br/>");
				/*if(workPackage.getWorkFlowEvent().getWorkFlow().getEntityMaster().getEntitymasterid() == IDPAConstants.WORKPACKAGE_ENTITY_ID && (workPackage.getWorkFlowEvent().getWorkFlow().getStageId() == IDPAConstants.WORKFLOW_STAGE_ID_COMPLETED)){
					messageText.append("Overall WorkPackage Status : COMPLETED");
				}
				messageText.append("<br/>");
				if(completedjobsCount > 1){
					messageText.append("Please refer the attached report for details");
					messageText.append("\n");
				}*/
				messageText.append("<br/>");
				messageText.append("Regards,");
				messageText.append("<br/>");
				messageText.append("TAF Administrator");
				messageText.append("<br/>");
				
				//Attachment for Workpackage
			
				if(workPackage.getWorkFlowEvent().getWorkFlow().getEntityMaster().getEntitymasterid() == IDPAConstants.WORKPACKAGE_ENTITY_ID  && (workPackage.getWorkFlowEvent().getWorkFlow().getStageId()== IDPAConstants.WORKFLOW_STAGE_ID_COMPLETED)){
					jobReportLocation = workPackageCompletionHTMLReportMail(workPackage);
					log.info("Workpackage Report Export Location : "+jobReportLocation);
				}
				
				if (jobReportLocation == null || jobReportLocation.trim().isEmpty()) {
					log.info("Could not add attachment by E-Mail.");
				} else {
					FileSystemResource file = new FileSystemResource(new File(jobReportLocation));
					helper.addAttachment("Workpackage : "+workPackage.getWorkPackageId()+".zip", file);
				}
				Map model = new HashMap<>();
				model.put("mime", messageText);
				
				log.info("Complete mail info:"+messageText.toString());
			helper.setText(messageText.toString());
			//SMTP Mail Channel
			
			if(emailChannel.equalsIgnoreCase("SMTP")) {
				try {
					mailSender.send(message);
				} catch (MailException m) {
					log.error("FAIL : Problem while sending report by mail ", m);
					return "FAIL : Problem while sending report by mail " + m.getMessage();
				}
			} else if (emailChannel.equalsIgnoreCase("Outlook")){
			
				HashMap<String, Object> mailParams = new HashMap<String, Object>();
				if(primaryEmailIds !="") {
					primaryEmailIds = primaryEmailIds.replaceAll(",", ";");
				}
				
				if(secondaryEmailIds !="") {
					secondaryEmailIds = secondaryEmailIds.replaceAll(",", ";");
				}
				mailParams.put("to", primaryEmailIds);
				if(secondaryEmailIds !="" && !secondaryEmailIds.trim().isEmpty()){
					mailParams.put("cc", secondaryEmailIds);
				}
				mailParams.put("subject",mailSubject );
				mailParams.put("body", messageText.toString());
				if(jobReportLocation != null && !jobReportLocation.contains("No records available for the current")){
					String attachment[]={jobReportLocation}; 
					mailParams.put("attachments", attachment);
				}
				String result = sendOutLookEmail(mailParams);
				if (result == null || result.startsWith("FAIL")) {
					return result;
				}
			} else {
				log.info("No Mail channel enabled");
				return "FAIL : No Mail channel enabled";
			}
			log.info("Workpackage Status mail has been successfully sent to subscribers");
		} catch (Exception e) {
			log.error("Unable to send Workpackage Status mail to subscribers : "+ e.getMessage());
		}	
		return "SUCCESS : Sent workpackage report by email";
	}
}