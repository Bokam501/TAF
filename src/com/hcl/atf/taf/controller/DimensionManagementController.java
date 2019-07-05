package com.hcl.atf.taf.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import com.hcl.atf.taf.controller.utilities.ValidationUtility;
import com.hcl.atf.taf.model.DimensionMaster;
import com.hcl.atf.taf.model.DimensionProduct;
import com.hcl.atf.taf.model.DimensionType;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.json.JsonDimensionMaster;
import com.hcl.atf.taf.model.json.JsonDimensionProduct;
import com.hcl.atf.taf.model.json.JsonDimensionType;
import com.hcl.atf.taf.model.json.jtable.JTableResponse;
import com.hcl.atf.taf.model.json.jtable.JTableResponseOptions;
import com.hcl.atf.taf.model.json.jtable.JTableSingleResponse;
import com.hcl.atf.taf.service.DimensionService;
import com.hcl.atf.taf.service.UserListService;

@Controller
public class DimensionManagementController {

	private static final Log log = LogFactory.getLog(DimensionManagementController.class);
	
	@Autowired
	private DimensionService dimensionService;
	
	@Autowired
	private UserListService userListService;
	
	@RequestMapping(value="dimension.list.by.status",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse getDimensionList(@RequestParam Integer status, @RequestParam Integer dimensionTypeId, @RequestParam Integer jtStartIndex, @RequestParam Integer jtPageSize) {
		log.debug("inside dimension.list.by.status");
		JTableResponse jTableResponse;
		 
		try {
			List<DimensionMaster> dimensionList = dimensionService.getDimensionList(status, jtStartIndex,jtPageSize, false, dimensionTypeId);
			List<JsonDimensionMaster> jsonDimensionMasters = new ArrayList<JsonDimensionMaster>();
			int totalRecordsAvailable = dimensionService.getTotalRecordsForDimensionPagination(status, DimensionMaster.class, false, dimensionTypeId);
			
			for(DimensionMaster dimensionMaster: dimensionList){
				jsonDimensionMasters.add(new JsonDimensionMaster(dimensionMaster));
				
			}
	        jTableResponse = new JTableResponse("OK", jsonDimensionMasters, totalRecordsAvailable);
	           
        } catch (Exception e) {
            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
            log.error("JSON ERROR", e);
        }
	        
        return jTableResponse;
    }
	
	@RequestMapping(value="dimension.list.options",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listDimensionOptions(@RequestParam Integer dimensionTypeId) {  
		log.debug("  ------------dimension.list.options-----------");
		JTableResponseOptions jTableResponseOptions = null;
		
		try {
			List<DimensionMaster> dimensionList = new ArrayList<DimensionMaster>();
			List<DimensionMaster> dimensionRetrived = dimensionService.getDimensionList(1, null, null, true, dimensionTypeId); 
			if(dimensionTypeId == 1){
				dimensionList = dimensionRetrived;
			}else{
				if(dimensionRetrived != null && dimensionRetrived.size() > 0){
					dimensionList.add(dimensionRetrived.get(0));
				}
			}
			
			List<JsonDimensionMaster> jsonDimensionMasters = new ArrayList<JsonDimensionMaster>();
			
			for(DimensionMaster dimensionMaster : dimensionList){
				jsonDimensionMasters.add(new JsonDimensionMaster(dimensionMaster));
			}
			
			jTableResponseOptions = new JTableResponseOptions("OK", jsonDimensionMasters,true);     
	    } catch (Exception e) {
			jTableResponseOptions = new JTableResponseOptions("ERROR","Error listing dimension for options!");
	        log.error("JSON ERROR", e);
	    }	        
        return jTableResponseOptions;		
    }
	
	@RequestMapping(value="dimension.type.list.options",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listDimensionTypeOptions() {  
		log.debug("  ------------dimension.type.list.options-----------");
		JTableResponseOptions jTableResponseOptions = null;
		
		try {
			List<DimensionType> dimensionTypes = dimensionService.getDimensionTypeList();
			List<JsonDimensionType> jsonDimensionTypes=new ArrayList<JsonDimensionType>();
			
			for(DimensionType dimensionType : dimensionTypes){
				jsonDimensionTypes.add(new JsonDimensionType(dimensionType));
			}
			
			jTableResponseOptions = new JTableResponseOptions("OK", jsonDimensionTypes,true);     
	    } catch (Exception e) {
			jTableResponseOptions = new JTableResponseOptions("ERROR","Error listing dimension for options!");
	        log.error("JSON ERROR", e);
	    }	        
        return jTableResponseOptions;		
    }
	
	@RequestMapping(value="dimension.add",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse addDimension(@ModelAttribute JsonDimensionMaster jsonDimensionMaster, BindingResult result, HttpServletRequest request) {  
		log.debug("  ------------dimension.add-----------");
		JTableSingleResponse jTableSingleResponse;
		if(result.hasErrors()){
			jTableSingleResponse = new JTableSingleResponse("ERROR","Invalid form!");
			return jTableSingleResponse;
		}
		try {
			String errorMessage = ValidationUtility.validateForDimensionAdditionOrUpdation(jsonDimensionMaster.getName().trim(), dimensionService, jsonDimensionMaster.getDimensionId(), jsonDimensionMaster.getTypeId());			
			if (errorMessage != null) {
				jTableSingleResponse = new JTableSingleResponse("ERROR",errorMessage);
				return jTableSingleResponse;
			}
			
			DimensionMaster dimensionMaster = jsonDimensionMaster.getDimensionMaster();
			dimensionMaster.setStatus(1);
			UserList userList = (UserList)request.getSession().getAttribute("USER");
			dimensionMaster.setOwner(userList);
			dimensionMaster.setModifiedBy(userList);
			
			dimensionService.addDimension(dimensionMaster);			
	        jTableSingleResponse = new JTableSingleResponse("OK",new JsonDimensionMaster(dimensionMaster));
	    } catch (Exception e) {
	        jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding new Dimension!");
	        log.error("JSON ERROR", e);
	    }	        
        return jTableSingleResponse;		
    }
	
	@RequestMapping(value="dimension.update",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse updateDimension(@ModelAttribute JsonDimensionMaster jsonDimensionMaster, BindingResult result, HttpServletRequest request) {  
		log.debug("  ------------dimension.update-----------");
		JTableSingleResponse jTableSingleResponse;
		if(result.hasErrors()){
			jTableSingleResponse = new JTableSingleResponse("ERROR","Invalid form!");
			return jTableSingleResponse;
		}
		try {
			String errorMessage = ValidationUtility.validateForDimensionAdditionOrUpdation(jsonDimensionMaster.getName().trim(), dimensionService, jsonDimensionMaster.getDimensionId(), jsonDimensionMaster.getTypeId());			
			if (errorMessage != null) {
				jTableSingleResponse = new JTableSingleResponse("ERROR",errorMessage);
				return jTableSingleResponse;
			}
			DimensionMaster dimensionMaster = jsonDimensionMaster.getDimensionMaster();
			UserList userList = userListService.getUserListById(jsonDimensionMaster.getOwnerId());
			dimensionMaster.setOwner(userList);
			userList = (UserList)request.getSession().getAttribute("USER");
			dimensionMaster.setModifiedBy(userList);
			dimensionMaster.setModifiedDate(new Date());
			
			dimensionService.updateDimension(dimensionMaster);			
	        jTableSingleResponse = new JTableSingleResponse("OK",new JsonDimensionMaster(dimensionMaster));
	    } catch (Exception e) {
	        jTableSingleResponse = new JTableSingleResponse("ERROR","Error updating Dimension!");
	        log.error("JSON ERROR", e);
	    }	        
        return jTableSingleResponse;		
    }
	
	@RequestMapping(value="dimension.product.by.status",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse getDimensionProductsList(@RequestParam Integer dimensionId, @RequestParam Integer status, @RequestParam Integer jtStartIndex, @RequestParam Integer jtPageSize) {
		log.debug("inside dimension.product.by.status");
		JTableResponse jTableResponse;
		 
		try {
			List<DimensionProduct> dimensionProducts = dimensionService.getDimensionProducts(dimensionId, status, jtStartIndex, jtPageSize);
			List<JsonDimensionProduct> jsonDimensionProducts = new ArrayList<JsonDimensionProduct>();
			int totalRecordsAvailable = dimensionService.getTotalRecordsForDimensionProductPagination(dimensionId, status, DimensionProduct.class);
			
			for(DimensionProduct dimensionProduct : dimensionProducts){
				jsonDimensionProducts.add(new JsonDimensionProduct(dimensionProduct));
			}
	        jTableResponse = new JTableResponse("OK", jsonDimensionProducts, totalRecordsAvailable);
	           
        } catch (Exception e) {
            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
            log.error("JSON ERROR", e);
        }
	        
        return jTableResponse;
    }
	
	@RequestMapping(value="dimension.product.update",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse updateProductWithDimension(@ModelAttribute JsonDimensionProduct jsonDimensionProduct, BindingResult result, HttpServletRequest request) {  
		log.debug("  ------------dimension.product.update-----------");
		JTableSingleResponse jTableSingleResponse;
		
		if(result.hasErrors()){
			jTableSingleResponse = new JTableSingleResponse("ERROR","Invalid form!");
			return jTableSingleResponse;
		}
		try {
			String errorMessage = "";
			DimensionProduct dimensionProduct = jsonDimensionProduct.getDimensionProduct();
			
			if(dimensionProduct.getStartDate().after(dimensionProduct.getEndDate())){
				errorMessage = "End date should greaterthan start date";
			}
			
			if (errorMessage != null && !errorMessage.isEmpty()) {
				jTableSingleResponse = new JTableSingleResponse("ERROR",errorMessage);
				return jTableSingleResponse;
			}
			
			UserList userList = (UserList)request.getSession().getAttribute("USER");
			dimensionProduct.setModifiedBy(userList);
			dimensionProduct.setModifiedDate(new Date());
						
			dimensionService.updateDimensionProduct(dimensionProduct);			
	        jTableSingleResponse = new JTableSingleResponse("OK",new JsonDimensionProduct(dimensionProduct));
	    } catch (Exception e) {
	        jTableSingleResponse = new JTableSingleResponse("ERROR","Error update product for dimension!");
	        log.error("JSON ERROR", e);
	    }	        
        return jTableSingleResponse;		
    }
	
	@RequestMapping(value="dimension.available.for.product",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listDimensionsToAddWithProduct(@RequestParam Integer productId, @RequestParam Integer dimensionTypeId, @RequestParam Integer jtStartIndex, @RequestParam Integer jtPageSize) {  
		log.debug("  ------------dimension.available.for.product-----------");
		JTableResponse jTableResponse;
		try {
			List<Object[]> dimensionMasters = dimensionService.getDimensionsToAddWithProduct(productId, false, dimensionTypeId, 1, jtStartIndex, jtPageSize);
			ArrayList<HashMap<String, Object>> availableDimensions = new ArrayList<HashMap<String, Object>>();
			for (Object[] row : dimensionMasters) {
				HashMap<String, Object> competencyDetail =new HashMap<String, Object>();
				competencyDetail.put(IDPAConstants.ITEM_ID, (Integer)row[0]);
				competencyDetail.put(IDPAConstants.ITEM_NAME, (String)row[1]);					
				availableDimensions.add(competencyDetail);					
			}				
			jTableResponse = new JTableResponse("OK", availableDimensions, availableDimensions.size());
		} catch (Exception e) {
	       	jTableResponse = new JTableResponse("ERROR","Error listing un mapped dimensions!");
	       	log.error("JSON ERROR", e);
	    }	        
		return jTableResponse;
    }
	
	@RequestMapping(value="dimension.available.for.product.count",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse listDimensionsToAddWithProductCount(@RequestParam Integer productId, @RequestParam Integer dimensionTypeId) {  
		log.debug("  ------------dimension.available.for.product.count-----------");
		JTableSingleResponse jTableSingleResponse;
		
		Integer unMappedDimensionCount = 0;		
		HashMap<String, Integer> unMappedDimensionsCountMap =new HashMap<String, Integer>();
		try {
			unMappedDimensionCount = dimensionService.getDimensionsToAddWithProductCount(productId, false, dimensionTypeId, 1);
			unMappedDimensionsCountMap.put("unMappedTCCount", unMappedDimensionCount);						
			jTableSingleResponse = new JTableSingleResponse("OK",unMappedDimensionsCountMap);
        } catch (Exception e) {
            jTableSingleResponse = new JTableSingleResponse("ERROR","Unable to fetch unmapped dimensions count to product!");
            log.error("JSON ERROR listING Dimensions To Add With ProductCount", e);	 
        }
	        
        return jTableSingleResponse;
    }
	
	@RequestMapping(value="dimension.list.for.product",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listDimensionOfProduct(@RequestParam Integer productId, @RequestParam Integer dimensionTypeId, @RequestParam Integer jtStartIndex, @RequestParam Integer jtPageSize) {
		log.debug("inside dimension.list.for.product");
		JTableResponse jTableResponse;
		 
		try {
			List<Object[]> dimensionMasters = dimensionService.getDimensionsOfProduct(productId, dimensionTypeId, 1, jtStartIndex, jtPageSize);
			int totalRecordsAvailable = dimensionService.getTotalRecordsForDimensionOfProductPagination(productId, 1, dimensionTypeId, DimensionProduct.class);
			ArrayList<HashMap<String, Object>> availableDimensions = new ArrayList<HashMap<String, Object>>();
			for (Object[] row : dimensionMasters) {
				HashMap<String, Object> competencyDetail =new HashMap<String, Object>();
				competencyDetail.put(IDPAConstants.ITEM_ID, (Integer)row[0]);
				competencyDetail.put(IDPAConstants.ITEM_NAME, (String)row[1]);					
				availableDimensions.add(competencyDetail);					
			}				
			jTableResponse = new JTableResponse("OK", availableDimensions, totalRecordsAvailable);
	           
        } catch (Exception e) {
            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
            log.error("JSON ERROR", e);
        }
	        
        return jTableResponse;
	}
	
	@RequestMapping(value="dimension.for.product.mapping",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse addDimensionForProduct(@RequestParam Integer productId, @RequestParam Integer dimensionId, @RequestParam String maporunmap, HttpServletRequest request) {  
		log.debug("  ------------dimension.for.product.mapping-----------");
		JTableSingleResponse jTableSingleResponse;
		
		try {
			if(maporunmap == null || maporunmap.isEmpty() || maporunmap.equalsIgnoreCase("map")){
				String errorMessage = ValidationUtility.validateForDimensionProductAdditionOrUpdation(productId, dimensionId, dimensionService, null);			
				if (errorMessage != null) {
					jTableSingleResponse = new JTableSingleResponse("ERROR",errorMessage);
					return jTableSingleResponse;
				}
			}
			
			DimensionProduct dimensionProduct = new DimensionProduct();
			ProductMaster productMaster = new ProductMaster();
			productMaster.setProductId(productId);
			dimensionProduct.setProductId(productMaster);
			
			DimensionMaster dimensionMaster = new DimensionMaster();
			dimensionMaster.setDimensionId(dimensionId);
			dimensionProduct.setDimensionId(dimensionMaster);
			
			dimensionProduct.setStatus(1);
			UserList userList = (UserList)request.getSession().getAttribute("USER");
			dimensionProduct.setMappedBy(userList);
			dimensionProduct.setMappedDate(new Date());
			dimensionProduct.setModifiedBy(userList);
			dimensionProduct.setModifiedDate(new Date());
			dimensionProduct.setStartDate(new Date());
			dimensionProduct.setEndDate(new Date());
			
			dimensionService.addDimensionForProduct(dimensionProduct, maporunmap);			
	        jTableSingleResponse = new JTableSingleResponse("OK",new JsonDimensionProduct(dimensionProduct));
	    } catch (Exception e) {
	        jTableSingleResponse = new JTableSingleResponse("ERROR","Error while adding dimension to product!");
	        log.error("JSON ERROR", e);
	    }	        
        return jTableSingleResponse;		
    }
	
}
