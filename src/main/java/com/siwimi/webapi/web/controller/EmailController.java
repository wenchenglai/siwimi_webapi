package com.siwimi.webapi.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.siwimi.webapi.domain.Activity;
import com.siwimi.webapi.domain.Group;
import com.siwimi.webapi.domain.Member;
import com.siwimi.webapi.exception.ExistingEntityException;
import com.siwimi.webapi.service.ActivityService;
import com.siwimi.webapi.service.EmailService;
import com.siwimi.webapi.service.GroupService;
import com.siwimi.webapi.service.MemberService;

@RestController
public class EmailController {
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private MemberService memberService;

	@Autowired
	private GroupService groupService;
	
	@Autowired
	private ActivityService activityService;
	
    @Autowired
    private HttpServletRequest httpServletRequest;
    
	// Get Email by ID
	@RequestMapping(value = "/email/sendConfirmation", method = RequestMethod.GET, produces = "application/json")
	public Map<String, Member> sendConfirmation(@RequestParam(value="id", required=true) String id) {
		Member member = null;
		if (id != null)
			if (!id.isEmpty())
				member = memberService.findByMemberId(id);
		Map<String, Member> responseBody = new HashMap<String, Member>();
		if (member == null)
			throw new ExistingEntityException("Unable to find this member!");
		else {
			responseBody.put("member", member);
			// This is for backend development at local machine purpose
			String serverName = this.httpServletRequest.getServerName();
			if (serverName != null) {
				emailService.notifyConfirmationToNewMember(member,serverName.toLowerCase().contains("localhost"));
			} else
				emailService.notifyConfirmationToNewMember(member,false);
		}		
		return responseBody;
	}

	// Resent Email
	@RequestMapping(value = "/email/forgetpassword", method = RequestMethod.GET, produces = "application/json")
	public Map<String, Member> resetEmail(@RequestParam(value="email", required=true) String email) {
		Map<String, Member> responseBody = new HashMap<String, Member>();
		Member member = null;
		// This is for backend development at local machine purpose
		String serverName = this.httpServletRequest.getServerName();
		if (serverName != null) {
			member = emailService.resetEmail(email,serverName.toLowerCase().contains("localhost"));
		} else
			member = emailService.resetEmail(email,false);

		if (member == null)
			throw new ExistingEntityException("Unable to match this email to any existing members!");
		
		responseBody.put("member", member);		
		return responseBody;
	}
	
	// Invite friends by Email
	@RequestMapping(value = "/email/invite", method = RequestMethod.GET, produces = "application/json")
	public Map<String, Member> invite(@RequestParam(value="email", required=true) String email,
									  @RequestParam(value="userid", required=true) String userId,
									  @RequestParam(value="groupid", required=true) String groupId) {
		Map<String, Member> responseBody = new HashMap<String, Member>();
		Member existingMember = null;
		if (userId != null)
			if (!userId.isEmpty())
				existingMember = memberService.findByMemberId(userId);
		if (existingMember == null)
			throw new ExistingEntityException("This member does not exist in the database!");
		
		Member newMember = null;
		// This is for backend development at local machine purpose
		String serverName = this.httpServletRequest.getServerName();	
		boolean isLocalhost = serverName == null ? false : serverName.toLowerCase().contains("localhost");
		// send email to friends, and create member for this new friend.
		newMember = emailService.inviteByEmail(email,existingMember,isLocalhost);
		// add this new friend into creator's group
		if (newMember!=null) {
			groupService.addMemberIntoGroup(groupId,newMember.getId());
		}
		
		responseBody.put("member", newMember);		
		return responseBody;
	}
	
	// Notify friends on events
	@RequestMapping(value = "/email/notify-events", method = RequestMethod.GET, produces = "application/json")
	public Map<String, Member> notifyActivity(@RequestParam(value="eventId", required=true) String eventId,
									          @RequestParam(value="userId", required=true) String userId,
									          @RequestParam(value="groupId", required=false) String[] groupId) {
		Map<String, Member> responseBody = new HashMap<String, Member>();
		
		if ((userId == null) && (groupId == null))
			throw new ExistingEntityException("User ID and event ID are both null!");
		
		if (userId.isEmpty() && (groupId.length == 0))
			throw new ExistingEntityException("User ID and event ID are both missing!");
		
		Activity existingActivity = null;
		if (eventId != null) {
			if (!eventId.isEmpty()) {
				existingActivity = activityService.findByActivityId(eventId);
				if (existingActivity == null)
					throw new ExistingEntityException("This event " + eventId + " does not exist in the database!");
			}
		}
		
		Member existingMember = null;
		if (userId != null) {
			if (!userId.isEmpty()) {
				existingMember = memberService.findByMemberId(userId);
				if (existingMember == null)
					throw new ExistingEntityException("This member " +  userId + " does not exist in the database!");	
			}
		}
	
		List<Group> existingGroups = null;
		if (groupId != null) {
			if (groupId.length > 0) {
				existingGroups = new ArrayList<Group>();
				for (int i=0; i<groupId.length; i++) {
					Group existingGroup = groupService.findByGroupId(groupId[i]);
					if (existingGroup == null)
						throw new ExistingEntityException("This group " + groupId[i] + " does not exist in the database!");
					if (!existingGroup.getCreator().equals(userId))
						throw new ExistingEntityException("This group " + groupId[i] + " does not belong to this creator " 
					                                      + userId + " !");
					existingGroups.add(existingGroup);
				}
			}
		}
		
		// This is for backend development at local machine purpose
		String serverName = this.httpServletRequest.getServerName();	
		boolean isLocalhost = serverName == null ? false : serverName.toLowerCase().contains("localhost");
		// send email to notify friends about this new event
		emailService.notifyEvents(existingActivity,existingMember,existingGroups,isLocalhost);
		
		responseBody.put("member", existingMember);		
		return responseBody;
	}
	
	// Exception handler
	@ExceptionHandler(ExistingEntityException.class)
	public Map<String, String> handleExistingEntityHandler(ExistingEntityException ex, HttpServletResponse res) {
		Map<String, String> responseBody = new HashMap<String, String>();
		responseBody.put("error",ex.getErrMsg());
		res.setStatus(422);
		return responseBody;
	}
	
}
