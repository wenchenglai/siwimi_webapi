package com.adarp.xiwami.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.adarp.xiwami.domain.Follow;

public interface FollowRepository extends MongoRepository<Follow,String>, FollowRepositoryCustom{
	Follow findByIdAndIsDestroyedIsFalse(String id);
}
