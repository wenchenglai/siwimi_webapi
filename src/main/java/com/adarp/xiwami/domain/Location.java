package com.adarp.xiwami.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Document(collection = "Location")
public class Location {

	@Id
	private String id;
	
	private String countryCode;
	private String zipCode;
	private String township;
	private String state;
	private String stateCode;
	private String county;
	private String countyCode;
	
	private double longitude;
	private double latitude;
	
	public String getId() {
		return id;
	}	
	
	@JsonIgnore
	public String getCountryCode() {
		return countryCode;
	}
	
	public String getTownship() {
		return township;
	}
	
	@JsonIgnore
	public String getState() {
		return state;
	}
	
	public String getStateCode() {
		return stateCode;
	}
	
	@JsonIgnore
	public String getCounty() {
		return county;
	}
	
	@JsonIgnore
	public String getCountyCode() {
		return countyCode;
	}
	
	@JsonIgnore
	public double getLatitude() {
		return latitude;
	}

	@JsonIgnore
	public double getLongitude() {
		return longitude;
	}
	
	@JsonIgnore
	public String getZipCode() {
		if (zipCode != null) {
			if (Integer.parseInt(zipCode)<10000)
				zipCode = "0"+zipCode;	
		}
		return zipCode;
	}
	
	@Override
	public int hashCode() {
		if (township == null)
			return id.hashCode();
		else
			return township.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
	    if (obj == null)
	        return false;
	    if (getClass() != obj.getClass())
	        return false;
	    
	    if (this.getTownship() == null)
	    	return false;
	    if (this.getState() == null)
	    	return false;
	    if (this.getStateCode() == null)
	    	return false;

	    Location other = (Location) obj;
	    if (other.getTownship() == null)
	    	return false;
	    if (other.getState() == null)
	    	return false;
	    if (other.getStateCode() == null)
	    	return false;
	    
	    if (  !((this.getTownship().equals(other.getTownship())) && 
	    		(this.getState().equals(other.getState())) && 
	    		(this.getStateCode().equals(other.getStateCode())))  
	    		)
	       return false;
	    
	    return true;
	}
}
