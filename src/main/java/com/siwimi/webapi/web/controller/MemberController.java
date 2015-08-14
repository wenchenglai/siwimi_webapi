package com.siwimi.webapi.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.siwimi.webapi.domain.JqueryObject;
import com.siwimi.webapi.domain.Member;
import com.siwimi.webapi.exception.ExistingMemberException;
import com.siwimi.webapi.service.EmailService;
import com.siwimi.webapi.service.FamilyService;
import com.siwimi.webapi.service.MemberService;
import com.siwimi.webapi.web.dto.MemberSideload;

@RestController
public class MemberController {
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private FamilyService familyService;	

	@Autowired
	private EmailService emailService;
	
    @Autowired
    private HttpServletRequest httpServletRequest;
	
	/**
	Scenario #1 : Get member by queryText (firstname or lastname) 
	OUTPUT      : Must contain { members: [ list of member object] }
	
	Or, confirm a newly added member
	**/
	@RequestMapping(value = "/members", method = RequestMethod.GET, produces = "application/json")
	public Map<String, List<Member>> findMembers(
			@RequestParam(value="queryText", required=false) String queryText,
			@RequestParam(value="id", required=false) String id,
            @RequestParam(value="action", required=false) String action) {		
		
		Map<String, List<Member>> responseBody = new HashMap<String, List<Member>>();
		
		if (action == null) {
			/** Query Member **/
			List<Member> members = memberService.find(null,queryText);
			if (members==null)
				members = new ArrayList<Member>();
			responseBody.put("member", members);			
		} else {
			/** Confirm new member **/
			if (id != null) {
				Member member = memberService.setConfirmedMember(id, action);
				// We have to use List<Member> to meet the output format of Map<String, List<Member>>	
				List<Member> members = new ArrayList<Member>();
				members.add(member);
				responseBody.put("member", members);	
			}
		}
		
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
	
	/** confirm a newly added member **/
	@RequestMapping(value = "/member", method = RequestMethod.GET, produces = "application/json")
	public Map<String, Member> confirmMember(
			@RequestParam(value="id", required=false) String id,
            @RequestParam(value="action", required=false) String action) {				
		Map<String, Member> responseBody = new HashMap<String, Member>();
		Member member = memberService.setConfirmedMember(id, action);
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
			throw new ExistingMemberException("This email is already on our system");
		else {
			responseBody.put("member", savedMember);
			emailService.notifyNewMemberToSiwimi(savedMember);
			// This is for backend development at local machine purpose
			String serverName = this.httpServletRequest.getServerName();
			if (serverName != null) {
				if (serverName.toLowerCase().contains("localhost"))
					emailService.notifyConfirmationToNewMember(savedMember,true);
				else
					emailService.notifyConfirmationToNewMember(savedMember,false);				
			} else
				emailService.notifyConfirmationToNewMember(savedMember,false);
		}		
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
	
	// Exception handler
	@ExceptionHandler(ExistingMemberException.class)
	public Map<String, Map<String, String>> handleExistingMemberHandler(ExistingMemberException ex, HttpServletResponse res) {
		Map<String, Map<String, String>> responseBody = new HashMap<String, Map<String, String>>();
		Map<String, String> response = new HashMap<String, String>();
		response.put("email",ex.getErrMsg());
		responseBody.put("error",response);
		res.setStatus(422);
		return responseBody;
	}
}
