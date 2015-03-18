package com.adarp.xiwami.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adarp.xiwami.domain.Email;
import com.adarp.xiwami.repository.EmailRepository;
import com.adarp.xiwami.service.misc.SendGridHandler;

@Service
public class EmailService {

	@Autowired
	EmailRepository emailRep;
	
	public Email addEmail(Email newEmail) {
		List<String> recipients = newEmail.getSentTo();
		for (String recipient : recipients) {
			SendGridHandler sendEmail = new SendGridHandler(newEmail.getSentFrom(),recipient,newEmail.getSubject(),newEmail.getEmailText());
			//sendEmail.doPost(request, response);
		}
		return null;
	}
}
