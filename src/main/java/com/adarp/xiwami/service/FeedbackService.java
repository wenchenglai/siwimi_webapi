package com.adarp.xiwami.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adarp.xiwami.domain.Feedback;
import com.adarp.xiwami.repository.FeedbackRepository;

@Service
public class FeedbackService {

	@Autowired
	FeedbackRepository feedbackRep;
	
	public List<Feedback> find(String creator, String parentId, String parentType, String queryText) {
		List<Feedback> feedbacks = feedbackRep.query(creator, parentId, parentType, queryText);
		
		// increment viewcount by 1, and save it to MongoDB
		for (int i=0; i<feedbacks.size(); i++) {
			Feedback feedback = feedbacks.get(i);
			feedback.setViewCount(feedback.getViewCount()+1);
			feedbackRep.save(feedback);
			feedbacks.set(i, feedback);
		}
		
		return feedbacks;
	}
	
	public Feedback findById(String id) {
		return feedbackRep.findOne(id);
	}
	
	public Feedback add(Feedback newObj) {
		newObj.setIsDestroyed(false);
		newObj.setViewCount(0);
		newObj.setLikeCount(0);
		return feedbackRep.save(newObj);
	}
	
	public Feedback update(String id, Feedback updatedObj) {
		updatedObj.setId(id);
		return feedbackRep.save(updatedObj);
	}
	
	public void delete(String id) {
		Feedback obj = feedbackRep.findOne(id);
		obj.setIsDestroyed(true);
		feedbackRep.save(obj);
	}
}

