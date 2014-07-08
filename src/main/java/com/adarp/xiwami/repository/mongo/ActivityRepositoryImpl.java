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
import com.adarp.xiwami.repository.ActivityRepositoryCustom;
import com.adarp.xiwami.domain.Activity;


@Repository
public class ActivityRepositoryImpl implements ActivityRepositoryCustom {
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	public List<Activity> GetActivities() throws Exception {
		
		return (mongoTemplate.findAll(Activity.class, "Activity"));
	}	
	
	@Override
	public Activity GetActivityById(String id) throws Exception {

		Query myQuery = new Query();
		myQuery.addCriteria(Criteria.where("_id").is(id));	
		return mongoTemplate.findOne(myQuery, Activity.class, "Activity");				
	}		
	
	@Override
	public void AddActivity(Activity newActivity) throws Exception {
		
		mongoTemplate.save(newActivity,"Activity");
	}
	
	@Override
	public void UpdateActivity(String id, Activity updatedActivity) throws Exception {
		Query myQuery = new Query();
		myQuery.addCriteria(Criteria.where("_id").is(id));	

		DBObject updatedActivityDBObject = (DBObject) mongoTemplate.getConverter().convertToMongoType(updatedActivity);
		updatedActivityDBObject.removeField("_id");		
		Update setUpdate = Update.fromDBObject(new BasicDBObject("$set",updatedActivityDBObject));
		mongoTemplate.updateFirst(myQuery, setUpdate, Activity.class, "Activity");

	}

	@Override
	public void DeleteActivity(String id) throws Exception {
//		Query query = new Query();
//		query.addCriteria(Criteria.where("_id").is(id));
//		mongoTemplate.remove(query, Activity.class, "Activity");

		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(id));
		mongoTemplate.updateFirst(query, Update.update("isDeleted", "Y"), Activity.class, "Activity");		
	}	
}
