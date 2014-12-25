package com.adarp.xiwami.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adarp.xiwami.domain.Activity;
import com.adarp.xiwami.domain.ZipCode;
import com.adarp.xiwami.repository.ActivityRepository;
import com.adarp.xiwami.repository.ZipCodeRepository;

@Service
public class ActivityService {
	
	@Autowired
	ActivityRepository activityRep;
	
	@Autowired
	private ZipCodeRepository zipCodeRep;
	
	public List<Activity> FindActivities(String creatorId,String status,Double longitude,Double latitude,String qsDistance,String queryText) {											
		return activityRep.queryActivity(creatorId, status, longitude, latitude, qsDistance, queryText);
	}
	
	public Activity FindByActivityId(String id){
		return activityRep.findOne(id);
	}
	
	public Activity AddActivity(Activity newActivity) {
			
		newActivity.setIsDeleted(false);
		newActivity = updateZipCode(newActivity);
		
		// fromTime must be ealier than toTime
		if (newActivity.getFromTime().compareTo(newActivity.getToTime())>0) {
			// if fromTime is after toTime --> fromTime = toTime
			newActivity.setToTime(newActivity.getFromTime());
		}
		
		/** We cannot use activityRep.save()
			Because Spring repository does not provide geospatial indexing : db.location.ensureIndex( {location: "2d"} )
		**/
		return activityRep.saveActivity(newActivity);
	}
	
	public Activity UpdateActivity(String id, Activity updatedActivity) {
		updatedActivity.setId(id);
		updatedActivity = updateZipCode(updatedActivity);
		return activityRep.saveActivity(updatedActivity);
	}
	
	public void DeleteActivity(String id) {
		Activity activity = activityRep.findOne(id);
		activity.setIsDeleted(true);
		activityRep.saveActivity(activity);
	}
	
	public Activity updateZipCode(Activity activity) {
		// lookup zipcode from the collection ZipCode;
		ZipCode thisZipCode = new ZipCode();
				
		// if the zipCode is not provided by the user
		if (activity.getZipCode()==null) {				
			if (activity.getCityState()==null){
				// if both zipcode and cityState are not completed, set default to 48105
				thisZipCode = zipCodeRep.findByzipCode(48105);
			} else {
				String [] parts = activity.getCityState().split(",");
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
			thisZipCode = zipCodeRep.findByzipCode(Integer.parseInt(activity.getZipCode()));			
		}
		
		// set longitude and latitude of the family object 
		double[] location = {thisZipCode.getLongitude(), thisZipCode.getLatitude()};
		activity.setZipCode(thisZipCode.getZipCode());
		activity.setLocation(location);
		activity.setCityState(thisZipCode.getTownship()+", "+thisZipCode.getStateCode());
		
		return activity;
	}
}
