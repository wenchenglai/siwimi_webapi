package com.adarp.xiwami.repository.mongo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.adarp.xiwami.repository.FamilyRepositoryCustom;
import com.adarp.xiwami.domain.Family;
import com.adarp.xiwami.domain.Member;
import com.adarp.xiwami.domain.Zipcode;

@Repository
public class FamilyRepositoryImpl implements FamilyRepositoryCustom {
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
/*	public List<Family> GetFamilies() throws Exception {
		
		return (mongoTemplate.findAll(Family.class, "Family"));
	}*/	
	
/*	@Override
	public Family GetFamilyById(String id) throws Exception {

		Query myQuery = new Query();
		myQuery.addCriteria(Criteria.where("_id").is(id));
		myQuery.fields().exclude("members.items");
		
		return mongoTemplate.findOne(myQuery, Family.class, "Family");		
		
	}*/		
	
	@Override
	public void AddFamily(Family newFamily) throws Exception {

		// Retrieve longitude and latitude from the collection of ZipCode
		Query myQuery = new Query();
		myQuery.addCriteria(Criteria.where("ZipCode").is(newFamily.getZipcode()));	
		Zipcode zipcode = mongoTemplate.findOne(myQuery, Zipcode.class, "Zipcode");	
		// set longitude and latitude of the family object 
		double[] location = {zipcode.getLatitude(), zipcode.getLongitude()};
		newFamily.setLocation(location);
		// Save family object into family collection
		mongoTemplate.save(newFamily,"Family");
	}
	
	@Override
	public void UpdateFamily(Family updatedFamily) throws Exception {
		
		// Retrieve longitude and latitude from the collection of ZipCode, if there is any updated zipcode in the family object
		if (updatedFamily.getZipcode() != null) {
			Query zipCodeQuery = new Query();
			zipCodeQuery.addCriteria(Criteria.where("ZipCode").is(updatedFamily.getZipcode()));	
			Zipcode zipcode = mongoTemplate.findOne(zipCodeQuery, Zipcode.class, "Zipcode");	
			// update longitude and latitude of the family object 
			double[] location = {zipcode.getLatitude(), zipcode.getLongitude()};
			updatedFamily.setLocation(location);			
		}
				
		Query myQuery = new Query();
		myQuery.addCriteria(Criteria.where("_id").is(updatedFamily.get_Id()));
		DBObject updatedFamilyDBObject = (DBObject) mongoTemplate.getConverter().convertToMongoType(updatedFamily);
		updatedFamilyDBObject.removeField("_id");
		updatedFamilyDBObject.removeField("members");
		Update setUpdate = Update.fromDBObject(new BasicDBObject("$set",updatedFamilyDBObject));
		mongoTemplate.updateFirst(myQuery, setUpdate, Family.class, "Family");
	}

	@Override
	public void DeleteFamily(String id) throws Exception {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(id));
		Family result = mongoTemplate.findAndRemove(query, Family.class, "Family");
		
		query = new Query();
		query.addCriteria(Criteria.where("family").is(result.get_Id()));
		mongoTemplate.remove(query, Member.class, "Member");
	}	
}
