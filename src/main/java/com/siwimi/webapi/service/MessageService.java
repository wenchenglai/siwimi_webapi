package com.siwimi.webapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.siwimi.webapi.domain.Message;
import com.siwimi.webapi.repository.MessageRepository;

@Service
public class MessageService {

	@Autowired
	private MessageRepository repo;
	
	public List<Message> find(String fromId, String toId, String fromStatus, String toStatus, String queryText, String sort) {
		List<Message> list = repo.query(fromId, toId, fromStatus, toStatus, queryText, sort);
		
		return list;
	}
	
	public Message findById(String id) {
		return repo.findByIdAndIsDeletedRecordIsFalse(id);
	}
	
	public Message add(Message newObj) {
		newObj.setIsDeletedRecord(false);
		return repo.save(newObj);
	}
	
	public Message update(String id, Message updatedObj) {
		updatedObj.setId(id);
		return repo.save(updatedObj);
	}
	
	public void delete(String id) {
		Message obj = repo.findOne(id);
		obj.setIsDeletedRecord(true);
		repo.save(obj);
	}
}


