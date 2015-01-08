package com.adarp.xiwami.domain;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="Message")
public class Message {

	@Id
	private String id;
	
	private String from; // entity ID points to a member
	private String to;  // entity ID points to a member
	private String subject;
	private String body;
	private Date createdDate;
	private String rootMessage; // an entity Id that points to the first message in a thread
	private String fromStatus; // draft, sent
	private String toStatus; // unread, read, trash
	
	//The default of the below field is set by backend
	private Boolean isDestroyed;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getRootMessage() {
		return rootMessage;
	}

	public void setRootMessage(String rootMessage) {
		this.rootMessage = rootMessage;
	}

	public String getFromStatus() {
		return fromStatus;
	}

	public void setFromStatus(String fromStatus) {
		this.fromStatus = fromStatus;
	}

	public String getToStatus() {
		return toStatus;
	}

	public void setToStatus(String toStatus) {
		this.toStatus = toStatus;
	}

	public Boolean getIsDestroyed() {
		return isDestroyed;
	}

	public void setIsDestroyed(Boolean isDestroyed) {
		this.isDestroyed = isDestroyed;
	}
}
