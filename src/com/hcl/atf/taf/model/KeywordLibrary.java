package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "keyword_library")
public class KeywordLibrary implements Serializable{
	
	private Integer id;
	private String type;
	private Languages language;
	private String className;
	private String binary;
	private String status;
	private TestToolMaster testToolMaster;
	private Date dateAdded;
	private UserList user;
	private BDDKeywordsPhrases keywords;
	private ProductType productType;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "keywordLibId", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	
	@Column(name = "functionType")
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	
	
		
	
	@Column(name = "className")
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	
	
	@Column(name = "url")
	public String getBinary() {
		return binary;
	}
	public void setBinary(String binary) {
		this.binary = binary;
	}
	
	
	@Column(name = "status")
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "testToolId")
	public TestToolMaster getTestToolMaster() {
		return testToolMaster;
	}
	public void setTestToolMaster(TestToolMaster testToolMaster) {
		this.testToolMaster = testToolMaster;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "languageId")
	public Languages getLanguage() {
		return language;
	}
	public void setLanguage(Languages language) {
		this.language = language;
	}
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	public UserList getUser() {
		return user;
	}
	public void setUser(UserList user) {
		this.user = user;
	}
	
	
	 @Temporal(value=TemporalType.TIMESTAMP)
	@Column(name = "createDate")
	public Date getDateAdded() {
		return dateAdded;
	}
	public void setDateAdded(Date dateAdded) {
		this.dateAdded = dateAdded;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id")
	public BDDKeywordsPhrases getKeywords() {
		return keywords;
	}
	public void setKeywords(BDDKeywordsPhrases keywords) {
		this.keywords = keywords;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="productTypeId")
	public ProductType getProductType() {
		return productType;
	}
	public void setProductType(ProductType productType) {
		this.productType = productType;
	}
	
}
