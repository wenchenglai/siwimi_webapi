package com.siwimi.webapi.repository;

import java.util.List;
import java.util.Set;

import com.siwimi.webapi.domain.Member;

public interface MemberRepositoryCustom {
	Member queryExistingMember(String id);
	Member LoginExistingMember(String email, String password);
	List<Member> query(String familyId, String queryText);
	Set<String> findFamilies(List<String> geoFamiliesId,Integer fromAge,Integer toAge,String[] languages);
}
