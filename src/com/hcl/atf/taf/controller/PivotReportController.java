package com.hcl.atf.taf.controller;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hcl.atf.taf.model.MongoCollection;
import com.hcl.atf.taf.model.PivotRestTemplate;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.dto.PivotRestTemplateDTO;
import com.hcl.atf.taf.model.json.JsonMongoCollections;
import com.hcl.atf.taf.model.json.JsonPivotRestMongoCollections;
import com.hcl.atf.taf.model.json.JsonPivotRestTemplate;
import com.hcl.atf.taf.model.json.jtable.JTableResponse;
import com.hcl.atf.taf.model.json.jtable.JTableResponseOptions;
import com.hcl.atf.taf.mongodb.service.MongoDBService;
import com.hcl.atf.taf.service.ReportService;

@Controller
public class PivotReportController {

	private static final Log log = LogFactory.getLog(PivotReportController.class);

	@Autowired
	private MongoDBService mongoDBService;
	@Autowired
	private ReportService reportService;
	
	@RequestMapping(value="mongo.collections.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions loadAllMongoCollections(HttpServletRequest request) {
		log.debug("inside mongo.collections.list");
		JTableResponseOptions jTableResponseOptions=null;
		 
		try {
			List<MongoCollection> mongoCollection = reportService.getMongoCollectionList();
			List<JsonMongoCollections> jsonMongoCollections=new ArrayList<JsonMongoCollections>();
			if (mongoCollection != null){
				for(MongoCollection dvm: mongoCollection){
					jsonMongoCollections.add(new JsonMongoCollections(dvm));
				}
				jTableResponseOptions = new JTableResponseOptions("OK", jsonMongoCollections);	
			}
			
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }
        return jTableResponseOptions;
    }
	public String processString(String jsonObj){
		String processString="";int indOf=0;int lIndOf=0;int tIndOf=0;String val="";String ip1="";String ip2="";String finalOutput="";
        processString=jsonObj.replaceAll("[{}]", "");       
        indOf=processString.indexOf("\"\"$date\":");
        if(indOf==-1){
        	indOf=processString.indexOf("\"$date\":");
        }        
        lIndOf=processString.indexOf("Z\"");
        finalOutput="";
        if(indOf!=-1 && lIndOf!=-1){
        	tIndOf=0;
        	val=processString.substring(indOf+9,lIndOf);	
        	tIndOf=val.indexOf("T");
        	if(tIndOf!=-1)
        		val=val.substring(0, tIndOf);
        	finalOutput=val;        	
        }else{
        	finalOutput=jsonObj;
        }
        finalOutput = finalOutput.replaceAll("[^\\w]", "");
        return finalOutput;
	}
	@RequestMapping(value="mongo.collection.pivot.report",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody String getMongoCollectionsForPivotReport(@RequestParam String collectionName, @RequestParam Integer testFactoryId, @RequestParam Integer productId) {			
		String finalResult="";
		List<JSONObject> mongoCollectionJsonObj=null;
		List<String> keyNameList=null;
		JSONObject finalObj = new JSONObject();
		JSONArray columnData1 = new JSONArray();
		InputStream inputStream = null;
		List<String> collectionList =new ArrayList<String>();
		log.info("mongo.collection.pivot.report collectionName>>>>"+collectionName+">>>>>testFactoryId*****"+testFactoryId+">>>ProdutId>>>"+productId);
		try{
			Properties properties = new Properties();
			inputStream = this.getClass().getResourceAsStream("/TAFServer.properties");
			properties.load(inputStream);			
			if(collectionName!=null && !collectionName.equals("")){
				if(properties.containsKey(collectionName.trim()+"_collection_not_required_fields")){
					String collections = properties.getProperty(collectionName.trim()+"_collection_not_required_fields");
					collectionList = Arrays.asList(collections.split(","));
					
				}else{
					collectionList=null;
				}
			}
		}catch(Exception e){
			log.error("Error..."+e);
		}
		try {
			
			keyNameList=mongoDBService.getKeyNameForPivotReportBasedOnCollectionName(collectionName);
			log.info("keyNameList size>>>>"+keyNameList.size());
			mongoCollectionJsonObj=mongoDBService.getCollectionForPivotReportBasedOnCollectionName(collectionName, testFactoryId, productId);
			log.info("mongoCollectionJsonObj>>>>"+mongoCollectionJsonObj.size());
			if(keyNameList!=null && keyNameList.size()>0){
				if(mongoCollectionJsonObj!=null && mongoCollectionJsonObj.size()>0){
					JSONArray columnData=null;
					
					JSONObject datObj =null;
					int i=1;boolean isNAFAvailable=false;
					for (JSONObject jObject : mongoCollectionJsonObj) {
						columnData = new JSONArray();
						datObj = new JSONObject();
						for(String keyName : keyNameList){
							if(collectionList!=null && collectionList.contains(keyName))
								isNAFAvailable=true;
							else
								isNAFAvailable=false;
							    if(!isNAFAvailable){
								    if(keyName!=null && keyName.equals("_id")){
								    	datObj.put("id", i);
								    }
								    else{
								    	try{
								    		if ( jObject.get(keyName)!=null) {
								    			datObj.put(keyName, ""+processString(jObject.get(keyName)+"")+"");
				
								    		}
								    	}catch(Exception e){
							    			datObj.put(keyName, "");
								    	}
								    }
							    }
						}
						columnData.put(datObj);
						columnData1.put(columnData);
						i++;
					}
					log.info("columnData1size>>>"+columnData1.length());
				}
			}
			
			finalObj.put("DATA", columnData1);				
			finalResult=finalObj.toString();
		} catch (Exception e) {
            log.error("JSON ERROR", e);
        }		        
	    return "["+finalResult+"]";
	}
	
	@RequestMapping(value = "pivot.rest.template.add", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody String addPivotRestTemplate( HttpServletRequest request,@RequestParam String templateName, @RequestParam String factoryId,@RequestParam String productId, @RequestParam Integer collectionId, @RequestParam String cubeName, @RequestParam String configJsonValue, @RequestParam String description) {	
	String response=null;
		try {
			
			
			log.info("templateName "+templateName);
			log.info("cubeName "+cubeName);
			log.info("configJsonValue "+configJsonValue);
			UserList user = (UserList) request.getSession().getAttribute("USER");
			PivotRestTemplate pivotRestTemplate=new PivotRestTemplate();
			pivotRestTemplate.setTemplateName(templateName);
			pivotRestTemplate.setDescription(description);
			pivotRestTemplate.setFactoryId(factoryId);
			pivotRestTemplate.setProductId(productId);
			pivotRestTemplate.setCollectionId(collectionId);
			pivotRestTemplate.setCubeName(cubeName);
			pivotRestTemplate.setConfigJsonValue(configJsonValue);
			pivotRestTemplate.setCreatedBy(user.getUserId());
			pivotRestTemplate.setUpdatedBy(user.getUserId());
			pivotRestTemplate.setCreatedDate(new Date());
			pivotRestTemplate.setUpdatedDate(new Date());
			pivotRestTemplate.setActiveStatus(1);
			log.info("pivotRestTemplate    --- "+ pivotRestTemplate);
			reportService.addPivotRestTemplate(pivotRestTemplate);
			
		} catch (Exception e) {
			response="Error adding pivot rest template record!";
			log.error("ERROR", e);
		}
		return response;
	}
	
	@RequestMapping(value="pivot.rest.template.report.byparams",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponseOptions getPivotRestTemplateReportByParams(@RequestParam Integer factoryId, @RequestParam Integer productId,@RequestParam Integer collectionId) {			
				
		log.info("pivot.rest.template.report.byparams collectionId>>>>"+collectionId+">>>>>factoryId*****"+factoryId+">>>ProdutId>>>"+productId);
		JTableResponseOptions jTableResponseOptions=null;
		 
		try {
			List<PivotRestTemplate> mongoCollection = reportService.getPivotRestTemplateReportByParams(factoryId,productId,collectionId);
			List<JsonPivotRestTemplate> jsonPivotRestTemplate=new ArrayList<JsonPivotRestTemplate>();
			if (mongoCollection != null){
				for(PivotRestTemplate dvm: mongoCollection){
			
					jsonPivotRestTemplate.add(new JsonPivotRestTemplate(dvm));
				}
				jTableResponseOptions = new JTableResponseOptions("OK", jsonPivotRestTemplate);	
			}
			
        } catch (Exception e) {
        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
            log.error("JSON ERROR", e);
        }
        return jTableResponseOptions;      
	}
	
	@RequestMapping(value="pivot.rest.template.list.id",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody String loadPivotReportTemplatesById(@RequestParam Integer templateId) {
		log.info("inside pivot.report.template.list.id>>>"+templateId);
		
		String finalResult="";int i=0;
		try {
			List<Object[]> pivotRestTemplateList = reportService.getPivotRestTemplateList(templateId);
			if (pivotRestTemplateList != null){
				for (Object[] objects : pivotRestTemplateList) {
					if(objects[1]!=null)
						finalResult=objects[1].toString();//productId
					if(objects[4]!=null)
						finalResult=finalResult+"~"+objects[4].toString();//cubeName
					if(objects[5]!=null)
						finalResult=finalResult+"~"+objects[5].toString();//configJsonValue
				}
			}
			
        } catch (Exception e) {
            log.error("JSON ERROR", e);
        }
		log.info("finalResult>>>"+finalResult);
	    return finalResult;

    }
	@RequestMapping(value="pivot.mongo.collections.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions loadAllPivotMongoCollections(HttpServletRequest request) {
		log.debug("inside pivot.mongo.collections.list");
		JTableResponseOptions jTableResponseOptions=null;
		 
		try {
			List<MongoCollection> mongoCollection = reportService.getMongoCollectionList();
			List<JsonPivotRestMongoCollections> jsonPivotRestMongoCollections=new ArrayList<JsonPivotRestMongoCollections>();
			if (mongoCollection != null){
				for(MongoCollection dvm: mongoCollection){
					jsonPivotRestMongoCollections.add(new JsonPivotRestMongoCollections(dvm));
				}
				jTableResponseOptions = new JTableResponseOptions("OK", jsonPivotRestMongoCollections);	
			}
			
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }
        return jTableResponseOptions;
    }
	@RequestMapping(value="pivot.rest.report.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions loadAllPivotReports(HttpServletRequest request) {
		log.debug("inside pivot.rest.report.list");
		JTableResponseOptions jTableResponseOptions=null;
		 
		try {
			List<PivotRestTemplate> pivotRestTemplate = reportService.getPivotRestTemplateList();
			List<JsonPivotRestTemplate> jsonPivotRestTemplate=new ArrayList<JsonPivotRestTemplate>();
			if (pivotRestTemplate != null){
				for(PivotRestTemplate dvm: pivotRestTemplate){
					jsonPivotRestTemplate.add(new JsonPivotRestTemplate(dvm));
				}
				jTableResponseOptions = new JTableResponseOptions("OK", jsonPivotRestTemplate);	
			}
			
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }
        return jTableResponseOptions;
    }
	
	@RequestMapping(value="pivot.rest.report.list.byId",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listTestRunJobByProductBuildId(@RequestParam Integer collectionId, @RequestParam Integer jtStartIndex, @RequestParam Integer jtPageSize) {
		log.info("calling pivot.rest.report.list.byId>>>>"+collectionId);
		JTableResponse jTableResponse = null;
		List<JsonPivotRestTemplate> jsonPivotRestTemplateList=new ArrayList<JsonPivotRestTemplate>();
		List<PivotRestTemplateDTO> pivotRestTemplateList=new ArrayList<PivotRestTemplateDTO>();
			try {
				pivotRestTemplateList=	reportService.getPivotRestTemplateList(collectionId,jtStartIndex, jtPageSize);
				if(pivotRestTemplateList!=null){
					for(PivotRestTemplateDTO testrp: pivotRestTemplateList){
						jsonPivotRestTemplateList.add(new JsonPivotRestTemplate(testrp,""));
					}
				}
					
				jTableResponse = new JTableResponse("OK", jsonPivotRestTemplateList,jsonPivotRestTemplateList.size() );
				
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);	            
	        }
        return jTableResponse;
    }	
	
	@RequestMapping(value="pivot.rest.template.delete.id",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse listTestRunJobByProductBuildId(@RequestParam Integer templateId) {
		log.info("calling pivot.rest.template.delete.id>>>>"+templateId);
		JTableResponse jTableResponse = null;
		
			try {
				reportService.deletePivotRestReportById(templateId);
				jTableResponse = new JTableResponse("OK","Deleted Successfully");
	        } catch (Exception e) {
	            log.error("JSON ERROR", e);	      
	            jTableResponse= new JTableResponse("ERROR","ERROR");
	        }
        return jTableResponse;
    }	
	@RequestMapping(value="pivot.load.product.names.id",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody String getProductsAndEngagementsNames(@RequestParam String productIds) {
		log.info("inside pivot.load.product.names.id>>>"+productIds);
		
		String finalResult="";List<Integer> dupEngList=new ArrayList<Integer>();
		String testFactoryNames="";String productNames="";int tIndOf=0;int pIndOf=0;
		try {
			List<Object[]> productsAndEngagementsNamesList = reportService.getProductAndEngagementNameList(productIds);
			if (productsAndEngagementsNamesList != null){
				for (Object[] objects : productsAndEngagementsNamesList) {
					
					if(objects[0]!=null){
						if(!dupEngList.contains((Integer)objects[0])){
							dupEngList.add((Integer)objects[0]);
							testFactoryNames=testFactoryNames+objects[1].toString()+", ";//testFactoryName
						}
							
					}
					if(objects[3]!=null)
						productNames=productNames+objects[3].toString()+", ";//productName
				}
				
				tIndOf=testFactoryNames.lastIndexOf(", ");
				if(tIndOf!=-1)
					testFactoryNames=testFactoryNames.substring(0,tIndOf);
				
				pIndOf=productNames.lastIndexOf(", ");
				if(pIndOf!=-1)
					productNames=productNames.substring(0,pIndOf);
				finalResult=testFactoryNames+"~"+productNames;
			}
			
        } catch (Exception e) {
            log.error("JSON ERROR", e);
        }
		log.info("finalResult>>>"+finalResult);
	    return finalResult;

    }
}
