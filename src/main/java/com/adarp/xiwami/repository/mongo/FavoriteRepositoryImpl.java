package com.adarp.xiwami.repository.mongo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.adarp.xiwami.domain.Favorite;
import com.adarp.xiwami.repository.FavoriteRepositoryCustom;

@Repository
public class FavoriteRepositoryImpl implements FavoriteRepositoryCustom{
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@SuppressWarnings("static-access")
	@Override
	public Favorite queryFavorite(String creator, String targetObject, String objectType) {
		List<Criteria> criterias = new ArrayList<Criteria>();		
		criterias.add(new Criteria().where("isDestroyed").is(false));	
		criterias.add(new Criteria().where("creator").is(creator));
		criterias.add(new Criteria().where("targetObject").is(targetObject));
		criterias.add(new Criteria().where("objectType").regex(objectType, "i"));
		
		Criteria c = new Criteria().andOperator(criterias.toArray(new Criteria[criterias.size()]));
		return mongoTemplate.findOne(new Query(c),Favorite.class,"Favorite");
	}
}