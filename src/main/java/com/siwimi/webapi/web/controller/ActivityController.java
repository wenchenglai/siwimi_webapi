package com.siwimi.webapi.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.siwimi.webapi.domain.Activity;
import com.siwimi.webapi.domain.Member;
import com.siwimi.webapi.service.ActivityService;
import com.siwimi.webapi.service.MemberService;
import com.siwimi.webapi.web.dto.ActivitySideload;
import com.siwimi.webapi.web.dto.ActivitySideloadList;

@RestController
public class ActivityController {

	@Autowired
	private ActivityService activityService;

	@Autowired
	private MemberService memberService;

	// Get activities by criteria
	@RequestMapping(value = "/activities", method = RequestMethod.GET, produces = "application/json")
	public ActivitySideloadList findActivities(
			@RequestParam(value="creator", required=false) String creatorId,
			@RequestParam(value="requester", required=false) String requesterId, // userId who is sending this query request
			@RequestParam(value="status", required=false) String status,
			@RequestParam(value="type", required=false) String type,
			@RequestParam(value="period", required=false) Integer period,
			@RequestParam(value="fromTime", required=false) String fromTime,
			@RequestParam(value="toTime", required=false) String toTime,
			@RequestParam(value="longitude", required=false) Double longitude,
			@RequestParam(value="latitude", required=false) Double latitude,
			@RequestParam(value="distance", required=false) String qsDistance, 
			@RequestParam(value="queryText", required=false) String queryText,
			@RequestParam(value="page", required=false) Integer pageNumber, 
			@RequestParam(value="per_page", required=false) Integer pageSize,
			@RequestParam(value="sortBy", required=false) String sortBy) {
		ActivitySideloadList responseBody = new ActivitySideloadList();
		List<Activity> activityList = activityService.findActivities(creatorId,requesterId,status,type,period,fromTime,toTime,
                                                                     longitude,latitude,qsDistance,queryText,
                                                                     pageNumber,pageSize,sortBy);
		Set<Member> members = new HashSet<Member>();
		if (activityList!=null) {
			for (Activity activity : activityList) {
				Member member = memberService.findByMemberId(activity.getCreator());
				// we must return an empty object so Ember can pick up the json data format.  Return null will crash the ember client.
				if (member!=null)
					members.add(member);
			}
		} else {
			// we must return an empty array so Ember can pick up the json data format.  Return null will crash the ember client.
			activityList = new ArrayList<Activity>();
		}
		responseBody.activities = activityList;
		responseBody.members = new ArrayList<Member>(members);
		/*		
		List<Activity> activityList = null;
		try {
			  activityList = activityService.findActivities(creatorId,requesterId,status,type,period,fromTime,toTime,
					                                        longitude,latitude,qsDistance,queryText);
		} catch (Exception err) {
			// we must return an empty array so Ember can pick up the json data format.  Return null will crash the ember client.
			activityList = new ArrayList<Activity>();
		}
		responseBody.put("activities", activityList);*/
		return responseBody;
	}

	// Get activity by ID
	@RequestMapping(value = "/activities/{id}", method = RequestMethod.GET, produces = "application/json")
	public Map<String,Activity> findByActivityId(@PathVariable("id") String id) {
		Map<String,Activity> responseBody = new HashMap<String,Activity>();			
		Activity activity = activityService.findByActivityId(id);
		responseBody.put("activity", activity);
		return responseBody;
	}
	
	// Add New Activity
	@RequestMapping(value = "/activities", method = RequestMethod.POST, produces = "application/json")
	public Map<String, Activity> addActivity(@RequestBody ActivitySideload newActivity) {
		Activity savedActivity = activityService.addActivity(newActivity.activity);
		
		Map<String, Activity> responseBody = new HashMap<String, Activity>();
		responseBody.put("activity", savedActivity);
		
		return responseBody;		
	}	
	
	// Update Activity
	@RequestMapping(value = "/activities/{id}", method = RequestMethod.PUT, produces = "application/json")
	public Map<String, Activity> updateActivity(@PathVariable("id") String id, @RequestBody ActivitySideload updatedActivity) {
		Activity savedActivity = activityService.updateActivity(id, updatedActivity.activity);
		Map<String, Activity> responseBody = new HashMap<String, Activity>();
		responseBody.put("activity", savedActivity);
		
		return responseBody;			
	}
	
	// Delete Activity
	@RequestMapping (value = "/activities/{id}", method = RequestMethod.DELETE, produces = "application/json")
	public void deleteActivity(@PathVariable("id")String id) {
		activityService.deleteActivity(id);
	}	
}
