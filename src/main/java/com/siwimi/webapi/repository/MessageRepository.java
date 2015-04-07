package com.siwimi.webapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.siwimi.webapi.domain.Message;

public interface MessageRepository extends MongoRepository<Message, String>, MessageRepositoryCustom{
	Message findByIdAndIsDestroyedIsFalse(String id);
}
