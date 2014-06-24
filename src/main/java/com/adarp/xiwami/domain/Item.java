package com.adarp.xiwami.domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Item {
//	@Id
//	private ObjectId _id;
	
	private int id;
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
	private Date createdDate;

//	public Item() {
//		_id = new ObjectId();
//	}
//	
//	public void set_id(String _id) {
//		this._id = ObjectId.massageToObjectId(_id);
//	}
//
//	public String get_id() {
//		return _id.toString();
//	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
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

	public String getCreatedDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd"); 
		return formatter.format(createdDate);
	}

	public void setCreatedDate(String createdDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd"); 
		try {	 
			Date mycreatedDate = formatter.parse(createdDate);
			this.createdDate = mycreatedDate; 
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
