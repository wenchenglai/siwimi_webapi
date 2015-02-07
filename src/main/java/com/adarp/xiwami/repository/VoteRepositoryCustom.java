package com.adarp.xiwami.repository;

import com.adarp.xiwami.domain.Vote;

public interface VoteRepositoryCustom {
	Vote queryVote(String creator, String targetObject, String objectType);
}
