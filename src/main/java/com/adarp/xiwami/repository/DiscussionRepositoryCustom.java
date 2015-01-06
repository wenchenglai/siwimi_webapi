package com.adarp.xiwami.repository;

import java.util.List;

import com.adarp.xiwami.domain.Discussion;

public interface DiscussionRepositoryCustom {
	List<Discussion> query(String creator, String entity, String entityType, String queryText);
	//Discussion save(Discussion newDiscussion);
}
