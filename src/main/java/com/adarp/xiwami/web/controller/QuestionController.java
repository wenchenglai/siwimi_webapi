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

import com.adarp.xiwami.service.QuestionService;
import com.adarp.xiwami.web.dto.QuestionSideload;
import com.adarp.xiwami.domain.Question;

@RestController
public class QuestionController {

	@Autowired
	private QuestionService questionService;

	// Get all questions
	@RequestMapping(value = "/questions", method = RequestMethod.GET, produces = "application/json")
	public Map<String,List<Question>> findQuestions(
			@RequestParam(value="creator", required=false) String creatorId,
			@RequestParam(value="longitude", required=false) Double longitude,
			@RequestParam(value="latitude", required=false) Double latitude,
			@RequestParam(value="distance", required=false) String qsDistance, 
			@RequestParam(value="queryText", required=false) String queryText) {
		Map<String,List<Question>> responseBody = new HashMap<String,List<Question>>();
		List<Question> questionList = null;
		try {
			questionList = questionService.findQuestions(creatorId,longitude,latitude,qsDistance,queryText);
		} catch (Exception err) {
			// we must return an empty array so Ember can pick up the json data format.  Return null will crash the ember client.
			questionList = new ArrayList<Question>();
		}
		responseBody.put("question", questionList);
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
	public void deleteQuestion(@PathVariable("id")String id) {
		questionService.deleteQuestion(id);
	}	
}
