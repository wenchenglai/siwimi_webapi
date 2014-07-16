package com.adarp.xiwami.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
	public Map<String,List<Question>> FindQuestions() {
		Map<String,List<Question>> responseBody = new HashMap<String,List<Question>>();
		List<Question> list = questionService.FindQuestions();
		responseBody.put("question", list);
		return responseBody;
	}

	// Get Question by ID
	@RequestMapping(value = "/questions/{id}", method = RequestMethod.GET, produces = "application/json")
	public Map<String,Question> FindByQuestionId(@PathVariable("id") String id) {
		Map<String,Question> responseBody = new HashMap<String,Question>();			
		Question question = questionService.FindByQuestionId(id);
		responseBody.put("question", question);
		return responseBody;
	}
	
	// Add New Question
	@RequestMapping(value = "/questions", method = RequestMethod.POST, produces = "application/json")
	public void AddQuestion(@RequestBody QuestionSideload newQuestion) {
		questionService.AddQuestion(newQuestion.question);	
	}	
	
	// Update Question
	@RequestMapping(value = "/questions/{id}", method = RequestMethod.PUT, produces = "application/json")
	public void EditQuestion(@PathVariable("id") String id, @RequestBody QuestionSideload updatedQuestion){
		questionService.EditQuestion(id, updatedQuestion.question);
	}
	
	// Delete Question
	@RequestMapping (value = "/questions/{id}", method = RequestMethod.DELETE, produces = "application/json")
	public void DeleteQuestion(@PathVariable("id")String id) {
		questionService.DeleteQuestion(id);
	}	
}
