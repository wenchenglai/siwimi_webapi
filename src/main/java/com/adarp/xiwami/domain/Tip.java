package com.adarp.xiwami.domain;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Tip")
public class Tip implements Comparable<Tip>{

	@Id
	private String id;

	private String creator;
	private String title;
	private String description;
	private String url;
	private Date createdDate;
	private Date expiredDate;
	private String type;
	private int viewCount;
	
	private String city;
	private String state;
	private String zipCode;
	private double[] location;
	
	@Transient
	private int voteUp = 0;
	
	@Transient
	private int voteDown = 0;
	
	@Transient
	private boolean isFavorite = false;
	
	// The default of the below field is set by backend
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Date getExpiredDate() {
		return expiredDate;
	}

	public void setExpiredDate(Date expiredDate) {
		this.expiredDate = expiredDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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
	
	public double[] getLocation() {
		return location;
	}

	public void setLocation(double[] location) {
		this.location = location;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public int getViewCount() {
		return viewCount;
	}

	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}

	public int getVoteUp() {
		return voteUp;
	}

	public void setVoteUp(int voteUp) {
		this.voteUp = voteUp;
	}

	public int getVoteDown() {
		return voteDown;
	}

	public void setVoteDown(int voteDown) {
		this.voteDown = voteDown;
	}
	
	public boolean getIsFavorite() {
		return isFavorite;
	}

	public void setIsFavorite(boolean isFavorite) {
		this.isFavorite = isFavorite;
	}
	
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Override
	public int compareTo(Tip compareTip) {
		int compareVoteUp = ((Tip)compareTip).getVoteUp();
		return compareVoteUp - this.voteUp;
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
	    
	    Tip other = (Tip) obj;
	    if (!this.getId().equals(other.getId()))
	          return false;
	       
	    return true;
	}
}
