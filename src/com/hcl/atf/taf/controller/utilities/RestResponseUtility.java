/**
 * 
 */
package com.hcl.atf.taf.controller.utilities;

import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import com.hcl.atf.taf.model.json.jtable.JTableSingleResponse;

/**
 * @author silambarasur
 *
 */
public class RestResponseUtility {
	private static final Log log = LogFactory.getLog(RestResponseUtility.class);
public static Response prepareErrorResponseWithoutData(String message, String failureDetails) {
		
		JSONObject responseJson = new JSONObject();
		try {
			responseJson.put("result","ERROR");
			responseJson.put("status", "400");			
			responseJson.put("message",message);
			responseJson.put("Failure_Details", failureDetails);
			responseJson.put("data", "");
		} catch (Exception e){
			log.error("Problem while preparing JSON Response", e);
		}
		return Response.ok(responseJson.toString()).build();
	}

	
public static Response prepareErrorResponse(String message, String failureDetails, String data) {
		
		JSONObject responseJson = new JSONObject();
		try {
			responseJson.put("result","ERROR");
			responseJson.put("status", "400");			
			responseJson.put("message",message);
			responseJson.put("Failure_Details", failureDetails);
			responseJson.put("data", data);
		} catch (Exception e){
			log.error("Problem while preparing JSON Response", e);
		}
		return Response.ok(responseJson.toString()).build();
	}

public static Response prepareSuccessResponse(String message, String data) {
		
		JSONObject responseJson = new JSONObject();
		try {
			responseJson.put("result","OK");
			responseJson.put("status", "200");			
			responseJson.put("message",message);
			responseJson.put("data", data);
		} catch (Exception e){
			log.error("Problem while preparing JSON Response", e);
		}
		return Response.ok(responseJson.toString()).build();
	}
public static Response prepareSuccessResponseWithJTableSingleResponse(String message, JTableSingleResponse data) {
	
	JSONObject responseJson = new JSONObject();
	try {
		responseJson.put("result","OK");
		responseJson.put("status", "200");			
		responseJson.put("message",message);
		responseJson.put("data", data);
	} catch (Exception e){
		log.error("Problem while preparing JSON Response", e);
	}
	return Response.ok(responseJson.toString()).build();
}
public static Response prepareSuccessResponseWithJSONObject(String message, JSONObject data) {
	
	JSONObject responseJson = new JSONObject();
	try {
		responseJson.put("result","OK");
		responseJson.put("status", "200");			
		responseJson.put("message",message);
		responseJson.put("data", data);
	} catch (Exception e){
		log.error("Problem while preparing JSON Response", e);
	}
	return Response.ok(responseJson.toString()).build();
}


public static Response prepareSuccessResponseWithJSONArray(String message, JSONArray data) {
	
	JSONObject responseJson = new JSONObject();
	try {
		responseJson.put("result","OK");
		responseJson.put("status", "200");			
		responseJson.put("message",message);
		responseJson.put("data", data);
	} catch (Exception e){
		log.error("Problem while preparing JSON Response", e);
	}
	return Response.ok(responseJson.toString()).build();
}

public static Response prepareSuccessResponseWithRecords(String message, String data,JSONArray jobRecords,JSONArray records) {
		
		JSONObject responseJson = new JSONObject();
		try {
			responseJson.put("result","OK");
			responseJson.put("status", "200");			
			responseJson.put("message",message);
			responseJson.put("data", data);
			responseJson.put("jobRecords", jobRecords);
			responseJson.put("Records", records);
		} catch (Exception e){
			log.error("Problem while preparing JSON Response", e);
		}
		return Response.ok(responseJson.toString()).build();
	}

public static Response prepareResponse(String result, String status, String message, String failureDetails, String data) {
		
		JSONObject responseJson = new JSONObject();
		try {
			responseJson.put("result",result);
			responseJson.put("status", status);			
			responseJson.put("message",message);
			responseJson.put("Failure_Details", failureDetails);
			responseJson.put("data", data);
		} catch (Exception e){
			log.error("Problem while preparing JSON Response", e);
		}
		return Response.ok(responseJson.toString()).build();
	}
	
public static Response prepareSuccessResponseWithSummaryandHistory(String message, String data,String summary,String history){
		JSONObject responseJson = new JSONObject();
		try {
			responseJson.put("result","OK");
			responseJson.put("status", "200");			
			responseJson.put("message",message);
			responseJson.put("data", data);
			responseJson.put("summary", summary);
			responseJson.put("history", history);
		} catch (Exception e){
			log.error("Problem while preparing JSON Response", e);
		}
		return Response.ok(responseJson.toString()).build();
	}

}
