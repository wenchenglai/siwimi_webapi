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

import com.adarp.xiwami.domain.Feedback;
import com.adarp.xiwami.service.FeedbackService;
import com.adarp.xiwami.web.dto.FeedbackSideload;
import com.adarp.xiwami.web.dto.FeedbackSideloadList;

@RestController
public class FeedbackController {

	@Autowired
	private FeedbackService feedbackService;
	
	// Get all feedbacks
	@RequestMapping(value = "/feedbacks", method = RequestMethod.GET, produces = "application/json")
	public FeedbackSideloadList Find(
			@RequestParam(value="creator", required=false) String creatorId,
			@RequestParam(value="parent", required=false) String parentId,	
			@RequestParam(value="parentType", required=false) String parentType,				
			@RequestParam(value="queryText", required=false) String queryText) {
		
		FeedbackSideloadList responseBody = new FeedbackSideloadList();
		
		List<Feedback> feedbacks = new ArrayList<Feedback>();
		try {
			feedbacks = feedbackService.find(creatorId, parentId, parentType, queryText);
		} catch (Exception err) {
			// we must return an empty array so Ember can pick up the json data format.  Return null will crash the ember client.
		}
		
		List <Feedback> comments = new ArrayList<Feedback>();
		for (Feedback feedback : feedbacks) {
			comments.addAll(feedback.getComments()); 
		}
		
		responseBody.feedbacks = feedbacks;
		responseBody.comments = comments;
		
		return responseBody;
	}
	
	// Get by ID
	@RequestMapping(value = "/feedbacks/{id}", method = RequestMethod.GET, produces = "application/json")
	public Map<String, Feedback> FindById(@PathVariable("id") String id) {		
		Map<String, Feedback> responseBody = new HashMap<String, Feedback>();			
		Feedback obj = feedbackService.findById(id);
		responseBody.put("feedback", obj);
		return responseBody;
	}
	
	// Add New
	@RequestMapping(value = "/feedbacks", method = RequestMethod.POST, produces = "application/json")
	public Map<String, Feedback> add(@RequestBody FeedbackSideload newObj){
		Feedback savedObj = feedbackService.add(newObj.feedback);			
		Map<String, Feedback> responseBody = new HashMap<String, Feedback>();
		responseBody.put("feedback", savedObj);
		return responseBody;
	}	
			
	// Update
	@RequestMapping(value = "/feedbacks/{id}", method = RequestMethod.PUT, produces = "application/json")
	public Map<String, Feedback> update(@PathVariable("id") String id, @RequestBody FeedbackSideload updatedObj) {
		Feedback savedObj = feedbackService.update(id, updatedObj.feedback);		
		Map<String, Feedback> responseBody = new HashMap<String, Feedback>();
		responseBody.put("feedback", savedObj);
		return responseBody;		
	}
	
	// Delete
	@RequestMapping (value = "/feedbacks/{id}", method = RequestMethod.DELETE, produces = "application/json")
	public void delete(@PathVariable("id")String id) {
		feedbackService.delete(id);
	}	
}
