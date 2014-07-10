package com.adarp.xiwami.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.adarp.xiwami.domain.Item;


public interface ItemRepository extends MongoRepository<Item, String>, ItemRepositoryCustom{
	

}
