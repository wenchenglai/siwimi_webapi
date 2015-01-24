package com.adarp.xiwami.repository;

import java.util.List;

import com.adarp.xiwami.domain.Member;

public interface MemberRepositoryCustom {
	List<Member> query(String queryText);
	Member findByid(String id);
	Member findDuplicated(String facebookId, String email);
}
