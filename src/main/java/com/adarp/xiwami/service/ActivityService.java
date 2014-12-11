package com.adarp.xiwami.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;

import com.adarp.xiwami.domain.Activity;
import com.adarp.xiwami.domain.Family;
import com.adarp.xiwami.domain.Member;
import com.adarp.xiwami.repository.ActivityRepository;
import com.adarp.xiwami.repository.FamilyRepository;
import com.adarp.xiwami.repository.MemberRepository;

@Service
public class ActivityService {
	
	@Autowired
	ActivityRepository activityRep;
	
	@Autowired
	private FamilyRepository familyRep;
	
	@Autowired
	private MemberRepository memberRep;
	
	public List<Activity> FindActivities(String creatorId,String status,Double longitude,Double latitude,String qsDistance,String queryText) {
		if ((creatorId!=null) && (status!=null)) {
			return activityRep.findByCreatorAndStatusAndIsDeletedIsFalse(creatorId, status);
		}
		else {
			// Geo search in family collection				
			String [] parts = qsDistance.split(" ");
			Distance distance;
			if (parts[1].toLowerCase().contains("mile"))
				distance = new Distance(Double.parseDouble(parts[0]),Metrics.MILES);
			else
				distance = new Distance(Double.parseDouble(parts[0]),Metrics.KILOMETERS);			
			List<Family> geoFamilies = familyRep.findByLocationNearAndIsDeletedIsFalse(new Point(longitude,latitude),distance);
			
			// Retrieve the id of geoFamilies
			List<String> geoFamilyId = new ArrayList<String>();
			for (Family family : geoFamilies) {
				geoFamilyId.add(family.getId());
			}
			
			// Retrieve id of the geoMembers
			List<Member> geoMembers = memberRep.findByFamilyInAndIsDeletedIsFalse(geoFamilyId);
			List<String> geoMemberId = new ArrayList<String>();
			for (Member member : geoMembers) {
				geoMemberId.add(member.getId());
			}	
			
			return activityRep.findByCreatorInAndTypeDescriptionLikeIgnoreCaseAndIsDeletedIsFalse(geoMemberId, queryText);			
		}
	}
	
	public Activity FindByActivityId(String id){
		return activityRep.findOne(id);
	}
	
	public Activity AddActivity(Activity newActivity) {
		newActivity.setIsDeleted(false);
		return activityRep.save(newActivity);
	}
	
	public Activity UpdateActivity(String id, Activity updatedActivity) {
		updatedActivity.setId(id);
		return activityRep.save(updatedActivity);
	}
	
	public void DeleteActivity(String id) {
		Activity activity = activityRep.findOne(id);
		activity.setIsDeleted(true);
		activityRep.save(activity);
	}
}
