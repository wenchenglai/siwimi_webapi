package com.adarp.xiwami.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.adarp.xiwami.service.ActivityService;
import com.adarp.xiwami.web.dto.ActivitySideload;
import com.adarp.xiwami.domain.Activity;

@RestController
public class ActivityController {

	@Autowired
	private ActivityService activityService;

	// Get activities by criteria
	@RequestMapping(value = "/activities", method = RequestMethod.GET, produces = "application/json")
	public Map<String,List<Activity>> FindActivities(
			@RequestParam(value="creator", required=false) String creatorId,
			@RequestParam(value="type", required=false) String type,
			@RequestParam(value="longitude", required=false) Double longitude,
			@RequestParam(value="latitude", required=false) Double latitude,
			@RequestParam(value="distance", required=false) String qsDistance, 
			@RequestParam(value="queryText", required=false) String queryText) {
		Map<String,List<Activity>> responseBody = new HashMap<String,List<Activity>>();
		List<Activity> list = activityService.FindActivities();
		responseBody.put("activity", list);
		return responseBody;
	}

	// Get activity by ID
	@RequestMapping(value = "/activities/{id}", method = RequestMethod.GET, produces = "application/json")
	public Map<String,Activity> FindByActivityId(@PathVariable("id") String id) {
		Map<String,Activity> responseBody = new HashMap<String,Activity>();			
		Activity activity = activityService.FindByActivityId(id);
		responseBody.put("activity", activity);
		return responseBody;
	}
	
	// Add New Activity
	@RequestMapping(value = "/activities", method = RequestMethod.POST, produces = "application/json")
	public Map<String, Activity> AddActivity(@RequestBody ActivitySideload newActivity) {
		Activity savedActivity = activityService.AddActivity(newActivity.activity);
		
		Map<String, Activity> responseBody = new HashMap<String, Activity>();
		responseBody.put("activity", savedActivity);
		
		return responseBody;		
	}	
	
	// Update Activity
	@RequestMapping(value = "/activities/{id}", method = RequestMethod.PUT, produces = "application/json")
	public Map<String, Activity> UpdateActivity(@PathVariable("id") String id, @RequestBody ActivitySideload updatedActivity) {
		Activity savedActivity = activityService.UpdateActivity(id, updatedActivity.activity);
		Map<String, Activity> responseBody = new HashMap<String, Activity>();
		responseBody.put("activity", savedActivity);
		
		return responseBody;			
	}
	
	// Delete Activity
	@RequestMapping (value = "/activities/{id}", method = RequestMethod.DELETE, produces = "application/json")
	public void DeleteActivity(@PathVariable("id")String id) {
		activityService.DeleteActivity(id);
	}	
}
