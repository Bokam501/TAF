package com.hcl.atf.taf.mongodb.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.hcl.atf.taf.model.DefectCollection;

@Document(collection = "defect")
public class ISEDefectCollectionMongo {
	private static final Log log = LogFactory.getLog(ISEDefectCollectionMongo.class);
	@Transient
	private SimpleDateFormat dateFormatForMongoDB = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
	
	@Id
	private Integer id;
	private String title;
	private String description;
	private String release;
	private String build;
	private String req_type;
	private String priority;
	private String severity;
	private String detection;
	private String injection;
	private String internalDefect;
	private Object date;
	private String status;
	private Object updatedDate;
	private String resolution;
	private String updatedBy;
	private Object last_updated_date;
	private String updated_by;
	private String file;
	
	
	
	public ISEDefectCollectionMongo() {		
		this.title = "";
		this.description = "";
		this.release = "";
		this.priority = "";
		this.resolution = "";
		this.severity = "";
		this.detection = "";
		this.injection = "";
		this.internalDefect = "";
		this.status = "";
		this.updatedDate = null;
		this.updatedBy = "";
		this.file = "";
		this.req_type = "";
		this.date = null;
		this.last_updated_date = null;
		this.updated_by = "";
	}

	public ISEDefectCollectionMongo(DefectCollection defectCollection) {
		this.id = defectCollection.get_id();
		this.title = defectCollection.getTitle();
		this.description = defectCollection.getDescription();
		this.release = defectCollection.getRelease();
		this.priority = defectCollection.getPriority();
		this.resolution = defectCollection.getResolution();
		this.severity = defectCollection.getSeverity();
		this.detection = defectCollection.getDetection();
		this.injection = defectCollection.getInjection();
		this.internalDefect = defectCollection.getInternalDefect();
		this.status = defectCollection.getStatus();
		this.updatedDate = setDateForMongoDB(defectCollection.getUpdatedDate());
		this.updatedBy = defectCollection.getUpdatedBy();
		this.file = defectCollection.getFile();
		this.req_type = defectCollection.getRequestType();
		this.date = setDateForMongoDB(defectCollection.getCreatedDate());
		this.last_updated_date = setDateForMongoDB(defectCollection.getLastUpdatedDate());
		this.updated_by = defectCollection.getUpdatedBy();
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Object _id) {
		String formatCheck = _id.toString();
		if(formatCheck.contains("oid=")){
			_id = formatCheck.split("oid=")[1].split("}")[0];
		}
		this.id = Integer.parseInt(_id.toString());
	}


	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}


	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	
	public String getRelease() {
		return release;
	}

	public void setRelease(String release) {
		this.release = release;
	}

	
	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	
	public String getResolution() {
		return resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}


	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}

	
	public String getDetection() {
		return detection;
	}

	public void setDetection(String detection) {
		this.detection = detection;
	}

	
	public String getInjection() {
		return injection;
	}

	public void setInjection(String injection) {
		this.injection = injection;
	}

	
	public String getInternalDefect() {
		return internalDefect;
	}

	public void setInternalDefect(String internalDefect) {
		this.internalDefect = internalDefect;
	}


	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	

	public Object getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Object updatedDate) {
		String formatCheck = updatedDate.toString();
		if(formatCheck.contains("date=")){
			try {
				updatedDate = dateFormatForMongoDB.parse(formatCheck.split("date=")[1].split("}")[0]);
			} catch (Exception e) {
				log.error("ERROR  ",e);
			}
		}
		this.updatedDate = updatedDate;
	}


	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}


	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	
	public String getBuild() {
		return build;
	}

	public void setBuild(String build) {
		this.build = build;
	}

	
	public String getReq_type() {
		return req_type;
	}

	public void setReq_type(String req_type) {
		this.req_type = req_type;
	}

	
	public Object getDate() {
		return date;
	}

	public void setDate(Object date) {
		this.date = date;
	}

	
	public Object getLast_updated_date() {
		return last_updated_date;
	}

	public void setLast_updated_date(Object last_updated_date) {
		this.last_updated_date = last_updated_date;
	}

	
	public String getUpdated_by() {
		return updated_by;
	}

	public void setUpdated_by(String updated_by) {
		this.updated_by = updated_by;
	}


	private Date setDateForMongoDB(Date dateToMongoDB){
		if(dateToMongoDB != null){
			Calendar dateToMongoDBCalendar = Calendar.getInstance();
			dateToMongoDBCalendar.setTime(dateToMongoDB);
			dateToMongoDBCalendar.add(Calendar.MILLISECOND, dateToMongoDBCalendar.getTimeZone().getRawOffset());
			dateToMongoDB = dateToMongoDBCalendar.getTime();
		}
		return dateToMongoDB;
	}
}
