package com.hcl.atf.taf.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.json.JsonProductBuild;
import com.hcl.atf.taf.model.json.JsonWorkPackageTestCaseExecutionPlanForTester;
import com.hcl.atf.taf.model.json.jtable.JTableResponse;
import com.hcl.atf.taf.service.DataMapTreeService;
import com.hcl.atf.taf.service.DataTreeService;
@Controller
public class TreeController {

	private static final Log log = LogFactory.getLog(TreeController.class);
	
	@Autowired
	private DataTreeService dataTreeService;
	
	@Autowired
	private DataMapTreeService dataMapTreeService;
	

		@RequestMapping(value="administration.workpackage.tree", method=RequestMethod.POST, produces="application/json")
	    public @ResponseBody String getWorkPackageTree(HttpServletRequest request) {
			log.debug("inside administration.workpackage.tree");
			String jsonStringDataForTree = "";
			UserList user = (UserList)request.getSession().getAttribute("USER");
			jsonStringDataForTree = dataTreeService.getWorkPackageTree(user.getUserRoleMaster().getUserRoleId(),user.getUserId());
	        return jsonStringDataForTree;
		}
		
		@RequestMapping(value="administration.workpackage.plan.tree", method=RequestMethod.POST, produces="application/json")
	    public @ResponseBody JSONArray getWorkPackagePlanTree(HttpServletRequest request) {
			JSONArray jsonStringDataForTree = null;
			try {
				UserList user = (UserList)request.getSession().getAttribute("USER");
				int treeType = 0;
				int parentId=0;
				String  parentType="";
				if(request.getParameter("type") != null){
					treeType = Integer.parseInt(request.getParameter("type"));
				}
				if(request.getParameter("parentId") != null){
					parentId = Integer.parseInt(request.getParameter("parentId"));
				}
				if(request.getParameter("node") != null){
					parentType = request.getParameter("node");
				}
				log.debug("treeType: "+treeType+"parentType=="+parentType+","+parentId);
				jsonStringDataForTree = dataTreeService.getWorkPackagePlanTreeInJSON(user.getUserRoleMaster().getUserRoleId(),user.getUserId(), treeType,parentType,parentId);
			} catch (NumberFormatException e) {
				log.error("JSON Error Fetching WorkPackagePlan Tree",e);
			}
	        return jsonStringDataForTree;
		}
		
	
		@RequestMapping(value="administration.workpackage.testcase.review.tree", method=RequestMethod.POST, produces="application/json")
	    public @ResponseBody String getWorkPackageTestCaseReviewTree(HttpServletRequest request) {
			String jsonStringDataForTree = "";
			UserList user = (UserList)request.getSession().getAttribute("USER");
			jsonStringDataForTree = dataTreeService.getWorkPackageTestCaseReviewTree(user.getUserRoleMaster().getUserRoleId(),user.getUserId());
	        return jsonStringDataForTree;
		}
		
		@RequestMapping(value="administration.product.tree",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody String getProductTree(HttpServletRequest request) {
			log.debug("inside administration.product.tree");
			String jsonStringDataForTree = "";
			UserList user = (UserList)request.getSession().getAttribute("USER");
			jsonStringDataForTree = dataTreeService.getProductTree(user.getUserRoleMaster().getUserRoleId(),user.getUserId());
	        return jsonStringDataForTree;
		}
		
		
		@RequestMapping(value="administration.product.bot.tree",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody String getProductBotTree(HttpServletRequest request) {
			log.debug("inside administration.product.tree");
			String jsonStringDataForTree = "";
			UserList user = (UserList)request.getSession().getAttribute("USER");
			jsonStringDataForTree = dataTreeService.getProductBotTree(user.getUserRoleMaster().getUserRoleId(),user.getUserId());
	        return jsonStringDataForTree;
		}
		
		
		
		@RequestMapping(value="administration.product.hierarchy.tree",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody String getCompleteProductHierarchyTreeData(HttpServletRequest request) {
			log.debug("inside administration.product.environment.tree");
			String jsonStringDataForTree = "";
			UserList user = (UserList)request.getSession().getAttribute("USER");
			jsonStringDataForTree = dataTreeService.getProductHierarchyTree(user.getUserRoleMaster().getUserRoleId(),user.getUserId());
	        return jsonStringDataForTree;
		}
		
		@RequestMapping(value="eod.shifts.report.tree",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody String getEodShiftsReportTree(HttpServletRequest request) {
			log.debug("inside administration.product.environment.tree");
			String jsonStringDataForTree = "";
			UserList user = (UserList)request.getSession().getAttribute("USER");
			jsonStringDataForTree = dataTreeService.getEodShiftsReportTree(user.getUserRoleMaster().getUserRoleId(),user.getUserId());
	        return jsonStringDataForTree;
		}
		
		@RequestMapping(value="administration.product.tree.with.folder",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody String getProductTreeWithFolder(HttpServletRequest request) {
			log.debug("inside administration.product.tree");
			String jsonStringDataForTree = "";
			UserList user = (UserList)request.getSession().getAttribute("USER");
			jsonStringDataForTree = dataTreeService.getProductTreeWithFolder(user.getUserRoleMaster().getUserRoleId(),user.getUserId());
	        return jsonStringDataForTree;
		}
		
		@RequestMapping(value="testfactory.product.tree",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody String getTestFactoryProductTree(HttpServletRequest request) {
			log.debug("inside testfactory.product.tree");
			String jsonStringDataForTree = "";
			UserList user = (UserList)request.getSession().getAttribute("USER");
			jsonStringDataForTree = dataTreeService.getTestFactoryProductTree(user.getUserRoleMaster().getUserRoleId(),user.getUserId());
	        return jsonStringDataForTree;
		}
		
		@RequestMapping(value="testfactorylab.testfactory.product.tree",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody String getTestFactoryLabTestFactoryProductTree(HttpServletRequest request) {
			log.debug("inside testfactorylab.testfactory.product.tree");
			String jsonStringDataForTree = "";
			UserList user = (UserList)request.getSession().getAttribute("USER");
			jsonStringDataForTree = dataTreeService.getTestFactoryLabTestFactoryProductTree(user.getUserRoleMaster().getUserRoleId(),user.getUserId());
	        return jsonStringDataForTree;
		}
		
		@RequestMapping(value="administration.resource.pool.type.tree",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody String getResourcePoolTree(HttpServletRequest request) {
			log.debug("Inside administration.resource.pool.type.tree");
			String jsonStringDataForTree = "";
			UserList user = (UserList)request.getSession().getAttribute("USER");
			jsonStringDataForTree = dataTreeService.getResourcePoolTree(user.getUserRoleMaster().getUserRoleId(),user.getUserId());
	        return jsonStringDataForTree;
		}
		
		@RequestMapping(value="administration.resource.pool.type.from.lab.tree",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody String getResourcePoolTreeFromLab(HttpServletRequest request) {
			log.debug("Inside administration.resource.pool.type.tree");
			String jsonStringDataForTree = "";
			UserList user = (UserList)request.getSession().getAttribute("USER");
			jsonStringDataForTree = dataTreeService.getResourcePoolTreeFromLab(user);
	        return jsonStringDataForTree;
		}
		
		@RequestMapping(value="administration.user.type.tree",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody String getUserTree(HttpServletRequest request) {
			log.debug("inside administration.user.type.tree");
			String jsonStringDataForTree = "";
			UserList user = (UserList)request.getSession().getAttribute("USER");
			jsonStringDataForTree = dataTreeService.getUserTypeTree(user.getUserRoleMaster().getUserRoleId(),user.getUserId());
	        return jsonStringDataForTree;
		}
		
		@RequestMapping(value="testfactorylab.testfactory.shifts.tree",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody String getTestFactoryLabTestFactoryShiftsTree(HttpServletRequest request) {
			log.debug("inside testfactorylab.testfactory.shifts.tree");
			String jsonStringDataForTree = "";
			int treeType = 0;
			if(request.getParameter("type") != null){
				treeType = Integer.parseInt(request.getParameter("type"));
			}
			log.debug("treeType: "+treeType);
			UserList user = (UserList)request.getSession().getAttribute("USER");
			jsonStringDataForTree = dataTreeService.getTestFactoryShiftTree(user.getUserRoleMaster().getUserRoleId(),user.getUserId(), treeType);
	        return jsonStringDataForTree;
		}
		
		@RequestMapping(value="envicategory.envigroup.tree",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody String getEnvironmentCategoryGroupTree(HttpServletRequest request) {
			log.debug("inside testfactorylab.testfactory.shifts.tree");
			String jsonStringDataForTree = "";
			UserList user = (UserList)request.getSession().getAttribute("USER");
			jsonStringDataForTree = dataTreeService.getEnvironmentCategoryGroupTree(user.getUserRoleMaster().getUserRoleId(),user.getUserId());
	        return jsonStringDataForTree;
		}
		
		@RequestMapping(value="testfactorylabs.testfactories.products.complete.tree",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody String getTestFactoryLabTestFactoryProductsCompleteTree(HttpServletRequest request) {
			log.debug("inside testfactorylabs.testfactory.products.tree");
			String jsonStringDataForTree = "";
			UserList user = (UserList)request.getSession().getAttribute("USER");
			jsonStringDataForTree = dataTreeService.getTestFactoryLabTestFactoryProductsCompleteTree(user.getUserRoleMaster().getUserRoleId(),user.getUserId());
	        return jsonStringDataForTree;
		}
		
		
		@RequestMapping(value="administration.user.with.vendors.tree",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody String getUserVendorTree(HttpServletRequest request) {
			log.debug("inside administration.user.with.vendors.tree");
			String jsonStringDataForTree = "";
			UserList user = (UserList)request.getSession().getAttribute("USER");
			jsonStringDataForTree = dataTreeService.getUserVendorTree(user.getUserRoleMaster().getUserRoleId(),user.getUserId());
	        return jsonStringDataForTree;
		}
		
		
		@RequestMapping(value="product.version.tree",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody String getProductVersionTree(HttpServletRequest request) {
			log.debug("inside getProductVersionTree");
			String jsonStringDataForTree = "";
			UserList user = (UserList)request.getSession().getAttribute("USER");
			jsonStringDataForTree = dataTreeService.getProductVersionTree(user.getUserRoleMaster().getUserRoleId(),user.getUserId());
	        return jsonStringDataForTree;
		}
		
		@RequestMapping(value="workpackage.allocate.testcase.filter",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody String getAllocateTestcaseFilter(HttpServletRequest request,@RequestParam int workpackageId,@RequestParam String param) {
			log.debug("inside workpackage.allocate.testcase.filter");
			String jsonStringDataForTree = "";
			UserList user = (UserList)request.getSession().getAttribute("USER");
			jsonStringDataForTree = dataTreeService.getAllocateTestcaseFilter(user,workpackageId,param);
	        return jsonStringDataForTree;
		}
		
		@RequestMapping(value="administration.productWithTF.tree",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody String getProductWithTFTree(HttpServletRequest request) {
			log.debug("inside administration.product.tree");
			String jsonStringDataForTree = "";
			UserList user = (UserList)request.getSession().getAttribute("USER");
			jsonStringDataForTree = dataTreeService.getProductWithTFTree(user.getUserRoleMaster().getUserRoleId(),user.getUserId());
	        return jsonStringDataForTree;
		}
		
		@RequestMapping(value="administration.device.lab",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody String getDeviceLab(HttpServletRequest request) {
			log.debug("inside administration.device.lab");
			String jsonStringDataForTree = "";
			UserList user = (UserList)request.getSession().getAttribute("USER");
			jsonStringDataForTree = dataTreeService.getDeviceLabTree(user.getUserRoleMaster().getUserRoleId(),user.getUserId());
	        return jsonStringDataForTree;
		}
		
		@RequestMapping(value="product.feature.testcase.mapping.tree", method=RequestMethod.POST, produces="application/json")
	    public @ResponseBody JSONArray getFeatureTestCaseMappingTree(HttpServletRequest request, int productId) {
			log.debug("inside administration.workpackage.tree");
			JSONArray jsonDataForTree = null;
			UserList user = (UserList)request.getSession().getAttribute("USER");
			jsonDataForTree = dataMapTreeService.getFeatureTestCaseMappingTree(productId);
	        return jsonDataForTree;
		}
		
		@RequestMapping(value="child.features.list.of.parent", method=RequestMethod.POST, produces="application/json")
	    public @ResponseBody JSONArray getChildFeaturesOfParentFeature(HttpServletRequest request, int parentFeatureId, String node) {
			log.debug("inside child.features.list.of.parent");
			JSONArray jsonDataForTree = null;
			UserList user = (UserList)request.getSession().getAttribute("USER");
			jsonDataForTree = dataMapTreeService.getChildFeaturesOfParentFeature(parentFeatureId,node);
	        return jsonDataForTree;
		}
		
		@RequestMapping(value="product.decoulpingCategory.testcase.mapping.tree", method=RequestMethod.POST, produces="application/json")
	    public @ResponseBody JSONArray getDecouplingCategoryTestCaseMappingTree(HttpServletRequest request, int productId) {
			log.debug("inside administration.workpackage.tree");
			JSONArray jsonDataForTree = null;
			UserList user = (UserList)request.getSession().getAttribute("USER");
			jsonDataForTree = dataMapTreeService.getDecouplingTestCaseMappingTree(productId);
	        return jsonDataForTree;
		}
		
		@RequestMapping(value="child.decouplingcategories.list.of.parent", method=RequestMethod.POST, produces="application/json")
	    public @ResponseBody JSONArray getChildDecouplingCategoriesOfParentCategory(HttpServletRequest request, int parentDcId, String node) {
			log.debug("inside child.decouplingcategories.list.of.parent");
			JSONArray jsonDataForTree = null;
			UserList user = (UserList)request.getSession().getAttribute("USER");
			jsonDataForTree = dataMapTreeService.getChildDecouplingCategoriesOfParentCatregory(parentDcId, node);
	        return jsonDataForTree;
		}
		
		
		@RequestMapping(value="product.testsuite.testcase.mapping.tree", method=RequestMethod.POST, produces="application/json")
	    public @ResponseBody JSONArray getTestSuiteTestCaseMappingTree(HttpServletRequest request, int productId) {
			log.debug("inside child.decouplingcategories.list.of.parent");
			JSONArray jsonDataForTree = null;
			UserList user = (UserList)request.getSession().getAttribute("USER");
			jsonDataForTree = dataMapTreeService.getTestSuiteTestCaseMappingTree(productId);
	        return jsonDataForTree;
		}
		
		
		@RequestMapping(value="sub.testsuite.list.of.parent", method=RequestMethod.POST, produces="application/json")
	    public @ResponseBody JSONArray getChildTestSuiteTestCaseMappingTree(HttpServletRequest request, int parentId, String node) {
			log.debug("inside sub.testsuite.list.of.parent");
			JSONArray jsonDataForTree = null;
			UserList user = (UserList)request.getSession().getAttribute("USER");
			jsonDataForTree = dataMapTreeService.getChildTestSuiteTestCaseMappingTree(parentId,node);
	        return jsonDataForTree;
		}
		
		
		@RequestMapping(value="environment.group.category.tree", method=RequestMethod.POST, produces="application/json")
	    public @ResponseBody JSONArray getEnvironmentGroupTree(HttpServletRequest request) {
			log.debug("inside environment.group.category.tree");
			JSONArray jsonDataForTree = null;
			UserList user = (UserList)request.getSession().getAttribute("USER");
			jsonDataForTree = dataMapTreeService.getEnvironmentGroupTree();
	        return jsonDataForTree;
		}
		
		@RequestMapping(value="environment.category.of.parentnode", method=RequestMethod.POST, produces="application/json")
	    public @ResponseBody JSONArray getEnvironmentCategoryOfParentNodeTree(HttpServletRequest request, int parentId, String node) {
			log.debug("inside environment.category.of.parentnode");
			JSONArray jsonDataForTree = null;
			UserList user = (UserList)request.getSession().getAttribute("USER");
			jsonDataForTree = dataMapTreeService.getEnvironmentCategoryOfParentNodeTree(parentId, node);
	        return jsonDataForTree;
		}
		
		@RequestMapping(value="administration.product.activity.workpackage.tree", method=RequestMethod.POST, produces="application/json")
		 public @ResponseBody String getProductActivityWorkPackageTreeData(HttpServletRequest request) {
			log.debug("inside administration.product.environment.tree");
			String jsonStringDataForTree = "";
			UserList user = (UserList)request.getSession().getAttribute("USER");
			jsonStringDataForTree = dataTreeService.getProductActivityWorkPackageTree(user.getUserRoleMaster().getUserRoleId(),user.getUserId());
	        return jsonStringDataForTree;
		}
		
		@RequestMapping(value="administration.product.activity.tree", method=RequestMethod.POST, produces="application/json")
		 public @ResponseBody String getProductActivityTreeData(HttpServletRequest request,Integer actionType) {
			log.debug("inside administration.product.environment.tree");
			String jsonStringDataForTree = "";
			UserList user = (UserList)request.getSession().getAttribute("USER");
			String roleType = "";
			if(actionType != null && actionType == 0){
				roleType = IDPAConstants.ACTIVITY_ASSISGNEE;				
			}else if(actionType != null && actionType == 1){
				roleType = IDPAConstants.ACTIVITY_REVIEWVER;				
			}else if(actionType != null && actionType == 2){
				roleType = IDPAConstants.ACTIVITY_PQAREVIEWVER;				
			}
			jsonStringDataForTree = dataTreeService.getProductActivityTree(user.getUserRoleMaster().getUserRoleId(),user.getUserId(),roleType);
	        return jsonStringDataForTree;
		}
		
		
		@RequestMapping(value="administration.activity.workpackage.grouping.tree", method=RequestMethod.POST, produces="application/json")
	    public @ResponseBody JSONArray getActivityWorkPackageGrouping(HttpServletRequest request) {
			JSONArray jsonStringDataForTree = null;
			UserList user = (UserList)request.getSession().getAttribute("USER");
			int treeType = 0;
			int parentId=0;
			String  parentType="";
			if(request.getParameter("type") != null){
				treeType = Integer.parseInt(request.getParameter("type"));
			}
			if(request.getParameter("parentId") != null){
				parentId = Integer.parseInt(request.getParameter("parentId"));
			}
			if(request.getParameter("node") != null){
				parentType = request.getParameter("node");
			}
			jsonStringDataForTree = dataTreeService.getActivityWorkPackageTaskPlanTreeInJSON(user.getUserRoleMaster().getUserRoleId(),user.getUserId(), treeType,parentType,parentId);
	        return jsonStringDataForTree;
		}
		
		@RequestMapping(value="administration.activity.workpackage.demand.reservation.tree", method=RequestMethod.POST, produces="application/json")
	    public @ResponseBody JSONArray getWorkpackageListForDemandAndReservation(HttpServletRequest request) {
			JSONArray jsonStringDataForTree = null;
			UserList user = (UserList)request.getSession().getAttribute("USER");
			int treeType = 0;
			int parentId=0;
			String  parentType="";
			if(request.getParameter("type") != null){
				treeType = Integer.parseInt(request.getParameter("type"));
			}
			if(request.getParameter("parentId") != null){
				parentId = Integer.parseInt(request.getParameter("parentId"));
			}
			if(request.getParameter("node") != null){
				parentType = request.getParameter("node");
			}
			log.debug("treeType: "+treeType+"parentType=="+parentType+","+parentId);
			jsonStringDataForTree = dataTreeService.getProductAndWPListForDemandAndReservationInJson(user.getUserRoleMaster().getUserRoleId(),user.getUserId(), treeType,parentType,parentId);
	        return jsonStringDataForTree;
		}
		
		 @RequestMapping(value="customers.tree", method=RequestMethod.POST, produces="application/json")
		 public @ResponseBody String getCustomersTreeData(HttpServletRequest request) {
			log.debug("inside customers.tree");
			String jsonStringDataForTree = "";
			UserList user = (UserList)request.getSession().getAttribute("USER");
			jsonStringDataForTree = dataTreeService.getCustomersTreeData(user.getUserRoleMaster().getUserRoleId(),user.getUserId());
	        return jsonStringDataForTree;
		}
		
		 @RequestMapping(value="process.review.activity.tree", method=RequestMethod.POST, produces="application/json")
		 public @ResponseBody String getActivityTreeData(HttpServletRequest request, int type) {
			log.debug("inside process.review.activity.tree");
			String jsonStringDataForTree = "";
			UserList user = (UserList)request.getSession().getAttribute("USER");
			String roleType = IDPAConstants.ACTIVITY_ASSISGNEE;
			if(request.getParameter("type") != null && Integer.parseInt(request.getParameter("type")) == 0){
				roleType = IDPAConstants.ACTIVITY_ASSISGNEE;
				jsonStringDataForTree = dataTreeService.getActivityTree(user.getUserRoleMaster().getUserRoleId(),user.getUserId(), roleType);
			}else if(request.getParameter("type") != null && Integer.parseInt(request.getParameter("type")) == 1){
				roleType = IDPAConstants.ACTIVITY_REVIEWVER;
				jsonStringDataForTree = dataTreeService.getActivityTree(user.getUserRoleMaster().getUserRoleId(),user.getUserId(), roleType);
			}else if(request.getParameter("type") != null && Integer.parseInt(request.getParameter("type")) == 2){
				roleType = IDPAConstants.ACTIVITY_PQAREVIEWVER;
				jsonStringDataForTree = dataTreeService.getActivityTreeForPqaReviewer(user.getUserRoleMaster().getUserRoleId(),user.getUserId(), roleType);
			}
			log.debug("roleType: "+roleType);
			return jsonStringDataForTree;
		}
		 
		@RequestMapping(value="dimension.type.tree",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody String getDimensionTreeByType(@RequestParam Integer dimensionTypeId, HttpServletRequest request) {
			log.debug("inside dimension.type.tree");
			
			String jsonStringDataForTree = "";
			UserList user = (UserList)request.getSession().getAttribute("USER");
			jsonStringDataForTree = dataTreeService.getDimensionTreeByType(user.getUserRoleMaster().getUserRoleId(),user.getUserId(),"", dimensionTypeId);
			
			return jsonStringDataForTree;
		}
		
		
		
		@RequestMapping(value="product.plan.tree", method=RequestMethod.POST, produces="application/json")
	    public @ResponseBody JSONArray getProductPlanTree(HttpServletRequest request) {
			log.debug("inside product.plan.tree");
			JSONArray jsonDataForTree = null;
			UserList user = (UserList)request.getSession().getAttribute("USER");
			jsonDataForTree = dataMapTreeService.getProductPlanTree(user);
	        return jsonDataForTree;
		}
		
		
		@RequestMapping(value="load.child.of.product.plan.tree", method=RequestMethod.POST, produces="application/json")
	    public @ResponseBody JSONArray getProductPlanTree(HttpServletRequest request, int entityId, String node) {
			log.debug("inside load.child.of.product.plan.tree");
			JSONArray jsonDataForTree = null;
			UserList user = (UserList)request.getSession().getAttribute("USER");
			if(node.equalsIgnoreCase(IDPAConstants.ENTITY_PRODUCT)){
				jsonDataForTree = dataMapTreeService.loadProductVersions(user,entityId);
			}else if(node.equalsIgnoreCase(IDPAConstants.ENTITY_PRODUCT_VERSION)){
				jsonDataForTree = dataMapTreeService.loadProductBuilds(user,entityId);
			}else if(node.equalsIgnoreCase(IDPAConstants.ENTITY_PRODUCT_BUILD)){
				jsonDataForTree = dataMapTreeService.loadProductWorkPackages(user,entityId);
			}
	        return jsonDataForTree;
		}
		 

		@RequestMapping(value="my.activities.tree", method=RequestMethod.POST, produces="application/json")
	    public @ResponseBody JSONArray getMyActivitiesTree(HttpServletRequest request) {
			JSONArray jsonStringDataForTree = null;
			UserList user = (UserList)request.getSession().getAttribute("USER");
			int treeType = 0;
			int parentId=0;
			String  parentType="";
			int isActive=1;
			if(request.getParameter("type") != null){
				treeType = Integer.parseInt(request.getParameter("type"));
			}
			if(request.getParameter("parentId") != null){
				parentId = 0;
			}
			if(request.getParameter("node") != null){
				parentType = request.getParameter("node");
			}
			log.info("treeType: "+treeType+"parentType=="+parentType+","+parentId);
			jsonStringDataForTree = dataTreeService.getProductTestingTeamActivityTreeJSON(user.getUserRoleMaster().getUserRoleId(),user.getUserId(), treeType,parentType,parentId,isActive);
			log.info("treeType: "+jsonStringDataForTree);
	        return jsonStringDataForTree;
		}
		
		@RequestMapping(value="workflow.my.actions.product.tree", method=RequestMethod.POST, produces="application/json")
	    public @ResponseBody String getProductTreeForMyWorkflowActions(HttpServletRequest request) {
			log.debug("inside workflow.my.action.product.tree");
			String jsonDataForTree = null;
			UserList user = (UserList)request.getSession().getAttribute("USER");
			jsonDataForTree = dataTreeService.getProductTreeForMyWorkflowActions(user.getUserRoleMaster().getUserRoleId(), user.getUserId());
	        return jsonDataForTree;
		}
		
		@RequestMapping(value="pivot.report.view.advanced.tree", method=RequestMethod.POST, produces="application/json")
	    public @ResponseBody JSONArray getPivotReportViewAdvancedTree(HttpServletRequest request) {
			log.debug("inside pivot.report.view.advanced.tree");
			JSONArray jsonDataForTree = null;
			UserList user = (UserList)request.getSession().getAttribute("USER");

			int treeType = 0;
			Integer parentId=0;
			String  parentType="";
			if(request.getParameter("type") != null){
				treeType = Integer.parseInt(request.getParameter("type"));
			}
			if(request.getParameter("parentId") != null){
				parentId = Integer.parseInt(request.getParameter("parentId"));
			}
			if(request.getParameter("node") != null){
				parentType = request.getParameter("node");
			}
	
			jsonDataForTree = dataTreeService.getPivotReportViewAdvanceTree(parentType, parentId);
	        return jsonDataForTree;
		}
		
		@RequestMapping(value="user.skills.mapping.tree", method=RequestMethod.POST, produces="application/json")
	    public @ResponseBody JSONArray getUserSkillsMappingTree(HttpServletRequest request) {
			log.debug("inside user.skills.mapping.tree");
			JSONArray jsonDataForTree = null;
			UserList user = (UserList)request.getSession().getAttribute("USER");
			jsonDataForTree = dataMapTreeService.getUserSkillsMappingTree();
	        return jsonDataForTree;
		}
		
		@RequestMapping(value="child.skills.list.of.parent", method=RequestMethod.POST, produces="application/json")
	    public @ResponseBody JSONArray getChildSkillsOfParentSkill(HttpServletRequest request, int parentSkillId, String node) {
			log.debug("inside child.features.list.of.parent");
			JSONArray jsonDataForTree = null;
			UserList user = (UserList)request.getSession().getAttribute("USER");
			jsonDataForTree = dataMapTreeService.getChildSkillsOfParentSkill(parentSkillId,node);
	        return jsonDataForTree;
		}
		
		@RequestMapping(value="administration.product.activity.workpackage.tree.by.productId", method=RequestMethod.POST, produces="application/json")
		 public @ResponseBody String getProductActivityWorkPackageTreeDataByProductId(HttpServletRequest request,Integer productId) {
			log.debug("inside administration.product.activity.workpackage.tree.by.productId");
			String jsonStringDataForTree = "";
			jsonStringDataForTree = dataTreeService.getProductActivityWorkPackageTreeByProductId(productId);
	        return jsonStringDataForTree;
		}
		
		@RequestMapping(value="administration.product.hierarchy.tree.by.productId",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody String getCompleteProductHierarchyTreeData(HttpServletRequest request,Integer productId) {
			log.debug("inside administration.product.hierarchy.tree.by.productId");
			String jsonStringDataForTree = "";
			jsonStringDataForTree = dataTreeService.getProductHierarchyTreeByProductId(productId);
	        return jsonStringDataForTree;
		}
		
		
		@RequestMapping(value="administration.product.build.node.tree", method=RequestMethod.POST, produces="application/json")
	    public @ResponseBody String getProductBuildNodeTree(HttpServletRequest request) {
			log.debug("inside administration.product.tree");
			String jsonStringDataForTree = "";
			UserList user = (UserList)request.getSession().getAttribute("USER");
			jsonStringDataForTree = dataTreeService.getProductBuildNodeTree(user.getUserRoleMaster().getUserRoleId(),user.getUserId());
	        return jsonStringDataForTree;
		}
		
		@RequestMapping(value="get.latest.product.build.details.by.day.or.week", method=RequestMethod.POST, produces="application/json")
	    public  @ResponseBody JTableResponse getProductBuildNodeTree(HttpServletRequest request,String filter) {
			log.debug("inside get.latest.product.build.details.by.day.or.week");
			JTableResponse jTableResponse = null;
			try {
				List<JsonProductBuild> jsonProductBuildList= new ArrayList<JsonProductBuild>();
				UserList user = (UserList)request.getSession().getAttribute("USER");
				jsonProductBuildList = dataTreeService.getProductBuildDetails(user.getUserRoleMaster().getUserRoleId(),user.getUserId(),filter);
				jTableResponse = new JTableResponse("OK", jsonProductBuildList);
			}catch(Exception e) {
				jTableResponse = new JTableResponse("ERROR","Error in get latest build details");
				log.error("JSON ERROR", e);
			}
	        return jTableResponse;
		}
		
		@RequestMapping(value="get.completed.and.aborted.workpackage.details", method=RequestMethod.POST, produces="application/json")
	    public  @ResponseBody JTableResponse getUserRolebasedCompletedAndAbortedWorkpackges(HttpServletRequest request) {
			log.debug("inside get.completed.and.aborted.workpackage.details");
			JTableResponse jTableResponse = null;
			try {
				List<JsonWorkPackageTestCaseExecutionPlanForTester> jsonWPTCEP = new ArrayList<JsonWorkPackageTestCaseExecutionPlanForTester>();
				UserList user = (UserList)request.getSession().getAttribute("USER");
				jsonWPTCEP = dataTreeService.getUserRolebasedCompletedAndAbortedWorkpackges(user.getUserRoleMaster().getUserRoleId(),user.getUserId());
				jTableResponse = new JTableResponse("OK", jsonWPTCEP);
			}catch(Exception e) {
				jTableResponse = new JTableResponse("ERROR","Error in get workpackage details");
				log.error("JSON ERROR", e);
			}
	        return jTableResponse;
		}
		
}
