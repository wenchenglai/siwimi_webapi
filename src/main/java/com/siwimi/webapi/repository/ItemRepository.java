package com.siwimi.webapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.siwimi.webapi.domain.Item;

public interface ItemRepository extends MongoRepository<Item, String>, ItemRepositoryCustom{
	Item findByIdAndIsDestroyedIsFalse(String id);
}
