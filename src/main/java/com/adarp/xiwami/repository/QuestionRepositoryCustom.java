package com.adarp.xiwami.repository;

import com.adarp.xiwami.domain.Question;

import java.util.List;


public interface QuestionRepositoryCustom {
	
	public List<Question> GetQuestions() throws Exception;
	
	public Question GetQuestionById(String id) throws Exception;
	
	public void AddQuestion(Question newQuestion) throws Exception;
	
	public void UpdateQuestion(String id, Question updateQuestion) throws Exception;
	
	public void DeleteQuestion(String id) throws Exception;		
}