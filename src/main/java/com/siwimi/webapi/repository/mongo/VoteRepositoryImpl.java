package com.siwimi.webapi.repository.mongo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.siwimi.webapi.domain.Vote;
import com.siwimi.webapi.repository.VoteRepositoryCustom;

@Repository
public class VoteRepositoryImpl implements VoteRepositoryCustom{

	@Autowired
	private MongoTemplate mongoTemplate;
	
	@SuppressWarnings("static-access")
	@Override
	public Vote queryVote(String creator, String targetObject, String objectType) {
		
		List<Criteria> criterias = new ArrayList<Criteria>();		
		criterias.add(new Criteria().where("isDestroyed").is(false));	
		criterias.add(new Criteria().where("creator").is(creator));
		criterias.add(new Criteria().where("targetObject").is(targetObject));
		criterias.add(new Criteria().where("objectType").is(objectType));
		
		Criteria c = new Criteria().andOperator(criterias.toArray(new Criteria[criterias.size()]));
		return mongoTemplate.findOne(new Query(c), Vote.class, "Vote");
	}	
}
