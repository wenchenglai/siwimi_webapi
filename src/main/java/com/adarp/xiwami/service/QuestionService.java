package com.adarp.xiwami.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adarp.xiwami.domain.Question;
import com.adarp.xiwami.repository.QuestionRepository;

@Service
public class QuestionService {

	@Autowired
	QuestionRepository questionRep;
	
	public List<Question> FindQuestions() {
		return questionRep.findAll();
	}
	
	public Question FindByQuestionId(String id) {
		return questionRep.findOne(id);
	}
	
	public Question AddQuestion(Question newQuestion) {
		newQuestion.setIsDeleted(false);
		return questionRep.save(newQuestion);
	}
	
	public Question UpdateQuestion(String id, Question updatedQuestion) {
		updatedQuestion.setId(id);
		return questionRep.save(updatedQuestion);
	}
	
	public void DeleteQuestion(String id) {
		Question question = questionRep.findOne(id);
		question.setIsDeleted(true);
		questionRep.save(question);
	}
}
