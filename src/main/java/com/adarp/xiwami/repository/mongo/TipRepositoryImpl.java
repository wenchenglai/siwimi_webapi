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

import com.adarp.xiwami.domain.Tip;
import com.adarp.xiwami.repository.TipRepositoryCustom;

@Repository
public class TipRepositoryImpl implements TipRepositoryCustom{
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@SuppressWarnings("static-access")
	@Override
	public List<Tip> queryTip(String status, String type, Double longitude, Double latitude, String qsDistance, String queryText) {				

		List<Criteria> criterias = new ArrayList<Criteria>();
		
		criterias.add(new Criteria().where("isDestroyed").is(false));
	
		// status is transient, depends on expirationDate.  They are {all, popular, active, expired}
		if (status != null) {
			Date now = new Date();
			if (status.equalsIgnoreCase("active"))
				criterias.add(new Criteria().where("expiredDate").gt(now));
			else if (status.equalsIgnoreCase("expired"))
				criterias.add(new Criteria().where("expiredDate").lt(now));
		}
		
		if (type != null) {
			criterias.add(new Criteria().where("type").is(type));
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
		
		Criteria c = new Criteria().andOperator(criterias.toArray(new Criteria[criterias.size()]));
		return mongoTemplate.find(new Query(c), Tip.class, "Tip");
	}
		
	@Override
	public Tip saveTip(Tip newTip) {		
		mongoTemplate.indexOps(Tip.class).ensureIndex(new GeospatialIndex("location"));
		mongoTemplate.save(newTip, "Tip");
		return newTip;
	}
}
