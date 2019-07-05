package com.hcl.atf.taf.model.json;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;





import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.TestDataItemValues;
import com.hcl.atf.taf.model.TestDataItems;
import com.hcl.atf.taf.model.TestDataPlan;
import com.hcl.atf.taf.model.json.jtable.JTableResponse;


public class JsonTestDataItemsValues implements Serializable{
	private static final Log log = LogFactory.getLog(JsonTestDataItemsValues.class);
	@JsonProperty
	private Integer testDataItemId;	
	@JsonProperty
	private String dataName;
	@JsonProperty
	private String type;
	@JsonProperty
	private String remarks;
	@JsonProperty
	private String createdDate;
	@JsonProperty
	private String modifiedDate;
	@JsonProperty
	private String createdBy;
	@JsonProperty
	private String productName;
	@JsonProperty
	private Integer testDataItemValueId;
	@JsonProperty
	private String values;
	@JsonProperty
	private Integer productId;
	@JsonProperty
	private Integer userId;
	@JsonProperty
	private Integer testDataPlanId;
	@JsonProperty
	private String handle;
	@JsonProperty
	private Integer isShare = 0;
	@JsonProperty
	private String modifiedFieldTitle;
	@JsonProperty
	private String modifiedFieldValue;
	@JsonProperty
	private String oldFieldValue;
	@JsonProperty
	private String value1;
	@JsonProperty
	private String value2;
	@JsonProperty
	private String value3;
	@JsonProperty
	private String value4;
	@JsonProperty
	private String value5;
	@JsonProperty
	private String value6;
	@JsonProperty
	private String value7;
	@JsonProperty
	private String value8;
	@JsonProperty
	private String value9;
	@JsonProperty
	private String value10;
	@JsonProperty
	private String value11;
	@JsonProperty
	private String value12;
	@JsonProperty
	private String value13;
	@JsonProperty
	private String value14;
	@JsonProperty
	private String value15;
	@JsonProperty
	private String value16;
	@JsonProperty
	private String value17;
	@JsonProperty
	private String value18;
	@JsonProperty
	private String value19;
	@JsonProperty
	private String value20;
	@JsonProperty
	private String value21;
	@JsonProperty
	private String value22;
	@JsonProperty
	private String value23;
	@JsonProperty
	private String value24;
	@JsonProperty
	private String value25;
	@JsonProperty
	private String value26;
	@JsonProperty
	private String value27;
	@JsonProperty
	private String value28;
	@JsonProperty
	private String value29;
	@JsonProperty
	private String value30;
	
	public JsonTestDataItemsValues() {

	}

	public JsonTestDataItemsValues(TestDataItems testDataItems){

		if(testDataItems != null){
			this.testDataItemId = testDataItems.getTestDataItemId();
			this.dataName = testDataItems.getDataName();
			this.type = testDataItems.getType();
			this.remarks = testDataItems.getRemarks();

			if (testDataItems.getCreatedDate() != null) {
				this.createdDate = DateUtility.sdfDateformatWithOutTime(testDataItems.getCreatedDate());

			}
			if (testDataItems.getModifiedDate() != null) {
				this.modifiedDate = DateUtility.sdfDateformatWithOutTime(testDataItems.getModifiedDate());
			}
		
			if(testDataItems.getProductMaster() !=  null){
				this.productId = testDataItems.getProductMaster().getProductId();
				this.productName = testDataItems.getProductMaster().getProductName();
			} 
			if(testDataItems.getUserlist() != null){
				this.createdBy = testDataItems.getUserlist().getLoginId();
			}
					
			if(testDataItems.getGroupName() != null && testDataItems.getGroupName() != ""){
				handle = testDataItems.getGroupName();
			}
			if(testDataItems.getIsShare() != null){

				isShare = testDataItems.getIsShare();
			}
		}
	}

	public JsonTestDataItemsValues getestDataItemsValues(TestDataItems testDataItems,boolean flag){
		JsonTestDataItemsValues js = new JsonTestDataItemsValues();
		if(testDataItems != null){
			js.testDataItemId = testDataItems.getTestDataItemId();
			js.dataName = testDataItems.getDataName();
			js.type = testDataItems.getType();
			js.remarks = testDataItems.getRemarks();
			if(testDataItems.getGroupName() != null && testDataItems.getGroupName() != ""){
				js.handle = testDataItems.getGroupName();
			}
			int count = 1;
			values = "";
			//Map<String, String> testDataIdValMap =  new HashMap<String, String>();
			//Set<TestDataItemValues> testDataItemValSet = testDataItems.getTestDataItemsValSet();
			try{
				if(testDataItems.getTestDataItemsValSet() != null && !testDataItems.getTestDataItemsValSet().isEmpty() && testDataItems.getTestDataItemsValSet() .size()>0){
					for(TestDataItemValues testDataItemVal :testDataItems.getTestDataItemsValSet()){
						if(testDataItemVal.getValues() != null && testDataItemVal.getValues() != ""){
							String val = "value"+count;
							Field field = js.getClass().getDeclaredField(val);
							log.info("Field Name :" +field.getName());
							field.setAccessible(true);
							field.set(js,new String(testDataItemVal.getValues()));
						}
						count++;
					}
				}
			}catch(Exception e){
				log.error("Unknown error "+e.getMessage());
			}
		}
		return js;
	}

	public JsonTestDataItemsValues(TestDataItemValues testDataItemvalues){

		if(testDataItemvalues != null){
			this.testDataItemValueId = testDataItemvalues.getTestDataValueId();
			this.values = testDataItemvalues.getValues();

		}
		if (testDataItemvalues.getCreatedDate() != null) {
			this.createdDate = DateUtility.sdfDateformatWithOutTime(testDataItemvalues.getCreatedDate());

		}

		if (testDataItemvalues.getTestDataItems() != null) {
			this.testDataItemId = testDataItemvalues.getTestDataItems().getTestDataItemId();

		}

		if(testDataItemvalues.getTestDataPlan() != null){
			testDataPlanId = testDataItemvalues.getTestDataPlan().getTestDataPlanId();
		}
	}


	@JsonIgnore
	public TestDataItems getTestDataItems(){
		TestDataItems testDataItems = new TestDataItems();
		if(testDataItemId != null){
			testDataItems.setTestDataItemId(testDataItemId);
		}
		if(createdDate != null ){
			testDataItems.setCreatedDate(DateUtility.dateFormatWithOutSeconds(createdDate));
		}else{
			testDataItems.setCreatedDate(new Date());
		}

		if(modifiedDate != null && modifiedDate != "" ){
			testDataItems.setModifiedDate(DateUtility.dateFormatWithOutSeconds(modifiedDate));	
		}else{
			testDataItems.setModifiedDate(new Date());
		}

	
		if(productId != null){
			ProductMaster productMaster = new ProductMaster();
			productMaster.setProductId(productId);
			testDataItems.setProductMaster(productMaster);
		}
		testDataItems.setDataName(dataName);
		testDataItems.setRemarks(remarks);
		testDataItems.setType(type);
		if(handle == null || handle == ""){
			handle = "UITestdata";
		}
		testDataItems.setGroupName(handle);
		if(isShare == null  ){
			isShare = 0 ;

		}
		testDataItems.setIsShare(isShare);

		return testDataItems;
	}
	@JsonIgnore
	public TestDataItemValues getTestDataItemValues(){
		TestDataItemValues testDataItemValues = new TestDataItemValues();
		TestDataItems testDataItem = new TestDataItems();
		if(testDataItemId != null){
			testDataItem.setTestDataItemId(testDataItemId);
			testDataItemValues.setTestDataItems(testDataItem);
		}
		if(createdDate != null && createdDate != ""){
			testDataItemValues.setCreatedDate(DateUtility.dateFormatWithOutSeconds(createdDate));
		}else{
			testDataItemValues.setCreatedDate(new Date());
		}
		testDataItemValues.setValues(values);
		testDataItemValues.setTestDataValueId(testDataItemValueId);
		TestDataPlan  tdPlan = new TestDataPlan();
		tdPlan.setTestDataPlanId(testDataPlanId);
		testDataItemValues.setTestDataPlan(tdPlan);
		return testDataItemValues;
	}
	public Integer getTestDataItemId() {
		return testDataItemId;
	}

	public String getModifiedFieldTitle() {
		return modifiedFieldTitle;
	}

	public void setModifiedFieldTitle(String modifiedFieldTitle) {
		this.modifiedFieldTitle = modifiedFieldTitle;
	}

	public String getModifiedFieldValue() {
		return modifiedFieldValue;
	}

	public void setModifiedFieldValue(String modifiedFieldValue) {
		this.modifiedFieldValue = modifiedFieldValue;
	}

	public String getOldFieldValue() {
		return oldFieldValue;
	}

	public void setOldFieldValue(String oldFieldValue) {
		this.oldFieldValue = oldFieldValue;
	}

	public void setTestDataItemId(Integer testDataItemId) {
		this.testDataItemId = testDataItemId;
	}

	public String getDataName() {
		return dataName;
	}

	public void setDataName(String dataName) {
		this.dataName = dataName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
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



	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}



	public Integer getTestDataItemValueId() {
		return testDataItemValueId;
	}

	public void setTestDataItemValueId(Integer testDataItemValueId) {
		this.testDataItemValueId = testDataItemValueId;
	}

	public String getValues() {
		return values;
	}

	public void setValues(String values) {
		this.values = values;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getTestDataPlanId() {
		return testDataPlanId;
	}

	public void setTestDataPlanId(Integer testDataPlanId) {
		this.testDataPlanId = testDataPlanId;
	}

	public Integer getIsShare() {
		return isShare;
	}

	public void setIsShare(Integer isShare) {
		this.isShare = isShare;
	}

	public String getHandle() {
		return handle;
	}

	public void setHandle(String handle) {
		this.handle = handle;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getValue1() {
		return value1;
	}

	public void setValue1(String value1) {
		this.value1 = value1;
	}

	public String getValue2() {
		return value2;
	}

	public void setValue2(String value2) {
		this.value2 = value2;
	}

	public String getValue3() {
		return value3;
	}

	public void setValue3(String value3) {
		this.value3 = value3;
	}

	public String getValue4() {
		return value4;
	}

	public void setValue4(String value4) {
		this.value4 = value4;
	}

	public String getValue5() {
		return value5;
	}

	public void setValue5(String value5) {
		this.value5 = value5;
	}

	public String getValue6() {
		return value6;
	}

	public void setValue6(String value6) {
		this.value6 = value6;
	}

	public String getValue7() {
		return value7;
	}

	public void setValue7(String value7) {
		this.value7 = value7;
	}

	public String getValue8() {
		return value8;
	}

	public void setValue8(String value8) {
		this.value8 = value8;
	}

	public String getValue9() {
		return value9;
	}

	public void setValue9(String value9) {
		this.value9 = value9;
	}

	public String getValue10() {
		return value10;
	}

	public void setValue10(String value10) {
		this.value10 = value10;
	}

	public String getValue11() {
		return value11;
	}

	public void setValue11(String value11) {
		this.value11 = value11;
	}

	public String getValue12() {
		return value12;
	}

	public void setValue12(String value12) {
		this.value12 = value12;
	}

	public String getValue13() {
		return value13;
	}

	public void setValue13(String value13) {
		this.value13 = value13;
	}

	public String getValue14() {
		return value14;
	}

	public void setValue14(String value14) {
		this.value14 = value14;
	}

	public String getValue15() {
		return value15;
	}

	public void setValue15(String value15) {
		this.value15 = value15;
	}

	public String getValue16() {
		return value16;
	}

	public void setValue16(String value16) {
		this.value16 = value16;
	}

	public String getValue17() {
		return value17;
	}

	public void setValue17(String value17) {
		this.value17 = value17;
	}

	public String getValue18() {
		return value18;
	}
	public void setValue18(String value18) {
		this.value18 = value18;
	}

	public String getValue19() {
		return value19;
	}

	public void setValue19(String value19) {
		this.value19 = value19;
	}

	public String getValue20() {
		return value20;
	}

	public void setValue20(String value20) {
		this.value20 = value20;
	}

	public String getValue21() {
		return value21;
	}

	public void setValue21(String value21) {
		this.value21 = value21;
	}

	public String getValue22() {
		return value22;
	}

	public void setValue22(String value22) {
		this.value22 = value22;
	}

	public String getValue23() {
		return value23;
	}

	public void setValue23(String value23) {
		this.value23 = value23;
	}

	public String getValue24() {
		return value24;
	}

	public void setValue24(String value24) {
		this.value24 = value24;
	}

	public String getValue25() {
		return value25;
	}

	public void setValue25(String value25) {
		this.value25 = value25;
	}

	public String getValue26() {
		return value26;
	}

	public void setValue26(String value26) {
		this.value26 = value26;
	}

	public String getValue27() {
		return value27;
	}

	public void setValue27(String value27) {
		this.value27 = value27;
	}

	public String getValue28() {
		return value28;
	}

	public void setValue28(String value28) {
		this.value28 = value28;
	}

	public String getValue29() {
		return value29;
	}

	public void setValue29(String value29) {
		this.value29 = value29;
	}

	public String getValue30() {
		return value30;
	}

	public void setValue30(String value30) {
		this.value30 = value30;
	}

}
