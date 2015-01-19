package com.adarp.xiwami.repository.mongo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.adarp.xiwami.domain.Member;
import com.adarp.xiwami.repository.MemberRepositoryCustom;

@Repository
public class MemberRepositoryImpl implements MemberRepositoryCustom {
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@SuppressWarnings("static-access")
	@Override
	public List<Member> query(String queryText) {
		
		List<Criteria> criterias = new ArrayList<Criteria>();

		criterias.add(new Criteria().where("isDestroyed").is(false));
		
		if (queryText != null) {
			criterias.add(new Criteria().orOperator(Criteria.where("firstName").regex(queryText.trim(), "i"),
                    								Criteria.where("lastName").regex(queryText.trim(), "i")));
		}
		
		Criteria c = new Criteria().andOperator(criterias.toArray(new Criteria[criterias.size()]));
		//Sort by "firstName" --> then sort by "lastName"
		Query q = new Query(c).with(new Sort(Sort.DEFAULT_DIRECTION.ASC,"firstName").and(new Sort(Sort.DEFAULT_DIRECTION.ASC,"lastName")));
		
		return mongoTemplate.find(q, Member.class, "Member");
				
	}

}
