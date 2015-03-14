package com.adarp.xiwami.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.adarp.xiwami.domain.ZipCode;

public interface ZipCodeRepository extends MongoRepository<ZipCode, String>, ZipCodeRepositoryCustom{

}
