package com.adarp.xiwami.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adarp.xiwami.domain.Survey;
import com.adarp.xiwami.repository.SurveyRepository;

@Service
public class SurveyService {

	@Autowired
	SurveyRepository surveyRep;
	
	public Survey AddSurvey(Survey newSurvey) {
		return surveyRep.save(newSurvey);
	}	
	
	public List<Survey> viewSurvey() {
		return surveyRep.findAll();
	}
	
	public void DeleteSurvey(String id) {
		surveyRep.delete(id);		
	}
}
