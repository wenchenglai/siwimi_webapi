package com.siwimi.webapi.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.siwimi.webapi.domain.Group;
import com.siwimi.webapi.domain.Member;
import com.siwimi.webapi.service.GroupService;
import com.siwimi.webapi.service.MemberService;
import com.siwimi.webapi.web.dto.GroupSideload;
import com.siwimi.webapi.web.dto.GroupSideloadList;

@RestController
public class GroupController {
	
	@Autowired
	private GroupService groupService;
	
	@Autowired
	private MemberService memberService;
		
	// Get all groups
	@RequestMapping(value = "/groups", method = RequestMethod.GET, produces = "application/json")
	public GroupSideloadList findGroups(
			@RequestParam(value="creator", required=false) String creatorId,
			@RequestParam(value="memberId", required=false) String groupMemberId,
			@RequestParam(value="queryText", required=false) String queryText) {
		
		GroupSideloadList responseBody = new GroupSideloadList();
		List<Group> groupList = groupService.findGroups(creatorId,groupMemberId,queryText);
		Set<Member> members = new HashSet<Member>();
		if (groupList!=null) {
			for (Group group : groupList) {
				List<String> memberIds = group.getMembers();
				for (String memberId : memberIds) {
					Member member = memberService.findByMemberId(memberId);
					// we must return an empty object so Ember can pick up the json data format.  Return null will crash the ember client.
					if (member!=null)
						members.add(member);	
				}
			}
		} else {
			// we must return an empty array so Ember can pick up the json data format.  Return null will crash the ember client.
			groupList = new ArrayList<Group>();
		}
		responseBody.groups = groupList;
		responseBody.members = new ArrayList<Member>(members);

		return responseBody;
	}

	// Get Group by ID
	@RequestMapping(value = "/groups/{id}", method = RequestMethod.GET, produces = "application/json")
	public Map<String,Group> findByGroupId(@PathVariable("id") String id) {
		Map<String,Group> responseBody = new HashMap<String,Group>();			
		Group group = groupService.findByGroupId(id);
		responseBody.put("group", group);
		return responseBody;
	}
	
	// Add New Group
	@RequestMapping(value = "/groups", method = RequestMethod.POST, produces = "application/json")
	public Map<String, Group> addGroup(@RequestBody GroupSideload newGroup) {
		Group savedGroup = groupService.addGroup(newGroup.group);		
		Map<String,Group> responseBody = new HashMap<String, Group>();
		responseBody.put("group", savedGroup);
		return responseBody;			
	}	
	
	// Update Group
	@RequestMapping(value = "/groups/{id}", method = RequestMethod.PUT, produces = "application/json")
	public Map<String, Group> updateGroup(@PathVariable("id") String id, @RequestBody GroupSideload updatedGroup){
		Group savedGroup = groupService.updateGroup(id, updatedGroup.group);		
		Map<String,Group> responseBody = new HashMap<String, Group>();
		responseBody.put("group", savedGroup);
		return responseBody;			
	}
	
	// Delete Group
	@RequestMapping (value = "/groups/{id}", method = RequestMethod.DELETE, produces = "application/json")
	public void deleteGroup(@PathVariable("id")String id) {
		groupService.deleteGroup(id);
	}	
}
