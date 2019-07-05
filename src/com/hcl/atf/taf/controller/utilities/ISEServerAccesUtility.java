/**
 * 
 */
package com.hcl.atf.taf.controller.utilities;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

/**
 * @author silambarasur
 *
 */

 public class ISEServerAccesUtility {
	
	 private static final Log log = LogFactory.getLog(ISEServerAccesUtility.class);
	 
	 	private static String url ="";
		private String restServiceName = "authenticate";
		private String respType = "json";
		private String authToken = "NA";
		private String methodName ="authtoken";
		private String projectName ="";	
		private String formData1="";
		private String formData = "";
		private static String responseData="";
		
		public static String GetISERestServiceCall(String iseURL,String inputFormData,String serviceName) {

			try {
				url = iseURL;
				ISEServerAccesUtility commonRestSerivceCall = new ISEServerAccesUtility();
				commonRestSerivceCall.setRestServiceName("JscriptWS");
				commonRestSerivceCall.setRespType("json");						
				commonRestSerivceCall.setMethodName("authenticate");
				String inputAuthJson = "{\"username\":\"admin@hcl.com\",\"password\":\"admin\",\"OrgId\":65}";
				commonRestSerivceCall.setFormData(inputAuthJson);
				JSONObject object = new JSONObject(commonRestSerivceCall.authenticateService());
				String authtoken = object.get("authToken").toString();
				log.info("--authtoken==="+authtoken);
				
			    commonRestSerivceCall.setFormData1(inputFormData);
				commonRestSerivceCall.setRestServiceName(serviceName);
				
				commonRestSerivceCall.setRespType("json");
				commonRestSerivceCall.setAuthToken(authtoken.split("\\|\\|")[0]);

				responseData= commonRestSerivceCall.invokePostService(null);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return responseData;
		}
		
		public String authenticateService(){

			ClientConfig config = new DefaultClientConfig();
			Client client = Client.create(config);
			String reqUrl = getUrl()+getRestServiceName()+"?type="+getRespType()+"&authtoken="+getAuthToken()+"&isAuthReq=true"	;
			if(null != getMethodName() && !getMethodName().equals("")){
				reqUrl+="&sname="+getMethodName();
			}
			if(null != getProjectName() && !getProjectName().equals("")){
				reqUrl+="&projectname="+getProjectName();
			}
			System.out.println(reqUrl);
			WebResource webResource = client.resource(UriBuilder.fromUri(reqUrl).build());
			ClientResponse response = webResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, getFormData());
			String resp = response.getEntity(String.class);
			return resp;
		}	


		public String invokePostService(String command){
			ClientConfig config = new DefaultClientConfig();
			Client client = Client.create(config);
			String reqUrl = getUrl()+getRestServiceName()+"?resptype="+getRespType()+"&authtoken="+getAuthToken();
			WebResource webResource = client.resource(UriBuilder.fromUri(reqUrl).build());
			ClientResponse response = webResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, getFormData1());
			String resp = response.getEntity(String.class);
			System.out.println("Response " + resp);
			return resp;
		}


		

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getRestServiceName() {
			return restServiceName;
		}

		public void setRestServiceName(String restServiceName) {
			this.restServiceName = restServiceName;
		}

		public String getRespType() {
			return respType;
		}

		public void setRespType(String respType) {
			this.respType = respType;
		}

		public String getAuthToken() {
			return authToken;
		}

		public void setAuthToken(String authToken) {
			this.authToken = authToken;
		}
		
		public String getMethodName() {
			return methodName;
		}
		public void setMethodName(String methodName) {
			this.methodName = methodName;
		}
		
		public String getFormData1() {
			return formData1;
		}
		public void setFormData1(String formData1) {
			this.formData1 = formData1;
		}
		
		public String getProjectName() {
			return projectName;
		}
		public void setProjectName(String projectName) {
			this.projectName = projectName;
		}
		
		public String getFormData() {
			return formData;
		}

		public void setFormData(String formData) {
			this.formData = formData;
		}

}
