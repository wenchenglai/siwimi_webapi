package com.adarp.xiwami.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.adarp.xiwami.domain.Activity;


public interface ActivityRepository extends MongoRepository<Activity, String>, ActivityRepositoryCustom{
	

}
