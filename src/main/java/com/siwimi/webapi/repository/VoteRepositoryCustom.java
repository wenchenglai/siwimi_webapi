package com.siwimi.webapi.repository;

import com.siwimi.webapi.domain.Vote;

public interface VoteRepositoryCustom {
	Vote queryVote(String creator, String targetObject, String objectType);
}
