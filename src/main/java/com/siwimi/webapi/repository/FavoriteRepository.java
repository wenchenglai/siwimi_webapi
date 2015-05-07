package com.siwimi.webapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.siwimi.webapi.domain.Favorite;

public interface FavoriteRepository extends MongoRepository<Favorite,String>,FavoriteRepositoryCustom{
	Favorite findByIdAndIsDeletedRecordIsFalse(String id);
}
