package com.siwimi.webapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.siwimi.webapi.domain.Survey;

public interface SurveyRepository extends MongoRepository<Survey, String>, SurveyRepositoryCustom{

}
