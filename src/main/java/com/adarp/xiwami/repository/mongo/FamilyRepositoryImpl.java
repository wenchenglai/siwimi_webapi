package com.adarp.xiwami.repository.mongo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.GeospatialIndex;
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
	
	@Override
	public void AddFamily(Family newFamily) throws Exception {

		// Retrieve longitude and latitude from the collection of ZipCode
		Query myQuery = new Query();
		myQuery.addCriteria(Criteria.where("zipCode").is(Integer.parseInt(newFamily.getZipCode())));
		Zipcode zipcode = mongoTemplate.findOne(myQuery, Zipcode.class, "ZipCode");
		// set longitude and latitude of the family object 
		double[] location = {zipcode.getLongitude(), zipcode.getLatitude()};
		newFamily.setLocation(location);
		// Save family object into family collection
		mongoTemplate.indexOps(Family.class).ensureIndex(new GeospatialIndex("location") );
		mongoTemplate.save(newFamily,"Family");
	}
	
	@Override
	public void UpdateFamily(Family updatedFamily) throws Exception {
		
		// Retrieve longitude and latitude from the collection of ZipCode, if there is any updated zipcode in the family object
		if (updatedFamily.getZipCode() != null) {
			Query zipCodeQuery = new Query();
			zipCodeQuery.addCriteria(Criteria.where("zipCode").is(Integer.parseInt(updatedFamily.getZipCode())));
			Zipcode zipcode = mongoTemplate.findOne(zipCodeQuery, Zipcode.class, "ZipCode");	
			// update longitude and latitude of the family object 
			double[] location = {zipcode.getLongitude(), zipcode.getLatitude()};
			updatedFamily.setLocation(location);	
			mongoTemplate.indexOps(Family.class).ensureIndex(new GeospatialIndex("location") );
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