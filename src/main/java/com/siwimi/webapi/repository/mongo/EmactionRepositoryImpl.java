package com.siwimi.webapi.repository.mongo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.siwimi.webapi.domain.Emaction;
import com.siwimi.webapi.repository.EmactionRepositoryCustom;


public class EmactionRepositoryImpl implements EmactionRepositoryCustom{
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@SuppressWarnings("static-access")
	@Override
	public List<Emaction> queryEmaction(String event) {
		
		if ((event == null) ) {
			return null;
		} else {
			List<Criteria> criterias = new ArrayList<Criteria>();		
			criterias.add(new Criteria().where("event").is(event));	
			
			Criteria c = new Criteria().andOperator(criterias.toArray(new Criteria[criterias.size()]));
			Query q = new Query(c).with(new Sort(Sort.DEFAULT_DIRECTION.ASC,"type"));
			return mongoTemplate.find(q,Emaction.class,"Emaction");
		}
		
	}
}
