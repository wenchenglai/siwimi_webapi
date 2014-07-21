package com.adarp.xiwami.service;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adarp.xiwami.domain.Family;
import com.adarp.xiwami.domain.Member;
import com.adarp.xiwami.repository.FamilyRepository;
import com.adarp.xiwami.repository.MemberRepository;

@Service
public class MemberService {
	
	@Autowired
	private MemberRepository memberRep;
	
	@Autowired
	private FamilyRepository familyRep;
	
	public void AddMember(Member newMember){
		// For Member collection
		newMember.setIsDeleted(false);
		memberRep.save(newMember);	
		
		// it's possible to add a member without a family associated, e.g. sing up for the first time using facebook/google+
		if (newMember.getFamily() != null) {
			// For Family collection
			Family thisFamily = familyRep.findOne(newMember.getFamily());
			List<String> thisFamilyMembers = thisFamily.getMembers();
			thisFamilyMembers.add(newMember.getId());
			thisFamily.setMembers(thisFamilyMembers);
			familyRep.save(thisFamily);		
		}
	}	
	
	public void UpdateMember(String id, Member updatedMember) {
		updatedMember.setId(id);
		updatedMember.setAvatarUrl(id + ".jpg");	
		memberRep.save(updatedMember);
	}
	
	
	public void DeleteMember(String id) {
		// For Member collection
		Member member = memberRep.findOne(id);
		member.setIsDeleted(true);
		memberRep.save(member);
		
		// For Family collection
		Family thisFamily = familyRep.findOne(member.getFamily());
		List<String> thisFamilyMembers = thisFamily.getMembers();
		Iterator<String> it = thisFamilyMembers.iterator();
		while (it.hasNext()) {
			if (it.next().equals(id)) {
				it.remove();
				break;
			}
		}
		thisFamily.setMembers(thisFamilyMembers);
		familyRep.save(thisFamily);
	}
	
	public List<Member> FindMembers(String familyId) {
		return memberRep.findByFamilyInAndIsDeletedIsFalse(familyId);
	}
	
	public List<Member> FindMemberByFacebookId(String id) {
		return memberRep.findByFacebookId(id);
	}
	
	public List<Member> FindMemberByGoogleplusId(String id) {
		return memberRep.findByGoogleplusId(id);
	}
}
