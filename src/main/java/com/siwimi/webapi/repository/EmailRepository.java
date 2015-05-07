package com.siwimi.webapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.siwimi.webapi.domain.Email;

public interface EmailRepository extends MongoRepository<Email, String>, EmailRepositoryCustom{
	Email findByIdAndIsDeletedRecordIsFalse(String id);
}
