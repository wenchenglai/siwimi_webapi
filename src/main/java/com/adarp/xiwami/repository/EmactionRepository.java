package com.adarp.xiwami.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.adarp.xiwami.domain.Emaction;

public interface EmactionRepository extends MongoRepository<Emaction, String>,EmactionRepositoryCustom{
	Emaction findByMemberAndEvent(String member, String event);
}
