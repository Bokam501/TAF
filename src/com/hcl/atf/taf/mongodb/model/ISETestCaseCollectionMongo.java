/**
 * 
 */
package com.hcl.atf.taf.mongodb.model;

import java.util.Iterator;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.hcl.atf.taf.model.ProductFeature;
import com.hcl.atf.taf.model.TestCaseList;

/**
 * @author silambarasur
 *
 */
@Document(collection = "test_executions")
public class ISETestCaseCollectionMongo {

	@Id
	private String id;
	private String _class;
	private String title;
    private String description;
    private String environment;
    private Object created_date;
    private Object last_updated_date;    
    private String primary_feature;
    private String primary_feature_parent;
    private String indexName;
	private String docType;
    
	public ISETestCaseCollectionMongo() {
		
		
	}	
    public ISETestCaseCollectionMongo(TestCaseList testCase) {
    	this.indexName="testcases";
		this.docType="testcases";
    	this.id=testCase.getTestCaseId()+"";
    	this._class = this.getClass().getName().replace("\\", ".").replace("/", ".");
		this.title=testCase.getTestCaseName();
		this.description=testCase.getTestCaseDescription();
		this.created_date=testCase.getTestCaseCreatedDate();
		this.last_updated_date=testCase.getTestCaseLastUpdatedDate();
		
		if(testCase.getProductFeature()!=null && testCase.getProductFeature().size() >0){
			Set<ProductFeature> features = testCase.getProductFeature();
			if (features != null && !features.isEmpty()) {
				Iterator iter = features.iterator();
				ProductFeature feature = (ProductFeature) iter.next();
				this.primary_feature = feature.getProductFeatureName();
				this.primary_feature_parent = feature.getParentFeature() != null ?feature.getParentFeature().getProductFeatureName() : "N/A";  
			}
			
		}
    	
    }
    
	public String getId() {
		return id;
	}
	public void setId(String _id) {
		this.id = _id;
	}
	public String get_class() {
		return _class;
	}
	public void set_class(String _class) {
		this._class = _class;
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
	public String getEnvironment() {
		return environment;
	}
	public void setEnvironment(String environment) {
		this.environment = environment;
	}
	public Object getCreated_date() {
		return created_date;
	}
	public void setCreated_date(Object created_date) {
		this.created_date = created_date;
	}
	public Object getLast_updated_date() {
		return last_updated_date;
	}
	public void setLast_updated_date(Object last_updated_date) {
		this.last_updated_date = last_updated_date;
	}
	public String getPrimary_feature() {
		return primary_feature;
	}
	public void setPrimary_feature(String primary_feature) {
		this.primary_feature = primary_feature;
	}
	public String getPrimary_feature_parent() {
		return primary_feature_parent;
	}
	public void setPrimary_feature_parent(String primary_feature_parent) {
		this.primary_feature_parent = primary_feature_parent;
	}
	public String getIndexName() {
		return indexName;
	}
	public String getDocType() {
		return docType;
	}
	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}
	public void setDocType(String docType) {
		this.docType = docType;
	}
    
}
