package com.siwimi.webapi.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
		String subject = "New Siwimi Member is added";
		String memberInfo = "Name : " + newMember.getFirstName() + " " + newMember.getLastName() + " \n" +
                            "Email : " + newMember.getEmail() + " \n" +
                            "FacebookId : " + newMember.getFacebookId();
		
		List<String> sentTo = new ArrayList<String>();
		sentTo.add("walay133@yahoo.com.tw");	
		sentTo.add("wenchenglai@gmail.com");
		
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
		// subject and recipient of the emails
		String subject = "New Siwimi Question is added";
		Member asker = memberRep.findByid(newQuestion.getCreator());
		
		// Sent email to Siwimi founders
		String questionInfo = "Creator : " + asker.getFirstName() + " " + asker.getLastName() + " \n " +
                              "Creator's email : " + asker.getEmail() + " \n " +
                              "Creator's facebookId : " + asker.getFacebookId() + " \n " +
                              "Title : " + newQuestion.getTitle() + " \n " +
                              "Description : " + newQuestion.getDescription();
		List<String> sentTo = new ArrayList<String>();
		sentTo.add("walay133@yahoo.com.tw");	
		sentTo.add("wenchenglai@gmail.com");
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
			// subject and recipient of the emails
			String subject = "New Siwimi Feedback is added";			
			// Sent email to Siwimi founders
			String questionInfo = "Creator : " + feedbackIssuer.getFirstName() + " " + feedbackIssuer.getLastName() + " \n " +
	                              "Creator's email : " + feedbackIssuer.getEmail() + " \n " +
	                              "Creator's facebookId : " + feedbackIssuer.getFacebookId() + " \n " +
	                              "Feedback description : " + newFeedback.getDescription();
			List<String> sentTo = new ArrayList<String>();
			sentTo.add("walay133@yahoo.com.tw");	
			sentTo.add("wenchenglai@gmail.com");
			Email notifySiwimi = new Email();
			notifySiwimi.setSentTo(sentTo);
			notifySiwimi.setSubject(subject);
			notifySiwimi.setEmailText(questionInfo);
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
			String subject = "Your question is added at Siwimi.com";
			String body = "You've asked a question on Siwimi.com. " +
		                  "We'll send this question to parents who live in your neighborhood.\n\n " +
					      "Your question is : \n " +
                          "Title -- " + newQuestion.getTitle() + "\n " +
                          "Description -- " + newQuestion.getDescription() + "\n\n\n " + 
                          "Best Regards," + "\n " + 
                          "The Siwimi Team";	        			
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
				String subject = "Thank you for helping others!";
				String body = "Thank your for your help!  You've replied to a question posted by a local parent. " +
			                  "We've notified the parent about your reply.\n\n " +
					          "The question that you answer is :\n " +
		                      "Title -- " + title + "\n " +
		                      "Description -- " + description + "\n\n " + 
		                      "Your answer is :\n" + answer + "\n\n " +
		                      "Best Regards," + "\n " + 
		                      "The Siwimi Team";	        			
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
			String subject = "Thank you for your feedback!";
			String body = "Thank you for your suggestions on Siwimi.com. " +
			              "We take your opinions very seriously. " +
					      "Your valuable feedback will help us to provide better service to you. \n\n " +
	                      "Best Regards," + "\n " + 
	                      "The Siwimi Team";	        			
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
			String subject = "Someone replies your question on Siwimi.com!";
			String body = "Someone has answered the question that you've posted on Siwimi.com.\n\n " +
				          "The question that you ask is :\n " +
                          "Title -- " + title + "\n " +
                          "Description -- " + description + "\n\n " + 
                          "The answer is :\n " + answer + "\n\n " +
                          "Best Regards," + "\n " + 
                          "The Siwimi Team";	
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
		String subject = "Siwimi sign-up confirmation is needed";
		String body = "You've signed up a new a account at siwimi.com.\n\n " +
		              "Please click on the link below : http://www.siwimi.com/#/member/browse?id=" + newMember.getId() +
		              "&action=confirm to confirm your sign up process.\n " +
		              "If you didn't sign up, please ignore this email.\n\n " +
                      "Best Regards," + "\n " + 
                      "The Siwimi Team";
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
