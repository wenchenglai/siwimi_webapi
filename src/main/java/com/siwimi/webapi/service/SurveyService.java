package com.siwimi.webapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.siwimi.webapi.domain.Survey;
import com.siwimi.webapi.repository.SurveyRepository;

@Service
public class SurveyService {

	@Autowired
	private SurveyRepository surveyRep;
	
	public Survey addSurvey(Survey newSurvey) {
		return surveyRep.save(newSurvey);
	}	
	
	public List<Survey> viewSurvey() {
		return surveyRep.findAll();
	}
	
	public void deleteSurvey(String id) {
		surveyRep.delete(id);		
	}
	
	public void updateServey(Survey updatedSurvey) {
		surveyRep.save(updatedSurvey);
	}
}
