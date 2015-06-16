package com.siwimi.webapi.web.controller;

import java.util.ArrayList;
import java.util.Date;
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

import com.siwimi.webapi.domain.Email;
import com.siwimi.webapi.domain.Feedback;
import com.siwimi.webapi.domain.Member;
import com.siwimi.webapi.domain.Question;
import com.siwimi.webapi.service.EmailService;
import com.siwimi.webapi.service.FeedbackService;
import com.siwimi.webapi.service.MemberService;
import com.siwimi.webapi.service.QuestionService;
import com.siwimi.webapi.web.dto.FeedbackSideload;
import com.siwimi.webapi.web.dto.FeedbackSideloadList;

@RestController
public class FeedbackController {

	@Autowired
	private FeedbackService feedbackService;
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private QuestionService questionService;
	
	@Autowired
	private EmailService emailService;
	
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
		Map <String,Member> members = new HashMap<String,Member>();		
		for (Feedback feedback : queryFeedbacks) {	

			feedbacks.add(feedback);
			// populate members
			String key = feedback.getCreator();
			if (!members.containsKey(key)) {
				members.put(key, memberService.findByMemberId(key));
			}	
		}

		responseBody.feedbacks = new ArrayList<Feedback>(feedbacks);	
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
			
		Member replier = memberService.findByMemberId(savedObj.getCreator());
		if (replier.getEmail() != null) {
			// Only send emails to the "question" type
			Question question = new Question();
			if (savedObj.getParentType() != null) {
				if (savedObj.getParentType().equals("question"))
					question = questionService.findByQuestionId(savedObj.getParent());
				else {
					Feedback parentComment = feedbackService.findById(savedObj.getParent());
					question = questionService.findByQuestionId(parentComment.getParent());
				}
			}

			// Sent email to the replier
			String subject1 = "Thank you for helping others!";
			String body1 = "Thank your for your help!  You've replied to a question posted by a local parent. " +
	                      "We've notified the parent about your reply.\n\n " +
				          "The question that you answer is :\n " +
                          "Title -- " + question.getTitle() + "\n " +
                          "Description -- " + question.getDescription() + "\n\n " + 
                          "Your answer is :\n" +
                          savedObj.getDescription() + "\n\n " +
                          "Best Regards," + "\n " + 
                          "The Siwimi Team";	        			
			List<String> recipent1 = new ArrayList<String> ();
			recipent1.add(replier.getEmail());
			Email notifyReplier = new Email();
			notifyReplier.setSentTo(recipent1);
			notifyReplier.setSubject(subject1);
			notifyReplier.setEmailText(body1);
			notifyReplier.setSentTime(new Date());		
			emailService.sentEmail(notifyReplier);
			emailService.addEmail(notifyReplier);	

			Member asker = memberService.findByMemberId(question.getCreator());
			// Sent email to asker
			if (asker.getEmail() != null) {
				String subject2 = "Someone replies your question on Siwimi.com!";
				String body2 = "Someone has answered the question that you've posted on Siwimi.com.\n\n " +
					          "The question that you ask is :\n " +
	                          "Title -- " + question.getTitle() + "\n " +
	                          "Description -- " + question.getDescription() + "\n\n " + 
	                          "The answer is :\n " +
	                          savedObj.getDescription() + "\n\n " +
	                          "Best Regards," + "\n " + 
	                          "The Siwimi Team";	
				List<String> recipent2 = new ArrayList<String> ();
				recipent2.add(asker.getEmail());
				Email notifyAsker = new Email();
				notifyAsker.setSentTo(recipent2);
				notifyAsker.setSubject(subject2);
				notifyAsker.setEmailText(body2);
				notifyAsker.setSentTime(new Date());		
				emailService.sentEmail(notifyAsker);
				emailService.addEmail(notifyAsker);	
			}
		}
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
