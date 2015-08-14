package com.siwimi.webapi.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.siwimi.webapi.domain.Member;
import com.siwimi.webapi.exception.ExistingMemberException;
import com.siwimi.webapi.service.EmailService;
import com.siwimi.webapi.service.MemberService;

@RestController
public class EmailController {
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private MemberService memberService;

    @Autowired
    private HttpServletRequest httpServletRequest;
    
	// Get Email by ID
	@RequestMapping(value = "/email/sendConfirmation", method = RequestMethod.GET, produces = "application/json")
	public Map<String, Member> sendConfirmation(@RequestParam(value="id", required=true) String id) {
		Member member = memberService.findByMemberId(id);
		Map<String, Member> responseBody = new HashMap<String, Member>();
		if (member == null)
			throw new ExistingMemberException("Unable to find this member!");
		else {
			responseBody.put("member", member);
			// This is for backend development at local machine purpose
			String serverName = this.httpServletRequest.getServerName();
			if (serverName != null) {
				if (serverName.toLowerCase().contains("localhost"))
					emailService.notifyConfirmationToNewMember(member,true);
				else
					emailService.notifyConfirmationToNewMember(member,false);
			} else
				emailService.notifyConfirmationToNewMember(member,false);
		}		
		return responseBody;
	}
		
	// Exception handler
	@ExceptionHandler(ExistingMemberException.class)
	public Map<String, String> handleExistingMemberHandler(ExistingMemberException ex, HttpServletResponse res) {
		Map<String, String> responseBody = new HashMap<String, String>();
		responseBody.put("error",ex.getErrMsg());
		res.setStatus(422);
		return responseBody;
	}
}
