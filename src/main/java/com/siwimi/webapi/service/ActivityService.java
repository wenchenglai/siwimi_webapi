package com.siwimi.webapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.siwimi.webapi.domain.Activity;
import com.siwimi.webapi.domain.Location;
import com.siwimi.webapi.repository.ActivityRepository;
import com.siwimi.webapi.repository.FavoriteRepository;
import com.siwimi.webapi.repository.LocationRepository;

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
										 String queryText,
										 Integer page,
										 Integer per_page) {													
		List<Activity> activityList = activityRep.queryActivity(creatorId,status,type,period,fromTime,toTime,
				                                                longitude,latitude,qsDistance,queryText,
				                                                page,per_page);

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
		return activityRep.findByIdAndIsDeletedRecordIsFalse(id);
	}
	
	public Activity addActivity(Activity newActivity) {			
		newActivity.setIsDeletedRecord(false);
		newActivity.setViewCount(0);
		newActivity = updateLocation(newActivity);
		
		// fromTime must be earlier than toTime
		if ((newActivity.getFromDate()!=null) && (newActivity.getToDate()!=null) ) {
			if (newActivity.getFromDate().compareTo(newActivity.getToDate())>0) {
				// if fromTime is after toTime --> fromTime = toTime
				newActivity.setToDate(newActivity.getFromDate());
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
		activity.setIsDeletedRecord(true);
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
