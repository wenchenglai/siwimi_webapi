package com.adarp.xiwami.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.adarp.xiwami.domain.Family;

public interface FamilyRepository extends MongoRepository<Family, String>, FamilyRepositoryCustom{	
	List<Family> findByIdIn(Set<String> foundFamilyId);
	Family findByIdAndIsDestroyedIsFalse(String id);
}
