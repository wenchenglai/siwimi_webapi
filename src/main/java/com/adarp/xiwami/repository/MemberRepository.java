package com.adarp.xiwami.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.adarp.xiwami.domain.Member;


public interface MemberRepository extends MongoRepository<Member, String>, MemberRepositoryCustom{	
	List<Member> findByFamilyInAndIsDestroyedIsFalse(String familyId);
	List<Member> findByFamilyInAndIsDestroyedIsFalse(List<String> familyId);
	List<Member> findByFamilyInAndBirthdayBetweenAndIsDestroyedIsFalse(List<String> geoFamilyId,Date fromDate, Date toDate);
	List<Member> findByFamilyInAndLanguagesInAndBirthdayBetweenAndIsDestroyedIsFalse(List<String> geoFamilyId,List<String> languageList,Date fromDate, Date toDate);

//	Member findByFacebookId(String id);
//	Member findByGoogleplusId(String id);
	List<Member> findByIdIn(List<String> memberId);
//	List<Member> findByEmailIgnoreCaseAndIsDestroyedIsFalse(String email);
	Member findByEmailIgnoreCaseAndPasswordAndIsDestroyedIsFalse(String email, String password);
}
