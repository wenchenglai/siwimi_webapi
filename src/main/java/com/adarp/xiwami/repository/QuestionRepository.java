package com.adarp.xiwami.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.adarp.xiwami.domain.Question;

public interface QuestionRepository extends MongoRepository<Question, String>, QuestionRepositoryCustom{

}
