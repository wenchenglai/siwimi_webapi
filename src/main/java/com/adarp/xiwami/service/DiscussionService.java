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
		return discussionRep.query(creator, entity, entityType, queryText);	
	}
	
	public Discussion findById(String id) {
		return discussionRep.findOne(id);
	}
	
	public Discussion add(Discussion newObj) {
		newObj.setIsDeleted(false);
		return discussionRep.save(newObj);
	}
	
	public Discussion update(String id, Discussion updatedObj) {
		updatedObj.setId(id);
		return discussionRep.save(updatedObj);
	}
	
	public void delete(String id) {
		Discussion obj = discussionRep.findOne(id);
		obj.setIsDeleted(true);
		discussionRep.save(obj);
	}
}

