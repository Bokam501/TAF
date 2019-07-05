/**
 * 
 */
package com.hcl.atf.taf.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.dao.CommonDAO;
import com.hcl.atf.taf.model.Comments;
import com.hcl.atf.taf.model.ReportIssue;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.json.JsonReporIssue;
import com.hcl.atf.taf.model.json.jtable.JTableResponse;
import com.hcl.atf.taf.model.json.jtable.JTableSingleResponse;
import com.hcl.atf.taf.service.CommonService;
import com.hcl.atf.taf.service.EmailService;
import com.hcl.atf.taf.service.ReportService;

/**
 * @author Tapashi
 *
 */
@Controller
@Path("reportIssue")
public class ReportIssueController {

	
	private static final Log log = LogFactory.getLog(ReportIssueController.class);
	@Autowired
	private EmailService emailService;
	
	@Value("#{ilcmProps['EMAIL_ADDRESS_TO_SUPPORT_TEAM']}")
    private String supportEmailIds;
	
	@Value("#{ilcmProps['EMAIL_ALERT_NOTIFICATION']}")
    private String emailNotification;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private ReportService reportService;
	@Autowired
	private CommonDAO commonDAO;
	@Autowired
	private CommonService commonService;
	
	@GET
	@Path("/reportIssueMailPreparation")
	public Response reportIssue(@QueryParam("message") String message,@QueryParam("reportIssueType") String reportIssueType) {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	
	    try{
		
	    	     String subject="["+reportIssueType+"] Report Issue";
	         
	             String[] primaryEmailIds = supportEmailIds.split(",");
	             String[] secondaryEmailIds = null;
	             UserList user = (UserList) request.getSession().getAttribute("USER");
	             String loginId="";
	             String fromMailId="";
	             if(user != null) {
	            	 loginId=user.getLoginId();
	            	 fromMailId= user.getEmailId();
	            	 
	             }
	             ReportIssue reportIssue = new ReportIssue();
	             reportIssue.setReportIssueName(message);
	             reportIssue.setReportIssueType(reportIssueType);
	             reportIssue.setReportIssueDate(new Date());
	             reportIssue.setReportIssueStatus("Open");
	             reportIssue.setReportLoginId(loginId);
	             reportService.addReportIssue(reportIssue);
	             
	            log.info("inside sendReportIssueEmail method");
	            if(emailNotification.equalsIgnoreCase("YES")) {
	            	emailService.sendReportIssue(fromMailId, primaryEmailIds, secondaryEmailIds, subject, message);	
	            }
			
		}catch(Exception e){
			log.error("report.issue.emails...",e);
		}
		
		
			return Response
			   .status(200)
			   .entity("<h1 style='color: Green;font-size:15px;'>Email Sent Successfully ").build();			

		}
	
	
	@RequestMapping(value = "administration.report.issue.list", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	JTableResponse viewRepotIssue(HttpServletRequest request,@RequestParam int jtStartIndex,@RequestParam int jtPageSize) {
		log.debug("inside administration.report.issue.list");
		List<Object[]> attachmentCountDetails = commonDAO.getAttachmentCountOfEntity(IDPAConstants.REPORTISSUE_ENTITY_MASTER_ID);
		List<JsonReporIssue> jsonReportIssueList = new ArrayList<JsonReporIssue>();
		List<ReportIssue> reportIssueList = null;
		JTableResponse jTableResponse;
		try {
			reportIssueList =	reportService.getReportIssueList(); 
			if(reportIssueList !=null && reportIssueList.size() >0){
				for(ReportIssue reportIssue : reportIssueList ){
					JsonReporIssue jsonReportIssue = new JsonReporIssue(reportIssue);
				    Comments comments =  commonService.getLatestCommentOfByEntityTypeAndInstanceId(IDPAConstants.REPORTISSUE_ENTITY_MASTER_ID, jsonReportIssue.getReportIssueid());
				    if(comments != null) {
				    	jsonReportIssue.setReportIssuecomment(comments.getComments());
				    }
					jsonReportIssue.setAttachmentCount(commonService.getCountFromAttachmentCountDetails(attachmentCountDetails, jsonReportIssue.getReportIssueid()));
					jsonReportIssueList.add(jsonReportIssue);
				}
			
			}          
			jTableResponse = new JTableResponse("OK",jsonReportIssueList);
			log.info("inside process fetching reportissue  records ");
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR",
					"Error fetching records!");
			log.error("JSON ERROR", e);
		}
		
		return jTableResponse;
	}
	
	@RequestMapping(value = "administration.report.issue.update", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	JTableResponse updateReportIssue(HttpServletRequest request, @ModelAttribute JsonReporIssue jsonReportIssue,BindingResult result) {
		log.debug("administration.report.issue.update");
		JTableResponse jTableResponse = null;
		if (result.hasErrors()) {
			jTableResponse = new JTableResponse("ERROR", "Invalid Form!");
		}
		try {
            	ReportIssue reportIssueFromUI = jsonReportIssue.getResponseIssue();
            	reportService.updateReportIssue(reportIssueFromUI);
            		
            	jTableResponse = new JTableResponse("OK","ReportIssue updated successfully");
		} 
		
		catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR",
					"Unable to update the ReportIssue!");
			log.error("JSON ERROR", e);
		}
		return jTableResponse;
	}	
	
	@RequestMapping(value = "administration.report.issue.add", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody JTableSingleResponse addReportIssue(HttpServletRequest req, @ModelAttribute JsonReporIssue jsoneReportIssue) {	
	JTableSingleResponse jTableSingleResponse = null;
		try {
			log.info("administration.report.issue.add");					
			ReportIssue reportIssue = jsoneReportIssue.getResponseIssue();	
			
			reportService.addReportIssue(reportIssue);
			jTableSingleResponse = new JTableSingleResponse("OK",
					new JsonReporIssue(reportIssue));
			
		} catch (Exception e) {
			jTableSingleResponse = new JTableSingleResponse("ERROR",
					"Error adding ReportIssue record!");
			log.error("JSON ERROR", e);
		}
		return jTableSingleResponse;
	}
	

}
