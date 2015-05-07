package com.siwimi.webapi.repository.mongo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.siwimi.webapi.domain.Group;
import com.siwimi.webapi.repository.GroupRepositoryCustom;

@Repository
public class GroupRepositoryImpl implements GroupRepositoryCustom{
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@SuppressWarnings("static-access")
	@Override
	public List<Group> queryGroup(String creatorId,String queryText) {
		List<Criteria> criterias = new ArrayList<Criteria>();
		
		criterias.add(new Criteria().where("isDeletedRecord").is(false));
	
		if (creatorId != null) {
			criterias.add(new Criteria().where("creator").is(creatorId));
		}
		
		if (queryText != null) {
			criterias.add(new Criteria().orOperator(Criteria.where("title").regex(queryText.trim(), "i"),
					                                Criteria.where("description").regex(queryText.trim(), "i")));
		}		
		
		Criteria c = new Criteria().andOperator(criterias.toArray(new Criteria[criterias.size()]));
		return mongoTemplate.find(new Query(c), Group.class, "Group");
	}
	
}
