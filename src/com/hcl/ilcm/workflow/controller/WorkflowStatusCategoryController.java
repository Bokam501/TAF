/**
 * 
 */
package com.hcl.ilcm.workflow.controller;

import java.util.ArrayList;
import java.util.Date;
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

import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.WorkflowStatusCategory;
import com.hcl.atf.taf.model.json.jtable.JTableResponse;
import com.hcl.atf.taf.model.json.jtable.JTableResponseOptions;
import com.hcl.atf.taf.model.json.jtable.JTableSingleResponse;
import com.hcl.ilcm.workflow.model.json.JsonWorkflowStatusCategory;
import com.hcl.ilcm.workflow.service.WorkflowStatusCategoryService;

/**
 * @author silambarasur
 *
 */
@Controller
public class WorkflowStatusCategoryController {
	
	private static final Log log = LogFactory.getLog(WorkflowStatusCategoryController.class);
	
	@Autowired
	private WorkflowStatusCategoryService workflowStatusCategoryService; 
	
	
	@RequestMapping(value = "workflow.status.category.list", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	JTableResponse getWorkflowStatusCategoryList(HttpServletRequest req,@RequestParam int workflowId,@RequestParam int isActive) {
		log.info("inside workflow.status.category.list");
		JTableResponse jTableResponse = null;
		List<JsonWorkflowStatusCategory> jsonWorkflowStatusCategories = new ArrayList<JsonWorkflowStatusCategory>();
		List<WorkflowStatusCategory> workflowStatusCategories = new ArrayList<WorkflowStatusCategory>();
		try {

			workflowStatusCategories = workflowStatusCategoryService.getWorkflowStatusCategoryByWorkflowId(workflowId,isActive);

			if (workflowStatusCategories != null && workflowStatusCategories.size() > 0) {
				for (WorkflowStatusCategory workflowStatusCategory : workflowStatusCategories) {
					jsonWorkflowStatusCategories.add(new JsonWorkflowStatusCategory(workflowStatusCategory));
				}
			}

			jTableResponse = new JTableResponse("OK", jsonWorkflowStatusCategories, jsonWorkflowStatusCategories.size());
			log.info("inside process fetching workflow Status Category records ");
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR",
					"Error fetching records!");
			log.error("JSON ERROR", e);
		}
		return jTableResponse;

	}

	@RequestMapping(value = "workflow.status.category.add", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	JTableSingleResponse addWorkflowStatusCategory(@ModelAttribute JsonWorkflowStatusCategory jsonWorkflowStatusCategory,BindingResult result, HttpServletRequest request) {
		JTableSingleResponse jTableSingleResponse = null;
		try {

			WorkflowStatusCategory workflowStatusCategory = jsonWorkflowStatusCategory.getWorkflowStatusCategory();
			
			UserList user = (UserList) request.getSession().getAttribute("USER");
			workflowStatusCategory.setCreatedBy(user);
			workflowStatusCategory.setModifiedBy(user);
			workflowStatusCategory.setCreatedDate(new Date());
			workflowStatusCategory.setModifiedDate(new Date());

			workflowStatusCategoryService.addWorkflowStatusCategory(workflowStatusCategory, jsonWorkflowStatusCategory.getWorkflowId());
			jTableSingleResponse = new JTableSingleResponse("OK",new JsonWorkflowStatusCategory(workflowStatusCategory));
			log.info("workflow.status.category.add success!");
		} catch (Exception e) {
			jTableSingleResponse = new JTableSingleResponse("ERROR",
					"Error adding workflow Status Category data record!");
			log.error("JSON ERROR", e);
		}
		return jTableSingleResponse;
	}
	
	@RequestMapping(value = "workflow.status.category.update", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	JTableSingleResponse updateWorkflowLifeCycleStatusCategory(HttpServletRequest request,@ModelAttribute JsonWorkflowStatusCategory jsonWorkflowStatusCategory,BindingResult result) {
		JTableSingleResponse jTableSingleResponse = null;
		try {

			WorkflowStatusCategory workflowStatusCategory = jsonWorkflowStatusCategory.getWorkflowStatusCategory();
			UserList user = (UserList) request.getSession().getAttribute("USER");
			workflowStatusCategory.setModifiedBy(user);
			workflowStatusCategory.setModifiedDate(new Date());	
			
			
			if(jsonWorkflowStatusCategory.getWorkflowStatusCategoryOrder() == null){
				String msg=" The WorkflowStatusCategory should not be alphanumeric ";
				jTableSingleResponse = new JTableSingleResponse("ERROR",msg);
			    return jTableSingleResponse; 
			}
			workflowStatusCategory.setWorkflowStatusCategoryOrder(jsonWorkflowStatusCategory.getWorkflowStatusCategoryOrder());			
			workflowStatusCategoryService.updateWorkflowStatusCategory(workflowStatusCategory, jsonWorkflowStatusCategory.getWorkflowId());
			jTableSingleResponse = new JTableSingleResponse("OK",new JsonWorkflowStatusCategory(workflowStatusCategory));
			log.info("workflow.status.category.update success!");
		} catch (Exception e) {
			jTableSingleResponse = new JTableSingleResponse("ERROR", "Error updating workflow Status Category data record!");
			log.error("JSON ERROR", e);
		}
		return jTableSingleResponse;
	}
	
	@RequestMapping(value="workflow.status.category.option.list", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody JTableResponseOptions listAllStatusCategories(HttpServletRequest req,@RequestParam int workflowId){
		JTableResponseOptions jTableResponseOptions = null;
		List<WorkflowStatusCategory> workflowStatusCategories = new ArrayList<WorkflowStatusCategory>();
		try{
			workflowStatusCategories = workflowStatusCategoryService.getWorkflowStatusCategoryList(workflowId);
			List<JsonWorkflowStatusCategory> jsonWorkflowStatusCategories = new ArrayList<JsonWorkflowStatusCategory>();
			if(workflowStatusCategories != null && workflowStatusCategories.size() > 0){
				for(WorkflowStatusCategory workflowWorkflowStatusCategory : workflowStatusCategories){
					jsonWorkflowStatusCategories.add(new JsonWorkflowStatusCategory(workflowWorkflowStatusCategory));
				}
			}else{
				JsonWorkflowStatusCategory jsonWorkflowStatusCategory = new JsonWorkflowStatusCategory();
				jsonWorkflowStatusCategory.setWorkflowStatusCategoryId(0);
				jsonWorkflowStatusCategory.setWorkflowStatusCategoryName("--");;
				jsonWorkflowStatusCategories.add(jsonWorkflowStatusCategory);
			}
			jTableResponseOptions = new JTableResponseOptions("OK", jsonWorkflowStatusCategories, true);
			return jTableResponseOptions;
		
		}catch (Exception e) {
        	jTableResponseOptions = new JTableResponseOptions("ERROR","Please map workflow status Category to the workflow.");
            log.error("JSON ERROR", e);
        }	
		jTableResponseOptions = new JTableResponseOptions("ERROR","Please map workflow status Category to the workflow.");
		log.info("jTableResponseOptions success");
		return jTableResponseOptions;
	}


}
