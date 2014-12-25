package com.adarp.xiwami.web.controller;

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

import com.adarp.xiwami.service.FamilyService;
import com.adarp.xiwami.service.MemberService;
import com.adarp.xiwami.web.dto.FamilySideload;
import com.adarp.xiwami.web.dto.FamilySideloadList;
import com.adarp.xiwami.domain.Family;
import com.adarp.xiwami.domain.Member;

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
				List<Member> membersPerFamily = memberService.findMembers(family.getId());
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
			List<Member> members = memberService.findMembers(family.getId());
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
	@RequestMapping(value = "/families/{id}", method = RequestMethod.PUT, produces = "application/json")
	public Map<String, Family> updateFamily(@PathVariable("id") String id, @RequestBody FamilySideload updatedFamily) {
		Family savedFamily = familyService.updateFamily(id, updatedFamily.family);
		Map<String, Family> responseBody = new HashMap<String, Family>();
		responseBody.put("family", savedFamily);
		
		return responseBody;		
	}
	
	// Delete Family
	@RequestMapping (value = "/families/{id}", method = RequestMethod.DELETE, produces = "application/json")
	public void deleteFamily(@PathVariable("id")String id) {
		familyService.deleteFamily(id);
	}		
}
