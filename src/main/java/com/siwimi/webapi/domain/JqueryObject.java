package com.siwimi.webapi.domain;

// This is for jQuery library at ember.js
public class JqueryObject {
	
	private String label;
	private String value;
	
	public JqueryObject(String label, String value) {
		super();
		this.label = label;
		this.value = value;
	}
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
}
