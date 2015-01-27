package com.adarp.xiwami.repository;

import java.util.List;

import com.adarp.xiwami.domain.Family;


public interface FamilyRepositoryCustom {			
	Family saveFamily(Family newFamily);
	List<String> findGeoFamiliesId(Double longitude,Double latitude,String qsDistance);

}
