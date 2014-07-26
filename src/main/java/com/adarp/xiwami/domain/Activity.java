package com.adarp.xiwami.domain;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="Activity")
public class Activity {

	@Id
	private String id;
	
	private String creator;
	private String title;
	private String description;
	private Date fromTime;
	private Date toTime;
	private String location;
	private String url;
	private String imageData;
	private String imageUrl;
	private String type;
	
	//This field is only for front-end purpose
	@Transient
	private String status = "Upcoming";
	
	//The default of the below field is set by backend
	private Boolean isDeleted;
	
	public Activity() {

		// this.status : this is for front-end only
		if (this.fromTime != null) {
			Date today = new Date();
			if (today.after(this.fromTime))
				this.status = "Past";
			else
				this.status = "Upcoming";							
		} 
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
	
//	public String getFromTime() {
//		if (this.fromTime != null) {
//			SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
//			return formatter.format(this.fromTime);			
//		} else {
//			return null;
//		}
//	}
//
//	public void setFromTime(String fromTime) {
//		if (fromTime != null) {
//			SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd"); 
//			Date fromTimeDate = new Date();
//			try {	 
//				fromTimeDate = formatter.parse(fromTime);
//			} catch (ParseException e) {
//				e.printStackTrace();
//			}
//			this.fromTime = fromTimeDate;
//			
//			// This is for front-end only
//			Date today = new Date();
//			if (today.after(this.fromTime))
//				this.status = "Past";
//			else
//				this.status = "Upcoming";							
//		} else {
//			this.fromTime = null;
//			this.status = null;
//		}						
//	}
//
//	public String getToTime() {
//		if (this.toTime != null) {
//			SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
//			return formatter.format(this.toTime);			
//		} else {
//			return null;
//		}
//	}
//
//	public void setToTime(String toTime) {
//		if (toTime != null) {
//			SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd"); 
//			Date toTimeDate = new Date();
//			try {	 
//				toTimeDate = formatter.parse(toTime);
//			} catch (ParseException e) {
//				e.printStackTrace();
//			}
//			this.toTime = toTimeDate; 			
//		} else {
//			this.toTime = null;
//		}
//	}

	public String getLocation() {
		return location;
	}
	
	public Date getFromTime() {
		return fromTime;
	}

	public void setFromTime(Date fromTime) {
		this.fromTime = fromTime;
	}

	public Date getToTime() {
		return toTime;
	}

	public void setToTime(Date toTime) {
		this.toTime = toTime;
	}

	public String getImageData() {
		return imageData;
	}

	public void setImageData(String imageData) {
		this.imageData = imageData;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getImageUrl() {
		return imageUrl;
	}
	
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}	
}
