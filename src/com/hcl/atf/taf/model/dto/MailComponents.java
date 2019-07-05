package com.hcl.atf.taf.model.dto;

import javax.mail.internet.InternetAddress;

public class MailComponents {

	private  InternetAddress[]  toEmail;
	private InternetAddress fromEmail;
	private String subject;
	private String messageBody;
	private byte[] attachments;
	
	
	public InternetAddress[] getToEmail() {
		return toEmail;
	}
	public void setToEmail(InternetAddress[] toEmail) {
		this.toEmail = toEmail;
	}
	public InternetAddress getFromEmail() {
		return fromEmail;
	}
	public void setFromEmail(InternetAddress fromEmail) {
		this.fromEmail = fromEmail;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getMessageBody() {
		return messageBody;
	}
	public void setMessageBody(String messageBody) {
		this.messageBody = messageBody;
	}
	public byte[] getAttachments() {
		return attachments;
	}
	public void setAttachments(byte[] attachments) {
		this.attachments = attachments;
	}
	
	
}
