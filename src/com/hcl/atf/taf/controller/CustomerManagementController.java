package com.hcl.atf.taf.controller;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
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
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.constants.TAFConstants;
import com.hcl.atf.taf.controller.utilities.CommonUtility;
import com.hcl.atf.taf.controller.utilities.ValidationUtility;
import com.hcl.atf.taf.dao.ProductMasterDAO;
import com.hcl.atf.taf.model.Customer;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.UserCustomerAccount;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.json.JsonCustomer;
import com.hcl.atf.taf.model.json.JsonProductMaster;
import com.hcl.atf.taf.model.json.JsonUserCustomerAccount;
import com.hcl.atf.taf.model.json.jtable.JTableResponse;
import com.hcl.atf.taf.model.json.jtable.JTableSingleResponse;
import com.hcl.atf.taf.service.CustomerService;
import com.hcl.atf.taf.service.EventsService;
import com.hcl.atf.taf.service.ProductListService;
import com.hcl.atf.taf.service.UserListService;
import com.hcl.atf.taf.service.WorkPackageService;
@Controller
public class CustomerManagementController {
	private static final Log log = LogFactory.getLog(CustomerManagementController.class);
	
	@Autowired
	private CustomerService customerService;
	@Autowired
	private ProductListService productListService;
	@Autowired
	private UserListService userListService;
	
	@Autowired
	private WorkPackageService workPackageService;
	@Autowired
	private EventsService eventsService;
	@Autowired
	private ProductMasterDAO productMasterDAO;
	
	@RequestMapping(value="administration.customer.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listCustomer() {
		log.debug("inside administration.customer.list");
		JTableResponse jTableResponse;
		 
		try {			
			List<Customer> customerList=customerService.listCustomer(TAFConstants.ENTITY_STATUS_ACTIVE, null, null);
			
			List<JsonCustomer> jsonCustomer=new ArrayList<JsonCustomer>();
			for(Customer cust: customerList){
				
				jsonCustomer.add(new JsonCustomer(cust));
			}
            jTableResponse = new JTableResponse("OK", jsonCustomer,jsonCustomer.size());     
            customerList = null;

			
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Unable to show Customers!");
	            log.error("JSON ERROR", e);
	        }
	        
        return jTableResponse;
    }
	
	@RequestMapping(value="administration.customer.list.bystatus",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listCustomerByStatus(@RequestParam int status, @RequestParam Integer jtStartIndex, @RequestParam Integer jtPageSize) {
		log.info("inside administration.customer.list.bystatus");
		JTableResponse jTableResponse;		 
		try {			
			List<Customer> customerList=customerService.listCustomer(status, jtStartIndex, jtPageSize);
			List<Customer> customerListforPagination=customerService.listCustomer(status, null, null);
			
			List<JsonCustomer> jsonCustomer=new ArrayList<JsonCustomer>();
			for(Customer cust: customerList){
				jsonCustomer.add(new JsonCustomer(cust));
			}
            jTableResponse = new JTableResponse("OK", jsonCustomer,customerListforPagination.size());     
            customerList = null;

			
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Unable to show Customers!");
	            log.error("JSON ERROR", e);
	        }
	        
        return jTableResponse;
    }
	@RequestMapping(value="administration.customer.add",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse addCustomer(HttpServletRequest req,@ModelAttribute JsonCustomer jsonCustomer, BindingResult result) {
		log.info("Inside Customer Add ");
		JTableSingleResponse jTableSingleResponse;
		JTableResponse jTableResponse;
		if(result.hasErrors()){
			jTableSingleResponse  = new JTableSingleResponse("ERROR","Invalid form!"); 
		}		
		try {   log.info("Stauts:-->"+jsonCustomer.getStatus());		
				Customer customer= jsonCustomer.getCustomer();	
				
				String errorMessage = ValidationUtility.validateForNewCustomerAddition(customer.getCustomerName().trim(), customerService);
				if (errorMessage!= null) {
					
					jTableSingleResponse = new JTableSingleResponse("ERROR",errorMessage);
					return jTableSingleResponse;
				}
				
				customerService.addCustomer(customer);
				if(customer != null && customer.getCustomerId() != null){
					UserList user=(UserList)req.getSession().getAttribute("USER");
					eventsService.addNewEntityEvent(IDPAConstants.ENTITY_CUSTOMER, customer.getCustomerId(), customer.getCustomerName(), user);
				}
					jTableSingleResponse = new JTableSingleResponse("OK",new JsonCustomer(customer));		
	        } catch (Exception e) {
	            jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding Customer record!");
	            log.error("JSON ERROR adding Customer ", e);
	        }
	        
        return jTableSingleResponse;
    }
	
	@RequestMapping(value="administration.customer.update",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse updateCustomer(HttpServletRequest req, @ModelAttribute JsonCustomer jsonCustomer, BindingResult result) {
		JTableResponse jTableResponse = null;
		String remarks = "";
		if(result.hasErrors()){
			jTableResponse = new JTableResponse("ERROR","Invalid Form!"); 
		}	
		Customer customerFromUI = null;
		if(result.hasErrors()){
			jTableResponse = new JTableResponse("ERROR","Invalid Form!"); 
		}
		
		try {
			int updatename=jsonCustomer.getCustomerName().length();
			int refid=jsonCustomer.getCustomerRefId().length();
			log.info("updatename length===>"+updatename);
			if((updatename<3)||(refid<3))
			{
				jTableResponse = new JTableResponse("ERROR","Please enter minimum 3 letters!"); 
				return jTableResponse;
			}
			
			customerFromUI= jsonCustomer.getCustomer();
					
			customerService.update(customerFromUI);	
			if(customerFromUI != null && customerFromUI.getCustomerId() != null){
				UserList user=(UserList)req.getSession().getAttribute("USER");
				eventsService.addEntityChangedEvent(IDPAConstants.ENTITY_CUSTOMER, customerFromUI.getCustomerId(), customerFromUI.getCustomerName(),
						jsonCustomer.getModifiedField(), jsonCustomer.getModifiedFieldTitle(),
						jsonCustomer.getOldFieldValue(), jsonCustomer.getModifiedFieldValue(), user, remarks);
			}			
			 jTableResponse = new JTableResponse("OK");  
		    } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Unable to update the Customer!");
	            log.error("JSON ERROR", e);
	        }	        
	        
        return jTableResponse;

    }	

	

	@RequestMapping(value="resourceManagement.user.customerAccount.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listUserCustomerAccount(@RequestParam Integer userId) {
		log.debug("inside customerManagement.user.customerAccount.list");
		JTableResponse jTableResponse;
		try {			
			List<UserCustomerAccount> userCustomerAccountList=customerService.listUserCustomerAccount(userId);
			
			List<JsonUserCustomerAccount> jsonUserCustomerAccountList=new ArrayList<JsonUserCustomerAccount>();
			for(UserCustomerAccount userCustomerAccount: userCustomerAccountList){
				
				jsonUserCustomerAccountList.add(new JsonUserCustomerAccount(userCustomerAccount));
			}
            jTableResponse = new JTableResponse("OK", jsonUserCustomerAccountList,jsonUserCustomerAccountList.size());     
            userCustomerAccountList = null;
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Unable to show User Customer Accounts!");
	            log.error("JSON ERROR", e);
	        }
	        
        return jTableResponse;
    }
	
	@RequestMapping(value="resourceManagement.user.customerAccount.add",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse addUserCustomerAccount(@ModelAttribute JsonUserCustomerAccount jsonUserCustomerAccount, BindingResult result) {
		log.info("Inside resourceManagement.user.customerAccount.add");
		JTableSingleResponse jTableSingleResponse;
		JTableResponse jTableResponse;
		if(result.hasErrors()){
			jTableSingleResponse  = new JTableSingleResponse("ERROR","Invalid form!"); 
		}		
		try {   		
				UserCustomerAccount userCustomerAccount= jsonUserCustomerAccount.getUserCustomerAccount();	
				boolean isActiveUserExistsByName = userListService.isCustomerUserExitsByLoginId(jsonUserCustomerAccount.getUserCustomerName());
				boolean isCustomerUserExistsByName = userListService.isCustomerUserExitsByLoginId(jsonUserCustomerAccount.getUserCustomerName());
				if(!isActiveUserExistsByName){
					if(!isCustomerUserExistsByName){
						Customer customer=new Customer();
						customer.setCustomerId(jsonUserCustomerAccount.getCustomerId());
						userCustomerAccount.setCustomer(customer);
						customerService.addUserCustomerAccount(userCustomerAccount);
						jTableSingleResponse = new JTableSingleResponse("OK",new JsonUserCustomerAccount(userCustomerAccount));		
					}else{
						jTableSingleResponse = new JTableSingleResponse("ERROR","Customer User already exists by name: '"+jsonUserCustomerAccount.getUserCustomerName()+"'");
						return jTableSingleResponse;
					}
				}else{
					jTableSingleResponse = new JTableSingleResponse("ERROR","User already exists by name: '"+jsonUserCustomerAccount.getUserCustomerName()+"'");
					return jTableSingleResponse;
				}
				
	        } catch (Exception e) {
	            jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding User Customer Account record!");
	            log.error("JSON ERROR adding User Customer Account record", e);
	        }
	        
        return jTableSingleResponse;
    }

	
	@RequestMapping(value="resourceManagement.user.customerAccount.update",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse updateUserCustomerAccount(@ModelAttribute JsonUserCustomerAccount jsonUserCustomerAccount, BindingResult result) {			
		JTableResponse jTableResponse = null;
		UserCustomerAccount userCustomerAccount = null;
		try {
			userCustomerAccount = jsonUserCustomerAccount.getUserCustomerAccount();
				if(userCustomerAccount != null){
					boolean isCustomerUserExistsByName = userListService.isCustomerUserExitsByLoginId(userCustomerAccount.getUserCustomerName());
						if(!isCustomerUserExistsByName){
							customerService.updateUserCustomerAccount(userCustomerAccount);
							ArrayList<JsonUserCustomerAccount> jsonUserCustomerAccounts = new ArrayList<JsonUserCustomerAccount>();
							JsonUserCustomerAccount jsonUserCustomerAccount2 = new JsonUserCustomerAccount(userCustomerAccount);
							jsonUserCustomerAccounts.add(jsonUserCustomerAccount2);
							jTableResponse = new JTableResponse("OK", jsonUserCustomerAccounts);	
						}else{
							jTableResponse = new JTableResponse("ERROR","Customer User already exists by name: '"+jsonUserCustomerAccount.getUserCustomerName()+"'");
							return jTableResponse;
						}
				}else{
					jTableResponse = new JTableResponse("ERROR", "Error in updating UserCustomerAccount");
				}
	        } catch (Exception e) {
	        	jTableResponse = new JTableResponse("ERROR","Error updating record!");
	            log.error("JSON ERROR", e);
	        }
        return jTableResponse;
    }
	

	
	@RequestMapping(value="testFactory.customer.products.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listTestFactoryCustomer(@RequestParam Integer testFactoryId,@RequestParam Integer productId ) {
		log.debug("inside testFactory.customer.products.list");
		JTableResponse jTableResponse;
		List<ProductMaster> productMasterList=null;
		try {		
			if(productId!=0){
				productMasterList =  productMasterDAO.getCustmersbyProductId(testFactoryId, productId);
			}else{
			productMasterList=productListService.listProductsByTestFactoryId(testFactoryId);
			}
			List<JsonCustomer> jsonCustomerList=new ArrayList<JsonCustomer>();
			List<Customer> customerList=new ArrayList<Customer>();
			for(ProductMaster productMasters: productMasterList){
		Customer customer=productMasters.getCustomer();
		if(customer!=null){
			if(!customerList.contains(customer)){
				customerList.add(customer);
			}
		}
		
			}
			if(customerList!=null){
				for(Customer customer:customerList){
					jsonCustomerList.add(new JsonCustomer(customer));
				}
			}
			if(jsonCustomerList!=null){
            jTableResponse = new JTableResponse("OK", jsonCustomerList,jsonCustomerList.size()); 
			}else{
				  jTableResponse = new JTableResponse("OK", jsonCustomerList,0); 
			}
            productMasterList = null;

	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Unable to show Customers!");
	            log.error("JSON ERROR", e);
	        }
	        
        return jTableResponse;
    }
	
	
	@RequestMapping(value="testFactory.customer.products.update",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse updateTestFactoryCustomer(@ModelAttribute JsonProductMaster jsonProductMaster, BindingResult result) {
		JTableResponse jTableResponse = null;
		Customer customerFromUI = null;
		if(result.hasErrors()){
			jTableResponse = new JTableResponse("ERROR","Invalid Form!"); 
		}
		
		try {
			customerFromUI= jsonProductMaster.getProductMaster().getCustomer();
			customerFromUI.setDescription(jsonProductMaster.getCustomerDescription());
			customerFromUI.setCustomerName(jsonProductMaster.getCustomerName());
			customerFromUI.setCustomerRefId(jsonProductMaster.getCustomerRefId());
			customerFromUI.setStatus(jsonProductMaster.getCustomerStatus());
					
			customerService.update(customerFromUI);		
			
		    } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Unable to update the Customer!");
	            log.error("JSON ERROR", e);
	        }	        
	        
        return jTableResponse;

    }	
	
	@RequestMapping(value="customer.list.by.customerid",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listCustomerById(@RequestParam Integer customerId) {
		log.debug("inside customer.list.by.customerid");
		JTableResponse jTableResponse;		 
		try {			
			Customer customer=customerService.getCustomerId(customerId);
			List<Customer> custList = new ArrayList<Customer>();
			custList.add(customer);
			
            jTableResponse = new JTableResponse("OK", custList);     
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Unable to show Customers!");
	            log.error("JSON ERROR", e);
	        }
	        
        return jTableResponse;
    }
	
	
	@RequestMapping(value="customer.logo.update",method=RequestMethod.POST ,produces="text/plain")
	public @ResponseBody JTableResponse customerLogoUpdate(HttpServletRequest request, @RequestParam Integer customerId) {
		JTableResponse jTableResponse = null;
		try {
			log.info(" customerId  : "+customerId);
			Customer customer=customerService.getCustomerId(customerId);
			String catalinaHome = System.getProperty("catalina.home");
			String serverFolderPath = catalinaHome + "\\webapps\\Logo\\";

			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			CommonsMultipartFile multipartFile = null;
			String fileName = "";
			Iterator<String> iterator = multipartRequest.getFileNames();
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				multipartFile = (CommonsMultipartFile) multipartRequest.getFile(key);
				fileName = multipartFile.getOriginalFilename();
				long size=multipartFile.getSize();
				String Path = multipartFile.getName();
				fileName=customer.getCustomerId()+fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
				String extn[] = {".jpg",".gif",".png"}; 
				if(size > 122880){ // modified to reduct low size
					jTableResponse = new JTableResponse("ERROR","Logo size should not exceed 120Kb!");
					return jTableResponse;
				}else if (!Arrays.asList(extn).contains(fileName.substring(fileName.lastIndexOf(".")).toLowerCase())) {
					jTableResponse = new JTableResponse("ERROR","Please upload an Image file!");
					return jTableResponse;
				}
				InputStream content = multipartFile.getInputStream();
				File filePath = new File(serverFolderPath);
				File image = new File(serverFolderPath + "\\"+ fileName);
				if (!filePath.isDirectory()) {
					FileUtils.forceMkdir(filePath);
				}
				CommonUtility.copyInputStreamToFile(content, image);
				
				customer.setImageURI(fileName);
				customerService.update(customer);
				jTableResponse = new JTableResponse("OK", "Customer data modified successfully");
			}
				
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error Updating Customer data !");
	        log.error("JSON ERROR", e);	            
	    }
        return jTableResponse;
	}
}
