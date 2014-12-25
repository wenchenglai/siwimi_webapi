package com.adarp.xiwami.repository.mongo;

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
	
	@Override
	public List<Tip> queryTip(String type,Double longitude,Double latitude,String qsDistance,String queryText) {				
		Criteria c = new Criteria();
		c = Criteria.where("isDeleted").is(false);
	
		if (type != null) {
			c = c.andOperator(Criteria.where("type").is(type));
		}
		
		if (queryText != null) {
			c = c.orOperator(Criteria.where("title").regex(queryText.trim(), "i"),
					         Criteria.where("description").regex(queryText.trim(), "i"));
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
		
		return mongoTemplate.find(new Query(c), Tip.class, "Tip");
	}
		
	@Override
	public Tip saveTip(Tip newTip) {		
		mongoTemplate.indexOps(Tip.class).ensureIndex(new GeospatialIndex("location"));
		mongoTemplate.save(newTip, "Tip");
		return newTip;
	}
}
