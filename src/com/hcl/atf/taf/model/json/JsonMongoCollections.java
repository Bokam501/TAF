package com.hcl.atf.taf.model.json;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.MongoCollection;

public class JsonMongoCollections implements java.io.Serializable {
	private static final Log log = LogFactory.getLog(JsonMongoCollections.class);
	@JsonProperty
	private Integer collectionId;
	@JsonProperty
	private String collectionName;
	@JsonProperty
	private String displayName;
	@JsonProperty
	private Integer activeStatus;
	public JsonMongoCollections() {
	}

	public JsonMongoCollections(MongoCollection mongoCollection) {
		this.collectionId = mongoCollection.getCollectionId();
		this.collectionName = mongoCollection.getCollectionName();
		this.displayName = mongoCollection.getDisplayName();
		this.activeStatus = mongoCollection.getActiveStatus();	
	}

	public Integer getCollectionId() {
		return collectionId;
	}

	public void setCollectionId(Integer collectionId) {
		this.collectionId = collectionId;
	}

	public String getCollectionName() {
		return collectionName;
	}

	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}

	public Integer getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Integer activeStatus) {
		this.activeStatus = activeStatus;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

}
