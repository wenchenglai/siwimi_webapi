package com.siwimi.webapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.siwimi.webapi.domain.Feedback;

public interface FeedbackRepository extends MongoRepository<Feedback, String>, FeedbackRepositoryCustom{
	Feedback findByIdAndIsDeletedRecordIsFalse(String id);
}
