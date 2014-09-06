package com.adarp.xiwami.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.adarp.xiwami.domain.Survey;

public interface SurveyRepository extends MongoRepository<Survey, String>, SurveyRepositoryCustom{

}
