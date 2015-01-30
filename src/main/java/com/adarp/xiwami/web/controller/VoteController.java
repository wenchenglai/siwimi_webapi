package com.adarp.xiwami.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.adarp.xiwami.domain.Vote;
import com.adarp.xiwami.service.VoteService;
import com.adarp.xiwami.web.dto.VoteSideload;

@RestController
public class VoteController {

	@Autowired
	private VoteService voteService;
	
	// Get Vote by ID
	@RequestMapping(value = "/votes/{id}", method = RequestMethod.GET, produces = "application/json")
	public Map<String,Vote> findByVoteId(@PathVariable("id") String id) {
		Map<String,Vote> responseBody = new HashMap<String,Vote>();			
		Vote vote = voteService.findByVoteId(id);
		responseBody.put("vote", vote);
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
	public void deleteVote(@PathVariable("id")String id) {
		voteService.deleteVote(id);
	}	
}
