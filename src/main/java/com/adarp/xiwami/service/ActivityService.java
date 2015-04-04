package com.adarp.xiwami.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adarp.xiwami.domain.Activity;
import com.adarp.xiwami.domain.Location;
import com.adarp.xiwami.repository.ActivityRepository;
import com.adarp.xiwami.repository.FavoriteRepository;
import com.adarp.xiwami.repository.LocationRepository;

@Service
public class ActivityService {
	
	@Autowired
	private ActivityRepository activityRep;
	
	@Autowired
	private FavoriteRepository favoriteRep;	
	
	@Autowired
	private LocationRepository locationRep;
	
	public List<Activity> findActivities(String creatorId,
										 String requesterId,
										 String status,
										 String type,
										 Integer period,
										 String fromTime,
										 String toTime,
										 Double longitude,Double latitude,String qsDistance,
										 String queryText) {													
		List<Activity> activityList = activityRep.queryActivity(creatorId,status,type,period,fromTime,toTime,
				                                                longitude,latitude,qsDistance,queryText);

		// increment viewcount by 1, and save it to MongoDB
		for (int i=0; i<activityList.size(); i++) {
			Activity activity = activityList.get(i);
			// Populate isFavorite
			if (favoriteRep.queryFavorite(requesterId, activity.getId(), "activity") != null) {
				activity.setIsFavorite(true);
			}
			activityList.set(i, activity);
			// increment viewcount by 1, and save it to MongoDB
			activity.setViewCount(activity.getViewCount()+1);
			activityRep.saveActivity(activity);		
		}
		
		return activityList;
	}
	
	public Activity findByActivityId(String id){
		return activityRep.findByIdAndIsDestroyedIsFalse(id);
	}
	
	public Activity addActivity(Activity newActivity) {			
		newActivity.setIsDestroyed(false);
		newActivity.setViewCount(0);
		newActivity = updateLocation(newActivity);
		
		// fromTime must be earlier than toTime
		if ((newActivity.getFromTime()!=null) && (newActivity.getToTime()!=null) ) {
			if (newActivity.getFromTime().compareTo(newActivity.getToTime())>0) {
				// if fromTime is after toTime --> fromTime = toTime
				newActivity.setToTime(newActivity.getFromTime());
			}
		}
		
		/** We cannot use activityRep.save()
			Because Spring repository does not provide geospatial indexing : db.location.ensureIndex( {location: "2d"} )
		**/
		return activityRep.saveActivity(newActivity);
	}
	
	public Activity updateActivity(String id, Activity updatedActivity) {
		updatedActivity.setId(id);
		updatedActivity = updateLocation(updatedActivity);
		return activityRep.saveActivity(updatedActivity);
	}
	
	public void deleteActivity(String id) {
		Activity activity = activityRep.findOne(id);
		activity.setIsDestroyed(true);
		activityRep.saveActivity(activity);
	}
	
	public Activity updateLocation(Activity activity) {
		// lookup location from the collection Location;
		Location thisLocation = locationRep.queryLocation(activity.getZipCode(), activity.getCity(), activity.getState());
		// set longitude and latitude 
		if (thisLocation!=null) {
			double[] location = {thisLocation.getLongitude(), thisLocation.getLatitude()};
			activity.setZipCode(thisLocation.getZipCode());
			activity.setLocation(location);
			activity.setCity(thisLocation.getTownship());
			activity.setState(thisLocation.getStateCode());
		}

		return activity;
	}
}
