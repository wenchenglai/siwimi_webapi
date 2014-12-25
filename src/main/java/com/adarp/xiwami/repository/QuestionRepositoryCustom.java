package com.adarp.xiwami.repository;

import java.util.List;

import com.adarp.xiwami.domain.Question;

public interface QuestionRepositoryCustom {
	List<Question> queryQuestion(String creatorId, Double longitude, Double latitude, String qsDistance, String queryText);
	Question saveQuestion(Question newQuestion);
}
