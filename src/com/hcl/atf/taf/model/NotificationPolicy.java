/**
 * 
 */
package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

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
@Table(name="notification_policy")
public class NotificationPolicy implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer notificationPolicyId;
	private ProductMaster product;
	private Integer email;
	private Integer sms;
	private Integer whatsapp;
	private Integer twitter;
	private Integer facebook;
	private NotificationMaster notification;
	private String primaryRecipients;
	private String secondaryRecipients;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "notificationPolicyId", unique = true, nullable = false)
	public Integer getNotificationPolicyId() {
		return notificationPolicyId;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "productId")
	public ProductMaster getProduct() {
		return product;
	}
	
	
	/*@Column(name="email")
	public boolean isEmail() {
		return email;
	}
	@Column(name="sms")
	public boolean isSms() {
		return sms;
	}
	@Column(name="whatsapp")
	public boolean isWhatsapp() {
		return whatsapp;
	}
	@Column(name="twitter")
	public boolean isTwitter() {
		return twitter;
	}
	@Column(name="facebook")
	public boolean isFacebook() {
		return facebook;
	}*/
	
	@Column(name="email")
	public Integer getEmail() {
		return email;
	}
	
	@Column(name="sms")
	public Integer getSms() {
		return sms;
	}
	@Column(name="whatsapp")
	public Integer getWhatsapp() {
		return whatsapp;
	}
	@Column(name="twitter")
	public Integer getTwitter() {
		return twitter;
	}
	
	public Integer getFacebook() {
		return facebook;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "notificationId", nullable = true)
	public NotificationMaster getNotification() {
		return notification;
	}

	@Column(name="primaryRecipients")
	public String getPrimaryRecipients() {
		return primaryRecipients;
	}
	@Column(name="secondaryRecipients")
	public String getSecondaryRecipients() {
		return secondaryRecipients;
	}

	public void setPrimaryRecipients(String primaryRecipients) {
		this.primaryRecipients = primaryRecipients;
	}

	public void setSecondaryRecipients(String secondaryRecipients) {
		this.secondaryRecipients = secondaryRecipients;
	}

	public void setNotification(NotificationMaster notification) {
		this.notification = notification;
	}


	public void setProduct(ProductMaster product) {
		this.product = product;
	}
	
	/*public void setEmail(boolean email) {
		this.email = email;
	}
	public void setSms(boolean sms) {
		this.sms = sms;
	}
	public void setWhatsapp(boolean whatsapp) {
		this.whatsapp = whatsapp;
	}
	public void setTwitter(boolean twitter) {
		this.twitter = twitter;
	}
	public void setFacebook(boolean facebook) {
		this.facebook = facebook;
	}*/
	
	public void setNotificationPolicyId(Integer notificationPolicyId) {
		this.notificationPolicyId = notificationPolicyId;
	}

	public void setEmail(Integer email) {
		this.email = email;
	}

	public void setSms(Integer sms) {
		this.sms = sms;
	}

	public void setWhatsapp(Integer whatsapp) {
		this.whatsapp = whatsapp;
	}

	public void setTwitter(Integer twitter) {
		this.twitter = twitter;
	}

	public void setFacebook(Integer facebook) {
		this.facebook = facebook;
	}
	
	
	
	
	
}
