package com.siwimi.webapi.domain;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Follow")
public class Follow {
	@Id
	private String id;

	private String follower; // member Id
	private String followee; // member Id
	private Date createdDate;
	private Boolean isDeletedRecord;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFollower() {
		return follower;
	}
	public void setFollower(String follower) {
		this.follower = follower;
	}
	public String getFollowee() {
		return followee;
	}
	public void setFollowee(String followee) {
		this.followee = followee;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Boolean getIsDeletedRecord() {
		return isDeletedRecord;
	}
	public void setIsDeletedRecord(Boolean isDeletedRecord) {
		this.isDeletedRecord = isDeletedRecord;
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
	    
	    Follow other = (Follow) obj;
	    if (!this.getId().equals(other.getId()))
	          return false;
	       
	    return true;
	}
}
