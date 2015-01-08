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

import com.adarp.xiwami.domain.Message;
import com.adarp.xiwami.service.MessageService;
import com.adarp.xiwami.web.dto.MessageSideload;

@RestController
public class MessageController {

	@Autowired
	private MessageService service;
	
	// Get discussions by type
	@RequestMapping(value = "/messages", method = RequestMethod.GET, produces = "application/json")
	public Map<String, List<Message>> Find(
			@RequestParam(value="from", required=false) String fromId,
			@RequestParam(value="to", required=false) String toId,	
			@RequestParam(value="fromStatus", required=false) String fromStatus,
			@RequestParam(value="toStatus", required=false) String toStatus,			
			@RequestParam(value="queryText", required=false) String queryText) {
		
		Map<String, List<Message>> responseBody = new HashMap<String, List<Message>>();
		
		List<Message> list = null;
		try {
			list = service.find(fromId, toId, fromStatus, toStatus, queryText);
		} catch (Exception err) {
			// we must return an empty array so Ember can pick up the json data format.  Return null will crash the ember client.
			list = new ArrayList<Message>();

		}
		responseBody.put("messages", list);
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
