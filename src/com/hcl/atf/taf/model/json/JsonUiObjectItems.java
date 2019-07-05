package com.hcl.atf.taf.model.json;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.UIObjectItems;


public class JsonUiObjectItems {
	@JsonProperty
	private Integer uiObjectItemId;
	@JsonProperty
	private String elementName;
	@JsonProperty
	private String description;
	@JsonProperty
	private String IdType;
	@JsonProperty
	private Integer productId;
	@JsonProperty
	private String groupName;
	@JsonProperty
	private String elementType;
	@JsonProperty
	private String weblabel;
	@JsonProperty
	private String ie;
	@JsonProperty
	private String safari;
	@JsonProperty
	private String firefox;
	@JsonProperty
	private String chrome;
	@JsonProperty
	private String seeTestZoneIndex;
	@JsonProperty
	private Integer seeTestIndex;
	@JsonProperty
	private String seetestLabel;
	@JsonProperty
	private String appiumLabel;
	@JsonProperty
	private String codeduiLabel;
	@JsonProperty
	private String testCompleteLabel;
	@JsonProperty
	private Integer userId;
	@JsonProperty
	private String createdBy;
	@JsonProperty
	private String createdDate;
	@JsonProperty
	private String modifiedDate;
	@JsonProperty
	private String handle;
	@JsonProperty
	private String pageName;
	@JsonProperty
	private String pageURL;
	@JsonProperty
	private String testEngineName;
	@JsonProperty
	private String chromeXpath;
	@JsonProperty
	private String chromeCssSelector;
	@JsonProperty
	private String firefoxXpath;
	@JsonProperty
	private String firefoxCssSelector;
	@JsonProperty
	private String safariXpath;
	@JsonProperty
	private String safariCssSelector;
	@JsonProperty
	private String ieXpath;
	@JsonProperty
	private String ieCssSelector;
	@JsonProperty
	private String id;
	@JsonProperty
	private Integer isShare = 0;
	@JsonProperty
	private String firefoxgecko;
	@JsonProperty
	private String edge;
	@JsonProperty
	private String edgeXpath;
	@JsonProperty
	private String edgeCssSelector;
	@JsonProperty
	private String firefoxgeckoXpath;
	@JsonProperty
	private String firefoxgeckoCssSelector;
	public JsonUiObjectItems() {
		
	}
	
	public JsonUiObjectItems(UIObjectItems uiObjectItems){
		
		if(uiObjectItems != null){
			this.uiObjectItemId = uiObjectItems.getUiObjectItemId();
			this.elementName = uiObjectItems.getElementName();
			this.description = uiObjectItems.getDescription();
			this.IdType = uiObjectItems.getIdType();
			this.groupName = uiObjectItems.getGroupName();
			this.elementType = uiObjectItems.getElementType();
			this.weblabel = uiObjectItems.getWebLabel();
			this.ie = uiObjectItems.getIe();
			this.safari = uiObjectItems.getSafari();
			this.firefox = uiObjectItems.getFirefox();
			this.chrome = uiObjectItems.getChrome();
			this.seeTestZoneIndex = uiObjectItems.getSeeTestZoneIndex();
			this.seeTestIndex = uiObjectItems.getSeeTestIndexIndex();
			this.seetestLabel = uiObjectItems.getSeetestLabel();
			this.appiumLabel = uiObjectItems.getAppiumLabel();
			this.codeduiLabel = uiObjectItems.getCodeduiLabel();
			this.testCompleteLabel = uiObjectItems.getTestCompleteLabel();
			if (uiObjectItems.getCreatedDate() != null) {
				this.createdDate = DateUtility.sdfDateformatWithOutTime((Date)uiObjectItems.getCreatedDate());
			}
		    if (uiObjectItems.getModifiedDate() != null) {
		    	this.modifiedDate = DateUtility.sdfDateformatWithOutTime((Date)uiObjectItems.getModifiedDate());
		    } 
			if(uiObjectItems.getUserlist() != null){
				this.userId = uiObjectItems.getUserlist().getUserId();
				this.createdBy = uiObjectItems.getUserlist().getLoginId();
			}
			if(uiObjectItems.getProductMaster() !=  null){
				this.productId = uiObjectItems.getProductMaster().getProductId();
			}
			if(uiObjectItems.getHandle() != null){
				this.handle = uiObjectItems.getHandle();
			}
			if(uiObjectItems.getPageName() != null){
				this.pageName = uiObjectItems.getPageName();
			}
			if(uiObjectItems.getPageURL() != null){
				this.pageURL = uiObjectItems.getPageURL();
			}
			if(uiObjectItems.getTestEngineName() != null){
				this.testEngineName = uiObjectItems.getTestEngineName();
			}
			if(uiObjectItems.getIsShare() != null){
				isShare = uiObjectItems.getIsShare();			
			}
			this.firefoxgecko = uiObjectItems.getFirefoxgecko();
			this.edge = uiObjectItems.getEdge();
		}
		
		
		
	}
	
	public JsonUiObjectItems(UIObjectItems uiObjectItems,Boolean flag){
		
		this.elementName = uiObjectItems.getElementName();
		this.description = uiObjectItems.getDescription();
		this.IdType = uiObjectItems.getIdType();
		this.elementType = uiObjectItems.getElementType();
		if(IdType.equalsIgnoreCase("xpath")){
			this.ieXpath = uiObjectItems.getIe();
			this.safariXpath = uiObjectItems.getSafari();
			this.firefoxXpath = uiObjectItems.getFirefox();
			this.chromeXpath = uiObjectItems.getChrome();
			this.firefoxgeckoXpath = uiObjectItems.getFirefoxgecko();
			this.edgeXpath = uiObjectItems.getEdge();
		}else if(IdType.equalsIgnoreCase("cssSelector")){
			this.ieCssSelector = uiObjectItems.getIe();
			this.safariCssSelector = uiObjectItems.getSafari();
			this.firefoxCssSelector = uiObjectItems.getFirefox();
			this.chromeCssSelector = uiObjectItems.getChrome();
			this.firefoxgeckoCssSelector = uiObjectItems.getFirefoxgecko();
		}else{
			
		}
		this.testEngineName = uiObjectItems.getTestEngineName();
		if(testEngineName.equalsIgnoreCase("web")){
			this.id = uiObjectItems.getWebLabel();
		}else if(testEngineName.equalsIgnoreCase("APPIUM")){
			this.id = uiObjectItems.getAppiumLabel();
		}else if(testEngineName.equalsIgnoreCase("MOBILE-SEETEST")){
			this.seetestLabel = uiObjectItems.getSeetestLabel();
		}else if(testEngineName.equalsIgnoreCase("CodedUI")){
			this.id = uiObjectItems.getCodeduiLabel();
		}else if(testEngineName.equalsIgnoreCase("TestComplete")){
			this.id = uiObjectItems.getTestCompleteLabel();
		}else{}
		
	}
	
	@JsonIgnore
	public UIObjectItems getUIObjects(){
		
		UIObjectItems uIObjectItems = new UIObjectItems();
		if(uiObjectItemId != null){
		uIObjectItems.setUiObjectItemId(uiObjectItemId);
		}
		uIObjectItems.setAppiumLabel(appiumLabel);
		uIObjectItems.setCodeduiLabel(codeduiLabel);
		uIObjectItems.setTestCompleteLabel(testCompleteLabel);
		uIObjectItems.setChrome(chrome);
		if(createdDate != null && !createdDate.isEmpty() ){
			uIObjectItems.setCreatedDate(DateUtility.dateFormatWithOutSeconds(createdDate));	
		}
		
		uIObjectItems.setDescription(description);
		uIObjectItems.setElementName(elementName);
		uIObjectItems.setTestEngineName(testEngineName);
		uIObjectItems.setFirefox(firefox);
		uIObjectItems.setGroupName(groupName);
		uIObjectItems.setIdType(IdType);
		uIObjectItems.setIe(ie);
		uIObjectItems.setSafari(safari);
		uIObjectItems.setFirefoxgecko(firefoxgecko);
		uIObjectItems.setEdge(edge);
		uIObjectItems.setSeeTestIndexIndex(seeTestIndex);
		uIObjectItems.setSeeTestZoneIndex(seeTestZoneIndex);
		uIObjectItems.setSeetestLabel(seetestLabel);
		uIObjectItems.setWebLabel(weblabel);
		ProductMaster productMaster = new ProductMaster();
		productMaster.setProductId(productId);
		uIObjectItems.setProductMaster(productMaster);
		uIObjectItems.setElementType(elementType);
		uIObjectItems.setPageName(pageName);
		uIObjectItems.setPageURL(pageURL);
		if(handle == null || handle == ""){
			handle = "UIObject";
		}
		uIObjectItems.setHandle(handle);
		if(isShare == null){
			isShare = 0;
		}
		uIObjectItems.setIsShare(isShare);		
		return uIObjectItems;
		
	}
	
	
	public Integer getUiObjectItemId() {
		return uiObjectItemId;
	}

	public void setUiObjectItemId(Integer uiObjectItemId) {
		this.uiObjectItemId = uiObjectItemId;
	}

	public String getElementName() {
		return elementName;
	}

	public void setElementName(String elementName) {
		this.elementName = elementName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIdType() {
		return IdType;
	}

	public void setIdType(String idType) {
		IdType = idType;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroup(String groupName) {
		this.groupName = groupName;
	}

	public String getElementType() {
		return elementType;
	}

	public void setElementType(String elementType) {
		this.elementType = elementType;
	}

	public String getWeblabel() {
		return weblabel;
	}

	public void setWeblabel(String weblabel) {
		this.weblabel = weblabel;
	}

	
	public String getIe() {
		return ie;
	}

	public void setIe(String ie) {
		this.ie = ie;
	}

	public String getSafari() {
		return safari;
	}

	public void setSafari(String safari) {
		this.safari = safari;
	}

	public String getFirefox() {
		return firefox;
	}

	public void setFirefox(String firefox) {
		this.firefox = firefox;
	}

	public String getChrome() {
		return chrome;
	}

	public void setChrome(String chrome) {
		this.chrome = chrome;
	}

	public String getAppiumLabel() {
		return appiumLabel;
	}

	public void setAppiumLabel(String appiumLabel) {
		this.appiumLabel = appiumLabel;
	}

	public String getCodeduiLabel() {
		return codeduiLabel;
	}

	public void setCodeduiLabel(String codeduiLabel) {
		this.codeduiLabel = codeduiLabel;
	}

	public String getTestCompleteLabel() {
		return testCompleteLabel;
	}

	public void setTestCompleteLabel(String testCompleteLabel) {
		this.testCompleteLabel = testCompleteLabel;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getSeeTestZoneIndex() {
		return seeTestZoneIndex;
	}

	public void setSeeTestZoneIndex(String seeTestZoneIndex) {
		this.seeTestZoneIndex = seeTestZoneIndex;
	}

	public Integer getSeeTestIndex() {
		return seeTestIndex;
	}

	public void setSeeTestIndex(Integer seeTestIndex) {
		this.seeTestIndex = seeTestIndex;
	}

	public String getHandle() {
		return handle;
	}

	public void setHandle(String handle) {
		this.handle = handle;
	}

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	public String getPageURL() {
		return pageURL;
	}

	public void setPageURL(String pageURL) {
		this.pageURL = pageURL;
	}

	public String getTestEngineName() {
		return testEngineName;
	}

	public void setTestEngineName(String testEngineName) {
		this.testEngineName = testEngineName;
	}

	public String getChromeXpath() {
		return chromeXpath;
	}

	public void setChromeXpath(String chromeXpath) {
		this.chromeXpath = chromeXpath;
	}

	public String getChromeCssSelector() {
		return chromeCssSelector;
	}

	public void setChromeCssSelector(String chromeCssSelector) {
		this.chromeCssSelector = chromeCssSelector;
	}

	public String getFirefoxXpath() {
		return firefoxXpath;
	}

	public void setFirefoxXpath(String firefoxXpath) {
		this.firefoxXpath = firefoxXpath;
	}

	public String getFirefoxCssSelector() {
		return firefoxCssSelector;
	}

	public void setFirefoxCssSelector(String firefoxCssSelector) {
		this.firefoxCssSelector = firefoxCssSelector;
	}

	public String getSafariXpath() {
		return safariXpath;
	}

	public void setSafariXpath(String safariXpath) {
		this.safariXpath = safariXpath;
	}

	public String getSafariCssSelector() {
		return safariCssSelector;
	}

	public void setSafariCssSelector(String safariCssSelector) {
		this.safariCssSelector = safariCssSelector;
	}

	public String getIeXpath() {
		return ieXpath;
	}

	public void setIeXpath(String ieXpath) {
		this.ieXpath = ieXpath;
	}

	public String getIeCssSelector() {
		return ieCssSelector;
	}

	public void setIeCssSelector(String ieCssSelector) {
		this.ieCssSelector = ieCssSelector;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public Integer getIsShare() {
		return isShare;
	}

	public void setIsShare(Integer isShare) {
		this.isShare = isShare;
	}

	public String getSeetestLabel() {
		return seetestLabel;
	}

	public void setSeetestLabel(String seetestLabel) {
		this.seetestLabel = seetestLabel;
	}

	public String getFirefoxgecko() {
		return firefoxgecko;
	}

	public void setFirefoxgecko(String firefoxgecko) {
		this.firefoxgecko = firefoxgecko;
	}

	public String getEdge() {
		return edge;
	}

	public void setEdge(String edge) {
		this.edge = edge;
	}

	public String getEdgeXpath() {
		return edgeXpath;
	}

	public void setEdgeXpath(String edgeXpath) {
		this.edgeXpath = edgeXpath;
	}

	public String getEdgeCssSelector() {
		return edgeCssSelector;
	}

	public void setEdgeCssSelector(String edgeCssSelector) {
		this.edgeCssSelector = edgeCssSelector;
	}

	public String getFirefoxgeckoXpath() {
		return firefoxgeckoXpath;
	}

	public void setFirefoxgeckoXpath(String firefoxgeckoXpath) {
		this.firefoxgeckoXpath = firefoxgeckoXpath;
	}

	public String getFirefoxgeckoCssSelector() {
		return firefoxgeckoCssSelector;
	}

	public void setFirefoxgeckoCssSelector(String firefoxgeckoCssSelector) {
		this.firefoxgeckoCssSelector = firefoxgeckoCssSelector;
	}
	
}
