package com.adarp.xiwami.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.adarp.xiwami.domain.Vote;

public interface VoteRepository extends MongoRepository<Vote,String>, VoteRepositoryCustom{
	Vote findByIdAndIsDestroyedIsFalse(String id);
}
