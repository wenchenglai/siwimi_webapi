package com.adarp.xiwami.repository.mongo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.GeospatialIndex;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.adarp.xiwami.domain.Item;
import com.adarp.xiwami.repository.ItemRepositoryCustom;

@Repository
public class ItemRepositoryImpl implements ItemRepositoryCustom {
	
	@Autowired
	private MongoTemplate mongoTemplate;

	@SuppressWarnings("static-access")
	@Override
	public List<Item> queryItem(String sellerId,String status,Double longitude,Double latitude,String qsDistance,String queryText) {
				
		List<Criteria> criterias = new ArrayList<Criteria>();
		
		criterias.add(new Criteria().where("isDeleted").is(false));
	
		if (sellerId != null) {
			criterias.add(new Criteria().where("seller").is(sellerId));
		}
		
		if (status != null) {
			criterias.add(new Criteria().where("status").is(status));
		}
		
		if (queryText != null) {
			criterias.add(new Criteria().orOperator(Criteria.where("name").regex(queryText.trim(), "i"),
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
		return mongoTemplate.find(new Query(c), Item.class, "Item");
	}
		
	@Override
	public Item saveItem(Item newItem) {		
		mongoTemplate.indexOps(Item.class).ensureIndex(new GeospatialIndex("location"));
		mongoTemplate.save(newItem, "Item");
		return newItem;
	}
}
