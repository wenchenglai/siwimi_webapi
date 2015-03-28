package com.adarp.xiwami.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Group")
public class Group {

	@Id
	private String id;
	
	private String creator;
	private String title;
	private String description;
	private Date createdDate;
	
	// This field is only for front-end use
	@Transient
	private List<String> members = new ArrayList<String>();
	
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<String> getMembers() {
		return members;
	}

	public void setMembers(List<String> members) {
		this.members = members;
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
	
	@Override
	public int hashCode() {
	    return id.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
	    if (obj == null)
	        return false;
	    if (getClass() != obj.getClass())
	        return false;
	    
	    Family other = (Family) obj;
	    if (!this.getId().equals(other.getId()))
	          return false;
	       
	    return true;
	}
}
