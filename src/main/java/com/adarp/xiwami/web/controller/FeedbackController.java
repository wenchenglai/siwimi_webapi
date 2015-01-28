package com.adarp.xiwami.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
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
	public FeedbackSideloadList find(
			@RequestParam(value="creator", required=false) String creatorId,
			@RequestParam(value="parent", required=false) String parentId,	
			@RequestParam(value="parentType", required=false) String parentType,				
			@RequestParam(value="queryText", required=false) String queryText) {
		
		FeedbackSideloadList responseBody = new FeedbackSideloadList();
		
		// idList could be either Feedback or Comment.
		List<String> idList = new ArrayList<String>();
		idList = feedbackService.find(creatorId, parentId, parentType, queryText);

		Set <Feedback> feedbacks = new LinkedHashSet<Feedback>();
		Set <Feedback> comments = new LinkedHashSet<Feedback>();
		for (String id : idList) {
			// feedback : Feedback or Comment from the document "Feedback".
			Feedback feedback = feedbackService.findById(id);			
			if (feedback.getParentType()==null) {
				// feedback is comment ==> need to retreive its related Feedback & comments.			
				
				Feedback parent = feedbackService.findById(feedback.getParent());
				// export parent
				feedbacks.add(parent);
				// export comment
				for (String commentId : parent.getComments()) {
					comments.add(feedbackService.findById(commentId));
				}										
			} else {
				// feedback is Feedback

				// export parent
				feedbacks.add(feedback);
				// export comment
				for (String commentId : feedback.getComments()) {
					comments.add(feedbackService.findById(commentId));
				}	
			}
		}
		
		responseBody.feedbacks = new ArrayList<Feedback>(feedbacks);
		responseBody.comments = new ArrayList<Feedback>(comments);
		
		return responseBody;
	}
	
	// Get by ID
	@RequestMapping(value = "/feedbacks/{id}", method = RequestMethod.GET, produces = "application/json")
	public Map<String, Feedback> findById(@PathVariable("id") String id) {		
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
