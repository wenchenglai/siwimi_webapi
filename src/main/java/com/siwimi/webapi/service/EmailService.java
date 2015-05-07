package com.siwimi.webapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.siwimi.webapi.domain.Email;
import com.siwimi.webapi.repository.EmailRepository;
import com.siwimi.webapi.service.misc.Emailer;

@Service
public class EmailService {

	@Autowired
	private EmailRepository emailRep;
	
	public Email sentEmail(Email newEmail) {
		Emailer sendEmail = new Emailer(newEmail);
		try {
			sendEmail.send();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newEmail;
	}
	
	public Email findByEmailId(String id) {
		return emailRep.findByIdAndIsDeletedRecordIsFalse(id);
	}
	
	public Email addEmail(Email newEmail) {
		newEmail.setIsDeletedRecord(false);
		return emailRep.save(newEmail);
	}
	
	public Email updateEmail(String id, Email updatedEmail) {
		updatedEmail.setId(id);
		return emailRep.save(updatedEmail);
	}
	
	public void deleteEmail(String id) {
		Email email = emailRep.findOne(id);
		email.setIsDeletedRecord(true);
		emailRep.save(email);
	}
	
}
