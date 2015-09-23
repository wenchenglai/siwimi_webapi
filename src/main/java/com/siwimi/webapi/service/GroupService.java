package com.siwimi.webapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.siwimi.webapi.domain.Group;
import com.siwimi.webapi.repository.GroupRepository;

@Service
public class GroupService {
	
	@Autowired
	private GroupRepository groupRep;
	
	public List<Group> findGroups(String creatorId,String queryText) {
		return groupRep.queryGroup(creatorId, queryText);
	}
		
	public Group findByGroupId(String id) {
		return groupRep.findByIdAndIsDeletedRecordIsFalse(id);
	}
	
	public void addMemberIntoGroup(String groupId, String memberId) {
		Group group = groupRep.findByIdAndIsDeletedRecordIsFalse(groupId);
		if (group!=null) {
			List<String> members = group.getMembers();
			members.add(memberId);
			//group.setMembers(members);	
			groupRep.save(group);
		}
	}
	
	public Group addGroup(Group newGroup) {
		newGroup.setIsDeletedRecord(false);
		// add creator into the member list
		List<String> members = newGroup.getMembers();
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
		group.setIsDeletedRecord(true);
		groupRep.save(group);
	}
}
