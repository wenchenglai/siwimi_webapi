package com.adarp.xiwami.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.adarp.xiwami.service.FamilyService;
import com.adarp.xiwami.service.MemberService;
import com.adarp.xiwami.web.dto.FamilySideload;
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
	public Map<String,List<Family>> FindFamilies(
			@RequestParam(value="longitude", required=true) Double longitude,
			@RequestParam(value="latitude", required=true) Double latitude,
			@RequestParam(value="distance", required=true) String qsDistance, 
			@RequestParam(value="fromAge", required=false) Integer fromAge,
			@RequestParam(value="toAge", required=false) Integer toAge,
			@RequestParam(value="languages[]", required=false) String[] languages) {			
		Map<String,List<Family>> responseBody = new HashMap<String,List<Family>>();
		responseBody.put("family", familyService.FindFamilies(longitude,latitude,qsDistance,fromAge,toAge,languages));
		return responseBody;		
	}

	// Get Family by ID
	@RequestMapping(value = "/families/{id}", method = RequestMethod.GET, produces = "application/json")
	public FamilySideload FindByFamilyId(@PathVariable("id") String id) {
		
		FamilySideload responseBody = new FamilySideload();
		Family family = familyService.FindByFamilyId(id);
		
		List<Member> members = memberService.FindMembers(family.getId());
		List<String> memberIds = new ArrayList<String>();
		for (Member member : members) {
			memberIds.add(member.getId());
		}
		family.setMembers(memberIds);
		
		responseBody.family = family;
		responseBody.members = members;
		
		return responseBody;
	}
	
	// Add New Family
	@RequestMapping(value = "/families", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<String> AddFamily(@RequestBody FamilySideload newFamily) {
		familyService.AddFamily(newFamily.family);
		return new ResponseEntity<String>(HttpStatus.CREATED);
	}	
	
	// Update Family
	@RequestMapping(value = "/families/{id}", method = RequestMethod.PUT, produces = "application/json")
	public void UpdateFamily(@PathVariable("id") String id, @RequestBody FamilySideload updatedFamily) {
		familyService.UpdateFamily(id, updatedFamily.family);
	}
	
	// Delete Family
	@RequestMapping (value = "/families/{id}", method = RequestMethod.DELETE, produces = "application/json")
	public void DeleteFamily(@PathVariable("id")String id) {
		familyService.DeleteFamily(id);
	}	
	
}
