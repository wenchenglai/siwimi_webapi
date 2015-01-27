package com.adarp.xiwami.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.adarp.xiwami.domain.Tip;

public interface TipRepository extends MongoRepository<Tip, String>, TipRepositoryCustom{
	Tip findByIdAndIsDestroyedIsFalse(String id);
}
