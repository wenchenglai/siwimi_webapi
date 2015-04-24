package com.siwimi.webapi.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.siwimi.webapi.domain.JqueryObject;
import com.siwimi.webapi.domain.Member;
import com.siwimi.webapi.service.FamilyService;
import com.siwimi.webapi.service.MemberService;
import com.siwimi.webapi.web.dto.MemberSideload;

@RestController
public class MemberController {
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private FamilyService familyService;	

	
	/**
	Scenario #1 : Get member by queryText (firstname or lastname) 
	OUTPUT      : Must contain { members: [ list of member object] }
	**/
	@RequestMapping(value = "/members", method = RequestMethod.GET, produces = "application/json")
	public Map<String, List<Member>> findMembers(
			@RequestParam(value="queryText", required=false) String queryText) {			
		List<Member> members = memberService.find(null,queryText);
		if (members==null)
			members = new ArrayList<Member>();
		Map<String, List<Member>> responseBody = new HashMap<String, List<Member>>();
		responseBody.put("member", members);
		return responseBody;
	}		
	
	/**
	Scenario #2 : Get member by queryText (firstname or lastname) 

	**/
	@RequestMapping(value = "/membersjquery", method = RequestMethod.GET, produces = "application/json")
	public List<JqueryObject> findFuzzyMembers(
			@RequestParam(value="queryText", required=false) String queryText) {			
		List<Member> members = memberService.find(null,queryText);
		if (members==null)
			members = new ArrayList<Member>();
		
		List<JqueryObject> jqueryObjects = new ArrayList<JqueryObject>();
		for (Member member : members) {
			String memberName = member.getFirstName() + " " + member.getLastName();
			//jqueryObjects.add(new JqueryObject(memberName,member.getId()));
			jqueryObjects.add(new JqueryObject(memberName,memberName));
		}

		return jqueryObjects;
	}		
	
	/**
	Scenario #3 : Get member by queryText (firstname or lastname) 

	**/
	@RequestMapping(value = "/membersajax", method = RequestMethod.GET, produces = "application/json")
	public List<String> findFuzzyMembersAjax(
			@RequestParam(value="queryText", required=false) String queryText) {			
		List<Member> members = memberService.find(null,queryText);
		if (members==null)
			members = new ArrayList<Member>();
		
		List<String> obj = new ArrayList<String>();
		for (Member member : members) {
			String memberName = member.getFirstName() + " " + member.getLastName();
			obj.add(memberName);
		}

		return obj;
	}		
	
	/**
	 Find member either by database id or facebook id.
	 **/
	// Get Member by ID
	@RequestMapping(value = "/members/{id}", method = RequestMethod.GET, produces = "application/json")
	public Map<String, Member> findByMemberId(@PathVariable("id") String id) {
		Member member = memberService.findByMemberId(id);	
		Map<String, Member> responseBody = new HashMap<String, Member>();		
		responseBody.put("member", member);		
		return responseBody;
	}	
	
	/**
	Add New Member
	**/
	@RequestMapping(value = "/members", method = RequestMethod.POST, produces = "application/json")
	public Map<String, Member> addMember(@RequestBody MemberSideload member) {
		Member savedMember = memberService.addMember(member.member);
		Map<String, Member> responseBody = new HashMap<String, Member>();
		if (savedMember == null)
			responseBody.put("error: duplicate email or facebookId", savedMember);
		else
			responseBody.put("member", savedMember);
		
		return responseBody;
	}	
	
	// Update Member
	@RequestMapping(value = "/members/{id}", method = RequestMethod.PUT, produces = "application/json")
	public Map<String, Member> updateMember(@PathVariable("id") String id, @RequestBody MemberSideload member) {		
		
		Member savedMember = memberService.updateMember(id, member.member);
		Map<String, Member> responseBody = new HashMap<String, Member>();
		responseBody.put("member", savedMember);
		
		return responseBody;		
	}
	
	// Delete Member
	@RequestMapping (value = "/members/{id}", method = RequestMethod.DELETE, produces = "application/json")
	public void deleteMember(@PathVariable("id")String id) {
		memberService.deleteMember(id);
	}	
}
