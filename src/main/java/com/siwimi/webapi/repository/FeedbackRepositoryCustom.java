package com.siwimi.webapi.repository;

import java.util.List;

import com.siwimi.webapi.domain.Feedback;

public interface FeedbackRepositoryCustom {
	List<Feedback> query(String creator, String parentId, String parentType, String queryText);
}
