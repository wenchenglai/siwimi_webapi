package com.adarp.xiwami.repository;

import java.util.List;

import com.adarp.xiwami.domain.Feedback;

public interface FeedbackRepositoryCustom {
	List<Feedback> query(String creator, String parentId, String parentType, String queryText);
}
