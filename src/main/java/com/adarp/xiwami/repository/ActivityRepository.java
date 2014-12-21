 package com.adarp.xiwami.repository;

import org.springframework.data.mongodb.repository.MongoRepository;


import com.adarp.xiwami.domain.Activity;


public interface ActivityRepository extends MongoRepository<Activity, String>, ActivityRepositoryCustom{

	/*****
	{
  		$and: 
   		[ 
    		{'isDeleted' : false},
    		{$or : [ { $where: '?0 == null' } , {'creator' : ?0}]}, 
    		{$or : [ { $where: '?1 == null' } , {'url' : ?1}]}, 
    		{$or : [ { $where: '?2 == -1.0' } , { $where: '?3 == -1.0' } , { $where: '?4 == -1.0' } , { 'location' : {'$nearSphere' : [?2, ?3], '$maxDistance' : ?4}}]},
    		{$or : [ { $where: '?5 == null' } , {'$or' : [{'title': ?5}, {'description' : ?5}] }]}
   		]
	}
	
	

	 ******/

	/***
	
	{
  		$and: 
   		[ 
    		{'isDeleted' : false},
    		{$or : [ { $where: '?0 == null' } , {'creator' : ?0}]}, 
    		{$or : [ { $where: '?1 == null' } , {'url' : ?1}]}, 
    		{$or : [ { $where: '?5 == null' } , {'$or' : [{'title': ?5}, {'description' : ?5}] }]}
   		]
	}

	
	{
		$and:
		[
			{ 'isDeleted' : false },
			{ $or :  [  { $where: '?0 == null' }, {'creator' : ?0}  ]   },
			{ $or :  [  { $where: '?1 == null' }, {'title' : {$in : [?1] }}  ]	}
		]
	}
	
	{
		$and:
		[
			{ 'isDeleted' : false },
			{ $or :  [  { $where: '?0 == null' }, {'creator' : ?0}  ]   },
			{ $or :  [  { 'title' : {$exists : true},$where: '?1 == null' }, {'title': {$regex : ?1, $options :'i'}}  ]	}
		]
	}
	
	***/
	
	//@Query("	{$and: [ {'isDeleted' : false},{$or : [ { $where: '?0 == null' } , {'creator' : ?0}]}, {$or : [ { $where: '?1 == null' } , {'url' : ?1}]}, {$or : [ { $where: '?5 == null' } , {'$or' : [{'title': ?5}, {'description' : ?5}] }]}]}")
	//List<Activity> findByCreatorOrLocationOrQuerytext(String creatorId,String status,double longitude,double latitude,double qsDistance,String queryText);
	

	
	//@Query("{$and:[{ 'isDeleted' : false },{ $or :  [  { $where: '?0 == null' }, {'creator' : ?0}  ]   },{ $or :  [  { $where: '?1 == null' }, {'title': {$regex : ?1, $options :'i'}}  ]	}]}")
	//List<Activity> kt(String creatorId, String quertText);

}
