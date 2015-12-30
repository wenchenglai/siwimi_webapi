package com.siwimi.webapi.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.siwimi.webapi.domain.ActivitySite;
import com.siwimi.webapi.repository.ActivitySiteRepository;
import com.siwimi.webapi.web.dto.ActivitySiteRoot;


@RestController
public class ActivitySiteController {

	@Autowired
	private ActivitySiteRepository activitySiteRep;
	
	// Get all activity Sites
	@RequestMapping(value = "/activitySites", method = RequestMethod.GET, produces = "application/json")
	public Map<String, List<ActivitySite>> getActivitySites() {
		Map<String, List<ActivitySite>> responseBody = new HashMap<String, List<ActivitySite>>();
		responseBody.put("activitySite", activitySiteRep.findAll());

		return responseBody;
	}
	
	// Get site by ID
	@RequestMapping(value = "/activitySites/{id}", method = RequestMethod.GET, produces = "application/json")
	public Map<String, ActivitySite> findById(@PathVariable("id") String id) {
		Map<String, ActivitySite> responseBody = new HashMap<String, ActivitySite>();			
		responseBody.put("activitySite", activitySiteRep.findOne(id));
		return responseBody;
	}	

	// Manually add new activity site
	@RequestMapping(value = "/activitySites", method = RequestMethod.POST, produces = "application/json")
	public ActivitySite addActivitySite(@RequestBody ActivitySiteRoot newActivitySite) {		
		return activitySiteRep.save(newActivitySite.activitySite);		
	}	
	
	// Update Activity site
	@RequestMapping(value = "/activitySites/{id}", method = RequestMethod.PUT, produces = "application/json")
	public Map<String, ActivitySite> updateActivitySite(HttpServletResponse response,
			                           @PathVariable("id") String id, 
			                           @RequestBody ActivitySiteRoot updatedActivitySite) {
		ActivitySite site = activitySiteRep.findOne(id);
		if (site != null) {
			updatedActivitySite.activitySite.setId(id);
			response.setStatus(HttpServletResponse.SC_OK);
			ActivitySite as = activitySiteRep.save(updatedActivitySite.activitySite);
			
			Map<String, ActivitySite> responseBody = new HashMap<String, ActivitySite>();
			responseBody.put("activitySite", as);
			return responseBody;
		} else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;			
		}
	}
	
	// Delete Activity site
	@RequestMapping (value = "/activitySites/{id}", method = RequestMethod.DELETE, produces = "application/json")
	public void deleteActivitySite(@PathVariable("id")String id, HttpServletResponse response) {
		activitySiteRep.delete(id);
	}	
}
