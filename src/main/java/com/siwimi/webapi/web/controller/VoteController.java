package com.siwimi.webapi.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

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
import com.siwimi.webapi.domain.Vote;
import com.siwimi.webapi.service.MemberService;
import com.siwimi.webapi.service.VoteService;
import com.siwimi.webapi.web.dto.VoteSideload;
import com.siwimi.webapi.web.dto.VoteSideloadList;

@RestController
public class VoteController {

	@Autowired
	private VoteService voteService;
	
	@Autowired
	private MemberService memberService;
	
	// Get Vote by ID
	@RequestMapping(value = "/votes/{id}", method = RequestMethod.GET, produces = "application/json")
	public Map<String,Vote> findByVoteId(@PathVariable("id") String id) {
		Map<String,Vote> responseBody = new HashMap<String,Vote>();			
		Vote vote = voteService.findByVoteId(id);
		responseBody.put("vote", vote);
		return responseBody;
	}
	
	// Query Votes
	@RequestMapping(value = "/votes", method = RequestMethod.GET, produces = "application/json")
	public VoteSideloadList find(@RequestParam(value="creator", required=true) String creatorId,
								 @RequestParam(value="targetObject", required=false) String targetObject,
								 @RequestParam(value="objectType", required=false) String objectType) {
		VoteSideloadList responseBody = new VoteSideloadList();			
		List<Vote> votes = voteService.query(creatorId, targetObject, objectType);
		Set<Member> members = new HashSet<Member>();
		if (votes != null) {
			for (Vote vote :  votes) {
				Member member = memberService.findByMemberId(vote.getCreator());
				// we must return an empty object so Ember can pick up the json data format.  Return null will crash the ember client.
				if (member!=null)
					members.add(member);				
			}
		} else 
			// we must return an empty array so Ember can pick up the json data format.  Return null will crash the ember client.
			votes = new ArrayList<Vote>();

		responseBody.votes = votes;
		responseBody.members = new ArrayList<Member>(members);
		return responseBody;
	}	
	
	// Add New Vote
	@RequestMapping(value = "/votes", method = RequestMethod.POST, produces = "application/json")
	public Map<String, Vote> addVote(@RequestBody VoteSideload newVote) {
		Vote savedVote = voteService.addVote(newVote.vote);
		
		Map<String, Vote> responseBody = new HashMap<String, Vote>();
		responseBody.put("vote", savedVote);
		
		return responseBody;		
	}	
	
	// Update Vote
	@RequestMapping(value = "/votes/{id}", method = RequestMethod.PUT, produces = "application/json")
	public Map<String, Vote> updateVote(@PathVariable("id") String id, @RequestBody VoteSideload updatedVote) {
		Vote savedVote = voteService.updateVote(id, updatedVote.vote);
		Map<String, Vote> responseBody = new HashMap<String, Vote>();
		responseBody.put("vote", savedVote);
		
		return responseBody;			
	}
	
	// Delete Vote
	@RequestMapping (value = "/votes/{id}", method = RequestMethod.DELETE, produces = "application/json")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteVote(@PathVariable("id")String id, HttpServletResponse response) {
		if (voteService.deleteVote(id) != null)
			response.setStatus(HttpServletResponse.SC_NO_CONTENT);
		else
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
	}	
}
