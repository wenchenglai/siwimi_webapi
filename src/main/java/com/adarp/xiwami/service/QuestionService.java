package com.adarp.xiwami.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adarp.xiwami.domain.Question;
import com.adarp.xiwami.domain.ZipCode;
import com.adarp.xiwami.repository.FavoriteRepository;
import com.adarp.xiwami.repository.QuestionRepository;
import com.adarp.xiwami.repository.ZipCodeRepository;

@Service
public class QuestionService {

	@Autowired
	private QuestionRepository questionRep;
	
	@Autowired
	private FavoriteRepository favoriteRep;	
	
	@Autowired
	private ZipCodeRepository zipCodeRep;
	
	public List<Question> findQuestions(String creatorId, 
			 							String requesterId, 
			                            Double longitude, Double latitude, String qsDistance, 
			                            String queryText) {
		List<Question> questionList = questionRep.queryQuestion(creatorId, longitude,latitude,qsDistance,queryText);
		
		// increment viewcount by 1, and save it to MongoDB
		for (int i=0; i<questionList.size();i++) {
			Question question = questionList.get(i);
			// Populate isFavorite
			if (favoriteRep.queryFavorite(requesterId, question.getId(), "question") != null) {
				question.setIsFavorite(true);
			}
			questionList.set(i, question);
			// increment viewcount by 1, and save it to MongoDB
			question.setViewCount(question.getViewCount()+1);
			questionRep.saveQuestion(question);
		}
		
		return questionList;
	}
	
	public Question findByQuestionId(String id) {
		return questionRep.findByIdAndIsDestroyedIsFalse(id);
	}
	
	public Question addQuestion(Question newQuestion) {
		newQuestion.setIsDestroyed(false);
		newQuestion.setViewCount(0);
		newQuestion = updateLocation(newQuestion);
		return questionRep.saveQuestion(newQuestion);
	}
	
	public Question updateQuestion(String id, Question updatedQuestion) {
		updatedQuestion.setId(id);
		updatedQuestion = updateLocation(updatedQuestion);
		return questionRep.saveQuestion(updatedQuestion);
	}
	
	public void deleteQuestion(String id) {
		Question question = questionRep.findOne(id);
		question.setIsDestroyed(true);
		questionRep.saveQuestion(question);
	}
	
	public Question updateLocation(Question question) {
		// lookup zipcode from the collection ZipCode;
		ZipCode thisZipCode = zipCodeRep.queryZipCode(question.getZipCode(), question.getCity(), question.getState());
		// set longitude and latitude 
		if (thisZipCode!=null) {
			double[] location = {thisZipCode.getLongitude(), thisZipCode.getLatitude()};
			question.setZipCode(thisZipCode.getZipCode());
			question.setLocation(location);
			question.setCity(thisZipCode.getTownship());
			question.setState(thisZipCode.getStateCode());
		}

		return question;
	}
}
