package com.adarp.xiwami.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adarp.xiwami.domain.Vote;
import com.adarp.xiwami.repository.VoteRepository;

@Service
public class VoteService {

	@Autowired
	VoteRepository voteRep;
	
	public Vote findByVoteId(String id) {
		return voteRep.findByIdAndIsDestroyedIsFalse(id);
	}
	
	public Vote addVote(Vote newVote) {
		newVote.setIsDestroyed(false);
		return voteRep.save(newVote);
	}
	
	public Vote updateVote(String id, Vote updatedVote) {
		updatedVote.setId(id);
		return voteRep.save(updatedVote);
	}
	
	public void deleteVote(String id) {
		Vote vote = voteRep.findOne(id);
		vote.setIsDestroyed(true);
		voteRep.save(vote);
	}
}
