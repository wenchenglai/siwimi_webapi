package com.siwimi.webapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.siwimi.webapi.domain.Vote;
import com.siwimi.webapi.repository.VoteRepository;

@Service
public class VoteService {

	@Autowired
	private VoteRepository voteRep;
	
	public List<Vote> query(String creator, String targetObject, String objectType) {
		return voteRep.queryVote(creator, targetObject, objectType);
	}
	
	public Vote findByVoteId(String id) {
		return voteRep.findByIdAndIsDeletedRecordIsFalse(id);
	}
	
	public Vote addVote(Vote newVote) {
		// Trying to search if voter gave the same vote before.
		List<Vote> existedVotes = voteRep.queryVote(newVote.getCreator(), newVote.getTargetObject(), newVote.getObjectType());
		
		if ((existedVotes != null) && (existedVotes.size() == 1)) {
			// Only one vote can be found.
			Vote existedVote = existedVotes.get(0);
			if (existedVote != null) {
				// Voter changes voteType : delete old vote, save new Vote
				if (!existedVote.getVoteType().equals(newVote.getVoteType())) {
					deleteVote(existedVote.getId());
					newVote.setIsDeletedRecord(false);
					return voteRep.save(newVote);	
				} else
					// Voter doesn't change voteType : backend no needs to do anything
					return null;
			} else {
				// Voter never voted before : save new Vote
				newVote.setIsDeletedRecord(false);
				return voteRep.save(newVote);
			}
		} else if ((existedVotes == null) || (existedVotes.isEmpty())) {
			// Voter never voted before : save new Vote
			newVote.setIsDeletedRecord(false);
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
		vote.setIsDeletedRecord(true);
		voteRep.save(vote);
	}
}
