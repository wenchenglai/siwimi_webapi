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
	
	public Member addMember(Member newMember){
		// 2014-12-11 A scenario would be that a new member is added by another user, so there is no email, no facebookId 
		if (newMember.getEmail() != null && memberRep.findByEmailIgnoreCaseAndIsDestroyedIsFalse(newMember.getEmail()).size()>0)
			// make sure no one uses the same email twice to sign up 
			return null;
		else {
			newMember.setIsDestroyed(false);
			Member member = memberRep.save(newMember);	
			return member;	
		}
	}
	
	public Member addMemberByFacebookId(Member newMember){
		// TODO: Must check if facebookId exist or not
		newMember.setIsDestroyed(false);
		Member member = memberRep.save(newMember);	
		return member;	
	}
	
	public Member updateMember(String id, Member updatedMember) {
		updatedMember.setId(id);	
		Member savedMember = memberRep.save(updatedMember);
		return savedMember;
	}
	
	
	public void deleteMember(String id) {
		// For Member collection
		Member member = memberRep.findOne(id);
		member.setIsDestroyed(true);
		memberRep.save(member);
	}
	
	public List<Member> findMembers(String familyId) {
		return memberRep.findByFamilyInAndIsDestroyedIsFalse(familyId);
	}

	public Member findByMemberId(String id) {
		return memberRep.findOne(id);
	}	
	
	public Member findMemberByFacebookId(String id) {
		return memberRep.findByFacebookId(id);
	}
	
	public List<Member> find(String queryText) {
		return memberRep.query(queryText);
	}

}
