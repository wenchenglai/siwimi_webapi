package com.siwimi.webapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.siwimi.webapi.domain.Feed;

public interface FeedRepository extends MongoRepository<Feed,String>, FeedRepositoryCustom {
		Feed findByIdAndIsDeletedRecordIsFalse(String id);
}
