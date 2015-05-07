package com.siwimi.webapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.siwimi.webapi.domain.Tip;

public interface TipRepository extends MongoRepository<Tip, String>, TipRepositoryCustom{
	Tip findByIdAndIsDeletedRecordIsFalse(String id);
}
