package com.adarp.xiwami.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.adarp.xiwami.domain.Family;
import com.adarp.xiwami.domain.Member;
import com.adarp.xiwami.domain.Message;
import com.adarp.xiwami.service.MemberService;
import com.adarp.xiwami.service.MessageService;
import com.adarp.xiwami.web.dto.MessageSideloadList;
import com.adarp.xiwami.web.dto.MessageSideload;

@RestController
public class MessageController {

	@Autowired
	private MessageService service;
	
	@Autowired
	private MemberService memberService;
	
	// Get by queryString
	@RequestMapping(value = "/messages", method = RequestMethod.GET, produces = "application/json")
	public  MessageSideloadList Find(
			@RequestParam(value="from", required=false) String fromId,
			@RequestParam(value="to", required=false) String toId,	
			@RequestParam(value="fromStatus", required=false) String fromStatus,
			@RequestParam(value="toStatus", required=false) String toStatus,			
			@RequestParam(value="queryText", required=false) String queryText) {
		
		MessageSideloadList responseBody = new MessageSideloadList();
		
		List<Message> messages = null;
		List<Member> members = new ArrayList<Member>();
		try {
			messages = service.find(fromId, toId, fromStatus, toStatus, queryText);
			
			if (messages.size() > 0) {
				Member fromMember = memberService.findByMemberId(messages.get(0).getFrom());
				members.add(fromMember);
				for (Message msg:messages) {
					Member toMember = memberService.findByMemberId(msg.getTo());
					members.add(toMember);
				}
			}			
			
		} catch (Exception err) {
			// we must return an empty array so Ember can pick up the json data format.  Return null will crash the ember client.
			messages = new ArrayList<Message>();
		}
		responseBody.messages = messages;
		responseBody.members = members;
		return responseBody;
	}
	
	// Get by ID
	@RequestMapping(value = "/messages/{id}", method = RequestMethod.GET, produces = "application/json")
	public Map<String, Message> FindById(@PathVariable("id") String id) {		
		Map<String, Message> responseBody = new HashMap<String, Message>();			
		Message obj = service.findById(id);
		responseBody.put("message", obj);
		return responseBody;
	}
	
	// Add New
	@RequestMapping(value = "/messages", method = RequestMethod.POST, produces = "application/json")
	public Map<String, Message> add(@RequestBody MessageSideload newObj){
		Message savedObj = service.add(newObj.message);			
		Map<String, Message> responseBody = new HashMap<String, Message>();
		responseBody.put("message", savedObj);
		return responseBody;
	}	
	
	// Delete
	@RequestMapping (value = "/messages/{id}", method = RequestMethod.DELETE, produces = "application/json")
	public void delete(@PathVariable("id")String id) {
		service.delete(id);
	}	
}
