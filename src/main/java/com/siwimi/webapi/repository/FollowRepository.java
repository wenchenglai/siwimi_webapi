package com.siwimi.webapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.siwimi.webapi.domain.Follow;

public interface FollowRepository extends MongoRepository<Follow,String>, FollowRepositoryCustom{
	Follow findByIdAndIsDeletedRecordIsFalse(String id);
}
