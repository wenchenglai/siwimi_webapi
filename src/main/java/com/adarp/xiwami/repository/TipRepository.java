package com.adarp.xiwami.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.adarp.xiwami.domain.Tip;
import com.adarp.xiwami.domain.Question;

public interface TipRepository extends MongoRepository<Question, String>, TipRepositoryCustom{

	List<Tip> findByUserAndIsDeletedIsFalse(String user);
	List<Tip> findByUserInAndIsDeletedIsFalse(List<String> geoMemberId);
}
