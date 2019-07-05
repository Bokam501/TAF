package com.hcl.atf.taf.model;

// Generated Feb 4, 2014 4:30:16 PM by Hibernate Tools 3.4.0.CR1

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.CascadeType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * HostList generated by hbm2java
 */
@Entity
@Table(name = "host_list")
public class HostList implements java.io.Serializable {

	private Integer hostId;
	private CommonActiveStatusMaster commonActiveStatusMaster;
	private HostTypeMaster hostTypeMaster;
	private HostPlatformMaster hostPlatformMaster;
	private String hostName;
	private String hostIpAddress;
	private Set<DeviceList> deviceLists = new HashSet<DeviceList>(0);
	private Set<GenericDevices> genericDeviceLists = new HashSet<GenericDevices>(0);
	private Set<ProductMaster> productMasterSet=new HashSet<ProductMaster>(0);

	
	public HostList() {
	}

	public HostList(CommonActiveStatusMaster commonActiveStatusMaster,
			HostPlatformMaster hostPlatformMaster, String hostName,
			String hostIpAddress) {
		this.commonActiveStatusMaster = commonActiveStatusMaster;
		this.hostPlatformMaster = hostPlatformMaster;
		this.hostName = hostName;
		this.hostIpAddress = hostIpAddress;
	}

	public HostList(CommonActiveStatusMaster commonActiveStatusMaster,
			HostTypeMaster hostTypeMaster,
			HostPlatformMaster hostPlatformMaster, String hostName,
			String hostIpAddress, Set<DeviceList> deviceLists) {
		this.commonActiveStatusMaster = commonActiveStatusMaster;
		this.hostTypeMaster = hostTypeMaster;
		this.hostPlatformMaster = hostPlatformMaster;
		this.hostName = hostName;
		this.hostIpAddress = hostIpAddress;
		this.deviceLists = deviceLists;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "hostId", unique = true, nullable = false)
	public Integer getHostId() {
		return this.hostId;
	}

	public void setHostId(Integer hostId) {
		this.hostId = hostId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "hostStatus", nullable = false)
	public CommonActiveStatusMaster getCommonActiveStatusMaster() {
		return this.commonActiveStatusMaster;
	}

	public void setCommonActiveStatusMaster(
			CommonActiveStatusMaster commonActiveStatusMaster) {
		this.commonActiveStatusMaster = commonActiveStatusMaster;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "hostType")
	public HostTypeMaster getHostTypeMaster() {
		return this.hostTypeMaster;
	}

	public void setHostTypeMaster(HostTypeMaster hostTypeMaster) {
		this.hostTypeMaster = hostTypeMaster;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "hostPlatform", nullable = false)
	public HostPlatformMaster getHostPlatformMaster() {
		return this.hostPlatformMaster;
	}

	public void setHostPlatformMaster(HostPlatformMaster hostPlatformMaster) {
		this.hostPlatformMaster = hostPlatformMaster;
	}

	@Column(name = "hostName", nullable = false, length = 100)
	public String getHostName() {
		return this.hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	@Column(name = "hostIpAddress", nullable = false, length = 100)
	public String getHostIpAddress() {
		return this.hostIpAddress;
	}

	public void setHostIpAddress(String hostIpAddress) {
		this.hostIpAddress = hostIpAddress;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "hostList")
	public Set<DeviceList> getDeviceLists() {
		return this.deviceLists;
	}

	public void setDeviceLists(Set<DeviceList> deviceLists) {
		this.deviceLists = deviceLists;
	}

	@Override
	public boolean equals(Object o) {
		HostList hostList = (HostList) o;
		if(hostList!=null){
		if (this.hostId == hostList.getHostId()) {
			return true;
		}else{
			return false;
		}
	}
		return true;
	}

	@Override
	public int hashCode(){
	    return (int) hostId;
	  }
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "hostList")
	public Set<GenericDevices> getGenericDeviceLists() {
		return genericDeviceLists;
	}

	public void setGenericDeviceLists(Set<GenericDevices> genericDeviceLists) {
		this.genericDeviceLists = genericDeviceLists;
	}
	
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "hostLists",cascade=CascadeType.ALL)
	public Set<ProductMaster> getProductMasterSet() {
		return productMasterSet;
	}
	public void setProductMasterSet(Set<ProductMaster> productMasterSet) {
		this.productMasterSet = productMasterSet;
	}	
}