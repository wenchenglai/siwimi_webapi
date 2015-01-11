package com.adarp.xiwami.domain;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="Discussion")
public class Discussion {

	@Id
	private String id;
	
	private String creator;
	private String entity;
	private String entityType;
	private String description;
	private ArrayList<Discussion> comments;
	private Date createdDate;
	private int likeCount;
	private int viewCount;
	
	//The default of the below field is set by backend
	private Boolean isDestroyed;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}	

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	public Boolean getIsDestroyed() {
		return isDestroyed;
	}

	public void setIsDestroyed(Boolean isDestroyed) {
		this.isDestroyed = isDestroyed;
	}

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public int getLike() {
		return likeCount;
	}

	public void setLike(int like) {
		this.likeCount = like;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public int getViewCount() {
		return viewCount;
	}

	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}

	public ArrayList<Discussion> getComments() {
		return comments;
	}

	public void setComments(ArrayList<Discussion> comments) {
		this.comments = comments;
	}
}
