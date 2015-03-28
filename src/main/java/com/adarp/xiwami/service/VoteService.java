package com.adarp.xiwami.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adarp.xiwami.domain.Vote;
import com.adarp.xiwami.repository.VoteRepository;

@Service
public class VoteService {

	@Autowired
	private VoteRepository voteRep;
	
	public Vote findByVoteId(String id) {
		return voteRep.findByIdAndIsDestroyedIsFalse(id);
	}
	
	public Vote addVote(Vote newVote) {
		// Trying to search if voter gave the same vote before.
		Vote existedVote = voteRep.queryVote(newVote.getCreator(), newVote.getTargetObject(), newVote.getObjectType());
		
		if ((existedVote != null) && (!existedVote.getVoteType().equalsIgnoreCase(newVote.getVoteType()))) {
			// Voter changes voteType : delete old vote, save new Vote
			deleteVote(existedVote.getId());
			newVote.setIsDestroyed(false);
			return voteRep.save(newVote);
		} else if (existedVote == null) {
			// Voter never voted before : save new Vote
			newVote.setIsDestroyed(false);
			return voteRep.save(newVote);
		} else
			return null;
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
