package com.adarp.xiwami.domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

public class Member {
	@Id
	private ObjectId id; 
	
	private String lastName;
	private String firstName;
	private String nickName;
	private String email;
	private Date birthday;
	private List<String> languages = new ArrayList<String>();
	private String type;
	private String gender;
	private String avatarUrl;
	private boolean isUser;
	private String facebookId;	
	private List<Item> items = new ArrayList<Item>();
	private String family;
	
	public Member() {
		id = new ObjectId();
	}
	
	public void setId(String id) {
		this.id = new ObjectId(id);
	}

	public String getId() {
		return id.toString();
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getBirthday() {		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd"); 
		return formatter.format(birthday);
		//return "1980/1/1";
	}

	public void setBirthday(String birthday) {		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd"); 
		Date birthdayDate = new Date();
		try {	 
			birthdayDate = formatter.parse(birthday);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		this.birthday = birthdayDate; 
	}

	public List<String> getLanguages() {
		return languages;
	}

	public void setLanguages (ArrayList<String> myLanguage) {	
		languages = myLanguage;	
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}
	
	public boolean getIsUser() {
		return isUser;
	}

	public void setIsUser(boolean isUser) {
		this.isUser = isUser;
	}	
	
	public String getFacebookId() {
		return facebookId;
	}

	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}	

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}
	
	public String getFamily() {
		return family;
	}

	public void setFamily(String familyId) {
		this.family = familyId;
	}		
}
