package com.adarp.xiwami.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="Question")
public class Question {

	@Id
	private String _id;
	
	private String userId;
	private String question;
	private String isAnswered;
	
	//The default of the below field is set by backend
	private Boolean isDeleted;
	
	public void set_Id(String id) {
		this._id = id;
	}

	public String get_Id() {
		return _id.toString();
	}
	
	public String getUserId() {
		return userId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getQuestion() {
		return question;
	}
	
	public void setQuestion(String question) {
		this.question = question;
	}
		
	public String getIsAnswered() {
		return isAnswered;
	}
	
	public void setIsAnswered(String isAnswered) {
		this.isAnswered = isAnswered;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
}
