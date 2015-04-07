package com.siwimi.webapi.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.siwimi.webapi.domain.Emaction;
import com.siwimi.webapi.domain.Member;
import com.siwimi.webapi.service.EmactionService;
import com.siwimi.webapi.service.MemberService;
import com.siwimi.webapi.web.dto.EmactionSideload;
import com.siwimi.webapi.web.dto.EmactionSideloadList;

@RestController
public class EmactionController {

	@Autowired
	private EmactionService emactionService;
		
	@Autowired
	private MemberService memberService;
	
	// Get all emactions
	@RequestMapping(value = "/emactions", method = RequestMethod.GET, produces = "application/json")
	public EmactionSideloadList findEmactions(@RequestParam(value="event", required=false) String event) {		
		EmactionSideloadList responseBody = new EmactionSideloadList();
		List<Emaction> emactionList = emactionService.findEmactions(event);
		Set<Member> members = new HashSet<Member>();
		if (emactionList!=null) {
			for (Emaction emaction : emactionList) {
				Member member = memberService.findByMemberId(emaction.getMember());
				// we must return an empty object so Ember can pick up the json data format.  Return null will crash the ember client.
				if (member!=null)
					members.add(member);
			}
		} else {
			// we must return an empty array so Ember can pick up the json data format.  Return null will crash the ember client.
			emactionList = new ArrayList<Emaction>();
		}
		responseBody.emactions = emactionList;
		responseBody.members = new ArrayList<Member>(members);

		return responseBody;
	}

	// Get Emaction by ID
	@RequestMapping(value = "/emactions/{id}", method = RequestMethod.GET, produces = "application/json")
	public Map<String,Emaction> findByEmactionId(@PathVariable("id") String id) {
		Map<String,Emaction> responseBody = new HashMap<String,Emaction>();			
		Emaction emaction = emactionService.findByEmactionId(id);
		responseBody.put("emaction", emaction);
		return responseBody;
	}
	
	// Update Emaction
	@RequestMapping(value = "/emactions", method = RequestMethod.PUT, produces = "application/json")
	public Map<String, Emaction> updateEmaction(@RequestBody EmactionSideload updatedEmaction){
		Emaction savedEmaction = emactionService.updateEmaction(updatedEmaction.emaction);		
		Map<String,Emaction> responseBody = new HashMap<String, Emaction>();
		responseBody.put("emaction", savedEmaction);
		return responseBody;			
	}
	
	// Delete Emaction
	@RequestMapping (value = "/emactions/{id}", method = RequestMethod.DELETE, produces = "application/json")
	public void deleteEmaction(@PathVariable("id")String id) {
		emactionService.deleteEmaction(id);
	}	
}
