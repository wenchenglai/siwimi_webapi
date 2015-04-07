package com.siwimi.webapi.repository;

import java.util.List;

import com.siwimi.webapi.domain.Question;

public interface QuestionRepositoryCustom {
	List<Question> queryQuestion(String creatorId, Double longitude, Double latitude, String qsDistance, String queryText);
	Question saveQuestion(Question newQuestion);
}
