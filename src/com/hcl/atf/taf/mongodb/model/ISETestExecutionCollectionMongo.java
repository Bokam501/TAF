/**
 * 
 */
package com.hcl.atf.taf.mongodb.model;

import java.util.Iterator;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.hcl.atf.taf.model.ProductFeature;
import com.hcl.atf.taf.model.TestCaseExecutionResult;

/**
 * @author silambarasur
 *
 */
@Document(collection = "test_executions")
public class ISETestExecutionCollectionMongo {
	
	
	@Id
	private String id;
	private String _class;
    private String release;
    private String build;
    private String testcaseid;
    private String title;
    private String environment;
    private String status;
    private Object date;
    private Object last_updated_date;
    private String updated_by; // Need to map
    
    private String primary_feature;
    private String primary_feature_parent;
    
    private Integer productId;
    private String productName;
    
  //Added to capturing the Mobile Device characteristics
  	private String deviceName;
  	private Integer platformTypeId;
  	private String platformTypeName;
  	private String platformTypeVersion;
  	
  	private String UDID;
  	private String deviceModelId;
  	private String deviceModel;
  	private String kernelNumber;
  	private String deviceBuildNumber;
  	private Integer deviceMakeId;
  	private String deviceMake;
  	private Integer screenResolutionX;
  	private Integer screenResolutionY;
    
    
    
public ISETestExecutionCollectionMongo(TestCaseExecutionResult testCaseResult) {
		
		
		
		if(testCaseResult!=null){
			this.id = testCaseResult.getTestCaseExecutionResultId()+"";
			
			this.status = testCaseResult.getResult();
			this.last_updated_date = testCaseResult.getEndTime();
			
				if(testCaseResult.getWorkPackageTestCaseExecutionPlan().getRunConfiguration()!=null){
					if(testCaseResult.getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration()!=null){
						this.environment = testCaseResult.getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration().getRunconfigName();
						if(testCaseResult.getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration().getGenericDevice() != null){
							
							UDID=testCaseResult.getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration().getGenericDevice().getUDID();
							screenResolutionX=testCaseResult.getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration().getGenericDevice().getScreenResolutionX();
							screenResolutionY=testCaseResult.getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration().getGenericDevice().getScreenResolutionY();
							if(testCaseResult.getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration().getGenericDevice().getDeviceModelMaster()!= null) {
								deviceModel=testCaseResult.getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration().getGenericDevice().getDeviceModelMaster().getDeviceModel();
								deviceName=testCaseResult.getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration().getGenericDevice().getDeviceModelMaster().getDeviceName();
								if(testCaseResult.getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration().getGenericDevice().getDeviceModelMaster().getDeviceMakeMaster() != null) {
									deviceMake = testCaseResult.getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration().getGenericDevice().getDeviceModelMaster().getDeviceMakeMaster().getDeviceMake();
								}
								if(testCaseResult.getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration().getGenericDevice().getPlatformType() != null) {
									
									platformTypeName=testCaseResult.getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration().getGenericDevice().getPlatformType().getName();
									platformTypeVersion=testCaseResult.getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration().getGenericDevice().getPlatformType().getVersion();
								}
							}
						}
						if(deviceMake != null && !deviceMake.isEmpty()) {
							environment = environment+"~"+deviceMake;
						}
					}
				}
			
				
				
				if(testCaseResult.getWorkPackageTestCaseExecutionPlan().getWorkPackage()!=null){
					if(testCaseResult.getWorkPackageTestCaseExecutionPlan().getWorkPackage().getProductBuild()!=null){
						this.build = testCaseResult.getWorkPackageTestCaseExecutionPlan().getWorkPackage().getProductBuild().getBuildname();
						this.date = testCaseResult.getWorkPackageTestCaseExecutionPlan().getActualExecutionDate();//execution date
						
						if(testCaseResult.getWorkPackageTestCaseExecutionPlan().getWorkPackage().getProductBuild().getProductVersion()!=null){
							this.release = testCaseResult.getWorkPackageTestCaseExecutionPlan().getWorkPackage().getProductBuild().getProductVersion().getProductVersionListId().toString();
							if( testCaseResult.getWorkPackageTestCaseExecutionPlan().getWorkPackage().getProductBuild().getProductVersion().getProductMaster()!=null){
								this.productId = testCaseResult.getWorkPackageTestCaseExecutionPlan().getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductId();
								this.productName = testCaseResult.getWorkPackageTestCaseExecutionPlan().getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductName();
							}
						}
							
						}
						
					}
				}
				
				if(testCaseResult.getWorkPackageTestCaseExecutionPlan().getTestCase()!=null){
					this.testcaseid=testCaseResult.getWorkPackageTestCaseExecutionPlan().getTestCase().getTestCaseId()+"";
					this.title = testCaseResult.getWorkPackageTestCaseExecutionPlan().getTestCase().getTestCaseName();
					Set<ProductFeature> features = testCaseResult.getWorkPackageTestCaseExecutionPlan().getTestCase().getProductFeature();
					if (features != null && !features.isEmpty()) {
						Iterator iter = features.iterator();
						ProductFeature feature = (ProductFeature) iter.next();
						this.primary_feature = feature.getProductFeatureName();
						this.primary_feature_parent = feature.getParentFeature() != null ?feature.getParentFeature().getProductFeatureName() : "N/A";  
					}
					
				}
				
				
				
			}
			
public Integer getProductId() {
	return productId;
}



public void setProductId(Integer productId) {
	this.productId = productId;
}



public String getProductName() {
	return productName;
}



public void setProductName(String productName) {
	this.productName = productName;
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
	public String getRelease() {
		return release;
	}
	public void setRelease(String release) {
		this.release = release;
	}
	public String getBuild() {
		return build;
	}
	public void setBuild(String build) {
		this.build = build;
	}
	public String getTestcaseid() {
		return testcaseid;
	}
	public void setTestcaseid(String testcaseid) {
		this.testcaseid = testcaseid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getEnvironment() {
		return environment;
	}
	public void setEnvironment(String environment) {
		this.environment = environment;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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

	public String getDeviceName() {
		return deviceName;
	}

	public Integer getPlatformTypeId() {
		return platformTypeId;
	}

	public String getPlatformTypeName() {
		return platformTypeName;
	}

	public String getPlatformTypeVersion() {
		return platformTypeVersion;
	}

	public String getUDID() {
		return UDID;
	}

	public String getDeviceModelId() {
		return deviceModelId;
	}

	public String getDeviceModel() {
		return deviceModel;
	}

	public String getKernelNumber() {
		return kernelNumber;
	}

	public String getDeviceBuildNumber() {
		return deviceBuildNumber;
	}

	public Integer getDeviceMakeId() {
		return deviceMakeId;
	}

	public String getDeviceMake() {
		return deviceMake;
	}

	public Integer getScreenResolutionX() {
		return screenResolutionX;
	}

	public Integer getScreenResolutionY() {
		return screenResolutionY;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public void setPlatformTypeId(Integer platformTypeId) {
		this.platformTypeId = platformTypeId;
	}

	public void setPlatformTypeName(String platformTypeName) {
		this.platformTypeName = platformTypeName;
	}

	public void setPlatformTypeVersion(String platformTypeVersion) {
		this.platformTypeVersion = platformTypeVersion;
	}

	public void setUDID(String uDID) {
		UDID = uDID;
	}

	public void setDeviceModelId(String deviceModelId) {
		this.deviceModelId = deviceModelId;
	}

	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	public void setKernelNumber(String kernelNumber) {
		this.kernelNumber = kernelNumber;
	}

	public void setDeviceBuildNumber(String deviceBuildNumber) {
		this.deviceBuildNumber = deviceBuildNumber;
	}

	public void setDeviceMakeId(Integer deviceMakeId) {
		this.deviceMakeId = deviceMakeId;
	}

	public void setDeviceMake(String deviceMake) {
		this.deviceMake = deviceMake;
	}

	public void setScreenResolutionX(Integer screenResolutionX) {
		this.screenResolutionX = screenResolutionX;
	}

	public void setScreenResolutionY(Integer screenResolutionY) {
		this.screenResolutionY = screenResolutionY;
	}
    
	
	
}
