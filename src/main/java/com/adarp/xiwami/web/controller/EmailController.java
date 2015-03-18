package com.adarp.xiwami.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.adarp.xiwami.domain.Email;
import com.adarp.xiwami.web.dto.EmailSideload;

@RestController
public class EmailController {
	
	
	// send New email
	@RequestMapping(value = "/emails", method = RequestMethod.POST, produces = "application/json")
	public Map<String, Email> addEmail(@RequestBody EmailSideload newEmail) {
		//Email savedEmail = emailService.addEmail(newEmail.email);
		
		Map<String, Email> responseBody = new HashMap<String, Email>();
		//responseBody.put("email", savedEmail);
		
		return responseBody;		
	}	
}
