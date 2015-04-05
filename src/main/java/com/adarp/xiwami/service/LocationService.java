package com.adarp.xiwami.service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adarp.xiwami.domain.Location;
import com.adarp.xiwami.repository.LocationRepository;

@Service
public class LocationService {
	
	@Autowired
	private LocationRepository locationRep;
	
	public List<Location> queryFuzzyLocations(String queryText) {
		//List<Location> locations = locationRep.queryFuzzyLocations(queryText);	
		// Remove duplicated objects
		return new ArrayList<Location>(new LinkedHashSet<Location>(locationRep.queryFuzzyLocations(queryText)));
	}
}
