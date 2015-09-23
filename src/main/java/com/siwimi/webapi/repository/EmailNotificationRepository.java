package com.siwimi.webapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.siwimi.webapi.domain.EmailNotification;

public interface EmailNotificationRepository extends MongoRepository<EmailNotification,String>, EmailNotificationRepositoryCustom {
	EmailNotification findByCreatorAndIsDeletedRecordIsFalse(String creatorId);
}
