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

import com.adarp.xiwami.repository.*;
import com.adarp.xiwami.domain.Activity;
import com.adarp.xiwami.web.dto.*;

@RestController
public class ActivityController {

	@Autowired
	private ActivityRepository activityRep;

	// Get all activities
	@RequestMapping(value = "/activities", method = RequestMethod.GET, produces = "application/json")
	public Map<String,List<Activity>> FindActivities() {
		try {				
			Map<String,List<Activity>> responseBody = new HashMap<String,List<Activity>>();
			//List<Activity> list = activityRep.GetActivities();
			List<Activity> list = activityRep.findAll();
			responseBody.put("activity", list);
			return responseBody;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error : unable to query Activities.");
			return null;
		}
	}

	// Get activity by ID
	@RequestMapping(value = "/activities/{id}", method = RequestMethod.GET, produces = "application/json")
	public Map<String,Activity> FindByActivityId(@PathVariable("id") String id) {
		
		try {
			Map<String,Activity> responseBody = new HashMap<String,Activity>();			
			//Activity activity = activityRep.GetActivityById(id);
			Activity activity = activityRep.findOne(id);
			responseBody.put("activity", activity);
			return responseBody;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error : unable to query Activity.");
			return null;
		}
	}
	
	// Add New Activity
	@RequestMapping(value = "/activities", method = RequestMethod.POST, produces = "application/json")
	public void AddActivity(@RequestBody ActivitySideload newActivity)
	{
		try {
			//activityRep.AddActivity(newActivity.activity);
			activityRep.save(newActivity.activity);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error : unable to add Activity.");
		}		
	}	
	
	// Update Activity
	@RequestMapping(value = "/activities/{id}", method = RequestMethod.PUT, produces = "application/json")
	public void EditActivity(@PathVariable("id") String id, @RequestBody ActivitySideload updatedActivity)
	{
		try {
			activityRep.UpdateActivity(id,updatedActivity.activity);
			//updatedActivity.activity.set_Id(id);
			//activityRep.save(updatedActivity.activity);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error : unable to update Activity.");
		}
	}
	
	// Delete Activity
	@RequestMapping (value = "/activities/{id}", method = RequestMethod.DELETE, produces = "application/json")
	public void DeleteActivity(@PathVariable("id")String id) {
		try {
			activityRep.DeleteActivity(id);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error : unable to delete Activity.");			
		}
	}	
}
