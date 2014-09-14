package com.adarp.xiwami.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.adarp.xiwami.domain.Member;


@RestController
public class AuthController {
	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json")
	public Map<String, String> Login(@RequestBody Member member) {
		Map<String, String> responseBody = new HashMap<String, String>();
		responseBody.put("auth", "success");
		return responseBody;		
	}	
}
