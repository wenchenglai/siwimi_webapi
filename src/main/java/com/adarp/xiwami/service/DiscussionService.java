package com.adarp.xiwami.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adarp.xiwami.domain.Discussion;
import com.adarp.xiwami.repository.DiscussionRepository;

@Service
public class DiscussionService {

	@Autowired
	DiscussionRepository discussionRep;
	
	public List<Discussion> find(String creator, String entity, String entityType, String queryText) {
		List<Discussion> discussionList = discussionRep.query(creator, entity, entityType, queryText);
		
		// increment viewcount by 1, and save it to MongoDB
		for (int i=0; i<discussionList.size(); i++) {
			Discussion discussion = discussionList.get(i);
			discussion.setViewCount(discussion.getViewCount()+1);
			discussionRep.save(discussion);
			discussionList.set(i, discussion);
		}
		
		return discussionList;
	}
	
	public Discussion findById(String id) {
		return discussionRep.findOne(id);
	}
	
	public Discussion add(Discussion newObj) {
		newObj.setIsDestroyed(false);
		newObj.setViewCount(0);
		return discussionRep.save(newObj);
	}
	
	public Discussion update(String id, Discussion updatedObj) {
		updatedObj.setId(id);
		return discussionRep.save(updatedObj);
	}
	
	public void delete(String id) {
		Discussion obj = discussionRep.findOne(id);
		obj.setIsDestroyed(true);
		discussionRep.save(obj);
	}
}

