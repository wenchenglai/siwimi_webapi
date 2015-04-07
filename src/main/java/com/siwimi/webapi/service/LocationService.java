package com.siwimi.webapi.service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.siwimi.webapi.domain.Location;
import com.siwimi.webapi.repository.LocationRepository;

@Service
public class LocationService {
	
	@Autowired
	private LocationRepository locationRep;
	
	public List<Location> queryFuzzyLocations(String queryText) {
		List<Location> locations = new ArrayList<Location>(new LinkedHashSet<Location>(locationRep.queryFuzzyLocations(queryText)));
		// only return the first five objects	
		return locations.subList(0, 5 > locations.size()? locations.size():5);
	}
}
