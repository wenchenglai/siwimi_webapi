package com.adarp.xiwami.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.adarp.xiwami.domain.Activity;


public interface ActivityRepository extends MongoRepository<Activity, String>, ActivityRepositoryCustom{
	List<Activity> findByCreatorAndTypeAndIsDeletedIsFalse(String creatorId, String type);

	@Query("{'$and':[ {'isDeleted' : false}, {'creator' : {'$in' : ?0}}, {'$or' : [{'title':{$regex : ?1, $options :'i'}}, {'description' : {'$in' :[{$regex : ?1, $options :'i'}]}}] } ] }")
	List<Activity> findByCreatorInAndTypeDescriptionLikeIgnoreCaseAndIsDeletedIsFalse(List<String> geoMemberId, String queryText);
}
