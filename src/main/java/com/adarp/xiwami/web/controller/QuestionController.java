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

import com.adarp.xiwami.repository.*;
import com.adarp.xiwami.domain.Question;
import com.adarp.xiwami.web.dto.*;

@RestController
public class QuestionController {

	@Autowired
	private QuestionRepository questionRep;

	// Get all questions
	@RequestMapping(value = "/questions", method = RequestMethod.GET, produces = "application/json")
	public Map<String,List<Question>> FindQuestions() {
		try {				
			Map<String,List<Question>> responseBody = new HashMap<String,List<Question>>();
			//List<Question> list = questionRep.GetQuestions();
			List<Question> list = questionRep.findAll();
			responseBody.put("question", list);
			return responseBody;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error : unable to query Question.");
			return null;
		}
	}

	// Get Question by ID
	@RequestMapping(value = "/questions/{id}", method = RequestMethod.GET, produces = "application/json")
	public Map<String,Question> FindByQuestionId(@PathVariable("id") String id) {
		
		try {
			Map<String,Question> responseBody = new HashMap<String,Question>();			
			//Question question = questionRep.GetQuestionById(id);
			Question question = questionRep.findOne(id);
			responseBody.put("question", question);
			return responseBody;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error : unable to query Question.");
			return null;
		}
	}
	
	// Add New Question
	@RequestMapping(value = "/questions", method = RequestMethod.POST, produces = "application/json")
	public void AddActivity(@RequestBody QuestionSideload newQuestion)
	{
		try {
			//questionRep.AddQuestion(newQuestion.question);
			questionRep.save(newQuestion.question);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error : unable to add Question.");
		}		
	}	
	
	// Update Question
	@RequestMapping(value = "/questions/{id}", method = RequestMethod.PUT, produces = "application/json")
	public void EditFamily(@PathVariable("id") String id, @RequestBody QuestionSideload updatedQuestion)
	{
		try {
			questionRep.UpdateQuestion(id,updatedQuestion.question);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error : unable to update Activity.");
		}
	}
	
	// Delete Question
	@RequestMapping (value = "/questions/{id}", method = RequestMethod.DELETE, produces = "application/json")
	public void DeleteQuestion(@PathVariable("id")String id) {
		try {
			questionRep.DeleteQuestion(id);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error : unable to delete Question.");			
		}
	}	
}
