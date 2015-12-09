package com.siwimi.webapi.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.siwimi.webapi.domain.ActivitySite;
import com.siwimi.webapi.repository.ActivitySiteRepository;


@RestController
public class ActivitySiteController {

	@Autowired
	private ActivitySiteRepository activitySiteRep;
	
	// Get all activity Sites
	@RequestMapping(value = "/activitySites", method = RequestMethod.GET, produces = "application/json")
	public List<ActivitySite> getActivitySites() {
		return activitySiteRep.findAll();
	}

	// Manually add new activity site
	@RequestMapping(value = "/activitySites", method = RequestMethod.POST, produces = "application/json")
	public ActivitySite addActivitySite(@RequestBody ActivitySite newActivitySite) {		
		return activitySiteRep.save(newActivitySite);		
	}	
	
	// Update Activity site
	@RequestMapping(value = "/activitySites/{id}", method = RequestMethod.PUT, produces = "application/json")
	public ActivitySite updateActivitySite(HttpServletResponse response,
			                           @PathVariable("id") String id, 
			                           @RequestBody ActivitySite updatedActivitySite) {
		ActivitySite site = activitySiteRep.findOne(id);
		if (site != null) {
			updatedActivitySite.setId(id);
			response.setStatus(HttpServletResponse.SC_OK);
			return activitySiteRep.save(updatedActivitySite);
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
