package com.siwimi.webapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.siwimi.webapi.domain.Location;
import com.siwimi.webapi.domain.Member;
import com.siwimi.webapi.repository.FamilyRepository;
import com.siwimi.webapi.repository.LocationRepository;
import com.siwimi.webapi.repository.MemberRepository;

@Service
public class MemberService {
	
	@Autowired
	private MemberRepository memberRep;
	
	@Autowired
	private FamilyRepository familyRep;
	
	@Autowired
	private LocationRepository locationRep;
	
	/**
	Add New Member
	
	Possible scenarios:
	1. Users register using email and password (input parameter member will contain ONLY email and password).
	   - must check if there is duplicate email exist before saving to database
	2. Users register using facebook (input parameter member contains facebookId and some other additional field.  
	   - must check if the facebookId is duplicated or not, before saving it to the DB
	   - NO password is needed because facebook controls it
	3. User create an additional family member.  In this case, facebookId and email will be null.
	   Because it's not a user of this app, it's a member of a family
	
	OUTPUT: must be { member: null } OR { member: { member object }}
	
	**/
	
	public Member addMember(Member newMember){		
		// If newMember is a duplicated member, don't save it.
		if (memberRep.findExistingMember(newMember.getFacebookId(), newMember.getEmail())!=null) {
			return null;
		} else {
			newMember.setIsDestroyed(false);
			newMember = updateLocation(newMember);
			Member member = memberRep.save(newMember);	
			return member;	
		}
	}
	
	public Member updateMember(String id, Member updatedMember) {
		updatedMember.setId(id);	
		updatedMember = updateLocation(updatedMember);
		Member savedMember = memberRep.save(updatedMember);
		return savedMember;
	}
		
	public void deleteMember(String id) {
		// For Member collection
		Member member = memberRep.findOne(id);
		member.setIsDestroyed(true);
		memberRep.save(member);
	}
	
	public Member findByMemberId(String id) {
		return memberRep.findByid(id);
	}	
	
	public List<Member> find(String familyId, String queryText) {
		return memberRep.query(familyId, queryText);
	}

	public Member updateLocation(Member member) {
		// lookup location from the collection Location;
		Location thisLocation = locationRep.queryLocation(member.getZipCode(), member.getCity(), member.getState());
		// set longitude and latitude 
		if (thisLocation!=null) {
			double[] location = {thisLocation.getLongitude(), thisLocation.getLatitude()};
			member.setZipCode(thisLocation.getZipCode());
			member.setLocation(location);
			member.setCity(thisLocation.getTownship());
			member.setState(thisLocation.getStateCode());
		}

		return member;
	}
	
}
