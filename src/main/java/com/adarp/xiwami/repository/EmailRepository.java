package com.adarp.xiwami.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.adarp.xiwami.domain.Email;

public interface EmailRepository extends MongoRepository<Email, String>, EmailRepositoryCustom{

}
