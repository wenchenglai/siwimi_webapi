package com.siwimi.webapi.repository;

import java.util.List;

import com.siwimi.webapi.domain.Family;


public interface FamilyRepositoryCustom {			
	Family saveFamily(Family newFamily);
	List<String> findGeoFamiliesId(Double longitude,Double latitude,String qsDistance);

}
