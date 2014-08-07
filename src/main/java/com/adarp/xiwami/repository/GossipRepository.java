package com.adarp.xiwami.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.adarp.xiwami.domain.Gossip;
import com.adarp.xiwami.domain.Question;

public interface GossipRepository extends MongoRepository<Question, String>, GossipRepositoryCustom{

	List<Gossip> findByUserAndIsDeletedIsFalse(String user);
	List<Gossip> findByUserInAndIsDeletedIsFalse(List<String> geoMemberId);
}
