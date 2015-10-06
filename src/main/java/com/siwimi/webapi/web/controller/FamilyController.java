package com.siwimi.webapi.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.siwimi.webapi.domain.Family;
import com.siwimi.webapi.domain.Member;
import com.siwimi.webapi.service.FamilyService;
import com.siwimi.webapi.service.MemberService;
import com.siwimi.webapi.web.dto.FamilySideload;
import com.siwimi.webapi.web.dto.FamilySideloadList;

@RestController
public class FamilyController {
	
	@Autowired
	private FamilyService familyService;
	
	@Autowired
	private MemberService memberService;

	// Get all families
	@RequestMapping(value = "/families", method = RequestMethod.GET, produces = "application/json")
	public  FamilySideloadList findFamilies(
			@RequestParam(value="longitude", required=true) Double longitude,
			@RequestParam(value="latitude", required=true) Double latitude,
			@RequestParam(value="distance", required=true) String qsDistance, 
			@RequestParam(value="fromAge", required=false) Integer fromAge,
			@RequestParam(value="toAge", required=false) Integer toAge,
			@RequestParam(value="languages[]", required=false) String[] languages) {
		FamilySideloadList responseBody = new FamilySideloadList();
		List<Family> families = familyService.findFamilies(longitude,latitude,qsDistance,fromAge,toAge,languages);
		List<Member> members = new ArrayList<Member>();
		if (families!=null) {
			for (Family family:families) {
				List<Member> membersPerFamily = memberService.find(family.getId(),null);
				List<String> memberIds = new ArrayList<String>();
				for (Member member : membersPerFamily) {
					memberIds.add(member.getId());
					members.add(member);
				}
				family.setMembers(memberIds);
			}
		}
		responseBody.family = families;
		responseBody.members = members;
		return responseBody;
	}

	// Get Family by ID
	@RequestMapping(value = "/families/{id}", method = RequestMethod.GET, produces = "application/json")
	public FamilySideload findByFamilyId(@PathVariable("id") String id) {
		FamilySideload responseBody = new FamilySideload();
		Family family = familyService.findByFamilyId(id);

		if (family != null) {
			List<Member> members = memberService.find(family.getId(),null);
			List<String> memberIds = new ArrayList<String>();
			for (Member member : members) {
				memberIds.add(member.getId());
			}
			family.setMembers(memberIds);
			
			responseBody.family = family;
			responseBody.members = members;
		}
		return responseBody;
	}
	
	// Add New Family
	@RequestMapping(value = "/families", method = RequestMethod.POST, produces = "application/json")
	public Map<String, Family> addFamily(@RequestBody FamilySideload newFamily) {
		Family family = familyService.addFamily(newFamily.family);
		Map<String, Family> responseBody = new HashMap<String, Family>();
		responseBody.put("family", family);
		
		return responseBody;
	}	
	
	// Update Family
	// 2015-02-14 When updating family, the sideloaded members must be present, else front end won't be able to load proper members
	@RequestMapping(value = "/families/{id}", method = RequestMethod.PUT, produces = "application/json")
	public FamilySideload updateFamily(@PathVariable("id") String id, @RequestBody FamilySideload updatedFamily) {
		Family family = familyService.updateFamily(id, updatedFamily.family);
		
		FamilySideload responseBody = new FamilySideload();		
		if (family != null) {
			List<Member> members = memberService.find(family.getId(),null);
			List<String> memberIds = new ArrayList<String>();
			for (Member member : members) {
				memberIds.add(member.getId());
			}
			family.setMembers(memberIds);
			
			responseBody.family = family;
			responseBody.members = members;
		}
		return responseBody;	
	}

	// Delete Family
	// 2015-02-14 front end needs the deleted object to make sure the deletion is a success
	@RequestMapping (value = "/families/{id}", method = RequestMethod.DELETE, produces = "application/json")
	public Map<String, Family> deleteFamily(@PathVariable("id")String id, HttpServletResponse response) {
		Family family = familyService.deleteFamily(id);
		Map<String, Family> responseBody = new HashMap<String, Family>();
		responseBody.put("family", family);
		if (family != null) 
			response.setStatus(HttpServletResponse.SC_OK);
		else
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		
		return responseBody;			
	}	
}
