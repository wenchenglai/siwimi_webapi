package com.adarp.xiwami.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.adarp.xiwami.domain.Discussion;

public interface DiscussionRepository extends MongoRepository<Discussion, String>, DiscussionRepositoryCustom{

}
