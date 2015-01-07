package com.adarp.xiwami.repository.mongo;

import java.util.ArrayList;
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
	
	@SuppressWarnings("static-access")
	@Override
	public List<Discussion> query(String creatorId, String entityId, String entityType, String queryText) {				
		
		List<Criteria> criterias = new ArrayList<Criteria>();
		
		criterias.add(new Criteria().where("isDestroyed").is(false));
		
		if (creatorId != null) {
			criterias.add(new Criteria().where("creator").is(creatorId));
		}
		
		if (entityId != null) {
			criterias.add(new Criteria().where("entity").is(entityId));
		}			
		
		if (entityType != null) {
			criterias.add(new Criteria().where("entityType").is(entityType));
		}
		
		if (queryText != null) {
			criterias.add(new Criteria().where("description").regex(queryText.trim(), "i"));
		}	
		
		Criteria c = new Criteria().andOperator(criterias.toArray(new Criteria[criterias.size()]));
		return mongoTemplate.find(new Query(c), Discussion.class, "Discussion");
	}		
}

