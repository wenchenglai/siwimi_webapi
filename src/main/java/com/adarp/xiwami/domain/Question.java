package com.adarp.xiwami.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="Question")
public class Question {

	@Id
	private String id;
	
	private String user;
	private String questionText;
	private Date createdDate;
	private List<String> answers = new ArrayList<String>();	
	
	//This field is only for front-end purpose
	@Transient
	private String status = "Open";
	
	//The default of the below field is set by backend
	private Boolean isDeleted;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getQuestionText() {
		return questionText;
	}

	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}

//	public String getCreatedDate() {
//		if (this.createdDate != null) {
//			SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
//			return formatter.format(this.createdDate);			
//		} else {
//			return null;
//		}
//	}
//
//	public void setCreatedDate(String createdDate) {
//		if (createdDate != null) {
//			SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd"); 
//			Date createdDateDate = new Date();
//			try {	 
//				createdDateDate = formatter.parse(createdDate);
//			} catch (ParseException e) {
//				e.printStackTrace();
//			}
//			this.createdDate = createdDateDate; 			
//		} else {
//			this.createdDate = null;
//		}
//	}	

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public List<String> getAnswers() {
		return answers;
	}

	public void setAnswers(List<String> answers) {
		this.answers = answers;
		//This is only for front-end only
		if (this.answers.size()>0)
			this.status = "Answered";
		else
			this.status = "Open";
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	

}
