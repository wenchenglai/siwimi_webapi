package com.adarp.xiwami.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.adarp.xiwami.domain.Location;
import com.adarp.xiwami.service.LocationService;

@RestController
public class LocationController {
	
	@Autowired
	private LocationService locationService;
	
	@RequestMapping(value = "/locations", method = RequestMethod.GET, produces = "application/json")
	Map<String,List<Location>> findFuzzuLocations(@RequestParam(value="queryText", required=false) String queryText) {
		Map<String,List<Location>> responseBody = new HashMap<String,List<Location>>();
		List<Location> locations = locationService.queryFuzzyLocations(queryText);
		if (locations == null) 
			locations = new ArrayList<Location>();
		responseBody.put("locations", locations);
		return responseBody;
	}
	
}
