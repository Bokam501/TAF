package com.hcl.atf.taf.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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

import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.constants.TAFConstants;
import com.hcl.atf.taf.controller.utilities.ValidationUtility;
import com.hcl.atf.taf.model.Customer;
import com.hcl.atf.taf.model.DataExtractorScheduleMaster;
import com.hcl.atf.taf.model.ExtractorTypeMaster;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.TestFactory;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.json.JsonCustomer;
import com.hcl.atf.taf.model.json.JsonDataExtractorScheduleMaster;
import com.hcl.atf.taf.model.json.JsonExtractorTypeMaster;
import com.hcl.atf.taf.model.json.JsonProductMaster;
import com.hcl.atf.taf.model.json.JsonTestFactory;
import com.hcl.atf.taf.model.json.jtable.JTableResponse;
import com.hcl.atf.taf.model.json.jtable.JTableResponseOptions;
import com.hcl.atf.taf.model.json.jtable.JTableSingleResponse;
import com.hcl.atf.taf.service.CommonService;
import com.hcl.atf.taf.service.CustomerService;
import com.hcl.atf.taf.service.DataSourceExtractorService;
import com.hcl.atf.taf.service.DimensionService;
import com.hcl.atf.taf.service.ProductListService;
import com.hcl.atf.taf.service.TestFactoryManagementService;


@Controller
public class DataSourceExtractorController {

	private static final Log log = LogFactory.getLog(DataSourceExtractorController.class);
		
	@Value("#{ilcmProps['DATA_EXTRACTOR_BASE_LOCATION']}")
    private String DATA_EXTRACTOR_BASE_LOCATION;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private ProductListService productListService;
	
	@Autowired
	private DimensionService dimensionService;

	@Autowired
	private CommonService commonService;
	
	@Autowired
	private TestFactoryManagementService testFactoryManagementService;
	
	@Autowired
	private DataSourceExtractorService dataSourceExtractorService;
		
	@RequestMapping(value="data.source.extractor.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse getExtractorsList(@RequestParam Integer status, @RequestParam Integer jtStartIndex, @RequestParam Integer jtPageSize) {
		log.debug("inside data.source.extractor.list");
		JTableResponse jTableResponse;
		 
		try {
			List<DataExtractorScheduleMaster> dataExtractorScheduleMasters = dataSourceExtractorService.getDataExtractorScheduleMastersList(status, jtStartIndex, jtPageSize);
			List<JsonDataExtractorScheduleMaster> jsonDataExtractorScheduleMasters = new ArrayList<JsonDataExtractorScheduleMaster>();
			int totalRecordsAvailable = dataSourceExtractorService.getTotalRecordsForDataExtractorSchedulePagination(status, DataExtractorScheduleMaster.class);
			List<Object[]> attachmentCountDetails = commonService.getAttachmentCountOfEntity(IDPAConstants.ENTITY_DATA_EXTRACTOR_ID);
			for(DataExtractorScheduleMaster dataExtractorScheduleMaster : dataExtractorScheduleMasters){
				JsonDataExtractorScheduleMaster jsonDataExtractorScheduleMaster = new JsonDataExtractorScheduleMaster(dataExtractorScheduleMaster);
				jsonDataExtractorScheduleMaster.setAttachmentCount(commonService.getCountFromAttachmentCountDetails(attachmentCountDetails, jsonDataExtractorScheduleMaster.getId()));
				jsonDataExtractorScheduleMasters.add(jsonDataExtractorScheduleMaster);
			}
	        jTableResponse = new JTableResponse("OK", jsonDataExtractorScheduleMasters, totalRecordsAvailable);
			
        } catch (Exception e) {
            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
            log.error("JSON ERROR", e);
        }
	        
        return jTableResponse;
    }
	
	@RequestMapping(value="data.source.extractor.add",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse addDataExtarctor(@ModelAttribute JsonDataExtractorScheduleMaster jsonDataExtractorScheduleMaster, BindingResult result, HttpServletRequest request) {  
		log.debug("  ------------data.source.extractor.add-----------");
		JTableSingleResponse jTableSingleResponse;
		if(result.hasErrors()){
			jTableSingleResponse = new JTableSingleResponse("ERROR","Invalid form!");
			return jTableSingleResponse;
		}
		try {
			DataExtractorScheduleMaster dataExtractorScheduleMaster = jsonDataExtractorScheduleMaster.getDataExtractorScheduleMaster();
			dataExtractorScheduleMaster.setStatus(1);
			
			if(dataExtractorScheduleMaster.getJobName() == null || dataExtractorScheduleMaster.getJobName().trim().isEmpty()){
				String jobName = dataExtractorScheduleMaster.getCustomer().getCustomerId()+"_"+dataExtractorScheduleMaster.getEngagement().getTestFactoryId()+"_";
				if(dataExtractorScheduleMaster.getCompetency().getDimensionId() != null && dataExtractorScheduleMaster.getCompetency().getDimensionId() > 1){
					jobName += dataExtractorScheduleMaster.getCompetency().getDimensionId()+"_";
				}
				if(dataExtractorScheduleMaster.getProduct() != null && dataExtractorScheduleMaster.getProduct().getProductId() != null && dataExtractorScheduleMaster.getProduct().getProductId() > 0){
					jobName += dataExtractorScheduleMaster.getProduct().getProductId()+"_";
				}
				jobName += dataExtractorScheduleMaster.getExtractorType().getId();
				jobName = jobName.replaceAll(" ", "_");
				dataExtractorScheduleMaster.setJobName(jobName);
			}
			
			if(dataExtractorScheduleMaster.getGroupName() == null || dataExtractorScheduleMaster.getGroupName().trim().isEmpty()){
				String groupName = dataExtractorScheduleMaster.getCustomer().getCustomerId()+"_"+dataExtractorScheduleMaster.getEngagement().getTestFactoryId()+"_";
				if(dataExtractorScheduleMaster.getCompetency().getDimensionId() != null && dataExtractorScheduleMaster.getCompetency().getDimensionId() > 1){
					groupName += dataExtractorScheduleMaster.getCompetency().getDimensionId()+"_";
				}
				if(dataExtractorScheduleMaster.getProduct() != null && dataExtractorScheduleMaster.getProduct().getProductId() != null && dataExtractorScheduleMaster.getProduct().getProductId() > 0){
					groupName += dataExtractorScheduleMaster.getProduct().getProductId()+"_";
				}
				groupName = groupName.replaceAll(" ", "_");
				dataExtractorScheduleMaster.setGroupName(groupName);
			}
			
			String errorMessage = ValidationUtility.validateForDataExtarctorAdditionOrUpdation(dataExtractorScheduleMaster.getJobName().trim(), dataSourceExtractorService, dataExtractorScheduleMaster.getId());			
			if (errorMessage != null) {
				jTableSingleResponse = new JTableSingleResponse("ERROR",errorMessage);
				return jTableSingleResponse;
			}
			
			if(dataExtractorScheduleMaster.getFileLocation() == null || dataExtractorScheduleMaster.getFileLocation().isEmpty()){
				dataExtractorScheduleMaster.setExtractorType(dataSourceExtractorService.getExtarctorTypeById(dataExtractorScheduleMaster.getExtractorType().getId()));
				dataExtractorScheduleMaster.setEngagement(testFactoryManagementService.getTestFactoryById(dataExtractorScheduleMaster.getEngagement().getTestFactoryId()));
				
				String fileLocation = DATA_EXTRACTOR_BASE_LOCATION+"\\"+dataExtractorScheduleMaster.getEngagement().getTestFactoryName()+"\\";
				if(dataExtractorScheduleMaster.getCompetency().getDimensionId() != null && dataExtractorScheduleMaster.getCompetency().getDimensionId() > 1){
					dataExtractorScheduleMaster.setCompetency(dimensionService.getDimensionById(dataExtractorScheduleMaster.getCompetency().getDimensionId()));
					fileLocation += dataExtractorScheduleMaster.getCompetency().getName()+"\\";
				}
				if(dataExtractorScheduleMaster.getProduct() != null && dataExtractorScheduleMaster.getProduct().getProductId() != null && dataExtractorScheduleMaster.getProduct().getProductId() > 0){
					dataExtractorScheduleMaster.setProduct(productListService.getProductDetailsById(dataExtractorScheduleMaster.getProduct().getProductId()));
					fileLocation += dataExtractorScheduleMaster.getProduct().getProductName()+"\\";
				}
				if(dataExtractorScheduleMaster.getExtractorType() != null) {
					fileLocation += dataExtractorScheduleMaster.getExtractorType().getExtarctorName();
				}
				dataExtractorScheduleMaster.setFileLocation(fileLocation);
			}
			
			dataSourceExtractorService.addDataExtractorScheduleMaster(dataExtractorScheduleMaster);			
	        File fileDirectory = new File(dataExtractorScheduleMaster.getFileLocation());
	        if(!fileDirectory.exists() || !fileDirectory.isDirectory()){
	        	fileDirectory.mkdirs();
	        }
	        
			jTableSingleResponse = new JTableSingleResponse("OK",new JsonDataExtractorScheduleMaster(dataExtractorScheduleMaster));
	    } catch (Exception e) {
	        jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding new data extractor!");
	        log.error("JSON ERROR", e);
	    }	        
        return jTableSingleResponse;		
    }
	
	@RequestMapping(value="data.source.extractor.update",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse updateDataExtarctor(@ModelAttribute JsonDataExtractorScheduleMaster jsonDataExtractorScheduleMaster, BindingResult result, HttpServletRequest request) {  
		log.debug("  ------------data.source.extractor.update-----------");
		JTableSingleResponse jTableSingleResponse;
		if(result.hasErrors()){
			jTableSingleResponse = new JTableSingleResponse("ERROR","Invalid form!");
			return jTableSingleResponse;
		}
		try {
			DataExtractorScheduleMaster dataExtractorScheduleMaster = jsonDataExtractorScheduleMaster.getDataExtractorScheduleMaster();
			
			String errorMessage = ValidationUtility.validateForDataExtarctorAdditionOrUpdation(dataExtractorScheduleMaster.getJobName().trim(), dataSourceExtractorService, dataExtractorScheduleMaster.getId());			
			if (errorMessage != null) {
				jTableSingleResponse = new JTableSingleResponse("ERROR",errorMessage);
				return jTableSingleResponse;
			}
			dataExtractorScheduleMaster.setUpdatedDate(new Date());
			if(dataExtractorScheduleMaster.getFileLocation() == null || dataExtractorScheduleMaster.getFileLocation().isEmpty()){
				dataExtractorScheduleMaster.setExtractorType(dataSourceExtractorService.getExtarctorTypeById(dataExtractorScheduleMaster.getExtractorType().getId()));
				dataExtractorScheduleMaster.setEngagement(testFactoryManagementService.getTestFactoryById(dataExtractorScheduleMaster.getEngagement().getTestFactoryId()));
				
				String fileLocation = DATA_EXTRACTOR_BASE_LOCATION+"\\"+dataExtractorScheduleMaster.getEngagement().getTestFactoryName()+"\\";
				if(dataExtractorScheduleMaster.getCompetency().getDimensionId() != null && dataExtractorScheduleMaster.getCompetency().getDimensionId() > 1){
					dataExtractorScheduleMaster.setCompetency(dimensionService.getDimensionById(dataExtractorScheduleMaster.getCompetency().getDimensionId()));
					fileLocation += dataExtractorScheduleMaster.getCompetency().getName()+"\\";
				}
				if(dataExtractorScheduleMaster.getProduct() != null && dataExtractorScheduleMaster.getProduct().getProductId() != null && dataExtractorScheduleMaster.getProduct().getProductId() > 1){
					dataExtractorScheduleMaster.setProduct(productListService.getProductDetailsById(dataExtractorScheduleMaster.getProduct().getProductId()));
					fileLocation += dataExtractorScheduleMaster.getProduct().getProductName()+"\\";
				}
				fileLocation += dataExtractorScheduleMaster.getExtractorType().getExtarctorName();
				dataExtractorScheduleMaster.setFileLocation(fileLocation);
			}
			dataSourceExtractorService.updateDataExtractorScheduleMasster(dataExtractorScheduleMaster);		
			File fileDirectory = new File(dataExtractorScheduleMaster.getFileLocation());
	        if(!fileDirectory.exists()){
	        	fileDirectory.mkdirs();
	        }
			jTableSingleResponse = new JTableSingleResponse("OK",new JsonDataExtractorScheduleMaster(dataExtractorScheduleMaster));
	    } catch (Exception e) {
	        jTableSingleResponse = new JTableSingleResponse("ERROR","Error updating Data extractor!");
	        log.error("JSON ERROR", e);
	    }	        
        return jTableSingleResponse;		
    }
	
	@RequestMapping(value="extractor.type.list.options",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listExtractorTypeOptions() {  
		log.debug("  ------------extractor.type.list.options-----------");
		JTableResponseOptions jTableResponseOptions = null;
		
		try {
			List<ExtractorTypeMaster> extartorTypeList = dataSourceExtractorService.getExtarctorTypeList();
			List<JsonExtractorTypeMaster> jsonExtarctorTypeList=new ArrayList<JsonExtractorTypeMaster>();
			
			for(ExtractorTypeMaster extractorTypeMaster : extartorTypeList){
				jsonExtarctorTypeList.add(new JsonExtractorTypeMaster(extractorTypeMaster));
			}
			
			jTableResponseOptions = new JTableResponseOptions("OK", jsonExtarctorTypeList,true);     
	    } catch (Exception e) {
			jTableResponseOptions = new JTableResponseOptions("ERROR","Error listing extarctor type for options!");
	        log.error("JSON ERROR", e);
	    }	        
        return jTableResponseOptions;		
    }
	
	
	@RequestMapping(value="data.source.extractor.customer.by.user.option.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions getDataSourceExtractorCustomerByUserOptions(HttpServletRequest request) {
		log.debug("inside data.source.extractor.customer.by.user.option.list");
		JTableResponseOptions jTableResponseOptions;
		 
		try {	
			UserList user = (UserList)request.getSession().getAttribute("USER");
			List<Customer> customerList = customerService.getCustomerByUserProducts(user.getUserId(), user.getUserRoleMaster().getUserRoleId(), TAFConstants.ENTITY_STATUS_ACTIVE);
			List<JsonCustomer> jsonCustomerList=new ArrayList<JsonCustomer>();
			if(customerList == null || customerList.size() == 0){
				JsonCustomer jsonCustomer = new JsonCustomer();
				jsonCustomer.setCustomerId(0);
				jsonCustomer.setCustomerName("--");
				jsonCustomerList.add(jsonCustomer);
				jTableResponseOptions = new JTableResponseOptions("OK", jsonCustomerList, true);
				return jTableResponseOptions;
			}else{
				for(Customer cust: customerList){
					jsonCustomerList.add(new JsonCustomer(cust));
				}	
			}
			jTableResponseOptions = new JTableResponseOptions("OK", jsonCustomerList, true);  
            customerList = null;
			
        } catch (Exception e) {
        	jTableResponseOptions = new JTableResponseOptions("ERROR","Unable to show Customers!");
            log.error("Error in getDataSourceExtractorCustomerByUserOptions - ", e);
        }
        return jTableResponseOptions;
    }
	
	@RequestMapping(value="data.source.extractor.engagement.by.user.customer.option.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions getDataSourceExtractorEngagementByUserAndCustomerOptions(@RequestParam Integer customerId, HttpServletRequest request) {
		log.debug("inside data.source.extractor.engagement.by.user.customer.option.list");
		JTableResponseOptions jTableResponseOptions;
		 
		try {
			UserList user = (UserList)request.getSession().getAttribute("USER");
			List<TestFactory> testFactories = testFactoryManagementService.getEngagementByUserAndCustomerProduct(user.getUserId(), user.getUserRoleMaster().getUserRoleId(), customerId, TAFConstants.ENTITY_STATUS_ACTIVE);
			List<JsonTestFactory> jsonTestFactories = new ArrayList<JsonTestFactory>();
			if(testFactories == null || testFactories.size() == 0){
				JsonTestFactory jsonTestFactory = new JsonTestFactory();
				jsonTestFactory.setTestFactoryId(0);
				jsonTestFactory.setTestFactoryName("--");
				jsonTestFactories.add(jsonTestFactory);
				jTableResponseOptions = new JTableResponseOptions("OK", jsonTestFactories, true);
				return jTableResponseOptions;
			}else{
				for(TestFactory testFactory : testFactories){
					jsonTestFactories.add(new JsonTestFactory(testFactory));
				}	
			}
			jTableResponseOptions = new JTableResponseOptions("OK", jsonTestFactories, true);  
            testFactories = null;
			
        } catch (Exception e) {
        	jTableResponseOptions = new JTableResponseOptions("ERROR","Unable to show engagement!");
            log.error("Error in getDataSourceExtractorEngagementByCustomerOptions - ", e);
        }
        return jTableResponseOptions;
    }
	
	@RequestMapping(value="data.source.extractor.product.by.user.customer.engagement.option.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions getDataSourceExtractorProductByUserCustomerAndEngagementOptions(@RequestParam Integer customerId, @RequestParam Integer engagementId, HttpServletRequest request) {
		log.debug("inside data.source.extractor.product.by.user.customer.engagement.option.list");
		JTableResponseOptions jTableResponseOptions;
		 
		try {
			UserList user = (UserList)request.getSession().getAttribute("USER");
			List<ProductMaster> productMasters = productListService.getProductByUserCustomerAndEngagement(user.getUserId(), user.getUserRoleMaster().getUserRoleId(), customerId, engagementId, TAFConstants.ENTITY_STATUS_ACTIVE);
			List<JsonProductMaster> jsonProductMasters = new ArrayList<JsonProductMaster>();
			
			JsonProductMaster jsonProductMaster = new JsonProductMaster();
			jsonProductMaster.setProductId(0);
			jsonProductMaster.setProductName("--");
			jsonProductMasters.add(jsonProductMaster);
			
			if(productMasters != null && productMasters.size() > 0){
				for(ProductMaster productMaster : productMasters){
					jsonProductMasters.add(new JsonProductMaster(productMaster));
				}	
			}
			jTableResponseOptions = new JTableResponseOptions("OK", jsonProductMasters, true);  
            productMasters = null;
			
        } catch (Exception e) {
        	jTableResponseOptions = new JTableResponseOptions("ERROR","Unable to show products!");
            log.error("Error in getDataSourceExtractorProductByUserCustomerAndEngagementOptions - ", e);
        }
        return jTableResponseOptions;
    }
}
