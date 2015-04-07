package com.siwimi.webapi.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.siwimi.webapi.domain.Family;

public interface FamilyRepository extends MongoRepository<Family, String>, FamilyRepositoryCustom{	
	List<Family> findByIdIn(Set<String> foundFamilyId);
	Family findByIdAndIsDestroyedIsFalse(String id);
}
