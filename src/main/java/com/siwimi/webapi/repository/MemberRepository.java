package com.siwimi.webapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.siwimi.webapi.domain.Member;


public interface MemberRepository extends MongoRepository<Member, String>, MemberRepositoryCustom{	
	
}
