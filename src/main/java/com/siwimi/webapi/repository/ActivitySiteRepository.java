 package com.siwimi.webapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.siwimi.webapi.domain.ActivitySite;

public interface ActivitySiteRepository extends MongoRepository<ActivitySite, String>, ActivitySiteRepositoryCustom{

}
