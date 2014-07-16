package com.adarp.xiwami.web.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
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
import com.adarp.xiwami.domain.Member;
import com.adarp.xiwami.service.MemberService;

@RestController
public class MemberController {
	
	@Autowired
	private MemberService memberService;		

	// Get member(s)
	@RequestMapping(value = "/members", method = RequestMethod.GET, produces = "application/json")
	public Map<String, Member> FindFamilies(
			@RequestParam(value="facebookId", required=false) String facebookId,
			@RequestParam(value="googleplusId", required=false) String googleplusId) {			
		Map<String, Member> responseBody = new HashMap<String,Member>();
		responseBody.put("member", memberService.FindMemberByFacebookId(facebookId));
		return responseBody;
	}	
	
	// Add New Member
	@RequestMapping(value = "/members", method = RequestMethod.POST, produces = "application/json")
	public void AddMember(@RequestBody MemberSideload member) {
		memberService.AddMember(member.member);		
	}	
	
	// Update Member
	@RequestMapping(value = "/members/{id}", method = RequestMethod.PUT, produces = "application/json")
	public void UpdateMember(@PathVariable("id") String id, @RequestBody MemberSideload member) {		
		StringBuilder sb = new StringBuilder();
		sb.append("src/main/resources/assets/img/");
		sb.append(id);
		sb.append(".jpg");
			
		File file = new File(sb.toString());
		file.deleteOnExit();
			
		String raw = member.member.getImageData();
		raw = raw.substring(23);
			
		BaseEncoding baseEncoding = BaseEncoding.base64();
		byte[] bytes = baseEncoding.decode(raw);
			
		try {
			Files.write(bytes, file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
	
		memberService.UpdateMember(id, member.member);	
	}
	
	// Delete Member
	@RequestMapping (value = "/members/{id}", method = RequestMethod.DELETE, produces = "application/json")
	public void DeleteMember(@PathVariable("id")String id) {
		memberService.DeleteMember(id);
	}	
}
