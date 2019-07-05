package com.hcl.atf.taf.model;

// Generated Feb 4, 2014 4:30:16 PM by Hibernate Tools 3.4.0.CR1

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * HostTypeMaster generated by hbm2java
 */
@Entity
@Table(name = "host_type_master")
public class HostTypeMaster implements java.io.Serializable {

	private String hostType;
	private Set<HostList> hostLists = new HashSet<HostList>(0);

	public HostTypeMaster() {
	}

	public HostTypeMaster(String hostType) {
		this.hostType = hostType;
	}

	public HostTypeMaster(String hostType, Set<HostList> hostLists) {
		this.hostType = hostType;
		this.hostLists = hostLists;
	}

	@Id
	@Column(name = "hostType", unique = true, nullable = false, length = 10)
	public String getHostType() {
		return this.hostType;
	}

	public void setHostType(String hostType) {
		this.hostType = hostType;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "hostTypeMaster")
	public Set<HostList> getHostLists() {
		return this.hostLists;
	}

	public void setHostLists(Set<HostList> hostLists) {
		this.hostLists = hostLists;
	}

}