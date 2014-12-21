package com.adarp.xiwami.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.adarp.xiwami.domain.Tip;

public interface TipRepository extends MongoRepository<Tip, String>, TipRepositoryCustom{

	List<Tip> findByTypeAndIsDeletedIsFalse(String type);
	//List<Tip> findByUserAndIsDeletedIsFalse(String user);
	//List<Tip> findByUserInAndIsDeletedIsFalse(List<String> geoMemberId);
}
