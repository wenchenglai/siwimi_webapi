package com.adarp.xiwami.repository.mongo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.adarp.xiwami.domain.Follow;
import com.adarp.xiwami.repository.FollowRepositoryCustom;

@Repository
public class FollowRepositoryImpl implements FollowRepositoryCustom {

	@Autowired
	private MongoTemplate mongoTemplate;
	
	@SuppressWarnings("static-access")
	@Override
	public List<Follow> queryFollow (String follower, String followee) {
		List<Criteria> criterias = new ArrayList<Criteria>();
		
		criterias.add(new Criteria().where("isDestroyed").is(false));
	
		if (follower != null) {
			criterias.add(new Criteria().where("follower").regex(follower.trim(), "i"));
		}
		
		if (followee != null) {
			criterias.add(new Criteria().where("followee").regex(followee.trim(), "i"));
		}
		
		Criteria c = new Criteria().andOperator(criterias.toArray(new Criteria[criterias.size()]));
		return mongoTemplate.find(new Query(c), Follow.class, "Follow");
	}
}
