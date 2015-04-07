package com.siwimi.webapi.repository.mongo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.siwimi.webapi.domain.Message;
import com.siwimi.webapi.repository.MessageRepositoryCustom;

@Repository
public class MessageRepositoryImpl implements MessageRepositoryCustom{
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@SuppressWarnings("static-access")
	@Override
	public List<Message> query(String fromId, String toId, String fromStatus, String toStatus, String queryText) {				
		
		List<Criteria> criterias = new ArrayList<Criteria>();
		
		criterias.add(new Criteria().where("isDestroyed").is(false));
		
		if (fromId != null) {
			criterias.add(new Criteria().where("from").is(fromId));
		}
		
		if (toId != null) {
			criterias.add(new Criteria().where("to").is(toId));
		}			
		
		if (fromStatus != null) {
			criterias.add(new Criteria().where("fromStatus").is(fromStatus));
		}
		
		if (toStatus != null) {
			String both = "both";
			if (toStatus.equals(both)) {
				criterias.add(new Criteria().orOperator(Criteria.where("toStatus").is("read"),
                        Criteria.where("toStatus").is("unread")));
			}
			else
				criterias.add(new Criteria().where("toStatus").is(toStatus));
		}
		
		if (queryText != null) {
			criterias.add(new Criteria().orOperator(Criteria.where("subject").regex(queryText.trim(), "i"),
			                                        Criteria.where("body").regex(queryText.trim(), "i")));
		}		
		
		Criteria c = new Criteria().andOperator(criterias.toArray(new Criteria[criterias.size()]));
		return mongoTemplate.find(new Query(c), Message.class, "Message");
	}
	
//	@Override
//	public Message save(Message newObj) {
//		mongoTemplate.save(newObj, "Message");
//		return newObj;
//	}	
}


