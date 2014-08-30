package com.adarp.xiwami.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.adarp.xiwami.domain.Survey;
import com.adarp.xiwami.service.SurveyService;

@RestController
public class SurveyController {

	@Autowired
	private SurveyService surveyService;	
	
	// Add New Survey
	@RequestMapping(value = "/surveys", method = RequestMethod.POST, produces = "application/json")
	public Map<String, Survey> AddSurvey(@RequestBody Survey newSurvey) {
		Survey savedSurvey = surveyService.AddSurvey(newSurvey);
		
		Map<String, Survey> responseBody = new HashMap<String, Survey>();
		responseBody.put("survey", savedSurvey);
		
		return responseBody;		
	}		
}
