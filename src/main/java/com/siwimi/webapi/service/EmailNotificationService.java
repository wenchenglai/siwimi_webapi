package com.siwimi.webapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.siwimi.webapi.domain.EmailNotification;
import com.siwimi.webapi.repository.EmailNotificationRepository;

@Service
public class EmailNotificationService {
	
	@Autowired
	private EmailNotificationRepository emailNotificationRep;
	
	public EmailNotification findByCreator(String creatorId) {
		return emailNotificationRep.findByCreatorAndIsDeletedRecordIsFalse(creatorId);
	}
	
	public EmailNotification updateEmailNotification(String creatorId, EmailNotification updatedEmailNotification){
		EmailNotification emailNotification = emailNotificationRep.findByCreatorAndIsDeletedRecordIsFalse(creatorId);
		if (emailNotification!=null) {
			updatedEmailNotification.setId(emailNotification.getId());
			return emailNotificationRep.save(updatedEmailNotification);
		} else
			return null; // unable to find the old emailNotification
	}
	
	public EmailNotification addEmailNotification(EmailNotification newEmailNotification){
		newEmailNotification.setIsDeletedRecord(false);
		return emailNotificationRep.save(newEmailNotification);
	}
}
