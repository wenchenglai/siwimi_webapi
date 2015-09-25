package com.siwimi.webapi.service;

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

import com.siwimi.webapi.domain.Activity;
import com.siwimi.webapi.domain.Email;
import com.siwimi.webapi.domain.Feedback;
import com.siwimi.webapi.domain.Group;
import com.siwimi.webapi.domain.Member;
import com.siwimi.webapi.domain.Question;
import com.siwimi.webapi.domain.Tip;
import com.siwimi.webapi.repository.EmailRepository;
import com.siwimi.webapi.repository.FeedbackRepository;
import com.siwimi.webapi.repository.GroupRepository;
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
	
	@Autowired
	private GroupRepository groupRep;
	
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
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		Properties properties = new Properties();
		try {
			properties.load(classLoader.getResourceAsStream("notifyNewMemberToSiwimi.properties"));
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
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		Properties properties = new Properties();
		try {
			properties.load(classLoader.getResourceAsStream("notifyNewQuestionToSiwimi.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// subject and recipient of the emails
		String subject = properties.getProperty("subject");	
		Member asker = memberRep.queryExistingMember(newQuestion.getCreator());
		
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
		
		Member feedbackIssuer = new Member();
		if (newFeedback.getCreator() != null)
			feedbackIssuer = memberRep.queryExistingMember(newFeedback.getCreator());
		else {
			feedbackIssuer.setEmail(newFeedback.getSenderEmail());
		}
				
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
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			Properties properties = new Properties();
			try {
				properties.load(classLoader.getResourceAsStream("notifyNewFeedbackToSiwimi.properties"));
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
		Member asker = memberRep.queryExistingMember(newQuestion.getCreator());
		
		if (asker.getEmail() != null) {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			// subject and recipient of the emails
			Properties properties = new Properties();
			try {
				properties.load(classLoader.getResourceAsStream("notifyNewQuestionToAsker.properties"));
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
		
		Member feedbackIssuer = new Member();
		if (newFeedback.getCreator() != null)
			feedbackIssuer = memberRep.queryExistingMember(newFeedback.getCreator());
		else {
			feedbackIssuer.setEmail(newFeedback.getSenderEmail());
		}
		
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
				// It is impossible to have repliers for "feedback"
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
			} else
				// No need to send emails for activity and item
				return;
						
			// Sent email to the replier
			String answer = newFeedback.getDescription();			
			if ( ((title != null) || (description != null)) && (answer != null) && (!answer.isEmpty())) {
				ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
				Properties properties = new Properties();
				try {
					properties.load(classLoader.getResourceAsStream("notifyNewFeedbackToReplier.properties"));
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
		
		Member feedbackIssuer = new Member();
		if (newFeedback.getCreator() != null)
			feedbackIssuer = memberRep.queryExistingMember(newFeedback.getCreator());
		else {
			feedbackIssuer.setEmail(newFeedback.getSenderEmail());
		}
		
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
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			Properties properties = new Properties();
			try {
				properties.load(classLoader.getResourceAsStream("notifyNewFeedbackToAsker.properties"));
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
			// Important : question must have a creator
			asker = memberRep.queryExistingMember(question.getCreator());
			title = question.getTitle();
			description = question.getDescription();
		} else if (parentFeedback.getParentType().equals("tip")) {
			// Retrieve title, description, and original asker of Tip object
			Tip tip = tipRep.findByIdAndIsDeletedRecordIsFalse(parentFeedback.getParent());
			// Important : tip must have a creator
			asker = memberRep.queryExistingMember(tip.getCreator());
			title = tip.getTitle();
			description = tip.getDescription();
		} else
			// No need to send emails for activity and item
			return;
		
		String answer = newFeedback.getDescription();
		// Sent email to asker
		if ((asker.getEmail() != null) && (answer != null) && (!answer.isEmpty())) {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			Properties properties = new Properties();
			try {
				properties.load(classLoader.getResourceAsStream("notifyNewAnswerToAsker.properties"));
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
	public void notifyConfirmationToNewMember(Member newMember, Boolean isLocalhost) {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		Properties properties = new Properties();
		try {
			// This is for backend development at local machine purpose
			if (isLocalhost)
				properties.load(classLoader.getResourceAsStream("notifyConfirmationToNewMember_localhost.properties"));
			else
				properties.load(classLoader.getResourceAsStream("notifyConfirmationToNewMember.properties"));
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
	
	/** Email reset password to the existing member **/
	public Member resetEmail(String email, Boolean isLocalhost) {
		
		Member member = memberRep.queryExistingMember(email);
		
		if (member!=null) {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			Properties properties = new Properties();
			try {
				// This is for backend development at local machine purpose
				if (isLocalhost)
					properties.load(classLoader.getResourceAsStream("resetPasswordToNewMember_localhost.properties"));
				else
					properties.load(classLoader.getResourceAsStream("resetPasswordToNewMember.properties"));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			String subject = properties.getProperty("subject");
			String body = MessageFormat.format(properties.getProperty("body"),member.getId());
			List<String> sentTo = new ArrayList<String>();
			sentTo.add(member.getEmail());	
			
			Email notifyMember = new Email();
			notifyMember.setSentTo(sentTo);
			notifyMember.setSubject(subject);
			notifyMember.setEmailText(body);
			notifyMember.setSentTime(new Date());	
			
			sentEmail(notifyMember);
			addEmail(notifyMember);			
		}		
		
		return member;
	}		
	
	
	/** Invite new member by email **/
	public Member inviteByEmail(String email,Member existingMember, Boolean isLocalhost) {
		// This is for backend development at local machine purpose
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		Properties properties = new Properties();
		try {
			if (isLocalhost)
				properties.load(classLoader.getResourceAsStream("inviteNewMember_localhost.properties"));
			else
				properties.load(classLoader.getResourceAsStream("inviteNewMember.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
			
		String existingMemberName = null;
		// If the email sender has no name, don't send email.
		if (existingMember.getFirstName() != null) {
			existingMemberName = existingMember.getFirstName();
		}
		if (existingMember.getLastName() != null) {
			if (existingMemberName == null)
				existingMemberName = existingMember.getLastName();
			else
				existingMemberName = existingMemberName + " " +existingMember.getLastName();
		}
			
		Member newMember = memberRep.queryExistingMember(email);
		if (existingMemberName!=null) {			
			if (newMember == null) {
				newMember = new Member();
				newMember.setEmail(email);
				newMember.setInvitedBy(existingMember.getId());
				newMember = memberRep.save(newMember);
			}
			
			String subject = MessageFormat.format(properties.getProperty("subject"),existingMemberName);
			String body = MessageFormat.format(properties.getProperty("body"),existingMemberName,newMember.getId());
			List<String> sentTo = new ArrayList<String>();
			sentTo.add(email);	
			
			Email notifyMember = new Email();
			notifyMember.setSentTo(sentTo);
			notifyMember.setSubject(subject);
			notifyMember.setEmailText(body);
			notifyMember.setSentTime(new Date());	
				
			sentEmail(notifyMember);
			addEmail(notifyMember);				
		}
		
		return newMember;
	}		
	
	/** Notify events to friends in the same group **/
	public void notifyEvents(Activity activity,Member creator, Group group, Boolean isLocalhost) {
		// This is for backend development at local machine purpose
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		Properties properties = new Properties();
		try {
			if (isLocalhost)
				properties.load(classLoader.getResourceAsStream("notifyEventsToMember_localhost.properties"));
			else
				properties.load(classLoader.getResourceAsStream("notifyEventsToMember.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
			
		// if group is specified, ignore creator 
		String existingMemberName = null;
		if (group != null)
			creator = memberRep.queryExistingMember(group.getCreator());

		if (creator != null) {
			if (creator.getFirstName() != null) {
				existingMemberName = creator.getFirstName();
			}
			if (creator.getLastName() != null) {
				if (existingMemberName == null)
					existingMemberName = creator.getLastName();
				else
					existingMemberName = existingMemberName + " " +creator.getLastName();
			}
		}
			
		// If the email sender has no name, don't send email.
		if (existingMemberName!=null) {			
			// if groupId is specified, ignore userId 
			if (group != null) {
				List <String> groupMemberId = group.getMembers();
				for (String memberId : groupMemberId) {
					String subject = MessageFormat.format(properties.getProperty("subject"),existingMemberName);
					String body = MessageFormat.format(properties.getProperty("body"),existingMemberName);
					List<String> sentTo = new ArrayList<String>();
					String email = memberRep.queryExistingMember(memberId).getEmail();
					if ((email != null) && (!email.isEmpty())) {
						sentTo.add(email);							
						Email notifyEventtoMember = new Email();
						notifyEventtoMember.setSentTo(sentTo);
						notifyEventtoMember.setSubject(subject);
						notifyEventtoMember.setEmailText(body);
						notifyEventtoMember.setSentTime(new Date());								
						sentEmail(notifyEventtoMember);
						addEmail(notifyEventtoMember);	
					}
				}
			} else {
				List <Group> groups = groupRep.queryGroup(creator.getId(), null, null);
				for (Group myGroup : groups) {
					List <String> groupMemberId = myGroup.getMembers();
					for (String memberId : groupMemberId) {
						String subject = MessageFormat.format(properties.getProperty("subject"),existingMemberName);
						String body = MessageFormat.format(properties.getProperty("body"),existingMemberName);
						List<String> sentTo = new ArrayList<String>();
						String email = memberRep.queryExistingMember(memberId).getEmail();
						if ((email != null) && (!email.isEmpty())) {
							sentTo.add(email);							
							Email notifyEventtoMember = new Email();
							notifyEventtoMember.setSentTo(sentTo);
							notifyEventtoMember.setSubject(subject);
							notifyEventtoMember.setEmailText(body);
							notifyEventtoMember.setSentTime(new Date());								
							sentEmail(notifyEventtoMember);
							addEmail(notifyEventtoMember);	
						}
					}
				}
			}						
		}
	}		
	
	
}

