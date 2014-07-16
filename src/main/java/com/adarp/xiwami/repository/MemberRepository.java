package com.adarp.xiwami.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.adarp.xiwami.domain.Member;


public interface MemberRepository extends MongoRepository<Member, String>, MemberRepositoryCustom{	
	List<Member> findByFamilyInAndIsDeletedIsFalse(String familyId);
	List<Member> findByFamilyInAndBirthdayBetweenAndIsDeletedIsFalse(List<String> geoFamilyId,Date fromDate, Date toDate);
	List<Member> findByFamilyInAndLanguagesInAndBirthdayBetweenAndIsDeletedIsFalse(List<String> geoFamilyId,List<String> languageList,Date fromDate, Date toDate);
	List<Member> findByFacebookId(String id);
	List<Member> findByGoogleplusId(String id);
}
