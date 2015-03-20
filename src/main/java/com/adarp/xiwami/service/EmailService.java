package com.adarp.xiwami.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adarp.xiwami.domain.Email;
import com.adarp.xiwami.repository.EmailRepository;
import com.adarp.xiwami.service.misc.Emailer;

@Service
public class EmailService {

	@Autowired
	EmailRepository emailRep;
	
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
		return emailRep.findByIdAndIsDestroyedIsFalse(id);
	}
	
	public Email addEmail(Email newEmail) {
		newEmail.setIsDestroyed(false);
		return emailRep.save(newEmail);
	}
	
	public Email updateEmail(String id, Email updatedEmail) {
		updatedEmail.setId(id);
		return emailRep.save(updatedEmail);
	}
	
	public void deleteEmail(String id) {
		Email email = emailRep.findOne(id);
		email.setIsDestroyed(true);
		emailRep.save(email);
	}
	
}
