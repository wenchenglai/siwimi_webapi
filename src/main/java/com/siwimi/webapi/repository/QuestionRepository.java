package com.siwimi.webapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.siwimi.webapi.domain.Question;

public interface QuestionRepository extends MongoRepository<Question, String>, QuestionRepositoryCustom{
	Question findByIdAndIsDestroyedIsFalse(String id);
}
