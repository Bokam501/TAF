package com.hcl.atf.taf.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "ui_object_items")
public class UIObjectItems implements Serializable {
	
	private Integer uiObjectItemId;	
	private String elementName;
	private String description;
	private String idType;
	private String groupName;
	private String elementType;
	private String webLabel;
	
	private String ie;
	private String safari;
	private String firefox;
	private String chrome;
	private String seeTestZoneIndex;
	private Integer seeTestIndexIndex;
	private String seetestLabel;
	private String appiumLabel;
	private String codeduiLabel;
	private String testCompleteLabel;
	private ProductMaster productMaster;
	private UserList userlist;
	private Date createdDate;
	private Date modifiedDate;
	
	private String pageName;
	private String pageURL;
	private String testEngineName;
	private String handle;
	private Integer isShare;
	private String firefoxgecko;
	private String edge;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "uiObjectItemId", unique = true, nullable = false)
	public Integer getUiObjectItemId() {
		return uiObjectItemId;
	}
	public void setUiObjectItemId(Integer uiObjectItemId) {
		this.uiObjectItemId = uiObjectItemId;
	}
	
	@Column(name = "elementName")
	public String getElementName() {
		return elementName;
	}
	
	public void setElementName(String elementName) {
		this.elementName = elementName;
	}
	@Column(name = "description")
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Column(name = "groupName")
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	
	@Column(name = "idType")
	public String getIdType() {
		return idType;
	}
	public void setIdType(String idType) {
		this.idType = idType;
	}
	@Column(name = "elementType")
	public String getElementType() {
		return elementType;
	}
	public void setElementType(String elementType) {
		this.elementType = elementType;
	}
	@Column(name = "webLabel")
	public String getWebLabel() {
		return webLabel;
	}
	public void setWebLabel(String webLabel) {
		this.webLabel = webLabel;
	}
	@Column(name = "ie")
	public String getIe() {
		return ie;
	}
	public void setIe(String ie) {
		this.ie = ie;
	}
	@Column(name = "safari")
	public String getSafari() {
		return safari;
	}
	public void setSafari(String safari) {
		this.safari = safari;
	}
	@Column(name = "firefox")
	public String getFirefox() {
		return firefox;
	}
	public void setFirefox(String firefox) {
		this.firefox = firefox;
	}
	@Column(name = "chrome")
	public String getChrome() {
		return chrome;
	}
	public void setChrome(String chrome) {
		this.chrome = chrome;
	}
	
	@Column(name = "seetestLabel")
	public String getSeetestLabel() {
		return seetestLabel;
	}
	public void setSeetestLabel(String seetestLabel) {
		this.seetestLabel = seetestLabel;
	}
	@Column(name = "appiumLabel")
	public String getAppiumLabel() {
		return appiumLabel;
	}
	public void setAppiumLabel(String appiumLabel) {
		this.appiumLabel = appiumLabel;
	}
	@Column(name = "codeduiLabel")
	public String getCodeduiLabel() {
		return codeduiLabel;
	}
	public void setCodeduiLabel(String codeduiLabel) {
		this.codeduiLabel = codeduiLabel;
	}
	@Column(name = "testCompleteLabel")
	public String getTestCompleteLabel() {
		return testCompleteLabel;
	}
	public void setTestCompleteLabel(String testCompleteLabel) {
		this.testCompleteLabel = testCompleteLabel;
	}

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="productId")
	public ProductMaster getProductMaster() {
		return productMaster;
	}
	public void setProductMaster(ProductMaster productMaster) {
		this.productMaster = productMaster;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="createdBy")
	public UserList getUserlist() {
		return userlist;
	}

	
	public void setUserlist(UserList userlist) {
		this.userlist = userlist;
	}
	
	@Temporal(value=TemporalType.TIMESTAMP)
	@Column(name="createdDate")
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	@Temporal(value=TemporalType.TIMESTAMP)
	@Column(name="modifiedDate")
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	@Column(name="seeTestZoneIndex")
	public String getSeeTestZoneIndex() {
		return seeTestZoneIndex;
	}
	public void setSeeTestZoneIndex(String seeTestZoneIndex) {
		this.seeTestZoneIndex = seeTestZoneIndex;
	}
	@Column(name="seetestIndex")
	public Integer getSeeTestIndexIndex() {
		return seeTestIndexIndex;
	}
	public void setSeeTestIndexIndex(Integer seeTestIndexIndex) {
		this.seeTestIndexIndex = seeTestIndexIndex;
	}
	
	@Column(name="pageName")
	public String getPageName() {
		return pageName;
	}
	public void setPageName(String pageName) {
		this.pageName = pageName;
	}
	
	@Column(name="pageURL")
	public String getPageURL() {
		return pageURL;
	}
	public void setPageURL(String pageURL) {
		this.pageURL = pageURL;
	}
	
	@Column(name="testEngineType")
	public String getTestEngineName() {
		return testEngineName;
	}
	public void setTestEngineName(String testEngineName) {
		this.testEngineName = testEngineName;
	}
	
	@Column(name="handle")
	public String getHandle() {
		return handle;
	}
	public void setHandle(String handle) {
		this.handle = handle;
	}
	
	@Column(name="isShare")
	public Integer getIsShare() {
		return isShare;
	}
	public void setIsShare(Integer isShare) {
		this.isShare = isShare;
	}
	@Column(name="firefoxgecko")
	public String getFirefoxgecko() {
		return firefoxgecko;
	}
	public void setFirefoxgecko(String firefoxgecko) {
		this.firefoxgecko = firefoxgecko;
	}
	
	@Column(name="edge")
	public String getEdge() {
		return edge;
	}
	public void setEdge(String edge) {
		this.edge = edge;
	}
}
