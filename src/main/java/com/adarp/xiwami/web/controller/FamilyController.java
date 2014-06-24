package com.adarp.xiwami.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.adarp.xiwami.repository.*;
import com.adarp.xiwami.domain.Family;
import com.adarp.xiwami.domain.Member;
import com.adarp.xiwami.web.dto.*;

@RestController
public class FamilyController {
	@Autowired
	private FamilyRepository familyRep;
	
	@Autowired
	private MemberRepository memberRep;	

	// Get all families
	@RequestMapping(value = "/families", method = RequestMethod.GET, produces = "application/json")
	public Map<String,List<Family>> FindFamilies() {
		try {				
			Map<String,List<Family>> responseBody = new HashMap<String,List<Family>>();
			List<Family> list = familyRep.GetFamilies();
			responseBody.put("family", list);
			return responseBody;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error : unable to query Families.");
			return null;
		}
	}

	// Get Family by ID
	@RequestMapping(value = "/families/{id}", method = RequestMethod.GET, produces = "application/json")
	public FamilySideload FindByFamilyId(@PathVariable("id") String id) {
		
		try {
			//return "Family:{" + myFamily.GetFamilyById(id) + "}";
			FamilySideload responseBody = new FamilySideload();
			
			Family family = familyRep.GetFamilyById(id);
			
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
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error : unable to query Family.");
			return null;
		}
	}
	
	// Add New Family
	@RequestMapping(value = "/families", method = RequestMethod.POST, produces = "application/json")
	public void AddFamily(@RequestBody FamilySideload newFamily)
	{
		try {
			familyRep.AddFamily(newFamily.family);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error : unable to add Family.");
		}
		
	}	
	
	// Update Family
	@RequestMapping(value = "/families/{id}", method = RequestMethod.PUT, produces = "application/json")
	public void EditFamily(@PathVariable("id") String id, @RequestBody FamilySideload updatedFamily)
	{
		try {
			updatedFamily.family.setId(id);
			familyRep.UpdateFamily(updatedFamily.family);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error : unable to add Family.");
		}
	}
	
	// Delete Family
	@RequestMapping (value = "/families/{id}", method = RequestMethod.DELETE, produces = "application/json")
	public void DeleteFamily(@PathVariable("id")String id) {
		try {
			familyRep.DeleteFamily(id);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error : unable to add Family.");			
		}
	}	
}
