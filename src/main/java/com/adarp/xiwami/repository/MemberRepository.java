package com.adarp.xiwami.repository;

import java.util.List;

import org.springframework.stereotype.Service;

import com.adarp.xiwami.domain.Member;

@Service
public interface MemberRepository {
	public void AddMember(Member member) throws Exception;
	
	public void UpdateMember(Member member);	
	
	public void DeleteMember(String id) throws Exception;
	
	List<Member> FindMembers(String familyId) throws Exception;		
}
