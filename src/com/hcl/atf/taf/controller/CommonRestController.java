package com.hcl.atf.taf.controller;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jettison.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.google.gson.Gson;
import com.hcl.atf.taf.constants.AOTCConstants;
import com.hcl.atf.taf.model.TestSuiteList;
import com.hcl.atf.taf.model.json.JsonTestSuiteList;
import com.hcl.atf.taf.service.TestSuiteConfigurationService;




@Path("/download")
public class CommonRestController {

	private static final Log log = LogFactory.getLog(CommonRestController.class);

	@Autowired
	private TestSuiteConfigurationService testSuiteConfigurationService; 

	@Value("#{ilcmProps['EVIDENCE_FOLDER']}")
	private String EVIDENCE_FOLDER;

	@GET
	@Path("/evidence")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response downloadEvidenceFile(@QueryParam("fileName") String fileName) {


		boolean flag=false;
		File file=null;
		try{
			file = new File(fileName);

			if(file.exists()){
				flag=true;
			}
			else{
				flag=false;
			}
		}catch(Exception e){

		}

		return Response.ok(file).header("Content-Disposition", "attachment; filename=" + file.getName()).build();
	}

	@GET
	@Path("/evidenceReport")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response downloadEvidenceReport(@QueryParam("fileName") String fileName) {


		fileName="D:\\TAF\\Evidence_Collected\\EVIDENCE_UNZIPPED\\"+fileName;
		boolean flag=false;
		File file=null;
		try{
			file = new File(fileName);

			if(file.exists()){
				flag=true;
			}
			else{
				flag=false;
			}
		}catch(Exception e){

		}

		return Response.ok(file).header("Content-Disposition", "attachment; filename=" + file.getName()).build();
	}


	@POST
	@Path("/product.testSuite.list")
	@Produces(MediaType.APPLICATION_JSON)
	public Response listTestSuitesofProductId(@QueryParam("productId") int productMasterId) {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
		List<JsonTestSuiteList> jsonTestSuiteList = null;
		try{
			List<TestSuiteList> testSuiteList=testSuiteConfigurationService.getByProductId(productMasterId);
			jsonTestSuiteList=new ArrayList<JsonTestSuiteList>();
			JSONArray ar=new JSONArray();
			StringBuilder  s = new StringBuilder();
			s.append("OK");
			for(TestSuiteList tsl: testSuiteList){
				Gson g = new Gson();				
				s.append(g.toJson(new JsonTestSuiteList(tsl)));				
				jsonTestSuiteList.add(new JsonTestSuiteList(tsl));
			}
			ar.put(s);
			testSuiteList = null;
			return Response.ok(ar).build();
		} catch(Exception e){
			e.printStackTrace();
			Response.ok("ERROR").build();
		}	
		return  Response.ok().build();
	}	

	@GET
	@Path("/testScript")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response downloadGeneratedTestScript(@QueryParam("fileName") String fileName) {

		boolean flag=false;
		File file=null;
		try{
			file = new File(fileName);

			if(file.exists()){
				flag=true;
			}
			else{
				flag=false;
			}
		}catch(Exception e){

		}

		return Response.ok(file).header("Content-Disposition", "attachment; filename=" + file.getName()).build();
	}
	@GET
	@Path("/excelReport")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response downloadExcelReport(@QueryParam("fileName") String fileName) {

		File file=null;
		try{

			if(fileName .equalsIgnoreCase(AOTCConstants.TEST_TOOL_SELENIUM)){
				Properties properties = new Properties();
				InputStream inputStream = Thread.currentThread().getContextClassLoader()
						.getResourceAsStream("TAFServer.properties");
				if(inputStream != null) {
					properties.load(inputStream);
				} 
				fileName = properties.getProperty("SELENIUM_PROJECT_DESTINATION_FOLDER");

			}else if(fileName .equalsIgnoreCase(AOTCConstants.TEST_TOOL_APPIUM)){
				Properties properties = new Properties();
				InputStream inputStream = Thread.currentThread().getContextClassLoader()
						.getResourceAsStream("TAFServer.properties");
				if(inputStream != null) {
					properties.load(inputStream);
				} 
				fileName = properties.getProperty("APPIUM_PROJECT_DESTINATION_FOLDER");

			}else if(fileName .equalsIgnoreCase(AOTCConstants.TEST_TOOL_SEETEST)){
				Properties properties = new Properties();
				InputStream inputStream = Thread.currentThread().getContextClassLoader()
						.getResourceAsStream("TAFServer.properties");
				if(inputStream != null) {
					properties.load(inputStream);
				} 
				fileName = properties.getProperty("SEETEST_PROJECT_DESTINATION_FOLDER");
			}
			else if(fileName .equalsIgnoreCase(AOTCConstants.TEST_TOOL_PROTRACTOR)){
				Properties properties = new Properties();
				InputStream inputStream = Thread.currentThread().getContextClassLoader()
						.getResourceAsStream("TAFServer.properties");
				if(inputStream != null) {
					properties.load(inputStream);
				} 
				fileName = properties.getProperty("PROTRACTOR_PROJECT_DESTINATION_FOLDER");
			}else if(fileName .equalsIgnoreCase("AOTCIntroPPT")){
				String location="";
				String contextLocation=System.getProperty("catalina.home")+File.separator+AOTCConstants.AOTC_INTRODUCTION_TEMPLATE;

				fileName = contextLocation;

			}else if(fileName .equalsIgnoreCase(AOTCConstants.TEST_TOOL_CODEDUI)){
				Properties properties = new Properties();
				InputStream inputStream = Thread.currentThread().getContextClassLoader()
						.getResourceAsStream("TAFServer.properties");
				if(inputStream != null) {
					properties.load(inputStream);
				} 
				fileName = properties.getProperty("CODEDUI_PROJECT_DESTINATION_FOLDER");
			}
			else if(fileName .equalsIgnoreCase(AOTCConstants.TEST_TOOL_TESTCOMPLETE)){
				Properties properties = new Properties();
				InputStream inputStream = Thread.currentThread().getContextClassLoader()
						.getResourceAsStream("TAFServer.properties");
				if(inputStream != null) {
					properties.load(inputStream);
				} 
				fileName = properties.getProperty("TESTCOMPLETE_PROJECT_DESTINATION_FOLDER");
			}
                 



			file = new File(fileName);


			if(file.exists()){
				return Response.ok(file).header("Content-Disposition", "attachment; filename=" + file.getName()).build();
			}
			else{
				return Response.noContent().build();
			}
		}catch(Exception e){
			System.out.println(e);
		}
		return Response.noContent().build();

	}

	@GET
	@Path("/documents")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response downloadDocuments(@QueryParam("type") String type,@QueryParam("path") String path) {

		File file=null;
		String location="";
		String contextLocation=System.getProperty("catalina.home")+File.separator+AOTCConstants.DOCUMENTS_TEMPLATE;
		try{
			if(type.equalsIgnoreCase("UIOBJECTREPOSITORY")){
				location = contextLocation+File.separator+"ObjectRepository.zip";
			}else if(type.equalsIgnoreCase("TESTDATAITEM")){
				
				location = contextLocation+File.separator+"TestDataItem.zip";
			}else if(type.equalsIgnoreCase("EDAT")){
				location =path;
				log.info("location : "+location);
			}else{
				location = contextLocation+".zip";
			}
			file = new File(location);

			if(file.exists()){
				return Response.ok(file).header("Content-Disposition", "attachment; filename=" + file.getName()).build();
			}
			else{
				return Response.noContent().build();
			}
		}catch(Exception e){
			System.out.println(e);
		}

		return Response.noContent().build();
	}


}
