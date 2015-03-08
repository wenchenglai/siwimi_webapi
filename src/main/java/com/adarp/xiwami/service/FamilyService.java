package com.adarp.xiwami.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adarp.xiwami.domain.Family;
import com.adarp.xiwami.domain.Member;
import com.adarp.xiwami.domain.ZipCode;
import com.adarp.xiwami.repository.FamilyRepository;
import com.adarp.xiwami.repository.MemberRepository;
import com.adarp.xiwami.repository.ZipCodeRepository;

@Service
public class FamilyService {

	@Autowired
	private FamilyRepository familyRep;
	
	@Autowired
	private MemberRepository memberRep;
	
	@Autowired
	private ZipCodeRepository zipCodeRep;
	
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
		ZipCode thisZipCode = new ZipCode();
				
		// if the zipCode is not provided by the user
		if (family.getZipCode() == null) {				
			// Front-end must provide City and State
			String city = family.getCity();
			String state = family.getState();
			thisZipCode = zipCodeRep.findByTownshipLikeIgnoreCaseAndStateLikeIgnoreCase(city, state);									
		} else {
			/** if the zipCode is provided by the the front-end:
			   (1) ignore state/City provided by the front-end, 
			   (2) lookup zipcode from the collection ZipCode
			   (3) The type of zipcode is "int" in the mongoDB collection 
			**/
			thisZipCode = zipCodeRep.findByzipCode(Integer.parseInt(family.getZipCode()));			
		}
			
		// set longitude and latitude of the family object 
		double[] location = {thisZipCode.getLongitude(), thisZipCode.getLatitude()};
		family.setZipCode(thisZipCode.getZipCode());
		family.setLocation(location);
		family.setCity(thisZipCode.getTownship());
		family.setState(thisZipCode.getStateCode());
		
		return family;
	}
}
