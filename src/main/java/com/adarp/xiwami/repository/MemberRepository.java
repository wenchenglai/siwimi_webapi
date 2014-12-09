package com.adarp.xiwami.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.adarp.xiwami.domain.Member;


public interface MemberRepository extends MongoRepository<Member, String>, MemberRepositoryCustom{	
	List<Member> findByFamilyInAndIsDeletedIsFalse(String familyId);
	List<Member> findByFamilyInAndIsDeletedIsFalse(List<String> familyId);
	List<Member> findByFamilyInAndBirthdayBetweenAndIsDeletedIsFalse(List<String> geoFamilyId,Date fromDate, Date toDate);
	List<Member> findByFamilyInAndLanguagesInAndBirthdayBetweenAndIsDeletedIsFalse(List<String> geoFamilyId,List<String> languageList,Date fromDate, Date toDate);
	// It's annoying to distinguish list & single-object in ember
	//List<Member> findByFacebookId(String id);
	//List<Member> findByGoogleplusId(String id);
	Member findByFacebookId(String id);
	Member findByGoogleplusId(String id);
	List<Member> findByIdIn(List<String> memberId);
	List<Member> findByEmailIgnoreCaseAndIsDeletedIsFalse(String email);
	Member findByEmailIgnoreCaseAndPasswordAndIsDeletedIsFalse(String email, String password);
}
