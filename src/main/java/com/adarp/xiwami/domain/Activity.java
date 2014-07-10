package com.adarp.xiwami.domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="Activity")
public class Activity {

	@Id
	private String _id;
	
	private String creatorId;
	private String title;
	private String description;
	private Date fromTime;
	private Date toTime;
	private String location;
	private String category;
	private String originalLink;
	private String facebookEventUrl;
	private String imageUrl;
	//private Date createdDate;
	private String isDeleted;
	
	public void set_Id(String id) {
		this._id = id;
	}

	public String get_Id() {
		return _id.toString();
	}
	
	public String getCreatorId() {
		return creatorId;
	}
	
	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
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
	
	public String getFromTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); 
		return formatter.format(fromTime);
	}

	public void setFromTime(String fromTime) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); 
		try {	 
			Date myFromtime = formatter.parse(fromTime);
			this.fromTime = myFromtime; 
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public String getToTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); 
		return formatter.format(toTime);
	}

	public void setToTime(String toTime) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); 
		try {	 
			Date myTotime = formatter.parse(toTime);
			//System.out.println(myTotime);
			this.toTime = myTotime; 
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getOriginalLink() {
		return originalLink;
	}
	public void setOriginalLink(String originalLink) {
		this.originalLink = originalLink;
	}
	public String getFacebookEventUrl() {
		return facebookEventUrl;
	}
	public void setFacebookEventUrl(String facebookEventUrl) {
		this.facebookEventUrl = facebookEventUrl;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
//	public String getCreatedDate() {
//		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); 
//		return formatter.format(createdDate);
//	}
//
//	public void setCreatedDate(String createdDate) {
//		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); 
//		try {	 
//			Date mycreatedDate = formatter.parse(createdDate);
//			this.createdDate = mycreatedDate; 
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//	}
	
	public String getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}
	
	
	
}
