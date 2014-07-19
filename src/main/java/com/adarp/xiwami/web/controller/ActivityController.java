package com.adarp.xiwami.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.adarp.xiwami.service.ActivityService;
import com.adarp.xiwami.web.dto.ActivitySideload;
import com.adarp.xiwami.domain.Activity;

@RestController
public class ActivityController {

	@Autowired
	private ActivityService activityService;

	// Get all activities
	@RequestMapping(value = "/activities", method = RequestMethod.GET, produces = "application/json")
	public Map<String,List<Activity>> FindActivities() {
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
	public void AddActivity(@RequestBody ActivitySideload newActivity) {
		activityService.AddActivity(newActivity.activity);	
	}	
	
	// Update Activity
	@RequestMapping(value = "/activities/{id}", method = RequestMethod.PUT, produces = "application/json")
	public void UpdateActivity(@PathVariable("id") String id, @RequestBody ActivitySideload updatedActivity) {
		activityService.UpdateActivity(id, updatedActivity.activity);
	}
	
	// Delete Activity
	@RequestMapping (value = "/activities/{id}", method = RequestMethod.DELETE, produces = "application/json")
	public void DeleteActivity(@PathVariable("id")String id) {
		activityService.DeleteActivity(id);
	}	
}
