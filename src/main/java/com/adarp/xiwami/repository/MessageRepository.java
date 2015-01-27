package com.adarp.xiwami.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.adarp.xiwami.domain.Message;

public interface MessageRepository extends MongoRepository<Message, String>, MessageRepositoryCustom{
	Message findByIdAndIsDestroyedIsFalse(String id);
}
