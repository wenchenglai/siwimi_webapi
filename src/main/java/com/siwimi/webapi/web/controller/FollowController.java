package com.siwimi.webapi.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.siwimi.webapi.domain.Follow;
import com.siwimi.webapi.service.FollowService;
import com.siwimi.webapi.web.dto.FollowSideload;

@RestController
public class FollowController {

	@Autowired
	private FollowService followService; 
	
	// Get Follow by ID
	@RequestMapping(value = "/follows/{id}", method = RequestMethod.GET, produces = "application/json")
	public Map<String,Follow> findByFollowId(@PathVariable("id") String id) {
		Map<String,Follow> responseBody = new HashMap<String,Follow>();			
		Follow follow = followService.findByFollowId(id);
		responseBody.put("follow", follow);
		return responseBody;
	}
	
	// Get all Follows
	@RequestMapping(value = "/follows", method = RequestMethod.GET, produces = "application/json")
	public Map<String,List<Follow>> findFollows(
			@RequestParam(value="follower", required=false) String follower,
			@RequestParam(value="followee", required=false) String followee) {
		Map<String,List<Follow>> responseBody = new HashMap<String,List<Follow>>();
		List<Follow> followList = null;
		try {
			followList = followService.findFollows(follower,followee);
		} catch (Exception err) {
			// we must return an empty array so Ember can pick up the json data format.  Return null will crash the ember client.
			followList = new ArrayList<Follow>();
		}
		responseBody.put("follow", followList);
		return responseBody;
	}
	
	// Add New Follow
	@RequestMapping(value = "/follows", method = RequestMethod.POST, produces = "application/json")
	public Map<String, Follow> addFollow(@RequestBody FollowSideload newFollow) {
		Follow savedFollow = followService.addFollow(newFollow.follow);		
		Map<String,Follow> responseBody = new HashMap<String, Follow>();
		responseBody.put("follow", savedFollow);
		return responseBody;			
	}	
		
	// Update Follow
	@RequestMapping(value = "/follows/{id}", method = RequestMethod.PUT, produces = "application/json")
	public Map<String, Follow> updateFollow(@PathVariable("id") String id, @RequestBody FollowSideload updatedFollow){
		Follow savedFollow = followService.updateFollow(id, updatedFollow.follow);		
		Map<String,Follow> responseBody = new HashMap<String, Follow>();
		responseBody.put("follow", savedFollow);
		return responseBody;			
	}
		
	// Delete Follow
	@RequestMapping (value = "/follows/{id}", method = RequestMethod.DELETE, produces = "application/json")
	public void deleteFollow(@PathVariable("id")String id, HttpServletResponse response) {
		if (followService.deleteFollow(id) != null)
			response.setStatus(HttpServletResponse.SC_NO_CONTENT);
		else
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
	}	
}
