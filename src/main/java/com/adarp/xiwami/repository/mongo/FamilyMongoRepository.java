package com.adarp.xiwami.repository.mongo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import com.adarp.xiwami.repository.FamilyRepository;
import com.adarp.xiwami.domain.Family;
import com.adarp.xiwami.domain.Member;

@Repository
public class FamilyMongoRepository implements FamilyRepository {
	@Autowired
	private MongoTemplate mongoTemplate;
	
	public List<Family> GetFamilies() throws Exception {
		
		return (mongoTemplate.findAll(Family.class, "Family"));
	}	
	
	@Override
	public Family GetFamilyById(String id) throws Exception {

		Query myQuery = new Query();
		myQuery.addCriteria(Criteria.where("_id").is(id));
		myQuery.fields().exclude("members.items");
		
		return mongoTemplate.findOne(myQuery, Family.class, "Family");		
		
	}		
	
	@Override
	public void AddFamily(Family newFamily) throws Exception {
		
		mongoTemplate.save(newFamily,"Family");
	}
	
	@Override
	public void UpdateFamily(Family updatedFamily) throws Exception {
		Query myQuery = new Query();
		myQuery.addCriteria(Criteria.where("_id").is(updatedFamily.getId()));

		DBObject updatedFamilyDBObject = (DBObject) mongoTemplate.getConverter().convertToMongoType(updatedFamily);
		updatedFamilyDBObject.removeField("_id");
		updatedFamilyDBObject.removeField("members");
		Update setUpdate = Update.fromDBObject(new BasicDBObject("$set",updatedFamilyDBObject));
		//mongoTemplate.updateFirst(myQuery, setUpdate, Family.class);
		mongoTemplate.updateFirst(myQuery, setUpdate, Family.class, "Family");
	}

	@Override
	public void DeleteFamily(String id) throws Exception {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(id));
		Family result = mongoTemplate.findAndRemove(query, Family.class, "Family");
		
		query = new Query();
		query.addCriteria(Criteria.where("family").is(result.getId()));
		mongoTemplate.remove(query, Member.class, "Member");
	}	
}
