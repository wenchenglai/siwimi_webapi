package com.adarp.xiwami.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="Item")
public class Item {
	
	@Id
	private String _id;
	
	private String name;
	private String description;
	private String size;
	private String width;
	private String height;
	private String fromAge;
	private String toAge;
	private String condition;
	private String type;
	private String status;
	private String imageUrl;
	//private Date createdDate;
	private String sellerId;
	private String buyerId;
	private String isDeleted;
	
	public void set_Id(String id) {
		this._id = id;
	}

	public String get_Id() {
		return _id.toString();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getFromAge() {
		return fromAge;
	}

	public void setFromAge(String fromAge) {
		this.fromAge = fromAge;
	}

	public String getToAge() {
		return toAge;
	}

	public void setToAge(String toAge) {
		this.toAge = toAge;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

/*	public String getCreatedDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); 
		return formatter.format(createdDate);
	}

	public void setCreatedDate(String createdDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); 
		try {	 
			Date mycreatedDate = formatter.parse(createdDate);
			this.createdDate = mycreatedDate; 
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}*/

	public String getSellerId() {
		return sellerId;
	}

	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}

	public String getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}

	public String getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}
	
	
}
