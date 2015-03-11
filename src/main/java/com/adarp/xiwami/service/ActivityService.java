package com.adarp.xiwami.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adarp.xiwami.domain.Activity;
import com.adarp.xiwami.domain.ZipCode;
import com.adarp.xiwami.repository.ActivityRepository;
import com.adarp.xiwami.repository.FavoriteRepository;
import com.adarp.xiwami.repository.ZipCodeRepository;

@Service
public class ActivityService {
	
	@Autowired
	ActivityRepository activityRep;
	
	@Autowired
	FavoriteRepository favoriteRep;	
	
	@Autowired
	private ZipCodeRepository zipCodeRep;
	
	public List<Activity> findActivities(String creatorId,
										 String requesterId,
										 String status,
										 String type,
										 Double longitude,Double latitude,String qsDistance,
										 String queryText) {													
		List<Activity> activityList = activityRep.queryActivity(creatorId, status, type, longitude, latitude, qsDistance, queryText);

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
		
		// fromTime must be ealier than toTime
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
		// lookup zipcode from the collection ZipCode;
		ZipCode thisZipCode = new ZipCode();
				
		// if the zipCode is not provided by the user
		if (activity.getZipCode() == null) {				
			// Front-end must provide City and State
			String city = activity.getCity();
			String state = activity.getState();
			if ((city != null) && (state != null)) {
				thisZipCode = zipCodeRep.findByTownshipLikeIgnoreCaseAndStateLikeIgnoreCase(city, state);		
			}								
		} else {
			/** if the zipCode is provided by the the front-end:
			   (1) ignore state/City provided by the front-end, 
			   (2) lookup zipcode from the collection ZipCode
			   (3) The type of zipcode is "int" in the mongoDB collection 
			**/
			thisZipCode = zipCodeRep.findByzipCode(Integer.parseInt(activity.getZipCode()));			
		}
		
		// set longitude and latitude of the family object 
		double[] location = {thisZipCode.getLongitude(), thisZipCode.getLatitude()};
		activity.setZipCode(thisZipCode.getZipCode());
		activity.setLocation(location);
		activity.setCity(thisZipCode.getTownship());
		activity.setState(thisZipCode.getStateCode());
		
		return activity;
	}
}
