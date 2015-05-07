package com.siwimi.webapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.siwimi.webapi.domain.Vote;

public interface VoteRepository extends MongoRepository<Vote,String>, VoteRepositoryCustom{
	Vote findByIdAndIsDeletedRecordIsFalse(String id);

}
