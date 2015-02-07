package com.adarp.xiwami.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.adarp.xiwami.domain.Favorite;

public interface FavoriteRepository extends MongoRepository<Favorite,String>,FavoriteRepositoryCustom{
	Favorite findByIdAndIsDestroyedIsFalse(String id);
}
