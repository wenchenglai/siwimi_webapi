package com.adarp.xiwami.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.adarp.xiwami.domain.Item;

public interface ItemRepository extends MongoRepository<Item, String>, ItemRepositoryCustom{
	List<Item> findBySellerAndStatusAndIsDeletedIsFalse(String sellerId, String status);
	
	@Query("{'$and':[ {'isDeleted' : false}, {'seller' : {'$in' : ?0}}, {'$or' : [{'name':{$regex : ?1, $options :'i'}}, {'description' : {'$in' :[{$regex : ?1, $options :'i'}]}}] } ] }")
	List<Item> findBySellerInAndNameDescriptionLikeIgnoreCaseAndIsDeletedIsFalse(List<String> geoMemberId, String queryText);
	
}
