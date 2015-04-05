package com.adarp.xiwami.repository;

import java.util.List;

import com.adarp.xiwami.domain.Location;

public interface LocationRepositoryCustom {
		Location queryLocation(String zipCode, String city, String state);
		List<Location> queryFuzzyLocations(String queryText);	
}