package com.adarp.xiwami.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.adarp.xiwami.domain.Survey;
import com.adarp.xiwami.service.SurveyService;

@RestController
public class SurveyController {

	@Autowired
	private SurveyService surveyService;	
	
	// Add New Survey
	@RequestMapping(value = "/surveys", method = RequestMethod.POST, produces = "application/json")
	public Map<String, Survey> addSurvey(@RequestBody Survey newSurvey) {
		Survey savedSurvey = surveyService.addSurvey(newSurvey);
		
		Map<String, Survey> responseBody = new HashMap<String, Survey>();
		responseBody.put("survey", savedSurvey);
		
		return responseBody;		
	}		
	
	// View all Surveys
	@RequestMapping(value = "/surveys", method = RequestMethod.GET, produces = "application/json")
	public ModelAndView viewSurvey() {
		ModelAndView model = new ModelAndView("viewSurveys");
		List<Survey> surveys = surveyService.viewSurvey();
		model.addObject("surveys", surveys);
		return model;		
	}
	
	// Delete a survey
	@RequestMapping(value = "/surveys/delete", method = RequestMethod.POST, produces = "application/json")
	public ModelAndView deleteSurvey(@RequestParam String id, Model model) {
		surveyService.deleteSurvey(id);
		return new ModelAndView("redirect:/surveys");
	}
	
	// Update a survey
	@RequestMapping(value = "/surveys/update", method = RequestMethod.POST, produces = "application/json")
	public ModelAndView updateSurvey(@ModelAttribute(value="survey") Survey updatedSurvey) {		
		surveyService.updateServey(updatedSurvey);
		return new ModelAndView("redirect:/surveys");
	}
}
