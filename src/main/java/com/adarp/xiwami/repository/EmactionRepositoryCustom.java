package com.adarp.xiwami.repository;

import java.util.List;

import com.adarp.xiwami.domain.Emaction;

public interface EmactionRepositoryCustom {
	List<Emaction> queryEmaction(String event);
}
