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

import com.siwimi.webapi.domain.Feedback;
import com.siwimi.webapi.domain.Member;
import com.siwimi.webapi.service.FeedbackService;
import com.siwimi.webapi.service.MemberService;
import com.siwimi.webapi.web.dto.FeedbackSideload;
import com.siwimi.webapi.web.dto.FeedbackSideloadList;

@RestController
public class FeedbackController {

	@Autowired
	private FeedbackService feedbackService;
	
	@Autowired
	private MemberService memberService;
	
	// Get all feedbacks
	@RequestMapping(value = "/feedbacks", method = RequestMethod.GET, produces = "application/json")
	public FeedbackSideloadList find(
			@RequestParam(value="creator", required=false) String creatorId,
			@RequestParam(value="parent", required=false) String parentId,	
			@RequestParam(value="parentType", required=false) String parentType,				
			@RequestParam(value="queryText", required=false) String queryText) {
		
		FeedbackSideloadList responseBody = new FeedbackSideloadList();
		
		// idList could be either Feedback or Comment.
		List<Feedback> queryFeedbacks = feedbackService.find(creatorId, parentId, parentType, queryText);
		
		/** Populate FeedbackSideloadList **/
		Set <Feedback> feedbacks = new HashSet<Feedback>();
		Set <Feedback> comments = new HashSet<Feedback>();
		Map <String,Member> members = new HashMap<String,Member>();		
		for (Feedback feedback : queryFeedbacks) {	
			// populate feedbacks or comments
			if (feedback.getParentType()==null) {
				comments.add(feedback);
			} else {
				feedbacks.add(feedback);
			}
			// populate members
			String key = feedback.getCreator();
			if (!members.containsKey(key)) {
				members.put(key, memberService.findByMemberId(key));
			}	
		}

		responseBody.feedbacks = new ArrayList<Feedback>(feedbacks);
		responseBody.comments = new ArrayList<Feedback>(comments);			
		responseBody.members = new ArrayList<Member>(members.values());
		
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
