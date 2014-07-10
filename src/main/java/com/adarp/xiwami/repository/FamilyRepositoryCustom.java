package com.adarp.xiwami.repository;

import com.adarp.xiwami.domain.Family;


public interface FamilyRepositoryCustom {
	
	//public List<Family> GetFamilies() throws Exception;
	
	//public Family GetFamilyById(String id) throws Exception;
	
	public void AddFamily(Family newFamily) throws Exception;
	
	public void UpdateFamily(Family updateFamily) throws Exception;
	
	public void DeleteFamily(String id) throws Exception;		
}
