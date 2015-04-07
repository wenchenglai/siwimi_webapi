package com.siwimi.webapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.siwimi.webapi.domain.Emaction;

public interface EmactionRepository extends MongoRepository<Emaction, String>,EmactionRepositoryCustom{
	Emaction findByMemberAndEvent(String member, String event);
}
