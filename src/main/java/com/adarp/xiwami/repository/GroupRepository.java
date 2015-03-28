package com.adarp.xiwami.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.adarp.xiwami.domain.Group;

public interface GroupRepository extends MongoRepository<Group, String>,GroupRepositoryCustom{
	Group findByIdAndIsDestroyedIsFalse(String id);
}
