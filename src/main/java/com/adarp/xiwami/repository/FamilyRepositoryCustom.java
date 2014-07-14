package com.adarp.xiwami.repository;

import com.adarp.xiwami.domain.Family;


public interface FamilyRepositoryCustom {
	
	//public List<Family> FindFamilyFromMemberCustom(Double longitude,Double latitude, String qsDistance, 
		//	Integer fromAge, Integer toAge, String[] languages) throws Exception;
	
	//public Family GetFamilyById(String id) throws Exception;
	
	public void AddFamily(Family newFamily);
	
	//public void UpdateFamily(Family updatedFamily);
	
	//public void DeleteFamily(String id);		
}
