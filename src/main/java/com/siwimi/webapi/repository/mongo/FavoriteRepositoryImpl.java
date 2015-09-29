package com.siwimi.webapi.repository.mongo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.siwimi.webapi.domain.Favorite;
import com.siwimi.webapi.repository.FavoriteRepositoryCustom;

@Repository
public class FavoriteRepositoryImpl implements FavoriteRepositoryCustom{
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@SuppressWarnings("static-access")
	@Override
	public List<Favorite> queryFavorite(String creator, String targetObject, String objectType) {
		
		List<Criteria> criterias = new ArrayList<Criteria>();		
		criterias.add(new Criteria().where("isDeletedRecord").is(false));	
		
		if (creator != null)
			criterias.add(new Criteria().where("creator").is(creator));
		
		if (targetObject != null)
			criterias.add(new Criteria().where("targetObject").is(targetObject));
		
		if (objectType != null)
			criterias.add(new Criteria().where("objectType").is(objectType));
		
		Criteria c = new Criteria().andOperator(criterias.toArray(new Criteria[criterias.size()]));
		return mongoTemplate.find(new Query(c), Favorite.class, "Favorite");
	}
}
