package com.siwimi.webapi.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="Email")
public class Email {

	@Id
	private String id;
	
	public Email() {
		this.sentFrom = "The Siwimi Team <customer@siwimi.com>";
	}
	
	private String sentFrom;
	private List<String> sentTo = new ArrayList<String>();
	private String subject;
	private String emailText;
	private Date sentTime;
	
	//The default of the below field is set by backend
	private Boolean isDeletedRecord;
		
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getSentFrom() {
		return sentFrom;
	}
	
	public void setSentFrom(String sentFrom) {
		this.sentFrom = sentFrom;
	}
	
	public List<String> getSentTo() {
		return sentTo;
	}
	
	public void setSentTo(List<String> sentTo) {
		this.sentTo = sentTo;
	}
	
	public String getSubject() {
		return subject;
	}
	
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public String getEmailText() {
		return emailText;
	}
	
	public void setEmailText(String emailText) {
		this.emailText = emailText;
	}	
	
	public Boolean getIsDeletedRecord() {
		return isDeletedRecord;
	}

	public void setIsDeletedRecord(Boolean isDeletedRecord) {
		this.isDeletedRecord = isDeletedRecord;
	}

	public Date getSentTime() {
		return sentTime;
	}

	public void setSentTime(Date sentTime) {
		this.sentTime = sentTime;
	}
	
	
}