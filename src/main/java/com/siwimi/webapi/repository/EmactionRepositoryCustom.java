package com.siwimi.webapi.repository;

import java.util.List;

import com.siwimi.webapi.domain.Emaction;

public interface EmactionRepositoryCustom {
	List<Emaction> queryEmaction(String event);
}
