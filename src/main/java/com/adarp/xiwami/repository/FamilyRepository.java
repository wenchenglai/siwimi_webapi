package com.adarp.xiwami.repository;

import java.util.List;

import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.adarp.xiwami.domain.Family;

public interface FamilyRepository extends MongoRepository<Family, String>, FamilyRepositoryCustom{	
	List<Family> findByLocationNear(Point p, Distance d);
}
