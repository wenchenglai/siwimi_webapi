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

import com.adarp.xiwami.repository.*;
import com.adarp.xiwami.service.FamilyService;
import com.adarp.xiwami.domain.Family;
import com.adarp.xiwami.domain.Member;
import com.adarp.xiwami.web.dto.*;

@RestController
public class FamilyController {
	
	@Autowired
	private FamilyService familyService;
	
	@Autowired
	private MemberRepository memberRep;	

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
		
		// note : because of memberRep.FindMembers(...), we have keep try-catch here.....
		try {			
			FamilySideload responseBody = new FamilySideload();
			Family family = familyService.FindByFamilyId(id);
			
			List<Member> members = memberRep.FindMembers(family.getId());
			
			List<String> memberIds = new ArrayList<String>();
			for (Member member : members) {
				memberIds.add(member.getId());
			}
			family.setMembers(memberIds);
			
			responseBody.family = family;
			responseBody.members = members;
			return responseBody;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error : unable to query Family.");
			return null;
		}
	}
	
	// Add New Family
	@RequestMapping(value = "/families", method = RequestMethod.POST, produces = "application/json")
	public void AddFamily(@RequestBody FamilySideload newFamily) {
		familyService.AddFamily(newFamily);
	}	
	
	// Update Family
	@RequestMapping(value = "/families/{id}", method = RequestMethod.PUT, produces = "application/json")
	public void EditFamily(@PathVariable("id") String id, @RequestBody FamilySideload updatedFamily) {
		familyService.EditFamily(id, updatedFamily);
	}
	
	// Delete Family
	@RequestMapping (value = "/families/{id}", method = RequestMethod.DELETE, produces = "application/json")
	public void DeleteFamily(@PathVariable("id")String id) {
		familyService.DeleteFamily(id);
	}	
	
	// Search nearby Family
	@RequestMapping (value = "/families/{id}/{distance}", method = RequestMethod.GET, produces = "application/json")
	public Map<String,List<Family>> SearchFamilyNearby(@PathVariable("id")String id, @PathVariable("distance")double distance) {
		Map<String,List<Family>> responseBody = new HashMap<String,List<Family>>();
		responseBody.put("family", familyService.SearchFamilyNearby(id, distance));
		return responseBody;
	}	
}
