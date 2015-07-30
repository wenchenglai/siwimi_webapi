package com.siwimi.webapi.repository.mongo;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.siwimi.webapi.domain.Feed;
import com.siwimi.webapi.repository.FeedRepositoryCustom;

@Repository
public class FeedRepositoryImpl implements FeedRepositoryCustom{

	@Autowired
	private MongoTemplate mongoTemplate;

	@SuppressWarnings("static-access")
	@Override
	public List<Feed> query(String requesterId) {

		List<Criteria> criterias = new ArrayList<Criteria>();
		
		criterias.add(new Criteria().where("isDeletedRecord").is(false));
		
		Criteria c = new Criteria().andOperator(criterias.toArray(new Criteria[criterias.size()]));

		if (requesterId != null) {
			criterias.add(new Criteria().where("creator").is(requesterId));
		}
		
		// Queried result without pagination
		List<Feed> allResults = mongoTemplate.find(new Query(c), Feed.class, "Feed");
		
		if ((allResults == null) || (allResults.isEmpty()))
			return new ArrayList<Feed>();
		else {
			Query q = new Query(c).limit(5).skip(0);	
			q = q.with(new Sort(Sort.DEFAULT_DIRECTION.DESC,"createdDate"));	
			// Queried result with pagination
			return mongoTemplate.find(q, Feed.class, "Feed");
		}			
	}
}
