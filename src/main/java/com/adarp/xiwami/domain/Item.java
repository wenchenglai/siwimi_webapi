package com.adarp.xiwami.domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="Item")
public class Item {

	@Id
	private String id;
	
	private String name;
	private String description;
	private String size;
	private float width;
	private float length;
	private float height;
	private int fromAge;
	private int toAge;
	private String condition;
	private String type;
	private String status;
	private float price;
	private String imageUrl;
	private String imageData;
	private String seller;
	private String buyer;
	private Date createdDate;
	
	//The default of the below field is set by backend
	private Boolean isDeleted;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getLength() {
		return length;
	}

	public void setLength(float length) {
		this.length = length;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public int getFromAge() {
		return fromAge;
	}

	public void setFromAge(int fromAge) {
		this.fromAge = fromAge;
	}

	public int getToAge() {
		return toAge;
	}

	public void setToAge(int toAge) {
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

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getImageData() {
		return imageData;
	}

	public void setImageData(String imageData) {
		this.imageData = imageData;
	}

	public String getSeller() {
		return seller;
	}

	public void setSeller(String seller) {
		this.seller = seller;
	}

	public String getBuyer() {
		return buyer;
	}

	public void setBuyer(String buyer) {
		this.buyer = buyer;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	
	public String getCreatedDate() {
		if (this.createdDate != null) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
			return formatter.format(this.createdDate);			
		} else {
			return null;
		}
	}

	public void setCreatedDate(String createdDate) {
		if (createdDate != null) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd"); 
			Date createdDateDate = new Date();
			try {	 
				createdDateDate = formatter.parse(createdDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			this.createdDate = createdDateDate; 			
		} else {
			this.createdDate = null;
		}
	}	
}
