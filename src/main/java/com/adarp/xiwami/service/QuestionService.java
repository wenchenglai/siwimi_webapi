package com.adarp.xiwami.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adarp.xiwami.domain.Question;
import com.adarp.xiwami.domain.ZipCode;
import com.adarp.xiwami.repository.QuestionRepository;
import com.adarp.xiwami.repository.ZipCodeRepository;

@Service
public class QuestionService {

	@Autowired
	QuestionRepository questionRep;
	
	@Autowired
	private ZipCodeRepository zipCodeRep;
	
	public List<Question> findQuestions(String creatorId, Double longitude, Double latitude, String qsDistance, String queryText) {
		List<Question> questionList = questionRep.queryQuestion(creatorId, longitude,latitude,qsDistance,queryText);
		
		// increment viewcount by 1, and save it to MongoDB
		for (int i=0; i<questionList.size();i++) {
			Question question = questionList.get(i);
			question.setViewCount(question.getViewCount()+1);
			questionRep.saveQuestion(question);
			questionList.set(i, question);
		}
		
		return questionList;
	}
	
	public Question findByQuestionId(String id) {
		return questionRep.findOne(id);
	}
	
	public Question addQuestion(Question newQuestion) {
		newQuestion.setIsDeleted(false);
		newQuestion.setViewCount(0);
		newQuestion = updateZipCode(newQuestion);
		return questionRep.saveQuestion(newQuestion);
	}
	
	public Question updateQuestion(String id, Question updatedQuestion) {
		updatedQuestion.setId(id);
		updatedQuestion = updateZipCode(updatedQuestion);
		return questionRep.saveQuestion(updatedQuestion);
	}
	
	public void deleteQuestion(String id) {
		Question question = questionRep.findOne(id);
		question.setIsDeleted(true);
		questionRep.saveQuestion(question);
	}
	
	public Question updateZipCode(Question question) {
		// lookup zipcode from the collection ZipCode;
			ZipCode thisZipCode = new ZipCode();
						
		// if the zipCode is not provided by the user
			if (question.getZipCode()==null) {				
				if (question.getCityState()==null){
					// if both zipcode and cityState are not completed, set default to 48105
					thisZipCode = zipCodeRep.findByzipCode(48105);
				} else {
					String [] parts = question.getCityState().split(",");
					String city = parts[0].trim();
					String stateCode = parts[1].trim();	
					thisZipCode = zipCodeRep.findByTownshipLikeIgnoreCaseAndStateCodeLikeIgnoreCase(city, stateCode);				
				}						
			} else {
				/** if the zipCode is provided by the user:
				   (1) ignore stateCity provided by the user, 
				   (2) lookup zipcode from the collection ZipCode
				   (3) please note that the type of zipcode is "int" in the mongoDB collection 
				**/
				thisZipCode = zipCodeRep.findByzipCode(Integer.parseInt(question.getZipCode()));			
			}
			
			// set longitude and latitude of the family object 
			double[] location = {thisZipCode.getLongitude(), thisZipCode.getLatitude()};
			question.setZipCode(thisZipCode.getZipCode());
			question.setLocation(location);
			question.setCityState(thisZipCode.getTownship()+", "+thisZipCode.getStateCode());
			
			return question;
	}
}
