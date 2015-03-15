package com.adarp.xiwami.domain;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "ZipCode")
public class ZipCode {

	private String countryCode;
	private String zipCode;
	private String township;
	private String state;
	private String stateCode;
	private String county;
	private String countyCode;
	
	private double longitude;
	private double latitude;
	
	public String getCountryCode() {
		return countryCode;
	}
	public String getTownship() {
		return township;
	}
	public String getState() {
		return state;
	}
	public String getStateCode() {
		return stateCode;
	}
	public String getCounty() {
		return county;
	}
	
	public String getCountyCode() {
		return countyCode;
	}
	
	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}
	public String getZipCode() {
		return zipCode;
	}
}
