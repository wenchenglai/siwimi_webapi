package com.siwimi.webapi.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.siwimi.webapi.domain.Member;
import com.siwimi.webapi.service.AuthService;

@RestController
public class AuthController {
	
	@Autowired
	private AuthService authService;
	
	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json")
	public Map<String, Object> login(@RequestBody Member member) {
		Member getMember = authService.getMember(member);
		Map<String, Object> responseBody = new HashMap<String, Object>();		
		if (getMember != null){
			responseBody.put("auth", "success");
			responseBody.put("member", getMember);
		}
		else
			responseBody.put("auth", "fail");
		
		return responseBody;		
	}	
}
