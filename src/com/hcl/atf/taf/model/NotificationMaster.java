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
@Table(name="notification_master")
public class NotificationMaster implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer notificationId;
	private String notificationName;
	private String notificationGroup;
	private String description;
	private String defaultPrimaryRecipients;
	private String defaultSecondaryRecipients;
	private UserList createdBy;
	private Date createdDate;
	private UserList modifiedBy;
	private Date modifiedDate;
	private Integer activeState;
	private EntityMaster entity;
	
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "notificationId", unique = true, nullable = false)
	public Integer getNotificationId() {
		return notificationId;
	}
	
	@Column(name="notificationName")
	public String getNotificationName() {
		return notificationName;
	}
	@Column(name="notificationGroup")
	public String getNotificationGroup() {
		return notificationGroup;
	}
	@Column(name="defaultPrimaryRecipients")
	public String getDefaultPrimaryRecipients() {
		return defaultPrimaryRecipients;
	}
	@Column(name="defaultSecondaryRecipients")
	public String getDefaultSecondaryRecipients() {
		return defaultSecondaryRecipients;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "createdBy")
	public UserList getCreatedBy() {
		return createdBy;
	}
	@Column(name="createdDate")
	public Date getCreatedDate() {
		return createdDate;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "modifiedBy")
	public UserList getModifiedBy() {
		return modifiedBy;
	}
	
	@Column(name="modifiedDate")
	public Date getModifiedDate() {
		return modifiedDate;
	}
	@Column(name="activeState")
	public Integer getActiveState() {
		return activeState;
	}
	
	@Column(name="description")
	public String getDescription() {
		return description;
	}
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityTypeId")
	public EntityMaster getEntity() {
		return entity;
	}

	public void setEntity(EntityMaster entity) {
		this.entity = entity;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setNotificationId(Integer notificationId) {
		this.notificationId = notificationId;
	}
	public void setNotificationName(String notificationName) {
		this.notificationName = notificationName;
	}
	public void setNotificationGroup(String notificationGroup) {
		this.notificationGroup = notificationGroup;
	}
	public void setDefaultPrimaryRecipients(String defaultPrimaryRecipients) {
		this.defaultPrimaryRecipients = defaultPrimaryRecipients;
	}
	public void setDefaultSecondaryRecipients(String defaultSecondaryRecipients) {
		this.defaultSecondaryRecipients = defaultSecondaryRecipients;
	}
	public void setCreatedBy(UserList createdBy) {
		this.createdBy = createdBy;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public void setModifiedBy(UserList modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public void setActiveState(Integer activeState) {
		this.activeState = activeState;
	}
}
