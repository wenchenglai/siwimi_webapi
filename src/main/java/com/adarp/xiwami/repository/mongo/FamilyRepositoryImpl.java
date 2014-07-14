package com.adarp.xiwami.repository.mongo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.GeospatialIndex;
import org.springframework.stereotype.Repository;

import com.adarp.xiwami.repository.FamilyRepositoryCustom;
import com.adarp.xiwami.domain.Family;

@Repository
public class FamilyRepositoryImpl implements FamilyRepositoryCustom {
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Override
	public void AddFamily(Family newFamily){
		mongoTemplate.indexOps(Family.class).ensureIndex(new GeospatialIndex("location"));
		mongoTemplate.save(newFamily,"Family");
	}
	
}
