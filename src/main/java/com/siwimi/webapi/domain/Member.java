package com.siwimi.webapi.domain;

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
	private String imageData;
	private String family;	
	
	// privilege is the software application user authorization level
	// by default, this is 0.  256 is Admin
	// 0: Anonymous User, 1: User, 2: Super User, 4: Content Editor, 8: Reserved, 16: Reserved, 32: Reserved, 64: Reserved, 128: Reserved, 256: Admin 
	private int privilege;
	
    // used to see if current user is considered a regular user with confirmed account.
    // TODO: might be able to merge this field to the above privilege	
	private boolean isUser;	
	
    // TODO: role should be merged with the privilege field, privilege better reflects the application authroization level, while role could be used for Father, Mother etc
    // authorization [admin, user, anonymous]	
	private String role;	
	
	private String facebookId;
	private String highSchool;
	private String college;
	private String fhometown;
	private String flink;
	private String flocale;
	private String flocation;
	private int ftimezone;
	private String zipCode;
	private String city;
	private String state;
	private String invitedBy;
	private String notification;
	private double[] location;
	
	// features
	private List<String> toys = new ArrayList<String>();
	private List<String> needs = new ArrayList<String>();
	
	//The default of the below field is set by back end
	private boolean isDeletedRecord = false;
	
	// need email confirmation from the user,
	private boolean isConfirmedMember = false;
	
	// need to know if this user is still in the process of signing up
	private boolean isInSignUpProcess = true;

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

	public Date getBirthday() {
		return birthday;
	}	
	
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public void setLanguages (ArrayList<String> myLanguage) {	
		languages = myLanguage;	
	}	
	
	public List<String> getLanguages() {
		return languages;
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
	
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
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

	public String getImageData() {
		return imageData;
	}

	public void setImageData(String imageData) {
		this.imageData = imageData;
	}

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

	public Boolean getIsDeletedRecord() {
		return isDeletedRecord;
	}

	public void setIsDeletedRecord(Boolean isDeletedRecord) {
		this.isDeletedRecord = isDeletedRecord;
	}

	public String getHighSchool() {
		return highSchool;
	}

	public void setHighSchool(String highSchool) {
		this.highSchool = highSchool;
	}

	public String getCollege() {
		return college;
	}

	public void setCollege(String college) {
		this.college = college;
	}

	public String getZipCode() {
		return zipCode;
	}

	// ZipCode stored in MongoDB could only have 4-digit
	public void setZipCode(String zipCode) {
		if (zipCode != null) {
			if (Integer.parseInt(zipCode)<10000)
				zipCode = "0"+zipCode;	
		}
		this.zipCode = zipCode;
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
	
	public double[] getLocation() {
		return location;
	}

	public void setLocation(double[] location) {
		this.location = location;
	}
	
	public boolean getIsConfirmedMember() {
		return isConfirmedMember;
	}

	public void setIsConfirmedMember(boolean isConfirmedMember) {
		this.isConfirmedMember = isConfirmedMember;
	}
	
	public String getInvitedBy() {
		return invitedBy;
	}

	public void setInvitedBy(String invitedBy) {
		this.invitedBy = invitedBy;
	}

	public String getNotification() {
		return notification;
	}

	public void setNotification(String notification) {
		this.notification = notification;
	}

	public boolean getIsInSignUpProcess() {
		return isInSignUpProcess;
	}

	public void setIsInSignUpProcess(boolean isInSignUpProcess) {
		this.isInSignUpProcess = isInSignUpProcess;
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
	    
	    Member other = (Member) obj;
	    if (!this.getId().equals(other.getId()))
	          return false;
	       
	    return true;
	}

	public List<String> getToys() {
		return toys;
	}

	public void setToys(List<String> toys) {
		this.toys = toys;
	}

	public List<String> getNeeds() {
		return needs;
	}

	public void setNeeds(List<String> needs) {
		this.needs = needs;
	}

	public int getPrivilege() {
		return privilege;
	}

	public void setPrivilege(int privilege) {
		this.privilege = privilege;
	}
}
