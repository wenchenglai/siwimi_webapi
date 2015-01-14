package com.adarp.xiwami.service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adarp.xiwami.domain.Feedback;
import com.adarp.xiwami.repository.FeedbackRepository;

@Service
public class FeedbackService {

	@Autowired
	FeedbackRepository feedbackRep;
	
	public List<String> find(String creator, String parentId, String parentType, String queryText) {
		List<Feedback> feedbacks = feedbackRep.query(creator, parentId, parentType, queryText);
		
		List<String> feedbackId = new ArrayList<String>();
		for (int i=0; i<feedbacks.size(); i++) {
			// increment viewcount by 1, and save it to MongoDB
			Feedback feedback = feedbacks.get(i);
			feedback.setViewCount(feedback.getViewCount()+1);
			feedbackRep.save(feedback);
			// Save feedback id into array
			feedbackId.add(feedback.getId());
		}
		
		return feedbackId;
	}
	
	public Feedback findById(String id) {
		return feedbackRep.findOne(id);
	}
	
	public Feedback add(Feedback newObj) {
		newObj.setIsDestroyed(false);
		newObj.setViewCount(0);
		newObj.setLikeCount(0);
		Feedback saveObj = feedbackRep.save(newObj);
		
		// if newObj is a comment, we need to update its parent.
		if (saveObj.getParentType() == null) {
			Feedback feedback = feedbackRep.findOne(saveObj.getParent());
			Set <String> commentSet = new LinkedHashSet<String>(feedback.getComments());
			commentSet.add(saveObj.getId());
			feedback.setComments(new ArrayList<String>(commentSet));
			feedbackRep.save(feedback);
		}
		
		return saveObj;
	}
	
	public Feedback update(String id, Feedback updatedObj) {
		updatedObj.setId(id);
		
		// if updatedObj is a comment, we need to update its parent.
		if (updatedObj.getParentType() == null) {
			Feedback feedback = feedbackRep.findOne(updatedObj.getParent());
			Set <String> commentSet = new LinkedHashSet<String>(feedback.getComments());
			commentSet.add(updatedObj.getId());
			feedback.setComments(new ArrayList<String>(commentSet));
			feedbackRep.save(feedback);
		}	
		
		return feedbackRep.save(updatedObj);
	}
	
	public void delete(String id) {
		Feedback obj = feedbackRep.findOne(id);
		obj.setIsDestroyed(true);
		feedbackRep.save(obj);
	}
}

