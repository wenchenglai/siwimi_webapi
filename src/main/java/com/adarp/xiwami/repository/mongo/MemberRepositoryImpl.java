package com.adarp.xiwami.repository.mongo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.adarp.xiwami.repository.MemberRepositoryCustom;
import com.adarp.xiwami.domain.Member;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

@Repository
public class MemberRepositoryImpl implements MemberRepositoryCustom {
	@Autowired
	private MongoTemplate mongoTemplate;	
	
	@Override
	public void AddMember(Member newMember) throws Exception {
		mongoTemplate.save(newMember, "Member");
	}
	
	@Override
	public void UpdateMember(Member member) {
		
		Query QueryInUser = new Query();
		QueryInUser.addCriteria(Criteria.where("_id").is(member.get_Id()));		
		DBObject updatedUserDBObject = (DBObject) mongoTemplate.getConverter().convertToMongoType(member);
		updatedUserDBObject.removeField("_id");		
		updatedUserDBObject.removeField("facebookId");
		Update setUserUpdate = Update.fromDBObject(new BasicDBObject("$set",updatedUserDBObject));
		
		mongoTemplate.findAndModify(QueryInUser, setUserUpdate, Member.class, "Member");
	}
	
	@Override
	public void DeleteMember(String id) throws Exception {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(id));
		mongoTemplate.findAndRemove(query, Member.class, "Member");		
	}
	
	@Override
	public List<Member> FindMembers(String familyId) throws Exception {

		Query myQuery = new Query();
		myQuery.addCriteria(Criteria.where("family").in(familyId));
		List<Member> members = mongoTemplate.find(myQuery, Member.class, "Member");
		
		return members;
	}		
}
