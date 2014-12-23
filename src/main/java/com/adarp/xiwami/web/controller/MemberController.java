package com.adarp.xiwami.web.controller;

//import java.util.ArrayList;
import java.util.HashMap;
//import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.adarp.xiwami.web.dto.MemberSideload;
//import com.adarp.xiwami.web.dto.MemberSideloadList;
//import com.adarp.xiwami.domain.Family;
import com.adarp.xiwami.domain.Member;
import com.adarp.xiwami.service.FamilyService;
import com.adarp.xiwami.service.MemberService;

@RestController
public class MemberController {
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private FamilyService familyService;	

//	// Get member by facebookId or googleplusId - used when users login using third-party authentication system
//	// OUTPUT: Must contain { members: [ list of member object] }
//	@RequestMapping(value = "/members", method = RequestMethod.GET, produces = "application/json")
//	public MemberSideloadList FindFamilies(
//			@RequestParam(value="facebookId", required=false) String facebookId,
//			@RequestParam(value="googleplusId", required=false) String googleplusId) {			
//
//		List<Member> list =  memberService.FindMemberByFacebookId(facebookId);
//		
//		if (list.size() > 0) {
//			Family family = familyService.FindByFamilyId(list.get(0).getFamily());
//			List<Family> flist = new ArrayList<Family>();
//			flist.add(family);
//			
//			MemberSideloadList responseBody = new MemberSideloadList();
//			responseBody.members = list;
//			responseBody.families = flist;
//			return responseBody;			
//		} else {
//			MemberSideloadList responseBody = new MemberSideloadList();
//			responseBody.members = new ArrayList<Member>();
//			responseBody.families = null;
//			return responseBody;	
//		}
//		
//
//	}	
	
	// Possible scenarios:
	// Scenario #1: Get member by facebookId or googleplusId - used when users login using third-party authentication system
	// OUTPUT: Must contain { members: [ list of member object] }
	@RequestMapping(value = "/members", method = RequestMethod.GET, produces = "application/json")
	public Map<String, Member> FindFacebookMembers(
			@RequestParam(value="facebookId", required=false) String facebookId) {
			//@RequestParam(value="googleplusId", required=false) String googleplusId) {			

		Member facebookMember =  memberService.FindMemberByFacebookId(facebookId);
		Map<String, Member> responseBody = new HashMap<String, Member>();
		responseBody.put("member", facebookMember);
		return responseBody;
		/*
		if (facebookMember!= null) {
			//Family family = familyService.FindByFamilyId(list.get(0).getFamily());
			//List<Family> flist = new ArrayList<Family>();
			//flist.add(family);
			
			Map<String, List<Member>> responseBody = new HashMap<String, List<Member>>();
			responseBody.put("members", facebookMember);
			return responseBody;			
		} else {
			Map<String, List<Member>> responseBody = new HashMap<String, List<Member>>();
			responseBody.put("members", facebookMember);
			return responseBody;	
		}
		*/

	}		
	
	// Get Member by ID
	@RequestMapping(value = "/members/{id}", method = RequestMethod.GET, produces = "application/json")
	public Map<String, Member> FindByMemberId(@PathVariable("id") String id) {
		Member member = memberService.FindByMemberId(id);
		if (member == null) {
			member = memberService.FindMemberByFacebookId(id);
			
		}
		
		Map<String, Member> responseBody = new HashMap<String, Member>();		
		responseBody.put("member", member);
		
		return responseBody;
	}	
	
	// Add New Member
	// Possible scenarios:
	// 1. Users register using email and password (input parameter member will contain ONLY email and password).
	//    - must check if there is duplicate email exist before saving to database
	// 2. Users register using facebook (input parameter member contains facebookId and some other additional field.  
	//    - must check if the facebookId is duplicated or not, before saving it to the DB
	//    - NO password is needed because facebook controls it
	// 3. User create an additional family member.  In this case, facebookId and email will be null, because it's not a user of this app, it's a member of a family
	// OUTPUT: must be { member: null } OR { member: { member object }}
	@RequestMapping(value = "/members", method = RequestMethod.POST, produces = "application/json")
	public Map<String, Member> AddMember(@RequestBody MemberSideload member) {
		Member savedMember = null;
		
		if (member.member.getFacebookId() != null)
			savedMember = memberService.AddMemberByFacebookId(member.member);
		else if (member.member.getEmail() != null)
			savedMember = memberService.AddMember(member.member);
		else
			savedMember = memberService.AddMember(member.member);	
		
		Map<String, Member> responseBody = new HashMap<String, Member>();
		if (savedMember == null)
			responseBody.put("error: duplicate email or facebookId", savedMember);
		else
			responseBody.put("member", savedMember);
		
		return responseBody;
	}	
	
	// Update Member
	@RequestMapping(value = "/members/{id}", method = RequestMethod.PUT, produces = "application/json")
	public Map<String, Member> UpdateMember(@PathVariable("id") String id, @RequestBody MemberSideload member) {		
		
		Member savedMember = memberService.UpdateMember(id, member.member);
		Map<String, Member> responseBody = new HashMap<String, Member>();
		responseBody.put("member", savedMember);
		
		return responseBody;		
	}
	
	// Delete Member
	@RequestMapping (value = "/members/{id}", method = RequestMethod.DELETE, produces = "application/json")
	public void DeleteMember(@PathVariable("id")String id) {
		memberService.DeleteMember(id);
	}	
}
