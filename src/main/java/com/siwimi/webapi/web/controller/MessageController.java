package com.siwimi.webapi.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.siwimi.webapi.domain.Member;
import com.siwimi.webapi.domain.Message;
import com.siwimi.webapi.service.MemberService;
import com.siwimi.webapi.service.MessageService;
import com.siwimi.webapi.web.dto.MessageSideload;
import com.siwimi.webapi.web.dto.MessageSideloadList;

@RestController
public class MessageController {

	@Autowired
	private MessageService service;
	
	@Autowired
	private MemberService memberService;
	
	// Get by queryString
	@RequestMapping(value = "/messages", method = RequestMethod.GET, produces = "application/json")
	public  MessageSideloadList find(
			@RequestParam(value="from", required=false) String fromId,
			@RequestParam(value="to", required=false) String toId,	
			@RequestParam(value="fromStatus", required=false) String fromStatus,
			@RequestParam(value="toStatus", required=false) String toStatus,			
			@RequestParam(value="queryText", required=false) String queryText,
			@RequestParam(value="sort", required=false) String sort) {
		
		MessageSideloadList responseBody = new MessageSideloadList();
		
		List<Message> messages = null;
		List<Member> members = new ArrayList<Member>();
		try {
			messages = service.find(fromId, toId, fromStatus, toStatus, queryText,sort);
			
			if (messages.size() > 0) {
				Member fromMember = memberService.findByMemberId(messages.get(0).getFrom());
				members.add(fromMember);
				for (Message msg:messages) {
					if (msg.getTo() != null) {
						Member toMember = memberService.findByMemberId(msg.getTo());
						members.add(toMember);
					}
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
	public Map<String, Message> findById(@PathVariable("id") String id) {		
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
	
	// Update
	@RequestMapping(value = "/messages/{id}", method = RequestMethod.PUT, produces = "application/json")
	public Map<String,Message> update(@PathVariable("id") String id, @RequestBody MessageSideload updatedObj) {
		Message msg = service.update(id, updatedObj.message);		
		Map<String,Message> responseBody = new HashMap<String,Message>();
		responseBody.put("message", msg);
		return responseBody;		
	}	
	
	// Delete
	@RequestMapping (value = "/messages/{id}", method = RequestMethod.DELETE, produces = "application/json")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id")String id) {
		service.delete(id);
	}	
}
