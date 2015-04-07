package com.siwimi.webapi.domain;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="Email")
public class Email {

	@Id
	private String id;
	
	public Email() {
		this.sentFrom = "admin@siwami.com";
	}
	
	private String sentFrom;
	private List<String> sentTo = new ArrayList<String>();
	private String subject;
	private String emailText;

	//The default of the below field is set by backend
	private Boolean isDestroyed;
		
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
	
	public Boolean getIsDestroyed() {
		return isDestroyed;
	}

	public void setIsDestroyed(Boolean isDestroyed) {
		this.isDestroyed = isDestroyed;
	}
}
