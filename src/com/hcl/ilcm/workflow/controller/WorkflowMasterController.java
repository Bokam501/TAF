/**
 * 
 */
package com.hcl.ilcm.workflow.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.json.jtable.JTableResponse;
import com.hcl.atf.taf.model.json.jtable.JTableResponseOptions;
import com.hcl.atf.taf.model.json.jtable.JTableSingleResponse;
import com.hcl.atf.taf.service.CommonService;
import com.hcl.atf.taf.service.EventsService;
import com.hcl.atf.taf.service.UserListService;
import com.hcl.atf.taf.service.WorkPackageService;
import com.hcl.ilcm.workflow.model.WorkflowMaster;
import com.hcl.ilcm.workflow.model.WorkflowMasterEntityMapping;
import com.hcl.ilcm.workflow.model.json.JsonWorkflowMaster;
import com.hcl.ilcm.workflow.service.WorkflowMasterService;
import com.hcl.ilcm.workflow.service.WorkflowStatusService;

/**
 * @author silambarasur
 * 
 */
@Controller
public class WorkflowMasterController {

	private static final Log log = LogFactory
			.getLog(WorkflowMasterController.class);
	@Autowired
	private WorkflowMasterService workflowMasterService;
	@Autowired
	private UserListService userListService;
	@Autowired
	private WorkPackageService workPackageService;
	@Autowired
	private EventsService eventsService;
	@Autowired
	private CommonService commonService;
	@Autowired
	private WorkflowStatusService workflowStatusService;

	@RequestMapping(value = "workflow.master.list", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	JTableResponse getWorkflowMasterList(HttpServletRequest req,
			@RequestParam int isActive, @RequestParam int jtStartIndex,
			@RequestParam int jtPageSize) {
		log.info("inside workflow.master.list");
		JTableResponse jTableResponse = null;
		List<JsonWorkflowMaster> jsonWorkflowMasters = new ArrayList<JsonWorkflowMaster>();
		List<WorkflowMaster> workflowMasterList = null;
		try {

			workflowMasterList = workflowMasterService.getWorkflowMasterListByStatus(isActive);

			if (workflowMasterList != null && workflowMasterList.size() > 0) {
				List<Object[]> attachmentCountDetails = commonService.getAttachmentCountOfEntity(IDPAConstants.ENTITY_WORKFLOW_TEMPLATE_ID);
				for (WorkflowMaster wfmaster : workflowMasterList) {
					JsonWorkflowMaster jsonWorkflowMaster = new JsonWorkflowMaster(wfmaster);
					Integer attachmentCount = commonService.getCountFromAttachmentCountDetails(attachmentCountDetails, jsonWorkflowMaster.getWorkflowId());
					jsonWorkflowMaster.setAttachmentCount(attachmentCount);
					jsonWorkflowMasters.add(jsonWorkflowMaster);
				}
			}

			jTableResponse = new JTableResponse("OK", jsonWorkflowMasters, jsonWorkflowMasters.size());
			log.info("inside process fetching workflow Master records ");
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR",
					"Error fetching records!");
			log.error("JSON ERROR", e);
		}
		return jTableResponse;

	}

	@RequestMapping(value = "workflow.master.add", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody JTableSingleResponse addWorkflowMaster(HttpServletRequest request,	@ModelAttribute JsonWorkflowMaster jsonWorkflowMaster, BindingResult result) {
		JTableSingleResponse jTableSingleResponse = null;
		try {

			WorkflowMaster workflowMaster = jsonWorkflowMaster.getWorkflowMaster();
			if(workflowMaster != null && (workflowMaster.getWorkflowName() == null || workflowMaster.getWorkflowName().isEmpty())) {
				jTableSingleResponse = new JTableSingleResponse("ERROR", "Workflow template name should not empty!");
				return jTableSingleResponse;
			}
			Boolean isWorkflowExist = workflowMasterService.isWorkflowAleadyExist(null, workflowMaster.getWorkflowName());
			if(isWorkflowExist){
				jTableSingleResponse = new JTableSingleResponse("ERROR", "Workflow with same name already available");
				return jTableSingleResponse;
			}
			UserList user = (UserList) request.getSession().getAttribute("USER");
			workflowMaster.setCreatedBy(user);
			workflowMaster.setModifiedBy(user);
			workflowMaster.setCreatedDate(new Date());
			workflowMaster.setModifiedDate(new Date());
			workflowMaster.setActiveState(1);
			
			workflowMasterService.addWorkflowMaster(workflowMaster);
			if(workflowMaster != null && workflowMaster.getWorkflowId() != null){
				eventsService.addNewEntityEvent(IDPAConstants.ENTITY_WORKFLOW_TEMPLATE, workflowMaster.getWorkflowId(), workflowMaster.getWorkflowName(), user);
			}
			jTableSingleResponse = new JTableSingleResponse("OK",new JsonWorkflowMaster(workflowMaster));
			log.info("workflow.master.add Success");
		} catch (Exception e) {
			jTableSingleResponse = new JTableSingleResponse("ERROR", "Error adding workflow master data record!");
			log.error("JSON ERROR", e);
		}
		return jTableSingleResponse;
	}

	@RequestMapping(value = "workflow.master.update", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody JTableSingleResponse updateWorkflowMaster(HttpServletRequest request, @ModelAttribute JsonWorkflowMaster jsonWorkflowMaster, BindingResult result) {
		JTableSingleResponse jTableSingleResponse = null;
		String remarks = "";
		try {

			WorkflowMaster workflowMaster = jsonWorkflowMaster.getWorkflowMaster();
			Boolean isWorkflowExist = workflowMasterService.isWorkflowAleadyExist(workflowMaster.getWorkflowId(), workflowMaster.getWorkflowName());
			if(isWorkflowExist){
				jTableSingleResponse = new JTableSingleResponse("ERROR", "Workflow with same name already available");
				return jTableSingleResponse;
			}
			UserList user = (UserList) request.getSession()
					.getAttribute("USER");
			workflowMaster.setModifiedBy(user);
			workflowMaster.setModifiedDate(new Date());
			if(jsonWorkflowMaster.getModifiedField() != null && "workflowType".equalsIgnoreCase(jsonWorkflowMaster.getModifiedField())){
				List<WorkflowMasterEntityMapping> workflowMasterEntityMappings = workflowMasterService.getEntitiesAndInstanceMappedWithWorkflow(workflowMaster.getWorkflowId());
				if(workflowMasterEntityMappings != null && workflowMasterEntityMappings.size() > 0){
					return new JTableSingleResponse("ERROR", "Workflow type change is not allowed, association with entity or instance is exist");
				}
			}
			
			if(jsonWorkflowMaster.getModifiedField() != null && "readyState".equalsIgnoreCase(jsonWorkflowMaster.getModifiedField()) && jsonWorkflowMaster.getReadyState() == 1){
				Boolean isWorkflowReady = workflowStatusService.isWorkflowContainsMandatoryStatusTypeStatus(workflowMaster.getWorkflowId());
				if(!isWorkflowReady){
					return new JTableSingleResponse("ERROR", "Please check workflow contains atleast on status in Begin, Intermediate and End status types");
				}
			}
			workflowMasterService.updateWorkflowMaster(workflowMaster);
			remarks = "WorkflowMaster :"+workflowMaster.getWorkflowName();
			if(workflowMaster != null && workflowMaster.getWorkflowId() != null){				
				eventsService.addEntityChangedEvent(IDPAConstants.ENTITY_WORKFLOW_TEMPLATE, workflowMaster.getWorkflowId(), workflowMaster.getWorkflowName(),
						jsonWorkflowMaster.getModifiedField(), jsonWorkflowMaster.getModifiedFieldTitle(),
						jsonWorkflowMaster.getOldFieldValue(), jsonWorkflowMaster.getModifiedFieldValue(), user, remarks);
			}
			jTableSingleResponse = new JTableSingleResponse("OK",new JsonWorkflowMaster(workflowMaster));
			log.info("workflow.master.update Success");
		} catch (Exception e) {
			jTableSingleResponse = new JTableSingleResponse("ERROR",
					"Error adding workflow master data record!");
			log.error("JSON ERROR", e);
		}
		return jTableSingleResponse;
	}
	
	@RequestMapping(value="workflow.master.list.options",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions getWorkflowMasterListOptions(@RequestParam String workflowType) {
		log.info("inside workflow.master.list.options ");
		JTableResponseOptions jTableResponseOptions;
		try {			
			List<WorkflowMaster> workflowMasters = workflowMasterService.getWorkflowMasterListByType(workflowType);
			List<JsonWorkflowMaster> jsonWorkflowMasters = new ArrayList<JsonWorkflowMaster>();
			if (workflowMasters != null && workflowMasters.size()>0) {
				JsonWorkflowMaster jsonWorkflowMaster =  null;
				for (WorkflowMaster workflowMaster : workflowMasters) {
					if(workflowMaster.getReadyState() != null && workflowMaster.getReadyState() == 1){
						jsonWorkflowMaster = new JsonWorkflowMaster(workflowMaster);
						jsonWorkflowMasters.add(jsonWorkflowMaster);
					}
				}
			}
			if(jsonWorkflowMasters != null && jsonWorkflowMasters.size() == 0){
				WorkflowMaster workflowMaster = new WorkflowMaster();
				workflowMaster.setWorkflowId(0);
				workflowMaster.setWorkflowName("--");
				JsonWorkflowMaster jsonWorkflowMaster = new JsonWorkflowMaster(workflowMaster);
				jsonWorkflowMasters.add(jsonWorkflowMaster);
			}
			jTableResponseOptions = new JTableResponseOptions("OK", jsonWorkflowMasters, true);
			
			return jTableResponseOptions;
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Workflow master retrival problem!");
	            log.error("JSON ERROR", e);
	        }
        return jTableResponseOptions;
    }
	
	@RequestMapping(value="workflow.master.mapped.to.entity.list.options",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions getWorkflowMasterMappedToEntityListOptions(@RequestParam Integer productId, @RequestParam Integer entityTypeId, @RequestParam Integer entityId) {
		log.info("Inside workflow.master.mapped.to.entity.list.options ");
		JTableResponseOptions jTableResponseOptions;
		Set<JsonWorkflowMaster> jsonWorkflowMasterSet = null;
		List<JsonWorkflowMaster> jsonWorkFlowMasterUnique = null;
		try {			
			Integer activeState = 1;
			List<WorkflowMaster> workflowMasters = workflowMasterService.getWorkflowMasterMappedToEntityList(productId, entityTypeId, entityId, activeState);
			List<JsonWorkflowMaster> jsonWorkflowMasters = new ArrayList<JsonWorkflowMaster>();
			if (workflowMasters != null && workflowMasters.size()>0) {
				JsonWorkflowMaster jsonWorkflowMaster =  null;
				for (WorkflowMaster workflowMaster : workflowMasters) {
					if(workflowMaster.getReadyState() != null && workflowMaster.getReadyState() == 1){
						jsonWorkflowMaster = new JsonWorkflowMaster(workflowMaster);
						jsonWorkflowMasters.add(jsonWorkflowMaster);
						jsonWorkflowMasterSet = new HashSet<JsonWorkflowMaster>(jsonWorkflowMasters);
						jsonWorkFlowMasterUnique = new ArrayList<JsonWorkflowMaster>(jsonWorkflowMasterSet);
					}
				}
			}
			if(jsonWorkflowMasters != null && jsonWorkflowMasters.size() == 0){
				WorkflowMaster workflowMaster = new WorkflowMaster();
				workflowMaster.setWorkflowId(0);
				workflowMaster.setWorkflowName("--");
				JsonWorkflowMaster jsonWorkflowMaster = new JsonWorkflowMaster(workflowMaster);
				jsonWorkflowMasters.add(jsonWorkflowMaster);				
				 jsonWorkflowMasterSet = new HashSet<JsonWorkflowMaster>(jsonWorkflowMasters);
				 jsonWorkFlowMasterUnique = new ArrayList<JsonWorkflowMaster>(jsonWorkflowMasterSet);
			}
			jTableResponseOptions = new JTableResponseOptions("OK", jsonWorkFlowMasterUnique, true);
			
			return jTableResponseOptions;
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Workflow master retrival problem!");
	            log.error("JSON ERROR", e);
	        }
        return jTableResponseOptions;
    }
	
	@RequestMapping(value="workflow.master.mapped.to.entity.instance",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableSingleResponse getWorkflowMasterMappedToEntityInstance(@RequestParam Integer productId, @RequestParam Integer entityTypeId, @RequestParam Integer entityId, @RequestParam Integer entityInstanceId) {
		log.info("Fetching workflow.master.mapped.to.entity.instance ");
		JTableSingleResponse jTableSingleResponse;
		try {			
			Integer activeState = 1;
			WorkflowMaster workflowMaster = workflowMasterService.getWorkflowForEntityTypeOrInstance(productId, entityTypeId, entityId, entityInstanceId, activeState);
			if(workflowMaster != null){
				jTableSingleResponse = new JTableSingleResponse("OK", workflowMaster.getWorkflowId());
			}else{
				jTableSingleResponse = new JTableSingleResponse("ERROR", "No workflow associated, please select a workflow to associate");
			}
		} catch (Exception e) {
			jTableSingleResponse = new JTableSingleResponse("ERROR","Workflow master retrival problem!");
			log.error("Error in getWorkflowMasterMappedToEntityInstance ", e);
		}
		return jTableSingleResponse;
	}
}
