package com.adarp.xiwami.web.controller;

import java.io.File;

import com.adarp.xiwami.web.dto.MemberSideload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.adarp.xiwami.repository.MemberRepository;
import com.google.common.io.BaseEncoding;
import com.google.common.io.Files;

@Controller
public class MemberController {
	
	@Autowired
	private MemberRepository memberRep;		

	// Add New Member
	@RequestMapping(value = "/members", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public void AddMember(@RequestBody MemberSideload member)
	{
		try {
			memberRep.AddMember(member.member);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error : unable to add Family.");
		}
		
	}	
	
	// Update Member
	@RequestMapping(value = "/members/{id}", method = RequestMethod.PUT, produces = "application/json")
	@ResponseBody
	public void EditMember(@PathVariable("id") String id, @RequestBody MemberSideload member)
	{
		try {
			member.member.setId(id);
			
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
			
			Files.write(bytes, file);			
			
			member.member.setAvatarUrl(id + ".jpg");
			memberRep.UpdateMember(member.member);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error : unable to add Family.");
		}
	}
	
	// Delete Member
	@RequestMapping (value = "/members/{id}", method = RequestMethod.DELETE, produces = "application/json")
	@ResponseBody
	public void DeleteMember(@PathVariable("id")String id) {
		try {
			memberRep.DeleteMember(id);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error : unable to add Family.");			
		}
	}	
}
