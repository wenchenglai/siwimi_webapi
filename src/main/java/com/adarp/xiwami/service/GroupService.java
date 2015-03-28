package com.adarp.xiwami.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adarp.xiwami.domain.Group;
import com.adarp.xiwami.repository.GroupRepository;

@Service
public class GroupService {
	
	@Autowired
	private GroupRepository groupRep;
	
	public List<Group> findGroups(String creatorId,String queryText) {
		return groupRep.queryGroup(creatorId, queryText);
	}
		
	public Group findByGroupId(String id) {
		return groupRep.findByIdAndIsDestroyedIsFalse(id);
	}
	
	public Group addGroup(Group newGroup) {
		newGroup.setIsDestroyed(false);
		// add creator into the member list
		List<String> members = new ArrayList<String>(); 
		members.add(newGroup.getCreator());
		newGroup.setMembers(members);
		
		return groupRep.save(newGroup);
	}
	
	public Group updateGroup(String id, Group updatedGroup) {
		updatedGroup.setId(id);
		return groupRep.save(updatedGroup);
	}
	
	public void deleteGroup(String id) {
		Group group = groupRep.findOne(id);
		group.setIsDestroyed(true);
		groupRep.save(group);
	}
}
