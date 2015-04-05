package com.adarp.xiwami.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adarp.xiwami.domain.Family;
import com.adarp.xiwami.domain.Member;
import com.adarp.xiwami.domain.Location;
import com.adarp.xiwami.repository.FamilyRepository;
import com.adarp.xiwami.repository.MemberRepository;
import com.adarp.xiwami.repository.LocationRepository;

@Service
public class FamilyService {

	@Autowired
	private FamilyRepository familyRep;
	
	@Autowired
	private MemberRepository memberRep;
	
	@Autowired
	private LocationRepository zipCodeRep;
	
	public List<Family> findFamilies(Double longitude,Double latitude,String qsDistance,Integer fromAge,Integer toAge,String[] languages) {
		List<String> geoFamiliesId = familyRep.findGeoFamiliesId(longitude, latitude, qsDistance);
		Set<String> qualifiedFamiliesId = memberRep.findFamilies(geoFamiliesId, fromAge, toAge, languages);
		return familyRep.findByIdIn(qualifiedFamiliesId);
	}
	
	public Family findByFamilyId(String id) {
		return familyRep.findByIdAndIsDestroyedIsFalse(id);
	}
	
	public Family addFamily(Family newFamily) {				
		newFamily.setIsDestroyed(false);
		newFamily = updateLocation(newFamily);
		return familyRep.saveFamily(newFamily);
	}
	
	public Family updateFamily(String id, Family updatedFamily) {
		updatedFamily.setId(id);
		updatedFamily = updateLocation(updatedFamily);
		Family savedFamily = familyRep.saveFamily(updatedFamily);
		return savedFamily;
	}
	
	public void deleteFamily(String id) {		
		//Delete members which belongs both "non-user" and this family
		List<Member> memberList = memberRep.query(id,null);
		for (Member member:memberList) {
			if ((member.getFacebookId()==null)) {
				member.setIsDestroyed(true);
				memberRep.save(member);
			}				
		}
		
		Family family = familyRep.findOne(id);
		family.setIsDestroyed(true);
		familyRep.save(family);		
		
	}
	
	public Family updateLocation(Family family) {
		// lookup zipcode from the collection ZipCode;
		Location thisZipCode = zipCodeRep.queryLocation(family.getZipCode(), family.getCity(), family.getState());
		// set longitude and latitude 
		if (thisZipCode!=null) {
			double[] location = {thisZipCode.getLongitude(), thisZipCode.getLatitude()};
			family.setZipCode(thisZipCode.getZipCode());
			family.setLocation(location);
			family.setCity(thisZipCode.getTownship());
			family.setState(thisZipCode.getStateCode());
		}

		return family;
	}
}
