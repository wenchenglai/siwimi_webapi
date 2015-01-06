package com.adarp.xiwami.repository.mongo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.adarp.xiwami.domain.Discussion;
import com.adarp.xiwami.repository.DiscussionRepositoryCustom;

@Repository
public class DiscussionRepositoryImpl implements DiscussionRepositoryCustom{
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Override
	public List<Discussion> query(String creatorId, String entityId, String entityType, String queryText) {				
		Criteria c = new Criteria();
		c = Criteria.where("isDeleted").is(false);
	
		if (creatorId != null) {
			c = c.andOperator(Criteria.where("creator").is(creatorId));
		}
		
		if (entityId != null) {
			c = c.andOperator(Criteria.where("entity").is(entityId));
		}			
		
		if (entityType != null) {
			c = c.andOperator(Criteria.where("entityType").is(entityType));
		}
		
		if (queryText != null) {
			c = c.orOperator(Criteria.where("description").regex(queryText.trim(), "i"));
		}
		
		return mongoTemplate.find(new Query(c), Discussion.class, "Discussion");
	}
		
	@Override
	public Discussion save(Discussion newObj) {
		mongoTemplate.save(newObj, "Discussion");
		return newObj;
	}
}

