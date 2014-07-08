package com.adarp.xiwami.domain;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Family")
public class Family {
	
	@Id
	private String _id;
	
	private String zipcode;
	private String cityState;
	private String familyName;
	private String description;	
	private List<String> members = new ArrayList<String>();
	
	private double[] location;
	

	public String get_Id() {
		return _id;
	}
	
	public void set_Id(String id) {
		this._id = id;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	
	public String getCityState() {
		return cityState;
	}

	public void setCityState(String cityState) {
		this.cityState = cityState;
	}	

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
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

	public void setMembers(List<String> member) {
		this.members = member;
	}

	public double[] getLocation() {
		return location;
	}

	public void setLocation(double[] location) {
		this.location = location;
	}
}
