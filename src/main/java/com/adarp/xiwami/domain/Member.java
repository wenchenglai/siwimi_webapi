package com.adarp.xiwami.domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="Member")
public class Member {
	
	@Id
	private String id; 
	
	private String lastName;
	private String firstName;
	private String nickName;
	private String email;
	private String password;
	private Date birthday;
	private List<String> languages = new ArrayList<String>();
	private String type;
	private String gender;
	private String avatarUrl;
	private String largePicture;
	private String smallPicture;
	private String family;	
	private boolean isUser;
	private String facebookId;
//	private List<String> feducation;
	private String fhometown;
	private String flink;
	private String flocale;
	private String flocation;
	private int ftimezone;
	
//	private List<String> items = new ArrayList<String>();
	
	//The default of the below field is set by backend
	private boolean isDeleted;
	
	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
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
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}	

	public String getBirthday() {
		if (this.birthday != null) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
			return formatter.format(this.birthday);			
		} else {
			return null;
		}

	}

	public void setBirthday(String birthday) {
		if (birthday != null) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd"); 
			Date birthdayDate = new Date();
			try {	 
				birthdayDate = formatter.parse(birthday);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			this.birthday = birthdayDate; 			
		} else {
			this.birthday = null;
		}
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
	
	public String getFamily() {
		return family;
	}

	public void setFamily(String familyId) {
		this.family = familyId;
	}

	public String getLargePicture() {
		return largePicture;
	}

	public void setLargePicture(String largePicture) {
		this.largePicture = largePicture;
	}

	public String getSmallPicture() {
		return smallPicture;
	}

	public void setSmallPicture(String smallPicture) {
		this.smallPicture = smallPicture;
	}

//	public List<String> getFeducation() {
//		return feducation;
//	}
//
//	public void setFeducation(List<String> feducation) {
//		this.feducation = feducation;
//	}

	public String getFhometown() {
		return fhometown;
	}

	public void setFhometown(String fhometown) {
		this.fhometown = fhometown;
	}

	public String getFlink() {
		return flink;
	}

	public void setFlink(String flink) {
		this.flink = flink;
	}

	public String getFlocale() {
		return flocale;
	}

	public void setFlocale(String flocale) {
		this.flocale = flocale;
	}

	public String getFlocation() {
		return flocation;
	}

	public void setFlocation(String flocation) {
		this.flocation = flocation;
	}

	public int getFtimezone() {
		return ftimezone;
	}

	public void setFtimezone(int ftimezone) {
		this.ftimezone = ftimezone;
	}

	public boolean isIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

//	public List<String> getItems() {
//		return items;
//	}
//
//	public void setItems(List<String> items) {
//		this.items = items;
//	}		
}
