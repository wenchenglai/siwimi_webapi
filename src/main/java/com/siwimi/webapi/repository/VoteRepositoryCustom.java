package com.siwimi.webapi.repository;

import java.util.List;

import com.siwimi.webapi.domain.Vote;

public interface VoteRepositoryCustom {
	List<Vote> queryVote(String creator, String targetObject, String objectType);
}
