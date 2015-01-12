package com.adarp.xiwami.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.adarp.xiwami.domain.Feedback;

public interface FeedbackRepository extends MongoRepository<Feedback, String>, FeedbackRepositoryCustom{

}
