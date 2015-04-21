package com.siwimi.webapi.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.siwimi.webapi.domain.JqueryObject;
import com.siwimi.webapi.domain.Location;
import com.siwimi.webapi.service.LocationService;

@RestController
public class LocationController {
	
	@Autowired
	private LocationService locationService;
	
	@RequestMapping(value = "/locations", method = RequestMethod.GET, produces = "application/json")
	public Map<String,List<Location>> findFuzzuLocations(@RequestParam(value="queryText", required=false) String queryText) {
		Map<String,List<Location>> responseBody = new HashMap<String,List<Location>>();
		List<Location> locations = locationService.queryFuzzyLocations(queryText);
		if (locations == null) 
			locations = new ArrayList<Location>();
		responseBody.put("locations", locations);
		return responseBody;
	}
	
	@RequestMapping(value = "/locationsjquery", method = RequestMethod.GET, produces = "application/json")
	public List<JqueryObject> findFuzzuLocationsForJquery(@RequestParam(value="queryText", required=false) String queryText) {
		List<Location> locations = locationService.queryFuzzyLocations(queryText);
		if (locations == null) 
			locations = new ArrayList<Location>();

		List<JqueryObject> jqueryObjects = new ArrayList<JqueryObject>();
		for (Location location : locations) {
			String place = location.getTownship() + ", " + location.getStateCode();
			jqueryObjects.add(new JqueryObject(place,location.getId()));
		}
		return jqueryObjects;
	}
}
