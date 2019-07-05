/**
 * 
 */
package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author silambarasur
 *
 */
@Entity
@Table(name="tool_intagration_master")
public class ToolIntagrationMaster implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String name;
	private String version;
	private ToolTypeMaster toolType;
	private String description;
	private Integer status;
	//private UserList createdBy;
	//private UserList modifiedBy;
	/*private Date createdOn;
	private Date modifiedOn;*/
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@Column(name="name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn (name = "tooltypeid")
	public ToolTypeMaster getToolType() {
		return toolType;
	}
	/*@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn (name = "modifiedby")
	public UserList getModifiedBy() {
		return modifiedBy;
	}*/
	public void setToolType(ToolTypeMaster toolTypeId) {
		this.toolType = toolTypeId;
	}
	@Column(name="description")
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Column(name="status")
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	/*@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn (name = "createdby")
	public UserList getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(UserList createdBy) {
		this.createdBy = createdBy;
	}*/
	
	/*@Column(name="createdon")
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	@Column(name="modifiedon")
	public Date getModifiedOn() {
		return modifiedOn;
	}
	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}*/
	@Column(name="version")
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	/*public void setModifiedBy(UserList modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	*/

	
}
