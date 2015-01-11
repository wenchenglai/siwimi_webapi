package com.adarp.xiwami.web.controller;

import java.util.ArrayList;
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

import com.adarp.xiwami.domain.Discussion;
import com.adarp.xiwami.service.DiscussionService;
import com.adarp.xiwami.web.dto.DiscussionSideload;

@RestController
public class DiscussionController {

	@Autowired
	private DiscussionService discussionService;
	
	// Get discussions by type
	@RequestMapping(value = "/discussions", method = RequestMethod.GET, produces = "application/json")
	public Map<String, List<Discussion>> Find(
			@RequestParam(value="creator", required=false) String creatorId,
			@RequestParam(value="entity", required=false) String entityId,	
			@RequestParam(value="entityType", required=false) String entityType,				
			@RequestParam(value="queryText", required=false) String queryText) {
		
		Map<String, List<Discussion>> responseBody = new HashMap<String, List<Discussion>>();
		
		List<Discussion> list = null;
		try {
			list = discussionService.find(creatorId, entityId, entityType, queryText);
		} catch (Exception err) {
			// we must return an empty array so Ember can pick up the json data format.  Return null will crash the ember client.
			list = new ArrayList<Discussion>();

		}
		responseBody.put("discussions", list);
		return responseBody;
	}
	
	// Get by ID
	@RequestMapping(value = "/discussions/{id}", method = RequestMethod.GET, produces = "application/json")
	public Map<String, Discussion> FindById(@PathVariable("id") String id) {		
		Map<String, Discussion> responseBody = new HashMap<String, Discussion>();			
		Discussion obj = discussionService.findById(id);
		responseBody.put("discussion", obj);
		return responseBody;
	}
	
	// Add New
	@RequestMapping(value = "/discussions", method = RequestMethod.POST, produces = "application/json")
	public Map<String, Discussion> add(@RequestBody DiscussionSideload newObj){
		Discussion savedObj = discussionService.add(newObj.discussion);			
		Map<String, Discussion> responseBody = new HashMap<String, Discussion>();
		responseBody.put("discussion", savedObj);
		return responseBody;
	}	
	
	// Update
	@RequestMapping(value = "/discussions/{id}", method = RequestMethod.PUT, produces = "application/json")
	public Map<String, Discussion> update(@PathVariable("id") String id, @RequestBody DiscussionSideload updatedObj) {
		Discussion savedObj = discussionService.update(id, updatedObj.discussion);		
		Map<String, Discussion> responseBody = new HashMap<String, Discussion>();
		responseBody.put("discussion", savedObj);
		return responseBody;		
	}
	
	// Delete
	@RequestMapping (value = "/discussions/{id}", method = RequestMethod.DELETE, produces = "application/json")
	public void delete(@PathVariable("id")String id) {
		discussionService.delete(id);
	}	
}
