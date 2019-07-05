/**
 * 
 */
package com.hcl.atf.taf.model.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.EntityMaster;
import com.hcl.atf.taf.model.NotificationMaster;


/**
 * @author silambarasur
 * 
 */
public class JsonNotification {
	
	@JsonProperty
	private Integer notificationId;
	@JsonProperty
	private String notificationName;
	@JsonProperty
	private String notificationGroup;
	@JsonProperty
	private String description;
	@JsonProperty
	private String defaultPrimaryRecipients;
	@JsonProperty
	private String defaultSecondaryRecipients;
	@JsonProperty
	private Integer activeState;
	
	@JsonProperty
	private Integer entityTypeId;
	@JsonProperty
	private String entityTypeName;
	
	public JsonNotification() {
		
	}
	
	public JsonNotification(NotificationMaster notificationMaster) {
		this.notificationId = notificationMaster.getNotificationId();
		this.notificationName = notificationMaster.getNotificationName();
		this.notificationGroup = notificationMaster.getNotificationGroup();
		this.description = notificationMaster.getDescription();
		this.defaultPrimaryRecipients = notificationMaster.getDefaultPrimaryRecipients();
		this.defaultSecondaryRecipients = notificationMaster.getDefaultSecondaryRecipients();
		if(notificationMaster != null && notificationMaster.getEntity() !=null){
			this.entityTypeId=notificationMaster.getEntity().getEntitymasterid();
			this.entityTypeName = notificationMaster.getEntity().getEntitymastername();
		}
		this.activeState = notificationMaster.getActiveState();
	}
	
	@JsonIgnore
	public NotificationMaster getNotificationMaster() {
		
		NotificationMaster notification= new NotificationMaster();
		EntityMaster entity = new EntityMaster();
		notification.setNotificationId(notificationId);
		notification.setNotificationName(notificationName);
		notification.setNotificationGroup(notificationGroup);
		notification.setDescription(description);
		notification.setActiveState(activeState);
		notification.setDefaultPrimaryRecipients(defaultPrimaryRecipients);
		notification.setDefaultSecondaryRecipients(defaultSecondaryRecipients);
		if(this.entityTypeId != null ) {
			entity.setEntitymasterid(entityTypeId);
			notification.setEntity(entity);
		}
		
		return notification;
		
	}
	
	public Integer getNotificationId() {
		return notificationId;
	}
	public String getNotificationName() {
		return notificationName;
	}
	public String getNotificationGroup() {
		return notificationGroup;
	}
	public String getDescription() {
		return description;
	}
	public String getDefaultPrimaryRecipients() {
		return defaultPrimaryRecipients;
	}
	public String getDefaultSecondaryRecipients() {
		return defaultSecondaryRecipients;
	}
	public Integer getActiveState() {
		return activeState;
	}

	public Integer getEntityTypeId() {
		return entityTypeId;
	}
	public String getEntityTypeName() {
		return entityTypeName;
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
	public void setDescription(String description) {
		this.description = description;
	}
	public void setDefaultPrimaryRecipients(String defaultPrimaryRecipients) {
		this.defaultPrimaryRecipients = defaultPrimaryRecipients;
	}
	public void setDefaultSecondaryRecipients(String defaultSecondaryRecipients) {
		this.defaultSecondaryRecipients = defaultSecondaryRecipients;
	}
	public void setActiveState(Integer activeState) {
		this.activeState = activeState;
	}

	public void setEntityTypeId(Integer entityTypeId) {
		this.entityTypeId = entityTypeId;
	}
	public void setEntityTypeName(String entityTypeName) {
		this.entityTypeName = entityTypeName;
	}
	
	
	
}
