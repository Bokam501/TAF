package com.hcl.atf.taf.model;



import static javax.persistence.GenerationType.IDENTITY;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * DefectManagementSystem  - Details of defect Management system , Version, and connection properties are
 * maintained.
 */
@Entity
@Table(name = "defect_management_system")
public class DefectManagementSystem implements java.io.Serializable {

	
	private Integer defectManagementSystemId;
	private String title;
	private String description;
	private String connectionUri;
	private String connectionUserName;
	private String connectionPassword;
	private String connectionProperty1;
	private String connectionProperty2;
	private String connectionProperty3;
	private String connectionProperty4;
	private ProductMaster  productMaster;
//	private String defectSystemName;
	private ToolIntagrationMaster toolIntagration;
	private String defectSystemVersion;
	private Set<DefectManagementSystemMapping> defectManagementSystemMappings = new HashSet<DefectManagementSystemMapping>(0);
	private Integer isPrimary;


	public DefectManagementSystem(){
		
	}

	public DefectManagementSystem(String title, String description, String connectionUri, String connectionUserName, String connectionPassword,
			String connectionProperty1,String connectionProperty2,String connectionProperty3,String connectionProperty4,ProductMaster productMaster,Integer productId, 
			String defectSystemName,String defectSystemVersion,Set<DefectManagementSystemMapping> defectManagementSystemMappings){
		
		this.title = title;
		this.description = description;
		this.connectionUri = connectionUri;
		this.connectionPassword = connectionPassword;
		this.connectionUserName=connectionUserName;
		this.connectionProperty1 = connectionProperty1;
		this.connectionProperty2 = connectionProperty2;
		this.connectionProperty3 = connectionProperty3;
		this.connectionProperty4 = connectionProperty4;
		this.productMaster = productMaster;
		//this.defectSystemName = defectSystemName;
		this.defectSystemVersion = defectSystemVersion;
		this.defectManagementSystemMappings=defectManagementSystemMappings;
	}
	
	
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "defectManagementSystemId", unique = true, nullable = false)
	public Integer getDefectManagementSystemId() {
		return defectManagementSystemId;
	}

	public void setDefectManagementSystemId(Integer defectManagementSystemId) {
		this.defectManagementSystemId = defectManagementSystemId;
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
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "productId")
	public ProductMaster getProductMaster() {
		return productMaster;
	}	
	
	public void setProductMaster(ProductMaster productMaster) {
		this.productMaster = productMaster;
	}
	

	@Column(name = "defectSystemVersion", length = 50)
	public String getDefectSystemVersion() {
		return defectSystemVersion;
	}

	public void setDefectSystemVersion(String defectSystemVersion) {
		this.defectSystemVersion = defectSystemVersion;
	}
	
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "defectManagementSystem",cascade=CascadeType.ALL)
	public Set<DefectManagementSystemMapping> getDefectManagementSystemMappings() {
		return defectManagementSystemMappings;
	}

	public void setDefectManagementSystemMappings(
			Set<DefectManagementSystemMapping> defectManagementSystemMappings) {
		this.defectManagementSystemMappings = defectManagementSystemMappings;
	}
	@Column(name = "isPrimary", length = 100)
	public Integer getIsPrimary() {
		return isPrimary;
	}
	public void setIsPrimary(Integer isPrimary) {
		this.isPrimary = isPrimary;
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
