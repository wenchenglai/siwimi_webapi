package com.siwimi.webapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.siwimi.webapi.domain.Location;
import com.siwimi.webapi.domain.Question;
import com.siwimi.webapi.repository.FavoriteRepository;
import com.siwimi.webapi.repository.LocationRepository;
import com.siwimi.webapi.repository.QuestionRepository;

@Service
public class QuestionService {

	@Autowired
	private QuestionRepository questionRep;
	
	@Autowired
	private FavoriteRepository favoriteRep;	
	
	@Autowired
	private LocationRepository locationRep;
	
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
		return questionRep.findByIdAndIsDeletedRecordIsFalse(id);
	}
	
	public Question addQuestion(Question newQuestion) {
		newQuestion.setIsDeletedRecord(false);
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
		question.setIsDeletedRecord(true);
		questionRep.saveQuestion(question);
	}
	
	public Question updateLocation(Question question) {
		// lookup location from the collection Location;
		Location thisLocation = locationRep.queryLocation(question.getZipCode(), question.getCity(), question.getState());
		// set longitude and latitude 
		if (thisLocation!=null) {
			double[] location = {thisLocation.getLongitude(), thisLocation.getLatitude()};
			question.setZipCode(thisLocation.getZipCode());
			question.setLocation(location);
			question.setCity(thisLocation.getTownship());
			question.setState(thisLocation.getStateCode());
		}

		return question;
	}
}
