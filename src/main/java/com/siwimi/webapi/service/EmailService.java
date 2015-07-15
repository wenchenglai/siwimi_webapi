package com.siwimi.webapi.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.siwimi.webapi.domain.Email;
import com.siwimi.webapi.domain.Feedback;
import com.siwimi.webapi.domain.Member;
import com.siwimi.webapi.domain.Question;
import com.siwimi.webapi.domain.Tip;
import com.siwimi.webapi.repository.EmailRepository;
import com.siwimi.webapi.repository.FeedbackRepository;
import com.siwimi.webapi.repository.MemberRepository;
import com.siwimi.webapi.repository.QuestionRepository;
import com.siwimi.webapi.repository.TipRepository;
import com.siwimi.webapi.service.misc.Emailer;

@Service
public class EmailService {

	@Autowired
	private EmailRepository emailRep;
	
	@Autowired
	private MemberRepository memberRep;
	
	@Autowired
	private QuestionRepository questionRep;
	
	@Autowired
	private TipRepository tipRep;
	
	@Autowired
	private FeedbackRepository feedbackRep;
	
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
	
	/** Notify Siwimi :  new member is added **/
	public void notifyNewMemberToSiwimi(Member newMember) {	
		
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream("/usr/local/tomcat8/siwimi/notifyNewMemberToSiwimi.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
						
		String subject = properties.getProperty("subject");		
		String memberInfo = MessageFormat.format(properties.getProperty("memberInfo"), 
				                                 newMember.getFirstName(), newMember.getLastName(),
				                                 newMember.getEmail(),newMember.getFacebookId());
		List<String> sentTo = Arrays.asList(properties.getProperty("sendTo").toString().split(","));
		
		Email notifySiwimi = new Email();
		notifySiwimi.setSentTo(sentTo);
		notifySiwimi.setSubject(subject);
		notifySiwimi.setEmailText(memberInfo);
		notifySiwimi.setSentTime(new Date());
		
		sentEmail(notifySiwimi);
		addEmail(notifySiwimi);
	}
	
	/** Notify Siwimi :  new question is added **/
	public void notifyNewQuestionToSiwimi(Question newQuestion) {	
		
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream("/usr/local/tomcat8/siwimi/notifyNewQuestionToSiwimi.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// subject and recipient of the emails
		String subject = properties.getProperty("subject");	
		Member asker = memberRep.findByid(newQuestion.getCreator());
		
		// Sent email to Siwimi founders
		String questionInfo = MessageFormat.format(properties.getProperty("questionInfo"), 
				                                   asker.getFirstName(), asker.getLastName(), asker.getEmail(),asker.getFacebookId(),
				                                   newQuestion.getTitle(), newQuestion.getDescription());
		List<String> sentTo = Arrays.asList(properties.getProperty("sendTo").toString().split(","));
		
		Email notifySiwimi = new Email();
		notifySiwimi.setSentTo(sentTo);
		notifySiwimi.setSubject(subject);
		notifySiwimi.setEmailText(questionInfo);
		notifySiwimi.setSentTime(new Date());		
		sentEmail(notifySiwimi);
		addEmail(notifySiwimi);
	}
	
	/** Notify Siwimi :  new feedback is added **/
	public void notifyNewFeedbackToSiwimi(Feedback newFeedback) {	
		
		Member feedbackIssuer = memberRep.findByid(newFeedback.getCreator());	
		// For child comment : find its parent comment
		Feedback parentFeedback = new Feedback();
		if (newFeedback.getParentType() == null)
			parentFeedback = feedbackRep.findByIdAndIsDeletedRecordIsFalse(newFeedback.getParent());
		else 
			parentFeedback = newFeedback;	
		
		if (parentFeedback.getParentType() == null)
			// parentType of parent-feedback is null : not allowed
			return;
		else if (parentFeedback.getParentType().equals("feedback")) {			
			Properties properties = new Properties();
			try {
				properties.load(new FileInputStream("/usr/local/tomcat8/siwimi/notifyNewFeedbackToSiwimi.properties"));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			// subject and recipient of the emails
			String subject = properties.getProperty("subject");			
			// Sent email to Siwimi founders
			String feedbackInfo = MessageFormat.format(properties.getProperty("feedbackInfo"), 
					                                   feedbackIssuer.getFirstName(), feedbackIssuer.getLastName(),
					                                   feedbackIssuer.getEmail(), feedbackIssuer.getFacebookId(),
					                                   newFeedback.getDescription());
			
			List<String> sentTo = Arrays.asList(properties.getProperty("sendTo").toString().split(","));
			
			Email notifySiwimi = new Email();
			notifySiwimi.setSentTo(sentTo);
			notifySiwimi.setSubject(subject);
			notifySiwimi.setEmailText(feedbackInfo);
			notifySiwimi.setSentTime(new Date());		
			sentEmail(notifySiwimi);
			addEmail(notifySiwimi);
		}
		return;
	}
	
	/** Notify question asker **/
	public void notifyNewQuestionToAsker(Question newQuestion) {
		Member asker = memberRep.findByid(newQuestion.getCreator());
		
		if (asker.getEmail() != null) {
			// subject and recipient of the emails
			Properties properties = new Properties();
			try {
				properties.load(new FileInputStream("/usr/local/tomcat8/siwimi/notifyNewQuestionToAsker.properties"));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			String subject = properties.getProperty("subject");
			String body = MessageFormat.format(properties.getProperty("body"),newQuestion.getTitle(),newQuestion.getDescription());	        			
			List<String> recipent = new ArrayList<String> ();
			recipent.add(asker.getEmail());
			Email notifyAsker = new Email();
			notifyAsker.setSentTo(recipent);
			notifyAsker.setSubject(subject);
			notifyAsker.setEmailText(body);
			notifyAsker.setSentTime(new Date());		
			sentEmail(notifyAsker);
			addEmail(notifyAsker);	
		}
	}
	
	/** Notify answer to the replier **/
	public void notifyNewFeedbackToReplier(Feedback newFeedback) {
		Member feedbackIssuer = memberRep.findByid(newFeedback.getCreator());
		
		if (feedbackIssuer.getEmail() != null) {
			
			// For child comment : find its parent comment
			Feedback parentFeedback = new Feedback();
			if (newFeedback.getParentType() == null)
				parentFeedback = feedbackRep.findByIdAndIsDeletedRecordIsFalse(newFeedback.getParent());
			else 
				parentFeedback = newFeedback;		
							
			String title = null;
			String description = null;
			if (parentFeedback.getParentType() == null)
				// parentType of parent-feedback is null : not allowed
				return;
			else if (parentFeedback.getParentType().equals("feedback")) {
				return;
			}
			else if (parentFeedback.getParentType().equals("question")) {
				// Retrieve title and description of Question object
				Question question = questionRep.findByIdAndIsDeletedRecordIsFalse(parentFeedback.getParent());
				title = question.getTitle();
				description = question.getDescription();
			}
			else if (parentFeedback.getParentType().equals("tip")) {
				// Retrieve title and description of Tip object
				Tip tip = tipRep.findByIdAndIsDeletedRecordIsFalse(parentFeedback.getParent());
				title = tip.getTitle();
				description = tip.getDescription();
			}				
						
			// Sent email to the replier
			String answer = newFeedback.getDescription();			
			if ( ((title != null) || (description != null)) && (answer != null) && (!answer.isEmpty())) {
				Properties properties = new Properties();
				try {
					properties.load(new FileInputStream("/usr/local/tomcat8/siwimi/notifyNewFeedbackToReplier.properties"));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				String subject = properties.getProperty("subject");
				String body = MessageFormat.format(properties.getProperty("body"),title,description,answer);	        			
				List<String> recipent = new ArrayList<String> ();
				recipent.add(feedbackIssuer.getEmail());
				Email notifyReplier = new Email();
				notifyReplier.setSentTo(recipent);
				notifyReplier.setSubject(subject);
				notifyReplier.setEmailText(body);
				notifyReplier.setSentTime(new Date());		
				sentEmail(notifyReplier);
				addEmail(notifyReplier);
			}	
		}		
	}
	
	/** Notify answer to the question asker **/
	public void notifyNewFeedbackToAsker(Feedback newFeedback) {
		Member feedbackIssuer = memberRep.findByid(newFeedback.getCreator());
		
		// For child comment : find its parent comment
		Feedback parentFeedback = new Feedback();
		if (newFeedback.getParentType() == null)
			parentFeedback = feedbackRep.findByIdAndIsDeletedRecordIsFalse(newFeedback.getParent());
		else 
			parentFeedback = newFeedback;
		
		// Fill in asker, title, and description from Question or Tip object
		Member asker = null;
		String title = null;
		String description = null;

		if (parentFeedback.getParentType() == null)
			// parentType of parent-feedback is null : not allowed
			return;
		else if (parentFeedback.getParentType().equals("feedback")) {
			Properties properties = new Properties();
			try {
				properties.load(new FileInputStream("/usr/local/tomcat8/siwimi/notifyNewFeedbackToAsker.properties"));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}			
			String subject = properties.getProperty("subject");
			String body = properties.getProperty("body");	        			
			List<String> recipent = new ArrayList<String> ();
			recipent.add(feedbackIssuer.getEmail());
			Email notifyReplier = new Email();
			notifyReplier.setSentTo(recipent);
			notifyReplier.setSubject(subject);
			notifyReplier.setEmailText(body);
			notifyReplier.setSentTime(new Date());		
			sentEmail(notifyReplier);
			addEmail(notifyReplier);
			return;
		} else if (parentFeedback.getParentType().equals("question")) {
			// Retrieve title, description, and original asker of Question object
			Question question = questionRep.findByIdAndIsDeletedRecordIsFalse(parentFeedback.getParent());
			asker = memberRep.findByid(question.getCreator());
			title = question.getTitle();
			description = question.getDescription();
		} else if (parentFeedback.getParentType().equals("tip")) {
			// Retrieve title, description, and original asker of Tip object
			Tip tip = tipRep.findByIdAndIsDeletedRecordIsFalse(parentFeedback.getParent());
			asker = memberRep.findByid(tip.getCreator());
			title = tip.getTitle();
			description = tip.getDescription();
		}
		
		String answer = newFeedback.getDescription();
		// Sent email to asker
		if ((asker.getEmail() != null) && (answer != null) && (!answer.isEmpty())) {
			Properties properties = new Properties();
			try {
				properties.load(new FileInputStream("/usr/local/tomcat8/siwimi/notifyNewAnswerToAsker.properties"));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			String subject = properties.getProperty("subject");
			String body = MessageFormat.format(properties.getProperty("body"),title,description,answer);	
			List<String> recipent = new ArrayList<String> ();
			recipent.add(asker.getEmail());
			Email notifyAsker = new Email();
			notifyAsker.setSentTo(recipent);
			notifyAsker.setSubject(subject);
			notifyAsker.setEmailText(body);
			notifyAsker.setSentTime(new Date());		
			sentEmail(notifyAsker);
			addEmail(notifyAsker);	
		}		
	}		
	
	/** Email confirmation to the new member **/
	public void notifyConfirmationToNewMember(Member newMember) {	
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream("/usr/local/tomcat8/siwimi/notifyConfirmationToNewMember.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String subject = properties.getProperty("subject");
		String body = MessageFormat.format(properties.getProperty("body"),newMember.getId());
		List<String> sentTo = new ArrayList<String>();
		sentTo.add(newMember.getEmail());	
		
		Email notifyMember = new Email();
		notifyMember.setSentTo(sentTo);
		notifyMember.setSubject(subject);
		notifyMember.setEmailText(body);
		notifyMember.setSentTime(new Date());
		
		sentEmail(notifyMember);
		addEmail(notifyMember);
	}
}
