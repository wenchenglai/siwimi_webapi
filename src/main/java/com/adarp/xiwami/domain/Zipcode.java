package com.adarp.xiwami.domain;

public class Zipcode {

	private String countryCode;
	private String township;
	private String state;
	private String stateCode;
	private String county;
	private String countyCode;
	
	private double latitude;
	private double longitude;
	
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
	
}
