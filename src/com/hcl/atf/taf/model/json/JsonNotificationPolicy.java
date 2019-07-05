/**
 * 
 */
package com.hcl.atf.taf.model.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.NotificationMaster;
import com.hcl.atf.taf.model.NotificationPolicy;
import com.hcl.atf.taf.model.ProductMaster;

/**
 * @author silambarasur
 *
 */
public class JsonNotificationPolicy {
	
	@JsonProperty
	private Integer notificationPolicyId;
	
	@JsonProperty
	private Integer notificationId;
	@JsonProperty
	private String notificationName;
	@JsonProperty
	private Integer productId;
	@JsonProperty
	private String productName;
	@JsonProperty
	private String primaryRecipients;
	@JsonProperty
	private String defaultPrimaryRecipients;
	@JsonProperty
	private String secondaryRecipients;
	@JsonProperty
	private String defaultSecondaryRecipients;
	@JsonProperty
	private Integer email;
	@JsonProperty
	private Integer sms;
	@JsonProperty
	private Integer whatsapp;
	@JsonProperty
	private Integer twitter;
	@JsonProperty
	private Integer facebook;
	
	@JsonProperty
	private String entityTypeName;
	
	@JsonProperty
	private Integer entityTypeId;
	
	@JsonProperty
	private String notificationGroup;
	
	public JsonNotificationPolicy() {
		
	}
	
	public JsonNotificationPolicy(NotificationPolicy notificationPolicy) {
		this.notificationPolicyId = notificationPolicy.getNotificationPolicyId();
		if(notificationPolicy !=null && notificationPolicy.getProduct() !=null) {
			this.productId=notificationPolicy.getProduct().getProductId();
			this.productName=notificationPolicy.getProduct().getProductName();
		}
		this.primaryRecipients = notificationPolicy.getPrimaryRecipients();
		this.secondaryRecipients = notificationPolicy.getSecondaryRecipients();
		this.email = notificationPolicy.getEmail();
		this.sms = notificationPolicy.getSms();
		this.whatsapp = notificationPolicy.getWhatsapp();
		this.facebook = notificationPolicy.getFacebook();
		this.twitter = notificationPolicy.getTwitter();
		
		if(notificationPolicy !=null && notificationPolicy.getNotification() != null) {
			this.notificationId = notificationPolicy.getNotification().getNotificationId();
			this.notificationName = notificationPolicy.getNotification().getNotificationName();
			if(notificationPolicy.getNotification() != null && notificationPolicy.getNotification().getEntity() != null) {
				this.entityTypeId = notificationPolicy.getNotification().getEntity().getEntitymasterid();
				this.entityTypeName = notificationPolicy.getNotification().getEntity().getEntitymastername();
			}
			this.defaultPrimaryRecipients = notificationPolicy.getNotification().getDefaultPrimaryRecipients();
			this.defaultSecondaryRecipients = notificationPolicy.getNotification().getDefaultSecondaryRecipients();
			this.notificationGroup = notificationPolicy.getNotification().getNotificationGroup();
		}
	}
	
	
	@JsonIgnore
	public NotificationPolicy getNotificationPolicy() {
		
		NotificationPolicy notificationPolicy = new NotificationPolicy();
		NotificationMaster notification= new NotificationMaster();
		ProductMaster product= new ProductMaster();
		
		notificationPolicy.setNotificationPolicyId(notificationPolicyId);
		product.setProductId(productId);
		notificationPolicy.setProduct(product);
		notification.setNotificationId(notificationId);
		notificationPolicy.setNotification(notification);
		notificationPolicy.setEmail(email);
		notificationPolicy.setSms(sms);
		notificationPolicy.setWhatsapp(whatsapp);
		notificationPolicy.setFacebook(facebook);
		notificationPolicy.setTwitter(twitter);
		notificationPolicy.setPrimaryRecipients(primaryRecipients);
		notificationPolicy.setSecondaryRecipients(secondaryRecipients);
		
		return notificationPolicy;
		
	}
	public Integer getNotificationPolicyId() {
		return notificationPolicyId;
	}

	public Integer getNotificationId() {
		return notificationId;
	}

	public String getNotificationName() {
		return notificationName;
	}

	public Integer getProductId() {
		return productId;
	}

	public String getProductName() {
		return productName;
	}

	public String getPrimaryRecipients() {
		return primaryRecipients;
	}

	public String getDefaultPrimaryRecipients() {
		return defaultPrimaryRecipients;
	}

	public String getSecondaryRecipients() {
		return secondaryRecipients;
	}

	public String getDefaultSecondaryRecipients() {
		return defaultSecondaryRecipients;
	}

	

	public String getEntityTypeName() {
		return entityTypeName;
	}

	public Integer getEntityTypeId() {
		return entityTypeId;
	}

	public void setNotificationPolicyId(Integer notificationPolicyId) {
		this.notificationPolicyId = notificationPolicyId;
	}

	public void setNotificationId(Integer notificationId) {
		this.notificationId = notificationId;
	}

	public void setNotificationName(String notificationName) {
		this.notificationName = notificationName;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public void setPrimaryRecipients(String primaryRecipients) {
		this.primaryRecipients = primaryRecipients;
	}

	public void setDefaultPrimaryRecipients(String defaultPrimaryRecipients) {
		this.defaultPrimaryRecipients = defaultPrimaryRecipients;
	}

	public void setSecondaryRecipients(String secondaryRecipients) {
		this.secondaryRecipients = secondaryRecipients;
	}

	public void setDefaultSecondaryRecipients(String defaultSecondaryRecipients) {
		this.defaultSecondaryRecipients = defaultSecondaryRecipients;
	}

	
	public void setEntityTypeName(String entityTypeName) {
		this.entityTypeName = entityTypeName;
	}

	public void setEntityTypeId(Integer entityTypeId) {
		this.entityTypeId = entityTypeId;
	}

	public Integer getEmail() {
		return email;
	}

	public void setEmail(Integer email) {
		this.email = email;
	}

	public Integer getSms() {
		return sms;
	}

	public void setSms(Integer sms) {
		this.sms = sms;
	}

	public Integer getWhatsapp() {
		return whatsapp;
	}

	public void setWhatsapp(Integer whatsapp) {
		this.whatsapp = whatsapp;
	}

	public Integer getTwitter() {
		return twitter;
	}

	public void setTwitter(Integer twitter) {
		this.twitter = twitter;
	}

	public Integer getFacebook() {
		return facebook;
	}

	public void setFacebook(Integer facebook) {
		this.facebook = facebook;
	}

	public String getNotificationGroup() {
		return notificationGroup;
	}

	public void setNotificationGroup(String notificationGroup) {
		this.notificationGroup = notificationGroup;
	}
	
	
	

}
