package com.adarp.xiwami.repository;

import java.util.List;

import com.adarp.xiwami.domain.Member;

public interface MemberRepositoryCustom {
	
	public void AddMember(Member member) throws Exception;
	
	public void UpdateMember(Member member);	
	
	public void DeleteMember(String id) throws Exception;
	
	List<Member> FindMembers(String familyId) throws Exception;		
}
