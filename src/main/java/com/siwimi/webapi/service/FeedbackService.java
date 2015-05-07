package com.siwimi.webapi.service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.siwimi.webapi.domain.Feedback;
import com.siwimi.webapi.repository.FeedbackRepository;

@Service
public class FeedbackService {

	@Autowired
	private FeedbackRepository feedbackRep;
	
	public List<Feedback> find(String creator, String parentId, String parentType, String queryText) {
		List<Feedback> feedbacks = feedbackRep.query(creator, parentId, parentType, queryText);
		
		for (int i=0; i<feedbacks.size(); i++) {
			// increment viewcount by 1, and save it to MongoDB
			Feedback feedback = feedbacks.get(i);
			feedback.setViewCount(feedback.getViewCount()+1);
			feedbackRep.save(feedback);
			//feedbacks.set(i, feedback);
		}
		
		return feedbacks;
	}
	
	public Feedback findById(String id) {
		return feedbackRep.findByIdAndIsDeletedRecordIsFalse(id);
	}
	
	public Feedback add(Feedback newObj) {
		newObj.setIsDeletedRecord(false);
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
		obj.setIsDeletedRecord(true);
		feedbackRep.save(obj);
	}
}

