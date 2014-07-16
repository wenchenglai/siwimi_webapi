package com.adarp.xiwami.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adarp.xiwami.domain.Activity;
import com.adarp.xiwami.repository.ActivityRepository;

@Service
public class ActivityService {
	
	@Autowired
	ActivityRepository activityRep;
	
	public List<Activity> FindActivities() {
		return activityRep.findAll();
	}
	
	public Activity FindByActivityId(String id){
		return activityRep.findOne(id);
	}
	
	public void AddActivity(Activity newActivity) {
		newActivity.setIsDeleted(false);
		activityRep.save(newActivity);
	}
	
	public void EditActivity(String id, Activity updatedActivity) {
		updatedActivity.set_Id(id);
		activityRep.save(updatedActivity);
	}
	
	public void DeleteActivity(String id) {
		Activity activity = activityRep.findOne(id);
		activity.setIsDeleted(true);
		activityRep.save(activity);
	}
}
