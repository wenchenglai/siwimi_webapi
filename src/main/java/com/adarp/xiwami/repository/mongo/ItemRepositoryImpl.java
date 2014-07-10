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

import com.adarp.xiwami.repository.ItemRepositoryCustom;
import com.adarp.xiwami.domain.Item;


@Repository
public class ItemRepositoryImpl implements ItemRepositoryCustom {
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	public List<Item> GetItems() throws Exception {
		
		return (mongoTemplate.findAll(Item.class, "Item"));
	}	
	
	@Override
	public Item GetItemById(String id) throws Exception {

		Query myQuery = new Query();
		myQuery.addCriteria(Criteria.where("_id").is(id));	
		return mongoTemplate.findOne(myQuery, Item.class, "Item");				
	}		
	
	@Override
	public void AddItem(Item newItem) throws Exception {
		
		mongoTemplate.save(newItem,"Item");
	}
	
	@Override
	public void UpdateItem(String id, Item updatedItem) throws Exception {
		Query myQuery = new Query();
		myQuery.addCriteria(Criteria.where("_id").is(id));		
		DBObject updatedItemDBObject = (DBObject) mongoTemplate.getConverter().convertToMongoType(updatedItem);
		updatedItemDBObject.removeField("_id");
		Update setUpdate = Update.fromDBObject(new BasicDBObject("$set",updatedItemDBObject));
		mongoTemplate.updateFirst(myQuery, setUpdate, Item.class, "Item");
	}

	@Override
	public void DeleteItem(String id) throws Exception {
//		Query query = new Query();
//		query.addCriteria(Criteria.where("_id").is(id));
//		mongoTemplate.remove(query, Item.class, "Item");

		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(id));
		mongoTemplate.updateFirst(query, Update.update("isDeleted", "Y"), Item.class, "Item");		
	}	
}
