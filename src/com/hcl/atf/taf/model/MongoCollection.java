package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "mongo_collections")
public class MongoCollection {

	private Integer collectionId;
	private String collectionName;
	private String displayName;
	private Integer activeStatus;
	
	public MongoCollection(){
		
	}
	public MongoCollection(String collectionName,String displayName,int activeStatus){
		this.collectionName=collectionName;
		this.displayName=displayName;
		this.activeStatus=activeStatus;	
		
	}
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "collectionId",unique = true, nullable = false)	
	public Integer getCollectionId() {
		return collectionId;
	}
	public void setCollectionId(Integer collectionId) {
		this.collectionId = collectionId;
	}
	@Column(name = "collectionName")
	public String getCollectionName() {
		return collectionName;
	}
	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}
	
	@Column(name = "activeStatus")
	public Integer getActiveStatus() {
		return activeStatus;
	}
	public void setActiveStatus(Integer activeStatus) {
		this.activeStatus = activeStatus;
	}
	@Column(name = "displayName")
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
}
