package com.adarp.xiwami.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.adarp.xiwami.domain.Family;


public interface FamilyRepository extends MongoRepository<Family, String>, FamilyRepositoryCustom{
	

}
