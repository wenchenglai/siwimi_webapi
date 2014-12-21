package com.adarp.xiwami.repository.mongo;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.GeospatialIndex;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.adarp.xiwami.domain.Activity;
import com.adarp.xiwami.repository.ActivityRepositoryCustom;

@Repository
public class ActivityRepositoryImpl implements ActivityRepositoryCustom {
	
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public List<Activity> queryActivity(String creatorId,String status,Double longitude,Double latitude,String qsDistance,String queryText) {
				
		Criteria c = new Criteria();
		c = Criteria.where("isDeleted").is(false);
	
		if (creatorId != null) {
			c = c.andOperator(Criteria.where("creator").is(creatorId));
		}
		
		if (queryText != null) {
			c = c.orOperator(Criteria.where("title").regex(queryText, "i"),
					         Criteria.where("description").regex(queryText, "i"));
		}
		
		if ((longitude != null) && (latitude != null) && (qsDistance!= null)) {
			
			double distance = 0.0;
			String [] parts = qsDistance.split(" ");
			if (parts[1].toLowerCase().contains("mile"))
				distance = Double.parseDouble(parts[0])/3959;
			else	
				distance = Double.parseDouble(parts[0])/6371;
					
			c = c.andOperator(Criteria.where("location").nearSphere(new Point(longitude,latitude)).maxDistance(distance));
		}
		
		if (status != null) {		
			Date now = new Date();
			if (status.equalsIgnoreCase("Past")) {
				c = c.andOperator(Criteria.where("toTime").lt(now));				
			} else if (status.equalsIgnoreCase("Ongoing")) {
				c = c.andOperator(Criteria.where("fromTime").lte(now).andOperator(Criteria.where("toTime").gte(now)));
			}
			else if (status.equalsIgnoreCase("Upcoming")){
				c = c.andOperator(Criteria.where("fromTime").gt(now));
			}			
		}

		Query queryActivity = new Query(c);
		
		return mongoTemplate.find(queryActivity, Activity.class, "Activity");
	}
	
	
	@Override
	public Activity saveActivity(Activity newActivity) {
		
		mongoTemplate.indexOps(Activity.class).ensureIndex(new GeospatialIndex("location"));
		mongoTemplate.save(newActivity, "Activity");
		return newActivity;
	}
	
}
