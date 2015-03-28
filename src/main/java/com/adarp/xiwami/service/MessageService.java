package com.adarp.xiwami.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adarp.xiwami.domain.Message;
import com.adarp.xiwami.repository.MessageRepository;

@Service
public class MessageService {

	@Autowired
	private MessageRepository repo;
	
	public List<Message> find(String fromId, String toId, String fromStatus, String toStatus, String queryText) {
		List<Message> list = repo.query(fromId, toId, fromStatus, toStatus, queryText);
		
		return list;
	}
	
	public Message findById(String id) {
		return repo.findByIdAndIsDestroyedIsFalse(id);
	}
	
	public Message add(Message newObj) {
		newObj.setIsDestroyed(false);
		return repo.save(newObj);
	}
	
	public Message update(String id, Message updatedObj) {
		updatedObj.setId(id);
		return repo.save(updatedObj);
	}
	
	public void delete(String id) {
		Message obj = repo.findOne(id);
		obj.setIsDestroyed(true);
		repo.save(obj);
	}
}


