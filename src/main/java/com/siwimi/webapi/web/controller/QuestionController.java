package com.siwimi.webapi.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.siwimi.webapi.domain.Feedback;
import com.siwimi.webapi.domain.Member;
import com.siwimi.webapi.domain.Question;
import com.siwimi.webapi.service.EmailService;
import com.siwimi.webapi.service.FeedbackService;
import com.siwimi.webapi.service.MemberService;
import com.siwimi.webapi.service.QuestionService;
import com.siwimi.webapi.web.dto.QuestionSideload;
import com.siwimi.webapi.web.dto.QuestionSideloadList;

@RestController
public class QuestionController {

	@Autowired
	private QuestionService questionService;

	@Autowired
	private MemberService memberService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private FeedbackService feedbackService;
	
	// Get all questions
	@RequestMapping(value = "/questions", method = RequestMethod.GET, produces = "application/json")
	public QuestionSideloadList findQuestions(
			@RequestParam(value="creator", required=false) String creatorId,
			@RequestParam(value="requester", required=false) String requesterId, // userId who is sending this query request
			@RequestParam(value="longitude", required=false) Double longitude,
			@RequestParam(value="latitude", required=false) Double latitude,
			@RequestParam(value="distance", required=false) String qsDistance, 
			@RequestParam(value="queryText", required=false) String queryText) {
		QuestionSideloadList responseBody = new QuestionSideloadList();
		List<Question> questionList = questionService.findQuestions(creatorId,requesterId,longitude,latitude,qsDistance,queryText);
		Set<Member> members = new HashSet<Member>();
		if ((questionList!=null) && (!questionList.isEmpty())) {
			for (Question question : questionList) {
				Member member = memberService.findByMemberId(question.getCreator());
				// we must return an empty object so Ember can pick up the json data format.  Return null will crash the ember client.
				if (member!=null)
					members.add(member);
				// Populate replies
				List<Feedback> feedbacks = feedbackService.find(null, question.getId(), "question", null);
				if ((feedbacks!=null) && (!feedbacks.isEmpty())) {
					for (Feedback feedback : feedbacks) {
						List<String> replies = question.getReplies();
						replies.add(feedback.getId());
						question.setReplies(replies);
					}
				}				
			}
		} else {
			// we must return an empty array so Ember can pick up the json data format.  Return null will crash the ember client.
			questionList = new ArrayList<Question>();
		}
		responseBody.questions = questionList;
		responseBody.members = new ArrayList<Member>(members);

		return responseBody;
	}

	// Get Question by ID
	@RequestMapping(value = "/questions/{id}", method = RequestMethod.GET, produces = "application/json")
	public Map<String,Question> findByQuestionId(@PathVariable("id") String id) {
		Map<String,Question> responseBody = new HashMap<String,Question>();			
		Question question = questionService.findByQuestionId(id);
		responseBody.put("question", question);
		return responseBody;
	}
	
	// Add New Question
	@RequestMapping(value = "/questions", method = RequestMethod.POST, produces = "application/json")
	public Map<String, Question> addQuestion(@RequestBody QuestionSideload newQuestion) {
		Question savedQuestion = questionService.addQuestion(newQuestion.question);		
		Map<String,Question> responseBody = new HashMap<String, Question>();
		responseBody.put("question", savedQuestion);
		// email to Siwimi.com
		emailService.notifyNewQuestionToSiwimi(savedQuestion);
		// email to asker
		emailService.notifyNewQuestionToAsker(savedQuestion);		
		return responseBody;			
	}	
	
	// Update Question
	@RequestMapping(value = "/questions/{id}", method = RequestMethod.PUT, produces = "application/json")
	public Map<String, Question> updateQuestion(@PathVariable("id") String id, @RequestBody QuestionSideload updatedQuestion){
		Question savedQuestion = questionService.updateQuestion(id, updatedQuestion.question);		
		Map<String,Question> responseBody = new HashMap<String, Question>();
		responseBody.put("question", savedQuestion);
		return responseBody;			
	}
	
	// Delete Question
	@RequestMapping (value = "/questions/{id}", method = RequestMethod.DELETE, produces = "application/json")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteQuestion(@PathVariable("id")String id, HttpServletResponse response) {
		if (questionService.deleteQuestion(id)!= null)
			response.setStatus(HttpServletResponse.SC_NO_CONTENT);
		else
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
	}	
}
