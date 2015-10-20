package com.siwimi.webapi.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.siwimi.webapi.domain.Group;
import com.siwimi.webapi.repository.GroupRepository;

@Service
public class GroupService {
	
	@Autowired
	private GroupRepository groupRep;
	
	public List<Group> findGroups(String creatorId,String memberId, String queryText) {
		return groupRep.queryGroup(creatorId, memberId, queryText);
	}
		
	public Group findByGroupId(String id) {
		return groupRep.findByIdAndIsDeletedRecordIsFalse(id);
	}
	
	public void addMemberIntoGroup(String groupId, String memberId) {
		Group group = groupRep.findByIdAndIsDeletedRecordIsFalse(groupId);
		if (group!=null) {
			// cannot add duplicated members in the group
			Set<String> members = new HashSet<String>(group.getMembers());
			members.add(memberId);
			group.setMembers(new ArrayList<String>(members));	
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
	
	public Group deleteGroup(String id) {
		Group group = groupRep.findOne(id);
		if (group == null) {
			return null;
		} else if (!group.getIsDeletedRecord()) {
			group.setIsDeletedRecord(true);
			return groupRep.save(group);
		} else
			return null;
	}
}
