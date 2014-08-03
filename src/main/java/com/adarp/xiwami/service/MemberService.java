package com.adarp.xiwami.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adarp.xiwami.domain.Member;
import com.adarp.xiwami.repository.FamilyRepository;
import com.adarp.xiwami.repository.MemberRepository;

@Service
public class MemberService {
	
	@Autowired
	private MemberRepository memberRep;
	
	@Autowired
	private FamilyRepository familyRep;
	
	public Member AddMember(Member newMember){		
		newMember.setIsDeleted(false);
		Member member = memberRep.save(newMember);	
		return member;
	}	
	
	public Member UpdateMember(String id, Member updatedMember) {
		updatedMember.setId(id);	
		Member savedMember = memberRep.save(updatedMember);
		return savedMember;
	}
	
	
	public void DeleteMember(String id) {
		// For Member collection
		Member member = memberRep.findOne(id);
		member.setIsDeleted(true);
		memberRep.save(member);
	}
	
	public List<Member> FindMembers(String familyId) {
		return memberRep.findByFamilyInAndIsDeletedIsFalse(familyId);
	}

	public Member FindByMemberId(String id) {
		return memberRep.findOne(id);
	}	
	
	public List<Member> FindMemberByFacebookId(String id) {
		return memberRep.findByFacebookId(id);
	}
	
	public List<Member> FindMemberByGoogleplusId(String id) {
		return memberRep.findByGoogleplusId(id);
	}
}
