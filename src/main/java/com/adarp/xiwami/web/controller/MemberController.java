package com.adarp.xiwami.web.controller;

import java.io.File;
import java.io.IOException;
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

import com.google.common.io.BaseEncoding;
import com.google.common.io.Files;
import com.adarp.xiwami.web.dto.MemberSideload;
import com.adarp.xiwami.web.dto.MemberSideloadList;
import com.adarp.xiwami.domain.Family;
import com.adarp.xiwami.domain.Member;
import com.adarp.xiwami.service.FamilyService;
import com.adarp.xiwami.service.MemberService;

@RestController
public class MemberController {
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private FamilyService familyService;	

	// Get member(s)
//	@RequestMapping(value = "/members", method = RequestMethod.GET, produces = "application/json")
//	public Map<String, List<Member>> FindFamilies(	
	@RequestMapping(value = "/members", method = RequestMethod.GET, produces = "application/json")
	public MemberSideloadList FindFamilies(
			@RequestParam(value="facebookId", required=false) String facebookId,
			@RequestParam(value="googleplusId", required=false) String googleplusId) {			
		//Map<String, List<Member>> responseBody = new HashMap<String,List<Member>>();
		List<Member> list =  memberService.FindMemberByFacebookId(facebookId);
		Family family = familyService.FindByFamilyId(list.get(0).getFamily());
		List<Family> flist = new ArrayList<Family>();
		flist.add(family);
		
		MemberSideloadList responseBody = new MemberSideloadList();
		responseBody.member = list;
		responseBody.family = flist;
		return responseBody;
	}	
	
	// Get Member by ID
	@RequestMapping(value = "/members/{id}", method = RequestMethod.GET, produces = "application/json")
	public Map<String, Member> FindByMemberId(@PathVariable("id") String id) {
		Member member = memberService.FindByMemberId(id);
		Map<String, Member> responseBody = new HashMap<String, Member>();
		responseBody.put("member", member);
		
		return responseBody;
	}	
	
	// Add New Member
	@RequestMapping(value = "/members", method = RequestMethod.POST, produces = "application/json")
	public Map<String, Member> AddMember(@RequestBody MemberSideload member) {
		Member savedMember = memberService.AddMember(member.member);	
		
		Map<String, Member> responseBody = new HashMap<String, Member>();
		responseBody.put("member", savedMember);
		
		return responseBody;
	}	
	
	// Update Member
	@RequestMapping(value = "/members/{id}", method = RequestMethod.PUT, produces = "application/json")
	public Map<String, Member> UpdateMember(@PathVariable("id") String id, @RequestBody MemberSideload member) {		
		String rawImage = member.member.getImageData();
		
		if (rawImage != null) {
			StringBuilder sb = new StringBuilder();
			sb.append("src/main/resources/assets/img/");
			sb.append(id);
			sb.append(".jpg");
				
			File file = new File(sb.toString());
			file.deleteOnExit();
				
			
			rawImage = rawImage.substring(23);
				
			BaseEncoding baseEncoding = BaseEncoding.base64();
			byte[] bytes = baseEncoding.decode(rawImage);
				
			try {
				Files.write(bytes, file);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}				
		}
		
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
