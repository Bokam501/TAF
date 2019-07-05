package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "ilcm_configuration_table")
public class IlcmConfigurationTable implements java.io.Serializable {

	private Integer Id;
	private String iLCMConfigurationPath;
	
	
	public IlcmConfigurationTable()
	{
		
	}
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "Id",unique = true, nullable = false)	
	public Integer getId() {
		return Id;
	}
	public void setId(Integer id) {
		Id = id;
	}
	
	@Column(name = "iLCMConfigurationPath")
	public String getiLCMConfigurationPath() {
		return iLCMConfigurationPath;
	}
	public void setiLCMConfigurationPath(String iLCMConfigurationPath) {
		this.iLCMConfigurationPath = iLCMConfigurationPath;
	}
	
	
}
