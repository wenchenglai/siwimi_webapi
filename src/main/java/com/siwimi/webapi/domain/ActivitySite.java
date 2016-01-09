package com.siwimi.webapi.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="ActivitySite")
public class ActivitySite {

	@Id
	private String id;
	
	private String siteName;
	private String url;
	private String className;
	private Boolean isActive;
	private Boolean isDeletedRecord = false;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	public Boolean getIsDeletedRecord() {
		return isDeletedRecord;
	}
	public void setIsDeletedRecord(Boolean isDeletedRecord) {
		this.isDeletedRecord = isDeletedRecord;
	}	
}
