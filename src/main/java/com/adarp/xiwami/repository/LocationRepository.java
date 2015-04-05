package com.adarp.xiwami.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.adarp.xiwami.domain.Location;

public interface LocationRepository extends MongoRepository<Location, String>, LocationRepositoryCustom{

}
