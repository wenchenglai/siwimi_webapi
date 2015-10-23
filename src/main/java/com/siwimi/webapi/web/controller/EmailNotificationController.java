package com.siwimi.webapi.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.siwimi.webapi.domain.EmailNotification;
import com.siwimi.webapi.service.EmailNotificationService;
import com.siwimi.webapi.web.dto.EmailNotificationSideload;


@RestController
public class EmailNotificationController {

	@Autowired
	private EmailNotificationService emailNotificationService;
	
	// Get emailNotification from creator Id
	@RequestMapping(value = "/emailnotification/{creatorId}", method = RequestMethod.GET, produces = "application/json")
	public Map<String,EmailNotification> findEmailNotification(@PathVariable("creatorId") String creatorId) {
		Map<String,EmailNotification> responseBody = new HashMap<String,EmailNotification>();
		responseBody.put("emailNotification", emailNotificationService.findByCreator(creatorId));
		return responseBody;
	}
	
	
	// Update emailNotification
	@RequestMapping(value = "/emailnotification/{creatorId}", method = RequestMethod.PUT, produces = "application/json")
	public Map<String, EmailNotification> updateEmailNotification(@PathVariable("creatorId") String creatorId, 
			                                                      @RequestBody EmailNotificationSideload updatedEmailNotification){
		Map<String,EmailNotification> responseBody = new HashMap<String, EmailNotification>();
		responseBody.put("emailNotification", emailNotificationService.updateEmailNotification(creatorId, 
				                                                                           updatedEmailNotification.emailNotification));
		return responseBody;			
	}
	
	// Add New emailNotification
	@RequestMapping(value = "/email-notification", method = RequestMethod.POST, produces = "application/json")
	public Map<String, EmailNotification> addEmailNotification(@RequestBody EmailNotificationSideload newEmailNotification) {		
		Map<String,EmailNotification> responseBody = new HashMap<String, EmailNotification>();
		responseBody.put("emailNotification", emailNotificationService.addEmailNotification(newEmailNotification.emailNotification));
		return responseBody;			
	}
}
