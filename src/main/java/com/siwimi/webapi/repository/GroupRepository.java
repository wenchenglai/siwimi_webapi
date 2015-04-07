package com.siwimi.webapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.siwimi.webapi.domain.Group;

public interface GroupRepository extends MongoRepository<Group, String>,GroupRepositoryCustom{
	Group findByIdAndIsDestroyedIsFalse(String id);
}
