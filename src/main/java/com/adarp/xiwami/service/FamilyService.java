package com.adarp.xiwami.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;

import com.adarp.xiwami.domain.Family;
import com.adarp.xiwami.domain.Member;
import com.adarp.xiwami.repository.FamilyRepository;
import com.adarp.xiwami.repository.MemberRepository;
import com.adarp.xiwami.web.dto.FamilySideload;

@Service
public class FamilyService {

	@Autowired
	private FamilyRepository familyRep;
	
	@Autowired
	private MemberRepository memberRep;
	
	public List<Family> FindFamilies(Double longitude,Double latitude,String qsDistance,Integer fromAge,Integer toAge,String[] languages) {
		try {
			// Geo search in family collection				
			String [] parts = qsDistance.split(" ");
			Distance distance;
			if (parts[1].toLowerCase().contains("mile"))
				distance = new Distance(Double.parseDouble(parts[0]),Metrics.MILES);
			else
				distance = new Distance(Double.parseDouble(parts[0]),Metrics.KILOMETERS);			
			List<Family> geoFamilies = familyRep.findByLocationNear(new Point(longitude,latitude),distance);
			
			// Convert geoFamilies to string array
			List<String> geoFamilyId = new ArrayList<String>();
			for (Family family : geoFamilies) {
				geoFamilyId.add(family.getId());
			}
					
			// Convert fromAge and toAge to Date(), if both are not null.
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
				List<Member> foundMember = memberRep.findByFamilyInAndBirthdayBetween(geoFamilyId, fromDate, toDate);
				Set<String> foundFamilyId = new HashSet<String>();
				for (Member member:foundMember) {
					foundFamilyId.add(member.getFamily());
				}
				return familyRep.findByIdIn(foundFamilyId);
			}
			else {
				// Convert String[] to List<String>
				List<String> languageList = new ArrayList<String>(Arrays.asList(languages));
				List<Member> foundMember = memberRep.findByFamilyInAndLanguagesInAndBirthdayBetween(geoFamilyId, languageList, fromDate, toDate);
				Set<String> foundFamilyId = new HashSet<String>();
				for (Member member:foundMember) {
					foundFamilyId.add(member.getFamily());
				}
				return familyRep.findByIdIn(foundFamilyId);											
			}
				
			

			


			//List<Member> members = memberRep.findBybirthdayBetween(toDate, fromDate);
			
/*			@Query("{ 'name' : {$regex : ?0, $options : 'i'}, 'createdDate' : {$gte : ?1, $lt : ?2 }} }")
			List<MyItem> getItemsLikeNameByDateRange(String name, Date startDateRange, Date endDateRange);*/		
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error : unable to query Families.");
			return null;
		}
	}
	
	public Family FindByFamilyId(String id) {
		try {
			return familyRep.findOne(id);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error : unable to query Family.");
			return null;
		}
	}
	
	public void AddFamily(FamilySideload newFamily) {
		try {
			familyRep.AddFamily(newFamily.family);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error : unable to add Family.");
		}

	}
	
	public void EditFamily(String id,FamilySideload updatedFamily) {
		try {
			updatedFamily.family.setId(id);
			familyRep.UpdateFamily(updatedFamily.family);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error : unable to update Family.");
		}
	}
	
	public void DeleteFamily(String id) {
		try {
			familyRep.DeleteFamily(id);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error : unable to delete Family.");			
		}
	}
	
	public List<Family> SearchFamilyNearby(String id, double distance) {
		Family thisFamily = familyRep.findOne(id);
		double[] coordinates = thisFamily.getLocation();	
		Point point = new Point(coordinates[0],coordinates[1]);
		return familyRep.findByLocationNear(point,new Distance(distance,Metrics.MILES));
	}
}
