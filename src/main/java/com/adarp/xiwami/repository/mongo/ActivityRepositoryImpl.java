package com.adarp.xiwami.repository.mongo;

import java.util.ArrayList;
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

	@SuppressWarnings("static-access")
	@Override
	public List<Activity> queryActivity(String creatorId,String status,String type,Double longitude,Double latitude,String qsDistance,String queryText) {
			
		List<Criteria> criterias = new ArrayList<Criteria>();
		
		criterias.add(new Criteria().where("isDestroyed").is(false));
	
		if (creatorId != null) {
			criterias.add(new Criteria().where("creator").is(creatorId));
		}
		
		if (queryText != null) {
			criterias.add(new Criteria().orOperator(Criteria.where("title").regex(queryText.trim(), "i"),
			                                        Criteria.where("description").regex(queryText.trim(), "i")));
		}
		
		if ((longitude != null) && (latitude != null) && (qsDistance!= null)) {			
			double distance = 0.0;
			String [] parts = qsDistance.split(" ");
			if (parts[1].toLowerCase().contains("mile"))
				distance = Double.parseDouble(parts[0])/3959;
			else	
				distance = Double.parseDouble(parts[0])/6371;
					
			criterias.add(new Criteria().where("location").nearSphere(new Point(longitude,latitude)).maxDistance(distance));
		}
		
		if (status != null) {		
			Date now = new Date();
			if (status.equalsIgnoreCase("Past")) {
				criterias.add(new Criteria().where("toTime").lt(now));
			} else if (status.equalsIgnoreCase("Ongoing")) {
				criterias.add(new Criteria().andOperator(Criteria.where("fromTime").lte(now),Criteria.where("toTime").gte(now)));
			}
			else if (status.equalsIgnoreCase("Upcoming")){
				criterias.add(new Criteria().where("fromTime").gt(now));
			}			
		}
	
		if (type != null) {
			criterias.add(new Criteria().where("type").is(type));
		}
		
		Criteria c = new Criteria().andOperator(criterias.toArray(new Criteria[criterias.size()]));
		return mongoTemplate.find(new Query(c), Activity.class, "Activity");
	}
		
	@Override
	public Activity saveActivity(Activity newActivity) {		
		mongoTemplate.indexOps(Activity.class).ensureIndex(new GeospatialIndex("location"));
		mongoTemplate.save(newActivity, "Activity");
		return newActivity;
	}	
}
