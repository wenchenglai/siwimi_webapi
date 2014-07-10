package com.adarp.xiwami.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.adarp.xiwami.domain.Member;


public interface MemberRepository extends MongoRepository<Member, String>, MemberRepositoryCustom{
	

}
