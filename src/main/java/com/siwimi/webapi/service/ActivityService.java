package com.siwimi.webapi.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

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
										 String ageGroup,boolean isFree,
										 String queryText,
										 Integer page,
										 Integer per_page,
										 String sortBy) {													
		List<Activity> activityList = activityRep.queryActivity(creatorId,status,type,period,fromTime,toTime,
				                                                longitude,latitude,qsDistance,ageGroup,isFree,queryText,
				                                                page,per_page,sortBy);

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
		newActivity = updateLocationAndTime(newActivity);
		
		/** We cannot use activityRep.save()
			Because Spring repository does not provide geospatial indexing : db.location.ensureIndex( {location: "2d"} )
		**/
		return activityRep.saveActivity(newActivity);
	}
	
	public Activity updateActivity(String id, Activity updatedActivity) {
		updatedActivity.setId(id);
		updatedActivity = updateLocationAndTime(updatedActivity);
		return activityRep.saveActivity(updatedActivity);
	}
	
	public Activity deleteActivity(String id) {
		Activity activity = activityRep.findOne(id);
		if (activity == null)
			return null;
		else if (!activity.getIsDeletedRecord()) {
			activity.setIsDeletedRecord(true);
			return activityRep.saveActivity(activity);
		} else
			return null;		
	}
	
	public Activity updateLocationAndTime(Activity activity) {
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
		
		/** Front-end doesn't combine hr/min in the fromDate object --> need to append hr/min into fromDate object **/		
		if ((activity.getFromTime() != null) && (activity.getFromDate() != null)) {
			// parse fromTime
			String [] part1 = activity.getFromTime().split(" ");
			String [] part2 = part1[0].split(":");
			Boolean AM = part1[1].toLowerCase().contains("am") ? true : false;  
			String hour = AM? part2[0]: Integer.toString(Integer.parseInt(part2[0])+12);
			String min = part2[1];
			
		    SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		    String fromDateString = isoFormat.format(activity.getFromDate());
		    String [] part3 = fromDateString.split("T");
		    fromDateString = part3[0]+"T"+hour+":"+min+":00.000Z";
		    
		    if (thisLocation.getTimezone() != null) {
		    	if (thisLocation.getTimezone().contains("-5"))
		    		isoFormat.setTimeZone(TimeZone.getTimeZone("America/New_York"));
		    	else if (thisLocation.getTimezone().contains("-6"))
		    		isoFormat.setTimeZone(TimeZone.getTimeZone("America/Winnipeg"));
		    	else if (thisLocation.getTimezone().contains("-7"))
		    		isoFormat.setTimeZone(TimeZone.getTimeZone("America/Phoenix"));
		    	else if (thisLocation.getTimezone().contains("-8"))
		    		isoFormat.setTimeZone(TimeZone.getTimeZone("America/Vancouver"));		    	
		    } else {
			    isoFormat.setTimeZone(TimeZone.getTimeZone("America/New_York"));
		    }
		    
		   try {
			Date fromDate = isoFormat.parse(fromDateString);
			activity.setFromDate(fromDate);
		   } catch (ParseException e) {
			   e.printStackTrace();
		   }			
		}
	
		/** Front-end doesn't combine hr/min in the toDate object --> need to append hr/min into toDate object **/		
		if ((activity.getToTime() != null) && (activity.getToDate() != null)) {
			// parse toTime
			String [] part1 = activity.getToTime().split(" ");
			String [] part2 = part1[0].split(":");
			Boolean AM = part1[1].toLowerCase().contains("am") ? true : false;  
			String hour = AM? part2[0]: Integer.toString(Integer.parseInt(part2[0])+12);
			String min = part2[1];
			
		    SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		    String toDateString = isoFormat.format(activity.getToDate());
		    String [] part3 = toDateString.split("T");
		    toDateString = part3[0]+"T"+hour+":"+min+":00.000Z";
		    
		    if (thisLocation.getTimezone() != null) {
		    	if (thisLocation.getTimezone().contains("-5"))
		    		isoFormat.setTimeZone(TimeZone.getTimeZone("America/New_York"));
		    	else if (thisLocation.getTimezone().contains("-6"))
		    		isoFormat.setTimeZone(TimeZone.getTimeZone("America/Winnipeg"));
		    	else if (thisLocation.getTimezone().contains("-7"))
		    		isoFormat.setTimeZone(TimeZone.getTimeZone("America/Phoenix"));
		    	else if (thisLocation.getTimezone().contains("-8"))
		    		isoFormat.setTimeZone(TimeZone.getTimeZone("America/Vancouver"));		    	
		    } else {
			    isoFormat.setTimeZone(TimeZone.getTimeZone("America/New_York"));
		    }
		    
		   try {
			Date toDate = isoFormat.parse(toDateString);
			activity.setToDate(toDate);
		   } catch (ParseException e) {
			   e.printStackTrace();
		   }			
		}
		
		return activity;
	}
}
