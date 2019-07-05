package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity @Table(name = "generic_devices")
@Inheritance(strategy= InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="deviceTypeId",discriminatorType=DiscriminatorType.INTEGER)  
@DiscriminatorValue(value="1")  
public class GenericDevices {
	private Integer genericsDevicesId;
	private String name;
	private String description;
	private Integer status;
	private DeviceLab deviceLab;
	private Date createdDate;
	private Date modifiedDate;
	private Integer availableStatus;
	private PlatformType platformType;
	private String UDID;
	private DeviceModelMaster deviceModelMaster;
	private TestFactory testFactory;
	private TestFactoryLab testFactoryLab;
	private ProductMaster productMaster;
	private HostList hostList;
	private Set<ProductMaster> productMasterSet=new HashSet<ProductMaster>(0);
	private Integer screenResolutionX;
	private Integer screenResolutionY;
	
	public  GenericDevices (){
	}
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "genericsDevicesId", unique = true, nullable = false)
	public Integer getGenericsDevicesId() {
		return genericsDevicesId;
	}
	public void setGenericsDevicesId(Integer genericsDevicesId) {
		this.genericsDevicesId = genericsDevicesId;
	}
	@Column(name = "name", length = 45)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column(name = "description", length = 45)
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Column(name = "status", length = 45)
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "deviceLabId") 
	public DeviceLab getDeviceLab() {
		return deviceLab;
	}
	public void setDeviceLab(DeviceLab deviceLab) {
		this.deviceLab = deviceLab;
	}
	@Column(name = "createdDate", length = 45)
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	@Column(name = "modifiedDate", length = 45)
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	@Column(name = "availableStatus", length = 45)
	public Integer getAvailableStatus() {
		return availableStatus;
	}
	public void setAvailableStatus(Integer availableStatus) {
		this.availableStatus = availableStatus;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "osId")
	public PlatformType getPlatformType() {
		return platformType;
	}
	public void setPlatformType(PlatformType platformType) {
		this.platformType = platformType;
	}
	@Column(name = "UDID", length = 45)
	public String getUDID() {
		return UDID;
	}
	public void setUDID(String uDID) {
		UDID = uDID;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "deviceModelId")
	public DeviceModelMaster getDeviceModelMaster() {
		return deviceModelMaster;
	}
	public void setDeviceModelMaster(DeviceModelMaster deviceModelMaster) {
		this.deviceModelMaster = deviceModelMaster;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "productId")
	public ProductMaster getProductMaster() {
		return productMaster;
	}
	public void setProductMaster(ProductMaster productMaster) {
		this.productMaster = productMaster;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "testFactoryId")
	public TestFactory getTestFactory() {
		return testFactory;
	}
	public void setTestFactory(TestFactory testFactory) {
		this.testFactory = testFactory;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "testFactoryLabId")
	public TestFactoryLab getTestFactoryLab() {
		return testFactoryLab;
	}
	public void setTestFactoryLab(TestFactoryLab testFactoryLab) {
		this.testFactoryLab = testFactoryLab;
	}
	@Override
	public boolean equals(Object o) {
		GenericDevices genericDevice = (GenericDevices) o;
		if (this.genericsDevicesId == genericDevice.getGenericsDevicesId()) {
			return true;
		}else{
			return false;
		}
	}
	
	@Override
	public int hashCode(){
	    return (int) genericsDevicesId;
	  }
		
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "hostId")
	public HostList getHostList() {
		return hostList;
	}
	public void setHostList(HostList hostList) {
		this.hostList = hostList;
	}
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "genericeDevices",cascade=CascadeType.ALL)
	public Set<ProductMaster> getProductMasterSet() {
		return productMasterSet;
	}
	public void setProductMasterSet(Set<ProductMaster> productMasterSet) {
		this.productMasterSet = productMasterSet;
	}
	@Transient
	public String getDecriminatorValue() {
	    return this.getClass().getAnnotation(DiscriminatorValue.class).value();	    
	}
	@Column(name = "screenResolutionX", length = 45)
	public Integer getScreenResolutionX() {
		return screenResolutionX;
	}
	public void setScreenResolutionX(Integer screenResolutionX) {
		this.screenResolutionX = screenResolutionX;
	}
	@Column(name = "screenResolutionY", length = 45)
	public Integer getScreenResolutionY() {
		return screenResolutionY;
	}
	public void setScreenResolutionY(Integer screenResolutionY) {
		this.screenResolutionY = screenResolutionY;
	}
	
}
