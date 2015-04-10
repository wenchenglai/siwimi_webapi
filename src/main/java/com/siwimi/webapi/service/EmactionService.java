package com.siwimi.webapi.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.siwimi.webapi.domain.Emaction;
import com.siwimi.webapi.repository.EmactionRepository;

@Service
public class EmactionService {

	@Autowired
	private EmactionRepository emactionRep;
	
	public List<Emaction> findEmactions(String event) {
		return emactionRep.queryEmaction(event);
	}
	
	public Emaction findByEmactionId(String id) {
		return emactionRep.findOne(id);
	}
	
	public Emaction save(Emaction obj) {				
		return emactionRep.save(obj);
	}	
	
	public Emaction updateEmaction(Emaction updatedEmaction) {
		Emaction emaction = emactionRep.findByMemberAndEvent(updatedEmaction.getMember(), updatedEmaction.getEvent());
		if (emaction != null) {
			// update the existing emation
			String id = emaction.getId();
			if ((id != null) && !id.isEmpty())
				updatedEmaction.setId(id);
		}
		// update lastUpdateDate : now
		updatedEmaction.setLastUpdateDate(new Date());
		// Save to database
		return emactionRep.save(updatedEmaction);
	}
	
	public void deleteEmaction(String id) {
		emactionRep.delete(id);
	}
}
