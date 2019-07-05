package com.hcl.atf.taf.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hcl.atf.taf.controller.utilities.ValidationUtility;
import com.hcl.atf.taf.model.DecouplingCategory;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.json.JsonDecouplingCategory;
import com.hcl.atf.taf.model.json.JsonDecouplingCategoryTestCase;
import com.hcl.atf.taf.model.json.JsonTestCaseList;
import com.hcl.atf.taf.model.json.jtable.JTableResponse;
import com.hcl.atf.taf.model.json.jtable.JTableSingleResponse;
import com.hcl.atf.taf.service.DecouplingCategoryService;
import com.hcl.atf.taf.service.HierarchicalEntitiesService;
import com.hcl.atf.taf.service.TestCaseService;
import com.hcl.atf.taf.service.UserTypeMasterNewService;

@Controller
public class DecouplingCategoryController {

	private static final Log log = LogFactory.getLog(DecouplingCategoryController.class);
	
	@Autowired(required=true)
    private SessionFactory sessionFactory;
	
	@Autowired
	private DecouplingCategoryService decouplingCategoryService;
	
	@Autowired
	private TestCaseService testCaseService;
	
	@Autowired
	private UserTypeMasterNewService userTypeMasterNewService;
	
	@Autowired
	private HierarchicalEntitiesService hierarchicalEntitiesService;
	
	
	@RequestMapping(value="product.decouplingcategory.testcase.update",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse updateDecouplingCategoriesTestCase(@ModelAttribute JsonDecouplingCategoryTestCase jsonTestCaseList, BindingResult result) {
		log.debug("inside product.decouplingcategory.testcase.update");
		
		JTableResponse jTableResponse = null;
		if(result.hasErrors()){
			jTableResponse = new JTableResponse("ERROR","Invalid Form!"); 
		}
		try {
			log.info("incoming data="+jsonTestCaseList);
			log.info("incoming data="+jsonTestCaseList.getTestCaseDescription());
			log.info("incoming data="+jsonTestCaseList.getTestCaseId());
			log.info("incoming data="+jsonTestCaseList.getDecouplingCategoryId());
			
			int testCaseId = jsonTestCaseList.getTestCaseId();
			int decouplingCategoryId = jsonTestCaseList.getDecouplingCategoryId();
			
			TestCaseList testCase = decouplingCategoryService.updateDecouplingCategoriesTestCases(testCaseId, decouplingCategoryId);
			
			List<JsonTestCaseList> jsonTestCaseListToUI = new ArrayList<JsonTestCaseList>();
			JsonTestCaseList jsonTestCaseSingleRecord = new JsonTestCaseList(testCase);
			jsonTestCaseListToUI.add(jsonTestCaseSingleRecord);
			
			jTableResponse = new JTableResponse("OK",jsonTestCaseListToUI,1);
			
	    } catch (Exception e) {
	        jTableResponse = new JTableResponse("ERROR","Unable to update the testcase decoupling category association!");
	        log.error("JSON ERROR", e);
	    }	        
	    return jTableResponse;
	}
	
	@RequestMapping(value="product.decouplingcategory.testcase.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listProductDecouplingCategoryTestCase(@RequestParam Integer productId) {
		log.info("inside product.decouplingcategory.testcase.list");
		JTableResponse jTableResponse;
			try {
				log.info("product Id : " + productId);
				if (productId == null || ("null").equals(productId)) {
					jTableResponse = new JTableResponse("OK", null,0);
					return jTableResponse;
				}
				
			List<TestCaseList> testCaseList=testCaseService.getTestCaseListByProductId(productId, null, null);
			
			if (testCaseList == null)
			{	
				log.info("testCaseList is null");
				jTableResponse = new JTableResponse("OK", null,0);
				return jTableResponse;
			}
			
			JsonDecouplingCategoryTestCase jsonDecouplingCategoryTestCase = null;
			List<JsonDecouplingCategoryTestCase> jsonDecouplingCategoryTestCaseList = new ArrayList<JsonDecouplingCategoryTestCase>();;
			for(TestCaseList testCase: testCaseList){
				jsonDecouplingCategoryTestCase = new JsonDecouplingCategoryTestCase(testCase);
				jsonDecouplingCategoryTestCaseList.add(jsonDecouplingCategoryTestCase);
				
			}	
			
			for(JsonDecouplingCategoryTestCase jsonDCT: jsonDecouplingCategoryTestCaseList){
				log.info("id --"+jsonDCT.getTestCaseId()+", -- Desc ---"+jsonDCT.getTestCaseDescription()+",---Code ---"+jsonDCT.getTestcaseCode()+"--, DCID --"+jsonDCT.getDecouplingCategoryId()+"--- DC Name  --"+jsonDCT.getDecouplingCategoryName());			
			}
            jTableResponse = new JTableResponse("OK", jsonDecouplingCategoryTestCaseList,testCaseService.getTotalRecordsOfTestCases());
            
            testCaseList = null;
			} catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }
        return jTableResponse;
    }
	
	@RequestMapping(value="administration.product.decouplingcategory.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listDecouplingCategory(@RequestParam int productMasterId) {	
		JTableResponse jTableResponse = new JTableResponse();			 
		try {
			List<DecouplingCategory> decouplingCategory = decouplingCategoryService.getDecouplingCategoryListByProductId(productMasterId);
			
			List<JsonDecouplingCategory> jsonDecouplingCategory = new ArrayList<JsonDecouplingCategory>();
			for(DecouplingCategory dcoupling: decouplingCategory){
				jsonDecouplingCategory.add(new JsonDecouplingCategory(dcoupling));
			}
			jTableResponse = new JTableResponse("OK", jsonDecouplingCategory,jsonDecouplingCategory.size()); 
			decouplingCategory = null;
			
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Listing DecouplingCategory!");
	            log.error("JSON ERROR Listing DecouplingCategory", e);
	        }		        
        return jTableResponse;
    }	
	
	@RequestMapping(value="administration.testcase.decouplingcategory.mappedlist",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listDecouplingCategoryMappedToTestCase(@RequestParam int testCaseId) {	
		JTableResponse jTableResponse;			 
		try {
			
			List<DecouplingCategory> decouplingCategoryFromDB = decouplingCategoryService.getDecouplingCategoriesMappedToTestCase(testCaseId);			
			
			List<JsonDecouplingCategory> jsonDecouplingCategory = new ArrayList<JsonDecouplingCategory>();
			for(DecouplingCategory decoupling: decouplingCategoryFromDB){
				jsonDecouplingCategory.add(new JsonDecouplingCategory(decoupling));
			}
			jTableResponse = new JTableResponse("OK", jsonDecouplingCategory,jsonDecouplingCategory.size()); 
			decouplingCategoryFromDB = null;
			
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Unable to listDecouplingCategoryMappedToTestCase !");
	            log.error("JSON ERROR listing DecouplingCategoryMappedToTestCase", e);
	        }		        
        return jTableResponse;
    }
	
	
	@RequestMapping(value="administration.product.decouplingcategory.add",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse addDecouplingCategory(@ModelAttribute JsonDecouplingCategory jsonDecouplingCategory, BindingResult result) {  
		log.debug("  ------------administration.product.decouplingcategory.add-----------");
		JTableSingleResponse jTableSingleResponse;
		if(result.hasErrors()){
			jTableSingleResponse =jTableSingleResponse = new JTableSingleResponse("ERROR","Invalid form!"); 
		}
		try {
			DecouplingCategory decouplingCategoryFromUI =  jsonDecouplingCategory.getDecouplingCategory();			
			
			ProductMaster productMasterFromUI = new ProductMaster();
			productMasterFromUI.setProductId(jsonDecouplingCategory.getProductMasterId());
						
			decouplingCategoryFromUI.setProduct(productMasterFromUI);			
			
			if(decouplingCategoryFromUI.getUserTypeMasterNew()!=null && decouplingCategoryFromUI.getUserTypeMasterNew().getUserTypeId()!=null && !decouplingCategoryFromUI.getUserTypeMasterNew().getUserTypeId().equals("0")){
				decouplingCategoryFromUI.setUserTypeMasterNew(userTypeMasterNewService.getByuserTypeId(decouplingCategoryFromUI.getUserTypeMasterNew().getUserTypeId()));				
			}
			if(decouplingCategoryFromUI.getUserTypeMasterNew()!=null && decouplingCategoryFromUI.getUserTypeMasterNew().getUserTypeLabel()!=null && !decouplingCategoryFromUI.getUserTypeMasterNew().getUserTypeLabel().equals("")){				
				decouplingCategoryFromUI.setUserTypeMasterNew(userTypeMasterNewService.getUserTypeMasterNewByuserTypeLabel(decouplingCategoryFromUI.getUserTypeMasterNew().getUserTypeLabel()));
			}			
			
			if(jsonDecouplingCategory.getParentCategoryId()!= null && !jsonDecouplingCategory.getParentCategoryId().equals("0")){
				decouplingCategoryFromUI.setParentCategory(decouplingCategoryService.getDecouplingCategoryById(jsonDecouplingCategory.getParentCategoryId()));
			}
		
			String errorMessage = ValidationUtility.validateForNewProductDecouplingCategoryAddition(decouplingCategoryFromUI, decouplingCategoryService);			
			if (errorMessage != null) {
				
				jTableSingleResponse = new JTableSingleResponse("ERROR",errorMessage);
				return jTableSingleResponse;
			}
			decouplingCategoryService.addProductDecouplingCategory(decouplingCategoryFromUI);			
	        jTableSingleResponse = new JTableSingleResponse("OK",new JsonDecouplingCategory(decouplingCategoryFromUI));
	    } catch (Exception e) {
	        jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding new Product Decoupling Category!");
	        log.error("JSON ERROR", e);
	    }	        
        return jTableSingleResponse;		
    }
	
	@RequestMapping(value="administration.product.decouplingcategory.update",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse updateDecouplingCategory(@ModelAttribute JsonDecouplingCategory jsonDecouplingCategory, BindingResult result) {
		JTableResponse jTableResponse = null;
		if(result.hasErrors()){
			jTableResponse = new JTableResponse("ERROR","Invalid Form!"); 
		}			
		try {	
			DecouplingCategory decouplingCategoryFromUI =  jsonDecouplingCategory.getDecouplingCategory();			
				
			ProductMaster productMasterFromUI = new ProductMaster();
			productMasterFromUI.setProductId(jsonDecouplingCategory.getProductMasterId());	
		
			decouplingCategoryFromUI.setProduct(productMasterFromUI);			
			
			if(decouplingCategoryFromUI.getUserTypeMasterNew()!=null && decouplingCategoryFromUI.getUserTypeMasterNew().getUserTypeId()!=null && !decouplingCategoryFromUI.getUserTypeMasterNew().getUserTypeId().equals("0")){
				decouplingCategoryFromUI.setUserTypeMasterNew(userTypeMasterNewService.getByuserTypeId(decouplingCategoryFromUI.getUserTypeMasterNew().getUserTypeId()));				
			}
			if(decouplingCategoryFromUI.getUserTypeMasterNew()!=null && decouplingCategoryFromUI.getUserTypeMasterNew().getUserTypeLabel()!=null && !decouplingCategoryFromUI.getUserTypeMasterNew().getUserTypeLabel().equals("")){
				decouplingCategoryFromUI.setUserTypeMasterNew(userTypeMasterNewService.getUserTypeMasterNewByuserTypeLabel(decouplingCategoryFromUI.getUserTypeMasterNew().getUserTypeLabel()));
			}
			
			if(decouplingCategoryFromUI.getParentCategory()!=null && decouplingCategoryFromUI.getParentCategory().getDecouplingCategoryId()!=null && !decouplingCategoryFromUI.getParentCategory().getDecouplingCategoryId().equals("0")){
				decouplingCategoryFromUI.setParentCategory(decouplingCategoryService.getDecouplingCategoryById(decouplingCategoryFromUI.getParentCategory().getDecouplingCategoryId()));				
			}
			if(decouplingCategoryFromUI.getParentCategory()!=null && decouplingCategoryFromUI.getParentCategory().getDecouplingCategoryName()!=null && !decouplingCategoryFromUI.getParentCategory().getDecouplingCategoryName().equals("")){
				decouplingCategoryFromUI.setParentCategory(decouplingCategoryService.getDecouplingCategoryByName(decouplingCategoryFromUI.getParentCategory().getDecouplingCategoryName()));
			}
			DecouplingCategory decouplingCategoryFromDB = decouplingCategoryService.getDecouplingCategoryById(jsonDecouplingCategory.getDecouplingCategoryId());		
			if(decouplingCategoryFromUI.getCreatedDate()!=null ){					
				decouplingCategoryFromUI.setCreatedDate(decouplingCategoryFromDB.getCreatedDate());
			}
			
			if (jsonDecouplingCategory.getModifiedField().equalsIgnoreCase("parentCategoryId")) {
				if (jsonDecouplingCategory.getOldFieldID() == null || jsonDecouplingCategory.getOldFieldID() == "" || jsonDecouplingCategory.getOldFieldID().equals("-1")) {
					DecouplingCategory rootDecouplingCategory = decouplingCategoryService.getRootDecouplingCategory();
					jsonDecouplingCategory.setOldFieldID(rootDecouplingCategory.getDecouplingCategoryId()+"");
					jsonDecouplingCategory.setOldFieldValue(rootDecouplingCategory.getDecouplingCategoryName());
				}
				if (jsonDecouplingCategory.getModifiedFieldID() == null || jsonDecouplingCategory.getModifiedFieldID() == "" || jsonDecouplingCategory.getModifiedFieldID().equals("-1")) {
					DecouplingCategory rootDecouplingCategory = decouplingCategoryService.getRootDecouplingCategory();
					jsonDecouplingCategory.setModifiedFieldID(rootDecouplingCategory.getDecouplingCategoryId()+"");
					jsonDecouplingCategory.setModifiedFieldValue(rootDecouplingCategory.getDecouplingCategoryName());
				}
				updateDecouplingCategory(decouplingCategoryFromUI, new Integer(jsonDecouplingCategory.getOldFieldID()), new Integer(jsonDecouplingCategory.getModifiedFieldID()));
			}
			else{
			decouplingCategoryService.updateProductDecouplingCategory(decouplingCategoryFromUI);
			}
			List<JsonDecouplingCategory> decouplingCategories = new ArrayList<JsonDecouplingCategory>();
			decouplingCategories.add(new JsonDecouplingCategory(decouplingCategoryFromUI));
			
			jTableResponse = new JTableResponse("OK",decouplingCategories,1);
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error updating Product Decoupling Category!");
	            log.error("JSON ERROR updating Decoupling Category", e);
	        }		
		return jTableResponse;      
    
    }	
	
	public void updateDecouplingCategory(DecouplingCategory decouplingCategory, Integer oldParentDCId, Integer newParentDCId) {
			
			DecouplingCategory oldDecouplingCategory = null;
			if (oldParentDCId == null || oldParentDCId < 0 ) {
				oldDecouplingCategory = decouplingCategoryService.getRootDecouplingCategory();
			} else {
				oldDecouplingCategory = decouplingCategoryService.getDecouplingCategoryById(oldParentDCId);
			}
			DecouplingCategory newDecouplingCategory = null;
			if (newParentDCId == null || newParentDCId < 0) {
				//This is becoming a root skill
				newDecouplingCategory = decouplingCategoryService.getRootDecouplingCategory();
			} else {
				newDecouplingCategory = decouplingCategoryService.getDecouplingCategoryById(newParentDCId);
			}
			updateDecouplingCategoryParent(decouplingCategory, oldDecouplingCategory, newDecouplingCategory);
		}
	
	public void updateDecouplingCategoryParent(DecouplingCategory decouplingCategory, DecouplingCategory oldDecouplingCategory, DecouplingCategory newDecouplingCategory) {
		
		log.debug("updating DecouplingCategory parent");
		String ENTITY_TABLE_NAME = "decoupling_category";

		try {
			
			log.info("Change DecouplingCategory parent for " + decouplingCategory.getDecouplingCategoryName() + " from " + oldDecouplingCategory.getDecouplingCategoryName() + " to " + newDecouplingCategory.getDecouplingCategoryName());
			//Refresh the DecouplingCategory from DB so that all index related fileds are available
			decouplingCategory = decouplingCategoryService.getDecouplingCategoryById(decouplingCategory.getDecouplingCategoryId());
			log.info("Updated DecouplingCategory Indexes : " + decouplingCategory.getLeftIndex() + " : " + decouplingCategory.getRightIndex());
			//Get the child nodes hierarchy in a list. The list will be ordered top to bottom, left to right
			List<DecouplingCategory> childDCHierarchy = decouplingCategoryService.listChildNodesInHierarchyinLayers(decouplingCategory);
			log.info("Updated Node : " + decouplingCategory.getDecouplingCategoryName());
			log.info("Child Nodes Count : " + childDCHierarchy.size());
			if (childDCHierarchy == null || childDCHierarchy.isEmpty()) {

				log.info("Updated DecouplingCategory Indexes Before Delete : " + decouplingCategory.getLeftIndex() + " : " + decouplingCategory.getRightIndex());
				log.info("Parent DecouplingCategory Indexes Before Delete : " + newDecouplingCategory.getLeftIndex() + " : " + newDecouplingCategory.getRightIndex());
				hierarchicalEntitiesService.updateHierarchyIndexForDelete(ENTITY_TABLE_NAME, decouplingCategory.getLeftIndex(), decouplingCategory.getRightIndex());
				
				//Refresh the Parent so that its indices are refreshed
				newDecouplingCategory = decouplingCategoryService.getDecouplingCategoryById(newDecouplingCategory.getDecouplingCategoryId());
				log.info("Parent DecouplingCategory Indexes After Delete : " + newDecouplingCategory.getLeftIndex() + " : " + newDecouplingCategory.getRightIndex());
				log.info("Updated DecouplingCategory Indexes After Delete : " + decouplingCategory.getLeftIndex() + " : " + decouplingCategory.getRightIndex());
				//Add the updated skill to its new parent hierarchy
				hierarchicalEntitiesService.updateHierarchyIndexForNew(ENTITY_TABLE_NAME, newDecouplingCategory.getRightIndex());
				newDecouplingCategory = decouplingCategoryService.getDecouplingCategoryById(newDecouplingCategory.getDecouplingCategoryId());

				log.info("Parent DecouplingCategory Indexes After Add : " + newDecouplingCategory.getLeftIndex() + " : " + newDecouplingCategory.getRightIndex());
				log.info("Updated DecouplingCategory Indexes After Add : " + decouplingCategory.getLeftIndex() + " : " + decouplingCategory.getRightIndex());
				
				//Update the skill's new parent in database				
				decouplingCategory.setParentCategory(newDecouplingCategory);
				decouplingCategory.setLeftIndex(newDecouplingCategory.getRightIndex() - 2);
				decouplingCategory.setRightIndex(newDecouplingCategory.getRightIndex() - 1);
				decouplingCategoryService.updateProductDecouplingCategory(decouplingCategory);
				decouplingCategory = decouplingCategoryService.getDecouplingCategoryById(decouplingCategory.getDecouplingCategoryId());
				log.info("Updated DecouplingCategory Indexes After Add : " + decouplingCategory.getLeftIndex() + " : " + decouplingCategory.getRightIndex());
				return;
			}
			
			
			for (DecouplingCategory decouplingCategory2 : childDCHierarchy) {
				log.info("DecouplingCategory Hierarchy Layered sorted : " + decouplingCategory2.getLeftIndex() + " : " + decouplingCategory2.getDecouplingCategoryName() + " : " + decouplingCategory2.getRightIndex());
			}
			
			//Remove all the child nodes, starting from leaf nodes and then progress upwards
			int childNodesCount = childDCHierarchy.size();
			for (int i = childNodesCount-1; i >= 0; i--) {
				
				DecouplingCategory childDC = childDCHierarchy.get(i);
				childDC = decouplingCategoryService.getDecouplingCategoryById(childDC.getDecouplingCategoryId());
				oldDecouplingCategory = decouplingCategoryService.getDecouplingCategoryById(oldDecouplingCategory.getDecouplingCategoryId());
				log.info("Old Parent before Removing child Node : " + oldDecouplingCategory.getLeftIndex() + " : " + oldDecouplingCategory.getDecouplingCategoryName() + " : " + oldDecouplingCategory.getRightIndex());
				hierarchicalEntitiesService.updateHierarchyIndexForDelete(ENTITY_TABLE_NAME, childDC.getLeftIndex(), childDC.getRightIndex());
				log.info("Removed child Node : " + childDC.getLeftIndex() + " : " + childDC.getDecouplingCategoryName() + " : " + childDC.getRightIndex());
				oldDecouplingCategory = decouplingCategoryService.getDecouplingCategoryById(oldDecouplingCategory.getDecouplingCategoryId());
				log.info("Old Parent after Removing child Node : " + oldDecouplingCategory.getLeftIndex() + " : " + oldDecouplingCategory.getDecouplingCategoryName() + " : " + oldDecouplingCategory.getRightIndex());
			}
			//Remove the updated node. With this, the hierarchy now has no indexes for the updated skill and its n level child nodes 
			decouplingCategory = decouplingCategoryService.getDecouplingCategoryById(decouplingCategory.getDecouplingCategoryId());
			hierarchicalEntitiesService.updateHierarchyIndexForDelete(ENTITY_TABLE_NAME, decouplingCategory.getLeftIndex(), decouplingCategory.getRightIndex());
			log.info("Removed updated Node : " + decouplingCategory.getLeftIndex() + " : " + decouplingCategory.getDecouplingCategoryName() + " : " + decouplingCategory.getRightIndex());
			oldDecouplingCategory = decouplingCategoryService.getDecouplingCategoryById(oldDecouplingCategory.getDecouplingCategoryId());
			log.info("Old Parent after updated Node : " + oldDecouplingCategory.getLeftIndex() + " : " + oldDecouplingCategory.getDecouplingCategoryName() + " : " + oldDecouplingCategory.getRightIndex());
			log.info("Finished removing Hierarchy from old parent");
			
			//Update the skill's new parent in database

			newDecouplingCategory = decouplingCategoryService.getDecouplingCategoryById(newDecouplingCategory.getDecouplingCategoryId());
			log.info("New Parent before adding updated Node : " + newDecouplingCategory.getLeftIndex() + " : " + newDecouplingCategory.getDecouplingCategoryName() + " : " + newDecouplingCategory.getRightIndex());
			hierarchicalEntitiesService.updateHierarchyIndexForNew(ENTITY_TABLE_NAME, newDecouplingCategory.getRightIndex());
			newDecouplingCategory = decouplingCategoryService.getDecouplingCategoryById(newDecouplingCategory.getDecouplingCategoryId());
			decouplingCategory.setParentCategory(newDecouplingCategory);
			decouplingCategory.setLeftIndex(newDecouplingCategory.getRightIndex() - 2);
			decouplingCategory.setRightIndex(newDecouplingCategory.getRightIndex() - 1);
			decouplingCategoryService.updateProductDecouplingCategory(decouplingCategory);
			
			decouplingCategory = decouplingCategoryService.getDecouplingCategoryById(decouplingCategory.getDecouplingCategoryId());
			log.info("Updated DecouplingCategory Indexes After Add : " + decouplingCategory.getLeftIndex() + " : " + decouplingCategory.getRightIndex());
			log.info("Added updated Node : " + decouplingCategory.getLeftIndex() + " : " + decouplingCategory.getDecouplingCategoryName() + " : " + decouplingCategory.getRightIndex());
			newDecouplingCategory = decouplingCategoryService.getDecouplingCategoryById(newDecouplingCategory.getDecouplingCategoryId());
			log.info("New Parent after adding updated Node : " + newDecouplingCategory.getLeftIndex() + " : " + newDecouplingCategory.getDecouplingCategoryName() + " : " + newDecouplingCategory.getRightIndex());
			oldDecouplingCategory = decouplingCategoryService.getDecouplingCategoryById(oldDecouplingCategory.getDecouplingCategoryId());
			log.info("Old Parent after adding updated Node : " + oldDecouplingCategory.getLeftIndex() + " : " + oldDecouplingCategory.getDecouplingCategoryName() + " : " + oldDecouplingCategory.getRightIndex());
			
			//Add the child nodes back to the hierarchy. Their parents have not changed
			for (int i = 0; i < childNodesCount; i++) {
				
				DecouplingCategory childDC = childDCHierarchy.get(i);
				//Reload parent from DB, so that it contains the updated indices
				DecouplingCategory tempParentDC = decouplingCategoryService.getDecouplingCategoryById(childDC.getParentCategory().getDecouplingCategoryId());
				log.info("Child node parent : " + tempParentDC.getLeftIndex() + " : " + tempParentDC.getDecouplingCategoryName() + " : " + tempParentDC.getRightIndex());
				hierarchicalEntitiesService.updateHierarchyIndexForNew(ENTITY_TABLE_NAME, tempParentDC.getRightIndex());
				tempParentDC = decouplingCategoryService.getDecouplingCategoryById(tempParentDC.getDecouplingCategoryId());
				childDC.setLeftIndex(tempParentDC.getRightIndex() - 2);
				childDC.setRightIndex(tempParentDC.getRightIndex() - 1);
				decouplingCategoryService.updateProductDecouplingCategory(childDC);
				log.info("Added child Node : " + childDC.getLeftIndex() + " : " + childDC.getDecouplingCategoryName() + " : " + childDC.getRightIndex());
				newDecouplingCategory = decouplingCategoryService.getDecouplingCategoryById(newDecouplingCategory.getDecouplingCategoryId());
				log.info("New Parent after adding child Node : " + newDecouplingCategory.getLeftIndex() + " : " + newDecouplingCategory.getDecouplingCategoryName() + " : " + newDecouplingCategory.getRightIndex());
				oldDecouplingCategory = decouplingCategoryService.getDecouplingCategoryById(oldDecouplingCategory.getDecouplingCategoryId());
				log.info("Old Parent after adding child Node : " + oldDecouplingCategory.getLeftIndex() + " : " + oldDecouplingCategory.getDecouplingCategoryName() + " : " + oldDecouplingCategory.getRightIndex());
			}
			log.debug("Parent DecouplingCategory Updated Successfully");
		} catch (RuntimeException re) {
			log.error("update DecouplingCategory failed", re);
		}		
	}
	
	@RequestMapping(value="administration.product.decouplingcategory.update.parent",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse updateFeatureParentInHierarchy(HttpServletRequest request,@RequestParam Integer decouplingCategoryId, @RequestParam Integer oldParentCategoryId, @RequestParam Integer newParentCategoryId) {

		log.debug("Updating DecouplingCategory");
		JTableResponse jTableResponse;
		try {
			DecouplingCategory decouplingCategory = decouplingCategoryService.getDecouplingCategoryById(decouplingCategoryId);
			updateDecouplingCategory(decouplingCategory, oldParentCategoryId, newParentCategoryId);
			jTableResponse = new JTableResponse("OK", "Updated DecouplingCategory parent");
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error updating DecouplingCategory parent!");
			log.error("JSON ERROR updating DecouplingCategory parent", e);
		}
		return jTableResponse;
	}
	
}
