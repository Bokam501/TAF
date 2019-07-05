package com.hcl.atf.taf.model;



import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;
import java.util.Set;

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

/**
 * TestManagementSystem  - Details of test Management system , Version, and connection properties are
 * maintained.
 */
@Entity
@Table(name = "scm_management_system")
public class SCMSystem implements java.io.Serializable {


	private Integer id;
	private String title;
	private String description;
	private String connectionUri;
	private String connectionUserName;
	private String connectionPassword;
	private String connectionProperty1;
	private String connectionProperty2;
	private String connectionProperty3;
	private String connectionProperty4;
	private String connectionProperty5;//Changes for Bug 792 - Adding TestSetFilter by name 
	private ProductMaster  productMaster;
	//private String scmSystemName;
	private String scmSystemVersion;
	private Integer isPrimary;
	private Integer status;
	private Date createdDate;
	private UserList createdBy;
	private UserList modifiedBy;
	private Date modifiedDate;
	private ToolIntagrationMaster toolIntagration;

	public SCMSystem(){

	}

	public SCMSystem(String title, String description, String connectionUri, String connectionUserName, String connectionPassword,
			String connectionProperty1,String connectionProperty2,String connectionProperty3,String connectionProperty4, String connectionProperty5, ProductMaster productMaster, 
			String scmSystemName,String scmSystemVersion, Set<TestManagementSystemMapping> testManagementSystemMappings){

		this.title = title;
		this.description = description;
		this.connectionUri = connectionUri;
		this.connectionPassword = connectionPassword;
		this.connectionProperty1 = connectionProperty1;
		this.connectionProperty2 = connectionProperty2;
		this.connectionProperty3 = connectionProperty3;
		this.connectionProperty4 = connectionProperty4;
		this.connectionProperty5 = connectionProperty5;

		this.productMaster = productMaster;
		//this.scmSystemName = scmSystemName;
		this.scmSystemVersion = scmSystemVersion;
	}


	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "scmManagementSystemId", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}


	@Column(name = "title", length = 250)
	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	@Column(name = "description", length = 2000)
	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	@Column(name = "connectionUri", length = 250)
	public String getConnectionUri() {
		return connectionUri;
	}

	public void setConnectionUri(String connectionUri) {
		this.connectionUri = connectionUri;
	}

	@Column(name = "connectionUsername", length = 50)
	public String getConnectionUserName() {
		return connectionUserName;
	}

	public void setConnectionUserName(String connectionUserName) {
		this.connectionUserName = connectionUserName;
	}

	@Column(name = "connectionPassword", length = 50)
	public String getConnectionPassword() {
		return connectionPassword;
	}


	public void setConnectionPassword(String connectionPassword) {
		this.connectionPassword = connectionPassword;
	}

	@Column(name = "connectionProperty1", length = 100)
	public String getConnectionProperty1() {
		return connectionProperty1;
	}

	public void setConnectionProperty1(String connectionProperty1) {
		this.connectionProperty1 = connectionProperty1;
	}

	@Column(name = "connectionProperty2", length = 100)
	public String getConnectionProperty2() {
		return connectionProperty2;
	}

	public void setConnectionProperty2(String connectionProperty2) {
		this.connectionProperty2 = connectionProperty2;
	}

	@Column(name = "connectionProperty3", length = 100)
	public String getConnectionProperty3() {
		return connectionProperty3;
	}


	public void setConnectionProperty3(String connectionProperty3) {
		this.connectionProperty3 = connectionProperty3;
	}

	@Column(name = "connectionProperty4", length = 100)
	public String getConnectionProperty4() {
		return connectionProperty4;
	}


	public void setConnectionProperty4(String connectionProperty4) {
		this.connectionProperty4 = connectionProperty4;
	}

	@Column(name = "connectionProperty5", length = 100)
	public String getConnectionProperty5() {
		return connectionProperty5;
	}


	public void setConnectionProperty5(String connectionProperty5) {
		this.connectionProperty5 = connectionProperty5;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "productId", nullable = false)
	public ProductMaster getProductMaster() {
		return productMaster;
	}              

	public void setProductMaster(ProductMaster productMaster) {
		this.productMaster = productMaster;
	}

/*	@Column(name = "scmSystemName", length = 50)
	public String getScmSystemName() {
		return scmSystemName;
	}


	public void setScmSystemName(String scmSystemName) {
		this.scmSystemName = scmSystemName;
	}*/


	@Column(name = "scmSystemVersion", length = 50)
	public String getScmSystemVersion() {
		return scmSystemVersion;
	}

	public void setScmSystemVersion(String scmSystemVersion) {
		this.scmSystemVersion = scmSystemVersion;
	}

	@Column(name = "isPrimary", length = 50)
	public Integer getIsPrimary() {
		return isPrimary;
	}

	public void setIsPrimary(Integer isPrimary) {
		this.isPrimary = isPrimary;
	}

	@Column(name = "status")
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createdDate")
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "createdBy") 
	public UserList getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(UserList createdBy) {
		this.createdBy = createdBy;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "modifiedBy") 
	public UserList getModifiedBy() {
		return createdBy;
	}

	public void setModifiedBy(UserList createdBy) {
		this.createdBy = createdBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modifiedDate")
	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "toolIntagrationId") 
	public ToolIntagrationMaster getToolIntagration() {
		return toolIntagration;
	}

	public void setToolIntagration(ToolIntagrationMaster toolIntagration) {
		this.toolIntagration = toolIntagration;
	}
	
	
}
