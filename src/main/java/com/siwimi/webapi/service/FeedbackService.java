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
		
		// Retrieve the sub-replies from feedbacks. Please note that we don't add view counts to the sub-replies
		if ((feedbacks != null) && (!feedbacks.isEmpty()) && (parentType != null)) {
			// Retrieve id of feedbacks
			List<String> feedbacksId = new ArrayList<String>();
			for (Feedback feedback : feedbacks) {
				feedbacksId.add(feedback.getId());
			}
			// populate feedbacks with sub-replies
			List<Feedback> subReplies = new ArrayList<Feedback>();
			for (String subReplyId : feedbacksId) {
				subReplies.addAll(feedbackRep.query(null, subReplyId, null, null)); 
			}
			feedbacks.addAll(subReplies);
		}
		
		return feedbacks;
	}
	
	public Feedback findById(String id) {
		return feedbackRep.findByIdAndIsDeletedRecordIsFalse(id);
	}
	
	public Feedback add(Feedback newObj) {
		newObj.setIsDeletedRecord(false);
		newObj.setLikeCount(0);
		Feedback saveObj = feedbackRep.save(newObj);
		
		// if newObj is a comment, we need to update its parent.
		if (saveObj.getParentType() == null) {
			if (saveObj.getParent() != null) {
				Feedback feedback = feedbackRep.findOne(saveObj.getParent());
				Set <String> commentSet = new LinkedHashSet<String>(feedback.getComments());
				commentSet.add(saveObj.getId());
				feedback.setComments(new ArrayList<String>(commentSet));
				feedbackRep.save(feedback);
			}
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
	
	public Feedback delete(String id) {
		Feedback obj = feedbackRep.findOne(id);
		if (obj == null)
			return null;
		else if (!obj.getIsDeletedRecord()) {				
			obj.setIsDeletedRecord(true);
			return feedbackRep.save(obj);
		} else
			return null;
	}
}

