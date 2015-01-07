package com.adarp.xiwami.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
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
		// Geo search in family collection				
		String [] parts = qsDistance.split(" ");
		Distance distance;
		if (parts[1].toLowerCase().contains("mile"))
			distance = new Distance(Double.parseDouble(parts[0]),Metrics.MILES);
		else
			distance = new Distance(Double.parseDouble(parts[0]),Metrics.KILOMETERS);			
		List<Family> geoFamilies = familyRep.findByLocationNearAndIsDestroyedIsFalse(new Point(longitude,latitude),distance);
		
		// Retrieve the id of geoFamilies
		List<String> geoFamilyId = new ArrayList<String>();
		for (Family family : geoFamilies) {
			geoFamilyId.add(family.getId());
		}
							
		// Convert fromAge and toAge to Date(), if both objects are not null.
		Date fromDate = null;
		Date toDate = null;
		if ((fromAge!=null) || (toAge!=null)) {				
			// if fromAge is not specified, set it to 0
			if (fromAge==null) 
				fromAge = new Integer(0);
			// if toAge is not specified, set it to 150
			if (toAge==null)
				toAge = new Integer(150);
			Calendar cal = Calendar.getInstance();
			Date today = new Date();
			cal.setTime(today);
			cal.add(Calendar.YEAR, -fromAge);
			fromDate = cal.getTime();

			cal.setTime(today);
			cal.add(Calendar.YEAR, -toAge);
			toDate = cal.getTime();
		}

					
		if ((languages==null) && (fromAge==null) && (toAge==null))
			return geoFamilies;
		else if (languages==null) {
			// Query the qualified members from geoFamily.
			List<Member> foundMember = memberRep.findByFamilyInAndBirthdayBetweenAndIsDestroyedIsFalse(geoFamilyId, toDate, fromDate);
			// Retrieve the familyId of the qualified members.
			Set<String> foundFamilyId = new HashSet<String>();
			for (Member member:foundMember) {
				foundFamilyId.add(member.getFamily());
			}
			return familyRep.findByIdIn(foundFamilyId);
		}
		else {
			// Convert String[] to List<String>
			List<String> languageList = new ArrayList<String>(Arrays.asList(languages));
			// Query the qualified members from geoFamily.
			List<Member> foundMember = memberRep.findByFamilyInAndLanguagesInAndBirthdayBetweenAndIsDestroyedIsFalse(geoFamilyId, languageList, toDate, fromDate);
			// Retrieve the familyId of the qualified members.
			Set<String> foundFamilyId = new HashSet<String>();
			for (Member member:foundMember) {
				foundFamilyId.add(member.getFamily());
			}
			return familyRep.findByIdIn(foundFamilyId);											
		}		
	}
	
	public Family findByFamilyId(String id) {
		return familyRep.findByIdAndIsDestroyedIsFalse(id);
	}
	
	public Family addFamily(Family newFamily) {				
		newFamily.setIsDestroyed(false);
		newFamily = updateZipCode(newFamily);
		return familyRep.addFamily(newFamily);
	}
	
	public Family updateFamily(String id, Family updatedFamily) {
		updatedFamily.setId(id);
		updatedFamily = updateZipCode(updatedFamily);
		Family savedFamily = familyRep.save(updatedFamily);
		return savedFamily;
	}
	
	public void deleteFamily(String id) {		
		//Delete members which belongs both "non-user" and this family
		List<Member> memberList = memberRep.findByFamilyInAndIsDestroyedIsFalse(id);
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
	public Family updateZipCode(Family family) {
		// lookup zipcode from the collection ZipCode;
		ZipCode thisZipCode = new ZipCode();
				
		// if the zipCode is not provided by the user
		if (family.getZipCode()==null) {				
			if (family.getCityState()==null){
				// if both zipcode and cityState are not completed, set default to 48105
				thisZipCode = zipCodeRep.findByzipCode(48105);
			} else {
				String [] parts = family.getCityState().split(",");
				String city = parts[0].trim();
				String stateCode = parts[1].trim();	
				thisZipCode = zipCodeRep.findByTownshipLikeIgnoreCaseAndStateCodeLikeIgnoreCase(city, stateCode);				
			}						
		} else {
			/** if the zipCode is provided by the user:
			   (1) ignore stateCity provided by the user, 
			   (2) lookup zipcode from the collection ZipCode
			   (3) please note that the type of zipcode is "int" in the mongoDB collection 
			**/
			thisZipCode = zipCodeRep.findByzipCode(Integer.parseInt(family.getZipCode()));			
		}
			
		// set longitude and latitude of the family object 
		double[] location = {thisZipCode.getLongitude(), thisZipCode.getLatitude()};
		family.setZipCode(thisZipCode.getZipCode());
		family.setLocation(location);
		family.setCityState(thisZipCode.getTownship()+", "+thisZipCode.getStateCode());	
		
		return family;
	}
}
